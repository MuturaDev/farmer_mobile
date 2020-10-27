package com.cw.farmer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.farmer.R;
import com.cw.farmer.model.BankNameDB;
import com.cw.farmer.model.BankNameResponse;
import com.cw.farmer.model.ContractSignDB;
import com.cw.farmer.model.CropDateDB;
import com.cw.farmer.model.CropDateResponse;
import com.cw.farmer.model.CropDestructionDB;
import com.cw.farmer.model.CropDestructionPostDB;
import com.cw.farmer.model.DestructionReasonResponse;
import com.cw.farmer.model.FarmerHarvestResponse;
import com.cw.farmer.model.FarmerModelDB;
import com.cw.farmer.model.GeneralSpinnerResponse;
import com.cw.farmer.model.HarvestingDB;
import com.cw.farmer.model.PageItemsSprayNumber;
import com.cw.farmer.model.PageItemstask;
import com.cw.farmer.model.PlantBlockResponse;
import com.cw.farmer.model.PlantVerifyResponse;
import com.cw.farmer.model.PlantingVerificationResponse;
import com.cw.farmer.model.PlantingVerifyDB;
import com.cw.farmer.model.RecruitFarmerDB;
import com.cw.farmer.model.RegisterResponse;
import com.cw.farmer.model.SearchAreaResponse;
import com.cw.farmer.model.SearchContractResponse;
import com.cw.farmer.model.SearchDestructionResponse;
import com.cw.farmer.model.SprayFarmerResponse;
import com.cw.farmer.model.SprayNumberDB;
import com.cw.farmer.model.SprayNumbersResponse;
import com.cw.farmer.model.SprayPostDB;
import com.cw.farmer.model.TasksResponse;
import com.cw.farmer.offlinefunctions.OfflineDataItem;
import com.cw.farmer.offlinefunctions.OfflineDataSyncActivity;
import com.cw.farmer.offlinefunctions.OfflineFeature;
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
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;


public class OfflineDataRecyclerAdapter extends RecyclerView.Adapter<OfflineDataRecyclerAdapter.MyViewHolder> {
    private List<OfflineDataItem> offlineDataItemList;
    private Context context;
    private TextView progressTitle,progress_indicator,complete_status,error_message;
    private ProgressBar progress;
    private Button btn_checkData;

    int maxFetchData = 20;

