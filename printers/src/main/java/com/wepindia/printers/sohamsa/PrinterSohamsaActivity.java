package com.wepindia.printers.sohamsa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.Bitmap.CreateBitmap;
import com.Bitmap.FormatAndroid;
import com.Utils.TextAlign;
import com.bt.BluetoothChatService;
import com.bt.Connectivity;
import com.bt.ReadFromBt;
import com.bt.SendToWrite;
import com.wep.common.app.ActionBarUtils;
import com.wep.common.app.print.BillKotItem;
import com.wep.common.app.print.BillServiceTaxItem;
import com.wep.common.app.print.BillSubTaxItem;
import com.wep.common.app.print.BillTaxItem;
import com.wep.common.app.print.Payment;
import com.wep.common.app.print.PrintKotBillItem;
import com.wepindia.printers.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PrinterSohamsaActivity extends Activity {

    Button BtnProceedPrint,btnConnect,BtnCancelPrint;
    public boolean isPrinting = false;
    private String sp;
    private static final String TAG = PrinterSohamsaActivity.class.getSimpleName();
    byte[] readBuf ;
    String readMessage="";
    EditText Key1Edit,key2Edit;
    // Message types sent from the PrinterSohamsaActivityService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    // int sendFlag =0;
    // Key names received from the PrinterSohamsaActivityService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    private static final int REQUEST_PRINT = 10;
    ReadFromBt rbt = new ReadFromBt();
    int count = 0;
    Handler mHandlerClient = null;
    Bitmap myBitmap;
    SendToWrite send = new SendToWrite();
    boolean insideMSR = false;
    boolean insideFPS=false;
    TextView Status, mTitle;
    boolean insideNFC = false;
    // Add device ID which to connect.
    // String DEVICEID="new ";
    int width = 384;
    int height = 850;
    byte[] bitmapArraywithLength;
    FormatAndroid createFormat = new FormatAndroid();
    String tmpTemplateData;
    // Name of the connected device
    private String mConnectedDeviceName = null,mConnectedDeviceAddress = null;
    // Array adapter for the conversation thread
    // String buffer for outgoing messages
    String[] result ;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothChatService mChatService = null;
    Intent serverIntent = null;
    int z;
    LinearLayout ll_ButtonConnect;
    List<String[]> tmpList ;
    private ImageView imageViewPreview;
    private PrintKotBillItem item;
    private ArrayList<ArrayList<String>> itemReport;
    private Payment payment;
    private String printType;
    public static String DEVICE_ADDRESS = "device_addr";
    SharedPreferences mSharedPreferences ;
    private int code;
    private String name;


    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "+++ ON CREATE +++");
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_printer_sohamsa);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        ActionBarUtils.goBack(PrinterSohamsaActivity.this,findViewById(R.id.imgTitleBackIcon));
        ActionBarUtils.setTitle(findViewById(R.id.tvTitleBarCaption),"Print");
        sp = getResources().getString(R.string.superSpace);
        mSharedPreferences = getSharedPreferences("MYPREFERENCES", MODE_PRIVATE);
        ll_ButtonConnect=(LinearLayout) findViewById(R.id.llButtonConnect);
        btnConnect = (Button) findViewById(R.id.ButtonConnect);
        BtnProceedPrint = (Button) findViewById(R.id.BtnProceedPrint);
        BtnCancelPrint = (Button) findViewById(R.id.BtnCancelPrint);
        mTitle = (TextView) findViewById(R.id.title_left_text);
        mTitle.setText(R.string.app_name);
        mTitle = (TextView) findViewById(R.id.title_right_text);
        imageViewPreview = (ImageView) findViewById(R.id.imageViewPreview);
        Key1Edit = new EditText(PrinterSohamsaActivity.this);
        key2Edit = new EditText(PrinterSohamsaActivity.this);
        // Get local Bluetooth adapter
        Intent intent = getIntent();
        if(intent!=null)
        {
            printType = intent.getStringExtra("printType");
            if(printType.equalsIgnoreCase("KOT"))
            {
                item = (PrintKotBillItem) intent.getSerializableExtra("printData");
                tmpList = getPrintKOT(item);
            }
            else if(printType.equalsIgnoreCase("BILL"))
            {
                item = (PrintKotBillItem) intent.getSerializableExtra("printData");
                tmpList = getPrintBill(item);
            }
            else if(printType.equalsIgnoreCase("REPORT"))
            {
                String reportName = intent.getStringExtra("reportName");
                itemReport = (ArrayList<ArrayList<String>>) intent.getSerializableExtra("printData");
                tmpList = getPrintReport(itemReport,reportName);
            }
            else if(printType.equalsIgnoreCase("PaymentPrint"))
            {
                String reportName = intent.getStringExtra("reportName");
                payment = (Payment) intent.getSerializableExtra("printData");
                tmpList = getPrintMSwipePaymentBill(payment,reportName);
            }
            else
            {
                code =  intent.getIntExtra("code",0);
                name = intent.getStringExtra("name");
                tmpList = getTestPrint();
            }
        }
        else
        {
            tmpList = getTestPrint();
        }

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        //tmpList = getPrintDataFormat1();
        setPrintDataPreview();
    }

    private List<String[]> getPrintMSwipePaymentBill(Payment payment, String reportName) {
        List<String[]> tmpList = new ArrayList<String[]>();
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", reportName, TextAlign.CENTER,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "======================================,", TextAlign.CENTER,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", String.valueOf(payment.getMerchantName()), TextAlign.CENTER,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", String.valueOf(payment.getMerchantAdd()), TextAlign.CENTER,getApplicationContext()));
        tmpList.add(CreateBitmap.AddBlank(1));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "======================================,", TextAlign.CENTER,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Date/Time     :"+ String.valueOf(payment.getDateTime()), TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Invoice Id    :"+ String.valueOf(payment.getInvoiceNo()), TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Card Name     : "+ String.valueOf(payment.getCardHolderName()), TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Card Number   :"+ String.valueOf(payment.getCardNo()), TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Expiry Date   : "+ String.valueOf(payment.getExpDate()), TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Card Type     : "+ String.valueOf(payment.getCardType()), TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Reference No  : "+ String.valueOf(payment.getRefNo()), TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", "======================================", TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Total         : "+ String.valueOf(payment.getTotal_Pay_Amount()), TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddBlank(8));
        return tmpList;
    }

    private void setPrintDataPreview() {
        myBitmap = CreateBitmap.CreateImage(tmpList);
        imageViewPreview.setImageBitmap(myBitmap);
    }

    @SuppressLint("NewApi")
    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "++ ON START ++");
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled())
        {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        }
        else
        {
            if (mChatService == null)
                setupChat();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        Log.e(TAG, "+ ON RESUME +");
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity
        // returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't
            // started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }

    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
                    serverIntent = new Intent(PrinterSohamsaActivity.this, DeviceListActivity.class);
                    startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE_SECURE);
                }
                else{
                    send.Disconnect(mChatService);
                }
            }

        });
        BtnCancelPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        BtnProceedPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String address = mSharedPreferences.getString("MAC_ADDRESS", null);
                String address = mSharedPreferences.getString(name+"#"+code, null);
                if(address != null)
                {
                    if (isPrinting)
                    {
                        Toast.makeText(getApplicationContext(), "Device Is busy. Please try again", Toast.LENGTH_LONG).show();
                    }
                    else
                        connectDevice();
                }
                else
                {
                    //Toast.makeText(PrinterSohamsaActivity.this, "Some error", Toast.LENGTH_SHORT).show();
                    if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED)
                    {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        serverIntent = new Intent(PrinterSohamsaActivity.this, DeviceListActivity.class);
                                        startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE_SECURE);
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(PrinterSohamsaActivity.this);
                        builder.setTitle("Printer Configuration!")
                                .setMessage("It seems you are printing first time in this device. Do you want to configure a new Printer?")
                                .setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener)
                                .show();
                    }
                }
            }
        });
        mChatService = new BluetoothChatService(this, mHandler);
    }

    private void printDoc() {
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED)
        {
            Toast.makeText(getApplicationContext(), R.string.not_connected, Toast.LENGTH_SHORT).show();
        }
        else if (isPrinting)
        {
            Toast.makeText(getApplicationContext(), "Device Is busy. Please try again", Toast.LENGTH_LONG).show();
        }
        else
        {
            try {
                // API to Generate image for Printing
                bitmapArraywithLength = createFormat.convert(myBitmap);
                z = 1;
                if (isPrinting) {
                    Toast.makeText(getApplicationContext(), "Printer Device is busy. Please try again", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getApplicationContext(), "print length:" + String.valueOf(bitmapArraywithLength.length), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Printing", Toast.LENGTH_SHORT).show();
                    boolean result1 = send.print(bitmapArraywithLength, mChatService, getApplicationContext());
                    if (!result1) {
                        Toast.makeText(getApplicationContext(), "Print data exceded the Maximum limit", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Please Enter Valid Details", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Unable to Save", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    /*@Override
    public void onBackPressed() {
        count = 0;
        if (insideNFC == true) {

            try {
                //Toast.makeText(getApplicationContext(), "insideNFC", 0).show();
                send.NFCStopPolling(mChatService);
                insideNFC = false;
                // sendFlag = 0;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (insideMSR == true) {
            send.resetMSR(mChatService);
            insideMSR = false;
        }
        if (insideFPS==true) {
            ll_ButtonConnect.setVisibility(View.VISIBLE);
            btn_connect.setVisibility(View.VISIBLE);
        }
        //fingerDetail.setVisibility(View.GONE);
        btnPrintTest.setVisibility(View.VISIBLE);
        //btnPinpadTest.setVisibility(View.GONE);
        btn_connect.setVisibility(View.VISIBLE);
    }*/

    @Override
    public synchronized void onPause() {
        super.onPause();
        Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null)
            mChatService.stop();
        Log.e(TAG, "--- ON DESTROY ---");
    }

    @SuppressLint("NewApi")
    private void ensureDiscoverable() {
        Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(
                    BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    // The Handler that gets information back from the PrinterSohamsaActivityService
    private final Handler mHandler = new Handler() {

        byte[] data;
        int counter =0,i;
        String dataString;
        @Override
        public void handleMessage(Message msg) {
            String connected;
            String notConnected;
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            mTitle.setText(R.string.title_connected_to);
                            connected = "Disconnect";
                            btnConnect.setText(connected);
                            mTitle.append(mConnectedDeviceName);
                            SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                            //mEditor.putString("MAC_ADDRESS", mConnectedDeviceAddress);
                            mEditor.putString(name+"#"+code, mConnectedDeviceAddress);
                            mEditor.commit();

                            // mConversationArrayAdapter.clear(); //ckk

                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            mTitle.setText(R.string.title_connecting);
                            notConnected = "Connect";
                            btnConnect.setText(notConnected);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                            connected = "Disconnect";
                            btnConnect.setText(connected);
                        case BluetoothChatService.STATE_NONE:
                            mTitle.setText(R.string.title_not_connected);
                            notConnected = "Connect";
                            btnConnect.setText(notConnected);
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    // mConversationArrayAdapter.add("Me:  " + writeMessage); //ckk
                    break;
                case MESSAGE_READ:
                    try {
                        readBuf = (byte[]) msg.obj;
                        readMessage = new String(readBuf, 0, msg.arg1);

                        result = rbt.getResponse(readMessage, readBuf);

                        // result[0] is used to identify from which peripheral, data is coming
                        String handledResult = result[0];

                        if (handledResult.equalsIgnoreCase("null")) {

                        }

                        //FPS packet received.
                        else if (handledResult.equalsIgnoreCase("PSF")) {
                            alertPFS(result);
                        }

                        //FPS Template and Image
                        else if (handledResult.equalsIgnoreCase("FPS")) {
                            alertFPS(result);
                        }

                        //NFC CARD Authentication Sucess.
                        else if (handledResult.equalsIgnoreCase("AUTHYS")) {
                            alertAUTHYS();
                        }

                        //NFC CARD Authentication failed
                        else if (handledResult.equalsIgnoreCase("AUTHNO")) {
                            alertAUTHNO();
                        }

                        else if (handledResult.equalsIgnoreCase("READFAIL")) {
                            alertREADFAIL();
                        }

                        else if (handledResult.equalsIgnoreCase("WRITEFAIL")) {
                            alertWRITEFAIL();
                        }

                        //NFC Write Success
                        else if (handledResult.equalsIgnoreCase("ACK")) {
                            alertACK(result);
                        }

                        //NFC Confirmation to write
                        else if (handledResult.equalsIgnoreCase("CONF")) {
                            alertCONF(result);
                        }

                        //NFC UID
                        else if (handledResult.equalsIgnoreCase("AUTH")) {
                            alertAUTH(result);
                        }

                        //NFC Read data
                        else if (handledResult.equalsIgnoreCase("NFCDATA")) {
                            Log.d("NFC", "result[1]datalength==>"+result[1].length());

                            alertNFCDATA(result);
                        }

                        // Data Received from NFC
                        else if (handledResult.equalsIgnoreCase("NFC")) {
                            alertNFC(result);
                        }

                        //Battery Status
                        else if (handledResult.equalsIgnoreCase("BAT")) {
                            alertBAT(result);
                        }

                        else if (handledResult.equalsIgnoreCase("AUENDE")) {
                            alertAUENDE(result);
                        }

                        else if (handledResult.equalsIgnoreCase("AUCOLL")) {
                            alertAUCOLL(result);
                        }

                        else if (handledResult.equalsIgnoreCase("FPR")) {
                            alertFPR(result);
                        }

                        // Data Received from Thermal Printer
                        else if (handledResult.equalsIgnoreCase("PRINT")) {
                            //z++;
                            //if(z>5)
                            alertPRINT(result);
                            //else
                            //	next();
                        }

                        // Data Received from MSR- result array Contains cardNumber,
                        // Name etc
                        else if (handledResult.equalsIgnoreCase("MSR")) {
                            alerMSR(result);
                        }

                        else if (handledResult.equalsIgnoreCase("DIS")) {
                            Status.setText("Disconnected");
                        }

                        else if (handledResult.equalsIgnoreCase("NOTCONF")) {
                            Toast.makeText(getApplicationContext(), "DATA NOT RECEIVED PROPERLY", Toast.LENGTH_SHORT).show();
                        }

                        else if (handledResult.equalsIgnoreCase("CON")) {
                            Status.setText("Connected");
                        }

                        else if (handledResult.equalsIgnoreCase("ERR")) {
                            {
                                mHandlerClient.obtainMessage(MESSAGE_READ, -1, -1,
                                        "#ERR*").sendToTarget();
                                break;
                            }
                        }

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    mConnectedDeviceAddress = msg.getData().getString(DEVICE_ADDRESS);
                    //Toast.makeText(getApplicationContext(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    printDoc();
                    break;
                case MESSAGE_TOAST:
                    //Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        private void alertWRITEFAIL() {
            Toast.makeText(getApplicationContext(),
                    "Card Write Failed. Please try again.", Toast.LENGTH_SHORT)
                    .show();
        }

        private void alertREADFAIL() {
            Toast.makeText(getApplicationContext(),
                    "Card Read Failed. Please try again.", Toast.LENGTH_SHORT)
                    .show();

        }

        private void alertAUCOLL(String[] result) {
            Toast.makeText(getApplicationContext(),
                    "Authentication Failed", Toast.LENGTH_SHORT)
                    .show();
        }

        private void alertPFS(String[] result) {

            //send.sendPacketACK(mChatService);
            if(result[4].equalsIgnoreCase("SUCCESS")){
                //Sending acknowledge for received packet
                send.sendPacketACK(mChatService);
                if(result[2].equalsIgnoreCase("0")){
                    Toast.makeText(getApplicationContext(), "Capturing Image!! Please wait...", Toast.LENGTH_LONG).show();
                }

            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setMessage(result[4])
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }

        private void alertAUENDE(String[] result) {
            Toast.makeText(getApplicationContext(),
                    "NFC ERROR 101", Toast.LENGTH_SHORT)
                    .show();
        }

        private void alertAUTHNO() {
            Toast.makeText(getApplicationContext(),
                    "Card not Authenticated", Toast.LENGTH_SHORT)
                    .show();
            //btnnfcRead.setEnabled(true);
            //btnNfcWrite.setEnabled(true);
        }

        private void alertAUTHYS() {
            Toast.makeText(getApplicationContext(),
                    "Card Authenticated Successfully",
                    Toast.LENGTH_SHORT).show();
        }

        private void alerMSR(String[] result) {
            String cardNumber = result[1];
            String Name = result[2];
            String expDate = result[3];
            String serviceCode = result[4];
            String PinVerificationKeyIndicator = result[5];
            String PINVerificationValue = result[6];
            String CardVerificationValue = result[7];

            // Seperating the Card Details for proper display
            String msrData = "Card Number : " + cardNumber + "\n\n"
                    + "Name : " + Name + "\n\n" + "Exp Date : "
                    + expDate + "(YY/MM)" + "\n\n"
                    + "Service Code : " + serviceCode + "\n\n"
                    + "Pin Verification Key Indicator : "
                    + PinVerificationKeyIndicator + "\n\n"
                    + "Pin Verification Key Value : "
                    + PINVerificationValue + "\n\n"
                    + "Card Verification Value : "
                    + CardVerificationValue;
            // sendFlag = 0;

        }

        private void alertPRINT(String[] result) {
            String errDesc = result[1];
            if(errDesc.equalsIgnoreCase("DEVICE BUSY"))
            {
                isPrinting = true;
            }
            else if (errDesc.equalsIgnoreCase("DEVICE FREE"))
            {
                isPrinting = false;
            }
            else
            {
                if(errDesc.equalsIgnoreCase("SUCCESS"))
                {
                    if(printType.equalsIgnoreCase("TEST"))
                    {
                        Intent intent = new Intent();
                        intent.putExtra("code",code);
                        intent.putExtra("name",name);
                        setResult(Activity.RESULT_OK, intent);
                    }
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), errDesc, Toast.LENGTH_LONG).show();
                    AlertDialog.Builder alertadd = new AlertDialog.Builder(PrinterSohamsaActivity.this);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.sample, null);
                    ScrollView sv = (ScrollView) view.findViewById(R.id.scrollView1);
                    LinearLayout ll = new LinearLayout(getApplicationContext());
                    ll.setOrientation(LinearLayout.VERTICAL);
                    TextView previewImage = new TextView(getApplicationContext());
                    previewImage.setText(errDesc);// setImageBitmap(myBitmap);
                    ll.addView(previewImage);
                    sv.addView(ll);
                    alertadd.setView(view);
                    alertadd.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dlg, int sumthin)
                                {
                                    //mChatService.stop();
                                    finish();
                                }
                            });
                    alertadd.show();
                }
            }
        }
        private void alertFPR(String[] result) {
            String status = result[1];
            AlertDialog.Builder alertadd = new AlertDialog.Builder(
                    PrinterSohamsaActivity.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.sample, null);
            ScrollView sv = (ScrollView) view
                    .findViewById(R.id.scrollView1);
            LinearLayout ll = new LinearLayout(
                    getApplicationContext());
            ll.setOrientation(LinearLayout.VERTICAL);
            ImageView previewImage1 = new ImageView(
                    getApplicationContext());

            if (status.equals("SUCCESS")) {
                Log.d("VERIFY CMD","COMPLETED");
                previewImage1
                        .setImageResource(R.drawable.checkmark);
            } else {
                previewImage1.setImageResource(R.drawable.fail);
            }
            TextView previewImage = new TextView(
                    getApplicationContext());
            previewImage.setText(status + "\n");
            ll.addView(previewImage1);
            ll.addView(previewImage);
            sv.addView(ll);
            alertadd.setView(view);
            alertadd.setTitle("Verification Result");
            alertadd.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dlg,
                                            int sumthin) {

                        }
                    });
            alertadd.show();
        }

        private void alertNFC(String[] result) {
            String prnCardType = result[1];
            String prnUID = result[3];
            String prnNFCData = result[2];
            byte[] nfcDataRead = result[4].getBytes();
            String valtoSet = "Card Type : " + prnCardType + "\n\n"
                    + "Data : " + prnUID + "\n\nUID : "
                    + prnNFCData;
        }

        private void alertNFCDATA(String[] result) {
            String valtoSet = "";
            valtoSet="Data : " + result[1];
        }

        private void alertAUTH(String[] result) {
            String prnCardType = result[1];
            String prnUID = result[2];
            String valtoSet = "Card Type : " + prnCardType + "\n\n"
                    + "UID : " + prnUID;
            //nfctxtview.setTextColor(Color.WHITE);
            //txt_ViewMSRData.setText("");
            //txt_ViewMSRData.setText(valtoSet);
        }

        private void alertFPS(String[] result) throws IOException, InterruptedException {



            String status = result[1];
            String description=result[2];
            String fpsTemplate = result[3];
            String compImage = result[4];

            if (status.equalsIgnoreCase("SUCCESS")) {
                AlertDialog.Builder alertadd = new AlertDialog.Builder(
                        PrinterSohamsaActivity.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.sample, null);

                alertadd.setView(view);
                alertadd.setTitle("FPS RESULT");
                alertadd.setMessage("Enroll Status :- "+status+"\n"+description);
                alertadd.setCancelable(false);
                alertadd.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dlg, int sumthin)
                            {
                                finish();
                            }
                        });
                alertadd.show();
				/*tmpTemplateData=null;
				tmpTemplateData = fpsTemplate;*/
                if(tmpTemplateData.length() <5){
                    tmpTemplateData=fpsTemplate;
                }else{
                    tmpTemplateData = tmpTemplateData+"*"+fpsTemplate;
                }
            }

            else if (status.equalsIgnoreCase("TIMEOUT")) {
                AlertDialog.Builder alertadd = new AlertDialog.Builder(
                        PrinterSohamsaActivity.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.sample,
                        null);

                alertadd.setView(view);
                alertadd.setTitle("FPS RESULT");
                alertadd.setMessage("Enroll Status :- "+status+"\n"+description);
                alertadd.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dlg,
                                    int sumthin) {

                            }
                        });
                alertadd.show();
            }
            else {
                AlertDialog.Builder alertadd = new AlertDialog.Builder(
                        PrinterSohamsaActivity.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.sample,
                        null);
                ScrollView sv = (ScrollView) view
                        .findViewById(R.id.scrollView1);
                LinearLayout ll = new LinearLayout(
                        getApplicationContext());
                ll.setOrientation(LinearLayout.VERTICAL);
                TextView previewImage = new TextView(
                        getApplicationContext());
                previewImage.setText("Enrollment Status :-\n "+status+"\n"+description);
                ll.addView(previewImage);
                sv.addView(ll);
                alertadd.setView(view);
                alertadd.setTitle("FPS RESULT");
                alertadd.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dlg,
                                    int sumthin) {
                            }
                        });
                alertadd.show();
            }
        }

        private void alertCONF(String[] result) {

            AlertDialog.Builder alertadd = new AlertDialog.Builder(
                    PrinterSohamsaActivity.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.sample, null);
            ScrollView sv = (ScrollView) view
                    .findViewById(R.id.scrollView1);
            LinearLayout ll = new LinearLayout(
                    getApplicationContext());
            ll.setOrientation(LinearLayout.VERTICAL);
            TextView previewImage = new TextView(
                    getApplicationContext());
            sv.addView(ll);
            alertadd.setView(view);
            alertadd.setTitle("Are you sure ? You want to Write ??");
            alertadd.setCancelable(false);
            alertadd.setNeutralButton("WRITE",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dlg,
                                            int sumthin) {
                            SendToWrite wr = new SendToWrite();
                            send.sendNFCCNF(mChatService);
                        }
                    });
            alertadd.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {

                            send.sendNFCCNL(mChatService);
                        }
                    });
            alertadd.show();
            // sendFlag = 0;

        }

        private void alertACK(String[] result) {
            AlertDialog.Builder alertadd = new AlertDialog.Builder(PrinterSohamsaActivity.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.sample, null);
            ScrollView sv = (ScrollView) view.findViewById(R.id.scrollView1);
            LinearLayout ll = new LinearLayout(getApplicationContext());
            ll.setOrientation(LinearLayout.VERTICAL);
            TextView previewImage = new TextView(getApplicationContext());
            previewImage.setText(result[1]);
            ll.addView(previewImage);
            sv.addView(ll);
            alertadd.setView(view);
            alertadd.setCancelable(false);
            alertadd.setNeutralButton("OK",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dlg, int sumthin) {
                    SendToWrite wr = new SendToWrite();
                    send.sendNFCACK(mChatService);
                }
            });
            alertadd.show();
        }

        private void alertBAT(String[] result) {
            String batStatus = result[1];
            AlertDialog.Builder alertadd = new AlertDialog.Builder(
                    PrinterSohamsaActivity.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.sample, null);
            ScrollView sv = (ScrollView) view
                    .findViewById(R.id.scrollView1);
            LinearLayout ll = new LinearLayout(
                    getApplicationContext());
            ll.setOrientation(LinearLayout.VERTICAL);
            TextView previewImage = new TextView(
                    getApplicationContext());
            previewImage.setText(batStatus + " " + result[2]+" %");
            ll.addView(previewImage);
            sv.addView(ll);
            alertadd.setView(view);
            alertadd.setTitle("Battery Status");
            alertadd.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dlg,
                                            int sumthin) {
                        }
                    });
            alertadd.show();
            // sendFlag = 0;
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case REQUEST_PRINT :
                Printeceipt(data);

        }

    }

    private void Printeceipt(Intent data) {
        String[] txt= data.getStringArrayExtra("text");
        PrinterSohamsaActivity bc = new PrinterSohamsaActivity();
        String text1 = txt[0];
        String text2 = txt[1];
        String text3= txt[2];
        String text4 = txt[3];
        Context context = getApplicationContext();

        try {
            List<String[]> tmpList = new ArrayList<String[]>();

            // API to Generate image for Printing
            myBitmap = CreateBitmap.CreateImage(tmpList);

            AlertDialog.Builder alertadd = new AlertDialog.Builder(PrinterSohamsaActivity.this);

            // Preview the generated Image generated from Sequence
            // of API Calls

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.sample, null);
            ScrollView sv = (ScrollView) view.findViewById(R.id.scrollView1);
            LinearLayout ll = new LinearLayout(getApplicationContext());
            ll.setOrientation(LinearLayout.VERTICAL);
            ImageView previewImage = new ImageView(getApplicationContext());
            previewImage.setImageBitmap(myBitmap);
            ll.addView(previewImage);
            sv.addView(ll);
            alertadd.setView(view);
            alertadd.setTitle("Preview");
            alertadd.setCancelable(false);
            alertadd.setPositiveButton("Print!",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dlg, int sumthin) {

                    // API to Generate the Bytes to send to Printer
                    bitmapArraywithLength = createFormat.convert(myBitmap);
                    Log.d("DONE", "DONE::Length :"+ bitmapArraywithLength.length);

                    boolean result=send.print(bitmapArraywithLength,
                            mChatService,getApplicationContext());
                    if(!result){
                        Toast.makeText(getApplicationContext(), "Print data exceded the Maximum limit", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Printing..", Toast.LENGTH_LONG).show();
                    }
                }
            });

            alertadd.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dlg, int sumthin) {

                }
            });
            alertadd.show();

        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(),"Please Enter Valid Details", Toast.LENGTH_LONG)
                    .show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Unable to Save", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras().getString(Connectivity.EXTRA_DEVICE_ADDRESS);
        // Get the BLuetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        mChatService.connect(device);
    }

    private void connectDevice() {
        // Get the device MAC address
        //String address = mSharedPreferences.getString("MAC_ADDRESS", null);
        String address = mSharedPreferences.getString(name+"#"+code, null);
        // Get the BLuetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        mChatService.connect(device);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    // Menu which provides Option to Connect Securely/Insecurely - Optional

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent serverIntent = null;
        int i = item.getItemId();
        if (i == R.id.secure_connect_scan) {// Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent(this, DeviceListActivity.class);
            return true;
        } else if (i == R.id.insecure_connect_scan) {// Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
            return true;
        } else if (i == R.id.discoverable) {// Ensure this device is discoverable by others
            ensureDiscoverable();
            return true;
        }
        return false;
    }


    public List<String[]> getPrintKOT(PrintKotBillItem item) {
        List<String[]> tmpList = new ArrayList<String[]>();
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Table # "+item.getTableNo() +" | "+"KOT # "+item.getBillNo(), TextAlign.CENTER,getApplicationContext()));
        tmpList.add(CreateBitmap.AddBlank(1));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "==========================================", TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Attandant : "+item.getOrderBy(), TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Date : "+item.getDate() +" | "+"Time : "+item.getTime(), TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "==========================================", TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Lists", TextAlign.CENTER,getApplicationContext()));
        tmpList.add(CreateBitmap.AddBlank(1));
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", "Sl              NAME              QTY", TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", "===============================================", TextAlign.LEFT,getApplicationContext()));
        ArrayList<BillKotItem> billKotItems = item.getBillKotItems();
        Iterator it = billKotItems.iterator();
        while (it.hasNext())
        {
            BillKotItem billKotItem = (BillKotItem) it.next();
            int id = billKotItem.getItemId();
            String name = getFormatedCharacterForPrint(billKotItem.getItemName(),10);
            String qty = billKotItem.getQty()+"";
            String pre = getPostAddedSpaceFormat("", String.valueOf(id),15)+name;
            tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", getPreAddedSpaceFormat(pre,qty,38), TextAlign.LEFT,getApplicationContext()));
        }
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", "===========================================", TextAlign.CENTER,getApplicationContext()));
        tmpList.add(CreateBitmap.AddBlank(5));

        return tmpList;
    }

    public List<String[]> getPrintBill(PrintKotBillItem item) {
        List<String[]> tmpList = new ArrayList<String[]>();
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Resturant Invoice", TextAlign.CENTER,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", item.getAddressLine1(), TextAlign.CENTER,getApplicationContext()));
        tmpList.add(CreateBitmap.AddBlank(1));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", item.getAddressLine2(), TextAlign.CENTER,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", item.getAddressLine3(), TextAlign.CENTER,getApplicationContext()));
        //tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Phone: 080661 12000", TextAlign.CENTER,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Bill No # "+item.getBillNo(), TextAlign.CENTER,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Table # "+item.getTableNo(), TextAlign.CENTER,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Date : "+item.getDate() +" | "+"Time : "+item.getTime(), TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Cashier   : "+item.getOrderBy(), TextAlign.CENTER,getApplicationContext()));
        tmpList.add(CreateBitmap.AddBlank(1));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Customer Name   : "+item.getCustomerName(), TextAlign.CENTER,getApplicationContext()));
        tmpList.add(CreateBitmap.AddBlank(1));
        // Added by raja
        if(item.getBillingMode().equalsIgnoreCase("4")) {
            tmpList.add(CreateBitmap.AddText(20, "Bold", "MONOSPACE", "Payment Status   : " + item.getPaymentStatus(), TextAlign.CENTER, getApplicationContext()));
            tmpList.add(CreateBitmap.AddBlank(1));
        }
        // -----------
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Dine In", TextAlign.CENTER,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", "ID   NAME        QTY   RATE    AMOUNT ", TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", "======================================", TextAlign.LEFT,getApplicationContext()));
        ArrayList<BillKotItem> billKotItems = item.getBillKotItems();
        Iterator it = billKotItems.iterator();
        while (it.hasNext())
        {
            BillKotItem billKotItem = (BillKotItem) it.next();
            String preId = getPostAddedSpaceFormat("", String.valueOf(billKotItem.getItemId()),5);
            String preName = getPostAddedSpaceFormat("",getFormatedCharacterForPrint(String.valueOf(billKotItem.getItemName()),10),12);
            String preQty = getPostAddedSpaceFormat("",getFormatedCharacterForPrint(String.valueOf(billKotItem.getQty()),4),6);
            String preRate = getPostAddedSpaceFormat("",getFormatedCharacterForPrint(String.valueOf(billKotItem.getRate()),4),9);
            String preAmount = getPostAddedSpaceFormat("", String.valueOf(billKotItem.getAmount()),7);
            String pre = preId+preName+preQty+preRate+preAmount;
            tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", pre, TextAlign.LEFT,getApplicationContext()));
        }
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", "======================================", TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", getSpaceFormat("SUB-TOTAL","", String.valueOf(item.getSubTotal()),36), TextAlign.LEFT,getApplicationContext()));
        ArrayList<BillTaxItem> billTaxItems = item.getBillTaxItems();
        Iterator it1 = billTaxItems.iterator();
        while (it1.hasNext())
        {
            BillTaxItem billKotItem = (BillTaxItem) it1.next();
            String pre = getSpaceFormat(billKotItem.getTxName(), String.valueOf(billKotItem.getPercent()), String.valueOf(billKotItem.getPrice()),36);
            tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", pre, TextAlign.LEFT,getApplicationContext()));
        }
        // added by raja
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", getSpaceFormat("Total VAT","", item.getTotalSalesTaxAmount(),36), TextAlign.LEFT,getApplicationContext()));

        double servicetaxpercent = 0, servicetaxamount = 0;
        ArrayList<BillServiceTaxItem> billServiceTaxItems = item.getBillServiceTaxItems();
        Iterator it2 = billServiceTaxItems.iterator();
        while (it2.hasNext())
        {
            BillServiceTaxItem billKotItem = (BillServiceTaxItem) it2.next();
            servicetaxpercent = billKotItem.getServicePrice();
            String pre = getSpaceFormat(billKotItem.getServiceTxName(), String.valueOf(billKotItem.getServicePercent()), String.valueOf(billKotItem.getServicePrice()),36);
            //tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", pre, TextAlign.LEFT,getApplicationContext()));
        }
        servicetaxpercent = servicetaxpercent - Double.parseDouble(String.valueOf(item.getTotalsubTaxPercent()));
        servicetaxamount = item.getSubTotal() * servicetaxpercent / 100;
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", getSpaceFormat("Service Tax", String.valueOf(servicetaxamount), String.valueOf(servicetaxpercent),36), TextAlign.LEFT,getApplicationContext()));
        ArrayList<BillSubTaxItem> billSubTaxItems = item.getBillSubTaxItems();
        Iterator it3 = billSubTaxItems.iterator();
        while (it3.hasNext())
        {
            BillSubTaxItem billKotItem = (BillSubTaxItem) it3.next();
            String pre = getSpaceFormat(billKotItem.getTxName(), String.valueOf(billKotItem.getPercent()), String.valueOf(billKotItem.getPrice()),36);
            tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", pre, TextAlign.LEFT,getApplicationContext()));
        }
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", getSpaceFormat("Total Service", "", item.getTotalServiceTaxAmount(),36), TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", "======================================", TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", getSpaceFormat("TOTAL  : ","", String.valueOf(item.getNetTotal()),34), TextAlign.CENTER,getApplicationContext()));
        tmpList.add(CreateBitmap.AddBlank(1));
        tmpList.add(CreateBitmap.AddBlank(1));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", item.getFooterLine(), TextAlign.CENTER,getApplicationContext()));
        //tmpList.add(CreateBitmap.AddBlank(1));
        //tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Hope you will visit Again,", TextAlign.CENTER,getApplicationContext()));
        tmpList.add(CreateBitmap.AddBlank(5));
        return tmpList;
    }

    private List<String[]> getPrintReport(ArrayList<ArrayList<String>> itemReport, String reportName) {
        List<String[]> tmpList = new ArrayList<String[]>();
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", reportName, TextAlign.CENTER,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(17, "Bold","MONOSPACE", "========================================", TextAlign.LEFT,getApplicationContext()));
        //Iterator itRow = itemReport.iterator();
        //tmpList.add(CreateBitmap.AddText(17, "Bold","MONOSPACE", "0123456789012345678901234567890123456789555555555", TextAlign.LEFT,getApplicationContext()));
        for(int j=0;j<itemReport.size();j++)
        {
            ArrayList<String> arrayListColumn = itemReport.get(j);
            StringBuffer sb = new StringBuffer();
            for (int i=0;i<arrayListColumn.size();i++)
            {
                String str = arrayListColumn.get(i);
                String preTxt = getAbsoluteCharacter(String.valueOf(str),10);
                if(j==0)
                    sb.append(preTxt+" |");
                else
                    sb.append(" "+preTxt+" ");
                int rem = i%3;
                if(rem == 0 && i!=0)
                {
                    tmpList.add(CreateBitmap.AddText(17, "Bold","MONOSPACE", sb.toString(), TextAlign.LEFT,getApplicationContext()));
                    sb = new StringBuffer();
                }

                if(rem != 0 && (arrayListColumn.size()-1)==i)
                {
                    tmpList.add(CreateBitmap.AddText(17, "Bold","MONOSPACE", getAbsoluteCharacter(sb.toString()), TextAlign.LEFT,getApplicationContext()));
                    sb = new StringBuffer();
                }
                /*else if(i == arrayListColumn.size())
                {
                    tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", sb.toString(), TextAlign.LEFT,getApplicationContext()));
                    sb = new StringBuffer();
                }*/
            }
            if(j==0)
                tmpList.add(CreateBitmap.AddText(17, "Bold","MONOSPACE", "========================================", TextAlign.LEFT,getApplicationContext()));
        }
        tmpList.add(CreateBitmap.AddText(17, "Bold","MONOSPACE", "========================================", TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddBlank(5));
        return tmpList;
    }

    private String getAbsoluteCharacter(String str, int num) {
        String strToDo = "";
        if(str.length() > num)
        {
            strToDo = str.substring(0,num);
        }
        else
        {
            strToDo = str;
        }
        String preTxt = getPostAddedSpaceFormat("", String.valueOf(strToDo),num);
        return preTxt;
    }

    private String getAbsoluteCharacter(String str) {
        return getSpaces(42-str.length())+str;
    }

    private String getSpaceFormat(String txtPre, String txtPercent, String txtPost, int num) {
        return txtPre+getSpaces(num-(txtPre.length()+txtPercent.length()+txtPost.length()))+txtPost+txtPercent;
    }

    public List<String[]> getTestPrint() {
        List<String[]> tmpList = new ArrayList<String[]>();
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Test Print Success!", TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "    ", TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGH", TextAlign.LEFT,getApplicationContext()));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGH", TextAlign.CENTER,getApplicationContext()));
        tmpList.add(CreateBitmap.AddBlank(5));
        return tmpList;
    }
    private String getFormatedCharacterForPrint(String txt, int limit) {
        if(txt.length()<limit){
            return txt+getSpaces(limit-txt.length());
        }else {
            return txt.substring(0,limit);
        }
    }

    public String getSpaces(int num)
    {
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<num;i++)
        {
            sb.append(getResources().getString(R.string.superSpace));
        }
        return sb.toString();
    }

    public String getPreAddedSpaceFormat(String sourceTxt, String toAddTxt, int max)
    {
        return sourceTxt+getSpaces(max-(sourceTxt.length()+toAddTxt.length()))+toAddTxt;
    }

    public String getPostAddedSpaceFormat(String sourceTxt, String toAddTxt, int max)
    {
        return sourceTxt+toAddTxt+getSpaces(max-(sourceTxt.length()+toAddTxt.length()));
    }
}
