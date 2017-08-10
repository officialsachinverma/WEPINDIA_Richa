package com.wep.common.app.gst.get;

/**
 * Created by PriyabratP on 28-11-2016.
 */

public class GetGSTR2B2BItem {

    private int num;
    private String ty;
    private String hsn_sc;
    private double txval;
    private double irt;
    private double iamt;
    private double crt;
    private double camt;
    private double srt;
    private double samt;

    public GetGSTR2B2BItem(){}

    public GetGSTR2B2BItem(int num, String ty, String hsn_sc, double txval, double irt, double iamt, double crt, double camt, double srt, double samt) {
        this.num = num;
        this.ty = ty;
        this.hsn_sc = hsn_sc;
        this.txval = txval;
        this.irt = irt;
        this.iamt = iamt;
        this.crt = crt;
        this.camt = camt;
        this.srt = srt;
        this.samt = samt;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
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
}
