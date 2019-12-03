package com.cw.farmer.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.cw.farmer.R;
import com.cw.farmer.model.BlacklistPostResponse;
import com.cw.farmer.model.BlacklistResponse;
import com.cw.farmer.model.PageItem;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BlacklistActivity extends AppCompatActivity {
    Button btn_blacklist;
    Spinner blacklist_reasons;
    List<Integer> blacklist_id;
    EditText editTextDateto,editTextDatefrom;
    DatePickerDialog picker,picker1;
    PageItem pageItem;
    TextView blacklist_text;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist);
        Bundle bundle = getIntent().getExtras();
        progressDialog = new ProgressDialog(this);
        pageItem = bundle.getParcelable("item");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btn_blacklist=findViewById(R.id.btn_blacklist);
        blacklist_text=findViewById(R.id.blacklist_text);
        blacklist_text.setText("Blacklisting "+ pageItem.getFirstname());
        blacklist_reasons= findViewById(R.id.blacklist_reasons);
        editTextDateto= findViewById(R.id.editTextDateto);
        editTextDateto.setInputType(InputType.TYPE_NULL);
        editTextDateto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(BlacklistActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String mDate=convertDate(convertToMillis(dayOfMonth,monthOfYear,year));
                                editTextDateto.setText(mDate);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        editTextDatefrom= findViewById(R.id.editTextDatefrom);
        editTextDatefrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker1 = new DatePickerDialog(BlacklistActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String mDate=convertDate(convertToMillis(dayOfMonth,monthOfYear,year));
                                editTextDatefrom.setText(mDate);
                            }
                        }, year, month, day);
                picker1.show();
            }
        });
        btn_blacklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (blacklist_reasons == null && blacklist_reasons.getSelectedItem() ==null) {
                    //Get the defined errorColor from color resource.
                    final int errorColor;
                    final int version = Build.VERSION.SDK_INT;
                    if (version >= 23) {
                        errorColor = ContextCompat.getColor(getApplicationContext(), R.color.errorColor);
                    } else {
                        errorColor = getResources().getColor(R.color.errorColor);
                    }

                    TextView errorText = (TextView)blacklist_reasons.getSelectedView();
                    errorText.setError("must input blacklist reason");
                    errorText.setTextColor(errorColor);
                }else{
                    blacklist();
                }

            }
        });
        Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
        APIService service = retrofit.create(APIService.class);
        SharedPreferences prefs = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
        String auth_key = prefs.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
        Call<List<BlacklistResponse>> call = service.getblacklistreasons(auth_key);
        call.enqueue(new Callback<List<BlacklistResponse>>() {
            @Override
            public void onResponse(Call<List<BlacklistResponse>> call, Response<List<BlacklistResponse>> response) {
                ArrayList<String> spinnerArray = new ArrayList<String>();
                spinnerArray.clear();
                blacklist_id = new ArrayList<Integer>();
                for(BlacklistResponse blacklist: response.body()) {

                    spinnerArray.add(blacklist.getReasonId());
                    blacklist_id.add(blacklist.getId());
                }
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(BlacklistActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                blacklist_reasons.setAdapter(spinnerArrayAdapter);
            }

            @Override
            public void onFailure(Call<List<BlacklistResponse>> call, Throwable t) {
                Toast.makeText(BlacklistActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });



    }
    public void blacklist() {
        progressDialog.setCancelable(false);
        // progressBar.setMessage("Please Wait...");
        progressDialog.show();
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("wef",editTextDatefrom.getText().toString());
        hashMap.put("wet",editTextDateto.getText().toString());
        hashMap.put("locale","en");
        hashMap.put("dateFormat","dd-MMM-yyyy");
        hashMap.put("reasonId",blacklist_id.get(blacklist_reasons.getSelectedItemPosition()).toString());
        Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
        APIService service = retrofit.create(APIService.class);
        SharedPreferences prefs = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
        String auth_key = prefs.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
        Call<BlacklistPostResponse> call = service.createblacklist(auth_key, pageItem.getId(), "blacklist", hashMap);
        call.enqueue(new Callback<BlacklistPostResponse>() {
            @Override
            public void onResponse(Call<BlacklistPostResponse> call, Response<BlacklistPostResponse> response) {
                progressDialog.hide();
                Toast.makeText(BlacklistActivity.this, "You have successfully blacklisted "+pageItem.getFirstname()+" because of "+blacklist_reasons.getSelectedItem(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(BlacklistActivity.this, FarmerDetailsActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable("item", pageItem);
                intent.putExtras(bundle1);
                intent.putExtra("id",pageItem.getId());
                startActivity(intent);
                finish();

            }

            @Override
            public void onFailure(Call<BlacklistPostResponse> call, Throwable t) {
                progressDialog.hide();
                Toast.makeText(BlacklistActivity.this, t.toString(), Toast.LENGTH_LONG).show();

            }
        });
    }
    public String convertDate(long mTime) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(mTime);
        return formattedDate;
    }

    public long convertToMillis(int day, int month, int year) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(Calendar.YEAR, year);
        calendarStart.set(Calendar.MONTH, month);
        calendarStart.set(Calendar.DAY_OF_MONTH, day);
        return calendarStart.getTimeInMillis();
    }

}
