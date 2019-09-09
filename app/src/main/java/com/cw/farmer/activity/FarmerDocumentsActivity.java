package com.cw.farmer.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.cw.farmer.model.BankNameResponse;
import com.cw.farmer.model.BlacklistPostResponse;
import com.cw.farmer.model.FarmerDocResponse;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cw.farmer.R;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FarmerDocumentsActivity extends AppCompatActivity {
    TextView docshtdesc,docno;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_documents);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        docshtdesc= findViewById(R.id.docshtdesc);
        docno= findViewById(R.id.docno);
        imageView= findViewById(R.id.imageView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null) {
            Integer id = (Integer) b.get("id");
            SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Fetching farmer details..");
            pDialog.setCancelable(false);
            pDialog.show();
            Retrofit retrofit = ApiClient.getClient("/authentication/");
            APIService service = retrofit.create(APIService.class);
            Call<List<FarmerDocResponse>> call = service.getfamerdocs(id);
            call.enqueue(new Callback<List<FarmerDocResponse>>() {
                @Override
                public void onResponse(Call<List<FarmerDocResponse>> call, Response<List<FarmerDocResponse>> response) {
                    for(FarmerDocResponse doc: response.body()) {


                        Log.w("myApp", response.message());
                        pDialog.dismissWithAnimation();

                        docshtdesc.setText("Document Type: "+doc.getDocshtdesc());
                        docno.setText("Document No: "+doc.getDocno());
                        String imageBytes = doc.getBase64ImageDesc().replace("data:image/png;base64,","");
                        byte[] imageByteArray = Base64.decode(imageBytes, Base64.DEFAULT);
                        Glide.with(FarmerDocumentsActivity.this).asBitmap().load(imageByteArray).into(imageView);
                    }

                }

                @Override
                public void onFailure(Call<List<FarmerDocResponse>> call, Throwable t) {
                    pDialog.dismissWithAnimation();
                    Log.w("myApp", t.toString());
                    Toast.makeText(FarmerDocumentsActivity.this, t.toString(), Toast.LENGTH_LONG).show();

                }
            });
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

}
