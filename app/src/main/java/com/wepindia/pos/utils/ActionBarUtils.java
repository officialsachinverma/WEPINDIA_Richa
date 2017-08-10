package com.wepindia.pos.utils;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.net.Uri;
        import android.os.Environment;
        import android.provider.MediaStore;
        import android.text.format.DateFormat;
        import android.util.Log;
        import android.view.View;
        import android.widget.Toast;

        import com.wepindia.pos.HomeActivity;

        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.util.Date;
        import java.util.Random;

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

    public static void goHome(final Activity activity, View view) {
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(activity, HomeActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    public static void navigateHome(final Activity activity) {
        Intent intent = new Intent(activity, HomeActivity.class);
        activity.startActivity(intent);
        activity.finishAffinity();
    }

    public static void takeScreenshot(final Activity activity, View view, final View view1) {
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Bitmap bitmap = takeScreenshot(activity);
                saveBitmap(bitmap,activity);
            }
        });
    }

    public static Bitmap takeScreenshot(Activity activity) {
        View rootView = activity.findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public static void saveBitmap(Bitmap bitmap, Context context) {
        Date d = new Date();
        CharSequence date  = DateFormat.format("yyyy-MM-dd_HH:mm:ss", d.getTime());
        Random r=new Random();
        int c=r.nextInt(1000000);
        File imagePath = new File(Environment.getExternalStorageDirectory() + "/" + date + "_screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            MediaStore.Images.Media.insertImage(context.getContentResolver(),bitmap,""+c,""+c);
            fos.flush();
            fos.close();

            Toast.makeText(context, "Screenshot taken Successfully", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }
}
