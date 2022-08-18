/*
 * Copyright (c) 2018, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.feasycom.feasymesh.viewmodels;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.ParcelUuid;
import android.util.Log;

import androidx.annotation.NonNull;

import com.feasycom.feasymesh.ble.BleMeshManager;
import com.feasycom.feasymesh.library.MeshBeacon;
import com.feasycom.feasymesh.library.MeshManagerApi;
import com.feasycom.feasymesh.library.MeshNetwork;
import com.feasycom.feasymesh.library.transport.ProvisionedMeshNode;
import com.feasycom.feasymesh.library.utils.MeshParserUtils;
import com.feasycom.feasymesh.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanRecord;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

/**
 * Repository for scanning for bluetooth mesh devices
 * @author LY
 */
public class ScannerRepository {

    private static final String TAG = ScannerRepository.class.getSimpleName();
    private final Context mContext;
    private final MeshManagerApi mMeshManagerApi;
    private String mNetworkId;

    /**
     * MutableLiveData containing the scanner state to notify MainActivity.
     */
    private final ScannerLiveData mScannerLiveData;
    private final ScannerStateLiveData mScannerStateLiveData;

    private UUID mFilterUuid;
    private Set<String> meshProvisioningAddress = new HashSet<>();
    private Set<String> meshProxyAddress = new HashSet<>();
    private final ScanCallback mScanCallbacks = new ScanCallback() {
        @Override
        public void onScanResult(final int callbackType, @NonNull final ScanResult result) {

            try{
                if (mFilterUuid.equals(BleMeshManager.MESH_PROVISIONING_UUID)) {
                    if (result.getScanRecord() != null){
                        if (result.getScanRecord().getServiceUuids() != null){
                            if(result.getScanRecord().getServiceUuids().get(0).toString().equals(BleMeshManager.MESH_PROVISIONING_UUID.toString())){
                                if (!meshProvisioningAddress.contains(result.getDevice().getAddress())){
                                    meshProvisioningAddress.add(result.getDevice().getAddress());
                                }
                                updateScannerLiveData(result);
                            }
                        }else {
                            if (meshProvisioningAddress.contains(result.getDevice().getAddress())){
                                updateScannerLiveData(result);
                            }
                        }
                    }
                }else if (mFilterUuid.equals(BleMeshManager.MESH_PROXY_UUID)) {
                    final byte[] serviceData = Utils.getServiceData(result, BleMeshManager.MESH_PROXY_UUID);
                    if (mMeshManagerApi != null) {
                        if (mMeshManagerApi.isAdvertisingWithNetworkIdentity(serviceData)) {
                            if (mMeshManagerApi.networkIdMatches(mNetworkId, serviceData)) {
                                updateScannerLiveData(result);
                            }
                        } else if (mMeshManagerApi.isAdvertisedWithNodeIdentity(serviceData)) {
                            if (checkIfNodeIdentityMatches(serviceData)) {
                                updateScannerLiveData(result);
                            }
                        }
                    }
                }
            }catch (Exception ex) {
                Log.i(TAG, "Error1: " + ex.getMessage());
            }
        }

        @Override
        public void onBatchScanResults(@NonNull final List<ScanResult> results) {
            // Batch scan is disabled (report delay = 0)
        }

        @Override
        public void onScanFailed(final int errorCode) {
            mScannerStateLiveData.scanningStopped();
        }
    };

    /**
     * Broadcast receiver to monitor the changes in the location provider
     */
    private final BroadcastReceiver mLocationProviderChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final boolean enabled = Utils.isLocationEnabled(context);

