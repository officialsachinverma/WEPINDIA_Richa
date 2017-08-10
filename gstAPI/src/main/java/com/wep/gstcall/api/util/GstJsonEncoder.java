package com.wep.gstcall.api.util;

import com.google.gson.Gson;
import com.wep.common.app.gst.GSTR1Data;
import com.wep.common.app.gst.GSTRData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by PriyabratP on 17-11-2016.
 */

public class GstJsonEncoder {


    public static String getGSTRJsonEncode(GSTRData gstrData){
        String jsonInString = "";
        try {
            Gson gson = new Gson();
            jsonInString = gson.toJson(gstrData);
            int i;
            return jsonInString;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonInString;
    }

    public static String getGSTR1JsonEncode(GSTR1Data gstrData){
        String jsonInString = "";
        try {
            Gson gson = new Gson();
            jsonInString = gson.toJson(gstrData);
            int i;
            return jsonInString;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonInString;
    }


    /*public static String getGSTR2JsonEncode(String userName, GSTR2Data gstr2Data){
        GSTRData gstrData = new GSTRData(userName,gstr2Data);
        Gson gson = new Gson();
        String jsonInString = gson.toJson(gstrData);
        return jsonInString;
    }*/
}
