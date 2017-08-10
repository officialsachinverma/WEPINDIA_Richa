package com.wep.common.app.gst.get;

import java.util.ArrayList;

/**
 * Created by PriyabratP on 29-11-2016.
 */

public class GetGSTR1Summary { // For Get GSTR1 Summary and GSTR1A Summary

    private String gstin;
    private String ret_pd;
    private String checksum;
    private double ttl_inv;
    private double ttl_tax;
    private double ttl_igst;
    private double ttl_sgst;
    private double ttl_cgst;
    private ArrayList<GetGSTR1SecSummary> section_summary;

    public GetGSTR1Summary(){}

    public GetGSTR1Summary(String gstin, String ret_pd, String checksum, double ttl_inv, double ttl_tax, double ttl_igst, double ttl_sgst, double ttl_cgst, ArrayList<GetGSTR1SecSummary> section_summary) {
        this.gstin = gstin;
        this.ret_pd = ret_pd;
        this.checksum = checksum;
        this.ttl_inv = ttl_inv;
        this.ttl_tax = ttl_tax;
        this.ttl_igst = ttl_igst;
        this.ttl_sgst = ttl_sgst;
        this.ttl_cgst = ttl_cgst;
        this.section_summary = section_summary;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getRet_pd() {
        return ret_pd;
    }

    public void setRet_pd(String ret_pd) {
        this.ret_pd = ret_pd;
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

    public ArrayList<GetGSTR1SecSummary> getSec_sum() {
        return section_summary;
    }

    public void setSec_sum(ArrayList<GetGSTR1SecSummary> section_summary) {
        this.section_summary = section_summary;
    }
}
