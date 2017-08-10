/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	MachineSetting
 * 
 * Purpose			:	Represents MachineSetting table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	15-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package com.wep.common.app.Database;

public class MachineSetting {
	
	// Private Variables
	String strPortName;
	int iBaudRate, iDataBits, iParity, iStopBits;
	
	// Default Constructor
	public MachineSetting(){
		this.strPortName = "";
		this.iBaudRate = 0;
		this.iDataBits = 0;
		this.iParity = 0;
		this.iStopBits = 0;
	}
	
	// Parameterized Constructor
	public MachineSetting(String PortName,int BaudRate,int DataBits,int Parity,int StopBits){
		this.strPortName = PortName;
		this.iBaudRate = BaudRate;
		this.iDataBits = DataBits;
		this.iParity = Parity;
		this.iStopBits = StopBits;
	}
	
	// getting PortName
	public String getPortName(){
		return this.strPortName;
	}
	
	// getting BaudRate
	public int getBaudRate(){
		return this.iBaudRate;
	}
	
	// getting DataBits
	public int getDataBits(){
		return this.iDataBits;
	}
		
	// getting Parity
	public int getParity(){
		return this.iParity;
	}
		
	// getting StopBits
	public int getStopBits(){
		return this.iStopBits;
	}
	
	// setting PortName
	public void setPortName(String PortName){
		this.strPortName = PortName;
	}
	
	// setting BaudRate
	public void setBaudRate(int BaudRate){
		this.iBaudRate = BaudRate;
	}
	
	// setting DataBits
	public void setDataBits(int DataBits){
		this.iDataBits = DataBits;
	}
		
	// setting BaudRate
	public void setParity(int Parity){
		this.iParity = Parity;
	}
		
	// setting BaudRate
	public void setStopBits(int StopBits){
		this.iStopBits = StopBits;
	}

}
