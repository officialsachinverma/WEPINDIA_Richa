package com.wepindia.pos.GenericClasses;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by RichaA on 5/26/2017.
 */

public class DecimalDigitsInputFilter implements InputFilter {

    Pattern mPattern;
    int digitsBeforeZero;
    int isQuantity;

    public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero/*, int isQuantity*/) {
        this.digitsBeforeZero = digitsBeforeZero;
        mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        //this.isQuantity = isQuantity;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        Matcher matcher = mPattern.matcher(dest);
        if (!matcher.matches()) {
            if (dest.toString().contains(".")) {
                if (dest.toString().substring(dest.toString().indexOf(".")).length() > 2) {
//                        MsgBox.Show("Error","Please note after decimal ,value can be entered upto 2 digits only");
                    return "";
                }
                return null;
            } else if (!Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}").matcher(dest).matches()) {
                if (!dest.toString().contains(".")) {
                    if (source.toString().equalsIgnoreCase(".")) {
                        return null;
                    }
                }
                return "";
            } else {
                return null;
            }
        }

        return null;
    }
}
