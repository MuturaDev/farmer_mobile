package com.cw.farmer.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.bumptech.glide.Glide;
import com.cw.farmer.HandleConnectionAppCompatActivity;
import com.cw.farmer.NetworkUtil;
import com.cw.farmer.R;
import com.cw.farmer.model.CropWalkPageItemResponse;
import com.cw.farmer.model.CropWalkTB;
import com.cw.farmer.offlinefunctions.OfflineFeature;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.cw.farmer.spinner_models.CropStageResponse;
import com.cw.farmer.spinner_models.GrowthParameterResponse;
import com.cw.farmer.utils.UtilityFunctions;
import com.google.android.material.snackbar.Snackbar;
import com.viven.imagezoom.ImageZoomHelper;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import id.zelory.compressor.Compressor;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.cw.farmer.ManifestPermission.hasPermissions;

public class CropWalkActivity extends HandleConnectionAppCompatActivity {

    private CardView btn_take_photo,btn_submit;
    private Spinner sp_crop_stage,sp_growth_parameter,sp_comment,sp_additional_info;
    private MultiSpinnerSearch sp_multi_additional_info;

    private TextView et_select_farmer,et_crop_date,et_comment,et_additional_feedback;

    private ProgressBar spinner_progress2,spinner_progress;

    private CropStageResponse selectedCropStage = null;
    private GrowthParameterResponse selectedGrowthParameter = null;
    private String selectedSpComment = null;
    private String selectedSpAdditionalInfo = null;

    private File compressedImageFile;

