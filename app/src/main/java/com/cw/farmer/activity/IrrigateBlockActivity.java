package com.cw.farmer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.cw.farmer.HandleConnectionAppCompatActivity;
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

public class IrrigateBlockActivity extends HandleConnectionAppCompatActivity {

    private EditText txt_irrigation_hours,txt_cubic_litres;
    private TextView select_block;

    private PageItemPlantBlock returnIntentPlantBlock;

    public void back(View view){
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irrigate_block_layout);


       // enableLocationTracking();

        txt_irrigation_hours = findViewById(R.id.txt_irrigation_hours);
        //txt_irrigation_hours.setText(locationText == null? "Location not Found" : locationText);

        txt_cubic_litres = findViewById(R.id.txt_cubic_litres);
        select_block = findViewById(R.id.select_block);

        if(getIntent() != null){
            if(getIntent().getExtras() != null){
                Bundle bundle = getIntent().getExtras();
                returnIntentPlantBlock = (PageItemPlantBlock)  bundle.getSerializable("Message");
                select_block.setText(returnIntentPlantBlock.getBlockName());
            }
        }

//        CheckNetworkConnectionHelper
//                .getInstance()
//                .registerNetworkChangeListener(new StopReceiveDisconnectedListener() {
//                    @Override
//                    public void onDisconnected() {
//                        //Do your task on Network Disconnected!
//                        Snackbar.make(findViewById(R.id.irrigate_block_layout), "Sorry, not internet connection", Snackbar.LENGTH_LONG).show();
//
//                    }
//
//                    @Override
//                    public void onNetworkConnected() {
//                        //Do your task on Network Connected!
//                        Snackbar.make(findViewById(R.id.irrigate_block_layout), "Internet Connection on", Snackbar.LENGTH_LONG).show();
//
//                    }
//
//                    @Override
//                    public Context getContext() {
//                        return IrrigateBlockActivity.this;
//                    }
//                });



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

        if (txt_irrigation_hours.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("Please Input a value in Irrigation Hours Field");
            ssbuilder.setSpan(fgcspan, 0, "Please Input a value in Irrigation Hours Field".length(), 0);
            txt_irrigation_hours.setError(ssbuilder);
            valid = false;
        } else {
            txt_irrigation_hours.setError(null);
        }

        if (txt_cubic_litres.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("Please Input a value in Cubic Litres Field");
            ssbuilder.setSpan(fgcspan, 0, "Please Input a value in Cubic Litres Field".length(), 0);
            txt_cubic_litres.setError(ssbuilder);
            valid = false;
        } else {
            txt_cubic_litres.setError(null);
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




    public void searchBlock(View view){
        Intent intent = new Intent(IrrigateBlockActivity.this, SearchPlantBlockActivity.class);
        intent.putExtra("SearchFor","Irrigate");
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
            hashMap.put("irrigationHours", txt_irrigation_hours.getText().toString());
            hashMap.put("cubicLitres",txt_cubic_litres.getText().toString());
            hashMap.put("locale","en");


            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
            APIService service = retrofit.create(APIService.class);
            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
            Call<ResponseBody> call = service.postIrrigateBlock(auth_key, hashMap);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        if (response.isSuccessful()) {
                            pDialog.dismissWithAnimation();
                            new SweetAlertDialog(IrrigateBlockActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success")
                                    .setContentText("Successfully submitted.")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            startActivity(new Intent(IrrigateBlockActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

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
                            new SweetAlertDialog(IrrigateBlockActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Try again")
                                    .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                    .show();


                        }
                    } catch (Exception e) {
                        pDialog.dismissWithAnimation();
                        new SweetAlertDialog(IrrigateBlockActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Try again")
                                .setContentText(e.getMessage())
                                .show();
                    }
                    //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pDialog.dismissWithAnimation();
                    new SweetAlertDialog(IrrigateBlockActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Try again...")
                            .setContentText(t.getMessage())
                            .show();
                }
            });
        }
    }




}
