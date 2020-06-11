package com.cw.farmer.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.cw.farmer.HandleConnectionAppCompatActivity;
import com.cw.farmer.NetworkUtil;
import com.cw.farmer.R;
import com.cw.farmer.model.AllResponse;
import com.cw.farmer.model.PlantingVerifyDB;
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

public class PlantingVerificationActivity extends HandleConnectionAppCompatActivity {
    EditText farmer,account_no,nounits,confirmedUnits;
    Spinner codedate_harvesting;
    String farmer_id_string,noofunits,plantingId;
    List<String> cropDateId_list,reason_main;
    RadioGroup Plantconfirmed_radio,Waterconfirmed_radio;
    RadioButton plant_btn,water_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planting_verification);

        enableLocationTracking();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Plantconfirmed_radio = findViewById(R.id.Plantconfirmed);
        Waterconfirmed_radio = findViewById(R.id.Waterconfirmed);
        farmer = findViewById(R.id.farmer);
        nounits = findViewById(R.id.no_units);
        confirmedUnits = findViewById(R.id.confirmedUnits);
        Plantconfirmed_radio.check(R.id.radioYes);
        Waterconfirmed_radio.check(R.id.radioYesW);
        farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor searcheditor = getSharedPreferences("search", MODE_PRIVATE).edit();
                searcheditor.putString("activity","planting vertification");
                searcheditor.apply();

                Intent intent = new Intent(PlantingVerificationActivity.this, SearchPlantingVerficationActivity.class);
                startActivity(intent);
            }
        });
        codedate_harvesting = findViewById(R.id.codedate_harvesting);
        account_no = findViewById(R.id.account_no);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String name =(String) b.get("name");
            farmer.setText(name);

            String id =(String) b.get("contractId");
            farmer_id_string=id;

            String noofunits_now =(String) b.get("accountNo");
            account_no.setText(noofunits_now);

            cropDateId_list = new ArrayList<String>();
            String cropDateId =(String) b.get("crop_date");
            cropDateId_list.add(cropDateId);

            String noofunits = (String)b.get("nounits");
            nounits.setText(noofunits);



            ArrayList<String> spinnerArray = new ArrayList<String>();
            spinnerArray.clear();
            String crop_date =(String) b.get("crop_date");
            spinnerArray.add(crop_date);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(PlantingVerificationActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
            codedate_harvesting.setAdapter(spinnerArrayAdapter);


        }


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

        if (codedate_harvesting == null && codedate_harvesting.getSelectedItem() == null) {

            TextView errorText = (TextView)codedate_harvesting.getSelectedView();
            errorText.setError("select crop date");
            errorText.setTextColor(errorColor);//just to highlight that this is an error

            valid = false;
        }

        if (account_no.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("must input account no");
            ssbuilder.setSpan(fgcspan, 0, "must input account no".length(), 0);
            account_no.setError(ssbuilder);
            valid = false;
        } else {
            account_no.setError(null);
        }

        if (farmer.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("Select a farmer");
            ssbuilder.setSpan(fgcspan, 0, "Select a farmer".length(), 0);
            farmer.setError(ssbuilder);
            valid = false;
        } else {
            farmer.setError(null);
        }

        if(confirmedUnits.getText().toString().isEmpty()){
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("must confirm No. of Units");
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, "must confirm No. of Units".length(), 0);
            confirmedUnits.setError(spannableStringBuilder);
            valid = false;
        }else{
            confirmedUnits.setError(null);
        }




        return valid;
    }
    public void plantverify_submit(View v){
        if (!validate()) {
            Snackbar.make(v, "Correct the above errors first", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        if(locationText != null && !location_str.isEmpty() && !coordinates.isEmpty()) {
            //Toast.makeText(this, "Location: " + location_str, Toast.LENGTH_SHORT).show();
            //return;
            StringBuilder sb = new StringBuilder();
            sb.append("Location Lat-Long: ");
            sb.append( "FusedLocationProviderClient = " +coordinates + " LocationBaseActivity = " + locationText );
            sb.append("\n");
            sb.append("Location Address: ");
            sb.append(location_str);

            Log.d(getPackageName().toUpperCase(), sb.toString());
        }else{
            return;
        }

        /*{
              “contractid”: 23,
              “cordinates”:””,
              “location”:””,
              “Plantconfirmed”:”Y” or “N”,
              “Waterconfirmed”:”Y” or “N”,
          }
         */
        int selectedId = Plantconfirmed_radio.getCheckedRadioButtonId();
        plant_btn = (RadioButton) findViewById(selectedId);
        String plant_print =plant_btn.getText().toString();
        String plant_value="N";

        if (plant_print.equals("Yes")){
            plant_value="Y";
        }else{
            plant_value="N";
        }

        int selectedId_2 = Waterconfirmed_radio.getCheckedRadioButtonId();
        water_btn = (RadioButton) findViewById(selectedId_2);
        String water_print =plant_btn.getText().toString();
        String water_value="N";

        if (water_print.equals("Yes")){
            water_value="Y";
        }else{
            water_value="N";
        }
//        {
//"contractid": 23,
// "cordinates":"",
//"location":"",
//"plantconfirmed":"Y",
//"waterconfirmed":"Y"
//}
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Submitting Planting Verification Data...");
        pDialog.setCancelable(false);
        pDialog.show();
        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("cordinates", coordinates);
            hashMap.put("location", location_str);
            hashMap.put("contractid", farmer_id_string);
            hashMap.put("plantconfirmed", plant_value);
            hashMap.put("waterconfirmed", water_value);
            hashMap.put("confirmedUnits", confirmedUnits.getText().toString());


            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
            APIService service = retrofit.create(APIService.class);
            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
            Call<AllResponse> call = service.postplantverify(auth_key, hashMap);
            call.enqueue(new Callback<AllResponse>() {
                @Override
                public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                    pDialog.dismissWithAnimation();
                    try {
                        if (response.body() != null) {
                            new SweetAlertDialog(PlantingVerificationActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success")
                                    .setContentText("You have successfully submitted " + farmer.getText() + " plant verification details")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            startActivity(new Intent(PlantingVerificationActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                        }
                                    })
//                                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
//                                        @Override
//                                        public void onClick(SweetAlertDialog sDialog) {
//                                            sDialog.dismissWithAnimation();
//                                            startActivity(new Intent(PlantingVerificationActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//
//                                        }
//                                    })
                                    .show();

                        } else {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            new SweetAlertDialog(PlantingVerificationActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Ooops...")
                                    .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                    .show();

                        }
                    } catch (Exception e) {
                        new SweetAlertDialog(PlantingVerificationActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText(e.getMessage())
                                .show();
                    }
                    //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(Call<AllResponse> call, Throwable t) {
                    pDialog.cancel();
                    new SweetAlertDialog(PlantingVerificationActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(t.getMessage())
                            .show();
                }
            });
        } else {
            PlantingVerifyDB book = new PlantingVerifyDB(coordinates, location_str, farmer_id_string, plant_value, water_value);
            book.save();
            pDialog.hide();
            new SweetAlertDialog(PlantingVerificationActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("No Wrong")
                    .setContentText("We have saved the data offline, We will submitted it when you have internet")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            startActivity(new Intent(PlantingVerificationActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                        }
                    })
                    .show();
        }

    }

}
