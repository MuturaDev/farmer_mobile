package com.cw.farmer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import com.google.android.material.appbar.AppBarLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.cw.farmer.R;
import com.cw.farmer.adapter.DashboardAdapter;
import com.cw.farmer.model.dashboard;

import java.util.List;
import java.util.Set;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout lin_register, lin_create;
    private Context mContext=HomeActivity.this;

    private RecyclerView recyclerView;
    private DashboardAdapter adapter;
    private List<dashboard> dashboardList;

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






        setSupportActionBar(toolbar);

        for (String s : permission) {
            System.out.println(s);
        }




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
