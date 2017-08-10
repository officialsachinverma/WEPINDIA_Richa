package com.mswipetech.wisepad.sdktest.view;

import com.mswipetech.wisepad.R;
import com.mswipetech.wisepad.sdk.MswipeWisepadController.GATEWAYENV;
import com.mswipetech.wisepad.sdk.MswipeWisepadController.NETWORK_TYPE;
import com.mswipetech.wisepad.sdktest.data.AppPrefrences;

import android.app.Activity;
import android.os.Bundle;

public class BaseTitleActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(AppPrefrences.getGateWayEnv() == GATEWAYENV.LABS){			
			
			this.setTitle(true);
		}else{
			this.setTitle(false);
		}
	}
	
	
	protected void setTitle(boolean isGateWayLabs){
		
		if(isGateWayLabs){
			if(AppPrefrences.getNetworkType() == NETWORK_TYPE.WIFI)	
				
				this.setTitle(getString(R.string.app_name) + "- (LABS)-W");
			else
				this.setTitle(getString(R.string.app_name) + "- (LABS)-E");
		}else{
			if(AppPrefrences.getNetworkType() == NETWORK_TYPE.WIFI)	
				
				this.setTitle(getString(R.string.app_name) + "- (Live)-W");
			else
				this.setTitle(getString(R.string.app_name) + "- (Live)-E");
		
		}
	}


	protected void setTitleForNetwork(boolean isNetworkWify){
		if(isNetworkWify){
			if(AppPrefrences.getGateWayEnv() == GATEWAYENV.LABS)	
				
				this.setTitle(getString(R.string.app_name) + "- (Labs)-W");
			else
				this.setTitle(getString(R.string.app_name) + "- (Live)-W");
		}else{
			if(AppPrefrences.getGateWayEnv() == GATEWAYENV.PRODUCTION)	
					
				this.setTitle(getString(R.string.app_name) + "- (Live)-E");
			else
				this.setTitle(getString(R.string.app_name) + "- (Labs)-E");
		
		}
	}

	
}
