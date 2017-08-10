/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	BillItem
 * 
 * Purpose			:	Represents BillItem table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	16-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package com.wep.common.app.Database;

public class BillItem {
	
	// Private variables
	String CustName, CustStateCode,Uom,strItemName, HSNCode, BusinessType, iBillNumber,  SupplyType,
			SupplierPhone, SupplierName, SupplierAddress,SupplierGSTIN,
            GSTIN;
	int  iItemNumber, iDeptCode, iCategCode, iKitchenCode, iTaxType;
	float fAmount, fDiscountAmount, fDiscountPercent, fQuantity, fvalue,fTaxAmount, fTaxPercent, fServiceTaxPercent, fServiceTaxAmount, fModifierAmount,
	IGSTRate, IGSTAmount, CGSTRate, CGSTAmount, SGSTRate, SGSTAmount;
	String TaxationType,SupplierType ;
	String InvoiceDate;
	String BillingMode; // richa_2012
	int isGoodInwarded, billStatus;
	int Suppliercode, PurchaseOrderNo;
    String additionalChargeName;
    float additionalChargeAmount;

	Float TaxableValue, SubTotal;
	double cessRate, cessAmount;
	// Default constructor
	public BillItem(){
		this.Suppliercode=-1;
        this.PurchaseOrderNo=0;
        this.SupplierAddress = "";
        this.additionalChargeAmount = 0;
        this.additionalChargeName = "";
		this.Uom="";
		this.CustStateCode="";
		this.CustName="";
		this.SupplierName="";
		this.GSTIN ="";
		this.SupplierPhone="";
		this.TaxationType="";
		this.SubTotal = 0f;
		this.TaxableValue = 0f;
		this.strItemName = "";
		this.SupplyType = "";
		this.iCategCode = 0;
		this.iDeptCode = 0;
		this.iItemNumber = 0;
		this.iKitchenCode = 0;
		this.iBillNumber = "";
		this.iTaxType = 0;
		this.fAmount = 0;
		this.fDiscountAmount = 0;
		this.fDiscountPercent = 0;
		this.fQuantity = 0;
		this.fvalue = 0;
		this.fTaxAmount = 0;
		this.fTaxPercent = 0;
		this.fServiceTaxAmount = 0;
		this.fServiceTaxPercent = 0;
		this.fModifierAmount = 0;
		this.BusinessType= "";
		this.SupplierGSTIN= "";
		this.InvoiceDate= "";
		this.HSNCode="";
		this.IGSTAmount=0;
		this.IGSTRate= 0;
		this.CGSTRate=0;
		this.CGSTAmount=0;
		this.SGSTRate=0;
		this.SGSTAmount=0;
		this.cessRate=0;
		this.cessAmount=0;
		this.isGoodInwarded=0;
		this.billStatus=0;
		this.SupplierType="";
	}

	public int getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(int billStatus) {
		this.billStatus = billStatus;
	}

	public double getCessRate() {
		return cessRate;
	}

	public void setCessRate(double cessRate) {
		this.cessRate = cessRate;
	}

	public double getCessAmount() {
		return cessAmount;
	}

	public void setCessAmount(double cessAmount) {
		this.cessAmount = cessAmount;
	}

	public String getSupplierGSTIN() {
		return SupplierGSTIN;
	}

	public void setSupplierGSTIN(String supplierGSTIN) {
		SupplierGSTIN = supplierGSTIN;
	}

	public int getIsGoodInwarded() {
		return isGoodInwarded;
	}

	public void setIsGoodInwarded(int isGoodInwarded) {
		this.isGoodInwarded = isGoodInwarded;
	}

	public int getPurchaseOrderNo() {
        return PurchaseOrderNo;
    }

    public void setPurchaseOrderNo(int purchaseOrderNo) {
        PurchaseOrderNo = purchaseOrderNo;
    }

    public float getAdditionalChargeAmount() {
        return additionalChargeAmount;
    }

    public void setAdditionalChargeAmount(float additionalChargeAmount) {
        this.additionalChargeAmount = additionalChargeAmount;
    }

    public String getAdditionalChargeName() {
        return additionalChargeName;
    }

    public void setAdditionalChargeName(String additionalChargeName) {
        this.additionalChargeName = additionalChargeName;
    }

    public String getSupplierAddress() {
        return SupplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        SupplierAddress = supplierAddress;
    }

    public int getSuppliercode() {
		return Suppliercode;
	}

