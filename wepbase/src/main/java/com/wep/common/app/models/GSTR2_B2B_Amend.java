package com.wep.common.app.models;

/**
 * Created by RichaA on 4/10/2017.
 */

public class GSTR2_B2B_Amend {
    private int sno;
    private String gstin_ori;
    private String invoiceNo_ori;
    private String invoiceDate_ori;
    private String gstin_rev;
    private String invoiceNo_rev;
    private String invoiceDate_rev;
    private double value;
    private String type;
    private String HSn ;
    private double taxableValue;
    private float igstrate, cgstrate, sgstrate;
    private float igstamt, cgstamt, sgstamt;
    private double csamt;
    private String POS;
    private String CustStateCode;
    private String RecipientName;
    private String RecipientStateCode;
    private String supplierType;

    private String taxMonth, type_ori, type_rev,hsn_ori, hsn_rev,pos_ori,pos_rev;

    public GSTR2_B2B_Amend()
    {
        this.sno=0;
        this.RecipientName="";
        this.RecipientStateCode="";
        this.gstin_ori="";
        this.invoiceNo_ori ="";
        this.invoiceDate_ori = "";
        this.gstin_rev="";
        this.invoiceNo_rev ="";
        this.invoiceDate_rev = "";
        this.supplierType = "";
        this.value= 0;
        this.type="";
        this.HSn= "";
        this.taxableValue=0;
        this.igstrate=0;
        this.sgstrate=0;
        this.cgstrate=0;
        this.igstamt=0;
        this.cgstamt=0;
        this.sgstamt=0;
        this.POS ="";
        this.taxMonth="";
        this.type_ori= "";
        this.type_rev = "";
        this.hsn_ori="";
        this.hsn_rev="";
        this.pos_ori= "";
        this.pos_rev="";
        this.CustStateCode="";
        this.csamt =0;
    }

    public double getCsamt() {
        return csamt;
    }

    public void setCsamt(double csamt) {
        this.csamt = csamt;
    }

    public String getCustStateCode() {
        return CustStateCode;
    }

    public void setCustStateCode(String custStateCode) {
        CustStateCode = custStateCode;
    }

    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    public String getTaxMonth() {
        return taxMonth;
    }

    public void setTaxMonth(String taxMonth) {
        this.taxMonth = taxMonth;
    }

    public String getType_ori() {
        return type_ori;
    }

    public void setType_ori(String type_ori) {
        this.type_ori = type_ori;
    }

    public String getType_rev() {
        return type_rev;
    }

    public void setType_rev(String type_rev) {
        this.type_rev = type_rev;
    }

    public String getHsn_ori() {
        return hsn_ori;
    }

    public void setHsn_ori(String hsn_ori) {
        this.hsn_ori = hsn_ori;
    }

    public String getHsn_rev() {
        return hsn_rev;
    }

    public void setHsn_rev(String hsn_rev) {
        this.hsn_rev = hsn_rev;
    }

    public String getPos_ori() {
        return pos_ori;
    }

    public void setPos_ori(String pos_ori) {
        this.pos_ori = pos_ori;
    }

    public String getPos_rev() {
        return pos_rev;
    }

    public void setPos_rev(String pos_rev) {
        this.pos_rev = pos_rev;
    }

    public String getRecipientName() {
        return RecipientName;
    }

    public void setRecipientName(String recipientName) {
        RecipientName = recipientName;
    }

    public String getRecipientStateCode() {
        return RecipientStateCode;
    }

    public void setRecipientStateCode(String recipientStateCode) {
        RecipientStateCode = recipientStateCode;
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getGstin_ori() {
        return gstin_ori;
    }

    public void setGstin_ori(String gstin_ori) {
        this.gstin_ori = gstin_ori;
    }

    public String getInvoiceNo_ori() {
        return invoiceNo_ori;
    }

    public void setInvoiceNo_ori(String invoiceNo_ori) {
        this.invoiceNo_ori = invoiceNo_ori;
    }

    public String getInvoiceDate_ori() {
        return invoiceDate_ori;
    }

    public void setInvoiceDate_ori(String invoiceDate_ori) {
        this.invoiceDate_ori = invoiceDate_ori;
    }

    public String getGstin_rev() {
        return gstin_rev;
    }

    public void setGstin_rev(String gstin_rev) {
        this.gstin_rev = gstin_rev;
    }

    public String getInvoiceNo_rev() {
        return invoiceNo_rev;
    }

    public void setInvoiceNo_rev(String invoiceNo_rev) {
        this.invoiceNo_rev = invoiceNo_rev;
    }

    public String getInvoiceDate_rev() {
        return invoiceDate_rev;
    }

    public void setInvoiceDate_rev(String invoiceDate_rev) {
        this.invoiceDate_rev = invoiceDate_rev;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHSn() {
        return HSn;
    }

    public void setHSn(String HSn) {
        this.HSn = HSn;
    }

    public double getTaxableValue() {
        return taxableValue;
    }

    public void setTaxableValue(double taxableValue) {
        this.taxableValue = taxableValue;
    }

    public float getIgstrate() {
        return igstrate;
    }

    public void setIgstrate(float igstrate) {
        this.igstrate = igstrate;
    }

    public float getCgstrate() {
        return cgstrate;
    }

    public void setCgstrate(float cgstrate) {
        this.cgstrate = cgstrate;
    }

    public float getSgstrate() {
        return sgstrate;
    }

    public void setSgstrate(float sgstrate) {
        this.sgstrate = sgstrate;
    }

    public float getIgstamt() {
        return igstamt;
    }

    public void setIgstamt(float igstamt) {
        this.igstamt = igstamt;
    }

    public float getCgstamt() {
        return cgstamt;
    }

    public void setCgstamt(float cgstamt) {
        this.cgstamt = cgstamt;
    }

    public float getSgstamt() {
        return sgstamt;
    }

    public void setSgstamt(float sgstamt) {
        this.sgstamt = sgstamt;
    }

    public String getPOS() {
        return POS;
    }

    public void setPOS(String POS) {
        this.POS = POS;
    }
}
