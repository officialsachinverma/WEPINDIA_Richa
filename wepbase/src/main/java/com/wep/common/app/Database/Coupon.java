/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	Coupon
 * 
 * Purpose			:	Represents Coupon table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	15-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package com.wep.common.app.Database;

public class Coupon {
	
	// Private Variables
	String strCouponDescription, strCouponBarcode;
	int iCouponId;
	float fCouponAmount;
	
	// Default Constructor
	public Coupon(){
		this.strCouponBarcode = "";
		this.strCouponDescription = "";
		this.iCouponId = 0;
		this.fCouponAmount = 0;
	}
	
	// Parameterized Constructor
	public Coupon(String CouponBarCode,String CouponDescription,int CouponId,float CouponAmount){
		this.strCouponBarcode = CouponBarCode;
		this.strCouponDescription = CouponDescription;
		this.iCouponId = CouponId;
		this.fCouponAmount = CouponAmount;
	}
	
	// getting Coupon Barcode
	public String getCouponBarcode(){
		return this.strCouponBarcode;
	}
	
	// getting Coupon Description
	public String getCouponDescription(){
		return this.strCouponDescription;
	}
	
	// getting Coupon Id
	public int getCouponId(){
		return this.iCouponId;
	}
	
	// getting Coupon Amount
	public float getCouponAmount(){
		return this.fCouponAmount;
	}
	
	// setting Coupon Barcode
	public void setCouponBarcode(String CouponBarCode){
		this.strCouponBarcode = CouponBarCode;
	}
	
	// setting Coupon Description
	public void setCouponDescription(String CouponDescription){
		this.strCouponDescription = CouponDescription;
	}
	
	// setting CouponId
	public void setCouponId(int CouponId){
		this.iCouponId = CouponId;
	}
	
	// setting CouponAmount
	public void setCouponAmount(float CouponAmount){
		this.fCouponAmount = CouponAmount;
	}

}
