package com.wep.common.app.gst;

/**
 * Created by RichaA on 5/2/2017.
 */

public class GSTR2_B2B_items {
    private  int num;
    private GSTR2_B2B_item_details itm_det;
    private GSTR2_B2B_ITC_details itc;

    public GSTR2_B2B_items(int num, GSTR2_B2B_item_details itm_det, GSTR2_B2B_ITC_details itc) {
        this.num = num;
        this.itm_det = itm_det;
        this.itc = itc;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public GSTR2_B2B_item_details getItm_det() {
        return itm_det;
    }

    public void setItm_det(GSTR2_B2B_item_details itm_det) {
        this.itm_det = itm_det;
    }

    public GSTR2_B2B_ITC_details getItc() {
        return itc;
    }

    public void setItc(GSTR2_B2B_ITC_details itc) {
        this.itc = itc;
    }
}
