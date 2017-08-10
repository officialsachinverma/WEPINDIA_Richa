package com.mswipetech.wisepad.sdktest.view;

import java.io.IOException;
import java.io.StringReader;
import java.util.Calendar;

import org.xmlpull.v1.XmlPullParser;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.mswipetech.wisepad.sdk.MswipeWisePadGatewayConnectionListener;
import com.mswipetech.wisepad.sdk.MswipeWisepadController;
import com.mswipetech.wisepad.R;
import com.mswipetech.wisepad.sdktest.data.AppPrefrences;
import com.mswipetech.wisepad.sdktest.data.ReciptDataModel;

public class BankSaleView extends BaseTitleActivity {
    public final static String log_tab = "BankSaleVeiw=>";

    //fields for cash sale screen
    EditText mTxtCreditAmountDollars = null;
    String mLast4Digits = "";
    
    //fields for Credit Notes
    EditText mTxtPhoneNum = null;
    EditText mTxtEmail = null;
    EditText mTxtReceipt = null;
    EditText mTxtChequeNo = null;

    EditText mTxtNotes = null;

    String mNotes = "";
    String mEmail = "";
    String mReceipt = "";
    String mPhoneNum = "";

    private static final String[] MONTHS = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private int year;
    private int month;
    private int day;
    public String mCheckDate = "";
    static final int DATE_DIALOG_ID = 999;
    TextView mLblCheckDate = null;


    public String displayMsg = "";
    int mCardSuccess = 0;
    public String mPhoneNO = "";
    public String mMessageText = "";


    CustomProgressDialog mProgressActivity = null;

    ApplicationData applicationData = null;
    private WisePadGatewayConncetionListener messagelistener;
    private MswipeWisepadController wisePadController;
    
    ReciptDataModel receiptData = new ReciptDataModel();

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banksale);
        applicationData = (ApplicationData) getApplicationContext();
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        messagelistener = new WisePadGatewayConncetionListener();
        wisePadController = new MswipeWisepadController(this, AppPrefrences.getGateWayEnv(),messagelistener);
  
        initViews();

    }
    

    @Override
    public void onStart() {
    	
        Log.v(ApplicationData.packName, log_tab + "On Start called***************************************");

        wisePadController.startMswipeGatewayConnection();
        
        super.onStart();
    }
    
    @Override
    public void onStop() {
    	
          Log.v(ApplicationData.packName, log_tab + "On stop called***************************************");

          wisePadController.stopMswipeGatewayConnection();
          
          super.onStop();
    
    }
    

    private void initViews() {
        ((TextView) findViewById(R.id.topbar_LBL_heading)).setText(Constants.BANKSALE_DIALOG_MSG);
        ((TextView) findViewById(R.id.topbar_LBL_heading)).setTypeface(applicationData.font);


        //sms prefix from the setting
        ((TextView) findViewById(R.id.banksale_LBL_countrycodeprefix)).setText(applicationData.smsCode);
        ((TextView) findViewById(R.id.banksale_LBL_countrycodeprefix)).setTypeface(applicationData.font);


// The fields for the notes
        mTxtPhoneNum = (EditText) findViewById(R.id.banksale_TXT_mobileno);
        mTxtEmail = (EditText) findViewById(R.id.banksale_TXT_email);

        mTxtReceipt = (EditText) findViewById(R.id.banksale_TXT_receipt);
        mTxtChequeNo = (EditText) findViewById(R.id.banksale_TXT_checqueno);

        mLblCheckDate = (TextView) findViewById(R.id.banksale_Lbl_checkdate);
        mTxtNotes = (EditText) findViewById(R.id.banksale_TXT_notes);


        mTxtPhoneNum.setTypeface(applicationData.font);
        mTxtEmail.setTypeface(applicationData.font);
        mTxtReceipt.setTypeface(applicationData.font);
        mTxtChequeNo.setTypeface(applicationData.font);
        mTxtNotes.setTypeface(applicationData.font);
        mLblCheckDate.setTypeface(applicationData.fontbold);

        ((TextView) findViewById(R.id.banksale_Lbl_lblcheckdate)).setTypeface(applicationData.font);

        mLblCheckDate.setText(day + " " + MONTHS[month] + " " + year);
        mCheckDate = day + "/" + (month + 1) + "/" + year;

        Button btnDate = (Button) findViewById(R.id.banksale_BTN_checkdate);
        btnDate.setTypeface(applicationData.font);
        btnDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);

            }
        });


