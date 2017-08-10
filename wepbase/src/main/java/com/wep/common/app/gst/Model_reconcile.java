package com.wep.common.app.gst;

import android.database.Cursor;

import com.wep.common.app.Database.DatabaseHandler;


/**
 * Created by welcome on 04-11-2016.
 */

public class Model_reconcile {

    String Sno;
    String Gstin;
    String invoiceNo;
    String  invoiceDate;
    String value;
    String HSN;
    String AttractsReverseCharge ;
    String taxable_value;
    String igst_amt;
    String cgst_amt;
    String sgst_amt;
    String pos;
    String SupplyType ;
    String igst_rate;
    String cgst_rate;
    String sgst_rate;
    String ProAss;

    public Model_reconcile()
    {
        this.ProAss="";
        this.SupplyType="";
        this.Sno ="";
        this.Gstin = "";
        this.invoiceNo="";
        this.invoiceDate="";
        this.HSN="";
        this.taxable_value="";
        this.igst_amt="";
        this.cgst_amt="";
        this.sgst_amt="";
        this.pos="";
        this.AttractsReverseCharge="";
    }
    public Model_reconcile(String sno, String gstin, String invoiceNo, String invoiceDate, String value, String HSN, String taxable_value, String igst_amt, String cgst_amt, String sgst_amt, String pos) {
        Sno = sno;
        Gstin = gstin;
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.value = value;
        this.HSN = HSN;
        this.taxable_value = taxable_value;
        this.igst_amt = igst_amt;
        this.cgst_amt = cgst_amt;
        this.sgst_amt = sgst_amt;
        this.pos = pos;
    }




    public Model_reconcile(int no, Cursor cursor)
    {

        try
        {
            this.Sno = String.valueOf(no);
            String GSTIN = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_GSTIN));
            if (GSTIN == null || GSTIN.equals(""))
            {
                GSTIN = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SUPPLIERNAME));
            }
            this.Gstin = GSTIN;
            this.invoiceNo = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_InvoiceNo));
            this.invoiceDate = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_InvoiceDate));
            this.value = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_Value));

            //richa to do - include hsn later
            //this.HSN = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_HSN));
            this.taxable_value = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_TaxableValue));
            if(this.taxable_value.equalsIgnoreCase("null"))
                this.taxable_value = "0";// richa to do
            this.igst_amt = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_IGSTAmount));
            this.sgst_amt = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SGSTAmount));
            this.cgst_amt = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_CGSTAmount));
            this.pos = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_POS));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /* Model_reconcile(int no, int n, Model_reconcile copy)
    {

        try
        {
            this.Sno = String.valueOf(no);
            String GSTIN = copy.getGstin();
            if (GSTIN == null)
            {
                //GSTIN = copy.getC;
            }
            this.Gstin = GSTIN;
            this.invoiceNo = copy.getInvoiceNo();
            this.invoiceDate = copy.getInvoiceDate();
            this.value = copy.getValue();
            //richa to do - include hsn later
            //this.HSN = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_HSN));
            this.taxable_value = copy.getTaxable_value();
            this.igst_amt = copy.getIgst_amt();
            this.sgst_amt = copy.getSgst_amt();
            this.cgst_amt = copy.getCgst_amt();
            this.pos = copy.getPos();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
*/

    public String getProAss() {
        return ProAss;
    }

    public void setProAss(String proAss) {
        ProAss = proAss;
    }

    public String getAttractsReverseCharge() {
        return AttractsReverseCharge;
    }

    public void setAttractsReverseCharge(String attractsReverseCharge) {
        AttractsReverseCharge = attractsReverseCharge;
    }

    public String getIgst_rate() {
        return igst_rate;
    }

    public void setIgst_rate(String igst_rate) {
        this.igst_rate = igst_rate;
    }

    public String getCgst_rate() {
        return cgst_rate;
    }

    public void setCgst_rate(String cgst_rate) {
        this.cgst_rate = cgst_rate;
    }

    public String getSgst_rate() {
        return sgst_rate;
    }

    public void setSgst_rate(String sgst_rate) {
        this.sgst_rate = sgst_rate;
    }

    public String getSupplyType() {
        return SupplyType;
    }

    public void setSupplyType(String supplyType) {
        SupplyType = supplyType;
    }

    public String getSno() {
        return Sno;
    }

    public void setSno(String sno) {
        Sno = sno;
    }

    public String getGstin() {
        return Gstin;
    }

    public void setGstin(String gstin) {
        Gstin = gstin;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getHSN() {
        return HSN;
    }

    public void setHSN(String HSN) {
        this.HSN = HSN;
    }

    public String getTaxable_value() {
        return taxable_value;
    }

    public void setTaxable_value(String taxable_value) {
        this.taxable_value = taxable_value;
    }

    public String getIgst_amt() {
        return igst_amt;
    }

    public void setIgst_amt(String igst_amt) {
        this.igst_amt = igst_amt;
    }

    public String getCgst_amt() {
        return cgst_amt;
    }

    public void setCgst_amt(String cgst_amt) {
        this.cgst_amt = cgst_amt;
    }

    public String getSgst_amt() {
        return sgst_amt;
    }

    public void setSgst_amt(String sgst_amt) {
        this.sgst_amt = sgst_amt;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
}
