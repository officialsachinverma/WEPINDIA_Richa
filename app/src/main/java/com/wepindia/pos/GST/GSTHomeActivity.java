
package com.wepindia.pos.GST;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.gst.get.GetGSTR2B2BFinal;
import com.wep.common.app.gst.get.GetGSTR2B2BInvoice;
import com.wep.common.app.gst.get.GetGSTR2B2BItem;
import com.wep.gstcall.api.http.DownloadFileFromURL;
import com.wep.gstcall.api.http.HTTPAsyncTask;
import com.wep.gstcall.api.util.Config;
import com.wepindia.pos.GST.controlers.GSTDataController;
import com.wepindia.pos.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by welcome on 22-10-2016.
 */

public class GSTHomeActivity extends Activity implements HTTPAsyncTask.OnHTTPRequestCompletedListener {

    private static int REQUEST_GET_GSTR2_B2B = 1001;
    Context myContext;
    // DatabaseHandler_gst object
    DatabaseHandler dbHomeScreen;
    //Message dialog object
    public AlertDialog.Builder MsgBox;

    // Variables
    private static final String GSTR1 = "1";
    private static final String strUserId = null;
    private static final String strUserName = null;
    private static final String date = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove default title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(R.layout.activity_home_gst);


        dbHomeScreen = new DatabaseHandler(GSTHomeActivity.this);
        myContext = this;

