package com.cw.farmer.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.cw.farmer.HandleConnectionAppCompatActivity;
import com.cw.farmer.R;

public class IrrigateBlockActivity extends HandleConnectionAppCompatActivity {

    private EditText txt_irrigation_hours,txt_cubic_litres;
    private TextView select_block;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irrigate_block_layout);


        enableLocationTracking();

        txt_irrigation_hours = findViewById(R.id.txt_irrigation_hours);
        //txt_irrigation_hours.setText(locationText == null? "Location not Found" : locationText);

        txt_cubic_litres = findViewById(R.id.txt_cubic_litres);
        select_block = findViewById(R.id.select_block);

//        CheckNetworkConnectionHelper
//                .getInstance()
//                .registerNetworkChangeListener(new StopReceiveDisconnectedListener() {
//                    @Override
//                    public void onDisconnected() {
//                        //Do your task on Network Disconnected!
//                        Snackbar.make(findViewById(R.id.irrigate_block_layout), "Sorry, not internet connection", Snackbar.LENGTH_LONG).show();
//
//                    }
//
//                    @Override
//                    public void onNetworkConnected() {
//                        //Do your task on Network Connected!
//                        Snackbar.make(findViewById(R.id.irrigate_block_layout), "Internet Connection on", Snackbar.LENGTH_LONG).show();
//
//                    }
//
//                    @Override
//                    public Context getContext() {
//                        return IrrigateBlockActivity.this;
//                    }
//                });



    }

    public boolean validate() {
        boolean valid = true;
        int errorColor;
        final int version = Build.VERSION.SDK_INT;

        //Get the defined errorColor from color resource.
        if (version >= 23) {
            errorColor = ContextCompat.getColor(getApplicationContext(), R.color.errorColor);
        } else {
            errorColor = getResources().getColor(R.color.errorColor);
        }

        if (txt_irrigation_hours.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("Please Input a value in Irrigation Hours Field");
            ssbuilder.setSpan(fgcspan, 0, "Please Input a value in Irrigation Hours Field".length(), 0);
            txt_irrigation_hours.setError(ssbuilder);
            valid = false;
        } else {
            txt_irrigation_hours.setError(null);
        }

        if (txt_cubic_litres.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("Please Input a value in Cubic Litres Field");
            ssbuilder.setSpan(fgcspan, 0, "Please Input a value in Cubic Litres Field".length(), 0);
            txt_cubic_litres.setError(ssbuilder);
            valid = false;
        } else {
            txt_cubic_litres.setError(null);
        }




        if( select_block.getText().toString().isEmpty()){
            select_block.setBackground(getResources().getDrawable(R.drawable.shake_search_bg));
            select_block.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            valid = false;
        }else{
            select_block.setBackground(getResources().getDrawable(R.drawable.search_bg));
        }




        return valid;
    }




    public void searchBlock(View view){

    }



    public void submit(View view){
        //txt_irrigation_hours.setText(locationText == null? "Location not Found" : locationText);
        if(locationText == null){
            getLocation();
            return;
        }



        if(validate()){
            Toast.makeText(this, "Everything is okay", Toast.LENGTH_SHORT).show();
        }

    }




}
