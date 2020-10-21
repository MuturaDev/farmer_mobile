package com.cw.farmer.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ClipDrawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cw.farmer.HandleConnectionAppCompatActivity;
import com.cw.farmer.NetworkUtil;
import com.cw.farmer.R;
import com.cw.farmer.adapter.AdhocAdapter;
import com.cw.farmer.adapter.DashboardAdapter;
import com.cw.farmer.adapter.ExpandableListAdapter;
import com.cw.farmer.adapter.RegisterAdapter;
import com.cw.farmer.crashreporting.Catcho;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.internetConnectionBroadcast.CheckNetworkJob;
import com.cw.farmer.internetConnectionBroadcast.NotificationNotify;
import com.cw.farmer.model.Accountdetails;
import com.cw.farmer.model.AdhocResponse;
import com.cw.farmer.model.AllResponse;
import com.cw.farmer.model.ContractSignDB;
import com.cw.farmer.model.CropDestructionPostDB;
import com.cw.farmer.model.FarmerDocResponse;
import com.cw.farmer.model.FarmerErrorResponse;
import com.cw.farmer.model.FarmerHarvestResponse;
import com.cw.farmer.model.FarmerModel;
import com.cw.farmer.model.FarmerModelDB;
import com.cw.farmer.model.GeneralSpinnerResponse;
import com.cw.farmer.model.HarvestingDB;
import com.cw.farmer.model.Identitydetails;
import com.cw.farmer.model.PageItem;
import com.cw.farmer.model.PageItemHarvest;
import com.cw.farmer.model.PageItemPlantBlock;
import com.cw.farmer.model.PageItemSearchArea;
import com.cw.farmer.model.PageItemsAdhoc;
import com.cw.farmer.model.PageItemsDestruction;
import com.cw.farmer.model.PageItemsPlantVerify;
import com.cw.farmer.model.PageItemsSprayFarmer;
import com.cw.farmer.model.PageItemstask;
import com.cw.farmer.model.PlantBlockResponse;
import com.cw.farmer.model.PlantVerifyResponse;
import com.cw.farmer.model.PlantingVerifyDB;
import com.cw.farmer.model.RecruitFarmerDB;
import com.cw.farmer.model.RegisterResponse;
import com.cw.farmer.model.SearchAreaResponse;
import com.cw.farmer.model.SearchContractPageItem;
import com.cw.farmer.model.SearchContractResponse;
import com.cw.farmer.model.SearchDestructionResponse;
import com.cw.farmer.model.SprayFarmerResponse;
import com.cw.farmer.model.SprayPostDB;
import com.cw.farmer.model.TasksResponse;
import com.cw.farmer.model.dashboard;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.cw.farmer.spinner_models.GeneralSpinner;
import com.cw.farmer.table_models.ApplyFertilizerTB;
import com.cw.farmer.table_models.ChangeCentreTB;
import com.cw.farmer.table_models.EditFarmerDetailsTB;
import com.cw.farmer.table_models.FarmerDocument;
import com.cw.farmer.table_models.HarvestBlockTB;
import com.cw.farmer.table_models.IrrigateBlockTB;
import com.cw.farmer.table_models.PlantBlockTB;
import com.cw.farmer.table_models.RegisterBlockTB;
import com.cw.farmer.table_models.ScoutingTB;
import com.cw.farmer.testexamples.SampleActivity;
import com.cw.farmer.utils.Constants;
import com.cw.farmer.utils.OfflineFeature;
import com.github.florent37.tutoshowcase.TutoShowcase;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.imaginativeworld.oopsnointernet.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.NoInternetDialog;
import org.imaginativeworld.oopsnointernet.NoInternetSnackbar;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;

public class HomeActivity extends HandleConnectionAppCompatActivity implements View.OnClickListener {




    //SHOW INTENET CONNECTION
    // No Internet Dialog
    private NoInternetDialog noInternetDialog;

