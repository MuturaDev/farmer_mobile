package com.cw.farmer.model;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.farmer.R;
import com.cw.farmer.activity.MainActivity;
import com.cw.farmer.adapter.RegisterAdapter;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;
import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

public class scheme extends Fragment {
    RecyclerView rv_register;
    RegisterAdapter registerAdapter;
    ProgressDialog progressDialog;
    ArrayList<PageItem> pageItemArrayList;
    ArrayList<PageItem> array_sort;
    FloatingActionButton fab;
    EditText search;
    Button btn_search_farm;

    private int page=0;
    private int limit=10;
    private int offset=0;
    private boolean end=false;
    int textlength = 0;
    View root;

    public scheme() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.scheme, container, false);
        rv_register = root.findViewById(R.id.rv_registers);
        fab = root.findViewById(R.id.fab);
        search = root.findViewById(R.id.search);
        rv_register.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressDialog = new ProgressDialog(getActivity());


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });
        btn_search_farm = root.findViewById(R.id.btn_search_farm);
        btn_search_farm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isNetworkAvailable(getActivity())) {
                    getData();
                } else {



                    if(search.getText().toString().isEmpty()){
                        pageItemArrayList = getArrayList("viewfarmer");
                    }else{

                        pageItemArrayList = new ArrayList<>();
                        pageItemArrayList.clear();

                        for(PageItem item : getArrayList("viewfarmer")){
                            if(item.getFirstname().toLowerCase().contains(search.getText().toString())
                            || item.getIdno().toLowerCase().contains(search.getText().toString())
                            ){
                                pageItemArrayList.add(item);
                            }
                        }
                    }

                    setData();
                }
            }
        });

        return root;

    }

    private void getData() {
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        Retrofit retrofit = ApiClient.getClient("/authentication/", getContext());
        APIService service = retrofit.create(APIService.class);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
        String auth_key = prefs.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
        Call<RegisterResponse> call = service.getRegister(limit, offset, search.getText().toString(), auth_key);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                progressDialog.dismiss();
                try {
                    //Toast.makeText(getContext(),String.valueOf( response.body().getPageItems().size()), Toast.LENGTH_LONG).show();
                    if (String.valueOf(response.body().getPageItems().size())!="0"){
                        pageItemArrayList = (ArrayList<PageItem>) response.body().getPageItems();
                        saveArrayList(pageItemArrayList, "viewfarmer");
                        setData();
                    }else {

                        Snackbar.make(root, "No more records available", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                } catch (Exception e) {
                    ////Utility.showToast(getContext(), e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                progressDialog.hide();
                //Utility.showToast(getContext(), t.getMessage());
            }
        });
    }

    private void setData() {
        registerAdapter = new RegisterAdapter(rv_register,getContext(), pageItemArrayList);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), HORIZONTAL);
        rv_register.addItemDecoration(itemDecor);
        //rv_register.setAdapter(registerAdapter);
        rv_register.setAdapter(registerAdapter);

    }

    public void saveArrayList(ArrayList<PageItem> list, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public ArrayList<PageItem> getArrayList(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<PageItem>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

}




