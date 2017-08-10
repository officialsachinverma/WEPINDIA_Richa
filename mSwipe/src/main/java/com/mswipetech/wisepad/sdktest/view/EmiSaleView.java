package com.mswipetech.wisepad.sdktest.view;


import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.mswipetech.wisepad.sdk.MswipeWisePadDeviceListener;
import com.mswipetech.wisepad.sdk.MswipeWisePadGatewayConnectionListener;
import com.mswipetech.wisepad.sdk.MswipeWisepadController;
import com.mswipetech.wisepad.R;
import com.mswipetech.wisepad.sdktest.data.AppPrefrences;
import com.mswipetech.wisepad.sdktest.data.CardSaleData;
import com.mswipetech.wisepad.sdktest.data.EmiData;
import com.mswipetech.wisepad.sdktest.data.ReciptDataModel;

public class EmiSaleView extends BaseTitleActivity  {
    public final static String log_tab = "EmiSaleView=>";

    //the activity and the wise pad is stopped both in onStop and when back key is pressed in the first screen and
    //this will control the calling of this function twice
    boolean onDoneWithCreditSaleCalled = false;

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVERABLE_BT = 0;
    
    ArrayList<BluetoothDevice> pairedDevicesFound = new ArrayList<BluetoothDevice>();
    // the MAC id of the wise pad device is stored when the connection to the wise pad is success full and
    //will be used later to filter the devices from the paired list of devices
    String mBlueToothMcId = "";
    
    //This will state the  status of the wise pad device  
    int bluetoothConnectionState = 0;
    public final static int DEVCIE_NO_PAIRED_DEVICES = 2;
    public final static int DEVCIE_MULTIPLE_FRIENDLY_DEVICES_FOUND = 3;
    public final static int DEVICE_PAIREDDEVICE_MATCHING = 7;
    public final static int DEVICE_PAIREDDEVICE_MATCHING_MAC = 8;
    public final static int DEVICE_NO_PAIRED_DEVICES_CONNECTED = 9;

    //in the onResume the device is checked to see if the blue tooth is paired
    //this will have the status of the blue-tooth  if its in the process of connecting
    boolean wisePadDeviceConnecting = false;

    //some time the card even though it is approved by gate the IC does not send approve in this scenarios the Trx to is  ignore
    boolean ignoreSendOnlineProcessTransactionResult = false;
    //ignore the back key when swiper routines are in process
    boolean ignoreBackDevicekeyOnCardProcess = false;
   
    //when the online process has started, and when a progress bar is shown, this  which give the ic card a chance to finish of it 
    //routines, this data represents what tasks
    String mstrEMVProcessTaskType = "";
    EMVProcessTask mEMVProcessTask = null;
    CustomProgressDialog mEMVProcessProgressActivity = null;
    
  //tehis will check the new changed sequnce of notificagitons from the sdk when the 
    //setonlineprocessresult is sent a null, in the new implementation the onautoreversal is been called.
    //boolean ignoreOnAutoReversalSetOnlineProcessResultsError;
   
    //when an online is approved and for some reason the card has declined. then show a message in
    //onTransactionResult message as the screen is not in the swiper screen.
    boolean OnOnlineTransactionApproved = false;

    private MswipeWisepadController wisePadController;
    private WisePadSwiperListener listener;

    private Dialog dialog;
    boolean isTrxEmv = true;
   
    
    private boolean isPinCanceled = false;
    CustomProgressDialog mProgressActivity = null;
    
    //fields for emi input screen
    EditText mTxtCardFirstSixDigit = null;
    EditText mTxtEmiAmount = null;
    Button mBtnEmiInputNext = null;
    
    
    //fields for emi selection screen
    ListView emiList;
    Button mBtnEmiSelectionNext = null;
    EmiAdapter mEmiAdapter;
    
    int emiSelectedItemPostion = ListView.INVALID_POSITION;
    boolean[] emiSelectStatus;
    String selectedEmiPeriod;
    String selectedEmiBankCode;
    ArrayList<EmiData> listData = new ArrayList<EmiData>();
    
    int mCurrentScreen = 0;
    //fields for card sale screen
    EditText mTxtCreditAmount = null;
    EditText mTxtSaleCash = null;
    TextView mTxtSaleCashTotal = null;

    //fields for card details
    Button mBtnSubmitCardDetails = null;
    
    //fields for Credit Notes
    EditText mTxtPhoneNo = null;
    EditText mTxtEmail = null;
    EditText mTxtReceipt = null;
    EditText mTxtNotes = null;

    EditText mTxtExtraOne = null;
    EditText mTxtExtraTwo = null;
    EditText mTxtExtraThree = null;
    EditText mTxtExtraFour = null;
    EditText mTxtExtraFive = null;

    //for the swiper menu
    TextView lblAmtMsg = null;
    TextView txtProgMsg = null;

    Button mBtnSwipe = null;
    TextView arrTxtSwiperMsgs[] = null;
   
    ViewFlipper mViewFlipper = null;
    LinearLayout[] arrPageLinearObjects = null;
    ApplicationData applicationData = null;

    
    boolean isPreAuth = false;
    boolean isSaleWithCash = false;
    private String CARDSALE_DIALOG_MSG = "EMI";
    private String CARDSALE_ALERT_AMOUNTMSG = "The total EMI sale amount is %s ";
    
    ReciptDataModel receiptData = new ReciptDataModel();

    private WisePadGatewayConncetionListener messagelistener;
    
	private CardSaleData mCardSaleData = new CardSaleData();;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emisaleview);
        
        applicationData = (ApplicationData) getApplicationContext();
        isPreAuth = getIntent().getBooleanExtra("isPreAuth", false);
        isSaleWithCash = getIntent().getBooleanExtra("isSaleWithCash",false);
       
        if (isPreAuth){
        	
            CARDSALE_DIALOG_MSG = "Pre Auth";
            CARDSALE_ALERT_AMOUNTMSG = "The total pre auth sale amount is %s ";
            
        }else if (isSaleWithCash){
        	
            CARDSALE_DIALOG_MSG = "Sale With Cash";
            CARDSALE_ALERT_AMOUNTMSG = "The total sale cash amount is %s ";
        }
        
              
        listener = new WisePadSwiperListener();
        
        messagelistener = new WisePadGatewayConncetionListener();
        
        wisePadController = new MswipeWisepadController(this, AppPrefrences.getGateWayEnv(),messagelistener);
        wisePadController.initMswipeWisePadDevice(listener);
    
        Constants.Auto_Reversed_Transaction = false;
        Constants.isPinVerfied = false;
        
        initViews();


    }


    @Override
    public void onStop() {
          Log.v(ApplicationData.packName, log_tab + "On stop called***************************************");

          wisePadController.stopMswipeGatewayConnection();
          
        //this will be called only when the user presed the back button or incase of any activity takes the view position.
        if (!onDoneWithCreditSaleCalled) {
            Log.v(ApplicationData.packName, log_tab + "Callind doneWithCredisSale in onStop***************************************");
            doneWithCreditSale();
            try {
                this.wait();
            } catch (Exception ex) {
            }
            super.onStop();
        } else {
            super.onStop();
        }
    }

    @Override
    public void onStart() {
        Log.v(ApplicationData.packName, log_tab + "On Start called***************************************");

        wisePadController.startMswipeGatewayConnection();
        
        if (mCurrentScreen == 0) {
            if (!wisePadController.isDevicePresent()) {
                Log.v(ApplicationData.packName, log_tab + "Device not present connecting to device in the back ground");
                // TODO Auto-generated method stub
                ConnectDeviceTask connecttask = new ConnectDeviceTask();
                connecttask.execute();


            } else {
                arrTxtSwiperMsgs[0].setTextColor(Color.rgb(0, 0, 0)); //pairing the device
                arrTxtSwiperMsgs[0].setText("Device connected");
                arrTxtSwiperMsgs[1].setTextColor(Color.rgb(176, 172, 174)); //press start
                arrTxtSwiperMsgs[2].setTextColor(Color.rgb(176, 172, 174)); //insert the card swiper
                arrTxtSwiperMsgs[3].setTextColor(Color.rgb(176, 172, 174)); //insert the card swiper
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Press start");
                mBtnSwipe.setText("Start");

            }
        }
       
        super.onStart();

    }

    public boolean checkConnections() {
        bluetoothConnectionState = 0;
        pairedDevicesFound.clear();
        if (BluetoothAdapter.getDefaultAdapter() == null)
            return false;
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                Log.v(ApplicationData.packName, log_tab + " Switching Bluetooth on ");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

                Log.v(ApplicationData.packName, log_tab + "Successfully Switched on the Bluetooth");
            return false;
        } else {
                Log.v(ApplicationData.packName, log_tab + " Bluetooth  on ");
            SharedPreferences preferences;
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String stBluetoothAddress = preferences.getString("bluetoothaddress", ""); //34:C8:03:6D:2B:91

 
            Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().toLowerCase().startsWith("wisepad") || 
                    		device.getName().toLowerCase().startsWith("wp") || 
                    			device.getName().toLowerCase().startsWith("1084")) {
                        if (stBluetoothAddress.equals(device.getAddress())) {
                            pairedDevicesFound.clear();
                            pairedDevicesFound.add(device);
                            break;
                        } else {
                            pairedDevicesFound.add(device);
                        }

                    }

                }
            }

            if (pairedDevicesFound.size() > 1) {
                bluetoothConnectionState = DEVCIE_MULTIPLE_FRIENDLY_DEVICES_FOUND;
                return false;
            } else if (pairedDevicesFound.size() == 1) {
                wisePadDeviceConnecting = true;
                bluetoothConnectionState = DEVICE_PAIREDDEVICE_MATCHING;
                wisePadController.startBTv2(pairedDevicesFound.get(0));
                
                
                return true;
            } else {

                bluetoothConnectionState = DEVCIE_NO_PAIRED_DEVICES;
                return true;
            }


        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            // if the state of the device connection is not DEVCIE_NO_PAIRED_DEVICES then from the amount screen it will take to next screen,
            //here the below will restrict the moving back.
            if (mCurrentScreen != 0 && bluetoothConnectionState == DEVCIE_NO_PAIRED_DEVICES) {
                Toast.makeText(this, "Wise pad paring, please wait...", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (mCurrentScreen == 0) {
                doneWithCreditSale();

            } else {
                if (mCurrentScreen == 3 && ignoreBackDevicekeyOnCardProcess) {
                    // the swiper screen
                    Toast.makeText(this, "Processing Card in progress..", Toast.LENGTH_SHORT).show();

                } else if (mCurrentScreen == 4) { // on the card details 	
                    // if its in scree 3 and a EMV Swiper and on pressig back the app has to send back null to the device.
                    if (isTrxEmv) {
                        //this has to be called since the emv is still powered on and waiting for responses, so incase of any
                        //errors the swiper has to be reset
                    	//ignoreOnAutoReversalSetOnlineProcessResultsError = true;
                        wisePadController.sendOnlineProcessResult(null);

                        mEMVProcessProgressActivity = null;
                        mEMVProcessProgressActivity = new CustomProgressDialog(EmiSaleView.this, "Please wait...");
                        mEMVProcessProgressActivity.show();
                        //in case if the task is still running cancel it and not execute any thing which are based on this data.
                        mstrEMVProcessTaskType = "";

                        if (mEMVProcessTask != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
                            mEMVProcessTask.cancel(true);

                        mstrEMVProcessTaskType = "backbutton";

                        mEMVProcessTask = new EMVProcessTask(); //every time create new object, as AsynTask will only be executed one time.
                        mEMVProcessTask.execute();

                    } else {
                        mViewFlipper.showPrevious();
                        mViewFlipper.showPrevious();

                        arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleyellow);
                        mCurrentScreen = mCurrentScreen - 2;
                        arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleblack);
                    }
                    resetSwiperRoutines();

                } else {
                    mViewFlipper.showPrevious();
                    arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleyellow);
                    mCurrentScreen--;
                    arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleblack);

                }
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    public void connectToDevice() {
        if (checkConnections()) {
            arrTxtSwiperMsgs[0].setTextColor(Color.rgb(0, 0, 0)); //press start
            arrTxtSwiperMsgs[0].setText("Connecting device");
            arrTxtSwiperMsgs[1].setTextColor(Color.rgb(176, 172, 174)); //pairing the device
            arrTxtSwiperMsgs[2].setTextColor(Color.rgb(176, 172, 174)); //insert the card swiper
            arrTxtSwiperMsgs[3].setTextColor(Color.rgb(176, 172, 174)); //insert the card swiper

            txtProgMsg.setTextColor(Color.BLUE);
            txtProgMsg.setText("Connecting...");
            mBtnSwipe.setText("Connect");
                Log.v(ApplicationData.packName, log_tab + "Connecting...");
        }
    }

    private class ConnectDeviceTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return "";
        }

        @Override
        protected void onPostExecute(String result) {

            if (checkConnections()) {
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("");

                arrTxtSwiperMsgs[0].setTextColor(Color.rgb(0, 0, 0)); //press start
                arrTxtSwiperMsgs[0].setText("Connecting device");
                arrTxtSwiperMsgs[1].setTextColor(Color.rgb(176, 172, 174)); //pairing the device
                arrTxtSwiperMsgs[2].setTextColor(Color.rgb(176, 172, 174)); //insert the card swiper
                arrTxtSwiperMsgs[3].setTextColor(Color.rgb(176, 172, 174)); //insert the card swiper
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Connecting");
                mBtnSwipe.setText("Connect");

            } else {

                arrTxtSwiperMsgs[0].setTextColor(Color.rgb(0, 0, 0)); //press start
                arrTxtSwiperMsgs[0].setText("Device not connected");
                arrTxtSwiperMsgs[1].setTextColor(Color.rgb(176, 172, 174)); //pairing the device
                arrTxtSwiperMsgs[2].setTextColor(Color.rgb(176, 172, 174)); //insert the card swiper
                arrTxtSwiperMsgs[3].setTextColor(Color.rgb(176, 172, 174)); //insert the card swiper
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Press start");
                mBtnSwipe.setText("Connect");
            }

        }
    }

    private class TaskShowMultiplePairedDevices extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            final Dialog dlgPairedDevices = Constants.shwoAppCustionDialog(EmiSaleView.this, "Paired devcies");
            ArrayList<String> deviceNameList = new ArrayList<String>();
            for (int i = 0; i < pairedDevicesFound.size(); ++i) {
                deviceNameList.add(pairedDevicesFound.get(i).getName());

            }

            ListView appListView = (ListView) dlgPairedDevices.findViewById(R.id.creditsale_LST_applications);
            appListView.setAdapter(new AppViewAdapter(EmiSaleView.this, deviceNameList));
            appListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    wisePadDeviceConnecting = true;
                    wisePadController.startBTv2(pairedDevicesFound.get(position));
                    dlgPairedDevices.dismiss();

                }

            });

            dlgPairedDevices.findViewById(R.id.bmessageDialogNo).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    arrTxtSwiperMsgs[0].setTextColor(Color.rgb(0, 0, 0)); //press start
                    arrTxtSwiperMsgs[0].setText("Connect device");
                    arrTxtSwiperMsgs[1].setTextColor(Color.rgb(176, 172, 174)); //pairing the device
                    arrTxtSwiperMsgs[2].setTextColor(Color.rgb(176, 172, 174)); //insert the card swiper
                    arrTxtSwiperMsgs[3].setTextColor(Color.rgb(176, 172, 174)); //insert the card swiper

                    txtProgMsg.setTextColor(Color.BLUE);
                    txtProgMsg.setText("Press start to connect...");
                    mBtnSwipe.setText("Connect");
                    dlgPairedDevices.dismiss();
                }
            });
            dlgPairedDevices.show();


        }
    }

    class EMVProcessTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onCancelled() {
                Log.v(ApplicationData.packName, log_tab + " EmvOnlinePorcessTask onCancelled Task");

            dismissEMVOnlieProcessProgressActivity();

            //when the back tab is pressed
            if (mstrEMVProcessTaskType.equalsIgnoreCase("backbutton")) {

                mViewFlipper.showPrevious();
                mViewFlipper.showPrevious();

                arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleyellow);
                mCurrentScreen = mCurrentScreen - 2;
                arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleblack);

            } else if (mstrEMVProcessTaskType.equalsIgnoreCase("onlinesubmit")) {
                //dismissProgressDialog();
            } else if (mstrEMVProcessTaskType.equalsIgnoreCase("stopbluetooth")) {
                try {
                    this.notify();
                } catch (Exception ex) {
                }
                finish();
            }

            mstrEMVProcessTaskType = "";

        }

        @Override
        protected Void doInBackground(Void... unused) {

            //calling after this statement and canceling task will no meaning if you do some update database kind of operation
            //so be wise to choose correct place to put this condition
            //you can also put this condition in for loop, if you are doing iterative task
            //you should only check this condition in doInBackground() method, otherwise there is no logical meaning

            // if the task is not cancelled by calling LoginTask.cancel(true), then make the thread wait for 10 sec and then
            //quit it self
                Log.v(ApplicationData.packName, log_tab + "EmvOnlinePorcessTask start doInBackground");
            int isec = 15;
            if (mstrEMVProcessTaskType.equalsIgnoreCase("stopbluetooth"))
                isec = 6;

            int ictr = 0;
            //it will wait for 15 sec or till the task is cancelled by the mSwiper routines.
            while (!isCancelled() & ictr < isec) {
                try {
                    Thread.sleep(1000);
                } catch (Exception ex) {
                }
                ictr++;
            }
                Log.v(ApplicationData.packName, log_tab + "EmvOnlinePorcessTask  end doInBackground");
            return null;
        }


        @Override
        protected void onPostExecute(Void unused) {
                Log.v(ApplicationData.packName, log_tab + "EmvOnlinePorcessTask onPostExecute");
            dismissEMVOnlieProcessProgressActivity();

            //when the back tab is pressed
            if (mstrEMVProcessTaskType.equalsIgnoreCase("backbutton")) {

                mViewFlipper.showPrevious();
                mViewFlipper.showPrevious();

                arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleyellow);
                mCurrentScreen = mCurrentScreen - 2;
                arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleblack);
            } else if (mstrEMVProcessTaskType.equalsIgnoreCase("onlinesubmit")) {

            } else if (mstrEMVProcessTaskType.equalsIgnoreCase("stopbluetooth")) {
                finish();
            }

            mstrEMVProcessTaskType = "";

        }
    }

    private void initViews() {
 
        mViewFlipper = (ViewFlipper) findViewById(R.id.creditsale_VFL_content);
        TextView txtHeading = ((TextView) findViewById(R.id.topbar_LBL_heading));
        txtHeading.setText(CARDSALE_DIALOG_MSG);
        txtHeading.setTypeface(applicationData.font);
        
        
        
        mTxtCardFirstSixDigit = (EditText) findViewById(R.id.creditsale_TXT_cardsixdigit);
        mTxtEmiAmount = (EditText) findViewById(R.id.creditsale_TXT_emiamount);
        mTxtEmiAmount.setTypeface(applicationData.font);
        mBtnEmiInputNext = (Button) findViewById(R.id.creditsale_BTN_emi_input_next);
        mTxtCardFirstSixDigit.requestFocus();
        
        mBtnEmiInputNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getEmiDetails(mTxtCardFirstSixDigit.getText().toString(),mTxtEmiAmount.getText().toString());
			}
		});
		
        emiList = (ListView) findViewById(R.id.creditsale_list_emi_selection);
       
        
        mBtnEmiSelectionNext =(Button) findViewById(R.id.creditsale_BTN_emi_selection_next);
        mBtnEmiSelectionNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(emiSelectedItemPostion == ListView.INVALID_POSITION){
					Constants.showDialog(EmiSaleView.this, CARDSALE_DIALOG_MSG, "please select an emi option.", 1);
				}else{
					mTxtCreditAmount.setText(mTxtEmiAmount.getText().toString());
					mTxtCreditAmount.setEnabled(false);
					mViewFlipper.showNext();
					mCurrentScreen++;
				}
			}
		});
        
        

        //sms prefix from the setting
        TextView txtSmsCode = ((TextView) findViewById(R.id.creditsale_LBL_countrycodeprefix));
        txtSmsCode.setText("+91");
        txtSmsCode.setTypeface(applicationData.font);


        arrPageLinearObjects = new LinearLayout[6];
        arrPageLinearObjects[0] = (LinearLayout) findViewById(R.id.creditsale_LNL_page1);
        arrPageLinearObjects[1] = (LinearLayout) findViewById(R.id.creditsale_LNL_page2);
        arrPageLinearObjects[2] = (LinearLayout) findViewById(R.id.creditsale_LNL_page3);
        arrPageLinearObjects[3] = (LinearLayout) findViewById(R.id.creditsale_LNL_page3);
        arrPageLinearObjects[4] = (LinearLayout) findViewById(R.id.creditsale_LNL_page3);
        arrPageLinearObjects[5] = (LinearLayout) findViewById(R.id.creditsale_LNL_page3);
     
