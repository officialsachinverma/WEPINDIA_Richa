package com.wep.common.app.gst.get;

import java.util.ArrayList;

/**
 * Created by PriyabratP on 29-11-2016.
 */

public class GetGSTR1SecSummary {

    private String Section_name;
    private String checksum;
    private double ttl_inv;
    private double ttl_tax;
    private double ttl_igst;
    private double ttl_sgst;
    private double ttl_cgst;
    private ArrayList<GetGSTR1CounterPartySummary> counter_party_summary;

    public GetGSTR1SecSummary() {
    }


    public GetGSTR1SecSummary(String Section_name, String checksum, double ttl_inv, double ttl_tax, double ttl_igst, double ttl_sgst, double ttl_cgst, ArrayList<GetGSTR1CounterPartySummary> counter_party_summary) {
        this.Section_name = Section_name;
        this.checksum = checksum;
        this.ttl_inv = ttl_inv;
        this.ttl_tax = ttl_tax;
        this.ttl_igst = ttl_igst;
        this.ttl_sgst = ttl_sgst;
        this.ttl_cgst = ttl_cgst;
        this.counter_party_summary = counter_party_summary;
    }

    public String getSec_nm() {
        return Section_name;
    }

    public void setSec_nm(String Section_name) {
        this.Section_name = Section_name;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public double getTtl_inv() {
        return ttl_inv;
    }

    public void setTtl_inv(double ttl_inv) {
        this.ttl_inv = ttl_inv;
    }

    public double getTtl_tax() {
        return ttl_tax;
    }

    public void setTtl_tax(double ttl_tax) {
        this.ttl_tax = ttl_tax;
    }

    public double getTtl_igst() {
        return ttl_igst;
    }

    public void setTtl_igst(double ttl_igst) {
        this.ttl_igst = ttl_igst;
    }

    public double getTtl_sgst() {
        return ttl_sgst;
    }

    public void setTtl_sgst(double ttl_sgst) {
        this.ttl_sgst = ttl_sgst;
    }

    public double getTtl_cgst() {
        return ttl_cgst;
    }

    public void setTtl_cgst(double ttl_cgst) {
        this.ttl_cgst = ttl_cgst;
    }

    public ArrayList<GetGSTR1CounterPartySummary> getCpty_sum() {
        return counter_party_summary;
    }

    public void setCpty_sum(ArrayList<GetGSTR1CounterPartySummary> counter_party_summary) {
        this.counter_party_summary = counter_party_summary;
    }
}
