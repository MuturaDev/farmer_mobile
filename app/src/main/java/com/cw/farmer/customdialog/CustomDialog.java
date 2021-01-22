package com.cw.farmer.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.farmer.R;
import com.cw.farmer.adapter.OfflineDataRecyclerAdapter;
import com.cw.farmer.model.EditContentModel;

import java.util.List;

public class CustomDialog extends Dialog {


    public CustomDialog(@NonNull Context context) {
        super(context);
    }

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_content_layout);

        recyclerView = findViewById(R.id.edit_recycler);


    }






}
