package com.mswipetech.wisepad.sdktest.view;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.mswipetech.wisepad.sdk.MswipeWisePadDeviceListener;
import com.mswipetech.wisepad.sdk.MswipeWisepadController;
import com.mswipetech.wisepad.R;
import com.mswipetech.wisepad.sdktest.data.AppPrefrences;


public class DeviceInfoView extends BaseTitleActivity  {
    public final static String log_tab = "DeviceInfoView=>";

    ViewFlipper mVFBluetoothSteps = null;
    //in onPause the activity is been closed and when the back key is been pressed on the amount screen this will control the calling of this funcation
    //twice
    boolean onDoneWithCreditSaleCalled = false;

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVERABLE_BT = 0;
    ArrayList<BluetoothDevice> pairedDevicesFound = new ArrayList<BluetoothDevice>();
    String mBlueToothMcId = "";
    int bluetoothConnectionState = 0;
    //bleow will be check by the sdk it
    public final static int DEVCIE_NO_PAIRED_DEVICES = 2;
    public final static int DEVCIE_MULTIPLE_FRIENDLY_DEVICES_FOUND = 3;
    public final static int DEVICE_PAIREDDEVICE_MATCHING = 7;
    public final static int DEVICE_PAIREDDEVICE_MATCHING_MAC = 8;
    public final static int DEVICE_NO_PAIRED_DEVICES_CONNECTED = 9;

    int mcrCardProcessedCtr = 0;
    //in the onResume the device is checked to see if the blue tooth is paired
    //and if the devi
    boolean wisePadDeviceConnecting = false;

    //when on the screen swiper and the card swiper routines are in process then ignore the back key untill any routines that
    //has stopped the card process.
    boolean ignoreBackDevicekeyOnCardProcess = false;

    //when the online process has started, and when a progress bar is show which give the ic card a chance to finish of it 
    //routines, this data represents what taks has involed this progress bar.
    String mstrEMVProcessTaskType = "";
    EMVProcessTask mEMVProcessTask = null;
    CustomProgressDialog mEMVProcessProgressActivity = null;
    //these are use when an onlline is approved and for some reason the card has declined. then show a message in
    //onTransactionREsult message as the screen is not in the swiper screen.

    private MswipeWisepadController wisePadController;
    private WisePadSwiperListener listener;

    private Dialog dialog;
    CustomProgressDialog mProgressActivity = null;

    int mCurrentScreen = 0;

    //for the swiper menu
    TextView lblAmtMsg = null;
    TextView txtProgMsg = null;

    Button mBtnSwipe = null;
    Button mBtnSwipeOk = null;
    Button mBtnSwipeMAG = null;
    TextView arrTxtSwiperMsgs[] = null;

