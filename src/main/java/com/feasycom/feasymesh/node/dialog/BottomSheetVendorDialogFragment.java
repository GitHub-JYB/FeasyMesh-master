package com.feasycom.feasymesh.node.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.feasycom.feasymesh.R;
import com.feasycom.feasymesh.library.utils.MeshParserUtils;
import com.feasycom.feasymesh.utils.HexKeyListener;
import com.feasycom.feasymesh.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.atomic.AtomicBoolean;


public class BottomSheetVendorDialogFragment extends BottomSheetDialogFragment {
    private static final String KEY_INDEX = "KEY_INDEX";
    private static final String MODEL_ID = "MODEL_ID";

    private int mModelId;
    private int mKeyIndex;

    private View messageContainer;
    private TextView receivedMessage;

    public interface BottomSheetVendorModelControlsListener {
        void sendVendorModelMessage(final int modelId, final int keyIndex, final int opCode, final byte[] parameters, final boolean acknowledged);
    }

    public static BottomSheetVendorDialogFragment getInstance(final int modelId, final int appKeyIndex) {
        final BottomSheetVendorDialogFragment fragment = new BottomSheetVendorDialogFragment();
        final Bundle args = new Bundle();
        args.putInt(MODEL_ID, modelId);
        args.putInt(KEY_INDEX, appKeyIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mModelId = getArguments().getInt(MODEL_ID);
            mKeyIndex = getArguments().getInt(KEY_INDEX);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View nodeControlsContainer = inflater.inflate(R.layout.layout_vendor_model_bottom_sheet, container, false);

        final CheckBox chkAcknowledged = nodeControlsContainer.findViewById(R.id.chk_acknowledged);
        final TextInputLayout opCodeLayout = nodeControlsContainer.findViewById(R.id.op_code_layout);
        final TextInputEditText opCodeEditText = nodeControlsContainer.findViewById(R.id.op_code);

        final KeyListener hexKeyListener = new HexKeyListener();

        final TextInputLayout parametersLayout = nodeControlsContainer.findViewById(R.id.parameters_layout);
        final TextInputEditText parametersEditText = nodeControlsContainer.findViewById(R.id.parameters);
        messageContainer = nodeControlsContainer.findViewById(R.id.received_message_container);
        receivedMessage = nodeControlsContainer.findViewById(R.id.received_message);
        final Button actionSend = nodeControlsContainer.findViewById(R.id.action_send);

        opCodeEditText.setKeyListener(hexKeyListener);
        opCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                opCodeLayout.setError(null);
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });

        parametersEditText.setKeyListener(hexKeyListener);
        parametersEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                parametersLayout.setError(null);
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });

        AtomicBoolean stop = new AtomicBoolean(false);
        final int[] i = {0};
        /*Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                parametersEditText.setText(autoGenericCode(String.valueOf(i[0]), 26));
                actionSend.callOnClick();
                i[0] += 1;
                if (!stop.get()){
                    handler.postDelayed(this::run, 350);
                }
            }
        };
        handler.postDelayed(runnable, 350);

        final Button stopbutton = nodeControlsContainer.findViewById(R.id.stopbutton);
        stopbutton.setOnClickListener(v -> {
            if(stop.get()){
                stop.set(false);
                Log.e("TAG", "onCreateView: 开启定时器" );
                handler.postDelayed(runnable, 350);
            }else {
                stop.set(true);
            }
        });*/


        actionSend.setOnClickListener(v -> {
            messageContainer.setVisibility(View.GONE);
            receivedMessage.setText("");
            final String opCode = opCodeEditText.getEditableText().toString().trim();

            final String parameters = parametersEditText.getEditableText().toString().trim();
            /*final String parameters1 = String.valueOf(i);
            final String parameters = autoGenericCode(parameters1, 19);*/
            Log.e("TAG", "onCreateView: "+ parameters );
            if (!validateOpcode(opCode, opCodeLayout))
                return;

            if (!validateParameters(parameters, parametersLayout))
                return;

            final byte[] params;
            if (TextUtils.isEmpty(parameters) && parameters.length() == 0) {
                params = null;
            } else {
                params = MeshParserUtils.toByteArray(parameters);
            }
            ((BottomSheetVendorModelControlsListener) requireActivity())
                    .sendVendorModelMessage(mModelId, mKeyIndex, Integer.parseInt(opCode, 16), params, chkAcknowledged.isChecked());

        });

        return nodeControlsContainer;
    }


    /**
     * 不够位数的在前面补0，保留num的长度位数字
     * @param code
     * @return
     */
    private String autoGenericCode(String code, int num) {
        String result = "";
        // 保留num的位数
        // 0 代表前面补充0
        // num 代表长度为4
        // d 代表参数为正数型
        result = String.format("%0" + num + "d", Integer.parseInt(code));

        return result;
    }

    /**
     * Validate opcode
     *
     * @param opCode       opcode
     * @param opCodeLayout op c0de view
     * @return true if success or false otherwise
     */
    private boolean validateOpcode(final String opCode, final TextInputLayout opCodeLayout) {
        try {
            if (TextUtils.isEmpty(opCode)) {
                opCodeLayout.setError(getString(R.string.error_empty_value));
                return false;
            }

            if (opCode.length() % 2 != 0 || !opCode.matches(Utils.HEX_PATTERN)) {
                opCodeLayout.setError(getString(R.string.invalid_hex_value));
                return false;
            }
            if (MeshParserUtils.isValidOpcode(Integer.valueOf(opCode, 16))) {
                return true;
            }
        } catch (NumberFormatException ex) {
            opCodeLayout.setError(getString(R.string.invalid_value));
            return false;
        } catch (IllegalArgumentException ex) {
            opCodeLayout.setError(ex.getMessage());
            return false;
        } catch (Exception ex) {
            opCodeLayout.setError(ex.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Validate parameters
     *
     * @param parameters       parameters
     * @param parametersLayout parameter view
     * @return true if success or false otherwise
     */
    private boolean validateParameters(final String parameters, final TextInputLayout parametersLayout) {
        try {
            if (TextUtils.isEmpty(parameters) && parameters.length() == 0) {
                return true;
            }

            if (parameters.length() % 2 != 0 || !parameters.matches(Utils.HEX_PATTERN)) {
                parametersLayout.setError(getString(R.string.invalid_hex_value));
                return false;
            }

            if (MeshParserUtils.isValidParameters(MeshParserUtils.toByteArray(parameters))) {
                return true;
            }
        } catch (NumberFormatException ex) {
            parametersLayout.setError(getString(R.string.invalid_value));
            return false;
        } catch (IllegalArgumentException ex) {
            parametersLayout.setError(ex.getMessage());
            return false;
        } catch (Exception ex) {
            parametersLayout.setError(ex.getMessage());
            return false;
        }
        return true;
    }

    public void setReceivedMessage(final byte[] accessPayload) {
        messageContainer.setVisibility(View.VISIBLE);
        receivedMessage.setText(MeshParserUtils.bytesToHex(accessPayload, false));
    }
}
