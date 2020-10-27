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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.cw.farmer.NetworkUtil;
import com.cw.farmer.R;
import com.cw.farmer.model.GeneralSpinnerResponse;
import com.cw.farmer.model.PageItemPlantBlock;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.cw.farmer.spinner_models.GeneralSpinner;
import com.cw.farmer.table_models.ApplyFertilizerTB;
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

public class ApplyFertilizerBlockActivity extends AppCompatActivity {


    private TextView select_block;
    private Spinner sp_fertilizer;
    private EditText txt_appliedrate;
    private Spinner sp_method;
    private Spinner sp_equipment;


    private GeneralSpinner fertilizerValue = null;
    private String methodValue = null;
    private String equipmentValue = null;

    private PageItemPlantBlock returnIntentPlantBlock;

    private ProgressBar spinner_progress;

    public void back(View view){
        finish();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_fertilizer_layout);

        spinner_progress = findViewById(R.id.spinner_progress);
        select_block = findViewById(R.id.select_block);

        if(getIntent() != null){
            if(getIntent().getExtras() != null){
                Bundle bundle = getIntent().getExtras();
                returnIntentPlantBlock = (PageItemPlantBlock)  bundle.getSerializable("Message");
                select_block.setText(returnIntentPlantBlock.getBlockName());
            }
        }