    ViewFlipper mViewFlipper = null;
    ApplicationData applicationData = null;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deviceinfo);
        applicationData = (ApplicationData) getApplicationContext();


        listener = new WisePadSwiperListener();
        wisePadController = new MswipeWisepadController(this, AppPrefrences.getGateWayEnv(),null);
        wisePadController.initMswipeWisePadDevice(listener);
    
        initViews();


    }


    @Override
    public void onStop() {
        if (applicationData.IS_DEBUGGING_ON)
            Logs.v(getPackageName(), log_tab + "On stop called***************************************", true, true);

        //this will be called only when the user presed the back button or incase of any activity takes the view position.
        if (!onDoneWithCreditSaleCalled) {
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "Callind doneWithCredisSale in onStop***************************************", true, true);
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
        if (applicationData.IS_DEBUGGING_ON)
            Logs.v(getPackageName(), log_tab + "On Start called***************************************", true, true);

        if (mCurrentScreen == 0) {
            if (!wisePadController.isDevicePresent()) {
                if (applicationData.IS_DEBUGGING_ON)
                    Logs.v(getPackageName(), log_tab + "Device not present connecting to device in the back ground", true, true);
                arrTxtSwiperMsgs[0].setTextColor(Color.rgb(0, 0, 0)); //press start
                arrTxtSwiperMsgs[0].setText("Device not connected");
                arrTxtSwiperMsgs[1].setTextColor(Color.rgb(176, 172, 174)); //pairing the device
                arrTxtSwiperMsgs[2].setTextColor(Color.rgb(176, 172, 174)); //insert the card swiper
                arrTxtSwiperMsgs[3].setTextColor(Color.rgb(176, 172, 174)); //insert the card swiper
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Press connect");
                mBtnSwipe.setText("Connect");


            } else {
                arrTxtSwiperMsgs[0].setTextColor(Color.rgb(0, 0, 0)); //pairing the device
                arrTxtSwiperMsgs[0].setText("Device connected");
                arrTxtSwiperMsgs[1].setTextColor(Color.rgb(176, 172, 174)); //press start
                arrTxtSwiperMsgs[2].setTextColor(Color.rgb(176, 172, 174)); //insert the card swiper
                arrTxtSwiperMsgs[3].setTextColor(Color.rgb(176, 172, 174)); //insert the card swiper
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Press device info.");
                mBtnSwipe.setText("Device-Info");

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
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + " Switching Bluetooth on ", true, true);
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "Successfully Switched on the Bluetooth", true, true);
            return false;
        } else {
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + " Bluetooth  on ", true, true);
            SharedPreferences preferences;
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String stBluetoothAddress = preferences.getString("bluetoothaddress", ""); //34:C8:03:6D:2B:91

            //When the devcie are not paired then connecting through bluetooth address does not thrown up a pass key and this process will be
            // removed.
            /*BluetoothDevice remoteDevice = null;
            if(stBluetoothAddress.length()>0)
            {
                 if(applicationData.IS_DEBUGGING_ON)
                    Logs.v(getPackageName(),log_tab + "Constructing the Devcie using bluetooth", true, true);
                try
                {
                    remoteDevice =  BluetoothAdapter.getDefaultAdapter().getRemoteDevice(stBluetoothAddress);
                    if(applicationData.IS_DEBUGGING_ON)
                        Logs.v(getPackageName(),log_tab + "Constructed the remote Devcie " + remoteDevice.getName(), true, true);
                }catch(Exception ex)
                {
                    remoteDevice = null;
                }
            }
            */
            /*if(remoteDevice != null)
            {
                wisePadDeviceConnecting = true;
                bluetoothConnectionState= DEVICE_PAIREDDEVICE_MATCHING_MAC;
                wisePadController.startBTv2(remoteDevice);


            }
            else{*/
            Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().toLowerCase().startsWith("wisepad")|| 
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

                //wisePadDeviceConnecting = true;
                bluetoothConnectionState = DEVCIE_NO_PAIRED_DEVICES;
                //wisePadController.startBTv2(new String[] {"iBT-02 Demo", "WisePad"});
                return true;
            }

            //}

        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {


                if (mCurrentScreen == 0 && ignoreBackDevicekeyOnCardProcess) {
                    // the swiper screen
                    Toast.makeText(this, "Processing Card in progress..", Toast.LENGTH_SHORT).show();
                }else{

                    doneWithCreditSale();


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
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "Connecting...", true, true);
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
                txtProgMsg.setText("Press connect");
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
            final Dialog dlgPairedDevices = Constants.shwoAppCustionDialog(DeviceInfoView.this, "Paired devices");
            ArrayList<String> deviceNameList = new ArrayList<String>();
            for (int i = 0; i < pairedDevicesFound.size(); ++i) {
                deviceNameList.add(pairedDevicesFound.get(i).getName());

            }

            ListView appListView = (ListView) dlgPairedDevices.findViewById(R.id.creditsale_LST_applications);
            appListView.setAdapter(new AppViewAdapter(DeviceInfoView.this, deviceNameList));
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
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + " EmvOnlinePorcessTask onCancelled Task", true, true);

                dismissEMVOnlieProcessProgressActivity();

                if (mstrEMVProcessTaskType.equalsIgnoreCase("stopbluetooth")) {
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
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "EmvOnlinePorcessTask start doInBackground", true, true);
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
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "EmvOnlinePorcessTask  end doInBackground", true, true);
            return null;
        }


        @Override
        protected void onPostExecute(Void unused) {
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "EmvOnlinePorcessTask onPostExecute", true, true);
            dismissEMVOnlieProcessProgressActivity();

            //when the back tab is pressed
             if (mstrEMVProcessTaskType.equalsIgnoreCase("stopbluetooth")) {
                finish();
            }

            mstrEMVProcessTaskType = "";

        }
    }

    private void initViews() {


        mViewFlipper = (ViewFlipper) findViewById(R.id.creditsale_VFL_content);
        TextView txtHeading = ((TextView) findViewById(R.id.topbar_LBL_heading));
        txtHeading.setText("Device Info");
        txtHeading.setTypeface(applicationData.font);





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


        mBtnSwipeOk = (Button) findViewById(R.id.creditsale_BTN_swipe_ok);
        mBtnSwipeOk.setTypeface(applicationData.font);

        ( (TextView) findViewById(R.id.creditsale_LBL_swipe_step11) ).setTypeface(applicationData.font);
        ( (TextView) findViewById(R.id.creditsale_LBL_swipe_step111) ).setTypeface(applicationData.font);

        ( (TextView) findViewById(R.id.creditsale_LBL_swipe_step21) ).setTypeface(applicationData.font);
        ( (TextView) findViewById(R.id.creditsale_LBL_swipe_step211) ).setTypeface(applicationData.font);

        ( (TextView) findViewById(R.id.creditsale_LBL_swipe_step31) ).setTypeface(applicationData.font);
        ( (TextView) findViewById(R.id.creditsale_LBL_swipe_step311) ).setTypeface(applicationData.font);

        mBtnSwipe = (Button) findViewById(R.id.creditsale_BTN_swipe);
        mBtnSwipe.setTypeface(applicationData.font);

        //mBtnSwipe.setEnabled(false);
        //mBtnSwipe.setBackgroundResource(R.drawable.roundrectgrey);
        mBtnSwipeMAG = (Button) findViewById(R.id.creditsale_BTN_swipe_mcr);
        mBtnSwipeMAG.setTypeface(applicationData.font);


        mBtnSwipe.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Button btnSwipe = (Button) arg0;

                if (!wisePadDeviceConnecting) {
                    if (!wisePadController.isDevicePresent()) {

                        final Dialog dialog = Constants.showDialog(DeviceInfoView.this, Constants.DEVICEINFO_DIALOG_MSG,
                                Constants.CARDSALE_Device_Connect_Msg, "2", "Connect", "Clo" +
                                "se");
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

                                           /*mViewFlipper.setVisibility(View.GONE);
                                            ((RelativeLayout) findViewById(R.id.creditsale_REL_bluetooth)).setVisibility(View.VISIBLE);
                                            ((RelativeLayout) findViewById(R.id.top_bar)).setVisibility(View.GONE);
                                            */

                                    //String dlgMsg = String.format(Constants.CARDSALE_ALERT_AMOUNTMSG, applicationData.mCurrency);
                                    final Dialog dlgNoPairedDevices = Constants.showDialog(DeviceInfoView.this, Constants.DEVICEINFO_DIALOG_MSG,
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
                        mcrCardProcessedCtr = 0;
                        arrTxtSwiperMsgs[0].setTextColor(Color.rgb(176, 172, 174)); //press start
                        arrTxtSwiperMsgs[1].setTextColor(Color.rgb(176, 172, 174)); //device detected
                        arrTxtSwiperMsgs[2].setTextColor(Color.rgb(0, 0, 0)); //initializing the swiper
                        arrTxtSwiperMsgs[3].setTextColor(Color.rgb(176, 172, 174)); //device detected

                        txtProgMsg.setTextColor(Color.BLUE);
                        txtProgMsg.setText("");
                        mBtnSwipe.setEnabled(false);
                        mBtnSwipe.setBackgroundResource(R.drawable.roundrectgrey);

                        ignoreBackDevicekeyOnCardProcess = true;

                        if (applicationData.IS_DEBUGGING_ON)
                            Logs.v(getPackageName(), log_tab + "********************************************** Swiper IC-Card intialized ******************************************************", true, true);
                        wisePadController.getDeviceInfo();
                    }
                } else { // if the device is connecting in the back ground

                    /*final Dialog dialog = Constants.showDialog(CreditSaleView.this, Constants.DEVICEINFO_DIALOG_MSG,
                            Constants.CARDSALE_Device_Connecting_Msg, "", "Connect","Close");*/
                    final Dialog dialog = Constants.showDialog(DeviceInfoView.this, Constants.DEVICEINFO_DIALOG_MSG,
                            Constants.CARDSALE_Device_Connecting_Msg, "1");

                    Button yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
                    yes.setOnClickListener(new OnClickListener() {

                        public void onClick(View v) {
                            dialog.dismiss();
                            /*connectToDevice();
                            // show the popup screen. only when if the bluetoothconnection state multiplepaired devcies
                            if(bluetoothConnectionState == DEVCIE_NO_PAIRED_DEVICES)
                            {
                                // TODO Auto-generated method stub
                                TaskShowMultiplePairedDevices pairedtask = new TaskShowMultiplePairedDevices();
                                pairedtask.execute();

                            }
                            */

                        }
                    });

                    Button no = (Button) dialog.findViewById(R.id.bmessageDialogNo);
                    no.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }

            }
        });

        mBtnSwipeOk = (Button) findViewById(R.id.creditsale_BTN_swipe_ok);
        mBtnSwipeOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Button btnSwipe = (Button) arg0;
                mBtnSwipe.setVisibility(View.VISIBLE);
                mBtnSwipeOk.setVisibility(View.INVISIBLE);
                //txtProgMsg.setText("");
                try {
                    //emvSwipeController.releaseAudioResource();
                } catch (Exception ex) {
                }

            }
        });




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
            mEMVProcessProgressActivity = new CustomProgressDialog(DeviceInfoView.this, "Please wait...");
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

    class WisePadSwiperListener implements MswipeWisePadDeviceListener {

        @Override
        public void onRequestInsertCard() {

        }

        @Override
        public void onWaitingForCard() {

        }


        @Override
        public void onReturnCheckCardResult(CheckCardResult checkCardResult, Hashtable<String, String> decodeData) {
            dismissDialog();

        }

        public void processMagCardData(CheckCardResult checkCardResult, Hashtable<String, String> decodeData) {



        }


        @Override
        public void onReturnStartEmvResult(StartEmvResult startEmvResult, String ksn) {

        }

        @Override
        public void onReturnDeviceInfo(Hashtable<String, String> deviceInfoData) {
            // TODO Auto-generated method stub
            dismissDialog();
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onReturnDeviceInfo ", true, true);

            SharedPreferences preferences;
            preferences = PreferenceManager.getDefaultSharedPreferences(DeviceInfoView.this);
            preferences.edit().putString("bluetoothaddress", mBlueToothMcId).commit();
            mBtnSwipe.setEnabled(true);
            mBtnSwipe.setBackgroundResource(R.drawable.roundrectblue);

            if(deviceInfoData.get("emvKsn")!=null)
            {
              String emv = (String)deviceInfoData.get("emvKsn");
              if(emv.length()>=14)
              {

                ( (TextView) findViewById(R.id.creditsale_LBL_swipe_step111) ).setText(emv.substring(0,14));

              }else{
                  ( (TextView) findViewById(R.id.creditsale_LBL_swipe_step111) ).setText(emv);
              }

            }

            if(deviceInfoData.get("trackKsn")!=null)
            {
              String emv = (String)deviceInfoData.get("trackKsn");
              if(emv.length()>=14)
              {
                  ( (TextView) findViewById(R.id.creditsale_LBL_swipe_step211) ).setText(emv.substring(0,14));
              }else{
                  ( (TextView) findViewById(R.id.creditsale_LBL_swipe_step211) ).setText(emv);
              }

            }

            if(deviceInfoData.get("batteryPercentage")!=null)
            {
              String emv = (String)deviceInfoData.get("batteryPercentage") + "%";
              ( (TextView) findViewById(R.id.creditsale_LBL_swipe_step311) ).setText(emv);

            }

            ignoreBackDevicekeyOnCardProcess = false;
        }

        @Override
        public void onReturnTransactionResult(TransactionResult transactionResult, String offlineDeclineTag) {
                dismissDialog();


        }

        @Override
        public void onReturnBatchData(HashMap<String, String> tlv) {
            


        }

        @Override
        public void onReturnTransactionLog(String tlv) {


        }

        @Override
        public void onReturnReversalData(String tlv) {

        }


        @Override
        public void onRequestSelectApplication(ArrayList<String> appList) {
                 }


        @Override
        public void onRequestSetAmount() {

        }

        @Override
        public void onReturnAmountConfirmResult(boolean isSuccess) {


        }

        @Override
        public void onRequestPinEntry(PinEntry pinentry) {
            


        }

        @Override
        public void onReturnPinEntryResult(PinEntryResult pinEntryResult, String epb, String ksn) {
            dismissDialog();

        }

        @Override
        public void onRequestCheckServerConnectivity() {


        }

        @Override
        public void onRequestOnlineProcess(HashMap<String, String> tlv) {
                dismissDialog();

        }

        @Override
        public void onRequestTerminalTime() {
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onRequestTerminalTime ", true, true);
            dismissDialog();
            String terminalTime = new SimpleDateFormat("yyMMddHHmmss").format(Calendar.getInstance().getTime());
            //wisePadController.sendTerminalTime(terminalTime);
            //statusEditText.setText(CreditSaleView.this.getString(R.string.request_terminal_time) + terminalTime);
        }

        @Override
        public void onRequestDisplayText(DisplayText displayText) {
            dismissDialog();
            String msg = "";
            txtProgMsg.setTextColor(Color.RED);
            if (displayText == DisplayText.AMOUNT_OK_OR_NOT) {
                msg = DeviceInfoView.this.getString(R.string.amount_ok);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Status:" + msg);

            } else if (displayText == DisplayText.APPROVED) {
                msg = DeviceInfoView.this.getString(R.string.approved);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Status:" + msg);

            } else if (displayText == DisplayText.CALL_YOUR_BANK) {
                msg = DeviceInfoView.this.getString(R.string.call_your_bank);
                txtProgMsg.setText("Error:" + msg);

            } else if (displayText == DisplayText.CANCEL_OR_ENTER) {
                msg = DeviceInfoView.this.getString(R.string.cancel_or_enter);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Status:" + msg);

            } else if (displayText == DisplayText.CARD_ERROR) {
                msg = DeviceInfoView.this.getString(R.string.card_error);
                txtProgMsg.setText("Error:" + msg);

            } else if (displayText == DisplayText.DECLINED) {
                msg = DeviceInfoView.this.getString(R.string.decline);

                txtProgMsg.setText("Error:" + msg);

            } else if (displayText == DisplayText.ENTER_PIN) {
                msg = DeviceInfoView.this.getString(R.string.enter_pin);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Status:" + msg);

            } else if (displayText == DisplayText.INCORRECT_PIN) {
                msg = DeviceInfoView.this.getString(R.string.incorrect_pin);
                txtProgMsg.setText("Status:" + msg);

            } else if (displayText == DisplayText.INSERT_CARD) {
                msg = DeviceInfoView.this.getString(R.string.insert_card);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Status:" + msg);

            } else if (displayText == DisplayText.NOT_ACCEPTED) {
                msg = DeviceInfoView.this.getString(R.string.not_accepted);
                txtProgMsg.setText("Error:" + msg);

            } else if (displayText == DisplayText.PIN_OK) {
                msg = DeviceInfoView.this.getString(R.string.pin_ok);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Status:" + msg);

            } else if (displayText == DisplayText.PLEASE_WAIT) {
                msg = DeviceInfoView.this.getString(R.string.wait);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Status:" + msg);

            } else if (displayText == DisplayText.PROCESSING_ERROR) {
                msg = DeviceInfoView.this.getString(R.string.processing_error);
                txtProgMsg.setText("Error:" + msg);

            } else if (displayText == DisplayText.REMOVE_CARD) {
                msg = DeviceInfoView.this.getString(R.string.remove_card);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Status:" + msg);

            } else if (displayText == DisplayText.USE_CHIP_READER) {
                msg = DeviceInfoView.this.getString(R.string.use_chip_reader);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Error: " + msg);

            } else if (displayText == DisplayText.USE_MAG_STRIPE) {
                msg = DeviceInfoView.this.getString(R.string.use_mag_stripe);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Status:" + msg);

            } else if (displayText == DisplayText.TRY_AGAIN) {
                msg = DeviceInfoView.this.getString(R.string.try_again);
                txtProgMsg.setText("Error:" + msg);

            } else if (displayText == DisplayText.REFER_TO_YOUR_PAYMENT_DEVICE) {
                msg = DeviceInfoView.this.getString(R.string.refer_payment_device);

                txtProgMsg.setText("Error:" + msg);

            } else if (displayText == DisplayText.TRANSACTION_TERMINATED) {
                msg = DeviceInfoView.this.getString(R.string.transaction_terminated);
                txtProgMsg.setText("Error:" + msg);

            } else if (displayText == DisplayText.TRY_ANOTHER_INTERFACE) {
                msg = DeviceInfoView.this.getString(R.string.try_another_interface);
                txtProgMsg.setText("Error:" + msg);

            } else if (displayText == DisplayText.ONLINE_REQUIRED) {
                msg = DeviceInfoView.this.getString(R.string.online_required);
                txtProgMsg.setText("Error:" + msg);

            } else if (displayText == DisplayText.PROCESSING) {
                msg = DeviceInfoView.this.getString(R.string.processing);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Status:" + msg);

            } else if (displayText == DisplayText.WELCOME) {
                msg = DeviceInfoView.this.getString(R.string.welcome);
            } else if (displayText == DisplayText.PRESENT_ONLY_ONE_CARD) {
                msg = DeviceInfoView.this.getString(R.string.present_one_card);
                txtProgMsg.setText("Error: " + msg);
            } else if (displayText == DisplayText.CAPK_LOADING_FAILED) {
                msg = DeviceInfoView.this.getString(R.string.capk_failed);
                txtProgMsg.setText("Error: " + msg);
            } else if (displayText == DisplayText.LAST_PIN_TRY) {
                msg = DeviceInfoView.this.getString(R.string.last_pin_try);
                txtProgMsg.setTextColor(Color.BLUE);
                txtProgMsg.setText("Status:" + msg);
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
        public void onRequestReferProcess(String pan) {

        }

        @Override
        public void onRequestAdviceProcess(String tlv) {

        }

        @Override
        public void onRequestFinalConfirm() {
            dismissDialog();


        }


        @Override
        public void onBatteryLow(BatteryStatus batteryStatus) {
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onBatteryLow  ", true, true);

            if (batteryStatus == BatteryStatus.LOW) {
                //statusEditText.setText(CreditSaleView.this.getString(R.string.battery_low));
                txtProgMsg.setTextColor(Color.RED);
                txtProgMsg.setText("Status: " + DeviceInfoView.this.getString(R.string.battery_low));

            } else if (batteryStatus == BatteryStatus.CRITICALLY_LOW) {
                //statusEditText.setText(CreditSaleView.this.getString(R.string.battery_critically_low));
                txtProgMsg.setTextColor(Color.RED);
                txtProgMsg.setText("Status: " + DeviceInfoView.this.getString(R.string.battery_critically_low));
            }
        }


        @Override
        public void onBTv2Detected() {
            //statusEditText.setText(getString(R.string.bluetooth_detected));
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onBluetoothDetected", true, true);

            txtProgMsg.setTextColor(Color.BLUE);
            txtProgMsg.setText("Device detected.");
            if (bluetoothConnectionState == DEVCIE_NO_PAIRED_DEVICES) {
                ((TextView) findViewById(R.id.creditsale_LBL_bluetooth_progress)).setTextColor(Color.BLUE);
                ((TextView) findViewById(R.id.creditsale_LBL_bluetooth_progress)).setText("Device detected.");
            }
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

            //statusEditText.setText(g etString(R.string.bluetooth_connected));
            txtProgMsg.setTextColor(Color.BLUE);
            txtProgMsg.setText("Device connected.");
            if (bluetoothConnectionState == DEVCIE_NO_PAIRED_DEVICES) {
                ((TextView) findViewById(R.id.creditsale_LBL_bluetooth_progress)).setTextColor(Color.BLUE);
                ((TextView) findViewById(R.id.creditsale_LBL_bluetooth_progress)).setText("Device connected.");
                bluetoothConnectionState = DEVICE_NO_PAIRED_DEVICES_CONNECTED;
                mViewFlipper.setVisibility(View.VISIBLE);

                ((RelativeLayout) findViewById(R.id.creditsale_REL_bluetooth)).setVisibility(View.GONE);
                ((RelativeLayout) findViewById(R.id.top_bar)).setVisibility(View.VISIBLE);
            }

            arrTxtSwiperMsgs[0].setTextColor(Color.rgb(176, 172, 174)); //pairing the device
            arrTxtSwiperMsgs[0].setText("Device connected");
            arrTxtSwiperMsgs[1].setTextColor(Color.rgb(0, 0, 0)); //press start
            arrTxtSwiperMsgs[2].setTextColor(Color.rgb(176, 172, 174));//initializing the swiper
            arrTxtSwiperMsgs[3].setTextColor(Color.rgb(176, 172, 174));

            // we delay the connected process to wait for about a sec so that internally the sdk prepares it self for the communications,
            // and then show up the start command and then
            //wisePadDeviceConnecting = false;
            ignoreBackDevicekeyOnCardProcess =false;
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

            dismissDialog();
            if (mstrEMVProcessTaskType.equalsIgnoreCase("backbutton") || mstrEMVProcessTaskType.equalsIgnoreCase("onlinesubmit")) {
                if (mEMVProcessTask != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
                    mEMVProcessTask.cancel(true);
            }

            //statusEditText.setText(CreditSaleView.this.getString(R.string.no_device_detected));
            txtProgMsg.setTextColor(Color.RED);
            txtProgMsg.setText("Unable to detect the Wisepad, re-start the device and try reconnecting.");
            mBtnSwipe.setText("Connect");
            if (bluetoothConnectionState == DEVCIE_NO_PAIRED_DEVICES) {
                ((TextView) findViewById(R.id.creditsale_LBL_bluetooth_progress)).setTextColor(Color.RED);
                ((TextView) findViewById(R.id.creditsale_LBL_bluetooth_progress)).setText("Unable to detect the Wisepad, re-start the device and try reconnecting.");
            }
            //mBtnSwipe.setVisibility(View.VISIBLE);
            //mBtnSwipeOk.setVisibility(View.INVISIBLE);

            mBtnSwipe.setEnabled(true);
            mBtnSwipe.setBackgroundResource(R.drawable.roundrectblue);



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

            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onBTv2Disconnected  ", true, true);

            //statusEditText.setText(CreditSaleView.this.getString(R.string.device_unplugged));
            txtProgMsg.setTextColor(Color.RED);
            txtProgMsg.setText("Device disconnected, please ensure the connecting device is  a Wisepad");
            mBtnSwipe.setText("Connect");

            if (bluetoothConnectionState == DEVCIE_NO_PAIRED_DEVICES) {
                ((TextView) findViewById(R.id.creditsale_LBL_bluetooth_progress)).setTextColor(Color.RED);
                ((TextView) findViewById(R.id.creditsale_LBL_bluetooth_progress)).setText("Device disconnected, please ensure the connecting device is  a Wisepad");
            }
            //mBtnSwipe.setVisibility(View.VISIBLE);
            //mBtnSwipeOk.setVisibility(View.INVISIBLE);

            if (mstrEMVProcessTaskType.equalsIgnoreCase("backbutton") || mstrEMVProcessTaskType.equalsIgnoreCase("onlinesubmit") ||
                    mstrEMVProcessTaskType.equalsIgnoreCase("stopbluetooth")) {
                if (mEMVProcessTask != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
                    mEMVProcessTask.cancel(true);
            }


        }

        @Override
        public void onError(Error errorState) {
            dismissDialog();
            ignoreBackDevicekeyOnCardProcess = false;
            mBtnSwipe.setEnabled(true);
            mBtnSwipe.setBackgroundResource(R.drawable.roundrectblue);


            if (mstrEMVProcessTaskType.equalsIgnoreCase("backbutton") || mstrEMVProcessTaskType.equalsIgnoreCase("onlinesubmit")) {
                if (mEMVProcessTask != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
                    mEMVProcessTask.cancel(true);

            }

            //amountEditText.setText("");
            String msg = "";
            if (errorState == Error.CMD_NOT_AVAILABLE) {
                msg = (DeviceInfoView.this.getString(R.string.command_not_available));

            } else if (errorState == Error.TIMEOUT) {
                msg = (DeviceInfoView.this.getString(R.string.device_no_response));
            } else if (errorState == Error.DEVICE_RESET) {
                msg = (DeviceInfoView.this.getString(R.string.device_reset));
            } else if (errorState == Error.UNKNOWN) {
                msg = (DeviceInfoView.this.getString(R.string.unknown_error));
            } else if (errorState == Error.DEVICE_BUSY) {
                msg = (DeviceInfoView.this.getString(R.string.device_busy));
            } else if (errorState == Error.INPUT_OUT_OF_RANGE) {
                msg = (DeviceInfoView.this.getString(R.string.out_of_range));
            } else if (errorState == Error.INPUT_INVALID_FORMAT) {
                msg = (DeviceInfoView.this.getString(R.string.invalid_format));
            } else if (errorState == Error.INPUT_ZERO_VALUES) {
                msg = (DeviceInfoView.this.getString(R.string.zero_values));
            } else if (errorState == Error.INPUT_INVALID) {
                msg = (DeviceInfoView.this.getString(R.string.input_invalid));
            } else if (errorState == Error.CASHBACK_NOT_SUPPORTED) {
                msg = (DeviceInfoView.this.getString(R.string.cashback_not_supported));
            } else if (errorState == Error.CRC_ERROR) {
                msg = (DeviceInfoView.this.getString(R.string.crc_error));
            } else if (errorState == Error.COMM_ERROR) {
                msg = (DeviceInfoView.this.getString(R.string.comm_error));
            } else if (errorState == Error.FAIL_TO_START_BTV2) {
                msg = (DeviceInfoView.this.getString(R.string.fail_to_start_bluetooth_v2));
                resetSwiperRoutines();
                wisePadDeviceConnecting = false;
            } else if (errorState == Error.INVALID_FUNCTION_IN_CURRENT_MODE) {
                msg = (DeviceInfoView.this.getString(R.string.invalid_function));
            } else if (errorState == Error.COMM_LINK_UNINITIALIZED) {
                msg = (DeviceInfoView.this.getString(R.string.comm_link_uninitialized));
            } else if (errorState == Error.BTV2_ALREADY_STARTED) {
                msg = (DeviceInfoView.this.getString(R.string.bluetooth_already_started));

            }
            txtProgMsg.setTextColor(Color.RED);
            txtProgMsg.setText("Error: " + msg);

            if (bluetoothConnectionState == DEVCIE_NO_PAIRED_DEVICES) {
                ((TextView) findViewById(R.id.creditsale_LBL_bluetooth_progress)).setTextColor(Color.RED);
                ((TextView) findViewById(R.id.creditsale_LBL_bluetooth_progress)).setText(msg);
            }
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
		public void onReturnCancelCheckCardResult(boolean arg0) {
			// TODO Auto-generated method stub
			
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
            txtProgMsg.setText("Press device info.");
                            mBtnSwipe.setText("Device-Info");
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
            //mBtnSwipe.setText("Start");
            mBtnSwipe.setEnabled(true);
            mBtnSwipe.setBackgroundResource(R.drawable.roundrectblue);
            mBtnSwipe.setText("Device-Info");

        }
    }


}


