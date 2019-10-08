package com.cw.farmer.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import com.cw.farmer.model.AllResponse;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.cw.farmer.R;

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

public class PlantingVerificationActivity extends AppCompatActivity {
    EditText farmer,account_no;
    Spinner codedate_harvesting;
    String farmer_id_string,noofunits,plantingId;
    List<String> cropDateId_list,reason_main;
    RadioGroup Plantconfirmed_radio,Waterconfirmed_radio;
    RadioButton plant_btn,water_btn;
    String location_str, coordinates;
    Double currentLat,currentLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planting_verification);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Plantconfirmed_radio = findViewById(R.id.Plantconfirmed);
        Waterconfirmed_radio = findViewById(R.id.Waterconfirmed);
        farmer = findViewById(R.id.farmer);
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

            ArrayList<String> spinnerArray = new ArrayList<String>();
            spinnerArray.clear();
            String crop_date =(String) b.get("crop_date");
            spinnerArray.add(crop_date);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(PlantingVerificationActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
            codedate_harvesting.setAdapter(spinnerArrayAdapter);


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
                        currentLat = location.getLatitude();
                        currentLong = location.getLongitude();
                        coordinates = currentLat+","+currentLong;

                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(PlantingVerificationActivity.this, Locale.getDefault());

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

        if (codedate_harvesting == null && codedate_harvesting.getSelectedItem() ==null) {

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




        return valid;
    }
    public void plantverify_submit(View v){
        if (!validate()) {
            Snackbar.make(v, "Correct the above errors first", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
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
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("cordinates",coordinates);
        hashMap.put("location",location_str);
        hashMap.put("contractid",farmer_id_string);
        hashMap.put("plantconfirmed",plant_value);
        hashMap.put("waterconfirmed",water_value);

        Retrofit retrofit = ApiClient.getClient("/authentication/");
        APIService service = retrofit.create(APIService.class);
        Call<AllResponse> call = service.postplantverify("Basic YWRtaW46bWFudW5pdGVk",hashMap);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                pDialog.dismissWithAnimation();
                try {
                    if (response.body()!=null){
                        new SweetAlertDialog(PlantingVerificationActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success")
                                .setContentText("You have successfully submitted "+farmer.getText()+" plant verification details")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        startActivity(new Intent(PlantingVerificationActivity.this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                    }
                                })
                                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        startActivity(new Intent(PlantingVerificationActivity.this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                    }
                                })
                                .show();

                    }else{
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
    }

}
