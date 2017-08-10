package com.mswipetech.wisepad.payment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mswipetech.wisepad.sdk.MswipeWisepadController;
import com.mswipetech.wisepad.R;
import com.mswipetech.wisepad.sdktest.data.AppPrefrences;
import com.mswipetech.wisepad.sdktest.view.ApplicationData;
import com.mswipetech.wisepad.sdktest.view.Constants;

public class PasswordChangeActivity extends Activity {

    private static final String TAG = PasswordChangeActivity.class.getSimpleName();
    EditText mTxtPassword = null;
    EditText mTxtNewPassword = null;
    EditText mTxtReTypeNewPassword = null;
    ApplicationData applicationData = null;
    private TextView textViewErrorDisplay;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        progressDialog = new ProgressDialog(PasswordChangeActivity.this);
        progressDialog.setMessage("Please wait...");
        applicationData = (ApplicationData) getApplicationContext();
        initViews();
        if(!(Constants.Reference_Id.length() !=0 && Constants.Session_Tokeniser.length()!=0))
        {
            Toast.makeText(PasswordChangeActivity.this, "", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews()
    {
        mTxtPassword = (EditText) findViewById(R.id.changepassword_TXT_password);
        mTxtNewPassword = (EditText) findViewById(R.id.changepassword_TXT_newpassword);
        mTxtReTypeNewPassword = (EditText) findViewById(R.id.changepassword_TXT_retypepassword);
        mTxtPassword.setTypeface(applicationData.font);
        mTxtNewPassword.setTypeface(applicationData.font);
        mTxtReTypeNewPassword.setTypeface(applicationData.font);
        Button submit = (Button) findViewById(R.id.changepassword_BTN_submit);
        textViewErrorDisplay = (TextView) findViewById(R.id.textViewErrorDisplay);
        submit.setTypeface(applicationData.font);
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.hideSoftInputFromWindow(tabHost.getApplicationWindowToken(), 0);
                imm.hideSoftInputFromWindow(PasswordChangeActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                if (mTxtPassword.getText().toString().trim().length() == 0)
                {
                    textViewErrorDisplay.setText(Constants.PWD_ERROR_INVALIDPWD);
                    textViewErrorDisplay.setTextColor(Constants.COLOR_RED);
                }
                else if (mTxtPassword.getText().length() < 6)
                {
                    textViewErrorDisplay.setText(Constants.PWD_ERROR_INVALIDPWDLENGTH);
                    textViewErrorDisplay.setTextColor(Constants.COLOR_RED);
                }
                else if (mTxtPassword.getText().length() > 10)
                {
                    textViewErrorDisplay.setText(Constants.PWD_ERROR_INVALIDPWDMAXLENGTH);
                    textViewErrorDisplay.setTextColor(Constants.COLOR_RED);
                }
                else if (mTxtNewPassword.getText().toString().trim().length() < 6)
                {
                    textViewErrorDisplay.setText(Constants.PWD_ERROR_INVALIDPWDNEWLENGTH);
                    textViewErrorDisplay.setTextColor(Constants.COLOR_RED);
                }
                else if (mTxtNewPassword.getText().toString().trim().length() > 10)
                {
                    textViewErrorDisplay.setText(Constants.PWD_ERROR_INVALIDPWDMAXNEWLENGTH);
                    textViewErrorDisplay.setTextColor(Constants.COLOR_RED);

                }
                else if (mTxtReTypeNewPassword.getText().toString().trim().length() < 6)
                {
                    textViewErrorDisplay.setText(Constants.PWD_ERROR_INVALIDPWDRETYPELENGTH);
                    textViewErrorDisplay.setTextColor(Constants.COLOR_RED);
                }
                else if (!mTxtReTypeNewPassword.getText().toString().trim().equals(mTxtNewPassword.getText().toString().trim()))
                {
                    textViewErrorDisplay.setText(Constants.PWD_ERROR_INVALIDNEWANDRETYPE);
                    textViewErrorDisplay.setTextColor(Constants.COLOR_RED);
                }

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                MswipeWisepadController wisepadController = new MswipeWisepadController(PasswordChangeActivity.this, AppPrefrences.getGateWayEnv(),null);
                                wisepadController.ChangePassword(changePasswordHandler, mTxtPassword.getText().toString(), mTxtNewPassword.getText().toString(), Constants.Reference_Id, Constants.Session_Tokeniser);
                                progressDialog.show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(PasswordChangeActivity.this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

    }

    public Handler changePasswordHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            progressDialog.dismiss();
            Bundle bundle = msg.getData();
            String responseMsg = bundle.getString("response");
            Log.v(TAG, " the response xml is " + responseMsg);

            String[][] strTags = new String[][]{{"status", ""}, {"ErrMsg", ""},
                    {"Session_Tokeniser", ""}, {"successMsg", ""}};
            try
            {
                Constants.parseXml(responseMsg, strTags);

                String status = strTags[0][1];
                if (status.equalsIgnoreCase("false")) {
                    String ErrMsg = strTags[1][1];
                    textViewErrorDisplay.setText(ErrMsg);
                    textViewErrorDisplay.setTextColor(Constants.COLOR_RED);

                } else if (status.equalsIgnoreCase("true")){


                    String Session_Tokeniser =  strTags[2][1];
                    Constants.Session_Tokeniser =Session_Tokeniser;
                    Log.v(TAG, " Session_Tokeniser  " + Session_Tokeniser);

                    String successMsg =  strTags[3][1];
                    setResult(RESULT_OK);
                    finish();


                }else{
                    textViewErrorDisplay.setText("Invalid response from Mswipe server, please contact support.");
                    textViewErrorDisplay.setTextColor(Constants.COLOR_RED);
                }


            }
            catch (Exception ex) {
                textViewErrorDisplay.setText("Invalid response from Mswipe server, please contact support.");
                textViewErrorDisplay.setTextColor(Constants.COLOR_RED);
            }
        }
    };

    public void doneWithChangePassword() {
        voidTrxViewDestory();
        finish();
    }

    public void voidTrxViewDestory() {
    }
}
