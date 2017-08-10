package com.mswipetech.wisepad.payment.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.mswipetech.wisepad.sdktest.view.ApplicationData;


public class CustomEditText extends EditText {
	ApplicationData applicationData;
	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		applicationData = (ApplicationData) context.getApplicationContext();
		this.setTypeface(applicationData.font);
	
	}
	
	public CustomEditText(Context context) {
		super(context);
		applicationData = (ApplicationData) context.getApplicationContext();
		this.setTypeface(applicationData.font);
	}
	
}
