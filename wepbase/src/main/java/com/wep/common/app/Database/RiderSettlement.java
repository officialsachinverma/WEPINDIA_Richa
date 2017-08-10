/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	RiderSettlement
 * 
 * Purpose			:	Represents RiderSettlement table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	16-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package com.wep.common.app.Database;

public class RiderSettlement {
	
	// Private variable
	int iBillNumber, iEmployeeId, iTotalItems, iCustId;
	float fBillAmount, fDeliveryCharge, fPettyCash, fSettledAmount;
	
	// Default constructor
	public RiderSettlement(){
		
		this.iBillNumber = 0;
		this.iEmployeeId = 0;
		this.iTotalItems = 0;
		this.fDeliveryCharge = 0;
		this.fBillAmount = 0;
		this.fPettyCash = 0;
		this.fSettledAmount = 0;
		this.iCustId = 0;
	}
	
	// Parameterized constructor
	public RiderSettlement(int BillNumber,int EmployeeId,int TotalItems,
			float BillAmount,float DeliveryCharge,float PettyCash,float SettledAmount, int CustId){
			
		this.iBillNumber = BillNumber;
		this.iEmployeeId = EmployeeId;
		this.iTotalItems = TotalItems;
		this.fBillAmount = BillAmount;
		this.fDeliveryCharge = DeliveryCharge;
		this.fPettyCash = PettyCash;
		this.fSettledAmount = SettledAmount;
        this.iCustId = CustId;
	}
	
	// getting BillNumber
	public int getBillNumber(){
		return this.iBillNumber;
	}
	
	// getting EmployeeId
	public int getEmployeeId(){
		return this.iEmployeeId;
	}
	
	// getting TotalItems
	public int getTotalItems(){
		return this.iTotalItems;
	}
	
	// getting BillAmount
	public float getBillAmount(){
		return this.fBillAmount;
	}
	
	// getting DeliveryCharge
	public float getDeliveryCharge(){
		return this.fDeliveryCharge;
	}
	
	// getting PettyCash
	public float getPettyCash(){
		return this.fPettyCash;
	}
		
	// getting SettledAmount
	public float getSettledAmount(){
		return this.fSettledAmount;
	}

    // getting CustId
    public float getCustId(){
        return this.iCustId;
    }
	
	// setting BillNumber
	public void setBillNumber(int BillNumber){
		this.iBillNumber = BillNumber;
	}
	
	// setting EmployeeId
	public void setEmployeeId(int EmployeeId){
		this.iEmployeeId = EmployeeId;
	}
	
	// setting TotalItems
	public void setTotalItems(int TotalItems){
		this.iTotalItems = TotalItems;
	}
	
	// setting BillAmount
	public void setBillAmount(float BillAmount){
		this.fBillAmount = BillAmount;
	}
	
	// setting DeliveryCharge
	public void setDeliveryCharge(float DeliveryCharge){
		this.fDeliveryCharge = DeliveryCharge;
	}
	
	// setting PettyCash
	public void setPettyCash(float PettyCash){
		this.fPettyCash = PettyCash;
	}
			
	// setting SettledAmount
	public void setSettledAmount(float SettledAmount){
		this.fSettledAmount = SettledAmount;
	}

	// setting SettledAmount
	public void setCustId(int CustId){
		this.iCustId = CustId;
	}

}
