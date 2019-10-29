package com.cw.farmer.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.cw.farmer.R;
import com.cw.farmer.model.AllResponse;
import com.cw.farmer.model.DestructionReasonResponse;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

public class CropDestructionActivity extends AppCompatActivity {
    private ImageView iv_destruction_image;
    private File compressedImageFile;
    EditText farmer_destruction, noofunits_destruction;
    Spinner codedate_destruction, type_destruction, reason_destruction;
    String farmer_id_string,accountNumber_string,referenceNo_string,units_string;
    String planting_id_string;
    List<Integer> reason_ids;
    List<String> cropDateId_list,reason_main;

    private RadioGroup radiotype;
    private RadioButton radioSexButton;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_destruction);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        radiotype = findViewById(R.id.radiotype);
        iv_destruction_image = findViewById(R.id.iv_destruction_image);
        iv_destruction_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageContract();
            }
        });
        farmer_destruction = findViewById(R.id.farmer_destruction);
        farmer_destruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor searcheditor = getSharedPreferences("search", MODE_PRIVATE).edit();
                searcheditor.putString("activity","destruction");
                searcheditor.apply();

                Intent intent = new Intent(CropDestructionActivity.this, SearchDestructionFarmerActivity.class);
                startActivity(intent);
            }
        });

        codedate_destruction = findViewById(R.id.codedate_destruction);
        noofunits_destruction = findViewById(R.id.noofunits_destruction);
        reason_destruction = findViewById(R.id.reason_destruction);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String name =(String) b.get("name");
            farmer_destruction.setText(name);

            String id =(String) b.get("farmerId");
            farmer_id_string=id;

            String accountNumber =(String) b.get("accountNumber");
            accountNumber_string=accountNumber;

            String referenceNo =(String) b.get("referenceNo");
            referenceNo_string=referenceNo;


            cropDateId_list = new ArrayList<String>();
            String cropDateId =(String) b.get("cropDateId");
            cropDateId_list.add(cropDateId);

            ArrayList<String> spinnerArray = new ArrayList<String>();
            spinnerArray.clear();
            String crop_date =(String) b.get("crop_date");
            spinnerArray.add(crop_date);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(CropDestructionActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
            codedate_destruction.setAdapter(spinnerArrayAdapter);

            ArrayList<String> spinnerArray1 = new ArrayList<String>();
            spinnerArray1.clear();
            String units =(String) b.get("units");


            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
            APIService service = retrofit.create(APIService.class);
            Call<List<DestructionReasonResponse>> call = service.getdestructionreasons();
            call.enqueue(new Callback<List<DestructionReasonResponse>>() {
                @Override
                public void onResponse(Call<List<DestructionReasonResponse>> call, Response<List<DestructionReasonResponse>> response) {
                    ArrayList<String> spinnerArray = new ArrayList<String>();
                    spinnerArray.clear();
                    reason_ids = new ArrayList<Integer>();
                    reason_main = new ArrayList<String>();
                    for(DestructionReasonResponse reason: response.body()) {

                        spinnerArray.add(reason.getDestructionShtDesc());
                        reason_ids.add(reason.getId());
                        reason_main.add(reason.getDestructionType());
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(CropDestructionActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                    reason_destruction.setAdapter(spinnerArrayAdapter);
                }

                @Override
                public void onFailure(Call<List<DestructionReasonResponse>> call, Throwable t) {
                    Toast.makeText(CropDestructionActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                }
            });

            reason_destruction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    //Toast.makeText(MainActivity.this, bank_ids.get(position).toString(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });



        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                    if (!hasPermissions(CropDestructionActivity.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(CropDestructionActivity.this, PERMISSIONS, PERMISSION_ALL);
                    } else {
                        EasyImage.openCamera(CropDestructionActivity.this, 1);
                    }

                } else if (items[item].equals("Choose from Library")) {
                    if (!hasPermissions(CropDestructionActivity.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(CropDestructionActivity.this, PERMISSIONS, PERMISSION_ALL);
                    } else {
                        EasyImage.openGallery(CropDestructionActivity.this, 1);
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

            EasyImage.handleActivityResult(requestCode, resultCode, data, CropDestructionActivity.this, new EasyImage.Callbacks() {
                @Override
                public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                    e.printStackTrace();
                    Toast.makeText(CropDestructionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onImagePicked(File imageFile, EasyImage.ImageSource source, final int type) {

                    new Compressor(CropDestructionActivity.this)
                            .compressToFileAsFlowable(imageFile)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new io.reactivex.functions.Consumer<File>() {
                                @Override
                                public void accept(File file) {
                                    if (type==1){
                                        compressedImageFile = file;

                                        Glide.with(CropDestructionActivity.this).load(compressedImageFile).into(iv_destruction_image);
                                    }

                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) {
                                    throwable.printStackTrace();
                                    Toast.makeText(CropDestructionActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
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
    public void destruction_submit(View v){
        int selectedId = radiotype.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        radioSexButton = (RadioButton) findViewById(selectedId);
        String landop =radioSexButton.getText().toString();
        String type="Crop Destruction";

        if (landop.equals("Partial Destruction")){
            type="Partial Destruction";
        }else{
            type="Crop Destruction";
        }
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Submitting Crop Destruction Data...");
        pDialog.setCancelable(false);
        pDialog.show();
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("cropDatesId",cropDateId_list.get(codedate_destruction.getSelectedItemPosition()).toString());
        hashMap.put("accountNumber",accountNumber_string);
        hashMap.put("unit", noofunits_destruction.getText().toString());
        hashMap.put("farmers_id",farmer_id_string);
        hashMap.put("file",getBase64FromPath());
        hashMap.put("locale","en");
        hashMap.put("cropDestructionType",reason_main.get(reason_destruction.getSelectedItemPosition()).toString());
        hashMap.put("cropDestructionReasonsId",reason_ids.get(reason_destruction.getSelectedItemPosition()).toString());

        Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
        APIService service = retrofit.create(APIService.class);
        Call<AllResponse> call = service.postcropdestruction("Basic YWRtaW46bWFudW5pdGVk",hashMap);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                pDialog.dismissWithAnimation();
                try {
                    if (response.body()!=null){
                        new SweetAlertDialog(CropDestructionActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success")
                                .setContentText("You have successfully submitted "+farmer_destruction.getText()+" crop destruction details")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        startActivity(new Intent(CropDestructionActivity.this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                    }
                                })
                                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        startActivity(new Intent(CropDestructionActivity.this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                    }
                                })
                                .show();

                    }else{
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        new SweetAlertDialog(CropDestructionActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Ooops...")
                                .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                .show();

                    }
                } catch (Exception e) {
                    new SweetAlertDialog(CropDestructionActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(e.getMessage())
                            .show();
                }
                //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                pDialog.cancel();
                new SweetAlertDialog(CropDestructionActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText(t.getMessage())
                        .show();
            }
        });
    }

}
