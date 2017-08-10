package com.wep.common.app.gst;

/**
 * Created by RichaA on 01-05-2017.
 */

public class GSTR1_B2CL_items {
    private  int num;
    private GSTR1_B2CL_item_details itm_det;

    public GSTR1_B2CL_items() {
        this.num = 0;
        this.itm_det = null;
    }
    public GSTR1_B2CL_items(int num, GSTR1_B2CL_item_details itm_det) {
        this.num = num;
        this.itm_det = itm_det;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public GSTR1_B2CL_item_details getItm_det() {
        return itm_det;
    }

    public void setItm_det(GSTR1_B2CL_item_details itm_det) {
        this.itm_det = itm_det;
    }
}
