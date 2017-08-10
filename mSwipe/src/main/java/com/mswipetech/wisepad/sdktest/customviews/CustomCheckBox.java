package com.mswipetech.wisepad.sdktest.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.mswipetech.wisepad.sdktest.view.ApplicationData;

public class CustomCheckBox extends CheckBox{
	ApplicationData applicationData;
	public CustomCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		applicationData = (ApplicationData) context.getApplicationContext();
		this.setTypeface(applicationData.font);
	}
	
	public CustomCheckBox(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		applicationData = (ApplicationData) context.getApplicationContext();
		this.setTypeface(applicationData.font);
	}
	
}
