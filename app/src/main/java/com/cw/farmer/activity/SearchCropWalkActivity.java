package com.cw.farmer.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.farmer.HandleConnectionAppCompatActivity;
import com.cw.farmer.NetworkUtil;
import com.cw.farmer.OnLoadMoreListener;
import com.cw.farmer.R;

import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.CropWalkPageItemResponse;
import com.cw.farmer.model.SearchCropWalkResponse;
import com.cw.farmer.offlinefunctions.OfflineFeature;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

public class SearchCropWalkActivity extends HandleConnectionAppCompatActivity {

    RecyclerView rv_register;
    SearchCropWalkAdapter registerAdapter;
    ArrayList<CropWalkPageItemResponse> pageItemArrayList;
    FloatingActionButton fab;
    EditText farmer_search;
    LinearLayout btn_search;
    SharedPreferences sharedPreferences;

    private int page = 0;
    private int limit = 15;//FIXME: FOR OFFLINE MODE YOU WILL NOT HAVE ALL THE DATA
    private int offset = 0;
    private boolean end = false;

    private ImageView generalActivityImage;
    private TextView generalActivityTitle;

    private ProgressBar progressBar;

    private  void showProgress(boolean show){
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_walk_search_activity_layout);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progress);

        generalActivityImage = findViewById(R.id.general_activity_image);
        ImageView btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        generalActivityTitle = findViewById(R.id.general_activity_title);
        generalActivityImage.setImageResource(R.drawable.crop_walk);
        generalActivityTitle.setText("Search Crop Walk");

        rv_register = findViewById(R.id.search_recylerview);
        rv_register.setLayoutManager(new LinearLayoutManager(this));
        farmer_search = findViewById(R.id.farmer_search);
        btn_search = findViewById(R.id.btn_search);
        //prevents fetching records after the page is open.. and allows fetching records only after clicking on search
        //search();

    }

    public void search() {
        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
            getData();
        } else {

            String farmerSearch = farmer_search.getText().toString();

            if(farmerSearch.isEmpty()){
                pageItemArrayList = (ArrayList<CropWalkPageItemResponse>) OfflineFeature.getSharedPreferencesObject("CropWalkSearch", getApplicationContext(), CropWalkPageItemResponse.class);
            }else{
                pageItemArrayList = new ArrayList<>();
                pageItemArrayList.clear();

                List<CropWalkPageItemResponse> list = (ArrayList<CropWalkPageItemResponse>) OfflineFeature.getSharedPreferencesObject("CropWalkSearch", getApplicationContext(), CropWalkPageItemResponse.class);
                if(list != null)
                    for(CropWalkPageItemResponse item : list){
                        if(item.getFarmerName().toLowerCase().contains(farmerSearch.toLowerCase()) || String.valueOf(item.getFarmerId()).contains(farmerSearch.toLowerCase())){
                            pageItemArrayList.add(item);
                        }
                    }
            }

            setData();
        }
    }

    private void getData() {
        showProgress(true);
        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
            APIService service = retrofit.create(APIService.class);
            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
            Call<SearchCropWalkResponse> call = service.getCropWalkSearch(auth_key,offset,limit, farmer_search.getText().toString());
            call.enqueue(new Callback<SearchCropWalkResponse>() {
                @Override
                public void onResponse(Call<SearchCropWalkResponse> call, Response<SearchCropWalkResponse> response) {
                    showProgress(false);
                    try {
                        if (response.body().getTotalFilteredRecords() > 0) {
                            pageItemArrayList = (ArrayList<CropWalkPageItemResponse>) response.body().getPageItems();
                            // saveArrayList(pageItemArrayList, "sprayfarmer");
                            setData();
                        } else {
                            Toast.makeText(SearchCropWalkActivity.this, "Data Not Found", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Utility.showToast(SearchCropWalkActivity.this, e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<SearchCropWalkResponse> call, Throwable t) {
                    showProgress(false);
                    Utility.showToast(SearchCropWalkActivity.this, t.getMessage());
                }
            });
        } else {
            pageItemArrayList = (ArrayList<CropWalkPageItemResponse>) OfflineFeature.getSharedPreferencesObject("CropWalkSearch", getApplicationContext(), CropWalkPageItemResponse.class);
            setData();
            showProgress(false);
        }

    }

    private void setData() {
        registerAdapter = new SearchCropWalkAdapter(rv_register, SearchCropWalkActivity.this, pageItemArrayList);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
        rv_register.addItemDecoration(itemDecor);
        rv_register.setAdapter(registerAdapter);

    }

    public void saveArrayList(ArrayList<SearchCropWalkAdapter> list, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

//    public ArrayList<SearchCropWalkAdapter> getArrayList(String key) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        Gson gson = new Gson();
//        String json = prefs.getString(key, null);
//        Type type = new TypeToken<ArrayList<SearchCropWalkAdapter>>() {
//        }.getType();
//        return gson.fromJson(json, type);
//    }

    public void search_button(View v) {
        search();
    }



    


    private class SearchCropWalkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;
        ArrayList<CropWalkPageItemResponse> pageItemArrayList;
        Context context;
        int length;
        int index = 0;
        private int lastVisibleItem, totalItemCount;
        private OnLoadMoreListener onLoadMoreListener;
        private boolean isLoading;
        private int visibleThreshold = 5;

        public SearchCropWalkAdapter(RecyclerView recyclerView, Context context, ArrayList<CropWalkPageItemResponse> pageItemArrayList) {
            this.context = context;
            this.pageItemArrayList = pageItemArrayList;
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            });
        }

        private  String removeLastChar(String str) {
            return str.substring(0, str.length() - 1);
        }

        public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
            this.onLoadMoreListener = mOnLoadMoreListener;
        }

        public void setdata(ArrayList<CropWalkPageItemResponse> feedDataArrayList) {
            this.pageItemArrayList = feedDataArrayList;

            this.notifyDataSetChanged();
        }
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(context).inflate(R.layout.custom_register,viewGroup,false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
//        final PageItem pageItem =pageItemArrayList.get(i);
//        viewHolder.tv_first_midel_last_name.setText(pageItem.getFirstname()+" "+pageItem.getMiddlename()+" "+pageItem.getLastname());
//        viewHolder.tv_mobile.setText(pageItem.getMobileno());
//
//        viewHolder.lin_item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent =new Intent(context, FarmerDetailsActivity.class);
//                Bundle bundle1 =new Bundle();
//                bundle1.putParcelable("item",pageItem);
//               intent.putExtras(bundle1);
//                context.startActivity(intent);
//            }
//        });
//
//    }

        public void setLoaded() {
            isLoading = false;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

            if (viewType == VIEW_TYPE_ITEM) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.crop_walk_search_item_layout, viewGroup, false);
                return new SearchCropWalkAdapter.ViewHolder(view);
            } else {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_loading, viewGroup, false);
                return new SearchCropWalkAdapter.LoadingViewHolder(view);
            }

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder instanceof SearchCropWalkAdapter.ViewHolder) {
                populateItemRows((SearchCropWalkAdapter.ViewHolder) viewHolder, i);
            } else if (viewHolder instanceof SearchCropWalkAdapter.LoadingViewHolder) {
                showLoadingView((SearchCropWalkAdapter.LoadingViewHolder) viewHolder, i);
            }

        }

        private void showLoadingView(SearchCropWalkAdapter.LoadingViewHolder viewHolder, int position) {
            //ProgressBar would be displayed

        }

        private void populateItemRows(SearchCropWalkAdapter.ViewHolder viewHolder, int position) {
            final CropWalkPageItemResponse pageItem = pageItemArrayList.get(position);
            String date = "";
            for (int elem : pageItem.getCropDate()) {
                date = elem + "/" + date;
            }
            String contractdate = "";
            for (int elem : pageItem.getCropDate()) {
                contractdate = elem + "/" + contractdate;
            }
//        Name
//        ID No
//        Crop Date
//        Centre Name
//        Contract No
//        Account No
//        Units
            
//            lblFarmerName,lblCropDate,lblPlantedUnits,lblCentre;

            viewHolder.lblFarmerName.setText(pageItem.getFarmerName());
            viewHolder.lblCropDate.setText( removeLastChar(date));
            viewHolder.lblPlantedUnits.setText(String.valueOf(pageItem.getTotalUnits()));
            viewHolder.lblCentre.setText(pageItem.getCentreName());
            //viewHolder.lblCentre.setVisibility(View.GONE);
            viewHolder.lin_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String date = "";
                    for (int elem : pageItem.getCropDate()) {
                        date = elem + "/" + date;
                    }
                    String finalDate = date;
                    Intent intent = new Intent(context, CropWalkActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("CropWalkPageItemResponse", pageItem);
                    intent.putExtra("message", b);
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return pageItemArrayList == null ? 0 : pageItemArrayList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return pageItemArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }

        private class LoadingViewHolder extends RecyclerView.ViewHolder {

            ProgressBar progressBar;

            public LoadingViewHolder(@NonNull View itemView) {
                super(itemView);
                progressBar = itemView.findViewById(R.id.progressBar);
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            protected TextView lblFarmerName,lblCropDate,lblPlantedUnits,lblCentre;
            protected LinearLayout lin_item;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                lblFarmerName = itemView.findViewById(R.id.lblFarmerName);
                lblCropDate = itemView.findViewById(R.id.lblCropDate);
                lin_item = itemView.findViewById(R.id.lin_item);
                lblPlantedUnits = itemView.findViewById(R.id.lblPlantedUnits);
                lblCentre = itemView.findViewById(R.id.lblCentre);
            }
        }
    }
}
