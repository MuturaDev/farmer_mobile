package com.cw.farmer.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.farmer.R;
import com.cw.farmer.adapter.RequistionAdapter;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.CentreRequiste;
import com.cw.farmer.model.Centrestore;
import com.cw.farmer.model.RequisitionResponse;
import com.cw.farmer.model.RetItem;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

public class RequisitionActivity extends AppCompatActivity {
    String entity_id,crop_date,type,centrename;
    RecyclerView rv_register;
    RequistionAdapter registerAdapter;
    ProgressDialog progressDialog;
    ArrayList<RequisitionResponse> pageItemArrayList;
    CentreRequiste centremain;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisition);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        rv_register = findViewById(R.id.requistion_list);
        rv_register.setLayoutManager(new LinearLayoutManager(this));
        centremain = new CentreRequiste();


        if(b!=null) {
            entity_id=(String) b.get("id");
            crop_date=(String) b.get("crop_date");
            type=(String) b.get("type");
            centrename=(String) b.get("centrename");
        }

        TextView tv_centre= findViewById(R.id.tv_centre);
        tv_centre.setText(centrename);
        TextView tv_crop_date= findViewById(R.id.tv_crop_date);
        tv_crop_date.setText(crop_date);
        TextView tv_type= findViewById(R.id.tv_type);
        tv_type.setText(type);

        progressDialog = new ProgressDialog(this);

        progressDialog.setCancelable(false);
        // progressBar.setMessage("Please Wait...");
        progressDialog.show();
        Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
        APIService service = retrofit.create(APIService.class);
        Call<List<RequisitionResponse>> call = service.getrequsition(entity_id);
        call.enqueue(new Callback<List<RequisitionResponse>>() {
            @Override
            public void onResponse(Call<List<RequisitionResponse>> call, Response<List<RequisitionResponse>> response) {
                progressDialog.hide();
                try {
                    if (response.body() != null){
                            pageItemArrayList = (ArrayList<RequisitionResponse>) response.body();
                        for (RequisitionResponse pageitem : response.body()) {

                            Centrestore centersub = new Centrestore();
                            centersub.setCentreid(pageitem.getCentreid() + " ");
                            centersub.setInvid(pageitem.getInvId() + " ");
                            centersub.setQty((int) pageitem.getQuantity() + " ");

                            centremain.setCentrestores(centersub);
                        }
                            setData();

                    }else{

                        Utility.showToast(RequisitionActivity.this, "No requisition details");
                    }

                } catch (Exception e) {
                    Utility.showToast(RequisitionActivity.this, e.getMessage());
                    Log.w("myApp",  e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<List<RequisitionResponse>> call, Throwable t) {
                progressDialog.hide();
                Utility.showToast(RequisitionActivity.this, t.getMessage());
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void setData() {
        registerAdapter = new RequistionAdapter(rv_register,RequisitionActivity.this, pageItemArrayList);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
        rv_register.addItemDecoration(itemDecor);
        rv_register.setAdapter(registerAdapter);

    }

    public void submit_requsition(View v) {

        JsonArray jsonArray = new JsonArray();
        for (RetItem yes : registerAdapter.retrieveData()) {
            System.out.println(yes.centreid + " Quanity");

            JsonObject student1 = new JsonObject();

            student1.addProperty("invid", yes.invid);
            student1.addProperty("centreid", yes.centreid);
            student1.addProperty("qty", yes.qty);
            student1.addProperty("reqid", yes.reqid);
            student1.addProperty("type", type);
            jsonArray.add(student1);


        }

        JsonObject studentsObj = new JsonObject();
        studentsObj.add("centrestores", jsonArray);

        String jsonStr = studentsObj.toString();

        System.out.println("jsonString: " + jsonStr);

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Submitting...");
        pDialog.setCancelable(false);
        pDialog.show();

        Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
        APIService service = retrofit.create(APIService.class);

        Call<JsonObject> call = service.postreceiveinventory("Basic YWRtaW46bWFudW5pdGVk", studentsObj);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.hide();
                try {
                    if (response.body() != null) {
                        new SweetAlertDialog(RequisitionActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success")
                                .setContentText("You have successfully submitted farmer recruitment details")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        startActivity(new Intent(RequisitionActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                    }
                                })
                                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        startActivity(new Intent(RequisitionActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                    }
                                })
                                .show();

                    } else {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        new SweetAlertDialog(RequisitionActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Ooops...")
                                .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                .show();

                    }
                } catch (Exception e) {
                    new SweetAlertDialog(RequisitionActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(e.getMessage())
                            .show();
                }
                //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                pDialog.cancel();
                new SweetAlertDialog(RequisitionActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText(t.getMessage())
                        .show();
            }
        });
    }


}
