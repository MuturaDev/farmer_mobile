package com.cw.farmer.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.farmer.NetworkUtil;
import com.cw.farmer.R;
import com.cw.farmer.adapter.SearchHarvestAdapter;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.FarmerHarvestResponse;
import com.cw.farmer.model.PageItemHarvest;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

public class SearchHarvestFarmerActivity extends AppCompatActivity {
    RecyclerView rv_register;
    SearchHarvestAdapter registerAdapter;
    ProgressDialog progressDialog;
    ArrayList<PageItemHarvest> pageItemArrayList;
    FloatingActionButton fab;
    EditText farmer_search;
    Button btn_search;

    private int page=0;
    private int limit=0;
    private int offset=0;
    private boolean end=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_harvest_farmer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rv_register = findViewById(R.id.search_recylerview);
        rv_register.setLayoutManager(new LinearLayoutManager(this));
        farmer_search = findViewById(R.id.farmer_search);
        btn_search = findViewById(R.id.btn_search);
        progressDialog = new ProgressDialog(this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void search(View v){

        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
            Log.d("Search","Searching Harvest data...");
            getData();
        } else {
            pageItemArrayList = getArrayList("harvestfarmer");
            setData();
        }
    }
    private void getData() {
        progressDialog.setCancelable(false);
        // progressBar.setMessage("Please Wait...");
        progressDialog.show();
        Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
        APIService service = retrofit.create(APIService.class);
        SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
        String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
        Log.d("Search","Searching Harvest data..icon.....");
        Call<FarmerHarvestResponse> call = service.getHarvestfarmer(limit, offset, farmer_search.getText().toString(), auth_key);
        call.enqueue(new Callback<FarmerHarvestResponse>() {
            @Override
            public void onResponse(Call<FarmerHarvestResponse> call, Response<FarmerHarvestResponse> response) {
                progressDialog.hide();
                try {
                    Log.d("Data count...",String.valueOf(response.body().getPageItemHarvest().size()));
                    System.out.println(response.body().getPageItemHarvest());

                    if (response.body().getPageItemHarvest().size()!=0){
                        pageItemArrayList = (ArrayList<PageItemHarvest>) response.body().getPageItemHarvest();
                        saveArrayList(pageItemArrayList, "harvestfarmer");
                        setData();
                    }else {
                        Toast.makeText(SearchHarvestFarmerActivity.this, "Data Not Found", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Utility.showToast(SearchHarvestFarmerActivity.this, e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<FarmerHarvestResponse> call, Throwable t) {
                progressDialog.hide();
                Utility.showToast(SearchHarvestFarmerActivity.this, t.getMessage());
            }
        });
    }

    private void setData() {
        registerAdapter = new SearchHarvestAdapter(rv_register,SearchHarvestFarmerActivity.this, pageItemArrayList);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
        rv_register.addItemDecoration(itemDecor);
        rv_register.setAdapter(registerAdapter);

    }

    public void saveArrayList(ArrayList<PageItemHarvest> list, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public ArrayList<PageItemHarvest> getArrayList(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<PageItemHarvest>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

}
