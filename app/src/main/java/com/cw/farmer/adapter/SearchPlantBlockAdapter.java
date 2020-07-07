package com.cw.farmer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.farmer.OnLoadMoreListener;
import com.cw.farmer.R;
import com.cw.farmer.activity.ApplyFertilizerBlockActivity;
import com.cw.farmer.activity.HarvestBlockActivity;
import com.cw.farmer.activity.IrrigateBlockActivity;
import com.cw.farmer.activity.PlantBlockActivity;
import com.cw.farmer.activity.ScoutingBlockActivity;
import com.cw.farmer.activity.SearchPlantBlockActivity;
import com.cw.farmer.model.PageItemPlantBlock;

import java.util.ArrayList;
import java.util.List;

public class SearchPlantBlockAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<PageItemPlantBlock> pageItemArrayList;
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

    public SearchPlantBlockAdapter(RecyclerView recyclerView, Context context, ArrayList<PageItemPlantBlock> pageItemArrayList) {
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

    public void setdata(ArrayList<PageItemPlantBlock> feedDataArrayList) {
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
            return new SearchPlantBlockAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_loading, viewGroup, false);
            return new SearchPlantBlockAdapter.LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof SearchPlantBlockAdapter.ViewHolder) {
            populateItemRows((SearchPlantBlockAdapter.ViewHolder) viewHolder, i);
        } else if (viewHolder instanceof SearchPlantBlockAdapter.LoadingViewHolder) {
            showLoadingView((SearchPlantBlockAdapter.LoadingViewHolder) viewHolder, i);
        }

    }


    private void showLoadingView(SearchPlantBlockAdapter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }
    Intent intent = null;
    private void populateItemRows(SearchPlantBlockAdapter.ViewHolder viewHolder, int position) {
        final PageItemPlantBlock pageItem = pageItemArrayList.get(position);

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
        //if(pageItem.getCropDate().contains(",")) {
            List<Integer> date = pageItem.getCropDate();
            viewHolder.tv_crop_date.setText(date.get(2) + "/"  +date.get(1) + "/" + date.get(0));
       // }
        viewHolder.tv_product.setText(pageItem.getProduct());
        viewHolder.tv_farm.setText(pageItem.getFarmName());

        String searchFor = ((SearchPlantBlockActivity)context).searchFor;


        if(searchFor != null) {
            if (!searchFor.isEmpty()) {

                if (searchFor.equalsIgnoreCase("Irrigate")) {
                     intent = new Intent(context, IrrigateBlockActivity.class);
                    intent.putExtra("Message", pageItem);
                    viewHolder.item_image.setImageResource(R.drawable.irrigate);
                } else if (searchFor.equalsIgnoreCase("Scouting")) {
                     intent = new Intent(context, ScoutingBlockActivity.class);
                    intent.putExtra("Message", pageItem);
                    viewHolder.item_image.setImageResource(R.drawable.monitor);
                } else if (searchFor.equalsIgnoreCase("ApplyFertilizer")) {
                     intent = new Intent(context, ApplyFertilizerBlockActivity.class);
                    intent.putExtra("Message", pageItem);
                    viewHolder.item_image.setImageResource(R.drawable.fertilizer);
                }else if(searchFor.equalsIgnoreCase("Harvest")){
                     intent = new Intent(context, HarvestBlockActivity.class);
                    intent.putExtra("Message", pageItem);
                    viewHolder.item_image.setImageResource(R.drawable.harvest_block);
                }
            }
        }




        viewHolder.lin_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String date="";
//                for(int elem : pageItem.getCropDate()){
//                    date=elem+"/"+date;
//                }
//                String finalDate = date;
                ////does not have crop date, but when the block was created.
                if(intent != null)
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
        protected TextView tv_area,tv_crop_date,tv_product,tv_farm;
        protected LinearLayout lin_item;
        protected ImageView item_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_area = itemView.findViewById(R.id.tv_area);
            tv_crop_date = itemView.findViewById(R.id.tv_crop_date);
            lin_item = itemView.findViewById(R.id.lin_item);
            tv_product=itemView.findViewById(R.id.tv_product);
            tv_farm=itemView.findViewById(R.id.tv_farm);
            item_image = itemView.findViewById(R.id.item_image);

        }
    }
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
}
