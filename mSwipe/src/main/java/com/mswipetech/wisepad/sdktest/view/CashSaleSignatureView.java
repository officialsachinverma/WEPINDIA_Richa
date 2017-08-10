package com.mswipetech.wisepad.sdktest.view;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbpos.simplyprint.SimplyPrintController.BatteryStatus;
import com.bbpos.simplyprint.SimplyPrintController.Error;
import com.bbpos.simplyprint.SimplyPrintController.PrinterResult;
import com.mswipetech.wisepad.sdk.MswipeWisepadController;
import com.mswipetech.wisepad.R;
import com.mswipetech.wisepad.sdktest.data.AppPrefrences;
import com.mswipetech.wisepad.sdktest.util.MswipePrinterListner;
import com.mswipetech.wisepad.sdktest.util.PrinterConnectionService;
import com.mswipetech.wisepad.sdktest.util.PrinterConnectionService.LocalBinder;
import com.mswipetech.wisepad.sdktest.util.ReceiptUtility;
import com.mswipetech.wisepad.sdktest.view.CreditSaleSignatureView.PrinterListner;


public class CashSaleSignatureView extends BaseTitleActivity {
    public final static String log_tab = "CashSaleSignatureView=>";
    public HashMap<String, String> hashMember = new HashMap<String, String>();
	private static final int REQUEST_ENABLE_BT = 0;

    CustomProgressDialog mProgressActivity = null;
    SignatureView signatureView = null;


    String mInvoiceNo = "";
    String mVoucherNo = "";
    String mMID = "";
    String mTID = "";
    String mAmt = "";
    String mDate = "";


    String title = "";
    String displayMsg = "";
    float scale = 0;
    
    
    PrinterConnectionService mPrinterConnectionService;
	PrinterListner printerListner;
	boolean isBound = false;
	//for handling submit button,when print in progress.
	boolean isPrinterStarted = false;
	boolean isPrintRequestInQue = false;
	ArrayList<BluetoothDevice> pairedDevicesFound = new ArrayList<BluetoothDevice>();
	ArrayList<byte[]> receipts = null;
	boolean isUploadingSignature = false;
	Button btnPrint;

