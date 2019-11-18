package com.cw.farmer.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.farmer.OnLoadMoreListener;
import com.cw.farmer.R;
import com.cw.farmer.model.RequisitionResponse;
import com.cw.farmer.model.RetItem;

import java.util.ArrayList;
import java.util.List;

public class RequistionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<RequisitionResponse> pageItemArrayList;
    Context context;
    int length;
    int index = 0;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private List<RetItem> _retData;


    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    public RequistionAdapter(RecyclerView recyclerView, Context context, ArrayList<RequisitionResponse> pageItemArrayList) {
        this.context = context;
        this.pageItemArrayList = pageItemArrayList;
        _retData = new ArrayList<>(pageItemArrayList.size());
        for (int i = 0; i < pageItemArrayList.size(); ++i) {
            _retData.add(new RetItem());
        }
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

    public void setdata(ArrayList<RequisitionResponse> feedDataArrayList) {
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
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_requisition, viewGroup, false);
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
        final RequisitionResponse pageItem = pageItemArrayList.get(position);
        viewHolder.tv_itemcode.setText(pageItem.getInvId()+"");
        _retData.get(position).invid = pageItem.getInvId() + "";
        viewHolder.tv_itemname.setText(pageItem.getInvitemdesc()+"");
        viewHolder.tv_quanity.setText(pageItem.getQuantity()+"");
        viewHolder.tv_actual.setText(pageItem.getQuantity() + "");
        _retData.get(position).qty = pageItem.getQuantity() + "";
        _retData.get(position).centreid = pageItem.getCentreid() + "";
        _retData.get(position).reqid = pageItem.getReqid() + "";
        _retData.get(position).type = pageItem.getReqid() + "";
        viewHolder.tv_actual.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                _retData.get(position).qty = s.toString();
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

    public List<RetItem> retrieveData() {
        return _retData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_itemcode, tv_itemname, tv_quanity;
        protected LinearLayout lin_item;
        protected EditText tv_actual;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_itemcode = itemView.findViewById(R.id.tv_itemcode);
            tv_itemname = itemView.findViewById(R.id.tv_itemname);
            lin_item = itemView.findViewById(R.id.lin_item);
            tv_quanity=itemView.findViewById(R.id.tv_quanity);
            tv_actual=itemView.findViewById(R.id.tv_actual);
        }
    }
}
