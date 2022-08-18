package com.feasycom.feasymesh.dialog;

import androidx.annotation.NonNull;

import com.feasycom.feasymesh.library.NetworkKey;

public interface NetKeyListener {

    void onKeyUpdated(@NonNull final NetworkKey key);

    void onKeyNameUpdated(@NonNull final String nodeName);
}
