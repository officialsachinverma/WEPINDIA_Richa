package com.wep.common.app.gst;

import java.util.ArrayList;

/**
 * Created by RichaA on 5/2/2017.
 */

public class GSTR2_B2B_invoices_registered {
    private String inum;
    private String idt;
    private double val;
    private String pos;
    private String rchrg;
    private String inv_typ;
    private ArrayList<GSTR2_B2B_items> itms;

    public GSTR2_B2B_invoices_registered(String inum, String idt, double val, String pos, String rchrg, String inv_typ,ArrayList<GSTR2_B2B_items> itms) {
        this.inum = inum;
        this.idt = idt;
        this.val = val;
        this.pos = pos;
        this.inv_typ = inv_typ;
        this.rchrg = rchrg;
        this.itms = itms;
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

    public ArrayList<GSTR2_B2B_items> getItms() {
        return itms;
    }

    public void setItms(ArrayList<GSTR2_B2B_items> itms) {
        this.itms = itms;
    }
}