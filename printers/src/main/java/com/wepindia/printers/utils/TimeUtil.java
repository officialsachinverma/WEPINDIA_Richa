package com.wepindia.printers.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by PriyabratP on 04-10-2016.
 */

public class TimeUtil {


    public static String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        return sdf.format(Calendar.getInstance().getTime());
    }

    public static String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        return sdf.format(Calendar.getInstance().getTime());
    }
}
