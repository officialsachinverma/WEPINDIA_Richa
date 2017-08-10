package com.mswipetech.wisepad.payment.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mswipetech.wisepad.sdktest.view.ApplicationData;


public class CustomTextView extends TextView {
	ApplicationData applicationData;
	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		applicationData = (ApplicationData) context.getApplicationContext();
		this.setTypeface(applicationData.font);
	}
	
	public CustomTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		applicationData = (ApplicationData) context.getApplicationContext();
		this.setTypeface(applicationData.font);
	}
	
}
