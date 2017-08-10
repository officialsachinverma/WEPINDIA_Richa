package com.wep.moduleactivate;

import android.app.Application;
import android.content.Context;

/**
 * Created by PriyabratP on 16-03-2017.
 */

public class AppController extends Application {

    private static AppController instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static AppController getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}
