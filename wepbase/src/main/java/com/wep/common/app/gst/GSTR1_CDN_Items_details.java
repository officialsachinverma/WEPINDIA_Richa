package com.wep.common.app.gst;

/**
 * Created by RichaA on 6/27/2017.
 */

public class GSTR1_CDN_Items_details {

    private double rt;
    private double txval;
    private double iamt;
    private double camt;
    private double samt;
    private double csamt;

    public GSTR1_CDN_Items_details(double rt, double txval, double iamt, double camt, double samt, double csamt) {
        this.rt = rt;
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

    public double getTxval() {
        return txval;
    }

    public void setTxval(double txval) {
        this.txval = txval;
    }

    public double getIamt() {
        return iamt;
    }

    public void setIamt(double iamt) {
        this.iamt = iamt;
    }

    public double getCamt() {
        return camt;
    }

    public void setCamt(double camt) {
        this.camt = camt;
    }

    public double getSamt() {
        return samt;
    }

    public void setSamt(double samt) {
        this.samt = samt;
    }

    public double getCsamt() {
        return csamt;
    }

    public void setCsamt(double csamt) {
        this.csamt = csamt;
    }
}
