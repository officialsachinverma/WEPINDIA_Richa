package com.wep.common.app.gst.get;

/**
 * Created by PriyabratP on 29-11-2016.
 */

public class GetGSTR1CounterPartySummary {

    private String ctin;
    private String checksum;
    private double ttl_inv;
    private double ttl_tax;
    private double ttl_igst;
    private double ttl_sgst;
    private double ttl_cgst;

    public GetGSTR1CounterPartySummary(){}

    public GetGSTR1CounterPartySummary(String ctin, String checksum, double ttl_inv, double ttl_tax, double ttl_igst, double ttl_sgst, double ttl_cgst) {
        this.ctin = ctin;
        this.checksum = checksum;
        this.ttl_inv = ttl_inv;
        this.ttl_tax = ttl_tax;
        this.ttl_igst = ttl_igst;
        this.ttl_sgst = ttl_sgst;
        this.ttl_cgst = ttl_cgst;
    }

    public String getCtin() {
        return ctin;
    }

    public void setCtin(String ctin) {
        this.ctin = ctin;
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
}
