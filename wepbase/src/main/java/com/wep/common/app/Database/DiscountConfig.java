/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	DiscountConfig
 * 
 * Purpose			:	Represents DiscountConfig table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	15-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package com.wep.common.app.Database;

public class DiscountConfig {

    // Private Variable
    String strDiscDesciption;
    int iDiscId;
    float fDiscPercentage;
    float fDiscAmount;

    // Default Constructor
    public DiscountConfig() {
        this.strDiscDesciption = "";
        this.iDiscId = 0;
        this.fDiscPercentage = 0;
        this.fDiscAmount = 0;
    }

    // Paramterized Constructor
    public DiscountConfig(String DiscDescription, int DiscId, float DiscPercentage, float DiscAmount) {
        this.strDiscDesciption = DiscDescription;
        this.iDiscId = DiscId;
        this.fDiscPercentage = DiscPercentage;
        this.fDiscAmount = DiscAmount;
    }

    // getting DiscDescription
    public String getDiscDescription() {
        return this.strDiscDesciption;
    }

    // getting DiscId
    public int getDiscId() {
        return this.iDiscId;
    }

    // getting DiscPercentage
    public float getDiscPercentage() {
        return this.fDiscPercentage;
    }

    // getting DiscAmount
    public float getDiscAmount() {
        return this.fDiscAmount;
    }

    // setting DiscDescription
    public void setDiscDescription(String DiscDescription) {
        this.strDiscDesciption = DiscDescription;
    }

    // setting DiscId
    public void setDiscId(int DiscId) {
        this.iDiscId = DiscId;
    }

    // setting DiscPercentage
    public void setDiscPercentage(float DiscPercentage) {
        this.fDiscPercentage = DiscPercentage;
    }

    // setting DiscAmount
    public void setDiscAmount(float DiscAmount) {
        this.fDiscAmount = DiscAmount;
    }

}
