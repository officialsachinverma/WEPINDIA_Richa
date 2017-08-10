package com.wepindia.pos;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class StatusActivity_unused extends TabActivity {
	
	Context myContext;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Remove default title bar
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.activity_status);
        
        /*getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.application_title_bar);
        
        TextView tvTitleText = (TextView)findViewById(R.id.tvTitleBarCaption);
        tvTitleText.setText("Settings");*/
        
        myContext = this;
        
        // Create a tab host
        TabHost tabStatus = getTabHost();
        
        // Home Delivery Status tab
        TabSpec tabHomeDeliveryStatus = tabStatus.newTabSpec("HomeDeliveryStatus");
        tabHomeDeliveryStatus.setIndicator("Home Delivery Status");
        //tabHomeDeliveryStatus.setContent(new Intent(myContext,HomeDeliveryStatusActivity.class));
        
        // KOT Status tab
        TabSpec tabKOTStatus = tabStatus.newTabSpec("KOTStatus");
        tabKOTStatus.setIndicator("KOT Status");
        tabKOTStatus.setContent(new Intent(myContext,KOTStatusActivity.class));
        
        // Table Status tab
        TabSpec tabTableStatus = tabStatus.newTabSpec("TableStatus");
        tabTableStatus.setIndicator("Table Status");
        tabTableStatus.setContent(new Intent(myContext,TableStatusActivity.class));
        
        // Table Booking tab
        TabSpec tabTableBooking = tabStatus.newTabSpec("TableBooking");
        tabTableBooking.setIndicator("Table Booking");
        tabTableBooking.setContent(new Intent(myContext,TableBookingActivity.class));
                
        // Add all the tabs to tab  host
        tabStatus.addTab(tabHomeDeliveryStatus);
        tabStatus.addTab(tabKOTStatus);
        tabStatus.addTab(tabTableStatus);
        tabStatus.addTab(tabTableBooking);        
        
    }
}