    // No Internet Snackbar
    private NoInternetSnackbar noInternetSnackbar;
    @Override
    protected void onResume() {
        super.onResume();

//        // No Internet Dialog
//        NoInternetDialog.Builder builder1 = new NoInternetDialog.Builder(this);
//
//        builder1.setConnectionCallback(new ConnectionCallback() { // Optional
//            @Override
//            public void hasActiveConnection(boolean hasActiveConnection) {
//                // ...
//                //Snackbar.make()
////
//
//            }
//        });
//        builder1.setCancelable(false); // Optional
//        builder1.setNoInternetConnectionTitle("No Internet"); // Optional
//        builder1.setNoInternetConnectionMessage("Check your Internet connection and try again"); // Optional
//        builder1.setShowInternetOnButtons(true); // Optional
//        builder1.setPleaseTurnOnText("Please turn on"); // Optional
//        builder1.setWifiOnButtonText("Wifi"); // Optional
//        builder1.setMobileDataOnButtonText("Mobile data"); // Optional
//
//        builder1.setOnAirplaneModeTitle("No Internet"); // Optional
//        builder1.setOnAirplaneModeMessage("You have turned on the airplane mode."); // Optional
//        builder1.setPleaseTurnOffText("Please turn off"); // Optional
//        builder1.setAirplaneModeOffButtonText("Airplane mode"); // Optional
//        builder1.setShowAirplaneModeOffButtons(true); // Optional
//
//        noInternetDialog = builder1.build();


        // No Internet Snackbar
        NoInternetSnackbar.Builder builder2 = new NoInternetSnackbar.Builder(this, (ViewGroup) findViewById(android.R.id.content));

        builder2.setConnectionCallback(new ConnectionCallback() { // Optional
            @Override
            public void hasActiveConnection(boolean hasActiveConnection) {
                // ...
            }
        });
        builder2.setIndefinite(true); // Optional
        builder2.setNoInternetConnectionMessage("No active Internet connection!"); // Optional
        builder2.setOnAirplaneModeMessage("You have turned on the airplane mode!"); // Optional
        builder2.setSnackbarActionText("Settings");
        builder2.setShowActionToDismiss(true);
        builder2.setSnackbarDismissActionText("Exit");

        noInternetSnackbar = builder2.build();

    }
    @Override
    protected void onPause() {
        super.onPause();

        // No Internet Dialog
        if (noInternetDialog != null) {
            noInternetDialog.destroy();
        }

        // No Internet Snackbar
        if (noInternetSnackbar != null) {
            noInternetSnackbar.destroy();
        }


        // Location
        dismissProgress();
    }


    LinearLayout lin_register, lin_create;
    private Context mContext = HomeActivity.this;

    private DashboardAdapter adapter;
    private List<dashboard> dashboardList;
    String user_id;
    RecyclerView rv_register;
    AdhocAdapter registerAdapter;
    // ProgressDialog progressDialog;

    private ProgressBar progressBar;

    ArrayList<PageItemsAdhoc> pageItemArrayListAdhoc;
    private PopupWindow POPUP_WINDOW_SCORE = null;

    AppBarLayout Appbar;
    Toolbar toolbar;
    CardView register_farmer, view_farmer, recruit_farmer, contract_signing, verify_planting, crop_destruction, harvest_collection, inventory_mgt, dashboard;
    CardView register_block_Card, harvest_block_Card, plant_block_Card;

