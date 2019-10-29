package com.cw.farmer.server;

import android.app.Application;
import android.content.Context;

public class AppContext extends Application {

    private static Context context;

    public static Context getAppContext() {
        return AppContext.context;
    }

    public void onCreate() {
        super.onCreate();
        AppContext.context = getApplicationContext();
    }
}