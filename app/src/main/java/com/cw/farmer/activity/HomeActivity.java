package com.cw.farmer.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.farmer.R;
import com.cw.farmer.adapter.DashboardAdapter;
import com.cw.farmer.adapter.ExpandableListAdapter;
import com.cw.farmer.adapter.TaskAdapter;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.PageItemstask;
import com.cw.farmer.model.TasksResponse;
import com.cw.farmer.model.dashboard;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Appbar = (AppBarLayout)findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressDialog = new ProgressDialog(this);
        // get the listview
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        expListView = (ExpandableListView) findViewById(R.id.verifyplanting_list);
        int width = getResources().getDisplayMetrics().widthPixels;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            expListView.setIndicatorBounds(width - getPixelValue(40), width - getPixelValue(10));
        } else {
            expListView.setIndicatorBoundsRelative(width - getPixelValue(40), width - getPixelValue(10));
        }




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
        // preparing list data
        prepareListData();
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
    private void prepareListData() {
        progressDialog.setCancelable(false);
        // progressBar.setMessage("Please Wait...");
        progressDialog.show();
        Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
        APIService service = retrofit.create(APIService.class);
        Call<TasksResponse> call = service.gettask("Basic YWRtaW46bWFudW5pdGVk", user_id+"");
        call.enqueue(new Callback<TasksResponse>() {
            @Override
            public void onResponse(Call<TasksResponse> call, Response<TasksResponse> response) {
                progressDialog.hide();
                try {
                    if (response.body().getTotalFilteredRecords() > 0){
                            listDataHeader = new ArrayList<String>();
                            listDataChild = new HashMap<String, List<String>>();

                            // Adding child data
                            listDataHeader.add("Receive Inventory");
                            listDataHeader.add("Verify Planting");
                            listDataHeader.add("Manage Sprays");
                        List<String> planting = new ArrayList<String>();
                        List<String> invent = new ArrayList<String>();
                        List<String> spray = new ArrayList<String>();
                        for(PageItemstask taks: response.body().getPageItemstasks()) {

                            if (taks.getEntityName().equals("VERIFY_PLANTING_MOBILE")) {
                                String date = "";
                                for (int elem : taks.getCropDate()) {
                                    date = elem + "/" + date;
                                }
                                planting.add(taks.getCentrename() + " ,Planting," + removeLastChar(date) + " ," + taks.getEntityId());
                            }else{
                                String date = "";
                                for (int elem : taks.getCreatedOn()) {
                                    date = elem + "/" + date;
                                }
                                invent.add(taks.getCentrename() + " ,Inventory," + removeLastChar(date) + " ," + taks.getEntityId());
                            }
                            System.out.println(taks.getCentreid());

                        }

                            listDataChild.put(listDataHeader.get(0), invent); // Header, Child data
                            listDataChild.put(listDataHeader.get(1), planting);
                            listDataChild.put(listDataHeader.get(2), spray);



                        listAdapter = new ExpandableListAdapter(HomeActivity.this, listDataHeader, listDataChild);

                        // setting list adapter
                        expListView.setAdapter(listAdapter);
                    }else{

                        Utility.showToast(HomeActivity.this, "No Task");
                    }

                } catch (Exception e) {
                    Utility.showToast(HomeActivity.this, e.getMessage());
                    System.out.println(e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<TasksResponse> call, Throwable t) {
                progressDialog.hide();
                Utility.showToast(HomeActivity.this, t.getMessage());
            }
        });

    }
    public int getPixelValue(int dp) {

        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
}
