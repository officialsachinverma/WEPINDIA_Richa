package com.wep.common.app.print;

import java.io.Serializable;

/**
 * Created by PriyabratP on 06-09-2016.
 */
public class BillServiceTaxItem implements Serializable {

    private String txName;
    private double percent;
    private double price;

    public BillServiceTaxItem() {
    }

    public BillServiceTaxItem(String txName, double percent, double price) {
        this.txName = txName;
        this.percent = percent;
        this.price = price;
    }

    public String getServiceTxName() {
        return txName;
    }

    public void setServiceTxName(String txName) {
        this.txName = txName;
    }

    public double getServicePercent() {
        return percent;
    }

    public void setServicePercent(double Percent) {
        this.percent = Percent;
    }

    public double getServicePrice() {
        return price;
    }

    public void setServicePrice(double price) {
        this.price = price;
    }
}
