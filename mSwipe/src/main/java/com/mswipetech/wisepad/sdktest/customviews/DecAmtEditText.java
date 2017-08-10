package com.mswipetech.wisepad.sdktest.customviews;
import com.mswipetech.wisepad.sdktest.view.ApplicationData;
import com.mswipetech.wisepad.sdktest.view.Constants;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


public class DecAmtEditText extends EditText {
	Context context = null;
	ApplicationData applicationData;

	public DecAmtEditText(Context context) {
        super(context);
        this.context = context; 
        applicationData = (ApplicationData) context.getApplicationContext();
        init();
    }
 
    public DecAmtEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context; 
        applicationData = (ApplicationData) context.getApplicationContext();
               init();
    }
 
    public DecAmtEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context; 
        applicationData = (ApplicationData) context.getApplicationContext();
        init();
    }
     
    void init() {
         
        // Set bounds of the Clear button so it will look ok
        this.setTypeface(applicationData.font);
        //if the Close image is displayed and the user remove his finger from the button, clear it. Otherwise do nothing
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
 
            	//this would enable the keyborad popup on ontouching the widget when the keyboard is not visible 
            	if (DecAmtEditText.this.isFocused() && DecAmtEditText.this.getText().length() != 0) {
            		DecAmtEditText.this.setSelection(DecAmtEditText.this.getText().length());
                    InputMethodManager imm = (InputMethodManager)
                    		context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
                    }
                    return true;
                }

                return false;
            }
        });
        
        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
            	DecAmtEditText.this.setSelection(DecAmtEditText.this.getText().length());
                

            }
        });
 
        //if text changes, take care of the button
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) 
            {
            	String stAmt =DecAmtEditText.this.getText().toString();
            	String stOrg = removeChar(stAmt, '.');
            	int ilen = stOrg.length();
                if (ilen ==2 || ilen ==1) {
                	stOrg = "." + stOrg;
                } else if (ilen > 2) {
                	stOrg = stOrg.substring(0, ilen - 2) + "." + stOrg.substring(ilen - 2, ilen);
                }
                if (!stOrg.equals(stAmt)) {
                	DecAmtEditText.this.setText(stOrg);
                    
                }
                DecAmtEditText.this.setSelection(DecAmtEditText.this.getText().length());


                
            }
 
            @Override
            public void afterTextChanged(Editable arg0) {
            }
 
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
    }
    
    public String removeChar(String s, char c) {

        String r = "";

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != c) r += s.charAt(i);
        }

        return r;
    }

}
