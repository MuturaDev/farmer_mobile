package com.cw.farmer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.cw.farmer.HandleConnectionAppCompatActivity;
import com.cw.farmer.R;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.FarmerAccountsResponse;
import com.cw.farmer.model.PageItem;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.cw.farmer.table_models.FarmerAccountTB;

import org.imaginativeworld.oopsnointernet.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.NoInternetDialog;
import org.imaginativeworld.oopsnointernet.NoInternetSnackbar;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FarmerAccountsActivity extends HandleConnectionAppCompatActivity {
    TextView bankName,accountno;
    ImageView imageView;
    PageItem pageItem;
    Integer id;
    String farmer_id, account_name, bank_name, account_image, entry_id, status_farmer, dob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_accounts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bankName = findViewById(R.id.bankName);
        accountno = findViewById(R.id.accountno);
        imageView = findViewById(R.id.imageView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            id = (Integer) b.get("id");
            status_farmer = (String) b.get("status_farmer");
            dob = (String) b.get("dob");
            farmer_id = id + "";
            pageItem = b.getParcelable("item");
            if (Utility.isNetworkAvailable(this)) {
                SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Fetching farmer bank account details..");
                pDialog.setCancelable(false);
                pDialog.show();
                Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                APIService service = retrofit.create(APIService.class);
                SharedPreferences prefs = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                String auth_key = prefs.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                Call<List<FarmerAccountsResponse>> call = service.getfameraccount(id, auth_key);
                call.enqueue(new Callback<List<FarmerAccountsResponse>>() {
                    @Override
                    public void onResponse(Call<List<FarmerAccountsResponse>> call, Response<List<FarmerAccountsResponse>> response) {
                        for (FarmerAccountsResponse doc : response.body()) {


                            Log.w("myApp", response.message());
                            pDialog.dismissWithAnimation();

                            bankName.setText("Bank Name: " + doc.getBankName());
                            accountno.setText("Account Number: " + doc.getAccountno());
                            account_name = doc.getAccountno();
                            bank_name = doc.getBankName();
                            entry_id = doc.getId() + "";

                            String imageBytes = doc.getBase64ImageData().replace("data:image/png;base64,", "");
                            account_image = imageBytes;
                            byte[] imageByteArray = Base64.decode(imageBytes, Base64.DEFAULT);
                            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                            imageView.setAdjustViewBounds(true);
                            Glide.with(FarmerAccountsActivity.this).asBitmap().load(imageByteArray).into(imageView);
                        }

                    }

                    @Override
                    public void onFailure(Call<List<FarmerAccountsResponse>> call, Throwable t) {
                        pDialog.dismissWithAnimation();
                        Log.w("myApp", t.toString());
                        Toast.makeText(FarmerAccountsActivity.this, t.toString(), Toast.LENGTH_LONG).show();

                    }
                });
            }else{

                for(FarmerAccountTB account : FarmerAccountTB.listAll(FarmerAccountTB.class)){
                    bankName.setText("Bank Name: " + account.getBankName());
                    accountno.setText("Account Number: " + account.getAccountName());
                    account_name = account.getAccountNo();
                    bank_name = account.getBankName();
                    entry_id = account.getId() + "";

                    String imageBytes = account.getImageBytes();
                    account_image = imageBytes;
                    byte[] imageByteArray = Base64.decode(imageBytes, Base64.DEFAULT);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    imageView.setAdjustViewBounds(true);
                    Glide.with(FarmerAccountsActivity.this).asBitmap().load(imageByteArray).into(imageView);
                }


            }
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void opendoc(View v) {
//        Intent intent = new Intent(FarmerAccountsActivity.this, UpdateBankActivity.class);
//        intent.putExtra("id", pageItem.getId() + "");
//        intent.putExtra("entry_id", entry_id + "");
//        intent.putExtra("account_name", account_name);
//        intent.putExtra("bank_name", bank_name);
        //TODO:ERROR: HERE IS THE ISSUE
//        intent.putExtra("account_image", account_image);
//        intent.putExtra("status_farmer", status_farmer);
//        //intent.putExtra("dob", dob);
//       // Bundle bundle1 = new Bundle();
//       // bundle1.putParcelable("item", pageItem);
//       // intent.putExtras(bundle1);
//        startActivity(intent);
    }

}