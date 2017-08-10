package com.wep.common.app.gst;

/**
 * Created by PriyabratP on 18-11-2016.
 */

public class GSTR2B2BAItemDetails {

    private String ty; //Identifier if Goods or Services
    private String hsn_sc; //HSN or SAC of Goods or Services as per Invoice line items
    private double txval; //Taxable value of Goods or Service as per invoice
    private double irt; //IGST Rate as per invoice
    private double imat; //IGST Amount as per invoice
    private double crt; //CGST Rate as per invoice
    private double camt; //CGST Amount as per invoice
    private double srt; //SGST Rate as per invoice
    private double samt;//SGST Amount as per invoice
    private String elg; //Eligiblity of Total Tax available as ITC

    private GSTR2B2BAItemDetails() {
    }

    public GSTR2B2BAItemDetails(String ty, String hsn_sc, double txval, double irt, double imat, double crt, double camt, double srt, double samt, String elg) {
        this.ty = ty;
        this.hsn_sc = hsn_sc;
        this.txval = txval;
        this.irt = irt;
        this.imat = imat;
        this.crt = crt;
        this.camt = camt;
        this.srt = srt;
        this.samt = samt;
        this.elg = elg;
    }

    public String getTy() {
        return ty;
    }

    public void setTy(String ty) {
        this.ty = ty;
    }

    public String getHsn_sc() {
        return hsn_sc;
    }

    public void setHsn_sc(String hsn_sc) {
        this.hsn_sc = hsn_sc;
    }

    public double getTxval() {
        return txval;
    }

    public void setTxval(double txval) {
        this.txval = txval;
    }

    public double getIrt() {
        return irt;
    }

    public void setIrt(double irt) {
        this.irt = irt;
    }

    public double getImat() {
        return imat;
    }

    public void setImat(double imat) {
        this.imat = imat;
    }

    public double getCrt() {
        return crt;
    }

    public void setCrt(double crt) {
        this.crt = crt;
    }

    public double getCamt() {
        return camt;
    }

    public void setCamt(double camt) {
        this.camt = camt;
    }

    public double getSrt() {
        return srt;
    }

    public void setSrt(double srt) {
        this.srt = srt;
    }

    public double getSamt() {
        return samt;
    }

    public void setSamt(double samt) {
        this.samt = samt;
    }

    public String getElg() {
        return elg;
    }

    public void setElg(String elg) {
        this.elg = elg;
    }
}
