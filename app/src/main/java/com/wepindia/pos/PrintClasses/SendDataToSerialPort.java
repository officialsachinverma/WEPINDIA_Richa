/****************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	SendDataToSerialPort
 * 
 * Purpose			:	
 * 
 * DateOfCreation	:	24-February-2013
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 ****************************************************************************/
package com.wepindia.pos.PrintClasses;

import java.io.IOException;

import android.util.Log;

/**
 * 
 * @author BharadwajB
 * 
 */
public class SendDataToSerialPort extends SerialPortHelper {
	
	private static final String TAG = "SendDataToSerialPort";
	
	SendingThread mSendThread;
	byte[] mBuffer;
	
	public SendDataToSerialPort(){
		super();
		
		mBuffer = new byte[26];
		
		mBuffer[0] = 0x04;
		for(int i=1; i<25; i++){
			mBuffer[i] = 0x3D;
		}
		mBuffer[25] = 0x0D;
	}
	
	public void Write(String Data){
		
		try {
			Log.v(TAG, "Print Data:"+ "\n" + Data);
			mBuffer = Data.getBytes();
			for(int p = 0; p < mBuffer.length; p++){
				if(mBuffer[p] == 0x0A){
					mBuffer[p] = (byte)0x0D;
				} 
				Log.v(TAG, "Buffer[" + p + "]:" + mBuffer[p]);
			}
			
			Log.v(TAG, "Starting Sending thread");
			if (mSerialPort != null) {
				Log.v(TAG, "Serial port is not NULL");
				mSendThread = new SendingThread();
				mSendThread.start();
			}
			
			// Clear the buffer
			//mBuffer = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.v(TAG, "Write Function exception");
			e.printStackTrace();
		}
	}
	
	public void Write(byte[] Data){
		
		try {
			//Log.v(TAG, "Print Data:"+ "\n" + Data);
			mBuffer = new byte[Data.length];
			mBuffer = Data;
			for(int p = 0; p < mBuffer.length; p++){
				
				Log.v(TAG, "Buffer[" + p + "]:" + mBuffer[p]);
			}
			
			Log.v(TAG, "Starting Sending thread");
			if (mSerialPort != null) {
				Log.v(TAG, "Serial port is not NULL");
				mSendThread = new SendingThread();
				mSendThread.start();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.v(TAG, "Write Function exception");
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onDataReceived(byte[] buffer, int size) {
		// ignore incoming data
	}
	
	public void Close(){
		Log.v(TAG, "Calling Serial Port close function");
		super.CloseSerialPort();
	}

	private class SendingThread extends Thread {
		@Override
		public void run() {
			try {
				Log.d(TAG, "Entered in Sending thread");
				if (mOutputStream != null) {
					Log.d(TAG, "Output stream is not null");
					mOutputStream.write(mBuffer);
				} else {
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();				
				return;
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
}
