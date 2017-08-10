package com.mswipetech.wisepad.sdktest.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mswipetech.wisepad.sdk.MswipeWisepadController;
import com.mswipetech.wisepad.sdk.MswipeWisepadController.GATEWAYENV;
import com.mswipetech.wisepad.R;
import com.mswipetech.wisepad.sdktest.data.AppPrefrences;

public class MenuView extends BaseTitleActivity {
	
	public final static String log_tab = "MenuView=>";
	ApplicationData applicationData = null;
	private final int CHANGE_PASSWORD = 1011;
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuview);
		applicationData = (ApplicationData) getApplicationContext();
		MswipeWisepadController.setMACADDType(AppPrefrences.getNetworkType());
		initViews();

	}

	private void initViews() {
        Log.v(getPackageName(), log_tab + " Sdk Test List" );

        //Constants.Reference_Id = "9100000072";
        //Constants.Session_Tokeniser = "78jcChoU20M4=0";
        

        
        
        ListView listView = (ListView) findViewById(R.id.menuview_LST_options);
        String[] values = null;

        if(AppPrefrences.getGateWayEnv() == GATEWAYENV.LABS){
  
        	values = new String[]{
        			"Server Environment",
        			"Login", 
        			"Card Sale Trx", 
        			"Signature Upload",
            		"Void Trx", 
            		"Preauth Sale", 
            		"CashSale", 
            		"BankSale", 
            		"Sale With Cash", 
            		"EMI",
            		//"Refund Trx",
            		"History", 
            		"Summary",  
            		"Last Transaction Status", 
            		"Change Password", 
            		"Device Info"};
        	
        }else{
        	
            values = new String[]{
            		"Server Environment", 
            		"Login", 
            		"Card Sale Trx", 
            		"Signature Upload",
            		"Void Trx", 
            		"Preauth Sale",
            		"CashSale", 
            		"BankSale", 
            		"Sale With Cash",
            		"EMI",
            		//"Refund Trx",
            		"History", 
            		"Summary",  
            		"Last Transaction Status", 
            		"Change Password", 
            		"Device Info"};
            
        }
        
        TextView txtHeading = (TextView) findViewById(R.id.topbar_LBL_heading);
        txtHeading.setText("SDK List");
        txtHeading.setTypeface(applicationData.font);

        MenuViewAdapter adapter = new MenuViewAdapter(this, values);
        int[] colors = {0, 0xFF0000FF, 0};
        listView.setDivider(new GradientDrawable(Orientation.LEFT_RIGHT, colors));
        listView.setDividerHeight(1);

        // Assign adapter to ListView
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int menuoption =0;
                
                if (arg2 == menuoption) {
                	Intent intent = new Intent(MenuView.this, GateWayEnvActivity.class);
                	startActivityForResult(intent, 0);
                }

                menuoption++;
            	if (arg2 == menuoption) {
                    Intent intent = new Intent(MenuView.this, LoginView.class);
                    startActivity(intent);
                    return;
                }
            	

 
                menuoption++;
                if (arg2 == menuoption) {
                    if(Constants.Reference_Id.length() !=0 && Constants.Session_Tokeniser.length()!=0)
                	{
   
                     Intent intent = new Intent(MenuView.this, CreditSaleView.class);
                     startActivity(intent);
                  	}else{
                        Constants.showDialog(MenuView.this, "SDk List", "Please login first to perform the card sale.", 1);

                	}
                    return;
                } 
                
                
                menuoption++;
                if (arg2 == menuoption) {

                    if(Constants.Reference_Id.length() !=0 
                    		&& Constants.Session_Tokeniser.length()!=0 
                    			&& Constants.RRNO.length()!=0)
                	{
                    	
                    	if (!Constants.Auto_Reversed_Transaction) {
                    		
                    		Intent intent = new Intent(MenuView.this, CreditSaleSignatureView.class);
                    		intent.putExtra("cardtype", Constants.isEmvTx);
                    		intent.putExtra("title", "Card Sale");
                    		intent.putExtra("mStandId", Constants.StandId);
                    		intent.putExtra("authCode", Constants.AuthCode);
                    		intent.putExtra("rrno", Constants.RRNO);
                    		intent.putExtra("date", Constants.TrxDate);
                    		intent.putExtra("lstFrDgts", Constants.Last4Digits);
                    		intent.putExtra("mExpiryDate", Constants.ExpiryDate);
                    		
                    		intent.putExtra("amt", Constants.Amount);
                    		intent.putExtra("EmvCardExpdate", Constants.EmvCardExpdate);
                    		intent.putExtra("SwitchCardType", Constants.SwitchCardType);
                    		intent.putExtra("AppIdentifier", Constants.AppIdentifier);
                    		intent.putExtra("ApplicationName", Constants.ApplicationName);
                    		intent.putExtra("TVR", Constants.TVR);
                    		intent.putExtra("TSI", Constants.TSI);
                    		startActivity(intent);
                    		
                    	}else{
                    		
                    		Constants.showDialog(MenuView.this, "Signature Upload", "The transaction has been auto reversed.", 1);
                    		
                    	}
                    	
                 	}else{
                 		
                        Constants.showDialog(MenuView.this, "Signature Upload", "Please do a card sale to upload a Signature receipt.", 1);

                	}
                    return;
                } 
                
                menuoption++;
                if (arg2 == menuoption) {
                    if(Constants.Reference_Id.length() !=0 && Constants.Session_Tokeniser.length()!=0)
                	{
   
                     Intent intent = new Intent(MenuView.this, VoidTransaction.class);
                     startActivity(intent);
                  	}else{
                        Constants.showDialog(MenuView.this, "SDk List", "Please login first to perform the void.", 1);

                	}
                    return;
                }

                menuoption++;
                if (arg2 == menuoption) {
                	if(Constants.Reference_Id.length() !=0 && Constants.Session_Tokeniser.length()!=0)
                	{
                		
                		Intent intent = new Intent(MenuView.this, PreAuthMenuView.class);
                		startActivity(intent);
                	}else{
                		Constants.showDialog(MenuView.this, "SDk List", "Please login first to perform the pre-auth.", 1);
                		
                	}
                	return;
                }            		
                
                menuoption++;
                if (arg2 == menuoption) {
                    if(Constants.Reference_Id.length() !=0 && Constants.Session_Tokeniser.length()!=0)
                	{
   
                     Intent intent = new Intent(MenuView.this, CashSaleView.class);
                     startActivity(intent);
                  	}else{
                        Constants.showDialog(MenuView.this, "SDk List", "Please login first to perform the cash sale.", 1);

                	}
                    return;
                }

                menuoption++;
                if (arg2 == menuoption) {
                    if(Constants.Reference_Id.length() !=0 && Constants.Session_Tokeniser.length()!=0)
                	{
   
                     Intent intent = new Intent(MenuView.this, BankSaleView.class);
                     startActivity(intent);
                  	}else{
                        Constants.showDialog(MenuView.this, "SDk List", "Please login first to perform the bank sale.", 1);

                	}
                    return;
                }

            
                
                menuoption++;
                if (arg2 == menuoption) {
                	if(Constants.Reference_Id.length() !=0 && Constants.Session_Tokeniser.length()!=0)
                	{
   
                     Intent intent = new Intent(MenuView.this, CreditSaleView.class);
                     intent.putExtra("isSaleWithCash", true);
                     startActivity(intent);
                  	}else{
                        Constants.showDialog(MenuView.this, "SDk List", "Please login first to perform the sale with cash .", 1);
                	}
                	return;
                }
                menuoption++;
                if (arg2 == menuoption) {
                	if(Constants.Reference_Id.length() !=0 && Constants.Session_Tokeniser.length()!=0)
                	{
                		 Intent intent = new Intent(MenuView.this, EmiSaleView.class);
                         startActivity(intent);
                  	}else{
                        Constants.showDialog(MenuView.this, "SDk List", "Please login first to perform EMI.", 1);

                	}
                	return;
                }
                /*               
                menuoption++;
                if (arg2 == menuoption) {
                	if(Constants.Reference_Id.length() !=0 && Constants.Session_Tokeniser.length()!=0)
                	{
   
                     Intent intent = new Intent(MenuView.this, RefundTrxView.class);
                     startActivity(intent);
                  	}else{
                        Constants.showDialog(MenuView.this, "SDk List", "Please login first to view the last trx details .", 1);

                	}
                	return;
                }*/
                
                menuoption++;
                if (arg2 == menuoption) {
                	if(Constants.Reference_Id.length() !=0 && Constants.Session_Tokeniser.length()!=0)
                	{
   
                     Intent intent = new Intent(MenuView.this, HistorySummaryView.class);
                     intent.putExtra("OptionsType", "History");
                     startActivity(intent);
                  	}else{
                        Constants.showDialog(MenuView.this, "SDk List", "Please login first to view the History.", 1);

                	}
                	return;
                }
                
                menuoption++;
                if (arg2 == menuoption) {
                	if(Constants.Reference_Id.length() !=0 && Constants.Session_Tokeniser.length()!=0)
                	{
   
                     Intent intent = new Intent(MenuView.this, HistorySummaryView.class);
                     intent.putExtra("OptionsType", "Summary");
                     startActivity(intent);
                  	}else{
                        Constants.showDialog(MenuView.this, "SDk List", "Please login first to view the Summary.", 1);

                	}
                	return;
                }
                
                menuoption++;
                if (arg2 == menuoption) {
                	
                	
                	if(Constants.Reference_Id.length() !=0 && Constants.Session_Tokeniser.length()!=0)
                	{
   
                     Intent intent = new Intent(MenuView.this, LastTrxStatus.class);
                     startActivity(intent);
                  	}else{
                        Constants.showDialog(MenuView.this, "SDk List", "Please login first to view the last trx details .", 1);

                	}
                	return;
                }

                menuoption++;
                if (arg2 == menuoption) {
                	if(Constants.Reference_Id.length() !=0 && Constants.Session_Tokeniser.length()!=0)
                	{
                		Intent intent = new Intent(MenuView.this, ChangePassword.class);
                		startActivityForResult(intent, CHANGE_PASSWORD);
                	}else{
                        Constants.showDialog(MenuView.this, "SDk List", "Please login first to change the password.", 1);

                	}
                }
                menuoption++;
                if (arg2 == menuoption) {
            		Intent intent = new Intent(MenuView.this, DeviceInfoView.class);
            		startActivity(intent);
                }
                

            }
        });


    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.loginview, menu);
	    return true;
	}
	  
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		int i = item.getItemId();
		if (i == R.id.loginview_exit) {
			finish();
		} else if (i == R.id.loginview_Help) {
			new HelpViewDlg(this).show();
		}
		return super.onOptionsItemSelected(item);

	} 


	public class MenuViewAdapter extends BaseAdapter {
		String[] listData = null;
		Context context;

		public MenuViewAdapter(Context context, String[] listData) {
			this.listData = listData;
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listData.length;
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
				convertView = inflater.inflate(R.layout.menuviewlstitem, null);
			}
			TextView txtItem = (TextView) convertView
					.findViewById(R.id.menuview_lsttext);
			txtItem.setText(listData[position]);
			txtItem.setTypeface(applicationData.font);

			return convertView;
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		Constants.Reference_Id = "";
        Constants.Session_Tokeniser = "";
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == 0){
			if(resultCode == RESULT_OK){
				this.finish();
				startActivity(this.getIntent());
			}
		}else if(requestCode == CHANGE_PASSWORD){
			if(resultCode == RESULT_OK){
				Constants.Reference_Id = "";
				Constants.Session_Tokeniser = "";
				Intent intent = new Intent(MenuView.this, LoginView.class);
				startActivity(intent);
			}
		}
	}
	
	
}
