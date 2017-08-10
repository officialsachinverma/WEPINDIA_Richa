package com.wepindia.pos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
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
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.mswipetech.wisepad.sdktest.view.ApplicationData;
import com.wep.common.app.Database.BillDetail;
import com.wep.common.app.Database.BillItem;
import com.wep.common.app.Database.Category;
import com.wep.common.app.Database.ComplimentaryBillDetail;
import com.wep.common.app.Database.Customer;
import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.Database.Department;
import com.wep.common.app.models.Items;
import com.wep.common.app.print.BillKotItem;
import com.wep.common.app.print.BillServiceTaxItem;
import com.wep.common.app.print.BillSubTaxItem;
import com.wep.common.app.print.BillTaxItem;
import com.wep.common.app.print.BillTaxSlab;
import com.wep.common.app.print.PrintKotBillItem;
import com.wep.common.app.utils.Preferences;
import com.wep.common.app.views.WepButton;
import com.wepindia.pos.GenericClasses.DateTime;
import com.wepindia.pos.GenericClasses.DecimalDigitsInputFilter;
import com.wepindia.pos.GenericClasses.EditTextInputHandler;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.adapters.CategoryAdapter;
import com.wepindia.pos.adapters.DepartmentAdapter;
import com.wepindia.pos.adapters.ItemsAdapter;
import com.wepindia.pos.utils.ActionBarUtils;
import com.wepindia.pos.utils.AddedItemsToOrderTableClass;
import com.wepindia.pos.utils.StockOutwardMaintain;
import com.wepindia.printers.WepPrinterBaseActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BillingCounterSalesActivity extends WepPrinterBaseActivity implements View.OnClickListener {

    private static final String TAG = BillingCounterSalesActivity.class.getSimpleName();
    private Toolbar toolbar;
    private String userId, userName;
    private DatabaseHandler db;
    private ItemsAdapter itemsAdapter;
    private DepartmentAdapter departmentAdapter;
    private CategoryAdapter categoryAdapter;
    private GridView gridViewItems;
    private ListView listViewDept,listViewCat;
    private MessageDialog messageDialog;
    Date d;
    Calendar Time; // Time variable
    private WepButton btn_PrintBill,btn_PayBill, btn_Clear, btn_DeleteBill, btn_Reprint,btn_DineInAddCustomer;
    private EditText editTextName,editTextMobile,editTextAddress, tvBillNumber;
    EditText tvWaiterNumber;
    EditText  edtCustName, edtCustPhoneNo, edtCustAddress, edtCustDineInPhoneNo, etCustGSTIN;
    private AutoCompleteTextView autoCompleteTextViewSearchItem, autoCompleteTextViewSearchMenuCode;
    private RelativeLayout boxDept,boxCat,boxItem;
    private LinearLayout idd_date;
    private Button btnDept,btnCat,btnItems;
    ArrayAdapter<CharSequence> POS_LIST;
    Spinner spnr_pos;
    //String strUserId = "", strUserName = "", strDate = "";
    private byte jBillingMode = 2, jWeighScale = 0;
    private TableLayout tblOrderItems;
    private String GSTEnable = "", HSNEnable_out = "", POSEnable = "";
    private Cursor crsrSettings = null;
    TextView tvHSNCode_out;
    private TextView textViewOtherCharges,tvIGSTValue,tvcessValue,tvTaxTotal,tvServiceTaxTotal,tvSubTotal,tvBillAmount,tvDate, tvDiscountAmount, tvDiscountPercentage;
    LinearLayout relative_Interstate;
    CheckBox chk_interstate = null;
    private String fastBillingMode = "1";
    private String customerId = "0";
    public boolean isPrinterAvailable = false;
    private String strPaymentStatus;
    private int PrintBillPayment = 0;
    String HomeDeliveryCaption="", TakeAwayCaption="", DineInCaption = "", CounterSalesCaption = "";
    float fTotalsubTaxPercent = 0;
    int iTaxType = 0, iTotalItems = 0, iCustId = 0, iTokenNumber = 0;
    float fChangePayment = 0;
    float fWalletPayment = 0;
    float fTotalDiscount = 0, fCashPayment = 0, fCardPayment = 0, fCouponPayment = 0, fPettCashPayment = 0, fPaidTotalPayment = 0;
    int BillwithStock = 0;
    String businessDate="";
    int reprintBillingMode =0;
    boolean isReprint = false;
    int ItemwiseDiscountEnabled =0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_counter_sales);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        messageDialog = new MessageDialog(this);
        userId = ApplicationData.getUserId(this);
        userName = ApplicationData.getUserName(this);
        d = new Date();
        CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
        iCustId = getIntent().getIntExtra("CUST_ID", 0);
        db = new DatabaseHandler(this);
        gridViewItems = (GridView) findViewById(R.id.listViewFilter3);
        gridViewItems.setOnItemClickListener(itemsClick);
        listViewDept = (ListView) findViewById(R.id.listViewFilter1);
        listViewDept.setOnItemClickListener(deptClick);
        listViewCat = (ListView) findViewById(R.id.listViewFilter2);
        listViewCat.setOnItemClickListener(catClick);
        tblOrderItems = (TableLayout) findViewById(R.id.tblOrderItems);
        crsrSettings = db.getBillSettings();
        initViews();
        init();

        com.wep.common.app.ActionBarUtils.setupToolbar(this,toolbar,getSupportActionBar(),CounterSalesCaption,userName," Date:"+s.toString());
        textViewOtherCharges = (TextView) findViewById(R.id.txtOthercharges);
        tvTaxTotal = (TextView) findViewById(R.id.tvTaxTotalValue);
        tvServiceTaxTotal = (TextView) findViewById(R.id.tvServiceTaxValue);
        tvDiscountAmount = (TextView) findViewById(R.id.tvDiscountAmount);
        tvDiscountPercentage = (TextView) findViewById(R.id.tvDiscountPercentage);
        tvSubTotal = (TextView) findViewById(R.id.tvSubTotalValue);
        tvBillAmount = (TextView) findViewById(R.id.tvBillTotalValue);
        ClearAll();
        loadAutoCompleteData();
        loadItems(0);
        Time = Calendar.getInstance();
        ClearAll();
        Cursor crssOtherChrg = db.getKOTModifierByModes_new(CounterSalesCaption);
        double dOtherChrgs = 0;
        if (crssOtherChrg.moveToFirst()) {
            do {
                dOtherChrgs += crssOtherChrg.getDouble(crssOtherChrg.getColumnIndex("ModifierAmount"));
            } while (crssOtherChrg.moveToNext());
            textViewOtherCharges.setText(String.format("%.2f", dOtherChrgs));
        }
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        int id = v.getId();
        if(id == R.id.btn_Clear)
        {
            if(isValidCustmerid())
                ClearAll();
        }
        else if(id == R.id.btn_DineInAddCustomer)
        {
            if(isValidCustmerid())
                addCustomer();
        }
        else if(id == R.id.btn_PrintBill)
        {
            //if(isValidCustmerid())
            printBILL();
        }
        else if(id == R.id.btn_PayBill)
        {
            Tender1();
        }
        else if(id == R.id.btn_DeleteBill)
        {
            deleteBill();
        }
        else if(id == R.id.btn_Reprint)
        {
            reprintBill();
        }
        else if(id == R.id.btnLabel1)
        {
            if(fastBillingMode.equals("3"))
                listViewCat.setVisibility(View.INVISIBLE);
            gridViewItems.setVisibility(View.INVISIBLE);
            loadDepartments();
        }
        else if(id == R.id.btnLabel2)
        {
            listViewDept.setVisibility(View.INVISIBLE);
            gridViewItems.setVisibility(View.INVISIBLE);
            loadCategories(0);
        }
        else if(id == R.id.btnLabel3)
        {
            switch (Integer.parseInt(fastBillingMode))
            {
                case 3 : listViewCat.setVisibility(View.INVISIBLE);
                case 2 : listViewDept.setVisibility(View.INVISIBLE);
            }
            loadItems(0);
        }
    }

    private void loadItems_for_dept(final int deptCode) {
        new AsyncTask<Void, Void, ArrayList<Items>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected ArrayList<Items> doInBackground(Void... params) {
                ArrayList<Items> list = null;
                try {
                    list =  db.getItemItems_dept(deptCode);
                } catch (Exception e) {
                    list = null;
                }
                return list;
            }

            @Override
            protected void onPostExecute(ArrayList<Items> list) {
                super.onPostExecute(list);
               // tvBillNumber.setText(String.valueOf(db.getNewBillNumber()));
                if(list!=null)
                    setItemsAdapter(list);
                gridViewItems.setVisibility(View.VISIBLE);
            }
        }.execute();
    }

    private void loadItems(final int categcode) {
        new AsyncTask<Void, Void, ArrayList<Items>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected ArrayList<Items> doInBackground(Void... params) {
                if(categcode == 0)
                    return db.getItemItems();
                else
                    return db.getItemItems(categcode);
            }

            @Override
            protected void onPostExecute(ArrayList<Items> list) {
                super.onPostExecute(list);
                //tvBillNumber.setText(String.valueOf(db.getNewBillNumber()));
                if(list!=null)
                    setItemsAdapter(list);
                gridViewItems.setVisibility(View.VISIBLE);
            }
        }.execute();
    }

    private void loadDepartments() {
        new AsyncTask<Void, Void, ArrayList<Department>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected ArrayList<Department> doInBackground(Void... params) {
                return db.getItemDepartment();
            }

            @Override
            protected void onPostExecute(ArrayList<Department> list) {
                super.onPostExecute(list);
                //tvBillNumber.setText(String.valueOf(db.getNewBillNumber()));
                if(list!=null)
                    setDepartmentAdapter(list);
                listViewDept.setVisibility(View.VISIBLE);
            }
        }.execute();
    }

    private void loadCategories(final int deptCode) {
        new AsyncTask<Void, Void, ArrayList<Category>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected ArrayList<Category> doInBackground(Void... params) {
                if(deptCode == 0)
                    return db.getAllItemCategory();
                else
                    return db.getAllItemCategory(deptCode);

            }

            @Override
            protected void onPostExecute(ArrayList<Category> list) {
                super.onPostExecute(list);
                //tvBillNumber.setText(String.valueOf(db.getNewBillNumber()));
                if(list!=null)
                    setCategoryAdapter(list);
                listViewCat.setVisibility(View.VISIBLE);
            }
        }.execute();
    }

    public boolean isValidCustmerid(){
        if(customerId==null)
            return false;
        else
            return true;
    }

    public void addCustomer()
    {
        try {
            if (editTextName.getText().toString().equalsIgnoreCase("") || editTextMobile.getText().toString().equalsIgnoreCase("") || editTextAddress.getText().toString().equalsIgnoreCase(""))
            {
                messageDialog.Show("Warning", "Please fill all details before adding customer");
            }
            else if (editTextMobile.getText().toString().length()!= 10)
            {
                messageDialog.Show("Warning", "Please fill 10 digit customer phone number");
                return;
            } else
            {
                Cursor crsrCust = db.getFnbCustomer(editTextMobile.getText().toString());
                if (crsrCust.moveToFirst())
                {
                    messageDialog.Show("Note", "Customer Already Exists");
                }
                else
                {
                    String gstin = etCustGSTIN.getText().toString();
                    if (gstin == null) {
                        gstin = "";
                    }
                    insertCustomer(editTextAddress.getText().toString(), editTextMobile.getText().toString(), editTextName.getText().toString(), 0, 0, 0, gstin);
                    //ResetCustomer();
                    //MsgBox.Show("", "Customer Added Successfully");
                    Toast.makeText(BillingCounterSalesActivity.this, "Customer Added Successfully", Toast.LENGTH_SHORT).show();
                    ControlsSetEnabled();

                }
            }
        } catch (Exception ex) {
            messageDialog.Show("Error", ex.getMessage());
        }
    }

    private void insertCustomer(String strAddress, String strContactNumber, String strName, float fLastTransaction, float fTotalTransaction, float fCreditAmount, String gstin)
    {
        long lRowId;
        Customer objCustomer = new Customer(strAddress, strName, strContactNumber, fLastTransaction, fTotalTransaction, fCreditAmount, gstin);
        lRowId = db.addCustomers(objCustomer);
        if (editTextMobile.getText().toString().length() == 10)
        {
            Cursor crsrCust = db.getFnbCustomer(editTextMobile.getText().toString());
            if (crsrCust.moveToFirst())
            {
                //edtCustId.setText(crsrCust.getString(crsrCust.getColumnIndex("CustId")));
                customerId = crsrCust.getString(crsrCust.getColumnIndex("CustId"));
            }
        }
        Log.d("Customer", "Row Id: " + String.valueOf(lRowId));
    }

    private void ClearAll()
    {
        isReprint = false;
        reprintBillingMode=0;
        tvSubTotal.setText("0.00");
        tvTaxTotal.setText("0.00");
        tvIGSTValue.setText("0.00");
        tvcessValue.setText("0.00");
        tvServiceTaxTotal.setText("0.00");
        tvBillAmount.setText("0.00");
        tvDiscountAmount.setText("0.00");
        tvDiscountPercentage.setText("0.00");
        editTextName.setText("");
        customerId = "0";
        editTextMobile.setText("");
        editTextAddress.setText("");
        tvBillNumber.setText("");
        autoCompleteTextViewSearchItem.setText("");
        autoCompleteTextViewSearchMenuCode.setText("");
        tblOrderItems.removeAllViews();
        etCustGSTIN.setText("");
        chk_interstate.setChecked(false);
        spnr_pos.setSelection(0);
        spnr_pos.setEnabled(false);
        tvBillNumber.setText(String.valueOf(db.getNewBillNumber()));
        setInvoiceDate();
        fTotalDiscount =0;
    }
    void setInvoiceDate()
    {
        Cursor crsrSetting = db.getBillSettings();
        if (crsrSetting.moveToFirst())
        {
            if (crsrSetting.getInt(crsrSetting.getColumnIndex("DateAndTime")) == 1)
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
                String strDate = crsrSetting.getString(crsrSetting.getColumnIndex("BusinessDate"));
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
        }
    }
    private void initViews() {
        btn_PrintBill = (WepButton) findViewById(R.id.btn_PrintBill);
        btn_PrintBill.setOnClickListener(this);
        //btn_PrintBill.setEnabled(false);
        btn_PayBill = (WepButton) findViewById(R.id.btn_PayBill);
        btn_PayBill.setOnClickListener(this);
        btn_Clear = (WepButton) findViewById(R.id.btn_Clear);
        btn_Clear.setOnClickListener(this);
        btn_DeleteBill = (WepButton) findViewById(R.id.btn_DeleteBill);
        btn_DeleteBill.setOnClickListener(this);
        btn_Reprint = (WepButton) findViewById(R.id.btn_Reprint);
        btn_Reprint.setOnClickListener(this);
        //btn_Reprint.setEnabled(false);
        btn_DineInAddCustomer = (WepButton) findViewById(R.id.btn_DineInAddCustomer);
        btn_DineInAddCustomer.setOnClickListener(this);
        btnDept = (Button) findViewById(R.id.btnLabel1);
        btnDept.setOnClickListener(this);
        btnCat = (Button) findViewById(R.id.btnLabel2);
        btnCat.setOnClickListener(this);
        btnItems = (Button) findViewById(R.id.btnLabel3);
        btnItems.setOnClickListener(this);
        spnr_pos = (Spinner) findViewById(R.id.spnr_pos);
        editTextName = (EditText) findViewById(R.id.edtCustName);
        editTextMobile = (EditText) findViewById(R.id.edtCustPhoneNo);
        editTextAddress = (EditText) findViewById(R.id.edtCustAddress);
        idd_date = (LinearLayout) findViewById(R.id.idd_date);

        // GST implementation
        etCustGSTIN = (EditText) findViewById(R.id.etCustGSTIN);
        tvHSNCode_out = (TextView) findViewById(R.id.tvColHSN);
        relative_Interstate = (LinearLayout) findViewById(R.id.relative_interstate);
        tvIGSTValue = (TextView) findViewById(R.id.tvIGSTValue);
        tvcessValue = (TextView) findViewById(R.id.tvcessValue);
        chk_interstate = (CheckBox) findViewById(R.id.checkbox_interstate);
        try{
        autoCompleteTextViewSearchItem = (AutoCompleteTextView) findViewById(R.id.aCTVSearchItem);
        autoCompleteTextViewSearchItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    /*Toast.makeText(BillingScreenActivity.this, aTViewSearchItem.getText().toString(),
                            Toast.LENGTH_SHORT).show();*/
                    if ((autoCompleteTextViewSearchItem.getText().toString().equals(""))) {
                        messageDialog.Show("Warning", "Enter Item Name");
                    } else {
                        Cursor MenucodeItem = db.getItemLists(autoCompleteTextViewSearchItem.getText().toString().trim());
                        if (MenucodeItem.moveToFirst()) {
                            btn_Clear.setEnabled(true);
                            AddItemToOrderTable(MenucodeItem);
                            autoCompleteTextViewSearchItem.setText("");
                            // ((EditText)v).setText("");
                        } else {
                            messageDialog.Show("Warning", "Item not found for Selected Item");
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(BillingCounterSalesActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        autoCompleteTextViewSearchMenuCode = (AutoCompleteTextView) findViewById(R.id.aCTVSearchMenuCode);
        autoCompleteTextViewSearchMenuCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    /*Toast.makeText(BillingScreenActivity.this, aTViewSearchMenuCode.getText().toString(),
                            Toast.LENGTH_SHORT).show();*/
                    if ((autoCompleteTextViewSearchMenuCode.getText().toString().equals(""))) {
                        messageDialog.Show("Warning", "Enter Menu Code");
                    } else {
                        Cursor MenucodeItem = db
                                .getItemss(Integer.parseInt(autoCompleteTextViewSearchMenuCode.getText().toString().trim()));
                        if (MenucodeItem.moveToFirst()) {
                            btn_Clear.setEnabled(true);
                            AddItemToOrderTable(MenucodeItem);
                            autoCompleteTextViewSearchMenuCode.setText("");
                            // ((EditText)v).setText("");
                        } else {
                            messageDialog.Show("Warning", "Item not found for Selected Item Code");
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(BillingCounterSalesActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        tvBillNumber = (EditText) findViewById(R.id.tvBillNumberValue);
        boxDept = (RelativeLayout) findViewById(R.id.boxDept);
        boxCat = (RelativeLayout) findViewById(R.id.boxCat);
        boxItem = (RelativeLayout) findViewById(R.id.boxItem);
        tvDate = (TextView) findViewById(R.id.tvBillDateValue);
        tvWaiterNumber = (EditText) findViewById(R.id.tvWaiterNoValue);
        editTextMobile.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                try {
                    if (editTextMobile.getText().toString().length() == 10) {
                        Cursor crsrCust = db.getFnbCustomer(editTextMobile.getText().toString());
                        if (crsrCust.moveToFirst()) {
                            customerId = crsrCust.getString(crsrCust.getColumnIndex("CustId"));
                            editTextName.setText(crsrCust.getString(crsrCust.getColumnIndex("CustName")));
                            editTextAddress.setText(crsrCust.getString(crsrCust.getColumnIndex("CustAddress")));
                            String gstin = crsrCust.getString(crsrCust.getColumnIndex("GSTIN"));
                            if (gstin == null)
                                etCustGSTIN.setText("");
                            else
                                etCustGSTIN.setText(gstin);
                            ControlsSetEnabled();
                            btn_DineInAddCustomer.setEnabled(false);
                            if (jBillingMode != 2) {
                                btn_PrintBill.setEnabled(false);
                                btn_PayBill.setEnabled(false);
                            } else {
                                btn_PrintBill.setEnabled(true);
                                btn_PayBill.setEnabled(true);
                            }
                        } else {
                            messageDialog.Show("Note", "Customer is not Found, Please Add Customer before Order");
                            btn_DineInAddCustomer.setVisibility(View.VISIBLE);
                            //ControlsSetDisabled();
                            btn_DineInAddCustomer.setEnabled(true);
                        }
                    } else {
                        btn_DineInAddCustomer.setEnabled(true);
                    }
                } catch (Exception ex) {
                    messageDialog.Show("Error " + ex.toString(), ex.getMessage());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        chk_interstate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == false) {
                    //et_pos.setBackgroundColor(Color.WHITE);
                    spnr_pos.setSelection(0);
                    spnr_pos.setEnabled(false);
                    tvIGSTValue.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                    tvTaxTotal.setTextColor(Color.WHITE);
                    tvServiceTaxTotal.setTextColor(Color.WHITE);
                } else {
                    // interstate
                    //et_pos.setBackground(Color.GRAY);
                    spnr_pos.setSelection(0);
                    spnr_pos.setEnabled(true);
                    tvIGSTValue.setTextColor(Color.WHITE);
                    tvTaxTotal.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                    tvServiceTaxTotal.setTextColor(getResources().getColor(R.color.colorPrimaryLight));


                }
            }
        });
        }catch(Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void init() {
        try {
            if (crsrSettings != null && crsrSettings.moveToFirst()) {
                DineInCaption = crsrSettings.getString(crsrSettings.getColumnIndex("HomeDineInCaption"));
                CounterSalesCaption = crsrSettings.getString(crsrSettings.getColumnIndex("HomeCounterSalesCaption"));
                HomeDeliveryCaption = crsrSettings.getString(crsrSettings.getColumnIndex("HomeHomeDeliveryCaption"));
                TakeAwayCaption = crsrSettings.getString(crsrSettings.getColumnIndex("HomeTakeAwayCaption"));

                iTaxType = crsrSettings.getInt(crsrSettings.getColumnIndex("TaxType"));
                ItemwiseDiscountEnabled = crsrSettings.getInt(crsrSettings.getColumnIndex("DiscountType"));

                fastBillingMode = crsrSettings.getString(crsrSettings.getColumnIndex("FastBillingMode"));

                // Handling Null pointer Exception
                if (fastBillingMode == null)
                    fastBillingMode = "";

                if (fastBillingMode.equalsIgnoreCase("1")) {
                    gridViewItems.setNumColumns(6);
                    //GetItemDetails();
                    boxDept.setVisibility(View.GONE);
                    boxCat.setVisibility(View.GONE);
                } else if (fastBillingMode.equalsIgnoreCase("2")) {
                    gridViewItems.setNumColumns(4);
                    //GetItemDetailswithoutDeptCateg();
                    boxCat.setVisibility(View.GONE);
                } else {
            /*GetItemDetailswithoutDeptCateg();
            lstvwDepartment.setAdapter(null);
            lstvwCategory.setAdapter(null);
            grdItems.setAdapter(null);*/
                }
                BillwithStock = crsrSettings.getInt(crsrSettings.getColumnIndex("BillwithStock"));
                businessDate = crsrSettings.getString(crsrSettings.getColumnIndex("BusinessDate"));
                // GSt
                HSNEnable_out = crsrSettings.getString(crsrSettings.getColumnIndex("HSNCode_Out"));
                if (HSNEnable_out == null || HSNEnable_out.equals("")|| HSNEnable_out.equals("0")) {
                    HSNEnable_out = "0";
                    tvHSNCode_out.setVisibility(View.INVISIBLE);
                } else {
                    tvHSNCode_out.setVisibility(View.VISIBLE);
                }

                POSEnable = crsrSettings.getString(crsrSettings.getColumnIndex("POS_Out"));
                if (POSEnable == null || POSEnable.equals("0")) {
                    POSEnable = "0";
                    relative_Interstate.setVisibility(View.INVISIBLE);
                } else {
                    relative_Interstate.setVisibility(View.VISIBLE);
                }
                GSTEnable = "1";
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void setItemsAdapter(ArrayList<Items> list)
    {
        if(itemsAdapter==null){
            itemsAdapter = new ItemsAdapter(this,list);
            gridViewItems.setAdapter(itemsAdapter);
        }
        else
            itemsAdapter.notifyDataSetChanged(list);
    }


    public void setDepartmentAdapter(ArrayList<Department> list)
    {
        if(departmentAdapter==null){
            departmentAdapter = new DepartmentAdapter(this,list);
            listViewDept.setAdapter(departmentAdapter);
        }
        else
            departmentAdapter.notifyDataSetChanged(list);
    }

    public void setCategoryAdapter(ArrayList<Category> list)
    {
        if(categoryAdapter==null){
            categoryAdapter = new CategoryAdapter(this,list);
            listViewCat.setAdapter(categoryAdapter);
        }
        else
            categoryAdapter.notifyDataSetChanged(list);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"Opening Activity Ended");
    }

    @Override
    public void onConfigurationRequired() {

    }

    public void onPrinterAvailable() {
        isPrinterAvailable = true;
        btn_PrintBill.setEnabled(true);
        btn_Reprint.setEnabled(true);
    }

    @Override
    public void onHomePressed() {
        ActionBarUtils.navigateHome(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.wep.common.app.R.menu.menu_main, menu);
        for (int j = 0; j < menu.size(); j++) {
            MenuItem item = menu.getItem(j);
            item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            finish();
        }
        else if (id == com.wep.common.app.R.id.action_home)
        {
            finish();
        }
        else if (id == com.wep.common.app.R.id.action_screen_shot)
        {
            com.wep.common.app.ActionBarUtils.takeScreenshot(this,findViewById(android.R.id.content).getRootView());
        }
        return super.onOptionsItemSelected(item);
    }

    private AdapterView.OnItemClickListener itemsClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Items items = (Items) itemsAdapter.getItem(position);
            Cursor cursor = db.getItemss(items.getItemCode());
            btn_Clear.setEnabled(true);
            AddItemToOrderTable(cursor);
        }
    };

    private AdapterView.OnItemClickListener deptClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Department department = (Department) departmentAdapter.getItem(position);
            int deptCode = department.getDeptCode();
            if(fastBillingMode.equals("3"))// dept+cat+items
            {
                loadCategories(deptCode);
            }
            loadItems_for_dept(deptCode);


        }
    };

    private AdapterView.OnItemClickListener catClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Category cat = (Category) categoryAdapter.getItem(position);
            int categcode = cat.getCategCode();
            loadItems(categcode);

        }
    };

    private void AddItemToOrderTable(Cursor crsrItem)
    {
        EditTextInputHandler etInputValidate = new EditTextInputHandler();
        String strQty = "0";
        double dRate = 0, dTaxPercent = 0, dDiscPercent = 0, dTaxAmt = 0, dIGSTAmt =0, dcessAmt = 0,dDiscAmt = 0, dTempAmt = 0, dServiceTaxPercent = 0;
        double dServiceTaxAmt = 0;
        int iTaxId = 0, iServiceTaxId = 0, iDiscId = 0;
        boolean bItemExists = false;
        TableRow rowItem = null;
        Cursor crsrTax, crsrDiscount;
        TextView tvName, tvAmount, tvTaxPercent, tvTaxAmt, tvDiscPercent, tvDiscAmt, tvDeptCode, tvCategCode, tvKitchenCode, tvTaxType, tvModifierCharge, tvServiceTaxPercent, tvServiceTaxAmt;
        EditText etQty, etRate;
        TextView tvHSn;
        CheckBox chkNumber;
        int crsrSettingsCount = crsrSettings.getCount();
        int crsrItemCount = crsrItem.getCount();
        TextView HSNCode;
        if (crsrItem.moveToFirst() && crsrSettings.moveToFirst())
        {
            do {
                tvServiceTaxPercent = new TextView(this);
                if (iTaxType == 1)
                {
                    String txtServiceTaxPercentage = crsrItem.getString(crsrItem.getColumnIndex("ServiceTaxPercent"));
                    tvServiceTaxPercent.setText(txtServiceTaxPercentage);
                }
                else
                {
                    tvServiceTaxPercent.setText("0");
                }
                // Check for the item in Order table, if present update quantity
                // and amounts
                for (int iRow = 0; iRow < tblOrderItems.getChildCount(); iRow++)
                {
                    TableRow Row = (TableRow) tblOrderItems.getChildAt(iRow);
                    // Check against item number present in table
                    if (Row.getChildAt(0) != null)
                    {
                        CheckBox Number = (CheckBox) Row.getChildAt(0);
                        TextView ItemName = (TextView) Row.getChildAt(1);
                        TextView PrintKOTStatus = (TextView) Row.getChildAt(21);
                        // Check for item number and name, if name is not same
                        // add new
                        if (Number.getText().toString().equalsIgnoreCase(crsrItem.getString(crsrItem.getColumnIndex("MenuCode"))) && ItemName.getText().toString().equalsIgnoreCase(crsrItem.getString(crsrItem.getColumnIndex("ItemName"))))
                        {
                            if (PrintKOTStatus.getText().toString().equalsIgnoreCase("0"))
                            {
                                EditText Qty = (EditText) Row.getChildAt(3);
                                Qty.setEnabled(false);
                            }
                            else
                            {
                                // Quantity
                                EditText Qty = (EditText) Row.getChildAt(3);
                                Qty.setSelectAllOnFocus(true);
                                strQty = Qty.getText().toString().equalsIgnoreCase("") ? "0" : Qty.getText().toString(); // Temp

                                if (BillwithStock == 1)
                                {
                                    String availableqty = crsrItem.getString(crsrItem.getColumnIndex("Quantity"));
                                    if (crsrItem.getFloat(crsrItem.getColumnIndex("Quantity")) < (Float.valueOf(strQty) + 1))
                                    {
                                        messageDialog.Show("Warning", "Stock is less, present stock quantity is " + availableqty);
                                        Qty.setText(String.format("%.2f", Double.parseDouble(availableqty)) );
                                        return;
                                    }
                                    else
                                    {
                                        Qty.setText(String.format("%.2f", Double.parseDouble(strQty) + 1));
                                    }
                                }
                                else
                                {
                                    Qty.setText(String.format("%.2f", Double.parseDouble(strQty) + 1));
                                }

                                // Amount
                                EditText Rate = (EditText) Row.getChildAt(4);
                                Rate.setSelectAllOnFocus(true);
                                TextView Amount = (TextView) Row.getChildAt(5);
                                dRate = Double.parseDouble(Rate.getText().toString().equalsIgnoreCase("") ? "0" : Rate.getText().toString()); // Temp

                                // Tax and Discount Amount
                                TextView TaxPer = (TextView) Row.getChildAt(6);
                                TextView TaxAmt = (TextView) Row.getChildAt(7);
                                TextView DiscPer = (TextView) Row.getChildAt(8);
                                TextView DiscAmt = (TextView) Row.getChildAt(9);
                                TextView ServiceTax = (TextView) Row.getChildAt(15);
                                TextView ServiceTaxAmt = (TextView) Row.getChildAt(16);
                                TextView IGSTRate = (TextView) Row.getChildAt(23);
                                TextView IGSTAmt = (TextView) Row.getChildAt(24);
                                TextView cessRate = (TextView) Row.getChildAt(25);
                                TextView cessAmt = (TextView) Row.getChildAt(26);


                                dTaxPercent = Double.parseDouble(TaxPer.getText().toString().equalsIgnoreCase("") ? "0"
                                        : TaxPer.getText().toString()); // Temp
                                dServiceTaxPercent = Double.parseDouble(ServiceTax.getText().toString().equalsIgnoreCase("") ? "0"
                                        : ServiceTax.getText().toString()); // Tempd
                                double dIGSTRate = Double.parseDouble(IGSTRate.getText().toString().equalsIgnoreCase("") ? "0"
                                        : IGSTRate.getText().toString()); // Temp
                                double dcessRate  = Double.parseDouble(cessRate.getText().toString().equalsIgnoreCase("") ? "0"
                                        : cessRate.getText().toString()); // Temp
                                dDiscPercent = Double.parseDouble(DiscPer.getText().toString().equalsIgnoreCase("") ? "0"
                                        : DiscPer.getText().toString()); // Temp

                                if (crsrSettings.getInt(crsrSettings.getColumnIndex("Tax")) == 1) { // forward tax
                                    // Discount
                                    dDiscAmt = dRate * (dDiscPercent / 100);
                                    dTempAmt = dDiscAmt;
                                    dDiscAmt = dDiscAmt * Double.parseDouble(Qty.getText().toString());

                                    // Tax
                                    dTaxAmt = (dRate - dTempAmt) * (dTaxPercent / 100);
                                    dTaxAmt = dTaxAmt * Double.parseDouble(Qty.getText().toString());

                                    dServiceTaxAmt = (dRate - dTempAmt) * (dServiceTaxPercent / 100);
                                    dServiceTaxAmt = dServiceTaxAmt * Double.parseDouble(Qty.getText().toString());

                                    dIGSTAmt = (dRate - dTempAmt) * (dIGSTRate / 100);
                                    dIGSTAmt = dIGSTAmt * Double.parseDouble(Qty.getText().toString());

                                    dcessAmt = (dRate - dTempAmt) * (dcessRate / 100);
                                    dcessAmt = dcessAmt * Double.parseDouble(Qty.getText().toString());

                                    TaxAmt.setText(String.format("%.2f", dTaxAmt));
                                    DiscAmt.setText(String.format("%.2f", dDiscAmt));
                                    ServiceTaxAmt.setText(String.format("%.2f", dServiceTaxAmt));
                                    cessAmt.setText(String.format("%.2f", dcessAmt));
                                    IGSTAmt.setText(String.format("%.2f", dIGSTAmt));
                                    Amount.setText(String.format("%.2f", (Double.parseDouble(Qty.getText().toString()) * (dRate-dTempAmt))));

                                } else {// reverse tax
                                    double dBasePrice = 0;
                                    dBasePrice = dRate *(1-(dDiscPercent/100))/ (1 + (dTaxPercent / 100)+(dServiceTaxPercent/100));

                                    // Discount
                                    dDiscAmt = dBasePrice * (dDiscPercent / 100);
                                    dTempAmt = dDiscAmt;
                                    dDiscAmt = dDiscAmt * Double.parseDouble(Qty.getText().toString());

                                    // Tax
                                    dTaxAmt = (dBasePrice ) * (dTaxPercent / 100);
                                    dTaxAmt = dTaxAmt * Double.parseDouble(Qty.getText().toString());
                                    //Service tax
                                    dServiceTaxAmt = (dBasePrice ) * (dServiceTaxPercent / 100);
                                    dServiceTaxAmt = dServiceTaxAmt * Double.parseDouble(Qty.getText().toString());

                                    dIGSTAmt = (dBasePrice ) * (dIGSTRate/ 100);
                                    dIGSTAmt = dIGSTAmt * Double.parseDouble(Qty.getText().toString());
                                    //Service tax
                                    dcessAmt = (dBasePrice ) * (dcessRate / 100);
                                    dcessAmt = dcessAmt * Double.parseDouble(Qty.getText().toString());

                                    ServiceTaxAmt.setText(String.format("%.2f", dServiceTaxAmt));
                                    TaxAmt.setText(String.format("%.2f", dTaxAmt));
                                    DiscAmt.setText(String.format("%.2f", dDiscAmt));
                                    cessAmt.setText(String.format("%.2f", dcessAmt));
                                    IGSTAmt.setText(String.format("%.2f", dIGSTAmt));
                                    Amount.setText(String.format("%.2f", (Double.parseDouble(Qty.getText().toString()) * (dRate))));
                                }


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
                                dIGSTAmt =0;
                                dcessAmt =0;
                                dcessRate =0;
                                dIGSTRate =0;
                                bItemExists = true;

                                break;
                            }


                        }
                    }
                }


                if (bItemExists == false) {

                    rowItem = new TableRow(BillingCounterSalesActivity.this);
                    rowItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    //crsrSettings = db.getBillSetting();

                    int DineInRate = crsrSettings.getInt(crsrSettings.getColumnIndex("DineInRate"));
                    int CounterSalesRate = crsrSettings.getInt(crsrSettings.getColumnIndex("CounterSalesRate"));
                    int PickUpRate = crsrSettings.getInt(crsrSettings.getColumnIndex("PickUpRate"));
                    int HomeDeliveryRate = crsrSettings.getInt(crsrSettings.getColumnIndex("HomeDeliveryRate"));
                    if (CounterSalesRate == 1) {
                        dRate = crsrItem.getInt(crsrItem.getColumnIndex("DineInPrice1"));
                    } else if (CounterSalesRate == 2) {
                        dRate = crsrItem.getInt(crsrItem.getColumnIndex("DineInPrice2"));
                    } else if (CounterSalesRate == 3) {
                        dRate = crsrItem.getInt(crsrItem.getColumnIndex("DineInPrice3"));
                    }

                    // Menu Code
                    chkNumber = new CheckBox(BillingCounterSalesActivity.this);
                    chkNumber.setWidth(40); // 57px ~= 85dp
                    chkNumber.setTextSize(0);
                    chkNumber.setTextColor(Color.TRANSPARENT);
                    chkNumber.setText(crsrItem.getString(crsrItem.getColumnIndex("MenuCode")));
                    //Toast.makeText(getApplicationContext(), chkNumber.getText().toString(), Toast.LENGTH_SHORT).show();

                    // Item Name
                    tvName = new TextView(BillingCounterSalesActivity.this);
                    tvName.setWidth(135); // 154px ~= 230dp
                    tvName.setTextSize(11);
                    tvName.setText(crsrItem.getString(crsrItem.getColumnIndex("ItemName")));

                    //hsn code
                    tvHSn = new TextView(BillingCounterSalesActivity.this);
                    tvHSn.setWidth(67); // 154px ~= 230dp
                    tvHSn.setTextSize(11);
                    tvHSn.setText(crsrItem.getString(crsrItem.getColumnIndex("HSNCode")));
                    if ( !HSNEnable_out.equals("1")) {
                        tvHSn.setVisibility(View.INVISIBLE);
                    }

                    // Quantity
                    etQty = new EditText(BillingCounterSalesActivity.this);
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
                    etQty.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(4,1)});
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

                    if (BillwithStock == 1) {
                        if (crsrItem.getFloat(crsrItem.getColumnIndex("Quantity")) < Float.valueOf(etQty.getText().toString())) {
                            String availableQty = crsrItem.getString(crsrItem.getColumnIndex("Quantity")) ;
                            messageDialog.Show("Warning", "Stock is less, present stock quantity is "
                                    + availableQty);
                            etQty.setText(String.format("%.2f", Double.parseDouble(availableQty)));
                            return;
                        }
                    }

                    // Rate
                    etRate = new EditText(BillingCounterSalesActivity.this);
                    etRate.setWidth(70); // 74px ~= 110dp
                    etRate.setTextSize(11);
                    etRate.setSelectAllOnFocus(true);
                    etRate.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(4,1)});
                    etRate.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    etRate.setText(String.format("%.2f", dRate));
                    etRate.setTag("QTY_RATE");
                    // Check whether Price change is enabled for the item, if
                    // not set Rate text box click able property to false
                    if (crsrSettings.getInt(crsrSettings.getColumnIndex("PriceChange")) == 0) {
                        etRate.setEnabled(false);
                    } else {
                        etRate.addTextChangedListener(new TextWatcher() {
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            public void afterTextChanged(Editable s) {
                                Qty_Rate_Edit();
                            }
                        });
                        etRate.setOnClickListener(Qty_Rate_Click);
                        etRate.setOnKeyListener(Qty_Rate_KeyPressEvent);
                        etInputValidate.ValidateDecimalInput(etRate);
                    }




                    // Discount Percent - Check whether Discount is enabled for
                    // the item,
                    // if enabled get discount percentage from discount table
                    /*if (crsrItem.getInt(crsrItem.getColumnIndex("DiscountEnable")) == 1) {
                        iDiscId = crsrItem.getInt(crsrItem.getColumnIndex("DiscId"));
                        crsrDiscount = db.getDiscountConfig(iDiscId);
                        if (!crsrDiscount.moveToFirst()) {
                            messageDialog.Show("Warning", "Failed to read Discount from crsrDiscount");
                            return;
                        } else {

                            dDiscPercent = crsrDiscount.getDouble(crsrDiscount.getColumnIndex("DiscPercentage"));
                        }
                    }
                    tvDiscPercent = new TextView(BillingCounterSalesActivity.this);
                    tvDiscPercent.setWidth(50);
                    tvDiscPercent.setText(String.format("%.2f", dDiscPercent));*/

                    if(ItemwiseDiscountEnabled ==1 && crsrItem.getString(crsrItem.getColumnIndex("DiscountPercent"))!=null)  // 1->itemwise discount , 0-> billwise discount
                    {
                        dDiscPercent = Double.parseDouble(String.format("%.2f",
                                                            crsrItem.getDouble(crsrItem.getColumnIndex("DiscountPercent"))));
                    }
                    tvDiscPercent = new TextView(BillingCounterSalesActivity.this);
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
                    tvDiscAmt = new TextView(BillingCounterSalesActivity.this);
                    tvDiscAmt.setWidth(50);
                    tvDiscAmt.setText(String.format("%.2f", dDiscAmt));
// Amount
                    tvAmount = new TextView(BillingCounterSalesActivity.this);
                    tvAmount.setWidth(60); // 97px ~= 145dp
                    tvAmount.setTextSize(11);
                    tvAmount.setGravity(Gravity.RIGHT | Gravity.END);
                    tvAmount.setText(String.format("  %.2f", dRate-dDiscAmt));

                    dTaxPercent = crsrItem.getDouble(crsrItem.getColumnIndex("CGSTRate"));
                    tvTaxPercent = new TextView(BillingCounterSalesActivity.this);
                    tvTaxPercent.setText(String.format("%.2f", dTaxPercent));


                    dServiceTaxPercent = crsrItem.getDouble(crsrItem.getColumnIndex("SGSTRate"));
                    tvServiceTaxPercent = new TextView(BillingCounterSalesActivity.this);
                    tvServiceTaxPercent.setText(String.format("%.2f", dServiceTaxPercent));


                    double dcessPercent = crsrItem.getDouble(crsrItem.getColumnIndex("cessRate"));
                    TextView tvcess = new TextView(BillingCounterSalesActivity.this);
                    tvcess.setText(String.format("%.2f",dcessPercent));

                    double dIGSTPercent = crsrItem.getDouble(crsrItem.getColumnIndex("IGSTRate"));
                    TextView tvIGSTRate = new TextView(BillingCounterSalesActivity.this);
                    tvIGSTRate.setText(String.format("%.2f",dIGSTPercent));


                    // Tax Amount
                    if (crsrSettings.getInt(crsrSettings.getColumnIndex("Tax")) == 1) { // Forward
                        // Tax
                        dTaxAmt = (dRate - dDiscAmt) * (dTaxPercent / 100);
                        dServiceTaxAmt = (dRate - dDiscAmt) * (dServiceTaxPercent / 100);
                        dIGSTAmt = (dRate - dDiscAmt) * (dIGSTPercent / 100);
                        dcessAmt = (dRate - dDiscAmt) * (dcessPercent / 100);
                    } else { // Reverse Tax
                        double dBasePrice = dRate *(1-(dDiscPercent/100))/ (1 + (dTaxPercent / 100)+(dServiceTaxPercent / 100));
                        dTaxAmt = (dBasePrice) * (dTaxPercent / 100);
                        dServiceTaxAmt = (dBasePrice ) * (dServiceTaxPercent / 100);
                        dIGSTAmt = (dBasePrice) * (dIGSTPercent / 100);
                        dcessAmt = (dBasePrice ) * (dcessPercent / 100);
                    }
                    tvTaxAmt = new TextView(BillingCounterSalesActivity.this);
                    tvTaxAmt.setWidth(50);
                    tvTaxAmt.setText(String.format("%.2f", dTaxAmt));

                    tvServiceTaxAmt = new TextView(BillingCounterSalesActivity.this);
                    tvServiceTaxAmt.setWidth(50);
                    tvServiceTaxAmt.setText(String.format("%.2f", dServiceTaxAmt));

                    TextView tvIGSTAmt = new TextView(BillingCounterSalesActivity.this);
                    tvIGSTAmt.setWidth(50);
                    tvIGSTAmt.setText(String.format("%.2f", dIGSTAmt));

                    TextView tvcessAmt = new TextView(BillingCounterSalesActivity.this);
                    tvcessAmt.setWidth(50);
                    tvcessAmt.setText(String.format("%.2f", dcessAmt));



                    // Department Code
                    tvDeptCode = new TextView(BillingCounterSalesActivity.this);
                    tvDeptCode.setWidth(50);
                    tvDeptCode.setText(crsrItem.getString(crsrItem.getColumnIndex("DeptCode")));

                    // Category Code
                    tvCategCode = new TextView(BillingCounterSalesActivity.this);
                    tvCategCode.setWidth(50);
                    tvCategCode.setText(crsrItem.getString(crsrItem.getColumnIndex("CategCode")));

                    // Kitchen Code
                    tvKitchenCode = new TextView(BillingCounterSalesActivity.this);
                    tvKitchenCode.setWidth(50);
                    tvKitchenCode.setText(crsrItem.getString(crsrItem.getColumnIndex("KitchenCode")));

                    // Tax Type [Forward - 1/ Reverse - 0]
                    tvTaxType = new TextView(BillingCounterSalesActivity.this);
                    tvTaxType.setWidth(50);
                    //tvTaxType.setText(crsrItem.getString(crsrItem.getColumnIndex("TaxType")));
                    tvTaxType.setText(crsrSettings.getString(crsrSettings.getColumnIndex("Tax")));

                    // Modifier Charge
                    tvModifierCharge = new TextView(BillingCounterSalesActivity.this);
                    tvModifierCharge.setWidth(50);
                    tvModifierCharge.setText("0.0");

                    TextView tvUOM = new TextView(BillingCounterSalesActivity.this);
                    tvUOM.setWidth(50);
                    tvUOM.setText(crsrItem.getString(crsrItem.getColumnIndex("UOM")));


                    // SupplyType
                    TextView SupplyType = new TextView(BillingCounterSalesActivity.this);
                    SupplyType.setText(crsrItem.getString(crsrItem.getColumnIndex("SupplyType")));
                    SupplyType.setWidth(30);

                    TextView tvSpace = new TextView(BillingCounterSalesActivity.this);
                    tvSpace.setText("        ");

                    // Delete
                    int res = getResources().getIdentifier("delete", "drawable", this.getPackageName());
                    ImageButton ImgDelete = new ImageButton(BillingCounterSalesActivity.this);
                    ImgDelete.setImageResource(res);
                    // btnDelete.setText(crsrItem.getString(crsrItem.getColumnIndex("MenuCode")));
                    ImgDelete.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v)
                        {
                            final View v1 = v;
                            AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(BillingCounterSalesActivity.this);
                            /*LayoutInflater UserAuthorization = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View vwAuthorization = UserAuthorization.inflate(R.layout.deleteconfirmation, null);*/
                            AuthorizationDialog
                                    .setIcon(R.drawable.ic_launcher)
                                    .setTitle("Confirmation")
                                    .setMessage("Are you sure to delete this item")
                                    .setNegativeButton("Cancel", null)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            View row = (View) v1.getParent();
                                            ViewGroup container = ((ViewGroup) row.getParent());
                                            container.removeView(row);
                                            container.invalidate();
                                            CalculateTotalAmount();

                                        }
                                    }).show();
                        }
                    });
                    TextView tvSpace1 = new TextView(BillingCounterSalesActivity.this);
                    tvSpace1.setText("       ");
                    TextView tvPrintKOTStatus = new TextView(BillingCounterSalesActivity.this);
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
                    rowItem.addView(tvIGSTRate);//23
                    rowItem.addView(tvIGSTAmt);//24
                    rowItem.addView(tvcess);//25
                    rowItem.addView(tvcessAmt);//26
                    tblOrderItems.addView(rowItem, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
                bItemExists = false;
            } while (crsrItem.moveToNext());
            CalculateTotalAmount();
        }
        else
        {
            Log.d("AddItemToOrderTable", "ItemNotFound Exception");
        }
    }


    /*************************************************************************************************************************************
     * Calculates all values such as Tax, Discount, Sub Total and grand total
     * whenever Quantity or Rate text value changed
     *************************************************************************************************************************************/
    private void Qty_Rate_Edit() {

        double strQty = 0;
        double dTaxPercent = 0,dServiceTaxPercent = 0, dDiscPercent = 0, dDiscAmt = 0, dTempAmt = 0, dTaxAmt = 0,dServiceTaxAmt =0;
        double dRate,dIGSTAmt=0, dcessAmt=0;
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
                    strQty = Double.parseDouble(
                            Qty.getText().toString().equalsIgnoreCase("") ? "0" : Qty.getText().toString()); // Temp
                    if (BillwithStock == 1) {
                        Cursor ItemCrsr = db.getItemDetail(ItemName.getText().toString());
                        if(ItemCrsr!=null && ItemCrsr.moveToFirst())
                        {
                            double availableStock = ItemCrsr.getDouble(ItemCrsr.getColumnIndex("Quantity"));
                            if ( availableStock < strQty) {
                                messageDialog.Show("Warning", "Stock is less, present stock quantity is "
                                        + String.valueOf(availableStock));
                                Qty.setText(String.format("%.2f", availableStock));

                                return;
                            }
                        }

                    }





                    // Amount
                    EditText Rate = (EditText) Row.getChildAt(4);
                    Rate.setSelectAllOnFocus(true);
                    TextView Amount = (TextView) Row.getChildAt(5);
                    dRate = Double.parseDouble(
                            Rate.getText().toString().equalsIgnoreCase("") ? "0" : Rate.getText().toString()); // Temp


                    // Tax and Discount Amount
                    TextView TaxPer = (TextView) Row.getChildAt(6);
                    TextView TaxAmt = (TextView) Row.getChildAt(7);
                    TextView DiscPer = (TextView) Row.getChildAt(8);
                    TextView DiscAmt = (TextView) Row.getChildAt(9);
                    TextView ServiceTax = (TextView) Row.getChildAt(15);
                    TextView ServiceTaxAmt = (TextView) Row.getChildAt(16);
                    TextView IGSTRate = (TextView) Row.getChildAt(23);
                    TextView IGSTAmt = (TextView) Row.getChildAt(24);
                    TextView cessRate = (TextView) Row.getChildAt(25);
                    TextView cessAmt = (TextView) Row.getChildAt(26);

                    dTaxPercent = Double.parseDouble(TaxPer.getText().toString().equalsIgnoreCase("") ? "0"
                            : TaxPer.getText().toString()); // Temp
                    dServiceTaxPercent = Double.parseDouble(ServiceTax.getText().toString().equalsIgnoreCase("") ? "0"
                            : ServiceTax.getText().toString()); // Temp
                    double dIGSTRate = Double.parseDouble(IGSTRate.getText().toString().equalsIgnoreCase("") ? "0"
                            : IGSTRate.getText().toString()); // Temp
                    double dcessRate  = Double.parseDouble(cessRate.getText().toString().equalsIgnoreCase("") ? "0"
                            : cessRate.getText().toString()); // Temp
                    dDiscPercent = Double.parseDouble(DiscPer.getText().toString().equalsIgnoreCase("") ? "0"
                            : DiscPer.getText().toString()); // Temp

                    if (crsrSettings.getInt(crsrSettings.getColumnIndex("Tax")) == 1) { // forward tax
                        // Discount
                        dDiscAmt = dRate * (dDiscPercent / 100);
                        dTempAmt = dDiscAmt;
                        dDiscAmt = dDiscAmt * strQty;

                        // Tax
                        dTaxAmt = (dRate - dTempAmt) * (dTaxPercent / 100);
                        dTaxAmt = dTaxAmt * strQty;

                        dServiceTaxAmt = (dRate - dTempAmt) * (dServiceTaxPercent / 100);
                        dServiceTaxAmt = dServiceTaxAmt * strQty;

                        dIGSTAmt = (dRate - dTempAmt) * (dIGSTRate / 100);
                        dIGSTAmt = dIGSTAmt * strQty;

                        dcessAmt = (dRate - dTempAmt) * (dcessRate / 100);
                        dcessAmt = dcessAmt * strQty;



                        TaxAmt.setText(String.format("%.2f", dTaxAmt));
                        DiscAmt.setText(String.format("%.2f", dDiscAmt));
                        ServiceTaxAmt.setText(String.format("%.2f", dServiceTaxAmt));
                        cessAmt.setText(String.format("%.2f", dcessAmt));
                        IGSTAmt.setText(String.format("%.2f", dIGSTAmt));
                        Amount.setText(
                                String.format("%.2f", (strQty * (dRate-dDiscAmt))));

                    } else {// reverse tax
                        double dBasePrice = 0;
                        dBasePrice = dRate / (1 + (dTaxPercent / 100)+(dServiceTaxPercent/100));

                        // Discount
                        dDiscAmt = dBasePrice * (dDiscPercent / 100);
                        dTempAmt = dDiscAmt;
                        dDiscAmt = dDiscAmt * Double.parseDouble(Qty.getText().toString());

                        // Tax
                        dTaxAmt = (dBasePrice - dTempAmt) * (dTaxPercent / 100);
                        dTaxAmt = dTaxAmt * Double.parseDouble(Qty.getText().toString());

                        dIGSTAmt = (dBasePrice ) * (dIGSTRate/ 100);
                        dIGSTAmt = dIGSTAmt * Double.parseDouble(Qty.getText().toString());

                        dcessAmt = (dBasePrice ) * (dcessRate / 100);
                        dcessAmt = dcessAmt * Double.parseDouble(Qty.getText().toString());

                        //Service tax
                        dServiceTaxAmt = (dBasePrice - dTempAmt) * (dServiceTaxPercent / 100);
                        dServiceTaxAmt = dServiceTaxAmt * Double.parseDouble(Qty.getText().toString());
                        ServiceTaxAmt.setText(String.format("%.2f", dServiceTaxAmt));

                        TaxAmt.setText(String.format("%.2f", dTaxAmt));
                        DiscAmt.setText(String.format("%.2f", dDiscAmt));
                        cessAmt.setText(String.format("%.2f", dcessAmt));
                        IGSTAmt.setText(String.format("%.2f", dIGSTAmt));
                        Amount.setText(
                                String.format("%.2f", (strQty * (dRate-dDiscAmt))));
                    }
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
            messageDialog.setMessage("Error while changing quantity directly :" + e.getMessage()).setPositiveButton("OK", null).show();
            e.printStackTrace();
        }
    }


    private void Qty_Rate_Edit_old() {

        double strQty = 0;
        double dTaxPercent = 0,dServiceTaxPercent = 0, dDiscPercent = 0, dDiscAmt = 0, dTempAmt = 0, dTaxAmt = 0,dServiceTaxAmt =0;
        double dRate,dIGSTAmt=0, dcessAmt=0;
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
                    if(Qty.getText().toString().equalsIgnoreCase(""))
                        Qty.setText("0.00");
                    strQty = Double.parseDouble(
                            Qty.getText().toString().equalsIgnoreCase("") ? "0" : Qty.getText().toString()); // Temp
                    if (BillwithStock == 1) {
                        Cursor ItemCrsr = db.getItemDetail(ItemName.getText().toString());
                        if(ItemCrsr!=null && ItemCrsr.moveToFirst())
                        {
                            double availableStock = ItemCrsr.getDouble(ItemCrsr.getColumnIndex("Quantity"));
                            if ( availableStock < strQty) {
                                messageDialog.Show("Warning", "Stock is less, present stock quantity is "
                                        + String.valueOf(availableStock));
                                Qty.setText(String.format("%.2f", availableStock));

                                return;
                            }
                        }

                    }




                    // Amount
                    EditText Rate = (EditText) Row.getChildAt(4);
                    Rate.setSelectAllOnFocus(true);
                    TextView Amount = (TextView) Row.getChildAt(5);
                    dRate = Double.parseDouble(
                            Rate.getText().toString().equalsIgnoreCase("") ? "0" : Rate.getText().toString()); // Temp


                    // Tax and Discount Amount
                    TextView TaxPer = (TextView) Row.getChildAt(6);
                    TextView TaxAmt = (TextView) Row.getChildAt(7);
                    TextView DiscPer = (TextView) Row.getChildAt(8);
                    TextView DiscAmt = (TextView) Row.getChildAt(9);
                    TextView ServiceTax = (TextView) Row.getChildAt(15);
                    TextView ServiceTaxAmt = (TextView) Row.getChildAt(16);
                    TextView IGSTRate = (TextView) Row.getChildAt(23);
                    TextView IGSTAmt = (TextView) Row.getChildAt(24);
                    TextView cessRate = (TextView) Row.getChildAt(25);
                    TextView cessAmt = (TextView) Row.getChildAt(26);

                    dTaxPercent = Double.parseDouble(TaxPer.getText().toString().equalsIgnoreCase("") ? "0"
                            : TaxPer.getText().toString()); // Temp
                    dServiceTaxPercent = Double.parseDouble(ServiceTax.getText().toString().equalsIgnoreCase("") ? "0"
                            : ServiceTax.getText().toString()); // Temp
                    double dIGSTRate = Double.parseDouble(IGSTRate.getText().toString().equalsIgnoreCase("") ? "0"
                            : IGSTRate.getText().toString()); // Temp
                    double dcessRate  = Double.parseDouble(cessRate.getText().toString().equalsIgnoreCase("") ? "0"
                            : cessRate.getText().toString()); // Temp
                    dDiscPercent = Double.parseDouble(DiscPer.getText().toString().equalsIgnoreCase("") ? "0"
                            : DiscPer.getText().toString()); // Temp

                    if (crsrSettings.getInt(crsrSettings.getColumnIndex("Tax")) == 1) { // forward tax
                        // Discount
                        dDiscAmt = dRate * (dDiscPercent / 100);
                        dTempAmt = dDiscAmt;
                        dDiscAmt = dDiscAmt * strQty;

                        // Tax
                        dTaxAmt = (dRate - dTempAmt) * (dTaxPercent / 100);
                        dTaxAmt = dTaxAmt * strQty;

                        dServiceTaxAmt = (dRate - dTempAmt) * (dServiceTaxPercent / 100);
                        dServiceTaxAmt = dServiceTaxAmt * strQty;

                        dIGSTAmt = (dRate - dTempAmt) * (dIGSTRate / 100);
                        dIGSTAmt = dIGSTAmt * strQty;

                        dcessAmt = (dRate - dTempAmt) * (dcessRate / 100);
                        dcessAmt = dcessAmt * strQty;



                        TaxAmt.setText(String.format("%.2f", dTaxAmt));
                        DiscAmt.setText(String.format("%.2f", dDiscAmt));
                        ServiceTaxAmt.setText(String.format("%.2f", dServiceTaxAmt));
                        cessAmt.setText(String.format("%.2f", dcessAmt));
                        IGSTAmt.setText(String.format("%.2f", dIGSTAmt));
                        Amount.setText(
                                String.format("%.2f", (strQty * (dRate-dDiscAmt))));

                    } else {// reverse tax
                        double dBasePrice = 0;
                        dBasePrice = dRate / (1 + (dTaxPercent / 100)+(dServiceTaxPercent/100));

                        // Discount
                        dDiscAmt = dBasePrice * (dDiscPercent / 100);
                        dTempAmt = dDiscAmt;
                        dDiscAmt = dDiscAmt * Double.parseDouble(Qty.getText().toString());

                        // Tax
                        dTaxAmt = (dBasePrice - dTempAmt) * (dTaxPercent / 100);
                        dTaxAmt = dTaxAmt * Double.parseDouble(Qty.getText().toString());

                        dIGSTAmt = (dBasePrice ) * (dIGSTRate/ 100);
                        dIGSTAmt = dIGSTAmt * Double.parseDouble(Qty.getText().toString());

                        dcessAmt = (dBasePrice ) * (dcessRate / 100);
                        dcessAmt = dcessAmt * Double.parseDouble(Qty.getText().toString());

                        //Service tax
                        dServiceTaxAmt = (dBasePrice - dTempAmt) * (dServiceTaxPercent / 100);
                        dServiceTaxAmt = dServiceTaxAmt * Double.parseDouble(Qty.getText().toString());
                        ServiceTaxAmt.setText(String.format("%.2f", dServiceTaxAmt));

                        TaxAmt.setText(String.format("%.2f", dTaxAmt));
                        DiscAmt.setText(String.format("%.2f", dDiscAmt));
                        cessAmt.setText(String.format("%.2f", dcessAmt));
                        IGSTAmt.setText(String.format("%.2f", dIGSTAmt));
                        Amount.setText(
                                String.format("%.2f", (strQty * (dRate-dDiscAmt))));
                    }

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
                    dIGSTAmt =0;
                    dcessAmt =0;
                    dcessRate =0;
                    dIGSTRate =0;
                    //bItemExists = true;

                }

            }
            CalculateTotalAmount();
        } catch (Exception e) {
            messageDialog.setMessage("Error while changing quantity directly :" + e.getMessage()).setPositiveButton("OK", null).show();
            e.printStackTrace();
        }
    }

    /*************************************************************************************************************************************
     * Calculates bill sub total, sales tax amount, service tax amount and Bill
     * total amount.
     ************************************************************************************************************************************/

    /*************************************************************************************************************************************
     * Quantity / Rate column text box click event listener which selects all
     * the text present in text box
     *************************************************************************************************************************************/
    View.OnClickListener Qty_Rate_Click = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            ((EditText) v).setSelection(((EditText) v).getText().length());
        }

    };

    /*************************************************************************************************************************************
     * Quantity / Rate column text key press event listener
     *************************************************************************************************************************************/
    View.OnKeyListener Qty_Rate_KeyPressEvent = new View.OnKeyListener() {

        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (v.getTag().toString().equalsIgnoreCase("QTY_RATE"))
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(((EditText) v).getWindowToken(), 0);
                    Qty_Rate_Edit();
                }
            }
            return false;
        }
    };

    private String getQuantityFromWeighScale() {
        return "1";
    }

    private int getIndex_pos(String substring){

        int index = 0;
        for (int i = 0; index==0 && i < spnr_pos.getCount(); i++){

            if (spnr_pos.getItemAtPosition(i).toString().contains(substring)){
                index = i;
            }

        }

        return index;

    }
    private String getState_pos(String substring){

        String  index = "";
        for (int i = 0; i < spnr_pos.getCount(); i++){

            if (spnr_pos.getItemAtPosition(i).toString().contains(substring)){
                index = spnr_pos.getItemAtPosition(i).toString();
                break;
            }

        }

        return index;

    }

    /*************************************************************************************************************************************
     * Calculates bill sub total, sales tax amount, service tax amount and Bill
     * total amount.
     ************************************************************************************************************************************/
    private void CalculateTotalAmount()
    {
        double dSubTotal = 0, dTaxTotal = 0, dModifierAmt = 0, dServiceTaxAmt = 0, dOtherCharges = 0, dTaxAmt = 0, dSerTaxAmt = 0;
        float dTaxPercent = 0, dSerTaxPercent = 0;
        double dIGSTAmt =0, dcessAmt =0;
        // Item wise tax calculation ----------------------------
        for (int iRow = 0; iRow < tblOrderItems.getChildCount(); iRow++)
        {
            TableRow RowItem = (TableRow) tblOrderItems.getChildAt(iRow);
            if (RowItem.getChildAt(0) != null)
            {
                TextView ColTaxType = (TextView) RowItem.getChildAt(13);
                TextView ColAmount = (TextView) RowItem.getChildAt(5);
                TextView ColDisc = (TextView) RowItem.getChildAt(9);
                TextView ColTax = (TextView) RowItem.getChildAt(7);
                TextView ColModifierAmount = (TextView) RowItem.getChildAt(14);
                TextView ColServiceTaxAmount = (TextView) RowItem.getChildAt(16);
                TextView ColIGSTAmount = (TextView) RowItem.getChildAt(24);
                TextView ColcessAmount = (TextView) RowItem.getChildAt(26);
                dTaxTotal += Double.parseDouble(ColTax.getText().toString());
                dServiceTaxAmt += Double.parseDouble(ColServiceTaxAmount.getText().toString());
                dIGSTAmt += Double.parseDouble(ColIGSTAmount.getText().toString());
                dcessAmt += Double.parseDouble(ColcessAmount.getText().toString());
                dSubTotal += Double.parseDouble(ColAmount.getText().toString());
            }
        }
        // ------------------------------------------
        // Bill wise tax Calculation -------------------------------
        Cursor crsrtax = db.getTaxConfigs(1);
        if (crsrtax.moveToFirst()) {
            dTaxPercent = crsrtax.getFloat(crsrtax.getColumnIndex("TotalPercentage"));
            dTaxAmt += dSubTotal * (dTaxPercent / 100);
        }
        Cursor crsrtax1 = db.getTaxConfigs(2);
        if (crsrtax1.moveToFirst()) {
            dSerTaxPercent = crsrtax1.getFloat(crsrtax1.getColumnIndex("TotalPercentage"));
            dSerTaxAmt += dSubTotal * (dSerTaxPercent / 100);
        }
        // -------------------------------------------------

        dOtherCharges = Double.valueOf(textViewOtherCharges.getText().toString());
        //String strTax = crsrSettings.getString(crsrSettings.getColumnIndex("Tax"));
        if (crsrSettings.moveToFirst()) {
            if (crsrSettings.getString(crsrSettings.getColumnIndex("Tax")).equalsIgnoreCase("1"))  // forward tax
            {
                if (crsrSettings.getString(crsrSettings.getColumnIndex("TaxType")).equalsIgnoreCase("1")) // itemwise
                {
                    tvIGSTValue.setText(String.format("%.2f", dIGSTAmt));
                    tvTaxTotal.setText(String.format("%.2f", dTaxTotal));
                    tvServiceTaxTotal.setText(String.format("%.2f", dServiceTaxAmt));
                    tvcessValue.setText(String.format("%.2f",dcessAmt));

                    if (chk_interstate.isChecked()) // interstate
                    {
                        tvIGSTValue.setTextColor(Color.WHITE);
                        tvTaxTotal.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                        tvServiceTaxTotal.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                    } else {
                        tvIGSTValue.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                        tvTaxTotal.setTextColor(Color.WHITE);
                        tvServiceTaxTotal.setTextColor(Color.WHITE);
                    }

                    tvSubTotal.setText(String.format("%.2f", dSubTotal));
                    tvBillAmount.setText(String.format("%.2f", dSubTotal + dTaxTotal + dServiceTaxAmt + dOtherCharges+dcessAmt));
                }
                else
                {
                    tvIGSTValue.setText(String.format("%.2f", dIGSTAmt));
                    tvTaxTotal.setText(String.format("%.2f", dTaxTotal));
                    tvServiceTaxTotal.setText(String.format("%.2f", dServiceTaxAmt));
                    tvcessValue.setText(String.format("%.2f",dcessAmt));

                    if (chk_interstate.isChecked()) // interstate
                    {
                        tvIGSTValue.setTextColor(Color.WHITE);
                        tvTaxTotal.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                        tvServiceTaxTotal.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                    } else {
                        tvIGSTValue.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                        tvTaxTotal.setTextColor(Color.WHITE);
                        tvServiceTaxTotal.setTextColor(Color.WHITE);
                    }
                    tvSubTotal.setText(String.format("%.2f", dSubTotal));
                    tvBillAmount.setText(String.format("%.2f", dSubTotal + dTaxAmt + dSerTaxAmt + dOtherCharges+dcessAmt));
                }
            }
            else // reverse tax
            {
                if (crsrSettings.getString(crsrSettings.getColumnIndex("TaxType")).equalsIgnoreCase("1")) // item wise
                {
                    tvIGSTValue.setText(String.format("%.2f", dIGSTAmt));
                    tvTaxTotal.setText(String.format("%.2f", dTaxTotal));
                    tvServiceTaxTotal.setText(String.format("%.2f", dServiceTaxAmt));
                    tvcessValue.setText(String.format("%.2f",dcessAmt));

                    if (chk_interstate.isChecked()) // interstate
                    {
                        tvIGSTValue.setTextColor(Color.WHITE);
                        tvTaxTotal.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                        tvServiceTaxTotal.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                    } else {
                        tvIGSTValue.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                        tvTaxTotal.setTextColor(Color.WHITE);
                        tvServiceTaxTotal.setTextColor(Color.WHITE);
                    }
                    tvSubTotal.setText(String.format("%.2f", dSubTotal));
                    tvBillAmount.setText(String.format("%.2f", dSubTotal + dOtherCharges));

                }
                else
                {
                    tvSubTotal.setText(String.format("%.2f", dSubTotal));
                    tvIGSTValue.setText(String.format("%.2f", dIGSTAmt));
                    tvTaxTotal.setText(String.format("%.2f", dTaxTotal));
                    tvServiceTaxTotal.setText(String.format("%.2f", dServiceTaxAmt));
                    tvcessValue.setText(String.format("%.2f",dcessAmt));

                    if (chk_interstate.isChecked()) // interstate
                    {
                        tvIGSTValue.setTextColor(Color.WHITE);
                        tvTaxTotal.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                        tvServiceTaxTotal.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                    } else {
                        tvIGSTValue.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                        tvTaxTotal.setTextColor(Color.WHITE);
                        tvServiceTaxTotal.setTextColor(Color.WHITE);
                    }
                    tvBillAmount.setText(String.format("%.2f", dSubTotal + dOtherCharges));
                }
            }
        }
    }

    public void ControlsSetEnabled() {
        btn_DineInAddCustomer.setVisibility(View.VISIBLE);
       // textViewOtherCharges.setEnabled(true);
        listViewDept.setEnabled(true);
        listViewCat.setEnabled(true);
        gridViewItems.setEnabled(true);
        //btnSplitBill.setEnabled(true);
        //btnPayBill.setEnabled(true);
        //btnSaveKOT.setEnabled(true);
        //btnKOTStatus.setEnabled(true);
        if(tblOrderItems.getChildCount()>0)
        {
            btn_PayBill.setEnabled(true);
            btn_PrintBill.setEnabled(true);
        }else
        {
            btn_PayBill.setEnabled(false);
            btn_PrintBill.setEnabled(false);
        }
        tblOrderItems.setEnabled(true);
        btn_Clear.setEnabled(true);
        btn_Reprint.setEnabled(true);
        autoCompleteTextViewSearchItem.setEnabled(true);
        autoCompleteTextViewSearchMenuCode.setEnabled(true);
        btnDept.setEnabled(true);
        btnCat.setEnabled(true);
        btnItems.setEnabled(true);
        if(jBillingMode==2) {
            btn_PrintBill.setEnabled(true);
            btn_PayBill.setEnabled(true);
        }
    }

    public void ControlsSetDisabled() {
        btn_DineInAddCustomer.setVisibility(View.VISIBLE);
        //tvHSNCode_out.setEnabled(false);
        autoCompleteTextViewSearchMenuCode.setEnabled(false);
        //textViewOtherCharges.setEnabled(false);
        //btn_item_fastBillingMode.setEnabled(false);
        listViewDept.setEnabled(false);
        listViewCat.setEnabled(false);
        gridViewItems.setEnabled(false);
        //btnSplitBill.setEnabled(false);
        btn_PayBill.setEnabled(false);
        //btnSaveKOT.setEnabled(false);
        //btnKOTStatus.setEnabled(false);
        //btnPrintKOT.setEnabled(false);
        btn_PrintBill.setEnabled(false);
        tblOrderItems.setEnabled(false);
        btn_Clear.setEnabled(false);
        btn_Reprint.setEnabled(false);
        autoCompleteTextViewSearchItem.setEnabled(false);
        //spnr_pos.setEnabled(false);
        autoCompleteTextViewSearchMenuCode.setEnabled(false);
        btnDept.setEnabled(false);
        btnCat.setEnabled(false);
        btnItems.setEnabled(false);
        if(jBillingMode==2) {
            btn_PrintBill.setEnabled(true);
            btn_PayBill.setEnabled(true);
        }
    }

    /*************************************************************************************************************************************
     * Opens tender window in dine in and take away billing mode
     *************************************************************************************************************************************/
    /*private void Tender()
    {
        if (jBillingMode == 2 && Double.parseDouble(tvBillAmount.getText().toString()) <= 0)
        {
            messageDialog.Show("Warning", "Empty bill can not be tendered");
            return;
        }else if (chk_interstate.isChecked() && spnr_pos.getSelectedItem().equals("")) {
            messageDialog.Show("Warning", "Please Select Code for Intersate Supply");
        }

        if (jBillingMode == Byte.parseByte("2"))
        {
            Intent intTender = new Intent(getApplicationContext(), PayBillActivity.class);
            Log.v("Debug", "Total Amount:" + tvBillAmount.getText().toString());
            intTender.putExtra("TotalAmount", tvBillAmount.getText().toString());
            intTender.putExtra("phone", editTextMobile.getText().toString());
            intTender.putExtra("BaseValue", Float.parseFloat(tvSubTotal.getText().toString()));
            intTender.putExtra("USER_NAME", userName);
            startActivityForResult(intTender, 1);

        }
    }
*/
    public void Tender1() {

        if (tvBillAmount.getText().toString().equals("") ) {
            messageDialog.Show("Warning", "Please add item to make bill");
        } else if ( tvSubTotal.getText().toString().equals("0.00")) {
            messageDialog.Show("Warning", "Please add item of rate greater than 0.00");
        }else if (chk_interstate.isChecked() && spnr_pos.getSelectedItem().equals("")) {
            messageDialog.Show("Warning", "Please Select Code for Intersate Supply");
        }

        else
        {
            ArrayList<AddedItemsToOrderTableClass> orderItemList = new ArrayList<>();
            int taxType =0;
            for (int iRow = 0; iRow < tblOrderItems.getChildCount(); iRow++) {

                int menuCode =0;
                String itemName= "";
                double quantity=0.00;
                double rate=0.00;
                double igstRate=0.00;
                double igstAmt=0.00;
                double cgstRate=0.00;
                double cgstAmt=0.00;
                double sgstRate=0.00;
                double sgstAmt=0.00;
                double cessRate=0.00;
                double cessAmt=0.00;
                double subtotal=0.00;
                double billamount=0.00;
                double discountamount=0.00;

                TableRow RowBillItem = (TableRow) tblOrderItems.getChildAt(iRow);


                // Item Number
                if (RowBillItem.getChildAt(0) != null) {
                    CheckBox ItemNumber = (CheckBox) RowBillItem.getChildAt(0);
                    menuCode = (Integer.parseInt(ItemNumber.getText().toString()));
                }

                // Item Name
                if (RowBillItem.getChildAt(1) != null) {
                    TextView ItemName = (TextView) RowBillItem.getChildAt(1);
                    itemName = (ItemName.getText().toString());
                }



                // Quantity
                if (RowBillItem.getChildAt(3) != null) {
                    EditText Quantity = (EditText) RowBillItem.getChildAt(3);
                    String qty_str = Quantity.getText().toString();
                    double qty_d = 0.00;
                    if(qty_str==null || qty_str.equals(""))
                    {
                        Quantity.setText("0.00");
                    }else
                    {
                        qty_d = Double.parseDouble(qty_str);
                    }
                    quantity = (Double.parseDouble(String.format("%.2f",qty_d)));

                }

                // Rate
                if (RowBillItem.getChildAt(4) != null) {
                    EditText Rate = (EditText) RowBillItem.getChildAt(4);
                    String rate_str = Rate.getText().toString();
                    double rate_d = 0.00;
                    if((rate_str==null || rate_str.equals("")))
                    {
                        Rate.setText("0.00");
                    }else
                    {
                        rate_d = Double.parseDouble(rate_str);
                    }
                    rate = (Double.parseDouble(String.format("%.2f",rate_d)));

                }


                // Service Tax Percent

                if(chk_interstate.isChecked()) // IGST
                {
                    cgstRate =0;
                    cgstAmt =0;
                    sgstRate =0;
                    sgstAmt =0;
                    if (RowBillItem.getChildAt(23) != null) {
                        TextView iRate = (TextView) RowBillItem.getChildAt(23);
                        igstRate = (Double.parseDouble(String.format("%.2f",
                                Double.parseDouble(iRate.getText().toString()))));
                    }

                    // Service Tax Amount
                    if (RowBillItem.getChildAt(24) != null) {
                        TextView iAmt = (TextView) RowBillItem.getChildAt(24);
                        igstAmt =  Double.parseDouble(String.format("%.2f",
                                Double.parseDouble(iAmt.getText().toString())));
                    }


                }else // CGST+SGST
                {
                    igstRate =0;
                    igstAmt =0;
                    if (RowBillItem.getChildAt(15) != null) {
                        TextView ServiceTaxPercent = (TextView) RowBillItem.getChildAt(15);
                        sgstRate = (Double.parseDouble(String.format("%.2f",
                                Double.parseDouble(ServiceTaxPercent.getText().toString()))));
                    }

                    // Service Tax Amount
                    if (RowBillItem.getChildAt(16) != null) {
                        TextView ServiceTaxAmount = (TextView) RowBillItem.getChildAt(16);
                        sgstAmt =  Double.parseDouble(String.format("%.2f",
                                Double.parseDouble(ServiceTaxAmount.getText().toString())));
                    }

                    // Sales Tax %
                    if (RowBillItem.getChildAt(6) != null) {
                        TextView SalesTaxPercent = (TextView) RowBillItem.getChildAt(6);
                        cgstRate = Double.parseDouble(String.format("%.2f",
                                Double.parseDouble(SalesTaxPercent.getText().toString())));
                    }
                    // Sales Tax Amount
                    if (RowBillItem.getChildAt(7) != null) {
                        TextView SalesTaxAmount = (TextView) RowBillItem.getChildAt(7);
                        cgstAmt = Double.parseDouble(String.format("%.2f",
                                Double.parseDouble(SalesTaxAmount.getText().toString())));
                    }
                }
                if (RowBillItem.getChildAt(25) != null) {
                    TextView cessRt = (TextView)RowBillItem.getChildAt(25);
                    if(!cessRt.getText().toString().equals(""))
                        cessRate = Double.parseDouble(cessRt.getText().toString());
                }
                if (RowBillItem.getChildAt(26) != null) {
                    TextView cessAt = (TextView)RowBillItem.getChildAt(26);
                    if(!cessAt.getText().toString().equals(""))
                        cessAmt = Double.parseDouble(cessAt.getText().toString());
                }



                // Tax Type
                if (RowBillItem.getChildAt(13) != null) {
                    TextView TaxType = (TextView) RowBillItem.getChildAt(13);
                    taxType = (Integer.parseInt(TaxType.getText().toString()));
                }
                // subtotal
                subtotal = (rate*quantity) + igstAmt+cgstAmt+sgstAmt;

                AddedItemsToOrderTableClass orderItem = new AddedItemsToOrderTableClass( menuCode, itemName, quantity, rate,
                        igstRate,igstAmt, cgstRate, cgstAmt, sgstRate,sgstAmt, rate*quantity,subtotal, billamount,cessRate,cessAmt,discountamount);
                orderItemList.add(orderItem);
            }



            Intent intentTender = new Intent(getApplicationContext(), PayBillActivity.class);
            intentTender.putExtra("TotalAmount", tvBillAmount.getText().toString());
            intentTender.putExtra("CustId", customerId);
            intentTender.putExtra("phone", editTextMobile.getText().toString());
            intentTender.putExtra("BaseValue", Float.parseFloat(tvSubTotal.getText().toString()));
            intentTender.putExtra("OtherCharges", Double.parseDouble(textViewOtherCharges.getText().toString()));
            intentTender.putExtra("TaxType", taxType);// forward/reverse
            intentTender.putParcelableArrayListExtra("OrderList", orderItemList);
            intentTender.putExtra("USER_NAME", userName);
            startActivityForResult(intentTender, 1);
        }
    }


    /*************************************************************************************************************************************
     * Loads KOT order items to billing table
     *
     * @param crsrBillItems : Cursor with KOT order item details
     *************************************************************************************************************************************/
    private void LoadKOTItems(Cursor crsrBillItems) {
        EditTextInputHandler etInputValidate = new EditTextInputHandler();
        TableRow rowItem;
        TextView tvName, tvHSn, tvAmount, tvTaxPercent, tvTaxAmt, tvDiscPercent, tvDiscAmt, tvDeptCode, tvCategCode, tvKitchenCode, tvTaxType, tvModifierCharge, tvServiceTaxPercent, tvServiceTaxAmt;
        EditText etQty, etRate;
        CheckBox Number;
        ImageButton ImgDelete;
        if (crsrBillItems.moveToFirst())
        {
            /*iTokenNumber = crsrBillItems.getInt(crsrBillItems.getColumnIndex("TokenNumber"));
            tvWaiterNumber.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("EmployeeId")));
            // Get Table number
            tvTableNumber.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("TableNumber")));
            // Get Table Split No
            tvTableSplitNo.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("TableSplitNo")));
            // Get Sub Udf number
            tvSubUdfValue.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("SubUdfNumber")));*/
            // Get Cust Id
            customerId = crsrBillItems.getString(crsrBillItems.getColumnIndex("CustId"));
            Cursor crsrCustomer = db.getCustomerById(crsrBillItems.getInt(crsrBillItems.getColumnIndex("CustId")));
            if (crsrCustomer.moveToFirst())
            {
                editTextMobile.setText(crsrCustomer.getString(crsrCustomer.getColumnIndex("CustContactNumber")));
                editTextName.setText(crsrCustomer.getString(crsrCustomer.getColumnIndex("CustName")));
                editTextAddress.setText(crsrCustomer.getString(crsrCustomer.getColumnIndex("CustAddress")));
            }

            // Display items in table
            do {
                rowItem = new TableRow(BillingCounterSalesActivity.this);
                rowItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                // Item Number
                Number = new CheckBox(BillingCounterSalesActivity.this);
                Number.setWidth(40);
                Number.setTextSize(0);
                Number.setTextColor(Color.TRANSPARENT);
                Number.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ItemNumber")));

                // Item Name
                tvName = new TextView(BillingCounterSalesActivity.this);
                tvName.setWidth(135);
                tvName.setTextSize(11);
                tvName.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ItemName")));

                //hsn code
                tvHSn = new TextView(BillingCounterSalesActivity.this);
                tvHSn.setWidth(67); // 154px ~= 230dp
                tvHSn.setTextSize(11);
                if (GSTEnable.equalsIgnoreCase("1") && (HSNEnable_out != null) && HSNEnable_out.equals("1")) {
                    tvHSn.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("HSNCode")));
                }
                // Quantity
                etQty = new EditText(BillingCounterSalesActivity.this);
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
                etRate = new EditText(BillingCounterSalesActivity.this);
                etRate.setWidth(70);
                etRate.setEnabled(false);
                etRate.setTextSize(11);
                etRate.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                etRate.setText(String.format("%.2f", crsrBillItems.getDouble(crsrBillItems.getColumnIndex("Rate"))));

                // Amount
                tvAmount = new TextView(BillingCounterSalesActivity.this);
                tvAmount.setWidth(60);
                tvAmount.setTextSize(11);
                tvAmount.setGravity(Gravity.RIGHT | Gravity.END);
                tvAmount.setText(
                        String.format("%.2f", crsrBillItems.getDouble(crsrBillItems.getColumnIndex("Amount"))));

                // Sales Tax%
                tvTaxPercent = new TextView(BillingCounterSalesActivity.this);
                tvTaxPercent.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("TaxPercent")));

                // Sales Tax Amount
                tvTaxAmt = new TextView(BillingCounterSalesActivity.this);
                tvTaxAmt.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("TaxAmount")));

                // Discount %
                tvDiscPercent = new TextView(BillingCounterSalesActivity.this);
                tvDiscPercent.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("DiscountPercent")));

                // Discount Amount
                tvDiscAmt = new TextView(BillingCounterSalesActivity.this);
                tvDiscAmt.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("DiscountAmount")));

                // Dept Code
                tvDeptCode = new TextView(BillingCounterSalesActivity.this);
                tvDeptCode.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("DeptCode")));

                // Categ Code
                tvCategCode = new TextView(BillingCounterSalesActivity.this);
                tvCategCode.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("CategCode")));

                // Kitchen Code
                tvKitchenCode = new TextView(BillingCounterSalesActivity.this);
                tvKitchenCode.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("KitchenCode")));

                // Tax Type
                tvTaxType = new TextView(BillingCounterSalesActivity.this);
                tvTaxType.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("TaxType")));

                // Modifier Amount
                tvModifierCharge = new TextView(BillingCounterSalesActivity.this);
                tvModifierCharge.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ModifierAmount")));

                // Service Tax %
                tvServiceTaxPercent = new TextView(BillingCounterSalesActivity.this);
                tvServiceTaxPercent.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ServiceTaxPercent")));

                // Service Tax Amount
                tvServiceTaxAmt = new TextView(BillingCounterSalesActivity.this);
                tvServiceTaxAmt.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ServiceTaxAmount")));

                // Service Tax Amount
                TextView tvSupplyType = new TextView(BillingCounterSalesActivity.this);
                tvSupplyType.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("SupplyType")));


                // Delete
                int res = getResources().getIdentifier("delete", "drawable", this.getPackageName());
                ImgDelete = new ImageButton(BillingCounterSalesActivity.this);
                ImgDelete.setImageResource(res);
                ImgDelete.setVisibility(View.INVISIBLE);



                TextView tvSpace = new TextView(BillingCounterSalesActivity.this);
                tvSpace.setText("        ");

                TextView tvSpace1 = new TextView(BillingCounterSalesActivity.this);
                tvSpace1.setText("       ");

                TextView tvPrintKOTStatus = new TextView(BillingCounterSalesActivity.this);
                /*if(REPRINT_KOT == 1)
                    tvPrintKOTStatus.setText("1");
                else
                    tvPrintKOTStatus.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("PrintKOTStatus")));*/



                // Add all text views and edit text to Item Row
                // rowItem.addView(tvNumber);
                rowItem.addView(Number);//0
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
                rowItem.addView(tvSupplyType);//17
                rowItem.addView(tvSpace);//
                rowItem.addView(ImgDelete);
                rowItem.addView(tvSpace1);
                rowItem.addView(tvPrintKOTStatus);

                // Add row to table
                tblOrderItems.addView(rowItem, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            } while (crsrBillItems.moveToNext());

            //REPRINT_KOT =0;

            CalculateTotalAmount();
            Log.d("LoadKOTItems", "Items loaded successfully");
        } else {
            Log.d("LoadKOTItems", "No rows in cursor");
        }
    }

    // -----Print Bill Code Started-----

    public void printBILL()
    {
        int proceed =1;
        if (tblOrderItems.getChildCount() < 1)
        {
            messageDialog.Show("Warning", "Add Item before Printing Bill");
            proceed =0;
        }else if (tvBillAmount.getText().toString().equals("") ) {
            messageDialog.Show("Warning", "Please add item to make bill");
            proceed =0;
        } else if ( tvBillAmount.getText().toString().equals("0.00")) {
            messageDialog.Show("Warning", "Please make bill of amount greater than 0.00");
            proceed =0;
        }else if (chk_interstate.isChecked() && spnr_pos.getSelectedItem().equals("")) {
            messageDialog.Show("Warning", "Please Select Code for Intersate Supply");
            proceed =0;
        }

        if(proceed == 0)
            return;
        if (isPrinterAvailable)
        {
            strPaymentStatus = "Paid";
            PrintBillPayment = 1;
            // Print Bill with Save Bill
            if (tblOrderItems.getChildCount() < 1)
            {
                messageDialog.Show("Warning", "Insert item before Print Bill");
                return;
            }
            else
            {
                l(2, true);
                PrintNewBill();
                Toast.makeText(BillingCounterSalesActivity.this, "Bill Saved Successfully", Toast.LENGTH_SHORT).show();
                if (jBillingMode == 2)
                {
                    ClearAll();
                    btn_PrintBill.setEnabled(true);
                }
            }
        }
        else
        {
            Toast.makeText(BillingCounterSalesActivity.this, "Printer is not ready", Toast.LENGTH_SHORT).show();
            askForConfig();
        }
    }

    private void l(int TenderType, boolean isPrintBill) { // TenderType:
        InsertBillItems();
        InsertBillDetail(TenderType);
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

                crsrUpdateItemStock = db.getItemss(Integer.parseInt(ItemNumber.getText().toString()));
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
                String qty_str = Quantity.getText().toString();
                double qty_d = 0.00;
                if(qty_str==null || qty_str.equals(""))
                {
                    Quantity.setText("0.00");
                }else
                {
                    qty_d = Double.parseDouble(qty_str);
                }

                objBillItem.setQuantity(Float.parseFloat(String.format("%.2f",qty_d)));
                Log.d("InsertBillItems", "Quantity:" + Quantity.getText().toString());

                if (crsrUpdateItemStock!=null && crsrUpdateItemStock.moveToFirst()) {
                    // Check if item's bill with stock enabled update the stock
                    // quantity
                    if (BillwithStock == 1) {
                        UpdateItemStock(crsrUpdateItemStock, Float.parseFloat(Quantity.getText().toString()));
                    }


                }
            }

            // Rate
            if (RowBillItem.getChildAt(4) != null) {
                EditText Rate = (EditText) RowBillItem.getChildAt(4);
                String rate_str = Rate.getText().toString();
                double rate_d = 0.00;
                if((rate_str==null || rate_str.equals("")))
                {
                    Rate.setText("0.00");
                }else
                {
                    rate_d = Double.parseDouble(rate_str);
                }

                objBillItem.setValue(Float.parseFloat(String.format("%.2f",rate_d)));
                Log.d("InsertBillItems", "Rate:" + Rate.getText().toString());
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
            // Amount
            if (RowBillItem.getChildAt(5) != null) {
                TextView Amount = (TextView) RowBillItem.getChildAt(5);
                objBillItem.setAmount(Float.parseFloat(Amount.getText().toString()));
                Log.d("InsertBillItems", "Taxable Value:" + Amount.getText().toString());
            }




            // Service Tax Percent
            float sgatTax = 0;
            if (RowBillItem.getChildAt(15) != null) {
                TextView ServiceTaxPercent = (TextView) RowBillItem.getChildAt(15);
                sgatTax = Float.parseFloat(ServiceTaxPercent.getText().toString());
                if (chk_interstate.isChecked()) {
                    objBillItem.setSGSTRate(0);
                    Log.d("InsertBillItems", "SGST Tax %: 0");

                } else {
                    objBillItem.setSGSTRate(Float.parseFloat(ServiceTaxPercent.getText().toString()));
                    Log.d("InsertBillItems", "SGST Tax %: " + objBillItem.getSGSTRate());
                }
            }

            // Service Tax Amount
            float sgstAmt = 0;
            if (RowBillItem.getChildAt(16) != null) {
                TextView ServiceTaxAmount = (TextView) RowBillItem.getChildAt(16);
                sgstAmt = Float.parseFloat(ServiceTaxAmount.getText().toString());
                if (chk_interstate.isChecked()) {
                    objBillItem.setSGSTAmount(0.00f);
                    Log.d("InsertBillItems", "SGST Amount : 0" );

                } else {
                    objBillItem.setSGSTAmount(Float.parseFloat(String.format("%.2f",
                            Float.parseFloat(ServiceTaxAmount.getText().toString()))));
                    Log.d("InsertBillItems", "SGST Amount : " + objBillItem.getSGSTAmount());
                }
            }

            // Sales Tax %
            if (RowBillItem.getChildAt(6) != null) {
                TextView SalesTaxPercent = (TextView) RowBillItem.getChildAt(6);
                float cgsttax = (Float.parseFloat(SalesTaxPercent.getText().toString()));
                if (chk_interstate.isChecked()) {
                    //objBillItem.setIGSTRate(Float.parseFloat(String.format("%.2f", cgsttax + sgatTax)));
                    //Log.d("InsertBillItems", " IGST Tax %: " + objBillItem.getIGSTRate());
                    objBillItem.setCGSTRate(0.00f);
                    Log.d("InsertBillItems", " CGST Tax %: 0.00");
                }else{
                    //objBillItem.setIGSTRate(0.00f);
                    //Log.d("InsertBillItems", " IGST Tax %: 0.00");
                    objBillItem.setCGSTRate(Float.parseFloat(String.format("%.2f",Float.parseFloat(SalesTaxPercent.getText().toString()))));
                    Log.d("InsertBillItems", " CGST Tax %: " + SalesTaxPercent.getText().toString());
                }
            }
            // Sales Tax Amount
            if (RowBillItem.getChildAt(7) != null) {
                TextView SalesTaxAmount = (TextView) RowBillItem.getChildAt(7);
                float cgstAmt = (Float.parseFloat(SalesTaxAmount.getText().toString()));
                if (chk_interstate.isChecked()) {
                    //objBillItem.setIGSTAmount(Float.parseFloat(String.format("%.2f",cgstAmt+sgstAmt)));
                    //Log.d("InsertBillItems", "IGST Amt: " + objBillItem.getIGSTAmount());
                    objBillItem.setCGSTAmount(0.00f);
                    Log.d("InsertBillItems", "CGST Amt: 0");
                } else {
                    //objBillItem.setIGSTAmount(0.00f);
                    //Log.d("InsertBillItems", "IGST Amt: 0");
                    objBillItem.setCGSTAmount(Float.parseFloat(String.format("%.2f",
                            Float.parseFloat(SalesTaxAmount.getText().toString()))));
                    Log.d("InsertBillItems", "CGST Amt: " + SalesTaxAmount.getText().toString());
                }
            }

            // IGST Tax %
            if (RowBillItem.getChildAt(23) != null) {
                TextView IGSTTaxPercent = (TextView) RowBillItem.getChildAt(23);
                float igsttax = (Float.parseFloat(IGSTTaxPercent.getText().toString()));
                if (chk_interstate.isChecked()) {
                    objBillItem.setIGSTRate(Float.parseFloat(String.format("%.2f", igsttax)));
                    Log.d("InsertBillItems", " IGST Tax %: " + objBillItem.getIGSTRate());
                }else{
                    objBillItem.setIGSTRate(0.00f);
                    Log.d("InsertBillItems", " IGST Tax %: 0.00");
                }
            }
            // IGST Tax Amount
            if (RowBillItem.getChildAt(24) != null) {
                TextView IGSTTaxAmount = (TextView) RowBillItem.getChildAt(24);
                float igstAmt = (Float.parseFloat(IGSTTaxAmount.getText().toString()));
                if (chk_interstate.isChecked()) {
                    objBillItem.setIGSTAmount(Float.parseFloat(String.format("%.2f",igstAmt)));
                    Log.d("InsertBillItems", "IGST Amt: " + objBillItem.getIGSTAmount());
                } else {
                    objBillItem.setIGSTAmount(0.00f);
                    Log.d("InsertBillItems", "IGST Amt: 0");
                }
            }

            // cess Tax %
            if (RowBillItem.getChildAt(25) != null) {
                TextView cessTaxPercent = (TextView) RowBillItem.getChildAt(25);
                float cesstax = (Float.parseFloat(cessTaxPercent.getText().toString()));
                objBillItem.setCessRate(Float.parseFloat(String.format("%.2f", cesstax)));
                Log.d("InsertBillItems", " cess Tax %: " + objBillItem.getCessRate());
            }
            // cessTax Amount
            if (RowBillItem.getChildAt(26) != null) {
                TextView cessTaxAmount = (TextView) RowBillItem.getChildAt(26);
                float cessAmt = (Float.parseFloat(cessTaxAmount.getText().toString()));
                objBillItem.setCessAmount(Float.parseFloat(String.format("%.2f",cessAmt)));
                Log.d("InsertBillItems", "cess Amt: " + objBillItem.getCessAmount());
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



            if (RowBillItem.getChildAt(17) != null) {
                TextView SupplyType = (TextView) RowBillItem.getChildAt(17);
                objBillItem.setSupplyType(SupplyType.getText().toString());
                Log.d("InsertBillItems", "SupplyType:" + SupplyType.getText().toString());

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
            String date_today = tvDate.getText().toString();
            //Log.d("Date ", date_today);
            try {
                Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(date_today);
                objBillItem.setInvoiceDate(String.valueOf(date1.getTime()));
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            // cust name
            String custname = editTextName.getText().toString();
            objBillItem.setCustName(custname);
            Log.d("InsertBillItems", "CustName :" + custname);

            String custGstin = etCustGSTIN.getText().toString();
            objBillItem.setGSTIN(custGstin);
            Log.d("InsertBillItems", "custGstin :" + custGstin);

            // cust StateCode
            if (chk_interstate.isChecked()) {
                String str = spnr_pos.getSelectedItem().toString();
                int length = str.length();
                String sub = "";
                if (length > 0) {
                    sub = str.substring(length - 2, length);
                }
                objBillItem.setCustStateCode(sub);
                Log.d("InsertBillItems", "CustStateCode :" + sub+" - "+str);
            } else {
                objBillItem.setCustStateCode(db.getOwnerPOS_counter());// to be retrieved from database later -- richa to do
                Log.d("InsertBillItems", "CustStateCode :"+objBillItem.getCustStateCode());
            }

            // BusinessType
            if (etCustGSTIN.getText().toString().equals("")) {
                objBillItem.setBusinessType("B2C");
            } else // gstin present means b2b bussiness
            {
                objBillItem.setBusinessType("B2B");
            }
            Log.d("InsertBillItems", "BusinessType : " + objBillItem.getBusinessType());
            objBillItem.setBillStatus(1);
            Log.d("InsertBillItems", "Bill Status:1");
            // richa to do - hardcoded b2b bussinies type
            //objBillItem.setBusinessType("B2B");
            lResult = db.addBillItems(objBillItem);
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
        try {
            String date_today = tvDate.getText().toString();
            //Log.d("Date ", date_today);
            Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(date_today);
            objBillDetail.setDate(String.valueOf(date1.getTime()));
            Log.d("InsertBillDetail", "Date:" + objBillDetail.getDate());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

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


        // custStateCode
        if (chk_interstate.isChecked()) {
            String str = spnr_pos.getSelectedItem().toString();
            int length = str.length();
            String sub = "";
            if (length > 0) {
                sub = str.substring(length - 2, length);
            }
            objBillDetail.setCustStateCode(sub);
            Log.d("InsertBillDetail", "CustStateCode :" + sub+" - "+str);
        } else {
            String userPOS = db.getOwnerPOS_counter();
            objBillDetail.setCustStateCode(userPOS);
            Log.d("InsertBillDetail", "CustStateCode : "+objBillDetail.getCustStateCode());
        }


        objBillDetail.setPOS(db.getOwnerPOS_counter());// to be retrieved from database later -- richa to do
        Log.d("InsertBillDetail", "POS : "+objBillDetail.getPOS());




        // Total Items
        objBillDetail.setTotalItems(iTotalItems);
        Log.d("InsertBillDetail", "Total Items:" + iTotalItems);

        // Bill Amount
        String billamt_temp = String.format("%.2f",Float.parseFloat(tvBillAmount.getText().toString()));
        objBillDetail.setBillAmount(Float.parseFloat(billamt_temp));
        Log.d("InsertBillDetail", "Bill Amount:" + tvBillAmount.getText().toString());

        // Discount Percentage
        objBillDetail.setTotalDiscountPercentage(Float.parseFloat(tvDiscountPercentage.getText().toString()));
        Log.d("InsertBillDetail", "Discount Percentage:" + objBillDetail.getTotalDiscountPercentage());

        // Discount Amount
        if(ItemwiseDiscountEnabled ==1)
            calculateDiscountAmount();
        objBillDetail.setTotalDiscountAmount(fTotalDiscount);
        Log.d("InsertBillDetail", "Total Discount:" + fTotalDiscount);

        // Sales Tax Amount
        if (chk_interstate.isChecked()) {
            objBillDetail.setIGSTAmount(Float.parseFloat(String.format("%.2f",Float.parseFloat(tvIGSTValue.getText().toString()))));
            objBillDetail.setCGSTAmount(0.00f);
            objBillDetail.setSGSTAmount(0.00f);
        } else {
            objBillDetail.setIGSTAmount(0.00f);
            objBillDetail.setCGSTAmount(Float.parseFloat(String.format("%.2f",Float.parseFloat(tvTaxTotal.getText().toString()))));
            objBillDetail.setSGSTAmount(Float.parseFloat(String.format("%.2f",Float.parseFloat(tvServiceTaxTotal.getText().toString()))));
        }
        Log.d("InsertBillDetail", "IGSTAmount : " + objBillDetail.getIGSTAmount());
        Log.d("InsertBillDetail", "CGSTAmount : " + objBillDetail.getCGSTAmount());
        Log.d("InsertBillDetail", "SGSTAmount : " + objBillDetail.getSGSTAmount());

        objBillDetail.setCessAmount(Float.parseFloat(String.format("%.2f",Float.parseFloat(tvcessValue.getText().toString()))));
        Log.d("InsertBillDetail", "cessAmount : " + objBillDetail.getCessAmount());
        // Delivery Charge
        objBillDetail.setDeliveryCharge(Float.parseFloat(textViewOtherCharges.getText().toString()));
        Log.d("InsertBillDetail", "Delivery Charge: "+objBillDetail.getDeliveryCharge());


        // Taxable Value
        float taxval_f = Float.parseFloat(tvSubTotal.getText().toString());
        objBillDetail.setAmount(String.valueOf(taxval_f));
        Log.d("InsertBillDetail", "Taxable Value:" + taxval_f);

        /*float cgstamt_f = 0, sgstamt_f = 0;
        if (tvTaxTotal.getText().toString().equals("") == false) {
            cgstamt_f = Float.parseFloat(tvTaxTotal.getText().toString());
        }
        if (tvServiceTaxTotal.getText().toString().equals("") == false) {
            sgstamt_f = Float.parseFloat(tvServiceTaxTotal.getText().toString());
        }*/


        float subtot_f = taxval_f + objBillDetail.getIGSTAmount() + objBillDetail.getCGSTAmount()+ objBillDetail.getSGSTAmount();
        objBillDetail.setSubTotal(subtot_f);
        Log.d("InsertBillDetail", "Sub Total :" + subtot_f);

        // cust name
        String custname = editTextName.getText().toString();
        objBillDetail.setCustname(custname);
        Log.d("InsertBillDetail", "CustName :" + custname);

        String custGSTIN = etCustGSTIN.getText().toString();
        objBillDetail.setGSTIN(custGSTIN);
        Log.d("InsertBillDetail", "custGSTIN :" + custGSTIN);

        /*// cust StateCode
        if (chk_interstate.isChecked()) {
            String str = spnr_pos.getSelectedItem().toString();
            int length = str.length();
            String sub = "";
            if (length > 0) {
                sub = str.substring(length - 2, length);
            }
            objBillDetail.setCustStateCode(sub);
            Log.d("InsertBillDetail", "CustStateCode :" + sub+" - "+str);
        } else {
            objBillDetail.setCustStateCode("29");// to be retrieved from database later -- richa to do
            Log.d("InsertBillDetail", "CustStateCode :"+objBillDetail.getCustStateCode());
        }*/
        /*String str = spnr_pos.getSelectedItem().toString();
        int length = str.length();
        String custStateCode = "";
        if (length > 0) {
            custStateCode = str.substring(length - 2, length);
        }*/
        /*objBillDetail.setCustStateCode(custStateCode);
        Log.d("InsertBillDetail", "CustStateCode :" + custStateCode);*/


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

        objBillDetail.setBillStatus(1);
        Log.d("InsertBillDetail", "Bill Status:1");


        // Employee Id (Waiter / Rider)
        if (jBillingMode == 1 ) {
            objBillDetail.setEmployeeId(Integer.parseInt(tvWaiterNumber.getText().toString()));
            Log.d("InsertBillDetail", "EmployeeId:" + tvWaiterNumber.getText().toString());
        } else {
            objBillDetail.setEmployeeId(0);
            Log.d("InsertBillDetail", "EmployeeId:0");
        }

        // Customer Id
        objBillDetail.setCustId(Integer.valueOf(customerId));
        Log.d("InsertBillDetail", "Customer Id:" + customerId);

        // User Id
        objBillDetail.setUserId(userId);
        Log.d("InsertBillDetail", "UserID:" + userId);

        lResult = db.addBilll(objBillDetail, objBillDetail.getGSTIN());
        Log.d("InsertBill", "Bill inserted at position:" + lResult);
        //lResult = dbBillScreen.updateBill(objBillDetail);

        if (String.valueOf(customerId).equalsIgnoreCase("") || String.valueOf(customerId).equalsIgnoreCase("0"))
        {
            // No customer Details, do nothing
        }
        else
        {
            iCustId = Integer.valueOf(customerId);
            float fTotalTransaction = db.getCustomerTotalTransaction(iCustId);
            float fCreditAmount = db.getCustomerCreditAmount(iCustId);
            //fCreditAmount = fCreditAmount - Float.parseFloat(tvBillAmount.getText().toString());
            fCreditAmount = fCreditAmount - fPettCashPayment;
            fTotalTransaction += Float.parseFloat(tvBillAmount.getText().toString());

            long lResult1 = db.updateCustomerTransaction(iCustId, Float.parseFloat(tvBillAmount.getText().toString()), fTotalTransaction, fCreditAmount);
        }

        // Bill No Reset Configuration
        long Result2 = db.UpdateBillNoResetInvoiceNos(Integer.parseInt(tvBillNumber.getText().toString()));
    }

    void calculateDiscountAmount()
    {
        fTotalDiscount =0;
        for(int i=0;i<tblOrderItems.getChildCount();i++)
        {
            TableRow row = (TableRow)tblOrderItems.getChildAt(i);
            TextView discountAmt = (TextView) row.getChildAt(9);
            if(discountAmt.getText().toString()!= null && !discountAmt.getText().toString().equals("") )
                fTotalDiscount += Double.parseDouble(discountAmt.getText().toString());
        }
    }
    protected void PrintNewBill() {
        if (isPrinterAvailable) {
            if (tblOrderItems.getChildCount() < 1)
            {
                messageDialog.Show("Warning", "Insert item before Print Bill");
                return;
            }
            else
            {
                int  waiterId = 0, orderId = 0;
                if ((!tvBillNumber.getText().toString().trim().equalsIgnoreCase("")))
                {
                    String tableId = "0";
                    waiterId = 0;
                    orderId = Integer.parseInt(tvBillNumber.getText().toString().trim());
                    ArrayList<BillTaxItem> billTaxItems ;
                    ArrayList<BillServiceTaxItem> billServiceTaxItems = new ArrayList<BillServiceTaxItem>();
                    ArrayList<BillTaxItem> billOtherChargesItems = otherChargesPrint();
                    ArrayList<BillServiceTaxItem> billcessTaxItems = new ArrayList<BillServiceTaxItem>();
                    ArrayList<BillTaxSlab> billTaxSlabs = new ArrayList<BillTaxSlab>();
                    ArrayList<BillKotItem> billKotItems = new ArrayList<>();

                    PrintKotBillItem item = new PrintKotBillItem();

                    Cursor crsrCustomer = db.getCustomerById(Integer.parseInt(customerId));
                    if (crsrCustomer.moveToFirst()) {
                        item.setCustomerName(crsrCustomer.getString(crsrCustomer.getColumnIndex("CustName")));
                    } else {
                        item.setCustomerName(" - - - ");
                    }
                    if(reprintBillingMode>0) {
                        item.setIsDuplicate("\n(Duplicate Bill)");
                    }else{
                        item.setIsDuplicate("");
                    }
                    if(chk_interstate.isChecked())
                    {
                        item.setIsInterState("y");
                        billTaxSlabs = TaxSlabPrint_InterState();
                    }
                    else
                    {
                        item.setIsInterState("n");
                        billTaxSlabs = TaxSlabPrint_IntraState();
                    }

                    billcessTaxItems = cessTaxPrint();
                    billKotItems = billPrint(billTaxSlabs);
                    item.setBillKotItems(billKotItems);
                    item.setBillOtherChargesItems(billOtherChargesItems);
                    item.setBillTaxSlabs(billTaxSlabs);
                    item.setBillcessTaxItems(billcessTaxItems);
                    item.setSubTotal(Double.parseDouble(tvSubTotal.getText().toString().trim()));
                    item.setNetTotal(Double.parseDouble(tvBillAmount.getText().toString().trim()));
                    item.setTableNo(tableId);
                    item.setWaiterNo(waiterId);
                    String billNoPrefix  = db.getBillNoPrefix();
                    item.setBillNo(billNoPrefix+String.valueOf(orderId));
                    item.setOrderBy(userName);
                    item.setBillingMode(String.valueOf(jBillingMode));
                    if (strPaymentStatus.equalsIgnoreCase("")) {
                        item.setPaymentStatus("");
                    } else {
                        item.setPaymentStatus(strPaymentStatus);
                    }
                    item.setdiscountPercentage(Float.parseFloat(tvDiscountPercentage.getText().toString()));
                    if(ItemwiseDiscountEnabled ==1)
                        calculateDiscountAmount();
                    item.setFdiscount(fTotalDiscount);
                    Log.d("Discount :",String.valueOf(fTotalDiscount));
                    item.setTotalsubTaxPercent(fTotalsubTaxPercent);
                    item.setTotalSalesTaxAmount(tvTaxTotal.getText().toString());
                    item.setTotalServiceTaxAmount(tvServiceTaxTotal.getText().toString());

                    if(reprintBillingMode == 0) {
                        item.setStrBillingModeName(CounterSalesCaption);
                        item.setDate(tvDate.getText().toString());
                        item.setTime(String.format("%tR", Time));

                    }else
                    {
                        switch (reprintBillingMode)
                        {
                            case 1 : item.setStrBillingModeName(DineInCaption);
                                item.setBillingMode("1");
                                //item.setPaymentStatus(""); // payment status not required for dinein Mode
                                break;
                            case 2 : item.setStrBillingModeName(CounterSalesCaption);
                                item.setBillingMode("2");
                                //item.setPaymentStatus(""); // payment status not required for CounterSales Mode
                                break;
                            case 3 : item.setStrBillingModeName(TakeAwayCaption);
                                item.setBillingMode("3");
                                break;
                            case 4 : item.setStrBillingModeName(HomeDeliveryCaption);
                                item.setBillingMode("4");
                                if(crsrCustomer!=null){
                                    String CustDetail = crsrCustomer.getString(crsrCustomer.getColumnIndex("CustName"));
                                    CustDetail = CustDetail +"\n"+crsrCustomer.getString(crsrCustomer.getColumnIndex("CustAddress"));
                                    item.setCustomerName(CustDetail);
                                }
                                break;
                        }
                        try{
                            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(tvDate.getText().toString());
                            Cursor c  = db.getBillDetail_counter(orderId,String.valueOf(date.getTime()));
                            if(c!=null && c.moveToNext()){

                                String time = c.getString(c.getColumnIndex("Time"));
                                item.setTime(time);
                                item.setDate(tvDate.getText().toString());

                                if(reprintBillingMode==1)
                                {
                                    String tableNo = c.getString(c.getColumnIndex("TableNo"));
                                    String splitno = c.getString(c.getColumnIndex("TableSplitNo"));
                                    if(splitno!=null && !splitno.equals(""))
                                        item.setTableNo(tableNo+" - "+splitno);
                                    else
                                        item.setTableNo(tableNo);
                                }
                                String userId = c.getString(c.getColumnIndex("UserId"));
                                if(reprintBillingMode != 0 && userId!=null)
                                {
                                    Cursor user_cursor = db.getUsers_counter(userId);
                                    if(user_cursor!=null && user_cursor.moveToFirst())
                                    {
                                        item.setOrderBy(user_cursor.getString(user_cursor.getColumnIndex("Name")));
                                    }
                                }

                            }}catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
//                    String billingmode= "";
//                    billingmode = CounterSalesCaption;
//                    /*switch (jBillingMode)
//                    {
//                        case 1 : billingmode = DineInCaption;
//                            break;
//                        case 2 :
//                            break;
//                        case 3 : billingmode = TakeAwayCaption;
//                            break;
//                        case 4 : billingmode = HomeDeliveryCaption;
//                            break;
//                    }*/
//                    billingmode = CounterSalesCaption;
//                    item.setStrBillingModeName(billingmode);
                    String prf = Preferences.getSharedPreferencesForPrint(BillingCounterSalesActivity.this).getString("bill", "--Select--");
                /*Intent intent = new Intent(getApplicationContext(), PrinterSohamsaActivity.class);*/
                    Intent intent = null;
                    if (prf.equalsIgnoreCase("Sohamsa")) {
                        String[] tokens = new String[3];
                        tokens[0] = "";
                        tokens[1] = "";
                        tokens[2] = "";

                        /*if (crsrHeaderFooterSetting.moveToFirst()) {
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
                                {  if(reprintBillingMode>0) {
                                    item.setAddressLine3(tokens[2]+"\n(Duplicate Bill)");
                                }else{
                                    item.setAddressLine3(tokens[2]);}
                            }
                            item.setFooterLine(crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("FooterText")));
                        } else {
                            Log.d(TAG, "DisplayHeaderFooterSettings No data in BillSettings table");
                        }*/

                        //printSohamsaBILL(item, "BILL");
                    } else if (prf.equalsIgnoreCase("Heyday")) {
                        String[] tokens = new String[3];
                        tokens[0] = "";
                        tokens[1] = "";
                        tokens[2] = "";
                        Cursor crsrHeaderFooterSetting = null;
                        crsrHeaderFooterSetting = db.getOwnerDetail_counter();
                        if (crsrHeaderFooterSetting.moveToFirst()) {
                            try {
                                tokens[0] = crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("GSTIN"));
                                tokens[1] = crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("FirmName"));
                                tokens[2] = crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("Address"));
                            } catch (Exception e) {
                                tokens[0] = "";
                                tokens[1] = "";
                                tokens[2] = "";
                            }
                            if (!tokens[0].equalsIgnoreCase(""))
                                item.setAddressLine1(tokens[0]);
                            if (!tokens[1].equalsIgnoreCase(""))
                                item.setAddressLine2(tokens[1]);

                            if(chk_interstate.isChecked())
                            {
                                item.setCustomerName(item.getCustomerName()+ "  ("+(spnr_pos.getSelectedItem().toString())+") ");
                                tokens[2] =  tokens[2] + "\n ("+getState_pos(db.getOwnerPOS_counter())+") ";
                            }
                            item.setAddressLine3(tokens[2]);
                            crsrHeaderFooterSetting = db.getBillSettings();
                            if(crsrHeaderFooterSetting.moveToNext()) {
                                item.setHeaderLine(crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("HeaderText")));
                                item.setFooterLine(crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("FooterText")));
                            }
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
                        Toast.makeText(BillingCounterSalesActivity.this, "Printer not configured. Kindly goto settings and configure printer", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(BillingCounterSalesActivity.this, "Please Enter Bill, Waiter, Table Number", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(BillingCounterSalesActivity.this, "Printer is not ready", Toast.LENGTH_SHORT).show();
            askForConfig();
        }
    }

    public ArrayList<BillKotItem> billPrint(ArrayList<BillTaxSlab> billTaxSlabs) {
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
            TextView UOM_tv = (TextView) row.getChildAt(22);
            TextView IGST_tv = (TextView) row.getChildAt(23);
            TextView CGST_tv = (TextView) row.getChildAt(6);
            TextView SGST_tv = (TextView) row.getChildAt(15);

            int id = Integer.parseInt(itemId.getText().toString().trim());
            int sno = count;
            String name = itemName.getText().toString().trim();
            String hsncode = HSNCode.getText().toString().trim();
            String UOM = UOM_tv.getText().toString().trim();
            Double qty = Double.parseDouble(itemQty.getText().toString().trim());
            double rate = Double.parseDouble(itemRate.getText().toString().trim());
            //double amount = Double.parseDouble(itemAmount.getText().toString().trim());
            double amount = rate *qty;
            String taxIndex = " ";
            double TaxRate =0;
            if(chk_interstate.isChecked())
                TaxRate = Double.parseDouble(IGST_tv.getText().toString().trim());
            else
                TaxRate = Double.parseDouble(CGST_tv.getText().toString().trim()) + Double.parseDouble(SGST_tv.getText().toString().trim());


            for (BillTaxSlab taxEntry : billTaxSlabs)
            {
                if(String.format("%.2f",TaxRate).equals(String.format("%.2f",taxEntry.getTaxRate())))
                {
                    taxIndex = taxEntry.getTaxIndex();
                    break;
                }
            }
            BillKotItem billKotItem = new BillKotItem(sno, name, qty, rate, amount, hsncode,UOM,taxIndex);
            billKotItems.add(billKotItem);
            count++;

        }
        return billKotItems;
    }

    public ArrayList<BillTaxItem> taxPrint() {
        ArrayList<BillTaxItem> billTaxItems = new ArrayList<BillTaxItem>();
        try {

            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(tvDate.getText().toString());
            Cursor crsrTax = db.getItemsForCGSTTaxPrints(Integer.valueOf(tvBillNumber.getText().toString()), String.valueOf(date.getTime()));
            if (crsrTax.moveToFirst()) {
                do {
                    String taxname = "CGST "; //crsrTax.getString(crsrTax.getColumnIndex("TaxDescription"));
                    String taxpercent = crsrTax.getString(crsrTax.getColumnIndex("CGSTRate"));
                    Double taxvalue = Double.parseDouble(crsrTax.getString(crsrTax.getColumnIndex("CGSTAmount")));

                    BillTaxItem taxItem = new BillTaxItem(taxname, Double.parseDouble(taxpercent), Double.parseDouble(String.format("%.2f", taxvalue)));
                    billTaxItems.add(taxItem);
                } while (crsrTax.moveToNext());
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            billTaxItems = new ArrayList<BillTaxItem>();
        }
        finally
        {
            return billTaxItems;
        }

    }
    public ArrayList<BillTaxItem> taxPrint_IGST() {
        ArrayList<BillTaxItem> billTaxItems = new ArrayList<BillTaxItem>();
        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(tvDate.getText().toString());
        Cursor crsrTax = db.getItemsForIGSTTaxPrints(Integer.valueOf(tvBillNumber.getText().toString()), String.valueOf(date.getTime()));
        if (crsrTax.moveToFirst()) {
            do {
                String taxname = "IGST "; //crsrTax.getString(crsrTax.getColumnIndex("TaxDescription"));
                String taxpercent = crsrTax.getString(crsrTax.getColumnIndex("IGSTRate"));
                Double taxvalue = Double.parseDouble(crsrTax.getString(crsrTax.getColumnIndex("IGSTAmount")));

                BillTaxItem taxItem = new BillTaxItem(taxname, Double.parseDouble(taxpercent), Double.parseDouble(String.format("%.2f", taxvalue)));
                billTaxItems.add(taxItem);
            } while (crsrTax.moveToNext());
        }
        }catch (Exception e)
        {
            e.printStackTrace();
            billTaxItems = new ArrayList<BillTaxItem>();
        }
        finally
        {
            return billTaxItems;
        }

    }

    public ArrayList<BillTaxItem> otherChargesPrint() {
        ArrayList<BillTaxItem> billOtherChargesItems = new ArrayList<BillTaxItem>();
        if(isReprint)
        {
            Cursor crsrTax = db.getBillDetail_counter(Integer.parseInt(tvBillNumber.getText().toString()));
            if(crsrTax.moveToFirst())
            {
                String taxname = "OtherCharges";
                double taxpercent = 0;
                Double taxvalue = crsrTax.getDouble(crsrTax.getColumnIndex("DeliveryCharge"));

                if(taxvalue>0)
                {
                    BillTaxItem taxItem = new BillTaxItem(taxname, (taxpercent), Double.parseDouble(String.format("%.2f", taxvalue)));
                    billOtherChargesItems.add(taxItem);
                    /*double totalamt = Double.parseDouble(tvBillAmount.getText().toString().trim());
                    totalamt+= taxvalue;*/
                    //tvBillAmount.setText(String.format("%.2f", totalamt));
                }
            }

        }else
        { // fresh print
            String billingmode= "";
            billingmode = CounterSalesCaption;
            Cursor crsrTax = db.getItemsForOtherChargesPrints(billingmode);
            if (crsrTax.moveToFirst()) {
                do {
                    String taxname = crsrTax.getString(crsrTax.getColumnIndex("ModifierDescription"));
                    String taxpercent = "0";
                    Double taxvalue = Double.parseDouble(crsrTax.getString(crsrTax.getColumnIndex("ModifierAmount")));

                    BillTaxItem taxItem = new BillTaxItem(taxname, Double.parseDouble(taxpercent), Double.parseDouble(String.format("%.2f", taxvalue)));
                    billOtherChargesItems.add(taxItem);
                } while (crsrTax.moveToNext());
            }
        }
        return billOtherChargesItems;
    }

    public ArrayList<BillServiceTaxItem> servicetaxPrint() {
        ArrayList<BillServiceTaxItem> billServiceTaxItems = new ArrayList<BillServiceTaxItem>();
        Cursor crsrTax = db.getItemsForServiceTaxPrints(Integer.valueOf(tvBillNumber.getText().toString()));
        if (crsrTax.moveToFirst()) {
            //do {
            BillServiceTaxItem ServicetaxItem = new BillServiceTaxItem("Service Tax", Double.parseDouble(crsrTax.getString(crsrTax.getColumnIndex("ServiceTaxPercent"))), Double.parseDouble(String.format("%.2f", Double.parseDouble(tvServiceTaxTotal.getText().toString()))));
            billServiceTaxItems.add(ServicetaxItem);
            //} while (crsrTax.moveToNext());
        }
        return billServiceTaxItems;
    }
    public ArrayList<BillServiceTaxItem> SGSTtaxPrint() {
        ArrayList<BillServiceTaxItem> billServiceTaxItems = new ArrayList<BillServiceTaxItem>();
        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(tvDate.getText().toString());
        Cursor crsrTax = db.getItemsForSGSTTaxPrints(Integer.valueOf(tvBillNumber.getText().toString()), String.valueOf(date.getTime()));
        if (crsrTax.moveToFirst()) {
            do {
                String taxname = "SGST "; //crsrTax.getString(crsrTax.getColumnIndex("TaxDescription"));
                String taxpercent = crsrTax.getString(crsrTax.getColumnIndex("SGSTRate"));
                Double taxvalue = Double.parseDouble(crsrTax.getString(crsrTax.getColumnIndex("SGSTAmount")));

                BillServiceTaxItem taxItem = new BillServiceTaxItem(taxname, Double.parseDouble(taxpercent), Double.parseDouble(String.format("%.2f", taxvalue)));
                billServiceTaxItems.add(taxItem);
            } while (crsrTax.moveToNext());
        }
        }catch (Exception e)
        {
            e.printStackTrace();
            billServiceTaxItems = new ArrayList<BillServiceTaxItem>();
        }
        finally
        {
            return billServiceTaxItems;
        }

    }
    public ArrayList<BillServiceTaxItem> cessTaxPrint() {
        ArrayList<BillServiceTaxItem> billcessTaxItems = new ArrayList<BillServiceTaxItem>();
        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(tvDate.getText().toString());
        Cursor crsrTax = db.getItemsForcessTaxPrints(Integer.valueOf(tvBillNumber.getText().toString()), String.valueOf(date.getTime()));
        if (crsrTax.moveToFirst()) {
            do {
                String taxname = "cess "; //crsrTax.getString(crsrTax.getColumnIndex("TaxDescription"));
                String taxpercent = crsrTax.getString(crsrTax.getColumnIndex("cessRate"));
                Double taxvalue = Double.parseDouble(crsrTax.getString(crsrTax.getColumnIndex("cessAmount")));

                BillServiceTaxItem taxItem = new BillServiceTaxItem(taxname, Double.parseDouble(taxpercent), Double.parseDouble(String.format("%.2f", taxvalue)));
                billcessTaxItems.add(taxItem);
            } while (crsrTax.moveToNext());
        }
        }catch (Exception e)
        {
            e.printStackTrace();
            billcessTaxItems = new ArrayList<BillServiceTaxItem>();
        }
        finally
        {
            return billcessTaxItems;
        }

    }
    public ArrayList<BillTaxSlab> TaxSlabPrint_IntraState() {
        ArrayList<BillTaxSlab> billTaxSlabs = new ArrayList<BillTaxSlab>();

        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(tvDate.getText().toString());
            Cursor crsrTax = db.getItemsForTaxSlabPrints(Integer.valueOf(tvBillNumber.getText().toString()), String.valueOf(date.getTime()));
            int count = 0;
            //System.out.println(crsrTax.getCount());
            if (crsrTax.moveToFirst()) {
                do {
                    Double taxpercent = crsrTax.getDouble(crsrTax.getColumnIndex("CGSTRate")) +
                                                crsrTax.getDouble(crsrTax.getColumnIndex("SGSTRate"));

                    Double cgstamt  = Double.parseDouble(crsrTax.getString(crsrTax.getColumnIndex("CGSTAmount")));
                    Double sgstamt  = Double.parseDouble(crsrTax.getString(crsrTax.getColumnIndex("SGSTAmount")));
                    Double taxableValue  = Double.parseDouble(crsrTax.getString(crsrTax.getColumnIndex("TaxableValue")));

                    if (taxpercent == 0)
                        continue;
                    BillTaxSlab taxItem = new BillTaxSlab("",taxpercent, 0.00,cgstamt,sgstamt, taxableValue,cgstamt+sgstamt);

                    int found =0;
                    for (BillTaxSlab taxSlabItem : billTaxSlabs )
                    {
                        if (taxSlabItem.getTaxRate() == taxpercent)
                        {
                            taxSlabItem.setCGSTAmount(taxSlabItem.getCGSTAmount()+cgstamt);
                            taxSlabItem.setSGSTAmount(taxSlabItem.getSGSTAmount()+sgstamt);
                            taxSlabItem.setTaxableValue(taxSlabItem.getTaxableValue()+taxableValue);
                            taxSlabItem.setTotalTaxAmount(taxSlabItem.getTotalTaxAmount()+cgstamt+sgstamt);
                            found =1;
                            break;
                        }
                    }
                    if(found == 0){
                        taxItem.setTaxIndex(Character.toString((char)('A'+count)));
                        count++;
                        billTaxSlabs.add(taxItem);
                    }

                } while (crsrTax.moveToNext());
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            billTaxSlabs = new ArrayList<BillTaxSlab>();
        }
        finally
        {
            return billTaxSlabs;
        }

    }
    public ArrayList<BillTaxSlab> TaxSlabPrint_InterState() {
        ArrayList<BillTaxSlab> billTaxSlabs = new ArrayList<BillTaxSlab>();

        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(tvDate.getText().toString());
            Cursor crsrTax = db.getItemsForTaxSlabPrints(Integer.valueOf(tvBillNumber.getText().toString()), String.valueOf(date.getTime()));
            int count = 0;
            //System.out.println(crsrTax.getCount());
            if (crsrTax.moveToFirst()) {
                do {
                    Double taxpercent = crsrTax.getDouble(crsrTax.getColumnIndex("IGSTRate"));
                    Double igstamt  = Double.parseDouble(crsrTax.getString(crsrTax.getColumnIndex("IGSTAmount")));
                    Double taxableValue  = Double.parseDouble(crsrTax.getString(crsrTax.getColumnIndex("TaxableValue")));

                    if (taxpercent == 0)
                        continue;

                    BillTaxSlab taxItem = new BillTaxSlab("",taxpercent, igstamt,0.00,0.00, taxableValue,igstamt);
                    int found =0;
                    for (BillTaxSlab taxSlabItem : billTaxSlabs )
                    {
                        if (taxSlabItem.getTaxRate() == taxpercent)
                        {
                            taxSlabItem.setIGSTAmount(taxSlabItem.getIGSTAmount()+igstamt);
                            taxSlabItem.setTaxableValue(taxSlabItem.getTaxableValue()+taxableValue);
                            taxSlabItem.setTotalTaxAmount(taxSlabItem.getTotalTaxAmount()+igstamt);
                            found =1;
                            break;
                        }
                    }
                    if(found == 0){
                        taxItem.setTaxIndex(Character.toString((char)('A'+count)));
                        count++;
                        billTaxSlabs.add(taxItem);
                    }

                } while (crsrTax.moveToNext());
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            billTaxSlabs = new ArrayList<BillTaxSlab>();
        }
        finally
        {
            return billTaxSlabs;
        }

    }
    public ArrayList<BillSubTaxItem> subtaxPrint() {
        ArrayList<BillSubTaxItem> billSubTaxItems = new ArrayList<BillSubTaxItem>();
        Cursor crsrSubTax = db.getAllSubTaxConfigs("2");
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
    // -----Print Bill Code Ended-----

    private void UpdateItemStock(Cursor crsrUpdateStock, float Quantity) {
        int iResult = 0;
        float fCurrentStock = 0, fNewStock = 0;

        // Get current stock of item
        fCurrentStock = crsrUpdateStock.getFloat(crsrUpdateStock.getColumnIndex("Quantity"));

        // New Stock
        fNewStock = fCurrentStock - Quantity;

        // Update new stock for item
        iResult = db.updateItemStock(crsrUpdateStock.getInt(crsrUpdateStock.getColumnIndex("MenuCode")),
                fNewStock);

        Log.d("UpdateItemStock", "Updated Rows:" + iResult);

    }

    private void updateOutwardStock()
    {
        Log.d(TAG, "updateOutwardStock()");
        String businessdate = tvDate.getText().toString();
        DatabaseHandler db_local = new DatabaseHandler(BillingCounterSalesActivity.this);
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
                TextView ItemQuantity = (TextView) RowBillItem.getChildAt(3);
                double qty_to_reduce = Double.parseDouble(ItemQuantity.getText().toString());
                //Cursor cursor = db_local.getItem(menuCode);
                Cursor cursor = db_local.getOutwardStockItem(businessdate,menuCode);
                if(cursor!=null && cursor.moveToNext())
                {
                    closingQty = cursor.getDouble(cursor.getColumnIndex("ClosingStock"));
                    if(closingQty<= qty_to_reduce)
                        closingQty =0;
                    else
                        closingQty -= qty_to_reduce;
                    stock_outward.updateClosingStock_Outward(businessdate,menuCode,itemname,closingQty);
                }

            }


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
            switch (requestCode)
            {
                case 1: // PayBill Activity Result
                    boolean isComplimentaryBill, isDiscounted, isPrintBill = false;
                    float dDiscPercent;
                    String strComplimentaryReason = "";
                    iCustId = data.getIntExtra("CUST_ID", 1);
                    customerId = iCustId+"";
                    isComplimentaryBill = data.getBooleanExtra(PayBillActivity.IS_COMPLIMENTARY_BILL, false);

                    isPrintBill = data.getBooleanExtra(PayBillActivity.IS_PRINT_BILL, true);
                    strComplimentaryReason = data.getStringExtra(PayBillActivity.COMPLIMENTARY_REASON);
                    dDiscPercent = data.getFloatExtra("DISCOUNT_PERCENTAGE", 0);

                    fCashPayment = data.getFloatExtra(PayBillActivity.TENDER_CASH_VALUE, 0);
                    fCardPayment = data.getFloatExtra(PayBillActivity.TENDER_CARD_VALUE, 0);
                    fCouponPayment = data.getFloatExtra(PayBillActivity.TENDER_COUPON_VALUE, 0);

                    fPettCashPayment = data.getFloatExtra(PayBillActivity.TENDER_PETTYCASH_VALUE, 0);
                    fPaidTotalPayment = data.getFloatExtra(PayBillActivity.TENDER_PAIDTOTAL_VALUE, 0);
                    fWalletPayment = data.getFloatExtra(PayBillActivity.TENDER_WALLET_VALUE, 0);
                    fChangePayment = data.getFloatExtra(PayBillActivity.TENDER_CHANGE_AMOUNT, 0);
                    isDiscounted = data.getBooleanExtra(PayBillActivity.IS_DISCOUNTED, false);
                    fTotalDiscount = 0;
                    fTotalDiscount = data.getFloatExtra(PayBillActivity.DISCOUNT_AMOUNT, 0);

                    /*iCustId = data.getIntExtra("CUST_ID", 1);
                    customerId = iCustId+"";*/
                    if (isDiscounted == true) {
                        Log.v("Tender Result", "Discounted:" + isDiscounted);
                        Log.v("Tender Result", "Discount Amount:" + fTotalDiscount);
                        tvDiscountAmount.setText(String.valueOf(fTotalDiscount));
                        tvDiscountPercentage.setText(String.valueOf(dDiscPercent));
                        //float total = Float.parseFloat(tvBillAmount.getText().toString());
                        //total = Math.round(total);
                        /*total -= fTotalDiscount;
                        tvBillAmount.setText(String.format("%.2f",total));*/

                        double igst = data.getDoubleExtra("TotalIGSTAmount",0);
                        double cgst = data.getDoubleExtra("TotalCGSTAmount",0);
                        double sgst = data.getDoubleExtra("TotalSGSTAmount",0);
                        double cess = data.getDoubleExtra("TotalcessAmount",0);
                        double billtot = data.getDoubleExtra("TotalBillAmount",0);
                        if(billtot >0)
                        {
                            tvBillAmount.setText(String.format("%.2f",billtot));
                            tvcessValue.setText(String.format("%.2f",cess));
                            if(chk_interstate.isChecked())
                            {
                                tvIGSTValue.setText(String.format("%.2f", igst));
                                tvTaxTotal.setText(String.format("%.2f", igst));
                                tvServiceTaxTotal.setText("0.00");
                            }else {
                                tvIGSTValue.setText(String.format("0.00"));
                                tvTaxTotal.setText(String.format("%.2f", cgst));
                                tvServiceTaxTotal.setText(String.format("%.2f", sgst));
                            }

                        }

                        ArrayList<AddedItemsToOrderTableClass> orderList_recieved = data.getParcelableArrayListExtra("OrderList");
                        for(int i =0;i<tblOrderItems.getChildCount();i++)
                        {
                            TableRow RowBillItem = (TableRow) tblOrderItems.getChildAt(i);

                            // Item Number
                            if (RowBillItem.getChildAt(0) != null) {
                                CheckBox ItemNumber = (CheckBox) RowBillItem.getChildAt(0);
                                int menucode = (Integer.parseInt(ItemNumber.getText().toString()));
                                for(AddedItemsToOrderTableClass item : orderList_recieved) {
                                    if(item.getMenuCode() == menucode) {

                                        /*if (RowBillItem.getChildAt(5) != null) {
                                            TextView Amount = (TextView) RowBillItem.getChildAt(5);
                                            Amount.setText(String.format("%.2f",item.getTaxableValue()));
                                        }*/
                                        if (RowBillItem.getChildAt(9) != null ) {
                                            TextView DiscountAmount = (TextView) RowBillItem.getChildAt(9);
                                            DiscountAmount.setText(String.format("%.2f",item.getDiscountamount()));
                                        }
                                        if (RowBillItem.getChildAt(16) != null) {
                                            TextView ServiceTaxAmount = (TextView) RowBillItem.getChildAt(16);
                                            ServiceTaxAmount.setText(String.format("%.2f",item.getSgstAmt()));
                                        }
                                        // Sales Tax Amount
                                        if (RowBillItem.getChildAt(7) != null) {
                                            TextView SalesTaxAmount = (TextView) RowBillItem.getChildAt(7);
                                            SalesTaxAmount.setText(String.format("%.2f",item.getCgstAmt()));
                                        }
                                        if (RowBillItem.getChildAt(24) != null) {
                                            TextView IGSTTaxAmount = (TextView) RowBillItem.getChildAt(24);
                                            IGSTTaxAmount.setText(String.format("%.2f",item.getIgstAmt()));
                                        }
                                        if (RowBillItem.getChildAt(26) != null) {
                                            TextView cessAmount = (TextView) RowBillItem.getChildAt(26);
                                            cessAmount.setText(String.format("%.2f",item.getCessAmt()));
                                        }
                                        if (RowBillItem.getChildAt(5) != null) {
                                            TextView TaxableValue = (TextView) RowBillItem.getChildAt(5);
                                            TaxableValue.setText(String.format("%.2f",item.getTaxableValue()));
                                        }
                                    }
                                }
                            }
                        }
                    }

                    PrintBillPayment =0;
                    l(2, isPrintBill);
                    updateOutwardStock();
                    Toast.makeText(BillingCounterSalesActivity.this, "Bill saved Successfully", Toast.LENGTH_SHORT).show();
                    if (isComplimentaryBill == true) {
                        // Save complimentary bill details
                        SaveComplimentaryBill(Integer.parseInt(tvBillNumber.getText().toString()), (fCashPayment + fCardPayment + fCouponPayment), strComplimentaryReason);
                    }
                    if (isPrintBill == true) {
                        strPaymentStatus = "Paid";
                        PrintNewBill();
                    }
                    if (jBillingMode == 2) {
                        int iResult = db.deleteKOTItem(iCustId, String.valueOf(jBillingMode));
                        ClearAll();
                        btn_PrintBill.setEnabled(true);
                    }
                    break;
            }
        }
        else if (resultCode == RESULT_CANCELED)
        {
            try {
                if (data.getBooleanExtra("isCancelled", false)) {
                    finish();
                }
            } catch (Exception e) {

            }
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

        lResult = db.addComplimentaryBillDetails(objComplimentaryBillDetail);

        Log.v("SaveComplimentaryBill", "Complimentary Bill inserted at Row:" + lResult);
    }

    private void loadAutoCompleteData() {

        // List - Get Item Name
        List<String> labelsItemName = db.getAllItemsNames();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                labelsItemName);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

        // attaching data adapter to spinner
        autoCompleteTextViewSearchItem.setAdapter(dataAdapter);

        // List - Get Menu Code
        List<String> labelsMenuCode = db.getAllMenuCodes();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                labelsMenuCode);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);

        // attaching data adapter to spinner
        autoCompleteTextViewSearchMenuCode.setAdapter(dataAdapter1);

        POS_LIST = ArrayAdapter.createFromResource(this, R.array.poscode, android.R.layout.simple_spinner_item);
        spnr_pos.setAdapter(POS_LIST);

    }

    /*************************************************************************************************************************************
     * Delete Bill Button Click event, calls delte bill function
     *
     *************************************************************************************************************************************/
    public void deleteBill()
    {
        tblOrderItems.removeAllViews();
        AlertDialog.Builder DineInTenderDialog = new AlertDialog.Builder(BillingCounterSalesActivity.this);
        LayoutInflater UserAuthorization = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vwAuthorization = UserAuthorization.inflate(R.layout.dinein_reprint, null);
        final ImageButton btnCal_reprint = (ImageButton) vwAuthorization.findViewById(R.id.btnCal_reprint);

        final EditText txtReprintBillNo = (EditText) vwAuthorization.findViewById(R.id.txtDineInReprintBillNumber);
        final TextView tv_inv_date = (TextView) vwAuthorization.findViewById(R.id.tv_inv_date);
        tv_inv_date.setText(tvDate.getText().toString());
        btnCal_reprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateSelection(tv_inv_date);
            }
        });

        DineInTenderDialog.setIcon(R.drawable.ic_launcher)
                .setTitle("Delete Bill")
                /*.setMessage("Enter Bill Number")*/
                .setView(vwAuthorization)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        if (txtReprintBillNo.getText().toString().equalsIgnoreCase(""))
                        {
                            messageDialog.Show("Warning", "Please enter Bill Number");
                            return;
                        }
                        else if (tv_inv_date.getText().toString().equalsIgnoreCase("")) {
                            messageDialog.Show("Warning", "Please enter Bill Date");
                            setInvoiceDate();
                            return;
                        }  else {
                            try {
                                int InvoiceNo = Integer.valueOf(txtReprintBillNo.getText().toString());
                                String date_reprint = tv_inv_date.getText().toString();
                                tvDate.setText(date_reprint);
                                Date date = new SimpleDateFormat("dd-MM-yyyy").parse(date_reprint);
                                Cursor result = db.getBillDetail_counter(InvoiceNo, String.valueOf(date.getTime()));
                                if (result.moveToFirst()) {
                                    if (result.getInt(result.getColumnIndex("BillStatus")) != 0) {
                                        VoidBill(InvoiceNo, String.valueOf(date.getTime()));
                                    } else {
                                        //Toast.makeText(BillingCounterSalesActivity.this, "Bill is already voided", Toast.LENGTH_SHORT).show();

                                        String msg = "Bill Number " + InvoiceNo + " is already voided";
                                        messageDialog.Show("Note",msg);
                                        Log.d("VoidBill", msg);
                                    }
                                } else {
                                    //Toast.makeText(BillingCounterSalesActivity.this, "No bill found with bill number " + InvoiceNo, Toast.LENGTH_SHORT).show();
                                    String msg = "No bill found with bill number " + InvoiceNo;
                                    messageDialog.Show("Note",msg);
                                    Log.d("VoidBill", msg);
                                }
                                ClearAll();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).show();
    }

    /*************************************************************************************************************************************
     * Void Bill Button Click event, opens a dialog to enter admin user id and
     * password for voiding bill if user is admin then bill will be voided
     *
     *************************************************************************************************************************************/
    public void VoidBill(final int invoiceno , final String Invoicedate) {

        AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(this);

        LayoutInflater UserAuthorization = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vwAuthorization = UserAuthorization.inflate(R.layout.user_authorization, null);

        final EditText txtUserId = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserId);
        final EditText txtPassword = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserPassword);

        AuthorizationDialog.setTitle("Authorization")
                .setIcon(R.drawable.ic_launcher)
                .setView(vwAuthorization)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Cursor User = db.getUserr(txtUserId.getText().toString(),
                        txtPassword.getText().toString());
                if (User.moveToFirst()) {
                    if (User.getInt(User.getColumnIndex("RoleId")) == 1) {
                        //ReprintVoid(Byte.parseByte("2"));
                        int result = db.makeBillVoids(invoiceno, Invoicedate);
                        if(result >0)
                        {
                            Date dd = new Date(Long.parseLong(Invoicedate));
                            String dd_str = new SimpleDateFormat("dd-MM-yyyy").format(dd);
                            String msg = "Bill Number "+invoiceno+" , Dated : "+dd_str+" voided successfully";
                            // MsgBox.Show("Warning", msg);
                            Toast.makeText(BillingCounterSalesActivity.this, msg, Toast.LENGTH_SHORT).show();
                            Log.d("VoidBill", msg);
                        }
                    } else {
                        messageDialog.Show("Warning", "Void Bill failed due to in sufficient access privilage");
                    }
                } else {
                    messageDialog.Show("Warning", "Void Bill failed due to wrong user id or password");
                }
            }
        }).show();
    }

    /*************************************************************************************************************************************
     * Reprint Bill Button Click event, calls reprint bill function
     *
     *************************************************************************************************************************************/
    public void reprintBill() {


        //ReprintVoid(Byte.parseByte("1"));
        tblOrderItems.removeAllViews();

        AlertDialog.Builder DineInTenderDialog = new AlertDialog.Builder(this);

        LayoutInflater UserAuthorization = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vwAuthorization = UserAuthorization.inflate(R.layout.dinein_reprint, null);

        final ImageButton btnCal_reprint = (ImageButton) vwAuthorization.findViewById(R.id.btnCal_reprint);

        final EditText txtReprintBillNo = (EditText) vwAuthorization.findViewById(R.id.txtDineInReprintBillNumber);
        final TextView tv_inv_date = (TextView) vwAuthorization.findViewById(R.id.tv_inv_date);
        tv_inv_date.setText(tvDate.getText().toString());
        btnCal_reprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateSelection(tv_inv_date);
            }
        });


        DineInTenderDialog.setIcon(R.drawable.ic_launcher).setTitle("RePrint Bill")
                .setView(vwAuthorization).setNegativeButton("Cancel", null)
                .setPositiveButton("RePrint", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        if (txtReprintBillNo.getText().toString().equalsIgnoreCase("")) {
                            messageDialog.Show("Warning", "Please enter Bill Number");
                            setInvoiceDate();
                            return;
                        } else if (tv_inv_date.getText().toString().equalsIgnoreCase("")) {
                            messageDialog.Show("Warning", "Please enter Bill Date");
                            setInvoiceDate();
                            return;
                        } else {
                            try {
                                int billNo = Integer.valueOf(txtReprintBillNo.getText().toString());
                                String date_reprint = tv_inv_date.getText().toString();
                                tvDate.setText(date_reprint);
                                Date date = new SimpleDateFormat("dd-MM-yyyy").parse(date_reprint);
                                Cursor LoadItemForReprint = db.getItemsFromBillItem_new(
                                        billNo, String.valueOf(date.getTime()));
                                if (LoadItemForReprint.moveToFirst()) {
                                    Cursor cursor = db.getBillDetail_counter(billNo, String.valueOf(date.getTime()));
                                    if (cursor != null && cursor.moveToFirst()) {
                                        int billStatus = cursor.getInt(cursor.getColumnIndex("BillStatus"));
                                        if (billStatus == 0) {
                                            messageDialog.Show("Warning", "This bill has been deleted");
                                            setInvoiceDate();
                                            return;
                                        }
                                        String pos = cursor.getString(cursor.getColumnIndex("POS"));
                                        String custStateCode = cursor.getString(cursor.getColumnIndex("CustStateCode"));
                                        if (pos != null && !pos.equals("") && custStateCode != null && !custStateCode.equals("") && !custStateCode.equalsIgnoreCase(pos)) {
                                            chk_interstate.setChecked(true);
                                            int index = getIndex_pos(custStateCode);
                                            spnr_pos.setSelection(index);
                                            //System.out.println("reprint : InterState");
                                        } else {
                                            chk_interstate.setChecked(false);
                                            spnr_pos.setSelection(0);
                                            //System.out.println("reprint : IntraState");
                                        }
                                        fTotalDiscount = cursor.getFloat(cursor.getColumnIndex("TotalDiscountAmount"));
                                        float discper = cursor.getFloat(cursor.getColumnIndex("DiscPercentage"));
                                        reprintBillingMode = cursor.getInt(cursor.getColumnIndex("BillingMode"));

                                        tvDiscountPercentage.setText(String.format("%.2f", discper));
                                        tvDiscountAmount.setText(String.format("%.2f", fTotalDiscount));
                                        tvBillNumber.setText(txtReprintBillNo.getText().toString());

                                        tvIGSTValue.setText(String.format("%.2f", cursor.getFloat(cursor.getColumnIndex("IGSTAmount"))));
                                        tvTaxTotal.setText(String.format("%.2f", cursor.getFloat(cursor.getColumnIndex("CGSTAmount"))));
                                        tvServiceTaxTotal.setText(String.format("%.2f", cursor.getFloat(cursor.getColumnIndex("SGSTAmount"))));
                                        tvSubTotal.setText(String.format("%.2f", cursor.getFloat(cursor.getColumnIndex("TaxableValue"))));
                                        tvBillAmount.setText(String.format("%.2f", cursor.getFloat(cursor.getColumnIndex("BillAmount"))));

                                        LoadItemsForReprintBill(LoadItemForReprint);
                                        Cursor crsrBillDetail = db.getBillDetail_counter(Integer.valueOf(txtReprintBillNo.getText().toString()));
                                        if (crsrBillDetail.moveToFirst()) {
                                            customerId = (crsrBillDetail.getString(crsrBillDetail.getColumnIndex("CustId")));
                                        }
                                    }

                                } else {
                                    messageDialog.Show("Warning",
                                            "No Item is present for the Bill Number " + txtReprintBillNo.getText().toString() +", Dated :"+tv_inv_date.getText().toString());
                                    setInvoiceDate();
                                    return;
                                }
                                strPaymentStatus = "Paid";
                                isReprint = true;
                                PrintNewBill();
                                // update bill reprint count
                                int Result = db
                                        .updateBillRepintCounts(Integer.parseInt(txtReprintBillNo.getText().toString()));
                                ClearAll();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).show();



    }

    private void DateSelection(final TextView tv_inv_date ) {        // StartDate: DateType = 1 EndDate: DateType = 2
        try {
            AlertDialog.Builder dlgReportDate = new AlertDialog.Builder(this);
            final DatePicker dateReportDate = new DatePicker(this);
            String date_str = tvDate.getText().toString();
            String dd = date_str.substring(6,10)+"-"+date_str.substring(3,5)+"-"+date_str.substring(0,2);
            DateTime objDate = new DateTime(dd);
            dateReportDate.updateDate(objDate.getYear(), objDate.getMonth(), objDate.getDay());
            String strMessage = "";


            dlgReportDate
                    .setIcon(R.drawable.ic_launcher)
                    .setTitle("Date Selection")
                    .setMessage(strMessage)
                    .setView(dateReportDate)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        String strDd = "";
                        public void onClick(DialogInterface dialog, int which) {
                            if (dateReportDate.getDayOfMonth() < 10) {
                                strDd = "0" + String.valueOf(dateReportDate.getDayOfMonth())+"-";
                            } else {
                                strDd = String.valueOf(dateReportDate.getDayOfMonth())+"-";
                            }
                            if (dateReportDate.getMonth() < 9) {
                                strDd += "0" + String.valueOf(dateReportDate.getMonth() + 1) + "-";
                            } else {
                                strDd += String.valueOf(dateReportDate.getMonth() + 1) + "-";
                            }

                            strDd += String.valueOf(dateReportDate.getYear());
                            tv_inv_date.setText(strDd);
                            Log.d("ReprintDate", "Selected Date:" + strDd);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    })
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                tvServiceTaxAmt , tvUOM;
        EditText etQty, etRate;
        CheckBox Number;
        ImageButton ImgDelete;

        if (crsrBillItems.moveToFirst()) {
            // Display items in table
            do {
                rowItem = new TableRow(BillingCounterSalesActivity.this);
                rowItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                // Item Number
                Number = new CheckBox(BillingCounterSalesActivity.this);
                Number.setWidth(40);
                Number.setTextSize(0);
                Number.setTextColor(Color.TRANSPARENT);
                Number.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ItemNumber")));

                // Item Name
                tvName = new TextView(BillingCounterSalesActivity.this);
                tvName.setWidth(135);
                tvName.setTextSize(11);
                tvName.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ItemName")));

                //hsn code
                tvHSn = new TextView(BillingCounterSalesActivity.this);
                tvHSn.setWidth(67); // 154px ~= 230dp
                tvHSn.setTextSize(11);
                if (GSTEnable.equalsIgnoreCase("1") && (HSNEnable_out != null) && HSNEnable_out.equals("1")) {
                    tvHSn.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("HSNCode")));
                }

                // Quantity
                etQty = new EditText(BillingCounterSalesActivity.this);
                etQty.setWidth(55);
                etQty.setTextSize(11);
                etQty.setText(String.format("%.2f", crsrBillItems.getDouble(crsrBillItems.getColumnIndex("Quantity"))));
                etQty.setOnClickListener(Qty_Rate_Click);
                etInputValidate.ValidateDecimalInput(etQty);

                // Rate
                etRate = new EditText(BillingCounterSalesActivity.this);
                etRate.setWidth(75);
                etRate.setEnabled(false);
                etRate.setTextSize(11);
                etRate.setText(String.format("%.2f", crsrBillItems.getDouble(crsrBillItems.getColumnIndex("Value"))));

                // Amount
                tvAmount = new TextView(BillingCounterSalesActivity.this);
                tvAmount.setWidth(105);
                tvAmount.setTextSize(11);
                tvAmount.setText(
                        String.format("%.2f", crsrBillItems.getDouble(crsrBillItems.getColumnIndex("TaxableValue"))));

                // Sales Tax%
                tvTaxPercent = new TextView(BillingCounterSalesActivity.this);
                tvTaxPercent.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("CGSTRate")));

                // Sales Tax Amount
                tvTaxAmt = new TextView(BillingCounterSalesActivity.this);
                tvTaxAmt.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("CGSTAmount")));

                // Discount %
                tvDiscPercent = new TextView(BillingCounterSalesActivity.this);
                tvDiscPercent.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("DiscountPercent")));

                // Discount Amount
                tvDiscAmt = new TextView(BillingCounterSalesActivity.this);
                tvDiscAmt.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("DiscountAmount")));

                // Dept Code
                tvDeptCode = new TextView(BillingCounterSalesActivity.this);
                tvDeptCode.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("DeptCode")));

                // Categ Code
                tvCategCode = new TextView(BillingCounterSalesActivity.this);
                tvCategCode.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("CategCode")));

                // Kitchen Code
                tvKitchenCode = new TextView(BillingCounterSalesActivity.this);
                tvKitchenCode.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("KitchenCode")));

                // Tax Type
                tvTaxType = new TextView(BillingCounterSalesActivity.this);
                tvTaxType.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("TaxType")));

                // Modifier Amount
                tvModifierCharge = new TextView(BillingCounterSalesActivity.this);
                tvModifierCharge.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("ModifierAmount")));

                // Service Tax %
                tvServiceTaxPercent = new TextView(BillingCounterSalesActivity.this);
                tvServiceTaxPercent.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("SGSTRate")));

                // Service Tax Amount
                tvServiceTaxAmt = new TextView(BillingCounterSalesActivity.this);
                tvServiceTaxAmt.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("SGSTAmount")));

                // UOM
                tvUOM = new TextView(BillingCounterSalesActivity.this);
                tvUOM.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("UOM")));

                // space
                TextView tvSpace = new TextView(BillingCounterSalesActivity.this);
                TextView tvSpace1 = new TextView(BillingCounterSalesActivity.this);
                TextView tvPrintKOTStatus = new TextView(BillingCounterSalesActivity.this);

                // SupplyType
                TextView SupplyType = new TextView(BillingCounterSalesActivity.this);
                SupplyType.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("SupplyType")));

                // IGSTRate
                TextView tvIGSTRate = new TextView(BillingCounterSalesActivity.this);
                tvIGSTRate.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("IGSTRate")));

                // IGSTAmount
                TextView tvIGSTAmt = new TextView(BillingCounterSalesActivity.this);
                tvIGSTAmt.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("IGSTAmount")));

                // cessRate
                TextView tvcess = new TextView(BillingCounterSalesActivity.this);
                tvcess.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("cessRate")));

                // IGSTAmount
                TextView tvcessAmt = new TextView(BillingCounterSalesActivity.this);
                tvcessAmt.setText(crsrBillItems.getString(crsrBillItems.getColumnIndex("cessAmount")));

                // Delete
                int res = getResources().getIdentifier("delete", "drawable", this.getPackageName());
                ImgDelete = new ImageButton(BillingCounterSalesActivity.this);
                ImgDelete.setImageResource(res);
                ImgDelete.setVisibility(View.INVISIBLE);

                // Add all text views and edit text to Item Row
                // rowItem.addView(tvNumber);
                rowItem.addView(Number); //0
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
                rowItem.addView(tvIGSTRate);//23
                rowItem.addView(tvIGSTAmt);//24
                rowItem.addView(tvcess);//25
                rowItem.addView(tvcessAmt);//26
                // Add row to table
                tblOrderItems.addView(rowItem, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            } while (crsrBillItems.moveToNext());

            //CalculateTotalAmountforRePrint();

        } else {
            Log.d("LoadKOTItems", "No rows in cursor");
        }
    }

    /*private void CalculateTotalAmountforRePrint() {

        double dSubTotal = 0, dTaxTotal = 0, dModifierAmt = 0, dServiceTaxAmt = 0, dOtherCharges = 0, dTaxAmt = 0, dSerTaxAmt = 0;
        float dTaxPercent = 0, dSerTaxPercent = 0;
        double discountamt = Double.parseDouble(tvDiscountAmount.getText().toString());
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
        Cursor crsrtax = db.getTaxConfigs(1);
        if (crsrtax.moveToFirst()) {
            dTaxPercent = crsrtax.getFloat(crsrtax.getColumnIndex("TotalPercentage"));
            dTaxAmt += dSubTotal * (dTaxPercent / 100);
        }
        Cursor crsrtax1 = db.getTaxConfigs(2);
        if (crsrtax1.moveToFirst()) {
            dSerTaxPercent = crsrtax1.getFloat(crsrtax1.getColumnIndex("TotalPercentage"));
            dSerTaxAmt += dSubTotal * (dSerTaxPercent / 100);
        }
        // -------------------------------------------------

        //dOtherCharges = Double.valueOf(textViewOtherCharges.getText().toString());
        if (crsrSettings.moveToFirst()) {
            if (crsrSettings.getString(crsrSettings.getColumnIndex("Tax")).equalsIgnoreCase("1")) {
                if (crsrSettings.getString(crsrSettings.getColumnIndex("TaxType")).equalsIgnoreCase("1")) {

                    tvIGSTValue.setText(String.format("%.2f", dTaxTotal + dServiceTaxAmt));
                    tvTaxTotal.setText(String.format("%.2f", dTaxTotal));
                    tvServiceTaxTotal.setText(String.format("%.2f", dServiceTaxAmt));


                    if (chk_interstate.isChecked()) // interstate
                    {
                        tvIGSTValue.setTextColor(Color.WHITE);
                        tvTaxTotal.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                        tvServiceTaxTotal.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                    } else {
                        tvIGSTValue.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                        tvTaxTotal.setTextColor(Color.WHITE);
                        tvServiceTaxTotal.setTextColor(Color.WHITE);
                    }

                    tvSubTotal.setText(String.format("%.2f", dSubTotal));
                    tvBillAmount.setText(String.format("%.2f", dSubTotal + dTaxTotal + dServiceTaxAmt + dOtherCharges- discountamt));
                } else {

                    tvIGSTValue.setText(String.format("%.2f", dTaxTotal + dServiceTaxAmt));
                    tvTaxTotal.setText(String.format("%.2f", dTaxTotal));
                    tvServiceTaxTotal.setText(String.format("%.2f", dServiceTaxAmt));

                    if (chk_interstate.isChecked()) // interstate
                    {
                        tvIGSTValue.setTextColor(Color.WHITE);
                        tvTaxTotal.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                        tvServiceTaxTotal.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                    } else {
                        tvIGSTValue.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                        tvTaxTotal.setTextColor(Color.WHITE);
                        tvServiceTaxTotal.setTextColor(Color.WHITE);
                    }
                    tvSubTotal.setText(String.format("%.2f", dSubTotal));
                    tvBillAmount.setText(String.format("%.2f", dSubTotal + dTaxAmt + dSerTaxAmt + dOtherCharges-discountamt));
                }
            } else {
                if (crsrSettings.getString(crsrSettings.getColumnIndex("TaxType")).equalsIgnoreCase("1")) {
                    tvIGSTValue.setText(String.format("%.2f", dTaxTotal + dServiceTaxAmt));
                    tvTaxTotal.setText(String.format("%.2f", dTaxTotal));
                    tvServiceTaxTotal.setText(String.format("%.2f", dServiceTaxAmt));

                    if (chk_interstate.isChecked()) // interstate
                    {
                        tvIGSTValue.setTextColor(Color.WHITE);
                        tvTaxTotal.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                        tvServiceTaxTotal.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                    } else {
                        tvIGSTValue.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                        tvTaxTotal.setTextColor(Color.WHITE);
                        tvServiceTaxTotal.setTextColor(Color.WHITE);
                    }
                    tvSubTotal.setText(String.format("%.2f", dSubTotal));
                    tvBillAmount.setText(String.format("%.2f", dSubTotal + dOtherCharges-discountamt));

                } else {
                    tvSubTotal.setText(String.format("%.2f", dSubTotal));
                    tvIGSTValue.setText(String.format("%.2f", dTaxTotal + dServiceTaxAmt));
                    tvTaxTotal.setText(String.format("%.2f", dTaxTotal));
                    tvServiceTaxTotal.setText(String.format("%.2f", dServiceTaxAmt));

                    if (chk_interstate.isChecked()) // interstate
                    {
                        tvIGSTValue.setTextColor(Color.WHITE);
                        tvTaxTotal.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                        tvServiceTaxTotal.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                    } else {
                        tvIGSTValue.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                        tvTaxTotal.setTextColor(Color.WHITE);
                        tvServiceTaxTotal.setTextColor(Color.WHITE);
                    }
                    tvBillAmount.setText(String.format("%.2f", dSubTotal + dOtherCharges-discountamt));
                }
            }
        }
    }
*/
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(BillingCounterSalesActivity.this);
            LayoutInflater UserAuthorization = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    .setIcon((R.drawable.ic_launcher))
                    .setTitle("Are you sure you want to exit ?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            db.close();
                            finish();
                        }
                    })
                    .show();
        }

        return super.onKeyDown(keyCode, event);
    }
}
