/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	BillDetail
 * 
 * Purpose			:	Represents BillDetail table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	16-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package com.wep.common.app.Database;

public class BillDetail {
	
	// Private variables
	String Custname, CustStateCode, POS,strDate, strTime, strUserId,BusinessType, Amount, GSTIN;
	int iBillNumber, iBillStatus, iCustId, iEmployeeId, iReprintCount, iTotalItems, iUserId;
	String BillingMode, TableNo, TableSplitNo; // richa_2012
	float fBillAmount, fCardPayment, fCashPayment, fCouponPayment, fPettyCashPayment, fPaidTotalPayment, fChangePayment, fWalletAmount,
	fDeliveryCharge, fTotalDiscountPercentage,fTotalDiscountAmount, fTotalTaxAmount, fTotalServiceTaxAmount, IGSTAmount, CGSTAmount, SGSTAmount,
            cessAmount,SubTotal;


	// Default Constructor
	public BillDetail(){
		this.GSTIN="";
		this.Custname="";
        this.BillingMode  = ""; // richa_2012
        this.TableNo  = ""; // richa_2012
        this.TableSplitNo  = ""; // richa_2012
		this.CustStateCode="";
		this.POS = "";
		this.Amount="";
		this.CGSTAmount=0;
		this.IGSTAmount=0;
		this.SGSTAmount=0;
		this.SubTotal=0;
		this.BusinessType="";
		this.strDate = "";
		this.strTime = "";
		this.iBillNumber = 0;
		this.iBillStatus = 0;
		this.iCustId = 0;
		this.iEmployeeId = 0;
		this.iReprintCount = 0;
		this.iTotalItems = 0;
		this.strUserId = "";
		this.iUserId = 0;
		this.fBillAmount = 0;
		this.fCardPayment = 0;
		this.fCashPayment = 0;
		this.fCouponPayment = 0;
		this.fDeliveryCharge = 0;
		this.fTotalDiscountAmount = 0;
		this.fTotalDiscountPercentage = 0;
		this.fTotalTaxAmount = 0;
		this.fTotalServiceTaxAmount = 0;
		this.fWalletAmount = 0;

		this.fPettyCashPayment = 0;
		this.fPaidTotalPayment = 0;
		this.fChangePayment = 0;
        this.cessAmount =0;

		
	}



	// Parameterized Constructor
	public BillDetail(String Date,String Time,int BillNumber,int BillStatus,int CustId,int EmployeeId,int ReprintCount,int TotalItems,String UserId,
			float BillAmount,float CardPayment,float CashPayment,float CouponPayment,float DeliveryCharge,
			float TotalDiscountAmount,float TotalTaxAmount,float TotalServiceTaxAmount, float PettyCashPayment, float PaidTotalPayment,
                      float ChangePayment,String BusinessType,String Amount,float walletAmount, String tableNo, String TableSplitNo, float cessAmount){
		this.Amount = Amount;
		this.strDate = Date;
		this.strTime = Time;
		this.BusinessType=BusinessType;
		this.iBillNumber = BillNumber;
		this.iBillStatus = BillStatus;
		this.iCustId = CustId;
		this.iEmployeeId = EmployeeId;
		this.iReprintCount = ReprintCount;
		this.iTotalItems = TotalItems;
		this.strUserId = UserId;
		//this.iUserId = UserId;
		this.fBillAmount = BillAmount;
		this.fCardPayment = CardPayment;
		this.fCashPayment = CashPayment;
		this.fCouponPayment = CouponPayment;
		this.fDeliveryCharge = DeliveryCharge;
		this.fTotalDiscountAmount = TotalDiscountAmount;
		this.fTotalTaxAmount = TotalTaxAmount;
		this.fTotalServiceTaxAmount = TotalServiceTaxAmount;

        this.fPettyCashPayment = PettyCashPayment;
        this.fPaidTotalPayment = PaidTotalPayment;
        this.fChangePayment = ChangePayment;
		this.fWalletAmount = walletAmount;
		this.TableNo = tableNo;
		this.TableSplitNo= TableSplitNo;
        this.cessAmount = cessAmount;
	}

    public float getCessAmount() {
        return cessAmount;
    }

    public void setCessAmount(float cessAmount) {
        this.cessAmount = cessAmount;
    }

    public String getGSTIN() {
		return GSTIN;
	}

	public void setGSTIN(String GSTIN) {
		this.GSTIN = GSTIN;
	}

	public String getTableNo() {
		return TableNo;
	}

	public void setTableNo(String tableNo) {
		TableNo = tableNo;
	}

	public String getTableSplitNo() {
		return TableSplitNo;
	}

	public void setTableSplitNo(String tableSplitNo) {
		TableSplitNo = tableSplitNo;
	}

	public float getTotalDiscountPercentage() {
		return fTotalDiscountPercentage;
	}

	public void setTotalDiscountPercentage(float fTotalDiscountPercentage) {
		this.fTotalDiscountPercentage = fTotalDiscountPercentage;
	}

	public float getWalletAmount() {
		return fWalletAmount;
	}

	public void setWalletAmount(float fWalletAmount) {
		this.fWalletAmount = fWalletAmount;
	}

	// richa_2012 starts

    public String getBillingMode() {
        return BillingMode;
    }

    public void setBillingMode(String billingMode) {
        BillingMode = billingMode;
    }


    // richa_2012 ends

	public String getCustname() {
		return Custname;
	}

	public void setCustname(String custname) {
		Custname = custname;
	}

	public String getCustStateCode() {
		return CustStateCode;
	}

	public void setCustStateCode(String custStateCode) {
		CustStateCode = custStateCode;
	}

	public float getSubTotal() {
		return SubTotal;
	}

