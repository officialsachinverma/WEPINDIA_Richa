/*
package com.wep.gstcall.api;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.wep.common.app.gst.GSTR1B2CSData;
import com.wep.common.app.gst.GSTR1Data;
import com.wep.common.app.gst.GSTR2B2BAItemDetails;
import com.wep.common.app.gst.GSTR2B2BITCDetails;
import com.wep.common.app.gst.GSTR2B2BInvoiceItems;
import com.wep.common.app.gst.GSTR2B2BItemDetails;
import com.wep.common.app.gst.GSTR2Data;
import com.wep.common.app.gst.GSTRData;
import com.wep.gstcall.api.http.HTTPAsyncTask;
import com.wep.gstcall.api.util.Config;
import com.wep.gstcall.api.util.GstJsonEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements HTTPAsyncTask.OnHTTPRequestCompletedListener {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
    }

    public void B2CSApiCall(View view) {
        progressDialog.show();
        GSTR1Data gstr1Data = getB2CSData();
        GSTRData gstrData = new GSTRData("priyabrat123","07CQZCD1111I4Z7",gstr1Data);
        String str = GstJsonEncoder.getGSTRJsonEncode(gstrData);
        new HTTPAsyncTask(MainActivity.this,HTTPAsyncTask.HTTP_POST,str,0, Config.GSTR1_URL).execute();
    }

    public void Gstr2ApiCall(View view) {
        GSTR2Data gstr2Data = getGSTRData();
        GSTRData gstrData = new GSTRData("priyabrat123","07CQZCD1111I4Z7",gstr2Data);
        String str = GstJsonEncoder.getGSTRJsonEncode(gstrData);
        new HTTPAsyncTask(MainActivity.this,HTTPAsyncTask.HTTP_POST,str,1, Config.GSTR2_URL).execute();
    }

    @Override
    public void onHttpRequestComplete(int requestCode, String data) {
        progressDialog.dismiss();
        if(data!=null)
        {
            if(*/
/*requestCode == 0*//*
true) // GSTR1
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
            else if(requestCode == 1) // GSTR2
            {

            }
        }
        else
        {
            Toast.makeText(this, "Sending error", Toast.LENGTH_SHORT).show();
        }
    }

    private GSTR1Data getB2CSData() {
        ArrayList<GSTR1B2CSData> list = new ArrayList<GSTR1B2CSData>();
        GSTR1B2CSData b2CSData = new GSTR1B2CSData("D","AflJufPlFStqKBZ","7","G","S1173",9595.33,0.0,0.0,22.0,28.54,39.26,978922198.61,"Y");
        list.add(b2CSData);
        list.add(b2CSData);
        GSTR1Data gstr1Data = new GSTR1Data("25ABCDE1028F6Z4","062016",3782969.01,list,null,null);
        return gstr1Data;
    }
    private GSTR2Data getGSTRData() {
        ArrayList<GSTR2B2BData> gstr2B2BDatasList = getGSTR2B2BData();
        ArrayList<GSTR2B2BAData> gstr2B2BADatasList = getGSTR2B2BAData();
        GSTR2Data data = new GSTR2Data("07CQZCD1111I4Z7","082016",1000,1000,gstr2B2BDatasList,gstr2B2BADatasList,null);
        return data;
    }

    private ArrayList<GSTR2B2BData> getGSTR2B2BData() {
        GSTR2_ITC_Details gstr2B2BITCDetails = new GSTR2_ITC_Details(0,23,23,0,23,23);
        GSTR2B2BItemDetails gstr2B2BItemDetails = new GSTR2B2BItemDetails("S","H724",5589.87,0,0,87.92,579475625.68,86.56,50.74,"ip");
        GSTR2B2BInvoiceItems gstr2InvoiceItems1 = new GSTR2B2BInvoiceItems(1,"A",gstr2B2BITCDetails,gstr2B2BItemDetails);
        //GSTR2InvoiceItems gstr2InvoiceItems2 = new GSTR2InvoiceItems(1,"A",gstr2B2BITCDetails,gstr2B2BItemDetails);
        ArrayList<GSTR2B2BInvoiceItems> gstr2InvoiceItemsList = new ArrayList<GSTR2B2BInvoiceItems>();
        gstr2InvoiceItemsList.add(gstr2InvoiceItems1);
        //gstr2InvoiceItemsList.add(gstr2InvoiceItems2);
        GSTR2B2BInvoices invoices1 = new GSTR2B2BInvoices("M","AflJufPlFStqKBZ","19081","05-02-2016",387395.25,"0","Yes",gstr2InvoiceItemsList);
        ArrayList<GSTR2B2BInvoices> invoicesList = new ArrayList<GSTR2B2BInvoices>();
        invoicesList.add(invoices1);
        GSTR2B2BData gstr2B2BData = new GSTR2B2BData("06ADECO9084R5Z4",invoicesList);
        ArrayList<GSTR2B2BData> gstr2B2BDatasList = new ArrayList<GSTR2B2BData>();
        gstr2B2BDatasList.add(gstr2B2BData);
        return gstr2B2BDatasList;
    }
    private ArrayList<GSTR2B2BAData> getGSTR2B2BAData() {
        GSTR2B2BAITCDetails gstr2B2BITCDetails = new GSTR2B2BAITCDetails(0,23,23,0,23,23);
        GSTR2B2BAItemDetails gstr2B2BItemDetails = new GSTR2B2BAItemDetails("S","H724",5589.87,0,0,87.92,579475625.68,86.56,50.74,"ip");
        GSTR2B2BAInvoiceItems gstr2InvoiceItems1 = new GSTR2B2BAInvoiceItems(1,"A",gstr2B2BITCDetails,gstr2B2BItemDetails);
        //GSTR2InvoiceItems gstr2InvoiceItems2 = new GSTR2InvoiceItems(1,"A",gstr2B2BITCDetails,gstr2B2BItemDetails);
        ArrayList<GSTR2B2BAInvoiceItems> gstr2InvoiceItemsList = new ArrayList<GSTR2B2BAInvoiceItems>();
        gstr2InvoiceItemsList.add(gstr2InvoiceItems1);
        //gstr2InvoiceItemsList.add(gstr2InvoiceItems2);
        GSTR2B2BAInvoices invoices1 = new GSTR2B2BAInvoices("M","AflJufPlFStqKBZ","19081","05-02-2016",387395.25,"0",gstr2InvoiceItemsList,"14565","19-08-2016");
        ArrayList<GSTR2B2BAInvoices> invoicesList = new ArrayList<GSTR2B2BAInvoices>();
        invoicesList.add(invoices1);
        GSTR2B2BAData gstr2B2BData = new GSTR2B2BAData("06ADECO9084R5Z4",invoicesList);
        ArrayList<GSTR2B2BAData> gstr2B2BDatasList = new ArrayList<GSTR2B2BAData>();
        gstr2B2BDatasList.add(gstr2B2BData);
        return gstr2B2BDatasList;
    }

}
*/
