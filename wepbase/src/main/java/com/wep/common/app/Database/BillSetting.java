/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	BillSetting
 * 
 * Purpose			:	Represents BillSetting table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	15-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package com.wep.common.app.Database;

public class BillSetting {

	// Private variables
	String strBusinessDate, strHeaderText, strFooterText, strSubUdfText, strTIN, strDineIn1Caption, strDineIn2Caption,
			strDineIn3Caption;
	int iDineIn1From, iDineIn1To, iDineIn2From, iDineIn2To, iDineIn3From, iDineIn3To;
	int iKOTType, iPrintKOT, iMaxTables, iMaxWaiter, iPOSNumber, iServiceTaxType, iWeighScale;
	float fServiceTaxpercent;

	String DineInCaption, CounterSalesCaption, TakeAwayCaption, HomeDeliveryCaption;

	int iLoginWith, iPeripherals;
	int iDateAndTime, iPriceChange, iBillwithStock, iBillwithoutStock, iTax, iTaxType, iKOT, iToken, iKitchen,iDiscountType;
	int iOtherChargesItemwise, iOtherChargesBillwise, iRestoreDefault;
	int idinein, icountersales, ipickup, ihomedelivery, GSTEnable;
	int fastBillingMode, iItemNoReset, iPrintPreview;
	int CummulativeHeadingEnable ; // richa_2012
	int iTableSpliting;
	int GSTIN, GSTIN_out, POS, POS_out, HSNCode, HSNCode_out, ReverseCharge, ReverseCharge_out;

	// Default constructor
	public BillSetting() {

		DineInCaption="";
		CounterSalesCaption= "";
		TakeAwayCaption="";
		HomeDeliveryCaption="";
		fastBillingMode=0;
		this.strBusinessDate = "";
		this.strHeaderText = "";
		this.strFooterText = "";
		this.strSubUdfText = "";
		this.strTIN = "";
		this.iDineIn1From = 0;
		this.iDineIn1To = 0;
		this.iDineIn2From = 0;
		this.iDineIn2To = 0;
		this.iDineIn3From = 0;
		this.iDineIn3To = 0;
		this.iKOTType = 0;
		this.iPrintKOT = 0;
		this.iMaxTables = 0;
		this.iMaxWaiter = 0;
		this.iPOSNumber = 0;
		this.iServiceTaxType = 0;
		this.fServiceTaxpercent = 0;
		this.iWeighScale = 0;
		this.strDineIn1Caption = "";
		this.strDineIn2Caption = "";
		this.strDineIn3Caption = "";

		this.iLoginWith = 0;
		this.iDateAndTime = 0;
		this.iPriceChange = 0;
		this.iBillwithStock = 0;
		this.iBillwithoutStock = 0;
		this.iTax = 0;
		this.iTaxType = 0;
		this.iDiscountType = 0;
		this.iKOT = 0;
		this.iToken = 0;
		this.iKitchen = 0;
		this.iOtherChargesItemwise = 0;
		this.iOtherChargesBillwise = 0;
		this.iPeripherals = 0;
		this.iRestoreDefault = 0;

		this.idinein = 0;
		this.icountersales = 0;
		this.ipickup = 0;
		this.ihomedelivery = 0;

		// GST
		this.GSTIN = 0;
		this.POS = 0;
		this.HSNCode =0;
		this.ReverseCharge =0;

		this.GSTIN_out = 0;
		this.POS_out = 0;
		this.HSNCode_out =0;
		this.ReverseCharge_out =0;
		this.GSTEnable = 1;

		this.iItemNoReset = 0;
		this.iPrintPreview = 0;
		this.iTableSpliting = 0;
	}

