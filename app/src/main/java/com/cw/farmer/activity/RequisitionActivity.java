package com.cw.farmer.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.cw.farmer.adapter.RequistionAdapter;
import com.cw.farmer.adapter.TaskAdapter;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.FarmerDocResponse;
import com.cw.farmer.model.PageItemstask;
import com.cw.farmer.model.RequisitionResponse;
import com.cw.farmer.model.TasksResponse;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cw.farmer.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

public class RequisitionActivity extends AppCompatActivity {
    String entity_id,crop_date,type,centrename;
    RecyclerView rv_register;
    RequistionAdapter registerAdapter;
    ProgressDialog progressDialog;
    ArrayList<RequisitionResponse> pageItemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisition);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        rv_register = findViewById(R.id.requistion_list);
        rv_register.setLayoutManager(new LinearLayoutManager(this));


        if(b!=null) {
            entity_id=(String) b.get("id");
            crop_date=(String) b.get("crop_date");
            type=(String) b.get("type");
            centrename=(String) b.get("centrename");
        }

        TextView tv_centre= findViewById(R.id.tv_centre);
        tv_centre.setText(centrename);
        TextView tv_crop_date= findViewById(R.id.tv_crop_date);
        tv_crop_date.setText(crop_date);
        TextView tv_type= findViewById(R.id.tv_type);
        tv_type.setText(type);

        progressDialog = new ProgressDialog(this);

        progressDialog.setCancelable(false);
        // progressBar.setMessage("Please Wait...");
        progressDialog.show();
        Retrofit retrofit = ApiClient.getClient("/authentication/");
        APIService service = retrofit.create(APIService.class);
        Call<List<RequisitionResponse>> call = service.getrequsition(entity_id);
        call.enqueue(new Callback<List<RequisitionResponse>>() {
            @Override
            public void onResponse(Call<List<RequisitionResponse>> call, Response<List<RequisitionResponse>> response) {
                progressDialog.hide();
                try {
                    if (response.body() != null){
                            pageItemArrayList = (ArrayList<RequisitionResponse>) response.body();
                            setData();
                    }else{

                        Utility.showToast(RequisitionActivity.this, "No requisition details");
                    }

                } catch (Exception e) {
                    Utility.showToast(RequisitionActivity.this, e.getMessage());
                    Log.w("myApp",  e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<List<RequisitionResponse>> call, Throwable t) {
                progressDialog.hide();
                Utility.showToast(RequisitionActivity.this, t.getMessage());
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void setData() {
        registerAdapter = new RequistionAdapter(rv_register,RequisitionActivity.this, pageItemArrayList);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
        rv_register.addItemDecoration(itemDecor);
        rv_register.setAdapter(registerAdapter);

    }

}
