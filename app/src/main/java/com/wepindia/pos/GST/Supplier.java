package com.wepindia.pos.GST;

/**
 * Created by welcome on 28-11-2016.
 */

public class Supplier {
    String supplierType;
    String suppliergstin;
    String suppliername;
    String supplierphone;
    String supplieraddress;


    public Supplier() {
        this.supplierType="";
        this.suppliergstin="";
        this.suppliername="";
        this.supplierphone="";
        this.supplieraddress="";
    }

    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    public String getSuppliergstin() {
        return suppliergstin;
    }

    public void setSuppliergstin(String suppliergstin) {
        this.suppliergstin = suppliergstin;
    }

    public String getSuppliername() {
        return suppliername;
    }

    public void setSuppliername(String suppliername) {
        this.suppliername = suppliername;
    }

    public String getSupplierphone() {
        return supplierphone;
    }

    public void setSupplierphone(String supplierphone) {
        this.supplierphone = supplierphone;
    }

    public String getSupplieraddress() {
        return supplieraddress;
    }

    public void setSupplieraddress(String supplieraddress) {
        this.supplieraddress = supplieraddress;
    }
}
