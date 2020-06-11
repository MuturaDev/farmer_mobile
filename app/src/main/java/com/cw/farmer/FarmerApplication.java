package com.cw.farmer.locationmanagerhelperclasses;

import android.app.Application;

import com.yayandroid.locationmanager.LocationManager;

public class FarmerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LocationManager.enableLog(true);
    }
}
