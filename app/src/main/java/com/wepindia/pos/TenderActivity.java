/****************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	TenderActivity
 * 
 * Purpose			:	Represents Tender activity, takes care of all
 * 						UI back end operations in this activity, such as event
 * 						handling data read from or display in views.
 * 
 * DateOfCreation	:	04-December-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 ****************************************************************************/
package com.wepindia.pos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.WepBaseActivity;
import com.wepindia.pos.GenericClasses.EditTextInputHandler;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.utils.ActionBarUtils;

import java.util.Date;

public class TenderActivity extends WepBaseActivity{
	
	// Context object
	Context myContext;
	
	// DatabaseHandler object
	DatabaseHandler dbTender = new DatabaseHandler(TenderActivity.this);
	// MessageDialog Object
	MessageDialog MsgBox;
			
	// View handlers
	com.wep.common.app.views.WepButton btnDiscount;
	CheckBox chkComplimentary;
	EditText txtCash, txtCard, txtBarcode, txtComplimentaryReason;
	TextView lblCouponValue, lblTotalValue, lblTenderValue, lblChange;
	TableLayout tblTenderCoupon;
				
	// Variables
	public static final String IS_COMPLIMENTARY_BILL = "false";
	public static final String COMPLIMENTARY_REASON = "complimenatry_reason";
	public static final String IS_DISCOUNTED = "false";
	public static final String IS_PRINT_BILL = "false";
	public static final String DISCOUNT_PERCENT = "0";
	public static final String TENDER_BILL_TOTAL = "bill_total";
	public static final String TENDER_TENDER_AMOUNT = "tender_amount";
	public static final String TENDER_CHANGE_AMOUNT = "change_amount";
	public static final String TENDER_CASH_VALUE = "cash_value";
	public static final String TENDER_CARD_VALUE = "card_value";
	public static final String TENDER_COUPON_VALUE = "coupon_value";
	
