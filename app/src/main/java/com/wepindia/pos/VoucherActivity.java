
package com.wepindia.pos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.Database.VoucherConfig;
import com.wepindia.pos.GenericClasses.EditTextInputHandler;
import com.wepindia.pos.GenericClasses.MessageDialog;

public class VoucherActivity extends Activity{
	
	// Context object
	Context myContext;
	
	// DatabaseHandler object
	DatabaseHandler dbVoucherConfig = new DatabaseHandler(VoucherActivity.this);
	// MessageDialog object
	MessageDialog MsgBox;
		
	// View handlers
	EditText txtVoucherDesc, txtVoucherPercent;
	Button btnAddVoucher, btnEditVoucher;
	TableLayout tblVoucherConfig;
			
	// Variables
	String strVoucherId, strVoucherDesc, strVoucherPercent;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);
        
        myContext = this;
        
        MsgBox = new MessageDialog(myContext);
        
        EditTextInputHandler etInputValidate =  new EditTextInputHandler();
        
        txtVoucherDesc = (EditText)findViewById(R.id.etVoucherDescription);
        txtVoucherPercent = (EditText)findViewById(R.id.etVoucherPercent);
        etInputValidate.ValidateDecimalInput(txtVoucherPercent);
        tblVoucherConfig = (TableLayout)findViewById(R.id.tblVoucherConfig);
        btnAddVoucher = (Button)findViewById(R.id.btnAddVoucher);
        btnEditVoucher = (Button)findViewById(R.id.btnEditVoucher);
        
        ResetVoucherConfig();
        
        try{
        	dbVoucherConfig.CloseDatabase();
        	dbVoucherConfig.CreateDatabase();
        	dbVoucherConfig.OpenDatabase();
	    	DisplayVoucherConfig();
	    }
	    catch(Exception exp){
	    	Toast.makeText(myContext, "OnCreate: " + exp.getMessage(), Toast.LENGTH_LONG).show();
	    }
    }
	
	@SuppressWarnings("deprecation")
	private void DisplayVoucherConfig(){
		Cursor crsrVoucherConfig;
		crsrVoucherConfig = dbVoucherConfig.getAllVoucherConfig();
		
		TableRow rowVoucherConfig = null;
		TextView tvSno, tvVoucherId, tvVoucherDescription, tvVoucherPercent;
		ImageButton btnImgDelete;
		int i = 1;
		if(crsrVoucherConfig.moveToFirst()){
			do{
				rowVoucherConfig = new TableRow(myContext);
				rowVoucherConfig.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				rowVoucherConfig.setBackgroundResource(R.drawable.row_background);

                tvSno = new TextView(myContext);
                tvSno.setTextSize(18);
                tvSno.setText(String.valueOf(i));
                tvSno.setGravity(1);
				rowVoucherConfig.addView(tvSno);

				tvVoucherId = new TextView(myContext);
				tvVoucherId.setTextSize(18);
				tvVoucherId.setText(crsrVoucherConfig.getString(0));
				rowVoucherConfig.addView(tvVoucherId);
				
				tvVoucherDescription = new TextView(myContext);
				tvVoucherDescription.setTextSize(18);
				tvVoucherDescription.setText(crsrVoucherConfig.getString(1));
				rowVoucherConfig.addView(tvVoucherDescription);
				
				tvVoucherPercent = new TextView(myContext);
				tvVoucherPercent.setTextSize(18);
				tvVoucherPercent.setText(crsrVoucherConfig.getString(2));
                tvVoucherPercent.setGravity(1);
				rowVoucherConfig.addView(tvVoucherPercent);

                // Delete
                int res = getResources().getIdentifier("delete", "drawable", this.getPackageName());
                btnImgDelete = new ImageButton(myContext);
                btnImgDelete.setImageResource(res);
                btnImgDelete.setLayoutParams(new TableRow.LayoutParams(60, 40));
                btnImgDelete.setOnClickListener(mListener);
                rowVoucherConfig.addView(btnImgDelete);
				
				rowVoucherConfig.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(String.valueOf(v.getTag()) == "TAG"){
							TableRow Row = (TableRow) v;
							TextView VoucherId = (TextView)Row.getChildAt(1);
							TextView VoucherDescription = (TextView)Row.getChildAt(2);
							TextView VoucherPercent = (TextView)Row.getChildAt(3);
							strVoucherId = VoucherId.getText().toString();
							txtVoucherDesc.setText(VoucherDescription.getText());
							txtVoucherPercent.setText(VoucherPercent.getText());
							btnAddVoucher.setClickable(false);
							btnEditVoucher.setClickable(true);
							btnAddVoucher.setTextColor(Color.GRAY);
							btnEditVoucher.setTextColor(Color.BLACK);
						}
					}
				});
				
				rowVoucherConfig.setTag("TAG");
								
				tblVoucherConfig.addView(rowVoucherConfig,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				i++;
			}while(crsrVoucherConfig.moveToNext());
		}
		else{
			Log.d("DisplayVoucher","No Voucher Config found");
		}
		
	}

    private View.OnClickListener mListener = new View.OnClickListener() {

        public void onClick(final View v) {

			AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
					.setTitle("Delete")
					.setMessage("Are you sure you want to Delete this Voucher")
					.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

                            TableRow tr = (TableRow) v.getParent();
                            TextView VoucherId = (TextView) tr.getChildAt(1);
                            long lResult = dbVoucherConfig.DeleteVoucher(VoucherId.getText().toString());
                            //MsgBox.Show("", "Voucher Deleted Successfully");
							Toast.makeText(myContext, "Voucher Deleted Successfully", Toast.LENGTH_SHORT).show();

                            ClearVoucherConfigTable();
                            DisplayVoucherConfig();

							dialog.dismiss();
						}
					})
					.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			AlertDialog alert = builder.create();
			alert.show();
        }
    };
	
	private boolean IsVoucherPercentExists(double VoucherPercent){
		boolean isVoucherExists = false;
		double dDiscount = 0;
		TextView Discount;
		
		for(int i=1; i<tblVoucherConfig.getChildCount(); i++){
			
			TableRow Row = (TableRow)tblVoucherConfig.getChildAt(i);
			
			if(Row.getChildAt(0) != null){
				Discount = (TextView) Row.getChildAt(3);
				
				dDiscount = Double.parseDouble(Discount.getText().toString());
				
				Log.v("VoucherActivity", "Voucher:" + dDiscount + " New Voucher:" + VoucherPercent);
				
				if(VoucherPercent == dDiscount){
					isVoucherExists = true;
					break;
				}
			}
		}
		return isVoucherExists;
	}
	
	private void InsertVoucherConfig(int iVoucherId,String strVoucherDescription,float fVoucherPercent){
		long lRowId;
		
		VoucherConfig objVoucherConfig = new VoucherConfig(strVoucherDescription,iVoucherId,fVoucherPercent);
		
		lRowId = dbVoucherConfig.addVoucherConfig(objVoucherConfig);
		
		Log.d("InsertTaxConfig","Row Id: " + String.valueOf(lRowId));
	}
	
	private void ClearVoucherConfigTable(){
		
		for(int i=1;i<tblVoucherConfig.getChildCount();i++){
			View Row = tblVoucherConfig.getChildAt(i);
			if(Row instanceof TableRow){
				((TableRow) Row).removeAllViews();
			}
		}
	}
	
	private void ResetVoucherConfig(){
		txtVoucherDesc.setText("");
		txtVoucherPercent.setText("");
		btnAddVoucher.setClickable(true);
		btnEditVoucher.setClickable(false);
		btnAddVoucher.setTextColor(Color.BLACK);
		btnEditVoucher.setTextColor(Color.GRAY);
	}
	
	public void AddVoucherConfig(View v){
		String strVoucherDescription = txtVoucherDesc.getText().toString();
		String strVoucherPercent = txtVoucherPercent.getText().toString();
		int iVoucherId;
		if(strVoucherDescription.equalsIgnoreCase("") || 
				strVoucherPercent.equalsIgnoreCase("")){
			MsgBox.Show("Warning", "Please enter description and percent before adding");
		}
		else{
			if(IsVoucherPercentExists(Double.parseDouble(strVoucherPercent))){
				MsgBox.Show("Warning", "Voucher percent is already present");
			} else {
			
				iVoucherId = dbVoucherConfig.getVoucherId();
				Log.d("InsertTaxConfig","Tax Id: " + String.valueOf(iVoucherId));
				iVoucherId++;
				InsertVoucherConfig(iVoucherId,strVoucherDescription,Float.parseFloat(strVoucherPercent));
				ResetVoucherConfig();
				ClearVoucherConfigTable();
				DisplayVoucherConfig();
			}
		}
	}
	
	public void EditVoucherConfig(View v){
		strVoucherDesc = txtVoucherDesc.getText().toString();
		strVoucherPercent = txtVoucherPercent.getText().toString();
		Log.d("Voucher Selection","Id: " + strVoucherId + " Description: " + strVoucherDesc + " Percent: " + strVoucherPercent);
		int iResult = dbVoucherConfig.updateVoucherConfig(strVoucherId, strVoucherDesc, strVoucherPercent);
		Log.d("updateVoucher","Updated Rows: " + String.valueOf(iResult));
		ResetVoucherConfig();
		if(iResult > 0){
			ClearVoucherConfigTable();
			DisplayVoucherConfig();
		}
		else{
			MsgBox.Show("Warning", "Update failed");
		}
		/*if(IsDiscountPercentExists(Double.parseDouble(strDiscPercent))){
			MsgBox.Show("Warning", "Discount precent is already present");
		} else {
			
		}*/
	}
	
	public void ClearVoucherConfig(View v){
		ResetVoucherConfig();
	}
	
	public void CloseVoucherConfig(View v){
		
		dbVoucherConfig.CloseDatabase();
		this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);
			LayoutInflater UserAuthorization = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View vwAuthorization = UserAuthorization.inflate(R.layout.user_authorization, null);
			final EditText txtUserId = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserId);
			final EditText txtPassword = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserPassword);
			final TextView tvAuthorizationUserId= (TextView) vwAuthorization.findViewById(R.id.tvAuthorizationUserId);
			final TextView tvAuthorizationUserPassword= (TextView) vwAuthorization.findViewById(R.id.tvAuthorizationUserPassword);
			tvAuthorizationUserId.setVisibility(View.GONE);
			tvAuthorizationUserPassword.setVisibility(View.GONE);
			txtUserId.setVisibility(View.GONE);
			txtPassword.setVisibility(View.GONE);
			AuthorizationDialog
					.setTitle("Are you sure you want to exit ?")
					.setView(vwAuthorization)
					.setNegativeButton("No", null)
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							/*Intent returnIntent =new Intent();
							setResult(Activity.RESULT_OK,returnIntent);*/
							finish();
						}
					})
					.show();
		}

		return super.onKeyDown(keyCode, event);
	}
}
