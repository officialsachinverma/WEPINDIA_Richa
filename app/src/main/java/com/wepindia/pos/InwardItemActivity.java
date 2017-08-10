package com.wepindia.pos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mswipetech.wisepad.sdktest.view.ApplicationData;
import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.WepBaseActivity;
import com.wep.common.app.models.ItemInward;
import com.wep.common.app.models.ItemStock;
import com.wep.common.app.views.WepButton;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.adapters.ItemInwardAdapter;
import com.wepindia.pos.utils.ActionBarUtils;
import com.wepindia.pos.utils.StockInwardMaintain;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

public class InwardItemActivity extends WepBaseActivity {

    public static final int RESULT_OK      = -1;
    int ALL = 0;
    int SUPPLIERWISE = 1;
    int ITEMWISE = 2;
    int sema_display=0;


    Toolbar toolbar;
    Context myContext;
    DatabaseHandler dbInwardItem;
    public AlertDialog.Builder MsgBox;
    public MessageDialog MsgBox1;
    WepButton btnAdd, btnEdit, btnUploadExcel, btnSaveExcel, btnCloseItem, btnClearItem, btnResetQuantity;
    Spinner spnrUOM,spnr_supplytype;
    EditText et_inw_ItemBarcode,et_inw_HSNCode;
    AutoCompleteTextView autocomplete_inw_ItemName;
    EditText et_inw_averagerate_entered,et_inw_quantity;
    EditText et_Inw_SGSTRate, et_Inw_CGSTRate,et_Inw_IGSTRate,et_Inw_cessRate;
    TextView tvMenuCode;
    ListView lstInwardItem;
    TextView  tv_AverageRate,et_Inw_Amount,tvFileName;
    private BufferedReader mBufferReader;
    private boolean mFlag;
    //ImageView imgItemImage;
    //TextView tvFileName;

    ArrayList<ItemInward> InwardItemList;
    ItemInwardAdapter InwardItemAdapter;
    ArrayList<String> itemlist;
    String businessDate="";
    String itemname_clicked;

    private double IGSTRate = 0;
    private double CGSTRate = 0;
    private double SGSTRate = 0;
    private double cessRate = 0;
    private double IGSTAmt = 0;
    private double CGSTAmt = 0;
    private double SGSTAmt = 0;
    private double cessAmt = 0;

    private String mUserCSVInvalidValue = "";
    private int mCheckCSVValueType;

    private int mMenuCode;
    private String mItemName;
    private String mSupplyType;
    private double mRate;
    private double mQuantity;
    private String mUOM;
    private double mCGSTRate;
    private double mSGSTRate;
    private double mIGSTRate;
    private double mCESSRate;

    private String mCurrentTime;
    private final int CHECK_INTEGER_VALUE = 0;
    private final int CHECK_DOUBLE_VALUE = 1;
    private final int CHECK_STRING_VALUE = 2;

