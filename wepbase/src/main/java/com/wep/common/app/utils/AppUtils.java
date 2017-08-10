package com.wep.common.app.utils;

import android.os.Environment;

/**
 * Created by PriyabratP on 06-03-2017.
 */

public class AppUtils {

    public static String FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/WeP_FnB/Images/";

    public static String getImagePath(String icon,String title){
        if(icon == null || icon.equalsIgnoreCase("")){
            String fileName = title;
            icon = AppUtils.FILE_PATH+fileName.replace(" ","_")+".jpg";
        }
        return icon;
    }
}
