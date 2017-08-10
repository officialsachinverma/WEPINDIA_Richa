package com.wep.common.app.gst;

import java.util.ArrayList;

/**
 * Created by RichaA on 4/28/2017.
 */

public class GSTR1_B2B_Data {

   private String ctin;
   private ArrayList<GSTR1_B2B_invoices> inv;

    public GSTR1_B2B_Data(String ctin, ArrayList<GSTR1_B2B_invoices> inv) {
        this.ctin = ctin;
        this.inv = inv;
    }

    public GSTR1_B2B_Data() {
        this.ctin = "";
        this.inv = null;
    }

    public String getCtin() {
        return ctin;
    }

    public void setCtin(String ctin) {
        this.ctin = ctin;
    }

    public ArrayList<GSTR1_B2B_invoices> getInv() {
        return inv;
    }

    public void setInv(ArrayList<GSTR1_B2B_invoices> inv) {
        this.inv = inv;
    }
}
