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
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.Database.Item;
import com.wep.common.app.Database.PurchaseOrder;
import com.wep.common.app.WepBaseActivity;
import com.wepindia.pos.GenericClasses.DateTime;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.adapters.PurchaseOrderAdapter;
import com.wepindia.pos.utils.ActionBarUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PurchaseOrderActivity extends WepBaseActivity {


    Context myContext;
    // DatabaseHandler_gst object
    DatabaseHandler dbPurchaseOrder;
    ArrayList<PurchaseOrder> dataList;
    //Message dialog object
    public MessageDialog MsgBox;
    long edit = 0;
    Spinner spnr_inwalrd_item_list;
    EditText et_inward_item_quantity,et_supplier_GSTIN, et_inward_sub_total,tx_inward_supply_invoice_number,et_inward_grand_total, et_inward_additionalchargename,
            et_inward_additionalchargeamount,et_supplier_address,et_supplier_phone,et_supplier_code;

    TextView tx_inward_invoice_date;

    //ImageButton btnimage_new_item ;
    AutoCompleteTextView autocompletetv_suppliername, autocompletetv_invoiceno,autocompletetv_itemlist, autocompletetv_purchase_order;
    ListView lv_inward_item_details;
    CheckBox chk_inward_additional_charge;
    com.wep.common.app.views.WepButton btnEditPO,btnGeneratePO,btnAddSupplier, btnClearItem, btnCloseItem, btn_add_new_item;
    PurchaseOrderAdapter purchaseOrderAdapter = null;

    ArrayList<String> labelsSupplierName;
    int count =0;
    long lRowId = 0;
    String Item_name , Item_rate, Item_quantity , Item_supplytype , Item_uom, Item_saletax, Item_servicetax ;
    String strDate= "";
    String strUserName="", strUserId ="";
    Date d;
    CheckBox chk_interState;
    Spinner spnrSupplierStateCode;

    Cursor purchase_crsr;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_order);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbPurchaseOrder = new DatabaseHandler(PurchaseOrderActivity.this);
        myContext = this;
        MsgBox =  new MessageDialog(myContext);
        purchase_crsr = null;

        try {
            strUserName = getIntent().getStringExtra("USER_NAME");
            strUserId = getIntent().getStringExtra("USER_ID");


            //tvTitleUserName.setText(strUserName.toUpperCase());
            d = new Date();
            CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
            //tvTitleDate.setText("Date : " + s);
            com.wep.common.app.ActionBarUtils.setupToolbar(PurchaseOrderActivity.this,toolbar,getSupportActionBar()," Purchase Order ",strUserName," Date:"+s.toString());

            InitializeViews();
            dbPurchaseOrder.CreateDatabase();
            dbPurchaseOrder.OpenDatabase();
            reset_inward(0);

            autocompletetv_suppliername.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event){
                    autocompletetv_suppliername.showDropDown();
                    return false;
                }
            });
            autocompletetv_suppliername.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int suppliercode = -1;
                    String suppliername_str = autocompletetv_suppliername.getText().toString().toUpperCase();
                    Cursor supplierdetail_cursor = dbPurchaseOrder.getSupplierDetails(suppliername_str);
                    if (supplierdetail_cursor!=null && supplierdetail_cursor.moveToFirst())
                    {
                        et_supplier_phone.setText(supplierdetail_cursor.getString(supplierdetail_cursor.getColumnIndex("SupplierPhone")));
                        et_supplier_address.setText(supplierdetail_cursor.getString(supplierdetail_cursor.getColumnIndex("SupplierAddress")));
                        suppliercode= supplierdetail_cursor.getInt(supplierdetail_cursor.getColumnIndex("SupplierCode"));
                        String suppliergstin= supplierdetail_cursor.getString(supplierdetail_cursor.getColumnIndex("GSTIN"));
                        if(suppliergstin!=null)
                            et_supplier_GSTIN.setText(suppliergstin);
                        et_supplier_code.setText(String.valueOf(suppliercode));
                        loadAutoCompleteData_item(suppliercode);
                        loadAutoCompleteData_purchaseOrder(suppliercode);
                        autocompletetv_purchase_order.setText("");
                        autocompletetv_itemlist.setText("");
                        btnAddSupplier.setEnabled(false);
                        if(dataList!=null ) {
                            dataList.clear();
                            if(purchaseOrderAdapter!=null)
                                purchaseOrderAdapter.notifyDataSetChanged(dataList);
                        }
                    }
                }
            });

            autocompletetv_purchase_order.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event){
                        autocompletetv_purchase_order.showDropDown();
                    return false;
                }
            });


            autocompletetv_purchase_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    btnEditPO.setEnabled(true);
                    btnGeneratePO.setEnabled(false);
                    loadTableonPurchaseOrderSelected();
                }
            });


            autocompletetv_itemlist.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event){
                    autocompletetv_itemlist.showDropDown();
                    return false;
                }
            });
            /*autocompletetv_itemlist.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event){
                    int suppliercode = Integer.parseInt(et_supplier_code.getText().toString());
                    if(suppliercode == 0)
                    {
                        autocompletetv_itemlist.setInputType(InputType.TYPE_NULL);
                        Toast.makeText(myContext, "Please Select the Supplier First", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                        autocompletetv_itemlist.showDropDown();
                        autocompletetv_itemlist.setInputType(InputType.TYPE_CLASS_TEXT);
                    }

                    return false;
                }
            });*/

            autocompletetv_itemlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String itemname = autocompletetv_itemlist.getText().toString();
                    if (itemname.equalsIgnoreCase("Not in list") || itemname.equalsIgnoreCase("Add new item") ) {
                        et_inward_item_quantity.setBackgroundColor(getResources().getColor(R.color.grey));
                        et_inward_item_quantity.setEnabled(false);
                       // autocompletetv_itemlist.setText("");
                        //input_window();
                        MsgBox.Show("Warning","Kindly goto \"Supplier Item Linkage\" and add the desired item." +
                                "\nPlease save your data , if any , before leaving this screen");

                    }else if (itemname.equalsIgnoreCase("Select/Add Supplier"))
                    {
                        //MsgBox.Show("Insufficient Information", "Please Select/Add the Supplier");
                        et_inward_item_quantity.setEnabled(false);
                        et_inward_item_quantity.setBackgroundColor(getResources().getColor(R.color.grey));
                    }
                    else {
                        et_inward_item_quantity.requestFocus();
                        et_inward_item_quantity.setEnabled(true);
                        et_inward_item_quantity.setBackgroundColor(getResources().getColor(R.color.white));
                        // btnimage_new_item.setEnabled(true);
                    }
                }
            });


            chk_inward_additional_charge.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(chk_inward_additional_charge.isChecked() == false)
                    {
                        et_inward_additionalchargeamount.setText("");
                        et_inward_additionalchargename.setText("");
                        et_inward_additionalchargeamount.setEnabled(false);
                        et_inward_additionalchargename.setEnabled(false);

                    }
                    else
                    {
                        et_inward_additionalchargeamount.setText("");
                        et_inward_additionalchargename.setText("");
                        et_inward_additionalchargeamount.setEnabled(true);
                        et_inward_additionalchargename.setEnabled(true);
                    }
                }
            });

            et_inward_additionalchargeamount.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                public void afterTextChanged(Editable s) {
                    calculateSubTotal();

                }
            });


            btn_add_new_item.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MsgBox = new MessageDialog(myContext);
                    String item = autocompletetv_itemlist.getText().toString();
                    int suppliercode = Integer.parseInt(et_supplier_code.getText().toString());
                    if (suppliercode <1 || item.equalsIgnoreCase("Select/Add Supplier") || item.equals(""))
                    {

                        if(suppliercode <1)
                        {
                            MsgBox.setTitle("Insufficient Information")
                                    .setMessage(" Please Select/Add Supplier ")
                                    .setPositiveButton("OK", null)
                                    .show();
                        } else
                        {
                            MsgBox.setTitle("Insufficient Information")
                                    .setMessage(" Please Select Item ")
                                    .setPositiveButton("OK", null)
                                    .show();
                        }

                    } else if (item.equalsIgnoreCase("Not in list") ||  item.equalsIgnoreCase("Add new item") )
                    {
                        /*MsgBox.setTitle("Error")
                                .setMessage(" Please Select Item  ")
                                .setPositiveButton("OK", null)
                                .show();*/
                        //input_window();
                        MsgBox.Show("Warning","Kindly goto \"Supplier Item Linkage\" and add the desired item." +
                                "\nPlease save your data , if any , before leaving this screen");
                    }else   if (et_inward_item_quantity.getText().toString().equals(""))
                    {
                        et_inward_item_quantity.setBackgroundColor(Color.WHITE);
                        et_inward_item_quantity.setEnabled(true);
                        MsgBox.setTitle("Error")
                                .setMessage(" Please Enter the Quantity ")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                    else {
                        // check whether user has entered new item without going through inputwindow()
                        Cursor itemdetails = dbPurchaseOrder.getItemDetail_inward(item);
                        if (itemdetails!=null && itemdetails.moveToFirst()) {
                            populate(2);
                        }
                        else
                        {
                            MsgBox.setTitle(" Insufficient Information")
                                    .setMessage(" Item not found in database for this Supplier." +
                                            "\nKindly goto \"Supplier Item Linkage\" and add the desired item" +
                                            "\nPlease save your data , if any , before leaving this screen")
                                    .setPositiveButton("Ok",null)
                                    .show();
                        }
                    }
                }
            });

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            AlertDialog.Builder msg = new AlertDialog.Builder(myContext);
            msg.setMessage(""+e.getMessage())
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    void loadTableonPurchaseOrderSelected()
    {
        int suppliercode = Integer.parseInt(et_supplier_code.getText().toString());
        String purchaseOrder = autocompletetv_purchase_order.getText().toString();
        try
        {
            if (purchaseOrder.equalsIgnoreCase("Select/Add Supplier")) {
               // MsgBox.Show("Insufficient Information", "Please Select/Add the Supplier");
            }/*else if (purchaseOrder.equalsIgnoreCase(""))
            {
                MsgBox.Show("Insufficient Information", "Please Select the Purchase Order");
                return;
            }else if (purchaseOrder.equalsIgnoreCase("Make New Purchase Order")) {
                Cursor crsr = dbPurchaseOrder.getMaxPurchaseOrderNo();
                int Max =1;
                if (crsr != null && crsr.moveToFirst())
                {
                    Max = crsr.getInt(0);
                    Max++;
                }
                autocompletetv_purchase_order.setText(String.valueOf(Max));
                loadAutoCompleteData_item(suppliercode);
            }*/
            else {
                // valid Purchase Order No is selected
                Cursor purchaseorder_crsr = dbPurchaseOrder.getPurchaseOrderDetails(suppliercode,Integer.parseInt(purchaseOrder));
                int flag =0;
                count =0;

                while (purchaseorder_crsr != null &&  purchaseorder_crsr.moveToNext())
                {
                    if (flag ==0)
                    {
                        ClearTable();

                        String additionalamount = purchaseorder_crsr.getString(purchaseorder_crsr.getColumnIndex("AdditionalChargeAmount"));
                        if(additionalamount== null || additionalamount.equals("") || additionalamount.equals("0"))
                        {
                            chk_inward_additional_charge.setChecked(false);
                            et_inward_additionalchargename.setText("");
                            et_inward_additionalchargeamount.setText("");
                        }
                        else {
                            // some value of additional charge
                            chk_inward_additional_charge.setChecked(true);
                            et_inward_additionalchargename.setText(purchaseorder_crsr.getString(purchaseorder_crsr.getColumnIndex("AdditionalChargeName")));
                            et_inward_additionalchargeamount.setText(additionalamount);
                        }
                                                flag = 1;
                    }
                    purchase_crsr = null;
                    purchase_crsr = purchaseorder_crsr;
                    populate(3);

                }
                calculateSubTotal();
            }
        }
        catch (Exception e)
        {
            Log.e( "PurchaseOrder",""+e.getMessage());
            Toast.makeText(myContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    void input_window()
    {
        AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);

        LayoutInflater UserAuthorization = (LayoutInflater) myContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vwAuthorization = UserAuthorization.inflate(R.layout.activity_new_item_window, null);


        //final TextView tv_attracts_reverse_charge = (TextView) findViewById(R.id.tv_attracts_reverse_charge);
        //final Spinner spnr_attracts_reverse_charge = (Spinner) findViewById(R.id.spnr_attracts_reverse_charge);

        final Spinner spnr_input_supplytype = (Spinner)vwAuthorization.findViewById(R.id.spnr_input_supplytype);
        final EditText et_input_name = (EditText) vwAuthorization.findViewById(R.id.et_input_item);
        final EditText et_input_rate = (EditText) vwAuthorization.findViewById(R.id.et_input_rate);
        final EditText et_input_quantity = (EditText) vwAuthorization.findViewById(R.id.et_input_quantity);
        final Spinner spnr_input_uom = (Spinner) vwAuthorization.findViewById(R.id.spnr_input_uom);
        final EditText et_input_salestax = (EditText) vwAuthorization.findViewById(R.id.et_input_salestax);
        final EditText et_input_servicetax = (EditText) vwAuthorization.findViewById(R.id.et_input_servicetax);



        AuthorizationDialog
                .setTitle("New Item")
                .setView(vwAuthorization)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        et_inward_item_quantity.setBackgroundColor(Color.WHITE);
                        et_inward_item_quantity.setEnabled(true);
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Item_name = et_input_name.getText().toString().toUpperCase();
                        Item_supplytype  = spnr_input_supplytype.getSelectedItem().toString();
                        String rate = et_input_rate.getText().toString();
                        String quantity = et_input_quantity.getText().toString();
                        Item_uom = spnr_input_uom.getSelectedItem().toString();
                        String saletax_str = et_input_salestax.getText().toString();
                        String servicetax_str = et_input_servicetax.getText().toString();

                        if (rate == null || rate.equals(""))
                            Item_rate= "0.00";
                        else
                            Item_rate = String.format("%.2f",Float.parseFloat(et_input_rate.getText().toString()));
                        if (quantity== null || quantity.equals(""))
                            Item_quantity = "0.00";
                        else
                            Item_quantity = String.format("%.2f",Float.parseFloat(et_input_quantity.getText().toString()));
                        if (saletax_str == null || saletax_str.equals(""))
                            Item_saletax= "0.00";
                        else {
                            double saletax_d = Double.parseDouble(saletax_str);
                            if(saletax_d <0 || saletax_d > 99.99)
                            {
                                MsgBox.Show("Warning", "Please enter sales tax % between 0 and 99.99");
                                return;
                            }
                            Item_saletax = String.format("%.2f", saletax_d);
                        }
                        if (servicetax_str== null || servicetax_str.equals(""))
                            Item_servicetax = "0.00";
                        else{
                            double servicetax_d = Double.parseDouble(servicetax_str);
                            if(servicetax_d <0 || servicetax_d > 99.99)
                            {
                                MsgBox.Show("Warning", "Please enter service tax % between 0 and 99.99");
                                return;
                            }
                            Item_servicetax = String.format("%.2f",servicetax_d);
                        }

                        if (Item_name.equals("") ||  quantity.equals("") || rate.equals(""))
                        {
                            AlertDialog.Builder MsgBox = new AlertDialog.Builder(myContext);
                            MsgBox. setMessage("Item Name , value and Quantity cannot be  blank. Please all details")
                                    .setPositiveButton("OK", null)
                                    .show();
                            set_list_spnr();
                        }
                        else if (Item_uom.equals("") ||  Item_uom.equalsIgnoreCase("Unit"))
                        {
                            AlertDialog.Builder MsgBox = new AlertDialog.Builder(myContext);
                            MsgBox. setMessage("Please Select the UOM")
                                    .setPositiveButton("OK", null)
                                    .show();
                            set_list_spnr();
                        }
                        else
                        {
                            String mou_temp = Item_uom;
                            int length = mou_temp.length();
                            String mou = mou_temp.substring(length-3, length-1);
                            int suppliercode = Integer.parseInt(et_supplier_code.getText().toString());
                            if (suppliercode>0)
                            {
                                Cursor item_crsr = dbPurchaseOrder.getItemdetailsforSupplier(suppliercode, Item_name);
                                if (item_crsr != null && item_crsr.moveToNext()) {
                                    //Toast.makeText(myContext, "Warning Item already present", Toast.LENGTH_SHORT).show();
                                    MsgBox.Show("Warning", "Item already present");
                                }
                                else {
                                    String suppliername = autocompletetv_suppliername.getText().toString();
                                    lRowId = 0;

                                    InsertItem( suppliercode,  suppliername, Item_name,  ""/*barcode*/, Float.parseFloat(Item_rate),
                                            Float.parseFloat(Item_quantity), mou, ""/*ImageUri*/, Float.parseFloat(Item_saletax),
                                            Float.parseFloat(Item_servicetax),Item_supplytype);
                                    if(lRowId>0)
                                    {
                                        autocompletetv_itemlist.setText(Item_name);
                                        et_inward_item_quantity.setEnabled(true);
                                        et_inward_item_quantity.setBackgroundColor(Color.WHITE);
                                        et_inward_item_quantity.setText(Item_quantity);
                                        populate(2); // new entry
                                        //autocompletetv_itemlist.setText("");
                                        loadAutoCompleteData_item(suppliercode);
                                    }
                                    else
                                    {
                                        Toast.makeText(myContext, " Error in adding the item to database", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }
                })
                .show();
    }



    public  void GeneratePO(View v)
    {
        AlertDialog.Builder msg = new AlertDialog.Builder(myContext);
        msg.setPositiveButton("OK", null);


        String suppliername_str = autocompletetv_suppliername.getText().toString(),
                supplieraddress_str= et_supplier_address.getText().toString(),
                supplierphn = et_supplier_phone.getText().toString();
        int suppliercode = Integer.parseInt(et_supplier_code.getText().toString());

        if(suppliername_str.equals("") || supplieraddress_str.equals("") || supplierphn.equals("")){
            MsgBox.Show(" Insufficient Information ","Please fill Supplier Details");
        } else if (suppliercode< 0) { // details are filled but supplier not in database
            MsgBox.Show("Error ", " Supplier is not in Database. Please add supplier");
        } else if (purchaseOrderAdapter == null){
            MsgBox.Show("Insufficient Information ", " Please add item");
        }else if (chk_interState.isChecked() && spnrSupplierStateCode.getSelectedItem().toString().equals(""))
        {
            MsgBox.Show("Insufficient Information ", " Please select state for supplier");
        }else
        {
            String purchaseorderno = autocompletetv_purchase_order.getText().toString();
            if (purchaseorderno.equals("")) {
                // generate purchase order no
                Cursor crsr = dbPurchaseOrder.getMaxPurchaseOrderNo();
                int Max =1;
                if (crsr != null && crsr.moveToFirst())
                {
                    if(crsr.getInt(0) != -100)
                    {
                        Max = crsr.getInt(0)+1;
                    }
                }
                autocompletetv_purchase_order.setText(String.valueOf(Max));

                long saved = savePurchaseOrder();
                if (saved >0)// successfully added bill items
                {
                    Toast.makeText(myContext, "Purchase Order Generated :"+Max, Toast.LENGTH_SHORT).show();
                    Log.d(" PurchaseOrderActivty", " Purchase Order Generated : "+Max);
                    reset_inward(0);
                }
            }else {
                Cursor duplicacy_crsr = dbPurchaseOrder.checkduplicatePO(suppliercode, Integer.parseInt(purchaseorderno));
                if (duplicacy_crsr!= null && duplicacy_crsr.moveToFirst())
                {
                    MsgBox.setTitle(" Duplicate ")
                            .setMessage(" Purchase Order No already present for Supplier. Do you want to overwrite")
                            .setNegativeButton("No", null)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    edit = savePurchaseOrder();
                                    reset_inward(0);
                                }
                            })
                            .show();
                }else { // no duplicacy
                    long saved = savePurchaseOrder();
                    if (saved >0)// successfully added bill items
                    {
                        Toast.makeText(myContext, "Purchase Order Generated ", Toast.LENGTH_SHORT).show();
                        Log.d(" PurchaseOrderActivty", " Purchase Order Generated at "+saved);
                        reset_inward(0);
                    }
                }
            }
        }
    }

    long savePurchaseOrder()
    {
        // 1 -> save
        // 2- >Edit

        // Inserted Row Id in database table
        long lResult = 0;


        long result = 1;
        int menucode = 0;
        String additionalchargename = "", additionalCharge = "";

        try
        {

            String purchaseorderno = autocompletetv_purchase_order.getText().toString();
            String supp_name = autocompletetv_suppliername.getText().toString();
            int suppliercode  = Integer.parseInt(et_supplier_code.getText().toString());
            String sup_phone = et_supplier_phone.getText().toString();
            String sup_address = et_supplier_address.getText().toString();
            String sup_gstin = et_supplier_GSTIN.getText().toString();



            int deletedrows = dbPurchaseOrder.deletePurchaseOrder( suppliercode,Integer.parseInt(purchaseorderno));
            Log.d("InsertPurchaseOrder", deletedrows+" Rows deleted for Purchase Order "+purchaseorderno);


            for (PurchaseOrder po :dataList) {

                po.setPurchaseOrderNo(purchaseorderno);
                if(chk_interState.isChecked())
                {
                    String supplier_stateCode = spnrSupplierStateCode.getSelectedItem().toString();
                    int l = supplier_stateCode.length();
                    String state_cd = supplier_stateCode.substring(l-2,l);
                    po.setSupplierPOS(state_cd);
                }else
                {
                    po.setSupplierPOS("");
                }
                if(chk_inward_additional_charge.isChecked())
                {
                    po.setAdditionalCharge(et_inward_additionalchargename.getText().toString());
                    po.setAdditionalChargeAmount(Double.parseDouble(et_inward_additionalchargeamount.getText().toString()));
                }
                else
                {
                    po.setAdditionalCharge("");
                    po.setAdditionalChargeAmount(0);
                }
                lResult = dbPurchaseOrder.InsertPurchaseOrder(po);
                if(lResult>0)
                    Log.d("InsertPurchaseOrder", " item inserted at position:" + lResult);
                //Cursor duplicacy_crsr = dbPurchaseOrder.checkDuplicatePurchaseOrder(suppliercode,menucode,Integer.parseInt(purchaseorderno));
//                if (duplicacy_crsr == null || !duplicacy_crsr.moveToFirst())
//                {
//                    // item not present for that purchase order
//                    lResult = dbPurchaseOrder.InsertPurchaseOrder(po);
//                    Log.d("InsertPurchaseOrder", " item inserted at position:" + lResult);
//                }
//                else
//                { // already present
//                    lResult = dbPurchaseOrder.UpdatePurchaseOrder(po);
//                    Log.d("InsertPurchaseOrder", "item updated at position:" + lResult);
//                }

            }
        } catch (Exception e) {
            Toast.makeText(myContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("log", e.getMessage(), e);
            result = -1;
        }
        return result;
    }


    public  void EditPO(View v)
    {
        String supplieraddress_str = et_supplier_address.getText().toString();
        String suppliername_str = autocompletetv_suppliername.getText().toString(),
                supplierphn = et_supplier_phone.getText().toString();
        int suppliercode = Integer.parseInt(et_supplier_code.getText().toString());
        String purchaseorder = autocompletetv_purchase_order.getText().toString();

        if(suppliername_str.equals("") || supplieraddress_str.equals("") || supplierphn.equals("")){
            MsgBox.Show(" Insufficient Information ","Please fill Supplier Details");
        } else if (suppliercode< 0) { // details are filled but supplier not in database
            MsgBox.Show("Error ", " Supplier is not in Database. Please add supplier");
        } else if (purchaseOrderAdapter == null){
            MsgBox.Show("Insufficient Information ", " Please add item");
        }else if (purchaseorder.equals(""))
        { MsgBox.Show(" Insufficent Information", " Please Select Purchase Order");
        }else if (chk_interState.isChecked() && spnrSupplierStateCode.getSelectedItem().toString().equals(""))
        {
            MsgBox.Show("Insufficient Information ", " Please select state for supplier");
        }else
        {
            Cursor duplicacy_crsr = dbPurchaseOrder.checkduplicatePO(suppliercode, Integer.parseInt(purchaseorder));
            if (duplicacy_crsr!= null && duplicacy_crsr.moveToFirst())
            {
                long saved = savePurchaseOrder();
                if (saved >0)// successfully added bill items
                {
                    Toast.makeText(myContext, "Purchase Order Updated ", Toast.LENGTH_SHORT).show();
                    Log.d(" PurchaseOrderActivty", " Purchase Order Updated at "+saved);
                    reset_inward(0);
                }
            }else { // no duplicacy
                MsgBox.setTitle(" Error ")
                        .setMessage(" This Purchase Order No is not present. Do you want to create new ")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                edit = savePurchaseOrder();
                                reset_inward(0);
                            }
                        })
                        .show();
            }
        }
    }

    void InitializeViews()
    {
        lv_inward_item_details = (ListView) findViewById(R.id.lv_inward_item_details);
        chk_interState = (CheckBox) findViewById(R.id.chk_interState);
        spnrSupplierStateCode = (Spinner) findViewById(R.id.spnrSupplierStateCode);
        et_supplier_code = (EditText) findViewById(R.id.et_supplier_code);
        autocompletetv_suppliername = (AutoCompleteTextView)findViewById(R.id.autocompletetv_suppliername);
        et_supplier_address = (EditText) findViewById(R.id.et_supplier_address);
        et_supplier_phone = (EditText) findViewById(R.id.et_supplier_phone);

        autocompletetv_invoiceno = (AutoCompleteTextView)findViewById(R.id.autocompletetv_invoiceno);
        tx_inward_supply_invoice_number = (EditText) findViewById(R.id.tx_inward_supply_invoice_number);
        tx_inward_invoice_date = (TextView) findViewById(R.id.tx_inward_invoice_date);
        autocompletetv_purchase_order = (AutoCompleteTextView)findViewById(R.id.autocompletetv_purchase_order);
        autocompletetv_itemlist = (AutoCompleteTextView)findViewById(R.id.autocompletetv_itemlist);
        et_inward_item_quantity = (EditText) findViewById(R.id.et_inward_item_quantity);
       // btnimage_new_item = (ImageButton) findViewById(R.id.btnimage_new_item);

        btnEditPO = (com.wep.common.app.views.WepButton) findViewById (R.id.btnEditPO);
        btnGeneratePO = (com.wep.common.app.views.WepButton) findViewById (R.id.btnGeneratePO);
        btnClearItem = (com.wep.common.app.views.WepButton) findViewById (R.id.btnClearItem);
        btnCloseItem = (com.wep.common.app.views.WepButton) findViewById (R.id.btnCloseItem);
        btnAddSupplier = (com.wep.common.app.views.WepButton) findViewById (R.id.btnAddSupplier);
        btn_add_new_item = (com.wep.common.app.views.WepButton) findViewById (R.id.btn_add_new_item);
        btnAddSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSupplier(v);
            }
        });
        btnGeneratePO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneratePO(v);
            }
        });

        btnClearItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clear_inward(v);
            }
        });

        btnCloseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Close_inward(v);
            }
        });
        btnEditPO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPO(v);
            }
        });
        chk_inward_additional_charge = (CheckBox) findViewById(R.id.chk_inward_additional_charge);
        et_inward_additionalchargeamount = (EditText) findViewById(R.id.et_inward_additionalchargeamount);
        et_inward_additionalchargename  = (EditText) findViewById(R.id.et_inward_additionalchargename);
        et_inward_sub_total = (EditText) findViewById(R.id.et_inward_sub_total);
        et_supplier_GSTIN = (EditText) findViewById(R.id.et_supplier_GSTIN);
        et_inward_grand_total = (EditText) findViewById(R.id.et_inward_grand_total);

        chk_interState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*if(purchaseOrderAdapter== null || dataList== null || dataList.size()==0)
                    return;*/
                if(!isChecked)
                {

                    spnrSupplierStateCode.setSelection(0);
                    spnrSupplierStateCode.setEnabled(false);
                    if(purchaseOrderAdapter== null || dataList== null || dataList.size()==0)
                        return;
                    for(PurchaseOrder po : dataList)
                    {
                        double sgstAmt =0, cgstAmt =0;
                        double taxval = po.getTaxableValue();
                        String suppliercode = po.getSupplierCode();
                        String itemname = po.getItemName();

                        Cursor crsr = dbPurchaseOrder.getItemDetail_inward(itemname);
                        if(crsr!=null && crsr.moveToNext())
                        {
                            double cgstRate = crsr.getDouble(crsr.getColumnIndex("CGSTRate"));
                            double sgstRate = crsr.getDouble(crsr.getColumnIndex("SGSTRate"));
                            sgstAmt= taxval*sgstRate/100;
                            cgstAmt= taxval*cgstRate/100;
                        }
                        po.setIgstAmount(0);
                        po.setCgstAmount(cgstAmt);
                        po.setSgstAmount(sgstAmt);
                    }
                    purchaseOrderAdapter.notifyNewDataAdded(dataList);

                }
                else
                {
                    spnrSupplierStateCode.setSelection(0);
                    spnrSupplierStateCode.setEnabled(true);
                    if(purchaseOrderAdapter== null || dataList== null || dataList.size()==0)
                        return;
                    for(PurchaseOrder po : dataList)
                    {
                        double sgst = po.getSgstAmount();
                        double cgst = po.getCgstAmount();
                        po.setIgstAmount(sgst+cgst);
                        po.setCgstAmount(0);
                        po.setSgstAmount(0);
                    }
                    purchaseOrderAdapter.notifyNewDataAdded(dataList);
                }

            }
        });

    }

    private void loadAutoCompleteData_purchaseOrder(int suppliercode) {
        // loading Purchase order for that supplier
        List<String> invoicelist = dbPurchaseOrder.getPurchaseOrderlist(suppliercode);
        final ArrayAdapter<String> dataAdapter11 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,invoicelist);
        dataAdapter11.setDropDownViewResource(android.R.layout.simple_list_item_1);
        // attaching data adapter to spinner
        autocompletetv_purchase_order.setAdapter(dataAdapter11);


    }

    private void loadAutoCompleteData_item(int suppliercode) {

        String suppliername_str = autocompletetv_suppliername.getText().toString().toUpperCase();
        List<String> itemlist = new ArrayList<>();
        itemlist.add("Not in list");
        Cursor menuCodeListcrsr= dbPurchaseOrder.getLinkedMenuCodeForSupplier(suppliercode);
        while (menuCodeListcrsr!=null && menuCodeListcrsr.moveToNext())
        {
            int menucode = menuCodeListcrsr.getInt(menuCodeListcrsr.getColumnIndex("MenuCode"));
            Cursor itemDetail = dbPurchaseOrder.getItem_inward(menucode);
            if(itemDetail!=null && itemDetail.moveToNext())
            {
                String itemName = itemDetail.getString(itemDetail.getColumnIndex("ItemName"));
                itemlist.add(itemName);
            }
        }

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,itemlist);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);

        // attaching data adapter to spinner
        autocompletetv_itemlist.setAdapter(dataAdapter1);

    }


    public int getIndex_POS(String item)
    {
        List<String> pos = Arrays.asList(getResources().getStringArray(R.array.poscode));
        int count =0;
        for (String pos_temp : pos)
        {
            if(pos_temp.contains(item))
                return count;
            count++;
        }
        return 0;
    }

    void populate(int type)
    {
        //1 - new item entry
        //2 - existing item in database
        // 3 - popualte table from saved purchase order


        try {

            if (type == 3)
            {
                if (purchase_crsr ==null)
                {
                    return;
                }

                PurchaseOrder po = new PurchaseOrder();

                String gstin = et_supplier_GSTIN.getText().toString();
                po.setSupplierCode((et_supplier_code.getText().toString()));
                po.setSupplierGSTIN(gstin);
                po.setSupplierName(autocompletetv_suppliername.getText().toString());
                po.setSupplierPhone(et_supplier_phone.getText().toString());
                po.setSupplierAddress(et_supplier_address.getText().toString());
                po.setSupplierPOS(purchase_crsr.getString(purchase_crsr.getColumnIndex("SupplierPOS")));
                if(po.getSupplierPOS()== null || po.getSupplierPOS().equals(""))
                {
                    chk_interState.setChecked(false);
                    spnrSupplierStateCode.setEnabled(false);
                }
                else
                {
                    chk_interState.setChecked(true);
                    spnrSupplierStateCode.setEnabled(true);
                    spnrSupplierStateCode.setSelection(getIndex_POS(po.getSupplierPOS()));
                }


                if(gstin.equals(""))
                    po.setSupplierType("UnRegistered");
                else
                    po.setSupplierType("Registered");

                po.setMenuCode(purchase_crsr.getInt(purchase_crsr.getColumnIndex("MenuCode")));
                po.setItemName(purchase_crsr.getString(purchase_crsr.getColumnIndex("ItemName")));
                po.setSupplyType(purchase_crsr.getString(purchase_crsr.getColumnIndex("SupplyType")));
                po.setHSNCode(purchase_crsr.getString(purchase_crsr.getColumnIndex("HSNCode")));
                String quantity_str = purchase_crsr.getString(purchase_crsr.getColumnIndex("Quantity"));
                po.setQuantity(Double.parseDouble(String.format("%.2f", Float.parseFloat(quantity_str))));


                String Rate = purchase_crsr.getString(purchase_crsr.getColumnIndex("Value"));
                if (Rate == null || Rate.equals(""))
                    po.setValue(0.00);
                else
                    po.setValue(Double.parseDouble(String.format("%.2f", Float.parseFloat(Rate))));


                Item_quantity = purchase_crsr.getString(purchase_crsr.getColumnIndex("Quantity"));
                if (Item_quantity == null || Item_quantity.equals(""))
                    po.setQuantity(0);
                else
                    po.setQuantity(Double.parseDouble(String.format("%.2f", Float.parseFloat(Item_quantity))));

                double taxval = purchase_crsr.getDouble(purchase_crsr.getColumnIndex("TaxableValue"));
                po.setTaxableValue(Double.parseDouble(String.format("%.2f", taxval)));

                String uom_str = purchase_crsr.getString(purchase_crsr.getColumnIndex("UOM"));
                po.setUOM(uom_str);



                String cgstRate_str = purchase_crsr.getString(purchase_crsr.getColumnIndex("CGSTRate"));
                if (cgstRate_str== null || cgstRate_str.equals(""))
                    cgstRate_str ="0.00";

                String sgstRate_str = purchase_crsr.getString(purchase_crsr.getColumnIndex("SGSTRate"));
                if (sgstRate_str== null || sgstRate_str.equals(""))
                    sgstRate_str =("0.00");

                String cgstAmt_str = purchase_crsr.getString(purchase_crsr.getColumnIndex("CGSTAmount"));
                if (cgstAmt_str== null || cgstAmt_str.equals(""))
                    cgstAmt_str ="0.00";

                String sgstAmt_str = purchase_crsr.getString(purchase_crsr.getColumnIndex("SGSTAmount"));
                if (sgstAmt_str== null || sgstAmt_str.equals(""))
                    sgstAmt_str =("0.00");

                String igstRate_str = purchase_crsr.getString(purchase_crsr.getColumnIndex("IGSTRate"));
                if (igstRate_str== null || igstRate_str.equals(""))
                    igstRate_str =("0.00");

                String igstAmt_str = purchase_crsr.getString(purchase_crsr.getColumnIndex("IGSTAmount"));
                if (igstAmt_str== null || igstAmt_str.equals(""))
                    igstAmt_str ="0.00";

                String cessRate_str = purchase_crsr.getString(purchase_crsr.getColumnIndex("cessRate"));
                if (cessRate_str== null || cessRate_str.equals(""))
                    cessRate_str =("0.00");

                String cessAmt_str = purchase_crsr.getString(purchase_crsr.getColumnIndex("cessAmount"));
                if (cessAmt_str== null || cessAmt_str.equals(""))
                    cessAmt_str ="0.00";

                po.setIgstRate(Double.parseDouble(igstRate_str));
                po.setIgstAmount(Double.parseDouble(igstAmt_str));
                po.setCgstRate(Double.parseDouble(cgstRate_str));
                po.setCgstAmount(Double.parseDouble(cgstAmt_str));
                po.setSgstRate(Double.parseDouble(sgstRate_str));
                po.setSgstAmount(Double.parseDouble(sgstAmt_str));
                po.setCsRate(Double.parseDouble(cessRate_str));
                po.setCsAmount(Double.parseDouble(cessAmt_str));

                double amt = purchase_crsr.getDouble(purchase_crsr.getColumnIndex("Amount"));
                po.setAmount(amt);
                po.setIsgoodInward(purchase_crsr.getString(purchase_crsr.getColumnIndex("isGoodinward")));
                String add_amt = purchase_crsr.getString(purchase_crsr.getColumnIndex("AdditionalChargeAmount"));
                if(add_amt!=null && !add_amt.equals("")&& !add_amt.equals("0"))
                {
                    et_inward_additionalchargename.setText(purchase_crsr.getString(purchase_crsr.getColumnIndex("AdditionalChargeName")));
                    et_inward_additionalchargeamount.setText(add_amt);
                    chk_inward_additional_charge.setChecked(true);
                    po.setAdditionalCharge(et_inward_additionalchargename.getText().toString());
                    po.setAdditionalChargeAmount(Double.parseDouble(et_inward_additionalchargeamount.getText().toString()));
                }
                dataList.add(po);

                set_list_spnr();
                calculateSubTotal();
                if (purchaseOrderAdapter == null) {
                    purchaseOrderAdapter = new PurchaseOrderAdapter(PurchaseOrderActivity.this, dbPurchaseOrder,dataList);
                    lv_inward_item_details.setAdapter(purchaseOrderAdapter);
                } else {
                    purchaseOrderAdapter.notifyNewDataAdded(dataList);
                }
            }
            else if(type ==2) // item existing in database
            {
                if (dataList== null)
                    dataList = new ArrayList<PurchaseOrder>();
                Item_name = autocompletetv_itemlist.getText().toString().toUpperCase();
                int suppliercode = Integer.parseInt(et_supplier_code.getText().toString());
                if (suppliercode <0)
                {
                    MsgBox = new MessageDialog(myContext);
                    MsgBox.Show("Insufficient Information ", " Please fill Supplier Details ");
                    return;
                }
                Cursor itemdetails = dbPurchaseOrder.getItemDetail_inward(Item_name);
                if (itemdetails!=null && itemdetails.moveToFirst()) {
                    PurchaseOrder po = new PurchaseOrder();
                    //po.setSn(dataList.size()+1);
                    String gstin = et_supplier_GSTIN.getText().toString();
                    po.setSupplierCode((et_supplier_code.getText().toString()));
                    po.setSupplierGSTIN(gstin);
                    po.setSupplierName(autocompletetv_suppliername.getText().toString());
                    po.setSupplierPhone(et_supplier_phone.getText().toString());
                    po.setSupplierAddress(et_supplier_address.getText().toString());
                    if(gstin.equals(""))
                        po.setSupplierType("UnRegistered");
                    else
                        po.setSupplierType("Registered");
                    po.setMenuCode(itemdetails.getInt(itemdetails.getColumnIndex("MenuCode")));
                    po.setItemName(Item_name);
                    po.setSupplyType(itemdetails.getString(itemdetails.getColumnIndex("SupplyType")));
                    po.setHSNCode(itemdetails.getString(itemdetails.getColumnIndex("HSNCode")));


                    String Rate = itemdetails.getString(itemdetails.getColumnIndex("AverageRate"));
                    if (Rate == null || Rate.equals(""))
                        po.setValue(0.00);
                    else
                        po.setValue(Double.parseDouble(String.format("%.2f", Float.parseFloat(Rate))));


                    Item_quantity = et_inward_item_quantity.getText().toString();
                    if (Item_quantity == null || Item_quantity.equals(""))
                        po.setQuantity(0);
                    else
                        po.setQuantity(Double.parseDouble(String.format("%.2f", Float.parseFloat(Item_quantity))));


                    float rate_f = Float.parseFloat(Rate);
                    float qty_f = Float.parseFloat(Item_quantity);
                    float taxval_f = rate_f * qty_f;
                    po.setTaxableValue(Double.parseDouble(String.format("%.2f", taxval_f)));

                    String uom_str = itemdetails.getString(itemdetails.getColumnIndex("UOM"));
                    po.setUOM(uom_str);

                    double cgstRate_d =0, sgstRate_d =0, igstRate_d = 0,cessRate_d=0;
                    double cgstAmt_d =0, sgstAmt_d =0, igstAmt_d = 0,cessAmt_d=0;

                    String cgstRate_str = itemdetails.getString(itemdetails.getColumnIndex("CGSTRate"));
                    if (cgstRate_str== null || cgstRate_str.equals(""))
                        cgstRate_str ="0.00";

                    String sgstRate_str = itemdetails.getString(itemdetails.getColumnIndex("SGSTRate"));
                    if (sgstRate_str== null || sgstRate_str.equals(""))
                        sgstRate_str =("0.00");

                    String igstRate_str = itemdetails.getString(itemdetails.getColumnIndex("IGSTRate"));
                    if (igstRate_str== null || igstRate_str.equals(""))
                        igstRate_str ="0.00";

                    String cessRate_str = itemdetails.getString(itemdetails.getColumnIndex("cessRate"));
                    if (cessRate_str== null || cessRate_str.equals(""))
                        cessRate_str =("0.00");

                    cgstRate_d = Double.parseDouble(cgstRate_str);
                    sgstRate_d = Double.parseDouble(sgstRate_str);
                    igstRate_d = Double.parseDouble(igstRate_str);
                    cessRate_d = Double.parseDouble(cessRate_str);
                    if(chk_interState.isChecked())
                    {
                        igstAmt_d = igstRate_d *taxval_f/100;
                        cgstRate_d = 0;
                        cgstAmt_d = 0;
                        sgstRate_d = 0;
                        sgstAmt_d = 0;
                    }
                    else
                    {
                        cgstAmt_d = cgstRate_d*taxval_f/100;
                        sgstAmt_d = sgstRate_d*taxval_f/100;
                        igstAmt_d =0;
                        igstRate_d=0;
                    }
                    cessAmt_d = cessRate_d*taxval_f/100;
                    po.setIgstRate(igstRate_d);
                    po.setIgstAmount(igstAmt_d);
                    po.setCgstRate(cgstRate_d);
                    po.setCgstAmount(cgstAmt_d);
                    po.setSgstRate(sgstRate_d);
                    po.setSgstAmount(sgstAmt_d);
                    po.setCsRate(cessRate_d);
                    po.setCsAmount(cessAmt_d);
                    double amount_f = taxval_f + cgstAmt_d +sgstAmt_d+igstAmt_d+cessAmt_d;
                    po.setAmount(amount_f);
                    po.setIsgoodInward("0");
                    if(chk_inward_additional_charge.isChecked())
                    {
                        po.setAdditionalCharge(et_inward_additionalchargename.getText().toString());
                        po.setAdditionalChargeAmount(Double.parseDouble(et_inward_additionalchargeamount.getText().toString()));
                    }
                    dataList.add(po);
                }
                set_list_spnr();
                calculateSubTotal();
                if (purchaseOrderAdapter == null) {
                    purchaseOrderAdapter = new PurchaseOrderAdapter(PurchaseOrderActivity.this, dbPurchaseOrder,dataList);
                    lv_inward_item_details.setAdapter(purchaseOrderAdapter);
                } else {
                    purchaseOrderAdapter.notifyNewDataAdded(dataList);
                }

            }// end of if(type ==2)
        }
        catch (Exception e)
        {
            Toast.makeText(myContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }
/*
void populate_old(int type)
    {
        //1 - new item entry
        //2 - existing item in database
        // 3 - popualte table from saved purchase order
        boolean flag_pos =true;

        TableRow tr = new TableRow(this);
        //tr.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

        TextView sn = new TextView(myContext);
        sn.setWidth(55);
        sn.setBackgroundResource(R.drawable.border_itemdatabase);
        count++;
        sn.setPadding(10,0,0,0);
        sn.setTextSize(20);
        sn.setText(String.valueOf(count));


        TextView Supplytype = new TextView(myContext);
        Supplytype.setWidth(55);
        Supplytype.setPadding(10,0,0,0);
        Supplytype.setTextSize(20);
        Supplytype.setBackgroundResource(R.drawable.border_itemdatabase);

        TextView name = new TextView(myContext);
        name.setBackgroundResource(R.drawable.border_itemdatabase);
        name.setWidth(225);
        name.setTextSize(20);
        name.setPadding(10,0,0,0);

        TextView rate = new TextView(myContext);
        rate.setWidth(110);
        rate.setPadding(0,0,10,0);
        rate.setGravity(Gravity.RIGHT);
        rate.setTextSize(20);
        rate.setBackgroundResource(R.drawable.border_itemdatabase);

        TextView qty = new TextView(myContext);
        qty.setWidth(90);
        qty.setPadding(0,0,10,0);
        qty.setGravity(Gravity.RIGHT);
        qty.setTextSize(20);
        qty.setBackgroundResource(R.drawable.border_itemdatabase);

        TextView uom = new TextView(myContext);
        uom.setWidth(110);
       // uom.setPadding(10,0,0,0);
        uom.setGravity(Gravity.CENTER);
        uom.setTextSize(20);
        uom.setBackgroundResource(R.drawable.border_itemdatabase);

        TextView taxvalue = new TextView(myContext);
        taxvalue.setWidth(170);
        taxvalue.setPadding(0,0,10,0);
        taxvalue.setGravity(Gravity.RIGHT);
        taxvalue.setTextSize(20);
        taxvalue.setBackgroundResource(R.drawable.border_itemdatabase);

        TextView Salestax = new TextView(myContext);
        Salestax.setWidth(110);
        Salestax.setPadding(0,0,10,0);
        Salestax.setGravity(Gravity.RIGHT);
        Salestax.setTextSize(20);
        Salestax.setBackgroundResource(R.drawable.border_itemdatabase);

        TextView Servicetax = new TextView(myContext);
        Servicetax.setWidth(110);
        Servicetax.setPadding(0,0,10,0);
        Servicetax.setGravity(Gravity.RIGHT);
        Servicetax.setTextSize(20);
        Servicetax.setBackgroundResource(R.drawable.border_itemdatabase);

        TextView Amount = new TextView(myContext);
        Amount.setWidth(155);
        Amount.setPadding(0,0,10,0);
        Amount.setGravity(Gravity.RIGHT);
        Amount.setTextSize(20);
        Amount.setBackgroundResource(R.drawable.border_itemdatabase);

        TextView MenuCode = new TextView(myContext);
        MenuCode.setWidth(130);
        MenuCode.setBackgroundResource(R.drawable.border_itemdatabase);


        // Delete


        Button ImgDelete = new Button(myContext);
        ImgDelete.setBackground(getResources().getDrawable(R.drawable.delete_icon_border));
        ImgDelete.setLayoutParams(new TableRow.LayoutParams(40, 35));
        //ImgDelete.setPadding(5,0,0,5);
        ImgDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final View v1 = v;
                AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);

                LayoutInflater UserAuthorization = (LayoutInflater) myContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View vwAuthorization = UserAuthorization.inflate(R.layout.deleteconfirmation, null);


                AuthorizationDialog
                        .setTitle("Confimation")
                        .setView(vwAuthorization)
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub

                                View row = (View) v1.getParent();
                                ViewGroup container = ((ViewGroup) row.getParent());
                                container.removeView(row);
                                container.invalidate();
                                calculateSubTotal();
                                */
/*int child = tbl_inward_item_details.getChildCount();
                                for(int i =0;i< child ;i++)
                                {
                                    if(i==0)
                                        count =0;

                                    TableRow row1 = (TableRow) tbl_inward_item_details.getChildAt(i);
                                    TextView Sn = (TextView) row1.getChildAt(0);
                                    count++;
                                    Sn.setText(String.valueOf(count));
                                }*//*

                            }
                        })
                        .show();

            }
        });

        */
/*ImgDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View row = (View) v.getParent();
                ViewGroup container = ((ViewGroup) row.getParent());
                container.removeView(row);
                container.invalidate();
                calculateSubTotal();
                int child = tbl_inward_item_details.getChildCount();
                for(int i =0;i< child ;i++)
                {
                    if(i==0)
                        count =0;

                    TableRow row1 = (TableRow) tbl_inward_item_details.getChildAt(i);
                    TextView Sn = (TextView) row1.getChildAt(0);
                    count++;
                    Sn.setText(String.valueOf(count));
                }
            }
        });*//*


        TextView spc = new TextView(myContext);
        spc.setWidth(5);

        Button btndel = new Button(myContext);
        btndel.setBackground(getResources().getDrawable(R.drawable.deletered1));
        btndel.setPadding(5,0,0,0);
        btndel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final View v1 = v;
                MsgBox = new MessageDialog(myContext);
                MsgBox.setTitle("Confirm")
                        .setMessage("Do you want to Delete this item ")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                View row = (View) v1.getParent();
                                ViewGroup container = ((ViewGroup) row.getParent());
                                container.removeView(row);
                                container.invalidate();
                                calculateSubTotal();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });
        */
/*btndel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final View v1 = v;
                AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);

                LayoutInflater UserAuthorization = (LayoutInflater) myContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View vwAuthorization = UserAuthorization.inflate(R.layout.deleteconfirmation, null);


                AuthorizationDialog
                        .setTitle("Confimation")
                        .setView(vwAuthorization)
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub

                                View row = (View) v1.getParent();
                                ViewGroup container = ((ViewGroup) row.getParent());
                                container.removeView(row);
                                container.invalidate();
                                calculateSubTotal();

                            }
                        })
                        .show();

            }
        });*//*

        */
/*btndel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View row = (View) v.getParent();
                ViewGroup container = ((ViewGroup) row.getParent());
                container.removeView(row);
                container.invalidate();
                calculateSubTotal();
            }
        });*//*


        try {


            if (type == 3)
            {
                if (purchase_crsr ==null)
                {
                    return;
                }

                String supplytype = purchase_crsr.getString(purchase_crsr.getColumnIndex("SupplyType"));
                Supplytype.setText(supplytype);

                String itemname = purchase_crsr.getString(purchase_crsr.getColumnIndex("ItemName"));
                name.setText(itemname);

                String rate_str = purchase_crsr.getString(purchase_crsr.getColumnIndex("Value"));
                rate.setText(String.format("%.2f", Float.parseFloat(rate_str)));

                String quantity_str = purchase_crsr.getString(purchase_crsr.getColumnIndex("Quantity"));
                qty.setText(String.format("%.2f", Float.parseFloat(quantity_str)));

                String uom_str = purchase_crsr.getString(purchase_crsr.getColumnIndex("UOM"));
                uom.setText(uom_str);

                String taxval_str = purchase_crsr.getString(purchase_crsr.getColumnIndex("TaxableValue"));
                taxvalue.setText(String.format("%.2f", Float.parseFloat(taxval_str)));

                String salestax_str = purchase_crsr.getString(purchase_crsr.getColumnIndex("SalesTax"));
                Salestax.setText(String.format("%.2f", Float.parseFloat(salestax_str)));

                String servicetax_str = purchase_crsr.getString(purchase_crsr.getColumnIndex("ServiceTaxAmount"));
                Servicetax.setText(String.format("%.2f", Float.parseFloat(servicetax_str)));

                String amount = purchase_crsr.getString(purchase_crsr.getColumnIndex("Amount"));
                Amount.setText(String.format("%.2f", Float.parseFloat(amount)));

                String menucode = purchase_crsr.getString(purchase_crsr.getColumnIndex("MenuCode"));
                MenuCode.setText(menucode);

                tr.addView(sn);
                tr.addView(Supplytype);
                tr.addView(name);
                tr.addView(rate);
                tr.addView(qty);
                tr.addView(uom);
                tr.addView(taxvalue);
                tr.addView(Salestax);
                tr.addView(Servicetax);
                tr.addView(Amount);
                tr.addView(MenuCode);
                tr.addView(spc);
               // tr.addView(btndel);
                tr.addView(ImgDelete);

               */
/* tbl_inward_item_details.addView(tr);*//*


            } else if(type ==2) // item existing in database
            {
                float salestaxamount =0 , servicetaxamount =0;
                Item_name = autocompletetv_itemlist.getText().toString().toUpperCase();
                int suppliercode = Integer.parseInt(et_supplier_code.getText().toString());
                if (suppliercode <0)
                {
                    MsgBox = new MessageDialog(myContext);
                    MsgBox.Show("Insufficient Information ", " Please fill Supplier Details ");
                    return;
                }
                Cursor itemdetails = dbPurchaseOrder.getItemdetailsforSupplier(suppliercode,Item_name);
                if (itemdetails!=null && itemdetails.moveToFirst()) {
                    name.setText(Item_name);

                    String supplytype = itemdetails.getString(itemdetails.getColumnIndex("SupplyType"));
                    Supplytype.setText(supplytype);

                    String Rate = itemdetails.getString(itemdetails.getColumnIndex("Rate"));
                    if (Rate == null || Rate.equals(""))
                        rate.setText("0.00");
                    else
                        rate.setText(String.format("%.2f", Float.parseFloat(Rate)));


                    Item_quantity = et_inward_item_quantity.getText().toString();
                    if (Item_quantity == null || Item_quantity.equals(""))
                        qty.setText("0.00");
                    else
                        qty.setText(String.format("%.2f", Float.parseFloat(Item_quantity)));


                    float rate_f = Float.parseFloat(Rate);
                    float qty_f = Float.parseFloat(Item_quantity);
                    float taxval_f = rate_f * qty_f;
                    taxvalue.setText(String.format("%.2f", taxval_f));

                    String uom_str = itemdetails.getString(itemdetails.getColumnIndex("UOM"));
                    uom.setText(uom_str);

                    float salestax_f =0, servicetax_f =0;

                    String salestax_str = itemdetails.getString(itemdetails.getColumnIndex("CGSTRate"));
                    if (salestax_str== null || salestax_str.equals(""))
                        Salestax.setText("0.00");
                    else
                    {
                        salestax_f = Float.parseFloat(salestax_str);
                        salestaxamount = salestax_f*taxval_f/100;
                        Salestax.setText(String.format("%.2f", salestaxamount));
                    }

                    String servicetax_str = itemdetails.getString(itemdetails.getColumnIndex("SGSTRate"));
                    if (servicetax_str== null || servicetax_str.equals(""))
                        Servicetax.setText("0.00");
                    else
                    {
                        servicetax_f = Float.parseFloat(servicetax_str);
                        servicetaxamount = servicetax_f *taxval_f/100;
                        Servicetax.setText(String.format("%.2f", servicetaxamount));

                    }

                    float amount_f = taxval_f + salestaxamount +servicetaxamount;
                    Amount.setText(String.format("%.2f", amount_f));

                    int menucode  = itemdetails.getInt(itemdetails.getColumnIndex("MenuCode"));
                    MenuCode.setText(String.valueOf(menucode));
                }

                tr.addView(sn);
                tr.addView(Supplytype);
                tr.addView(name);
                tr.addView(rate);
                tr.addView(qty);
                tr.addView(uom);
                tr.addView(taxvalue);
                tr.addView(Salestax);
                tr.addView(Servicetax);
                tr.addView(Amount);
                tr.addView(MenuCode);
                tr.addView(spc);
                //tr.addView(btndel);
                tr.addView(ImgDelete);



//                tbl_inward_item_details.addView(tr);
                set_list_spnr();
                calculateSubTotal();

            }// end of if(type ==2)
        }
        catch (Exception e)
        {
            Toast.makeText(myContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
*/

    public void calculateSubTotal()
    {
        float grandtotal_f =0;
        float subtotal_f =0;
        float additionalcharge_f = 0;
        TextView Amount;
        String amount_str = "0";
        for (PurchaseOrder po : dataList)
        {

            subtotal_f += po.getAmount();

        }
        et_inward_sub_total.setText(String.format("%.2f",subtotal_f));

        // addtional charge
        if (chk_inward_additional_charge.isChecked())
            if( et_inward_additionalchargeamount.getText().toString().equals(""))
            {
                additionalcharge_f =0;
            }
            else
            {
                additionalcharge_f = Float.parseFloat(et_inward_additionalchargeamount.getText().toString());
            }

        grandtotal_f = subtotal_f+ additionalcharge_f;
        et_inward_grand_total.setText(String.format("%.2f",grandtotal_f));

    }

    void set_list_spnr()
    {
        autocompletetv_itemlist.setText("");
        //autocompletetv_purchase_order.setText("");
        et_inward_item_quantity.setBackgroundColor(Color.WHITE);
        et_inward_item_quantity.setText("");
        et_inward_item_quantity.setEnabled(true);
    }

    public void dateSelection_inward (View v)
    {
        String currentdate = DateFormat.format("yyyy-MM-dd", (new Date()).getTime()).toString();

        DateTime objDate = new DateTime(currentdate);
        try {
            AlertDialog.Builder dlgReportDate = new AlertDialog.Builder(myContext);
            final DatePicker dateReportDate = new DatePicker(myContext);
            // Initialize date picker value to business date
            dateReportDate.updateDate(objDate.getYear(), objDate.getMonth(), objDate.getDay());
            String strMessage = " Select the date";


            dlgReportDate
                    .setIcon(R.drawable.ic_launcher)
                    .setTitle("Date Selection")
                    .setMessage(strMessage)
                    .setView(dateReportDate)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
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

                            tx_inward_invoice_date.setText(strDate);

                            Log.d("ReportDate", "Selected Date:" + strDate);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    })
                    .show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void Clear_inward(View v)
    {
        /*for (int i = tbl_inward_item_details.getChildCount(); i >0;  i--) {
            View Row = tbl_inward_item_details.getChildAt(i-1);
            if (Row instanceof TableRow) {
                ((TableRow) Row).removeAllViews();
            }
            ViewGroup container = ((ViewGroup) Row.getParent());
            container.removeView(Row);
            container.invalidate();
            count--;
        }*/
        purchaseOrderAdapter = null;
        if(dataList!=null)
            dataList.clear();
        lv_inward_item_details.setAdapter(purchaseOrderAdapter);
        count=0;
        autocompletetv_purchase_order.setText("");
        set_list_spnr();

        et_inward_additionalchargename.setText("");
        et_inward_additionalchargeamount.setText("");
        chk_inward_additional_charge.setChecked(false);
        et_inward_grand_total.setText("");
        et_inward_sub_total.setText("");

        AlertDialog.Builder msg = new AlertDialog.Builder(myContext);
        msg.setMessage(" Do you want to clear supplier details also")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                reset_inward(1);
                            }

                        }
                )
                .show();
    }

    void loadAutoCompleteDate()
    {
        try
        {
            labelsSupplierName = dbPurchaseOrder.getAllSupplierName();
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    labelsSupplierName);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            autocompletetv_suppliername.setAdapter(dataAdapter);

            ArrayList<String> labelsItemName = new ArrayList<String>();
            //labelsItemName.add(" ");
            labelsItemName.add("Select/Add Supplier");
            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    labelsItemName);
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);
            autocompletetv_itemlist.setAdapter(dataAdapter1);


            ArrayList<String> labelsPurchaseOrder = new ArrayList<String>();
            //labelsItemName.add(" ");
            labelsPurchaseOrder.add("Select/Add Supplier");
            ArrayAdapter<String> dataAdapter11 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    labelsPurchaseOrder);
            dataAdapter11.setDropDownViewResource(android.R.layout.simple_list_item_1);
            autocompletetv_purchase_order.setAdapter(dataAdapter11);


           /* ArrayList<String> labelsInvoiceNo = new ArrayList<String>();
            labelsInvoiceNo.add("Select Supplier");
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    labelsInvoiceNo);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_list_item_1);
            autocompletetv_invoiceno.setAdapter(dataAdapter2);*/
        }
        catch(Exception e )
        {
            MsgBox.setMessage(e.getMessage())
                    .setNeutralButton("OK", null)
                    .show();
        }

    }

    void reset_inward(int type)
    {
        et_supplier_code.setText("0");
        et_supplier_GSTIN.setText("");
        autocompletetv_suppliername.setText("");
        et_supplier_address.setText("");
        et_supplier_phone.setText("");
        chk_interState.setChecked(false);
        spnrSupplierStateCode.setSelection(0);
        spnrSupplierStateCode.setEnabled(false);

        et_inward_sub_total.setText("");
        autocompletetv_invoiceno.setText("");
        tx_inward_supply_invoice_number.setText("");
        autocompletetv_purchase_order.setText("");
        tx_inward_invoice_date.setText("");
        et_inward_item_quantity.setEnabled(false);

        btnEditPO.setEnabled(false);
        btnGeneratePO.setEnabled(true);
        btnAddSupplier.setEnabled(true);

        //btnimage_new_item.setEnabled(true);



        if (type ==0)
        {
            ClearTable();
            set_list_spnr();

            et_inward_additionalchargename.setText("");
            et_inward_additionalchargeamount.setText("");
            chk_inward_additional_charge.setChecked(false);
            et_inward_grand_total.setText("");
            et_inward_sub_total.setText("");
        }
        loadAutoCompleteDate();
    }

    public  void ClearTable()
    {
//        for (int i = tbl_inward_item_details.getChildCount(); i >0;  i--) {
//            View Row = tbl_inward_item_details.getChildAt(i-1);
//            if (Row instanceof TableRow) {
//                ((TableRow) Row).removeAllViews();
//            }
//            ViewGroup container = ((ViewGroup) Row.getParent());
//            container.removeView(Row);
//            container.invalidate();
//            count--;
//        }
        if(dataList!=null)
            dataList.clear();
        dataList= new ArrayList<>();
        purchaseOrderAdapter= null;
        lv_inward_item_details.setAdapter(purchaseOrderAdapter);

        count=0;
    }
    public void Close_inward(View v) {

        dbPurchaseOrder.CloseDatabase();
        this.finish();
    }


    public void AddSupplier(View v)
    {
        //String suppliercode = et_supplier_code.getText().toString();
        String suppliername = autocompletetv_suppliername.getText().toString().toUpperCase();
        String supplierphone = et_supplier_phone.getText().toString();
        String supplieradress = et_supplier_address.getText().toString();

        if (suppliername.equals("") || supplierphone.equals("") || supplieradress.equals(""))
        {
            MsgBox.Show("Insufficient Information", " Please fill Supplier Details");
        }else {
            try {

                Cursor cursor = dbPurchaseOrder.getAllSupplierName_nonGST();
                //labelsSupplierName = dbPurchaseOrder.getAllSupplierName_nonGST();
                labelsSupplierName = new ArrayList<String>();
                ArrayList<String>labelsSupplierGSTIN = new ArrayList<String>();
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        labelsSupplierName.add(cursor.getString(cursor.getColumnIndex("SupplierName")));// adding
                        String gstin =(cursor.getString(cursor.getColumnIndex("GSTIN")));
                        if(gstin!=null && !gstin.equals(""))
                            labelsSupplierGSTIN.add(gstin);
                    } while (cursor.moveToNext());
                }

                for (String supplier : labelsSupplierName) {
                    if (suppliername.equalsIgnoreCase(supplier)) {
                        MsgBox.setTitle("Warning")
                                .setMessage("Supplier already present in list")
                                .setPositiveButton("OK", null)
                                .show();
                        return;
                    }
                }
                String gstin = et_supplier_GSTIN.getText().toString();
                if(gstin!=null && !gstin.equals("") && labelsSupplierGSTIN.contains(gstin))
                {
                    MsgBox.setTitle("Warning")
                            .setMessage("Supplier already present in list")
                            .setPositiveButton("OK", null)
                            .show();
                    return;
                }

                long l =0;
                if(gstin!=null && !gstin.equals(""))
                    l = dbPurchaseOrder.saveSupplierDetails("Registered", gstin, suppliername, supplierphone, supplieradress);
                else
                    l = dbPurchaseOrder.saveSupplierDetails("UnRegistered", "", suppliername, supplierphone, supplieradress);

                if (l > 0) {
                    Log.d("Inward_Item_Entry", " Supplier details saved at " + l);
                    Toast.makeText(myContext, "Supplier details saved at " + l, Toast.LENGTH_SHORT).show();
                }

                cursor = dbPurchaseOrder.getAllSupplierName_nonGST();
                labelsSupplierName = new ArrayList<String>();
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        labelsSupplierName.add(cursor.getString(cursor.getColumnIndex("SupplierName")));// adding
                    } while (cursor.moveToNext());
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                        labelsSupplierName);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                autocompletetv_suppliername.setAdapter(dataAdapter);

                et_supplier_code.setText(String.valueOf(l));
                loadAutoCompleteData_purchaseOrder((int)(l));
                loadAutoCompleteData_item((int)l);
                autocompletetv_purchase_order.setText("");
                autocompletetv_itemlist.setText("");

            } catch (Exception e) {
                MsgBox.setMessage(e.getMessage())
                        .setNeutralButton("Ok", null)
                        .show();
            }
        }
    }
    private void InsertItem(int suppliercode, String suppliername, String itemName, String strbarCode, float ratef,
                            float quantity, String mou, String ImageUri, float SalesTax, float ServiceTax,String supplytype) {


        Item objItem = new Item();
        objItem.setsuppliercode(suppliercode);
        objItem.setsupplierName(suppliername);
        objItem.setItemname(itemName);
        objItem.setItemBarcode(strbarCode);
        objItem.setRate(ratef);
        objItem.setQuantity(0);
        objItem.setMOU(mou);
        objItem.setImageId(ImageUri);
        objItem.setSalesTaxPercent(SalesTax);
        objItem.setServiceTaxPercent(ServiceTax);
        objItem.setSupplyType(supplytype);



        lRowId = dbPurchaseOrder.addItem_Inw_nonGST(objItem);

        Log.d("Item", "Row Id:" + String.valueOf(lRowId));
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
                            dbPurchaseOrder.CloseDatabase();
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