    // Provide a suitable constructor (depends on the kind of dataset)
    public OfflineDataRecyclerAdapter(List<OfflineDataItem> myDataset, Context context,
                                      TextView progressTitle,TextView progress_indicator,
                                      TextView complete_status, TextView error_message,
                                              ProgressBar progress,
                                              Button btn_checkData) {
        this.progressTitle = progressTitle;
        this.progress_indicator = progress_indicator;
        this.complete_status = complete_status;
        this.error_message = error_message;
        this.progress = progress;
        this.btn_checkData = btn_checkData;

        this.offlineDataItemList = myDataset;
        this.context = context;
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView progressTitle,progress_indicator,complete_status,error_message;
        public ProgressBar progress;
        public Button btn_checkData;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            progress = itemView.findViewById(R.id.progress);
            progressTitle = itemView.findViewById(R.id.progressTitle);
            progress_indicator = itemView.findViewById(R.id.progress_indicator);
            complete_status = itemView.findViewById(R.id.complete_status);
            btn_checkData = itemView.findViewById(R.id.btn_checkData);
            error_message = itemView.findViewById(R.id.error_message);
            btn_checkData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    OfflineDataItem offlineDataItem =  offlineDataItemList.get(getAdapterPosition());

                    if(offlineDataItem.getDataItemObject().equals(FarmerModelDB.class)){


                    }else if(offlineDataItem.getDataItemObject().equals(RecruitFarmerDB.class)){

                    }else if(offlineDataItem.getDataItemObject().equals(ContractSignDB.class)){

                    }else if(offlineDataItem.getDataItemObject().equals(PlantingVerifyDB.class)){

                    }else if(offlineDataItem.getDataItemObject().equals(CropDestructionPostDB.class)){

                    }else if(offlineDataItem.getDataItemObject().equals(HarvestingDB.class)){

                    }else if(offlineDataItem.getDataItemObject().equals(SprayPostDB.class)){

                    }else if(offlineDataItem.getDataItemObject().equals(ApplyFertilizerTB.class)){

                    }else if(offlineDataItem.getDataItemObject().equals(HarvestBlockTB.class)){

                    }else if(offlineDataItem.getDataItemObject().equals(IrrigateBlockTB.class)){

                    }else if(offlineDataItem.getDataItemObject().equals(PlantBlockTB.class)){

                    }else if(offlineDataItem.getDataItemObject().equals(RegisterBlockTB.class)){

                    }else if(offlineDataItem.getDataItemObject().equals(ScoutingTB.class)){

                    }else if(offlineDataItem.getDataItemObject().equals(ChangeCentreTB.class)){

                    }else if(offlineDataItem.getDataItemObject().equals(EditFarmerDetailsTB.class)){

                    }

                }
            });

        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public OfflineDataRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offline_sync_item_activity_layout, parent, false);
        return  new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
       // holder.textView.setText(offlineDataItemList[position]);


        int max = offlineDataItemList.get(position).getDataItemSize();
        int progress = offlineDataItemList.get(position).getDataItemProgress();

        int progressValue = (progress * 100)/max;

        holder.progress.setProgress(progressValue);
        holder.progress_indicator.setText(progressValue + "%");

        holder.progress.setMax(100);

        if(offlineDataItemList.get(position).getDateItemErrorText() != null){
            holder.error_message.setText("Error Message: \n" + offlineDataItemList.get(position).getDateItemErrorText());
            holder.error_message.setVisibility(View.VISIBLE);
            holder.btn_checkData.setVisibility(View.VISIBLE);

            holder.complete_status.setText(offlineDataItemList.get(position).isDataItemCompleteStatus() ? "Successfully completed" : "Unsuccessful");
            holder.complete_status.setVisibility(View.VISIBLE);
        }


        if(progress == max) {
            holder.complete_status.setText(offlineDataItemList.get(position).isDataItemCompleteStatus() ? "Successfully completed" : "Unsuccessful");
            holder.complete_status.setVisibility(View.VISIBLE);
        }


        if((offlineDataItemList.size() -1) == position && offlineDataItemList.get(position).isDataItemCompleteStatus()
               /* &&
        offlineDataItemList.get(position).getDateItemErrorText() != null*/) silentDataDump(context);

        holder.complete_status.setTextColor(context.getResources().getColor(
                offlineDataItemList.get(position).isDataItemCompleteStatus()? R.color.colorPrimary :R.color.red));
        holder.progressTitle.setText(offlineDataItemList.get(position).getDateItemTitle());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return offlineDataItemList.size();
    }

    static int count = 0;

    private void offlineFetch(String dataItemErrorText){


        int progress1 = (((progress.getProgress()) * maxFetchData ) / 100);
         progress1 = progress1 + 1;

        count++;

        int progressValue = (progress1 * 100)/maxFetchData;

//        Log.d(context.getPackageName().toUpperCase(), "Fetch Data Count: " +
//                "\nProgress1 = " + progress1 +
//                "\nGetProgress() = " + progress.getProgress() +
//                "\nCount = " + count
//        );

        boolean isDataItemCompleteStatus = (maxFetchData == progress1) ? true : false;

        progress.setProgress(progressValue);
        progress_indicator.setText(progressValue + "%");

        progress.setMax(100);

        if(dataItemErrorText != null){
            if(!dataItemErrorText.isEmpty()) {
                error_message.setText("Error Message: \n" + dataItemErrorText);
                error_message.setVisibility(View.VISIBLE);
                btn_checkData.setVisibility(View.VISIBLE);
                btn_checkData.setText("Try Again");
                btn_checkData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, OfflineDataSyncActivity.class);
                        context.startActivity(intent);
                        ((OfflineDataSyncActivity) context).finish();
                    }
                });

                complete_status.setText(isDataItemCompleteStatus ? "Successfully completed" : "Unsuccessful");
                complete_status.setVisibility(View.VISIBLE);
            }
        }

        if(progress1 == maxFetchData) {
            complete_status.setText(isDataItemCompleteStatus ? "Successfully completed" : "Unsuccessful");
            complete_status.setVisibility(View.VISIBLE);
        }


        complete_status.setTextColor(context.getResources().getColor(
                isDataItemCompleteStatus? R.color.colorPrimary :R.color.red));
        progressTitle.setText(dataItemErrorText);
    }


    public  void silentDataDump(Context context){

        int offline = 0;

        //FETCH
//        @SuppressLint("StaticFieldLeak") AsyncTask asyncTask1 = new AsyncTask() {
//            @Override
//            protected void onPreExecute() {
//                Log.d(context.getPackageName().toUpperCase(), "Start of SilentDataDump");
//                super.onPreExecute();
//            }
//
//            @Override
//            protected Object doInBackground(Object[] objects) {

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
                                        //reportDataFetched("ListFarmerActivity/scheme/", json,context);

                                        Log.d(context.getPackageName().toUpperCase(),"Checking whats not called: " + "1");

                                        editor.putString("viewfarmer", json);
                                        editor.apply();     // This
                                    }// line is IMPORTANT !!!

                                if(response.code() == 200){
                                   offlineFetch("");
                                }else{
                                    offlineFetch(response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                            : response.message());
                                }
                            } catch (Exception e) {
                                offlineFetch(e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(Call<RegisterResponse> call, Throwable t) {offlineFetch(t.getMessage());
                            offlineFetch(t.getMessage());
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
//                                        public void onFailure(Call<List<FarmerDocResponse>> call, Throwable t) {offlineFetch(t.getMessage());
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
//                                        public void onFailure(Call<List<FarmerAccountsResponse>> call, Throwable t) {offlineFetch(t.getMessage());
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
//                                        public void onFailure(Call<List<AllCentreResponse>> call, Throwable t) {offlineFetch(t.getMessage());
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
//                        public void onFailure(Call<RegisterResponse> call, Throwable t) {offlineFetch(t.getMessage());
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
//                   public void onFailure(Call<List<FarmerDocResponse>> call, Throwable t) {offlineFetch(t.getMessage());
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
                            Log.d(context.getPackageName().toUpperCase(),"Checking whats not called: " + "2");
                            try {
                                if(response.body().getPageItems() != null)
                                    if (String.valueOf(response.body().getPageItems().size()) != "0") {
                                        // saveArrayList(pageItemArrayList, "viewrecruitfarmer");
                                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        Gson gson = new Gson();
                                        String json = gson.toJson(response.body().getPageItems());
                                        //reportDataFetched("Recruit Farmer/select farmer/SearchFarmerActivity", json,context);
                                        editor.putString("viewrecruitfarmer", json);
                                        editor.apply();     // Th
                                    } else {
                                    }

                                if(response.code() == 200){
                                    offlineFetch("");
                                }else{
                                    offlineFetch(response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                            : response.message());
                                }
                            } catch (Exception e) {
                                offlineFetch(e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(Call<RegisterResponse> call, Throwable t) {offlineFetch(t.getMessage());
                            offlineFetch(t.getMessage());
                        }
                    });
                }


                //Contract Sign/ select farmer/SearchContractFarmerActivity
                if(offline == 5 || offline == 0) {
                    Call<SearchContractResponse> call4 = service.getContractfarmer(0, 0, "", auth_key);
                    call4.enqueue(new Callback<SearchContractResponse>() {
                        @Override
                        public void onResponse(Call<SearchContractResponse> call, Response<SearchContractResponse> response) {
                            Log.d(context.getPackageName().toUpperCase(),"Checking whats not called: " + "3");
                            try {
                                if(response.body().getPageItems() != null)
                                    if (response.body().getPageItems().size() != 0) {
                                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        Gson gson = new Gson();
                                        String json = gson.toJson(response.body().getPageItems());
                                        //reportDataFetched("Contract Sign/ select farmer/SearchContractFarmerActivity", json,context);
                                        editor.putString("contractfarmer", json);
                                        editor.apply();

                                    }
                                if(response.code() == 200){
                                    offlineFetch("");
                                }else{
                                    offlineFetch(response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                            : response.message());
                                }
                            } catch (Exception e) {
                                offlineFetch(e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<SearchContractResponse> call, Throwable t) {offlineFetch(t.getMessage());
                            offlineFetch(t.getMessage());
                        }
                    });
                }

                //Verify planting/select farmer/SearchPlantingVerificationActivity
                if(offline == 6 || offline == 0) {
                    Call<PlantVerifyResponse> call5 = service.getplantingfarmer(0, 0, "", auth_key);
                    call5.enqueue(new Callback<PlantVerifyResponse>() {
                        @Override
                        public void onResponse(Call<PlantVerifyResponse> call, Response<PlantVerifyResponse> response) {
                            Log.d(context.getPackageName().toUpperCase(),"Checking whats not called: " + "4");
                            try {
                                System.out.println(response.body().getPageItemsPlantVerify());

                                if (response.body().getPageItemsPlantVerify().size() != 0) {
//                               pageItemArrayList = (ArrayList<PageItemsPlantVerify>) response.body().getPageItemsPlantVerify();
//                               saveArrayList(pageItemArrayList, "verifyplantingfarmer");

                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body().getPageItemsPlantVerify());
                                    //reportDataFetched("Verify planting/select farmer/SearchPlantingVerificationActivity", json,context);
                                    editor.putString("verifyplantingfarmer", json);
                                    editor.apply();     // This line is IMPORTANT !!!

                                }

                                if(response.code() == 200){
                                    offlineFetch("");
                                }else{
                                    offlineFetch(response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                            : response.message());
                                }
                            } catch (Exception e) {
                                offlineFetch(e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(Call<PlantVerifyResponse> call, Throwable t) {offlineFetch(t.getMessage());

                        }
                    });
                }

                //Crop Desctuction/select farmer/SearchDestructionFarmerActivity
                if(offline == 7 || offline == 0) {
                    Call<SearchDestructionResponse> call6 = service.getDestructionfarmer(0, 0, "", auth_key);
                    call6.enqueue(new Callback<SearchDestructionResponse>() {
                        @Override
                        public void onResponse(Call<SearchDestructionResponse> call, Response<SearchDestructionResponse> response) {
                            Log.d(context.getPackageName().toUpperCase(),"Checking whats not called: " + "5");
                            try {

                                if (response.body().getPageItemsDestruction().size() != 0) {
//                               pageItemArrayList = (ArrayList<PageItemsDestruction>) response.body().getPageItemsDestruction();
//                               saveArrayList(pageItemArrayList, "destructionfarmer");
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body().getPageItemsDestruction());
                                    //reportDataFetched("Crop Desctuction/select farmer/SearchDestructionFarmerActivity", json,context);
                                    editor.putString("destructionfarmer", json);
                                    editor.apply();
                                }

                                if(response.code() == 200){
                                    offlineFetch("");
                                }else{
                                    offlineFetch(response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                            : response.message());
                                }
                            } catch (Exception e) {
                                offlineFetch(e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(Call<SearchDestructionResponse> call, Throwable t) {offlineFetch(t.getMessage());

                        }
                    });
                }

                //Spray Confirmation/select farmer/SearchSprayActivity
                if(offline == 8 || offline == 0) {
                    Call<SprayFarmerResponse> call7 = service.getSprayfarmer(0, 0, "", auth_key);
                    call7.enqueue(new Callback<SprayFarmerResponse>() {
                        @Override
                        public void onResponse(Call<SprayFarmerResponse> call, Response<SprayFarmerResponse> response) {
                            Log.d(context.getPackageName().toUpperCase(),"Checking whats not called: " + "6");
                            try {
                                if (response.body().getPageItemsSprayFarmer().size() != 0) {
//                               pageItemArrayList = (ArrayList<PageItemsSprayFarmer>) response.body().getPageItemsSprayFarmer();
//                               saveArrayList(pageItemArrayList, "sprayfarmer");
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body().getPageItemsSprayFarmer());
                                    //reportDataFetched("Spray Confirmation/select farmer/SearchSprayActivity", json,context);
                                    editor.putString("sprayfarmer", json);
                                    editor.apply();     // This line is IMPORTANT !!!
                                }

                                if(response.code() == 200){
                                    offlineFetch("");
                                }else{
                                    offlineFetch(response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                            : response.message());
                                }
                            } catch (Exception e) {
                                offlineFetch(e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(Call<SprayFarmerResponse> call, Throwable t) {offlineFetch(t.getMessage());

                        }
                    });
                }

                //Harvest Collection/select farmer/SearchHarvestFarmerActivity
                if(offline == 9 || offline == 0) {
                    Call<FarmerHarvestResponse> call8 = service.getHarvestfarmer(0, 0, "", auth_key);
                    call8.enqueue(new Callback<FarmerHarvestResponse>() {
                        @Override
                        public void onResponse(Call<FarmerHarvestResponse> call, Response<FarmerHarvestResponse> response) {
                            Log.d(context.getPackageName().toUpperCase(),"Checking whats not called: " + "7");
                            try {


                                if (response.body().getPageItemHarvest().size() != 0) {
//                               pageItemArrayList = (ArrayList<PageItemHarvest>) response.body().getPageItemHarvest();
//                               saveArrayList(pageItemArrayList, "harvestfarmer");

                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body().getPageItemHarvest());

                                    //reportDataFetched("Harvest Collection/select farmer/SearchHarvestFarmerActivity", json,context);
                                    editor.putString("harvestfarmer", json);
                                    editor.apply();     // This line is IMPORTANT !!!
                                }
                                if(response.code() == 200){
                                    offlineFetch("");
                                }else{
                                    offlineFetch(response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                            : response.message());
                                }
                            } catch (Exception e) {
                                offlineFetch(e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<FarmerHarvestResponse> call, Throwable t) {offlineFetch(t.getMessage());

                        }
                    });
                }

                //Register Block/select farm, spinner
                if(offline == 10 || offline == 0) {
                    Call<List<GeneralSpinnerResponse>> call9 = service.getFarmNames(auth_key);
                    call9.enqueue(new Callback<List<GeneralSpinnerResponse>>() {
                        @Override
                        public void onResponse(Call<List<GeneralSpinnerResponse>> call, Response<List<GeneralSpinnerResponse>> response) {
                            Log.d(context.getPackageName().toUpperCase(),"Checking whats not called: " + "8");
                            try{
                                if (response.body() != null) {
                                    if (response.body() != null)
                                        if (response.body().size() > 0) {
                                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                            SharedPreferences.Editor editor = prefs.edit();
                                            Gson gson = new Gson();
                                            String json = gson.toJson(response.body());
                                            // reportDataFetched("Register Block/select farm, spinner", json,context);
                                            editor.putString("RegisterBlockSelectFarm", json);
                                            editor.apply();
                                        }
                                }

                                if(response.code() == 200){
                                    offlineFetch("");
                                }else{
                                    offlineFetch(response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                            : response.message());
                                }
                            } catch (Exception e) {
                                offlineFetch(e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(Call<List<GeneralSpinnerResponse>> call, Throwable t) {offlineFetch(t.getMessage());

                        }
                    });
                }



                //Plant Block/select block/SearchSearchAreaActivity
                if(offline == 11 || offline == 0) {
                    Call<SearchAreaResponse> call10 = service.getSearchArea(/*15, 0*/limit, offset, auth_key);
                    call10.enqueue(new Callback<SearchAreaResponse>() {
                        @Override
                        public void onResponse(Call<SearchAreaResponse> call, Response<SearchAreaResponse> response) {
                            Log.d(context.getPackageName().toUpperCase(),"Checking whats not called: " + "9");
                            try {
                                if (response.body().getPageItemSearchAreaList().size() > 0) {
                                    //pageItemArrayList = (ArrayList<PageItemSearchArea>) response.body().getPageItemSearchAreaList();
                                    //saveArrayList(pageItemArrayList, "harvestblocksearch");
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body().getPageItemSearchAreaList());
                                    //reportDataFetched("Plant Block/select block/SearchSearchAreaActivity", json,context);
                                    editor.putString("SearchSearchAreaActivity", json);
                                    editor.apply();     // This line is IMPORTANT !!!
                                } else {

                                }
                                if(response.code() == 200){
                                    offlineFetch("");
                                }else{
                                    offlineFetch(response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                            : response.message());
                                }
                            } catch (Exception e) {
                                offlineFetch(e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(Call<SearchAreaResponse> call, Throwable t) {offlineFetch(t.getMessage());

                        }
                    });
                }

                //Plant Block/spinner/PlantBlockActivity
                if(offline == 12 || offline == 0) {
                    Call<List<GeneralSpinnerResponse>> call101 = service.getVariety(auth_key);
                    call101.enqueue(new Callback<List<GeneralSpinnerResponse>>() {
                        @Override
                        public void onResponse(Call<List<GeneralSpinnerResponse>> call, Response<List<GeneralSpinnerResponse>> response) {
                            Log.d(context.getPackageName().toUpperCase(),"Checking whats not called: " + "10");
                            try{
                                if (response.body() != null) {
                                    if (response.body() != null)
                                        if (response.body().size() > 0) {
                                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                            SharedPreferences.Editor editor = prefs.edit();
                                            Gson gson = new Gson();
                                            String json = gson.toJson(response.body());
                                            //reportDataFetched("Plant Block/spinner/PlantBlockActivity", json,context);
                                            editor.putString("PlantBlockSpinner", json);
                                            editor.apply();
                                        }




                                }

                                if(response.code() == 200){
                                    offlineFetch("");
                                }else{
                                    offlineFetch(response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                            : response.message());
                                }
                            } catch (Exception e) {
                                offlineFetch(e.getMessage());
                            }



                        }

                        @Override
                        public void onFailure(Call<List<GeneralSpinnerResponse>> call, Throwable t) {offlineFetch(t.getMessage());

                        }
                    });
                }



                //Irrigate Block/select block/SearchPlantBlockActivity
                if(offline == 13 || offline == 0) {
                    Call<PlantBlockResponse> call11 = service.getPlantBlockNames(/*15, 0*/limit, offset, auth_key);
                    call11.enqueue(new Callback<PlantBlockResponse>() {
                        @Override
                        public void onResponse(Call<PlantBlockResponse> call, Response<PlantBlockResponse> response) {
                            Log.d(context.getPackageName().toUpperCase(),"Checking whats not called: " + "11");
                            try {

                                if (response.body().getPageItemPlantBlocksList().size() > 0) {

                                    // pageItemArrayList = (ArrayList<PageItemPlantBlock>) response.body().getPageItemPlantBlocksList();
                                    // saveArrayList(pageItemArrayList, "plantblockresponse");
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body().getPageItemPlantBlocksList());
                                    // reportDataFetched("Irrigate Block/select block/SearchPlantBlockActivity", json,context);
                                    editor.putString("searchPlantBlockActivity", json);
                                    editor.apply();     // This line is IMPORTANT !!!
                                }
                                if(response.code() == 200){
                                    offlineFetch("");
                                }else{
                                    offlineFetch(response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                            : response.message());
                                }
                            } catch (Exception e) {
                                offlineFetch(e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<PlantBlockResponse> call, Throwable t) {offlineFetch(t.getMessage());

                        }
                    });
                }


                //Apply Fertilizer's/select block/SearchPlantBlockActivity
                if(offline == 14 || offline == 0) {
                    Call<PlantBlockResponse> call12 = service.getPlantBlockNames(/*15, 0*/limit, offset, auth_key);
                    call12.enqueue(new Callback<PlantBlockResponse>() {
                        @Override
                        public void onResponse(Call<PlantBlockResponse> call, Response<PlantBlockResponse> response) {
                            Log.d(context.getPackageName().toUpperCase(),"Checking whats not called: " + "12");
                            try {

                                if (response.body().getPageItemPlantBlocksList().size() > 0) {

//                               pageItemArrayList = (ArrayList<PageItemPlantBlock>) response.body().getPageItemPlantBlocksList();
//                               saveArrayList(pageItemArrayList, "searchPlantBlockActivity");

                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body().getPageItemPlantBlocksList());
                                    // reportDataFetched("Apply Fertilizer's/select block/SearchPlantBlockActivity", json,context);
                                    editor.putString("searchPlantBlockActivity", json);
                                    editor.apply();

                                }

                                if(response.code() == 200){
                                    offlineFetch("");
                                }else{
                                    offlineFetch(response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                            : response.message());
                                }
                            } catch (Exception e) {
                                offlineFetch(e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(Call<PlantBlockResponse> call, Throwable t) {offlineFetch(t.getMessage());

                        }
                    });
                }

                //Apply Fertilizer's/spinner/ApplyFertilizerBlockActivity
                if(offline == 15 || offline == 0) {
                    Call<List<GeneralSpinnerResponse>> call121 = service.getFertilizer(auth_key);
                    call121.enqueue(new Callback<List<GeneralSpinnerResponse>>() {
                        @Override
                        public void onResponse(Call<List<GeneralSpinnerResponse>> call, Response<List<GeneralSpinnerResponse>> response) {
                            Log.d(context.getPackageName().toUpperCase(),"Checking whats not called: " + "13");
                            try{
                                if (response.body() != null) {
                                    if (response.body() != null)
                                        if (response.body().size() > 0) {
                                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                            SharedPreferences.Editor editor = prefs.edit();
                                            Gson gson = new Gson();
                                            String json = gson.toJson(response.body());
                                            // reportDataFetched("Apply Fertilizer's/spinner/ApplyFertilizerBlockActivity", json,context);
                                            editor.putString("ApplyFertilizerSpinner", json);
                                            editor.apply();
                                        }
                                }
                                if(response.code() == 200){
                                    offlineFetch("");
                                }else{
                                    offlineFetch(response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                            : response.message());
                                }
                            } catch (Exception e) {
                                offlineFetch(e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(Call<List<GeneralSpinnerResponse>> call, Throwable t) {offlineFetch(t.getMessage());

                        }
                    });
                }



                //Scouting/select block/SearchPlantBlockActivity
                if(offline == 16 || offline == 0) {
                    Call<PlantBlockResponse> call13 = service.getPlantBlockNames(/*15, 0*/limit, offset, auth_key);
                    call13.enqueue(new Callback<PlantBlockResponse>() {
                        @Override
                        public void onResponse(Call<PlantBlockResponse> call, Response<PlantBlockResponse> response) {
                            Log.d(context.getPackageName().toUpperCase(),"Checking whats not called: " + "14");
                            try {

                                if (response.body().getPageItemPlantBlocksList().size() > 0) {

//                               pageItemArrayList = (ArrayList<PageItemPlantBlock>) response.body().getPageItemPlantBlocksList();
//                               saveArrayList(pageItemArrayList, "searchPlantBlockActivity");

                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body().getPageItemPlantBlocksList());
                                    //reportDataFetched("Scouting/select block/SearchPlantBlockActivity", json,context);
                                    editor.putString("searchPlantBlockActivity", json);
                                    editor.apply();

                                }

                                if(response.code() == 200){
                                    offlineFetch("");
                                }else{
                                    offlineFetch(response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                            : response.message());
                                }
                            } catch (Exception e) {
                                offlineFetch(e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(Call<PlantBlockResponse> call, Throwable t) {offlineFetch(t.getMessage());

                        }
                    });
                }

                //Harvest Block/select block/SearchPlantBlockActivity
                if(offline == 17 || offline == 0) {
                    Call<PlantBlockResponse> call14 = service.getPlantBlockNames(/*15, 0*/limit, offset, auth_key);
                    call14.enqueue(new Callback<PlantBlockResponse>() {
                        @Override
                        public void onResponse(Call<PlantBlockResponse> call, Response<PlantBlockResponse> response) {
                            Log.d(context.getPackageName().toUpperCase(),"Checking whats not called: " + "15");
                            try {

                                if (response.body().getPageItemPlantBlocksList().size() > 0) {

//                               pageItemArrayList = (ArrayList<PageItemPlantBlock>) response.body().getPageItemPlantBlocksList();
//                               saveArrayList(pageItemArrayList, "searchPlantBlockActivity");

                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body().getPageItemPlantBlocksList());
                                    // reportDataFetched("Harvest Block/select block/SearchPlantBlockActivity", json,context);
                                    editor.putString("searchPlantBlockActivity", json);
                                    editor.apply();

                                }

                                if(response.code() == 200){
                                    offlineFetch("");
                                }else{
                                    offlineFetch(response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                            : response.message());
                                }
                            } catch (Exception e) {
                                offlineFetch(e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<PlantBlockResponse> call, Throwable t) {offlineFetch(t.getMessage());

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
                            Log.d(context.getPackageName().toUpperCase(),"Checking whats not called: " + "16");
                            try {
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

                                                        try {
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

                                                                //reportDataFetched("HomeActivity", hashMap.toString(),context);

                                                                OfflineFeature.saveSharedPreferences(hashMap, "ReturnActivityOffline", context);


                                                            }


                                                        } catch (Exception e) {
                                                            // offlineFetch(e.getMessage());
                                                        }


                                                    }

                                                    @Override
                                                    public void onFailure(Call<PlantingVerificationResponse> call, Throwable t) {
                                                        offlineFetch(t.getMessage());

                                                    }
                                                });
                                            }

                                            count++;
                                        }
                                    }

                                if(response.code() == 200){
                                    offlineFetch("");
                                }else{
                                    offlineFetch(response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                            : response.message());
                                }

                            }catch (Exception e){

                            }
                        }

                        @Override
                        public void onFailure(Call<TasksResponse> call, Throwable t) {offlineFetch(t.getMessage());

                        }
                    });
                }

                //FarmerRecruiteActivity/Crop Date
                if(offline == 19 || offline == 0) {
                    Call<List<CropDateResponse>> callCropDate = service.getRecruitCropDates(auth_key);
                    callCropDate.enqueue(new Callback<List<CropDateResponse>>() {
                        @Override
                        public void onResponse(Call<List<CropDateResponse>> call, Response<List<CropDateResponse>> response) {
                            Log.d(context.getPackageName().toUpperCase(),"Checking whats not called: " + "17");
                            try {
                                if (response.body() != null) {
                                    if (response.body().size() > 0) {
                                        // reportDataFetched("FarmerRecruiteActivity/Crop Date", response.body().toString(),context);
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
                                if(response.code() == 200){
                                    offlineFetch("");
                                }else{
                                    offlineFetch(response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                            : response.message());
                                }
                            } catch (Exception e) {
                                offlineFetch(e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(Call<List<CropDateResponse>> call, Throwable t) {offlineFetch(t.getMessage());

                        }
                    });
                }

                //SprayConfirmationActivity/Spray Number
                if(offline == 20 || offline == 0) {
                    Call<SprayNumbersResponse> callSprayNumber = service.getspraynumber(auth_key);
                    callSprayNumber.enqueue(new Callback<SprayNumbersResponse>() {
                        @Override
                        public void onResponse(Call<SprayNumbersResponse> call, Response<SprayNumbersResponse> response) {
                            Log.d(context.getPackageName().toUpperCase(),"Checking whats not called: " + "18");
                            try {
                                if (response.body().getPageItemsSprayNumbers().size() != 0) {

                                    SprayNumberDB.deleteAll(SprayNumberDB.class);
                                    for (PageItemsSprayNumber blacklist : response.body().getPageItemsSprayNumbers()) {

                                        SprayNumberDB book = new SprayNumberDB(blacklist.getId(), blacklist.getSprayno());
                                        book.save();
                                    }

                                    //reportDataFetched("SprayConfirmationActivity/Spray Number", response.body().toString(),context);

                                } else {

                                }

                                if(response.code() == 200){
                                    offlineFetch("");
                                }else{
                                    offlineFetch(response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                            : response.message());
                                }
                            } catch (Exception e) {
                                offlineFetch(e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(Call<SprayNumbersResponse> call, Throwable t) {offlineFetch(t.getMessage());

                        }
                    });
                }

                //CropDesturctionActivity/destruction reason
                if(offline == 21 || offline == 0) {
                    Call<List<DestructionReasonResponse>> callDestructionReason = service.getdestructionreasons(auth_key);
                    callDestructionReason.enqueue(new Callback<List<DestructionReasonResponse>>() {
                        @Override
                        public void onResponse(Call<List<DestructionReasonResponse>> call, Response<List<DestructionReasonResponse>> response) {
                            Log.d(context.getPackageName().toUpperCase(),"Checking whats not called: " + "19");
                            try{
                                if (response.body() != null) {
                                    if (response.body().size() > 0) {
                                        CropDestructionDB.deleteAll(CropDestructionDB.class);
                                        for (DestructionReasonResponse reason : response.body()) {
                                            CropDestructionDB book = new CropDestructionDB(reason.getId(), reason.getDestructionShtDesc(), reason.getDestructionReason(), reason.getDestructionType());
                                            book.save();
                                        }

                                        // reportDataFetched("CropDesturctionActivity/destruction reason", response.body().toString(),context);
                                    }
                                }
                                if(response.code() == 200){
                                    offlineFetch("");
                                }else{
                                    offlineFetch(response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                            : response.message());
                                }
                            } catch (Exception e) {
                                offlineFetch(e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(Call<List<DestructionReasonResponse>> call, Throwable t) {offlineFetch(t.getMessage());

                        }
                    });
                }
                
                //ManAcivity, Spinner bank
                if(offline == 22 || offline == 0) {
                    Call<List<BankNameResponse>> callSpinnerBank = service.getbankname(auth_key);
                    callSpinnerBank.enqueue(new Callback<List<BankNameResponse>>() {
                        @Override
                        public void onResponse(Call<List<BankNameResponse>> call, Response<List<BankNameResponse>> response) {
                            Log.d(context.getPackageName().toUpperCase(),"Checking whats not called: " + "20");

                            try{
                                if (response.body() != null) {
                                    if (response.body().size() > 0) {
                                        BankNameDB.deleteAll(BankNameDB.class);
                                        for (BankNameResponse bank : response.body()) {
                                            BankNameDB book = new BankNameDB(bank.getId(), bank.getBankname(), bank.getAccountformat());
                                            book.save();
                                        }


                                        //reportDataFetched("ManAcivity, Spinner bank", response.body().toString(),context);
                                    }

                                }
                                if(response.code() == 200){
                                    offlineFetch("");
                                }else{
                                    offlineFetch(response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).get("developerMessage").toString()
                                            : response.message());
                                }
                            } catch (Exception e) {
                                offlineFetch(e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(Call<List<BankNameResponse>> call, Throwable t) {offlineFetch(t.getMessage());

                        }
                    });
                }



//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Object o) {
//                super.onPostExecute(o);
//                Log.d(context.getPackageName().toUpperCase(), "End of SilentDataDump");
//            }
//        };
//        asyncTask1.execute();
    }

}