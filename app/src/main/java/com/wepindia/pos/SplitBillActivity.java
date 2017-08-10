/****************************************************************************
 * Project Name		:	VAJRA
 * <p/>
 * File Name		:	SplitBillActivity
 * <p/>
 * Purpose			:	Represents bill splitting activity, takes care of all
 * UI back end operations in this activity, such as event
 * handling data read from or display in views.
 * <p/>
 * DateOfCreation	:	24-December-2012
 * <p/>
 * Author			:	Balasubramanya Bharadwaj B S
 ****************************************************************************/
package com.wepindia.pos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.wep.common.app.Database.BillDetail;
import com.wep.common.app.Database.BillItem;
import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.WepBaseActivity;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.utils.ActionBarUtils;

import java.util.Date;

public class SplitBillActivity extends WepBaseActivity {

    // Context object
    Context myContext;

    // DatabaseHandler object
    DatabaseHandler dbSplitBill = new DatabaseHandler(SplitBillActivity.this);
    // MessagDialog object
    MessageDialog MsgBox;

    // View handlers
    TextView txtSBNumber, txtSB1Number, txtSB2Number;
    TextView OBUserName, OBDate, OBBillNumber, OBSubTotal, OBTax, OBServiceTax, OBAmount,
            SB1SubTotal, SB1Tax, SB1ServiceTax, SB1Amount, SB2SubTotal, SB2Tax, SB2ServiceTax, SB2Amount;
    TableLayout tblOriginalBill, tblSplitBill1, tblSplitBill2;

