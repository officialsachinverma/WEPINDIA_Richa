package com.wep.common.app.gst.get;

import java.util.ArrayList;

/**
 * Created by PriyabratP on 28-11-2016.
 */

public class GetGSTR2B2BInvoice {

    private String inum;
    private String idt;
    private double val;
    private String pos;
    private String rchrg;
    private String pro_ass;
    private ArrayList<GetGSTR2B2BItem> items;

    public GetGSTR2B2BInvoice() {

    }

    public GetGSTR2B2BInvoice(String inum, String idt, double val, String pos, String rchrg, String pro_ass, ArrayList<GetGSTR2B2BItem> items) {
        this.inum = inum;
        this.idt = idt;
        this.val = val;
        this.pos = pos;
        this.rchrg = rchrg;
        this.pro_ass = pro_ass;
        this.items = items;
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

    public ArrayList<GetGSTR2B2BItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<GetGSTR2B2BItem> items) {
        this.items = items;
    }
}