	boolean isDiscounted = false;
	String strTotal = "0";
	int iDiscountType = 0;
	double dDiscountPercent = 0;
	private Toolbar toolbar;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tender);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
        /*TextView tvTitleText = (TextView)findViewById(R.id.tvTitleBarCaption);
		ActionBarUtils.goBack(this, findViewById(R.id.imgTitleBackIcon));
		ActionBarUtils.goHome(this, findViewById(R.id.imgTitleHomeIcon));
        tvTitleText.setText("Tender Screen");*/
        
        myContext = this;
        
        MsgBox = new MessageDialog(myContext);
                        
        try {
        	InitializeViewVariables();
        	
        	strTotal = getIntent().getStringExtra("TotalAmount");
        	Log.v("Debug", "Total Amount:" + strTotal);
        	lblTotalValue.setText(strTotal);
        	
        	dbTender.CreateDatabase();
			dbTender.OpenDatabase();
			
			ResetTender();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date d = new Date();
		CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
		com.wep.common.app.ActionBarUtils.setupToolbar(this,toolbar,getSupportActionBar(),"Tender Screen",""," Date:"+s.toString());
    }
	
	private void InitializeViewVariables(){
		EditTextInputHandler etInputValidate =  new EditTextInputHandler();
		txtComplimentaryReason = (EditText)findViewById(R.id.etComplimentaryReason);
        txtCash = (EditText)findViewById(R.id.etTenderCashValue);
        txtCash.addTextChangedListener(ChangeAmountEvent);
        etInputValidate.ValidateDecimalInput(txtCash);
        txtCard = (EditText)findViewById(R.id.etTenderCardValue);
        txtCard.addTextChangedListener(ChangeAmountEvent);
        etInputValidate.ValidateDecimalInput(txtCard);
        lblCouponValue = (TextView)findViewById(R.id.tvTenderCouponValue);
        lblCouponValue.addTextChangedListener(ChangeAmountEvent);
        txtBarcode = (EditText)findViewById(R.id.etTenderCouponBarcodeValue);
        txtBarcode.setOnKeyListener(BarcodeKeyPressEvent);
        lblTotalValue = (TextView)findViewById(R.id.tvTenderTotalValue);
        lblTenderValue = (TextView)findViewById(R.id.tvTenderTenderValue);
        lblChange = (TextView)findViewById(R.id.tvTenderChangeValue);
        tblTenderCoupon = (TableLayout)findViewById(R.id.tblTenderCoupon);
        btnDiscount = (com.wep.common.app.views.WepButton)findViewById(R.id.btnTenderDiscount);
        chkComplimentary = (CheckBox)findViewById(R.id.chkComplimentaryBill);
        chkComplimentary.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					btnDiscount.setEnabled(false);
					txtComplimentaryReason.setVisibility(View.VISIBLE);
				} else {
					btnDiscount.setEnabled(true);
					txtComplimentaryReason.setVisibility(View.GONE);
				}
			}
		});
	}
	
	OnKeyListener BarcodeKeyPressEvent = new OnKeyListener() {

		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			EditText txtBarcode = (EditText)v;

			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

				if(v.getId() == R.id.etTenderCouponBarcodeValue){

					if(txtBarcode.getText().toString().equalsIgnoreCase("")){

						MsgBox.Show("Tender", "Please enter coupon bar code before searching!");
					}else{
						Cursor CouponSearch = dbTender.getCoupon(txtBarcode.getText().toString());
						ClearTenderCouponTable();
						DisplayCoupon(CouponSearch);
					}
				}

				return true;
			}
			else{
				return false;
			}
		}
	};

	OnKeyListener CouponQtyKeyPressEvent = new OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(((EditText) v).getWindowToken(), 0);
				
				//CouponAmount();
				
				return true;
			}
			else{
				return false;
			}
		}
	};
	
	TextWatcher CouponQtyChangeEvent = new TextWatcher(){

		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
			CouponAmount();
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	TextWatcher ChangeAmountEvent = new TextWatcher() {

		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			TenderChange();
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	@SuppressWarnings("deprecation")
	private void DisplayCoupon(Cursor CouponList){
		
		int iPosition = 0;
		TableRow rowCoupon = null;
		TextView tvCouponAmount, tvCouponBarcode, tvCouponId, tvCouponDescription;
		EditText txtCouponQty = null;
		
		if(CouponList.moveToFirst()){
			do{
				rowCoupon = new TableRow(myContext);
				rowCoupon.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				rowCoupon.setBackgroundResource(R.drawable.row_background);
				
				tvCouponId = new TextView(myContext);
				iPosition = CouponList.getColumnIndex("CouponId");
				tvCouponId.setText(CouponList.getString(iPosition));
				rowCoupon.addView(tvCouponId);
				
				tvCouponDescription = new TextView(myContext);
				iPosition = CouponList.getColumnIndex("CouponDescription");
				tvCouponDescription.setText(CouponList.getString(iPosition));
				rowCoupon.addView(tvCouponDescription);
				
				tvCouponAmount = new TextView(myContext);
				iPosition = CouponList.getColumnIndex("CouponAmount");
				tvCouponAmount.setText(CouponList.getString(iPosition));
				rowCoupon.addView(tvCouponAmount);
				
				txtCouponQty = new EditText(myContext);
				txtCouponQty.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
				txtCouponQty.setText("0");
				txtCouponQty.setOnKeyListener(CouponQtyKeyPressEvent);
				txtCouponQty.addTextChangedListener(CouponQtyChangeEvent);
				rowCoupon.addView(txtCouponQty);
				
				tvCouponBarcode = new TextView(myContext);
				iPosition = CouponList.getColumnIndex("CouponBarcode");
				tvCouponBarcode.setText(CouponList.getString(iPosition));
				rowCoupon.addView(tvCouponBarcode);
								
				tblTenderCoupon.addView(rowCoupon,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				
			}while(CouponList.moveToNext());
		}
		else{
			Log.d("DisplayCoupon","No Coupon found");
		}
		
	}
	
	private void ClearTenderCouponTable(){
		for(int i = tblTenderCoupon.getChildCount() - 1; i >= 1; i--){
			View Row = tblTenderCoupon.getChildAt(i);
			if(Row instanceof TableRow){
				// remove all views present in row
				((TableRow) Row).removeAllViews();
				
				// Delete the empty row
				tblTenderCoupon.removeView(Row);
			}
		}
	}
	
	private void ResetTender(){
		txtCash.setText("0.00");
		txtCard.setText("0.00");
		lblCouponValue.setText("0.00");
		//lblTotalValue.setText(strTotal);
		lblTenderValue.setText("0.00");
		lblChange.setText("0.00");
		txtBarcode.setText("");
		ClearTenderCouponTable();
		Cursor CouponList = dbTender.getAllCoupon();
		if(CouponList.getCount() < 1){
			txtBarcode.setEnabled(false);
		}
		DisplayCoupon(CouponList);
		//TenderChange();
	}
	
	private void CouponAmount(){
		double dCouponAmount = 0, dAmount = 0;
		int iQty = 0;
		
		for(int i=1;i<tblTenderCoupon.getChildCount();i++){
			
			TableRow row = (TableRow)tblTenderCoupon.getChildAt(i);
			//Log.d("Coupon Amount", "Row Id:" + i + "Views inside rows:" + row.getChildCount());
			
			if(row.getChildAt(2) != null){
				TextView Amount = (TextView)row.getChildAt(2);
				dAmount = Amount.getText().toString().equalsIgnoreCase("") ? 0 : Double.parseDouble(Amount.getText().toString());
			}
			
			if(row.getChildAt(3)!=null){
				EditText Quantity = (EditText)row.getChildAt(3);
				iQty = Quantity.getText().toString().equalsIgnoreCase("") ? 0 : Integer.parseInt(Quantity.getText().toString());
			}
						
			dCouponAmount = dCouponAmount + (dAmount * iQty);
			
		}
		
		lblCouponValue.setText(String.format("%.2f",dCouponAmount));
	}
	
	private void TenderChange(){
		double dTenderAmount = 0.00, dChangeAmount = 0.00;
		double dCash = 0.00, dCard = 0.00, dCoupon = 0.00;
		
		dCash = txtCash.getText().toString().equalsIgnoreCase("") ? 0.00 : Double.parseDouble(txtCash.getText().toString());
		dCard = txtCard.getText().toString().equalsIgnoreCase("") ? 0.00 : Double.parseDouble(txtCard.getText().toString());
		dCoupon = lblCouponValue.getText().toString().equalsIgnoreCase("") ? 0.00 : Double.parseDouble(lblCouponValue.getText().toString());
		
		dTenderAmount = (dCash + dCard + dCoupon);
		
		dChangeAmount = dTenderAmount - Double.parseDouble(lblTotalValue.getText().toString());
		
		dChangeAmount = Math.round(dChangeAmount);
		
		if(dTenderAmount < Double.parseDouble(strTotal)){
			lblTenderValue.setTextColor(Color.RED);
		} else {
			lblTenderValue.setTextColor(Color.GREEN);
		}
				
		lblTenderValue.setText(String.format("%.2f",dTenderAmount));
		
		lblChange.setText(String.format("%.2f",dChangeAmount));
	}
	
	private void TenderDiscount(){
		AlertDialog.Builder TenderDiscountDialog = new AlertDialog.Builder(myContext);
		
		LayoutInflater TenderDiscount = (LayoutInflater)myContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View vwTenderDiscount = TenderDiscount.inflate(R.layout.tender_discount, null);
		
		final RadioButton rbDiscPercent = (RadioButton)vwTenderDiscount.findViewById(R.id.rbDiscountInPercent);
		final RadioButton rbDiscAmount = (RadioButton)vwTenderDiscount.findViewById(R.id.rbDiscountInAmount);
		final EditText txtDiscValue = (EditText)vwTenderDiscount.findViewById(R.id.etDiscountValue);
		
		TenderDiscountDialog
		.setIcon(R.drawable.ic_launcher)
		.setTitle("Discount")
		.setView(vwTenderDiscount)
		.setNegativeButton("Cancel", null)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(txtDiscValue.getText().toString().equalsIgnoreCase("")){
					MsgBox.Show("Discount", "Enter discount value");
					return;
				} else {
					// Discount in percentage
					if(rbDiscPercent.isChecked()){
						
						if(Double.parseDouble(txtDiscValue.getText().toString()) > 100){
							MsgBox.Show("Discount", "Discount percent can not exceed 100%");
							return;
						} else{
							double dAmt = 0;
							dAmt = Double.parseDouble(strTotal) - (Double.parseDouble(strTotal) * 
									Double.parseDouble(txtDiscValue.getText().toString()) / 100);
							lblTotalValue.setText(String.format("%.2f", dAmt));
							
							isDiscounted = true;
							dDiscountPercent = Double.parseDouble(txtDiscValue.getText().toString());
							Log.v("Tender Discount", "Before rounding discount percent:" + dDiscountPercent);
							dDiscountPercent = Math.round(dDiscountPercent * 100.0)/100.0;
							
							Log.v("Tender Discount", "Percent Discount - Discounted:" + isDiscounted + " Discount Percent:" + dDiscountPercent);
						}
					}
					// Discount in amount
					else if(rbDiscAmount.isChecked()){
						
						if(Double.parseDouble(txtDiscValue.getText().toString()) > 
						Double.parseDouble(strTotal)){
							MsgBox.Show("Discount", "Discount amount can not exceed bill amount " + strTotal);
							return;
						} else {
							double dAmt = 0;
							dAmt = Double.parseDouble(strTotal) - Double.parseDouble(txtDiscValue.getText().toString());
							lblTotalValue.setText(String.format("%.2f", dAmt));
							
							isDiscounted = true;
							dDiscountPercent = (Double.parseDouble(txtDiscValue.getText().toString()) * 100) / 
									Double.parseDouble(strTotal);
							Log.v("Tender Discount", "Before rounding discount percent:" + dDiscountPercent);
							dDiscountPercent = Math.round(dDiscountPercent * 100.0)/100.0;
							
							Log.v("Tender Discount", "Amount Discount - Discounted:" + isDiscounted + " Discount Percent:" + dDiscountPercent);
						}
					}
				}
			}
		})
		.show();
	}
	
	public void Print(View v){
		if(!chkComplimentary.isChecked() && Double.parseDouble(lblTenderValue.getText().toString()) < 
				Double.parseDouble(lblTotalValue.getText().toString())){
			//Toast.makeText(myContext, "Tendered amount is less than Bill amount", Toast.LENGTH_LONG).show();
			MsgBox.Show("Tender", "Tendered Amount is less than Bill Amount!");
		}else{
			
			if(chkComplimentary.isChecked() && txtComplimentaryReason
					.getText().toString().equalsIgnoreCase("")){
				MsgBox.Show("Tender", "Please enter reason for this complimentary bill!");
				return;
			}
			
			// Close Database connection
			dbTender.CloseDatabase();
			
			// set Results
			Intent intentResult = new Intent();
			
			intentResult.putExtra(IS_COMPLIMENTARY_BILL, chkComplimentary.isChecked());
			intentResult.putExtra(COMPLIMENTARY_REASON, txtComplimentaryReason.getText().toString());
			intentResult.putExtra(IS_DISCOUNTED, isDiscounted);
			intentResult.putExtra(IS_PRINT_BILL, true);
			intentResult.putExtra(DISCOUNT_PERCENT, dDiscountPercent);
			intentResult.putExtra(TENDER_CASH_VALUE, Float.parseFloat(txtCash.getText().toString()));
			intentResult.putExtra(TENDER_CASH_VALUE, Float.parseFloat(txtCash.getText().toString()));
			intentResult.putExtra(TENDER_CARD_VALUE, Float.parseFloat(txtCard.getText().toString()));
			intentResult.putExtra(TENDER_COUPON_VALUE, Float.parseFloat(lblCouponValue.getText().toString()));
			
			setResult(RESULT_OK,intentResult);
			
			// Finish the activity
			this.finish();
		}
	}
	
	public void Save(View v){
		if(!chkComplimentary.isChecked() && Double.parseDouble(lblTenderValue.getText().toString()) < 
				Double.parseDouble(lblTotalValue.getText().toString())){
			
			MsgBox.Show("Tender", "Tendered Amount is less than Bill Amount!");
		}else{
			
			if(chkComplimentary.isChecked() && txtComplimentaryReason
					.getText().toString().equalsIgnoreCase("")){
				MsgBox.Show("Tender", "Please enter reason for this complimentary bill!");
				return;
			}
			
			// Close Database connection
			dbTender.CloseDatabase();
			
			// set Results
			Intent intentResult = new Intent();
			
			intentResult.putExtra(IS_COMPLIMENTARY_BILL, chkComplimentary.isChecked());
			intentResult.putExtra(COMPLIMENTARY_REASON, txtComplimentaryReason.getText().toString());
			intentResult.putExtra(IS_DISCOUNTED, isDiscounted);
			intentResult.putExtra(IS_PRINT_BILL, false);
			intentResult.putExtra(DISCOUNT_PERCENT, dDiscountPercent);
			intentResult.putExtra(TENDER_CASH_VALUE, Float.parseFloat(txtCash.getText().toString()));
			intentResult.putExtra(TENDER_CARD_VALUE, Float.parseFloat(txtCard.getText().toString()));
			intentResult.putExtra(TENDER_COUPON_VALUE, Float.parseFloat(lblCouponValue.getText().toString()));
			
			setResult(RESULT_OK,intentResult);
			
			// Finish the activity
			this.finish();
		}
	}
	
	public void Discount(View v){
		TenderDiscount();
		TenderChange();
	}
	
	public void Clear(View v){
		
		ResetTender();
	}
	
	public void Close(View v){
		// Close Database connection
		dbTender.CloseDatabase();
		
		// Finish the activity
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

	@Override
	public void onHomePressed() {
		ActionBarUtils.navigateHome(this);
	}
}
