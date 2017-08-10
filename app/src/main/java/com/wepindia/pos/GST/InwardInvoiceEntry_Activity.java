package com.wepindia.pos.GST;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wep.common.app.Database.BillItem;
import com.wep.common.app.Database.DatabaseHandler;
import com.wepindia.pos.GenericClasses.DateTime;
import com.wepindia.pos.R;
import com.wepindia.pos.utils.ActionBarUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* Created by welcome on 08-11-2016.
*/
public class InwardInvoiceEntry_Activity extends Activity  {

    Context myContext;
    // DatabaseHandler_gst object
    DatabaseHandler dbInwardInvoiceEntry;
    //Message dialog object
    public AlertDialog.Builder MsgBox;
    Spinner spnr_inwalrd_item_list, spnr_supplier_type;
     EditText et_supplier_name , et_inward_item_quantity,et_inward_pos, et_inward_grand_total, et_inward_additionalchargename,
             et_inward_additionalchargeamount,et_supplier_address,et_supplier_phone,et_inward_supplier_gstin;
     TextView tx_inward_pos, tx_inward_invoice_date, tx_inward_supply_invoice_number;

     ImageButton btnimage_new_item ;
    AutoCompleteTextView autocompletetv_suppliername;
     TableLayout tbl_inward_item_details;
     CheckBox chk_pos,chk_inward_additional_charge;
    Button btnEditItem,btnLoadItem,btnSaveItem;
    float subtotal;

