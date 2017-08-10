package com.mswipetech.wisepad.sdktest.view;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mswipetech.wisepad.R;


public class HistorySummaryView extends BaseTitleActivity {
	
	String mOptionType = ""; 
	ApplicationData applicationData =null;		
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.menuview);
    	mOptionType = getIntent().getStringExtra("OptionsType");
		applicationData = (ApplicationData)getApplicationContext();		
		initViews();
	
    }
	
		
	private void initViews() 
	{
		ListView listView = (ListView) findViewById(R.id.menuview_LST_options);
		String[] values =null;
		
        if(mOptionType.equals("History"))
        {
        	values= new String[] {"card sale history", "cash sale history", "bank sale history"};
        }else{
           	values= new String[] {"card sale summary", "cash summary", "bank summary"};
                   	
        }
		 TextView txtHeading = (TextView) findViewById(R.id.topbar_LBL_heading);
		 txtHeading.setTypeface(applicationData.font);
		 
	     if(mOptionType.equals("History"))
	    	 txtHeading.setText("history menu");
	     else 
	    	 txtHeading.setText("day summary");
	     
	    	 
	     txtHeading.setTypeface(applicationData.font);

	     MenuViewAdapter adapter = new MenuViewAdapter(this,values);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
			{
			    if(mOptionType.equals("History"))
			    {
					if (arg2 == 0){
						Intent intent = new Intent(HistorySummaryView.this, CardHistoryList.class);
						startActivity(intent);
						return;
					}else if (arg2 == 1){
						
						Intent intent = new Intent(HistorySummaryView.this, CashHistoryList.class);
						startActivity(intent);
						return;
					}else if (arg2 == 2){
						
						Intent intent = new Intent(HistorySummaryView.this, BankHistoryList.class);
						startActivity(intent);
						return;
					}	
					
			    }else{
			    	
					if (arg2 == 0){
						
						Intent intent = new Intent(HistorySummaryView.this, CardSummary.class);
						startActivity(intent);
						return;
						
					}else if (arg2 == 1){
						
						Intent intent = new Intent(HistorySummaryView.this, CashSummary.class);
						startActivity(intent);
						return;
						
					}else if (arg2 == 2){
						
						Intent intent = new Intent(HistorySummaryView.this, BankSummary.class);
						startActivity(intent);
						return;
						
					}	
			    }
			}
		});
	}
	
	public class MenuViewAdapter extends BaseAdapter
	{
		String[] listData = null;
		Context context;

		public MenuViewAdapter(Context context, String[] listData)
		{
			this.listData = listData;
			this.context=context;
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
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			// TODO Auto-generated method stub
			if(convertView == null)
			{
				LayoutInflater inflater= LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.menuviewlstitem, null);
			}
			TextView txtItem= (TextView)convertView.findViewById(R.id.menuview_lsttext);
			txtItem.setText(listData[position]);
			txtItem.setTypeface(applicationData.font);
			return convertView;
	    }
	}	
}