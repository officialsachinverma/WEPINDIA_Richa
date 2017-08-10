package com.mswipetech.wisepad.sdktest.view;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mswipetech.wisepad.sdk.MswipeWisepadController;
import com.mswipetech.wisepad.R;
import com.mswipetech.wisepad.sdktest.data.AppPrefrences;
import com.mswipetech.wisepad.sdktest.data.HistoryDetails;

public class CardHistoryList extends BaseTitleActivity {
    public final static String log_tab = "CardHistoryList=>";

    CustomProgressDialog mProgressActivity = null;
    ListView lstHistory = null;
    TextView lblRefresh = null;

    Button mBtnRefresh = null;
    Button mBtnRefreshDown = null;

    LinearLayout lnrLayoutList = null;
    ArrayList<Object> listData = new ArrayList<Object>();
    int totalRecord = 0;
    int ihistoryrecords = 0;

    String randomKey = "";
    ApplicationData applicationData = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardhistorylist);
        applicationData = (ApplicationData) getApplicationContext();
        
        initViews();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void initViews() {
        ((TextView) findViewById(R.id.topbar_LBL_heading)).setText("History");
        ((TextView) findViewById(R.id.topbar_LBL_heading)).setTypeface(applicationData.font);

        lblRefresh = (TextView) findViewById(R.id.cardhistory_LBL_refresh);
        lblRefresh.setTypeface(applicationData.font);


        lstHistory = (ListView) findViewById(R.id.cardhistory_LST_cardhistory);
        lstHistory.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(CardHistoryList.this, CardHistory.class);
                intent.putExtra("curRecord", position + 1);
                intent.putExtra("totalRecord", listData.size());
                intent.putExtra("listData", listData);  
                startActivity(intent);


            }
        });

        mBtnRefresh = (Button) findViewById(R.id.cardhistorylist_BTN_refresh);
        mBtnRefresh.setTypeface(applicationData.font);
        mBtnRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = Constants.showDialog(CardHistoryList.this, Constants.CARDHISTORYLIST_DIALOG_MSG,
                        Constants.CARDHISTORYLIST_GET_HISTROYDATA, "2");
                Button yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
                yes.setTypeface(applicationData.font);
                yes.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        dialog.dismiss();
                        getCardHistory();


                    }
                });

                Button no = (Button) dialog.findViewById(R.id.bmessageDialogNo);
                no.setTypeface(applicationData.font);
                no.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                dialog.show();


            }
        });

        mBtnRefreshDown = (Button) findViewById(R.id.cardhistorylist_BTN_refreshdown);
        mBtnRefreshDown.setTypeface(applicationData.font);
        mBtnRefreshDown.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = Constants.showDialog(CardHistoryList.this, Constants.CARDHISTORYLIST_DIALOG_MSG,
                        Constants.CARDHISTORYLIST_GET_HISTROYDATA, "2");
                Button yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
                yes.setTypeface(applicationData.font);
                yes.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        dialog.dismiss();
                        getCardHistory();


                    }
                });

                Button no = (Button) dialog.findViewById(R.id.bmessageDialogNo);
                no.setTypeface(applicationData.font);
                no.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                dialog.show();


            }
        });

        lnrLayoutList = (LinearLayout) findViewById(R.id.cardhistory_LNR_historylist);

        ShowHistoryData("");


    }

    public void ShowHistoryData(String xmlHistoryData) {
        int recordCount = 0;
        ihistoryrecords = 0;
        totalRecord = xmlHistoryData.length();
        if (totalRecord == 0) {
            lblRefresh.setVisibility(View.VISIBLE);
            mBtnRefresh.setVisibility(View.VISIBLE);
            lnrLayoutList.setVisibility(View.INVISIBLE);

        } else {
            lblRefresh.setVisibility(View.GONE);
            mBtnRefresh.setVisibility(View.GONE);
            lnrLayoutList.setVisibility(View.VISIBLE);
            listData.clear();
            String errMsg= "";
            try{
             errMsg= getHistoryListFromXml(xmlHistoryData);
            }catch(Exception ex){ errMsg="Error";}
            
            if (errMsg.length() == 0 && listData.size()>0) {
                lstHistory.setAdapter(new CardHistoryAdapter(this, listData));
            } else {
                final Dialog dialog = Constants.showDialog(CardHistoryList.this, Constants.CARDHISTORYLIST_DIALOG_MSG, "Unable to show the history data, please contact support.", "1");
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
    }

    public String getHistoryListFromXml(String xmlHistoryData) throws Exception
    {
    	XmlPullParser parser = Xml.newPullParser();
        String errMsg = "";
        boolean isErrElementExists = false;
        HashMap<String, String> hashMember = new HashMap<String, String>();
        
        String strDataTags[] = new String[]{ "TrxDate", "CardLastFourDigits", 
        		"TrxAmount", "TrxType", "TrxNotes", "MerchantInvoice", 
        		"StanNo", "VoucherNo", "AuthNo", 
        		"RRNo", "TrxStatus","TerminalMessage"};
        
        /*String strDataTags[]<t1>TrxDate</t1>
        <t2>CardLastFourDigits</t2>
        <t3>TrxAmount</t3>
        <t4>TrxType</t4>
        <t5>TrxNotes </t5>
        <t6>MerchantInvoice </t6>
        <t7>StanNo</t7>
        <t8>VoucherNo</t8>
        <t9>AuthNo </t9>
        <t10>RRNo</t10>
        <t11>TrxStatus</t11>
        <t12>TerminalMessage</t12> */ 
        
        HistoryDetails historyData = null;
        try {
           parser.setInput(new StringReader(xmlHistoryData));
           int eventType = XmlPullParser.START_TAG;
           boolean leave = false;
           boolean isDocElementExists = false;

           boolean isRowTagFound = false;
           
           int iDataTagIndex = 0;

           while (!leave && eventType != XmlPullParser.END_DOCUMENT) {
               eventType = parser.next();
               switch (eventType) {
                   case XmlPullParser.START_TAG: {
                       if ("ResultElement".equalsIgnoreCase(parser.getName().trim())) {
                           //String xmlText = parser.Text(); // there is no text for this
                           hashMember.put("ResultElement", "");// store the key
                           isDocElementExists = true;
                       
                       } else if ("data".equalsIgnoreCase(parser.getName().trim())) {
                           isRowTagFound = true;
                           historyData = new HistoryDetails();
                           
                       } else if (isDocElementExists && isRowTagFound && !isErrElementExists) {
                           String xmlTag = parser.getName().toString();
                           if (iDataTagIndex < strDataTags.length && strDataTags[iDataTagIndex].equals(xmlTag) ) {
                               
                               eventType = parser.next();
                               String xmlText = "";
                               if (eventType == XmlPullParser.TEXT) {
                                   xmlText = ((parser.getText() == null)? "":parser.getText());
                               } else if (eventType == XmlPullParser.END_TAG) {

                               }
                               if(iDataTagIndex ==0) 	historyData.TrxDate =xmlText;
                               else if(iDataTagIndex ==1) historyData.CardLastFourDigits=xmlText;
                               else if(iDataTagIndex ==2) historyData.TrxAmount=xmlText;
                               else if(iDataTagIndex ==3) historyData.TrxType=xmlText;
                               else if(iDataTagIndex ==4) historyData.TrxNotes=xmlText;
                               else if(iDataTagIndex ==5) historyData.MerchantInvoice=xmlText;
                               else if(iDataTagIndex ==6) historyData.StanNo=xmlText;
                               else if(iDataTagIndex ==7) historyData.VoucherNo=xmlText;
                               else if(iDataTagIndex ==8) historyData.AuthNo=xmlText;
                               else if(iDataTagIndex ==9) historyData.RRNo=xmlText;
                               else if(iDataTagIndex ==10) historyData.TrxStatus=xmlText;
                               else if(iDataTagIndex ==11) historyData.TerminalMessage=xmlText;  
                               iDataTagIndex++;
                               if (ApplicationData.IS_DEBUGGING_ON) {
                                   Logs.v(ApplicationData.packName, log_tab + " The data is  " + xmlText, true, true);
                               }
                               //if (iDataTagIndex != strDataTags.length)
                                   //stHistoryRowData += xmlText + "||";
                               //else
                                   //stHistoryRowData += xmlText;
                               
                               
                               

                           }
                       }
                       break;
                   }
                   case XmlPullParser.END_TAG: {
                       if (!isErrElementExists && isRowTagFound && "data".equalsIgnoreCase(parser.getName().trim())) {
                           isRowTagFound = false;
                           ihistoryrecords++;

                           if (ApplicationData.IS_DEBUGGING_ON) {
                               Logs.v(ApplicationData.packName, log_tab + " The rows " + ihistoryrecords + " data is " + historyData.StanNo, true, true);
                           }
                           if (ihistoryrecords == 1) {
                                
                           }
                           try{
                            //saveValueToHistoryDB(Constants.compressRespData(stHistoryRowData,context), ihistoryrecords);
                           	listData.add(historyData);
                           }
                           catch(Exception ex)              {
                               ex.printStackTrace();
                               errMsg = "Unable to get the History data, please contact support.";
                               leave =true;
                           }
                           //stHistoryRowData = "";
                           iDataTagIndex = 0;
                       }
                       break;

                   }
               }
           }

       } catch (IOException e) {
           e.printStackTrace();
           throw e;

       } catch (Exception e) {
           e.printStackTrace();
           throw e;
       } finally {
           if (parser != null) {
               parser = null;
           }
       }
       return errMsg;
    	
    	
    	
    	 
    }
    public void getCardHistory() {
            
        	MswipeWisepadController wisepadController = new MswipeWisepadController(CardHistoryList.this, AppPrefrences.getGateWayEnv(),null);
            wisepadController.getCardHistory(refreshHandler,Constants.Reference_Id, Constants.Session_Tokeniser);
            

            mProgressActivity = null;
            mProgressActivity = new CustomProgressDialog(CardHistoryList.this, "Fetching History...");
            mProgressActivity.show();

     }


    public Handler refreshHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mProgressActivity.dismiss();

            Bundle bundle = msg.getData();
            String responseMsg = bundle.getString("response");
            Log.v(ApplicationData.packName, log_tab + " the response xml is " + responseMsg);

            String[][] strTags = new String[][]{{"status", ""}, {"ErrMsg", ""}};
            try
            {
                Constants.parseXml(responseMsg, strTags);

                String status = strTags[0][1];
                if (status.equalsIgnoreCase("false")) {
                    String ErrMsg = strTags[1][1];
                    Constants.showDialog(CardHistoryList.this, Constants.CARDHISTORYLIST_DIALOG_MSG, ErrMsg, 1);

                }else{
                	ShowHistoryData(responseMsg);
                }
            
            
                

            }
            catch (Exception ex) {

                Constants.showDialog(CardHistoryList.this, Constants.CARDHISTORYLIST_DIALOG_MSG, Constants.CARDHISTORYLIST_ERROR_PROCESSIING_DATA, 1);

            }


        }
    };

    public class CardHistoryAdapter extends BaseAdapter {
    	ArrayList<Object> listData = null;
        Context context;

        public CardHistoryAdapter(Context context, ArrayList<Object> listData) {
            this.listData = listData;
            this.context = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.cardhistorylistdata, null);
            }
            HistoryDetails cardSaleDetails = (HistoryDetails) listData.get(position);

              //TextView lstFourDigits= (TextView)convertView.findViewById(R.id.cardhistorylist_LBL_lastfourdigits);
            //lstFourDigits.setText(cardSaleDetails.CardFourDigits);

            TextView lblamt = (TextView) convertView.findViewById(R.id.cardhistorylist_LBL_lblamount);
            lblamt.setText(Constants.Currency_Code);
            lblamt.setTypeface(applicationData.font);


            TextView amt = (TextView) convertView.findViewById(R.id.cardhistorylist_LBL_amount);
            amt.setText(cardSaleDetails.TrxAmount.toString());
            amt.setTypeface(applicationData.font);


            TextView date = (TextView) convertView.findViewById(R.id.cardhistorylist_LBL_date);
            date.setText(cardSaleDetails.TrxDate);
            date.setTypeface(applicationData.font);


            TextView type = (TextView) convertView.findViewById(R.id.cardhistorylist_LBL_cardtype);
            type.setText(cardSaleDetails.TrxType);
            type.setTypeface(applicationData.font);

            if (cardSaleDetails.TrxType.toString().equalsIgnoreCase("card sale")) {
                type.setTextColor(Color.rgb(0, 176, 80));

            } else {
                type.setTextColor(Color.rgb(255, 0, 0));
            }
            
            return convertView;
        }

    }
}
