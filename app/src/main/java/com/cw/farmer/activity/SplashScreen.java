package com.cw.farmer.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.SubscriptionPlan;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cw.farmer.HandleConnectionAppCompatActivity;
import com.cw.farmer.R;
import com.cw.farmer.locationmanagerhelperclasses.SamplePresenter;
import com.cw.farmer.utils.GpsUtils;
import com.cw.farmer.locationmanagerhelperclasses.customlocation.base.LocationBaseActivity;
import com.cw.farmer.locationmanagerhelperclasses.customlocation.configuration.Configurations;
import com.cw.farmer.locationmanagerhelperclasses.customlocation.configuration.LocationConfiguration;
import com.cw.farmer.locationmanagerhelperclasses.customlocation.constants.FailType;
import com.cw.farmer.locationmanagerhelperclasses.customlocation.constants.ProcessType;

public class SplashScreen  extends LocationBaseActivity implements SamplePresenter.SampleView{

    private boolean isGPS = true;
    private ProgressDialog progressDialog;

    private SamplePresenter samplePresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_layout);
       // enableLocationTracking();


        samplePresenter = new SamplePresenter(this);
        getLocation();



        //TODO: SHOULD BE CHANGED TO A DIFFERENT LOCATION



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 9999) {

                isGPS = true; // flag maintain before get location
                if(isGPS)
                    moveToHomeActivity();

            }
        }
    }


    private void moveToHomeActivity(){

        SharedPreferences sharedPrefs = getSharedPreferences("PERMISSIONS", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed;
        if (!sharedPrefs.contains("userid")) {
            startActivity(new Intent(SplashScreen.this, LoginActivity.class));

        }else{
            startActivity(new Intent(SplashScreen.this, HomeActivity.class));
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        samplePresenter.destroy();
    }

    @Override
    public LocationConfiguration getLocationConfiguration() {
        return Configurations.defaultConfiguration("Gimme the permission!", "Would you mind to turn GPS on?");
    }

    @Override
    public void onLocationChanged(Location location) {
        samplePresenter.onLocationChanged(location);
    }

    @Override
    public void onLocationFailed(@FailType int failType) {
        samplePresenter.onLocationFailed(failType);
    }

    @Override
    public void onProcessTypeChanged(@ProcessType int processType) {
        samplePresenter.onProcessTypeChanged(processType);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getLocationManager().isWaitingForLocation()
                && !getLocationManager().isAnyDialogShowing()) {
            displayProgress();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        dismissProgress();
    }

    private void displayProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
            progressDialog.setMessage("Getting location...");
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private String locationText;

    @Override
    public String getText() {
        return locationText;
    }

    @Override
    public void setText(String text) {
        locationText = text;

        Log.d(this.getPackageName().toUpperCase(), "Splash Screen Location: " + locationText);
        if(locationText.contains("Couldn't get location, because network is not accessible!")){

            finish();
            startActivity(new Intent(SplashScreen.this, SplashScreen.class));

        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    //Its a good implementation but we used another
//                new GpsUtils(SplashScreen.this).turnGPSOn(new GpsUtils.onGpsListener() {
//                    @Override
//                    public void gpsStatus(boolean isGPSEnable) {
//                        // turn on GPS
//                        isGPS = isGPSEnable;
//                    }
//                });

                    if(isGPS)
                        moveToHomeActivity();




                }
            }, 2000);
        }



    }

    @Override
    public void updateProgress(String text) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setMessage(text);
        }
    }

    @Override
    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
