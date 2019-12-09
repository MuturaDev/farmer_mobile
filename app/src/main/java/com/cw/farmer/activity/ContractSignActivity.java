package com.cw.farmer.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.cw.farmer.NetworkUtil;
import com.cw.farmer.R;
import com.cw.farmer.model.AllResponse;
import com.cw.farmer.model.ContractSignDB;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.cw.farmer.ManifestPermission.hasPermissions;

public class ContractSignActivity extends AppCompatActivity {
    private ImageView iv_contract_image;
    private File compressedImageFile;
    EditText farmer,noofunits,codedate,contract_refno;
    String farmer_id_string;
    String planting_id_string, noofunits_text, recruit_id_string;;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_sign);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        iv_contract_image = findViewById(R.id.iv_contract_image);
        iv_contract_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageContract();
            }
        });
        farmer=findViewById(R.id.farmer_contract);
        codedate=findViewById(R.id.codedate);
        noofunits=findViewById(R.id.noofunits);
        contract_refno=findViewById(R.id.contract_refno);
        farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor searcheditor = getSharedPreferences("search", MODE_PRIVATE).edit();
                searcheditor.putString("activity","contract");
                searcheditor.apply();

                SharedPreferences.Editor editor = getSharedPreferences("recruit_farmer", MODE_PRIVATE).edit();
                editor.putString("noofunits",noofunits.getText().toString());
                editor.apply();

                Intent intent = new Intent(ContractSignActivity.this, SearchContractFarmerActivity.class);
                startActivity(intent);
            }
        });
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String name =(String) b.get("name");
            farmer.setText(name);

            String id =(String) b.get("id");
            farmer_id_string=id;

            String crop_date =(String) b.get("crop_date");
            codedate.setText(crop_date);

            noofunits_text = (String) b.get("noofunits");
            noofunits.setText(noofunits_text);

            String plantingid =(String) b.get("plantingid");
            planting_id_string=plantingid;

            String recruitId = (String)b.get("recruitid");
            recruit_id_string = recruitId;

        }
    }
    private void selectImageContract(){
        final CharSequence[] items =
                {
                        "Take Photo",
                        "Choose from Library",
                        "Cancel"
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    if (!hasPermissions(ContractSignActivity.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(ContractSignActivity.this, PERMISSIONS, PERMISSION_ALL);
                    } else {
                        EasyImage.openCamera(ContractSignActivity.this, 1);
                    }

                } else if (items[item].equals("Choose from Library")) {
                    if (!hasPermissions(ContractSignActivity.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(ContractSignActivity.this, PERMISSIONS, PERMISSION_ALL);
                    } else {
                        EasyImage.openGallery(ContractSignActivity.this, 1);
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            EasyImage.handleActivityResult(requestCode, resultCode, data, ContractSignActivity.this, new EasyImage.Callbacks() {
                @Override
                public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                    e.printStackTrace();
                    Toast.makeText(ContractSignActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onImagePicked(File imageFile, EasyImage.ImageSource source, final int type) {

                    new Compressor(ContractSignActivity.this)
                            .compressToFileAsFlowable(imageFile)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new io.reactivex.functions.Consumer<File>() {
                                @Override
                                public void accept(File file) {
                                    if (type==1){
                                        compressedImageFile = file;

                                        Glide.with(ContractSignActivity.this).load(compressedImageFile).into(iv_contract_image);
                                    }

                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) {
                                    throwable.printStackTrace();
                                    Toast.makeText(ContractSignActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                                    ;

                                }
                            });
                }

                @Override
                public void onCanceled(EasyImage.ImageSource source, int type) {
                }
            });
        }

    }
    public String getBase64FromPath() {
        String base64 = "";
        try {/*from   w w w .  ja  va  2s  .  c om*/
            File file = compressedImageFile;

            if (file!=null) {


                byte[] buffer = new byte[(int) file.length() + 100];
                @SuppressWarnings("resource")
                int length = new FileInputStream(file).read(buffer);
                base64 = Base64.encodeToString(buffer, 0, length,
                        Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64;
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

        if (farmer.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("must select a farmer");
            ssbuilder.setSpan(fgcspan, 0, "must select a farmer".length(), 0);
            farmer.setError(ssbuilder);
            valid = false;
        } else {
            farmer.setError(null);
        }

        if (codedate.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("must select a farmer to have the crop date field filled");
            ssbuilder.setSpan(fgcspan, 0, "must select a farmer to have the crop date field filled".length(), 0);
            codedate.setError(ssbuilder);
            valid = false;
        } else {
            codedate.setError(null);
        }

        //noofunits_text
        int current;
        if (noofunits.getText().toString().equals("")) {
            current = 0;
        } else {
            current = Integer.parseInt(noofunits.getText().toString());
        }
        int correct;
        if (noofunits.getText().toString().equals("")) {
            correct = 0;
        } else {
            correct = Integer.parseInt(noofunits_text) + 1;
        }
        System.out.println(correct + "<" + current);

        if (noofunits.getText().toString().isEmpty()) {
            String message = "Must input no of units";
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder(message);
            ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
            noofunits.setError(ssbuilder);
            valid = false;
        } else if (correct < current) {
            String message = "Max no of units can only " + noofunits_text + " below";
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder(message);
            ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
            noofunits.setError(ssbuilder);
            valid = false;
        } else {
            noofunits.setError(null);
        }


        if (contract_refno.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("must input reference number");
            ssbuilder.setSpan(fgcspan, 0, "must input reference number".length(), 0);
            contract_refno.setError(ssbuilder);
            valid = false;
        } else {
            contract_refno.setError(null);
        }
        if (getBase64FromPath().equals("")) {
            Toast.makeText(ContractSignActivity.this, "Must upload the contract image", Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;
    }
    public void contract_sign(View v){
        if (!validate()) {
            Snackbar.make(v, "Correct the above errors first", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Submitting Contract Signing...");
        pDialog.setCancelable(false);
        pDialog.show();
        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("referenceNo", contract_refno.getText().toString().trim());
            hashMap.put("cropDateId", planting_id_string);
            hashMap.put("units", noofunits.getText().toString().trim());
            hashMap.put("farmerId", farmer_id_string);
            hashMap.put("recruitId", recruit_id_string);
            hashMap.put("file", getBase64FromPath());
            hashMap.put("dateFormat", "DD/M/YYYY");
            hashMap.put("locale", "en");

            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
            APIService service = retrofit.create(APIService.class);
            SharedPreferences prefs = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
            String auth_key = prefs.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
            Call<AllResponse> call = service.postcontract(auth_key, hashMap);
            call.enqueue(new Callback<AllResponse>() {
                @Override
                public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                    //pDialog.hide();
                    pDialog.dismissWithAnimation();
                    try {
                        if (response.body() != null) {
                            new SweetAlertDialog(ContractSignActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success")
                                    .setContentText("You have successfully submitted " + farmer.getText().toString().trim() + " farmer contract signing details")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            startActivity(new Intent(ContractSignActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                        }
                                    })
                                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            startActivity(new Intent(ContractSignActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                        }
                                    })
                                    .show();

                        } else {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            new SweetAlertDialog(ContractSignActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Ooops...")
                                    .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                    .show();

                        }
                    } catch (Exception e) {
                        new SweetAlertDialog(ContractSignActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText(e.getMessage())
                                .show();
                    }
                    //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(Call<AllResponse> call, Throwable t) {
                    pDialog.cancel();
                    new SweetAlertDialog(ContractSignActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(t.getMessage())
                            .show();
                }
            });
        } else {
            ContractSignDB book = new ContractSignDB(contract_refno.getText().toString().trim(), planting_id_string, noofunits.getText().toString().trim(), farmer_id_string, getBase64FromPath(), "DD/M/YYYY", "en",recruit_id_string);
            book.save();
            pDialog.hide();
            new SweetAlertDialog(ContractSignActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("No Wrong")
                    .setContentText("We have saved the data offline, We will submitted it when you have internet")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            startActivity(new Intent(ContractSignActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                        }
                    })
                    .show();
        }

    }

}
