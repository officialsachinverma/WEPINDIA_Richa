package com.mswipetech.wisepad.sdktest.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ReciptDataModel implements Parcelable{
	
	public String bankName= "";
	public String merchantName= "";
	public String merchantAdd= "";
	public String dateTime= "";
	public String mId= "";
	public String tId= "";
	public String batchNo= "";
	public String voucherNo= "";
	public String refNo= "";
	public String saleType= "";
	public String cardNo= "";
	public String trxType= "";
	public String cardType= "";
	public String expDate= "";
	public String emvSigExpDate= "";
	public String cardHolderName= "";
	public String currency= "";
	public String cashAmount= "";
	public String baseAmount= "";
	public String tipAmount= "";
	public String totalAmount= "";
	public String authCode= "";
	public String rrNo= "";
	public String certif= "";
	public String appId= "";
	public String appName= "";
	public String tvr= "";
	public String tsi= "";
	public String appVersion= "";
	public String isPinVarifed= "";
	public String stan= "";
	public String cardIssuer="";
	public String emiPerMonthAmount="";
	public String total_Pay_Amount = "";
	public String noOfEmi = "";
	public String interestRate = "";
	public String billNo = "";
	public String firstDigitsOfCard = "";
	
	public String isConvenceFeeEnable = "false";
	
	//for cash and bank

	public String invoiceNo= "";
	public String trxDate= "";
	public String trxImgDate= "";
	public String chequeDate= "";
	public String chequeNo= "";
	
	
	
	 public static final Parcelable.Creator<ReciptDataModel> CREATOR = new Creator<ReciptDataModel>() {  
	    	public ReciptDataModel createFromParcel(Parcel source) { 
	    		
	    		ReciptDataModel reciptDataModel = new ReciptDataModel();  
	    		reciptDataModel.bankName = source.readString(); 
	    		reciptDataModel.merchantName = source.readString(); 
	    		reciptDataModel.merchantAdd = source.readString(); 
	    		reciptDataModel.dateTime = source.readString(); 
	    		reciptDataModel.mId = source.readString(); 
	    		reciptDataModel.tId = source.readString(); 
	    		reciptDataModel.batchNo = source.readString(); 
	    		reciptDataModel.voucherNo = source.readString(); 
	    		reciptDataModel.refNo = source.readString(); 
	    		reciptDataModel.saleType = source.readString(); 
	    		reciptDataModel.cardNo = source.readString(); 
	    		reciptDataModel.trxType = source.readString(); 
	    		reciptDataModel.cardType = source.readString(); 
	    		reciptDataModel.expDate = source.readString(); 
	    		reciptDataModel.emvSigExpDate = source.readString(); 
	    		reciptDataModel.cardHolderName = source.readString(); 
	    		reciptDataModel.currency = source.readString(); 
	    		reciptDataModel.cashAmount = source.readString(); 
	    		reciptDataModel.baseAmount = source.readString(); 
	    		reciptDataModel.tipAmount = source.readString(); 
	    		reciptDataModel.totalAmount = source.readString(); 
	    		reciptDataModel.authCode = source.readString(); 
	    		reciptDataModel.rrNo = source.readString(); 
	    		reciptDataModel.certif = source.readString(); 
	    		reciptDataModel.appId = source.readString(); 
	    		reciptDataModel.appName = source.readString(); 
	    		reciptDataModel.tvr = source.readString(); 
	    		reciptDataModel.tsi = source.readString(); 
	    		reciptDataModel.appVersion = source.readString(); 
	    		reciptDataModel.isPinVarifed = source.readString(); 
	    		reciptDataModel.stan = source.readString(); 
	    		reciptDataModel.cardIssuer = source.readString();
	    		reciptDataModel.emiPerMonthAmount = source.readString();
	    		reciptDataModel.total_Pay_Amount = source.readString(); 
	    		reciptDataModel.noOfEmi  = source.readString(); 
	    		reciptDataModel.interestRate  = source.readString(); 
	    		reciptDataModel.billNo  = source.readString(); 
	    		reciptDataModel.firstDigitsOfCard  = source.readString(); 
	    		reciptDataModel.isConvenceFeeEnable  = source.readString(); 
	    		
	    		//for cash and bank
	    		
	    		reciptDataModel.invoiceNo = source.readString(); 
	    		reciptDataModel.trxDate = source.readString(); 
	    		reciptDataModel.trxImgDate = source.readString(); 
	    		reciptDataModel.chequeDate = source.readString(); 
	    		reciptDataModel.chequeNo = source.readString(); 
	    			
	    		return reciptDataModel;  
	    		
	    	}  
	    	public ReciptDataModel[] newArray(int size) {  
	    		
	    		return new ReciptDataModel[size];  
	    	}  
	    };  
	    
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		
	    parcel.writeString(bankName); 
		parcel.writeString(merchantName); 
		parcel.writeString(merchantAdd); 
		parcel.writeString(dateTime); 
		parcel.writeString(mId); 
		parcel.writeString(tId); 
		parcel.writeString(batchNo); 
		parcel.writeString(voucherNo); 
		parcel.writeString(refNo); 
		parcel.writeString(saleType); 
		parcel.writeString(cardNo); 
		parcel.writeString(trxType); 
		parcel.writeString(cardType); 
		parcel.writeString(expDate); 
		parcel.writeString(emvSigExpDate); 
		parcel.writeString(cardHolderName); 
		parcel.writeString(currency); 
		parcel.writeString(cashAmount); 
		parcel.writeString(baseAmount); 
		parcel.writeString(tipAmount); 
		parcel.writeString(totalAmount); 
		parcel.writeString(authCode); 
		parcel.writeString(rrNo); 
		parcel.writeString(certif); 
		parcel.writeString(appId); 
		parcel.writeString(appName); 
		parcel.writeString(tvr); 
		parcel.writeString(tsi); 
		parcel.writeString(appVersion); 
		parcel.writeString(isPinVarifed); 
		parcel.writeString(stan); 
		parcel.writeString(cardIssuer);
		parcel.writeString(emiPerMonthAmount);
		parcel.writeString(total_Pay_Amount); 
		parcel.writeString(noOfEmi ); 
		parcel.writeString(interestRate); 
		parcel.writeString(billNo ); 
		parcel.writeString(firstDigitsOfCard); 
		
		parcel.writeString(isConvenceFeeEnable);
		
		//for cash and bank

		parcel.writeString(invoiceNo); 
		parcel.writeString(trxDate); 
		parcel.writeString(trxImgDate); 
		parcel.writeString(chequeDate); 
		parcel.writeString(chequeNo); 
		
		
	}
}

