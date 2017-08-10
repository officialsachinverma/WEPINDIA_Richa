package com.wep.common.app.gst;

/**
 * Created by PriyabratP on 21-11-2016.
 */

public class GSTRData {

    private String userName;

    private String gstin;

    private Object gstvalue;

    public GSTRData(){}

    public GSTRData(String userName, String gstin, Object gstvalue) {
        this.userName = userName;
        this.gstin = gstin;
        this.gstvalue = gstvalue;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Object getGstvalue() {
        return gstvalue;
    }

    public void setGstvalue(Object gstvalue) {
        this.gstvalue = gstvalue;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }
}
