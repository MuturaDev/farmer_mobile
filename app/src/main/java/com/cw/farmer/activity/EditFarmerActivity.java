package com.cw.farmer.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.cw.farmer.R;
import com.cw.farmer.model.AllResponse;
import com.cw.farmer.model.PageItem;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditFarmerActivity extends AppCompatActivity {
    private EditText full_name, mobile_no, dateofbirth, et_dob, et_accountno;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private int mYear;
    private int mMonth;
    private int mDay;
    RadioButton radioMale,radioFemale;
    int farmer_id;
    PageItem pageItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_farmer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        full_name = findViewById(R.id.full_name);
        mobile_no = findViewById(R.id.mobile_no);
        dateofbirth = findViewById(R.id.dateofbirth);
        radioFemale = findViewById(R.id.radioFemale);
        radioMale = findViewById(R.id.radioMale);
        dateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });

        radioSexGroup = findViewById(R.id.radiogender);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null) {
            farmer_id = (Integer) b.get("id");
            pageItem = b.getParcelable("item");
            full_name.setText(pageItem.getFirstname()+" "+pageItem.getMiddlename()+" "+pageItem.getLastname());
            mobile_no.setText(pageItem.getMobileno());

            dateofbirth.setText((String) b.get("dob")+"");
            if (pageItem.getGender().equals("M")){
                radioMale.setChecked(true);
            }else{
                radioFemale.setChecked(true);
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        if (full_name.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("must input farmer names");
            ssbuilder.setSpan(fgcspan, 0, "must input farmer names".length(), 0);
            full_name.setError(ssbuilder);
            valid = false;
        } else {
            full_name.setError(null);
        }

        if (mobile_no.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("must input farmer mobile number");
            ssbuilder.setSpan(fgcspan, 0, "must input farmer mobile number".length(), 0);
            mobile_no.setError(ssbuilder);
            valid = false;
        } else {
            mobile_no.setError(null);
        }

        if (dateofbirth.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("must input date of birth");
            ssbuilder.setSpan(fgcspan, 0, "must input date of birth".length(), 0);
            dateofbirth.setError(ssbuilder);
            valid = false;
        } else {
            dateofbirth.setError(null);
        }
        return valid;
    }
    public void edit_farmer(View v){
        if (!validate()) {
            Snackbar.make(v, "Correct the above errors first", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);
        String gender =radioSexButton.getText().toString();
        String genstr="M";

        if (gender.equals("Male")){
            genstr="M";
        }else{
            genstr="F";
        }
        //  “farmerId”: 23,
        // “firstname”:””,
        //“middlename”:””,
        //“lastname”:””,
        //“mobileno”:””o,
        //“Gender”:””,
        //“locale” :””;
        //“dateFormat”:””,
        //“dateOfBirth”:””
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Submitting farmer details to be edited.");
        pDialog.setCancelable(false);
        pDialog.show();
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("farmerId",farmer_id+"");
        hashMap.put("firstname",full_name.getText().toString().trim());
        hashMap.put("middlename","");
        hashMap.put("lastname","");
        hashMap.put("mobileno",mobile_no.getText().toString().trim());
        hashMap.put("Gender",genstr);
        hashMap.put("dateOfBirth",dateofbirth.getText().toString().trim());
        hashMap.put("dateFormat","DD-MM-YYYY");
        hashMap.put("locale","en");

        Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
        APIService service = retrofit.create(APIService.class);
        Call<AllResponse> call = service.posteditfarmer("Basic YWRtaW46bWFudW5pdGVk",hashMap,farmer_id);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                pDialog.dismissWithAnimation();
                try {
                    if (response.body()!=null){
                        new SweetAlertDialog(EditFarmerActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success")
                                .setContentText("You have successfully submitted "+full_name.getText()+" details")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        finish();
                                    }
                                })
                                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        finish();
                                    }
                                })
                                .show();

                    }else{
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        new SweetAlertDialog(EditFarmerActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Ooops...")
                                .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                .show();

                    }
                } catch (Exception e) {
                    new SweetAlertDialog(EditFarmerActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(e.getMessage())
                            .show();
                }
                //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                pDialog.cancel();
                new SweetAlertDialog(EditFarmerActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText(t.getMessage())
                        .show();
            }
        });

    }
    private void openDatePicker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String mDate=convertDate(convertToMillis(dayOfMonth,month,year));
                dateofbirth.setText(mDate);

            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    public String convertDate(long mTime) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
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
