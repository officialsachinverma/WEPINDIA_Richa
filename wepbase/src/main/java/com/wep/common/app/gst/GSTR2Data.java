package com.wep.common.app.gst;

import java.util.ArrayList;

/**
 * Created by PriyabratP on 18-11-2016.
 */

public class GSTR2Data {

    private String gstin; //GSTIN of the Tax Payer
    private String fp; //Financial period
    private String crclm_17_7; //Claiming under section 17(3)
    //private double ttl; // Total Tax Liability
    ArrayList<GSTR2_B2B_Data_registered> b2b;
    ArrayList<GSTR2_B2B_Data_Unregistered> b2bur;
    ArrayList<GSTR2_B2B_A_Data_registered> b2ba;
    ArrayList<GSTR2_B2B_A_Data_Unregistered> b2bura;
    ArrayList<GSTR2_CDN_Data> cdn;

    public GSTR2Data(String gstin, String fp, String crclm_17_7, ArrayList<GSTR2_B2B_Data_registered> b2b, ArrayList<GSTR2_B2B_Data_Unregistered> b2bur, ArrayList<GSTR2_B2B_A_Data_registered> b2ba, ArrayList<GSTR2_B2B_A_Data_Unregistered> b2bura, ArrayList<GSTR2_CDN_Data> cdn) {
        this.gstin = gstin;
        this.fp = fp;
        this.crclm_17_7 = crclm_17_7;
        this.b2b = b2b;
        this.b2bur = b2bur;
        this.b2ba = b2ba;
        this.b2bura = b2bura;
        this.cdn = cdn;
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

    public String getCrclm_17_7() {
        return crclm_17_7;
    }

    public void setCrclm_17_7(String crclm_17_7) {
        this.crclm_17_7 = crclm_17_7;
    }

    public ArrayList<GSTR2_B2B_Data_registered> getB2b() {
        return b2b;
    }

    public void setB2b(ArrayList<GSTR2_B2B_Data_registered> b2b) {
        this.b2b = b2b;
    }

    public ArrayList<GSTR2_B2B_Data_Unregistered> getB2bur() {
        return b2bur;
    }

    public void setB2bur(ArrayList<GSTR2_B2B_Data_Unregistered> b2bur) {
        this.b2bur = b2bur;
    }

    public ArrayList<GSTR2_B2B_A_Data_registered> getB2ba() {
        return b2ba;
    }

    public void setB2ba(ArrayList<GSTR2_B2B_A_Data_registered> b2ba) {
        this.b2ba = b2ba;
    }

    public ArrayList<GSTR2_B2B_A_Data_Unregistered> getB2bura() {
        return b2bura;
    }

    public void setB2bura(ArrayList<GSTR2_B2B_A_Data_Unregistered> b2bura) {
        this.b2bura = b2bura;
    }

    public ArrayList<GSTR2_CDN_Data> getCdn() {
        return cdn;
    }

    public void setCdn(ArrayList<GSTR2_CDN_Data> cdn) {
        this.cdn = cdn;
    }
}