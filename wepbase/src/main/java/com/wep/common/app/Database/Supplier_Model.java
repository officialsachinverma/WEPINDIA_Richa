package com.wep.common.app.Database;

/**
 * Created by RichaA on 3/15/2017.
 */

public class Supplier_Model {
    // Private variables
    String SupplierName;
    String SupplierGSTIN;
    int SupplierCode;
    String SupplierPhone;
    String SupplierAddress;

    public Supplier_Model() {
        SupplierName = "";
        SupplierGSTIN = "";
        SupplierAddress="";
        SupplierCode=-1;
        SupplierPhone = "";
    }

    public Supplier_Model(int supplierCode,  String supplierGSTIN,String supplierName,  String supplierPhone, String supplierAddress) {
        SupplierName = supplierName;
        SupplierGSTIN = supplierGSTIN;
        SupplierCode = supplierCode;
        SupplierPhone = supplierPhone;
        SupplierAddress = supplierAddress;
    }

    public String getSupplierGSTIN() {
        return SupplierGSTIN;
    }

    public void setSupplierGSTIN(String supplierGSTIN) {
        SupplierGSTIN = supplierGSTIN;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }

    public int getSupplierCode() {
        return SupplierCode;
    }

    public void setSupplierCode(int supplierCode) {
        SupplierCode = supplierCode;
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
}
