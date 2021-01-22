package com.cw.farmer.offlinefunctions;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.farmer.NetworkUtil;
import com.cw.farmer.R;
import com.cw.farmer.adapter.OfflineDataRecyclerAdapter;
import com.cw.farmer.model.Accountdetails;
import com.cw.farmer.model.AllResponse;
import com.cw.farmer.model.ContractSignDB;
import com.cw.farmer.model.CropDestructionPostDB;
import com.cw.farmer.model.EditContentModel;
import com.cw.farmer.model.FarmerErrorResponse;
import com.cw.farmer.model.FarmerModel;
import com.cw.farmer.model.FarmerModelDB;
import com.cw.farmer.model.HarvestingDB;
import com.cw.farmer.model.Identitydetails;
import com.cw.farmer.model.PlantingVerifyDB;
import com.cw.farmer.model.RecruitFarmerDB;
import com.cw.farmer.model.SprayPostDB;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.cw.farmer.table_models.ApplyFertilizerTB;
import com.cw.farmer.table_models.ChangeCentreTB;
import com.cw.farmer.table_models.EditFarmerDetailsTB;
import com.cw.farmer.table_models.HarvestBlockTB;
import com.cw.farmer.table_models.IrrigateBlockTB;
import com.cw.farmer.table_models.PlantBlockTB;
import com.cw.farmer.table_models.RegisterBlockTB;
import com.cw.farmer.table_models.ScoutingTB;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OfflineDataSyncActivity extends AppCompatActivity {

    private RecyclerView offlineDataSyncRc;
    private RecyclerView.LayoutManager layoutManager;
    private OfflineDataRecyclerAdapter mAdapter;
    static List<OfflineDataItem> offlineDataItemList;
    static List<OfflineDataItem> offlineUploadDataItemList;
    public static ArrayList<EditContentModel> registerfarmer = new ArrayList<>();
    public static ArrayList<EditContentModel> harvestCollection = new ArrayList<>();


    private void populateUploadData(){
        OfflineDataItem farmerModelItem = new OfflineDataItem(
                FarmerModelDB.class,
                0,
                FarmerModelDB.listAll(FarmerModelDB.class).size(),
                false,
                ""
        );
        if(farmerModelItem.getDataItemSize() > 0) offlineDataItemList.add(farmerModelItem);
    }

    private int offline = 0;





    private void populateData(){
        OfflineDataItem farmerModelItem = new OfflineDataItem(
                FarmerModelDB.class,
                0,
                FarmerModelDB.listAll(FarmerModelDB.class).size(),
                false,
                "Register Farmer"
        );
        if(farmerModelItem.getDataItemSize() > 0) offlineDataItemList.add(farmerModelItem);

        OfflineDataItem recruitFarmerItem = new OfflineDataItem(
                RecruitFarmerDB.class,
                0,
                RecruitFarmerDB.listAll(RecruitFarmerDB.class).size(),
                false,
                "Recruit Farmer"
        );
        if(recruitFarmerItem.getDataItemSize() > 0) offlineDataItemList.add(recruitFarmerItem);

        OfflineDataItem contractSignItem = new OfflineDataItem(
                ContractSignDB.class,
                0,
                ContractSignDB.listAll(ContractSignDB.class).size(),
                false,
                "Contract Signing"
        );
        if(contractSignItem.getDataItemSize() > 0) offlineDataItemList.add(contractSignItem);

        OfflineDataItem plantingVerifyItem = new OfflineDataItem(
                PlantingVerifyDB.class,
                0,
                PlantingVerifyDB.listAll(PlantingVerifyDB.class).size(),
                false,
                "Verify Planting"
        );
        if(plantingVerifyItem.getDataItemSize() > 0) offlineDataItemList.add(plantingVerifyItem);

        OfflineDataItem cropDestructionItem = new OfflineDataItem(
                CropDestructionPostDB.class,
                0,
                CropDestructionPostDB.listAll(CropDestructionPostDB.class).size(),
                false,
                "Crop Destruction"
        );
        if(cropDestructionItem.getDataItemSize() > 0) offlineDataItemList.add(cropDestructionItem);

        OfflineDataItem harvestItem = new OfflineDataItem(
                HarvestingDB.class,
                0,
                HarvestingDB.listAll(HarvestingDB.class).size(),
                false,
                "Harvest Collection"
        );
        if(harvestItem.getDataItemSize() > 0) offlineDataItemList.add(harvestItem);

        OfflineDataItem sprayPostItem = new OfflineDataItem(
                SprayPostDB.class,
                0,
                SprayPostDB.listAll(SprayPostDB.class).size(),
                false,
                "Spray Confirmation"
        );
        if(sprayPostItem.getDataItemSize() > 0) offlineDataItemList.add(sprayPostItem);

        OfflineDataItem applyFertilizerItem = new OfflineDataItem(
                ApplyFertilizerTB.class,
                0,
                ApplyFertilizerTB.listAll(ApplyFertilizerTB.class).size(),
                false,
                "Apply Fertiliser"
        );
        if(applyFertilizerItem.getDataItemSize() > 0) offlineDataItemList.add(applyFertilizerItem);

        OfflineDataItem harvestBlockItem = new OfflineDataItem(
                HarvestBlockTB.class,
                0,
                HarvestBlockTB.listAll(HarvestBlockTB.class).size(),
                false,
                "Harvest block"
        );
        if(harvestBlockItem.getDataItemSize() > 0) offlineDataItemList.add(harvestBlockItem);

        OfflineDataItem irrigateBlockItem = new OfflineDataItem(
                IrrigateBlockTB.class,
                0,
                IrrigateBlockTB.listAll(IrrigateBlockTB.class).size(),
                false,
                "Irrigate Block"
        );
        if(irrigateBlockItem.getDataItemSize() > 0) offlineDataItemList.add(irrigateBlockItem);

        OfflineDataItem plantBlockItem = new OfflineDataItem(
                PlantBlockTB.class,
                0,
                PlantBlockTB.listAll(PlantBlockTB.class).size(),
                false,
                "Plant Block"
        );
        if(plantBlockItem.getDataItemSize() > 0) offlineDataItemList.add(plantBlockItem);

        OfflineDataItem registerBlockItem = new OfflineDataItem(
                RegisterBlockTB.class,
                0,
                RegisterBlockTB.listAll(RegisterBlockTB.class).size(),
                false,
                "Register Block"
        );
        if(registerBlockItem.getDataItemSize() > 0) offlineDataItemList.add(registerBlockItem);

        OfflineDataItem scoutingItem = new OfflineDataItem(
                ScoutingTB.class,
                0,
                ScoutingTB.listAll(ScoutingTB.class).size(),
                false,
                "Scouting"
        );
        if(scoutingItem.getDataItemSize() > 0) offlineDataItemList.add(scoutingItem);

        OfflineDataItem changeCentreItem = new OfflineDataItem(
                ChangeCentreTB.class,
                0,
                ChangeCentreTB.listAll(ChangeCentreTB.class).size(),
                false,
                "Change Centre"
        );

        if(changeCentreItem.getDataItemSize() > 0) offlineDataItemList.add(changeCentreItem);

        OfflineDataItem editFarmerDetailsItem = new OfflineDataItem(
                EditFarmerDetailsTB.class,
                0,
                EditFarmerDetailsTB.listAll(EditFarmerDetailsTB.class).size(),
                false,
                "Edit Farmer Details"
        );
        if(editFarmerDetailsItem.getDataItemSize() > 0) offlineDataItemList.add(editFarmerDetailsItem);


        if(offlineDataItemList.size() > 0){
            findViewById(R.id.no_content).setVisibility(View.GONE);
        }else{
            findViewById(R.id.no_content).setVisibility(View.VISIBLE);
        }
    }


    private OfflineDataItem getOfflineDataItem(Object obj){
        OfflineDataItem returnObject = null;

        for(OfflineDataItem item : offlineDataItemList){
            if(item.getDataItemObject().equals(obj)){
                returnObject = item;
                break;
            }
        }

        return returnObject;
    }


    private void upateProgress(Object obj, boolean error, String errorMessage){

        OfflineDataItem item =  getOfflineDataItem(obj);
        if(item != null) {
            if(!error) {
                item.setDataItemProgress(item.getDataItemProgress() + 1);
                if (item.getDataItemProgress() == item.getDataItemSize())
                    item.setDataItemCompleteStatus(true);
                else
                    item.setDataItemCompleteStatus(false);
            }else{
                item.setDataItemCompleteStatus(false);
                item.setDateItemErrorText(errorMessage);
            }

            populateRecycler(offlineDataItemList, true);
        }

    }


    private TextView progressTitle,progress_indicator,complete_status,error_message;
    private ProgressBar progress;
    private Button btn_checkData;


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_sync_activity_layout);
        registerfarmer.clear();
        harvestCollection.clear();
        progress = findViewById(R.id.progress);
        progressTitle = findViewById(R.id.progressTitle);
        progressTitle.setVisibility(View.GONE);
        progress_indicator = findViewById(R.id.progress_indicator);
        complete_status = findViewById(R.id.complete_status);
        btn_checkData = findViewById(R.id.btn_checkData);
        error_message = findViewById(R.id.error_message);

        offlineDataSyncRc = findViewById(R.id.offline_recycler);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        offlineDataSyncRc.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        offlineDataSyncRc.setLayoutManager(layoutManager);

        offlineDataItemList = new ArrayList<>();
        offlineDataItemList.clear();

