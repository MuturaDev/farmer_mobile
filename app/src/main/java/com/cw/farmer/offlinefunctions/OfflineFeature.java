package com.cw.farmer.offlinefunctions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.cw.farmer.activity.CropWalkActivity;
import com.cw.farmer.activity.SearchCropWalkActivity;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.internetConnectionBroadcast.NotificationNotify;
import com.cw.farmer.model.BankNameDB;
import com.cw.farmer.model.BankNameResponse;
import com.cw.farmer.model.ContractSignDB;
import com.cw.farmer.model.CropDateDB;
import com.cw.farmer.model.CropDateResponse;
import com.cw.farmer.model.CropDestructionDB;
import com.cw.farmer.model.CropDestructionPostDB;
import com.cw.farmer.model.CropWalkPageItemResponse;
import com.cw.farmer.model.DestructionReasonResponse;
import com.cw.farmer.model.FarmerHarvestResponse;
import com.cw.farmer.model.FarmerModelDB;
import com.cw.farmer.model.GeneralSpinnerResponse;
import com.cw.farmer.model.HarvestingDB;
import com.cw.farmer.model.PageItem;
import com.cw.farmer.model.PageItemHarvest;
import com.cw.farmer.model.PageItemPlantBlock;
import com.cw.farmer.model.PageItemSearchArea;
import com.cw.farmer.model.PageItemsDestruction;
import com.cw.farmer.model.PageItemsPlantVerify;
import com.cw.farmer.model.PageItemsSprayFarmer;
import com.cw.farmer.model.PageItemsSprayNumber;
import com.cw.farmer.model.PageItemstask;
import com.cw.farmer.model.PlantBlockResponse;
import com.cw.farmer.model.PlantVerifyResponse;
import com.cw.farmer.model.PlantingVerificationResponse;
import com.cw.farmer.model.PlantingVerifyDB;
import com.cw.farmer.model.RecruitFarmerDB;
import com.cw.farmer.model.RegisterResponse;
import com.cw.farmer.model.SearchAreaResponse;
import com.cw.farmer.model.SearchContractPageItem;
import com.cw.farmer.model.SearchContractResponse;
import com.cw.farmer.model.SearchCropWalkResponse;
import com.cw.farmer.model.SearchDestructionResponse;
import com.cw.farmer.model.SprayFarmerResponse;
import com.cw.farmer.model.SprayNumberDB;
import com.cw.farmer.model.SprayNumbersResponse;
import com.cw.farmer.model.SprayPostDB;
import com.cw.farmer.model.TasksResponse;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.cw.farmer.spinner_models.CropStageResponse;
import com.cw.farmer.spinner_models.GrowthParameterResponse;
import com.cw.farmer.table_models.ApplyFertilizerTB;
import com.cw.farmer.table_models.ChangeCentreTB;
import com.cw.farmer.table_models.EditFarmerDetailsTB;
import com.cw.farmer.table_models.HarvestBlockTB;
import com.cw.farmer.table_models.IrrigateBlockTB;
import com.cw.farmer.table_models.PlantBlockTB;
import com.cw.farmer.table_models.RegisterBlockTB;
import com.cw.farmer.table_models.ScoutingTB;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

 public class OfflineFeature {

    private int offline;
    //flag for showing popups
    private boolean showPopup;


    public OfflineFeature(int offline, boolean showPopup) {
        this.offline = offline;
        this.showPopup = showPopup;
    }

    public static boolean hasAnyData(){
        boolean hasData = false;
        //New features
        List<ApplyFertilizerTB> applyList = ApplyFertilizerTB.listAll(ApplyFertilizerTB.class);
        if(applyList.size() > 0) hasData = true;

        List<HarvestBlockTB> harvestList = HarvestBlockTB.listAll(HarvestBlockTB.class);
        if(harvestList.size() > 0) hasData = true;

        List<IrrigateBlockTB> irrigateList = IrrigateBlockTB.listAll(IrrigateBlockTB.class);
        if(irrigateList.size() > 0) hasData = true;

        List<PlantBlockTB> plantBlockList = PlantBlockTB.listAll(PlantBlockTB.class);
        if(plantBlockList.size() > 0) hasData = true;

        List<RegisterBlockTB> registerBlockList = RegisterBlockTB.listAll(RegisterBlockTB.class);
        if(registerBlockList.size() > 0) hasData = true;

        List<ScoutingTB> scoutingList = ScoutingTB.listAll(ScoutingTB.class);
        if(scoutingList.size() > 0) hasData = true;

        //Old Feature
        List<FarmerModelDB> farmerList = FarmerModelDB.listAll(FarmerModelDB.class);
        if(farmerList.size() > 0) hasData = true;

        List<RecruitFarmerDB> recruitFarmerList = RecruitFarmerDB.listAll(RecruitFarmerDB.class);
        if(recruitFarmerList.size() > 0) hasData = true;

        List<ContractSignDB> contractSignList = ContractSignDB.listAll(ContractSignDB.class);
        if(contractSignList.size() > 0) hasData = true;

        List<PlantingVerifyDB> plantVerifyList = PlantingVerifyDB.listAll(PlantingVerifyDB.class);
        if(plantVerifyList.size() > 0) hasData = true;

        List<CropDestructionPostDB> destroyList = CropDestructionPostDB.listAll(CropDestructionPostDB.class);
        if(destroyList.size() > 0) hasData = true;

        List<HarvestingDB> harvestingList = HarvestingDB.listAll(HarvestingDB.class);
        if(harvestingList.size() > 0) hasData = true;

        List<SprayPostDB> sprayList = SprayPostDB.listAll(SprayPostDB.class);
        if(sprayList.size() > 0) hasData = true;

        List<ChangeCentreTB> changeCentre = ChangeCentreTB.listAll(ChangeCentreTB.class);
        if(changeCentre.size() > 0) hasData = true;

        List<EditFarmerDetailsTB> editFarmer = EditFarmerDetailsTB.listAll(EditFarmerDetailsTB.class);
        if(editFarmer.size() > 0) hasData = true;

        
        return hasData;
    }

    public static int dataCountProgress(){

        int hasData = 0;
        //New features
        List<ApplyFertilizerTB> applyList = ApplyFertilizerTB.listAll(ApplyFertilizerTB.class);
        if(applyList.size() > 0) hasData++;

        List<HarvestBlockTB> harvestList = HarvestBlockTB.listAll(HarvestBlockTB.class);
        if(harvestList.size() > 0) hasData++;

        List<IrrigateBlockTB> irrigateList = IrrigateBlockTB.listAll(IrrigateBlockTB.class);
        if(irrigateList.size() > 0) hasData++;

        List<PlantBlockTB> plantBlockList = PlantBlockTB.listAll(PlantBlockTB.class);
        if(plantBlockList.size() > 0) hasData++;

        List<RegisterBlockTB> registerBlockList = RegisterBlockTB.listAll(RegisterBlockTB.class);
        if(registerBlockList.size() > 0) hasData++;

        List<ScoutingTB> scoutingList = ScoutingTB.listAll(ScoutingTB.class);
        if(scoutingList.size() > 0) hasData++;

        //Old Feature
        List<FarmerModelDB> farmerList = FarmerModelDB.listAll(FarmerModelDB.class);
        if(farmerList.size() > 0) hasData++;

        List<RecruitFarmerDB> recruitFarmerList = RecruitFarmerDB.listAll(RecruitFarmerDB.class);
        if(recruitFarmerList.size() > 0) hasData++;

        List<ContractSignDB> contractSignList = ContractSignDB.listAll(ContractSignDB.class);
        if(contractSignList.size() > 0) hasData++;

        List<PlantingVerifyDB> plantVerifyList = PlantingVerifyDB.listAll(PlantingVerifyDB.class);
        if(plantVerifyList.size() > 0) hasData++;

        List<CropDestructionPostDB> destroyList = CropDestructionPostDB.listAll(CropDestructionPostDB.class);
        if(destroyList.size() > 0) hasData++;

        List<HarvestingDB> harvestingList = HarvestingDB.listAll(HarvestingDB.class);
        if(harvestingList.size() > 0) hasData++;

        List<SprayPostDB> sprayList = SprayPostDB.listAll(SprayPostDB.class);
        if(sprayList.size() > 0) hasData++;

        List<ChangeCentreTB> changeCentre = ChangeCentreTB.listAll(ChangeCentreTB.class);
        if(changeCentre.size() > 0) hasData++;

        List<EditFarmerDetailsTB> editFarmer = EditFarmerDetailsTB.listAll(EditFarmerDetailsTB.class);
        if(editFarmer.size() > 0) hasData++;


        return hasData;


    }




    private void reportDataFetched(String title, String message, Context context){
        if(!showPopup) {
           // new NotificationNotify(context).displayNotification(title, message, "");

        }
    }
    
    
    public void silentDataDump(Context context){

        //FETCH
        @SuppressLint("StaticFieldLeak") AsyncTask asyncTask1 = new AsyncTask() {
            @Override
            protected void onPreExecute() {
                Log.d(context.getPackageName().toUpperCase(), "Start of SilentDataDump");
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] objects) {

                int limit = 0;
                int offset = 0;
                //ListFarmerActivity/scheme/
                Retrofit retrofit = ApiClient.getClient("/authentication/", context);
                APIService service = retrofit.create(APIService.class);
                SharedPreferences prefs = context.getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                String auth_key = prefs.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");

                String search = "";
                if(offline == 2 || offline == 0){
                    Call<RegisterResponse> call = service.getRegister(/*10, 0*/limit,offset, search, auth_key);
                    call.enqueue(new Callback<RegisterResponse>() {
                        @Override
                        public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                            try {
                                if(response.body().getPageItems() != null)
                                if (String.valueOf(response.body().getPageItems().size()) != "0") {



                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body().getPageItems());
                                    reportDataFetched("ListFarmerActivity/scheme/", json,context);
                                    editor.putString("viewfarmer", json);
                                    editor.apply();     // This
                                }// line is IMPORTANT !!!
                            } catch (Exception e) {
                                ////Utility.showToast(getContext(), e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(Call<RegisterResponse> call, Throwable t) {

                        }
                    });
                }


                //Register Activity
                //TODO: THIS SHOULD BE REMOVED
//                if(offline == 3 || offline == 0) {
//                    Call<RegisterResponse> call16 = service.getRegister(/*10, 0*/limit, offset, null, auth_key);
//                    call16.enqueue(new Callback<RegisterResponse>() {
//                        @Override
//                        public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
//
//                            if(response.body().getPageItems() != null)
//                            if (response.body().getPageItems().size() != 0) {
//
////                                pageItemArrayList = (ArrayList<PageItem>) response.body().getPageItems();
////                                setData();
//
//                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//                                SharedPreferences.Editor editor = prefs.edit();
//                                Gson gson = new Gson();
//                                String json = gson.toJson(response.body().getPageItems());
//                                editor.putString("RegisterActivityOffline", json);
//                                editor.apply();
//
//
//                                //HERE YOU FETCH FOR THE ACTIONS, using the pageItem.getID
//
//                                for (PageItem item : response.body().getPageItems()) {
//
//                                    //ACTIONS:
//                                    //VIEW ID
//                                    Call<List<FarmerDocResponse>> callViewID = service.getfamerdocs(item.getId(), auth_key);
//                                    callViewID.enqueue(new Callback<List<FarmerDocResponse>>() {
//                                        @Override
//                                        public void onResponse(Call<List<FarmerDocResponse>> call, Response<List<FarmerDocResponse>> response) {
//                                            if (response.body() != null) {
//
//                                                if (response.body() != null)
//                                                    if (response.body().size() > 0) {
//                                                        //first delete the cached data
//                                                        FarmerDocument.deleteAll(FarmerDocument.class);
//
//                                                        FarmerDocResponse doc = response.body().get(0);
//                                                        FarmerDocument document = new FarmerDocument(
//                                                                doc.getDocshtdesc(),
//                                                                doc.getDocno(),
//                                                                doc.getDocno(),
//                                                                String.valueOf(doc.getId()),
//                                                                doc.getBase64ImageDesc().replace("data:image/png;base64,", "")
//                                                        );
//
//                                                        document.save();
//
//                                                    }
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call<List<FarmerDocResponse>> call, Throwable t) {
//
//                                        }
//                                    });
//
//                                    //view Bank Card
//                                    Call<List<FarmerAccountsResponse>> callBankCard = service.getfameraccount(item.getId(), auth_key);
//                                    callBankCard.enqueue(new Callback<List<FarmerAccountsResponse>>() {
//                                        @Override
//                                        public void onResponse(Call<List<FarmerAccountsResponse>> call, Response<List<FarmerAccountsResponse>> response) {
//
//
//                                            if (response.body() != null)
//                                                if (response.body().size() > 0) {
//
//                                                    FarmerAccountTB.deleteAll(FarmerAccountTB.class);
//
//                                                    FarmerAccountsResponse doc = response.body().get(0);
//
//                                                    FarmerAccountTB account = new FarmerAccountTB(
//                                                            doc.getBankName(),
//                                                            doc.getAccountno(),
//                                                            doc.getAccountno(),
//                                                            String.valueOf(doc.getId()),
//                                                            doc.getBase64ImageData().replace("data:image/png;base64,", "")
//
//                                                    );
//
//                                                    account.save();
//
//
//                                                }
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call<List<FarmerAccountsResponse>> call, Throwable t) {
//
//                                        }
//                                    });
//
//
//                                    //TODO: BLACK LIST NOT DONE
//                                    //Blacklist Farmer
//
//
//                                    //Request Changes
//                                    Call<List<AllCentreResponse>> callRequestChanges = service.getcentre(auth_key);
//                                    callRequestChanges.enqueue(new Callback<List<AllCentreResponse>>() {
//                                        @Override
//                                        public void onResponse(Call<List<AllCentreResponse>> call, Response<List<AllCentreResponse>> response) {
//                                            if (response.body() != null)
//                                                if (response.body().size() > 0) {
//                                                    AllCentreTB.deleteAll(AllCentreTB.class);
//
//                                                    for (AllCentreResponse centreResponse : response.body()) {
//                                                        AllCentreTB centre = new AllCentreTB(centreResponse.getId(), centreResponse.getName(), centreResponse.getCentretype());
//                                                        centre.save();
//                                                    }
//
//                                                }
//
//
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call<List<AllCentreResponse>> call, Throwable t) {
//
//                                        }
//                                    });
//
//
//                                    //Change Farmer Centre
//
//
//                                }
//
//
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<RegisterResponse> call, Throwable t) {
//
//                        }
//                    });
//                }



                //ListFarmerActivity/scheme/Clicked Item/View ID
                //get id, which is the pageItem.getId() from previous activity, FarmerDetailsActivity
                //id specified in Intent for opendoc method
//               Call<List<FarmerDocResponse>> call2 = service.getfamerdocs(id, auth_key);
//               call2.enqueue(new Callback<List<FarmerDocResponse>>() {
//                   @Override
//                   public void onResponse(Call<List<FarmerDocResponse>> call, Response<List<FarmerDocResponse>> response) {
//                       if(response.body() != null) {
//                           for (FarmerDocResponse doc : response.body()) {
//
//
//                               Log.w("myApp", response.message());
//                               pDialog.dismissWithAnimation();
//
//
//
//
//                           }
//                       }
//                   }
//
//                   @Override
//                   public void onFailure(Call<List<FarmerDocResponse>> call, Throwable t) {
//
//
//                   }
//               });


                //Recruit Farmer/select farmer/SearchFarmerActivity
                if(offline == 4 || offline == 0) {
                    Call<RegisterResponse> call3 = service.getRegister(0, 0, "", auth_key);
                    call3.enqueue(new Callback<RegisterResponse>() {
                        @Override
                        public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                            try {
                                if(response.body().getPageItems() != null)
                                if (String.valueOf(response.body().getPageItems().size()) != "0") {
                                    // saveArrayList(pageItemArrayList, "viewrecruitfarmer");
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body().getPageItems());
                                    reportDataFetched("Recruit Farmer/select farmer/SearchFarmerActivity", json,context);
                                    editor.putString("viewrecruitfarmer", json);
                                    editor.apply();     // Th
                                } else {
                                }

                            } catch (Exception e) {

                            }

                        }

                        @Override
                        public void onFailure(Call<RegisterResponse> call, Throwable t) {

                        }
                    });
                }


                //Contract Sign/ select farmer/SearchContractFarmerActivity
                if(offline == 5 || offline == 0) {
                    Call<SearchContractResponse> call4 = service.getContractfarmer(0, 0, "", auth_key);
                    call4.enqueue(new Callback<SearchContractResponse>() {
                        @Override
                        public void onResponse(Call<SearchContractResponse> call, Response<SearchContractResponse> response) {
                            try {
                                if(response.body().getPageItems() != null)
                                if (response.body().getPageItems().size() != 0) {
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body().getPageItems());
                                    reportDataFetched("Contract Sign/ select farmer/SearchContractFarmerActivity", json,context);
                                    editor.putString("contractfarmer", json);
                                    editor.apply();

                                }
                            } catch (Exception e) {
                            }
                        }

                        @Override
                        public void onFailure(Call<SearchContractResponse> call, Throwable t) {

                        }
                    });
                }

                //Verify planting/select farmer/SearchPlantingVerificationActivity
                if(offline == 6 || offline == 0) {
                    Call<PlantVerifyResponse> call5 = service.getplantingfarmer(0, 0, "", auth_key);
                    call5.enqueue(new Callback<PlantVerifyResponse>() {
                        @Override
                        public void onResponse(Call<PlantVerifyResponse> call, Response<PlantVerifyResponse> response) {

                            try {
                                System.out.println(response.body().getPageItemsPlantVerify());

                                if (response.body().getPageItemsPlantVerify().size() != 0) {
//                               pageItemArrayList = (ArrayList<PageItemsPlantVerify>) response.body().getPageItemsPlantVerify();
//                               saveArrayList(pageItemArrayList, "verifyplantingfarmer");

                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body().getPageItemsPlantVerify());
                                    reportDataFetched("Verify planting/select farmer/SearchPlantingVerificationActivity", json,context);
                                    editor.putString("verifyplantingfarmer", json);
                                    editor.apply();     // This line is IMPORTANT !!!

                                }

                            } catch (Exception e) {

                            }

                        }

                        @Override
                        public void onFailure(Call<PlantVerifyResponse> call, Throwable t) {

                        }
                    });
                }

                //Crop Desctuction/select farmer/SearchDestructionFarmerActivity
                if(offline == 7 || offline == 0) {
                    Call<SearchDestructionResponse> call6 = service.getDestructionfarmer(0, 0, "", auth_key);
                    call6.enqueue(new Callback<SearchDestructionResponse>() {
                        @Override
                        public void onResponse(Call<SearchDestructionResponse> call, Response<SearchDestructionResponse> response) {

                            try {

                                if (response.body().getPageItemsDestruction().size() != 0) {
//                               pageItemArrayList = (ArrayList<PageItemsDestruction>) response.body().getPageItemsDestruction();
//                               saveArrayList(pageItemArrayList, "destructionfarmer");
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body().getPageItemsDestruction());
                                    reportDataFetched("Crop Desctuction/select farmer/SearchDestructionFarmerActivity", json,context);
                                    editor.putString("destructionfarmer", json);
                                    editor.apply();
                                }

                            } catch (Exception e) {

                            }

                        }

                        @Override
                        public void onFailure(Call<SearchDestructionResponse> call, Throwable t) {

                        }
                    });
                }

                //Spray Confirmation/select farmer/SearchSprayActivity
                if(offline == 8 || offline == 0) {
                    Call<SprayFarmerResponse> call7 = service.getSprayfarmer(0, 0, "", auth_key);
                    call7.enqueue(new Callback<SprayFarmerResponse>() {
                        @Override
                        public void onResponse(Call<SprayFarmerResponse> call, Response<SprayFarmerResponse> response) {

                            try {
                                if (response.body().getPageItemsSprayFarmer().size() != 0) {
//                               pageItemArrayList = (ArrayList<PageItemsSprayFarmer>) response.body().getPageItemsSprayFarmer();
//                               saveArrayList(pageItemArrayList, "sprayfarmer");
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body().getPageItemsSprayFarmer());
                                    reportDataFetched("Spray Confirmation/select farmer/SearchSprayActivity", json,context);
                                    editor.putString("sprayfarmer", json);
                                    editor.apply();     // This line is IMPORTANT !!!
                                }

                            } catch (Exception e) {

                            }

                        }

                        @Override
                        public void onFailure(Call<SprayFarmerResponse> call, Throwable t) {

                        }
                    });
                }

                //Harvest Collection/select farmer/SearchHarvestFarmerActivity
                if(offline == 9 || offline == 0) {
                    Call<FarmerHarvestResponse> call8 = service.getHarvestfarmer(0, 0, "", auth_key);
                    call8.enqueue(new Callback<FarmerHarvestResponse>() {
                        @Override
                        public void onResponse(Call<FarmerHarvestResponse> call, Response<FarmerHarvestResponse> response) {

                            try {


                                if (response.body().getPageItemHarvest().size() != 0) {
//                               pageItemArrayList = (ArrayList<PageItemHarvest>) response.body().getPageItemHarvest();
//                               saveArrayList(pageItemArrayList, "harvestfarmer");

                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body().getPageItemHarvest());

                                    reportDataFetched("Harvest Collection/select farmer/SearchHarvestFarmerActivity", json,context);
                                    editor.putString("harvestfarmer", json);
                                    editor.apply();     // This line is IMPORTANT !!!
                                }
                            } catch (Exception e) {
                            }
                        }

                        @Override
                        public void onFailure(Call<FarmerHarvestResponse> call, Throwable t) {

                        }
                    });
                }

                //Register Block/select farm, spinner
                if(offline == 10 || offline == 0) {
                    Call<List<GeneralSpinnerResponse>> call9 = service.getFarmNames(auth_key);
                    call9.enqueue(new Callback<List<GeneralSpinnerResponse>>() {
                        @Override
                        public void onResponse(Call<List<GeneralSpinnerResponse>> call, Response<List<GeneralSpinnerResponse>> response) {

                            if (response.body() != null) {
                                if (response.body() != null)
                                    if (response.body().size() > 0) {
                                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        Gson gson = new Gson();
                                        String json = gson.toJson(response.body());
                                        reportDataFetched("Register Block/select farm, spinner", json,context);
                                        editor.putString("RegisterBlockSelectFarm", json);
                                        editor.apply();
                                    }
                            }

                        }

                        @Override
                        public void onFailure(Call<List<GeneralSpinnerResponse>> call, Throwable t) {

                        }
                    });
                }

                //Plant Block/select block/SearchSearchAreaActivity
                if(offline == 11 || offline == 0) {
                    Call<SearchAreaResponse> call10 = service.getSearchArea(/*15, 0*/limit, offset, auth_key);
                    call10.enqueue(new Callback<SearchAreaResponse>() {
                        @Override
                        public void onResponse(Call<SearchAreaResponse> call, Response<SearchAreaResponse> response) {

                            try {
                                if (response.body().getPageItemSearchAreaList().size() > 0) {
                                    //pageItemArrayList = (ArrayList<PageItemSearchArea>) response.body().getPageItemSearchAreaList();
                                    //saveArrayList(pageItemArrayList, "harvestblocksearch");
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body().getPageItemSearchAreaList());
                                    reportDataFetched("Plant Block/select block/SearchSearchAreaActivity", json,context);
                                    editor.putString("SearchSearchAreaActivity", json);
                                    editor.apply();     // This line is IMPORTANT !!!
                                } else {

                                }
                            } catch (Exception e) {
                            }

                        }

                        @Override
                        public void onFailure(Call<SearchAreaResponse> call, Throwable t) {

                        }
                    });
                }

                //Plant Block/spinner/PlantBlockActivity
                if(offline == 12 || offline == 0) {
                    Call<List<GeneralSpinnerResponse>> call101 = service.getVariety(auth_key);
                    call101.enqueue(new Callback<List<GeneralSpinnerResponse>>() {
                        @Override
                        public void onResponse(Call<List<GeneralSpinnerResponse>> call, Response<List<GeneralSpinnerResponse>> response) {

                            if (response.body() != null) {
                                if (response.body() != null)
                                    if (response.body().size() > 0) {
                                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        Gson gson = new Gson();
                                        String json = gson.toJson(response.body());
                                        reportDataFetched("Plant Block/spinner/PlantBlockActivity", json,context);
                                        editor.putString("PlantBlockSpinner", json);
                                        editor.apply();
                                    }
                            }

                        }

                        @Override
                        public void onFailure(Call<List<GeneralSpinnerResponse>> call, Throwable t) {

                        }
                    });
                }

                //Irrigate Block/select block/SearchPlantBlockActivity
                if(offline == 13 || offline == 0) {
                    Call<PlantBlockResponse> call11 = service.getPlantBlockNames(/*15, 0*/limit, offset, auth_key);
                    call11.enqueue(new Callback<PlantBlockResponse>() {
                        @Override
                        public void onResponse(Call<PlantBlockResponse> call, Response<PlantBlockResponse> response) {

                            try {

                                if (response.body().getPageItemPlantBlocksList().size() > 0) {

                                    // pageItemArrayList = (ArrayList<PageItemPlantBlock>) response.body().getPageItemPlantBlocksList();
                                    // saveArrayList(pageItemArrayList, "plantblockresponse");
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body().getPageItemPlantBlocksList());
                                    reportDataFetched("Irrigate Block/select block/SearchPlantBlockActivity", json,context);
                                    editor.putString("searchPlantBlockActivity", json);
                                    editor.apply();     // This line is IMPORTANT !!!
                                }
                            } catch (Exception e) {
                            }
                        }

                        @Override
                        public void onFailure(Call<PlantBlockResponse> call, Throwable t) {

                        }
                    });
                }


                //Apply Fertilizer's/select block/SearchPlantBlockActivity
                if(offline == 14 || offline == 0) {
                    Call<PlantBlockResponse> call12 = service.getPlantBlockNames(/*15, 0*/limit, offset, auth_key);
                    call12.enqueue(new Callback<PlantBlockResponse>() {
                        @Override
                        public void onResponse(Call<PlantBlockResponse> call, Response<PlantBlockResponse> response) {

                            try {

                                if (response.body().getPageItemPlantBlocksList().size() > 0) {

//                               pageItemArrayList = (ArrayList<PageItemPlantBlock>) response.body().getPageItemPlantBlocksList();
//                               saveArrayList(pageItemArrayList, "searchPlantBlockActivity");

                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body().getPageItemPlantBlocksList());
                                    reportDataFetched("Apply Fertilizer's/select block/SearchPlantBlockActivity", json,context);
                                    editor.putString("searchPlantBlockActivity", json);
                                    editor.apply();

                                }

                            } catch (Exception e) {

                            }

                        }

                        @Override
                        public void onFailure(Call<PlantBlockResponse> call, Throwable t) {

                        }
                    });
                }

                //Apply Fertilizer's/spinner/ApplyFertilizerBlockActivity
                if(offline == 15 || offline == 0) {
                    Call<List<GeneralSpinnerResponse>> call121 = service.getFertilizer(auth_key);
                    call121.enqueue(new Callback<List<GeneralSpinnerResponse>>() {
                        @Override
                        public void onResponse(Call<List<GeneralSpinnerResponse>> call, Response<List<GeneralSpinnerResponse>> response) {

                            if (response.body() != null) {
                                if (response.body() != null)
                                    if (response.body().size() > 0) {
                                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        Gson gson = new Gson();
                                        String json = gson.toJson(response.body());
                                        reportDataFetched("Apply Fertilizer's/spinner/ApplyFertilizerBlockActivity", json,context);
                                        editor.putString("ApplyFertilizerSpinner", json);
                                        editor.apply();
                                    }
                            }

                        }

                        @Override
                        public void onFailure(Call<List<GeneralSpinnerResponse>> call, Throwable t) {

                        }
                    });
                }

                //Scouting/select block/SearchPlantBlockActivity
                if(offline == 16 || offline == 0) {
                    Call<PlantBlockResponse> call13 = service.getPlantBlockNames(/*15, 0*/limit, offset, auth_key);
                    call13.enqueue(new Callback<PlantBlockResponse>() {
                        @Override
                        public void onResponse(Call<PlantBlockResponse> call, Response<PlantBlockResponse> response) {

                            try {

                                if (response.body().getPageItemPlantBlocksList().size() > 0) {

//                               pageItemArrayList = (ArrayList<PageItemPlantBlock>) response.body().getPageItemPlantBlocksList();
//                               saveArrayList(pageItemArrayList, "searchPlantBlockActivity");

                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body().getPageItemPlantBlocksList());
                                    reportDataFetched("Scouting/select block/SearchPlantBlockActivity", json,context);
                                    editor.putString("searchPlantBlockActivity", json);
                                    editor.apply();

                                }

                            } catch (Exception e) {

                            }

                        }

                        @Override
                        public void onFailure(Call<PlantBlockResponse> call, Throwable t) {

                        }
                    });
                }

                //Harvest Block/select block/SearchPlantBlockActivity
                if(offline == 17 || offline == 0) {
                    Call<PlantBlockResponse> call14 = service.getPlantBlockNames(/*15, 0*/limit, offset, auth_key);
                    call14.enqueue(new Callback<PlantBlockResponse>() {
                        @Override
                        public void onResponse(Call<PlantBlockResponse> call, Response<PlantBlockResponse> response) {

                            try {

                                if (response.body().getPageItemPlantBlocksList().size() > 0) {

//                               pageItemArrayList = (ArrayList<PageItemPlantBlock>) response.body().getPageItemPlantBlocksList();
//                               saveArrayList(pageItemArrayList, "searchPlantBlockActivity");

                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body().getPageItemPlantBlocksList());
                                    reportDataFetched("Harvest Block/select block/SearchPlantBlockActivity", json,context);
                                    editor.putString("searchPlantBlockActivity", json);
                                    editor.apply();

                                }

                            } catch (Exception e) {

                            }

                        }

                        @Override
                        public void onFailure(Call<PlantBlockResponse> call, Throwable t) {

                        }
                    });
                }

                //HomeActivity
                if(offline == 18 || offline == 0) {
                    SharedPreferences preffss = context.getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                    String user_id = preffss.getString("userid", "-1");
                    Call<TasksResponse> callhomeactivity = service.gettask(auth_key, user_id + "");
                    callhomeactivity.enqueue(new Callback<TasksResponse>() {
                        @Override
                        public void onResponse(Call<TasksResponse> call, Response<TasksResponse> response) {

                            if (response.body() != null)
                                if (response.body().getTotalFilteredRecords() > 0) {

                                    OfflineFeature.saveSharedPreferences(response.body().getPageItemstasks(), "PageItemsTasks", context);

                                    int count = 0;
                                    List<HashMap> hasMapList = new ArrayList<>();

                                    for (PageItemstask taks : response.body().getPageItemstasks()) {

                                        if (taks.getEntityName().equals("VERIFY_PLANTING_MOBILE")) {
                                            String date = "";
                                            for (int elem : taks.getCropDate()) {
                                                date = elem + "/" + date;
                                            }
                                            int entityID = taks.getEntityId();/*3*/

                                            Call<PlantingVerificationResponse> callplantVerification = service.getPlantingVerfication(String.valueOf(entityID), auth_key);

                                            callplantVerification.enqueue(new Callback<PlantingVerificationResponse>() {
                                                @Override
                                                public void onResponse(Call<PlantingVerificationResponse> call, Response<PlantingVerificationResponse> response) {

                                                    if (response.body() != null) {


//                                                    tv_centre.setText(response.body().getCentreName());
//                                                    tv_crop_date.setText(response.body().getCropDate().get(2) + "/"  +response.body().getCropDate().get(1) + "/" + response.body().getCropDate().get(0));
//                                                    tv_farmer_name.setText(response.body().getFarmerName());
//                                                    tv_no_of_units.setText(response.body().getContractUnits());

                                                        HashMap hashMap = new HashMap();
                                                        hashMap.put("entityID", entityID);
                                                        hashMap.put("centre", response.body().getCentreName());
                                                        hashMap.put("cropDate", response.body().getCropDate().get(2) + "/" + response.body().getCropDate().get(1) + "/" + response.body().getCropDate().get(0));
                                                        hashMap.put("farmerName", response.body().getFarmerName());
                                                        hashMap.put("noOfUnits", response.body().getContractUnits());

                                                        reportDataFetched("HomeActivity", hashMap.toString(),context);

                                                        OfflineFeature.saveSharedPreferences(hashMap, "ReturnActivityOffline", context);

                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<PlantingVerificationResponse> call, Throwable t) {

                                                }
                                            });
                                        }

                                        count++;
                                    }

                                }
                        }

                        @Override
                        public void onFailure(Call<TasksResponse> call, Throwable t) {

                        }
                    });
                }

                //FarmerRecruiteActivity/Crop Date
                if(offline == 19 || offline == 0) {
                    Call<List<CropDateResponse>> callCropDate = service.getRecruitCropDates(auth_key);
                    callCropDate.enqueue(new Callback<List<CropDateResponse>>() {
                        @Override
                        public void onResponse(Call<List<CropDateResponse>> call, Response<List<CropDateResponse>> response) {
                            if (response.body() != null) {
                                if (response.body().size() > 0) {
                                    reportDataFetched("FarmerRecruiteActivity/Crop Date", response.body().toString(),context);
                                    CropDateDB.deleteAll(CropDateDB.class);
                                    for (CropDateResponse blacklist : response.body()) {
                                        String date = "";
                                        for (int elem : blacklist.getPlantingDate()) {
                                            date = elem + "-" + date;
                                        }
                                        CropDateDB book = new CropDateDB(blacklist.getId(), blacklist.getProdId(), blacklist.getProdname(), date);
                                        book.save();

                                    }
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<List<CropDateResponse>> call, Throwable t) {

                        }
                    });
                }

                //SprayConfirmationActivity/Spray Number
                if(offline == 20 || offline == 0) {
                    Call<SprayNumbersResponse> callSprayNumber = service.getspraynumber(auth_key);
                    callSprayNumber.enqueue(new Callback<SprayNumbersResponse>() {
                        @Override
                        public void onResponse(Call<SprayNumbersResponse> call, Response<SprayNumbersResponse> response) {

                            try {
                                if (response.body().getPageItemsSprayNumbers().size() != 0) {

                                    SprayNumberDB.deleteAll(SprayNumberDB.class);
                                    for (PageItemsSprayNumber blacklist : response.body().getPageItemsSprayNumbers()) {

                                        SprayNumberDB book = new SprayNumberDB(blacklist.getId(), blacklist.getSprayno());
                                        book.save();
                                    }

                                    reportDataFetched("SprayConfirmationActivity/Spray Number", response.body().toString(),context);

                                } else {

                                }

                            } catch (Exception e) {

                            }

                        }

                        @Override
                        public void onFailure(Call<SprayNumbersResponse> call, Throwable t) {

                        }
                    });
                }

                //CropDesturctionActivity/destruction reason
                if(offline == 21 || offline == 0) {
                    Call<List<DestructionReasonResponse>> callDestructionReason = service.getdestructionreasons(auth_key);
                    callDestructionReason.enqueue(new Callback<List<DestructionReasonResponse>>() {
                        @Override
                        public void onResponse(Call<List<DestructionReasonResponse>> call, Response<List<DestructionReasonResponse>> response) {
                            if (response.body() != null) {
                                if (response.body().size() > 0) {
                                    CropDestructionDB.deleteAll(CropDestructionDB.class);
                                    for (DestructionReasonResponse reason : response.body()) {
                                        CropDestructionDB book = new CropDestructionDB(reason.getId(), reason.getDestructionShtDesc(), reason.getDestructionReason(), reason.getDestructionType());
                                        book.save();
                                    }

                                    reportDataFetched("CropDesturctionActivity/destruction reason", response.body().toString(),context);
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<List<DestructionReasonResponse>> call, Throwable t) {

                        }
                    });
                }

                //ManAcivity, Spinner bank
                if(offline == 22 || offline == 0) {
                    Call<List<BankNameResponse>> callSpinnerBank = service.getbankname(auth_key);
                    callSpinnerBank.enqueue(new Callback<List<BankNameResponse>>() {
                        @Override
                        public void onResponse(Call<List<BankNameResponse>> call, Response<List<BankNameResponse>> response) {


                            if (response.body() != null) {
                                if (response.body().size() > 0) {
                                    BankNameDB.deleteAll(BankNameDB.class);
                                    for (BankNameResponse bank : response.body()) {
                                        BankNameDB book = new BankNameDB(bank.getId(), bank.getBankname(), bank.getAccountformat());
                                        book.save();
                                    }

                                    reportDataFetched("ManAcivity, Spinner bank", response.body().toString(),context);
                                }

                            }

                        }

                        @Override
                        public void onFailure(Call<List<BankNameResponse>> call, Throwable t) {

                        }
                    });
                }

//                if(offline == 23 || offline == 0){
//                    //FIXME: HOW WILL THIS WORK OFFLINE, IF IT NEEDS A VALUE TO BE SELECTED BEFORE
//                    Call<List<GrowthParameterResponse>> call = service.getGrowthParameters(auth_key, Long.valueOf(0));
//                    call.enqueue(new Callback<List<GrowthParameterResponse>>() {
//                        @Override
//                        public void onResponse(Call<List<GrowthParameterResponse>> call, Response<List<GrowthParameterResponse>> response) {
//
//                            if(response.code() == 200 || response.code() == 201)
//                                if (response.body() != null) {
//                                    if (response.body().size() > 0) {
//                                        OfflineFeature.saveSharedPreferences(response.body(), "GrowthParameterSpinner", context);
//                                    }
//                                }
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<List<GrowthParameterResponse>> call, Throwable t) {
//
//                           // Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }

                if(offline == 24 || offline == 0){
                    Call<List<CropStageResponse>> call = service.getCropStages(auth_key);
                    call.enqueue(new Callback<List<CropStageResponse>>() {
                        @Override
                        public void onResponse(Call<List<CropStageResponse>> call, Response<List<CropStageResponse>> response) {

                            if(response.code() == 200 || response.code() == 201)
                                if (response.body() != null) {
                                    if (response.body().size() > 0) {
                                        OfflineFeature.saveSharedPreferences(response.body(), "CropStageSpinner", context);
                                    }
                                }

                        }

                        @Override
                        public void onFailure(Call<List<CropStageResponse>> call, Throwable t) {
                           // Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                if(offline == 25 || offline == 0){
                    Call<SearchCropWalkResponse> call = service.getCropWalkSearch(auth_key,offset,limit, "");
                    call.enqueue(new Callback<SearchCropWalkResponse>() {
                        @Override
                        public void onResponse(Call<SearchCropWalkResponse> call, Response<SearchCropWalkResponse> response) {

                           if(response.code() == 200 || response.code() == 201)
                                if (response.body().getTotalFilteredRecords() > 0) {
                                    OfflineFeature.saveSharedPreferences(response.body().getPageItems(), "CropWalkSearch", context);
                                }
                        }

                        @Override
                        public void onFailure(Call<SearchCropWalkResponse> call, Throwable t) {

                        }
                    });
                }



                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Log.d(context.getPackageName().toUpperCase(), "End of SilentDataDump");
            }
        };
        asyncTask1.execute();
    }



    public static void saveSharedPreferences(Object list, String key, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }


     public static List<CropStageResponse> getSharedPreferencesArray(String key, Context context, Object cClass){
         SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
         Gson gson = new Gson();
         String json = prefs.getString(key, null);

         Type type = null;

         if(cClass.equals(CropStageResponse.class)){
             type = new TypeToken<List<CropStageResponse>>(){
             }.getType();
         }

         return gson.fromJson(json, type);
     }

    public static Object getSharedPreferencesObject(String key, Context context, Object cClass){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);

        Type type = null;

        if(cClass.equals(GeneralSpinnerResponse.class) ) {
            type = new TypeToken<ArrayList<GeneralSpinnerResponse>>() {
                    }.getType();
        }

        if(cClass.equals(PageItem.class) ) {
            type = new TypeToken<ArrayList<PageItem>>() {
            }.getType();
        }

        if(cClass.equals(SearchContractPageItem.class) ) {
            type = new TypeToken<ArrayList<SearchContractPageItem>>() {
            }.getType();
        }

        if(cClass.equals(PageItemsDestruction.class) ) {

            type = new TypeToken<ArrayList<PageItemsDestruction>>() {
            }.getType();
        }

        if(cClass.equals(PageItemHarvest.class) ) {
            type = new TypeToken<ArrayList<PageItemHarvest>>() {
            }.getType();
        }

        boolean check = cClass.equals(PageItemPlantBlock.class);

        if(check) {

            type = new TypeToken<ArrayList<PageItemPlantBlock>>() {
            }.getType();
        }

        if(cClass.equals(PageItemsPlantVerify.class) ) {
            type = new TypeToken<ArrayList<PageItemsPlantVerify>>() {
            }.getType();
        }

        if(cClass.equals(PageItemSearchArea.class) ) {
            type = new TypeToken<ArrayList<PageItemSearchArea>>() {
            }.getType();
        }

        if(cClass.equals(PageItemsSprayFarmer.class) ) {
            type = new TypeToken<ArrayList<PageItemsSprayFarmer>>() {
            }.getType();
        }

        if(cClass.equals(PageItemsDestruction.class) ) {
            type = new TypeToken<ArrayList<PageItemsDestruction>>() {
            }.getType();
        }

        if(cClass.equals(PageItemPlantBlock.class) ) {
            type = new TypeToken<ArrayList<PageItemPlantBlock>>() {
            }.getType();
        }

        if(cClass.equals(PageItemstask.class) ) {
            type = new TypeToken<ArrayList<PageItemstask>>() {
            }.getType();
        }

        if(cClass.equals(HashMap.class)){
            type = new TypeToken<HashMap>(){
            }.getType();
        }

        //use the one above
//        if(cClass.equals(CropStageResponse.class)){
//            type = new TypeToken<CropStageResponse>(){
//            }.getType();
//        }

//        if(cClass.equals(GrowthParameterResponse.class)){
//            type = new TypeToken<GrowthParameterResponse>(){
//            }.getType();
//        }

        if(cClass.equals(CropWalkPageItemResponse.class)){
            type = new TypeToken<ArrayList<CropWalkPageItemResponse>>(){}.getType();
        }





        return gson.fromJson(json, type);
    }





}
