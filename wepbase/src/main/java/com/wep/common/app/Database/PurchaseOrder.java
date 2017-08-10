package com.wep.common.app.Database;

/**
 * Created by RichaA on 5/2/2017.
 */

public class PurchaseOrder {

    private int sn;
    private String purchaseOrderNo;
    private String invoiceNo;
    private String invoiceDate;
    private String SupplierCode;
    private String SupplierName;
    private String SupplierPhone;
    private String SupplierAddress;
    private String SupplierGSTIN;
    private String SupplierType;
    private int menuCode;
    private String supplyType;
    private String HSNCode;
    private String itemName;
    private double value;
    private double quantity;
    private String UOM;
    private double taxableValue;
    private double igstRate;
    private double igstAmount;
    private double cgstRate;
    private double cgstAmount;
    private double sgstRate;
    private double sgstAmount;
    private double csRate;
    private double csAmount;
    private double salestaxAmount;
    private double servicetaxAmount;
    private double amount;
    private String additionalCharge;
    private double additionalChargeAmount;
    private String isgoodInward;
    private String SupplierPOS;

    public PurchaseOrder() {
        this.sn = 0;
        this.purchaseOrderNo = "";
        this.invoiceNo ="";
        this.invoiceDate = "";
        SupplierCode = "";
        SupplierName = "";
        SupplierPhone = "";
        SupplierAddress = "";
        SupplierGSTIN = "";
        SupplierType = "";
        this.menuCode = 0;
        this.supplyType = "";
        this.HSNCode = "";
        this.itemName = "";
        this.value = 0;
        this.quantity = 0;
        this.UOM = "";
        this.taxableValue = 0;
        this.igstRate = 0;
        this.igstAmount = 0;
        this.cgstRate = 0;
        this.cgstAmount = 0;
        this.sgstRate = 0;
        this.sgstAmount = 0;
        this.csRate = 0;
        this.csAmount = 0;
        this.salestaxAmount = 0;
        this.servicetaxAmount = 0;
        this.amount = 0;
        this.additionalCharge = "";
        this.additionalChargeAmount = 0;
        this.isgoodInward = "0";
        this.SupplierPOS="";
    }
    public PurchaseOrder(String purchaseOrderNo, String invoiceNo, String invoiceDate, String supplierCode,
                         String supplierName, String supplierPhone, String supplierAddress, String supplierGSTIN,
                         String supplierType, int menuCode, String supplyType, String HSNCode, String itemName,
                         double value, double quantity, String UOM, double taxableValue, double igstRate,
                         double igstAmount, double cgstRate, double cgstAmount, double sgstRate, double sgstAmount,
                         double csRate, double csAmount, double amount, String additionalCharge, double additionalChargeAmount,
                         String isgoodInward,String pos) {
        this.purchaseOrderNo = purchaseOrderNo;
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        SupplierCode = supplierCode;
        SupplierName = supplierName;
        SupplierPhone = supplierPhone;
        SupplierAddress = supplierAddress;
        SupplierGSTIN = supplierGSTIN;
        SupplierType = supplierType;
        this.menuCode = menuCode;
        this.supplyType = supplyType;
        this.HSNCode = HSNCode;
        this.itemName = itemName;
        this.value = value;
        this.quantity = quantity;
        this.UOM = UOM;
        this.taxableValue = taxableValue;
        this.igstRate = igstRate;
        this.igstAmount = igstAmount;
        this.cgstRate = cgstRate;
        this.cgstAmount = cgstAmount;
        this.sgstRate = sgstRate;
        this.sgstAmount = sgstAmount;
        this.csRate = csRate;
        this.csAmount = csAmount;
        this.salestaxAmount = 0;
        this.servicetaxAmount = 0;
        this.amount = amount;
        this.additionalCharge = additionalCharge;
        this.additionalChargeAmount = additionalChargeAmount;
        this.isgoodInward = isgoodInward;
        SupplierPOS = pos;
    }
    public PurchaseOrder(String purchaseOrderNo, String invoiceNo, String invoiceDate, String supplierCode, String supplierName, String supplierPhone, String supplierAddress, String supplierGSTIN, String supplierType, int menuCode, String supplyType, String HSNCode, String itemName, double value, double quantity, String UOM, double taxableValue, double igstRate, double igstAmount, double cgstRate, double cgstAmount, double sgstRate, double sgstAmount, double csRate, double csAmount, double salestaxAmount, double servicetaxAmount, double amount, String additionalCharge, double additionalChargeAmount, String isgoodInward, String pos) {
        this.purchaseOrderNo = purchaseOrderNo;
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        SupplierCode = supplierCode;
        SupplierName = supplierName;
        SupplierPhone = supplierPhone;
        SupplierAddress = supplierAddress;
        SupplierGSTIN = supplierGSTIN;
        SupplierType = supplierType;
        this.menuCode = menuCode;
        this.supplyType = supplyType;
        this.HSNCode = HSNCode;
        this.itemName = itemName;
        this.value = value;
        this.quantity = quantity;
        this.UOM = UOM;
        this.taxableValue = taxableValue;
        this.igstRate = igstRate;
        this.igstAmount = igstAmount;
        this.cgstRate = cgstRate;
        this.cgstAmount = cgstAmount;
        this.sgstRate = sgstRate;
        this.sgstAmount = sgstAmount;
        this.csRate = csRate;
        this.csAmount = csAmount;
        this.salestaxAmount = salestaxAmount;
        this.servicetaxAmount = servicetaxAmount;
        this.amount = amount;
        this.additionalCharge = additionalCharge;
        this.additionalChargeAmount = additionalChargeAmount;
        this.isgoodInward = isgoodInward;
        SupplierPOS = pos;
    }

