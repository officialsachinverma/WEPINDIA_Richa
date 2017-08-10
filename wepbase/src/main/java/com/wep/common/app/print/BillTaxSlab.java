package com.wep.common.app.print;


import java.io.Serializable;

/**
 * Created by RichaA on 7/20/2017.
 */

public class BillTaxSlab implements Serializable {
    private String TaxIndex;
    private double TaxRate;
    private double IGSTAmount;
    private double CGSTAmount;
    private double SGSTAmount;
    private double TaxableValue;
    private double TotalTaxAmount;

    public BillTaxSlab(String taxIndex, double taxRate, double IGSTAmount, double CGSTAmount, double SGSTAmount, double taxableValue, double totalTaxAmount) {
        TaxIndex = taxIndex;
        TaxRate = taxRate;
        this.IGSTAmount = IGSTAmount;
        this.CGSTAmount = CGSTAmount;
        this.SGSTAmount = SGSTAmount;
        TaxableValue = taxableValue;
        TotalTaxAmount = totalTaxAmount;
    }

    public String getTaxIndex() {
        return TaxIndex;
    }

    public void setTaxIndex(String taxIndex) {
        TaxIndex = taxIndex;
    }

    public double getTaxRate() {
        return TaxRate;
    }

    public void setTaxRate(double taxRate) {
        TaxRate = taxRate;
    }

    public double getIGSTAmount() {
        return IGSTAmount;
    }

    public void setIGSTAmount(double IGSTAmount) {
        this.IGSTAmount = IGSTAmount;
    }

    public double getCGSTAmount() {
        return CGSTAmount;
    }

    public void setCGSTAmount(double CGSTAmount) {
        this.CGSTAmount = CGSTAmount;
    }

    public double getSGSTAmount() {
        return SGSTAmount;
    }

    public void setSGSTAmount(double SGSTAmount) {
        this.SGSTAmount = SGSTAmount;
    }

    public double getTaxableValue() {
        return TaxableValue;
    }

    public void setTaxableValue(double taxableValue) {
        TaxableValue = taxableValue;
    }

    public double getTotalTaxAmount() {
        return TotalTaxAmount;
    }

    public void setTotalTaxAmount(double totalTaxAmount) {
        TotalTaxAmount = totalTaxAmount;
    }
}
