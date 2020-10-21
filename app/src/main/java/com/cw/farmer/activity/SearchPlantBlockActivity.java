package com.cw.farmer.activity;

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
import com.cw.farmer.adapter.SearchPlantBlockAdapter;
import com.cw.farmer.model.PageItemHarvest;
import com.cw.farmer.model.PageItemPlantBlock;
import com.cw.farmer.model.PlantBlockResponse;
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

public class SearchPlantBlockActivity   extends HandleConnectionAppCompatActivity {
    RecyclerView rv_register;
    SearchPlantBlockAdapter registerAdapter;
   // ProgressDialog progressDialog;
    ArrayList<PageItemPlantBlock> pageItemArrayList;
    FloatingActionButton fab;
    EditText farmer_search;


    private int page=0;
    private int limit=15;
    private int offset=0;
    private boolean end=false;

    private ProgressBar progressBar;

    public String searchFor;
    private ImageView generalActivityImage;
    private TextView generalActivityTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_search_activity_layout);

        generalActivityImage = findViewById(R.id.general_activity_image);
        generalActivityTitle = findViewById(R.id.general_activity_title);

        if(getIntent() != null){
            if(getIntent().getExtras() != null){
                Bundle bundle = getIntent().getExtras();
               searchFor = bundle.getString("SearchFor");
            }
        }


        if(searchFor != null) {
            if (!searchFor.isEmpty()) {

                if (searchFor.equalsIgnoreCase("Irrigate")) {
                    generalActivityImage.setImageResource(R.drawable.irrigate);
                    generalActivityTitle.setText("Irrigate Block");
                } else if (searchFor.equalsIgnoreCase("Scouting")) {
                    generalActivityImage.setImageResource(R.drawable.monitor);
                    generalActivityTitle.setText("Scouting/Monitoring");
                } else if (searchFor.equalsIgnoreCase("ApplyFertilizer")) {
                   generalActivityImage.setImageResource(R.drawable.fertilizer);
                    generalActivityTitle.setText("Apply Fertilizer");
                }else if(searchFor.equalsIgnoreCase("Harvest")){
                    generalActivityImage.setImageResource(R.drawable.harvest_block);
                    generalActivityTitle.setText("Harvest Block");
                }

            } else {
                generalActivityImage.setImageResource(R.drawable.plant_block);
                generalActivityTitle.setText("Plant Block");
            }
        }else{
            generalActivityImage.setImageResource(R.drawable.plant_block);
            generalActivityTitle.setText("Plant Block");
        }



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progress);

        rv_register = findViewById(R.id.search_recylerview);
        rv_register.setLayoutManager(new LinearLayoutManager(this));
        farmer_search = findViewById(R.id.farmer_search);
        farmer_search.setHint("Type Block");
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
//
//            }
//        });



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void search(View v){

        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
           // Log.d("Search","Searching Harvest data...");
            getData();
        } else {
            pageItemArrayList = (ArrayList<PageItemPlantBlock>) OfflineFeature.getSharedPreferences("searchPlantBlockActivity", getApplicationContext(),PageItemPlantBlock.class);
            setData();
        }
    }
    private void getData() {
//        progressDialog.setCancelable(false);
//        // progressBar.setMessage("Please Wait...");
//        progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
        APIService service = retrofit.create(APIService.class);
        SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
        String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
        Call<PlantBlockResponse> call = service.getPlantBlockNames(limit, offset, auth_key);
        call.enqueue(new Callback<PlantBlockResponse>() {
            @Override
            public void onResponse(Call<PlantBlockResponse> call, Response<PlantBlockResponse> response) {
              //  progressDialog.hide();
                progressBar.setVisibility(View.GONE);
                try {
                   // Log.d("Data count...",String.valueOf(response.body().getPageItemPlantBlocksList().size()));
                   // System.out.println(response.body().getPageItemPlantBlocksList());

                    if (response.body().getPageItemPlantBlocksList().size() > 0){

                        pageItemArrayList = (ArrayList<PageItemPlantBlock>) response.body().getPageItemPlantBlocksList();

                        saveArrayList(pageItemArrayList, "searchPlantBlockActivity");
                        setData();
                    }else {
                       // Toast.makeText(SearchHarvestFarmerActivity.this, "Data Not Found", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                   // Utility.showToast(SearchHarvestFarmerActivity.this, e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<PlantBlockResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setData() {

        if(farmer_search.getText().toString().isEmpty()){
            registerAdapter = new SearchPlantBlockAdapter(rv_register,SearchPlantBlockActivity.this, pageItemArrayList);
            DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
            rv_register.addItemDecoration(itemDecor);
            rv_register.setAdapter(registerAdapter);
        }else{

            ArrayList<PageItemPlantBlock> filteredList = new ArrayList<>();
            filteredList.clear();

            for(PageItemPlantBlock item : pageItemArrayList){
                if(item.getBlockName().toLowerCase().contains(farmer_search.getText().toString().toLowerCase())){
                    filteredList.add(item);
                }
            }

            registerAdapter = new SearchPlantBlockAdapter(rv_register,SearchPlantBlockActivity.this, filteredList);
            DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
            rv_register.addItemDecoration(itemDecor);
            rv_register.setAdapter(registerAdapter);

        }



    }

    public void saveArrayList(ArrayList<PageItemPlantBlock> list, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

//    public ArrayList<PageItemPlantBlock> getArrayList(String key) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        Gson gson = new Gson();
//        String json = prefs.getString(key, null);
//        Type type = new TypeToken<ArrayList<PageItemHarvest>>() {
//        }.getType();
//        return gson.fromJson(json, type);
//    }

}