            mScannerStateLiveData.setLocationEnabled(enabled);
        }
    };
    /**
     * Broadcast receiver to monitor the changes in the bluetooth adapter
     */
    private final BroadcastReceiver mBluetoothStateBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
            final int previousState = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, BluetoothAdapter.STATE_OFF);

            switch (state) {
                case BluetoothAdapter.STATE_ON:
                    mScannerStateLiveData.bluetoothEnabled();
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                case BluetoothAdapter.STATE_OFF:
                    if (previousState != BluetoothAdapter.STATE_TURNING_OFF && previousState != BluetoothAdapter.STATE_OFF) {
                        stopScan();
                        mScannerStateLiveData.bluetoothDisabled();
                    }
                    break;
            }
        }
    };

    @Inject
    public ScannerRepository(final Context context, final MeshManagerApi meshManagerApi) {
        this.mContext = context;
        this.mMeshManagerApi = meshManagerApi;
        mScannerStateLiveData = new ScannerStateLiveData(Utils.isBleEnabled(), Utils.isLocationEnabled(context));
        mScannerLiveData = new ScannerLiveData();
    }

    public ScannerStateLiveData getScannerState() {
        return mScannerStateLiveData;
    }

    public ScannerLiveData getScannerResults() {
        return mScannerLiveData;
    }

    private void updateScannerLiveData(final ScanResult result) {
       /* if (result.getDevice() .getName()== null && result.getScanRecord().getDeviceName() == null){
            return;
        }*/
        final ScanRecord scanRecord = result.getScanRecord();
        // Log.e(TAG, "updateScannerLiveData: " + MeshParserUtils.bytesToHex(scanRecord.getBytes(), false) );
        if (scanRecord != null) {
            if (scanRecord.getBytes() != null) {
                final byte[] beaconData = mMeshManagerApi.getMeshBeaconData(scanRecord.getBytes());
                if (beaconData != null) {
                    mScannerLiveData.deviceDiscovered(result, mMeshManagerApi.getMeshBeacon(beaconData));
                } else {
                    // mScannerLiveData.deviceDiscovered(result);
                }
                mScannerStateLiveData.deviceFound();
            }
        }
    }

    /**
     * Register for required broadcast receivers.
     */
    void registerBroadcastReceivers() {
        mContext.registerReceiver(mBluetoothStateBroadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        if (Utils.isMarshmallowOrAbove()) {
            mContext.registerReceiver(mLocationProviderChangedReceiver, new IntentFilter(LocationManager.MODE_CHANGED_ACTION));
        }
    }

    /**
     * Unregister for required broadcast receivers.
     */
    void unregisterBroadcastReceivers() {
        mContext.unregisterReceiver(mBluetoothStateBroadcastReceiver);
        if (Utils.isMarshmallowOrAbove()) {
            mContext.unregisterReceiver(mLocationProviderChangedReceiver);
        }
    }

    /**
     * Start scanning for Bluetooth devices.
     *
     * @param filterUuid UUID to filter scan results with
     */
    public void startScan(final UUID filterUuid, boolean auto) {
        mFilterUuid = filterUuid;
        if (mScannerStateLiveData.isScanning()) {
            return;
        }

        if (mFilterUuid.equals(BleMeshManager.MESH_PROXY_UUID)) {
            final MeshNetwork network = mMeshManagerApi.getMeshNetwork();
            if (network != null) {
                if (!network.getNetKeys().isEmpty()) {
                    mNetworkId = mMeshManagerApi.generateNetworkId(network.getNetKeys().get(0).getKey());
                }
            }
        }
        mScannerStateLiveData.scanningStarted();
        //Scanning settings
        final ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                // Refresh the devices list every second
                .setReportDelay(0)
                // Hardware filtering has some issues on selected devices
                .setUseHardwareFilteringIfSupported(true)
                // Samsung S6 and S6 Edge report equal value of RSSI for all devices. In this app we ignore the RSSI.
                /*.setUseHardwareBatchingIfSupported(false)*/
                .build();

        //Let's use the filter to scan only for unprovisioned mesh nodes.
        /*final List<ScanFilter> filters = new ArrayList<>();
        filters.add(new ScanFilter.Builder().setServiceUuid(new ParcelUuid((filterUuid))).build());*/

        final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        scanner.startScan(null, settings, mScanCallbacks);
    }

    /**
     * stop scanning for bluetooth devices.
     */
    public void stopScan() {
        final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        scanner.stopScan(mScanCallbacks);
        mScannerStateLiveData.scanningStopped();
    }

    /**
     * Check if node identity matches
     *
     * @param serviceData service data received from the advertising data
     * @return true if the node identity matches or false otherwise
     */
    private boolean checkIfNodeIdentityMatches(final byte[] serviceData) {
        final MeshNetwork network = mMeshManagerApi.getMeshNetwork();
        if (network != null) {
            for (ProvisionedMeshNode node : network.getNodes()) {
                if (mMeshManagerApi.nodeIdentityMatches(node, serviceData)) {
                    return true;
                }
            }
        }
        return false;
    }
}