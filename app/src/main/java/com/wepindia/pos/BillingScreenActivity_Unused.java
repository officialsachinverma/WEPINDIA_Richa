/****************************************************************************
 * Project Name		:	VAJRA
 * <p/>
 * File Name		:	BillingScreenActivity_Unused
 * <p/>
 * Purpose			:	Represents Billing screen activity, takes care of all
 * UI back end operations in this activity, such as event
 * handling data read from or display in views.
 * <p/>
 * DateOfCreation	:	10-December-2012
 * <p/>
 * Author			:	Balasubramanya Bharadwaj B S
 ****************************************************************************/
package com.wepindia.pos;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.mswipetech.wisepad.sdktest.view.ApplicationData;
import com.wep.common.app.Database.BillDetail;
import com.wep.common.app.Database.BillItem;
import com.wep.common.app.Database.ComplimentaryBillDetail;
import com.wep.common.app.Database.Customer;
import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.Database.DeletedKOT;
import com.wep.common.app.Database.PendingKOT;
import com.wep.common.app.print.BillKotItem;
import com.wep.common.app.print.BillServiceTaxItem;
import com.wep.common.app.print.BillSubTaxItem;
import com.wep.common.app.print.BillTaxItem;
import com.wep.common.app.print.PrintKotBillItem;
import com.wep.common.app.utils.Preferences;
import com.wep.common.app.views.WepButton;
import com.wepindia.pos.GenericClasses.EditTextInputHandler;
import com.wepindia.pos.GenericClasses.ImageAdapter;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.utils.ActionBarUtils;
import com.wepindia.pos.utils.StockOutwardMaintain;
import com.wepindia.printers.WepPrinterBaseActivity;
import com.wepindia.printers.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class BillingScreenActivity_Unused extends WepPrinterBaseActivity {

    private static final String TAG = BillingScreenActivity_Unused.class.getSimpleName();
    private static final int CUTOMER_ORDER_REQUEST_CODE = 234532;
    Context myContext;
    DatabaseHandler dbBillScreen = new DatabaseHandler(BillingScreenActivity_Unused.this);
    MessageDialog MsgBox;
    EditText txtSearchItemBarcode,  tvWaiterNumber, tvTableNumber, tvTableSplitNo, tvBillNumber;
    ListView lstvwDepartment, lstvwCategory, lstvwKOTModifiers;
    GridView grdItems;
    TableLayout tblOrderItems;
    TextView tvSalesTax;
    TextView tvServiceTax;
    WepButton btnSplitBill, btnShiftTable, btnMergeTable, btn_ReprintKOT,btnModifier, btnSaveKOT, btnPayBill, btnLoadKOT, btnDeleteKOT,btnDeleteBill, btnKOTStatus, btnPrintKOT, btnPrintBill, btnClear, btnReprint;
    TextView tvDate, tvSubTotal, tvTaxTotal, tvServiceTaxTotal, tvBillAmount, tvSubUdfValue, txtOthercharges;
    TextView tvWaiterName, tVLabelTableNo, tVLabelWaiterNo, tVLabelOrderNo;
    EditText edtCustId, edtCustName, edtCustPhoneNo, edtCustAddress, edtCustDineInPhoneNo, etCustGSTIN;
    Button btnAddCustomer, btn_item_fastBillingMode;
    CheckBox chk_interstate = null;
    EditText et_pos = null;
    RelativeLayout relative_Interstate, relative_pos, relative_waiterno, relative_tableNo;
    Calendar Time; // Time variable
    private LinearLayout idd_date;
    Cursor crsrSettings = null, crsrCustomerDetails = null;
    ArrayAdapter<String> adapDept, adapCateg, adapModifiers;
    String[] Name;
    int REPRINT_KOT =0;
    String[] ImageUri;
    String strUserId = "", strUserName = "", strDate = "";
    boolean bAuthorizationResult = false;
    int[] MenuCode;
    byte jBillingMode = 0, jWeighScale = 0;
    int BillwithStock = 0;
    int iTaxType = 0, iTotalItems = 0, iCustId = 0, iTokenNumber = 0;
    float fTotalDiscount = 0, fCashPayment = 0, fCardPayment = 0, fCouponPayment = 0, fPettCashPayment = 0, fPaidTotalPayment = 0;
    float fChangePayment = 0;
    float fWalletPayment = 0;
    double dServiceTaxPercent = 0, dOtherChrgs = 0;
    String strPaymentStatus = "", strMakeOrder = "";
    Date d;
    int PrintBillPayment = 0;
    AutoCompleteTextView aTViewSearchItem, aTViewSearchMenuCode;
    Spinner spnr_pos;
    ArrayAdapter<CharSequence> POS_LIST;
    SimpleCursorAdapter deptdataAdapter, categdataAdapter;
    LinearLayout rowbtns, rowtexts;
    String GSTEnable = "", HSNEnable_out = "", POSEnable = "";
    TextView tvHSNCode_out;
    Date strDate_date;
    float fTotalsubTaxPercent = 0;
    Button btndepart, btncateg, btnitem;
    TextView tvdeptline, tvcategline;
    String DineInCaption ="" ,CounterSalesCaption="",TakeAwayCaption="",HomeDeliveryCaption="";
    int iKOTNo = -1;
    int iPrintKOTStatus = 0;
    public boolean isPrinterAvailable = false;
    private Toolbar toolbar;
    private AppCompatDelegate delegate;

    public void onSohamsaPrinterResponses(String resp) {

    }

    public void connectionStatus(String statusTxt) {

    }

    public void onConfigurationRequired() {

    }

    public void onPrinterAvailable() {
        isPrinterAvailable = true;
    }

    /************************************************************************************************************************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billingscreen);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textViewCenter = (TextView) toolbar.findViewById(com.wep.common.app.R.id.textViewCenter);
        tvSalesTax = (TextView) findViewById(R.id.tvTaxTotal);
        tvServiceTax = (TextView) findViewById(R.id.tvServiceTax);
        myContext = this;
        try {
            MsgBox = new MessageDialog(myContext);
            String strBillMode = getIntent().getStringExtra("BILLING_MODE");
            jBillingMode = Byte.parseByte(strBillMode);
            strUserId = ApplicationData.getUserId(this);//ApplicationData.USER_ID;
            strUserName = ApplicationData.getUserName(this);//ApplicationData.USER_NAME;
            iCustId = getIntent().getIntExtra("CUST_ID", 0);
            strPaymentStatus = getIntent().getStringExtra("Payment_Status");
            strMakeOrder = getIntent().getStringExtra("MAKE_ORDER");
            d = new Date();
            CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
            com.wep.common.app.ActionBarUtils.setupToolbar(BillingScreenActivity_Unused.this,toolbar,getSupportActionBar(),"",strUserName," Date:"+s.toString());
            IntializeViewVariables();
            dbBillScreen.OpenDatabase();
            crsrSettings = dbBillScreen.getBillSetting();
            if (crsrSettings.moveToFirst())
            {
                DineInCaption = crsrSettings.getString(crsrSettings.getColumnIndex("HomeDineInCaption"));
                CounterSalesCaption = crsrSettings.getString(crsrSettings.getColumnIndex("HomeCounterSalesCaption"));
                TakeAwayCaption = crsrSettings.getString(crsrSettings.getColumnIndex("HomeTakeAwayCaption"));
                HomeDeliveryCaption = crsrSettings.getString(crsrSettings.getColumnIndex("HomeHomeDeliveryCaption"));
                if (crsrSettings.getInt(crsrSettings.getColumnIndex("DateAndTime")) == 1)
                {
                    Date date1 = new Date();
                    try {
                        CharSequence sdate = DateFormat.format("dd-MM-yyyy", date1.getTime());
                        tvDate.setText(String.valueOf(sdate));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    String strDate = crsrSettings.getString(crsrSettings.getColumnIndex("BusinessDate"));
                    try {
                        tvDate.setText(String.valueOf(strDate));
                        Date date1 = new Date();
                        CharSequence sdate = DateFormat.format("dd-MM-yyyy", date1.getTime());
                        if(strDate.equals(sdate.toString()))
                            idd_date.setVisibility(View.INVISIBLE);
                        else
                            idd_date.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                BillwithStock = crsrSettings.getInt(crsrSettings.getColumnIndex("BillwithStock"));
                iTaxType = crsrSettings.getInt(crsrSettings.getColumnIndex("TaxType"));
            }

           /* if ((crsrSettings != null) && crsrSettings.moveToFirst()) {
                GSTEnable = crsrSettings.getString(crsrSettings.getColumnIndex("GSTEnable"));
                HSNEnable_out = crsrSettings.getString(crsrSettings.getColumnIndex("HSNCode_Out"));
                if (HSNEnable_out == null) {
                    HSNEnable_out = "0";
                }
                POSEnable = crsrSettings.getString(crsrSettings.getColumnIndex("POS_Out"));
                if (POSEnable == null) {
                    POSEnable = "0";
                }

            }
            if ((GSTEnable != null) && (GSTEnable.equals("1"))) {
                // setting TAX name
                tvSalesTax.setText(NAME_CGST_RATE);
                tvServiceTax.setText(NAME_SGST_RATE);
                etCustGSTIN.setVisibility(View.VISIBLE);


            } else {
                tvSalesTax.setText(NAME_SALES_RATE);
                tvServiceTax.setText(NAME_SERVICE_RATE);
                etCustGSTIN.setVisibility(View.GONE);
            }*/

            tvTableNumber.setEnabled(false);
            tvTableSplitNo.setEnabled(false);
            tvWaiterNumber.setEnabled(false);

            if (jBillingMode == 1)
            {
                textViewCenter.setText(DineInCaption);
                btnDeleteKOT.setEnabled(false);
                rowbtns.setVisibility(View.VISIBLE);
                btnPayBill.setEnabled(false);
                btnPrintKOT.setEnabled(true);
                btnPrintBill.setEnabled(false);
                // Table Activity
                if (TableActivity.TABLE_NO.equalsIgnoreCase("") && TableActivity.TABLE_SPLIT_NO.equalsIgnoreCase(""))
                {
                    Close(null);
                }
                else if (TableActivity.TABLE_NO.equalsIgnoreCase("0") && TableActivity.WAITER_NO.equalsIgnoreCase("0"))
                {
                    MsgBox.Show("Warning", "Either Load KOT or Select Table and Waiter");
                }
                else
                {
                    tvTableNumber.setText(getIntent().getStringExtra(TableActivity.TABLE_NO));
                    tvWaiterNumber.setText(getIntent().getStringExtra(TableActivity.WAITER_NO));
                    tvTableSplitNo.setText(getIntent().getStringExtra(TableActivity.TABLE_SPLIT_NO));
                    if (tvTableNumber.getText().toString().equalsIgnoreCase("0") && tvWaiterNumber.getText().toString().equalsIgnoreCase("0"))
                    {
                        MsgBox.Show("Warning", "Either Load KOT or Select Table and Waiter");
                    }
                    Cursor LoadKOT = dbBillScreen.getKOTItems(Integer.parseInt(tvTableNumber.getText().toString()), Integer.parseInt(tvSubUdfValue.getText().toString()), Integer.parseInt(tvTableSplitNo.getText().toString()));
                    if (LoadKOT.moveToFirst())
                    {
                        LoadKOTItems(LoadKOT);
                        btnPayBill.setEnabled(true);
                        btnPrintKOT.setEnabled(true);
                        btnPrintBill.setEnabled(true);
                    }

                    Cursor crsrWaiter = dbBillScreen.getUsers(tvWaiterNumber.getText().toString());
                    if (crsrWaiter.moveToFirst())
                    {
                        tvWaiterName.setText(crsrWaiter.getString(crsrWaiter.getColumnIndex("Name")));
                    }
                    else
                    {
                        tvWaiterName.setText("Waiter");
                    }
                }
                // Table Activity
                if (crsrSettings.getInt(crsrSettings.getColumnIndex("TableSpliting")) == 1) {
                    tvTableSplitNo.setVisibility(View.VISIBLE);
                } else {
                    tvTableSplitNo.setVisibility(View.GONE);
                }

                if (GSTEnable.equalsIgnoreCase("1")) {
                    if (HSNEnable_out.equalsIgnoreCase("1")) {
                        tvHSNCode_out.setVisibility(View.VISIBLE);
                    } else {
                        tvHSNCode_out.setVisibility(View.INVISIBLE);
                    }
                } else { // no gst
                    tvHSNCode_out.setVisibility(View.INVISIBLE);
                }
                Cursor crssOtherChrg = dbBillScreen.getKOTModifierByModes(DineInCaption);
                if (crssOtherChrg.moveToFirst()) {
                    do {
                        dOtherChrgs += crssOtherChrg.getDouble(crssOtherChrg.getColumnIndex("ModifierAmount"));
                    } while (crssOtherChrg.moveToNext());
                    txtOthercharges.setText(String.format("%.2f", dOtherChrgs));
                }
            } else if (jBillingMode == 2) {
                textViewCenter.setText(CounterSalesCaption);
                rowtexts.setVisibility(View.VISIBLE);
                btnPayBill.setEnabled(false);
                btnPrintKOT.setEnabled(false);
                // btnPrintBill.setEnabled(false);
                tvTableNumber.setText("0");
                tvTableSplitNo.setText("0");
                tvWaiterNumber.setText("0");
                tvTableNumber.setEnabled(false);
                tvTableSplitNo.setEnabled(false);
                tvWaiterNumber.setEnabled(false);

                tVLabelTableNo.setVisibility(View.INVISIBLE);
                tVLabelWaiterNo.setVisibility(View.INVISIBLE);
                tvTableNumber.setVisibility(View.INVISIBLE);
                tvTableSplitNo.setVisibility(View.INVISIBLE);
                tvWaiterNumber.setVisibility(View.INVISIBLE);
                tvWaiterName.setVisibility(View.INVISIBLE);

                btnKOTStatus.setVisibility(View.INVISIBLE);
                btnSaveKOT.setVisibility(View.INVISIBLE);
                btnPrintKOT.setVisibility(View.INVISIBLE);
                btnPrintBill.setEnabled(true);
                btnPayBill.setEnabled(true);

                // gst
                if (GSTEnable.equalsIgnoreCase("1")) {
                    if ((HSNEnable_out != null) && (HSNEnable_out.equalsIgnoreCase("1"))) {
                        tvHSNCode_out.setVisibility(View.VISIBLE);
                    } else {
                        tvHSNCode_out.setVisibility(View.INVISIBLE);
                    }

                    if ((POSEnable != null) && (POSEnable.equalsIgnoreCase("1"))) {
                        relative_tableNo.setVisibility(View.GONE);
                        relative_waiterno.setVisibility(View.GONE);
                        relative_Interstate.setVisibility(View.VISIBLE);
                        relative_pos.setVisibility(View.VISIBLE);
                    } else {
                        relative_tableNo.setVisibility(View.VISIBLE);
                        relative_waiterno.setVisibility(View.VISIBLE);
                        relative_Interstate.setVisibility(View.GONE);
                        relative_pos.setVisibility(View.GONE);
                    }

                } else { // no gst
                    tvHSNCode_out.setVisibility(View.INVISIBLE);
                    relative_tableNo.setVisibility(View.VISIBLE);
                    relative_waiterno.setVisibility(View.VISIBLE);
                    relative_Interstate.setVisibility(View.GONE);
                    relative_pos.setVisibility(View.GONE);
                }
                Cursor crssOtherChrg = dbBillScreen.getKOTModifierByModes(CounterSalesCaption);
                if (crssOtherChrg.moveToFirst()) {
                    do {
                        dOtherChrgs += crssOtherChrg.getDouble(crssOtherChrg.getColumnIndex("ModifierAmount"));
                    } while (crssOtherChrg.moveToNext());
                    txtOthercharges.setText(String.format("%.2f", dOtherChrgs));
                }
            } else if (jBillingMode == 3) {
                textViewCenter.setText(TakeAwayCaption);
                ControlsSetEnabled();
                rowtexts.setVisibility(View.VISIBLE);
                etCustGSTIN.setVisibility(View.GONE);

                //btnSaveKOT.setEnabled(false);   //-> enabled in ControlSetEnabled()
                //btnPrintKOT.setEnabled(false);  //-> enabled in ControlSetEnabled()
                btnPrintBill.setEnabled(false);
                btnPayBill.setEnabled(false);
                btnClear.setEnabled(true);
                btnReprint.setEnabled(true);
                btnKOTStatus.setEnabled(true);

                tvTableNumber.setText("0");
                tvTableSplitNo.setText("0");
                tvWaiterNumber.setText("0");
                tvTableNumber.setEnabled(false);
                tvTableSplitNo.setEnabled(false);
                tvWaiterNumber.setEnabled(false);

                tVLabelTableNo.setVisibility(View.INVISIBLE);
                tVLabelWaiterNo.setVisibility(View.INVISIBLE);
                tvTableNumber.setVisibility(View.INVISIBLE);
                tvTableSplitNo.setVisibility(View.INVISIBLE);
                tvWaiterNumber.setVisibility(View.INVISIBLE);
                tvWaiterName.setVisibility(View.INVISIBLE);
                btnKOTStatus.setText("PickUp Status");

                // gst
                if (GSTEnable.equalsIgnoreCase("1")) {
                    if ((HSNEnable_out != null) && (HSNEnable_out.equalsIgnoreCase("1"))) {
                        tvHSNCode_out.setVisibility(View.VISIBLE);
                    } else {
                        tvHSNCode_out.setVisibility(View.INVISIBLE);
                    }

                    if ((POSEnable != null) && (POSEnable.equalsIgnoreCase("1"))) {
                        relative_tableNo.setVisibility(View.GONE);
                        relative_waiterno.setVisibility(View.GONE);
                        relative_Interstate.setVisibility(View.VISIBLE);
                        relative_pos.setVisibility(View.VISIBLE);
                    } else {
                        relative_tableNo.setVisibility(View.VISIBLE);
                        relative_waiterno.setVisibility(View.VISIBLE);
                        relative_Interstate.setVisibility(View.GONE);
                        relative_pos.setVisibility(View.GONE);
                    }

                } else { // no gst
                    tvHSNCode_out.setVisibility(View.INVISIBLE);
                    relative_tableNo.setVisibility(View.VISIBLE);
                    relative_waiterno.setVisibility(View.VISIBLE);
                    relative_Interstate.setVisibility(View.GONE);
                    relative_pos.setVisibility(View.GONE);
                }

                Cursor crssOtherChrg = dbBillScreen.getKOTModifierByModes(TakeAwayCaption);
                if (crssOtherChrg.moveToFirst()) {
                    do {
                        dOtherChrgs += crssOtherChrg.getDouble(crssOtherChrg.getColumnIndex("ModifierAmount"));
                    } while (crssOtherChrg.moveToNext());
                    txtOthercharges.setText(String.format("%.2f", dOtherChrgs));
                }

            } else {// jBillingMode == 4
                textViewCenter.setText(HomeDeliveryCaption);
                //ControlsSetDisabled();
                ControlsSetEnabled();
                rowtexts.setVisibility(View.VISIBLE);
                etCustGSTIN.setVisibility(View.GONE);

                //btnSaveKOT.setEnabled(false);   //-> enabled in ControlSetEnabled()
                //btnPrintKOT.setEnabled(false);  //-> enabled in ControlSetEnabled()
                btnPrintBill.setEnabled(false);
                btnPayBill.setEnabled(false);
                btnClear.setEnabled(true);
                btnReprint.setEnabled(true);
                btnKOTStatus.setEnabled(true);
                btnKOTStatus.setText("Delivery Status");
                
                
                tvTableNumber.setText("0");
                tvTableSplitNo.setText("0");
                tvWaiterNumber.setText("0");
                tvTableNumber.setEnabled(false);
                tvTableSplitNo.setEnabled(false);
                tvWaiterNumber.setEnabled(false);

                tVLabelTableNo.setVisibility(View.INVISIBLE);
                tVLabelWaiterNo.setVisibility(View.INVISIBLE);
                tvTableNumber.setVisibility(View.INVISIBLE);
                tvTableSplitNo.setVisibility(View.INVISIBLE);
                tvWaiterNumber.setVisibility(View.INVISIBLE);
                tvWaiterName.setVisibility(View.INVISIBLE);
                

                //gst
                /*if (GSTEnable.equalsIgnoreCase("1")) {
                    if ((HSNEnable_out != null) && (HSNEnable_out.equalsIgnoreCase("1"))) {
                        tvHSNCode_out.setVisibility(View.VISIBLE);
                    } else {
                        tvHSNCode_out.setVisibility(View.GONE);
                    }
                } else { // no gst
                    tvHSNCode_out.setVisibility(View.GONE);
                }*/

                Cursor crssOtherChrg = dbBillScreen.getKOTModifierByModes(HomeDeliveryCaption);
                if (crssOtherChrg.moveToFirst()) {
                    do {
                        dOtherChrgs += crssOtherChrg.getDouble(crssOtherChrg.getColumnIndex("ModifierAmount"));
                    } while (crssOtherChrg.moveToNext());
                    txtOthercharges.setText(String.format("%.2f", dOtherChrgs));
                }
            }

            // Get bill number
            int iBillNumber = dbBillScreen.getNewBillNumbers();
            tvBillNumber.setText(String.valueOf(iBillNumber));



            /*chk_interstate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked == false) {
                        //et_pos.setBackgroundColor(Color.WHITE);
                        spnr_pos.setSelection(0);
                        spnr_pos.setEnabled(false);
                        tvSalesTax.setText("CGST Tax");
                        tvServiceTax.setText("SGST Tax");
                        float taxAmount = Float.parseFloat(tvTaxTotal.getText().toString());
                        if (taxAmount <= 0) {
                            tvTaxTotal.setText("0");
                            tvServiceTaxTotal.setText("0");
                        } else {
                            tvTaxTotal.setText(String.valueOf(taxAmount / 2));
                            tvServiceTaxTotal.setText(String.valueOf(taxAmount / 2));
                        }
                        tvServiceTaxTotal.setVisibility(View.VISIBLE);
                        tvServiceTax.setVisibility(View.VISIBLE);

                    } else {
                        // interstate
                        //et_pos.setBackground(Color.GRAY);
                        spnr_pos.setSelection(0);
                        spnr_pos.setEnabled(true);
                        tvSalesTax.setText("IGST Tax");
                        tvServiceTax.setText("");
                        float taxAmount = Float.parseFloat(tvTaxTotal.getText().toString());
                        if (taxAmount <= 0) {
                            tvTaxTotal.setText("0");
                            tvServiceTaxTotal.setText("");
                        } else {
                            tvTaxTotal.setText(String.valueOf(taxAmount * 2));
                            tvServiceTaxTotal.setText("");

                        }
                        tvServiceTaxTotal.setVisibility(View.INVISIBLE);
                        tvServiceTax.setVisibility(View.INVISIBLE);

                    }
                }
            });*/

            // Table Activity Starts
            tVLabelTableNo.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intentDineIn = new Intent(myContext, TableActivity.class);
                    intentDineIn.putExtra("BILLING_MODE", String.valueOf(jBillingMode));
                    intentDineIn.putExtra("USER_ID", strUserId);
                    intentDineIn.putExtra("USER_NAME", strUserName);
                    intentDineIn.putExtra("CUST_ID", 0);
                    startActivity(intentDineIn);

                    finish();
                }
            });
            tVLabelWaiterNo.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intentDineIn = new Intent(myContext, TableActivity.class);
                    intentDineIn.putExtra("BILLING_MODE", String.valueOf(jBillingMode));
                    intentDineIn.putExtra("USER_ID", strUserId);
                    intentDineIn.putExtra("USER_NAME", strUserName);
                    intentDineIn.putExtra("CUST_ID", 0);
                    startActivity(intentDineIn);

                    finish();
                }
            });
            // Table Activity Ends

            loadAutoCompleteData();


            /*btndepart.setVisibility(View.INVISIBLE);
            btncateg.setVisibility(View.INVISIBLE);
            btnitem.setVisibility(View.INVISIBLE);*/
            if (crsrSettings.getString(crsrSettings.getColumnIndex("FastBillingMode")).equalsIgnoreCase("1")) {
                btndepart.setVisibility(View.GONE);
                btncateg.setVisibility(View.GONE);
                btnitem.setVisibility(View.VISIBLE);
            } else if (crsrSettings.getString(crsrSettings.getColumnIndex("FastBillingMode")).equalsIgnoreCase("2")) {
                btndepart.setVisibility(View.VISIBLE);
                btncateg.setVisibility(View.GONE);
                btnitem.setVisibility(View.VISIBLE);
            } else if (crsrSettings.getString(crsrSettings.getColumnIndex("FastBillingMode")).equalsIgnoreCase("3")) {
                btndepart.setVisibility(View.VISIBLE);
                btncateg.setVisibility(View.VISIBLE);
                btnitem.setVisibility(View.VISIBLE);
            }

            btndepart.setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    lstvwDepartment.setVisibility(View.VISIBLE);
                    Cursor Departments = null;
                    // Get departments
                    Departments = dbBillScreen.getAllDepartments();
                    // Load departments to Department list
                    if (crsrSettings.getString(crsrSettings.getColumnIndex("FastBillingMode")).equalsIgnoreCase("3")) {
                        LoadDepartments(Departments);
                    } else if (crsrSettings.getString(crsrSettings.getColumnIndex("FastBillingMode")).equalsIgnoreCase("2")) {
                        LoadDepartmentsItems(Departments);
                    }
                    lstvwCategory.setAdapter(null);
                    grdItems.setAdapter(null);
                }
            });

            btncateg.setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    lstvwCategory.setVisibility(View.VISIBLE);
                    Cursor Category = null;
                    // Get Category
                    //Category = dbBillScreen.getCategories();
                    Category = dbBillScreen.getCategorybyDept();
                    // Load Category to Category List
                    LoadCategories(Category);
                    lstvwDepartment.setAdapter(null);
                    grdItems.setAdapter(null);

                }
            });

            btnitem.setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    grdItems.setVisibility(View.VISIBLE);
                    // Get Department items detail
                    if (crsrSettings.getString(crsrSettings.getColumnIndex("FastBillingMode")).equalsIgnoreCase("1")) {
                        grdItems.setNumColumns(6);
                        GetItemDetails();
                    } else {
                        //GetItemDetailswithoutDeptCateg();
                        GetItemDetails();
                    }
                    // This condition is to avoid NullReferenceException in getCount()
                    // in ImageAdapter class.
                    if (Name.length > 0) {
                        // Assign item grid to image adapter
                        grdItems.setAdapter(new ImageAdapter(myContext, Name, MenuCode, ImageUri, Byte.parseByte("1")));
                        // Make the item grid visible
                        // grdItems.setVisibility(View.VISIBLE);
                    } else {
                        // Make the item grid invisible
                        grdItems.setVisibility(View.INVISIBLE);
                    }
                    lstvwDepartment.setAdapter(null);
                    lstvwCategory.setAdapter(null);
                }
            });

            // Load Items without Dept and Categ
            if (crsrSettings.getString(crsrSettings.getColumnIndex("FastBillingMode")).equalsIgnoreCase("1")) {
                grdItems.setNumColumns(6);
                GetItemDetails();
                tvdeptline.setVisibility(View.GONE);
                tvcategline.setVisibility(View.GONE);
                lstvwDepartment.setVisibility(View.GONE);
                lstvwCategory.setVisibility(View.GONE);


            } else if (crsrSettings.getString(crsrSettings.getColumnIndex("FastBillingMode")).equalsIgnoreCase("2")) {
                grdItems.setNumColumns(4);
                //GetItemDetailswithoutDeptCateg();
                GetItemDetails();
                tvcategline.setVisibility(View.GONE);
                lstvwCategory.setVisibility(View.GONE);
            } else {
                //GetItemDetailswithoutDeptCateg();
                GetItemDetails();
            }

            if (Name.length > 0) {
                // Assign item grid to image adapter
                grdItems.setAdapter(new ImageAdapter(myContext, Name, MenuCode, ImageUri, Byte.parseByte("1")));
                // Make the item grid visible
                grdItems.setVisibility(View.VISIBLE);
            } else {
                // Make the item grid invisible
                grdItems.setVisibility(View.INVISIBLE);
            }

            // DINE IN BILLING
            if (jBillingMode == 1) {
                if (tvTableNumber.getText().toString().equalsIgnoreCase("") && tvTableSplitNo.getText().toString().equalsIgnoreCase("")) {
                    MsgBox.Show("", "No KOT's Found");
                } else {
//                            Integer.parseInt(tvSubUdfValue.getText().toString()), Integer.parseInt(tvTableSplitNo.getText().toString()));
//                    if (LoadKOT.moveToFirst()) {
//                        LoadKOTItems(LoadKOT);
//                    }
                }
            }

            // PICK UP & DELIVERY
            if (jBillingMode == Byte.parseByte("3") || jBillingMode == Byte.parseByte("4")) {
                crsrCustomerDetails = dbBillScreen.getCustomer(iCustId);
                if (crsrCustomerDetails == null || !crsrCustomerDetails.moveToFirst()) {
                    //MsgBox.Show("Warning", "Customer details not found");
                }

                Cursor BillItems = dbBillScreen.getKOTItems(iCustId, String.valueOf(jBillingMode));
                if (BillItems.moveToFirst()) {

                    LoadKOTItems(BillItems);
                    if (!strMakeOrder.equalsIgnoreCase("YES") && !strMakeOrder.equalsIgnoreCase("")) {
                        Tender();
                    }

                }
            }
        } catch (Exception e) {
            MsgBox.Show("Exception", e.getMessage());
            e.printStackTrace();
        }
    }

    /*************************************************************************************************************************************
     * Initializes all view handlers present in the billing screen with
     * appropriate controls
     ************************************************************************************************************************************/
    private void IntializeViewVariables() {

        Time = Calendar.getInstance();
        btndepart = (Button) findViewById(R.id.btn_depart);
        btncateg = (Button) findViewById(R.id.btn_categ);
        btnitem = (Button) findViewById(R.id.btn_item);
        tvdeptline = (TextView) findViewById(R.id.tvdeptline);
        tvcategline = (TextView) findViewById(R.id.tvcategline);
        //txtTotalItems = (EditText) findViewById(R.id.etTotalItems);
        tvHSNCode_out = (TextView) findViewById(R.id.tvColHSN);
        txtSearchItemBarcode = (EditText) findViewById(R.id.etSearchItemBarcode);
        txtSearchItemBarcode.setOnKeyListener(Item_Search_Barcode_KeyPressEvent);
        rowbtns = (LinearLayout) findViewById(R.id.rowbtns);
        rowtexts = (LinearLayout) findViewById(R.id.rowtexts);
        txtOthercharges = (TextView) findViewById(R.id.txtOthercharges);
        edtCustId = (EditText) findViewById(R.id.edtCustId);
        edtCustName = (EditText) findViewById(R.id.edtCustName);
        edtCustPhoneNo = (EditText) findViewById(R.id.edtCustPhoneNo);
        edtCustDineInPhoneNo = (EditText) findViewById(R.id.edtCustDineInPhoneNo);
        etCustGSTIN = (EditText) findViewById(R.id.etCustGSTIN);
        edtCustAddress = (EditText) findViewById(R.id.edtCustAddress);
        btnAddCustomer = (Button) findViewById(R.id.btn_DineInAddCustomer);
        btn_item_fastBillingMode = (Button) findViewById(R.id.btn_item_fastBillingMode);
        edtCustPhoneNo.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                try {
                    if (edtCustPhoneNo.getText().toString().length() == 10) {
                        Cursor crsrCust = dbBillScreen.getCustomer(edtCustPhoneNo.getText().toString());
                        if (crsrCust.moveToFirst()) {
                            //if(crsrCust.getString(crsrCust.getColumnIndex("CustContactNumber")) == edtCustPhoneNo.getText().toString()) {
                            edtCustId.setText(crsrCust.getString(crsrCust.getColumnIndex("CustId")));
                            edtCustName.setText(crsrCust.getString(crsrCust.getColumnIndex("CustName")));
                            //edtCustPhoneNo.setText(crsrCust.getString(crsrCust.getColumnIndex("CustContactNumber")));
                            edtCustAddress.setText(crsrCust.getString(crsrCust.getColumnIndex("CustAddress")));
                            String gstin = crsrCust.getString(crsrCust.getColumnIndex("GSTIN"));
                            if (gstin == null)
                                etCustGSTIN.setText("");
                            else
                                etCustGSTIN.setText(gstin);
//                            MsgBox.Show("", "Already Customer Exists");
                            btnAddCustomer.setVisibility(View.INVISIBLE);
                            ControlsSetEnabled();
                            if(jBillingMode!=2) {
                                btnPrintBill.setEnabled(false);
                                btnPayBill.setEnabled(false);
                            }
                            else
                            {
                                btnPrintBill.setEnabled(true);
                                btnPayBill.setEnabled(true);
                            }
                            //}
                        } else {
                            MsgBox.Show("", "Customer is not Found, Please Add Customer before Order");
                            btnAddCustomer.setVisibility(View.VISIBLE);
                            ControlsSetDisabled();
                        }
                    } else {

                    }
                } catch (Exception ex) {
                    MsgBox.Show("Error", ex.getMessage());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        edtCustDineInPhoneNo.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                try {
                    if (edtCustDineInPhoneNo.getText().toString().length() == 10) {
                        Cursor crsrCust = dbBillScreen.getCustomer(edtCustDineInPhoneNo.getText().toString());
                        if (crsrCust.moveToFirst()) {
                            //if(crsrCust.getString(crsrCust.getColumnIndex("CustContactNumber")) == edtCustPhoneNo.getText().toString()) {
                            edtCustId.setText(crsrCust.getString(crsrCust.getColumnIndex("CustId")));
                            edtCustName.setText(crsrCust.getString(crsrCust.getColumnIndex("CustName")));
                            //edtCustPhoneNo.setText(crsrCust.getString(crsrCust.getColumnIndex("CustContactNumber")));
                            edtCustAddress.setText(crsrCust.getString(crsrCust.getColumnIndex("CustAddress")));
                            /*String gstin = crsrCust.getString(crsrCust.getColumnIndex("CustAddress"));
                            if (gstin==null)
                                etCustGSTIN.setText("");
                            else
                                etCustGSTIN.setText(gstin);*/
                            Toast.makeText(myContext, "Customer Fetched Successfully", Toast.LENGTH_LONG).show();
                            //}
                        } else {
                            MsgBox.Show("", "Customer is not Found, Please Add Customer before Order");
                        }
                    } else {

                    }
                } catch (Exception ex) {
                    MsgBox.Show("Error", ex.getMessage());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        idd_date  = (LinearLayout) findViewById(R.id.idd_date);

        tVLabelTableNo = (TextView) findViewById(R.id.textViewLabelTableNo);
        tVLabelWaiterNo = (TextView) findViewById(R.id.textViewLabelWaiterNo);
        tVLabelOrderNo = (TextView) findViewById(R.id.textViewLabelOrderNo);
        relative_Interstate = (RelativeLayout) findViewById(R.id.relative_interstate);
        relative_pos = (RelativeLayout) findViewById(R.id.relative_pos);
        relative_waiterno = (RelativeLayout) findViewById(R.id.relative_waiterno);
        relative_tableNo = (RelativeLayout) findViewById(R.id.relative_tableNo);
        // et_pos = (EditText) findViewById(R.id.etpos);
        chk_interstate = (CheckBox) findViewById(R.id.checkbox_interstate);
        //tvUserName = (TextView) findViewById(R.id.tvBillUserName);
        tvDate = (TextView) findViewById(R.id.tvBillDateValue);
        tvBillNumber = (EditText) findViewById(R.id.tvBillNumberValue);
        tvBillNumber.setGravity(Gravity.CENTER);
        //tvSubUdfName = (TextView) findViewById(R.id.tvSubUdfText);
        tvSubUdfValue = (EditText) findViewById(R.id.tvSubUdfValue);
        tvTableSplitNo = (EditText) findViewById(R.id.tvTableSplitNoValue);
        tvTableSplitNo.setGravity(Gravity.CENTER);
        tvWaiterName = (EditText) findViewById(R.id.tvWaiterName);
        tvWaiterNumber = (EditText) findViewById(R.id.tvWaiterNoValue);
        tvWaiterName.setGravity(Gravity.CENTER);
        tvTableNumber = (EditText) findViewById(R.id.tvTableNoValue);
        tvTableNumber.setGravity(Gravity.CENTER);
        tvSubTotal = (TextView) findViewById(R.id.tvSubTotalValue);
        tvTaxTotal = (TextView) findViewById(R.id.tvTaxTotalValue);
        tvServiceTaxTotal = (TextView) findViewById(R.id.tvServiceTaxValue);
        tvBillAmount = (TextView) findViewById(R.id.tvBillTotalValue);

        lstvwDepartment = (ListView) findViewById(R.id.lstDepartmentNames);
        lstvwCategory = (ListView) findViewById(R.id.lstCategoryNames);
        grdItems = (GridView) findViewById(R.id.gridItems);
        grdItems.setOnItemClickListener(GridItemImageClick);

        //btnPayCash = (Button) findViewById(R.id.btn_PayCash);
        btnSplitBill = (WepButton) findViewById(R.id.btn_SplitBill);
        btnDeleteBill = (WepButton) findViewById(R.id.btn_DeleteBill);
        btnPayBill = (WepButton) findViewById(R.id.btn_PayBill);
        btnShiftTable = (WepButton) findViewById(R.id.btn_ShiftTable);
        btnMergeTable = (WepButton) findViewById(R.id.btn_MergeTable);
        btnModifier = (WepButton) findViewById(R.id.btn_Modifier);
        btn_ReprintKOT = (WepButton) findViewById(R.id.btn_ReprintKOT);
        btnSaveKOT = (WepButton) findViewById(R.id.btn_SaveKOT);
        //btnTable = (Button) findViewById(R.id.btn_TableNo);
        //btnWaiter = (Button) findViewById(R.id.btn_WaiterNo);
        btnLoadKOT = (WepButton) findViewById(R.id.btn_LoadKOT);
        btnDeleteKOT = (WepButton) findViewById(R.id.btn_DeleteKOT);
        btnKOTStatus = (WepButton) findViewById(R.id.btn_KOTStatus);
        btnPrintKOT = (WepButton) findViewById(R.id.btn_PrintKOT);
        btnPrintBill = (WepButton) findViewById(R.id.btn_PrintBill);
        btnClear = (WepButton) findViewById(R.id.btn_Clear);
        btnReprint = (WepButton) findViewById(R.id.btn_Reprint);
        tblOrderItems = (TableLayout) findViewById(R.id.tblOrderItems);

        aTViewSearchItem = (AutoCompleteTextView) findViewById(R.id.aCTVSearchItem);
        aTViewSearchItem.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    /*Toast.makeText(BillingScreenActivity_Unused.this, aTViewSearchItem.getText().toString(),
                            Toast.LENGTH_SHORT).show();*/
                    if ((aTViewSearchItem.getText().toString().equals(""))) {
                        MsgBox.Show("Warning", "Enter Item Name");
                    } else {
                        Cursor MenucodeItem = dbBillScreen.getItemList(aTViewSearchItem.getText().toString().trim());
                        if (MenucodeItem.moveToFirst()) {
                            btnClear.setEnabled(true);
                            AddItemToOrderTable(MenucodeItem);
                            aTViewSearchItem.setText("");
                            // ((EditText)v).setText("");
                        } else {
                            MsgBox.Show("Warning", "Item not found for Selected Item");
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        spnr_pos = (Spinner) findViewById(R.id.spnr_pos);

        aTViewSearchMenuCode = (AutoCompleteTextView) findViewById(R.id.aCTVSearchMenuCode);
        aTViewSearchMenuCode.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    /*Toast.makeText(BillingScreenActivity_Unused.this, aTViewSearchMenuCode.getText().toString(),
                            Toast.LENGTH_SHORT).show();*/
                    if ((aTViewSearchMenuCode.getText().toString().equals(""))) {
                        MsgBox.Show("Warning", "Enter Menu Code");
                    } else {
                        Cursor MenucodeItem = dbBillScreen
                                .getItem(Integer.parseInt(aTViewSearchMenuCode.getText().toString().trim()));
                        if (MenucodeItem.moveToFirst()) {
                            btnClear.setEnabled(true);
                            AddItemToOrderTable(MenucodeItem);
                            aTViewSearchMenuCode.setText("");
                            // ((EditText)v).setText("");
                        } else {
                            MsgBox.Show("Warning", "Item not found for Selected Item Code");
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnLoadKOT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadKOT(v);
            }
        });
        btnModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyKOT(v);
            }
        });
        btn_ReprintKOT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReprintKOT(v);
            }
        });
        btnDeleteKOT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoidKOT(v);
            }
        });
        btnShiftTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShiftTable(v);
            }
        });
        btnMergeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MergeTable(v);
            }
        });
        btnSaveKOT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveKOT(v);
            }
        });
        btnPrintKOT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printKOT(v);
            }
        });
        btnPrintBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printBILL(v);
            }
        });
        btnPayBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tender(v);
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tender(v);
            }
        });
        btnDeleteBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteBill(v);
            }
        });
        btnReprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReprintBill(v);
            }
        });
        btnKOTStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KOTStatus(v);
            }
        });

    }

    /*************************************************************************************************************************************
     * Department list click listener which loads the items of selected
     * department to Category list
     *************************************************************************************************************************************/
    OnItemClickListener DepartmentListClick = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

            lstvwCategory.setVisibility(View.VISIBLE);
            Cursor Category = dbBillScreen.getCatItems(position + 1);
            // Load Category to Category List
            LoadCategories(Category);
        }

    };

    /*************************************************************************************************************************************
     * Category list click listener which loads the items of selected Category
     * to item grid
     *************************************************************************************************************************************/
    OnItemClickListener CategoryListClick = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
            // TODO Auto-generated method stub
            GetItemDetails(position + 1);
            // This condition is to avoid NullReferenceException in getCount()
            // in ImageAdapter class.
            if (Name.length > 0) {
                // Assign item grid to image adapter
                grdItems.setAdapter(new ImageAdapter(myContext, Name, MenuCode, ImageUri, Byte.parseByte("1")));
                // Make the item grid visible
                grdItems.setVisibility(View.VISIBLE);
            } else {
                // Make the item grid invisible
                grdItems.setVisibility(View.INVISIBLE);
            }
        }

    };

    /*************************************************************************************************************************************
     * Item grid image click listener which loads the item to billing grid if
     * selected item is not present otherwise increments the quantity by one
     *************************************************************************************************************************************/
    OnItemClickListener GridItemImageClick = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
            Cursor Item = null;
            if (v.getTag() != null)
            {
                if (jBillingMode == Byte.parseByte("1"))
                {
                    if (tvTableNumber.getText().toString().equalsIgnoreCase("")
                            || tvWaiterNumber.getText().toString().equalsIgnoreCase("")
                            || tvTableNumber.getText().toString().equalsIgnoreCase("0")
                            || tvWaiterNumber.getText().toString().equalsIgnoreCase("0"))
                    {
                        MsgBox.Show("Warning", "Select waiter and table before adding item to bill");
                        //Table_Waiter(null);
                        Intent intentDineIn = new Intent(myContext, TableActivity.class);
                        intentDineIn.putExtra("BILLING_MODE", String.valueOf(jBillingMode));
                        intentDineIn.putExtra("USER_ID", strUserId);//spUser.getString("USER_ID", "GHOST"));
                        intentDineIn.putExtra("USER_NAME", strUserName);//spUser.getString("USER_NAME", "GHOST"));
                        intentDineIn.putExtra("CUST_ID", 0);
                        startActivity(intentDineIn);
                        finish();
                        return;
                    }
                    else
                    {
                        Item = dbBillScreen.getItem(Integer.parseInt(v.getTag().toString()));
                        btnClear.setEnabled(true);
                        AddItemToOrderTable(Item);
                    }
                }
                else if (jBillingMode == Byte.parseByte("4"))
                {
                    btnPrintBill.setEnabled(false);
                    btnPayBill.setEnabled(false);
                    Item = dbBillScreen.getItem(Integer.parseInt(v.getTag().toString()));
                    btnClear.setEnabled(true);
                    AddItemToOrderTable(Item);
                }
                else
                {
                    Item = dbBillScreen.getItem(Integer.parseInt(v.getTag().toString()));
                    btnClear.setEnabled(true);
                    AddItemToOrderTable(Item);
                }
            }
        }
    };

    /*************************************************************************************************************************************
     * Modifier list click listener which adds the modifier amount to selected
     * item from the billing table
     *************************************************************************************************************************************/
    OnItemClickListener ModifierListClick = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
            // TODO Auto-generated method stub
            Cursor crsrModifier = null;
            crsrModifier = dbBillScreen.getKOTModifier(position + 1);

            AddModifier(crsrModifier);

        }

    };

    /*************************************************************************************************************************************
     * Menu code item search key-press / key-listener handler which loads the
     * item to billing grid if selected item is not present otherwise increments
     * the quantity by one
     *************************************************************************************************************************************/
    OnKeyListener Item_Search_MenuCode_KeyPressEvent = new OnKeyListener() {

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // TODO Auto-generated method stub

            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(((EditText) v).getWindowToken(), 0);

                if (((EditText) v).getText().toString().equalsIgnoreCase("")) {
                    MsgBox.Show("Warning", "Enter item menu code");
                } else {
                    Cursor MenucodeItem = dbBillScreen.getItem(Integer.parseInt(((EditText) v).getText().toString()));
                    if (MenucodeItem.moveToFirst()) {
                        btnClear.setEnabled(true);
                        AddItemToOrderTable(MenucodeItem);
                        // ((EditText)v).setText("");
                        return true;
                    } else {
                        MsgBox.Show("Warning", "Item not found for given menu code");
                    }
                }
            }
            // ((EditText)v).setText("");
            return false;
        }
    };

    /*************************************************************************************************************************************
     * Item item search key-press / key-listener handler which loads the item to
     * billing grid if selected item is not present otherwise increments the
     * quantity by one
     *************************************************************************************************************************************/
    OnKeyListener Item_Search_Itemname_ClickEvent = new OnKeyListener() {

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // TODO Auto-generated method stub

            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(((EditText) v).getWindowToken(), 0);

                if (((EditText) v).getText().toString().equalsIgnoreCase("")) {
                    MsgBox.Show("Warning", "Enter item menu code");
                } else {
                    Cursor MenucodeItem = dbBillScreen.getItem(Integer.parseInt(((EditText) v).getText().toString()));
                    if (MenucodeItem.moveToFirst()) {
                        btnClear.setEnabled(true);
                        AddItemToOrderTable(MenucodeItem);
                        // ((EditText)v).setText("");
                        return true;
                    } else {
                        MsgBox.Show("Warning", "Item not found for given menu code");
                    }
                }
            }
            // ((EditText)v).setText("");
            return false;
        }
    };

    /*************************************************************************************************************************************
     * Bar-code item search key-press / key-listener handler which loads the
     * item to billing grid if selected item is not present otherwise increments
     * the quantity by one
     ************************************************************************************************************************************/
    OnKeyListener Item_Search_Barcode_KeyPressEvent = new OnKeyListener() {

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // TODO Auto-generated method stub

            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(((EditText) v).getWindowToken(), 0);

                if (((EditText) v).getText().toString().equalsIgnoreCase("")) {
                    MsgBox.Show("Warning", "Scan item barcode");
                } else {
                    Cursor BarcodeItem = dbBillScreen.getItem(((EditText) v).getText().toString());
                    if (BarcodeItem.moveToFirst()) {
                        btnClear.setEnabled(true);
                        AddItemToOrderTable(BarcodeItem);
                        // ((EditText)v).setText("");
                        return true;
                    } else {
                        MsgBox.Show("Warning", "Item not found for the above barcode");
                    }
                }
            }
            // ((EditText)v).setText("");
            return false;
        }
    };

    /*************************************************************************************************************************************
     * Quantity / Rate column text box click event listener which selects all
     * the text present in text box
     *************************************************************************************************************************************/
    OnClickListener Qty_Rate_Click = new OnClickListener() {

        public void onClick(View v) {
            // TODO Auto-generated method stub
            ((EditText) v).setSelection(0, ((EditText) v).getText().length());
        }

    };

    /*************************************************************************************************************************************
     * Quantity / Rate column text key press event listener
     *************************************************************************************************************************************/
    OnKeyListener Qty_Rate_KeyPressEvent = new OnKeyListener() {

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // TODO Auto-generated method stub

            if (v.getTag().toString().equalsIgnoreCase("QTY_RATE")) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(((EditText) v).getWindowToken(), 0);

                    Qty_Rate_Edit();
                    // return true;
                }
                // else { return false; }
            }
            return false;
        }
    };

    /*************************************************************************************************************************************
     * Loads all the departments to department list
     *
     * @param crsrDept : Cursor containing all the departments from list
     *************************************************************************************************************************************/
    private void LoadDepartments(Cursor crsrDept) {

        Cursor cursor = dbBillScreen.getDepartments();
        String columns[] = new String[]{"_id", "DeptName"};
        int vals[] = new int[]{R.id.tvlstDeptCode, R.id.tvlstDeptName};
        deptdataAdapter = new SimpleCursorAdapter(this, R.layout.activity_deptnames, cursor, columns, vals);

        lstvwDepartment.setVisibility(View.VISIBLE);
        lstvwDepartment.setAdapter(deptdataAdapter);
        lstvwDepartment.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                @SuppressWarnings("unchecked")
                String deptcode = ((TextView) view.findViewById(R.id.tvlstDeptCode)).getText().toString();

                Cursor Category = dbBillScreen.getCategoryItems(Integer.valueOf(deptcode));
                // Load Category to Category List
                if (Category.moveToFirst()) {
                    lstvwCategory.setVisibility(View.VISIBLE);
                    LoadCategories(Category);
                    //grdItems.setAdapter(null);
                    GetItemDetailsByDept(Integer.valueOf(deptcode));
                    if (Name.length > 0) {
                        // Assign item grid to image adapter
                        grdItems.setAdapter(new ImageAdapter(myContext, Name, MenuCode, ImageUri, Byte.parseByte("1")));
                        // Make the item grid visible
                        grdItems.setVisibility(View.VISIBLE);
                    } else {
                        // Make the item grid invisible
                        grdItems.setVisibility(View.INVISIBLE);
                    }
                } else {
                    lstvwCategory.setAdapter(null);
                    //MsgBox.Show("","Items");
                    GetItemDetailsByDept(Integer.valueOf(deptcode));//, Integer.valueOf(categdeptcode));
                    // This condition is to avoid NullReferenceException in getCount()
                    // in ImageAdapter class.
                    if (Name.length > 0) {
                        // Assign item grid to image adapter
                        grdItems.setAdapter(new ImageAdapter(myContext, Name, MenuCode, ImageUri, Byte.parseByte("1")));
                        // Make the item grid visible
                        grdItems.setVisibility(View.VISIBLE);
                    } else {
                        // Make the item grid invisible
                        grdItems.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    private void LoadDepartmentsItems(Cursor crsrDept) {

        Cursor cursor = dbBillScreen.getDepartments();
        String columns[] = new String[]{"_id", "DeptName"};
        int vals[] = new int[]{R.id.tvlstDeptCode, R.id.tvlstDeptName};
        deptdataAdapter = new SimpleCursorAdapter(this, R.layout.activity_deptnames, cursor, columns, vals);

        lstvwDepartment.setAdapter(deptdataAdapter);
        lstvwDepartment.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                @SuppressWarnings("unchecked")
                //String val = o.get("id");
                        String deptcode = ((TextView) view.findViewById(R.id.tvlstDeptCode)).getText().toString();

                lstvwCategory.setAdapter(null);
                //MsgBox.Show("","Items");
                GetItemDetailsByDept(Integer.valueOf(deptcode));//, Integer.valueOf(categdeptcode));
                // This condition is to avoid NullReferenceException in getCount()
                // in ImageAdapter class.
                if (Name.length > 0) {
                    // Assign item grid to image adapter
                    grdItems.setAdapter(new ImageAdapter(myContext, Name, MenuCode, ImageUri, Byte.parseByte("1")));
                    // Make the item grid visible
                    grdItems.setVisibility(View.VISIBLE);
                } else {
                    // Make the item grid invisible
                    grdItems.setVisibility(View.INVISIBLE);
                }

            }
        });
    }

    private void LoadCategories(Cursor crsrCateg) {

        //Cursor cursor = crsrCateg;
        String columns[] = new String[]{"_id", "CategName", "DeptCode"};
        int vals[] = new int[]{R.id.tvlstCategCode, R.id.tvlstCategName, R.id.tvlstCategDeptCode};
        categdataAdapter = new SimpleCursorAdapter(this, R.layout.activity_categnames, crsrCateg, columns, vals);

        lstvwCategory.setAdapter(categdataAdapter);
        lstvwCategory.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                @SuppressWarnings("unchecked")

                String categcode = ((TextView) view.findViewById(R.id.tvlstCategCode)).getText().toString();
                String categdeptcode = ((TextView) view.findViewById(R.id.tvlstCategDeptCode)).getText().toString();

//                Toast.makeText(myContext, message, Toast.LENGTH_LONG).show();
                grdItems.setVisibility(View.VISIBLE);
                GetItemDetails(Integer.valueOf(categcode));//, Integer.valueOf(categdeptcode));
                // This condition is to avoid NullReferenceException in getCount()
                // in ImageAdapter class.
                if (Name.length > 0) {
                    // Assign item grid to image adapter
                    grdItems.setAdapter(new ImageAdapter(myContext, Name, MenuCode, ImageUri, Byte.parseByte("1")));
                    // Make the item grid visible
                    grdItems.setVisibility(View.VISIBLE);
                } else {
                    // Make the item grid invisible
                    grdItems.setVisibility(View.INVISIBLE);
                }

            }
        });
    }

    /*************************************************************************************************************************************
     * Loads all the KOT modifiers to list
     *************************************************************************************************************************************/
    private void LoadKOTModifiers() {
        Cursor crsrKOTModifiers = null;
        crsrKOTModifiers = dbBillScreen.getAllKOTModifier();
        if (crsrKOTModifiers.moveToFirst()) {

            adapModifiers = new ArrayAdapter<String>(myContext, android.R.layout.simple_list_item_multiple_choice);
            lstvwKOTModifiers.setAdapter(adapModifiers);
            lstvwKOTModifiers.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

            do {
                adapModifiers.add(crsrKOTModifiers.getString(crsrKOTModifiers.getColumnIndex("ModifierDescription")));

            } while (crsrKOTModifiers.moveToNext());

        } else {
            MsgBox.Show("Warning", "No KOT Modifiers found");
        }
    }

    /*************************************************************************************************************************************
     * Retrieves Item details such as MenuCode, Name, ImageUri to display in
     * Item image grid
     *
     * @param : Item details will be returned based on Department Code
     *          parameter
     ************************************************************************************************************************************/
    // Get Items by CategCode
    private void GetItemDetails() {
        Cursor Items = null;
        Items = dbBillScreen.getAllItems();
        //Items = dbBillScreen.getAllItemsWithoutDeptCateg();
        if (Items.moveToFirst()) {

            Name = new String[Items.getCount()];
            ImageUri = new String[Items.getCount()];
            MenuCode = new int[Items.getCount()];

            do {
                MenuCode[Items.getPosition()] = Items.getInt(Items.getColumnIndex("MenuCode"));
                Name[Items.getPosition()] = Items.getString(Items.getColumnIndex("ItemName"));
                ImageUri[Items.getPosition()] = Items.getString(Items.getColumnIndex("ImageUri"));
            } while (Items.moveToNext());

        } else {
            Name = new String[0];
            ImageUri = new String[0];
            MenuCode = new int[0];
        }
    }

    private void GetItemDetailswithoutDeptCateg() {
        Cursor Items = null;
        //Items = dbBillScreen.getAllItems();
        Items = dbBillScreen.getAllItemsWithoutDeptCateg();
        if (Items.moveToFirst()) {

            Name = new String[Items.getCount()];
            ImageUri = new String[Items.getCount()];
            MenuCode = new int[Items.getCount()];

            do {
                MenuCode[Items.getPosition()] = Items.getInt(Items.getColumnIndex("MenuCode"));
                Name[Items.getPosition()] = Items.getString(Items.getColumnIndex("ItemName"));
                ImageUri[Items.getPosition()] = Items.getString(Items.getColumnIndex("ImageUri"));
            } while (Items.moveToNext());

        } else {
            Name = new String[0];
            ImageUri = new String[0];
            MenuCode = new int[0];
        }
    }

    // Get Items by CategCode
    private void GetItemDetails(int iCategCode) {
        Cursor Items = null;
        Items = dbBillScreen.getCatbyItems(iCategCode);
        if (Items.moveToFirst()) {

            Name = new String[Items.getCount()];
            ImageUri = new String[Items.getCount()];
            MenuCode = new int[Items.getCount()];

            do {
                MenuCode[Items.getPosition()] = Items.getInt(Items.getColumnIndex("MenuCode"));
                Name[Items.getPosition()] = Items.getString(Items.getColumnIndex("ItemName"));
                ImageUri[Items.getPosition()] = Items.getString(Items.getColumnIndex("ImageUri"));
            } while (Items.moveToNext());

        } else {

            Log.d("LoadItemsToGrid", "No Items found for Category " + iCategCode);

            Name = new String[0];
            ImageUri = new String[0];
            MenuCode = new int[0];
        }
    }

    // Get Items by DeptCode
    private void GetItemDetailsByDept(int iDeptCode) {
        Cursor Items = null;
        Items = dbBillScreen.getItems(iDeptCode);
        if (Items.moveToFirst()) {

            Name = new String[Items.getCount()];
            ImageUri = new String[Items.getCount()];
            MenuCode = new int[Items.getCount()];

            do {
                MenuCode[Items.getPosition()] = Items.getInt(Items.getColumnIndex("MenuCode"));
                Name[Items.getPosition()] = Items.getString(Items.getColumnIndex("ItemName"));
                ImageUri[Items.getPosition()] = Items.getString(Items.getColumnIndex("ImageUri"));
            } while (Items.moveToNext());

        } else {

            Log.d("LoadItemsToGrid", "No Items found");

            Name = new String[0];
            ImageUri = new String[0];
            MenuCode = new int[0];
        }
    }

    /*************************************************************************************************************************************
     * Adds the item to billing table from the cursor
     *
     * @param crsrItem : Cursor which contains the item details
     *************************************************************************************************************************************/
    @SuppressWarnings("deprecation")
    private void AddItemToOrderTable(Cursor crsrItem) {

        EditTextInputHandler etInputValidate = new EditTextInputHandler();
        String strQty = "0";
        double dRate = 0, dTaxPercent = 0, dDiscPercent = 0, dTaxAmt = 0, dDiscAmt = 0, dTempAmt = 0, dServiceTaxPercent = 0;
        double dServiceTaxAmt = 0;
        int iTaxId = 0, iServiceTaxId = 0, iDiscId = 0;
        boolean bItemExists = false;

        TableRow rowItem = null;
        Cursor crsrTax, crsrDiscount;

        TextView tvName, tvAmount, tvTaxPercent, tvTaxAmt, tvDiscPercent, tvDiscAmt, tvDeptCode, tvCategCode,
                tvKitchenCode, tvTaxType, tvModifierCharge, tvServiceTaxPercent, tvServiceTaxAmt;
        EditText etQty, etRate;
        TextView tvHSn;
        CheckBox chkNumber;
        crsrSettings = dbBillScreen.getBillSetting();

        TextView HSNCode;
        // If item is present in cursor
        if (crsrItem.moveToFirst() && crsrSettings.moveToFirst()) {

            // If item is stock enabled and stock quantity is zero, return
            // without adding item

            do {
                // Get service tax percentage
                tvServiceTaxPercent = new TextView(myContext);
                if (iTaxType == 1) {
                    /*iTaxId = crsrItem.getInt(crsrItem.getColumnIndex("AdditionalTaxId"));
                    crsrTax = dbBillScreen.getTaxConfig(iTaxId);
                    if (crsrTax.moveToFirst()) {
                        tvServiceTaxPercent.setText(crsrTax.getString(crsrTax.getColumnIndex("TotalPercentage")));
                    } else {
                        tvServiceTaxPercent.setText("0");
                    }*/
                    tvServiceTaxPercent.setText(crsrItem.getString(crsrItem.getColumnIndex("ServiceTaxPercent")));
                } else {
                    tvServiceTaxPercent.setText("0");
                }

                // Check for the item in Order table, if present update quantity
                // and amounts
                for (int iRow = 0; iRow < tblOrderItems.getChildCount(); iRow++) {

                    TableRow Row = (TableRow) tblOrderItems.getChildAt(iRow);

                    // Check against item number present in table
                    if (Row.getChildAt(0) != null) {

                        CheckBox Number = (CheckBox) Row.getChildAt(0);
                        TextView ItemName = (TextView) Row.getChildAt(1);
                        TextView PrintKOTStatus = (TextView) Row.getChildAt(21);

                        // Check for item number and name, if name is not same
                        // add new
                        if (Number.getText().toString()
                                .equalsIgnoreCase(crsrItem.getString(crsrItem.getColumnIndex("MenuCode")))
                                && ItemName.getText().toString()
                                .equalsIgnoreCase(crsrItem.getString(crsrItem.getColumnIndex("ItemName"))
                                )) {
                            if (PrintKOTStatus.getText().toString().equalsIgnoreCase("0")) {
                                EditText Qty = (EditText) Row.getChildAt(3);
                                Qty.setEnabled(false);
                            } else {
                                // Quantity
                                EditText Qty = (EditText) Row.getChildAt(3);
                                Qty.setSelectAllOnFocus(true);
                                strQty = Qty.getText().toString().equalsIgnoreCase("") ? "0" : Qty.getText().toString(); // Temp

                                int BillwithStock = crsrSettings.getInt(crsrSettings.getColumnIndex("BillwithStock"));
                                if (BillwithStock == 1) {
                                    String availableqty = crsrItem.getString(crsrItem.getColumnIndex("Quantity"));
                                    if (crsrItem.getFloat(crsrItem.getColumnIndex("Quantity")) < (Float.valueOf(strQty) + 1)) {
                                        MsgBox.Show("Warning", "Stock is less, present stock quantity is "
                                                + availableqty);
                                        Qty.setText(String.format("%.2f", Double.parseDouble(availableqty)) );
                                        return;
                                    } else {
                                        Qty.setText(String.format("%.2f", Double.parseDouble(strQty) + 1));
                                    }
                                } else {
                                    Qty.setText(String.format("%.2f", Double.parseDouble(strQty) + 1));
                                }

                                // Amount
                                EditText Rate = (EditText) Row.getChildAt(4);
                                Rate.setSelectAllOnFocus(true);
                                TextView Amount = (TextView) Row.getChildAt(5);
                                dRate = Double.parseDouble(
                                        Rate.getText().toString().equalsIgnoreCase("") ? "0" : Rate.getText().toString()); // Temp
                                Amount.setText(
                                        String.format("%.2f", (Double.parseDouble(Qty.getText().toString()) * dRate)));

                                // Tax and Discount Amount
                                TextView TaxPer = (TextView) Row.getChildAt(6);
                                TextView TaxAmt = (TextView) Row.getChildAt(7);
                                TextView DiscPer = (TextView) Row.getChildAt(8);
                                TextView DiscAmt = (TextView) Row.getChildAt(9);
                                TextView ServiceTax = (TextView) Row.getChildAt(15);
                                TextView ServiceTaxAmt = (TextView) Row.getChildAt(16);
                                // TextView Delete = (TextView) Row.getChildAt(16);

                                dTaxPercent = Double.parseDouble(TaxPer.getText().toString().equalsIgnoreCase("") ? "0"
                                        : TaxPer.getText().toString()); // Temp
                                dDiscPercent = Double.parseDouble(DiscPer.getText().toString().equalsIgnoreCase("") ? "0"
                                        : DiscPer.getText().toString()); // Temp

                                if (crsrSettings.getInt(crsrSettings.getColumnIndex("Tax")) == 1) {
                                    // Discount
                                    dDiscAmt = dRate * (dDiscPercent / 100);
                                    dTempAmt = dDiscAmt;
                                    dDiscAmt = dDiscAmt * Double.parseDouble(Qty.getText().toString());

                                    // Tax
                                    dTaxAmt = (dRate - dTempAmt) * (dTaxPercent / 100);
                                    dTaxAmt = dTaxAmt * Double.parseDouble(Qty.getText().toString());

                                    TaxAmt.setText(String.format("%.2f", dTaxAmt));
                                    DiscAmt.setText(String.format("%.2f", dDiscAmt));

                                } else {
                                    double dBasePrice = 0;
                                    dBasePrice = dRate / (1 + (dTaxPercent / 100));

                                    // Discount
                                    dDiscAmt = dBasePrice * (dDiscPercent / 100);
                                    dTempAmt = dDiscAmt;
                                    dDiscAmt = dDiscAmt * Double.parseDouble(Qty.getText().toString());

                                    // Tax
                                    dTaxAmt = (dBasePrice - dTempAmt) * (dTaxPercent / 100);
                                    dTaxAmt = dTaxAmt * Double.parseDouble(Qty.getText().toString());

                                    TaxAmt.setText(String.format("%.2f", dTaxAmt));
                                    DiscAmt.setText(String.format("%.2f", dDiscAmt));
                                }

                                // Service Tax charge
                                dTaxAmt = dRate * (Double.parseDouble(ServiceTax.getText().toString()) / 100);
                                ServiceTaxAmt.setText(
                                        String.format("%.2f", (Double.parseDouble(Qty.getText().toString()) * dTaxAmt)));

                                // // delete
                                // Delete.setText("Hi");

                                // Clear all variables and set ItemExists to TRUE
                                // and break from the loop
                                dRate = 0;
                                dTaxPercent = 0;
                                dDiscPercent = 0;
                                dTaxAmt = 0;
                                dDiscAmt = 0;
                                dTempAmt = 0;
                                bItemExists = true;

                                break;
                            }


                        }
                    }
                }


                if (bItemExists == false) {

                    rowItem = new TableRow(myContext);
                    rowItem.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                    //crsrSettings = dbBillScreen.getBillSetting();

                    int DineInRate = crsrSettings.getInt(crsrSettings.getColumnIndex("DineInRate"));
                    int CounterSalesRate = crsrSettings.getInt(crsrSettings.getColumnIndex("CounterSalesRate"));
                    int PickUpRate = crsrSettings.getInt(crsrSettings.getColumnIndex("PickUpRate"));
                    int HomeDeliveryRate = crsrSettings.getInt(crsrSettings.getColumnIndex("HomeDeliveryRate"));
                    if (GSTEnable!=null && GSTEnable.equalsIgnoreCase("1")) {
                        //int rate_dummy = crsrItem.getString(crsrItem.getColumnIndex("Rate"));
                        dRate = crsrItem.getInt(crsrItem.getColumnIndex("Rate"));
                    } else { // gst disable
                        if (jBillingMode == 1) {
                            if (DineInRate == 1) {
                                dRate = crsrItem.getInt(crsrItem.getColumnIndex("DineInPrice1"));
                            } else if (DineInRate == 2) {
                                dRate = crsrItem.getInt(crsrItem.getColumnIndex("DineInPrice2"));
                            } else if (DineInRate == 3) {
                                dRate = crsrItem.getInt(crsrItem.getColumnIndex("DineInPrice3"));
                            }
                        }
                        if (jBillingMode == 2) {
                            if (CounterSalesRate == 1) {
                                dRate = crsrItem.getInt(crsrItem.getColumnIndex("DineInPrice1"));
                            } else if (CounterSalesRate == 2) {
                                dRate = crsrItem.getInt(crsrItem.getColumnIndex("DineInPrice2"));
                            } else if (CounterSalesRate == 3) {
                                dRate = crsrItem.getInt(crsrItem.getColumnIndex("DineInPrice3"));
                            }
                        }
                        if (jBillingMode == 3) {
                            if (PickUpRate == 1) {
                                dRate = crsrItem.getInt(crsrItem.getColumnIndex("DineInPrice1"));
                            } else if (PickUpRate == 2) {
                                dRate = crsrItem.getInt(crsrItem.getColumnIndex("DineInPrice2"));
                            } else if (PickUpRate == 3) {
                                dRate = crsrItem.getInt(crsrItem.getColumnIndex("DineInPrice3"));
                            }
                        }
                        if (jBillingMode == 4) {
                            if (HomeDeliveryRate == 1) {
                                dRate = crsrItem.getInt(crsrItem.getColumnIndex("DineInPrice1"));
                            } else if (HomeDeliveryRate == 2) {
                                dRate = crsrItem.getInt(crsrItem.getColumnIndex("DineInPrice2"));
                            } else if (HomeDeliveryRate == 3) {
                                dRate = crsrItem.getInt(crsrItem.getColumnIndex("DineInPrice3"));
                            }
                        }
                    }
                    // Menu Code
                    chkNumber = new CheckBox(myContext);
                    chkNumber.setWidth(40); // 57px ~= 85dp
                    chkNumber.setTextSize(0);
                    chkNumber.setTextColor(Color.TRANSPARENT);
                    chkNumber.setText(crsrItem.getString(crsrItem.getColumnIndex("MenuCode")));

                    // Item Name
                    tvName = new TextView(myContext);
                    tvName.setWidth(135); // 154px ~= 230dp
                    tvName.setTextSize(11);
                    tvName.setText(crsrItem.getString(crsrItem.getColumnIndex("ItemName")));

                    //hsn code
                    tvHSn = new TextView(myContext);
                    tvHSn.setWidth(67); // 154px ~= 230dp
                    tvHSn.setTextSize(11);
                    if (GSTEnable.equalsIgnoreCase("1") && HSNEnable_out.equals("1")) {
                        tvHSn.setText(crsrItem.getString(crsrItem.getColumnIndex("HSNCode")));
                    }

                    // Quantity
                    etQty = new EditText(myContext);
                    etQty.setWidth(55); // 57px ~= 85dp
                    etQty.setTextSize(11);
                    etQty.setSelectAllOnFocus(true);
                    etQty.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    // Read quantity from weighing scale if read from weigh
                    // scale is set in settings
                    if (jWeighScale == 0) {
                        etQty.setText("1.00");
                    } else {
                        etQty.setText(String.format("%.2f", getQuantityFromWeighScale()));
                    }
                    etQty.setTag("QTY_RATE");
                    etQty.setOnClickListener(Qty_Rate_Click);
                    etQty.setOnKeyListener(Qty_Rate_KeyPressEvent);
                    etInputValidate.ValidateDecimalInput(etQty);
                    etQty.addTextChangedListener(new TextWatcher() {
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        public void afterTextChanged(Editable s) {
                            Qty_Rate_Edit();
                        }
                    });

                    int BillwithStock = crsrSettings.getInt(crsrSettings.getColumnIndex("BillwithStock"));
                    if (BillwithStock == 1) {
                        if (crsrItem.getFloat(crsrItem.getColumnIndex("Quantity")) < Float.valueOf(etQty.getText().toString())) {
                            String availableQty = crsrItem.getString(crsrItem.getColumnIndex("Quantity")) ;
                            MsgBox.Show("Warning", "Stock is less, present stock quantity is "
                                    + availableQty);
                            etQty.setText(String.format("%.2f", Double.parseDouble(availableQty)));
                            return;
                        }
                    }

                    // Rate
                    etRate = new EditText(myContext);
                    etRate.setWidth(70); // 74px ~= 110dp
                    etRate.setTextSize(11);
                    etRate.setSelectAllOnFocus(true);
                    etRate.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    etRate.setText(String.format("%.2f", dRate));
                    etRate.setTag("QTY_RATE");
                    // Check whether Price change is enabled for the item, if
                    // not set Rate text box click able property to false
                    if (crsrSettings.getInt(crsrSettings.getColumnIndex("PriceChange")) == 0) {
                        etRate.setEnabled(false);
                    } else {
                        etRate.setOnClickListener(Qty_Rate_Click);
                        etRate.setOnKeyListener(Qty_Rate_KeyPressEvent);
                        etInputValidate.ValidateDecimalInput(etRate);
                    }

                    // Amount
                    tvAmount = new TextView(myContext);
                    tvAmount.setWidth(60); // 97px ~= 145dp
                    tvAmount.setTextSize(11);
                    tvAmount.setGravity(Gravity.RIGHT | Gravity.END);
                    tvAmount.setText(String.format("  %.2f", dRate));


                    // Discount Percent - Check whether Discount is enabled for
                    // the item,
                    // if enabled get discount percentage from discount table
                    if (crsrItem.getInt(crsrItem.getColumnIndex("DiscountEnable")) == 1) {
                        iDiscId = crsrItem.getInt(crsrItem.getColumnIndex("DiscId"));
                        crsrDiscount = dbBillScreen.getDiscountConfig(iDiscId);
                        if (!crsrDiscount.moveToFirst()) {
                            MsgBox.Show("Warning", "Failed to read Discount from crsrDiscount");
                            return;
                        } else {

                            dDiscPercent = crsrDiscount.getDouble(crsrDiscount.getColumnIndex("DiscPercentage"));
                        }
                    }
                    tvDiscPercent = new TextView(myContext);
                    tvDiscPercent.setWidth(50);
                    tvDiscPercent.setText(String.format("%.2f", dDiscPercent));

                    // Discount Amount
                    if (crsrSettings.getInt(crsrSettings.getColumnIndex("Tax")) == 1) { // Forward
                        // Tax
                        dDiscAmt = dRate * (dDiscPercent / 100);
                    } else { // Reverse Tax
                        double dBasePrice = 0;
                        dBasePrice = dRate / (1 + (dTaxPercent / 100));
                        dDiscAmt = dBasePrice * (dDiscPercent / 100);
                    }
                    tvDiscAmt = new TextView(myContext);
                    tvDiscAmt.setWidth(50);
                    tvDiscAmt.setText(String.format("%.2f", dDiscAmt));


                    // Tax Percent
                    if (GSTEnable.equalsIgnoreCase("1")) {
                        dTaxPercent = crsrItem.getDouble(crsrItem.getColumnIndex("CGSTRate"));
                    } else {
                        /*iTaxId = crsrItem.getInt(crsrItem.getColumnIndex("SalesTaxId"));
                        crsrTax = dbBillScreen.getTaxConfig(iTaxId);
                        // Return back, If tax table data is returned empty.
                        if (!crsrTax.moveToFirst()) {
                            MsgBox.Show("Warning", "Failed to read Tax from crsrTax");
                            return;
                        } else {

                            dTaxPercent = crsrTax.getDouble(crsrTax.getColumnIndex("TotalPercentage"));
                        }*/
                        dTaxPercent = crsrItem.getDouble(crsrItem.getColumnIndex("SalesTaxPercent"));
                    }
                    tvTaxPercent = new TextView(myContext);
                    tvTaxPercent.setWidth(50);
                    tvTaxPercent.setText(String.format("%.2f", dTaxPercent));


                    // Tax Amount
                    if (crsrSettings.getInt(crsrSettings.getColumnIndex("Tax")) == 1) { // Forward
                        // Tax
                        dTaxAmt = (dRate - dDiscAmt) * (dTaxPercent / 100);
                    } else { // Reverse Tax
                        double dBasePrice = 0;
                        dBasePrice = dRate / (1 + (dTaxPercent / 100));
                        dTaxAmt = (dBasePrice - dDiscAmt) * (dTaxPercent / 100);
                    }
                    tvTaxAmt = new TextView(myContext);
                    tvTaxAmt.setWidth(50);
                    tvTaxAmt.setText(String.format("%.2f", dTaxAmt));
//                    MsgBox.Show("", tvTaxAmt.getText().toString());

                    // Service Tax charge
                    if (GSTEnable.equalsIgnoreCase("1")) {
                        dServiceTaxPercent = crsrItem.getDouble(crsrItem.getColumnIndex("SGSTRate"));
                    } else {
                        /*iServiceTaxId = crsrItem.getInt(crsrItem.getColumnIndex("AdditionalTaxId"));
                        crsrTax = dbBillScreen.getTaxConfig(iServiceTaxId);
                        // Return back, If tax table data is returned empty.
                        if (!crsrTax.moveToFirst()) {
                            MsgBox.Show("Warning", "Failed to read Tax from crsrTax");
                            return;
                        } else {

                            dServiceTaxPercent = crsrTax.getDouble(crsrTax.getColumnIndex("TotalPercentage"));
                        }*/
                        dServiceTaxPercent = crsrItem.getDouble(crsrItem.getColumnIndex("ServiceTaxPercent"));
                    }
                    // Service Tax Amount
                    if (crsrSettings.getInt(crsrSettings.getColumnIndex("Tax")) == 1) { // Forward
                        // Tax
                        dServiceTaxAmt = (dRate - dDiscAmt) * (dServiceTaxPercent / 100);
                    } else { // Reverse Tax
                        double dBasePrice = 0;
                        dBasePrice = dRate / (1 + (dServiceTaxPercent / 100));
                        dServiceTaxAmt = (dBasePrice - dDiscAmt) * (dServiceTaxPercent / 100);
                    }
                    tvServiceTaxAmt = new TextView(myContext);
                    tvServiceTaxAmt.setWidth(50);
                    tvServiceTaxAmt.setText(String.format("%.2f", dServiceTaxAmt));
//                    MsgBox.Show("", tvServiceTaxAmt.getText().toString());


                    // Department Code
                    tvDeptCode = new TextView(myContext);
                    tvDeptCode.setWidth(50);
                    tvDeptCode.setText(crsrItem.getString(crsrItem.getColumnIndex("DeptCode")));

                    // Category Code
                    tvCategCode = new TextView(myContext);
                    tvCategCode.setWidth(50);
                    tvCategCode.setText(crsrItem.getString(crsrItem.getColumnIndex("CategCode")));

                    // Kitchen Code
                    tvKitchenCode = new TextView(myContext);
                    tvKitchenCode.setWidth(50);
                    tvKitchenCode.setText(crsrItem.getString(crsrItem.getColumnIndex("KitchenCode")));

                    // Tax Type [Forward - 1/ Reverse - 2]
                    tvTaxType = new TextView(myContext);
                    tvTaxType.setWidth(50);
                    tvTaxType.setText(crsrItem.getString(crsrItem.getColumnIndex("TaxType")));

                    // Modifier Charge
                    tvModifierCharge = new TextView(myContext);
                    tvModifierCharge.setWidth(50);
                    tvModifierCharge.setText("0.0");

                    TextView tvUOM = new TextView(myContext);
                    tvUOM.setWidth(50);
                    tvUOM.setText(crsrItem.getString(crsrItem.getColumnIndex("UOM")));

                    // SupplyType
                    TextView SupplyType = new TextView(myContext);
                    SupplyType.setText(crsrItem.getString(crsrItem.getColumnIndex("SupplyType")));
                    SupplyType.setWidth(30);

                    TextView tvSpace = new TextView(myContext);
                    tvSpace.setText("        ");

                    // Delete
                    int res = getResources().getIdentifier("delete", "drawable", this.getPackageName());
                    ImageButton ImgDelete = new ImageButton(myContext);
                    ImgDelete.setImageResource(res);
                    // btnDelete.setText(crsrItem.getString(crsrItem.getColumnIndex("MenuCode")));
                    ImgDelete.setOnClickListener(new OnClickListener() {
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
                                    CalculateTotalAmount();

                                }
                            })
                            .show();

                }
            });

                    TextView tvSpace1 = new TextView(myContext);
                    tvSpace1.setText("       ");

                    TextView tvPrintKOTStatus = new TextView(myContext);
                    tvPrintKOTStatus.setText("1");

                    // Add all text views and edit text to Item Row
                    rowItem.addView(chkNumber);//0
                    rowItem.addView(tvName);//1
                    rowItem.addView(tvHSn);//2
                    rowItem.addView(etQty);//3
                    rowItem.addView(etRate);//4
                    rowItem.addView(tvAmount);//5
                    rowItem.addView(tvTaxPercent);//6
                    rowItem.addView(tvTaxAmt);//7
                    rowItem.addView(tvDiscPercent);//8
                    rowItem.addView(tvDiscAmt);//9
                    rowItem.addView(tvDeptCode);//10
                    rowItem.addView(tvCategCode);//11
                    rowItem.addView(tvKitchenCode);//12
                    rowItem.addView(tvTaxType);//13
                    rowItem.addView(tvModifierCharge);//14
                    rowItem.addView(tvServiceTaxPercent);//15
                    rowItem.addView(tvServiceTaxAmt);//16
                    rowItem.addView(SupplyType);//17
                    rowItem.addView(tvSpace);//18
                    rowItem.addView(ImgDelete);//19
                    rowItem.addView(tvSpace1);//20
                    rowItem.addView(tvPrintKOTStatus);//21
                    rowItem.addView(tvUOM);//22
                    //android:collapseColumns="0,5,6,7,8,9,10,11,12,13,14,15"
                    tblOrderItems.addView(rowItem,
                            new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                }

                bItemExists = false;

            } while (crsrItem.moveToNext());

            CalculateTotalAmount();

            // Display Total Items
            //txtTotalItems.setText(String.valueOf(tblOrderItems.getChildCount()));

        } else {
            Log.d("AddItemToOrderTable", "ItemNotFound Exception");
        }
    }

    /*************************************************************************************************************************************
     * Calculates all values such as Tax, Discount, Sub Total and grand total
     * whenever Quantity or Rate text value changed
     *************************************************************************************************************************************/
    private void Qty_Rate_Edit() {

        String strQty = "0";
        double dTaxPercent = 0, dDiscPercent = 0, dDiscAmt = 0, dTempAmt = 0, dTaxAmt = 0;
        double dRate;
        try {
            for (int iRow = 0; iRow < tblOrderItems.getChildCount(); iRow++) {

                TableRow Row = (TableRow) tblOrderItems.getChildAt(iRow);

                // Check against item number present in table
                if (Row.getChildAt(0) != null) {

                    CheckBox Number = (CheckBox) Row.getChildAt(0);
                    TextView ItemName = (TextView) Row.getChildAt(1);


                    // Quantity
                    EditText Qty = (EditText) Row.getChildAt(3);
                    Qty.setSelectAllOnFocus(true);
                    strQty = Qty.getText().toString().equalsIgnoreCase("") ? "0" : Qty.getText().toString(); // Temp
                    Cursor crsrSett = dbBillScreen.getBillSetting();
                    if(crsrSett!=null && crsrSett.moveToFirst())
                    {
                        int BillwithStock = crsrSett.getInt(crsrSett.getColumnIndex("BillwithStock"));
                        if (BillwithStock == 1) {
                            Cursor ItemCrsr = dbBillScreen.getItemDetails(ItemName.getText().toString());
                            if(ItemCrsr!=null && ItemCrsr.moveToFirst())
                            {
                                double availableStock = ItemCrsr.getDouble(ItemCrsr.getColumnIndex("Quantity"));
                                if ( availableStock < Float.valueOf(Qty.getText().toString())) {
                                    MsgBox.Show("Warning", "Stock is less, present stock quantity is "
                                            + String.valueOf(availableStock));
                                    Qty.setText(String.format("%.2f", availableStock));

                                    return;
                                }
                            }

                        }
                    }



                    // Amount
                    EditText Rate = (EditText) Row.getChildAt(4);
                    Rate.setSelectAllOnFocus(true);
                    TextView Amount = (TextView) Row.getChildAt(5);
                    dRate = Double.parseDouble(
                            Rate.getText().toString().equalsIgnoreCase("") ? "0" : Rate.getText().toString()); // Temp
                    Amount.setText(
                            String.format("%.2f", (Double.parseDouble(Qty.getText().toString()) * dRate)));

                    // Tax and Discount Amount
                    TextView TaxPer = (TextView) Row.getChildAt(6);
                    TextView TaxAmt = (TextView) Row.getChildAt(7);
                    TextView DiscPer = (TextView) Row.getChildAt(8);
                    TextView DiscAmt = (TextView) Row.getChildAt(9);
                    TextView ServiceTax = (TextView) Row.getChildAt(15);
                    TextView ServiceTaxAmt = (TextView) Row.getChildAt(16);
                    // TextView Delete = (TextView) Row.getChildAt(16);

                    dTaxPercent = Double.parseDouble(TaxPer.getText().toString().equalsIgnoreCase("") ? "0"
                            : TaxPer.getText().toString()); // Temp
                    dDiscPercent = Double.parseDouble(DiscPer.getText().toString().equalsIgnoreCase("") ? "0"
                            : DiscPer.getText().toString()); // Temp

                    if (crsrSettings.getInt(crsrSettings.getColumnIndex("Tax")) == 1) {
                        // Discount
                        dDiscAmt = dRate * (dDiscPercent / 100);
                        dTempAmt = dDiscAmt;
                        dDiscAmt = dDiscAmt * Double.parseDouble(Qty.getText().toString());

                        // Tax
                        dTaxAmt = (dRate - dTempAmt) * (dTaxPercent / 100);
                        dTaxAmt = dTaxAmt * Double.parseDouble(Qty.getText().toString());

                        TaxAmt.setText(String.format("%.2f", dTaxAmt));
                        DiscAmt.setText(String.format("%.2f", dDiscAmt));

                    } else {
                        double dBasePrice = 0;
                        dBasePrice = dRate / (1 + (dTaxPercent / 100));

                        // Discount
                        dDiscAmt = dBasePrice * (dDiscPercent / 100);
                        dTempAmt = dDiscAmt;
                        dDiscAmt = dDiscAmt * Double.parseDouble(Qty.getText().toString());

                        // Tax
                        dTaxAmt = (dBasePrice - dTempAmt) * (dTaxPercent / 100);
                        dTaxAmt = dTaxAmt * Double.parseDouble(Qty.getText().toString());

                        TaxAmt.setText(String.format("%.2f", dTaxAmt));
                        DiscAmt.setText(String.format("%.2f", dDiscAmt));
                    }

                    // Service Tax charge
                    dTaxAmt = dRate * (Double.parseDouble(ServiceTax.getText().toString()) / 100);
                    ServiceTaxAmt.setText(
                            String.format("%.2f", (Double.parseDouble(Qty.getText().toString()) * dTaxAmt)));

                    // // delete
                    // Delete.setText("Hi");

                    // Clear all variables and set ItemExists to TRUE
                    // and break from the loop
                    dRate = 0;
                    dTaxPercent = 0;
                    dDiscPercent = 0;
                    dTaxAmt = 0;
                    dDiscAmt = 0;
                    dTempAmt = 0;
                    //bItemExists = true;

                }

            }
            CalculateTotalAmount();
        } catch (Exception e) {
            MsgBox.setMessage("Error while changing quantity directly :" + e.getMessage())
                    .setPositiveButton("OK", null)
                    .show();
        }


    }

    /*************************************************************************************************************************************
     * Adds selected KOT modifier to selected items in billing table
     *
     * @param Modifier : Modifier data which has to included to item
     *************************************************************************************************************************************/
    private void AddModifier(Cursor Modifier) {

        double dModifierAmt = 0;

        CheckBox Number;
        TextView Name, ModifierAmt;

        if (Modifier.moveToFirst()) {

            for (int iRow = 0; iRow < tblOrderItems.getChildCount(); iRow++) {

                TableRow RowItem = (TableRow) tblOrderItems.getChildAt(iRow);

                if (RowItem.getChildAt(0) != null) {

                    Number = (CheckBox) RowItem.getChildAt(0);
                    Name = (TextView) RowItem.getChildAt(1);
                    ModifierAmt = (TextView) RowItem.getChildAt(13);

                    if (Number.isChecked()) {
                        Name.setText(Name.getText().toString() + "+"
                                + Modifier.getString(Modifier.getColumnIndex("ModifierDescription")));

                        if (Modifier.getInt(Modifier.getColumnIndex("IsChargeable")) == 1) {

                            dModifierAmt = Double.parseDouble(ModifierAmt.getText().toString());
                            dModifierAmt += Modifier.getDouble(Modifier.getColumnIndex("ModifierAmount"));
                            ModifierAmt.setText(String.format("%.2f", dModifierAmt));
                        }
                    }
                }
            }
            CalculateTotalAmount();
        } else {
            MsgBox.Show("Warning", "No KOT modifiers found");
        }
    }

    /*************************************************************************************************************************************
     * Deletes selected items from billing table
     *************************************************************************************************************************************/
    private void DeleteItem() {

        CheckBox MenuCode;

        for (int iRow = 0; iRow < tblOrderItems.getChildCount(); iRow++) {

            TableRow Row = (TableRow) tblOrderItems.getChildAt(iRow);

            if (Row.getChildAt(0) != null) {

                MenuCode = (CheckBox) Row.getChildAt(0);

                if (MenuCode.isChecked()) {

                    // Remove all the view present in the row.
                    Row.removeAllViews();

                    // Remove the row
                    tblOrderItems.removeView(Row);

                    // Exit from the loop
                    // break;
                }
            } else {
                continue;
            }

        }

        CalculateTotalAmount();

        // Display Total Items
        //txtTotalItems.setText(String.valueOf(tblOrderItems.getChildCount()));

    }

    /*************************************************************************************************************************************
     * Calculates bill sub total, sales tax amount, service tax amount and Bill
     * total amount.
     ************************************************************************************************************************************/
    private void CalculateTotalAmount() {

        double dSubTotal = 0, dTaxTotal = 0, dModifierAmt = 0, dServiceTaxAmt = 0, dOtherCharges = 0, dTaxAmt = 0, dSerTaxAmt = 0;
        float dTaxPercent = 0, dSerTaxPercent = 0;

        // Item wise tax calculation ----------------------------
        for (int iRow = 0; iRow < tblOrderItems.getChildCount(); iRow++) {

            TableRow RowItem = (TableRow) tblOrderItems.getChildAt(iRow);

            if (RowItem.getChildAt(0) != null) {

                TextView ColTaxType = (TextView) RowItem.getChildAt(13);
                TextView ColAmount = (TextView) RowItem.getChildAt(5);
                TextView ColDisc = (TextView) RowItem.getChildAt(9);
                TextView ColTax = (TextView) RowItem.getChildAt(7);
                TextView ColModifierAmount = (TextView) RowItem.getChildAt(14);
                TextView ColServiceTaxAmount = (TextView) RowItem.getChildAt(16);

                dTaxTotal += Double.parseDouble(ColTax.getText().toString());
                dServiceTaxAmt += Double.parseDouble(ColServiceTaxAmount.getText().toString());

                dSubTotal += Double.parseDouble(ColAmount.getText().toString());
            }
        }
        // ------------------------------------------

        // Bill wise tax Calculation -------------------------------
        Cursor crsrtax = dbBillScreen.getTaxConfig(1);
        if (crsrtax.moveToFirst()) {
            dTaxPercent = crsrtax.getFloat(crsrtax.getColumnIndex("TotalPercentage"));
            dTaxAmt += dSubTotal * (dTaxPercent / 100);
        }
        Cursor crsrtax1 = dbBillScreen.getTaxConfig(2);
        if (crsrtax1.moveToFirst()) {
            dSerTaxPercent = crsrtax1.getFloat(crsrtax1.getColumnIndex("TotalPercentage"));
            dSerTaxAmt += dSubTotal * (dSerTaxPercent / 100);
        }
        // -------------------------------------------------

        dOtherCharges = Double.valueOf(txtOthercharges.getText().toString());
        //String strTax = crsrSettings.getString(crsrSettings.getColumnIndex("Tax"));
        if (crsrSettings.moveToFirst()) {
            if (crsrSettings.getString(crsrSettings.getColumnIndex("Tax")).equalsIgnoreCase("1")) {
                if (crsrSettings.getString(crsrSettings.getColumnIndex("TaxType")).equalsIgnoreCase("1")) {

                    if (chk_interstate.isChecked()) // interstate
                    {
                        tvTaxTotal.setText(String.format("%.2f", dTaxTotal + dServiceTaxAmt));
                        //tvServiceTaxTotal.setText(String.format("%.2f", dServiceTaxAmt));
                        tvServiceTaxTotal.setText("");
                    } else {
                        tvTaxTotal.setText(String.format("%.2f", dTaxTotal));
                        tvServiceTaxTotal.setText(String.format("%.2f", dServiceTaxAmt));
                    }

                    tvSubTotal.setText(String.format("%.2f", dSubTotal));
                    tvBillAmount.setText(String.format("%.2f", dSubTotal + dTaxTotal + dServiceTaxAmt + dOtherCharges));
                } else {

                    if (chk_interstate.isChecked()) {
                        tvTaxTotal.setText(String.format("%.2f", dTaxAmt + dSerTaxAmt));
                        //tvServiceTaxTotal.setText(String.format("%.2f", dSerTaxAmt));
                        tvServiceTaxTotal.setText("");
                    } else {
                        tvTaxTotal.setText(String.format("%.2f", dTaxAmt));
                        tvServiceTaxTotal.setText(String.format("%.2f", dSerTaxAmt));
                    }
                    tvSubTotal.setText(String.format("%.2f", dSubTotal));
                    tvBillAmount.setText(String.format("%.2f", dSubTotal + dTaxAmt + dSerTaxAmt + dOtherCharges));
                }
            } else {
                if (crsrSettings.getString(crsrSettings.getColumnIndex("TaxType")).equalsIgnoreCase("1")) {
                    if (chk_interstate.isChecked()) {
                        tvTaxTotal.setText(String.format("%.2f", dTaxTotal + dServiceTaxAmt));
                        // tvServiceTaxTotal.setText(String.format("%.2f", dServiceTaxAmt));
                        tvServiceTaxTotal.setText("");
                    } else {
                        tvTaxTotal.setText(String.format("%.2f", dTaxTotal));
                        tvServiceTaxTotal.setText(String.format("%.2f", dServiceTaxAmt));
                    }
                    tvSubTotal.setText(String.format("%.2f", dSubTotal));
                    tvBillAmount.setText(String.format("%.2f", dSubTotal + dOtherCharges));

                } else {
                    tvSubTotal.setText(String.format("%.2f", dSubTotal));
                    if (chk_interstate.isChecked()) {
                        tvTaxTotal.setText(String.format("%.2f", dTaxAmt + dSerTaxAmt));
                        // tvServiceTaxTotal.setText(String.format("%.2f", dSerTaxAmt));
                        tvServiceTaxTotal.setText("");
                    } else {
                        tvTaxTotal.setText(String.format("%.2f", dTaxAmt));
                        tvServiceTaxTotal.setText(String.format("%.2f", dSerTaxAmt));
                    }
                    tvTaxTotal.setText(String.format("%.2f", dTaxAmt));
                    tvServiceTaxTotal.setText(String.format("%.2f", dSerTaxAmt));
                    tvBillAmount.setText(String.format("%.2f", dSubTotal + dOtherCharges));
                }
            }
        }
    }

    private void CalculateTotalAmountforRePrint() {

        double dSubTotal = 0, dTaxTotal = 0, dModifierAmt = 0, dServiceTaxAmt = 0, dOtherCharges = 0, dTaxAmt = 0, dSerTaxAmt = 0;
        float dTaxPercent = 0, dSerTaxPercent = 0;

        // Item wise tax calculation ----------------------------
        for (int iRow = 0; iRow < tblOrderItems.getChildCount(); iRow++) {

            TableRow RowItem = (TableRow) tblOrderItems.getChildAt(iRow);

            if (RowItem.getChildAt(0) != null) {

                //TextView ColTaxType = (TextView) RowItem.getChildAt(12);
                TextView ColAmount = (TextView) RowItem.getChildAt(5);
                //TextView ColDisc = (TextView) RowItem.getChildAt(8);
                TextView ColTax = (TextView) RowItem.getChildAt(7);
                //TextView ColModifierAmount = (TextView) RowItem.getChildAt(13);
                TextView ColServiceTaxAmount = (TextView) RowItem.getChildAt(16);

                dTaxTotal += Double.parseDouble(ColTax.getText().toString());
                dServiceTaxAmt += Double.parseDouble(ColServiceTaxAmount.getText().toString());

                dSubTotal += Double.parseDouble(ColAmount.getText().toString());
            }
        }
        // ------------------------------------------

        // Bill wise tax Calculation -------------------------------
        Cursor crsrtax = dbBillScreen.getTaxConfig(1);
        if (crsrtax.moveToFirst()) {
            dTaxPercent = crsrtax.getFloat(crsrtax.getColumnIndex("TotalPercentage"));
            dTaxAmt += dSubTotal * (dTaxPercent / 100);
        }
        Cursor crsrtax1 = dbBillScreen.getTaxConfig(2);
        if (crsrtax1.moveToFirst()) {
            dSerTaxPercent = crsrtax1.getFloat(crsrtax1.getColumnIndex("TotalPercentage"));
            dSerTaxAmt += dSubTotal * (dSerTaxPercent / 100);
        }
        // -------------------------------------------------

        dOtherCharges = Double.valueOf(txtOthercharges.getText().toString());
        if (crsrSettings.moveToFirst()) {
            if (crsrSettings.getString(crsrSettings.getColumnIndex("Tax")).equalsIgnoreCase("1")) {
                if (crsrSettings.getString(crsrSettings.getColumnIndex("TaxType")).equalsIgnoreCase("1")) {

                    if (chk_interstate.isChecked()) // interstate
                    {
                        tvTaxTotal.setText(String.format("%.2f", dTaxTotal + dServiceTaxAmt));
                        //tvServiceTaxTotal.setText(String.format("%.2f", dServiceTaxAmt));
                        tvServiceTaxTotal.setText("");
                    } else {
                        tvTaxTotal.setText(String.format("%.2f", dTaxTotal));
                        tvServiceTaxTotal.setText(String.format("%.2f", dServiceTaxAmt));
                    }

                    tvSubTotal.setText(String.format("%.2f", dSubTotal));
                    tvBillAmount.setText(String.format("%.2f", dSubTotal + dTaxTotal + dServiceTaxAmt + dOtherCharges));
                } else {

                    if (chk_interstate.isChecked()) {
                        tvTaxTotal.setText(String.format("%.2f", dTaxAmt + dSerTaxAmt));
                        //tvServiceTaxTotal.setText(String.format("%.2f", dSerTaxAmt));
                        tvServiceTaxTotal.setText("");
                    } else {
                        tvTaxTotal.setText(String.format("%.2f", dTaxAmt));
                        tvServiceTaxTotal.setText(String.format("%.2f", dSerTaxAmt));
                    }
                    tvSubTotal.setText(String.format("%.2f", dSubTotal));
                    tvBillAmount.setText(String.format("%.2f", dSubTotal + dTaxAmt + dSerTaxAmt + dOtherCharges));
                }
            } else {
                if (crsrSettings.getString(crsrSettings.getColumnIndex("TaxType")).equalsIgnoreCase("1")) {
                    if (chk_interstate.isChecked()) {
                        tvTaxTotal.setText(String.format("%.2f", dTaxTotal + dServiceTaxAmt));
                        // tvServiceTaxTotal.setText(String.format("%.2f", dServiceTaxAmt));
                        tvServiceTaxTotal.setText("");
                    } else {
                        tvTaxTotal.setText(String.format("%.2f", dTaxTotal));
                        tvServiceTaxTotal.setText(String.format("%.2f", dServiceTaxAmt));
                    }
                    tvSubTotal.setText(String.format("%.2f", dSubTotal));
                    tvBillAmount.setText(String.format("%.2f", dSubTotal + dOtherCharges));

                } else {
                    tvSubTotal.setText(String.format("%.2f", dSubTotal));
                    if (chk_interstate.isChecked()) {
                        tvTaxTotal.setText(String.format("%.2f", dTaxAmt + dSerTaxAmt));
                        // tvServiceTaxTotal.setText(String.format("%.2f", dSerTaxAmt));
                        tvServiceTaxTotal.setText("");
                    } else {
                        tvTaxTotal.setText(String.format("%.2f", dTaxAmt));
                        tvServiceTaxTotal.setText(String.format("%.2f", dSerTaxAmt));
                    }
                    tvTaxTotal.setText(String.format("%.2f", dTaxAmt));
                    tvServiceTaxTotal.setText(String.format("%.2f", dSerTaxAmt));
                    tvBillAmount.setText(String.format("%.2f", dSubTotal + dOtherCharges));
                }
            }
        }
    }

    /*************************************************************************************************************************************
     * Delete all the items in billing table and clears all text boxes and item
     * grid in billing screen
     *************************************************************************************************************************************/
    private void ClearAll() {

        txtSearchItemBarcode.setText("");
        // txtSearchItemMenucode.setText("");
        tvWaiterNumber.setText("0");
        tvTableNumber.setText("0");
        tvTableSplitNo.setText("1");
        tvSubUdfValue.setText("1");
        tvSubTotal.setText("0.00");
        tvTaxTotal.setText("0.00");
        tvServiceTaxTotal.setText("0.00");
        tvBillAmount.setText("0.00");
        chk_interstate.setChecked(false);
        aTViewSearchItem.setText("");
        aTViewSearchMenuCode.setText("");

        btnDeleteKOT.setEnabled(false);
        btnAddCustomer.setEnabled(true);

        // Clear order item table
        tblOrderItems.removeAllViews();
        edtCustName.setText("");
        edtCustId.setText("0");
        edtCustPhoneNo.setText("");
        edtCustAddress.setText("");
        etCustGSTIN.setText("");
        // Display Total Items
        //txtTotalItems.setText("0");

        // Display current bill number
        tvBillNumber.setText(String.valueOf(dbBillScreen.getNewBillNumbers()));
        btnPrintBill.setEnabled(false);
        btnPrintKOT.setEnabled(false);
        // By default load 1st department items to grid
        //GetItemDetails(1);
        // This condition is to avoid NullReferenceException in getCount() in
        // ImageAdapter class.
        /*if (Name.length > 0) {
            // Assign item grid to image adapter
            grdItems.setAdapter(new ImageAdapter(myContext, Name, MenuCode, ImageUri, Byte.parseByte("1")));
            // Make the item grid visible
            grdItems.setVisibility(View.VISIBLE);
        } else {
            // Make the item grid invisible
            grdItems.setVisibility(View.INVISIBLE);
        }*/
    }


    /*************************************************************************************************************************************
     * Opens a dialog window which takes table number as input and proceeds for
     * billing
     *************************************************************************************************************************************/
    private void DinInTender() {

        AlertDialog.Builder DineInTenderDialog = new AlertDialog.Builder(myContext);

        LayoutInflater UserAuthorization = (LayoutInflater) myContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vwAuthorization = UserAuthorization.inflate(R.layout.tender_dinein, null);

        final EditText txtTblNo = (EditText) vwAuthorization.findViewById(R.id.txtDineInTenderTableNumber);
        final EditText txtTblSplitNo = (EditText) vwAuthorization.findViewById(R.id.txtDineInTenderTableSplitNo);

//        final EditText txtTblNo = new EditText(myContext);
//        txtTblNo.setInputType(InputType.TYPE_CLASS_NUMBER);
//
//        final EditText txtTblSplitNo = new EditText(myContext);
//        txtTblSplitNo.setInputType(InputType.TYPE_CLASS_NUMBER);

        DineInTenderDialog.setIcon(R.drawable.ic_launcher).setTitle("Dine In Tender").setMessage("Enter table number")
                .setView(vwAuthorization).setNegativeButton("Cancel", null)
                .setNeutralButton("CheckOut", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        if (txtTblNo.getText().toString().equalsIgnoreCase("")) {
                            MsgBox.Show("Warning", "Please enter table number");
                            return;
                        } else {
                            if (dbBillScreen.getCheckedOutStatus(Integer.parseInt(txtTblNo.getText().toString()),
                                    1) == 0) {
                                Cursor BillItems = dbBillScreen
                                        .getKOTItems(Integer.parseInt(txtTblNo.getText().toString()), 1, Integer.parseInt(txtTblSplitNo.getText().toString()));
                                if (BillItems.moveToFirst()) {
                                    int iResult = dbBillScreen
                                            .updateCheckedOutStatus(Integer.parseInt(txtTblNo.getText().toString()), 1);
                                    Log.v("CheckOut", "Updated rows:" + iResult);
                                    LoadKOTItems(BillItems);
                                    //PrintCheckOutBill();
                                    ClearAll();
                                }
                            } else {
                                MsgBox.Show("Warning",
                                        "Table is already checked out or no KOT is pending for the table number "
                                                + txtTblNo.getText().toString());
                            }
                        }
                    }
                }).setPositiveButton("Tender", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                if (txtTblNo.getText().toString().equalsIgnoreCase("")) {
                    MsgBox.Show("Warning", "Please enter table number");
                    return;
                } else {

                    tvTableNumber.setText(txtTblNo.getText().toString());
                    tvTableSplitNo.setText(txtTblSplitNo.getText().toString());
                    tvSubUdfValue.setText("1");
                    Cursor BillItems = dbBillScreen.getKOTItems(Integer.parseInt(txtTblNo.getText().toString()),
                            1, Integer.parseInt(txtTblSplitNo.getText().toString()));
                    if (BillItems.moveToFirst()) {
                        LoadKOTItems(BillItems);

                        Intent intentTender = new Intent(myContext, TenderActivity.class);
                        intentTender.putExtra("TotalAmount", tvBillAmount.getText().toString());
                        startActivityForResult(intentTender, 1);

                    } else {
                        MsgBox.Show("Warning",
                                "No KOT is present for the table number " + txtTblNo.getText().toString());
                    }
                }
            }
        }).show();
    }

    /*************************************************************************************************************************************
     * Inserts all the ordered item data to database with table number as
     * reference
     *************************************************************************************************************************************/
    private void InsertKOTItems(int KOTNo) {
        // Inserted Row Id in database table
        long lResult = 0;
        // PendingKOT object
        PendingKOT objPendingKOT;
        objPendingKOT = new PendingKOT();
        // Token number variable
        /*if(KOTNo<=0) {
            //int iTokenNumber = dbBillScreen.getKOTNo();
            objPendingKOT.setTokenNumber(dbBillScreen.getKOTNo());
            long iResult = dbBillScreen.updateKOTNo(dbBillScreen.getKOTNo());
        } else {
            objPendingKOT.setTokenNumber(iKOTNo);
        }*/
        objPendingKOT.setTokenNumber(KOTNo);
        String strTime = String.format("%tR", Time);
        String msg =  "Time:" + strTime+" No : "+KOTNo;
        Log.v("KOT Time, No",msg);

        for (int iRow = 0; iRow < tblOrderItems.getChildCount(); iRow++) {

            TableRow RowKOTItem = (TableRow) tblOrderItems.getChildAt(iRow);
            CheckBox ItemNumber = (CheckBox) RowKOTItem.getChildAt(0);
            TextView ItemName = (TextView) RowKOTItem.getChildAt(1);
            TextView hsn = (TextView) RowKOTItem.getChildAt(2);
            EditText Quantity = (EditText) RowKOTItem.getChildAt(3);
            EditText Rate = (EditText) RowKOTItem.getChildAt(4);
            TextView Amount = (TextView) RowKOTItem.getChildAt(5);
            TextView SalesTaxPercent = (TextView) RowKOTItem.getChildAt(6);
            TextView SalesTaxAmount = (TextView) RowKOTItem.getChildAt(7);
            TextView DiscountPercent = (TextView) RowKOTItem.getChildAt(8);
            TextView DiscountAmount = (TextView) RowKOTItem.getChildAt(9);
            TextView DeptCode = (TextView) RowKOTItem.getChildAt(10);
            TextView CategCode = (TextView) RowKOTItem.getChildAt(11);
            TextView KitchenCode = (TextView) RowKOTItem.getChildAt(12);
            TextView TaxType = (TextView) RowKOTItem.getChildAt(13);
            TextView ModifierAmount = (TextView) RowKOTItem.getChildAt(14);
            TextView ServiceTaxPercent = (TextView) RowKOTItem.getChildAt(15);
            TextView ServiceTaxAmount = (TextView) RowKOTItem.getChildAt(16);
            TextView SupplyType = (TextView) RowKOTItem.getChildAt(17);
            TextView printstatus = (TextView) RowKOTItem.getChildAt(21);
            String printStatus_str = printstatus.getText().toString();

            // TokenNumber
            //objPendingKOT.setTokenNumber(iTokenNumber);
            /*if (et_pos.getText().toString().equals(""))
            {
                objPendingKOT.setPOS("");
            }
            else
            {
                objPendingKOT.setPOS(et_pos.getText().toString());
            }*/
            objPendingKOT.setPOS("");
            if (jBillingMode == Byte.parseByte("1")) {
                // TableNumber
                objPendingKOT.setTableNumber(Integer.parseInt(tvTableNumber.getText().toString()));

                // SubUdfNumber
                objPendingKOT.setSubUdfNumber(Integer.parseInt(tvSubUdfValue.getText().toString()));

                // WaiterId
                objPendingKOT.setEmployeeId(Integer.parseInt(tvWaiterNumber.getText().toString()));

                // Checked out status
                objPendingKOT.setIsCheckedOut(0);

                // TableSplitNo
                objPendingKOT.setTableSplitNo(Integer.parseInt(tvTableSplitNo.getText().toString()));

                // Customer Id
                objPendingKOT.setCusId(Integer.valueOf(edtCustId.getText().toString()));

            } else {
                // TableNumber
                objPendingKOT.setTableNumber(0);

                // SubUdfNumber
                objPendingKOT.setSubUdfNumber(0);

                // WaiterId
                objPendingKOT.setEmployeeId(0);

                // TableSplitNo
                objPendingKOT.setTableSplitNo(Integer.parseInt(tvTableSplitNo.getText().toString()));

                // Customer Id
                objPendingKOT.setCusId(Integer.valueOf(edtCustId.getText().toString()));
            }


            // Time
            objPendingKOT.setTime(strTime);

            // Item Number
            if (RowKOTItem.getChildAt(0) != null) {
                objPendingKOT.setItemNumber(Integer.parseInt(ItemNumber.getText().toString()));
            }

            // Item Name
            if (RowKOTItem.getChildAt(1) != null) {
                objPendingKOT.setItemName(ItemName.getText().toString());
            }

            // Item Name
            if (RowKOTItem.getChildAt(2) != null) {
                objPendingKOT.setHSNCode(hsn.getText().toString());
            }

            // Quantity
            if (RowKOTItem.getChildAt(3) != null) {
                objPendingKOT.setQuantity(Float.parseFloat(Quantity.getText().toString()));
            }

            // Rate
            if (RowKOTItem.getChildAt(4) != null) {
                objPendingKOT.setRate(Float.parseFloat(Rate.getText().toString()));
            }

            // Amount
            if (RowKOTItem.getChildAt(5) != null) {
                objPendingKOT.setAmount(Float.parseFloat(Amount.getText().toString()));
            }

            // Sales Tax %
            if (RowKOTItem.getChildAt(6) != null) {
                objPendingKOT.setTaxPercent(Float.parseFloat(SalesTaxPercent.getText().toString()));
            }

            // Sales Tax Amount
            if (RowKOTItem.getChildAt(7) != null) {
                objPendingKOT.setTaxAmount(Float.parseFloat(SalesTaxAmount.getText().toString()));
            }

            // Discount %
            if (RowKOTItem.getChildAt(8) != null) {
                objPendingKOT.setDiscountPercent(Float.parseFloat(DiscountPercent.getText().toString()));
            }

            // Discount Amount
            if (RowKOTItem.getChildAt(9) != null) {
                objPendingKOT.setDiscountAmount(Float.parseFloat(DiscountAmount.getText().toString()));
            }

            // Department Code
            if (RowKOTItem.getChildAt(10) != null) {
                objPendingKOT.setDeptCode(Integer.parseInt(DeptCode.getText().toString()));
            }

            // Category Code
            if (RowKOTItem.getChildAt(11) != null) {
                objPendingKOT.setCategCode(Integer.parseInt(CategCode.getText().toString()));
            }

            // Kitchen Code
            if (RowKOTItem.getChildAt(12) != null) {
                objPendingKOT.setKitchenCode(Integer.parseInt(KitchenCode.getText().toString()));
            }

            // Tax Type
            if (RowKOTItem.getChildAt(13) != null) {
                objPendingKOT.setTaxType(Integer.parseInt(TaxType.getText().toString()));
            }

            // Modifier Amount
            if (RowKOTItem.getChildAt(14) != null) {
                objPendingKOT.setModifierAmount(Float.parseFloat(ModifierAmount.getText().toString()));
            }

            // Service Tax Percent
            if (RowKOTItem.getChildAt(15) != null) {
                objPendingKOT.setServiceTaxPercent(Float.parseFloat(ServiceTaxPercent.getText().toString()));
            }

            // Service Tax Amount
            if (RowKOTItem.getChildAt(16) != null) {
                objPendingKOT.setServiceTaxAmount(Float.parseFloat(ServiceTaxAmount.getText().toString()));
            }

            // SupplyType - G/S
            if (RowKOTItem.getChildAt(17) != null) {
                objPendingKOT.setSupplyType(SupplyType.getText().toString());
            }

            // Order Mode
            objPendingKOT.setOrderMode(jBillingMode);

            // KOT No
           // objPendingKOT.setTokenNumber(iTokenNumber);

            // Print KOT STatus
            objPendingKOT.setPrintKOTStatus(iPrintKOTStatus);

            int tblno, subudfno, tblsplitno, itemno, Status;
            tblno = Integer.valueOf(tvTableNumber.getText().toString());
            subudfno = Integer.valueOf(tvSubUdfValue.getText().toString());
            tblsplitno = Integer.valueOf(tvTableSplitNo.getText().toString());
            itemno = Integer.valueOf(ItemNumber.getText().toString());
            Status = Integer.valueOf(printstatus.getText().toString());

            Cursor crsrItemsUpdate = dbBillScreen.getItemsForUpdatingKOT(tblno, subudfno, tblsplitno, itemno, jBillingMode);
            if (crsrItemsUpdate.moveToFirst()) {
                float Qty = 0, Amt = 0, TaxAmt = 0, SerTaxAmt = 0;
                float qty_temp = Float.valueOf(crsrItemsUpdate.getString(crsrItemsUpdate.getColumnIndex("Quantity")));
                Qty = Float.valueOf(Quantity.getText().toString()) + Float.valueOf(crsrItemsUpdate.getString(crsrItemsUpdate.getColumnIndex("Quantity")));
                Amt = Float.valueOf(Amount.getText().toString()) + Float.valueOf(crsrItemsUpdate.getString(crsrItemsUpdate.getColumnIndex("Amount")));
                TaxAmt = Float.valueOf(Quantity.getText().toString()) + Float.valueOf(crsrItemsUpdate.getString(crsrItemsUpdate.getColumnIndex("TaxAmount")));
                SerTaxAmt = Float.valueOf(Quantity.getText().toString()) + Float.valueOf(crsrItemsUpdate.getString(crsrItemsUpdate.getColumnIndex("ServiceTaxAmount")));

                lResult = dbBillScreen.updateKOT(itemno, Qty, Amt, TaxAmt, SerTaxAmt, jBillingMode, Status,0,0);
                Log.d("UpdateKOT", "KOT item updated at position:" + lResult);
            } else {

                lResult = dbBillScreen.addKOT(objPendingKOT);
                Log.d("InsertKOT", "KOT item inserted at position:" + lResult);
            }


        }
    }

    /*************************************************************************************************************************************
     * Loads KOT order items to billing table
     *
     * @param crsrBillItems : Cursor with KOT order item details
     *************************************************************************************************************************************/
    @SuppressWarnings("deprecation")
    private void LoadKOTItems(Cursor crsrBillItems) {
        EditTextInputHandler etInputValidate = new EditTextInputHandler();
        TableRow rowItem;
        TextView tvName, tvHSn, tvAmount, tvTaxPercent, tvTaxAmt, tvDiscPercent, tvDiscAmt, // tvQty,
                // tvRate,
                tvDeptCode, tvCategCode, tvKitchenCode, tvTaxType, tvModifierCharge, tvServiceTaxPercent,
                tvServiceTaxAmt;
        EditText etQty, etRate;
        CheckBox Number;
        ImageButton ImgDelete;

        if (crsrBillItems.moveToFirst()) {

            // Get Token number
            iTokenNumber = crsrBillItems.getInt(crsrBillItems.getColumnIndex("TokenNumber"));

           /* et_pos.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("POS")));
            if (et_pos.getText().toString().equals(""))
            {
                chk_interstate.setChecked(false);
            }
            else
            {
                chk_interstate.setChecked(true);
            }
            */// Get waiter Id
            tvWaiterNumber.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("EmployeeId")));

            // Get Table number
            tvTableNumber.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("TableNumber")));

            // Get Table Split No
            tvTableSplitNo.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("TableSplitNo")));

            // Get Sub Udf number
            tvSubUdfValue.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("SubUdfNumber")));

            // Get Cust Id
            edtCustId.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("CustId")));
            Cursor crsrCustomer = dbBillScreen.getCustomer(crsrBillItems.getInt(crsrBillItems.getColumnIndex("CustId")));
            if (crsrCustomer.moveToFirst()) {
                edtCustPhoneNo.setText(crsrCustomer.getString(crsrCustomer.getColumnIndex("CustContactNumber")));
                edtCustName.setText(crsrCustomer.getString(crsrCustomer.getColumnIndex("CustName")));
                edtCustAddress.setText(crsrCustomer.getString(crsrCustomer.getColumnIndex("CustAddress")));
            }

            // Display items in table
            do {
                rowItem = new TableRow(myContext);
                rowItem.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

                // Item Number
                Number = new CheckBox(myContext);
                Number.setWidth(40);
                Number.setTextSize(0);
                Number.setTextColor(Color.TRANSPARENT);
                Number.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ItemNumber")));

                // Item Name
                tvName = new TextView(myContext);
                tvName.setWidth(135);
                tvName.setTextSize(11);
                tvName.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ItemName")));

                //hsn code
                tvHSn = new TextView(myContext);
                tvHSn.setWidth(67); // 154px ~= 230dp
                tvHSn.setTextSize(11);
                if (GSTEnable.equalsIgnoreCase("1") && (HSNEnable_out != null) && HSNEnable_out.equals("1")) {
                    tvHSn.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("HSNCode")));
                }
                // Quantity
                etQty = new EditText(myContext);
                etQty.setWidth(55);
                etQty.setTextSize(11);
                if (crsrBillItems.getString(crsrBillItems.getColumnIndex("PrintKOTStatus")).equalsIgnoreCase("1")) {
                    etQty.setEnabled(true);
                } else {
                    etQty.setEnabled(false);
                }
                etQty.setText(String.format("%.2f", crsrBillItems.getDouble(crsrBillItems.getColumnIndex("Quantity"))));
                etQty.setSelectAllOnFocus(true);
                etQty.setTag("QTY_RATE");
                if(jBillingMode ==2 || jBillingMode ==3 || jBillingMode ==4)
                {
                    etQty.setOnClickListener(Qty_Rate_Click);
                    etQty.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    etQty.setOnKeyListener(Qty_Rate_KeyPressEvent);
                    etInputValidate.ValidateDecimalInput(etQty);
                    etQty.addTextChangedListener(new TextWatcher() {
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        public void afterTextChanged(Editable s) {
                            Qty_Rate_Edit();
                        }
                    });
                }


                // Rate
                etRate = new EditText(myContext);
                etRate.setWidth(70);
                etRate.setEnabled(false);
                etRate.setTextSize(11);
                etRate.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                etRate.setText(String.format("%.2f", crsrBillItems.getDouble(crsrBillItems.getColumnIndex("Rate"))));

                // Amount
                tvAmount = new TextView(myContext);
                tvAmount.setWidth(60);
                tvAmount.setTextSize(11);
                tvAmount.setGravity(Gravity.RIGHT | Gravity.END);
                tvAmount.setText(
                        String.format("%.2f", crsrBillItems.getDouble(crsrBillItems.getColumnIndex("Amount"))));

                // Sales Tax%
                tvTaxPercent = new TextView(myContext);
                tvTaxPercent.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("TaxPercent")));

                // Sales Tax Amount
                tvTaxAmt = new TextView(myContext);
                tvTaxAmt.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("TaxAmount")));

                // Discount %
                tvDiscPercent = new TextView(myContext);
                tvDiscPercent.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("DiscountPercent")));

                // Discount Amount
                tvDiscAmt = new TextView(myContext);
                tvDiscAmt.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("DiscountAmount")));

                // Dept Code
                tvDeptCode = new TextView(myContext);
                tvDeptCode.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("DeptCode")));

                // Categ Code
                tvCategCode = new TextView(myContext);
                tvCategCode.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("CategCode")));

                // Kitchen Code
                tvKitchenCode = new TextView(myContext);
                tvKitchenCode.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("KitchenCode")));

                // Tax Type
                tvTaxType = new TextView(myContext);
                tvTaxType.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("TaxType")));

                // Modifier Amount
                tvModifierCharge = new TextView(myContext);
                tvModifierCharge.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ModifierAmount")));

                // Service Tax %
                tvServiceTaxPercent = new TextView(myContext);
                tvServiceTaxPercent.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ServiceTaxPercent")));

                // Service Tax Amount
                tvServiceTaxAmt = new TextView(myContext);
                tvServiceTaxAmt.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ServiceTaxAmount")));

                // Service Tax Amount
                TextView tvSupplyType = new TextView(myContext);
                tvSupplyType.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("SupplyType")));


                // Delete
                int res = getResources().getIdentifier("delete", "drawable", this.getPackageName());
                ImgDelete = new ImageButton(myContext);
                ImgDelete.setImageResource(res);
                ImgDelete.setVisibility(View.INVISIBLE);



                TextView tvSpace = new TextView(myContext);
                tvSpace.setText("        ");

                TextView tvSpace1 = new TextView(myContext);
                tvSpace1.setText("       ");

                TextView tvPrintKOTStatus = new TextView(myContext);
                if(REPRINT_KOT == 1)
                    tvPrintKOTStatus.setText("1");
                else
                    tvPrintKOTStatus.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("PrintKOTStatus")));



                // Add all text views and edit text to Item Row
                // rowItem.addView(tvNumber);
                rowItem.addView(Number);
                rowItem.addView(tvName);
                rowItem.addView(tvHSn);
                rowItem.addView(etQty);
                rowItem.addView(etRate);
                rowItem.addView(tvAmount);
                rowItem.addView(tvTaxPercent);
                rowItem.addView(tvTaxAmt);
                rowItem.addView(tvDiscPercent);
                rowItem.addView(tvDiscAmt);
                rowItem.addView(tvDeptCode);
                rowItem.addView(tvCategCode);
                rowItem.addView(tvKitchenCode);
                rowItem.addView(tvTaxType);
                rowItem.addView(tvModifierCharge);
                rowItem.addView(tvServiceTaxPercent);
                rowItem.addView(tvServiceTaxAmt);
                rowItem.addView(tvSupplyType);
                rowItem.addView(tvSpace);
                rowItem.addView(ImgDelete);
                rowItem.addView(tvSpace1);
                rowItem.addView(tvPrintKOTStatus);

                // Add row to table
                tblOrderItems.addView(rowItem, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

            } while (crsrBillItems.moveToNext());

            REPRINT_KOT =0;

            CalculateTotalAmount();
            Log.d("LoadKOTItems", "Items loaded successfully");
        } else {
            Log.d("LoadKOTItems", "No rows in cursor");
        }
    }

    /*************************************************************************************************************************************
     * Loads KOT order items to billing table
     *
     * @param crsrBillItems : Cursor with KOT order item details
     *************************************************************************************************************************************/
    @SuppressWarnings("deprecation")
    private void LoadModifyKOTItems(Cursor crsrBillItems) {
        EditTextInputHandler etInputValidate = new EditTextInputHandler();
        TableRow rowItem;
        TextView tvHSn, tvName, tvAmount, tvTaxPercent, tvTaxAmt, tvDiscPercent, tvDiscAmt, // tvQty,
                // tvRate,
                tvDeptCode, tvCategCode, tvKitchenCode, tvTaxType, tvModifierCharge, tvServiceTaxPercent,
                tvServiceTaxAmt;
        EditText etQty, etRate;
        CheckBox Number;
        ImageButton ImgDelete;

        if (crsrBillItems.moveToFirst()) {

            // Get Token number
            iTokenNumber = crsrBillItems.getInt(crsrBillItems.getColumnIndex("TokenNumber"));

            // Get waiter Id
            tvWaiterNumber.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("EmployeeId")));

            // Get Table number
            tvTableNumber.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("TableNumber")));

            // Get Sub Udf number
            tvSubUdfValue.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("SubUdfNumber")));

            // Get Cust Id
            edtCustId.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("CustId")));
            Cursor crsrCustomer = dbBillScreen.getCustomer(crsrBillItems.getInt(crsrBillItems.getColumnIndex("CustId")));
            if (crsrCustomer.moveToFirst()) {
                edtCustPhoneNo.setText(crsrCustomer.getString(crsrCustomer.getColumnIndex("CustContactNumber")));
                edtCustName.setText(crsrCustomer.getString(crsrCustomer.getColumnIndex("CustName")));
                edtCustAddress.setText(crsrCustomer.getString(crsrCustomer.getColumnIndex("CustAddress")));
            }

            // Display items in table
            do {
                rowItem = new TableRow(myContext);
                rowItem.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

                // Item Number
                Number = new CheckBox(myContext);
                Number.setWidth(40);
                Number.setTextSize(0);
                Number.setTextColor(Color.TRANSPARENT);
                Number.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ItemNumber")));

                // Item Name
                tvName = new TextView(myContext);
                tvName.setWidth(135);
                tvName.setTextSize(11);
                tvName.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ItemName")));

                //hsn code
                tvHSn = new TextView(myContext);
                tvHSn.setWidth(67); // 154px ~= 230dp
                tvHSn.setTextSize(11);
                if (GSTEnable.equalsIgnoreCase("1") && (HSNEnable_out != null) && HSNEnable_out.equals("1")) {
                    tvHSn.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("HSNCode")));
                }
                // Quantity
                etQty = new EditText(myContext);
                etQty.setWidth(55);
                etQty.setTextSize(11);
                if (crsrBillItems.getString(crsrBillItems.getColumnIndex("PrintKOTStatus")).equalsIgnoreCase("1")) {
                    etQty.setEnabled(true);
                } else {
                    etQty.setEnabled(false);
                }
                etQty.setText(String.format("%.2f", crsrBillItems.getDouble(crsrBillItems.getColumnIndex("Quantity"))));
                etQty.setSelectAllOnFocus(true);
                etQty.setTag("QTY_RATE");
                etQty.setOnClickListener(Qty_Rate_Click);
                etQty.setOnKeyListener(Qty_Rate_KeyPressEvent);
                etInputValidate.ValidateDecimalInput(etQty);
                etQty.addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    public void afterTextChanged(Editable s) {
                        Qty_Rate_Edit();
                    }
                });

                // Rate
                etRate = new EditText(myContext);
                etRate.setWidth(70);
                etRate.setEnabled(false);
                etRate.setTextSize(11);
                etRate.setText(String.format("%.2f", crsrBillItems.getDouble(crsrBillItems.getColumnIndex("Rate"))));

                // Amount
                tvAmount = new TextView(myContext);
                tvAmount.setWidth(60);
                tvAmount.setTextSize(11);
                tvAmount.setGravity(Gravity.RIGHT | Gravity.END);
                tvAmount.setText(
                        String.format("%.2f", crsrBillItems.getDouble(crsrBillItems.getColumnIndex("Amount"))));

                // Sales Tax%
                tvTaxPercent = new TextView(myContext);
                tvTaxPercent.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("TaxPercent")));

                // Sales Tax Amount
                tvTaxAmt = new TextView(myContext);
                tvTaxAmt.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("TaxAmount")));

                // Discount %
                tvDiscPercent = new TextView(myContext);
                tvDiscPercent.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("DiscountPercent")));

                // Discount Amount
                tvDiscAmt = new TextView(myContext);
                tvDiscAmt.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("DiscountAmount")));

                // Dept Code
                tvDeptCode = new TextView(myContext);
                tvDeptCode.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("DeptCode")));

                // Categ Code
                tvCategCode = new TextView(myContext);
                tvCategCode.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("CategCode")));

                // Kitchen Code
                tvKitchenCode = new TextView(myContext);
                tvKitchenCode.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("KitchenCode")));

                // Tax Type
                tvTaxType = new TextView(myContext);
                tvTaxType.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("TaxType")));

                // Modifier Amount
                tvModifierCharge = new TextView(myContext);
                tvModifierCharge.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ModifierAmount")));

                // Service Tax %
                tvServiceTaxPercent = new TextView(myContext);
                tvServiceTaxPercent.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ServiceTaxPercent")));

                // Service Tax Amount
                tvServiceTaxAmt = new TextView(myContext);
                tvServiceTaxAmt.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ServiceTaxAmount")));

                // SupplyType
                TextView SupplyType = new TextView(myContext);
                SupplyType.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("SupplyType")));
                SupplyType.setWidth(30);

                // rowItem.setClickable(true);
                // rowItem.setOnClickListener(new OnClickListener() {
                // public void onClick(View view) {
                // TableRow tablerow = (TableRow) view;
                // TextView sample = (TextView) tablerow.getChildAt(0);
                //
                // Toast.makeText(myContext, sample.getText().toString(),
                // Toast.LENGTH_LONG).show();
                // }
                // });
                // Delete
                int res = getResources().getIdentifier("delete", "drawable", this.getPackageName());
                ImgDelete = new ImageButton(myContext);
                // ImgDeleteKOT.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ItemNumber")));
                ImgDelete.setImageResource(res);
                ImgDelete.setOnClickListener(mListener);

                TextView tvSpace = new TextView(myContext);
                tvSpace.setText("        ");

                TextView tvSpace1 = new TextView(myContext);
                tvSpace1.setText("       ");

                TextView tvPrintKOTStatus = new TextView(myContext);
                tvPrintKOTStatus.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("PrintKOTStatus")));

                // Add all text views and edit text to Item Row
                // rowItem.addView(tvNumber);
                rowItem.addView(Number);
                rowItem.addView(tvName);
                rowItem.addView(tvHSn);
                rowItem.addView(etQty);
                rowItem.addView(etRate);
                rowItem.addView(tvAmount);
                rowItem.addView(tvTaxPercent);
                rowItem.addView(tvTaxAmt);
                rowItem.addView(tvDiscPercent);
                rowItem.addView(tvDiscAmt);
                rowItem.addView(tvDeptCode);
                rowItem.addView(tvCategCode);
                rowItem.addView(tvKitchenCode);
                rowItem.addView(tvTaxType);
                rowItem.addView(tvModifierCharge);
                rowItem.addView(tvServiceTaxPercent);
                rowItem.addView(tvServiceTaxAmt);
                rowItem.addView(SupplyType);
                rowItem.addView(tvSpace);
                rowItem.addView(ImgDelete);
                rowItem.addView(tvSpace1);
                rowItem.addView(tvPrintKOTStatus);

                // Add row to table
                tblOrderItems.addView(rowItem, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

            } while (crsrBillItems.moveToNext());

            CalculateTotalAmount();

        } else {
            Log.d("LoadKOTItems", "No rows in cursor");
        }
    }

    private OnClickListener mListener = new OnClickListener() {

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

                            TableRow tr = (TableRow) v1.getParent();
                            TextView ItemNumber = (TextView) tr.getChildAt(0);

                            DeletedKOT objDeletedKOT = new DeletedKOT();
                            objDeletedKOT.setReason("Modified");
                            objDeletedKOT.setEmployeeId(Integer.parseInt(tvWaiterNumber.getText().toString()));
                            objDeletedKOT.setTableNumber(Integer.parseInt(tvTableNumber.getText().toString()));
                            objDeletedKOT.setSubUdfNumber(Integer.parseInt(tvSubUdfValue.getText().toString()));
                            objDeletedKOT.setTokenNumber(iTokenNumber);
                            objDeletedKOT.setTime(String.format("%tR", Time));
                            long lResult = dbBillScreen.addDeletedKOT(objDeletedKOT);
                            Log.v("VoidKOT", "Deleted KOT added, Row ID:" + lResult);
                            lResult = dbBillScreen.deleteKOTItemsByItemToken(ItemNumber.getText().toString(), iTokenNumber,
                                    Integer.parseInt(tvSubUdfValue.getText().toString()));

                            DisplayModifyKOT();

                        }
                    })
                    .show();





