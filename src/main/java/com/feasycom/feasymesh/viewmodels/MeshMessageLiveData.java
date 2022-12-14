package com.feasycom.feasymesh.viewmodels;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.feasycom.feasymesh.library.transport.MeshMessage;


public class MeshMessageLiveData extends SingleLiveEvent<MeshMessage> {

    @Override
    public void postValue(final MeshMessage value) {
        super.postValue(value);
    }

    @Override
    @MainThread
    public void observe(@NonNull final LifecycleOwner owner, @NonNull final Observer<? super MeshMessage> observer) {
        super.observe(owner, observer);
    }
}
