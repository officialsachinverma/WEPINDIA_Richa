package com.wepindia.printers.utils;

import com.google.common.base.Strings;

/**
 * Created by PriyabratP on 06-09-2016.
 */
public class StringUtil {


    public static String stringOfLength(final int length, final char c)
    {
        return Strings.padEnd("", length, c);
    }
}
