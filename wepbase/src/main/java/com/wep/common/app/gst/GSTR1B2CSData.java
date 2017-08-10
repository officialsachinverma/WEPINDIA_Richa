package com.wep.common.app.gst;

/**
 * Created by PriyabratP on 17-11-2016.
 */

public class GSTR1B2CSData {
    private String flag;
    private String chksum;
    private String state_cd;
    private String ty;
    private String hsn_sc;
    private double irt;
    private double crt;
    private double srt;
    private double csrt;
    private String pro_ass;
    private String OrderNumber;
    private String OrderDate;

    private String sply_ty;
    private double rt;
    private String etin;
    private String typ;
    private String pos;
    private double txval;
    private double iamt;
    private double camt;
    private double samt;
    private double csamt;



    public GSTR1B2CSData() {
    }

    public GSTR1B2CSData(String flag, String chksum, String state_cd, String ty, String hsn_sc, double txval, double irt, double iamt, double crt, double camt, double srt, double samt, double csrt, double csamt, String pro_ass, String etin, String etype, String orderNumber, String orderDate) {
        this.flag = flag;
        this.chksum = chksum;
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
        this.csrt = csrt;
        this.csamt = csamt;
        this.pro_ass = pro_ass;
        this.etin = etin;
        this.typ = etype;
        OrderNumber = orderNumber;
        OrderDate = orderDate;
    }

    public GSTR1B2CSData(String flag, String chksum, String state_cd, String ty, String hsn_sc, double txval, double irt, double iamt, double crt, double camt, double srt, double samt, String pro_ass) {
        this.flag = flag;
        this.chksum = chksum;
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

    public GSTR1B2CSData(String sply_ty, double rt, String typ,String etin, String pos, double txval, double iamt, double camt, double samt, double csamt) {
        this.sply_ty = sply_ty;
        this.rt = rt;
        this.typ = typ;
        this.etin = etin;
        this.pos = pos;
        this.txval = txval;
        this.iamt = iamt;
        this.camt = camt;
        this.samt = samt;
        this.csamt = csamt;
    }

    public double getRt() {
        return rt;
    }

    public void setRt(double rt) {
        this.rt = rt;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getSply_ty() {
        return sply_ty;
    }

    public void setSply_ty(String sply_ty) {
        this.sply_ty = sply_ty;
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
        return typ;
    }

    public void setEtype(String etype) {
        this.typ = etype;
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
