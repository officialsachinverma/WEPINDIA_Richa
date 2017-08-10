package com.wep.common.app.gst.get;

import java.util.ArrayList;

/**
 * Created by PriyabratP on 28-11-2016.
 */

public class GetGSTR2B2BFinal {

    private String ctin;
    private ArrayList<GetGSTR2B2BInvoice> invoicesList;

    public GetGSTR2B2BFinal() {
    }


    public GetGSTR2B2BFinal(String ctin, ArrayList<GetGSTR2B2BInvoice> invoicesList) {
        this.ctin = ctin;
        this.invoicesList = invoicesList;
    }

    public String getCtin() {
        return ctin;
    }

    public void setCtin(String ctin) {
        this.ctin = ctin;
    }

    public ArrayList<GetGSTR2B2BInvoice> getInvoicesList() {
        return invoicesList;
    }

    public void setInvoicesList(ArrayList<GetGSTR2B2BInvoice> invoicesList) {
        this.invoicesList = invoicesList;
    }
}
