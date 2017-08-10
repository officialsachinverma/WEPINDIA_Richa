package com.mswipetech.wisepad.sdktest.view;

import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.mswipetech.wisepad.sdk.MswipeWisepadController;
import com.mswipetech.wisepad.R;
import com.mswipetech.wisepad.sdktest.data.AppPrefrences;


public class RefundTrxView extends BaseTitleActivity{

    public HashMap<String, String> setHashMember = new HashMap<String, String>();
    public final static String log_tab = "RefundTrx=>";
    CustomProgressDialog mProgressActivity = null;

    int mCurrentScreen = 0;
    TextView lblSummaryDate = null;
    //fields for card sale screen
    EditText mTxtCreditAmountDollars = null;
    EditText mTxtLast4Digits = null;
    String mLast4Digits = "";

    public String mSelectedDate = "";
    private static final String[] MONTHS = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    static final int DATE_DIALOG_ID = 999;
    private int year;
    private int month;
    private int day;

    //CreditSaleStanController creditSaleStanController = null;
    //GetVoidSaleDetailsController getVoidSaleDetailsController = null;
    //VoidSaleController voidSaleController = null;

    public String displayMsg = "";
    public String mStandId = "";
    public String mTrxDate = "";
    public String mVoucherno = "";

    ViewFlipper mViewFlipper = null;
    LinearLayout[] arrPageLinearObjects = null;
    ApplicationData applicationData = null;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voidtransaction);
        applicationData = (ApplicationData) getApplicationContext();

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        initViews();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mCurrentScreen == 0) {
                finish();

            } else {

                mViewFlipper.showPrevious();
                arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleyellow);
                mCurrentScreen--;
                arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleblack);

            }

            return true;

        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    private void initViews() {
        mViewFlipper = (ViewFlipper) findViewById(R.id.creditsale_VFL_content);

        TextView txtHeading = ((TextView) findViewById(R.id.topbar_LBL_heading));
        txtHeading.setText(Constants.REFUNDVOID_DIALOG_MSG);
        txtHeading.setTypeface(applicationData.font);


        arrPageLinearObjects = new LinearLayout[4];
        arrPageLinearObjects[0] = (LinearLayout) findViewById(R.id.creditsale_LNL_page1);
        arrPageLinearObjects[1] = (LinearLayout) findViewById(R.id.creditsale_LNL_page2);

//The screen are for the amount		
        lblSummaryDate = (TextView) findViewById(R.id.cardsale_LBL_date);
        lblSummaryDate.setTypeface(applicationData.fontbold);

        lblSummaryDate.setText(day + " " + MONTHS[month] + " " + year);
        mSelectedDate = year + "-" + (month + 1) + "-" + day;

        Button btnDate = (Button) findViewById(R.id.creditsale_BTN_date);
        btnDate.setTypeface(applicationData.font);
        btnDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        mTxtCreditAmountDollars = (EditText) findViewById(R.id.creditsale_TXT_amount);
        mTxtLast4Digits = (EditText) findViewById(R.id.creditsale_TXT_cardfourdigits);

        

        mTxtCreditAmountDollars.setTypeface(applicationData.font);
        mTxtLast4Digits.setTypeface(applicationData.font);


        mTxtCreditAmountDollars.setTag("1");
        mTxtCreditAmountDollars.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                if (mTxtCreditAmountDollars.isFocused() && mTxtCreditAmountDollars.getText().length() != 0) {
                                    mTxtCreditAmountDollars.setSelection(mTxtCreditAmountDollars.getText().length());
                                    InputMethodManager imm = (InputMethodManager)
                                            getSystemService(Context.INPUT_METHOD_SERVICE);
                                    if (imm != null) {
                                        imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
                                    }
                                    return true;
                                }
                return false;

            }
        });

        mTxtCreditAmountDollars.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (v.getTag().toString().equals("1")) {
                    mTxtCreditAmountDollars.setSelection(mTxtCreditAmountDollars.getText().length());
                }

            }
        });

        mTxtCreditAmountDollars.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                String oldString = mTxtCreditAmountDollars.getText().toString();
                String newString = removeChar(oldString, '.');
                int ilen = newString.length();
                if (ilen == 1 || ilen == 2) {
                    newString = "." + newString;
                } else if (ilen > 2) {
                    newString = newString.substring(0, ilen - 2) + "." + newString.substring(ilen - 2, ilen);
                }
                if (!newString.equals(oldString)) {
                    mTxtCreditAmountDollars.setText(newString);
                    mTxtCreditAmountDollars.setSelection(mTxtCreditAmountDollars.getText().length());

                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
               

            }
        });


        Button btnSubmit = (Button) findViewById(R.id.creditsale_BTN_submit);
        btnSubmit.setTypeface(applicationData.font);
        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(RefundTrxView.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);




                mLast4Digits = mTxtLast4Digits.getText().toString().trim();
                double miAmountDisplay = 0;
                try {
                    if (mTxtCreditAmountDollars.length() > 0)
                        miAmountDisplay = Double.parseDouble(mTxtCreditAmountDollars.getText().toString());


                } catch (Exception ex) {
                    miAmountDisplay = 0;
                }


                if (miAmountDisplay < 1) {
                    Constants.showDialog(RefundTrxView.this, Constants.REFUNDVOID_DIALOG_MSG, Constants.CARDSALE_ERROR_INVALIDAMT, 1);

                    return;
                } else if (mTxtLast4Digits.getText().toString().trim().length() < 4) {
                    Constants.showDialog(RefundTrxView.this, Constants.REFUNDVOID_DIALOG_MSG, Constants.CARDSALE_ERROR_INVALIDCARDDIGITS, 1);
                    mTxtLast4Digits.requestFocus();
                    return;
                } else {

                    String voidAmt = String.format(Constants.REFUNDVOID_ALERT_AMOUNTMSG, Constants.Currency_Code, (mTxtCreditAmountDollars.getText().toString() + ""),
                            mLast4Digits);
                    final Dialog dialog = Constants.showDialog(RefundTrxView.this, Constants.REFUNDVOID_DIALOG_MSG,
                            voidAmt, "2");
                    Button yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
                    yes.setTypeface(applicationData.font);
                    yes.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {

                            dialog.dismiss();
                            try {
                                if (applicationData.IS_DEBUGGING_ON)
                                    Logs.v(ApplicationData.packName, log_tab + "The base amout " + mTxtCreditAmountDollars.getText().toString() , false, true);

                                MswipeWisepadController wisepadController = new MswipeWisepadController(RefundTrxView.this, AppPrefrences.getGateWayEnv(),null);
                                wisepadController.Get_RefundTrx(GetVoidSaleHandler, Constants.Reference_Id, Constants.Session_Tokeniser,
                                		mSelectedDate, mTxtCreditAmountDollars.getText().toString(), mLast4Digits);
                                
                                mProgressActivity = new CustomProgressDialog(RefundTrxView.this, "Fetching Transaction Details");
                                mProgressActivity.show();


                            } catch (Exception ex) {
                                Constants.showDialog(RefundTrxView.this, Constants.REFUNDVOID_DIALOG_MSG, Constants.REFUNDVOID_ERROR_Processing_DATA, 1);


                            }

                        }
                    });

                    Button no = (Button) dialog.findViewById(R.id.bmessageDialogNo);
                    no.setTypeface(applicationData.font);
                    no.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            mTxtCreditAmountDollars.requestFocus();
                            dialog.dismiss();

                        }
                    });
                    dialog.show();


                }

            }
        });

