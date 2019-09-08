package com.cw.farmer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cw.farmer.R;
import com.cw.farmer.adapter.SearchContractAdapter;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.SearchContractPageItem;
import com.cw.farmer.model.SearchContractResponse;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

public class SearchContractFarmerActivity extends AppCompatActivity {
    RecyclerView rv_register;
    SearchContractAdapter registerAdapter;
    ProgressDialog progressDialog;
    ArrayList<SearchContractPageItem> pageItemArrayList;
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
        setContentView(R.layout.activity_search_contract_farmer);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String name =(String) b.get("name");
            String id =(String) b.get("id");
            String crop_date =(String) b.get("crop_date");
            String noofunits =(String) b.get("noofunits");
            String plantingid =(String) b.get("plantingid");
            Intent intent = null;
            SharedPreferences prefs = getSharedPreferences("search", MODE_PRIVATE);
                intent = new Intent(SearchContractFarmerActivity.this, ContractSignActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("id",id);
                intent.putExtra("crop_date",crop_date);
                intent.putExtra("noofunits",noofunits);
                intent.putExtra("plantingid",plantingid);
                startActivity(intent);

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
        Retrofit retrofit = ApiClient.getClient("/authentication/");
        APIService service = retrofit.create(APIService.class);
        Call<SearchContractResponse> call = service.getContractfarmer(limit,offset,farmer_search.getText().toString());
        call.enqueue(new Callback<SearchContractResponse>() {
            @Override
            public void onResponse(Call<SearchContractResponse> call, Response<SearchContractResponse> response) {
                progressDialog.hide();
                try {
                    if (response.body().getPageItems().size()!=0){
                        if (pageItemArrayList==null){
                            pageItemArrayList = (ArrayList<SearchContractPageItem>) response.body().getPageItems();
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
                        Toast.makeText(SearchContractFarmerActivity.this, "Data Not Found", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Utility.showToast(SearchContractFarmerActivity.this, e.getMessage()+"Aggrey");
                }

            }

            @Override
            public void onFailure(Call<SearchContractResponse> call, Throwable t) {
                progressDialog.hide();
                Utility.showToast(SearchContractFarmerActivity.this, t.getMessage());
            }
        });
    }

    private void setData() {
        registerAdapter = new SearchContractAdapter(rv_register,SearchContractFarmerActivity.this, pageItemArrayList);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
        rv_register.addItemDecoration(itemDecor);
        rv_register.setAdapter(registerAdapter);

    }
}
