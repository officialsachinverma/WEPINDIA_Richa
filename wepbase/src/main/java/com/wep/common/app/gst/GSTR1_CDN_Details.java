package com.wep.common.app.gst;

import java.util.ArrayList;

/**
 * Created by PriyabratP on 01-12-2016.
 */
public class GSTR1_CDN_Details {
    private int sno;
    private String ntty;
    private int nt_num;
    private String nt_dt;
    private String rsn;
    private String inum;// invoice no
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
    private double cessamt;
    private String etin;
    private String p_gst;
    ArrayList<GSTR1_CDN_Items> itms;

    public GSTR1_CDN_Details() {
        this.ntty = "";
        this.nt_num = 0;
        this.nt_dt = "";
        this.rsn = "";
        this.inum = "";
        this.idt = "";
        this.rchrg = "";
        this.val = 0.00;
        this.irt = 0.00;
        this.iamt = 0.00;
        this.crt = 0.00;
        this.camt = 0.00;
        this.srt =0.00;
        this.samt = 0.00;
        this.csrt =0.00;
        this.csamt = 0.00;
        this.cessamt = 0.00;
        this.etin="";
        this.p_gst="N";
        this.sno = 0;

    }

    public GSTR1_CDN_Details(String ntty, int nt_num, String nt_dt, String rsn, String inum, String idt, double val, double irt, double iamt, double crt, double camt, double srt, double samt) {
        this.ntty = ntty;
        this.nt_num = nt_num;
        this.nt_dt = nt_dt;
        this.rsn = rsn;
        this.inum = inum;
        this.idt = idt;
        this.rchrg = "";
        this.val = val;
        this.irt = irt;
        this.iamt = iamt;
        this.crt = crt;
        this.camt = camt;
        this.srt = srt;
        this.samt = samt;
        this.csrt =0.00;
        this.csamt = 0.00;
        this.etin="";
        this.sno = 0;
    }

    public GSTR1_CDN_Details(String ntty, int nt_num, String nt_dt, String rsn, String inum, String idt,
                             String rchrg, double val, double irt, double iamt, double crt, double camt, double srt,
                             double samt, double csrt, double csamt, String etin) {
        this.sno = 0;
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
        this.csamt = cessamt;

        this.etin = etin;
    }

    public GSTR1_CDN_Details(String ntty, int nt_num, String nt_dt, String p_gst,String rsn, String inum,
                             String idt,double val, ArrayList<GSTR1_CDN_Items> items
                             ) {

        this.ntty = ntty;
        this.nt_num = nt_num;
        this.nt_dt = nt_dt;
        this.p_gst = p_gst;
        this.rsn = rsn;
        this.inum = inum;
        this.idt = idt;
        this.val = val;
        this.itms = items;

    }

    public double getCessamt() {
        return cessamt;
    }

    public void setCessamt(double cessamt) {
        this.cessamt = cessamt;
    }

    public String getP_gst() {
        return p_gst;
    }

    public void setP_gst(String p_gst) {
        this.p_gst = p_gst;
    }

    public ArrayList<GSTR1_CDN_Items> getItms() {
        return itms;
    }

    public void setItms(ArrayList<GSTR1_CDN_Items> itms) {
        this.itms = itms;
    }

    public String getRchrg() {
        return rchrg;
    }

    public void setRchrg(String rchrg) {
        this.rchrg = rchrg;
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

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getNtty() {
        return ntty;
    }

    public void setNtty(String ntty) {
        this.ntty = ntty;
    }

    public int getNt_num() {
        return nt_num;
    }

    public void setNt_num(int nt_num) {
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
}