//The scrren for Tx details

        ((TextView) findViewById(R.id.creditsale_LBL_lblTrxDateTime)).setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.creditsale_LBL_TrxDateTime)).setTypeface(applicationData.font);

        ((TextView) findViewById(R.id.creditsale_LBL_lblTrxCardNo)).setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.creditsale_LBL_TrxCardNo)).setTypeface(applicationData.font);

        ((TextView) findViewById(R.id.creditsale_LBL_lblTxAmt)).setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.creditsale_LBL_lblTxAmt)).setText("Amt ");

        ((TextView) findViewById(R.id.creditsale_LBL_AmtPrefix)).setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.creditsale_LBL_AmtPrefix)).setText(Constants.Currency_Code + " ");

        ((TextView) findViewById(R.id.creditsale_LBL_TxAmt)).setTypeface(applicationData.font);

        ((TextView) findViewById(R.id.creditsale_LBL_lblAuthCode)).setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.creditsale_LBL_AuthCode)).setTypeface(applicationData.font);

        ((TextView) findViewById(R.id.creditsale_LBL_lblRRno)).setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.creditsale_LBL_RRno)).setTypeface(applicationData.font);


        Button btnTxNext = (Button) findViewById(R.id.creditsale_BTN_tx_next);
        btnTxNext.setTypeface(applicationData.font);
        btnTxNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String voidAmt = String.format(Constants.REFUNDVOID_ALERT_FORVOID, mTrxDate, Constants.Currency_Code, (mTxtCreditAmountDollars.getText().toString() + ""),
                        mLast4Digits);
                final Dialog dialog = Constants.showDialog(RefundTrxView.this, Constants.REFUNDVOID_DIALOG_MSG, voidAmt, "2");
                Button yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
                yes.setTypeface(applicationData.font);
                yes.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        dialog.dismiss();

                        processCardSaleVoid_MCR_IC();


                    }
                });

                Button no = (Button) dialog.findViewById(R.id.bmessageDialogNo);
                no.setTypeface(applicationData.font);
                no.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                        mTxtCreditAmountDollars.requestFocus();

                    }
                });
                dialog.show();


            }
        });


    }

    public Handler GetVoidSaleHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            Bundle bundle = msg.getData();
            String responseMsg = bundle.getString("response");
            if (mProgressActivity != null)
                mProgressActivity.dismiss();
            
            Log.v(ApplicationData.packName, log_tab + " the response xml is " + responseMsg);

                
              //<t1> TrxDate</t1><t2> CardLastFourDigits </t2> <t3> TrxAmount</t3> <t4> TrxNotes </t4> <t5> MerchantInvoice</t5>
                //<t6> StanSale</t6><t7> VoucherSale</t7> <t8> AuthNo</t8><t9> RRNo</t9>
            	String[][] strTags = new String[][]{{"status", ""}, {"ErrMsg", ""}, {"TrxDate", ""}, 
            				{"StandId", ""}, {"Voucherno", ""}, {"CardLast4Digits", ""},
                           {"CardLast4Digits", ""}, {"TrxAmt", ""}, {"AuthNo", ""},
                           {"RRNO", ""}
                           
                           };
            	
            	
            	
                try
                {
                    Constants.parseXml(responseMsg, strTags);

                    String status = strTags[0][1];
                    if (status.equalsIgnoreCase("false")) {
                        String ErrMsg = strTags[1][1];
                        Constants.showDialog(RefundTrxView.this, "VoidTrx", ErrMsg, 1);

                    } else if (status.equalsIgnoreCase("true")){

                        
                        mViewFlipper.showNext();
                        arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleyellow);
                        mCurrentScreen++;
                        arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleblack);
                    	
                        Constants.StandId = strTags[3][1];
                        Constants.Voucherno = strTags[4][1];
                        
                        TextView lblTxDateTime = (TextView) findViewById(R.id.creditsale_LBL_TrxDateTime);
                        lblTxDateTime.setText(strTags[2][1]);
                        Constants.TrxDate = strTags[2][1];
                        
                        TextView lblTxcardno = (TextView) findViewById(R.id.creditsale_LBL_TrxCardNo);
                        lblTxcardno.setText("  **** **** **** " + strTags[5][1]);
                        Constants.Last4Digits = strTags[5][1];
                        
                        TextView lblTxAmt = (TextView) findViewById(R.id.creditsale_LBL_TxAmt);
                        lblTxAmt.setText(strTags[7][1]);
                        Constants.TrxAmount = strTags[7][1];
                        
                        TextView lblAuthNo = (TextView) findViewById(R.id.creditsale_LBL_AuthCode);
                        lblAuthNo.setText(strTags[8][1]);
                        Constants.AuthCode = strTags[8][1];
                        

                        TextView lblRRNO = (TextView) findViewById(R.id.creditsale_LBL_RRno);
                        lblRRNO.setText(strTags[9][1]);
                        Constants.RRNO = strTags[9][1];
                        

                    }


                }
                catch (Exception ex) {
                    Constants.showDialog(RefundTrxView.this, "VoidTrx", "Invalid response from Mswipe server, please contact support.", 1);
                }
            	
 
        }

    };

    public boolean processCardSaleVoid_MCR_IC() {
        try {

            // the last parameter is for PreAuthCompletion.
            
            MswipeWisepadController wisepadController = new MswipeWisepadController(RefundTrxView.this, AppPrefrences.getGateWayEnv(),null);
            wisepadController.Set_RefundTrx(SetVoidSaleHandler, Constants.Reference_Id, Constants.Session_Tokeniser,
            		mSelectedDate, mTxtCreditAmountDollars.getText().toString(), mLast4Digits, Constants.StandId, Constants.Voucherno);

        	
            mProgressActivity = null;
            mProgressActivity = new CustomProgressDialog(RefundTrxView.this, "Processing Void Tx");
            mProgressActivity.show();

        } catch (Exception ex) {

            Constants.showDialog(RefundTrxView.this, Constants.REFUNDVOID_DIALOG_MSG, Constants.REFUNDVOID_ERROR_Processing_DATA, 1);

        }

        return true;
    }


    public Handler SetVoidSaleHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (mProgressActivity != null)
                mProgressActivity.dismiss();
            Bundle bundle = msg.getData();
            String responseMsg = bundle.getString("response");

            try
            {
            	String[][] strTags = new String[][]{{"status", ""}, {"ErrMsg", ""}};
                Constants.parseXml(responseMsg, strTags);

                String status = strTags[0][1];
                if (status.equalsIgnoreCase("false")) {
                    displayMsg = strTags[1][1];
                    //Constants.showDialog(VoidTransaction.this, "VoidTrx", ErrMsg, 1);

                } else if (status.equalsIgnoreCase("true")){

                      displayMsg = "Approved";                  

                }

                final Dialog dialog = Constants.showDialog(RefundTrxView.this, Constants.REFUNDVOID_DIALOG_MSG, displayMsg, "1");
                Button yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
                yes.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                        doneWithCreditSale();

                    }
                });
                dialog.show();

            }
            catch (Exception ex) {
            	ex.printStackTrace();
                Constants.showDialog(RefundTrxView.this, "VoidTrx", "Invalid response from Mswipe server, please contact support.", 1);
            }

        }
    };



    public void doneWithCreditSale() {

        creditSaleViewDestory();
        finish();
        //Intent intent = new Intent(CreditSaleView.this,MenuView.class);
        //startActivity(intent);

    }

    public void creditSaleViewDestory() {

        


    }

  
    public String removeChar(String s, char c) {

        String r = "";

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != c) r += s.charAt(i);
        }

        return r;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            lblSummaryDate.setText(day + " " + MONTHS[month] + " " + year);
            mSelectedDate = year + "-" + (month + 1) + "-" + day;


        }
    };


}
