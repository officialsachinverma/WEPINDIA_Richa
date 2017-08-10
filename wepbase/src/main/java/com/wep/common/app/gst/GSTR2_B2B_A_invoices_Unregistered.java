package com.wep.common.app.gst;

import java.util.ArrayList;

/**
 * Created by RichaA on 5/3/2017.
 */

public class GSTR2_B2B_A_invoices_Unregistered {
    private String cname;
    private String inum;
    private String idt;
    private String oinum;
    private String oidt;
    private double val;
    private String pos;
    private String rchrg;
    private ArrayList<GSTR2_B2B_items> itms;

    public GSTR2_B2B_A_invoices_Unregistered(String cname, String inum, String idt, String oinum, String oidt, double val, String pos, String rchrg, ArrayList<GSTR2_B2B_items> itms) {
        this.cname = cname;
        this.inum = inum;
        this.idt = idt;
        this.oinum = oinum;
        this.oidt = oidt;
        this.val = val;
        this.pos = pos;
        this.rchrg = rchrg;
        this.itms = itms;
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
