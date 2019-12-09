package com.cw.farmer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.farmer.OnLoadMoreListener;
import com.cw.farmer.R;
import com.cw.farmer.activity.SearchContractFarmerActivity;
import com.cw.farmer.activity.SearchFarmerActivity;
import com.cw.farmer.model.SearchContractPageItem;

import java.util.ArrayList;

public class SearchContractAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<SearchContractPageItem> pageItemArrayList;
    Context context;
    int length;
    int index = 0;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    public SearchContractAdapter(RecyclerView recyclerView, Context context, ArrayList<SearchContractPageItem> pageItemArrayList) {
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

    public void setdata(ArrayList<SearchContractPageItem> feedDataArrayList) {
        this.pageItemArrayList = feedDataArrayList;

        this.notifyDataSetChanged();
    }


    public void setLoaded() {
        isLoading = false;
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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_contractsearch, viewGroup, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_loading, viewGroup, false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolder) {
            populateItemRows((ViewHolder) viewHolder, i);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, i);
        }

    }


    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ViewHolder viewHolder, int position) {
        final SearchContractPageItem pageItem = pageItemArrayList.get(position);
        String date="";
        for(int elem : pageItem.getCropDate()){
            date=elem+"-"+date;
        }
        viewHolder.tv_first_midel_last_name.setText(pageItem.getFarmerName());
        viewHolder.tv_mobile.setText("Farmer Code: "+pageItem.getFarmerCode());
        viewHolder.tv_idno.setText("Crop Date: "+removeLastChar(date));
        viewHolder.tv_noofunits.setText("No of units: "+pageItem.getTotalUnits());

        viewHolder.lin_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date="";
                for(int elem : pageItem.getCropDate()){
                    date=elem+"/"+date;
                }
                Intent intent = new Intent(context, SearchContractFarmerActivity.class);
                intent.putExtra("name",pageItem.getFarmerName());
                intent.putExtra("id",pageItem.getFarmerId()+"");
                intent.putExtra("crop_date",removeLastChar(date)+"");
                intent.putExtra("noofunits",pageItem.getTotalUnits()+"");
                intent.putExtra("plantingid",pageItem.getPlantingId()+"");
                intent.putExtra("recruitid",pageItem.getRecruitId()+"");
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
        protected TextView tv_first_midel_last_name, tv_mobile,tv_idno,tv_noofunits;
        protected LinearLayout lin_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_first_midel_last_name = itemView.findViewById(R.id.tv_first_midel_last_name);
            tv_mobile = itemView.findViewById(R.id.tv_mobile);
            lin_item = itemView.findViewById(R.id.lin_item);
            tv_idno=itemView.findViewById(R.id.tv_idno);
            tv_noofunits=itemView.findViewById(R.id.tv_noofunits);
        }
    }
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
}
