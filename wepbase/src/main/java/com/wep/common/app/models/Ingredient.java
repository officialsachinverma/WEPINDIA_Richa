package com.wep.common.app.models;

/**
 * Created by RichaA on 5/31/2017.
 */

public class Ingredient {

    private  int sn;
    private  int ingredientcode;
    private  String ingredientName;
    private double IngredientQuantity;
    private String UOM;

    public Ingredient(int sn, int ingredientcode, String ingredientName, double ingredientQuantity, String UOM) {
        this.sn = sn;
        this.ingredientcode = ingredientcode;
        this.ingredientName = ingredientName;
        IngredientQuantity = ingredientQuantity;
        this.UOM = UOM;
    }
    public Ingredient() {
        this.sn = 0;
        this.ingredientcode = -1;
        this.ingredientName = "";
        IngredientQuantity = 0.00;
        this.UOM = "";
    }

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public int getIngredientcode() {
        return ingredientcode;
    }

    public void setIngredientcode(int ingredientcode) {
        this.ingredientcode = ingredientcode;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public double getIngredientQuantity() {
        return IngredientQuantity;
    }

    public void setIngredientQuantity(double ingredientQuantity) {
        IngredientQuantity = ingredientQuantity;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }
}
