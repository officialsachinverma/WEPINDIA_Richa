package com.mswipetech.wisepad.sdktest.util;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;

import com.mswipetech.wisepad.sdktest.data.ReciptDataModel;
import com.mswipetech.wisepad.sdktest.view.ApplicationData;
import com.mswipetech.wisepad.sdktest.view.Logs;

public class ReceiptUtility {
	
	private final String log_tab = "ReceiptUtility=>";
	private  DecimalFormat df = new DecimalFormat(".00");
    
    
    private Context context = null;
	private ReciptDataModel reciptDataModel = null;
	private boolean isPrintSignatureRequired = false; 
	private byte[] bitmapBytes = null;
	private int mswipeTargetWidth = 380;
	
    public static enum TYPE{
		CARD, EMI, CASH
	}

	public static final byte[] INIT = {0x1B,0x40};
	public static final  byte[] POWER_ON = {0x1B,0x3D,0x01};
	public static final  byte[] POWER_OFF = {0x1B,0x3D,0x02};
	public static final  byte[] NEW_LINE = {0x0A};
	public static final  byte[] ALIGN_LEFT = {0x1B,0x61,0x00};
	public static final  byte[] ALIGN_CENTER = {0x1B,0x61,0x01};
	public static final  byte[] ALIGN_RIGHT = {0x1B,0x61,0x02};
	public static final  byte[] EMPHASIZE_ON = {0x1B,0x45,0x01};
	public static final  byte[] EMPHASIZE_OFF = {0x1B,0x45,0x00};
	
	public static final  byte[] FONT_5X8 = {0x1B,0x4D,0x00};
	public static final  byte[] FONT_5X12 = {0x1B,0x4D,0x01};
	public static final  byte[] FONT_8X12 = {0x1B,0x4D,0x02};
	public static final  byte[] FONT_10X18 = {0x1B,0x4D,0x03};
	
	public static final  byte[] FONT_SIZE_0 = {0x1D,0x21,0x00};
	public static final  byte[] FONT_SIZE_1 = {0x1D,0x21,0x11};
	
	public static final  byte[] CHAR_SPACING_0 = {0x1B,0x20,0x00};
	public static final  byte[] CHAR_SPACING_1 = {0x1B,0x20,0x01};
	
	public ReceiptUtility(Context context){
		
		this.context = context;
		
	}
	
	public byte[] printReceipt(ReciptDataModel reciptDataModel,  Bitmap bitmap, boolean isPrintSignatureRequired, TYPE type){
		
		this.reciptDataModel = reciptDataModel;
		this.isPrintSignatureRequired = isPrintSignatureRequired;
		
		if (bitmap != null) {
			
			bitmapBytes = convertBitmap(bitmap, mswipeTargetWidth, 160);
		}else{
			
			this.isPrintSignatureRequired = false;
		}
		
		if (type == TYPE.CARD) {
			return genCardSaleReceipt();
		}else if(type == TYPE.EMI){
			return genEmiSaleReceipt();
		}else if(type == TYPE.CASH){
			return genCashSaleReceipt();
		}else{
			return null;
		}
	}

