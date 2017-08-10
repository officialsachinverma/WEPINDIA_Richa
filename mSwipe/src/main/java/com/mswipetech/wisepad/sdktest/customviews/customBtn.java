package com.mswipetech.wisepad.sdktest.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.mswipetech.wisepad.sdktest.view.ApplicationData;

public class customBtn extends Button{
	ApplicationData applicationData;
	public customBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		applicationData = (ApplicationData) context.getApplicationContext();
		this.setTypeface(applicationData.font);
	
	}
	
	public customBtn(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		applicationData = (ApplicationData) context.getApplicationContext();
		this.setTypeface(applicationData.font);
	}
	
}
