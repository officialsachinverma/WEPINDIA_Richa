package com.wepindia.pos.GST;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wepindia.pos.R;

import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by welcome on 02-11-2016.
 */

public class GSTR2_tables_Activity  extends Activity implements DatePickerDialog.OnDateSetListener{

    Context myContext;
    DatabaseHandler dbGSTR2;
    //Message dialog object
    public AlertDialog.Builder MsgBox;

    // Variables
    TextView textview_month, textview_year;
    TableLayout tb_inward_taxed ,tb_inward_taxed_amend, tb_inward_untaxed;
    String day = "", month = "", year = "";
    int day_int, month_int, year_int;
    String month_array[] = {"January", "February", "March", "April","May","June","July","August","September","October","November","December"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove default title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gstr2_tables);

        dbGSTR2 = new DatabaseHandler(GSTR2_tables_Activity.this);
        myContext = this;

        try {

            day = getIntent().getStringExtra("Date");
            month = getIntent().getStringExtra("Month");
            year = getIntent().getStringExtra("Year");
            tb_inward_taxed = (TableLayout) findViewById(R.id.tbl_inward_taxed);
            tb_inward_taxed_amend = (TableLayout) findViewById(R.id.tbl_inward_taxed_amend);
            tb_inward_untaxed = (TableLayout) findViewById(R.id.tbl_inward_untaxed);
            textview_month = (TextView) findViewById(R.id.text_MonthValue);
            textview_year = (TextView) findViewById(R.id.text_YearValue);

            //clearTables();
            if (month.equals("") || year.equals(""))
            {
                MsgBox = new AlertDialog.Builder(myContext);
                MsgBox. setMessage("Please select date/month")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
            else
            {
                textview_month.setText(month);
                textview_year.setText(year);
                loadTable_inward_taxed();
                loadTable_inward_taxed_amend();
            }

            dbGSTR2.CreateDatabase();


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void loadTable_inward_taxed()
    {
        try
        {
            dbGSTR2.OpenDatabase();
            Cursor cursor = dbGSTR2.getInward_taxed("","");
            if (cursor == null )
            {
                MsgBox = new AlertDialog.Builder(myContext);
                MsgBox. setMessage("No data for this month b2b")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
            else {
                if (cursor.moveToFirst()) {

                    TextView GSTIN, InvoiceNo, InvoiceDate, SupplyType,HSNCode, Value,TaxableValue;
                    TextView CGSTRate, CGSTAmount, SGSTRate, SGSTAmount,IGSTRate, IGSTAmount, Pos,Eligible,Total_IGST, Tital_CGST, Total_SGST;
                    TextView Total_ITC_CGST, Total_ITC_IGST, Total_ITC_SGST;

                    int count =1001;
                    TableRow rowcursor;

                    do {
                        rowcursor = new TableRow(myContext);
                        rowcursor.setLayoutParams(new TableRow.LayoutParams
                                (TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                        TextView SNo = new TextView(myContext);
                        SNo.setLeft(20);
                        SNo.setText(String.valueOf(count));
                        SNo.setWidth(52);
                        SNo.setTextSize(12);
                        SNo.setBackgroundResource(R.drawable.border);
                        count++;

                        GSTIN = new TextView(myContext);
                        GSTIN.setLeft(10);
                        String gstin = null;
                        gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
                        if (gstin == null)
                            gstin = cursor.getString(cursor.getColumnIndex("CustName"));
                        GSTIN.setText(gstin);
                        GSTIN.setWidth(100);
                        GSTIN.setTextSize(12);
                        GSTIN.setBackgroundResource(R.drawable.border);


                        InvoiceNo = new TextView(myContext);
                        InvoiceNo.setLeft(10);
                        InvoiceNo.setWidth(70);
                        InvoiceNo.setTextSize(12);
                        //Richa to do
                        String no = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                        no.trim();
                        InvoiceNo.setText(no);
                        InvoiceNo.setBackgroundResource(R.drawable.border);

                        InvoiceDate = new TextView(myContext);
                        InvoiceDate.setLeft(10);
                        InvoiceDate.setWidth(60);
                        //InvoiceDate = (TextView)findViewById(R.id.b2b_InvDate);
                        String date_temp = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                        date_temp.trim();
                        String substr=date_temp.substring(0,2);
                        substr+= month.substring(0,3);;
                        InvoiceDate.setText(substr);
                        InvoiceDate.setTextSize(12);
                        InvoiceDate.setBackgroundResource(R.drawable.border);

                        SupplyType = new TextView(myContext);
                        //SupplyType.setText(cursor.getString(cursor.getColumnIndex("SupplyType")));
                        SupplyType.setLeft(10);
                        SupplyType.setWidth(25);
                        SupplyType.setTextSize(12);
                        SupplyType.setBackgroundResource(R.drawable.border);


                        HSNCode = new TextView(myContext);
                        HSNCode.setLeft(10);
                        HSNCode.setWidth(70);
                        HSNCode.setTextSize(12);
                        HSNCode.setBackgroundResource(R.drawable.border);

                        Value = new TextView(myContext);
                        Value.setText(cursor.getString(cursor.getColumnIndex("Value")));
                        Value.setLeft(10);
                        Value.setWidth(70);
                        Value.setTextSize(12);
                        Value.setBackgroundResource(R.drawable.border);

                        TaxableValue = new TextView(myContext);
                        TaxableValue.setText(cursor.getString(cursor.getColumnIndex("TaxableValue")));
                        TaxableValue.setLeft(10);
                        TaxableValue.setWidth(70);
                        TaxableValue.setTextSize(12);
                        TaxableValue.setBackgroundResource(R.drawable.border);

                        CGSTRate = new TextView(myContext);
                        CGSTRate.setText(cursor.getString(cursor.getColumnIndex("CGSTRate")));
                        CGSTRate.setLeft(10);
                        CGSTRate.setWidth(50);
                        CGSTRate.setTextSize(12);
                        CGSTRate.setBackgroundResource(R.drawable.border);


                        CGSTAmount = new TextView(myContext);
                        CGSTAmount.setText(cursor.getString(cursor.getColumnIndex("CGSTAmount")));
                        CGSTAmount.setLeft(10);
                        CGSTAmount.setWidth(50);
                        CGSTAmount.setTextSize(12);
                        CGSTAmount.setBackgroundResource(R.drawable.border);


                        SGSTRate = new TextView(myContext);
                        SGSTRate.setText(cursor.getString(cursor.getColumnIndex("SGSTRate")));
                        SGSTRate.setLeft(10);
                        SGSTRate.setWidth(50);
                        SGSTRate.setTextSize(12);
                        SGSTRate.setBackgroundResource(R.drawable.border);

                        SGSTAmount = new TextView(myContext);
                        SGSTAmount.setText(cursor.getString(cursor.getColumnIndex("SGSTAmount")));
                        SGSTAmount.setLeft(10);
                        SGSTAmount.setWidth(50);
                        SGSTAmount.setTextSize(12);
                        SGSTAmount.setBackgroundResource(R.drawable.border);

                        IGSTRate = new TextView(myContext);
                        IGSTRate.setText(cursor.getString(cursor.getColumnIndex("IGSTRate")));
                        IGSTRate.setLeft(10);
                        IGSTRate.setWidth(50);
                        IGSTRate.setTextSize(12);
                        IGSTRate.setBackgroundResource(R.drawable.border);

                        IGSTAmount = new TextView(myContext);
                        IGSTAmount.setText(cursor.getString(cursor.getColumnIndex("IGSTAmount")));
                        IGSTAmount.setLeft(10);
                        IGSTAmount.setWidth(50);
                        IGSTAmount.setTextSize(12);
                        IGSTAmount.setBackgroundResource(R.drawable.border);

                        Pos  = new TextView(myContext);
                        Pos.setText(cursor.getString(cursor.getColumnIndex("POS")));
                        Pos.setLeft(10);
                        Pos.setWidth(60);
                        Pos.setTextSize(12);
                        Pos.setBackgroundResource(R.drawable.border);

                        Eligible = new TextView(myContext);
                       // Eligible.setText(cursor.getString(cursor.getColumnIndex("ITC_Eligible")));
                        Eligible.setLeft(10);
                        Eligible.setWidth(60);
                        Eligible.setTextSize(12);
                        Eligible.setBackgroundResource(R.drawable.border);


                        Total_ITC_IGST = new TextView(myContext);
                        Total_ITC_IGST.setText(cursor.getString(cursor.getColumnIndex("Total_ITC_IGST")));
                        Total_ITC_IGST.setLeft(10);
                        Total_ITC_IGST.setWidth(50);
                        Total_ITC_IGST.setTextSize(12);
                        Total_ITC_IGST.setBackgroundResource(R.drawable.border);

                        Total_ITC_CGST = new TextView(myContext);
                        Total_ITC_CGST.setText(cursor.getString(cursor.getColumnIndex("Total_ITC_CGST")));
                        Total_ITC_CGST.setLeft(10);
                        Total_ITC_CGST.setWidth(50);
                        Total_ITC_CGST.setTextSize(12);
                        Total_ITC_CGST.setBackgroundResource(R.drawable.border);

                        Total_ITC_SGST = new TextView(myContext);
                        Total_ITC_SGST.setText(cursor.getString(cursor.getColumnIndex("Total_ITC_SGST")));
                        Total_ITC_SGST.setLeft(10);
                        Total_ITC_SGST.setWidth(50);
                        Total_ITC_SGST.setTextSize(12);
                        Total_ITC_SGST.setBackgroundResource(R.drawable.border);

                        TextView monthly  = new TextView(myContext);
                        // monthly.setText(cursor.getString(cursor.getColumnIndex("Total_ITC_SGST")));
                        monthly.setLeft(10);
                        monthly.setWidth(50);
                        monthly.setTextSize(12);
                        monthly.setBackgroundResource(R.drawable.border);

                        TextView monthly1  = new TextView(myContext);
                        // monthly1.setText(cursor.getString(cursor.getColumnIndex("Total_ITC_SGST")));
                        monthly1.setLeft(10);
                        monthly1.setWidth(50);
                        monthly1.setTextSize(12);
                        monthly1.setBackgroundResource(R.drawable.border);

                        TextView monthly2  = new TextView(myContext);
                        // monthly2.setText(cursor.getString(cursor.getColumnIndex("Total_ITC_SGST")));
                        monthly2.setLeft(10);
                        monthly2.setWidth(50);
                        monthly2.setTextSize(12);
                        monthly2.setBackgroundResource(R.drawable.border);

                        String suppliertype = cursor.getString(cursor.getColumnIndex("SupplierType"));
                        String revercharge = cursor.getString(cursor.getColumnIndex("AttractsReverseCharge"));
                        if (revercharge == null)
                        {
                            revercharge="no";
                        }

                        String taxationtype = cursor.getString(cursor.getColumnIndex("TaxationType"));
                        if ((suppliertype.equalsIgnoreCase("registered") && taxationtype.equalsIgnoreCase("GST")) ||
                                ((suppliertype.equalsIgnoreCase("unregistered"))&&(revercharge.equalsIgnoreCase("yes"))))

                        {
                            rowcursor.addView(SNo);
                            rowcursor.addView(GSTIN);
                            rowcursor.addView(InvoiceNo);
                            rowcursor.addView(InvoiceDate);
                            rowcursor.addView(Value);
                            rowcursor.addView(SupplyType);
                            rowcursor.addView(HSNCode);
                            rowcursor.addView(TaxableValue);
                            rowcursor.addView(IGSTRate);
                            rowcursor.addView(IGSTAmount);
                            rowcursor.addView(CGSTRate);
                            rowcursor.addView(CGSTAmount);
                            rowcursor.addView(SGSTRate);
                            rowcursor.addView(SGSTAmount);
                            rowcursor.addView(Pos);
                            rowcursor.addView(Eligible);
                            rowcursor.addView(Total_ITC_IGST);
                            rowcursor.addView(Total_ITC_CGST);
                            rowcursor.addView(Total_ITC_SGST);
                            rowcursor.addView(monthly);
                            rowcursor.addView(monthly1);
                            rowcursor.addView(monthly2);

                            tb_inward_taxed.addView(rowcursor, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                            //add a new line to the TableLayout:
                            final View vline = new View(this);
                            vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 2));
                            //vline.setBackgroundColor(Color.BLUE);
                            tb_inward_taxed.addView(vline);
                            add_gstr2_items(no, date_temp);
                        }
                        else
                        {
                            TextView Description = new TextView(myContext);
                            String igstrate = IGSTRate.getText().toString();
                            if (igstrate.equals(""))
                            {
                                Description.setText("Intrastate Supplies");
                            }
                            else
                            {
                                Description.setText("InterState Supplies");
                            }
                            Description.setLeft(10);
                            Description.setWidth(150);
                            Description.setTextSize(12);
                            Description.setBackgroundResource(R.drawable.border);

                            HSNCode.setWidth(100);

                            TextView Compounding  = new TextView(myContext);
                            if (suppliertype.equalsIgnoreCase("Compounding"))
                            Compounding.setText(cursor.getString(cursor.getColumnIndex("TaxableValue")));
                            Compounding.setLeft(10);
                            Compounding.setWidth(100);
                            Compounding.setTextSize(12);
                            Compounding.setBackgroundResource(R.drawable.border);

                            TextView Unregistered  = new TextView(myContext);
                            if (suppliertype.equalsIgnoreCase("Unregistered"))
                                Unregistered.setText(cursor.getString(cursor.getColumnIndex("TaxableValue")));
                            Unregistered.setLeft(10);
                            Unregistered.setWidth(100);
                            Unregistered.setTextSize(12);
                            Unregistered.setBackgroundResource(R.drawable.border);

                            TextView Exempt  = new TextView(myContext);
                            if(taxationtype.equalsIgnoreCase("Exempt"))
                                Exempt.setText(cursor.getString(cursor.getColumnIndex("TaxableValue")));
                            Exempt.setLeft(10);
                            Exempt.setWidth(100);
                            Exempt.setTextSize(12);
                            Exempt.setBackgroundResource(R.drawable.border);

                            TextView Nil  = new TextView(myContext);
                            if(taxationtype.equalsIgnoreCase("Nil"))
                                Nil.setText(cursor.getString(cursor.getColumnIndex("TaxableValue")));
                            Nil.setLeft(10);
                            Nil.setWidth(100);
                            Nil.setTextSize(12);
                            Nil.setBackgroundResource(R.drawable.border);

                            TextView NonGst  = new TextView(myContext);
                            if(taxationtype.equalsIgnoreCase("NonGst"))
                                NonGst.setText(cursor.getString(cursor.getColumnIndex("TaxableValue")));
                            NonGst.setLeft(10);
                            NonGst.setWidth(100);
                            NonGst.setTextSize(12);
                            NonGst.setBackgroundResource(R.drawable.border);

                            rowcursor.addView(SNo);
                            rowcursor.addView(Description);
                            rowcursor.addView(HSNCode);
                            rowcursor.addView(Compounding);
                            rowcursor.addView(Unregistered);
                            rowcursor.addView(Exempt);
                            rowcursor.addView(Nil);
                            rowcursor.addView(NonGst);

                            tb_inward_untaxed.addView(rowcursor, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                            //add a new line to the TableLayout:
                            final View vline = new View(this);
                            vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 2));
                            //vline.setBackgroundColor(Color.BLUE);
                            tb_inward_untaxed.addView(vline);
                            //add_gstr2_items(no, date_temp);
                        }


                    } while (cursor.moveToNext());
                }
            }
        }// end try
        catch(Exception e)
        {
            MsgBox = new AlertDialog.Builder(myContext);
            MsgBox. setMessage(e.getMessage())
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
        finally {
            dbGSTR2.CloseDatabase();
        }

    }

    void add_gstr2_items(String No, String Date)
    {

        Cursor cursor = dbGSTR2.getitems_inward_taxed(No, Date,"","","");
        if (cursor == null)
        {
            MsgBox = new AlertDialog.Builder(myContext);
            MsgBox. setMessage("No items for Invoice No : "+No+" & Invoice Date : "+Date)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
        else
        {

            try{

                if (cursor.moveToFirst()) {

                    TextView GSTIN, InvoiceNo, InvoiceDate, SupplyType, HSNCode, Value,TaxableValue;
                    TextView CGSTRate, CGSTAmount, SGSTRate, SGSTAmount,IGSTRate, IGSTAmount,Pos, Eligible;
                    TextView Total_ITC_IGST,Total_ITC_CGST,Total_ITC_SGST;

                    int count =1;
                    TableRow rowcursor;

                    do {
                        rowcursor = new TableRow(myContext);
                        rowcursor.setLayoutParams(new TableRow.LayoutParams
                                (TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                        TextView SNo = new TextView(myContext);
                        SNo.setLeft(20);
                        SNo.setText(String.valueOf(count));
                        SNo.setWidth(52);
                        SNo.setTextSize(12);
                        SNo.setBackgroundResource(R.drawable.border_item);
                        count++;

                        GSTIN = new TextView(myContext);
                        GSTIN.setLeft(10);
                        // GSTIN.setText("GSTIN12345678AB");
                        GSTIN.setWidth(100);
                        GSTIN.setTextSize(12);
                        GSTIN.setBackgroundResource(R.drawable.border_item);


                        InvoiceNo = new TextView(myContext);
                        InvoiceNo.setLeft(10);
                        InvoiceNo.setWidth(70);
                        InvoiceNo.setTextSize(12);
                        //Richa to do
                        //String no = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                        //no.trim();
                        //InvoiceNo.setText(no);
                        InvoiceNo.setBackgroundResource(R.drawable.border_item);

                        InvoiceDate = new TextView(myContext);
                        InvoiceDate.setLeft(10);
                        InvoiceDate.setWidth(60);
                        String date_temp = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                        date_temp.trim();
                        String substr=date_temp.substring(0,2);
                        substr+= month.substring(0,3);;
                        //InvoiceDate.setText(substr);
                        InvoiceDate.setTextSize(12);
                        InvoiceDate.setBackgroundResource(R.drawable.border_item);

                        SupplyType = new TextView(myContext);
                        SupplyType.setText(cursor.getString(cursor.getColumnIndex("SupplyType")));
                        SupplyType.setLeft(10);
                        SupplyType.setWidth(25);
                        SupplyType.setTextSize(12);
                        SupplyType.setBackgroundResource(R.drawable.border_item);

                        HSNCode = new TextView(myContext);
                        //HSNCode.setText(cursor.getString(cursor.getColumnIndex("HSNCode")));
                        String HSN = cursor.getString(cursor.getColumnIndex("HSNCode"));
                        String desc = cursor.getString(cursor.getColumnIndex("ItemName"));
                        HSN = HSN + "-" + desc;
                        HSNCode.setText(HSN);
                        HSNCode.setLeft(10);
                        HSNCode.setWidth(70);
                        HSNCode.setTextSize(12);
                        HSNCode.setBackgroundResource(R.drawable.border_item);

                        Value = new TextView(myContext);
                        Value.setText(cursor.getString(cursor.getColumnIndex("Value")));
                        Value.setLeft(10);
                        Value.setWidth(70);
                        Value.setTextSize(12);
                        Value.setBackgroundResource(R.drawable.border_item);

                        TaxableValue = new TextView(myContext);
                        TaxableValue.setText(cursor.getString(cursor.getColumnIndex("TaxableValue")));
                        TaxableValue.setLeft(10);
                        TaxableValue.setWidth(70);
                        TaxableValue.setTextSize(12);
                        TaxableValue.setBackgroundResource(R.drawable.border_item);

                        CGSTRate = new TextView(myContext);
                        CGSTRate.setText(cursor.getString(cursor.getColumnIndex("CGSTRate")));
                        CGSTRate.setLeft(10);
                        CGSTRate.setWidth(50);
                        CGSTRate.setTextSize(12);
                        CGSTRate.setBackgroundResource(R.drawable.border_item);


                        CGSTAmount = new TextView(myContext);
                        CGSTAmount.setText(cursor.getString(cursor.getColumnIndex("CGSTAmount")));
                        CGSTAmount.setLeft(10);
                        CGSTAmount.setWidth(50);
                        CGSTAmount.setTextSize(12);
                        CGSTAmount.setBackgroundResource(R.drawable.border_item);


                        SGSTRate = new TextView(myContext);
                        SGSTRate.setText(cursor.getString(cursor.getColumnIndex("SGSTRate")));
                        SGSTRate.setLeft(10);
                        SGSTRate.setWidth(50);
                        SGSTRate.setTextSize(12);
                        SGSTRate.setBackgroundResource(R.drawable.border_item);

                        SGSTAmount = new TextView(myContext);
                        SGSTAmount.setText(cursor.getString(cursor.getColumnIndex("SGSTAmount")));
                        SGSTAmount.setLeft(10);
                        SGSTAmount.setWidth(50);
                        SGSTAmount.setTextSize(12);
                        SGSTAmount.setBackgroundResource(R.drawable.border_item);

                        IGSTRate = new TextView(myContext);
                        IGSTRate.setText(cursor.getString(cursor.getColumnIndex("IGSTRate")));
                        IGSTRate.setLeft(10);
                        IGSTRate.setWidth(50);
                        IGSTRate.setTextSize(12);
                        IGSTRate.setBackgroundResource(R.drawable.border_item);

                        IGSTAmount = new TextView(myContext);
                        IGSTAmount.setText(cursor.getString(cursor.getColumnIndex("IGSTAmount")));
                        IGSTAmount.setLeft(10);
                        IGSTAmount.setWidth(50);
                        IGSTAmount.setTextSize(12);
                        IGSTAmount.setBackgroundResource(R.drawable.border_item);

                        Pos  = new TextView(myContext);
                        //Pos.setText(cursor.getString(cursor.getColumnIndex("POS")));
                        Pos.setLeft(10);
                        Pos.setWidth(60);
                        Pos.setTextSize(12);
                        Pos.setBackgroundResource(R.drawable.border_item);

                        Eligible  = new TextView(myContext);
                        Eligible.setText(cursor.getString(cursor.getColumnIndex("ITC_Eligible")));
                        Eligible.setLeft(10);
                        Eligible.setWidth(60);
                        Eligible.setTextSize(12);
                        Eligible.setBackgroundResource(R.drawable.border_item);

                        Total_ITC_IGST  = new TextView(myContext);
                        Total_ITC_IGST.setText(cursor.getString(cursor.getColumnIndex("Total_ITC_IGST")));
                        Total_ITC_IGST.setLeft(10);
                        Total_ITC_IGST.setWidth(50);
                        Total_ITC_IGST.setTextSize(12);
                        Total_ITC_IGST.setBackgroundResource(R.drawable.border_item);

                        Total_ITC_CGST  = new TextView(myContext);
                        Total_ITC_CGST.setText(cursor.getString(cursor.getColumnIndex("Total_ITC_CGST")));
                        Total_ITC_CGST.setLeft(10);
                        Total_ITC_CGST.setWidth(50);
                        Total_ITC_CGST.setTextSize(12);
                        Total_ITC_CGST.setBackgroundResource(R.drawable.border_item);

                        Total_ITC_SGST  = new TextView(myContext);
                        Total_ITC_SGST.setText(cursor.getString(cursor.getColumnIndex("Total_ITC_SGST")));
                        Total_ITC_SGST.setLeft(10);
                        Total_ITC_SGST.setWidth(50);
                        Total_ITC_SGST.setTextSize(12);
                        Total_ITC_SGST.setBackgroundResource(R.drawable.border_item);

                        TextView monthly  = new TextView(myContext);
                       // monthly.setText(cursor.getString(cursor.getColumnIndex("Total_ITC_SGST")));
                        monthly.setLeft(10);
                        monthly.setWidth(50);
                        monthly.setTextSize(12);
                        monthly.setBackgroundResource(R.drawable.border_item);

                        TextView monthly1  = new TextView(myContext);
                        // monthly1.setText(cursor.getString(cursor.getColumnIndex("Total_ITC_SGST")));
                        monthly1.setLeft(10);
                        monthly1.setWidth(50);
                        monthly1.setTextSize(12);
                        monthly1.setBackgroundResource(R.drawable.border_item);

                        TextView monthly2  = new TextView(myContext);
                        // monthly2.setText(cursor.getString(cursor.getColumnIndex("Total_ITC_SGST")));
                        monthly2.setLeft(10);
                        monthly2.setWidth(50);
                        monthly2.setTextSize(12);
                        monthly2.setBackgroundResource(R.drawable.border_item);

                        rowcursor.addView(SNo);
                        rowcursor.addView(GSTIN);
                        rowcursor.addView(InvoiceNo);
                        rowcursor.addView(InvoiceDate);
                        rowcursor.addView(Value);
                        rowcursor.addView(SupplyType);
                        rowcursor.addView(HSNCode);
                        rowcursor.addView(TaxableValue);
                        rowcursor.addView(IGSTRate);
                        rowcursor.addView(IGSTAmount);
                        rowcursor.addView(CGSTRate);
                        rowcursor.addView(CGSTAmount);
                        rowcursor.addView(SGSTRate);
                        rowcursor.addView(SGSTAmount);
                        rowcursor.addView(Pos);
                        rowcursor.addView(Eligible);
                        rowcursor.addView(Total_ITC_IGST);
                        rowcursor.addView(Total_ITC_CGST);
                        rowcursor.addView(Total_ITC_SGST);
                        rowcursor.addView(monthly);
                        rowcursor.addView(monthly1);
                        rowcursor.addView(monthly2);

                        tb_inward_taxed.addView(rowcursor, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                        //add a new line to the TableLayout:
                        final View vline = new View(this);
                        vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,2));
                        tb_inward_taxed.addView(vline);


                    } while (cursor.moveToNext());

                }

            }// end try
            catch (Exception e)
            {
                MsgBox = new AlertDialog.Builder(myContext);
                MsgBox .setTitle("Error while fetching items details")
                        .setMessage(e.getMessage())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }

        } // end else
    }

    public void clearTables()
    {
        // clear all data from screen
        tb_inward_taxed.removeAllViews();
        tb_inward_taxed_amend.removeAllViews();
        tb_inward_untaxed.removeAllViews();
    }

    public void loadTable_inward_taxed_amend()
    {
        try
        {
            dbGSTR2.OpenDatabase();
            Cursor cursor = dbGSTR2.getInward_taxed_amend();
            if (cursor == null )
            {
                MsgBox = new AlertDialog.Builder(myContext);
                MsgBox. setMessage("No data for this month b2ba")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
            else {
                if (cursor.moveToFirst()) {

                    TextView GSTIN, InvoiceNo, InvoiceDate, SupplyType, TaxationType, HSNCode, ItemName, Price, Quantity,Units,Value,DiscountRate,TaxableValue;
                    TextView CGSTRate, CGSTAmount, SGSTRate, SGSTAmount,IGSTRate, IGSTAmount,AdditionalChargeName,AdditionalChargeAmount;

                    int count =1001;
                    TableRow rowcursor;

                    do {
                        rowcursor = new TableRow(myContext);
                        rowcursor.setLayoutParams(new TableRow.LayoutParams
                                (TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                        TextView SNo = new TextView(myContext);
                        SNo.setLeft(10);
                        SNo.setText(String.valueOf(count));
                        SNo.setWidth(40);
                        SNo.setTextSize(12);
                        SNo.setBackgroundResource(R.drawable.border);
                        count++;

                        TextView OriNo = new TextView(myContext);
                        OriNo.setLeft(10);
                        OriNo.setText(cursor.getString(cursor.getColumnIndex("OriginalInvoiceNo")));
                        OriNo.setWidth(55);
                        OriNo.setTextSize(12);
                        OriNo.setBackgroundResource(R.drawable.border);

                        TextView OriDate = new TextView(myContext);
                        OriDate.setLeft(10);
                        //OriDate.setText(cursor.getString(cursor.getColumnIndex("OriginalInvoiceDate")));
                        String date_temp = cursor.getString(cursor.getColumnIndex("OriginalInvoiceDate"));
                        date_temp.trim();
                        String substr=date_temp.substring(0,2);
                        String month_temp = date_temp.substring(3,5);
                        String month_t1 = month_array[Integer.valueOf(month_temp) -1];
                        substr+= month_t1.substring(0,3);

                        OriDate.setText(substr);
                        OriDate.setWidth(50);
                        OriDate.setTextSize(12);
                        OriDate.setBackgroundResource(R.drawable.border);


                        GSTIN = new TextView(myContext);
                        GSTIN.setLeft(10);
                        String gstn = null;
                        gstn = cursor.getString(cursor.getColumnIndex("GSTIN"));
                        if (gstn == null)
                            gstn = cursor.getString(cursor.getColumnIndex("CustName"));
                        GSTIN.setText(gstn);
                        GSTIN.setWidth(100);
                        GSTIN.setTextSize(12);
                        GSTIN.setBackgroundResource(R.drawable.border);

                        InvoiceNo = new TextView(myContext);
                        //InvoiceNo = (TextView)findViewById(R.id.b2b_InvNo);
                        InvoiceNo.setLeft(10);
                        InvoiceNo.setWidth(70);
                        InvoiceNo.setTextSize(12);
                        //Richa to do
                        String no = cursor.getString(cursor.getColumnIndex("RevisedInvoiceNo"));
                        no.trim();
                        InvoiceNo.setText(no);
                        InvoiceNo.setBackgroundResource(R.drawable.border);

                        InvoiceDate = new TextView(myContext);
                        InvoiceDate.setLeft(10);
                        InvoiceDate.setWidth(60);
                        //InvoiceDate = (TextView)findViewById(R.id.b2b_InvDate);
                        String date_temp1 = cursor.getString(cursor.getColumnIndex("RevisedInvoiceDate"));
                        date_temp1.trim();
                        String substr1=date_temp1.substring(0,2);
                        String month_temp1 = date_temp.substring(3,5);
                        String month_t11 = month_array[Integer.valueOf(month_temp) -1];
                        substr1+= month_t11.substring(0,3);
                        InvoiceDate.setText(substr1);
                        InvoiceDate.setTextSize(12);
                        InvoiceDate.setBackgroundResource(R.drawable.border);

                        SupplyType = new TextView(myContext);
                        SupplyType.setText(cursor.getString(cursor.getColumnIndex("SupplyType")));
                        SupplyType.setLeft(10);
                        SupplyType.setWidth(25);
                        SupplyType.setTextSize(12);
                        SupplyType.setBackgroundResource(R.drawable.border);


                        //TaxationType = new TextView(myContext);
                        //TaxationType.setText(cursor.getString(cursor.getColumnIndex("TaxationType")));

                        HSNCode = new TextView(myContext);
                        //HSNCode.setText(cursor.getString(cursor.getColumnIndex("HSNCode")));
                        String HSN = cursor.getString(cursor.getColumnIndex("HSNCode"));
                        String Desc = cursor.getString(cursor.getColumnIndex("ItemName"));
                        HSN = HSN + " - " + Desc;
                        HSNCode.setText(HSN);
                        HSNCode.setLeft(10);
                        HSNCode.setWidth(70);
                        HSNCode.setTextSize(12);
                        HSNCode.setBackgroundResource(R.drawable.border);


                        Value = new TextView(myContext);
                        Value.setText(cursor.getString(cursor.getColumnIndex("Value")));
                        Value.setLeft(10);
                        Value.setWidth(50);
                        Value.setTextSize(12);
                        Value.setBackgroundResource(R.drawable.border);

                        TaxableValue = new TextView(myContext);
                        TaxableValue.setText(cursor.getString(cursor.getColumnIndex("TaxableValue")));
                        TaxableValue.setLeft(10);
                        TaxableValue.setWidth(50);
                        TaxableValue.setTextSize(12);
                        TaxableValue.setBackgroundResource(R.drawable.border);

                        CGSTRate = new TextView(myContext);
                        CGSTRate.setText(cursor.getString(cursor.getColumnIndex("CGSTRate")));
                        CGSTRate.setLeft(10);
                        CGSTRate.setWidth(50);
                        CGSTRate.setTextSize(12);
                        CGSTRate.setBackgroundResource(R.drawable.border);


                        CGSTAmount = new TextView(myContext);
                        CGSTAmount.setText(cursor.getString(cursor.getColumnIndex("CGSTAmount")));
                        CGSTAmount.setLeft(10);
                        CGSTAmount.setWidth(50);
                        CGSTAmount.setTextSize(12);
                        CGSTAmount.setBackgroundResource(R.drawable.border);


                        SGSTRate = new TextView(myContext);
                        SGSTRate.setText(cursor.getString(cursor.getColumnIndex("SGSTRate")));
                        SGSTRate.setLeft(10);
                        SGSTRate.setWidth(50);
                        SGSTRate.setTextSize(12);
                        SGSTRate.setBackgroundResource(R.drawable.border);

                        SGSTAmount = new TextView(myContext);
                        SGSTAmount.setText(cursor.getString(cursor.getColumnIndex("SGSTAmount")));
                        SGSTAmount.setLeft(10);
                        SGSTAmount.setWidth(50);
                        SGSTAmount.setTextSize(12);
                        SGSTAmount.setBackgroundResource(R.drawable.border);

                        IGSTRate = new TextView(myContext);
                        IGSTRate.setText(cursor.getString(cursor.getColumnIndex("IGSTRate")));
                        IGSTRate.setLeft(10);
                        IGSTRate.setWidth(50);
                        IGSTRate.setTextSize(12);
                        IGSTRate.setBackgroundResource(R.drawable.border);

                        IGSTAmount = new TextView(myContext);
                        IGSTAmount.setText(cursor.getString(cursor.getColumnIndex("IGSTAmount")));
                        IGSTAmount.setLeft(10);
                        IGSTAmount.setWidth(50);
                        IGSTAmount.setTextSize(12);
                        IGSTAmount.setBackgroundResource(R.drawable.border);

                        TextView Pos  = new TextView(myContext);
                        Pos.setText(cursor.getString(cursor.getColumnIndex("POS")));
                        Pos.setLeft(10);
                        Pos.setWidth(60);
                        Pos.setTextSize(12);
                        Pos.setBackgroundResource(R.drawable.border);

                        rowcursor.addView(SNo);
                        rowcursor.addView(OriNo);
                        rowcursor.addView(OriDate);
                        rowcursor.addView(GSTIN);
                        rowcursor.addView(InvoiceNo);
                        rowcursor.addView(InvoiceDate);
                        rowcursor.addView(Value);
                        rowcursor.addView(SupplyType);
                        rowcursor.addView(HSNCode);
                        rowcursor.addView(TaxableValue);
                        rowcursor.addView(IGSTRate);
                        rowcursor.addView(IGSTAmount);
                        rowcursor.addView(CGSTRate);
                        rowcursor.addView(CGSTAmount);
                        rowcursor.addView(SGSTRate);
                        rowcursor.addView(SGSTAmount);
                        rowcursor.addView(Pos);

                        tb_inward_taxed_amend.addView(rowcursor, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                        //add a new line to the TableLayout:
                        final View vline = new View(this);
                        vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,2));
                        //vline.setBackgroundColor(Color.BLUE);
                        tb_inward_taxed_amend.addView(vline);



                    } while (cursor.moveToNext());
                }
            }
        }// end try
        catch(Exception e)
        {
            MsgBox = new AlertDialog.Builder(myContext);
            MsgBox. setMessage(e.getMessage())
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
        finally {
            dbGSTR2.CloseDatabase();
        }

    }
    // calendar button click event
    public void dateDialog(View view) {

        try {
            DatePickerFragment dialog = new DatePickerFragment();
            dialog.show(getFragmentManager(), "Date Picker");
        }
        catch (Exception e)
        {
            Toast.makeText(myContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year_param, int month_param, int dayOfMonth) {
        year_int = year_param;
        month_int = month_param;
        day_int = dayOfMonth;
        try {
            /*textview_month = (TextView) findViewById(R.id.text_MonthValue);
            textview_year = (TextView) findViewById(R.id.text_YearValue);
            */
            month = month_array[month_int];
            year = String.valueOf(year_param);
            textview_month.setText(month);
            textview_year.setText(year);
            clearTables();
            loadTable_inward_taxed();
            loadTable_inward_taxed_amend();
        }
        catch(Exception e)
        {
            Toast.makeText(myContext, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public class  DatePickerFragment extends DialogFragment
    {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {

            if (day.equals("") || year.equals("")||month.equals("")){
                Calendar cal = Calendar.getInstance();
                year_int = cal.get(Calendar.YEAR);
                month_int = cal.get(Calendar.MONTH);
                day_int = cal.get(Calendar.DAY_OF_MONTH);
            }
            else {

                year_int = Integer.parseInt(year);
                day_int = Integer.parseInt(day);
                month_int = Arrays.asList(month_array).indexOf(month);
            }
            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener)getActivity(),year_int, month_int,day_int);
        }
    }



}

