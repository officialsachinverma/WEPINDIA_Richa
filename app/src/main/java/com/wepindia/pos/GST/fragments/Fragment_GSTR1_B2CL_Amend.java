package com.wepindia.pos.GST.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.models.GSTR2_B2B_Amend;
import com.wepindia.pos.GST.Adapter.GSTR1_B2CL_AmendAdapter;
import com.wepindia.pos.GenericClasses.DateTime;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class Fragment_GSTR1_B2CL_Amend extends Fragment {
    Context myContext;
    DatabaseHandler dbAmmend_b2cl_GSTR1;
    MessageDialog MsgBox;// = new MessageDialog(HeaderFooterActivity.this);
    EditText et_invno_ori, et_invdate_ori,  et_invno_rev, et_invdate_rev;
    EditText et_hsn,et_taxval,et_igstrate, et_igstamt,et_recipientStateCode, et_recipientName,et_cessamt,et_val;
    Spinner spnr_g_s;
    ImageButton btnCal_ori, btnCal_rev;
    Spinner spnr_CustStateCode;

    com.wep.common.app.views.WepButton btnAdd, btnSave, btnClear,btnClose,btnLoad;
    ListView listview_gstr2_amend;
    TableRow rowItems;
    String TAG = Fragment_GSTR1_B2CL_Amend.class.getSimpleName();
    TextView tvTitleDate;// = (TextView) findViewById(R.id.tvTitleBarDate);
    ArrayList<GSTR2_B2B_Amend> ammendList;
    GSTR1_B2CL_AmendAdapter ammendAdapter ;

    public Fragment_GSTR1_B2CL_Amend() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            dbAmmend_b2cl_GSTR1 = new DatabaseHandler(getActivity());

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment__gstr1__b2_cl_amend, container, false);
        myContext = getActivity();
        MsgBox = new MessageDialog(myContext);
        try{
            dbAmmend_b2cl_GSTR1.CloseDatabase();
            dbAmmend_b2cl_GSTR1.CreateDatabase();
            dbAmmend_b2cl_GSTR1.OpenDatabase();

            MsgBox = new MessageDialog(myContext);
            InitializeViewVariables(view);
            Clear(null);

        }
        catch(Exception exp){
            Toast.makeText(myContext, "OnCreate: " + exp.getMessage(), Toast.LENGTH_LONG).show();
        }
        return view;
    }
    void InitializeViewVariables(View view)
    {
        et_invno_ori = (EditText)view.findViewById(R.id.et_invno_ori);
        et_invdate_ori = (EditText) view.findViewById(R.id.et_invdate_ori);

        et_invno_rev = (EditText) view.findViewById(R.id.et_invno_rev);
        et_invdate_rev = (EditText) view.findViewById(R.id.et_invdate_rev);

        et_recipientStateCode = (EditText) view.findViewById (R.id.et_recipientStateCode);
        et_recipientName = (EditText) view.findViewById (R.id.et_recipientName);
        et_taxval = (EditText) view.findViewById (R.id.et_taxval);
        et_igstrate = (EditText) view.findViewById (R.id.et_igstrate);

        et_igstamt = (EditText) view.findViewById (R.id.et_igstamt);
        et_cessamt = (EditText) view.findViewById (R.id.et_cessamt);
        et_val = (EditText) view.findViewById (R.id.et_val);

        et_hsn= (EditText)view.findViewById(R.id.et_hsn);
        btnCal_ori= (ImageButton)view.findViewById(R.id.btnCal_ori);
        btnCal_rev= (ImageButton)view.findViewById(R.id.btnCal_rev);

        spnr_g_s = (Spinner) view.findViewById(R.id.spnr_g_s);
        spnr_CustStateCode = (Spinner) view.findViewById(R.id.spnr_CustStateCode);
        btnAdd = (com.wep.common.app.views.WepButton) view.findViewById(R.id.btnAdd);
        btnSave = (com.wep.common.app.views.WepButton) view.findViewById(R.id.btnSave);
        btnClear = (com.wep.common.app.views.WepButton) view.findViewById(R.id.btnClear);
        btnClose = (com.wep.common.app.views.WepButton) view.findViewById(R.id.btnClose);
        btnLoad = (com.wep.common.app.views.WepButton) view.findViewById(R.id.btnLoad);
        listview_gstr2_amend = (ListView)view.findViewById(R.id.listview_gstr2_amend);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ammendList!=null)
                    ammendList.clear();
                ammendAdapter = null;
                listview_gstr2_amend.setAdapter(null);
                load(v,1);
                Add(v);
            }
        });
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ammendList!=null)
                    ammendList.clear();
                ammendAdapter = null;
                listview_gstr2_amend.setAdapter(null);
                load(v,0);
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
        btnCal_ori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateSelection(1);
            }
        });
        btnCal_rev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateSelection(2);
            }
        });


    }

    private void DateSelection(final int DateType) {        // Original Date: DateType = 1 RevisedDate: DateType = 2
        try {
            AlertDialog.Builder dlgReportDate = new AlertDialog.Builder(myContext);
            final DatePicker dateReportDate = new DatePicker(myContext);
            Date date = new Date();
            DateTime objDate = new DateTime(new SimpleDateFormat("yyyy-MM-dd").format(date));
            dateReportDate.updateDate(objDate.getYear(), objDate.getMonth(), objDate.getDay());
            String strMessage = "";
            if (DateType == 1) {
                strMessage = "Select Original Invoice date";
            } else {
                strMessage = "Select Revised Invoice date";
            }

            dlgReportDate
                    .setIcon(R.drawable.ic_launcher)
                    .setTitle("Date Selection")
                    .setMessage(strMessage)
                    .setView(dateReportDate)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            // richa date format change
                            String strDate = "";
                            //strDate = String.valueOf(dateReportDate.getYear()) + "-";
                            if (dateReportDate.getDayOfMonth() < 10) {
                                strDate = "0" + String.valueOf(dateReportDate.getDayOfMonth())+"-";
                            } else {
                                strDate = String.valueOf(dateReportDate.getDayOfMonth())+"-";
                            }
                            if (dateReportDate.getMonth() < 9) {
                                strDate += "0" + String.valueOf(dateReportDate.getMonth() + 1) + "-";
                            } else {
                                strDate += String.valueOf(dateReportDate.getMonth() + 1) + "-";
                            }

                            strDate += String.valueOf(dateReportDate.getYear());

                            if (DateType == 1) {
                                et_invdate_ori.setText(strDate);
                            } else {
                                et_invdate_rev.setText(strDate);
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    })
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load(View v,int from)
    {
        String recipientStateCode = "", recipientName= "", invoiceNo= "", invoiceDate = "",CustStateCode="";
        try{
        if(ammendList == null)
            ammendList = new ArrayList<GSTR2_B2B_Amend>();
        recipientStateCode = et_recipientStateCode.getText().toString();
        recipientName = et_recipientName.getText().toString();
        invoiceNo = et_invno_ori.getText().toString();
        invoiceDate = et_invdate_ori.getText().toString();
        String custStateCode_str = spnr_CustStateCode.getSelectedItem().toString();
        if (!custStateCode_str.equals(""))
            CustStateCode = custStateCode_str.substring(custStateCode_str.length()-2, custStateCode_str.length());
        Date dd  = new SimpleDateFormat("dd-MM-yyyy").parse(invoiceDate);
        Cursor cursor = dbAmmend_b2cl_GSTR1.getAmmends_GSTR1_b2cl(recipientName,recipientStateCode,invoiceNo,String.valueOf(dd.getTime()),CustStateCode);
        int count =1;
        while (cursor != null && cursor.moveToNext()) {
            GSTR2_B2B_Amend ammend = new GSTR2_B2B_Amend();
            ammend.setSno(count++);
            ammend.setRecipientName(cursor.getString(cursor.getColumnIndex("CustName")));
            ammend.setRecipientStateCode(cursor.getString(cursor.getColumnIndex("POS")));
            long invdate_ori = cursor.getLong(cursor.getColumnIndex("OriginalInvoiceDate"));
            Date date = new Date(invdate_ori);
            String dd1 = new SimpleDateFormat("dd-MM-yyyy").format(date);
            ammend.setInvoiceDate_ori(dd1);
            ammend.setInvoiceNo_ori(cursor.getString(cursor.getColumnIndex("OriginalInvoiceNo")));
            ammend.setInvoiceNo_rev(cursor.getString(cursor.getColumnIndex("InvoiceNo")));
            long invdate_rev = cursor.getLong(cursor.getColumnIndex("InvoiceDate"));
            dd1 = new SimpleDateFormat("dd-MM-yyyy").format(invdate_rev);
            ammend.setInvoiceDate_rev(dd1);
            ammend.setType(cursor.getString(cursor.getColumnIndex("SupplyType")));
            ammend.setHSn(cursor.getString(cursor.getColumnIndex("HSNCode")));
            ammend.setValue(cursor.getDouble(cursor.getColumnIndex("Value")));
            ammend.setTaxableValue(cursor.getDouble(cursor.getColumnIndex("TaxableValue")));
            ammend.setIgstrate(cursor.getFloat(cursor.getColumnIndex("IGSTRate")));
            ammend.setIgstamt(cursor.getFloat(cursor.getColumnIndex("IGSTAmount")));
            ammend.setPOS(cursor.getString(cursor.getColumnIndex("RevisedPOS")));
            ammend.setCustStateCode(cursor.getString(cursor.getColumnIndex("CustStateCode")));
            ammend.setCsamt(cursor.getDouble(cursor.getColumnIndex("cessAmount")));
            ammendList.add(ammend);
        }
    }catch (Exception e)
    {
        e.printStackTrace();
    }
    if(from ==0 && ammendList.size()==0)
    {
        MsgBox.Show("Sorry", " No records found");
        return;
    }
    if(ammendAdapter == null)
    {
        ammendAdapter = new GSTR1_B2CL_AmendAdapter(getActivity(),ammendList, dbAmmend_b2cl_GSTR1,
                recipientStateCode,recipientName,invoiceNo,invoiceDate,CustStateCode);
        listview_gstr2_amend.setAdapter(ammendAdapter);
    }
    else
    {
        ammendAdapter.notifyNewDataAdded(ammendList);
    }
    }

    public int getIndexPOS(String item)
    {

        List<String> posList = Arrays.asList(getResources().getStringArray(R.array.poscode));
        int index =0;
        for (String poscode:posList) {
            if(poscode.contains(item))
                return index;
            index++;
        }

        return 0;
    }

    void Reset()
    {
        //et_gstin_ori.setText("12ANTPA0870E1A1");
        et_recipientStateCode.setText("");
        et_recipientName.setText("");
        et_invno_ori.setText("");
        et_invdate_ori.setText("");

        et_invno_rev.setText("");
        et_invdate_rev.setText("");
        //et_value.setText("500");
        //et_pos.setText("14");
        spnr_CustStateCode.setSelection(0);
        spnr_g_s.setSelection(0);
        et_taxval.setText("");
        et_hsn.setText("");
        et_igstrate.setText("0");
       /* et_sgstrate.setText("0");
        et_cgstrate.setText("0");*/
        et_val.setText("0");
        et_igstamt.setText("0");
        et_cessamt.setText("0");
        /*et_sgstamt.setText("0");
        et_cgstamt.setText("0");
*/



        // btnSave.setEnabled(false);
        ammendAdapter = null;
        listview_gstr2_amend.setAdapter(null);
        if(ammendList!=null)
            ammendList.clear();

    }
/*
void Reset()
    {
        //et_gstin_ori.setText("12ANTPA0870E1A1");
        et_recipientStateCode.setText("12");
        et_recipientName.setText("neha");
        et_invno_ori.setText("23");
        et_invdate_ori.setText("01-05-2017");

        et_invno_rev.setText("50");
        et_invdate_rev.setText("01-05-2017");
        //et_value.setText("500");
        //et_pos.setText("14");
        spnr_CustStateCode.setSelection(getIndexPOS("29"));
        spnr_g_s.setSelection(0);
        et_taxval.setText("1000");
        et_hsn.setText("h5");
        et_igstrate.setText("0");
       */
/* et_sgstrate.setText("0");
        et_cgstrate.setText("0");*//*

        et_val.setText("0");
        et_igstamt.setText("0");
        et_cessamt.setText("0");
        */
/*et_sgstamt.setText("0");
        et_cgstamt.setText("0");
*//*




        // btnSave.setEnabled(false);
        ammendAdapter = null;
        listview_gstr2_amend.setAdapter(null);
        if(ammendList!=null)
            ammendList.clear();

    }
*/


    public void Add(View v)
    {
        String recipientStateCode = "", recipientName= "", invno_ori= "", invdate_ori = "",CustStateCode="";
        try {
        recipientStateCode = et_recipientStateCode.getText().toString();
        recipientName = et_recipientName.getText().toString();
        invno_ori = et_invno_ori.getText().toString();
        String invno_rev = et_invno_rev.getText().toString();
        invdate_ori = et_invdate_ori.getText().toString();
        String invdate_rev = et_invdate_rev.getText().toString();
        String hsn = et_hsn.getText().toString();
        //String pos1 = et_pos.getText().toString();
        String custStateCode_temp = spnr_CustStateCode.getSelectedItem().toString().trim();
        CustStateCode = "";
        if (!custStateCode_temp.equals(""))
        {
            int length = custStateCode_temp.length();
            CustStateCode = custStateCode_temp.substring(length - 2, length);
        }

        String supply = spnr_g_s.getSelectedItem().toString();

        String value = String.format("%.2f",Float.parseFloat(et_val.getText().toString()));
        String taxval = String.format("%.2f",Float.parseFloat(et_taxval.getText().toString()));
        String igstrate = String.format("%.2f",Float.parseFloat(et_igstrate.getText().toString()));
//        String cgstrate = String.format("%.2f",Float.parseFloat(et_cgstrate.getText().toString()));
//        String sgstrate = String.format("%.2f",Float.parseFloat(et_sgstrate.getText().toString()));
        String igstamt = String.format("%.2f",Float.parseFloat(et_igstamt.getText().toString()));
        String cessamt = String.format("%.2f",Float.parseFloat(et_cessamt.getText().toString()));
//        String cgstamt = String.format("%.2f",Float.parseFloat(et_cgstamt.getText().toString()));
//        String sgstamt = String.format("%.2f",Float.parseFloat(et_sgstamt.getText().toString()));

        int count =listview_gstr2_amend.getCount()+1;



        if (recipientName.equals("") || (invdate_ori.equals("")) || (invno_ori.equals(""))||
                (invdate_rev.equals("")) || (invno_rev.equals(""))|| (taxval.equals(""))||
                (igstrate.equals(""))|| (igstamt.equals(""))||cessamt.equals("") ||value.equals("") )
        {
            MsgBox.setTitle(" Error ")
                    .setMessage(" Please fill all details ")
                    .show();
        }else {

                GSTR2_B2B_Amend ammend = new GSTR2_B2B_Amend();
                ammend.setSno(count);
                ammend.setRecipientName(recipientName);
                ammend.setRecipientStateCode(recipientStateCode);
                ammend.setInvoiceNo_ori(invno_ori);
                ammend.setInvoiceDate_ori(invdate_ori);

                ammend.setInvoiceNo_rev(invno_rev);
                ammend.setInvoiceDate_rev(invdate_rev);
                ammend.setCustStateCode(CustStateCode);
                ammend.setHSn(hsn);
                ammend.setType(supply);
                ammend.setValue(Double.parseDouble(value));

                ammend.setTaxableValue(Double.parseDouble(taxval));
                ammend.setIgstrate(Float.parseFloat(igstrate));
                ammend.setIgstamt(Float.parseFloat(igstamt));
                ammend.setCsamt(Double.parseDouble(cessamt));

                long lResult = dbAmmend_b2cl_GSTR1.add_GSTR1_B2CLAmmend(ammend);
                if(lResult>0)
                {
                    Log.d(TAG,"new ammend inserted successfully");
                    if (ammendList == null) {
                        ammendList = new ArrayList<GSTR2_B2B_Amend>();
                    }
                    ammendList.add(ammend);
                    if (ammendAdapter == null) {
                        ammendAdapter = new GSTR1_B2CL_AmendAdapter(getActivity(),ammendList, dbAmmend_b2cl_GSTR1,
                                recipientStateCode,recipientName,invno_ori,invdate_ori,CustStateCode);
                        listview_gstr2_amend.setAdapter(ammendAdapter);
                    } else {
                        ammendAdapter.notifyNewDataAdded(ammendList);
                    }
                }

        }
    } catch (Exception e) {
        e.printStackTrace();
        MsgBox.Show("Error",e.getMessage());
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

                long l = dbAmmend_b2cl_GSTR1.add_GSTR2_B2BAmmend(gstin_ori, invno_ori , invdate_ori, gstin_rev, invno_rev ,invdate_rev,
                        gs,hsn,value,taxval,igstrate,cgstrate, sgstrate , igstamt, cgstamt ,sgstamt ,pos,"B2B",date);
            }

        }

    }*/
    public void Close(View v){
        dbAmmend_b2cl_GSTR1.CloseDatabase();
        getActivity().finish();
    }
}
