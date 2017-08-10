package com.mswipetech.wisepad.payment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mswipetech.wisepad.payment.fragments.FragmentLogin;
import com.mswipetech.wisepad.sdk.MswipeWisepadController;
import com.mswipetech.wisepad.R;
import com.mswipetech.wisepad.sdktest.data.AppPrefrences;
import com.mswipetech.wisepad.sdktest.view.Constants;

public class PaymentActivity extends FragmentActivity implements FragmentLogin.OnLoginCompletedListener {

    private String TAG = PaymentActivity.class.getSimpleName();
    private EditText editTextPay;
    String txt;
    SharedPreferences sharedPreferences ;
    private ProgressDialog progressDialog;
    private Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        editTextPay = (EditText) findViewById(R.id.editTextAmount);
        editTextPay.setEnabled(false);
        intent = getIntent();
        try{
            editTextPay.setText(intent.getStringExtra("amount"));
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }
        sharedPreferences = getSharedPreferences("appPreferences", Context.MODE_PRIVATE);
    }

    public void makePayment(View view) {
        txt = editTextPay.getText().toString();
        if(txt.equalsIgnoreCase(""))
        {
            Toast.makeText(PaymentActivity.this, "Enter Amount", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(Constants.Reference_Id.length() !=0 && Constants.Session_Tokeniser.length()!=0)
            {
                Intent intent = new Intent(getApplicationContext(),MSwipePaymentActivity.class);
                intent.putExtra("amount",Double.parseDouble(txt));
                startActivity(intent);
                finish();
            }
            else
            {
                //Constants.showDialog(MenuView.this, "SDk List", "Please login first to perform the card sale.", 1);
                if(!(sharedPreferences.getString("userId","").equalsIgnoreCase("")))
                {
                    // Do Auto Authentication
                    validate(sharedPreferences.getString("userId",""),sharedPreferences.getString("userPass",""));

                }
                else
                {
                    promptLoginFragment();
                }
            }
        }
    }

    public void cancelPayment(View view) {
        finish();
    }

    private void promptLoginFragment() {
        FragmentLogin alertdFragment = new FragmentLogin();
        alertdFragment.setCancelable(false);
        alertdFragment.show(getSupportFragmentManager(), "LoginDialog");
    }

    public void validate(String name, String password) {
        MswipeWisepadController wisepadController = new MswipeWisepadController(PaymentActivity.this, AppPrefrences.getGateWayEnv(),null);
        wisepadController.AuthenticateMerchant(loginHandler, name, password);
        progressDialog = new ProgressDialog(PaymentActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
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
                    Toast.makeText(PaymentActivity.this, ErrMsg, Toast.LENGTH_SHORT).show();
                    promptLoginFragment();

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
                        changePassword();

                    }
                    else
                    {
                        // Login Succes Save in shared preferences and Proceed for Payment
                        /*SharedPreferences sharedPreferences = getSharedPreferences("appPreferences",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userId",editTextUserName.getText().toString().trim());
                        editor.putString("userPass",editTextUserPass.getText().toString().trim());
                        editor.apply();*/
                        loginCompleted();
                    }

                }else{
                    Toast.makeText(PaymentActivity.this, "Invalid response from Mswipe server, please contact support.", Toast.LENGTH_SHORT).show();

                }

            }
            catch (Exception ex) {
                Toast.makeText(PaymentActivity.this, "Invalid response from Mswipe server, please contact support.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onLoginCompleted(boolean success) {
        loginCompleted();
    }

    private void loginCompleted() {
        Intent intent = new Intent(getApplicationContext(),MSwipePaymentActivity.class);
        intent.putExtra("amount",Double.parseDouble(txt));
        startActivity(intent);
        finish();
    }

    @Override
    public void onChangePassword(boolean success) {
        changePassword();
    }

    private void changePassword() {
        Intent intent = new Intent(getApplicationContext(),PasswordChangeActivity.class);
        intent.putExtra("amount",Double.parseDouble(txt));
        startActivity(intent);
    }
}
