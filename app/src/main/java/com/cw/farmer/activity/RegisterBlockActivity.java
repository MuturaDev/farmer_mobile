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

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.cw.farmer.HandleConnectionAppCompatActivity;
import com.cw.farmer.NetworkUtil;
import com.cw.farmer.R;
import com.cw.farmer.model.GeneralSpinnerResponse;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.cw.farmer.spinner_models.GeneralSpinner;
import com.cw.farmer.table_models.RegisterBlockTB;
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

public class RegisterBlockActivity extends HandleConnectionAppCompatActivity {

    private ProgressBar spinner_progress;
    private Spinner spinner_farm;
    private GeneralSpinner selectedValue = null;


    private EditText txt_Capture_Block;

    public void back(View view){
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_block_layout);

        txt_Capture_Block = findViewById(R.id.txt_Capture_Block);


        spinner_progress = findViewById(R.id.spinner_progress);
        spinner_farm = findViewById(R.id.spinner_farm);
        spinner_farm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (spinner_farm.getId()){
                    case -1:
                        spinner_farm.setSelection(-1,true);
                        break;
                    default:
                        selectedValue = (GeneralSpinner)  adapterView.getSelectedItem();
                        Log.d(RegisterBlockActivity.this.getPackageName().toUpperCase(), "Selected Spinner Value: " + selectedValue.getId() + " " + selectedValue.toString());
                        if(selectedValue.getId() == -1)
                            selectedValue = null;

                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        populateSpinner();

    }



