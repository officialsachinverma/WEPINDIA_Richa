package com.wepindia.pos.GST;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.gst.Model_reconcile;
import com.wepindia.pos.R;

import java.util.ArrayList;

public class GSTR_reconcile_Activity extends AppCompatActivity {

    Context myContext;
    DatabaseHandler dbGSTR_reconcile;
    public AlertDialog.Builder MsgBox;


    // Variables
    TextView textView_taxeeName, textView_taxeeGSTIN;
    Spinner spinner;
    ArrayList<Model_reconcile> list_to_display ; //= new ArrayList<Model_reconcile>();
    ArrayAdapter<Model_reconcile> adapter ; //= new reconcileAdapter(myContext, list_to_display);
    ListView listView; // = (ListView)findViewById(R.id.listview_reconcile_data);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove default title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gstr_reconcile);



        dbGSTR_reconcile = new DatabaseHandler(GSTR_reconcile_Activity.this);
        myContext = this;
        spinner = (Spinner) findViewById(R.id.spinner_report);
        try
        {
            dbGSTR_reconcile.CreateDatabase();
            dbGSTR_reconcile.OpenDatabase();

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String choice = parent.getItemAtPosition(position).toString();
                        if (choice.equalsIgnoreCase("GSTR1"))
                        {
                            reconcile1();
                        }
                        else if(choice.equalsIgnoreCase("GSTR2")) {
                            reconcile2();
                        }
                        else
                        {
                            Toast.makeText(myContext, "Choose GSTR1 or GSTR2", Toast.LENGTH_SHORT).show();
                        }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    MsgBox = new AlertDialog.Builder(myContext);
                    MsgBox.setMessage("Please choose the register")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
            });


        }
        catch(Exception e)
        {
            MsgBox = new AlertDialog.Builder(this);
            MsgBox.setMessage(e.getMessage())
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
        finally {
            dbGSTR_reconcile.CloseDatabase();
        }



    } //  end onCreate

    /*int gettotal(Cursor cursorBill, String ColumnName )
    {
        int total =0;
        if ((cursorBill != null ) &&(cursorBill.moveToFirst()))
        {
            do {
                total += Integer.valueOf(cursorBill.getString(cursorBill.getColumnIndex(ColumnName)));
            }while(cursorBill.moveToNext());
        }
        return  total;
    }*/

    void reconcile2()
    {
        dbGSTR_reconcile.OpenDatabase();
        /*TextView tv_Sno, tv_gstin, tv_invoiceno, tv_invoiceDate, tv_value,tv_hsn,tv_taxableValue, tv_igstAmt, tv_sgstAmt, tv_cgstAmt, tv_pos;
        tv_Sno = (TextView) findViewById(R.id.gstr2a_item_sno);
        tv_gstin = (TextView) findViewById(R.id.gstr2a_item_gstin);
        tv_invoiceno = (TextView) findViewById(R.id.gstr2a_item_invoice_no);
        tv_invoiceDate = (TextView) findViewById(R.id.gstr2a_item_invoice_date);
        tv_value = (TextView) findViewById(R.id.gstr2a_item_value);
        tv_hsn = (TextView) findViewById(R.id.gstr2a_item_hsn);
        tv_taxableValue = (TextView) findViewById(R.id.gstr2a_item_taxable_value);
        tv_igstAmt = (TextView) findViewById(R.id.gstr2a_item_igst_amt);
        tv_cgstAmt = (TextView) findViewById(R.id.gstr2a_item_cgst_amt);
        tv_sgstAmt = (TextView) findViewById(R.id.gstr2a_item_sgst_amt);
        tv_pos = (TextView) findViewById(R.id.gstr2a_item_pos);
*/
        String category = "Supplier";
        Cursor cursorSupplierList = dbGSTR_reconcile.getGSTINListOnCategory(category);// user_gstin
        try {
            if ((cursorSupplierList != null) && (cursorSupplierList.moveToFirst())) {
                Toast.makeText(myContext,"GSTIN found",Toast.LENGTH_SHORT).show();
                do {
                    String gstin = cursorSupplierList.getString(cursorSupplierList.getColumnIndex("GSTIN")).toString();
                    String date = "0"; // richa to do
                    Cursor cursorBill_2 = dbGSTR_reconcile.getBillsforGSTIN_2(gstin, "","",date);
                    Cursor cursorBill_2A = dbGSTR_reconcile.getBillsforGSTIN_2A(gstin,"",date);
                    ArrayList<Model_reconcile> mArrayList_2 = new ArrayList<Model_reconcile>();
                    ArrayList<Model_reconcile> mArrayList_2A = new ArrayList<Model_reconcile>();
                    int countBill_2 = 0;
                    int countBill_2A =0;
                    int value_2 = 0, value_2A = 0, taxable_value_2A = 0, taxable_value_2 = 0;
                    while(cursorBill_2.moveToNext())
                    {
                        Toast.makeText(myContext,"cursorBill_2",Toast.LENGTH_SHORT).show();
                        countBill_2++;


                        mArrayList_2.add(new Model_reconcile(countBill_2,cursorBill_2)); //add the item
                        //value_2 += cursorBill_2.getInt(cursorBill_2.getColumnIndex(DatabaseHandler.KEY_Value));
                        taxable_value_2 += cursorBill_2.getInt(cursorBill_2.getColumnIndex(DatabaseHandler.KEY_TaxableValue));
                    }
                    while(cursorBill_2A.moveToNext())
                    {
                        Toast.makeText(myContext,"cursorBill_2A",Toast.LENGTH_SHORT).show();
                        countBill_2A++;
                        mArrayList_2A.add(new Model_reconcile(countBill_2A,cursorBill_2A)); //add the item
                        //value_2A += cursorBill_2A.getInt(cursorBill_2A.getColumnIndex(DatabaseHandler.KEY_Value));
                        taxable_value_2A += cursorBill_2A.getInt(cursorBill_2A.getColumnIndex(DatabaseHandler.KEY_TaxableValue));
                    }

                    if (countBill_2 != countBill_2A) // mismatch in no of invoices
                    {
                        display(mArrayList_2,mArrayList_2A);
                    }
                    else if ((value_2 != value_2A) || (taxable_value_2 != taxable_value_2A)) // if same no of bill but mismatch in total values
                    {
                        display(mArrayList_2,mArrayList_2A);
                    }
                    // otherwise no mismatch for this supplier

                } while (cursorSupplierList.moveToNext());



            } else {
                MsgBox = new AlertDialog.Builder(myContext);
                MsgBox.setMessage("No GSTIN list for " + category)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        }
        catch (Exception e)
        {
            MsgBox = new AlertDialog.Builder(this);
            MsgBox.setMessage(e.getMessage())
                    .setPositiveButton("OK ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
        finally {

            dbGSTR_reconcile.CloseDatabase();
        }

    }
    void display(ArrayList<Model_reconcile> mArrayList_2, ArrayList<Model_reconcile> mArrayList_2A )
    {
        Boolean found = false, print = false;
        Model_reconcile obj_2A = null;
        Model_reconcile obj_2 = null;
        list_to_display = new ArrayList<Model_reconcile>();
        if (adapter != null) {
            //richa to do
           // adapter.notifyNewDataAdded(list_to_display);
        } else {
            adapter = new reconcileAdapter(myContext, list_to_display);

        }

        Model_reconcile mm = new Model_reconcile();
        list_to_display.add(mm);
        listView = (ListView)findViewById(R.id.listview_reconcile_data);
        listView.setAdapter(adapter);

        for(int i = 0; i< mArrayList_2.size();i++)
        {
            Toast.makeText(myContext, "Richa 1", Toast.LENGTH_SHORT).show();
            obj_2 = mArrayList_2.get(i);
            for(int j=0;found == false && j<mArrayList_2A.size();j++)
            {
                Toast.makeText(myContext, "Richa2", Toast.LENGTH_SHORT).show();
                found = false;
                obj_2A = mArrayList_2A.get(j);
                if((obj_2.getInvoiceNo().equalsIgnoreCase(obj_2A.getInvoiceNo())) && (obj_2.getInvoiceDate().equalsIgnoreCase(obj_2A.getInvoiceDate())))
                {
                    found = true;
                    if((obj_2.getValue().equalsIgnoreCase(obj_2A.getValue())) && (obj_2.getTaxable_value().equalsIgnoreCase(obj_2A.getTaxable_value())))
                    {
                            // no mismatch
                        // nothing to do
                        Toast.makeText(myContext, "Same Invoice", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        // mismatch in values/taxable values
                        print = true;
                        Toast.makeText(myContext, "not Same Invoice", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            if(found == false)
            {
                // entry missing from supplier end, ie in GSTR2A
                print = true;
            }
            // printing code
                    print= true;
            if (print == true)
            {
                // display data

                list_to_display.add(obj_2A);
                //adapter.notifyNewDataAdded();
                //SystemClock.sleep(10000);

            }
            if (print == true)
            {
                // display data

                //list_to_display.add(obj_2);
               // adapter.notifyDataSetChanged();
                //SystemClock.sleep(5000);

            }


        }
        // displaying entries present in  2A but not in draft 2 , ie. those entries which are entered by supplier but not by owner of this module
        for(int i = 0; i< mArrayList_2A.size();i++)
        {
            obj_2A = mArrayList_2A.get(i);
            for(int j=0;found == false && j<mArrayList_2.size();j++)
            {
                found = false;
                obj_2 = mArrayList_2.get(j);
                if((obj_2.getInvoiceNo().equalsIgnoreCase(obj_2A.getInvoiceNo())) && (obj_2.getInvoiceDate().equalsIgnoreCase(obj_2A.getInvoiceDate())))
                {
                    //invoice present in drft gstr2 also
                    found = true;
                }
            }
            if(found == false)
            {
                // entry missing from owner end, ie in GSTR2
                // printing code
            }

        }
    }
    void reconcile1()
    {
        dbGSTR_reconcile.OpenDatabase();
        dbGSTR_reconcile.CloseDatabase();
    }
}
