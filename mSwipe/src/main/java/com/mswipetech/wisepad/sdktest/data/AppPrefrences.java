package com.mswipetech.wisepad.sdktest.data;

import android.content.SharedPreferences.Editor;

import com.mswipetech.wisepad.sdk.MswipeWisepadController.GATEWAYENV;
import com.mswipetech.wisepad.sdk.MswipeWisepadController.NETWORK_TYPE;
import com.mswipetech.wisepad.sdktest.view.ApplicationData;

public class AppPrefrences {
	
	public static void setgateway(GATEWAYENV gateWay){
		Editor edit = ApplicationData.appSharedPreferences.edit();
		edit.putInt("gateway", gateWay.ordinal());
		edit.commit();
	}
	
	public static GATEWAYENV getGateWayEnv(){
		GATEWAYENV gateWayDefault = GATEWAYENV.LABS;		
		int gateWay = ApplicationData.appSharedPreferences.getInt("gateway", gateWayDefault.ordinal());
		return	GATEWAYENV.values()[gateWay];
	}
	
	
	public static void setNetworkType(NETWORK_TYPE macAddType){
		Editor edit = ApplicationData.appSharedPreferences.edit();
		edit.putInt("macaddtype", macAddType.ordinal());
		edit.commit();
	}
	
	public static NETWORK_TYPE getNetworkType(){
		NETWORK_TYPE gateWayDefault = NETWORK_TYPE.WIFI;		
		int gateWay = ApplicationData.appSharedPreferences.getInt("macaddtype", gateWayDefault.ordinal());
		return	NETWORK_TYPE.values()[gateWay];
	}
	
	
	/**
	 * setting amount
	 */
	public static void setLastTrxAmount(String amount){
		Editor edit = ApplicationData.appSharedPreferences.edit();
		edit.putString("amount", amount);
		edit.commit();
	}
	
	/**
	 * getting amount
	 * @return
	 */
	public static String getLastTrxAmount(){	
		String amount = ApplicationData.appSharedPreferences.getString("amount","");
		return amount;
	}
	
	/**
	 * setting card number
	 */
	public static void setLastTrxCardLastFourDigits(String lastfourdigits){
		Editor edit = ApplicationData.appSharedPreferences.edit();
		edit.putString("lastfourdigits", lastfourdigits);
		edit.commit();
	}
	
	/**
	 * getting card number
	 * @return
	 */
	public static String getLastTrxCardLastFourDigits(){	
		String lastfourdigits = ApplicationData.appSharedPreferences.getString("lastfourdigits","");
		return lastfourdigits;
	}
	
	
	/**
	 * setting ksn
	 */
	public static void setLastTrxKSN(String ksn){
		Editor edit = ApplicationData.appSharedPreferences.edit();
		edit.putString("ksn", ksn);
		edit.commit();
	}
	
	/**
	 * getting ksn
	 * @return
	 */
	public static String getLastTrxKSN(){	
		String ksn = ApplicationData.appSharedPreferences.getString("ksn","");
		return ksn;
	}
	
}
