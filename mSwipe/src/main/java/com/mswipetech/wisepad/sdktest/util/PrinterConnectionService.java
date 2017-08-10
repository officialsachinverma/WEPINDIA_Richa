package com.mswipetech.wisepad.sdktest.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.bbpos.simplyprint.SimplyPrintController;
import com.bbpos.simplyprint.SimplyPrintController.BatteryStatus;
import com.bbpos.simplyprint.SimplyPrintController.Error;
import com.bbpos.simplyprint.SimplyPrintController.PrinterResult;
import com.bbpos.simplyprint.SimplyPrintController.SimplyPrintControllerListener;
import com.mswipetech.wisepad.R;
import com.mswipetech.wisepad.sdktest.util.MswipePrinterListner.PRINTER_CONNECTION_ERROR;
import com.mswipetech.wisepad.sdktest.util.MswipePrinterListner.PRINTER_STATE;
import com.mswipetech.wisepad.sdktest.view.ApplicationData;
import com.mswipetech.wisepad.sdktest.view.Logs;

public class PrinterConnectionService extends Service {
	private String log_tab = "PrinterConnectionService=>";

	private LocalBinder localBinder = new LocalBinder();
	
	public static final int MSG_REGISTER_CLIENT = 1;
	public static final int MSG_UNREGISTER_CLIENT = 2;
	public static final int MSG_GET_DEVICE_INFO_CLIENT = 3;
	public static final int MSG_PRINT_RECEIPT = 4;
	
	public final static int DEVCIE_NO_PAIRED_DEVICES = 2;
	public final static int DEVCIE_MULTIPLE_FRIENDLY_DEVICES_FOUND = 3;
	public final static int DEVICE_PAIREDDEVICE_MATCHING = 7;
	public final static int DEVICE_PAIREDDEVICE_MATCHING_MAC = 8;
	public final static int DEVICE_NO_PAIRED_DEVICES_CONNECTED = 9;
	public final static int DEVICE_BLUETOOTH_OFF = 10;
	
	private SimplyPrintController mSimplyPrintController;
	private ApplicationData applicationData;
	private int bluetoothConnectionState;
	private boolean wisePadDeviceConnecting;
	private boolean isPrintStarted =  false;
	private MswipePrinterListner mPrinterListner;
	ArrayList<BluetoothDevice> pairedDevicesFound = new ArrayList<BluetoothDevice>();
	byte[] data;
	
	PRINTER_STATE printerState = PRINTER_STATE.UNKNOWN;

	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		/*mMessenger = new Messenger(new IncomingHandler(this));*/
		applicationData = (ApplicationData)getApplicationContext();
		mSimplyPrintController = new SimplyPrintController(getApplicationContext(), new MySimplyPrintControllerListener());
		Log.d(getPackageName(), "onCreate");
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();		
		/*if(isPrinterConnected)
			mSimplyPrintController.stopBTv2();*/
		mPrinterListner = null;
		Log.d(getPackageName(), "onDestroy");
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return localBinder;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		
		/*if(isPrinterConnected)*/
		if(mSimplyPrintController != null)
			mSimplyPrintController.stopBTv2();
		
