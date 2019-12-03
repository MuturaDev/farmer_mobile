package com.cw.farmer.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.cw.farmer.NetworkUtil;
import com.cw.farmer.R;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.AllResponse;
import com.cw.farmer.model.PageItemsSprayNumber;
import com.cw.farmer.model.SprayNumberDB;
import com.cw.farmer.model.SprayNumbersResponse;
import com.cw.farmer.model.SprayPostDB;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SprayConfirmationActivity extends AppCompatActivity {
    EditText farmer, noofunits, cropdate;
    Spinner spraynumber, section, farmerspinner;
    String location_str, coordinates;
    Double currentLat, currentLong;
    ProgressDialog progressDialog;
    String farmer_id_string;
    List<Integer> crop_id;
    private RadioGroup radioland;
    private RadioButton radioSexButton;
    private TextView location, latLong, diff;
    private Double lati, longi;

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spray_confirmation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);
        cropdate = findViewById(R.id.cropdate);
        spraynumber = findViewById(R.id.spraynumber);
        noofunits = findViewById(R.id.noofunits);
        farmer = findViewById(R.id.farmer);
        farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SprayConfirmationActivity.this, SearchSprayActivity.class);
                startActivity(intent);
            }
        });

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            String name = (String) b.get("name");
            farmer.setText(name);

            String id = (String) b.get("id");
            farmer_id_string = id;

            String cropdate_gre = (String) b.get("cropdate");
            cropdate.setText(cropdate_gre);

            String units_gre = (String) b.get("units");
            noofunits.setText(units_gre);

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayList<String> spinnerArray = new ArrayList<String>();
        spinnerArray.clear();
        crop_id = new ArrayList<Integer>();
        List<SprayNumberDB> sprays = SprayNumberDB.listAll(SprayNumberDB.class);
        for (SprayNumberDB spray : sprays) {
            spinnerArray.add(spray.spray_no);
            crop_id.add(spray.id_spray);
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(SprayConfirmationActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spraynumber.setAdapter(spinnerArrayAdapter);
        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
            progressDialog.setCancelable(false);
            // progressBar.setMessage("Please Wait...");
            progressDialog.show();
            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
            APIService service = retrofit.create(APIService.class);
            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
            Call<SprayNumbersResponse> call = service.getspraynumber(auth_key);
            call.enqueue(new Callback<SprayNumbersResponse>() {
                @Override
                public void onResponse(Call<SprayNumbersResponse> call, Response<SprayNumbersResponse> response) {
                    progressDialog.hide();
                    try {
                        if (response.body().getPageItemsSprayNumbers().size() != 0) {
                            ArrayList<String> spinnerArray = new ArrayList<String>();
                            spinnerArray.clear();
                            crop_id = new ArrayList<Integer>();
                            SprayNumberDB.deleteAll(SprayNumberDB.class);
                            for (PageItemsSprayNumber blacklist : response.body().getPageItemsSprayNumbers()) {

                                SprayNumberDB book = new SprayNumberDB(blacklist.getId(), blacklist.getSprayno());
                                book.save();

                                spinnerArray.add(blacklist.getSprayno());
                                crop_id.add(blacklist.getId());
                            }
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(SprayConfirmationActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                            spraynumber.setAdapter(spinnerArrayAdapter);

                        } else {

                            Toast.makeText(SprayConfirmationActivity.this, "Spray numbers not found", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Utility.showToast(SprayConfirmationActivity.this, e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<SprayNumbersResponse> call, Throwable t) {
                    progressDialog.hide();
                    Utility.showToast(SprayConfirmationActivity.this, t.getMessage());
                }
            });
        }
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


                        currentLat = location.getLatitude();
                        currentLong = location.getLongitude();
                        coordinates = currentLat + "," + currentLong;
                        Log.d(TAG, "1 coordinates" + coordinates);
                        editor.putString("coordinates", coordinates);

                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(SprayConfirmationActivity.this, Locale.getDefault());

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
                            Log.d(TAG, "1 location_str" + location_str);
                            editor.putString("location_str", location_str);
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

    public boolean validate() {
        boolean valid = true;
        int errorColor;
        final int version = Build.VERSION.SDK_INT;

        //Get the defined errorColor from color resource.
        if (version >= 23) {
            errorColor = ContextCompat.getColor(getApplicationContext(), R.color.errorColor);
        } else {
            errorColor = getResources().getColor(R.color.errorColor);
        }

        if (cropdate.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("must input crop date");
            ssbuilder.setSpan(fgcspan, 0, "must input crop date".length(), 0);
            noofunits.setError(ssbuilder);
            valid = false;
        }
        if (spraynumber == null && spraynumber.getSelectedItem() == null) {

            TextView errorText = (TextView) spraynumber.getSelectedView();
            errorText.setError("must input spray number");
            errorText.setTextColor(errorColor);//just to highlight that this is an error

            valid = false;
        }
        if (noofunits.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("must input no of units");
            ssbuilder.setSpan(fgcspan, 0, "must input no of units".length(), 0);
            noofunits.setError(ssbuilder);
            valid = false;
        } else {
            noofunits.setError(null);
        }

        return valid;
    }

    public void submit(View v) {
        if (!validate()) {
            Snackbar.make(v, "Correct the above errors first", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        Log.d(TAG, "2 location_str" + location_str);
        Log.d(TAG, "2 coordinates" + coordinates);

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Recruiting...");
        pDialog.setCancelable(false);
        pDialog.show();
        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("verificationid", farmer_id_string);
            hashMap.put("cordinates", coordinates);
            hashMap.put("location", location_str);
            hashMap.put("sprayconfirmed", "Y");
            hashMap.put("programid", crop_id.get(spraynumber.getSelectedItemPosition()).toString());

            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
            APIService service = retrofit.create(APIService.class);
            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
            Call<AllResponse> call = service.spraypost(auth_key, hashMap);
            call.enqueue(new Callback<AllResponse>() {
                @Override
                public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                    progressDialog.hide();
                    try {
                        if (response.body() != null) {
                            pDialog.dismissWithAnimation();
                            new SweetAlertDialog(SprayConfirmationActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success")
                                    .setContentText("You have successfully submitted farmer spray confirmation details")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            startActivity(new Intent(SprayConfirmationActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                        }
                                    })
                                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            startActivity(new Intent(SprayConfirmationActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                        }
                                    })
                                    .show();

                        } else {
                            pDialog.dismissWithAnimation();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            new SweetAlertDialog(SprayConfirmationActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Ooops...")
                                    .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                    .show();


                        }
                    } catch (Exception e) {
                        pDialog.dismissWithAnimation();
                        new SweetAlertDialog(SprayConfirmationActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText(e.getMessage())
                                .show();
                    }
                    //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(Call<AllResponse> call, Throwable t) {
                    pDialog.dismissWithAnimation();
                    new SweetAlertDialog(SprayConfirmationActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(t.getMessage())
                            .show();
                }
            });
        } else {
            SprayPostDB book = new SprayPostDB(farmer_id_string, coordinates, location_str, noofunits.getText().toString().trim(), "Y", crop_id.get(spraynumber.getSelectedItemPosition()).toString());
            book.save();
            progressDialog.hide();
            new SweetAlertDialog(SprayConfirmationActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("No Wrong")
                    .setContentText("We have saved the data offline, We will submitted it when you have internet")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            startActivity(new Intent(SprayConfirmationActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                        }
                    })
                    .show();
        }

    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(SprayConfirmationActivity.this, Locale.getDefault());
        String location = " ";
        String add = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            add = obj.getAddressLine(0);
            add = add + "\n" + obj.getLocality();

            Log.v("IGA", "Address" + add);
            location = add;

            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return location;

    }




}
