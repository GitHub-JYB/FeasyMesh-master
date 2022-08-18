package com.feasycom.feasymesh.viewmodels;

import javax.inject.Inject;

import androidx.annotation.NonNull;

import com.feasycom.feasymesh.keys.AppKeysActivity;

/**
 * ViewModel for {@link AppKeysActivity}
 */
public class EditNetKeyViewModel extends KeysViewModel {

    @Inject
    EditNetKeyViewModel(@NonNull final NrfMeshRepository nrfMeshRepository) {
        super(nrfMeshRepository);
    }
}
