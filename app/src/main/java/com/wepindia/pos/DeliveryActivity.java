/****************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	DeliveryActivity
 * 
 * Purpose			:	Represents order delivery activity, takes care of all
 * 						UI back end operations in this activity, such as event
 * 						handling data read from or display in views.
 * 
 * DateOfCreation	:	09-January-2013
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 ****************************************************************************/
package com.wepindia.pos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.wep.common.app.Database.DatabaseHandler;
import com.wepindia.pos.GenericClasses.EditTextInputHandler;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.utils.ActionBarUtils;

public class DeliveryActivity extends Activity{
	
	// Context object
	Context myContext;
			
	// DatabaseHandler object
	DatabaseHandler dbDelivery = new DatabaseHandler(DeliveryActivity.this);
	// MessageDialog object
	MessageDialog MsgBox;
			
	// View handling variables
	EditText txtRiderName, txtPaidStatus, txtAmountDue;
	TableLayout tblDeliveryRider;
	
	// Global variables
	String strRiderCode, strCustId, strDueAmount, strBillAmt;

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Remove default title bar
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_delivery);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.application_title_bar);
        
        TextView tvTitleText = (TextView)findViewById(R.id.tvTitleBarCaption);
		ActionBarUtils.goBack(this, findViewById(R.id.imgTitleBackIcon));
		ActionBarUtils.goHome(this, findViewById(R.id.imgTitleHomeIcon));
		ActionBarUtils.takeScreenshot(this, findViewById(R.id.imgTitleScreenshotIcon), findViewById(R.id.lnrDelivery));
        tvTitleText.setText("Rider Select");
        
        myContext = this;
        
        try {
        	MsgBox = new MessageDialog(myContext);
        	IntializeViews();
            ResetDelivery();
			dbDelivery.CreateDatabase();
			dbDelivery.OpenDatabase();

			strCustId = getIntent().getStringExtra("CUST_ID");
            strDueAmount = getIntent().getStringExtra("DueAmount");
			strBillAmt = getIntent().getStringExtra("BILLAMT");

            Cursor crsrBillDetail = dbDelivery.getBillDetailByCustomerWithTime(Integer.valueOf(strCustId), 2, Float.parseFloat(strBillAmt));
            if(crsrBillDetail.moveToFirst()) {
                txtPaidStatus.setText("Paid");
                txtAmountDue.setText("0");
            }
            else
            {
                txtPaidStatus.setText("Cash on Delivery");
                txtAmountDue.setText(strDueAmount.toString());
            }
			LoadRidersToTable();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	private void IntializeViews(){
		EditTextInputHandler etInputValidate =  new EditTextInputHandler();
		txtRiderName = (EditText)findViewById(R.id.etDeliveryRiderName);
        txtPaidStatus = (EditText) findViewById(R.id.etPaidStatus);
        txtAmountDue = (EditText) findViewById(R.id.etAmountDue);
		tblDeliveryRider = (TableLayout)findViewById(R.id.tblRider);
	}
	
	@SuppressWarnings("deprecation")
	private void LoadRidersToTable(){
		TableRow rowRider = null;
		TextView tvSno, Code, Name, Status;
		Cursor crsrRider = dbDelivery.getAllDeliveryRiders();
        int i = 1;
		if(crsrRider.moveToFirst()){
			do{
				rowRider = new TableRow(myContext);
				rowRider.setLayoutParams(new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

                //S.No
                tvSno = new TextView(myContext);
                tvSno.setTextSize(18);
                tvSno.setGravity(1);
                tvSno.setText(String.valueOf(i));

				// Rider code
				Code = new TextView(myContext);
				Code.setTextSize(18);
				Code.setText(crsrRider.getString(crsrRider.getColumnIndex("UserId")));
				
				// Rider Name
				Name = new TextView(myContext);
				Name.setTextSize(18);
				Name.setText(crsrRider.getString(crsrRider.getColumnIndex("Name")));
				
				// Rider status
				Status = new TextView(myContext);
				Status.setText("Free");
				
				// add views to row
                rowRider.addView(tvSno);
                rowRider.addView(Code);
				rowRider.addView(Name);
				rowRider.addView(Status);
				
				rowRider.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(((TableRow)v).getChildAt(0) != null){
							
							TextView RiderCode = (TextView) ((TableRow)v).getChildAt(1);
							TextView RiderName = (TextView) ((TableRow)v).getChildAt(2);
							strRiderCode = RiderCode.getText().toString();
							txtRiderName.setText(RiderName.getText());
							
							Log.d("Delivery", "Selected Rider - Code:" + strRiderCode + 
									" Name:" + txtRiderName.getText().toString());
						}
					}
				});
									
				// Add row to table
				tblDeliveryRider.addView(rowRider,
						new LayoutParams(LayoutParams.FILL_PARENT,
								LayoutParams.WRAP_CONTENT));
				
				/*if(crsrRider.getInt(crsrRider.getColumnIndex("EmployeeRole")) == 2 || 
						crsrRider.getInt(crsrRider.getColumnIndex("EmployeeRole")) == 3){
					
					
				}*/
                i++;
			}while(crsrRider.moveToNext());
		}
	}
	
	public void OK(View v){
		
		double dDeliveryCharge = 0, dPettyCash = 0;
		
		if(txtRiderName.getText().toString().equalsIgnoreCase("")){
			MsgBox.Show("Warning", "Please Select Rider for Delivery");
			return;
		} else {

			Intent intentResult = new Intent();
			intentResult.putExtra("RIDER_CODE", Integer.parseInt(strRiderCode));
			/*intentResult.putExtra("DELIVERY_CHARGE", dDeliveryCharge);
			intentResult.putExtra("PETTY_CASH", dPettyCash);*/
            intentResult.putExtra("BILLAMT", strBillAmt);
			intentResult.putExtra("PAYMENT_STATUS", txtPaidStatus.getText().toString());
			intentResult.putExtra("CUST_ID", strCustId);
			
			setResult(RESULT_OK,intentResult);
			
			// Close database connection
			dbDelivery.CloseDatabase();
					
			// Close the activity
			this.finish();
		}
	}

    private void ResetDelivery()
    {
        txtRiderName.setText("");
        txtAmountDue.setText("");
        txtPaidStatus.setText("");
    }

	public void PrintBill(View view)
	{
        double dDeliveryCharge = 0, dPettyCash = 0;

        if(txtRiderName.getText().toString().equalsIgnoreCase("")){
            MsgBox.Show("Warning", "Please Select Rider for Delivery");
            return;
        } else {

            Intent intentResult = new Intent();
            intentResult.putExtra("RIDER_CODE", Integer.parseInt(strRiderCode));
            /*intentResult.putExtra("DELIVERY_CHARGE", dDeliveryCharge);
            intentResult.putExtra("PETTY_CASH", dPettyCash);*/
            intentResult.putExtra("BILLAMT", strBillAmt);
            intentResult.putExtra("PAYMENT_STATUS", txtPaidStatus.getText().toString());
            intentResult.putExtra("CUST_ID", strCustId);

            setResult(RESULT_OK,intentResult);

            // Close database connection
            dbDelivery.CloseDatabase();

            // Close the activity
            this.finish();
        }
	}
	
	public void Cancel(View v){
		// Close database connection
		dbDelivery.CloseDatabase();
		
		// Close the activity
		this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);
			AuthorizationDialog
					.setTitle("Are you sure you want to exit ?")
					.setIcon(R.drawable.ic_launcher)
					.setNegativeButton("No", null)
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							/*Intent returnIntent =new Intent();
							setResult(Activity.RESULT_OK,returnIntent);*/
							dbDelivery.CloseDatabase();
							finish();
						}
					})
					.show();
		}

		return super.onKeyDown(keyCode, event);
	}
	
}
