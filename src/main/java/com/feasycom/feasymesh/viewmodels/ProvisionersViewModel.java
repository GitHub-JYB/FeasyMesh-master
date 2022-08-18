package com.feasycom.feasymesh.viewmodels;

import androidx.annotation.NonNull;

import com.feasycom.feasymesh.library.Provisioner;
import com.feasycom.feasymesh.keys.AppKeysActivity;

import javax.inject.Inject;

/**
 * ViewModel for {@link AppKeysActivity}
 */
public class ProvisionersViewModel extends BaseViewModel {

    @Inject
    ProvisionersViewModel(@NonNull final NrfMeshRepository nrfMeshRepository) {
        super(nrfMeshRepository);
    }

    public void setSelectedProvisioner(@NonNull final Provisioner provisioner) {
        mNrfMeshRepository.setSelectedProvisioner(provisioner);
    }
}
