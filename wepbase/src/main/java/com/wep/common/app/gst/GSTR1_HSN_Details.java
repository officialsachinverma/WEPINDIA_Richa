package com.wep.common.app.gst;

import android.support.annotation.NonNull;

/**
 * Created by RichaA on 5/5/2017.
 */

public class GSTR1_HSN_Details implements  Comparable<GSTR1_HSN_Details>{
    private int num;
    private String ty;
    private  String hsn_sc;
    private double txval;
    private double val;
    private double irt;
    private double iamt;
    private double crt;
    private double camt;
    private double srt;
    private double samt;
    private double csrt;
    private double csamt;
    private String desc;
    private String uqc;
    private double qty;
    private String sply_ty;

    public GSTR1_HSN_Details(int num, String ty, String hsn_sc, double txval, double irt, double iamt, double crt, double camt, double srt, double samt, double csrt, double csamt, String desc, String uqc, double qty, String sply_ty) {
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
        this.csrt = csrt;
        this.csamt = csamt;
        this.desc = desc;
        this.uqc = uqc;
        this.qty = qty;
        this.sply_ty = sply_ty;
    }
    public GSTR1_HSN_Details(int num, String hsn_sc, String desc, String uqc, double qty,double val,
                             double txval, double iamt,double camt, double samt, double csamt) {
        this.num = num;
        this.hsn_sc = hsn_sc;
        this.desc = desc;
        this.uqc = uqc;
        this.qty = qty;
        this.val = val;
        this.txval = txval;
        this.iamt = iamt;
        this.camt = camt;
        this.samt = samt;
        this.csamt = csamt;
    }
    public GSTR1_HSN_Details(int num, String ty, String hsn_sc, double val, double txval,double irt, double iamt, double crt, double camt, double srt, double samt, double csrt, double csamt, String desc, String uqc, double qty, String sply_ty) {
        this.num = num;
        this.ty = ty;
        this.hsn_sc = hsn_sc;
        this.val = val;
        this.txval = txval;
        this.irt = irt;
        this.iamt = iamt;
        this.crt = crt;
        this.camt = camt;
        this.srt = srt;
        this.samt = samt;
        this.csrt = csrt;
        this.csamt = csamt;
        this.desc = desc;
        this.uqc = uqc;
        this.qty = qty;
        this.sply_ty = sply_ty;
    }

    public double getVal() {
        return val;
    }

    public void setVal(double val) {
        this.val = val;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUqc() {
        return uqc;
    }

    public void setUqc(String uqc) {
        this.uqc = uqc;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public String getSply_ty() {
        return sply_ty;
    }

    public void setSply_ty(String sply_ty) {
        this.sply_ty = sply_ty;
    }

    @Override
    public int compareTo(@NonNull GSTR1_HSN_Details hsndata) {
        return  hsn_sc.compareTo(hsndata.hsn_sc);
    }
}