	// Parameterized constructor
	public BillSetting(String BusinessDate, String HeaderText, String FooterText, String SubUdfText, String TIN,
			int DineIn1From, int DineIn1To, int DineIn2From, int DineIn2To, int DineIn3From, int DineIn3To,
			int WeighScale, int KOTType, int PrintKOT, int MaxTable, int MaxWaiter, int POSNumber, int ServiceTaxType,
			float ServiceTaxPercent, String DineIn1Caption, String DineIn2Caption, String DineIn3Caption,
			int LoginWith, int DateAndtime, int PriceChange, int BillwithStock, int BillwithoutStock, int Tax,
			int TaxType, int KOT, int Token, int Kitchen, int OtherChargesItemwise, int OtherChargesBillwise,
			int Peripherals, int RestoreDefault, int DineIn, int CounterSales, int PickUp, int HomeDelivery, int GSTIN, int POS,
			int HSNCode, int ReverseCharge , int GSTIN_out, int POS_out, int HSNCode_out, int ReverseCharge_out, int GSTEnable,
			int fastBillingMode, int ItemoNoReset, int PrintPreview, int TableSpliting) {

		this.strBusinessDate = BusinessDate;
		this.strHeaderText = HeaderText;
		this.strFooterText = FooterText;
		this.strSubUdfText = SubUdfText;
		this.strTIN = TIN;
		this.iDineIn1From = DineIn1From;
		this.iDineIn1To = DineIn1To;
		this.iDineIn2From = DineIn2From;
		this.iDineIn2To = DineIn2To;
		this.iDineIn3From = DineIn3From;
		this.iDineIn3To = DineIn3To;
		this.iKOTType = KOTType;
		this.iPrintKOT = PrintKOT;
		this.iMaxTables = MaxTable;
		this.iMaxWaiter = MaxWaiter;
		this.iPOSNumber = POSNumber;
		this.iServiceTaxType = ServiceTaxType;
		this.fServiceTaxpercent = ServiceTaxPercent;
		this.iWeighScale = WeighScale;
		this.strDineIn1Caption = DineIn1Caption;
		this.strDineIn2Caption = DineIn2Caption;
		this.strDineIn3Caption = DineIn3Caption;

		this.iLoginWith = LoginWith;
		this.iDateAndTime = DateAndtime;
		this.iPriceChange = PriceChange;
		this.iBillwithStock = BillwithStock;
		this.iBillwithoutStock = BillwithoutStock;
		this.iTax = Tax;
		this.iTaxType = TaxType;
		this.fastBillingMode = fastBillingMode;
		this.iKOT = KOT;
		this.iToken = Token;
		this.iKitchen = Kitchen;
		this.iOtherChargesItemwise = OtherChargesItemwise;
		this.iOtherChargesBillwise = OtherChargesBillwise;
		this.iPeripherals = Peripherals;
		this.iRestoreDefault = RestoreDefault;

		this.idinein = DineIn;
		this.icountersales = CounterSales;
		this.ipickup = PickUp;
		this.ihomedelivery = HomeDelivery;

        this.iItemNoReset = ItemoNoReset;
        this.iPrintPreview = PrintPreview;
		this.iTableSpliting = TableSpliting;

		// GST

		this.GSTIN = GSTIN;
		this.POS  = POS;
		this.HSNCode = HSNCode;
		this.ReverseCharge = ReverseCharge;

		this.GSTIN_out = GSTIN_out;
		this.POS_out = POS_out;
		this.HSNCode_out = HSNCode_out;
		this.ReverseCharge_out = ReverseCharge_out;
		this.GSTEnable = GSTEnable;
	}

	public int getTableSpliting() {
		return iTableSpliting;
	}

	public void setTableSpliting(int TableSpliting) {
		this.iTableSpliting = TableSpliting;
	}

    //richa_2012 starts

    public int getCummulativeHeadingEnable() {
        return CummulativeHeadingEnable;
    }

    public void setCummulativeHeadingEnable(int CummulativeHeadingEnable) {
        this.CummulativeHeadingEnable = CummulativeHeadingEnable;
    }


    // richa_2012 ends

	public String getDineInCaption() {
		return DineInCaption;
	}

	public void setDineInCaption(String dineInCaption) {
		DineInCaption = dineInCaption;
	}

	public String getCounterSalesCaption() {
		return CounterSalesCaption;
	}

	public void setCounterSalesCaption(String counterSalesCaption) {
		CounterSalesCaption = counterSalesCaption;
	}

	public String getTakeAwayCaption() {
		return TakeAwayCaption;
	}

	public void setTakeAwayCaption(String takeAwayCaption) {
		TakeAwayCaption = takeAwayCaption;
	}

	public String getHomeDeliveryCaption() {
		return HomeDeliveryCaption;
	}

	public void setHomeDeliveryCaption(String homeDeliveryCaption) {
		HomeDeliveryCaption = homeDeliveryCaption;
	}

	public int getFastBillingMode() {
		return fastBillingMode;
	}

	public void setFastBillingMode(int fastBillingMode) {
		this.fastBillingMode = fastBillingMode;
	}

	public int getGSTEnable() {
		return GSTEnable;
	}

	public void setGSTEnable(int GSTEnable) {
		this.GSTEnable = GSTEnable;
	}

	public int getGSTIN() {
		return GSTIN;
	}

	public void setGSTIN(int GSTIN) {
		this.GSTIN = GSTIN;
	}

	public int getGSTIN_out() {
		return GSTIN_out;
	}

	public void setGSTIN_out(int GSTIN_out) {
		this.GSTIN_out = GSTIN_out;
	}

	public int getPOS() {
		return POS;
	}

	public void setPOS(int POS) {
		this.POS = POS;
	}

	public int getPOS_out() {
		return POS_out;
	}

	public void setPOS_out(int POS_out) {
		this.POS_out = POS_out;
	}