	public void setSuppliercode(int suppliercode) {
		Suppliercode = suppliercode;
	}

	public String getCustName() {
		return CustName;
	}

	public void setCustName(String custName) {
		CustName = custName;
	}

	public String getCustStateCode() {
		return CustStateCode;
	}

	public void setCustStateCode(String custStateCode) {
		CustStateCode = custStateCode;
	}

	public String getSupplierType() {
		return SupplierType;
	}

	public void setSupplierType(String supplierType) {
		SupplierType = supplierType;
	}

	// Parameterized constructor
	public BillItem(String ItemName,int CategCode,int DeptCode,int ItemNumber,
			int KitchenCode,String BillNumber,int TaxType,float Amount,float DiscountAmount,
			float DiscountPercent,float Quantity,float Value,float TaxAmount,float TaxPercent,
			float ServiceTaxAmount,float ServiceTaxPercent,float ModifierAmount,
			String BusinessType, String InvoiceDate, String hsn, String TaxationType){

		this.TaxationType=TaxationType;
		this.HSNCode = hsn;
		this.strItemName = ItemName;
		this.iCategCode = CategCode;
		this.iDeptCode = DeptCode;
		this.iItemNumber = ItemNumber;
		this.iKitchenCode = KitchenCode;
		this.iBillNumber = BillNumber;
		this.iTaxType = TaxType;
		this.fAmount = Amount;
		this.fDiscountAmount = DiscountAmount;
		this.fDiscountPercent = DiscountPercent;

		this.fQuantity = Quantity;
		this.fvalue = Value;
		this.fTaxAmount = TaxAmount;
		this.fTaxPercent = TaxPercent;
		this.fServiceTaxAmount = ServiceTaxAmount;
		this.fServiceTaxPercent = ServiceTaxPercent;
		this.fModifierAmount = ModifierAmount;
		this.BusinessType = BusinessType;
		this.InvoiceDate = InvoiceDate;
		
	}

    //richa_2012 starts

    public String getBillingMode() {
        return BillingMode;
    }

    public void setBillingMode(String billingMode) {
        BillingMode = billingMode;
    }


    //richa_2012 ends

	public String getSupplierName() {
		return SupplierName;
	}

	public void setSupplierName(String supplierName) {
		SupplierName = supplierName;
	}

	public String getGSTIN() {
		return GSTIN;
	}

	public void setGSTIN(String GSTIN) {
		this.GSTIN = GSTIN;
	}

	public String getSupplierPhone() {
		return SupplierPhone;
	}

	public void setSupplierPhone(String supplierPhone) {
		SupplierPhone = supplierPhone;
	}

	public String getTaxationType() {
		return TaxationType;
	}

	public void setTaxationType(String taxationType) {
		TaxationType = taxationType;
	}

	public Float getSubTotal() {
		return SubTotal;
	}

	public void setSubTotal(Float subTotal) {
		SubTotal = subTotal;
	}

	public Float getTaxableValue() {
		return TaxableValue;
	}

	public void setTaxableValue(Float taxableValue) {
		TaxableValue = taxableValue;
	}

	public String getUom() {
		return Uom;
	}

	public void setUom(String uom) {
		Uom = uom;
	}

	public String getSupplyType() {
		return SupplyType;
	}

	public void setSupplyType(String supplyType) {
		SupplyType = supplyType;
	}

	public String getHSNCode() {
		return HSNCode;
	}

	public void setHSNCode(String HSNCode) {
		this.HSNCode = HSNCode;
	}

	public String getInvoiceDate() {
		return InvoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		InvoiceDate = invoiceDate;
	}

	public String getBusinessType() {
		return BusinessType;
	}

	public void setBusinessType(String businessType) {
		BusinessType = businessType;
	}

	// getting ItemName
	public String getItemName(){
		return this.strItemName;
	}
	
	// getting BillNumber
	public String getBillNumber(){
		return this.iBillNumber;
	}
	
	// getting CategCode
	public int getCategCode(){
		return this.iCategCode;
	}

	public float getIGSTRate() {
		return IGSTRate;
	}

	public void setIGSTRate(float IGSTRate) {
		this.IGSTRate = IGSTRate;
	}

	public float getIGSTAmount() {
		return IGSTAmount;
	}

	public void setIGSTAmount(float IGSTAmount) {
		this.IGSTAmount = IGSTAmount;
	}

