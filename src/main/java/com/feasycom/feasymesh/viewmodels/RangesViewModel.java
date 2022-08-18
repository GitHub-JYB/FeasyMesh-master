package com.feasycom.feasymesh.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.feasycom.feasymesh.library.Provisioner;

import javax.inject.Inject;

public class RangesViewModel extends BaseViewModel {

    /**
     * Constructs {@link BaseViewModel}
     *
     * @param nRfMeshRepository Mesh Repository {@link NrfMeshRepository}
     */
    @Inject
    RangesViewModel(@NonNull final NrfMeshRepository nRfMeshRepository) {
        super(nRfMeshRepository);
    }

    public LiveData<Provisioner> getSelectedProvisioner() {
        return mNrfMeshRepository.getSelectedProvisioner();
    }

    public void setSelectedProvisioner(@NonNull final Provisioner provisioner) {
        mNrfMeshRepository.setSelectedProvisioner(provisioner);
    }
}
