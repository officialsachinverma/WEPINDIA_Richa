package com.wep.common.app.print;

import java.io.Serializable;

/**
 * Created by RichaA on 3/7/2017.
 */

public class PrintIngredientsModel implements Serializable {
    private String itemId;
    private String itemName;
    private double qty;
    private String uom;

    public PrintIngredientsModel() {
        this.itemId=" ";
        this.itemName = "";
        this.uom = "";
        this.qty = 0;
    }


    public PrintIngredientsModel(String itemId,String itemName, String uom, double qty) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.uom = uom;
        this.qty = qty;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }
}
