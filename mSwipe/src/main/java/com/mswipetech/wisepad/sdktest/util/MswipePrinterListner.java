package com.mswipetech.wisepad.sdktest.util;

import java.util.ArrayList;
import java.util.Hashtable;

import android.bluetooth.BluetoothDevice;

import com.bbpos.simplyprint.SimplyPrintController.BatteryStatus;
import com.bbpos.simplyprint.SimplyPrintController.Error;
import com.bbpos.simplyprint.SimplyPrintController.PrinterResult;

public class MswipePrinterListner {
	 public static enum PRINTER_STATE
     {
		  CONNECTED, DISCONNECTED, WAITINGFORCONNECTION, PRINTING, UNKNOWN
     }
	 
	 public static enum PRINTER_CONNECTION_ERROR
     {
		  BLUETOOTH_OFF, NO_PAIRED_DEVICE_FOUND, MULTIPLE_PAIRED_DEVICE,CONNECTING, UNKNOWN,WISEPAD_SWITCHED_OFF, PRINTING_IN_PROGRESS
     }
	 
	 /**
	  * a call back method that get the the current state of the printer.
	  * @param state
	  */
	 public void onPrinterStateChanged(PRINTER_STATE state){};
	 
	 /**
	  * a call back method for all error related to printer connection
	  * @param error will contain the error type.
	  * @param devices contains all the paired simply printer list that one can use in case multiple printers found to select one.
	  */
	 public void onPrinterConnectionError(PRINTER_CONNECTION_ERROR error,ArrayList<BluetoothDevice> devices){};
	 public void onRegisterd(){};
	 public void onUnRegisterd(){};
	 public void onError(Error error){};
	 public void onReturnDeviceInfo(Hashtable<String, String> deviceInfoTable) {};
	 public void onReturnPrinterResult(PrinterResult printerResult) {};
	 public void onPrinterOperationEnd() {};
	 public void onBatteryLow(BatteryStatus batteryStatus) {};
}
