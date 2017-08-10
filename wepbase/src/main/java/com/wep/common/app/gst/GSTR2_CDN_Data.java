package com.wep.common.app.gst;

import java.util.ArrayList;

/**
 * Created by PriyabratP on 01-12-2016.
 */
public class GSTR2_CDN_Data {

    private String cgstin;
    private ArrayList<GSTR2_CDN_Details> cdn;

    

    public GSTR2_CDN_Data(String cgstin, ArrayList<GSTR2_CDN_Details> cdn) {
        this.cgstin = cgstin;
        this.cdn = cdn;
    }

    public String getCgstin() {
        return cgstin;
    }

    public void setCgstin(String cgstin) {
        this.cgstin = cgstin;
    }
    

    public ArrayList<GSTR2_CDN_Details> getCdn() {
        return cdn;
    }

    public void setCdn(ArrayList<GSTR2_CDN_Details> cdn) {
        this.cdn = cdn;
    }
}
