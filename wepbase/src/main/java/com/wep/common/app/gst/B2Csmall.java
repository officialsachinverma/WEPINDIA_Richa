package com.wep.common.app.gst;

import java.util.Comparator;

/**
 * Created by welcome on 21-11-2016.
 */

public class B2Csmall  {

    private String SupplyType;
    private String HSNCode;
    private String PlaceOfSupply, Description,stateCode;
    private double TaxableValue , SubTotal ;
    private double IGSTRate, IGSTAmt, CGSTRate, CGSTAmt, SGSTRate, SGSTAmt;
    private double GSTRate,cessAmt;
    private String ProAss;
    private float NilRatedValue, ExemptedValue, NonGSTValue, CompoundingValue,unregisteredValue;
    private String cessRate ="0";
    private String Orderno="0";
    private String OrderDate="0";
    private String etin="";
    private String etype="";
    private String sply_ty=""; // Inter/Intra


    public B2Csmall() {
        this.Description="";
        this.SupplyType ="";
        this.HSNCode="";
        this.PlaceOfSupply="";
        this.TaxableValue=0;
        this.IGSTRate=0;
        this.IGSTAmt=0;
        this.CGSTRate=0;
        this.CGSTAmt=0;
        this.SGSTRate=0;
        this.SGSTAmt=0;
        this.GSTRate=0;
        this.ProAss="";
        this.NilRatedValue=0;
        this.ExemptedValue=0;
        this.NonGSTValue=0;
        this.CompoundingValue=0;
        this.unregisteredValue=0;
        this.SubTotal=0;
        cessRate ="0";
        cessAmt  =0;
        Orderno="0";
        OrderDate="0";
        etin="";
        etype="";
        sply_ty="";
        stateCode="";
    }

    public B2Csmall(String supplyType, String HSNCode, String placeOfSupply, String description, double taxableValue, float subTotal, double IGSTRate, double IGSTAmt, double CGSTRate,
                    double CGSTAmt, double SGSTRate, double SGSTAmt, String proAss, float nilRatedValue, float exemptedValue,
                    float nonGSTValue, float compoundingValue, float unregisteredValue, String cessRate, double cessAmt,
                    String orderno, String orderDate, String etin, String etype
                    ,double GSTRate) {
        SupplyType = supplyType;
        this.HSNCode = HSNCode;
        stateCode = stateCode;
        Description = description;
        TaxableValue = taxableValue;
        SubTotal = subTotal;
        this.IGSTRate = IGSTRate;
        this.IGSTAmt = IGSTAmt;
        this.CGSTRate = CGSTRate;
        this.CGSTAmt = CGSTAmt;
        this.SGSTRate = SGSTRate;
        this.SGSTAmt = SGSTAmt;
        this.GSTRate = GSTRate;
        ProAss = proAss;
        NilRatedValue = nilRatedValue;
        ExemptedValue = exemptedValue;
        NonGSTValue = nonGSTValue;
        CompoundingValue = compoundingValue;
        this.unregisteredValue = unregisteredValue;
        this.cessRate = cessRate;
        this.cessAmt = cessAmt;
        Orderno = orderno;
        OrderDate = orderDate;
        this.etin = etin;
        this.etype = etype;
        //this.stateCode = stateCode;
    }

    /*public B2Csmall(String supplyType, String HSNCode, String placeOfSupply, float TaxableValue, float IGSTRate, float IGSTAmt, float CGSTRate, float CGSTAmt, float SGSTRate, float SGSTAmt, String proAss, float nilRatedValue, float exemptedValue, float nonGSTValue, float compoundingValue, float unregisteredValue) {
            SupplyType = supplyType;
            this.HSNCode = HSNCode;
            PlaceOfSupply = placeOfSupply;
            this.TaxableValue = TaxableValue;
            this.IGSTRate = IGSTRate;
            this.IGSTAmt = IGSTAmt;
            this.CGSTRate = CGSTRate;
            this.CGSTAmt = CGSTAmt;
            this.SGSTRate = SGSTRate;
            this.SGSTAmt = SGSTAmt;
            ProAss = proAss;
            NilRatedValue = nilRatedValue;
            ExemptedValue = exemptedValue;
            NonGSTValue = nonGSTValue;
            CompoundingValue = compoundingValue;
            this.unregisteredValue = unregisteredValue;
        }
    */

