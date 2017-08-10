package com.wep.common.app.gst;

import java.util.ArrayList;

/**
 * Created by RichaA on 5/3/2017.
 */

public class GSTR2_B2B_A_Data_registered {
    private String ctin;
    private ArrayList<GSTR2_B2B_A_invoices_registered> inv;

    public GSTR2_B2B_A_Data_registered(String ctin, ArrayList<GSTR2_B2B_A_invoices_registered> inv) {
        this.ctin = ctin;
        this.inv = inv;
    }

    public String getCtin() {
        return ctin;
    }

    public void setCtin(String ctin) {
        this.ctin = ctin;
    }

    public ArrayList<GSTR2_B2B_A_invoices_registered> getInv() {
        return inv;
    }

    public void setInv(ArrayList<GSTR2_B2B_A_invoices_registered> inv) {
        this.inv = inv;
    }
}
