package com.cw.farmer.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cw.farmer.HandleConnectionAppCompatActivity;
import com.cw.farmer.NetworkUtil;
import com.cw.farmer.R;
import com.cw.farmer.model.CropDateDB;
import com.cw.farmer.model.CropDateResponse;
import com.cw.farmer.model.DashboardResponse;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyDashboardActivity extends HandleConnectionAppCompatActivity {
    TextView farmer_planting_not_verify, farmer_kilo_harvest, farmer_recruit_no, farmer_contract_signed, farmer_planting_verify;
    Spinner cropdate;
    List<Integer> crop_id;
    ProgressDialog progressDialog;

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        farmer_planting_not_verify = findViewById(R.id.farmer_planting_not_verify);
        farmer_kilo_harvest = findViewById(R.id.farmer_kilo_harvest);
        farmer_recruit_no = findViewById(R.id.farmer_recruit_no);
        farmer_contract_signed = findViewById(R.id.farmer_contract_signed);
        farmer_planting_verify = findViewById(R.id.farmer_planting_verify);
        cropdate = findViewById(R.id.cropdate);

        ArrayList<String> spinnerArray = new ArrayList<String>();
        spinnerArray.clear();
        crop_id = new ArrayList<Integer>();
        List<CropDateDB> crops = CropDateDB.listAll(CropDateDB.class);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(MyDashboardActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
            progressDialog.setCancelable(false);
            // progressBar.setMessage("Please Wait...");
            progressDialog.show();
            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
            APIService service = retrofit.create(APIService.class);
            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
            Call<List<CropDateResponse>> call = service.getcropdate(auth_key);
            call.enqueue(new Callback<List<CropDateResponse>>() {
                @Override
                public void onResponse(Call<List<CropDateResponse>> call, Response<List<CropDateResponse>> response) {
                    progressDialog.hide();
                    ArrayList<String> spinnerArray = new ArrayList<String>();
                    spinnerArray.clear();
                    crop_id = new ArrayList<Integer>();
                    CropDateDB.deleteAll(CropDateDB.class);
                    for (CropDateResponse blacklist : response.body()) {
                        String date = "";
                        for (int elem : blacklist.getPlantingDate()) {
                            date = elem + "-" + date;
                        }
                        CropDateDB book = new CropDateDB(blacklist.getId(), blacklist.getProdId(), blacklist.getProdname(), date);
                        book.save();

                        spinnerArray.add(removeLastChar(date) + "(" + blacklist.getProdId() + ")");
                        crop_id.add(blacklist.getId());
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(MyDashboardActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                    cropdate.setAdapter(spinnerArrayAdapter);
                    cropdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            SweetAlertDialog pDialog = new SweetAlertDialog(MyDashboardActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Fetching details...");
                            pDialog.setCancelable(false);
                            pDialog.show();
                            if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {

                                Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                                APIService service = retrofit.create(APIService.class);
                                SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                                String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                                Call<DashboardResponse> call = service.getdashboard(crop_id.get(position) + "", auth_key);
                                call.enqueue(new Callback<DashboardResponse>() {
                                    @Override
                                    public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                                        //progressDialog.hide();
                                        try {
                                            pDialog.dismissWithAnimation();
                                            Toast.makeText(MyDashboardActivity.this, response.body().getPlantingNotVerified() + " bba", Toast.LENGTH_LONG).show();
                                            farmer_planting_not_verify.setText(response.body().getPlantingNotVerified() + " ");
                                            farmer_kilo_harvest.setText(response.body().getKilosHarvested() + "");
                                            farmer_recruit_no.setText(response.body().getTotalRecruited() + " ");
                                            farmer_contract_signed.setText(response.body().getContractsSigned() + " ");
                                            farmer_planting_verify.setText(response.body().getPlantingVerified() + " ");
                                        } catch (Exception e) {
                                            pDialog.dismissWithAnimation();
                                            new SweetAlertDialog(MyDashboardActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Oops...")
                                                    .setContentText(e.getMessage())
                                                    .show();
                                        }
                                        //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void onFailure(Call<DashboardResponse> call, Throwable t) {
                                        pDialog.dismissWithAnimation();
                                        new SweetAlertDialog(MyDashboardActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("Oops...")
                                                .setContentText(t.getMessage())
                                                .show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }

                    });
                }

                @Override
                public void onFailure(Call<List<CropDateResponse>> call, Throwable t) {
                    progressDialog.hide();
                    Toast.makeText(MyDashboardActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            spinnerArray = new ArrayList<String>();
            spinnerArray.clear();
            crop_id = new ArrayList<Integer>();
            crops = CropDateDB.listAll(CropDateDB.class);
            for (CropDateDB crop : crops) {
                spinnerArray.add(removeLastChar(crop.plantingDate) + "(" + crop.prodId + ")");
                crop_id.add(crop.id_crop);
            }
            spinnerArrayAdapter = new ArrayAdapter<String>(MyDashboardActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
            cropdate.setAdapter(spinnerArrayAdapter);
            cropdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    SweetAlertDialog pDialog = new SweetAlertDialog(MyDashboardActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Fetching details...");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {

                        Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                        APIService service = retrofit.create(APIService.class);
                        SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                        String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                        Call<DashboardResponse> call = service.getdashboard(crop_id.get(position) + "", auth_key);
                        call.enqueue(new Callback<DashboardResponse>() {
                            @Override
                            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                                progressDialog.hide();
                                try {
                                    if (response.body() != null) {
                                        pDialog.dismissWithAnimation();
                                        farmer_planting_not_verify.setText(response.body().getPlantingNotVerified());
                                        farmer_kilo_harvest.setText(response.body().getKilosHarvested() + "");
                                        farmer_recruit_no.setText(response.body().getTotalRecruited());
                                        farmer_contract_signed.setText(response.body().getContractsSigned());
                                        farmer_planting_verify.setText(response.body().getPlantingVerified());

                                    } else {
                                        pDialog.dismissWithAnimation();
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        new SweetAlertDialog(MyDashboardActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText("Ooops...")
                                                .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                                .show();


                                    }
                                } catch (Exception e) {
                                    pDialog.dismissWithAnimation();
                                    new SweetAlertDialog(MyDashboardActivity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText(e.getMessage())
                                            .show();
                                }
                                //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onFailure(Call<DashboardResponse> call, Throwable t) {
                                pDialog.dismissWithAnimation();
                                new SweetAlertDialog(MyDashboardActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText(t.getMessage())
                                        .show();
                            }
                        });
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
        }

    }

}
