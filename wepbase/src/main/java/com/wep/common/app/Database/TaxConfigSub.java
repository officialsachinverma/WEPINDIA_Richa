/*
 * **************************************************************************
 * Project Name		:	VAJRA
 *
 * File Name		:	TaxConfig
 *
 * Purpose			:	Represents TaxConfig table, takes care of intializing
 * 						assining and returning values of all the variables.
 *
 * DateOfCreation	:	15-October-2012
 *
 * Author			:	Balasubramanya Bharadwaj B S
 *
 * **************************************************************************
 */

package com.wep.common.app.Database;

public class TaxConfigSub {

    // Private Variable
    String strSubTaxDesciption;
    int iSubTaxId;
    float fSubTaxPercentage;
    int iTaxId;

    // Default Constructor
    public TaxConfigSub(){
        this.strSubTaxDesciption = "";
        this.iSubTaxId = 0;
        this.fSubTaxPercentage = 0;
        this.iTaxId = 0;
    }

    // Paramterized Constructor
    public TaxConfigSub(String SubTaxDescription,int SubTaxId,float SubTaxPercentage, int TaxId){
        this.strSubTaxDesciption = SubTaxDescription;
        this.iSubTaxId = SubTaxId;
        this.fSubTaxPercentage = SubTaxPercentage;
        this.iTaxId = TaxId;
    }

    // getting SubTaxDescription
    public String getSubTaxDescription(){
        return this.strSubTaxDesciption;
    }

    // getting SubTaxId
    public int getSubTaxId(){
        return this.iSubTaxId;
    }

    // getting SubTaxPercentage
    public float getSubTaxPercentage(){
        return this.fSubTaxPercentage;
    }

    // getting TaxId
    public float getTaxId(){
        return this.iTaxId;
    }

    // setting SubTaxDescription
    public void setSubTaxDescription(String SubTaxDescription){
        this.strSubTaxDesciption = SubTaxDescription;
    }

    // setting SubTaxId
    public void setSubTaxId(int SubTaxId){
        this.iSubTaxId = SubTaxId;
    }

    // setting SubTaxPercentage
    public void setSubTaxPercentage(float SubTaxPercentage){
        this.fSubTaxPercentage = SubTaxPercentage;
    }

    // setting TaxId
    public void setTaxId(int TaxId){
        this.iTaxId = TaxId;
    }

}