	public void setSubTotal(float subTotal) {
		SubTotal = subTotal;
	}

	public float getIGSTAmount() {
		return IGSTAmount;
	}

	public void setIGSTAmount(float IGSTAmount) {
		this.IGSTAmount = IGSTAmount;
	}

	public float getCGSTAmount() {
		return CGSTAmount;
	}

	public void setCGSTAmount(float CGSTAmount) {
		this.CGSTAmount = CGSTAmount;
	}

	public float getSGSTAmount() {
		return SGSTAmount;
	}

	public void setSGSTAmount(float SGSTAmount) {
		this.SGSTAmount = SGSTAmount;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getBusinessType() {
		return BusinessType;
	}

	public void setBusinessType(String businessType) {
		BusinessType = businessType;
	}

	public String getPOS() {
		return POS;
	}

	public void setPOS(String POS) {
		this.POS = POS;
	}

	// getting Date
	public String getDate(){
		return this.strDate;
	}
	
	// getting Time
	public String getTime(){
		return this.strTime;
	}
	
	// getting BillNumber
	public int getBillNumber(){
		return this.iBillNumber;
	}
	
	// getting BillStatus
	public int getBillStatus(){
		return this.iBillStatus;
	}
		
	// getting CustId
	public int getCustId(){
		return this.iCustId;
	}
		
	// getting EmployeeId
	public int getEmployeeId(){
		return this.iEmployeeId;
	}
		
	// getting ReprintCount
	public int getReprintCount(){
		return this.iReprintCount;
	}
		
	// getting TotalItems
	public int getTotalItems(){
		return this.iTotalItems;
	}
		
	// getting UserId
	public String getUserId(){
		return this.strUserId;
	}
	
	// getting BillAmount
	public float getBillAmount(){
		return this.fBillAmount;
	}
	
	// getting CardPayment
	public float getCardPayment(){
		return this.fCardPayment;
	}
		
	// getting CashPayment
	public float getCashPayment(){
		return this.fCashPayment;
	}
		
	// getting CouponPayment
	public float getCouponPayment(){
		return this.fCouponPayment;
	}
		
	// getting DeliveryCharge
	public float getDeliveryCharge(){
		return this.fDeliveryCharge;
	}
		
	// getting TotalDiscountAmount
	public float getTotalDiscountAmount(){
		return this.fTotalDiscountAmount;
	}
		
	// getting TotalTaxAmount
	public float getTotalTaxAmount(){
		return this.fTotalTaxAmount;
	}
	
	// getting TotalServiceTaxAmount
	public float getTotalServiceTaxAmount(){
		return this.fTotalServiceTaxAmount;
	}

    // getting PettyCashPayment
    public float getPettyCashPayment(){
        return this.fPettyCashPayment;
    }

    // getting PaidTotalPayment
    public float getPaidTotalPayment(){
        return this.fPaidTotalPayment;
    }

    // getting ChangePayment
    public float getChangePayment(){
        return this.fChangePayment;
    }

	
	// setting Date
	public void setDate(String Date){
		this.strDate = Date;
	}
	
	// setting Time
	public void setTime(String Time){
		this.strTime = Time;
	}
		
	// setting BillNumber
	public void setBillNumber(int BillNumber){
		this.iBillNumber = BillNumber;
	}
		
	// setting BillStatus
	public void setBillStatus(int BillStatus){
		this.iBillStatus = BillStatus;
	}
			
	// setting CustId
	public void setCustId(int CustId){
		this.iCustId = CustId;
	}
			
	// setting EmployeeId
	public void setEmployeeId(int EmployeeId){
		this.iEmployeeId = EmployeeId;
	}
			
	// setting ReprintCount
	public void setReprintCount(int ReprintCount){
		this.iReprintCount = ReprintCount;
	}
			
	// setting TotalItems
	public void setTotalItems(int TotalItems){
		this.iTotalItems = TotalItems;
	}
			
	// setting UserId
	public void setUserId(String UserId){
		this.strUserId = UserId;
	}
		
	// setting BillAmount
	public void setBillAmount(float BillAmount){
		this.fBillAmount = BillAmount;
	}
		
	// setting CardPayment
	public void setCardPayment(float CardPayment){
		this.fCardPayment = CardPayment;
	}
			
	// setting CashPayment
	public void setCashPayment(float CashPayment){
		this.fCashPayment = CashPayment;
	}
			
	// setting CouponPayment
	public void setCouponPayment(float CouponPayment){
		this.fCouponPayment = CouponPayment;
	}
			
	// setting DeliveryCharge
	public void setDeliveryCharge(float DeliveryCharge){
		this.fDeliveryCharge = DeliveryCharge;
	}
			
	// setting TotalDiscountAmount
	public void setTotalDiscountAmount(float TotalDiscountAmount){
		this.fTotalDiscountAmount = TotalDiscountAmount;
	}
			
	// setting TotalTaxAmount
	public void setTotalTaxAmount(float TotalTaxAmount){
		this.fTotalTaxAmount = TotalTaxAmount;
	}
	
	// setting TotalServiceTaxAmount
	public void setTotalServiceTaxAmount(float TotalServiceTaxAmount){
		this.fTotalServiceTaxAmount = TotalServiceTaxAmount;
	}

    // setting PettyCashPayment
    public void setPettyCashPayment(float PettyCashPayment){
        this.fPettyCashPayment = PettyCashPayment;
    }

    // setting PaidTotalPayment
    public void setPaidTotalPayment(float PaidTotalPayment){
        this.fPaidTotalPayment = PaidTotalPayment;
    }

    // setting ChangePayment
    public void setChangePayment(float ChangePayment){
        this.fChangePayment = ChangePayment;
    }

}
