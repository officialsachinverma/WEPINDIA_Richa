package com.wep.common.app.gst;

import java.util.ArrayList;

/**
 * Created by RichaA on 4/28/2017.
 */

public class GSTR1_B2B_invoices {
    private String inum;
    private String idt;
    private double val;
    private String pos;
    private  String rchrg;
    private String prs;
    private String od_num;
    private String od_dt;
    private String etin;
    private String inv_typ;
    private ArrayList<GSTR1_B2B_items> itms;

    public GSTR1_B2B_invoices(String inum, String idt, double val, String pos, String rchrg, String prs, String od_num, String od_dt, String inv_typ, String etin, ArrayList<GSTR1_B2B_items> itms) {
        this.inum = inum;
        this.idt = idt;
        this.val = val;
        this.pos = pos;
        this.rchrg = rchrg;
        this.prs = prs;
        this.od_num = od_num;
        this.od_dt = od_dt;
        this.etin = etin;
        this.inv_typ = inv_typ;
        this.itms = itms;
    }
    public GSTR1_B2B_invoices(String inum, String idt, double val, String pos, String rchrg,   String etin, String inv_typ,ArrayList<GSTR1_B2B_items> itms) {
        this.inum = inum;
        this.idt = idt;
        this.val = val;
        this.pos = pos;
        this.rchrg = rchrg;
        this.etin = etin;
        this.inv_typ = inv_typ;
        this.itms = itms;
    }

    public String getInv_typ() {
        return inv_typ;
    }

    public void setInv_typ(String inv_typ) {
        this.inv_typ = inv_typ;
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

    public ArrayList<GSTR1_B2B_items> getItms() {
        return itms;
    }

    public void setItms(ArrayList<GSTR1_B2B_items> itms) {
        this.itms = itms;
    }
}
