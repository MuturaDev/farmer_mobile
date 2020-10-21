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

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.cw.farmer.HandleConnectionAppCompatActivity;
import com.cw.farmer.NetworkUtil;
import com.cw.farmer.R;
import com.cw.farmer.model.HarvestingDB;
import com.cw.farmer.model.PageItemPlantBlock;
import com.cw.farmer.model.PageItemSearchArea;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.cw.farmer.table_models.HarvestBlockTB;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HarvestBlockActivity extends HandleConnectionAppCompatActivity {

    private TextView select_block;
    private EditText txt_harvest_kilos;
    private PageItemPlantBlock returnHarvestBlock;



    public void back(View view){
        finish();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harvest_block_layout);

        select_block = findViewById(R.id.select_block);
        txt_harvest_kilos = findViewById(R.id.txt_harvest_kilos);

       if(getIntent() != null){
           if(getIntent().getExtras() != null){
               Bundle bundle = getIntent().getExtras();
             returnHarvestBlock = (PageItemPlantBlock)  bundle.getSerializable("Message");
             select_block.setText(returnHarvestBlock.getBlockName());
           }
       }
    }


    public void searchBlock(View view){
        Intent intent = new Intent(HarvestBlockActivity.this, SearchPlantBlockActivity.class);
        intent.putExtra("SearchFor","Harvest");
        startActivity(intent);
    }


//    public void submit(View view){
//        if(validate()){
//            Toast.makeText(this, "Everything is okay", Toast.LENGTH_SHORT).show();
//        }
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

        if (txt_harvest_kilos.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("Please Input a value in Harvest Kilos Field");
            ssbuilder.setSpan(fgcspan, 0, "Please Input a value in Harvest Kilos Field".length(), 0);
            txt_harvest_kilos.setError(ssbuilder);
            valid = false;
        } else {
            txt_harvest_kilos.setError(null);
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
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("harvestKilos", txt_harvest_kilos.getText().toString());
            hashMap.put("blockId", String.valueOf(returnHarvestBlock.getId()));
            hashMap.put("locale","en");//should this be hardcoded?

            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
            APIService service = retrofit.create(APIService.class);
            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
            Call<ResponseBody> call = service.postHarvestBlocks(auth_key, hashMap);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        if (response.isSuccessful()) {
                            pDialog.dismissWithAnimation();
                            new SweetAlertDialog(HarvestBlockActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Confirmation")
                                    .setContentText("Successfully submitted")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            startActivity(new Intent(HarvestBlockActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                        }
                                    })
//                                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
//                                        @Override
//                                        public void onClick(SweetAlertDialog sDialog) {
//                                            sDialog.dismissWithAnimation();
//                                            startActivity(new Intent(HarvestBlockActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//
//                                        }
//                                    })
                                    .show();

                        } else {
                            pDialog.dismissWithAnimation();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            new SweetAlertDialog(HarvestBlockActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Try Again")
                                   // .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                    .show();


                        }
                    } catch (Exception e) {
                        pDialog.dismissWithAnimation();
                        new SweetAlertDialog(HarvestBlockActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Try Again")
                                //.setContentText(e.getMessage())
                                .show();
                    }
                    //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    pDialog.dismissWithAnimation();
//                    new SweetAlertDialog(HarvestBlockActivity.this, SweetAlertDialog.ERROR_TYPE)
//                            .setTitleText("Oops...")
//                            .setContentText(t.getMessage())
//                            .show();
                }
            });
        }else{
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("harvestKilos", txt_harvest_kilos.getText().toString());
            hashMap.put("blockId", String.valueOf(returnHarvestBlock.getId()));
            hashMap.put("locale","en");//should this be hardcoded?

            HarvestBlockTB harvest = new HarvestBlockTB(
                    hashMap.get("harvestKilos"),
                    hashMap.get("blockId"),
                    hashMap.get("locale")
            );

            harvest.save();

            new SweetAlertDialog(HarvestBlockActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("No Wrong")
                    .setContentText("We have saved the data offline, We will submitted it when you have internet")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            startActivity(new Intent(HarvestBlockActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                        }
                    })
                    .show();

        }


    }





}
