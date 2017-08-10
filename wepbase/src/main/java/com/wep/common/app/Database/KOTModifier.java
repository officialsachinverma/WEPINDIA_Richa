/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	KOTModifier
 * 
 * Purpose			:	Represents KOTModifier table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	15-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package com.wep.common.app.Database;

public class KOTModifier {
	
	// Private Variables
	String strModifierDescription, strModes;
	int iModifierId, iIsChargeable;
	float fModifierAmount;
	
	// Default constructor
	public KOTModifier(){
		this.strModifierDescription = "";
		this.iIsChargeable = 0;
		this.iModifierId = 0;
		this.fModifierAmount = 0;
		this.strModes = "";
	}
	
	// Parameterized construcotr
	public KOTModifier(String ModifierDescription,int IsChargeable,int ModiferId,float ModifierAmount, String ModifierModes){
		this.strModifierDescription = ModifierDescription;
		this.iIsChargeable = IsChargeable;
		this.iModifierId = ModiferId;
		this.fModifierAmount = ModifierAmount;
		this.strModes = ModifierModes;
	}
	
	// getting ModifierDescription
	public String getModifierDescription(){
		return this.strModifierDescription;
	}
	
	// getting IsChargeable
	public int getIsChargeable(){
		return this.iIsChargeable;
	}
	
	// getting ModifierId
	public int getModifierId(){
		return this.iModifierId;
	}
	
	// getting Modifier Amount
	public float getModifierAmount(){
		return this.fModifierAmount;
	}

	// getting Modifier Modes
	public String getModifierModes(){
		return this.strModes;
	}
	
	// setting ModifierDescription
	public void setModofierDescription(String ModifierDescription){
		this.strModifierDescription = ModifierDescription;
	}
	
	// setting IsChargeable
	public void setIsChargeable(int IsChargeable){
		this.iIsChargeable = IsChargeable;
	}
		
	// setting ModifierId
	public void setModofierId(int ModifierId){
		this.iModifierId = ModifierId;
	}
	
	// setting ModifierAmount
	public void setModofierAmount(float ModifierAmount){
		this.fModifierAmount = ModifierAmount;
	}

	// setting ModifierModes
	public void setModifierModes(String ModifierModes){
		this.strModes = ModifierModes;
	}

}
