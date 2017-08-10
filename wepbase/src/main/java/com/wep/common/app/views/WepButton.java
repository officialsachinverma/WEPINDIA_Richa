package com.wep.common.app.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

import com.wep.common.app.R;

/**
 * Created by PriyabratP on 30-01-2017.
 */

public class WepButton extends Button {

    private Context context;
    private boolean btnEnabled;

    public WepButton(Context context) {
        super(context);
        this.context = context;
    }
    public WepButton (Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WepButton, 0, 0);
        try {
            btnEnabled = a.getBoolean(R.styleable.WepButton_btnEnabled, true);
        } finally {
            a.recycle();
        }
        try{
            setEnabled(btnEnabled);
        }catch (Exception e){

        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        try{
            if(enabled)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    setTextColor(context.getResources().getColor(R.color.colorBtnEnabled,context.getTheme()));
                }
                else
                {
                    setTextColor(context.getResources().getColor(R.color.colorBtnEnabled));
                }
            }
            else
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    setTextColor(context.getResources().getColor(R.color.colorBtnDisabled,context.getTheme()));
                }
                else
                {
                    setTextColor(context.getResources().getColor(R.color.colorBtnDisabled));
                }
            }
        }catch (Exception e){

        }
    }
}
