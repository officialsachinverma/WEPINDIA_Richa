package com.mswipetech.wisepad.sdktest.view;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mswipetech.wisepad.sdk.MswipeWisepadController;
import com.mswipetech.wisepad.R;
import com.mswipetech.wisepad.sdktest.data.AppPrefrences;


public class LastTrxStatus extends BaseTitleActivity {
    public final static String log_tab = "LstTx=>";
    CustomProgressDialog mProgressActivity = null;
    String[][] strTags = new String[][]{
    		{"status", ""}, 
    		{"ErrMsg", ""},
     		{"TrxDate", ""}, 
     		{"CardHolderName", ""},
     		{"CardLastFourDigits", ""}, 
     		{"TrxAmount", ""},
     		{"TrxType", ""}, 
     		{"TrxNotes", ""},
     		{"VoucherNo", ""}, 
     		{"AuthNo", ""},
     		{"RRNo", ""}, 
     		{"StanNo", ""},
     		{"TrxStatus", ""}, 
     		{"TerminalMessage",""}, 
     		{"MID",""},
     		{"TID",""},
     		{"BatchNo",""},
     		{"ReceiptNo",""}};

    
    /**
     * Called when the activity is first created.
     */
    ApplicationData applicationData = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lasttrxstatus);
        applicationData = (ApplicationData) getApplicationContext();


        ((TextView) findViewById(R.id.topbar_LBL_heading)).setText("Last Tx");
        ((TextView) findViewById(R.id.topbar_LBL_heading)).setTypeface(applicationData.font);


        LastTrxStatusTask task = new LastTrxStatusTask();
        task.execute();
    }

    @Override
    public void onStart() {

        super.onStart();


    }

    private void initViews() {
        ((LinearLayout) findViewById(R.id.lsttrxstatus_LNR_content)).setVisibility(View.VISIBLE);

        try {
            TextView lblStatus = (TextView) findViewById(R.id.lsttrxstatus_LBL_TxStatus);

            
            
            lblStatus.setText(strTags[12][1]); //TrxStatus
            lblStatus.setTypeface(applicationData.font);
            ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblTxStatus)).setTypeface(applicationData.font);

            if (lblStatus.getText().toString().equalsIgnoreCase("approved")) {
                lblStatus.setTextColor(Color.rgb(0, 176, 80));

            } else {
                lblStatus.setTextColor(Color.rgb(255, 0, 0));
            }
            TextView lblTxtDateTime = (TextView) findViewById(R.id.lsttrxstatus_LBL_TxDateTime);
            lblTxtDateTime.setText(strTags[2][1]); //TrxDate
            lblTxtDateTime.setTypeface(applicationData.font);
            ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblTxDateTime)).setTypeface(applicationData.font);


            TextView lblName = (TextView) findViewById(R.id.lsttrxstatus_LBL_CardholderName);
            lblName.setText(strTags[3][1]);//CardHolderName
            lblName.setTypeface(applicationData.font);
            ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblCardholderName)).setTypeface(applicationData.font);

            TextView lblNum = (TextView) findViewById(R.id.lsttrxstatus_LBL_CreditCardNum);
            lblNum.setText(strTags[4][1]);//CardLastFourDigits
            lblNum.setTypeface(applicationData.font);
            ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblCreditCardNum)).setTypeface(applicationData.font);


            TextView lblamt = (TextView) findViewById(R.id.lsttrxstatus_LBL_AmtRs);
            lblamt.setText(strTags[5][1]);//TrxAmount
            lblamt.setTypeface(applicationData.font);
            ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblAmtRs)).setTypeface(applicationData.font);
            ((TextView) findViewById(R.id.creditsale_LBL_Amtprefix)).setTypeface(applicationData.font);
            ((TextView) findViewById(R.id.creditsale_LBL_Amtprefix)).setText(Constants.Currency_Code);

            TextView lblType = (TextView) findViewById(R.id.lsttrxstatus_LBL_TypeofTx);
            lblType.setText(strTags[6][1]);//TrxType
            lblType.setTypeface(applicationData.font);
            ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblTypeofTx)).setTypeface(applicationData.font);

            TextView lblStan = (TextView) findViewById(R.id.lsttrxstatus_LBL_StanID);
            lblStan.setText(strTags[11][1]);//StanNo
            lblStan.setTypeface(applicationData.font);
            ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblStanID)).setTypeface(applicationData.font);

            TextView lblVoucher = (TextView) findViewById(R.id.lsttrxstatus_LBL_Voucher);
            lblVoucher.setText(strTags[8][1]);//VoucherNo
            lblVoucher.setTypeface(applicationData.font);
            ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblVoucher)).setTypeface(applicationData.font);

            TextView lblAuthNo = (TextView) findViewById(R.id.lsttrxstatus_LBL_AuthNo);
            lblAuthNo.setText(strTags[9][1]);//AuthNo
            lblAuthNo.setTypeface(applicationData.font);
            ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblAuthNo)).setTypeface(applicationData.font);

            TextView lblRRNo = (TextView) findViewById(R.id.lsttrxstatus_LBL_RRNo);
            lblRRNo.setText(strTags[10][1]);//RRNo
            lblRRNo.setTypeface(applicationData.font);
            ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblRRNo)).setTypeface(applicationData.font);

            TextView lbltmsg = (TextView) findViewById(R.id.lsttrxstatus_TXT_TerminalMessage);
            lbltmsg.setText(strTags[13][1]);//TerminalMessage
            lbltmsg.setTypeface(applicationData.font);
            ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblTerminalMessage)).setTypeface(applicationData.font);

            EditText lblnotes = (EditText) findViewById(R.id.lsttrxstatus_TXT_Notes);
            lblnotes.setText(strTags[7][1]);//txtnotes
            lblnotes.setTypeface(applicationData.font);
            ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblNotes)).setTypeface(applicationData.font);

            TextView lblmid = (TextView) findViewById(R.id.lsttrxstatus_TXT_mid);
            lblmid.setText(strTags[14][1]);//txtnotes
            lblmid.setTypeface(applicationData.font);
            ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblmid)).setTypeface(applicationData.font);

            TextView lbltid = (TextView) findViewById(R.id.lsttrxstatus_TXT_tid);
            lbltid.setText(strTags[15][1]);//txtnotes
            lbltid.setTypeface(applicationData.font);
            ((TextView) findViewById(R.id.lsttrxstatus_LBL_lbltid)).setTypeface(applicationData.font);

            TextView lblbatchno = (TextView) findViewById(R.id.lsttrxstatus_TXT_batchno);
            lblbatchno.setText(strTags[16][1]);//txtnotes
            lblbatchno.setTypeface(applicationData.font);
            ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblbatchno)).setTypeface(applicationData.font);

            TextView lblreceiptno = (TextView) findViewById(R.id.lsttrxstatus_TXT_receiptno);
            lblreceiptno.setText(strTags[17][1]);//txtnotes
            lblreceiptno.setTypeface(applicationData.font);
            ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblreceiptno)).setTypeface(applicationData.font);
            

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    private class LastTrxStatusTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {


            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            
        	MswipeWisepadController wisepadController = new MswipeWisepadController(LastTrxStatus.this, AppPrefrences.getGateWayEnv(),null);
            wisepadController.LastTrxStatus(LastTrxStatusHandler,
            		Constants.Reference_Id, Constants.Session_Tokeniser);
            mProgressActivity = new CustomProgressDialog(LastTrxStatus.this, "Fetching last trx details...");
            mProgressActivity.show();


        }
    }

    public Handler LastTrxStatusHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            
         mProgressActivity.dismiss();
         Bundle bundle = msg.getData();
         String responseMsg = bundle.getString("response");
         Log.v(getPackageName(), log_tab + " the response xml is " + responseMsg);

         try
         {
        	 Constants.parseXml(responseMsg, strTags);

             String status = strTags[0][1];
             if (status.equalsIgnoreCase("false")) {
                 String ErrMsg = strTags[1][1];
                 Constants.showDialog(LastTrxStatus.this, "LastTrxStatus", ErrMsg, 1);

             } else if (status.equalsIgnoreCase("true")){
                 			
            	 initViews();

             }else{
                 Constants.showDialog(LastTrxStatus.this, "LstTrxview", "Invalid response from Mswipe server, please contact support.", 1);
             }


         }
         catch (Exception ex) {
             Constants.showDialog(LastTrxStatus.this, "LstTrxview", "Invalid response from Mswipe server, please contact support.", 1);
         }
            

        }
    };
}
