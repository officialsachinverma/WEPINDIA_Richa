package com.wep.gstcall.api.http;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by PriyabratP on 17-11-2016.
 */

public class HTTPAsyncTask extends AsyncTask<Void,Void,String> {

    private static final String TAG = HTTPAsyncTask.class.getSimpleName();
    private final String USER_AGENT = "Mozilla/5.0";
    private Activity activity;
    private String strJson;
    private int requestCode;
    private String url;
    private OnHTTPRequestCompletedListener httpRequestCompletedListener;
    public static int HTTP_GET = 1;
    public static int HTTP_POST = 2;
    private int method;

    public HTTPAsyncTask(Activity activity,int method, String strJson,int requestCode,String url)
    {
        this.activity = activity;
        this.strJson = strJson;
        this.requestCode = requestCode;
        this.url = url;
        this.method = method;
        httpRequestCompletedListener = (OnHTTPRequestCompletedListener) activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        String resp = "";
        if(this.method == HTTP_GET)
        {
            resp = sendHTTPGETData(url);
        }
        else
        {
            resp = sendHTTPData(url,strJson);
        }
        return resp;
    }

    @Override
    protected void onPostExecute(String resp) {
        super.onPostExecute(resp);
        httpRequestCompletedListener.onHttpRequestComplete(requestCode,resp);
    }

    public interface OnHTTPRequestCompletedListener {
        void onHttpRequestComplete(int requestCode,String filePath);
    }

    public String sendHTTPData(String urlpath, String jsonData) {
        String resp = null;
        HttpURLConnection connection = null;
        try {
            URL url=new URL(urlpath);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());
            streamWriter.write(jsonData.toString());
            streamWriter.flush();
            StringBuilder stringBuilder = new StringBuilder();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                String response = null;
                while ((response = bufferedReader.readLine()) != null) {
                    stringBuilder.append(response + "\n");
                }
                bufferedReader.close();

                Log.d("test", stringBuilder.toString());
                resp =  stringBuilder.toString();
                Log.d(TAG,resp.toString());
                Log.d(TAG,jsonData);
            } else {
                Log.e("test", connection.getResponseMessage());
                resp =  null;
            }
        } catch (Exception exception){
            Log.e(TAG, exception.toString());
            resp = null;
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return resp;
    }

    private String sendHTTPGETData(String url) {
        String resp = null;
        try{
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            //print result
            Log.d(TAG,response.toString());
            resp = response.toString();
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }
        return resp;
    }
}
