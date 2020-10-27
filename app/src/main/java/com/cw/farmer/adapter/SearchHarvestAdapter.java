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
import com.cw.farmer.activity.HarvestingActivity;
import com.cw.farmer.model.PageItemHarvest;

import java.util.ArrayList;

public class SearchHarvestAdapter extends RecyclerView.Adapter<SearchHarvestAdapter.ViewHolder> {
    ArrayList<PageItemHarvest> pageItemArrayList;
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

    public SearchHarvestAdapter(RecyclerView recyclerView, Context context, ArrayList<PageItemHarvest> pageItemArrayList) {
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

    public void setdata(ArrayList<PageItemHarvest> feedDataArrayList) {
        this.pageItemArrayList = feedDataArrayList;

        this.notifyDataSetChanged();
    }


    public void setLoaded() {
        isLoading = false;
    }

    @NonNull
    @Override
    public SearchHarvestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_harvesting, viewGroup, false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHarvestAdapter.ViewHolder viewHolder, int i) {

       if(viewHolder != null)
           populateItemRows(viewHolder, i);
    }




    private void populateItemRows(@NonNull SearchHarvestAdapter.ViewHolder viewHolder, int position) {
        final PageItemHarvest pageItem = pageItemArrayList.get(position);
        String date="";
        for(int elem : pageItem.getCropDate()){
            date=elem+"/"+date;
        }
        String contractdate="";
        for(int elem : pageItem.getCropDate()){
            contractdate=elem+"/"+contractdate;
        }

        viewHolder.tv_first_midel_last_name.setText(pageItem.getFamerName());
       viewHolder.tv_idno.setText("ID No: "+pageItem.getIdno());
        viewHolder.crop_date.setText("Crop Date: "+removeLastChar(date));
        viewHolder.tv_mobile.setText("Phone: " + pageItem.getMobileno());

        viewHolder.tv_noofunits.setText("Centre Name: "+pageItem.getCentrename());


        viewHolder.lin_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date="";
                for(int elem : pageItem.getCropDate()){
                    date=elem+"/"+date;
                }
                String finalDate = date;
                Intent intent = new Intent(context, HarvestingActivity.class);
                intent.putExtra("name",pageItem.getFamerName());
                intent.putExtra("farmerId",pageItem.getFarmerId()+"");
                intent.putExtra("id",pageItem.getId()+"");
                intent.putExtra("farmerCode",pageItem.getFarmerCode()+"");
                intent.putExtra("crop_date",removeLastChar(finalDate)+"");
                intent.putExtra("totalUnits",pageItem.getUnits()+"");
                intent.putExtra("plantingId",pageItem.getCropDateId()+"");
                intent.putExtra("idno",pageItem.getAccountNumber()+"");
                intent.putExtra("centreName",pageItem.getCentrename()+"");
                intent.putExtra("mobileno",pageItem.getMobileno()+"");
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


    public class ViewHolder extends RecyclerView.ViewHolder {
         TextView tv_first_midel_last_name, tv_mobile,tv_idno,tv_noofunits,crop_date;
         LinearLayout lin_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_first_midel_last_name = itemView.findViewById(R.id.tv_first_midel_last_name);
            tv_mobile = itemView.findViewById(R.id.tv_mobile);
            lin_item = itemView.findViewById(R.id.lin_item);
            tv_idno=itemView.findViewById(R.id.tv_idno);
            tv_noofunits=itemView.findViewById(R.id.tv_noofunits);
            crop_date=itemView.findViewById(R.id.crop_date);
        }
    }
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
}
