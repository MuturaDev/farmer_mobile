package com.cw.farmer.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.farmer.HandleConnectionAppCompatActivity;
import com.cw.farmer.NetworkUtil;
import com.cw.farmer.R;
import com.cw.farmer.adapter.RequistionAdapter;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.CentreRequiste;
import com.cw.farmer.model.PlantingVerificationResponse;
import com.cw.farmer.model.RequisitionResponse;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.cw.farmer.offlinefunctions.OfflineFeature;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

public class ReturnActivity extends HandleConnectionAppCompatActivity{
    
        String entity_id,crop_date,type,centrename,taskID;
        RecyclerView rv_register;
        RequistionAdapter registerAdapter;
        ProgressDialog progressDialog;
        ArrayList<RequisitionResponse> pageItemArrayList;
        CentreRequiste centremain;


        private TextView tv_centre,tv_crop_date,tv_farmer_name,tv_no_of_units;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        rv_register = findViewById(R.id.requistion_list);
        rv_register.setLayoutManager(new LinearLayoutManager(this));
        centremain = new CentreRequiste();





        if(b!=null) {
            taskID = (String) b.get("taskID");
            entity_id=(String) b.get("entityId");
            crop_date=(String) b.get("crop_date");
            type=(String) b.get("type");
            centrename=(String) b.get("centrename");


            StringBuilder sb = new StringBuilder();
            sb.append(" EntityID: " + entity_id);
            sb.append(" CropDate: " + crop_date);
            sb.append(" Type: "+ type);
            sb.append(" Centrename: " + centrename);



            Log.d(ReturnActivity.this.getPackageName().toUpperCase(), sb.toString());
        }

        tv_centre = findViewById(R.id.tv_centre);
        tv_crop_date = findViewById(R.id.tv_crop_date);
        tv_farmer_name = findViewById(R.id.tv_farmer_name);
        tv_no_of_units = findViewById(R.id.tv_no_of_units);

//        TextView tv_centre= findViewById(R.id.tv_centre);
//        tv_centre.setText(centrename);
//        TextView tv_crop_date= findViewById(R.id.tv_crop_date);
//        tv_crop_date.setText(crop_date);
//        TextView tv_type= findViewById(R.id.tv_type);
//        tv_type.setText(type);


            if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
                progressDialog = new ProgressDialog(this);

                progressDialog.setCancelable(false);
                // progressBar.setMessage("Please Wait...");
                progressDialog.show();

                Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                APIService service = retrofit.create(APIService.class);
                SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                Call<PlantingVerificationResponse> call = service.getPlantingVerfication(entity_id, auth_key);

                call.enqueue(new Callback<PlantingVerificationResponse>() {
                    @Override
                    public void onResponse(Call<PlantingVerificationResponse> call, Response<PlantingVerificationResponse> response) {
                        progressDialog.hide();

                        if (response.body() != null) {
                            Log.d(ReturnActivity.this.getPackageName().toUpperCase(), response.body().toString());

                            tv_centre.setText(response.body().getCentreName());
                            tv_crop_date.setText(response.body().getCropDate().get(2) + "/" + response.body().getCropDate().get(1) + "/" + response.body().getCropDate().get(0));
                            tv_farmer_name.setText(response.body().getFarmerName());
                            tv_no_of_units.setText(response.body().getContractUnits());

                            //  TextView tv_centre= findViewById(R.id.tv_centre);
//        tv_centre.setText(centrename);
//        TextView tv_crop_date= findViewById(R.id.tv_crop_date);
//        tv_crop_date.setText(crop_date);
////        TextView tv_type= findViewById(R.id.tv_type);
////        tv_type.setText(type);

                            //What was there before
//                    //if(response.body().size() > 0){
//                            pageItemArrayList = (ArrayList<RequisitionResponse>) response.body();
//                        for (RequisitionResponse pageitem : response.body()) {
//
//                            Centrestore centersub = new Centrestore();
//                            centersub.setCentreid(pageitem.getCentreid() + " ");
//                            centersub.setInvid(pageitem.getInvId() + " ");
//                            centersub.setQty((int) pageitem.getQuantity() + " ");
//
//                            centremain.setCentrestores(centersub);
//                        }
//                            setData();

                        } else {

                            Utility.showToast(ReturnActivity.this, "No requisition details");
                        }


                    }

                    @Override
                    public void onFailure(Call<PlantingVerificationResponse> call, Throwable t) {
                        progressDialog.hide();
                        Utility.showToast(ReturnActivity.this, t.getMessage());
                        Log.e(ReturnActivity.this.getPackageName().toUpperCase(), t.getMessage());
                    }
                });
            }else{

                HashMap hashMap = (HashMap) OfflineFeature.getSharedPreferences("ReturnActivityOffline",getApplicationContext(),HashMap.class);

                tv_centre.setText(hashMap.get("centre").toString());
                tv_crop_date.setText(hashMap.get("cropDate").toString());
                tv_farmer_name.setText(hashMap.get("farmerName").toString());
                tv_no_of_units.setText(hashMap.get("noOfUnits").toString());

            }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
        private void setData() {
        registerAdapter = new RequistionAdapter(rv_register,ReturnActivity.this, pageItemArrayList);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
        rv_register.addItemDecoration(itemDecor);
        rv_register.setAdapter(registerAdapter);

    }

        private void setFormat(String format){

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

        try{


        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

        public void submit_requsition(View v) {

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Submitting...");
        pDialog.setCancelable(false);
        pDialog.show();

        Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
        APIService service = retrofit.create(APIService.class);
        SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
        String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
        Call<ResponseBody> call = service.postReturnStock(taskID,auth_key);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.hide();
                try {
                    if (response.isSuccessful()) {
                        new SweetAlertDialog(ReturnActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success")
                                .setContentText("Successfully submitted.")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        startActivity(new Intent(ReturnActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                    }
                                })
//                                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sDialog) {
//                                        sDialog.dismissWithAnimation();
//                                        startActivity(new Intent(ReturnActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//
//                                    }
//                                })
                                .show();

                    } else {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        new SweetAlertDialog(ReturnActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Ooops...")
                                .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                .show();

                    }
                } catch (Exception e) {
                    new SweetAlertDialog(ReturnActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(e.getMessage())
                            .show();
                }
                //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.cancel();
                new SweetAlertDialog(ReturnActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText(t.getMessage())
                        .show();
            }
        });
    }


}