/*public class ReciptDataModel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public String bankName= "";
	public String merchantName= "";
	public String merchantAdd= "";
	public String dateTime= "";
	public String mId= "";
	public String tId= "";
	public String batchNo= "";
	public String voucherNo= "";
	public String refNo= "";
	public String saleType= "";
	public String cardNo= "";
	public String trxType= "";
	public String cardType= "";
	public String expDate= "";
	public String emvSigExpDate= "";
	public String cardHolderName= "";
	public String currency= "";
	public String cashAmount= "";
	public String baseAmount= "";
	public String tipAmount= "";
	public String totalAmount= "";
	public String authCode= "";
	public String rrNo= "";
	public String certif= "";
	public String appId= "";
	public String appName= "";
	public String tvr= "";
	public String tsi= "";
	public String appVersion= "";
	public String isPinVarifed= "";
	public String stan= "";
	public String cardIssuer="";
	public String emiPerMonthAmount="";
	public String total_Pay_Amount = "";
	public String noOfEmi = "";
	public String interestRate = "";
	public String billNo = "";
	public String firstDigitsOfCard = "";
	
	//for cash and bank

	public String invoiceNo= "";
	public String trxDate= "";
	public String trxImgDate= "";
	public String chequeDate= "";
	public String chequeNo= "";
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		
		String data = 
				" bank name " + bankName +
				" merhant name "+merchantName+
				" customer name "+ cardHolderName +
				" card number "+cardNo;
		
		return data;
	}
}*/