package com.wep.common.app.print;

import java.io.Serializable;

/**
 * Created by PriyabratP on 06-09-2016.
 */
public class BillTaxItem implements Serializable {

    private String txName;
    private double percent;
    private double price;

    public BillTaxItem() {
    }

    public BillTaxItem(String txName, double percent, double price) {
        this.txName = txName;
        this.percent = percent;
        this.price = price;
    }

    public String getTxName() {
        return txName;
    }

    public void setTxName(String txName) {
        this.txName = txName;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double Percent) {
        this.percent = Percent;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
