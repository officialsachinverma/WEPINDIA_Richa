/****************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	PaymentReceiptActivity
 * 
 * Purpose			:	Represents Payment Receipt activity, takes care of all
 * 						UI back end operations in this activity, such as event
 * 						handling data read from or display in views.
 * 
 * DateOfCreation	:	14-November-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 ****************************************************************************/
package com.wepindia.pos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.Database.PaymentReceipt;
import com.wep.common.app.WepBaseActivity;
import com.wep.common.app.views.WepButton;
import com.wepindia.pos.GenericClasses.EditTextInputHandler;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.PrintClasses.SendDataToSerialPort;
import com.wepindia.pos.utils.ActionBarUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PaymentReceiptActivity extends WepBaseActivity{
	
	// Context object
	Context myContext;
	
	// DatabaseHandler object
	DatabaseHandler dbPaymentReceipt = new DatabaseHandler(PaymentReceiptActivity.this);
	// DateTime object
	Date objDate;
	Date d, d1;
	Calendar Calobj;
	int DateChange =0;
				
	// View handlers
	EditText txtReason, txtAmount, txtPaymentReceiptDate;
	Spinner spnrDescription1, spnrDescription2, spnrDescription3;
	RadioButton rbPayment, rbReceipt;
				
	// Variables
	ArrayAdapter<String> adapDescriptionText;
	Cursor crsrSettings;
	String strPaymentReceiptDate = "", strUserName = "";
	MessageDialog MsgBox;
	private Toolbar toolbar;
	private WepButton btnPrintPaymentReceipt, btnSavePaymentReceipt,btnClearPaymentReceipt,btnClosePaymentReceipt;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentreceipt);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
        myContext = this;
		strUserName = getIntent().getStringExtra("USER_NAME");
		//tvTitleUserName.setText(strUserName.toUpperCase());
		d = new Date();
		CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
		//tvTitleDate.setText("Date : " + s);

		MsgBox = new MessageDialog(myContext);
        
        EditTextInputHandler etInputValidate =  new EditTextInputHandler();
        
        spnrDescription1 = (Spinner)findViewById(R.id.spnrDescription1);
        spnrDescription2 = (Spinner)findViewById(R.id.spnrDescription2);
        spnrDescription3 = (Spinner)findViewById(R.id.spnrDescription3);
        
        rbPayment = (RadioButton)findViewById(R.id.rbPayment);
        rbReceipt = (RadioButton)findViewById(R.id.rbReceipt);
        
        txtPaymentReceiptDate = (EditText)findViewById(R.id.etPaymentReceiptDate);
        txtReason = (EditText)findViewById(R.id.etPaymentReceiptReason);
        txtAmount = (EditText)findViewById(R.id.etPaymentReceiptAmount);
        etInputValidate.ValidateDecimalInput(txtAmount);

		btnSavePaymentReceipt = (WepButton)findViewById(R.id.btnSavePaymentReceipt);
		btnClearPaymentReceipt = (WepButton)findViewById(R.id.btnClearPaymentReceipt);
		btnClosePaymentReceipt = (WepButton)findViewById(R.id.btnClosePaymentReceipt);

		btnSavePaymentReceipt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SavePaymentReceipt(v);
			}
		});
		btnClearPaymentReceipt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ClearPaymentReceipt(v);
			}
		});
		btnClosePaymentReceipt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ClosePaymentReceipt(v);
			}
		});
        ResetPaymentReceipt();
        
        try{
        	dbPaymentReceipt.CreateDatabase();
        	dbPaymentReceipt.OpenDatabase();
        	crsrSettings = dbPaymentReceipt.getBillSetting();
        	if(!crsrSettings.moveToFirst()){
        		Log.d("PaymentReceipt", "No Settings table data");
        	}
			else
			{
				DateChange = (crsrSettings.getInt(crsrSettings.getColumnIndex("DateAndTime")));
				String date_str = crsrSettings.getString(crsrSettings.getColumnIndex("BusinessDate"));
				if(date_str!=null && !date_str.equals(""))
				{
					Calobj = convertDate(date_str,"dd-MM-yyyy");
					txtPaymentReceiptDate.setText(date_str);
				} else {
					objDate = new Date();
					Calobj.setTime(objDate);
				}
			}
        	InitializeViews();
			com.wep.common.app.ActionBarUtils.setupToolbar(PaymentReceiptActivity.this,toolbar,getSupportActionBar(),"Payment/Receipt",strUserName," Date:"+s.toString());

        }
        catch(Exception exp){
        	Toast.makeText(myContext, "OnCreate: " + exp.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

	private Calendar convertDate(String startDateString, String format)
	{

		SimpleDateFormat df = new SimpleDateFormat(format);
		Calendar startDate= null;
		try {
			startDate =  Calendar.getInstance();
			startDate.setTime(df.parse(startDateString));
			//String newDateString = df.format(startDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return startDate;
	}
	
	private void InitializeViews(){
			
		try {
			Cursor crsrDescription, BusinessDate; 
			crsrDescription = dbPaymentReceipt.getAllDescription();
			
			// set adapter to all spinners and initialize adapter with description text
			adapDescriptionText = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
			adapDescriptionText.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnrDescription1.setAdapter(adapDescriptionText);
			spnrDescription2.setAdapter(adapDescriptionText);
			spnrDescription3.setAdapter(adapDescriptionText);
			adapDescriptionText.add("Select");
			if(crsrDescription.moveToFirst()){

				do{
					adapDescriptionText.add(crsrDescription.getString(1));
				}while(crsrDescription.moveToNext());
			}
			else{
				Log.d("SpinnerData", "No Description Text");
			}
			

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void DateSelection(View v){
		try {
			//DateChange -> 1 - auto, 0 = manual
			/*if(DateChange == 1)
			{
				MsgBox = new MessageDialog(myContext);
				MsgBox.Show("Information", " Date Selection is in auto mode. Please change it to manual mode.");
				return;
			}*/
			AlertDialog.Builder dlgReportDate = new AlertDialog.Builder(myContext);
			final DatePicker dateReportDate = new DatePicker(myContext);



			int year = Calobj.get(Calendar.YEAR);
			int month = Calobj.get(Calendar.MONTH);
			int day = Calobj.get(Calendar.DAY_OF_MONTH);

			dateReportDate.updateDate(year, month,day);

			String strMessage = "";


			dlgReportDate
					.setIcon(R.drawable.ic_launcher)
					.setTitle("Date Selection")
					.setMessage(strMessage)
					.setView(dateReportDate)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							// richa date format change

							//strDate = String.valueOf(dateReportDate.getYear()) + "-";
							String strDate = "";
							if (dateReportDate.getDayOfMonth() < 10) {
								strDate = "0" + String.valueOf(dateReportDate.getDayOfMonth())+"-";
							} else {
								strDate = String.valueOf(dateReportDate.getDayOfMonth())+"-";
							}
							if (dateReportDate.getMonth() < 9) {
								strDate += "0" + String.valueOf(dateReportDate.getMonth() + 1) + "-";
							} else {
								strDate += String.valueOf(dateReportDate.getMonth() + 1) + "-";
							}

							strDate += String.valueOf(dateReportDate.getYear());
							txtPaymentReceiptDate.setText(strDate);


							Log.d("PaymentReceipt", "Date:" + strDate);
						}
					})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					})
					.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void ResetPaymentReceipt(){
		txtReason.setText("");
		txtAmount.setText("");

	}
	
	private long InsertPaymentReceipt(String Reason,float Amount,String Date,int BillType,String DescriptionText,int DescriptionId1,int DescriptionId2,int DescriptionId3){
		long lRowId = 0;
						
		PaymentReceipt objPaymentReceipt = new PaymentReceipt(Date,Reason,BillType,DescriptionText,DescriptionId1,DescriptionId2,DescriptionId3,Amount);
		
		lRowId = dbPaymentReceipt.addPaymentReceipt(objPaymentReceipt);
		
		Log.d("PaymentReceipt","Row Id: " + String.valueOf(lRowId));
        return lRowId;
	}
	
	private void PrintPaymentReceipt(){
		SendDataToSerialPort spPrinter = new SendDataToSerialPort();
		
		Calendar Time = Calendar.getInstance();
		String strPrintData = "";
		
		strPrintData = crsrSettings.getString(crsrSettings.getColumnIndex("HeaderText")) + "\r";
		spPrinter.Write(strPrintData.getBytes());
		
		strPrintData = String.format("%-15s", "Date" + txtPaymentReceiptDate.getText().toString());
		strPrintData += String.format("%9s", "Time" + String.format("%tR", Time) + "\r");
		spPrinter.Write(strPrintData.getBytes());
				
		if(rbPayment.isChecked()){
			strPrintData = "        Payment         " + "\r";
			spPrinter.Write(strPrintData.getBytes());
		}
		else{
			strPrintData = "        Receipt         " + "\r";
			spPrinter.Write(strPrintData.getBytes());
		}
		strPrintData = "------------------------" + "\r";
		spPrinter.Write(strPrintData.getBytes());
		
		strPrintData = "Description:" + spnrDescription1.getSelectedItem().toString() + "\r";
		spPrinter.Write(strPrintData.getBytes());
		
		//strPrintData += "Description 2:" + spnrDescription2.getSelectedItem().toString() + "\n";
		//strPrintData += "Description 3:" + spnrDescription3.getSelectedItem().toString() + "\n";
		if(!txtReason.getText().toString().equalsIgnoreCase("")){
			strPrintData = String.format("%24s", "Reason:" + txtReason.getText().toString()) + "\r";
			spPrinter.Write(strPrintData.getBytes());
		}
		strPrintData = "------------------------" + "\r";
		spPrinter.Write(strPrintData.getBytes());
				
		strPrintData = String.format("%-16s", "AMOUNT:");
		strPrintData += String.format("%8s", txtAmount.getText().toString()) + "\r";
		spPrinter.Write(strPrintData.getBytes());
						
		strPrintData = "------------------------" + "\r";
		spPrinter.Write(strPrintData.getBytes());
				
		strPrintData = crsrSettings.getString(crsrSettings.getColumnIndex("FooterText")) + "\r";
		spPrinter.Write(strPrintData.getBytes());
		
		// Close Serial port
		spPrinter.Close();
		
	}
	
	public void PrintPaymentReceipt(View v){
		try {
			
			PrintPaymentReceipt();
			
			SavePaymentReceipt(v);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    Calendar convertStringToCalendar(String selectedDate)
    {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        Calendar cal=Calendar.getInstance();

        Date date = null;
        try {
            date = df.parse(selectedDate);
            cal.setTime(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }
	public void SavePaymentReceipt(View v){
		int iBillType = 0;
		String strReason = "", strDate = "", strAmount = "";
		
		// get reason and amount from text box
		strReason = txtReason.getText().toString();
		strAmount = txtAmount.getText().toString();
		String temp_date = txtPaymentReceiptDate.getText().toString();
        Calendar cal = convertStringToCalendar(temp_date);
        strDate = String.valueOf(cal.getTimeInMillis());

        //strDate = txtPaymentReceiptDate.getText().toString();
						
		// get bill type based  on radio button selection
		if(rbPayment.isChecked()){
			iBillType = 1;	// Payment Bill Type
		}
		else{
			iBillType = 2;	// Receipt Bill Type
		}

        long l =0;
		if(strAmount.equalsIgnoreCase("")){
			Toast.makeText(myContext, "Please enter amount before saving", Toast.LENGTH_SHORT).show();
		}else if (spnrDescription1.getSelectedItem().toString().equalsIgnoreCase("Select"))
        {
            Toast.makeText(myContext, "Please select description before saving", Toast.LENGTH_SHORT).show();
        } else{
			l = InsertPaymentReceipt(strReason,Float.parseFloat(strAmount),strDate,iBillType,spnrDescription1.getSelectedItem().toString(),
					spnrDescription1.getSelectedItemPosition(),spnrDescription2.getSelectedItemPosition() + 1,spnrDescription3.getSelectedItemPosition() + 1);
            if (l>0)
            {
                ClearData();
                MsgBox.Show("","Payments/Receipts Saved Successfully");
            }
            else
            {
                Toast.makeText(myContext, "Error : Payments/Receipts cannot be saved", Toast.LENGTH_SHORT).show();
            }
		}

	}

    void ClearData()
    {
        ResetPaymentReceipt();
        String date_str = crsrSettings.getString(crsrSettings.getColumnIndex("BusinessDate"));
        txtPaymentReceiptDate.setText(date_str);
        spnrDescription1.setSelection(0);
    }
	public void ClearPaymentReceipt(View v){
		ClearData();
	}
	
	public void ClosePaymentReceipt(View v){
		
		dbPaymentReceipt.CloseDatabase();
		this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);
			AuthorizationDialog
					.setIcon(R.drawable.ic_launcher)
					.setTitle("Are you sure you want to exit ?")
					.setNegativeButton("No", null)
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							/*Intent returnIntent =new Intent();
							setResult(Activity.RESULT_OK,returnIntent);*/
							dbPaymentReceipt.CloseDatabase();
							finish();
						}
					})
					.show();
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onHomePressed() {
		ActionBarUtils.navigateHome(this);
	}
}
