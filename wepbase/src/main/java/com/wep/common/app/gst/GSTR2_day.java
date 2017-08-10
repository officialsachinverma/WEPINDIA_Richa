package com.wep.common.app.gst;

/**
 * Created by welcome on 30-11-2016.
 */

public class GSTR2_day {

    String gstin;
    String date;
    float salevalue;


    public GSTR2_day(String gstin, String date, float salevalue) {
        this.gstin = gstin;
        this.date = date;
        this.salevalue = salevalue;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getSalevalue() {
        return salevalue;
    }

    public void setSalevalue(float salevalue) {
        this.salevalue = salevalue;
    }
}
