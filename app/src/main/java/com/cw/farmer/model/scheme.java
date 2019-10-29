package com.cw.farmer.model;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.farmer.OnLoadMoreListener;
import com.cw.farmer.R;
import com.cw.farmer.activity.MainActivity;
import com.cw.farmer.adapter.RegisterAdapter;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
        if (Utility.isNetworkAvailable(getActivity())) {
            getData();
        }

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
                rv_register.setLayoutManager(new LinearLayoutManager(getActivity()));
                pageItemArrayList.clear();
                registerAdapter.notifyDataSetChanged();
                progressDialog.setCancelable(false);
                // progressBar.setMessage("Please Wait...");
                progressDialog.show();
                Retrofit retrofit = ApiClient.getClient("/authentication/", getContext());
                APIService service = retrofit.create(APIService.class);
                Call<RegisterResponse> call = service.getRegister(0,0,search.getText().toString());
                call.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        progressDialog.hide();
                        try {
                            if (String.valueOf(response.body().getPageItems().size())!="0"){
//                                    if (pageItemArrayList==null){
//                                        pageItemArrayList = response.body().getPageItems();
//                                    }else{
//                                        pageItemArrayList.removeAll();
//                                        pageItemArrayList .addAll(response.body().getPageItems());
//                                    }

                                pageItemArrayList .addAll(response.body().getPageItems());
                                registerAdapter.notifyItemRangeInserted(0, pageItemArrayList.size());
                                end =true;
                                setData();

                            }else {
                                if (registerAdapter!=null)
                                {
                                    registerAdapter.setLoaded();
                                }
                                end =true;
                                Snackbar.make(root, "Search Not Found", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                        } catch (Exception e) {
                            Utility.showToast(getContext(), e.getMessage());
                        }

                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        progressDialog.hide();
                        Utility.showToast(getContext(), t.getMessage());
                    }
                });
            }
        });

        return root;

    }

    private void getData() {
        //progressDialog.setCancelable(false);
        // progressBar.setMessage("Please Wait...");
        //progressDialog.show();
        Retrofit retrofit = ApiClient.getClient("/authentication/", getContext());
        APIService service = retrofit.create(APIService.class);
        Call<RegisterResponse> call = service.getRegister(limit,offset,null);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                //progressDialog.hide();
                try {
                    //Toast.makeText(getContext(),String.valueOf( response.body().getPageItems().size()), Toast.LENGTH_LONG).show();
                    if (String.valueOf(response.body().getPageItems().size())!="0"){
                        if (pageItemArrayList==null){
                            pageItemArrayList = (ArrayList<PageItem>) response.body().getPageItems();
                        }else{
                            pageItemArrayList .addAll(response.body().getPageItems());
                        }
                        setData();
                    }else {
                        if (registerAdapter!=null)
                        {
                            registerAdapter.setLoaded();
                        }
                        end =true;
                        Snackbar.make(root, "No more records available", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                } catch (Exception e) {
                    Utility.showToast(getContext(), e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                progressDialog.hide();
                Utility.showToast(getContext(), t.getMessage());
            }
        });
    }

    private void setData() {
        registerAdapter = new RegisterAdapter(rv_register,getContext(), pageItemArrayList);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), HORIZONTAL);
        rv_register.addItemDecoration(itemDecor);
        //rv_register.setAdapter(registerAdapter);
        if (offset == 0) {
            rv_register.setAdapter(registerAdapter);
        }else{
            registerAdapter.setdata(pageItemArrayList);
            rv_register.scrollToPosition(pageItemArrayList.size());
        }
        registerAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pageItemArrayList.add(null);
                registerAdapter.notifyItemInserted(pageItemArrayList.size() - 1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageItemArrayList.remove(pageItemArrayList.size() - 1);
                        registerAdapter.notifyItemRemoved(pageItemArrayList.size());
                        //  rv_feed.scrollToPosition(feedDataArrayList.size()-1);
                        //Generating more data
                        int index = pageItemArrayList.size();
                        offset = offset + limit;
                        if (!end)
                        {
                            getData();
                        }
                        //feedAdapter.notifyDataSetChanged();
                    }
                }, 1000);

            }
        });

    }

}
