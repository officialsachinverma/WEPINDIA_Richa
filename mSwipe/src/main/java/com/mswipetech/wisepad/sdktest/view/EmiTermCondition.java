package com.mswipetech.wisepad.sdktest.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.mswipetech.wisepad.R;
import com.mswipetech.wisepad.sdktest.data.ReciptDataModel;

public class EmiTermCondition extends Activity{
	
	private static final int MS_SIGNATURE_UPLOAD_ACTIVITY_REQUEST_CODE = 10101;

	public final static String log_tab = "EmiTermCondition=>";
	TextView txtTransactionDetails, txtEmiDetials;
	CheckBox chkIagree;
	Button btn_next;
	private boolean isEmvSwiper;
	private boolean isEmiTrx;
	private String merchantDetails= "";
	private String merchantOtherDetails= "";
	private String title="";
	private String mStandId="";
	private String mAmt="";
	private String lstFrDgts="";
	private String emiTenure="";
	private String emiRate="";
	private String emiTotalPayableAmount="";
	private String emiPerMonthEmi="";
	private String sponsorBankName="";
	private String txtID="";
	private String emiTxnID="";
	private String cardIssuer= "";
	private String TVR="";
	private String TSI="";
	private String mStrAuthCodeReceipt="";
	private String mStrDate="";
	private String mStrCardNum="";
	private String mStrExpDate="";
	private String mStrAmt="";
	private String mStrCardType="";
	private String mStrApplication="";
	private String mStrTVR="";

