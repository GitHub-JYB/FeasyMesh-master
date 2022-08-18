package com.feasycom.feasymesh.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;

import com.feasycom.feasymesh.library.transport.ConfigAppKeyGet;
import com.feasycom.feasymesh.library.transport.ProvisionedMeshNode;
import com.feasycom.feasymesh.library.utils.MeshParserUtils;
import com.feasycom.feasymesh.keys.AppKeysActivity;
import com.feasycom.feasymesh.keys.NetKeysActivity;

import java.util.LinkedList;
import java.util.Queue;

import javax.inject.Inject;

/**
 * ViewModel for {@link NetKeysActivity}, {@link AppKeysActivity}
 */
public class AddKeysViewModel extends KeysViewModel {

    private Queue<ConfigAppKeyGet> messageQueue = new LinkedList<>();

    @Inject
    AddKeysViewModel(@NonNull final NrfMeshRepository nrfMeshRepository) {
        super(nrfMeshRepository);
    }

    public Queue<ConfigAppKeyGet> getMessageQueue() {
        return messageQueue;
    }

    /**
     * Checks if the key has been added to the node
     *
     * @param keyIndex index of the key
     * @return true if added or false otherwise
     */
    public boolean isNetKeyAdded(final int keyIndex) {
        final ProvisionedMeshNode node = getSelectedMeshNode().getValue();
        if (node != null) {
            return MeshParserUtils.isNodeKeyExists(node.getAddedNetKeys(), keyIndex);
        }
        return false;
    }

    /**
     * Checks if the key has been added to the node
     *
     * @param keyIndex index of the key
     * @return true if added or false otherwise
     */
    public boolean isAppKeyAdded(final int keyIndex) {
        final ProvisionedMeshNode node = getSelectedMeshNode().getValue();
        if (node != null) {
            return MeshParserUtils.isNodeKeyExists(node.getAddedAppKeys(), keyIndex);
        }
        return false;
    }

    private static final String TAG = "AddKeysViewModel";

}