    private ImageView image;
    private TextView image_preview;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };

    public void back(View view){
        finish();
    }


    CropWalkPageItemResponse cropWalkSearchPageItemResponse;

    boolean commentValidate = false;

    private ImageView image_dropdown_additional_info,image_dropdown_comment;

    private ImageZoomHelper imageZoomHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_walk_activity_layout);

        enableLocationTracking();

        imageZoomHelper = new ImageZoomHelper(this);

        image_dropdown_additional_info = findViewById(R.id.image_dropdown_additional_info);
        image_dropdown_comment = findViewById(R.id.image_dropdown_comment);

        image = findViewById(R.id.image);
        image_preview = findViewById(R.id.image_preview);

        btn_take_photo = findViewById(R.id.btn_take_photo);
        btn_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });

        sp_crop_stage = findViewById(R.id.sp_crop_stage);
        sp_crop_stage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i==-1){
                    selectedCropStage = null;
                }else{
                    if(adapterView.getSelectedItem() instanceof CropStageResponse ){
                        selectedCropStage = (CropStageResponse) adapterView.getSelectedItem();
                        populateGrowthParameter(selectedCropStage.getId());
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedCropStage = null;
            }
        });
        sp_growth_parameter = findViewById(R.id.sp_growth_parameter);
        sp_growth_parameter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i==-1){
                    selectedGrowthParameter = null;
                }else{
                    if(adapterView.getSelectedItem() instanceof GrowthParameterResponse) {
                        selectedGrowthParameter = (GrowthParameterResponse) adapterView.getSelectedItem();
                        if(selectedGrowthParameter.getParamType().equals("N")){
                            et_comment.setVisibility(View.VISIBLE);
                            et_comment.setInputType(InputType.TYPE_CLASS_NUMBER);
                            sp_comment.setVisibility(View.GONE);
                            image_dropdown_comment.setVisibility(View.GONE);

                            if(TextUtils.isEmpty(selectedGrowthParameter.getAdditionalDependentValue())){
                                sp_multi_additional_info.setVisibility(View.GONE);
                                sp_additional_info.setVisibility(View.GONE);
                                image_dropdown_additional_info.setVisibility(View.GONE);
                                et_additional_feedback.setVisibility(View.VISIBLE);
                            }

                        }else if(selectedGrowthParameter.getParamType().equals("T")){
                            et_comment.setVisibility(View.VISIBLE);
                            et_comment.setInputType(InputType.TYPE_CLASS_TEXT);
                            //https://stackoverflow.com/questions/3285412/whats-the-best-way-to-limit-text-length-of-edittext-in-android#:~:text=This%20will%20limit%20the%20maximum,EditText%20widget%20to%206%20characters.&text=Programmatically%3A,%5B0%5D%20%3D%20new%20InputFilter.
                            //Set Length filter. Restricting to 10 characters only
                            if(selectedGrowthParameter.getParamConstraints() != null)
                            if(TextUtils.isDigitsOnly(selectedGrowthParameter.getParamConstraints()))
                                et_comment.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(selectedGrowthParameter.getParamConstraints()))});
                            sp_comment.setVisibility(View.GONE);
                            image_dropdown_comment.setVisibility(View.GONE);

                            if(TextUtils.isEmpty(selectedGrowthParameter.getAdditionalDependentValue())){
                                sp_multi_additional_info.setVisibility(View.GONE);
                                sp_additional_info.setVisibility(View.GONE);
                                image_dropdown_additional_info.setVisibility(View.GONE);
                                et_additional_feedback.setVisibility(View.VISIBLE);
                            }

                        }else if(selectedGrowthParameter.getParamType().equals("D")){
                            et_comment.setVisibility(View.GONE);

                            image_dropdown_comment.setVisibility(View.VISIBLE);
                            sp_comment.setVisibility(View.VISIBLE);
                            //TODO: as a listing there is need to check.
                            if(!TextUtils.isEmpty(selectedGrowthParameter.getParamConstraints())){
                                //String[] dropDownList = new String[]{"Select Comment"};
                                List<String> dropDownList = new ArrayList<>();
                                dropDownList.add("Select Comment");
                                dropDownList.addAll(Arrays.asList(selectedGrowthParameter.getParamConstraints().split(",")));
                                sp_comment.setAdapter(new ArrayAdapter<>(CropWalkActivity.this, android.R.layout.simple_spinner_dropdown_item, dropDownList));

                            }

                            if(TextUtils.isEmpty(selectedGrowthParameter.getAdditionalDependentValue())){
                                sp_multi_additional_info.setVisibility(View.GONE);
                                sp_additional_info.setVisibility(View.GONE);
                                image_dropdown_additional_info.setVisibility(View.GONE);
                                et_additional_feedback.setVisibility(View.VISIBLE);
                            }


                        }


                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedGrowthParameter = null;
            }
        });



        et_select_farmer = findViewById(R.id.et_select_farmer);

        et_select_farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CropWalkActivity.this, SearchCropWalkActivity.class);
                startActivity(intent);
            }
        });
        et_crop_date = findViewById(R.id.et_crop_date);
        et_comment = findViewById(R.id.et_comment);
        et_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int errorColor;
                final int version = Build.VERSION.SDK_INT;

                //Get the defined errorColor from color resource.
                if (version >= 23) {
                    errorColor = ContextCompat.getColor(getApplicationContext(), R.color.errorColor);
                } else {
                    errorColor = getResources().getColor(R.color.errorColor);
                }

                if(selectedGrowthParameter.getParamType().equals("N")){
                    if(selectedGrowthParameter.getParamConstraints().contains(",")){
                        String[] constraintParam = selectedGrowthParameter.getParamConstraints().split(",");
                        int max = 0;int min = 0;
                        if(constraintParam.length == 2){
                            if(TextUtils.isDigitsOnly(constraintParam[0])){
                                min = Integer.parseInt(constraintParam[0]);
                            }

                            if(TextUtils.isDigitsOnly(constraintParam[1])){
                                max = Integer.parseInt(constraintParam[1]);
                            }
                        }

                        if(!TextUtils.isEmpty(editable.toString()))
                        if(TextUtils.isDigitsOnly(editable.toString().trim())){
                            int numberEntered = Integer.parseInt(editable.toString().trim());
                            if(numberEntered <= max && numberEntered >= min){
                                et_comment.setError(null);
                                commentValidate = true;
                            }else{
                                ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
                                SpannableStringBuilder ssbuilder = new SpannableStringBuilder("Out of Range. Should be between " + min + " and " + max + " inclusive");
                                ssbuilder.setSpan(fgcspan, 0, ("Out of Range. Should be between " + min + " and " + max + " inclusive").length(), 0);
                                et_comment.setError(ssbuilder);
                                commentValidate = false;

                            }
                        }
                    }

                }else if(selectedGrowthParameter.getParamType().equals("T")){


                }else if(selectedGrowthParameter.getParamType().equals("D")){

                }
            }
        });
        sp_additional_info  = findViewById(R.id.sp_additional_info);
        sp_additional_info.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    selectedSpAdditionalInfo = null;
                }else{
                    selectedSpAdditionalInfo = (String) adapterView.getSelectedItem();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_multi_additional_info = findViewById(R.id.multipleItemSelectionSpinner);
        sp_comment = findViewById(R.id.sp_comment);


        sp_comment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    selectedSpComment = null;
                }else{
                    if(adapterView.getSelectedItem() instanceof String) {
                        selectedSpComment = (String) adapterView.getSelectedItem();
                        if(!TextUtils.isEmpty(selectedSpComment) && !TextUtils.isEmpty(selectedGrowthParameter.getAdditionalDependentValue())){
                            if(selectedSpComment.equals(selectedGrowthParameter.getAdditionalDependentValue())){
                                if(selectedGrowthParameter.getAdditionalDependParamType().equals("M")){
                                    //you can select multiple
                                    if(!TextUtils.isEmpty(selectedGrowthParameter.getAdditionalDependConstraints())){

                                        List<String> dropDownList = new ArrayList<>();
                                       // dropDownList.add("Select Additional Info");
                                        dropDownList.addAll(Arrays.asList(selectedGrowthParameter.getAdditionalDependConstraints().split(",")));

                                       // ArrayAdapter<String> adapter = new ArrayAdapter <String>(CropWalkActivity.this, android.R.layout.simple_list_item_multiple_choice, dropDownList);
//                                        sp_multi_additional_info
//                                                .setListAdapter(adapter)
//                                                .setSelectAll(false)
//                                                .setMinSelectedItems(1)
//                                                .setTitle("Select Additional Info")
//                                                .setListener( /*"All Types", "none",*/ new MultiSpinnerSearch.MultiSpinnerListener() {
//                                                    @Override
//                                                    public void onItemsSelected(boolean[] checkedItems) {
//                                                        List<String> checkedItemList = new ArrayList<>();
//
//                                                        //add checked item
//                                                        for(int i =0; i< checkedItems.length; i++){
//                                                            String item = dropDownList.get(i);
//                                                            if(checkedItems[i]){
//                                                                checkedItemList.add(item);
//                                                            }
//                                                        }
//
//                                                        //concat checked items
//                                                        StringBuilder sb = new StringBuilder();
//                                                        for(int i =0; i< checkedItemList.size(); i++){
//                                                            if(i > 0){
//                                                                sb.append(",");
//                                                            }
//                                                            String item = checkedItemList.get(i).trim();
//                                                            sb.append(item);
//
//                                                        }
//                                                        selectedSpAdditionalInfo = sb.toString();
//                                                        //sp_multi_additional_info.
//                                                        int i = 0;
//                                                    }
//                                                });


                                        //NEW MULTI DROP DOWN
//                                        final List<String> list = Arrays.asList(getResources().getStringArray(R.array.mood_array));
                                        final List<KeyPairBoolData> listArray0 = new ArrayList<>();
                                        for (int ii = 0; ii < dropDownList.size(); ii++) {
                                            KeyPairBoolData h = new KeyPairBoolData();
                                            h.setId(ii+1);
                                            h.setName(dropDownList.get(ii));
                                            h.setSelected(false);
                                            listArray0.add(h);
                                        }

                                        /**
                                         * Search MultiSelection Spinner (With Search/Filter Functionality)
                                         *
                                         *  Using MultiSpinnerSearch class
                                         */
                                         sp_multi_additional_info = findViewById(R.id.multipleItemSelectionSpinner);

                                        // Pass true If you want searchView above the list. Otherwise false. default = true.
                                        sp_multi_additional_info.setSearchEnabled(true);

                                        // A text that will display in search hint.
                                        sp_multi_additional_info.setSearchHint("Select one/multiple");

                                        // Set text that will display when search result not found...
                                        sp_multi_additional_info.setEmptyTitle("Not Data Found!");

                                        // If you will set the limit, this button will not display automatically.
                                        sp_multi_additional_info.setShowSelectAllButton(true);

                                        //A text that will display in clear text button
                                        sp_multi_additional_info.setClearText("Close & Clear");

                                        // Removed second parameter, position. Its not required now..
                                        // If you want to pass preselected items, you can do it while making listArray,
                                        // pass true in setSelected of any item that you want to preselect
                                        sp_multi_additional_info.setItems(listArray0, new MultiSpinnerListener() {
                                            @Override
                                            public void onItemsSelected(List<KeyPairBoolData> items) {

                                                List<String> checkedItemList = new ArrayList<>();

                                                //add checked item
                                                for (int i = 0; i < items.size(); i++) {
                                                    if (items.get(i).isSelected()) {
                                                        //Log.i(TAG, i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                                        String item = items.get(i).getName();
                                                            checkedItemList.add(item);

                                                    }
                                                }

                                                //concat checked items
                                                StringBuilder sb = new StringBuilder();
                                                for(int i =0; i< checkedItemList.size(); i++){
                                                    if(i > 0){
                                                        sb.append(",");
                                                    }
                                                    String item = checkedItemList.get(i).trim();
                                                    sb.append(item);

                                                }
                                                selectedSpAdditionalInfo = sb.toString();

                                            }
                                        });

                                        /**
                                         * If you want to set limit as maximum item should be selected is 2.
                                         * For No limit -1 or do not call this method.
                                         *
                                         */
                                        sp_multi_additional_info.setLimit(100, new MultiSpinnerSearch.LimitExceedListener() {
                                            @Override
                                            public void onLimitListener(KeyPairBoolData data) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Limit exceed ", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                        sp_multi_additional_info.setVisibility(View.VISIBLE);
                                        image_dropdown_additional_info.setVisibility(View.VISIBLE);
                                        sp_additional_info.setVisibility(View.GONE);
                                        et_additional_feedback.setVisibility(View.GONE);

                                    }
                                }else{
                                    //you can only select one
                                    if(!TextUtils.isEmpty(selectedGrowthParameter.getAdditionalDependConstraints())) {

                                        List<String> dropDownList = new ArrayList<>();
                                        dropDownList.add("Select Additional Info");
                                        dropDownList.addAll(Arrays.asList(selectedGrowthParameter.getAdditionalDependConstraints().split(",")));

                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CropWalkActivity.this, android.R.layout.simple_spinner_dropdown_item, dropDownList);
                                        sp_additional_info.setAdapter(adapter);

                                        sp_multi_additional_info.setVisibility(View.GONE);
                                        sp_additional_info.setVisibility(View.VISIBLE);
                                        image_dropdown_additional_info.setVisibility(View.VISIBLE);
                                        et_additional_feedback.setVisibility(View.GONE);

                                    }
                                }
                            }else{
                                sp_multi_additional_info.setVisibility(View.GONE);
                                sp_additional_info.setVisibility(View.GONE);
                                image_dropdown_additional_info.setVisibility(View.GONE);
                                et_additional_feedback.setVisibility(View.VISIBLE);
                            }
                        }else{
                            sp_multi_additional_info.setVisibility(View.GONE);
                            sp_additional_info.setVisibility(View.GONE);
                            image_dropdown_additional_info.setVisibility(View.GONE);
                            et_additional_feedback.setVisibility(View.VISIBLE);
                        }
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
               selectedSpComment = null;
            }
        });


        et_additional_feedback = findViewById(R.id.et_additional_feedback);
        spinner_progress2 = findViewById(R.id.spinner_progress2);
        spinner_progress = findViewById(R.id.spinner_progress);


        populateCropStages();

            //NOTE: TESTING SPINNER WITH MULTIPLE OPTIONS



        if(getIntent() != null)
            if(getIntent().getBundleExtra("message") != null){
                Bundle b = getIntent().getBundleExtra("message");
                cropWalkSearchPageItemResponse = (CropWalkPageItemResponse) b.getSerializable("CropWalkPageItemResponse");
                et_select_farmer.setText(cropWalkSearchPageItemResponse.getFarmerName());
                et_crop_date.setText(UtilityFunctions.formatDate(cropWalkSearchPageItemResponse.getCropDate()));

            }

    }



    private boolean validate(){
        boolean valid = true;
        int errorColor;
        final int version = Build.VERSION.SDK_INT;

        //Get the defined errorColor from color resource.
        if (version >= 23) {
            errorColor = ContextCompat.getColor(getApplicationContext(), R.color.errorColor);
        } else {
            errorColor = getResources().getColor(R.color.errorColor);
        }



        if (et_select_farmer.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("Please Input a value in Select Farmer Field.");
            ssbuilder.setSpan(fgcspan, 0, "Please Input a value in Select Farmer Field".length(), 0);
            et_select_farmer.setError(ssbuilder);
            valid = false;
        } else {
            et_select_farmer.setError(null);
        }


        if (et_crop_date.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("Please Input a value in Crop Date Field.");
            ssbuilder.setSpan(fgcspan, 0, "Please Input a value in Crop Date Field".length(), 0);
            et_crop_date.setError(ssbuilder);
            valid = false;
        } else {
            et_crop_date.setError(null);
        }

//        if (et_comment.getText().toString().isEmpty()) {
//
//            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
//            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("Please Input a value in Comment Field.");
//            ssbuilder.setSpan(fgcspan, 0, "Please Input a value in Comment Field".length(), 0);
//            et_comment.setError(ssbuilder);
//            valid = false;
//        } else {
//            et_comment.setError(null);
//        }



        if( selectedCropStage == null){
            (findViewById(R.id.spinner_layout1)).setBackground(getResources().getDrawable(R.drawable.edit_text_layout_bg_lightblue_error));
            (findViewById(R.id.spinner_layout1)).startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            valid = false;
        }else{
            (findViewById(R.id.spinner_layout1)).setBackground(getResources().getDrawable(R.drawable.edit_text_layout_bg_lightblue));
        }

        if(selectedGrowthParameter == null){
            (findViewById(R.id.spinner_layout2)).setBackground(getResources().getDrawable(R.drawable.edit_text_layout_bg_lightblue_error));
            (findViewById(R.id.spinner_layout2)).startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            valid = false;
        }else{
            (findViewById(R.id.spinner_layout2)).setBackground(getResources().getDrawable(R.drawable.edit_text_layout_bg_lightblue));
        }

        //NOTE: its not a must the image to be there
//        if (getBase64FromPath().equals("")) {
//            Toast.makeText(CropWalkActivity.this, "Must upload the contract image", Toast.LENGTH_LONG).show();
//            valid = false;
//        }



        return valid;
    }


    private void submit(){

        //close keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(R.id.crop_walk_activity_layout).getWindowToken(), 0);


        if (!validate()) {
            Snackbar.make(findViewById(R.id.crop_walk_activity_layout), "Correct the above errors first", Snackbar.LENGTH_LONG)
                    .show();
            return;
        }


        if(locationText != null  && coordinates != null) {
            if (locationText != null && !coordinates.isEmpty()) {
                if (location_str == null) {
                    location_str = "Offline";
                } else {
                    if (location_str.isEmpty()) {
                        location_str = "Offline";
                    }
                }

                //Comment
                String postComment = "";
                if(sp_comment.getVisibility() == View.VISIBLE){
                    postComment = selectedSpComment;
                }

                if(et_comment.getVisibility() == View.VISIBLE){
                    postComment = et_comment.getText().toString().trim();
                }

                //Additional Fee
                String postAdditionalInfo = "";
                if(sp_additional_info.getVisibility() == View.VISIBLE){
                    postAdditionalInfo = selectedSpAdditionalInfo;
                }

                if(sp_multi_additional_info.getVisibility() == View.VISIBLE){
                    postAdditionalInfo = selectedSpAdditionalInfo;
                }

                if(et_additional_feedback.getVisibility() == View.VISIBLE){
                    postAdditionalInfo = et_additional_feedback.getText().toString().trim();
                }


                SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Please wait...submitting.");
                pDialog.setCancelable(false);
                pDialog.show();
                if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
                    SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);


                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("farmerid", Integer.valueOf(cropWalkSearchPageItemResponse.getFarmerId()));
                    hashMap.put("dateid", Integer.valueOf(cropWalkSearchPageItemResponse.getPlantingId()));
                    hashMap.put("walkid",Integer.valueOf(selectedGrowthParameter.getId()));
                    hashMap.put("cordinates", coordinates);
                    hashMap.put("location", location_str);
                    hashMap.put("cropstageid",Integer.valueOf(selectedCropStage.getId()));
                    hashMap.put("centerid",null);
                    //hashMap.put("value", cropWalkSearchPageItemResponse.getParameterValue());
                    hashMap.put("units",Integer.valueOf(cropWalkSearchPageItemResponse.getTotalUnits()));
                    hashMap.put("image",getBase64FromPath());
                    hashMap.put("filetype","image/png");
                    hashMap.put("locale", "en");
                    hashMap.put("comment",postComment);
                    hashMap.put("other",postAdditionalInfo);

                    //Log.d(this.getPackageName().toUpperCase(), "Before Submitting: " + hashMap.toString());

                    Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                    APIService service = retrofit.create(APIService.class);
                    String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                    Call<ResponseBody> call = service.postCropWalk(auth_key, hashMap);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                if (response.isSuccessful()) {
                                    pDialog.dismissWithAnimation();
                                    new SweetAlertDialog(CropWalkActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Success")
                                            .setContentText("Successfully submitted.")
                                            .setConfirmText("Ok")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                    startActivity(new Intent(CropWalkActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                                }
                                            })
//                                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
//                                        @Override
//                                        public void onClick(SweetAlertDialog sDialog) {
//                                            sDialog.dismissWithAnimation();
//                                            startActivity(new Intent(PlantBlockActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//
//                                        }
//                                    })
                                            .show();

                                } else {
                                    pDialog.dismissWithAnimation();
                                    new SweetAlertDialog(CropWalkActivity.this, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Try again")
                                            .setContentText(UtilityFunctions.parseThroughErrorBody(new JSONObject(response.errorBody().string())))
                                            .show();
                                }
                            } catch (Exception e) {
                                pDialog.dismissWithAnimation();
                                new SweetAlertDialog(CropWalkActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Try again")
                                        .setContentText(e.getMessage())
                                        .show();
                            }
                            //Toast.makeText(FarmerRecruitActivity.this,"You have successfully submitted farmer recruitment details", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            pDialog.dismissWithAnimation();
                            new SweetAlertDialog(CropWalkActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Try again...")
                                    .setContentText(t.getMessage())
                                    .show();
                        }
                    });
                }else {
                    SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                    CropWalkTB cropWalk = new CropWalkTB(
                           (cropWalkSearchPageItemResponse.getFarmerId()),
                           (cropWalkSearchPageItemResponse.getPlantingId()),
                            Integer.valueOf(selectedGrowthParameter.getId()),//(cropWalkSearchPageItemResponse.getID()),
                           coordinates,
                           location_str,
                           (selectedCropStage.getId()),
                            //TODO: REMOVE THIS, PART
                           null,//Integer.valueOf(prefs_auth.getString("center_ids", "1")),
                           null,//cropWalkSearchPageItemResponse.getParameterValue(),
                           Integer.valueOf(cropWalkSearchPageItemResponse.getTotalUnits()),
                    getBase64FromPath(),
                           "image/png",
                            postComment,
                   postAdditionalInfo
                    );
                    cropWalk.save();



                    new SweetAlertDialog(CropWalkActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("No Wrong")
                            .setContentText("We have saved the data offline, We will submit it when you have internet")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    startActivity(new Intent(CropWalkActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                }
                            })
                            .show();
                }

            }
        }
    }


    private void populateCropStages(){
        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
            showProgress1(true);
            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
            APIService service = retrofit.create(APIService.class);
            SharedPreferences prefs_auth = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
            String auth_key = prefs_auth.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
            Call<List<CropStageResponse>> call = service.getCropStages(auth_key);
            call.enqueue(new Callback<List<CropStageResponse>>() {
                @Override
                public void onResponse(Call<List<CropStageResponse>> call, Response<List<CropStageResponse>> response) {

                    if(response.code() == 200 || response.code() == 201)
                        if (response.body() != null) {
                            if (response.body().size() > 0) {
                                List<CropStageResponse> list = response.body();
                                list.add(new CropStageResponse(-1,"Crop Stage", "","", null,null,null,null,null));
                                //Collections.sort(list, Collections.reverseOrder());
                                Collections.reverse(list);
                                sp_crop_stage.setAdapter(new ArrayAdapter<>(CropWalkActivity.this, android.R.layout.simple_spinner_dropdown_item, list));
                                OfflineFeature.saveSharedPreferences(list, "CropStageSpinner", CropWalkActivity.this);
                            }
                        }
                    showProgress1(false);
                }

                @Override
                public void onFailure(Call<List<CropStageResponse>> call, Throwable t) {
                    showProgress1(false);
                }
            });
        }else{
            sp_crop_stage.setAdapter(new ArrayAdapter<>(CropWalkActivity.this, android.R.layout.simple_spinner_dropdown_item, (ArrayList<CropStageResponse>) OfflineFeature.getSharedPreferencesArray("CropStageSpinner", getApplicationContext(), CropStageResponse.class) ));
             showProgress1(false);
        }
    }



    private void populateGrowthParameter(Integer cropStageID){
        showProgress2(true);
        List<CropStageResponse> cropStageResponseList = (List<CropStageResponse>) OfflineFeature.getSharedPreferencesArray("CropStageSpinner", getApplicationContext(), CropStageResponse.class);

        for(CropStageResponse cropStageResponse : cropStageResponseList){
            if(cropStageResponse.getId() == cropStageID){
                List<GrowthParameterResponse> growthParameterResponseList = cropStageResponse.getParameters();
                if (growthParameterResponseList != null) {
                    if (growthParameterResponseList.size() > 0) {
                        growthParameterResponseList.add(new GrowthParameterResponse(-1, "Growth Parameter", "", "", "", "", -1, "", "", null,null,null,null,null,null));
                        //Collections.sort(list, Collections.reverseOrder());
                        Collections.reverse(growthParameterResponseList);
                        sp_growth_parameter.setAdapter(new ArrayAdapter<>(CropWalkActivity.this, android.R.layout.simple_spinner_dropdown_item, growthParameterResponseList));
                    } else {
                        List<GrowthParameterResponse> list = new ArrayList<>();
                        list.add(new GrowthParameterResponse(-1, "Growth Parameter", "", "", "", "", -1, "", "", null,null,null,null,null,null));
                        //Collections.sort(list, Collections.reverseOrder());
                        //Collections.reverse(list);
                        sp_growth_parameter.setAdapter(new ArrayAdapter<>(CropWalkActivity.this, android.R.layout.simple_spinner_dropdown_item, list));
                    }
                    break;
                }else{
                    List<GrowthParameterResponse> list = new ArrayList<>();
                    list.add(new GrowthParameterResponse(-1, "Growth Parameter", "", "", "", "", -1, "", "", null,null,null,null,null,null));
                    //Collections.sort(list, Collections.reverseOrder());
                    //Collections.reverse(list);
                    sp_growth_parameter.setAdapter(new ArrayAdapter<>(CropWalkActivity.this, android.R.layout.simple_spinner_dropdown_item, list));
                    break;
                }
            }
        }

        //populate growth parameters
        showProgress2(false);

    }

    private void showProgress1(boolean show){
        if(show){
            spinner_progress.setVisibility(View.VISIBLE);
            sp_crop_stage.setVisibility(View.GONE);
        }else{
            spinner_progress.setVisibility(View.GONE);
            sp_crop_stage.setVisibility(View.VISIBLE);
        }
    }

    private void showProgress2(boolean show){
        if(show){
            spinner_progress2.setVisibility(View.VISIBLE);
            sp_growth_parameter.setVisibility(View.GONE);
        }else{
            spinner_progress2.setVisibility(View.GONE);
            sp_growth_parameter.setVisibility(View.VISIBLE);
        }
    }


    private void selectImage(){
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
                    if (!hasPermissions(CropWalkActivity.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(CropWalkActivity.this, PERMISSIONS, PERMISSION_ALL);
                    } else {
                        EasyImage.openCamera(CropWalkActivity.this, 1);
                    }

                } else if (items[item].equals("Choose from Library")) {
                    if (!hasPermissions(CropWalkActivity.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(CropWalkActivity.this, PERMISSIONS, PERMISSION_ALL);
                    } else {
                        EasyImage.openGallery(CropWalkActivity.this, 1);
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            EasyImage.handleActivityResult(requestCode, resultCode, data, CropWalkActivity.this, new EasyImage.Callbacks() {
                @Override
                public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                    e.printStackTrace();
                    Toast.makeText(CropWalkActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onImagePicked(File imageFile, EasyImage.ImageSource source, final int type) {

                    new Compressor(CropWalkActivity.this)
                            .compressToFileAsFlowable(imageFile)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new io.reactivex.functions.Consumer<File>() {
                                @Override
                                public void accept(File file) {
                                    if (type==1){
                                        compressedImageFile = file;
                                        image.setScaleType(ImageView.ScaleType.MATRIX);
                                        //image.setAdjustViewBounds(true);
                                        Glide.with(CropWalkActivity.this).load(compressedImageFile).into(image);
                                        image_preview.setVisibility(View.GONE);
                                        ImageZoomHelper.setViewZoomable(image);
                                    }

                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) {
                                    throwable.printStackTrace();
                                    Toast.makeText(CropWalkActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();


                                }
                            });
                }

                @Override
                public void onCanceled(EasyImage.ImageSource source, int type) {
                }
            });
        }

        super.onActivityResult(requestCode,requestCode,data);

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


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return imageZoomHelper.onDispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

}
