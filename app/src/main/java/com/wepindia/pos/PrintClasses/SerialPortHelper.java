/****************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	SerialPortHelper
 * 
 * Purpose			:	
 * 
 * DateOfCreation	:	24-February-2013
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 ****************************************************************************/
package com.wepindia.pos.PrintClasses;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import android.util.Log;
import android_serialport_api.*;


/**
 * 
 * @author BharadwajB
 *
 */
public abstract class SerialPortHelper {
		
	private static final String TAG = "SerialPortHelper";
	// O1 -> for Device; S2 -> for PC checking
	private static final String SERIAL_PORT_PATH = "/dev/ttyO1";
	//private static final String SERIAL_PORT_PATH = "/dev/ttyS2";
	private static final int BAUD_RATE = 115200;	//9600;
	
	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadingThread mReadThread;
	
	public SerialPortHelper(){
		try {			
			/* Get Serial port object */
			mSerialPort = new SerialPort(new File(SERIAL_PORT_PATH), BAUD_RATE, 0);
			
			if(mSerialPort == null){
				Log.d(TAG, "Serial port is NULL");
			} else{
				Log.d(TAG, "Serial port is not NULL");
			}
			
			/* Get output and input stream */
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();

			/* Create a receiving thread */
			mReadThread = new ReadingThread();
			mReadThread.start();
			
		} catch (SecurityException e) {
			Log.d(TAG, "You do not have read/write permission to the serial port.");
		} catch (IOException e) {
			Log.d(TAG, "The serial port can not be opened for an unknown reason.");
		} catch (InvalidParameterException e) {
			Log.d(TAG, "Please configure serial port first");
		}
	}
	
	protected abstract void onDataReceived(final byte[] buffer, final int size);
	
	protected void CloseSerialPort(){
		Log.d(TAG, "Closing Serial Port");
		if (mReadThread != null)
			mReadThread.interrupt();
		if (mSerialPort != null) {
			mSerialPort.close();
		}
	}
	
	private class ReadingThread extends Thread {
		@Override
		public void run() {
			super.run();
			while(!isInterrupted()) {
				int size;
				try {
					byte[] buffer = new byte[1024];
					if (mInputStream == null) return;
					size = mInputStream.read(buffer);
					if (size > 0) {
						onDataReceived(buffer, size);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
	
}