    ArrayList<String> labelsSupplierName;
    int count =0;
     String Item_name , Item_HSn  , Item_value, Item_quantity , Item_taxation , Item_gst ;
     String GSTEnable = "", GSTINEnable="", POSEnable ="", HSNEnable ="";
     String strDate= "";
    String reversecharge="no";
        String strUserName="", strUserId ="";
    Date d;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_inward_invoice_entry);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.application_title_bar);

        TextView tvTitleText = (TextView) findViewById(R.id.tvTitleBarCaption);
        TextView tvTitleUserName = (TextView) findViewById(R.id.tvTitleBarUserName);
        TextView tvTitleDate = (TextView) findViewById(R.id.tvTitleBarDate);
        ActionBarUtils.goBack(this, findViewById(R.id.imgTitleBackIcon));
        ActionBarUtils.goHome(this, findViewById(R.id.imgTitleHomeIcon));
        ActionBarUtils.takeScreenshot(this, findViewById(R.id.imgTitleScreenshotIcon), findViewById(R.id.lnrPaymentReceipt));
        tvTitleText.setText("Inward Supply Module");

        //setContentView(R.layout.activity_inward_invoice_entry);

        dbInwardInvoiceEntry = new DatabaseHandler(InwardInvoiceEntry_Activity.this);
        myContext = this;

        try {
            strUserName = getIntent().getStringExtra("USER_NAME");
            strUserId = getIntent().getStringExtra("USER_ID");


            tvTitleUserName.setText(strUserName.toUpperCase());
            d = new Date();
            CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
            tvTitleDate.setText("Date : " + s);



            /*AlertDialog.Builder mg = new AlertDialog.Builder(myContext);
            mg.setMessage("For Exempted, NilRated, NonGst Supplies, choose supplier type as \"Others\"")
                .setPositiveButton("OK",null)
                    .show();*/

            InitializeViews();
            dbInwardInvoiceEntry.CreateDatabase();
            dbInwardInvoiceEntry.OpenDatabase();
            reset_inward(0);
            Cursor crsrSettings = dbInwardInvoiceEntry.getBillSetting();
            if ( (crsrSettings !=null) && crsrSettings.moveToFirst()) {
                GSTEnable = crsrSettings.getString(crsrSettings.getColumnIndex("GSTEnable"));
                HSNEnable =  crsrSettings.getString(crsrSettings.getColumnIndex("HSNCode"));
                if (HSNEnable == null)
                {
                    HSNEnable= "0";
                }
                POSEnable =  crsrSettings.getString(crsrSettings.getColumnIndex("POS"));
                if (POSEnable == null)
                {
                    POSEnable= "0";
                }
                GSTINEnable =  crsrSettings.getString(crsrSettings.getColumnIndex("GSTIN"));
                if (GSTINEnable == null)
                {
                    GSTINEnable= "0";
                }


            }

            if(GSTEnable!= null && GSTEnable.equals("1") )
            {
                if (POSEnable.equals("1"))
                {
                    chk_pos.setEnabled(true);
                    et_inward_pos.setEnabled(true);
                    chk_pos.setTextColor(Color.BLACK);
                    tx_inward_pos.setTextColor(Color.BLACK);
                }
                else
                {
                    chk_pos.setEnabled(false);
                    chk_pos.setTextColor(getResources().getColor(R.color.grey));
                    tx_inward_pos.setTextColor(getResources().getColor(R.color.grey));
                    et_inward_pos.setEnabled(false);
                    et_inward_pos.setBackgroundResource(R.color.grey);
                }
                if (GSTINEnable.equals("1"))
                {
                    et_inward_supplier_gstin.setEnabled(true);
                    et_inward_supplier_gstin.setBackgroundResource(R.color.white);
                }
                else
                {
                    et_inward_supplier_gstin.setEnabled(false);
                    et_inward_supplier_gstin.setBackgroundResource(R.color.grey);
                }

            }

            autocompletetv_suppliername.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String suppliername_str = autocompletetv_suppliername.getText().toString().toUpperCase();
                    Cursor supplierdetail_cursor = dbInwardInvoiceEntry.getSupplierDetails(suppliername_str);
                    if (supplierdetail_cursor!=null && supplierdetail_cursor.moveToFirst())
                    {
                        et_inward_supplier_gstin.setText(supplierdetail_cursor.getString(supplierdetail_cursor.getColumnIndex("GSTIN")));
                        et_supplier_phone.setText(supplierdetail_cursor.getString(supplierdetail_cursor.getColumnIndex("SupplierPhone")));
                        et_supplier_address.setText(supplierdetail_cursor.getString(supplierdetail_cursor.getColumnIndex("SupplierAddress")));
                        String suppliertype = supplierdetail_cursor.getString(supplierdetail_cursor.getColumnIndex("SupplierType"));
                        if (suppliertype.equalsIgnoreCase("registered"))
                            spnr_supplier_type.setSelection(1);
                        if (suppliertype.equalsIgnoreCase("compounding"))
                            spnr_supplier_type.setSelection(2);
                        if (suppliertype.equalsIgnoreCase("unregistered"))
                            spnr_supplier_type.setSelection(3);
                        loadAutoCompleteData_item();

                    }
                }
            });
            spnr_inwalrd_item_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    btnLoadItem.setEnabled(false);
                    String pos_str = et_inward_pos.getText().toString();
                    if ( POSEnable.equals("1") && chk_pos.isChecked() && pos_str.equals("") )
                    {
                        AlertDialog.Builder MsgBox = new AlertDialog.Builder(myContext);
                        MsgBox. setMessage("Please Enter Supplier POS first")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                        set_list_spnr();
                    }
                    else {
                        String item_selected = parent.getItemAtPosition(position).toString();
                        if (item_selected.equalsIgnoreCase("Not in list")) {
                            et_inward_item_quantity.setBackgroundColor(getResources().getColor(R.color.grey));
                            et_inward_item_quantity.setEnabled(false);
                            input_window();

                        }
                        else {
                            et_inward_item_quantity.setEnabled(true);
                            btnimage_new_item.setEnabled(true);

                        }
                    }

                }

                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            btnimage_new_item.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MsgBox = new AlertDialog.Builder(myContext);
                    String item = spnr_inwalrd_item_list.getSelectedItem().toString();
                    if (et_inward_item_quantity.getText().toString().equals(""))
                    {
                        MsgBox.setTitle("Error")
                                .setMessage(" Please Enter the Quantity ")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                    else if (spnr_inwalrd_item_list.getSelectedItem().toString().equals(" "))
                    {
                        MsgBox.setTitle("Error")
                                .setMessage(" Please Select the Item from list  ")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                    else {
                        populate(2);
                    }
                }
            });

            //dbGSTR1.OpenDatabase();//
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            AlertDialog.Builder msg = new AlertDialog.Builder(myContext);
            msg.setMessage(""+e.getMessage())
                    .setPositiveButton("OK", null)
                    .show();
        }
    }
     void input_window()
     {
         AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);

         LayoutInflater UserAuthorization = (LayoutInflater) myContext
                 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

         View vwAuthorization = UserAuthorization.inflate(R.layout.inward_entry_list, null);


         //final TextView tv_attracts_reverse_charge = (TextView) findViewById(R.id.tv_attracts_reverse_charge);
         //final Spinner spnr_attracts_reverse_charge = (Spinner) findViewById(R.id.spnr_attracts_reverse_charge);

         final EditText et_new_item_name = (EditText) vwAuthorization.findViewById(R.id.et_new_item_name);
         final EditText et_new_item_hsn = (EditText) vwAuthorization.findViewById(R.id.et_new_item_hsn);
         final EditText et_new_item_value = (EditText) vwAuthorization.findViewById(R.id.et_new_item_value);
         final EditText et_new_item_quantity = (EditText) vwAuthorization.findViewById(R.id.et_new_item_quantity);

         final EditText et_new_item_gst = (EditText) vwAuthorization.findViewById(R.id.et_new_item_gst);
         final Spinner spnr_taxationtype = (Spinner) vwAuthorization.findViewById(R.id.spnr_taxationtype);



         if (GSTEnable.equals("0") || HSNEnable.equals("0"))
         {
             et_new_item_hsn.setEnabled(false);
             et_new_item_hsn.setBackgroundResource(R.color.grey);
         }
         else
         {
             et_new_item_hsn.setEnabled(true);
             et_new_item_hsn.setBackgroundResource(R.color.white);
         }

         AuthorizationDialog
                 .setTitle("New Item")
                 .setView(vwAuthorization)
                 .setNegativeButton("Cancel", null)
                 .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                     public void onClick(DialogInterface dialog, int which) {
                         // TODO Auto-generated method stub
                          Item_name = et_new_item_name.getText().toString();
                          Item_HSn  = et_new_item_hsn.getText().toString();
                          Item_value = et_new_item_value.getText().toString();
                          Item_quantity = et_new_item_quantity.getText().toString();
                          Item_taxation = spnr_taxationtype.getSelectedItem().toString();
                          Item_gst = et_new_item_gst.getText().toString();
                         if (Item_name.equals("") ||  Item_quantity.equals("") || Item_value.equals(""))
                         {
                             AlertDialog.Builder MsgBox = new AlertDialog.Builder(myContext);
                             MsgBox. setMessage("Item Name , value and Quantity cannot be  blank. Please all details")
                                     .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                         public void onClick(DialogInterface dialog, int which) {

                                         }
                                     })
                                     .show();
                             set_list_spnr();
                         }
                         else if(Item_taxation.equalsIgnoreCase("GST") && Item_gst.equals(""))
                         {
                             AlertDialog.Builder MsgBox = new AlertDialog.Builder(myContext);
                             MsgBox. setMessage("Please mention GST Rate")
                                     .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                         public void onClick(DialogInterface dialog, int which) {

                                         }
                                     })
                                     .show();
                         }
                         else
                         {
                             populate(1); // new entry
                         }


                     }
                 })
                 .show();
     }
     public  void add_inward(View v)
     {

     }

    public  void Load_inward(View v)
    {
        btnEditItem.setEnabled(true);
        btnSaveItem.setEnabled(false);

    }

    public  void Edit_inward(View v)
    {
        String suppliertype_str = spnr_supplier_type.getSelectedItem().toString();
        String supplier_gstin = et_inward_supplier_gstin.getText().toString();
        String suppliername_str = et_supplier_name.getText().toString(),
                supplierphn = et_supplier_phone.getText().toString();
        String invno = tx_inward_supply_invoice_number.getText().toString(),
                invodate = tx_inward_invoice_date.getText().toString();
        int result = dbInwardInvoiceEntry.deleteItem_inward(suppliertype_str, supplier_gstin ,suppliername_str, invno, invodate);
        Log.d("deleteItem_inward :", "No of rows deleted = "+result);
        result = dbInwardInvoiceEntry.deleteItemDetail_inward(suppliertype_str, supplier_gstin ,suppliername_str, invno, invodate);
        Log.d("deleteItemDetail_inward", ": rows deleted: "+result);
        Save_inward(v);
        //reset_inward(0);

    }
    public  void Save_inward(View v)
    {
        AlertDialog.Builder msg = new AlertDialog.Builder(myContext);
        msg.setPositiveButton("OK", null);
        int i = tbl_inward_item_details.getChildCount();
        String suppliertype_str = spnr_supplier_type.getSelectedItem().toString();
        String supplier_gstin = et_inward_supplier_gstin.getText().toString();
        String suppliername_str = autocompletetv_suppliername.getText().toString(),
                supplieraddress_str= et_supplier_address.getText().toString(),
                supplierphn = et_supplier_phone.getText().toString();
        String invno = tx_inward_supply_invoice_number.getText().toString(),
                invodate = tx_inward_invoice_date.getText().toString();
        String taxationtype = Item_taxation;

        String pos_str = et_inward_pos.getText().toString();

        if(suppliertype_str.equalsIgnoreCase("Supplier Type"))
        {
            msg.setMessage("Please Select Supplier Type")
                    .show();
        }
        else if (suppliertype_str.equalsIgnoreCase("Compounding") && chk_pos.isChecked())
        {
            msg.setMessage("Compounding Supplier cannot do interstate supply")
                    .show();
        }else if (suppliertype_str.equalsIgnoreCase("others") &&
                    (!Item_taxation.equalsIgnoreCase("NonGST") && Item_taxation.equalsIgnoreCase("exempt") ==false &&
                            Item_taxation.equalsIgnoreCase("NilRate")== false))
        {
            msg.setMessage("For NilRated, Exempt  or NonGST supplies, choose Other Supplier type")
                    .show();
        }
        else if (suppliertype_str.equalsIgnoreCase("Registered") && supplier_gstin != null &&
                (supplier_gstin.equals("") || supplier_gstin.equals("0")))
        {
            msg.setMessage("Please Enter Valid GSTIN for Registered Supplier")
                    .show();
        }
        else if (suppliertype_str.equalsIgnoreCase("unregistered") && supplier_gstin != null &&
                (supplier_gstin.equals("") == false))
        {
            msg.setMessage("Unregistered Person cannot have gstin")
                    .show();
        }
        else if(suppliername_str.equals("") || supplieraddress_str.equals("") || supplierphn.equals(""))
        {
            msg.setMessage("Please fill Supplier Details")
                    .show();
        }
        else if (invno.equals("") || invodate.equals(""))
        {
            msg.setMessage("Please fill Invoice Details ")
                    .show();
        }
        else if (chk_pos.isChecked() && et_inward_pos.getText().toString().equals(""))
        {
            msg.setMessage("Please Enter POS for interstate Supplier")
                    .show();
        }
        else if ((chk_pos.isChecked()==false) && (false == et_inward_pos.getText().toString().equals("")))
        {
            msg.setMessage("You have entered Supplier POS but not checked Interstate option. Please Check ! ")
                    .show();
        }
        else if (tbl_inward_item_details.getChildCount() < 1)
        {
            msg.setMessage("Please add item")
                    .show();
        }
        else {

            int saved = save_billitem();
            if (saved ==1)// successfully added bill items
            {
                save_billdetail();
                reset_inward(0);
            }

        }
    }

    int save_billitem()
    {
        // Inserted Row Id in database table
        long lResult = 0;


        // Bill item object
        BillItem objBillItem;

        // Reset TotalItems count
        int iTotalItems = 0;
        boolean insert = true;

        String invoiceno= tx_inward_supply_invoice_number.getText().toString();
        //String invodate = tx_inward_invoice_date.getText().toString();
        String invodate = String.valueOf(d.getTime());
        String supp_name = autocompletetv_suppliername.getText().toString();
        String supp_gstin = et_inward_supplier_gstin.getText().toString();
        String sup_type = spnr_supplier_type.getSelectedItem().toString();
        String sup_phone = et_supplier_phone.getText().toString();


        Cursor duplicatebill_cursor = dbInwardInvoiceEntry.getbill_inward(invoiceno,  invodate);
        if ( duplicatebill_cursor!=null && duplicatebill_cursor.moveToFirst())
        {
            do {
                String get_gstin= duplicatebill_cursor.getString(duplicatebill_cursor.getColumnIndex("GSTIN"));
                String get_name = duplicatebill_cursor.getString(duplicatebill_cursor.getColumnIndex("SupplierName"));
                String get_sup_type = duplicatebill_cursor.getString(duplicatebill_cursor.getColumnIndex("SupplierType"));
                String get_sup_phn = duplicatebill_cursor.getString(duplicatebill_cursor.getColumnIndex("SupplierPhone"));
                if (get_gstin!= null && get_gstin.equalsIgnoreCase(supp_gstin))
                {
                    insert = false;
                }
                else if (get_name!= null && get_name.equalsIgnoreCase(supp_name) &&
                            get_sup_type !=null && get_sup_type.equalsIgnoreCase(sup_type) &&
                            get_sup_phn!=null && get_sup_phn.equalsIgnoreCase(sup_phone))
                {
                    insert = false;
                }
                else
                {
                    insert = true;
                }

            }while(duplicatebill_cursor.moveToNext());

        }
        if (insert== false)
        {
            btnEditItem.setEnabled(true);
            AlertDialog.Builder msg = new AlertDialog.Builder(myContext);
            msg.setMessage(" This billno from same supplier on same date already present. Use Edit button to overwrite existing data ")
                    .setPositiveButton("Ok", null)
                    .show();
            Log.d("InsertBillItem", "This bill no and bill date from supplier is already present");
            return 0;
        }
        else {

            for (int iRow = 0; iRow < tbl_inward_item_details.getChildCount(); iRow++) {
                objBillItem = new BillItem();

                TableRow RowBillItem = (TableRow) tbl_inward_item_details.getChildAt(iRow);

                // Increment Total item count if row is not empty
                if (RowBillItem.getChildCount() > 0) {
                    iTotalItems++;
                }

                // Bill Number

                objBillItem.setBillNumber(invoiceno);
                Log.d("InsertBillItems", "Bill Number:" + invoiceno);

                objBillItem.setInvoiceDate(invodate);
                Log.d("InsertBillItems", "Bill Date:" + tx_inward_invoice_date.getText().toString());


                /*objBillItem.setSupplierPhone(et_supplier_phone.getText().toString());
                Log.d("InsertBillItems", "Supplier phone : " + et_supplier_phone.getText().toString());
*/
                objBillItem.setSupplierType(sup_type);
                Log.d("InsertBillItems", "Suppliertype : " + sup_type);
                objBillItem.setSupplierName(supp_name);
                Log.d("InsertBillItems", "SupplierName : " + supp_name);
                objBillItem.setGSTIN(supp_gstin);
                Log.d("InsertBillItems", "SupplierGSTIN : " + supp_gstin);

                objBillItem.setSupplierPhone(sup_phone);
                // Item Number
                if (RowBillItem.getChildAt(1) != null) {
                    Spinner Supplytype_spnr = (Spinner) RowBillItem.getChildAt(1);
                    objBillItem.setSupplyType(Supplytype_spnr.getSelectedItem().toString());
                    Log.d("InsertBillItems", "Supply Type:" + Supplytype_spnr.getSelectedItem().toString());

                    //crsrUpdateItemStock = dbBillScreen.getItem(Integer.parseInt(ItemNumber.getText().toString()));
                }

                // Item Name

                if (RowBillItem.getChildAt(2) != null) {
                    TextView ItemName = (TextView) RowBillItem.getChildAt(2);
                    objBillItem.setItemName(ItemName.getText().toString());
                    Log.d("InsertBillItems", "Item Name:" + ItemName.getText().toString());
                }

                if (RowBillItem.getChildAt(3) != null) {
                    TextView HSN = (TextView) RowBillItem.getChildAt(3);
                    objBillItem.setHSNCode(HSN.getText().toString());
                    Log.d("InsertBillItems", "Item HSN:" + HSN.getText().toString());
                }

                // Rate
                if (RowBillItem.getChildAt(4) != null) {
                    TextView Value = (TextView) RowBillItem.getChildAt(4);
                    objBillItem.setValue(Float.parseFloat(Value.getText().toString()));
                    Log.d("InsertBillItems", "Value :" + Value.getText().toString());
                }

                // Quantity
                if (RowBillItem.getChildAt(5) != null) {
                    TextView Quantity = (TextView) RowBillItem.getChildAt(5);
                    objBillItem.setQuantity(Float.parseFloat(Quantity.getText().toString()));
                    Log.d("InsertBillItems", "Quantity:" + Quantity.getText().toString());

                    /*if (crsrUpdateItemStock.moveToFirst()) {
                        // Check if item's bill with stock enabled update the stock
                        // quantity
                        if (crsrUpdateItemStock.getInt(crsrUpdateItemStock.getColumnIndex("BillWithStock")) == 1) {
                            UpdateItemStock(crsrUpdateItemStock, Float.parseFloat(Quantity.getText().toString()));
                        }
                    }*/
                }


                // UoM
                if (RowBillItem.getChildAt(6) != null) {
                    Spinner uom_spn = (Spinner) RowBillItem.getChildAt(6);
                    objBillItem.setUom(uom_spn.getSelectedItem().toString());
                    Log.d("InsertBillItems", "UoM:" + uom_spn.getSelectedItem().toString());
                }


                // taxable value
                if (RowBillItem.getChildAt(7) != null) {
                    TextView TaxValue = (TextView) RowBillItem.getChildAt(7);
                    objBillItem.setTaxableValue(Float.parseFloat(TaxValue.getText().toString()));
                    Log.d("InsertBillItems", "taxable Value:" + TaxValue.getText().toString());
                }

                // IGST RATE
                if (RowBillItem.getChildAt(15) != null) {
                    TextView IGSTRATE = (TextView) RowBillItem.getChildAt(15);
                    objBillItem.setIGSTRate(Float.parseFloat(IGSTRATE.getText().toString()));
                    Log.d("InsertBillItems", "IGST Rate:" + IGSTRATE.getText().toString());
                }
                // IGST Amt
                if (RowBillItem.getChildAt(9) != null) {
                    TextView IGSTAmt = (TextView) RowBillItem.getChildAt(9);
                    objBillItem.setIGSTAmount(Float.parseFloat(IGSTAmt.getText().toString()));
                    Log.d("InsertBillItems", "IGST Amt:" + IGSTAmt.getText().toString());
                }
                // CGST RATE
                if (RowBillItem.getChildAt(16) != null) {
                    TextView CGSTRate = (TextView) RowBillItem.getChildAt(16);
                    objBillItem.setCGSTRate(Float.parseFloat(CGSTRate.getText().toString()));
                    Log.d("InsertBillItems", "CGST RAte:" + CGSTRate.getText().toString());
                }
                // CGST AMT
                if (RowBillItem.getChildAt(10) != null) {
                    TextView CGSTAmt = (TextView) RowBillItem.getChildAt(10);
                    objBillItem.setCGSTAmount(Float.parseFloat(CGSTAmt.getText().toString()));
                    Log.d("InsertBillItems", "CGST Amt:" + CGSTAmt.getText().toString());
                }

                // SGST RATE
                if (RowBillItem.getChildAt(17) != null) {
                    TextView SGSTRate = (TextView) RowBillItem.getChildAt(17);
                    objBillItem.setSGSTRate(Float.parseFloat(SGSTRate.getText().toString()));
                    Log.d("InsertBillItems", "SGST RAte:" + SGSTRate.getText().toString());
                }
                // SGST AMT
                if (RowBillItem.getChildAt(11) != null) {
                    TextView SGSTAmt = (TextView) RowBillItem.getChildAt(11);
                    objBillItem.setSGSTAmount(Float.parseFloat(SGSTAmt.getText().toString()));
                    Log.d("InsertBillItems", "SGST Amt:" + SGSTAmt.getText().toString());
                }

                // Sub Total
                if (RowBillItem.getChildAt(12) != null) {
                    TextView SubTot = (TextView) RowBillItem.getChildAt(12);
                    objBillItem.setSubTotal(Float.parseFloat(SubTot.getText().toString()));
                    Log.d("InsertBillItems", "taxable Value:" + SubTot.getText().toString());

                }

                // Sub Total
                if (RowBillItem.getChildAt(14) != null) {
                    TextView TaxationType = (TextView) RowBillItem.getChildAt(14);
                    objBillItem.setTaxationType(TaxationType.getText().toString());
                    Log.d("InsertBillItems", "TaxationType:" + TaxationType.getText().toString());

                }

                lResult = dbInwardInvoiceEntry.addBillItem_inward(objBillItem);
                Log.d("InsertBillItem", "Bill item inserted at position:" + lResult);
            }

        }
        return 1;
    }

    void save_billdetail()
    {
        float subtotal =0f;
        String suppliertype_str = spnr_supplier_type.getSelectedItem().toString();
        String supplier_gstin = et_inward_supplier_gstin.getText().toString();
        String suppliername_str = autocompletetv_suppliername.getText().toString(),
                supplieraddress_str= et_supplier_address.getText().toString(),
                supplierphn = et_supplier_phone.getText().toString();
        String invno = tx_inward_supply_invoice_number.getText().toString(),
                invodate = tx_inward_invoice_date.getText().toString();


        String pos_str1 = et_inward_pos.getText().toString();

            String additionalCharge = et_inward_additionalchargeamount.getText().toString();
            String grantotal = et_inward_grand_total.getText().toString();
            String additionalchargename = et_inward_additionalchargename.getText().toString();
            int iTotalItems=0;
            float igstamt =0, cgstamt =0, sgstamt =0;
            float mona =0, taxvalue =0;

        for (int iRow = 0; iRow < tbl_inward_item_details.getChildCount(); iRow++) {

            TableRow RowBillItem = (TableRow) tbl_inward_item_details.getChildAt(iRow);

            // Increment Total item count if row is not empty
            if (RowBillItem.getChildCount() > 0) {
                iTotalItems++;
            }



            // Taxable Value
            if (RowBillItem.getChildAt(7) != null) {
                TextView TaxVal = (TextView) RowBillItem.getChildAt(7);
                taxvalue += (Float.parseFloat(TaxVal.getText().toString()));
            }

            // IGST Amt
            if (RowBillItem.getChildAt(9) != null) {
                TextView IGSTAmt = (TextView) RowBillItem.getChildAt(9);
                igstamt += (Float.parseFloat(IGSTAmt.getText().toString()));
            }

            // CGST AMT
            if (RowBillItem.getChildAt(10) != null) {
                TextView CGSTAmt = (TextView) RowBillItem.getChildAt(10);
                cgstamt += (Float.parseFloat(CGSTAmt.getText().toString()));
            }


            // SGST AMT
            if (RowBillItem.getChildAt(11) != null) {
                TextView SGSTAmt = (TextView) RowBillItem.getChildAt(11);
                sgstamt += (Float.parseFloat(SGSTAmt.getText().toString()));
            }
            // Sub Total
            if (RowBillItem.getChildAt(12) != null) {
                TextView SubTot = (TextView) RowBillItem.getChildAt(12);
                subtotal += (Float.parseFloat(SubTot.getText().toString()));

            }

        }

        // richa to do - hardcoded b2b bussinies type
        //objBillItem.setBusinessType("B2B");
        //Log.d("InsertBillItems", "Now value:" + mona);
        Log.d("InsertBillItems", "Now Taxable value:" + taxvalue);
        Log.d("InsertBillItems", "Now subtotal:" + subtotal);
        Log.d("InsertBillItems", "Now igstamt:" + igstamt);
        Log.d("InsertBillItems", "Now cgstamt:" + cgstamt);
        Log.d("InsertBillItems", "Now sgstamt:" + sgstamt);
        Log.d("InsertBillItems", "ReverseCharge:" + reversecharge);

            long result = dbInwardInvoiceEntry.addBill_inward(suppliername_str, supplieraddress_str, supplierphn, suppliertype_str,
                            supplier_gstin,pos_str1,invno,invodate,additionalCharge, additionalchargename ,grantotal,
                            String.valueOf(iTotalItems), String.valueOf(subtotal) , String.valueOf(igstamt) ,String.valueOf(cgstamt) ,
                           String.valueOf(sgstamt) ,String.valueOf(taxvalue),reversecharge);
            Log.d("save_inward "," entry to inward supply ledger :"+result);
        if (result >0)
        {
            Toast.makeText(myContext, "Bill added successfully", Toast.LENGTH_SHORT).show();
        }
    }







     void InitializeViews()
     {
         tbl_inward_item_details = (TableLayout) findViewById(R.id.tbl_inward_item_details);
         autocompletetv_suppliername = (AutoCompleteTextView)findViewById(R.id.autocompletetv_suppliername);
         btnimage_new_item = (ImageButton) findViewById(R.id.btnimage_new_item);
         btnEditItem = (Button) findViewById (R.id.btnEditItem);
         btnLoadItem = (Button) findViewById (R.id.btnLoadItem);
         btnSaveItem = (Button) findViewById (R.id.btnSaveItem);
         tx_inward_pos = (TextView) findViewById(R.id.tx_inward_pos);
         tx_inward_invoice_date = (TextView) findViewById(R.id.tx_inward_invoice_date);
         et_supplier_address = (EditText) findViewById(R.id.et_supplier_address);
         et_supplier_phone = (EditText) findViewById(R.id.et_supplier_phone);
         spnr_supplier_type= (Spinner) findViewById(R.id.spnr_supplier_type);
         spnr_supplier_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 String supplr_type = parent.getSelectedItem().toString();
                 if (supplr_type.equalsIgnoreCase("unregistered"))
                 {
                     AlertDialog.Builder msg1 = new AlertDialog.Builder(myContext);
                     msg1.setMessage(" Attracts Reverse Charge")
                             .setNegativeButton("No", null)
                             .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int which) {
                                     reversecharge="yes";
                                 }
                             })
                             .show();
                 }
                 else
                 {
                     reversecharge="no";
                 }
             }

             public void onNothingSelected(AdapterView<?> parent) {

             }
         });

         et_inward_supplier_gstin = (EditText) findViewById(R.id.et_inward_supplier_gstin);
         tx_inward_supply_invoice_number= (TextView) findViewById(R.id.tx_inward_supply_invoice_number);
         chk_pos = (CheckBox) findViewById(R.id.chk_inward_pos);
         chk_inward_additional_charge = (CheckBox) findViewById(R.id.chk_inward_additional_charge);
         et_inward_additionalchargeamount = (EditText) findViewById(R.id.et_inward_additionalchargeamount);
         et_inward_additionalchargename  = (EditText) findViewById(R.id.et_inward_additionalchargename);
         et_inward_grand_total = (EditText) findViewById(R.id.et_inward_grand_total);
         et_inward_pos = (EditText)findViewById(R.id.et_inward_pos) ;
             et_inward_pos.setEnabled(false);
             et_inward_pos.setBackgroundResource(R.color.grey);
         spnr_inwalrd_item_list = (Spinner) findViewById(R.id.spnr_inwalrd_item_list);
         et_inward_item_quantity = (EditText) findViewById(R.id.et_inward_item_quantity);
         et_supplier_name = (EditText) findViewById(R.id.et_supplier_name);
         et_supplier_name.addTextChangedListener(new TextWatcher() {
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             public void onTextChanged(CharSequence s, int start, int before, int count) {

             }

             public void afterTextChanged(Editable s) {
                 Cursor item_list_cursor = dbInwardInvoiceEntry.getItemListForSupplier(autocompletetv_suppliername.getText().toString());
                 ArrayList<String> labels = new ArrayList<String>();
                 labels.add(" ");
                 if (item_list_cursor ==null)
                 {
                     Toast.makeText(myContext, "No Item found for Supplier : "+et_supplier_name.getText().toString(), Toast.LENGTH_SHORT).show();
                 }
                 else
                 {
                     if (item_list_cursor.moveToFirst()) {
                         do {
                             labels.add(item_list_cursor.getString(1));
                         } while (item_list_cursor.moveToNext());
                         labels.add("Not in list");

                     }
                     else
                     {
                         labels.add("Not in list");
                     }
                     ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(myContext,
                             android.R.layout.simple_spinner_item, labels);

                     // Drop down layout style - list view with radio button
                     dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                     // attaching data adapter to spinner
                     spnr_inwalrd_item_list.setAdapter(dataAdapter);

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
                 calculateTotalAmount();

             }
         });

            chk_pos.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chk_pos.isChecked())
                    {
                        et_inward_pos.setEnabled(true);
                        et_inward_pos.setBackgroundResource(R.color.white);
                    }
                    else
                    {
                        et_inward_pos.setEnabled(false);
                        et_inward_pos.setBackgroundResource(R.color.grey);
                    }

                        recalculatetax();

                }
            });

     }

    private void loadAutoCompleteData_item() {

        String suppliername_str = autocompletetv_suppliername.getText().toString().toUpperCase();
        /*List<String> itemlist = dbInwardInvoiceEntry.getitemlist_inward(suppliername_str);

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,itemlist);

        dataAdapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);

        // attaching data adapter to spinner
        autocomplete_inw_ItemName.setAdapter(dataAdapter1);*/

        Cursor item_list_cursor = dbInwardInvoiceEntry.getItemListForSupplier(suppliername_str);
        ArrayList<String> labels = new ArrayList<String>();
        if (item_list_cursor ==null)
        {
            Toast.makeText(myContext, "No Item found for Supplier : "+et_supplier_name.getText().toString(), Toast.LENGTH_SHORT).show();
        }
        else
        {
            labels.add(" ");
            if (item_list_cursor.moveToFirst()) {
                do {
                    labels.add(item_list_cursor.getString(item_list_cursor.getColumnIndex("ItemName")));
                } while (item_list_cursor.moveToNext());
            }
            labels.add("Not in list");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(myContext,
                    android.R.layout.simple_spinner_item, labels);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnr_inwalrd_item_list.setAdapter(dataAdapter);

        }
    }


     void recalculatetax()
     {
         if(chk_pos.isChecked()== false )
         {
             et_inward_pos.setText("");
             for (int i =0; i<tbl_inward_item_details.getChildCount();i++)
             {
                 TableRow row = (TableRow)tbl_inward_item_details.getChildAt(i);
                 TextView mona= ((TextView)row.getChildAt(6));
                 TextView taxval= ((TextView)row.getChildAt(7));
                 TextView igstrate= ((TextView)row.getChildAt(8));
                 TextView igstamt= ((TextView)row.getChildAt(9));
                 TextView cgstrate= ((TextView)row.getChildAt(10));
                 TextView cgstamt= ((TextView)row.getChildAt(11));
                 TextView sgstrate= ((TextView)row.getChildAt(12));
                 TextView sgstamt= ((TextView)row.getChildAt(13));
                 TextView subtot= ((TextView)row.getChildAt(14));


                 float igstRate_f = Float.parseFloat(igstrate.getText().toString());
                 float cgstrate_f= Float.parseFloat(igstrate.getText().toString()),
                         sgstrate_f =Float.parseFloat(sgstrate.getText().toString()),
                         cgstamt_f =0, sgstamt_f =0, igstamt_f =0;
                 float taxval_f = Float.parseFloat(taxval.getText().toString());
                 if(igstRate_f > 0)
                 {
                     cgstrate_f = igstRate_f/2;
                     sgstrate_f = igstRate_f/2;
                 }
                 cgstamt_f  = taxval_f* (cgstrate_f/100);
                 sgstamt_f  = taxval_f* (sgstrate_f/100);
                 igstRate_f = igstamt_f =0;

                 float subtot_f = taxval_f+igstamt_f+cgstamt_f+sgstamt_f;


                 igstrate.setText(String.valueOf(igstRate_f));
                 igstamt.setText(String.format("%.2f", igstamt_f));
                 cgstrate.setText(String.valueOf(cgstrate_f));
                 cgstamt.setText(String.format("%.2f", cgstamt_f));
                 sgstrate.setText(String.valueOf(sgstrate_f));
                 sgstamt.setText(String.format("%.2f", sgstamt_f));
                 subtot.setText(String.valueOf(subtot_f));

             }
             // though ideaaly there should not be any change in grand total, but still calling this func
             calculateTotalAmount();
         }
         else //if (chk_pos.isChecked()== true )
         {
             for (int i =0; i<tbl_inward_item_details.getChildCount();i++)
             {
                 TableRow row = (TableRow)tbl_inward_item_details.getChildAt(i);
                 /*TextView sn= ((TextView)row.getChildAt(0));
                 TextView name= ((TextView)row.getChildAt(1));
                 TextView hsn= ((TextView)row.getChildAt(2));
                 TextView value= ((TextView)row.getChildAt(3));
                 TextView qty= ((TextView)row.getChildAt(4));
                 Spinner uom= ((Spinner) row.getChildAt(5));*/
                 TextView mona= ((TextView)row.getChildAt(6));
                 TextView taxval= ((TextView)row.getChildAt(7));
                 TextView igstrate= ((TextView)row.getChildAt(8));
                 TextView igstamt= ((TextView)row.getChildAt(9));
                 TextView cgstrate= ((TextView)row.getChildAt(10));
                 TextView cgstamt= ((TextView)row.getChildAt(11));
                 TextView sgstrate= ((TextView)row.getChildAt(12));
                 TextView sgstamt= ((TextView)row.getChildAt(13));
                 TextView subtot= ((TextView)row.getChildAt(14));


                 float igstrate_f = 0;
                 float cgstrate_f=0, sgstrate_f =0, cgstamt_f =0, sgstamt_f =0, igstamt_f =0;
                 float taxval_f = Float.parseFloat(taxval.getText().toString());
                 cgstrate_f = Float.parseFloat(cgstrate.getText().toString());
                 sgstrate_f = Float.parseFloat(sgstrate.getText().toString());

                 igstrate_f = cgstrate_f + sgstrate_f;
                 sgstrate_f  = 0;
                 cgstrate_f  = 0;
                 cgstamt_f = sgstamt_f  = 0;
                 igstamt_f =taxval_f*(igstrate_f/100);

                 float subtot_f = taxval_f+igstamt_f+cgstamt_f+sgstamt_f;


                 igstrate.setText(String.valueOf(igstrate_f));
                 igstamt.setText(String.format("%.2f", igstamt_f));
                 cgstrate.setText(String.valueOf(cgstrate_f));
                 cgstamt.setText(String.format("%.2f", cgstamt_f));
                 sgstrate.setText(String.valueOf(sgstrate_f));
                 sgstamt.setText(String.format("%.2f", sgstamt_f));
                 subtot.setText(String.valueOf(subtot_f));

             }
             // though ideaaly there should not be any change in grand total, but still calling this func
             calculateTotalAmount();
         }
     }
    void populate(int type)
    {
        //1 - new item entry
        //2 - existing item in database
        boolean flag_pos =true;

         TableLayout tbl = (TableLayout) findViewById (R.id.tbl_inward_item_details);
         TableRow tr = new TableRow(this);

        TextView sn = new TextView(myContext);
        sn.setWidth(20);
        sn.setBackgroundResource(R.drawable.border);
        count++;
        sn.setText(String.valueOf(count));


        Spinner spn_supplytype = new Spinner(myContext);
        ArrayAdapter<CharSequence> ad1 = ArrayAdapter.createFromResource(this, R.array.g_s, android.R.layout.simple_spinner_item);
        spn_supplytype.setAdapter(ad1);


        TextView name = new TextView(myContext);
        name.setBackgroundResource(R.drawable.border);
        name.setWidth(130);

        TextView hsn = new TextView(myContext);
        hsn.setWidth(70);
        hsn.setBackgroundResource(R.drawable.border);

        TextView value = new TextView(myContext);
        value.setWidth(95);
        value.setBackgroundResource(R.drawable.border);

        TextView qty = new TextView(myContext);
        qty.setWidth(70);
        qty.setBackgroundResource(R.drawable.border);

        Spinner spn = new Spinner(myContext);
        ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(this, R.array.UOM, android.R.layout.simple_spinner_item);
        spn.setAdapter(ad);


        TextView taxvalue = new TextView(myContext);
        taxvalue.setWidth(150);
        taxvalue.setBackgroundResource(R.drawable.border);

        TextView GSTRate = new TextView(myContext);
        GSTRate.setWidth(75);
        GSTRate.setBackgroundResource(R.drawable.border);

        TextView IGSTAmt = new TextView(myContext);
        IGSTAmt.setWidth(95);
        IGSTAmt.setBackgroundResource(R.drawable.border);

        TextView CGSTAmt = new TextView(myContext);
        CGSTAmt.setWidth(95);
        CGSTAmt.setBackgroundResource(R.drawable.border);

        TextView SGSTAmt = new TextView(myContext);
        SGSTAmt.setWidth(95);
        SGSTAmt.setBackgroundResource(R.drawable.border);

        TextView IGSTRate = new TextView(myContext);
        TextView CGSTRate = new TextView(myContext);
        TextView SGSTRate = new TextView(myContext);


        TextView SubTotal = new TextView(myContext);
        SubTotal.setWidth(130);
        SubTotal.setBackgroundResource(R.drawable.border);


        // Delete
        int res = getResources().getIdentifier("delete", "drawable", this.getPackageName());
        ImageButton ImgDelete = new ImageButton(myContext);
        ImgDelete.setImageResource(res);
        // btnDelete.setText(crsrItem.getString(crsrItem.getColumnIndex("MenuCode")));
        ImgDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View row = (View) v.getParent();
                ViewGroup container = ((ViewGroup) row.getParent());
                container.removeView(row);
                container.invalidate();
                calculateTotalAmount();
            }
        });

        TextView TaxationType = new TextView(myContext);
        TaxationType.setText(Item_taxation);



        try {
            if(type ==1) // new item
            {
                name.setText(Item_name);
                if(GSTEnable.equals("1")&& HSNEnable.equals("1"))
                {
                    hsn.setText(Item_HSn);
                }
                else
                {
                    hsn.setBackgroundResource(R.color.grey);
                }

                value.setText(Item_value);
                qty.setText(Item_quantity);
                float value_f = Float.parseFloat(Item_value);
                float qty_f = Float.parseFloat(Item_quantity);
                float taxval_f = value_f * qty_f;
                taxvalue.setText(String.valueOf(taxval_f));
                if(Item_taxation.equalsIgnoreCase("nonGST") || Item_taxation.equalsIgnoreCase("Exempt") ||
                        Item_taxation.equalsIgnoreCase("NilRate") )
                {
                    // no tax
                    Item_gst = "0";
                }


                float igstrate_f = 0, igstamt_f =0, cgstrate_f = 0, cgstamt_f =0, sgstrate_f = 0, sgstamt_f =0 ,gstrate_f =0;
                if(chk_pos.isChecked() )
                {
                    String pos_str = et_inward_pos.getText().toString();
                    if (pos_str.equals(""))
                    {
                        flag_pos =false;
                        Toast.makeText(myContext, "Please mention Supplier POS first", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        igstrate_f = Float.parseFloat(Item_gst);
                        igstamt_f = igstrate_f*taxval_f/100;
                        gstrate_f = igstrate_f;

                    }
                }
                else
                {
                    gstrate_f = Float.parseFloat(Item_gst);
                    if (gstrate_f >0) {
                        cgstrate_f = gstrate_f / 2;
                        sgstrate_f = gstrate_f / 2;
                    }
                    sgstamt_f = sgstrate_f*taxval_f/100;
                    cgstamt_f = cgstrate_f*taxval_f/100;

                }
                GSTRate.setText(String.valueOf(gstrate_f));
                IGSTAmt.setText(String.format("%.2f", igstamt_f));
                CGSTAmt.setText(String.format("%.2f", cgstamt_f));
                SGSTAmt.setText(String.format("%.2f", sgstamt_f));
                IGSTRate.setText(String.valueOf(igstrate_f));
                CGSTRate.setText(String.valueOf(cgstrate_f));
                SGSTRate.setText(String.valueOf(sgstrate_f));


                float sub_total_f = taxval_f+igstamt_f + cgstamt_f + sgstamt_f;
                SubTotal.setText(String.valueOf(sub_total_f));

            }// end of if(type ==1) // new item
            else if(type ==2) // item exixting in database
            {
                Item_name = spnr_inwalrd_item_list.getSelectedItem().toString();
                Cursor itemdetails = dbInwardInvoiceEntry.getItemsdetail_Inw(Item_name, autocompletetv_suppliername.getText().toString());
                if (itemdetails!=null && itemdetails.moveToFirst()) {
                    name.setText(Item_name);
                    Item_HSn = itemdetails.getString(itemdetails.getColumnIndex("HSNCode"));
                    if (Item_HSn == null)
                        Item_HSn="";
                    Item_value = itemdetails.getString(itemdetails.getColumnIndex("Rate"));
                    Item_quantity = et_inward_item_quantity.getText().toString();
                    Item_taxation = itemdetails.getString(itemdetails.getColumnIndex("TaxationType"));

                    if (GSTEnable.equals("1") && HSNEnable.equals("1")) {
                        hsn.setText(Item_HSn);
                    } else {
                        hsn.setBackgroundResource(R.color.grey);
                    }

                    value.setText(Item_value);
                    qty.setText(Item_quantity);
                    float value_f = Float.parseFloat(Item_value);
                    float qty_f = Float.parseFloat(Item_quantity);
                    float taxval_f = value_f * qty_f;
                    taxvalue.setText(String.valueOf(taxval_f));
                    if (Item_taxation.equalsIgnoreCase("nonGST") || Item_taxation.equalsIgnoreCase("Exempt") ||
                            Item_taxation.equalsIgnoreCase("NilRate")) {
                        // no tax
                        Item_gst = "0";
                    }


                    float igstrate_f = 0, igstamt_f = 0, cgstrate_f = 0, cgstamt_f = 0, sgstrate_f = 0, sgstamt_f = 0, gstrate_f = 0;
                    if (chk_pos.isChecked()) {
                        String pos_str = et_inward_pos.getText().toString();
                        if (pos_str.equals("")) {
                            flag_pos = false;
                            Toast.makeText(myContext, "Please mention Supplier POS first", Toast.LENGTH_SHORT).show();
                        } else {
                            Item_gst = itemdetails.getString(itemdetails.getColumnIndex("IGSTRate"));
                            if (Item_gst == null || Item_gst.equals(""))
                                Item_gst= "0";

                            igstrate_f = Float.parseFloat(Item_gst);
                            igstamt_f = igstrate_f * taxval_f / 100;
                            gstrate_f = igstrate_f;
                            GSTRate.setText(String.valueOf(igstrate_f));

                        }
                    } else {

                        /*gstrate_f = Float.parseFloat(Item_gst);
                        if (gstrate_f > 0) {
                            cgstrate_f = gstrate_f / 2;
                            sgstrate_f = gstrate_f / 2;
                        }*/
                        String crate = itemdetails.getString(itemdetails.getColumnIndex("CGSTRate"));
                        String srate = itemdetails.getString(itemdetails.getColumnIndex("SGSTRate"));
                        if (crate== null || crate.equals(""))
                            cgstrate_f = 0;
                        else
                            cgstrate_f = Float.parseFloat(crate);

                        if (srate== null || srate.equals(""))
                            sgstrate_f = 0;
                        else
                            sgstrate_f = Float.parseFloat(srate);
                        sgstamt_f = sgstrate_f * taxval_f / 100;
                        cgstamt_f = cgstrate_f * taxval_f / 100;
                        GSTRate.setText(String.valueOf(cgstrate_f+sgstrate_f));

                    }
                    //GSTRate.setText(String.valueOf(gstrate_f));
                    IGSTAmt.setText(String.format("%.2f", igstamt_f));
                    CGSTAmt.setText(String.format("%.2f", cgstamt_f));
                    SGSTAmt.setText(String.format("%.2f", sgstamt_f));
                    IGSTRate.setText(String.valueOf(igstrate_f));
                    CGSTRate.setText(String.valueOf(cgstrate_f));
                    SGSTRate.setText(String.valueOf(sgstrate_f));


                    float sub_total_f = taxval_f + igstamt_f + cgstamt_f + sgstamt_f;
                    SubTotal.setText(String.valueOf(sub_total_f));
                }
            }// end of if(type ==1) // new item
            if(flag_pos) { // to ensure that pos is added to remove discrepency between interstate / intrastate tax
                tr.addView(sn);
                tr.addView(spn_supplytype);
                tr.addView(name);
                tr.addView(hsn);
                tr.addView(value);
                tr.addView(qty);
                tr.addView(spn);
                tr.addView(taxvalue);
                tr.addView(GSTRate);
                tr.addView(IGSTAmt);
                tr.addView(CGSTAmt);
                tr.addView(SGSTAmt);
                tr.addView(SubTotal);
                tr.addView(ImgDelete);
                tr.addView(TaxationType);
                tr.addView(IGSTRate);
                tr.addView(CGSTRate);
                tr.addView(SGSTRate);

                tbl_inward_item_details.addView(tr);
                set_list_spnr();
            }

            calculateTotalAmount();
        }
        catch (Exception e)
        {
            Toast.makeText(myContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

     void calculateTotalAmount()
     {
         float grandtotal_f =0;
         float additionalcharge_f = 0;
         TextView subtot;
         String subtotal_str = "0";
         for (int i =0;i<tbl_inward_item_details.getChildCount(); i++)
         {
             TableRow row = (TableRow) tbl_inward_item_details.getChildAt(i);
             subtot = (TextView) row.getChildAt(12);
             if (subtot != null)
             {
                 subtotal_str  = subtot.getText().toString();
             }

             grandtotal_f += Float.parseFloat(subtotal_str);

         }
         // addtional charge
         if( et_inward_additionalchargeamount.getText().toString().equals(""))
         {
             additionalcharge_f =0;
         }
         else
         {
             additionalcharge_f = Float.parseFloat(et_inward_additionalchargeamount.getText().toString());
         }

         if(chk_inward_additional_charge.isChecked() && additionalcharge_f >0) {
             grandtotal_f += additionalcharge_f;
         }
         et_inward_grand_total.setText(String.valueOf(grandtotal_f));

     }

     void set_list_spnr()
     {
         spnr_inwalrd_item_list.setSelection(0);
         et_inward_item_quantity.setBackgroundColor(Color.WHITE);
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
        for (int i = tbl_inward_item_details.getChildCount(); i >0;  i--) {
            View Row = tbl_inward_item_details.getChildAt(i-1);
            if (Row instanceof TableRow) {
                ((TableRow) Row).removeAllViews();
            }
            ViewGroup container = ((ViewGroup) Row.getParent());
            container.removeView(Row);
            container.invalidate();
            count--;
        }
        count=0;
        set_list_spnr();

        et_inward_additionalchargename.setText("");
        et_inward_additionalchargeamount.setText("");
        chk_inward_additional_charge.setChecked(false);
        et_inward_grand_total.setText("");
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
            labelsSupplierName = dbInwardInvoiceEntry.getAllSupplierName();
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    labelsSupplierName);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            autocompletetv_suppliername.setAdapter(dataAdapter);
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
        btnimage_new_item.setEnabled(false);
        et_inward_item_quantity.setEnabled(false);
        autocompletetv_suppliername.setText("");

        et_supplier_name.setText("");
        et_supplier_address.setText("");
        et_supplier_phone.setText("");
        spnr_supplier_type.setSelection(0);
        et_inward_supplier_gstin.setText("");
        chk_pos.setChecked(false);
        et_inward_pos.setText("");
        tx_inward_supply_invoice_number.setText("");
        tx_inward_invoice_date.setText("");
        btnEditItem.setEnabled(false);
        btnLoadItem.setEnabled(true);
        btnSaveItem.setEnabled(true);
        if (type ==0)
        {
            for (int i = tbl_inward_item_details.getChildCount(); i >0;  i--) {
                View Row = tbl_inward_item_details.getChildAt(i-1);
                if (Row instanceof TableRow) {
                    ((TableRow) Row).removeAllViews();
                }
                ViewGroup container = ((ViewGroup) Row.getParent());
                container.removeView(Row);
                container.invalidate();
                count--;
            }
            count=0;
            set_list_spnr();

            et_inward_additionalchargename.setText("");
            et_inward_additionalchargeamount.setText("");
            chk_inward_additional_charge.setChecked(false);
            et_inward_grand_total.setText("");
        }
        loadAutoCompleteDate();
    }

    public void Close_inward(View v) {

        dbInwardInvoiceEntry.CloseDatabase();
        this.finish();
    }
}












