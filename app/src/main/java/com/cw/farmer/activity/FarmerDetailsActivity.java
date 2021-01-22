package com.cw.farmer.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cw.farmer.HandleConnectionAppCompatActivity;
import com.cw.farmer.R;
import com.cw.farmer.custom.Utility;
import com.cw.farmer.model.PageItem;
import com.cw.farmer.utils.UtilityFunctions;

import org.imaginativeworld.oopsnointernet.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.NoInternetDialog;
import org.imaginativeworld.oopsnointernet.NoInternetSnackbar;

import java.util.List;

public class FarmerDetailsActivity extends HandleConnectionAppCompatActivity {
    TextView tv_farmerstatus,tv_full_name, tv_mobileno, tv_email, tv_gender, tv_dateOfbirth, tv_address, tv_centername;
    PageItem pageItem;
    Button btn_blacklist;
    Spinner blacklist_reasons;
    List<Integer> blacklist_id;
    EditText editTextDateto,editTextDatefrom;
    DatePickerDialog picker,picker1;
    String dob, status_farmer;

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
            dob=(String) bundle.get("dob");
            if (pageItem.getGender().equals("M")) {
                gender = "Male";
            } else {
                gender = "Female";
            }

            tv_gender.setText("Gender : " +gender );
            tv_centername.setText("Center : " + pageItem.getCentername());
            tv_farmerstatus.setText("Farmer Status : "+bundle.getString("status"));
            status_farmer = bundle.getString("status");
        }

    }


    public void openBlacklist(View v){
        new AsyncTask() {
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Boolean returnObject = (Boolean)o;
                if(!returnObject) {
                    ((TextView) findViewById(R.id.txt_check_internection)).setText("Oops no internet connection. You will need internet connection for some services to work");
                    ((TextView) findViewById(R.id.txt_check_internection)).setVisibility(View.VISIBLE);
                }else{
                    Intent intent = new Intent(FarmerDetailsActivity.this, BlacklistActivity.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putParcelable("item", pageItem);
                    intent.putExtras(bundle1);
                    intent.putExtra("id",pageItem.getId());
                    startActivity(intent);
                    finish();
                }
                super.onPostExecute(o);
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                return UtilityFunctions.isConnected(FarmerDetailsActivity.this);
            }
        }.execute();
    }

    public void opendoc(View v){

        new AsyncTask() {
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Boolean returnObject = (Boolean)o;
                if(!returnObject) {
                    ((TextView) findViewById(R.id.txt_check_internection)).setText("Oops no internet connection. You will need internet connection for some services to work");
                    ((TextView) findViewById(R.id.txt_check_internection)).setVisibility(View.VISIBLE);
                }else{
                    ((TextView) findViewById(R.id.txt_check_internection)).setVisibility(View.GONE);
                    Intent intent = new Intent(FarmerDetailsActivity.this, FarmerDocumentsActivity.class);
                    intent.putExtra("id",pageItem.getId());
                    intent.putExtra("status_farmer", status_farmer);
                    intent.putExtra("dob", dob);
                    Bundle bundle1 = new Bundle();
                    bundle1.putParcelable("item", pageItem);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                }
                super.onPostExecute(o);
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                return UtilityFunctions.isConnected(FarmerDetailsActivity.this);
            }
        }.execute();
    }


    public void openbank(View v){

        new AsyncTask() {
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Boolean returnObject = (Boolean)o;
                if(!returnObject) {
                    ((TextView) findViewById(R.id.txt_check_internection)).setText("Oops no internet connection. You will need internet connection for some services to work");
                    ((TextView) findViewById(R.id.txt_check_internection)).setVisibility(View.VISIBLE);
                }else{
                    ((TextView) findViewById(R.id.txt_check_internection)).setVisibility(View.GONE);
                    Intent intent = new Intent(FarmerDetailsActivity.this, FarmerAccountsActivity.class);
                    intent.putExtra("id",pageItem.getId());
                    intent.putExtra("status_farmer", status_farmer);
                    intent.putExtra("dob", dob);
                    Bundle bundle1 = new Bundle();
                    bundle1.putParcelable("item", pageItem);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                }
                super.onPostExecute(o);
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                return UtilityFunctions.isConnected(FarmerDetailsActivity.this);
            }
        }.execute();
    }

    public void openchangefarmercentre(View v) {

        new AsyncTask() {
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Boolean returnObject = (Boolean)o;
                if(!returnObject) {
                    ((TextView) findViewById(R.id.txt_check_internection)).setText("Oops no internet connection. You will need internet connection for some services to work");
                    ((TextView) findViewById(R.id.txt_check_internection)).setVisibility(View.VISIBLE);
                }else{
                    ((TextView) findViewById(R.id.txt_check_internection)).setVisibility(View.GONE);
                    Intent intent = new Intent(FarmerDetailsActivity.this, ChangeCentreActivity.class);
                    intent.putExtra("user_id", pageItem.getId() + "");
                    intent.putExtra("centre_name", pageItem.getCentername());
                    startActivity(intent);
                }
                super.onPostExecute(o);
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                return UtilityFunctions.isConnected(FarmerDetailsActivity.this);
            }
        }.execute();
    }

    public void openeditfarmer(View v){

        new AsyncTask() {
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Boolean returnObject = (Boolean)o;
                if(!returnObject) {
                    ((TextView) findViewById(R.id.txt_check_internection)).setText("Oops no internet connection. You will need internet connection for some services to work");
                    ((TextView) findViewById(R.id.txt_check_internection)).setVisibility(View.VISIBLE);
                }else{
                    ((TextView) findViewById(R.id.txt_check_internection)).setVisibility(View.GONE);
                    Intent intent = new Intent(FarmerDetailsActivity.this, EditFarmerActivity.class);
                    intent.putExtra("id",pageItem.getId());
                    intent.putExtra("dob",dob);
                    Bundle bundle1 = new Bundle();
                    bundle1.putParcelable("item", pageItem);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                }
                super.onPostExecute(o);
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                return UtilityFunctions.isConnected(FarmerDetailsActivity.this);
            }
        }.execute();
    }


    //SHOW INTENET CONNECTION
    // No Internet Dialog
    private NoInternetDialog noInternetDialog;

    // No Internet Snackbar
    private NoInternetSnackbar noInternetSnackbar;
    //@Override
    protected void onResume() {
        super.onResume();

        NoInternetSnackbar.Builder builder2 = new NoInternetSnackbar.Builder(this, (ViewGroup) findViewById(android.R.id.content));
        builder2.setConnectionCallback(new ConnectionCallback() { // Optional
            @Override
            public void hasActiveConnection(boolean hasActiveConnection) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });

            }
        });
        builder2.setIndefinite(true); // Optional
        builder2.setNoInternetConnectionMessage("No active Internet connection!"); // Optional
        builder2.setOnAirplaneModeMessage("You have turned on the airplane mode!"); // Optional
        builder2.setSnackbarActionText("Settings");
        builder2.setShowActionToDismiss(true);
        builder2.setSnackbarDismissActionText("Exit");

        noInternetSnackbar = builder2.build();

    }


    @Override
    protected void onPause() {
        super.onPause();
        // No Internet Snackbar
        if (noInternetSnackbar != null) {
            noInternetSnackbar.destroy();
        }
    }



}