        try {

            MsgBox = new AlertDialog.Builder(myContext);

            //strUserId = getIntent().getStringExtra("USER_ID");
            //strUserName = getIntent().getStringExtra("USER_NAME");
            //date = getIntent().getIntExtra("Date");

            dbHomeScreen.CreateDatabase();
            dbHomeScreen.OpenDatabase();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    // GSTR1 button event
    public void GSTR1(View v){

        Cursor User = null;// dbLogin.getUser(txtUserId.getText().toString(), txtPassword.getText().toString());

            //Intent intentGSTHomeScreen = new Intent(myContext,GSTR1_Activity.class);
            //startActivity(intentGSTHomeScreen, null);
        startActivity(new Intent(myContext,GSTR1_Activity.class));

    }

    // GSTR1 button event
    public void GSTR2(View v){

        Cursor User = null;// dbLogin.getUser(txtUserId.getText().toString(), txtPassword.getText().toString());

        //Intent intentGSTHomeScreen = new Intent(myContext,GSTR1_Activity.class);
        //startActivity(intentGSTHomeScreen, null);
        startActivity(new Intent(myContext,GSTR2_Activity.class));

    }

    public void reconcile(View view)
    {
        startActivity(new Intent(myContext,GSTR_reconcile_Activity.class));
    }

    public void InwardItemEntry(View view)
    {
        startActivity(new Intent(myContext,Inward_Item_Entry_Activity.class));
    }

    public void InwardInvoiceEntry(View view)
    {
        startActivity(new Intent(myContext,InwardInvoiceEntry_Activity.class));
    }

    public void GSTR2_upload_forDay(View view)
    {
        //DatabaseHandler dbGSTHome = new DatabaseHandler(GSTHomeActivity.this);

        //GSTDataController dataController = new GSTDataController(GSTHomeActivity.this,dbHomeScreen);
        if (dbHomeScreen == null)
            dbHomeScreen =  new DatabaseHandler(GSTHomeActivity.this);
        dbHomeScreen.CloseDatabase();
        dbHomeScreen.OpenDatabase();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String startDate = sdf.format(date);
        String gstin_owner = "123wsd";

            String gstin_owner1 = dbHomeScreen.getGstin_owner();
            Cursor gstr2_crsr = dbHomeScreen.getInvoice_inward(startDate);
            float taxval = 0;
            while (gstr2_crsr!=null && gstr2_crsr.moveToNext())
            {
                taxval += Float.parseFloat(gstr2_crsr.getString(gstr2_crsr.getColumnIndex("TaxableValue")));
            }
        //float taxval = 5000;
        //GSTR2_day gstr2_data = new GSTR2_day(gstin_owner,startDate,taxval);
            /*GSTR2Data gstr1Data = new GSTR2Data(sharedPreferences.getString("gstin","0"),str[0]+str[1],123,234,gstr2B2BDatasList,null);
            progressDialog.show();
            GSTRData gstrData = new GSTRData(sharedPreferences.getString("userName","demouser"),sharedPreferences.getString("gstin","0"),gstr1Data);
            */
        String paramStr = "gstin="+gstin_owner1+"&date="+startDate+"&salevalue="+taxval+"";
        //String strJson = GstJsonEncoder.getGSTRJsonEncode(gstr2_data);
        new HTTPAsyncTask(GSTHomeActivity.this,HTTPAsyncTask.HTTP_GET,"",111, Config.GSTR2_DAY+paramStr).execute();


    }

    public void GSTR1_upload_forDay(View view)
    {
        //DatabaseHandler dbGSTHome = new DatabaseHandler(GSTHomeActivity.this);

        GSTDataController dataController = new GSTDataController(GSTHomeActivity.this,dbHomeScreen);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String startDate = sdf.format(date);
        String gstin_owner = "123wsd_out";
        if (dbHomeScreen == null)
            dbHomeScreen =  new DatabaseHandler(GSTHomeActivity.this);
        dbHomeScreen.CloseDatabase();
        dbHomeScreen.OpenDatabase();
        gstin_owner = dbHomeScreen.getGstin_owner();
        Cursor gstr1_crsr = dbHomeScreen.getInvoice_outward(startDate);
            float taxval = 0;
            while (gstr1_crsr!=null && gstr1_crsr.moveToNext())
            {
                taxval += Float.parseFloat(gstr1_crsr.getString(gstr1_crsr.getColumnIndex("TaxableValue")));
            }
        //float taxval = 6000;
        //GSTR2_day gstr2_data = new GSTR2_day(gstin_owner,startDate,taxval);
            /*GSTR2Data gstr1Data = new GSTR2Data(sharedPreferences.getString("gstin","0"),str[0]+str[1],123,234,gstr2B2BDatasList,null);
            progressDialog.show();
            GSTRData gstrData = new GSTRData(sharedPreferences.getString("userName","demouser"),sharedPreferences.getString("gstin","0"),gstr1Data);
            */
        String paramStr = "gstin="+gstin_owner+"&date="+startDate+"&purchasevalue="+taxval+"";
        //String strJson = GstJsonEncoder.getGSTRJsonEncode(gstr2_data);
        new HTTPAsyncTask(GSTHomeActivity.this,HTTPAsyncTask.HTTP_GET,"",1111, Config.GSTR1_DAY+paramStr).execute();

    }

    //@Override
    public void onHttpRequestComplete(int requestCode, String data) {
        //progressDialog.dismiss();
        if(data!=null)
        {
            if(requestCode == 0/*true*/) // GSTR1
            {
                if(data.equalsIgnoreCase(""))
                {
                    Toast.makeText(this, "Error due to empty response", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        if(jsonObject.getBoolean("success")){
                            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error due to "+e, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
            else if(requestCode == 1) // GSTR2REQUEST_GET_GSTR2_B2B
            {
                if(data.equalsIgnoreCase(""))
                {
                    Toast.makeText(this, "Error due to empty response", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        if(jsonObject.getBoolean("success")){
                            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error due to "+e, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
            else if(requestCode == 111) // GSTR2REQUEST_GET_GSTR2_B2B
            {
                if(data.equalsIgnoreCase("Success"))
                {
                    Toast.makeText(this, "Success in uploading ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "Error due to empty response", Toast.LENGTH_SHORT).show();
                }
            }else if(requestCode == 1111) // GSTR2REQUEST_GET_GSTR2_B2B
            {
                if(data.equalsIgnoreCase("Success"))
                {
                    Toast.makeText(this, "Success in uploading ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "Error due to empty response", Toast.LENGTH_SHORT).show();
                }
            }
            else if(requestCode == REQUEST_GET_GSTR2_B2B) // REQUEST_GET_GSTR2_B2B
            {
                //GetGSTR2B2BFinal getGSTR2B2BFinal = null;
                ArrayList<GetGSTR2B2BFinal> finalsList = new ArrayList<GetGSTR2B2BFinal>();
                data = data.replaceAll("\\\\", "");
                data = data.substring(1,data.length()-1);
                if(data.equalsIgnoreCase(""))
                {
                    Toast.makeText(this, "Error due to empty response", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray jsonArray = jsonObject.getJSONArray("b2b");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            GetGSTR2B2BFinal getGSTR2B2BFinal = new GetGSTR2B2BFinal();
                            getGSTR2B2BFinal.setCtin(jsonObject1.getString("ctin"));
                            JSONArray jsonArrayInv = jsonObject1.getJSONArray("inv");
                            ArrayList<GetGSTR2B2BInvoice> getGSTR2B2BInvoicesList = new ArrayList<GetGSTR2B2BInvoice>();
                            for(int j=0;j<jsonArrayInv.length();j++)
                            {
                                GetGSTR2B2BInvoice getGSTR2B2BInvoice = new GetGSTR2B2BInvoice();
                                JSONObject jsonObjectInv = jsonArrayInv.getJSONObject(j);
                                ArrayList<GetGSTR2B2BItem> itemsLis = new ArrayList<GetGSTR2B2BItem>();
                                //some items like inum, idt
                                getGSTR2B2BInvoice.setInum(jsonObjectInv.getString("inum"));
                                getGSTR2B2BInvoice.setIdt(jsonObjectInv.getString("idt"));
                                getGSTR2B2BInvoice.setVal(jsonObjectInv.getDouble("val"));
                                getGSTR2B2BInvoice.setPos(jsonObjectInv.getString("pos"));
                                getGSTR2B2BInvoice.setRchrg(jsonObjectInv.getString("rchrg"));
                                getGSTR2B2BInvoice.setPro_ass(jsonObjectInv.getString("pro_ass"));

                                JSONArray jsonArrayBillItems = jsonObjectInv.getJSONArray("itms");
                                for(int k=0;k<jsonArrayBillItems.length();k++)
                                {
                                    JSONObject jsonObjectItem = jsonArrayBillItems.getJSONObject(k);
                                    int lineNum = jsonObjectItem.getInt("num");
                                    JSONObject jsonObjectitm_det = jsonObjectItem.getJSONObject("itm_det");
                                    GetGSTR2B2BItem item = new GetGSTR2B2BItem(
                                            lineNum,
                                            jsonObjectitm_det.getString("ty"),
                                            jsonObjectitm_det.getString("hsn_sc"),
                                            jsonObjectitm_det.getDouble("txval"),
                                            jsonObjectitm_det.getDouble("irt"),
                                            jsonObjectitm_det.getDouble("iamt"),
                                            jsonObjectitm_det.getDouble("crt"),
                                            jsonObjectitm_det.getDouble("camt"),
                                            jsonObjectitm_det.getDouble("srt"),
                                            jsonObjectitm_det.getDouble("samt")
                                    );
                                    itemsLis.add(item);
                                }
                                getGSTR2B2BInvoice.setItems(itemsLis);
                                getGSTR2B2BInvoicesList.add(getGSTR2B2BInvoice);
                            }
                            getGSTR2B2BFinal.setInvoicesList(getGSTR2B2BInvoicesList);
                            finalsList.add(getGSTR2B2BFinal);
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Error due to "+e, Toast.LENGTH_SHORT).show();
                        finalsList = null;
                        e.printStackTrace();
                    }
                    // Add to db
                    dbHomeScreen.addGSTR2B2BItems(finalsList);
                }
            }
        }
        else
        {
            Toast.makeText(this, "Sending error", Toast.LENGTH_SHORT).show();
        }
    }
}


