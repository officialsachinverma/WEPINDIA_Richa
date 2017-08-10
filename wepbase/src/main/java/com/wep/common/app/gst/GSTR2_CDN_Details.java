package com.wep.common.app.gst;

import java.util.ArrayList;

/**
 * Created by PriyabratP on 01-12-2016.
 */
public class GSTR2_CDN_Details {


    private String ntty;
    private double nt_num;
    private String nt_dt;
    private String rsn;
    private String inum;
    private String idt;
    private String rchrg;
    private double val;
    private double irt;
    private double iamt;
    private double crt;
    private double camt;
    private double srt;
    private double samt;
    private double csrt;
    private double csamt;
    private String elg;
    private GSTR2_ITC_Details itc;

    public GSTR2_CDN_Details(String ntty, double nt_num, String nt_dt, String rsn, String inum, String idt, String rchrg, double val, double irt, double iamt, double crt, double camt, double srt, double samt, double csrt, double csamt, String elg, GSTR2_ITC_Details itc) {
        this.ntty = ntty;
        this.nt_num = nt_num;
        this.nt_dt = nt_dt;
        this.rsn = rsn;
        this.inum = inum;
        this.idt = idt;
        this.rchrg = rchrg;
        this.val = val;
        this.irt = irt;
        this.iamt = iamt;
        this.crt = crt;
        this.camt = camt;
        this.srt = srt;
        this.samt = samt;
        this.csrt = csrt;
        this.csamt = csamt;
        this.elg = elg;
        this.itc = itc;
    }

    public String getNtty() {
        return ntty;
    }

    public void setNtty(String ntty) {
        this.ntty = ntty;
    }

    public double getNt_num() {
        return nt_num;
    }

    public void setNt_num(double nt_num) {
        this.nt_num = nt_num;
    }

    public String getNt_dt() {
        return nt_dt;
    }

    public void setNt_dt(String nt_dt) {
        this.nt_dt = nt_dt;
    }

    public String getRsn() {
        return rsn;
    }

    public void setRsn(String rsn) {
        this.rsn = rsn;
    }

    public String getInum() {
        return inum;
    }

    public void setInum(String inum) {
        this.inum = inum;
    }

    public String getIdt() {
        return idt;
    }

    public void setIdt(String idt) {
        this.idt = idt;
    }

    public String getRchrg() {
        return rchrg;
    }

    public void setRchrg(String rchrg) {
        this.rchrg = rchrg;
    }

    public double getVal() {
        return val;
    }

    public void setVal(double val) {
        this.val = val;
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

    public GSTR2_ITC_Details getItc() {
        return itc;
    }

    public void setItc(GSTR2_ITC_Details itc) {
        this.itc = itc;
    }
}
