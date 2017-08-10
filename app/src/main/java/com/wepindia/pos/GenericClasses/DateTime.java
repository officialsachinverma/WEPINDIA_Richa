/****************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	Date
 * 
 * Purpose			:	Represents date class, initializes date string and returns
 * 						year, month and date as integers.
 * 
 * DateOfCreation	:	18-January-2013
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 ****************************************************************************/
package com.wepindia.pos.GenericClasses;

import android.nfc.FormatException;

/**
 * 
 * @author BharadwajB
 *
 */
public class DateTime {
	
	String strDateString;
	
	/***********************************************************************************************************************************************
	* Default constructor which initializes date value to 2000-01-01 (yyyy-MM-dd)
	***********************************************************************************************************************************************/
	public DateTime(){
		this.strDateString = "2000-01-01"; 
	}
	
	/***********************************************************************************************************************************************
	* Parameterized constructor which initializes date value to given parameter
	* @param DateString - Initializing parameter 
	***********************************************************************************************************************************************/
	public DateTime(String DateString){
		this.strDateString = DateString;
	}
	
	/***********************************************************************************************************************************************
	* Returns the year from the date string
	***********************************************************************************************************************************************/
	public int getYear()throws FormatException{
		
		if(strDateString.equalsIgnoreCase(null)){
			return 0;
		} else {
			return Integer.valueOf(strDateString.substring(0, 4));
		}
	}
	
	/***********************************************************************************************************************************************
	* Returns the month from the date string
	***********************************************************************************************************************************************/
	public int getMonth()throws FormatException{
		if(strDateString.equalsIgnoreCase(null)){
			return 0;
		} else {
			return Integer.valueOf(strDateString.substring(5, 7)) - 1;
		}
		
	}
	
	/***********************************************************************************************************************************************
	* Returns the date from the date string
	***********************************************************************************************************************************************/
	public int getDay()throws FormatException{
		if(strDateString.equalsIgnoreCase(null)){
			return 0;
		} else {
			return Integer.valueOf(strDateString.substring(8, 10));
		}
		
	}

}
