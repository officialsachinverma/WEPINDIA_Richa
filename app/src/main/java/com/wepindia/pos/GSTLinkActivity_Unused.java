package com.wepindia.pos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.gst.B2Csmall;
import com.wep.common.app.gst.GSTR1AB2BSData;
import com.wep.common.app.gst.GSTR1AData;
import com.wep.common.app.gst.GSTR1B2BAInvoiceItems;
import com.wep.common.app.gst.GSTR1B2CSAData;
import com.wep.common.app.gst.GSTR1B2CSData;
import com.wep.common.app.gst.GSTR1_CDN_Data;
import com.wep.common.app.gst.GSTR1Data;
import com.wep.common.app.gst.GSTR2B2BAItemDetails;
import com.wep.common.app.gst.GSTRData;
import com.wep.common.app.gst.get.GetGSTR1Summary;
import com.wep.common.app.gst.get.GetGSTR2B2BFinal;
import com.wep.common.app.gst.get.GetGSTR2B2BInvoice;
import com.wep.common.app.gst.get.GetGSTR2B2BItem;
import com.wep.gstcall.api.http.DownloadFileFromURL;
import com.wep.gstcall.api.http.HTTPAsyncTask;
import com.wep.gstcall.api.util.Config;
import com.wep.gstcall.api.util.GstJsonEncoder;
import com.wepindia.pos.GST.GSTFileActivity;
import com.wepindia.pos.GST.controlers.GSTDataController;
import com.wepindia.pos.GST.fragments.AuthFragment;
import com.wepindia.pos.GenericClasses.DateTime;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.utils.ConnectionDetector;
import com.wepindia.pos.utils.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class GSTLinkActivity_Unused extends Activity implements HTTPAsyncTask.OnHTTPRequestCompletedListener, DownloadFileFromURL.OnFileDownloadCompletedListener,AuthFragment.OnLocalAuthCompletedListener {

    private static final int REQUEST_GET_GSTR1_SUMMERY = 1002;
    private static final int REQUEST_GET_GSTR3 = 1003;
    private static final int REQUEST_SAVE_GSTR1 = 1;
    private static final int REQUEST_SAVE_GSTR2 = 2;
    private static final int REQUEST_GET_GSTR1_SUMMARY = 1006;
    private static final int REQUEST_GET_GSTR1A_SUMMARY = 1007;
    private static final int REQUEST_SAVE_GSTR1A = 11;
    private Context myContext;
    private String strDate = "";
    private EditText etReportDateStart,etReportDateEnd;
    private MessageDialog MsgBox;
    public DatabaseHandler dbGSTLink;
    private DateTime objDate;
    private GSTDataController dataController;
    private ProgressDialog progressDialog,pDialog;
    private SharedPreferences sharedPreferences;
    private RelativeLayout SaveGSTR1,fileGSTR1,getGSTRR2B2B,SaveGSTR2,getGSTR1Summery,getGSTR3;

    private static int REQUEST_GET_GSTR2_B2B = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gstlink);
        init();
    }


    private void getGSTR3All() {
        progressDialog.show();
        new HTTPAsyncTask(GSTLinkActivity_Unused.this, HTTPAsyncTask.HTTP_GET,"",REQUEST_GET_GSTR3, Config.GSTR3_GET).execute();
    }

    private void getGSTR1AllSummery() {
        progressDialog.show();
        new HTTPAsyncTask(GSTLinkActivity_Unused.this, HTTPAsyncTask.HTTP_GET,"",REQUEST_GET_GSTR1_SUMMERY, Config.GSTR1_SUMMERY_GET_API).execute();
    }
    // After Tab UI Completes Put All codes below

    private void init() {
        MsgBox = new MessageDialog(GSTLinkActivity_Unused.this);
        etReportDateStart = (EditText) findViewById(R.id.etReportDateStart);
        etReportDateEnd = (EditText) findViewById(R.id.etReportDateEnd);
        myContext = this;
        dbGSTLink = new DatabaseHandler(myContext);
        sharedPreferences = getSharedPreferences("com.wepindia.pos",Context.MODE_PRIVATE);

        try {
            dbGSTLink.CreateDatabase();
            dbGSTLink.OpenDatabase();
            /*Cursor BusinessDate = dbGSTLink.getCurrentDate();
            if (BusinessDate.moveToFirst()) {
                objDate = new DateTime(BusinessDate.getString(0));
                etReportDateStart.setText(BusinessDate.getString(0));
                etReportDateEnd.setText(BusinessDate.getString(0));
            } else {
                //objDate = new DateTime();
            }*/
            Date d = new Date();
            CharSequence currentdate = DateFormat.format("yyyy-MM-dd", d.getTime());
            objDate = new DateTime(currentdate.toString());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            MsgBox.Show("Error", e.getMessage());
        }
        dataController = new GSTDataController(GSTLinkActivity_Unused.this,dbGSTLink);
        progressDialog = new ProgressDialog(GSTLinkActivity_Unused.this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        pDialog = new ProgressDialog(GSTLinkActivity_Unused.this);
        pDialog.setMessage("Downloading file. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(true);
    }
    private void DateSelection(final int DateType) {        // StartDate: DateType = 1 EndDate: DateType = 2
        try {
            AlertDialog.Builder dlgReportDate = new AlertDialog.Builder(GSTLinkActivity_Unused.this);
            final DatePicker dateReportDate = new DatePicker(GSTLinkActivity_Unused.this);
            // Initialize date picker value to business date
            dateReportDate.updateDate(objDate.getYear(), objDate.getMonth(), objDate.getDay());
            String strMessage = "";
            if (DateType == 1) {
                strMessage = "Select report Start date";
            } else {
                strMessage = "Select report End date";
            }

            dlgReportDate
                    .setIcon(R.drawable.ic_launcher)
                    .setTitle("Date Selection")
                    .setMessage(strMessage)
                    .setView(dateReportDate)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            strDate = String.valueOf(dateReportDate.getYear()) + "-";
                            if (dateReportDate.getMonth() < 9) {
                                strDate += "0" + String.valueOf(dateReportDate.getMonth() + 1) + "-";
                            } else {
                                strDate += String.valueOf(dateReportDate.getMonth() + 1) + "-";
                            }
                            if (dateReportDate.getDayOfMonth() < 10) {
                                strDate += "0" + String.valueOf(dateReportDate.getDayOfMonth());
                            } else {
                                strDate += String.valueOf(dateReportDate.getDayOfMonth());
                            }

                            if (DateType == 1) {
                                etReportDateStart.setText(strDate);
                            } else {
                                etReportDateEnd.setText(strDate);
                            }
                            Log.d("ReportDate", "Selected Date:" + strDate);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    })
                    .show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private boolean DateValidation(String StartDate, String EndDate) {
        if (StartDate.equalsIgnoreCase("") ||
                EndDate.equalsIgnoreCase("")) {
            MsgBox.Show("Warning", "Please select report date range properly");
            return false;
        } else {
            return true;
        }
    }

    public void From(View v) {
        DateSelection(1);
    }

    public void To(View v) {
        if (!etReportDateStart.getText().toString().equalsIgnoreCase("")) {
            DateSelection(2);

        } else {
            MsgBox.Show("Warning", "Please select report FROM date");
        }
    }

    //@Override
    public void onHttpRequestComplete(int requestCode, String data) {
        progressDialog.dismiss();
        if(data!=null)
        {
            if(requestCode == REQUEST_SAVE_GSTR1)
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

            if(requestCode == REQUEST_SAVE_GSTR1A)
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
            else if(requestCode == REQUEST_SAVE_GSTR2) // GSTR2REQUEST_GET_GSTR2_B2B
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

            else if(requestCode == REQUEST_GET_GSTR1A_SUMMARY) // REQUEST_GET_GSTR1A_SUMMARY
            {
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
                        JSONArray jsonarray = jsonObject.getJSONArray("gstr1summary");
                        Type listType = new TypeToken<ArrayList<GetGSTR1Summary>>(){}.getType();
                        ArrayList<GetGSTR1Summary> dataList = new GsonBuilder().create().fromJson(jsonarray.toString(), listType);
                        dbGSTLink.saveGSTR1ASummary(dataList);
                        Toast.makeText(this, "Data loaded successfully", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Error due to "+e, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            else if(requestCode == REQUEST_GET_GSTR1_SUMMARY) // REQUEST_GET_GSTR1A_SUMMARY
            {
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
                        JSONArray jsonarray = jsonObject.getJSONArray("gstr1summary");
                        Type listType = new TypeToken<ArrayList<GetGSTR1Summary>>(){}.getType();
                        ArrayList<GetGSTR1Summary> dataList = new GsonBuilder().create().fromJson(jsonarray.toString(), listType);
                        dbGSTLink.saveGSTR1ASummary(dataList);
                        Toast.makeText(this, "Data loaded successfully", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Error due to "+e, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
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
                    dbGSTLink.addGSTR2B2BItems(finalsList);
                    Toast.makeText(this, "Data loaded successfully", Toast.LENGTH_SHORT).show();
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }
            else if(requestCode == REQUEST_GET_GSTR3) // GSTR2REQUEST_GET_GSTR2_B2B
            {
                if(data.equalsIgnoreCase(""))
                {
                    Toast.makeText(this, "Error due to empty response", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    /*try {
                        JSONObject jsonObject = new JSONObject(data);
                        if(jsonObject.getBoolean("success")){
                            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error due to "+e, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }*/
                }
            }
        }
        else
        {
            Toast.makeText(this, "Sending error", Toast.LENGTH_SHORT).show();
        }
    }
    public void saveGSTR1(String userName) {
        //Toast.makeText(myContext, "hello", Toast.LENGTH_SHORT).show();
        String startDate = DateUtil.getDateForDatePicker(etReportDateStart.getText().toString()) ;
        String endDate = DateUtil.getDateForDatePicker(etReportDateEnd.getText().toString()) ;
        if(ConnectionDetector.isInternetConnection(GSTLinkActivity_Unused.this))
        {
            progressDialog.show();
            ArrayList<GSTR1B2CSData> list = new ArrayList<GSTR1B2CSData>();
            ArrayList<B2Csmall> b2CsmallsList = dataController.getGSTR1B2CSDataList(startDate,endDate);
            double gt = 0;
            for(B2Csmall b2Csmall : b2CsmallsList)
            {
                gt = gt+b2Csmall.getSubTotal();
                GSTR1B2CSData b2CSData = new GSTR1B2CSData(
                            "*flag*"/*"A"*/,
                            "*chksum*"/*"HHJJHJJHJJJJJJ"*/,
                        b2Csmall.getPlaceOfSupply(),
                        b2Csmall.getSupplyType(),
                        b2Csmall.getHSNCode().substring(0,b2Csmall.getHSNCode().indexOf("-")),
                        b2Csmall.getTaxableValue(),
                        b2Csmall.getIGSTRate(),
                        b2Csmall.getIGSTAmt(),
                        b2Csmall.getCGSTRate(),
                        b2Csmall.getCGSTAmt(),
                        b2Csmall.getSGSTRate(),
                        b2Csmall.getSGSTAmt(),
                        /*"Y"*/(b2Csmall.getProAss().equalsIgnoreCase(""))? "*pro_ass*" : b2Csmall.getProAss()
                );
                list.add(b2CSData);
            }
            String str[] = startDate.split("-");
            // Get All B2CSA data
            ArrayList<GSTR1B2CSAData> b2CSADataArrayList =null;//dataController.getGSTR1B2CSAList(startDate,endDate);
            // Get All CDN Data
            ArrayList<GSTR1_CDN_Data> cdnList = null;// dataController.getGSTR1CDNData(startDate,endDate);
            GSTR1Data gstr1Data = new GSTR1Data(dbGSTLink.getGSTIN(),str[2]+str[0],gt,list,b2CSADataArrayList,cdnList);
            GSTRData gstrData = new GSTRData(userName,dbGSTLink.getGSTIN(),gstr1Data);
            String strJson = GstJsonEncoder.getGSTRJsonEncode(gstrData);
            new HTTPAsyncTask(GSTLinkActivity_Unused.this, HTTPAsyncTask.HTTP_POST,strJson,REQUEST_SAVE_GSTR1, Config.GSTR1_URL).execute();
        }
        else
        {
            Toast.makeText(GSTLinkActivity_Unused.this, "No Internet Connection! Try again Later", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickDownloadGSTR2A(View view) {
        pDialog.show();
        new DownloadFileFromURL(GSTLinkActivity_Unused.this,pDialog,"B2B", Config.GSTR_GET_API).execute();
    }

    public void onFileDownloadComplete(boolean status, String filePath) {
        pDialog.dismiss();
        if(status)
        {
            Toast.makeText(myContext, "Download Complete "+filePath, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(myContext, "Download Failed due to "+filePath, Toast.LENGTH_SHORT).show();
        }

    }

    public void saveGSTR2(String userName) {
        String startDate = DateUtil.getDateForDatePicker(etReportDateStart.getText().toString()) ;
        String endDate = DateUtil.getDateForDatePicker(etReportDateEnd.getText().toString()) ;
        if(ConnectionDetector.isInternetConnection(GSTLinkActivity_Unused.this))
        {
            /*ArrayList<GSTR2B2BData> gstr2B2BDatasList = dataController.getB2BItems(startDate,endDate);
            String str[] = startDate.split("-");
            ArrayList<GSTR2B2BAData> b2baList = null;//dataController.getGSTR2B2BDataList_registered();
            ArrayList<GSTR2_CDN_Data> gstr2cdnList = null;;//dataController.getGSTR2CDNSaveData();
            GSTR2Data gstr1Data = new GSTR2Data(dbGSTLink.getGSTIN(),str[2]+str[0],123,234,gstr2B2BDatasList,b2baList,gstr2cdnList);
            progressDialog.show();
            GSTRData gstrData = new GSTRData(userName,dbGSTLink.getGSTIN(),gstr1Data);
            String strJson = GstJsonEncoder.getGSTRJsonEncode(gstrData);
            new HTTPAsyncTask(GSTLinkActivity_Unused.this, HTTPAsyncTask.HTTP_POST,strJson,REQUEST_SAVE_GSTR2, Config.GSTR2_URL).execute();
        */}
        else
        {
            Toast.makeText(GSTLinkActivity_Unused.this, "No Internet Connection! Try again Later", Toast.LENGTH_SHORT).show();
        }
    }

    public void disMiss(){
        if(progressDialog!=null){
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }

    private void promptAuthFragment(int code) {
        AuthFragment alertdFragment = new AuthFragment();
        alertdFragment.setCancelable(false);
        alertdFragment.setCode(code);
        alertdFragment.show(getFragmentManager(), "auth");
    }

    public void onLocalAuthCompleted(boolean success, int code, String userName) {
        if(success)
        {
            if(code == REQUEST_SAVE_GSTR1)
            {
                saveGSTR1(userName);
            }
            if(code == REQUEST_SAVE_GSTR1A)
            {
                saveGSTR1A(userName);
            }
            else if(code == REQUEST_SAVE_GSTR2)
            {
                saveGSTR2(userName);
            }
        }
        else
        {
            Toast.makeText(myContext, "Authentication Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickGetGstr1Summary(View view) {
        startActivity(new Intent(getApplicationContext(),GSTFileActivity.class));
    }
// public void onClickGetGstr1Summary(View view) {
//        progressDialog.show();
//        new HTTPAsyncTask(GSTLinkActivity_Unused.this, HTTPAsyncTask.HTTP_GET,"",REQUEST_GET_GSTR1_SUMMARY, Config.GSTR1_SUMMERY_GET_API).execute();
//    }

    public void onClickGetGstr1ASummary(View view) {
        progressDialog.show();
        new HTTPAsyncTask(GSTLinkActivity_Unused.this, HTTPAsyncTask.HTTP_GET,"",REQUEST_GET_GSTR1A_SUMMARY, Config.GSTR1A_SUMMERY_GET_API).execute();
    }

    public void onClickSaveGstr1(View view) {
        String startDate = etReportDateStart.getText().toString() ;
        String endDate = etReportDateEnd.getText().toString() ;
        if(startDate.equalsIgnoreCase("") || endDate.equalsIgnoreCase(""))
        {
            //disMiss();
            MsgBox.setMessage("Please select Date")
                    .setPositiveButton("OK", null)
                    .show();
        }
        else
        {
            promptAuthFragment(REQUEST_SAVE_GSTR1);
        }
    }

    public void onClickSaveGstr1A(View view) {
        String startDate = etReportDateStart.getText().toString() ;
        String endDate = etReportDateEnd.getText().toString() ;
        if(startDate.equalsIgnoreCase("") || endDate.equalsIgnoreCase(""))
        {
            //disMiss();
            MsgBox.setMessage("Please select Date")
                    .setPositiveButton("OK", null)
                    .show();
        }
        else
        {
            promptAuthFragment(REQUEST_SAVE_GSTR1A);
        }

    }

    private void saveGSTR1A(String userName) {

        String startDate = DateUtil.getDateForDatePicker(etReportDateStart.getText().toString()) ;
        String endDate = DateUtil.getDateForDatePicker(etReportDateEnd.getText().toString()) ;
        if(ConnectionDetector.isInternetConnection(GSTLinkActivity_Unused.this))
        {
            GSTR2B2BAItemDetails gstr2B2BAItemDetails = new GSTR2B2BAItemDetails("S","S1991",6210.99,0,0,37.4,8874614.44,33.41,5.68,null);
            ArrayList<GSTR2B2BAItemDetails> itm_detList = new ArrayList<GSTR2B2BAItemDetails>();
            itm_detList.add(gstr2B2BAItemDetails);
            GSTR1B2BAInvoiceItems items = new GSTR1B2BAInvoiceItems(1,"A",itm_detList);
            ArrayList<GSTR1B2BAInvoiceItems> itemsArrayList = new ArrayList<GSTR1B2BAInvoiceItems>();
            itemsArrayList.add(items);
            GSTR1AB2BSData gstr1AB2BSData = new GSTR1AB2BSData("AflJufPlFStqKBZ","A","98678","25-10-2016",776522.02,"01","N","Y",itemsArrayList);
            ArrayList<GSTR1AB2BSData> listData = new ArrayList<GSTR1AB2BSData>();
            listData.add(gstr1AB2BSData);
            GSTR1AData gstr1AData = new GSTR1AData(dbGSTLink.getGSTIN(),"062016",3782969.01,listData);
            GSTRData gstrData = new GSTRData(userName,dbGSTLink.getGSTIN(),gstr1AData);
            progressDialog.show();
            String strJson = GstJsonEncoder.getGSTRJsonEncode(gstrData);
            new HTTPAsyncTask(GSTLinkActivity_Unused.this, HTTPAsyncTask.HTTP_POST,strJson,REQUEST_SAVE_GSTR1A, Config.GSTR1A_URL).execute();
        }
        else
        {
            Toast.makeText(GSTLinkActivity_Unused.this, "No Internet Connection! Try again Later", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickFileGstr1(View view) {
        startActivity(new Intent(getApplicationContext(),GSTFileActivity.class));
    }

    public void onClickFileGstr1A(View view) {
        startActivity(new Intent(getApplicationContext(),GSTFileActivity.class));
    }

    public void onClickGetGstr2B2B(View view) {
        progressDialog.show();
        new HTTPAsyncTask(GSTLinkActivity_Unused.this, HTTPAsyncTask.HTTP_GET,"",REQUEST_GET_GSTR2_B2B, Config.GSTR2_B2B_GET_API).execute();
    }

    public void onClickSaveGstr2(View view) {
        String startDate = DateUtil.getDateForDatePicker(etReportDateStart.getText().toString()) ;
        String endDate = DateUtil.getDateForDatePicker(etReportDateEnd.getText().toString()) ;
        if(startDate.equalsIgnoreCase("") || endDate.equalsIgnoreCase(""))
        {
            disMiss();
            MsgBox.setMessage("Please select Date")
                    .setPositiveButton("OK", null)
                    .show();
        }
        else
        {
            promptAuthFragment(REQUEST_SAVE_GSTR2);
        }
    }

    public void onClickFileGstr2(View view) {
        startActivity(new Intent(getApplicationContext(),GSTFileActivity.class));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);
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
                    .setTitle("Are you sure you want to exit ?")
                    .setView(vwAuthorization)
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            /*Intent returnIntent =new Intent();
                            setResult(Activity.RESULT_OK,returnIntent);*/
                            finish();
                        }
                    })
                    .show();
        }

        return super.onKeyDown(keyCode, event);
    }
}
