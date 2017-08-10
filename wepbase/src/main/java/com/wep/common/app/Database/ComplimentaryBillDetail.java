/***************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	ComplimentaryBillDetail
 * 
 * Purpose			:	Represents ComplimentaryBillDetail table, takes care of 
 * 						initializing assigning and returning values of all the variables.
 * 
 * DateOfCreation	:	06-February-2013
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 ***************************************************************************/
package com.wep.common.app.Database;

public class ComplimentaryBillDetail {
	
	// Private variables
	String strComplimentaryReason;
	int iBillNumber;
	float fPaidAmount; 
	
	// Default Constructor
	public ComplimentaryBillDetail(){
		this.iBillNumber = 0;
		this.fPaidAmount = 0;
		this.strComplimentaryReason = "";
	}
	
	// Parameterized Constructor
	public ComplimentaryBillDetail(int BillNumber,float PaidAmount,String ComplimentaryReason){
		this.iBillNumber = BillNumber;
		this.fPaidAmount = PaidAmount;
		this.strComplimentaryReason = ComplimentaryReason;
	}
	
	// getting BillNumber
	public int getBillNumber(){
		return this.iBillNumber;
	}
	
	// getting PaidAmount
	public float getPaidAmount(){
		return this.fPaidAmount;
	}
	
	// getting ComplimentaryReason
	public String getComplimentaryReason(){
		return this.strComplimentaryReason;
	}
	
	// setting BillNumber
	public void setBillNumber(int BillNumber){
		this.iBillNumber = BillNumber;
	}
	
	// setting PaidAmount
	public void setPaidAmount(float PaidAmount){
		this.fPaidAmount = PaidAmount;
	}
	
	// setting ComplimentaryReason
	public void setComplimentaryReason(String ComplimentaryReason){
		this.strComplimentaryReason = ComplimentaryReason;
	}
}
