/***************************************************************************
 * Project Name		:	VAJRA
 * <p>
 * File Name		:	DatabaseHandler
 * <p>
 * DateOfCreation	:	13-October-2012
 * <p>
 * Author			:	Balasubramanya Bharadwaj B S
 **************************************************************************/
package com.wepindia.pos;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mswipetech.wisepad.sdktest.view.ApplicationData;
import com.wep.common.app.Database.BillSetting;
import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.WepBaseActivity;
import com.wep.common.app.utils.Preferences;

import com.wepindia.pos.GenericClasses.MessageDialog;

import java.util.Calendar;

import java.util.Locale;

@SuppressWarnings("Since15")
public class LoginActivity extends WepBaseActivity {

    // DatabaseHandler object
    DatabaseHandler dbLogin = new DatabaseHandler(LoginActivity.this);
    // MessageDialog Object
    MessageDialog MsgBox;

    // View handling variables
    EditText txtUserId, txtPassword;
    Button btnDateDisplay, btnMonthDisplay, btnYearDisplay, btnLogin, btnClose;

    // Class Variables
    private static final int HOME_RESULT = 1;
    //private static final String FILE_SHARED_PREFERENCE = "WeP_FnB";
    Calendar calDate;
    // Variables - BillSettings object
    BillSetting objBillSettings = new BillSetting();
    private SharedPreferences sharedPreferences;
    private AppCompatCheckBox appCompatCheckBox;

    @TargetApi(9)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            MsgBox = new MessageDialog(this);
            calDate = Calendar.getInstance();
            txtUserId = (EditText) findViewById(R.id.txtUserId);
            txtPassword = (EditText) findViewById(R.id.txtPassword);
            btnDateDisplay = (Button) findViewById(R.id.btnDateDisplay);
            btnMonthDisplay = (Button) findViewById(R.id.btnMonthDisplay);
            btnYearDisplay = (Button) findViewById(R.id.btnYearDisplay);
            btnLogin = (Button) findViewById(R.id.btnLogin);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLogin(v);
                }
            });
            btnClose = (Button) findViewById(R.id.btnClose);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Close(v);
                }
            });
            appCompatCheckBox = (AppCompatCheckBox) findViewById(R.id.checkboxRememberMe);
            btnDateDisplay.setText(String.valueOf(calDate.get(Calendar.DAY_OF_MONTH)));
            btnMonthDisplay.setText(calDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US));
            btnYearDisplay.setText(String.valueOf(calDate.get(Calendar.YEAR)));

            dbLogin.CreateDatabase();
            dbLogin.OpenDatabase();



        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        initSinglePrinter();
    }


    private void initSinglePrinter() {
        sharedPreferences = Preferences.getSharedPreferencesForPrint(LoginActivity.this); // getSharedPreferences("PrinterConfigurationActivity", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("kot","Heyday");
        editor.putString("bill","Heyday");
        editor.putString("report","Heyday");
        editor.putString("receipt","Heyday");
        editor.commit();

        appCompatCheckBox.setChecked(sharedPreferences.getBoolean("isChecked",false));
        txtUserId.setText(sharedPreferences.getString("userNameTxt",""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Login Activity", "OnStart() Event");
    }

    // About button
    public void About(View v) {
        String strAboutMsg = "WeP Touch POS\nVersion:1.1.0\n\nAbout WeP Solutions Limited." +
                "\n\n\tWeP Digital is the Digital Services arm of WeP Solutions Limited (WeP). WeP is an innovative, reliable and a dynamic company. WeP came into being as a result of entrepreneurial work culture in Wipro. It has a committed and experienced team, helping the company grow leaps and bounds. We have grown and diversified, having spread our roots in an array of different areas like Managed Printing Solutions (MPS), Manufacturing and distribution of IT peripherals, Retail Billing solutions, and Document Management Solutions." +
                "\n\n\tWeP has been a very dynamic and adaptable organization. We keep reinventing ourselves to adapt to the ever-changing technology by introducing new products to the market, based on the need of the hour. We are a self-reliant company for technology for both new products and its manufacturing." +
                "\n\n\tWe are the pioneers of retail printers and billing solutions. We have a large presence in the retail automation space in select segments. We have introduced a lot of innovative products in the retail space which help the small time store owners as well as high-end supermarkets to support their customers. We have innovative business models suiting all kinds of consumers based on their requirements. We are known for our reliability and dependability in the market. We are a pan-India company. So our clients can seek our support across the country and we will be there to serve them.";
        AlertDialog.Builder PickUpTender = new AlertDialog.Builder(this);
        PickUpTender
                .setIcon(R.drawable.ic_launcher)
                .setTitle("About")
                .setMessage(strAboutMsg)
                .setNeutralButton("OK", null)
                .show();
    }

    // Login button event

    public void onLogin(View view) {
        String userNameTxt = txtUserId.getText().toString();
        String userPassTxt = txtPassword.getText().toString();
        if(userNameTxt.equalsIgnoreCase("") || userPassTxt.equalsIgnoreCase(""))
        {
            MsgBox.Show("Login", "Please enter a Username & Password");
        }
        else
        {
            if(appCompatCheckBox.isChecked()){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userNameTxt",userNameTxt);
                editor.putBoolean("isChecked",true);
                editor.commit();
            }
            Cursor User = dbLogin.getUser(userNameTxt,userPassTxt);
            if (User != null) {
                if (User.moveToFirst()) {
                    Intent intentHomeScreen = new Intent(this, HomeActivity.class);
                    Intent intentOwnerDetail = new Intent(this, OwnerDetailsActivity.class);
                    String userId = User.getString(User.getColumnIndex("UserId"));
                    String userName = User.getString(User.getColumnIndex("Name"));
                    String userRole = User.getString(User.getColumnIndex("RoleId"))+"";

                    ApplicationData.savePreference(this,ApplicationData.USER_ID,userId);
                    ApplicationData.savePreference(this,ApplicationData.USER_NAME,userName);
                    ApplicationData.savePreference(this,ApplicationData.USER_ROLE,userRole);

                    //startActivity(intentHomeScreen);
                    Cursor cursor = dbLogin.getAllBillDetail();
                    if(cursor!=null && cursor.moveToFirst())
                        startActivity(intentHomeScreen);
                    else
                        startActivity(intentOwnerDetail);

                    finish();

                } else {
                    MsgBox.Show("Login", "Wrong user id or password");
                }
            } else {
                MsgBox.Show("Login", "Login DB Error");
            }
        }
    }

    // Close button event
    public void Close(View v) {
        // Close Database connection
        dbLogin.CloseDatabase();

        // Close the application
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == HOME_RESULT) {
                this.finish();
            } else {
                this.txtUserId.setText("");
                this.txtPassword.setText("");
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(this);
            AuthorizationDialog
                    .setTitle("Are you sure you want to exit ?")
                    .setIcon(R.drawable.ic_launcher)
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dbLogin.CloseDatabase();
                            finish();
                        }
                    })
                    .show();
        }

        return super.onKeyDown(keyCode, event);
    }

    /*public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }*/

    @Override
    public void onHomePressed()
    {}
}
