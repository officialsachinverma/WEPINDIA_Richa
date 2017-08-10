package com.mswipetech.wisepad.sdktest.view;

import com.mswipetech.wisepad.sdk.MswipeWisepadController;
import com.mswipetech.wisepad.sdktest.data.AppPrefrences;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;

public class ApplicationData extends Application
{
	public final static boolean IS_DEBUGGING_ON = false;
	public Typeface font = null;
	public Typeface fontbold = null;
    public final static String packName = "com.mswipetech.wisepad.sdk";
    public static final String SERVER_NAME = "Mswipe";
    public static final int PhoneNoLength = 10;
    public static final String Currency_Code = "INR.";
    public static final String smsCode = "+91";
    public static final String mTipRequired = "false";
    public static SharedPreferences appSharedPreferences;

	public static String USER_ID = "user_id";
	public static String USER_NAME = "user_name";
	public static String USER_ROLE = "user_role";
	@Override
	public void onCreate()
	{
		super.onCreate();
		font = Typeface.createFromAsset(getAssets(), "fonts/HELVETICANEUELTSTDLTCN.OTF");
		fontbold = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeueLTStd-MdCn.otf");
		appSharedPreferences = this.getSharedPreferences("prefrences", MODE_PRIVATE);
		MultiDex.install(this);
		MswipeWisepadController.setMACADDType(AppPrefrences.getNetworkType());
	}

	public static SharedPreferences getPreference(Activity activity){
		if(appSharedPreferences==null)
			appSharedPreferences = activity.getSharedPreferences("prefrences", MODE_PRIVATE);
		return appSharedPreferences;
	}

	public static String getUserId(Activity activity){
		return getPreference(activity).getString(USER_ID,"").toUpperCase();
	}

	public static String getUserName(Activity activity){
		return getPreference(activity).getString(USER_NAME,"");
	}

	public static String getUserRole(Activity activity){
		return getPreference(activity).getString(USER_ROLE,"");
	}

	public static void savePreference(Activity activity,String key,String value){
		try{
			getPreference(activity).edit().putString(key,value).commit();
		}catch (Exception e){

		}
	}
}





