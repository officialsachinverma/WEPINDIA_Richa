package com.wepindia.pos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.WepBaseActivity;
import com.wep.common.app.models.SupplierItemLinkageModel;
import com.wep.common.app.views.WepButton;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.adapters.SupplierItemLinkageAdapter;
import com.wepindia.pos.utils.ActionBarUtils;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItemSupplierLinkageActivity extends WepBaseActivity {

    Context myContext;
    DatabaseHandler dbSupplierItemLink;
    public MessageDialog MsgBox;
    int ALL = 0;
    int SUPPLIERWISE = 1;
    int ITEMWISE = 2;
    int sema_display=0;

    ArrayList<SupplierItemLinkageModel> supplierItemLinkageList;
    SupplierItemLinkageAdapter supplierItemLinkageAdapter;
    ListView lstSupplieritem;

    Toolbar toolbar;


    WepButton btnLinkItem, btnDelinkItem, btnClose, btnClear;
    EditText spnrUOM,spnr_supplytype;
    EditText et_inw_supplierAddress,et_inw_ItemBarcode,et_inw_HSNCode;

    AutoCompleteTextView autocompletetv_supplierPhn, autocompletetv_suppliername,autocomplete_inw_ItemName;

    EditText et_inw_rate,et_inw_quantity,edt_supplierGSTIN;
    EditText et_Inw_SGSTRate, et_Inw_CGSTRate,et_Inw_IGSTRate,et_Inw_cessRate;
    ArrayList<String> labelsSupplierName,labelsSupplierPhn;
    ArrayList<String> itemlist;
    TextView tv_suppliercode, et_Inw_AverageRate,tv_count,et_Inw_Amount,tv_menucode;


    String strMenuCode,  strUserName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_supplier_linkage);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myContext = this;
        strUserName = getIntent().getStringExtra("USER_NAME");
    try
    {
        Date d = new Date();
        CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
        com.wep.common.app.ActionBarUtils.setupToolbar(this,toolbar,getSupportActionBar(),"Supplier Item Linkage",strUserName," Date:"+s.toString());

        myContext = this;
        MsgBox = new MessageDialog(myContext);
        dbSupplierItemLink = new DatabaseHandler(myContext);

        dbSupplierItemLink.CloseDatabase();
        dbSupplierItemLink.CreateDatabase();
        dbSupplierItemLink.OpenDatabase();

        InitializeViewVariables();
        OnClickEvents();
        Reset();
        loadAutoCompleteData();
        DisplayItems(-1); // display all data

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    private void InitializeViewVariables()
    {
        lstSupplieritem = (ListView) findViewById(R.id.lstSupplieritem);
        autocompletetv_suppliername = (AutoCompleteTextView) findViewById(R.id.autocompletetv_suppliername);
        autocompletetv_supplierPhn = (AutoCompleteTextView) findViewById(R.id.autocompletetv_supplierPhn);
        et_inw_supplierAddress = (EditText) findViewById(R.id.et_inw_supplierAddress);
        edt_supplierGSTIN = (EditText) findViewById(R.id.edt_supplierGSTIN);
        tv_suppliercode  = (TextView)findViewById(R.id.tv_suppliercode);

        tv_menucode = (TextView)findViewById(R.id.tv_menucode);
        autocomplete_inw_ItemName= (AutoCompleteTextView) findViewById(R.id.autocomplete_inw_ItemName);
        et_Inw_AverageRate = (TextView)findViewById(R.id.et_Inw__AverageRate);
        et_inw_ItemBarcode = (EditText) findViewById(R.id.et_inw_ItemBarcode);
        et_inw_HSNCode = (EditText) findViewById(R.id.et_inw_HSNCode);
        spnrUOM= (EditText)findViewById(R.id.spnrUOM);
        spnr_supplytype = (EditText)findViewById(R.id.spnr_supplytype);

        et_Inw_SGSTRate = (EditText) findViewById(R.id.et_Inw_SGSTRate);
        et_Inw_CGSTRate = (EditText) findViewById(R.id.et_Inw_CGSTRate);
        et_Inw_IGSTRate = (EditText) findViewById(R.id.et_Inw_IGSTRate);
        et_Inw_cessRate = (EditText) findViewById(R.id.et_Inw_cessRate);

        btnLinkItem = (WepButton) findViewById(R.id.btnLinkItem);
        btnDelinkItem = (WepButton) findViewById(R.id.btnDelinkItem);
        btnClear = (WepButton) findViewById(R.id.btnClear);
        btnClose = (WepButton) findViewById(R.id.btnClose);

        btnLinkItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkItem(v);
            }
        });
        btnDelinkItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DelinkItem(v);
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
    }

    public void  LinkItem(View v)
    {
        String supplierCode_str = tv_suppliercode.getText().toString();
        String menuCode_str = tv_menucode.getText().toString();

        if(supplierCode_str.equalsIgnoreCase("-1") || supplierCode_str.equals(""))
        {
            MsgBox.Show("Insufficient Information", "Kindly enter valid supplier ");
            return;
        }
        if(menuCode_str.equalsIgnoreCase("-1") || menuCode_str.equals(""))
        {
            MsgBox.Show("Insufficient Information", "Kindly enter valid item");
            return;
        }
        long l = dbSupplierItemLink.addLinkage(supplierCode_str, menuCode_str);
        if(l>0)
        {
            String itemname = autocomplete_inw_ItemName.getText().toString();
            String suppliername = autocompletetv_suppliername.getText().toString();
            String msg = itemname+" linked successfully to supplier "+suppliername;
            Toast.makeText(myContext, msg, Toast.LENGTH_SHORT).show();
            Log.d("ItemSupplierLinkageAct",msg);
            Clear(null);
        }else
        {
            Toast.makeText(myContext, "Linkage cannot happen", Toast.LENGTH_SHORT).show();
            Log.d("ItemSupplierLinkageAct","Linkage cannot happen");
        }


    }

    public void  DelinkItem(View v)
    {
        String supplierCode_str = tv_suppliercode.getText().toString();
        String menuCode_str = tv_menucode.getText().toString();

        if(supplierCode_str.equalsIgnoreCase("-1") || supplierCode_str.equals(""))
        {
            MsgBox.Show("Insufficient Information", "Kindly enter valid supplier ");
            return;
        }
        if(menuCode_str.equalsIgnoreCase("-1") || menuCode_str.equals(""))
        {
            MsgBox.Show("Insufficient Information", "Kindly enter valid item");
            return;
        }
        long l = dbSupplierItemLink.deleteLinkage(supplierCode_str, menuCode_str);
        if(l>0)
        {
            String itemname = autocomplete_inw_ItemName.getText().toString();
            String suppliername = autocompletetv_suppliername.getText().toString();
            String msg = itemname+" delinked successfully from supplier "+suppliername;
            Toast.makeText(myContext, msg, Toast.LENGTH_SHORT).show();
            Log.d("ItemSupplierLinkageAct",msg);
            Clear(null);
        }else
        {
            Toast.makeText(myContext, "Delinking cannot happen", Toast.LENGTH_SHORT).show();
            Log.d("ItemSupplierLinkageAct","Delinking cannot happen");
        }

    }

    private  void OnClickEvents()
    {
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
                Cursor supplierdetail_cursor = dbSupplierItemLink.getSupplierDetails(suppliername_str);
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
                    //loadAutoCompleteData_item(suppliercode);

                    DisplayItems(suppliercode,  suppliername_str,supp_gstin,SupplierPhone,SupplierAddress);
                }
                String menuCode = tv_menucode.getText().toString();
                if(!menuCode.equals("") && !menuCode.equals("-1"))
                {
                    Cursor cursor = dbSupplierItemLink.isLinked(suppliercode, Integer.parseInt(menuCode));
                    if(cursor!=null && cursor.moveToNext())
                    {
                        btnDelinkItem.setEnabled(true);
                        btnLinkItem.setEnabled(false);
                    }else
                    {
                        btnDelinkItem.setEnabled(false);
                        btnLinkItem.setEnabled(true);
                    }
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
                Cursor crsritem = dbSupplierItemLink.getItemDetail_inward(itemname);
                if((crsritem != null && crsritem.moveToNext()))
                {
                    tv_menucode.setText(crsritem.getString(crsritem.getColumnIndex("MenuCode")));
                    et_inw_HSNCode.setText(crsritem.getString(crsritem.getColumnIndex("HSNCode")));
                    et_Inw_AverageRate.setText(String.format("%.2f",crsritem.getDouble(crsritem.getColumnIndex("AverageRate"))));
                    et_Inw_IGSTRate.setText(String.format("%.2f",crsritem.getDouble(crsritem.getColumnIndex("IGSTRate"))));
                    et_Inw_CGSTRate.setText(String.format("%.2f",crsritem.getDouble(crsritem.getColumnIndex("CGSTRate"))));
                    et_Inw_SGSTRate.setText(String.format("%.2f",crsritem.getDouble(crsritem.getColumnIndex("SGSTRate"))));
                    et_Inw_cessRate.setText(String.format("%.2f",crsritem.getDouble(crsritem.getColumnIndex("cessRate"))));

//                    if(crsritem.getString(crsritem.getColumnIndex("SupplyType")).equalsIgnoreCase("G"))
//                        spnr_supplytype.setSelection(0);
//                    else
//                        spnr_supplytype.setSelection(1);

                    String uom_temp = crsritem.getString(crsritem.getColumnIndex("UOM"));
                    String uom = "("+uom_temp+")";
//                    spnrUOM.setSelection(getIndex_uom(uom));
                    spnrUOM.setText(uom);
                }
                if (suppliername.equals(""))
                { // itemwise
                    sema_display = ITEMWISE;
                    DisplayItems(itemname);
                }
                else
                {  //supplierwise
                    if(suppliercode_str== null || suppliercode_str.equals(""))
                        suppliercode = -1;
                    else
                        suppliercode = Integer.parseInt(suppliercode_str);
                    int menuCode = crsritem.getInt(crsritem.getColumnIndex("MenuCode"));
                    Cursor cursor = dbSupplierItemLink.isLinked(suppliercode, menuCode);
                    if (cursor !=null && cursor.moveToFirst())
                    {
                            btnLinkItem.setEnabled(false);
                            btnDelinkItem.setEnabled(true);
                    }else
                    {
                        btnLinkItem.setEnabled(true);
                        btnDelinkItem.setEnabled(false);
                    }
                }

            }
        });

        lstSupplieritem.setOnItemClickListener(listViewClickevent);
    }

    public AdapterView.OnItemClickListener listViewClickevent = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                SupplierItemLinkageModel link = (SupplierItemLinkageModel)parent.getItemAtPosition(position);
                autocompletetv_suppliername.setText(link.getSupplierName());
                tv_suppliercode.setText(String.valueOf(link.getSupplierCode()));
                autocompletetv_supplierPhn.setText(link.getSupplierPhone());
                et_inw_supplierAddress.setText(link.getSupplierAddress());

                autocomplete_inw_ItemName.setText(link.getItemName());
                tv_menucode.setText(String.valueOf(link.getMenuCode()));
                et_Inw_AverageRate.setText(String.format("%.2f",link.getAverageRate()));
                et_inw_HSNCode.setText(link.getHsnCode());
