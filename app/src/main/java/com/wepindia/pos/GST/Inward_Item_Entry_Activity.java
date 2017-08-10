package com.wepindia.pos.GST;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
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
import com.wep.common.app.WepBaseActivity;
import com.wepindia.pos.FilePickerActivity;
import com.wepindia.pos.GenericClasses.EditTextInputHandler;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.R;
import com.wepindia.pos.utils.ActionBarUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by welcome on 07-11-2016.
 */
public class Inward_Item_Entry_Activity extends WepBaseActivity {

    Context myContext;
    DatabaseHandler dbInwardItem;
    public AlertDialog.Builder MsgBox;

    // View handlers




    Button btnAdd, btnEdit, btnUploadExcel, btnSaveExcel;
    ImageView imgItemImage;
    TableLayout tblItems;
    TextView tvFileName;

    Spinner spnr_supplierType, spnrUOM, spnrTaxationType,spnr_supplytype;
    EditText et_inward_supplier_gstin ,et_inw_supplierPhn, et_inw_supplierAddress,et_inw_ItemBarcode,et_inw_hsnCode;
    EditText et_Inw_CGSTRate, et_Inw_SGSTRate, et_Inw_IGSTRate;
    AutoCompleteTextView autocompletetv_suppliername,autocomplete_inw_ItemName;
    Button btnAddSupplier;
    EditText et_inw_rate,et_inw_quantity;
    ArrayList<String> labelsSupplierName;

