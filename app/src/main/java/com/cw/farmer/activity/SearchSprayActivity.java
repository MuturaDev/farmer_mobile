package com.cw.farmer.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.farmer.HandleConnectionAppCompatActivity;
import com.cw.farmer.NetworkUtil;
import com.cw.farmer.R;
import com.cw.farmer.adapter.SearchSprayAdapter;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.PageItemsSprayFarmer;
import com.cw.farmer.model.SprayFarmerResponse;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.cw.farmer.utils.OfflineFeature;
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

public class SearchSprayActivity extends HandleConnectionAppCompatActivity {

    RecyclerView rv_register;
    SearchSprayAdapter registerAdapter;
    ProgressDialog progressDialog;
    ArrayList<PageItemsSprayFarmer> pageItemArrayList;
    FloatingActionButton fab;
    EditText farmer_search;
    Button btn_search;
    SharedPreferences sharedPreferences;

    private int page = 0;
    private int limit = 0;
    private int offset = 0;
    private boolean end = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_spray);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rv_register = findViewById(R.id.search_recylerview);
        rv_register.setLayoutManager(new LinearLayoutManager(this));
        farmer_search = findViewById(R.id.farmer_search);
        btn_search = findViewById(R.id.btn_search);
        progressDialog = new ProgressDialog(this);
        //prevents fetching records after the page is open.. and allows fetching records only after clicking on search
        //search();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void search() {
        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
            getData();
        } else {

            String farmerSearch = farmer_search.getText().toString();

            if(farmerSearch.isEmpty()){
                pageItemArrayList = (ArrayList<PageItemsSprayFarmer>) OfflineFeature.getSharedPreferences("sprayfarmer", getApplicationContext(), PageItemsSprayFarmer.class);
            }else{
                pageItemArrayList = new ArrayList<>();
                pageItemArrayList.clear();
                for(PageItemsSprayFarmer item : (ArrayList<PageItemsSprayFarmer>) OfflineFeature.getSharedPreferences("sprayfarmer", getApplicationContext(), PageItemsSprayFarmer.class)){
                    if(item.getFarmerName().toLowerCase().contains(farmerSearch.toLowerCase())){
                        pageItemArrayList.add(item);
                    }
                }
            }

            setData();
        }
    }

    private void getData() {
        progressDialog.setCancelable(false);
        // progressBar.setMessage("Please Wait...");
        progressDialog.show();
        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
            APIService service = retrofit.create(APIService.class);
            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
            Call<SprayFarmerResponse> call = service.getSprayfarmer(limit, offset, farmer_search.getText().toString(), auth_key);
            call.enqueue(new Callback<SprayFarmerResponse>() {
                @Override
                public void onResponse(Call<SprayFarmerResponse> call, Response<SprayFarmerResponse> response) {
                    progressDialog.hide();
                    try {
                        if (response.body().getPageItemsSprayFarmer().size() != 0) {
                            pageItemArrayList = (ArrayList<PageItemsSprayFarmer>) response.body().getPageItemsSprayFarmer();
                            saveArrayList(pageItemArrayList, "sprayfarmer");
                            setData();
                        } else {
                            Toast.makeText(SearchSprayActivity.this, "Data Not Found", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Utility.showToast(SearchSprayActivity.this, e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<SprayFarmerResponse> call, Throwable t) {
                    progressDialog.hide();
                    Utility.showToast(SearchSprayActivity.this, t.getMessage());
                }
            });
        } else {
            pageItemArrayList = (ArrayList<PageItemsSprayFarmer>) OfflineFeature.getSharedPreferences("sprayfarmer", getApplicationContext(), PageItemsSprayFarmer.class);
            setData();
            progressDialog.hide();
        }

    }

    private void setData() {
        registerAdapter = new SearchSprayAdapter(rv_register, SearchSprayActivity.this, pageItemArrayList);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
        rv_register.addItemDecoration(itemDecor);
        rv_register.setAdapter(registerAdapter);

    }

    public void saveArrayList(ArrayList<PageItemsSprayFarmer> list, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

//    public ArrayList<PageItemsSprayFarmer> getArrayList(String key) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        Gson gson = new Gson();
//        String json = prefs.getString(key, null);
//        Type type = new TypeToken<ArrayList<PageItemsSprayFarmer>>() {
//        }.getType();
//        return gson.fromJson(json, type);
//    }

    public void search_button(View v) {
        search();
    }

}