//                if(link.getSupplyType().equalsIgnoreCase("G"))
//                    spnr_supplytype.setSelection(0);
//                else if(link.getSupplyType().equalsIgnoreCase("S"))
//                    spnr_supplytype.setSelection(1);
                spnr_supplytype.setText(link.getSupplyType());
                spnrUOM.setText(link.getUom());
                et_Inw_CGSTRate.setText(String.format("%.2f",link.getCGSTRate()));
                et_Inw_SGSTRate.setText(String.format("%.2f",link.getSGSTRate()));
                et_Inw_IGSTRate.setText(String.format("%.2f",link.getIGSTRate()));
                et_Inw_cessRate.setText(String.format("%.2f",link.getCessRate()));

                btnLinkItem.setEnabled(false);
                btnDelinkItem.setEnabled(true);
            }catch (Exception e)
            {
                e.printStackTrace();
                MsgBox.Show("Error",e.getMessage());
            }
        }
    };

//    private int getIndex_uom(String substring){
//
//        int index = 0;
//        for (int i = 0; index==0 && i < spnrUOM.getCount(); i++){
//
//            if (spnrUOM.getItemAtPosition(i).toString().contains(substring)){
//                index = i;
//            }
//
//        }
//
//        return index;
//
//    }
    @SuppressWarnings("deprecation")
    private void  DisplayItems(int suppliercode_recv ) {
        // if suppliercode_recv == -1 , then display all whole data , i.e. all items for all the suppliers
        // else display for that suppliercode
        Cursor crsrItems = null;

        if(supplierItemLinkageList!=null)
            supplierItemLinkageList.clear();
        supplierItemLinkageList = new ArrayList<>();
        Cursor supplier_crsr = dbSupplierItemLink.getAllSupplierName_nonGST();
        while (supplier_crsr!=null && supplier_crsr.moveToNext())
        {

            String supplierName = supplier_crsr.getString(supplier_crsr.getColumnIndex("SupplierName"));
            String supplierPhone = supplier_crsr.getString(supplier_crsr.getColumnIndex("SupplierPhone"));
            String supplierAddress = supplier_crsr.getString(supplier_crsr.getColumnIndex("SupplierAddress"));
            String supplierGSTIN = supplier_crsr.getString(supplier_crsr.getColumnIndex("GSTIN"));
            int suppliercode = supplier_crsr.getInt(supplier_crsr.getColumnIndex("SupplierCode"));
            if (suppliercode == suppliercode_recv)
            {
                continue;
            }
            Cursor crsrMenuCode = dbSupplierItemLink.getLinkedMenuCodeForSupplier(suppliercode);

            if (crsrMenuCode.moveToFirst()) {
                do {
                    int menuCode = crsrMenuCode.getInt(crsrMenuCode.getColumnIndex("MenuCode"));
                    crsrItems = dbSupplierItemLink.getItem_inward(menuCode);
                    if(crsrItems!=null && crsrItems.moveToNext())
                    {
                        String itemName = crsrItems.getString(crsrItems.getColumnIndex("ItemName"));
                        String barcode = crsrItems.getString(crsrItems.getColumnIndex("ItemBarcode"));
                        String hsn = crsrItems.getString(crsrItems.getColumnIndex("HSNCode"));
                        String supplyType = crsrItems.getString(crsrItems.getColumnIndex("SupplyType"));
                        String uom = crsrItems.getString(crsrItems.getColumnIndex("UOM"));
                        double averageRate = crsrItems.getDouble(crsrItems.getColumnIndex("AverageRate"));
                        double igstRate = crsrItems.getDouble(crsrItems.getColumnIndex("IGSTRate"));
                        double cgstRate = crsrItems.getDouble(crsrItems.getColumnIndex("CGSTRate"));
                        double sgstRate = crsrItems.getDouble(crsrItems.getColumnIndex("SGSTRate"));
                        double cessRate = crsrItems.getDouble(crsrItems.getColumnIndex("cessRate"));


                        SupplierItemLinkageModel data = new SupplierItemLinkageModel(suppliercode, supplierName,
                                supplierPhone,supplierAddress,supplierGSTIN,
                                menuCode,itemName,barcode,hsn,supplyType,uom,averageRate,
                                igstRate,cgstRate,sgstRate,cessRate);

                        supplierItemLinkageList.add(data);
                    }


                } while (crsrMenuCode.moveToNext());
            }
        }
        if(supplierItemLinkageList.size()==0)
        {
            Toast.makeText(myContext, "No Item is linked to suppliers", Toast.LENGTH_SHORT).show();
        }else if(supplierItemLinkageAdapter == null)
        {
            supplierItemLinkageAdapter = new SupplierItemLinkageAdapter(supplierItemLinkageList, myContext);
            lstSupplieritem.setAdapter(supplierItemLinkageAdapter);
        }else
        {
            supplierItemLinkageAdapter.notifyDataSetChanged(supplierItemLinkageList);
        }
    }

    private void DisplayItems(String itemName ) {
        Cursor crsrItems = null;
        try {

            if(supplierItemLinkageList!=null)
                supplierItemLinkageList.clear();
            supplierItemLinkageList = new ArrayList<>();
            crsrItems = dbSupplierItemLink.getItemDetail_inward(itemName);
            if (crsrItems != null && crsrItems.moveToNext()) {
                int menuCode = crsrItems.getInt(crsrItems.getColumnIndex("MenuCode"));
                String barcode = crsrItems.getString(crsrItems.getColumnIndex("ItemBarcode"));
                String hsn = crsrItems.getString(crsrItems.getColumnIndex("HSNCode"));
                String supplyType = crsrItems.getString(crsrItems.getColumnIndex("SupplyType"));
                String uom = crsrItems.getString(crsrItems.getColumnIndex("UOM"));
                double averageRate = crsrItems.getDouble(crsrItems.getColumnIndex("AverageRate"));
                double igstRate = crsrItems.getDouble(crsrItems.getColumnIndex("IGSTRate"));
                double cgstRate = crsrItems.getDouble(crsrItems.getColumnIndex("CGSTRate"));
                double sgstRate = crsrItems.getDouble(crsrItems.getColumnIndex("SGSTRate"));
                double cessRate = crsrItems.getDouble(crsrItems.getColumnIndex("cessRate"));

                Cursor supplierCodecrsr = dbSupplierItemLink.getLinkedSupplierCodeForItem(menuCode);
                while(supplierCodecrsr!=null && supplierCodecrsr.moveToNext())
                {
                    int suppliercode = supplierCodecrsr.getInt(supplierCodecrsr.getColumnIndex("SupplierCode"));
                    Cursor supplierDetailscrsr = dbSupplierItemLink.getSupplierDetails_forcode(suppliercode);
                    while (supplierDetailscrsr!=null && supplierDetailscrsr.moveToNext())
                    {
                        String supplierName = supplierDetailscrsr.getString(supplierDetailscrsr.getColumnIndex("SupplierName"));
                        String supplierPhone = supplierDetailscrsr.getString(supplierDetailscrsr.getColumnIndex("SupplierPhone"));
                        String supplierAddress = supplierDetailscrsr.getString(supplierDetailscrsr.getColumnIndex("SupplierAddress"));
                        String supplierGSTIN = supplierDetailscrsr.getString(supplierDetailscrsr.getColumnIndex("GSTIN"));
                        SupplierItemLinkageModel data = new SupplierItemLinkageModel(suppliercode, supplierName,
                                supplierPhone,supplierAddress,supplierGSTIN,
                                menuCode,itemName,barcode,hsn,supplyType,uom,averageRate,
                                igstRate,cgstRate,sgstRate,cessRate);

                        supplierItemLinkageList.add(data);
                    }

                }

            }
            if(supplierItemLinkageList.size()==0)
            {
                Toast.makeText(myContext, "This Item is not linked to any supplier", Toast.LENGTH_SHORT).show();
            }else if(supplierItemLinkageAdapter == null)
            {
                supplierItemLinkageAdapter = new SupplierItemLinkageAdapter(supplierItemLinkageList, myContext);
                lstSupplieritem.setAdapter(supplierItemLinkageAdapter);
            }else
            {
                supplierItemLinkageAdapter.notifyDataSetChanged(supplierItemLinkageList);
            }

        }
        catch (Exception e)
        {
            MsgBox.Show(" Error", e.getMessage());
            e.printStackTrace();
        }
    }


    private void DisplayItems(int suppliercode , String supplierName, String supplierGSTIN,
                              String supplierPhone, final String supplierAddress) {
        Cursor crsrItems = null;
        if(supplierItemLinkageList!=null)
            supplierItemLinkageList.clear();
        supplierItemLinkageList=new ArrayList<>();
        Cursor crsrItemMenuCode = dbSupplierItemLink.getLinkedMenuCodeForSupplier(suppliercode);
        if (crsrItemMenuCode!=null && crsrItemMenuCode.moveToFirst())
        {
            do {

                int menuCode = crsrItemMenuCode.getInt(crsrItemMenuCode.getColumnIndex("MenuCode"));
                crsrItems = dbSupplierItemLink.getItem_inward(menuCode);
                if(crsrItems!=null && crsrItems.moveToNext())
                {
                    String itemName = crsrItems.getString(crsrItems.getColumnIndex("ItemName"));
                    String barcode = crsrItems.getString(crsrItems.getColumnIndex("ItemBarcode"));
                    String hsn = crsrItems.getString(crsrItems.getColumnIndex("HSNCode"));
                    String supplyType = crsrItems.getString(crsrItems.getColumnIndex("SupplyType"));
                    String uom = crsrItems.getString(crsrItems.getColumnIndex("UOM"));
                    double averageRate = crsrItems.getDouble(crsrItems.getColumnIndex("AverageRate"));
                    double igstRate = crsrItems.getDouble(crsrItems.getColumnIndex("IGSTRate"));
                    double cgstRate = crsrItems.getDouble(crsrItems.getColumnIndex("CGSTRate"));
                    double sgstRate = crsrItems.getDouble(crsrItems.getColumnIndex("SGSTRate"));
                    double cessRate = crsrItems.getDouble(crsrItems.getColumnIndex("cessRate"));


                    SupplierItemLinkageModel data = new SupplierItemLinkageModel(suppliercode, supplierName,
                            supplierPhone,supplierAddress,supplierGSTIN,
                            menuCode,itemName,barcode,hsn,supplyType,uom,averageRate,
                            igstRate,cgstRate,sgstRate,cessRate);

                    supplierItemLinkageList.add(data);

                }
            } while (crsrItemMenuCode.moveToNext());
        }
        if(supplierItemLinkageList.size()==0)
        {
            Toast.makeText(myContext, "No items found for "+supplierName, Toast.LENGTH_SHORT).show();
            Log.d("DisplayItem", "No Item found for supplier : "+supplierName);
        }else if(supplierItemLinkageAdapter == null)
        {
            supplierItemLinkageAdapter = new SupplierItemLinkageAdapter(supplierItemLinkageList, myContext);
            lstSupplieritem.setAdapter(supplierItemLinkageAdapter);
        }else
        {
            supplierItemLinkageAdapter.notifyDataSetChanged(supplierItemLinkageList);
        }
        //DisplayItems( suppliercode);
    }


        /*private boolean IsItemExists_withSupplier(String ItemFullName, int supplierCode_recieved) {
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
        }*/

    private void Reset() {
        sema_display=ALL;

        tv_suppliercode.setText("-1");
        autocompletetv_suppliername.setText("");
        edt_supplierGSTIN.setText("");
        autocompletetv_supplierPhn.setText("");
        et_inw_supplierAddress.setText("");

        strMenuCode = "";
        autocomplete_inw_ItemName.setText("");
        tv_menucode.setText("-1");
        et_inw_ItemBarcode.setText("");
        et_Inw_AverageRate.setText("0.00");
        et_inw_HSNCode.setText("");

       // spnr_supplytype.setSelection(0);
      //  spnr_supplytype.setEnabled(true);

        spnr_supplytype.setText("");
        spnrUOM.setText("");

        et_Inw_SGSTRate.setText("0");
        et_Inw_CGSTRate.setText("0");
        et_Inw_IGSTRate.setText("0");
        et_Inw_cessRate.setText("0");

        btnLinkItem.setEnabled(false);
        btnDelinkItem.setEnabled(false);


    }



    public void ClearingAndDisplaying()
    {
;
        if(sema_display == ALL)
        {

            DisplayItems(-1);
        }else if(sema_display == SUPPLIERWISE)
        {
            String suppliercode = tv_suppliercode.getText().toString();
            String suppliername = autocompletetv_suppliername.getText().toString();
            String supplierphn = autocompletetv_supplierPhn.getText().toString();
            String supplierAddress = et_inw_supplierAddress.getText().toString();
            String supplierGSTIN = edt_supplierGSTIN.getText().toString();
            int suppcode = -1;
            if (suppliercode== null || suppliercode.equals(""))
                suppcode = -1;
            else
                suppcode = Integer.parseInt(suppliercode);

            DisplayItems(suppcode, suppliername,supplierGSTIN, supplierphn, supplierAddress);


        } else // Itemwise
        {
            String itemname = autocomplete_inw_ItemName.getText().toString();
            DisplayItems(itemname);
        }

        Reset();
    }

    public void Clear(View v) {
        Reset();
        loadAutoCompleteData();
        ClearingAndDisplaying();
    }

    public void Close(View v) {
        dbSupplierItemLink.CloseDatabase();
        finish();
    }


    public void loadAutoCompleteData_item(int suppliercode) {

        String suppliername_str = autocompletetv_suppliername.getText().toString().toUpperCase();
        List<String> itemlist = dbSupplierItemLink.getitemlist_inward_nonGST(suppliername_str, suppliercode);

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(myContext,android.R.layout.simple_list_item_1,itemlist);

        dataAdapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);

        // attaching data adapter to spinner
        autocomplete_inw_ItemName.setAdapter(dataAdapter1);

    }


    public void loadAutoCompleteData()
    {
        Cursor cursor = dbSupplierItemLink.getAllSupplierName_nonGST();
        labelsSupplierName = new ArrayList<String>();
        labelsSupplierPhn = new ArrayList<String>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                labelsSupplierName.add(cursor.getString(cursor.getColumnIndex("SupplierName")));// adding
                labelsSupplierPhn.add(cursor.getString(cursor.getColumnIndex("SupplierPhone")));// adding
            } while (cursor.moveToNext());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(myContext, android.R.layout.simple_list_item_1,
                labelsSupplierName);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        autocompletetv_suppliername.setAdapter(dataAdapter);


        /*ArrayAdapter<String> dataAdapter_phn = new ArrayAdapter<String>(myContext, android.R.layout.simple_list_item_1,
                labelsSupplierPhn);
        dataAdapter_phn.setDropDownViewResource(android.R.layout.simple_list_item_1);*/
        //autocompletetv_supplierPhn.setAdapter(dataAdapter_phn);


        Cursor cursor_item = dbSupplierItemLink.getAllInwardItemNames();
        itemlist = new ArrayList<String>();
        if (cursor_item != null && cursor_item.moveToFirst()) {
            do {
                itemlist.add(cursor_item.getString(cursor_item.getColumnIndex("ItemName")));// adding
            } while (cursor_item.moveToNext());
        }
        dataAdapter = new ArrayAdapter<String>(myContext, android.R.layout.simple_list_item_1,itemlist);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        autocomplete_inw_ItemName.setAdapter(dataAdapter);

    }
//    private int getIndex(String substring){
//
//        int index = 0;
//        for (int i = 0; index==0 && i < spnrUOM.getCount(); i++){
//
//            if (spnrUOM.getItemAtPosition(i).toString().contains(substring)){
//                index = i;
//            }
//
//        }
//
//        return index;
//
//    }

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
                            dbSupplierItemLink.CloseDatabase();
                            finish();
                        }
                    })
                    .show();
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onHomePressed() {
        ActionBarUtils.navigateHome(this);
    }



}
