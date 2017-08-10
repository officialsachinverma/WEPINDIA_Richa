/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	Item
 * 
 * Purpose			:	Represents Item table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	15-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package com.wep.common.app.Database;

public class Item {
	
	// Private variable
	String strItemname, strItemBarcode, strImageUri,  strsupplierName;
	int isuppliercode,iMenuCode, iDeptCode, iCategCode, iKitchenCode, iSalesTaxId, iAdditionalTaxId, iOptionalTaxId1, iOptionalTaxId2, iDiscId,
	iPriceChange, iBillWithStock, iDiscountEnable, iTaxType;
	float fDineInPrice1, fDineInPrice2, fDineInPrice3, fTakeAwayPrice, fPickUpPrice, fDeliveryPrice, fQuantity;
	float IGSTRate, IGSTAmount, CGSTRate, CGSTAmount,SGSTRate, SGSTAmount, Rate;
	String MOU,HSNCode,TaxationType , SupplyType, pos;
	float fSalesTaxPercent, fServiceTaxPercent, AverageRate;
	int count ;

	
	// Default Constructor
	public Item(){
		this.pos="";
		this.strItemBarcode = "";
		this.strItemname = "";
		this.isuppliercode=0;
		this.strsupplierName="";
		this.iAdditionalTaxId = 0;
		this.iBillWithStock = 0;
		this.iCategCode = 0;
		this.iDeptCode = 0;
		this.iDiscId = 0;
		this.iDiscountEnable = 0;
		this.iKitchenCode = 0;
		this.iMenuCode = 0;
		this.iOptionalTaxId1 = 0;
		this.iOptionalTaxId2 = 0;
		this.iPriceChange = 0;
		this.iSalesTaxId = 0;
		this.iTaxType = 0;
		this.fDeliveryPrice = 0;
		this.fDineInPrice1 = 0;
		this.fDineInPrice2 = 0;
		this.fDineInPrice3 = 0;
		this.fPickUpPrice = 0;
		this.fQuantity = 0;
		this.fTakeAwayPrice = 0;
		this.strImageUri = "";

		this.IGSTAmount = 0;
		this.IGSTRate = 0;
		this.CGSTRate = 0;
		this.CGSTAmount = 0;
		this.SGSTRate = 0;
		this.SGSTAmount = 0;
		this.TaxationType = "";
		this.MOU = "";
		this.HSNCode="";
        this.Rate= 0;
		this.SupplyType= "";

		this.fSalesTaxPercent = 0;
		this.fServiceTaxPercent = 0;
		this.AverageRate = 0;
		this.count = 0;
	}


