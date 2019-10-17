package com.cw.farmer.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;

import com.cw.farmer.adapter.SearchAdapter;
import com.cw.farmer.adapter.TaskAdapter;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.PageItem;
import com.cw.farmer.model.PageItemstask;
import com.cw.farmer.model.RegisterResponse;
import com.cw.farmer.model.TasksResponse;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cw.farmer.R;
import com.cw.farmer.adapter.DashboardAdapter;
import com.cw.farmer.model.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;
import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout lin_register, lin_create;
    private Context mContext=HomeActivity.this;

    private DashboardAdapter adapter;
    private List<dashboard> dashboardList;
    String user_id;
    RecyclerView rv_register;
    TaskAdapter registerAdapter;
    ProgressDialog progressDialog;
    ArrayList<PageItemstask> pageItemArrayList;

    AppBarLayout Appbar;
    Toolbar toolbar;
    CardView register_farmer,view_farmer,recruit_farmer,contract_signing,verify_planting,crop_destruction,harvest_collection,inventory_mgt,dashboard;

    boolean ExpandedActionBar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Appbar = (AppBarLayout)findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressDialog = new ProgressDialog(this);
        rv_register = findViewById(R.id.verifyplanting_list);
        rv_register.setLayoutManager(new LinearLayoutManager(this));
        SharedPreferences prefs = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
        Set<String> permission = prefs.getStringSet("key", null);

        if (!permission.contains("ALL_FUNCTIONS")){
            register_farmer=findViewById(R.id.register_farmer);
            if (!permission.contains("CREATE_FARMER")) {
                register_farmer.setVisibility(View.GONE);
            }
            view_farmer=findViewById(R.id.view_farmer);
            if (!permission.contains("READ_FARMER_MOBILE")) {
                view_farmer.setVisibility(View.GONE);
            }
            recruit_farmer=findViewById(R.id.recruit_farmer);
            if (!permission.contains("RECRUIT_FARMER_MOBILE")) {
                recruit_farmer.setVisibility(View.GONE);
            }
            contract_signing=findViewById(R.id.contract_signing);
            if (!permission.contains("CONTRACT_FARMER_MOBILE")) {
                contract_signing.setVisibility(View.GONE);
            }
            verify_planting=findViewById(R.id.verify_planting);
            if (!permission.contains("VERIFY_PLANTING_MOBILE")) {
                verify_planting.setVisibility(View.GONE);
            }
            crop_destruction=findViewById(R.id.crop_destruction);
            if (!permission.contains("CROP_DESTRUCTION_MOBILE")) {
                crop_destruction.setVisibility(View.GONE);
            }
            harvest_collection=findViewById(R.id.harvest_collection);
            if (!permission.contains("HARVEST_COLLECTION_MOBILE")) {
                harvest_collection.setVisibility(View.GONE);
            }
            dashboard=findViewById(R.id.dashboard);
            inventory_mgt=findViewById(R.id.inventory_mgt);
        }

        user_id=prefs.getString("userid", "-1");
        progressDialog.setCancelable(false);
        // progressBar.setMessage("Please Wait...");
        progressDialog.show();
        Retrofit retrofit = ApiClient.getClient("/authentication/");
        APIService service = retrofit.create(APIService.class);
        Call<TasksResponse> call = service.gettask("Basic YWRtaW46bWFudW5pdGVk", Integer.parseInt(user_id));
        call.enqueue(new Callback<TasksResponse>() {
            @Override
            public void onResponse(Call<TasksResponse> call, Response<TasksResponse> response) {
                progressDialog.hide();
                try {
                    if (response.body().getTotalFilteredRecords() > 0){
                        if (pageItemArrayList==null){
                            pageItemArrayList = (ArrayList<PageItemstask>) response.body().getPageItemstasks();
                            registerAdapter = new TaskAdapter(rv_register,HomeActivity.this, pageItemArrayList);
                            DividerItemDecoration itemDecor = new DividerItemDecoration(HomeActivity.this, HORIZONTAL);
                            rv_register.addItemDecoration(itemDecor);
                            rv_register.setAdapter(registerAdapter);
                        }
                    }else{

                        Utility.showToast(HomeActivity.this, "No Task");
                    }

                } catch (Exception e) {
                    Utility.showToast(HomeActivity.this, e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<TasksResponse> call, Throwable t) {
                progressDialog.hide();
                Utility.showToast(HomeActivity.this, t.getMessage());
            }
        });







        setSupportActionBar(toolbar);
    }



    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    public int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void init() {
        lin_register = findViewById(R.id.lin_register);
        lin_create = findViewById(R.id.lin_farmer_bank_det);
        lin_register.setOnClickListener(this);
        lin_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_register:
                startActivity(new Intent(HomeActivity.this, RegisterActivity.class));
                break;
        }
    }
    private void setData() {


    }
    public void openregister(View v){
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
    }
    public void openfarmerlist(View v){
        startActivity(new Intent(HomeActivity.this, ListFarmerActivity.class));
    }
    public void openrecruit(View v){
        startActivity(new Intent(HomeActivity.this, FarmerRecruitActivity.class));
    }
    public void opencontract(View v){
        startActivity(new Intent(HomeActivity.this, ContractSignActivity.class));
    }
    public void opendestruction(View v){
        startActivity(new Intent(HomeActivity.this, CropDestructionActivity.class));
    }
    public void openharvesting(View v){
        startActivity(new Intent(HomeActivity.this, HarvestingActivity.class));
    }
    public void openplantverfication(View v){
        startActivity(new Intent(HomeActivity.this, PlantingVerificationActivity.class));
    }
}