//The screen are for the amount		

        mTxtCreditAmountDollars = (EditText) findViewById(R.id.banksale_TXT_amount);

        mTxtCreditAmountDollars.setTag("1");

        mTxtCreditAmountDollars.setTypeface(applicationData.font);
      
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

 
        Button btnSubmit = (Button) findViewById(R.id.banksale_BTN_submit);
        btnSubmit.setTypeface(applicationData.font);

        btnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //remove the decimal since in j2me it does not exists
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(BankSaleView.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);


               
                double miAmountDisplay = 0;
                try {
                    if (mTxtCreditAmountDollars.length() > 0)
                        miAmountDisplay = Double.parseDouble(mTxtCreditAmountDollars.getText().toString());


                } catch (Exception ex) {
                    miAmountDisplay = 0;
                }

                if (miAmountDisplay < 1) {
                    Constants.showDialog(BankSaleView.this, Constants.BANKSALE_DIALOG_MSG, Constants.CARDSALE_ERROR_INVALIDAMT, 1);
                    return;
                } else if (mTxtChequeNo.getText().toString().trim().length() != 6) {
                    Constants.showDialog(BankSaleView.this, Constants.BANKSALE_DIALOG_MSG, Constants.CASHSALE_ERROR_invalidChequeNO, 1);
                    return;
                } else if (mTxtPhoneNum.getText().toString().trim().length() != applicationData.PhoneNoLength) {
                    String phoneLength = String.format(Constants.CARDSALE_ERROR_mobilenolen, applicationData.PhoneNoLength);
                    Constants.showDialog(BankSaleView.this, Constants.BANKSALE_DIALOG_MSG, phoneLength, 1);
                    mTxtPhoneNum.requestFocus();
                    return;
                } else if (mTxtPhoneNum.getText().toString().trim().startsWith("0")) {

                    Constants.showDialog(BankSaleView.this, Constants.BANKSALE_DIALOG_MSG, Constants.CARDSALE_ERROR_mobileformat, 1);

                    mTxtPhoneNum.requestFocus();
                    return;
                } else {

                    if (mTxtEmail.getText().toString().trim().length() != 0) {

                        if (emailcheck(mTxtEmail.getText().toString())) {

                        } else {
                            mTxtEmail.requestFocus();
                            //isEmail="False";
                            return;
                        }

                    }

                    //for cash sale the recipt is mandatory
                    //if(Constants.mReceiptRequired.equalsIgnoreCase("true"))
                    {
                        if (mTxtReceipt.getText().toString().trim().length() == 0) {

                            Constants.showDialog(BankSaleView.this, Constants.BANKSALE_DIALOG_MSG, Constants.CARDSALE_ERROR_receiptvalidation, 1);
                            return;
                        }
                    }

                    mPhoneNum = mTxtPhoneNum.getText().toString();
                    mEmail = mTxtEmail.getText().toString();
                    mNotes = mTxtNotes.getText().toString();
                    mReceipt = mTxtReceipt.getText().toString();
                    String dlgMsg = String.format(Constants.BANKSALE_ALERT_AMOUNTMSG, Constants.Currency_Code);
                    final Dialog dialog = Constants.showDialog(BankSaleView.this, Constants.BANKSALE_DIALOG_MSG,
                            dlgMsg + mTxtCreditAmountDollars.getText().toString(), "2");
                    Button yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
                    yes.setOnClickListener(new OnClickListener() {

                        public void onClick(View v) {
                            dialog.dismiss();
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                            try {

                                
                            	MswipeWisepadController wisepadController = new MswipeWisepadController(BankSaleView.this, AppPrefrences.getGateWayEnv(),messagelistener);
                            	wisepadController.BankSaleTrx(banksaleHandler,
                	            		Constants.Reference_Id, Constants.Session_Tokeniser, mTxtReceipt.getText().toString(), 
                	            		ApplicationData.smsCode + mTxtPhoneNum.getText().toString(),
                	            		mTxtEmail.getText().toString(),mTxtCreditAmountDollars.getText().toString(), 
                	            		mTxtNotes.getText().toString(),mCheckDate,mTxtChequeNo.getText().toString());


                                


                                mProgressActivity = new CustomProgressDialog(BankSaleView.this, "Processing Bank...");
                                mProgressActivity.show();
                                


                            } catch (Exception ex) {

                                Constants.showDialog(BankSaleView.this, Constants.BANKSALE_DIALOG_MSG, Constants.CARDSALE_ERROR_PROCESSIING_DATA, 1);
                            }
                        }
                    });

                    Button no = (Button) dialog.findViewById(R.id.bmessageDialogNo);
                    no.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            mTxtCreditAmountDollars.requestFocus();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }

            }// end of the funcation
        });

    }

    public Handler banksaleHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mProgressActivity.dismiss();
            Bundle bundle = msg.getData();
            String responseMsg = bundle.getString("response");
            Log.v(getPackageName(), log_tab + " the response xml is " + responseMsg);

            //<t1> TrxDate</t1><t2> CardLastFourDigits </t2> <t3> TrxAmount</t3> <t4> TrxNotes </t4> <t5> MerchantInvoice</t5>
            //<t6> StanSale</t6><t7> VoucherSale</t7> <t8> AuthNo</t8><t9> RRNo</t9>
        	String[][] strTags = new String[][]{{"status", ""}, {"ErrMsg", ""}, 
        			{"InvoiceNo", ""}, 
        				{"MID", ""}, {"TID", ""}, {"VoucherNo", ""},
                       {"Date", ""}
                       
                       };
        	
        	
            try
            {
                Constants.parseXml(responseMsg, strTags);

                String status = strTags[0][1];
                if (status.equalsIgnoreCase("false")) {
                    String ErrMsg = strTags[1][1];
                    final Dialog dialog = Constants.showDialog(BankSaleView.this, Constants.BANKSALE_DIALOG_MSG, ErrMsg, "1");
                    Button yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
                    yes.setOnClickListener(new OnClickListener() {

                        public void onClick(View v) {
                            dialog.dismiss();
                            doneWithCashSale();


                        }
                    });
                    dialog.show();
                } else if (status.equalsIgnoreCase("true")){

                	
                	try {
						getReceiptDataFromXml(responseMsg.substring(responseMsg.indexOf("<ReceiptData>")+13, responseMsg.indexOf("</ReceiptData>")));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	
                	
                	Constants.InvoiceNo= strTags[2][1]; 
                	Constants.MID= strTags[3][1];
                	Constants.TID= strTags[4][1];
                	Constants.VoucherNo= strTags[5][1];
                	Constants.Date= strTags[6][1];
                    
                	CardSaleDialog dlg = new CardSaleDialog(BankSaleView.this, null, Constants.BANKSALE_DIALOG_MSG, "Approved",
                			Constants.InvoiceNo, Constants.VoucherNo, false);
                    dlg.setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            // TODO Auto-generated method stub
                            showSignature();
                            
                        }
                    });
                    dlg.show();
                    

                }


            }
            catch (Exception ex) {
            	ex.printStackTrace();
                Constants.showDialog(BankSaleView.this, "BankSale", "Invalid response from Mswipe server, please contact support.", 1);
            }
            
            
            
            
            
            
        }
    };

    public void showSignature() {

    	Constants.mReciptDataModel = receiptData;

        Intent intent = new Intent(BankSaleView.this, CashSaleSignatureView.class);
        intent.putExtra("title", Constants.BANKSALE_DIALOG_MSG);
        intent.putExtra("mInvoiceNo", Constants.InvoiceNo);
        intent.putExtra("mVoucherNo", Constants.VoucherNo);
        intent.putExtra("mDate", Constants.Date);

        intent.putExtra("mMID", Constants.MID);
        intent.putExtra("mTID", Constants.TID);
        intent.putExtra("mAmt", mTxtCreditAmountDollars.getText().toString());

        startActivity(intent);
        doneWithCashSale();

    }

    public String removeChar(String s, char c) {

        String r = "";

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != c) r += s.charAt(i);
        }

        return r;
    }

  
    public void doneWithCashSale() {

        cashSaleViewDestory();
        finish();
        //Intent intent = new Intent(CreditSaleView.this,MenuView.class);
        //startActivity(intent);

    }

    public void cashSaleViewDestory() {


       


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

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            mLblCheckDate.setText(day + " " + MONTHS[month] + " " + year);
            mCheckDate = day + "/" + (month + 1) + "/" + year;
        }
    };


    public boolean emailcheck(String str) {

    	boolean results = Constants.isValidEmail(str);         
        if (results == false) {
            Constants.showDialog(BankSaleView.this, Constants.BANKSALE_DIALOG_MSG, Constants.CARDSALE_ERROR_email, 1);
            return false;
        } else {
            return true;
        }
    }

    class WisePadGatewayConncetionListener implements  MswipeWisePadGatewayConnectionListener{

		@Override
		public void Connected(String msg) {
			
			((TextView)findViewById(R.id.topbar_LBL_status)).setText(msg);
			
		}

		@Override
		public void Connecting(String msg) {
			
			((TextView)findViewById(R.id.topbar_LBL_status)).setText(msg);
			
		}

		@Override
		public void disConnect(String msg) {
			
			((TextView)findViewById(R.id.topbar_LBL_status)).setText(msg);
			
		}    	
    }
    
    
    public String getReceiptDataFromXml(String xmlReceiptData) throws Exception
    {

        XmlPullParser parser = Xml.newPullParser();
        String errMsg = "";
        String xmlText = "";

        try {
        	
        	
            Log.v(getPackageName(), log_tab + " receiptdata " + xmlReceiptData.toString() );

        	
            parser.setInput(new StringReader(xmlReceiptData));
            int eventType = XmlPullParser.START_TAG;
            boolean leave = false;
            boolean isDocElementExists = false;
            eventType = parser.getEventType();
            String xmlTag;
            
            while (!leave && eventType != XmlPullParser.END_DOCUMENT) {
            	
            	switch (eventType) {
            	
            	case XmlPullParser.START_TAG:
            		
            		xmlTag = parser.getName().toString();            		
            		            		
                    Log.v(getPackageName(), log_tab + " xmlTag " + xmlTag);

                    
            		if(xmlTag.equalsIgnoreCase("mt2")){
            			
            			isDocElementExists = true;
            			receiptData = new ReciptDataModel();
            			
            		}else{
            			
            			if(isDocElementExists){
            				
            				if(xmlTag.equalsIgnoreCase("MERCHANTNAME")){
            					xmlText = parser.nextText();
            					receiptData.merchantName = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("MERCHANTADDRESS")){
            					xmlText = parser.nextText();
            					receiptData.merchantAdd = Html.fromHtml((xmlText == null ? "" : xmlText)).toString() ;
            				}else if(xmlTag.equalsIgnoreCase("DATETIME") || xmlTag.equalsIgnoreCase("TrxDate")){
            					xmlText = parser.nextText();
            					receiptData.dateTime = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("MID")){
            					xmlText = parser.nextText();
            					receiptData.mId = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("TID")){
            					xmlText = parser.nextText();
            					receiptData.tId = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("VOUCHERNO")){
            					xmlText = parser.nextText();
            					receiptData.voucherNo = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("INVOICENO") || xmlTag.equalsIgnoreCase("PrismInvoiceNo")){
            					xmlText = parser.nextText();
            					receiptData.invoiceNo = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("REFNO")|| xmlTag.equalsIgnoreCase("MerInvoiceNo")){
            					xmlText = parser.nextText();
            					receiptData.refNo = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("TXTYPE")){
            					xmlText = parser.nextText();
            					receiptData.trxType = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("CURRENCY")){
            					xmlText = parser.nextText();
            					receiptData.currency = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("TOTALAMOUNT")){
            					xmlText = parser.nextText();
            					receiptData.totalAmount = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("APPVERSION") || xmlTag.equalsIgnoreCase("VersionNo")){
            					xmlText = parser.nextText();
            					receiptData.appVersion = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("TRXDATE")){
            					xmlText = parser.nextText();
            					receiptData.trxDate = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("TRXIMGDATE")){
            					xmlText = parser.nextText();
            					receiptData.trxImgDate = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("CHEQUEDATE")){
            					xmlText = parser.nextText();
            					receiptData.chequeDate = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("CHEQUENO")){
            					xmlText = parser.nextText();
            					receiptData.chequeNo = (xmlText == null ? "" : xmlText);
            				}
            			}
            		}
            		
            		break;
            		
            	case XmlPullParser.END_TAG:
            		
            		xmlTag = parser.getName();
            		
                    Log.v(getPackageName(), log_tab + " xmlTag " + xmlTag);

            		
            		if(xmlTag.equalsIgnoreCase("mt2")){
            			isDocElementExists = true;
            			leave = true;
            		}
            		
            		
            		break;
            	}
            	
            	eventType = parser.next();
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw e;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (parser != null) {
                parser = null;
            }
        }
        return errMsg;

    }
}