//        private Object DataItemObject;
//        private int DataItemProgress;
//        private int DataItemSize;
//        private boolean DataItemCompleteStatus;
//        private String DateItemTitle;

        populateData();

        if(offlineDataItemList.size() > 0){

        }else{
            new OfflineDataRecyclerAdapter(offlineDataItemList, OfflineDataSyncActivity.this,
                    progressTitle,progress_indicator,complete_status,error_message,
                    progress,btn_checkData).silentDataDump(OfflineDataSyncActivity.this);
        }

        populateRecycler(offlineDataItemList, false);

                if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {

                    List<FarmerModelDB> farmers = FarmerModelDB.listAll(FarmerModelDB.class);

                    for (FarmerModelDB farmer : farmers) {

                        FarmerModel farmerModel = new FarmerModel();
                        Accountdetails accountdetails = new Accountdetails();
                        Identitydetails identitydetails = new Identitydetails();

                        farmerModel.setFirstname(farmer.firstname);
                        farmerModel.setMobileno(farmer.mobileno);
                        farmerModel.setEmail(farmer.email);
                        farmerModel.setGender(farmer.gender);
                        farmerModel.setIdno(farmer.idno);
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

//123456789464664648464546846
//123456789464664648464546846
                                //ID Number  does not conform the standard...

                                if (response.code() == 200) {
                                    upateProgress(FarmerModelDB.class, false,"");
                                    farmer.delete();
                                }else {

                                    try {

                                        String error = response.errorBody() != null ?
                                                new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                                : response.message();
                                        EditContentModel model = new EditContentModel(farmer.firstname,
                                                farmer,
                                                error,
                                                farmer.getId(),
                                                false);
                                        registerfarmer.add(model);

                                        upateProgress(FarmerModelDB.class, true, error);

                                    } catch (Exception e) {
                                        upateProgress(FarmerModelDB.class, true, e.getMessage());
                                    }


                                }
                            }

                            @Override
                            public void onFailure(Call<FarmerErrorResponse> call, Throwable e) {
                                upateProgress(FarmerModelDB.class, true, e.getMessage());
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
                                if (response.code() == 200) {
                                    upateProgress(RecruitFarmerDB.class, false,"");
                                    recruit.delete();
                                }else{
                                    try {
                                        upateProgress(RecruitFarmerDB.class, true, response.errorBody() != null ?
                                                new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                                : response.message());

                                    } catch (Exception e) {
                                        upateProgress(RecruitFarmerDB.class, true, e.getMessage());
                                    }

                                }

                            }

                            @Override
                            public void onFailure(Call<AllResponse> call, Throwable t) {
                                upateProgress(RecruitFarmerDB.class, true, t.getMessage());
                            }
                        });
                    }

                    //testing this since, its thought to have the issue of duplicating records, on the farmer side
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
                                if (response.code() == 200) {
                                    upateProgress(ContractSignDB.class, false,"");
                                    sign.delete();
                                }else{
                                    try {
                                        upateProgress(ContractSignDB.class, true, response.errorBody() != null ?
                                                new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                                : response.message());

                                    } catch (Exception e) {
                                        upateProgress(ContractSignDB.class, true, e.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<AllResponse> call, Throwable t) {
                                upateProgress(ContractSignDB.class, true, t.getMessage());
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
                        hashMap.put("confirmedUnits", plant.confirmedUnits);


                        Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                        APIService service = retrofit.create(APIService.class);
                        SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                        String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                        Call<AllResponse> call = service.postplantverify(auth_key, hashMap);
                        call.enqueue(new Callback<AllResponse>() {
                            @Override
                            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                                if (response.code() == 200) {
                                    upateProgress(PlantingVerifyDB.class, false,"");
                                    plant.delete();
                                }else{
                                    try {
                                        upateProgress(PlantingVerifyDB.class, true, response.errorBody() != null ?
                                                new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                                : response.message());

                                    } catch (Exception e) {
                                        upateProgress(PlantingVerifyDB.class, true, e.getMessage());
                                    }

                                }

                            }

                            @Override
                            public void onFailure(Call<AllResponse> call, Throwable t) {
                                upateProgress(PlantingVerifyDB.class, true, t.getMessage());
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
                                if (response.code() == 200) {
                                    upateProgress(CropDestructionPostDB.class, false,"");
                                    destroy.delete();
                                }else{
                                    try {
                                        upateProgress(CropDestructionPostDB.class, true, response.errorBody() != null ?
                                                new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                                : response.message());

                                    } catch (Exception e) {
                                        upateProgress(CropDestructionPostDB.class, true, e.getMessage());
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<AllResponse> call, Throwable t) {
                                upateProgress(CropDestructionPostDB.class, true, t.getMessage());
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
                                if (response.code() == 200) {
                                    upateProgress(HarvestingDB.class, false,"");
                                    harvest.delete();
                                }else{
                                    try {

                                        String error = response.errorBody() != null ?
                                                new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                                : response.message();
                                        EditContentModel model = new EditContentModel(harvest.farmerName + ", harvested " + harvest.harvestkilos + " kgs",
                                                harvest,
                                                error,
                                                harvest.getId(),
                                                false);
                                        harvestCollection.add(model);
                                        upateProgress(HarvestingDB.class, true, response.errorBody() != null ?
                                                new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                                : response.message());

                                    } catch (Exception e) {
                                        upateProgress(HarvestingDB.class, true, e.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<AllResponse> call, Throwable t) {
                                upateProgress(HarvestingDB.class, true, t.getMessage());
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
                                if (response.code() == 200) {
                                    upateProgress(SprayPostDB.class, false,"");
                                    spray.delete();
                                }else{
                                    try {
                                        upateProgress(SprayPostDB.class, true, response.errorBody() != null ?
                                                new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                                : response.message());

                                    } catch (Exception e) {
                                        upateProgress(SprayPostDB.class, true, e.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<AllResponse> call, Throwable t) {
                                upateProgress(SprayPostDB.class, true, t.getMessage());
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

                                    if (response.code() == 200) {
                                        upateProgress(ApplyFertilizerTB.class, false,"");
                                        apply.delete();
                                    }else{
                                        try {
                                            upateProgress(ApplyFertilizerTB.class, true, response.errorBody() != null ?
                                                    new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                                    : response.message());

                                        } catch (Exception e) {
                                            upateProgress(ApplyFertilizerTB.class, true, e.getMessage());
                                        }
                                    }


                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                upateProgress(ApplyFertilizerTB.class, true, t.getMessage());
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
                                if (response.code() == 200) {
                                    upateProgress(HarvestBlockTB.class, false,"");
                                    harvest.delete();
                                }else{
                                    try {
                                        upateProgress(HarvestBlockTB.class, true, response.errorBody() != null ?
                                                new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                                : response.message());

                                    } catch (Exception e) {
                                        upateProgress(HarvestBlockTB.class, true, e.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                upateProgress(HarvestBlockTB.class, true, t.getMessage());
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
                                if (response.code() == 200) {
                                    upateProgress(IrrigateBlockTB.class, false,"");
                                    irrigate.delete();
                                }else{
                                    try {
                                        upateProgress(IrrigateBlockTB.class, true, response.errorBody() != null ?
                                                new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                                : response.message());

                                    } catch (Exception e) {
                                        upateProgress(IrrigateBlockTB.class, true, e.getMessage());
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                upateProgress(IrrigateBlockTB.class, true, t.getMessage());
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
                                if (response.code() == 200) {
                                    upateProgress(PlantBlockTB.class, false,"");
                                    plant.delete();
                                }else{
                                    try {
                                        upateProgress(PlantBlockTB.class, true, response.errorBody() != null ?
                                                new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                                : response.message());

                                    } catch (Exception e) {
                                        upateProgress(PlantBlockTB.class, true, e.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                upateProgress(PlantBlockTB.class, true, t.getMessage());
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
                                if (response.code() == 200) {
                                    upateProgress(RegisterBlockTB.class, false,"");
                                    register.delete();
                                }else{
                                    try {
                                        upateProgress(RegisterBlockTB.class, true, response.errorBody() != null ?
                                                new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                                : response.message());

                                    } catch (Exception e) {
                                        upateProgress(RegisterBlockTB.class, true, e.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                upateProgress(RegisterBlockTB.class, true, t.getMessage());
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
                                if (response.code() == 200) {
                                    upateProgress(ScoutingTB.class, false,"");
                                    scouting.delete();
                                }else{
                                    try {
                                        upateProgress(ScoutingTB.class, true, response.errorBody() != null ?
                                                new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                                : response.message());

                                    } catch (Exception e) {
                                        upateProgress(ScoutingTB.class, true, e.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                upateProgress(ScoutingTB.class, true, t.getMessage());
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
                                if (response.code() == 200) {
                                    upateProgress(ChangeCentreTB.class, false,"");
                                    centre.delete();
                                }else{
                                    try {
                                        upateProgress(ChangeCentreTB.class, true, response.errorBody() != null ?
                                                new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                                : response.message());

                                    } catch (Exception e) {
                                        upateProgress(ChangeCentreTB.class, true, e.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<AllResponse> call, Throwable t) {
                                upateProgress(ChangeCentreTB.class, true, t.getMessage());
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
                                if (response.code() == 200) {
                                    upateProgress(EditFarmerDetailsTB.class, false,"");
                                    details.delete();
                                }else{
                                    try {
                                        upateProgress(EditFarmerDetailsTB.class, true, response.errorBody() != null ?
                                                new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                                : response.message());

                                    } catch (Exception e) {
                                        upateProgress(EditFarmerDetailsTB.class, true, e.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<AllResponse> call, Throwable t) {
                                upateProgress(EditFarmerDetailsTB.class, true, t.getMessage());
                            }
                        });
                    }

                }

    }


    private void populateRecycler(List<OfflineDataItem> offlineDataItemList, boolean update){


        if(update){
            // specify an adapter (see also next example)
            mAdapter = new OfflineDataRecyclerAdapter(offlineDataItemList, OfflineDataSyncActivity.this,
                                         progressTitle,progress_indicator,complete_status,error_message,
                                        progress,btn_checkData
                    );
            offlineDataSyncRc.setAdapter( mAdapter);
            mAdapter.notifyDataSetChanged();
        }else{
            // specify an adapter (see also next example)
            mAdapter = new OfflineDataRecyclerAdapter(offlineDataItemList, OfflineDataSyncActivity.this,
                    progressTitle,progress_indicator,complete_status,error_message,
                    progress,btn_checkData);
            offlineDataSyncRc.setAdapter(mAdapter);
        }
    }


}




