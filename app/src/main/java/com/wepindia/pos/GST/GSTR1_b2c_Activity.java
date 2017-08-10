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
 * Created by welcome on 28-10-2016.
 */

public class GSTR1_b2c_Activity extends Activity implements DatePickerDialog.OnDateSetListener {
    Context myContext;
    // DatabaseHandler_gst object
    DatabaseHandler dbGSTR1;
    //Message dialog object
    public AlertDialog.Builder MsgBox;

    final int TAXABLE_LIMIT = 250000;

    // Variables
    TextView textview_month, textview_year;
    String day = "", month = "", year = "";
    int day_int, month_int, year_int;
    String month_array[] = {"January", "February", "March", "April","May","June","July","August","September","October","November","December"};
    TableLayout tb_b2cl, tb_b2cla, tb_b2cs, tb_b2csa;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove default title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gstr1_b2c  );

        dbGSTR1 = new DatabaseHandler(GSTR1_b2c_Activity.this);
        myContext = this;

        try {

            day = getIntent().getStringExtra("Date");
            month = getIntent().getStringExtra("Month");
            year = getIntent().getStringExtra("Year");

            textview_month = (TextView) findViewById(R.id.text_MonthValue_b2c);
            textview_year = (TextView) findViewById(R.id.text_YearValue_b2c);
            tb_b2cl = (TableLayout) findViewById(R.id.tbl_b2cl) ;
            tb_b2cla = (TableLayout) findViewById(R.id.tbl_b2cla) ;
            tb_b2cs = (TableLayout) findViewById(R.id.tbl_b2cs) ;
            tb_b2csa = (TableLayout) findViewById(R.id.tbl_b2csa) ;

            clearTables();
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
                loadTable_b2cl();
                loadTable_b2cla();

            }

            //dbHomeScreen.DeleteDatabase();
            dbGSTR1.CreateDatabase();
            dbGSTR1.OpenDatabase();//
            dbGSTR1.CloseDatabase();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void loadTable_b2cl()
    {
        try
        {
            dbGSTR1.OpenDatabase();
            Cursor cursor = dbGSTR1.getOutwardB2Cl("","");
            if (cursor == null )
            {
                MsgBox = new AlertDialog.Builder(myContext);
                MsgBox. setMessage("No data for this month for B2Cl")
                        .setPositiveButton("OK", null)
                        .show();
            }
            else {
                if (cursor.moveToFirst()) {

                    TextView CustStateCode, CustName, InvoiceNo, InvoiceDate, SupplyType, HSNCode, TaxableValue;
                    TextView CGSTRate, CGSTAmount, SGSTRate, SGSTAmount,IGSTRate, IGSTAmount;

                    int count_l =1001;
                    int count_s =1001;
                    TableRow rowcursor;

                    do {
                        rowcursor = new TableRow(myContext);
                        rowcursor.setLayoutParams(new TableRow.LayoutParams
                                (TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                        TextView SNo = new TextView(myContext);
                        SNo.setLeft(10);
                        //SNo.setText(String.valueOf(count));
                        SNo.setWidth(53);
                        SNo.setTextSize(12);
                        SNo.setBackgroundResource(R.drawable.border);
                        //count++;

                        CustStateCode = new TextView(myContext);
                        CustStateCode.setLeft(10);
                        //CustStateCode.setText(cursor.getString(cursor.getColumnIndex("CustStateCode")));
                        CustStateCode.setWidth(195);
                        CustStateCode.setTextSize(12);
                        CustStateCode.setBackgroundResource(R.drawable.border);

                        CustName = new TextView(myContext);
                        CustName.setLeft(10);
                        CustName.setText(cursor.getString(cursor.getColumnIndex("CustName")));
                        CustName.setWidth(145);
                        CustName.setTextSize(12);
                        CustName.setBackgroundResource(R.drawable.border);


                        InvoiceNo = new TextView(myContext);
                        InvoiceNo.setLeft(10);
                        InvoiceNo.setWidth(75);
                        InvoiceNo.setTextSize(12);
                        //Richa to do
                        String no = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                        no.trim();
                        InvoiceNo.setText(no);
                        InvoiceNo.setBackgroundResource(R.drawable.border);

                        InvoiceDate = new TextView(myContext);
                        InvoiceDate.setLeft(10);
                        InvoiceDate.setWidth(85);
                        String substr = "";
                        //InvoiceDate = (TextView)findViewById(R.id.b2b_InvDate);
                        String date_temp = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                        /*if (date_temp !=null)
                        {
                            date_temp.trim();
                            substr=date_temp.substring(0,2);
                            substr+= month.substring(0,3);;
                        }*/

                        InvoiceDate.setText(date_temp);
                        InvoiceDate.setTextSize(12);
                        InvoiceDate.setBackgroundResource(R.drawable.border);

                        SupplyType = new TextView(myContext);
                        SupplyType.setText(cursor.getString(cursor.getColumnIndex("SupplyType")));
                        SupplyType.setLeft(10);
                        SupplyType.setWidth(35);
                        SupplyType.setTextSize(12);
                        SupplyType.setId(100);
                        SupplyType.setBackgroundResource(R.drawable.border);


                        HSNCode = new TextView(myContext);
                        HSNCode.setText(cursor.getString(cursor.getColumnIndex("HSNCode")));
                        HSNCode.setLeft(10);
                        HSNCode.setWidth(95);
                        HSNCode.setTextSize(12);
                        HSNCode.setBackgroundResource(R.drawable.border);
                        //HSNCode.setText("Richa");

                        TaxableValue = new TextView(myContext);
                        TaxableValue.setText(cursor.getString(cursor.getColumnIndex("TaxableValue")));
                        TaxableValue.setLeft(10);
                        TaxableValue.setWidth(130);
                        TaxableValue.setTextSize(12);
                        TaxableValue.setBackgroundResource(R.drawable.border);


                        IGSTRate = new TextView(myContext);
                        IGSTRate.setText(cursor.getString(cursor.getColumnIndex("IGSTRate")));
                        IGSTRate.setLeft(10);
                        IGSTRate.setWidth(95);
                        IGSTRate.setTextSize(12);
                        IGSTRate.setBackgroundResource(R.drawable.border);

                        IGSTAmount = new TextView(myContext);
                        IGSTAmount.setText(cursor.getString(cursor.getColumnIndex("IGSTAmount")));
                        IGSTAmount.setLeft(10);
                        IGSTAmount.setWidth(90);
                        IGSTAmount.setTextSize(12);
                        IGSTAmount.setBackgroundResource(R.drawable.border);

                        CGSTRate = new TextView(myContext);
                        CGSTRate.setText(cursor.getString(cursor.getColumnIndex("CGSTRate")));
                        CGSTRate.setLeft(10);
                        CGSTRate.setWidth(95);
                        CGSTRate.setTextSize(12);
                        CGSTRate.setBackgroundResource(R.drawable.border);

                        CGSTAmount = new TextView(myContext);
                        CGSTAmount.setText(cursor.getString(cursor.getColumnIndex("CGSTAmount")));
                        CGSTAmount.setLeft(10);
                        CGSTAmount.setWidth(90);
                        CGSTAmount.setTextSize(12);
                        CGSTAmount.setBackgroundResource(R.drawable.border);


                        SGSTRate = new TextView(myContext);
                        SGSTRate.setText(cursor.getString(cursor.getColumnIndex("SGSTRate")));
                        SGSTRate.setLeft(10);
                        SGSTRate.setWidth(95);
                        SGSTRate.setTextSize(12);
                        SGSTRate.setBackgroundResource(R.drawable.border);

                        SGSTAmount = new TextView(myContext);
                        SGSTAmount.setText(cursor.getString(cursor.getColumnIndex("SGSTAmount")));
                        SGSTAmount.setLeft(10);
                        SGSTAmount.setWidth(90);
                        SGSTAmount.setTextSize(12);
                        SGSTAmount.setBackgroundResource(R.drawable.border);

                        TextView Pos  = new TextView(myContext);
                        Pos.setText(cursor.getString(cursor.getColumnIndex("POS")));
                        Pos.setLeft(10);
                        Pos.setWidth(60);
                        Pos.setTextSize(12);
                        Pos.setBackgroundResource(R.drawable.border);

                        TextView ProAss = new TextView(myContext);
                        //richa to do
                        ProAss.setText(cursor.getString(cursor.getColumnIndex("ProvisionalAssess")));
                        ProAss.setLeft(10);
                        ProAss.setWidth(100);
                        ProAss.setTextSize(12);
                        ProAss.setBackgroundResource(R.drawable.border);
                        String IGSTRate_str = IGSTRate.getText().toString();
                        if ((Float.valueOf(TaxableValue.getText().toString()) >= TAXABLE_LIMIT ) && (IGSTRate_str.equals("")))
                        {
                            SNo.setText(String.valueOf(count_l));
                            count_l++;

                            rowcursor.addView(SNo);
                            rowcursor.addView(CustStateCode);
                            rowcursor.addView(CustName);
                            rowcursor.addView(InvoiceNo);
                            rowcursor.addView(InvoiceDate);
                            rowcursor.addView(SupplyType);
                            rowcursor.addView(HSNCode);
                            rowcursor.addView(TaxableValue);
                            rowcursor.addView(IGSTRate);
                            rowcursor.addView(IGSTAmount);
                            rowcursor.addView(Pos);
                            rowcursor.addView(ProAss);

                            tb_b2cl.addView(rowcursor, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                            //add a new line to the TableLayout:
                            final View vline = new View(this);
                            vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 2));
                            tb_b2cl.addView(vline);
                            addb2c_items(no,date_temp,true);
                        }
                        else
                        {
                            SNo.setText(String.valueOf(count_s));
                            count_s++;
                            String sn = InvoiceNo.getText().toString()+"-"+String.valueOf(count_s);
                            SNo.setText(sn);
                            rowcursor.addView(SNo);
                            rowcursor.addView(SupplyType);
                            rowcursor.addView(HSNCode);
                            rowcursor.addView(CustStateCode);
                            rowcursor.addView(TaxableValue);
                            rowcursor.addView(IGSTRate);
                            rowcursor.addView(IGSTAmount);
                            rowcursor.addView(CGSTRate);
                            rowcursor.addView(CGSTAmount);
                            rowcursor.addView(SGSTRate);
                            rowcursor.addView(SGSTAmount);
                            rowcursor.addView(ProAss);

                            tb_b2cs.addView(rowcursor, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                            //add a new line to the TableLayout:
                            final View vline = new View(this);
                            vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 2));
                            tb_b2cs.addView(vline);
                            //addb2c_items(no,date_temp,false);
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
            dbGSTR1.CloseDatabase();
        }


    }

    public void addb2c_items(String no,String date_temp,Boolean isb2cl)
    {
        Cursor cursor = dbGSTR1.getitems_b2cl(no, date_temp,"","");
        if (cursor == null)
        {
            MsgBox = new AlertDialog.Builder(myContext);
            MsgBox. setMessage("B2CL : No items for Invoice No : "+no+" & Invoice Date : "+date_temp)
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

                    TextView CustStateCode,CustName, InvoiceNo, InvoiceDate, SupplyType, TaxationType, HSNCode, Description, Price, Quantity,Units,SubValue,DiscountRate,TaxableValue;
                    TextView CGSTRate, CGSTAmount, SGSTRate, SGSTAmount,IGSTRate, IGSTAmount,AdditionalChargeName,AdditionalChargeAmount;

                    int count_l =1;
                    int count_s =1;
                    TableRow rowcursor;

                    do {
                        rowcursor = new TableRow(myContext);
                        rowcursor.setLayoutParams(new TableRow.LayoutParams
                                (TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                        TextView SNo = new TextView(myContext);
                        SNo.setLeft(10);
                       // SNo.setText(String.valueOf(count_));
                        SNo.setWidth(42);
                        SNo.setTextSize(12);
                        SNo.setBackgroundResource(R.drawable.border_item);
                        //count++;

                        CustStateCode = new TextView(myContext);
                        CustStateCode.setLeft(10);
                        // GSTIN.setText("GSTIN12345678AB");
                        CustStateCode.setWidth(155);
                        CustStateCode.setTextSize(12);
                        CustStateCode.setBackgroundResource(R.drawable.border_item);
                        CustName = new TextView(myContext);
                        CustName.setLeft(10);
                        // GSTIN.setText("GSTIN12345678AB");
                        CustName.setWidth(155);
                        CustName.setTextSize(12);
                        CustName.setBackgroundResource(R.drawable.border_item);


                        InvoiceNo = new TextView(myContext);
                        InvoiceNo.setLeft(10);
                        InvoiceNo.setWidth(70);
                        InvoiceNo.setTextSize(12);
                        //Richa to do
                        String no1 = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                        no1.trim();
                        //InvoiceNo.setText(no);
                        InvoiceNo.setBackgroundResource(R.drawable.border_item);

                        InvoiceDate = new TextView(myContext);
                        InvoiceDate.setLeft(10);
                        InvoiceDate.setWidth(60);
                        String date_temp1 = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                        date_temp1.trim();
                        String substr=date_temp1.substring(0,2);
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


                        //TaxationType = new TextView(myContext);
                        //TaxationType.setText(cursor.getString(cursor.getColumnIndex("TaxationType")));

                        HSNCode = new TextView(myContext);
                        //HSNCode.setText(cursor.getString(cursor.getColumnIndex("HSNCode")));
                        String HSN = cursor.getString(cursor.getColumnIndex("HSNCode"));
                        String desc = cursor.getString(cursor.getColumnIndex("Description"));
                        HSN = HSN + "-" + desc;
                        HSNCode.setText(HSN);
                        HSNCode.setLeft(10);
                        HSNCode.setWidth(70);
                        HSNCode.setTextSize(12);
                        HSNCode.setBackgroundResource(R.drawable.border_item);


                        TaxableValue = new TextView(myContext);
                        TaxableValue.setText(cursor.getString(cursor.getColumnIndex("TaxableValue")));
                        TaxableValue.setLeft(10);
                        TaxableValue.setWidth(100);
                        TaxableValue.setTextSize(12);
                        TaxableValue.setBackgroundResource(R.drawable.border_item);

                        IGSTRate = new TextView(myContext);
                        IGSTRate.setText(cursor.getString(cursor.getColumnIndex("IGSTRate")));
                        IGSTRate.setLeft(10);
                        IGSTRate.setWidth(60);
                        IGSTRate.setTextSize(12);
                        IGSTRate.setBackgroundResource(R.drawable.border_item);

                        IGSTAmount = new TextView(myContext);
                        IGSTAmount.setText(cursor.getString(cursor.getColumnIndex("IGSTAmount")));
                        IGSTAmount.setLeft(10);
                        IGSTAmount.setWidth(60);
                        IGSTAmount.setTextSize(12);
                        IGSTAmount.setBackgroundResource(R.drawable.border_item);

                        CGSTRate = new TextView(myContext);
                        CGSTRate.setText(cursor.getString(cursor.getColumnIndex("CGSTRate")));
                        CGSTRate.setLeft(10);
                        CGSTRate.setWidth(60);
                        CGSTRate.setTextSize(12);
                        CGSTRate.setBackgroundResource(R.drawable.border_item);

                        CGSTAmount = new TextView(myContext);
                        CGSTAmount.setText(cursor.getString(cursor.getColumnIndex("CGSTAmount")));
                        CGSTAmount.setLeft(10);
                        CGSTAmount.setWidth(60);
                        CGSTAmount.setTextSize(12);
                        CGSTAmount.setBackgroundResource(R.drawable.border_item);

                        SGSTRate = new TextView(myContext);
                        SGSTRate.setText(cursor.getString(cursor.getColumnIndex("SGSTRate")));
                        SGSTRate.setLeft(10);
                        SGSTRate.setWidth(60);
                        SGSTRate.setTextSize(12);
                        SGSTRate.setBackgroundResource(R.drawable.border_item);

                        SGSTAmount = new TextView(myContext);
                        SGSTAmount.setText(cursor.getString(cursor.getColumnIndex("SGSTAmount")));
                        SGSTAmount.setLeft(10);
                        SGSTAmount.setWidth(60);
                        SGSTAmount.setTextSize(12);
                        SGSTAmount.setBackgroundResource(R.drawable.border_item);

                        TextView Pos  = new TextView(myContext);
                        //Pos.setText(cursor.getString(cursor.getColumnIndex("POS")));
                        Pos.setLeft(10);
                        Pos.setWidth(60);
                        Pos.setTextSize(12);
                        Pos.setBackgroundResource(R.drawable.border_item);

                        TextView ProAss = new TextView(myContext);
                        //ProAss.setText(cursor.getString(cursor.getColumnIndex("ProvisionalAssess")));
                        ProAss.setLeft(10);
                        ProAss.setWidth(60);
                        ProAss.setTextSize(12);
                        ProAss.setBackgroundResource(R.drawable.border_item);

                        if (isb2cl)
                        {
                            SNo.setText(String.valueOf(count_l));
                            count_l++;
                            rowcursor.addView(SNo);
                            rowcursor.addView(CustStateCode);
                            rowcursor.addView(CustName);
                            rowcursor.addView(InvoiceNo);
                            rowcursor.addView(InvoiceDate);
                            rowcursor.addView(SupplyType);
                            rowcursor.addView(HSNCode);
                            rowcursor.addView(TaxableValue);
                            rowcursor.addView(IGSTRate);
                            rowcursor.addView(IGSTAmount);
                            rowcursor.addView(Pos);
                            rowcursor.addView(ProAss);

                            tb_b2cl.addView(rowcursor, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                            //add a new line to the TableLayout:
                            final View vline = new View(this);
                            vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 2));
                            tb_b2cl.addView(vline);
                        }
                        else
                        {
                            SNo.setText(String.valueOf(count_s));
                            count_s++;
                            rowcursor.addView(SNo);
                            rowcursor.addView(SupplyType);
                            rowcursor.addView(HSNCode);
                            rowcursor.addView(CustStateCode);
                            rowcursor.addView(TaxableValue);
                            rowcursor.addView(IGSTRate);
                            rowcursor.addView(IGSTAmount);
                            rowcursor.addView(CGSTRate);
                            rowcursor.addView(CGSTAmount);
                            rowcursor.addView(SGSTRate);
                            rowcursor.addView(SGSTAmount);
                            rowcursor.addView(ProAss);

                            tb_b2cs.addView(rowcursor, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                            //add a new line to the TableLayout:
                            final View vline = new View(this);
                            vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 2));
                            tb_b2cs.addView(vline);
                        }


                    } while (cursor.moveToNext());

                }

            }// end try
            catch (Exception e)
            {
                MsgBox = new AlertDialog.Builder(myContext);
                MsgBox .setTitle("B2CL : Error while fetching items details")
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


    public void loadTable_b2cla()
    {
        try
        {
            dbGSTR1.OpenDatabase();
            // Richa to do : check for fetching logic
            Cursor cursor = dbGSTR1.getOutwardB2cla();
            if (cursor == null )
            {
                MsgBox = new AlertDialog.Builder(myContext);
                MsgBox. setMessage("No data for this month b2cla")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
            else {
                if (cursor.moveToFirst()) {

                    TextView CustName, CustStateCode, InvoiceNo, InvoiceDate, SupplyType, TaxationType, HSNCode, Description, Price, Quantity,Units,SubValue,DiscountRate,TaxableValue;
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
                        SNo.setWidth(52);
                        SNo.setTextSize(12);
                        SNo.setBackgroundResource(R.drawable.border);
                        count++;

                        TextView OriNo = new TextView(myContext);
                        OriNo.setLeft(10);
                        OriNo.setText(cursor.getString(cursor.getColumnIndex("OriginalInvoiceNo")));
                        OriNo.setWidth(70);
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
                        OriDate.setWidth(65);
                        OriDate.setTextSize(12);
                        OriDate.setBackgroundResource(R.drawable.border);


                        CustStateCode = new TextView(myContext);
                        CustStateCode.setLeft(10);
                       // CustStateCode.setText(cursor.getString(cursor.getColumnIndex("CustStateCode")));
                        CustStateCode.setWidth(150);
                        CustStateCode.setTextSize(12);
                        CustStateCode.setBackgroundResource(R.drawable.border);

                        CustName = new TextView(myContext);
                        CustName.setLeft(10);
                        CustName.setText(cursor.getString(cursor.getColumnIndex("CustName")));
                        CustName.setWidth(140);
                        CustName.setTextSize(12);
                        CustName.setBackgroundResource(R.drawable.border);

                        InvoiceNo = new TextView(myContext);
                        //InvoiceNo = (TextView)findViewById(R.id.b2b_InvNo);
                        InvoiceNo.setLeft(10);
                        InvoiceNo.setWidth(70);
                        InvoiceNo.setTextSize(12);
                        //Richa to do
                        String no = cursor.getString(cursor.getColumnIndex("NewInvoiceNo"));
                        InvoiceNo.setText(no);
                        InvoiceNo.setBackgroundResource(R.drawable.border);

                        InvoiceDate = new TextView(myContext);
                        InvoiceDate.setLeft(10);
                        InvoiceDate.setWidth(80);
                        //InvoiceDate = (TextView)findViewById(R.id.b2b_InvDate);
                        String date_temp1 = cursor.getString(cursor.getColumnIndex("NewInvoiceDate"));
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
                        String Desc = cursor.getString(cursor.getColumnIndex("Description"));
                        HSN = HSN + " - " + Desc;
                        HSNCode.setText(HSN);
                        HSNCode.setLeft(10);
                        HSNCode.setWidth(100);
                        HSNCode.setTextSize(12);
                        HSNCode.setBackgroundResource(R.drawable.border);


                        TaxableValue = new TextView(myContext);
                        TaxableValue.setText(cursor.getString(cursor.getColumnIndex("TaxableValue")));
                        TaxableValue.setLeft(10);
                        TaxableValue.setWidth(90);
                        TaxableValue.setTextSize(12);
                        TaxableValue.setBackgroundResource(R.drawable.border);

                        IGSTRate = new TextView(myContext);
                        IGSTRate.setText(cursor.getString(cursor.getColumnIndex("IGSTRate")));
                        IGSTRate.setLeft(10);
                        IGSTRate.setWidth(60);
                        IGSTRate.setTextSize(12);
                        IGSTRate.setBackgroundResource(R.drawable.border);

                        IGSTAmount = new TextView(myContext);
                        IGSTAmount.setText(cursor.getString(cursor.getColumnIndex("IGSTAmount")));
                        IGSTAmount.setLeft(10);
                        IGSTAmount.setWidth(60);
                        IGSTAmount.setTextSize(12);
                        IGSTAmount.setBackgroundResource(R.drawable.border);

                        CGSTRate = new TextView(myContext);
                        CGSTRate.setText(cursor.getString(cursor.getColumnIndex("IGSTRate")));
                        CGSTRate.setLeft(10);
                        CGSTRate.setWidth(60);
                        CGSTRate.setTextSize(12);
                        CGSTRate.setBackgroundResource(R.drawable.border);

                        CGSTAmount = new TextView(myContext);
                        CGSTAmount.setText(cursor.getString(cursor.getColumnIndex("CGSTAmount")));
                        CGSTAmount.setLeft(10);
                        CGSTAmount.setWidth(60);
                        CGSTAmount.setTextSize(12);
                        CGSTAmount.setBackgroundResource(R.drawable.border);

                        SGSTRate = new TextView(myContext);
                        SGSTRate.setText(cursor.getString(cursor.getColumnIndex("SGSTRate")));
                        SGSTRate.setLeft(10);
                        SGSTRate.setWidth(60);
                        SGSTRate.setTextSize(12);
                        SGSTRate.setBackgroundResource(R.drawable.border);

                        SGSTAmount = new TextView(myContext);
                        SGSTAmount.setText(cursor.getString(cursor.getColumnIndex("SGSTAmount")));
                        SGSTAmount.setLeft(10);
                        SGSTAmount.setWidth(60);
                        SGSTAmount.setTextSize(12);
                        SGSTAmount.setBackgroundResource(R.drawable.border);

                        TextView Pos  = new TextView(myContext);
                        Pos.setText(cursor.getString(cursor.getColumnIndex("POS")));
                        Pos.setLeft(10);
                        Pos.setWidth(60);
                        Pos.setTextSize(12);
                        Pos.setBackgroundResource(R.drawable.border);

                        TextView ProAss = new TextView(myContext);
                        ProAss.setText(cursor.getString(cursor.getColumnIndex("ProvisionalAssess")));
                        ProAss.setLeft(10);
                        ProAss.setWidth(60);
                        ProAss.setTextSize(12);
                        ProAss.setBackgroundResource(R.drawable.border);

                        if ((Integer.valueOf(TaxableValue.getText().toString()) >TAXABLE_LIMIT) && (Integer.valueOf(IGSTRate.getText().toString()) >0)) {
                            rowcursor.addView(SNo);
                            rowcursor.addView(OriNo);
                            rowcursor.addView(OriDate);
                            rowcursor.addView(CustStateCode);
                            rowcursor.addView(CustName);
                            rowcursor.addView(InvoiceNo);
                            rowcursor.addView(InvoiceDate);
                            rowcursor.addView(SupplyType);
                            rowcursor.addView(HSNCode);
                            rowcursor.addView(TaxableValue);
                            rowcursor.addView(IGSTRate);
                            rowcursor.addView(IGSTAmount);
                            rowcursor.addView(Pos);
                            rowcursor.addView(ProAss);

                            tb_b2cla.addView(rowcursor, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                            //add a new line to the TableLayout:
                            final View vline = new View(this);
                            vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 2));
                            //vline.setBackgroundColor(Color.BLUE);
                            tb_b2cla.addView(vline);
                        }
                        else
                        {
                            OriDate.setText(month_t1);
                            rowcursor.addView(SupplyType);
                            rowcursor.addView(HSNCode);
                            rowcursor.addView(OriDate);
                            rowcursor.addView(CustStateCode);

                            rowcursor.addView(SupplyType);
                            rowcursor.addView(HSNCode);
                            rowcursor.addView(InvoiceDate);
                            rowcursor.addView(CustStateCode);

                            rowcursor.addView(TaxableValue);
                            rowcursor.addView(IGSTRate);
                            rowcursor.addView(IGSTAmount);
                            rowcursor.addView(CGSTRate);
                            rowcursor.addView(CGSTAmount);
                            rowcursor.addView(SGSTRate);
                            rowcursor.addView(SGSTAmount);
                            rowcursor.addView(ProAss);

                            tb_b2csa.addView(rowcursor, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                            //add a new line to the TableLayout:
                            final View vline = new View(this);
                            vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,2));
                            //vline.setBackgroundColor(Color.BLUE);
                            tb_b2csa.addView(vline);
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
            dbGSTR1.CloseDatabase();
        }

    }
    public void clearTables()
    {
        // clear all data from screen
        tb_b2cl.removeAllViews();
        tb_b2cla.removeAllViews();
        tb_b2cs.removeAllViews();
        tb_b2csa.removeAllViews();
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

            month = month_array[month_int];
            year = String.valueOf(year_param);
            textview_month.setText(month);
            textview_year.setText(year);
            clearTables();
            loadTable_b2cl();
            loadTable_b2cla();
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
            int year_int;
            int month_int;
            int day_int;
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
