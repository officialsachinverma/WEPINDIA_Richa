package com.wep.common.app.gst;

import java.util.ArrayList;

/**
 * Created by PriyabratP on 21-11-2016.
 */

public class GSTR1Data {

    private String gstin;
    private String fp;
    private double gt;
    private ArrayList<GSTR1_B2B_Data> b2b;
    private ArrayList<GSTR1_B2B_A_Data> b2ba;
    private ArrayList<GSTR1_B2CL_Data> b2cl;
    private ArrayList<GSTR1_B2CL_A_Data> b2cla;
    private ArrayList<GSTR1B2CSData> b2cs;
    private ArrayList<GSTR1B2CSAData> b2csa;
    private ArrayList<GSTR1_CDN_Data> cdnr;
    private ArrayList<GSTR1_HSN_Data> hsn;
    private  ArrayList<GSTR1_DOCS_Data> doc_issue;

    public GSTR1Data() {
    }
    public GSTR1Data(String gstin, String fp, double gt, ArrayList<GSTR1B2CSData> b2cs, ArrayList<GSTR1B2CSAData> b2csa, ArrayList<GSTR1_CDN_Data> cdn) {
        this.gstin = gstin;
        this.fp = fp;
        this.gt = gt;
        this.b2cs = b2cs;
        this.b2csa = b2csa;
        this.cdnr = cdn;
    }

    public GSTR1Data(String gstin,ArrayList<GSTR1_B2B_Data> b2b, ArrayList<GSTR1_B2B_A_Data> b2ba, ArrayList<GSTR1_B2CL_Data> b2cl, ArrayList<GSTR1_B2CL_A_Data> b2cla, ArrayList<GSTR1B2CSData> b2cs, ArrayList<GSTR1B2CSAData> b2csa, ArrayList<GSTR1_CDN_Data> cdnr, ArrayList<GSTR1_HSN_Data> hsn, ArrayList<GSTR1_DOCS_Data> doc_issue) {
        this.gstin = gstin;
        this.b2b = b2b;
        this.b2ba = b2ba;
        this.b2cl = b2cl;
        this.b2cla = b2cla;
        this.b2cs = b2cs;
        this.b2csa = b2csa;
        this.cdnr = cdnr;
        this.hsn = hsn;
        this.doc_issue = doc_issue;
    }

    public GSTR1Data(String gstin, String fp, double gt, ArrayList<GSTR1_B2B_Data> b2b, ArrayList<GSTR1_B2B_A_Data> b2ba, ArrayList<GSTR1_B2CL_Data> b2cl, ArrayList<GSTR1_B2CL_A_Data> b2cla, ArrayList<GSTR1B2CSData> b2cs, ArrayList<GSTR1B2CSAData> b2csa, ArrayList<GSTR1_CDN_Data> cdn, ArrayList<GSTR1_HSN_Data> hsn) {
        this.gstin = gstin;
        this.fp = fp;
        this.gt = gt;
        this.b2b = b2b;
        this.b2ba = b2ba;
        this.b2cl = b2cl;
        this.b2cla = b2cla;
        this.b2cs = b2cs;
        this.b2csa = b2csa;
        this.cdnr = cdn;
        this.hsn = hsn;
    }

    public ArrayList<GSTR1_B2CL_Data> getB2cl() {
        return b2cl;
    }

    public void setB2cl(ArrayList<GSTR1_B2CL_Data> b2cl) {
        this.b2cl = b2cl;
    }

    public ArrayList<GSTR1_B2CL_A_Data> getB2cla() {
        return b2cla;
    }

    public void setB2cla(ArrayList<GSTR1_B2CL_A_Data> b2cla) {
        this.b2cla = b2cla;
    }

    public ArrayList<GSTR1_HSN_Data> getHsn() {
        return hsn;
    }

    public void setHsn(ArrayList<GSTR1_HSN_Data> hsn) {
        this.hsn = hsn;
    }

    public ArrayList<GSTR1_B2B_A_Data> getB2ba() {
        return b2ba;
    }

    public void setB2ba(ArrayList<GSTR1_B2B_A_Data> b2ba) {
        this.b2ba = b2ba;
    }

    public ArrayList<GSTR1_B2B_Data> getB2b() {
        return b2b;
    }

    public void setB2b(ArrayList<GSTR1_B2B_Data> b2b) {
        this.b2b = b2b;
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

    public ArrayList<GSTR1B2CSData> getB2cs() {
        return b2cs;
    }

    public void setB2cs(ArrayList<GSTR1B2CSData> b2cs) {
        this.b2cs = b2cs;
    }

    public ArrayList<GSTR1B2CSAData> getB2csa() {
        return b2csa;
    }

    public void setB2csa(ArrayList<GSTR1B2CSAData> b2csa) {
        this.b2csa = b2csa;
    }

    public ArrayList<GSTR1_CDN_Data> getCdnr() {
        return cdnr;
    }

    public void setCdnr(ArrayList<GSTR1_CDN_Data> cdnr) {
        this.cdnr = cdnr;
    }
}
