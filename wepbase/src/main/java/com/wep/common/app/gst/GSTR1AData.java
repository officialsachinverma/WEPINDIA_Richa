package com.wep.common.app.gst;

import java.util.ArrayList;

/**
 * Created by PriyabratP on 21-11-2016.
 */

public class GSTR1AData {

    private String gstin;
    private String fp;
    private double gt;
    private ArrayList<GSTR1AB2BSData> b2bList;

    public GSTR1AData() {
    }

    public GSTR1AData(String gstin, String fp, double gt, ArrayList<GSTR1AB2BSData> b2bList) {
        this.gstin = gstin;
        this.fp = fp;
        this.gt = gt;
        this.b2bList = b2bList;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getFp() {
        return fp;
    }

    public void setFp(String fp) {
        this.fp = fp;
    }

    public double getGt() {
        return gt;
    }

    public void setGt(double gt) {
        this.gt = gt;
    }

    public ArrayList<GSTR1AB2BSData> getB2bList() {
        return b2bList;
    }

    public void setB2bList(ArrayList<GSTR1AB2BSData> b2bList) {
        this.b2bList = b2bList;
    }
}
