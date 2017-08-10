/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	Customer
 * 
 * Purpose			:	Represents Customer table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	15-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package com.wep.common.app.Database;

public class Customer {

	// Private Variable
	String strCustName, strCustContactNumber, strCustAddress, strCustGSTIN;
	int iCustId;
	float fLastTransaction, fTotalTransaction, fCreditAmount;

	// Default constructor
	public Customer() {
		this.strCustAddress = "";
		this.strCustName = "";
		this.strCustGSTIN="";
		this.strCustContactNumber = "";
		this.iCustId = 0;
		this.fLastTransaction = 0;
		this.fTotalTransaction = 0;
		this.fCreditAmount = 0;
	}

	// Parameterized construcor
	public Customer(String CustAddress, String CustName, String CustContactNumber, float LastTransaction,
			float TotalTransaction, float CreaditAmount,String gstin) {
		this.strCustAddress = CustAddress;
		this.strCustName = CustName;
		this.strCustContactNumber = CustContactNumber;
		this.fLastTransaction = LastTransaction;
		this.fTotalTransaction = TotalTransaction;
		this.fCreditAmount = CreaditAmount;
		this.strCustGSTIN= gstin;
	}

	public String getStrCustGSTIN() {
		return strCustGSTIN;
	}

	public void setStrCustGSTIN(String strCustGSTIN) {
		this.strCustGSTIN = strCustGSTIN;
	}

	// getting CustAddress
	public String getCustAddress() {
		return this.strCustAddress;
	}

	// getting CustName
	public String getCustName() {
		return this.strCustName;
	}

	// getting Contact number
	public String getCustContactNumber() {
		return this.strCustContactNumber;
	}

	// getting CustId
	public int getCustId() {
		return this.iCustId;
	}

	// getting LastTransaction
	public float getLastTransaction() {
		return this.fLastTransaction;
	}

	// getting TotalTransaction
	public float getTotalTransaction() {
		return this.fTotalTransaction;
	}

	// getting CreditAmount
	public float getCreditAmount() {
		return this.fCreditAmount;
	}

	// setting CustAddress
	public void setCustAddress(String CustAddress) {
		this.strCustAddress = CustAddress;
	}

	// setting CustName
	public void setCustName(String CustName) {
		this.strCustName = CustName;
	}

	// setting CustContactNumber
	public void setCustContactNumber(String CustContactNumber) {
		this.strCustContactNumber = CustContactNumber;
	}

	// setting EmployeeId
	public void setCustId(int CustId) {
		this.iCustId = CustId;
	}

	// setting LastTransaction
	public void setLastTransaction(float LastTransaction) {
		this.fLastTransaction = LastTransaction;
	}

	// setting TotalTransaction
	public void setTotalTransaction(float TotalTransaction) {
		this.fTotalTransaction = TotalTransaction;
	}

	// setting CreaditAmount
	public void setCeraditAmount(float CreaditAmount) {
		this.fCreditAmount = CreaditAmount;
	}
}
