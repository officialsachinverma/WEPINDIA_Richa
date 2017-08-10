/****************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	KOTModifierActivity
 * 
 * Purpose			:	Represents KOTModifier activity, takes care of all
 * 						UI back end operations in this activity, such as event
 * 						handling data read from or display in views.
 * 
 * DateOfCreation	:	02-November-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 ****************************************************************************/
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.Database.KOTModifier;
import com.wepindia.pos.GenericClasses.EditTextInputHandler;
import com.wepindia.pos.GenericClasses.MessageDialog;

import java.util.ArrayList;
import java.util.List;


public class KOTModifierActivity extends Activity{
	
	// Context object
	Context myContext;
	
	// DatabaseHandler object
	DatabaseHandler dbKOTModifier = new DatabaseHandler(KOTModifierActivity.this);
	// MessageDialog object
	MessageDialog MsgBox;
			
	// View handlers
	EditText txtModifierDesc, txtModifierAmount;
	CheckBox chkChargeable;
	Button btnAddKOTModifier, btnEditKOTModifier;
	TableLayout tblKOTModifier;
    Spinner spinnerModes;
	
	// Variables
	String strModifierId, strModifierDesc, strModifierAmount, strIsChargeable, strModifierModes;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kotmodifier);
        
        myContext = this;
        
        MsgBox = new MessageDialog(myContext);
        
        EditTextInputHandler etInputValidate =  new EditTextInputHandler();
        
        txtModifierDesc = (EditText)findViewById(R.id.etModifierDescription);
        txtModifierAmount = (EditText)findViewById(R.id.etModifierAmount);
        etInputValidate.ValidateDecimalInput(txtModifierAmount);
        chkChargeable = (CheckBox)findViewById(R.id.chkIsChargeable);
		spinnerModes = (Spinner) findViewById(R.id.spnrModes);
        tblKOTModifier = (TableLayout)findViewById(R.id.tblKOTModifier);
        btnAddKOTModifier = (Button)findViewById(R.id.btnAddModifier);
        btnEditKOTModifier = (Button)findViewById(R.id.btnEditModifier);
        

	    
	    try{
	    	dbKOTModifier.CloseDatabase();
	    	dbKOTModifier.CreateDatabase();
	    	dbKOTModifier.OpenDatabase();
			ResetKOTModifier();
	    	DisplayKOTModifier();

            //loadAutoCompleteData();
	    }
	    catch(Exception exp){
	    	Toast.makeText(myContext, "OnCreate: " + exp.getMessage(), Toast.LENGTH_LONG).show();
	    }
    }

    private void loadSpinnerData()
    {
        List<String> listModes = new ArrayList<String>();
        listModes.add("Select");
        /*listModes.add("DineIn");
        listModes.add("CounterSales");
        listModes.add("PickUp");
        listModes.add("HomeDelivery");
*/
		Cursor crsrBillSetting = dbKOTModifier.getBillSetting();

		if(crsrBillSetting.moveToFirst()) {

			// displaying Captions as per settings
			String DineInCaption = crsrBillSetting.getString(crsrBillSetting.getColumnIndex("HomeDineInCaption"));
			String CounterSalesCaption = crsrBillSetting.getString(crsrBillSetting.getColumnIndex("HomeCounterSalesCaption"));
			String TakeAwayCaption = crsrBillSetting.getString(crsrBillSetting.getColumnIndex("HomeTakeAwayCaption"));
			String HomeDeliveryCaption = crsrBillSetting.getString(crsrBillSetting.getColumnIndex("HomeHomeDeliveryCaption"));
			listModes.add(DineInCaption);
			listModes.add(CounterSalesCaption);
			listModes.add(TakeAwayCaption);
			listModes.add(HomeDeliveryCaption);
		}
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, listModes);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerModes.setAdapter(dataAdapter);
    }
	
	@SuppressWarnings("deprecation")
	private void DisplayKOTModifier(){
		Cursor crsrKOTModifier;
		crsrKOTModifier = dbKOTModifier.getAllKOTModifier();
		
		TableRow rowKOTModifier = null;
		TextView tvSno, tvModifierAmount, tvModifierId, tvModifierDescription, tvModifierChargeable, tvModes;
		ImageButton btnImgDelete;
		int i = 1;
		if(crsrKOTModifier.moveToFirst()){
			do{
				rowKOTModifier = new TableRow(myContext);
				rowKOTModifier.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				rowKOTModifier.setBackgroundResource(R.drawable.row_background);

                tvSno = new TextView(myContext);
                tvSno.setTextSize(18);
                tvSno.setText(String.valueOf(i));
                tvSno.setGravity(1);
				rowKOTModifier.addView(tvSno);
				
				tvModifierId = new TextView(myContext);
				tvModifierId.setTextSize(18);
				tvModifierId.setText(crsrKOTModifier.getString(0));
				rowKOTModifier.addView(tvModifierId);
				
				tvModifierDescription = new TextView(myContext);
				tvModifierDescription.setTextSize(18);
				tvModifierDescription.setText(crsrKOTModifier.getString(1));
				rowKOTModifier.addView(tvModifierDescription);
				
				tvModifierAmount = new TextView(myContext);
				tvModifierAmount.setTextSize(18);
				tvModifierAmount.setText(crsrKOTModifier.getString(2));
                tvModifierAmount.setGravity(1);
				rowKOTModifier.addView(tvModifierAmount);
				
				tvModifierChargeable = new TextView(myContext);
				if(crsrKOTModifier.getInt(3) == 1){
					tvModifierChargeable.setText("Yes");
				}
				else{
					tvModifierChargeable.setText("No");
				}
                tvModifierChargeable.setGravity(1);
                tvModifierChargeable.setTextSize(18);
				rowKOTModifier.addView(tvModifierChargeable);

                tvModes = new TextView(myContext);
                tvModes.setTextSize(18);
                tvModes.setText(crsrKOTModifier.getString(4));
                rowKOTModifier.addView(tvModes);

                TextView spc = new TextView(myContext);
                spc.setWidth(10);
                rowKOTModifier.addView(spc);
                // Delete
                int res = getResources().getIdentifier("delete", "drawable", this.getPackageName());
                btnImgDelete = new ImageButton(myContext);
                btnImgDelete.setImageResource(res);
                btnImgDelete.setLayoutParams(new TableRow.LayoutParams(60, 40));
                btnImgDelete.setOnClickListener(mListener);
                rowKOTModifier.addView(btnImgDelete);
				
				rowKOTModifier.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(String.valueOf(v.getTag()) == "TAG"){
							TableRow Row = (TableRow) v;
							TextView ModifierId = (TextView)Row.getChildAt(1);
							TextView ModifierDescription = (TextView)Row.getChildAt(2);
							TextView ModifierAmount = (TextView)Row.getChildAt(3);
							TextView ModifierChargeable = (TextView)Row.getChildAt(4);
                            TextView spinner = (TextView)Row.getChildAt(5);
							strModifierId = ModifierId.getText().toString();
							txtModifierDesc.setText(ModifierDescription.getText());
							txtModifierAmount.setText(ModifierAmount.getText());
							if(ModifierChargeable.getText() == "Yes"){
								chkChargeable.setChecked(true);
							}
							else{
								chkChargeable.setChecked(false);
							}
                            spinnerModes.setSelection(getIndex(spinnerModes, spinner.getText().toString()));
							btnAddKOTModifier.setClickable(false);
							btnEditKOTModifier.setClickable(true);
							btnAddKOTModifier.setTextColor(Color.GRAY);
							btnEditKOTModifier.setTextColor(Color.BLACK);
						}
					}
				});
				
				rowKOTModifier.setTag("TAG");
								
				tblKOTModifier.addView(rowKOTModifier,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				i++;
			}while(crsrKOTModifier.moveToNext());
		}
		else{
			Log.d("DisplayTax","No TaxConfig found");
		}
		
	}

    private int getIndex(Spinner s1, String prefNameCurGOV) {
        int index = 0;
        for (int i = 0; i < s1.getCount(); i++) {
            if (s1.getItemAtPosition(i).equals(prefNameCurGOV)) {
                index = i;
            }
        }
        return index;
    }


    private View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(final View v) {

			AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
					.setTitle("Delete")
					.setMessage("Are you sure you want to Delete this Other Charges")
					.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							TableRow tr = (TableRow) v.getParent();
							TextView OtherChargesId = (TextView) tr.getChildAt(1);
							long lResult = dbKOTModifier.DeleteOtherCharges(OtherChargesId.getText().toString());
							//MsgBox.Show("", "OtherCharges Deleted Successfully");
							Toast.makeText(myContext, "OtherCharges Deleted Successfully", Toast.LENGTH_SHORT).show();

							ClearKOTModifierTable();
							DisplayKOTModifier();

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
	
	private boolean IsModifierExists(String ModifierName, double Amount, String SpnrMode){
		boolean isModifierExists = false;
		String strModifier="", strModes = "";
        double OCAmt = 0;
		TextView Modifier, OCAmount, OCModes;
		
		for(int i=1; i<tblKOTModifier.getChildCount(); i++){
			
			TableRow Row = (TableRow)tblKOTModifier.getChildAt(i);
			
			if(Row.getChildAt(0) != null){
				Modifier = (TextView) Row.getChildAt(2);
                OCAmount = (TextView) Row.getChildAt(3);
				OCModes = (TextView) Row.getChildAt(5);

				strModifier = Modifier.getText().toString();
                strModes = OCModes.getText().toString();
                OCAmt = Double.parseDouble(OCAmount.getText().toString());

				Log.v("ModifierActivity", "Modifier:" + strModifier.toUpperCase() + " New Modifier:" + ModifierName.toUpperCase());
				
				if(strModifier.toUpperCase().equalsIgnoreCase(ModifierName.toUpperCase()) && OCAmt == Amount && strModes.equalsIgnoreCase(SpnrMode)){
					isModifierExists = true;
					break;
				}
			}
		}
		return isModifierExists;
	}
	
	private void InsertKOTModifier(String strModifierDescription,int iIsChargeable,int iModifierId,float fModifierAmount, String strModes){
		long lRowId;
		
		KOTModifier objKOTModifier = new KOTModifier(strModifierDescription,iIsChargeable,iModifierId,fModifierAmount, strModes);
		
		lRowId = dbKOTModifier.addKOTModifier(objKOTModifier);
		
		Log.d("InsertKOTmodifier","Row Id: " + String.valueOf(lRowId));
	}
	
	private void ClearKOTModifierTable(){
		
		for(int i=1;i<tblKOTModifier.getChildCount();i++){
			View Row = tblKOTModifier.getChildAt(i);
			if(Row instanceof TableRow){
				((TableRow) Row).removeAllViews();
			}
		}
	}
	
	private void ResetKOTModifier(){
		txtModifierDesc.setText("");
		txtModifierAmount.setText("");
		chkChargeable.setChecked(true);
        loadSpinnerData();
		btnAddKOTModifier.setClickable(true);
		btnEditKOTModifier.setClickable(false);
		btnAddKOTModifier.setTextColor(Color.BLACK);
		btnEditKOTModifier.setTextColor(Color.GRAY);
	}
	
	public void AddKOTModifier(View v){
		int iModifierId, iIsChargeable;
		String strModifierDescription = txtModifierDesc.getText().toString();
		String strModifierAmount = txtModifierAmount.getText().toString();
        String spnrModes = spinnerModes.getSelectedItem().toString();
		if(chkChargeable.isChecked() == true){
			iIsChargeable = 1;
		}
		else{
			iIsChargeable = 0;
		}
		if(strModifierDescription.equalsIgnoreCase("")){
			MsgBox.Show("Warning", "Please Enter OtherCharges Name, amount and Select Mode before adding");
		} else if(strModifierAmount.equalsIgnoreCase("")) {
			MsgBox.Show("Warning", "Please Enter OtherCharges Amount and Select Mode before adding");
		} else if(spnrModes.equalsIgnoreCase("Select")) {
			MsgBox.Show("Warning", "Please Select OtherCharges Mode before adding");
		}
		else{
			if(IsModifierExists(strModifierDescription, Double.parseDouble(strModifierAmount), spnrModes)){
				MsgBox.Show("Warning", "Other Charges is already present");
			} else {
				iModifierId = dbKOTModifier.getKOTModifierId();
				Log.d("AddModifier","Modifier Id: " + String.valueOf(iModifierId));
				iModifierId++;
				InsertKOTModifier(strModifierDescription,iIsChargeable,iModifierId,Float.parseFloat(strModifierAmount), spnrModes);
				ResetKOTModifier();
				ClearKOTModifierTable();
				DisplayKOTModifier();
			}
		}
	}
	
	public void EditKOTModifier(View v){
		strModifierDesc = txtModifierDesc.getText().toString();
		strModifierAmount = txtModifierAmount.getText().toString();
        strModifierModes = spinnerModes.getSelectedItem().toString();
		if(chkChargeable.isChecked() == true){
			strIsChargeable = "1";
		}
		else{
			strIsChargeable = "0";
		}
		Log.d("Modifier Selection","Id: " + strModifierId + " Description: " + strModifierDesc + " Chargeable: " + strIsChargeable +" Amount: " + strModifierAmount);
        if(strModifierDesc.equalsIgnoreCase("") || strModifierModes.equalsIgnoreCase("Select") ||
                strModifierAmount.equalsIgnoreCase("")){
            MsgBox.Show("Warning", "Please enter modifier name, amount and Select Mode before adding");
        }
        else {
            int iResult = dbKOTModifier.updateKOTModifier(strModifierId, strModifierDesc, strModifierAmount, strIsChargeable, strModifierModes);
            Log.d("updateCoupon", "Updated Rows: " + String.valueOf(iResult));
            ResetKOTModifier();
            if (iResult > 0) {
                ClearKOTModifierTable();
                DisplayKOTModifier();
            } else {
                MsgBox.Show("Warning", "Update failed");
            }
        }
		/*if(IsModifierExists(strModifierDesc)){
			MsgBox.Show("Warning", "Modifier is already present");
		} else {
			
		}*/
	}
	
	public void ClearKOTModifier(View v){
		ResetKOTModifier();
	}
	
	public void CloseKOTModifier(View v){
		
		dbKOTModifier.CloseDatabase();
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
