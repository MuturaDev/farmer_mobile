package com.cw.farmer.activity.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.farmer.NetworkUtil;
import com.cw.farmer.R;
import com.cw.farmer.activity.ContractSignActivity;
import com.cw.farmer.activity.CropDestructionActivity;
import com.cw.farmer.activity.FarmerRecruitActivity;
import com.cw.farmer.activity.HarvestingActivity;
import com.cw.farmer.activity.ListFarmerActivity;
import com.cw.farmer.activity.MainActivity;
import com.cw.farmer.activity.MyDashboardActivity;
import com.cw.farmer.activity.PlantingVerificationActivity;
import com.cw.farmer.activity.SprayConfirmationActivity;
import com.cw.farmer.adapter.AdhocAdapter;
import com.cw.farmer.adapter.DashboardAdapter;
import com.cw.farmer.adapter.ExpandableListAdapter;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.Accountdetails;
import com.cw.farmer.model.AdhocResponse;
import com.cw.farmer.model.AllResponse;
import com.cw.farmer.model.ContractSignDB;
import com.cw.farmer.model.CropDestructionPostDB;
import com.cw.farmer.model.FarmerErrorResponse;
import com.cw.farmer.model.FarmerModel;
import com.cw.farmer.model.FarmerModelDB;
import com.cw.farmer.model.HarvestingDB;
import com.cw.farmer.model.Identitydetails;
import com.cw.farmer.model.PageItemsAdhoc;
import com.cw.farmer.model.PageItemstask;
import com.cw.farmer.model.PlantingVerifyDB;
import com.cw.farmer.model.RecruitFarmerDB;
import com.cw.farmer.model.SprayPostDB;
import com.cw.farmer.model.TasksResponse;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;
import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;

public class HomeFragment extends Fragment {

    LinearLayout lin_register, lin_create;
    String user_id;
    RecyclerView rv_register;
    AdhocAdapter registerAdapter;
    ProgressDialog progressDialog;
    ArrayList<PageItemsAdhoc> pageItemArrayListAdhoc;
    AppBarLayout Appbar;
    Toolbar toolbar;
    CardView register_farmer, view_farmer, recruit_farmer, contract_signing, verify_planting, crop_destruction, harvest_collection, inventory_mgt, dashboard;
    boolean ExpandedActionBar = true;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    View root;
    private HomeViewModel homeViewModel;
    private Context mContext = getActivity();
    private DashboardAdapter adapter;
    private List<com.cw.farmer.model.dashboard> dashboardList;

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        lin_register = root.findViewById(R.id.lin_register);
        lin_create = root.findViewById(R.id.lin_farmer_bank_det);

