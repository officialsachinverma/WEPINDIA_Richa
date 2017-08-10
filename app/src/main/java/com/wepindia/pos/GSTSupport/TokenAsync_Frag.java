package com.wepindia.pos.GSTSupport;

import android.os.AsyncTask;
import android.util.Log;

import com.wep.gstcall.api.http.HTTPAsyncTask;
import com.wepindia.pos.fragments.FragmentGSTLink;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by RichaA on 6/27/2017.
 */

public class TokenAsync_Frag extends AsyncTask<Void,Void,String> {

    private static final String TAG = HTTPAsyncTask.class.getSimpleName();
    private final String USER_AGENT = "Mozilla/5.0";
    private FragmentGSTLink activity;
    private String userName;
    private int requestCode;
    private String url;
    private OnTokenRequestCompletedListener TokenRequestCompletedListener;
    public static int HTTP_GET = 1;
    public static int HTTP_GET_TOKEN = 2;
    public static int HTTP_POST = 3;
    private int method;
    private String Header;


    public TokenAsync_Frag(FragmentGSTLink activity, int method, String userName, int requestCode, String url, String Header)
    {
        this.activity = activity;
        this.userName = userName;
        this.requestCode = requestCode;
        this.url = url;
        this.method = method;
        TokenRequestCompletedListener = (OnTokenRequestCompletedListener) activity;
        this.Header = Header;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        String resp = "";
        if(this.method == HTTP_GET_TOKEN)
        {
            resp = sendHTTPGETData_TOKEN(url);
        }
        return resp;
    }

    @Override
    protected void onPostExecute(String resp) {
        super.onPostExecute(resp);
        TokenRequestCompletedListener.onTokenRequestComplete(requestCode,resp,userName);
    }

    public interface OnTokenRequestCompletedListener {
        void ActionAfterTokenRequestCompleted(int requestCode, String respData,String username);
        void onTokenRequestComplete(int requestCode , String respData, String userName);
    }


    private String sendHTTPGETData_TOKEN(String url) {
        String resp = null;
        try{
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            if(Header.length()>0)
            {
                String[] headerData=Header.split(",");
                for(String head : headerData)
                {
                    String[] prop = head.split("@");
                    con.setRequestProperty(prop[0], prop[1]);
                }
            }
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET Token ' request to URL : " + url);
            System.out.println("Token Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            if(responseCode == 200) {
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }
            //print result
            Log.d(TAG,response.toString());
            resp = response.toString();
        }catch (Exception e){
            Log.d(TAG,e.toString());
            e.printStackTrace();
        }
        return resp;
    }


}
