package com.cw.farmer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.cw.farmer.model.AllResponse;
import com.cw.farmer.model.DestructionReasonResponse;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cw.farmer.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HarvestingActivity extends AppCompatActivity {
    EditText farmer_harvesting,crop_weight;
    Spinner codedate_harvesting;
    String farmer_id_string,noofunits,plantingId;
    List<String> cropDateId_list,reason_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harvesting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        farmer_harvesting = findViewById(R.id.farmer_harvesting);
        farmer_harvesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor searcheditor = getSharedPreferences("search", MODE_PRIVATE).edit();
                searcheditor.putString("activity","destruction");
                searcheditor.apply();

                Intent intent = new Intent(HarvestingActivity.this, SearchHarvestFarmerActivity.class);
                startActivity(intent);
            }
        });
        codedate_harvesting = findViewById(R.id.codedate_harvesting);
        crop_weight = findViewById(R.id.crop_weight);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String name =(String) b.get("name");
            farmer_harvesting.setText(name);

            String id =(String) b.get("farmerId");
            farmer_id_string=id;

            String noofunits_now =(String) b.get("totalUnits");
            noofunits=noofunits_now;

            String plantingId_now =(String) b.get("plantingId");
            plantingId=plantingId_now;



            cropDateId_list = new ArrayList<String>();
            String cropDateId =(String) b.get("crop_date");
            cropDateId_list.add(cropDateId);

            ArrayList<String> spinnerArray = new ArrayList<String>();
            spinnerArray.clear();
            String crop_date =(String) b.get("crop_date");
            spinnerArray.add(crop_date);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(HarvestingActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
            codedate_harvesting.setAdapter(spinnerArrayAdapter);




        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        if (crop_weight.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("must input weight");
            ssbuilder.setSpan(fgcspan, 0, "must input weight".length(), 0);
            crop_weight.setError(ssbuilder);
            valid = false;
        } else {
            crop_weight.setError(null);
        }

        if (farmer_harvesting.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("Select a farmer");
            ssbuilder.setSpan(fgcspan, 0, "Select a farmer".length(), 0);
            farmer_harvesting.setError(ssbuilder);
            valid = false;
        } else {
            farmer_harvesting.setError(null);
        }




        return valid;
    }
    public void harvesting_submit(View v){
        if (!validate()) {
            Snackbar.make(v, "Correct the above errors first", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        //{
        //    "dateid":1,  // from lov api shared below
        //    "noofunits":1,  // from lov api shared below
        //    "farmerid":5,  //from lov  api shared below
        //    "Harvestkilos":5 //Number of kilos harvested,
        //    "locale":"en",
        //
        //}
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Submitting Harvesting Data...");
        pDialog.setCancelable(false);
        pDialog.show();
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("dateid",plantingId);
        hashMap.put("noofunits",noofunits);
        hashMap.put("farmerid",farmer_id_string);
        hashMap.put("harvestkilos",crop_weight.getText().toString());
        hashMap.put("locale","en");

        Retrofit retrofit = ApiClient.getClient("/authentication/");
        APIService service = retrofit.create(APIService.class);
        Call<AllResponse> call = service.postharvesting("Basic YWRtaW46bWFudW5pdGVk",hashMap);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                pDialog.dismissWithAnimation();
                try {
                    if (response.body()!=null){
                        new SweetAlertDialog(HarvestingActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success")
                                .setContentText("You have successfully submitted "+farmer_harvesting.getText()+" harvesting details")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        startActivity(new Intent(HarvestingActivity.this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                    }
                                })
                                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        startActivity(new Intent(HarvestingActivity.this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                    }
                                })
                                .show();

                    }else{
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        new SweetAlertDialog(HarvestingActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Ooops...")
                                .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                .show();

                    }
                } catch (Exception e) {
                    new SweetAlertDialog(HarvestingActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(e.getMessage())
                            .show();
                }
                //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                pDialog.cancel();
                new SweetAlertDialog(HarvestingActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText(t.getMessage())
                        .show();
            }
        });
    }

}
