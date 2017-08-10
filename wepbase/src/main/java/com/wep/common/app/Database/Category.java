/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	Category
 * 
 * Purpose			:	Represents Category table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	15-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package com.wep.common.app.Database;

public class Category {
	
	// Private variables
	String strCategName;
	int iCategCode;
	int iDeptCode;
		
	// Default constructor
	public Category(){
		this.strCategName = "";
		this.iCategCode = 0;
		this.iDeptCode = 0;
	}
		
	// Parameterized construcor
	public Category(String CategName,int CategCode, int DeptCode){
		this.strCategName = CategName;
		this.iCategCode = CategCode;
		this.iDeptCode = DeptCode;
	}
		
	// getting CategName
	public String getCategName(){
		return this.strCategName;
	}
		
	// getting CategCode
	public int getCategCode(){
		return this.iCategCode;
	}
	
	// getting DeptCode
		public int getDeptCode(){
			return this.iDeptCode;
		}
		
	// setting CategName
	public void setDeptName(String CategName){
		this.strCategName = CategName;
	}
		
	// setting CategCode
	public void setDeptCode(int CategCode){
		this.iCategCode = CategCode;
	}
	
	// setting DeptCode
		public void setDepartmentCode(int DeptCode){
			this.iDeptCode = DeptCode;
		}

}