		return super.onUnbind(intent);
	}
	
	public void setPrinterListner(MswipePrinterListner printerListner){
		this.mPrinterListner = printerListner;
		mPrinterListner.onRegisterd();
		connectToWisePad();	
	}
	
	public void connectToDevice() {
		if (checkConnections()) {
			
		}else{
			if (bluetoothConnectionState == DEVCIE_MULTIPLE_FRIENDLY_DEVICES_FOUND) {

				setPrinterConnectionError(PRINTER_CONNECTION_ERROR.MULTIPLE_PAIRED_DEVICE);
			} else if (bluetoothConnectionState == DEVCIE_NO_PAIRED_DEVICES) {
				
				setPrinterConnectionError(PRINTER_CONNECTION_ERROR.NO_PAIRED_DEVICE_FOUND);
			}
		}
	}
	
	
	public void reconnectToDevice(){

		connectToDevice();
		
		// show the popup screen. only when if the bluetoothconnection state multiplepaired devcies
		if (bluetoothConnectionState == DEVCIE_MULTIPLE_FRIENDLY_DEVICES_FOUND) {

			setPrinterConnectionError(PRINTER_CONNECTION_ERROR.MULTIPLE_PAIRED_DEVICE);
		} else if (bluetoothConnectionState == DEVCIE_NO_PAIRED_DEVICES) {
			
			setPrinterConnectionError(PRINTER_CONNECTION_ERROR.NO_PAIRED_DEVICE_FOUND);
		}

	}
	
	
	/**
	 * this method will print the data.
	 * @param data data in the form of byte array that has to print
	 */
	public void printReceipt(byte[] data){
		this.data = data;
		if (!wisePadDeviceConnecting) 
		{
			if (!mSimplyPrintController.isDevicePresent()) 
			{
				setPrinterConnectionError(PRINTER_CONNECTION_ERROR.WISEPAD_SWITCHED_OFF);					
			} else {
				if(printerState != PRINTER_STATE.PRINTING)
					printReceipt();
				else
					setPrinterConnectionError(PRINTER_CONNECTION_ERROR.PRINTING_IN_PROGRESS);
			}
		} 
		else { // if the device is connecting in the back ground
				setPrinterConnectionError(PRINTER_CONNECTION_ERROR.CONNECTING);
				/*final Dialog dialog = Constants.showDialog(CreditSaleView.this, CARDSALE_DIALOG_MSG,
	                    Constants.CARDSALE_Device_Connecting_Msg, "", "Connect","Close");*/
				/*final Dialog dialog = Constants.showDialog(CreditSaleView.this, CARDSALE_DIALOG_MSG,
						getString(R.string.connecting_to_wisepad_if_its_taking_longer_than_usual_please_restart_the_wisepad_and_try_reconnecting), "1");*/
		}
		
	}	
	
	/**
	 * this method will try to connect to the paired devices. 
	 * @param device the bluetooth device.
	 */
	public void connectToWisePad(BluetoothDevice device){

		//setPriterState(MswipePrinterListner.PRINTER_STATE.WAITINGFORCONNECTION);
		if (ApplicationData.IS_DEBUGGING_ON)
			Logs.v(ApplicationData.packName,log_tab+ "Device not present connecting to device in the back ground",true, true);
		checkConnections(device);
	}
	
	/**
	 * the method will return device info.
	 */
	public void getDeviceInfo(){
		mSimplyPrintController.getDeviceInfo();
	}
	
	private void connectToWisePad()
	{
			
		//setPriterState(MswipePrinterListner.PRINTER_STATE.WAITINGFORCONNECTION);
		if (ApplicationData.IS_DEBUGGING_ON)
			Logs.v(ApplicationData.packName,log_tab+ "Device not present connecting to device in the back ground",true, true);
		
		if (!mSimplyPrintController.isDevicePresent() && !wisePadDeviceConnecting) {
			if (ApplicationData.IS_DEBUGGING_ON)
				Logs.v(ApplicationData.packName,log_tab+ "Device not present connecting to device in the back ground",true, true);
			// TODO Auto-generated method stub
			ConnectDeviceTask connecttask = new ConnectDeviceTask();
			connecttask.execute();
			
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
				
			
				
			} else {
				
				if (bluetoothConnectionState == DEVCIE_MULTIPLE_FRIENDLY_DEVICES_FOUND) {

					setPrinterConnectionError(PRINTER_CONNECTION_ERROR.MULTIPLE_PAIRED_DEVICE);
				} else if (bluetoothConnectionState == DEVCIE_NO_PAIRED_DEVICES) {
					
					setPrinterConnectionError(PRINTER_CONNECTION_ERROR.NO_PAIRED_DEVICE_FOUND);
				}
			}
			
		}
	}
	
	private boolean checkConnections() {
		
		bluetoothConnectionState = 0;
		pairedDevicesFound.clear();
		
		if (BluetoothAdapter.getDefaultAdapter() == null)
			return false;
		
		if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
			
			if (ApplicationData.IS_DEBUGGING_ON)
				Logs.v(getPackageName(), log_tab + " Switching Bluetooth on ", true, true);
			
			/*Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);*/
			
			if (ApplicationData.IS_DEBUGGING_ON)
				Logs.v(getPackageName(), log_tab + "Successfully Switched on the Bluetooth", true, true);
			bluetoothConnectionState = DEVICE_BLUETOOTH_OFF;
			setPrinterConnectionError(PRINTER_CONNECTION_ERROR.BLUETOOTH_OFF);
			return false;
			
		} else {
			
			if (ApplicationData.IS_DEBUGGING_ON)
				Logs.v(getPackageName(), log_tab + " Bluetooth  on ", true, true);
			
			SharedPreferences preferences;
			preferences = PreferenceManager.getDefaultSharedPreferences(this);
			String stBluetoothAddress = preferences.getString("bluetoothprinteraddress", ""); //34:C8:03:6D:2B:91
			
			if (ApplicationData.IS_DEBUGGING_ON)
				Logs.v(getPackageName(), log_tab + " stBluetoothAddress " + stBluetoothAddress, true, true);
			
			
			Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
			
			if (pairedDevices.size() > 0) {
				
				for (BluetoothDevice device : pairedDevices) {
					
					if (ApplicationData.IS_DEBUGGING_ON)
						Logs.v(ApplicationData.packName,log_tab+ "BluetoothDevice name "+ device.getName(),true, true);
					
					if (device.getName().toLowerCase().startsWith("btptr") 
							|| device.getName().startsWith("BTPTR")
							|| device.getName().toLowerCase().startsWith("sim")
							|| device.getName().startsWith("SIM")) {
						
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
				Toast.makeText(PrinterConnectionService.this, "More than one printer devices found", Toast.LENGTH_SHORT).show();
				bluetoothConnectionState = DEVCIE_MULTIPLE_FRIENDLY_DEVICES_FOUND;
				return false;
				
			} else if (pairedDevicesFound.size() == 1) {
				
				if (ApplicationData.IS_DEBUGGING_ON)
					Logs.v(ApplicationData.packName,log_tab+ "pairedDevicesFound " + pairedDevicesFound.get(0),true, true);
				
				wisePadDeviceConnecting = true;
				bluetoothConnectionState = DEVICE_PAIREDDEVICE_MATCHING;
				mSimplyPrintController.startBTv2(pairedDevicesFound.get(0));

				
				return true;
				
			} else {
				Toast.makeText(PrinterConnectionService.this, "No paried device found", Toast.LENGTH_SHORT).show();
				bluetoothConnectionState = DEVCIE_NO_PAIRED_DEVICES;
				return false;
			}
		}
	}
	
	private boolean checkConnections(BluetoothDevice device) {
		
		bluetoothConnectionState = 0;
		pairedDevicesFound.clear();
		
		if (BluetoothAdapter.getDefaultAdapter() == null)
			return false;
		
		if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
			
			if (ApplicationData.IS_DEBUGGING_ON)
				Logs.v(getPackageName(), log_tab + " Switching Bluetooth on ", true, true);
			
			/*Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);*/
			
			if (ApplicationData.IS_DEBUGGING_ON)
				Logs.v(getPackageName(), log_tab + "Successfully Switched on the Bluetooth", true, true);
			bluetoothConnectionState = DEVICE_BLUETOOTH_OFF;
			setPrinterConnectionError(PRINTER_CONNECTION_ERROR.BLUETOOTH_OFF);
			return false;
			
		} else {
			
			if (ApplicationData.IS_DEBUGGING_ON)
				Logs.v(getPackageName(), log_tab + " Bluetooth  on ", true, true);
			
			SharedPreferences preferences;
			preferences = PreferenceManager.getDefaultSharedPreferences(this);
			String stBluetoothAddress = preferences.getString("bluetoothprinteraddress", ""); //34:C8:03:6D:2B:91
			
			if (ApplicationData.IS_DEBUGGING_ON)
				Logs.v(getPackageName(), log_tab + " stBluetoothAddress " + stBluetoothAddress, true, true);
			
			wisePadDeviceConnecting = true;
			bluetoothConnectionState = DEVICE_PAIREDDEVICE_MATCHING;
			mSimplyPrintController.startBTv2(device);
			
			return true;
		}
	}
	
	private class DeviceConnectedWaitTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			try {
				int ictr = 0;
				while (ictr < 1) {
					Thread.sleep(500);
					ictr++;
				}
				
			} catch (Exception ex) {
			}
			
			return "";
		}
		
		@Override
		protected void onPostExecute(String result) {
			
			//btnPrint.setEnabled(true);
			//wisePadDeviceConnecting = false;
			
			wisePadDeviceConnecting = false;
			printReceipt();
			
		}
	}
	
	
	 private void printReceipt() {
		 	if(printerState != PRINTER_STATE.PRINTING)
		 	{
		 		if (applicationData.IS_DEBUGGING_ON)
		 			Log.v(getPackageName(), " printReceipt clicked " );
		 		printerState = printerState.PRINTING;
		 		isPrintStarted = true;
		 		mSimplyPrintController.startPrinting(1, 120, 120);		 		
		 	}else{
		 		setPrinterConnectionError(PRINTER_CONNECTION_ERROR.PRINTING_IN_PROGRESS);
		 	}
	    	
	}
	
	 class MySimplyPrintControllerListener implements SimplyPrintControllerListener {

			private boolean isonPrinterOperationEnd;
			private String mBlueToothMcId;

			@Override
			public void onBTv2Detected() {
			}

			@Override
			public void onBTv2Connected(BluetoothDevice bluetoothDevice) {
				
				setPriterState(MswipePrinterListner.PRINTER_STATE.CONNECTED);
				try {
					mBlueToothMcId = bluetoothDevice.getAddress();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				if (applicationData.IS_DEBUGGING_ON)
					Logs.v(getPackageName(), log_tab + "onBTv2Connected", true, true);
				
				SharedPreferences preferences;
				preferences = PreferenceManager.getDefaultSharedPreferences(PrinterConnectionService.this);
				preferences.edit().putString("bluetoothprinteraddress", mBlueToothMcId).commit();	
				
				wisePadDeviceConnecting = false;
				
				/*DeviceConnectedWaitTask deviceConnectedWaitTask = new DeviceConnectedWaitTask();
				deviceConnectedWaitTask.execute();*/
			}

			@Override
			public void onBTv2Disconnected() {
				setPriterState(MswipePrinterListner.PRINTER_STATE.DISCONNECTED);
				wisePadDeviceConnecting = false;
				isPrintStarted = false;
				bluetoothConnectionState = 0;
				
				if (applicationData.IS_DEBUGGING_ON)
					Logs.v(getPackageName(), log_tab + "onBTv2Disconnected  ", true, true);
				
				
			}

			@Override
			public void onBTv4DeviceListRefresh(List<BluetoothDevice> foundDevices) {
				
			
			}

			@Override
			public void onBTv4Connected() {
			}

			@Override
			public void onBTv4Disconnected() {
			}

			@Override
			public void onBTv4ScanStopped() {
			}

			@Override
			public void onBTv4ScanTimeout() {
			}

			@Override
			public void onReturnDeviceInfo(Hashtable<String, String> deviceInfoTable) {
				if(mPrinterListner != null)
					mPrinterListner.onReturnDeviceInfo(deviceInfoTable);
				
			}

			@Override
			public void onReturnPrinterResult(PrinterResult printerResult) {
				if(mPrinterListner != null)
					mPrinterListner.onReturnPrinterResult(printerResult);
				
				printerState = PRINTER_STATE.UNKNOWN;
				
				String msg = "";	        	
				if(printerResult == PrinterResult.SUCCESS) {
					msg =  "print success";
				} else if(printerResult == PrinterResult.NO_PAPER) {
					msg =  "No paper";
				} else if(printerResult == PrinterResult.WRONG_CMD) {
					msg =  "Wrong command";
				} else if(printerResult == PrinterResult.OVERHEAT) {
					msg =  "Printer over heat";
				}
				
				
				if (ApplicationData.IS_DEBUGGING_ON)
					Logs.v(getPackageName(), log_tab + " onReturnPrinterResult PrinterResult "+ msg, true, true);
				
//	        	Constants.showDialog(.this, title, msg, 1);

			}

			@Override
			public void onRequestPrinterData(int index, boolean isReprint) {
				if(data != null){
					setPriterState(MswipePrinterListner.PRINTER_STATE.PRINTING);
					String msg = "";
					
					mSimplyPrintController.sendPrinterData(data);
					if(isReprint) {
						msg =  "Request reprint data" + index;
					} else {
						msg =  "Request print data"+ index;
					}
				}
				
			}

			@Override
			public void onPrinterOperationEnd() {
				if(mPrinterListner != null)
					mPrinterListner.onPrinterOperationEnd();
				printerState = PRINTER_STATE.UNKNOWN;
				isonPrinterOperationEnd = true;
				isPrintStarted = false;
			}

			@Override
			public void onBatteryLow(BatteryStatus batteryStatus) {

				if(mPrinterListner != null)
					mPrinterListner.onBatteryLow(batteryStatus);
				String msg = "";
				
				if(batteryStatus == BatteryStatus.LOW) {
					msg =  getString(R.string.battery_low);
				} else if(batteryStatus == BatteryStatus.CRITICALLY_LOW) {
					msg =  getString(R.string.battery_critically_low);
				}
				
				try{
					
				//	Constants.showDialog(CreditSaleSignatureView.this, title, msg, 1);
					
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}

			@Override
			public void onBTv2DeviceNotFound() {
				if (applicationData.IS_DEBUGGING_ON)
					Logs.v(getPackageName(), log_tab + "onBTv2DeviceNotFound ", true, true);
			}

			@Override
			public void onError(Error errorState) {
				isPrintStarted = false;
				if(mPrinterListner != null)
					mPrinterListner.onError(errorState);
				
				String msg = "";
				if (applicationData.IS_DEBUGGING_ON)
					Logs.v(getPackageName(), log_tab + "onError " + errorState, true, true);
			
				if(errorState == Error.UNKNOWN) {
					msg =  getString(R.string.unknown_error);
				} else if(errorState == Error.CMD_NOT_AVAILABLE) {
					msg =  getString(R.string.command_not_available);
				} else if(errorState == Error.TIMEOUT) {
					msg =  getString(R.string.device_no_response);
				} else if(errorState == Error.DEVICE_BUSY) {
					msg =  getString(R.string.device_busy);
				} else if(errorState == Error.INPUT_OUT_OF_RANGE) {
					msg =  getString(R.string.out_of_range);
				} else if(errorState == Error.INPUT_INVALID) {
					msg =  getString(R.string.input_invalid);
				} else if(errorState == Error.CRC_ERROR) {
					msg =  getString(R.string.crc_error);
				} else if(errorState == Error.FAIL_TO_START_BTV2) {
					wisePadDeviceConnecting = false;
					isPrintStarted = false;
					msg =  "fail to start btv2";
				} else if(errorState == Error.COMM_LINK_UNINITIALIZED) {
					wisePadDeviceConnecting = false;
					isPrintStarted = false;
					msg =  getString(R.string.comm_link_uninitialized);
				} else if(errorState == Error.BTV2_ALREADY_STARTED) {
					msg =  getString(R.string.bluetooth_already_started);
				}
				
				
				if (applicationData.IS_DEBUGGING_ON)
					Logs.v(getPackageName(), log_tab + "onError  the error state is " + errorState, true, true);

				try{
					//Constants.showDialog(CreditSaleSignatureView.this, title, msg, 1);
					
				}catch(Exception e){
					e.printStackTrace();
				}

	        

			}

			@Override
			public void onBTv2DeviceListRefresh(List<BluetoothDevice> arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onBTv2ScanStopped() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onBTv2ScanTimeout() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onReturnGetDarknessResult(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onReturnSetDarknessResult(boolean arg0) {
				// TODO Auto-generated method stub
				
			}
		}
	 
	 	public class LocalBinder extends Binder{
	 		public PrinterConnectionService getService(){
	 			return PrinterConnectionService.this;
	 		}
	 	}
	 	
	 	private void setPriterState(MswipePrinterListner.PRINTER_STATE state){
	 		if(mPrinterListner != null){
	 			mPrinterListner.onPrinterStateChanged(state);
	 		}
	 	}
	 	
	 	private void setPrinterConnectionError (MswipePrinterListner.PRINTER_CONNECTION_ERROR error){
	 		if(mPrinterListner != null){
	 			mPrinterListner.onPrinterConnectionError(error, pairedDevicesFound);
	 		}
	 	}
	 	
	 	
	 	
	
}
