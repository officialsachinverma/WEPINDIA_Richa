package com.wep.common.app.gst;

/**
 * Created by RichaA on 01-05-2017.
 */

public class GSTR1_B2CL_item_details {
    private String ty;
    private String hsn_sc;
    private double txval;
    private double rt;
    private double irt;
    private double iamt;
    private double csrt;
    private double csamt;

    public GSTR1_B2CL_item_details() {
        this.ty = "";
        this.hsn_sc = "";
        this.txval = 0;
        this.rt = 0;
        this.irt = 0;
        this.iamt = 0;
        this.csrt = 0;
        this.csamt = 0;
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

    public double getRt() {
        return rt;
    }

    public void setRt(double rt) {
        this.rt = rt;
    }

    public GSTR1_B2CL_item_details(String ty, String hsn_sc, double txval, double irt, double iamt, double csrt, double csamt) {
        this.ty = ty;
        this.hsn_sc = hsn_sc;
        this.txval = txval;
        this.irt = irt;
        this.iamt = iamt;
        this.csrt = csrt;
        this.csamt = csamt;
    }
    public GSTR1_B2CL_item_details(double rt,double txval,  double iamt,  double csamt) {

        this.txval = txval;
        this.rt = rt;
        this.iamt = iamt;
        this.csamt = csamt;
    }

}
