/****************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	ReportColumns
 * 
 * Purpose			:	Represents report column caption drawing to report table
 * 						based on reprt type selection.
 * 
 * DateOfCreation	:	16-January-2013
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 ****************************************************************************/
package com.wepindia.pos.GenericClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.wep.common.app.Database.DatabaseHandler;
import com.wepindia.pos.PrintClasses.SendDataToSerialPort;
import com.wepindia.pos.R;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class ReportHelper{
	
	private static final String REPORT_PATH = Environment.getExternalStorageDirectory().getPath() + "/WeP_FnB_Reports/";
	MessageDialog MsgBox;
	
	public ReportHelper(Context activityContext){
		
		MsgBox = new MessageDialog(activityContext);
	}
	
	private void CheckReportsDirectory(){
		File ReportDirectory = new File(REPORT_PATH);
		Log.d("ReportHelper", "Does ReportDirectory exists? Ans:" + ReportDirectory.exists());
		if(!ReportDirectory.exists()){
			if(ReportDirectory.mkdir()){
				Log.d("ReportHelper", "ReportDirectory created");
			} else{
				Log.d("ReportHelper", "ReportDirectory does not created");
			}
		}
	}
	
	public void setReportColumnCaptions(Context activityContext,String ReportName,TableLayout ReportTable){
		TableRow rowColumnCaption;
		String GSTEnable="0", POSEnable="0",HSNEnable="0",ReverseChargeEnabe="0";
		DatabaseHandler dbReportHelper = new DatabaseHandler(activityContext);
		dbReportHelper.OpenDatabase();
		Cursor billsettingcursor = dbReportHelper.getBillSetting();
		if (billsettingcursor!=null && billsettingcursor.moveToFirst())
		{
			GSTEnable = billsettingcursor.getString(billsettingcursor.getColumnIndex("GSTEnable"));
			if (GSTEnable == null )
			{
				GSTEnable="0";
			}
			else if (GSTEnable.equals("1"))
			{
				HSNEnable= billsettingcursor.getString(billsettingcursor.getColumnIndex("HSNCode_Out"));
				if (HSNEnable==null)
				{
					HSNEnable="0";
				}
				POSEnable = billsettingcursor.getString(billsettingcursor.getColumnIndex("POS_Out"));
				if (POSEnable== null)
				{
					POSEnable= "0";
				}
				ReverseChargeEnabe = billsettingcursor.getString(billsettingcursor.getColumnIndex("ReverseCharge_Out"));
				if (ReverseChargeEnabe == null)
				{
					ReverseChargeEnabe="0";
				}
			}
		}
		dbReportHelper.CloseDatabase();
		// Create Heading row
		rowColumnCaption = new TableRow(activityContext);
		rowColumnCaption.setLayoutParams(new LayoutParams
				(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		rowColumnCaption.setBackgroundColor(activityContext.getResources().getColor(R.color.colorPrimaryLight));
		
		if(/*ReportName.equalsIgnoreCase("Bill wise Report") ||*/
				ReportName.equalsIgnoreCase("Void Bill Report") ||
				ReportName.equalsIgnoreCase("Day wise Report") || ReportName.equalsIgnoreCase("Month wise Report") ||
				ReportName.equalsIgnoreCase("Duplicate Bill Report")){
			
			TextView Date = new TextView(activityContext);
			Date.setWidth(120);
			Date.setTextSize(15);
			Date.setTextColor(Color.WHITE);
			if(ReportName.equalsIgnoreCase("Month wise Report"))
				Date.setText("Month");
			else
				Date.setText("Date");
			
			TextView BillNumber = new TextView(activityContext);
			BillNumber.setWidth(100);
			BillNumber.setTextSize(15);
			BillNumber.setTextColor(Color.WHITE);
			if(ReportName.equalsIgnoreCase("Day wise Report") || 
					ReportName.equalsIgnoreCase("Month wise Report")){
				BillNumber.setText("Total Bills");
			} else {
				BillNumber.setText("Bill Number");
			}
			
			TextView TotalItems = null;
			TotalItems = new TextView(activityContext);
			TotalItems.setWidth(100);
			TotalItems.setTextSize(15);
			TotalItems.setTextColor(Color.WHITE);
			TotalItems.setText("Items");
						
			TextView Amount = new TextView(activityContext);
			Amount.setWidth(100);
			Amount.setTextSize(15);
			Amount.setTextColor(Color.WHITE);
			Amount.setText("Bill Amount");
			
			TextView IGSTTax = new TextView(activityContext);
			IGSTTax.setWidth(100);
			IGSTTax.setTextSize(15);
			IGSTTax.setTextColor(Color.WHITE);
			IGSTTax.setText("IGST Amt");

			TextView SalesTax = new TextView(activityContext);
			SalesTax.setWidth(100);
			SalesTax.setTextSize(15);
			SalesTax.setTextColor(Color.WHITE);
			SalesTax.setText("CGST Amt");
			
			TextView ServiceTax = new TextView(activityContext);
			ServiceTax.setWidth(100);
			ServiceTax.setTextSize(15);
			ServiceTax.setTextColor(Color.WHITE);
			ServiceTax.setText("SGST Amt");

			TextView cessTax = new TextView(activityContext);
			cessTax.setWidth(100);
			cessTax.setTextSize(15);
			cessTax.setTextColor(Color.WHITE);
			cessTax.setText("cess Amt");
			
			TextView Discount = new TextView(activityContext);
			Discount.setWidth(100);
			Discount.setTextSize(15);
			Discount.setTextColor(Color.WHITE);
			Discount.setText("Discount");
			
			TextView ReprintCount = new TextView(activityContext);
			ReprintCount.setWidth(100);
			ReprintCount.setTextSize(15);
			ReprintCount.setTextColor(Color.WHITE);
			ReprintCount.setText("Reprint Count");
			
			// Add views to row
			rowColumnCaption.addView(Date);
			rowColumnCaption.addView(BillNumber);
			if(ReportName.equalsIgnoreCase("Bill wise Report") || 
					ReportName.equalsIgnoreCase("Void Bill Report") || 
					ReportName.equalsIgnoreCase("Duplicate Bill Report")){
				
				rowColumnCaption.addView(TotalItems);
			}
			rowColumnCaption.addView(Discount);
			rowColumnCaption.addView(IGSTTax);
			rowColumnCaption.addView(SalesTax);
			rowColumnCaption.addView(ServiceTax);
			rowColumnCaption.addView(cessTax);
			rowColumnCaption.addView(Amount);
			if (ReportName.equalsIgnoreCase("Duplicate Bill Report")) {
				rowColumnCaption.addView(ReprintCount);
			}
			
		}
		else if(ReportName.equalsIgnoreCase("Bill wise Report") ){

			TextView Date = new TextView(activityContext);
			Date.setWidth(120);
			Date.setTextSize(15);
			Date.setTextColor(Color.WHITE);
			Date.setText("Date");

			TextView BillNumber = new TextView(activityContext);
			BillNumber.setWidth(100);
			BillNumber.setTextSize(15);
			BillNumber.setTextColor(Color.WHITE);
			if(ReportName.equalsIgnoreCase("Day wise Report") ||
					ReportName.equalsIgnoreCase("Month wise Report")){
				BillNumber.setText("Total Bills");
			} else {
				BillNumber.setText("Bill Number");
			}

			TextView TotalItems = null;
			TotalItems = new TextView(activityContext);
			TotalItems.setWidth(100);
			TotalItems.setTextSize(15);
			TotalItems.setTextColor(Color.WHITE);
			TotalItems.setText("Items");

			TextView Amount = new TextView(activityContext);
			Amount.setWidth(100);
			Amount.setTextSize(15);
			Amount.setTextColor(Color.WHITE);
			Amount.setText("Bill Amount");

			TextView SalesTax = new TextView(activityContext);
			SalesTax.setWidth(100);
			SalesTax.setTextSize(15);
			SalesTax.setTextColor(Color.WHITE);
			SalesTax.setText("      Tax");

			TextView ServiceTax = new TextView(activityContext);
			ServiceTax.setWidth(100);
			ServiceTax.setTextSize(15);
			ServiceTax.setTextColor(Color.WHITE);
			ServiceTax.setText("Service Tax");

			TextView Discount = new TextView(activityContext);
			Discount.setWidth(100);
			Discount.setTextSize(15);
			Discount.setTextColor(Color.WHITE);
			Discount.setText("Discount");



			// Add views to row
			rowColumnCaption.addView(Date);
			rowColumnCaption.addView(BillNumber);
			rowColumnCaption.addView(TotalItems);
			rowColumnCaption.addView(Discount);
			rowColumnCaption.addView(SalesTax);
			//rowColumnCaption.addView(ServiceTax); richa making single tax
			rowColumnCaption.addView(Amount);

		}
        else if(ReportName.equalsIgnoreCase("Outward Stock Report") || ReportName.equalsIgnoreCase("Inward Stock Report") ){

			TextView Date = new TextView(activityContext);
			Date.setWidth(80);
			Date.setTextSize(15);
			Date.setTextColor(Color.WHITE);
            Date.setText("ItemCode");



			TextView TotalItems = null;
			TotalItems = new TextView(activityContext);
			TotalItems.setWidth(180);
			TotalItems.setTextSize(15);
			TotalItems.setTextColor(Color.WHITE);
			TotalItems.setText("Items");

			TextView SalesTax = new TextView(activityContext);
			SalesTax.setWidth(100);
			SalesTax.setTextSize(15);
			SalesTax.setTextColor(Color.WHITE);

			SalesTax.setText("Open STK");

			TextView ServiceTax = new TextView(activityContext);
			ServiceTax.setWidth(100);
			ServiceTax.setTextSize(15);
			ServiceTax.setTextColor(Color.WHITE);

			ServiceTax.setText("Close STK");

			TextView Amount = new TextView(activityContext);
			Amount.setWidth(120);
			Amount.setTextSize(15);
			Amount.setTextColor(Color.WHITE);
			Amount.setGravity(Gravity.CENTER);
			Amount.setText("C.STK Value");

			// Add views to row
			rowColumnCaption.addView(Date);
			rowColumnCaption.addView(TotalItems);
			rowColumnCaption.addView(SalesTax);
			rowColumnCaption.addView(ServiceTax);
            if (ReportName.equalsIgnoreCase("Outward Stock Report"))
                rowColumnCaption.addView(Amount);



		}

		else if(ReportName.equalsIgnoreCase("Transaction Report")){
			
			TextView Date = new TextView(activityContext);
			Date.setWidth(100);
			Date.setTextSize(15);
			Date.setTextColor(Color.WHITE);
			Date.setText("Date");
			
			TextView BillNumber = new TextView(activityContext);
			BillNumber.setWidth(100);
			BillNumber.setTextSize(15);
			BillNumber.setTextColor(Color.WHITE);
			BillNumber.setText("Bill Number");
			
			TextView TotalItems = new TextView(activityContext);
			TotalItems.setWidth(100);
			TotalItems.setTextSize(15);
			TotalItems.setTextColor(Color.WHITE);
			TotalItems.setText("Items");
			
			TextView Amount = new TextView(activityContext);
			Amount.setWidth(135);
			Amount.setTextSize(15);
			Amount.setGravity(Gravity.CENTER);
			Amount.setTextColor(Color.WHITE);
			Amount.setText("Bill Amount");
			
			TextView Cash = new TextView(activityContext);
			Cash.setWidth(120);
			Cash.setTextSize(15);
			Cash.setTextColor(Color.WHITE);
			Cash.setText("Cash Payment");
			
			TextView Card = new TextView(activityContext);
			Card.setWidth(120);
			Card.setTextSize(15);
			Card.setTextColor(Color.WHITE);
			Card.setText("Card Payment");
			
			TextView Coupon = new TextView(activityContext);
			Coupon.setWidth(120);
			Coupon.setTextSize(15);
			Coupon.setTextColor(Color.WHITE);
			Coupon.setText("Coupon Payment");

            TextView PettyCash = new TextView(activityContext);
            PettyCash.setWidth(135);
            PettyCash.setTextSize(15);
            PettyCash.setGravity(Gravity.CENTER);
            PettyCash.setTextColor(Color.WHITE);
            PettyCash.setText("PettyCash Payment");

            TextView eWallet = new TextView(activityContext);
            eWallet.setWidth(120);
            eWallet.setTextSize(15);
            eWallet.setTextColor(Color.WHITE);
            eWallet.setText("Wallet Payment");

			
			// Add views to row
			rowColumnCaption.addView(Date);
			rowColumnCaption.addView(BillNumber);
			rowColumnCaption.addView(TotalItems);
			rowColumnCaption.addView(Amount);
			rowColumnCaption.addView(Cash);
			rowColumnCaption.addView(Card);
			rowColumnCaption.addView(Coupon);
			rowColumnCaption.addView(PettyCash);
			rowColumnCaption.addView(eWallet);

		}
		
		else if(ReportName.equalsIgnoreCase("Tax Report") ||
				ReportName.equalsIgnoreCase("Service Tax Report")){
			
			TextView SNo = new TextView(activityContext);
			SNo.setWidth(130);
			SNo.setTextSize(15);
			SNo.setTextColor(Color.WHITE);
			SNo.setText("SNo");

			TextView Percent = new TextView(activityContext);
			Percent.setWidth(130);
			Percent.setTextSize(15);
			Percent.setTextColor(Color.WHITE);
			Percent.setText("Tax Percent");
			
			TextView Description = new TextView(activityContext);
			Description.setWidth(130);
			Description.setTextSize(15);
			Description.setTextColor(Color.WHITE);
			Description.setText("Description");
			
			TextView Tax = new TextView(activityContext);
			Tax.setWidth(135);
			Tax.setTextSize(15);
			Tax.setGravity(Gravity.CENTER);
			Tax.setTextColor(Color.WHITE);
			Tax.setText("Tax Amount");
			
			TextView Amount = new TextView(activityContext);
			Amount.setWidth(135);
			Amount.setTextSize(15);
			Amount.setGravity(Gravity.CENTER);
			Amount.setTextColor(Color.WHITE);
			Amount.setText("Taxable Amount");
									
			// Add views to row
			rowColumnCaption.addView(SNo);
			rowColumnCaption.addView(Description);
			rowColumnCaption.addView(Percent);
			rowColumnCaption.addView(Tax);
			rowColumnCaption.addView(Amount);
			
		}
		
		else if(ReportName.equalsIgnoreCase("KOT Pending Report") || 
				ReportName.equalsIgnoreCase("KOT Deleted Report")){
			
			TextView Token = new TextView(activityContext);
			Token.setWidth(115);
			Token.setTextSize(15);
			Token.setTextColor(Color.WHITE);
			Token.setText("Token Number");
			
			TextView Table = new TextView(activityContext);
			Table.setWidth(115);
			Table.setTextSize(15);
			Table.setTextColor(Color.WHITE);
			Table.setText("Table Number");
			
			TextView Time = new TextView(activityContext);
			Time.setWidth(125);
			Time.setTextSize(15);
			Time.setTextColor(Color.WHITE);
			Time.setText("Time");
			
			TextView Waiter = new TextView(activityContext);
			Waiter.setWidth(100);
			Waiter.setTextSize(15);
			Waiter.setTextColor(Color.WHITE);
			Waiter.setText("Waiter");
			
			TextView Items = null;
			if (ReportName.equalsIgnoreCase("KOT Pending Report")) {
				Items = new TextView(activityContext);
				Items.setWidth(100);
				Items.setTextSize(15);
				Items.setTextColor(Color.WHITE);
				Items.setText("Items");
			}
			
			TextView Reason = null;
			if (ReportName.equalsIgnoreCase("KOT Deleted Report")) {
				Reason = new TextView(activityContext);
				Reason.setWidth(150);
				Reason.setTextSize(15);
				Reason.setTextColor(Color.WHITE);
				Reason.setText("Reason");
			}
			// Add views to row
			rowColumnCaption.addView(Token);
			rowColumnCaption.addView(Table);
			rowColumnCaption.addView(Time);
			rowColumnCaption.addView(Waiter);
			
			if (ReportName.equalsIgnoreCase("KOT Pending Report")) {
				rowColumnCaption.addView(Items);
			} else {
				rowColumnCaption.addView(Reason);
			}
			
		}
		
		else if(ReportName.equalsIgnoreCase("Item wise Report")){
			
			TextView Number = new TextView(activityContext);
			Number.setWidth(100);
			Number.setTextSize(15);
			Number.setTextColor(Color.WHITE);
			Number.setText("Item No");
			
			TextView Name = new TextView(activityContext);
			Name.setWidth(180);
			Name.setTextSize(15);
			Name.setTextColor(Color.WHITE);
			Name.setText("Item Name");
			
			TextView Quantity = new TextView(activityContext);
			Quantity.setWidth(100);
			Quantity.setTextSize(15);
			Quantity.setTextColor(Color.WHITE);
			Quantity.setText("Sold Quantity");
			
			TextView Amount = new TextView(activityContext);
			Amount.setWidth(100);
			Amount.setTextSize(15);
			Amount.setTextColor(Color.WHITE);
			Amount.setText("Total Price");
			
			TextView SalesTax = new TextView(activityContext);
			SalesTax.setWidth(100);
			SalesTax.setTextSize(15);
			SalesTax.setTextColor(Color.WHITE);
			//SalesTax.setText("Sales Tax");
			SalesTax.setText("      Tax");

			TextView ServiceTax = new TextView(activityContext);
			ServiceTax.setWidth(100);
			ServiceTax.setTextSize(15);
			ServiceTax.setTextColor(Color.WHITE);
			ServiceTax.setText("Service Tax");
			
			TextView Discount = new TextView(activityContext);
			Discount.setWidth(100);
			Discount.setTextSize(15);
			Discount.setTextColor(Color.WHITE);
			Discount.setText("Discount");
			
			TextView Modifier = new TextView(activityContext);
			Modifier.setWidth(125);
			Modifier.setTextSize(15);
			Modifier.setTextColor(Color.WHITE);
			Modifier.setText("Modifier Amount");
			
			// Add views to row
			rowColumnCaption.addView(Number);
			rowColumnCaption.addView(Name);
			rowColumnCaption.addView(Quantity);
			rowColumnCaption.addView(Discount);
			rowColumnCaption.addView(SalesTax);
			//rowColumnCaption.addView(ServiceTax); richa - making single tax
			rowColumnCaption.addView(Amount);
			rowColumnCaption.addView(Modifier);
			
		}
		
		else if(ReportName.equalsIgnoreCase("Department wise Report") || 
				ReportName.equalsIgnoreCase("Category wise Report") || 
				ReportName.equalsIgnoreCase("Kitchen wise Report")){
			
			TextView Code = new TextView(activityContext);
			Code.setWidth(100);
			Code.setTextSize(15);
			Code.setTextColor(Color.WHITE);
			if(ReportName.equalsIgnoreCase("Department wise Report"))
				Code.setText("Dept. Code");
			else if (ReportName.equalsIgnoreCase("Category wise Report"))
				Code.setText("Categ. Code");
			else if (ReportName.equalsIgnoreCase("Kitchen wise Report"))
				Code.setText("Kitchen Code");

			
			TextView Name = new TextView(activityContext);
			Name.setWidth(100);
			Name.setTextSize(15);
			Name.setTextColor(Color.WHITE);
			Name.setText("Name");
			
			TextView Items = new TextView(activityContext);
			Items.setWidth(100);
			Items.setTextSize(15);
			Items.setTextColor(Color.WHITE);
			Items.setText("Total Items");
			
			TextView Amount = new TextView(activityContext);
			Amount.setWidth(100);
			Amount.setTextSize(15);
			Amount.setTextColor(Color.WHITE);
			Amount.setText("Taxable Value");
			
			TextView IGSTTax = new TextView(activityContext);
			IGSTTax.setWidth(100);
			IGSTTax.setTextSize(15);
			IGSTTax.setTextColor(Color.WHITE);
			IGSTTax.setText("IGST Amt");

			TextView SalesTax = new TextView(activityContext);
			SalesTax.setWidth(100);
			SalesTax.setTextSize(15);
			SalesTax.setTextColor(Color.WHITE);
			SalesTax.setText("CGST Amt");
			
			TextView ServiceTax = new TextView(activityContext);
			ServiceTax.setWidth(100);
			ServiceTax.setTextSize(15);
			ServiceTax.setTextColor(Color.WHITE);
			ServiceTax.setText("SGST Amt");

			TextView cessTax = new TextView(activityContext);
			cessTax.setWidth(100);
			cessTax.setTextSize(15);
			cessTax.setTextColor(Color.WHITE);
			cessTax.setText("cess Amt");
			
			TextView Discount = new TextView(activityContext);
			Discount.setWidth(100);
			Discount.setTextSize(15);
			Discount.setTextColor(Color.WHITE);
			Discount.setText("Discount");
			
			// Add views to row
			rowColumnCaption.addView(Code);
			rowColumnCaption.addView(Name);
			rowColumnCaption.addView(Items);
			rowColumnCaption.addView(Discount);
			rowColumnCaption.addView(IGSTTax);
			rowColumnCaption.addView(SalesTax);
			rowColumnCaption.addView(ServiceTax);
			rowColumnCaption.addView(cessTax);
			rowColumnCaption.addView(Amount);
			
		}
		
		else if(ReportName.equalsIgnoreCase("Waiter wise Report") || 
				ReportName.equalsIgnoreCase("Rider wise Report") || 
				ReportName.equalsIgnoreCase("User wise Report") ||
				ReportName.equalsIgnoreCase("Supplier wise Report")){

			TextView Id = new TextView(activityContext);
			Id.setWidth(100);
			Id.setTextSize(15);
			Id.setTextColor(Color.WHITE);
			Id.setText("Id");
			
			TextView Name = new TextView(activityContext);
			Name.setWidth(100);
			Name.setTextSize(15);
			Name.setTextColor(Color.WHITE);
			Name.setText("Name");
			
			TextView Bills = new TextView(activityContext);
			Bills.setWidth(100);
			Bills.setTextSize(15);
			Bills.setTextColor(Color.WHITE);
			Bills.setText("Total Bills");
			
			TextView Amount = new TextView(activityContext);
			Amount.setWidth(100);
			Amount.setTextSize(15);
			Amount.setTextColor(Color.WHITE);
			Amount.setText("Bill Amount");
			
			// Add views to row
			rowColumnCaption.addView(Id);
			rowColumnCaption.addView(Name);
			rowColumnCaption.addView(Bills);
			rowColumnCaption.addView(Amount);
			
		}
		
		else if(ReportName.equalsIgnoreCase("Customer wise Report")){
			
			TextView Id = new TextView(activityContext);
			Id.setWidth(100);
			Id.setTextSize(15);
			Id.setTextColor(Color.WHITE);
			Id.setText("Cust. Id");
			
			TextView Name = new TextView(activityContext);
			Name.setWidth(100);
			Name.setTextSize(15);
			Name.setTextColor(Color.WHITE);
			Name.setText("Name");
			
			TextView Bills = new TextView(activityContext);
			Bills.setWidth(100);
			Bills.setTextSize(15);
			Bills.setTextColor(Color.WHITE);
			Bills.setText("Total Bills");
			
			TextView LastTransaction = new TextView(activityContext);
			LastTransaction.setWidth(125);
			LastTransaction.setTextSize(15);
			LastTransaction.setTextColor(Color.WHITE);
			LastTransaction.setText("Last Transaction");

			TextView cash = new TextView(activityContext);
			cash.setWidth(120);
			cash.setTextSize(15);
			cash.setTextColor(Color.WHITE);
			cash.setText("Cash Payment  ");


			TextView card = new TextView(activityContext);
			card.setWidth(120);
			card.setTextSize(15);
			card.setTextColor(Color.WHITE);
			card.setText("Card Payment");

			TextView coupon = new TextView(activityContext);
			coupon.setWidth(130);
			coupon.setTextSize(15);
			coupon.setTextColor(Color.WHITE);
			coupon.setText("Coupon Payment");

			TextView credit = new TextView(activityContext);
			credit.setWidth(120);
			credit.setTextSize(15);
			credit.setTextColor(Color.WHITE);
			credit.setText("Credit Payment");

			TextView wallet = new TextView(activityContext);
			wallet.setWidth(120);
			wallet.setTextSize(15);
			wallet.setTextColor(Color.WHITE);
			wallet.setText("Wallet Payment");
			
			TextView TotalTransaction = new TextView(activityContext);
			TotalTransaction.setWidth(125);
			TotalTransaction.setTextSize(15);
			TotalTransaction.setTextColor(Color.WHITE);
			TotalTransaction.setText("Total Transaction");
			
			// Add views to row
			rowColumnCaption.addView(Id);
			rowColumnCaption.addView(Name);
			rowColumnCaption.addView(Bills);
			rowColumnCaption.addView(LastTransaction);
			rowColumnCaption.addView(cash);
			rowColumnCaption.addView(card);
			rowColumnCaption.addView(coupon);
			rowColumnCaption.addView(credit);
			rowColumnCaption.addView(wallet);
			rowColumnCaption.addView(TotalTransaction);
			
		}
		
		else if(ReportName.equalsIgnoreCase("Waiter Detailed Report") ||
				ReportName.equalsIgnoreCase("Rider Detailed Report") ||
				ReportName.equalsIgnoreCase("User Detailed Report"))
		{
			
			TextView Date = new TextView(activityContext);
			Date.setWidth(100);
			Date.setTextSize(15);
			Date.setTextColor(Color.WHITE);
			Date.setText("Date");
			
			TextView BillNumber = new TextView(activityContext);
			BillNumber.setWidth(100);
			BillNumber.setTextSize(15);
			BillNumber.setTextColor(Color.WHITE);
			BillNumber.setText("Bill Number");
			
			TextView TotalItems = new TextView(activityContext);
			TotalItems.setWidth(100);
			TotalItems.setTextSize(15);
			TotalItems.setTextColor(Color.WHITE);
			TotalItems.setText("Total Items");
			
			TextView Discount = new TextView(activityContext);
			Discount.setWidth(100);
			Discount.setTextSize(15);
			Discount.setTextColor(Color.WHITE);
			Discount.setText("Discount");
			
			TextView SalesTax = new TextView(activityContext);
			SalesTax.setWidth(105);
			SalesTax.setTextSize(15);
            SalesTax.setGravity(Gravity.CENTER);
			SalesTax.setTextColor(Color.WHITE);
			SalesTax.setText("Tax");
			
			/*TextView ServiceTax = new TextView(activityContext);
			ServiceTax.setWidth(115);
			ServiceTax.setTextSize(15);
			ServiceTax.setTextColor(Color.WHITE);
			ServiceTax.setText("Service Tax");*/
			
			TextView Amount = new TextView(activityContext);
			Amount.setWidth(100);
			Amount.setTextSize(15);
			Amount.setTextColor(Color.WHITE);
			Amount.setText("Bill Amount");
			
			// Add views to row
			rowColumnCaption.addView(Date);
			rowColumnCaption.addView(BillNumber);
			rowColumnCaption.addView(TotalItems);
			rowColumnCaption.addView(Discount);
			rowColumnCaption.addView(SalesTax);
			//rowColumnCaption.addView(ServiceTax);
			rowColumnCaption.addView(Amount);
			
		}

		else if(ReportName.equalsIgnoreCase("Customer Detailed Report"))
		{
			TextView Date = new TextView(activityContext);
			Date.setWidth(100);
			Date.setTextSize(15);
			Date.setTextColor(Color.WHITE);
			Date.setText("Date");

			TextView BillNumber = new TextView(activityContext);
			BillNumber.setWidth(100);
			BillNumber.setTextSize(15);
			BillNumber.setTextColor(Color.WHITE);
			BillNumber.setText("Bill Number");

			TextView TotalItems = new TextView(activityContext);
			TotalItems.setWidth(100);
			TotalItems.setTextSize(15);
			TotalItems.setTextColor(Color.WHITE);
			TotalItems.setText("Total Items");

			TextView Discount = new TextView(activityContext);
			Discount.setWidth(100);
			Discount.setTextSize(15);
			Discount.setTextColor(Color.WHITE);
			Discount.setText("Discount");

			TextView cash = new TextView(activityContext);
			cash.setWidth(120);
			cash.setTextSize(15);
			cash.setTextColor(Color.WHITE);
			cash.setText("Cash Payment  ");


			TextView card = new TextView(activityContext);
			card.setWidth(120);
			card.setTextSize(15);
			card.setTextColor(Color.WHITE);
			card.setText("Card Payment");

			TextView coupon = new TextView(activityContext);
			coupon.setWidth(130);
			coupon.setTextSize(15);
			coupon.setTextColor(Color.WHITE);
			coupon.setText("Coupon Payment");

			TextView credit = new TextView(activityContext);
			credit.setWidth(120);
			credit.setTextSize(15);
			credit.setTextColor(Color.WHITE);
			credit.setText("Credit Payment");

			TextView wallet = new TextView(activityContext);
			wallet.setWidth(120);
			wallet.setTextSize(15);
			wallet.setTextColor(Color.WHITE);
			wallet.setText("Wallet Payment");

			TextView Amount = new TextView(activityContext);
			Amount.setWidth(120);
			Amount.setTextSize(15);
			Amount.setTextColor(Color.WHITE);
			Amount.setText("Bill Amount");

			// Add views to row
			rowColumnCaption.addView(Date);
			rowColumnCaption.addView(BillNumber);
			rowColumnCaption.addView(TotalItems);
			rowColumnCaption.addView(Discount);
			rowColumnCaption.addView(cash);
			rowColumnCaption.addView(card);
			rowColumnCaption.addView(coupon);
			rowColumnCaption.addView(credit);
			rowColumnCaption.addView(wallet);
			rowColumnCaption.addView(Amount);

		}
		
		else if(ReportName.equalsIgnoreCase("Payments Report") || 
				ReportName.equalsIgnoreCase("Receipts Report")){
			
			TextView Date = new TextView(activityContext);
			Date.setWidth(100);
			Date.setTextSize(15);
			Date.setTextColor(Color.WHITE);
			Date.setText("Date");
			
			TextView Description = new TextView(activityContext);
			Description.setWidth(100);
			Description.setTextSize(15);
			Description.setTextColor(Color.WHITE);
			Description.setText("Description");
			
			TextView Reason = new TextView(activityContext);
			Reason.setWidth(130);
			Reason.setTextSize(15);
			Reason.setTextColor(Color.WHITE);
			Reason.setText("Reason");
			
			TextView Amount = new TextView(activityContext);
			Amount.setWidth(100);
			Amount.setTextSize(15);
			Amount.setTextColor(Color.WHITE);
			Amount.setText("Bill Amount");
			
			// Add views to row
			rowColumnCaption.addView(Date);
			rowColumnCaption.addView(Description);
			rowColumnCaption.addView(Amount);
			rowColumnCaption.addView(Reason);
			
		}
		else if(ReportName.equalsIgnoreCase("Fast Selling Itemwise Report")){

			TextView SNo = new TextView(activityContext);
			SNo.setWidth(50);
			SNo.setTextSize(15);
			SNo.setTextColor(Color.WHITE);
			SNo.setText("SNo");

			TextView MenuCode = new TextView(activityContext);
			MenuCode.setWidth(120);
			MenuCode.setTextSize(15);
			MenuCode.setTextColor(Color.WHITE);
			MenuCode.setText("MenuCode");


			TextView DeptName = new TextView(activityContext);
			DeptName.setWidth(120);
			DeptName.setTextSize(15);
			DeptName.setTextColor(Color.WHITE);
			DeptName.setText("Dept Name");

			TextView CategName = new TextView(activityContext);
			CategName.setWidth(120);
			CategName.setTextSize(15);
			CategName.setTextColor(Color.WHITE);
			CategName.setText("Categ Name");

			TextView ItemName = new TextView(activityContext);
			ItemName.setWidth(250);
			ItemName.setTextSize(15);
			ItemName.setTextColor(Color.WHITE);
			ItemName.setText("Item Name");

			TextView Qty = new TextView(activityContext);
			Qty.setWidth(150);
			Qty.setTextSize(15);
			Qty.setPadding(5,0,0,0);
			Qty.setTextColor(Color.WHITE);
			Qty.setText("Qty");

			TextView Rate = new TextView(activityContext);
			Rate.setWidth(150);
			Rate.setTextSize(15);
            Rate.setGravity(Gravity.CENTER);
			Rate.setTextColor(Color.WHITE);
			Rate.setText("Total Price");

			// Add views to row
			rowColumnCaption.addView(SNo);
			rowColumnCaption.addView(MenuCode);
			rowColumnCaption.addView(DeptName);
			rowColumnCaption.addView(CategName);
			rowColumnCaption.addView(ItemName);
			rowColumnCaption.addView(Qty);
			rowColumnCaption.addView(Rate);
		}
		else if(ReportName.equalsIgnoreCase("GSTR2-B2C")) {

			TextView SNo = new TextView(activityContext);
			SNo.setWidth(60);
			SNo.setTextSize(15);
			SNo.setTextColor(Color.WHITE);
			SNo.setText("S.No");

			TextView Description = new TextView(activityContext);
			Description.setWidth(160);
			Description.setTextSize(15);
			Description.setTextColor(Color.WHITE);
			Description.setText("Description");

			TextView HSN = new TextView(activityContext);
			HSN.setWidth(100);
			HSN.setTextSize(15);
			HSN.setTextColor(Color.WHITE);
			HSN.setText("HSN/SAC");

			TextView Compounding = new TextView(activityContext);
			Compounding.setWidth(160);
			Compounding.setTextSize(15);
			Compounding.setTextColor(Color.WHITE);
			Compounding.setText("Compounding Person");

			TextView Unregistered = new TextView(activityContext);
			Unregistered.setWidth(160);
			Unregistered.setTextSize(15);
			Unregistered.setTextColor(Color.WHITE);
			Unregistered.setText("Unregistered Person");

			TextView ExemptSupply = new TextView(activityContext);
			ExemptSupply.setWidth(160);
			ExemptSupply.setTextSize(15);
			ExemptSupply.setTextColor(Color.WHITE);
			ExemptSupply.setText("Exempt Supply");

			TextView NilRated = new TextView(activityContext);
			NilRated.setWidth(160);
			NilRated.setTextSize(15);
			NilRated.setTextColor(Color.WHITE);
			NilRated.setText("Nil Rated Supply");

			TextView NonGST = new TextView(activityContext);
			NonGST.setWidth(160);
			NonGST.setTextSize(15);
			NonGST.setTextColor(Color.WHITE);
			NonGST.setText("NonGST Supply");

			// Add views to row
			rowColumnCaption.addView(SNo);
			rowColumnCaption.addView(Description);
			rowColumnCaption.addView(HSN);
			rowColumnCaption.addView(Compounding);
			rowColumnCaption.addView(Unregistered);
			rowColumnCaption.addView(ExemptSupply);
			rowColumnCaption.addView(NilRated);
			rowColumnCaption.addView(NonGST);
		}else if(ReportName.equalsIgnoreCase("GSTR1-CDN")) {

			TextView SNo = new TextView(activityContext);
			SNo.setWidth(50);
			SNo.setTextSize(15);
			SNo.setTextColor(Color.WHITE);
			SNo.setText("S.No");

			TextView NoteType = new TextView(activityContext);
            NoteType.setWidth(60);
            NoteType.setTextSize(15);
            NoteType.setTextColor(Color.WHITE);
            NoteType.setText("Note Type");

			TextView NoteNo = new TextView(activityContext);
            NoteNo.setWidth(85);
            NoteNo.setTextSize(15);
            NoteNo.setTextColor(Color.WHITE);
            NoteNo.setText("Note No");

			TextView NoteDate = new TextView(activityContext);
            NoteDate.setWidth(110);
            NoteDate.setTextSize(15);
            NoteDate.setTextColor(Color.WHITE);
            NoteDate.setText("Note Date");

			TextView InvoiceNo = new TextView(activityContext);
            InvoiceNo.setWidth(85);
            InvoiceNo.setTextSize(15);
            InvoiceNo.setTextColor(Color.WHITE);
            InvoiceNo.setText("Invoice No");

			TextView InvoiceDate = new TextView(activityContext);
            InvoiceDate.setWidth(110);
            InvoiceDate.setTextSize(15);
            InvoiceDate.setTextColor(Color.WHITE);
            InvoiceDate.setText("Invoice Date");

			TextView Reason = new TextView(activityContext);
            Reason.setWidth(180);
            Reason.setTextSize(15);
            Reason.setTextColor(Color.WHITE);
            Reason.setGravity(Gravity.CENTER);
            Reason.setText("Reason");

			TextView DifferentialValue = new TextView(activityContext);
            DifferentialValue.setWidth(100);
            DifferentialValue.setTextSize(15);
            DifferentialValue.setGravity(Gravity.CENTER);
            DifferentialValue.setTextColor(Color.WHITE);
            DifferentialValue.setText("Differential    Value");

			TextView IAmt = new TextView(activityContext);
            IAmt.setWidth(65);
            IAmt.setTextSize(15);
            IAmt.setGravity(Gravity.CENTER);
            IAmt.setTextColor(Color.WHITE);
            IAmt.setText("IGST   Amt");

			TextView CAmt = new TextView(activityContext);
            CAmt.setWidth(65);
            CAmt.setGravity(Gravity.CENTER);
            CAmt.setTextSize(15);
            CAmt.setTextColor(Color.WHITE);
            CAmt.setText("CGST Amt");

			TextView SAmt = new TextView(activityContext);
            SAmt.setWidth(65);
            SAmt.setGravity(Gravity.CENTER);
            SAmt.setTextSize(15);
            SAmt.setTextColor(Color.WHITE);
            SAmt.setText("SGST Amt");

            TextView cessAmt = new TextView(activityContext);
            cessAmt.setWidth(65);
            cessAmt.setTextSize(15);
            cessAmt.setGravity(Gravity.CENTER);
            cessAmt.setTextColor(Color.WHITE);
            cessAmt.setText("cess   Amt");

            TextView GSTRate = new TextView(activityContext);
            GSTRate.setWidth(65);
            GSTRate.setTextSize(15);
            GSTRate.setTextColor(Color.WHITE);
            GSTRate.setGravity(Gravity.CENTER);
            GSTRate.setText("GST Rate");



			// Add views to row
			rowColumnCaption.addView(SNo);
            rowColumnCaption.addView(NoteType);
            rowColumnCaption.addView(NoteNo);
            rowColumnCaption.addView(NoteDate);
            rowColumnCaption.addView(InvoiceNo);
            rowColumnCaption.addView(InvoiceDate);
            rowColumnCaption.addView(Reason);
            rowColumnCaption.addView(GSTRate);
            rowColumnCaption.addView(DifferentialValue);
            rowColumnCaption.addView(IAmt);
            rowColumnCaption.addView(CAmt);
            rowColumnCaption.addView(SAmt);
            rowColumnCaption.addView(cessAmt);
		}else if(ReportName.equalsIgnoreCase("GSTR2_Registered_Amend") ||
                ReportName.equalsIgnoreCase("GSTR2_UnRegistered_Amend")) {

			TextView SNo = new TextView(activityContext);
			SNo.setWidth(60);
			SNo.setTextSize(15);
			SNo.setTextColor(Color.WHITE);
			SNo.setText("S.No");

			TextView GSTIN = new TextView(activityContext);
			GSTIN.setWidth(120);
            GSTIN.setTextSize(15);
            GSTIN.setTextColor(Color.WHITE);
            if(ReportName.equalsIgnoreCase("GSTR2-B2BA"))
                GSTIN.setText("GSTIN");
            else
                GSTIN.setText("Supplier Name");

            TextView InvoiceNo_ori = new TextView(activityContext);
			InvoiceNo_ori.setWidth(90);
			InvoiceNo_ori.setTextSize(15);
			InvoiceNo_ori.setTextColor(Color.WHITE);
			InvoiceNo_ori.setText("Ori. Invoice No");

			TextView InvoiceDate_ori = new TextView(activityContext);
			InvoiceDate_ori.setWidth(110);
			InvoiceDate_ori.setTextSize(15);
			InvoiceDate_ori.setTextColor(Color.WHITE);
			InvoiceDate_ori.setText("Ori. Invoice  Date");

			TextView InvoiceNo = new TextView(activityContext);
			InvoiceNo.setWidth(85);
			InvoiceNo.setTextSize(15);
			InvoiceNo.setTextColor(Color.WHITE);
			InvoiceNo.setText("Invoice No");

			TextView InvoiceDate = new TextView(activityContext);
			InvoiceDate.setWidth(110);
			InvoiceDate.setTextSize(15);
			InvoiceDate.setTextColor(Color.WHITE);
			InvoiceDate.setText("Invoice Date");

			TextView HSN = new TextView(activityContext);
			HSN.setWidth(70);
			HSN.setTextSize(15);
			HSN.setTextColor(Color.WHITE);
			HSN.setText("HSNCode");

			TextView SupplyType = new TextView(activityContext);
			SupplyType.setWidth(50);
			SupplyType.setTextSize(15);
			SupplyType.setTextColor(Color.WHITE);
			SupplyType.setText("G/S");

			TextView Value = new TextView(activityContext);
			Value.setWidth(100);
			Value.setTextSize(15);
			Value.setTextColor(Color.WHITE);
			Value.setText("Value");
            Value.setGravity(Gravity.CENTER);

			TextView TaxableValue = new TextView(activityContext);
			TaxableValue.setWidth(100);
			TaxableValue.setTextSize(15);
			TaxableValue.setTextColor(Color.WHITE);
			TaxableValue.setText("Taxable   Value");

			TextView IAMT = new TextView(activityContext);
			IAMT.setWidth(65);
			IAMT.setTextSize(15);
			IAMT.setTextColor(Color.WHITE);
			IAMT.setText("IGST   Amount");

			TextView CAMT = new TextView(activityContext);
			CAMT.setWidth(65);
			CAMT.setTextSize(15);
			CAMT.setTextColor(Color.WHITE);
			CAMT.setText("CGST   Amount");

			TextView SAMT = new TextView(activityContext);
			SAMT.setWidth(65);
			SAMT.setTextSize(15);
			SAMT.setTextColor(Color.WHITE);
			SAMT.setText("SGST   Amount");

            TextView Subtotal = new TextView(activityContext);
            Subtotal.setWidth(100);
            Subtotal.setTextSize(15);
            Subtotal.setGravity(Gravity.CENTER);
            Subtotal.setTextColor(Color.WHITE);
            Subtotal.setText("Sub Total");

            TextView SupplierPOS = new TextView(activityContext);
            SupplierPOS.setWidth(60);
            SupplierPOS.setTextSize(15);
            SupplierPOS.setGravity(Gravity.CENTER);
            SupplierPOS.setTextColor(Color.WHITE);
            SupplierPOS.setText("Supplier POS ");



			// Add views to row
			rowColumnCaption.addView(SNo);
			rowColumnCaption.addView(GSTIN);
			rowColumnCaption.addView(InvoiceNo_ori);
			rowColumnCaption.addView(InvoiceDate_ori);
			rowColumnCaption.addView(InvoiceNo);
			rowColumnCaption.addView(InvoiceDate);
			rowColumnCaption.addView(SupplyType);
			rowColumnCaption.addView(HSN);
			rowColumnCaption.addView(Value);
			rowColumnCaption.addView(TaxableValue);
			rowColumnCaption.addView(IAMT);
			rowColumnCaption.addView(CAMT);
			rowColumnCaption.addView(SAMT);
			rowColumnCaption.addView(Subtotal);
			rowColumnCaption.addView(SupplierPOS);
		}
		else if(ReportName.equalsIgnoreCase("GSTR2-Registered") || ReportName.equalsIgnoreCase("GSTR2-UnRegistered")){

			TextView SNo = new TextView(activityContext);
			SNo.setWidth(60);
			SNo.setTextSize(15);
			SNo.setTextColor(Color.WHITE);
			//SNo.setBackgroundResource(R.drawable.border_tab_heading);
			SNo.setText("S.No");

			TextView GSTIN = new TextView(activityContext);
			GSTIN.setWidth(150);
			GSTIN.setTextSize(15);
			//GSTIN.setBackgroundResource(R.drawable.border_tab_heading);
			GSTIN.setTextColor(Color.WHITE);
			if(ReportName.equalsIgnoreCase("GSTR2-Registered"))
				GSTIN.setText("Supplier GSTIN");
			else
				GSTIN.setText("Supplier Name");


			TextView InvNo = new TextView(activityContext);
			InvNo.setWidth(85);
			InvNo.setTextSize(15);
			InvNo.setTextColor(Color.WHITE);
			InvNo.setText("Invoice No.");

			TextView InvDate = new TextView(activityContext);
			InvDate.setWidth(110);
			InvDate.setTextSize(15);
			InvDate.setTextColor(Color.WHITE);
			InvDate.setText("Invoice Date");

			TextView G_S = new TextView(activityContext);
			G_S.setWidth(50);
			G_S.setTextSize(15);
			G_S.setTextColor(Color.WHITE);
			G_S.setText("G/S");

			TextView HSN = new TextView(activityContext);
			HSN.setWidth(70);
			HSN.setTextSize(15);
			HSN.setTextColor(Color.WHITE);
			HSN.setText("HSNCode");

			TextView Value = new TextView(activityContext);
			Value.setWidth(100);
			Value.setTextSize(15);
			Value.setTextColor(Color.WHITE);
			Value.setText("  Value");

			TextView TaxableValue = new TextView(activityContext);
			TaxableValue.setWidth(100);
			TaxableValue.setTextSize(15);
			TaxableValue.setTextColor(Color.WHITE);
			TaxableValue.setText("Taxable   Value");

			TextView IGSTRate = new TextView(activityContext);
			IGSTRate.setWidth(65);
			IGSTRate.setTextSize(15);
			IGSTRate.setTextColor(Color.WHITE);
			IGSTRate.setText("IGST Rate");

			TextView IGSTAmt = new TextView(activityContext);
			IGSTAmt.setWidth(65);
			IGSTAmt.setTextSize(15);
			IGSTAmt.setTextColor(Color.WHITE);
			IGSTAmt.setText("IGST   Amt");

			TextView CGSTRate = new TextView(activityContext);
			CGSTRate.setWidth(65);
			CGSTRate.setTextSize(15);
			CGSTRate.setTextColor(Color.WHITE);
			CGSTRate.setText("CGST Rate");

			TextView CGSTAmt = new TextView(activityContext);
			CGSTAmt.setWidth(65);
			CGSTAmt.setTextSize(15);
			CGSTAmt.setTextColor(Color.WHITE);
			CGSTAmt.setText("CGST Amt");

			TextView SGSTRate = new TextView(activityContext);
			SGSTRate.setWidth(65);
			SGSTRate.setTextSize(15);
			SGSTRate.setTextColor(Color.WHITE);
			SGSTRate.setText("SGST Rate");

			TextView SGSTAmt = new TextView(activityContext);
			SGSTAmt.setWidth(65);
			SGSTAmt.setTextSize(15);
			SGSTAmt.setTextColor(Color.WHITE);
			SGSTAmt.setText("SGST Amt");

            TextView cessAmt = new TextView(activityContext);
            cessAmt.setWidth(65);
            cessAmt.setTextSize(15);
            cessAmt.setTextColor(Color.WHITE);
            cessAmt.setText("cess Amt");

			TextView SubTotal = new TextView(activityContext);
			SubTotal.setWidth(100);
			SubTotal.setTextSize(15);
			SubTotal.setGravity(Gravity.CENTER);
			SubTotal.setTextColor(Color.WHITE);
			SubTotal.setText("Amount");

			TextView POS = new TextView(activityContext);
			POS.setWidth(50);
			POS.setTextSize(15);
			POS.setTextColor(Color.WHITE);
			POS.setText("POS");

			TextView ITC = new TextView(activityContext);
			ITC.setWidth(80);
			ITC.setTextSize(15);
			ITC.setTextColor(Color.WHITE);
			ITC.setText("ITC ");


			// Add views to row
			rowColumnCaption.addView(SNo);
			rowColumnCaption.addView(GSTIN);
			rowColumnCaption.addView(InvNo);
			rowColumnCaption.addView(InvDate);
			rowColumnCaption.addView(G_S);
			rowColumnCaption.addView(HSN);

			rowColumnCaption.addView(Value);
			rowColumnCaption.addView(TaxableValue);
			//rowColumnCaption.addView(IGSTRate);
			rowColumnCaption.addView(IGSTAmt);
			//rowColumnCaption.addView(CGSTRate);
			rowColumnCaption.addView(CGSTAmt);

			//rowColumnCaption.addView(SGSTRate);
			rowColumnCaption.addView(SGSTAmt);
			rowColumnCaption.addView(cessAmt);
            rowColumnCaption.addView(SubTotal);
			/*rowColumnCaption.addView(POS);
			rowColumnCaption.addView(ITC);*/

		} else if(ReportName.equalsIgnoreCase("GSTR1-HSN Summary")){

			TextView SNo = new TextView(activityContext);
			SNo.setWidth(60);
			SNo.setTextSize(15);
			SNo.setTextColor(Color.WHITE);
			SNo.setText("S.No");





			TextView G_S = new TextView(activityContext);
			G_S.setWidth(50);
			G_S.setTextSize(15);
			G_S.setTextColor(Color.WHITE);
			G_S.setText("G/S");

			TextView Description = new TextView(activityContext);
            Description.setWidth(100);
            Description.setTextSize(15);
            Description.setTextColor(Color.WHITE);
            Description.setText("Description");
            Description.setGravity(Gravity.CENTER);

            TextView HSN = new TextView(activityContext);
			HSN.setWidth(100);
			HSN.setTextSize(15);
			HSN.setTextColor(Color.WHITE);
			HSN.setText("HSN/SAC");
            //HSN.setGravity(Gravity.CENTER);


            TextView UOM = new TextView(activityContext);
            UOM.setWidth(60);
            UOM.setTextSize(15);
            UOM.setTextColor(Color.WHITE);
            UOM.setText("UOM");
            UOM.setGravity(Gravity.CENTER);

			TextView Value = new TextView(activityContext);
			Value.setWidth(100);
			Value.setTextSize(15);
            Value.setGravity(Gravity.CENTER);
			Value.setTextColor(Color.WHITE);
			Value.setText("Value");

			TextView Quantity = new TextView(activityContext);
			Quantity.setWidth(100);
			Quantity.setTextSize(15);
			Quantity.setTextColor(Color.WHITE);
			Quantity.setText("Qty");
			Quantity.setGravity(Gravity.CENTER);

			TextView TaxableValue = new TextView(activityContext);
			TaxableValue.setWidth(100);
			TaxableValue.setTextSize(15);
            TaxableValue.setGravity(Gravity.CENTER);
			TaxableValue.setTextColor(Color.WHITE);
			TaxableValue.setText("Taxable       Value");

			TextView GSTRate = new TextView(activityContext);
			GSTRate.setWidth(75);
			GSTRate.setTextSize(15);
			GSTRate.setTextColor(Color.WHITE);
			GSTRate.setText("GST Rate");

			/*TextView IGSTRate = new TextView(activityContext);
			IGSTRate.setWidth(65);
			IGSTRate.setTextSize(15);
			IGSTRate.setTextColor(Color.WHITE);
			IGSTRate.setText("IGST Rate");*/
			TextView IGSTAmt = new TextView(activityContext);
			IGSTAmt.setWidth(75);
			IGSTAmt.setTextSize(15);
            IGSTAmt.setGravity(Gravity.CENTER);
			IGSTAmt.setTextColor(Color.WHITE);
			IGSTAmt.setText("IGST     Amt");

			/*TextView CGSTRate = new TextView(activityContext);
			CGSTRate.setWidth(65);
			CGSTRate.setTextSize(15);
			CGSTRate.setTextColor(Color.WHITE);
			CGSTRate.setText("CGST Rate");*/

			TextView CGSTAmt = new TextView(activityContext);
			CGSTAmt.setWidth(75);
			CGSTAmt.setTextSize(15);
			CGSTAmt.setGravity(Gravity.CENTER);
			CGSTAmt.setTextColor(Color.WHITE);
			CGSTAmt.setText("CGST   Amt");

			/*TextView SGSTRate = new TextView(activityContext);
			SGSTRate.setWidth(65);
			SGSTRate.setTextSize(15);
			SGSTRate.setTextColor(Color.WHITE);
			SGSTRate.setText("SGST Rate");*/

			TextView SGSTAmt = new TextView(activityContext);
			SGSTAmt.setWidth(75);
			SGSTAmt.setTextSize(15);
			SGSTAmt.setTextColor(Color.WHITE);
			SGSTAmt.setGravity(Gravity.CENTER);
			SGSTAmt.setText("SGST      Amt");
			
			TextView cessAmt = new TextView(activityContext);
			cessAmt.setWidth(75);
			cessAmt.setTextSize(15);
			cessAmt.setGravity(Gravity.CENTER);
			cessAmt.setTextColor(Color.WHITE);
			cessAmt.setText("cess      Amt");

			TextView SubTotal = new TextView(activityContext);
			SubTotal.setWidth(105);
			SubTotal.setTextSize(15);
			SubTotal.setTextColor(Color.WHITE);
			SubTotal.setText("Sub Total");

			TextView POS = new TextView(activityContext);
			POS.setWidth(75);
			POS.setTextSize(15);
			POS.setTextColor(Color.WHITE);
			POS.setText("Cust StateCode");




			// Add views to row
			rowColumnCaption.addView(SNo);

			//rowColumnCaption.addView(G_S);
			rowColumnCaption.addView(HSN);
			rowColumnCaption.addView(Description);

			rowColumnCaption.addView(UOM);
			rowColumnCaption.addView(Quantity);
			rowColumnCaption.addView(Value);

			rowColumnCaption.addView(GSTRate);
            rowColumnCaption.addView(TaxableValue);
			//rowColumnCaption.addView(IGSTRate);
			rowColumnCaption.addView(IGSTAmt);
			//rowColumnCaption.addView(CGSTRate);
			rowColumnCaption.addView(CGSTAmt);

			//rowColumnCaption.addView(SGSTRate);
			rowColumnCaption.addView(SGSTAmt);
			rowColumnCaption.addView(cessAmt);
			//rowColumnCaption.addView(SubTotal);


		}else if(ReportName.equalsIgnoreCase("GSTR4-Composite Report")){




			TextView TaxableValue = new TextView(activityContext);
			TaxableValue.setWidth(200);
			TaxableValue.setTextSize(15);
            TaxableValue.setGravity(Gravity.END);
			TaxableValue.setTextColor(Color.WHITE);
			TaxableValue.setText("Taxable Value");

			TextView GSTRate = new TextView(activityContext);
			GSTRate.setWidth(75);
			GSTRate.setTextSize(15);
			GSTRate.setGravity(Gravity.START);
			GSTRate.setTextColor(Color.WHITE);
			GSTRate.setText("Tax Rate(%)");


			TextView CGSTAmt = new TextView(activityContext);
			CGSTAmt.setWidth(150);
			CGSTAmt.setTextSize(15);
			CGSTAmt.setGravity(Gravity.END);
			CGSTAmt.setTextColor(Color.WHITE);
			CGSTAmt.setText("CGST Amt");



			TextView SGSTAmt = new TextView(activityContext);
			SGSTAmt.setWidth(150);
			SGSTAmt.setTextSize(15);
			SGSTAmt.setTextColor(Color.WHITE);
			SGSTAmt.setGravity(Gravity.END);
			SGSTAmt.setText("SGST Amt");

			TextView cessAmt = new TextView(activityContext);
			cessAmt.setWidth(10);
			cessAmt.setTextSize(15);
			cessAmt.setGravity(Gravity.CENTER);
			cessAmt.setTextColor(Color.WHITE);
			//cessAmt.setText("cess      Amt");


			rowColumnCaption.addView(GSTRate);
            rowColumnCaption.addView(TaxableValue);
			rowColumnCaption.addView(CGSTAmt);
			rowColumnCaption.addView(SGSTAmt);
			rowColumnCaption.addView(cessAmt);

		}else if(ReportName.equalsIgnoreCase("GSTR1-B2B")){

			TextView SNo = new TextView(activityContext);
			SNo.setWidth(60);
			SNo.setTextSize(15);
			SNo.setTextColor(Color.WHITE);
			SNo.setText("S.No");

			TextView GSTIN = new TextView(activityContext);
			GSTIN.setWidth(140);
			GSTIN.setTextSize(15);
			GSTIN.setTextColor(Color.WHITE);
			GSTIN.setText("Recipient GSTIN");

			TextView InvNo = new TextView(activityContext);
			InvNo.setWidth(80);
			InvNo.setTextSize(15);
			InvNo.setTextColor(Color.WHITE);
			InvNo.setText("Invoice    No.");

			TextView InvDate = new TextView(activityContext);
			InvDate.setWidth(110);
			InvDate.setTextSize(15);
			InvDate.setTextColor(Color.WHITE);
			InvDate.setText("Invoice Date");

			TextView G_S = new TextView(activityContext);
			G_S.setWidth(50);
			G_S.setTextSize(15);
			G_S.setTextColor(Color.WHITE);
			G_S.setText("G/S");

			TextView HSN = new TextView(activityContext);
			HSN.setWidth(100);
			HSN.setTextSize(15);
			HSN.setTextColor(Color.WHITE);
			HSN.setText("HSN/SAC");

			TextView Value = new TextView(activityContext);
			Value.setWidth(100);
			Value.setTextSize(15);
            Value.setGravity(Gravity.CENTER);
			Value.setTextColor(Color.WHITE);
			Value.setText("Value");

			TextView Quantity = new TextView(activityContext);
			Quantity.setWidth(40);
			Quantity.setTextSize(15);
			Quantity.setTextColor(Color.WHITE);
			Quantity.setText("Qty");

			TextView TaxableValue = new TextView(activityContext);
			TaxableValue.setWidth(100);
			TaxableValue.setTextSize(15);
            TaxableValue.setGravity(Gravity.CENTER);
			TaxableValue.setTextColor(Color.WHITE);
			TaxableValue.setText("Taxable       Value");

			TextView GSTRate = new TextView(activityContext);
			GSTRate.setWidth(75);
			GSTRate.setTextSize(15);
			GSTRate.setTextColor(Color.WHITE);
			GSTRate.setText("GST Rate");

			/*TextView IGSTRate = new TextView(activityContext);
			IGSTRate.setWidth(65);
			IGSTRate.setTextSize(15);
			IGSTRate.setTextColor(Color.WHITE);
			IGSTRate.setText("IGST Rate");*/
			TextView IGSTAmt = new TextView(activityContext);
			IGSTAmt.setWidth(75);
			IGSTAmt.setTextSize(15);
            IGSTAmt.setGravity(Gravity.CENTER);
			IGSTAmt.setTextColor(Color.WHITE);
			IGSTAmt.setText("IGST     Amt");

			/*TextView CGSTRate = new TextView(activityContext);
			CGSTRate.setWidth(65);
			CGSTRate.setTextSize(15);
			CGSTRate.setTextColor(Color.WHITE);
			CGSTRate.setText("CGST Rate");*/

			TextView CGSTAmt = new TextView(activityContext);
			CGSTAmt.setWidth(75);
			CGSTAmt.setTextSize(15);
			CGSTAmt.setGravity(Gravity.CENTER);
			CGSTAmt.setTextColor(Color.WHITE);
			CGSTAmt.setText("CGST   Amt");

			/*TextView SGSTRate = new TextView(activityContext);
			SGSTRate.setWidth(65);
			SGSTRate.setTextSize(15);
			SGSTRate.setTextColor(Color.WHITE);
			SGSTRate.setText("SGST Rate");*/

			TextView SGSTAmt = new TextView(activityContext);
			SGSTAmt.setWidth(75);
			SGSTAmt.setTextSize(15);
			SGSTAmt.setTextColor(Color.WHITE);
			SGSTAmt.setGravity(Gravity.CENTER);
			SGSTAmt.setText("SGST      Amt");

			TextView cessAmt = new TextView(activityContext);
			cessAmt.setWidth(75);
			cessAmt.setTextSize(15);
			cessAmt.setGravity(Gravity.CENTER);
			cessAmt.setTextColor(Color.WHITE);
			cessAmt.setText("cess      Amt");

			TextView SubTotal = new TextView(activityContext);
			SubTotal.setWidth(105);
			SubTotal.setTextSize(15);
			SubTotal.setTextColor(Color.WHITE);
			SubTotal.setText("Sub Total");

			TextView POS = new TextView(activityContext);
			POS.setWidth(75);
			POS.setTextSize(15);
			POS.setTextColor(Color.WHITE);
			POS.setText("Cust StateCode");

			TextView RevCh = new TextView(activityContext);
			RevCh.setWidth(50);
			RevCh.setTextSize(15);
			RevCh.setTextColor(Color.WHITE);
			RevCh.setText("Rev Chrg");

			TextView Pro = new TextView(activityContext);
			Pro.setWidth(50);
			Pro.setTextSize(15);
			Pro.setTextColor(Color.WHITE);
			Pro.setText("Pro Assess ");



			// Add views to row
			rowColumnCaption.addView(SNo);
			rowColumnCaption.addView(GSTIN);
			rowColumnCaption.addView(InvNo);
			rowColumnCaption.addView(InvDate);
			//rowColumnCaption.addView(G_S);
			//rowColumnCaption.addView(HSN);

			rowColumnCaption.addView(Value);
			//rowColumnCaption.addView(Quantity);

			rowColumnCaption.addView(GSTRate);
            rowColumnCaption.addView(TaxableValue);
			//rowColumnCaption.addView(IGSTRate);
			rowColumnCaption.addView(IGSTAmt);
			//rowColumnCaption.addView(CGSTRate);
			rowColumnCaption.addView(CGSTAmt);

			//rowColumnCaption.addView(SGSTRate);
			rowColumnCaption.addView(SGSTAmt);
			rowColumnCaption.addView(cessAmt);
			//rowColumnCaption.addView(SubTotal);
			rowColumnCaption.addView(POS);
			if (ReverseChargeEnabe.equals("1"))
			{
				rowColumnCaption.addView(RevCh);
				rowColumnCaption.addView((Pro));
			}

		} else if(ReportName.equalsIgnoreCase("GSTR1-B2Cl")){

			TextView SNo = new TextView(activityContext);
			SNo.setWidth(60);
			SNo.setTextSize(15);
			SNo.setTextColor(Color.WHITE);
			SNo.setText("S.No");

			TextView CustStateCode = new TextView(activityContext);
			CustStateCode.setWidth(140);
			CustStateCode.setTextSize(15);
			CustStateCode.setGravity(Gravity.CENTER);
			CustStateCode.setTextColor(Color.WHITE);
			CustStateCode.setText("Recipient's   State Code");

			TextView CustName = new TextView(activityContext);
			CustName.setWidth(140);
			CustName.setTextSize(15);
			CustName.setGravity(Gravity.CENTER);
			CustName.setTextColor(Color.WHITE);
			CustName.setText("Recipient Name ");

			TextView InvNo = new TextView(activityContext);
			InvNo.setWidth(80);
			InvNo.setTextSize(15);
			InvNo.setGravity(Gravity.CENTER);
			InvNo.setTextColor(Color.WHITE);
			InvNo.setText("Invoice      No.");

			TextView InvDate = new TextView(activityContext);
			InvDate.setWidth(110);
			InvDate.setTextSize(15);
			InvDate.setGravity(Gravity.CENTER);
			InvDate.setTextColor(Color.WHITE);
			InvDate.setText("Invoice Date");

			TextView G_S = new TextView(activityContext);
			G_S.setWidth(50);
			G_S.setTextSize(15);
			G_S.setTextColor(Color.WHITE);
			G_S.setText("G/S");

			TextView HSN = new TextView(activityContext);
			HSN.setWidth(100);
			HSN.setTextSize(15);
			HSN.setTextColor(Color.WHITE);
			HSN.setText("HSN/SAC");

			TextView Value = new TextView(activityContext);
			Value.setWidth(100);
			Value.setGravity(Gravity.CENTER);
			Value.setTextSize(15);
			Value.setTextColor(Color.WHITE);
			Value.setText("Value");

			TextView Quantity = new TextView(activityContext);
			Quantity.setWidth(40);
			Quantity.setTextSize(15);
			Quantity.setTextColor(Color.WHITE);
			Quantity.setText("Qty");

			TextView TaxableValue = new TextView(activityContext);
			TaxableValue.setWidth(120);
			TaxableValue.setTextSize(15);
			TaxableValue.setGravity(Gravity.CENTER);
			TaxableValue.setTextColor(Color.WHITE);
			TaxableValue.setText("Taxable          Value");

			TextView IGSTRate = new TextView(activityContext);
			IGSTRate.setWidth(65);
			IGSTRate.setTextSize(15);
			IGSTRate.setGravity(Gravity.CENTER);
			IGSTRate.setTextColor(Color.WHITE);
			IGSTRate.setText("IGST Rate");

			TextView IGSTAmt = new TextView(activityContext);
			IGSTAmt.setWidth(85);
			IGSTAmt.setTextSize(15);
			IGSTAmt.setGravity(Gravity.CENTER);
			IGSTAmt.setTextColor(Color.WHITE);
			IGSTAmt.setText("IGST   Amt");

			TextView cessAmt = new TextView(activityContext);
			cessAmt.setWidth(80);
			cessAmt.setGravity(Gravity.CENTER);
			cessAmt.setTextSize(15);
			cessAmt.setTextColor(Color.WHITE);
			cessAmt.setText("cess   Amt");

			TextView SubTotal = new TextView(activityContext);
			SubTotal.setWidth(105);
			SubTotal.setTextSize(15);
			SubTotal.setTextColor(Color.WHITE);
			SubTotal.setText("SubTotal");

			TextView POS = new TextView(activityContext);
			POS.setWidth(50);
			POS.setTextSize(15);
			POS.setTextColor(Color.WHITE);
			POS.setText("POS");

			TextView Pro = new TextView(activityContext);
			Pro.setWidth(50);
			Pro.setTextSize(15);
			Pro.setTextColor(Color.WHITE);
			Pro.setText("Pro Assess ");



			// Add views to row
			rowColumnCaption.addView(SNo);
			rowColumnCaption.addView(CustStateCode);
			rowColumnCaption.addView(CustName);
			rowColumnCaption.addView(InvNo);
			rowColumnCaption.addView(InvDate);
			//rowColumnCaption.addView(G_S);
            //rowColumnCaption.addView(HSN);

			rowColumnCaption.addView(Value);
			//rowColumnCaption.addView(Quantity);
			rowColumnCaption.addView(IGSTRate);
			rowColumnCaption.addView(TaxableValue);

			rowColumnCaption.addView(IGSTAmt);
			rowColumnCaption.addView(cessAmt);
			//rowColumnCaption.addView(SubTotal);
            //rowColumnCaption.addView(POS);
			if (ReverseChargeEnabe.equals("1"))
			{
				rowColumnCaption.addView((Pro));
			}

		} else if(ReportName.equalsIgnoreCase("GSTR1-B2Cs")){

			TextView SNo = new TextView(activityContext);
			SNo.setWidth(60);
			SNo.setTextSize(15);
			SNo.setTextColor(Color.WHITE);
			SNo.setText("S.No");

			TextView G_S = new TextView(activityContext);
			G_S.setWidth(60);
			G_S.setTextSize(15);
			G_S.setTextColor(Color.WHITE);
			G_S.setText("G/S");

			TextView HSN = new TextView(activityContext);
			HSN.setWidth(100);
			HSN.setTextSize(15);
			HSN.setTextColor(Color.WHITE);
			HSN.setText("  HSN/SAC");

			TextView TaxableValue = new TextView(activityContext);
			TaxableValue.setWidth(230);
			TaxableValue.setGravity(Gravity.CENTER);
			TaxableValue.setTextSize(15);
			TaxableValue.setTextColor(Color.WHITE);
			TaxableValue.setText("Taxable Value");

			TextView IGSTRate = new TextView(activityContext);
			IGSTRate.setWidth(70);
			IGSTRate.setTextSize(15);
			IGSTRate.setTextColor(Color.WHITE);
			IGSTRate.setText("IGST    Rate");

			TextView GSTRate = new TextView(activityContext);
			GSTRate.setWidth(100);
			GSTRate.setTextSize(15);
			GSTRate.setGravity(Gravity.CENTER);
			GSTRate.setTextColor(Color.WHITE);
			GSTRate.setText("GST Rate");

			TextView IGSTAmt = new TextView(activityContext);
			IGSTAmt.setWidth(150);
			IGSTAmt.setTextSize(15);
			IGSTAmt.setGravity(Gravity.CENTER);
			IGSTAmt.setTextColor(Color.WHITE);
			IGSTAmt.setText("IGST Amt");

			TextView CGSTRate = new TextView(activityContext);
			CGSTRate.setWidth(70);
			CGSTRate.setTextSize(15);
			CGSTRate.setTextColor(Color.WHITE);
			CGSTRate.setText("CGST Rate");

			TextView CGSTAmt = new TextView(activityContext);
			CGSTAmt.setWidth(170);
			CGSTAmt.setTextSize(15);
			CGSTAmt.setGravity(Gravity.CENTER);
			CGSTAmt.setTextColor(Color.WHITE);
			CGSTAmt.setText("CGST Amt");

			TextView SGSTRate = new TextView(activityContext);
			SGSTRate.setWidth(70);
			SGSTRate.setTextSize(15);
			SGSTRate.setTextColor(Color.WHITE);
			SGSTRate.setText("SGST Rate");

			TextView SGSTAmt = new TextView(activityContext);
			SGSTAmt.setWidth(170);
			SGSTAmt.setGravity(Gravity.CENTER);
			SGSTAmt.setTextSize(15);
			SGSTAmt.setTextColor(Color.WHITE);
			SGSTAmt.setText("SGST Amt");

			TextView cessAmt = new TextView(activityContext);
			cessAmt.setWidth(170);
			cessAmt.setGravity(Gravity.CENTER);
			cessAmt.setTextSize(15);
			cessAmt.setTextColor(Color.WHITE);
			cessAmt.setText("cess Amt");

			TextView SubTotal = new TextView(activityContext);
			SubTotal.setWidth(110);
			SubTotal.setTextSize(15);
			SubTotal.setTextColor(Color.WHITE);
			SubTotal.setText("SubTotal");

			TextView CustStateCode = new TextView(activityContext);
			CustStateCode.setWidth(110);
			CustStateCode.setTextSize(15);
			CustStateCode.setTextColor(Color.WHITE);
			CustStateCode.setText("CustStateCode");

			TextView POS = new TextView(activityContext);
			POS.setWidth(85);
			POS.setTextSize(15);
			POS.setTextColor(Color.WHITE);
			POS.setText("POS");

			TextView Pro = new TextView(activityContext);
			Pro.setWidth(70);
			Pro.setTextSize(15);
			Pro.setTextColor(Color.WHITE);
			Pro.setText("Pro Assess ");



			// Add views to row
			rowColumnCaption.addView(SNo);
			//rowColumnCaption.addView(G_S);
           // rowColumnCaption.addView(HSN);
            //rowColumnCaption.addView(POS);
			if (POSEnable.equals("1"))
			{

			}
			if (HSNEnable.equals("1"))
			{

			}
			rowColumnCaption.addView(GSTRate);
			rowColumnCaption.addView(TaxableValue);
			//rowColumnCaption.addView(IGSTRate);
			rowColumnCaption.addView(IGSTAmt);
			//rowColumnCaption.addView(CGSTRate);
			rowColumnCaption.addView(CGSTAmt);
			//rowColumnCaption.addView(SGSTRate);
			rowColumnCaption.addView(SGSTAmt);
			rowColumnCaption.addView(cessAmt);
			//rowColumnCaption.addView(SubTotal);
			if (ReverseChargeEnabe.equals("1"))
			{
				rowColumnCaption.addView((Pro));
			}

		}/*else if(ReportName.equalsIgnoreCase("GSTR1-B2Cs"))//HSN,itemwise
		{

			TextView SNo = new TextView(activityContext);
			SNo.setWidth(60);
			SNo.setTextSize(15);
			SNo.setTextColor(Color.WHITE);
			SNo.setText("S.No");

			TextView G_S = new TextView(activityContext);
			G_S.setWidth(60);
			G_S.setTextSize(15);
			G_S.setTextColor(Color.WHITE);
			G_S.setText("G/S");

			TextView HSN = new TextView(activityContext);
			HSN.setWidth(100);
			HSN.setTextSize(15);
			HSN.setTextColor(Color.WHITE);
			HSN.setText("  HSN/SAC");

			TextView TaxableValue = new TextView(activityContext);
			TaxableValue.setWidth(110);
			TaxableValue.setTextSize(15);
			TaxableValue.setTextColor(Color.WHITE);
			TaxableValue.setText("Taxable Value");

			TextView IGSTRate = new TextView(activityContext);
			IGSTRate.setWidth(70);
			IGSTRate.setTextSize(15);
			IGSTRate.setTextColor(Color.WHITE);
			IGSTRate.setText("IGST    Rate");

			TextView IGSTAmt = new TextView(activityContext);
			IGSTAmt.setWidth(75);
			IGSTAmt.setTextSize(15);
			IGSTAmt.setTextColor(Color.WHITE);
			IGSTAmt.setText("IGST    Amt");

			TextView CGSTRate = new TextView(activityContext);
			CGSTRate.setWidth(70);
			CGSTRate.setTextSize(15);
			CGSTRate.setTextColor(Color.WHITE);
			CGSTRate.setText("CGST Rate");

			TextView CGSTAmt = new TextView(activityContext);
			CGSTAmt.setWidth(75);
			CGSTAmt.setTextSize(15);
			CGSTAmt.setTextColor(Color.WHITE);
			CGSTAmt.setText("CGST    Amt");

			TextView SGSTRate = new TextView(activityContext);
			SGSTRate.setWidth(70);
			SGSTRate.setTextSize(15);
			SGSTRate.setTextColor(Color.WHITE);
			SGSTRate.setText("SGST Rate");

			TextView SGSTAmt = new TextView(activityContext);
			SGSTAmt.setWidth(75);
			SGSTAmt.setTextSize(15);
			SGSTAmt.setTextColor(Color.WHITE);
			SGSTAmt.setText("SGST    Amt");

			TextView SubTotal = new TextView(activityContext);
			SubTotal.setWidth(110);
			SubTotal.setTextSize(15);
			SubTotal.setTextColor(Color.WHITE);
			SubTotal.setText("CustStateCode");

			TextView POS = new TextView(activityContext);
			POS.setWidth(85);
			POS.setTextSize(15);
			POS.setTextColor(Color.WHITE);
			POS.setText("POS");

			TextView Pro = new TextView(activityContext);
			Pro.setWidth(70);
			Pro.setTextSize(15);
			Pro.setTextColor(Color.WHITE);
			Pro.setText("Pro Assess ");



			// Add views to row
			rowColumnCaption.addView(SNo);
			rowColumnCaption.addView(G_S);
            rowColumnCaption.addView(HSN);
            rowColumnCaption.addView(POS);
			if (POSEnable.equals("1"))
			{

			}
			if (HSNEnable.equals("1"))
			{

			}
			rowColumnCaption.addView(TaxableValue);
			rowColumnCaption.addView(IGSTRate);
			rowColumnCaption.addView(IGSTAmt);
			rowColumnCaption.addView(CGSTRate);
			rowColumnCaption.addView(CGSTAmt);
			rowColumnCaption.addView(SGSTRate);
			rowColumnCaption.addView(SGSTAmt);
			rowColumnCaption.addView(SubTotal);
			if (ReverseChargeEnabe.equals("1"))
			{
				rowColumnCaption.addView((Pro));
			}

		}*/else if(ReportName.equalsIgnoreCase("GSTR2-2A Validation")){

			TextView SNo = new TextView(activityContext);
			SNo.setWidth(60);
			SNo.setTextSize(15);
			SNo.setTextColor(Color.WHITE);
			SNo.setText(" S.No");

			TextView GSTIN = new TextView(activityContext);
			GSTIN.setWidth(100);
			GSTIN.setTextSize(15);
			//GSTIN.setBackgroundResource(R.drawable.border_tab_heading);
			GSTIN.setTextColor(Color.WHITE);
			GSTIN.setText("Supplier GSTIN/Name");

			TextView InvNo = new TextView(activityContext);
			InvNo.setWidth(40);
			InvNo.setTextSize(15);
			InvNo.setTextColor(Color.WHITE);
			InvNo.setText("Invoice No.");

			TextView InvDate = new TextView(activityContext);
			InvDate.setWidth(130);
			InvDate.setTextSize(15);
			InvDate.setTextColor(Color.WHITE);
			InvDate.setText("Invoice Date");

			TextView TaxableValue = new TextView(activityContext);
			TaxableValue.setWidth(130);
			TaxableValue.setTextSize(15);
			TaxableValue.setTextColor(Color.WHITE);
			TaxableValue.setText("Taxable Value");

			TextView TaxAmt = new TextView(activityContext);
			TaxAmt.setWidth(100);
			TaxAmt.setTextSize(15);
			TaxAmt.setTextColor(Color.WHITE);
			TaxAmt.setText("Tax Amount");


			TextView Divider = new TextView(activityContext);
			Divider.setWidth(10);
			Divider.setTextSize(15);
			//Divider.setBackgroundColor(Color.WHITE);
			Divider.setText("");

			TextView GSTIN_2A = new TextView(activityContext);
			GSTIN_2A.setWidth(200);
			GSTIN_2A.setTextSize(15);
			//GSTIN.setBackgroundResource(R.drawable.border_tab_heading);
			GSTIN_2A.setTextColor(Color.WHITE);
			GSTIN_2A.setText("Supplier GSTIN/Name");

			TextView InvNo_2A = new TextView(activityContext);
			InvNo_2A.setWidth(100);
			InvNo_2A.setTextSize(15);
			InvNo_2A.setTextColor(Color.WHITE);
			InvNo_2A.setText("Invoice No.");

			TextView InvDate_2A = new TextView(activityContext);
			InvDate_2A.setWidth(130);
			InvDate_2A.setTextSize(15);
			InvDate_2A.setTextColor(Color.WHITE);
			InvDate_2A.setText("Invoice Date");


			TextView TaxableValue_2A = new TextView(activityContext);
			TaxableValue_2A.setWidth(130);
			TaxableValue_2A.setTextSize(15);
			TaxableValue_2A.setTextColor(Color.WHITE);
			TaxableValue_2A.setText("Taxable Value");

			TextView TaxAmt_2A = new TextView(activityContext);
			TaxAmt_2A.setWidth(100);
			TaxAmt_2A.setTextSize(15);
			TaxAmt_2A.setTextColor(Color.WHITE);
			TaxAmt_2A.setText("Tax Amount");

			// Add views to row

			rowColumnCaption.addView(SNo);
			rowColumnCaption.addView(GSTIN);
			rowColumnCaption.addView(InvNo);
			rowColumnCaption.addView(InvDate);
			rowColumnCaption.addView(TaxableValue);
			//rowColumnCaption.addView(TaxAmt);
			rowColumnCaption.addView(Divider);
			rowColumnCaption.addView(GSTIN_2A);
			rowColumnCaption.addView(InvNo_2A);
			rowColumnCaption.addView(InvDate_2A);
			rowColumnCaption.addView(TaxableValue_2A);
			//rowColumnCaption.addView(TaxAmt_2A);

		}else if(ReportName.equalsIgnoreCase("GSTR1-B2BA")){

			TextView SNo = new TextView(activityContext);
			SNo.setWidth(50);
			SNo.setTextSize(15);
			SNo.setTextColor(Color.WHITE);
			SNo.setText("S.No");

			TextView InvNo_ori = new TextView(activityContext);
			InvNo_ori.setWidth(80);
			InvNo_ori.setTextSize(15);
			InvNo_ori.setTextColor(Color.WHITE);
			InvNo_ori.setText("Ori.Invoice No.");

			TextView InvDate_ori = new TextView(activityContext);
			InvDate_ori.setWidth(100);
			InvDate_ori.setTextSize(15);
			InvDate_ori.setTextColor(Color.WHITE);
			InvDate_ori.setText("Ori. Invoice Date");


			TextView GSTIN = new TextView(activityContext);
			GSTIN.setWidth(140);
			GSTIN.setTextSize(15);
			GSTIN.setTextColor(Color.WHITE);
			GSTIN.setText("Recipient GSTIN");

			TextView InvNo = new TextView(activityContext);
			InvNo.setWidth(80);
			InvNo.setTextSize(15);
			InvNo.setTextColor(Color.WHITE);
			InvNo.setText("Invoice   No.");

			TextView InvDate = new TextView(activityContext);
			InvDate.setWidth(100);
			InvDate.setTextSize(15);
			InvDate.setTextColor(Color.WHITE);
			InvDate.setText("Invoice Date");

			TextView G_S = new TextView(activityContext);
			G_S.setWidth(50);
			G_S.setTextSize(15);
			G_S.setTextColor(Color.WHITE);
			G_S.setText("G/S");

			TextView HSN = new TextView(activityContext);
			HSN.setWidth(80);
			HSN.setTextSize(15);
			HSN.setTextColor(Color.WHITE);
			HSN.setText("HSN/SAC");

			TextView Value = new TextView(activityContext);
			Value.setWidth(100);
			Value.setTextSize(15);
			Value.setTextColor(Color.WHITE);
			Value.setText("Value");
			Value.setGravity(Gravity.CENTER);

			TextView Quantity = new TextView(activityContext);
			Quantity.setWidth(40);
			Quantity.setTextSize(15);
			Quantity.setTextColor(Color.WHITE);
			Quantity.setText("Qty");

			TextView TaxableValue = new TextView(activityContext);
			TaxableValue.setWidth(100);
			TaxableValue.setTextSize(15);
			TaxableValue.setGravity(Gravity.CENTER);
			TaxableValue.setTextColor(Color.WHITE);
			TaxableValue.setText("Taxable   Value");

			TextView GSTRate = new TextView(activityContext);
			GSTRate.setWidth(65);
			GSTRate.setTextSize(15);
			GSTRate.setTextColor(Color.WHITE);
			GSTRate.setText("GST Rate");

			/*TextView IGSTRate = new TextView(activityContext);
			IGSTRate.setWidth(65);
			IGSTRate.setTextSize(15);
			IGSTRate.setTextColor(Color.WHITE);
			IGSTRate.setText("IGST Rate");*/
			TextView IGSTAmt = new TextView(activityContext);
			IGSTAmt.setWidth(80);
			IGSTAmt.setTextSize(15);
			IGSTAmt.setTextColor(Color.WHITE);
			IGSTAmt.setText("IGST Amt");

			/*TextView CGSTRate = new TextView(activityContext);
			CGSTRate.setWidth(65);
			CGSTRate.setTextSize(15);
			CGSTRate.setTextColor(Color.WHITE);
			CGSTRate.setText("CGST Rate");*/

			TextView CGSTAmt = new TextView(activityContext);
			CGSTAmt.setWidth(80);
			CGSTAmt.setTextSize(15);
			CGSTAmt.setTextColor(Color.WHITE);
			CGSTAmt.setText("CGST Amt");

			/*TextView SGSTRate = new TextView(activityContext);
			SGSTRate.setWidth(65);
			SGSTRate.setTextSize(15);
			SGSTRate.setTextColor(Color.WHITE);
			SGSTRate.setText("SGST Rate");*/

			TextView SGSTAmt = new TextView(activityContext);
			SGSTAmt.setWidth(80);
			SGSTAmt.setTextSize(15);
			SGSTAmt.setTextColor(Color.WHITE);
			SGSTAmt.setText("SGST Amt");

            TextView cessAmt = new TextView(activityContext);
            cessAmt.setWidth(80);
            cessAmt.setTextSize(15);
            cessAmt.setTextColor(Color.WHITE);
            cessAmt.setText("cess Amt");

			TextView SubTotal = new TextView(activityContext);
			SubTotal.setWidth(105);
			SubTotal.setTextSize(15);
			SubTotal.setGravity(Gravity.CENTER);
			SubTotal.setTextColor(Color.WHITE);
			SubTotal.setText("SubTotal");

			TextView POS = new TextView(activityContext);
			POS.setWidth(50);
			POS.setTextSize(15);
			POS.setTextColor(Color.WHITE);
			POS.setText("POS");

            TextView CustStateCode = new TextView(activityContext);
            CustStateCode.setWidth(80);
            CustStateCode.setTextSize(15);
            CustStateCode.setTextColor(Color.WHITE);
            CustStateCode.setText("Recipient StateCode");

			TextView RevCh = new TextView(activityContext);
			RevCh.setWidth(50);
			RevCh.setTextSize(15);
			RevCh.setTextColor(Color.WHITE);
			RevCh.setText("Rev Chrg");

			TextView Pro = new TextView(activityContext);
			Pro.setWidth(50);
			Pro.setTextSize(15);
			Pro.setTextColor(Color.WHITE);
			Pro.setText("Pro Assess ");



			// Add views to row
			rowColumnCaption.addView(SNo);
			rowColumnCaption.addView(InvNo_ori);
			rowColumnCaption.addView(InvDate_ori);
			rowColumnCaption.addView(GSTIN);
			rowColumnCaption.addView(InvNo);
			rowColumnCaption.addView(InvDate);
			//rowColumnCaption.addView(G_S);
			//rowColumnCaption.addView(HSN);
			if (HSNEnable.equals("1"))
			{

			}

			rowColumnCaption.addView(Value);
			//rowColumnCaption.addView(Quantity);
            rowColumnCaption.addView(GSTRate);
			rowColumnCaption.addView(TaxableValue);

			//rowColumnCaption.addView(IGSTRate);
			rowColumnCaption.addView(IGSTAmt);
			//rowColumnCaption.addView(CGSTRate);
			rowColumnCaption.addView(CGSTAmt);

			//rowColumnCaption.addView(SGSTRate);
			rowColumnCaption.addView(SGSTAmt);
			rowColumnCaption.addView(cessAmt);
			//rowColumnCaption.addView(SubTotal);
            rowColumnCaption.addView(CustStateCode);
			if (POSEnable.equals("1"))
			{
				//rowColumnCaption.addView(POS);
			}
			if (ReverseChargeEnabe.equals("1"))
			{
				rowColumnCaption.addView(RevCh);
				rowColumnCaption.addView((Pro));
			}

		} else if(ReportName.equalsIgnoreCase("GSTR1-B2ClA")){

			TextView SNo = new TextView(activityContext);
			SNo.setWidth(60);
			SNo.setTextSize(15);
			SNo.setTextColor(Color.WHITE);
			SNo.setText("S.No");

			TextView InvNo_ori = new TextView(activityContext);
			InvNo_ori.setWidth(80);
			InvNo_ori.setTextSize(15);
			InvNo_ori.setTextColor(Color.WHITE);
			InvNo_ori.setText("Ori.   Invoice No.");

			TextView InvDate_ori = new TextView(activityContext);
			InvDate_ori.setWidth(110);
			InvDate_ori.setTextSize(15);
			InvDate_ori.setTextColor(Color.WHITE);
			InvDate_ori.setText("Ori.   Invoice Date");

			TextView CustStateCode = new TextView(activityContext);
			CustStateCode.setWidth(140);
			CustStateCode.setTextSize(15);
			CustStateCode.setTextColor(Color.WHITE);
			CustStateCode.setText("Cust StateCode");

			TextView CustName = new TextView(activityContext);
			CustName.setWidth(140);
			CustName.setTextSize(15);
			CustName.setTextColor(Color.WHITE);
			CustName.setText("Recipient Name ");

			TextView InvNo = new TextView(activityContext);
			InvNo.setWidth(80);
            InvNo.setGravity(Gravity.CENTER);
			InvNo.setTextSize(15);
			InvNo.setTextColor(Color.WHITE);
			InvNo.setText("Invoice      No.");

			TextView InvDate = new TextView(activityContext);
			InvDate.setWidth(110);
			InvDate.setTextSize(15);
            InvDate.setGravity(Gravity.CENTER);
			InvDate.setTextColor(Color.WHITE);
			InvDate.setText("Invoice          Date");

			TextView G_S = new TextView(activityContext);
			G_S.setWidth(50);
			G_S.setTextSize(15);
			G_S.setTextColor(Color.WHITE);
			G_S.setText("G/S");

			TextView HSN = new TextView(activityContext);
			HSN.setWidth(80);
			HSN.setTextSize(15);
			HSN.setTextColor(Color.WHITE);
			HSN.setText("HSN/SAC");

			TextView Value = new TextView(activityContext);
			Value.setWidth(100);
			Value.setTextSize(15);
            Value.setGravity(Gravity.CENTER);
			Value.setTextColor(Color.WHITE);
			Value.setText("Value");

			TextView Quantity = new TextView(activityContext);
			Quantity.setWidth(40);
			Quantity.setTextSize(15);
			Quantity.setTextColor(Color.WHITE);
			Quantity.setText("Qty");

			TextView TaxableValue = new TextView(activityContext);
			TaxableValue.setWidth(100);
			TaxableValue.setTextSize(15);
            TaxableValue.setGravity(Gravity.CENTER);
			TaxableValue.setTextColor(Color.WHITE);
			TaxableValue.setText("Taxable    Value");

			TextView IGSTRate = new TextView(activityContext);
			IGSTRate.setWidth(65);
			IGSTRate.setTextSize(15);
            IGSTRate.setGravity(Gravity.CENTER);
			IGSTRate.setTextColor(Color.WHITE);
			IGSTRate.setText("GST     Rate");

			TextView IGSTAmt = new TextView(activityContext);
			IGSTAmt.setWidth(70);
            IGSTAmt.setGravity(Gravity.CENTER);
			IGSTAmt.setTextSize(15);
			IGSTAmt.setTextColor(Color.WHITE);
			IGSTAmt.setText("IGST      Amt");

            TextView cessAmt = new TextView(activityContext);
            cessAmt.setWidth(70);
            cessAmt.setGravity(Gravity.CENTER);
            cessAmt.setTextSize(15);
            cessAmt.setTextColor(Color.WHITE);
            cessAmt.setText("cess     Amt");

			TextView SubTotal = new TextView(activityContext);
			SubTotal.setWidth(105);
			SubTotal.setGravity(Gravity.CENTER);
			SubTotal.setTextSize(15);
			SubTotal.setTextColor(Color.WHITE);
			SubTotal.setText("SubTotal");

			TextView POS = new TextView(activityContext);
			POS.setWidth(50);
			POS.setTextSize(15);
			POS.setTextColor(Color.WHITE);
			POS.setText("POS");

			TextView Pro = new TextView(activityContext);
			Pro.setWidth(50);
			Pro.setTextSize(15);
			Pro.setTextColor(Color.WHITE);
			Pro.setText("Pro Assess ");



			// Add views to row
			rowColumnCaption.addView(SNo);
			rowColumnCaption.addView(InvNo_ori);
			rowColumnCaption.addView(InvDate_ori);
			rowColumnCaption.addView(CustStateCode);
			rowColumnCaption.addView(CustName);
			rowColumnCaption.addView(InvNo);
			rowColumnCaption.addView(InvDate);
			//rowColumnCaption.addView(G_S);
			//rowColumnCaption.addView(HSN);
			if (HSNEnable.equals("1"))
			{
				//rowColumnCaption.addView(HSN);
			}

			rowColumnCaption.addView(Value);
			//rowColumnCaption.addView(Quantity);
            rowColumnCaption.addView(IGSTRate);
			rowColumnCaption.addView(TaxableValue);

			rowColumnCaption.addView(IGSTAmt);
			rowColumnCaption.addView(cessAmt);
			//rowColumnCaption.addView(SubTotal);
			if (POSEnable.equals("1"))
			{
				//rowColumnCaption.addView(POS);
			}
			if (ReverseChargeEnabe.equals("1"))
			{
				//rowColumnCaption.addView((Pro));
			}

		} else if(ReportName.equalsIgnoreCase("GSTR1-B2CsA")){

			/*TextView SNo = new TextView(activityContext);
			SNo.setWidth(50);
			SNo.setTextSize(15);
			SNo.setTextColor(Color.WHITE);
			SNo.setText("S.No");

			TextView G_S_ori = new TextView(activityContext);
			G_S_ori.setWidth(75);
			G_S_ori.setTextSize(15);
			G_S_ori.setTextColor(Color.WHITE);
			G_S_ori.setText("Original    G/S");

			TextView HSN_ori = new TextView(activityContext);
			HSN_ori.setWidth(150);
			HSN_ori.setTextSize(15);
			HSN_ori.setTextColor(Color.WHITE);
			HSN_ori.setText("Original HSN/SAC");

			TextView POS_ori = new TextView(activityContext);
			POS_ori.setWidth(85);
			POS_ori.setTextSize(15);
			POS_ori.setTextColor(Color.WHITE);
			POS_ori.setText("Original State Code");

			TextView G_S = new TextView(activityContext);
			G_S.setWidth(60);
			G_S.setTextSize(15);
			G_S.setTextColor(Color.WHITE);
			G_S.setText("G/S");

			TextView HSN = new TextView(activityContext);
			HSN.setWidth(150);
			HSN.setTextSize(15);
			HSN.setTextColor(Color.WHITE);
			HSN.setText("  HSN/SAC");

			TextView POS = new TextView(activityContext);
			POS.setWidth(85);
			POS.setTextSize(15);
			POS.setTextColor(Color.WHITE);
			POS.setText("State Code");

			TextView TaxableValue = new TextView(activityContext);
			TaxableValue.setWidth(110);
			TaxableValue.setTextSize(15);
			TaxableValue.setTextColor(Color.WHITE);
			TaxableValue.setText("Taxable Value");

			TextView GSTRate = new TextView(activityContext);
            GSTRate.setWidth(70);
            GSTRate.setTextSize(15);
            GSTRate.setGravity(Gravity.CENTER);
            GSTRate.setTextColor(Color.WHITE);
            GSTRate.setText("GST    Rate");

            TextView IGSTRate = new TextView(activityContext);
            IGSTRate.setWidth(70);
            IGSTRate.setTextSize(15);
            IGSTRate.setTextColor(Color.WHITE);
            IGSTRate.setText("IGST Rate");

			TextView IGSTAmt = new TextView(activityContext);
			IGSTAmt.setWidth(75);
			IGSTAmt.setTextSize(15);
			IGSTAmt.setGravity(Gravity.CENTER);
			IGSTAmt.setTextColor(Color.WHITE);
			IGSTAmt.setText("IGST      Amt");

			TextView CGSTRate = new TextView(activityContext);
			CGSTRate.setWidth(70);
			CGSTRate.setTextSize(15);
			CGSTRate.setTextColor(Color.WHITE);
			CGSTRate.setText("CGST Rate");

			TextView CGSTAmt = new TextView(activityContext);
			CGSTAmt.setWidth(75);
			CGSTAmt.setTextSize(15);
			CGSTAmt.setGravity(Gravity.CENTER);
			CGSTAmt.setTextColor(Color.WHITE);
			CGSTAmt.setText("CGST     Amt");

			TextView SGSTRate = new TextView(activityContext);
			SGSTRate.setWidth(70);
			SGSTRate.setTextSize(15);
			SGSTRate.setTextColor(Color.WHITE);
			SGSTRate.setText("SGST Rate");

			TextView SGSTAmt = new TextView(activityContext);
			SGSTAmt.setWidth(75);
			SGSTAmt.setTextSize(15);
			SGSTAmt.setGravity(Gravity.CENTER);
			SGSTAmt.setTextColor(Color.WHITE);
			SGSTAmt.setText("SGST    Amt");

            TextView cessAmt = new TextView(activityContext);
            cessAmt.setWidth(75);
            cessAmt.setTextSize(15);
            cessAmt.setGravity(Gravity.CENTER);
            cessAmt.setTextColor(Color.WHITE);
            cessAmt.setText("cess    Amt");

			TextView SubTotal = new TextView(activityContext);
			SubTotal.setWidth(110);
			SubTotal.setTextSize(15);
			SubTotal.setTextColor(Color.WHITE);
			SubTotal.setText("SubTotal");

			TextView Pro = new TextView(activityContext);
			Pro.setWidth(70);
			Pro.setTextSize(15);
			Pro.setTextColor(Color.WHITE);
			Pro.setText("Pro Assess ");



			// Add views to row
			rowColumnCaption.addView(SNo);
			//rowColumnCaption.addView(G_S_ori);
			//rowColumnCaption.addView(HSN_ori);
			//rowColumnCaption.addView(POS_ori);
			if (POSEnable.equals("1"))
			{

			}
			if (HSNEnable.equals("1"))
			{

			}
			//rowColumnCaption.addView(G_S);
			//rowColumnCaption.addView(HSN);
			//rowColumnCaption.addView(POS);
			if (POSEnable.equals("1"))
			{

			}
			if (HSNEnable.equals("1"))
			{

			}
			rowColumnCaption.addView(GSTRate);
			rowColumnCaption.addView(TaxableValue);
			//rowColumnCaption.addView(IGSTRate);
			rowColumnCaption.addView(IGSTAmt);
			//rowColumnCaption.addView(CGSTRate);
			rowColumnCaption.addView(CGSTAmt);
			//rowColumnCaption.addView(SGSTRate);
			rowColumnCaption.addView(SGSTAmt);
			//rowColumnCaption.addView(SubTotal);
			if (ReverseChargeEnabe.equals("1"))
			{
				rowColumnCaption.addView((Pro));
			}
*/
            TextView SNo = new TextView(activityContext);
            SNo.setWidth(60);
            SNo.setTextSize(15);
            SNo.setTextColor(Color.WHITE);
            SNo.setText("S.No");

            TextView G_S = new TextView(activityContext);
            G_S.setWidth(60);
            G_S.setTextSize(15);
            G_S.setTextColor(Color.WHITE);
            G_S.setText("G/S");

            TextView HSN = new TextView(activityContext);
            HSN.setWidth(100);
            HSN.setTextSize(15);
            HSN.setTextColor(Color.WHITE);
            HSN.setText("  HSN/SAC");

            TextView TaxableValue = new TextView(activityContext);
            TaxableValue.setWidth(230);
            TaxableValue.setGravity(Gravity.CENTER);
            TaxableValue.setTextSize(15);
            TaxableValue.setTextColor(Color.WHITE);
            TaxableValue.setText("Taxable Value");

            TextView IGSTRate = new TextView(activityContext);
            IGSTRate.setWidth(70);
            IGSTRate.setTextSize(15);
            IGSTRate.setTextColor(Color.WHITE);
            IGSTRate.setText("IGST    Rate");

            TextView GSTRate = new TextView(activityContext);
            GSTRate.setWidth(100);
            GSTRate.setTextSize(15);
            GSTRate.setGravity(Gravity.CENTER);
            GSTRate.setTextColor(Color.WHITE);
            GSTRate.setText("GST Rate");

            TextView IGSTAmt = new TextView(activityContext);
            IGSTAmt.setWidth(150);
            IGSTAmt.setTextSize(15);
            IGSTAmt.setGravity(Gravity.CENTER);
            IGSTAmt.setTextColor(Color.WHITE);
            IGSTAmt.setText("IGST Amt");

            TextView CGSTRate = new TextView(activityContext);
            CGSTRate.setWidth(70);
            CGSTRate.setTextSize(15);
            CGSTRate.setTextColor(Color.WHITE);
            CGSTRate.setText("CGST Rate");

            TextView CGSTAmt = new TextView(activityContext);
            CGSTAmt.setWidth(170);
            CGSTAmt.setTextSize(15);
            CGSTAmt.setGravity(Gravity.CENTER);
            CGSTAmt.setTextColor(Color.WHITE);
            CGSTAmt.setText("CGST Amt");

            TextView SGSTRate = new TextView(activityContext);
            SGSTRate.setWidth(70);
            SGSTRate.setTextSize(15);
            SGSTRate.setTextColor(Color.WHITE);
            SGSTRate.setText("SGST Rate");

            TextView SGSTAmt = new TextView(activityContext);
            SGSTAmt.setWidth(170);
            SGSTAmt.setGravity(Gravity.CENTER);
            SGSTAmt.setTextSize(15);
            SGSTAmt.setTextColor(Color.WHITE);
            SGSTAmt.setText("SGST Amt");

            TextView cessAmt = new TextView(activityContext);
            cessAmt.setWidth(170);
            cessAmt.setGravity(Gravity.CENTER);
            cessAmt.setTextSize(15);
            cessAmt.setTextColor(Color.WHITE);
            cessAmt.setText("cess Amt");

            TextView SubTotal = new TextView(activityContext);
            SubTotal.setWidth(110);
            SubTotal.setTextSize(15);
            SubTotal.setTextColor(Color.WHITE);
            SubTotal.setText("SubTotal");

            TextView CustStateCode = new TextView(activityContext);
            CustStateCode.setWidth(110);
            CustStateCode.setTextSize(15);
            CustStateCode.setTextColor(Color.WHITE);
            CustStateCode.setText("CustStateCode");

            TextView POS = new TextView(activityContext);
            POS.setWidth(85);
            POS.setTextSize(15);
            POS.setTextColor(Color.WHITE);
            POS.setText("POS");

            TextView Pro = new TextView(activityContext);
            Pro.setWidth(70);
            Pro.setTextSize(15);
            Pro.setTextColor(Color.WHITE);
            Pro.setText("Pro Assess ");



            // Add views to row
            rowColumnCaption.addView(SNo);
            //rowColumnCaption.addView(G_S);
            // rowColumnCaption.addView(HSN);
            //rowColumnCaption.addView(POS);
            if (POSEnable.equals("1"))
            {

            }
            if (HSNEnable.equals("1"))
            {

            }
            rowColumnCaption.addView(GSTRate);
            rowColumnCaption.addView(TaxableValue);
            //rowColumnCaption.addView(IGSTRate);
            rowColumnCaption.addView(IGSTAmt);
            //rowColumnCaption.addView(CGSTRate);
            rowColumnCaption.addView(CGSTAmt);
            //rowColumnCaption.addView(SGSTRate);
            rowColumnCaption.addView(SGSTAmt);
            rowColumnCaption.addView(cessAmt);
            //rowColumnCaption.addView(SubTotal);
            if (ReverseChargeEnabe.equals("1"))
            {
                rowColumnCaption.addView((Pro));
            }
		}else if(ReportName.equalsIgnoreCase("GSTR1-1A Validation")){

			TextView SNo = new TextView(activityContext);
			SNo.setWidth(50);
			SNo.setTextSize(15);
			SNo.setTextColor(Color.WHITE);
			SNo.setText("S.No");

			TextView GSTIN = new TextView(activityContext);
			GSTIN.setWidth(110);
			GSTIN.setTextSize(15);
			//GSTIN.setBackgroundResource(R.drawable.border_tab_heading);
			GSTIN.setTextColor(Color.WHITE);
			GSTIN.setText("Supplier GSTIN/Name");

			TextView InvNo = new TextView(activityContext);
			InvNo.setWidth(100);
			InvNo.setTextSize(15);
			InvNo.setTextColor(Color.WHITE);
			InvNo.setText("Invoice No.");

			TextView InvDate = new TextView(activityContext);
			InvDate.setWidth(120);
			InvDate.setTextSize(15);
			InvDate.setTextColor(Color.WHITE);
			InvDate.setText("Invoice Date");

			TextView TaxableValue = new TextView(activityContext);
			TaxableValue.setWidth(100);
			TaxableValue.setTextSize(15);
			TaxableValue.setTextColor(Color.WHITE);
			TaxableValue.setText("Taxable Value");


			TextView Divider = new TextView(activityContext);
			Divider.setWidth(10);
			Divider.setTextSize(15);
			//Divider.setBackgroundColor(Color.WHITE);
			Divider.setText("");

			TextView GSTIN_1A = new TextView(activityContext);
			GSTIN_1A.setWidth(110);
			GSTIN_1A.setTextSize(15);
			//GSTIN.setBackgroundResource(R.drawable.border_tab_heading);
			GSTIN_1A.setTextColor(Color.WHITE);
			GSTIN_1A.setText("Supplier GSTIN/Name");

			TextView InvNo_1A = new TextView(activityContext);
			InvNo_1A.setWidth(100);
			InvNo_1A.setTextSize(15);
			InvNo_1A.setTextColor(Color.WHITE);
			InvNo_1A.setText("Invoice No.");

			TextView InvDate_1A = new TextView(activityContext);
			InvDate_1A.setWidth(120);
			InvDate_1A.setTextSize(15);
			InvDate_1A.setTextColor(Color.WHITE);
			InvDate_1A.setText("Invoice Date");


			TextView TaxableValue_1A = new TextView(activityContext);
			TaxableValue_1A.setWidth(100);
			TaxableValue_1A.setTextSize(15);
			TaxableValue_1A.setTextColor(Color.WHITE);
			TaxableValue_1A.setText("Taxable Value");


			// Add views to row

			rowColumnCaption.addView(SNo);
			rowColumnCaption.addView(GSTIN);
			rowColumnCaption.addView(InvNo);
			rowColumnCaption.addView(InvDate);
			rowColumnCaption.addView(TaxableValue);
			rowColumnCaption.addView(Divider);
			rowColumnCaption.addView(GSTIN_1A);
			rowColumnCaption.addView(InvNo_1A);
			rowColumnCaption.addView(InvDate_1A);
			rowColumnCaption.addView(TaxableValue_1A);
		}



		// Add row to table layout
		ReportTable.addView(rowColumnCaption,
				new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));
	}
	
	public boolean ExportReportToCSV(Context activityContext,TableLayout ReportTable,String ReportName,String StartDate,String EndDate){
		FileOutputStream Report;
		TableRow rowReport;
		TextView ReportData;
		int iRow = 0, iColumn = 0;
        String strData, strReportFileName;
        byte[] bDataBuffer;
        byte[] bReportName = ReportName.getBytes();
        byte[] bColumnSeperator = (",").getBytes();
        byte[] bLineFeed = ("\n").getBytes();
        
		try {
			CheckReportsDirectory();
			
			// Set default file name.
			strReportFileName = ReportName +"{"+ StartDate + " To " + EndDate +"}" +".csv";
			Report = new FileOutputStream(REPORT_PATH + strReportFileName);

			// Set Report name in file.
            // Center alignment for report name
			TableRow rowHeading = (TableRow)ReportTable.getChildAt(0);
			for(iColumn=1; iColumn<((rowHeading.getChildCount() / 2) + 1); iColumn++){
				Report.write(bColumnSeperator,0,bColumnSeperator.length);
			}
			Report.write(bReportName,0,bReportName.length);
			
			// Line space between column headers and report date range
			Report.write(bLineFeed,0,bLineFeed.length);

			// Copy all the data to file from report table
			for(iRow=0; iRow<ReportTable.getChildCount();iRow++){
				
				rowReport = (TableRow)ReportTable.getChildAt(iRow);
				for(iColumn=0;iColumn<rowReport.getChildCount();iColumn++){
					
					ReportData = (TextView)rowReport.getChildAt(iColumn);
					strData = ReportData.getText().toString();
					if(strData.contains(",")){
						strData.replace(',', ' ');
					}
					
					bDataBuffer = strData.getBytes();
					Report.write(bDataBuffer,0,bDataBuffer.length);
					Report.write(bColumnSeperator,0,bColumnSeperator.length);
				}
				Report.write(bLineFeed,0,bLineFeed.length);
			}
			
			// Flush the buffer and close the file
			Report.flush();
			Report.close();
			
			return true;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MsgBox.Show("Exception", "Error(FNF): ExportReportToCSV - " + e.getMessage());
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MsgBox.Show("Exception", "Error(IOE): ExportReportToCSV - " + e.getMessage());
			return false;
		}
	}
	
	private String getReportColumn(String ReportName){
		String strHeader = "";
		
		if(ReportName.equalsIgnoreCase("Bill wise Report") || 
				ReportName.equalsIgnoreCase("Void Bill Report")){
			strHeader = "------------------------" + "\r";
			strHeader += "Bill #   Items  Discount" + "\r";
			strHeader += "   Sales Service        " + "\r";
			strHeader += "     Tax     Tax  Amount" + "\r";
			strHeader += "------------------------" + "\r";
		}
		else if(ReportName.equalsIgnoreCase("Waiter wise Report") || ReportName.equalsIgnoreCase("Rider wise Report") ||
				ReportName.equalsIgnoreCase("User wise Report")){
			strHeader = "------------------------" + "\r";
			strHeader += "Id    Name              " + "\r";
			strHeader += "Bills    Amount         " + "\r";
			strHeader += "------------------------" + "\r";
		}
		else if(ReportName.equalsIgnoreCase("Waiter Detailed Report") || ReportName.equalsIgnoreCase("Rider Detailed Report") ||
				ReportName.equalsIgnoreCase("User Detailed Report") || ReportName.equalsIgnoreCase("Customer Detailed Report") || 
				ReportName.equalsIgnoreCase("Day wise Report") || ReportName.equalsIgnoreCase("Month wise Report")){
			strHeader = "------------------------" + "\r";
			strHeader += "Date       Bill   Disct." + "\r";
			strHeader += "   Sales Service        " + "\r";
			strHeader += "     Tax     Tax  Amount" + "\r";
			strHeader += "------------------------" + "\r";
		}
		else if(ReportName.equalsIgnoreCase("Transaction Report")){
			strHeader = "------------------------" + "\r";
			strHeader += "Date       Bill#  Amount" + "\r";
			strHeader += "    Cash    Card  Coupon" + "\r";
			strHeader += "------------------------" + "\r";
		}
		else if(ReportName.equalsIgnoreCase("Customer wise Report")){
			strHeader = "------------------------" + "\r";
			strHeader += "Id  Name                " + "\r";
			strHeader += "Bills  Lst.Trsn Tot.Trsn" + "\r";
			strHeader += "------------------------" + "\r";
		}
		else if(ReportName.equalsIgnoreCase("Sales Tax Report") || ReportName.equalsIgnoreCase("Service Tax Report")){
			strHeader = "------------------------" + "\r";
			strHeader += "Percent Description     " + "\r";
			strHeader += "      Tax    Amount     " + "\r";
			strHeader += "------------------------" + "\r";
		}
		else if(ReportName.equalsIgnoreCase("KOT Pending Report")){
			strHeader = "------------------------" + "\r";
			strHeader += "Token Table Time  Waiter" + "\r";
			strHeader += "Items                   " + "\r";
			strHeader += "------------------------" + "\r";
		}
		else if(ReportName.equalsIgnoreCase("KOT Deleted Report")){
			strHeader = "------------------------" + "\r";
			strHeader += "Token Table Time  Waiter" + "\r";
			strHeader += "Reason                  " + "\r";
			strHeader += "------------------------" + "\r";
		}
		else if(ReportName.equalsIgnoreCase("Item wise Report")){
			strHeader = "------------------------" + "\r";
			strHeader += "Name          Qty Disct." + "\r";
			strHeader += "   Sales Service        " + "\r";
			strHeader += "     Tax     Tax  Amount" + "\r";
			strHeader += "------------------------" + "\r";
		}
		else if(ReportName.equalsIgnoreCase("Department wise Report") || ReportName.equalsIgnoreCase("Category wise Report") ||
				ReportName.equalsIgnoreCase("Kitchen wise Report")){
			strHeader = "------------------------" + "\r";
			strHeader += "Name        Items Disct." + "\r";
			strHeader += "   Sales Service        " + "\r";
			strHeader += "     Tax     Tax  Amount" + "\r";
			strHeader += "------------------------" + "\r";
		}
		else if(ReportName.equalsIgnoreCase("Duplicate Bill Report")){
			strHeader = "------------------------" + "\r";
			strHeader += "Bill Items Count  Disct." + "\r";
			strHeader += "   Sales Service        " + "\r";
			strHeader += "     Tax     Tax  Amount" + "\r";
			strHeader += "------------------------" + "\r";
		}
		else if(ReportName.equalsIgnoreCase("Payments Report") || ReportName.equalsIgnoreCase("Receipts Report")){
			strHeader = "------------------------" + "\r";
			strHeader += "Date       Description  " + "\r";
			strHeader += "  Amount Reason         " + "\r";
			strHeader += "------------------------" + "\r";
		}
		
		return strHeader;
	}
	
	public void PrintReport(String ReportName,TableLayout ReportTable){
		SendDataToSerialPort spPrinter = new SendDataToSerialPort();
		int iCount = 0;
		String strPrintData = "";
		TableRow Row;
		TextView Amount = null,Bill = null,Card = null,Cash = null,Count = null,Coupon = null,
				Date = null,Description = null,Discount = null,Id = null,Items = null,Name = null,
				Percent = null,Qty = null,Reason = null,SalesTax = null,ServiceTax = null,Table = null,
				Time = null,Token = null, Waiter = null;
		
		//strPrintData = crsrSettings.getString(crsrSettings.getColumnIndex("HeaderText")) + "\r\r";
		//spPrinter.Write(strPrintData.getBytes());
		
		strPrintData = "\r\r" + ReportName + "\r\r";
		spPrinter.Write(strPrintData.getBytes());
		
		strPrintData = getReportColumn(ReportName);
		spPrinter.Write(strPrintData.getBytes());
		
		if(ReportName.equalsIgnoreCase("Bill wise Report") || 
				ReportName.equalsIgnoreCase("Void Bill Report")){
			
			for(iCount = 1; iCount < ReportTable.getChildCount(); iCount++){
				
				Row = (TableRow)ReportTable.getChildAt(iCount);
				
				if(Row.getChildAt(0) != null){
					
					Bill = (TextView)Row.getChildAt(1);
					Items = (TextView)Row.getChildAt(2);
					Discount = (TextView)Row.getChildAt(3);
					SalesTax = (TextView)Row.getChildAt(4);
					ServiceTax = (TextView)Row.getChildAt(5);
					Amount = (TextView)Row.getChildAt(6);
					
					strPrintData = String.format("%-7s", Bill.getText().toString());
					strPrintData += String.format("%7s", Items.getText().toString());
					strPrintData += String.format("%10s", Discount.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
					
					strPrintData = String.format("%8s", SalesTax.getText().toString());
					strPrintData += String.format("%8s", ServiceTax.getText().toString());
					strPrintData += String.format("%8s", Amount.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
				}
			}
		}
		else if(ReportName.equalsIgnoreCase("Waiter Detailed Report") || ReportName.equalsIgnoreCase("Rider Detailed Report") ||
				ReportName.equalsIgnoreCase("User Detailed Report") || ReportName.equalsIgnoreCase("Customer Detailed Report") || 
				ReportName.equalsIgnoreCase("Day wise Report") || ReportName.equalsIgnoreCase("Month wise Report")){
			
			for(iCount = 1; iCount < ReportTable.getChildCount(); iCount++){
				
				Row = (TableRow)ReportTable.getChildAt(iCount);
				
				if(Row.getChildAt(0) != null){
					
					Date = (TextView)Row.getChildAt(0);
					Bill = (TextView)Row.getChildAt(1);
					Discount = (TextView)Row.getChildAt(3);
					SalesTax = (TextView)Row.getChildAt(4);
					ServiceTax = (TextView)Row.getChildAt(5);
					Amount = (TextView)Row.getChildAt(6);
					
					strPrintData = String.format("%-11s", Date.getText().toString());
					strPrintData += String.format("%6s", Bill.getText().toString());
					strPrintData += String.format("%7s", Discount.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
					
					strPrintData = String.format("%8s", SalesTax.getText().toString());
					strPrintData += String.format("%8s", ServiceTax.getText().toString());
					strPrintData += String.format("%8s", Amount.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
				}
			}
		}
		else if(ReportName.equalsIgnoreCase("Waiter wise Report") || 
				ReportName.equalsIgnoreCase("Rider wise Report") ||  
				ReportName.equalsIgnoreCase("User wise Report")){
			
			for(iCount = 1; iCount < ReportTable.getChildCount(); iCount++){
				
				Row = (TableRow)ReportTable.getChildAt(iCount);
				
				if(Row.getChildAt(0) != null){
					
					Id = (TextView)Row.getChildAt(0);
					Name = (TextView)Row.getChildAt(1);
					Bill = (TextView)Row.getChildAt(3);
					Amount = (TextView)Row.getChildAt(6);
					
					strPrintData = String.format("%-6s", Id.getText().toString());
					strPrintData += String.format("%-18s", Name.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
					
					strPrintData = String.format("%-6s", Bill.getText().toString());
					strPrintData += String.format("%9s", Amount.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
				}
			}
		}
		else if(ReportName.equalsIgnoreCase("Department wise Report") || 
				ReportName.equalsIgnoreCase("Category wise Report") ||  
				ReportName.equalsIgnoreCase("Kitchen wise Report")){
			
			for(iCount = 1; iCount < ReportTable.getChildCount(); iCount++){
				
				Row = (TableRow)ReportTable.getChildAt(iCount);
				
				if(Row.getChildAt(0) != null){
					
					Name = (TextView)Row.getChildAt(1);
					Items = (TextView)Row.getChildAt(2);
					Discount = (TextView)Row.getChildAt(3);
					SalesTax = (TextView)Row.getChildAt(4);
					ServiceTax = (TextView)Row.getChildAt(5);
					Amount = (TextView)Row.getChildAt(6);
					
					strPrintData = String.format("%-11s", Name.getText().toString());
					strPrintData += String.format("%6s", Items.getText().toString());
					strPrintData += String.format("%7s", Discount.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
					
					strPrintData = String.format("%8s", SalesTax.getText().toString());
					strPrintData += String.format("%8s", ServiceTax.getText().toString());
					strPrintData += String.format("%8s", Amount.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
				}
			}
		}
		else if(ReportName.equalsIgnoreCase("Sales Tax Report") || 
				ReportName.equalsIgnoreCase("Service Tax Report")){
			
			for(iCount = 1; iCount < ReportTable.getChildCount(); iCount++){
				
				Row = (TableRow)ReportTable.getChildAt(iCount);
				
				if(Row.getChildAt(0) != null){
					
					Percent = (TextView)Row.getChildAt(0);
					Description = (TextView)Row.getChildAt(1);
					SalesTax = (TextView)Row.getChildAt(2);
					Amount = (TextView)Row.getChildAt(3);
					
					strPrintData = String.format("%-8s", Percent.getText().toString());
					strPrintData += String.format("%-16s", Description.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
					
					strPrintData = String.format("%-9s", SalesTax.getText().toString());
					strPrintData += String.format("%10s", Amount.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
				}
			}
		}
		else if(ReportName.equalsIgnoreCase("Payments Report") || 
				ReportName.equalsIgnoreCase("Receipts Report")){
			
			for(iCount = 1; iCount < ReportTable.getChildCount(); iCount++){
				
				Row = (TableRow)ReportTable.getChildAt(iCount);
				
				if(Row.getChildAt(0) != null){
					
					Date = (TextView)Row.getChildAt(0);
					Description = (TextView)Row.getChildAt(1);
					Amount = (TextView)Row.getChildAt(2);
					Reason = (TextView)Row.getChildAt(3);
					
					strPrintData = String.format("%-11s", Date.getText().toString());
					strPrintData += String.format("%-13s", Description.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
					
					strPrintData = String.format("%9s", Amount.getText().toString());
					strPrintData += String.format("%-15s", Reason.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
				}
			}
		}
		else if(ReportName.equalsIgnoreCase("Transaction Report")){
			
			for(iCount = 1; iCount < ReportTable.getChildCount(); iCount++){
				
				Row = (TableRow)ReportTable.getChildAt(iCount);
				
				if(Row.getChildAt(0) != null){
					
					Date = (TextView)Row.getChildAt(0);
					Bill = (TextView)Row.getChildAt(1);
					Amount = (TextView)Row.getChildAt(3);
					Cash = (TextView)Row.getChildAt(4);
					Card = (TextView)Row.getChildAt(5);
					Coupon = (TextView)Row.getChildAt(6);
					TextView pettyCash = (TextView)Row.getChildAt(7);
                    TextView ewallet = (TextView)Row.getChildAt(8);

					strPrintData = String.format("%-11s", Date.getText().toString());
					strPrintData += String.format("%-6s", Bill.getText().toString());
					strPrintData += String.format("%7s", Amount.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
					
					strPrintData = String.format("%8s", Cash.getText().toString());
					strPrintData += String.format("%8s", Card.getText().toString());
					strPrintData += String.format("%8s", Coupon.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
				}
			}
		}
		else if(ReportName.equalsIgnoreCase("Customer wise Report")){
			
			for(iCount = 1; iCount < ReportTable.getChildCount(); iCount++){
				
				Row = (TableRow)ReportTable.getChildAt(iCount);
				
				if(Row.getChildAt(0) != null){
					
					Id = (TextView)Row.getChildAt(0);
					Name = (TextView)Row.getChildAt(1);
					Bill = (TextView)Row.getChildAt(3);
					SalesTax = (TextView)Row.getChildAt(4);			// Last Transaction
					ServiceTax = (TextView)Row.getChildAt(5);		// Total Transaction
					
					strPrintData = String.format("%-4s", Id.getText().toString());
					strPrintData += String.format("%-20s", Name.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
					
					strPrintData = String.format("%-6s", Bill.getText().toString());
					strPrintData += String.format("%-9s", SalesTax.getText().toString());
					strPrintData += String.format("%-9s", ServiceTax.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
				}
			}
		}
		else if(ReportName.equalsIgnoreCase("KOT Pending Report")){
			
			for(iCount = 1; iCount < ReportTable.getChildCount(); iCount++){
				
				Row = (TableRow)ReportTable.getChildAt(iCount);
				
				if(Row.getChildAt(0) != null){
					
					Token = (TextView)Row.getChildAt(0);
					Table = (TextView)Row.getChildAt(1);
					Time = (TextView)Row.getChildAt(2);
					Waiter = (TextView)Row.getChildAt(3);
					Items = (TextView)Row.getChildAt(4);
					
					strPrintData = String.format("%-6s", Token.getText().toString());
					strPrintData += String.format("%-6s", Table.getText().toString());
					strPrintData += String.format("%-6s", Time.getText().toString());
					strPrintData += String.format("%-6s", Waiter.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
					
					strPrintData = String.format("%-6s", Items.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
				}
			}
		}
		else if(ReportName.equalsIgnoreCase("KOT Deleted Report")){
			
			for(iCount = 1; iCount < ReportTable.getChildCount(); iCount++){
				
				Row = (TableRow)ReportTable.getChildAt(iCount);
				
				if(Row.getChildAt(0) != null){
					
					Token = (TextView)Row.getChildAt(0);
					Table = (TextView)Row.getChildAt(1);
					Time = (TextView)Row.getChildAt(2);
					Waiter = (TextView)Row.getChildAt(3);
					Reason = (TextView)Row.getChildAt(4);
					
					strPrintData = String.format("%-6s", Token.getText().toString());
					strPrintData += String.format("%-6s", Table.getText().toString());
					strPrintData += String.format("%-6s", Time.getText().toString());
					strPrintData += String.format("%-6s", Waiter.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
					
					strPrintData = String.format("%-24s", Reason.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
				}
			}
		}
		else if(ReportName.equalsIgnoreCase("Item wise Report")){
			
			for(iCount = 1; iCount < ReportTable.getChildCount(); iCount++){
				
				Row = (TableRow)ReportTable.getChildAt(iCount);
				
				if(Row.getChildAt(0) != null){
					
					Name = (TextView)Row.getChildAt(1);
					Qty = (TextView)Row.getChildAt(2);
					Discount = (TextView)Row.getChildAt(3);
					SalesTax = (TextView)Row.getChildAt(4);
					ServiceTax = (TextView)Row.getChildAt(5);
					Amount = (TextView)Row.getChildAt(6);
					
					strPrintData = String.format("%-12s", Name.getText().toString());
					strPrintData += String.format("%5s", Qty.getText().toString());
					strPrintData += String.format("%7s", Discount.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
					
					strPrintData = String.format("%8s", SalesTax.getText().toString());
					strPrintData += String.format("%8s", ServiceTax.getText().toString());
					strPrintData += String.format("%8s", Amount.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
				}
			}
		}
		else if(ReportName.equalsIgnoreCase("Duplicate Bill Report")){
			
			for(iCount = 1; iCount < ReportTable.getChildCount(); iCount++){
				
				Row = (TableRow)ReportTable.getChildAt(iCount);
				
				if(Row.getChildAt(0) != null){
					
					Bill = (TextView)Row.getChildAt(1);
					Items = (TextView)Row.getChildAt(2);
					Count = (TextView)Row.getChildAt(7);
					Discount = (TextView)Row.getChildAt(3);
					SalesTax = (TextView)Row.getChildAt(4);
					ServiceTax = (TextView)Row.getChildAt(5);
					Amount = (TextView)Row.getChildAt(6);
					
					strPrintData = String.format("%-5s", Bill.getText().toString());
					strPrintData += String.format("%-6s", Items.getText().toString());
					strPrintData += String.format("%-6s", Count.getText().toString());
					strPrintData += String.format("%7s", Discount.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
					
					strPrintData = String.format("%8s", SalesTax.getText().toString());
					strPrintData += String.format("%8s", ServiceTax.getText().toString());
					strPrintData += String.format("%8s", Amount.getText().toString()) + "\r";
					spPrinter.Write(strPrintData.getBytes());
				}
			}
		}
		strPrintData = "------------------------" + "\r\r\r";
		spPrinter.Write(strPrintData.getBytes());
		
		//Close Serial port
		spPrinter.Close();
		
		//return strPrintData;
	}

}
