/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	DeletedKOT
 * 
 * Purpose			:	Represents DeletedKOT table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	16-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package com.wep.common.app.Database;

public class DeletedKOT {
	
	// Private variables
	String strReason,strTime;
	int iEmployeeId, iSubUdfNumber, iTableNumber, iTokenNumber;
	
	// Default Constructor
	public DeletedKOT(){
		
		this.strReason = "";
		this.strTime = "";
		this.iEmployeeId = 0;
		this.iSubUdfNumber = 0;
		this.iTableNumber = 0;
		this.iTokenNumber = 0;
		
	}
		
	// Parameterized Constructor
	public DeletedKOT(String Reason,String Time,int EmployeeId,int SubUdfNumber,int TableNumber,int TokenNumber){
				
		this.strReason = Reason;
		this.strTime = Time;
		this.iEmployeeId = EmployeeId;
		this.iSubUdfNumber = SubUdfNumber;
		this.iTableNumber = TableNumber;
		this.iTokenNumber = TokenNumber;
			
	}
	
	// getting Reason
	public String getReason(){
		return this.strReason;
	}
	
	// getting Time
	public String getTime(){
		return this.strTime;
	}
	
	// getting EmployeeId
	public int getEmployeeId(){
		return this.iEmployeeId;
	}
		
	// getting SubUdfNumber
	public int getSubudfNumber(){
		return this.iSubUdfNumber;
	}
	
	// getting TableNumber
	public int getTableNumber(){
		return this.iTableNumber;
	}
		
	// getting TokenNumber
	public int getTokenNumber(){
		return this.iTokenNumber;
	}
	
	// setting Reason
	public void setReason(String Reason){
		this.strReason = Reason;
	}
	
	// setting Time
	public void setTime(String Time){
		this.strTime = Time;
	}
	
	// setting EmployeeId
	public void setEmployeeId(int EmployeeId){
		this.iEmployeeId = EmployeeId;
	}
		
	// setting SubUdfNumber
	public void setSubUdfNumber(int SubUdfNumber){
		this.iSubUdfNumber = SubUdfNumber;
	}
	
	// setting TableNumber
	public void setTableNumber(int TableNumber){
		this.iTableNumber = TableNumber;
	}
			
	// setting TokenNumber
	public void setTokenNumber(int TokenNumber){
		this.iTokenNumber = TokenNumber;
	}

}
