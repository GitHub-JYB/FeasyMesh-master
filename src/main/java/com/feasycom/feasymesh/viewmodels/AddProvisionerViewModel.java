package com.feasycom.feasymesh.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.feasycom.feasymesh.library.Provisioner;
import com.feasycom.feasymesh.keys.AppKeysActivity;

import javax.inject.Inject;

/**
 * ViewModel for {@link AppKeysActivity}
 */
public class AddProvisionerViewModel extends BaseViewModel {

    @Inject
    AddProvisionerViewModel(@NonNull final NrfMeshRepository nrfMeshRepository) {
        super(nrfMeshRepository);
        mNrfMeshRepository.clearTransactionStatus();
    }

    public void setSelectedProvisioner(@NonNull final Provisioner provisioner) {
        mNrfMeshRepository.setSelectedProvisioner(provisioner);
    }

    public LiveData<Provisioner> getSelectedProvisioner() {
        return mNrfMeshRepository.getSelectedProvisioner();
    }
}
