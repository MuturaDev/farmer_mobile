package com.cw.farmer;


import com.orm.SugarApp;
import com.cw.farmer.locationmanagerhelperclasses.customlocation.LocationManager;

public class FarmerApplication extends SugarApp {
    @Override
    public void onCreate() {
        super.onCreate();
       LocationManager.enableLog(false);

//        AutoErrorReporter.get(this)
//                .setEmailAddresses("jamesgituma9961@gmail.com")
//                .setEmailSubject("Farmer App Crash Reporter")
//                .start();


    }
}
