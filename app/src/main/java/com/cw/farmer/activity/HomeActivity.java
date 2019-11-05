package com.cw.farmer.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
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

import com.cw.farmer.NetworkUtil;
import com.cw.farmer.R;
import com.cw.farmer.adapter.DashboardAdapter;
import com.cw.farmer.adapter.ExpandableListAdapter;
import com.cw.farmer.adapter.TaskAdapter;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.Accountdetails;
import com.cw.farmer.model.AllResponse;
import com.cw.farmer.model.ContractSignDB;
import com.cw.farmer.model.CropDestructionPostDB;
import com.cw.farmer.model.FarmerErrorResponse;
import com.cw.farmer.model.FarmerModel;
import com.cw.farmer.model.FarmerModelDB;
import com.cw.farmer.model.HarvestingDB;
import com.cw.farmer.model.Identitydetails;
import com.cw.farmer.model.PageItemstask;
import com.cw.farmer.model.PlantingVerifyDB;
import com.cw.farmer.model.RecruitFarmerDB;
import com.cw.farmer.model.SprayPostDB;
import com.cw.farmer.model.TasksResponse;
import com.cw.farmer.model.dashboard;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;
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
        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
            prepareListData();
        }

        setSupportActionBar(toolbar);
        //offlinesync();

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

    public void opensprayconfirmation(View v) {
        startActivity(new Intent(HomeActivity.this, SprayConfirmationActivity.class));
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
                        listDataHeader.add("Message");
                        //listDataHeader.add("");
                        List<String> planting = new ArrayList<String>();
                        List<String> invent = new ArrayList<String>();
                        for(PageItemstask taks: response.body().getPageItemstasks()) {

                            if (taks.getEntityName().equals("VERIFY_PLANTING_MOBILE")) {
                                String date = "";
                                for (int elem : taks.getCropDate()) {
                                    date = elem + "/" + date;
                                }
                                invent.add(taks.getCentrename() + " ,Planting," + removeLastChar(date) + " ," + taks.getEntityId());
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
                        // listDataChild.put(listDataHeader.get(1), planting);



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

    public void offlinesync(View v) {

        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
            SharedPreferences prefs = getSharedPreferences("location", MODE_PRIVATE);


            List<FarmerModelDB> farmers = FarmerModelDB.listAll(FarmerModelDB.class);
            for (FarmerModelDB farmer : farmers) {
                FarmerModel farmerModel = new FarmerModel();
                Accountdetails accountdetails = new Accountdetails();
                Identitydetails identitydetails = new Identitydetails();

                farmerModel.setFirstname(farmer.firstname);
                farmerModel.setMobileno(farmer.mobileno);
                farmerModel.setEmail(farmer.email);
                farmerModel.setGender(farmer.gender);
                farmerModel.getIdno(farmer.idno);
                farmerModel.setDateOfBirth(farmer.dateOfBirth);
                farmerModel.setActivated(farmer.activated);
                farmerModel.setCenterid(farmer.centerid);
                farmerModel.setDateFormat(farmer.dateFormat);
                farmerModel.setLocale(farmer.locale);

                accountdetails.setAccountno(farmer.accountno);
                accountdetails.setBankId(farmer.bankId);
                accountdetails.setImage(farmer.image_bank);
                accountdetails.setFiletype(farmer.filetype_bank);

                identitydetails.setDocId(farmer.docId);
                identitydetails.setImage(farmer.image_id);
                identitydetails.setFiletype(farmer.filetype_id);
                identitydetails.setDocno(farmer.docno_id);

                farmerModel.setAccountdetails(accountdetails);
                farmerModel.setIdentitydetails(identitydetails);
                SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Synchronizing offline data to online ...");
                pDialog.setCancelable(false);
                pDialog.show();

                Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                APIService service = retrofit.create(APIService.class);
                Call<FarmerErrorResponse> call = service.createFarmer("Basic YWRtaW46bWFudW5pdGVk", farmerModel);
                call.enqueue(new Callback<FarmerErrorResponse>() {
                    @Override
                    public void onResponse(Call<FarmerErrorResponse> call, Response<FarmerErrorResponse> response) {
                        try {
                            if (response.body() != null) {
                                farmer.delete();
                                pDialog.dismissWithAnimation();

                            } else {
                                farmer.delete();
                                pDialog.dismissWithAnimation();
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Ooops...")
                                        .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                        .show();
                            }

                            //Toast.makeText(MainActivity.this, response.body().getHttpStatusCode(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            pDialog.dismissWithAnimation();

                        }

                    }

                    @Override
                    public void onFailure(Call<FarmerErrorResponse> call, Throwable t) {
                        pDialog.dismissWithAnimation();
                    }
                });
            }

            List<RecruitFarmerDB> recruits = RecruitFarmerDB.listAll(RecruitFarmerDB.class);
            for (RecruitFarmerDB recruit : recruits) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("farmerid", recruit.farmerid);
                hashMap.put("dateid", recruit.dateid);
                hashMap.put("landownership", recruit.landownership);
                hashMap.put("cordinates", prefs.getString("coordinates", "000,000"));
                hashMap.put("location", prefs.getString("location_str", "offline"));
                hashMap.put("noofunits", recruit.noofunits);
                hashMap.put("section", recruit.section);

                SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Synchronizing offline data to online ...");
                pDialog.setCancelable(false);
                pDialog.show();

                Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                APIService service = retrofit.create(APIService.class);
                Call<AllResponse> call = service.recruit("Basic YWRtaW46bWFudW5pdGVk", hashMap);
                call.enqueue(new Callback<AllResponse>() {
                    @Override
                    public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                        progressDialog.hide();
                        try {
                            if (response.body() != null) {
                                recruit.delete();
                                pDialog.dismissWithAnimation();
                            } else {
                                recruit.delete();
                                pDialog.dismissWithAnimation();
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Ooops...")
                                        .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                        .show();
                            }
                        } catch (Exception e) {
                            pDialog.dismissWithAnimation();
                        }
                        //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Call<AllResponse> call, Throwable t) {
                        pDialog.dismissWithAnimation();
                    }
                });
            }

            List<ContractSignDB> signs = ContractSignDB.listAll(ContractSignDB.class);
            for (ContractSignDB sign : signs) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("referenceNo", sign.referenceNo);
                hashMap.put("cropDateId", sign.cropDateId);
                hashMap.put("units", sign.units);
                hashMap.put("farmerId", sign.farmerId);
                hashMap.put("file", sign.file);
                hashMap.put("dateFormat", sign.dateFormat);
                hashMap.put("locale", sign.locale);

                SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Synchronizing offline data to online ...");
                pDialog.setCancelable(false);
                pDialog.show();

                Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                APIService service = retrofit.create(APIService.class);
                Call<AllResponse> call = service.postcontract("Basic YWRtaW46bWFudW5pdGVk", hashMap);
                call.enqueue(new Callback<AllResponse>() {
                    @Override
                    public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                        //pDialog.hide();
                        try {
                            if (response.body() != null) {
                                sign.delete();
                                pDialog.dismissWithAnimation();

                            } else {
                                sign.delete();
                                pDialog.dismissWithAnimation();
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Ooops...")
                                        .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                        .show();
                            }
                        } catch (Exception e) {
                            pDialog.dismissWithAnimation();
                        }
                        //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Call<AllResponse> call, Throwable t) {
                        pDialog.dismissWithAnimation();
                    }
                });
            }

            List<PlantingVerifyDB> plants = PlantingVerifyDB.listAll(PlantingVerifyDB.class);
            for (PlantingVerifyDB plant : plants) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("cordinates", prefs.getString("cordinates", "000,000"));
                hashMap.put("location", prefs.getString("location_str", "offline"));
                hashMap.put("contractid", plant.contractid);
                hashMap.put("plantconfirmed", plant.plant_value);
                hashMap.put("waterconfirmed", plant.water_value);

                SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Synchronizing offline data to online ...");
                pDialog.setCancelable(false);
                pDialog.show();

                Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                APIService service = retrofit.create(APIService.class);
                Call<AllResponse> call = service.postplantverify("Basic YWRtaW46bWFudW5pdGVk", hashMap);
                call.enqueue(new Callback<AllResponse>() {
                    @Override
                    public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                        try {
                            if (response.body() != null) {
                                plant.delete();
                                pDialog.dismissWithAnimation();
                            } else {
                                plant.delete();
                                pDialog.dismissWithAnimation();
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Ooops...")
                                        .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                        .show();
                            }
                        } catch (Exception e) {
                            pDialog.dismissWithAnimation();
                        }
                        //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Call<AllResponse> call, Throwable t) {
                        pDialog.dismissWithAnimation();
                    }
                });
            }

            List<CropDestructionPostDB> destroys = CropDestructionPostDB.listAll(CropDestructionPostDB.class);
            SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Synchronizing offline data to online ...");
            pDialog.setCancelable(false);
            pDialog.show();
            for (CropDestructionPostDB destroy : destroys) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("cropDatesId", destroy.cropDatesId);
                hashMap.put("accountNumber", destroy.accountNumber);
                hashMap.put("unit", destroy.unit);
                hashMap.put("farmers_id", destroy.farmers_id);
                hashMap.put("file", destroy.file);
                hashMap.put("locale", destroy.locale);
                hashMap.put("cropDestructionType", destroy.cropDestructionType);
                hashMap.put("cropDestructionReasonsId", destroy.cropDestructionReasonsId);

                Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                APIService service = retrofit.create(APIService.class);
                Call<AllResponse> call = service.postcropdestruction("Basic YWRtaW46bWFudW5pdGVk", hashMap);
                call.enqueue(new Callback<AllResponse>() {
                    @Override
                    public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                        try {
                            if (response.body() != null) {
                                destroy.delete();
                                pDialog.dismissWithAnimation();

                            } else {
                                destroy.delete();
                                pDialog.dismissWithAnimation();
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Ooops...")
                                        .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                        .show();
                            }
                        } catch (Exception e) {
                            pDialog.dismissWithAnimation();
                        }
                        //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Call<AllResponse> call, Throwable t) {
                        pDialog.dismissWithAnimation();
                    }
                });
            }


            List<HarvestingDB> harvests = HarvestingDB.listAll(HarvestingDB.class);
            for (HarvestingDB harvest : harvests) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("dateid", harvest.dateid);
                hashMap.put("noofunits", harvest.noofunits);
                hashMap.put("farmerid", harvest.farmerid);
                hashMap.put("harvestkilos", harvest.harvestkilos);
                hashMap.put("locale", harvest.locale);

                Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                APIService service = retrofit.create(APIService.class);
                Call<AllResponse> call = service.postharvesting("Basic YWRtaW46bWFudW5pdGVk", hashMap);
                call.enqueue(new Callback<AllResponse>() {
                    @Override
                    public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                        try {
                            if (response.body() != null) {
                                harvest.delete();
                                pDialog.dismissWithAnimation();

                            } else {
                                harvest.delete();
                                pDialog.dismissWithAnimation();
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Ooops...")
                                        .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                        .show();
                            }
                        } catch (Exception e) {
                            pDialog.dismissWithAnimation();
                        }
                        //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Call<AllResponse> call, Throwable t) {
                        pDialog.dismissWithAnimation();
                    }
                });
            }

            List<SprayPostDB> sprays = SprayPostDB.listAll(SprayPostDB.class);
            for (SprayPostDB spray : sprays) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("verificationid", spray.verificationid);
                hashMap.put("cordinates", prefs.getString("cordinates", "000,000"));
                hashMap.put("location", prefs.getString("location_str", "offline"));
                hashMap.put("sprayconfirmed", spray.sprayconfirmed);
                hashMap.put("programid", spray.programid);

                Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                APIService service = retrofit.create(APIService.class);
                Call<AllResponse> call = service.spraypost("Basic YWRtaW46bWFudW5pdGVk", hashMap);
                call.enqueue(new Callback<AllResponse>() {
                    @Override
                    public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                        try {
                            if (response.body() != null) {
                                spray.delete();
                                pDialog.dismissWithAnimation();

                            } else {
                                spray.delete();
                                pDialog.dismissWithAnimation();
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Ooops...")
                                        .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                        .show();
                            }
                        } catch (Exception e) {
                            pDialog.dismissWithAnimation();
                        }
                        //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Call<AllResponse> call, Throwable t) {
                        pDialog.dismissWithAnimation();
                    }
                });
            }
            //pDialog.dismissWithAnimation();


        }

    }
}
