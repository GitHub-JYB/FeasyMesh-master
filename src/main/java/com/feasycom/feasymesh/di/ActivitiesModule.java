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

package com.feasycom.feasymesh.di;

import com.feasycom.feasymesh.GroupControlsActivity;
import com.feasycom.feasymesh.MainActivity;
import com.feasycom.feasymesh.ProvisioningActivity;
import com.feasycom.feasymesh.SplashScreenActivity;
import com.feasycom.feasymesh.ble.ReconnectActivity;
import com.feasycom.feasymesh.ble.ScannerActivity;
import com.feasycom.feasymesh.keys.AddAppKeyActivity;
import com.feasycom.feasymesh.keys.AddAppKeysActivity;
import com.feasycom.feasymesh.keys.AddNetKeyActivity;
import com.feasycom.feasymesh.keys.AddNetKeysActivity;
import com.feasycom.feasymesh.keys.AppKeysActivity;
import com.feasycom.feasymesh.keys.EditAppKeyActivity;
import com.feasycom.feasymesh.keys.EditNetKeyActivity;
import com.feasycom.feasymesh.keys.NetKeysActivity;
import com.feasycom.feasymesh.light.LightActivity;
import com.feasycom.feasymesh.node.ConfigurationClientActivity;
import com.feasycom.feasymesh.node.ConfigurationServerActivity;
import com.feasycom.feasymesh.node.GenericLevelServerActivity;
import com.feasycom.feasymesh.node.GenericOnOffServerActivity;
import com.feasycom.feasymesh.node.ModelConfigurationActivity;
import com.feasycom.feasymesh.node.NodeConfigurationActivity;
import com.feasycom.feasymesh.node.NodeDetailsActivity;
import com.feasycom.feasymesh.node.PublicationSettingsActivity;
import com.feasycom.feasymesh.node.VendorModelActivity;
import com.feasycom.feasymesh.provisioners.AddProvisionerActivity;
import com.feasycom.feasymesh.provisioners.EditProvisionerActivity;
import com.feasycom.feasymesh.provisioners.ProvisionersActivity;
import com.feasycom.feasymesh.provisioners.RangesActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivitiesModule {

    @ContributesAndroidInjector()
    abstract SplashScreenActivity contributeSplashScreenActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector()
    abstract ProvisionersActivity contributeProvisionersActivity();

    @ContributesAndroidInjector()
    abstract AddProvisionerActivity contributeAddProvisionersActivity();

    @ContributesAndroidInjector()
    abstract EditProvisionerActivity contributeEditProvisionersActivity();

    @ContributesAndroidInjector()
    abstract RangesActivity contributeRangesActivity();

    @ContributesAndroidInjector()
    abstract NetKeysActivity contributeNetKeysActivity();

    @ContributesAndroidInjector()
    abstract AddNetKeyActivity contributeAddNetKeyActivity();

    @ContributesAndroidInjector()
    abstract EditNetKeyActivity contributeEditNetKeyActivity();

    @ContributesAndroidInjector()
    abstract AppKeysActivity contributeAppKeysActivity();

    @ContributesAndroidInjector()
    abstract AddAppKeyActivity contributeAddAppKeyActivity();

    @ContributesAndroidInjector()
    abstract EditAppKeyActivity contributeEditAppKeyActivity();

    @ContributesAndroidInjector()
    abstract ProvisioningActivity contributeMeshProvisionerActivity();

    @ContributesAndroidInjector()
    abstract NodeConfigurationActivity contributeElementConfigurationActivity();

    @ContributesAndroidInjector()
    abstract AddAppKeysActivity contributeAddAppKeysActivity();

    @ContributesAndroidInjector()
    abstract AddNetKeysActivity contributeAddNetKeysActivity();

    @ContributesAndroidInjector()
    abstract ScannerActivity contributeScannerActivity();

    @ContributesAndroidInjector()
    abstract ReconnectActivity contributeReconnectActivity();

    @ContributesAndroidInjector()
    abstract NodeDetailsActivity contributeNodeDetailsActivity();

    @ContributesAndroidInjector()
    abstract GroupControlsActivity contributeGroupControlsActivity();

    @ContributesAndroidInjector()
    abstract PublicationSettingsActivity contributePublicationSettingsActivity();

    @ContributesAndroidInjector()
    abstract ConfigurationServerActivity contributeConfigurationServerActivity();

    @ContributesAndroidInjector()
    abstract ConfigurationClientActivity contributeConfigurationClientActivity();

    @ContributesAndroidInjector()
    abstract GenericOnOffServerActivity contributeGenericOnOffServerActivity();

    @ContributesAndroidInjector()
    abstract GenericLevelServerActivity contributeGenericLevelServerActivity();

    @ContributesAndroidInjector()
    abstract VendorModelActivity contributeVendorModelActivity();

    @ContributesAndroidInjector()
    abstract ModelConfigurationActivity contributeModelConfigurationActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule_2.class)
    abstract LightActivity contributeLightActivity();

}
