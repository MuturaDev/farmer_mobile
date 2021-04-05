package com.cw.farmer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.farmer.HandleConnectionAppCompatActivity;
import com.cw.farmer.NetworkUtil;
import com.cw.farmer.R;
import com.cw.farmer.adapter.SearchSearchAreaBlockAdapter;
import com.cw.farmer.model.SearchAreaResponse;
import com.cw.farmer.model.PageItemSearchArea;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.cw.farmer.offlinefunctions.OfflineFeature;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

public class SearchSearchAreaActivity extends HandleConnectionAppCompatActivity {
    RecyclerView rv_register;
    SearchSearchAreaBlockAdapter registerAdapter;

    ArrayList<PageItemSearchArea> pageItemArrayList;
    FloatingActionButton fab;
    EditText farmer_search;


    private int page=0;
    private int limit=15;
    private int offset=0;
    private boolean end=false;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_search_activity_layout);

        ((ImageView) findViewById(R.id.general_activity_image)).setImageResource(R.drawable.plant_block);
        ((TextView) findViewById(R.id.general_activity_title)).setText("Plant Block");

        progressBar = findViewById(R.id.progress);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        rv_register = findViewById(R.id.search_recylerview);
        rv_register.setLayoutManager(new LinearLayoutManager(this));
        farmer_search = findViewById(R.id.farmer_search);
        farmer_search.setHint("Type Area");
//        farmer_search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if(editable.length() == 1)
//                    getData();//this is just to cache the data, not to request for each text change/each letter
//                else
//                    setData();
//            }
//        });




    }



    public void search(View v){

        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
            getData();
        } else {
//            pageItemArrayList = getArrayList("SearchSearchAreaActivity");
            pageItemArrayList = (ArrayList<PageItemSearchArea>) OfflineFeature.getSharedPreferencesObject("SearchSearchAreaActivity", getApplicationContext(),PageItemSearchArea.class );
            setData();
        }
    }
    private void getData() {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
        APIService service = retrofit.create(APIService.class);
        SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
        String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
       // Log.d("Search","Searching Harvest data..icon.....");
        Call<SearchAreaResponse> call = service.getSearchArea(limit, offset, auth_key);
        call.enqueue(new Callback<SearchAreaResponse>() {
            @Override
            public void onResponse(Call<SearchAreaResponse> call, Response<SearchAreaResponse> response) {
                progressBar.setVisibility(View.GONE);
                try {
                    Log.d("Plant...",response.body().toString());
                    System.out.println(response.body().getPageItemSearchAreaList());

                    if (response.body().getPageItemSearchAreaList().size() > 0){
                        pageItemArrayList = (ArrayList<PageItemSearchArea>) response.body().getPageItemSearchAreaList();
                       // saveArrayList(pageItemArrayList, "SearchSearchAreaActivity");
                        setData();
                    }else {
                       // Toast.makeText(SearchHarvestFarmerActivity.this, "Data Not Found", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                   // Utility.showToast(SearchHarvestFarmerActivity.this, e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<SearchAreaResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setData() {


        if(farmer_search.getText().toString().isEmpty()){
            registerAdapter = new SearchSearchAreaBlockAdapter(rv_register, SearchSearchAreaActivity.this, pageItemArrayList);
            DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
            rv_register.addItemDecoration(itemDecor);
            rv_register.setAdapter(registerAdapter);
        }else{

            ArrayList<PageItemSearchArea> filteredList = new ArrayList<>();
            filteredList.clear();

            for(PageItemSearchArea item : pageItemArrayList){
                if(item.getFarmName().toLowerCase().contains(farmer_search.getText().toString().toLowerCase())){
                    filteredList.add(item);
                }
            }

            registerAdapter = new SearchSearchAreaBlockAdapter(rv_register, SearchSearchAreaActivity.this, filteredList);
            DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
            rv_register.addItemDecoration(itemDecor);
            rv_register.setAdapter(registerAdapter);

        }


    }

    public void saveArrayList(ArrayList<PageItemSearchArea> list, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }


//    public ArrayList<PageItemSearchArea> getArrayList(String key) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        Gson gson = new Gson();
//        String json = prefs.getString(key, null);
//        Type type = new TypeToken<ArrayList<PageItemHarvest>>() {
//        }.getType();
//        return gson.fromJson(json, type);
//    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SearchSearchAreaActivity.this, HomeActivity.class));
        finish();
    }
}

