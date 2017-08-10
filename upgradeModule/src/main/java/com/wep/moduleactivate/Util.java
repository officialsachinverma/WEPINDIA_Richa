package com.wep.moduleactivate;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * Created by PriyabratP on 20-03-2017.
 */

public class Util {

    public static String getMacAddress(Context context) {
        WifiManager wimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String macAddress = wimanager.getConnectionInfo().getMacAddress();
        if (macAddress == null) {
            macAddress = "Device don't have mac address or wi-fi is disabled";
        }
        return macAddress;
    }
}
