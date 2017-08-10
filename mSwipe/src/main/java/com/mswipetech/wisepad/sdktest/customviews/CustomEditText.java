package com.mswipetech.wisepad.sdktest.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.mswipetech.wisepad.sdktest.view.ApplicationData;

public class CustomEditText extends EditText{
	ApplicationData applicationData;
	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		applicationData = (ApplicationData) context.getApplicationContext();
		this.setTypeface(applicationData.font);
	
	}
	
	public CustomEditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		applicationData = (ApplicationData) context.getApplicationContext();
		this.setTypeface(applicationData.font);
	}
	
}
