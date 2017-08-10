package com.wepindia.printers.heydey;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.io.PortParameters;
import com.gprinter.save.PortParamDataBase;
import com.gprinter.service.GpPrintService;
import com.wepindia.printers.R;

import org.apache.commons.lang.ArrayUtils;

import java.util.Vector;

public class HomeActivity extends Activity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private GpService mGpService= null;
    private HomeActivity.PrinterServiceConnection conn = null;
    private  int mPrinterIndex = 0;
    private  int mTotalCopies = 0;
    private int mPrinterId = 0;
    private Button btnPrintKot,btnPrintBill,btnPrintReport;
    private Intent intent;
    private String type;
    private static final int INTENT_PORT_SETTINGS = 0;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static final int REQUEST_ENABLE_BT = 2;
    public static final int REQUEST_CONNECT_DEVICE = 3;
    public static final int REQUEST_USB_DEVICE = 4;
    private PortParameters mPortParam[] = new PortParameters[GpPrintService.MAX_PRINTER_CNT];

    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected() called");
            mGpService = null;
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService =GpService.Stub.asInterface(service);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnPrintKot = (Button) findViewById(R.id.btnPrintKot);
        btnPrintBill = (Button) findViewById(R.id.btnPrintBill);
        btnPrintReport = (Button) findViewById(R.id.btnPrintReport);
        intent = getIntent();
        type = intent.getStringExtra("type");
        if(type.equalsIgnoreCase("kot"))
        {
            btnPrintKot.setVisibility(View.VISIBLE);
            btnPrintBill.setVisibility(View.GONE);
            btnPrintReport.setVisibility(View.GONE);
        }
        else if(type.equalsIgnoreCase("bill"))
        {
            btnPrintKot.setVisibility(View.GONE);
            btnPrintBill.setVisibility(View.VISIBLE);
            btnPrintReport.setVisibility(View.GONE);
        }
        else if(type.equalsIgnoreCase("report"))
        {
            btnPrintKot.setVisibility(View.GONE);
            btnPrintBill.setVisibility(View.GONE);
            btnPrintReport.setVisibility(View.VISIBLE);
        }
        connection();
        initPortParam();
    }

    private void connection() {
        conn = new HomeActivity.PrinterServiceConnection();
        Intent intent = new Intent("com.gprinter.aidl.GpPrintService");
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
    }

    public void testPrint(View view) {
        try {
            mTotalCopies = 0;
            int status = mGpService.queryPrinterStatus(mPrinterIndex,500);
            String str = new String();
            if (status == GpCom.STATE_NO_ERR)
            {
                str = "The printer is OK";
            }
            else
            {
                str = "printer ";
                if ((byte) (status & GpCom.STATE_OFFLINE) > 0) {
                    str  += "Offline";
                }
                if ((byte) (status & GpCom.STATE_PAPER_ERR) > 0) {
                    str += "Out of paper";
                }
                if ((byte) (status & GpCom.STATE_COVER_OPEN) > 0) {
                    str  += "The printer is opened";
                }
                if ((byte) (status & GpCom.STATE_ERR_OCCURS) > 0) {
                    str += "Printer error";
                }
                if ((byte) (status & GpCom.STATE_TIMES_OUT) > 0) {
                    str  += "The query timed out";
                }
            }
            Toast.makeText(getApplicationContext(), "printer：" + mPrinterIndex+ " status：" + str, Toast.LENGTH_SHORT).show();
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    public void printTestPageClicked(View view) {
        try {
            int rel = mGpService.printeTestPage(mPrinterIndex); //
            Log.i("ServiceConnection", "rel " + rel);
            GpCom.ERROR_CODE r= GpCom.ERROR_CODE.values()[rel];
            if(r != GpCom.ERROR_CODE.SUCCESS){
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    public void clickKot(View view) {
        mPrinterId = 0;
        /*Intent intent = new Intent(HomeActivity.this, PortConfigurationActivity.class);
        startActivityForResult(intent, INTENT_PORT_SETTINGS);*/
        mPortParam[mPrinterId].setPortType(PortParameters.BLUETOOTH);
        // Get local Bluetooth adapter
        try{
            int status = mGpService.queryPrinterStatus(mPrinterIndex,500);
            if (status == GpCom.STATE_NO_ERR)
            {
                sendReceipt();
            }
            else
            {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                // If the adapter is null, then Bluetooth is not supported
                if (bluetoothAdapter == null)
                {
                    messageBox("Bluetooth is not supported by the device");
                }
                else
                {
                    // If BT is not on, request that it be enabled.
                    // setupChat() will then be called during onActivityResult
                    if (!bluetoothAdapter.isEnabled())
                    {
                        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                    }
                    else
                    {
                        Intent intent = new Intent(HomeActivity.this, BluetoothDeviceList.class);
                        startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
                    }
                }
            }
        }catch (Exception e){
            Toast.makeText(this, "Exception", Toast.LENGTH_SHORT).show();
        }
    }

    private void messageBox(String err){
        Toast.makeText(getApplicationContext(), err,Toast.LENGTH_SHORT).show();
    }

    public void clickBill(View view) {
        mPrinterId = 1;
        Intent intent = new Intent(HomeActivity.this, PortConfigurationActivity.class);
        startActivityForResult(intent, INTENT_PORT_SETTINGS);
    }

    public void clickReport(View view) {
        mPrinterId = 2;
        Intent intent = new Intent(HomeActivity.this, PortConfigurationActivity.class);
        startActivityForResult(intent, INTENT_PORT_SETTINGS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "requestCode" + requestCode + '\n' + "resultCode" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_PORT_SETTINGS)
        {
            // getIP settings info from IP settings dialog
            if (resultCode == RESULT_OK) {
                Bundle bundle = new Bundle();
                bundle = data.getExtras();
                Log.d(TAG, "PrinterId " + mPrinterId);
                int param = bundle.getInt(GpPrintService.PORT_TYPE);
                mPortParam[mPrinterId].setPortType(param);
                Log.d(TAG, "PortType " + param);
                String str = bundle.getString(GpPrintService.IP_ADDR);
                mPortParam[mPrinterId].setIpAddr(str);
                Log.d(TAG, "IP addr " + str);
                param = bundle.getInt(GpPrintService.PORT_NUMBER);
                mPortParam[mPrinterId].setPortNumber(param);
                Log.d(TAG, "PortNumber " + param);
                str = bundle.getString(GpPrintService.BLUETOOT_ADDR);
                mPortParam[mPrinterId].setBluetoothAddr(str);
                Log.d(TAG, "BluetoothAddr " + str);
                str = bundle.getString(GpPrintService.USB_DEVICE_NAME);
                mPortParam[mPrinterId].setUsbDeviceName(str);
                Log.d(TAG, "USBDeviceName " + str);
                if (CheckPortParamters(mPortParam[mPrinterId]))
                {
                    PortParamDataBase database = new PortParamDataBase(this);
                    database.deleteDataBase("" + mPrinterId);
                    database.insertPortParam(mPrinterId, mPortParam[mPrinterId]);
                }
                else
                {
                    Util.messageBox(HomeActivity.this,getString(R.string.port_parameters_wrong));
                }

            } else {
                Util.messageBox(HomeActivity.this,getString(R.string.port_parameters_is_not_save));
            }
        }
        if (requestCode == REQUEST_ENABLE_BT)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                // bluetooth is opened , select bluetooth device fome list
                Intent intent = new Intent(HomeActivity.this, BluetoothDeviceList.class);
                startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
            }
            else
            {
                // bluetooth is not open
                Toast.makeText(this, R.string.bluetooth_is_not_enabled, Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == REQUEST_CONNECT_DEVICE)
        {
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                // Get the device MAC address
                String address = data.getExtras().getString(EXTRA_DEVICE_ADDRESS);
                // fill in some parameters
                /*tvPortInfo.setVisibility(View.VISIBLE);
                tvPortInfo.setText(getString(R.string.bluetooth_address)+address);
                btConnect.setVisibility(View.VISIBLE);*/
                mPortParam[mPrinterId].setBluetoothAddr(address);
                mPortParam[mPrinterId].setPortType(mPortParam[mPrinterId].getPortType());
                mPortParam[mPrinterId].setIpAddr(mPortParam[mPrinterId].getIpAddr());
                mPortParam[mPrinterId].setPortNumber(mPortParam[mPrinterId].getPortNumber());
                mPortParam[mPrinterId].setUsbDeviceName(mPortParam[mPrinterId].getUsbDeviceName());
                if (CheckPortParamters(mPortParam[mPrinterId]))
                {
                    PortParamDataBase database = new PortParamDataBase(this);
                    database.deleteDataBase("" + mPrinterId);
                    database.insertPortParam(mPrinterId, mPortParam[mPrinterId]);
                    // Write Print code
                    Toast.makeText(this, "Value inserted", Toast.LENGTH_SHORT).show();
                    connectOrDisConnectToDevice(mPrinterId);
                }
                else
                {
                    messageBox(getString(R.string.port_parameters_wrong));
                }

            }
            else
            {
                messageBox(getString(R.string.port_parameters_is_not_save));
            }
        }
        else if (requestCode == REQUEST_USB_DEVICE)
        {
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                // Get the device MAC address
                String address = data.getExtras().getString(EXTRA_DEVICE_ADDRESS);
                // fill in some parameters
                /*tvPortInfo.setVisibility(View.VISIBLE);
                tvPortInfo.setText(getString(R.string.usb_address)+address);
                btConnect.setVisibility(View.VISIBLE);*/
                mPortParam[2].setUsbDeviceName(address);
            }
        }
    }

    void connectOrDisConnectToDevice(final int PrinterId) {
        mPrinterId = PrinterId;
        int  rel = 0 ;
        if (mPortParam[PrinterId].getPortOpenState() == false) {
            if (CheckPortParamters(mPortParam[PrinterId])) {
                switch(mPortParam[PrinterId].getPortType()){
                    case  PortParameters.USB:
                        try {
                            rel = mGpService.openPort(PrinterId, mPortParam[PrinterId].getPortType(), mPortParam[PrinterId].getUsbDeviceName(), 0);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    case PortParameters.ETHERNET:
                        try {
                            rel = mGpService.openPort(PrinterId, mPortParam[PrinterId].getPortType(), mPortParam[PrinterId].getIpAddr(), mPortParam[PrinterId].getPortNumber());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    case PortParameters.BLUETOOTH:
                        try {
                            rel = mGpService.openPort(PrinterId, mPortParam[PrinterId].getPortType(), mPortParam[PrinterId].getBluetoothAddr(), 0);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                GpCom.ERROR_CODE r= GpCom.ERROR_CODE.values()[rel];
                if(r != GpCom.ERROR_CODE.SUCCESS){
                    if(r== GpCom.ERROR_CODE.DEVICE_ALREADY_OPEN)
                    {
                        mPortParam[PrinterId].setPortOpenState(true);
                        messageBox("SUCCESS -- "+ GpCom.getErrorText(r));
                        printReceipt();
                    }
                    else
                    {
                        messageBox("ERR -- "+ GpCom.getErrorText(r));
                    }
                }
                else
                {
                    new AsyncTask<Void,Void,Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            connectOrDisConnectToDevice(PrinterId);;
                        }
                    }.execute();
                }
            } else {
                messageBox(getString(R.string.port_parameters_wrong));
            }
        }
        else
        {
            Log.d(TAG, "DisconnectToDevice ");
            Toast.makeText(this, getString(R.string.cutting), Toast.LENGTH_SHORT).show();
            try {
                mGpService.closePort(PrinterId);}
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    void sendReceipt(){
        EscCommand esc = new EscCommand();
        esc.addPrintAndFeedLines((byte)3);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        esc.addText("Sample\n");
        esc.addPrintAndLineFeed();
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);//设置打印左对齐
        esc.addText("Print text\n");   //  打印文字
        esc.addText("Welcome to use Gprinter!\n");   //  打印文字
        String message = Util.SimToTra("佳博票据打印机\n");
        esc.addText(message,"GB2312");
        esc.addPrintAndLineFeed();
        esc.addText("Print bitmap!\n");   //  打印文字
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gprinter);
        esc.addRastBitImage(b,b.getWidth(),0);   //打印图片
        esc.addText("Print code128\n");   //  打印文字
        esc.addSelectPrintingPositionForHRICharacters(EscCommand.HRI_POSITION.BELOW);//设置条码可识别字符位置在条码下方
        esc.addSetBarcodeHeight((byte)60); //设置条码高度为60点
        esc.addCODE128("Gprinter");  //打印Code128码
        esc.addPrintAndLineFeed();
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);//设置打印左对齐
        esc.addText("Completed!\r\n");   //  打印结束
        esc.addPrintAndFeedLines((byte)8);
        Vector<Byte> datas = esc.getCommand(); //发送数据
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendEscCommand(mPrinterIndex, str);
            GpCom.ERROR_CODE r= GpCom.ERROR_CODE.values()[rel];
            if(r != GpCom.ERROR_CODE.SUCCESS){
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public void printReceipt() {
        /*try {
            int type = mGpService.getPrinterCommandType(mPrinterIndex);
            if (type == GpCom.ESC_COMMAND)
            {
                int status = mGpService.queryPrinterStatus(mPrinterIndex,500);
                if (status == GpCom.STATE_NO_ERR)
                {
                    *//*new AsyncTask<Void,Void,Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            sendReceipt();
                        }
                    }.execute();*//*
                    sendReceipt();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Printer error!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (RemoteException e1) {
            e1.printStackTrace();
        }*/

        try {
            mTotalCopies = 0;
            int status = mGpService.queryPrinterStatus(mPrinterIndex,500);
            String str = new String();
            if (status == GpCom.STATE_NO_ERR)
            {
                str = "The printer is OK";
                sendReceipt();
            }
            else
            {
                str = "printer ";
                if ((byte) (status & GpCom.STATE_OFFLINE) > 0) {
                    str  += "Offline";
                }
                if ((byte) (status & GpCom.STATE_PAPER_ERR) > 0) {
                    str += "Out of paper";
                }
                if ((byte) (status & GpCom.STATE_COVER_OPEN) > 0) {
                    str  += "The printer is opened";
                }
                if ((byte) (status & GpCom.STATE_ERR_OCCURS) > 0) {
                    str += "Printer error";
                }
                if ((byte) (status & GpCom.STATE_TIMES_OUT) > 0) {
                    str  += "The query timed out";
                }
            }
            Toast.makeText(getApplicationContext(), "printer：" + mPrinterIndex+ " status：" + str, Toast.LENGTH_SHORT).show();
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    private void initPortParam() {
        Intent intent = getIntent();
        boolean[] state = intent.getBooleanArrayExtra(MainActivity.CONNECT_STATUS);
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            PortParamDataBase database = new PortParamDataBase(this);
            mPortParam[i] = new PortParameters();
            mPortParam[i] = database.queryPortParamDataBase("" + i);
            mPortParam[i].setPortOpenState(state[i]);
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
}
