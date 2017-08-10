package com.mswipetech.wisepad.sdktest.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mswipetech.wisepad.sdktest.view.ApplicationData;

public class CustomBoldTextView extends TextView{
	ApplicationData applicationData;
	public CustomBoldTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		applicationData = (ApplicationData) context.getApplicationContext();
		this.setTypeface(applicationData.fontbold);
	}
	
	public CustomBoldTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		applicationData = (ApplicationData) context.getApplicationContext();
		this.setTypeface(applicationData.fontbold);
	}
	
}