//            TableRow tr = (TableRow) v.getParent();
//            TextView ItemNumber = (TextView) tr.getChildAt(0);
//
//            DeletedKOT objDeletedKOT = new DeletedKOT();
//            objDeletedKOT.setReason("Modified");
//            objDeletedKOT.setEmployeeId(Integer.parseInt(tvWaiterNumber.getText().toString()));
//            objDeletedKOT.setTableNumber(Integer.parseInt(tvTableNumber.getText().toString()));
//            objDeletedKOT.setSubUdfNumber(Integer.parseInt(tvSubUdfValue.getText().toString()));
//            objDeletedKOT.setTokenNumber(iTokenNumber);
//            objDeletedKOT.setTime(String.format("%tR", Time));
//            long lResult = dbBillScreen.addDeletedKOT(objDeletedKOT);
//            Log.v("VoidKOT", "Deleted KOT added, Row ID:" + lResult);
//            lResult = dbBillScreen.deleteKOTItemsByItemToken(ItemNumber.getText().toString(), iTokenNumber,
//                    Integer.parseInt(tvSubUdfValue.getText().toString()));
//
//            DisplayModifyKOT();
            // Toast.makeText(myContext, sample.getText().toString(),
            // Toast.LENGTH_LONG).show();
        }
    };

    /*************************************************************************************************************************************
     * Updates the item stock quantity after every transaction
     *
     * @param crsrUpdateStock : Cursor containing Item detail
     * @param Quantity        : Item quantity to be subtracted from current item quantity
     *************************************************************************************************************************************/
    private void UpdateItemStock(Cursor crsrUpdateStock, float Quantity) {
        int iResult = 0;
        float fCurrentStock = 0, fNewStock = 0;

        // Get current stock of item
        fCurrentStock = crsrUpdateStock.getFloat(crsrUpdateStock.getColumnIndex("Quantity"));

        // New Stock
        fNewStock = fCurrentStock - Quantity;

        // Update new stock for item
        iResult = dbBillScreen.updateItemStock(crsrUpdateStock.getInt(crsrUpdateStock.getColumnIndex("MenuCode")),
                fNewStock);

        Log.d("UpdateItemStock", "Updated Rows:" + iResult);

    }

    /*************************************************************************************************************************************
     * Insert each bill item to bill items database table
     *************************************************************************************************************************************/
    private void InsertBillItems() {

        // Inserted Row Id in database table
        long lResult = 0;

        // Bill item object
        BillItem objBillItem;

        // Reset TotalItems count
        iTotalItems = 0;

        Cursor crsrUpdateItemStock = null;

        for (int iRow = 0; iRow < tblOrderItems.getChildCount(); iRow++) {
            objBillItem = new BillItem();

            TableRow RowBillItem = (TableRow) tblOrderItems.getChildAt(iRow);

            // Increment Total item count if row is not empty
            if (RowBillItem.getChildCount() > 0) {
                iTotalItems++;
            }

            // Bill Number
            objBillItem.setBillNumber(tvBillNumber.getText().toString());
            Log.d("InsertBillItems", "InvoiceNo:" + tvBillNumber.getText().toString());

            // richa_2012
            //BillingMode
            objBillItem.setBillingMode(String.valueOf(jBillingMode));
            Log.d("InsertBillItems", "Billing Mode :" + String.valueOf(jBillingMode));

            // Item Number
            if (RowBillItem.getChildAt(0) != null) {
                CheckBox ItemNumber = (CheckBox) RowBillItem.getChildAt(0);
                objBillItem.setItemNumber(Integer.parseInt(ItemNumber.getText().toString()));
                Log.d("InsertBillItems", "Item Number:" + ItemNumber.getText().toString());

                crsrUpdateItemStock = dbBillScreen.getItem(Integer.parseInt(ItemNumber.getText().toString()));
            }

            // Item Name
            if (RowBillItem.getChildAt(1) != null) {
                TextView ItemName = (TextView) RowBillItem.getChildAt(1);
                objBillItem.setItemName(ItemName.getText().toString());
                Log.d("InsertBillItems", "Item Name:" + ItemName.getText().toString());
            }

            if (RowBillItem.getChildAt(2) != null) {
                TextView HSN = (TextView) RowBillItem.getChildAt(2);
                objBillItem.setHSNCode(HSN.getText().toString());
                Log.d("InsertBillItems", "Item HSN:" + HSN.getText().toString());
            }

            // Quantity
            if (RowBillItem.getChildAt(3) != null) {
                EditText Quantity = (EditText) RowBillItem.getChildAt(3);
                objBillItem.setQuantity(Float.parseFloat(Quantity.getText().toString()));
                Log.d("InsertBillItems", "Quantity:" + Quantity.getText().toString());

                if (crsrUpdateItemStock!=null && crsrUpdateItemStock.moveToFirst()) {
                    // Check if item's bill with stock enabled update the stock
                    // quantity
                    Cursor billsettingCursor = dbBillScreen.getBillSetting();
                    if(billsettingCursor!= null && billsettingCursor.moveToFirst())
                    {
                        //String i = billsettingCursor.getString(billsettingCursor.getColumnIndex("BillwithStock"));
                        if (billsettingCursor.getInt(billsettingCursor.getColumnIndex("BillwithStock")) == 1) {
                            UpdateItemStock(crsrUpdateItemStock, Float.parseFloat(Quantity.getText().toString()));
                        }
                    }

                }
            }

            // Rate
            if (RowBillItem.getChildAt(4) != null) {
                EditText Rate = (EditText) RowBillItem.getChildAt(4);
                objBillItem.setValue(Float.parseFloat(Rate.getText().toString()));
                Log.d("InsertBillItems", "Rate:" + Rate.getText().toString());
            }

            // Amount
            if (RowBillItem.getChildAt(5) != null) {
                TextView Amount = (TextView) RowBillItem.getChildAt(5);
                objBillItem.setAmount(Float.parseFloat(Amount.getText().toString()));
                Log.d("InsertBillItems", "Taxable Value:" + Amount.getText().toString());
            }

            // Sales Tax %
            if (RowBillItem.getChildAt(6) != null) {
                TextView SalesTaxPercent = (TextView) RowBillItem.getChildAt(6);
                if (GSTEnable.equals("1")) {
                    String taxName = tvSalesTax.getText().toString();
                    if (taxName.equalsIgnoreCase("IGST TAX")) {
                        objBillItem.setIGSTRate((Float.parseFloat(SalesTaxPercent.getText().toString())) * 2);
                        Log.d("InsertBillItems", " IGST Tax %:" + objBillItem.getIGSTRate());
                    } else {
                        objBillItem.setCGSTRate(Float.parseFloat(SalesTaxPercent.getText().toString()));
                        Log.d("InsertBillItems", " CGST Tax %:" + SalesTaxPercent.getText().toString());
                    }

                } else {
                    objBillItem.setTaxPercent(Float.parseFloat(SalesTaxPercent.getText().toString()));
                    Log.d("InsertBillItems", "Tax %:" + SalesTaxPercent.getText().toString());
                }
            }

            // Sales Tax Amount
            if (RowBillItem.getChildAt(7) != null) {
                TextView SalesTaxAmount = (TextView) RowBillItem.getChildAt(7);
                if (GSTEnable.equals("1")) {
                    String taxName = tvSalesTax.getText().toString();
                    if (taxName.equalsIgnoreCase("IGST TAX")) {
                        objBillItem.setIGSTAmount((Float.parseFloat(SalesTaxAmount.getText().toString()) * 2));
                        Log.d("InsertBillItems", "IGST Amt:" + objBillItem.getIGSTAmount());
                    } else {
                        objBillItem.setCGSTAmount(Float.parseFloat(SalesTaxAmount.getText().toString()));
                        Log.d("InsertBillItems", "CGST Amt:" + SalesTaxAmount.getText().toString());
                    }

                } else {
                    objBillItem.setTaxAmount(Float.parseFloat(SalesTaxAmount.getText().toString()));
                    Log.d("InsertBillItems", "Tax Amt:" + SalesTaxAmount.getText().toString());
                }
            }

            // Discount %
            if (RowBillItem.getChildAt(8) != null) {
                TextView DiscountPercent = (TextView) RowBillItem.getChildAt(8);
                objBillItem.setDiscountPercent(Float.parseFloat(DiscountPercent.getText().toString()));
                Log.d("InsertBillItems", "Disc %:" + DiscountPercent.getText().toString());
            }

            // Discount Amount
            if (RowBillItem.getChildAt(9) != null) {
                TextView DiscountAmount = (TextView) RowBillItem.getChildAt(9);
                objBillItem.setDiscountAmount(Float.parseFloat(DiscountAmount.getText().toString()));
                Log.d("InsertBillItems", "Disc Amt:" + DiscountAmount.getText().toString());

               // fTotalDiscount += Float.parseFloat(DiscountAmount.getText().toString());
            }

            // Department Code
            if (RowBillItem.getChildAt(10) != null) {
                TextView DeptCode = (TextView) RowBillItem.getChildAt(10);
                objBillItem.setDeptCode(Integer.parseInt(DeptCode.getText().toString()));
                Log.d("InsertBillItems", "Dept Code:" + DeptCode.getText().toString());
            }

            // Category Code
            if (RowBillItem.getChildAt(11) != null) {
                TextView CategCode = (TextView) RowBillItem.getChildAt(11);
                objBillItem.setCategCode(Integer.parseInt(CategCode.getText().toString()));
                Log.d("InsertBillItems", "Categ Code:" + CategCode.getText().toString());
            }

            // Kitchen Code
            if (RowBillItem.getChildAt(12) != null) {
                TextView KitchenCode = (TextView) RowBillItem.getChildAt(12);
                objBillItem.setKitchenCode(Integer.parseInt(KitchenCode.getText().toString()));
                Log.d("InsertBillItems", "Kitchen Code:" + KitchenCode.getText().toString());
            }

            // Tax Type
            if (RowBillItem.getChildAt(13) != null) {
                TextView TaxType = (TextView) RowBillItem.getChildAt(13);
                objBillItem.setTaxType(Integer.parseInt(TaxType.getText().toString()));
                Log.d("InsertBillItems", "Tax Type:" + TaxType.getText().toString());
            }

            // Modifier Amount
            if (RowBillItem.getChildAt(14) != null) {
                TextView ModifierAmount = (TextView) RowBillItem.getChildAt(14);
                objBillItem.setModifierAmount(Float.parseFloat(ModifierAmount.getText().toString()));
                Log.d("InsertBillItems", "Modifier Amt:" + ModifierAmount.getText().toString());
            }

            // Service Tax Percent
            if (RowBillItem.getChildAt(15) != null) {
                TextView ServiceTaxPercent = (TextView) RowBillItem.getChildAt(15);
                if (GSTEnable.equals("1")) {
                    String taxName = tvServiceTax.getText().toString();
                    if (taxName.equalsIgnoreCase("SGST TAX")) {
                        objBillItem.setSGSTRate(Float.parseFloat(ServiceTaxPercent.getText().toString()));
                        Log.d("InsertBillItems", "SGST Tax %:" + ServiceTaxPercent.getText().toString());
                    }

                } else {
                    objBillItem.setServiceTaxPercent(Float.parseFloat(ServiceTaxPercent.getText().toString()));
                    Log.d("InsertBillItems", "Service Tax %:" + ServiceTaxPercent.getText().toString());
                }
            }

            // Service Tax Amount
            if (RowBillItem.getChildAt(16) != null) {
                TextView ServiceTaxAmount = (TextView) RowBillItem.getChildAt(16);
                if (GSTEnable.equals("1")) {
                    String taxName = tvServiceTax.getText().toString();
                    if (taxName.equalsIgnoreCase("SGST TAX")) {
                        objBillItem.setSGSTAmount(Float.parseFloat(ServiceTaxAmount.getText().toString()));
                        Log.d("InsertBillItems", "SGST Amount :" + ServiceTaxAmount.getText().toString());
                    }

                } else {

                    objBillItem.setServiceTaxAmount(Float.parseFloat(ServiceTaxAmount.getText().toString()));
                    Log.d("InsertBillItems", "Service Tax Amt:" + ServiceTaxAmount.getText().toString());
                }
            }

            if (RowBillItem.getChildAt(17) != null) {
                TextView SupplyType = (TextView) RowBillItem.getChildAt(17);
                objBillItem.setSupplyType(SupplyType.getText().toString());
                Log.d("InsertBillItems", "SupplyType:" + SupplyType.getText().toString());
                /*if (GSTEnable.equals("1")) {
                    objBillItem.setSupplyType(SupplyType.getText().toString());
                    Log.d("InsertBillItems", "SupplyType:" + SupplyType.getText().toString());
                } else {
                    objBillItem.setSupplyType("");
                }*/
            }
            if (RowBillItem.getChildAt(22) != null) {
                TextView UOM = (TextView) RowBillItem.getChildAt(22);
                objBillItem.setUom(UOM.getText().toString());
                Log.d("InsertBillItems", "UOM:" + UOM.getText().toString());

            }

            // subtotal
            float subtotal = objBillItem.getAmount() + objBillItem.getIGSTAmount() + objBillItem.getCGSTAmount() + objBillItem.getSGSTAmount();
            objBillItem.setSubTotal(subtotal);
            Log.d("InsertBillItems", "Sub Total :" + subtotal);

            // Date
            objBillItem.setInvoiceDate(String.valueOf(d.getTime()));

            // cust name
            String custname = edtCustName.getText().toString();
            objBillItem.setCustName(custname);
            Log.d("InsertBillDetail", "CustName :" + custname);

            // cust StateCode
            //String custStateCode =spnr_pos.getSelectedItem().toString();
            String str = spnr_pos.getSelectedItem().toString();
            int length = str.length();
            String custStateCode = "";
            if (length > 0) {
                custStateCode = str.substring(length - 2, length);
            }
            objBillItem.setCustStateCode(custStateCode);
            Log.d("InsertBillDetail", "CustStateCode :" + custStateCode);

            // BusinessType
            if (etCustGSTIN.getText().toString().equals("")) {
                objBillItem.setBusinessType("B2C");
            } else // gstin present means b2b bussiness
            {
                objBillItem.setBusinessType("B2B");
            }

            Log.d("InsertBillDetail", "BusinessType : " + objBillItem.getBusinessType());

            // richa to do - hardcoded b2b bussinies type
            //objBillItem.setBusinessType("B2B");
            lResult = dbBillScreen.addBillItem(objBillItem);
            Log.d("InsertBillItem", "Bill item inserted at position:" + lResult);
        }
    }

    /*************************************************************************************************************************************
     * Inserts bill details to bill detail database table
     *
     * @param TenderType : Type of tender, 1 - Pay cash, 2 - Tender Screen payment
     *************************************************************************************************************************************/
    private void InsertBillDetail(int TenderType) {

        // Inserted Row Id in database table
        long lResult = 0;

        // BillDetail object
        BillDetail objBillDetail;

        objBillDetail = new BillDetail();

        // Date
        //objBillDetail.setDate(String.valueOf(d.getTime()));
        objBillDetail.setDate(tvDate.getText().toString());
        Log.d("InsertBillDetail", "Date:" + d.getTime());

        // Time
        objBillDetail.setTime(String.format("%tR", Time));
        Log.d("InsertBillDetail", "Time:" + String.format("%tR", Time));

        // Bill Number
        objBillDetail.setBillNumber(Integer.parseInt(tvBillNumber.getText().toString()));
        Log.d("InsertBillDetail", "Bill Number:" + tvBillNumber.getText().toString());

        // richa_2012
        //BillingMode
        objBillDetail.setBillingMode(String.valueOf(jBillingMode));
        Log.d("InsertBillDetail", "Billing Mode :" + String.valueOf(jBillingMode));



        // pos
        if (chk_interstate.isChecked()) {
            String str = spnr_pos.getSelectedItem().toString();
            int length = str.length();
            String sub = "";
            if (length > 0) {
                sub = str.substring(length - 2, length);
            }
            objBillDetail.setPOS(sub);
            Log.d("InsertBillDetail", "POS :" + sub);
        } else {
            objBillDetail.setPOS("");
            Log.d("InsertBillDetail", "POS :");
        }


        // Total Items
        objBillDetail.setTotalItems(iTotalItems);
        Log.d("InsertBillDetail", "Total Items:" + iTotalItems);

        // Bill Amount
        String billamt_temp = String.format("%.2f",Float.parseFloat(tvBillAmount.getText().toString()));
        objBillDetail.setBillAmount(Float.parseFloat(billamt_temp));
        Log.d("InsertBillDetail", "Bill Amount:" + tvBillAmount.getText().toString());

        // Discount Amount
        objBillDetail.setTotalDiscountAmount(fTotalDiscount);
        Log.d("InsertBillDetail", "Total Discount:" + fTotalDiscount);

        // Sales Tax Amount
        if (GSTEnable.equals("1")) {
            if (chk_interstate.isChecked()) {
                objBillDetail.setIGSTAmount(Float.parseFloat(tvTaxTotal.getText().toString()));
                objBillDetail.setCGSTAmount(0);
                objBillDetail.setSGSTAmount(0);
            } else {
                objBillDetail.setIGSTAmount(0);
                objBillDetail.setCGSTAmount(Float.parseFloat(tvTaxTotal.getText().toString()));
                objBillDetail.setSGSTAmount(Float.parseFloat(tvServiceTaxTotal.getText().toString()));
            }
            Log.d("InsertBillDetail", "IGSTAmount : " + objBillDetail.getIGSTAmount());
            Log.d("InsertBillDetail", "CGSTAmount : " + objBillDetail.getCGSTAmount());
            Log.d("InsertBillDetail", "SGSTAmount : " + objBillDetail.getSGSTAmount());
        } else {
            objBillDetail.setTotalTaxAmount(Float.parseFloat(tvTaxTotal.getText().toString()));
            Log.d("InsertBillDetail", "Total Tax:" + tvTaxTotal.getText().toString());

            // Service Tax Amount
            objBillDetail.setTotalServiceTaxAmount(Float.parseFloat(tvServiceTaxTotal.getText().toString()));
            Log.d("InsertBillDetail", "Service Tax:" + tvServiceTaxTotal.getText().toString());

        }


        // Delivery Charge
        objBillDetail.setDeliveryCharge(Float.parseFloat(txtOthercharges.getText().toString()));
        Log.d("InsertBillDetail", "Delivery Charge:0");


        // Taxable Value
        float taxval_f = Float.parseFloat(tvSubTotal.getText().toString());
        objBillDetail.setAmount(String.valueOf(taxval_f));
        Log.d("InsertBillDetail", "Taxable Value:" + taxval_f);

        float cgstamt_f = 0, sgstamt_f = 0;
        if (tvTaxTotal.getText().toString().equals("") == false) {
            cgstamt_f = Float.parseFloat(tvTaxTotal.getText().toString());
        }
        if (tvServiceTaxTotal.getText().toString().equals("") == false) {
            sgstamt_f = Float.parseFloat(tvServiceTaxTotal.getText().toString());
        }


        float subtot_f = taxval_f + cgstamt_f + sgstamt_f;
        objBillDetail.setSubTotal(subtot_f);
        Log.d("InsertBillDetail", "Sub Total :" + subtot_f);

        // cust name
        String custname = edtCustName.getText().toString();
        objBillDetail.setCustname(custname);
        Log.d("InsertBillDetail", "CustName :" + custname);

        // cust StateCode
        //String custStateCode =spnr_pos.getSelectedItem().toString();
        String str = spnr_pos.getSelectedItem().toString();
        int length = str.length();
        String custStateCode = "";
        if (length > 0) {
            custStateCode = str.substring(length - 2, length);
        }
        objBillDetail.setCustStateCode(custStateCode);
        Log.d("InsertBillDetail", "CustStateCode :" + custStateCode);

        // BusinessType
        if (etCustGSTIN.getText().toString().equals("")) {
            objBillDetail.setBusinessType("B2C");
        } else // gstin present means b2b bussiness
        {
            objBillDetail.setBusinessType("B2B");
        }
        //objBillDetail.setBusinessType("B2C");
        Log.d("InsertBillDetail", "BusinessType : " + objBillDetail.getBusinessType());
        // Payment types
        if (TenderType == 1) {
            // Cash Payment
            objBillDetail.setCashPayment(Float.parseFloat(tvBillAmount.getText().toString()));
            Log.d("InsertBillDetail", "Cash:" + tvBillAmount.getText().toString());

            // Card Payment
            objBillDetail.setCardPayment(fCardPayment);
            Log.d("InsertBillDetail", "Card:" + fCardPayment);

            // Coupon Payment
            objBillDetail.setCouponPayment(fCouponPayment);
            Log.d("InsertBillDetail", "Coupon:" + fCouponPayment);

            // PettyCash Payment
            objBillDetail.setPettyCashPayment(fPettCashPayment);
            Log.d("InsertBillDetail", "PettyCash:" + fPettCashPayment);

            // Wallet Payment
            objBillDetail.setWalletAmount(fWalletPayment);
            Log.d("InsertBillDetail", "Wallet:" + fWalletPayment);

            // PaidTotal Payment
            objBillDetail.setPaidTotalPayment(fPaidTotalPayment);

            // Change Payment
            objBillDetail.setChangePayment(fChangePayment);

        } else if (TenderType == 2) {

            if (PrintBillPayment == 1) {
                // Cash Payment
                objBillDetail.setCashPayment(Float.parseFloat(tvBillAmount.getText().toString()));
                Log.d("InsertBillDetail", "Cash:" + Float.parseFloat(tvBillAmount.getText().toString()));

                // Card Payment
                objBillDetail.setCardPayment(fCardPayment);
                Log.d("InsertBillDetail", "Card:" + fCardPayment);

                // Coupon Payment
                objBillDetail.setCouponPayment(fCouponPayment);
                Log.d("InsertBillDetail", "Coupon:" + fCouponPayment);

                // PettyCash Payment
                objBillDetail.setPettyCashPayment(fPettCashPayment);
                Log.d("InsertBillDetail", "PettyCash:" + fPettCashPayment);

                // Wallet Payment
                objBillDetail.setWalletAmount(fWalletPayment);
                Log.d("InsertBillDetail", "Wallet:" + fWalletPayment);

                // PaidTotal Payment
                objBillDetail.setPaidTotalPayment(Float.parseFloat(tvBillAmount.getText().toString()));

                // Change Payment
                objBillDetail.setChangePayment(fChangePayment);
            } else {
                // Cash Payment
                objBillDetail.setCashPayment(fCashPayment);
                Log.d("InsertBillDetail", "Cash:" + fCashPayment);

                // Card Payment
                objBillDetail.setCardPayment(fCardPayment);
                Log.d("InsertBillDetail", "Card:" + fCardPayment);

                // Coupon Payment
                objBillDetail.setCouponPayment(fCouponPayment);
                Log.d("InsertBillDetail", "Coupon:" + fCouponPayment);

                // PettyCash Payment
                objBillDetail.setPettyCashPayment(fPettCashPayment);
                Log.d("InsertBillDetail", "PettyCash:" + fPettCashPayment);

                // Wallet Payment
                objBillDetail.setWalletAmount(fWalletPayment);
                Log.d("InsertBillDetail", "Wallet:" + fWalletPayment);

                // PaidTotal Payment
                objBillDetail.setPaidTotalPayment(fPaidTotalPayment);

                // Change Payment
                objBillDetail.setChangePayment(fChangePayment);
            }
        }

        // Reprint Count
        objBillDetail.setReprintCount(0);
        Log.d("InsertBillDetail", "Reprint Count:0");

        // Bill Status
        if (jBillingMode == 4) {
            objBillDetail.setBillStatus(2);
            Log.d("InsertBillDetail", "Bill Status:2");
        } else {
            objBillDetail.setBillStatus(1);
            Log.d("InsertBillDetail", "Bill Status:1");
        }

        // Employee Id (Waiter / Rider)
        if (jBillingMode == 1 ) {
            objBillDetail.setEmployeeId(Integer.parseInt(tvWaiterNumber.getText().toString()));
            Log.d("InsertBillDetail", "EmployeeId:" + tvWaiterNumber.getText().toString());
        } else {
            objBillDetail.setEmployeeId(0);
            Log.d("InsertBillDetail", "EmployeeId:0");
        }

        // Customer Id
        objBillDetail.setCustId(Integer.valueOf(edtCustId.getText().toString()));
        Log.d("InsertBillDetail", "Customer Id:" + Integer.valueOf(edtCustId.getText().toString()));

        // User Id
        objBillDetail.setUserId(strUserId);
        Log.d("InsertBillDetail", "UserID:" + strUserId);

        lResult = dbBillScreen.addBill(objBillDetail, etCustGSTIN.getText().toString());
        Log.d("InsertBill", "Bill inserted at position:" + lResult);
        //lResult = dbBillScreen.updateBill(objBillDetail);

        if (String.valueOf(iCustId).equalsIgnoreCase("") || String.valueOf(iCustId).equalsIgnoreCase("0"))
        {
        }
        else
        {
            float fTotalTransaction = dbBillScreen.getCustomerTotalTransaction(iCustId);
            float fCreditAmount = dbBillScreen.getCustomerCreditAmount(iCustId);
            //fCreditAmount = fCreditAmount - Float.parseFloat(tvBillAmount.getText().toString());
            fCreditAmount = fCreditAmount - fPettCashPayment;
            fTotalTransaction += Float.parseFloat(tvBillAmount.getText().toString());

            long lResult1 = dbBillScreen.updateCustomerTransaction(iCustId, Float.parseFloat(tvBillAmount.getText().toString()), fTotalTransaction, fCreditAmount);
        }
        // Bill No Reset Configuration
        long Result2 = dbBillScreen.UpdateBillNoResetInvoiceNo(Integer.parseInt(tvBillNumber.getText().toString()));
    }

    /*************************************************************************************************************************************
     * Invokes InsertBillItems() and InsertBillDetail() functions
     *
     * @param TenderType  : Type of tender, 1 - Pay cash, 2 - Tender Screen payment
     * @param isPrintBill : True if bill needs to be printed else False
     *************************************************************************************************************************************/
    private void l(int TenderType, boolean isPrintBill) { // TenderType:
        // 1=PayCash
        // 2=Tender

        // Insert all bill items to database
        InsertBillItems();

        // Insert bill details to database
        InsertBillDetail(TenderType);

        /*if (isPrintBill) {
            // Print bill
            PrintBill();
        }*/
    }

    /*************************************************************************************************************************************
     * Updates complimentary bill details in database
     *
     * @param BillNumber          : Complimentary bill number
     * @param PaidAmount          : Amount paid for the complimentary bill
     * @param ComplimentaryReason : Reason for giving complimentary bill
     *************************************************************************************************************************************/
    private void SaveComplimentaryBill(int BillNumber, float PaidAmount, String ComplimentaryReason) {
        long lResult = 0;

        ComplimentaryBillDetail objComplimentaryBillDetail = new ComplimentaryBillDetail();

        // Set bill number
        objComplimentaryBillDetail.setBillNumber(BillNumber);

        // Set complimentary reason
        objComplimentaryBillDetail.setComplimentaryReason(ComplimentaryReason);

        // Set paid amount
        objComplimentaryBillDetail.setPaidAmount(PaidAmount);

        lResult = dbBillScreen.addComplimentaryBillDetail(objComplimentaryBillDetail);

        Log.v("SaveComplimentaryBill", "Complimentary Bill inserted at Row:" + lResult);
    }

    /*************************************************************************************************************************************
     * Gets quantity for item from weighing scale
     *
     * @return Returns Weighing scale reading represented by string
     *************************************************************************************************************************************/
    private String getQuantityFromWeighScale() {
        // Read data from weigh scale through serial port

        // Convert data to string and return the value to calling function

        return "1";
    }

    /*************************************************************************************************************************************
     * Opens tender window in dine in and take away billing mode
     *************************************************************************************************************************************/
    private void Tender() {
        if (jBillingMode == 2 && Double.parseDouble(tvBillAmount.getText().toString()) <= 0) {
            MsgBox.Show("Warning", "Empty bill can not be tendered");
            return;
        }

        if (jBillingMode == Byte.parseByte("1")) {
            DinInTender();

        } else if (jBillingMode == Byte.parseByte("2")) {

            Intent intTender = new Intent(myContext, PayBillActivity.class);
            Log.v("Debug", "Total Amount:" + tvBillAmount.getText().toString());
            intTender.putExtra("TotalAmount", tvBillAmount.getText().toString());
            if (jBillingMode == 1) {
                intTender.putExtra("phone", edtCustDineInPhoneNo.getText().toString());
            } else {
                intTender.putExtra("phone", edtCustPhoneNo.getText().toString());
            }
            intTender.putExtra("USER_NAME", strUserName);
            startActivityForResult(intTender, 1);

        } else if (jBillingMode == Byte.parseByte("3")) {

            Intent intTender = new Intent(myContext, PayBillActivity.class);
            intTender.putExtra("TotalAmount", tvBillAmount.getText().toString());
            intTender.putExtra("CustId", String.valueOf(iCustId));
            if (jBillingMode == 1) {
                intTender.putExtra("phone", edtCustDineInPhoneNo.getText().toString());
            } else {
                intTender.putExtra("phone", edtCustPhoneNo.getText().toString());
            }
            intTender.putExtra("USER_NAME", strUserName);
            startActivityForResult(intTender, 1);

        } else if (jBillingMode == Byte.parseByte("4")) {

            Cursor crsrBillDetail = dbBillScreen.getBillDetailByCustomer(iCustId, 2, Float.parseFloat(tvBillAmount.getText().toString()));
            if (crsrBillDetail.moveToFirst()) {
                strPaymentStatus = "Paid";
                PrintNewBill();
            } else {
                l(2, true);
                //strPaymentStatus = "Pay";
                PrintNewBill();
            }

            int iResult = dbBillScreen.deleteKOTItems(iCustId, String.valueOf(jBillingMode));
            Log.d("Delivery:", "Items deleted from pending KOT:" + iResult);

            ClearAll();
            Close(null);
        }
    }

    /*************************************************************************************************************************************
     * Calculates all the amount after giving overall discount in tender window
     * and updates the new values in text boxes
     *
     * @param dDiscountPercent : Discount percent
     *************************************************************************************************************************************/
    private void OverAllDiscount(double dDiscountPercent) {
        double dRate = 0, dTaxPercent = 0, dTaxAmt = 0, dDiscAmt = 0, dTempAmt = 0;
        TableRow rowItem;
        TextView DiscAmt, DiscPercent, Qty, Rate, TaxAmt, TaxPercent, TaxType;

        for (int i = 0; i < tblOrderItems.getChildCount(); i++) {

            // Get Item row
            rowItem = (TableRow) tblOrderItems.getChildAt(i);
            if (rowItem.getChildAt(0) != null) {
                // Get Discount percent
                Qty = (TextView) rowItem.getChildAt(3);
                Rate = (TextView) rowItem.getChildAt(4);
                DiscPercent = (TextView) rowItem.getChildAt(8);
                DiscAmt = (TextView) rowItem.getChildAt(9);
                TaxPercent = (TextView) rowItem.getChildAt(6);
                TaxAmt = (TextView) rowItem.getChildAt(7);
                TaxType = (TextView) rowItem.getChildAt(13);
                DiscPercent.setText(String.format("%.2f", dDiscountPercent));

                dRate = Double.parseDouble(Rate.getText().toString());
                dTaxPercent = Double.parseDouble(TaxPercent.getText().toString());

                if (TaxType.getText().toString().equalsIgnoreCase("1")) {
                    // Discount
                    dDiscAmt = dRate * (dDiscountPercent / 100);
                    dTempAmt = dDiscAmt;
                    dDiscAmt = dDiscAmt * Double.parseDouble(Qty.getText().toString());

                    // Tax
                    dTaxAmt = (dRate - dTempAmt) * (dTaxPercent / 100);
                    dTaxAmt = dTaxAmt * Double.parseDouble(Qty.getText().toString());

                    TaxAmt.setText(String.format("%.2f", dTaxAmt));
                    DiscAmt.setText(String.format("%.2f", dDiscAmt));

                } else {
                    double dBasePrice = 0;
                    dBasePrice = dRate / (1 + (dTaxPercent / 100));

                    // Discount
                    dDiscAmt = dBasePrice * (dDiscountPercent / 100);
                    dTempAmt = dDiscAmt;
                    dDiscAmt = dDiscAmt * Double.parseDouble(Qty.getText().toString());

                    // Tax
                    dTaxAmt = (dBasePrice - dTempAmt) * (dTaxPercent / 100);
                    dTaxAmt = dTaxAmt * Double.parseDouble(Qty.getText().toString());

                    TaxAmt.setText(String.format("%.2f", dTaxAmt));
                    DiscAmt.setText(String.format("%.2f", dDiscAmt));
                }
            }
        }

        CalculateTotalAmount();
    }


    /*************************************************************************************************************************************
     * Delete Button Click event, calls DeleteItemByMenuCode() function
     *
     * @param v : Clicked Button
     *************************************************************************************************************************************/
    public void Delete(View v) {
        DeleteItem();
    }

    /*************************************************************************************************************************************
     * Clear Button Click event, calls ClearAll() function
     *
     * @param v : Clicked Button
     *************************************************************************************************************************************/
    public void Clear(View v) {
        ClearAll();
        ResetCustomer();
        /*if (jBillingMode == 3) {
            ControlsSetDisabled();
        }*/
        if (jBillingMode == 4 || jBillingMode == 3 || jBillingMode==1) {
            btnPayBill.setEnabled(false);
            btnPrintBill.setEnabled(false);
            //btnClear.setEnabled(false);
           // ControlsSetDisabled();
        }

        if (crsrSettings.getString(crsrSettings.getColumnIndex("FastBillingMode")).equalsIgnoreCase("1")) {
            grdItems.setNumColumns(6);
            GetItemDetails();
            tvdeptline.setVisibility(View.GONE);
            tvcategline.setVisibility(View.GONE);
            lstvwDepartment.setVisibility(View.GONE);
            lstvwCategory.setVisibility(View.GONE);
        } else if (crsrSettings.getString(crsrSettings.getColumnIndex("FastBillingMode")).equalsIgnoreCase("2")) {
            grdItems.setNumColumns(4);
            GetItemDetailswithoutDeptCateg();
            tvcategline.setVisibility(View.GONE);
            lstvwCategory.setVisibility(View.GONE);
            lstvwDepartment.setAdapter(null);
            grdItems.setAdapter(null);
        } else {
            GetItemDetailswithoutDeptCateg();

            lstvwDepartment.setAdapter(null);
            lstvwCategory.setAdapter(null);
            grdItems.setAdapter(null);
        }
    }

    /*************************************************************************************************************************************
     * Load KOT Button Click event, opens Load KOT activity only if KOT is
     * pending
     *
     * @param v : Clicked Button
     *************************************************************************************************************************************/
    public void LoadKOT(View v) {
        Cursor KOT = dbBillScreen.getOccupiedTables();
        if (KOT.moveToFirst()) {
            do {
                Log.d("OccupiedTables", "Table Number:" + KOT.getString(0) + " SubUdf Number:" + KOT.getString(1));
            } while (KOT.moveToNext());
            ClearAll(); // To avoid adding item to existing item in table layout
            Intent intentShiftTable = new Intent(myContext, TableShiftMergeActivity.class);
            intentShiftTable.putExtra("SHIFT_MERGE", 3);
            intentShiftTable.putExtra("USER_NAME", strUserName);
            startActivityForResult(intentShiftTable, 4);

        } else {
            MsgBox.Show("Warning", "No occupied tables");
        }
    }

    /*************************************************************************************************************************************
     * Modify KOT Button Click event, opens Load KOT activity only if KOT is
     * pending
     *
     * @param v : Clicked Button
     *************************************************************************************************************************************/
    public void ModifyKOT(View v) {
        if (tblOrderItems.getChildCount() > 0) {
            DisplayModifyKOT();
        } else {
            MsgBox.Show("", "No Items Found in KOT");
        }
    }

    private void DisplayModifyKOT() {
        tblOrderItems.removeAllViews();
        String strTableNumber = tvTableNumber.getText().toString();
        String strTableSplitNo = tvTableSplitNo.getText().toString();
        // String strSubUdfNumber = data.getStringExtra("SUB_UDF_NUMBER");
        Log.v("Load Modify KOT", "TableNumber:" + strTableNumber);
        Cursor LoadModifyKOT = null;
        if(jBillingMode==1)
            LoadModifyKOT = dbBillScreen.getKOTItems(Integer.parseInt(strTableNumber), 1, Integer.parseInt(strTableSplitNo));
        else
            LoadModifyKOT =dbBillScreen.getKOTItems(iCustId, String.valueOf(jBillingMode));

        if (LoadModifyKOT.moveToFirst()) {
            LoadModifyKOTItems(LoadModifyKOT);
            // btnDeleteKOT.setEnabled(true);
        } else {
            Log.v("Load Modify KOT", "ERROR: No items found");
            MsgBox.Show("Warning", "No Items Found in KOT");
        }
    }

    /*************************************************************************************************************************************
     * Void KOT Button Click event, prompts a dialog to enter reason for voiding
     * KOT and saves the reason in database and deletes the KOT
     *
     * @param v : Clicked Button
     *************************************************************************************************************************************/
    public void VoidKOT(View v) {
        AlertDialog.Builder VoidKOT = new AlertDialog.Builder(myContext);
        final EditText txtReason = new EditText(myContext);

        VoidKOT.setTitle("Void KOT").setMessage("Please enter reason for voiding KOT").setView(txtReason)
                .setNegativeButton("Cancel", null).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                if (txtReason.getText().toString().equalsIgnoreCase("")) {
                    MsgBox.Show("Warning", "Enter reason before voiding KOT");
                } else {
                    // Calendar now = Calendar.getInstance();
                    DeletedKOT objDeletedKOT = new DeletedKOT();
                    objDeletedKOT.setReason(txtReason.getText().toString());
                    objDeletedKOT.setEmployeeId(Integer.parseInt(tvWaiterNumber.getText().toString()));
                    objDeletedKOT.setTableNumber(Integer.parseInt(tvTableNumber.getText().toString()));
                    objDeletedKOT.setSubUdfNumber(Integer.parseInt(tvSubUdfValue.getText().toString()));
                    objDeletedKOT.setTokenNumber(iTokenNumber);
                    String strTime = String.format("%tR", Time);
                    objDeletedKOT.setTime(strTime);
                    long lResult = dbBillScreen.addDeletedKOT(objDeletedKOT);
                    Log.v("VoidKOT", "Deleted KOT added, Row ID:" + lResult);
                    lResult = dbBillScreen.deleteKOTItems(Integer.parseInt(tvTableNumber.getText().toString()),
                            Integer.parseInt(tvSubUdfValue.getText().toString()), Integer.parseInt(tvTableSplitNo.getText().toString()));
                    Log.v("VoidKOT", "KOT items deleted, Rows Deleted:" + lResult);
                    ClearAll();
                }
            }
        }).show();
    }

    /*************************************************************************************************************************************
     * Pay Cash Button Click event, directly saves and prints the bill without
     * opening Tender dialog
     *
     * @param v : Clicked Button
     *************************************************************************************************************************************/
    public void PayCash(View v) {
        if (tblOrderItems.getChildCount() > 0) {
            l(1, true);
            if (jBillingMode == 1) {
                int iResult = dbBillScreen.deleteKOTItems(Integer.parseInt(tvTableNumber.getText().toString()),
                        Integer.parseInt(tvSubUdfValue.getText().toString()), Integer.parseInt(tvTableSplitNo.getText().toString()));
                Log.d("Pay Cash", "Items deleted from pending KOT:" + iResult);
            }
            ClearAll();
        } else {
            MsgBox.Show("Warning", "No item in bill");
        }
    }

    /*************************************************************************************************************************************
     * Close Button Click event, closes database connection and finishes the
     * bill screen activity
     *
     * @param v : Clicked Button
     *************************************************************************************************************************************/
    public void Close(View v) {

        // Close Database Connection
        dbBillScreen.CloseDatabase();

        // finish the activity
        this.finish();
    }

    /*************************************************************************************************************************************
     * Split Bill Button Click event, opens split bill window by loading present
     * bill items in split bill window
     *
     * @param v : Clicked Button
     *************************************************************************************************************************************/
    public void SplitBill(View v) {
        try {
            String UserName, TableNumber;
            //UserName = tvUserName.getText().toString();
            TableNumber = tvTableNumber.getText().toString();

            Intent intentSplitBill = new Intent(myContext, SplitBillActivity.class);
            intentSplitBill.putExtra("USER_ID", strUserId);
            intentSplitBill.putExtra("USER_NAME", strUserName);
            intentSplitBill.putExtra("BILL_DATE", strDate);
            intentSplitBill.putExtra("BILL_NUMBER", tvBillNumber.getText().toString());
            intentSplitBill.putExtra("TABLE_NUMBER", TableNumber);
            intentSplitBill.putExtra("TABLE_SPLIT_NO", tvTableSplitNo.getText().toString());
            intentSplitBill.putExtra("UDF_NUMBER", "1");// tvSubUdfValue.getText());
            intentSplitBill.putExtra("SERVICETAX_TYPE", String.valueOf(iTaxType));
            intentSplitBill.putExtra("SERVICETAX_PERCENT", String.valueOf(dServiceTaxPercent));
            startActivity(intentSplitBill);
            ClearAll();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*************************************************************************************************************************************
     * Tender Button Click event, opens tender dialog for bill tender process in
     * dine in billing mode
     *
     * @param v : Clicked Button
     *************************************************************************************************************************************/
    public void Tender(View v) {

        if (tvBillAmount.getText().toString().equals("") || tvBillAmount.getText().toString().equals("0.00")) {
            MsgBox.Show("Warning", "Please add item to make bill");
        } else if (chk_interstate.isChecked() && spnr_pos.getSelectedItem().equals("")) {
            MsgBox.Show("Warning", "Please Select Code for Intersate Supply");
        }
        /*else if (jBillingMode== 4 && strPaymentStatus!= null && strPaymentStatus.equalsIgnoreCase("Paid"))
        { // richa to prevent from repaying the bill in delivery mode
            MsgBox.Show("Warning", "Bill is already paid");
        }*/

        else {
            Intent intentTender = new Intent(myContext, PayBillActivity.class);
            intentTender.putExtra("TotalAmount", tvBillAmount.getText().toString());
            intentTender.putExtra("CustId", edtCustId.getText().toString());
            if (jBillingMode == 1) {
                intentTender.putExtra("phone", edtCustDineInPhoneNo.getText().toString());
            } else {
                intentTender.putExtra("phone", edtCustPhoneNo.getText().toString());
            }
            intentTender.putExtra("USER_NAME", strUserName);
            startActivityForResult(intentTender, 1);
        }
    }


    /*************************************************************************************************************************************
     * Delete Bill Button Click event, calls delte bill function
     *
     * @param v : Clicked Button
     *************************************************************************************************************************************/
    public void DeleteBill(View v) {

        //DeleteVoid(Byte.parseByte("1"));
        tblOrderItems.removeAllViews();

        AlertDialog.Builder DineInTenderDialog = new AlertDialog.Builder(myContext);

        LayoutInflater UserAuthorization = (LayoutInflater) myContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vwAuthorization = UserAuthorization.inflate(R.layout.dinein_reprint, null);

        final EditText txtReprintBillNo = (EditText) vwAuthorization.findViewById(R.id.txtDineInReprintBillNumber);

        DineInTenderDialog.setIcon(R.drawable.ic_launcher).setTitle("Delete Bill").setMessage("Enter Bill Number")
                .setView(vwAuthorization).setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        if (txtReprintBillNo.getText().toString().equalsIgnoreCase("")) {
                            MsgBox.Show("Warning", "Please enter Bill Number");
                            return;
                        } else {
                            String InvoiceNo = txtReprintBillNo.getText().toString();
                            Cursor result = dbBillScreen.getBillDetail(Integer.parseInt(InvoiceNo));

                            if (result.moveToFirst()) {
                                if (result.getInt(result.getColumnIndex("BillStatus")) != 0) {
                                    VoidBill(Integer.parseInt(InvoiceNo));
                                } else {

                                    Toast.makeText(myContext, "Bill is already voided", Toast.LENGTH_SHORT).show();
                                    String msg = "Bill Number "+InvoiceNo+ " is already voided";
                                    //MsgBox.Show("VoidBill",msg);
                                    Log.d("VoidBill",msg);
                                }
                            } else {
                                Toast.makeText(myContext, "No bill found with bill number " + InvoiceNo, Toast.LENGTH_SHORT).show();
                                String msg = "No bill found with bill number " + InvoiceNo;
                                //MsgBox.Show("VoidBill",msg);
                                Log.d("VoidBill",msg);
                            }
                            ClearAll();

                        }
                    }
                }).show();
    }

    /*************************************************************************************************************************************
     * Reprint Bill Button Click event, calls reprint bill function
     *
     * @param v : Clicked Button
     *************************************************************************************************************************************/
    public void ReprintBill(View v) {

        //ReprintVoid(Byte.parseByte("1"));
        tblOrderItems.removeAllViews();

        AlertDialog.Builder DineInTenderDialog = new AlertDialog.Builder(myContext);

        LayoutInflater UserAuthorization = (LayoutInflater) myContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vwAuthorization = UserAuthorization.inflate(R.layout.dinein_reprint, null);

        final EditText txtReprintBillNo = (EditText) vwAuthorization.findViewById(R.id.txtDineInReprintBillNumber);

        DineInTenderDialog.setIcon(R.drawable.ic_launcher).setTitle("Print Bill").setMessage("Enter Bill Number")
                .setView(vwAuthorization).setNegativeButton("Cancel", null)
                .setPositiveButton("RePrint", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        if (txtReprintBillNo.getText().toString().equalsIgnoreCase("")) {
                            MsgBox.Show("Warning", "Please enter Bill Number");
                            return;
                        } else {

                            Cursor LoadItemForReprint = dbBillScreen.getItemsForReprintBill(Integer.valueOf(txtReprintBillNo.getText().toString()));
                            if (LoadItemForReprint.moveToFirst()) {
                                tvBillNumber.setText(txtReprintBillNo.getText().toString());
                                LoadItemsForReprintBill(LoadItemForReprint);
                                Cursor crsrBillDetail = dbBillScreen.getBillDetail(Integer.valueOf(txtReprintBillNo.getText().toString()));
                                if (crsrBillDetail.moveToFirst()) {
                                    edtCustId.setText(crsrBillDetail.getString(crsrBillDetail.getColumnIndex("CustId")));
                                }
                            } else {
                                MsgBox.Show("Warning",
                                        "No Item is present for the Bill Number " + txtReprintBillNo.getText().toString());
                            }
                            strPaymentStatus = "Paid";
                            PrintNewBill();
                            // update bill reprint count
                            int Result = dbBillScreen
                                    .updateBillRepintCount(Integer.parseInt(txtReprintBillNo.getText().toString()));
                            ClearAll();

                        }
                    }
                }).show();
    }

    /*************************************************************************************************************************************
     * Void Bill Button Click event, opens a dialog to enter admin user id and
     * password for voiding bill if user is admin then bill will be voided
     *
     *************************************************************************************************************************************/
    public void VoidBill(final int invoiceno) {

        AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);

        LayoutInflater UserAuthorization = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vwAuthorization = UserAuthorization.inflate(R.layout.user_authorization, null);

        final EditText txtUserId = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserId);
        final EditText txtPassword = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserPassword);

        AuthorizationDialog.setTitle("Authorization").setIcon(R.drawable.ic_launcher).setView(vwAuthorization)
                .setNegativeButton("Cancel", null).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Cursor User = dbBillScreen.getUser(txtUserId.getText().toString(),
                        txtPassword.getText().toString());
                if (User.moveToFirst()) {
                    if (User.getInt(User.getColumnIndex("RoleId")) == 1) {
                        //ReprintVoid(Byte.parseByte("2"));
                        int result = dbBillScreen.makeBillVoid(invoiceno);
                        if(result >0)
                        {
                            String msg = "Bill Number "+invoiceno+" voided successfully";
                           // MsgBox.Show("Warning", msg);
                            Toast.makeText(myContext, msg, Toast.LENGTH_SHORT).show();
                            Log.d("VoidBill", msg);
                        }
                    } else {
                        MsgBox.Show("Warning", "Void Bill failed due to in sufficient access privilage");
                    }
                } else {
                    MsgBox.Show("Warning", "Void Bill failed due to wrong user id or password");
                }
            }
        }).show();
    }


    /*************************************************************************************************************************************
     * Shift table Button Click event, opens shift table dialog
     *
     * @param v : Clicked Button
     *************************************************************************************************************************************/
    public void ShiftTable(View v) {

        Cursor KOT = dbBillScreen.getOccupiedTables();
        if (KOT.moveToFirst()) {
            do {
                Log.d("OccupiedTables", "Table Number:" + KOT.getString(0) + " SubUdf Number:" + KOT.getString(1));
            } while (KOT.moveToNext());
            ClearAll();
            Intent intentShiftTable = new Intent(myContext, TableShiftMergeActivity.class);
            intentShiftTable.putExtra("SHIFT_MERGE", 1);
            intentShiftTable.putExtra("USER_NAME", strUserName);
            startActivity(intentShiftTable);

        } else {
            MsgBox.Show("Warning", "No occupied tables");
            Log.d("OccupiedTables", "No rows returned from database");
        }
    }

    /*************************************************************************************************************************************
     * Starts TableShiftMerge activity if and only if pending KOT is present in
     * database
     *
     * @param v : Merge Table button
     ************************************************************************************************************************************/
    public void MergeTable(View v) {

        Cursor KOT = dbBillScreen.getOccupiedTables();
        if (KOT.moveToFirst()) {
            do {
                Log.d("OccupiedTables", "Table Number:" + KOT.getString(0) + " SubUdf Number:" + KOT.getString(1));
            } while (KOT.moveToNext());
            ClearAll();
            Intent intentShiftTable = new Intent(myContext, TableShiftMergeActivity.class);
            intentShiftTable.putExtra("SHIFT_MERGE", 2);
            intentShiftTable.putExtra("USER_NAME", strUserName);
            startActivity(intentShiftTable);

        } else {
            MsgBox.Show("Warning", "No occupied tables");
            Log.d("OccupiedTables", "No rows returned from database");
        }
    }

    /*************************************************************************************************************************************
     * Starts Table activity for table and waiter selection
     *
     * @param v :Table / Waiter buttons present Table - Waiter row in dine in
     *          screen
     ************************************************************************************************************************************/
    public void Table_Waiter(View v) {
        //startActivityForResult(new Intent(myContext, TableActivity.class), 2);
        Intent intentDineIn = new Intent(myContext, TableActivity.class);
        intentDineIn.putExtra("BILLING_MODE", String.valueOf(jBillingMode));
        intentDineIn.putExtra("USER_ID", strUserId);//spUser.getString("USER_ID", "GHOST"));
        intentDineIn.putExtra("USER_NAME", strUserName);//spUser.getString("USER_NAME", "GHOST"));
        intentDineIn.putExtra("CUST_ID", 0);
        startActivity(intentDineIn);
        this.finish();
    }

    /*************************************************************************************************************************************
     * Invokes saveOrder function to save order if order is new else updates the
     * order in database
     *
     * @param v : SaveOrder button
     ************************************************************************************************************************************/
    public void SaveKOT(View v) {
        iPrintKOTStatus = 1;
        int i = SaveKOT();
        Log.d("Billing Activity", "SaveKOT : Status = "+i );
        if(i>0)
        {
            btnPayBill.setEnabled(true);
            btnPrintBill.setEnabled(true);
        }
        else
        {
            btnPayBill.setEnabled(false);
            btnPrintBill.setEnabled(false);
        }
        //PrintKOT();
    }

    public int  SaveKOT() {
        int returnStatus =1;
        if (jBillingMode == 1) {
            String tableno = tvTableNumber.getText().toString();
            String waiterno = tvWaiterNumber.getText().toString();
            if (tableno.equalsIgnoreCase("") || waiterno.equalsIgnoreCase("") || tableno.equalsIgnoreCase("0") || waiterno.equalsIgnoreCase("0")) {
                MsgBox.Show("Warning", "Select Table and Waiter before saving order");
            } else {
                if (tblOrderItems.getChildCount() < 1) {
                    MsgBox.Show("Warning", "Add Item before Saving KOT");

                } else {
                    int iResult = 0;
                    Cursor crsrKOTNo = dbBillScreen.getKOTItems(Integer.parseInt(tvTableNumber.getText().toString()),
                            Integer.parseInt(tvSubUdfValue.getText().toString()), Integer.parseInt(tvTableSplitNo.getText().toString()));
                    if(crsrKOTNo.moveToFirst())
                    {
                        iKOTNo = crsrKOTNo.getInt(crsrKOTNo.getColumnIndex("TokenNumber"));
                    } else {
                        iKOTNo = dbBillScreen.getKOTNo();
                        long iResult1 = dbBillScreen.updateKOTNo(iKOTNo);
                    }
                    // Delete the KOT items from Pending KOT if KOT is updated
                    iResult = dbBillScreen.deleteKOTItems(Integer.parseInt(tvTableNumber.getText().toString()),
                            Integer.parseInt(tvSubUdfValue.getText().toString()), Integer.parseInt(tvTableSplitNo.getText().toString()));

                    Log.v("KOT Update", "Deleted rows:" + iResult);
                    InsertKOTItems(iKOTNo);
                    Toast.makeText(myContext, "KOT Saved Successfully", Toast.LENGTH_LONG).show();

                    btnPrintKOT.setEnabled(true);
                    btnPrintBill.setEnabled(true);
                    // Delete Table Booking
                    int Result = dbBillScreen.DeleteTableBooking(tvTableNumber.getText().toString());
                    //ClearAll();
                    btnPayBill.setEnabled(true);
                }
            }
        } else if (jBillingMode == 2) {
            if (tblOrderItems.getChildCount() < 1) {
                MsgBox.Show("Warning", "Add Item before Saving KOT");

            } else {
                int iResult = 0;
                Cursor crsrKOTNo = dbBillScreen.getKOTItems(Integer.valueOf(edtCustId.getText().toString()), String.valueOf(jBillingMode));
                if(crsrKOTNo.moveToFirst())
                {
                    iKOTNo = crsrKOTNo.getInt(crsrKOTNo.getColumnIndex("TokenNumber"));
                } else {
                    iKOTNo = dbBillScreen.getKOTNo();
                    long iResult1 = dbBillScreen.updateKOTNo(iKOTNo);
                }
                // Delete the KOT items from Pending KOT if KOT is updated
                iResult = dbBillScreen.deleteKOTItems(Integer.valueOf(edtCustId.getText().toString()), String.valueOf(jBillingMode));
                Log.v("KOT Update", "Deleted rows:" + iResult);
                InsertKOTItems(iKOTNo);
                Toast.makeText(myContext, "KOT Saved Successfully", Toast.LENGTH_LONG).show();
                //PrintKOT();
                btnPrintKOT.setEnabled(true);
                btnPrintBill.setEnabled(true);

                //ClearAll();
                btnPayBill.setEnabled(true);
                Cursor crsrLoadItems = dbBillScreen.getKOTItems(Integer.valueOf(edtCustId.getText().toString()), String.valueOf(jBillingMode));
                LoadKOTItems(crsrLoadItems);
            }

        } else { // billingmode -> takeaway/delivery
            String tempCustId = edtCustId.getText().toString();
            if (tempCustId.equalsIgnoreCase("") || tempCustId.equalsIgnoreCase("0")) {
                MsgBox.Show("Warning", "Please Select Customer for Billing");
                returnStatus = 0;

            } else {
                if (tblOrderItems.getChildCount() < 1) {
                    MsgBox.Show("Warning", "Add Item before Saving KOT");
                    returnStatus = 0;

                } else {
                    int iResult = 0;
                    Cursor crsrKOTNo = dbBillScreen.getKOTItems(Integer.valueOf(edtCustId.getText().toString()), String.valueOf(jBillingMode));
                    if(crsrKOTNo.moveToFirst())
                    {
                        iKOTNo = crsrKOTNo.getInt(crsrKOTNo.getColumnIndex("TokenNumber"));
                    } else {
                        iKOTNo = dbBillScreen.getKOTNo();
                        long iResult1 = dbBillScreen.updateKOTNo(iKOTNo);
                    }

                    // Delete the KOT items from Pending KOT if KOT is updated
                    iResult = dbBillScreen.deleteKOTItems(Integer.valueOf(edtCustId.getText().toString()), String.valueOf(jBillingMode));
                    Log.v("KOT Update", "Deleted rows:" + iResult);
                    InsertKOTItems(iKOTNo);
                    Toast.makeText(myContext, "KOT Saved Successfully", Toast.LENGTH_LONG).show();
                    //PrintKOT();
                    btnPrintKOT.setEnabled(true);
                    btnPrintBill.setEnabled(true);
                    btnPayBill.setEnabled(true);
                    //strPaymentStatus="";
                    //ClearAll();

                    /*if (jBillingMode == 4) {
                        btnPayBill.setEnabled(true);
                    }*/
                    //Cursor crsrLoadItems = dbBillScreen.getKOTItems(Integer.valueOf(edtCustId.getText().toString()), String.valueOf(jBillingMode));
                    //LoadKOTItems(crsrLoadItems);
                }
            }
        }
        return returnStatus;
    }

    private void updateOutwardStock()
    {
        Log.d(TAG, "updateOutwardStock()");
        String businessdate = tvDate.getText().toString();
        DatabaseHandler db_local = new DatabaseHandler(getApplicationContext());
        db_local.CreateDatabase();
        db_local.OpenDatabase();
        StockOutwardMaintain stock_outward = new StockOutwardMaintain(getApplicationContext(), db_local);
        for (int iRow = 0; iRow < tblOrderItems.getChildCount(); iRow++) {
            TableRow RowBillItem = (TableRow) tblOrderItems.getChildAt(iRow);
            int menuCode = -1;
            String itemname = "";
            double closingQty = 0;
            // Item Number
            if (RowBillItem.getChildAt(0) != null) {
                CheckBox ItemNumber = (CheckBox) RowBillItem.getChildAt(0);
                menuCode = (Integer.parseInt(ItemNumber.getText().toString()));
            }
            // Item Name
            if (RowBillItem.getChildAt(1) != null) {
                TextView ItemName = (TextView) RowBillItem.getChildAt(1);
                itemname = (ItemName.getText().toString());
            }

            // Quantity
            if (RowBillItem.getChildAt(3) != null){
                Cursor cursor = db_local.getItem(menuCode);
                if(cursor.moveToNext())
                    closingQty = cursor.getDouble(cursor.getColumnIndex("Quantity"));
            }
            stock_outward.updateClosingStock_Outward(businessdate,menuCode,itemname,closingQty);

        } // end of for
        db_local.CloseDatabase();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {

        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1: // PayBill Activity Result
                    boolean isComplimentaryBill, isDiscounted, isPrintBill = false;
                    float dDiscPercent;
                    String strComplimentaryReason = "";

                    isComplimentaryBill = data.getBooleanExtra(PayBillActivity.IS_COMPLIMENTARY_BILL, false);
                    isDiscounted = data.getBooleanExtra(PayBillActivity.IS_DISCOUNTED, false);
                    isPrintBill = data.getBooleanExtra(PayBillActivity.IS_PRINT_BILL, true);
                    strComplimentaryReason = data.getStringExtra(PayBillActivity.COMPLIMENTARY_REASON);
                    dDiscPercent = data.getFloatExtra(PayBillActivity.DISCOUNT_AMOUNT, 0);
                    fCashPayment = data.getFloatExtra(PayBillActivity.TENDER_CASH_VALUE, 0);
                    fCardPayment = data.getFloatExtra(PayBillActivity.TENDER_CARD_VALUE, 0);
                    fCouponPayment = data.getFloatExtra(PayBillActivity.TENDER_COUPON_VALUE, 0);
                    fTotalDiscount = data.getFloatExtra(PayBillActivity.DISCOUNT_AMOUNT, 0);

                    fPettCashPayment = data.getFloatExtra(PayBillActivity.TENDER_PETTYCASH_VALUE, 0);
                    fPaidTotalPayment = data.getFloatExtra(PayBillActivity.TENDER_PAIDTOTAL_VALUE, 0);
                    fWalletPayment = data.getFloatExtra(PayBillActivity.TENDER_WALLET_VALUE, 0);
                    fChangePayment = data.getFloatExtra(PayBillActivity.TENDER_CHANGE_AMOUNT, 0);

                    iCustId = data.getIntExtra("CUST_ID", 1);

                    if (isDiscounted == true) {
                        Log.v("Tender Result", "Discounted:" + isDiscounted);
                        Log.v("Tender Result", "Discount Percent:" + dDiscPercent);
                        OverAllDiscount(dDiscPercent);
                    }

                    l(2, isPrintBill);
                    Toast.makeText(myContext, "Bill saved Successfully", Toast.LENGTH_SHORT).show();
                    if (BillwithStock==1)
                        updateOutwardStock();
                    if (isComplimentaryBill == true) {
                        // Save complimentary bill details
                        SaveComplimentaryBill(Integer.parseInt(tvBillNumber.getText().toString()),
                                (fCashPayment + fCardPayment + fCouponPayment), strComplimentaryReason);
                    }
                    if (isPrintBill == true) {
                        strPaymentStatus = "Paid";
                        PrintNewBill();
                    }
                    if (jBillingMode == 1) {
                        int iResult = dbBillScreen.deleteKOTItems(Integer.parseInt(tvTableNumber.getText().toString()),
                                Integer.parseInt(tvSubUdfValue.getText().toString()), Integer.parseInt(tvTableSplitNo.getText().toString()));

                        ClearAll();
                    } else if (jBillingMode == 2) {
                        int iResult = dbBillScreen.deleteKOTItems(iCustId, String.valueOf(jBillingMode));
                        ClearAll();
                        btnPrintBill.setEnabled(true);
                    } else if (jBillingMode == 3) {
                        int iResult = dbBillScreen.deleteKOTItems(iCustId, String.valueOf(jBillingMode));
                        //ControlsSetDisabled();
                        ControlsSetEnabled();
                        ClearAll();
                        //Close(null);
                    } else if (jBillingMode == 4) {
                        //int iResult = dbBillScreen.deleteKOTItems(iCustId, String.valueOf(jBillingMode));
                        //Log.d("Pick Up:TenderActivity Result", "Items deleted from pending KOT:" + iResult);
                        //ControlsSetDisabled();
                        ControlsSetEnabled();
                        ClearAll();
                        //Close(null);
                    }
                    break;

                case 2: // Table Activity Result
                    /*if (TableActivity.TABLE_NO.equalsIgnoreCase("") && TableActivity.TABLE_SPLIT_NO.equalsIgnoreCase("")) {
                        Close(null);
                    } else if (TableActivity.TABLE_NO.equalsIgnoreCase("0") && TableActivity.WAITER_NO.equalsIgnoreCase("0")) {
                        MsgBox.Show("Warning", "Either Load KOT or Select Table and Waiter");
                    } else {
                        tvTableNumber.setText(data.getStringExtra(TableActivity.TABLE_NO));
                        tvWaiterNumber.setText(data.getStringExtra(TableActivity.WAITER_NO));
                        tvTableSplitNo.setText(data.getStringExtra(TableActivity.TABLE_SPLIT_NO));
                        //tvSubUdfValue.setText(data.getStringExtra(TableActivity.SUBUDF_NO));

                        if (tvTableNumber.getText().toString().equalsIgnoreCase("0") && tvWaiterNumber.getText().toString().equalsIgnoreCase("0")) {
                            MsgBox.Show("Warning", "Either Load KOT or Select Table and Waiter");
                        }

                        Cursor LoadKOT = dbBillScreen.getKOTItems(Integer.parseInt(tvTableNumber.getText().toString()),
                                Integer.parseInt(tvSubUdfValue.getText().toString()), Integer.parseInt(tvTableSplitNo.getText().toString()));
                        if (LoadKOT.moveToFirst()) {
                            LoadKOTItems(LoadKOT);
                            btnPayBill.setEnabled(true);
                            btnPrintKOT.setEnabled(true);
                            btnPrintBill.setEnabled(true);
                        }
                    }*/
                    break;

                case 3: // Split bill
                    break;

                case 4: // Table Shift Merge (Load KOT)
                    String strTableNumber = data.getStringExtra("TABLE_NUMBER");
                    String strTableSplitNo = data.getStringExtra("TABLE_SPLIT_NO");
                    String strSubUdfNumber = data.getStringExtra("SUB_UDF_NUMBER");
                    Log.v("Load KOT", "TableNumber:" + strTableNumber + " Sub Udf:" + strSubUdfNumber);
                    Cursor LoadKOT = dbBillScreen.getKOTItems(Integer.parseInt(strTableNumber),
                            Integer.parseInt(strSubUdfNumber), Integer.parseInt(strTableSplitNo));
                    if (LoadKOT.moveToFirst()) {
                        LoadKOTItems(LoadKOT);
                        btnDeleteKOT.setEnabled(true);
                        btnPrintKOT.setEnabled(true);
                        btnPrintBill.setEnabled(true);
                        btnPayBill.setEnabled(true);

                    } else {
                        Log.v("Load KOT", "ERROR: No items found");
                    }
                    break;

                case 5: // Pickup and Delivery Status\
                    try {
                        String strBillMode = data.getStringExtra("BILLING_MODE");
                        jBillingMode = Byte.parseByte(strBillMode);
                        strUserName = data.getStringExtra("USER_NAME");
                        strUserId = data.getStringExtra("USER_ID");
                        iCustId = data.getIntExtra("CUST_ID", 0);
                        //strPaymentStatus = data.getStringExtra("Payment_Status");
                        strMakeOrder = data.getStringExtra("MAKE_ORDER");

                        // PICK UP & DELIVERY
                        if (jBillingMode == Byte.parseByte("3") || jBillingMode == Byte.parseByte("4")) {
                            /*crsrCustomerDetails = dbBillScreen.getCustomer(iCustId);
                            if (crsrCustomerDetails!= null && crsrCustomerDetails.moveToFirst()) {
                                edtCustName.setText(crsrCustomerDetails.getString(crsrCustomerDetails.getColumnIndex("CustName")));
                                edtCustPhoneNo.setText(crsrCustomerDetails.getString(crsrCustomerDetails.getColumnIndex("CustContactNumber")));
                                edtCustAddress.setText(crsrCustomerDetails.getString(crsrCustomerDetails.getColumnIndex("CustAddress")));
                            }
*/
                            Cursor BillItems = dbBillScreen.getKOTItems(iCustId, String.valueOf(jBillingMode));
                            if (BillItems.moveToFirst()) {
                                ClearAll();
                                //LoadKOTItems(BillItems);
                                LoadModifyKOTItems(BillItems);
                                btnPayBill.setEnabled(true);
                                btnPrintBill.setEnabled(true);
                                if (!strMakeOrder.equalsIgnoreCase("YES") && !strMakeOrder.equalsIgnoreCase("")) {
                                    Tender();
                                }
                            }
                        }
                    } catch (Exception ex) {
                        MsgBox.Show("", ex.getMessage());
                    }
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            try {
                if (data.getBooleanExtra("isCancelled", false)) {
                    finish();
                }
            } catch (Exception e) {

            }
        }
    }

    /*************************************************************************************************************************************/


    private void loadAutoCompleteData() {

        // List - Get Item Name
        List<String> labelsItemName = dbBillScreen.getAllItemsName();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                labelsItemName);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

        // attaching data adapter to spinner
        aTViewSearchItem.setAdapter(dataAdapter);

        // List - Get Menu Code
        List<String> labelsMenuCode = dbBillScreen.getAllMenuCode();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                labelsMenuCode);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);

        // attaching data adapter to spinner
        aTViewSearchMenuCode.setAdapter(dataAdapter1);

        POS_LIST = ArrayAdapter.createFromResource(this, R.array.poscode, android.R.layout.simple_spinner_item);
        spnr_pos.setAdapter(POS_LIST);

    }


    public ArrayList<BillKotItem> kotPrint() {
        ArrayList<BillKotItem> billKotItems = new ArrayList<BillKotItem>();
        int count = 1;
        for (int iRow = 0; iRow < tblOrderItems.getChildCount(); iRow++) {
            TableRow row = (TableRow) tblOrderItems.getChildAt(iRow);
            CheckBox itemId = (CheckBox) row.getChildAt(0);
            TextView itemName = (TextView) row.getChildAt(1);
            TextView HSNCode = (TextView) row.getChildAt(2);
            EditText itemQty = (EditText) row.getChildAt(3);
            EditText itemRate = (EditText) row.getChildAt(4);
            TextView itemAmount = (TextView) row.getChildAt(5);
            TextView printstatus = (TextView) row.getChildAt(21);
            if (printstatus.getText().toString().equalsIgnoreCase("1")) {
                int id = Integer.parseInt(itemId.getText().toString().trim());
                int sno = count;
                String name = itemName.getText().toString().trim();
                String hsn = HSNCode.getText().toString().trim();
                Double qty = Double.parseDouble(itemQty.getText().toString().trim());
                double rate = Double.parseDouble(itemRate.getText().toString().trim());
                double amount = Double.parseDouble(itemAmount.getText().toString().trim());
                BillKotItem billKotItem = new BillKotItem(sno, name, qty.intValue(), rate, amount,hsn,"","");
                billKotItems.add(billKotItem);
                count++;
            }
        }
        return billKotItems;
    }

    public ArrayList<BillKotItem> billPrint() {
        ArrayList<BillKotItem> billKotItems = new ArrayList<BillKotItem>();
        int count = 1;
        for (int iRow = 0; iRow < tblOrderItems.getChildCount(); iRow++) {
            TableRow row = (TableRow) tblOrderItems.getChildAt(iRow);
            CheckBox itemId = (CheckBox) row.getChildAt(0);
            TextView itemName = (TextView) row.getChildAt(1);
            TextView HSNCode = (TextView) row.getChildAt(2);
            EditText itemQty = (EditText) row.getChildAt(3);
            EditText itemRate = (EditText) row.getChildAt(4);
            TextView itemAmount = (TextView) row.getChildAt(5);
            TextView printstatus = (TextView) row.getChildAt(21);
            int id = Integer.parseInt(itemId.getText().toString().trim());
            int sno = count;
            String name = itemName.getText().toString().trim();
            String hsn = HSNCode.getText().toString().trim();
            Double qty = Double.parseDouble(itemQty.getText().toString().trim());
            double rate = Double.parseDouble(itemRate.getText().toString().trim());
            double amount = Double.parseDouble(itemAmount.getText().toString().trim());
            BillKotItem billKotItem = new BillKotItem(sno, name, qty.intValue(), rate, amount,hsn,"","");
            billKotItems.add(billKotItem);
            count++;

        }
        return billKotItems;
    }

    public ArrayList<BillTaxItem> taxPrint() {
        ArrayList<BillTaxItem> billTaxItems = new ArrayList<BillTaxItem>();

        Cursor crsrTax = dbBillScreen.getItemsForSalesTaxPrint(Integer.valueOf(tvBillNumber.getText().toString()));
        if (crsrTax.moveToFirst()) {
            do {
                String taxname = "Sales Tax"; //crsrTax.getString(crsrTax.getColumnIndex("TaxDescription"));
                String taxpercent = crsrTax.getString(crsrTax.getColumnIndex("TaxPercent"));
                Double taxvalue = Double.parseDouble(crsrTax.getString(crsrTax.getColumnIndex("TaxAmount")));

                BillTaxItem taxItem = new BillTaxItem(taxname, Double.parseDouble(taxpercent), Double.parseDouble(String.format("%.2f", taxvalue)));
                billTaxItems.add(taxItem);
            } while (crsrTax.moveToNext());
        }
        return billTaxItems;
    }

    public ArrayList<BillTaxItem> otherChargesPrint() {
        ArrayList<BillTaxItem> billOtherChargesItems = new ArrayList<BillTaxItem>();
        String billingmode= "";
        switch (jBillingMode)
        {
            case 1 : billingmode = DineInCaption;
                        break;
            case 2 : billingmode = CounterSalesCaption;
                break;
            case 3 : billingmode = TakeAwayCaption;
                break;
            case 4 : billingmode = HomeDeliveryCaption;
                break;
        }
        Cursor crsrTax = dbBillScreen.getItemsForOtherChargesPrint(billingmode);
        if (crsrTax.moveToFirst()) {
            do {
                String taxname = crsrTax.getString(crsrTax.getColumnIndex("ModifierDescription"));
                String taxpercent = "0";
                Double taxvalue = Double.parseDouble(crsrTax.getString(crsrTax.getColumnIndex("ModifierAmount")));

                BillTaxItem taxItem = new BillTaxItem(taxname, Double.parseDouble(taxpercent), Double.parseDouble(String.format("%.2f", taxvalue)));
                billOtherChargesItems.add(taxItem);
            } while (crsrTax.moveToNext());
        }
        return billOtherChargesItems;
    }

    public ArrayList<BillServiceTaxItem> servicetaxPrint() {
        ArrayList<BillServiceTaxItem> billServiceTaxItems = new ArrayList<BillServiceTaxItem>();

        Cursor crsrTax = dbBillScreen.getItemsForServiceTaxPrint(Integer.valueOf(tvBillNumber.getText().toString()));
        if (crsrTax.moveToFirst()) {
            //do {
                BillServiceTaxItem ServicetaxItem = new BillServiceTaxItem("Service Tax", Double.parseDouble(crsrTax.getString(crsrTax.getColumnIndex("ServiceTaxPercent"))), Double.parseDouble(String.format("%.2f", Double.parseDouble(tvServiceTaxTotal.getText().toString()))));
                billServiceTaxItems.add(ServicetaxItem);
            //} while (crsrTax.moveToNext());
        }
        return billServiceTaxItems;
    }

    public ArrayList<BillSubTaxItem> subtaxPrint() {
        ArrayList<BillSubTaxItem> billSubTaxItems = new ArrayList<BillSubTaxItem>();
        Cursor crsrSubTax = dbBillScreen.getAllSubTaxConfig("2");
        if (crsrSubTax.moveToFirst()) {
            do {
                String subtaxname = crsrSubTax.getString(crsrSubTax.getColumnIndex("SubTaxDescription"));
                String subtaxpercent = crsrSubTax.getString(crsrSubTax.getColumnIndex("SubTaxPercent"));
                double subtaxvalue = Double.parseDouble(tvSubTotal.getText().toString().trim()) * (Double.parseDouble(subtaxpercent) / 100);

                BillSubTaxItem taxSubItem = new BillSubTaxItem(subtaxname, Double.parseDouble(subtaxpercent), Double.parseDouble(String.format("%.2f", subtaxvalue)));
                billSubTaxItems.add(taxSubItem);

                fTotalsubTaxPercent += Float.parseFloat(subtaxpercent);

            } while (crsrSubTax.moveToNext());
        }
        return billSubTaxItems;
    }

    public void printKOT(View view) {
        int proceed = 1;

        if (tblOrderItems.getChildCount() < 1){
            MsgBox.Show("Warning", "Add Item before Saving KOT");
            proceed =0;
        }
        else if (jBillingMode==4 || jBillingMode==3)
        {
            String tempCustId = edtCustId.getText().toString();
            if (tempCustId.equalsIgnoreCase("") || tempCustId.equalsIgnoreCase("0")) {
                MsgBox.Show("Warning", "Please Select Customer for Billing");
                proceed =0;
            }
        }
        if(proceed == 1)
        {
            if (isPrinterAvailable) {
                iPrintKOTStatus = 0;
                int i = SaveKOT();
                if(i == 0) // print only when KOT is saved successfully
                {
                    return;
                }
                PrintKOT();
                tblOrderItems.removeAllViews();
                Cursor LoadKOT = dbBillScreen.getKOTItems(Integer.parseInt(tvTableNumber.getText().toString()),
                        Integer.parseInt(tvSubUdfValue.getText().toString()), Integer.parseInt(tvTableSplitNo.getText().toString()));
                if (LoadKOT.moveToFirst()) {
                    LoadKOTItems(LoadKOT);
                }
            } else {
                Toast.makeText(myContext, "Printer is not ready", Toast.LENGTH_SHORT).show();
                askForConfig();
            }
        }
    }

    protected void PrintKOT() {
        if (isPrinterAvailable) {
            if (tblOrderItems.getChildCount() < 1) {
                MsgBox.Show("Warning", "Insert item before Print KOT");
                return;
            } else {
                int tableId = 0, waiterId = 0, orderId = 0;
                if ((!tvTableNumber.getText().toString().trim().equalsIgnoreCase("")) && (!tvWaiterNumber.getText().toString().trim().equalsIgnoreCase("")) && (!tvBillNumber.getText().toString().trim().equalsIgnoreCase(""))) {
                    tableId = Integer.parseInt(tvTableNumber.getText().toString().trim());
                    waiterId = Integer.parseInt(tvWaiterNumber.getText().toString().trim());
                    orderId = Integer.parseInt(tvBillNumber.getText().toString().trim());
                    ArrayList<BillKotItem> billKotItems = kotPrint();
                    PrintKotBillItem item = new PrintKotBillItem();
                    item.setBillKotItems(billKotItems);
                    item.setTableNo(String.valueOf(tableId));
                    item.setWaiterNo(waiterId);
                    item.setBillNo(String.valueOf(iKOTNo));
                    item.setOrderBy(strUserName);
                    item.setBillingMode(String.valueOf(jBillingMode));
                    item.setDate(TimeUtil.getDate());
                    item.setTime(TimeUtil.getTime());
            /*Intent intent = new Intent(getApplicationContext(), PrinterSohamsaActivity.class);
            intent.putExtra("printType", "KOT");
            intent.putExtra("printData", item);
            startActivity(intent);*/
                    String prf = Preferences.getSharedPreferencesForPrint(BillingScreenActivity_Unused.this).getString("kot", "--Select--");
                    Intent intent = null;
                    if (prf.equalsIgnoreCase("Sohamsa")) {
                    /*intent = new Intent(getApplicationContext(), PrinterSohamsaActivity.class);
                    intent.putExtra("printType", "KOT");
                    intent.putExtra("printData", item);
                    startActivity(intent);*/
                        //printSohamsa(item, "KOT");
                        Toast.makeText(myContext, "KOT is printing... ", Toast.LENGTH_LONG).show();
                    } else if (prf.equalsIgnoreCase("Heyday")) {
                    /*intent = new Intent(getApplicationContext(), PrinterFragment.class);
                    intent.putExtra("printType", "KOT");
                    intent.putExtra("printData", item);
                    startActivity(intent);*/
                        if (isPrinterAvailable) {
                            printHeydeyKOT(item, "KOT");
                        } else {
                            askForConfig();
                        }
                        Toast.makeText(myContext, "KOT is printing... ", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(myContext, "Printer not configured", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BillingScreenActivity_Unused.this, "Please enter bill,waiter,table number", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(myContext, "Printer is not ready", Toast.LENGTH_SHORT).show();
            askForConfig();
        }
    }

    public void printBILL(View view) {
        int proceed =1;
        if (tblOrderItems.getChildCount() < 1){
            MsgBox.Show("Warning", "Add Item before Printing Bill");
            proceed =0;
        }
        else if (jBillingMode==4 )
        {
            String tempCustId = edtCustId.getText().toString();
            if (tempCustId.equalsIgnoreCase("") || tempCustId.equalsIgnoreCase("0")) {
                MsgBox.Show("Warning", "Please Select Customer for Billing");
                proceed =0;
            }
        }
        if(proceed == 0)
            return;
        if (isPrinterAvailable) {
            strPaymentStatus = "Paid";
            PrintBillPayment = 1;

            // Print Bill with Save Bill
            if (tblOrderItems.getChildCount() < 1) {
                MsgBox.Show("Warning", "Insert item before Print Bill");
                return;
            } else {
                l(2, true);
                PrintNewBill();
                Toast.makeText(myContext, "Bill Saved Successfully", Toast.LENGTH_SHORT).show();
                if (jBillingMode == 1) {
                    int iResult = dbBillScreen.deleteKOTItems(Integer.parseInt(tvTableNumber.getText().toString()),
                            Integer.parseInt(tvSubUdfValue.getText().toString()), Integer.parseInt(tvTableSplitNo.getText().toString()));
                    //Log.d("Dine In:TenderActivity Result", "Items deleted from pending KOT:" + iResult);
                    ClearAll();

                } else if (jBillingMode == 2) {
                    ClearAll();
                    btnPrintBill.setEnabled(true);
                } else if (jBillingMode == 3) {
                    int iResult = dbBillScreen.deleteKOTItems(iCustId, String.valueOf(jBillingMode));
                    //Log.d("Pick Up:TenderActivity Result", "Items deleted from pending KOT:" + iResult);
                    ClearAll();
                    Close(null);
                } else if (jBillingMode == 4) {
                    //int iResult = dbBillScreen.deleteKOTItems(iCustId, String.valueOf(jBillingMode));
                    //Log.d("Pick Up:TenderActivity Result", "Items deleted from pending KOT:" + iResult);
                    ClearAll();
                    Close(null);
                }
            }
        } else {
            Toast.makeText(myContext, "Printer is not ready", Toast.LENGTH_SHORT).show();
            askForConfig();
        }
    }

    protected void PrintNewBill() {
        if (isPrinterAvailable) {
            if (tblOrderItems.getChildCount() < 1) {
                MsgBox.Show("Warning", "Insert item before Print Bill");
                return;
            } else {
                int tableId = 0, waiterId = 0, orderId = 0;
                if ((!tvTableNumber.getText().toString().trim().equalsIgnoreCase("")) && (!tvWaiterNumber.getText().toString().trim().equalsIgnoreCase("")) && (!tvBillNumber.getText().toString().trim().equalsIgnoreCase(""))) {
                    tableId = Integer.parseInt(tvTableNumber.getText().toString().trim());
                    waiterId = Integer.parseInt(tvWaiterNumber.getText().toString().trim());
                    orderId = Integer.parseInt(tvBillNumber.getText().toString().trim());
                    ArrayList<BillKotItem> billKotItems = billPrint();
                    ArrayList<BillTaxItem> billTaxItems = taxPrint();
                    ArrayList<BillTaxItem> billOtherChargesItems = otherChargesPrint();
                    ArrayList<BillServiceTaxItem> billServiceTaxItems = servicetaxPrint();

                    ArrayList<BillSubTaxItem> billSubTaxItems = subtaxPrint();
                    PrintKotBillItem item = new PrintKotBillItem();

                    Cursor crsrCustomer = dbBillScreen.getCustomer(Integer.valueOf(edtCustId.getText().toString()));
                    if (crsrCustomer.moveToFirst()) {
                        item.setCustomerName(crsrCustomer.getString(crsrCustomer.getColumnIndex("CustName")));
                    } else {
                        item.setCustomerName(" - - - ");
                    }
                    item.setBillKotItems(billKotItems);
                    item.setBillOtherChargesItems(billOtherChargesItems);
                    item.setBillTaxItems(billTaxItems);
                    item.setBillServiceTaxItems(billServiceTaxItems);
                    item.setBillSubTaxItems(billSubTaxItems);
                    item.setSubTotal(Double.parseDouble(tvSubTotal.getText().toString().trim()));
                    item.setNetTotal(Double.parseDouble(tvBillAmount.getText().toString().trim()));
                    item.setTableNo(String.valueOf(tableId));
                    item.setWaiterNo(waiterId);
                    item.setBillNo(String.valueOf(orderId));
                    item.setOrderBy(strUserName);
                    item.setBillingMode(String.valueOf(jBillingMode));
                    if (strPaymentStatus.equalsIgnoreCase("")) {
                        item.setPaymentStatus("");
                    } else {
                        item.setPaymentStatus(strPaymentStatus);
                    }
                    item.setDate(TimeUtil.getDate());
                    item.setTime(TimeUtil.getTime());

                    item.setTotalsubTaxPercent(fTotalsubTaxPercent);
                    item.setTotalSalesTaxAmount(tvTaxTotal.getText().toString());
                    item.setTotalServiceTaxAmount(tvServiceTaxTotal.getText().toString());

                    String billingmode= "";
                    switch (jBillingMode)
                    {
                        case 1 : billingmode = DineInCaption;
                            break;
                        case 2 : billingmode = CounterSalesCaption;
                            break;
                        case 3 : billingmode = TakeAwayCaption;
                            break;
                        case 4 : billingmode = HomeDeliveryCaption;
                            break;
                    }
                    item.setStrBillingModeName(billingmode);
                    String prf = Preferences.getSharedPreferencesForPrint(BillingScreenActivity_Unused.this).getString("bill", "--Select--");
                /*Intent intent = new Intent(getApplicationContext(), PrinterSohamsaActivity.class);*/
                    Intent intent = null;
                    if (prf.equalsIgnoreCase("Sohamsa")) {
                        String[] tokens = new String[3];
                        tokens[0] = "";
                        tokens[1] = "";
                        tokens[2] = "";
                        Cursor crsrHeaderFooterSetting = null;
                        crsrHeaderFooterSetting = dbBillScreen.getBillSetting();
                        if (crsrHeaderFooterSetting.moveToFirst()) {
                            try {
                                tokens = crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("HeaderText")).split(Pattern.quote("|"));
                            } catch (Exception e) {
                                tokens[0] = "";
                                tokens[1] = "";
                                tokens[2] = "";
                            }
                            if (!tokens[0].equalsIgnoreCase(""))
                                item.setAddressLine1(tokens[0]);
                            if (!tokens[1].equalsIgnoreCase(""))
                                item.setAddressLine2(tokens[1]);
                            if (!tokens[2].equalsIgnoreCase(""))
                                item.setAddressLine3(tokens[2]);
                            item.setFooterLine(crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("FooterText")));
                        } else {
                            Log.d(TAG, "DisplayHeaderFooterSettings No data in BillSettings table");
                        }

                        //printSohamsaBILL(item, "BILL");
                    } else if (prf.equalsIgnoreCase("Heyday")) {
                        String[] tokens = new String[3];
                        tokens[0] = "";
                        tokens[1] = "";
                        tokens[2] = "";
                        Cursor crsrHeaderFooterSetting = null;
                        crsrHeaderFooterSetting = dbBillScreen.getBillSetting();
                        if (crsrHeaderFooterSetting.moveToFirst()) {
                            try {
                                tokens = crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("HeaderText")).split(Pattern.quote("|"));
                            } catch (Exception e) {
                                tokens[0] = "";
                                tokens[1] = "";
                                tokens[2] = "";
                            }
                            if (!tokens[0].equalsIgnoreCase(""))
                                item.setAddressLine1(tokens[0]);
                            if (!tokens[1].equalsIgnoreCase(""))
                                item.setAddressLine2(tokens[1]);
                            if (!tokens[2].equalsIgnoreCase(""))
                                item.setAddressLine3(tokens[2]);
                            item.setFooterLine(crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("FooterText")));
                        } else {
                            Log.d(TAG, "DisplayHeaderFooterSettings No data in BillSettings table");
                        }
                        //startActivity(intent);
                        if (isPrinterAvailable) {
                            printHeydeyBILL(item, "BILL");
                        } else {
                            askForConfig();
                        }
                    } else {
                        Toast.makeText(myContext, "Printer not configured", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BillingScreenActivity_Unused.this, "Please Enter Bill, Waiter, Table Number", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(myContext, "Printer is not ready", Toast.LENGTH_SHORT).show();
            askForConfig();
        }
    }

    public void ReprintKOT(final View v) {
        //takeScreenshot();
        tblOrderItems.removeAllViews();

        AlertDialog.Builder DineInTenderDialog = new AlertDialog.Builder(myContext);

        LayoutInflater UserAuthorization = (LayoutInflater) myContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vwAuthorization = UserAuthorization.inflate(R.layout.tender_dinein, null);

        final EditText txtTblNo = (EditText) vwAuthorization.findViewById(R.id.txtDineInTenderTableNumber);
        final EditText txtTblSplitNo = (EditText) vwAuthorization.findViewById(R.id.txtDineInTenderTableSplitNo);
        txtTblSplitNo.setText("1");

        DineInTenderDialog.setIcon(R.drawable.ic_launcher).setTitle("RePrint KOT").setMessage("Enter Table Number")
                .setView(vwAuthorization).setNegativeButton("Cancel", null)
                .setPositiveButton("RePrint", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        if (txtTblNo.getText().toString().equalsIgnoreCase("")) {
                            MsgBox.Show("Warning", "Please Enter Table Number");
                            return;
                        } else if (txtTblSplitNo.getText().toString().equalsIgnoreCase("")) {
                            MsgBox.Show("Warning", "Please Enter Table Split Number");
                            return;
                        } else {

                            tvTableNumber.setText(txtTblNo.getText().toString());
                            tvTableSplitNo.setText(txtTblSplitNo.getText().toString());
                            tvSubUdfValue.setText("1");
                            Cursor BillItems = dbBillScreen.getKOTItems(Integer.parseInt(txtTblNo.getText().toString()),
                                    1, Integer.parseInt(txtTblSplitNo.getText().toString()));
                            if (BillItems.moveToFirst()) {
                                REPRINT_KOT =1;
                                LoadKOTItems(BillItems);

                                printKOT(v);
                            } else {
                                MsgBox.Show("Warning",
                                        "No KOT is present for the table number " + txtTblNo.getText().toString());
                            }
                        }
                    }
                }).show();
    }


    /*************************************************************************************************************************************
     * Loads KOT order items to billing table
     *
     * @param crsrBillItems : Cursor with KOT order item details
     *************************************************************************************************************************************/
    @SuppressWarnings("deprecation")
    private void LoadItemsForReprintBill(Cursor crsrBillItems) {
        EditTextInputHandler etInputValidate = new EditTextInputHandler();
        TableRow rowItem;
        TextView tvHSn, tvName, tvAmount, tvTaxPercent, tvTaxAmt, tvDiscPercent, tvDiscAmt, // tvQty,
                // tvRate,
                tvDeptCode, tvCategCode, tvKitchenCode, tvTaxType, tvModifierCharge, tvServiceTaxPercent,
                tvServiceTaxAmt;
        EditText etQty, etRate;
        CheckBox Number;
        ImageButton ImgDelete;

        if (crsrBillItems.moveToFirst()) {
            // Display items in table
            do {
                rowItem = new TableRow(myContext);
                rowItem.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

                // Item Number
                Number = new CheckBox(myContext);
                Number.setWidth(40);
                Number.setTextSize(0);
                Number.setTextColor(Color.TRANSPARENT);
                Number.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ItemNumber")));

                // Item Name
                tvName = new TextView(myContext);
                tvName.setWidth(135);
                tvName.setTextSize(11);
                tvName.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ItemName")));

                //hsn code
                tvHSn = new TextView(myContext);
                tvHSn.setWidth(67); // 154px ~= 230dp
                tvHSn.setTextSize(11);
                if (GSTEnable.equalsIgnoreCase("1") && (HSNEnable_out != null) && HSNEnable_out.equals("1")) {
                    tvHSn.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("HSNCode")));
                }

                // Quantity
                etQty = new EditText(myContext);
                etQty.setWidth(55);
                etQty.setTextSize(11);
                etQty.setText(String.format("%.2f", crsrBillItems.getDouble(crsrBillItems.getColumnIndex("Quantity"))));
                etQty.setOnClickListener(Qty_Rate_Click);
                etInputValidate.ValidateDecimalInput(etQty);

                // Rate
                etRate = new EditText(myContext);
                etRate.setWidth(75);
                etRate.setEnabled(false);
                etRate.setTextSize(11);
                etRate.setText(String.format("%.2f", crsrBillItems.getDouble(crsrBillItems.getColumnIndex("Value"))));

                // Amount
                tvAmount = new TextView(myContext);
                tvAmount.setWidth(105);
                tvAmount.setTextSize(11);
                tvAmount.setText(
                        String.format("%.2f", crsrBillItems.getDouble(crsrBillItems.getColumnIndex("SubTotal"))));

                // Sales Tax%
                tvTaxPercent = new TextView(myContext);
                tvTaxPercent.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("TaxPercent")));

                // Sales Tax Amount
                tvTaxAmt = new TextView(myContext);
                tvTaxAmt.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("TaxAmount")));

                // Discount %
                tvDiscPercent = new TextView(myContext);
                tvDiscPercent.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("DiscountPercent")));

                // Discount Amount
                tvDiscAmt = new TextView(myContext);
                tvDiscAmt.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("DiscountAmount")));

                // Dept Code
                tvDeptCode = new TextView(myContext);
                tvDeptCode.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("DeptCode")));

                // Categ Code
                tvCategCode = new TextView(myContext);
                tvCategCode.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("CategCode")));

                // Kitchen Code
                tvKitchenCode = new TextView(myContext);
                tvKitchenCode.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("KitchenCode")));

                // Tax Type
                tvTaxType = new TextView(myContext);
                tvTaxType.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("TaxType")));

                // Modifier Amount
                tvModifierCharge = new TextView(myContext);
                tvModifierCharge.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ModifierAmount")));

                // Service Tax %
                tvServiceTaxPercent = new TextView(myContext);
                tvServiceTaxPercent.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ServiceTaxPercent")));

                // Service Tax Amount
                tvServiceTaxAmt = new TextView(myContext);
                tvServiceTaxAmt.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ServiceTaxAmount")));

                // Delete
                int res = getResources().getIdentifier("delete", "drawable", this.getPackageName());
                ImgDelete = new ImageButton(myContext);
                ImgDelete.setImageResource(res);
                ImgDelete.setVisibility(View.INVISIBLE);

                // Add all text views and edit text to Item Row
                // rowItem.addView(tvNumber);
                rowItem.addView(Number);
                rowItem.addView(tvName);
                rowItem.addView(tvHSn);
                rowItem.addView(etQty);
                rowItem.addView(etRate);
                rowItem.addView(tvAmount);
                rowItem.addView(tvTaxPercent);
                rowItem.addView(tvTaxAmt);
                rowItem.addView(tvDiscPercent);
                rowItem.addView(tvDiscAmt);
                rowItem.addView(tvDeptCode);
                rowItem.addView(tvCategCode);
                rowItem.addView(tvKitchenCode);
                rowItem.addView(tvTaxType);
                rowItem.addView(tvModifierCharge);
                rowItem.addView(tvServiceTaxPercent);
                rowItem.addView(tvServiceTaxAmt);
                rowItem.addView(ImgDelete);

                // Add row to table
                tblOrderItems.addView(rowItem, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

            } while (crsrBillItems.moveToNext());

            CalculateTotalAmountforRePrint();

        } else {
            Log.d("LoadKOTItems", "No rows in cursor");
        }
    }

    public void KOTStatus(View view) {

        if (jBillingMode == 1) {
            Intent intentDineIn = new Intent(myContext, KOTStatusActivity.class);
            intentDineIn.putExtra("jBillingMode", String.valueOf(jBillingMode));
            intentDineIn.putExtra("USER_NAME", strUserName);
            startActivity(intentDineIn);
        } else if (jBillingMode == 2) {
            Intent intentCounterSales = new Intent(myContext, KOTStatusActivity.class);
            intentCounterSales.putExtra("jBillingMode", String.valueOf(jBillingMode));
            intentCounterSales.putExtra("USER_NAME", strUserName);
            startActivity(intentCounterSales);
        } else if (jBillingMode == 3) {
            Intent intentPickUp = new Intent(myContext, CustomerOrdersActivity.class);
            intentPickUp.putExtra("BILLING_MODE", "3");
            intentPickUp.putExtra("USER_ID", strUserId);//spUser.getString("USER_ID", "GHOST"));
            intentPickUp.putExtra("USER_NAME", strUserName);//spUser.getString("USER_NAME", "GHOST"));
            //startActivity(intentPickUp);
            startActivityForResult(intentPickUp, 5);
            //this.finish();
        } else if (jBillingMode == 4) {
            Intent intentDelivery = new Intent(myContext, CustomerOrdersActivity.class);
            intentDelivery.putExtra("BILLING_MODE", "4");
            intentDelivery.putExtra("USER_ID", strUserId);//spUser.getString("USER_ID", "GHOST"));
            intentDelivery.putExtra("USER_NAME", strUserName);//spUser.getString("USER_NAME", "GHOST"));
            startActivityForResult(intentDelivery, 5);
            //this.finish();
        }

    }

    // Add Customer if Customer is not Found for Pickup and Home Delivery
    public void AddCustomer(View v) {
        try {
            if (edtCustName.getText().toString().equalsIgnoreCase("") || edtCustPhoneNo.getText().toString().equalsIgnoreCase("") || edtCustAddress.getText().toString().equalsIgnoreCase("")) {
                MsgBox.Show("Warning", "Please fill all details before adding customer");
            } else {
                Cursor crsrCust = dbBillScreen.getCustomer(edtCustPhoneNo.getText().toString());
                if (crsrCust.moveToFirst()) {
                    MsgBox.Show("", "Customer Already Exists");
                } else {
                    String gstin = etCustGSTIN.getText().toString();
                    if (gstin == null) {
                        gstin = "";
                    }
                    InsertCustomer(edtCustAddress.getText().toString(), edtCustPhoneNo.getText().toString(),
                            edtCustName.getText().toString(), 0, 0, 0, gstin);
                    //ResetCustomer();
                    //MsgBox.Show("", "Customer Added Successfully");
                    Toast.makeText(myContext, "Customer Added Successfully", Toast.LENGTH_SHORT).show();
                    ControlsSetEnabled();

                }
            }
        } catch (Exception ex) {
            MsgBox.Show("Error", ex.getMessage());
        }
    }

    private void InsertCustomer(String strAddress, String strContactNumber, String strName, float fLastTransaction,
                                float fTotalTransaction, float fCreditAmount, String gstin) {
        long lRowId;

        Customer objCustomer = new Customer(strAddress, strName, strContactNumber, fLastTransaction, fTotalTransaction,
                fCreditAmount, gstin);

        lRowId = dbBillScreen.addCustomer(objCustomer);
        /*edtCustId.setText(String.valueOf(lRowId));*/

        if (edtCustPhoneNo.getText().toString().length() == 10) {
            Cursor crsrCust = dbBillScreen.getCustomer(edtCustPhoneNo.getText().toString());
            if (crsrCust.moveToFirst()) {
                edtCustId.setText(crsrCust.getString(crsrCust.getColumnIndex("CustId")));
            }
        }
        Log.d("Customer", "Row Id: " + String.valueOf(lRowId));

    }

    private void ResetCustomer() {
        edtCustName.setText("");
        edtCustPhoneNo.setText("");
        edtCustAddress.setText("");
        edtCustId.setText("0");
        edtCustDineInPhoneNo.setText("");
    }

    public void ControlsSetEnabled() {
        btnAddCustomer.setVisibility(View.VISIBLE);
        tvHSNCode_out.setEnabled(true);
        txtSearchItemBarcode.setEnabled(true);
        txtOthercharges.setEnabled(true);
        btn_item_fastBillingMode.setEnabled(true);
        lstvwDepartment.setEnabled(true);
        lstvwCategory.setEnabled(true);
        grdItems.setEnabled(true);
        btnSplitBill.setEnabled(true);
        //btnPayBill.setEnabled(true);
        btnSaveKOT.setEnabled(true);
        //btnDeliveryStatus.setEnabled(true);
        if(tblOrderItems.getChildCount()>0)
        {
            btnPayBill.setEnabled(true);
            btnPrintBill.setEnabled(true);
        }else
        {
            btnPayBill.setEnabled(false);
            btnPrintBill.setEnabled(false);
        }
        btnPrintKOT.setEnabled(true);
        //btnPrintBill.setEnabled(true);
        tblOrderItems.setEnabled(true);
        btnClear.setEnabled(true);
        btnReprint.setEnabled(true);
        aTViewSearchItem.setEnabled(true);
        spnr_pos.setEnabled(true);
        aTViewSearchMenuCode.setEnabled(true);
        btndepart.setEnabled(true);
        btncateg.setEnabled(true);
        btnitem.setEnabled(true);
        if(jBillingMode==2) {
            btnPrintBill.setEnabled(true);
            btnPayBill.setEnabled(true);
        }

    }

    public void ControlsSetDisabled() {
        btnAddCustomer.setVisibility(View.VISIBLE);
        tvHSNCode_out.setEnabled(false);
        txtSearchItemBarcode.setEnabled(false);
        txtOthercharges.setEnabled(false);
        btn_item_fastBillingMode.setEnabled(false);
        lstvwDepartment.setEnabled(false);
        lstvwCategory.setEnabled(false);
        grdItems.setEnabled(false);
        btnSplitBill.setEnabled(false);
        btnPayBill.setEnabled(false);
        btnSaveKOT.setEnabled(false);
        //btnDeliveryStatus.setEnabled(false);
        btnPrintKOT.setEnabled(false);
        btnPrintBill.setEnabled(false);
        tblOrderItems.setEnabled(false);
        btnClear.setEnabled(false);
        btnReprint.setEnabled(false);
        aTViewSearchItem.setEnabled(false);
        spnr_pos.setEnabled(false);
        aTViewSearchMenuCode.setEnabled(false);
        btndepart.setEnabled(false);
        btncateg.setEnabled(false);
        btnitem.setEnabled(false);
        if(jBillingMode==2) {
            btnPrintBill.setEnabled(true);
            btnPayBill.setEnabled(true);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);
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
                    .setTitle("Are you sure you want to exit ?")
                    .setView(vwAuthorization)
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            /*Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);*/
                            finish();
                        }
                    })
                    .show();
        }

        return super.onKeyDown(keyCode, event);
    }


    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.wep.common.app.R.menu.menu_main, menu);
        for (int j = 0; j < menu.size(); j++) {
            MenuItem item = menu.getItem(j);
            item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return true;
    }

    @Override
    public void onHomePressed() {
        ActionBarUtils.navigateHome(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }else if (id == com.wep.common.app.R.id.action_home) {
            //ActionBarUtils.navigateHome(this);
            finish();
        }else if (id == com.wep.common.app.R.id.action_screen_shot) {
            com.wep.common.app.ActionBarUtils.takeScreenshot(this,findViewById(android.R.id.content).getRootView());
        }
        return super.onOptionsItemSelected(item);
    }
}


/*************************************************************************************************************************************
 * End Of File
 * <p>
 * <p>
 * /************************************************************
 * End Of File
 * <p>
 * <p>
 * /
 *************************************************************************************************************************************/