    ApplicationData applicationData = null;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creditsalesingnature);
        applicationData = (ApplicationData) getApplicationContext();


        Resources resources = this.getResources();
        scale = resources.getDisplayMetrics().density;


        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Intent intent = getIntent();
        title = (String) intent.getStringExtra("title");
        if (title == null)
            title = "Amex Card";

        mInvoiceNo = getIntent().getStringExtra("mInvoiceNo");
        mVoucherNo = getIntent().getStringExtra("mVoucherNo");
        mMID = getIntent().getStringExtra("mMID");
        mTID = getIntent().getStringExtra("mTID");
        mAmt = "AMT: " + Constants.Currency_Code + " " + getIntent().getStringExtra("mAmt");
        mDate = getIntent().getStringExtra("mDate");


        initViews();
        
        printerListner = new PrinterListner();


    }


    public void initViews() {
        ((TextView) findViewById(R.id.topbar_LBL_heading)).setText(title);
        ((TextView) findViewById(R.id.topbar_LBL_heading)).setTypeface(applicationData.font);


        LinearLayout lnrReciept = (LinearLayout) findViewById(R.id.creditsale_LNR_redceiptdetails);
        final RecieptView recieptView = new RecieptView(this);
        recieptView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        lnrReciept.addView(recieptView);

        //lnrSignature = (LinearLayout)findViewById(R.id.creditsale_LNR_signature);
        //signatureView = new SignatureView(this);
        signatureView = (SignatureView) findViewById(R.id.creditsale_View_signature);

        //signatureView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 140));

        //lnrSignature.addView(signatureView);
        //signatureView.bringToFront();
        
        
        btnPrint = (Button) findViewById(R.id.creditsale_BTN_printsignature);
    	btnPrint.setTypeface(applicationData.font);
    	btnPrint.setText(getString(R.string.print));

    	btnPrint.setOnClickListener(new OnClickListener()
    	{
			@Override
			public void onClick(View arg0)
			{
				if(signatureView.signatureDrawn)
					printReceipt();
				else
					Constants.showDialog(CashSaleSignatureView.this, title, "Please sign the receipt to proceed", 1);
	    	}
		});
    	

        Button btnSubmit = (Button) findViewById(R.id.creditsale_BTN_submitsignature);
        btnSubmit.setTypeface(applicationData.font);
        btnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    if (signatureView.signatureDrawn) {
                        Bitmap bitmapCreditSale = null;
                        Paint mPaintReciept = null;
                        mPaintReciept = new Paint();
                        mPaintReciept.setTextSize(getResources().getDimension(R.dimen.font_size));
                        // smooth's out the edges of what is being drawn
                        mPaintReciept.setAntiAlias(true);
                        mPaintReciept.setColor(Color.BLACK);

                        bitmapCreditSale = Bitmap.createBitmap(signatureView.mBitmap.getWidth() + 5, recieptView.mBitmapReciept.getHeight() + signatureView.mBitmap.getHeight() + 5,
                                Bitmap.Config.ARGB_8888); // this creates a MUTABLE bitmap

                        Canvas canvas = new Canvas(bitmapCreditSale);
                        canvas.drawColor(Color.WHITE);


                        int ypos = 0;
                        Rect sizetext = new Rect();
                        String strText = "";
                        Rect bounds = new Rect();
                        int yoffset = 2;

                        mPaintReciept.setTextSize((int) (12 * scale));
                        strText = "InvoiceNo: " + mInvoiceNo + " mVoucherNo: " + mVoucherNo;
                        mPaintReciept.getTextBounds(strText, 0, strText.length(), bounds);
                        ypos = bounds.height() + yoffset;
                        canvas.drawText(strText, 2, ypos, mPaintReciept);

                        strText = "DATE/TIME: " + mDate;
                        ypos = ypos + bounds.height() + yoffset;
                        canvas.drawText(strText, 2, ypos, mPaintReciept);

                        strText = "MID: " + mMID + " TID: " + mTID;
                        mPaintReciept.getTextBounds(strText, 0, strText.length(), sizetext);
                        ypos = ypos + sizetext.height() + yoffset;
                        canvas.drawText(strText, 2, ypos, mPaintReciept);

                        mPaintReciept.setTextSize((int) (14 * scale));
                        mPaintReciept.setTypeface(Typeface.DEFAULT_BOLD);
                        strText = mAmt;

                        mPaintReciept.getTextBounds(strText, 0, strText.length(), sizetext);
                        ypos = ypos + sizetext.height() + yoffset;
                        canvas.drawText(strText, 2, ypos, mPaintReciept);

                        canvas.drawBitmap(signatureView.mBitmap, 1f, recieptView.mBitmapReciept.getHeight(), null);





                        int newWidth = 360;
                        int newHeight = 180;
                        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth,newHeight, Bitmap.Config.ARGB_8888);

                        float ratioX = newWidth / (float) bitmapCreditSale.getWidth();
                        float ratioY = newHeight / (float) bitmapCreditSale.getHeight();
                        float middleX = newWidth / 2.0f;
                        float middleY = newHeight / 2.0f;

                        Matrix scaleMatrix = new Matrix();
                        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

                        Canvas scaleCanvas = new Canvas(scaledBitmap);
                        scaleCanvas.setMatrix(scaleMatrix);
                        scaleCanvas.drawBitmap(bitmapCreditSale, middleX - bitmapCreditSale.getWidth() / 2, middleY - bitmapCreditSale.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

                        int quality = 40;
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
                        byte[] encodedImage = os.toByteArray();


                        MswipeWisepadController wisepadController = new MswipeWisepadController(CashSaleSignatureView.this, AppPrefrences.getGateWayEnv(),null);
			 	          wisepadController.UploadCashSignature(SignatureHandler,
			 	            		Constants.Reference_Id, Constants.Session_Tokeniser, mInvoiceNo, mVoucherNo, encodedImage);
				            mProgressActivity = new CustomProgressDialog(CashSaleSignatureView.this, "Processing Signature");
	                        mProgressActivity.show();
			 	  

                    } else {
                        Constants.showDialog(CashSaleSignatureView.this, title, Constants.CARDSALE_Sign_Validation, 1);

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Constants.showDialog(CashSaleSignatureView.this, title, Constants.CARDSALE_Sign_ERROR_PROCESSIING_DATA, 1);


                }

            }
        });

        Button btnClear = (Button) findViewById(R.id.creditsale_BTN_clear);
        btnClear.setTypeface(applicationData.font);

        btnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                signatureView.clear();

            }
        });

    }

    private class RecieptView extends View {

        public Bitmap mBitmapReciept = null;
        public Paint mPaintReciept = null;


        // CONSTRUCTOR
        public RecieptView(Context context) {
            super(context);
            setFocusable(true);

            mPaintReciept = new Paint();
            mPaintReciept.setTextSize(getResources().getDimension(R.dimen.font_size));


        }

        @Override
        protected void onSizeChanged(int width, int height, int oldw, int oldh) {
            super.onSizeChanged(width, height, oldw, oldh);

            // smooth's out the edges of what is being drawn
            mPaintReciept.setAntiAlias(true);
            mBitmapReciept = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); // this creates a MUTABLE bitmap
            Canvas canvas = new Canvas(mBitmapReciept);

            int ypos = 0;
            Rect sizetext = new Rect();
            String strText = "";
            Rect bounds = new Rect();
            int yoffset = 2;

            mPaintReciept.setTextSize((int) (12 * scale));
            strText = "InvoiceNo: " + mInvoiceNo + " mVoucherNo: " + mVoucherNo;
            mPaintReciept.getTextBounds(strText, 0, strText.length(), bounds);
            ypos = bounds.height() + yoffset;
            canvas.drawText(strText, 2, ypos, mPaintReciept);

            strText = "DATE/TIME: " + mDate;
            ypos = ypos + bounds.height() + yoffset;
            canvas.drawText(strText, 2, ypos, mPaintReciept);

            strText = "MID: " + mMID + " TID: " + mTID;
            mPaintReciept.getTextBounds(strText, 0, strText.length(), sizetext);
            ypos = ypos + sizetext.height() + yoffset;
            canvas.drawText(strText, 2, ypos, mPaintReciept);

            mPaintReciept.setTextSize((int) (14 * scale));
            mPaintReciept.setTypeface(Typeface.DEFAULT_BOLD);
            strText = mAmt;
            mPaintReciept.getTextBounds(strText, 0, strText.length(), sizetext);
            ypos = ypos + sizetext.height() + yoffset;
            canvas.drawText(strText, 2, ypos, mPaintReciept);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap(mBitmapReciept, 0, 0, mPaintReciept);

        }

    }

    public Handler SignatureHandler = new Handler() {
             public void handleMessage(android.os.Message msg) {
            	 
            	 mProgressActivity.dismiss();
            	 
                 Bundle bundle = msg.getData();
                 String responseMsg = bundle.getString("response");
                 Log.v(ApplicationData.packName, log_tab + " the response xml is " + responseMsg);
                 String errMsg ="";
                 try
                 {
                     
                	 String[][] strTags = new String[][]{{"status", ""}, {"ErrMsg", ""}};
                     Constants.parseXml(responseMsg, strTags);

                     String status = strTags[0][1];
                     if (status.equalsIgnoreCase("false")) {
                          errMsg = strTags[1][1];
                         
                     } else if (status.equalsIgnoreCase("true")){
           
                    	 Constants.showDialog(CashSaleSignatureView.this, title, Constants.CARDSALE_Sign_SUCCESS_Msg, 1);

                     	 errMsg = Constants.CARDSALE_Sign_SUCCESS_Msg;
                     	   
                     }else{
                     	 errMsg = "Invalid response from Mswipe server, please contact support.";
                     	   
                     }


                 }
                 catch (Exception ex) {
                	 errMsg = "Invalid response from Mswipe server, please contact support.";
                 }
                 
                 
                 final Dialog dlg = Constants.showDialog(CashSaleSignatureView.this, title, errMsg, "1");
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
         };



    public void doneWithCreditSale() {

        creditSaleViewDestory();
        finish();
        //Intent intent = new Intent(CreditSaleView.this,MenuView.class);
        //startActivity(intent);

    }

    public void creditSaleViewDestory() {


    }
    
    private void printReceipt()
    {
    	
    	if(isBound){
    		
    		   	
        	ReceiptUtility receiptUtility = new ReceiptUtility(this);	
        
        	isPrinterStarted =true;
        	
        	if (ApplicationData.IS_DEBUGGING_ON)
        		Logs.v(getPackageName(), log_tab + " genEmiSaleReceipt " , true, true);
        	
        	receipts = new ArrayList<byte[]>();    		
        	receipts.add(receiptUtility.printReceipt(Constants.mReciptDataModel, signatureView.mBitmap, true, ReceiptUtility.TYPE.CASH));
        	        		
        	
    		isPrintRequestInQue = true;	
    		
    		if(receipts.size() > 0)
    		{
    			
            	mPrinterConnectionService.printReceipt(receipts.get(0));
    		}
    		
    		
    	}else{
    		isPrintRequestInQue = true;
			doBindService();
    	}
    	
    	
    }
    void doBindService() {
		bindService(new Intent(this, PrinterConnectionService.class), mConnection, Context.BIND_AUTO_CREATE);
		isBound = true;
	}
    
    
    void doUnbindService() {
		if (isBound) {
			// If we have received the service, and hence registered with it,
			// then now is the time to unregister.
			// Detach our existing connection.
			unbindService(mConnection);
			isBound = false;
		}
	}
    
    private ServiceConnection mConnection = new ServiceConnection() {
		
		public void onServiceConnected(ComponentName className, IBinder service) {
			
			try {
				
				LocalBinder localBinder = (LocalBinder) service;
				mPrinterConnectionService =  localBinder.getService();
				mPrinterConnectionService.setPrinterListner(printerListner);
				
			} catch (Exception e) {
				e.printStackTrace();
				// In this case the service has crashed before we could even do
				// anything with it
			}
			
		}

		public void onServiceDisconnected(ComponentName className) {
			// This is called when the connection with the service has been
			// unexpectedly disconnected - process crashed.
			mPrinterConnectionService = null;
		}
	};
	
	
	class PrinterListner extends MswipePrinterListner {
		
		
		@Override
		public void onRegisterd() {
			// TODO Auto-generated method stub
			super.onRegisterd();
			
		}
		
		@Override
		public void onUnRegisterd() {
			// TODO Auto-generated method stub
			super.onUnRegisterd();
		}
		

		@Override
		public void onPrinterStateChanged(PRINTER_STATE state) {
			// TODO Auto-generated method stub
			super.onPrinterStateChanged(state);	
			if (ApplicationData.IS_DEBUGGING_ON)
				Logs.v(getPackageName(), "printer state "+state, true, true);
			
			if (state == PRINTER_STATE.WAITINGFORCONNECTION) {
				
				isPrinterStarted =false;
				
				btnPrint.setVisibility(View.VISIBLE);
				btnPrint.setEnabled(true);
				
				final Dialog dialog = Constants.showDialog(CashSaleSignatureView.this, title,"Connecting to printer if its taking longer than usual please restart the printer and try again", "1");
				
				Button yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
				yes.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						dialog.dismiss();
						
					}
				});
				
				Button no = (Button) dialog.findViewById(R.id.bmessageDialogNo);
				no.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				
				dialog.show();
				
			}else if (state == PRINTER_STATE.CONNECTED) {
				
				isPrinterStarted =false;
				
				btnPrint.setVisibility(View.VISIBLE);
				btnPrint.setEnabled(true);
				
				if (applicationData.IS_DEBUGGING_ON)
					Logs.v(getPackageName(), log_tab + "onBTv2Connected", true, true);
				
			
				
				new Thread() {
					public void run() {
						try {
							sleep(1000);

						
							printReceipt();
							isPrintRequestInQue = false;

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					};
				}.start();
				
		
				
			}else if (state == PRINTER_STATE.DISCONNECTED) {
				
				
				isPrinterStarted =false;
				
				btnPrint.setVisibility(View.VISIBLE);
				btnPrint.setEnabled(true);
				
				if (applicationData.IS_DEBUGGING_ON)
					Logs.v(getPackageName(), log_tab + "onBTv2Disconnected  ", true, true);
				
				
			}else if (state == PRINTER_STATE.PRINTING) {
				
				
			}
		}
		
		
		@Override
		public void onPrinterConnectionError(PRINTER_CONNECTION_ERROR state, ArrayList<BluetoothDevice> devices) {
			// TODO Auto-generated method stub
			super.onPrinterConnectionError(state, devices);
			
			isPrinterStarted =false;
			
			
			String msg = "";
        	btnPrint.setVisibility(View.VISIBLE);
        	btnPrint.setEnabled(true);
         	
			
			if(state == PRINTER_CONNECTION_ERROR.NO_PAIRED_DEVICE_FOUND){
				
				msg = "no paired printer found please pair the printer from your phone's bluetooth settings and try again";
			}else if(state == PRINTER_CONNECTION_ERROR.MULTIPLE_PAIRED_DEVICE){
				
				pairedDevicesFound = devices;
				
				TaskShowMultiplePairedDevices pairedtask = new TaskShowMultiplePairedDevices();
				pairedtask.execute();
				
				btnPrint.setEnabled(false);
				return;
				
			}else if (state == PRINTER_CONNECTION_ERROR.BLUETOOTH_OFF) {
				
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
				
			}else if (state == PRINTER_CONNECTION_ERROR.UNKNOWN) {
				
				msg = "unable to connect to bluetooth printer";
				
			}else if (state == PRINTER_CONNECTION_ERROR.CONNECTING){
				msg = "connecting to printer, if its taking loger than usual please restart the printer and try reconnecting";
			}else if (state == PRINTER_CONNECTION_ERROR.WISEPAD_SWITCHED_OFF){
				final Dialog dialog = Constants.showDialog(CashSaleSignatureView.this, title,
						"printer not connected pleace make sure that the printer is switched on", "2", 
						getResources().getString(R.string.connect), 
						getResources().getString(R.string.close));
				
				Button yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
				yes.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						dialog.dismiss();
						mPrinterConnectionService.reconnectToDevice();
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
			}else if(state == PRINTER_CONNECTION_ERROR.PRINTING_IN_PROGRESS){
				
				
				
				new Handler(Looper.getMainLooper()).post(new Runnable() {
				    @Override
				    public void run() {
				    	Toast.makeText(CashSaleSignatureView.this,"printing in process", Toast.LENGTH_SHORT).show();
				    }
				});
				
			}
			
			if (ApplicationData.IS_DEBUGGING_ON)
				Logs.v(ApplicationData.packName,log_tab+ "makeConnnection msg "+ msg,true, true);
			
			if (msg.length()>0) {
				showDialog(msg);
			}		
		}
        	
		private void showDialog(String msg){
			if(!isUploadingSignature){
				try{
					showWaringDialog(msg);					
				}catch(Exception e){
					e.printStackTrace();
				}			
			}
		}
		

		@Override
		public void onReturnDeviceInfo(Hashtable<String, String> deviceInfoTable) {

			String msg = "";
			
			String productId = deviceInfoTable.get("productId");
			String firmwareVersion = deviceInfoTable.get("firmwareVersion");
			String bootloaderVersion = deviceInfoTable.get("bootloaderVersion");
			String hardwareVersion = deviceInfoTable.get("hardwareVersion");
			String isUsbConnected = deviceInfoTable.get("isUsbConnected");
			String isCharging = deviceInfoTable.get("isCharging");
			String batteryLevel = deviceInfoTable.get("batteryLevel");
			
			String content = "";
			content += "product id " + productId + "\n";
			content += getString(R.string.firmware_version) + firmwareVersion + "\n";
			content += getString(R.string.bootloader_version) + bootloaderVersion + "\n";
			content += getString(R.string.hardware_version) + hardwareVersion + "\n";
			content += getString(R.string.usb) + isUsbConnected + "\n";
			content += getString(R.string.charge) + isCharging + "\n";
			content += getString(R.string.battery_level) + batteryLevel + "\n";
			
			msg =  content;
			
			if (applicationData.IS_DEBUGGING_ON)
				Logs.v(getPackageName(), log_tab + "onReturnDeviceInfo =>  msg " + msg, true, true);
		}

		@Override
		public void onReturnPrinterResult(PrinterResult printerResult) {

			isPrinterStarted =false;
			
			String msg = "";
			
			btnPrint.setVisibility(View.VISIBLE);
			btnPrint.setEnabled(true);
      	
			if(printerResult == PrinterResult.SUCCESS) {
				msg =  "print success";
			} else if(printerResult == PrinterResult.NO_PAPER) {
				msg =  "No paper";
			} else if(printerResult == PrinterResult.WRONG_CMD) {
				msg = "Wrong command";
			} else if(printerResult == PrinterResult.OVERHEAT) {
				msg =  "Printer overheat";
			}
			
			if (ApplicationData.IS_DEBUGGING_ON)
				Logs.v(getPackageName(), log_tab + " onReturnPrinterResult PrinterResult "+ msg, true, true);
			
      	Constants.showDialog(CashSaleSignatureView.this, title, msg, 1);

		}

		@Override
		public void onPrinterOperationEnd() {
			isPrinterStarted =false;
		}

		@Override
		public void onBatteryLow(BatteryStatus batteryStatus) {

			String msg = "";
			
			if(batteryStatus == BatteryStatus.LOW) {
				msg =  getString(R.string.battery_low);
			} else if(batteryStatus == BatteryStatus.CRITICALLY_LOW) {
				msg =  getString(R.string.battery_critically_low);
			}
			
			showDialog(msg);
			
		}

		@Override
		public void onError(Error errorState) {

			isPrinterStarted =false;
			
			String msg = "";
			
		
			
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
				msg =  "fail to start btv2";
			} else if(errorState == Error.COMM_LINK_UNINITIALIZED) {
				msg =  getString(R.string.comm_link_uninitialized);
			} else if(errorState == Error.BTV2_ALREADY_STARTED) {
				msg =  getString(R.string.bluetooth_already_started);
			}
			
			
			if (applicationData.IS_DEBUGGING_ON)
				Logs.v(getPackageName(), log_tab + "onError  the error state is " + errorState, true, true);

			try{
				showDialog(msg);
				
			}catch(Exception e){
				e.printStackTrace();
			}

			btnPrint.setVisibility(View.VISIBLE);
			btnPrint.setEnabled(true);

		}

	}
	
	private class TaskShowMultiplePairedDevices extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			return "";
		}
		
		@Override
		protected void onPostExecute(String result) {
			final Dialog dlgPairedDevices = Constants.shwoAppCustionDialog(CashSaleSignatureView.this, getResources().getString(R.string.Paired_devices));
			ArrayList<String> deviceNameList = new ArrayList<String>();
			for (int i = 0; i < pairedDevicesFound.size(); ++i) {
				deviceNameList.add(pairedDevicesFound.get(i).getName());
				
			}
			
			ListView appListView = (ListView) dlgPairedDevices.findViewById(R.id.creditsale_LST_applications);
			appListView.setAdapter(new AppViewAdapter(CashSaleSignatureView.this, deviceNameList));
			appListView.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					
					btnPrint.setEnabled(false);
					mPrinterConnectionService.connectToWisePad(pairedDevicesFound.get(position));
					dlgPairedDevices.dismiss();
				}
				
			});
			
			dlgPairedDevices.findViewById(R.id.bmessageDialogNo).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					dlgPairedDevices.dismiss();
				}
			});
			dlgPairedDevices.show();
			
			
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
	
    Dialog warningDialog;
	private void showWaringDialog(String message){

    	
	      
        if(warningDialog == null){
          warningDialog =  new Dialog(this, R.style.styleCustDlg);
          warningDialog.setContentView(R.layout.customdlg);
    	  warningDialog.setCanceledOnTouchOutside(false);

    	  warningDialog.setCancelable(true);

          ApplicationData applicationData = (ApplicationData) getApplicationContext();

          // set the title
          TextView txttitle = (TextView) warningDialog.findViewById(R.id.tvmessagedialogtitle);
          txttitle.setText(title);
          txttitle.setTypeface(applicationData.font);


          // to set the message
          TextView txtMessage = (TextView) warningDialog.findViewById(R.id.tvmessagedialogtext);
          txtMessage.setText(message);
          txtMessage.setTypeface(applicationData.font);

          Button yes = (Button) warningDialog.findViewById(R.id.bmessageDialogYes);
          yes.setText("ok");          
          yes.setTypeface(applicationData.font);

          Button no = (Button) warningDialog.findViewById(R.id.bmessageDialogNo);
          no.setText("cancel");
          no.setTypeface(applicationData.font);
          no.setVisibility(View.GONE);
          yes.setOnClickListener(new OnClickListener() {

              public void onClick(View v) {
                  warningDialog.dismiss();

              }
          });

          warningDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
          warningDialog.show();

        }else{
        	ApplicationData applicationData = (ApplicationData)getApplicationContext();

            // set the title
            TextView txttitle = (TextView) warningDialog.findViewById(R.id.tvmessagedialogtitle);
            txttitle.setText(title);
            txttitle.setTypeface(applicationData.font);


            // to set the message
            TextView txtMessage = (TextView) warningDialog.findViewById(R.id.tvmessagedialogtext);
            txtMessage.setText(message);
            txtMessage.setTypeface(applicationData.font);

        }
        warningDialog.show();

      }

}
