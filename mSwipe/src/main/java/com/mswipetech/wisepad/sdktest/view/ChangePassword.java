package com.mswipetech.wisepad.sdktest.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mswipetech.wisepad.sdk.MswipeWisepadController;
import com.mswipetech.wisepad.R;
import com.mswipetech.wisepad.sdktest.data.AppPrefrences;



public class ChangePassword extends BaseTitleActivity {
    public final static String log_tab = "ChangePassword=>";
    CustomProgressDialog mProgressActivity = null;

    //fields for credit sale screen
    EditText mTxtPassword = null;
    EditText mTxtNewPassword = null;
    EditText mTxtReTypeNewPassword = null;

    ApplicationData applicationData = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);
        applicationData = (ApplicationData) getApplicationContext();

        initViews();
      
    }

    @Override
    public void onResume() {

        super.onResume();

    }

    private void initViews()
    {
        ((TextView) findViewById(R.id.topbar_LBL_heading)).setText("Password");
        ((TextView) findViewById(R.id.topbar_LBL_heading)).setTypeface(applicationData.font);


        mTxtPassword = (EditText) findViewById(R.id.changepassword_TXT_password);
        mTxtNewPassword = (EditText) findViewById(R.id.changepassword_TXT_newpassword);
        mTxtReTypeNewPassword = (EditText) findViewById(R.id.changepassword_TXT_retypepassword);

        mTxtPassword.setTypeface(applicationData.font);
        mTxtNewPassword.setTypeface(applicationData.font);
        mTxtReTypeNewPassword.setTypeface(applicationData.font);

        Button submit = (Button) findViewById(R.id.changepassword_BTN_submit);
        submit.setTypeface(applicationData.font);
        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.hideSoftInputFromWindow(tabHost.getApplicationWindowToken(), 0);
                imm.hideSoftInputFromWindow(ChangePassword.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                if (mTxtPassword.getText().toString().trim().length() == 0) {
                    Constants.showDialog(ChangePassword.this, Constants.PWD_DIALOG_MSG, Constants.PWD_ERROR_INVALIDPWD, 1);


                    mTxtPassword.requestFocus();
                    return;
                } else if (mTxtPassword.getText().length() < 6) {
                    Constants.showDialog(ChangePassword.this, Constants.PWD_DIALOG_MSG, Constants.PWD_ERROR_INVALIDPWDLENGTH, 1);

                    mTxtPassword.requestFocus();
                    return;
                } else if (mTxtPassword.getText().length() > 10) {
                    Constants.showDialog(ChangePassword.this, Constants.PWD_DIALOG_MSG, Constants.PWD_ERROR_INVALIDPWDMAXLENGTH, 1);

                    mTxtPassword.requestFocus();
                    return;
                } else if (mTxtNewPassword.getText().toString().trim().length() < 6) {
                    Constants.showDialog(ChangePassword.this, Constants.PWD_DIALOG_MSG, Constants.PWD_ERROR_INVALIDPWDNEWLENGTH, 1);
                    mTxtNewPassword.requestFocus();
                    return;
                } else if (mTxtNewPassword.getText().toString().trim().length() > 10) {
                    Constants.showDialog(ChangePassword.this, Constants.PWD_DIALOG_MSG, Constants.PWD_ERROR_INVALIDPWDMAXNEWLENGTH, 1);
                    mTxtNewPassword.requestFocus();
                    return;

                } else if (mTxtReTypeNewPassword.getText().toString().trim().length() < 6) {
                    Constants.showDialog(ChangePassword.this, Constants.PWD_DIALOG_MSG, Constants.PWD_ERROR_INVALIDPWDRETYPELENGTH, 1);
                    mTxtReTypeNewPassword.requestFocus();
                    return;
                } else if (!mTxtReTypeNewPassword.getText().toString().trim().equals(mTxtNewPassword.getText().toString().trim())) {
                    Constants.showDialog(ChangePassword.this, Constants.PWD_DIALOG_MSG, Constants.PWD_ERROR_INVALIDNEWANDRETYPE, 1);
                    mTxtReTypeNewPassword.requestFocus();
                    return;
                }


                final Dialog dialog = Constants.showDialog(ChangePassword.this, Constants.PWD_DIALOG_MSG, Constants.PWD_CHANGEPWD_CONFIRMATION, "2");
                Button yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
                yes.setTypeface(applicationData.font);
                yes.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        dialog.dismiss();


                        MswipeWisepadController wisepadController = new MswipeWisepadController(ChangePassword.this, AppPrefrences.getGateWayEnv(),null);
                        wisepadController.ChangePassword(changePasswordHandler, mTxtPassword.getText().toString(), mTxtNewPassword.getText().toString(),
                        		Constants.Reference_Id, Constants.Session_Tokeniser);
                        mProgressActivity = new CustomProgressDialog(ChangePassword.this, "Processing password...");
                        mProgressActivity.show();
   


                    }
                });

                Button no = (Button) dialog.findViewById(R.id.bmessageDialogNo);
                no.setTypeface(applicationData.font);
                no.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });
                dialog.show();


            }
        });

    }

    public Handler changePasswordHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
                 mProgressActivity.dismiss();
            Bundle bundle = msg.getData();
            String responseMsg = bundle.getString("response");
            Log.v(ApplicationData.packName, log_tab + " the response xml is " + responseMsg);

            String[][] strTags = new String[][]{{"status", ""}, {"ErrMsg", ""},
            		{"Session_Tokeniser", ""}, {"successMsg", ""}};
            try
            {
                Constants.parseXml(responseMsg, strTags);

                String status = strTags[0][1];
                if (status.equalsIgnoreCase("false")) {
                    String ErrMsg = strTags[1][1];
                    Constants.showDialog(ChangePassword.this, "ChangePwdView", ErrMsg, 1);

                } else if (status.equalsIgnoreCase("true")){

    
                    String Session_Tokeniser =  strTags[2][1];
                    Constants.Session_Tokeniser =Session_Tokeniser;
                    Log.v(ApplicationData.packName, log_tab + " Session_Tokeniser  " + Session_Tokeniser);
                    
                    String successMsg =  strTags[3][1];
                    final Dialog dlg = Constants.showDialog(ChangePassword.this, "ChangePwdView", successMsg, "1");
                    Button btnOk = (Button) dlg.findViewById(R.id.bmessageDialogYes);
                    btnOk.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							dlg.dismiss();
							
							setResult(RESULT_OK);
							finish();
							
						}
					});
                    dlg.show();
              

                }else{
                    Constants.showDialog(ChangePassword.this, "ChangePwdView", "Invalid response from Mswipe server, please contact support.", 1);
                }


            }
            catch (Exception ex) {
                Constants.showDialog(ChangePassword.this, "ChangePwdView", "Invalid response from Mswipe server, please contact support.", 1);
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
