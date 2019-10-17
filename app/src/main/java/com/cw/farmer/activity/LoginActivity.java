package com.cw.farmer.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cw.farmer.R;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.CropDateResponse;
import com.cw.farmer.model.Result;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;

import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_username, et_password;
    private Button btn_login;
    private ProgressDialog progressBar;
    private TextView errorview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
            if (isValid()){
                if (Utility.isNetworkAvailable(this)){
                    login();
                }
            }
                break;
        }
    }

    private void login() {

        progressBar.setCancelable(false);
       // progressBar.setMessage("Please Wait...");
        progressBar.show();
        Retrofit retrofit = ApiClient.getClient("/authentication/");
        APIService service =retrofit.create(APIService.class);
        String username=et_username.getText().toString();
        String password =et_password.getText().toString();
        Call<Result> call = service.userLogin(username,password);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressBar.hide();

                try {
                    if (response.body().getBase64EncodedAuthenticationKey() != null){
                        //Utility.showToast(LoginActivity.this,"success");
                        //Utility.showToast(LoginActivity.this,response.body().getPermissions());

                        //Set the values
                        SharedPreferences mEdit1 = getSharedPreferences("PERMISSIONS", Context.MODE_PRIVATE);
                        SharedPreferences.Editor scoreEditor = mEdit1.edit();
                        scoreEditor.putString("userid",response.body().getCentreId()+"" );
                        Set<String> set = new HashSet<String>();
                        set.addAll(response.body().getPermissions());
                        scoreEditor.putStringSet("key", set);
                        scoreEditor.commit();

                        startActivity(new Intent(LoginActivity.this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    }
                }catch (Exception e){
                    errorview.setText("Wrong Username or Password");
                    //Utility.showToast(LoginActivity.this,e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressBar.hide();
                Utility.showToast(LoginActivity.this,t.getMessage());
            }
        });


    }

    private boolean isValid() {
        if (et_username.getText().length()==0||et_username.getText().toString().equals("")||et_username.getText().equals(null)){
            et_username.setError("Can Not Empty");
            return false;
        }else if(et_password.getText().length()==0||et_password.getText().toString().equals("")||et_password.getText().equals(null))
        {
            et_password.setError("Can Not Empty");
            return false;
        }
        return true;
    }
}
