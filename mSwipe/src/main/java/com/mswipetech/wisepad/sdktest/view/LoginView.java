package com.mswipetech.wisepad.sdktest.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.mswipetech.wisepad.sdk.MswipeWisePadGatewayConnectionListener;
import com.mswipetech.wisepad.sdk.MswipeWisepadController;
import com.mswipetech.wisepad.R;
import com.mswipetech.wisepad.sdktest.data.AppPrefrences;
import com.mswipetech.wisepad.sdktest.view.CreditSaleView.WisePadGatewayConncetionListener;

public class LoginView extends BaseTitleActivity {
    public final static String log_tab = "LoginView=>";

    TextView mTxtUsername = null;
    TextView mTxtPassword = null;
    CustomProgressDialog mProgressActivity = null;
    ApplicationData applicationData = null;

    private WisePadGatewayConncetionListener messagelistener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applicationData = (ApplicationData) getApplicationContext();
        setContentView(R.layout.login_activity);
        
        messagelistener = new WisePadGatewayConncetionListener();
        
    	MswipeWisepadController wisepadController = new MswipeWisepadController(this, AppPrefrences.getGateWayEnv(),null);
    	wisepadController.startMswipeGatewayConnection();
    	
    	initViews();

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void initViews() {
    	
        mTxtUsername = (TextView) findViewById(R.id.login_TXT_merchantid);
        mTxtPassword = (TextView) findViewById(R.id.login_TXT_merchantpassword);      

        mTxtUsername.setTypeface(applicationData.font);
        mTxtPassword.setTypeface(applicationData.font);
        
        TextView txtHeading = (TextView) findViewById(R.id.topbar_LBL_heading);
        txtHeading.setText("Login");
        txtHeading.setTypeface(applicationData.font);

        Button btnLogin = (Button) findViewById(R.id.login_BTN_signin);
        btnLogin.setTypeface(applicationData.font);

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.hideSoftInputFromWindow(tabHost.getApplicationWindowToken(), 0);
                imm.hideSoftInputFromWindow(LoginView.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                validate(mTxtUsername.getText().toString(), mTxtPassword.getText().toString());

            }
        });


    }


    public void validate(String name, String password) {
        //Dialog.show("Login", _name + " " + _password, "Ok", null);
        if ((name.equals(""))) {
            Constants.showDialog(LoginView.this, Constants.LOGIN_DIALOG_MSG, Constants.LOGIN_ERROR_ValidUserMsg, 1);
            mTxtUsername.requestFocus();

        } else if (password.equals("")) {
            Constants.showDialog(LoginView.this, Constants.LOGIN_DIALOG_MSG, Constants.LOGIN_ERROR_ValidUserMsg, 1);

            mTxtPassword.requestFocus();

        } else if (password.length() < 6) {
            Constants.showDialog(LoginView.this, Constants.LOGIN_DIALOG_MSG, Constants.PWD_ERROR_INVALIDPWDLENGTH, 1);

            mTxtPassword.requestFocus();
        } else if (password.length() > 10) {
            Constants.showDialog(LoginView.this, Constants.LOGIN_DIALOG_MSG, Constants.PWD_ERROR_INVALIDPWDMAXLENGTH, 1);
            mTxtPassword.requestFocus();

        } else {
        	MswipeWisepadController wisepadController = new MswipeWisepadController(this, AppPrefrences.getGateWayEnv(),null);
            wisepadController.AuthenticateMerchant(loginHandler, name, password);
            mProgressActivity = new CustomProgressDialog(LoginView.this, "Logging in...");
            mProgressActivity.show();

        }

    }

    public Handler loginHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg) {
            mProgressActivity.dismiss();
            Bundle bundle = msg.getData();
            String responseMsg = bundle.getString("response");
            Log.v(ApplicationData.packName, log_tab + " the response xml is " + responseMsg);

            String[][] strTags = new String[][]{{"status", ""}, {"ErrMsg", ""}, {"IS_Password_Changed", ""}, {"First_Name", ""},
                    {"Reference_Id", ""}, {"Session_Tokeniser", ""}, {"Currency_Code", ""},{"IS_TIP_REQUIRED",""}, {"CONVENIENCE_PERCENTAGE",""},{"SERVICE_TAX",""}};
            try
            {
                Constants.parseXml(responseMsg, strTags);

                String status = strTags[0][1];
                if (status.equalsIgnoreCase("false")) {
                    String ErrMsg = strTags[1][1];
                    Constants.showDialog(LoginView.this, "LoginView", ErrMsg, 1);

                } else if (status.equalsIgnoreCase("true")){

                    String FirstName =  strTags[3][1];
                    Log.v(ApplicationData.packName, log_tab + " FirstName  " + FirstName);

                    String Reference_Id =  strTags[4][1];
                    Log.v(ApplicationData.packName, log_tab + " Reference_Id  " + Reference_Id);

                    String Session_Tokeniser =  strTags[5][1];
                    Log.v(ApplicationData.packName, log_tab + " Session_Tokeniser  " + Session_Tokeniser);
                    
                    String Currency_Code =  strTags[6][1];
                    Log.v(ApplicationData.packName, log_tab + " Currency_Code  " + Currency_Code);

                    String tipRequired =  strTags[7][1];
                    Log.v(ApplicationData.packName, log_tab + " tipRequired  " + tipRequired);

                    String convienencePercentage = strTags[8][1];
                    Log.v(ApplicationData.packName, log_tab + " convienencePercentage  " + convienencePercentage);

                    String serviceTax = strTags[9][1];
                    Log.v(ApplicationData.packName, log_tab + " serviceTax  " + serviceTax);

                    //save the referebceId and Session_Tokeniser for passing to the other web servives
                    Constants.Reference_Id = Reference_Id;
                    Constants.Session_Tokeniser = Session_Tokeniser;
                    Constants.Currency_Code = Currency_Code+".";
                    Constants.mTipRequired = tipRequired;
                    Constants.mConveniencePercentage = convienencePercentage == null ? "0" :convienencePercentage;
                    Constants.mServiceTax = serviceTax == null ? "0" : serviceTax;
                    
                    String IS_Password_Changed = strTags[2][1];
                    
                    if (IS_Password_Changed.equalsIgnoreCase("false")) {
                        startActivity(new Intent(LoginView.this, ChangePassword.class));
                        finish();
                        return;
                        
                    }else{
                    	
                        final Dialog dlg = Constants.showDialog(LoginView.this, "LoginView", "User authenticated.", "1");
                        Button btnOk = (Button) dlg.findViewById(R.id.bmessageDialogYes);
                        btnOk.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								dlg.dismiss();
								
								finish();
								
							}
						});
                        dlg.show();
                                         	
                    }

                }else{
                    Constants.showDialog(LoginView.this, "LoginView", "Invalid response from Mswipe server, please contact support.", 1);
                }

            }
            catch (Exception ex) {
                Constants.showDialog(LoginView.this, "LoginView", "Invalid response from Mswipe server, please contact support.", 1);
            }
        }
    };
    
    class WisePadGatewayConncetionListener implements  MswipeWisePadGatewayConnectionListener{

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