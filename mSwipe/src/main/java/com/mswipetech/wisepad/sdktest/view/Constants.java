package com.mswipetech.wisepad.sdktest.view;


import java.io.IOException;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.mswipetech.wisepad.R;
import com.mswipetech.wisepad.sdktest.data.ReciptDataModel;
import com.mswipetech.wisepad.sdktest.util.ReceiptUtility;


public class Constants {

    public static int COLOR_RED = Color.parseColor("#f44336");
    public static int COLOR_GREEN = Color.parseColor("#4caf50");
	
	public static String Session_Tokeniser = "";
	public static String Reference_Id = "";
	public static String Currency_Code = "";
	public static boolean Auto_Reversed_Transaction = false;
	
	public static String mTipRequired = "false";
	public static String mConveniencePercentage="0.00";
	public static String mServiceTax="0.00";	
	
	public static String StandId = "";
	public static String RRNO = "";
	public static String AuthCode = "";
	public static String TrxDate = "";
	public static String Voucherno = "";
	public static String TrxAmount = "";
	
	
	
	
	public static String F055tag = "";
	public static String EmvCardExpdate = "";
	public static String SwitchCardType = "";
	public static String IssuerAuthCode = "";
	
	public static String AppIdentifier = "";
	public static String ApplicationName = "";
	public static String TVR = "";
	public static String TSI = "";
	
	public static String ExpiryDate ="";
	public static String Last4Digits ="";
	public static String Amount ="";
	
	public static boolean isEmvTx = false;
	public static boolean isPinVerfied = false;

	
	public static ReciptDataModel mReciptDataModel;
	
	
	//Banks Sale
	public static String InvoiceNo= ""; 
	public static String MID= "";
	public static String TID= "";
	public static String VoucherNo= "";
	public static String Date= "";
	
    public static void parseXml(String xmlString,String[][] strTags ) throws Exception {

        XmlPullParser parser = Xml.newPullParser();

        try {
            parser.setInput(new StringReader(xmlString));
            int eventType = XmlPullParser.START_TAG;
            boolean leave = false;

            while (!leave && eventType != XmlPullParser.END_DOCUMENT) {
                eventType = parser.next();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                            String xmlTag = parser.getName().toString();
                            for (int iTagIndex = 0; iTagIndex < strTags.length; iTagIndex++) {
                                if (strTags[iTagIndex][0].equals(xmlTag)) {
                                    eventType = parser.next();
                                    if (eventType == XmlPullParser.TEXT) {
                                        String xmlText = parser.getText();
                                            Log.v(ApplicationData.packName, "Constatnts " + xmlTag + " message " + xmlText);
                                        
                                        strTags[iTagIndex][1] = ( xmlText == null ? "" : xmlText);// store the key
                                    } else if (eventType == XmlPullParser.END_TAG) {
                                            Log.v(ApplicationData.packName, "Constatnts " + xmlTag + " message is blank");
                                        strTags[iTagIndex][1]  =  "";
                                    }
                                    break;
                                }
                            }
                        break;
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

    }

    public static void showDialog(Context context, String title, String msg,
                                  int type) {
        String stType = "";
        if (type == 1) {
            stType = "3";
        }
        Dialog dialog = showDialog(context, title, msg, stType, "Ok", "Cancel");
        dialog.show();

    }

    public static Dialog showDialog(Context context, String title, String msg,
                                    String type) {

        return showDialog(context, title, msg, type, "Ok", "Cancel");

    }

    public static Dialog showDialog(Context context, String title, String msg,
                                    String type, String firstBtnText, String secondBtnText) {
        // new AlertDialogMsg(LoginView.this,
        // "Please enter valid User Id and Password", "Login").show();
        final Dialog dialog = new Dialog(context, R.style.styleCustDlg);
        dialog.setContentView(R.layout.customdlg);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setCancelable(true);

        ApplicationData applicationData = (ApplicationData) context.getApplicationContext();

        // set the title
        TextView txttitle = (TextView) dialog.findViewById(R.id.tvmessagedialogtitle);
        txttitle.setText(title);
        txttitle.setTypeface(applicationData.fontbold);


        // to set the message
        TextView message = (TextView) dialog.findViewById(R.id.tvmessagedialogtext);
        message.setText(msg);
        message.setTypeface(applicationData.font);

        Button yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
        yes.setText(firstBtnText);
        yes.setTypeface(applicationData.font);

        Button no = (Button) dialog.findViewById(R.id.bmessageDialogNo);
        no.setText(secondBtnText);
        no.setTypeface(applicationData.font);
        if (type.equalsIgnoreCase("1")) {
            no.setVisibility(View.GONE);
        } else if (type.equalsIgnoreCase("3")) {
            no.setVisibility(View.GONE);
            yes.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    dialog.dismiss();

                }
            });

        }

