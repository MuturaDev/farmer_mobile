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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.cw.farmer.R;
import com.cw.farmer.model.AllResponse;
import com.cw.farmer.model.BankNameResponse;
import com.cw.farmer.model.PageItem;
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

public class UpdateBankActivity extends AppCompatActivity {
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };
    List<Integer> bank_ids;
    String farmer_id, account_name, bank_name, account_image, entry_id, status_farmer, dob;
    PageItem pageItem;
    private File compressedImageFile;
    private File bankImageFile;
    private Spinner et_bankname;
    private EditText et_accountno;
    private ImageView iv_bank_image;

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bank);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        iv_bank_image = findViewById(R.id.iv_bank_image);
        iv_bank_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageBank();
            }
        });
        et_bankname = findViewById(R.id.et_bankname);
        et_accountno = findViewById(R.id.et_account_no);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            et_accountno.setText((String) b.get("account_name"));
            //account_name,bank_name,account_image
            farmer_id = (String) b.get("id");
            bank_name = (String) b.get("bank_name");
            account_image = (String) b.get("account_image");
            entry_id = (String) b.get("entry_id");
            account_name = (String) b.get("account_name");
            status_farmer = (String) b.get("status_farmer");
            dob = (String) b.get("dob");

            pageItem = b.getParcelable("item");

            byte[] imageByteArray = Base64.decode(account_image, Base64.DEFAULT);
            Glide.with(UpdateBankActivity.this).asBitmap().load(imageByteArray).into(iv_bank_image);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
        APIService service = retrofit.create(APIService.class);
        SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
        String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
        Call<List<BankNameResponse>> call = service.getbankname(auth_key);
        call.enqueue(new Callback<List<BankNameResponse>>() {
            @Override
            public void onResponse(Call<List<BankNameResponse>> call, Response<List<BankNameResponse>> response) {
                ArrayList<String> spinnerArray = new ArrayList<String>();
                spinnerArray.clear();
                bank_ids = new ArrayList<Integer>();
                for (BankNameResponse bank : response.body()) {

                    spinnerArray.add(bank.getBankname());
                    bank_ids.add(bank.getId());
                }
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(UpdateBankActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                et_bankname.setAdapter(spinnerArrayAdapter);
                et_bankname.setSelection(spinnerArrayAdapter.getPosition(bank_name));
            }

            @Override
            public void onFailure(Call<List<BankNameResponse>> call, Throwable t) {
                Toast.makeText(UpdateBankActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void selectImageBank() {

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
                    if (!hasPermissions(UpdateBankActivity.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(UpdateBankActivity.this, PERMISSIONS, PERMISSION_ALL);
                    } else {
                        EasyImage.openCamera(UpdateBankActivity.this, 1);
                    }

                } else if (items[item].equals("Choose from Library")) {
                    if (!hasPermissions(UpdateBankActivity.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(UpdateBankActivity.this, PERMISSIONS, PERMISSION_ALL);
                    } else {
                        EasyImage.openGallery(UpdateBankActivity.this, 1);
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

            EasyImage.handleActivityResult(requestCode, resultCode, data, UpdateBankActivity.this, new EasyImage.Callbacks() {
                @Override
                public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                    e.printStackTrace();
                    Toast.makeText(UpdateBankActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onImagePicked(File imageFile, EasyImage.ImageSource source, final int type) {

                    new Compressor(UpdateBankActivity.this)
                            .compressToFileAsFlowable(imageFile)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new io.reactivex.functions.Consumer<File>() {
                                @Override
                                public void accept(File file) {
                                    if (type == 0) {
                                        compressedImageFile = file;

                                    } else {
                                        bankImageFile = file;

                                        Glide.with(UpdateBankActivity.this).load(bankImageFile).into(iv_bank_image);
                                    }

                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) {
                                    throwable.printStackTrace();
                                    Toast.makeText(UpdateBankActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
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

    public void submit_changes(View v) {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Updating bank account details...");
        pDialog.setCancelable(false);
        pDialog.show();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("accountno", et_accountno.getText().toString());
        hashMap.put("filetype", "image/png");
        hashMap.put("bankId", bank_ids.get(et_bankname.getSelectedItemPosition()).toString());
        hashMap.put("image", getBase64FromPathBank());

        Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
        APIService service = retrofit.create(APIService.class);
        SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
        String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
        Call<AllResponse> call = service.changebank(auth_key, hashMap, farmer_id, entry_id);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                pDialog.dismissWithAnimation();
                try {
                    if (response.body() != null) {
                        new SweetAlertDialog(UpdateBankActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success")
                                .setContentText("You have successfully updated farmer bank account details")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        Intent intent = new Intent(UpdateBankActivity.this, FarmerDetailsActivity.class);
                                        Bundle bundle1 = new Bundle();
                                        bundle1.putParcelable("item", pageItem);
                                        intent.putExtras(bundle1);
                                        intent.putExtra("id", pageItem.getId());
                                        intent.putExtra("status", status_farmer);
                                        intent.putExtra("dob", dob);
                                        startActivity(intent);
                                    }
                                })
                                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        Intent intent = new Intent(UpdateBankActivity.this, FarmerDetailsActivity.class);
                                        Bundle bundle1 = new Bundle();
                                        bundle1.putParcelable("item", pageItem);
                                        intent.putExtras(bundle1);
                                        intent.putExtra("id", pageItem.getId());
                                        intent.putExtra("status", status_farmer);
                                        intent.putExtra("dob", dob);
                                        startActivity(intent);
                                    }
                                })
                                .show();

                    } else {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        new SweetAlertDialog(UpdateBankActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Ooops...")
                                .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                .show();

                    }
                } catch (Exception e) {
                    new SweetAlertDialog(UpdateBankActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(e.getMessage())
                            .show();
                }
                //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                pDialog.cancel();
                new SweetAlertDialog(UpdateBankActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText(t.getMessage())
                        .show();
            }
        });
    }

    public String getBase64FromPathBank() {
        String base64 = "";
        try {/*from   w w w .  ja  va  2s  .  c om*/
            File file = bankImageFile;

            if (file != null) {


                byte[] buffer = new byte[(int) file.length() + 100];
                @SuppressWarnings("resource")
                int length = new FileInputStream(file).read(buffer);
                base64 = Base64.encodeToString(buffer, 0, length,
                        Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (base64.equals("")) {
            return account_image;
        }
        return base64;
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
