package com.wep.common.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by PriyabratP on 03-11-2016.
 */

public class Preferences {


    public static SharedPreferences getSharedPreferences(Activity activity,String name)
    {
        return activity.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getSharedPreferencesForPrint(Activity activity)
    {
        return getSharedPreferences(activity,"PrinterConfigurationActivity");
    }

    public static SharedPreferences.Editor getEditorPreferencesForPrint(Activity activity){
        return getSharedPreferencesForPrint(activity).edit();
    }
}