    boolean ExpandedActionBar = true;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    void startJob() {
        JobInfo myJob = new JobInfo.Builder(0, new ComponentName(this, CheckNetworkJob.class))
                .setMinimumLatency(1000)
                .setOverrideDeadline(2000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(myJob);
    }


    public void networkTest(View view){
        Intent intent = new Intent(getApplicationContext(), NetworkTestingActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//REPORT CRASH IN THIS ACTIVITY
        Catcho.Builder(this)
                .recipients(Constants.CRASH_REPORT_EMAIL)
                .build();

        setContentView(R.layout.activity_home);



        //HANDLES NOTIFICATIONS FOR INTERNET CONNECTION
        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().getBoolean("FromNotification")) {
                    new NotificationNotify(this).cancelNotificationDisplay();

                    ImageView img_offline_sync = findViewById(R.id.img_offline_sync);

                    TutoShowcase.from(this)
                            .setListener(new TutoShowcase.Listener() {
                                @Override
                                public void onDismissed() {
//                                    Toast.makeText(TutoShowcaseMainActivity.this, "Tutorial dismissed", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setContentView(R.layout.tuto_showcase_tuto_layout)
                            .setFitsSystemWindows(true)
                            .on(img_offline_sync)
                            .addCircle()
                            .withBorder()

                            .onClick(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .show();

                    //.on(R.id.menu_show)
                    // .displaySwipableLeft()
                    //.delayed(399)
                    // .animated(true)
                    //.show();


                } else {
                    startJob();
                }
            } else {
                startJob();
            }
        } else {
            startJob();
        }

        Appbar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        progressBar = findViewById(R.id.progress);
        //progressDialog = new ProgressDialog(this);
        // get the listview
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        expListView = (ExpandableListView) findViewById(R.id.verifyplanting_list);
        rv_register = findViewById(R.id.adhoc_recylerview);
        rv_register.setLayoutManager(new LinearLayoutManager(this));
        int width = getResources().getDisplayMetrics().widthPixels;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            expListView.setIndicatorBounds(width - getPixelValue(40), width - getPixelValue(10));
        } else {
            expListView.setIndicatorBoundsRelative(width - getPixelValue(40), width - getPixelValue(10));
        }
        SharedPreferences prefs = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
        Set<String> permission = prefs.getStringSet("key", null);


        if (permission == null) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            return;
        }

        if (!permission.contains("ALL_FUNCTIONS")) {
            register_farmer = findViewById(R.id.register_farmer);
            if (!permission.contains("CREATE_FARMER")) {
                register_farmer.setVisibility(View.GONE);
            }
            view_farmer = findViewById(R.id.view_farmer);
            if (!permission.contains("READ_FARMER_MOBILE")) {
                view_farmer.setVisibility(View.GONE);
            }
            recruit_farmer = findViewById(R.id.recruit_farmer);
            if (!permission.contains("RECRUIT_FARMER_MOBILE")) {
                recruit_farmer.setVisibility(View.GONE);
            }
            contract_signing = findViewById(R.id.contract_signing);
            if (!permission.contains("CONTRACT_FARMER_MOBILE")) {
                contract_signing.setVisibility(View.GONE);
            }
            verify_planting = findViewById(R.id.verify_planting);
            if (!permission.contains("VERIFY_PLANTING_MOBILE")) {
                verify_planting.setVisibility(View.GONE);
            }
            crop_destruction = findViewById(R.id.crop_destruction);
            if (!permission.contains("CROP_DESTRUCTION_MOBILE")) {
                crop_destruction.setVisibility(View.GONE);
            }
            harvest_collection = findViewById(R.id.harvest_collection);
            if (!permission.contains("HARVEST_COLLECTION_MOBILE")) {
                harvest_collection.setVisibility(View.GONE);
            }
            dashboard = findViewById(R.id.dashboard);
            inventory_mgt = findViewById(R.id.inventory_mgt);

            register_block_Card = findViewById(R.id.register_block_Card);
            harvest_block_Card = findViewById(R.id.harvest_block_Card);
            plant_block_Card = findViewById(R.id.plant_block_Card);

            checkPermissionSetVisibility(permission.contains("READ_HARVESTBLOCK"), findViewById(R.id.harvest_block_Card));

            checkPermissionSetVisibility(permission.contains("READ_IRRIGATEBLOCK"), findViewById(R.id.irrigation_block_Card));

            checkPermissionSetVisibility(permission.contains("READ_APPLYFERTILIZER"), findViewById(R.id.apply_fertilizer_block_Card));

            checkPermissionSetVisibility(permission.contains("READ_APPLYFERTILIZER"), findViewById(R.id.scouting_block_Card));

            checkPermissionSetVisibility(permission.contains("READ_PLANTBLOCK"), findViewById(R.id.plant_block_Card));


        }

        user_id = prefs.getString("userid", "-1");
        setSupportActionBar(toolbar);
        // preparing list data
        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
            prepareListData();
        } else {

            listData((List<PageItemstask>) OfflineFeature.getSharedPreferences("PageItemsTasks",getApplicationContext(),PageItemstask.class));
        }


       // offlinesync();

    }

