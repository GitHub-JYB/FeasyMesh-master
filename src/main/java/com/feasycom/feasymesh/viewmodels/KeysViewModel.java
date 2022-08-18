package com.feasycom.feasymesh.viewmodels;

import androidx.annotation.NonNull;

import com.feasycom.feasymesh.keys.AppKeysActivity;
import com.feasycom.feasymesh.keys.NetKeysActivity;

/**
 * ViewModel for {@link NetKeysActivity}, {@link AppKeysActivity}
 */
abstract class KeysViewModel extends BaseViewModel {

    KeysViewModel(@NonNull final NrfMeshRepository nrfMeshRepository) {
        super(nrfMeshRepository);
    }
}