        return dialog;

    }

    public static Dialog showDialogPin(Context context, String title, String msg,
                                       String type, String firstBtnText, String secondBtnText, String thirdText) {
        // new AlertDialogMsg(LoginView.this,
        // "Please enter valid User Id and Password", "Login").show();
        Dialog dialog = new Dialog(context, R.style.styleCustDlg);
        dialog.setContentView(R.layout.custompindlg);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    return true;
                }

                return false;
            }
        });

        ApplicationData applicationData = (ApplicationData) context.getApplicationContext();

        // set the title
        TextView txttitle = (TextView) dialog.findViewById(R.id.tvmessagedialogtitle);
        txttitle.setText(title);
        txttitle.setTypeface(applicationData.fontbold);


        // to set the message
        TextView message = (TextView) dialog.findViewById(R.id.tvmessagedialogtext);
        message.setText(msg);
        message.setTypeface(applicationData.font);

        Button accept = (Button) dialog.findViewById(R.id.bmessageDialogAccept);
        accept.setText(firstBtnText);
        accept.setTypeface(applicationData.font);


        Button bypass = (Button) dialog.findViewById(R.id.bmessageDialogBypass);
        bypass.setText(secondBtnText);
        bypass.setTypeface(applicationData.font);

        Button no = (Button) dialog.findViewById(R.id.bmessageDialogNo);
        no.setText(thirdText);
        no.setTypeface(applicationData.font);
        if (thirdText.length() == 0) {
            no.setVisibility(View.GONE);
        }
        return dialog;

    }

    public static Dialog shwoAppCustionDialog(Context context, String title) {
        Dialog dialog = new Dialog(context, R.style.styleCustDlg);
        dialog.setContentView(R.layout.application_cust_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    return true;
                }

                return false;
            }
        });

        dialog.setCancelable(true);
        ApplicationData applicationData = (ApplicationData) context.getApplicationContext();

        // set the title
        TextView txttitle = (TextView) dialog.findViewById(R.id.tvmessagedialogtitle);
        txttitle.setText(title);
        txttitle.setTypeface(applicationData.font);
        return dialog;

    }

    public static Dialog showProgDialog(Context context, String msg) {
        // new AlertDialogMsg(LoginView.this,
        // "Please enter valid User Id and Password", "Login").show();
        Dialog dialog = new Dialog(context, R.style.styleCustDlg);
        dialog.setContentView(R.layout.progressdlg);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        ApplicationData applicationData = (ApplicationData) context.getApplicationContext();

        // set the title
        TextView txttitle = (TextView) dialog.findViewById(R.id.tvmessagedialogtitle);
        txttitle.setText(msg);
        txttitle.setTypeface(applicationData.font);

        return dialog;

    }
    
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    // change password and Login
    public static final String PWD_DIALOG_MSG = "Change Password";
    public static final String PWD_ERROR_INVALIDPWD = "Invalid Password! Cannot be blank.";
    public static final String PWD_ERROR_INVALIDPWDLENGTH = "Minimum length of the password should be 6 characters.";
    public static final String PWD_ERROR_INVALIDPWDMAXLENGTH = "Length of the password should be between 6 and 10 characters.";

    public static final String PWD_ERROR_INVALIDPWDNEWLENGTH = "Minimum length of the new password should be 6 characters.";
    public static final String PWD_ERROR_INVALIDPWDMAXNEWLENGTH = "Length of the new password should be between 6 and 10 characters.";
    public static final String PWD_ERROR_INVALIDPWDRETYPELENGTH = "Minimum length of the re-entered password should be 6 characters.";
    public static final String PWD_ERROR_INVALIDNEWANDRETYPE = "New passwords do not match.";
    public static final String PWD_CHANGEPWD_CONFIRMATION = "Would you like to change the password?";
    public static final String PWD_ERROR_PROCESSIING_DATA = "Error in processing your change password request.";
 // cash sale
    public static final String CASHSALE_DIALOG_MSG = "Cash Sale";
    // bank sale
    public static final String BANKSALE_DIALOG_MSG = "Bank Sale";
    public static final String CASHSALE_ERROR_invalidChequeNO = "Required length of the Cheque no. is 6 digits";
    public static final String BANKSALE_ALERT_AMOUNTMSG = "The total  bank sale amount is %s ";

    // card sale
    public static final String CARDSALE_DIALOG_MSG = "Card Sale";
    public static final String CARDSALE_ALERT_AMOUNTMSG = "The total card sale amount is %s ";
    public static final String CARDSALE_ERROR_INVALIDAMT = "Invalid amount! Minimum amount should be " + Currency_Code + " 1.00 to proceed.";
    public static final String CARDSALE_ERROR_INVALIDCARDDIGITS = "Invalid last 4 digits.";
    public static final String CARDSALE_ERROR_salecash_max= "Invalid Sale cash amount!  Amount should be between " + Currency_Code + " 1.00 to 1000.00 to proceed.";
    public static final String CARDSALE_ERROR_salecash= "amount or sale cash is mandatory for this transaction";
    

    public static final String CASHSALE_ALERT_AMOUNTMSG = "The total cash sale amount is %s ";
    
    public static final String CARDSALE_ALERT_swiperAMOUNTMSG = "Total amount of this transaction \nis %s ";
    public static final String CARDSALE_ERROR_mobilenolen = "Required length of the mobile number is %d digits.";
    public static final String CARDSALE_ERROR_mobileformat = "The mobile number cannot start with 0.";
    public static final String CARDSALE_ERROR_receiptvalidation = "Please enter a valid Receipt Number.";
    public static final String CARDSALE_ERROR_receiptmandatory = "Receipt mandatory for this transaction, please un check the field to proceed";


    public static final String CARDSALE_ERROR_email = "Invalid e-mail address.";
    public static final String CARDSALE_ERROR_LstFourDigitsNotMatched = "Last 4 digits don't match, bad card.";
    public static final String CARDSALE_ERROR_PROCESSIING_DATA = "Error in processing Card Sale.";

    public static final String CARDSALE_AMEX_Validation = "Invalid Amex card security code.";

    public static final String CARDSALE_Sign_Validation = "Receipt needs to be signed to proceed.";
    public static final String CARDSALE_Sign_ERROR_PROCESSIING_DATA = "Error in uploading the receipt to " + ApplicationData.SERVER_NAME + " server.";
    public static final String CARDSALE_Sign_SUCCESS_Msg = "Receipt successfully uploaded to " + ApplicationData.SERVER_NAME + " server.";

    public static final String CARDSALE_AUTO_REVERSAL = "Auto Reversal successfull.";
    public static final String CARDSALE_ERROR_FO35 = "Error in processing Card Sale.";


    public static final String CARDSALE_Device_Connect_Msg = "WisePad not connected, please make sure the WisePad is switched on.";
    public static final String CARDSALE_Device_Connecting_Msg = "Connecting to  Wisepad, if its taking longer then usual, please restart the WisePad and try re-connecting.";
    // for Card history
    public static final String CARDHISTORYLIST_DIALOG_MSG = "History";
    public static final String CARDHISTORYLIST_GET_HISTROYDATA = "Fetch the latest sale history?";
    public static final String CARDHISTORYLIST_ERROR_PROCESSIING_DATA = "Error in processing sale history, please try again.";
    public static final String CARDHISTORYLIST_ERROR_NODATA_FOUND = "No sale history found on the " + ApplicationData.SERVER_NAME + " server.";

    // last transaction
    public static final String LSTTRXST_DIALOG_MSG = "Last Tx Status";
    public static final String LSTTRXST_ERROR_FETCHING_DATA = "Error in fetching last tx details.";
    public static final String LSTTRXST_ERROR_Processing_DATA = "Error in processing the last transaction request.";
    public static final String LSTTRXST_Success_msg = "The receipt was successfully resent.";
    public static final String LSTTRXST_NODATAFOUND = "No  transaction found on " + ApplicationData.SERVER_NAME + " server.";
    // Login
    public static final String LOGIN_DIALOG_MSG = "Login";
    public static final String LOGIN_ERROR_ValidUserMsg = "Please enter a valid User Id and Password.";
    public static final String LOGIN_ERROR_Processing_DATA = "Unable to login, please try again.";

    // void
    public static final String VOID_DIALOG_MSG = "Void Sale";
    public static final String VOID_ERROR_Processing_DATA = "Error in processing the void sale request.";
    public static final String VOID_ERROR_Processing_FLAG = "Error in updating the void sale flag.";
    public static final String VOID_ALERT_AMOUNTMSG = "Proceed to void card sale of %s %s for the card ending with last 4 digits %s?";
    public static final String VOID_ALERT_FORVOID = "Would you like to VOID the selected transaction dated %s of %s %s for the card with the last 4 digits %s?";

    public static final String VOID_NODATAFOUND = "No matching transaction found on " + ApplicationData.SERVER_NAME + " server.";
  //Pre auth completion
    public static final String PREAUTHVOID_DIALOG_MSG = "PreAuth Completion";
    public static final String PREAUTHVOID_ERROR_Processing_DATA = "Error in processing the PreAuth sale request.";
    public static final String PREAUTHVOID_ERROR_Processing_FLAG = "Error in updating the PreAuth sale flag.";
    public static final String PREAUTHVOID_ALERT_AMOUNTMSG = "Proceed to Pre auth compeltion  of %s %s for the card ending with last 4 digits %s?";
   // 
    public static final String PREAUTHVOID_ALERT_FORVOID = "Would you like to use Preauth Completion for the selected transaction dated %s of %s %s for the card with the last 4 digits %s?";
    public static final String PREAUTH_ERROR_NODATA_FOUND = "No PreAuth sale data found on the " + ApplicationData.SERVER_NAME + " server.";
    public static final String PREAUTH_ERROR_INVALIDAMT = "Invalid amount ! PreAuth completion amount cannot exceed the PreAuth sale amount.";


//Refund trx
      public static final String REFUNDVOID_DIALOG_MSG = "Refund";
      public static final String REFUNDVOID_ERROR_Processing_DATA = "Error in processing the refund sale request.";
      public static final String REFUNDVOID_ERROR_Processing_FLAG = "Error in updating the refund sale flag.";
      public static final String REFUNDVOID_ALERT_AMOUNTMSG = "Proceed to Refund Trx  of %s %s for the card ending with last 4 digits %s?";
      public static final String REFUNDVOID_ALERT_FORVOID = "Would you like to Refund the selected transaction dated %s of %s %s for the card with the last 4 digits %s?";

      public static final String DEVICEINFO_DIALOG_MSG = "Device Info";

      //Error
      public static final String  MSWIPE_ERROR_INENRYPTINGKEY ="Data encryption error.";
}