// The screen is for the notes
        mTxtEmail = (EditText) findViewById(R.id.creditsale_TXT_email);
        mTxtReceipt = (EditText) findViewById(R.id.creditsale_TXT_receipt);
        mTxtNotes = (EditText) findViewById(R.id.creditsale_TXT_notes);
        
        mTxtExtraOne = (EditText) findViewById(R.id.creditsale_TXT_extra_one);
        mTxtExtraTwo = (EditText) findViewById(R.id.creditsale_TXT_extra_two);
        mTxtExtraThree = (EditText) findViewById(R.id.creditsale_TXT_extra_three);
        mTxtExtraFour = (EditText) findViewById(R.id.creditsale_TXT_extra_four);
        mTxtExtraFive = (EditText) findViewById(R.id.creditsale_TXT_extra_five);

        mTxtEmail.setTypeface(applicationData.font);
        mTxtReceipt.setTypeface(applicationData.font);
        mTxtNotes.setTypeface(applicationData.font);
        
        mTxtExtraOne.setTypeface(applicationData.font);
        mTxtExtraTwo.setTypeface(applicationData.font);
        mTxtExtraThree.setTypeface(applicationData.font);
        mTxtExtraFour.setTypeface(applicationData.font);
        mTxtExtraFive.setTypeface(applicationData.font);

