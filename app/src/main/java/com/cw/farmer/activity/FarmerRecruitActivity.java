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
import com.cw.farmer.model.AllResponse;
import com.cw.farmer.model.CropDateDB;
import com.cw.farmer.model.CropDateResponse;
import com.cw.farmer.model.PageItem;
import com.cw.farmer.model.RecruitFarmerDB;
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


public class FarmerRecruitActivity extends AppCompatActivity {
    EditText farmer, noofunits;
    Spinner cropdate, section, farmerspinner;
    private RadioGroup radioland;
    private RadioButton radioSexButton;
    private TextView location, latLong, diff;
    private Double lati, longi;
    String location_str, coordinates;
    Double currentLat,currentLong;
    ProgressDialog progressDialog;
    String farmer_id_string;
    List<Integer> crop_id;
    List<Integer> farmer_id;
    ArrayList<PageItem> pageItemArrayList;
    ArrayList<PageItem> array_sort;
    private int timeInterval = 3000;
    private int fastestTimeInterval = 3000;
    private boolean runAsBackgroundService = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_recruit);
        progressDialog = new ProgressDialog(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cropdate = findViewById(R.id.cropdate);
        section = findViewById(R.id.section);
        noofunits = findViewById(R.id.noofunits);
        radioland = findViewById(R.id.radioland);
        farmer = findViewById(R.id.farmer);
        farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor searcheditor = getSharedPreferences("search", MODE_PRIVATE).edit();
                searcheditor.putString("activity", "recruit");
                searcheditor.apply();

                SharedPreferences.Editor editor = getSharedPreferences("recruit_farmer", MODE_PRIVATE).edit();
                if (!cropdate.getSelectedItem().toString().equals("")) {
                    editor.putString("cropdate", cropdate.getSelectedItem().toString());
                }

                if (!section.getSelectedItem().toString().equals("")) {
                    editor.putString("section", section.getSelectedItem().toString());
                }

                if (!noofunits.getText().toString().equals("")) {
                    editor.putString("noofunits", noofunits.getText().toString());
                }

                editor.apply();

                Intent intent = new Intent(FarmerRecruitActivity.this, SearchFarmerActivity.class);
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

        }
        SharedPreferences prefs = getSharedPreferences("recruit_farmer", MODE_PRIVATE);
        noofunits.setText(prefs.getString("noofunits", " "));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayList<String> spinnerArray = new ArrayList<String>();
        spinnerArray.clear();
        crop_id = new ArrayList<Integer>();
        List<CropDateDB> crops = CropDateDB.listAll(CropDateDB.class);
        for (CropDateDB crop : crops) {
            spinnerArray.add(removeLastChar(crop.plantingDate) + "(" + crop.prodId + ")");
            crop_id.add(crop.id_crop);
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(FarmerRecruitActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        cropdate.setAdapter(spinnerArrayAdapter);
        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
            progressDialog.setCancelable(false);
            progressDialog.dismiss();
            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
            APIService service = retrofit.create(APIService.class);
            SharedPreferences prefs123 = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
            String auth_key = prefs123.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
            Call<List<CropDateResponse>> call = service.getRecruitCropDates(auth_key);
            call.enqueue(new Callback<List<CropDateResponse>>() {
                @Override
                public void onResponse(Call<List<CropDateResponse>> call, Response<List<CropDateResponse>> response) {
                    progressDialog.hide();
                    ArrayList<String> spinnerArray = new ArrayList<String>();
                    spinnerArray.clear();
                    crop_id = new ArrayList<Integer>();
                    CropDateDB.deleteAll(CropDateDB.class);
                    for (CropDateResponse blacklist : response.body()) {
                        String date = "";
                        for (int elem : blacklist.getPlantingDate()) {
                            date = elem + "-" + date;
                        }
                        CropDateDB book = new CropDateDB(blacklist.getId(), blacklist.getProdId(), blacklist.getProdname(), date);
                        book.save();

                        spinnerArray.add(removeLastChar(date) + "(" + blacklist.getProdId() + ")");
                        crop_id.add(blacklist.getId());
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(FarmerRecruitActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                    cropdate.setAdapter(spinnerArrayAdapter);
                }

                @Override
                public void onFailure(Call<List<CropDateResponse>> call, Throwable t) {
                    progressDialog.hide();
                    Toast.makeText(FarmerRecruitActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            spinnerArray = new ArrayList<String>();
            spinnerArray.clear();
            crop_id = new ArrayList<Integer>();
            crops = CropDateDB.listAll(CropDateDB.class);
            for (CropDateDB crop : crops) {
                spinnerArray.add(removeLastChar(crop.plantingDate) + "(" + crop.prodId + ")");
                crop_id.add(crop.id_crop);
            }
            spinnerArrayAdapter = new ArrayAdapter<String>(FarmerRecruitActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
            cropdate.setAdapter(spinnerArrayAdapter);
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
                        coordinates = currentLat+","+currentLong;
                        Log.d(TAG, "1 coordinates" + coordinates);
                        editor.putString("coordinates", coordinates);

                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(FarmerRecruitActivity.this, Locale.getDefault());

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

        if (cropdate == null && cropdate.getSelectedItem() ==null) {

            TextView errorText = (TextView)cropdate.getSelectedView();
            errorText.setError("must input Crop date");
            errorText.setTextColor(errorColor);//just to highlight that this is an error

            valid = false;
        }
        if (section == null && section.getSelectedItem() ==null) {

            TextView errorText = (TextView)section.getSelectedView();
            errorText.setError("must input section");
            errorText.setTextColor(errorColor);//just to highlight that this is an error

            valid = false;
        }
        if (section == null && section.getSelectedItem() ==null) {

            TextView errorText = (TextView)section.getSelectedView();
            errorText.setError("must input section");
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
        int selectedId = radioland.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        radioSexButton = (RadioButton) findViewById(selectedId);
        if (radioSexButton == null) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("must input no of units");
            ssbuilder.setSpan(fgcspan, 0, "must input no of units".length(), 0);
            //radioSexButton.setError(ssbuilder);
            valid = false;
        } else {
            radioSexButton.setError(null);
        }




        return valid;
    }
    public void submit(View v){
        if (!validate()) {
            Snackbar.make(v, "Correct the above errors first", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        int selectedId = radioland.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        radioSexButton = (RadioButton) findViewById(selectedId);
        String landop =radioSexButton.getText().toString();
        String land="M";

        if (landop.equals("Owned")){
            land="O";
        }else{
            land="L";
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
            hashMap.put("farmerid", farmer_id_string);
            hashMap.put("dateid", crop_id.get(cropdate.getSelectedItemPosition()).toString());
            hashMap.put("landownership", land);
            hashMap.put("cordinates", coordinates);
            hashMap.put("location", location_str);
            hashMap.put("noofunits", noofunits.getText().toString().trim());
            hashMap.put("section", section.getSelectedItem().toString().trim());

            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
            APIService service = retrofit.create(APIService.class);
            SharedPreferences prefs = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
            String auth_key = prefs.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
            Call<AllResponse> call = service.recruit(auth_key, hashMap);
            call.enqueue(new Callback<AllResponse>() {
                @Override
                public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                    progressDialog.hide();
                    try {
                        if (response.body() != null) {
                            pDialog.dismissWithAnimation();
                            new SweetAlertDialog(FarmerRecruitActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success")
                                    .setContentText("You have successfully recruited the farmer")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            startActivity(new Intent(FarmerRecruitActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                        }
                                    })
                                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            startActivity(new Intent(FarmerRecruitActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                        }
                                    })
                                    .show();

                        } else {
                            pDialog.dismissWithAnimation();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            new SweetAlertDialog(FarmerRecruitActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Ooops...")
                                    .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                    .show();


                        }
                    } catch (Exception e) {
                        pDialog.dismissWithAnimation();
                        new SweetAlertDialog(FarmerRecruitActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText(e.getMessage())
                                .show();
                    }
                    //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(Call<AllResponse> call, Throwable t) {
                    pDialog.dismissWithAnimation();
                    new SweetAlertDialog(FarmerRecruitActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(t.getMessage())
                            .show();
                }
            });
        } else {
            Log.d(TAG, "2 crop see " + crop_id.get(cropdate.getSelectedItemPosition()).toString());
            RecruitFarmerDB book = new RecruitFarmerDB(farmer_id_string, crop_id.get(cropdate.getSelectedItemPosition()).toString(), land, coordinates, location_str, noofunits.getText().toString().trim(), section.getSelectedItem().toString().trim());
            book.save();
            progressDialog.hide();
            new SweetAlertDialog(FarmerRecruitActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("No Wrong")
                    .setContentText("We have saved the data offline, We will submitted it when you have internet")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            startActivity(new Intent(FarmerRecruitActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                        }
                    })
                    .show();
        }

    }





    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
    public  String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(FarmerRecruitActivity.this, Locale.getDefault());
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
    public void onpenlocation(View v){

    }

}
