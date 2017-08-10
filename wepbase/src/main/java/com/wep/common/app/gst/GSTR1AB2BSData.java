package com.wep.common.app.gst;

import java.util.ArrayList;

/**
 * Created by PriyabratP on 01-12-2016.
 */

public class GSTR1AB2BSData {

    private String chksum;
    private String flag;
    private String inum;
    private String idt;
    private double val;
    private String pos;
    private String rchrg;
    private String pro_ass;
    private ArrayList<GSTR1B2BAInvoiceItems> itms;

    public GSTR1AB2BSData(){}

    public GSTR1AB2BSData(String chksum, String flag, String inum, String idt, double val, String pos, String rchrg, String pro_ass, ArrayList<GSTR1B2BAInvoiceItems> itms) {
        this.chksum = chksum;
        this.flag = flag;
        this.inum = inum;
        this.idt = idt;
        this.val = val;
        this.pos = pos;
        this.rchrg = rchrg;
        this.pro_ass = pro_ass;
        this.itms = itms;
    }

    public String getChksum() {
        return chksum;
    }

    public void setChksum(String chksum) {
        this.chksum = chksum;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
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

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getRchrg() {
        return rchrg;
    }

    public void setRchrg(String rchrg) {
        this.rchrg = rchrg;
    }

    public String getPro_ass() {
        return pro_ass;
    }

    public void setPro_ass(String pro_ass) {
        this.pro_ass = pro_ass;
    }

    public ArrayList<GSTR1B2BAInvoiceItems> getItms() {
        return itms;
    }

    public void setItms(ArrayList<GSTR1B2BAInvoiceItems> itms) {
        this.itms = itms;
    }
}
