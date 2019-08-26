package com.cw.farmer.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.cw.farmer.OnLoadMoreListener;
import com.cw.farmer.R;
import com.cw.farmer.adapter.RegisterAdapter;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.PageItem;
import com.cw.farmer.model.RegisterResponse;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

public class RegisterActivity extends AppCompatActivity {
    RecyclerView rv_register;
    RegisterAdapter registerAdapter;
    ProgressDialog progressDialog;
    ArrayList<PageItem> pageItemArrayList;
    FloatingActionButton fab;

    private int page=0;
    private int limit=10;
    private int offset=0;
    private boolean end=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        rv_register = findViewById(R.id.rv_registers);
        fab = findViewById(R.id.fab);
        rv_register.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);
        if (Utility.isNetworkAvailable(this)) {
            //getData();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
        //setData();
    }

    private void getData() {
        progressDialog.setCancelable(false);
        // progressBar.setMessage("Please Wait...");
        progressDialog.show();
        Retrofit retrofit = ApiClient.getClient("/authentication/");
        APIService service = retrofit.create(APIService.class);
        Call<RegisterResponse> call = service.getRegister(limit,offset,null);
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
                        Toast.makeText(RegisterActivity.this, "Data Not Found", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Utility.showToast(RegisterActivity.this, e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                progressDialog.hide();
                Utility.showToast(RegisterActivity.this, t.getMessage());
            }
        });
    }

    private void setData() {
        registerAdapter = new RegisterAdapter(rv_register,RegisterActivity.this, pageItemArrayList);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
        rv_register.addItemDecoration(itemDecor);
       //rv_register.setAdapter(registerAdapter);
        if (offset == 0) {
            rv_register.setAdapter(registerAdapter);
        }else{
            registerAdapter.setdata(pageItemArrayList);
            rv_register.scrollToPosition(pageItemArrayList.size());
        }
        registerAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pageItemArrayList.add(null);
                registerAdapter.notifyItemInserted(pageItemArrayList.size() - 1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageItemArrayList.remove(pageItemArrayList.size() - 1);
                        registerAdapter.notifyItemRemoved(pageItemArrayList.size());
                        //  rv_feed.scrollToPosition(feedDataArrayList.size()-1);
                        //Generating more data
                        int index = pageItemArrayList.size();
                        offset = offset + limit;
                        if (!end)
                        {
                            getData();
                        }
                        //feedAdapter.notifyDataSetChanged();
                    }
                }, 1000);

            }
        });

    }

}
