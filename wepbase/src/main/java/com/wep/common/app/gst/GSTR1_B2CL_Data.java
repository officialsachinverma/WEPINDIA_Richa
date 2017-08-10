package com.wep.common.app.gst;

import java.util.ArrayList;

/**
 * Created by RichaA on 01-05-2017.
 */

public class GSTR1_B2CL_Data {
    private String state_cd;
    private ArrayList<GSTR1_B2CL_invoices> inv;

    public GSTR1_B2CL_Data(String state_cd, ArrayList<GSTR1_B2CL_invoices> inv) {
        this.state_cd = state_cd;
        this.inv = inv;
    }

    public String getState_cd() {
        return state_cd;
    }

    public void setState_cd(String state_cd) {
        this.state_cd = state_cd;
    }

    public ArrayList<GSTR1_B2CL_invoices> getInv() {
        return inv;
    }

    public void setInv(ArrayList<GSTR1_B2CL_invoices> inv) {
        this.inv = inv;
    }
}
