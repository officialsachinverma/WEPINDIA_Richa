package com.wepindia.printers;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.io.GpDevice;
import com.gprinter.io.PortParameters;
import com.gprinter.save.PortParamDataBase;
import com.gprinter.service.GpPrintService;
import com.wep.common.app.WepBaseActivity;
import com.wep.common.app.print.Payment;
import com.wep.common.app.print.PrintIngredientsModel;
import com.wep.common.app.print.PrintKotBillItem;
import com.wepindia.printers.heydey.BluetoothDeviceList;
import com.wepindia.printers.heydey.Util;
import com.wepindia.printers.utils.PrinterUtil;

import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public abstract class HeyDeyBaseActivity extends WepBaseActivity implements View.OnClickListener {

    private GpService mGpService = null;
    private static final String DEBUG_TAG = HeyDeyBaseActivity.class.getSimpleName();
    public static final String ACTION_CONNECT_STATUS = "action.connect.status";
    private HeyDeyBaseActivity.PrinterServiceConnection conn = null;
    private PortParameters mPortParam = null;
    int printerNum = 0;
    private String printType = "";
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static final int REQUEST_ENABLE_BT = 2;
    public static final int REQUEST_CONNECT_DEVICE = 3;
    public static final int REQUEST_USB_DEVICE = 4;
    private int code = 0;
    private String name = "priyabrat";
    private PrintKotBillItem item;
    private String tmpList;
    private ArrayList<ArrayList<String>> itemReport;
    protected PrinterUtil printerUtil;

    public abstract void onConfigurationRequired();

    public abstract void onPrinterAvailable();

    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("ServiceConnection", "onServiceDisconnected() called");
            mGpService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
            checkPrinterConfig(false);
        }
    }

    ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printerUtil = new PrinterUtil(HeyDeyBaseActivity.this);
        connection();
        registerBroadcast();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            Log.d(DEBUG_TAG, "requestCode" + requestCode + '\n' + "resultCode" + resultCode);
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_ENABLE_BT) {
                if (resultCode == Activity.RESULT_OK) {
                    // bluetooth is opened , select bluetooth device fome list
                    Intent intent = new Intent(HeyDeyBaseActivity.this, BluetoothDeviceList.class);
                    startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
                } else {
                    // bluetooth is not open
                    Toast.makeText(this, R.string.bluetooth_is_not_enabled, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_USB_DEVICE) {
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(EXTRA_DEVICE_ADDRESS);
                    mPortParam.setUsbDeviceName(address);
                }
            } else if (requestCode == REQUEST_CONNECT_DEVICE) {
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(EXTRA_DEVICE_ADDRESS);
                    mPortParam.setBluetoothAddr(address);
                    mPortParam.setPortType(4/*mPortParam.getPortType()*/); // 4 For Bluetooth Connectivity
                    mPortParam.setIpAddr(mPortParam.getIpAddr());
                    mPortParam.setPortNumber(mPortParam.getPortNumber());
                    mPortParam.setUsbDeviceName(mPortParam.getUsbDeviceName());
                    if (CheckPortParamters(mPortParam)) {
                        PortParamDataBase database = new PortParamDataBase(this);
                        database.deleteDataBase("" + printerNum);
                        database.insertPortParam(printerNum, mPortParam);
                        // Write Print code
                        //Toast.makeText(this, "Value inserted", Toast.LENGTH_SHORT).show();
                        if (printType.equalsIgnoreCase("TEST")) {
                            Intent intent = new Intent();
                            intent.putExtra("code", code);
                            intent.putExtra("name", name);
                            intent.putExtra("printer", printerNum);
                            setResult(Activity.RESULT_OK, intent);
                            //finish();

                        }
                        try{
                            connectOrDisConnectToDevice();
                        }catch (DeadObjectException e){

                        }
                    } else {
                        messageBox(getString(R.string.port_parameters_wrong));
                    }

                } else {
                    messageBox(getString(R.string.port_parameters_is_not_save));
                }
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
    }

    public void askForConfig() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (bluetoothAdapter == null) {
            messageBox("Bluetooth is not supported by the device");
        } else {
            // If BT is not on, request that it be enabled.
            // setupChat() will then be called during onActivityResult
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            } else {
                Intent intent = new Intent(HeyDeyBaseActivity.this, BluetoothDeviceList.class);
                startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.e(DEBUG_TAG, "onDestroy ");
        super.onDestroy();
        this.unregisterReceiver(PrinterStatusBroadcastReceiver);
        if (conn != null) {
            unbindService(conn);
        }
    }

    private void connection() {
        conn = new HeyDeyBaseActivity.PrinterServiceConnection();
        Intent intent = new Intent("com.gprinter.aidl.GpPrintService");
        intent.setPackage(this.getPackageName());
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
    }

    private void checkPrinterConfig(boolean isErrorCorrection) {
        String str = new String();
        boolean[] state = getConnectState();
        PortParamDataBase database = new PortParamDataBase(this);
        mPortParam = new PortParameters();
        mPortParam = database.queryPortParamDataBase("" + printerNum);
        mPortParam.setPortOpenState(state[printerNum]);
        if (mPortParam.getBluetoothAddr().equalsIgnoreCase("") || state[printerNum] == false) {
            if (mPortParam.getBluetoothAddr().equalsIgnoreCase("")) {
                // Printer Not Configured
                str = "Configure";
                //btnConfig.setText(str);
                onConfigurationRequired();
            } else if (state[printerNum] == false) {
                // Printer port not connected
                str = "Connect";
                //btnConfig.setText(str);
                //btnConnect.setVisibility(View.GONE);
                try{
                    connectOrDisConnectToDevice();
                }catch (DeadObjectException e){

                }
            }
        } else {
            try {
                int status = mGpService.queryPrinterStatus(printerNum, 500);

                if (status == GpCom.STATE_NO_ERR) {
                    //"The printer is OK";
                    str = "Disconnect";
                    //btnConfig.setText(str);
                    //btnConnect.setVisibility(View.VISIBLE);
                    if (printType.equalsIgnoreCase("TEST")) {
                        Intent intent = new Intent();
                        intent.putExtra("code", code);
                        intent.putExtra("name", name);
                        intent.putExtra("printer", printerNum);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                    setResultt();
                } else {
                    str = "printer ";
                    if ((byte) (status & GpCom.STATE_OFFLINE) > 0) {
                        str += "Offline";
                    }
                    if ((byte) (status & GpCom.STATE_PAPER_ERR) > 0) {
                        str += "Out of paper";
                    }
                    if ((byte) (status & GpCom.STATE_COVER_OPEN) > 0) {
                        str += "The printer is opened";
                    }
                    if ((byte) (status & GpCom.STATE_ERR_OCCURS) > 0) {
                        str += "Printer error";
                    }
                    if ((byte) (status & GpCom.STATE_TIMES_OUT) > 0) {
                        str += "The query timed out";
                    }
                }
                if (isErrorCorrection && status == 0) {
                    printReceiptPrint();
                }
                //Toast.makeText(this, "printer：" + printerNum + " status：" + str, Toast.LENGTH_SHORT).show();
            } catch (RemoteException e1) {
                Toast.makeText(this, "exception", Toast.LENGTH_SHORT).show();
                e1.printStackTrace();
            }
        }
    }

    public boolean[] getConnectState() {
        boolean[] state = new boolean[GpPrintService.MAX_PRINTER_CNT];
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            state[i] = false;
        }
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            try {
                if (mGpService.getPrinterConnectStatus(i) == GpDevice.STATE_CONNECTED) {
                    state[i] = true;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return state;
    }

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CONNECT_STATUS);
        this.registerReceiver(PrinterStatusBroadcastReceiver, filter);
    }

    private BroadcastReceiver PrinterStatusBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_CONNECT_STATUS.equals(intent.getAction())) {
                int type = intent.getIntExtra(GpPrintService.CONNECT_STATUS, 0);
                int id = intent.getIntExtra(GpPrintService.PRINTER_ID, 0);
                Log.d(DEBUG_TAG, "connect status " + type);
                if (type == GpDevice.STATE_CONNECTING) {
                    //btnConfig.setText("Connecting...");

                } else if (type == GpDevice.STATE_NONE) {
                    //btnConfig.setText("Connect");
                    //btnConnect.setVisibility(View.GONE);
                    /*try{
                        connectOrDisConnectToDevice();
                    }catch (DeadObjectException e){

                    }*/
                } else if (type == GpDevice.STATE_VALID_PRINTER) {
                    //btnConfig.setText("Disconnect");
                    //btnConnect.setVisibility(View.VISIBLE);
                    //connectOrDisConnectToDevice();
                    //setResultt();
                    onPrinterAvailable();

                } else if (type == GpDevice.STATE_INVALID_PRINTER) {
                    messageBox("Please use Gprinter!");
                }
            }
        }
    };

    public void setResultt() {
        if (printType.equalsIgnoreCase("TEST")) {
            printReceiptPrint();
        }
    }

    public void connectOrDisConnectToDevice() throws DeadObjectException {
        int rel = 0;
        if (mPortParam.getPortOpenState() == false) {
            if (CheckPortParamters(mPortParam)) {
                switch (mPortParam.getPortType()) {
                    case PortParameters.USB:
                        try {
                            rel = mGpService.openPort(printerNum, mPortParam.getPortType(), mPortParam.getUsbDeviceName(), 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case PortParameters.ETHERNET:
                        try {
                            rel = mGpService.openPort(printerNum, mPortParam.getPortType(), mPortParam.getIpAddr(), mPortParam.getPortNumber());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case PortParameters.BLUETOOTH:
                        try {
                            rel = mGpService.openPort(printerNum, mPortParam.getPortType(), mPortParam.getBluetoothAddr(), 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
                GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
                if (r != GpCom.ERROR_CODE.SUCCESS) {
                    if (r == GpCom.ERROR_CODE.DEVICE_ALREADY_OPEN) {
                        mPortParam.setPortOpenState(true);
                        //btnConfig.setText("Disconnect");
                        //btnConnect.setVisibility(View.VISIBLE);
                        if (printType.equalsIgnoreCase("TEST")) {
                            Intent intent = new Intent();
                            intent.putExtra("code", code);
                            intent.putExtra("name", name);
                            intent.putExtra("printer", printerNum);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                        setResultt();
                    } else {
                        messageBox(GpCom.getErrorText(r));
                    }
                }
            } else {
                messageBox(getString(R.string.port_parameters_wrong));
            }
        } else {
            Log.d(DEBUG_TAG, "Disconnecting to Device ");
            //btnConfig.setText("Disconnecting");
            try {
                mGpService.closePort(printerNum);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    Boolean CheckPortParamters(PortParameters param) {
        boolean rel = false;
        int type = param.getPortType();
        if (type == PortParameters.BLUETOOTH) {
            if (!param.getBluetoothAddr().equals("")) {
                rel = true;
            }
        } else if (type == PortParameters.ETHERNET) {
            if ((!param.getIpAddr().equals("")) && (param.getPortNumber() != 0)) {
                rel = true;
            }
        } else if (type == PortParameters.USB) {
            if (!param.getUsbDeviceName().equals("")) {
                rel = true;
            }
        }
        return rel;
    }

    private void messageBox(String err) {
        Toast.makeText(getApplicationContext(), err, Toast.LENGTH_SHORT).show();
    }

    public void printReceiptPrint() {
        try {
            int type = mGpService.getPrinterCommandType(printerNum);
            if (type == GpCom.ESC_COMMAND) {
                int status = mGpService.queryPrinterStatus(printerNum, 500);
                if (status == GpCom.STATE_NO_ERR)
                {
                    if (printType.equalsIgnoreCase("TEST")) {
                        testPrint();
                    } else {
                        int rel;
                        try {
                            rel = mGpService.sendEscCommand(printerNum, tmpList);
                            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
                            if (r != GpCom.ERROR_CODE.SUCCESS) {
                                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else if(status == GpCom.STATE_OFFLINE)
                {
                    Toast.makeText(this, "Printer Offline", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Toast.makeText(getApplicationContext(), "Printer error!", Toast.LENGTH_SHORT).show();
                    try {
                        checkPrinterConfig(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    void testPrint() {
        EscCommand esc = new EscCommand();
        esc.addPrintAndFeedLines((byte) 3);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        esc.addText("Sample\n");
        esc.addPrintAndLineFeed();

        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText("Print text\n");
        esc.addText("Welcome to use Gprinter!\n");

        String message = Util.SimToTra("佳博票据打印机\n");
        esc.addText(message, "GB2312");
        esc.addPrintAndLineFeed();

        esc.addText("Print bitmap!\n");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gprinter);
        esc.addRastBitImage(b, b.getWidth(), 0);

        esc.addText("Print code128\n");
        esc.addSelectPrintingPositionForHRICharacters(EscCommand.HRI_POSITION.BELOW);
        esc.addSetBarcodeHeight((byte) 60);
        esc.addCODE128("Gprinter");
        esc.addPrintAndLineFeed();


        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addText("Completed!\r\n");
        esc.addPrintAndFeedLines((byte) 2);

        Vector<Byte> datas = esc.getCommand();
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendEscCommand(printerNum, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void printHeydeyKOT(PrintKotBillItem item, String type) {
        printType = type;
        tmpList = printerUtil.getPrintKOT(item);
        printReceiptPrint();
    }

    public void printHeydeyBILL(PrintKotBillItem item, String type) {
        printType = type;
        tmpList = printerUtil.getPrintBill(item);
        printReceiptPrint();
    }

    public void printHeydeyReport(ArrayList<ArrayList<String>> Report, String ReportName, String type) {
        printType = type;
        String reportName = ReportName;
        itemReport = Report;
        tmpList = printerUtil.getPrintReport(itemReport, reportName);
        printReceiptPrint();
    }

    public void printHeydeyPayment(Payment item, String ReportName, String type) {
        printType = type;
        String reportName = ReportName;
        tmpList = printerUtil.getPrintMSwipePaymentBill(item, reportName);
        printReceiptPrint();
    }

    public void printHeydeyIngredients(ArrayList<PrintIngredientsModel> item, String type) {
        printType = type;
        tmpList = printerUtil.getPrintIngredients(item);
        printReceiptPrint();
    }
    public void printTest() {
        testPrint();
        ;
    }
}