package com.wep.common.app.gst;

/**
 * Created by RichaA on 6/27/2017.
 */

public class GSTR1_CDN_Items {
    private  int num;
    private  GSTR1_CDN_Items_details itm_det;

    public GSTR1_CDN_Items(int num, GSTR1_CDN_Items_details itms) {
        this.num = num;
        this.itm_det = itms;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public GSTR1_CDN_Items_details getItm_det() {
        return itm_det;
    }

    public void setItm_det(GSTR1_CDN_Items_details itm_det) {
        this.itm_det = itm_det;
    }
}
