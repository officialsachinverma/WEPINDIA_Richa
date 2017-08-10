/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	PendingKOT
 * 
 * Purpose			:	Represents PendingKOT table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	15-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package com.wep.common.app.Database;

public class PendingKOT {

    // Private Variable
    String strItemName, strTime , HSNCode , POS , SupplyType,UOM;
    int iTokenNumber, iItemNumber, iTableNumber, iSubUdfNumber, iDeptCode, iTableSplitNo, iPrintKOTStatus,
            iCategCode, iKitchenCode, iEmployeeId, iCustId, iTaxType, iOrderMode, iIsCheckedOut;
    float fQuantity, fRate, fAmount, fTaxPercent, fTaxAmount,
            fDiscountPercent, fDiscountAmount, fModifierAmount, fServiceTaxAmount, fServiceTaxPercent;
    float cessRate, cessAmount,IGSTRate,IGSTAmount;

    // Default constructor
    public PendingKOT() {
        this.SupplyType="";
        this.strItemName = "";
        this.strTime = "";
        this.iCategCode = 0;
        this.iCustId = 0;
        this.iDeptCode = 0;
        this.iEmployeeId = 0;
        this.iItemNumber = 0;
        this.iKitchenCode = 0;
        this.iSubUdfNumber = 0;
        this.iTableNumber = 0;
        this.iTokenNumber = 0;
        this.fAmount = 0;
        this.fDiscountAmount = 0;
        this.fDiscountPercent = 0;
        this.fQuantity = 0;
        this.fRate = 0;
        this.fTaxAmount = 0;
        this.fTaxPercent = 0;
        this.iTaxType = 0;
        this.fModifierAmount = 0;
        this.fServiceTaxAmount = 0;
        this.fServiceTaxPercent = 0;
        this.iOrderMode = 0;
        this.iIsCheckedOut = 0;
        this.iTableSplitNo = 0;
        this.HSNCode= "";
        this.POS ="";
        this.iPrintKOTStatus = 0;
        this.UOM="";
        this.IGSTRate=0;
        this.IGSTAmount=0;
        this.cessRate=0;
        this.cessAmount=0;
    }

