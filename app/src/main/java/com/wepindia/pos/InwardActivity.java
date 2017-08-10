package com.wepindia.pos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.mswipetech.wisepad.sdktest.view.ApplicationData;
import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.WepBaseActivity;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.utils.ActionBarUtils;

import java.util.Date;

public class InwardActivity extends WepBaseActivity {

    // Context object
    Context myContext;
    String  strUserId = "",strUserName = "";
    DatabaseHandler dbInward = null;
    MessageDialog MsgBox;
    private Toolbar toolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inward);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myContext = this;
        strUserName = getIntent().getStringExtra("USER_NAME");
        strUserId = ApplicationData.getUserId(this);

        try {
            MsgBox = new MessageDialog(myContext);
            getDb().CloseDatabase();
            getDb().CreateDatabase();
            getDb().OpenDatabase();
            Date d = new Date();
            CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
            com.wep.common.app.ActionBarUtils.setupToolbar(this,toolbar,getSupportActionBar(),"Inward Module",strUserName," Date:"+s.toString());

        }
        catch (Exception e)
        {
            MsgBox.Show("Exception", e.getMessage());
            e.printStackTrace();
        }

    }

    public DatabaseHandler getDb(){
        if(dbInward ==null){
            dbInward = new DatabaseHandler(this);
            try{
                dbInward.OpenDatabase();
            }catch (Exception e){

            }
        }
        return dbInward;
    }



    public void LaunchActivity(View v){
        if (v.getContentDescription().toString().equalsIgnoreCase("PurchaseOrder"))
        {
            Intent intentPO = new Intent(myContext, PurchaseOrderActivity.class);
            intentPO.putExtra("USER_ID", strUserId);
            intentPO.putExtra("USER_NAME", strUserName);
            startActivityForResult(intentPO,1);

        } else if(v.getContentDescription().toString().equalsIgnoreCase("GoodInwardNote")){
            // Launch stock activity
            Intent intentGoodsInwardNote = new Intent(myContext,GoodsInwardNoteActivity.class);
            intentGoodsInwardNote.putExtra("USER_NAME", strUserName);
            intentGoodsInwardNote.putExtra("USER_ID", strUserId);
            startActivity(intentGoodsInwardNote);
        } else if (v.getContentDescription().toString().equalsIgnoreCase("Ingredients")) {

            Intent intentDelivery = new Intent(myContext, IngredientManagementActivity.class);
            intentDelivery.putExtra("USER_ID", strUserId);//spUser.getString("USER_ID", "GHOST"));
            intentDelivery.putExtra("USER_NAME", strUserName);//spUser.getString("USER_NAME", "GHOST"));
            intentDelivery.putExtra("CUST_ID", 0);
            startActivity(intentDelivery);

        }
        else if (v.getContentDescription().toString().equalsIgnoreCase("SupplierDetails")) {

            Intent intentSupplierDetails = new Intent(myContext, SupplierDetailsActivity.class);
            intentSupplierDetails.putExtra("USER_ID", strUserId);//spUser.getString("USER_ID", "GHOST"));
            intentSupplierDetails.putExtra("USER_NAME", strUserName);//spUser.getString("USER_NAME", "GHOST"));
            intentSupplierDetails.putExtra("CUST_ID", 0);
            startActivity(intentSupplierDetails);

        }
        else if (v.getContentDescription().toString().equalsIgnoreCase("InwardItemEntry")) {

            Intent intentSupplierDetails = new Intent(myContext, InwardItemActivity.class);
            intentSupplierDetails.putExtra("USER_ID", strUserId);//spUser.getString("USER_ID", "GHOST"));
            intentSupplierDetails.putExtra("USER_NAME", strUserName);//spUser.getString("USER_NAME", "GHOST"));
            intentSupplierDetails.putExtra("CUST_ID", 0);
            startActivity(intentSupplierDetails);

        }else if (v.getContentDescription().toString().equalsIgnoreCase("SupplierItemLinkage")) {

            Intent intentDelivery = new Intent(myContext, ItemSupplierLinkageActivity.class);
            intentDelivery.putExtra("USER_ID", strUserId);//spUser.getString("USER_ID", "GHOST"));
            intentDelivery.putExtra("USER_NAME", strUserName);//spUser.getString("USER_NAME", "GHOST"));
            intentDelivery.putExtra("CUST_ID", 0);
            startActivity(intentDelivery);

        }
        else if (v.getContentDescription().toString().equalsIgnoreCase("Inwards")) {

            Intent intentDelivery = new Intent(myContext, TabbedSupplierItemLinkage.class);
            intentDelivery.putExtra("USER_ID", strUserId);//spUser.getString("USER_ID", "GHOST"));
            intentDelivery.putExtra("USER_NAME", strUserName);//spUser.getString("USER_NAME", "GHOST"));
            intentDelivery.putExtra("CUST_ID", 0);
            startActivity(intentDelivery);

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

