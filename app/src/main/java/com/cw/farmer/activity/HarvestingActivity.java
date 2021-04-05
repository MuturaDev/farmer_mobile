package com.cw.farmer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.farmer.HandleConnectionAppCompatActivity;
import com.cw.farmer.NetworkUtil;
import com.cw.farmer.R;
import com.cw.farmer.model.AllResponse;
import com.cw.farmer.model.EditContentModel;
import com.cw.farmer.model.HarvestingDB;
import com.cw.farmer.model.PageItemHarvest;
import com.cw.farmer.offlinefunctions.OfflineDataSyncActivity;
import com.cw.farmer.offlinefunctions.OfflineFeature;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HarvestingActivity extends HandleConnectionAppCompatActivity {
    EditText farmer_harvesting,crop_weight;
    Spinner codedate_harvesting;
    String farmer_id_string,noofunits,plantingId, contract_id_string;
    List<String> cropDateId_list,reason_main;


    private boolean editEnabled;
    public ArrayList<EditContentModel> editContentModelArrayList = new ArrayList<>();
    private EditContentModel editModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harvesting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        farmer_harvesting = findViewById(R.id.farmer_harvesting);
        farmer_harvesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("harvesting","Search harvesting.....");
                SharedPreferences.Editor searcheditor = getSharedPreferences("search", MODE_PRIVATE).edit();
                searcheditor.putString("activity","destruction");
                searcheditor.apply();

                Intent intent = new Intent(HarvestingActivity.this, SearchHarvestFarmerActivity.class);
                startActivity(intent);
            }
        });
        codedate_harvesting = findViewById(R.id.codedate_harvesting);
        crop_weight = findViewById(R.id.crop_weight);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
           if( b.containsKey("name") &&
                   b.containsKey("farmerId") &&
                   b.containsKey("totalUnits") &&
                   b.containsKey("plantingId") &&
                   b.containsKey("id") &&
                   b.containsKey("crop_date")) {

               String name = (String) b.get("name");
               farmer_harvesting.setText(name);

               String id = (String) b.get("farmerId");
               farmer_id_string = id;

               String noofunits_now = (String) b.get("totalUnits");
               noofunits = noofunits_now;

               String plantingId_now = (String) b.get("plantingId");
               plantingId = plantingId_now;

               String contract_idnow = (String) b.get("id");
               contract_id_string = contract_idnow;

               cropDateId_list = new ArrayList<String>();
               String cropDateId = (String) b.get("plantingId");
               cropDateId_list.add(cropDateId);

               ArrayList<String> spinnerArray = new ArrayList<String>();
               spinnerArray.clear();
               String crop_date = (String) b.get("crop_date");
               spinnerArray.add(crop_date);
               ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(HarvestingActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
               codedate_harvesting.setAdapter(spinnerArrayAdapter);
           }
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent() != null){
            if(getIntent().getExtras() != null){
                if(getIntent().getExtras().containsKey("CHECKDATA")) {
                    editContentModelArrayList = (ArrayList<EditContentModel>) getIntent().getExtras().getSerializable("CHECKDATA");
                    editEnabled = true;
                }
            }
        }


        bottomSheetCode(editContentModelArrayList);
    }
    
    

    public LinearLayout layoutBottomSheet;
    private BottomSheetBehavior sheetBehavior;
    private TextView label_change_bottom_sheet_state_ID;
    private LinearLayout btn_change_bottom_sheet_state_ID;
    private RecyclerView recyclerView;

    //BOTTOM SHEET
    class CustomDialogAdapter extends RecyclerView.Adapter<CustomDialogAdapter.MyViewHolder>{

        private List<EditContentModel> editContentModelList;
        private Context context;


        public CustomDialogAdapter(Context context, List<EditContentModel> editContentModelList) {
            this.editContentModelList = editContentModelList;
            this.context = context;
        }



        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class MyViewHolder extends RecyclerView.ViewHolder {

            LinearLayout status_layout;
            TextView title,error_message;
            LinearLayout edit_content_item_layout;

            private  String removeLastChar(String str) {
                return str.substring(0, str.length() - 1);
            }

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                status_layout = itemView.findViewById(R.id.status_layout);
                title = itemView.findViewById(R.id.title);
                error_message = itemView.findViewById(R.id.error_message);
                edit_content_item_layout = itemView.findViewById(R.id.edit_content_item_layout);
                edit_content_item_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ArrayList<PageItemHarvest> list = (ArrayList<PageItemHarvest>) OfflineFeature.getSharedPreferencesObject("harvestfarmer", getApplicationContext(), PageItemHarvest.class);
                        EditContentModel positionModel = editContentModelList.get(getAdapterPosition());
                        editModel = positionModel;
                        HarvestingDB modelDB = (HarvestingDB) positionModel.getContentObject();

                        for(PageItemHarvest item : list){
                            if(item.getFamerName().equalsIgnoreCase(modelDB.farmerName) &&
                                String.valueOf(item.getFarmerId()).equalsIgnoreCase(modelDB.farmerid) &&
                                String.valueOf(item.getCropDateId()).equalsIgnoreCase(modelDB.dateid)){

                                farmer_harvesting.setText(item.getFamerName());
                                farmer_id_string = String.valueOf(item.getFarmerId());
                                noofunits = String.valueOf(item.getUnits());
                                plantingId = String.valueOf(item.getCropDateId());
                                contract_id_string = String.valueOf(item.getId());
                                cropDateId_list = new ArrayList<String>();
                                cropDateId_list.add(String.valueOf(item.getCropDateId()));
                                crop_weight.setText(modelDB.harvestkilos);


                                String date="";
                                for(int elem : item.getCropDate()){
                                    date=elem+"/"+date;
                                }

                                String finalDate = date;

                                ArrayList<String> spinnerArray = new ArrayList<String>();
                                spinnerArray.clear();
                                String crop_date =removeLastChar(finalDate);
                                spinnerArray.add(crop_date);
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(HarvestingActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                                codedate_harvesting.setAdapter(spinnerArrayAdapter);


                                break;
                            }
                        }

                        ((Button)findViewById(R.id.btn_recruit)).setText("Edit");
                        toggleBottomSheet();
                    }
                });

            }
        }


        @NonNull
        @Override
        public CustomDialogAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.edit_content_item_layout, parent, false);
            return  new CustomDialogAdapter.MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomDialogAdapter.MyViewHolder holder, int position) {
            holder.title.setText(editContentModelList.get(position).getTitle());
            holder.error_message.setText(editContentModelList.get(position).getErrorMessage());

            if(editContentModelList.get(position).isStatus())
                holder.status_layout.setBackground(context.getResources().getDrawable(R.drawable.edit_circle));
            else
                holder.status_layout.setBackground(context.getResources().getDrawable(R.drawable.not_edit_circle));
        }

        @Override
        public int getItemCount() {
            return editContentModelList.size();
        }
    }
    
    public void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            label_change_bottom_sheet_state_ID.setText("Hide Content for Edit");
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            label_change_bottom_sheet_state_ID.setText("Show Content for Edit");
        }
    }
    private void bottomSheetCode(ArrayList<EditContentModel> list){
        //BOTTOM SHEET
        layoutBottomSheet = findViewById(R.id.bottom_sheet);

        if(list == null){
            layoutBottomSheet.setVisibility(View.GONE);
        }else {
            if (list.size() == 0)
                layoutBottomSheet.setVisibility(View.GONE);
            else
                layoutBottomSheet.setVisibility(View.VISIBLE);
        }

        ImageView img_offline_sync = findViewById(R.id.img_offline_sync);
        img_offline_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
                    Intent intent = new Intent(HarvestingActivity.this, OfflineDataSyncActivity.class);
                    startActivity(intent);
                    finish();
                }else{

                    new SweetAlertDialog(HarvestingActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("No network connection")
                            .setContentText("Connect to the internet and try again.")
                            .setConfirmText("OK")
                            .show();

                }
            }
        });
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        label_change_bottom_sheet_state_ID = findViewById(R.id.label_change_bottom_sheet_state_ID);
        btn_change_bottom_sheet_state_ID = findViewById(R.id.btn_change_bottom_sheet_state_ID);

        /**
         * Bottom sheet state change listener.
         * We are changing bottom text when sheet changed state
         **/
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        label_change_bottom_sheet_state_ID.setText("Hide Content for Edit");
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        label_change_bottom_sheet_state_ID.setText("Show Content for Edit");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        btn_change_bottom_sheet_state_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                if(sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){

                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    //sheetBehavior.setPeekHeight(200);
                    label_change_bottom_sheet_state_ID.setText("Hide Content for Edit");//4
                }else{
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);//3
                    label_change_bottom_sheet_state_ID.setText("Show Content for Edit");

                }
            }
        });

      recyclerView = findViewById(R.id.edit_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(HarvestingActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        CustomDialogAdapter adapter = new CustomDialogAdapter(HarvestingActivity.this,list);
        recyclerView.setAdapter(adapter);

         sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        if(list != null)
        if(list.size() != 0){
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                        //sheetBehavior.setPeekHeight(200);
            label_change_bottom_sheet_state_ID.setText("Hide Content for Edit");//4
        }else{
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);//3
            label_change_bottom_sheet_state_ID.setText("Show Content for Edit");
        }

    }
    
    public boolean validate() {
        boolean valid = true;
        int errorColor;
        final int version = Build.VERSION.SDK_INT;

        //Get the defined errorColor from color resource.
        if (version >= 23) {
            errorColor = ContextCompat.getColor(getApplicationContext(), R.color.errorColor);
        } else {
            errorColor = getResources().getColor(R.color.errorColor);
        }

        if (codedate_harvesting == null && codedate_harvesting.getSelectedItem() ==null) {

            TextView errorText = (TextView)codedate_harvesting.getSelectedView();
            errorText.setError("select crop date");
            errorText.setTextColor(errorColor);//just to highlight that this is an error

            valid = false;
        }
        if (crop_weight.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("must input weight");
            ssbuilder.setSpan(fgcspan, 0, "must input weight".length(), 0);
            crop_weight.setError(ssbuilder);
            valid = false;
        } else {
            crop_weight.setError(null);
        }

        if (farmer_harvesting.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("Select a farmer");
            ssbuilder.setSpan(fgcspan, 0, "Select a farmer".length(), 0);
            farmer_harvesting.setError(ssbuilder);
            valid = false;
        } else {
            farmer_harvesting.setError(null);
        }




        return valid;
    }
    public void harvesting_submit(View v){
        if (!validate()) {
            Snackbar.make(v, "Correct the above errors first", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        //{
        //    "dateid":1,  // from lov api shared below
        //    "noofunits":1,  // from lov api shared below
        //    "farmerid":5,  //from lov  api shared below
        //    "Harvestkilos":5 //Number of kilos harvested,
        //    "locale":"en",
        //

        //}

        if(editEnabled){
            HarvestingDB harvestingDB =  HarvestingDB.findById(HarvestingDB.class, editModel.getTableID());
            harvestingDB.dateid  = plantingId;
            harvestingDB.noofunits = noofunits;
            harvestingDB.farmerid = farmer_id_string;
            harvestingDB.harvestkilos = crop_weight.getText().toString();
            harvestingDB.locale =  "en";
            harvestingDB.contractId = contract_id_string;
            harvestingDB.farmerName = farmer_harvesting.getText().toString();
            harvestingDB.save();

            for(EditContentModel contentModel : editContentModelArrayList){
                if(editModel.getTableID() == contentModel.getTableID()){
                    contentModel.setStatus(true);
                }
            }

            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            CustomDialogAdapter adapter = new CustomDialogAdapter(
                    HarvestingActivity.this,editContentModelArrayList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();


            if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")){
                Intent intent = new Intent(HarvestingActivity.this, OfflineDataSyncActivity.class);
                startActivity(intent);
                finish();
            }else{
                new SweetAlertDialog(HarvestingActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Successful Edit")
                        .setContentText( editModel.getTitle() + " details have been edited and saved offline, will submit the details when you have an internet connection.")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                toggleBottomSheet();
                                //startActivity(new Intent(MainActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                            }
                        })
                        .show();
            }

        }else {
            SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Submitting Harvesting Data...");
            pDialog.setCancelable(false);
            pDialog.show();
            if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("dateid", plantingId);
                hashMap.put("noofunits", noofunits);
                hashMap.put("contractId", contract_id_string);
                hashMap.put("farmerid", farmer_id_string);
                hashMap.put("harvestkilos", crop_weight.getText().toString());
                hashMap.put("locale", "en");

                Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                APIService service = retrofit.create(APIService.class);
                SharedPreferences prefs = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                String auth_key = prefs.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                Log.d("Payload", hashMap.toString());
                Call<AllResponse> call = service.postharvesting(auth_key, hashMap);
                call.enqueue(new Callback<AllResponse>() {
                    @Override
                    public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                        pDialog.dismissWithAnimation();
                        try {
                            if (response.body() != null) {
                                new SweetAlertDialog(HarvestingActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success")
                                        .setContentText("You have successfully submitted " + farmer_harvesting.getText() + " harvesting details")
                                        .setConfirmText("Ok")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                startActivity(new Intent(HarvestingActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                            }
                                        })
                                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                startActivity(new Intent(HarvestingActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                            }
                                        })
                                        .show();

                            } else {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                new SweetAlertDialog(HarvestingActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Ooops...")
                                        .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                        .show();

                            }
                        } catch (Exception e) {
                            new SweetAlertDialog(HarvestingActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText(e.getMessage())
                                    .show();
                        }
                        //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Call<AllResponse> call, Throwable t) {
                        pDialog.cancel();
                        new SweetAlertDialog(HarvestingActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText(t.getMessage())
                                .show();
                    }
                });
            } else {
                HarvestingDB book = new HarvestingDB(plantingId, noofunits, farmer_id_string, crop_weight.getText().toString(), "en", contract_id_string, farmer_harvesting.getText().toString());
                book.save();
                pDialog.cancel();
                new SweetAlertDialog(HarvestingActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("No Wrong")
                        .setContentText("We have saved the data offline, We will submitted it when you have internet")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                startActivity(new Intent(HarvestingActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                            }
                        })
                        .show();

            }
        }

    }

}