    // Parameterized constructor
    public PendingKOT(String ItemName, String Time, int CategCode, int CustId, int DeptCode, int EmployeeId,
                      int ItemNumber, int KitchenCode, int SubUdfNumber, int TableNumber, int TokenNumber, float Amount,
                      float DiscountAmount, float DiscountPercent, float Quantity, float Rate, float TaxAmount, float TaxPercent,
                      int TaxType, float ModifierAmount, float ServiceTaxAmount, float ServiceTaxPercent, int OrderMode,
                      int IsCheckedOut, int TableSplitNo, String hsn,String pos, int PrintKOTStatus) {
        this.strItemName = ItemName;
        this.strTime = Time;
        this.iCategCode = CategCode;
        this.iCustId = CustId;
        this.iDeptCode = DeptCode;
        this.iEmployeeId = EmployeeId;
        this.iItemNumber = ItemNumber;
        this.iKitchenCode = KitchenCode;
        this.iSubUdfNumber = SubUdfNumber;
        this.iTableNumber = TableNumber;
        this.iTokenNumber = TokenNumber;
        this.fAmount = Amount;
        this.fDiscountAmount = DiscountAmount;
        this.fDiscountPercent = DiscountPercent;
        this.fQuantity = Quantity;
        this.fRate = Rate;
        this.fTaxAmount = TaxAmount;
        this.fTaxPercent = TaxPercent;
        this.iTaxType = TaxType;
        this.fModifierAmount = ModifierAmount;
        this.fServiceTaxAmount = ServiceTaxAmount;
        this.fServiceTaxPercent = ServiceTaxPercent;
        this.iOrderMode = OrderMode;
        this.iIsCheckedOut = IsCheckedOut;
        this.iTableSplitNo = TableSplitNo;
        this.HSNCode = hsn;
        this.POS=pos;
        this.iPrintKOTStatus = PrintKOTStatus;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public float getCessRate() {
        return cessRate;
    }

    public void setCessRate(float cessRate) {
        this.cessRate = cessRate;
    }

    public float getCessAmount() {
        return cessAmount;
    }

    public void setCessAmount(float cessAmount) {
        this.cessAmount = cessAmount;
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

    public int getPrintKOTStatus() {
        return iPrintKOTStatus;
    }

    public void setPrintKOTStatus(int PrintKOTStatus) {
        this.iPrintKOTStatus = PrintKOTStatus;
    }

    public String getSupplyType() {
        return SupplyType;
    }

    public void setSupplyType(String supplyType) {
        SupplyType = supplyType;
    }

    public String getPOS() {
        return POS;
    }

    public void setPOS(String POS) {
        this.POS = POS;
    }

    public String getHSNCode() {
        return HSNCode;
    }

    public void setHSNCode(String HSNCode) {
        this.HSNCode = HSNCode;
    }

    // getting ItemNumber
    public int getItemNumber() {
        return this.iItemNumber;
    }

    // getting ItemName
    public String getItemName() {
        return this.strItemName;
    }

    // getting Time
    public String getTime() {
        return this.strTime;
    }

    // getting CategCode
    public int getCategCode() {
        return this.iCategCode;
    }

    // getting CustId
    public int getCusId() {
        return this.iCustId;
    }

    // getting DeptCode
    public int getDeptCode() {
        return this.iDeptCode;
    }

    // getting EmployeeId
    public int getEmployeeId() {
        return this.iEmployeeId;
    }

    // getting KitchenCode
    public int getKitchenCode() {
        return this.iKitchenCode;
    }

    // getting SubUdfNumber
    public int getSubUdfNumber() {
        return this.iSubUdfNumber;
    }

    // getting TableNumber
    public int getTableNumber() {
        return this.iTableNumber;
    }

    // getting TokenNumber
    public int getTokenNumber() {
        return this.iTokenNumber;
    }

    // getting Rate
    public float getRate() {
        return this.fRate;
    }

    // getting Amount
    public float getAmount() {
        return this.fAmount;
    }

    // getting DiscountAmount
    public float getDiscountAmount() {
        return this.fDiscountAmount;
    }

    // getting DiscountPercent
    public float getDiscountPercent() {
        return this.fDiscountPercent;
    }

    // getting Quantity
    public float getQuantity() {
        return this.fQuantity;
    }

    // getting TaxAmount
    public float getTaxAmount() {
        return this.fTaxAmount;
    }

    // getting TaxPercent
    public float getTaxPercent() {
        return this.fTaxPercent;
    }

    // getting TaxType
    public int getTaxType() {
        return this.iTaxType;
    }

    // getting ModifierAmount
    public float getModifierAmount() {
        return this.fModifierAmount;
    }

    // getting ServiceTaxAmount
    public float getServiceTaxAmount() {
        return this.fServiceTaxAmount;
    }

    // getting TaxPercent
    public float getServiceTaxPercent() {
        return this.fServiceTaxPercent;
    }

    // getting OrderMode
    public int getOrderMode() {
        return this.iOrderMode;
    }

    // getting IsCheckedOut
    public int getIsCheckedOut() {
        return this.iIsCheckedOut;
    }

    // getting Table Split No
    public int getTableSplitNo() {
        return this.iTableSplitNo;
    }

    // setting ItemNumber
    public void setItemNumber(int ItemNumber) {
        this.iItemNumber = ItemNumber;
    }

    // setting ItemName
    public void setItemName(String ItemName) {
        this.strItemName = ItemName;
    }

    // setting Time
    public void setTime(String Time) {
        this.strTime = Time;
    }

    // setting CategCode
    public void setCategCode(int CategCode) {
        this.iCategCode = CategCode;
    }

    // setting CustId
    public void setCusId(int CustId) {
        this.iCustId = CustId;
    }

    // setting DeptCode
    public void setDeptCode(int DeptCode) {
        this.iDeptCode = DeptCode;
    }

    // setting EmployeeId
    public void setEmployeeId(int EmployeeId) {
        this.iEmployeeId = EmployeeId;
    }

    // setting KitchenCode
    public void setKitchenCode(int KitchenCode) {
        this.iKitchenCode = KitchenCode;
    }

    // setting SubUdfNumber
    public void setSubUdfNumber(int SubUdfNumber) {
        this.iSubUdfNumber = SubUdfNumber;
    }

    // setting TableNumber
    public void setTableNumber(int TableNumber) {
        this.iTableNumber = TableNumber;
    }

    // setting TokenNumber
    public void setTokenNumber(int TokenNumber) {
        this.iTokenNumber = TokenNumber;
    }

    // setting Rate
    public void setRate(float Rate) {
        this.fRate = Rate;
    }

    // setting Amount
    public void setAmount(float Amount) {
        this.fAmount = Amount;
    }

    // setting DiscountAmount
    public void setDiscountAmount(float DiscountAmount) {
        this.fDiscountAmount = DiscountAmount;
    }

    // setting DiscountPercent
    public void setDiscountPercent(float DiscountPercent) {
        this.fDiscountPercent = DiscountPercent;
    }

    // setting Quantity
    public void setQuantity(float Quantity) {
        this.fQuantity = Quantity;
    }

    // setting TaxAmount
    public void setTaxAmount(float TaxAmount) {
        this.fTaxAmount = TaxAmount;
    }

    // setting TaxPercent
    public void setTaxPercent(float TaxPercent) {
        this.fTaxPercent = TaxPercent;
    }

    // setting TaxType
    public void setTaxType(int TaxType) {
        this.iTaxType = TaxType;
    }

    // setting ModifierAmount
    public void setModifierAmount(float ModifierAmount) {
        this.fModifierAmount = ModifierAmount;
    }

    // setting TaxAmount
    public void setServiceTaxAmount(float ServiceTaxAmount) {
        this.fServiceTaxAmount = ServiceTaxAmount;
    }

    // setting TaxPercent
    public void setServiceTaxPercent(float ServiceTaxPercent) {
        this.fServiceTaxPercent = ServiceTaxPercent;
    }

    // setting OrderMode
    public void setOrderMode(int OrderMode) {
        this.iOrderMode = OrderMode;
    }

    // setting IsCheckedOut
    public void setIsCheckedOut(int IsCheckedOut) {
        this.iIsCheckedOut = IsCheckedOut;
    }

    // setting TableSplitNo
    public void setTableSplitNo(int TableSplitNo) {
        this.iTableSplitNo = TableSplitNo;
    }

}
