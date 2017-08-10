package com.wep.common.app.gst;

import java.util.ArrayList;

/**
 * Created by RichaA on 29-04-2017.
 */

public class GSTR1_B2B_A_Data {private String ctin;
    private ArrayList<GSTR1_B2B_A_invoices> inv;

    public GSTR1_B2B_A_Data(String ctin, ArrayList<GSTR1_B2B_A_invoices> inv) {
        this.ctin = ctin;
        this.inv = inv;
    }

    public GSTR1_B2B_A_Data() {
        this.ctin = "";
        this.inv = null;
    }

    public String getCtin() {
        return ctin;
    }

    public void setCtin(String ctin) {
        this.ctin = ctin;
    }

    public ArrayList<GSTR1_B2B_A_invoices> getInv() {
        return inv;
    }

    public void setInv(ArrayList<GSTR1_B2B_A_invoices> inv) {
        this.inv = inv;
    }
}

