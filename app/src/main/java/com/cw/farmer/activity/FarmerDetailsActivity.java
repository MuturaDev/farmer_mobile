package com.cw.farmer.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cw.farmer.R;
import com.cw.farmer.model.PageItem;

import java.util.List;

public class FarmerDetailsActivity extends AppCompatActivity {
    TextView tv_farmerstatus,tv_full_name, tv_mobileno, tv_email, tv_gender, tv_dateOfbirth, tv_address, tv_centername;
    PageItem pageItem;
    Button btn_blacklist;
    Spinner blacklist_reasons;
    List<Integer> blacklist_id;
    EditText editTextDateto,editTextDatefrom;
    DatePickerDialog picker,picker1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_details);
        tv_full_name = findViewById(R.id.tv_full_name);
        tv_mobileno = findViewById(R.id.tv_mobileno);
        tv_email = findViewById(R.id.tv_email);
        tv_gender = findViewById(R.id.tv_gender);
        tv_dateOfbirth = findViewById(R.id.tv_dateOfbirth);
        tv_address = findViewById(R.id.tv_address);
        tv_centername = findViewById(R.id.tv_centername);
        tv_farmerstatus = findViewById(R.id.tv_farmerstatus);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            final int errorColor;
            final int version = Build.VERSION.SDK_INT;


            pageItem = bundle.getParcelable("item");
            tv_full_name.setText("Farmer Name : " + pageItem.getFirstname() + " " + pageItem.getMiddlename() + " " + pageItem.getLastname());
            tv_mobileno.setText("Farmer Reference No : " + pageItem.getFarmercode());
            tv_email.setText("Farmer Id No : " + pageItem.getIdno());
            tv_address.setText("Mobile No : " + pageItem.getMobileno());
            String gender="";
            if (pageItem.getGender().equals("M")){
               gender="Male";
            }else {
                gender="Female";
            }
            tv_gender.setText("Gender : " +gender );
            tv_centername.setText("Center : " + pageItem.getCentername());
            tv_farmerstatus.setText("Farmer Status : "+bundle.getString("status"));


        }
    }


    public void openBlacklist(View v){
        Intent intent = new Intent(FarmerDetailsActivity.this, BlacklistActivity.class);
        Bundle bundle1 = new Bundle();
        bundle1.putParcelable("item", pageItem);
        intent.putExtras(bundle1);
        intent.putExtra("id",pageItem.getId());
        startActivity(intent);
        finish();
    }
    public void opendoc(View v){
        Intent intent = new Intent(FarmerDetailsActivity.this, FarmerDocumentsActivity.class);
        intent.putExtra("id",pageItem.getId());
        startActivity(intent);
    }
    public void openbank(View v){
        Intent intent = new Intent(FarmerDetailsActivity.this, FarmerAccountsActivity.class);
        intent.putExtra("id",pageItem.getId());
        startActivity(intent);
    }
}