    int count =1;
    float rate_prev_for_touched_item =0;
    String strMenuCode, AMU,strImageUri = "";
    String strUploadFilepath = "", strUserName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inward_item);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String userName = ApplicationData.getUserName(this);
        Date d = new Date();
        CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
        InwardItemAdapter = null;
        try{
            InwardItemList =null;
            InwardItemAdapter = null;
            myContext = this;

            MsgBox = new MessageDialog(myContext);
            MsgBox1 = new MessageDialog(myContext);
            dbInwardItem = new DatabaseHandler(this);

            dbInwardItem.CloseDatabase();
            dbInwardItem.CreateDatabase();
            dbInwardItem.OpenDatabase();

            Cursor cursor = dbInwardItem.getCurrentDate();
            if(cursor!=null && cursor.moveToFirst())
            {
                businessDate= cursor.getString(cursor.getColumnIndex("BusinessDate"));
            }

            InitializeViewVariables();
            onClickActions();
            ResetItem();
            loadSpinnerData();
            count =1;
            DisplayItems(); // display all data

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            com.wep.common.app.ActionBarUtils.setupToolbar(this,toolbar,getSupportActionBar(),"Item Inward",userName," Date:"+s.toString());
        }
    }

    void onClickActions()
    {
        try{
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddItem1(v);
                }
            });
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditItem1(v, 0);
                }
            });
            /*btnResetQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ResetQuantity(v);
                }
            });*/
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

            lstInwardItem.setOnItemClickListener(ListViewClickEvent);
            autocomplete_inw_ItemName.setOnTouchListener(new View.OnTouchListener(){
                //@Override
                public boolean onTouch(View v, MotionEvent event){
                    autocomplete_inw_ItemName.showDropDown();
                    return false;
                }
            });

            et_Inw_SGSTRate.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String SGST = et_Inw_SGSTRate.getText().toString();
                    String CGST = et_Inw_CGSTRate.getText().toString();
                    if(SGST.equals("")){
                        //et_Inw_SGSTRate.setText("0");
                        SGST = ("0");
                    }
                    if(CGST.equals("")){
                        //et_Inw_CGSTRate.setText("0");
                        CGST = ("0");}

                    double sgst_d = Double.parseDouble(SGST);
                    double cgst_d = Double.parseDouble(CGST);
                    double igst_d = sgst_d+cgst_d;
                    et_Inw_IGSTRate.setText(String.format("%.2f",igst_d));

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
                    String SGST = et_Inw_SGSTRate.getText().toString();
                    String CGST = et_Inw_CGSTRate.getText().toString();
                    if(CGST.equals("")) {
                        //et_Inw_CGSTRate.setText("0");
                        CGST = ("0");
                    }
                    if(SGST.equals("")) {
                        //et_Inw_SGSTRate.setText("0");
                        SGST = ("0");
                    }

                    double sgst_d = Double.parseDouble(SGST);
                    double cgst_d = Double.parseDouble(CGST);
                    double igst_d = sgst_d+cgst_d;
                    et_Inw_IGSTRate.setText(String.format("%.2f",igst_d));

                }
            });

            autocomplete_inw_ItemName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String itemname = autocomplete_inw_ItemName.getText().toString();
                    sema_display = ITEMWISE;
                    count =1;
                    Cursor itemDetail = dbInwardItem.getItemDetail_inward(itemname);
                    if(itemDetail!=null && itemDetail.moveToNext())
                    {
                        tvMenuCode.setText(itemDetail.getString(itemDetail.getColumnIndex("MenuCode")));
                        et_inw_ItemBarcode.setText(itemDetail.getString(itemDetail.getColumnIndex("ItemBarcode")));
                        et_inw_HSNCode.setText(itemDetail.getString(itemDetail.getColumnIndex("HSNCode")));
                        et_inw_averagerate_entered.setText(String.format("%.2f,",itemDetail.getDouble(itemDetail.getColumnIndex("AverageRate"))));
                        et_inw_quantity.setText(String.format("%.2f,",itemDetail.getDouble(itemDetail.getColumnIndex("Quantity"))));

                        et_Inw_CGSTRate.setText(String.format("%.2f",itemDetail.getDouble(itemDetail.getColumnIndex("CGSTRate"))));
                        et_Inw_SGSTRate.setText(String.format("%.2f",itemDetail.getDouble(itemDetail.getColumnIndex("SGSTRate"))));
                        et_Inw_IGSTRate.setText(String.format("%.2f",itemDetail.getDouble(itemDetail.getColumnIndex("IGSTRate"))));
                        et_Inw_cessRate.setText(String.format("%.2f",itemDetail.getDouble(itemDetail.getColumnIndex("cessRate"))));

                        if (itemDetail.getString(itemDetail.getColumnIndex("SupplyType")).equalsIgnoreCase("g"))
                            spnr_supplytype.setSelection(0);
                        else
                            spnr_supplytype.setSelection(1);

                        String uom_temp = itemDetail.getString(itemDetail.getColumnIndex("UOM"));
                        String uom = "(" + uom_temp + ")";
                        int index = getIndex(uom);
                        spnrUOM.setSelection(index);

                        btnAdd.setEnabled(false);
                        btnEdit.setEnabled(true);
                    }

                }
            });
            btnUploadExcel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(myContext, FilePickerActivity.class);
                    intent.putExtra("contentType", "csv");
                    startActivityForResult(intent, FilePickerActivity.FILE_PICKER_CODE);
                }
            });
            btnSaveExcel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        AssetManager manager = myContext.getAssets();
                        if (strUploadFilepath.equalsIgnoreCase("")) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_found_file), Toast.LENGTH_SHORT).show();
                        } else {
                            String path = strUploadFilepath;
                            FileInputStream inputStream = new FileInputStream(path);
                            mBufferReader = new BufferedReader(new InputStreamReader(inputStream));

                            Cursor cursor = dbInwardItem.getCurrentDate();

                            setCSVFileToDB(InwardItemList);
                        }
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    } finally {
                        tvFileName.setText(getResources().getString(R.string.select_filename));
                        strUploadFilepath = "";
                    }
                }
            });

            /*et_inw_averagerate_entered.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                public void afterTextChanged(Editable s) {
                    try{
                        double rate =0;
                        double quantity =0;
                        String quantity_str =et_inw_quantity.getText().toString();
                        String rate_str = et_inw_averagerate_entered.getText().toString();
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
                        MsgBox.Show("Error  ", ex.getMessage());
                    }

                }
            });*/
            /*et_inw_quantity.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                public void afterTextChanged(Editable s) {
                    try{
                        double rate =0;
                        double quantity =0;
                        String quantity_str =et_inw_quantity.getText().toString();
                        String rate_str = et_inw_averagerate_entered.getText().toString();
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
                        MsgBox.Show("Error", e.getMessage());
                    }

                }
            });*/
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

                                                        Cursor crsrItem = dbSupplierItemLink.getbyItemName(colums[0].trim().toUpperCase());
                                                        if (crsrItem.moveToFirst()) {
                                                            //MsgBox.Show("", colums[0].trim() + " - " + crsrItem.getString(0));
                                                            *//*int iRowId = dbSupplierItemLink.updateItem_inward(Integer.parseInt(crsrItem.getString(0)),
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
                                                        DisplayItems();
                                                    }
                                                })
                                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                        ClearItemTable();
                                                        count =1;
                                                        DisplayItems();
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
                                DisplayItems();
                            }
                        } catch (Exception e) {
                            Toast.makeText(myContext, e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                });*/



            //tvFileName.setPaintFlags(tvFileName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

               /* spnrSalesTax.setSelection(0);
                spnrAdditionalTax.setSelection(1);*/
                /*spnrSalesTax.setEnabled(false);
                spnrAdditionalTax.setEnabled(false);*/

            /*et_Inw_SGSTRate.addTextChangedListener(new TextWatcher() {
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
            });*/

    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    }


    /**
     * Display exiting data as well as new data from data base
     *
     * @param inwardItemList - List of ItemInward
     */

    private void setCSVFileToDB(ArrayList<ItemInward> inwardItemList) {

        if (inwardItemList.size() > 0) {
            showCSVAlertMessage();
        } else {
            downloadCSVData();
        }
    }

    /**
     * Ask User permission to dispaly old data or new data from CSV file
     */

    private void showCSVAlertMessage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                .setIcon(R.drawable.ic_launcher)
                .setTitle(getResources().getString(R.string.replace_item))
                .setMessage(getResources().getString(R.string.are_you_sure_you_want_replace_all_exiting_item))
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbInwardItem.clearInwardItemdatabase();
                        dbInwardItem.clearInwardStock(businessDate);
                        dbInwardItem.clearSupplierLinkage();
                        InwardItemList.clear();
                        InwardItemAdapter.notifyDataSetChanged(InwardItemList);
                        downloadCSVData();

                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        DisplayItems();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Download all data in Background then it will be return to UI
     */

    private void downloadCSVData() {
        new AsyncTask<Void, Void, Void>() {
            ProgressDialog pd;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(InwardItemActivity.this);
                pd.setMessage(getResources().getString(R.string.loading));
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                readCSVValue(businessDate);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                try {
                    ResetItem();
                    if(mFlag== true){
                        MsgBox1.Show("Error",mUserCSVInvalidValue );
                        dbInwardItem.clearInwardItemdatabase();
                        InwardItemList.clear();
                        InwardItemAdapter.notifyDataSetChanged(InwardItemList);
                    }else {
                        DisplayItems();
                        Toast.makeText(InwardItemActivity.this, getResources().getString(R.string.item_import_successfully), Toast.LENGTH_SHORT).show();
                    }
                    pd.dismiss();
                } catch (Exception e) {
                    Toast.makeText(InwardItemActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    /**
     * Read response from CSV file and set data to in Data base
     * checking All possible validation
     *
     * @param currentDate - need to current date
     */

    private void readCSVValue(String currentDate) {

        String checkUOMTypye = "Pk Lt Ml Gm Kg Bg Bx No Mt Dz Sa St Bt Pl Pc";
        String[] checkSupplyType = {"G", "S"};
        String csvHeading = "MENU CODE,ITEM NAME,SUPPLY TYPE,RATE,QUANTITY,UOM,CGST RATE,SGST RATE,IGST RATE,CESS RATE,IMAGEURL";
        boolean flag;
        try {
            String line;
            String chechCSVHeaderLine;
            chechCSVHeaderLine = mBufferReader.readLine();

            flag = csvHeading.equals(chechCSVHeaderLine);
            mFlag = false;
            if (!flag) {
                mFlag = true;
                mUserCSVInvalidValue = getResources().getString(R.string.header_value_empty) + "\n"
                        + "MENU CODE,ITEM NAME,SUPPLY TYPE,RATE,QUANTITY,UOM,CGST RATE,SGST RATE,IGST RATE,CESS RATE";
                return;
            }

            int count =0;
            while ((line = mBufferReader.readLine()) != null) {
                count++;
                final String[] colums = line.split(",");

                if (colums[0].length() > 0 && colums[0].trim().length() > 0 && colums[0] != null) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[0]);
                    if (mCheckCSVValueType == CHECK_INTEGER_VALUE) {
                        mMenuCode = Integer.parseInt(colums[0]);
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.menu_code_invalid) + " "+ colums[1]+" at position "+count;;
                        break;
                    }
                } else {
                    mFlag = true;
                    if(colums[1].length() > 0  && colums[0].trim().length() > 0 ) {
                        mUserCSVInvalidValue = getResources().getString(R.string.menu_code_empty) + " "+ colums[1]+" at position "+count;;
                        break;
                    } else {
                        mUserCSVInvalidValue = getResources().getString(R.string.please_enter_item_name)+ " at position "+count;
                        break;
                    }
                }

                if (colums[1].length() > 0 && colums[1].trim().length() > 0 && colums[1] != null) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[1]);
                    if (mCheckCSVValueType == CHECK_STRING_VALUE) {
                        mItemName = colums[1];
                    } else {
                        // TODO: 7/19/2017  alert messge
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.item_name_invalid) + " "+ colums[0]+" at position "+count;;
                        break;
                    }
                } else {
                    mFlag = true;
                    mUserCSVInvalidValue = getResources().getString(R.string.item_name_empty) + " "+ colums[0]+" at position "+count;;
                    break;
                }

                if (colums[2].length() > 0 && colums[2].trim().length() > 0 && colums[2] != null) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[2]);
                    if (mCheckCSVValueType == CHECK_STRING_VALUE) {

                        if (checkSupplyType[0].equals(colums[2])) {
                            mSupplyType = colums[2];
                        } else if (checkSupplyType[1].equals(colums[2])) {
                            mSupplyType = colums[2];
                        } else {
                            mFlag = true;
                            mUserCSVInvalidValue = getResources().getString(R.string.supply_type_invalid)+ " " + colums[1] + " at position "+count+" e.g " + checkSupplyType;
                            break;
                        }

                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.supply_type_invalid) + " "+ colums[1]+" at position "+count;
                        break;
                    }
                } else {
                    mFlag = true;
                    mUserCSVInvalidValue = getResources().getString(R.string.supply_type_empty)+ " " + colums[1]+" at position "+count;;
                    break;
                }

                if (colums[3].length() > 0 && colums[3].trim().length() > 0 && colums[3] != null) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[3]);
                    if (mCheckCSVValueType == CHECK_DOUBLE_VALUE) {
                        mRate = Double.parseDouble(colums[3]);
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.rate_invalid)+ " " + colums[1]+" at position "+count;;
                        break;
                    }
                } else {
                    mFlag = true;
                    mUserCSVInvalidValue = getResources().getString(R.string.rate_empty) + " "+ colums[1]+" at position "+count;;
                    break;
                }

                if (colums[4].length() > 0 && colums[4].trim().length() > 0 && colums[4] != null) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[4]);
                    if (mCheckCSVValueType == CHECK_DOUBLE_VALUE) {
                        mQuantity = Double.parseDouble(colums[4]);
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.quantity_invalid) + " "+ colums[1]+" at position "+count;;
                        break;
                    }
                } else {
                    mFlag = true;
                    mUserCSVInvalidValue = getResources().getString(R.string.quantity_empty) + " "+ colums[1]+" at position "+count;;
                    break;
                }

                if (colums[5].length() > 0 && colums[5].trim().length() > 0 && colums[5] != null) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[5]);
                    if (mCheckCSVValueType == CHECK_STRING_VALUE) {

                        if (colums[5].trim().length() == 2 && checkUOMTypye.contains(colums[5])) {
                            mUOM = colums[5];
                        } else {
                            mFlag = true;
                            mUserCSVInvalidValue = getResources().getString(R.string.uom_invalid)+ " " + colums[1] +" at position "+count+ " e.g " + checkUOMTypye;
                            break;
                        }
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.uom_invalid) + " "+ colums[1]+" at position "+count;;
                        break;
                    }
                } else {
                    mFlag = true;
                    mUserCSVInvalidValue = getResources().getString(R.string.uom_empty)+ " " + colums[1]+" at position "+count;;
                    break;
                }

                if (colums[6].length() > 0 && colums[6].trim().length() > 0 && colums[6] != null) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[6]);
                    if (mCheckCSVValueType == CHECK_DOUBLE_VALUE) {
                        mCGSTRate = Double.parseDouble(colums[6]);
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.cgst_rate_invalid) + " "+ colums[1]+" at position "+count;;
                        break;
                    }
                } else {
                    mFlag = true;
                    mUserCSVInvalidValue = getResources().getString(R.string.cgst_rate_empty) + " "+ colums[1]+" at position "+count;;
                    break;
                }

                if (colums[7].length() > 0 && colums[7].trim().length() > 0 && colums[7] != null) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[7]);
                    if (mCheckCSVValueType == CHECK_DOUBLE_VALUE) {
                        mSGSTRate = Double.parseDouble(colums[7]);
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.sgst_rate_invalid) + " "+ colums[1]+" at position "+count;;
                        break;
                    }
                } else {
                    mFlag = true;
                    mUserCSVInvalidValue = getResources().getString(R.string.sgst_rate_empty)+ " " + colums[1]+" at position "+count;;
                    break;
                }

                if (colums[8].length() > 0 && colums[8].trim().length() > 0 && colums[8] != null) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[8]);
                    if (mCheckCSVValueType == CHECK_DOUBLE_VALUE) {
                        mIGSTRate = Double.parseDouble(colums[8]);
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.igst_rate_invalid) + " "+ colums[1]+" at position "+count;;
                        break;
                    }
                } else {
                    mFlag = true;
                    mUserCSVInvalidValue = getResources().getString(R.string.igst_rate_empty)+ " " + colums[1]+" at position "+count;;
                    break;
                }

                if (colums[9].length() > 0 && colums[9].trim().length() > 0 && colums[9] != null) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[9]);
                    if (mCheckCSVValueType == CHECK_DOUBLE_VALUE) {
                        mCESSRate = Double.parseDouble(colums[9]);
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.cess_rate_invalid)+ " " + colums[1]+" at position "+count;;
                        break;
                    }
                } else {
                    mFlag = true;
                    mUserCSVInvalidValue = getResources().getString(R.string.cess_rate_empty)+ " " + colums[1]+" at position "+count;;
                    break;
                }

                ItemInward itemInwardObj = new ItemInward(mMenuCode, mItemName, "Barcode", colums[6].trim(), "HSNCode",
                        mRate, mQuantity, mUOM,
                        mIGSTRate, IGSTAmt, mCGSTRate, CGSTAmt, mSGSTRate, SGSTAmt, mCESSRate, cessAmt,
                        "TaxationType", mSupplyType);

                long lRowId = dbInwardItem.addItem_InwardDatabase(itemInwardObj);
            }
            StockInwardMaintain stock_outward = new StockInwardMaintain(InwardItemActivity.this, dbInwardItem);
            stock_outward.saveOpeningStock_Inward(currentDate);

        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    /**
     * checking types of data validation(Integer , Double , String)
     *
     * @param value - csv value
     */

    public static int checkCSVTypeValue(String value) {
        int flag;
        try {
            Integer.parseInt(value);
            flag = 0;
            return flag;
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        try {
            Double.parseDouble(value);
            flag = 1;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            flag = 2;
        }
        return flag;
    }

    private void InitializeViewVariables() {

        //tvFileName = (TextView) view.findViewById(R.id.tvFileName);
//        imgItemImage = (ImageView) findViewById(R.id.imgItemImage);
//        imgItemImage.setImageResource(R.drawable.img_noimage);

        lstInwardItem = (ListView)  findViewById(R.id.lstInwardItem);
        spnrUOM= (Spinner)findViewById(R.id.spnrUOM);
        spnr_supplytype = (Spinner)findViewById(R.id.spnr_supplytype);
        et_inw_averagerate_entered = (EditText) findViewById(R.id.et_inw_rate);
        et_Inw_SGSTRate = (EditText) findViewById(R.id.et_Inw_SGSTRate);
        et_Inw_CGSTRate = (EditText) findViewById(R.id.et_Inw_CGSTRate);
        et_Inw_IGSTRate = (EditText) findViewById(R.id.et_Inw_IGSTRate);
        et_Inw_cessRate = (EditText) findViewById(R.id.et_Inw_cessRate);
        et_inw_quantity = (EditText) findViewById(R.id.et_inw_quantity);
        et_Inw_Amount = (TextView) findViewById(R.id.et_Inw_Amount);
        tvMenuCode = (TextView) findViewById(R.id.tvMenuCode);
        autocomplete_inw_ItemName= (AutoCompleteTextView) findViewById(R.id.autocomplete_inw_ItemName);
        et_inw_ItemBarcode = (EditText) findViewById(R.id.et_inw_ItemBarcode);
        et_inw_HSNCode = (EditText) findViewById(R.id.et_inw_HSNCode);
        tv_AverageRate  = (TextView)findViewById(R.id.tv_AverageRate);

        btnAdd = (WepButton) findViewById(R.id.btnAddItem);
        btnEdit = (WepButton) findViewById(R.id.btnEditItem);
        //btnResetQuantity = (WepButton) findViewById(R.id.btnResetQuantity);
        btnClearItem = (WepButton) findViewById(R.id.btnClearItem);
        btnCloseItem = (WepButton) findViewById(R.id.btnCloseItem);
        btnUploadExcel = (WepButton) findViewById(R.id.buttonUploadExcel);
        btnSaveExcel = (WepButton) findViewById(R.id.buttonSaveExcel);
        tvFileName = (TextView) findViewById(R.id.tvFileName);



    }


    public AdapterView.OnItemClickListener ListViewClickEvent = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try{
                if(InwardItemAdapter==null )
                    return;
                ItemInward item = (ItemInward)InwardItemAdapter.getItem(position);
                itemname_clicked = (item.getStrItemname());
                autocomplete_inw_ItemName.setText(item.getStrItemname());
                tvMenuCode.setText(String.valueOf(item.getiMenuCode()));
                et_inw_ItemBarcode.setText(item.getStrItemBarcode());
                et_inw_averagerate_entered.setText(String.format("%.2f",item.getfAveragerate()));
                et_inw_quantity.setText(String.format("%.2f",item.getfQuantity()));
                et_inw_HSNCode.setText(item.getHSNCode());
                et_Inw_CGSTRate.setText(String.format("%.2f",item.getCGSTRate()));
                et_Inw_SGSTRate.setText(String.format("%.2f",item.getSGSTRate()));
                et_Inw_IGSTRate.setText(String.format("%.2f",item.getIGSTRate()));
                et_Inw_cessRate.setText(String.format("%.2f",item.getCessRate()));

                if (item.getSupplyType().equalsIgnoreCase("g"))
                    spnr_supplytype.setSelection(0);
                else
                    spnr_supplytype.setSelection(1);

                String uom_temp = item.getUOM();
                String uom = "(" + uom_temp + ")";
                int index = getIndex(uom);
                spnrUOM.setSelection(index);

                btnAdd.setEnabled(false);
                btnEdit.setEnabled(true);
            }catch (Exception e)
            {
                e.printStackTrace();
                MsgBox1.Show("Error",e.getMessage());
            }

        }
    };
    @SuppressWarnings("deprecation")
    private void  DisplayItems() {
        Cursor crsrItems = dbInwardItem.getAllInwardItems();
        InwardItemList = new ArrayList<>();
        boolean isItemsPresents = false;
        while (crsrItems!=null && crsrItems.moveToNext())
        {
            isItemsPresents = true;
            String ItemName = crsrItems.getString(crsrItems.getColumnIndex("ItemName"));
            int MenuCode = crsrItems.getInt(crsrItems.getColumnIndex("MenuCode"));
            String Barcode = crsrItems.getString(crsrItems.getColumnIndex("ItemBarcode"));
            String HSNCode = crsrItems.getString(crsrItems.getColumnIndex("HSNCode"));
            String ImageUri = crsrItems.getString(crsrItems.getColumnIndex("ImageUri"));
            double averageRate = crsrItems.getDouble(crsrItems.getColumnIndex("AverageRate"));
            double quantity = crsrItems.getDouble(crsrItems.getColumnIndex("Quantity"));
            String uom = crsrItems.getString(crsrItems.getColumnIndex("UOM"));
            double IGSTRate = crsrItems.getDouble(crsrItems.getColumnIndex("IGSTRate"));
            double CGSTRate = crsrItems.getDouble(crsrItems.getColumnIndex("CGSTRate"));
            double SGSTRate = crsrItems.getDouble(crsrItems.getColumnIndex("SGSTRate"));
            double cessRate = crsrItems.getDouble(crsrItems.getColumnIndex("cessRate"));
            String TaxationType =  crsrItems.getString(crsrItems.getColumnIndex("TaxationType"));
            String SupplyType =  crsrItems.getString(crsrItems.getColumnIndex("SupplyType"));

            double IGSTAmt =0;
            double CGSTAmt =0;
            double SGSTAmt =0;
            double cessAmt =0;

            ItemInward itemInward = new ItemInward(MenuCode, ItemName, Barcode,ImageUri,HSNCode,averageRate,quantity,uom,
                    IGSTRate, IGSTAmt, CGSTRate,CGSTAmt, SGSTRate,SGSTAmt,cessRate,cessAmt, TaxationType,SupplyType);
            InwardItemList.add(itemInward);
        }
        if(isItemsPresents)// items present in inward
        {
            if(InwardItemAdapter == null)
            {
                InwardItemAdapter = new ItemInwardAdapter(myContext,dbInwardItem,InwardItemList, "InwardItemActivity");
                InwardItemAdapter.setActivityAdapter(InwardItemAdapter);
                lstInwardItem.setAdapter(InwardItemAdapter);
            }else
            {
                InwardItemAdapter.notifyDataSetChanged(InwardItemList);
            }
        }else {
            MsgBox1.Show("Note","No items present in inward database");
        }
    }

    /*private void DisplayItems(String itemname ) {
        Cursor crsrItems = null;
        try {


            crsrItems = dbSupplierItemLink.getItemDetail_inward(itemname);
            while (crsrItems != null && crsrItems.moveToNext()) {

                int suppliercode = crsrItems.getInt(crsrItems.getColumnIndex("SupplierCode"));
                Cursor cursor_supplier = dbSupplierItemLink.getSupplierDetails_forcode(suppliercode);
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
                int res = getResources().getIdentifier("delete", "drawable", this.getPackageName());
                btnItemDelete = new ImageButton(myContext);
                btnItemDelete.setImageResource(res);
                btnItemDelete.setLayoutParams(new TableRow.LayoutParams(60, 45));

                //rowItems.addView(btnItemDelete);
               *//* Button btndel = new Button(myContext);
                btndel.setBackground(getResources().getDrawable(R.drawable.deletered1));
                btndel.setPadding(5,0,0,0);
                btndel.setLayoutParams(new TableRow.LayoutParams(30, 20));*//*

                Button btndel = new Button(myContext);
                btndel.setBackground(getResources().getDrawable(R.drawable.delete_icon_lightyellow));
                btndel.setLayoutParams(new TableRow.LayoutParams(40, 35));

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



                            *//*autocompletetv_suppliername.setText(Suppliername.getText().toString());*//*

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
                            et_inw_averagerate_entered.setText(Rate.getText().toString());
                            et_inw_quantity.setText(Quantity.getText().toString());
                            double rate = Double.parseDouble(et_inw_averagerate_entered.getText().toString());
                            double quantity = Double.parseDouble(et_inw_quantity.getText().toString());
                            rate_prev_for_touched_item = Float.parseFloat(et_inw_averagerate_entered.getText().toString());
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
                            btnLinkItem.setEnabled(false);
                            btnDelinkItem.setEnabled(true);
                            btnResetQuantity.setEnabled(true);

                        }
                    }
                });

                rowItems.setTag("TAG");

                //tblItems.addView(rowItems, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            }
        }
        catch (Exception e)
        {
            MsgBox.Show(" Error", e.getMessage());
            e.printStackTrace();
        }
        //DisplayItems();
    }
*/



    private boolean IsItemExists_InDatabase(String item_name) {
        boolean isItemExists = false;
        Cursor crsr = dbInwardItem.getItemDetail_inward(item_name);
        String uom = spnrUOM.getSelectedItem().toString();
        if(crsr!=null && crsr.moveToNext())
        {
            isItemExists = true;
            int length = uom.length();
            String mou_temp = uom.substring(length-3, length-1);
            String uom_already_saved = crsr.getString(crsr.getColumnIndex("UOM"));
            if(!mou_temp.equalsIgnoreCase(uom_already_saved))
            {
                MsgBox.setTitle("Warning")
                        .setMessage(item_name+" is already present in database with unit "+uom_already_saved+". " +
                                "\nTo change the unit , kindly delete this item and add it again.")
                        .setPositiveButton("Ok",null)
                        .show();

            }else
            {
                MsgBox.setTitle("Warning")
                        .setMessage(item_name+" is already present in database with unit "+uom_already_saved+". ")
                        .setPositiveButton("Ok",null)
                        .show();
            }
        }
        return isItemExists;
    }


    private void ReadData(int Type, int pos) {

        final String imageURI ="",taxationType="";
        final double igstAmount =0, cgstAmount=0, sgstAmount =0, cessAmount=0;

        final String itemname = autocomplete_inw_ItemName.getText().toString().toUpperCase();
        final String strBarcode = et_inw_ItemBarcode.getText().toString();
        final double averageRate_temp = Float.parseFloat(et_inw_averagerate_entered.getText().toString());
        String rate_str = String.format("%.2f",averageRate_temp);
        final double averageRate = Float.parseFloat(rate_str);
        final double quantity =Float.parseFloat(et_inw_quantity.getText().toString());
        final double cgstRate = Float.parseFloat(et_Inw_CGSTRate.getText().toString());
        final double sgstRate = Float.parseFloat(et_Inw_SGSTRate.getText().toString());
        final double igstRate = Float.parseFloat(et_Inw_IGSTRate.getText().toString());
        final double cessRate = Float.parseFloat(et_Inw_cessRate.getText().toString());
        String mou_temp = spnrUOM.getSelectedItem().toString();
        int length = mou_temp.length();
        final String uom = mou_temp.substring(length-3, length-1);
        final String supplyType = spnr_supplytype.getSelectedItem().toString();
        final String HSNCode = et_inw_HSNCode.getText().toString();


        // Type 1 - addItem, Type 2 - updateItem
        if (Type == 1) {
            ItemInward item =  new ItemInward(0, itemname,  strBarcode, imageURI, HSNCode,averageRate,quantity, uom,
                    igstRate, igstAmount,cgstRate, cgstAmount, sgstRate, sgstAmount, cessRate, cessAmount,taxationType,supplyType);
            long lRowId = dbInwardItem.addItem_InwardDatabase(item);
            if(lRowId>0) {
                Log.d("Item Inward", "Row Id:" + String.valueOf(lRowId));
                Long l = dbInwardItem.addIngredient(itemname, quantity, uom, averageRate, 0);
                UpdateStockInward(itemname,quantity, averageRate);
            }

        } else if (Type == 2)
        { // update
            int menuCode = Integer.parseInt(tvMenuCode.getText().toString());
            ItemInward item =  new ItemInward( menuCode, itemname,  strBarcode, imageURI, HSNCode,averageRate,quantity, uom,
                    igstRate, igstAmount,cgstRate, cgstAmount, sgstRate, sgstAmount, cessRate, cessAmount,
                    taxationType,supplyType);


            Cursor item_crsr = dbInwardItem.getItem_inward(menuCode);
            if (item_crsr != null && item_crsr.moveToFirst()) //  item present in database
            {
                int iRowId = dbInwardItem.updateItem_InwardDatabase(item);
                if (iRowId >0  ){ // not updated
                    Toast.makeText(myContext, "Update Item Inward : Row updated: "+String.valueOf(iRowId), Toast.LENGTH_SHORT).show();
                    long i = dbInwardItem.updateIngredient(itemname, quantity,averageRate,1);
                    UpdateStockInward(itemname,quantity,averageRate);
                } else{
                    Toast.makeText(myContext, " Item Cannot be updated", Toast.LENGTH_SHORT).show();
                }

                Log.d("Inward Item Update", "Rows updated : "+String.valueOf(iRowId));
            } else{ // item not present in database


                MsgBox.setTitle("Item not found in database. Do you want to add it ?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ItemInward item =  new ItemInward( 0, itemname,  strBarcode, imageURI, HSNCode,averageRate,quantity, uom,
                                        igstRate, igstAmount,cgstRate, cgstAmount, sgstRate, sgstAmount, cessRate, cessAmount,taxationType,supplyType);
                                long lRowId = dbInwardItem.addItem_InwardDatabase(item);
                                if(lRowId>0)
                                {
                                    Log.d("Item Inward", "Row Id:" + String.valueOf(lRowId));
                                    Long l = dbInwardItem.addIngredient(itemname, quantity, uom, averageRate,0);
                                }
                                ClearingAndDisplaying();
                                UpdateStockInward(itemname,quantity,averageRate);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();


            }
        }
    }


    private void UpdateStockInward(String itemname, double quantity,  double averagerate)
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
                if(!ClosingQty_str.equalsIgnoreCase(""))
                    Closingqty_prev =  Double.parseDouble(qty_str);
                item.setMenuCode(item_present_crsr.getInt(item_present_crsr.getColumnIndex("MenuCode")));
                item.setOpeningStock(Openingqty_prev+Double.parseDouble(String.valueOf(quantity)));
                item.setClosingStock(Closingqty_prev+Double.parseDouble(String.valueOf(quantity)));
                item.setRate(averagerate);
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
                    item.setRate(averagerate);
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


    private void ResetItem() {
        sema_display= ALL;

        strMenuCode = "";
        strImageUri = "";
        AMU = "";
        spnrUOM.setSelection(0);
        spnrUOM.setEnabled(true);

        //tvFileName.setText("FileName");
        et_inw_ItemBarcode.setText("");
        autocomplete_inw_ItemName.setText("");
        tvMenuCode.setText("-1");
        et_inw_averagerate_entered.setText("");
        et_inw_quantity.setText("");
        et_Inw_Amount.setText("");
        et_inw_HSNCode.setText("");
        tv_AverageRate.setText("0.00"); // no value
        et_Inw_SGSTRate.setText("0");
        et_Inw_CGSTRate.setText("0");
        et_Inw_IGSTRate.setText("0");
        et_Inw_cessRate.setText("0");
        btnAdd.setEnabled(true);
        btnEdit.setEnabled(false);
        //btnResetQuantity.setEnabled(false);

        rate_prev_for_touched_item = 0;
        itemname_clicked = "";
    }



    public void AddItem1 (View v)
    {


        String item_name = autocomplete_inw_ItemName.getText().toString().toUpperCase();
        if (item_name.equalsIgnoreCase("")) {
            MsgBox.setTitle("Warning")
                    .setMessage("Please enter item name")
                    .show();
            return;
        }

        if(IsItemExists_InDatabase(item_name))
        {
            return;
        }
        String uom = spnrUOM.getSelectedItem().toString();
        if (uom.equalsIgnoreCase("") || uom.equalsIgnoreCase("Select")) {
            MsgBox.setTitle("Warning")
                    .setMessage("Please select item UoM")
                    .setPositiveButton("Ok",null)
                    .show();
            return;
        }

        String CGSTTax_str = et_Inw_CGSTRate.getText().toString();
        if (CGSTTax_str.equalsIgnoreCase("")) {
            et_Inw_CGSTRate.setText("0");
        }else if (Double.parseDouble(CGSTTax_str)< 0 || Double.parseDouble(CGSTTax_str)>99.99)
        {
            MsgBox1.Show("Warning","Please enter CGST Rate between 0 and 99.99");
            return;
        }

        String SGSTTax_str = et_Inw_SGSTRate.getText().toString();
        if (SGSTTax_str.equalsIgnoreCase("")) {
            et_Inw_SGSTRate.setText("0");
        }else if (Double.parseDouble(SGSTTax_str) <0 || Double.parseDouble(SGSTTax_str)> 99.99)
        {
            MsgBox1.Show("Warning","Please enter SGST Rate between 0 and 99.99");
            return;
        }

        String IGSTTax_str = et_Inw_IGSTRate.getText().toString();
        if (IGSTTax_str.equalsIgnoreCase("")) {
            et_Inw_IGSTRate.setText("0");
        }else if (Double.parseDouble(IGSTTax_str) <0 || Double.parseDouble(IGSTTax_str)> 99.99)
        {
            MsgBox1.Show("Warning","Please enter IGST Rate between 0 and 99.99");
            return;
        }

        String cessTax_str = et_Inw_cessRate.getText().toString();
        if (cessTax_str.equalsIgnoreCase("")) {
            et_Inw_cessRate.setText("0");
        }else if (Double.parseDouble(cessTax_str) <0 || Double.parseDouble(cessTax_str)> 99.99)
        {
            MsgBox1.Show("Warning","Please enter cess Rate between 0 and 99.99");
            return;
        }

        if (et_inw_quantity.getText().toString().equalsIgnoreCase("")) {
            MsgBox.setTitle("Warning")
                    .setMessage("Please enter item's quantity ")
                    .setPositiveButton("Ok",null)
                    .show();
            return;
        }else
        {
            double quantity = Double.parseDouble(et_inw_quantity.getText().toString());
            if(quantity<0 || quantity>9999.99)
            {
                MsgBox.setTitle("Warning")
                        .setMessage("Please enter item's quantity between 0 and 9999.99")
                        .setPositiveButton("Ok",null)
                        .show();
                return;
            }
        }
        if (et_inw_averagerate_entered.getText().toString().equalsIgnoreCase("")) {
            MsgBox.setTitle("Warning")
                    .setMessage("Please enter item's average rate")
                    .setPositiveButton("Ok",null)
                    .show();
            return;
        }else
        {
            double averageRate = Double.parseDouble(et_inw_averagerate_entered.getText().toString());
            if(averageRate<0 || averageRate>9999.99)
            {
                MsgBox.setTitle("Warning")
                        .setMessage("Please enter item's average rate between 0 and 9999.99")
                        .setPositiveButton("Ok",null)
                        .show();
                return;
            }
        }



        try {
            ReadData(1, 0); // 1 - addItem
            ClearingAndDisplaying();
            loadSpinnerData();
            //loadAutoCompleteData_item(-1);

        } catch (Exception exp) {
            Toast.makeText(myContext, exp.getMessage(), Toast.LENGTH_SHORT).show();
            exp.printStackTrace();
        }
    }

    public void ResetQuantity(View v)
    {
        MsgBox.setTitle("Warning")
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
        int supplierCode =-1;// Integer.parseInt(tv_suppliercode.getText().toString());
        int menuCode = Integer.parseInt(strMenuCode);
        long l = dbInwardItem.resetStock_inward(supplierCode, menuCode,itemName);
        if(l>0)
        {

        }
            //Toast.makeText(myContext, itemName+" reset to 0 for supplier : "+autocompletetv_suppliername.getText().toString(), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(myContext, itemName+" cannot be reset to 0 ", Toast.LENGTH_LONG).show();
        ClearingAndDisplaying();
    }
    public void EditItem1(View v, int pos) {
        String itemName = autocomplete_inw_ItemName.getText().toString();
        if (itemName.equalsIgnoreCase("")) {
            Toast.makeText(myContext, "Please enter item name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!(itemName.equalsIgnoreCase(itemname_clicked)) && IsItemExists_InDatabase(itemName))
        {
            return;
        }

         if (et_inw_quantity.getText().toString().equalsIgnoreCase("")) {
            et_inw_quantity.setText("0");
        }else if(Double.parseDouble(et_inw_quantity.getText().toString())< 0 ||
                 Double.parseDouble(et_inw_quantity.getText().toString())>9999.99)
         {
             MsgBox1.Show("Warning","Please enter item's quantity between 0 and 9999.99");
             return;
         }

        if (et_inw_averagerate_entered.getText().toString().equalsIgnoreCase("")) {
            et_inw_averagerate_entered.setText("0");
        }else if(Double.parseDouble(et_inw_averagerate_entered.getText().toString())< 0 ||
                Double.parseDouble(et_inw_averagerate_entered.getText().toString())>9999.99)
        {
            MsgBox1.Show("Warning","Please enter item's average rate between 0 and 9999.99");
            return;
        }

        String CGSTTax_str = et_Inw_CGSTRate.getText().toString();
        if (CGSTTax_str.equalsIgnoreCase("")) {
            et_Inw_CGSTRate.setText("0");
        }else if (Double.parseDouble(CGSTTax_str)< 0 || Double.parseDouble(CGSTTax_str)>99.99)
        {
            MsgBox1.Show("Warning","Please enter CGST TAX percent between 0 and 99.99");
            return;
        }

        String SGSTTax_str = et_Inw_SGSTRate.getText().toString();
        if (SGSTTax_str.equalsIgnoreCase("")) {
            et_Inw_SGSTRate.setText("0");
        }else if (Double.parseDouble(SGSTTax_str) <0 || Double.parseDouble(SGSTTax_str)> 99.99)
        {
            MsgBox1.Show("Warning","Please enter SGST TAX percent between 0 and 99.99");
            return;
        }

        String IGSTTax_str = et_Inw_IGSTRate.getText().toString();
        if (IGSTTax_str.equalsIgnoreCase("")) {
            et_Inw_IGSTRate.setText("0");
        }else if (Double.parseDouble(IGSTTax_str)< 0 || Double.parseDouble(IGSTTax_str)>99.99)
        {
            MsgBox1.Show("Warning","Please enter IGST TAX percent between 0 and 99.99");
            return;
        }

        String cessTax_str = et_Inw_cessRate.getText().toString();
        if (cessTax_str.equalsIgnoreCase("")) {
            et_Inw_cessRate.setText("0");
        }else if (Double.parseDouble(cessTax_str) <0 || Double.parseDouble(cessTax_str)> 99.99)
        {
            MsgBox1.Show("Warning","Please enter cess TAX percent between 0 and 99.99");
            return;
        }


        // richa 2712_UOM
        String mou_temp = spnrUOM.getSelectedItem().toString();
        if (mou_temp.equalsIgnoreCase("Unit") || mou_temp.equalsIgnoreCase("Select Unit"))
        {
            MsgBox.setTitle("Error")
                    .setMessage("Please Select the Unit for the item.")
                    .setPositiveButton("OK",null)
                    .show();
            return;
        }
        // richa 2712_UOM

        try {
            ReadData(2, 0); // 2 - updateItem
            ClearingAndDisplaying();
        } catch (Exception exp) {
            Toast.makeText(myContext, exp.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void ClearingAndDisplaying()
    {
        if(sema_display == ALL)
        {

            DisplayItems();
        } else // Itemwise
        {
            String itemname = autocomplete_inw_ItemName.getText().toString();
           // DisplayItems(itemname);
        }

        ResetItem();
    }

    public void ClearItem1(View v) {
        ClearingAndDisplaying();
    }

    public void CloseItem1(View v) {
        dbInwardItem.CloseDatabase();
        finish();
    }


   /* public void loadAutoCompleteData_item(int suppliercode) {

        String suppliername_str = autocompletetv_suppliername.getText().toString().toUpperCase();
        List<String> itemlist = dbSupplierItemLink.getitemlist_inward_nonGST(suppliername_str, suppliercode);

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(myContext,android.R.layout.simple_list_item_1,itemlist);

        dataAdapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);

        // attaching data adapter to spinner
        autocomplete_inw_ItemName.setAdapter(dataAdapter1);

    }*/

    public void loadSpinnerData()
    {
        Cursor cursor_item = dbInwardItem.getAllInwardItemNames();
        itemlist = new ArrayList<String>();
        if (cursor_item != null && cursor_item.moveToFirst()) {
            do {
                itemlist.add(cursor_item.getString(cursor_item.getColumnIndex("ItemName")));// adding
            } while (cursor_item.moveToNext());
        }
        ArrayAdapter<String>  dataAdapter = new ArrayAdapter<String>(myContext, android.R.layout.simple_list_item_1,itemlist);
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
                tvFileName.setText(strUploadFilepath.substring(strUploadFilepath.lastIndexOf("/")+1));
            }
        }
        super.onActivityResult( requestCode,  resultCode,  data);
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
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            dbInwardItem.CloseDatabase();
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
