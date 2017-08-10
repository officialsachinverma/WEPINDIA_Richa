package com.wepindia.printers.heydey;


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
import com.gprinter.io.GpDevice;
import com.gprinter.io.PortParameters;
import com.gprinter.save.PortParamDataBase;
import com.gprinter.service.GpPrintService;
import com.wep.common.app.print.BillKotItem;
import com.wep.common.app.print.BillTaxItem;
import com.wep.common.app.print.Payment;
import com.wep.common.app.print.PrintKotBillItem;
import com.wepindia.printers.R;

import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public class PrinterFragment extends Activity implements View.OnClickListener {

    private GpService mGpService= null;
    private static final String DEBUG_TAG = PrinterFragment.class.getSimpleName();
    public static final String ACTION_CONNECT_STATUS = "action.connect.status";
    private PrinterServiceConnection conn = null;
    private Button btnConnect,btnConfig;
    private PortParameters mPortParam = null;
    int printerNum = 0;
    private String printType = "";

    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static final int REQUEST_ENABLE_BT = 2;
    public static final int REQUEST_CONNECT_DEVICE = 3;
    public static final int REQUEST_USB_DEVICE = 4;
    private int code;
    private String name;
    private PrintKotBillItem item;
    private String tmpList;
    private ArrayList<ArrayList<String>> itemReport;

    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("ServiceConnection", "onServiceDisconnected() called");
            mGpService = null;
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService =GpService.Stub.asInterface(service);
            checkPrinterConfig();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_printer);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnConnect.setVisibility(View.GONE);
        btnConnect.setOnClickListener(this);
        btnConfig = (Button) findViewById(R.id.btnConfig);
        btnConfig.setOnClickListener(this);
        connection();
        registerBroadcast();
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
                //payment = (Payment) intent.getSerializableExtra("printData");
                //tmpList = getPrintMSwipePaymentBill(payment,reportName);
            }
            else
            {
                code =  intent.getIntExtra("code",0);
                name = intent.getStringExtra("name");
                try{
                    printerNum = intent.getIntExtra("printerNum",0);
                }catch (Exception e){

                }
                //tmpList = getTestPrint();
            }
        }
        else
        {
            //tmpList = getTestPrint();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(DEBUG_TAG, "requestCode" + requestCode + '\n' + "resultCode" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                // bluetooth is opened , select bluetooth device fome list
                Intent intent = new Intent(PrinterFragment.this, BluetoothDeviceList.class);
                startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
            }
            else
            {
                // bluetooth is not open
                Toast.makeText(this, R.string.bluetooth_is_not_enabled, Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == REQUEST_USB_DEVICE)
        {
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                String address = data.getExtras().getString(EXTRA_DEVICE_ADDRESS);
                mPortParam.setUsbDeviceName(address);
            }
        }
        else if (requestCode == REQUEST_CONNECT_DEVICE)
        {
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                String address = data.getExtras().getString(EXTRA_DEVICE_ADDRESS);
                mPortParam.setBluetoothAddr(address);
                mPortParam.setPortType(4/*mPortParam.getPortType()*/); // 4 For Bluetooth Connectivity
                mPortParam.setIpAddr(mPortParam.getIpAddr());
                mPortParam.setPortNumber(mPortParam.getPortNumber());
                mPortParam.setUsbDeviceName(mPortParam.getUsbDeviceName());
                if (CheckPortParamters(mPortParam))
                {
                    PortParamDataBase database = new PortParamDataBase(this);
                    database.deleteDataBase("" + printerNum);
                    database.insertPortParam(printerNum, mPortParam);
                    // Write Print code
                    //Toast.makeText(this, "Value inserted", Toast.LENGTH_SHORT).show();
                    if(printType.equalsIgnoreCase("TEST"))
                    {
                        Intent intent = new Intent();
                        intent.putExtra("code",code);
                        intent.putExtra("name",name);
                        intent.putExtra("printer",printerNum);
                        setResult(Activity.RESULT_OK, intent);
                        //finish();

                    }
                    connectOrDisConnectToDevice();
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
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btnConfig)
        {
            if(btnConfig.getText().toString().equalsIgnoreCase("Connect"))
            {
                connectOrDisConnectToDevice();
            }
            else if(btnConfig.getText().toString().equalsIgnoreCase("Configure"))
            {
                // First Config
                askForConfig();
            }
            else if(btnConfig.getText().toString().equalsIgnoreCase("Disconnect"))
            {
                // Code to Disconnect
                connectOrDisConnectToDevice();
            }
        }
        else if(id == R.id.btnConnect)
        {
            printReceipt();
        }
    }

    private void askForConfig() {
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
                Intent intent = new Intent(PrinterFragment.this, BluetoothDeviceList.class);
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
        conn = new PrinterServiceConnection();
        Intent intent = new Intent("com.gprinter.aidl.GpPrintService");
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
    }

    private void checkPrinterConfig() {
        String str = new String();
        boolean[] state = getConnectState();
        PortParamDataBase database = new PortParamDataBase(this);
        mPortParam = new PortParameters();
        mPortParam = database.queryPortParamDataBase("" + printerNum);
        mPortParam.setPortOpenState(state[printerNum]);
        if(mPortParam.getBluetoothAddr().equalsIgnoreCase("") || state[printerNum]==false)
        {
            if(mPortParam.getBluetoothAddr().equalsIgnoreCase(""))
            {
                // Printer Not Configured
                str = "Configure";
                btnConfig.setText(str);
            }
            else if(state[printerNum]==false)
            {
                // Printer port not connected
                str = "Connect";
                btnConfig.setText(str);
                btnConnect.setVisibility(View.GONE);
                connectOrDisConnectToDevice();
            }
        }
        else
        {
            try {
                int status = mGpService.queryPrinterStatus(printerNum,500);

                if (status == GpCom.STATE_NO_ERR)
                {
                    //"The printer is OK";
                    str = "Disconnect";
                    btnConfig.setText(str);
                    btnConnect.setVisibility(View.VISIBLE);
                    if(printType.equalsIgnoreCase("TEST"))
                    {
                        Intent intent = new Intent();
                        intent.putExtra("code",code);
                        intent.putExtra("name",name);
                        intent.putExtra("printer",printerNum);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                    setResultt();
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
                Toast.makeText(this, "printer：" + printerNum+ " status：" + str, Toast.LENGTH_SHORT).show();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        }
    }
    public boolean[] getConnectState() {
        boolean[] state = new boolean[GpPrintService.MAX_PRINTER_CNT];
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++)
        {
            state[i] = false;
        }
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++)
        {
            try {
                if (mGpService .getPrinterConnectStatus(i) == GpDevice.STATE_CONNECTED) {
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
                if (type == GpDevice.STATE_CONNECTING)
                {
                    btnConfig.setText("Connecting...");

                }
                else if (type == GpDevice.STATE_NONE)
                {
                    btnConfig.setText("Connect");
                    btnConnect.setVisibility(View.GONE);
                    connectOrDisConnectToDevice();
                }
                else if (type == GpDevice.STATE_VALID_PRINTER)
                {
                    btnConfig.setText("Disconnect");
                    btnConnect.setVisibility(View.VISIBLE);
                    //connectOrDisConnectToDevice();
                    setResultt();

                }
                else if (type == GpDevice.STATE_INVALID_PRINTER)
                {
                    messageBox("Please use Gprinter!");
                }
            }
        }
    };

    public void setResultt()
    {
        if(printType.equalsIgnoreCase("TEST"))
        {
            printReceipt();
        }
    }

    public void connectOrDisConnectToDevice() {
        int  rel = 0 ;
        if (mPortParam.getPortOpenState() == false)
        {
            if (CheckPortParamters(mPortParam))
            {
                switch(mPortParam.getPortType()){
                    case  PortParameters.USB:
                        try {
                            rel = mGpService.openPort(printerNum, mPortParam.getPortType(), mPortParam.getUsbDeviceName(), 0);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    case PortParameters.ETHERNET:
                        try {
                            rel = mGpService.openPort(printerNum, mPortParam.getPortType(), mPortParam.getIpAddr(), mPortParam.getPortNumber());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    case PortParameters.BLUETOOTH:
                        try {
                            rel = mGpService.openPort(printerNum, mPortParam.getPortType(), mPortParam.getBluetoothAddr(), 0);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                GpCom.ERROR_CODE r= GpCom.ERROR_CODE.values()[rel];
                if(r != GpCom.ERROR_CODE.SUCCESS)
                {
                    if(r== GpCom.ERROR_CODE.DEVICE_ALREADY_OPEN)
                    {
                        mPortParam.setPortOpenState(true);
                        btnConfig.setText("Disconnect");
                        btnConnect.setVisibility(View.VISIBLE);
                        if(printType.equalsIgnoreCase("TEST"))
                        {
                            Intent intent = new Intent();
                            intent.putExtra("code",code);
                            intent.putExtra("name",name);
                            intent.putExtra("printer",printerNum);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                        setResultt();
                    }
                    else
                    {
                        messageBox(GpCom.getErrorText(r));
                    }
                }
            }
            else
            {
                messageBox(getString(R.string.port_parameters_wrong));
            }
        }
        else
        {
            Log.d(DEBUG_TAG, "Disconnecting to Device ");
            btnConfig.setText("Disconnecting");
            try {
                mGpService.closePort(printerNum);}
            catch (RemoteException e) {
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

    public void printReceipt() {
        try {
            int type = mGpService.getPrinterCommandType(printerNum);
            if (type == GpCom.ESC_COMMAND)
            {
                int status = mGpService.queryPrinterStatus(printerNum,500);
                if (status == GpCom.STATE_NO_ERR)
                {
                    if(printType.equalsIgnoreCase("TEST"))
                    {
                        testPrint();
                    }
                    else
                    {
                        int rel;
                        try {
                            rel = mGpService.sendEscCommand(printerNum, tmpList);
                            GpCom.ERROR_CODE r= GpCom.ERROR_CODE.values()[rel];
                            if(r != GpCom.ERROR_CODE.SUCCESS)
                            {
                                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Printer error!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    void testPrint(){
        EscCommand esc = new EscCommand();
        esc.addPrintAndFeedLines((byte)3);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        esc.addText("Sample\n");
        esc.addPrintAndLineFeed();

        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText("Print text\n");
        esc.addText("Welcome to use Gprinter!\n");

        String message = Util.SimToTra("佳博票据打印机\n");
        esc.addText(message,"GB2312");
        esc.addPrintAndLineFeed();

        esc.addText("Print bitmap!\n");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gprinter);
        esc.addRastBitImage(b,b.getWidth(),0);

        esc.addText("Print code128\n");
        esc.addSelectPrintingPositionForHRICharacters(EscCommand.HRI_POSITION.BELOW);
        esc.addSetBarcodeHeight((byte)60);
        esc.addCODE128("Gprinter");
        esc.addPrintAndLineFeed();


        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addText("Completed!\r\n");
        esc.addPrintAndFeedLines((byte)2);

        Vector<Byte> datas = esc.getCommand();
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendEscCommand(printerNum, str);
            GpCom.ERROR_CODE r= GpCom.ERROR_CODE.values()[rel];
            if(r != GpCom.ERROR_CODE.SUCCESS)
            {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public String getPrintKOT(PrintKotBillItem item) {
        EscCommand esc = new EscCommand();
        esc.addPrintAndFeedLines((byte)3);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        esc.addText("Table # "+item.getTableNo() +" | "+"KOT # "+item.getBillNo()+"\n");
        esc.addPrintAndLineFeed();

        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText("=============================================\n");
        esc.addText("Attandant : "+item.getOrderBy()+"\n");
        esc.addText("Date : "+item.getDate() +" | "+"Time : "+item.getTime()+"\n");
        esc.addText("=============================================\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addText("Lists\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText("=============================================\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText("Sl              NAME              QTY"+"\n");
        esc.addText("=============================================\n");
        ArrayList<BillKotItem> billKotItems = item.getBillKotItems();
        Iterator it = billKotItems.iterator();
        while (it.hasNext())
        {
            BillKotItem billKotItem = (BillKotItem) it.next();
            int id = billKotItem.getItemId();
            String name = getFormatedCharacterForPrint(billKotItem.getItemName(),10);
            String qty = billKotItem.getQty()+"";
            String pre = getPostAddedSpaceFormat("",String.valueOf(id),15)+name;
            esc.addText(getPreAddedSpaceFormat(pre,qty,38)+"\n");
        }
        esc.addText("=============================================\n");
        esc.addPrintAndFeedLines((byte)3);

        Vector<Byte> datas = esc.getCommand();
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        return str;
    }

    public String getPrintBill(PrintKotBillItem item) {
        EscCommand esc = new EscCommand();
        esc.addPrintAndFeedLines((byte)3);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        esc.addText("Resturant Bill"+"\n");
        esc.addText(item.getAddressLine1()+"\n");
        esc.addPrintAndLineFeed();

        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addText(item.getAddressLine2()+"\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addText(item.getAddressLine3()+"\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText("=============================================\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addText("Invoice no # "+item.getBillNo()+"\n");
        esc.addText("Table # "+item.getTableNo()+"\n");
        esc.addText("Date : "+item.getDate() +" | "+"Time : "+item.getTime()+"\n");
        esc.addText("Cashier   : "+item.getOrderBy()+"\n");
        esc.addText("Customer Name   : "+item.getCustomerName()+"\n");
        if(item.getBillingMode().equalsIgnoreCase("4")) {
            esc.addText("Payment Status   : " + item.getPaymentStatus()+"\n");
        }
        // -----------
        esc.addText("Dine In"+"\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText("ID   NAME        QTY   RATE    AMOUNT "+"\n");
        esc.addText("======================================"+"\n");
        ArrayList<BillKotItem> billKotItems = item.getBillKotItems();
        Iterator it = billKotItems.iterator();
        while (it.hasNext())
        {
            BillKotItem billKotItem = (BillKotItem) it.next();
            String preId = getPostAddedSpaceFormat("",String.valueOf(billKotItem.getItemId()),5);
            String preName = getPostAddedSpaceFormat("",getFormatedCharacterForPrint(String.valueOf(billKotItem.getItemName()),10),12);
            String preQty = getPostAddedSpaceFormat("",getFormatedCharacterForPrint(String.valueOf(billKotItem.getQty()),4),6);
            String preRate = getPostAddedSpaceFormat("",getFormatedCharacterForPrint(String.valueOf(billKotItem.getRate()),4),7);
            String preAmount = getPostAddedSpaceFormat("",String.valueOf(billKotItem.getAmount()),7);
            String pre = preId+preName+preQty+preRate+preAmount;
            esc.addText(pre+"\n");
        }
        esc.addText("======================================"+"\n");
        esc.addText(getSpaceFormat("Sub-Total",String.valueOf(item.getSubTotal()),36)+"\n");
        ArrayList<BillTaxItem> billTaxItems = item.getBillTaxItems();
        Iterator it1 = billTaxItems.iterator();
        while (it1.hasNext())
        {
            BillTaxItem billKotItem = (BillTaxItem) it1.next();
            String pre = getSpaceFormat(billKotItem.getTxName(),String.valueOf(billKotItem.getPrice()),36);
            esc.addText(pre+"\n");
        }
        esc.addText("======================================"+"\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addText(getSpaceFormat("Net Total",String.valueOf(item.getNetTotal()),36)+"\n");
        esc.addText(item.getFooterLine()+"\n");
        esc.addText("=============================================\n");
        esc.addPrintAndFeedLines((byte)3);

        Vector<Byte> datas = esc.getCommand();
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        return str;
    }

    private String getPrintReport(ArrayList<ArrayList<String>> itemReport, String reportName) {
        EscCommand esc = new EscCommand();
        esc.addPrintAndFeedLines((byte)3);
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addText(reportName+"\n");
        esc.addText("========================================"+"\n");
        esc.addPrintAndLineFeed();

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
                    esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
                    esc.addText(sb.toString()+"\n");
                    sb = new StringBuffer();
                }

                if(rem != 0 && (arrayListColumn.size()-1)==i)
                {
                    esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
                    esc.addText(getAbsoluteCharacter(sb.toString())+"\n");
                    sb = new StringBuffer();
                }
                /*else if(i == arrayListColumn.size())
                {
                    tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", sb.toString(), TextAlign.LEFT,getApplicationContext()));
                    sb = new StringBuffer();
                }*/
            }
            if(j==0)
                esc.addText("========================================"+"\n");
        }
        esc.addText("========================================"+"\n");
        esc.addPrintAndFeedLines((byte)3);
        Vector<Byte> datas = esc.getCommand();
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        return str;
    }

    private String getPrintMSwipePaymentBill(Payment payment, String reportName) {
        EscCommand esc = new EscCommand();
        esc.addPrintAndFeedLines((byte)3);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        esc.addText(reportName+"\n");
        esc.addText("======================================,"+"\n");
        esc.addText(String.valueOf(payment.getMerchantName())+"\n");
        esc.addText(String.valueOf(payment.getMerchantName())+"\n");
        esc.addText(String.valueOf(payment.getMerchantAdd())+"\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText("======================================,"+"\n");
        esc.addText("Date/Time     :"+String.valueOf(payment.getDateTime())+"\n");
        esc.addText("Invoice Id    :"+String.valueOf(payment.getInvoiceNo())+"\n");
        esc.addText("Card Name     : "+String.valueOf(payment.getCardHolderName())+"\n");
        esc.addText("Card Number   :"+String.valueOf(payment.getCardNo())+"\n");
        esc.addText("Expiry Date   : "+String.valueOf(payment.getExpDate())+"\n");
        esc.addText("Card Type     : "+String.valueOf(payment.getCardType())+"\n");
        esc.addText("Reference No  : "+String.valueOf(payment.getRefNo())+"\n");
        esc.addText("======================================"+"\n");
        esc.addText("Total         : "+String.valueOf(payment.getTotal_Pay_Amount())+"\n");
        Vector<Byte> datas = esc.getCommand();
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        return str;
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
        String preTxt = getPostAddedSpaceFormat("",String.valueOf(strToDo),num);
        return preTxt;
    }

    private String getAbsoluteCharacter(String str) {
        return getSpaces(42-str.length())+str;
    }

    private String getSpaceFormat(String txtPre, String txtPost, int num) {
        return txtPre+getSpaces(num-(txtPre.length()+txtPost.length()))+txtPost;
    }

    private String getFormatedCharacterForPrint(String txt,int limit) {
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
            sb.append(" ");
        }
        return sb.toString();
    }

    public String getPreAddedSpaceFormat(String sourceTxt, String toAddTxt,int max)
    {
        return sourceTxt+getSpaces(max-(sourceTxt.length()+toAddTxt.length()))+toAddTxt;
    }

    public String getPostAddedSpaceFormat(String sourceTxt, String toAddTxt,int max)
    {
        return sourceTxt+toAddTxt+getSpaces(max-(sourceTxt.length()+toAddTxt.length()));
    }
}
