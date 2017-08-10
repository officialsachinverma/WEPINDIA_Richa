package com.wep.common.app.gst;

import java.util.ArrayList;

/**
 * Created by PriyabratP on 01-12-2016.
 */
public class GSTR1_CDN_Data {

    private String ctin;
    private ArrayList<GSTR1_CDN_Details> nt;

    public GSTR1_CDN_Data(String cgstin, ArrayList<GSTR1_CDN_Details> nt) {
        this.ctin = cgstin;
        this.nt = nt;
    }

    public String getCtin() {
        return ctin;
    }

    public void setCtin(String ctin) {
        this.ctin = ctin;
    }

    public ArrayList<GSTR1_CDN_Details> getNt() {
        return nt;
    }

    public void setNt(ArrayList<GSTR1_CDN_Details> nt) {
        this.nt = nt;
    }
}