    // Variables
    String strTableNumber = "", strSubUdfNumber = "", strTableSplitNo = "", strBillNumber = "", strUserId = "", strUserName = "", strDate = "";
    int iServiceTaxType = 0, iTotalItems = 0, iEmployeeId = 0;
    float fTotalDiscount = 0;
    double dServiceTaxPercent = 0;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splitbill);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.application_title_bar);

        TextView tvTitleText = (TextView) findViewById(R.id.tvTitleBarCaption);
        TextView tvTitleUserName = (TextView) findViewById(R.id.tvTitleBarUserName);
        TextView tvTitleDate = (TextView) findViewById(R.id.tvTitleBarDate);
        ActionBarUtils.goBack(this, findViewById(R.id.imgTitleBackIcon));
        ActionBarUtils.goHome(this, findViewById(R.id.imgTitleHomeIcon));
        ActionBarUtils.takeScreenshot(this, findViewById(R.id.imgTitleScreenshotIcon), findViewById(R.id.lnrTableShitMerge));
        tvTitleText.setText("Split Bill Screen");*/

        myContext = this;

        try {

            MsgBox = new MessageDialog(myContext);

            // Get data from intent
            strUserId = getIntent().getStringExtra("USER_ID");
            strUserName = getIntent().getStringExtra("USER_NAME");
            strDate = getIntent().getStringExtra("BILL_DATE");
            strBillNumber = getIntent().getStringExtra("BILL_NUMBER");
            strTableNumber = getIntent().getStringExtra("TABLE_NUMBER");
            strSubUdfNumber = getIntent().getStringExtra("UDF_NUMBER");
            strTableSplitNo = getIntent().getStringExtra("TABLE_SPLIT_NO");
            iServiceTaxType = Integer.parseInt(getIntent()
                    .getStringExtra("SERVICETAX_TYPE"));
            dServiceTaxPercent = Double.parseDouble(getIntent()
                    .getStringExtra("SERVICETAX_PERCENT"));

            //tvTitleUserName.setText(strUserName.toUpperCase());
            Date d = new Date();
            CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
            //tvTitleDate.setText("Date : " + s);
            com.wep.common.app.ActionBarUtils.setupToolbar(this,toolbar,getSupportActionBar(),"Split Bill Screen",strUserName," Date:"+s.toString());

            OBUserName = (TextView) findViewById(R.id.tvSplitBillUserName);
            OBDate = (TextView) findViewById(R.id.tvSplitBillDateValue);
            OBBillNumber = (TextView) findViewById(R.id.tvSplitBillNumberValue);

            tblOriginalBill = (TableLayout) findViewById(R.id.tblOriginalBill);
            tblSplitBill1 = (TableLayout) findViewById(R.id.tblSplitBill1);
            tblSplitBill2 = (TableLayout) findViewById(R.id.tblSplitBill2);

            txtSBNumber = (TextView) findViewById(R.id.etSplitBillBillNumber);
            txtSB1Number = (TextView) findViewById(R.id.etSplitBill1BillNumber);
            txtSB2Number = (TextView) findViewById(R.id.etSplitBill2BillNumber);

            OBSubTotal = (TextView) findViewById(R.id.tvOBSubTotalValue);
            OBTax = (TextView) findViewById(R.id.tvOBTaxTotalValue);
            OBServiceTax = (TextView) findViewById(R.id.tvOBServiceTaxValue);
            OBAmount = (TextView) findViewById(R.id.tvOBBillTotalValue);
            SB1SubTotal = (TextView) findViewById(R.id.tvSB1SubTotalValue);
            SB1Tax = (TextView) findViewById(R.id.tvSB1TaxTotalValue);
            SB1ServiceTax = (TextView) findViewById(R.id.tvSB1ServiceTaxValue);
            SB1Amount = (TextView) findViewById(R.id.tvSB1BillTotalValue);
            SB2SubTotal = (TextView) findViewById(R.id.tvSB2SubTotalValue);
            SB2Tax = (TextView) findViewById(R.id.tvSB2TaxTotalValue);
            SB2ServiceTax = (TextView) findViewById(R.id.tvSB2ServiceTaxValue);
            SB2Amount = (TextView) findViewById(R.id.tvSB2BillTotalValue);

            OBUserName.setText(strUserName);
            OBDate.setText(strDate);
            txtSBNumber.setText(strBillNumber);

            txtSB1Number.setText(String.valueOf(Integer.parseInt(strBillNumber) + 1));
            txtSB2Number.setText(String.valueOf(Integer.parseInt(strBillNumber) + 2));

            dbSplitBill.CreateDatabase();
            dbSplitBill.OpenDatabase();
            // Load the bill items to table
            LoadItemsToTable(Integer
                    .parseInt(strTableNumber), Integer.parseInt(strSubUdfNumber), Integer.parseInt(strTableSplitNo));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    private void LoadItemsToTable(int TableNo, int SubUdfNo, int TableSplitNo) {

        Cursor crsrItems = dbSplitBill.getKOTItems(TableNo, SubUdfNo, TableSplitNo);
        TableRow rowOBItem = null;
        TextView Name, Qty, Rate, Amount, TaxPercent, TaxAmt, DiscPercent, DiscAmt, DeptCode,
                CategCode, KitchenCode, TaxType, ModifierAmt, ServiceTaxPercent, ServiceTaxAmt;
        CheckBox ItemNumber;

        if (crsrItems.moveToFirst()) {
            do {
                rowOBItem = new TableRow(myContext);
                rowOBItem.setLayoutParams(new LayoutParams(
                        LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                //rowOBItem.setBackgroundResource(R.drawable.row_background);

                ItemNumber = new CheckBox(myContext);
                ItemNumber.setWidth(40);
                ItemNumber.setTextSize(0);
                ItemNumber.setTextColor(Color.TRANSPARENT);
                ItemNumber.setText(crsrItems.getString(crsrItems.getColumnIndex("ItemNumber")));

                Name = new TextView(myContext);
                Name.setWidth(150);
                Name.setText(crsrItems.getString(crsrItems.getColumnIndex("ItemName")));

                Qty = new TextView(myContext);
                Qty.setWidth(55);
                Qty.setText(crsrItems.getString(crsrItems.getColumnIndex("Quantity")));

                Rate = new TextView(myContext);
                Rate.setWidth(70);
                Rate.setText(crsrItems.getString(crsrItems.getColumnIndex("Rate")));

                Amount = new TextView(myContext);
                Amount.setWidth(85);
                Amount.setText(crsrItems.getString(crsrItems.getColumnIndex("Amount")));

                TaxPercent = new TextView(myContext);
                TaxPercent.setText(crsrItems.getString(crsrItems.getColumnIndex("TaxPercent")));

                TaxAmt = new TextView(myContext);
                TaxAmt.setText(crsrItems.getString(crsrItems.getColumnIndex("TaxAmount")));

                DiscPercent = new TextView(myContext);
                DiscPercent.setText(crsrItems.getString(crsrItems.getColumnIndex("DiscountPercent")));

                DiscAmt = new TextView(myContext);
                DiscAmt.setText(crsrItems.getString(crsrItems.getColumnIndex("DiscountAmount")));

                DeptCode = new TextView(myContext);
                DeptCode.setText(crsrItems.getString(crsrItems.getColumnIndex("DeptCode")));

                CategCode = new TextView(myContext);
                CategCode.setText(crsrItems.getString(crsrItems.getColumnIndex("CategCode")));

                KitchenCode = new TextView(myContext);
                KitchenCode.setText(crsrItems.getString(crsrItems.getColumnIndex("KitchenCode")));

                TaxType = new TextView(myContext);
                TaxType.setText(crsrItems.getString(crsrItems.getColumnIndex("TaxType")));

                ModifierAmt = new TextView(myContext);
                ModifierAmt.setText(crsrItems.getString(crsrItems.getColumnIndex("ModifierAmount")));

                ServiceTaxPercent = new TextView(myContext);
                ServiceTaxPercent.setText(crsrItems.getString(crsrItems.getColumnIndex("ServiceTaxPercent")));

                ServiceTaxAmt = new TextView(myContext);
                ServiceTaxAmt.setText(crsrItems.getString(crsrItems.getColumnIndex("ServiceTaxAmount")));

                // Add all views to table
                rowOBItem.addView(ItemNumber);
                rowOBItem.addView(Name);
                rowOBItem.addView(Qty);
                rowOBItem.addView(Rate);
                rowOBItem.addView(Amount);
                rowOBItem.addView(TaxPercent);
                rowOBItem.addView(TaxAmt);
                rowOBItem.addView(DiscPercent);
                rowOBItem.addView(DiscAmt);
                rowOBItem.addView(DeptCode);
                rowOBItem.addView(CategCode);
                rowOBItem.addView(KitchenCode);
                rowOBItem.addView(TaxType);
                rowOBItem.addView(ModifierAmt);
                rowOBItem.addView(ServiceTaxPercent);
                rowOBItem.addView(ServiceTaxAmt);

                tblOriginalBill.addView(rowOBItem, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

            } while (crsrItems.moveToNext());

            // Calculate Sub total, Tax and Bill total amount
            TotalAmount(tblOriginalBill, OBSubTotal, OBTax, OBServiceTax, OBAmount);

        } else {
            Log.d("LoadItemsToTable", "No items found");
        }
    }

    private void TotalAmount(TableLayout Table, TextView SubTotal, TextView SalesTax, TextView ServiceTax, TextView Total) {
        double dTax = 0, dModifier = 0, dSubTotal = 0, dServiceTax = 0;

        for (int iRow = 1; iRow < Table.getChildCount(); iRow++) {

            TableRow RowItem = (TableRow) Table.getChildAt(iRow);

            if (RowItem.getChildAt(0) != null) {

                TextView ColTaxType = (TextView) RowItem.getChildAt(12);
                TextView ColAmount = (TextView) RowItem.getChildAt(4);
                TextView ColDisc = (TextView) RowItem.getChildAt(8);
                TextView ColTax = (TextView) RowItem.getChildAt(6);
                TextView ColModifierAmount = (TextView) RowItem.getChildAt(13);
                TextView ColServiceTax = (TextView) RowItem.getChildAt(15);

                dTax += Double.parseDouble(ColTax.getText().toString());
                dModifier += Double.parseDouble(ColModifierAmount.getText().toString());
                dServiceTax += Double.parseDouble(ColServiceTax.getText().toString());

                if (ColTaxType.getText().toString().equalsIgnoreCase("1")) {

                    dSubTotal += Double.parseDouble(ColAmount.getText().toString()) -
                            Double.parseDouble(ColDisc.getText().toString());
                } else {

                    dSubTotal += (Double.parseDouble(ColAmount.getText().toString()) -
                            Double.parseDouble(ColDisc.getText().toString())) -
                            Double.parseDouble(ColTax.getText().toString());
                }
            }
        }

        SubTotal.setText(String.format("%.2f", dSubTotal));
        SalesTax.setText(String.format("%.2f", dTax));

        if (iServiceTaxType == 1) {
            ServiceTax.setText(String.format("%.2f", dServiceTax));
            Total.setText(String.format("%.2f", dSubTotal + dTax + dModifier + dServiceTax));
        } else {
            dServiceTax = (dSubTotal + dTax + dModifier) * (dServiceTaxPercent / 100);
            ServiceTax.setText(String.format("%.2f", dServiceTax));
            Total.setText(String.format("%.2f", dSubTotal + dTax + dModifier + dServiceTax));
        }
    }

    private void SplitBill(int SplitBillNumber) {
        CheckBox MenuCode;

        for (int iRow = 1; iRow < tblOriginalBill.getChildCount(); iRow++) {

            TableRow Row = (TableRow) tblOriginalBill.getChildAt(iRow);

            if (Row.getChildAt(0) != null) {

                MenuCode = (CheckBox) Row.getChildAt(0);

                if (MenuCode.isChecked()) {

                    if (SplitBillNumber == 1) {
                        // Move the item to split bill 1 table
                        MoveItemRow(Row, 1);
                    } else {
                        // Move the item to split bill 2 table
                        MoveItemRow(Row, 2);
                    }

                    // Remove all the view present in the row.
                    Row.removeAllViews();

                    // Remove the row
                    tblOriginalBill.removeView(Row);

                    // Exit from the loop
                    break;
                }
            } else {
                continue;
            }

        }

        // Original Bill
        TotalAmount(tblOriginalBill, OBSubTotal, OBTax, OBServiceTax, OBAmount);
        if (SplitBillNumber == 1) {
            // Split bill 1
            TotalAmount(tblSplitBill1, SB1SubTotal, SB1Tax, SB1ServiceTax, SB1Amount);
        } else {
            // Split bill 2
            TotalAmount(tblSplitBill2, SB2SubTotal, SB2Tax, SB2ServiceTax, SB2Amount);
        }
    }

    @SuppressWarnings("deprecation")
    private void MoveItemRow(TableRow rowItem, int SpliBillNumber) {
        TextView Name, Qty, Rate, Amount, TaxPercent, TaxAmt, DiscPercent, DiscAmt, DeptCode,
                CategCode, KitchenCode, TaxType, ModifierAmt, ServiceTaxPercent, ServiceTaxAmt;
        CheckBox Number;

        TableRow RowItem = new TableRow(myContext);

        RowItem.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        //RowItem.setBackgroundResource(R.drawable.row_background);

        // ItemNumber
        Number = new CheckBox(myContext);
        Number.setWidth(40);
        Number.setTextSize(0);
        Number.setTextColor(Color.TRANSPARENT);
        Number.setText(((CheckBox) rowItem.getChildAt(0)).getText());

        // Item Name
        Name = new TextView(myContext);
        Name.setWidth(150);
        Name.setText(((TextView) rowItem.getChildAt(1)).getText());

        // Quantity
        Qty = new TextView(myContext);
        Qty.setWidth(55);
        Qty.setText(((TextView) rowItem.getChildAt(2)).getText());

        // Rate
        Rate = new TextView(myContext);
        Rate.setWidth(70);
        Rate.setText(((TextView) rowItem.getChildAt(3)).getText());

        // Amount
        Amount = new TextView(myContext);
        Amount.setWidth(75);
        Amount.setText(((TextView) rowItem.getChildAt(4)).getText());

        // Sales Tax %
        TaxPercent = new TextView(myContext);
        TaxPercent.setText(((TextView) rowItem.getChildAt(5)).getText());

        // Sales Tax Amount
        TaxAmt = new TextView(myContext);
        TaxAmt.setText(((TextView) rowItem.getChildAt(6)).getText());

        // Discount %
        DiscPercent = new TextView(myContext);
        DiscPercent.setText(((TextView) rowItem.getChildAt(7)).getText());

        // Discount Amount
        DiscAmt = new TextView(myContext);
        DiscAmt.setText(((TextView) rowItem.getChildAt(8)).getText());

        // Dept Code
        DeptCode = new TextView(myContext);
        DeptCode.setText(((TextView) rowItem.getChildAt(9)).getText());

        // Categ Code
        CategCode = new TextView(myContext);
        CategCode.setText(((TextView) rowItem.getChildAt(10)).getText());

        // Kitchen Code
        KitchenCode = new TextView(myContext);
        KitchenCode.setText(((TextView) rowItem.getChildAt(11)).getText());

        // Tax Type
        TaxType = new TextView(myContext);
        TaxType.setText(((TextView) rowItem.getChildAt(12)).getText());

        // Modifier Amount
        ModifierAmt = new TextView(myContext);
        ModifierAmt.setText(((TextView) rowItem.getChildAt(13)).getText());

        // Service Tax Percent
        ServiceTaxPercent = new TextView(myContext);
        ServiceTaxPercent.setText(((TextView) rowItem.getChildAt(14)).getText());

        // Service Tax Amount
        ServiceTaxAmt = new TextView(myContext);
        ServiceTaxAmt.setText(((TextView) rowItem.getChildAt(15)).getText());

        // Add all views to row
        RowItem.addView(Number);
        RowItem.addView(Name);
        RowItem.addView(Qty);
        RowItem.addView(Rate);
        RowItem.addView(Amount);
        RowItem.addView(TaxPercent);
        RowItem.addView(TaxAmt);
        RowItem.addView(DiscPercent);
        RowItem.addView(DiscAmt);
        RowItem.addView(DeptCode);
        RowItem.addView(CategCode);
        RowItem.addView(KitchenCode);
        RowItem.addView(TaxType);
        RowItem.addView(ModifierAmt);
        RowItem.addView(ServiceTaxPercent);
        RowItem.addView(ServiceTaxAmt);

        if (SpliBillNumber == 1) {
            // Copy new Item row to SplitBill1 table
            tblSplitBill1.addView(RowItem, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        } else {
            // Copy new Item row to SplitBill1 table
            tblSplitBill2.addView(RowItem, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        }
    }

    private void UpdateItemStock(Cursor crsrUpdateStock, float Quantity) {
        int iResult = 0;
        float fCurrentStock = 0, fNewStock = 0;

        // Get current stock of item
        fCurrentStock = crsrUpdateStock.getFloat(
                crsrUpdateStock.getColumnIndex("StockQty"));

        // New Stock
        fNewStock = fCurrentStock - Quantity;

        // Update new stock for item
        iResult = dbSplitBill.updateItemStock(crsrUpdateStock.getInt(
                crsrUpdateStock.getColumnIndex("MenuCode")), fNewStock);

        Log.d("UpdateItemStock", "Updated Rows:" + iResult);

    }

    private void InsertBillItems(TableLayout TableBillItems, int BillNumber) {

        // Inserted Row Id in database table
        long lResult = 0;

        // Bill item object
        BillItem objBillItem;

        Cursor crsrUpdateItemStock = null;

        for (int iRow = 1; iRow < TableBillItems.getChildCount(); iRow++) {
            objBillItem = new BillItem();

            TableRow RowBillItem = (TableRow) TableBillItems.getChildAt(iRow);

            // Increment Total item count if row is not empty
            if (RowBillItem.getChildCount() > 0) {
                iTotalItems++;
            }

            // Bill Number
            objBillItem.setBillNumber(String.valueOf(BillNumber));

            // Item Number
            if (RowBillItem.getChildAt(0) != null) {
                CheckBox ItemNumber = (CheckBox)
                        RowBillItem.getChildAt(0);
                objBillItem.setItemNumber(Integer.
                        parseInt(ItemNumber.getText().toString()));

                crsrUpdateItemStock = dbSplitBill.getItem(Integer.
                        parseInt(ItemNumber.getText().toString()));
            }

            // Item Name
            if (RowBillItem.getChildAt(1) != null) {
                TextView ItemName = (TextView)
                        RowBillItem.getChildAt(1);
                objBillItem.setItemName(ItemName.getText().toString());
            }

            // Quantity
            if (RowBillItem.getChildAt(2) != null) {
                TextView Quantity = (TextView)
                        RowBillItem.getChildAt(2);
                objBillItem.setQuantity(Float.
                        parseFloat(Quantity.getText().toString()));

                if (crsrUpdateItemStock.moveToFirst()) {
                    // Check if item's bill with stock enabled update the stock quantity
                    if (crsrUpdateItemStock.getInt(crsrUpdateItemStock.getColumnIndex("BillWithStock")) == 1) {
                        UpdateItemStock(crsrUpdateItemStock, Float.
                                parseFloat(Quantity.getText().toString()));
                    }
                }
            }

            // Rate
            if (RowBillItem.getChildAt(3) != null) {
                TextView Rate = (TextView)
                        RowBillItem.getChildAt(3);
                objBillItem.setValue(Float.
                        parseFloat(Rate.getText().toString()));
            }

            // Amount
            if (RowBillItem.getChildAt(4) != null) {
                TextView Amount = (TextView)
                        RowBillItem.getChildAt(4);
                objBillItem.setAmount(Float.
                        parseFloat(Amount.getText().toString()));
            }

            // Sales Tax %
            if (RowBillItem.getChildAt(5) != null) {
                TextView SalesTaxPercent = (TextView)
                        RowBillItem.getChildAt(5);
                objBillItem.setTaxPercent(Float.
                        parseFloat(SalesTaxPercent.getText().toString()));
            }

            // Sales Tax Amount
            if (RowBillItem.getChildAt(6) != null) {
                TextView SalesTaxAmount = (TextView)
                        RowBillItem.getChildAt(6);
                objBillItem.setTaxAmount(Float.
                        parseFloat(SalesTaxAmount.getText().toString()));
            }

            // Discount %
            if (RowBillItem.getChildAt(7) != null) {
                TextView DiscountPercent = (TextView)
                        RowBillItem.getChildAt(7);
                objBillItem.setDiscountPercent(Float.
                        parseFloat(DiscountPercent.getText().toString()));
            }

            // Discount Amount
            if (RowBillItem.getChildAt(8) != null) {
                TextView DiscountAmount = (TextView)
                        RowBillItem.getChildAt(8);
                objBillItem.setDiscountAmount(Float.
                        parseFloat(DiscountAmount.getText().toString()));

                fTotalDiscount += Float.
                        parseFloat(DiscountAmount.getText().toString());
            }

            // Department Code
            if (RowBillItem.getChildAt(9) != null) {
                TextView DeptCode = (TextView)
                        RowBillItem.getChildAt(9);
                objBillItem.setDeptCode(Integer.
                        parseInt(DeptCode.getText().toString()));
            }

            // Category Code
            if (RowBillItem.getChildAt(10) != null) {
                TextView CategCode = (TextView)
                        RowBillItem.getChildAt(10);
                objBillItem.setCategCode(Integer.
                        parseInt(CategCode.getText().toString()));
            }

            // Kitchen Code
            if (RowBillItem.getChildAt(11) != null) {
                TextView KitchenCode = (TextView)
                        RowBillItem.getChildAt(11);
                objBillItem.setKitchenCode(Integer.
                        parseInt(KitchenCode.getText().toString()));
            }

            // Tax Type
            if (RowBillItem.getChildAt(12) != null) {
                TextView TaxType = (TextView)
                        RowBillItem.getChildAt(12);
                objBillItem.setTaxType(Integer.
                        parseInt(TaxType.getText().toString()));
            }

            // Modifier Amount
            if (RowBillItem.getChildAt(13) != null) {
                TextView ModifierAmount = (TextView)
                        RowBillItem.getChildAt(13);
                objBillItem.setModifierAmount(Float.
                        parseFloat(ModifierAmount.getText().toString()));
            }

            // Service Tax Percent
            if (RowBillItem.getChildAt(14) != null) {
                TextView ServiceTaxPercent = (TextView)
                        RowBillItem.getChildAt(14);
                objBillItem.setServiceTaxPercent(Float.
                        parseFloat(ServiceTaxPercent.getText().toString()));
            }

            // Service Tax Amount
            if (RowBillItem.getChildAt(15) != null) {
                TextView ServiceTaxAmount = (TextView)
                        RowBillItem.getChildAt(15);
                objBillItem.setServiceTaxAmount(Float.
                        parseFloat(ServiceTaxAmount.getText().toString()));
            }

            lResult = dbSplitBill.addBillItem(objBillItem);
            Log.d("InsertBillItem", "Bill item inserted at position:" + lResult);
        }
    }

    private void InsertBillDetail(int BillNumber, TextView Tax, TextView ServiceTax, TextView BillTotal) {

        // Inserted Row Id in database table
        long lResult = 0;

        // BillDetail object
        BillDetail objBillDetail;

        objBillDetail = new BillDetail();

        // Date
        objBillDetail.setDate(OBDate.getText().toString());

        // Bill Number
        objBillDetail.setBillNumber(BillNumber);

        // Total Items
        objBillDetail.setTotalItems(iTotalItems);

        // Bill Amount
        objBillDetail.setBillAmount(Float.
                parseFloat(BillTotal.getText().toString()));

        // Discount Amount
        objBillDetail.setTotalDiscountAmount(fTotalDiscount);

        // Sales Tax Amount
        objBillDetail.setTotalTaxAmount(Float.
                parseFloat(Tax.getText().toString()));

        // Service Tax Amount
        objBillDetail.setTotalTaxAmount(Float.
                parseFloat(ServiceTax.getText().toString()));

        // Delivery Charge
        objBillDetail.setDeliveryCharge(0);

        // Payment types
        // Cash Payment
        objBillDetail.setCashPayment(Float
                .parseFloat(BillTotal.getText().toString()));

        // Card Payment
        objBillDetail.setCardPayment(0);

        // Coupon Payment
        objBillDetail.setCouponPayment(0);

        // Reprint Count
        objBillDetail.setReprintCount(0);

        // Bill Status
        objBillDetail.setBillStatus(1);

        // Employee Id (Waiter / Rider)
        objBillDetail.setEmployeeId(iEmployeeId);

        // User Id
        objBillDetail.setUserId(strUserId);

        lResult = dbSplitBill.addBill(objBillDetail, "");
        Log.d("InsertBill", "Bill inserted at position:" + lResult);
    }

    public void SplitBill1(View v) {
        if (tblOriginalBill.getChildCount() <= 2) {
            MsgBox.Show("Warning", "Can not split any more, original bill should have one or more items");
            return;
        } else {
            SplitBill(1);
        }
    }

    public void SplitBill2(View v) {
        if (tblOriginalBill.getChildCount() <= 2) {
            MsgBox.Show("Warning", "Can not split any more, original bill should have one or more items");
            return;
        }
        if (tblSplitBill1.getChildCount() < 2) {
            MsgBox.Show("Warning", "Can not split any more, original bill should have one or more items");
            return;
        } else {
            SplitBill(2);
        }
    }

    public void TenderSplit(View v) {
        int iResult = 0;

        if (tblOriginalBill.getChildCount() >= 2) {
            InsertBillItems(tblOriginalBill,
                    Integer.parseInt(OBBillNumber.getText().toString()));
            InsertBillDetail(Integer.parseInt(OBBillNumber.getText().toString()),
                    OBTax, OBServiceTax, OBAmount);

            if (tblSplitBill1.getChildCount() >= 2) {
                InsertBillItems(tblSplitBill1,
                        Integer.parseInt(txtSB1Number.getText().toString()));
                InsertBillDetail(Integer.parseInt(txtSB1Number.getText().toString()),
                        SB1Tax, SB1ServiceTax, SB1Amount);
            }
            if (tblSplitBill2.getChildCount() >= 2) {
                InsertBillItems(tblSplitBill2,
                        Integer.parseInt(txtSB2Number.getText().toString()));
                InsertBillDetail(Integer.parseInt(txtSB2Number.getText().toString()),
                        SB2Tax, SB2ServiceTax, SB2Amount);

            }

            iResult = dbSplitBill.deleteKOTItems(Integer
                    .parseInt(strTableNumber), Integer.parseInt(strSubUdfNumber), Integer.parseInt(strTableSplitNo));
            Log.d("SplitBillActivity", "Rows deleted from pending KOT:" + iResult);

        } else {
            MsgBox.Show("Warning", "No items in bill");
            return;
        }

        // Finish the activity
        Close(v);
    }

    public void Close(View v) {
        // Close database connection
        dbSplitBill.CloseDatabase();
        // finish activity
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);
            LayoutInflater UserAuthorization = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vwAuthorization = UserAuthorization.inflate(R.layout.user_authorization, null);
            final EditText txtUserId = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserId);
            final EditText txtPassword = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserPassword);
            final TextView tvAuthorizationUserId= (TextView) vwAuthorization.findViewById(R.id.tvAuthorizationUserId);
            final TextView tvAuthorizationUserPassword= (TextView) vwAuthorization.findViewById(R.id.tvAuthorizationUserPassword);
            tvAuthorizationUserId.setVisibility(View.GONE);
            tvAuthorizationUserPassword.setVisibility(View.GONE);
            txtUserId.setVisibility(View.GONE);
            txtPassword.setVisibility(View.GONE);
            AuthorizationDialog
                    .setTitle("Are you sure you want to exit ?")
                    .setView(vwAuthorization)
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            /*Intent returnIntent =new Intent();
                            setResult(Activity.RESULT_OK,returnIntent);*/
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
