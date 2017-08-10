package com.wepindia.pos.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.Database.Item;
import com.wep.common.app.models.ItemStock;
import com.wep.common.app.views.WepButton;
import com.wepindia.pos.FilePickerActivity;
import com.wepindia.pos.GenericClasses.EditTextInputHandler;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.R;
import com.wepindia.pos.UploadFilePickerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FragmentInwardSupply extends Fragment {

    public static final int RESULT_OK      = -1;

    Context myContext;
    DatabaseHandler dbInwardItem;
    public AlertDialog.Builder MsgBox;
    public MessageDialog MsgBox1;
    int ALL = 0;
    int SUPPLIERWISE = 1;
    int ITEMWISE = 2;
    int sema_display=0;


    String supplierphn1, supplieraddress1 , suppliername1, strBarcode1, mou1, ImageUri1, supplytype1;
    float rate1,  quantity1, SalesTax1, ServiceTax1;
    String itemname1;
    int suppliercode1;
    String businessDate="";

    WepButton btnAdd, btnEdit, btnUploadExcel, btnSaveExcel, btnCloseItem, btnClearItem, btnResetQuantity;
    //ImageView imgItemImage;
    TableLayout tblItems;
    //TextView tvFileName;

    Spinner spnrUOM,spnr_supplytype;
    EditText et_inw_supplierAddress,et_inw_ItemBarcode,et_inw_HSNCode;

    AutoCompleteTextView autocompletetv_supplierPhn, autocompletetv_suppliername,autocomplete_inw_ItemName;
    //WepButton btnAddSupplier;
    EditText et_inw_rate,et_inw_quantity,edt_supplierGSTIN;
    EditText et_Inw_SGSTRate, et_Inw_CGSTRate,et_Inw_IGSTRate;
    ArrayList<String> labelsSupplierName,labelsSupplierPhn;
    ArrayList<String> itemlist;
    TextView tv_suppliercode, tv_AverageRate,tv_count,et_Inw_Amount;//, spnrUOM_selection_code;
    int count =1;
    float rate_prev_for_touched_item =0;
    // Variables
    ArrayAdapter<String> adapDeptCode, adapCategCode, adapKitCode, adapTax, adapDiscount;
    String strMenuCode, AMU,strImageUri = "";
    SQLiteDatabase db;
    String strUploadFilepath = "", strUserName = "";


    public FragmentInwardSupply() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            dbInwardItem = new DatabaseHandler(getActivity());
            dbInwardItem.OpenDatabase();
        }catch (Exception e){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_inward__item__entry_non_gst, container, false);
