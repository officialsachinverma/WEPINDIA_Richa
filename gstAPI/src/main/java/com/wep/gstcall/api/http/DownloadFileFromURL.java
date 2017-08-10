package com.wep.gstcall.api.http;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.wep.gstcall.api.util.Config;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by PriyabratP on 24-11-2016.
 */

public class DownloadFileFromURL extends AsyncTask<String, String, String> {

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     * */

    private String downLoadUrl;
    private String path;
    private ProgressDialog progressDialog;
    private Activity activity;
    private OnFileDownloadCompletedListener onFileDownloadCompletedListener;

    public DownloadFileFromURL(Activity activity,ProgressDialog progressDialog,String type,String downLoadUrl){
        this.activity = activity;
        this.onFileDownloadCompletedListener = (OnFileDownloadCompletedListener) activity;
        this.progressDialog = progressDialog;
        this.downLoadUrl = downLoadUrl;
        Date date = new Date();
        String modifiedDate= new SimpleDateFormat("yyyy-MM-dd hh-mm").format(date);
        this.path = Config.FILE_PATH + "/"+type+"_"+modifiedDate+".xlsx";
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        String status  = "";
        try {
            URL url = new URL(downLoadUrl);
            URLConnection conection = url.openConnection();
            conection.connect();
            // getting file length
            int lenghtOfFile = conection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file
            OutputStream output = new FileOutputStream(this.path/*"/sdcard/downloadedfile.jpg"*/);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress(""+(int)((total*100)/lenghtOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();
            status = "";
        } catch (Exception e) {
            status  = e.toString();
            Log.e("Error: ", e.getMessage());
        }
        return status;
    }

    /**
     * Updating progress bar
     * */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        progressDialog.setProgress(Integer.parseInt(progress[0]));
    }

    /**
     * After completing background task
     * Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {
        //String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
        if(file_url.equalsIgnoreCase("")){
            onFileDownloadCompletedListener.onFileDownloadComplete(true,this.path);
        }else {
            onFileDownloadCompletedListener.onFileDownloadComplete(true,file_url);
        }
    }

    public interface OnFileDownloadCompletedListener {
        void onFileDownloadComplete(boolean status,String filePath);
    }
}
