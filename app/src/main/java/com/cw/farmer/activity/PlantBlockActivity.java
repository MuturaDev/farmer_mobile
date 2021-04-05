package com.cw.farmer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.cw.farmer.HandleConnectionAppCompatActivity;
import com.cw.farmer.NetworkUtil;
import com.cw.farmer.R;
import com.cw.farmer.model.GeneralSpinnerResponse;
import com.cw.farmer.model.PageItemSearchArea;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.cw.farmer.spinner_models.GeneralSpinner;
import com.cw.farmer.table_models.PlantBlockTB;
import com.cw.farmer.offlinefunctions.OfflineFeature;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PlantBlockActivity extends HandleConnectionAppCompatActivity {

   
    private ProgressBar spinner_progress;
    private Spinner spinner_variety;
    private GeneralSpinner varietyValue;

    private TextView select_block;
    private EditText txt_bags_planted;
    private Spinner spinner_seed_rate;
    private String seedrate_spinner_selectedValue;
    private EditText txt_bags_lot_no;
    private PageItemSearchArea returnPlantBlock;


    public void back(View view){
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_block_layout);


        enableLocationTracking();

        select_block = findViewById(R.id.select_block);
        txt_bags_planted = findViewById(R.id.txt_bags_planted);
        spinner_seed_rate = findViewById(R.id.spinner_seed_rate);
        txt_bags_lot_no = findViewById(R.id.txt_bags_lot_no);

        if(getIntent() != null){
            if(getIntent().getExtras() != null){
                Bundle bundle = getIntent().getExtras();
                returnPlantBlock = (PageItemSearchArea)  bundle.getSerializable("Message");
                select_block.setText(returnPlantBlock.getArea() + " " + returnPlantBlock.getAreaType());
            }
        }

        spinner_seed_rate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (spinner_seed_rate.getId()){
                    case 0:
                        seedrate_spinner_selectedValue = null;
                        break;
                    default:
                        seedrate_spinner_selectedValue = (String)  adapterView.getSelectedItem();
                        Log.d(PlantBlockActivity.this.getPackageName().toUpperCase(), "Selected Spinner Value: " + (seedrate_spinner_selectedValue == null) + " " + seedrate_spinner_selectedValue);
                        if(seedrate_spinner_selectedValue.equals("Select seed rate"))
                            seedrate_spinner_selectedValue = null;


                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_progress = findViewById(R.id.spinner_progress);
        spinner_variety = findViewById(R.id.spinner_variety);
        spinner_variety.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (spinner_variety.getId()){
                    case -1:
                        varietyValue = null;
                        break;
                    default:
                        varietyValue = (GeneralSpinner)  adapterView.getSelectedItem();
                        Log.d(PlantBlockActivity.this.getPackageName().toUpperCase(), "Selected Spinner Value: " + varietyValue.getId() + " " + varietyValue.toString());
                        if(varietyValue.getId() == -1)
                            varietyValue = null;


                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                varietyValue = null;
            }
        });

        populateSpinner();


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

        if (txt_bags_lot_no.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("Please Input a value in Bags Lot No. Field");
            ssbuilder.setSpan(fgcspan, 0, "Please Input a value in Bags Lot No. Field".length(), 0);
            txt_bags_lot_no.setError(ssbuilder);
            valid = false;
        } else {
            txt_bags_lot_no.setError(null);
        }

        if (txt_bags_planted.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("Please Input a value in Bags Planted Field.");
            ssbuilder.setSpan(fgcspan, 0, "Please Input a value in Bags Planted Field".length(), 0);
            txt_bags_planted.setError(ssbuilder);
            valid = false;
        } else {
            txt_bags_planted.setError(null);
        }


        if( seedrate_spinner_selectedValue == null){
            spinner_seed_rate.setBackground(getResources().getDrawable(R.drawable.shake_spinner_bg));
            spinner_seed_rate.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            valid = false;
        }else{
            spinner_seed_rate.setBackground(getResources().getDrawable(R.drawable.spinner_backgroung));
        }

        if(varietyValue == null){
            (findViewById(R.id.spinner_layout)).setBackground(getResources().getDrawable(R.drawable.shake_spinner_bg));
            (findViewById(R.id.spinner_layout)).startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            valid = false;
        }else{
            (findViewById(R.id.spinner_layout)).setBackground(getResources().getDrawable(R.drawable.spinner_backgroung));
        }

        if( select_block.getText().toString().isEmpty()){
            (findViewById(R.id.select_block_layout)).setBackground(getResources().getDrawable(R.drawable.shake_search_bg));
            (findViewById(R.id.select_block_layout)).startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            valid = false;
        }else{
            (findViewById(R.id.select_block_layout)).setBackground(getResources().getDrawable(R.drawable.search_bg));
        }


        return valid;
    }


    public void searchBlock(View view) {
        startActivity(new Intent(PlantBlockActivity.this, SearchSearchAreaActivity.class));
    }

    public void submit(View v) {



        if (!validate()) {
            Snackbar.make(v, "Correct the above errors first", Snackbar.LENGTH_LONG)
                    .show();
            return;
        }

        if(locationText != null  && coordinates != null) {
            if(locationText != null && !coordinates.isEmpty()) {
                if(location_str == null){
                    location_str = "Offline";
                }else{
                    if(location_str.isEmpty()){
                        location_str = "Offline";
                    }
                }

                //Toast.makeText(this, "Location: " + location_str, Toast.LENGTH_SHORT).show();
                //return;
                StringBuilder sb = new StringBuilder();
                sb.append("Location Lat-Long: ");
                sb.append("FusedLocationProviderClient = " + coordinates + " LocationBaseActivity = " + locationText);
                sb.append("\n");
                sb.append("Location Address: ");
                sb.append(location_str);

                Log.d(getPackageName().toUpperCase(), sb.toString());

                //close keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);



                SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Please wait...submitting.");
                pDialog.setCancelable(false);
                pDialog.show();
                if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("bagsPlanted", txt_bags_planted.getText().toString());
                    hashMap.put("seedRate", seedrate_spinner_selectedValue);
                    hashMap.put("varietyId",String.valueOf(varietyValue.getId()));
                    hashMap.put("blockId", String.valueOf(returnPlantBlock.getId()));
                    hashMap.put("cordinates", coordinates);
                    hashMap.put("location",location_str);
                    hashMap.put("bagLotNo",txt_bags_lot_no.getText().toString());

                    Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                    APIService service = retrofit.create(APIService.class);
                    SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                    String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                    Call<ResponseBody> call = service.postPlantBlock(auth_key, hashMap);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            try {
                                if (response.isSuccessful()) {
                                    pDialog.dismissWithAnimation();
                                    new SweetAlertDialog(PlantBlockActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Success")
                                            .setContentText("Successfully submitted.")
                                            .setConfirmText("Ok")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                    startActivity(new Intent(PlantBlockActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                                }
                                            })
//                                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
//                                        @Override
//                                        public void onClick(SweetAlertDialog sDialog) {
//                                            sDialog.dismissWithAnimation();
//                                            startActivity(new Intent(PlantBlockActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//
//                                        }
//                                    })
                                            .show();

                                } else {
                                    pDialog.dismissWithAnimation();
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    new SweetAlertDialog(PlantBlockActivity.this, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Try again")
                                            .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                            .show();


                                }
                            } catch (Exception e) {
                                pDialog.dismissWithAnimation();
                                new SweetAlertDialog(PlantBlockActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Try again")
                                        .setContentText(e.getMessage())
                                        .show();
                            }
                            //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            pDialog.dismissWithAnimation();
                            new SweetAlertDialog(PlantBlockActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Try again...")
                                    .setContentText(t.getMessage())
                                    .show();
                        }
                    });
                }else{
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("bagsPlanted", txt_bags_planted.getText().toString());
                    hashMap.put("seedRate", seedrate_spinner_selectedValue);
                    hashMap.put("varietyId",String.valueOf(varietyValue.getId()));
                    hashMap.put("blockId", String.valueOf(returnPlantBlock.getId()));
                    hashMap.put("cordinates", coordinates);
                    hashMap.put("location",location_str);
                    hashMap.put("bagLotNo",txt_bags_lot_no.getText().toString());

                    PlantBlockTB plant = new PlantBlockTB(
                            hashMap.get("bagsPlanted"),
                            hashMap.get("seedRate"),
                            hashMap.get("varietyId"),
                            hashMap.get("blockId"),
                            hashMap.get("cordinates"),
                            hashMap.get("location"),
                            hashMap.get("bagLotNo")
                    );
                    plant.save();

                    new SweetAlertDialog(PlantBlockActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("No Wrong")
                            .setContentText("We have saved the data offline, We will submitted it when you have internet")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    startActivity(new Intent(PlantBlockActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                }
                            })
                            .show();
                }



            } else {
                Snackbar.make(v, "Couldn't get location, because network is not accessible!", Snackbar.LENGTH_SHORT)
                        .show();
            }
        }else{
            Snackbar.make(v, "Couldn't get location, because network is not accessible!", Snackbar.LENGTH_SHORT)
                    .show();
        }



    }



    private void showProgress(boolean show){
        if(show){
            spinner_progress.setVisibility(View.VISIBLE);
            spinner_variety.setVisibility(View.GONE);
        }else{
            spinner_variety.setVisibility(View.VISIBLE);
            spinner_progress.setVisibility(View.GONE);
        }
    }

