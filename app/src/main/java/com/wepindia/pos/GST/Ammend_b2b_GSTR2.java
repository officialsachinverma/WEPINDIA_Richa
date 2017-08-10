/****************************************************************************
 * Project Name		:	VAJRA
 *
 * File Name		:	HeaderFooterActivity
 *
 * Purpose			:	Represents bill header and footer configuration activity,
 * 						takes care of all UI back end operations in this activity,
 * 						such as event handling data read from or display in views.
 *
 * DateOfCreation	:	07-November-2012
 *
 * Author			:	Balasubramanya Bharadwaj B S
 *
 ****************************************************************************/
package com.wepindia.pos.GST;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.models.GSTR2_B2B_Amend;
import com.wepindia.pos.GST.Adapter.GSTR2_B2B_AmendAdapter;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Ammend_b2b_GSTR2 extends Activity{

    Context myContext;
    DatabaseHandler dbAmmend_b2b_GSTR2;
    MessageDialog MsgBox;// = new MessageDialog(HeaderFooterActivity.this);
    EditText et_gstin_ori, et_invno_ori, et_invdate_ori, et_gstin_rev, et_invno_rev, et_invdate_rev;
    EditText et_value, et_pos,et_hsn,et_taxval,et_igstrate,et_sgstrate,et_cgstrate;
    EditText et_igstamt, et_cgstamt, et_sgstamt;
    Spinner spnr_g_s;

    com.wep.common.app.views.WepButton btnAdd, btnSave, btnClear,btnClose,btnLoad;
    ListView listview_gstr2_amend;
    TableRow rowItems;
    private String TAG = Ammend_b2b_GSTR2.class.getSimpleName();
    TextView tvTitleDate;// = (TextView) findViewById(R.id.tvTitleBarDate);
    ArrayList<GSTR2_B2B_Amend> ammendList;
    GSTR2_B2B_AmendAdapter ammendAdapter ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ammend_b2b_inward);
        myContext = this;
        tvTitleDate = (TextView) findViewById(R.id.tvTitleBarDate);

        dbAmmend_b2b_GSTR2 = new DatabaseHandler(Ammend_b2b_GSTR2.this);
        MsgBox = new MessageDialog(myContext);


        try{
            dbAmmend_b2b_GSTR2.CreateDatabase();
            dbAmmend_b2b_GSTR2.OpenDatabase();
            InitializeViews();
            Reset();
        }
        catch(Exception exp){
            exp.printStackTrace();
            MsgBox.Show("Exception", exp.getMessage());
        }
    }

    void InitializeViews()
    {
        et_gstin_ori = (EditText) findViewById(R.id.et_gstin_ori);
        et_invno_ori = (EditText)findViewById(R.id.et_invno_ori);
        et_invdate_ori = (EditText) findViewById(R.id.et_invdate_ori);
        et_gstin_rev = (EditText) findViewById(R.id.et_gstin_rev);
        et_invno_rev = (EditText) findViewById(R.id.et_invno_rev);
        et_invdate_rev = (EditText) findViewById(R.id.et_invdate_rev);
        et_value = (EditText)findViewById(R.id.et_value);
        et_pos = (EditText) findViewById(R.id.et_pos);
        et_taxval = (EditText) findViewById (R.id.et_taxval);
        et_igstrate = (EditText) findViewById (R.id.et_igstrate);
        et_cgstrate = (EditText) findViewById (R.id.et_cgstrate);
        et_sgstrate = (EditText) findViewById (R.id.et_sgstrate);
        et_igstamt = (EditText) findViewById (R.id.et_igstamt);
        et_cgstamt = (EditText) findViewById (R.id.et_cgstamt);
        et_sgstamt = (EditText) findViewById (R.id.et_sgstamt);
        et_hsn= (EditText)findViewById(R.id.et_hsn);

        spnr_g_s = (Spinner) findViewById(R.id.spnr_g_s);
        btnAdd = (com.wep.common.app.views.WepButton) findViewById(R.id.btnAdd);
        btnSave = (com.wep.common.app.views.WepButton) findViewById(R.id.btnSave);
        btnClear = (com.wep.common.app.views.WepButton) findViewById(R.id.btnClear);
        btnClose = (com.wep.common.app.views.WepButton) findViewById(R.id.btnClose);
        btnLoad = (com.wep.common.app.views.WepButton) findViewById(R.id.btnLoad);
        listview_gstr2_amend = (ListView)findViewById(R.id.listview_gstr2_amend);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load(v);
                Add(v);
            }
        });
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load(v);
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clear(v);
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Close(v);
            }
        });


    }

    public void load(View v)
    {
        ammendList = new ArrayList<GSTR2_B2B_Amend>();
        String gstin = et_gstin_ori.getText().toString();
        String invoiceNo = et_invno_ori.getText().toString();
        String invoiceDate = et_invdate_ori.getText().toString();
        Cursor cursor = dbAmmend_b2b_GSTR2.getAmmends_GSTR2_b2b(gstin, invoiceNo, invoiceDate,"");
        int count =1;
        while(cursor!=null && cursor.moveToNext())
        {
            GSTR2_B2B_Amend ammend = new GSTR2_B2B_Amend();
            ammend.setSno(count++);
            ammend.setGstin_ori(cursor.getString(cursor.getColumnIndex("GSTIN_Ori")));
            ammend.setInvoiceNo_ori(cursor.getString(cursor.getColumnIndex("OriginalInvoiceNo")));
            ammend.setInvoiceDate_ori(cursor.getString(cursor.getColumnIndex("OriginalInvoiceDate")));
            ammend.setGstin_rev(cursor.getString(cursor.getColumnIndex("GSTIN")));
            ammend.setInvoiceNo_rev(cursor.getString(cursor.getColumnIndex("InvoiceNo")));
            ammend.setInvoiceDate_rev(cursor.getString(cursor.getColumnIndex("InvoiceDate")));
            ammend.setValue(cursor.getDouble(cursor.getColumnIndex("Value")));
            ammend.setType(cursor.getString(cursor.getColumnIndex("SupplyType")));
            ammend.setHSn(cursor.getString(cursor.getColumnIndex("HSNCode")));
            ammend.setTaxableValue(cursor.getDouble(cursor.getColumnIndex("TaxableValue")));
            ammend.setIgstrate(cursor.getFloat(cursor.getColumnIndex("IGSTRate")));
            ammend.setCgstrate(cursor.getFloat(cursor.getColumnIndex("CGSTRate")));
            ammend.setSgstrate(cursor.getFloat(cursor.getColumnIndex("SGSTRate")));
            ammend.setIgstamt(cursor.getFloat(cursor.getColumnIndex("IGSTAmount")));
            ammend.setCgstamt(cursor.getFloat(cursor.getColumnIndex("CGSTAmount")));
            ammend.setSgstamt(cursor.getFloat(cursor.getColumnIndex("SGSTAmount")));
            ammend.setPOS(cursor.getString(cursor.getColumnIndex("POS")));
            ammendList.add(ammend);
        }
        if(ammendAdapter == null)
        {
            ammendAdapter = new GSTR2_B2B_AmendAdapter(this,ammendList,dbAmmend_b2b_GSTR2,2,"","","","");
            listview_gstr2_amend.setAdapter(ammendAdapter);
        }
        else
        {
            ammendAdapter.notifyNewDataAdded(ammendList);
        }
    }

    void Reset()
    {
        et_gstin_ori.setText("12ANTPA0870E1A1");
        et_invno_ori.setText("23");
        et_invdate_ori.setText("12-11-2016");
        et_gstin_rev.setText("12ANTPA0870E1A1");
        et_invno_rev.setText("50");
        et_invdate_rev.setText("22-11-2016");
        et_value.setText("500");
        et_pos.setText("14");
        et_taxval.setText("1000");
        et_hsn.setText("h5");
        et_igstrate.setText("0");
        et_sgstrate.setText("0");
        et_cgstrate.setText("0");
        et_igstamt.setText("0");
        et_sgstamt.setText("0");
        et_cgstamt.setText("0");

        spnr_g_s = (Spinner) findViewById(R.id.spnr_g_s);


       // btnSave.setEnabled(false);
        ammendAdapter = null;

    }


    public void Add(View v)
    {
        String gstin_ori = et_gstin_ori.getText().toString();
        String gstin_rev = et_gstin_rev.getText().toString();
        String invno_ori = et_invno_ori.getText().toString();
        String invno_rev = et_invno_rev.getText().toString();
        String invdate_ori = et_invdate_ori.getText().toString();
        String invdate_rev = et_invdate_rev.getText().toString();
        String hsn = et_hsn.getText().toString();
        String pos1 = et_pos.getText().toString();

        String supply = spnr_g_s.getSelectedItem().toString();

        String value = String.format("%.2f",Float.parseFloat(et_value.getText().toString()));
        String taxval = String.format("%.2f",Float.parseFloat(et_taxval.getText().toString()));
        String igstrate = String.format("%.2f",Float.parseFloat(et_igstrate.getText().toString()));
        String cgstrate = String.format("%.2f",Float.parseFloat(et_cgstrate.getText().toString()));
        String sgstrate = String.format("%.2f",Float.parseFloat(et_sgstrate.getText().toString()));
        String igstamt = String.format("%.2f",Float.parseFloat(et_igstamt.getText().toString()));
        String cgstamt = String.format("%.2f",Float.parseFloat(et_cgstamt.getText().toString()));
        String sgstamt = String.format("%.2f",Float.parseFloat(et_sgstamt.getText().toString()));

        int count =listview_gstr2_amend.getCount()+1;



        if (gstin_ori.equals("") || (invdate_ori.equals("")) || (invno_ori.equals(""))||(gstin_rev.equals(""))||
                (invdate_rev.equals("")) || (invno_rev.equals(""))||(pos1.equals(""))||
                (value.equals("")) || (taxval.equals(""))||
                (igstrate.equals(""))|| (sgstrate.equals(""))||(cgstrate.equals("")) ||
                (igstamt.equals(""))|| (sgstamt.equals(""))||(cgstamt.equals("")))
        {
            MsgBox.setTitle(" Error ")
                    .setMessage(" Please fill all details ")
                    .show();
        }else {
            GSTR2_B2B_Amend ammend = new GSTR2_B2B_Amend();
            ammend.setSno(count);
            ammend.setGstin_ori(gstin_ori);
            ammend.setInvoiceNo_ori(invno_ori);
            ammend.setInvoiceDate_ori(invdate_ori);
            ammend.setGstin_rev(gstin_rev);
            ammend.setInvoiceNo_rev(invno_rev);
            ammend.setInvoiceDate_rev(invdate_rev);
            ammend.setPOS(pos1);
            ammend.setHSn(hsn);
            ammend.setType(supply);
            ammend.setValue(Double.parseDouble(value));
            ammend.setTaxableValue(Double.parseDouble(taxval));
            ammend.setIgstrate(Float.parseFloat(igstrate));
            ammend.setIgstamt(Float.parseFloat(igstamt));
            ammend.setCgstrate(Float.parseFloat(cgstrate));
            ammend.setCgstamt(Float.parseFloat(cgstamt));
            ammend.setSgstrate(Float.parseFloat(sgstrate));
            ammend.setSgstamt(Float.parseFloat(sgstamt));
            if(ammendList == null)
            {
                ammendList = new ArrayList<GSTR2_B2B_Amend>();
            }
            ammendList.add(ammend);
            if(ammendAdapter == null)
            {
                ammendAdapter = new GSTR2_B2B_AmendAdapter(this,ammendList,dbAmmend_b2b_GSTR2,2,"","","","");
                listview_gstr2_amend.setAdapter(ammendAdapter);
            }
            else
            {
                ammendAdapter.notifyNewDataAdded(ammendList);
            }
        }


    }

    public void Clear(View v) {
        Reset();
    }

    /*public void Save(View V)
    {
         int count = tbl_data.getChildCount();
        if (count<1)
        {
            MsgBox.setMessage("Please Add the Entry")
                    .setNeutralButton("OK",null)
                    .show();
            return;
        }

        for (int i=0; i<count; i++)
        {
            TableRow row = (TableRow) tbl_data.getChildAt(i);
            if (row.getChildAt(0)!= null)
            {
                TextView tvgstin_ori = (TextView) row.getChildAt(1);
                TextView tvinvno_ori = (TextView) row.getChildAt(2);
                TextView tvinvdate_ori = (TextView) row.getChildAt(3);
                TextView tvgstin_rev = (TextView) row.getChildAt(4);
                TextView tvinvno_rev = (TextView) row.getChildAt(5);
                TextView tvinvdate_rev = (TextView) row.getChildAt(6);
                TextView tvhsn = (TextView) row.getChildAt(7);
                TextView tvgs = (TextView) row.getChildAt(8);
                TextView tvvalue = (TextView) row.getChildAt(9);
                TextView tvtaxval= (TextView) row.getChildAt(10);
                TextView tvigstrate = (TextView) row.getChildAt(11);
                TextView tvigstamt = (TextView) row.getChildAt(12);
                TextView tvcgstrate = (TextView) row.getChildAt(13);
                TextView tvcgstamt = (TextView) row.getChildAt(14);
                TextView tvsgstrate = (TextView) row.getChildAt(15);
                TextView tvsgstamt = (TextView) row.getChildAt(16);
                TextView tvpos = (TextView) row.getChildAt(17);

                String gstin_ori = tvgstin_ori.getText().toString();
                String invno_ori = tvinvno_ori.getText().toString();
                String invdate_ori = tvinvdate_ori.getText().toString();
                String gstin_rev = tvgstin_rev.getText().toString();
                String invno_rev = tvinvno_rev.getText().toString();
                String invdate_rev = tvinvdate_rev.getText().toString();
                String hsn = tvhsn.getText().toString();
                String gs = tvgs.getText().toString();
                String value = tvvalue.getText().toString();
                String taxval = tvtaxval.getText().toString();
                String igstrate = tvigstrate.getText().toString();
                String cgstrate = tvcgstrate.getText().toString();
                String sgstrate = tvsgstrate.getText().toString();
                String igstamt = tvigstamt.getText().toString();
                String cgstamt = tvcgstamt.getText().toString();
                String sgstamt = tvsgstamt.getText().toString();
                String pos = tvpos.getText().toString();
                Calendar c =  Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String date = sdf.format(c.getTime());

                long l = dbAmmend_b2b_GSTR2.add_GSTR2_B2BAmmend(gstin_ori, invno_ori , invdate_ori, gstin_rev, invno_rev ,invdate_rev,
                        gs,hsn,value,taxval,igstrate,cgstrate, sgstrate , igstamt, cgstamt ,sgstamt ,pos,"B2B",date);
            }

        }

    }*/
    public void Close(View v){
        dbAmmend_b2b_GSTR2.CloseDatabase();
        this.finish();
    }
}
