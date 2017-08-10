/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	PaymentReceipt
 * 
 * Purpose			:	Represents PaymentReceipt table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	16-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package com.wep.common.app.Database;

public class PaymentReceipt {
	
	// Private variables
	String strDate, strReason;
	int iBillType, iDescriptionId1, iDescriptionId2, iDescriptionId3;
	float fAmount;
	String DescriptionText;
	
	// Default constructor
	public PaymentReceipt(){
		
		this.strDate = "";
		this.strReason = "";
		this.iBillType = 0;
		this.iDescriptionId1 = 0;
		this.iDescriptionId2 = 0;
		this.iDescriptionId3 = 0;
		this.fAmount = 0;
		this.DescriptionText= "";
		
	}
	
	// Parameterized constructor
	public PaymentReceipt(String Date,String Reason,int BillType,String DescriptionText,int DescriptionId1,int DescriptionId2,int DescriptionId3,float Amount){
		
		this.strDate = Date;
		this.strReason = Reason;
		this.iBillType = BillType;
		this.iDescriptionId1 = DescriptionId1;
		this.iDescriptionId2 = DescriptionId2;
		this.iDescriptionId3 = DescriptionId3;
		this.fAmount = Amount;
		this.DescriptionText = DescriptionText;
			
	}

	public String getDescriptionText() {
		return DescriptionText;
	}

	public void setDescriptionText(String descriptionText) {
		DescriptionText = descriptionText;
	}

	// getting Date
	public String getDate(){
		return this.strDate;
	}
	
	// getting Reason
	public String getReason(){
		return this.strReason;
	}
	
	// getting BillType
	public int getBillType(){
		return this.iBillType;
	}
	
	// getting DescriptionId1
	public int getDescriptionId1(){
		return this.iDescriptionId1;
	}
	
	// getting DescriptionId2
	public int getDescriptionId2(){
		return this.iDescriptionId2;
	}
		
	// getting DescriptionId3
	public int getDescriptionId3(){
		return this.iDescriptionId3;
	}
		
	// getting Amount
	public float getAmount(){
		return this.fAmount;
	}
	
	// setting Date
	public void setDate(String Date){
		this.strDate = Date;
	}
	
	// setting Reason
	public void setReason(String Reason){
		this.strReason = Reason;
	}
		
	// setting BillType
	public void setBillType(int BillType){
		this.iBillType = BillType;
	}
		
	// setting DescriptionId1
	public void setDescriptionId1(int DescriptionId1){
		this.iDescriptionId1 = DescriptionId1;
	}
		
	// setting DescriptionId2
	public void setDescriptionId2(int DescriptionId2){
		this.iDescriptionId2 = DescriptionId2;
	}
			
	// setting DescriptionId3
	public void setDescriptionId3(int DescriptionId3){
		this.iDescriptionId3 = DescriptionId3;
	}
			
	// setting Amount
	public void setAmount(float Amount){
		this.fAmount = Amount;
	}

}
