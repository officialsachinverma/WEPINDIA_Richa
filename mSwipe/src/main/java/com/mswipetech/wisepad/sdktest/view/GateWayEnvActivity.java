package com.mswipetech.wisepad.sdktest.view;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.mswipetech.wisepad.sdk.MswipeWisepadController.GATEWAYENV;
import com.mswipetech.wisepad.sdk.MswipeWisepadController.NETWORK_TYPE;
import com.mswipetech.wisepad.R;
import com.mswipetech.wisepad.sdktest.data.AppPrefrences;
import com.mswipetech.wisepad.sdktest.util.Utils;

public class GateWayEnvActivity extends BaseTitleActivity {
	public static final String log_tab ="GateWayEnvActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_gateway);
		initViews();
	}
	
	private void initViews()
	{
		((TextView)findViewById(R.id.topbar_LBL_heading)).setText("Server Vertical");
		
		CheckBox chkBoxGateWay = (CheckBox) findViewById(R.id.setgateway_checkbox);
		CheckBox chkBoxNetwork = (CheckBox) findViewById(R.id.setnetworkid_checkbox);
		
		if(AppPrefrences.getGateWayEnv() == GATEWAYENV.LABS){
			chkBoxGateWay.setChecked(true);

		}else{
			chkBoxGateWay.setChecked(false);
		}
		
		chkBoxGateWay.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					AppPrefrences.setgateway(GATEWAYENV.LABS);	
				}else{
					AppPrefrences.setgateway(GATEWAYENV.PRODUCTION);
				}
				
				setTitle(isChecked);
			}
		});
		

		if(AppPrefrences.getNetworkType() == NETWORK_TYPE.WIFI){
			chkBoxNetwork.setChecked(true);

		}else{
			chkBoxNetwork.setChecked(false);
		}
		
		chkBoxNetwork.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					AppPrefrences.setNetworkType(NETWORK_TYPE.WIFI);	
				}else{
					AppPrefrences.setNetworkType(NETWORK_TYPE.EHTERNET);
				}
				
				getMacAddress();
				
				setTitleForNetwork(isChecked);
			}
		});
		
		
		getMacAddress();
		
		
	}
	
	public void getMacAddress()
	{
		
		String deviceId = "";
		try{
			
			if(AppPrefrences.getNetworkType() == NETWORK_TYPE.WIFI){
				deviceId = Utils.getWifiAddress(this);
				
			}else{
				deviceId = Utils.getMACAddress("eth0");

			}
			
		}
		catch(Exception ex){
			deviceId="";	
		}
		
		if(deviceId != null && deviceId.length() ==0)
		{
			if(AppPrefrences.getNetworkType() == NETWORK_TYPE.WIFI){
				deviceId = "Wifi not supported.";
	
			}else{
				deviceId = "Ethernet not supported.";
			}
		}
		((TextView) findViewById(R.id.lbl_networkid)).setText(deviceId);

		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		this.setResult(RESULT_OK);		
		finish();
	}

}