        return root;
    }

    /**
     * Converting dp to pixel
     */
    public int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    private void setData() {


    }

    public void openregister(View v) {
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    public void openfarmerlist(View v) {
        startActivity(new Intent(getActivity(), ListFarmerActivity.class));
    }

    public void openrecruit(View v) {
        startActivity(new Intent(getActivity(), FarmerRecruitActivity.class));
    }

    public void opencontract(View v) {
        startActivity(new Intent(getActivity(), ContractSignActivity.class));
    }

    public void opendestruction(View v) {
        startActivity(new Intent(getActivity(), CropDestructionActivity.class));
    }

    public void openharvesting(View v) {
        startActivity(new Intent(getActivity(), HarvestingActivity.class));
    }

    public void openplantverfication(View v) {
        startActivity(new Intent(getActivity(), PlantingVerificationActivity.class));
    }

    public void opensprayconfirmation(View v) {
        startActivity(new Intent(getActivity(), SprayConfirmationActivity.class));
    }

    public void opendashboard(View v) {
        startActivity(new Intent(getActivity(), MyDashboardActivity.class));
    }

    private void prepareListData() {
        progressDialog.setCancelable(false);
        // progressBar.setMessage("Please Wait...");
        progressDialog.show();
        Retrofit retrofit = ApiClient.getClient("/authentication/", getActivity());
        APIService service = retrofit.create(APIService.class);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
        String auth_key = prefs.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
        Call<TasksResponse> call = service.gettask(auth_key, user_id + "");
        call.enqueue(new Callback<TasksResponse>() {
            @Override
            public void onResponse(Call<TasksResponse> call, Response<TasksResponse> response) {
                progressDialog.hide();
                try {
                    if (response.body().getTotalFilteredRecords() > 0) {
                        listDataHeader = new ArrayList<String>();
                        listDataChild = new HashMap<String, List<String>>();

                        // Adding child data
                        listDataHeader.add("Planting");
                        listDataHeader.add("Spray");
                        listDataHeader.add("Return");
                        List<String> message = new ArrayList<String>();
                        List<String> invent = new ArrayList<String>();
                        List<String> return_inve = new ArrayList<String>();
                        for (PageItemstask taks : response.body().getPageItemstasks()) {

                            if (taks.getEntityName().equals("VERIFY_PLANTING_MOBILE")) {
                                String date = "";
                                for (int elem : taks.getCropDate()) {
                                    date = elem + "/" + date;
                                }
                                invent.add(taks.getCentrename() + " ,planting," + removeLastChar(date) + " ," + taks.getEntityId());
                            } else if (taks.getEntityName().equals("VERIFY_SPRAY_MOBILE")) {
                                String date = "";
                                for (int elem : taks.getCropDate()) {
                                    date = elem + "/" + date;
                                }
                                invent.add(taks.getCentrename() + " ,planting," + removeLastChar(date) + " ," + taks.getEntityId());
                            } else {
                                String date = "";
                                for (int elem : taks.getCreatedOn()) {
                                    date = elem + "/" + date;
                                }
                                return_inve.add(taks.getCentrename() + " ,return," + removeLastChar(date) + " ," + taks.getEntityId());
                            }


                        }

                        listDataChild.put(listDataHeader.get(0), invent); // Header, Child data
                        listDataChild.put(listDataHeader.get(1), message);
                        listDataChild.put(listDataHeader.get(2), return_inve);


                        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

                        // setting list adapter
                        expListView.setAdapter(listAdapter);
                        adhoc();
                    } else {

                        Utility.showToast(getActivity(), "No Task");
                    }

                } catch (Exception e) {
                    Utility.showToast(getActivity(), e.getMessage());
                    System.out.println(e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<TasksResponse> call, Throwable t) {
                progressDialog.hide();
                Utility.showToast(getActivity(), t.getMessage());
            }
        });

    }

    public void adhoc() {
        progressDialog.setCancelable(false);
        // progressBar.setMessage("Please Wait...");
        progressDialog.show();
        if (NetworkUtil.getConnectivityStatusString(getActivity()).equals("yes")) {
            Retrofit retrofit = ApiClient.getClient("/authentication/", getActivity());
            APIService service = retrofit.create(APIService.class);
            SharedPreferences prefs = this.getActivity().getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
            String auth_key = prefs.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
            Call<AdhocResponse> call = service.getAdhoc(user_id, auth_key);
            call.enqueue(new Callback<AdhocResponse>() {
                @Override
                public void onResponse(Call<AdhocResponse> call, Response<AdhocResponse> response) {
                    progressDialog.hide();
                    try {
                        if (response.body().getPageItemsAdhoc().size() != 0) {
                            pageItemArrayListAdhoc = (ArrayList<PageItemsAdhoc>) response.body().getPageItemsAdhoc();
                            registerAdapter = new AdhocAdapter(rv_register, getActivity(), pageItemArrayListAdhoc);
                            DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), HORIZONTAL);
                            rv_register.addItemDecoration(itemDecor);
                            rv_register.setAdapter(registerAdapter);
                        } else {
                            Toast.makeText(getActivity(), "No message found", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Utility.showToast(getActivity(), e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<AdhocResponse> call, Throwable t) {
                    progressDialog.hide();
                    Utility.showToast(getActivity(), t.getMessage());
                }
            });
        }
    }

    public int getPixelValue(int dp) {

        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public void offlinesync(View v) {
        Toast.makeText(getActivity(), "Synchronisation process has started", Toast.LENGTH_SHORT).show();

        if (NetworkUtil.getConnectivityStatusString(getActivity()).equals("yes")) {
            SharedPreferences prefs = this.getActivity().getSharedPreferences("location", MODE_PRIVATE);


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
                SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Synchronizing offline data to online ...");
                pDialog.setCancelable(false);
                pDialog.show();

                Retrofit retrofit = ApiClient.getClient("/authentication/", getActivity());
                APIService service = retrofit.create(APIService.class);
                SharedPreferences prefs_auth = this.getActivity().getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                Call<FarmerErrorResponse> call = service.createFarmer(auth_key, farmerModel);
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
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
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

                SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Synchronizing offline data to online ...");
                pDialog.setCancelable(false);
                pDialog.show();

                Retrofit retrofit = ApiClient.getClient("/authentication/", getActivity());
                APIService service = retrofit.create(APIService.class);
                SharedPreferences prefs_auth = this.getActivity().getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                Call<AllResponse> call = service.recruit(auth_key, hashMap);
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
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
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

                SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Synchronizing offline data to online ...");
                pDialog.setCancelable(false);
                pDialog.show();

                Retrofit retrofit = ApiClient.getClient("/authentication/", getActivity());
                APIService service = retrofit.create(APIService.class);
                SharedPreferences prefs_auth = this.getActivity().getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                Call<AllResponse> call = service.postcontract(auth_key, hashMap);
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
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
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

                SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Synchronizing offline data to online ...");
                pDialog.setCancelable(false);
                pDialog.show();

                Retrofit retrofit = ApiClient.getClient("/authentication/", getActivity());
                APIService service = retrofit.create(APIService.class);
                SharedPreferences prefs_auth = this.getActivity().getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                Call<AllResponse> call = service.postplantverify(auth_key, hashMap);
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
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
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

                SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Synchronizing offline data to online ...");
                pDialog.setCancelable(false);
                pDialog.show();

                Retrofit retrofit = ApiClient.getClient("/authentication/", getActivity());
                APIService service = retrofit.create(APIService.class);
                SharedPreferences prefs_auth = this.getActivity().getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                Call<AllResponse> call = service.postcropdestruction(auth_key, hashMap);
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
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
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
                SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Synchronizing offline data to online ...");
                pDialog.setCancelable(false);
                pDialog.show();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("dateid", harvest.dateid);
                hashMap.put("noofunits", harvest.noofunits);
                hashMap.put("farmerid", harvest.farmerid);
                hashMap.put("harvestkilos", harvest.harvestkilos);
                hashMap.put("locale", harvest.locale);

                Retrofit retrofit = ApiClient.getClient("/authentication/", getActivity());
                APIService service = retrofit.create(APIService.class);
                SharedPreferences prefs_auth = this.getActivity().getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                Call<AllResponse> call = service.postharvesting(auth_key, hashMap);
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
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
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
                SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Synchronizing offline data to online ...");
                pDialog.setCancelable(false);
                pDialog.show();

                Retrofit retrofit = ApiClient.getClient("/authentication/", getActivity());
                APIService service = retrofit.create(APIService.class);
                SharedPreferences prefs_auth = this.getActivity().getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                Call<AllResponse> call = service.spraypost(auth_key, hashMap);
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
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
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

        Toast.makeText(getActivity(), "Synchronisation process has ended", Toast.LENGTH_SHORT).show();

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
}