/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	Employee
 * 
 * Purpose			:	Represents Employee table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	15-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package com.wep.common.app.Database;

public class Employee {
	
	// Private Variable
	String strEmployeeName, strEmployeeContactNumber;
	int iEmployeeId,iEmployeeRole;
	
	// Default constructor
	public Employee(){
		this.strEmployeeName = "";
		this.strEmployeeContactNumber = "";
		this.iEmployeeId = 0;
		this.iEmployeeRole = 0;
	}
		
	// Parameterized construcor
	public Employee(String EmployeeName,String EmployeeContactNumber,int EmployeeRole){
		this.strEmployeeName = EmployeeName;
		this.strEmployeeContactNumber = EmployeeContactNumber;
		//this.iEmployeeId = 0;
		this.iEmployeeRole = EmployeeRole;
	}
		
	// getting EmployeeName
	public String getEmployeeName(){
		return this.strEmployeeName;
	}
		
	// getting Contact number
	public String getEmployeeContactNumber(){
		return this.strEmployeeContactNumber;
	}
		
	// getting EmployeeId
	public int getEmployeeId(){
		return this.iEmployeeId;
	}
		
	// getting EmployeeRole
	public int getEmployeeRole(){
		return this.iEmployeeRole;
	}
		
	// setting EmployeeName
	public void setEmployeeName(String EmployeeName){
		this.strEmployeeName = EmployeeName;
	}
		
	// setting EmployeeContactNumber
	public void setEmployeeContactNumber(String EmployeeContactNumber){
		this.strEmployeeContactNumber = EmployeeContactNumber;
	}
		
	// setting EmployeeId
	public void setEmployeeId(int EmployeeId){
		this.iEmployeeId = EmployeeId;
	}
		
	// setting EmployeeRole
	public void setEmployeeRole(int EmployeeRole){
		this.iEmployeeRole = EmployeeRole;
	}
	
}
