package com.mswipetech.wisepad.sdktest.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mswipetech.wisepad.R;
import com.mswipetech.wisepad.sdktest.data.HistoryDetails;

public class CardHistory extends BaseTitleActivity {
    public final static String log_tab = "CardHistory=>";
    public HashMap<String, String> hashResendMember = new HashMap<String, String>();

    int curRecord = 0;
    int totalRecord = 0;
    HistoryDetails historyData = null;

    CustomProgressDialog mProgressActivity = null;
    ApplicationData applicationData = null;
    ArrayList<Object> listData = null;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardhistory);
        applicationData = (ApplicationData) getApplicationContext();

        curRecord = getIntent().getIntExtra("curRecord", 0);
        totalRecord = getIntent().getIntExtra("totalRecord", 0);
        listData = (ArrayList<Object>)getIntent().getSerializableExtra("listData");
        
        initViews();
    }

    @Override
    public void onResume() {

        super.onResume();

    }

    private void initViews() {
        ((TextView) findViewById(R.id.topbar_LBL_heading)).setText("History");
        ((TextView) findViewById(R.id.topbar_LBL_heading)).setTypeface(applicationData.font);

        TextView lblcount = (TextView) findViewById(R.id.cardhistory_LBL_recordcount);
        ((TextView) findViewById(R.id.cardhistory_LBL_recordcount)).setTypeface(applicationData.font);

        lblcount.setText(curRecord + " of " + totalRecord);
        if (totalRecord > 0) {
            historyData = (HistoryDetails)listData.get(curRecord-1);

        }

        TextView lblStatus = (TextView) findViewById(R.id.lsttrxstatus_LBL_TxStatus);
        lblStatus.setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblTxStatus)).setTypeface(applicationData.font);
        lblStatus.setText(getString(R.string.approved));


        TextView lblTxtDateTime = (TextView) findViewById(R.id.lsttrxstatus_LBL_TxDateTime);
        lblTxtDateTime.setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblTxDateTime)).setTypeface(applicationData.font);
        lblTxtDateTime.setText((String) historyData.TrxDate);


        //TextView lblName = (TextView) findViewById(R.id.lsttrxstatus_LBL_CardholderName);
        //lblName.setText((String) historyData.elementAt(1));

        TextView lblNum = (TextView) findViewById(R.id.lsttrxstatus_LBL_CreditCardNum);
        lblNum.setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblCreditCardNum)).setTypeface(applicationData.font);

        lblNum.setText((String) historyData.CardLastFourDigits);

        TextView lblamt = (TextView) findViewById(R.id.lsttrxstatus_LBL_AmtRs);
        lblamt.setText((String) historyData.TrxAmount);
        lblamt.setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblAmtRs)).setTypeface(applicationData.font);

        ((TextView) findViewById(R.id.creditsale_LBL_Amtprefix)).setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.creditsale_LBL_Amtprefix)).setText(Constants.Currency_Code);


        TextView lblType = (TextView) findViewById(R.id.lsttrxstatus_LBL_TypeofTx);
        lblType.setText((String) historyData.TrxType);
        lblType.setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblTypeofTx)).setTypeface(applicationData.font);


        TextView lblStan = (TextView) findViewById(R.id.lsttrxstatus_LBL_StanID);
        lblStan.setText((String) historyData.StanNo);
        lblStan.setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblStanID)).setTypeface(applicationData.font);


        TextView lblVoucher = (TextView) findViewById(R.id.lsttrxstatus_LBL_Voucher);
        lblVoucher.setText((String) historyData.VoucherNo);
        lblVoucher.setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblVoucher)).setTypeface(applicationData.font);

        TextView lblAuthNo = (TextView) findViewById(R.id.lsttrxstatus_LBL_AuthNo);
        lblAuthNo.setText((String) historyData.AuthNo);
        lblAuthNo.setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblAuthNo)).setTypeface(applicationData.font);

        TextView lblRRNo = (TextView) findViewById(R.id.lsttrxstatus_LBL_RRNo);
        lblRRNo.setText((String) historyData.RRNo);
        lblRRNo.setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblRRNo)).setTypeface(applicationData.font);

        TextView lbltmsg = (TextView) findViewById(R.id.lsttrxstatus_TXT_TerminalMessage);
        lbltmsg.setText((String) historyData.TerminalMessage);
        lbltmsg.setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblTerminalMessage)).setTypeface(applicationData.font);

        EditText lblnotes = (EditText) findViewById(R.id.lsttrxstatus_TXT_Notes);
        lblnotes.setText((String) historyData.TrxNotes);
        lblnotes.setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.lsttrxstatus_LBL_lblNotes)).setTypeface(applicationData.font);

        Button btnPrev = (Button) findViewById(R.id.cardhistory_BTN_previous);
        btnPrev.setTypeface(applicationData.font);
        btnPrev.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (curRecord != 1) {
                    curRecord = curRecord - 1;
                    initViews();
                    ScrollView lstView = (ScrollView) findViewById(R.id.cardhistory_SCL_cardhistory);
                    lstView.scrollTo(0, 0);
                } else {
                    finish();
                }
            }
        });

        Button btnNext = (Button) findViewById(R.id.cardhistory_BTN_next);
        btnNext.setTypeface(applicationData.font);
        btnNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (curRecord != totalRecord) {
                    curRecord = curRecord + 1;
                    initViews();
                    ScrollView lstView = (ScrollView) findViewById(R.id.cardhistory_SCL_cardhistory);
                    lstView.scrollTo(0, 0);
                }

            }
        });

        if (listData.size() == 0) {

            final Dialog dialog = Constants.showDialog(CardHistory.this, Constants.CARDHISTORYLIST_DIALOG_MSG, "Unable to show the history data, please contact support.", "1");
            Button yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
            yes.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    dialog.dismiss();

                    finish();

                }
            });
            dialog.show();

        }


    }

    public void destroyHistory() {
        
        finish();
    }

    

    

    

}
