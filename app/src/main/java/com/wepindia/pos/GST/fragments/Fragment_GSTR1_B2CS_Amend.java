package com.wepindia.pos.GST.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.models.GSTR2_B2B_Amend;
import com.wepindia.pos.GST.Adapter.GSTR1_B2CS_AmendAdapter;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.GenericClasses.MonthYearPicker;
import com.wepindia.pos.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Fragment_GSTR1_B2CS_Amend extends Fragment {
    Context myContext;
    DatabaseHandler dbAmmend_b2cs_GSTR1;
    MessageDialog MsgBox;// = new MessageDialog(HeaderFooterActivity.this);
    EditText et_taxMonth,et_hsn_ori,et_hsn_rev;
    EditText et_igstamt, et_cgstamt, et_sgstamt,et_taxval,et_igstrate,et_sgstrate,et_cgstrate,et_cessamt;
    Spinner spnr_g_s_ori, spnr_g_s_rev;
    ImageButton btnMonthPicker;
    Spinner spnr_pos_ori, spnr_CustStateCode;

    com.wep.common.app.views.WepButton btnAdd, btnSave, btnClear,btnClose,btnLoad;
    ListView listview_gstr2_amend;

    String TAG = Fragment_GSTR1_B2CL_Amend.class.getSimpleName();

    ArrayList<GSTR2_B2B_Amend> ammendList;
    GSTR1_B2CS_AmendAdapter ammendAdapter ;

    public Fragment_GSTR1_B2CS_Amend() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            dbAmmend_b2cs_GSTR1 = new DatabaseHandler(getActivity());

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment__gstr1__b2_cs__amend, container, false);
        myContext = getActivity();
        MsgBox = new MessageDialog(myContext);

        try{
            dbAmmend_b2cs_GSTR1.CloseDatabase();
            dbAmmend_b2cs_GSTR1.CreateDatabase();
            dbAmmend_b2cs_GSTR1.OpenDatabase();

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
        et_hsn_ori= (EditText)view.findViewById(R.id.et_hsn_ori);
        et_hsn_rev= (EditText)view.findViewById(R.id.et_hsn_rev);
        spnr_g_s_ori = (Spinner) view.findViewById(R.id.spnr_g_s_ori);
        spnr_pos_ori = (Spinner) view.findViewById(R.id.spnr_pos_ori);
        spnr_g_s_rev = (Spinner) view.findViewById(R.id.spnr_g_s_rev);
        spnr_CustStateCode = (Spinner) view.findViewById(R.id.spnr_CustStateCode);
        et_taxMonth= (EditText)view.findViewById(R.id.et_taxMonth);
        btnMonthPicker= (ImageButton)view.findViewById(R.id.btnMonthPicker);




        et_taxval = (EditText) view.findViewById (R.id.et_taxval);
        et_igstrate = (EditText) view.findViewById (R.id.et_igstrate);
        et_cgstrate = (EditText) view.findViewById (R.id.et_cgstrate);
        et_sgstrate = (EditText) view.findViewById (R.id.et_sgstrate);
        et_igstamt = (EditText) view.findViewById (R.id.et_igstamt);
        et_cgstamt = (EditText) view.findViewById (R.id.et_cgstamt);
        et_sgstamt = (EditText) view.findViewById (R.id.et_sgstamt);
        et_cessamt = (EditText) view.findViewById (R.id.et_cessamt);

        btnAdd = (com.wep.common.app.views.WepButton) view.findViewById(R.id.btnAdd);
       //btnSave = (com.wep.common.app.views.WepButton) view.findViewById(R.id.btnSave);
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
        btnMonthPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthPicker(v);
            }
        });



    }

    public void MonthPicker(View view) {
        try {
//            AlertDialog.Builder dlgReportDate = new AlertDialog.Builder(myContext);
            final MonthYearPicker myp = new MonthYearPicker(getActivity());
            myp.build(new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    et_taxMonth.setText(myp.getSelectedMonthName() + myp.getSelectedYear());
                }
            }, null);
            myp.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void load(View v,int from)
    {   try{
        if(ammendList == null)
            ammendList = new ArrayList<GSTR2_B2B_Amend>();
        String taxMonth = et_taxMonth.getText().toString();
        String hsn_ori = et_hsn_ori.getText().toString();
        Cursor cursor = dbAmmend_b2cs_GSTR1.getAmmends_GSTR1_b2cs(hsn_ori,taxMonth);
        int count =1;
        while (cursor != null && cursor.moveToNext()) {
            GSTR2_B2B_Amend ammend = new GSTR2_B2B_Amend();
            ammend.setSno(count++);
            ammend.setTaxMonth(cursor.getString(cursor.getColumnIndex("Month")));
            ammend.setHsn_ori(cursor.getString(cursor.getColumnIndex("HSNCode")));
            ammend.setType_ori(cursor.getString(cursor.getColumnIndex("SupplyType")));
            ammend.setPos_ori(cursor.getString(cursor.getColumnIndex("POS")));
            ammend.setHsn_rev(cursor.getString(cursor.getColumnIndex("ReviseHSNCode")));
            ammend.setType_rev(cursor.getString(cursor.getColumnIndex("RevisedSupplyType")));
            ammend.setCustStateCode(cursor.getString(cursor.getColumnIndex("CustStateCode")));
            ammend.setTaxableValue(cursor.getDouble(cursor.getColumnIndex("TaxableValue")));
            ammend.setIgstrate(cursor.getFloat(cursor.getColumnIndex("IGSTRate")));
            ammend.setCgstrate(cursor.getFloat(cursor.getColumnIndex("CGSTRate")));
            ammend.setSgstrate(cursor.getFloat(cursor.getColumnIndex("SGSTRate")));
            ammend.setIgstamt(cursor.getFloat(cursor.getColumnIndex("IGSTAmount")));
            ammend.setCgstamt(cursor.getFloat(cursor.getColumnIndex("CGSTAmount")));
            ammend.setSgstamt(cursor.getFloat(cursor.getColumnIndex("SGSTAmount")));
            ammend.setCsamt(cursor.getFloat(cursor.getColumnIndex("cessAmount")));

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
            ammendAdapter = new GSTR1_B2CS_AmendAdapter(getActivity(),ammendList, dbAmmend_b2cs_GSTR1);
            listview_gstr2_amend.setAdapter(ammendAdapter);
        }
        else
        {
            ammendAdapter.notifyNewDataAdded(ammendList);
        }
    }

    void Reset()
    {
        et_hsn_ori.setText("-");
        et_hsn_rev.setText("-");
        spnr_pos_ori.setSelection(0);
        spnr_CustStateCode.setSelection(0);
        spnr_g_s_ori.setSelection(0);
        spnr_g_s_rev.setSelection(0);
        et_taxMonth.setText("");

        et_taxval.setText("0");
        et_igstrate.setText("0");
        et_sgstrate.setText("0");
        et_cgstrate.setText("0");
        et_igstamt.setText("0");
        et_sgstamt.setText("0");
        et_cgstamt.setText("0");
        et_cessamt.setText("0");

        // btnSave.setEnabled(false);
        ammendAdapter = null;
        listview_gstr2_amend.setAdapter(null);
        if(ammendList!=null)
            ammendList.clear();

    }
/*
void Reset()
    {
        et_hsn_ori.setText("h5");
        et_hsn_rev.setText("h6");
        spnr_pos_ori.setSelection(17);
        spnr_CustStateCode.setSelection(18);
        spnr_g_s_ori.setSelection(0);
        spnr_g_s_rev.setSelection(0);
        et_taxMonth.setText("");

        et_taxval.setText("1000");
        et_igstrate.setText("0");
        et_sgstrate.setText("0");
        et_cgstrate.setText("0");
        et_igstamt.setText("0");
        et_sgstamt.setText("0");
        et_cgstamt.setText("0");
        et_cessamt.setText("0");

        // btnSave.setEnabled(false);
        ammendAdapter = null;
        listview_gstr2_amend.setAdapter(null);
        if(ammendList!=null)
            ammendList.clear();

    }
*/


    public void Add(View v)
    {try{
        String taxMonth = et_taxMonth.getText().toString();
        String hsn_ori = et_hsn_ori.getText().toString();
        String type_ori = spnr_g_s_ori.getSelectedItem().toString();
        String str = spnr_pos_ori.getSelectedItem().toString().trim();
        String pos1 = "";
        if (!str.equals(""))
        {
            int length = str.length();
            pos1 = str.substring(length - 2, length);
        }

        String hsn_rev = et_hsn_rev.getText().toString();
        String type_rev = spnr_g_s_rev.getSelectedItem().toString();
        str = spnr_CustStateCode.getSelectedItem().toString().trim();
        String custStateCode = "";
        if (!str.equals(""))
        {
            int length = str.length();
            custStateCode = str.substring(length - 2, length);
        }


        String taxval1 = et_taxval.getText().toString();
        String igstrate1 = et_igstrate.getText().toString();
        String cgstrate1 = et_cgstrate.getText().toString();
        String sgstrate1 = et_sgstrate.getText().toString();
        String igstamt1 = et_igstamt.getText().toString();
        String cgstamt1 = et_cgstamt.getText().toString();
        String sgstamt1 = et_sgstamt.getText().toString();
        String cessamt1 = et_cessamt.getText().toString();


        int count =listview_gstr2_amend.getCount()+1;



        /*if (taxMonth.equals("") || pos1.equals("")|| custStateCode.equals("")||(taxval1.equals(""))||
                (igstrate1.equals(""))|| (sgstrate1.equals(""))||(cgstrate1.equals("")) ||
                (igstamt1.equals(""))|| (sgstamt1.equals(""))||(cgstamt1.equals("")))*/
        if (taxMonth.equals("") || custStateCode.equals("")||(taxval1.equals(""))||
                (igstrate1.equals(""))|| (sgstrate1.equals(""))||(cgstrate1.equals("")) ||
                (igstamt1.equals(""))|| (sgstamt1.equals(""))||(cgstamt1.equals("")) || cessamt1.equals(""))
        {
            MsgBox.Show("Error", " Please fill all details ");
        }else {
            String taxval = String.format("%.2f",Float.parseFloat(et_taxval.getText().toString()));
            String igstrate = String.format("%.2f",Float.parseFloat(et_igstrate.getText().toString()));
            String cgstrate = String.format("%.2f",Float.parseFloat(et_cgstrate.getText().toString()));
            String sgstrate = String.format("%.2f",Float.parseFloat(et_sgstrate.getText().toString()));
            String igstamt = String.format("%.2f",Float.parseFloat(et_igstamt.getText().toString()));
            String cgstamt = String.format("%.2f",Float.parseFloat(et_cgstamt.getText().toString()));
            String sgstamt = String.format("%.2f",Float.parseFloat(et_sgstamt.getText().toString()));
            String cessamt = String.format("%.2f",Float.parseFloat(et_cessamt.getText().toString()));


                GSTR2_B2B_Amend ammend = new GSTR2_B2B_Amend();
                ammend.setSno(count);
                ammend.setTaxMonth(taxMonth);
                ammend.setHsn_ori(hsn_ori);
                ammend.setType_ori(type_ori);
                ammend.setPos_ori(pos1);
                ammend.setHsn_rev(hsn_rev);
                ammend.setType_rev(type_rev);
                ammend.setCustStateCode(custStateCode);

                ammend.setTaxableValue(Double.parseDouble(taxval));
                ammend.setIgstrate(Float.parseFloat(igstrate));
                ammend.setIgstamt(Float.parseFloat(igstamt));
                ammend.setCgstrate(Float.parseFloat(cgstrate));
                ammend.setCgstamt(Float.parseFloat(cgstamt));
                ammend.setSgstrate(Float.parseFloat(sgstrate));
                ammend.setSgstamt(Float.parseFloat(sgstamt));
                ammend.setCsamt(Float.parseFloat(cessamt));
                Date dd = new Date();
                String dd1 = new SimpleDateFormat("dd-MM-yyyy").format(dd);
                Date dd2 = new SimpleDateFormat("dd-MM-yyyy").parse(dd1);
                ammend.setInvoiceDate_ori(String.valueOf(dd2.getTime()));

                long lResult = dbAmmend_b2cs_GSTR1.add_GSTR1_B2CSAmmend(ammend);
                if(lResult>0)
                {
                    Log.d(TAG,"new ammend inserted successfully");
                    if (ammendList == null) {
                        ammendList = new ArrayList<GSTR2_B2B_Amend>();
                    }
                    ammendList.add(ammend);
                    if (ammendAdapter == null) {
                        ammendAdapter = new GSTR1_B2CS_AmendAdapter(getActivity(), ammendList, dbAmmend_b2cs_GSTR1);
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
        dbAmmend_b2cs_GSTR1.CloseDatabase();
        getActivity().finish();
    }
}
