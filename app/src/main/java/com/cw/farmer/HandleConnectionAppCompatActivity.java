package com.cw.farmer;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cw.farmer.activity.FarmerRecruitActivity;
import com.cw.farmer.locationmanagerhelperclasses.SamplePresenter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.yayandroid.locationmanager.base.LocationBaseActivity;
import com.yayandroid.locationmanager.configuration.Configurations;
import com.yayandroid.locationmanager.configuration.LocationConfiguration;
import com.yayandroid.locationmanager.constants.FailType;
import com.yayandroid.locationmanager.constants.ProcessType;

import org.imaginativeworld.oopsnointernet.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.NoInternetDialog;
import org.imaginativeworld.oopsnointernet.NoInternetSnackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
        locationText = text;
        Log.d(this.getPackageName().toUpperCase(), "Location: " + locationText);
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



    // No Internet Dialog
    private NoInternetDialog noInternetDialog;

    // No Internet Snackbar
    private NoInternetSnackbar noInternetSnackbar;







    @Override
    protected void onResume() {
        super.onResume();

        // No Internet Dialog
        NoInternetDialog.Builder builder1 = new NoInternetDialog.Builder(this);

        builder1.setConnectionCallback(new ConnectionCallback() { // Optional
            @Override
            public void hasActiveConnection(boolean hasActiveConnection) {
                // ...
                //Snackbar.make()
//

            }
        });
        builder1.setCancelable(false); // Optional
        builder1.setNoInternetConnectionTitle("No Internet"); // Optional
        builder1.setNoInternetConnectionMessage("Check your Internet connection and try again"); // Optional
        builder1.setShowInternetOnButtons(true); // Optional
        builder1.setPleaseTurnOnText("Please turn on"); // Optional
        builder1.setWifiOnButtonText("Wifi"); // Optional
        builder1.setMobileDataOnButtonText("Mobile data"); // Optional

        builder1.setOnAirplaneModeTitle("No Internet"); // Optional
        builder1.setOnAirplaneModeMessage("You have turned on the airplane mode."); // Optional
        builder1.setPleaseTurnOffText("Please turn off"); // Optional
        builder1.setAirplaneModeOffButtonText("Airplane mode"); // Optional
        builder1.setShowAirplaneModeOffButtons(true); // Optional

        noInternetDialog = builder1.build();


        // No Internet Snackbar
//        NoInternetSnackbar.Builder builder2 = new NoInternetSnackbar.Builder(this, (ViewGroup) findViewById(android.R.id.content));
//
//        builder2.setConnectionCallback(new ConnectionCallback() { // Optional
//            @Override
//            public void hasActiveConnection(boolean hasActiveConnection) {
//                // ...
//            }
//        });
//        builder2.setIndefinite(true); // Optional
//        builder2.setNoInternetConnectionMessage("No active Internet connection!"); // Optional
//        builder2.setOnAirplaneModeMessage("You have turned on the airplane mode!"); // Optional
//        builder2.setSnackbarActionText("Settings");
//        builder2.setShowActionToDismiss(false);
//        builder2.setSnackbarDismissActionText("OK");

        //noInternetSnackbar = builder2.build();


        // Location
        if (getLocationManager().isWaitingForLocation()
                && !getLocationManager().isAnyDialogShowing()) {
            displayProgress();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // No Internet Dialog
        if (noInternetDialog != null) {
            noInternetDialog.destroy();
        }

        // No Internet Snackbar
        if (noInternetSnackbar != null) {
            noInternetSnackbar.destroy();
        }


        // Location
        dismissProgress();
    }

}
