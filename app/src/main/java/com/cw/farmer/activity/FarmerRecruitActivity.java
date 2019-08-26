package com.cw.farmer.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cw.farmer.R;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.AllResponse;
import com.cw.farmer.model.BlacklistPostResponse;
import com.cw.farmer.model.BlacklistResponse;
import com.cw.farmer.model.CropDateResponse;
import com.cw.farmer.model.PageItem;
import com.cw.farmer.model.RegisterResponse;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.Listener;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.easywaylocation.EasyWayLocation.LOCATION_SETTING_REQUEST_CODE;

public class FarmerRecruitActivity extends AppCompatActivity implements Listener {
    EditText farmer,noofunits;
    Spinner cropdate,section,farmerspinner;
    private RadioGroup radioland;
    private RadioButton radioSexButton;
    EasyWayLocation easyWayLocation;
    private TextView location, latLong, diff;
    private Double lati, longi;
    String location_str,coordinates;
    ProgressDialog progressDialog;
    String farmer_id_string;
    List<Integer> crop_id;
    List<Integer> farmer_id;
    ArrayList<PageItem> pageItemArrayList;
    ArrayList<PageItem> array_sort;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_recruit);
        progressDialog = new ProgressDialog(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        easyWayLocation = new EasyWayLocation(this, false,this);
        farmer=findViewById(R.id.farmer);
        cropdate=findViewById(R.id.cropdate);
        section=findViewById(R.id.section);
        noofunits=findViewById(R.id.noofunits);
        radioland = findViewById(R.id.radioland);
        coordinates="45,67";
        location_str="Githurai East";

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String name =(String) b.get("name");
            farmer.setText(name);

            String id =(String) b.get("id");
            farmer_id_string=id;

        }
        SharedPreferences prefs = getSharedPreferences("recruit_farmer", MODE_PRIVATE);
        noofunits.setText(prefs.getString("noofunits", " "));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor searcheditor = getSharedPreferences("search", MODE_PRIVATE).edit();
                searcheditor.putString("activity","recruit");
                searcheditor.apply();

                SharedPreferences.Editor editor = getSharedPreferences("recruit_farmer", MODE_PRIVATE).edit();
                editor.putString("cropdate",cropdate.getSelectedItem().toString());
                editor.putString("section",section.getSelectedItem().toString());
                editor.putString("noofunits",noofunits.getText().toString());
                editor.apply();

                Intent intent = new Intent(FarmerRecruitActivity.this, SearchFarmerActivity.class);
                startActivity(intent);
            }
        });
        progressDialog.setCancelable(false);
        // progressBar.setMessage("Please Wait...");
        progressDialog.show();
        Retrofit retrofit = ApiClient.getClient("/authentication/");
        APIService service = retrofit.create(APIService.class);
        Call<List<CropDateResponse>> call = service.getcropdate();
        call.enqueue(new Callback<List<CropDateResponse>>() {
            @Override
            public void onResponse(Call<List<CropDateResponse>> call, Response<List<CropDateResponse>> response) {
                progressDialog.hide();
                ArrayList<String> spinnerArray = new ArrayList<String>();
                spinnerArray.clear();
                crop_id = new ArrayList<Integer>();
                for(CropDateResponse blacklist: response.body()) {
                    String date="";
                    for(int elem : blacklist.getPlantingDate()){
                        date=elem+"-"+date;
                    }

                    spinnerArray.add(removeLastChar(date)+"("+blacklist.getProdId()+")");
                    crop_id.add(blacklist.getId());
                }
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(FarmerRecruitActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                cropdate.setAdapter(spinnerArrayAdapter);
            }

            @Override
            public void onFailure(Call<List<CropDateResponse>> call, Throwable t) {
                progressDialog.hide();
                Toast.makeText(FarmerRecruitActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
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

        if (cropdate == null && cropdate.getSelectedItem() ==null) {

            TextView errorText = (TextView)cropdate.getSelectedView();
            errorText.setError("must input Crop date");
            errorText.setTextColor(errorColor);//just to highlight that this is an error

            valid = false;
        }
        if (section == null && section.getSelectedItem() ==null) {

            TextView errorText = (TextView)section.getSelectedView();
            errorText.setError("must input section");
            errorText.setTextColor(errorColor);//just to highlight that this is an error

            valid = false;
        }
        if (section == null && section.getSelectedItem() ==null) {

            TextView errorText = (TextView)section.getSelectedView();
            errorText.setError("must input section");
            errorText.setTextColor(errorColor);//just to highlight that this is an error

            valid = false;
        }
        if (noofunits.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("must input no of units");
            ssbuilder.setSpan(fgcspan, 0, "must input no of units".length(), 0);
            noofunits.setError(ssbuilder);
            valid = false;
        } else {
            noofunits.setError(null);
        }
        int selectedId = radioland.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        radioSexButton = (RadioButton) findViewById(selectedId);
        String landop =radioSexButton.getText()+"";
        if (landop.isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("must input no of units");
            ssbuilder.setSpan(fgcspan, 0, "must input no of units".length(), 0);
            radioSexButton.setError(ssbuilder);
            valid = false;
        } else {
            radioSexButton.setError(null);
        }




        return valid;
    }
    public void submit(View v){
        if (!validate()) {
            Snackbar.make(v, "Correct the above errors first", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        int selectedId = radioland.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        radioSexButton = (RadioButton) findViewById(selectedId);
        String landop =radioSexButton.getText().toString();
        String land="M";

        if (landop.equals("Owned")){
            land="O";
        }else{
            land="L";
        }
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Recruiting...");
        pDialog.setCancelable(false);
        pDialog.show();
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("farmerid",farmer_id_string);
        hashMap.put("dateid",crop_id.get(cropdate.getSelectedItemPosition()).toString());
        hashMap.put("landownership",land);
        hashMap.put("cordinates",coordinates);
        hashMap.put("location",location_str);
        hashMap.put("noofunits",noofunits.getText().toString().trim());
        hashMap.put("section",section.getSelectedItem().toString().trim());

        Retrofit retrofit = ApiClient.getClient("/authentication/");
        APIService service = retrofit.create(APIService.class);
        Call<AllResponse> call = service.recruit("Basic YWRtaW46bWFudW5pdGVk",hashMap);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                progressDialog.hide();
                try {
                if (response.body()!=null){
                    new SweetAlertDialog(FarmerRecruitActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText("You have successfully submitted farmer recruitment details")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    startActivity(new Intent(FarmerRecruitActivity.this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                }
                            })
                            .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    startActivity(new Intent(FarmerRecruitActivity.this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                }
                            })
                            .show();

                }else{
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    new SweetAlertDialog(FarmerRecruitActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Ooops...")
                            .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                            .show();

                }
            } catch (Exception e) {
                new SweetAlertDialog(FarmerRecruitActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText(e.getMessage())
                        .show();
            }
                //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                pDialog.cancel();
                new SweetAlertDialog(FarmerRecruitActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText(t.getMessage())
                        .show();
            }
        });
    }

    @Override
    public void locationOn() {
        Toast.makeText(this, "Location ON", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void currentLocation(Location location) {
        StringBuilder data = new StringBuilder();
        data.append(location.getLatitude());
        data.append(" , ");
        data.append(location.getLongitude());
        coordinates= String.valueOf(data);
        Toast.makeText(this, coordinates, Toast.LENGTH_SHORT).show();
        Geocoder geocoder = new Geocoder(FarmerRecruitActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getSubAdminArea();

            Log.v("IGA", "Address" + add);
            location_str=add;
            Toast.makeText(this, location_str, Toast.LENGTH_SHORT).show();
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void locationCancelled() {
        Toast.makeText(this, "Location Cancelled", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOCATION_SETTING_REQUEST_CODE:
                easyWayLocation.onActivityResult(resultCode);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        easyWayLocation.startLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        easyWayLocation.endUpdates();

    }
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

}
