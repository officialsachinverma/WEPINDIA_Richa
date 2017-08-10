package com.wep.common.app.gst;

/**
 * Created by PriyabratP on 01-12-2016.
 */

public class GSTR1B2CSAData {

    private String flag;
    private String chksum;
    private String osupst_cd;
    private String omon;
    private String oty;
    private String ohsn_sc;
    private String state_cd;
    private String ty;
    private String hsn_sc;
    private double txval;
    private double irt;
    private double iamt;
    private double crt;
    private double camt;
    private double srt;
    private double samt;
    private String pro_ass;
    private double csrt;
    private double csamt;
    private String etin;
    private String etype;
    private String OrderNumber;
    private String OrderDate;

    public GSTR1B2CSAData() {
    }

    public GSTR1B2CSAData(String flag, String chksum, String osupst_cd, String omon, String oty, String ohsn_sc, String state_cd, String ty, String hsn_sc, double txval, double irt, double iamt, double crt, double camt, double srt, double samt, String pro_ass) {
        this.flag = flag;
        this.chksum = chksum;
        this.osupst_cd = osupst_cd;
        this.omon = omon;
        this.oty = oty;
        this.ohsn_sc = ohsn_sc;
        this.state_cd = state_cd;
        this.ty = ty;
        this.hsn_sc = hsn_sc;
        this.txval = txval;
        this.irt = irt;
        this.iamt = iamt;
        this.crt = crt;
        this.camt = camt;
        this.srt = srt;
        this.samt = samt;
        this.pro_ass = pro_ass;
    }

    public GSTR1B2CSAData(String flag, String chksum, String osupst_cd, String omon, String oty, String ohsn_sc, String state_cd, String ty, String hsn_sc, double txval, double irt, double iamt, double crt, double camt, double srt, double samt, String pro_ass, double csrt, double csamt, String etin, String etype, String orderNumber, String orderDate) {
        this.flag = flag;
        this.chksum = chksum;
        this.osupst_cd = osupst_cd;
        this.omon = omon;
        this.oty = oty;
        this.ohsn_sc = ohsn_sc;
        this.state_cd = state_cd;
        this.ty = ty;
        this.hsn_sc = hsn_sc;
        this.txval = txval;
        this.irt = irt;
        this.iamt = iamt;
        this.crt = crt;
        this.camt = camt;
        this.srt = srt;
        this.samt = samt;
        this.pro_ass = pro_ass;
        this.csrt = csrt;
        this.csamt = csamt;
        this.etin = etin;
        this.etype = etype;
        OrderNumber = orderNumber;
        OrderDate = orderDate;
    }

    public double getCsrt() {
        return csrt;
    }

    public void setCsrt(double csrt) {
        this.csrt = csrt;
    }

    public double getCsamt() {
        return csamt;
    }

    public void setCsamt(double csamt) {
        this.csamt = csamt;
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

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getChksum() {
        return chksum;
    }

    public void setChksum(String chksum) {
        this.chksum = chksum;
    }

    public String getOsupst_cd() {
        return osupst_cd;
    }

    public void setOsupst_cd(String osupst_cd) {
        this.osupst_cd = osupst_cd;
    }

    public String getOmon() {
        return omon;
    }

    public void setOmon(String omon) {
        this.omon = omon;
    }

    public String getOty() {
        return oty;
    }

    public void setOty(String oty) {
        this.oty = oty;
    }

    public String getOhsn_sc() {
        return ohsn_sc;
    }

    public void setOhsn_sc(String ohsn_sc) {
        this.ohsn_sc = ohsn_sc;
    }

    public String getState_cd() {
        return state_cd;
    }

    public void setState_cd(String state_cd) {
        this.state_cd = state_cd;
    }

    public String getTy() {
        return ty;
    }

    public void setTy(String ty) {
        this.ty = ty;
    }

    public String getHsn_sc() {
        return hsn_sc;
    }

    public void setHsn_sc(String hsn_sc) {
        this.hsn_sc = hsn_sc;
    }

    public double getTxval() {
        return txval;
    }

    public void setTxval(double txval) {
        this.txval = txval;
    }

    public double getIrt() {
        return irt;
    }

    public void setIrt(double irt) {
        this.irt = irt;
    }

    public double getIamt() {
        return iamt;
    }

    public void setIamt(double iamt) {
        this.iamt = iamt;
    }

    public double getCrt() {
        return crt;
    }

    public void setCrt(double crt) {
        this.crt = crt;
    }

    public double getCamt() {
        return camt;
    }

    public void setCamt(double camt) {
        this.camt = camt;
    }

    public double getSrt() {
        return srt;
    }

    public void setSrt(double srt) {
        this.srt = srt;
    }

    public double getSamt() {
        return samt;
    }

    public void setSamt(double samt) {
        this.samt = samt;
    }

    public String getPro_ass() {
        return pro_ass;
    }

    public void setPro_ass(String pro_ass) {
        this.pro_ass = pro_ass;
    }
}
