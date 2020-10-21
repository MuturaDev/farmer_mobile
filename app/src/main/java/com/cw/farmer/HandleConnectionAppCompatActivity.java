package com.cw.farmer;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.cw.farmer.activity.FarmerRecruitActivity;
import com.cw.farmer.activity.HomeActivity;
import com.cw.farmer.activity.SplashScreen;
import com.cw.farmer.locationmanagerhelperclasses.SamplePresenter;
import com.cw.farmer.locationmanagerhelperclasses.customlocation.base.LocationBaseActivity;
import com.cw.farmer.locationmanagerhelperclasses.customlocation.configuration.Configurations;
import com.cw.farmer.locationmanagerhelperclasses.customlocation.configuration.LocationConfiguration;
import com.cw.farmer.locationmanagerhelperclasses.customlocation.constants.FailType;
import com.cw.farmer.locationmanagerhelperclasses.customlocation.constants.ProcessType;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.jakewharton.processphoenix.ProcessPhoenix;


import org.imaginativeworld.oopsnointernet.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.NoInternetDialog;
import org.imaginativeworld.oopsnointernet.NoInternetSnackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HandleConnectionAppCompatActivity extends LocationBaseActivity implements SamplePresenter.SampleView {


    //Location Tracking
    private ProgressDialog progressDialog;
    private SamplePresenter samplePresenter;



    protected  String location_str, coordinates;
    protected String locationText = null;
    protected void enableLocationTracking(){//will use this method as an aid for FusedLocationProviderClient

        samplePresenter = new SamplePresenter(this);
        getLocation();

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(samplePresenter != null)
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



    private void displayProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Getting location...");
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public String getText() {
        return locationText == null ? "" : locationText;
    }

    @Override
    public void setText(String text) {
        if(locationText == null)
        locationText = text.replace("\n","").replace(" ","");
        Log.d(this.getPackageName().toUpperCase(), "Location: " + locationText);
        if(locationText.contains("Couldn't get location") || locationText.contains("Couldn'tgetlocation")){


          final  SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("");
            pDialog.setContentText("Sorry, but will be restarting the app shortly to get your location");
            pDialog.setCancelable(false);
            pDialog.show();

//            Snackbar.with(getApplicationContext(),null)
//                    .type(Type.SUCCESS)
//                    .message("Sorry, but will be restarting the app shortly to get location.")
//                    .duration(Duration.LONG)
//                    .fillParent(true)
//                    .textAlign(Align.LEFT)
//                    .show();


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pDialog.dismissWithAnimation();
                }
            }, 6000 );


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    Intent nextIntent = new Intent(getApplicationContext(), SplashScreen.class);
                            ProcessPhoenix.triggerRebirth(getApplicationContext(), nextIntent);

                            //this is restarting on the wrong screen
                   // ProcessPhoenix.triggerRebirth(getApplicationContext());//getLocation();
                }
            }, 7500 );
        }else
            if(locationText != null){



            String[] strings = locationText.split(",");



            double   currentLat = Double.valueOf(strings[0]);
            double   currentLong = Double.valueOf(strings[1]);
            coordinates = currentLat+","+currentLong;


            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(HandleConnectionAppCompatActivity.this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(currentLat, currentLong, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                //Toast.makeText(FarmerRecruitActivity.this, "lat " + city + "\nlong " + address, Toast.LENGTH_LONG).show();
                location_str = address;



            } catch (IOException e) {
                e.printStackTrace();
            }

//            FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//            mFusedLocationClient.getLastLocation()
//                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            // Got last known location. In some rare situations this can be null.
//                            // GPS location can be null if GPS is switched off
//
//
//
//                            double   currentLat = location.getLatitude();
//                            double   currentLong = location.getLongitude();
//                            coordinates = currentLat+","+currentLong;
//
//
//                            Geocoder geocoder;
//                            List<Address> addresses;
//                            geocoder = new Geocoder(HandleConnectionAppCompatActivity.this, Locale.getDefault());
//
//                            try {
//                                addresses = geocoder.getFromLocation(currentLat, currentLong, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                                String city = addresses.get(0).getLocality();
//                                String state = addresses.get(0).getAdminArea();
//                                String country = addresses.get(0).getCountryName();
//                                String postalCode = addresses.get(0).getPostalCode();
//                                String knownName = addresses.get(0).getFeatureName();
//                                //Toast.makeText(FarmerRecruitActivity.this, "lat " + city + "\nlong " + address, Toast.LENGTH_LONG).show();
//                                location_str = address;
//
//
//
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//
//                        }
//                    })
//                    .addOnFailureListener(this, new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//
//                        }
//                    });
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


    @Override
    protected void onResume() {
        super.onResume();
        // Location
        if (getLocationManager().isWaitingForLocation()
                && !getLocationManager().isAnyDialogShowing()) {
            displayProgress();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Location
        dismissProgress();
    }

}
