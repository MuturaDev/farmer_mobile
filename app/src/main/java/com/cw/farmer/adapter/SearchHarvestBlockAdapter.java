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
import com.cw.farmer.activity.HarvestBlockActivity;
import com.cw.farmer.activity.HarvestingActivity;

import com.cw.farmer.activity.SearchHarvestBlockActivity;
import com.cw.farmer.model.PageItemHarvestBlocks;

import java.util.ArrayList;

public class SearchHarvestBlockAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<PageItemHarvestBlocks> pageItemArrayList;
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

    public SearchHarvestBlockAdapter(RecyclerView recyclerView, Context context, ArrayList<PageItemHarvestBlocks> pageItemArrayList) {
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

    public void setdata(ArrayList<PageItemHarvestBlocks> feedDataArrayList) {
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
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_plant_block_item_layout, viewGroup, false);
            return new SearchHarvestBlockAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_loading, viewGroup, false);
            return new SearchHarvestBlockAdapter.LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof SearchHarvestBlockAdapter.ViewHolder) {
            populateItemRows((SearchHarvestBlockAdapter.ViewHolder) viewHolder, i);
        } else if (viewHolder instanceof SearchHarvestBlockAdapter.LoadingViewHolder) {
            showLoadingView((SearchHarvestBlockAdapter.LoadingViewHolder) viewHolder, i);
        }

    }


    private void showLoadingView(SearchHarvestBlockAdapter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(SearchHarvestBlockAdapter.ViewHolder viewHolder, int position) {
        final PageItemHarvestBlocks pageItem = pageItemArrayList.get(position);

        //does not have crop date, but when the block was created.
//        String date="";
//        for(int elem : pageItem.getCropDate()){
//            date=elem+"/"+date;
//        }
//        String contractdate="";
//        for(int elem : pageItem.getCropDate()){
//            contractdate=elem+"/"+contractdate;
//        }

        viewHolder.tv_area.setText(pageItem.getBlockName());
                viewHolder.tv_crop_date.setText("");
                viewHolder.tv_product.setText("");
                viewHolder.tv_farm.setText("");




        viewHolder.lin_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String date="";
//                for(int elem : pageItem.getCropDate()){
//                    date=elem+"/"+date;
//                }
//                String finalDate = date;
                ////does not have crop date, but when the block was created.
                Intent intent = new Intent(context, HarvestBlockActivity.class);
                intent.putExtra("Message", pageItem);
//                intent.putExtra("name",pageItem.getFamerName());
//                intent.putExtra("farmerId",pageItem.getFarmerId()+"");
//                intent.putExtra("id",pageItem.getId()+"");
//                intent.putExtra("farmerCode",pageItem.getFarmerCode()+"");
//                intent.putExtra("crop_date",removeLastChar(finalDate)+"");
//                intent.putExtra("totalUnits",pageItem.getUnits()+"");
//                intent.putExtra("plantingId",pageItem.getCropDateId()+"");
//                intent.putExtra("idno",pageItem.getAccountNumber()+"");
//                intent.putExtra("centreName",pageItem.getCentrename()+"");
//                intent.putExtra("mobileno",pageItem.getMobileno()+"");
                context.startActivity(intent);
                ((SearchHarvestBlockActivity)context).finish();
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
        protected TextView tv_area,tv_crop_date,tv_product,tv_farm;
        protected LinearLayout lin_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_area = itemView.findViewById(R.id.tv_area);
            tv_crop_date = itemView.findViewById(R.id.tv_crop_date);
            lin_item = itemView.findViewById(R.id.lin_item);
            tv_product=itemView.findViewById(R.id.tv_product);
            tv_farm=itemView.findViewById(R.id.tv_farm);

        }
    }
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
}
