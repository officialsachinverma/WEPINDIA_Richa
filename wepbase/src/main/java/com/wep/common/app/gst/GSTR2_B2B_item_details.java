package com.wep.common.app.gst;

/**
 * Created by RichaA on 5/2/2017.
 */

public class GSTR2_B2B_item_details {
    private String ty;
    private String hsn_sc;
    private double txval;
    private double irt;
    private double iamt;
    private double crt;
    private double camt;
    private double srt;
    private double samt;
    private double csrt;
    private double csamt;
    private double rt;
    private String elg;

    public GSTR2_B2B_item_details(String ty, String hsn_sc, double txval, double irt, double iamt, double crt, double camt, double srt, double samt, double csrt, double csamt, double rt, String elg) {
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
        this.elg = elg;
        this.rt = rt;
    }
    public GSTR2_B2B_item_details() {

    }

    public GSTR2_B2B_item_details(double txval, double iamt, double camt, double samt, double csamt, double rt) {
        this.txval = txval;
        this.iamt = iamt;
        this.camt = camt;
        this.samt = samt;
        this.csamt = csamt;
        this.rt = rt;
    }

    public double getRt() {
        return rt;
    }

    public void setRt(double rt) {
        this.rt = rt;
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

    public String getElg() {
        return elg;
    }

    public void setElg(String elg) {
        this.elg = elg;
    }
}
