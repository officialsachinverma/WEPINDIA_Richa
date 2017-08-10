package com.mswipetech.wisepad.payment.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.mswipetech.wisepad.sdktest.view.ApplicationData;


public class CustomCheckBox extends CheckBox {
	ApplicationData applicationData;
	public CustomCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		applicationData = (ApplicationData) context.getApplicationContext();
		this.setTypeface(applicationData.font);
	}
	
	public CustomCheckBox(Context context) {
		super(context);
		applicationData = (ApplicationData) context.getApplicationContext();
		this.setTypeface(applicationData.font);
	}
	
}