    private void checkPermissionSetVisibility(boolean visibility, View view) {
        if (!visibility) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
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

    private void setData() {


    }

    public void openregister(View v) {
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
    }

    public void openfarmerlist(View v) {
        startActivity(new Intent(HomeActivity.this, ListFarmerActivity.class));
    }

    public void openrecruit(View v) {

        startActivity(new Intent(HomeActivity.this, FarmerRecruitActivity.class));
    }

    public void opencontract(View v) {
        startActivity(new Intent(HomeActivity.this, ContractSignActivity.class));
    }

    public void opendestruction(View v) {
        startActivity(new Intent(HomeActivity.this, CropDestructionActivity.class));
    }

    public void openharvesting(View v) {
        startActivity(new Intent(HomeActivity.this, HarvestingActivity.class));
    }

    public void registerBlock(View view) {
        startActivity(new Intent(HomeActivity.this, RegisterBlockActivity.class));
    }

    public void harvestBlock(View vivew) {
        startActivity(new Intent(HomeActivity.this, HarvestBlockActivity.class));
    }

    public void plantBlock(View view) {
        startActivity(new Intent(HomeActivity.this, PlantBlockActivity.class));
    }

    public void irrigateBlock(View view) {
        startActivity(new Intent(HomeActivity.this, IrrigateBlockActivity.class));
        // TODO: 2020-06-06 Testing for Location using SampleActivity. 
        //startActivity(new Intent(HomeActivity.this, SampleActivity.class));
    }

    public void scoutingBlock(View view) {
        startActivity(new Intent(HomeActivity.this, ScoutingBlockActivity.class));
    }

    public void applyFertilizer(View view) {
        startActivity(new Intent(HomeActivity.this, ApplyFertilizerBlockActivity.class));
    }


    public void openplantverfication(View v) {
        startActivity(new Intent(HomeActivity.this, PlantingVerificationActivity.class));
    }

    public void opensprayconfirmation(View v) {
        startActivity(new Intent(HomeActivity.this, SprayConfirmationActivity.class));
    }

    public void opendashboard(View v) {
        startActivity(new Intent(HomeActivity.this, MyDashboardActivity.class));
    }

    private void listData(List<PageItemstask> list) {
        try {
            // TODO: 2020-06-02 Until they say what this notificaiton is for, we can not continue using it.
//                        int NOTIFICATION_ID = 23134;
//                        String CHANNEL_ID = "my_channel_01";
//                        CharSequence name = "my_channel";
//                        String Description = "This is my channel";
//                        NotificationManager notificationManager = (NotificationManager) HomeActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
//
//                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//
//                            int importance = NotificationManager.IMPORTANCE_HIGH;
//                            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
//                            mChannel.setDescription(Description);
//                            mChannel.enableLights(true);
//                            mChannel.setLightColor(Color.RED);
//                            mChannel.enableVibration(true);
//                          //  mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//                            mChannel.setShowBadge(false);
//                            notificationManager.createNotificationChannel(mChannel);
//                        }
//
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(HomeActivity.this, CHANNEL_ID)
//                                .setSmallIcon(R.drawable.logo_nice)
//                                .setContentTitle("You have a new task")
//                                .setContentText("Open farmer application to view the tasks");
//
//                        notificationManager.notify(NOTIFICATION_ID, builder.build());

            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();

            // Adding child data
            listDataHeader.add("Planting");
            listDataHeader.add("Spray");
            listDataHeader.add("Return");
            List<String> message = new ArrayList<String>();
            List<String> invent = new ArrayList<String>();
            List<String> return_inve = new ArrayList<String>();
            for (PageItemstask taks : list) {

                if (taks.getEntityName().equals("VERIFY_PLANTING_MOBILE")) {
                    String date = "";
                    for (int elem : taks.getCropDate()) {
                        date = elem + "/" + date;
                    }
                    invent.add(taks.getCentrename()/*0*/ + " ,planting,"/*1*/ + removeLastChar(date)/*2*/ + " ," + taks.getEntityId()/*3*/ + "," + taks.getId()/*4*/);
                } else if (taks.getEntityName().equals("VERIFY_SPRAY_MOBILE")) {
                    String date = "";
                    for (int elem : taks.getCropDate()) {
                        date = elem + "/" + date;
                    }
                    invent.add(taks.getCentrename() + " ,planting," + removeLastChar(date) + " ," + taks.getEntityId() + "," + taks.getId());
                } else {
                    String date = "";
                    for (int elem : taks.getCreatedOn()) {
                        date = elem + "/" + date;
                    }
                    return_inve.add(taks.getCentrename() + " ,return," + removeLastChar(date) + " ," + taks.getEntityId() + "," + taks.getId());
                }




            listDataChild.put(listDataHeader.get(0), invent); // Header, Child data
            listDataChild.put(listDataHeader.get(1), message);
            listDataChild.put(listDataHeader.get(2), return_inve);


            listAdapter = new ExpandableListAdapter(HomeActivity.this, listDataHeader, listDataChild);
            // setting list adapter
            expListView.setAdapter(listAdapter);
            adhoc();

        }

    } catch(
    Exception e)

    {
        Utility.showToast(HomeActivity.this, e.getMessage());
        //System.out.println(e.getMessage());
    }

}
    private void prepareListData() {


//        progressDialog.setCancelable(false);
//        // progressBar.setMessage("Please Wait...");
//        progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
        APIService service = retrofit.create(APIService.class);
        SharedPreferences prefs = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
        String auth_key = prefs.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
        Call<TasksResponse> call = service.gettask(auth_key, user_id + "");
        call.enqueue(new Callback<TasksResponse>() {
            @Override
            public void onResponse(Call<TasksResponse> call, Response<TasksResponse> response) {
              //  progressDialog.dismiss();
                progressBar.setVisibility(View.GONE);

                    if (response.body().getTotalFilteredRecords() > 0) {

                        listData(response.body().getPageItemstasks());
                        OfflineFeature.saveSharedPreferences(response.body().getPageItemstasks(), "PageItemsTasks", getApplicationContext());


                    }

            }

            @Override
            public void onFailure(Call<TasksResponse> call, Throwable t) {
//                progressDialog.hide();
                progressBar.setVisibility(View.GONE);
               Utility.showToast(HomeActivity.this, t.getMessage());
            }
        });

    }

    public void adhoc() {
            progressBar.setVisibility(View.GONE);
//        progressDialog.setCancelable(false);
//        // progressBar.setMessage("Please Wait...");
//        progressDialog.dismiss();
        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
            APIService service = retrofit.create(APIService.class);
            SharedPreferences prefs = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
            String auth_key = prefs.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
            Call<AdhocResponse> call = service.getAdhoc(user_id, auth_key);
            call.enqueue(new Callback<AdhocResponse>() {
                @Override
                public void onResponse(Call<AdhocResponse> call, Response<AdhocResponse> response) {
                    //progressDialog.hide();
                    progressBar.setVisibility(View.GONE);
                    try {
                        if (response.body().getPageItemsAdhoc().size() != 0) {
                            int NOTIFICATION_ID = 23135;
                            String CHANNEL_ID = "my_channel_02";
                            CharSequence name = "my_channel";
                            String Description = "This is my channel";
                            NotificationManager notificationManager = (NotificationManager) HomeActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);

                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                                int importance = NotificationManager.IMPORTANCE_HIGH;
                                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                                mChannel.setDescription(Description);
                                mChannel.enableLights(true);
                                mChannel.setLightColor(Color.RED);
                                mChannel.enableVibration(true);
                                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                                mChannel.setShowBadge(false);
                                notificationManager.createNotificationChannel(mChannel);
                            }

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(HomeActivity.this, CHANNEL_ID)
                                    .setSmallIcon(R.drawable.logo_nice)
                                    .setContentTitle("You have a new message")
                                    .setContentText("Open farmer application to view the messages");

                            notificationManager.notify(NOTIFICATION_ID, builder.build());
                            pageItemArrayListAdhoc = (ArrayList<PageItemsAdhoc>) response.body().getPageItemsAdhoc();
                            registerAdapter = new AdhocAdapter(rv_register, HomeActivity.this, pageItemArrayListAdhoc);
                            DividerItemDecoration itemDecor = new DividerItemDecoration(HomeActivity.this, HORIZONTAL);
                            rv_register.addItemDecoration(itemDecor);
                            rv_register.setAdapter(registerAdapter);
                        } else {
                            Toast.makeText(HomeActivity.this, "No message found", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Utility.showToast(HomeActivity.this, e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<AdhocResponse> call, Throwable t) {
                   // progressDialog.hide();
                    progressBar.setVisibility(View.GONE);
                    Utility.showToast(HomeActivity.this, t.getMessage());
                }
            });
        }
    }
    public int getPixelValue(int dp) {

        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    SweetAlertDialog pDialog;

    public void  offlinesync(View v) {

        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
            final Context context = this;
            //PUSH
            AsyncTask asyncTask = new AsyncTask() {
                @Override
                protected void onPreExecute() {
                    pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Data Sync Process in progress...");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    //Toast.makeText(HomeActivity.this, "Synchronisation process has started", Toast.LENGTH_SHORT).show();
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Object[] objects) {



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


                            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                            APIService service = retrofit.create(APIService.class);
                            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                            Call<FarmerErrorResponse> call = service.createFarmer(auth_key, farmerModel);
                            call.enqueue(new Callback<FarmerErrorResponse>() {
                                @Override
                                public void onResponse(Call<FarmerErrorResponse> call, Response<FarmerErrorResponse> response) {
                                    if (response.isSuccessful()) farmer.delete();


                                }

                                @Override
                                public void onFailure(Call<FarmerErrorResponse> call, Throwable t) {

                                }
                            });
                        }
//
//
                        List<RecruitFarmerDB> recruits = RecruitFarmerDB.listAll(RecruitFarmerDB.class);
                        for (RecruitFarmerDB recruit : recruits) {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("farmerid", recruit.farmerid);
                            hashMap.put("dateid", recruit.dateid);
                            hashMap.put("landownership", recruit.landownership);
                            hashMap.put("cordinates", recruit.cordinates);
                            hashMap.put("location", "Offline");
                            hashMap.put("noofunits", recruit.noofunits);
                            hashMap.put("section", recruit.section);


                            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                            APIService service = retrofit.create(APIService.class);
                            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                            Call<AllResponse> call = service.recruit(auth_key, hashMap);
                            call.enqueue(new Callback<AllResponse>() {
                                @Override
                                public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                                    if (response.isSuccessful()) recruit.delete();

                                }

                                @Override
                                public void onFailure(Call<AllResponse> call, Throwable t) {

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
                            hashMap.put("recruitId", sign.recruitId);


                            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                            APIService service = retrofit.create(APIService.class);
                            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                            Call<AllResponse> call = service.postcontract(auth_key, hashMap);
                            call.enqueue(new Callback<AllResponse>() {
                                @Override
                                public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                                    if (response.isSuccessful()) sign.delete();
                                }

                                @Override
                                public void onFailure(Call<AllResponse> call, Throwable t) {

                                }
                            });
                        }

//
                        List<PlantingVerifyDB> plants = PlantingVerifyDB.listAll(PlantingVerifyDB.class);
                        for (PlantingVerifyDB plant : plants) {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("cordinates", plant.cordinates);
                            hashMap.put("location", "Offline");
                            hashMap.put("contractid", plant.contractid);
                            hashMap.put("plantconfirmed", plant.plant_value);
                            hashMap.put("waterconfirmed", plant.water_value);


                            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                            APIService service = retrofit.create(APIService.class);
                            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                            Call<AllResponse> call = service.postplantverify(auth_key, hashMap);
                            call.enqueue(new Callback<AllResponse>() {
                                @Override
                                public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                                    if (response.isSuccessful()) plant.delete();
                                }

                                @Override
                                public void onFailure(Call<AllResponse> call, Throwable t) {

                                }
                            });
                        }

                        List<CropDestructionPostDB> destroys = CropDestructionPostDB.listAll(CropDestructionPostDB.class);
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
                            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                            Call<AllResponse> call = service.postcropdestruction(auth_key, hashMap);
                            call.enqueue(new Callback<AllResponse>() {
                                @Override
                                public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                                    if (response.isSuccessful()) destroy.delete();
                                }

                                @Override
                                public void onFailure(Call<AllResponse> call, Throwable t) {

                                }
                            });
                        }


//                   SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
//                   pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//                   pDialog.setTitleText("Data Sync Process in progress...");
//                   pDialog.setCancelable(false);
//                   pDialog.show();
                        List<HarvestingDB> harvests = HarvestingDB.listAll(HarvestingDB.class);
                        for (HarvestingDB harvest : harvests) {

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("dateid", harvest.dateid);
                            hashMap.put("noofunits", harvest.noofunits);
                            hashMap.put("contractId", harvest.contractId);
                            hashMap.put("farmerid", harvest.farmerid);
                            hashMap.put("harvestkilos", harvest.harvestkilos);
                            hashMap.put("locale", harvest.locale);

                            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                            APIService service = retrofit.create(APIService.class);
                            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                            Call<AllResponse> call = service.postharvesting(auth_key, hashMap);
                            call.enqueue(new Callback<AllResponse>() {
                                @Override
                                public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                                    if (response.isSuccessful()) harvest.delete();
                                }

                                @Override
                                public void onFailure(Call<AllResponse> call, Throwable t) {

                                }
                            });
                        }


//                   SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
//                   pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//                   pDialog.setTitleText("Data Sync Process in progress...");
//                   pDialog.setCancelable(false);
//                   pDialog.show();

                        List<SprayPostDB> sprays = SprayPostDB.listAll(SprayPostDB.class);
                        for (SprayPostDB spray : sprays) {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("verificationid", spray.verificationid);
                            hashMap.put("cordinates", spray.cordinates);
                            hashMap.put("location", "Offline");
                            hashMap.put("sprayconfirmed", spray.sprayconfirmed);
                            hashMap.put("programid", spray.programid);


                            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                            APIService service = retrofit.create(APIService.class);
                            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                            Call<AllResponse> call = service.spraypost(auth_key, hashMap);
                            call.enqueue(new Callback<AllResponse>() {
                                @Override
                                public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                                    if (response.isSuccessful()) spray.delete();
                                }

                                @Override
                                public void onFailure(Call<AllResponse> call, Throwable t) {

                                }
                            });
                        }


                        //new features
                        List<ApplyFertilizerTB> applyList = ApplyFertilizerTB.listAll(ApplyFertilizerTB.class);
                        for (ApplyFertilizerTB apply : applyList) {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("blockId", apply.getBlockId());
                            hashMap.put("fertilizerId", apply.getFertilizerId());
                            hashMap.put("appliedRate", apply.getAppliedRate());
                            hashMap.put("method", apply.getMethod());
                            hashMap.put("equipment", apply.getEquipment());
                            hashMap.put("locale", "en");

                            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                            APIService service = retrofit.create(APIService.class);
                            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                            Call<ResponseBody> call = service.postApplyFertilizer(auth_key, hashMap);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {

                                        if (response.isSuccessful()) {
                                            apply.delete();
                                        }

                                    } catch (Exception e) {
                                        pDialog.dismissWithAnimation();
                                    }

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {


                                }
                            });

                        }

                        List<HarvestBlockTB> harvestList = HarvestBlockTB.listAll(HarvestBlockTB.class);
                        for (HarvestBlockTB harvest : harvestList) {

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("harvestKilos", harvest.getHarvestKilos());
                            hashMap.put("blockId", harvest.getBlockId());
                            hashMap.put("locale", "en");//should this be hardcoded?

                            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                            APIService service = retrofit.create(APIService.class);
                            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                            Call<ResponseBody> call = service.postHarvestBlocks(auth_key, hashMap);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        harvest.delete();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
//
                                }
                            });
                        }


                        List<IrrigateBlockTB> irrigateList = IrrigateBlockTB.listAll(IrrigateBlockTB.class);
                        for (IrrigateBlockTB irrigate : irrigateList) {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("blockId", irrigate.getBlockId());
                            hashMap.put("irrigationHours", irrigate.getIrrigationHours());
                            hashMap.put("cubicLitres", irrigate.getCubicLitres());
                            hashMap.put("locale", "en");


                            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                            APIService service = retrofit.create(APIService.class);
                            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                            Call<ResponseBody> call = service.postIrrigateBlock(auth_key, hashMap);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    if (response.isSuccessful()) {
                                        irrigate.delete();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                        }


                        List<PlantBlockTB> plantBlockList = PlantBlockTB.listAll(PlantBlockTB.class);
                        for (PlantBlockTB plant : plantBlockList) {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("bagsPlanted", plant.getBagsPlanted());
                            hashMap.put("seedRate", plant.getSeedRate());
                            hashMap.put("varietyId", plant.getVarietyId());
                            hashMap.put("blockId", plant.getBlockId());
                            hashMap.put("cordinates", plant.getCordinates());
                            hashMap.put("location", plant.getLocation());
                            hashMap.put("bagLotNo", plant.getBagLotNo());

                            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                            APIService service = retrofit.create(APIService.class);
                            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                            Call<ResponseBody> call = service.postPlantBlock(auth_key, hashMap);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    if (response.isSuccessful()) {

                                        plant.delete();

                                    }

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                        }


                        List<RegisterBlockTB> registerBlockList = RegisterBlockTB.listAll(RegisterBlockTB.class);
                        for (RegisterBlockTB register : registerBlockList) {

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("centerId", register.getCenterID());//ask about this
                            hashMap.put("farmNameId", register.getFarmNameId());
                            hashMap.put("blockName", register.getBlockName());

                            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                            APIService service = retrofit.create(APIService.class);
                            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                            Call<ResponseBody> call = service.postRegisterBlock(auth_key, hashMap);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    if (response.isSuccessful()) {
                                        register.delete();
                                    }

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
//
                                }
                            });
                        }

                        List<ScoutingTB> scoutingList = ScoutingTB.listAll(ScoutingTB.class);
                        for (ScoutingTB scouting : scoutingList) {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("blockId", scouting.getBlockId());
                            hashMap.put("germination", scouting.getGermination());
                            hashMap.put("weeded", scouting.getWeeded());
                            hashMap.put("watered", scouting.getWatered());
                            hashMap.put("survivalRate", scouting.getSurvivalRate());
                            hashMap.put("floweringRate", scouting.getFloweringRate());
                            hashMap.put("averagePods", scouting.getAveragePods());
                            hashMap.put("locale", "en");

                            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                            APIService service = retrofit.create(APIService.class);
                            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                            Call<ResponseBody> call = service.postScoutMonitor(auth_key, hashMap);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        scouting.delete();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                        }

                        List<ChangeCentreTB> centreList = ChangeCentreTB.listAll(ChangeCentreTB.class);
                        for (ChangeCentreTB centre : centreList) {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("activate", "N");
                            hashMap.put("farmerid", centre.getFarmerId());
                            hashMap.put("centerid", centre.getCenterId());

                            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                            APIService service = retrofit.create(APIService.class);
                            SharedPreferences preffs = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                            String auth_key = preffs.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                            Call<AllResponse> call = service.postchangecentre(auth_key, hashMap);
                            call.enqueue(new Callback<AllResponse>() {
                                @Override
                                public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                                    if (response.isSuccessful()) centre.delete();
                                }

                                @Override
                                public void onFailure(Call<AllResponse> call, Throwable t) {

                                }
                            });
                        }


                        List<EditFarmerDetailsTB> editFarmer = EditFarmerDetailsTB.listAll(EditFarmerDetailsTB.class);
                        for (EditFarmerDetailsTB details : editFarmer) {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("farmerId", details.getFarmerId());
                            hashMap.put("firstname", details.getFirstName());
                            hashMap.put("middlename", details.getMiddleName());
                            hashMap.put("lastname", details.getLastName());
                            hashMap.put("mobileno", details.getMobileNo());
                            hashMap.put("Gender", details.getGender());
                            hashMap.put("dateOfBirth", details.getDateOfBirth());
                            hashMap.put("dateFormat", details.getDateFormat());
                            hashMap.put("locale", details.getLocale());

                            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                            APIService service = retrofit.create(APIService.class);
                            SharedPreferences prefss = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                            String auth_key = prefss.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                            Call<AllResponse> call = service.posteditfarmer(auth_key, hashMap, Integer.valueOf(details.getFarmerId()));
                            call.enqueue(new Callback<AllResponse>() {
                                @Override
                                public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {

                                    if (response.isSuccessful()) {
                                        details.delete();
                                    }
                                }

                                @Override
                                public void onFailure(Call<AllResponse> call, Throwable t) {

                                }
                            });
                        }

                    }
                    return null;
                }


                @Override
                protected void onPostExecute(Object o) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new OfflineFeature(0, false).silentDataDump(context);
                        }
                    }, 4000);

                    pDialog.dismissWithAnimation();
                    Toast.makeText(HomeActivity.this, "Synchronisation process has ended", Toast.LENGTH_SHORT).show();
                    super.onPostExecute(o);
                }
            };
            asyncTask.execute();
        }else{

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("No network connection")
                    .setContentText("Connect to the internet and try again.")
                    .setConfirmText("OK")
                    .show();

        }



    }

    public void ShowPopup(View v) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        // Inflate the popup_layout.xml
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.layout_popup, null);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                POPUP_WINDOW_SCORE.dismiss();
            }
        });

        // Creating the PopupWindow
        POPUP_WINDOW_SCORE = new PopupWindow(this);
        POPUP_WINDOW_SCORE.setContentView(layout);
        POPUP_WINDOW_SCORE.setWidth(width);
        POPUP_WINDOW_SCORE.setHeight(height);
        POPUP_WINDOW_SCORE.setFocusable(true);

        // prevent clickable background
        POPUP_WINDOW_SCORE.setBackgroundDrawable(null);

        POPUP_WINDOW_SCORE.showAtLocation(layout, Gravity.CENTER, 1, 1);
        SharedPreferences prefs = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
        String center_names = prefs.getString("center_names", " ");
        String username = prefs.getString("name", " ");

        TextView txtMessage = (TextView) layout.findViewById(R.id.layout_popup_txtMessage);
        txtMessage.setText("Name: " + username);

        TextView txtCentre = (TextView) layout.findViewById(R.id.layout_popup_txtcentre);
        txtCentre.setText("Centres: " + center_names);

        // Getting a reference to button one and do something
        Button butOne = (Button) layout.findViewById(R.id.layout_popup_butOne);
        butOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                POPUP_WINDOW_SCORE.dismiss();
                SharedPreferences settings = getSharedPreferences("PERMISSIONS", Context.MODE_PRIVATE);
                settings.edit().clear().commit();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));

            }
        });

    }


    @Override
    public void onBackPressed() {
        Context context = HomeActivity.this;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            super.onBackPressed();
        }else
        {

            super.onBackPressed();

            //ALVIN, ASKED THIS TO BE REMOVED
//            new AlertDialog.Builder(context)
//                    .setCancelable(false)
//                    .setTitle("Disable GPS")  // GPS not found
//                    .setMessage("If exiting the farmer app, disable your gps.") // Want to enable?
//                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                        }
//                    })
//                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.cancel();
//                        }
//                    })
//                    .show();

        }

    }


}
