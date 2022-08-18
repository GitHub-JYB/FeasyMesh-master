package com.feasycom.feasymesh.viewmodels;

import javax.inject.Inject;

import androidx.annotation.NonNull;

import com.feasycom.feasymesh.keys.AppKeysActivity;

/**
 * ViewModel for {@link AppKeysActivity}
 */
public class NetKeysViewModel extends KeysViewModel {

    @Inject
    NetKeysViewModel(@NonNull final NrfMeshRepository nrfMeshRepository) {
        super(nrfMeshRepository);
    }
}
