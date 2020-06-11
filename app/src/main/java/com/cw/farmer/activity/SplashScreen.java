package com.cw.farmer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cw.farmer.R;
import com.cw.farmer.utils.GpsUtils;

public class SplashScreen extends AppCompatActivity {

    private boolean isGPS = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_layout);



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
        }, 4000);


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
        startActivity(new Intent(SplashScreen.this, HomeActivity.class));
        finish();
    }
}
