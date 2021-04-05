package com.cw.farmer.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cw.farmer.HandleConnectionAppCompatActivity;
import com.cw.farmer.R;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.CentreId;
import com.cw.farmer.model.Result;
import com.cw.farmer.offlinefunctions.OfflineFeature;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LoginActivity extends HandleConnectionAppCompatActivity implements View.OnClickListener {
    private EditText et_username, et_password;
    private ImageButton btn_login;
    private ProgressDialog progressBar;
    private TextView errorview;

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    private void init() {
        progressBar =new ProgressDialog(this);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        errorview=findViewById(R.id.login_error_view);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
            if (isValid()){
                login(view);

            }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

    }

    private boolean isValid() {
        if (et_username.getText().length() == 0 || et_username.getText().toString().equals("") || et_username.getText().equals(null)) {
            et_username.setError("Field cannot be blank");
            return false;
        } else if (et_password.getText().length() == 0 || et_password.getText().toString().equals("") || et_password.getText().equals(null)) {
            et_password.setError("Field cannot be blank");
            return false;
        }
        return true;
    }



    private void login(View view) {

        //close keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        progressBar.setCancelable(false);
        progressBar.setMessage("Please Wait...");
        progressBar.show();
        SharedPreferences prefs = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
        if (prefs.getString("username", "9990988").equals(et_username.getText().toString()) &&
                prefs.getString("password", "-2000099").equals(et_password.getText().toString())) {

            startActivity(new Intent(LoginActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

        }

        Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
        APIService service =retrofit.create(APIService.class);
        String username=et_username.getText().toString();
        String password =et_password.getText().toString();
        Call<Result> call = service.userLogin(username,password);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressBar.dismiss();

                try {
                    if (response.code() == 200) {

                        //Utility.showToast(LoginActivity.this,response.body().getPermissions());

                        //Set the values
                        String center_idss = "";
                        String center_names = "";
                        for (CentreId elem : response.body().getCentreId()) {
                            center_idss = elem.getCenterId() + "," + center_idss;
                            center_names = elem.getCentreName() + "," + center_names;
                        }
                        center_idss = removeLastChar(center_idss);
                        center_names = removeLastChar(center_names);
                        Log.d("Centers",center_names);
                        Log.d("Centers Ids",center_idss);
                        SharedPreferences mEdit1 = getSharedPreferences("PERMISSIONS", Context.MODE_PRIVATE);
                        SharedPreferences.Editor scoreEditor = mEdit1.edit();
                        scoreEditor.putString("userid", center_idss + "");
                        scoreEditor.putString("username", et_username.getText().toString());
                        scoreEditor.putString("password", et_password.getText().toString());
                        scoreEditor.putString("enter", "yes");
                        scoreEditor.putString("center_names", center_names);
                        scoreEditor.putString("center_ids", center_idss);
                        scoreEditor.putString("name", "" + response.body().getName());
                        scoreEditor.putString("auth_key", "Basic " + response.body().getBase64EncodedAuthenticationKey());
                        //startActivity(new Intent(LoginActivity.this, Home2Activity.class));


                        Set<String> set = new HashSet<String>();
                        set.addAll(response.body().getPermissions());
                        scoreEditor.putStringSet("key", set);
                        scoreEditor.commit();

                        new OfflineFeature(0,false).silentDataDump(getApplicationContext());

                        startActivity(new Intent(LoginActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                        //Utility.showToast(LoginActivity.this,center_idss);


                    } else {
                        errorview.setText("Wrong Username or Password");
                    }
                }catch (Exception e){

                    //Utility.showToast(LoginActivity.this,e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                //progressBar.hide();
                Utility.showToast(LoginActivity.this,t.getMessage());
            }
        });


    }
}
