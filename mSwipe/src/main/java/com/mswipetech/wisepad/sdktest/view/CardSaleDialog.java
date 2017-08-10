package com.mswipetech.wisepad.sdktest.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mswipetech.wisepad.R;
import com.wep.common.app.print.Payment;


public class CardSaleDialog extends Dialog implements android.view.View.OnClickListener
{

	private final Payment payment;
	private Button	 			btnOk= null;
	private Context			  	context;
	String status = "";
	String authcode ="";
	String rrno= "";
	String title = "";
	boolean misCardSale = true;

	ApplicationData applicationData =null;
	private Button btnPrint;
	private CardSaleDialogInterface cardSaleListener;
	SharedPreferences sharedPreference = null;

	public CardSaleDialog(Activity context, Payment payment, String title, String status, String authcode, String rrno, boolean isCardSale){
		super(context,R.style.styleCustDlg);
		this.payment = payment;
		applicationData = (ApplicationData)context.getApplicationContext();
		this.status = status;
		this.authcode = authcode;
		this.rrno  =rrno;
		this.context=  context;
		this.title=title;
		this.misCardSale = isCardSale;
		cardSaleListener = (CardSaleDialogInterface) context;
		sharedPreference = context.getSharedPreferences("appPreferences", Context.MODE_PRIVATE);
		//this.setTitle(title);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if(misCardSale)
			setContentView(R.layout.cardsaledlg);
		else
			setContentView(R.layout.cashsaledlg);
		this.setCanceledOnTouchOutside(false);
		initUI();

	}

	@Override
	public void onClick(View v)
	{
		int i = v.getId();
		if (i == R.id.cardsalecustomdlg_BTN_ok) {

			SharedPreferences.Editor editor = sharedPreference.edit();
			editor.putString("paidAmount",/*payment.getTotalAmount()*/"0");
			editor.commit();
			CardSaleDialog.this.dismiss();
			cardSaleListener.onOkClicked();

		}else if (i == R.id.cardsalecustomdlg_BTN_print) {
			/*Intent intent = new Intent(context, PrinterSohamsaActivity.class);
			intent.putExtra("reportName","Card Payment Bill");
			intent.putExtra("printType","PaymentPrint");
			intent.putExtra("printData",payment);
			context.startActivity(intent);*/

			SharedPreferences.Editor editor = sharedPreference.edit();
			editor.putString("paidAmount",/*payment.getTotalAmount()*/"0");
			editor.commit();
			CardSaleDialog.this.dismiss();
			cardSaleListener.onPrintClicked(payment);
		}
	}


	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{

			return true;

		}else{
			return super.onKeyDown(keyCode, event);
		}
	}

	private void initUI(){
		TextView lblTitle = (TextView) findViewById(R.id.cardsalecustomdlg_LBL_title);
		lblTitle.setText(title);
		lblTitle.setTypeface(applicationData.fontbold);


		btnOk=(Button)findViewById(R.id.cardsalecustomdlg_BTN_ok);
		btnOk.setTypeface(applicationData.fontbold);
		btnOk.setOnClickListener(this);

		btnPrint=(Button)findViewById(R.id.cardsalecustomdlg_BTN_print);
		btnPrint.setTypeface(applicationData.fontbold);
		btnPrint.setOnClickListener(this);

		TextView lblStatus =(TextView)findViewById(R.id.cardsalecustomdlg_LBL_TxStatus);
		lblStatus.setTypeface(applicationData.font);
		((TextView)findViewById(R.id.cardsalecustomdlg_LBL_lblTxStatus)).setTypeface(applicationData.font);


		if(status.toString().equalsIgnoreCase("approved"))
		{
			lblStatus.setTextColor(Color.rgb(0,176,80));

		}else{
			lblStatus.setTextColor(Color.rgb(255,0,0));
		}
		lblStatus.setText(status);

		TextView lblAuthCode =(TextView)findViewById(R.id.cardsalecustomdlg_LBL_AuthNo);
		lblAuthCode.setTypeface(applicationData.font);
		lblAuthCode.setText(authcode);
		((TextView)findViewById(R.id.cardsalecustomdlg_LBL_lblAuthNo)).setTypeface(applicationData.font);

		TextView lblRrno =(TextView)findViewById(R.id.cardsalecustomdlg_LBL_RRNo);
		lblRrno.setTypeface(applicationData.font);
		lblRrno.setText(rrno);
		((TextView)findViewById(R.id.cardsalecustomdlg_LBL_lblRRNo)).setTypeface(applicationData.font);

		if(status.toString().equalsIgnoreCase("approved"))
		{
			cardSaleListener.onPaymentCompleted(true,payment);
			CardSaleDialog.this.dismiss();
			//SharedPreferences.Editor editor = sharedPreference.edit();
			//editor.putString("paidAmount",payment.getTotalAmount());
			//editor.commit();

		}
		else
		{
			cardSaleListener.onPaymentCompleted(false,payment);
			CardSaleDialog.this.dismiss();
			//SharedPreferences.Editor editor = sharedPreference.edit();
			//editor.putString("paidAmount",payment.getTotalAmount());
			//editor.commit();
		}
	}

	public interface CardSaleDialogInterface {
		public void onOkClicked();
		public void onPrintClicked(Payment payment);
		public void onPaymentCompleted(boolean status,Payment payment);
	}
}
