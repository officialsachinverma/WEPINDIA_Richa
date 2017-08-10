/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	TaxConfig
 * 
 * Purpose			:	Represents TaxConfig table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	15-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package com.wep.common.app.Database;

public class TaxConfig {
	
	// Private Variable
	String strTaxDesciption;
	int iTaxId;
	float fTaxPercentage;
	float fTotalPercentage;
	
	// Default Constructor
	public TaxConfig(){
		this.strTaxDesciption = "";
		this.iTaxId = 0;
		this.fTaxPercentage = 0;
		this.fTotalPercentage = 0;
	}
	
	// Paramterized Constructor
	public TaxConfig(String TaxDescription,int TaxId,float TaxPercentage, float TotalPercentage){
		this.strTaxDesciption = TaxDescription;
		this.iTaxId = TaxId;
		this.fTaxPercentage = TaxPercentage;
		this.fTotalPercentage = TotalPercentage;
	}
	
	// getting TaxDescription
	public String getTaxDescription(){
		return this.strTaxDesciption;
	}
	
	// getting TaxId
	public int getTaxId(){
		return this.iTaxId;
	}
	
	// getting TaxPercentage
	public float getTaxPercentage(){
		return this.fTaxPercentage;
	}

	// getting TotalPercentage
	public float getTotalPercentage(){
		return this.fTotalPercentage;
	}
	
	// setting TaxDescription
	public void setTaxDescription(String TaxDescription){
		this.strTaxDesciption = TaxDescription;
	}
	
	// setting TaxId
	public void setTaxId(int TaxId){
		this.iTaxId = TaxId;
	}
	
	// setting TaxPercentage
	public void setTaxPercentage(float TaxPercentage){
		this.fTaxPercentage = TaxPercentage;
	}

	// setting TaxPercentage
	public void setTotalPercentage(float TotalPercentage){
		this.fTotalPercentage = TotalPercentage;
	}

}
