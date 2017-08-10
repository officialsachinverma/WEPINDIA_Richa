/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	Description
 * 
 * Purpose			:	Represents Description table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	15-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package com.wep.common.app.Database;

public class Description {
	
	// Private variables
	String strDescriptionText;
	int iDescriptionId;
				
	// Default constructor
	public Description(){
		this.strDescriptionText = "";
		this.iDescriptionId = 0;
	}
			
	// Parameterized construcor
	public Description(String DescriptionText,int DescriptionId){
		this.strDescriptionText = DescriptionText;
		this.iDescriptionId = DescriptionId;
	}
				
	// getting DescriptionText
	public String getDescriptionText(){
		return this.strDescriptionText;
	}
				
	// getting DescriptionId
	public int getDescriptionId(){
		return this.iDescriptionId;
	}
				
	// setting DescriptionText
	public void setDescriptionText(String DescriptionText){
		this.strDescriptionText = DescriptionText;
	}
				
	// setting DescriptionId
	public void setDescriptionId(int DescriptionId){
		this.iDescriptionId = DescriptionId;
	}

}