	private  byte[] genCardSaleReceipt() {
		
		if (ApplicationData.IS_DEBUGGING_ON)
			Logs.v("", log_tab + " genCradSaleReceipt " , true, true);
		
		try {
			
			
		    String strTipLbl = "TIP AMOUNT:    "; 
		    if(reciptDataModel.isConvenceFeeEnable.equalsIgnoreCase("true")){
		    	strTipLbl = "CONVENIENCE FEE:";
		    }
		    
		    if (ApplicationData.IS_DEBUGGING_ON)
				Logs.v(ApplicationData.packName, log_tab + " isConvenceFeeEnable " + reciptDataModel.isConvenceFeeEnable + strTipLbl, true, true);
		    
		    
			String address = "";
	
			try {
				
				address = (Html.fromHtml(reciptDataModel.merchantAdd)).toString();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				address = reciptDataModel.merchantAdd;
			}
			
			double tipamount = 0.00;
			if (reciptDataModel.tipAmount.length() > 0 ) {				
				tipamount = Double.parseDouble(reciptDataModel.tipAmount);
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(INIT);
			baos.write(POWER_ON);
				
			baos.write(EMPHASIZE_OFF);
			baos.write(ALIGN_CENTER);
			
			baos.write(FONT_SIZE_1);
			baos.write(FONT_8X12);
			baos.write(reciptDataModel.bankName.getBytes());
			baos.write(NEW_LINE);
			
			baos.write(FONT_SIZE_1);
			baos.write(FONT_5X12);
			baos.write(reciptDataModel.merchantName.getBytes());
			baos.write(NEW_LINE);	
			
			baos.write(FONT_SIZE_1);
			baos.write(FONT_5X12);
			baos.write(address.getBytes());
			baos.write(NEW_LINE);	
			baos.write(NEW_LINE);
			
			baos.write(FONT_SIZE_1);
			baos.write(FONT_5X12);
			baos.write(reciptDataModel.dateTime.getBytes());
			baos.write(NEW_LINE);	
			baos.write(NEW_LINE);
			
			String mMidTid = "MID:" + reciptDataModel.mId + "   TID:"+ reciptDataModel.tId;
			String mBatchInvoide = "BATCH NO:" +reciptDataModel.batchNo + "   INVOICE NO:"+reciptDataModel.invoiceNo;

			baos.write(ALIGN_LEFT);
			baos.write(FONT_SIZE_0);
			baos.write(FONT_10X18);
			baos.write(mMidTid.getBytes());
			baos.write(NEW_LINE);
			baos.write(mBatchInvoide.getBytes());
			baos.write(NEW_LINE);
			baos.write(("REF. NO.:" + reciptDataModel.refNo).getBytes());
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			
			if (reciptDataModel.cashAmount.length() > 0 && reciptDataModel.tipAmount.length()<= 0 && reciptDataModel.baseAmount.length()<= 0) {
				
				reciptDataModel.saleType = "Cash Only";
				
			}

			baos.write(ALIGN_CENTER);
			baos.write(FONT_SIZE_1);
			baos.write(FONT_8X12);
			baos.write(reciptDataModel.saleType.getBytes());
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			
			baos.write(ALIGN_LEFT);
			
			baos.write(FONT_SIZE_0);
			baos.write(FONT_10X18);
			baos.write(("CARD NO:" + reciptDataModel.cardNo+" "+reciptDataModel.trxType).getBytes());
			baos.write(NEW_LINE);
			baos.write(("CARD TYPE:" + reciptDataModel.cardType).getBytes());
			baos.write(("  EXP DT:" + reciptDataModel.expDate).getBytes());
			baos.write(NEW_LINE);
			
			
			if (reciptDataModel.cashAmount.length() > 0 && reciptDataModel.baseAmount.length() > 0) {
				
				baos.write(("SALE AMT:   " + reciptDataModel.currency + " " +reciptDataModel.baseAmount).getBytes());
				baos.write(NEW_LINE);
				baos.write(("CASH PAID:    " + reciptDataModel.currency + " " +reciptDataModel.cashAmount).getBytes());
				baos.write(NEW_LINE);
				
				if (reciptDataModel.tipAmount.length() > 0 && tipamount >0) {
					
					baos.write((strTipLbl + reciptDataModel.currency + " " +reciptDataModel.tipAmount).getBytes());
					baos.write(NEW_LINE);
					
				}
				
				baos.write(("-------------------------------").getBytes());
				baos.write(NEW_LINE);
				baos.write(("TOTAL AMOUNT:   " + reciptDataModel.currency + " " +String.format("%.2f",(Double.parseDouble(reciptDataModel.totalAmount)))).getBytes());
				baos.write(NEW_LINE);
				
			}else if (reciptDataModel.cashAmount.length() > 0 && reciptDataModel.baseAmount.length()<= 0) {
				
				if (reciptDataModel.tipAmount.length() > 0 && tipamount >0) {
					
					baos.write(("SALE AMT:   " + reciptDataModel.currency + " 0.00").getBytes());
					baos.write(NEW_LINE);
					baos.write(("CASH PAID:    " + reciptDataModel.currency + " " +reciptDataModel.cashAmount).getBytes());
					baos.write(NEW_LINE);
					baos.write((strTipLbl + reciptDataModel.currency + " " +reciptDataModel.tipAmount).getBytes());
					baos.write(NEW_LINE);
					baos.write(("-------------------------------").getBytes());
					baos.write(NEW_LINE);
					baos.write(("TOTAL AMOUNT:   " + reciptDataModel.currency + " " +reciptDataModel.totalAmount).getBytes());
					baos.write(NEW_LINE);
					
				}else{
				
					baos.write(("CASH PAID:    " + reciptDataModel.currency + " " +reciptDataModel.cashAmount).getBytes());
					baos.write(NEW_LINE);
				
				}
				
			}else{
				
				if (reciptDataModel.tipAmount.length() > 0 && tipamount >0) {
					
					baos.write(("BASE AMOUNT:    " + reciptDataModel.currency + " " + reciptDataModel.baseAmount).getBytes());
					baos.write(NEW_LINE);
					baos.write((strTipLbl + reciptDataModel.currency + " " +reciptDataModel.tipAmount).getBytes());
					baos.write(NEW_LINE);
					baos.write(("-------------------------------").getBytes());
					baos.write(NEW_LINE);
					baos.write(("TOTAL AMOUNT:   " + reciptDataModel.currency + " " +reciptDataModel.totalAmount).getBytes());
					baos.write(NEW_LINE);
					
					
				}else{
					
					baos.write(("TOTAL AMOUNT:   " + reciptDataModel.currency + " " +reciptDataModel.totalAmount).getBytes());
					baos.write(NEW_LINE);
				}
			}
			
			
			if (reciptDataModel.trxType.equalsIgnoreCase("Chip")) {
				
				baos.write(("TC:" + reciptDataModel.certif).getBytes());
				baos.write(NEW_LINE);
				baos.write(("APPLICATION IDENTIFIER:" + reciptDataModel.appId).getBytes());
				baos.write(NEW_LINE);
				baos.write(("APPLICATION NAME:" + reciptDataModel.appName).getBytes());
				baos.write(NEW_LINE);
				
			}
			
			baos.write(NEW_LINE);
			
			baos.write(FONT_SIZE_1);
			baos.write(FONT_5X12);
			baos.write(("APPR CD:" + reciptDataModel.authCode).getBytes());
			baos.write((" RREF NUM:" + reciptDataModel.rrNo).getBytes());
			baos.write(NEW_LINE);
			baos.write(("DATE/TIME:" + reciptDataModel.dateTime).getBytes());
			baos.write(NEW_LINE);
			baos.write(("CARD NO:" + reciptDataModel.cardNo+" "+reciptDataModel.trxType).getBytes());
			baos.write(NEW_LINE);
			baos.write(("EXP DT:" + reciptDataModel.expDate).getBytes());
			baos.write(NEW_LINE);
			
			String strAmtValue = reciptDataModel.currency + " " +reciptDataModel.totalAmount;
			String strAmtText = "    ("+reciptDataModel.cardType+" - "+reciptDataModel.trxType+")";

			baos.write(FONT_SIZE_1);
			baos.write(FONT_8X12);
			baos.write(("AMT:" + strAmtValue ).getBytes());

			baos.write(FONT_SIZE_1);
			baos.write(FONT_5X12);
			baos.write(strAmtText.getBytes());
			
			if (reciptDataModel.trxType.equalsIgnoreCase("Chip")) {
				
				baos.write(NEW_LINE);
				baos.write(("APP ID:" + reciptDataModel.appId).getBytes());
				baos.write(("   APP NAME:" + reciptDataModel.appName).getBytes());
				baos.write(NEW_LINE);
				baos.write(("TVR:" + reciptDataModel.tvr.trim()).getBytes());
				baos.write((" TSI:" + reciptDataModel.tsi.trim()).getBytes());
				
			}
			
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			
			if (reciptDataModel.isPinVarifed.equalsIgnoreCase("true")) {
				
				baos.write(FONT_SIZE_1);
				baos.write(FONT_10X18);
				baos.write(EMPHASIZE_ON);
				baos.write(("Pin Verified Ok").getBytes());
				baos.write(EMPHASIZE_OFF);
				baos.write(NEW_LINE);
				
			}else{
				
				if (isPrintSignatureRequired) {
					

					
					for(int j = 0; j < bitmapBytes.length / mswipeTargetWidth; ++j) {
						baos.write(hexToByteArray("1B2A00"));
						baos.write((byte)mswipeTargetWidth);
						baos.write((byte)(mswipeTargetWidth >> 8));
						byte[] temp = new byte[mswipeTargetWidth];
						System.arraycopy(bitmapBytes, j * mswipeTargetWidth, temp, 0, temp.length);
						baos.write(temp);
						baos.write(NEW_LINE);
					}
					
				
					
				}else{
					
					baos.write(FONT_SIZE_1);
					baos.write(FONT_10X18);
					baos.write(EMPHASIZE_ON);
					baos.write(("               ").getBytes());
					baos.write(NEW_LINE);
					baos.write(EMPHASIZE_OFF);
					baos.write(NEW_LINE);
					baos.write(NEW_LINE);
					baos.write(FONT_SIZE_0);
					baos.write(FONT_8X12);
					baos.write("Signature".getBytes());
					baos.write(NEW_LINE);
					baos.write(ALIGN_CENTER);
					baos.write(FONT_SIZE_0);
					baos.write(FONT_8X12);
					baos.write("--------------------------------------".getBytes());
					baos.write(NEW_LINE);
				}
				
				
			}
			
			baos.write(NEW_LINE);
			
			baos.write(ALIGN_LEFT);
			baos.write(FONT_SIZE_1);
			baos.write(FONT_8X12);
			baos.write(reciptDataModel.cardHolderName.getBytes());
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			
			baos.write(ALIGN_CENTER);
			baos.write(FONT_SIZE_0);
			baos.write(FONT_8X12);
			baos.write("I AGREE TO PAY THE ABOVE TOTAL AMOUNT".getBytes());
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write("ACCORDING TO THE CARD ISSUER AGREEMENT".getBytes());
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			
			baos.write(FONT_SIZE_0);
			baos.write(FONT_10X18);
			baos.write("*** CUSTOMER COPY ***".getBytes());
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);

			baos.write(FONT_SIZE_1);
			baos.write(FONT_5X12);
			baos.write(ALIGN_RIGHT);
			baos.write(("Version No :"+reciptDataModel.appVersion).getBytes());
			baos.write(NEW_LINE);
			
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			
			baos.write(POWER_OFF);

			
			return baos.toByteArray();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	private  byte[] genEmiSaleReceipt() {		
		
		if (ApplicationData.IS_DEBUGGING_ON)
			Logs.v("", log_tab + " genEmiSaleReceipt " , true, true);

		try {
			
			StringBuilder cardNumMask = new StringBuilder();
	     	
	     	if ((reciptDataModel.firstDigitsOfCard).length() >= 6) {
				
	     		cardNumMask.append((reciptDataModel.firstDigitsOfCard).substring(0,4)+" "+(reciptDataModel.firstDigitsOfCard).substring(4,6));
			}
	     	
	     	if (reciptDataModel.cardNo.length() >= 8) {
				
	     		cardNumMask.append((reciptDataModel.cardNo).substring(8,reciptDataModel.cardNo.length()));
			}
	     	
	     	reciptDataModel.cardNo = cardNumMask.toString();
	     	
			if (reciptDataModel.emiPerMonthAmount.length() <= 0) {
				reciptDataModel.emiPerMonthAmount = "0.00";
			}
			
			if (reciptDataModel.total_Pay_Amount.length() <= 0) {
				reciptDataModel.total_Pay_Amount = "0.00";
			}
			
			double emiPerMonthAmountDouble = Double.parseDouble(reciptDataModel.emiPerMonthAmount.toString());
			double totalPayAmountDouble = Double.parseDouble(reciptDataModel.total_Pay_Amount.toString());
			
			String address = "";
	
			try {
				
				address = (Html.fromHtml(reciptDataModel.merchantAdd)).toString();
			} catch (Exception e) {
				// TODO: handle exception
				address = reciptDataModel.merchantAdd;
			}
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(INIT);
			baos.write(POWER_ON);
				
			baos.write(EMPHASIZE_OFF);
			baos.write(ALIGN_CENTER);
			
			baos.write(FONT_SIZE_1);
			baos.write(FONT_8X12);
			baos.write(reciptDataModel.bankName.getBytes());
			baos.write(NEW_LINE);
			
			baos.write(FONT_SIZE_1);
			baos.write(FONT_5X12);
			baos.write(reciptDataModel.merchantName.getBytes());
			baos.write(NEW_LINE);	
			
			baos.write(FONT_SIZE_1);
			baos.write(FONT_5X12);
			baos.write(address.getBytes());
			baos.write(NEW_LINE);	
			baos.write(NEW_LINE);

			String mDate = reciptDataModel.dateTime.split(" ")[0];
			String mTime = reciptDataModel.dateTime.split(" ")[1];
			
			try {
				
				String mAmPm = reciptDataModel.dateTime.split(" ")[2];
				
				if (mAmPm.length() > 0) {
					mTime = mTime + " " + mAmPm;
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
			baos.write(ALIGN_LEFT);

			baos.write(FONT_SIZE_0);
			baos.write(FONT_10X18);
			
			baos.write(("Date: "+mDate).getBytes());
			baos.write(NEW_LINE);	

			baos.write(("Time: "+mTime).getBytes());
			baos.write(NEW_LINE);	
			
			baos.write(("MID: "+reciptDataModel.mId).getBytes());
			baos.write(NEW_LINE);	
			
			baos.write(("TID: "+reciptDataModel.tId).getBytes());
			baos.write(NEW_LINE);
			
			baos.write(("Batch No.: "+reciptDataModel.batchNo).getBytes());
			baos.write(NEW_LINE);
			
			baos.write(("Inv.No.: "+reciptDataModel.refNo).getBytes());
			baos.write(NEW_LINE);
			
			baos.write(("Bill No.: "+reciptDataModel.billNo).getBytes());
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);

			baos.write(ALIGN_CENTER);
			baos.write(FONT_SIZE_1);
			baos.write(FONT_8X12);
			baos.write(("Sale").getBytes());
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			
			baos.write(ALIGN_LEFT);
			baos.write(FONT_SIZE_0);
			baos.write(FONT_10X18);
			
			baos.write(("CARD NO:" + reciptDataModel.cardNo+" "+reciptDataModel.trxType).getBytes());
			baos.write(NEW_LINE);
			
			baos.write(("Exp Date:" + reciptDataModel.expDate).getBytes());
			baos.write(NEW_LINE);
			
			baos.write(("Card Type:" + reciptDataModel.cardType).getBytes());
			baos.write(NEW_LINE);
			
			baos.write(("Tnx ID: " + reciptDataModel.stan).getBytes());
			baos.write(NEW_LINE);	
			
			baos.write(("Appr.Code: " + reciptDataModel.authCode).getBytes());
			baos.write(NEW_LINE);	
			
			baos.write(("RRN: " + reciptDataModel.rrNo).getBytes());
			baos.write(NEW_LINE);	
			
			baos.write(("-------------------------------").getBytes());
			baos.write(NEW_LINE);
		
			
			if (reciptDataModel.trxType.equalsIgnoreCase("Chip")) {
				
				baos.write(("TC: " + reciptDataModel.certif).getBytes());
				baos.write(NEW_LINE);
				
				baos.write(("APP. ID: " + reciptDataModel.appId).getBytes());
				baos.write(NEW_LINE);
				
				baos.write(("APP. Name: " + reciptDataModel.appName).getBytes());
				baos.write(NEW_LINE);
				
				baos.write(("TVR: " + reciptDataModel.tvr.trim()).getBytes());
				baos.write((" TSI: " + reciptDataModel.tsi.trim()).getBytes());
				baos.write(NEW_LINE);	
				
				baos.write(("-------------------------------").getBytes());
				baos.write(NEW_LINE);
				
			}

			
			baos.write(("EMI Txn ID: " + reciptDataModel.billNo).getBytes());
			baos.write(NEW_LINE);
			
			baos.write(("Tenure: " + reciptDataModel.noOfEmi + " Months").getBytes());
			baos.write(NEW_LINE);
			
			baos.write(("Card Issuer: " + reciptDataModel.cardIssuer).getBytes());
			baos.write(NEW_LINE);
			
			
			baos.write(FONT_SIZE_1);
			baos.write(FONT_5X12);
			baos.write(("Base Amt: ").getBytes());
			baos.write(FONT_SIZE_1);
			baos.write(FONT_8X12);
			baos.write((reciptDataModel.currency + " " + reciptDataModel.baseAmount).getBytes());
			baos.write(NEW_LINE);
			
			baos.write(FONT_SIZE_1);
			baos.write(FONT_5X12);
			baos.write(("Applicable Intr. Rate (P.A.) : " + reciptDataModel.interestRate + "%").getBytes());
			baos.write(NEW_LINE);	
			
			baos.write(FONT_SIZE_1);
			baos.write(FONT_5X12);
			baos.write(("EMI Amt: ").getBytes());
			baos.write(FONT_SIZE_1);
			baos.write(FONT_8X12);
			baos.write((reciptDataModel.currency + " " + df.format(emiPerMonthAmountDouble).toString()).getBytes());
			baos.write(NEW_LINE);
			
			baos.write(FONT_SIZE_1);
			baos.write(FONT_5X12);
			baos.write(("Total Amt Payable: ").getBytes());
			baos.write(NEW_LINE);
			baos.write(FONT_SIZE_1);
			baos.write(FONT_8X12);
			baos.write((reciptDataModel.currency + " " + df.format(totalPayAmountDouble).toString()).getBytes());
			baos.write(NEW_LINE);
			
			baos.write(FONT_SIZE_0);
			baos.write(FONT_10X18);			
			baos.write(("-------------------------------").getBytes());
			baos.write(NEW_LINE);
			
			baos.write(ALIGN_CENTER);
			
			baos.write("CUSTOMER CONSENT FOR EMI".getBytes());
			baos.write(NEW_LINE);
			
			baos.write(ALIGN_LEFT);
			
			baos.write("1. I have been offered the choice of normal as well as EMI for this purchase and I have chosen EMI".getBytes());
			baos.write(NEW_LINE);
			baos.write("2. I have fully understood and accept the terms of EMI scheme and applicable charges mentioned in this charge-slip".getBytes());
			baos.write(NEW_LINE);
			baos.write("3. EMI conversion subject to banks discretion".getBytes());
			baos.write(NEW_LINE);
			baos.write("4. Service Tax applicable on Interest and Processing fees".getBytes());
			baos.write(NEW_LINE);
			
			baos.write(("-------------------------------").getBytes());
			baos.write(NEW_LINE);
			
			baos.write(("Base Amt: " + reciptDataModel.currency + " " + reciptDataModel.baseAmount).getBytes());
			baos.write(NEW_LINE);	
			
			baos.write(("Total Amt Payable (Incl. Interest): " + reciptDataModel.currency + " " + df.format(totalPayAmountDouble).toString()).getBytes());
			
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			
			if (/*reciptDataModel.isPinVarifed.equalsIgnoreCase("true")*/ false) {
				
				baos.write(FONT_SIZE_1);
				baos.write(FONT_10X18);
				baos.write(EMPHASIZE_ON);
				baos.write(("Pin Verified Ok").getBytes());
				baos.write(EMPHASIZE_OFF);
				baos.write(NEW_LINE);
				baos.write(NEW_LINE);
				
				
			}else{
				
				if (isPrintSignatureRequired) {
					
					for(int j = 0; j < bitmapBytes.length / mswipeTargetWidth; ++j) {
						baos.write(hexToByteArray("1B2A00"));
						baos.write((byte)mswipeTargetWidth);
						baos.write((byte)(mswipeTargetWidth >> 8));
						byte[] temp = new byte[mswipeTargetWidth];
						System.arraycopy(bitmapBytes, j * mswipeTargetWidth, temp, 0, temp.length);
						baos.write(temp);
						baos.write(NEW_LINE);
					}
					
				}else{
					
					baos.write(FONT_SIZE_1);
					baos.write(FONT_10X18);
					baos.write(EMPHASIZE_ON);
					baos.write(("               ").getBytes());
					baos.write(NEW_LINE);
					baos.write(EMPHASIZE_OFF);
					baos.write(NEW_LINE);
					baos.write(NEW_LINE);
					baos.write(FONT_SIZE_0);
					baos.write(FONT_8X12);
					baos.write("Signature".getBytes());
					baos.write(NEW_LINE);
				}
				
				baos.write(ALIGN_CENTER);
				baos.write(FONT_SIZE_0);
				baos.write(FONT_8X12);
				baos.write("--------------------------------------".getBytes());
				baos.write(NEW_LINE);
				baos.write(NEW_LINE);
				
			}
			
			baos.write(ALIGN_LEFT);
			baos.write(FONT_SIZE_1);
			baos.write(FONT_8X12);
			baos.write(reciptDataModel.cardHolderName.getBytes());
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			
			baos.write(ALIGN_CENTER);
			baos.write(FONT_SIZE_0);
			baos.write(FONT_8X12);
			baos.write("--------------------------------------".getBytes());
			baos.write(NEW_LINE);
			
			baos.write(ALIGN_CENTER);
			baos.write(FONT_SIZE_0);
			baos.write(FONT_8X12);
			baos.write("I AGREE TO PAY AS PER CARD ISSUER".getBytes());
			baos.write(NEW_LINE);
			baos.write("AGREEMENT".getBytes());
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			
			baos.write(FONT_SIZE_0);
			baos.write(FONT_10X18);
			baos.write("***  Customer Copy ***".getBytes());
			baos.write(NEW_LINE);
			
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);

			baos.write(POWER_OFF);

			
			return baos.toByteArray();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	private  byte[] genCashSaleReceipt() {
		
		if (ApplicationData.IS_DEBUGGING_ON)
			Logs.v("", log_tab + " genCashSaleReceipt " , true, true);
		
		try {
			
			String address = "";
	
			try {
				
				address = (Html.fromHtml(reciptDataModel.merchantAdd)).toString();
			} catch (Exception e) {
				// TODO: handle exception
				address = reciptDataModel.merchantAdd;
			}
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(INIT);
			baos.write(POWER_ON);
				
			baos.write(EMPHASIZE_OFF);
			baos.write(ALIGN_CENTER);
			
			baos.write(FONT_SIZE_1);
			baos.write(FONT_5X12);
			baos.write(reciptDataModel.merchantName.getBytes());
			baos.write(NEW_LINE);	
			
			baos.write(FONT_SIZE_1);
			baos.write(FONT_5X12);
			baos.write(address.getBytes());
			baos.write(NEW_LINE);	
			baos.write(NEW_LINE);
			
			baos.write(FONT_SIZE_1);
			baos.write(FONT_5X12);
			baos.write(reciptDataModel.dateTime.getBytes());
			baos.write(NEW_LINE);	
			baos.write(NEW_LINE);
			
			String mMidTid = "MID:" + reciptDataModel.mId + "  TID:"+ reciptDataModel.tId;
			String mInvoide = "INVOICE NO:" +reciptDataModel.invoiceNo;
			String mRef = "REF. NO.:"+reciptDataModel.voucherNo;

			baos.write(ALIGN_LEFT);
			baos.write(FONT_SIZE_0);
			baos.write(FONT_10X18);
			baos.write(mMidTid.getBytes());
			baos.write(NEW_LINE);
			baos.write(mInvoide.getBytes());
			baos.write(NEW_LINE);
			baos.write(mRef.getBytes());
			baos.write(NEW_LINE);
			
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);

			baos.write(ALIGN_CENTER);
			baos.write(FONT_SIZE_1);
			baos.write(FONT_8X12);
			baos.write(reciptDataModel.trxType.getBytes());
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			
			baos.write(ALIGN_LEFT);
			baos.write(FONT_SIZE_0);
			baos.write(FONT_10X18);
			baos.write(("TOTAL AMOUNT:   ").getBytes());
			
			baos.write(FONT_SIZE_1);
			baos.write(FONT_8X12);
			baos.write((reciptDataModel.currency + " " +reciptDataModel.totalAmount).getBytes());
			
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			
			if (isPrintSignatureRequired) {
				
				for(int j = 0; j < bitmapBytes.length / mswipeTargetWidth; ++j) {
					baos.write(hexToByteArray("1B2A00"));
					baos.write((byte)mswipeTargetWidth);
					baos.write((byte)(mswipeTargetWidth >> 8));
					byte[] temp = new byte[mswipeTargetWidth];
					System.arraycopy(bitmapBytes, j * mswipeTargetWidth, temp, 0, temp.length);
					baos.write(temp);
					baos.write(NEW_LINE);
				}
				
			}else{
				
				baos.write(FONT_SIZE_1);
				baos.write(FONT_10X18);
				baos.write(EMPHASIZE_ON);
				baos.write(("               ").getBytes());
				baos.write(NEW_LINE);
				baos.write(EMPHASIZE_OFF);
				baos.write(NEW_LINE);
				baos.write(NEW_LINE);
				baos.write(FONT_SIZE_0);
				baos.write(FONT_8X12);
				baos.write("Signature".getBytes());
				baos.write(NEW_LINE);
			}
			
			baos.write(ALIGN_CENTER);
			baos.write(FONT_SIZE_0);
			baos.write(FONT_8X12);
			baos.write("--------------------------------------".getBytes());
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			
			baos.write(ALIGN_CENTER);
			baos.write(FONT_SIZE_0);
			baos.write(FONT_8X12);
			baos.write("I AGREE TO PAY THE ABOVE TOTAL AMOUNT".getBytes());
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write("ACCORDING TO THE CARD ISSUER AGREEMENT".getBytes());
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			
			baos.write(FONT_SIZE_0);
			baos.write(FONT_10X18);
			baos.write("*** CUSTOMER COPY ***".getBytes());
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);

			baos.write(FONT_SIZE_1);
			baos.write(FONT_5X12);
			baos.write(ALIGN_RIGHT);
			baos.write(("Version No :"+reciptDataModel.appVersion).getBytes());
			baos.write(NEW_LINE);
			
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);

			baos.write(POWER_OFF);
			
			return baos.toByteArray();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	private  byte[] hexToByteArray(String s) {
		if(s == null) {
			s = "";
		}
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		for(int i = 0; i < s.length() - 1; i += 2) {
			String data = s.substring(i, i + 2);
			bout.write(Integer.parseInt(data, 16));
		}
		return bout.toByteArray();
	}
	
	private  byte[] convertBitmap(Bitmap bitmap, int targetWidth, int threshold) {
		int targetHeight = (int)Math.round((double)targetWidth / (double)bitmap.getWidth() * (double)bitmap.getHeight());
		
		byte[] pixels = new byte[targetWidth * targetHeight];
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false);
		for(int j = 0; j < scaledBitmap.getHeight(); ++j) {
			for(int i = 0; i < scaledBitmap.getWidth(); ++i) {
				int pixel = scaledBitmap.getPixel(i, j);
				int alpha = (pixel >> 24) & 0xFF;
				int r = (pixel >> 16) & 0xFF;
				int g = (pixel >> 8) & 0xFF;
				int b = pixel & 0xFF;
				if(alpha < 50) {
					pixels[i + j * scaledBitmap.getWidth()] = 0;
				} else if((r + g + b) / 3 >= threshold) {
					pixels[i + j * scaledBitmap.getWidth()] = 0;
				} else {
					pixels[i + j * scaledBitmap.getWidth()] = 1;
				}
			}
		}
		
		byte[] output = new byte[scaledBitmap.getWidth() * (int)Math.ceil((double)scaledBitmap.getHeight() / (double)8)];
		
		for(int i = 0; i < scaledBitmap.getWidth(); ++i) {
			for(int j = 0; j < (int)Math.ceil((double)scaledBitmap.getHeight() / (double)8); ++j) {
				for(int n = 0; n < 8; ++n) {
					if(j * 8 + n < scaledBitmap.getHeight()) {
						output[i + j * scaledBitmap.getWidth()] |= pixels[i + (j * 8 + n) * scaledBitmap.getWidth()] << (7 - n);
					}
				}
			}
		}
		
		return output;
	}
	
	private  void checkReceipt(ReciptDataModel reciptDataModel) {
		
		try {
			
			if (ApplicationData.IS_DEBUGGING_ON) {
				
		
			if (reciptDataModel.emiPerMonthAmount.length() <= 0) {
				reciptDataModel.emiPerMonthAmount = "0.00";
			}
			
			if (reciptDataModel.total_Pay_Amount.length() <= 0) {
				reciptDataModel.total_Pay_Amount = "0.00";
			}
			
			double emiPerMonthAmountDouble = Double.parseDouble(reciptDataModel.emiPerMonthAmount.toString());
			double totalPayAmountDouble = Double.parseDouble(reciptDataModel.total_Pay_Amount.toString());
			 
			
			String mDate = reciptDataModel.dateTime.split(" ")[0];
			String mTime = reciptDataModel.dateTime.split(" ")[1];
			try {
				
				String mAmPm = reciptDataModel.dateTime.split(" ")[2];
				
				if (mAmPm.length() > 0) {
					mTime = mTime + " " + mAmPm;
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
			String data = 
					"bankName " + reciptDataModel.bankName + "\n" +
					"merchantName " + reciptDataModel.merchantName + "\n" +
					"merchantAdd " + reciptDataModel.merchantAdd + "\n" +
					"dateTime " + reciptDataModel.dateTime + "\n" +
					"Date " + mDate + "\n" +
					"Time " + mTime + "\n" +
					"mId " + reciptDataModel.mId + "\n" +
					"tId " + reciptDataModel.tId + "\n" +
					"batchNo " + reciptDataModel.batchNo + "\n" +
					"invoiceNo " + reciptDataModel.invoiceNo + "\n" +
					"refNo " + reciptDataModel.refNo + "\n" +
					"saleType " + reciptDataModel.saleType + "\n" 	+
					"trxType " + reciptDataModel.trxType + "\n" +
					"expDate " + reciptDataModel.expDate + "\n" +
					"emvSigExpDate " + reciptDataModel.emvSigExpDate + "\n" +
					"cardNo " + reciptDataModel.cardNo + "\n" +
					"cardType " + reciptDataModel.cardType + "\n" +
					"cardHolderName " + reciptDataModel.cardHolderName + "\n" +
					"currency " + reciptDataModel.currency + "\n" +
					"cashAmount " + reciptDataModel.cashAmount + "\n" +
					"baseAmount " + reciptDataModel.baseAmount + "\n" +
					"tipAmount " + reciptDataModel.tipAmount + "\n" +
					"totalAmount " + reciptDataModel.totalAmount + "\n" +
					"authCode " + reciptDataModel.authCode + "\n" +
					"rrNo  " + reciptDataModel.rrNo + "\n" +
					"certif " + reciptDataModel.certif + "\n" +
					"appId " + reciptDataModel.appId + "\n" +
					"appName " + reciptDataModel.appName + "\n" +
					"tvr " + reciptDataModel.tvr + "\n" +
					"tsi " + reciptDataModel.tsi + "\n" +
					"appVersion " + reciptDataModel.appVersion + "\n" +
					"isPinVarifed " + reciptDataModel.isPinVarifed + "\n" +
					"stan " + reciptDataModel.stan + "\n"+ 
					"emiPerMonthAmount " + df.format(emiPerMonthAmountDouble).toString() + "\n"+ 
					"totalPayAmount " + df.format(totalPayAmountDouble).toString() + "\n";
			
				Logs.v("", log_tab + " Receipt data " + data, true, true);
			
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}