    private void showProgress(boolean show){
        if(show){
            spinner_progress.setVisibility(View.VISIBLE);
            spinner_farm.setVisibility(View.GONE);
        }else{
            spinner_farm.setVisibility(View.VISIBLE);
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
                    Call<List<GeneralSpinnerResponse>> call = service.getFarmNames(auth_key);
                    call.enqueue(new Callback<List<GeneralSpinnerResponse>>() {
                            @Override
                            public void onResponse(Call<List<GeneralSpinnerResponse>> call, Response<List<GeneralSpinnerResponse>> response) {

                                if(response.body() != null) {
                                    if(response.body().size() > 0) {

                                        OfflineFeature.saveSharedPreferences(response.body(), "RegisterBlockSelectFarm", getApplicationContext());

                                        List<GeneralSpinner> farmNames = new ArrayList<>();
                                        farmNames.clear();

                                        GeneralSpinner spinnerValue = new GeneralSpinner(-1, "Select Farm");
                                        farmNames.add(spinnerValue);

                                        for (GeneralSpinnerResponse names : response.body()) {
                                            GeneralSpinner spinnerValue1 = new GeneralSpinner(names.getId(), names.getShtDesc());
                                            farmNames.add(spinnerValue1);
                                        }

                                        ArrayAdapter<GeneralSpinner> farmSpinnerArrayAdapter = new ArrayAdapter<>(RegisterBlockActivity.this, android.R.layout.simple_spinner_dropdown_item, farmNames);

                                        showProgress(false);
                                        spinner_farm.setAdapter(farmSpinnerArrayAdapter);
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<List<GeneralSpinnerResponse>> call, Throwable t) {

                            }
                    });
                }else{
                    List<GeneralSpinner> farmNames = new ArrayList<>();
                   // Log.d(RegisterBlockActivity.this.getPackageName().toUpperCase(), response.body().toString());
                    farmNames.clear();

                    GeneralSpinner spinnerValue = new GeneralSpinner(-1, "Select Farm");
                    farmNames.add(spinnerValue);

                    for (GeneralSpinnerResponse names : (ArrayList<GeneralSpinnerResponse>) OfflineFeature.getSharedPreferencesObject("RegisterBlockSelectFarm",getApplicationContext(), GeneralSpinnerResponse.class)) {
                        GeneralSpinner spinnerValue1 = new GeneralSpinner(names.getId(), names.getShtDesc());
                        farmNames.add(spinnerValue1);
                    }

                    ArrayAdapter<GeneralSpinner> farmSpinnerArrayAdapter = new ArrayAdapter<>(RegisterBlockActivity.this, android.R.layout.simple_spinner_dropdown_item, farmNames);

                    showProgress(false);
                    spinner_farm.setAdapter(farmSpinnerArrayAdapter);
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


//    public List<GeneralSpinnerResponse> getArrayList(String key) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        Gson gson = new Gson();
//        String json = prefs.getString(key, null);
//        Type type = new TypeToken<ArrayList<PageItemsDestruction>>() {
//        }.getType();
//        return gson.fromJson(json, type);
//    }


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




        if (txt_Capture_Block.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("Please Input a value in Harvest Kilos Field");
            ssbuilder.setSpan(fgcspan, 0, "Please Input a value in Harvest Kilos Field".length(), 0);
            txt_Capture_Block.setError(ssbuilder);
            valid = false;
        } else {
            txt_Capture_Block.setError(null);
        }




        if( selectedValue == null){
            (findViewById(R.id.spinner_layout)).setBackground(getResources().getDrawable(R.drawable.shake_spinner_bg));
            (findViewById(R.id.spinner_layout)).startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            valid = false;
        }else{
            (findViewById(R.id.spinner_layout)).setBackground(getResources().getDrawable(R.drawable.spinner_backgroung));
        }




        return valid;
    }


    public void submit(View v) {
        if (!validate()) {
            Snackbar.make(v, "Correct the above errors first", Snackbar.LENGTH_LONG)
                    .show();
            return;
        }

        //close keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Please wait...submitting.");
        pDialog.setCancelable(false);
        pDialog.show();
        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {

            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
            String centerid = prefs_auth.getString("center_ids", "1");
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("centerId", centerid);//ask about this
            hashMap.put("farmNameId", String.valueOf(selectedValue.getId()));
            hashMap.put("blockName", txt_Capture_Block.getText().toString());

            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
            APIService service = retrofit.create(APIService.class);

            Call<ResponseBody> call = service.postRegisterBlock(auth_key, hashMap);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        if (response.isSuccessful()) {
                            pDialog.dismissWithAnimation();
                            new SweetAlertDialog(RegisterBlockActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Confirmation")
                                    .setContentText("Successfully submitted")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            startActivity(new Intent(RegisterBlockActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                        }
                                    })
//                                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
//                                        @Override
//                                        public void onClick(SweetAlertDialog sDialog) {
//                                            sDialog.dismissWithAnimation();
//                                            startActivity(new Intent(RegisterBlockActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//
//                                        }
//                                    })
                                    .show();

                        } else {
                            pDialog.dismissWithAnimation();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            new SweetAlertDialog(RegisterBlockActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Try Again")
                                    // .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                    .show();


                        }
                    } catch (Exception e) {
                        pDialog.dismissWithAnimation();
                        new SweetAlertDialog(RegisterBlockActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Try Again")
                                //.setContentText(e.getMessage())
                                .show();
                    }
                    //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    pDialog.dismissWithAnimation();
//                    new SweetAlertDialog(RegisterBlockActivity.this, SweetAlertDialog.ERROR_TYPE)
//                            .setTitleText("Oops...")
//                            .setContentText(t.getMessage())
//                            .show();
                }
            });
        }else{

            HashMap<String, String> hashMap = new HashMap<>();
            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
            String centerid = prefs_auth.getString("center_ids", "1");
            hashMap.put("centerId", centerid);//ask about this
            hashMap.put("farmNameId", String.valueOf(selectedValue.getId()));
            hashMap.put("blockName", txt_Capture_Block.getText().toString());

            RegisterBlockTB register = new RegisterBlockTB(hashMap.get("centerId"), hashMap.get("farmNameId"), hashMap.get("blockName"));
            register.save();

            pDialog.dismissWithAnimation();
            new SweetAlertDialog(RegisterBlockActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("No Wrong")
                    .setContentText("We have saved the data offline, We will submitted it when you have internet")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            startActivity(new Intent(RegisterBlockActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                        }
                    })
                    .show();
        }


    }

}
