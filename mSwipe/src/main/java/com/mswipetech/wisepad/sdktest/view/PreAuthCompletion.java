package com.mswipetech.wisepad.sdktest.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.mswipetech.wisepad.sdk.MswipeWisepadController;
import com.mswipetech.wisepad.sdk.Data.PreAuthData;

import com.mswipetech.wisepad.R;
import com.mswipetech.wisepad.sdktest.data.AppPrefrences;


public class PreAuthCompletion extends BaseTitleActivity {

    public HashMap<String, String> setHashMember = new HashMap<String, String>();
    public final static String log_tab = "VoidSale=>";
    CustomProgressDialog mProgressActivity = null;

    int mCurrentScreen = 0;
    int iSelectedListPosition =0;
    int iDataCount = 0;
    TextView lblSummaryDate = null;
    //fields for card sale screen
    EditText mTxtCreditAmountDollars = null;
    EditText mTxtLast4Digits = null;
    EditText mTxtDisAmountDollars = null;
    TextView mLblTot = null;
    String mLast4Digits = "";
    String mTipAmout = "";

    ListView lstHistory = null;

    public String mSelectedDate = "";
    private static final String[] MONTHS = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    static final int DATE_DIALOG_ID = 999;
    private int year;
    private int month;
    private int day;

    MswipeWisepadController mWisepadController = null;
    //for the void trx details
    EditText mTxtPreAuthAmount = null;

    public String displayMsg = "";
    public String mStandId = "";
    public String mTrxDate = "";
    public String mVoucherno = "";
    public String mPrismInvoiceNo = "";

