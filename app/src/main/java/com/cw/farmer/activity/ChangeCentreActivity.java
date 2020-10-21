package com.cw.farmer.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.cw.farmer.HandleConnectionAppCompatActivity;
import com.cw.farmer.R;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.AllCentreResponse;
import com.cw.farmer.model.AllResponse;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.cw.farmer.table_models.AllCentreTB;
import com.cw.farmer.table_models.ChangeCentreTB;
import com.cw.farmer.table_models.FarmerAccountTB;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangeCentreActivity extends HandleConnectionAppCompatActivity {
    List<Integer> centre_id;
    String user_id, centre;
    EditText search_centre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_centre);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        TextView current_centre = findViewById(R.id.current_centre);
        search_centre = findViewById(R.id.search_centre);


        if (b != null) {
            String centre_name = (String) b.get("centre_name");
            current_centre.setText(centre_name);
            user_id = (String) b.get("user_id") + "";
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void searchcentre(View v) {
        if (Utility.isNetworkAvailable(this)) {
            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
            APIService service = retrofit.create(APIService.class);
            SharedPreferences prefs = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
            String auth_key = prefs.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
            Call<List<AllCentreResponse>> call = service.getcentre(auth_key);
            call.enqueue(new Callback<List<AllCentreResponse>>() {
                @Override
                public void onResponse(Call<List<AllCentreResponse>> call, Response<List<AllCentreResponse>> response) {
                    Dialog dialog = new Dialog(ChangeCentreActivity.this);
                    LayoutInflater inflater = LayoutInflater.from(ChangeCentreActivity.this);
                    View view1 = inflater.inflate(R.layout.custom_centresearch, null);
                    ListView listView = view1.findViewById(R.id.listView);
                    SearchView searchView = view1.findViewById(R.id.searchView);
                    centre_id = new ArrayList<Integer>();
                    ArrayList<String> spinnerArray = new ArrayList<String>();
                    spinnerArray.clear();
                    for (AllCentreResponse doc : response.body()) {
                        spinnerArray.add(doc.getName());
                        centre_id.add(doc.getId());

                    }
                    final ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(ChangeCentreActivity.this, android.R.layout.simple_list_item_1, spinnerArray);
                    listView.setAdapter(stringArrayAdapter);

                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String newText) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            stringArrayAdapter.getFilter().filter(newText);
                            return false;
                        }
                    });
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            // TODO Auto-generated method stub
                            //Toast.makeText(ChangeCentreActivity.this, spinnerArray.get(position), Toast.LENGTH_SHORT).show();
                            centre = centre_id.get(position) + "";
                            search_centre.setText(spinnerArray.get(position));
                            dialog.dismiss();
                        }
                    });
                    dialog.setContentView(view1);
                    dialog.show();


                }

                @Override
                public void onFailure(Call<List<AllCentreResponse>> call, Throwable t) {
                    Log.w("myApp", t.toString());
                    Toast.makeText(ChangeCentreActivity.this, t.toString(), Toast.LENGTH_LONG).show();

                }
            });
        }else{
            Dialog dialog = new Dialog(ChangeCentreActivity.this);
            LayoutInflater inflater = LayoutInflater.from(ChangeCentreActivity.this);
            View view1 = inflater.inflate(R.layout.custom_centresearch, null);
            ListView listView = view1.findViewById(R.id.listView);
            SearchView searchView = view1.findViewById(R.id.searchView);
            centre_id = new ArrayList<Integer>();
            ArrayList<String> spinnerArray = new ArrayList<String>();
            spinnerArray.clear();
            for (AllCentreTB doc : AllCentreTB.listAll(AllCentreTB.class)) {
                spinnerArray.add(doc.getName());
                centre_id.add(doc.getID());

            }
            final ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(ChangeCentreActivity.this, android.R.layout.simple_list_item_1, spinnerArray);
            listView.setAdapter(stringArrayAdapter);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String newText) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    stringArrayAdapter.getFilter().filter(newText);
                    return false;
                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    //Toast.makeText(ChangeCentreActivity.this, spinnerArray.get(position), Toast.LENGTH_SHORT).show();
                    centre = centre_id.get(position) + "";
                    search_centre.setText(spinnerArray.get(position));
                    dialog.dismiss();
                }
            });
            dialog.setContentView(view1);
            dialog.show();
        }



    }

    public void submit(View v) {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Submitting Farmer Centre Changing Request...");
        pDialog.setCancelable(false);
        pDialog.show();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("activate", "N");
        hashMap.put("farmerid", user_id);
        hashMap.put("centerid", centre);

        if (Utility.isNetworkAvailable(this)) {

            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
            APIService service = retrofit.create(APIService.class);
            SharedPreferences prefs = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
            String auth_key = prefs.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
            Call<AllResponse> call = service.postchangecentre(auth_key, hashMap);
            call.enqueue(new Callback<AllResponse>() {
                @Override
                public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                    pDialog.dismissWithAnimation();
                    try {
                        if (response.body() != null) {
                            new SweetAlertDialog(ChangeCentreActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success")
                                    .setContentText("You have successfully changed farmer centre")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            startActivity(new Intent(ChangeCentreActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                        }
                                    })
                                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            startActivity(new Intent(ChangeCentreActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                        }
                                    })
                                    .show();

                        } else {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            new SweetAlertDialog(ChangeCentreActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Ooops...")
                                    .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                    .show();

                        }
                    } catch (Exception e) {
                        new SweetAlertDialog(ChangeCentreActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText(e.getMessage())
                                .show();
                    }
                    //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(Call<AllResponse> call, Throwable t) {
                    pDialog.cancel();
                    new SweetAlertDialog(ChangeCentreActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(t.getMessage())
                            .show();
                }
            });
        }else{

            ChangeCentreTB change = new ChangeCentreTB(
                    hashMap.get("activate"),
                    hashMap.get("farmerid"),
                    hashMap.get("centerid")
            );
            change.save();

            new SweetAlertDialog(ChangeCentreActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("No Wrong")
                    .setContentText("We have saved the data offline, We will submitted it when you have internet")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            startActivity(new Intent(ChangeCentreActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                        }
                    })
                    .show();
        }
    }

}
