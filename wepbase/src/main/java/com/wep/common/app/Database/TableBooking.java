
package com.wep.common.app.Database;

public class TableBooking {

	// Private variables
	String strCustomerName;
	String strTimeBooking;
	int iTableNo;
	String strMobileNo;
	int iTBookId;

	// Default constructor
	public TableBooking() {
		this.strCustomerName = "";
		this.strTimeBooking = "";
		this.strMobileNo = "";
		this.iTableNo = 0;
		this.iTBookId = 0;
	}

	// Parameterized construcor
	public TableBooking(String CustName, String TimeBooking, String MobileNo, int TableNo, int TBookId) {
		this.strCustomerName = CustName;
		this.strTimeBooking = TimeBooking;
		this.strMobileNo = MobileNo;
		this.iTableNo = TableNo;
		this.iTBookId = TBookId;
	}

	// getting CustName
	public String getCustomerName() {
		return this.strCustomerName;
	}

	// getting CustName
	public String getTimeBooking() {
		return this.strTimeBooking;
	}

	// getting CustName
	public String getMobileNo() {
		return this.strMobileNo;
	}

	// getting TableNo
	public int getTableNo() {
		return this.iTableNo;
	}

	// getting TBookId
	public int getTBookId() {
		return this.iTBookId;
	}

	// setting CustName
	public void setCustomerName(String CustName) {
		this.strCustomerName = CustName;
	}

	// setting TimeBooking
	public void setTimeBooking(String TimeBooking) {
		this.strCustomerName = TimeBooking;
	}

	// setting MobileNo
	public void setMobileNo(String MobileNo) {
		this.strMobileNo = MobileNo;
	}

	// setting TableNo
	public void setTableNo(int TableNo) {
		this.iTableNo = TableNo;
	}

	// setting TBookId
	public void setTBookId(int TBookId) {
		this.iTBookId = TBookId;
	}

}
