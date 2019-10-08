package com.cw.farmer.activity;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.cw.farmer.adapter.SearchHarvestAdapter;
import com.cw.farmer.adapter.SearchPlantingVerificationAdapter;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.FarmerHarvestResponse;
import com.cw.farmer.model.PageItemHarvest;
import com.cw.farmer.model.PageItemsPlantVerify;
import com.cw.farmer.model.PlantVerifyResponse;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cw.farmer.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

public class SearchPlantingVerficationActivity extends AppCompatActivity {
    RecyclerView rv_register;
    SearchPlantingVerificationAdapter registerAdapter;
    ProgressDialog progressDialog;
    ArrayList<PageItemsPlantVerify> pageItemArrayList;
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
        setContentView(R.layout.activity_search_planting_verfication);
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
        Call<PlantVerifyResponse> call = service.getplantingfarmer(limit,offset,farmer_search.getText().toString());
        call.enqueue(new Callback<PlantVerifyResponse>() {
            @Override
            public void onResponse(Call<PlantVerifyResponse> call, Response<PlantVerifyResponse> response) {
                progressDialog.hide();
                try {
                    System.out.println(response.body().getPageItemsPlantVerify());

                    if (response.body().getPageItemsPlantVerify().size()!=0){
                        if (pageItemArrayList==null){
                            pageItemArrayList = (ArrayList<PageItemsPlantVerify>) response.body().getPageItemsPlantVerify();
                        }else{
                            pageItemArrayList .addAll(response.body().getPageItemsPlantVerify());
                        }
                        setData();
                    }else {
                        if (registerAdapter!=null)
                        {
                            registerAdapter.setLoaded();
                        }
                        end =true;
                        Toast.makeText(SearchPlantingVerficationActivity.this, "Data Not Found", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Utility.showToast(SearchPlantingVerficationActivity.this, e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<PlantVerifyResponse> call, Throwable t) {
                progressDialog.hide();
                Utility.showToast(SearchPlantingVerficationActivity.this, t.getMessage());
            }
        });
    }

    private void setData() {
        registerAdapter = new SearchPlantingVerificationAdapter(rv_register,SearchPlantingVerficationActivity.this, pageItemArrayList);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
        rv_register.addItemDecoration(itemDecor);
        rv_register.setAdapter(registerAdapter);

    }

}
