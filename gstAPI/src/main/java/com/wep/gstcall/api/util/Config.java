package com.wep.gstcall.api.util;

import android.os.Environment;

/**
 * Created by PriyabratP on 21-11-2016.
 */

public class Config {

    public static final String FILE_PATH = Environment.getExternalStorageDirectory().toString();

    public static final String BASE_URL = "http://103.230.84.67";

    public static final String GSTR1_URL = "http://13.71.118.152/AndroWeb/API/GSTR1Save/SendGSTR1Save";

    public static final String GSTR1A_URL = BASE_URL+"/GstAPI/API/GSTR1/sendGstr1A";

    public static final String GSTR2_URL = "http://13.71.118.152/AndroWeb/API/GSTR2Save/SendGSTR2Save";

    public static final String GSTR_GET_API = BASE_URL+"/API/API/GSTR2A?gstin=04AABFN9870CMZT&ret_period=072016&action=B2B&action_required=Y";

    public static final String GSTR2_B2B_GET_API = BASE_URL+"/API/API/GSTR2AJson?gstin=04AABFN9870CMZT&ret_period=072016&action=B2B&action_required=Y";

    public static final String GSTR1_SUMMERY_GET_API = BASE_URL+"/API/API/GSTR1Json?ret_period=072016&gstin=04AABFN9870CMZT&action=RETSUM";

    public static final String GSTR1A_SUMMERY_GET_API = BASE_URL+"/API/API/GSTR1Json?ret_period=072016&gstin=04AABFN9870CMZT&action=RETSUM";

    public static final String GSTR3_GET = "http://103.230.84.67/API/API/GSTR3Json?ret_period=072016&gstin=04AABFN9870CMZT&action=RETDET";

    public static final String GSTR2_DAY= BASE_URL+"/API/API/Inward?";

    public static final String GSTR1_DAY= BASE_URL+"/API/API/Outward?";


    public static final String Upload_No_of_Invoices = "http://metering.wepaspservices.com/api/Device/addBillCount?";
    public static final String Base_URL_Azure = "https://wepgspapi.azure-api.net";
    public static final String GET_TOKEN_API = "/api/token/getToken";
    public static final String GSTR1_SAVE_AZURE_API = "/sendGstr1Save/SendGstr1Save";
    public static final String GSTR2_SAVE_AZURE_API = "/gstr2SaveData/SendGstr2Save";

}