//    public List<GeneralSpinnerResponse> getArrayList(String key) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        Gson gson = new Gson();
//        String json = prefs.getString(key, null);
//        Type type = new TypeToken<ArrayList<PageItemsDestruction>>() {
//        }.getType();
//        return gson.fromJson(json, type);
//    }


    private void populateSpinner(){

        AsyncTask asyncTask = new AsyncTask() {

            @Override
            protected void onPreExecute() {
                showProgress(true);
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Object o) {

                if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
                    Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                    APIService service = retrofit.create(APIService.class);
                    SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                    String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                    Call<List<GeneralSpinnerResponse>> call = service.getVariety(auth_key);
                    call.enqueue(new Callback<List<GeneralSpinnerResponse>>() {
                        @Override
                        public void onResponse(Call<List<GeneralSpinnerResponse>> call, Response<List<GeneralSpinnerResponse>> response) {

                            if(response.body() != null) {
                                if(response.body().size() > 0) {
                                    OfflineFeature.saveSharedPreferences(response.body(), "PlantBlockSpinner", getApplicationContext());
                                    List<GeneralSpinner> farmNames = new ArrayList<>();
                                    farmNames.clear();

                                    GeneralSpinner spinnerValue = new GeneralSpinner(-1, "Variety");
                                    farmNames.add(spinnerValue);

                                    for (GeneralSpinnerResponse names : response.body()) {
                                        GeneralSpinner spinnerValue1 = new GeneralSpinner(names.getId(), names.getShtDesc());
                                        farmNames.add(spinnerValue1);
                                    }

                                    ArrayAdapter<GeneralSpinner> farmSpinnerArrayAdapter = new ArrayAdapter<>(PlantBlockActivity.this, android.R.layout.simple_spinner_dropdown_item, farmNames);

                                    showProgress(false);
                                    spinner_variety.setAdapter(farmSpinnerArrayAdapter);
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<List<GeneralSpinnerResponse>> call, Throwable t) {

                        }
                    });

                }else{


                    List<GeneralSpinner> farmNames = new ArrayList<>();
                    farmNames.clear();

                    GeneralSpinner spinnerValue = new GeneralSpinner(-1, "Variety");
                    farmNames.add(spinnerValue);

                    for (GeneralSpinnerResponse names : (ArrayList<GeneralSpinnerResponse>) OfflineFeature.getSharedPreferencesObject("PlantBlockSpinner", getApplicationContext(),GeneralSpinnerResponse.class)) {
                        GeneralSpinner spinnerValue1 = new GeneralSpinner(names.getId(), names.getShtDesc());
                        farmNames.add(spinnerValue1);
                    }

                    ArrayAdapter<GeneralSpinner> farmSpinnerArrayAdapter = new ArrayAdapter<>(PlantBlockActivity.this, android.R.layout.simple_spinner_dropdown_item, farmNames);

                    showProgress(false);
                    spinner_variety.setAdapter(farmSpinnerArrayAdapter);
                }



                super.onPostExecute(o);
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                //get from database
                return null;
            }
        };
        asyncTask.execute();


    }



}
