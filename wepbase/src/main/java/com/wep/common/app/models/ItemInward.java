package com.wep.common.app.models;

/**
 * Created by RichaA on 6/5/2017.
 */

public class ItemInward {
    // Private variable
    String strItemname, strItemBarcode, strImageUri;
    int iMenuCode;
    double fAveragerate, fQuantity;
    double IGSTRate, IGSTAmount, CGSTRate, CGSTAmount,SGSTRate, SGSTAmount, cessRate, cessAmount;
    String UOM,HSNCode,TaxationType , SupplyType, pos;

    public ItemInward(String strItemname, String strItemBarcode, String strImageUri, int iMenuCode, double fAveragerate, double fQuantity, double IGSTRate, double IGSTAmount, double CGSTRate,
                      double CGSTAmount, double SGSTRate, double SGSTAmount,double cessRate, double cessAmount, String MOU, String HSNCode, String taxationType, String supplyType, String pos) {
        this.strItemname = strItemname;
        this.strItemBarcode = strItemBarcode;
        this.strImageUri = strImageUri;
        this.iMenuCode = iMenuCode;
        this.fAveragerate = fAveragerate;
        this.fQuantity = fQuantity;
        this.IGSTRate = IGSTRate;
        this.IGSTAmount = IGSTAmount;
        this.CGSTRate = CGSTRate;
        this.CGSTAmount = CGSTAmount;
        this.SGSTRate = SGSTRate;
        this.SGSTAmount = SGSTAmount;
        this.cessRate = cessRate;
        this.cessAmount = cessAmount;
        this.UOM = MOU;
        this.HSNCode = HSNCode;
        TaxationType = taxationType;
        SupplyType = supplyType;
        this.pos = pos;
    }
    public ItemInward(int iMenuCode, String strItemname, String strItemBarcode, String strImageUri, String HSNCode,
                      double fAveragerate,double fQuantity, String MOU,
                      double IGSTRate, double IGSTAmount, double CGSTRate, double CGSTAmount,
                      double SGSTRate, double SGSTAmount,double cessRate, double cessAmount,
                      String taxationType, String supplyType)
    {
        this.strItemname = strItemname;
        this.strItemBarcode = strItemBarcode;
        this.strImageUri = strImageUri;
        this.iMenuCode = iMenuCode;
        this.fAveragerate = fAveragerate;
        this.fQuantity = fQuantity;
        this.IGSTRate = IGSTRate;
        this.IGSTAmount = IGSTAmount;
        this.CGSTRate = CGSTRate;
        this.CGSTAmount = CGSTAmount;
        this.SGSTRate = SGSTRate;
        this.SGSTAmount = SGSTAmount;
        this.cessRate = cessRate;
        this.cessAmount = cessAmount;
        this.UOM = MOU;
        this.HSNCode = HSNCode;
        TaxationType = taxationType;
        SupplyType = supplyType;
        this.pos = "";
    }
    public ItemInward() {
        this.strItemname = "";
        this.strItemBarcode = "";
        this.strImageUri = "";
        this.iMenuCode = -1;
        this.fAveragerate = 0;
        this.fQuantity = 0;
        this.IGSTRate = 0;
        this.IGSTAmount = 0;
        this.CGSTRate = 0;
        this.CGSTAmount = 0;
        this.SGSTRate = 0;
        this.SGSTAmount = 0;
        this.cessRate = 0;
        this.cessAmount = 0;
        this.UOM = "";
        this.HSNCode = "";
        TaxationType = "";
        SupplyType = "";
        this.pos = "";
    }

    public String getStrItemname() {
        return strItemname;
    }

    public void setStrItemname(String strItemname) {
        this.strItemname = strItemname;
    }

    public String getStrItemBarcode() {
        return strItemBarcode;
    }

    public void setStrItemBarcode(String strItemBarcode) {
        this.strItemBarcode = strItemBarcode;
    }

    public String getStrImageUri() {
        return strImageUri;
    }

    public void setStrImageUri(String strImageUri) {
        this.strImageUri = strImageUri;
    }

    public int getiMenuCode() {
        return iMenuCode;
    }

    public void setiMenuCode(int iMenuCode) {
        this.iMenuCode = iMenuCode;
    }

    public double getfAveragerate() {
        return fAveragerate;
    }

    public void setfAveragerate(double fAveragerate) {
        this.fAveragerate = fAveragerate;
    }

    public double getfQuantity() {
        return fQuantity;
    }

    public void setfQuantity(double fQuantity) {
        this.fQuantity = fQuantity;
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

    public double getIGSTRate() {
        return IGSTRate;
    }

    public void setIGSTRate(double IGSTRate) {
        this.IGSTRate = IGSTRate;
    }

    public double getIGSTAmount() {
        return IGSTAmount;
    }

    public void setIGSTAmount(double IGSTAmount) {
        this.IGSTAmount = IGSTAmount;
    }

    public double getCGSTRate() {
        return CGSTRate;
    }

    public void setCGSTRate(double CGSTRate) {
        this.CGSTRate = CGSTRate;
    }

    public double getCGSTAmount() {
        return CGSTAmount;
    }

    public void setCGSTAmount(double CGSTAmount) {
        this.CGSTAmount = CGSTAmount;
    }

    public double getSGSTRate() {
        return SGSTRate;
    }

    public void setSGSTRate(double SGSTRate) {
        this.SGSTRate = SGSTRate;
    }

    public double getSGSTAmount() {
        return SGSTAmount;
    }

    public void setSGSTAmount(double SGSTAmount) {
        this.SGSTAmount = SGSTAmount;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public String getHSNCode() {
        return HSNCode;
    }

    public void setHSNCode(String HSNCode) {
        this.HSNCode = HSNCode;
    }

    public String getTaxationType() {
        return TaxationType;
    }

    public void setTaxationType(String taxationType) {
        TaxationType = taxationType;
    }

    public String getSupplyType() {
        return SupplyType;
    }

    public void setSupplyType(String supplyType) {
        SupplyType = supplyType;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
}