        sp_fertilizer = findViewById(R.id.spinner_fertilizer);
        sp_fertilizer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i==-1){
                    fertilizerValue = null;
                }else{
                    fertilizerValue = (GeneralSpinner) adapterView.getSelectedItem();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        txt_appliedrate = findViewById(R.id.txt_applied_rate);
        sp_method = findViewById(R.id.spinner_method);
        sp_method.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i==0){
                    methodValue = null;
                }else{
                    methodValue = (String) adapterView.getSelectedItem();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_equipment = findViewById(R.id.spinner_equipment);
        sp_equipment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    equipmentValue = null;
                }else{
                    equipmentValue  = (String) adapterView.getSelectedItem();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        populateSpinner();
    }

    public void searchBlock(View view){
        //select_block.setText("Nyeri");
        Intent intent = new Intent(ApplyFertilizerBlockActivity.this, SearchPlantBlockActivity.class);
        intent.putExtra("SearchFor","ApplyFertilizer");
        startActivity(intent);
    }

    public void submit(View view){
        if (!validate()) {
            Snackbar.make(view, "Correct the above errors first", Snackbar.LENGTH_LONG)
                    .show();
            return;
        }



        //close keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);



        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Please wait...submitting.");
        pDialog.setCancelable(false);
        pDialog.show();
        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("blockId", String.valueOf(returnIntentPlantBlock.getId()));
            hashMap.put("fertilizerId", String.valueOf(fertilizerValue.getId()));
            hashMap.put("appliedRate",txt_appliedrate.getText().toString());
            hashMap.put("method", methodValue);
            hashMap.put("equipment", equipmentValue);
            hashMap.put("locale","en");

            Log.d(this.getPackageName().toUpperCase(), "Before Submitting: " + hashMap.toString());


            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
            APIService service = retrofit.create(APIService.class);
            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
            Call<ResponseBody> call = service.postApplyFertilizer(auth_key, hashMap);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        if (response.isSuccessful()) {
                            pDialog.dismissWithAnimation();
                            new SweetAlertDialog(ApplyFertilizerBlockActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success")
                                    .setContentText("Successfully submitted.")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            startActivity(new Intent(ApplyFertilizerBlockActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

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
                            new SweetAlertDialog(ApplyFertilizerBlockActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Try again")
                                    .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                    .show();


                        }
                    } catch (Exception e) {
                        pDialog.dismissWithAnimation();
                        new SweetAlertDialog(ApplyFertilizerBlockActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Try again")
                                .setContentText(e.getMessage())
                                .show();
                    }
                    //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pDialog.dismissWithAnimation();
                    new SweetAlertDialog(ApplyFertilizerBlockActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Try again...")
                            .setContentText(t.getMessage())
                            .show();
                }
            });
        }else{
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("blockId", String.valueOf(returnIntentPlantBlock.getId()));
            hashMap.put("fertilizerId", String.valueOf(fertilizerValue.getId()));
            hashMap.put("appliedRate",txt_appliedrate.getText().toString());
            hashMap.put("method", methodValue);
            hashMap.put("equipment", equipmentValue);
            hashMap.put("locale","en");

            ApplyFertilizerTB apply = new ApplyFertilizerTB(
                    hashMap.get("blockId"),
                    hashMap.get("fertilizerId"),
                    hashMap.get("appliedRate"),
                    hashMap.get("method"),
                    hashMap.get("equipment"),
                    hashMap.get("locale")
            );
            apply.save();

            new SweetAlertDialog(ApplyFertilizerBlockActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("No Wrong")
                    .setContentText("We have saved the data offline, We will submitted it when you have internet")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            startActivity(new Intent(ApplyFertilizerBlockActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                        }
                    })
                    .show();
        }
    }

    private boolean validate(){
        boolean valid = true;
        int errorColor;
        final int version = Build.VERSION.SDK_INT;

        //Get the defined errorColor from color resource.
        if (version >= 23) {
            errorColor = ContextCompat.getColor(getApplicationContext(), R.color.errorColor);
        } else {
            errorColor = getResources().getColor(R.color.errorColor);
        }



        if (txt_appliedrate.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("Please Input a value in Bags Planted Field.");
            ssbuilder.setSpan(fgcspan, 0, "Please Input a value in Bags Planted Field".length(), 0);
            txt_appliedrate.setError(ssbuilder);
            valid = false;
        } else {
            txt_appliedrate.setError(null);
        }


        if( fertilizerValue == null){
            (findViewById(R.id.spinner_layout)).setBackground(getResources().getDrawable(R.drawable.shake_spinner_bg));
            (findViewById(R.id.spinner_layout)).startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            valid = false;
        }else{
            (findViewById(R.id.spinner_layout)).setBackground(getResources().getDrawable(R.drawable.spinner_backgroung));
        }

        if(methodValue == null){
            sp_method.setBackground(getResources().getDrawable(R.drawable.shake_spinner_bg));
            sp_method.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            valid = false;
        }else{
            sp_method.setBackground(getResources().getDrawable(R.drawable.spinner_backgroung));
        }
        if (equipmentValue == null) {

            sp_equipment.setBackground(getResources().getDrawable(R.drawable.shake_spinner_bg));
            sp_equipment.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            valid = false;
        } else {
            sp_equipment.setBackground(getResources().getDrawable(R.drawable.spinner_backgroung));
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


    private void showProgress(boolean show){
        if(show){
            spinner_progress.setVisibility(View.VISIBLE);
            sp_fertilizer.setVisibility(View.GONE);
        }else{
            sp_fertilizer.setVisibility(View.VISIBLE);
            spinner_progress.setVisibility(View.GONE);
        }
    }


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
                    Call<List<GeneralSpinnerResponse>> call = service.getFertilizer(auth_key);
                    call.enqueue(new Callback<List<GeneralSpinnerResponse>>() {
                        @Override
                        public void onResponse(Call<List<GeneralSpinnerResponse>> call, Response<List<GeneralSpinnerResponse>> response) {

                            if (response.body() != null) {
                                if (response.body().size() > 0) {
                                    List<GeneralSpinner> farmNames = new ArrayList<>();
                                    Log.d(ApplyFertilizerBlockActivity.this.getPackageName().toUpperCase(), response.body().toString());
                                    farmNames.clear();

                                    GeneralSpinner spinnerValue = new GeneralSpinner(-1, "Select Fertilizer");
                                    farmNames.add(spinnerValue);

                                    for (GeneralSpinnerResponse names : response.body()) {
                                        GeneralSpinner spinnerValue1 = new GeneralSpinner(names.getId(), names.getShtDesc());
                                        farmNames.add(spinnerValue1);
                                    }

                                    ArrayAdapter<GeneralSpinner> farmSpinnerArrayAdapter = new ArrayAdapter<>(ApplyFertilizerBlockActivity.this, android.R.layout.simple_spinner_dropdown_item, farmNames);

                                    showProgress(false);
                                    sp_fertilizer.setAdapter(farmSpinnerArrayAdapter);
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

                    GeneralSpinner spinnerValue = new GeneralSpinner(-1, "Select Fertilizer");
                    farmNames.add(spinnerValue);

                    for (GeneralSpinnerResponse names :  (ArrayList<GeneralSpinnerResponse>) OfflineFeature.getSharedPreferences("ApplyFertilizerSpinner", getApplicationContext(), GeneralSpinnerResponse.class)) {
                        GeneralSpinner spinnerValue1 = new GeneralSpinner(names.getId(), names.getShtDesc());
                        farmNames.add(spinnerValue1);
                    }

                    ArrayAdapter<GeneralSpinner> farmSpinnerArrayAdapter = new ArrayAdapter<>(ApplyFertilizerBlockActivity.this, android.R.layout.simple_spinner_dropdown_item, farmNames);

                    showProgress(false);
                    sp_fertilizer.setAdapter(farmSpinnerArrayAdapter);
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
