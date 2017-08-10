package com.mswipetech.wisepad.payment.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mswipetech.wisepad.sdktest.view.ApplicationData;


public class CustomBoldTextView extends TextView {
	ApplicationData applicationData;
	public CustomBoldTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		applicationData = (ApplicationData) context.getApplicationContext();
		this.setTypeface(applicationData.fontbold);
	}
	
	public CustomBoldTextView(Context context) {
		super(context);
		applicationData = (ApplicationData) context.getApplicationContext();
		this.setTypeface(applicationData.fontbold);
	}
	
}
