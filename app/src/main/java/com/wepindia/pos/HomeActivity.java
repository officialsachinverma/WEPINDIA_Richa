package com.wepindia.pos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mswipetech.wisepad.sdktest.view.ApplicationData;
import com.wep.common.app.Database.BillSetting;
import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.WepBaseActivity;
import com.wep.gstcall.api.http.HTTPAsyncTask;
import com.wep.gstcall.api.util.Config;
import com.wepindia.pos.GenericClasses.BillNoReset;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.utils.ActionBarUtils;
import com.wepindia.pos.utils.StockInwardMaintain;
import com.wepindia.pos.utils.StockOutwardMaintain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends WepBaseActivity implements HTTPAsyncTask.OnHTTPRequestCompletedListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    Context myContext;
    Date objDate;
    MessageDialog MsgBox;
    private static final String DINEIN = "1";
    private static final String TAKEAWAY = "2";
    private static final String PICKUP = "3";
    private static final String DELIVERY = "4";
    public static int Upload_Invoice_Count = 1010;
    String strUserId = "", strUserName = "";
    int strUserRole = 0;
    RelativeLayout rl_dinein, rl_CounterSales,rl_pickup,rl_delivery,rl_inward_invoice_entry,rl_amend,rl_cdn;
    TextView txtUserName;
    TextView tvDineInOption, tvCounterSalesOption, tvPickUpOption1, tvDeliveryOption;
    BillSetting objBillSettings;
    private Toolbar toolbar;
	ArrayList<String> listAccesses ;
    Cursor settingcrsr;
    CharSequence s;
    DatabaseHandler dbHomeScreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myContext = this;
        try {

            MsgBox = new MessageDialog(myContext);
            strUserId = ApplicationData.getUserId(this);//ApplicationData.USER_ID;
            strUserName = ApplicationData.getUserName(this);//ApplicationData.USER_NAME;
            strUserRole = Integer.valueOf(ApplicationData.getUserRole(this));

            getDb().CreateDatabase();
            listAccesses = getDb().getPermissionsNamesForRole(getDb().getRoleName(strUserRole+""));

            InitializeViews();
            Display();
            checkForAutoDayEnd(); // called after display because settingcrsr is being set in Display()
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Date d = new Date();
        s = DateFormat.format("dd-MM-yyyy", d.getTime());
        com.wep.common.app.ActionBarUtils.setupToolbarMenu(this,toolbar,getSupportActionBar(),"Home",strUserName," Date:"+s.toString());
    }

    public DatabaseHandler getDb(){
        if(dbHomeScreen==null){
            dbHomeScreen = new DatabaseHandler(HomeActivity.this);
            try{
                dbHomeScreen.OpenDatabase();
            }catch (Exception e){

            }
        }
        return dbHomeScreen;
    }
    private void checkForAutoDayEnd()
    {
        if(settingcrsr!=null)
        {
            int autoDayend = settingcrsr.getInt(settingcrsr.getColumnIndex("DateAndTime"));
            if(autoDayend== 1) //DateChange -> 1 - auto, 0 = manual
            {
                String businessdate = settingcrsr.getString(settingcrsr.getColumnIndex("BusinessDate"));
                Date d = new Date();
                String currentdate  = String.valueOf(DateFormat.format("dd-MM-yyyy", d.getTime()));

                if(!businessdate.equals(currentdate))
                {
                    // needs to change to current date
                    int iResult = 0;


                    iResult = getDb().updateBusinessDate(currentdate);
                    Log.d("AutoDayEnd", "BusinessDate set to "+currentdate+". Status of updation : " + iResult);
                    StockOutwardMaintain stock_outward = new StockOutwardMaintain(myContext, getDb());
                    stock_outward.saveOpeningStock_Outward(currentdate);

                    StockInwardMaintain stock_inward = new StockInwardMaintain(myContext, getDb());
                    stock_inward.saveOpeningStock_Inward(currentdate);
                    // delete all pending KOTS
                    iResult = 0;
                    iResult = getDb().deleteAllKOTItems();
                    Log.d("AutoDayEnd", "Items deleted from Pending KOT status:" + iResult);

                    iResult =0;
                    iResult = getDb().deleteAllVoidedKOT();
                    Log.d("AutoDayEnd", "Items deleted from Void KOT status: :" + iResult);

                    // delete All reserved tables
                    iResult = 0;
                    iResult = getDb().deleteAllReservedTables();
                    Log.d("AutoDayEnd", "No of Reserved Tables deleted for Past :" + iResult);

                    // reset KOT No
                    iResult = 0;
                    long Result = getDb().updateKOTNo(0);
                    Log.d("AutoDayEnd", "KOT No reset to 0 status :"+Result);

                    BillNoReset bs = new BillNoReset();
                    bs.setBillNo(dbHomeScreen);

                    try
                    {
                        Date dd = new SimpleDateFormat("dd-MM-yyyy").parse(businessdate);
                        long milli = dd.getTime();
                        Cursor cursor = dbHomeScreen.getInvoice_outward(Long.toString(milli));
                        if(cursor!=null && cursor.moveToNext())
                        {
                            int billcount = cursor.getCount();
                            Cursor cursor_owner = dbHomeScreen.getOwnerDetail();
                            if(cursor_owner!= null && cursor_owner.moveToNext())
                            {
                                String deviceid = cursor_owner.getString(cursor_owner.getColumnIndex("DeviceId"));
                                String deviceName = cursor_owner.getString(cursor_owner.getColumnIndex("DeviceName"));
                                String Email = cursor_owner.getString(cursor_owner.getColumnIndex("Email"));
                                String paramStr ="data="+deviceid+","+Email+","+businessdate+","+billcount+","+deviceName;
                                new HTTPAsyncTask(HomeActivity.this,HTTPAsyncTask.HTTP_GET,"",Upload_Invoice_Count, Config.Upload_No_of_Invoices+paramStr).execute();
                            }
                            else
                            {
                                Log.d("TAG", "Cannot upload invoices count due to insufficient owners details");
                            }

                        }else
                        {
                            Toast.makeText(myContext, "No Invoice count to send for businessDate :"+businessdate, Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "No Invoice count to send for businessDate :"+businessdate);
                        }

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }

            }
            else {
                Log.d("HomeActivity", "day end is manual");
            }
        }
    }

@Override
    protected void onResume() {
        super.onResume();
    listAccesses = getDb().getPermissionsNamesForRole(getDb().getRoleName(strUserRole+""));
    BillNoReset bs = new BillNoReset();
    bs.setBillNo(dbHomeScreen);
    }

    public boolean isAccessable(String type){
        return listAccesses.contains(listAccesses);
    }

    public void Display()
    {
        settingcrsr = getDb().getBillSetting();

        if (settingcrsr!=null && settingcrsr.moveToNext()) {

            // displaying Captions as per settings
            String DineInCaption = settingcrsr.getString(settingcrsr.getColumnIndex("HomeDineInCaption"));
            String CounterSalesCaption = settingcrsr.getString(settingcrsr.getColumnIndex("HomeCounterSalesCaption"));
            String TakeAwayCaption = settingcrsr.getString(settingcrsr.getColumnIndex("HomeTakeAwayCaption"));
            String HomeDeliveryCaption = settingcrsr.getString(settingcrsr.getColumnIndex("HomeHomeDeliveryCaption"));

            if (DineInCaption != null) {
                tvDineInOption.setText(DineInCaption);
            }
            if (CounterSalesCaption != null) {
                tvCounterSalesOption.setText(CounterSalesCaption);
            }
            if (TakeAwayCaption != null) {
                tvPickUpOption1.setText(TakeAwayCaption);
            }
            if (HomeDeliveryCaption != null) {
                tvDeliveryOption.setText(HomeDeliveryCaption);
            }
        }
    }
    void InitializeViews()
    {
        tvDeliveryOption = (TextView)findViewById(R.id.tvDeliveryOption);
        tvDineInOption = (TextView) findViewById(R.id.tvDineInOption);
        tvCounterSalesOption = (TextView) findViewById(R.id.tvCounterSalesOption);
        tvPickUpOption1 = (TextView) findViewById(R.id.tvPickUpOption1);
    }



    @SuppressWarnings("deprecation")
    private void DayEnd_new()
    {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");


    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            return  dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            //btnDate.setText(ConverterDate.ConvertDate(year, month + 1, day));
            Toast.makeText(getContext(), ""+year, Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), ""+month, Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), ""+day, Toast.LENGTH_SHORT).show();
        }
    }
    private void DayEnd() {
        try {

            AlertDialog.Builder DayEndDialog = new AlertDialog.Builder(myContext);
            final DatePicker dateNextDate = new DatePicker(myContext);

            Cursor BusinessDate = getDb().getCurrentDate();
            String date_str = "";


            if (BusinessDate.moveToFirst()) {
                date_str = BusinessDate.getString(BusinessDate.getColumnIndex("BusinessDate"));
            } else {
                objDate = new Date();
            }
            final String date_str1 = date_str;
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date d = (Date) formatter.parse(date_str);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);
            dateNextDate.updateDate(year, month, day + 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                Calendar prevDate = Calendar.getInstance();
                prevDate.add(Calendar.MONTH,-1);
                int qday = prevDate.get(Calendar.DAY_OF_MONTH);
                prevDate.add(Calendar.DAY_OF_MONTH, -(qday));
                dateNextDate.setMinDate(prevDate.getTimeInMillis());
                Calendar nextDate = Calendar.getInstance();
                nextDate.add(Calendar.DAY_OF_MONTH,5);
                dateNextDate.setMaxDate(nextDate.getTimeInMillis());
            }


            DayEndDialog
                    .setIcon(R.drawable.ic_launcher)
                    .setTitle("Day End")
                    .setMessage("Current transaction date is " + BusinessDate.getString(0) + "\n" + "select next transaction date")
                    .setView(dateNextDate)
                    .setPositiveButton("Set", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                            String strNextDate = "";

                            if (dateNextDate.getDayOfMonth() < 10) {
                                strNextDate = "0" + String.valueOf(dateNextDate.getDayOfMonth());
                            } else {
                                strNextDate = String.valueOf(dateNextDate.getDayOfMonth());
                            }
                            if (dateNextDate.getMonth() < 9) {
                                strNextDate += "-" + "0" + String.valueOf(dateNextDate.getMonth() + 1) + "-";
                            } else {
                                strNextDate += "-" + String.valueOf(dateNextDate.getMonth() + 1) + "-";
                            }
                            strNextDate += String.valueOf(dateNextDate.getYear());

                            try{
                                if(!date_str1.equals("") && !date_str1.equals(strNextDate)){
                                    Date dd = new SimpleDateFormat("dd-MM-yyyy").parse(strNextDate);
                                    Cursor cc = getDb().getBillDetailByInvoiceDate(dd.getTime());
                                    if(cc!= null && cc.getCount()>0)
                                    {
                                        MsgBox.Show("Error","Since bill for date "+strNextDate+" is already present in database, therefore this date cannot be selected");
                                        return;
                                    }
                                }else if(date_str1.equals(strNextDate))
                                {
                                    return;
                                }
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            int result = 0;
                            result = getDb().deleteAllKOTItems();
                            Log.d("ManualDayEnd", "Items deleted from Pending KOT status: :" + result);
                            result =0;
                            result = getDb().deleteAllVoidedKOT();
                            Log.d("ManualDayEnd", "Items deleted from Void KOT status: :" + result);
                            result = 0;
                            result = getDb().deleteAllReservedTables();
                            Log.d("ManualDayEnd", "No of Reserved Tables deleted :" + result);

                            // reset KOT No

                            long Result = getDb().updateKOTNo(0);
                            Log.d("ManualDayEnd", "KOT No reset to 0 status :"+Result);

                            try
                            {   Cursor businessdate_cursor = getDb().getCurrentDate();
                                if(businessdate_cursor != null && businessdate_cursor.moveToNext())
                                {
                                    String businessdate =  businessdate_cursor.getString(businessdate_cursor.getColumnIndex("BusinessDate"));
                                    Date dd = new SimpleDateFormat("dd-MM-yyyy").parse(businessdate);
                                    long milli = dd.getTime();
                                    Cursor cursor = getDb().getInvoice_outward(Long.toString(milli));
                                    if(cursor!=null && cursor.moveToNext())
                                    {
                                        int billcount = cursor.getCount();
                                        Cursor cursor_owner = getDb().getOwnerDetail();
                                        if(cursor_owner!= null && cursor_owner.moveToNext())
                                        {
                                            String deviceid = cursor_owner.getString(cursor_owner.getColumnIndex("DeviceId"));
                                            String deviceName = cursor_owner.getString(cursor_owner.getColumnIndex("DeviceName"));
                                            String Email = cursor_owner.getString(cursor_owner.getColumnIndex("Email"));
                                            //Date newDate = new Date(milli);
                                            //String dd1 = new SimpleDateFormat("yyyy-MM-dd").format(newDate);
                                            String paramStr ="data="+deviceid+","+Email+","+businessdate+","+billcount+","+deviceName;
                                            //String paramStr ="data="+deviceid+","+Email+","+dd1+","+billcount+","+deviceName;
                                            new HTTPAsyncTask(HomeActivity.this,HTTPAsyncTask.HTTP_GET,"",Upload_Invoice_Count, Config.Upload_No_of_Invoices+paramStr).execute();
                                        }
                                        else
                                        {
                                            Log.d("TAG", "Cannot upload invoices count due to insufficient owners details");
                                        }

                                    }else
                                    {
                                        Toast.makeText(myContext, "No Invoice count to send for businessDate :"+businessdate, Toast.LENGTH_SHORT).show();
                                        Log.d("TAG", "No Invoice count to send for businessDate :"+businessdate);
                                    }
                                }


                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                            //UpdateStock();
                            long iResult = getDb().updateBusinessDate(String.valueOf(strNextDate));
                            Log.d("ManualDayEnd", "Bussiness Date updation status :" + iResult);
                            StockOutwardMaintain stock_outward = new StockOutwardMaintain(myContext, getDb());
                            stock_outward.saveOpeningStock_Outward(strNextDate);

                            StockInwardMaintain stock_inward = new StockInwardMaintain(myContext, getDb());
                            stock_inward.saveOpeningStock_Inward(strNextDate);
                            //setBillNo();
                            BillNoReset bs = new BillNoReset();
                            bs.setBillNo(dbHomeScreen);


                            if (iResult > 0) {
                                MsgBox.Show("Information", "Transaction Date changed to " + strNextDate);
                            } else {
                                MsgBox.Show("Warning", "Error: DayEnd is not done");
                            }
                        }

                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            MsgBox.Show("Warning", "DayEnd operation has been cancelled, Transaction date remains same");
                        }
                    })
                    .show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void LaunchActivity(View v) {
        if (v.getContentDescription().toString().equalsIgnoreCase("DineIn"))
        {
            Intent intentDineIn = new Intent(myContext, TableActivity.class);
            intentDineIn.putExtra("BILLING_MODE", DINEIN);
            intentDineIn.putExtra("CUST_ID", 0);
            startActivity(intentDineIn);
        }
        else if (v.getContentDescription().toString().equalsIgnoreCase("CounterSales"))
        {
            Intent intentTakeAway = new Intent(myContext, BillingCounterSalesActivity.class);
            Log.d(TAG,"Opening Activity Started");
            startActivity(intentTakeAway);
        }
        else if (v.getContentDescription().toString().equalsIgnoreCase("PickUp"))
        {
            Intent intentPickUp = new Intent(myContext, BillingHomeDeliveryActivity.class);
            intentPickUp.putExtra("BILLING_MODE", PICKUP);
            intentPickUp.putExtra("CUST_ID", 0);
            startActivity(intentPickUp);

        } else if (v.getContentDescription().toString().equalsIgnoreCase("Delivery")) {
            // Launch Billing screen activity in Delivery billing mode

            Intent intentDelivery = new Intent(myContext, BillingHomeDeliveryActivity.class);
            intentDelivery.putExtra("BILLING_MODE", DELIVERY);
            intentDelivery.putExtra("CUST_ID", 0);
            startActivity(intentDelivery);

        }  else if (v.getContentDescription().toString().equalsIgnoreCase("Ammend")) {

            Intent intentDelivery = new Intent(myContext, TabbedAmmendActivity.class);
            intentDelivery.putExtra("USER_ID", strUserId);//spUser.getString("USER_ID", "GHOST"));
            intentDelivery.putExtra("USER_NAME", strUserName);//spUser.getString("USER_NAME", "GHOST"));
            intentDelivery.putExtra("CUST_ID", 0);
            startActivity(intentDelivery);

        } else if (v.getContentDescription().toString().equalsIgnoreCase("cdnr")) {

            Intent intentDelivery = new Intent(myContext, TabbedCreditDebitNote.class);
            intentDelivery.putExtra("BILLING_MODE", DELIVERY);
            intentDelivery.putExtra("USER_ID", strUserId);//spUser.getString("USER_ID", "GHOST"));
            intentDelivery.putExtra("USER_NAME", strUserName);//spUser.getString("USER_NAME", "GHOST"));
            intentDelivery.putExtra("CUST_ID", 0);
            startActivity(intentDelivery);
        }
        else if (v.getContentDescription().toString().equalsIgnoreCase("Masters"))
        {
            if (listAccesses.contains("Masters"))
            {
                // Launch employee activity
                Intent intentMasters = new Intent(myContext, MasterActivity.class);
                intentMasters.putExtra("USER_ID", strUserId);
                intentMasters.putExtra("USER_NAME", strUserName);
                startActivityForResult(intentMasters,1);
            }
            else
            {
                MsgBox.Show("Warning", "Access denied");
            }

        }

        else if (v.getContentDescription().toString().equalsIgnoreCase("PaymentReceipt")) {
            //startActivity(new Intent(myContext,TableActivity.class));
            if (listAccesses.contains("Payment & Receipt"))
            {
                Intent intentPaymentReceipt = new Intent(myContext, PaymentReceiptActivity.class);
                intentPaymentReceipt.putExtra("USER_ID", strUserId);
                intentPaymentReceipt.putExtra("USER_NAME", strUserName);
                startActivity(intentPaymentReceipt);
            }
            else
            {
                MsgBox.Show("Warning", "Access denied");
            }

        } else if (v.getContentDescription().toString().equalsIgnoreCase("Reports")) {
            if (listAccesses.contains("Reports"))
            {
                Intent intentReports = new Intent(myContext, TabbedReportActivity.class);
                intentReports.putExtra("USER_ID", strUserId);
                intentReports.putExtra("USER_NAME", strUserName);
                startActivity(intentReports);
            }
            else
            {
                MsgBox.Show("Warning", "Access denied");
            }

        } else if (v.getContentDescription().toString().equalsIgnoreCase("DayEnd")) {
            if (settingcrsr != null) {
                int autoDayend = settingcrsr.getInt(settingcrsr.getColumnIndex("DateAndTime"));
                if (autoDayend == 1) //DateChange -> 1 - auto, 0 = manual
                {
                    MsgBox.Show(" Day End ", "Day End has been to auto mode. To manually set Date and Time , please go to settings ");
                    return;
                }
            }
            AlertDialog.Builder dlgDayEnd = new AlertDialog.Builder(myContext);
            dlgDayEnd
                    .setIcon(R.drawable.ic_launcher)
                    .setTitle("Day End")
                    .setMessage("Day End operation will erase all pending KOT ,Voided KOT and Table Booking details from database (if any)"
                            + " and changes transaction date to next date. " +
                            "\nAlso please note, back date (upto last month) can only be selected if, no bill was made for that date."
                            + "\n" + "Do you want to proceed?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                            DayEnd();
                        }
                    })
                    .show();
        }
        getDb().CloseDatabase();
        dbHomeScreen = null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode==1)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                Display();
            }
        }
    }

    public void onHttpRequestComplete(int requestCode, String data) {
        //progressDialog.dismiss();
        if (data != null) {
            try{
                if (requestCode == Upload_Invoice_Count) // Upload_invoice count for metering
                {
                    if(data.contains("\"success\":true,\"message\":\"Data Updated Successfully\""))
                    {
                        Toast.makeText(myContext, "No of invoices uploaded sucessfully.", Toast.LENGTH_SHORT).show();
                    }
                    else if (data.contains("\"success\":false,\"message\":\"Check Parameters\""))
                    {
                        Toast.makeText(myContext, "No of invoices uploading failed.", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"No of invoices uploading failed.");
                    }
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error due to " + e, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, "Sending error", Toast.LENGTH_SHORT).show();
        }

    }

    @Override

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);
            AuthorizationDialog
                    .setTitle("Are you sure you want to exit ?")
                    .setIcon(R.drawable.ic_launcher)
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getDb().CloseDatabase();
                            finish();
                        }
                    })
                    .show();
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