package com.wepindia.pos.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by PriyabratP on 29-11-2016.
 */

public class DateUtil {

    public static String getDateForDatePicker(String input){
        String rData = "";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd");
        java.util.Date date= null;
        try {
            date = sdf.parse(input);
            sdf=new SimpleDateFormat("dd-mm-yyyy");
            rData = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rData;
    }

    public static Date getInMills(String dateTxt){
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        Date d = null;
        try {
            d = f.parse(dateTxt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long milliseconds = d.getTime();
        return d;
    }
}
