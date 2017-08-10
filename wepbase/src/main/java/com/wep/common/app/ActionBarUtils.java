package com.wep.common.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ALV on 17-09-2016.
 */
public class ActionBarUtils {


    public static void goBack(final Activity activity, View view) {
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                activity.finish();
            }
        });
    }

    public static void setTitle(View view, final String txt) {
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TextView textView = (TextView) view;
                textView.setText(txt);
            }
        });
    }

    public static void takeScreenshot(final Activity activity, final View view1) {
        String dd = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss").format(new Date());


        try {
            String path = "";
            if (Environment.getExternalStorageDirectory().exists() == false) {
                path = Environment.getRootDirectory().getPath();
            }else {
                path = Environment.getExternalStorageDirectory().toString();
            }
            path = path+"/WeP_ScreenShots/";
            File screenPath = new File(path);
            if(screenPath.exists() == false)
                screenPath.mkdir();

            // image naming and path  to include sd card  appending name you choose for file
            String mPath = path + "/" + "Screenshot_" +dd+ ".jpg";

            // create bitmap screen capture
            View v1 = view1;//activity.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            //openScreenshot(activity, imageFile);
            Toast.makeText(activity, "Screenshot Saved Successfully", Toast.LENGTH_SHORT).show();
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
            Toast.makeText(activity, "dg"+e, Toast.LENGTH_SHORT).show();
        }
    }

    public static void openScreenshot(Activity activity, File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        activity.startActivity(intent);
    }

    public static void setupToolbar(Activity activity, Toolbar toolbar, ActionBar actionBar,String title,String userName,String time){
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setTitle(title);
        TextView textViewCenter = (TextView) toolbar.findViewById(R.id.textViewCenter);
        textViewCenter.setText(title);
        TextView textViewLeft = (TextView) toolbar.findViewById(R.id.textViewLeft);
        textViewLeft.setText(userName.toUpperCase()+" "+time);
    }

    public static void setupToolbarMenu(Activity activity, Toolbar toolbar, ActionBar actionBar,String title,String userName,String time){
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setTitle(title);
        TextView textViewCenter = (TextView) toolbar.findViewById(R.id.textViewCenter);
        textViewCenter.setText(title);
        TextView textViewLeft = (TextView) toolbar.findViewById(R.id.textViewLeft);
        textViewLeft.setText(userName.toUpperCase()+" "+time);
    }
}
