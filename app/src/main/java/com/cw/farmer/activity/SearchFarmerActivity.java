package com.cw.farmer.activity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.cw.farmer.adapter.SearchAdapter;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.PageItem;
import com.cw.farmer.model.RegisterResponse;
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

public class SearchFarmerActivity extends HandleConnectionAppCompatActivity {
    RecyclerView rv_register;
    SearchAdapter registerAdapter;
    ProgressDialog progressDialog;
    ArrayList<PageItem> pageItemArrayList;
    FloatingActionButton fab;
    EditText farmer_search;
    Button btn_search;
    SharedPreferences sharedPreferences;

    private int page=0;
    private int limit=0;
    private int offset=0;
    private boolean end=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_farmer);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);

        if(b!=null)
        {
            String name =(String) b.get("name");
            String id =(String) b.get("id");
            Intent intent = null;
            SharedPreferences prefs = getSharedPreferences("search", MODE_PRIVATE);
            //Toast.makeText(SearchFarmerActivity.this, prefs.getString("activity", " "), Toast.LENGTH_LONG).show();
            if (prefs.getString("activity", " ").equals("recruit")){
                intent = new Intent(SearchFarmerActivity.this, FarmerRecruitActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("id",id);
                startActivity(intent);
            }


        }
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
            getData();
        } else {

            String searchFarmer = farmer_search.getText().toString();

            if(searchFarmer.isEmpty()){
                pageItemArrayList = (ArrayList<PageItem>) OfflineFeature.getSharedPreferences("viewrecruitfarmer", getApplicationContext(), PageItem.class);
            }else{
                pageItemArrayList = new ArrayList<>();
                pageItemArrayList.clear();

                for(PageItem item :  (ArrayList<PageItem>) OfflineFeature.getSharedPreferences("viewrecruitfarmer", getApplicationContext(), PageItem.class)){

                    if(item.getFirstname().toLowerCase().contains(searchFarmer.toLowerCase()) ||
                            item.getMiddlename().toLowerCase().contains(searchFarmer.toLowerCase()) ||
                            item.getLastname().toLowerCase().contains(searchFarmer.toLowerCase())


                    ||

                            item.getIdno().toLowerCase().contains(searchFarmer.toLowerCase())
                            ){


                        pageItemArrayList.add(item);
                    }
                }
            }

            //pageItemArrayList = getArrayList("viewrecruitfarmer");
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
            SharedPreferences prefs = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
            String auth_key = prefs.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
            Call<RegisterResponse> call = service.getRegister(limit, offset, farmer_search.getText().toString(), auth_key);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    progressDialog.hide();
                    try {
                        if (String.valueOf(response.body().getPageItems().size()) != "0") {
                            pageItemArrayList = (ArrayList<PageItem>) response.body().getPageItems();
                            saveArrayList(pageItemArrayList, "viewrecruitfarmer");
                            setData();
                        } else {

                            Toast.makeText(SearchFarmerActivity.this, "Data Not Found", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Utility.showToast(SearchFarmerActivity.this, e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    progressDialog.hide();
                    Utility.showToast(SearchFarmerActivity.this, t.getMessage());
                }
            });
        } else {
            pageItemArrayList = (ArrayList<PageItem>) OfflineFeature.getSharedPreferences("viewrecruitfarmer", getApplicationContext(), PageItem.class);
            setData();
            progressDialog.hide();
        }

    }

    private void setData() {
        registerAdapter = new SearchAdapter(rv_register,SearchFarmerActivity.this, pageItemArrayList);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
        rv_register.addItemDecoration(itemDecor);
        rv_register.setAdapter(registerAdapter);

    }

    public void saveArrayList(ArrayList<PageItem> list, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

//    public ArrayList<PageItem> getArrayList(String key) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        Gson gson = new Gson();
//        String json = prefs.getString(key, null);
//        Type type = new TypeToken<ArrayList<PageItem>>() {
//        }.getType();
//        return gson.fromJson(json, type);
//    }

}
