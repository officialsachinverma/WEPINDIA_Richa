package com.wepindia.pos.GST.fragments;


import android.app.DialogFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mswipetech.wisepad.sdktest.view.Constants;
import com.wepindia.pos.GSTLinkActivity_Unused;
import com.wepindia.pos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthFragment extends DialogFragment {

    private EditText editTextUserName,editTextUserPass;
    private Button btnCancel,btnOkay;
    private TextView textViewValidationMsg;
    private OnLocalAuthCompletedListener onLocalAuthCompletedListener;
    private int code;


    public AuthFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_conf_dialog, container, false);
        editTextUserName = (EditText) view.findViewById(R.id.editTextUserName);
        editTextUserPass = (EditText) view.findViewById(R.id.editTextUserPass);
        textViewValidationMsg = (TextView) view.findViewById(R.id.textViewVal);
        onLocalAuthCompletedListener = (OnLocalAuthCompletedListener) getActivity();
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthFragment.this.dismiss();
            }
        });
        btnOkay = (Button) view.findViewById(R.id.btnLogin);
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtUser = editTextUserName.getText().toString().trim();
                String txtPass = editTextUserPass.getText().toString().trim();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.hideSoftInputFromWindow(tabHost.getApplicationWindowToken(), 0);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                if(txtUser.equalsIgnoreCase("") || txtPass.equalsIgnoreCase(""))
                {
                    textViewValidationMsg.setText("Enter username & password");
                    textViewValidationMsg.setTextColor(Constants.COLOR_RED);
                }
                else
                {
                    Cursor User = ((GSTLinkActivity_Unused)getActivity()).dbGSTLink.getUser(txtUser, txtPass);
                    if(User!=null) {
                        if (User.getCount() > 0) {
                            if(User.moveToFirst()){
                                onLocalAuthCompletedListener.onLocalAuthCompleted(true,code,User.getString(User.getColumnIndex("Name")));
                            }
                            AuthFragment.this.dismiss();
                        }else {
                            textViewValidationMsg.setText("Invalid username & password");
                            textViewValidationMsg.setTextColor(Constants.COLOR_RED);
                        }
                    }
                }
            }
        });
        return view;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public interface OnLocalAuthCompletedListener {
        public void onLocalAuthCompleted(boolean success, int code, String userName);
    }

}
