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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.cw.farmer.HandleConnectionAppCompatActivity;
import com.cw.farmer.R;
import com.cw.farmer.model.AllResponse;
import com.cw.farmer.model.PageItem;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

public class UpdateDocsActivity extends HandleConnectionAppCompatActivity {
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };
    String farmer_id, id_no, id_image, entry_id, status_farmer, dob;
    PageItem pageItem;
    private ImageView iv_image;
    private EditText et_idno;
    private File compressedImageFile;

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_docs);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        et_idno = findViewById(R.id.et_idnumber);
        iv_image = findViewById(R.id.iv_image);
        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        Intent iin = getIntent();
        Bundle bb = iin.getExtras();


        Map<String,String> b =  FarmerDocumentsActivity.pageItemObject;
        if (b != null) {
            et_idno.setText((String) b.get("id_no"));
            farmer_id = (String) b.get("id");
            entry_id = (String) b.get("entry_id");
            id_image = (String) b.get("id_image");
            id_no = (String) b.get("id_no");
            status_farmer = (String) b.get("status_farmer");
            dob = (String) b.get("dob");
            pageItem = bb.getParcelable("item");

            byte[] imageByteArray = Base64.decode(id_image, Base64.DEFAULT);
            Glide.with(UpdateDocsActivity.this).asBitmap().load(imageByteArray).into(iv_image);
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void selectImage() {

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
                    if (!hasPermissions(UpdateDocsActivity.this, PERMISSIONS)) {
//                        boolean showRationale = shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA);
//                        if (!showRationale) {
//                            Intent intent = new Intent();
//                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(), null);
//                            intent.setData(uri);
//                            startActivity(intent);
//                        } else {
                        ActivityCompat.requestPermissions(UpdateDocsActivity.this, PERMISSIONS, PERMISSION_ALL);
                        //  }


                    } else {
                        EasyImage.openCamera(UpdateDocsActivity.this, 0);
                    }

                } else if (items[item].equals("Choose from Library")) {
                    if (!hasPermissions(UpdateDocsActivity.this, PERMISSIONS)) {
//                        boolean showRationale = shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA);
//                        if (!showRationale) {
//                            Intent intent = new Intent();
//                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            Uri uri = Uri.fromParts("package", getPackageName(), null);
//                            intent.setData(uri);
//                            startActivity(intent);
//                        } else {
                        ActivityCompat.requestPermissions(UpdateDocsActivity.this, PERMISSIONS, PERMISSION_ALL);
                        //}


                    } else {
                        EasyImage.openGallery(UpdateDocsActivity.this, 0);
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

            EasyImage.handleActivityResult(requestCode, resultCode, data, UpdateDocsActivity.this, new EasyImage.Callbacks() {
                @Override
                public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                    e.printStackTrace();
                    Toast.makeText(UpdateDocsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onImagePicked(File imageFile, EasyImage.ImageSource source, final int type) {

                    new Compressor(UpdateDocsActivity.this)
                            .compressToFileAsFlowable(imageFile)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<File>() {
                                @Override
                                public void accept(File file) {
                                    if (type == 0) {
                                        compressedImageFile = file;
                                        iv_image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                        iv_image.setAdjustViewBounds(true);
                                        Glide.with(UpdateDocsActivity.this).load(compressedImageFile).into(iv_image);
                                    }

                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) {
                                    throwable.printStackTrace();
                                    Toast.makeText(UpdateDocsActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                                    ;

                                }
                            });
                }

                @Override
                public void onCanceled(EasyImage.ImageSource source, int type) {
                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getBase64FromPath() {
        String base64 = "";
        try {/*from   w w w .  ja  va  2s  .  c om*/
            File file = compressedImageFile;

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
            return id_image;
        }
        return base64;
    }

    public void submit_changes(View v) {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Updating farmer document details...");
        pDialog.setCancelable(false);
        pDialog.show();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("docId", "1");
        hashMap.put("docno", et_idno.getText().toString());
        hashMap.put("filetype", "image/png");
        hashMap.put("image", getBase64FromPath());

        Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
        APIService service = retrofit.create(APIService.class);
        SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
        String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
        Call<AllResponse> call = service.changedocs(auth_key, hashMap, farmer_id, entry_id);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                pDialog.dismissWithAnimation();
                try {
                    if (response.body() != null) {
                        new SweetAlertDialog(UpdateDocsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success")
                                .setContentText("You have successfully updated farmer document details")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        Intent intent = new Intent(UpdateDocsActivity.this, FarmerDetailsActivity.class);
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
                                        Intent intent = new Intent(UpdateDocsActivity.this, FarmerDetailsActivity.class);
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
                        new SweetAlertDialog(UpdateDocsActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Ooops...")
                                .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                .show();

                    }
                } catch (Exception e) {
                    new SweetAlertDialog(UpdateDocsActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(e.getMessage())
                            .show();
                }
                //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                pDialog.cancel();
                new SweetAlertDialog(UpdateDocsActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText(t.getMessage())
                        .show();
            }
        });
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