    public String getSupplierPOS() {
        return SupplierPOS;
    }

    public void setSupplierPOS(String supplierPOS) {
        SupplierPOS = supplierPOS;
    }

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public double getSgstAmount() {
        return sgstAmount;
    }

    public void setSgstAmount(double sgstAmount) {
        this.sgstAmount = sgstAmount;
    }

    public double getCsRate() {
        return csRate;
    }

    public void setCsRate(double csRate) {
        this.csRate = csRate;
    }

    public double getCsAmount() {
        return csAmount;
    }

    public void setCsAmount(double csAmount) {
        this.csAmount = csAmount;
    }

    public double getSalestaxAmount() {
        return salestaxAmount;
    }

    public void setSalestaxAmount(double salestaxAmount) {
        this.salestaxAmount = salestaxAmount;
    }

    public double getServicetaxAmount() {
        return servicetaxAmount;
    }

    public void setServicetaxAmount(double servicetaxAmount) {
        this.servicetaxAmount = servicetaxAmount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAdditionalCharge() {
        return additionalCharge;
    }

    public void setAdditionalCharge(String additionalCharge) {
        this.additionalCharge = additionalCharge;
    }

    public double getAdditionalChargeAmount() {
        return additionalChargeAmount;
    }

    public void setAdditionalChargeAmount(double additionalChargeAmount) {
        this.additionalChargeAmount = additionalChargeAmount;
    }

    public String getIsgoodInward() {
        return isgoodInward;
    }

    public void setIsgoodInward(String isgoodInward) {
        this.isgoodInward = isgoodInward;
    }

    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getSupplierCode() {
        return SupplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        SupplierCode = supplierCode;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }

    public String getSupplierPhone() {
        return SupplierPhone;
    }

    public void setSupplierPhone(String supplierPhone) {
        SupplierPhone = supplierPhone;
    }

    public String getSupplierAddress() {
        return SupplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        SupplierAddress = supplierAddress;
    }

    public String getSupplierGSTIN() {
        return SupplierGSTIN;
    }

    public void setSupplierGSTIN(String supplierGSTIN) {
        SupplierGSTIN = supplierGSTIN;
    }

    public String getSupplierType() {
        return SupplierType;
    }

    public void setSupplierType(String supplierType) {
        SupplierType = supplierType;
    }

    public int getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(int menuCode) {
        this.menuCode = menuCode;
    }

    public String getSupplyType() {
        return supplyType;
    }

    public void setSupplyType(String supplyType) {
        this.supplyType = supplyType;
    }

    public String getHSNCode() {
        return HSNCode;
    }

    public void setHSNCode(String HSNCode) {
        this.HSNCode = HSNCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public double getTaxableValue() {
        return taxableValue;
    }

    public void setTaxableValue(double taxableValue) {
        this.taxableValue = taxableValue;
    }

    public double getIgstRate() {
        return igstRate;
    }

    public void setIgstRate(double igstRate) {
        this.igstRate = igstRate;
    }

    public double getIgstAmount() {
        return igstAmount;
    }

    public void setIgstAmount(double igstAmount) {
        this.igstAmount = igstAmount;
    }

    public double getCgstRate() {
        return cgstRate;
    }

    public void setCgstRate(double cgstRate) {
        this.cgstRate = cgstRate;
    }

    public double getCgstAmount() {
        return cgstAmount;
    }

    public void setCgstAmount(double cgstAmount) {
        this.cgstAmount = cgstAmount;
    }

    public double getSgstRate() {
        return sgstRate;
    }

    public void setSgstRate(double sgstRate) {
        this.sgstRate = sgstRate;
    }
}