    // Variables
    ArrayAdapter<String> adapDeptCode, adapCategCode, adapKitCode, adapTax, adapDiscount;
    String strMenuCode, AMU,strImageUri = "";
    SQLiteDatabase db;
    String strUploadFilepath = "", strUserName = "";
    private List<String> labelsDept;
    private List<String> labelsCateg;
    private Toolbar toolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inward_item_entry);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.application_title_bar);
        TextView tvTitleText = (TextView) findViewById(R.id.tvTitleBarCaption);
        TextView tvTitleUserName = (TextView) findViewById(R.id.tvTitleBarUserName);
        TextView tvTitleDate = (TextView) findViewById(R.id.tvTitleBarDate);
        ActionBarUtils.goBack(this, findViewById(R.id.imgTitleBackIcon));
        ActionBarUtils.goHome(this, findViewById(R.id.imgTitleHomeIcon));
        ActionBarUtils.takeScreenshot(this, findViewById(R.id.imgTitleScreenshotIcon), findViewById(R.id.lnrItemMaster));
        tvTitleText.setText("Item_Inward Master");*/

        myContext = this;
        MsgBox = new MessageDialog(myContext);

        strUserName = getIntent().getStringExtra("USER_NAME");
        //tvTitleUserName.setText(strUserName.toUpperCase());
        Date d = new Date();
        CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
        //tvTitleDate.setText("Date : " + s);

        dbInwardItem = new DatabaseHandler(Inward_Item_Entry_Activity.this);
        myContext = this;
        btnUploadExcel = (Button) findViewById(R.id.buttonUploadExcel);
        btnSaveExcel = (Button) findViewById(R.id.buttonSaveExcel);
        com.wep.common.app.ActionBarUtils.setupToolbar(this,toolbar,getSupportActionBar(),"Item_Inward Master",strUserName," Date:"+s.toString());
        try {

            MsgBox = new AlertDialog.Builder(myContext);
            InitializeViewVariables();
            ResetItem();
            dbInwardItem.CreateDatabase();
            dbInwardItem.CloseDatabase();
            dbInwardItem.OpenDatabase();
            InitializeAdapters();
            DisplayItems();
            labelsSupplierName = new ArrayList<String>();
            loadSpinnerData();

            spnrTaxationType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String taxationtype = spnrTaxationType.getSelectedItem().toString();
                    if (taxationtype.equalsIgnoreCase("GST"))
                    {
                        et_Inw_IGSTRate.setEnabled(true);
                        et_Inw_IGSTRate.setText("0");
                        et_Inw_CGSTRate.setText("0");
                        et_Inw_SGSTRate.setText("0");
                        et_Inw_SGSTRate.setEnabled(true);
                        et_Inw_CGSTRate.setEnabled(true);

                    }else if (taxationtype.equalsIgnoreCase("Additional tax"))
                    {
                        et_Inw_IGSTRate.setText("0");
                        et_Inw_CGSTRate.setText("0");
                        et_Inw_SGSTRate.setText("0");
                        et_Inw_IGSTRate.setEnabled(false);
                        et_Inw_SGSTRate.setEnabled(false);
                        et_Inw_CGSTRate.setEnabled(true);
                    }
                    else {
                        MsgBox.setMessage("For NilRate, Exempt, nonGST, TaxRate is disabled")
                                .setPositiveButton("Ok",null)
                                .show();
                        et_Inw_IGSTRate.setText("0");
                        et_Inw_CGSTRate.setText("0");
                        et_Inw_SGSTRate.setText("0");
                        et_Inw_IGSTRate.setEnabled(false);
                        et_Inw_SGSTRate.setEnabled(false);
                        et_Inw_CGSTRate.setEnabled(false);
                    }

                }

                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            autocompletetv_suppliername.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String suppliername_str = autocompletetv_suppliername.getText().toString().toUpperCase();
                    Cursor supplierdetail_cursor = dbInwardItem.getSupplierDetails(suppliername_str);
                    if (supplierdetail_cursor!=null && supplierdetail_cursor.moveToFirst())
                    {
                        et_inward_supplier_gstin.setText(supplierdetail_cursor.getString(supplierdetail_cursor.getColumnIndex("GSTIN")));
                        et_inw_supplierPhn.setText(supplierdetail_cursor.getString(supplierdetail_cursor.getColumnIndex("SupplierPhone")));
                        et_inw_supplierAddress.setText(supplierdetail_cursor.getString(supplierdetail_cursor.getColumnIndex("SupplierAddress")));
                        String suppliertype = supplierdetail_cursor.getString(supplierdetail_cursor.getColumnIndex("SupplierType"));
                        if (suppliertype.equalsIgnoreCase("registered"))
                            spnr_supplierType.setSelection(1);
                        if (suppliertype.equalsIgnoreCase("compounding"))
                            spnr_supplierType.setSelection(2);
                        if (suppliertype.equalsIgnoreCase("unregistered"))
                            spnr_supplierType.setSelection(3);
                        loadAutoCompleteData_item();
                    }
                }
            });

            //loadSpinnerData1();
            btnUploadExcel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //startActivityForResult(new Intent(myContext, UploadFilePickerActivity.class), 1);
                    //tvFileName.setText(strUploadFilepath);
                }
            });

            btnSaveExcel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    /*try {
                        AssetManager manager = myContext.getAssets();
                        //Environment.getExternalStorageDirectory() + "/stock1.xls";
                        //InputStream inputStream = getResources().openRawResource(R.raw.itemdb);
                        if (strUploadFilepath.equalsIgnoreCase("")) {
                            Toast.makeText(getApplicationContext(), "No File Found", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(myContext, "Skipping Bad CSV Row", Toast.LENGTH_LONG).show();
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

                                                    Cursor crsrItem = dbItems.getbyItemName(colums[0].trim().toUpperCase());
                                                    if (crsrItem.moveToFirst()) {
                                                        //MsgBox.Show("", colums[0].trim() + " - " + crsrItem.getString(0));
                                                        int iRowId = dbItems.updateItem(Integer.parseInt(crsrItem.getString(0)),
                                                                colums[0].trim(), colums[0].trim(), "", 0, 0, 0,
                                                                Float.parseFloat(colums[1].trim()), Float.parseFloat(colums[2].trim()),
                                                                Float.parseFloat(colums[3].trim()), 0, 0, 0, 1,
                                                                2, 0, 0, 0, Float.parseFloat(colums[4].trim()), 0, 0, 0, "", 0);
                                                        Log.d("updateItem", "Updated Rows: " + String.valueOf(iRowId));
                                                        //Toast.makeText(getApplicationContext(), "Please wait... Importing Items...", Toast.LENGTH_LONG).show();
                                                    }
                                                    dialog.dismiss();
                                                    ClearItemTable();
                                                    DisplayItems();
                                                }
                                            })
                                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    ClearItemTable();
                                                    DisplayItems();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                } else {
                                    InsertItem(colums[0].trim(), colums[0].trim(), Float.parseFloat(colums[1].trim()),
                                            Float.parseFloat(colums[2].trim()), Float.parseFloat(colums[3].trim()),
                                            0, 0, 0, Float.parseFloat(colums[4].trim()), 0, 0, 0, 0, 0, 0, 0, 1,
                                            2, 0, 0, 0, "", "");
                                    //Toast.makeText(getApplicationContext(), "Please wait... Importing Items...", Toast.LENGTH_LONG).show();
                                }
                            }
                            Toast.makeText(getApplicationContext(), "Items Imported Successfully", Toast.LENGTH_LONG).show();
                            ClearItemTable();
                            DisplayItems();
                        }
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }*/
                }
            });

            ClearItemTable();
            DisplayItems();

            tvFileName.setPaintFlags(tvFileName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

           /* spnrSalesTax.setSelection(0);
            spnrAdditionalTax.setSelection(1);*/
            /*spnrSalesTax.setEnabled(false);
            spnrAdditionalTax.setEnabled(false);*/



        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            //dbInwardItem.CloseDatabase();
        }
    }

    private void InitializeViewVariables() {
        EditTextInputHandler etInputValidate = new EditTextInputHandler();

       /* txtLongName = (EditText) findViewById(R.id.tv_inw_ItemLongName);
        txtShortName = new EditText(myContext);
        txtBarcode = (EditText) findViewById(R.id.et_inw_ItemBarcode);
        txtHSNCode = (EditText) findViewById(R.id.et_inw_hsnCode);
        //etInputValidate.ValidateDecimalInput(txtDineIn1);

        txtRate = (EditText) findViewById(R.id.et_inw_rate);
        txtQuantity = (EditText) findViewById(R.id.et_inw_quantity);*/
        tvFileName = (TextView) findViewById(R.id.tvFileName);

        //tv_rate = (TextView) findViewById(R.id.et_inw_rate);


        /*spnrCategory = (Spinner) findViewById(R.id.spnrItemCategCode);
        spnrKitchen = (Spinner) findViewById(R.id.spnrItemKitchenCode);
        spnrOptionalTax1 = (Spinner) findViewById(R.id.spnrItemOptionalTax1);
        spnrOptionalTax2 = (Spinner) findViewById(R.id.spnrItemOptionalTax2);
        */
        /*spnrDiscount = (Spinner) findViewById(R.id.spnrItemDiscount);
        spnrSalesTax = (Spinner) findViewById(R.id.spnrItemSalesTax);
        spnrAdditionalTax = (Spinner) findViewById(R.id.spnrServiceTax);
*/
        /*chkPriceChange = (CheckBox) findViewById(R.id.chkPriceChange);
        chkDiscountEnable = (CheckBox) findViewById(R.id.chkDiscount);
        chkBillWithStock = (CheckBox) findViewById(R.id.chkBillwithStock);

        rbForwardTax = (RadioButton) findViewById(R.id.rbForwardTax);
        rbReverseTax = (RadioButton) findViewById(R.id.rbReverseTax);
*/
        btnAdd = (Button) findViewById(R.id.btnAddItem);
        btnEdit = (Button) findViewById(R.id.btnEditItem);

        tblItems = (TableLayout) findViewById(R.id.tblItem);

        imgItemImage = (ImageView) findViewById(R.id.imgItemImage);
        imgItemImage.setImageResource(R.drawable.img_noimage);

        spnr_supplierType = (Spinner)findViewById(R.id.spnr_supplierType);
        spnrUOM= (Spinner)findViewById(R.id.spnrUOM);
        spnrTaxationType = (Spinner)findViewById(R.id.spnrTaxationType);
        spnr_supplytype = (Spinner)findViewById(R.id.spnr_supplytype);
        et_inward_supplier_gstin = (EditText) findViewById(R.id.et_inward_supplier_gstin);
        et_Inw_CGSTRate = (EditText) findViewById(R.id.et_Inw_CGSTRate);
        et_Inw_SGSTRate = (EditText) findViewById(R.id.et_Inw_SGSTRate);
        et_Inw_IGSTRate = (EditText) findViewById(R.id.et_Inw_IGSTRate);
        et_inw_rate = (EditText) findViewById(R.id.et_inw_rate);
        et_inw_quantity = (EditText) findViewById(R.id.et_inw_quantity);
        autocompletetv_suppliername = (AutoCompleteTextView) findViewById(R.id.autocompletetv_suppliername);

        autocomplete_inw_ItemName= (AutoCompleteTextView) findViewById(R.id.autocomplete_inw_ItemName);
        et_inw_supplierPhn = (EditText) findViewById(R.id.et_inw_supplierPhn);
        et_inw_supplierAddress = (EditText) findViewById(R.id.et_inw_supplierAddress);
        et_inw_ItemBarcode = (EditText) findViewById(R.id.et_inw_ItemBarcode);
        et_inw_hsnCode = (EditText) findViewById(R.id.et_inw_hsnCode);
        btnAddSupplier = (Button) findViewById (R.id.btnAddSupplier);


    }

    private void InitializeAdapters() {
        Log.d("Functions", "InitializeAdapters");
        // Cursor variable
        Cursor crsrAdapterData;

        // Initialize adapters
        /*adapDeptCode = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapDeptCode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapCategCode = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapCategCode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapKitCode = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapKitCode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
*/
        /*adapTax = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapTax.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapDiscount = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapDiscount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
*/
        // Assign adapters to spinners
        /*spnrDepartment.setAdapter(adapDeptCode);
        //spnrCategory.setAdapter(adapCategCode);
        spnrKitchen.setAdapter(adapKitCode);*/
  /*      spnrSalesTax.setAdapter(adapTax);
        spnrAdditionalTax.setAdapter(adapTax);
        spnrDiscount.setAdapter(adapDiscount);
  */      /*spnrOptionalTax1.setAdapter(adapTax);
        spnrOptionalTax2.setAdapter(adapTax);
        */


        // Add Kitchen to adapter
        /*crsrAdapterData = null;
        crsrAdapterData = dbInwardItem.getAllKitchen();
        Log.d("Kitchen", "Rows:" + String.valueOf(crsrAdapterData.getCount()));
        if (crsrAdapterData.moveToFirst()) {
            do {
                adapKitCode.add(crsrAdapterData.getString(1));
            } while (crsrAdapterData.moveToNext());
        }*/

        // Add Tax to adapter
       /* crsrAdapterData = null;
        crsrAdapterData = dbInwardItem.getAllTaxConfig();
        Log.d("Tax", "Rows:" + String.valueOf(crsrAdapterData.getCount()));
        if (crsrAdapterData.moveToFirst()) {
            do {
                adapTax.add(crsrAdapterData.getString(1));
            } while (crsrAdapterData.moveToNext());
        }

        // Add Tax to adapter
        crsrAdapterData = null;
        crsrAdapterData = dbInwardItem.getAllDiscountConfig();
        Log.d("Discount", "Rows:" + String.valueOf(crsrAdapterData.getCount()));
        if (crsrAdapterData.moveToFirst()) {
            do {
                adapDiscount.add(crsrAdapterData.getString(1));
            } while (crsrAdapterData.moveToNext());
        }*/
    }

    @SuppressWarnings("deprecation")
    private void DisplayItems() {
        Cursor crsrItems = null;

        Cursor supplier_crsr = dbInwardItem.getallsupplier();
        while (supplier_crsr!=null && supplier_crsr.moveToNext())
        {
            final String supplierType = supplier_crsr.getString(supplier_crsr.getColumnIndex("SupplierType"));
            String supplierName = supplier_crsr.getString(supplier_crsr.getColumnIndex("SupplierName"));
            String suppliergstin = supplier_crsr.getString(supplier_crsr.getColumnIndex("GSTIN"));
            String supplierPhone = supplier_crsr.getString(supplier_crsr.getColumnIndex("SupplierPhone"));
            String supplierAddress = supplier_crsr.getString(supplier_crsr.getColumnIndex("SupplierAddress"));
            int suppliercode = supplier_crsr.getInt(supplier_crsr.getColumnIndex("SupplierCode"));
            crsrItems = dbInwardItem.getLinkedMenuCodeForSupplier(suppliercode);
            TableRow rowItems = null;

            TextView tvSno, tvMenuCode, tvHSN, tvLongName, tvShortName, tvDineIn1, tvDineIn2, tvDineIn3, tvTakeAway, tvPickUp, tvDelivery,
                    tvStock, tvPriceChange, tvDiscountEnable, tvBillWithStock, tvTaxType, tvDeptCode, tvCategCode,
                    tvKitchenCode, tvSalesTaxId, tvServicetax, tvAdditionalTaxId, tvOptionalTaxId1, tvOptionalTaxId2, tvDiscountId,
                    tvItemBarcode, tvImageUri, tvSpace, tvDeptName, tvCategName;
            TextView tvSupplierType, tvSuppliergstin, tvSupplierName, tvSupplierPhone, tvSupplierAddress,tvSupplyType,tvTaxationType;

            TextView tvQuantity, tvRate, tvAMU, tvIGSTRate, tvIGSTAmount, tvSGSTRate, tvSGSTAmount, tvCGSTRate, tvCGSTAmount;
            ImageView imgIcon;
            ImageButton btnItemDelete;
            int i = 1;
            if (crsrItems.moveToFirst()) {
                do {

                    rowItems = new TableRow(myContext);
                    rowItems.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    //rowItems.setBackgroundResource(R.drawable.row_background);
                    rowItems.setBackgroundResource(R.drawable.border_itemdatabase);

                    tvSno = new TextView(myContext);
                    tvSno.setTextSize(18);
                    tvSno.setGravity(1);
                    tvSno.setWidth(85);
                    tvSno.setText(String.valueOf(i));
                    rowItems.addView(tvSno);
                    i++;

                    tvSupplierType = new TextView(myContext);
                    tvSupplierType.setTextSize(18);
                    tvSupplierType.setWidth(300);
                    tvSupplierType.setText(supplierType);
                    rowItems.addView(tvSupplierType);

                    tvSupplierName = new TextView(myContext);
                    tvSupplierName.setTextSize(18);
                    tvSupplierName.setWidth(300);
                    tvSupplierName.setText(supplierName);
                    rowItems.addView(tvSupplierName);

                    tvSuppliergstin = new TextView(myContext);
                    tvSuppliergstin.setTextSize(18);
                    tvSuppliergstin.setWidth(300);
                    tvSuppliergstin.setText(suppliergstin);
                    rowItems.addView(tvSuppliergstin);

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

                    tvHSN = new TextView(myContext);
                    tvHSN.setTextSize(18);
                    tvHSN.setWidth(200);
                    tvHSN.setText(crsrItems.getString(crsrItems.getColumnIndex("HSNCode")));
                    rowItems.addView(tvHSN);

                    tvRate = new TextView(myContext);
                    tvRate.setGravity(Gravity.RIGHT|Gravity.END);
                    tvRate.setWidth(55);
                    tvRate.setText(crsrItems.getString(crsrItems.getColumnIndex("Rate")));
                    rowItems.addView(tvRate);

                    tvQuantity = new TextView(myContext);
                    tvQuantity.setGravity(Gravity.RIGHT|Gravity.END);
                    tvQuantity.setWidth(130);
                    tvQuantity.setText(crsrItems.getString(crsrItems.getColumnIndex("Quantity")));
                    rowItems.addView(tvQuantity);

                    tvAMU = new TextView(myContext);
                    tvAMU.setGravity(Gravity.RIGHT);
                    tvAMU.setWidth(140);
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

                    tvIGSTRate = new TextView(myContext);
                    tvIGSTRate.setText(crsrItems.getString(crsrItems.getColumnIndex("IGSTRate")));
                    rowItems.addView(tvIGSTRate);

                    tvCGSTRate = new TextView(myContext);
                    tvCGSTRate.setText(crsrItems.getString(crsrItems.getColumnIndex("CGSTRate")));
                    rowItems.addView(tvCGSTRate);

                    tvSGSTRate = new TextView(myContext);
                    tvSGSTRate.setText(crsrItems.getString(crsrItems.getColumnIndex("SGSTRate")));
                    rowItems.addView(tvSGSTRate);

                    // For Space purpose
                    tvSpace = new TextView(myContext);
                    tvSpace.setText("                ");
                    rowItems.addView(tvSpace);

                    // Delete
                    int res = getResources().getIdentifier("delete", "drawable", this.getPackageName());
                    btnItemDelete = new ImageButton(myContext);
                    btnItemDelete.setImageResource(res);
                    btnItemDelete.setLayoutParams(new TableRow.LayoutParams(60, 40));
                    btnItemDelete.setOnClickListener(mListener);
                    rowItems.addView(btnItemDelete);

                    // taxationtype
                    tvTaxationType =  new TextView(myContext);
                    tvTaxationType.setText(crsrItems.getString(crsrItems.getColumnIndex("TaxationType")));
                    rowItems.addView(tvTaxationType);

                    rowItems.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub

                            if (String.valueOf(v.getTag()) == "TAG") {
                                TableRow Row = (TableRow) v;

                                //TextView MenuCode = (TextView) Row.getChildAt(1);
                                TextView SuplierType  = (TextView) Row.getChildAt(1);
                                TextView Suppliername = (TextView) Row.getChildAt(2);
                                TextView SupplierGSTIN = (TextView) Row.getChildAt(3);
                                TextView Supplierphn = (TextView) Row.getChildAt(4);
                                TextView SupplierAddr = (TextView) Row.getChildAt(5);
                                TextView MenuCode = (TextView) Row.getChildAt(6);
                                TextView LongName = (TextView) Row.getChildAt(7);
                                TextView SupplyType = (TextView) Row.getChildAt(8);
                                TextView HSN = (TextView) Row.getChildAt(9);
                                TextView Rate = (TextView) Row.getChildAt(10);
                                TextView Quantity = (TextView) Row.getChildAt(11);
                                TextView Uom = (TextView) Row.getChildAt(12);
                                TextView IGSTRate = (TextView) Row.getChildAt(15);
                                TextView CGSTRate = (TextView) Row.getChildAt(16);
                                TextView SGSTRate = (TextView) Row.getChildAt(17);
                                TextView TaxationType = (TextView)Row.getChildAt(20);

                                strMenuCode = MenuCode.getText().toString();
                                String suppliertype = SuplierType.getText().toString();
                                if (suppliertype.equalsIgnoreCase("Registered"))
                                {
                                    spnr_supplierType.setSelection(1);
                                }else  if (suppliertype.equalsIgnoreCase("compounding"))
                                {
                                    spnr_supplierType.setSelection(2);
                                }else
                                {
                                    spnr_supplierType.setSelection(3);
                                }
                                et_inward_supplier_gstin.setText(SupplierGSTIN.getText().toString());
                                autocompletetv_suppliername.setText(Suppliername.getText().toString());
                                et_inw_supplierPhn.setText(Supplierphn.getText().toString());
                                et_inw_supplierAddress.setText(SupplierAddr.getText().toString());
                                String supplyType = SupplyType.getText().toString();
                                if (supplyType.equalsIgnoreCase("g"))
                                    spnr_supplytype.setSelection(0);
                                else
                                    spnr_supplytype.setSelection(1);
                                autocomplete_inw_ItemName.setText(LongName.getText());
                                et_inw_hsnCode.setText(HSN.getText().toString());
                                et_inw_rate.setText(Rate.getText().toString());
                                et_inw_quantity.setText(Quantity.getText().toString());
                                //String uom = Uom.getText().toString();
                                String taxationtype  = TaxationType.getText().toString();
                                if (taxationtype.equalsIgnoreCase("gst"))
                                {
                                    spnrTaxationType.setSelection(0);
                                }
                                else if (taxationtype.equalsIgnoreCase("additionaltax"))
                                    spnrTaxationType.setSelection(1);
                                else if (taxationtype.equalsIgnoreCase("Exempt"))
                                    spnrTaxationType.setSelection(2);
                                else if (taxationtype.equalsIgnoreCase("NilRate"))
                                    spnrTaxationType.setSelection(3);
                                else if (taxationtype.equalsIgnoreCase("nonGST"))
                                    spnrTaxationType.setSelection(4);

                                et_Inw_IGSTRate.setText(IGSTRate.getText());
                                et_Inw_CGSTRate.setText(CGSTRate.getText());
                                et_Inw_SGSTRate.setText(SGSTRate.getText());

                                imgItemImage.setImageURI(null);
                                if (!strImageUri.equalsIgnoreCase("")) { // &&
                                    // strImageUri.contains("\\")){
                                    imgItemImage.setImageURI(Uri.fromFile(new File(strImageUri)));
                                } else {
                                    imgItemImage.setImageResource(R.drawable.img_noimage);
                                }
                                btnAdd.setEnabled(false);
                                btnEdit.setEnabled(true);
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

    private View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(final View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to Delete this Item")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            TableRow tr = (TableRow) v.getParent();
                            TextView ItemNumber = (TextView) tr.getChildAt(18);
                            long lResult = dbInwardItem.DeleteItem_Inw(ItemNumber.getText().toString());
                            //MsgBox.Show("", "Item Deleted Successfully");
                            Toast.makeText(myContext, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();

                            ClearItemTable();
                            DisplayItems();

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
    };

    private boolean IsItemExists(String ItemFullName) {
        boolean isItemExists = false;
        String strItem = "";
        TextView Item;

        for (int i = 0; i < tblItems.getChildCount(); i++) {

            TableRow Row = (TableRow) tblItems.getChildAt(i);

            if (Row.getChildAt(0) != null) {
                Item = (TextView) Row.getChildAt(1);

                strItem = Item.getText().toString();

                Log.v("ItemActivity", "Item:" + strItem.toUpperCase() + " New Item:" + ItemFullName.toUpperCase());

                if (strItem.toUpperCase().equalsIgnoreCase(ItemFullName.toUpperCase())) {
                    isItemExists = true;
                    break;
                }
            }
        }

        return isItemExists;
    }

    private void InsertItem(int suppliercode, String suppliername, String itemName, String strbarCode, String hsn, float ratef,
                            float quantity, String mou, String ImageUri, String taxationtype ,float IGSTRate, float CGSTRate,float SGSTRate,
                            String supplytype) {

        long lRowId = 0;

        Item objItem = new Item();
        objItem.setsuppliercode(suppliercode);
        objItem.setsupplierName(suppliername);
        objItem.setItemname(itemName);
        objItem.setItemBarcode(strbarCode);
        objItem.setHSNCode(hsn);
        objItem.setRate(ratef);
        objItem.setQuantity(quantity);
        objItem.setMOU(mou);
        objItem.setImageId(ImageUri);
        objItem.setTaxationType(taxationtype);
        objItem.setIGSTRate(IGSTRate);
        objItem.setCGSTRate(CGSTRate);
        objItem.setSGSTRate(SGSTRate);
        objItem.setSupplyType(supplytype);



        lRowId = dbInwardItem.addItem_Inw(objItem);

        Log.d("Item", "Row Id:" + String.valueOf(lRowId));
    }

    private void ReadData(int Type) {
        String itemname = "", strShortName = "", strBarcode = "",mou ="";
        int iDeptCode = 0, iCategCode = 0, iKitchenCode = 0, iSalesTaxId = 0, iAdditionalTaxId = 0, iOptionalTaxId1 = 0,
                iOptionalTaxId2 = 0, iDiscountId = 0, iPriceChange = 0, iDiscountEnable = 0, iBillWithStock = 0,
                iTaxType = 0;
        float fDineIn1 = 0, fDineIn2 = 0, fDineIn3 = 0, fTakeAway = 0, fPickUp = 0, fDelivery = 0, fStock = 0;
        String HSNCode = "",TaxationType = ""; //, AMU = "";
        float rate =0, quantity =0,igstRate =0,igstAmount =0, cgstRate =0, cgstAmount=0, sgstRate =0, sgstAmount =0;


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
        HSNCode = et_inw_hsnCode.getText().toString();
        igstRate = Float.parseFloat(et_Inw_IGSTRate.getText().toString());
        cgstRate = Float.parseFloat(et_Inw_CGSTRate.getText().toString());
        sgstRate = Float.parseFloat(et_Inw_SGSTRate.getText().toString());
        mou = spnrUOM.getSelectedItem().toString();
        TaxationType = spnrTaxationType.getSelectedItem().toString();
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
                InsertItem( suppliercode,  suppliername, itemname,  strBarcode,HSNCode,  rate,  quantity, mou, ImageUri, TaxationType
                        ,igstRate, cgstRate,sgstRate,supplytype);

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
                objItem.setHSNCode(HSNCode);
                objItem.setRate(rate);
                objItem.setQuantity(quantity);
                objItem.setMOU(mou);
                objItem.setImageId(ImageUri);
                objItem.setTaxationType(TaxationType);
                objItem.setIGSTRate(igstRate);
                objItem.setCGSTRate(cgstRate);
                objItem.setSGSTRate(sgstRate);
                objItem.setSupplyType(supplytype);
                objItem.setMenuCode(menucode);


                int iRowId = dbInwardItem.updateItem_Inw(objItem);
                Log.d("updateCategory", "Updated Rows: " + String.valueOf(iRowId));
            }
        }

    }

    public void AddSupplier(View v)
    {
        labelsSupplierName = dbInwardItem.getAllSupplierName();

        MsgBox.setTitle("Incomplete Information")
                .setPositiveButton("Ok", null);
        String supplierType_str = spnr_supplierType.getSelectedItem().toString();
        String suppliername_str = autocompletetv_suppliername.getText().toString().toUpperCase();
        String supplierphn_str = et_inw_supplierPhn.getText().toString();
        String suppliergstin_str = et_inward_supplier_gstin.getText().toString();
        String supplieraddress_str = et_inw_supplierAddress.getText().toString();

        for (String supplier : labelsSupplierName)
        {
            if (suppliername_str.equalsIgnoreCase(supplier))
            {
                MsgBox.setTitle("Warning")
                        .setMessage("Supplier already present in list")
                        .setPositiveButton("OK",null)
                        .show();
                return;
            }
        }
        if (supplierType_str.equalsIgnoreCase("Supplier type"))
        {
            MsgBox.setMessage("Please choose supplier type")
                    .show();
        }else if (supplierType_str.equalsIgnoreCase("registered") && suppliergstin_str.equals(""))
        {
            MsgBox.setMessage(" Please fill gstin no for registered Supplier")
                    .show();

        }else if (suppliername_str.equals("") || supplieraddress_str.equals("") || supplierphn_str.equals(""))
        {
            MsgBox.setMessage("Please fill all details of Supplier")
                    .show();
        }
        else
        {
            long l = dbInwardItem.saveSupplierDetails(supplierType_str, suppliergstin_str,suppliername_str, supplierphn_str, supplieraddress_str);
            if (l>0)
            {
                Log.d("Inward_Item_Entry"," Supplier details saved at "+l);
                Toast.makeText(myContext, "Supplier details saved at "+l, Toast.LENGTH_SHORT).show();
            }
        }
        try
        {
            labelsSupplierName = dbInwardItem.getAllSupplierName();
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    labelsSupplierName);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            autocompletetv_suppliername.setAdapter(dataAdapter);
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
        autocomplete_inw_ItemName.setText("");
        et_inw_rate.setText("");
        et_inw_quantity.setText("");
        spnr_supplierType.setSelection(0);
        et_inw_supplierAddress.setText("");
        et_inward_supplier_gstin.setText("");
        et_inw_supplierPhn.setText("");
        et_inw_supplierAddress.setText("");
        autocompletetv_suppliername.setText("");
        btnAddSupplier.setEnabled(true);
        et_inw_hsnCode.setText("");
        spnrUOM.setSelection(0);
        spnrTaxationType.setSelection(0);
        et_Inw_SGSTRate.setText("0");
        et_Inw_CGSTRate.setText("0");
        et_Inw_IGSTRate.setText("0");
        btnAdd.setEnabled(true);
        btnEdit.setEnabled(false);

    }

    public void Browse1 (View v) {
        startActivityForResult(new Intent(myContext, FilePickerActivity.class), 1);
    }

    public void AddItem1 (View v)
    {
        labelsSupplierName = dbInwardItem.getAllSupplierName();
        String suppliername_str = autocompletetv_suppliername.getText().toString().toUpperCase();
        int flag =0;
        for (String supplier : labelsSupplierName)
        {
            if (supplier.equalsIgnoreCase(suppliername_str))
            {
                flag =1;
                break;
            }
        }
        if (flag ==0)
        {
            MsgBox.setTitle("Warning")
                    .setMessage("Supplier not list. Please add supplier first")
                    .setPositiveButton("OK",null)
                    .show();
            return;
        }
        if (autocompletetv_suppliername.getText().toString().equalsIgnoreCase("")) {
            MsgBox.setTitle("Warning")
                    .setMessage("Please enter item name")
                    .show();
            return;
        }



        if (et_Inw_IGSTRate.getText().toString().equalsIgnoreCase("")) {
            et_Inw_IGSTRate.setText("0");
        }

        if (et_Inw_CGSTRate.getText().toString().equalsIgnoreCase("")) {
            et_Inw_CGSTRate.setText("0");
        }

        if (et_Inw_SGSTRate.getText().toString().equalsIgnoreCase("")) {
            et_Inw_SGSTRate.setText("0");
        }

        if (et_inw_quantity.getText().toString().equalsIgnoreCase("")) {
            et_inw_quantity.setText("0");
        }
        if (et_inw_rate.getText().toString().equalsIgnoreCase("")) {
            et_inw_rate.setText("0");
        }

        try {
            ReadData(1); // 1 - addItem
            Toast.makeText(myContext, "Item Added Successfully", Toast.LENGTH_LONG).show();
            ResetItem();
            ClearItemTable();
            DisplayItems();
        } catch (Exception exp) {
            Toast.makeText(myContext, exp.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void EditItem1(View v) {
        if (autocomplete_inw_ItemName.getText().toString().equalsIgnoreCase("")) {
            //|| txtShortName.getText().toString().equalsIgnoreCase("")) {
            /*MsgBox.Show("Warning", "Please enter item full name");*/
            Toast.makeText(myContext, "Please enter item full name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (et_Inw_IGSTRate.getText().toString().equalsIgnoreCase("")) {
            et_Inw_IGSTRate.setText("0");
        }
        if (et_Inw_CGSTRate.getText().toString().equalsIgnoreCase("")) {
            et_Inw_CGSTRate.setText("0");
        }

        if (et_Inw_SGSTRate.getText().toString().equalsIgnoreCase("")) {
            et_Inw_SGSTRate.setText("0");
        }

        if (et_inw_quantity.getText().toString().equalsIgnoreCase("")) {
            et_inw_quantity.setText("0");
        }

        if (et_inw_rate.getText().toString().equalsIgnoreCase("")) {
            et_inw_rate.setText("0");
        }


        try {
            ReadData(2); // 2 - updateItem
            Toast.makeText(myContext, "Item Updated Successfully", Toast.LENGTH_LONG).show();
            ResetItem();
            ClearItemTable();
            DisplayItems();
        } catch (Exception exp) {
            Toast.makeText(myContext, exp.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void ClearItem1(View v) {
        ResetItem();
        loadSpinnerData();
        //loadSpinnerData1();
    }

    public void CloseItem1(View v) {
        dbInwardItem.CloseDatabase();
        this.finish();
    }


    private void loadAutoCompleteData_item() {

        String suppliername_str = autocompletetv_suppliername.getText().toString().toUpperCase();
        List<String> itemlist = dbInwardItem.getitemlist_inward(suppliername_str);

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,itemlist);

        dataAdapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);

        // attaching data adapter to spinner
        autocomplete_inw_ItemName.setAdapter(dataAdapter1);

    }

    private void loadSpinnerData() {


        labelsSupplierName = dbInwardItem.getAllSupplierName();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                labelsSupplierName);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        autocompletetv_suppliername.setAdapter(dataAdapter);

        //labelsDept = dbItems.getAllDeptforCateg();
        // Creating adapter for spinner



        /*ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, amuList);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spnrAMU.setAdapter(dataAdapter);*/
    }

    private void loadSpinnerData1() {
        /*labelsCateg = dbItems.getAllCategforDept();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labelsCateg);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spnrCategory.setAdapter(dataAdapter);*/
    }

    @Override
    public void onHomePressed() {
        ActionBarUtils.navigateHome(this);
    }
}
