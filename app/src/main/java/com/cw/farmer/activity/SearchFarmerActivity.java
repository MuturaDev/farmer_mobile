package com.cw.farmer.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.cw.farmer.OnLoadMoreListener;
import com.cw.farmer.R;
import com.cw.farmer.adapter.RegisterAdapter;
import com.cw.farmer.adapter.SearchAdapter;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.PageItem;
import com.cw.farmer.model.RegisterResponse;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

public class SearchFarmerActivity extends AppCompatActivity {
    RecyclerView rv_register;
    SearchAdapter registerAdapter;
    ProgressDialog progressDialog;
    ArrayList<PageItem> pageItemArrayList;
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
        setContentView(R.layout.activity_search_farmer);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

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
        getData();
    }
    private void getData() {
        progressDialog.setCancelable(false);
        // progressBar.setMessage("Please Wait...");
        progressDialog.show();
        Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
        APIService service = retrofit.create(APIService.class);
        Call<RegisterResponse> call = service.getRegister(limit,offset,farmer_search.getText().toString());
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                progressDialog.hide();
                try {
                    if (response.body().getPageItems().size()!=0){
                        if (pageItemArrayList==null){
                            pageItemArrayList = (ArrayList<PageItem>) response.body().getPageItems();
                        }else{
                            pageItemArrayList .addAll(response.body().getPageItems());
                        }
                        setData();
                    }else {
                        if (registerAdapter!=null)
                        {
                            registerAdapter.setLoaded();
                        }
                        end =true;
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
    }

    private void setData() {
        registerAdapter = new SearchAdapter(rv_register,SearchFarmerActivity.this, pageItemArrayList);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
        rv_register.addItemDecoration(itemDecor);
        rv_register.setAdapter(registerAdapter);

    }

}
