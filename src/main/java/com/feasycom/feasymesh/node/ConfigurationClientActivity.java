package com.feasycom.feasymesh.node;

import android.os.Bundle;

import com.feasycom.feasymesh.library.models.ConfigurationClientModel;
import com.feasycom.feasymesh.library.transport.MeshModel;


public class ConfigurationClientActivity extends BaseModelConfigurationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final MeshModel model = mViewModel.getSelectedModel().getValue();
        if (model instanceof ConfigurationClientModel) {
            disableClickableViews();
        }
    }
}
