package com.mswipetech.wisepad.sdktest.view;

import java.util.Calendar;
import java.util.HashMap;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.mswipetech.wisepad.sdk.MswipeWisepadController;
import com.mswipetech.wisepad.R;
import com.mswipetech.wisepad.sdktest.data.AppPrefrences;

public class BankSummary extends BaseTitleActivity
{
    public final static String log_tab = "CardSummary=>";

    CustomProgressDialog mProgressActivity = null;
    public HashMap<String, String> hashMember = new HashMap<String, String>();

    private static final String[] MONTHS = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private int year;
    private int month;
    private int day;
    public String mSelectedDate = "";

    static final int DATE_DIALOG_ID = 999;

    TextView lblSummaryDate = null;
    ApplicationData applicationData = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardsummary);
        applicationData = (ApplicationData) getApplicationContext();

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        initViews();
    }


    private void initViews() {
    	
        ((TextView) findViewById(R.id.topbar_LBL_heading)).setText("bank summary");
        ((TextView) findViewById(R.id.topbar_LBL_heading)).setTypeface(applicationData.font);

        lblSummaryDate = (TextView) findViewById(R.id.cardsummary_LBL_date);
        lblSummaryDate.setText(day + "/" + MONTHS[month] + "/" + year);
        mSelectedDate = year + "-" + (month + 1) + "-" + day;

        lblSummaryDate.setTypeface(applicationData.font);

        Button btnSubmit = (Button) findViewById(R.id.cardsummary_BTN_submit);
        btnSubmit.setTypeface(applicationData.font);

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
            	
            	getBankSummary();
            	
            }
        });
    }
    
    
    public void getBankSummary() {
        
    	MswipeWisepadController wisepadController = new MswipeWisepadController(this, AppPrefrences.getGateWayEnv(),null);
        wisepadController.getBankSummary(BankSummaryHandler,Constants.Reference_Id, Constants.Session_Tokeniser, mSelectedDate);

        mProgressActivity = null;
        mProgressActivity = new CustomProgressDialog(this, "Fetching Summary...");
        mProgressActivity.show();

    }

    public Handler BankSummaryHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mProgressActivity.dismiss();

            Bundle bundle = msg.getData();
            String responseMsg = bundle.getString("response");
            Log.v(ApplicationData.packName, log_tab + " the response xml is " + responseMsg);

            String[][] strTags = new String[][]{{"status", ""}, {"ErrMsg", ""}, {"BankSaleAmount", ""}, {"BankSaleCount", ""}};
            
            try
            {
            	
            	Constants.parseXml(responseMsg, strTags);
            	
            	String status = strTags[0][1];
                if (status.equalsIgnoreCase("False")) {
                	
                    String ErrMsg = strTags[1][1];
                    Constants.showDialog(BankSummary.this, "Summary", ErrMsg, 1);

                }else{

                	 Intent intent = new Intent(BankSummary.this, BankSummaryDetails.class);
                     intent.putExtra("BankSaleAmount", strTags[2][1]);
                     intent.putExtra("BankSaleCount", strTags[3][1]);
                     intent.putExtra("SummaryDate", lblSummaryDate.getText().toString());

                     startActivity(intent);
                     
                }
            }
            catch (Exception ex) {

                Constants.showDialog(BankSummary.this, "Summary", "The bank summary was not processed successfully please try again", 1);

            }
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            lblSummaryDate.setText(day + "/" + MONTHS[month] + "/" + year);
            mSelectedDate = year + "-" + (month + 1) + "-" + day;

        }
    };

    
    public void onDateClicked(View v){

        showDialog(DATE_DIALOG_ID);
    
    }

}
