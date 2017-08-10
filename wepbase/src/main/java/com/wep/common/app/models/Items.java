package com.wep.common.app.models;

/**
 * Created by PriyabratP on 14-02-2017.
 */

public class Items {

    private String itemName;
    private String itemImage;
    private int itemCode;

    public Items() {
    }

    public Items(String itemName, String itemImage, int itemCode) {
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public int getItemCode() {
        return itemCode;
    }

    public void setItemCode(int itemCode) {
        this.itemCode = itemCode;
    }
}
