package com.wep.common.app.models;

/**
 * Created by RichaA on 6/7/2017.
 */

public class SupplierItemLinkageModel {

    private int supplierCode;
    private String supplierName;
    private String supplierPhone;
    private String supplierAddress;
    private String supplierGSTIN;
    private int menuCode;
    private String itemName;
    private String barCode;
    private String hsnCode;
    private String supplyType;
    private String uom;
    private double averageRate;
    private double IGSTRate;
    private double CGSTRate;
    private double SGSTRate;
    private double cessRate;

    public SupplierItemLinkageModel(int supplierCode, String supplierName, String supplierPhone,
                                    String supplierAddress, String supplierGSTIN, int menuCode,
                                    String itemName, String barCode,String hsnCode, String supplyType, String uom, double averageRate, double IGSTRate, double CGSTRate, double SGSTRate, double cessRate) {
        this.supplierCode = supplierCode;
        this.supplierName = supplierName;
        this.supplierPhone = supplierPhone;
        this.supplierAddress = supplierAddress;
        this.supplierGSTIN = supplierGSTIN;
        this.menuCode = menuCode;
        this.itemName = itemName;
        this.barCode = barCode;
        this.hsnCode = hsnCode;
        this.supplyType = supplyType;
        this.uom = uom;
        this.averageRate = averageRate;
        this.IGSTRate = IGSTRate;
        this.CGSTRate = CGSTRate;
        this.SGSTRate = SGSTRate;
        this.cessRate = cessRate;
    }
    public SupplierItemLinkageModel() {
        this.supplierCode = -1;
        this.supplierName = "";
        this.supplierPhone = "";
        this.supplierAddress = "";
        this.supplierGSTIN = "";
        this.menuCode = -1;
        this.itemName = "";
        this.barCode = "";
        this.supplyType = "G";
        this.uom = "";
        this.averageRate = 0;
        this.IGSTRate = 0;
        this.CGSTRate = 0;
        this.SGSTRate = 0;
        this.cessRate = 0;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public int getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(int supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public void setSupplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getSupplierGSTIN() {
        return supplierGSTIN;
    }

    public void setSupplierGSTIN(String supplierGSTIN) {
        this.supplierGSTIN = supplierGSTIN;
    }

    public int getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(int menuCode) {
        this.menuCode = menuCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getSupplyType() {
        return supplyType;
    }

    public void setSupplyType(String supplyType) {
        this.supplyType = supplyType;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public double getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(double averageRate) {
        this.averageRate = averageRate;
    }

    public double getIGSTRate() {
        return IGSTRate;
    }

    public void setIGSTRate(double IGSTRate) {
        this.IGSTRate = IGSTRate;
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

    public double getCessRate() {
        return cessRate;
    }

    public void setCessRate(double cessRate) {
        this.cessRate = cessRate;
    }
}
