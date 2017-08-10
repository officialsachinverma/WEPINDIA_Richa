package com.wep.common.app.Database;

/**
 * Created by RichaA on 1/5/2017.
 */

public class ItemIngredients {

    String itemname, ingredientname, uom,status, ingredientUOM;
    int menucode, ingredientcode;
    float itemquantity, ingredientquantity;

    public ItemIngredients() {
        this.itemname = "";
        this.ingredientname = "";
        this.uom = "";
        this.ingredientUOM = "";
        this.status = "0";
        this.menucode = 0;
        this.ingredientcode = 0;
        this.itemquantity = 0;
        this.ingredientquantity = 0;
    }

    public String getIngredientUOM() {
        return ingredientUOM;
    }

    public void setIngredientUOM(String ingredientUOM) {
        this.ingredientUOM = ingredientUOM;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getIngredientname() {
        return ingredientname;
    }

    public void setIngredientname(String ingredientname) {
        this.ingredientname = ingredientname;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMenucode() {
        return menucode;
    }

    public void setMenucode(int menucode) {
        this.menucode = menucode;
    }

    public int getIngredientcode() {
        return ingredientcode;
    }

    public void setIngredientcode(int ingredientcode) {
        this.ingredientcode = ingredientcode;
    }

    public float getItemquantity() {
        return itemquantity;
    }

    public void setItemquantity(float itemquantity) {
        this.itemquantity = itemquantity;
    }

    public float getIngredientquantity() {
        return ingredientquantity;
    }

    public void setIngredientquantity(float ingredientquantity) {
        this.ingredientquantity = ingredientquantity;
    }
}
