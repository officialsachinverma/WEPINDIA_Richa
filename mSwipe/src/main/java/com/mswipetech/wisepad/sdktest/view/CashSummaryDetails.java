package com.mswipetech.wisepad.sdktest.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.mswipetech.wisepad.R;

public class CashSummaryDetails extends BaseTitleActivity 
{
	ApplicationData applicationData = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cashbanksummarydetails);
		applicationData = (ApplicationData)getApplicationContext();

        initViews();
    }
	
	private void initViews() 
	{
		((TextView)findViewById(R.id.topbar_LBL_heading)).setText("cash summary");
		((TextView)findViewById(R.id.topbar_LBL_heading)).setTypeface(applicationData.font);
		
		
		Intent intent = getIntent();
		
		String totalAmt = intent.getStringExtra("CashSaleAmount");
		String totalCount = intent.getStringExtra("CashSaleCount");
		String summaryDate = intent.getStringExtra("SummaryDate");
		
		
		TextView lblSummaryDate = (TextView)  findViewById(R.id.cashsummarydetails_LBL_TxDateTime);
		lblSummaryDate.setTypeface(applicationData.font);
		((TextView)  findViewById(R.id.cashsummarydetails_LBL_lblTxDateTime)).setTypeface(applicationData.font);
		
		if(summaryDate!=null)
			lblSummaryDate.setText(summaryDate);
		
		
		TextView txtAmt = (TextView) findViewById(R.id.cashsummarydetails_LBL_TotalAmt);
		txtAmt.setTypeface(applicationData.font);
		((TextView)  findViewById(R.id.cashsummarydetails_LBL_lblTotalAmt)).setTypeface(applicationData.font);
		
		if(totalAmt!=null)
			txtAmt.setText(totalAmt);
		
		((TextView) findViewById(R.id.cashsummarydetails_LBL_Amtprefixtotal)).setTypeface(applicationData.font);
		((TextView) findViewById(R.id.cashsummarydetails_LBL_Amtprefixtotal)).setText(Constants.Currency_Code);
		
		
		((TextView)findViewById(R.id.cashsummarydetails_LBL_lblNoofVoids)).setText("no. of cash sale");
		((TextView)findViewById(R.id.cashsummarydetails_LBL_lblNoofVoids)).setTypeface(applicationData.font);
		TextView txtVoid = (TextView) findViewById(R.id.cashsummarydetails_LBL_NoofVoids);
		txtVoid.setTypeface(applicationData.font);
		((TextView)  findViewById(R.id.cashsummarydetails_LBL_lblNoofVoids)).setTypeface(applicationData.font);
		
		if(totalCount!=null)
			txtVoid.setText(totalCount);
    	
				
	}	
}