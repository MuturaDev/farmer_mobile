package com.cw.farmer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.cw.farmer.NetworkUtil;
import com.cw.farmer.R;
import com.cw.farmer.model.PageItemPlantBlock;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ScoutingBlockActivity extends AppCompatActivity {

    private CardView cv_first, cv_next;

    //first
    private Spinner sp_watered;
    private Spinner sp_germination;
    private Spinner sp_weeded;
    //next
    private EditText txt_crop_survival_rate;
    private EditText txt_flowering;
    private EditText txt_average_pods;

    private String wateredValue = null;
    private String germinationValue = null;
    private String weededValue = null;

    private TextView select_block;

    private PageItemPlantBlock returnIntentPlantBlock;


    public void back(View view){
        finish();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scouting_block_layout);

        //creates space between scouting image and back button
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cv_first = findViewById(R.id.cv_first);
        cv_next = findViewById(R.id.cv_next);

        sp_watered = findViewById(R.id.spinner_watered);

        sp_watered.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    wateredValue = null;
                }else{
                    wateredValue = (String) adapterView.getSelectedItem();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_germination = findViewById(R.id.spinner_germination);
        sp_germination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    germinationValue = null;
                }else{
                    germinationValue = (String) adapterView.getSelectedItem();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_weeded = findViewById(R.id.spinner_weeded);
        sp_weeded.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    weededValue = null;
                }else{
                    weededValue = (String) adapterView.getSelectedItem();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        txt_crop_survival_rate = findViewById(R.id.txt_crop_survival_rate);
        txt_flowering = findViewById(R.id.txt_flowering);
        txt_average_pods = findViewById(R.id.txt_average_pods);

        select_block = findViewById(R.id.select_block);

        if(getIntent() != null){
            if(getIntent().getExtras() != null){
                Bundle bundle = getIntent().getExtras();
                returnIntentPlantBlock = (PageItemPlantBlock)  bundle.getSerializable("Message");
                select_block.setText(returnIntentPlantBlock.getBlockName());
            }
        }


    }

    public void searchBlock(View view){
       // select_block.setText("Nyeri");
        Intent intent = new Intent(ScoutingBlockActivity.this, SearchPlantBlockActivity.class);
        intent.putExtra("SearchFor","Scouting");
        startActivity(intent);
    }


    public void nextClick(View view){
        if(!validateFirst()){

           return;
        }


        cv_first.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cv_first.setVisibility(View.GONE);
                cv_next.setVisibility(View.VISIBLE);
                cv_next.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right));

            }
        }, 400);

    }

    public void previousClick(){
        cv_next.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cv_next.setVisibility(View.GONE);
                cv_first.setVisibility(View.VISIBLE);
                cv_first.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_right));

            }
        }, 400);


    }

    public void submit(View view){
        if (!validateNext()) {
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
            hashMap.put("germination", germinationValue.equals("Yes") ? "Y" : "N");
            hashMap.put("weeded",weededValue.equals("Yes") ? "Y" : "N");
            hashMap.put("watered", wateredValue.equals("Yes") ? "Y" : "N");
            hashMap.put("survivalRate",txt_crop_survival_rate.getText().toString());
            hashMap.put("floweringRate", txt_flowering.getText().toString());
            hashMap.put("averagePods",txt_average_pods.getText().toString());
            hashMap.put("locale","en");

            Log.d(this.getPackageName().toUpperCase(), "Before Submitting: " + hashMap.toString());


            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
            APIService service = retrofit.create(APIService.class);
            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
            Call<ResponseBody> call = service.postScoutMonitor(auth_key, hashMap);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        if (response.isSuccessful()) {
                            pDialog.dismissWithAnimation();
                            new SweetAlertDialog(ScoutingBlockActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success")
                                    .setContentText("Successfully submitted.")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            startActivity(new Intent(ScoutingBlockActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

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
                            new SweetAlertDialog(ScoutingBlockActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Try again")
                                    .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                    .show();


                        }
                    } catch (Exception e) {
                        pDialog.dismissWithAnimation();
                        new SweetAlertDialog(ScoutingBlockActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Try again")
                                .setContentText(e.getMessage())
                                .show();
                    }
                    //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pDialog.dismissWithAnimation();
                    new SweetAlertDialog(ScoutingBlockActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Try again...")
                            .setContentText(t.getMessage())
                            .show();
                }
            });
        }
    }


    private boolean validateNext(){
        boolean valid = true;
        int errorColor;
        final int version = Build.VERSION.SDK_INT;

        //Get the defined errorColor from color resource.
        if (version >= 23) {
            errorColor = ContextCompat.getColor(getApplicationContext(), R.color.errorColor);
        } else {
            errorColor = getResources().getColor(R.color.errorColor);
        }

        if (txt_crop_survival_rate.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("Please Input a value in Crop Survival Rate (%) Field");
            ssbuilder.setSpan(fgcspan, 0, "Please Input a value in Crop Survival Rate (%) Field".length(), 0);
            txt_crop_survival_rate.setError(ssbuilder);
            valid = false;
        } else {
            txt_crop_survival_rate.setError(null);
        }

        if (txt_flowering.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("Please Input a value in Flowering (%) Field.");
            ssbuilder.setSpan(fgcspan, 0, "Please Input a value in Flowering (%) Field".length(), 0);
            txt_flowering.setError(ssbuilder);
            valid = false;
        } else {
            txt_flowering.setError(null);
        }

        if (txt_average_pods.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("Please Input a value in Average Pods (%) Field.");
            ssbuilder.setSpan(fgcspan, 0, "Please Input a value in Average Pods (%) Field".length(), 0);
            txt_average_pods.setError(ssbuilder);
            valid = false;
        } else {
            txt_average_pods.setError(null);
        }


        return valid;
    }



    private boolean validateFirst() {
        boolean valid = true;


        if( select_block.getText().toString().isEmpty()){
            (findViewById(R.id.select_block_layout)).setBackground(getResources().getDrawable(R.drawable.shake_search_bg));
            (findViewById(R.id.select_block_layout)).startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            valid = false;
        }else{
            (findViewById(R.id.select_block_layout)).setBackground(getResources().getDrawable(R.drawable.search_bg));
        }


        if( wateredValue == null){
            sp_watered.setBackground(getResources().getDrawable(R.drawable.shake_spinner_bg));
            sp_watered.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            valid = false;
        }else{
            sp_watered.setBackground(getResources().getDrawable(R.drawable.spinner_backgroung));
        }

        if(germinationValue == null){
            sp_germination.setBackground(getResources().getDrawable(R.drawable.shake_spinner_bg));
            sp_germination.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            valid = false;
        }else{
            sp_germination.setBackground(getResources().getDrawable(R.drawable.spinner_backgroung));
        }

        if(weededValue == null){
            sp_weeded.setBackground(getResources().getDrawable(R.drawable.shake_spinner_bg));
            sp_weeded.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            valid = false;
        }else{
            sp_weeded.setBackground(getResources().getDrawable(R.drawable.spinner_backgroung));
        }


        return valid;
    }


    @Override
    public void onBackPressed() {
        if(cv_first.getVisibility() == View.VISIBLE)
            super.onBackPressed();
        else
            previousClick();

    }
}
