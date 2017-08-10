package com.wep.common.app.gst;

import java.util.ArrayList;

/**
 * Created by RichaA on 01-05-2017.
 */

public class GSTR1_B2CL_A_invoices {
    private String oinum;
    private String oidt;
    private String cname;
    private String inum;
    private String idt;
    private double val;
    private String pos;
    private String prs;
    private String od_num;
    private String od_dt;
    private String etin;
    private ArrayList<GSTR1_B2CL_items> itms;

    public GSTR1_B2CL_A_invoices(String oinum, String oidt, String cname, String inum, String idt, double val, String pos, String prs, String od_num, String od_dt, String etin, ArrayList<GSTR1_B2CL_items> itms) {
        this.oinum = oinum;
        this.oidt = oidt;
        this.cname = cname;
        this.inum = inum;
        this.idt = idt;
        this.val = val;
        this.pos = pos;
        this.prs = prs;
        this.od_num = od_num;
        this.od_dt = od_dt;
        this.etin = etin;
        this.itms = itms;
    }

    public ArrayList<GSTR1_B2CL_items> getItms() {
        return itms;
    }

    public void setItms(ArrayList<GSTR1_B2CL_items> itms) {
        this.itms = itms;
    }

    public String getOinum() {
        return oinum;
    }

    public void setOinum(String oinum) {
        this.oinum = oinum;
    }

    public String getOidt() {
        return oidt;
    }

    public void setOidt(String oidt) {
        this.oidt = oidt;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
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

    public String getPrs() {
        return prs;
    }

    public void setPrs(String prs) {
        this.prs = prs;
    }

    public String getOd_num() {
        return od_num;
    }

    public void setOd_num(String od_num) {
        this.od_num = od_num;
    }

    public String getOd_dt() {
        return od_dt;
    }

    public void setOd_dt(String od_dt) {
        this.od_dt = od_dt;
    }

    public String getEtin() {
        return etin;
    }

    public void setEtin(String etin) {
        this.etin = etin;
    }
}
