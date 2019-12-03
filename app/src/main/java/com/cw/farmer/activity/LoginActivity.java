package com.cw.farmer.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cw.farmer.R;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.CentreId;
import com.cw.farmer.model.Result;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_username, et_password;
    private Button btn_login;
    private ProgressDialog progressBar;
    private TextView errorview;

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    private void init() {
        progressBar =new ProgressDialog(this);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        errorview=findViewById(R.id.login_error_view);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
            if (isValid()){
                login();

            }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION) // ask single or multiple permission once
                .subscribe(granted -> {
                    if (granted) {
                        // All requested permissions are granted
                    } else {
                        // At least one permission is denied
                    }
                });
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        // GPS location can be null if GPS is switched off
                        SharedPreferences.Editor editor = getSharedPreferences("location", MODE_PRIVATE).edit();
                        String latitude = location + "";
                        Double latitude_final;
                        String longitude = location + "";
                        Double longitude_final;
                        System.out.println(latitude);
                        if (location == null) {
                            latitude_final = 12345678.3456;
                        } else {
                            latitude_final = location.getLatitude();
                        }
                        if (location == null) {
                            longitude_final = 12345678.3456;
                        } else {
                            longitude_final = location.getLongitude();
                        }
                        double currentLat = latitude_final;
                        double currentLong = longitude_final;
                        String coordinates = currentLat + "," + currentLong;
                        Log.d(TAG, "1 coordinates" + coordinates);
                        editor.putString("coordinates", coordinates);

                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(LoginActivity.this, Locale.getDefault());

                        try {
                            addresses = geocoder.getFromLocation(currentLat, currentLong, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();
                            //Toast.makeText(FarmerRecruitActivity.this, "lat " + city + "\nlong " + address, Toast.LENGTH_LONG).show();
                            Log.d(TAG, "1 location" + address);
                            editor.putString("location_str", address);
                            editor.apply();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private boolean isValid() {
        if (et_username.getText().length() == 0 || et_username.getText().toString().equals("") || et_username.getText().equals(null)) {
            et_username.setError("Can Not Empty");
            return false;
        } else if (et_password.getText().length() == 0 || et_password.getText().toString().equals("") || et_password.getText().equals(null)) {
            et_password.setError("Can Not Empty");
            return false;
        }
        return true;
    }

    private void login() {

        //progressBar.setCancelable(false);
       // progressBar.setMessage("Please Wait...");
        //progressBar.show();
        SharedPreferences prefs = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
        if (prefs.getString("username", "aggrey1234").equals(et_username.getText().toString()) && prefs.getString("password", "woow").equals(et_password.getText().toString())) {

            startActivity(new Intent(LoginActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

        }

        Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
        APIService service =retrofit.create(APIService.class);
        String username=et_username.getText().toString();
        String password =et_password.getText().toString();
        Call<Result> call = service.userLogin(username,password);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                //progressBar.hide();

                try {
                    if (response.code() == 200) {

                        //Utility.showToast(LoginActivity.this,response.body().getPermissions());

                        //Set the values
                        String center_idss = "";
                        String center_names = "";
                        for (CentreId elem : response.body().getCentreId()) {
                            center_idss = elem.getCenterId() + "," + center_idss;
                            center_names = elem.getCentreName() + "," + center_names;
                        }
                        center_idss = removeLastChar(center_idss);
                        center_names = removeLastChar(center_names);
                        SharedPreferences mEdit1 = getSharedPreferences("PERMISSIONS", Context.MODE_PRIVATE);
                        SharedPreferences.Editor scoreEditor = mEdit1.edit();
                        scoreEditor.putString("userid", center_idss + "");
                        scoreEditor.putString("username", et_username.getText().toString());
                        scoreEditor.putString("password", et_password.getText().toString());
                        scoreEditor.putString("enter", "yes");
                        scoreEditor.putString("center_names", center_names);
                        scoreEditor.putString("center_ids", center_idss);
                        scoreEditor.putString("auth_key", "Basic " + response.body().getBase64EncodedAuthenticationKey());
                        startActivity(new Intent(LoginActivity.this, Home2Activity.class));


                        Set<String> set = new HashSet<String>();
                        set.addAll(response.body().getPermissions());
                        scoreEditor.putStringSet("key", set);
                        scoreEditor.commit();

                        //Utility.showToast(LoginActivity.this,center_idss);


                    } else {
                        errorview.setText("Wrong Username or Password");
                    }
                }catch (Exception e){

                    //Utility.showToast(LoginActivity.this,e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                //progressBar.hide();
                Utility.showToast(LoginActivity.this,t.getMessage());
            }
        });


    }
}