	public int getHSNCode() {
		return HSNCode;
	}

	public void setHSNCode(int HSNCode) {
		this.HSNCode = HSNCode;
	}

	public int getHSNCode_out() {
		return HSNCode_out;
	}

	public void setHSNCode_out(int HSNCode_out) {
		this.HSNCode_out = HSNCode_out;
	}

	public int getReverseCharge() {
		return ReverseCharge;
	}

	public void setReverseCharge(int reverseCharge) {
		ReverseCharge = reverseCharge;
	}

	public int getReverseCharge_out() {
		return ReverseCharge_out;
	}

	public void setReverseCharge_out(int reverseCharge_out) {
		ReverseCharge_out = reverseCharge_out;
	}

	// getting BusinessDate
	public String getBusinessDate() {
		return this.strBusinessDate;
	}

	// getting HeaderText
	public String getHeaderText() {
		return this.strHeaderText;
	}

	// getting FooterText
	public String getFooterText() {
		return this.strFooterText;
	}

	// getting SubUdfText
	public String getSubUdfText() {
		return this.strSubUdfText;
	}

	// getting TIN
	public String getTIN() {
		return this.strTIN;
	}

	// getting DineIn1From
	public int getDineIn1From() {
		return this.iDineIn1From;
	}

	// getting DineIn1To
	public int getDineIn1To() {
		return this.iDineIn1To;
	}

	// getting DineIn2From
	public int getDineIn2From() {
		return this.iDineIn2From;
	}

	// getting DineIn2To
	public int getDineIn2To() {
		return this.iDineIn2To;
	}

	// getting DineIn3From
	public int getDineIn3From() {
		return this.iDineIn3From;
	}

	// getting DineIn3To
	public int getDineIn3To() {
		return this.iDineIn3To;
	}

	// getting KOTType
	public int getKOTType() {
		return this.iKOTType;
	}

	// getting PrintKOT
	public int getPrintKOT() {
		return this.iPrintKOT;
	}

	// getting MaxTable
	public int getMaxTable() {
		return this.iMaxTables;
	}

	// getting MaxWaiter
	public int getMaxWaiter() {
		return this.iMaxWaiter;
	}

	// getting POSNumber
	public int getPOSNumber() {
		return this.iPOSNumber;
	}

	// getting Service Tax Type
	public int getServiceTaxType() {
		return this.iServiceTaxType;
	}

	// getting Service tax percent
	public float getServiceTaxPercent() {
		return this.fServiceTaxpercent;
	}

	// getting WeighScale
	public int getWeighScale() {
		return this.iWeighScale;
	}

	// getting DineIn1Caption
	public String getDineIn1Caption() {
		return this.strDineIn1Caption;
	}

	// getting DineIn2Caption
	public String getDineIn2Caption() {
		return this.strDineIn2Caption;
	}

	// getting DineIn3Caption
	public String getDineIn3Caption() {
		return this.strDineIn3Caption;
	}

	// getting Login With
	public int getLoginWith() {
		return this.iLoginWith;
	}

	public int getDateAndTime() {
		return this.iDateAndTime;
	}

	public int getPriceChange() {
		return this.iPriceChange;
	}

	public int getBillwithStock() {
		return this.iBillwithStock;
	}

	public int getBillwithoutStock() {
		return this.iBillwithoutStock;
	}

	public int getTax() {
		return this.iTax;
	}

	public int getTaxType() {
		return this.iTaxType;
	}
	public int getDiscountType() {
		return this.iDiscountType;
	}

	public int getKOT() {
		return this.iKOT;
	}

	public int getToken() {
		return this.iToken;
	}

	public int getKitchen() {
		return this.iKitchen;
	}

	public int getOtherChargesItemwise() {
		return this.iOtherChargesItemwise;
	}

	public int getOtherChargesBillwise() {
		return this.iOtherChargesBillwise;
	}

	public int getPeripherals() {
		return this.iPeripherals;
	}

	public int getRestoreDefault() {
		return this.iRestoreDefault;
	}

	public int getDineInRate() {
		return this.idinein;
	}

	public int getCounterSalesRate() {
		return this.icountersales;
	}

	public int getPickUpRate() {
		return this.ipickup;
	}

	public int getHomeDeliveryRate() {
		return this.ihomedelivery;
	}

    public int getItemNoReset() {
        return this.iItemNoReset;
    }

    public int getPrintPreview() {
        return this.iPrintPreview;
    }

	// setting BusinessDate
	public void setBusinessDate(String BusinessDate) {
		this.strBusinessDate = BusinessDate;
	}

	// setting HeaderText
	public void setHeaderText(String HeaderText) {
		this.strHeaderText = HeaderText;
	}

	// setting FooterText
	public void setFooterText(String FooterText) {
		this.strFooterText = FooterText;
	}

