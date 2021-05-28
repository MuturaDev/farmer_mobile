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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.cw.farmer.HandleConnectionAppCompatActivity;
import com.cw.farmer.R;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.FarmerDocResponse;
import com.cw.farmer.model.PageItem;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.cw.farmer.table_models.FarmerDocument;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FarmerDocumentsActivity extends HandleConnectionAppCompatActivity {
    TextView docshtdesc,docno;
    ImageView imageView;
    PageItem pageItem;
    String farmer_id, id_no, id_image, entry_id, status_farmer, dob;

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
            status_farmer = (String) b.get("status_farmer");
            dob = (String) b.get("dob");
            farmer_id = id + "";
            pageItem = b.getParcelable("item");
            if (Utility.isNetworkAvailable(this)) {
                SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Fetching farmer details..");
                pDialog.setCancelable(false);
                pDialog.show();
                Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                APIService service = retrofit.create(APIService.class);
                SharedPreferences prefs = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                String auth_key = prefs.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                Call<List<FarmerDocResponse>> call = service.getfamerdocs(id, auth_key);
                call.enqueue(new Callback<List<FarmerDocResponse>>() {
                    @Override
                    public void onResponse(Call<List<FarmerDocResponse>> call, Response<List<FarmerDocResponse>> response) {
                        if (response.body() != null) {
                            for (FarmerDocResponse doc : response.body()) {


                                Log.w("myApp", response.message());
                                pDialog.dismissWithAnimation();

                                docshtdesc.setText("Document Type: " + doc.getDocshtdesc());
                                docno.setText("Document No: " + doc.getDocno());
                                id_no = doc.getDocno();
                                entry_id = doc.getId() + "";


                                String imageBytes = doc.getBase64ImageDesc().replace("data:image/png;base64,", "");
                                id_image = imageBytes;
                                byte[] imageByteArray = Base64.decode(imageBytes, Base64.DEFAULT);
                                Glide.with(FarmerDocumentsActivity.this).asBitmap().load(imageByteArray).into(imageView);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<FarmerDocResponse>> call, Throwable t) {
                        pDialog.dismissWithAnimation();
                        Log.w("myApp", t.toString());
                        Toast.makeText(FarmerDocumentsActivity.this, t.toString(), Toast.LENGTH_LONG).show();

                    }
                });
            }else{
                List<FarmerDocument> list = FarmerDocument.listAll(FarmerDocument.class);
                for(FarmerDocument doc : list){
                    docshtdesc.setText("Document Type: " + doc.getDocShortDescription());
                    docno.setText("Document No: " + doc.getDocNumber());
                    id_no = doc.getDocNumber();
                    entry_id = doc.getId() + "";


                    String imageBytes = doc.getImageBytes();
                    id_image = imageBytes;
                    byte[] imageByteArray = Base64.decode(imageBytes, Base64.DEFAULT);
                    Glide.with(FarmerDocumentsActivity.this).asBitmap().load(imageByteArray).into(imageView);
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


    static Map<String,String> pageItemObject;



    //http://frigo@w/7
    public void opendoc(View v) {
        pageItemObject = new HashMap<>();
        pageItemObject.put("id", pageItem.getId() + "");
        pageItemObject.put("entry_id", entry_id + "");
        pageItemObject.put("id_no", id_no);
        pageItemObject.put("id_image", id_image);
        pageItemObject.put("status_farmer", status_farmer);
        pageItemObject.put("dob", dob);

        Intent intent = new Intent(FarmerDocumentsActivity.this, UpdateDocsActivity.class);
        Bundle bundle1 = new Bundle();
        bundle1.putParcelable("item", pageItem);
        intent.putExtras(bundle1);
        startActivity(intent);
    }

}
