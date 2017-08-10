package com.wepindia.pos.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import static android.content.Context.DOWNLOAD_SERVICE;
import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.gst.GSTR1AB2BSData;
import com.wep.common.app.gst.GSTR1AData;
import com.wep.common.app.gst.GSTR1B2BAInvoiceItems;
import com.wep.common.app.gst.GSTR1B2CSAData;
import com.wep.common.app.gst.GSTR1B2CSData;
import com.wep.common.app.gst.GSTR1Data;
import com.wep.common.app.gst.GSTR1_B2B_A_Data;
import com.wep.common.app.gst.GSTR1_B2B_Data;
import com.wep.common.app.gst.GSTR1_B2CL_A_Data;
import com.wep.common.app.gst.GSTR1_B2CL_Data;
import com.wep.common.app.gst.GSTR1_CDN_Data;
import com.wep.common.app.gst.GSTR1_DOCS_Data;
import com.wep.common.app.gst.GSTR1_HSN_Data;
import com.wep.common.app.gst.GSTR2B2BAItemDetails;
import com.wep.common.app.gst.GSTR2Data;
import com.wep.common.app.gst.GSTR2_B2B_A_Data_Unregistered;
import com.wep.common.app.gst.GSTR2_B2B_A_Data_registered;
import com.wep.common.app.gst.GSTR2_B2B_Data_Unregistered;
import com.wep.common.app.gst.GSTR2_B2B_Data_registered;
import com.wep.common.app.gst.GSTR2_CDN_Data;
import com.wep.common.app.gst.GSTRData;
import com.wep.gstcall.api.http.DownloadFileFromURL;
import com.wep.gstcall.api.http.HTTPAsyncTask;
import com.wep.gstcall.api.util.Config;
import com.wep.gstcall.api.util.GstJsonEncoder;
import com.wepindia.pos.GST.controlers.GSTDataController;
import com.wepindia.pos.GST.controlers.GSTUploadFunctions;
import com.wepindia.pos.GSTSupport.HTTPAsyncTask_Frag;
import com.wepindia.pos.GSTSupport.TokenAsync_Frag;
import com.wepindia.pos.GenericClasses.DateTime;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.R;
import com.wepindia.pos.utils.ConnectionDetector;
import com.wepindia.pos.utils.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FragmentGSTLink extends Fragment   implements HTTPAsyncTask_Frag.OnHTTPRequestCompletedListener,
        DownloadFileFromURL.OnFileDownloadCompletedListener,TokenAsync_Frag.OnTokenRequestCompletedListener{


    public FragmentGSTLink() {
        // Required empty public constructor
    }

    String HeaderAuthorizationData_POST_APIS ="Ocp-Apim-Subscription-Key@07cde031cc1646efae45746a8c844974"+
            ",SOURCE_TYPE@POS";

    private static final String Header_TokenAuth ="Ocp-Apim-Subscription-Key@07cde031cc1646efae45746a8c844974," +
            "client_id@e03001c6-59b7-4bbb-919a-778108e643b9,client_secret@nOViR/b/Q7L/iwQkzWIDG19DzcqbaiC82yNVFF3J9qc=";

    String strJson1 = "{\n" +
            "\t\"gstValue\" :  {\n" +
            "  \"gstin\": \"27AHQPA7588L1ZJ\",\n" +
            "  \"fp\": \"122016\",\n" +
            "  \"gt\": 3782969.01,\n" +
            "  \"cur_gt\": 3782969.01,\n" +
            "  \"b2b\": [\n" +
            "    {\n" +
            "      \"ctin\": \"01AABCE2207R1Z5\",\n" +
            "      \"inv\": [\n" +
            "        {\n" +
            "          \"inum\": \"S008400\",\n" +
            "          \"idt\": \"24-11-2016\",\n" +
            "          \"val\": 729248.16,\n" +
            "          \"pos\": \"06\",\n" +
            "          \"rchrg\": \"N\",\n" +
            "          \"etin\": \"01AABCE5507R1Z4\",\n" +
            "          \"inv_typ\": \"R\",\n" +
            "          \"itms\": [\n" +
            "            {\n" +
            "              \"num\": 1,\n" +
            "              \"itm_det\": {\n" +
            "                \"rt\": 5,\n" +
            "                \"txval\": 10000,\n" +
            "                \"iamt\": 833.33,\n" +
            "                \"csamt\": 500\n" +
            "              }\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"b2cl\": [\n" +
            "    {\n" +
            "      \"pos\": \"05\",\n" +
            "      \"inv\": [\n" +
            "        {\n" +
            "          \"inum\": \"92661\",\n" +
            "          \"idt\": \"10-01-2016\",\n" +
            "          \"val\": 784586.33,\n" +
            "          \"etin\": \"27AHQPA8875L1ZU\",\n" +
            "          \"itms\": [\n" +
            "            {\n" +
            "              \"num\": 1,\n" +
            "              \"itm_det\": {\n" +
            "                \"rt\": 5,\n" +
            "                \"txval\": 10000,\n" +
            "                \"iamt\": 833.33,\n" +
            "                \"csamt\": 500\n" +
            "              }\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"cdnr\": [\n" +
            "    {\n" +
            "      \"ctin\": \"01AAAAP1208Q1ZS\",\n" +
            "      \"nt\": [\n" +
            "        {\n" +
            "          \"ntty\": \"C\",\n" +
            "          \"nt_num\": \"533515\",\n" +
            "          \"nt_dt\": \"23-09-2016\",\n" +
            "          \"inum\": \"915914\",\n" +
            "          \"idt\": \"23-09-2016\",\n" +
            "          \"val\": 123123,\n" +
            "          \"itms\": [\n" +
            "            {\n" +
            "              \"num\": 1,\n" +
            "              \"itm_det\": {\n" +
            "                \"rt\": 10,\n" +
            "                \"txval\": 5225.28,\n" +
            "                \"iamt\": 845.22,\n" +
            "                \"csamt\": 789.52\n" +
            "              }\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"b2cs\": [\n" +
            "    {\n" +
            "      \"sply_ty\": \"INTER\",\n" +
            "      \"rt\": 5,\n" +
            "      \"typ\": \"E\",\n" +
            "      \"etin\": \"20ABCDE7588L1ZJ\",\n" +
            "      \"pos\": \"05\",\n" +
            "      \"txval\": 110,\n" +
            "      \"iamt\": 10,\n" +
            "      \"csamt\": 10\n" +
            "    },\n" +
            "    {\n" +
            "      \"rt\": 5,\n" +
            "      \"sply_ty\": \"INTER\",\n" +
            "      \"typ\": \"OE\",\n" +
            "      \"txval\": 100,\n" +
            "      \"iamt\": 10,\n" +
            "      \"csamt\": 10\n" +
            "    }\n" +
            "  ],\n" +
            "  \"exp\": [\n" +
            "    {\n" +
            "      \"exp_typ\": \"WPAY\",\n" +
            "      \"inv\": [\n" +
            "        {\n" +
            "          \"inum\": \"81542\",\n" +
            "          \"idt\": \"12-02-2016\",\n" +
            "          \"val\": 995048.36,\n" +
            "          \"sbnum\": \"ASB9950842981\",\n" +
            "          \"sbdt\": \"04-10-2016\",\n" +
            "          \"itms\": [\n" +
            "            {\n" +
            "              \"txval\": 10000,\n" +
            "              \"rt\": 5,\n" +
            "              \"iamt\": 833.33\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"exp_typ\": \"WOPAY\",\n" +
            "      \"inv\": [\n" +
            "        {\n" +
            "          \"inum\": \"81542\",\n" +
            "          \"idt\": \"12-02-2016\",\n" +
            "          \"val\": 995048.36,\n" +
            "          \"sbnum\": \"ASB9950842981\",\n" +
            "          \"sbdt\": \"04-10-2016\",\n" +
            "          \"itms\": [\n" +
            "            {\n" +
            "              \"txval\": 10000,\n" +
            "              \"rt\": 0,\n" +
            "              \"iamt\": 0\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"hsn\": {\n" +
            "    \"data\": [\n" +
            "      {\n" +
            "        \"num\": 1,\n" +
            "        \"hsn_sc\": \"1009\",\n" +
            "        \"desc\": \"Goods Description\",\n" +
            "        \"uqc\": \"kg\",\n" +
            "        \"qty\": 2.05,\n" +
            "        \"val\": 995048.36,\n" +
            "        \"txval\": 10.23,\n" +
            "        \"iamt\": 14.52,\n" +
            "        \"csamt\": 500\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"nil\": {\n" +
            "    \"inv\": [\n" +
            "      {\n" +
            "        \"sply_ty\": \"INTRB2B\",\n" +
            "        \"expt_amt\": 123.45,\n" +
            "        \"nil_amt\": 1470.85,\n" +
            "        \"ngsup_amt\": 1258.5\n" +
            "      },\n" +
            "      {\n" +
            "        \"sply_ty\": \"INTRB2C\",\n" +
            "        \"expt_amt\": 123.45,\n" +
            "        \"nil_amt\": 1470.85,\n" +
            "        \"ngsup_amt\": 1258.5\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"txpd\": [\n" +
            "    {\n" +
            "      \"pos\": \"05\",\n" +
            "      \"sply_ty\": \"INTER\",\n" +
            "      \"itms\": [\n" +
            "        {\n" +
            "          \"rt\": 5,\n" +
            "          \"ad_amt\": 100,\n" +
            "          \"iamt\": 9400,\n" +
            "          \"csamt\": 500\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"at\": [\n" +
            "    {\n" +
            "      \"pos\": \"05\",\n" +
            "      \"sply_ty\": \"INTER\",\n" +
            "      \"itms\": [\n" +
            "        {\n" +
            "          \"rt\": 5,\n" +
            "          \"ad_amt\": 100,\n" +
            "          \"iamt\": 9400,\n" +
            "          \"csamt\": 500\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"doc_issue\": [\n" +
            "    {\n" +
            "      \"doc_num\": 1,\n" +
            "      \"doc_typ\": \"Invoices for outward supply\",\n" +
            "      \"docs\": [\n" +
            "        {\n" +
            "          \"num\": 1,\n" +
            "          \"from\": \"20\",\n" +
            "          \"to\": \"29\",\n" +
            "          \"totnum\": 20,\n" +
            "          \"cancel\": 3,\n" +
            "          \"net_issue\": 17\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"cdnur\": [\n" +
            "    {\n" +
            "      \"typ\": \"B2CL\",\n" +
            "      \"ntty\": \"C\",\n" +
            "      \"nt_num\": \"533515\",\n" +
            "      \"nt_dt\": \"23-09-2016\",\n" +
            "      \"inum\": \"915914\",\n" +
            "      \"idt\": \"23-09-2016\",\n" +
            "      \"val\": 64646,\n" +
            "      \"itms\": [\n" +
            "        {\n" +
            "          \"num\": 1,\n" +
            "          \"itm_det\": {\n" +
            "            \"rt\": 10,\n" +
            "            \"txval\": 5225.28,\n" +
            "            \"iamt\": 845.22,\n" +
            "            \"csamt\": 789.52\n" +
            "          }\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}\n" +
            "}\n";

    String strjson2 = "{\n" +
            "\t\"gstValue\" :  {\n" +
            "  \"gstin\": \"07CQZCD1111I4Z7\",\n" +
            "  \"fp\": \"082016\",\n" +
            "  \"b2b\": [\n" +
            "    {\n" +
            "      \"ctin\": \"01AABCE2207R1Z5\",\n" +
            "      \"inv\": [\n" +
            "        {\n" +
            "          \"inum\": \"S008400\",\n" +
            "          \"idt\": \"24-11-2016\",\n" +
            "          \"val\": 729248.16,\n" +
            "          \"pos\": \"06\",\n" +
            "          \"rchrg\": \"N\",\n" +
            "          \"inv_typ\": \"R\",\n" +
            "          \"itms\": [\n" +
            "            {\n" +
            "              \"num\": 1,\n" +
            "              \"itm_det\": {\n" +
            "                \"rt\": 10.1,\n" +
            "                \"txval\": 6210.99,\n" +
            "                \"iamt\": 0,\n" +
            "                \"camt\": 614.44,\n" +
            "                \"samt\": 5.68,\n" +
            "                \"csamt\": 621.09\n" +
            "              },\n" +
            "              \"itc\": {\n" +
            "                \"elg\": \"ip\",\n" +
            "                \"tx_i\": 147.2,\n" +
            "                \"tx_s\": 159.3,\n" +
            "                \"tx_c\": 159.3,\n" +
            "                \"tx_cs\": 0\n" +
            "              }\n" +
            "            }\n" +
            "          ]\n" +
            "        },\n" +
            "        {\n" +
            "          \"flag\": \"M\",\n" +
            "          \"chksum\": \"adsgshaklfsgdhdydudjdk\",\n" +
            "          \"inum\": \"S008401\",\n" +
            "          \"idt\": \"24-11-2016\",\n" +
            "          \"val\": 729248.16,\n" +
            "          \"pos\": \"06\",\n" +
            "          \"rchrg\": \"N\",\n" +
            "          \"inv_typ\": \"R\",\n" +
            "          \"itms\": [\n" +
            "            {\n" +
            "              \"num\": 1,\n" +
            "              \"itm_det\": {\n" +
            "                \"rt\": 10.1,\n" +
            "                \"txval\": 6210.99,\n" +
            "                \"iamt\": 0,\n" +
            "                \"camt\": 614.44,\n" +
            "                \"samt\": 5.68,\n" +
            "                \"csamt\": 621.09\n" +
            "              },\n" +
            "              \"itc\": {\n" +
            "                \"elg\": \"ip\",\n" +
            "                \"tx_i\": 147.2,\n" +
            "                \"tx_s\": 159.3,\n" +
            "                \"tx_c\": 159.3,\n" +
            "                \"tx_cs\": 0\n" +
            "              }\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"b2bur\": [\n" +
            "    {\n" +
            "      \"inv\": [\n" +
            "        {\n" +
            "          \"cname\": \"Kamath Food Ltd.\",\n" +
            "          \"chksum\": \"AflJufPlFStqKBZ\",\n" +
            "          \"inum\": \"S008400\",\n" +
            "          \"idt\": \"24-11-2016\",\n" +
            "          \"val\": 729248.16,\n" +
            "          \"itms\": [\n" +
            "            {\n" +
            "              \"num\": 1,\n" +
            "              \"itm_det\": {\n" +
            "                \"rt\": 10.1,\n" +
            "                \"txval\": 6210.99,\n" +
            "                \"camt\": 614.44,\n" +
            "                \"samt\": 5.68,\n" +
            "                \"csamt\": 621.09\n" +
            "              },\n" +
            "              \"itc\": {\n" +
            "                \"elg\": \"ip\",\n" +
            "                \"tx_s\": 159.3,\n" +
            "                \"tx_c\": 159.3,\n" +
            "                \"tx_cs\": 0\n" +
            "              }\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"cdn\": [\n" +
            "    {\n" +
            "      \"ctin\": \"01AAAAP1208Q1ZS\",\n" +
            "      \"nt\": [\n" +
            "        {\n" +
            "          \"ntty\": \"C\",\n" +
            "          \"nt_num\": \"533515\",\n" +
            "          \"nt_dt\": \"23-09-2016\",\n" +
            "          \"rsn\": \"Not Mentioned\",\n" +
            "          \"p_gst\": \"N\",\n" +
            "          \"inum\": \"915914\",\n" +
            "          \"idt\": \"23-09-2016\",\n" +
            "          \"itms\": [\n" +
            "            {\n" +
            "              \"num\": 1,\n" +
            "              \"itm_det\": {\n" +
            "                \"rt\": 10,\n" +
            "                \"txval\": 5225.28,\n" +
            "                \"iamt\": 845.22,\n" +
            "                \"camt\": 37661.29,\n" +
            "                \"samt\": 42.13,\n" +
            "                \"csamt\": 789.52\n" +
            "              },\n" +
            "              \"itc\": {\n" +
            "                \"elg\": \"ip\",\n" +
            "                \"tx_i\": 147.2,\n" +
            "                \"tx_s\": 159.3,\n" +
            "                \"tx_c\": 159.3,\n" +
            "                \"tx_cs\": 0\n" +
            "              }\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"hsnsum\": {\n" +
            "    \"det\": [\n" +
            "      {\n" +
            "        \"num\": 1,\n" +
            "        \"hsn_sc\": \"40561111\",\n" +
            "        \"desc\": \"Goods Description\",\n" +
            "        \"uqc\": \"Kg\",\n" +
            "        \"qty\": 80,\n" +
            "        \"val\": 9000.5,\n" +
            "        \"txval\": 8451.65,\n" +
            "        \"iamt\": 0,\n" +
            "        \"camt\": 0.83,\n" +
            "        \"samt\": 6736920.69,\n" +
            "        \"csamt\": 0\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"imp_g\": [\n" +
            "    {\n" +
            "      \"is_sez\": \"Y\",\n" +
            "      \"stin\": \"01AABCE2207R1Z5\",\n" +
            "      \"boe_num\": \"25662\",\n" +
            "      \"boe_dt\": \"18-04-2016\",\n" +
            "      \"boe_val\": 338203.29,\n" +
            "      \"itms\": [\n" +
            "        {\n" +
            "          \"num\": 1,\n" +
            "          \"txval\": 582.88,\n" +
            "          \"rt\": 10.5,\n" +
            "          \"iamt\": 159.3,\n" +
            "          \"csamt\": 159.3,\n" +
            "          \"elg\": \"ip\",\n" +
            "          \"tx_i\": 123.02,\n" +
            "          \"tx_cs\": 0\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"imp_s\": [\n" +
            "    {\n" +
            "      \"inum\": \"85619\",\n" +
            "      \"idt\": \"22-03-2016\",\n" +
            "      \"ival\": 962559.86,\n" +
            "      \"pos\": \"28\",\n" +
            "      \"itms\": [\n" +
            "        {\n" +
            "          \"num\": 1,\n" +
            "          \"txval\": 582.88,\n" +
            "          \"elg\": \"ip\",\n" +
            "          \"rt\": 10.5,\n" +
            "          \"iamt\": 123.02,\n" +
            "          \"csamt\": 0,\n" +
            "          \"tx_i\": 500,\n" +
            "          \"tx_cs\": 0\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"nil_supplies\": {\n" +
            "    \"inter\": {\n" +
            "      \"cpddr\": 0,\n" +
            "      \"exptdsply\": 5394970.87,\n" +
            "      \"ngsply\": 992.93,\n" +
            "      \"nilsply\": 0\n" +
            "    },\n" +
            "    \"intra\": {\n" +
            "      \"cpddr\": 1000,\n" +
            "      \"exptdsply\": 5394970.87,\n" +
            "      \"ngsply\": 992.93,\n" +
            "      \"nilsply\": 0\n" +
            "    }\n" +
            "  },\n" +
            "  \"txi\": [\n" +
            "    {\n" +
            "      \"pos\": \"05\",\n" +
            "      \"sply_ty\": \"INTER\",\n" +
            "      \"itms\": [\n" +
            "        {\n" +
            "          \"num\": 1,\n" +
            "          \"rt\": 5,\n" +
            "          \"adamt\": 100,\n" +
            "          \"iamt\": 9400,\n" +
            "          \"camt\": 0,\n" +
            "          \"samt\": 0,\n" +
            "          \"csamt\": 500\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"txpd\": [\n" +
            "    {\n" +
            "      \"pos\": \"05\",\n" +
            "      \"sply_ty\": \"INTER\",\n" +
            "      \"itms\": [\n" +
            "        {\n" +
            "          \"num\": 1,\n" +
            "          \"rt\": 5,\n" +
            "          \"adamt\": 100,\n" +
            "          \"iamt\": 9400,\n" +
            "          \"camt\": 0,\n" +
            "          \"samt\": 0,\n" +
            "          \"csamt\": 500\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"itc_rvsl\": {\n" +
            "    \"rule2_2\": {\n" +
            "      \"iamt\": 0,\n" +
            "      \"camt\": 13,\n" +
            "      \"samt\": 13,\n" +
            "      \"csamt\": 12\n" +
            "    },\n" +
            "    \"rule7_1_m\": {\n" +
            "      \"iamt\": 0,\n" +
            "      \"camt\": 13,\n" +
            "      \"samt\": 13,\n" +
            "      \"csamt\": 12\n" +
            "    },\n" +
            "    \"rule8_1_h\": {\n" +
            "      \"iamt\": 0,\n" +
            "      \"camt\": 13,\n" +
            "      \"samt\": 13,\n" +
            "      \"csamt\": 12\n" +
            "    },\n" +
            "    \"rule7_2_a\": {\n" +
            "      \"iamt\": 0,\n" +
            "      \"camt\": 13,\n" +
            "      \"samt\": 13,\n" +
            "      \"csamt\": 12\n" +
            "    },\n" +
            "    \"rule7_2_b\": {\n" +
            "      \"iamt\": 0,\n" +
            "      \"camt\": 13,\n" +
            "      \"samt\": 13,\n" +
            "      \"csamt\": 12\n" +
            "    },\n" +
            "    \"revitc\": {\n" +
            "      \"iamt\": 0,\n" +
            "      \"camt\": 13,\n" +
            "      \"samt\": 13,\n" +
            "      \"csamt\": 12\n" +
            "    },\n" +
            "    \"other\": {\n" +
            "      \"iamt\": 0,\n" +
            "      \"camt\": 13,\n" +
            "      \"samt\": 13,\n" +
            "      \"csamt\": 12\n" +
            "    }\n" +
            "  },\n" +
            "  \"cdnur\": [\n" +
            "    {\n" +
            "      \"rtin\": \"01AAAAP1208Q1ZS\",\n" +
            "      \"ntty\": \"C\",\n" +
            "      \"nt_num\": \"533515\",\n" +
            "      \"nt_dt\": \"23-09-2016\",\n" +
            "      \"rsn\": \"Not Mentioned\",\n" +
            "      \"p_gst\": \"N\",\n" +
            "      \"inum\": \"915914\",\n" +
            "      \"idt\": \"23-09-2016\",\n" +
            "      \"itms\": [\n" +
            "        {\n" +
            "          \"num\": 1,\n" +
            "          \"itm_det\": {\n" +
            "            \"rt\": 10,\n" +
            "            \"txval\": 5225.28,\n" +
            "            \"camt\": 37661.29,\n" +
            "            \"samt\": 42.13,\n" +
            "            \"csamt\": 789.52\n" +
            "          },\n" +
            "          \"itc\": {\n" +
            "            \"elg\": \"ip\",\n" +
            "            \"tx_s\": 159.3,\n" +
            "            \"tx_c\": 159.3,\n" +
            "            \"tx_cs\": 0\n" +
            "          }\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}\n" +
            "}\n";
    private static final int REQUEST_GET_GSTR1_SUMMERY = 1002;
    private static final int REQUEST_GET_GSTR3 = 1003;
    private static final int REQUEST_SAVE_GSTR1 = 1;
    private static final int REQUEST_SAVE_GSTR2 = 2;
    private static final int REQUEST_GET_GSTR1_SUMMARY = 1006;
    private static final int REQUEST_GET_GSTR1 = 1009;
    private static final int REQUEST_GET_GSTR1A = 1007;
    private static final int REQUEST_GET_GSTR2A = 1008;
    private static final int REQUEST_GET_GSTR2_Reconcile = 1010;
    private static final int REQUEST_SAVE_GSTR1A = 11;
    private String strDate = "";
    private EditText etReportDateStart,etReportDateEnd;
    Button btn_ReportDateFrom,btn_ReportDateTo;
    private MessageDialog MsgBox;
    public DatabaseHandler dbGSTLink;
    private DateTime objDate;
    private GSTDataController dataController;
    private ProgressDialog progressDialog,pDialog,progressToken;

    private SharedPreferences sharedPreferences;
    private RelativeLayout PostGSTR1,fileGSTR1,getGSTRR2B2B,postGSTR2,getGSTR3;
    private RelativeLayout fileGSTR2, fileGSTR3, getGSTR1Summary,getGSTR1,getGSTR1A, getGSTR2A,getGSTR2Reconcile;
    private  String BillNoPrefix = "";

    private static final int REQUEST_GET_GSTR2_B2B = 1001;

    Context myContext ;
    public Activity myActivity ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            dbGSTLink = new DatabaseHandler(getActivity());
            dbGSTLink.OpenDatabase();
            BillNoPrefix = dbGSTLink.getBillNoPrefix();
        }catch (Exception e){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_gstlink, container, false);
        myActivity = getActivity();
        myContext = getContext();
        init(view);
        return view;
    }

    private void getGSTR3() {
        progressDialog.show();
        new HTTPAsyncTask_Frag(this, HTTPAsyncTask.HTTP_GET,"",REQUEST_GET_GSTR3, Config.GSTR3_GET,"").execute();
    }

    private void getGSTR1AllSummery() {
        progressDialog.show();
        new HTTPAsyncTask_Frag(this, HTTPAsyncTask.HTTP_GET,"",REQUEST_GET_GSTR1_SUMMERY, Config.GSTR1_SUMMERY_GET_API,"").execute();
    }
    // After Tab UI Completes Put All codes below

    private void init(View view) {
        MsgBox = new MessageDialog(myActivity);
        etReportDateStart = (EditText) view.findViewById(R.id.etReportDateStart);
        etReportDateEnd = (EditText) view.findViewById(R.id.etReportDateEnd);
        btn_ReportDateFrom = (Button)view.findViewById(R.id.btn_ReportDateFrom);
        btn_ReportDateTo = (Button)view.findViewById(R.id.btn_ReportDateTo);
        fileGSTR1 = (RelativeLayout) view.findViewById(R.id.fileGSTR1);
        fileGSTR2 = (RelativeLayout) view.findViewById(R.id.fileGSTR2);
        fileGSTR3 = (RelativeLayout) view.findViewById(R.id.fileGSTR3);
        postGSTR2 = (RelativeLayout) view.findViewById(R.id.postGSTR2);
        PostGSTR1 = (RelativeLayout) view.findViewById(R.id.PostGSTR1);
        getGSTR1Summary = (RelativeLayout) view.findViewById(R.id.getGSTR1Summary);
        getGSTR1 = (RelativeLayout) view.findViewById(R.id.getGSTR1);
        getGSTR1A = (RelativeLayout) view.findViewById(R.id.getGSTR1A);
        getGSTR2A = (RelativeLayout) view.findViewById(R.id.getGSTR2A);
        getGSTR2Reconcile = (RelativeLayout) view.findViewById(R.id.getGSTR2Reconcile);
        getGSTR3 = (RelativeLayout) view.findViewById(R.id.getGSTR3);
        getGSTRR2B2B = (RelativeLayout) view.findViewById(R.id.getGSTRR2B2B);
        dbGSTLink = new DatabaseHandler(myContext);
        sharedPreferences = myActivity.getSharedPreferences("com.wepindia.pos",Context.MODE_PRIVATE);

        try {
            dbGSTLink.CreateDatabase();
            dbGSTLink.OpenDatabase();

            Date d = new Date();
            CharSequence currentdate = DateFormat.format("yyyy-MM-dd", d.getTime());
            objDate = new DateTime(currentdate.toString());
            btn_ReportDateFrom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    From(v);
                }
            });
            btn_ReportDateTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    To(v);
                }
            });
            fileGSTR1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickFileGstr(v);
                }
            });
            fileGSTR2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickFileGstr(v);
                }
            });
            fileGSTR3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickFileGstr(v);
                }
            });
            postGSTR2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickPostGstr2(v);
                }
            });
            PostGSTR1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickPostGstr1(v);
                }
            });
            getGSTR1Summary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { onClickGetGstr1Summary(v); }});
            getGSTR1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { onClickGetGstr1(v); }});
            getGSTR1A.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { onClickGetGstr1A(v); }});
            getGSTR2A.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { onClickGetGstr2A(v); }});
            getGSTR2Reconcile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { onClickGetGstr2Reconcile(v); }});
            getGSTR3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { onClickGetGstr3(v); }});

           /* getGSTRR2B2B.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickGetGstr2B2B(v);
                }
            });*/



        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            MsgBox.Show("Error", e.getMessage());
        }
        dataController = new GSTDataController(myContext,dbGSTLink);
        progressDialog = new ProgressDialog(myContext);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        pDialog = new ProgressDialog(myContext);
        pDialog.setMessage("Downloading file. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(true);
        progressToken = new ProgressDialog(myContext);
        progressToken.setMessage("Getting Token ..");
        progressToken.setIndeterminate(false);
        progressToken.setMax(100);
        progressToken.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressToken.setCancelable(true);
    }
    private void DateSelection(final int DateType) {        // StartDate: DateType = 1 EndDate: DateType = 2
        try {
            AlertDialog.Builder dlgReportDate = new AlertDialog.Builder(myContext);
            final DatePicker dateReportDate = new DatePicker(myContext);
            // Initialize date picker value to business date
            dateReportDate.updateDate(objDate.getYear(), objDate.getMonth(), objDate.getDay());
            String strMessage = "";
            if (DateType == 1) {
                strMessage = "Select report Start date";
            } else {
                strMessage = "Select report End date";
            }

            dlgReportDate
                    .setIcon(R.drawable.ic_launcher)
                    .setTitle("Date Selection")
                    .setMessage(strMessage)
                    .setView(dateReportDate)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                            String strDate ="";
                            if (dateReportDate.getDayOfMonth() < 10) {
                                strDate += "0" + String.valueOf(dateReportDate.getDayOfMonth());
                            } else {
                                strDate += String.valueOf(dateReportDate.getDayOfMonth());
                            }
                            if (dateReportDate.getMonth() < 9) {
                                strDate += "-0" + String.valueOf(dateReportDate.getMonth() + 1) + "-";
                            } else {
                                strDate += "-"+String.valueOf(dateReportDate.getMonth() + 1) + "-";
                            }
                            strDate += String.valueOf(dateReportDate.getYear());

                            if (DateType == 1) {
                                etReportDateStart.setText(strDate);
                            } else {
                                etReportDateEnd.setText(strDate);
                            }
                            Log.d("ReportDate", "Selected Date:" + strDate);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    })
                    .show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private boolean DateValidation(String StartDate, String EndDate) {
        if (StartDate.equalsIgnoreCase("") ||
                EndDate.equalsIgnoreCase("")) {
            MsgBox.Show("Warning", "Please select report date range properly");
            return false;
        } else {
            return true;
        }
    }

    public void From(View v) {
        DateSelection(1);
    }

    public void To(View v) {
        if (!etReportDateStart.getText().toString().equalsIgnoreCase("")) {
            DateSelection(2);

        } else {
            MsgBox.Show("Warning", "Please select report FROM date");
        }
    }


    public void postGSTR1Invoices(String respData,String userName) {
        //Toast.makeText(myContext, "hello", Toast.LENGTH_SHORT).show();
        String startDate_str = (etReportDateStart.getText().toString()) ;
        String endDate_str = (etReportDateEnd.getText().toString()) ;
        try{
            String start_milli = String.valueOf((new SimpleDateFormat("dd-MM-yyyy").parse(startDate_str)).getTime());
            String end_milli = String.valueOf((new SimpleDateFormat("dd-MM-yyyy").parse(endDate_str)).getTime());
        if(ConnectionDetector.isInternetConnection(myContext)) {
            progressDialog.show();
            String str[] = startDate_str.split("-");
            GSTUploadFunctions handle = new GSTUploadFunctions(myContext,dbGSTLink,BillNoPrefix);
            ArrayList<GSTR1_B2B_Data> list_b2b = handle.getGSTR1B2BList(start_milli,end_milli);
            ArrayList<GSTR1_B2CL_Data> list_b2cl= handle.getGSTR1B2CLList(start_milli,end_milli);
            ArrayList<GSTR1_CDN_Data> cdnList= handle.getGSTR1CDNData(start_milli,end_milli);
            ArrayList<GSTR1B2CSData> list_b2cs= handle.makeGSTR1B2CS( start_milli,  end_milli);
            ArrayList<GSTR1_HSN_Data> hsnList= handle.getGSTR1HSNData(start_milli,end_milli);
            ArrayList<GSTR1_B2B_A_Data> list_b2ba = new ArrayList<>();// =dataController.getGSTR1B2BAList(start_milli,end_milli);
            ArrayList<GSTR1_B2CL_A_Data> list_b2cla= new ArrayList<>();// = dataController.getGSTR1B2CL_A_List(start_milli,end_milli);
            ArrayList<GSTR1B2CSAData> list_b2csA= new ArrayList<>();// = makeGSTR1B2CSA( start_milli,  end_milli);
            ArrayList<GSTR1_DOCS_Data> list_doc= handle.getGSTR1DOCData( start_milli,  end_milli);
            GSTR1Data gstr1Data = new GSTR1Data( dbGSTLink.getGSTIN(),list_b2b, list_b2ba,list_b2cl,list_b2cla,list_b2cs, list_b2csA,
                    cdnList,hsnList,list_doc);
            GSTRData gstrData = new GSTRData(userName, dbGSTLink.getGSTIN(), gstr1Data);
            String strJson = GstJsonEncoder.getGSTRJsonEncode(gstrData);
            //String strJson = GstJsonEncoder.getGSTR1JsonEncode(gstr1Data);
            System.out.println(strJson);

            try{
                String gstin = dbGSTLink.getGSTIN();
                if(gstin==null)
                    gstin="";
                String referenceno = dbGSTLink.getOwnerReferenceNo();
                if(referenceno==null)
                    referenceno="";
                HeaderAuthorizationData_POST_APIS += ",GSTINNO@"+gstin.trim();
                HeaderAuthorizationData_POST_APIS += ",REFERENCE_NO@"+referenceno.trim();
                JSONObject jsonObject = new JSONObject(respData);
                String Bearer = (String) jsonObject.get("access_token");
                if (Bearer!=null && !Bearer.equals(""))
                {
                    //HeaderAuthorizationData_POST_APIS += ",Authorization@Bearer "+Bearer.trim();
                    HeaderAuthorizationData_POST_APIS += ",Authorization@Bearer "+Bearer;
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }

            String URL = Config.Base_URL_Azure+Config.GSTR1_SAVE_AZURE_API;
            new HTTPAsyncTask_Frag(this, HTTPAsyncTask.HTTP_POST, strJson, REQUEST_SAVE_GSTR1, URL, HeaderAuthorizationData_POST_APIS).execute();

        }

        else
        {
            Toast.makeText(myContext, "No Internet Connection! Try again Later", Toast.LENGTH_SHORT).show();
        }
        }catch(Exception e)
        {
            e.printStackTrace();
            MsgBox.Show("Error", "An error occured while uploading the data");
        }
    }


    public void onClickDownloadGSTR2A(View view) {
        pDialog.show();
        new DownloadFileFromURL(myActivity,pDialog,"B2B", Config.GSTR_GET_API).execute();
    }


    public void PostGSTR2Invoices(String respData,String userName) {
        String startDate_str = (etReportDateStart.getText().toString()) ;
        String endDate_str = (etReportDateEnd.getText().toString()) ;
        GSTUploadFunctions handle = new GSTUploadFunctions(myContext,dbGSTLink, BillNoPrefix);
        try {
            String start_milli = String.valueOf((new SimpleDateFormat("dd-MM-yyyy").parse(startDate_str)).getTime());
            String end_milli = String.valueOf((new SimpleDateFormat("dd-MM-yyyy").parse(endDate_str)).getTime());
            if (ConnectionDetector.isInternetConnection(myContext)) {

                String str[] = startDate_str.split("-");
                ArrayList<GSTR2_B2B_Data_registered> b2bList_registered = handle.getGSTR2_B2B_DataList_registered(start_milli, end_milli);
                ArrayList<GSTR2_B2B_Data_Unregistered> b2bList_Unregistered = handle.getGSTR2_B2B_DataList_Unregistered(start_milli, end_milli);
                ArrayList<GSTR2_B2B_A_Data_registered> b2baList_registered = new ArrayList<>();// dataController.getGSTR2_B2B_A_DataList_registered(start_milli, end_milli);
                ArrayList<GSTR2_B2B_A_Data_Unregistered> b2baList_Unregistered = new ArrayList<>();// dataController.getGSTR2_B2B_A_DataList_Unregistered(start_milli, end_milli);
                ArrayList<GSTR2_CDN_Data> gstr2_cdn_list = new ArrayList<>();// dataController.getGSTR2_CDNData(start_milli, end_milli);

                GSTR2Data gstr2Data = new GSTR2Data(dbGSTLink.getGSTIN(), str[1] + str[2], "N", b2bList_registered,
                        b2bList_Unregistered,b2baList_registered, b2baList_Unregistered,gstr2_cdn_list);
                progressDialog.show();
                GSTRData gstrData = new GSTRData(userName, dbGSTLink.getGSTIN(), gstr2Data);
                String strJson = GstJsonEncoder.getGSTRJsonEncode(gstrData);

                try{
                    String gstin = dbGSTLink.getGSTIN();
                    if(gstin==null)
                        gstin="";
                    String referenceno = dbGSTLink.getOwnerReferenceNo();
                    if(referenceno==null)
                        referenceno="";
                    HeaderAuthorizationData_POST_APIS += ",GSTINNO@"+gstin.trim();
                    HeaderAuthorizationData_POST_APIS += ",REFERENCE_NO@"+referenceno.trim();
                    JSONObject jsonObject = new JSONObject(respData);
                    String Bearer = (String) jsonObject.get("access_token");
                    if (Bearer!=null && !Bearer.equals(""))
                    {
                        //HeaderAuthorizationData_POST_APIS += ",Authorization@Bearer "+Bearer.trim();
                        HeaderAuthorizationData_POST_APIS += ",Authorization@Bearer "+Bearer;
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                String URL = Config.Base_URL_Azure+Config.GSTR2_SAVE_AZURE_API;

                new HTTPAsyncTask_Frag(this, HTTPAsyncTask.HTTP_POST, strJson, REQUEST_SAVE_GSTR2, URL,HeaderAuthorizationData_POST_APIS).execute();
            } else {
                Toast.makeText(myContext, "No Internet Connection! Try again Later", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void disMiss(){
        if(progressDialog!=null){
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }

    /*private void promptAuthFragment(int code) {
        Bundle bundle4=new Bundle();
        AuthFragment alertdFragment = new AuthFragment();
        alertdFragment.setCancelable(false);
        alertdFragment.setCode(code);
        alertdFragment.show(myActivity.getFragmentManager(), "auth");

    }*/

    public void onClickPostGstr1(View view) {
        String startDate = etReportDateStart.getText().toString() ;
        String endDate = etReportDateEnd.getText().toString() ;
        String token1[] = startDate.split("-");
        String token2[] = endDate.split("-");
        if(startDate.equalsIgnoreCase("") || endDate.equalsIgnoreCase(""))
        {
            //disMiss();
            MsgBox.setMessage("Please select Date")
                    .setPositiveButton("OK", null)
                    .show();
        }
        else if (!token1[1].equals(token2[1]))
        {
            MsgBox.setMessage("Please select Date range for one month at a time")
                    .setPositiveButton("OK", null)
                    .show();
        }
        else
        {
            promptAuthFragment(REQUEST_SAVE_GSTR1);
        }
    }

    private void promptAuthFragment(final int REQUESTED_API) {

        AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);

        LayoutInflater UserAuthorization = (LayoutInflater) myContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vwAuthorization = UserAuthorization.inflate(R.layout.user_authorization, null);
       // View view = LayoutInflater.inflate(R.layout.fragment_login_conf_dialog, container, false);
        final EditText editTextUserName = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserId);
        final EditText editTextUserPass = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserPassword);


        AuthorizationDialog
                .setTitle("Authorization")
                .setIcon(R.drawable.ic_launcher)
                .setView(vwAuthorization)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                String txtUser = editTextUserName.getText().toString().trim();
                String txtPass = editTextUserPass.getText().toString().trim();

                if(txtUser.equalsIgnoreCase("") || txtPass.equalsIgnoreCase(""))
                {

                    Toast.makeText(myContext, "Enter username & password", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    Cursor User = (dbGSTLink.getUser(txtUser, txtPass));
                    if(User!=null) {
                        if (User.getCount() > 0) {
                            if(User.moveToFirst()){
                                // onLocalAuthCompleted(true,code,User.getString(User.getColumnIndex("Name")));
                                promptGetToken(REQUESTED_API,User.getString(User.getColumnIndex("Name")));

                            }

                        }else {
                            Toast.makeText(myContext, "Invalid username & password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        })
        .show();
    }

    private void promptGetToken(int REQUESTED_API, String userName)
    {
        progressToken.show();
        String URL= Config.Base_URL_Azure+Config.GET_TOKEN_API;
        new TokenAsync_Frag(this, TokenAsync_Frag.HTTP_GET_TOKEN,userName,REQUESTED_API, URL, Header_TokenAuth).execute();
    }

    public void onClickGetGstr1Summary(View view) {
        /*progressDialog.show();
        new HTTPAsyncTask_Frag(this, HTTPAsyncTask.HTTP_GET,"",REQUEST_GET_GSTR1_SUMMARY, "https://tcd.blackboard.com/webapps/dur-browserCheck-BBLEARN/samples/sample.xlsx").execute();*/
        onHttpRequestComplete(REQUEST_GET_GSTR1_SUMMARY, "Helloe");
    }
    public void onClickGetGstr1(View view) {
        /*progressDialog.show();
        new HTTPAsyncTask_Frag(this, HTTPAsyncTask.HTTP_GET,"",REQUEST_GET_GSTR1_SUMMARY, "https://tcd.blackboard.com/webapps/dur-browserCheck-BBLEARN/samples/sample.xlsx").execute();*/
        onHttpRequestComplete(REQUEST_GET_GSTR1, "Helloe");
    }
    public void onClickGetGstr1A(View view) {
        /*progressDialog.show();
        new HTTPAsyncTask_Frag(this, HTTPAsyncTask.HTTP_GET,"",REQUEST_GET_GSTR1_SUMMARY, "https://tcd.blackboard.com/webapps/dur-browserCheck-BBLEARN/samples/sample.xlsx").execute();*/
        onHttpRequestComplete(REQUEST_GET_GSTR1A, "Helloe");
    }
    public void onClickGetGstr2A(View view) {
        /*progressDialog.show();
        new HTTPAsyncTask_Frag(this, HTTPAsyncTask.HTTP_GET,"",REQUEST_GET_GSTR1_SUMMARY, "https://tcd.blackboard.com/webapps/dur-browserCheck-BBLEARN/samples/sample.xlsx").execute();*/
        onHttpRequestComplete(REQUEST_GET_GSTR2A, "Helloe");
    }
    public void onClickGetGstr2Reconcile(View view) {
        /*progressDialog.show();
        new HTTPAsyncTask_Frag(this, HTTPAsyncTask.HTTP_GET,"",REQUEST_GET_GSTR1_SUMMARY, "https://tcd.blackboard.com/webapps/dur-browserCheck-BBLEARN/samples/sample.xlsx").execute();*/
        onHttpRequestComplete(REQUEST_GET_GSTR2_Reconcile, "Helloe");
    }
    public void onClickGetGstr3(View view) {
        /*progressDialog.show();
        new HTTPAsyncTask_Frag(this, HTTPAsyncTask.HTTP_GET,"",REQUEST_GET_GSTR1_SUMMARY, "https://tcd.blackboard.com/webapps/dur-browserCheck-BBLEARN/samples/sample.xlsx").execute();*/
        onHttpRequestComplete(REQUEST_GET_GSTR3, "Helloe");
    }

    /*public void onClickGetGstr1ASummary(View view) {
        progressDialog.show();
        new HTTPAsyncTask_Frag(this, HTTPAsyncTask.HTTP_GET,"",REQUEST_GET_GSTR1A_SUMMARY, Config.GSTR1A_SUMMERY_GET_API).execute();
    }*/



    private void saveGSTR1A(String userName) {

        String startDate = DateUtil.getDateForDatePicker(etReportDateStart.getText().toString()) ;
        String endDate = DateUtil.getDateForDatePicker(etReportDateEnd.getText().toString()) ;
        if(ConnectionDetector.isInternetConnection(myContext))
        {
            GSTR2B2BAItemDetails gstr2B2BAItemDetails = new GSTR2B2BAItemDetails("S","S1991",6210.99,0,0,37.4,8874614.44,33.41,5.68,null);
            ArrayList<GSTR2B2BAItemDetails> itm_detList = new ArrayList<GSTR2B2BAItemDetails>();
            itm_detList.add(gstr2B2BAItemDetails);
            GSTR1B2BAInvoiceItems items = new GSTR1B2BAInvoiceItems(1,"A",itm_detList);
            ArrayList<GSTR1B2BAInvoiceItems> itemsArrayList = new ArrayList<GSTR1B2BAInvoiceItems>();
            itemsArrayList.add(items);
            GSTR1AB2BSData gstr1AB2BSData = new GSTR1AB2BSData("AflJufPlFStqKBZ","A","98678","25-10-2016",776522.02,"01","N","Y",itemsArrayList);
            ArrayList<GSTR1AB2BSData> listData = new ArrayList<GSTR1AB2BSData>();
            listData.add(gstr1AB2BSData);
            GSTR1AData gstr1AData = new GSTR1AData(dbGSTLink.getGSTIN(),"062016",3782969.01,listData);
            GSTRData gstrData = new GSTRData(userName,dbGSTLink.getGSTIN(),gstr1AData);
            progressDialog.show();
            String strJson = GstJsonEncoder.getGSTRJsonEncode(gstrData);
            new HTTPAsyncTask_Frag(this, HTTPAsyncTask.HTTP_POST,strJson,REQUEST_SAVE_GSTR1A, Config.GSTR1A_URL,"").execute();
        }
        else
        {
            Toast.makeText(myContext, "No Internet Connection! Try again Later", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickFileGstr(View view) {
        //startActivity(new Intent(myContext,GSTFileActivity.class));
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.wepaspservices.com/"));
        startActivity(browserIntent);
    }


    public void onClickGetGstr2B2B(View view) {
        progressDialog.show();
        new HTTPAsyncTask_Frag(this, HTTPAsyncTask.HTTP_GET,"",REQUEST_GET_GSTR2_B2B, Config.GSTR2_B2B_GET_API,"").execute();
    }

    public void onClickPostGstr2(View view) {
        /*String startDate = DateUtil.getDateForDatePicker(etReportDateStart.getText().toString()) ;
        String endDate = DateUtil.getDateForDatePicker(etReportDateEnd.getText().toString()) ;*/
        String startDate = (etReportDateStart.getText().toString()) ;
        String endDate = (etReportDateEnd.getText().toString()) ;
        if(startDate.equalsIgnoreCase("") || endDate.equalsIgnoreCase(""))
        {
            disMiss();
            MsgBox.setMessage("Please select Date")
                    .setPositiveButton("OK", null)
                    .show();
        }
        else
        {
            promptAuthFragment(REQUEST_SAVE_GSTR2);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myActivity = null;
        myContext = null;
    }



        @Override
        public void onTokenRequestComplete(int API_REQUESTED, String respData, String userName) {
            progressToken.dismiss();
            if(respData== null || respData.equals("")|| respData.equals("Error"))
            {
                Toast.makeText(myContext, "Token Status : Failed", Toast.LENGTH_SHORT).show();
                Log.d("Token Status : ","Failed");
            }else
            {
                // successfully received token
                Log.d("Token Status : ","Received Successfully");

                ActionAfterTokenRequestCompleted(API_REQUESTED,respData, userName);
            }

        }

        @Override
        public void ActionAfterTokenRequestCompleted(int API_REQUESTED, String respData, String userName) {

            if(API_REQUESTED == REQUEST_SAVE_GSTR1)
            {
                postGSTR1Invoices(respData,userName);
            }
            else if(API_REQUESTED == REQUEST_SAVE_GSTR2)
            {
                PostGSTR2Invoices(respData,userName);
            }
        }

        @Override
        public void onFileDownloadComplete(boolean status, String filePath) {
            pDialog.dismiss();
            if(status)
            {
                Toast.makeText(myContext, "Download Complete "+filePath, Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(myContext, "Download Failed due to "+filePath, Toast.LENGTH_SHORT).show();
            }

        }


        //@Override
        public void onHttpRequestComplete(int requestCode, String data) {
            progressDialog.dismiss();
            if(data!=null) {
                if (requestCode == REQUEST_SAVE_GSTR1 || requestCode == REQUEST_SAVE_GSTR2) {
                    if (data.equalsIgnoreCase("")) {
                        Toast.makeText(myContext, "Error due to empty response", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            if (jsonObject.getBoolean("success")) {
                                //Toast.makeText(myContext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                MsgBox.Show("Success",jsonObject.getString("message"));
                            }
                            else
                            {
                                MsgBox.Show("Error",jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            Toast.makeText(myContext, "Error due to " + e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }
                else if (requestCode == REQUEST_GET_GSTR1_SUMMARY ||requestCode == REQUEST_GET_GSTR1 || requestCode == REQUEST_GET_GSTR1A ||
                        requestCode == REQUEST_GET_GSTR2A|| requestCode == REQUEST_GET_GSTR2_Reconcile ||
                         requestCode == REQUEST_GET_GSTR3)
                {
                    String URL = "https://tcd.blackboard.com/webapps/dur-browserCheck-BBLEARN/samples/sample.xlsx";
                    String Filename = "sample.xlsx";
                    String gstin = dbGSTLink.getGSTIN();

                    switch (requestCode) {
                        case REQUEST_GET_GSTR1_SUMMARY:
                            URL = "http://13.71.118.152/AndroWeb/API/GetGSTR1CSV?gstin="+gstin+"&fp=052017";
                            Filename = "GSTR1Summary.xlsx";
                            break;
                        case REQUEST_GET_GSTR1:
                            URL = "http://13.71.118.152/AndroWeb/API/GetGSTR1CSV?gstin="+gstin+"&fp=052017";
                            Filename = "GSTR1.xlsx";
                            break;
                        case REQUEST_GET_GSTR1A:
                            URL = "http://13.71.118.152/AndroWeb/API/GetGSTR1ACSV?gstin="+gstin+"&fp=052017";
                            Filename = "GSTR1A.xlsx";
                            break;
                        case REQUEST_GET_GSTR3:
                            URL = "http://13.71.118.152/AndroWeb/API/GetGSTR3CSV?gstin="+gstin+"&fp=052017";
                            Filename = "GSTR3.xlsx";
                            break;
                        case REQUEST_GET_GSTR2A:
                            URL = "http://13.71.118.152/AndroWeb/API/GetGSTR2ACSV?gstin="+gstin+"&fp=052017";
                            Filename = "GSTR2A.xlsx";
                            break;
                        case REQUEST_GET_GSTR2_Reconcile:
                            URL = "http://13.71.118.152/AndroWeb/API/GetReconciledCSV?gstin="+gstin;
                            Filename = "GSTR2_Reconcile.xlsx";
                            break;
                    }
                    File direct1 = new File(Environment.getExternalStorageDirectory()
                            + "/WeP_DownloadReports");

                    if (!direct1.exists()) {
                        direct1.mkdirs();
                    }
                    String dd = "";
                    Cursor cc = dbGSTLink.getCurrentDate();
                    if (cc != null && cc.moveToNext())
                        dd = cc.getString(cc.getColumnIndex("BusinessDate"));
                    File direct = new File(direct1 + "/" + dd.substring(3, 5) + dd.substring(6));
                    if (!direct.exists()) {
                        direct.mkdirs();
                    }

                    final DownloadManager dm = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(URL));
                    request.allowScanningByMediaScanner();
                    request.setDestinationInExternalPublicDir("/WeP_DownloadReports/"+dd.substring(3, 5) + dd.substring(6), Filename);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    final long enqueue = dm.enqueue(request);

                    BroadcastReceiver receiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            String action = intent.getAction();
                            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {

                                long downloadId = intent.getLongExtra(
                                        DownloadManager.EXTRA_DOWNLOAD_ID, 0);

                                DownloadManager.Query query = new DownloadManager.Query();
                                query.setFilterById(enqueue);
                                Cursor c = dm.query(query);
                                if (c.moveToFirst()) {
                                    int columnIndex = c
                                            .getColumnIndex(DownloadManager.COLUMN_STATUS);
                                    if (DownloadManager.STATUS_SUCCESSFUL == c
                                            .getInt(columnIndex)) {


                            /*ImageView view = (ImageView) findViewById(R.id.imageView1);
                            String uriString = c
                                    .getString(c
                                            .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            view.setImageURI(Uri.parse(uriString));*/
                                    }
                                }
                            }
                        }
                    };

                }


               /* else if(requestCode == REQUEST_GET_GSTR2_B2B) // REQUEST_GET_GSTR2_B2B
                {
                    //GetGSTR2B2BFinal getGSTR2B2BFinal = null;
                    ArrayList<GetGSTR2B2BFinal> finalsList = new ArrayList<GetGSTR2B2BFinal>();
                    data = data.replaceAll("\\\\", "");
                    data = data.substring(1,data.length()-1);
                    if(data.equalsIgnoreCase(""))
                    {
                        Toast.makeText(myContext, "Error due to empty response", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            JSONArray jsonArray = jsonObject.getJSONArray("b2b");
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                GetGSTR2B2BFinal getGSTR2B2BFinal = new GetGSTR2B2BFinal();
                                getGSTR2B2BFinal.setCtin(jsonObject1.getString("ctin"));
                                JSONArray jsonArrayInv = jsonObject1.getJSONArray("inv");
                                ArrayList<GetGSTR2B2BInvoice> getGSTR2B2BInvoicesList = new ArrayList<GetGSTR2B2BInvoice>();
                                for(int j=0;j<jsonArrayInv.length();j++)
                                {
                                    GetGSTR2B2BInvoice getGSTR2B2BInvoice = new GetGSTR2B2BInvoice();
                                    JSONObject jsonObjectInv = jsonArrayInv.getJSONObject(j);
                                    ArrayList<GetGSTR2B2BItem> itemsLis = new ArrayList<GetGSTR2B2BItem>();
                                    //some items like inum, idt
                                    getGSTR2B2BInvoice.setInum(jsonObjectInv.getString("inum"));
                                    getGSTR2B2BInvoice.setIdt(jsonObjectInv.getString("idt"));
                                    getGSTR2B2BInvoice.setVal(jsonObjectInv.getDouble("val"));
                                    getGSTR2B2BInvoice.setPos(jsonObjectInv.getString("pos"));
                                    getGSTR2B2BInvoice.setRchrg(jsonObjectInv.getString("rchrg"));
                                    getGSTR2B2BInvoice.setPro_ass(jsonObjectInv.getString("pro_ass"));

                                    JSONArray jsonArrayBillItems = jsonObjectInv.getJSONArray("itms");
                                    for(int k=0;k<jsonArrayBillItems.length();k++)
                                    {
                                        JSONObject jsonObjectItem = jsonArrayBillItems.getJSONObject(k);
                                        int lineNum = jsonObjectItem.getInt("num");
                                        JSONObject jsonObjectitm_det = jsonObjectItem.getJSONObject("itm_det");
                                        GetGSTR2B2BItem item = new GetGSTR2B2BItem(
                                                lineNum,
                                                jsonObjectitm_det.getString("ty"),
                                                jsonObjectitm_det.getString("hsn_sc"),
                                                jsonObjectitm_det.getDouble("txval"),
                                                jsonObjectitm_det.getDouble("irt"),
                                                jsonObjectitm_det.getDouble("iamt"),
                                                jsonObjectitm_det.getDouble("crt"),
                                                jsonObjectitm_det.getDouble("camt"),
                                                jsonObjectitm_det.getDouble("srt"),
                                                jsonObjectitm_det.getDouble("samt")
                                        );
                                        itemsLis.add(item);
                                    }
                                    getGSTR2B2BInvoice.setItems(itemsLis);
                                    getGSTR2B2BInvoicesList.add(getGSTR2B2BInvoice);
                                }
                                getGSTR2B2BFinal.setInvoicesList(getGSTR2B2BInvoicesList);
                                finalsList.add(getGSTR2B2BFinal);
                            }
                        } catch (Exception e) {
                            Toast.makeText(myContext, "Error due to "+e, Toast.LENGTH_SHORT).show();
                            finalsList = null;
                            e.printStackTrace();
                        }
                        // Add to db
                        dbGSTLink.addGSTR2B2BItems(finalsList);
                        Toast.makeText(myContext, "Data loaded successfully", Toast.LENGTH_SHORT).show();
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                }*/
            }else
            {
                Toast.makeText(myContext, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    //}

}
