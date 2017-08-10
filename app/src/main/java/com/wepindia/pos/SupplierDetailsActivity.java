package com.wepindia.pos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mswipetech.wisepad.sdktest.view.ApplicationData;
import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.Database.Supplier_Model;
import com.wep.common.app.WepBaseActivity;
import com.wep.common.app.views.WepButton;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.adapters.SupplierAdapter;
import com.wepindia.pos.utils.ActionBarUtils;

import java.util.ArrayList;
import java.util.Date;

public class SupplierDetailsActivity extends WepBaseActivity {

    Context myContext;
    DatabaseHandler dbSupplierDetails;
    MessageDialog MsgBox;
    Toolbar toolbar;
    ListView lstSupplierDetails;
    EditText edt_supplierGSTIN,et_inw_supplierAddress, autocompletetv_supplierPhn;
    TextView tv_suppliercode;
    AutoCompleteTextView autocompletetv_suppliername;
    WepButton btnAddSupplier,btnUpdateSupplier, btnClear,btnClose;
    ArrayList<Supplier_Model> SupplierList ;
    com.wepindia.pos.adapters.SupplierAdapter SupplierAdapter;
    ArrayList<String> labelsSupplierName ;

    String suppliername_clicked , suppliergstin_clicked;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String userName = ApplicationData.getUserName(this);
        Date d = new Date();
        CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
        try {

            myContext = this;
            MsgBox = new MessageDialog(myContext);
            dbSupplierDetails = new DatabaseHandler(myContext);
            dbSupplierDetails.CloseDatabase();
            dbSupplierDetails.CreateDatabase();
            dbSupplierDetails.OpenDatabase();

            InitialiseViewVariables();
            AssignClickActions();
            loadAutoCompleteSupplierName();
            Clear();
            Display();

        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.Show("Oops", "" + e.getMessage());
        }
        finally {
            com.wep.common.app.ActionBarUtils.setupToolbar(this,toolbar,getSupportActionBar(),"Supplier Details",userName," Date:"+s.toString());
        }

    }

    private void InitialiseViewVariables()
    {
        lstSupplierDetails = (ListView) findViewById(R.id.lstSupplierDetails);
        tv_suppliercode = (TextView) findViewById(R.id.tv_suppliercode);;
        edt_supplierGSTIN = (EditText) findViewById(R.id.edt_supplierGSTIN);;
        et_inw_supplierAddress = (EditText) findViewById(R.id.et_inw_supplierAddress);
        autocompletetv_supplierPhn = (EditText) findViewById(R.id.autocompletetv_supplierPhn);
        autocompletetv_suppliername = (AutoCompleteTextView) findViewById(R.id.autocompletetv_suppliername);
        btnAddSupplier = (WepButton) findViewById(R.id.btnAddSupplier);
        btnUpdateSupplier = (WepButton) findViewById(R.id.btnUpdateSupplier);
        btnClear = (WepButton) findViewById(R.id.btnClear);
        btnClose = (WepButton) findViewById(R.id.btnClose);
    }

    private  void AssignClickActions()
    {
        btnAddSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSupplier();
                loadAutoCompleteSupplierName();
                Clear();
                Display();
            }
        });
        btnUpdateSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSupplier();
                loadAutoCompleteSupplierName();
                Clear();
                Display();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clear();
                Display();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Close();
            }
        });
        lstSupplierDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListClickEvent(SupplierAdapter.getItem(position));
            }});

        autocompletetv_suppliername.setOnTouchListener(new View.OnTouchListener(){
            //@Override
            public boolean onTouch(View v, MotionEvent event){
                autocompletetv_suppliername.showDropDown();
                return false;
            }
        });
        autocompletetv_suppliername.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String suppliername_str = autocompletetv_suppliername.getText().toString().toUpperCase();
                Cursor supplierdetail_cursor = dbSupplierDetails.getSupplierDetails(suppliername_str);
                int suppliercode = -1;
                String SupplierPhone= "", SupplierAddress = "";
                if (supplierdetail_cursor!=null && supplierdetail_cursor.moveToFirst())
                {
                    SupplierPhone = supplierdetail_cursor.getString(supplierdetail_cursor.getColumnIndex("SupplierPhone"));
                    SupplierAddress = supplierdetail_cursor.getString(supplierdetail_cursor.getColumnIndex("SupplierAddress"));
                    String supp_gstin  = supplierdetail_cursor.getString(supplierdetail_cursor.getColumnIndex("GSTIN"));
                    autocompletetv_supplierPhn.setText(SupplierPhone);
                    et_inw_supplierAddress.setText(SupplierAddress);
                    if(supp_gstin!=null )
                        edt_supplierGSTIN.setText(supp_gstin);
                    else
                        supp_gstin= "";

                    suppliercode = supplierdetail_cursor.getInt(supplierdetail_cursor.getColumnIndex("SupplierCode"));
                    tv_suppliercode.setText(String.valueOf(suppliercode));
                    suppliergstin_clicked = supp_gstin;
                    suppliername_clicked = suppliername_str;
                    btnAddSupplier.setEnabled(false);
                    btnUpdateSupplier.setEnabled(true);
                }
                AutoCompleteTextView autocompletetv_suppliername_1 = (AutoCompleteTextView) findViewById(R.id.autocompletetv_suppliername);
                autocompletetv_suppliername_1.setFocusableInTouchMode(true);
                autocompletetv_suppliername_1.requestFocus();
            }
        });
    }

    private void ListClickEvent(Supplier_Model supplier)
    {
        btnUpdateSupplier.setEnabled(true);
        btnAddSupplier.setEnabled(false);

        tv_suppliercode.setText(String.valueOf(supplier.getSupplierCode()));

        autocompletetv_suppliername.setFocusable(false);
        autocompletetv_suppliername.setFocusableInTouchMode(false);
        autocompletetv_suppliername.setText(supplier.getSupplierName());
        autocompletetv_suppliername.setFocusable(true);
        autocompletetv_suppliername.setFocusableInTouchMode(true);

        autocompletetv_supplierPhn.setText(supplier.getSupplierPhone());
        edt_supplierGSTIN.setText(supplier.getSupplierGSTIN());
        et_inw_supplierAddress.setText(supplier.getSupplierAddress());

        suppliername_clicked = supplier.getSupplierName();
        suppliergstin_clicked = supplier.getSupplierGSTIN();
    }


    private  void UpdateSupplier() {
        long l =0;
        Cursor cursor = dbSupplierDetails.getAllSupplierName_nonGST();
        labelsSupplierName = new ArrayList<String>();
        ArrayList<String> labelsSupplierGSTIN = new ArrayList<String>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                labelsSupplierName.add(cursor.getString(cursor.getColumnIndex("SupplierName")));// adding
                String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
                if (gstin != null && !gstin.equals(""))
                    labelsSupplierGSTIN.add(gstin);
            } while (cursor.moveToNext());
        }

        MsgBox.setTitle("Incomplete Information")
                .setPositiveButton("Ok", null);

        String supplierType_str = "UnRegistered";
        String suppliername_str = autocompletetv_suppliername.getText().toString().toUpperCase();
        String supplierphn_str = autocompletetv_supplierPhn.getText().toString();
        String supplieraddress_str = et_inw_supplierAddress.getText().toString();
        String suppliergstin_str = edt_supplierGSTIN.getText().toString();
        if (suppliergstin_str != null && !suppliergstin_str.equals(""))
            supplierType_str = "Registered";
        else
            suppliergstin_str = "";

        if (labelsSupplierGSTIN.contains(suppliergstin_str) && !suppliergstin_str.equalsIgnoreCase(suppliergstin_clicked) ) {
            MsgBox.setTitle("Warning")
                    .setMessage("Supplier with gstin already present in list")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        for (String supplier : labelsSupplierName) {
            if (suppliername_str.equalsIgnoreCase(supplier) && !suppliername_str.equalsIgnoreCase(suppliername_clicked)) {
                MsgBox.setTitle("Warning")
                        .setMessage("Supplier with name already present in list")
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }
        }


        if (suppliername_str.equals("") || supplieraddress_str.equals("") || supplierphn_str.equals("")) {
            MsgBox.setMessage("Please fill all details of Supplier")
                    .show();
        } else {
            l = dbSupplierDetails.updateSupplierDetails(supplierType_str, suppliergstin_str, suppliername_str,
                    supplierphn_str, supplieraddress_str, Integer.parseInt(tv_suppliercode.getText().toString()));
            if (l > 0) {
                Log.d("Inward_Supplier Detail", " Supplier details updated at " + l);
                Toast.makeText(myContext, "Supplier details saved at " + l, Toast.LENGTH_SHORT).show();

            }
        }

    }
    private  void AddSupplier() {
        long l = 0;
        Cursor cursor = dbSupplierDetails.getAllSupplierName_nonGST();
        labelsSupplierName = new ArrayList<String>();
        ArrayList<String> labelsSupplierGSTIN = new ArrayList<String>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                labelsSupplierName.add(cursor.getString(cursor.getColumnIndex("SupplierName")));// adding
                String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
                if (gstin != null && !gstin.equals(""))
                    labelsSupplierGSTIN.add(gstin);
            } while (cursor.moveToNext());
        }

        MsgBox.setTitle("Incomplete Information")
                .setPositiveButton("Ok", null);

        String supplierType_str = "UnRegistered";
        String suppliername_str = autocompletetv_suppliername.getText().toString().toUpperCase();
        String supplierphn_str = autocompletetv_supplierPhn.getText().toString();
        String supplieraddress_str = et_inw_supplierAddress.getText().toString();
        String suppliergstin_str = edt_supplierGSTIN.getText().toString();
        if (suppliergstin_str != null && !suppliergstin_str.equals(""))
            supplierType_str = "Registered";
        else
            suppliergstin_str = "";

        if (labelsSupplierGSTIN.contains(suppliergstin_str)) {
            MsgBox.setTitle("Warning")
                    .setMessage("Supplier with gstin already present in list")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        for (String supplier : labelsSupplierName) {
            if (suppliername_str.equalsIgnoreCase(supplier)) {
                MsgBox.setTitle("Warning")
                        .setMessage("Supplier with name already present in list")
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }
        }


        if (suppliername_str.equals("") || supplieraddress_str.equals("") || supplierphn_str.equals("")) {
            MsgBox.setMessage("Please fill all details of Supplier")
                    .show();
        } else {
            l = dbSupplierDetails.saveSupplierDetails(supplierType_str, suppliergstin_str, suppliername_str,
                    supplierphn_str, supplieraddress_str);
            if (l > 0) {
                Log.d("Inward_Item_Entry", " Supplier details saved at " + l);
                Toast.makeText(myContext, "Supplier details saved at " + l, Toast.LENGTH_SHORT).show();

            }
        }

    }

    public void loadAutoCompleteSupplierName()
    {
        try {
            Cursor cursor = dbSupplierDetails.getAllSupplierName_nonGST();
            labelsSupplierName = new ArrayList<String>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    labelsSupplierName.add(cursor.getString(cursor.getColumnIndex("SupplierName")));// adding
                } while (cursor.moveToNext());
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(myContext, android.R.layout.simple_list_item_1,
                    labelsSupplierName);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            autocompletetv_suppliername.setAdapter(dataAdapter);

        } catch (Exception e) {
            MsgBox.setMessage(e.getMessage())
                    .setNeutralButton("Ok", null)
                    .show();
        }
    }
    public void Display()
    {
        if(SupplierList == null)
            SupplierList = new ArrayList<>();

        Cursor supplierCursor = dbSupplierDetails.getAllSupplierName_nonGST();
        while(supplierCursor!=null && supplierCursor.moveToNext())
        {
            int suppliercode = supplierCursor.getInt(supplierCursor.getColumnIndex("SupplierCode"));
            String suppliergstin = supplierCursor.getString(supplierCursor.getColumnIndex("GSTIN"));
            String suppliername = supplierCursor.getString(supplierCursor.getColumnIndex("SupplierName"));
            String suppliernphone = supplierCursor.getString(supplierCursor.getColumnIndex("SupplierPhone"));
            String supplieraddress = supplierCursor.getString(supplierCursor.getColumnIndex("SupplierAddress"));
            Supplier_Model supplier_model = new Supplier_Model(suppliercode, suppliergstin, suppliername, suppliernphone, supplieraddress);
            SupplierList.add(supplier_model);
        } // end of while
        if (SupplierList.size() >0)
        {
            if(SupplierAdapter == null)
            {
                SupplierAdapter = new SupplierAdapter(this,SupplierList,dbSupplierDetails , this.getClass().getName());
                lstSupplierDetails.setAdapter(SupplierAdapter);
            } else {
                SupplierAdapter.notifyNewDataAdded(SupplierList);
            }
        }

    }
    public void Clear()
    {
        suppliername_clicked="";
        suppliergstin_clicked="";
        btnAddSupplier.setEnabled(true);
        btnUpdateSupplier.setEnabled(false);
        tv_suppliercode.setText("-1");
        edt_supplierGSTIN.setText("");
        autocompletetv_suppliername.setText("");
        autocompletetv_supplierPhn.setText("");
        et_inw_supplierAddress.setText("");
        SupplierList = null;
        SupplierAdapter = null;
        lstSupplierDetails.setAdapter(SupplierAdapter);
    }
    public void Close() {
        dbSupplierDetails.CloseDatabase();
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);
            AuthorizationDialog
                    .setTitle("Are you sure you want to exit ?")
                    .setIcon(R.drawable.ic_launcher)
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dbSupplierDetails.CloseDatabase();
                            finish();
                        }
                    })
                    .show();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onHomePressed() {
        ActionBarUtils.navigateHome(this);    }
}
