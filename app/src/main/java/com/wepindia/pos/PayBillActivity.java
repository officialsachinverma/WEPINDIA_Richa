package com.wepindia.pos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.app.Dialog;
import android.widget.Toast;

import com.mswipetech.wisepad.payment.MSwipePaymentActivity;
import com.mswipetech.wisepad.payment.PasswordChangeActivity;
import com.mswipetech.wisepad.payment.fragments.FragmentLogin;
import com.mswipetech.wisepad.sdk.MswipeWisepadController;
import com.mswipetech.wisepad.sdktest.data.AppPrefrences;
import com.mswipetech.wisepad.sdktest.view.Constants;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.print.Payment;
import com.wep.common.app.views.WepButton;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.utils.AddedItemsToOrderTableClass;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class PayBillActivity extends FragmentActivity implements FragmentLogin.OnLoginCompletedListener,PaymentResultListener {

    private static final String TAG = PayBillActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CARD_PAYMENT = 12;
    Context myContext;

    AlertDialog alertDialog = null;

    // DatabaseHandler object
    DatabaseHandler dbPayBill = new DatabaseHandler(PayBillActivity.this);
    // MessageDialog object
    MessageDialog MsgBox;
    Dialog PayBillDialog;

    TextView tvCustId;
    EditText edtTotalValue, edtRoundOff, edtPaid, edtCard, edtChange, edtPettyCash, edtDiscount, edtVoucher, edtCoupon, edtTenderTotalValue;
    WepButton btnCreditCustomer, btnDiscount, btnCardPayment, /*btnVoucher,*/ btnSaveBill, btnCoupon, btnPrintBill;
    TableLayout tblPayBill;
    String strTotal, strCustId = "0";
    float baseValue_recieved =0;
    int taxType_recieved =0;
    double dRoundoffTotal;
    float dWalletPayment =0;
    int RESETCALLED =0;
    float discPercent =0;
    double otherCharges_recieved =0;
    double totalcessAmount =0;
    double totalIGSTAmount =0;
    double totalCGSTAmount =0;
    double totalSGSTAmount =0;
    double totalBillAmount =0;



    // Variables
    public static final String IS_COMPLIMENTARY_BILL = "false";
    public static final String COMPLIMENTARY_REASON = "complimenatry_reason";
    public static final String IS_DISCOUNTED = "IS_DISCOUNTED";
    public static final String IS_PRINT_BILL = "false";
    public static final String DISCOUNT_AMOUNT = "DISCOUNT_AMOUNT";
    public static final String TENDER_BILL_TOTAL = "bill_total";
    public static final String TENDER_TENDER_AMOUNT = "tender_amount";
    public static final String TENDER_CHANGE_AMOUNT = "change_amount";
    public static final String TENDER_CASH_VALUE = "cash_value";
    public static final String TENDER_CARD_VALUE = "card_value";
    public static final String TENDER_COUPON_VALUE = "coupon_value";

    public static final String TENDER_PETTYCASH_VALUE = "pettycash_value";
    public static final String TENDER_PAIDTOTAL_VALUE = "paidtotal_value";
    public static final String TENDER_CHANGE_VALUE = "change_value";
    public static final String TENDER_WALLET_VALUE = "wallet_value";
    public static final String ORDER_DELIVERED = "ORDER_DELIVERED";
    boolean strOrderDelivered = false;
    String txt;
    SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private Intent intent = null;
    private String phone;
    String strUserName = "";
    Cursor crsrCustomer;
    ArrayList<AddedItemsToOrderTableClass> orderList_recieved;
    Date d;
    private double toPayAmount;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_paybill);
        myContext = this;

        MsgBox = new MessageDialog(myContext);

        try {
            InitializeViewVariables();
            Intent intentt = getIntent();
            strTotal = intentt.getStringExtra("TotalAmount");
            strCustId = intentt.getStringExtra("CustId");
            baseValue_recieved = intentt.getFloatExtra("BaseValue",0);
            otherCharges_recieved = intentt.getDoubleExtra("OtherCharges",0);
            taxType_recieved = intentt.getIntExtra("TaxType",1); // forward ->1, reverse -> 0
            orderList_recieved = intentt.getParcelableArrayListExtra("OrderList");
            strOrderDelivered = intentt.getBooleanExtra(ORDER_DELIVERED,false);
            if(strCustId==null)
                strCustId = "0";
            strUserName = intentt.getStringExtra("USER_NAME");
            phone = intentt.getStringExtra("phone");
            Log.v("Debug", "Total Amount:" + strTotal);
            dRoundoffTotal = Math.round(Double.valueOf(strTotal));

            edtTotalValue.setText(String.format( "%.2f", dRoundoffTotal ));
            edtRoundOff.setText("0" + strTotal.substring(strTotal.indexOf(".")));
            tvCustId.setText(strCustId);

            /*tvTitleUserName.setText(strUserName.toUpperCase());
            d = new Date();
            CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
            tvTitleDate.setText("Date : " + s);*/

            dbPayBill.CreateDatabase();
            dbPayBill.OpenDatabase();

            ResetPayBill();

        } catch (Exception ex) {
            MsgBox.Show("Error", ex.getMessage());
        }
        sharedPreferences = getSharedPreferences("appPreferences", Context.MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        edtCard.setText(sharedPreferences.getString("paidAmount","0"));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("paidAmount","0");
        editor.commit();

        TenderChange();
    }

    private void InitializeViewVariables() {
        tvCustId = (TextView) findViewById(R.id.tvCustId);
        tvCustId.setText("0");
        edtTotalValue = (EditText) findViewById(R.id.edtTotalValue);
        edtTotalValue.setEnabled(false);
        edtTotalValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtRoundOff = (EditText) findViewById(R.id.edtRoundOff);
        edtRoundOff.setEnabled(false);
        edtTenderTotalValue = (EditText) findViewById(R.id.edtTenderTotalValue);
        edtTenderTotalValue.setEnabled(false);
        edtPaid = (EditText) findViewById(R.id.edtPaid);
        edtPaid.addTextChangedListener(ChangeAmountEvent);
        edtCard = (EditText) findViewById(R.id.edtCard);
        edtCard.setEnabled(false);
        edtChange = (EditText) findViewById(R.id.edtChange);
        edtChange.setEnabled(false);
        edtPettyCash = (EditText) findViewById(R.id.edtPettycash);
        edtPettyCash.setEnabled(false);
        edtPettyCash.addTextChangedListener(ChangeAmountEvent);
        edtCard.addTextChangedListener(textChangeCard);
        edtDiscount = (EditText) findViewById(R.id.edtDisc);
        edtDiscount.addTextChangedListener(ChangeAmountEvent);
        edtDiscount.setEnabled(false);
        edtVoucher = (EditText) findViewById(R.id.edtVoucher);
        edtVoucher.setEnabled(false);
        edtCoupon = (EditText) findViewById(R.id.edtCoupon);
        edtCoupon.setEnabled(false);
        edtCoupon.addTextChangedListener(ChangeAmountEvent);

       /* btnCreditCustomer = (Button) findViewById(R.id.btnCreditCustomer);
        btnCardPayment = (Button) findViewById(R.id.btnCard);
        btnDiscount = (Button) findViewById(R.id.btnDiscount);
        //btnVoucher = (Button) findViewById(R.id.btnVoucher);
        btnCoupon = (Button) findViewById(R.id.btnCoupon);*/
        btnSaveBill = (WepButton) findViewById(R.id.btnSaveBill);
        btnPrintBill = (WepButton) findViewById(R.id.btnPrintBill);
    }

    TextWatcher ChangeAmountEvent = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            TenderChange();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    };

    TextWatcher textChangeCard = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            TenderChange();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //double amount = Double.parseDouble(s.toString());
        }
    };

    private void RecalculateBillAmount() {
        double dTotalValue = 0.00, dPaidTotal = 0.00;
        double dTenderAmount = 0.00, dChangeAmount = 0.00, dDiscAmt = 0.00;
        totalcessAmount=totalIGSTAmount=totalCGSTAmount = totalSGSTAmount =totalBillAmount=0;

        if(discPercent>0){
            for(AddedItemsToOrderTableClass item : orderList_recieved)
            {
                double rate= item.getRate();
                double quantity = item.getQuantity();
                double igstRate = item.getIgstRate();
                double cgstRate = item.getCgstRate();
                double sgstRate = item.getSgstRate();
                double cessRate = item.getCessRate();
                if(taxType_recieved ==1 ) // forward
                {
                    double discountamount = quantity*rate*discPercent/100 ;
                    double discountedrate_item = rate*(1-(discPercent/100));
                    double discountedrate = discountedrate_item*quantity;
                    double igstAmt_new = discountedrate*igstRate/100;
                    double cgstAmt_new = discountedrate*cgstRate/100;
                    double sgstAmt_new = discountedrate*sgstRate/100;
                    double cessAmt_new = discountedrate*cessRate/100;
                    totalIGSTAmount += igstAmt_new;
                    totalCGSTAmount += cgstAmt_new;
                    totalSGSTAmount += sgstAmt_new;
                    totalcessAmount+= cessAmt_new;

                    dTotalValue += discountedrate+igstAmt_new + cgstAmt_new +sgstAmt_new+cessAmt_new;
                    item.setSubtotal(discountedrate+igstAmt_new + cgstAmt_new +sgstAmt_new+cessAmt_new);
                    item.setIgstAmt(igstAmt_new);
                    item.setCgstAmt(cgstAmt_new);
                    item.setSgstAmt(sgstAmt_new);
                    item.setCessAmt(cessAmt_new);
                    item.setTaxableValue(discountedrate_item);
                    item.setDiscountamount(discountamount);
                    //item.setTaxableValue(discountedrate);
                }
                /*else *//*if(taxType_recieved ==0 )*//*// reverse
                {
                    double discountedrate_item = rate*(1-(discPercent/100))/(1+ (igstRate/100)+(cgstRate/100)+(sgstRate/100));
                    //double discountedrate_item = basePrice*(1-(discPercent/100));
                    double discountedrate = discountedrate_item*quantity;
                    double igstAmt_new = discountedrate*igstRate/100;
                    double cgstAmt_new = discountedrate*cgstRate/100;
                    double sgstAmt_new = discountedrate*sgstRate/100;
                    totalCGSTAmount += cgstAmt_new;
                    totalSGSTAmount += sgstAmt_new;

                    dTotalValue += discountedrate+igstAmt_new + cgstAmt_new +sgstAmt_new;
                    item.setSubtotal(discountedrate+igstAmt_new + cgstAmt_new +sgstAmt_new);
                    item.setIgstAmt(igstAmt_new);
                    item.setCgstAmt(cgstAmt_new);
                    item.setSgstAmt(sgstAmt_new);
                    //item.setTaxableValue(discountedrate);
                }*/
            }  // end of for
            dTotalValue+= otherCharges_recieved;
            totalBillAmount = dTotalValue;
            dRoundoffTotal = Math.round(dTotalValue);
            String str = String.format("%.2f",dTotalValue);
            edtTotalValue.setText(String.format( "%.2f", dRoundoffTotal ));
            edtRoundOff.setText("0" + str.substring(str.indexOf(".")));

        }
        else
        { // disc in RS
            double discAmt  =0;
            String disAmount_str = edtDiscount.getText().toString();
            if(disAmount_str.equals(""))
                edtDiscount.setText("0");
            else
            {
                for(AddedItemsToOrderTableClass item : orderList_recieved) {

                    totalIGSTAmount += item.getIgstAmt();
                    totalCGSTAmount += item.getCgstAmt();
                    totalSGSTAmount += item.getSgstAmt();
                    totalcessAmount += item.getCessAmt();

                }
                discAmt = Double.parseDouble(disAmount_str);
                totalBillAmount = Double.parseDouble(strTotal);
                totalBillAmount -= discAmt;
                dRoundoffTotal = Math.round(totalBillAmount);
                String str = String.format("%.2f",totalBillAmount);
                edtTotalValue.setText(String.format( "%.2f", dRoundoffTotal ));
                edtRoundOff.setText("0" + str.substring(str.indexOf(".")));

            }
        }

    }

    private  void restoretoPreviousValue()
    {
        double dTotalValue = 0.00;
        totalcessAmount= totalIGSTAmount=totalCGSTAmount = totalSGSTAmount =totalBillAmount=0;
        for(AddedItemsToOrderTableClass item : orderList_recieved) {
            double rate = item.getRate();
            double quantity = item.getQuantity();
            double igstRate = item.getIgstRate();
            double cgstRate = item.getCgstRate();
            double sgstRate = item.getSgstRate();
            double cessRate = item.getSgstRate();
            if (taxType_recieved == 1) // forward
            {

                double igstAmt_new = rate*quantity * igstRate / 100;
                double cgstAmt_new = rate*quantity * cgstRate / 100;
                double sgstAmt_new = rate*quantity * sgstRate / 100;
                double cessAmt_new = rate*quantity * cessRate / 100;
                totalIGSTAmount += igstAmt_new;
                totalCGSTAmount += cgstAmt_new;
                totalSGSTAmount += sgstAmt_new;
                totalcessAmount += cessAmt_new;

                dTotalValue += rate*quantity + igstAmt_new + cgstAmt_new + sgstAmt_new+cessAmt_new;
                item.setSubtotal(rate*quantity + igstAmt_new + cgstAmt_new + sgstAmt_new+cessAmt_new);
                item.setIgstAmt(igstAmt_new);
                item.setCgstAmt(cgstAmt_new);
                item.setSgstAmt(sgstAmt_new);
                item.setCessAmt(cessAmt_new);
                item.setTaxableValue(rate*quantity);

            }
        }
    }

    private void TenderChange() {
        double dTotalValue = 0.00, dPaidTotal = 0.00;
        double dTenderAmount = 0.00, dChangeAmount = 0.00;
        double dCash = 0.00, dCard = 0.00, dCoupon = 0.00;
        double dDisc = 0, dDiscAmt = 0, dVouvher = 0, dPettyCash = 0, dWallet =0;

        dCash = edtPaid.getText().toString().equalsIgnoreCase("") ? 0.00 : Double.parseDouble(edtPaid.getText().toString());
        dCard = edtCard.getText().toString().equalsIgnoreCase("") ? 0.00 : Double.parseDouble(edtCard.getText().toString());
        dCoupon = edtCoupon.getText().toString().equalsIgnoreCase("") ? 0.00 : Double.parseDouble(edtCoupon.getText().toString());
        //dDiscAmt = edtDiscount.getText().toString().equalsIgnoreCase("") ? 0.00 : Double.parseDouble(edtDiscount.getText().toString());
        dPettyCash = edtPettyCash.getText().toString().equalsIgnoreCase("") ? 0.00 : Double.parseDouble(edtPettyCash.getText().toString());
        dWallet = edtVoucher.getText().toString().equalsIgnoreCase("") ? 0.00 : Double.parseDouble(edtVoucher.getText().toString());
        dTotalValue = edtTotalValue.getText().toString().equalsIgnoreCase("") ? 0.00 : Double.parseDouble(edtTotalValue.getText().toString());

        dTenderAmount = (dCash + dCard + dCoupon + dDiscAmt + dPettyCash+dWallet);

        if(dTenderAmount > dTotalValue) {
            dChangeAmount = dTenderAmount - dTotalValue;
        }
        else
        {
            dChangeAmount = dTotalValue - dTenderAmount;
        }
        String strChange = String.format("%.2f", dChangeAmount);
        edtTenderTotalValue.setText(String.format("%.2f", dTenderAmount));
        edtChange.setText(strChange);
        if (dTenderAmount < dTotalValue) {
            edtTenderTotalValue.setTextColor(Color.RED);
        } else {
            edtTenderTotalValue.setTextColor(Color.GREEN);
        }

        /*if (dDiscAmt > 0)
        {
            if (dTenderAmount <dTotalValue) {
                edtTenderTotalValue.setTextColor(Color.RED);
            } else {
                edtTenderTotalValue.setTextColor(Color.GREEN);
            }
        }
        else
        {
            if (dTenderAmount < dTotalValue) {
                edtTenderTotalValue.setTextColor(Color.RED);
            } else {
                edtTenderTotalValue.setTextColor(Color.GREEN);
            }
        }
*/


        if(dTenderAmount == dTotalValue) {
            Toast.makeText(myContext, "No Due is : " + strChange + ", Please Save the Bill.", Toast.LENGTH_SHORT).show();
        }
        else if(dTenderAmount > dTotalValue) {
            Toast.makeText(myContext, "Change Due is : " + strChange + ", Please Give.", Toast.LENGTH_SHORT).show();
        }
        else if(dTenderAmount < dTotalValue && RESETCALLED==0) {
            Toast.makeText(myContext, "Amount Due is : " + String.valueOf(dChangeAmount) + ", Please Collect.", Toast.LENGTH_SHORT).show();
        }
    }

    private void ResetPayBill() {
        RESETCALLED =1;
        edtPaid.setText("0");
        edtCard.setText("0");
        edtCoupon.setText("0");
        edtPettyCash.setText("0");
        edtVoucher.setText("0");
        edtDiscount.setText("0");
        edtChange.setText("0");
        RESETCALLED = 0;
        discPercent =0;
    }

    public void CardPayment(View view) {
        /*Intent intentDineIn = new Intent(myContext, PayBillActivity.class);
        intentDineIn.putExtra("amount", edtTotalValue.getText().toString());
        startActivity(intentDineIn);*/
        makePayment_Card();
    }

    public void makePayment_Card() {
        txt = edtTotalValue.getText().toString().trim();
        txt = getCardPaybles()+"";
        if (txt.equalsIgnoreCase(""))
        {
            Toast.makeText(PayBillActivity.this, "Enter Amount", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (Constants.Reference_Id.length() != 0 && Constants.Session_Tokeniser.length() != 0) {
                Intent intent = new Intent(getApplicationContext(), MSwipePaymentActivity.class);
                intent.putExtra("amount", txt);
                intent.putExtra("phone", phone);
                startActivityForResult(intent,REQUEST_CODE_CARD_PAYMENT);
                //finish();
            } else {
                //Constants.showDialog(MenuView.this, "SDk List", "Please login first to perform the card sale.", 1);
                if (!(sharedPreferences.getString("userId", "").equalsIgnoreCase(""))) {
                    // Do Auto Authentication
                    validate(sharedPreferences.getString("userId", ""), sharedPreferences.getString("userPass", ""));

                } else {
                    promptLoginFragment();
                }
            }
        }
    }

    public String getCardPaybles(){
        double val = 0;
        try{
            val = Double.parseDouble(edtChange.getText().toString().trim());
        }catch (Exception e){

        }
        if(val>0)
            return edtChange.getText().toString().trim();
        else
            return 0+"";
    }

    public void SaveBill(View view)
    {
        if(Float.parseFloat(edtTenderTotalValue.getText().toString()) >= Float.parseFloat(edtTotalValue.getText().toString()))
        {
            dbPayBill.CloseDatabase();
            setResult(RESULT_OK, getSaveIntent(false));
            this.finish();
        }
        else
        {
            MsgBox.Show("Warning", "Check Bill Amount is Paid or not.\nPaid Total is less than Total Amount");
        }
    }

    public void PrintBill(View view)
    {
        if(Float.parseFloat(edtTenderTotalValue.getText().toString()) >= Float.parseFloat(edtTotalValue.getText().toString()))
        {
            dbPayBill.CloseDatabase();
            setResult(RESULT_OK, getSaveIntent(true));
            this.finish();
        }
        else
        {
            MsgBox.Show("Warning", "Check Bill Amount is Paid or not.\nPaid Total is less than Total Amount");
        }
    }

    public Intent getSaveIntent(boolean isPrint){
        Intent intentResult = new Intent();
        intentResult.putExtra(IS_COMPLIMENTARY_BILL, false);
        intentResult.putExtra(COMPLIMENTARY_REASON, "");
        if (Float.parseFloat(edtDiscount.getText().toString()) > 0)
        {
            intentResult.putExtra(IS_DISCOUNTED, true);
        }
        else
        {
            intentResult.putExtra(IS_DISCOUNTED, false);
        }
        intentResult.putExtra(IS_PRINT_BILL, isPrint);
        intentResult.putExtra(DISCOUNT_AMOUNT, Float.parseFloat(edtDiscount.getText().toString()));
        intentResult.putExtra("DISCOUNT_PERCENTAGE",discPercent);
        intentResult.putExtra(TENDER_CASH_VALUE, Float.parseFloat(edtPaid.getText().toString()));
        intentResult.putExtra(TENDER_CARD_VALUE, Float.parseFloat(edtCard.getText().toString()));
        intentResult.putExtra(TENDER_COUPON_VALUE, Float.parseFloat(edtCoupon.getText().toString()));
        intentResult.putExtra(TENDER_PETTYCASH_VALUE, (float) getPettyCash());
        intentResult.putExtra(TENDER_PAIDTOTAL_VALUE, Float.parseFloat(edtTenderTotalValue.getText().toString()));
        intentResult.putExtra(TENDER_CHANGE_VALUE, Float.parseFloat(edtChange.getText().toString()));
        intentResult.putExtra(TENDER_WALLET_VALUE, dWalletPayment);
        intentResult.putExtra(ORDER_DELIVERED, strOrderDelivered);
        intentResult.putExtra("CUST_ID", Integer.parseInt(tvCustId.getText().toString()));
        intentResult.putParcelableArrayListExtra("OrderList", orderList_recieved);
        intentResult.putExtra("TotalBillAmount", totalBillAmount);
        intentResult.putExtra("TotalIGSTAmount", totalIGSTAmount);
        intentResult.putExtra("TotalCGSTAmount", totalCGSTAmount);
        intentResult.putExtra("TotalSGSTAmount", totalSGSTAmount);
        intentResult.putExtra("TotalcessAmount", totalcessAmount);
        return intentResult;
    }

    private double getPettyCash() {
        double totalValue = Double.parseDouble(edtTotalValue.getText().toString());
        double petty = Double.parseDouble(edtPettyCash.getText().toString());
        double effectiveVal = toPayAmount-petty;
        if(effectiveVal >= 0)
        {
            return Double.parseDouble(edtPettyCash.getText().toString());
        }
        else
        {
            return toPayAmount-effectiveVal;
        }
    }

    public void Discount(View view) {

        Cursor crsrBillSetting = dbPayBill.getBillSetting();
        if(crsrBillSetting!=null && crsrBillSetting.moveToNext())
        {
            if(crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("Tax")) != 1) // reverse
            {
                MsgBox.Show("Invalid","Discount is not applicable in reverse tax.");
                return;
            }
            if(crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("DiscountType")) == 1) // Itemwise Discount
            {
                MsgBox.Show("Invalid","Discount not allowed here as itemwise discount is enabled");
                return;
            }
        }


       // edtDiscount.setEnabled(true);
        // custom dialog
        PayBillDialog = new Dialog(myContext);
        PayBillDialog.setContentView(R.layout.paybill_tablelist);
        PayBillDialog.setTitle("Discount");


        final RadioGroup rgDiscPerAmt = (RadioGroup) PayBillDialog.findViewById(R.id.rgDiscPerAmt);
        final RadioButton rbDiscPercent = (RadioButton) PayBillDialog.findViewById(R.id.rbDiscPercent);
        final RadioButton rbDiscAmount = (RadioButton) PayBillDialog.findViewById(R.id.rbDiscAmount);
        rgDiscPerAmt.setVisibility(View.VISIBLE);

        // set the custom dialog components - text, image and button
        tblPayBill = (TableLayout) PayBillDialog.findViewById(R.id.tblPayBill);
        TextView tvCaptionPaybillValue = (TextView) PayBillDialog.findViewById(R.id.tvCaptionPaybillValue);
        TextView tvCaptionPaybillAmount = (TextView) PayBillDialog.findViewById(R.id.tvCaptionPaybillAmount);
        tvCaptionPaybillValue.setText("Percent");
        tvCaptionPaybillAmount.setText("Amount");

        Cursor crsrDiscount = dbPayBill.getPayBillDiscount();

        if (crsrDiscount.moveToFirst()) {
            int i = 1;
            TableRow rowPayBill = null;
            TextView tvSno, tvName, tvPercent, tvAmount;

            if (crsrDiscount.moveToFirst()) {
                do {
                    rowPayBill = new TableRow(myContext);
                    rowPayBill.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    rowPayBill.setBackgroundResource(R.drawable.row_background);

                    tvSno = new TextView(myContext);
                    tvSno.setHeight(40);
                    tvSno.setTextSize(20);
                    tvSno.setPadding(3,0,0,0);
                    tvSno.setTextColor(Color.parseColor("#000000"));
                    tvSno.setText(String.valueOf(i));
                    i++;
                    rowPayBill.addView(tvSno);

                    tvName = new TextView(myContext);
                    tvName.setHeight(40);
                    tvName.setTextSize(20);
                    tvName.setTextColor(Color.parseColor("#000000"));
                    tvName.setText(crsrDiscount.getString(1));
                    rowPayBill.addView(tvName);

                    tvPercent = new TextView(myContext);
                    tvPercent.setHeight(40);
                    tvPercent.setTextSize(20);
                    tvPercent.setTextColor(Color.parseColor("#000000"));
                    tvPercent.setText(crsrDiscount.getString(2));
                    rowPayBill.addView(tvPercent);

                    tvAmount = new TextView(myContext);
                    tvAmount.setHeight(40);
                    tvAmount.setTextSize(20);
                    tvAmount.setTextColor(Color.parseColor("#000000"));
                    tvAmount.setText(crsrDiscount.getString(3));
                    rowPayBill.addView(tvAmount);

                    rowPayBill.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            if (String.valueOf(v.getTag()) == "TAG") {

                                if(rbDiscPercent.isChecked() == true || rbDiscAmount.isChecked() == true) {
                                    TableRow Row = (TableRow) v;
                                    TextView DiscountPercent = (TextView) Row.getChildAt(2);
                                    TextView DiscountAmount = (TextView) Row.getChildAt(3);
                                    if (rbDiscPercent.isChecked() == true) {
                                        if(discPercent>0)
                                        {
                                            restoretoPreviousValue();
                                        }
                                        discPercent = (Float.parseFloat(DiscountPercent.getText().toString()));
                                        float disc_amt = baseValue_recieved *  (discPercent/ 100);
                                        edtDiscount.setText(String.format("%.2f",disc_amt));
//
                                    }
                                    if (rbDiscAmount.isChecked() == true) {
                                        if(discPercent>0)
                                        {
                                            restoretoPreviousValue();
                                        }
                                        double discAmt = Double.parseDouble(DiscountAmount.getText().toString());
                                        discPercent =0;
                                        if(discAmt <= dRoundoffTotal)
                                            edtDiscount.setText(String.format("%.2f",discAmt));
                                        else
                                        {
                                            edtDiscount.setText("0");
                                            MsgBox.Show("Invalid","Discount is not applicable as bill amount is less than "+discAmt);
                                            //Toast.makeText(myContext, "Discount is not applicable as bill amount is less than "+discAmt, Toast.LENGTH_LONG).show();
                                            PayBillDialog.dismiss();
                                            return;
                                        }
                                    }
                                    RecalculateBillAmount();
                                    TenderChange();
                                    PayBillDialog.dismiss();
                                } else {
                                    MsgBox.Show("Warning", "Please select Percent / Amount");
                                }
                            }
                        }
                    });
                    rowPayBill.setTag("TAG");

                    tblPayBill.addView(rowPayBill, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                } while (crsrDiscount.moveToNext());
            } else {
                Log.d("DisplayCoupon", "No Coupon found");
            }
        }
        PayBillDialog.show();
    }

    public void Vouchers(View view) {
        edtVoucher.setEnabled(true);
        // custom dialog
        PayBillDialog = new Dialog(myContext);
        PayBillDialog.setContentView(R.layout.paybill_tablelist);
        PayBillDialog.setTitle("Voucher");

        // set the custom dialog components - text, image and button
        tblPayBill = (TableLayout) PayBillDialog.findViewById(R.id.tblPayBill);
        Cursor crsrVoucher = dbPayBill.getPayBillVoucher();
        if (crsrVoucher.moveToFirst()) {
            int i = 1;
            TableRow rowPayBill = null;
            TextView tvSno, tvName, tvValue;

            if (crsrVoucher.moveToFirst()) {
                do {
                    rowPayBill = new TableRow(myContext);
                    rowPayBill.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    rowPayBill.setBackgroundResource(R.drawable.row_background);

                    tvSno = new TextView(myContext);
                    tvSno.setHeight(40);
                    tvSno.setTextSize(20);
                    tvSno.setGravity(1);
                    tvSno.setTextColor(Color.parseColor("#000000"));
                    tvSno.setText(String.valueOf(i));
                    rowPayBill.addView(tvSno);

                    tvName = new TextView(myContext);
                    tvName.setHeight(40);
                    tvName.setTextSize(20);
                    tvName.setTextColor(Color.parseColor("#000000"));
                    tvName.setText(crsrVoucher.getString(1));
                    rowPayBill.addView(tvName);

                    tvValue = new TextView(myContext);
                    tvValue.setHeight(40);
                    tvValue.setTextSize(20);
                    tvValue.setTextColor(Color.parseColor("#000000"));
                    tvValue.setText(crsrVoucher.getString(2));
                    rowPayBill.addView(tvValue);

                    rowPayBill.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            if (String.valueOf(v.getTag()) == "TAG") {
                                TableRow Row = (TableRow) v;
                                TextView VoucherValue = (TextView) Row.getChildAt(2);
                                edtVoucher.setText(VoucherValue.getText().toString());
                                PayBillDialog.dismiss();
                            }
                        }
                    });
                    rowPayBill.setTag("TAG");

                    tblPayBill.addView(rowPayBill, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                } while (crsrVoucher.moveToNext());
            } else {
                Log.d("DisplayCoupon", "No Coupon found");
            }
        }
        PayBillDialog.show();
    }

    public void Coupons(View view) {
        edtCoupon.setEnabled(true);
        // custom dialog
        PayBillDialog = new Dialog(myContext);
        PayBillDialog.setContentView(R.layout.paybill_tablelist);
        PayBillDialog.setTitle("Coupon");

        // set the custom dialog components - text, image and button
        tblPayBill = (TableLayout) PayBillDialog.findViewById(R.id.tblPayBill);
        TextView tvCaptionPaybillAmount = (TextView) PayBillDialog.findViewById(R.id.tvCaptionPaybillAmount);
        tvCaptionPaybillAmount.setText("Qty");
        Cursor crsrCoupon = dbPayBill.getPayBillCoupon();
        if (crsrCoupon.moveToFirst()) {
            int i = 1;
            TableRow rowPayBill = null;
            TextView tvSno, tvName, tvValue;
            EditText txtCouponQty;

            if (crsrCoupon.moveToFirst()) {
                do {
                    rowPayBill = new TableRow(myContext);
                    rowPayBill.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    rowPayBill.setBackgroundResource(R.drawable.row_background);

                    tvSno = new TextView(myContext);
                    tvSno.setHeight(40);
                    tvSno.setTextSize(20);
                    tvSno.setGravity(1);
                    tvSno.setTextColor(Color.parseColor("#000000"));
                    tvSno.setText(String.valueOf(i));
                    rowPayBill.addView(tvSno);

                    tvName = new TextView(myContext);
                    tvName.setHeight(40);
                    tvName.setTextSize(20);
                    tvName.setTextColor(Color.parseColor("#000000"));
                    tvName.setText(crsrCoupon.getString(1));
                    rowPayBill.addView(tvName);

                    tvValue = new TextView(myContext);
                    tvValue.setHeight(40);
                    tvValue.setTextSize(20);
                    tvValue.setTextColor(Color.parseColor("#000000"));
                    tvValue.setText(crsrCoupon.getString(2));
                    rowPayBill.addView(tvValue);

                    txtCouponQty = new EditText(myContext);
                    txtCouponQty.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    txtCouponQty.setText("0");
                    txtCouponQty.setWidth(80);
                    txtCouponQty.setSelectAllOnFocus(true);
                    txtCouponQty.setTextColor(Color.parseColor("#000000"));
                    txtCouponQty.setOnKeyListener(CouponQtyKeyPressEvent);
                    txtCouponQty.addTextChangedListener(CouponQtyChangeEvent);
                    rowPayBill.addView(txtCouponQty);

                    rowPayBill.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            if (String.valueOf(v.getTag()) == "TAG") {
                                TableRow Row = (TableRow) v;
                                TextView CouponValue = (TextView) Row.getChildAt(2);
                                edtCoupon.setText(CouponValue.getText().toString());
                                TenderChange();
                                PayBillDialog.dismiss();
                            }
                        }
                    });
                    rowPayBill.setTag("TAG");

                    tblPayBill.addView(rowPayBill, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                } while (crsrCoupon.moveToNext());
            } else {
                Log.d("DisplayCoupon", "No Coupon found");
            }
        }
        PayBillDialog.show();
    }

    View.OnKeyListener CouponQtyKeyPressEvent = new View.OnKeyListener() {

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // TODO Auto-generated method stub

            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(((EditText) v).getWindowToken(), 0);

                //CouponAmount();

                return true;
            }
            else{
                return false;
            }
        }
    };

    TextWatcher CouponQtyChangeEvent = new TextWatcher(){

        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

            CouponAmount();
            TenderChange();
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub

        }

    };

    private void CouponAmount(){
        double dCouponAmount = 0, dAmount = 0;
        int iQty = 0;

        for(int i=1;i<tblPayBill.getChildCount();i++){

            TableRow row = (TableRow)tblPayBill.getChildAt(i);
            //Log.d("Coupon Amount", "Row Id:" + i + "Views inside rows:" + row.getChildCount());

            if(row.getChildAt(2) != null){
                TextView Amount = (TextView)row.getChildAt(2);
                dAmount = Amount.getText().toString().equalsIgnoreCase("") ? 0 : Double.parseDouble(Amount.getText().toString());
            }

            if(row.getChildAt(3)!=null){
                EditText Quantity = (EditText)row.getChildAt(3);
                iQty = Quantity.getText().toString().equalsIgnoreCase("") ? 0 : Integer.parseInt(Quantity.getText().toString());
            }

            dCouponAmount = dCouponAmount + (dAmount * iQty);

        }

        edtCoupon.setText(String.format("%.2f",dCouponAmount));
    }

    public void showCreditCustomerDialog()
    {
        String btnTxtOkay = "Pay";
        String btnTxtCancel = "Cancel";
        final String btnTxtNutral = "Credit & Pay";

        LayoutInflater li = LayoutInflater.from(PayBillActivity.this);
        View promptsView = li.inflate(R.layout.pay_bill_dialog, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PayBillActivity.this);
        alertDialogBuilder.setView(promptsView);
        final EditText editTextMobile = (EditText) promptsView.findViewById(R.id.editTextMobile);
        final EditText editTextName = (EditText) promptsView.findViewById(R.id.editTextName);
        final EditText editTextBalance = (EditText) promptsView.findViewById(R.id.editTextBalance);

        final TextView textViewBalanceUpdate = (TextView) promptsView.findViewById(R.id.textViewBalanceUpdate);
        final TextView textViewMessage = (TextView) promptsView.findViewById(R.id.textViewMessage);

        final Button btnCancel = (Button) promptsView.findViewById(R.id.btnCancel);
        btnCancel.setText(btnTxtCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AlertDialog) alertDialog).cancel();
            }
        });
        final Button btnNutral = (Button) promptsView.findViewById(R.id.btnNutral);
        btnNutral.setText(btnTxtNutral);
        btnNutral.setVisibility(View.INVISIBLE);
        btnNutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AlertDialog) alertDialog).cancel();
                /*float fCreditAmount = dbPayBill.getCustomerCreditAmount(Integer.parseInt(strCustId));
                if(fCreditAmount <= 0)
                {
                    edtPettyCash.setText(getEffectivePaybleAmountForCreditCustomerNegative()+"");
                }
                else
                {
                    edtPettyCash.setText(getEffectivePaybleAmountForCreditCustomer()+"");
                }*/

                edtPettyCash.setText(getEffectivePaybleAmountForCreditCustomer()+"");
            }
        });
        final Button btnOk = (Button) promptsView.findViewById(R.id.btnOk);
        btnOk.setText(btnTxtOkay);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double newBlnc = getUpdatedAmountForCreditCustomer(toPayAmount,getEffectivePaybleAmountForCreditCustomer());
                if(btnNutral.getVisibility() == View.VISIBLE)
                {
                    //Exceed Payble Amount
                    //edtPettyCash.setText(toPayAmount+"");
                    float fCreditAmount = dbPayBill.getCustomerCreditAmount(Integer.parseInt(tvCustId.getText().toString()));
                    if(fCreditAmount < 0)
                    {
                        edtPettyCash.setText(0+"");
                    }
                    else
                    {
                        edtPettyCash.setText(toPayAmount+"");
                    }
                }
                else
                {
                    edtPettyCash.setText(getEffectivePaybleAmountForCreditCustomer()+"");
                }
                ((AlertDialog) alertDialog).cancel();
            }
        });

        editTextMobile.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                try {
                    if (editTextMobile.getText().toString().length() == 10)
                    {
                        Cursor crsrCust = dbPayBill.getPayBillCustomerByMobileNo(editTextMobile.getText().toString());
                        if (crsrCust.moveToFirst())
                        {
                            try{
                                String custId = crsrCust.getString(crsrCust.getColumnIndex("CustId"));
                                tvCustId.setText(custId+"");
                                Cursor cursor = dbPayBill.getPayBillCustomer(custId);
                                cursor.moveToFirst();
                                String custName = cursor.getString(cursor.getColumnIndex("CustName"));
                                double custBalance = cursor.getDouble(cursor.getColumnIndex("CreditAmount"));
                                toPayAmount = custBalance;
                                editTextName.setText(custName);
                                editTextBalance.setText(custBalance+"");
                                //Balance After Payment: 100-46=54
                                double newBlnc = getUpdatedAmountForCreditCustomer(custBalance,getEffectivePaybleAmountForCreditCustomer());
                                if(newBlnc >= 0)
                                {
                                    textViewBalanceUpdate.setText("Balance After Payment: "+custBalance+"-"+getEffectivePaybleAmountForCreditCustomer()+"="+newBlnc);
                                    textViewBalanceUpdate.setVisibility(View.VISIBLE);
                                    btnNutral.setVisibility(View.INVISIBLE);
                                    btnOk.setText("Pay");
                                }
                                else if(newBlnc < 0)
                                {
                                    if(custBalance != 0)
                                    {
                                        textViewBalanceUpdate.setText("");
                                        textViewMessage.setVisibility(View.VISIBLE);
                                        textViewMessage.setText(getString(R.string.text_message_payment_zero));
                                        textViewBalanceUpdate.setVisibility(View.VISIBLE);
                                        btnNutral.setVisibility(View.VISIBLE);
                                        btnNutral.setText("Credit & Pay");
                                        btnOk.setText("Partial Payment");
                                    }
                                    else
                                    {
                                        textViewBalanceUpdate.setText("");
                                        textViewMessage.setVisibility(View.VISIBLE);
                                        textViewMessage.setText(getString(R.string.text_message_payment_zero_negative));
                                        textViewBalanceUpdate.setVisibility(View.VISIBLE);
                                        btnNutral.setVisibility(View.VISIBLE);
                                        btnNutral.setText("Credit & Pay");
                                        btnOk.setText("Others");
                                    }
                                }
                                else
                                {
                                    textViewBalanceUpdate.setText("");
                                    textViewMessage.setVisibility(View.VISIBLE);
                                    textViewBalanceUpdate.setVisibility(View.VISIBLE);
                                    btnNutral.setVisibility(View.VISIBLE);
                                    btnNutral.setText("Credit & Pay");
                                    btnOk.setText("Pay Partialy");
                                    textViewMessage.setText(getString(R.string.text_message_payment_zero_negative));
                                }

                            }catch (Exception e){
                                MsgBox.Show("", e+"");
                            }
                        }
                        else
                        {
                            MsgBox.Show("", "Customer is not Found, Please Add Customer for Petty Cash");
                        }
                    }
                    else
                    {
                        editTextName.setText("");
                        editTextBalance.setText("");
                        textViewBalanceUpdate.setVisibility(View.INVISIBLE);
                        textViewMessage.setVisibility(View.INVISIBLE);
                        btnNutral.setVisibility(View.INVISIBLE);
                        btnOk.setText("Pay");
                        /*btnNutral.setText("Credit & Pay");
                        btnOk.setText("Pay Partialy");*/
                    }
                } catch (Exception ex) {
                    editTextName.setText("");
                    editTextBalance.setText("");
                    textViewBalanceUpdate.setVisibility(View.INVISIBLE);
                    textViewMessage.setVisibility(View.INVISIBLE);
                    btnNutral.setVisibility(View.INVISIBLE);
                    MsgBox.Show("Error", ex.getMessage());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        editTextMobile.setText(phone);
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle("Credit Customer");
        alertDialog.show();

    }

    public double getEffectivePaybleAmountForCreditCustomer(){
        double amount = 0.0;
        try{
            double paybleAmount = Double.parseDouble(edtTotalValue.getText().toString());
            double paidAmount = Double.parseDouble(edtTenderTotalValue.getText().toString());
            amount = paybleAmount - paidAmount;
        }catch (Exception e){

        }
        return amount;
    }

    public double getEffectivePaybleAmountForCreditCustomerNegative(){
        double amount = 0.0;
        try{
            double paybleAmount = Double.parseDouble(edtTotalValue.getText().toString());
            double paidAmount = Double.parseDouble(edtTenderTotalValue.getText().toString());
            amount = paybleAmount - paidAmount;
        }catch (Exception e){

        }
        return amount;
    }

    public double getUpdatedAmountForCreditCustomer(double amt1,double amt2){
        double amount = 0.0;
        try{
            amount = amt1 - amt2;
        }catch (Exception e){
            amount = 0.0;
        }
        return amount;
    }

    public void CreditCustomer(View view) {
        edtPettyCash.setText("0");
        //edtPettyCash.setEnabled(true);
        // custom dialog
        //showOldPaybillDialog(view);
        showCreditCustomerDialog();
    }

    private void showOldPaybillDialog(final View view) {
        PayBillDialog = new Dialog(myContext);
        PayBillDialog.setContentView(R.layout.paybill_tablelist);
        PayBillDialog.setTitle("Credit Customer");

        // set the custom dialog components - text, image and button
        final EditText edtMobileNo = (EditText) PayBillDialog.findViewById(R.id.edtPayBillCustMobileNo);
        tblPayBill = (TableLayout) PayBillDialog.findViewById(R.id.tblPayBill);
        edtMobileNo.setVisibility(View.VISIBLE);
        edtMobileNo.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                try {
                    if (edtMobileNo.getText().toString().length() == 10) {
                        Cursor crsrCust = dbPayBill.getPayBillCustomerByMobileNo(edtMobileNo.getText().toString());
                        if (crsrCust.moveToFirst()) {
                            tvCustId.setText(crsrCust.getString(crsrCust.getColumnIndex("CustId")));
                            crsrCustomer = dbPayBill.getPayBillCustomer(tvCustId.getText().toString());
                            PayBillDialog.dismiss();
                            CreditCustomer(view);
                        } else {
                            MsgBox.Show("", "Customer is not Found, Please Add Customer for Petty Cash");
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

        String str1 = tvCustId.getText().toString();
        if(str1.equalsIgnoreCase("") || str1.equalsIgnoreCase("0"))
        {
            crsrCustomer = dbPayBill.getPayBillCustomer(tvCustId.getText().toString());
        }
        else
        {
            crsrCustomer = dbPayBill.getPayBillCustomer(tvCustId.getText().toString());
        }
        if (crsrCustomer.moveToFirst()) {
            int i = 1;
            TableRow rowPayBill = null;
            TextView tvSno, tvName, tvValue;

            if (crsrCustomer.moveToFirst()) {
                do {
                    rowPayBill = new TableRow(myContext);
                    rowPayBill.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    rowPayBill.setBackgroundResource(R.drawable.row_background);

                    tvSno = new TextView(myContext);
                    tvSno.setHeight(40);
                    tvSno.setTextSize(20);
                    tvSno.setGravity(1);
                    tvSno.setTextColor(Color.parseColor("#000000"));
                    tvSno.setText(crsrCustomer.getString(0));
                    rowPayBill.addView(tvSno);

                    tvName = new TextView(myContext);
                    tvName.setHeight(40);
                    tvName.setTextSize(20);
                    tvName.setTextColor(Color.parseColor("#000000"));
                    tvName.setText(crsrCustomer.getString(1));
                    rowPayBill.addView(tvName);

                    tvValue = new TextView(myContext);
                    tvValue.setHeight(40);
                    tvValue.setTextSize(20);
                    tvValue.setTextColor(Color.parseColor("#000000"));
                    tvValue.setText(crsrCustomer.getString(2));
                    rowPayBill.addView(tvValue);

                    rowPayBill.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            if (String.valueOf(v.getTag()) == "TAG") {
                                TableRow Row = (TableRow) v;
                                TextView CustomerId = (TextView) Row.getChildAt(0);
                                TextView CustomerValue = (TextView) Row.getChildAt(2);
                                tvCustId.setText(CustomerId.getText().toString());

                                double TotalAmt = Double.parseDouble(edtTotalValue.getText().toString());
                                double CustAmt = Double.parseDouble(CustomerValue.getText().toString());

                                if(TotalAmt > CustAmt)
                                {
                                    edtPettyCash.setText(CustomerValue.getText().toString());
                                }
                                else
                                {
                                    edtPettyCash.setEnabled(true);
                                    edtPettyCash.setText(edtTotalValue.getText().toString());
                                }

                                TenderChange();
                                PayBillDialog.dismiss();
                            }
                        }
                    });
                    rowPayBill.setTag("TAG");
                    tblPayBill.addView(rowPayBill, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                } while (crsrCustomer.moveToNext());
            } else {
                Log.d("CreditCustomer", "No Customer found");
            }
        }
        PayBillDialog.show();
    }

    public void cancelPayment(View view) {
        finish();
    }

    private void promptLoginFragment() {
        FragmentLogin alertdFragment = new FragmentLogin();
        alertdFragment.setCancelable(false);
        alertdFragment.show(getSupportFragmentManager(), "LoginDialog");
    }

    public void validate(String name, String password) {
        MswipeWisepadController wisepadController = new MswipeWisepadController(PayBillActivity.this, AppPrefrences.getGateWayEnv(), null);
        wisepadController.AuthenticateMerchant(loginHandler, name, password);
        progressDialog = new ProgressDialog(PayBillActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }

    public Handler loginHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            progressDialog.dismiss();
            Bundle bundle = msg.getData();
            String responseMsg = bundle.getString("response");
            Log.v(TAG, " the response xml is " + responseMsg);

            String[][] strTags = new String[][]{{"status", ""}, {"ErrMsg", ""}, {"IS_Password_Changed", ""}, {"First_Name", ""},
                    {"Reference_Id", ""}, {"Session_Tokeniser", ""}, {"Currency_Code", ""}, {"IS_TIP_REQUIRED", ""}, {"CONVENIENCE_PERCENTAGE", ""}, {"SERVICE_TAX", ""}};
            try {
                Constants.parseXml(responseMsg, strTags);

                String status = strTags[0][1];
                if (status.equalsIgnoreCase("false")) {
                    String ErrMsg = strTags[1][1];
                    Toast.makeText(PayBillActivity.this, ErrMsg, Toast.LENGTH_SHORT).show();
                    promptLoginFragment();

                } else if (status.equalsIgnoreCase("true")) {

                    String FirstName = strTags[3][1];
                    Log.v(TAG, " FirstName  " + FirstName);

                    String Reference_Id = strTags[4][1];
                    Log.v(TAG, " Reference_Id  " + Reference_Id);

                    String Session_Tokeniser = strTags[5][1];
                    Log.v(TAG, " Session_Tokeniser  " + Session_Tokeniser);

                    String Currency_Code = strTags[6][1];
                    Log.v(TAG, " Currency_Code  " + Currency_Code);

                    String tipRequired = strTags[7][1];
                    Log.v(TAG, " tipRequired  " + tipRequired);

                    String convienencePercentage = strTags[8][1];
                    Log.v(TAG, " convienencePercentage  " + convienencePercentage);

                    String serviceTax = strTags[9][1];
                    Log.v(TAG, " serviceTax  " + serviceTax);

                    //save the referebceId and Session_Tokeniser for passing to the other web servives
                    Constants.Reference_Id = Reference_Id;
                    Constants.Session_Tokeniser = Session_Tokeniser;
                    Constants.Currency_Code = Currency_Code + ".";
                    Constants.mTipRequired = tipRequired;
                    Constants.mConveniencePercentage = convienencePercentage == null ? "0" : convienencePercentage;
                    Constants.mServiceTax = serviceTax == null ? "0" : serviceTax;

                    String IS_Password_Changed = strTags[2][1];

                    if (IS_Password_Changed.equalsIgnoreCase("false")) {
                        changePassword();

                    } else {
                        // Login Succes Save in shared preferences and Proceed for Payment
                        /*SharedPreferences sharedPreferences = getSharedPreferences("appPreferences",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userId",editTextUserName.getText().toString().trim());
                        editor.putString("userPass",editTextUserPass.getText().toString().trim());
                        editor.apply();*/
                        loginCompleted();
                    }

                } else {
                    Toast.makeText(PayBillActivity.this, "Invalid response from Mswipe server, please contact support.", Toast.LENGTH_SHORT).show();

                }

            } catch (Exception ex) {
                Toast.makeText(PayBillActivity.this, "Invalid response from Mswipe server, please contact support.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void onLoginCompleted(boolean success) {
        loginCompleted();
    }

    private void loginCompleted() {
        Intent intent = new Intent(getApplicationContext(), MSwipePaymentActivity.class);
        intent.putExtra("amount", getCardPaybles()+"");
        intent.putExtra("phone", phone);
        startActivityForResult(intent,REQUEST_CODE_CARD_PAYMENT);
        //finish();
    }

    public void onChangePassword(boolean success) {
        changePassword();
    }

    private void changePassword() {
        Intent intent = new Intent(getApplicationContext(), PasswordChangeActivity.class);
        intent.putExtra("amount", txt);
        startActivity(intent);
    }

    public void Close(View view)
    {
        this.finish();
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
                            /*Intent returnIntent =new Intent();
                            setResult(Activity.RESULT_OK,returnIntent);*/
                            finish();
                        }
                    })
                    .show();
        }

        return super.onKeyDown(keyCode, event);
    }

    public void payByWallet(View view) {
        startPayment();
    }

    public void startPayment() {
        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Wep Solutions Ltd");
            options.put("description", "Resturant bill payment");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSVnUM4lZzEAgU62oQU9yjp_Z0i6KkrNzdrlZZT5LyfxZUIJpnL");
            options.put("currency", "INR");
            toPayAmount = getIntegers(edtChange.getText().toString().trim());
            options.put("amount", toPayAmount+"");
            JSONObject preFill = new JSONObject();
            preFill.put("email", "");
            preFill.put("contact", phone);
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private int getIntegers(String txt) {
        Integer i = 0;
        try{
            Double d = Double.parseDouble(txt);
            i = d.intValue()*100;
        }catch (Exception e){

        }
        return i;
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        /*Toast.makeText(getApplicationContext(), "Payment Success", Toast.LENGTH_SHORT).show();
        try {
            edtChange.setText(edtChange.getText().toString().trim());
            Payment payment = new Payment();
            payment.setPaymentType("Card");
            payment.setTotalAmount(toPayAmount+"");
            payment.setPaymentStatus("true");
            com.wep.common.app.Database.DatabaseHandler handler = new com.wep.common.app.Database.DatabaseHandler(this);
            handler.saveTransaction(payment);
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }*/


        Toast.makeText(getApplicationContext(), "Payment Success", Toast.LENGTH_SHORT).show();
        try {
            edtVoucher.setText(edtChange.getText().toString().trim());
            dWalletPayment = Float.parseFloat(edtVoucher.getText().toString());
            Payment payment = new Payment();
            payment.setPaymentType("Card");
            payment.setTotalAmount(toPayAmount+"");
            payment.setPaymentStatus("true");
            com.wep.common.app.Database.DatabaseHandler handler = new com.wep.common.app.Database.DatabaseHandler(this);
            handler.saveTransaction(payment);
            edtChange.setText("0");
        } catch (Exception e) {
            Log.e(TAG, "Problem in Payment", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(getApplicationContext(), "Payment Failed", Toast.LENGTH_SHORT).show();
        try {
            Payment payment = new Payment();
            //Toast.makeText(this, "Payment Successful: " /*+ razorpayPaymentID*/, Toast.LENGTH_SHORT).show();
            payment.setPaymentType("Card");
            payment.setTotalAmount(toPayAmount+"");
            payment.setPaymentStatus("false");
            com.wep.common.app.Database.DatabaseHandler handler = new com.wep.common.app.Database.DatabaseHandler(this);
            handler.saveTransaction(payment);
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_CARD_PAYMENT)
        {
            if(data!=null){
                final String amount = data.getStringExtra("amount");
                edtCard.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        edtCard.setText(amount);
                    }
                },500);
                //edtChange.setText("0");
                Toast.makeText(myContext, "Hello"+amount, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