	public float getCGSTRate() {
		return CGSTRate;
	}

	public void setCGSTRate(float CGSTRate) {
		this.CGSTRate = CGSTRate;
	}

	public float getCGSTAmount() {
		return CGSTAmount;
	}

	public void setCGSTAmount(float CGSTAmount) {
		this.CGSTAmount = CGSTAmount;
	}

	public float getSGSTRate() {
		return SGSTRate;
	}

	public void setSGSTRate(float SGSTRate) {
		this.SGSTRate = SGSTRate;
	}

	public float getSGSTAmount() {
		return SGSTAmount;
	}

	public void setSGSTAmount(float SGSTAmount) {
		this.SGSTAmount = SGSTAmount;
	}

	// getting DeptCode
	public int getDeptCode(){
		return this.iDeptCode;
	}
			
	// getting ItemNumber
	public int getItemNumber(){
		return this.iItemNumber;
	}
			
	// getting KitchenCode
	public int getKitchenCode(){
		return this.iKitchenCode;
	}
				
	// getting TaxType
	public int getTaxType(){
		return this.iTaxType;
	}
				
	// getting Amount
	public float getAmount(){
		return this.fAmount;
	}
		
	// getting DiscountAmount
	public float getDiscountAmount(){
		return this.fDiscountAmount;
	}
			
	// getting DiscountPercent
	public float getDiscountPercent(){
		return this.fDiscountPercent;
	}
			
	// getting Quantity
	public float getQuantity(){
		return this.fQuantity;
	}
			
	// getting Value
	public float getValue(){
		return this.fvalue;
	}
			
	// getting TaxAmount
	public float getTaxAmount(){
		return this.fTaxAmount;
	}
			
	// getting TaxPercent
	public float getTaxPercent(){
		return this.fTaxPercent;
	}
	
	// getting ServiceTaxAmount
	public float getServiceTaxAmount(){
		return this.fServiceTaxAmount;
	}
		
	// getting ServiceTaxPercent
	public float getServiceTaxPercent(){
		return this.fServiceTaxPercent;
	}
	
	// getting ModifierAmount
	public float getModifierAmount(){
		return this.fModifierAmount;
	}
	
	// setting ItemName
	public void setItemName(String ItemName){
		this.strItemName = ItemName;
	}
		
	// setting BillNumber
	public void setBillNumber(String BillNumber){
		this.iBillNumber = BillNumber;
	}
		
	// setting CategCode
	public void setCategCode(int CategCode){
		this.iCategCode = CategCode;
	}
			
	// setting DeptCode
	public void setDeptCode(int DeptCode){
		this.iDeptCode = DeptCode;
	}
				
	// setting ItemNumber
	public void setItemNumber(int ItemNumber){
		this.iItemNumber = ItemNumber;
	}
				
	// setting KitchenCode
	public void setKitchenCode(int KitchenCode){
		this.iKitchenCode = KitchenCode;
	}
					
	// setting TaxType
	public void setTaxType(int TaxType){
		this.iTaxType = TaxType;
	}
					
	// setting Amount
	public void setAmount(float Amount){
		this.fAmount = Amount;
	}
			
	// setting DiscountAmount
	public void setDiscountAmount(float DiscountAmount){
		this.fDiscountAmount = DiscountAmount;
	}
				
	// setting DiscountPercent
	public void setDiscountPercent(float DiscountPercent){
		this.fDiscountPercent = DiscountPercent;
	}
				
	// setting Quantity
	public void setQuantity(float Quantity){
		this.fQuantity = Quantity;
	}
				
	// setting Value
	public void setValue(float Value){
		this.fvalue = Value;
	}
				
	// setting TaxAmount
	public void setTaxAmount(float TaxAmount){
		this.fTaxAmount = TaxAmount;
	}
				
	// setting TaxPercent
	public void setTaxPercent(float TaxPercent){
		this.fTaxPercent = TaxPercent;
	}
	
	// setting ServiceTaxAmount
	public void setServiceTaxAmount(float ServiceTaxAmount){
		this.fServiceTaxAmount = ServiceTaxAmount;
	}
			
	// setting ServiceTaxPercent
	public void setServiceTaxPercent(float ServiceTaxPercent){
		this.fServiceTaxPercent = ServiceTaxPercent;
	}
	
	// setting ModifierAmount
	public void setModifierAmount(float ModifierAmount){
		this.fModifierAmount = ModifierAmount;
	}

}
