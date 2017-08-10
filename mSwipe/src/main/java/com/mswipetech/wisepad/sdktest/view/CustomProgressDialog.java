package com.mswipetech.wisepad.sdktest.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.mswipetech.wisepad.R;
public class CustomProgressDialog extends Dialog 
{

		String Message = "";
	    Context context = null;
	    @Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.progressdlg);
			ApplicationData applicationData = (ApplicationData)context.getApplicationContext();	

			TextView msg = (TextView) findViewById(R.id.tvmessagedialogtitle);
			msg.setTypeface(applicationData.font);
			msg.setText(Message);
			
		}

	    public CustomProgressDialog(Context context, String msg) {
	        super(context, R.style.styleCustDlg);
	    	this.context = context;
	        Message = msg;
	    	this.setTitle("");
	    	this.setCancelable(false);

	    }

}
