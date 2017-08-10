package com.mswipetech.wisepad.sdktest.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mswipetech.wisepad.R;




public class HelpViewDlg extends Dialog implements android.view.View.OnClickListener
{
	Context context = null;
	public HelpViewDlg(Context context){ 
		super(context, R.style.styleCustDlg);
		this.context = context;
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helpview);
		ApplicationData applicationData = (ApplicationData)context.getApplicationContext();
		
		((TextView) findViewById(R.id.tvmessagedialogtitle) ).setTypeface(applicationData.fontbold);
		((TextView) findViewById(R.id.helpview_LBL_text1) ).setTypeface(applicationData.font);
		((TextView) findViewById(R.id.helpview_LBL_text2) ).setTypeface(applicationData.fontbold);
		((TextView) findViewById(R.id.helpview_LBL_version) ).setTypeface(applicationData.fontbold);
		
		Button button = (Button) findViewById(R.id.helpview_BTN_ok);
		button.setTypeface(applicationData.font);
		button.setOnClickListener(this);
	}
 
	@Override
	public void onClick(View v) 
	{
		int i = v.getId();
		if (i == R.id.helpview_BTN_ok) {
			this.dismiss();

		}
	}
	
	

}
