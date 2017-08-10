package com.wep.common.app.gst;

import java.util.ArrayList;

/**
 * Created by RichaA on 5/5/2017.
 */

public class GSTR1_HSN_Data {
    private ArrayList<GSTR1_HSN_Details> data;

    public GSTR1_HSN_Data(ArrayList<GSTR1_HSN_Details> data) {
        this.data = data;
    }

    public ArrayList<GSTR1_HSN_Details> getData() {
        return data;
    }

    public void setData(ArrayList<GSTR1_HSN_Details> data) {
        this.data = data;
    }
}
