package com.wepindia.pos.GST.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.wepindia.pos.GST.Adapter.GSTR2_B2B_AmendAdapter;
import com.wepindia.pos.GST.Ammend_b2b_GSTR2;
import com.wepindia.pos.GenericClasses.DateTime;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class Fragment_GSTR2_B2B_Amend extends Fragment {
    Context myContext;
    DatabaseHandler dbAmmend_b2b_GSTR2;
    MessageDialog MsgBox;// = new MessageDialog(HeaderFooterActivity.this);
    EditText et_gstin_ori, et_invno_ori, et_invdate_ori, et_gstin_rev, et_invno_rev, et_invdate_rev;
    EditText et_value,et_hsn,et_taxval,et_igstrate,et_sgstrate,et_cgstrate;
    EditText et_igstamt, et_cgstamt, et_sgstamt,et_cessamt;
    Spinner spnr_g_s;
    ImageButton btnCal_ori, btnCal_rev;
    Spinner spnr_pos,spnr_SupplierType;

    com.wep.common.app.views.WepButton btnAdd, btnSave, btnClear,btnClose,btnLoad;
    ListView listview_gstr2_amend;
    TableRow rowItems;
    String TAG = Fragment_GSTR2_B2B_Amend.class.getSimpleName();
    TextView tvTitleDate;// = (TextView) findViewById(R.id.tvTitleBarDate);
    ArrayList<GSTR2_B2B_Amend> ammendList;
    GSTR2_B2B_AmendAdapter ammendAdapter ;

    public Fragment_GSTR2_B2B_Amend() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            dbAmmend_b2b_GSTR2 = new DatabaseHandler(getActivity());

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment__gstr2__b2_b__amend, container, false);
        myContext = getActivity();
        MsgBox = new MessageDialog(myContext);
        try{
            dbAmmend_b2b_GSTR2.CloseDatabase();
            dbAmmend_b2b_GSTR2.CreateDatabase();
            dbAmmend_b2b_GSTR2.OpenDatabase();

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
        et_gstin_ori = (EditText) view.findViewById(R.id.et_gstin_ori);
        et_invno_ori = (EditText)view.findViewById(R.id.et_invno_ori);
        et_invdate_ori = (EditText) view.findViewById(R.id.et_invdate_ori);
        et_gstin_rev = (EditText) view.findViewById(R.id.et_gstin_rev);
        et_invno_rev = (EditText) view.findViewById(R.id.et_invno_rev);
        et_invdate_rev = (EditText) view.findViewById(R.id.et_invdate_rev);
        et_value = (EditText)view.findViewById(R.id.et_value);
        //et_pos = (EditText) view.findViewById(R.id.et_pos);
        et_taxval = (EditText) view.findViewById (R.id.et_taxval);
        et_igstrate = (EditText) view.findViewById (R.id.et_igstrate);
        et_cgstrate = (EditText) view.findViewById (R.id.et_cgstrate);
        et_sgstrate = (EditText) view.findViewById (R.id.et_sgstrate);
        et_igstamt = (EditText) view.findViewById (R.id.et_igstamt);
        et_cgstamt = (EditText) view.findViewById (R.id.et_cgstamt);
        et_sgstamt = (EditText) view.findViewById (R.id.et_sgstamt);
        et_cessamt = (EditText) view.findViewById (R.id.et_cessamt);
        et_hsn= (EditText)view.findViewById(R.id.et_hsn);
        btnCal_ori= (ImageButton)view.findViewById(R.id.btnCal_ori);
        btnCal_rev= (ImageButton)view.findViewById(R.id.btnCal_rev);

        spnr_g_s = (Spinner) view.findViewById(R.id.spnr_g_s);
        spnr_pos = (Spinner) view.findViewById(R.id.spnr_pos);
        spnr_SupplierType = (Spinner) view.findViewById(R.id.spnr_SupplierType);
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

        et_gstin_ori.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                et_gstin_rev.setText(s);
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
    {   String pos = "";
        String supplierType = "";
        try{
            supplierType = spnr_SupplierType.getSelectedItem().toString();
            if(supplierType.equals(""))
            {
                MsgBox.Show("Insufficient Information", "Please select supplier type");
                return;
            }

            if(ammendList == null)
                ammendList = new ArrayList<GSTR2_B2B_Amend>();
            String gstin = et_gstin_ori.getText().toString();
            String invoiceNo = et_invno_ori.getText().toString();
            String invoiceDate = et_invdate_ori.getText().toString();
            Date dd  = new SimpleDateFormat("dd-MM-yyyy").parse(invoiceDate);
            Cursor cursor = dbAmmend_b2b_GSTR2.getAmmends_GSTR2_b2b(gstin, invoiceNo, String.valueOf(dd.getTime()),supplierType);
            int count =1;
            while (cursor != null && cursor.moveToNext()) {
                GSTR2_B2B_Amend ammend = new GSTR2_B2B_Amend();
                ammend.setSno(count++);
                ammend.setGstin_ori(cursor.getString(cursor.getColumnIndex("GSTIN_Ori")));

                long invdate_ori = cursor.getLong(cursor.getColumnIndex("OriginalInvoiceDate"));
                Date date = new Date(invdate_ori);
                String dd1 = new SimpleDateFormat("dd-MM-yyyy").format(date);
                ammend.setInvoiceDate_ori(dd1);

                ammend.setInvoiceNo_ori(cursor.getString(cursor.getColumnIndex("OriginalInvoiceNo")));
                ammend.setGstin_rev(cursor.getString(cursor.getColumnIndex("GSTIN")));
                ammend.setInvoiceNo_rev(cursor.getString(cursor.getColumnIndex("InvoiceNo")));
                long invdate_rev = cursor.getLong(cursor.getColumnIndex("InvoiceDate"));
                 dd1 = new SimpleDateFormat("dd-MM-yyyy").format(invdate_rev);
                ammend.setInvoiceDate_rev(dd1);
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
                pos = ammend.getPOS();
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
            String gstin = et_gstin_ori.getText().toString();
            String invoiceNo = et_invno_ori.getText().toString();
            String invoiceDate = et_invdate_ori.getText().toString();
            ammendAdapter = new GSTR2_B2B_AmendAdapter(getActivity(),ammendList,dbAmmend_b2b_GSTR2,2,gstin, invoiceNo,invoiceDate,supplierType);
            listview_gstr2_amend.setAdapter(ammendAdapter);
        }
        else
        {
            ammendAdapter.notifyNewDataAdded(ammendList);
            spnr_pos.setSelection(getIndexPOS(pos));
        }
    }

    private int getIndexPOS(String pos)
    {
        List<String> posList = Arrays.asList(getResources().getStringArray(R.array.poscode));
        int count =0;
        for (String pos_temp : posList)
        {
            if(pos_temp.contains(pos))
                return count;
            count++;
        }
        return 0;
    }
    void Reset()
    {
        spnr_SupplierType.setSelection(0);
        et_gstin_ori.setText("");
        et_invno_ori.setText("");
        et_invdate_ori.setText("");
        et_gstin_rev.setText("");
        et_invno_rev.setText("");
        et_invdate_rev.setText("");
        et_value.setText("0.00");
        //et_pos.setText("14");
        spnr_pos.setSelection(0);
        spnr_g_s.setSelection(0);
        et_taxval.setText("");
        et_hsn.setText("");
        et_igstrate.setText("0");
        et_sgstrate.setText("0");
        et_cgstrate.setText("0");
        et_igstamt.setText("0");
        et_sgstamt.setText("0");
        et_cessamt.setText("0");
        et_cgstamt.setText("0");




        // btnSave.setEnabled(false);
        ammendAdapter = null;
        listview_gstr2_amend.setAdapter(null);
        if(ammendList!=null)
            ammendList.clear();

    }
    /*void Reset()
    {
        spnr_SupplierType.setSelection(0);
        et_gstin_ori.setText("12ANTPA0870E1A1");
        et_invno_ori.setText("23");
        et_invdate_ori.setText("01-04-2017");
        et_gstin_rev.setText("12ANTPA0870E1A1");
        et_invno_rev.setText("50");
        et_invdate_rev.setText("02-05-2017");
        et_value.setText("500");
        //et_pos.setText("14");
        spnr_pos.setSelection(0);
        spnr_g_s.setSelection(0);
        et_taxval.setText("1000");
        et_hsn.setText("h5");
        et_igstrate.setText("0");
        et_sgstrate.setText("0");
        et_cgstrate.setText("0");
        et_igstamt.setText("0");
        et_sgstamt.setText("0");
        et_cgstamt.setText("0");




        // btnSave.setEnabled(false);
        ammendAdapter = null;
        listview_gstr2_amend.setAdapter(null);
        if(ammendList!=null)
            ammendList.clear();

    }*/


    public void Add(View v)
    {try{
        String supplierType = spnr_SupplierType.getSelectedItem().toString();
        if(supplierType.equals(""))
        {
            MsgBox.Show("Insufficient Information", "Please select supplier type");
            return;
        }
        String gstin_ori = et_gstin_ori.getText().toString();
        String gstin_rev = et_gstin_rev.getText().toString();
        String invno_ori = et_invno_ori.getText().toString();
        String invno_rev = et_invno_rev.getText().toString();
        String invdate_ori = et_invdate_ori.getText().toString();
        String invdate_rev = et_invdate_rev.getText().toString();
        String hsn = et_hsn.getText().toString();
        //String pos1 = et_pos.getText().toString();
        String str = spnr_pos.getSelectedItem().toString().trim();
        String pos1 = "";
        if (!str.equals(""))
        {
            int length = str.length();
            pos1 = str.substring(length - 2, length);
        }

        String supply = spnr_g_s.getSelectedItem().toString();

        String value = String.format("%.2f",Float.parseFloat(et_value.getText().toString()));
        String taxval = String.format("%.2f",Float.parseFloat(et_taxval.getText().toString()));
        String igstrate = String.format("%.2f",Float.parseFloat(et_igstrate.getText().toString()));
        String cgstrate = String.format("%.2f",Float.parseFloat(et_cgstrate.getText().toString()));
        String sgstrate = String.format("%.2f",Float.parseFloat(et_sgstrate.getText().toString()));
        String igstamt = String.format("%.2f",Float.parseFloat(et_igstamt.getText().toString()));
        String cgstamt = String.format("%.2f",Float.parseFloat(et_cgstamt.getText().toString()));
        String sgstamt = String.format("%.2f",Float.parseFloat(et_sgstamt.getText().toString()));
        String cessamt = String.format("%.2f",Float.parseFloat(et_cessamt.getText().toString()));

        int count =listview_gstr2_amend.getCount()+1;



        if (gstin_ori.equals("") || (invdate_ori.equals("")) || (invno_ori.equals(""))||(gstin_rev.equals(""))||
                (invdate_rev.equals("")) || (invno_rev.equals(""))||
                (value.equals("")) || (taxval.equals(""))||
                (igstrate.equals(""))|| (sgstrate.equals(""))||(cgstrate.equals("")) ||
                (igstamt.equals(""))|| (sgstamt.equals(""))||(cgstamt.equals(""))||(cessamt.equals("")))
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
                ammend.setCsamt(Float.parseFloat(cessamt));
                ammend.setSupplierType(supplierType);
                long lResult = dbAmmend_b2b_GSTR2.add_GSTR2_B2BAmmend(ammend);
                if(lResult>0)
                {
                    Log.d(TAG,"new ammend inserted successfully");
                    if (ammendList == null) {
                        ammendList = new ArrayList<GSTR2_B2B_Amend>();
                    }
                    ammendList.add(ammend);
                    if (ammendAdapter == null) {
                        String gstin = et_gstin_ori.getText().toString();
                        String invoiceNo = et_invno_ori.getText().toString();
                        String invoiceDate = et_invdate_ori.getText().toString();
                        ammendAdapter = new GSTR2_B2B_AmendAdapter(getActivity(),ammendList,dbAmmend_b2b_GSTR2,2,gstin, invoiceNo,invoiceDate,supplierType);
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
        dbAmmend_b2b_GSTR2.CloseDatabase();
        getActivity().finish();
    }
}
