/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	DiscountConfig
 * 
 * Purpose			:	Represents DiscountConfig table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	15-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package com.wep.common.app.Database;

public class VoucherConfig {
	
	// Private Variable
		String strVoucherDesciption;
		int iVoucherId;
		float fVoucherPercentage;
		
		// Default Constructor
		public VoucherConfig(){
			this.strVoucherDesciption = "";
			this.iVoucherId = 0;
			this.fVoucherPercentage = 0;
		}
		
		// Paramterized Constructor
		public VoucherConfig(String VoucherDescription,int VoucherId,float VoucherPercentage){
			this.strVoucherDesciption = VoucherDescription;
			this.iVoucherId = VoucherId;
			this.fVoucherPercentage = VoucherPercentage;
		}
		
		// getting DiscDescription
		public String getVoucherDescription(){
			return this.strVoucherDesciption;
		}
		
		// getting DiscId
		public int getVoucherId(){
			return this.iVoucherId;
		}
		
		// getting DiscPercentage
		public float getVoucherPercentage(){
			return this.fVoucherPercentage;
		}
		
		// setting TaxDescription
		public void setVoucherDescription(String VoucherDescription){
			this.strVoucherDesciption = VoucherDescription;
		}
		
		// setting DiscId
		public void setVoucherId(int VoucherId){
			this.iVoucherId = VoucherId;
		}
		
		// setting DiscPercentage
		public void setVoucherPercentage(float VoucherPercentage){
			this.fVoucherPercentage = VoucherPercentage;
		}

}
