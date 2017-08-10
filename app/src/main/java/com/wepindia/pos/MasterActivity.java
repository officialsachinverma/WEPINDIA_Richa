/****************************************************************************
 * Project Name		:	VAJRA
 *
 * File Name		:	MasterActivity
 *
 * Purpose			:	Represents Master options activity, takes care of all
 * 						UI back end operations in this activity, such as event
 * 						handling data read from or display in views.
 *
 * DateOfCreation	:	27-November-2012
 *
 * Author			:	Balasubramanya Bharadwaj B S
 *
 ****************************************************************************/
package com.wepindia.pos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.WepBaseActivity;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.utils.ActionBarUtils;

import java.util.Date;

public class MasterActivity extends WepBaseActivity {

    // Context object
    Context myContext;
    String strUserName = "";
    TextView txtUserName;
    DatabaseHandler dbMaster= null;
    Cursor crsrSettings = null;
    String GSTEnable = "";
    // MessageDialog object
    MessageDialog MsgBox;
    private Toolbar toolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.application_title_bar);
        TextView tvTitleText = (TextView)findViewById(R.id.tvTitleBarCaption);
        tvTitleText.setText("Masters");*/

        myContext = this;
        strUserName = getIntent().getStringExtra("USER_NAME");

        txtUserName = (TextView) findViewById(R.id.txtMasterUserName);
        txtUserName.setText(strUserName.toUpperCase());
        try {
            MsgBox = new MessageDialog(myContext);
            getDb().CloseDatabase();
            getDb().CreateDatabase();
            getDb().OpenDatabase();
            crsrSettings = getDb().getBillSetting();

            if ((crsrSettings != null) && crsrSettings.moveToFirst()) {
                GSTEnable = crsrSettings.getString(crsrSettings.getColumnIndex("GSTEnable"));
                if (GSTEnable == null) {
                    GSTEnable = "0";
                }

            }
        }
        catch (Exception e)
        {
            MsgBox.Show("Exception", e.getMessage());
            e.printStackTrace();
        }
        Date d = new Date();
        CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
        com.wep.common.app.ActionBarUtils.setupToolbar(this,toolbar,getSupportActionBar(),"Masters",strUserName," Date:"+s.toString());
    }

    public DatabaseHandler getDb(){
        if(dbMaster==null){
            dbMaster = new DatabaseHandler(this);
            try{
                dbMaster.OpenDatabase();
            }catch (Exception e){

            }
        }
        return dbMaster;
    }



    public void LaunchActivity(View v){
        if(v.getContentDescription().toString().equalsIgnoreCase("Users")){
            // Launch users activity
            Intent intentRoles = new Intent(myContext,AddRolesActivity.class);
            intentRoles.putExtra("USER_NAME", strUserName);
            startActivity(intentRoles);
//			startActivity(new Intent(myContext,AddRolesActivity.class));

        } else if(v.getContentDescription().toString().equalsIgnoreCase("Configurations")){
            // Launch item configuration activity
            Intent intentConfig = new Intent(myContext,ConfigurationActivity.class);
            intentConfig.putExtra("USER_NAME", strUserName);
            startActivity(intentConfig);
//			startActivity(new Intent(myContext,ConfigurationActivity.class));

        } else if(v.getContentDescription().toString().equalsIgnoreCase("Item")){
            // Launch item management activity
            Intent intentItems = new Intent(myContext,ItemManagementActivity.class);
            intentItems.putExtra("USER_NAME", strUserName);
            startActivity(intentItems);
//			startActivity(new Intent(myContext,ItemManagementActivity.class));

        }  else if(v.getContentDescription().toString().equalsIgnoreCase("PriceStock")){
            // Launch stock activity
            Intent intentStock = new Intent(myContext,StockActivity.class);
            intentStock.putExtra("USER_NAME", strUserName);
            startActivity(intentStock);
//			startActivity(new Intent(myContext,StockActivity.class));

        } else if (v.getContentDescription().toString().equalsIgnoreCase("Inward")) {
            // Launch Billing screen activity in Delivery billing mode

            Intent intentInward = new Intent(myContext, InwardActivity.class);
            intentInward.putExtra("USER_NAME", strUserName);
            startActivity(intentInward);

        } else if(v.getContentDescription().toString().equalsIgnoreCase("Employee")){
            // Launch employee activity
            Intent intentUsers = new Intent(myContext,UserManagementActivity.class);
            intentUsers.putExtra("USER_NAME", strUserName);
            startActivity(intentUsers);
//			startActivity(new Intent(myContext,UserManagementActivity.class));

        } else if(v.getContentDescription().toString().equalsIgnoreCase("Customers")){
            // Launch customer detail activity
            Intent intentCustomer = new Intent(myContext,CustomerDetailActivity.class);
            intentCustomer.putExtra("USER_NAME", strUserName);
            startActivity(intentCustomer);
//			startActivity(new Intent(myContext,CustomerDetailActivity.class));

        } else if(v.getContentDescription().toString().equalsIgnoreCase("Settings")){
            // Launch settings activity
            Intent intentSettings = new Intent(myContext,TabbedSettingsActivity.class);
            intentSettings.putExtra("USER_NAME", strUserName);
            startActivity(intentSettings);
//			startActivity(new Intent(myContext,SettingsActivity.class));

        } else {
            // Close master options window and get back to Home screen
            Intent returnIntent =new Intent();
            setResult(Activity.RESULT_OK,returnIntent);
            this.finish();
        }
    }

    public void Screenshot(View v){
        ActionBarUtils.takeScreenshot(this, findViewById(R.id.imgMasterScreenshot), findViewById(R.id.MasterParent));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            /*AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);
            LayoutInflater UserAuthorization = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vwAuthorization = UserAuthorization.inflate(R.layout.user_authorization, null);
            final EditText txtUserId = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserId);
            final EditText txtPassword = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserPassword);
            final TextView tvAuthorizationUserId= (TextView) vwAuthorization.findViewById(R.id.tvAuthorizationUserId);
            final TextView tvAuthorizationUserPassword= (TextView) vwAuthorization.findViewById(R.id.tvAuthorizationUserPassword);
            tvAuthorizationUserId.setVisibility(View.GONE);
            tvAuthorizationUserPassword.setVisibility(View.GONE);
            txtUserId.setVisibility(View.GONE);
            txtPassword.setVisibility(View.GONE);
            AuthorizationDialog
                    .setTitle("Do you want to go back ")
                    .setView(vwAuthorization)
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent returnIntent =new Intent();
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();
                        }
                    })
                    .show(); */
                    Intent returnIntent =new Intent();
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.wep.common.app.R.menu.menu_wep_base, menu);
        for (int j = 0; j < menu.size(); j++) {
            MenuItem item = menu.getItem(j);
            item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return true;
    }

    @Override
    public void onHomePressed() {
        ActionBarUtils.navigateHome(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            getDb().CloseDatabase();
            finish();
        }else if (id == com.wep.common.app.R.id.action_screen_shot) {

        }else if (id == com.wep.common.app.R.id.action_logout) {
            Intent intentResult = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intentResult);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