	// setting SubUdfText
	public void setSubUdfText(String SubUdfText) {
		this.strSubUdfText = SubUdfText;
	}

	// setting TIN
	public void setTIN(String TIN) {
		this.strTIN = TIN;
	}

	// setting DineIn1From
	public void setDineIn1From(int DineIn1From) {
		this.iDineIn1From = DineIn1From;
	}

	// setting DineIn1To
	public void setDineIn1To(int DineIn1To) {
		this.iDineIn1To = DineIn1To;
	}

	// setting DineIn2From
	public void setDineIn2From(int DineIn2From) {
		this.iDineIn2From = DineIn2From;
	}

	// setting DineIn2To
	public void setDineIn2To(int DineIn2To) {
		this.iDineIn2To = DineIn2To;
	}

	// setting DineIn3From
	public void setDineIn3From(int DineIn3From) {
		this.iDineIn3From = DineIn3From;
	}

	// setting DineIn3To
	public void setDineIn3To(int DineIn3To) {
		this.iDineIn3To = DineIn3To;
	}

	// setting KOTType
	public void setKOTType(int KOTType) {
		this.iKOTType = KOTType;
	}

	// setting PrintKOT
	public void setPrintKOT(int PrintKOT) {
		this.iPrintKOT = PrintKOT;
	}

	// setting MaxTable
	public void setMaxTable(int MaxTable) {
		this.iMaxTables = MaxTable;
	}

	// setting MaxWaiter
	public void setMaxWaiter(int MaxWaiter) {
		this.iMaxWaiter = MaxWaiter;
	}

	// setting POSNumber
	public void setPOSNumber(int POSNumber) {
		this.iPOSNumber = POSNumber;
	}

	// getting Service Tax Type
	public void setServiceTaxType(int ServiceTaxType) {
		this.iServiceTaxType = ServiceTaxType;
	}

	// getting Service tax percent
	public void setServiceTaxPercent(float ServiceTaxpercent) {
		this.fServiceTaxpercent = ServiceTaxpercent;
	}

	// setting WeighScale
	public void setWeighScale(int WeighScale) {
		this.iWeighScale = WeighScale;
	}

	// setting DineIn1Caption
	public void setDineIn1Caption(String DineIn1Caption) {
		this.strDineIn1Caption = DineIn1Caption;
	}

	// setting DineIn2Caption
	public void setDineIn2Caption(String DineIn2Caption) {
		this.strDineIn2Caption = DineIn2Caption;
	}

	// setting DineIn3Caption
	public void setDineIn3Caption(String DineIn3Caption) {
		this.strDineIn3Caption = DineIn3Caption;
	}

	// setting Login with
	public void setLoginWith(int LoginWith) {
		this.iLoginWith = LoginWith;
	}

	public void setDateAndTime(int DateAndTime) {
		this.iDateAndTime = DateAndTime;
	}

	public void setPriceChange(int PriceChange) {
		this.iPriceChange = PriceChange;
	}

	public void setBillwithStock(int BillwithStock) {
		this.iBillwithStock = BillwithStock;
	}

	public void setBillwithoutStock(int BillwithoutStock) {
		this.iBillwithoutStock = BillwithoutStock;
	}

	public void setTax(int Tax) {
		this.iTax = Tax;
	}

	public void setTaxType(int TaxType) {
		this.iTaxType = TaxType;
	}

	public void setDiscountType(int DiscountType) {
		this.iDiscountType = DiscountType;
	}

	public void setKOT(int KOT) {
		this.iKOT = KOT;
	}

	public void setToken(int Token) {
		this.iToken = Token;
	}

	public void setKitchen(int Kitchen) {
		this.iKitchen = Kitchen;
	}

	public void setOtherChargesItemwise(int OtherChargesItemwise) {
		this.iOtherChargesItemwise = OtherChargesItemwise;
	}

	public void setOtherChargesBillwise(int OtherChargesBillwise) {
		this.iOtherChargesBillwise = OtherChargesBillwise;
	}

	public void setPeripherals(int Peripherals) {
		this.iPeripherals = Peripherals;
	}

	public void setRestoreDefault(int RestoreDefault) {
		this.iRestoreDefault = RestoreDefault;
	}

	public void setDineInRate(int dinein) {
		this.idinein = dinein;
	}

	public void setCounterSalesRate(int countersales) {
		this.icountersales = countersales;
	}

	public void setPickUpRate(int pickup) {
		this.ipickup = pickup;
	}

	public void setHomeDeliveryRate(int homedelivery) {
		this.ihomedelivery = homedelivery;
	}

    public void setItemNoReset(int ItemNoReset) {
        this.iItemNoReset = ItemNoReset;
    }

    public void setPrintPreview(int PrintPreview) {
        this.iPrintPreview = PrintPreview;
    }

}