    // Parameterized constructor
	public Item(String ItemBarcode,String Itemname,String ShortName,int AdditionalTaxId,int BillWithStock,int CategCode,int DeptCode,
			int DiscId,int DiscountEnable,int KitchenCode,int OptionalTaxId1,int OptionalTaxId2,int PriceChange,int SalesTaxId,
			int TaxType,float DeliveryPrice,float DineInPrice1,float DineInPrice2,float DineInPrice3,float PickUpPrice,float Quantity,
				float TakeAwayPrice,String ImageUri,String HSNCode, float IGSTRate, float IGSTAmount, float CGSTRate,
				float CGSTAmount, float SGSTRate, float SGSTAmount,String MOU,String TaxationType , float Rate, String supplyType,
                float SalesTaxPercent, float ServiceTaxPercent, int MenuCode , float AverageRate, int count){

        this.iMenuCode = MenuCode;
		this.strItemBarcode = ItemBarcode;
		this.strItemname = Itemname;
		this.iAdditionalTaxId = AdditionalTaxId;
		this.iBillWithStock = BillWithStock;
		this.iCategCode = CategCode;
		this.iDeptCode = DeptCode;
		this.iDiscId = DiscId;
		this.iDiscountEnable = DiscountEnable;
		this.iKitchenCode = KitchenCode;
		//this.iMenuCode = MenuCode;
		this.iOptionalTaxId1 = OptionalTaxId1;
		this.iOptionalTaxId2 = OptionalTaxId2;
		this.iPriceChange = PriceChange;
		this.iSalesTaxId = SalesTaxId;
		this.iTaxType = TaxType;
		this.fDeliveryPrice = DeliveryPrice;
		this.fDineInPrice1 = DineInPrice1;
		this.fDineInPrice2 = DineInPrice2;
		this.fDineInPrice3 = DineInPrice3;
		this.fPickUpPrice = PickUpPrice;
		this.fQuantity = Quantity;
		this.fTakeAwayPrice = TakeAwayPrice;
		this.strImageUri = ImageUri;
		this.HSNCode = HSNCode;
		this.IGSTRate = IGSTRate;
		this.IGSTAmount = IGSTAmount;
		this.CGSTRate = CGSTRate;
		this.CGSTAmount = CGSTAmount;
		this.SGSTRate = SGSTRate;
		this.SGSTAmount = SGSTAmount;
		this.MOU = MOU;
		this.TaxationType = TaxationType;
        this.Rate = Rate;
		this.SupplyType = supplyType;

        this.fSalesTaxPercent = SalesTaxPercent;
        this.fServiceTaxPercent = ServiceTaxPercent;
        this.AverageRate = AverageRate;
        this.count = count;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public float getAverageRate() {
		return AverageRate;
	}

	public void setAverageRate(float averageRate) {
		AverageRate = averageRate;
	}

	public float getRate() {
		return Rate;
	}

	public void setRate(float rate) {
		Rate = rate;
	}

	public String getPOS() {
		return pos;
	}

	public void setPOS(String pos) {
		this.pos = pos;
	}

	public String getsupplierName() {
		return strsupplierName;
	}

	public void setsupplierName(String strsupplierName) {
		this.strsupplierName = strsupplierName;
	}

	public int getsuppliercode() {
		return isuppliercode;
	}

	public void setsuppliercode(int isuppliercode) {
		this.isuppliercode = isuppliercode;
	}

	public String getSupplyType() {
		return SupplyType;
	}

	public void setSupplyType(String supplyType) {
		SupplyType = supplyType;
	}

	public String getTaxationType() {
        return TaxationType;
    }

    public void setTaxationType(String taxationType) {
        TaxationType = taxationType;
    }

    public String getHSNCode() {
        return HSNCode;
    }

    public void setHSNCode(String HSNCode) {
        this.HSNCode = HSNCode;
    }

    public String getMOU() {
        return MOU;
    }

    public void setMOU(String MOU) {
        this.MOU = MOU;
    }

    public float getSGSTAmount() {
        return SGSTAmount;
    }

    public void setSGSTAmount(float SGSTAmount) {
        this.SGSTAmount = SGSTAmount;
    }

    public float getSGSTRate() {
        return SGSTRate;
    }

    public void setSGSTRate(float SGSTRate) {
        this.SGSTRate = SGSTRate;
    }

    public float getCGSTAmount() {
        return CGSTAmount;
    }

    public void setCGSTAmount(float CGSTAmount) {
        this.CGSTAmount = CGSTAmount;
    }

    public float getCGSTRate() {
        return CGSTRate;
    }

    public void setCGSTRate(float CGSTRate) {
        this.CGSTRate = CGSTRate;
    }

    public float getIGSTAmount() {
        return IGSTAmount;
    }

    public void setIGSTAmount(float IGSTAmount) {
        this.IGSTAmount = IGSTAmount;
    }

    public float getIGSTRate() {
        return IGSTRate;
    }

    public void setIGSTRate(float IGSTRate) {
        this.IGSTRate = IGSTRate;
    }

    // getting ItemBarCode
	public String getItemBarcode(){
		return this.strItemBarcode;
	}
	
	// getting Itemname
	public String getItemname(){
		return this.strItemname;
	}
		

	// getting AdditionalTaxId
	public int getAdditionalTaxId(){
		return this.iAdditionalTaxId;
	}
	
	// getting BillWithStock
	public int getBillWithStock(){
		return this.iBillWithStock;
	}
	
	// getting CategCode
	public int getCategCode(){
		return this.iCategCode;
	}
		
	// getting DeptCode
	public int getDeptCode(){
		return this.iDeptCode;
	}
		
	// getting DiscId
	public int getDiscId(){
		return this.iDiscId;
	}
		
	// getting DiscountEnable
	public int getDiscountEnable(){
		return this.iDiscountEnable;
	}
		
	// getting KitchenCode
	public int getKitchenCode(){
		return this.iKitchenCode;
	}
		
	// getting MenuCode
	public int getMenuCode(){
		return this.iMenuCode;
	}
		
	// getting OptionalTaxId1
	public int getOptionalTaxId1(){
		return this.iOptionalTaxId1;
	}
		
	// getting OptionalTaxId2
	public int getOptionalTaxId2(){
		return this.iOptionalTaxId2;
	}
			
	// getting PriceChange
	public int getPriceChange(){
		return this.iPriceChange;
	}
		
	// getting SalesTaxId
	public int getSalesTaxId(){
		return this.iSalesTaxId;
	}
		
	// getting TaxType
	public int getTaxType(){
		return this.iTaxType;
	}
	
	// getting DeliveryPrice
	public float getDeliveryPrice(){
		return this.fDeliveryPrice;
	}
	
	// getting DineInPrice1
	public float getDineInPrice1(){
		return this.fDineInPrice1;
	}
	
	// getting DineInPrice2
	public float getDineInPrice2(){
		return this.fDineInPrice2;
	}
		
	// getting DineInPrice3
	public float getDineInPrice3(){
		return this.fDineInPrice3;
	}
		
	// getting PickUpPrice
	public float getPickUpPrice(){
		return this.fPickUpPrice;
	}
		
	// getting Quantity
	public float getQuantity(){
		return this.fQuantity;
	}
		
	// getting TakeAwayPrice
	public float getTakeAwayPrice(){
		return this.fTakeAwayPrice;
	}
	
	// getting ImageUri
	public String getImageUri(){
		return this.strImageUri;
	}

    // getting SalesTaxPercent
    public float getSalesTaxPercent(){
        return this.fSalesTaxPercent;
    }

    // getting ServiceTaxPercent
    public float getServiceTaxPercent(){
        return this.fServiceTaxPercent;
    }
	
	// setting ItemBarCode
	public void setItemBarcode(String ItemBarCode){
		this.strItemBarcode = ItemBarCode;
	}
		
	// setting Itemname
	public void setItemname(String Itemname){
		this.strItemname = Itemname;
	}
			

	// setting AdditionalTaxId
	public void setAdditionalTaxId(int AdditionalTaxId){
		this.iAdditionalTaxId = AdditionalTaxId;
	}
		
	// setting BillWithStock
	public void setBillWithStock(int BillWithStock){
		this.iBillWithStock = BillWithStock;
	}
		
	// setting CategCode
	public void setCategCode(int CategCode){
		this.iCategCode = CategCode;
	}
			
	// setting DeptCode
	public void setDeptCode(int DeptCode){
		this.iDeptCode = DeptCode;
	}
			
	// setting DiscId
	public void setDiscId(int DiscId){
		this.iDiscId = DiscId;
	}
			
	// setting DiscountEnable
	public void setDiscountEnable(int DiscountEnable){
		this.iDiscountEnable = DiscountEnable;
	}
			
	// setting KitchenCode
	public void setKitchenCode(int KitchenCode){
		this.iKitchenCode = KitchenCode;
	}
			
	// setting MenuCode
	public void setMenuCode(int MenuCode){
		this.iMenuCode = MenuCode;
	}
			
	// setting OptionalTaxId1
	public void setOptionalTaxId1(int OptionalTaxId1){
		this.iOptionalTaxId1 = OptionalTaxId1;
	}
			
	// setting OptionalTaxId2
	public void setOptionalTaxId2(int OptionalTaxId2){
		this.iOptionalTaxId2 = OptionalTaxId2;
	}
				
	// setting PriceChange
	public void setPriceChange(int PriceChange){
		this.iPriceChange = PriceChange;
	}
			
	// setting SalesTaxId
	public void setSalesTaxId(int SalesTaxId){
		this.iSalesTaxId = SalesTaxId;
	}
			
	// setting TaxType
	public void setTaxType(int TaxType){
		this.iTaxType = TaxType;
	}
		
	// setting DeliveryPrice
	public void setDeliveryPrice(int DeliveryPrice){
		this.fDeliveryPrice = DeliveryPrice;
	}
		
	// setting DineInPrice1
	public void setDineInPrice1(float DineInPrice1){
		this.fDineInPrice1 = DineInPrice1;
	}
		
	// setting DineInPrice2
	public void setDineInPrice2(float DineInPrice2){
		this.fDineInPrice2 = DineInPrice2;
	}
			
	// setting DineInPrice3
	public void setDineInPrice3(float DineInPrice3){
		this.fDineInPrice3 = DineInPrice3;
	}
			
	// setting PickUpPrice
	public void setPickUpPrice(float PickUpPrice){
		this.fPickUpPrice = PickUpPrice;
	}
			
	// setting Quantity
	public void setQuantity(float Quantity){
		this.fQuantity = Quantity;
	}
			
	// setting TakeAwayPrice
	public void setTakeAwayPrice(float TakeAwayPrice){
		this.fTakeAwayPrice = TakeAwayPrice;
	}
	
	// setting ImageUri
	public void setImageId(String ImageUri){
		this.strImageUri = ImageUri;
	}

    // setting SalesTaxPercent
    public void setSalesTaxPercent(float SalesTaxPercent){
        this.fSalesTaxPercent = SalesTaxPercent;
    }

    // setting ServiceTaxPercent
    public void setServiceTaxPercent(float ServiceTaxPercent){
        this.fServiceTaxPercent = ServiceTaxPercent;
    }

}
