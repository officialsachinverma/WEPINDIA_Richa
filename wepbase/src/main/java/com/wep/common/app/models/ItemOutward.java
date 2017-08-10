package com.wep.common.app.models;

/**
 * Created by RichaA on 2/13/2017.
 */

public class ItemOutward {


    private int menuCode;
    private String ItemName;
    private double DineIn1;
    private double DineIn2;
    private double DineIn3;
    private double Stock;
    private int DeptCode;
    private int CategCode;
    private int KitchenCode;
    private String BarCode;
    private String ImageUri;
    private int ItemId;
    private double CGSTRate;
    private double SGSTRate;
    private double IGSTRate;
    private double cessRate;
    private String UOM;
    private String HSN;
    private String TaxationType;
    private String SupplyType;
    private  double itemDiscount;

    public ItemOutward() {

        this.menuCode = 0;
        ItemName = "";
        DineIn1 = 0;
        DineIn2 = 0;
        DineIn3 = 0;
        Stock = 0;
        DeptCode = 0;
        CategCode = 0;
        KitchenCode = 0;
        BarCode = "";
        ImageUri = "";
        ItemId = 0;
        CGSTRate = 0;
        SGSTRate = 0;
        IGSTRate = 0;
        cessRate = 0;
        itemDiscount = 0;
        this.UOM = "";
        this.HSN = "";
        this.TaxationType="GST";
        this.SupplyType="G";
    }

    public ItemOutward(int menuCode, String longName, double dineIn1, double dineIn2, double dineIn3, double stock,
                       int deptCode, int categCode, int kitchenCode, String barCode, String imageUri, int itemId,
                       double CGSTRate, double SGSTRate, double IGSTRate, double cessRate, String UOM, String HSN,
                       String taxationType, String SupplyType, double itemDiscount) {
        this.menuCode = menuCode;
        this.ItemName = longName;
        this.DineIn1 = dineIn1;
        this.DineIn2 = dineIn2;
        this.DineIn3 = dineIn3;
        this.Stock = stock;
        this.DeptCode = deptCode;
        this.CategCode = categCode;
        this.KitchenCode = kitchenCode;
        this.BarCode = barCode;
        this.ImageUri = imageUri;
        this.ItemId = itemId;
        this.CGSTRate = CGSTRate;
        this.SGSTRate = SGSTRate;
        this.IGSTRate = IGSTRate;
        this.cessRate = cessRate;
        this.UOM = UOM;
        this.HSN = HSN;
        this.TaxationType = taxationType;
        this.SupplyType = SupplyType;
        this.itemDiscount = itemDiscount;
    }

    public double getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(double itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public String getSupplyType() {
        return SupplyType;
    }

    public void setSupplyType(String supplyType) {
        SupplyType = supplyType;
    }

    public double getCessRate() {
        return cessRate;
    }

    public void setCessRate(double cessRate) {
        this.cessRate = cessRate;
    }

    public String getTaxationType() {
        return TaxationType;
    }

    public void setTaxationType(String taxationType) {
        TaxationType = taxationType;
    }

    public String getHSN() {
        return HSN;
    }

    public void setHSN(String HSN) {
        this.HSN = HSN;
    }

    public int getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(int menuCode) {
        this.menuCode = menuCode;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public double getDineIn1() {
        return DineIn1;
    }

    public void setDineIn1(double dineIn1) {
        DineIn1 = dineIn1;
    }

    public double getDineIn2() {
        return DineIn2;
    }

    public void setDineIn2(double dineIn2) {
        DineIn2 = dineIn2;
    }

    public double getDineIn3() {
        return DineIn3;
    }

    public void setDineIn3(double dineIn3) {
        DineIn3 = dineIn3;
    }

    public double getStock() {
        return Stock;
    }

    public void setStock(double stock) {
        Stock = stock;
    }

    public int getDeptCode() {
        return DeptCode;
    }

    public void setDeptCode(int deptCode) {
        DeptCode = deptCode;
    }

    public int getCategCode() {
        return CategCode;
    }

    public void setCategCode(int categCode) {
        CategCode = categCode;
    }

    public int getKitchenCode() {
        return KitchenCode;
    }

    public void setKitchenCode(int kitchenCode) {
        KitchenCode = kitchenCode;
    }

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(String barCode) {
        BarCode = barCode;
    }

    public String getImageUri() {
        return ImageUri;
    }

    public void setImageUri(String imageUri) {
        ImageUri = imageUri;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public double getCGSTRate() {
        return CGSTRate;
    }

    public void setCGSTRate(double CGSTRate) {
        this.CGSTRate = CGSTRate;
    }

    public double getSGSTRate() {
        return SGSTRate;
    }

    public void setSGSTRate(double SGSTRate) {
        this.SGSTRate = SGSTRate;
    }

    public double getIGSTRate() {
        return IGSTRate;
    }

    public void setIGSTRate(double IGSTRate) {
        this.IGSTRate = IGSTRate;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

}











