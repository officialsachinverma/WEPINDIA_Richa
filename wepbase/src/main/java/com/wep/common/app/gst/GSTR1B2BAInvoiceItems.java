package com.wep.common.app.gst;

import java.util.ArrayList;

/**
 * Created by PriyabratP on 18-11-2016.
 */

public class GSTR1B2BAInvoiceItems {

    private int num; //Serial no
    private String status;//Status of invoice
    private ArrayList<GSTR2B2BAItemDetails> itm_det;//Item Details

    public GSTR1B2BAInvoiceItems() {
    }

    public GSTR1B2BAInvoiceItems(int num, String status, ArrayList<GSTR2B2BAItemDetails> itm_det) {
        this.num = num;
        this.status = status;
        this.itm_det = itm_det;
    }

    public int getLineno() {
        return num;
    }

    public void setLineno(int num) {
        this.num = num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public ArrayList<GSTR2B2BAItemDetails> getItemDetails() {
        return itm_det;
    }

    public void setItemDetails(ArrayList<GSTR2B2BAItemDetails> itemDetails) {
        this.itm_det = itemDetails;
    }
}
