package com.mswipetech.wisepad.payment.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.mswipetech.wisepad.R;
import com.mswipetech.wisepad.sdk.MswipeWisePadGatewayConnectionListener;
import com.mswipetech.wisepad.sdk.MswipeWisepadController;
import com.mswipetech.wisepad.sdktest.data.AppPrefrences;
import com.mswipetech.wisepad.sdktest.view.ApplicationData;
import com.mswipetech.wisepad.sdktest.view.Constants;

public class FragmentLogin extends DialogFragment {

    private static final String TAG = FragmentLogin.class.getSimpleName();
    private Button btnCancel,btnOkay;
    private EditText editTextUserName,editTextUserPass;
    private OnLoginCompletedListener completedListener;
    private ApplicationData applicationData;
    private WisePadGatewayConncetionListener messagelistener;
    private ProgressDialog progressDialog;
    private TextView textViewValidationMsg;
    private CheckBox checkboxRemember;


    public FragmentLogin() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        applicationData = (ApplicationData) getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.fragment_fragment_login, container, false);
        //getDialog().requestWindowFeature(STYLE_NO_TITLE);
        getDialog().setTitle("MSwipe Login");
        completedListener = (OnLoginCompletedListener) getActivity();
        editTextUserName = (EditText) view.findViewById(R.id.editTextUserName);
        editTextUserPass = (EditText) view.findViewById(R.id.editTextUserPass);
        textViewValidationMsg = (TextView) view.findViewById(R.id.textViewValidationMsg);
        checkboxRemember = (CheckBox) view.findViewById(R.id.checkboxRemember);
        onCreatemSwipe(view);
        return view;
    }

    public void onCreatemSwipe(View view){
        messagelistener = new WisePadGatewayConncetionListener();
        MswipeWisepadController wisepadController = new MswipeWisepadController(getActivity(), AppPrefrences.getGateWayEnv(),null);
        wisepadController.startMswipeGatewayConnection();
        editTextUserName.setTypeface(applicationData.font);
        editTextUserPass.setTypeface(applicationData.font);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentLogin.this.dismiss();
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
                    // On Login Success call completedListener.onLoginCompleted(true) and save credentials in SharedPreferences
                    validate(txtUser, txtPass);
                }
            }
        });
    }

    public void validate(String name, String password) {
        if ((name.equals("")))
        {
            textViewValidationMsg.setText("Please enter a valid User Id and Password.");
            textViewValidationMsg.setTextColor(Constants.COLOR_RED);

        }
        else if (password.equals(""))
        {
            textViewValidationMsg.setText("Please enter a valid User Id and Password.");
            textViewValidationMsg.setTextColor(Constants.COLOR_RED);
        }
        else if (password.length() < 6)
        {
            textViewValidationMsg.setText("Minimum length of the password should be 6 characters.");
            textViewValidationMsg.setTextColor(Constants.COLOR_RED);
        }
        else if (password.length() > 10)
        {
            textViewValidationMsg.setText("Length of the password should be between 6 and 10 characters.");
            textViewValidationMsg.setTextColor(Constants.COLOR_RED);
        }
        else
        {
            MswipeWisepadController wisepadController = new MswipeWisepadController(getActivity(), AppPrefrences.getGateWayEnv(),null);
            wisepadController.AuthenticateMerchant(loginHandler, name, password);
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }
    }

    public Handler loginHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg) {
            progressDialog.dismiss();
            Bundle bundle = msg.getData();
            String responseMsg = bundle.getString("response");
            Log.v(TAG, " the response xml is " + responseMsg);

            String[][] strTags = new String[][]{{"status", ""}, {"ErrMsg", ""}, {"IS_Password_Changed", ""}, {"First_Name", ""},
                    {"Reference_Id", ""}, {"Session_Tokeniser", ""}, {"Currency_Code", ""},{"IS_TIP_REQUIRED",""}, {"CONVENIENCE_PERCENTAGE",""},{"SERVICE_TAX",""}};
            try
            {
                Constants.parseXml(responseMsg, strTags);
                String status = strTags[0][1];
                if (status.equalsIgnoreCase("false"))
                {
                    String ErrMsg = strTags[1][1];
                    //Constants.showDialog(LoginView.this, "LoginView", ErrMsg, 1);
                    textViewValidationMsg.setText(ErrMsg);
                    textViewValidationMsg.setTextColor(Constants.COLOR_RED);
                }
                else if (status.equalsIgnoreCase("true"))
                {

                    String FirstName =  strTags[3][1];
                    Log.v(TAG, " FirstName  " + FirstName);

                    String Reference_Id =  strTags[4][1];
                    Log.v(TAG, " Reference_Id  " + Reference_Id);

                    String Session_Tokeniser =  strTags[5][1];
                    Log.v(TAG, " Session_Tokeniser  " + Session_Tokeniser);

                    String Currency_Code =  strTags[6][1];
                    Log.v(TAG, " Currency_Code  " + Currency_Code);

                    String tipRequired =  strTags[7][1];
                    Log.v(TAG, " tipRequired  " + tipRequired);

                    String convienencePercentage = strTags[8][1];
                    Log.v(TAG, " convienencePercentage  " + convienencePercentage);

                    String serviceTax = strTags[9][1];
                    Log.v(TAG, " serviceTax  " + serviceTax);

                    //save the referebceId and Session_Tokeniser for passing to the other web servives
                    Constants.Reference_Id = Reference_Id;
                    Constants.Session_Tokeniser = Session_Tokeniser;
                    Constants.Currency_Code = Currency_Code+".";
                    Constants.mTipRequired = tipRequired;
                    Constants.mConveniencePercentage = convienencePercentage == null ? "0" :convienencePercentage;
                    Constants.mServiceTax = serviceTax == null ? "0" : serviceTax;

                    String IS_Password_Changed = strTags[2][1];

                    if (IS_Password_Changed.equalsIgnoreCase("false"))
                    {
                        completedListener.onChangePassword(true);
                        FragmentLogin.this.dismiss();

                    }
                    else
                    {
                        // Login Succes Save in shared preferences and Proceed for Payment
                        if(checkboxRemember.isChecked()) {
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("appPreferences",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userId",editTextUserName.getText().toString().trim());
                            editor.putString("userPass",editTextUserPass.getText().toString().trim());
                            editor.apply();
                        }
                        completedListener.onLoginCompleted(true);
                        FragmentLogin.this.dismiss();
                    }

                }else{
                    textViewValidationMsg.setText("Invalid response from Mswipe server, please contact support.");
                    textViewValidationMsg.setTextColor(Constants.COLOR_RED);

                }

            }
            catch (Exception ex) {
                textViewValidationMsg.setText("Invalid response from Mswipe server, please contact support.");
                textViewValidationMsg.setTextColor(Constants.COLOR_RED);
            }
        }
    };

    public interface OnLoginCompletedListener {
        public void onLoginCompleted(boolean success);
        public void onChangePassword(boolean success);
    }
    class WisePadGatewayConncetionListener implements MswipeWisePadGatewayConnectionListener {

        @Override
        public void Connected(String msg) {
            //((TextView)findViewById(R.id.topbar_LBL_status)).setText(msg);
        }

        @Override
        public void Connecting(String msg) {
            //((TextView)findViewById(R.id.topbar_LBL_status)).setText(msg);
        }

        @Override
        public void disConnect(String msg) {
            //((TextView)findViewById(R.id.topbar_LBL_status)).setText(msg);
        }
    }
}
