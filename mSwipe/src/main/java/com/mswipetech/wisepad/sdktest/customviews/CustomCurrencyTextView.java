package com.mswipetech.wisepad.sdktest.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mswipetech.wisepad.sdktest.view.ApplicationData;
import com.mswipetech.wisepad.sdktest.view.Constants;

public class CustomCurrencyTextView extends TextView{
	ApplicationData applicationData;
	public CustomCurrencyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		applicationData = (ApplicationData) context.getApplicationContext();
		this.setTypeface(applicationData.font);
		this.setText(Constants.Currency_Code.toLowerCase());

	}
	
	public CustomCurrencyTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		applicationData = (ApplicationData) context.getApplicationContext();
		this.setTypeface(applicationData.font);
	}
	
}