    ViewFlipper mViewFlipper = null;
    LinearLayout[] arrPageLinearObjects = null;
    ApplicationData applicationData = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preauthvoidtransaction);
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
    public void onDestroy()
    {
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mCurrentScreen == 0) {
                finish();

            } else {
                if(iDataCount ==1){
                                    mViewFlipper.showPrevious();
                                    arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleyellow);
                                    mCurrentScreen--;
                                    arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleblack);

                }
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
        txtHeading.setText(Constants.PREAUTHVOID_DIALOG_MSG);
        txtHeading.setTypeface(applicationData.font);

        LinearLayout lnl = (LinearLayout) findViewById(R.id.creditsale_LNR_PrismInvoiceNo);
        lnl.setVisibility(View.VISIBLE);

        arrPageLinearObjects = new LinearLayout[4];
        arrPageLinearObjects[0] = (LinearLayout) findViewById(R.id.creditsale_LNL_page1);
        arrPageLinearObjects[1] = (LinearLayout) findViewById(R.id.creditsale_LNL_page2);
        arrPageLinearObjects[2] = (LinearLayout) findViewById(R.id.creditsale_LNL_page3);


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

        mTxtDisAmountDollars = (EditText) findViewById(R.id.creditsale_TXT_tipamount);
        mLblTot = (TextView) findViewById(R.id.creditsale_LBL_totalamount);

        //TextView mLblLblTot = (TextView) findViewById(R.id.creditsale_LBL_lbltotalamount);
        //mLblLblTot.setVisibility(View.GONE);

        mLblTot = (TextView) findViewById(R.id.creditsale_LBL_totalamount);
        mLblTot.setVisibility(View.GONE);

        mTxtCreditAmountDollars.setTypeface(applicationData.font);
        mTxtDisAmountDollars.setTypeface(applicationData.font);
        mTxtDisAmountDollars.setTypeface(applicationData.font);
        mLblTot.setTypeface(applicationData.fontbold);
        mTxtLast4Digits.setTypeface(applicationData.font);


  
        mTxtCreditAmountDollars.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                
                calculateTotalAmt();

            }
        });

        mTxtDisAmountDollars.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
              

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                
                               
                calculateTotalAmt();

                
            }
        });


        if (applicationData.mTipRequired.equalsIgnoreCase("true")) {
            mTxtDisAmountDollars.setVisibility(View.GONE);
            //((TextView)findViewById(R.id.creditsale_LBL_tipamount)).setVisibility(View.GONE);

        } else {
            mTxtDisAmountDollars.setVisibility(View.GONE);
            //((TextView)findViewById(R.id.creditsale_LBL_tipamount)).setVisibility(View.GONE);
        }
        mTxtDisAmountDollars.setVisibility(View.GONE);
        mLblTot.setVisibility(View.GONE);

        Button btnSubmit = (Button) findViewById(R.id.creditsale_BTN_submit);
        btnSubmit.setTypeface(applicationData.font);
        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.hideSoftInputFromWindow(PreAuthCompletion.this.getCurrentFocus().getWindowToken(),
                        //InputMethodManager.HIDE_NOT_ALWAYS);

                String tempAmount = mTxtCreditAmountDollars.getText().toString().trim();
                tempAmount = removeChar(tempAmount, '.');
                String tempDisAmount = mTxtDisAmountDollars.getText().toString().trim();
                tempDisAmount = removeChar(tempDisAmount, '.');


                if (applicationData.mTipRequired.equalsIgnoreCase("false")) {
                    mTipAmout = ("0.00");
                } else if (mTxtDisAmountDollars.length() == 0) {
                    mTipAmout = ("0.00");
                } else {
                    mTipAmout = mTxtDisAmountDollars.getText().toString();
                }

                mLast4Digits = mTxtLast4Digits.getText().toString().trim();
                double miAmountDisplay = 0;
                try {
                    if (mTxtCreditAmountDollars.length() > 0)
                        miAmountDisplay = Double.parseDouble(mTxtCreditAmountDollars.getText().toString());


                } catch (Exception ex) {
                    miAmountDisplay = 0;
                }


                if (miAmountDisplay < 1) {
                    Constants.showDialog(PreAuthCompletion.this, Constants.PREAUTHVOID_DIALOG_MSG, Constants.CARDSALE_ERROR_INVALIDAMT, 1);

                    return;
                } else if (mTxtLast4Digits.getText().toString().trim().length() < 3) {
                    Constants.showDialog(PreAuthCompletion.this, Constants.PREAUTHVOID_DIALOG_MSG, Constants.CARDSALE_ERROR_INVALIDCARDDIGITS, 1);
                    mTxtLast4Digits.requestFocus();
                    return;
                } else {

                    String voidAmt = String.format(Constants.PREAUTHVOID_ALERT_AMOUNTMSG, applicationData.Currency_Code, (mLblTot.getText().toString() + ""), mLast4Digits);
                    final Dialog dialog = Constants.showDialog(PreAuthCompletion.this, Constants.PREAUTHVOID_DIALOG_MSG,
                            voidAmt, "2");
                    Button yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
                    yes.setTypeface(applicationData.font);
                    yes.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {

                            dialog.dismiss();
                            try {
                                if (applicationData.IS_DEBUGGING_ON)
                                    Logs.v(ApplicationData.packName, log_tab + "The base amout " + mTxtCreditAmountDollars.getText().toString() + " Tot " + mLblTot.getText().toString() + " tip " + mTipAmout, false, true);

                                mWisepadController = new MswipeWisepadController(PreAuthCompletion.this, AppPrefrences.getGateWayEnv(),null);
                                mWisepadController.Get_PreauthDetailsForCompletion(getPreauthDetailHandler, Constants.Reference_Id, Constants.Session_Tokeniser, mSelectedDate, mLblTot.getText().toString(), mLast4Digits);

                                mProgressActivity = new CustomProgressDialog(PreAuthCompletion.this, "Fetching transaction details");
                                mProgressActivity.show();


                            } catch (Exception ex) {
                                Constants.showDialog(PreAuthCompletion.this, Constants.PREAUTHVOID_DIALOG_MSG, Constants.PREAUTHVOID_ERROR_Processing_DATA, 1);


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
//the screen for tx list
		lstHistory = (ListView) findViewById(R.id.preauthvoid_LST_trx);
		lstHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
						// TODO Auto-generated method stub
						mViewFlipper.showNext();
						arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleyellow);
						mCurrentScreen++;
						arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleblack);
						iSelectedListPosition = position;
						mTxtPreAuthAmount.setText(mTxtCreditAmountDollars.getText());

						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						if (imm != null) {
							imm.toggleSoftInput(0,InputMethodManager.SHOW_IMPLICIT);
						}
						showVoidTxDetails(listData.get(position));

					}
				});