	boolean isSignatureRequired = true;	
	ReciptDataModel mReciptDataModel;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emi_confirmation_screen);
		initView();
	}
	
 	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Constants.showDialog(EmiTermCondition.this, title, "please accept the agreement and press submit", 1);
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}

	}
	
	private void initView(){
		
	        
      
       	isEmvSwiper = Constants.isEmvTx ;
       	
       	
       	
    	if(title == null)
    		title = "Emi Sale";
     	mReciptDataModel = (ReciptDataModel) getIntent().getExtras().get("receiptDetailModel");

        mStandId = mReciptDataModel.stan;
        mAmt= mReciptDataModel.totalAmount;
     	String authCode = mReciptDataModel.authCode;
     	String rrno = mReciptDataModel.rrNo;
     	String date = mReciptDataModel.dateTime;
     	lstFrDgts= mReciptDataModel.cardNo;
     	String ExpiryDate = mReciptDataModel.expDate;
     	String EmvCardExpdate = mReciptDataModel.emvSigExpDate;
		String SwitchCardType = mReciptDataModel.cardType;
		String AppIdentifier = mReciptDataModel.appId;
		String ApplicationName = mReciptDataModel.appName;
		
		TVR = mReciptDataModel.tvr;
        if(TVR == null) TVR = "";

		TSI = mReciptDataModel.tsi;
        if(TSI == null) TSI = "";

		if(EmvCardExpdate==null)
			EmvCardExpdate = "";
     	    	
 		txtID = "TXN ID: "+mStandId;

     	if(mReciptDataModel != null){
     		merchantDetails = mReciptDataModel.merchantName+"\n"+Html.fromHtml(mReciptDataModel.merchantAdd);
     		cardIssuer = "CARD ISSUER: "+mReciptDataModel.cardIssuer;
     		sponsorBankName = mReciptDataModel.bankName;
     		emiTxnID = "EMI TXN ID: "+mReciptDataModel.billNo;

     		String[] dateTime = date.split(" ");
     		merchantOtherDetails = "Date: "+dateTime[0]+"\n"+"Time: "+dateTime[1].trim()+"\n"+"MID: "+mReciptDataModel.mId+"\n"+"TID: "+mReciptDataModel.tId+"\n"+"Batch No.: "+mReciptDataModel.batchNo+"\nInvoice No.: "+mReciptDataModel.refNo+"\n"+"Bill No.:"+mReciptDataModel.billNo;
     	}
 		
 		emiTenure = "TENURE: "+mReciptDataModel.noOfEmi+" Months";
 		emiRate = "INTEREST RATE(P.A): "+mReciptDataModel.interestRate+"%";
 		emiTotalPayableAmount = "TOTAL PAYABLE AMT(Incl. Interest): "+Constants.Currency_Code+" "+mReciptDataModel.total_Pay_Amount;
 		emiPerMonthEmi = "EMI AMT: "+Constants.Currency_Code+" "+String.format("%.2f", (Double.parseDouble(mReciptDataModel.total_Pay_Amount)/Double.parseDouble(mReciptDataModel.noOfEmi)));
     	
     	StringBuilder cardNumMask = new StringBuilder();
     	
     	if (mReciptDataModel.firstDigitsOfCard.length() >= 6) {
			
     		cardNumMask.append(mReciptDataModel.firstDigitsOfCard.substring(0,4)+" "+mReciptDataModel.firstDigitsOfCard.substring(4,6));
		}
     	
     	if (mReciptDataModel.cardNo.length() >= 8) {
			
     		cardNumMask.append(mReciptDataModel.cardNo.substring(8,mReciptDataModel.cardNo.length()));
		}
     	
     	if(isEmvSwiper)
     	{
 	        String tempString=EmvCardExpdate.trim();
	        if(tempString.length()==5)
	        {
	        	EmvCardExpdate=tempString.substring(3,5);
	        	EmvCardExpdate=EmvCardExpdate+"/" +tempString.substring(0,2);

	        }else if(tempString.length()==4){
	        	EmvCardExpdate=tempString.substring(2,4);
	            EmvCardExpdate=EmvCardExpdate+  "/" + tempString.substring(0,2);
	        }else{
	        	EmvCardExpdate=tempString;
	        }

     		mStrAuthCodeReceipt = "APPR CD: " + authCode;
     		mStrDate = "DATE/TIME: " + date;
     		//mStrCardNum = "CARD NUM: " + mReciptDataModel.cardNo;
     		mStrCardNum = "CARD NUM: " + cardNumMask.toString().replaceAll("\\s","");
     		mStrExpDate = "EXP DT: " + EmvCardExpdate;
     		mStrAmt = "BASE AMT: " + Constants.Currency_Code + " "  + mAmt ;
     		mStrCardType =  SwitchCardType +"-Chip";
     		mStrApplication = "APP ID: " + AppIdentifier + " APP NAME: " + ApplicationName;
     		mStrTVR = "TVR: " + TVR + " TSI: " + TSI;

     	}
     	else{
     		mStrAuthCodeReceipt = "APPR CD: " + authCode;
     		mStrDate = "DATE: " + date ;
     		mStrCardNum = "CARD NUM: " + cardNumMask.toString().replaceAll("\\s","");
     		mStrExpDate = "EXP DT: " +( ExpiryDate.length() >= 3 ? ExpiryDate.substring(0, 2)+"/"+ExpiryDate.substring(2) : ExpiryDate);
     		mStrAmt = "BASE AMT: " + Constants.Currency_Code + " "  + mAmt ;
     		mStrCardType = SwitchCardType +"-Swipe";

     	}
     	
		
     	((TextView) findViewById(R.id.topbar_LBL_heading)).setText(title);
     	((TextView) findViewById(R.id.sponsorbankname)).setText(sponsorBankName);
     	((TextView) findViewById(R.id.mearchant_details)).setText(merchantDetails.trim());
     	((TextView) findViewById(R.id.mearchant_other_details)).setText(merchantOtherDetails);
     	txtTransactionDetails = (TextView) findViewById(R.id.transaction_details);
     	String swipeChip = "";
		if(isEmvSwiper)
			swipeChip = "Chip";
		else
			swipeChip ="Swipe";
		txtTransactionDetails.setText(mStrCardNum+" "+swipeChip+"\n"+mStrExpDate+"\n"+"CARD TYPE: "+mStrCardType+"\n"+txtID+"\n"+mStrAuthCodeReceipt
				+"\nRRNO: "+rrno+"\n"+mStrTVR);
		
		txtEmiDetials = (TextView) findViewById(R.id.emi_details);
		txtEmiDetials.setText(emiTxnID+"\n"+emiTenure+"\n"+cardIssuer+"\n"+mStrAmt+"\n"+emiRate+"\n"+emiPerMonthEmi+"\n"+emiTotalPayableAmount);
		((TextView) findViewById(R.id.final_transction_details)).setText("BASE AMT. :"+Constants.Currency_Code+" "+mAmt+"\n"+emiTotalPayableAmount);
		btn_next = (Button) findViewById(R.id.emi_BTN_next);
		btn_next.setEnabled(false);
		btn_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				finish();
			}
		});
		
		chkIagree = (CheckBox) findViewById(R.id.emi_CHK_i_agree);
		chkIagree.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				btn_next.setEnabled(isChecked);
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		super.onActivityResult(requestCode, resultCode, data);
		
		if (ApplicationData.IS_DEBUGGING_ON)
			Logs.v(getPackageName(), log_tab + "onActivityResult", true, true);
		
		if(requestCode == MS_SIGNATURE_UPLOAD_ACTIVITY_REQUEST_CODE){
			
			if (ApplicationData.IS_DEBUGGING_ON)
				Logs.v(getPackageName(), log_tab + "onActivityResult", true, true);
			
			Intent intent = new Intent();
			
			if(resultCode == RESULT_OK ){
				
				intent.putExtra("signatureUpdateStatus", true);
				intent.putExtra("errMsg", "");
				setResult(RESULT_OK, intent);
				
			}else if(resultCode == RESULT_CANCELED){
				
				intent.putExtra("signatureUpdateStatus", false);
				intent.putExtra("errMsg", data.getExtras().getString("msg"));
				setResult(RESULT_CANCELED, intent);
				
			}
			
			finish();
			
		}
	}
}