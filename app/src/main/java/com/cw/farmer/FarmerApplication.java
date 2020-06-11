package com.cw.farmer;

import android.app.Application;

import com.orm.SugarApp;
import com.yayandroid.locationmanager.LocationManager;

public class FarmerApplication extends SugarApp {

    @Override
    public void onCreate() {
        super.onCreate();
       LocationManager.enableLog(false);
    }
}
