package com.wep.common.app.gst;

/**
 * Created by PriyabratP on 18-11-2016.
 */

public class GSTR2_ITC_Details {

    private double tx_i; //Total Tax available as ITC IGST Amount
    private double tx_c; //Total Tax available as ITC CGST Amount
    private double tx_s; //Total Tax available as ITC SGST Amount
    private double tx_cs; //Total Tax available as ITC SGST Amount
    private double tc_i; //Total Input Tax Credit available for claim this month based on the Invoices uploaded(IGST Amount)
    private double tc_c; //Total Input Tax Credit available for claim this month based on the Invoices uploaded(CGST Amount)
    private double tc_s; //Total Input Tax Credit available for claim this month based on the Invoices uploaded(SGST Amount)
    private double tc_cs; //Total Input Tax Credit available for claim this month based on the Invoices uploaded(SGST Amount)

    public GSTR2_ITC_Details(double tx_i, double tx_c, double tx_s, double tx_cs, double tc_i, double tc_c, double tc_s, double tc_cs) {
        this.tx_i = tx_i;
        this.tx_c = tx_c;
        this.tx_s = tx_s;
        this.tx_cs = tx_cs;
        this.tc_i = tc_i;
        this.tc_c = tc_c;
        this.tc_s = tc_s;
        this.tc_cs = tc_cs;
    }
    public GSTR2_ITC_Details() {
        this.tx_i = 0;
        this.tx_c = 0;
        this.tx_s = 0;
        this.tx_cs = 0;
        this.tc_i = 0;
        this.tc_c = 0;
        this.tc_s = 0;
        this.tc_cs = 0;
    }

    public double getTx_i() {
        return tx_i;
    }

    public void setTx_i(double tx_i) {
        this.tx_i = tx_i;
    }

    public double getTx_c() {
        return tx_c;
    }

    public void setTx_c(double tx_c) {
        this.tx_c = tx_c;
    }

    public double getTx_s() {
        return tx_s;
    }

    public void setTx_s(double tx_s) {
        this.tx_s = tx_s;
    }

    public double getTx_cs() {
        return tx_cs;
    }

    public void setTx_cs(double tx_cs) {
        this.tx_cs = tx_cs;
    }

    public double getTc_i() {
        return tc_i;
    }

    public void setTc_i(double tc_i) {
        this.tc_i = tc_i;
    }

    public double getTc_c() {
        return tc_c;
    }

    public void setTc_c(double tc_c) {
        this.tc_c = tc_c;
    }

    public double getTc_s() {
        return tc_s;
    }

    public void setTc_s(double tc_s) {
        this.tc_s = tc_s;
    }

    public double getTc_cs() {
        return tc_cs;
    }

    public void setTc_cs(double tc_cs) {
        this.tc_cs = tc_cs;
    }
}