//The screen are for the amount		
        mTxtCreditAmount = (EditText) findViewById(R.id.creditsale_TXT_amount);
        mTxtPhoneNo = (EditText) findViewById(R.id.creditsale_TXT_mobileno);
        mTxtSaleCash = (EditText) findViewById(R.id.creditsale_TXT_salecash);
        mTxtSaleCashTotal = (TextView) findViewById(R.id.creditsale_TXT_salecash_total);
        
        mTxtSaleCash.setTypeface(applicationData.font);
        mTxtSaleCashTotal.setTypeface(applicationData.font);
        
        mTxtPhoneNo.setTypeface(applicationData.font);

        mTxtCreditAmount.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
				calculateTotalAmt();
				
			}
		});
		
		
		mTxtSaleCash.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
				calculateTotalAmt();
				
			}
		});
     
        if (isSaleWithCash) {
            ((LinearLayout) findViewById(R.id.creditsale_LNR_salecash)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.creditsale_LNR_salecash_total)).setVisibility(View.VISIBLE);


        }

        
       Button btnAmtSwipe = (Button) findViewById(R.id.creditsale_BTN_amt_next);
       btnAmtSwipe.setTypeface(applicationData.font);
       btnAmtSwipe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //remove the decimal since in j2me it does not exists
            	
                double totalAmount = 0;
                double miAmountDisplay = 0;
                DecimalFormat df = new DecimalFormat(".00");
                
            	try{
            		
            		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(EmiSaleView.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
            	}catch(Exception e){
            		e.printStackTrace();
            	}
                

                try {
                    if (mTxtCreditAmount.length() > 0)
                        miAmountDisplay = Double.parseDouble(mTxtCreditAmount.getText().toString());


                } catch (Exception ex) {
                    miAmountDisplay = 0;
                }
                
            	if (isSaleWithCash) {
            		
                    double iSaelCash = 0;
                    
                    try {
                    	
                        if (mTxtSaleCash.length() > 0)
                            iSaelCash = Double.parseDouble(mTxtSaleCash.getText().toString());


                    } catch (Exception ex) {
                        iSaelCash = 0;
                    }

                    
                    if(iSaelCash <= 0 && miAmountDisplay <= 0){
						
						Constants.showDialog(EmiSaleView.this,CARDSALE_DIALOG_MSG, Constants.CARDSALE_ERROR_salecash, 1);
						return;
						
					}else{
						
						if (miAmountDisplay > 0 && miAmountDisplay < 1) {
	                		Constants.showDialog(EmiSaleView.this, CARDSALE_DIALOG_MSG, Constants.CARDSALE_ERROR_INVALIDAMT, 1);
	                		return;
	                	}else if (iSaelCash > 0 && iSaelCash < 1) {
	                		Constants.showDialog(EmiSaleView.this, CARDSALE_DIALOG_MSG, Constants.CARDSALE_ERROR_INVALIDAMT, 1);
	                		return;
	                	}  
						
						if (iSaelCash > 1000) {
							Constants.showDialog(EmiSaleView.this,CARDSALE_DIALOG_MSG, Constants.CARDSALE_ERROR_salecash_max, 1);
							return;
						}
					}
                    
                    totalAmount = iSaelCash + miAmountDisplay;
                    
                }else{

                	if (miAmountDisplay < 1) {
                		Constants.showDialog(EmiSaleView.this, CARDSALE_DIALOG_MSG, Constants.CARDSALE_ERROR_INVALIDAMT, 1);
                		return;
                		//String isEmail="False";
                	}  
                	
                	totalAmount = miAmountDisplay;
                }
                	
                	
                	
                if (mTxtPhoneNo.getText().toString().trim().length() != ApplicationData.PhoneNoLength) {
                    String phoneLength = String.format(Constants.CARDSALE_ERROR_mobilenolen, ApplicationData.PhoneNoLength);
                    Constants.showDialog(EmiSaleView.this, CARDSALE_DIALOG_MSG, phoneLength, 1);
                    mTxtPhoneNo.requestFocus();
                    return;
                } else if (mTxtPhoneNo.getText().toString().trim().startsWith("0")) {
                    Constants.showDialog(EmiSaleView.this, CARDSALE_DIALOG_MSG, Constants.CARDSALE_ERROR_mobileformat, 1);

                    mTxtPhoneNo.requestFocus();
                    return;

                } else {

                	
                    if (mTxtEmail.getText().toString().trim().length() != 0) {

                        if (emailcheck(mTxtEmail.getText().toString())) {

                        } else {
                            mTxtEmail.requestFocus();
                            return;
                        }

                    }
                    
                    String dlgMsg = String.format(CARDSALE_ALERT_AMOUNTMSG, ApplicationData.Currency_Code);
                    final Dialog dialog = Constants.showDialog(EmiSaleView.this, CARDSALE_DIALOG_MSG,
                            dlgMsg + (df.format(totalAmount)), "2");
                    Button yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
                    yes.setOnClickListener(new OnClickListener() {

                        public void onClick(View v) {
                            dialog.dismiss();
                            getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                            mCardSaleData.mAmexSecurityCode = "";
                            mBtnSwipe.setEnabled(true);

                            
                            ///*
                            String swiperMsg = String.format(Constants.CARDSALE_ALERT_swiperAMOUNTMSG, ApplicationData.Currency_Code);
                            
                            
                            lblAmtMsg.setText(swiperMsg +  mTxtCreditAmount.getText().toString());
                            mViewFlipper.showNext();
                            arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleyellow);
                            mCurrentScreen++;
                            arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleblack);

                            //check to see if the connection is already established since this screen can be navigated back from the swiper screen,
                            // show the selection box when the bluetoothConnectinState is set to multiple paired devcies.
                            if (!wisePadController.isDevicePresent()) {
                                // show the popup screen. only when if the bluetoothconnection state multiplepaired devcies
                                if (bluetoothConnectionState == DEVCIE_MULTIPLE_FRIENDLY_DEVICES_FOUND) {
                                    // TODO Auto-generated method stub
                                    TaskShowMultiplePairedDevices pairedtask = new TaskShowMultiplePairedDevices();
                                    pairedtask.execute();

                                } else if (bluetoothConnectionState == DEVCIE_NO_PAIRED_DEVICES) {

                                    String dlgMsg = String.format(CARDSALE_ALERT_AMOUNTMSG, ApplicationData.Currency_Code);
                                    final Dialog dlgNoPairedDevices = Constants.showDialog(EmiSaleView.this, CARDSALE_DIALOG_MSG,
                                            "No paired WisePad found !! Please pair the WisePad from your phone's bluetooth settings and try again.", "1");
                                    Button yes = (Button) dlgNoPairedDevices.findViewById(R.id.bmessageDialogYes);
                                    yes.setOnClickListener(new OnClickListener() {
                                        public void onClick(View v) {

                                            dlgNoPairedDevices.dismiss();
                                            doneWithCreditSale();
                                        }
                                    });
                                    dlgNoPairedDevices.show();

                                }
                            }

 
                        }
                    });

                    Button no = (Button) dialog.findViewById(R.id.bmessageDialogNo);
                    no.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            mTxtCreditAmount.requestFocus();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }

            }
        });

       	//The screen is  for the Swiper		
        //lblProgressMsg = (TextView) findViewById(R.id.creditsale_LBL_lblprogmsg);
        //lblRecordingPause =(TextView) findViewById(R.id.creditsale_LBL_lblrecordpause);
        arrTxtSwiperMsgs = new TextView[6];
        arrTxtSwiperMsgs[0] = (TextView) findViewById(R.id.creditsale_LBL_swipe_step1); //press start
        arrTxtSwiperMsgs[1] = (TextView) findViewById(R.id.creditsale_LBL_swipe_step2); //device detected
        arrTxtSwiperMsgs[2] = (TextView) findViewById(R.id.creditsale_LBL_swipe_step3); //waiting for card swipe
        arrTxtSwiperMsgs[3] = (TextView) findViewById(R.id.creditsale_LBL_swipe_step4); //waiting for card swipe


        lblAmtMsg = (TextView) findViewById(R.id.creditsale_LBL_swipe_amtmsg);
        txtProgMsg = (TextView) findViewById(R.id.creditsale_EDT_swipe_progmsg);

        arrTxtSwiperMsgs[0].setTypeface(applicationData.font);
        arrTxtSwiperMsgs[1].setTypeface(applicationData.font);
        arrTxtSwiperMsgs[2].setTypeface(applicationData.font);
        arrTxtSwiperMsgs[3].setTypeface(applicationData.font);

        lblAmtMsg.setTypeface(applicationData.font);
        txtProgMsg.setTypeface(applicationData.font);

  
        mBtnSwipe = (Button) findViewById(R.id.creditsale_BTN_swipe);
        mBtnSwipe.setTypeface(applicationData.font);

   

        mBtnSwipe.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Button btnSwipe = (Button) arg0;

                if (!wisePadDeviceConnecting) {
                    if (!wisePadController.isDevicePresent()) {

                        final Dialog dialog = Constants.showDialog(EmiSaleView.this, CARDSALE_DIALOG_MSG,
                                Constants.CARDSALE_Device_Connect_Msg, "2", "Connect", "Close");
                        Button yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
                        yes.setOnClickListener(new OnClickListener() {

                            public void onClick(View v) {
                                dialog.dismiss();
                                connectToDevice();

                                // show the popup screen. only when if the bluetoothconnection state multiplepaired devcies
                                if (bluetoothConnectionState == DEVCIE_MULTIPLE_FRIENDLY_DEVICES_FOUND) {
                                    // TODO Auto-generated method stub
                                    TaskShowMultiplePairedDevices pairedtask = new TaskShowMultiplePairedDevices();
                                    pairedtask.execute();
                                } else if (bluetoothConnectionState == DEVCIE_NO_PAIRED_DEVICES) {

                                     String dlgMsg = String.format(CARDSALE_ALERT_AMOUNTMSG, ApplicationData.Currency_Code);
                                    final Dialog dlgNoPairedDevices = Constants.showDialog(EmiSaleView.this, CARDSALE_DIALOG_MSG,
                                            "No paired WisePad found !! Please pair the WisePad from your phone's bluetooth settings and try again.", "1");
                                    Button yes = (Button) dlgNoPairedDevices.findViewById(R.id.bmessageDialogYes);
                                    yes.setOnClickListener(new OnClickListener() {
                                        public void onClick(View v) {

                                            dlgNoPairedDevices.dismiss();
                                            doneWithCreditSale();

                                        }
                                    });
                                    dlgNoPairedDevices.show();
                                }

                            }
                        });

                        Button no = (Button) dialog.findViewById(R.id.bmessageDialogNo);
                        no.setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                        return;
                    } else {
                       
                        arrTxtSwiperMsgs[0].setTextColor(Color.rgb(176, 172, 174)); //press start
                        arrTxtSwiperMsgs[1].setTextColor(Color.rgb(176, 172, 174)); //device detected
                        arrTxtSwiperMsgs[2].setTextColor(Color.rgb(0, 0, 0)); //initializing the swiper
                        arrTxtSwiperMsgs[3].setTextColor(Color.rgb(176, 172, 174)); //device detected

                        txtProgMsg.setTextColor(Color.BLUE);
                        txtProgMsg.setText("");
                        
                        //ignoreOnAutoReversalSetOnlineProcessResultsError = false;
                        ignoreSendOnlineProcessTransactionResult = false;
                        ignoreBackDevicekeyOnCardProcess = true;
                       
                        isPinCanceled = false;
                        Log.v(ApplicationData.packName, log_tab + "********************************************** Swiper IC-Card intialized ******************************************************");
                        isTrxEmv = true;
                        wisePadController.checkCard();
                    }
                } else { // if the device is connecting in the back ground

                    final Dialog dialog = Constants.showDialog(EmiSaleView.this, CARDSALE_DIALOG_MSG,
                            Constants.CARDSALE_Device_Connecting_Msg, "1");

                    Button yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
                    yes.setOnClickListener(new OnClickListener() {

                        public void onClick(View v) {
                            dialog.dismiss();
 
                        }
                    });

                    Button no = (Button) dialog.findViewById(R.id.bmessageDialogNo);
                    no.setOnClickListener(new OnClickListener() 
                    {
                        public void onClick(View v) 
                        {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }

            }
        });


        //The screen is  for the credit details   
        ((TextView) findViewById(R.id.creditsale_LBL_CardHolderName)).setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.creditsale_LBL_CardNo)).setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.creditsale_LBL_ExpiryDate)).setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.creditsale_LBL_AmtRs)).setTypeface(applicationData.font);

        ((TextView) findViewById(R.id.creditsale_LBL_lblCardHolderName)).setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.creditsale_LBL_lblCardNo)).setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.creditsale_LBL_lblExpiryDate)).setTypeface(applicationData.font);
        ((TextView) findViewById(R.id.creditsale_LBL_lblAmtRs)).setTypeface(applicationData.font);


  
        mBtnSubmitCardDetails = (Button) findViewById(R.id.creditsale_BTN_carddetails_submit);
        mBtnSubmitCardDetails.setTypeface(applicationData.font);
        mBtnSubmitCardDetails.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                // TODO Auto-generated method stub
                if (!isTrxEmv) {
                    //this will get called only 
                    processCardSale_MCR();
                } else {

                    //this will get called only 
                    processCardSale_EMV();
                }            
            }
        });
    }
    
    
    protected void getEmiDetails(String cardFirstSixDigits, String amount) {
    	
    	if(cardFirstSixDigits.length() < 6){
    		Constants.showDialog(EmiSaleView.this, CARDSALE_DIALOG_MSG, "first 6 digits of card required", 1);
            return;
    	}
    	
    	double emiAmount;
		if (amount.length() > 0)
			emiAmount = Double.parseDouble(removeChar(amount,','));
		else
			emiAmount = 0;
		
    	if(emiAmount < 1){
			  Constants.showDialog(EmiSaleView.this, CARDSALE_DIALOG_MSG, String.format("invalid amount! minimum amount should be %s 1.00 to proceed", ApplicationData.Currency_Code), 1);
            return;
		}
		// TODO Auto-generated method stub
        mProgressActivity = new CustomProgressDialog(EmiSaleView.this, "Processing Card Sale...");
        mProgressActivity.show();
    	wisePadController.getEmiDetails(emiDetailsHandler, Constants.Reference_Id, Constants.Session_Tokeniser, cardFirstSixDigits, amount);
		
	}

    
    Handler emiDetailsHandler = new Handler(){
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
                    Constants.showDialog(EmiSaleView.this, Constants.CARDHISTORYLIST_DIALOG_MSG, ErrMsg, 1);

                }else{
                	ShowHistoryData(responseMsg);
                	Log.e("data", responseMsg);
                }
            }
            catch (Exception ex) {
                Constants.showDialog(EmiSaleView.this, Constants.CARDHISTORYLIST_DIALOG_MSG, Constants.CARDHISTORYLIST_ERROR_PROCESSIING_DATA, 1);
            }


        };
	};
	
	public void ShowHistoryData(String xmlHistoryData) {
        int recordCount = 0;      
        listData.clear();
        String errMsg= "";
        try{
         errMsg= getEMIListFromXml(xmlHistoryData);
        }catch(Exception ex){ errMsg="Error";}
        
        if (errMsg.length() == 0 && listData.size()>0) {
            emiList.setAdapter(new EmiAdapter(this, listData));
            mViewFlipper.showNext();
            arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleyellow);
            mCurrentScreen++;
            arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleblack);
        } else {
            final Dialog dialog = Constants.showDialog(EmiSaleView.this, Constants.CARDHISTORYLIST_DIALOG_MSG, "Unable to show the history data, please contact support.", "1");
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
	
    public String getEMIListFromXml(String xmlEmiData) throws Exception
    {
    	XmlPullParser parser = Xml.newPullParser();
        String errMsg = "";
        boolean isErrElementExists = false;
        HashMap<String, String> hashMember = new HashMap<String, String>();
        
        String strDataTags[] = new String[]{"EMIBankCode", "EMI_TENURE", "EMI_Rate", "EMI_Amt", "Cash_Back_Rate", "Cash_Back_Amt"};
        
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
        
        EmiData emiData = null;
        try {
           parser.setInput(new StringReader(xmlEmiData));
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
                           emiData = new EmiData();
                           
                       } else if (isDocElementExists && isRowTagFound && !isErrElementExists) {
                           String xmlTag = parser.getName().toString();
                           if (iDataTagIndex < strDataTags.length && strDataTags[iDataTagIndex].equals(xmlTag) ) {
                               
                               eventType = parser.next();
                               String xmlText = "";
                               if (eventType == XmlPullParser.TEXT) {
                                   xmlText = ((parser.getText() == null)? "":parser.getText());
                               } else if (eventType == XmlPullParser.END_TAG) {

                               }
                               if(iDataTagIndex ==0) 	emiData.emiBankCode =xmlText;
                               else if(iDataTagIndex ==1) emiData.emiTenure=xmlText;
                               else if(iDataTagIndex ==2) emiData.emiRate=xmlText;
                               else if(iDataTagIndex ==3) emiData.emiAmt=xmlText;
                               else if(iDataTagIndex ==4) emiData.cashBackRate=xmlText;
                               else if(iDataTagIndex ==5) emiData.cashBackAmt=xmlText;
                               iDataTagIndex++;
                               if (ApplicationData.IS_DEBUGGING_ON) {
                                   Logs.v(ApplicationData.packName, log_tab + " The data is  " + xmlText, true, true);
                               }
                        
                           }
                       }
                       break;
                   }
                   case XmlPullParser.END_TAG: {
                       if (!isErrElementExists && isRowTagFound && "data".equalsIgnoreCase(parser.getName().trim())) {
                           isRowTagFound = false;

                           try{
                            //saveValueToHistoryDB(Constants.compressRespData(stHistoryRowData,context), ihistoryrecords);
                           		listData.add(emiData);
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
    
    
    public String getReceiptDataFromXml(String xmlReceiptData) throws Exception
    {

        XmlPullParser parser = Xml.newPullParser();
        String errMsg = "";
        String xmlText = "";

        try {
        	
        	if (applicationData.IS_DEBUGGING_ON) {
                Logs.v(ApplicationData.packName, log_tab +" receiptdata "+ xmlReceiptData.toString() , true, true);
            }
        	
            parser.setInput(new StringReader(xmlReceiptData));
            int eventType = XmlPullParser.START_TAG;
            boolean leave = false;
            boolean isDocElementExists = false;
            eventType = parser.getEventType();
            String xmlTag;
            
            while (!leave && eventType != XmlPullParser.END_DOCUMENT) {
            	
            	switch (eventType) {
            	
            	case XmlPullParser.START_TAG:
            		
            		xmlTag = parser.getName().toString();
            		
            		/*if (applicationData.IS_DEBUGGING_ON) {
            			Logs.v(ApplicationData.packName, log_tab + " xmlTag " + xmlTag , true, true);
            		}*/
            		
            		if(xmlTag.equalsIgnoreCase("mt2")){
            			
            			isDocElementExists = true;
            			receiptData = new ReciptDataModel();
            			
            		}else{
            			
            			if(isDocElementExists){
            				
            				if(xmlTag.equalsIgnoreCase("BANKNAME")){
            					xmlText = parser.nextText();
            					receiptData.bankName = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("MERCHANTNAME")){
            					xmlText = parser.nextText();
            					receiptData.merchantName = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("MERCHANTADDRESS")){
            					xmlText = parser.nextText();
            					receiptData.merchantAdd = Html.fromHtml((xmlText == null ? "" : xmlText)).toString() ;
            				}else if(xmlTag.equalsIgnoreCase("DATETIME") || xmlTag.equalsIgnoreCase("TrxDate")){
            					xmlText = parser.nextText();
            					receiptData.dateTime = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("MID")){
            					xmlText = parser.nextText();
            					receiptData.mId = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("TID")){
            					xmlText = parser.nextText();
            					receiptData.tId = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("BATCHNO")){
            					xmlText = parser.nextText();
            					receiptData.batchNo = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("INVOICENO") || xmlTag.equalsIgnoreCase("PrismInvoiceNo")){
            					xmlText = parser.nextText();
            					receiptData.invoiceNo = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("REFNO")|| xmlTag.equalsIgnoreCase("MerInvoiceNo")){
            					xmlText = parser.nextText();
            					receiptData.refNo = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("SALETYPE")){
            					xmlText = parser.nextText();
            					receiptData.saleType = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("CARDNO")){
            					xmlText = parser.nextText();
            					receiptData.cardNo = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("TXTYPE")){
            					xmlText = parser.nextText();
            					receiptData.trxType = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("CARDTYPE")){
            					xmlText = parser.nextText();
            					receiptData.cardType = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("EXPDT") || xmlTag.equalsIgnoreCase("CardExpDt")){
            					xmlText = parser.nextText();
            					receiptData.expDate =  (xmlText == null ? "" : xmlText);;
            				}else if(xmlTag.equalsIgnoreCase("EMVSIGEXPDT")){
            					xmlText = parser.nextText();
            					receiptData.emvSigExpDate =  (xmlText == null ? "" : xmlText);;
            				}else if(xmlTag.equalsIgnoreCase("CARDHOLDERNAME")){
            					xmlText = parser.nextText();
            					receiptData.cardHolderName = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("CURRENCY")){
            					xmlText = parser.nextText();
            					receiptData.currency = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("CASHAMOUNT")){
            					xmlText = parser.nextText();
            					receiptData.cashAmount = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("BASEAMOUNT")){
            					xmlText = parser.nextText();
            					receiptData.baseAmount = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("TIPAMOUNT")){
            					xmlText = parser.nextText();
            					receiptData.tipAmount = (xmlText == null ? "" : xmlText);
            					receiptData.isConvenceFeeEnable = "false";
            				}else if(xmlTag.equalsIgnoreCase("CONVENIENCEFEE")){
            					receiptData.tipAmount = parser.nextText();
            					receiptData.isConvenceFeeEnable = "true";
            				}else if(xmlTag.equalsIgnoreCase("TOTALAMOUNT")){
            					xmlText = parser.nextText();
            					receiptData.totalAmount = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("AUTHCODE")){
            					xmlText = parser.nextText();
            					receiptData.authCode = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("RRNO")){
            					xmlText = parser.nextText();
            					receiptData.rrNo = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("CERTIF")){
            					xmlText = parser.nextText();
            					receiptData.certif = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("APPLICATIONIDENTIFIER")){
            					xmlText = parser.nextText();
            					receiptData.appId = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("APPLICATIONNAME")){
            					xmlText = parser.nextText();
            					receiptData.appName = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("TVR")){
            					xmlText = parser.nextText();
            					receiptData.tvr = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("TSI")){
            					xmlText = parser.nextText();
            					receiptData.tsi = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("APPVERSION") || xmlTag.equalsIgnoreCase("VersionNo")){
            					xmlText = parser.nextText();
            					receiptData.appVersion = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("ISPINVERIFIED")){
            					xmlText = parser.nextText();
            					receiptData.isPinVarifed = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("STAN")){
            					xmlText = parser.nextText();
            					receiptData.stan = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("CardIssuuer")){
            					xmlText = parser.nextText();
            					receiptData.cardIssuer = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("EMI_PM_Amount")){
            					xmlText = parser.nextText();
            					receiptData.emiPerMonthAmount = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("Total_Pay_Amount")){
            					xmlText = parser.nextText();
            					receiptData.total_Pay_Amount = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("NoOfEMI")){
            					xmlText = parser.nextText();
            					receiptData.noOfEmi = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("ApplInttRate")){
            					xmlText = parser.nextText();
            					receiptData.interestRate  = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("BillNo")){
            					xmlText = parser.nextText();
            					receiptData.billNo  = (xmlText == null ? "" : xmlText);
            				}else if(xmlTag.equalsIgnoreCase("FirstDigitsOfCard")){
            					xmlText = parser.nextText();
            					receiptData.firstDigitsOfCard  = (xmlText == null ? "" : xmlText);
            				}
            			}
            		}
            		
            		break;
            		
            	case XmlPullParser.END_TAG:
            		
            		xmlTag = parser.getName();
            		
            		/*if (applicationData.IS_DEBUGGING_ON) {
            			Logs.v(ApplicationData.packName, log_tab + " xmlTag " + xmlTag , true, true);
            		}*/
            		
            		if(xmlTag.equalsIgnoreCase("l") || xmlTag.equalsIgnoreCase("p")){
            			isDocElementExists = true;
            			leave = true;
            		}
            		
            		
            		break;
            	}
            	
            	eventType = parser.next();
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

	public void calculateTotalAmt() {
		
		int amt = 0;
		int tips = 0;
		int cash = 0;
		//remove the decimal since in j2me it does not exists
		String tempAmount = mTxtCreditAmount.getText().toString().trim();
		String tempSaleCash = mTxtSaleCash.getText().toString().trim();
		
		tempAmount = removeChar(tempAmount, '.');
		tempAmount = removeChar(tempAmount, ',');
		tempSaleCash = removeChar(tempSaleCash, '.');
		tempSaleCash = removeChar(tempSaleCash, ',');
		
		if (tempAmount.length() > 0) {
			
			amt = Integer.parseInt(tempAmount);
		}
		if (tempSaleCash.length() > 0) {
			
			cash = Integer.parseInt(tempSaleCash);
		}
		
		String tempStr = "";
		if ((amt + tips + cash) > 0)
			tempStr = "" + (amt + tips + cash);
		
		StringBuffer tempamtStr = new StringBuffer(tempStr);
		
		
		int ilen = tempamtStr.length();
		StringBuffer amttotDisplay = new StringBuffer();
		
		if (ilen > 0 && ilen <= 2) {
			
			if(tempamtStr.length() == 1)
				amttotDisplay.append("0.0" + tempamtStr);
			else
				amttotDisplay.append("0." + tempamtStr);
			
		} else if (ilen > 2) {
			tempamtStr.insert(ilen - 2, ".");
			amttotDisplay.append(tempamtStr);
			
		}
		
		String text = amttotDisplay.toString();
		ilen = text.length();
		if (ilen > 6){
			text = text.substring(0, ilen - 6)+","+ text.substring(ilen - 6, ilen);
		}
		
		ilen = text.length();
		if (ilen > 9){
			text = text.substring(0, ilen - 9)+","+ text.substring(ilen - 9, ilen);
		}
		
		mTxtSaleCashTotal.setText(text.toString());
		//the below is commented so that the hint should be displayed when the total is 0.0
		//msTotal = amttotDisplay.toString();
		
	}
    
    public void showSignature()
    {
    	String  mCreditCardNo = mCardSaleData.mCreditCardNo;
    	Constants.AppIdentifier =  mCardSaleData.mAppIdentifier;
    	Constants.ApplicationName =  mCardSaleData.mApplicationName;
    	Constants.TVR =  mCardSaleData.mTVR;
    	Constants.TSI =  mCardSaleData.mTSI;
    	
    	Constants.isEmvTx = isTrxEmv;
    	Constants.ExpiryDate =  mCardSaleData.mExpiryDate;
    	Constants.Amount = mTxtCreditAmount.getText().toString();
    	
    	String last4Digits = "";
        int ilen = mCreditCardNo.length();
        if (ilen >= 4)
        	last4Digits = mCreditCardNo.substring(ilen - 4, ilen);
        else
        	last4Digits = mCreditCardNo;
    	Constants.Last4Digits = last4Digits;
    	
    	
    	String tempFirst2Digits = "";
		if (ilen >= 2)
			tempFirst2Digits = mCreditCardNo.substring(0, 2);
		else
			tempFirst2Digits = mCreditCardNo;
		
		if (tempFirst2Digits.equals("34") || tempFirst2Digits.equals("37")) 
	        Constants.isPinVerfied = false;
    	
    	Constants.mReciptDataModel = receiptData;
    	startActivity(new Intent(EmiSaleView.this, EmiTermCondition.class).putExtra("receiptDetailModel", receiptData));
    	
    	doneWithCreditSale();
    	
    }

    public void processCardSale_MCR()
    {
           	
    	wisePadController.EMI_MCR(CreditSaleHandler,
        		Constants.Reference_Id, 
        		Constants.Session_Tokeniser, 
        		mTxtReceipt.getText().toString(), 
        		mCardSaleData.mAmexSecurityCode,  
        		mTxtCreditAmount.getText().toString(),
        		ApplicationData.smsCode + mTxtPhoneNo.getText().toString(), 
        		mTxtEmail.getText().toString(), 
        		mTxtNotes.getText().toString(),
        		selectedEmiPeriod,
        		selectedEmiBankCode,
        		mTxtExtraOne.getText().toString(),
        		mTxtExtraTwo.getText().toString(),
        		mTxtExtraThree.getText().toString(),
        		mTxtExtraFour.getText().toString(),
        		mTxtExtraFive.getText().toString());
   
                 
        mProgressActivity = new CustomProgressDialog(EmiSaleView.this, "Processing Card Sale...");
        mProgressActivity.show();
            
        
    }
    
    public void processCardSale_EMV()
    {
    	if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(getPackageName(), log_tab 
            		+ "mTVR: " + mCardSaleData.mTVR
            		+ "mTSI: " + mCardSaleData.mTSI, true, true);
            
        String mCreditCardNo = mCardSaleData.mCreditCardNo;

        String last4Digits = "";
        int ilen = mCreditCardNo.length();
        if (ilen >= 4)
        	last4Digits = mCreditCardNo.substring(ilen - 4, ilen);
        else
        	last4Digits = mCreditCardNo;

        String first4Digits = "";
        if (ilen >= 4)
        	first4Digits = mCreditCardNo.substring(0, 4);
        else
        	first4Digits = mCreditCardNo;
              
      	wisePadController.EMI_EMV(CreditSaleHandler,
            		Constants.Reference_Id, 
            		Constants.Session_Tokeniser, 
            		mTxtReceipt.getText().toString(), 
            		mTxtCreditAmount.getText().toString(),
            		ApplicationData.smsCode + mTxtPhoneNo.getText().toString(), 
            		mTxtEmail.getText().toString(),
            		mTxtNotes.getText().toString(),
            		mCardSaleData.mTVR, mCardSaleData.mTSI,
            		selectedEmiPeriod,
            		selectedEmiBankCode,
            		mTxtExtraOne.getText().toString(),
            		mTxtExtraTwo.getText().toString(),
            		mTxtExtraThree.getText().toString(),
            		mTxtExtraFour.getText().toString(),
            		mTxtExtraFive.getText().toString());
          
        
        mProgressActivity = new CustomProgressDialog(EmiSaleView.this, "Processing Chip Card ...");
        mProgressActivity.show();
         
    }
  
    public Handler CreditSaleHandler = new Handler() 
    {
        public void handleMessage(android.os.Message msg) 
        {
            
	         mProgressActivity.dismiss();
	         Bundle bundle = msg.getData();
	         String responseMsg = bundle.getString("response");
	         Log.v(ApplicationData.packName, log_tab + " the response xml is " + responseMsg);
        	 String errMsg = "";
        		
	         try
	         {
	             
	        	 String[][] strTags = new String[][]{{"status", ""}, {"ErrMsg", ""},
	        	     		{"StandId", ""}, {"RRNO", ""},
	        	     		{"AuthCode", ""}, {"Date", ""},
	        	     		{"F055tag", ""}, {"EmvCardExpdate", ""},
	        	     		{"SwitchCardType", ""}, {"IssuerAuthCode", ""},
	        	     		{"MID", ""}, {"TID", ""},
	        	     		{"BatchNo", ""}};
	 
	             Constants.parseXml(responseMsg, strTags);
	
	             String status = strTags[0][1];
	             if (status.equalsIgnoreCase("false")) {
	                 errMsg = strTags[1][1];
	                 
	                 //the below has to be called for any erroneous response form the mswipe server
	                 if (isTrxEmv){

	                	 //ignoreOnAutoReversalSetOnlineProcessResultsError = true;
	                     wisePadController.sendOnlineProcessResult(null);
	                 }
	
	             } else if (status.equalsIgnoreCase("true")){

		        	 	try {
							getReceiptDataFromXml(responseMsg.substring(responseMsg.indexOf("<ReceiptData>")+13, responseMsg.indexOf("</ReceiptData>")));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                 String StandId =  strTags[2][1];
	                 Constants.StandId =StandId;
	                 mCardSaleData.mStandId = Constants.StandId;
	                 Log.v(ApplicationData.packName, log_tab + " StandId  " + StandId);
	                 
	                 String RRNO =  strTags[3][1];
	                 Constants.RRNO =RRNO;
	                 Log.v(ApplicationData.packName, log_tab + " RRNO  " + RRNO);
	                 
	                 String AuthCode =  strTags[4][1];
	                 Constants.AuthCode =AuthCode;
	                 Log.v(ApplicationData.packName, log_tab + " AuthCode  " + AuthCode);
	
	                 String Date =  strTags[5][1];
	                 Constants.TrxDate =Date;
	                 Log.v(ApplicationData.packName, log_tab + " Date  " + Date);
	            	 
	                 mCardSaleData.mAuthCode = AuthCode;
	                 mCardSaleData.mRRNo = RRNO;
	                 mCardSaleData.mDate = Date;
	                 if(!isTrxEmv)
	                 {
	                	 mCardSaleData.mMid =  strTags[6][1];
			             Log.v(ApplicationData.packName, log_tab + " MID  " + mCardSaleData.mMid);
			             
			             mCardSaleData.mTid =  strTags[7][1];
			             Log.v(ApplicationData.packName, log_tab + " TID  " + mCardSaleData.mTid);
			             
			             mCardSaleData.mBatchNo =  strTags[8][1];
			             Log.v(ApplicationData.packName, log_tab + " BatchNo  " + mCardSaleData.mBatchNo);
	                 }
	                 else{
	                	 mCardSaleData.mMid =  strTags[10][1];
			             Log.v(ApplicationData.packName, log_tab + " MID  " + mCardSaleData.mMid);
			             
			             mCardSaleData.mTid =  strTags[11][1];
			             Log.v(ApplicationData.packName, log_tab + " TID  " + mCardSaleData.mTid);
			             
			             mCardSaleData.mBatchNo =  strTags[12][1];
			             Log.v(ApplicationData.packName, log_tab + " BatchNo  " + mCardSaleData.mBatchNo);
	                 }
	                    
	                   
	            	if(isTrxEmv)
	        		{
	      
	            		String F055tag =  strTags[6][1];
	                    Constants.F055tag =F055tag;
	                    Log.v(ApplicationData.packName, log_tab + " F055tag  " + F055tag);
	             	   
	                    String EmvCardExpdate =  strTags[7][1];
	                    Constants.EmvCardExpdate =EmvCardExpdate;
	                    Log.v(ApplicationData.packName, log_tab + " EmvCardExpdate  " + EmvCardExpdate);
	                    
	                    String SwitchCardType =  strTags[8][1];
	                    Constants.SwitchCardType =SwitchCardType;
	                    Log.v(ApplicationData.packName, log_tab + " SwitchCardType  " + SwitchCardType);
	                    
	                    String IssuerAuthCode =  strTags[9][1];
	                    Constants.IssuerAuthCode =IssuerAuthCode;
	                    Log.v(ApplicationData.packName, log_tab + " F055tag  " + IssuerAuthCode);
	                    
	                    mCardSaleData.mIssuerAuthCode = IssuerAuthCode;
	                    mCardSaleData.mEmvCardExpdate = EmvCardExpdate;
	                    mCardSaleData.mSwitchCardType = SwitchCardType;
	                    mCardSaleData.mF055tag = F055tag;
	                   
	                    
	                    
	                    // incase if the pin has been cancelled and the trx was submitted to the server then
	                    //for the resposne back form the server send null to the emv device
	                    if (isPinCanceled) {
	                    	//ignoreOnAutoReversalSetOnlineProcessResultsError = true;
	                        wisePadController.sendOnlineProcessResult(null);
	                        
	                        
	                    } else {
	                    	//if the trx is successfull and approved by the mswipe gateway send the response mIssuerAuthCode
	                    	//back to the emv device which check this issuecode for further validation
	                        
	                        OnOnlineTransactionApproved = true;
	
	                        String tlvProcessResultData = mCardSaleData.mIssuerAuthCode;
	                        Log.v(ApplicationData.packName, log_tab + "The issuer Authenticate code  sendOnlineProcessResult data " + tlvProcessResultData);
	                        wisePadController.sendOnlineProcessResult(tlvProcessResultData);
	
	                        mEMVProcessProgressActivity = null;
	                        mEMVProcessProgressActivity = new CustomProgressDialog(EmiSaleView.this, "Please wait...");
	                        mEMVProcessProgressActivity.show();
	
	                        //in case if the task is still running cancel it and not execute any thing which are based on this data.
	                        mstrEMVProcessTaskType = "";
	
	                        if (mEMVProcessTask != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
	                            mEMVProcessTask.cancel(true);
	
	                        mstrEMVProcessTaskType = "onlinesubmit";
	
	                        mEMVProcessTask = new EMVProcessTask(); //every time create new object, as AsynTask will only be executed one time.
	                        mEMVProcessTask.execute();
	
	                    }
	        			
	        		}else{
	                    
	                    CardSaleDialog dlgTrxResults = new CardSaleDialog(EmiSaleView.this, null, CARDSALE_DIALOG_MSG, "Approved",
	                    		mCardSaleData.mAuthCode, mCardSaleData.mRRNo, true);
	                    dlgTrxResults.setOnDismissListener(new OnDismissListener() {
	
	                        @Override
	                        public void onDismiss(DialogInterface dialog) {
	                            showSignature();
	
	                        }
	                    });
	                    dlgTrxResults.show();
	        			
	        		}
	  
	
	             }else{
		        	 errMsg = "Invalid response from Mswipe server, please contact support.";
	             }
	
	
	         }
	         catch (Exception ex) {
	        	 errMsg = "Invalid response from Mswipe server, please contact support.";
	         }
	         
	         if(errMsg.length() !=0)
	         {
	        	 	
	        	 
	                final Dialog dlg = Constants.showDialog(EmiSaleView.this, CARDSALE_DIALOG_MSG, errMsg, "1");
	                 Button btnOk = (Button) dlg.findViewById(R.id.bmessageDialogYes);
	                 btnOk.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								dlg.dismiss();						
								doneWithCreditSale();
								
							}
						});
	                 
	                 dlg.show();

	         }
            

        }
    };

    public void processReversalSale(String offlineDeclineTag)
    {
    	
    	Constants.Auto_Reversed_Transaction = true;
    	
        String mCreditCardNo = mCardSaleData.mCreditCardNo;

    	String last4Digits = "";
        int ilen = mCreditCardNo.length();
        if (ilen >= 4)
        	last4Digits = mCreditCardNo.substring(ilen - 4, ilen);
        else
        	last4Digits = mCreditCardNo;
        
    	
        MswipeWisepadController wisepadController = new MswipeWisepadController(EmiSaleView.this, AppPrefrences.getGateWayEnv(),null);
        wisepadController.AutoReversalCardSaleTrx(voidSaleReversalHandler,
        		Constants.Reference_Id, Constants.Session_Tokeniser, mCardSaleData.mDate, mTxtCreditAmount.getText().toString(),
        		last4Digits, mCardSaleData.mStandId, mCardSaleData.mF055tag, offlineDeclineTag);
   

        mProgressActivity = new CustomProgressDialog(EmiSaleView.this, "Processing auto reversal Tx...");
        mProgressActivity.show();
         
    }
    
    public Handler voidSaleReversalHandler = new Handler() 
    {
        public void handleMessage(android.os.Message msg) 
        {
  
	        String errMsg = "";	
	         mProgressActivity.dismiss();
	         Bundle bundle = msg.getData();
	         String responseMsg = bundle.getString("response");
	         Log.v(ApplicationData.packName, log_tab + " the response xml is " + responseMsg);
	
	         try
	         {
	        	 String[][] strTags = new String[][]{{"status", ""}, {"ErrMsg", ""},
	        	     		
	        	    		};
	             Constants.parseXml(responseMsg, strTags);
	
	             String status = strTags[0][1];
	             if (status.equalsIgnoreCase("false")) {
	                 errMsg = strTags[1][1];
	                 
	
	             } else if (status.equalsIgnoreCase("true")){
	                 		errMsg = "Auto Reversal successfull.";	
	
	             }else{
	            	 errMsg = "Invalid response from Mswipe server, please contact support.";
	            		             
	             }
	
	
	         }
	         catch (Exception ex) {
	        	 errMsg = "Invalid response from Mswipe server, please contact support.";
	         }
	         
	         if(errMsg.length() !=0)
	         {
	        	 
	        	 
	                final Dialog dlg = Constants.showDialog(EmiSaleView.this, CARDSALE_DIALOG_MSG, errMsg, "1");
	                 Button btnOk = (Button) dlg.findViewById(R.id.bmessageDialogYes);
	                 btnOk.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								dlg.dismiss();						
								doneWithCreditSale();
								
							}
						});
	                 dlg.show();

	         }
        }
    };

    public void showCreditDetailsScreen() {
        ignoreBackDevicekeyOnCardProcess = false;
        
        //storing the wisepad device mac is and will be used for further filtering the devcie using this mac id 
       // from the paired devices list
        SharedPreferences preferences;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putString("bluetoothaddress", mBlueToothMcId).commit();
        mBtnSwipe.setEnabled(true);
        mBtnSwipe.setBackgroundResource(R.drawable.roundrectblue);
        
        String mCreditCardNo = mCardSaleData.mCreditCardNo;
        String tempLast4digits = "";
        
        int ilen = mCreditCardNo.length();
        if (ilen >= 4)
        	tempLast4digits = mCreditCardNo.substring(ilen - 4, ilen);
        else
        	tempLast4digits = mCreditCardNo;

        final String last4Digits = tempLast4digits;
        
        
        /*
		 * below condition is added to check whether the card six digits entered on first screen matching 
		 * with the card swiped or not. if match then it will allow to complete the transaction.
		 * 
		 */
		
		if(ilen >= 6){
			String cardFirstSixDigit = mCreditCardNo.substring(0, 6);
			if(!mTxtCardFirstSixDigit.getText().toString().equalsIgnoreCase(cardFirstSixDigit)){
				Constants.showDialog(EmiSaleView.this, CARDSALE_DIALOG_MSG, "invalid card, card first six digits not matched.", 1);
				
				mViewFlipper.showPrevious();	         	
				arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleyellow);
				mCurrentScreen --;
				arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleblack);
				
				if (isTrxEmv){
					wisePadController.sendOnlineProcessResult(null);
				}
				
				resetSwiperRoutines();
				return;
			}
		}
        
        if (!isTrxEmv) {
 

                ((TextView) findViewById(R.id.creditsale_LBL_CardHolderName)).setText("  " + mCardSaleData.mCardHolderName);
                String tempString1 = "";
                int ilen1 = mCreditCardNo.length();
                if (ilen1 >= 4)
                    tempString1 = mCreditCardNo.substring(ilen1 - 4, ilen1);
                else
                    tempString1 = mCreditCardNo;

                ((TextView) findViewById(R.id.creditsale_LBL_CardNo)).setText("  " + "**** **** **** " + tempString1);
                ((TextView) findViewById(R.id.creditsale_LBL_ExpiryDate)).setText("  " + mCardSaleData.mExpiryDate);

                ((TextView) findViewById(R.id.creditsale_LBL_Amtprefix)).setText("  " + ApplicationData.Currency_Code);
                //((TextView) findViewById(R.id.creditsale_LBL_AmtRs)).setText(mTxtCreditAmount.getText().toString());
                ((TextView) findViewById(R.id.creditsale_LBL_AmtRs)).setText(mTxtCreditAmount.getText().toString());
                

                mViewFlipper.showNext();
                arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleyellow);
                mCurrentScreen++;
                arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleblack);

            

        } else {

            ((TextView) findViewById(R.id.creditsale_LBL_CardHolderName)).setText("  " + mCardSaleData.mCardHolderName);
            ((TextView) findViewById(R.id.creditsale_LBL_CardNo)).setText("  " + "**** **** **** " + last4Digits);
            ((TextView) findViewById(R.id.creditsale_LBL_ExpiryDate)).setText("  " + "xx/xx");
            ((TextView) findViewById(R.id.creditsale_LBL_Amtprefix)).setText("  " + ApplicationData.Currency_Code);
            //((TextView) findViewById(R.id.creditsale_LBL_AmtRs)).setText(mTxtCreditAmount.getText().toString());
            ((TextView) findViewById(R.id.creditsale_LBL_AmtRs)).setText(mTxtCreditAmount.getText().toString());

            mViewFlipper.showNext();
            arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleyellow);
            mCurrentScreen++;
            arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleblack);


        }

    }

    // When the Intention has been fulfilled Android will notify us through this
    // method
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String mCreditCardNo = mCardSaleData.mCreditCardNo;
    	String last4Digits = "";
    	
        int ilen = mCreditCardNo.length();
        if (ilen >= 4)
        	last4Digits = mCreditCardNo.substring(ilen - 4, ilen);
        else
        	last4Digits = mCreditCardNo;

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    int action = data.getIntExtra("action", 0);
                    mCardSaleData.mAmexSecurityCode = data.getStringExtra("securitycode");
                    if (action == 0) // when back in pressed on amex screen action is set to 0
                    {
                        mViewFlipper.showPrevious();
                        arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleyellow);
                        mCurrentScreen = 0;
                        arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleblack);

                    } else if (action == 2) { //on pressing next on amex screen action is set to 2

                        mViewFlipper.showNext();
                        arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleyellow);
                        mCurrentScreen++;
                        arrPageLinearObjects[mCurrentScreen].setBackgroundResource(R.drawable.circleblack);

                        ((TextView) findViewById(R.id.creditsale_LBL_CardHolderName)).setText("  " + mCardSaleData.mCardHolderName);
                        ((TextView) findViewById(R.id.creditsale_LBL_CardNo)).setText("  " + "**** **** **** " + last4Digits);
                        ((TextView) findViewById(R.id.creditsale_LBL_ExpiryDate)).setText("  " + "xx/xx");
                        ((TextView) findViewById(R.id.creditsale_LBL_Amtprefix)).setText("  " + ApplicationData.Currency_Code);
                        //((TextView) findViewById(R.id.creditsale_LBL_AmtRs)).setText(mTxtCreditAmount.getText().toString());
                        ((TextView) findViewById(R.id.creditsale_LBL_AmtRs)).setText(mTxtCreditAmount.getText().toString());
                    }
                    break;
                }
        }
    }


  
    public void doneWithCreditSale() {
        if (!onDoneWithCreditSaleCalled) {
            creditSaleViewDestory();
            // the finish is been called in the Task,
            try {
                wisePadController.stopBTv2();
            } catch (Exception ex) {
            }

            mEMVProcessProgressActivity = null;
            mEMVProcessProgressActivity = new CustomProgressDialog(EmiSaleView.this, "Please wait...");
            mEMVProcessProgressActivity.show();
            //in case if the task is still running cancel it and not execute any thing which are based on this data.
            mstrEMVProcessTaskType = "";

            if (mEMVProcessTask != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
                mEMVProcessTask.cancel(true);

            mstrEMVProcessTaskType = "stopbluetooth";
            mEMVProcessTask = new EMVProcessTask(); //every time create new object, as AsynTask will only be executed one time.
            mEMVProcessTask.execute();

            onDoneWithCreditSaleCalled = true;
        }

    }

    public void creditSaleViewDestory() {

        dismissDialog();
        // this will be called in the EMVOnlineThreadTask, since we are showing the progress bar in the thread and once the wisepad devcie stops
        //this will be any way dismissed
        dismissEMVOnlieProcessProgressActivity();
 


    }

    public boolean emailcheck(String str) {
       
        boolean results = Constants.isValidEmail(str);
        
        if (results == false) {
            Constants.showDialog(EmiSaleView.this, CARDSALE_DIALOG_MSG, Constants.CARDSALE_ERROR_email, 1);

            return false;
        } else {
            return true;
        }
    }

    public String removeChar(String s, char c) {

        String r = "";

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != c) r += s.charAt(i);
        }

        return r;
    }



    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public void dismissEMVOnlieProcessProgressActivity() {
        if (mEMVProcessProgressActivity != null) {
            mEMVProcessProgressActivity.dismiss();
            mEMVProcessProgressActivity = null;
        }
    }
    
    
    class WisePadGatewayConncetionListener implements  MswipeWisePadGatewayConnectionListener{

		@Override
		public void Connected(String msg) {
			
			((TextView)findViewById(R.id.topbar_LBL_status)).setText(msg);
			
		}

		@Override
		public void Connecting(String msg) {
			
			((TextView)findViewById(R.id.topbar_LBL_status)).setText(msg);
			
		}

		@Override
		public void disConnect(String msg) {
			
			((TextView)findViewById(R.id.topbar_LBL_status)).setText(msg);
			
		}
		
    }
    
    
    class WisePadSwiperListener implements MswipeWisePadDeviceListener {

        @Override
        public void onRequestInsertCard() {
            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v("Example", "onRequestInsertCard ", true, true);
            // if the card is already insert then it wont throw up the cardindertor swiper method
            arrTxtSwiperMsgs[0].setTextColor(Color.rgb(176, 172, 174)); //pairing the device
            arrTxtSwiperMsgs[1].setTextColor(Color.rgb(176, 172, 174)); //press start
            arrTxtSwiperMsgs[2].setTextColor(Color.rgb(176, 172, 174));//initializing the swiper
            arrTxtSwiperMsgs[3].setTextColor(Color.rgb(0, 0, 0));

            txtProgMsg.setTextColor(Color.BLUE);
            txtProgMsg.setText(EmiSaleView.this.getString(R.string.waiting_for_card));

        }

        @Override
        public void onWaitingForCard() {
            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v("Example", "onWaitingForCard ", true, true);
            // if the card is already insert then it wont throw up the cardindertor swiper method
            arrTxtSwiperMsgs[0].setTextColor(Color.rgb(176, 172, 174)); //pairing the device
            arrTxtSwiperMsgs[1].setTextColor(Color.rgb(176, 172, 174)); //press start
            arrTxtSwiperMsgs[2].setTextColor(Color.rgb(176, 172, 174));//initializing the swiper
            arrTxtSwiperMsgs[3].setTextColor(Color.rgb(0, 0, 0));

            txtProgMsg.setTextColor(Color.BLUE);
            txtProgMsg.setText(EmiSaleView.this.getString(R.string.waiting_for_card));

        }


        @Override
        public void onReturnCheckCardResult(CheckCardResult checkCardResult, Hashtable<String, String> decodeData) {
            dismissDialog();
            
            //reset all the data, since this value would be used for both
            //ic an mag card
            mCardSaleData = new CardSaleData();

            
            
            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onReturnCheckCardResult The chechCardResult is " + checkCardResult, true, true);

            if (checkCardResult == CheckCardResult.NONE) {
                //statusEditText.setText(CreditSaleView.this.getString(R.string.no_card_detected));
                txtProgMsg.setTextColor(Color.RED);
                txtProgMsg.setText(EmiSaleView.this.getString(R.string.no_card_detected));
                ignoreBackDevicekeyOnCardProcess = false;

            } else if (checkCardResult == CheckCardResult.ICC) {
                //statusEditText.setText(CreditSaleView.this.getString(R.string.icc_card_inserted));

                isTrxEmv = true;
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("IC card inserted, please wait...");

                
                mBtnSwipe.setEnabled(false);
                mBtnSwipe.setBackgroundResource(R.drawable.roundrectgrey);
                wisePadController.startEmv();

            } else if (checkCardResult == CheckCardResult.NOT_ICC) {
                txtProgMsg.setTextColor(Color.RED);
                txtProgMsg.setText(EmiSaleView.this.getString(R.string.card_inserted));
                ignoreBackDevicekeyOnCardProcess = false;

            } else if (checkCardResult == CheckCardResult.BAD_SWIPE) {
                //statusEditText.setText(CreditSaleView.this.getString(R.string.bad_swipe));
                txtProgMsg.setTextColor(Color.RED);
                txtProgMsg.setText(EmiSaleView.this.getString(R.string.bad_swipe));
                ignoreBackDevicekeyOnCardProcess = false;


            } else if (checkCardResult == CheckCardResult.MCR) {
                    mBtnSwipe.setEnabled(false);
                    mBtnSwipe.setBackgroundResource(R.drawable.roundrectgrey);
                    isTrxEmv = false;
                    processMagCardData(checkCardResult, decodeData);
            } else if (checkCardResult == CheckCardResult.SERVICECODE_FAIL_USE_CHIPCARD) {
            	  mBtnSwipe.setEnabled(true);
                  mBtnSwipe.setBackgroundResource(R.drawable.roundrectblue);
                  txtProgMsg.setTextColor(Color.RED);
                  txtProgMsg.setText("Please use chip card");
                 
                    
            } else if (checkCardResult == CheckCardResult.MCR_AMEXCARD) {
            	isTrxEmv = false;
            	mBtnSwipe.setEnabled(true);
                mBtnSwipe.setBackgroundResource(R.drawable.roundrectblue);
                
                
                final CheckCardResult finalcheckCardResult = checkCardResult; 
                final Hashtable<String, String> finaldecodeData = decodeData;
            	final Dialog dlgAmexPin = Constants.showDialogPin(EmiSaleView.this, "Amex Card", "Pin required, please select the option for the Pin entry?", "2", "Ok", "Cancel", "");
                Button accept = (Button) dlgAmexPin.findViewById(R.id.bmessageDialogAccept);
                accept.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {

                        EditText pin = (EditText) dlgAmexPin.findViewById(R.id.tvmessagedialogtext_TXT_pin);
                        if (pin.getText().length() != 4) {

                            final Dialog dlgamex = Constants.showDialog(EmiSaleView.this, "Amex Card", "Invalid pin input, should be 4 digits in length.", "1");
                                                       Button yes = (Button) dlgamex.findViewById(R.id.bmessageDialogYes);
                                                       yes.setOnClickListener(new OnClickListener() {

                                                           public void onClick(View v) {
                                                               dlgamex.dismiss();

                                                           }
                                                       });
                            dlgamex.show();

                        } else {
                            dlgAmexPin.dismiss();
                            mCardSaleData.mAmexSecurityCode = pin.getText().toString();
                            isTrxEmv = false;
                            processMagCardData(finalcheckCardResult, finaldecodeData);
                            
                        }
                    }
                });

                Button bypass = (Button) dlgAmexPin.findViewById(R.id.bmessageDialogBypass);
                bypass.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        dlgAmexPin.dismiss();
                        

                    }
                });

                dlgAmexPin.show();
            	
            	
            } else if (checkCardResult == CheckCardResult.NO_RESPONSE) {
                //statusEditText.setText(CreditSaleView.this.getString(R.string.card_no_response));
                txtProgMsg.setTextColor(Color.RED);
                txtProgMsg.setText(EmiSaleView.this.getString(R.string.card_no_response));
                ignoreBackDevicekeyOnCardProcess = false;


            } else if (checkCardResult == CheckCardResult.TRACK2_ONLY) {
                
            	isTrxEmv = false;
            	processMagCardData(checkCardResult, decodeData);
                
            }
        }

        public void processMagCardData(CheckCardResult checkCardResult, Hashtable<String, String> decodeData) {

            if (applicationData.IS_DEBUGGING_ON) {
                StringBuffer cardDetails = new StringBuffer();
                cardDetails.append("Card Swiped:\n");

                //cardDetails.append("Format id : " + decodeData.get("formatID") + "\n");
                cardDetails.append("MaskedPAN: " + decodeData.get("maskedPAN") + "\n");
                cardDetails.append("ExpiryDate : " + decodeData.get("expiryDate") + "\n");
                cardDetails.append("CardHolderName : " + decodeData.get("cardholderName") + "\n");
                cardDetails.append("ksn : " + decodeData.get("ksn") + "\n");
                //cardDetails.append("ServiceCode : " + decodeData.get("serviceCode") + "\n");
                //cardDetails.append("PartialTrack : " + decodeData.get("partialTrack") + "\n");


                if (ApplicationData.IS_DEBUGGING_ON)
                    Logs.v(getPackageName(), log_tab + "onReturnCheckCardResult  " + cardDetails.toString(), true, true);
            }

            mCardSaleData.mCreditCardNo = decodeData.get("maskedPAN") == null ? "" : decodeData.get("maskedPAN");
            mCardSaleData.mCardHolderName = decodeData.get("cardholderName") == null ? "" : decodeData.get("cardholderName");
            mCardSaleData.mExpiryDate = decodeData.get("expiryDate") == null ? "" : decodeData.get("expiryDate");
            
           showCreditDetailsScreen();

        }


        @Override
        public void onReturnStartEmvResult(StartEmvResult startEmvResult, String ksn) {
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onReturnStartEmvResult the ksn value is " + ksn, true, true);

            if (startEmvResult == StartEmvResult.SUCCESS) {
                //statusEditText.setText(CreditSaleView.this.getString(R.string.start_emv_success));
            } else {
                //statusEditText.setText(CreditSaleView.this.getString(R.string.start_emv_fail));
                String msg = "";
                msg = (EmiSaleView.this.getString(R.string.start_emv_fail));

                txtProgMsg.setTextColor(Color.RED);
                txtProgMsg.setText("Error: " + msg);
                //mBtnSwipe.setVisibility(View.VISIBLE);
                //mBtnSwipeOk.setVisibility(View.INVISIBLE);


            }
        }


        @Override
        public void onRequestSelectApplication(ArrayList<String> appList) {
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onRequestSelectApplication " + appList.size(), true, true);

            dismissDialog();
            dismissEMVOnlieProcessProgressActivity();
            if (appList.size() == 1) {
                wisePadController.selectApplication(0);
                mEMVProcessProgressActivity = null;
                mEMVProcessProgressActivity = new CustomProgressDialog(EmiSaleView.this, "Processing Chip card...");
                mEMVProcessProgressActivity.show();

                //in case if the task is still running cancel it and not execute any thing which are based on this data.
                /*mstrEMVProcessTaskType = "";

                if (mEMVProcessTask  != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
                    mEMVProcessTask.cancel(true);
                 mstrEMVProcessTaskType = "onlinesubmit";

                mEMVProcessTask = new EMVProcessTask(); //every time create new object, as AsynTask will only be executed one time.
                mEMVProcessTask.execute();*/

            } else {
                final Dialog dialogApp = Constants.shwoAppCustionDialog(EmiSaleView.this, "Please select app");

                String[] appNameList = new String[appList.size()];
                for (int i = 0; i < appNameList.length; ++i) {
                    appNameList[i] = appList.get(i);
                    if (applicationData.IS_DEBUGGING_ON)
                        Logs.v(getPackageName(), log_tab + "App name " + appNameList[i], true, true);
                }

                ListView appListView = (ListView) dialogApp.findViewById(R.id.creditsale_LST_applications);
                appListView.setAdapter(new AppViewAdapter(EmiSaleView.this, appList));
                appListView.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        wisePadController.selectApplication(position);
                        mEMVProcessProgressActivity = null;
                        mEMVProcessProgressActivity = new CustomProgressDialog(EmiSaleView.this, "Processing Chip card...");
                        mEMVProcessProgressActivity.show();

                        //in case if the task is still running cancel it and not execute any thing which are based on this data.
                        /*mstrEMVProcessTaskType = "";

                        if (mEMVProcessTask  != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
                            mEMVProcessTask.cancel(true);
                        mstrEMVProcessTaskType = "onlinesubmit";

                        mEMVProcessTask = new EMVProcessTask(); //every time create new object, as AsynTask will only be executed one time.
                        mEMVProcessTask.execute();*/

                    }

                });

                dialogApp.findViewById(R.id.bmessageDialogNo).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        wisePadController.cancelSelectApplication();
                        dialogApp.dismiss();
                    }
                });
                dialogApp.show();
            }

        }


        @Override
        public void onRequestSetAmount() {
            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onRequestSetAmount ", true, true);
            dismissDialog();
            txtProgMsg.setTextColor(Color.BLUE);
            txtProgMsg.setText(R.string.please_confirm_amount);
            wisePadController.setAmount(mTxtCreditAmount.getText().toString(),"586" );
            
        }

        @Override
        public void onReturnAmountConfirmResult(boolean isSuccess) {
            // TODO Auto-generated method stub
            if (isSuccess) {
                //statusEditText.setText(getString(R.string.amount_confirmed));
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText(R.string.amount_confirmed);
            } else {
                //statusEditText.setText(getString(R.string.amount_canceled));
                txtProgMsg.setTextColor(Color.RED);
                txtProgMsg.setText(R.string.amount_canceled);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Please use Chip card.");
                ignoreBackDevicekeyOnCardProcess = false;
                mBtnSwipe.setEnabled(true);
                mBtnSwipe.setBackgroundResource(R.drawable.roundrectblue);

            }
            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onReturnAmountConfirmResult " + isSuccess, true, true);
            dismissDialog();

        }

        @Override
        public void onRequestPinEntry(PinEntry pinentry) {
            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onRequestPinEntry ", true, true);
            dismissEMVOnlieProcessProgressActivity();
            txtProgMsg.setTextColor(Color.BLUE);

            if (pinentry== PinEntry.PIN_ENTRY_MAGCARD) {
                txtProgMsg.setText("Please enter PIN on WisePad or Press ENTER (green key) to bypass PIN.");

            } else {
                txtProgMsg.setText(R.string.enter_pin);
            }


        }

        @Override
        public void onReturnPinEntryResult(PinEntryResult pinEntryResult, String epb, String ksn) {
            dismissDialog();
            if (ApplicationData.IS_DEBUGGING_ON) {
                Logs.v(getPackageName(), log_tab + "onReturnPinEntryResult ", true, true);
            }
            
            Constants.isPinVerfied = false;
            
            if (pinEntryResult == PinEntryResult.ENTERED) {
                //statusEditText.setText(getString(R.string.pin_entered));
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText(R.string.pin_entered);
                
                Constants.isPinVerfied = true;
                
            } else if (pinEntryResult == PinEntryResult.BYPASS) {
                //statusEditText.setText(getString(R.string.pin_canceled));
                txtProgMsg.setTextColor(Color.RED);
                txtProgMsg.setText(R.string.pin_bypassed);
            } else if (pinEntryResult == PinEntryResult.CANCEL) {
                //statusEditText.setText(getString(R.string.pin_canceled));
                txtProgMsg.setTextColor(Color.RED);
                txtProgMsg.setText(R.string.pin_canceled);
            } else if (pinEntryResult == PinEntryResult.TIMEOUT) {
                //statusEditText.setText(getString(R.string.pin_timeout));
                txtProgMsg.setTextColor(Color.RED);
                txtProgMsg.setText(R.string.pin_timeout);
            } else if (pinEntryResult == PinEntryResult.KEY_ERROR) {
                txtProgMsg.setTextColor(Color.RED);
                txtProgMsg.setText(R.string.key_error);
            } else if (pinEntryResult == PinEntryResult.NO_PIN) {
                txtProgMsg.setTextColor(Color.RED);
                txtProgMsg.setText(R.string.no_pin);
            }
            
        }

        
        @Override
        public void onRequestTerminalTime() {
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onRequestTerminalTime ", true, true);
            dismissDialog();
        }
        
        @Override
        public void onRequestCheckServerConnectivity() {
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onRequestCheckServerConnectivity ", true, true);
            dismissDialog();

        }

        @Override
        public void onRequestFinalConfirm() {
            dismissDialog();
            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onRequestFinalConfirm  ", true, true);

            

        }
        
        @Override
        public void onRequestOnlineProcess(HashMap<String, String> tlv) {
            dismissDialog();
            dismissEMVOnlieProcessProgressActivity();
            
            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onRequestOnlineProcess the tlv string is " + tlv, true, true);
            
            mBtnSwipe.setEnabled(true);
            mBtnSwipe.setBackgroundResource(R.drawable.roundrectblue);

            mCardSaleData.mCardHolderName = tlv.get("CardHolderName");
            mCardSaleData.mAppIdentifier = tlv.get("AppIdentifier");;
            mCardSaleData.mCertif = tlv.get("Certif");;
            mCardSaleData.mApplicationName = tlv.get("ApplicationName");
            
            mCardSaleData.mTVR = tlv.get("TVR");
            mCardSaleData.mTSI = tlv.get("TSI");

            mCardSaleData.mCreditCardNo = tlv.get("CreditCardNo");
            String tempCreditNo = "";
            int ilen = mCardSaleData.mCreditCardNo.length();
            // compare the last four digits inputted by the user with the swiper returned details
            mCardSaleData.mExpiryDate = tlv.get("ExpiryDate");
            showCreditDetailsScreen();

        }
        
        @Override
        public void onRequestReferProcess(String pan) {
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onRequestReferProcess the pan is " + pan, true, true);

            dismissDialog();
            
        }

        @Override
        public void onRequestAdviceProcess(String tlv) {
            dismissDialog();
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onRequestAdviceProcess the tlv is " + tlv, true, true);

        }

        
        @Override
        public void onReturnReversalData(String tlv) {
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onReturnReversalData the tlv string is " + tlv, true, true);
            dismissDialog();
            
        }
        

        @Override
        public void onReturnBatchData(HashMap<String, String> tlv) {
            
        	mCardSaleData.mCertif = tlv.get("Certif");;
        	mCardSaleData.mTVR = tlv.get("TVR");;
        	mCardSaleData.mTSI = tlv.get("TSI");;
			
        	ignoreBackDevicekeyOnCardProcess = false;
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onReturnBatchData the tlv string is " + tlv, true, true);
            
            dismissDialog();
            mBtnSwipe.setEnabled(true);
            mBtnSwipe.setBackgroundResource(R.drawable.roundrectblue);

            mBtnSubmitCardDetails.setEnabled(true);
            mBtnSubmitCardDetails.setBackgroundResource(R.drawable.roundrectblue);

            

        }
        @Override
        public void onReturnTransactionResult(TransactionResult transactionResult, String offlineDeclineTag) {
            dismissDialog();
            dismissEMVOnlieProcessProgressActivity();
            

            String msg = "";
            txtProgMsg.setTextColor(Color.RED);
            mBtnSwipe.setEnabled(true);
            mBtnSwipe.setBackgroundResource(R.drawable.roundrectblue);


            ignoreBackDevicekeyOnCardProcess = false;
            mBtnSubmitCardDetails.setEnabled(true);
            mBtnSubmitCardDetails.setBackgroundResource(R.drawable.roundrectblue);

            //when the back tab is pressed

            if (mstrEMVProcessTaskType.equalsIgnoreCase("backbutton") || mstrEMVProcessTaskType.equalsIgnoreCase("onlinesubmit")) {
                if (mEMVProcessTask != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
                    mEMVProcessTask.cancel(true);
            }
            if (transactionResult == TransactionResult.APPROVED) {

                ignoreSendOnlineProcessTransactionResult = false;

                CardSaleDialog dlgTrxResults = new CardSaleDialog(EmiSaleView.this, null, CARDSALE_DIALOG_MSG, "Approved",
                		mCardSaleData.mAuthCode, mCardSaleData.mRRNo, true);
                dlgTrxResults.setOnDismissListener(new OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {

                        final Dialog dlgRemoveCard = Constants.showDialog(EmiSaleView.this, CARDSALE_DIALOG_MSG, "Please remove the card", "1");
                        Button yes = (Button) dlgRemoveCard.findViewById(R.id.bmessageDialogYes);
                        yes.setOnClickListener(new OnClickListener() {

                            public void onClick(View v) {
                                showSignature();
                                

                            }
                        });
                        dlgRemoveCard.show();

                    }
                });
                dlgTrxResults.show();
            }else if(transactionResult == TransactionResult.AUTO_REVERSAL_TRX){
				
				//messageTextView.setText("Auto reversal void.");
            		final String finofflineDeclineTag = offlineDeclineTag;
                
                    final Dialog dlgProcessReversal = Constants.showDialog(EmiSaleView.this, CARDSALE_DIALOG_MSG, "Approved online, card declined transaction, press ok for AutoReversal", "1");
                    Button yes = (Button) dlgProcessReversal.findViewById(R.id.bmessageDialogYes);
                    yes.setOnClickListener(new OnClickListener() {

                        public void onClick(View v) {

                        	dlgProcessReversal.dismiss();
                        	processReversalSale(finofflineDeclineTag);
                            

                        }
                    });
                    dlgProcessReversal.show();
            	
            } else if (transactionResult == TransactionResult.TERMINATED) {
                //messageTextView.setText(CreditSaleView.this.getString(R.string.transaction_terminated));
                txtProgMsg.setText(EmiSaleView.this.getString(R.string.transaction_terminated));

            } else if (transactionResult == TransactionResult.DECLINED) {
                txtProgMsg.setText(EmiSaleView.this.getString(R.string.transaction_declined));
                //messageTextView.setText(CreditSaleView.this.getString(R.string.transaction_declined));

            } else if (transactionResult == TransactionResult.CANCEL) {
                //messageTextView.setText(CreditSaleView.this.getString(R.string.transaction_cancel));
                txtProgMsg.setText(EmiSaleView.this.getString(R.string.transaction_cancel));

            } else if (transactionResult == TransactionResult.CAPK_FAIL) {
                //messageTextView.setText(CreditSaleView.this.getString(R.string.transaction_capk_fail));
                txtProgMsg.setText(EmiSaleView.this.getString(R.string.transaction_capk_fail));

            } else if (transactionResult == TransactionResult.NOT_ICC) {
                txtProgMsg.setText(EmiSaleView.this.getString(R.string.transaction_not_icc));
                //messageTextView.setText(CreditSaleView.this.getString(R.string.transaction_not_icc));

            } else if (transactionResult == TransactionResult.SELECT_APP_FAIL) {
                txtProgMsg.setText(EmiSaleView.this.getString(R.string.transaction_app_fail));
                //messageTextView.setText(CreditSaleView.this.getString(R.string.transaction_app_fail));

            } else if (transactionResult == TransactionResult.DEVICE_ERROR) {
                //messageTextView.setText(CreditSaleView.this.getString(R.string.transaction_device_error));
                txtProgMsg.setText(EmiSaleView.this.getString(R.string.transaction_device_error));
            } else if(transactionResult == TransactionResult.APPLICATION_BLOCKED) {
            	txtProgMsg.setText(EmiSaleView.this.getString(R.string.transaction_application_blocked));
			} else if(transactionResult == TransactionResult.ICC_CARD_REMOVED) {
				 txtProgMsg.setText(EmiSaleView.this.getString(R.string.transaction_icc_card_removed));
			}
            msg = txtProgMsg.getText().toString();
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onReturnTransactionResult msg => " + msg, true, true);
                

        }

        

        @Override
        public void onReturnTransactionLog(String tlv) {
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onReturnTransactionLog the tlv string is " + tlv, true, true);

            dismissDialog();

        }


        
        @Override
        public void onReturnDeviceInfo(Hashtable<String, String> arg0) {
            // TODO Auto-generated method stub
            dismissDialog();
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onReturnDeviceInfo ", true, true);

        }


        @Override
        public void onRequestDisplayText(DisplayText displayText) {
            dismissDialog();
            String msg = "";
            txtProgMsg.setTextColor(Color.RED);
            if (displayText == DisplayText.AMOUNT_OK_OR_NOT) {
                msg = EmiSaleView.this.getString(R.string.amount_ok);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Status:" + msg);

            } else if (displayText == DisplayText.APPROVED) {
                msg = EmiSaleView.this.getString(R.string.approved);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Status:" + msg);

            } else if (displayText == DisplayText.CALL_YOUR_BANK) {
                msg = EmiSaleView.this.getString(R.string.call_your_bank);
                txtProgMsg.setText("Error:" + msg);

            } else if (displayText == DisplayText.CANCEL_OR_ENTER) {
                msg = EmiSaleView.this.getString(R.string.cancel_or_enter);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Status:" + msg);

            } else if (displayText == DisplayText.CARD_ERROR) {
                msg = EmiSaleView.this.getString(R.string.card_error);
                txtProgMsg.setText("Error:" + msg);

            } else if (displayText == DisplayText.DECLINED) {
                msg = EmiSaleView.this.getString(R.string.decline);

                txtProgMsg.setText("Error:" + msg);
            } else if (displayText == DisplayText.ENTER_PIN_BYPASS) {
            	msg = "Please enter PIN on WisePad or Press ENTER (green key) to bypass PIN.";

                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Status:" + msg);

            } else if (displayText == DisplayText.ENTER_PIN) {
                    msg = EmiSaleView.this.getString(R.string.enter_pin);
               
            } else if (displayText == DisplayText.INCORRECT_PIN) {
                msg = EmiSaleView.this.getString(R.string.incorrect_pin);
                txtProgMsg.setText("Status:" + msg);

            } else if (displayText == DisplayText.INSERT_CARD) {
                msg = EmiSaleView.this.getString(R.string.insert_card);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Status:" + msg);

            } else if (displayText == DisplayText.NOT_ACCEPTED) {
                msg = EmiSaleView.this.getString(R.string.not_accepted);
                txtProgMsg.setText("Error:" + msg);

            } else if (displayText == DisplayText.PIN_OK) {
                msg = EmiSaleView.this.getString(R.string.pin_ok);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Status:" + msg);

            } else if (displayText == DisplayText.PLEASE_WAIT) {
                msg = EmiSaleView.this.getString(R.string.wait);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Status:" + msg);

            } else if (displayText == DisplayText.PROCESSING_ERROR) {
                msg = EmiSaleView.this.getString(R.string.processing_error);
                txtProgMsg.setText("Error:" + msg);

            } else if (displayText == DisplayText.REMOVE_CARD) {
                msg = EmiSaleView.this.getString(R.string.remove_card);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Status:" + msg);

            } else if (displayText == DisplayText.USE_CHIP_READER) {
                msg = EmiSaleView.this.getString(R.string.use_chip_reader);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Error: " + msg);

            } else if (displayText == DisplayText.USE_MAG_STRIPE) {
                msg = EmiSaleView.this.getString(R.string.use_mag_stripe);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Status:" + msg);

            } else if (displayText == DisplayText.TRY_AGAIN) {
                msg = EmiSaleView.this.getString(R.string.try_again);
                txtProgMsg.setText("Error:" + msg);

            } else if (displayText == DisplayText.REFER_TO_YOUR_PAYMENT_DEVICE) {
                msg = EmiSaleView.this.getString(R.string.refer_payment_device);

                txtProgMsg.setText("Error:" + msg);

            } else if (displayText == DisplayText.TRANSACTION_TERMINATED) {
                msg = EmiSaleView.this.getString(R.string.transaction_terminated);
                txtProgMsg.setText("Error:" + msg);

            } else if (displayText == DisplayText.TRY_ANOTHER_INTERFACE) {
                msg = EmiSaleView.this.getString(R.string.try_another_interface);
                txtProgMsg.setText("Error:" + msg);

            } else if (displayText == DisplayText.ONLINE_REQUIRED) {
                msg = EmiSaleView.this.getString(R.string.online_required);
                txtProgMsg.setText("Error:" + msg);

            } else if (displayText == DisplayText.PROCESSING) {
                msg = EmiSaleView.this.getString(R.string.processing);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Status:" + msg);

            } else if (displayText == DisplayText.WELCOME) {
                msg = EmiSaleView.this.getString(R.string.welcome);
            } else if (displayText == DisplayText.PRESENT_ONLY_ONE_CARD) {
                msg = EmiSaleView.this.getString(R.string.present_one_card);
                txtProgMsg.setText("Error: " + msg);
            } else if (displayText == DisplayText.CAPK_LOADING_FAILED) {
                msg = EmiSaleView.this.getString(R.string.capk_failed);
                txtProgMsg.setText("Error: " + msg);
            } else if (displayText == DisplayText.LAST_PIN_TRY) {
                msg = EmiSaleView.this.getString(R.string.last_pin_try);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Status:" + msg);
            }else if(displayText == DisplayText.SELECT_ACCOUNT) {
				msg = getString(R.string.select_account);
                txtProgMsg.setText("Error: " + msg);
			}
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onRequestDisplayText the display Text is " + msg, true, true);

            //statusEditText.setText(msg);
        }

        @Override
        public void onRequestClearDisplay() {
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onRequestClearDisplay ", true, true);
            dismissDialog();
            //statusEditText.setText("");
        }

        


        @Override
        public void onBatteryLow(BatteryStatus batteryStatus) {
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onBatteryLow  ", true, true);

            if (batteryStatus == BatteryStatus.LOW) {
                //statusEditText.setText(CreditSaleView.this.getString(R.string.battery_low));
                txtProgMsg.setTextColor(Color.RED);
                txtProgMsg.setText("Status: " + EmiSaleView.this.getString(R.string.battery_low));

            } else if (batteryStatus == BatteryStatus.CRITICALLY_LOW) {
                //statusEditText.setText(CreditSaleView.this.getString(R.string.battery_critically_low));
                txtProgMsg.setTextColor(Color.RED);
                txtProgMsg.setText("Status: " + EmiSaleView.this.getString(R.string.battery_critically_low));
            }
        }


        @Override
        public void onBTv2Detected() {
            //statusEditText.setText(getString(R.string.bluetooth_detected));
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onBluetoothDetected", true, true);

            txtProgMsg.setTextColor(Color.BLUE);
            txtProgMsg.setText("Device detected.");
            
            /*arrTxtSwiperMsgs[0].setTextColor(Color.rgb(0, 0, 0)); //pairing the device
            arrTxtSwiperMsgs[0].setText("Connected to mSwiper-pad device");
            arrTxtSwiperMsgs[1].setTextColor(Color.rgb(176, 172, 174)); //press start
            arrTxtSwiperMsgs[2].setTextColor(Color.rgb(176, 172, 174));//initializing the swiper
            arrTxtSwiperMsgs[3].setTextColor(Color.rgb(176, 172, 174));
            */

        }


        @Override
        public void onBTv2Connected(BluetoothDevice bluetoothDevice) {
            try {
                mBlueToothMcId = bluetoothDevice.getAddress();
            } catch (Exception ex) {
            }
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onBTv2Connected", true, true);

            if (mstrEMVProcessTaskType.equalsIgnoreCase("backbutton") || mstrEMVProcessTaskType.equalsIgnoreCase("onlinesubmit")) {
                if (mEMVProcessTask != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
                    mEMVProcessTask.cancel(true);
            }
            dismissDialog();
            mBtnSubmitCardDetails.setEnabled(true);
            mBtnSubmitCardDetails.setBackgroundResource(R.drawable.roundrectblue);

            
            mBtnSwipe.setEnabled(true);
            mBtnSwipe.setBackgroundResource(R.drawable.roundrectblue);

            OnOnlineTransactionApproved = false;


            //statusEditText.setText(g etString(R.string.bluetooth_connected));
            txtProgMsg.setTextColor(Color.BLUE);
            txtProgMsg.setText("Device connected.");
            if (bluetoothConnectionState == DEVCIE_NO_PAIRED_DEVICES) {
                bluetoothConnectionState = DEVICE_NO_PAIRED_DEVICES_CONNECTED;
               
            }

            arrTxtSwiperMsgs[0].setTextColor(Color.rgb(176, 172, 174)); //pairing the device
            arrTxtSwiperMsgs[0].setText("Device connected");
            arrTxtSwiperMsgs[1].setTextColor(Color.rgb(0, 0, 0)); //press start
            arrTxtSwiperMsgs[2].setTextColor(Color.rgb(176, 172, 174));//initializing the swiper
            arrTxtSwiperMsgs[3].setTextColor(Color.rgb(176, 172, 174));

            // we delay the connected process to wait for about a sec so that internally the sdk prepares it self for the communications,
            // and then show up the start command and then
            //wisePadDeviceConnecting = false;

            DeviceConnectedWaitTask deviceConnectedWaitTask = new DeviceConnectedWaitTask(); //every time create new object, as AsynTask will only be executed one time.
            deviceConnectedWaitTask.execute();
        }

        //when trying to start and if the device is not found this will get called
        @Override
        public void onBTv2DeviceNotFound() //same as onNodeviceDected
        {
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onBTv2DeviceNotFound  ", true, true);
            ignoreBackDevicekeyOnCardProcess = false;
            mBtnSubmitCardDetails.setEnabled(true);
            mBtnSubmitCardDetails.setBackgroundResource(R.drawable.roundrectblue);

           
            dismissDialog();
            if (mstrEMVProcessTaskType.equalsIgnoreCase("backbutton") || mstrEMVProcessTaskType.equalsIgnoreCase("onlinesubmit")) {
                if (mEMVProcessTask != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
                    mEMVProcessTask.cancel(true);
            }

            //statusEditText.setText(CreditSaleView.this.getString(R.string.no_device_detected));
            txtProgMsg.setTextColor(Color.RED);
            txtProgMsg.setText("Unable to detect the Wisepad, re-start the device and try reconnecting.");
            mBtnSwipe.setText("Connect");
            //mBtnSwipe.setVisibility(View.VISIBLE);
            //mBtnSwipeOk.setVisibility(View.INVISIBLE);

            mBtnSwipe.setEnabled(true);
            mBtnSwipe.setBackgroundResource(R.drawable.roundrectblue);


            OnOnlineTransactionApproved = false;


            wisePadDeviceConnecting = false;
            arrTxtSwiperMsgs[0].setTextColor(Color.rgb(0, 0, 0)); //pairing the device
            arrTxtSwiperMsgs[0].setText("Device not found");
            arrTxtSwiperMsgs[1].setTextColor(Color.rgb(176, 172, 174)); //press start
            arrTxtSwiperMsgs[2].setTextColor(Color.rgb(176, 172, 174)); //insert the card swiper
            arrTxtSwiperMsgs[3].setTextColor(Color.rgb(176, 172, 174)); //insert the card swiper


        }


        //when the devcie after the connection get diconnected
        @Override
        public void onBTv2Disconnected() //this is equivalent to onDeviceUnPlugged this is bluetooth diconnected.
        {
            dismissDialog();
            mBtnSwipe.setEnabled(true);
            mBtnSwipe.setBackgroundResource(R.drawable.roundrectblue);
            wisePadDeviceConnecting = false;
            ignoreBackDevicekeyOnCardProcess = false;
            mBtnSubmitCardDetails.setEnabled(true);
            mBtnSubmitCardDetails.setBackgroundResource(R.drawable.roundrectblue);

            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onBTv2Disconnected  ", true, true);

            //statusEditText.setText(CreditSaleView.this.getString(R.string.device_unplugged));
            txtProgMsg.setTextColor(Color.RED);
            txtProgMsg.setText("Device disconnected, please ensure the connecting device is  a Wisepad");
            mBtnSwipe.setText("Connect");
            
            //mBtnSwipe.setVisibility(View.VISIBLE);
            //mBtnSwipeOk.setVisibility(View.INVISIBLE);

            if (mstrEMVProcessTaskType.equalsIgnoreCase("backbutton") || mstrEMVProcessTaskType.equalsIgnoreCase("onlinesubmit") ||
                    mstrEMVProcessTaskType.equalsIgnoreCase("stopbluetooth")) {
                if (mEMVProcessTask != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
                    mEMVProcessTask.cancel(true);
            }

            OnOnlineTransactionApproved = false;

        }

        @Override
        public void onError(Error errorState) {
            dismissDialog();
            ignoreBackDevicekeyOnCardProcess = false;
            mBtnSubmitCardDetails.setEnabled(true);
            mBtnSubmitCardDetails.setBackgroundResource(R.drawable.roundrectblue);

            mBtnSwipe.setEnabled(true);
            mBtnSwipe.setBackgroundResource(R.drawable.roundrectblue);

            OnOnlineTransactionApproved = false;

            if (mstrEMVProcessTaskType.equalsIgnoreCase("backbutton") || mstrEMVProcessTaskType.equalsIgnoreCase("onlinesubmit")) {
                if (mEMVProcessTask != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
                    mEMVProcessTask.cancel(true);

            }

            //amountEditText.setText("");
            String msg = "";
            if (errorState == Error.CMD_NOT_AVAILABLE) {
                msg = (EmiSaleView.this.getString(R.string.command_not_available));

            } else if (errorState == Error.TIMEOUT) {
                msg = (EmiSaleView.this.getString(R.string.device_no_response));
            } else if (errorState == Error.DEVICE_RESET) {
                msg = (EmiSaleView.this.getString(R.string.device_reset));
            } else if (errorState == Error.UNKNOWN) {
                msg = (EmiSaleView.this.getString(R.string.unknown_error));
            } else if (errorState == Error.DEVICE_BUSY) {
                msg = (EmiSaleView.this.getString(R.string.device_busy));
            } else if (errorState == Error.INPUT_OUT_OF_RANGE) {
                msg = (EmiSaleView.this.getString(R.string.out_of_range));
            } else if (errorState == Error.INPUT_INVALID_FORMAT) {
                msg = (EmiSaleView.this.getString(R.string.invalid_format));
            } else if (errorState == Error.INPUT_ZERO_VALUES) {
                msg = (EmiSaleView.this.getString(R.string.zero_values));
            } else if (errorState == Error.INPUT_INVALID) {
                msg = (EmiSaleView.this.getString(R.string.input_invalid));
            } else if (errorState == Error.CASHBACK_NOT_SUPPORTED) {
                msg = (EmiSaleView.this.getString(R.string.cashback_not_supported));
            } else if (errorState == Error.CRC_ERROR) {
                msg = (EmiSaleView.this.getString(R.string.crc_error));
            } else if (errorState == Error.COMM_ERROR) {
                msg = (EmiSaleView.this.getString(R.string.comm_error));
            } else if (errorState == Error.FAIL_TO_START_BTV2) {
                msg = (EmiSaleView.this.getString(R.string.fail_to_start_bluetooth_v2));
                resetSwiperRoutines();
                wisePadDeviceConnecting = false;
            } else if (errorState == Error.INVALID_FUNCTION_IN_CURRENT_MODE) {
                msg = (EmiSaleView.this.getString(R.string.invalid_function));
            } else if (errorState == Error.COMM_LINK_UNINITIALIZED) {
                msg = (EmiSaleView.this.getString(R.string.comm_link_uninitialized));
            } else if (errorState == Error.BTV2_ALREADY_STARTED) {
                msg = (EmiSaleView.this.getString(R.string.bluetooth_already_started));
            } else if(errorState == Error.FAIL_TO_START_AUDIO) {
				msg = (EmiSaleView.this.getString(R.string.fail_to_start_audio));
			} else if(errorState == Error.BTV4_ALREADY_STARTED) {
				msg = (EmiSaleView.this.getString(R.string.bluetooth_already_started));
			} else if(errorState == Error.BTV4_NOT_SUPPORTED) {
				msg =(EmiSaleView.this.getString(R.string.bluetooth_4_not_supported));
			}else if(errorState == Error.FAIL_TO_START_BTV4) {
				msg = (EmiSaleView.this.getString(R.string.fail_to_start_bluetooth_v4));
			} 
            txtProgMsg.setTextColor(Color.RED);
            txtProgMsg.setText("Error: " + msg);

            
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onError  the error state is " + errorState, true, true);
            //mBtnSwipe.setVisibility(View.VISIBLE);
            //mBtnSwipeOk.setVisibility(View.INVISIBLE);

        }

		@Override
		public void onAudioDeviceNotFound() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBTv4Connected() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBTv4DeviceListRefresh(List<BluetoothDevice> arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBTv4Disconnected() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBTv4ScanStopped() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBTv4ScanTimeout() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDevicePlugged() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDeviceUnplugged() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPrinterOperationEnd() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRequestPrinterData(int arg0, boolean arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRequestVerifyID(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnAmount(String arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnCancelCheckCardResult(boolean isSuccess) {
			
			if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName , log_tab + "onReturnCancelCheckCardResult", true, true);

			txtProgMsg.setTextColor(Color.RED);
            txtProgMsg.setText("Canceled, Please press start");
            mBtnSwipe.setEnabled(true);		
		}

		@Override
		public void onReturnDisableInputAmountResult(boolean arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnEmvCardDataResult(boolean arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnEnableInputAmountResult(boolean arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnEncryptDataResult(String arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnInjectSessionKeyResult(boolean arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnPhoneNumber(PhoneEntryResult arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnPrinterResult(PrinterResult arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnReadTerminalSettingResult(
				TerminalSettingStatus arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReturnUpdateTerminalSettingResult(
				TerminalSettingStatus arg0) {
			// TODO Auto-generated method stub
			
		}

		

    }

    public void resetSwiperRoutines() {

        if (!wisePadController.isDevicePresent()) {
            arrTxtSwiperMsgs[0].setTextColor(Color.rgb(0, 0, 0)); //press start
            arrTxtSwiperMsgs[0].setText("Device not connected");
            arrTxtSwiperMsgs[1].setTextColor(Color.rgb(176, 172, 174)); //pairing the device
            arrTxtSwiperMsgs[2].setTextColor(Color.rgb(176, 172, 174)); //insert the card swiper
            arrTxtSwiperMsgs[3].setTextColor(Color.rgb(176, 172, 174)); //insert the card swiper
            txtProgMsg.setTextColor(Color.BLUE);
            txtProgMsg.setText("Press connect...");
            mBtnSwipe.setText("Connect");

        } else {
            arrTxtSwiperMsgs[0].setText("Device connected");
            arrTxtSwiperMsgs[0].setTextColor(Color.rgb(176, 172, 174)); //press start
            arrTxtSwiperMsgs[1].setTextColor(Color.rgb(0, 0, 0)); //pairing the device
            arrTxtSwiperMsgs[2].setTextColor(Color.rgb(176, 172, 174)); //insert the card swiper
            arrTxtSwiperMsgs[3].setTextColor(Color.rgb(176, 172, 174)); //insert the card swiper
            txtProgMsg.setTextColor(Color.BLUE);
            txtProgMsg.setText("Press start...");
            mBtnSwipe.setText("Start");
        }

    }

    public class AppViewAdapter extends BaseAdapter {
        ArrayList<String> listData = null;
        Context context;

        public AppViewAdapter(Context context, ArrayList<String> listData) {
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
                convertView = inflater.inflate(R.layout.appcustomlstitem, null);
            }

            TextView txtItem = (TextView) convertView.findViewById(R.id.menuview_lsttext);
            if (listData.get(position) != null)
                txtItem.setText(listData.get(position));
            return convertView;
        }
    }

 
    private class DeviceConnectedWaitTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                int ictr = 0;
                while (ictr < 3) {
                    Thread.sleep(500);
                    ictr++;
                }

            } catch (Exception ex) {
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            wisePadDeviceConnecting = false;
            mBtnSwipe.setText("Start");
        }
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.wisepad, menu);
	    return true;
	}
	  
	public boolean onOptionsItemSelected(MenuItem item) 
	{
        int i = item.getItemId();
        if (i == R.id.loginview_cancelcheckcard) {
            wisePadController.cancelCheckCard();
        }
		return super.onOptionsItemSelected(item);

	}
	
	public class EmiAdapter extends BaseAdapter {
        ArrayList<EmiData> listData = null;
        Context context;

        public EmiAdapter(Context context, ArrayList<EmiData> listData) {
            this.listData = listData;
            this.context = context;
            emiSelectStatus = new boolean[listData.size()];
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.emi_list_row, null);
            }

            TextView txt_month = (TextView) convertView.findViewById(R.id.emi_list_txt_month);
            TextView txt_month_intallment = (TextView) convertView.findViewById(R.id.emi_list_txt_monthly_installment);
            TextView txt_rate = (TextView) convertView.findViewById(R.id.emi_list_txt_rate);
            TextView txt_cashbank_amt = (TextView) convertView.findViewById(R.id.emi_list_txt_cashbackamt);
            TextView txt_cashback_rate = (TextView) convertView.findViewById(R.id.emi_list_txt_cashbackrate);
            final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.emi_list_checkbox);
            
            if(emiSelectStatus[position]){
            	checkBox.setChecked(true);
            }else{
            	checkBox.setChecked(false);
            }
            
			EmiData data = listData.get(position);

            if (listData.get(position) != null){
            	txt_month.setText(data.emiTenure+" months");
            	txt_rate.setText(data.emiRate+" %");
            	txt_month_intallment.setText(Constants.Currency_Code +" "+data.emiAmt);
            	txt_cashback_rate.setText(data.cashBackRate+" %");
            	txt_cashbank_amt.setText(Constants.Currency_Code +" "+ data.cashBackAmt);
            }

            if(emiList.getSelectedItemPosition() == position){
            	txt_month.setSelected(true);

            }else{
            	txt_month.setSelected(false);
            }
            
            convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					checkBox.setChecked(true);
					Arrays.fill(emiSelectStatus, false);
					emiSelectStatus[position] = true;
					emiSelectedItemPostion = position;
					selectedEmiBankCode = listData.get(position).emiBankCode;
					selectedEmiPeriod = listData.get(position).emiTenure;
					notifyDataSetChanged();
				}
			});
            
            
            return convertView;
        }
    }
}