    public String getSply_ty() {
        return sply_ty;
    }

    public void setSply_ty(String sply_ty) {
        this.sply_ty = sply_ty;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getCessRate() {
        return cessRate;
    }

    public void setCessRate(String cessRate) {
        this.cessRate = cessRate;
    }

    public double getCessAmt() {
        return cessAmt;
    }

    public void setCessAmt(double cessAmt) {
        this.cessAmt = cessAmt;
    }

    public double getGSTRate() {
        return GSTRate;
    }

    public void setGSTRate(double GSTRate) {
        this.GSTRate = GSTRate;
    }

    public String getOrderno() {
        return Orderno;
    }

    public void setOrderno(String orderno) {
        Orderno = orderno;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getEtin() {
        return etin;
    }

    public void setEtin(String etin) {
        this.etin = etin;
    }

    public String getEtype() {
        return etype;
    }

    public void setEtype(String etype) {
        this.etype = etype;
    }

    public double getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(double subTotal) {
        SubTotal = subTotal;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public String getPlaceOfSupply() {
        return PlaceOfSupply;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        PlaceOfSupply = placeOfSupply;
    }

    public double getTaxableValue() {
        return TaxableValue;
    }

    public void setTaxableValue(double TaxableValue) {
        this.TaxableValue = TaxableValue;
    }

    public double getIGSTRate() {
        return IGSTRate;
    }

    public void setIGSTRate(double IGSTRate) {
        this.IGSTRate = IGSTRate;
    }

    public double getIGSTAmt() {
        return IGSTAmt;
    }

    public void setIGSTAmt(double IGSTAmt) {
        this.IGSTAmt = IGSTAmt;
    }

    public double getCGSTRate() {
        return CGSTRate;
    }

    public void setCGSTRate(double CGSTRate) {
        this.CGSTRate = CGSTRate;
    }

    public double getCGSTAmt() {
        return CGSTAmt;
    }

    public void setCGSTAmt(double CGSTAmt) {
        this.CGSTAmt = CGSTAmt;
    }

    public double getSGSTRate() {
        return SGSTRate;
    }

    public void setSGSTRate(double SGSTRate) {
        this.SGSTRate = SGSTRate;
    }

    public double getSGSTAmt() {
        return SGSTAmt;
    }

    public void setSGSTAmt(double SGSTAmt) {
        this.SGSTAmt = SGSTAmt;
    }

    public String getProAss() {
        return ProAss;
    }

    public void setProAss(String proAss) {
        ProAss = proAss;
    }

    public float getNilRatedValue() {
        return NilRatedValue;
    }

    public void setNilRatedValue(float nilRatedValue) {
        NilRatedValue = nilRatedValue;
    }

    public float getExemptedValue() {
        return ExemptedValue;
    }

    public void setExemptedValue(float exemptedValue) {
        ExemptedValue = exemptedValue;
    }

    public float getNonGSTValue() {
        return NonGSTValue;
    }

    public void setNonGSTValue(float nonGSTValue) {
        NonGSTValue = nonGSTValue;
    }

    public float getCompoundingValue() {
        return CompoundingValue;
    }

    public void setCompoundingValue(float compoundingValue) {
        CompoundingValue = compoundingValue;
    }

    public float getUnregisteredValue() {
        return unregisteredValue;
    }

    public void setUnregisteredValue(float unregisteredValue) {
        this.unregisteredValue = unregisteredValue;
    }

    public static Comparator<B2Csmall>  HSNComparator = new Comparator<B2Csmall>() {
        @Override
        public int compare(B2Csmall o1, B2Csmall o2) {

            String B2Csmall1 = String.valueOf(o1.getIGSTRate());
            String B2Csmall2 = String.valueOf(o2.getIGSTRate());
            /*String B2Csmall1 = o1.getStateCode();
            String B2Csmall2 = o2.getStateCode();*/

            //ascending order
            return B2Csmall1.compareTo(B2Csmall2);
        }
    };

}
