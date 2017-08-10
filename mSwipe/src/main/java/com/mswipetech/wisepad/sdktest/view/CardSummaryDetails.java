package com.mswipetech.wisepad.sdktest.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.mswipetech.wisepad.R;

public class CardSummaryDetails extends BaseTitleActivity 
{
	ApplicationData applicationData = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardsummarydetails);
		applicationData = (ApplicationData)getApplicationContext();

        initViews();
    }


	
	private void initViews() 
	{
		((TextView)findViewById(R.id.topbar_LBL_heading)).setText("card sale summary");
		((TextView)findViewById(R.id.topbar_LBL_heading)).setTypeface(applicationData.font);
		
	
		Intent intent = getIntent();
		
		String TotalAmt = intent.getStringExtra("TotalAmt");
		String VoidAmt = intent.getStringExtra("VoidAmt");
		String NoofSwipes = intent.getStringExtra("NoofSwipes");
		String NoofVoids = intent.getStringExtra("NoofVoids");
		String SummaryDate = intent.getStringExtra("SummaryDate");
													
		
		TextView lblSummaryDate = (TextView)  findViewById(R.id.cardsummarydetails_LBL_TxDateTime);
		lblSummaryDate.setTypeface(applicationData.font);
		((TextView)  findViewById(R.id.cardsummarydetails_LBL_lblTxDateTime)).setTypeface(applicationData.font);
		
		if(SummaryDate!=null)
			lblSummaryDate.setText(SummaryDate);
		
	
    	TextView txtAmt = (TextView) findViewById(R.id.cardsummarydetails_LBL_TotalAmt);
    	txtAmt.setTypeface(applicationData.font);
		((TextView)  findViewById(R.id.cardsummarydetails_LBL_lblTotalAmt)).setTypeface(applicationData.font);

		if(TotalAmt!=null)
			txtAmt.setText(TotalAmt);
		((TextView) findViewById(R.id.cardsummarydetails_LBL_Amtprefixtotal)).setTypeface(applicationData.font);
		((TextView) findViewById(R.id.cardsummarydetails_LBL_Amtprefixtotal)).setText(Constants.Currency_Code);

		
    	TextView txtVoidAmt = (TextView) findViewById(R.id.cardsummarydetails_LBL_VoidAmt);
    	txtVoidAmt.setTypeface(applicationData.font);
		((TextView)  findViewById(R.id.cardsummarydetails_LBL_lblVoidAmt)).setTypeface(applicationData.font);

		((TextView) findViewById(R.id.cardsummarydetails_LBL_Amtprefixvoid)).setTypeface(applicationData.font);
		((TextView) findViewById(R.id.cardsummarydetails_LBL_Amtprefixvoid)).setText(Constants.Currency_Code);

		if(VoidAmt!=null)
			txtVoidAmt.setText(VoidAmt);

    	TextView txtNoOfSwipe = (TextView) findViewById(R.id.cardsummarydetails_LBL_NoofSwipes);
    	txtNoOfSwipe.setTypeface(applicationData.font);
    	((TextView)  findViewById(R.id.cardsummarydetails_LBL_lblNoofSwipes)).setTypeface(applicationData.font);

    	if(NoofSwipes!=null)
			txtNoOfSwipe.setText(NoofSwipes);
    	
    	TextView txtVoid = (TextView) findViewById(R.id.cardsummarydetails_LBL_NoofVoids);
    	txtVoid.setTypeface(applicationData.font);
    	((TextView)  findViewById(R.id.cardsummarydetails_LBL_lblNoofVoids)).setTypeface(applicationData.font);
		if(NoofVoids!=null)
    	txtVoid.setText(NoofVoids);
    	
				
	}	
}