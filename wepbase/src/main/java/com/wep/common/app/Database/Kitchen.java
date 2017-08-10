/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	Kitchen
 * 
 * Purpose			:	Represents Kitchen table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	15-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package com.wep.common.app.Database;

public class Kitchen {
	
	// Private variables
	String strKitchenName;
	int iKitchenCode;
			
	// Default constructor
	public Kitchen(){
		this.strKitchenName = "";
		this.iKitchenCode = 0;
	}
		
	// Parameterized construcor
	public Kitchen(String KitchenName,int KitchenCode){
		this.strKitchenName = KitchenName;
		this.iKitchenCode = KitchenCode;
	}
			
	// getting KitchenName
	public String getKitchenName(){
		return this.strKitchenName;
	}
			
	// getting KitchenCode
	public int getKitchenCode(){
		return this.iKitchenCode;
	}
			
	// setting KitchenName
	public void setKitchenName(String KitchenName){
		this.strKitchenName = KitchenName;
	}
			
	// setting KitchenCode
	public void setKitchenCode(int KitchenCode){
		this.iKitchenCode = KitchenCode;
	}
}