//        Toolbar toolbar =(Toolbar) view.findViewById(R.id.toolbar);
//        toolbar.setVisibility(View.GONE);

        try {

            myContext = getActivity();

            MsgBox = new MessageDialog(myContext);
            MsgBox1 = new MessageDialog(myContext);

            dbInwardItem.CloseDatabase();
            dbInwardItem.CreateDatabase();
            dbInwardItem.OpenDatabase();

            Cursor cursor = dbInwardItem.getCurrentDate();
            if(cursor!=null && cursor.moveToFirst())
            {
                businessDate= cursor.getString(cursor.getColumnIndex("BusinessDate"));
            }
            //InitializeAdapters();
            sema_display=ALL;
            InitializeViewVariables(view);
            ResetItem();
            count =1;
            DisplayItems(-1); // display all data
            labelsSupplierName = new ArrayList<String>();
            loadSpinnerData();

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
                    Cursor supplierdetail_cursor = dbInwardItem.getSupplierDetails(suppliername_str);
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

                        suppliercode = supplierdetail_cursor.getInt(supplierdetail_cursor.getColumnIndex("SupplierCode"));
                        tv_suppliercode.setText(String.valueOf(suppliercode));
                        sema_display = SUPPLIERWISE;
                        loadAutoCompleteData_item(suppliercode);
                        ClearItemTable();
                        count =1;
                        DisplayItems(suppliercode,  suppliername_str,SupplierPhone,SupplierAddress);
                    }
                }
            });

            autocompletetv_supplierPhn.setOnTouchListener(new View.OnTouchListener(){
                //@Override
                public boolean onTouch(View v, MotionEvent event){
                    autocompletetv_supplierPhn.showDropDown();
                    return false;
                }
            });




            autocomplete_inw_ItemName.setOnTouchListener(new View.OnTouchListener(){
                //@Override
                public boolean onTouch(View v, MotionEvent event){
                    autocomplete_inw_ItemName.showDropDown();
                    return false;
                }
            });

            autocomplete_inw_ItemName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String suppliername = autocompletetv_suppliername.getText().toString();
                    String suppliercode_str = tv_suppliercode.getText().toString();
                    int suppliercode = 0;
                    String itemname = autocomplete_inw_ItemName.getText().toString();
                    if (suppliername.equals(""))
                    { // itemwise
                        sema_display = ITEMWISE;
                        ClearItemTable();
                        count =1;
                        DisplayItems(itemname);
                    }
                    else
                    {  //supplierwise
                        if(suppliercode_str== null || suppliercode_str.equals(""))
                            suppliercode = -1;
                        else
                            suppliercode = Integer.parseInt(suppliercode_str);
                        Cursor cursor = dbInwardItem.getItemdetailsforSupplier(suppliercode, itemname);
                        if (cursor !=null && cursor.moveToFirst())
                        {
                            int rate =0, quantity =0, amount =0, saletax =0, servicetax =0;
                            String rate_str = cursor.getString(cursor.getColumnIndex("Rate"));
                            String quantity_str = cursor.getString(cursor.getColumnIndex("Quantity"));
                            String salestax_str = cursor.getString(cursor.getColumnIndex("SalesTaxPercent"));
                            String servicetax_str = cursor.getString(cursor.getColumnIndex("ServiceTaxPercent"));
                            String uom = cursor.getString(cursor.getColumnIndex("UOM"));
                            strMenuCode = cursor.getString(cursor.getColumnIndex("MenuCode"));
                            String supplytype = cursor.getString(cursor.getColumnIndex("SupplyType"));

                            if (rate_str == null || rate_str.equals("") )
                                rate =0;
                            else
                                rate = Integer.parseInt(rate_str);

                            if (quantity_str == null ||  quantity_str.equals(""))
                                quantity=0;
                            else
                                quantity = Integer.parseInt(quantity_str);

                            amount = rate * quantity ;

                            if (salestax_str == null || salestax_str.equals(""))
                                saletax =0;
                            else
                                saletax = Integer.parseInt(salestax_str);

                            if (servicetax_str == null || servicetax_str.equals(""))
                                servicetax =0;
                            else
                                servicetax = Integer.parseInt(servicetax_str);

                            et_inw_quantity.setText(String.valueOf(quantity));
                            et_inw_rate.setText(String.valueOf(rate));
                            et_Inw_Amount.setText(String.valueOf(amount));
                            et_Inw_CGSTRate.setText(String.valueOf(saletax));
                            et_Inw_SGSTRate.setText(String.valueOf(servicetax));
                            if(supplytype.equalsIgnoreCase("G"))
                                spnr_supplytype.setSelection(0);
                            else
                                spnr_supplytype.setSelection(1);

                            spnrUOM.setSelection(getIndex(uom));
                            spnrUOM.setEnabled(false);
                            btnAdd.setEnabled(false);
                            btnResetQuantity.setEnabled(true);
                            btnEdit.setEnabled(true);

                        }
                    }

                }
            });

            et_inw_rate.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                public void afterTextChanged(Editable s) {
                    try{
                        double rate =0;
                        double quantity =0;
                        String quantity_str =et_inw_quantity.getText().toString();
                        String rate_str =et_inw_rate.getText().toString();
                        if (rate_str.equals(""))
                        {
                            rate =0;
                        }
                        else {
                            rate = Double.parseDouble(rate_str);
                        }
                        if (quantity_str.equals(""))
                        {
                            quantity =0;
                        }
                        else {
                            quantity = Double.parseDouble(quantity_str);
                        }

                        double amount = rate * quantity;
                        et_Inw_Amount.setText(String.valueOf(amount));
                    }
                    catch(Exception ex)
                    {
                        MsgBox1.Show("Error  ", ex.getMessage());
                    }

                }
            });
            et_inw_quantity.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                public void afterTextChanged(Editable s) {
                    try{
                        double rate =0;
                        double quantity =0;
                        String quantity_str =et_inw_quantity.getText().toString();
                        String rate_str =et_inw_rate.getText().toString();
                        if (rate_str.equals(""))
                        {
                            rate =0;
                        }
                        else {
                            rate = Double.parseDouble(rate_str);
                        }
                        if (quantity_str.equals(""))
                        {
                            quantity =0;
                        }
                        else {
                            quantity = Double.parseDouble(quantity_str);
                        }

                        double amount = rate * quantity;
                        et_Inw_Amount.setText(String.valueOf(amount));
                    }
                    catch(Exception e)
                    {
                        MsgBox1.Show("Error", e.getMessage());
                    }

                }
            });
            //loadSpinnerData1();
            /*btnUploadExcel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //startActivityForResult(new Intent(myContext, UploadFilePickerActivity.class), 1);
                    //tvFileName.setText(strUploadFilepath);
                    //startActivityForResult(new Intent(myContext, UploadFilePickerActivity.class), 1);
                    Intent intent = new Intent(myContext, FilePickerActivity.class);
                    intent.putExtra("contentType","csv");
                    startActivityForResult(intent, FilePickerActivity.FILE_PICKER_CODE);
                }
            });

            btnSaveExcel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    try {
                        AssetManager manager = myContext.getAssets();
                        //Environment.getExternalStorageDirectory() + "/stock1.xls";
                        //InputStream inputStream = getResources().openRawResource(R.raw.itemdb);
                        if (strUploadFilepath.equalsIgnoreCase("")) {
                            Toast.makeText(myContext, "No File Found", Toast.LENGTH_SHORT).show();
                        } else {
                            //tvFileName.setText(strUploadFilepath);
                            //String path = Environment.getExternalStorageDirectory() + "/itemdb.csv";
                            String path = strUploadFilepath;
                            FileInputStream inputStream = new FileInputStream(path);

                            BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
                            String line = "";
                            int iteration = 0;
                            while ((line = buffer.readLine()) != null) {
                                final String[] colums = line.split(",");
                                if (colums.length != 5) {
                                    Log.d("CSVParser", "Skipping Bad CSV Row");
                                    Toast.makeText(myContext, "Skipping Bad CSV Row", Toast.LENGTH_SHORT).show();
                                    continue;
                                }
                                if (iteration == 0) {
                                    iteration++;
                                    continue;
                                }

                                if (IsItemExists(colums[0].trim().toUpperCase())) {
                                    //MsgBox.Show("Warning", "Item already present");
                                    AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                                            .setTitle("Replace Item")
                                            .setMessage(colums[0].trim().toUpperCase() + "\nAre you sure you want to Replace this Item")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    Cursor crsrItem = dbInwardItem.getbyItemName(colums[0].trim().toUpperCase());
                                                    if (crsrItem.moveToFirst()) {
                                                        //MsgBox.Show("", colums[0].trim() + " - " + crsrItem.getString(0));
                                                        *//*int iRowId = dbInwardItem.updateItem_inward(Integer.parseInt(crsrItem.getString(0)),
                                                                0, colums[1].trim(), Integer.parseInt(crsrItem.getString(2)),colums[4].trim(),"",
                                                                        colums[5].trim(),0,0,0,0,0,0,"",0,Float.parseFloat(colums[6].trim()),
                                                                        colums[7].trim(),0,Float.parseFloat(colums[8].trim()),0,0,0,"",0,0,0,
                                                                        Float.parseFloat(colums[9].trim()),Float.parseFloat(colums[10].trim()),0);*//*
                                                        int iRowId =0;
                                                        Log.d("updateItem", "Updated Rows: " + String.valueOf(iRowId));




                                                        //Toast.makeText(getApplicationContext(), "Please wait... Importing Items...", Toast.LENGTH_LONG).show();
                                                    }
                                                    dialog.dismiss();
                                                    ClearItemTable();
                                                    count =1;
                                                    DisplayItems(-1);
                                                }
                                            })
                                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    ClearItemTable();
                                                    count =1;
                                                    DisplayItems(-1);
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                } else {
                                    *//*InsertItem(0, colums[1].trim(), Integer.parseInt(crsrItem.getString(2)),colums[4].trim(),"",
                                            colums[5].trim(),0,0,0,0,0,0,"",0,Float.parseFloat(colums[6].trim()),
                                            colums[7].trim(),0,Float.parseFloat(colums[8].trim()),0,0,0,"",0,0,0,
                                            Float.parseFloat(colums[9].trim()),Float.parseFloat(colums[10].trim()),0);*//*
                                    //Toast.makeText(getApplicationContext(), "Please wait... Importing Items...", Toast.LENGTH_LONG).show();
                                }
                            }
                            Toast.makeText(myContext, "Items Imported Successfully", Toast.LENGTH_LONG).show();
                            ClearItemTable();
                            count =1;
                            DisplayItems(-1);
                        }
                    } catch (Exception e) {
                        Toast.makeText(myContext, e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            });*/

            ClearItemTable();
            count =1;
            DisplayItems(-1);

            //tvFileName.setPaintFlags(tvFileName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

           /* spnrSalesTax.setSelection(0);
            spnrAdditionalTax.setSelection(1);*/
            /*spnrSalesTax.setEnabled(false);
            spnrAdditionalTax.setEnabled(false);*/

            et_Inw_SGSTRate.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                        String sgst_str = et_Inw_SGSTRate.getText().toString();
                        String cgst_str = et_Inw_CGSTRate.getText().toString();
                    float sgst_f =0, cgst_f =0, igst_f =0;
                    if(!(sgst_str== null || sgst_str.equals("")))
                        sgst_f = Float.parseFloat(sgst_str);
                    if(!(cgst_str== null || cgst_str.equals("")))
                        cgst_f = Float.parseFloat(cgst_str);
                    et_Inw_IGSTRate.setText(String.format("%.2f",sgst_f+cgst_f));
                }
            });
            et_Inw_CGSTRate.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String sgst_str = et_Inw_SGSTRate.getText().toString();
                    String cgst_str = et_Inw_CGSTRate.getText().toString();
                    float sgst_f =0, cgst_f =0, igst_f =0;
                    if(!(sgst_str== null || sgst_str.equals("")))
                        sgst_f = Float.parseFloat(sgst_str);
                    if(!(cgst_str== null || cgst_str.equals("")))
                        cgst_f = Float.parseFloat(cgst_str);
                    et_Inw_IGSTRate.setText(String.format("%.2f",sgst_f+cgst_f));
                }
            });

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            //dbInwardItem.CloseDatabase();
            return view;
        }

    }

    private void InitializeViewVariables(View view) {
        EditTextInputHandler etInputValidate = new EditTextInputHandler();
        //tvFileName = (TextView) view.findViewById(R.id.tvFileName);
        tblItems = (TableLayout) view.findViewById(R.id.tblItem);
//        imgItemImage = (ImageView) view.findViewById(R.id.imgItemImage);
//        imgItemImage.setImageResource(R.drawable.img_noimage);
        spnrUOM= (Spinner)view.findViewById(R.id.spnrUOM);
        spnr_supplytype = (Spinner)view.findViewById(R.id.spnr_supplytype);
        et_inw_rate = (EditText) view.findViewById(R.id.et_inw_rate);
        et_Inw_SGSTRate = (EditText) view.findViewById(R.id.et_Inw_SGSTRate);
        et_Inw_CGSTRate = (EditText) view.findViewById(R.id.et_Inw_CGSTRate);
        et_Inw_IGSTRate = (EditText) view.findViewById(R.id.et_Inw_IGSTRate);
        et_inw_quantity = (EditText) view.findViewById(R.id.et_inw_quantity);
        edt_supplierGSTIN = (EditText) view.findViewById(R.id.edt_supplierGSTIN);
        et_Inw_Amount = (TextView) view.findViewById(R.id.et_Inw_Amount);
        autocompletetv_suppliername = (AutoCompleteTextView) view.findViewById(R.id.autocompletetv_suppliername);
        autocomplete_inw_ItemName= (AutoCompleteTextView) view.findViewById(R.id.autocomplete_inw_ItemName);
        autocompletetv_supplierPhn = (AutoCompleteTextView) view.findViewById(R.id.autocompletetv_supplierPhn);
        et_inw_supplierAddress = (EditText) view.findViewById(R.id.et_inw_supplierAddress);
        et_inw_ItemBarcode = (EditText) view.findViewById(R.id.et_inw_ItemBarcode);
        et_inw_HSNCode = (EditText) view.findViewById(R.id.et_inw_HSNCode);
        tv_suppliercode  = (TextView)view.findViewById(R.id.tv_suppliercode);
        tv_AverageRate  = (TextView)view.findViewById(R.id.tv_AverageRate);
        tv_count  = (TextView)view.findViewById(R.id.tv_count);
        //  = (TextView)view.findViewById(R.id.spnrUOM_selection_code);
        //btnAddSupplier = (WepButton) view.findViewById (R.id.btnAddSupplier);
        btnAdd = (WepButton) view.findViewById(R.id.btnAddItem);
        btnEdit = (WepButton) view.findViewById(R.id.btnEditItem);
        btnResetQuantity = (WepButton) view.findViewById(R.id.btnResetQuantity);
        btnClearItem = (WepButton) view.findViewById(R.id.btnClearItem);
        btnCloseItem = (WepButton) view.findViewById(R.id.btnCloseItem);
        btnUploadExcel = (WepButton) view.findViewById(R.id.buttonUploadExcel);
        btnSaveExcel = (WepButton) view.findViewById(R.id.buttonSaveExcel);
       /* btnAddSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Test", "onClickListener is getting started");
                AddSupplier(v);
            }
        });*/

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              AddItem1(v);
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditItem1(v);
            }
        });
        btnResetQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetQuantity(v);
            }
        });
        btnClearItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearItem1(v);
            }
        });
        btnCloseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseItem1(v);
            }
        });

    }

    /*@Override
    public void onResume() {
        super.onResume();
        ClearItem1(null);
    }*/

    /*@Override
    public void onPause() {
        super.onPause();
        ClearItem1(null);

    }
*/
    /* @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        InitializeViewVariables(onCreateView());
        ResetItem();
        DisplayItems(-1);
    }
*/

    @SuppressWarnings("deprecation")
    private void  DisplayItems(int suppliercode_recv ) {
        // if suppliercode_recv == -1 , then display all whole data , i.e. all items for all the suppliers
        // else display for that suppliercode
        Cursor crsrItems = null;


        Cursor supplier_crsr = dbInwardItem.getAllSupplierName_nonGST();
        while (supplier_crsr!=null && supplier_crsr.moveToNext())
        {

            String supplierName = supplier_crsr.getString(supplier_crsr.getColumnIndex("SupplierName"));
            String supplierPhone = supplier_crsr.getString(supplier_crsr.getColumnIndex("SupplierPhone"));
            String supplierAddress = supplier_crsr.getString(supplier_crsr.getColumnIndex("SupplierAddress"));
            int suppliercode = supplier_crsr.getInt(supplier_crsr.getColumnIndex("SupplierCode"));
            if (suppliercode == suppliercode_recv)
            {
                continue;
            }
            crsrItems = dbInwardItem.getLinkedMenuCodeForSupplier(suppliercode);
            TableRow rowItems = null;

            TextView tvSno, tvMenuCode, tvHSN, tvLongName, tvShortName, tvDineIn1, tvDineIn2, tvDineIn3, tvTakeAway, tvPickUp, tvDelivery,
                    tvStock, tvPriceChange, tvDiscountEnable, tvBillWithStock, tvTaxType, tvDeptCode, tvCategCode,
                    tvKitchenCode, tvSalesTaxId, tvServicetax, tvAdditionalTaxId, tvOptionalTaxId1, tvOptionalTaxId2, tvDiscountId,
                    tvItemBarcode, tvImageUri, tvSpace, tvDeptName, tvCategName, tvHSNCode;
            TextView tvSupplierType, tvSuppliergstin, tvSupplierName, tvSupplierPhone, tvSupplierAddress,tvSupplyType,tvTaxationType;

            TextView tvQuantity, tvRate, tvAMU, tvSalesTax, tvOtherTax;
            ImageView imgIcon;
            ImageButton btnItemDelete;

            if (crsrItems.moveToFirst()) {
                do {

                    rowItems = new TableRow(myContext);
                    rowItems.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    //rowItems.setBackgroundResource(R.drawable.row_background);
                    rowItems.setBackgroundResource(R.drawable.border_itemdatabase);

                    tvSno = new TextView(myContext);
                    tvSno.setTextSize(18);
                    tvSno.setGravity(1);
                    tvSno.setWidth(90);
                    tvSno.setText(String.valueOf(count));
                    rowItems.addView(tvSno);
                    count++;


                    tvSupplierName = new TextView(myContext);
                    tvSupplierName.setTextSize(18);
                    tvSupplierName.setWidth(300);
                    tvSupplierName.setText(supplierName);
                    rowItems.addView(tvSupplierName);


                    tvSupplierPhone =  new TextView(myContext);
                    tvSupplierPhone.setTextSize(18);
                    tvSupplierPhone.setWidth(300);
                    tvSupplierPhone.setText(supplierPhone);
                    rowItems.addView(tvSupplierPhone);

                    tvSupplierAddress = new TextView(myContext);
                    tvSupplierAddress.setTextSize(18);
                    tvSupplierAddress.setWidth(300);
                    tvSupplierAddress.setText(supplierAddress);
                    rowItems.addView(tvSupplierAddress);

                    tvMenuCode = new TextView(myContext);
                    tvMenuCode.setTextSize(18);
                    tvMenuCode.setWidth(300);
                    tvMenuCode.setText(crsrItems.getString(crsrItems.getColumnIndex("MenuCode")));
                    rowItems.addView(tvMenuCode);

                    tvLongName = new TextView(myContext);
                    tvLongName.setTextSize(18);
                    tvLongName.setWidth(330);
                    tvLongName.setText(crsrItems.getString(crsrItems.getColumnIndex("ItemName")).toUpperCase());
                    rowItems.addView(tvLongName);

                    tvSupplyType = new TextView(myContext);
                    tvSupplyType.setTextSize(18);
                    tvSupplyType.setWidth(300);
                    tvSupplyType.setText(crsrItems.getString(crsrItems.getColumnIndex("SupplyType")));
                    rowItems.addView(tvSupplyType);


                    tvRate = new TextView(myContext);
                    tvRate.setGravity(Gravity.RIGHT|Gravity.END);
                    tvRate.setWidth(55);
                    tvRate.setTextSize(18);
                    tvRate.setText(crsrItems.getString(crsrItems.getColumnIndex("Rate")));
                    rowItems.addView(tvRate);

                    tvQuantity = new TextView(myContext);
                    tvQuantity.setGravity(Gravity.RIGHT|Gravity.END);
                    tvQuantity.setWidth(130);
                    tvQuantity.setTextSize(18);
                    tvQuantity.setVisibility(View.INVISIBLE);
                    tvQuantity.setText(crsrItems.getString(crsrItems.getColumnIndex("Quantity")));
                    rowItems.addView(tvQuantity);

                    tvAMU = new TextView(myContext);
                    tvAMU.setGravity(Gravity.RIGHT);
                    tvAMU.setWidth(140);
                    tvAMU.setTextSize(18);
                    tvAMU.setPadding(0,0,0,15);
                    tvAMU.setText(crsrItems.getString(crsrItems.getColumnIndex("UOM")));
                    rowItems.addView(tvAMU);

                    tvImageUri = new TextView(myContext);
                    tvImageUri.setText(crsrItems.getString(crsrItems.getColumnIndex("ImageUri")));
                    tvImageUri.setGravity(2);
                    tvImageUri.setWidth(100);
                    rowItems.addView(tvImageUri);


                    imgIcon = new ImageView(myContext);
                    imgIcon.setMinimumWidth(100);
                    imgIcon.setLayoutParams(new TableRow.LayoutParams(50, 40));
                    imgIcon.setImageURI(null);



                    if (!crsrItems.getString(crsrItems.getColumnIndex("ImageUri")).equalsIgnoreCase("")) { // &&
                        // strImageUri.contains("\\")){
                        imgIcon.setImageURI(Uri.fromFile(new File(crsrItems.getString(crsrItems.getColumnIndex("ImageUri")))));
                    } else {
                        imgIcon.setImageResource(R.drawable.img_noimage);
                    }
                    rowItems.addView(imgIcon);

                    tvSalesTax = new TextView(myContext);
                    tvSalesTax.setText(crsrItems.getString(crsrItems.getColumnIndex("CGSTRate")));
                    rowItems.addView(tvSalesTax);

                    tvOtherTax = new TextView(myContext);
                    tvOtherTax.setText(crsrItems.getString(crsrItems.getColumnIndex("SGSTRate")));
                    rowItems.addView(tvOtherTax);



                    // For Space purpose
                    tvSpace = new TextView(myContext);
                    tvSpace.setText("                                 ");
                    rowItems.addView(tvSpace);

                    // Delete
                    int res = getResources().getIdentifier("delete", "drawable", getContext().getPackageName());
                    btnItemDelete = new ImageButton(myContext);
                    btnItemDelete.setImageResource(res);
                    btnItemDelete.setLayoutParams(new TableRow.LayoutParams(60, 45));
                    btnItemDelete.setOnClickListener(mListener);
                    //rowItems.addView(btnItemDelete);

/*
                    Button btndel = new Button(myContext);
                    btndel.setBackground(getResources().getDrawable(R.drawable.deletered1));
                    btndel.setPadding(5,0,0,0);*/
                    // btndel.setLayoutParams(new TableRow.LayoutParams(30, 20));

                    Button btndel = new Button(myContext);
                    btndel.setBackground(getResources().getDrawable(R.drawable.delete_icon_border));
                    btndel.setLayoutParams(new TableRow.LayoutParams(40, 35));
                    btndel.setOnClickListener(mListener);
                    rowItems.addView(btndel);

                    TextView SupplierCode =  new TextView(myContext);
                    SupplierCode.setText(String.valueOf(suppliercode));
                    rowItems.addView(SupplierCode);

                    TextView AverageRate_i =  new TextView(myContext);
                    AverageRate_i.setText(String.format("%.2f",crsrItems.getFloat(crsrItems.getColumnIndex("AverageRate"))));
                    rowItems.addView(AverageRate_i);

                    TextView Count_i =  new TextView(myContext);
                    Count_i.setText(crsrItems.getString(crsrItems.getColumnIndex("Count")));
                    rowItems.addView(Count_i);

                    tvHSN = new TextView(myContext);
                    tvHSN.setText(crsrItems.getString(crsrItems.getColumnIndex("HSNCode")));
                    rowItems.addView(tvHSN);
                    rowItems.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub

                            if (String.valueOf(v.getTag()) == "TAG") {
                                TableRow Row = (TableRow) v;

                                //TextView MenuCode = (TextView) Row.getChildAt(1);
                                TextView Suppliername  = (TextView) Row.getChildAt(1);
                                TextView Supplierphn = (TextView) Row.getChildAt(2);
                                TextView SupplierAddr = (TextView) Row.getChildAt(3);
                                TextView MenuCode = (TextView) Row.getChildAt(4);
                                TextView LongName = (TextView) Row.getChildAt(5);
                                TextView SupplyType = (TextView) Row.getChildAt(6);
                                TextView Rate = (TextView) Row.getChildAt(7);
                                TextView Quantity = (TextView) Row.getChildAt(8);
                                TextView Uom = (TextView) Row.getChildAt(9);
                                TextView SalesTax = (TextView) Row.getChildAt(12);
                                TextView OtherTax = (TextView) Row.getChildAt(13);
                                TextView SupplierCode = (TextView) Row.getChildAt(16);
                                TextView AverageRate = (TextView) Row.getChildAt(17);
                                TextView Count = (TextView) Row.getChildAt(18);
                                TextView hsnCode = (TextView) Row.getChildAt(19);

                                tv_AverageRate.setText(AverageRate.getText().toString());
                                tv_count.setText(Count.getText().toString());

                                strMenuCode = MenuCode.getText().toString();
                                autocompletetv_suppliername.setFocusable(false);
                                autocompletetv_suppliername.setFocusableInTouchMode(false);
                                autocompletetv_suppliername.setText(Suppliername.getText().toString());
                                autocompletetv_suppliername.setFocusable(true);
                                autocompletetv_suppliername.setFocusableInTouchMode(true);


                                /*autocompletetv_suppliername.setText(Suppliername.getText().toString());*/
                                autocompletetv_supplierPhn.setText(Supplierphn.getText().toString());
                                et_inw_supplierAddress.setText(SupplierAddr.getText().toString());
                                tv_suppliercode.setText(SupplierCode.getText().toString());
                                String gstin_sup = dbInwardItem.getSupplierGSTIN((SupplierCode.getText().toString()));
                                edt_supplierGSTIN.setText(gstin_sup);
                                String supplyType = SupplyType.getText().toString();
                                if (supplyType.equalsIgnoreCase("g"))
                                    spnr_supplytype.setSelection(0);
                                else
                                    spnr_supplytype.setSelection(1);

                                autocomplete_inw_ItemName.setFocusable(false);
                                autocomplete_inw_ItemName.setFocusableInTouchMode(false);
                                autocomplete_inw_ItemName.setText(LongName.getText().toString());
                                autocomplete_inw_ItemName.setFocusable(true);
                                autocomplete_inw_ItemName.setFocusableInTouchMode(true);

                                et_inw_HSNCode.setText(hsnCode.getText().toString());
                                //autocomplete_inw_ItemName.setText(LongName.getText());
                                et_inw_rate.setText(Rate.getText().toString());
                                rate_prev_for_touched_item = Float.parseFloat(et_inw_rate.getText().toString());
                                et_inw_quantity.setText(Quantity.getText().toString());
                                double rate = Double.parseDouble(et_inw_rate.getText().toString());
                                double quantity = Double.parseDouble(et_inw_quantity.getText().toString());
                                double amount = rate * quantity;
                                et_Inw_Amount.setText(String.valueOf(amount));

                                String uom_temp = Uom.getText().toString();
                                String uom = "("+uom_temp+")";
                                int index = getIndex(uom);
                                spnrUOM.setSelection(index);
                                spnrUOM.setEnabled(false);
                               // spnrUOM_selection_code.setText(index);

                                et_Inw_CGSTRate.setText(SalesTax.getText());
                                et_Inw_SGSTRate.setText(OtherTax.getText());
                                double cgstrate = Double.parseDouble(SalesTax.getText().toString());
                                double sgstrate = Double.parseDouble(OtherTax.getText().toString());
                                et_Inw_IGSTRate.setText(String.format("%.2f",cgstrate+sgstrate));



//                                imgItemImage.setImageURI(null);
//                                if (!strImageUri.equalsIgnoreCase("")) { // &&
//                                    // strImageUri.contains("\\")){
//                                    imgItemImage.setImageURI(Uri.fromFile(new File(strImageUri)));
//                                } else {
//                                    imgItemImage.setImageResource(R.drawable.img_noimage);
//                                }
                                btnAdd.setEnabled(false);
                                btnEdit.setEnabled(true);
                                btnResetQuantity.setEnabled(true);

                            }
                        }
                    });

                    rowItems.setTag("TAG");

                    tblItems.addView(rowItems, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                } while (crsrItems.moveToNext());
            } else {
                Log.d("DisplayItem", "No Item found");
            }
        }
    }

    private void DisplayItems(String itemname ) {
        Cursor crsrItems = null;
        try {


            crsrItems = dbInwardItem.getItemDetail_inward(itemname);
            while (crsrItems != null && crsrItems.moveToNext()) {

                int suppliercode = crsrItems.getInt(crsrItems.getColumnIndex("SupplierCode"));
                Cursor cursor_supplier = dbInwardItem.getSupplierDetails_forcode(suppliercode);
                if (cursor_supplier == null || !cursor_supplier.moveToFirst())
                {
                    return;
                }
                String supplierName = cursor_supplier.getString(cursor_supplier.getColumnIndex("SupplierName"));
                String supplierPhone = cursor_supplier.getString(cursor_supplier.getColumnIndex("SupplierPhone"));
                String supplierAddress = cursor_supplier.getString(cursor_supplier.getColumnIndex("SupplierAddress"));



                TableRow rowItems = null;

                TextView tvSno, tvMenuCode, tvHSN, tvLongName, tvShortName, tvDineIn1, tvDineIn2, tvDineIn3, tvTakeAway, tvPickUp, tvDelivery,
                        tvStock, tvPriceChange, tvDiscountEnable, tvBillWithStock, tvTaxType, tvDeptCode, tvCategCode,
                        tvKitchenCode, tvSalesTaxId, tvServicetax, tvAdditionalTaxId, tvOptionalTaxId1, tvOptionalTaxId2, tvDiscountId,
                        tvItemBarcode, tvImageUri, tvSpace, tvDeptName, tvCategName;
                TextView tvSupplierType, tvSuppliergstin, tvSupplierName, tvSupplierPhone, tvSupplierAddress, tvSupplyType, tvTaxationType;

                TextView tvQuantity, tvRate, tvAMU, tvSalesTax, tvOtherTax;
                ImageView imgIcon;
                ImageButton btnItemDelete;
                int i = 1;

                rowItems = new TableRow(myContext);
                rowItems.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                //rowItems.setBackgroundResource(R.drawable.row_background);
                rowItems.setBackgroundResource(R.drawable.border);

                tvSno = new TextView(myContext);
                tvSno.setTextSize(18);
                tvSno.setGravity(1);
                tvSno.setWidth(90);
                tvSno.setText(String.valueOf(count));
                rowItems.addView(tvSno);
                count++;


                tvSupplierName = new TextView(myContext);
                tvSupplierName.setTextSize(18);
                tvSupplierName.setWidth(300);
                tvSupplierName.setText(supplierName);
                rowItems.addView(tvSupplierName);


                tvSupplierPhone = new TextView(myContext);
                tvSupplierPhone.setTextSize(18);
                tvSupplierPhone.setWidth(300);
                tvSupplierPhone.setText(supplierPhone);
                rowItems.addView(tvSupplierPhone);

                tvSupplierAddress = new TextView(myContext);
                tvSupplierAddress.setTextSize(18);
                tvSupplierAddress.setWidth(300);
                tvSupplierAddress.setText(supplierAddress);
                rowItems.addView(tvSupplierAddress);

                tvMenuCode = new TextView(myContext);
                tvMenuCode.setTextSize(18);
                tvMenuCode.setWidth(300);
                tvMenuCode.setText(crsrItems.getString(crsrItems.getColumnIndex("MenuCode")));
                rowItems.addView(tvMenuCode);

                tvLongName = new TextView(myContext);
                tvLongName.setTextSize(18);
                tvLongName.setWidth(330);
                tvLongName.setText(crsrItems.getString(crsrItems.getColumnIndex("ItemName")).toUpperCase());
                rowItems.addView(tvLongName);

                tvSupplyType = new TextView(myContext);
                tvSupplyType.setTextSize(18);
                tvSupplyType.setWidth(300);
                tvSupplyType.setText(crsrItems.getString(crsrItems.getColumnIndex("SupplyType")));
                rowItems.addView(tvSupplyType);


                tvRate = new TextView(myContext);
                tvRate.setGravity(Gravity.RIGHT | Gravity.END);
                tvRate.setWidth(55);
                tvRate.setTextSize(18);
                tvRate.setText(crsrItems.getString(crsrItems.getColumnIndex("Rate")));
                rowItems.addView(tvRate);

                tvQuantity = new TextView(myContext);
                tvQuantity.setGravity(Gravity.RIGHT | Gravity.END);
                tvQuantity.setWidth(130);
                tvQuantity.setTextSize(18);
                tvQuantity.setVisibility(View.INVISIBLE);
                tvQuantity.setText(crsrItems.getString(crsrItems.getColumnIndex("Quantity")));
                rowItems.addView(tvQuantity);

                tvAMU = new TextView(myContext);
                tvAMU.setGravity(Gravity.RIGHT);
                tvAMU.setWidth(140);
                tvAMU.setTextSize(18);
                tvAMU.setPadding(0,0,0,5);
                tvAMU.setText(crsrItems.getString(crsrItems.getColumnIndex("UOM")));
                rowItems.addView(tvAMU);

                tvImageUri = new TextView(myContext);
                tvImageUri.setText(crsrItems.getString(crsrItems.getColumnIndex("ImageUri")));
                tvImageUri.setGravity(2);
                tvImageUri.setWidth(100);
                rowItems.addView(tvImageUri);


                imgIcon = new ImageView(myContext);
                imgIcon.setMinimumWidth(100);
                imgIcon.setLayoutParams(new TableRow.LayoutParams(50, 40));
                imgIcon.setImageURI(null);


                if (!crsrItems.getString(crsrItems.getColumnIndex("ImageUri")).equalsIgnoreCase("")) { // &&
                    // strImageUri.contains("\\")){
                    imgIcon.setImageURI(Uri.fromFile(new File(crsrItems.getString(crsrItems.getColumnIndex("ImageUri")))));
                } else {
                    imgIcon.setImageResource(R.drawable.img_noimage);
                }
                rowItems.addView(imgIcon);

                tvSalesTax = new TextView(myContext);
                tvSalesTax.setText(crsrItems.getString(crsrItems.getColumnIndex("SalesTaxPercent")));
                rowItems.addView(tvSalesTax);

                tvOtherTax = new TextView(myContext);
                tvOtherTax.setText(crsrItems.getString(crsrItems.getColumnIndex("ServiceTaxPercent")));
                rowItems.addView(tvOtherTax);


                // For Space purpose
                tvSpace = new TextView(myContext);
                tvSpace.setText("                               ");
                rowItems.addView(tvSpace);

                // Delete
                int res = getResources().getIdentifier("delete", "drawable", getContext().getPackageName());
                btnItemDelete = new ImageButton(myContext);
                btnItemDelete.setImageResource(res);
                btnItemDelete.setLayoutParams(new TableRow.LayoutParams(60, 45));
                btnItemDelete.setOnClickListener(mListener);
                //rowItems.addView(btnItemDelete);
               /* Button btndel = new Button(myContext);
                btndel.setBackground(getResources().getDrawable(R.drawable.deletered1));
                btndel.setPadding(5,0,0,0);
                btndel.setLayoutParams(new TableRow.LayoutParams(30, 20));*/

                Button btndel = new Button(myContext);
                btndel.setBackground(getResources().getDrawable(R.drawable.delete_icon_lightyellow));
                btndel.setLayoutParams(new TableRow.LayoutParams(40, 35));
                btndel.setOnClickListener(mListener);
                rowItems.addView(btndel);


                TextView SupplierCode =  new TextView(myContext);
                SupplierCode.setText(String.valueOf(suppliercode));
                rowItems.addView(SupplierCode);


                tvHSN = new TextView(myContext);
                tvHSN.setText(crsrItems.getString(crsrItems.getColumnIndex("HSNCode")));
                rowItems.addView(tvHSN);

                rowItems.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        if (String.valueOf(v.getTag()) == "TAG") {
                            TableRow Row = (TableRow) v;

                            //TextView MenuCode = (TextView) Row.getChildAt(1);
                            TextView Suppliername = (TextView) Row.getChildAt(1);
                            TextView Supplierphn = (TextView) Row.getChildAt(2);
                            TextView SupplierAddr = (TextView) Row.getChildAt(3);
                            TextView MenuCode = (TextView) Row.getChildAt(4);
                            TextView LongName = (TextView) Row.getChildAt(5);
                            TextView SupplyType = (TextView) Row.getChildAt(6);
                            TextView Rate = (TextView) Row.getChildAt(7);
                            TextView Quantity = (TextView) Row.getChildAt(8);
                            TextView Uom = (TextView) Row.getChildAt(9);
                            TextView SalesTax = (TextView) Row.getChildAt(12);
                            TextView OtherTax = (TextView) Row.getChildAt(13);
                            TextView SupplierCode = (TextView) Row.getChildAt(16);
                            TextView HSN = (TextView) Row.getChildAt(17);


                            et_inw_HSNCode.setText(HSN.getText().toString());

                            strMenuCode = MenuCode.getText().toString();
                            autocompletetv_suppliername.setFocusable(false);
                            autocompletetv_suppliername.setFocusableInTouchMode(false);
                            autocompletetv_suppliername.setText(Suppliername.getText().toString());
                            autocompletetv_suppliername.setFocusable(true);
                            autocompletetv_suppliername.setFocusableInTouchMode(true);



                            /*autocompletetv_suppliername.setText(Suppliername.getText().toString());*/
                            autocompletetv_supplierPhn.setText(Supplierphn.getText().toString());
                            et_inw_supplierAddress.setText(SupplierAddr.getText().toString());
                            tv_suppliercode.setText(SupplierCode.getText().toString());
                            String supplyType = SupplyType.getText().toString();
                            if (supplyType.equalsIgnoreCase("g"))
                                spnr_supplytype.setSelection(0);
                            else
                                spnr_supplytype.setSelection(1);

                            autocomplete_inw_ItemName.setFocusable(false);
                            autocomplete_inw_ItemName.setFocusableInTouchMode(false);
                            autocomplete_inw_ItemName.setText(LongName.getText().toString());
                            autocomplete_inw_ItemName.setFocusable(true);
                            autocomplete_inw_ItemName.setFocusableInTouchMode(true);

                            //autocomplete_inw_ItemName.setText(LongName.getText());
                            et_inw_rate.setText(Rate.getText().toString());
                            et_inw_quantity.setText(Quantity.getText().toString());
                            double rate = Double.parseDouble(et_inw_rate.getText().toString());
                            double quantity = Double.parseDouble(et_inw_quantity.getText().toString());
                            rate_prev_for_touched_item = Float.parseFloat(et_inw_rate.getText().toString());
                            double amount = rate * quantity;
                            et_Inw_Amount.setText(String.valueOf(amount));

                            String uom_temp = Uom.getText().toString();
                            String uom = "(" + uom_temp + ")";
                            int index = getIndex(uom);
                            spnrUOM.setSelection(index);
                            spnrUOM.setEnabled(false);
                            //spnrUOM_selection_code.setText(index);

                            et_Inw_CGSTRate.setText(SalesTax.getText());
                            et_Inw_SGSTRate.setText(OtherTax.getText());


//                            imgItemImage.setImageURI(null);
//                            if (!strImageUri.equalsIgnoreCase("")) { // &&
//                                // strImageUri.contains("\\")){
//                                imgItemImage.setImageURI(Uri.fromFile(new File(strImageUri)));
//                            } else {
//                                imgItemImage.setImageResource(R.drawable.img_noimage);
//                            }
                            btnAdd.setEnabled(false);
                            btnEdit.setEnabled(true);
                            btnResetQuantity.setEnabled(true);

                        }
                    }
                });

                rowItems.setTag("TAG");

                tblItems.addView(rowItems, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            }
        }
        catch (Exception e)
        {
            MsgBox1.Show(" Error", e.getMessage());
            e.printStackTrace();
        }
        //DisplayItems(-1);
    }


    private void DisplayItems(int suppliercode , String suppliername_str, String SupplierPhone, final String SupplierAddress) {
        Cursor crsrItems = null;
        crsrItems = dbInwardItem.getLinkedMenuCodeForSupplier(suppliercode);
        int sn =1;
        if (crsrItems!=null && crsrItems.moveToFirst())
        {

            TableRow rowItems = null;

            TextView tvSno, tvMenuCode, tvHSN, tvLongName, tvShortName, tvDineIn1, tvDineIn2, tvDineIn3, tvTakeAway, tvPickUp, tvDelivery,
                    tvStock, tvPriceChange, tvDiscountEnable, tvBillWithStock, tvTaxType, tvDeptCode, tvCategCode,
                    tvKitchenCode, tvSalesTaxId, tvServicetax, tvAdditionalTaxId, tvOptionalTaxId1, tvOptionalTaxId2, tvDiscountId,
                    tvItemBarcode, tvImageUri, tvSpace, tvDeptName, tvCategName;
            TextView tvSupplierType, tvSuppliergstin, tvSupplierName, tvSupplierPhone, tvSupplierAddress,tvSupplyType,tvTaxationType;

            TextView tvQuantity, tvRate, tvAMU, tvSalesTax, tvOtherTax;
            ImageView imgIcon;
            ImageButton btnItemDelete;

            do {

                rowItems = new TableRow(myContext);
                rowItems.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                //rowItems.setBackgroundResource(R.drawable.row_background);
                rowItems.setBackgroundResource(R.drawable.border);

                tvSno = new TextView(myContext);
                tvSno.setTextSize(18);
                tvSno.setGravity(1);
                tvSno.setWidth(90);
                tvSno.setText(String.valueOf(count));
                rowItems.addView(tvSno);
                count++;


                tvSupplierName = new TextView(myContext);
                tvSupplierName.setTextSize(18);
                tvSupplierName.setWidth(300);
                tvSupplierName.setText(suppliername_str);
                rowItems.addView(tvSupplierName);


                tvSupplierPhone =  new TextView(myContext);
                tvSupplierPhone.setTextSize(18);
                tvSupplierPhone.setWidth(300);
                tvSupplierPhone.setText(SupplierPhone);
                rowItems.addView(tvSupplierPhone);

                tvSupplierAddress = new TextView(myContext);
                tvSupplierAddress.setTextSize(18);
                tvSupplierAddress.setWidth(300);
                tvSupplierAddress.setText(SupplierAddress);
                rowItems.addView(tvSupplierAddress);

                tvMenuCode = new TextView(myContext);
                tvMenuCode.setTextSize(18);
                tvMenuCode.setWidth(300);
                tvMenuCode.setText(crsrItems.getString(crsrItems.getColumnIndex("MenuCode")));
                rowItems.addView(tvMenuCode);

                tvLongName = new TextView(myContext);
                tvLongName.setTextSize(18);
                tvLongName.setWidth(330);
                tvLongName.setText(crsrItems.getString(crsrItems.getColumnIndex("ItemName")).toUpperCase());
                rowItems.addView(tvLongName);

                tvSupplyType = new TextView(myContext);
                tvSupplyType.setTextSize(18);
                tvSupplyType.setWidth(300);
                tvSupplyType.setText(crsrItems.getString(crsrItems.getColumnIndex("SupplyType")));
                rowItems.addView(tvSupplyType);


                tvRate = new TextView(myContext);
                tvRate.setGravity(Gravity.RIGHT|Gravity.END);
                tvRate.setWidth(55);
                tvRate.setTextSize(18);
                tvRate.setText(crsrItems.getString(crsrItems.getColumnIndex("Rate")));
                rowItems.addView(tvRate);

                tvQuantity = new TextView(myContext);
                tvQuantity.setGravity(Gravity.RIGHT|Gravity.END);
                tvQuantity.setWidth(130);
                tvQuantity.setTextSize(18);
                tvQuantity.setVisibility(View.INVISIBLE);
                tvQuantity.setText(crsrItems.getString(crsrItems.getColumnIndex("Quantity")));
                rowItems.addView(tvQuantity);

                tvAMU = new TextView(myContext);
                tvAMU.setGravity(Gravity.RIGHT);
                tvAMU.setWidth(140);
                tvAMU.setTextSize(18);
                tvAMU.setPadding(0,0,0,5);
                tvAMU.setText(crsrItems.getString(crsrItems.getColumnIndex("UOM")));
                rowItems.addView(tvAMU);

                tvImageUri = new TextView(myContext);
                tvImageUri.setText(crsrItems.getString(crsrItems.getColumnIndex("ImageUri")));
                tvImageUri.setGravity(2);
                tvImageUri.setWidth(100);
                rowItems.addView(tvImageUri);


                imgIcon = new ImageView(myContext);
                imgIcon.setMinimumWidth(100);
                imgIcon.setLayoutParams(new TableRow.LayoutParams(50, 40));
                imgIcon.setImageURI(null);



                if (!crsrItems.getString(crsrItems.getColumnIndex("ImageUri")).equalsIgnoreCase("")) { // &&
                    // strImageUri.contains("\\")){
                    imgIcon.setImageURI(Uri.fromFile(new File(crsrItems.getString(crsrItems.getColumnIndex("ImageUri")))));
                } else {
                    imgIcon.setImageResource(R.drawable.img_noimage);
                }
                rowItems.addView(imgIcon);

                tvSalesTax = new TextView(myContext);
                tvSalesTax.setText(crsrItems.getString(crsrItems.getColumnIndex("SalesTaxPercent")));
                rowItems.addView(tvSalesTax);

                tvOtherTax = new TextView(myContext);
                tvOtherTax.setText(crsrItems.getString(crsrItems.getColumnIndex("ServiceTaxPercent")));
                rowItems.addView(tvOtherTax);



                // For Space purpose
                tvSpace = new TextView(myContext);
                tvSpace.setText("                               ");
                rowItems.addView(tvSpace);

                // Delete
                int res = getResources().getIdentifier("delete", "drawable", getContext().getPackageName());
                btnItemDelete = new ImageButton(myContext);
                btnItemDelete.setImageResource(res);
                btnItemDelete.setLayoutParams(new TableRow.LayoutParams(60, 45));
                btnItemDelete.setOnClickListener(mListener);
                //rowItems.addView(btnItemDelete);
                /*Button btndel = new Button(myContext);
                btndel.setBackground(getResources().getDrawable(R.drawable.deletered1));
                btndel.setPadding(5,0,0,0);
                btndel.setLayoutParams(new TableRow.LayoutParams(30, 20));*/

                Button btndel = new Button(myContext);
                btndel.setBackground(getResources().getDrawable(R.drawable.delete_icon_lightyellow));
                btndel.setLayoutParams(new TableRow.LayoutParams(40, 35));
                btndel.setOnClickListener(mListener);
                rowItems.addView(btndel);


                TextView Suppliercode = new TextView(myContext);
                Suppliercode.setText(String.valueOf(suppliercode));
                rowItems.addView(Suppliercode);

                tvHSN = new TextView(myContext);
                tvHSN.setText(crsrItems.getString(crsrItems.getColumnIndex("HSNCode")));
                rowItems.addView(tvHSN);

                rowItems.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        if (String.valueOf(v.getTag()) == "TAG") {
                            TableRow Row = (TableRow) v;

                            //TextView MenuCode = (TextView) Row.getChildAt(1);
                            TextView Suppliername  = (TextView) Row.getChildAt(1);
                            TextView Supplierphn = (TextView) Row.getChildAt(2);
                            TextView SupplierAddr = (TextView) Row.getChildAt(3);
                            TextView MenuCode = (TextView) Row.getChildAt(4);
                            TextView LongName = (TextView) Row.getChildAt(5);
                            TextView SupplyType = (TextView) Row.getChildAt(6);
                            TextView Rate = (TextView) Row.getChildAt(7);
                            TextView Quantity = (TextView) Row.getChildAt(8);
                            TextView Uom = (TextView) Row.getChildAt(9);
                            TextView SalesTax = (TextView) Row.getChildAt(12);
                            TextView OtherTax = (TextView) Row.getChildAt(13);
                            TextView SupplierCode = (TextView) Row.getChildAt(16);
                            TextView HSN = (TextView) Row.getChildAt(17);


                            et_inw_HSNCode.setText(HSN.getText().toString());

                            strMenuCode = MenuCode.getText().toString();
                            autocompletetv_suppliername.setFocusable(false);
                            autocompletetv_suppliername.setFocusableInTouchMode(false);
                            autocompletetv_suppliername.setText(Suppliername.getText().toString());
                            autocompletetv_suppliername.setFocusable(true);
                            autocompletetv_suppliername.setFocusableInTouchMode(true);


                            //autocompletetv_suppliername.setText(Suppliername.getText().toString());
                            autocompletetv_supplierPhn.setText(Supplierphn.getText().toString());
                            et_inw_supplierAddress.setText(SupplierAddr.getText().toString());
                            tv_suppliercode.setText(SupplierCode.getText().toString());
                            String supplyType = SupplyType.getText().toString();
                            if (supplyType.equalsIgnoreCase("g"))
                                spnr_supplytype.setSelection(0);
                            else
                                spnr_supplytype.setSelection(1);

                            autocomplete_inw_ItemName.setFocusable(false);
                            autocomplete_inw_ItemName.setFocusableInTouchMode(false);
                            autocomplete_inw_ItemName.setText(LongName.getText().toString());
                            autocomplete_inw_ItemName.setFocusable(true);
                            autocomplete_inw_ItemName.setFocusableInTouchMode(true);

                            //autocomplete_inw_ItemName.setText(LongName.getText());

                            et_inw_rate.setText(Rate.getText().toString());
                            et_inw_quantity.setText(Quantity.getText().toString());
                            rate_prev_for_touched_item = Float.parseFloat(et_inw_rate.getText().toString());
                            double rate = Double.parseDouble(et_inw_rate.getText().toString());
                            double quantity = Double.parseDouble(et_inw_quantity.getText().toString());
                            double amount = rate * quantity;
                            et_Inw_Amount.setText(String.valueOf(amount));

                            String uom_temp = Uom.getText().toString();
                            String uom = "("+uom_temp+")";
                            int index = getIndex(uom);
                            spnrUOM.setSelection(index);
                            spnrUOM.setEnabled(false);
                            //spnrUOM_selection_code.setText(index);

                            et_Inw_CGSTRate.setText(SalesTax.getText());
                            et_Inw_SGSTRate.setText(OtherTax.getText());


//                            imgItemImage.setImageURI(null);
//                            if (!strImageUri.equalsIgnoreCase("")) { // &&
//                                // strImageUri.contains("\\")){
//                                imgItemImage.setImageURI(Uri.fromFile(new File(strImageUri)));
//                            } else {
//                                imgItemImage.setImageResource(R.drawable.img_noimage);
//                            }
                            btnAdd.setEnabled(false);
                            btnEdit.setEnabled(true);
                            btnResetQuantity.setEnabled(true);

                        }
                    }
                });

                rowItems.setTag("TAG");

                tblItems.addView(rowItems, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            } while (crsrItems.moveToNext());

        } else if (!suppliername_str.equals("")){

            MsgBox1.Show(" Information ", "No items found for "+suppliername_str);
            Log.d("DisplayItem", "No Item found for supplier : "+suppliername_str);
        }
        DisplayItems( suppliercode);
    }

    private void updateGoodsInwardQuantity(final String itemname, float quantity, boolean isItem_todelete)
    {
        if(isItem_todelete)
        {
           int l =  dbInwardItem.deleteItemInGoodsInward(itemname);
            if(l>0)
                Log.d("InwardSupply", itemname + " deleted successfully in TBL_GoodsInward");
            else
                Log.d("InwardSupply", itemname + " cannot be deleted in TBL_GoodsInward");
        }
        else{
        Cursor cursor = dbInwardItem. getItem_GoodsInward(itemname);
        if(cursor!=null && cursor.moveToFirst())
        {
            int count = cursor.getInt(cursor.getColumnIndex("SupplierCount")) -1;
            long ll = dbInwardItem.updateSupplierCountInGoodsInward(itemname, count);
            if (quantity > 0) {
                float qty_prev = cursor.getFloat(cursor.getColumnIndex("Quantity"));
                if (qty_prev <= quantity)
                    quantity = 0;
                else
                    quantity = qty_prev-quantity;

                int l = dbInwardItem.updateIngredientQuantityInGoodsInward(itemname, quantity);
                if (l > 0)
                    Log.d("InwardSupply", itemname + " set to quantity " + qty_prev + " to " + quantity + " in TBL_GoodsInward");
                else
                    Log.d("InwardSupply", itemname + "  quantity cannot be reset in TBL_GoodsInward");
            }
        }
    }

    }
    private View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(final View v) {

            TableRow tr = (TableRow) v.getParent();
            TextView ItemName_tv = (TextView) tr.getChildAt(5);
            String itemname = ItemName_tv.getText().toString();

            Cursor cursor = dbInwardItem. getItem_GoodsInward(itemname);
            if(cursor!=null && cursor.moveToFirst()) {
                int count_item = cursor.getInt(cursor.getColumnIndex("SupplierCount")) - 1;
                if (count_item == 0) {
                    deleteitemWithWarning(tr);
                }else {
                    deleteItem(tr);
                }
            }else{
               deleteItem(tr);
            }
        }

    };

    private void deleteitemWithWarning(final TableRow tr)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                .setTitle("Delete")
                .setIcon(R.drawable.ic_launcher)
                .setMessage("Are you sure you want to Delete this Item. " +
                        "\nPlease note that this item has only one supplier now." +
                        "\nThis will delete the item from Inward Item database also" +
                        "\nIf you still want to delete it,press Ok")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        TextView ItemNumber = (TextView) tr.getChildAt(4);
                        TextView ItemQuantity = (TextView) tr.getChildAt(8);
                        TextView ItemName = (TextView) tr.getChildAt(5);
                        TextView SupplierCode = (TextView) tr.getChildAt(16);
                        TextView SupplierName = (TextView) tr.getChildAt(1);
                        TextView SupplierPhone = (TextView) tr.getChildAt(2);
                        TextView SupplierAddress = (TextView) tr.getChildAt(3);

                        String itemname = ItemName.getText().toString();
                        String suppliername = SupplierName.getText().toString();
                        String supplierphone = SupplierPhone.getText().toString();
                        String supplieraddress = SupplierAddress.getText().toString();
                        int suppliercode = Integer.parseInt(SupplierCode.getText().toString());

                        long lResult = dbInwardItem.DeleteItem_Inw(ItemNumber.getText().toString());
                        //MsgBox.Show("", "Item Deleted Successfully");
                        if (lResult>0){
                            Toast.makeText(myContext, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
                            Log.d("InwardSupply",itemname+" delete successfully for supplier "+suppliername+" in TBL_ItemInward");
                            updateGoodsInwardQuantity(itemname, Float.parseFloat(ItemQuantity.getText().toString()),true);
                        }

                        ResetItem();
                        ClearItemTable();
                        if (sema_display == SUPPLIERWISE)
                        {
                            count =1;
                            DisplayItems(suppliercode, suppliername,supplierphone,supplieraddress);
                        }else if (sema_display == ITEMWISE)
                        {
                            count =1;
                            DisplayItems(itemname);
                        }else
                        {
                            // display all
                            count =1;
                            DisplayItems(-1);
                        }


                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private  void deleteItem(final TableRow tr)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                .setTitle("Delete")
                .setIcon(R.drawable.ic_launcher)
                .setMessage("Are you sure you want to Delete this Item. " +
                        "\nPlease note that quantity will also be reduced from Inward Item database." +
                        "\nTo delete only item for this supplier, without reducing the quantity, kindly first reset the quantity , then delete this.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        TextView ItemNumber = (TextView) tr.getChildAt(4);
                        TextView ItemQuantity = (TextView) tr.getChildAt(8);
                        TextView ItemName = (TextView) tr.getChildAt(5);
                        TextView SupplierCode = (TextView) tr.getChildAt(16);
                        TextView SupplierName = (TextView) tr.getChildAt(1);
                        TextView SupplierPhone = (TextView) tr.getChildAt(2);
                        TextView SupplierAddress = (TextView) tr.getChildAt(3);

                        String itemname = ItemName.getText().toString();
                        String suppliername = SupplierName.getText().toString();
                        String supplierphone = SupplierPhone.getText().toString();
                        String supplieraddress = SupplierAddress.getText().toString();
                        int suppliercode = Integer.parseInt(SupplierCode.getText().toString());

                        long lResult = dbInwardItem.DeleteItem_Inw(ItemNumber.getText().toString());
                        //MsgBox.Show("", "Item Deleted Successfully");
                        if (lResult>0){
                            Toast.makeText(myContext, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
                            Log.d("InwardSupply",itemname+" delete successfully for supplier "+suppliername+" in TBL_ItemInward");
                            updateGoodsInwardQuantity(itemname, Float.parseFloat(ItemQuantity.getText().toString()),false);
                        }
                        ResetItem();
                        ClearItemTable();
                        if (sema_display == SUPPLIERWISE)
                        {
                            count =1;
                            DisplayItems(suppliercode, suppliername,supplierphone,supplieraddress);
                        }else if (sema_display == ITEMWISE)
                        {
                            count =1;
                            DisplayItems(itemname);
                        }else
                        {
                            // display all
                            count =1;
                            DisplayItems(-1);
                        }


                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean IsItemExists(String ItemFullName) {
        boolean isItemExists = false;
        String strItem = "";
        TextView Item;

        for (int i = 0; i < tblItems.getChildCount(); i++) {

            TableRow Row = (TableRow) tblItems.getChildAt(i);

            if (Row.getChildAt(0) != null) {
                Item = (TextView) Row.getChildAt(1);

                strItem = Item.getText().toString();
                if (strItem.toUpperCase().equalsIgnoreCase(ItemFullName.toUpperCase())) {
                    isItemExists = true;
                    break;
                }
            }
        }

        return isItemExists;
    }
    private boolean IsItemExists_withSupplier(String ItemFullName, int supplierCode_recieved) {
        boolean isItemExists = false;
        String strItem = "";
        TextView Item, SupplierCode;

        for (int i = 0; i < tblItems.getChildCount(); i++) {

            TableRow Row = (TableRow) tblItems.getChildAt(i);

            if (Row.getChildAt(0) != null) {
                Item = (TextView) Row.getChildAt(5);
                SupplierCode = (TextView) Row.getChildAt(16);
                strItem = Item.getText().toString();
                int supplierCode = Integer.parseInt(SupplierCode.getText().toString());
                if ((strItem.toUpperCase().equalsIgnoreCase(ItemFullName.toUpperCase())) && (supplierCode== supplierCode_recieved)) {
                    isItemExists = true;
                    break;
                }
            }
        }

        return isItemExists;
    }

    private void InsertItem(int suppliercode, String suppliername, String itemName, String strbarCode, float ratef,
                            float quantity, String mou, String ImageUri, float cgst, float sgst, float igstrate,
                            String supplytype, String hsnCode) {

        long lRowId = 0;

        Item objItem = new Item();
        objItem.setsuppliercode(suppliercode);
        objItem.setsupplierName(suppliername);
        objItem.setItemname(itemName);
        objItem.setItemBarcode(strbarCode);
        objItem.setRate(ratef);
        objItem.setQuantity(quantity);
        objItem.setMOU(mou);
        objItem.setImageId(ImageUri);
        objItem.setCGSTRate(cgst);
        objItem.setSGSTRate(sgst);
        objItem.setIGSTRate(igstrate);
        objItem.setSupplyType(supplytype);
        objItem.setAverageRate(ratef);
        objItem.setHSNCode(hsnCode);


        lRowId = dbInwardItem.addItem_Inw_nonGST(objItem);

        Log.d("Item Inward", "Row Id:" + String.valueOf(lRowId));
    }

    /*private void ReadData(int Type) {
        String itemname = "", strShortName = "", strBarcode = "",mou ="";
        int iDeptCode = 0, iCategCode = 0, iKitchenCode = 0, iSalesTaxId = 0, iAdditionalTaxId = 0, iOptionalTaxId1 = 0,
                iOptionalTaxId2 = 0, iDiscountId = 0, iPriceChange = 0, iDiscountEnable = 0, iBillWithStock = 0,
                iTaxType = 0;
        float fDineIn1 = 0, fDineIn2 = 0, fDineIn3 = 0, fTakeAway = 0, fPickUp = 0, fDelivery = 0, fStock = 0;
        String HSNCode = "",TaxationType = ""; //, AMU = "";
        float rate =0, quantity =0,igstRate =0,igstAmount =0, cgstRate =0, cgstAmount=0, sgstRate =0, sgstAmount =0;
        float SalesTax =0, ServiceTax =0;


        String suppliername = autocompletetv_suppliername.getText().toString().toUpperCase().toUpperCase();
        int suppliercode = dbInwardItem.getSuppliercode(suppliername);
        if (suppliercode<0)
        {
            MsgBox.setTitle(" Warning")
                    .setMessage(" Supplier not found in list. Please save supplier first")
                    .setPositiveButton("Ok", null)
                    .show();
            return;
        }
        itemname = autocomplete_inw_ItemName.getText().toString().toUpperCase();
        strBarcode = et_inw_ItemBarcode.getText().toString();
        rate = Float.parseFloat(et_inw_rate.getText().toString());
        quantity = Float.parseFloat(et_inw_quantity.getText().toString());
        SalesTax = Float.parseFloat(et_Inw_CGSTRate.getText().toString());
        ServiceTax = Float.parseFloat(et_Inw_SGSTRate.getText().toString());
        String mou_temp = spnrUOM.getSelectedItem().toString();
        int length = mou_temp.length();
        mou = mou_temp.substring(length-3, length-1);
        String supplytype = spnr_supplytype.getSelectedItem().toString();
        String ImageUri= "" ;


        // Type 1 - addItem, Type 2 - updateItem
        if (Type == 1) {
            if (IsItemExists(itemname.toUpperCase())) {
                MsgBox = new AlertDialog.Builder(myContext);
                Toast.makeText(myContext, "Warning Item already present", Toast.LENGTH_SHORT).show();
                // MsgBox.show("Warning", "Item already present");
            }
            else {
                InsertItem( suppliercode,  suppliername, itemname,  strBarcode, rate,  quantity, mou, ImageUri, SalesTax, ServiceTax,supplytype);

            }

        } else if (Type == 2) {


            int menucode =   Integer.parseInt(strMenuCode);
            if (menucode>0) {
                long lRowId = 0;

                Item objItem = new Item();
                objItem.setsuppliercode(suppliercode);
                objItem.setsupplierName(suppliername);
                objItem.setItemname(itemname);
                objItem.setItemBarcode(strBarcode);
                objItem.setRate(rate);
                objItem.setQuantity(quantity);
                objItem.setUOM(mou);
                objItem.setImageId(ImageUri);
                objItem.setCGSTRate(SalesTax);
                objItem.setSGSTRate(ServiceTax);
                objItem.setSupplyType(supplytype);
                objItem.setMenuCode(menucode);


                int iRowId = dbInwardItem.updateItem_Inw_nonGST(objItem);
                if (iRowId <1 ) // not updated
                {
                    Cursor  item_crsr = dbInwardItem.getItem_inward(menucode);
                    if (item_crsr == null || !item_crsr.moveToFirst()) {

                        *//*AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);
                        LayoutInflater UserAuthorization = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View vwAuthorization = UserAuthorization.inflate(R.layout.user_authorization, null);
                        final EditText txtUserId = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserId);
                        final EditText txtPassword = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserPassword);
                        final TextView tvAuthorizationUserId = (TextView) vwAuthorization.findViewById(R.id.tvAuthorizationUserId);
                        final TextView tvAuthorizationUserPassword = (TextView) vwAuthorization.findViewById(R.id.tvAuthorizationUserPassword);

                        tvAuthorizationUserId.setVisibility(View.GONE);
                        tvAuthorizationUserPassword.setVisibility(View.GONE);
                        txtUserId.setVisibility(View.GONE);
                        txtPassword.setVisibility(View.GONE);
                        AuthorizationDialog
                                .setTitle("Item not found in database. Do you want to add it ?")
                                .setView(vwAuthorization)
                                .setNegativeButton("No", null)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        ReadData(1);
                                    }
                                })
                                .show();*//*
                        MsgBox.setTitle("Item not found in database. Do you want to add it ?")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        ReadData(1);
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }
                }
                else // successfully updated
                {
                    Toast.makeText(myContext, "Item Updated Successfully", Toast.LENGTH_LONG).show();
                }
                //Log.d("itemdatabase inward : ", "Updated Rows: " + String.valueOf(iRowId));
            }
        }

    }*/

    private void ReadData(int Type) {
        String itemname = "", strShortName = "", strBarcode = "",mou ="";
        int iDeptCode = 0, iCategCode = 0, iKitchenCode = 0, iSalesTaxId = 0, iAdditionalTaxId = 0, iOptionalTaxId1 = 0,
                iOptionalTaxId2 = 0, iDiscountId = 0, iPriceChange = 0, iDiscountEnable = 0, iBillWithStock = 0,
                iTaxType = 0;
        float fDineIn1 = 0, fDineIn2 = 0, fDineIn3 = 0, fTakeAway = 0, fPickUp = 0, fDelivery = 0, fStock = 0;
        String HSNCode = "",TaxationType = ""; //, AMU = "";
        float rate =0, quantity =0,igstRate =0,igstAmount =0, cgstRate =0, cgstAmount=0, sgstRate =0, sgstAmount =0;
        float SalesTax =0, ServiceTax =0, IGSTRate =0;


        String suppliername = autocompletetv_suppliername.getText().toString().toUpperCase();
        String suppliergstin = edt_supplierGSTIN.getText().toString();
        int suppliercode = dbInwardItem.getSuppliercode(suppliername);
        if (suppliercode<0)
        {
            MsgBox1.Show(" Warning"," Supplier not found in list. Please save supplier first");
            return;
        }
        itemname = autocomplete_inw_ItemName.getText().toString().toUpperCase();
        strBarcode = et_inw_ItemBarcode.getText().toString();
        rate = Float.parseFloat(et_inw_rate.getText().toString());
        String rate_str = String.format("%.2f",rate);
        rate = Float.parseFloat(rate_str);
        //quantity =Float.parseFloat(et_inw_quantity.getText().toString());
        SalesTax = Float.parseFloat(et_Inw_CGSTRate.getText().toString());
        ServiceTax = Float.parseFloat(et_Inw_SGSTRate.getText().toString());
        IGSTRate = Float.parseFloat(et_Inw_IGSTRate.getText().toString());
        String mou_temp = spnrUOM.getSelectedItem().toString();
        int length = mou_temp.length();
        mou = mou_temp.substring(length-3, length-1);
        String supplytype = spnr_supplytype.getSelectedItem().toString();
        String ImageUri= "" ;
        String hsnCode = et_inw_HSNCode.getText().toString();


        // Type 1 - addItem, Type 2 - updateItem
        if (Type == 1) {
            if (false)/*IsItemExists(itemname.toUpperCase()))*/
            {
                MsgBox = new AlertDialog.Builder(myContext);
                Toast.makeText(myContext, "Warning Item already present", Toast.LENGTH_SHORT).show();
                // MsgBox.show("Warning", "Item already present");
            }
            else {
                InsertItem( suppliercode,  suppliername, itemname,  strBarcode, rate,  quantity, mou, ImageUri, SalesTax, ServiceTax,IGSTRate,supplytype,hsnCode);
                double rate_new = UpdateGoodsInward(itemname, quantity, mou, Double.parseDouble(et_inw_rate.getText().toString()), true);
               // UpdateStockInward(itemname,quantity,   rate_new);
            }

        } else if (Type == 2) { // update

            int menucode =   Integer.parseInt(strMenuCode);
            long lRowId = 0;
            Item objItem = new Item();

            objItem.setsuppliercode(suppliercode);
            objItem.setsupplierName(suppliername);
            objItem.setItemname(itemname);
            objItem.setItemBarcode(strBarcode);
            objItem.setRate(rate);
            objItem.setQuantity(quantity);
            objItem.setMOU(mou);
            objItem.setImageId(ImageUri);
            objItem.setSalesTaxPercent(SalesTax);
            objItem.setServiceTaxPercent(ServiceTax);
            objItem.setSupplyType(supplytype);
            objItem.setMenuCode(menucode);
            objItem.setHSNCode(hsnCode);

            int count = Integer.parseInt(tv_count.getText().toString());
            float averageRate_f = Float.parseFloat(tv_AverageRate.getText().toString());
            float tot = averageRate_f;
            if(rate_prev_for_touched_item != rate) {
                tot = count * averageRate_f;
                tot += rate;
                count++;
                tot /= count;

                String tot_str = String.format("%.2f", tot);
                tot = Float.parseFloat(tot_str);
            }
            objItem.setCount(count);
            objItem.setAverageRate(tot);

            Cursor item_crsr = dbInwardItem.getItem_inward(menucode);
            if (item_crsr != null && item_crsr.moveToFirst()) //  item present in database
            {
                int iRowId = dbInwardItem.updateItem_Inw_nonGST(objItem);
                if (iRowId >0  ){ // not updated
                    Toast.makeText(myContext, "Update Item Inward : Row updated: "+String.valueOf(iRowId), Toast.LENGTH_SHORT).show();
                    double rate_new =  UpdateGoodsInward(itemname,quantity,mou,Double.parseDouble(et_inw_rate.getText().toString()), false);
                    //UpdateStockInward(itemname,quantity,rate_new);
                } else{
                    Toast.makeText(myContext, " Item Cannot be updated", Toast.LENGTH_SHORT).show();
                }

                Log.d("Inward Item Update", "Rows updated : "+String.valueOf(iRowId));
            } else{ // item not present in database
                suppliercode1 = suppliercode;
                suppliername1  = suppliername ;
                supplierphn1 = autocompletetv_supplierPhn.getText().toString();
                supplieraddress1 = et_inw_supplierAddress.getText().toString();
                itemname1 = itemname;
                strBarcode1 = strBarcode;
                rate1 = rate;
                quantity1 = quantity;
                mou1 = mou;
                ImageUri1 = ImageUri;
                SalesTax1 = SalesTax;
                ServiceTax1 = ServiceTax;
                supplytype1 = supplytype;
                final String HSNCode1 = hsnCode;
                final float igstrate = IGSTRate;


                MsgBox.setTitle("Item not found in database. Do you want to add it ?")
                        .setIcon(R.drawable.ic_launcher)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                InsertItem( suppliercode1,  suppliername1, itemname1,  strBarcode1, rate1,  quantity1, mou1, ImageUri1, SalesTax1, ServiceTax1,igstrate,supplytype1,HSNCode1);
                                tv_suppliercode.setText(String.valueOf(suppliercode1));
                                autocompletetv_suppliername.setText(suppliername1);
                                autocompletetv_supplierPhn.setText(supplierphn1);
                                et_inw_supplierAddress.setText(supplieraddress1);
                                autocomplete_inw_ItemName.setText(itemname1);
                                ClearingAndDisplaying();
                                double rate_new =  UpdateGoodsInward(itemname1, quantity1, mou1, Double.parseDouble(et_inw_rate.getText().toString()), true);
                               // UpdateStockInward(itemname1, quantity1,rate_new);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();


            }
        }
    }

    private double UpdateGoodsInward(String itemname, float quantity, String mou, double rate, boolean isNewSupplier)
    {
        double rate_new = 0;
        try {
            Cursor item_present_crsr = dbInwardItem.getItem_GoodsInward(itemname);
            if (item_present_crsr != null && item_present_crsr.moveToFirst()) {
                // already present , needs to update
               /* String qty_str = item_present_crsr.getString(item_present_crsr.getColumnIndex("Quantity"));
                float qty_temp = Float.parseFloat(qty_str);
                quantity += qty_temp;

                int newSupplierCount = item_present_crsr.getInt(item_present_crsr.getColumnIndex("SupplierCount"));
                double rate_prev = item_present_crsr.getDouble(item_present_crsr.getColumnIndex("Value"));
                rate_new = rate_prev*newSupplierCount;
                rate_new += rate;
                newSupplierCount++;
                rate_new /= newSupplierCount;

                Long l = dbInwardItem.updateIngredient(itemname, quantity,rate_new, newSupplierCount);
                if (l > 0) {
                    Log.d(" GoodsInwardNote ", itemname + " updated  successfully at " + l);
                }*/
                if(isNewSupplier)
                {
                    int newSupplierCount = item_present_crsr.getInt(item_present_crsr.getColumnIndex("SupplierCount"));
                    newSupplierCount++;
                    Long l = dbInwardItem.updateSupplierCountInGoodsInward(itemname,newSupplierCount);

                }
            }else
            {
                // new entry
                rate_new = rate;
                //Long  l = dbInwardItem.addIngredient(itemname, quantity, mou, rate_new, 1);
                Long  l = dbInwardItem.addIngredient(itemname, quantity, mou, 0, 1);
                if (l > 0) {
                    Log.d(" GoodsInwardNote ", itemname + " added  successfully at " + l);
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(myContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            return rate_new;
        }
    }
    private void UpdateStockInward(String itemname, float quantity,  double rate)
    {
        try{
            Cursor item_present_crsr = dbInwardItem.getInwardStock(itemname);
            double Openingqty_prev = 0;
            double Closingqty_prev = 0;
            ItemStock item = new ItemStock();

            if (item_present_crsr != null && item_present_crsr.moveToFirst()) {
                // already present , needs to update
                String qty_str = item_present_crsr.getString(item_present_crsr.getColumnIndex("OpeningStock"));
                if(!qty_str.equalsIgnoreCase(""))
                    Openingqty_prev =  Double.parseDouble(qty_str);
                String ClosingQty_str = item_present_crsr.getString(item_present_crsr.getColumnIndex("ClosingStock"));
                if(!qty_str.equalsIgnoreCase(""))
                    Closingqty_prev =  Double.parseDouble(qty_str);
                item.setMenuCode(item_present_crsr.getInt(item_present_crsr.getColumnIndex("MenuCode")));
                item.setOpeningStock(Openingqty_prev+Double.parseDouble(String.valueOf(quantity)));
                item.setClosingStock(Closingqty_prev+Double.parseDouble(String.valueOf(quantity)));
                item.setRate(rate);
                Long l = dbInwardItem.updateOpeningStockInward(item, businessDate);
                if (l>0) {
                    Log.d("Stockinwardmaintain", " SaveStock() : opening save stock for item :" + item.getItemName());
                }
                l = dbInwardItem.updateClosingStockInward(item, businessDate);
                if (l>0) {
                    Log.d("Stockinwardmaintain", " SaveStock() : closing save stock for item :" + item.getItemName());
                }

            }else
            {
                // new entry
                Cursor itemCursor = dbInwardItem.getItem_GoodsInward(itemname);
                if(itemCursor != null && itemCursor.moveToFirst())
                {
                    item.setMenuCode(itemCursor.getInt(itemCursor.getColumnIndex("MenuCode")));
                    item.setItemName(itemname);
                    item.setOpeningStock(quantity);
                    item.setClosingStock(quantity);
                    item.setRate(rate);
                    Log.d("SaveStock():", item.getItemName() + " @ " + businessDate);
                    long l = dbInwardItem.insertStockInward(item, businessDate);
                    if (l>0) {
                        Log.d("Stockinwardmaintain", " SaveStock() : save stock for item :" + item.getItemName());
                    }
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(myContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void AddSupplier(View v)
    {
        long l =0;
        Cursor cursor = dbInwardItem.getAllSupplierName_nonGST();
        //labelsSupplierName = dbInwardItem.getAllSupplierName_nonGST();
        labelsSupplierName = new ArrayList<String>();
        ArrayList<String>labelsSupplierGSTIN = new ArrayList<String>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                labelsSupplierName.add(cursor.getString(cursor.getColumnIndex("SupplierName")));// adding
                String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
                if(gstin!=null && !gstin.equals(""))
                    labelsSupplierGSTIN.add(gstin);
            } while (cursor.moveToNext());
        }

        MsgBox.setTitle("Incomplete Information")
                .setIcon(R.drawable.ic_launcher)
                .setPositiveButton("Ok", null);

        String supplierType_str = "UnRegistered";
        String suppliername_str = autocompletetv_suppliername.getText().toString().toUpperCase();
        String supplierphn_str = autocompletetv_supplierPhn.getText().toString();
        String supplieraddress_str = et_inw_supplierAddress.getText().toString();
        String suppliergstin_str = edt_supplierGSTIN.getText().toString();
        if(suppliergstin_str!=null && !suppliergstin_str.equals(""))
            supplierType_str="Registered";
        else
            suppliergstin_str="";

        if(labelsSupplierGSTIN.contains(suppliergstin_str))
        {
            MsgBox.setTitle("Warning")
                    .setIcon(R.drawable.ic_launcher)
                    .setMessage("Supplier with gstin already present in list")
                    .setPositiveButton("OK",null)
                    .show();
            return;
        }

        for (String supplier : labelsSupplierName)
        {
            if (suppliername_str.equalsIgnoreCase(supplier))
            {
                MsgBox.setTitle("Warning")
                        .setIcon(R.drawable.ic_launcher)
                        .setMessage("Supplier with name already present in list")
                        .setPositiveButton("OK",null)
                        .show();
                return;
            }
        }


        if (suppliername_str.equals("") || supplieraddress_str.equals("") || supplierphn_str.equals(""))
        {
            MsgBox.setMessage("Please fill all details of Supplier")
                    .show();
        }
        else
        {
            l = dbInwardItem.saveSupplierDetails(supplierType_str, suppliergstin_str,suppliername_str,
                    supplierphn_str, supplieraddress_str);
            if (l>0)
            {
                Log.d("Inward_Item_Entry"," Supplier details saved at "+l);
                Toast.makeText(myContext, "Supplier details saved at "+l, Toast.LENGTH_SHORT).show();

            }
        }
        try
        {
            cursor = dbInwardItem.getAllSupplierName_nonGST();
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
            tv_suppliercode.setText(String.valueOf(l));
            loadAutoCompleteData_item((int)l);

        }
        catch(Exception e )
        {
            MsgBox.setMessage(e.getMessage())
                    .setNeutralButton("Ok", null)
                    .show();
        }

    }

    private void ClearItemTable() {
        for (int i = 0; i < tblItems.getChildCount(); i++) {
            View Row = tblItems.getChildAt(i);
            if (Row instanceof TableRow) {
                ((TableRow) Row).removeAllViews();
            }
        }
    }

    private void ResetItem() {
        strMenuCode = "";
        strImageUri = "";
        AMU = "";
        spnrUOM.setSelection(0);
        spnrUOM.setEnabled(true);
        //spnrUOM_selection_code.setText("-1");
        //tvFileName.setText("FileName");
        et_inw_ItemBarcode.setText("");
        autocomplete_inw_ItemName.setText("");
        edt_supplierGSTIN.setText("");
        et_inw_rate.setText("");
        et_inw_quantity.setText("");
        et_Inw_Amount.setText("");
        et_inw_HSNCode.setText("");
        et_inw_supplierAddress.setText("");
        autocompletetv_supplierPhn.setText("");
        et_inw_supplierAddress.setText("");
        tv_suppliercode.setText("-1"); // no value
        tv_AverageRate.setText("0.00"); // no value
        tv_count.setText("0"); // no value
        autocompletetv_suppliername.setText("");
        et_Inw_SGSTRate.setText("0");
        et_Inw_CGSTRate.setText("0");
        et_Inw_IGSTRate.setText("0");
        //btnAddSupplier.setEnabled(true);
        btnAdd.setEnabled(true);
        btnEdit.setEnabled(false);
        btnResetQuantity.setEnabled(false);
        rate_prev_for_touched_item = 0;
    }



    public void AddItem1 (View v)
    {
        Cursor cursor = dbInwardItem.getAllSupplierName_nonGST();
        //labelsSupplierName = dbInwardItem.getAllSupplierName_nonGST();
        labelsSupplierName = new ArrayList<String>();
        ArrayList<String>labelsSupplierGSTIN = new ArrayList<String>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                labelsSupplierName.add(cursor.getString(cursor.getColumnIndex("SupplierName")));// adding
                String gstin = (cursor.getString(cursor.getColumnIndex("GSTIN")));// adding
                if(gstin!=null && !gstin.equals(""))
                    labelsSupplierGSTIN.add(gstin);
            } while (cursor.moveToNext());
        }

        String suppliername_str = autocompletetv_suppliername.getText().toString().toUpperCase();
        String suppliergstin_str = edt_supplierGSTIN.getText().toString();
        int flag =0;
        for (String supplier : labelsSupplierName)
        {
            if (supplier.equalsIgnoreCase(suppliername_str))
            {
                flag =1;
                break;
            }
        }
        if(labelsSupplierGSTIN.contains(suppliergstin_str))
            flag =1;

        if (flag ==0)
        {
            MsgBox.setTitle("Warning")
                    .setIcon(R.drawable.ic_launcher)
                    .setMessage("Supplier not list. Please add supplier first")
                    .setPositiveButton("OK",null)
                    .show();
            return;
        }
        String item_name = autocomplete_inw_ItemName.getText().toString().toUpperCase();
        if (item_name.equalsIgnoreCase("")) {
            MsgBox.setTitle("Warning")
                    .setIcon(R.drawable.ic_launcher)
                    .setMessage("Please enter item name")
                    .show();
            return;
        }
        String supplierCode_str = tv_suppliercode.getText().toString();
        if(supplierCode_str == null || supplierCode_str.equalsIgnoreCase(""))
        {
            MsgBox.setTitle("Warning")
                    .setIcon(R.drawable.ic_launcher)
                    .setMessage("Supplier not list. Please add supplier first")
                    .setPositiveButton("OK",null)
                    .show();
            return;
        }
        if(IsItemExists_withSupplier(item_name, Integer.parseInt(supplierCode_str)))
        {
            MsgBox.setTitle("Warning")
                    .setIcon(R.drawable.ic_launcher)
                    .setMessage(item_name+" is already present with supplier "+suppliername_str)
                    .setPositiveButton("OK",null)
                    .show();
            return;
        }
        String uom = spnrUOM.getSelectedItem().toString();
        if (uom.equalsIgnoreCase("") || uom.equalsIgnoreCase("Select")) {
            MsgBox.setTitle("Warning")
                    .setIcon(R.drawable.ic_launcher)
                    .setMessage("Please select item UoM")
                    .setPositiveButton("Ok",null)
                    .show();
            return;
        }
        Cursor cursor_item = dbInwardItem.getItem_GoodsInward(item_name);
        if(cursor_item.moveToFirst())
        {
            int length = uom.length();
            String mou_temp = uom.substring(length-3, length-1);
            String uom_already_saved = cursor_item.getString(cursor_item.getColumnIndex("UOM"));
            if(!mou_temp.equalsIgnoreCase(uom_already_saved))
            {
                MsgBox.setTitle("Warning")
                        .setIcon(R.drawable.ic_launcher)
                        .setMessage(item_name+" is already present in database with unit "+uom_already_saved+". " +
                                "\nKindly set the unit to "+uom_already_saved+
                                "\nOr to change the unit , kindly delete this item for all the suppliers." +
                                "\nThen add the item")
                        .setPositiveButton("Ok",null)
                        .show();
                return;
            }
        }


        String salesTax_str = et_Inw_CGSTRate.getText().toString();
        if (salesTax_str.equalsIgnoreCase("")) {
            et_Inw_CGSTRate.setText("0");
        }else if (Double.parseDouble(salesTax_str)< 0 || Double.parseDouble(salesTax_str)>99.99)
        {
            MsgBox1.Show("Warning","Please enter CGST Rate between 0 and 99.99");
            return;
        }

        String serviceTax_str = et_Inw_SGSTRate.getText().toString();
        if (serviceTax_str.equalsIgnoreCase("")) {
            et_Inw_SGSTRate.setText("0");
        }else if (Double.parseDouble(serviceTax_str) <0 || Double.parseDouble(serviceTax_str)> 99.99)
        {
            MsgBox1.Show("Warning","Please enter SGST Rate between 0 and 99.99");
            return;
        }

        /*if (et_inw_quantity.getText().toString().equalsIgnoreCase("")) {
            MsgBox.setTitle("Warning")
                    .setMessage("Please enter item quantity ")
                    .setPositiveButton("Ok",null)
                    .show();
            return;
        }*/
        if (et_inw_rate.getText().toString().equalsIgnoreCase("")) {
            MsgBox.setTitle("Warning")
                    .setIcon(R.drawable.ic_launcher)
                    .setMessage("Please enter item rate")
                    .setPositiveButton("Ok",null)
                    .show();
            return;
        }



       /* String mou_temp = spnrUOM.getSelectedItem().toString();
        if (mou_temp.equalsIgnoreCase("Unit") || mou_temp.equalsIgnoreCase("Select Unit"))
        {
            MsgBox.setTitle("Error")
                    .setMessage("Please Select the Unit for the item.")
                    .setPositiveButton("OK",null)
                    .show();
            return;
        }*/

        try {
            ReadData(1); // 1 - addItem
            Toast.makeText(myContext, "Item Added Successfully", Toast.LENGTH_LONG).show();
            ClearingAndDisplaying();
            loadAutoCompleteData_item(-1);

        } catch (Exception exp) {
            Toast.makeText(myContext, exp.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void ResetQuantity(View v)
    {
        MsgBox.setTitle("Warning")
                .setIcon(R.drawable.ic_launcher)
                .setMessage("Please note only quantity for this item and supplier will be reset to 0. " +
                        "\nAll other changes, if any , will be discarded")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetQty();
                    }
                })
                .show();
    }

    private  void resetQty()
    {
        String itemName = autocomplete_inw_ItemName.getText().toString();
        if (itemName.equalsIgnoreCase("")) {
            /*MsgBox.Show("Warning", "Please enter item full name");*/
            Toast.makeText(myContext, "Please enter item name", Toast.LENGTH_SHORT).show();
            return;
        }
        int supplierCode = Integer.parseInt(tv_suppliercode.getText().toString());
        int menuCode = Integer.parseInt(strMenuCode);
        long l = dbInwardItem.resetStock_inward(supplierCode, menuCode,itemName);
        if(l>0)
            Toast.makeText(myContext, itemName+" reset to 0 for supplier : "+autocompletetv_suppliername.getText().toString(), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(myContext, itemName+" cannot be reset to 0 ", Toast.LENGTH_LONG).show();
        ClearingAndDisplaying();
    }
    public void EditItem1(View v) {
        if (autocomplete_inw_ItemName.getText().toString().equalsIgnoreCase("")) {
            /*MsgBox.Show("Warning", "Please enter item full name");*/
            Toast.makeText(myContext, "Please enter item name", Toast.LENGTH_SHORT).show();
            return;
        }
        Cursor cursor = dbInwardItem.getAllSupplierName_nonGST();
        ArrayList<String>labelsSupplierGSTIN = new ArrayList<String>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                labelsSupplierName.add(cursor.getString(cursor.getColumnIndex("SupplierName")));// adding
                String gstin = (cursor.getString(cursor.getColumnIndex("GSTIN")));// adding
                if(gstin!=null && !gstin.equals(""))
                    labelsSupplierGSTIN.add(gstin);
            } while (cursor.moveToNext());
        }

        String suppliername_str = autocompletetv_suppliername.getText().toString().toUpperCase();
        String suppliergstin_str = edt_supplierGSTIN.getText().toString();
        int flag =0;
        for (String supplier : labelsSupplierName)
        {
            if (supplier.equalsIgnoreCase(suppliername_str))
            {
                flag =1;
                break;
            }
        }
        if(labelsSupplierGSTIN.contains(suppliergstin_str))
            flag =1;
        if(flag==0)
        {
            MsgBox1.Show("Error","Supplier not in list.");
            return;
        }

        String salesTax_str = et_Inw_CGSTRate.getText().toString();
        if (salesTax_str.equalsIgnoreCase("")) {
            et_Inw_CGSTRate.setText("0");
        }else if (Double.parseDouble(salesTax_str)< 0 || Double.parseDouble(salesTax_str)>99.99)
        {
            MsgBox1.Show("Warning","Please enter sales tax percent between 0 and 99.99");
            return;
        }

        String serviceTax_str = et_Inw_SGSTRate.getText().toString();
        if (serviceTax_str.equalsIgnoreCase("")) {
            et_Inw_SGSTRate.setText("0");
        }else if (Double.parseDouble(serviceTax_str) <0 || Double.parseDouble(serviceTax_str)> 99.99)
        {
            MsgBox1.Show("Warning","Please enter service tax percent between 0 and 99.99");
            return;
        }

        /*if (et_inw_quantity.getText().toString().equalsIgnoreCase("")) {
            et_inw_quantity.setText("0");
        }*/

        if (et_inw_rate.getText().toString().equalsIgnoreCase("")) {
            et_inw_rate.setText("0");
        }
        // richa 2712_UOM
        String mou_temp = spnrUOM.getSelectedItem().toString();
        if (mou_temp.equalsIgnoreCase("Unit") || mou_temp.equalsIgnoreCase("Select Unit"))
        {
            MsgBox.setTitle("Error")
                    .setIcon(R.drawable.ic_launcher)
                    .setMessage("Please Select the Unit for the item.")
                    .setPositiveButton("OK",null)
                    .show();
            return;
        }
        // richa 2712_UOM

        try {
            ReadData(2); // 2 - updateItem
            ClearingAndDisplaying();
        } catch (Exception exp) {
            Toast.makeText(myContext, exp.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void ClearingAndDisplaying()
    {
        ClearItemTable();
        if(sema_display == ALL)
        {
            count =1;
            DisplayItems(-1);
        }else if(sema_display == SUPPLIERWISE)
        {
            String suppliercode = tv_suppliercode.getText().toString();
            String suppliername = autocompletetv_suppliername.getText().toString();
            String supplierphn = autocompletetv_supplierPhn.getText().toString();
            String supplierAddress = et_inw_supplierAddress.getText().toString();
            int suppcode = -1;
            if (suppliercode== null || suppliercode.equals(""))
                suppcode = -1;
            else
                suppcode = Integer.parseInt(suppliercode);

            count =1;
            DisplayItems(suppcode, suppliername, supplierphn, supplierAddress);


        } else // Itemwise
        {
            String itemname = autocomplete_inw_ItemName.getText().toString();
            count =1;
            DisplayItems(itemname);
            // MsgBox.Show("Check", "itemwise on adding");
        }

        ResetItem();
    }

    public void ClearItem1(View v) {
        ResetItem();
        loadSpinnerData();
        sema_display= ALL;
        ClearingAndDisplaying();
        //loadSpinnerData1();
    }

    public void CloseItem1(View v) {
        dbInwardItem.CloseDatabase();
        getActivity().finish();
    }


    public void loadAutoCompleteData_item(int suppliercode) {

        String suppliername_str = autocompletetv_suppliername.getText().toString().toUpperCase();
        List<String> itemlist = dbInwardItem.getitemlist_inward_nonGST(suppliername_str, suppliercode);

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,itemlist);

        dataAdapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);

        // attaching data adapter to spinner
        autocomplete_inw_ItemName.setAdapter(dataAdapter1);

    }

    public void loadSpinnerData()
    {
        Cursor cursor = dbInwardItem.getAllSupplierName_nonGST();
        labelsSupplierName = new ArrayList<String>();
        labelsSupplierPhn = new ArrayList<String>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                labelsSupplierName.add(cursor.getString(cursor.getColumnIndex("SupplierName")));// adding
                labelsSupplierPhn.add(cursor.getString(cursor.getColumnIndex("SupplierPhone")));// adding
            } while (cursor.moveToNext());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,
                labelsSupplierName);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        autocompletetv_suppliername.setAdapter(dataAdapter);


        ArrayAdapter<String> dataAdapter_phn = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,
                labelsSupplierPhn);
        dataAdapter_phn.setDropDownViewResource(android.R.layout.simple_list_item_1);
        //autocompletetv_supplierPhn.setAdapter(dataAdapter_phn);



        // for itemwise search , load item adapter also. If any supplier is selected then item adapter
        // will be reloaded for that particular supplier only

        Cursor cursor_item = dbInwardItem.getAllInwardItemNames();
        itemlist = new ArrayList<String>();
        if (cursor_item != null && cursor_item.moveToFirst()) {
            do {
                itemlist.add(cursor_item.getString(cursor_item.getColumnIndex("ItemName")));// adding
            } while (cursor_item.moveToNext());
        }
        dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,itemlist);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        autocomplete_inw_ItemName.setAdapter(dataAdapter);

    }

    private int getIndex(String substring){

        int index = 0;
        for (int i = 0; index==0 && i < spnrUOM.getCount(); i++){

            if (spnrUOM.getItemAtPosition(i).toString().contains(substring)){
                index = i;
            }

        }

        return index;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == FilePickerActivity.FILE_PICKER_CODE) {
                strImageUri = data.getStringExtra(FilePickerActivity.EXTRA_FILE_PATH);
                Log.d("FilePicker Result", "Path + FileName:" + strImageUri);
                // Toast.makeText(myContext, "Image Path:" + strImageUri,
                // Toast.LENGTH_LONG).show();
//                if(!strImageUri.equalsIgnoreCase("")) {
//                    imgItemImage.setImageURI(null);
//                    imgItemImage.setImageURI(Uri.fromFile(new File(strImageUri)));
//                }else{
//                    imgItemImage.setImageResource(R.drawable.img_noimage);
//                }
                strUploadFilepath = data.getStringExtra(UploadFilePickerActivity.EXTRA_FILE_PATH);
               // tvFileName.setText(strUploadFilepath.substring(strUploadFilepath.lastIndexOf("/")+1));
            }
        }
        super.onActivityResult( requestCode,  resultCode,  data);
    }




}