//The scrren for Tx details

        mTxtPreAuthAmount = (EditText) findViewById(R.id.creditsale_TXT_preauthamount);
        mTxtPreAuthAmount.setTypeface(applicationData.font);



        mTxtPreAuthAmount.setTag("1");
        mTxtPreAuthAmount.setOnTouchListener(new OnTouchListener() {

               @Override
               public boolean onTouch(View arg0, MotionEvent arg1) {
                   // TODO Auto-generated method stub
				if (mTxtPreAuthAmount.isFocused()) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					if (imm != null) {
						imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
					}

					if (mTxtPreAuthAmount.getText().length() != 0) {
						mTxtPreAuthAmount.setSelection(mTxtPreAuthAmount.getText().length());
						return true;
					}
				}
				return false;

               }
           });

        mTxtPreAuthAmount.setOnFocusChangeListener(new OnFocusChangeListener() {
               @Override
               public void onFocusChange(View v, boolean hasFocus) {
                   // TODO Auto-generated method stub
                   if (v.getTag().toString().equals("1")) {
                       mTxtPreAuthAmount.setSelection(mTxtPreAuthAmount.getText().length());
                   }

               }
           });

        mTxtPreAuthAmount.addTextChangedListener(new TextWatcher() {

               @Override
               public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                   // TODO Auto-generated method stub
                   String oldString = mTxtPreAuthAmount.getText().toString();
                   String newString = removeChar(oldString, '.');
                   int ilen = newString.length();
                   if (ilen == 1 || ilen == 2) {
                       newString = "." + newString;
                   } else if (ilen > 2) {
                       newString = newString.substring(0, ilen - 2) + "." + newString.substring(ilen - 2, ilen);
                   }
                   if (!newString.equals(oldString)) {
                       mTxtPreAuthAmount.setText(newString);
                       mTxtPreAuthAmount.setSelection(mTxtPreAuthAmount.getText().length());

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

        ((TextView) findViewById(R.id.creditsale_LBL_lblTrxDateTime)).setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.creditsale_LBL_TrxDateTime)).setTypeface(applicationData.font);

        ((TextView) findViewById(R.id.creditsale_LBL_lblTrxCardNo)).setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.creditsale_LBL_TrxCardNo)).setTypeface(applicationData.font);

       ((TextView) findViewById(R.id.creditsale_LBL_lblTxAmt)).setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.creditsale_LBL_lblTxAmt)).setText("Amt ");

       ((TextView) findViewById(R.id.creditsale_LBL_AmtPrefix)).setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.creditsale_LBL_AmtPrefix)).setText(applicationData.Currency_Code + " ");

        ((TextView) findViewById(R.id.creditsale_LBL_TxAmt)).setTypeface(applicationData.font);

        ((TextView) findViewById(R.id.creditsale_LBL_lblAuthCode)).setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.creditsale_LBL_AuthCode)).setTypeface(applicationData.font);

        ((TextView) findViewById(R.id.creditsale_LBL_lblRRno)).setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.creditsale_LBL_RRno)).setTypeface(applicationData.font);

        ((TextView) findViewById(R.id.creditsale_LBL_lblPrismInvoiceNo)).setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.creditsale_LBL_PrismInvoiceNo)).setTypeface(applicationData.font);




        Button btnTxNext = (Button) findViewById(R.id.creditsale_BTN_tx_next);
        btnTxNext.setTypeface(applicationData.font);
        btnTxNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                double miPreautAmountDisplay = 0;
                try {
                    if (mTxtPreAuthAmount.length() > 0)
                        miPreautAmountDisplay = Double.parseDouble(mTxtPreAuthAmount.getText().toString());


                } catch (Exception ex) {
                    miPreautAmountDisplay = 0;
                }
                double miAmountDisplay = 0;
                try {
                    if (mTxtCreditAmountDollars.length() > 0)
                        miAmountDisplay = Double.parseDouble(mTxtCreditAmountDollars.getText().toString());


                } catch (Exception ex) {
                    miAmountDisplay = 0;
                }


                if (miPreautAmountDisplay == 0) {
                    Constants.showDialog(PreAuthCompletion.this, Constants.PREAUTHVOID_DIALOG_MSG, Constants.CARDSALE_ERROR_INVALIDAMT, 1);

                    return;
                }else if(miPreautAmountDisplay > miAmountDisplay) {
                    Constants.showDialog(PreAuthCompletion.this, Constants.PREAUTHVOID_DIALOG_MSG, Constants.PREAUTH_ERROR_INVALIDAMT, 1);
                    return;
                }

                String voidAmt = String.format(Constants.PREAUTHVOID_ALERT_FORVOID, mSelectedDate, applicationData.Currency_Code, (mTxtPreAuthAmount.getText().toString() + ""),
                        mLast4Digits);
                final Dialog dialog = Constants.showDialog(PreAuthCompletion.this, Constants.PREAUTHVOID_DIALOG_MSG, voidAmt, "2");
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
                        //mTxtPreAuthAmount.requestFocus();


                    }
                });
                dialog.show();


            }
        });


    }
    ArrayList<PreAuthData> listData;
	public Handler getPreauthDetailHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if (mProgressActivity != null)
				mProgressActivity.dismiss();

			Bundle bundle = msg.getData();
			boolean status = bundle.getBoolean("status");			
			if (status) {
				listData = (ArrayList<PreAuthData>) bundle.getSerializable("data");
				iDataCount = listData.size();

				if (listData.size() > 0) {

					mViewFlipper.showNext();
					arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleyellow);
					mCurrentScreen++;
					arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleblack);
					if (listData.size() > 1) {
						lstHistory.setAdapter(new CardHistoryAdapter(PreAuthCompletion.this, listData));

					} else {
						mViewFlipper.showNext();
						arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleyellow);
						mCurrentScreen++;
						arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleblack);
						iSelectedListPosition = 0;
						mTxtPreAuthAmount.setText(mTxtCreditAmountDollars.getText());
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						if (imm != null) {
							imm.toggleSoftInput(0,InputMethodManager.SHOW_IMPLICIT);
						}
						showVoidTxDetails(listData.get(0));
						Log.e(applicationData.packName, listData.size() + " " + listData.get(0).trxDate);
					}
				}else{
					final Dialog dialog = Constants.showDialog(PreAuthCompletion.this,Constants.PREAUTHVOID_DIALOG_MSG,"Unable to show the PreAuth data, please contact support.","1");
					Button yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
					yes.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							dialog.dismiss();
							finish();
						}
					});
					dialog.show();
				}
			} else {
				String errMsg = bundle.getString("errMsg");
				Constants.showDialog(PreAuthCompletion.this,Constants.PREAUTHVOID_DIALOG_MSG, errMsg , 1);
			}

		}

	};

    public boolean processCardSaleVoid_MCR_IC() {
        try {
        	

            mWisepadController.Set_PreauthTrxCompletion(SetVoidSaleHandler, Constants.Reference_Id, Constants.Session_Tokeniser, mSelectedDate, mTxtPreAuthAmount.getText().toString(), mLast4Digits, mStandId, mPrismInvoiceNo);
            mProgressActivity = null;
            mProgressActivity = new CustomProgressDialog(PreAuthCompletion.this, "Processing Preauth Tx");
            mProgressActivity.show();

        } catch (Exception ex) {

            Constants.showDialog(PreAuthCompletion.this, Constants.PREAUTHVOID_DIALOG_MSG, Constants.PREAUTHVOID_ERROR_Processing_DATA, 1);

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

                final Dialog dialog = Constants.showDialog(PreAuthCompletion.this, Constants.PREAUTHVOID_DIALOG_MSG, displayMsg, "1");
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
                Constants.showDialog(PreAuthCompletion.this,  Constants.PREAUTHVOID_DIALOG_MSG, "Invalid response from Mswipe server, please contact support.", 1);
            }

        }
    };

   



    public void showVoidTxDetails(PreAuthData preAuthData) {

        //<t1> TrxDate</t1><t2> CardLastFourDigits </t2> <t3> TrxAmount</t3> <t4> TrxNotes </t4> <t5> MerchantInvoice</t5>
        //<t6> StanSale</t6><t7> VoucherSale</t7> <t8> AuthNo</t8><t9> RRNo</t9>
        

        TextView lblTxDateTime = (TextView) findViewById(R.id.creditsale_LBL_TrxDateTime);
        lblTxDateTime.setText(preAuthData.trxDate);
        mSelectedDate = preAuthData.trxDate;

        TextView lblTxcardno = (TextView) findViewById(R.id.creditsale_LBL_TrxCardNo);
        lblTxcardno.setText("  **** **** **** " + preAuthData.cardLastFourDigits);
        mLast4Digits = preAuthData.cardLastFourDigits;

        TextView lblTxAmt = (TextView) findViewById(R.id.creditsale_LBL_TxAmt);
        lblTxAmt.setText(preAuthData.trxAmount);
        

        TextView lblAuthNo = (TextView) findViewById(R.id.creditsale_LBL_AuthCode);
        lblAuthNo.setText(preAuthData.authNo);

        TextView lblRRNO = (TextView) findViewById(R.id.creditsale_LBL_RRno);
        lblRRNO.setText(preAuthData.rrNo);
        
        TextView lblPrismInvoiceNo =((TextView) findViewById(R.id.creditsale_LBL_PrismInvoiceNo));
        lblPrismInvoiceNo.setText(preAuthData.prismInvoiceNo);
        mPrismInvoiceNo = preAuthData.prismInvoiceNo;
        mStandId = preAuthData.stanSale;
    }

    public void doneWithCreditSale() {

        creditSaleViewDestory();
        finish();
        //Intent intent = new Intent(CreditSaleView.this,MenuView.class);
        //startActivity(intent);

    }

    public void creditSaleViewDestory() {

   


    }

     public void calculateTotalAmt() {
    	
    	int amt = 0;
        int tips = 0;
        //remove the decimal since in j2me it does not exists
        String tempAmount = mTxtCreditAmountDollars.getText().toString().trim();
        String tempDisAmount = mTxtDisAmountDollars.getText().toString().trim();
        tempAmount = removeChar(tempAmount, '.');
        tempDisAmount = removeChar(tempDisAmount, '.');


        if (tempAmount.length() > 0) {

            amt = Integer.parseInt(tempAmount);
        }
        if (tempDisAmount.length() > 0) {

            tips = Integer.parseInt(tempDisAmount);
        }
    	
        String tempStr = "";
        if ((amt + tips) > 0)
            tempStr = "" + (amt + tips);

        StringBuffer tempamtStr = new StringBuffer(tempStr);


        int ilen = tempamtStr.length();
        StringBuffer amttotDisplay = new StringBuffer();
        if (ilen > 0 && ilen <= 2) {
            amttotDisplay.append("0." + tempamtStr);
        } else if (ilen > 2) {
            tempamtStr.insert(ilen - 2, ".");
            amttotDisplay.append(tempamtStr);

        }
        mLblTot.setText(amttotDisplay.toString());
        //the below is commented so that the hint should be displayed when the total is 0.0
        //msTotal = amttotDisplay.toString();

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

    public class CardHistoryAdapter extends BaseAdapter {
            ArrayList<PreAuthData> listData = null;
            Context context;

            public CardHistoryAdapter(Context context, ArrayList<PreAuthData> listData) {
                this.listData = listData;
                this.context = context;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return listData.size();
            }

            @Override
            public Object getItem(int position) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public long getItemId(int position) {
                // TODO Auto-generated method stub
                return 0;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    convertView = inflater.inflate(R.layout.cardhistorylistdata, null);
                }

                PreAuthData data = listData.get(position);


			TextView lblamt = (TextView) convertView.findViewById(R.id.cardhistorylist_LBL_lblamount);
			lblamt.setText("");
			lblamt.setTypeface(applicationData.font);

			TextView amt = (TextView) convertView.findViewById(R.id.cardhistorylist_LBL_amount);
			amt.setText(data.trxDate);
			amt.setTypeface(applicationData.font);

			TextView date = (TextView) convertView.findViewById(R.id.cardhistorylist_LBL_date);
			date.setText(data.stanSale);
			date.setTypeface(applicationData.font);

			TextView type = (TextView) convertView.findViewById(R.id.cardhistorylist_LBL_cardtype);
			type.setText(data.prismInvoiceNo);
			type.setTypeface(applicationData.font);

                return convertView;
            }

        }

}
