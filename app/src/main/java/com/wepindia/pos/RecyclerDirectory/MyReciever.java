package com.wepindia.pos.RecyclerDirectory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by Administrator on 29-06-2017.
 */

public class MyReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Date d = new Date();
        Toast ToastMessage = Toast.makeText(context, d.getDate() + "" + d.getMonth(), Toast.LENGTH_SHORT);

        ToastMessage.setGravity(Gravity.RIGHT | Gravity.END | Gravity.TOP, 0, 0);
        ToastMessage.show();
    }
}