package com.cw.farmer.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cw.farmer.HandleConnectionAppCompatActivity;
import com.cw.farmer.NetworkUtil;
import com.cw.farmer.R;
import com.cw.farmer.crashreporting.Catcho;
import com.cw.farmer.model.Accountdetails;
import com.cw.farmer.model.BankNameDB;
import com.cw.farmer.model.BankNameResponse;
import com.cw.farmer.model.EditContentModel;
import com.cw.farmer.model.FarmerErrorResponse;
import com.cw.farmer.model.FarmerModel;
import com.cw.farmer.model.FarmerModelDB;
import com.cw.farmer.model.Identitydetails;
import com.cw.farmer.offlinefunctions.OfflineDataSyncActivity;
import com.cw.farmer.server.APIService;
import com.cw.farmer.server.ApiClient;
import com.cw.farmer.utils.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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

public class MainActivity extends HandleConnectionAppCompatActivity implements View.OnClickListener {
    private ImageView iv_arrow_icon, iv_bank_arrow_icon, iv_document_arrow_icon, iv_image,iv_bank_image;
    private LinearLayout lin_farmer_det, lin_farmer_bank_det, lin_farmer_doc_det;
    private Button btn_farmel_det_next, btn_farmel_bank_det_pre, btn_farmel_bank_det_next, btn_farmel_doc_det_pre, btn_farmel_doc_det_next, btn_finish;
    private CardView cv_farmer_det, cv_bank_det, cv_document;

    public EditText et_firstName, et_phoneno, et_idno, et_dob, et_accountno;
    public RadioGroup radioSexGroup;
    public RadioButton radioSexButton;

    List<Integer> bank_ids, center_id_submit;
    ProgressDialog progressDialog;
    TextView doberror;
    private Spinner et_bankname, centre_id;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };
    private File compressedImageFile;
    private File bankImageFile;
    private int mYear;
    private int mMonth;
    private int mDay;


    private boolean editEnabled;
    public ArrayList<EditContentModel> editContentModelArrayList = new ArrayList<>();
    private EditContentModel editModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //REPORT CRASH IN THIS ACTIVITY
        Catcho.Builder(this)
                .recipients(Constants.CRASH_REPORT_EMAIL)
                .build();

        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        init();


        if(getIntent() != null){
            if(getIntent().getExtras() != null){

                String message = (String) getIntent().getExtras().getString("CHECKDATA");
                if(message.equalsIgnoreCase("registerfarmer")){
                    editContentModelArrayList   =  OfflineDataSyncActivity.registerfarmer;//(ArrayList<EditContentModel>) getIntent().getExtras().getSerializable("CHECKDATA");
                    editEnabled = true;
                }

            }
        }

        bottomSheetCode(editContentModelArrayList);

    }



    //BOTTOM SHEET
     class CustomDialogAdapter extends RecyclerView.Adapter<CustomDialogAdapter.MyViewHolder>{

        private List<EditContentModel> editContentModelList;
        private Context context;


        public CustomDialogAdapter(Context context, List<EditContentModel> editContentModelList) {
            this.editContentModelList = editContentModelList;
            this.context = context;

        }



        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class MyViewHolder extends RecyclerView.ViewHolder {

            LinearLayout status_layout;
            TextView title,error_message;
            LinearLayout edit_content_item_layout;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                status_layout = itemView.findViewById(R.id.status_layout);
                title = itemView.findViewById(R.id.title);
                error_message = itemView.findViewById(R.id.error_message);
                edit_content_item_layout = itemView.findViewById(R.id.edit_content_item_layout);
                edit_content_item_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditContentModel positionModel = editContentModelList.get(getAdapterPosition());
                        editModel = positionModel;
                        FarmerModelDB modelDB = (FarmerModelDB) positionModel.getContentObject();
                        et_firstName.setText(modelDB.firstname);
                        et_phoneno.setText(modelDB.mobileno);
                        et_idno.setText(modelDB.idno);
                        et_dob.setText(modelDB.dateOfBirth);
                        et_accountno.setText(modelDB.accountno);

                        List<BankNameDB> banks = BankNameDB.listAll(BankNameDB.class);
                        int count = 0;
                        for (BankNameDB bank : banks) {

//                            bank.bankname_db
//                            bank.id_db;

                            if(modelDB.bankId == bank.id_db){
                                break;
                            }
                            count++;
                        }

                        et_bankname.setSelection(count);
                        if(editEnabled) {
                            iv_bank_image.setVisibility(View.GONE);
                            iv_image.setVisibility(View.GONE);
                            btn_finish.setText("EDIT");
                        }


//                        Integer.parseInt(bank_ids.get(et_bankname.getSelectedItemPosition()).toString());
//                        et_bankname.setSelection(modelDB.bankId);

                        if(modelDB.gender.equals("F")) {
                            ((RadioButton) findViewById(R.id.radioMale)).setChecked(false);
                            ((RadioButton) findViewById(R.id.radioFemale)).setChecked(true);
                        }else {
                            ((RadioButton) findViewById(R.id.radioFemale)).setChecked(false);
                            ((RadioButton) findViewById(R.id.radioMale)).setChecked(true);
                        }


                        toggleBottomSheet();


                    }
                });

            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.edit_content_item_layout, parent, false);
            return  new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.title.setText(editContentModelList.get(position).getTitle());
            holder.error_message.setText(editContentModelList.get(position).getErrorMessage());

            if(editContentModelList.get(position).isStatus())
                holder.status_layout.setBackground(context.getResources().getDrawable(R.drawable.edit_circle));
            else
                holder.status_layout.setBackground(context.getResources().getDrawable(R.drawable.not_edit_circle));
        }

        @Override
        public int getItemCount() {
            return editContentModelList.size();
        }
    }

    public LinearLayout layoutBottomSheet;
    private BottomSheetBehavior sheetBehavior;
    private TextView label_change_bottom_sheet_state_ID;
    private LinearLayout btn_change_bottom_sheet_state_ID;
    private RecyclerView recyclerView;
    public void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            label_change_bottom_sheet_state_ID.setText("Hide Content for Edit");
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            label_change_bottom_sheet_state_ID.setText("Show Content for Edit");
        }
    }
    private void bottomSheetCode(ArrayList<EditContentModel> list){
        //BOTTOM SHEET
        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        if(list.size() == 0)
            layoutBottomSheet.setVisibility(View.GONE);
        else
            layoutBottomSheet.setVisibility(View.VISIBLE);

        ImageView img_offline_sync = findViewById(R.id.img_offline_sync);
        img_offline_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
                    Intent intent = new Intent(MainActivity.this, OfflineDataSyncActivity.class);
                    startActivity(intent);
                    finish();
                }else{

                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("No network connection")
                            .setContentText("Connect to the internet and try again.")
                            .setConfirmText("OK")
                            .show();

                }
            }
        });
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        label_change_bottom_sheet_state_ID = findViewById(R.id.label_change_bottom_sheet_state_ID);
        btn_change_bottom_sheet_state_ID = findViewById(R.id.btn_change_bottom_sheet_state_ID);

        /**
         * Bottom sheet state change listener.
         * We are changing bottom text when sheet changed state*/
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        label_change_bottom_sheet_state_ID.setText("Hide Content for Edit");
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        label_change_bottom_sheet_state_ID.setText("Show Content for Edit");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        btn_change_bottom_sheet_state_ID.setOnClickListener(this);
        recyclerView = findViewById(R.id.edit_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        CustomDialogAdapter adapter = new CustomDialogAdapter(MainActivity.this,list);
        recyclerView.setAdapter(adapter);

       // sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        if(list.size() != 0){
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                        //sheetBehavior.setPeekHeight(200);
            label_change_bottom_sheet_state_ID.setText("Hide Content for Edit");//4
        }else{
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);//3
            label_change_bottom_sheet_state_ID.setText("Show Content for Edit");
        }
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

        if (et_firstName.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("must input Full Names");
            ssbuilder.setSpan(fgcspan, 0, "must input Full Names".length(), 0);
            et_firstName.setError(ssbuilder);
            valid = false;
        } else {
            //ADD CHECK FOR TWO NAMES
            if (et_firstName.getText().toString().split(" ").length <= 1) {

                ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
                SpannableStringBuilder ssbuilder = new SpannableStringBuilder("must input Farmer two names ");
                ssbuilder.setSpan(fgcspan, 0, "must input Farmer two names".length(), 0);
                et_firstName.setError(ssbuilder);
                valid = false;
            } else {
                et_firstName.setError(null);
            }
        }




        if (et_phoneno.getText().toString().isEmpty()) {
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("must input Phone Number");
            ssbuilder.setSpan(fgcspan, 0, "must input Phone Number".length(), 0);
            et_phoneno.setError(ssbuilder);
            valid = false;

        }else if (et_phoneno.getText().length()!=10) {//also check for 07
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("Phone Number must be 10 digits");
            ssbuilder.setSpan(fgcspan, 0, "Phone Number must be 10 digits".length(), 0);
            et_phoneno.setError(ssbuilder);
            valid = false;
        } else {
            //CHECK FOR VALID PHONE NUMBER, 07XXXXXXXX
            //NEW CHANGES TO ACCOMMODATE NEW PHONENUMBERS
            if (et_phoneno.getText().toString().substring(0, 1).contains("0") /*&& et_phoneno.getText().toString().substring(1, 2).contains("7")*/) {//also check for 07

                et_phoneno.setError(null);
            } else {

                ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
                SpannableStringBuilder ssbuilder = new SpannableStringBuilder("Enter a valid Phone Number eg. 0xxxxxxxxx");
                ssbuilder.setSpan(fgcspan, 0, "Enter a valid Phone Number eg. 0xxxxxxxxx".length(), 0);
                et_phoneno.setError(ssbuilder);
                valid = false;

            }
        }




        final Calendar t = Calendar.getInstance();
        String today=convertDate(convertToMillis(t.get(Calendar.DAY_OF_MONTH),t.get(Calendar.MONTH),t.get(Calendar.YEAR)));


        long diff =  getDateDiff(new SimpleDateFormat("dd-MM-yyyy"), et_dob.getText().toString(), today);


        if (et_dob.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("must input Date of Birth");
            ssbuilder.setSpan(fgcspan, 0, "must input Date of Birth".length(), 0);
            et_dob.setError(ssbuilder);
            doberror.setText("must input Date of Birth");
            valid = false;
        }else if (diff<6570) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("DOB must be greater than 18 years");
            ssbuilder.setSpan(fgcspan, 0, "DOB must be greater than 18 years".length(), 0);
            et_dob.setError(ssbuilder);
            doberror.setText("DOB must be greater than 18 years");
            valid = false;
        }else {
            et_dob.setError(null);
            doberror.setText("");
        }


        return valid;
    }

    public boolean validate_bank() {
        boolean valid = true;
        int errorColor;
        final int version = Build.VERSION.SDK_INT;

        //Get the defined errorColor from color resource.
        if (version >= 23) {
            errorColor = ContextCompat.getColor(getApplicationContext(), R.color.errorColor);
        } else {
            errorColor = getResources().getColor(R.color.errorColor);
        }

        if (et_accountno.getText().toString().isEmpty()) {

            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("must input Account No");
            ssbuilder.setSpan(fgcspan, 0, "must input Account No".length(), 0);
            et_accountno.setError(ssbuilder);
            valid = false;
        } else {
            et_accountno.setError(null);
        }

        if (et_bankname == null && et_bankname.getSelectedItem() ==null) {

            TextView errorText = (TextView)et_bankname.getSelectedView();
            errorText.setError("must input Bank Name");
            errorText.setTextColor(errorColor);//just to highlight that this is an error

            valid = false;
        }
        if(!editEnabled)
        if (getBase64FromPathBank().equals("")) {
            Toast.makeText(MainActivity.this, "Must upload the bank ATM card image", Toast.LENGTH_LONG).show();
            valid = false;
        }


        return valid;
    }

    public boolean validate_doc() {
        boolean valid = true;
        int errorColor;
        final int version = Build.VERSION.SDK_INT;

        //Get the defined errorColor from color resource.
        if (version >= 23) {
            errorColor = ContextCompat.getColor(getApplicationContext(), R.color.errorColor);
        } else {
            errorColor = getResources().getColor(R.color.errorColor);
        }


        if (et_idno.getText().toString().isEmpty()) {
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder("must input ID Number");
            ssbuilder.setSpan(fgcspan, 0, "must input ID Number".length(), 0);
            et_idno.setError(ssbuilder);
            valid = false;
        } else {
            et_idno.setError(null);
        }

        if(!editEnabled)
        if (getBase64FromPath().equals("")) {
            Toast.makeText(MainActivity.this, "Must upload ID Document image ", Toast.LENGTH_LONG).show();
            valid = false;
        }



        return valid;
    }



    private void init() {
        progressDialog = new ProgressDialog(this);
        //EDITTEXT INSTANT
        et_firstName = findViewById(R.id.et_first_name);
        et_phoneno = findViewById(R.id.et_phoneno);
        et_idno = findViewById(R.id.et_idnumber);
        doberror = findViewById(R.id.doberror);
        et_dob = findViewById(R.id.et_dob);

        et_accountno = findViewById(R.id.et_account_no);
        et_bankname = findViewById(R.id.et_bankname);

        radioSexGroup = findViewById(R.id.radiogender);
        btn_finish = findViewById(R.id.btn_finish);

        iv_arrow_icon = findViewById(R.id.iv_arrow_icon);
        iv_bank_arrow_icon = findViewById(R.id.iv_bank_arrow_icon);
        iv_document_arrow_icon = findViewById(R.id.iv_document_arrow_icon);
        iv_image = findViewById(R.id.iv_image);
        iv_bank_image = findViewById(R.id.iv_bank_image);

        //FORM INSTANT
        lin_farmer_det = findViewById(R.id.lin_farmer_det);
        lin_farmer_bank_det = findViewById(R.id.lin_farmer_bank_det);
        lin_farmer_doc_det = findViewById(R.id.lin_farmer_doc_det);
        //Buttion instant
        btn_farmel_det_next = findViewById(R.id.btn_farmel_det_next);
        btn_farmel_bank_det_pre = findViewById(R.id.btn_farmel_bank_det_pre);
        btn_farmel_bank_det_next = findViewById(R.id.btn_farmel_bank_det_next);
        btn_farmel_doc_det_pre = findViewById(R.id.btn_farmel_doc_det_pre);
        //CARD VIEW INSTANT
        cv_farmer_det = findViewById(R.id.cv_farmer_det);
        cv_bank_det = findViewById(R.id.cv_bank_det);
        cv_document = findViewById(R.id.cv_document);

        centre_id = findViewById(R.id.centre_id);

        //IMAGE IVON INSTANT
        iv_arrow_icon.setOnClickListener(this);
        iv_bank_arrow_icon.setOnClickListener(this);
        iv_document_arrow_icon.setOnClickListener(this);
        iv_image.setOnClickListener(this);
        iv_bank_image.setOnClickListener(this);


        //BUTTION CLICK LISTENER
        btn_farmel_det_next.setOnClickListener(this);
        btn_farmel_bank_det_pre.setOnClickListener(this);
        btn_farmel_bank_det_next.setOnClickListener(this);
        btn_farmel_doc_det_pre.setOnClickListener(this);
        btn_finish.setOnClickListener(this);
        et_dob.setOnClickListener(this);

        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
            Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
            APIService service = retrofit.create(APIService.class);
            SharedPreferences prefs = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
            Set<String> permission = prefs.getStringSet("key", null);

            if(permission==null){
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return;
            }

            String auth_key = prefs.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
            Call<List<BankNameResponse>> call = service.getbankname(auth_key);
            call.enqueue(new Callback<List<BankNameResponse>>() {
                @Override
                public void onResponse(Call<List<BankNameResponse>> call, Response<List<BankNameResponse>> response) {
                    ArrayList<String> spinnerArray = new ArrayList<String>();
                    spinnerArray.clear();
                    bank_ids = new ArrayList<Integer>();
                    BankNameDB.deleteAll(BankNameDB.class);
                    for (BankNameResponse bank : response.body()) {
                        BankNameDB book = new BankNameDB(bank.getId(), bank.getBankname(), bank.getAccountformat());
                        book.save();
                        spinnerArray.add(bank.getBankname());
                        bank_ids.add(bank.getId());
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                    et_bankname.setAdapter(spinnerArrayAdapter);
                }

                @Override
                public void onFailure(Call<List<BankNameResponse>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            ArrayList<String> spinnerArray = new ArrayList<String>();
            spinnerArray.clear();
            bank_ids = new ArrayList<Integer>();

            List<BankNameDB> books = BankNameDB.listAll(BankNameDB.class);
            for (BankNameDB book : books) {
                spinnerArray.add(book.bankname_db);
                bank_ids.add(book.id_db);
            }
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
            et_bankname.setAdapter(spinnerArrayAdapter);
        }

        SharedPreferences prefs = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);

        Set<String> permission = prefs.getStringSet("key", null);

        if(permission==null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            return;
        }

        String center_names = prefs.getString("center_names", "");
        String center_ids = prefs.getString("center_ids", "");
        //centre_id
        String[] namesList = center_names.split(",");
        String[] center_idlist = center_ids.split(",");
        ArrayList<String> spinnerArray123 = new ArrayList<String>();
        spinnerArray123.clear();
        for (String name : namesList) {
            spinnerArray123.add(name);
            //System.out.println(name);
        }
        center_id_submit = new ArrayList<Integer>();

        for (String centre : center_idlist) {
            Log.d("Centre...",centre);
            center_id_submit.add(Integer.valueOf(centre));
        }
        ArrayAdapter<String> spinnerArrayAdapter123 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray123);
        centre_id.setAdapter(spinnerArrayAdapter123);
        if (namesList.length < 2) {
            centre_id.setVisibility(View.INVISIBLE);
        }


        //int selectedId = radioSexGroup.getCheckedRadioButtonId();
        //
        //			// find the radiobutton by returned id
        //		        radioSexButton = (RadioButton) findViewById(selectedId);



        et_bankname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //Toast.makeText(MainActivity.this, bank_ids.get(position).toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change_bottom_sheet_state_ID :
                //  Toast.makeText(MainActivity.this, String.valueOf(sheetBehavior.getState()), Toast.LENGTH_SHORT).show();
                //sheetBehavior.setPeekHeight(200);
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                if(sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){

                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    //sheetBehavior.setPeekHeight(200);
                    label_change_bottom_sheet_state_ID.setText("Hide Content for Edit");//4
                }else{
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);//3
                    label_change_bottom_sheet_state_ID.setText("Show Content for Edit");

                }
                break;

                // sheetBehavior.setState(sheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN ? BottomSheetBehavior.STATE_COLLAPSED : BottomSheetBehavior.STATE_HIDDEN);

            case R.id.iv_arrow_icon:
                if (lin_farmer_det.getVisibility() == View.VISIBLE) {
                    iv_arrow_icon.setImageResource(R.drawable.ic_expand);
                    lin_farmer_det.setVisibility(View.GONE);
                } else {
                    lin_farmer_det.setVisibility(View.VISIBLE);
                    iv_arrow_icon.setImageResource(R.drawable.ic_expand_less_black_24dp);
                }
                break;
            case R.id.iv_bank_arrow_icon:
                if (lin_farmer_bank_det.getVisibility() == View.VISIBLE) {
                    iv_bank_arrow_icon.setImageResource(R.drawable.ic_expand);
                    lin_farmer_bank_det.setVisibility(View.GONE);
                } else {
                    lin_farmer_bank_det.setVisibility(View.VISIBLE);
                    iv_bank_arrow_icon.setImageResource(R.drawable.ic_expand_less_black_24dp);
                }
                break;
            case R.id.iv_document_arrow_icon:
                if (lin_farmer_doc_det.getVisibility() == View.VISIBLE) {
                    iv_document_arrow_icon.setImageResource(R.drawable.ic_expand);
                    lin_farmer_doc_det.setVisibility(View.GONE);
                } else {
                    lin_farmer_doc_det.setVisibility(View.VISIBLE);
                    iv_document_arrow_icon.setImageResource(R.drawable.ic_expand_less_black_24dp);
                }
                break;

            case R.id.btn_farmel_det_next:
                    if (!validate()) {
                        Snackbar.make(v, "Correct the above errors first", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        break;
                    }
                cv_bank_det.setVisibility(View.VISIBLE);
                cv_farmer_det.setVisibility(View.GONE);
                cv_document.setVisibility(View.GONE);
                break;

            case R.id.btn_farmel_bank_det_pre:
                cv_bank_det.setVisibility(View.GONE);
                cv_farmer_det.setVisibility(View.VISIBLE);
                cv_document.setVisibility(View.GONE);
                break;
            case R.id.btn_farmel_bank_det_next:
                if (!validate_bank()) {
                    Snackbar.make(v, "Correct the above errors first", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    break;
                }
                cv_bank_det.setVisibility(View.GONE);
                cv_farmer_det.setVisibility(View.GONE);
                cv_document.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_farmel_doc_det_pre:
                cv_bank_det.setVisibility(View.VISIBLE);
                cv_farmer_det.setVisibility(View.GONE);
                cv_document.setVisibility(View.GONE);
                break;
            case R.id.iv_image:
                selectImage();
                break;
            case R.id.iv_bank_image:
                selectImageBank();
                break;
            case R.id.et_dob:
                openDatePicker();
                break;
            case R.id.btn_finish:
                if (!validate_doc()) {
                    Snackbar.make(v, "Correct the above errors first", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    break;
                }
                createFarmer(v);
                break;
        }
    }

    private void createFarmer(View view) {


        if(bank_ids.size() > 0){

        if(editEnabled){

            FarmerModelDB model = FarmerModelDB.findById(FarmerModelDB.class, editModel.getTableID());

            model.bankId = Integer.parseInt(bank_ids.get(et_bankname.getSelectedItemPosition()).toString());
            model.firstname = et_firstName.getText().toString().trim();
            model.mobileno = et_phoneno.getText().toString().trim();
            model.idno = et_idno.getText().toString().trim();
            model.dateOfBirth = et_dob.getText().toString().trim();
            model.accountno = et_accountno.getText().toString();
            model.docno_id = et_idno.getText().toString().trim();

            int selectedId = radioSexGroup.getCheckedRadioButtonId();
            radioSexButton = (RadioButton) findViewById(selectedId);
            String gender =radioSexButton.getText().toString();
            String genstr="M";

            if (gender.equals("Male")){
                genstr="M";
            }else{
                genstr="F";
            }
            model.gender = genstr;
            model.save();


            for(EditContentModel contentModel : editContentModelArrayList){
                if(editModel.getTableID() == contentModel.getTableID()){
                    contentModel.setStatus(true);
                }
            }

            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            CustomDialogAdapter adapter = new CustomDialogAdapter(MainActivity.this,editContentModelArrayList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")){
                Intent intent = new Intent(MainActivity.this, OfflineDataSyncActivity.class);
                startActivity(intent);
                finish();
            }else{
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Successful Edit")
                        .setContentText( editModel.getTitle() + " details have been edited and saved offline, will submit the details when you have an internet connection.")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                toggleBottomSheet();
                                //startActivity(new Intent(MainActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                            }
                        })
                        .show();
            }


        }else {
            SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Submitting...");
            pDialog.setCancelable(false);
            pDialog.show();

            int selectedId = radioSexGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            radioSexButton = (RadioButton) findViewById(selectedId);
            String gender =radioSexButton.getText().toString();
            String genstr="M";

            if (gender.equals("Male")){
                genstr="M";
            }else{
                genstr="F";
            }


            FarmerModel farmerModel = new FarmerModel();
            Accountdetails accountdetails = new Accountdetails();
            Identitydetails identitydetails = new Identitydetails();

            farmerModel.setFirstname(et_firstName.getText().toString().trim());
            farmerModel.setMobileno(et_phoneno.getText().toString().trim());
            farmerModel.setEmail("");
            farmerModel.setGender(genstr);
            farmerModel.getIdno(et_idno.getText().toString().trim());
            farmerModel.setDateOfBirth(et_dob.getText().toString().trim());
            farmerModel.setActivated("true");
            farmerModel.setCenterid(center_id_submit.get(centre_id.getSelectedItemPosition()));
            farmerModel.setDateFormat("DD-MM-YYYY");
            farmerModel.setLocale("en");
            accountdetails.setAccountno(et_accountno.getText().toString());

            //TODO: CRASHES OCCURED HERE
            accountdetails.setBankId(Integer.parseInt(bank_ids.get(et_bankname.getSelectedItemPosition()).toString()));
            accountdetails.setImage(getBase64FromPathBank());
            accountdetails.setFiletype("image/png");

            identitydetails.setDocId(1);
            identitydetails.setImage(getBase64FromPath());
            identitydetails.setFiletype("image/png");
            identitydetails.setDocno(et_idno.getText().toString());

            farmerModel.setAccountdetails(accountdetails);
            farmerModel.setIdentitydetails(identitydetails);

            if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("yes")) {
                Retrofit retrofit = ApiClient.getClient("/authentication/", getApplicationContext());
                APIService service = retrofit.create(APIService.class);
                SharedPreferences prefs = getSharedPreferences("PERMISSIONS", MODE_PRIVATE);
                String auth_key = prefs.getString("auth_key", "Basic YWRtaW46bWFudW5pdGVk");
                Call<FarmerErrorResponse> call = service.createFarmer(auth_key, farmerModel);
                call.enqueue(new Callback<FarmerErrorResponse>() {
                    @Override
                    public void onResponse(Call<FarmerErrorResponse> call, Response<FarmerErrorResponse> response) {
                        pDialog.cancel();
                        try {
                            if (response.body() != null) {
                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success")
                                        .setContentText("Farmer Details Registered Successfully")
                                        .setConfirmText("Ok")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                startActivity(new Intent(MainActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                            }
                                        })
                                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                startActivity(new Intent(MainActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                            }
                                        })
                                        .show();

                            } else {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Ooops...")
                                        .setContentText(jObjError.getJSONArray("errors").getJSONObject(0).get("developerMessage").toString())
                                        .show();

                            }

                            //Toast.makeText(MainActivity.this, response.body().getHttpStatusCode(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText(e.getMessage())
                                    .show();
                        }

                    }

                    @Override
                    public void onFailure(Call<FarmerErrorResponse> call, Throwable t) {
                        pDialog.cancel();
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText(t.getMessage())
                                .show();
                    }
                });
            } else {
                FarmerModelDB book = new FarmerModelDB(farmerModel.getFirstname(), farmerModel.getMobileno(), farmerModel.getEmail(), farmerModel.getGender(),
                        et_idno.getText().toString(), farmerModel.getDateOfBirth(), farmerModel.getActivated(), farmerModel.getCenterid(), farmerModel.getDateFormat(), farmerModel.getLocale(), accountdetails.getAccountno(), accountdetails.getBankId(), accountdetails.getImage(), accountdetails.getFiletype(), identitydetails.getDocId(), identitydetails.getImage(), identitydetails.getFiletype(), identitydetails.getDocno());
                book.save();
                pDialog.cancel();
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Confirmation")
                        .setContentText("We have saved the data offline, will submitted it when you have internet")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                startActivity(new Intent(MainActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                            }
                        })
                        .show();
            }
        }


        }else{
            Snackbar.make(view,"No data in Bank Drop Down", Snackbar.LENGTH_LONG  ).show();
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
    public String getBase64FromPathBank() {
        String base64 = "";
        try {/*from   w w w .  ja  va  2s  .  c om*/
            File file = bankImageFile;

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
                et_dob.setText(mDate);

            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
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
                    if (!hasPermissions(MainActivity.this, PERMISSIONS)) {
//                        boolean showRationale = shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA);
//                        if (!showRationale) {
//                            Intent intent = new Intent();
//                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(), null);
//                            intent.setData(uri);
//                            startActivity(intent);
//                        } else {
                        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
                        //  }


                    } else {
                        EasyImage.openCamera(MainActivity.this, 0);
                    }

                } else if (items[item].equals("Choose from Library")) {
                    if (!hasPermissions(MainActivity.this, PERMISSIONS)) {
//                        boolean showRationale = shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA);
//                        if (!showRationale) {
//                            Intent intent = new Intent();
//                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            Uri uri = Uri.fromParts("package", getPackageName(), null);
//                            intent.setData(uri);
//                            startActivity(intent);
//                        } else {
                        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
                        //}


                    } else {
                        EasyImage.openGallery(MainActivity.this, 0);
                    }

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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
                    if (!hasPermissions(MainActivity.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
                    } else {
                        EasyImage.openCamera(MainActivity.this, 1);
                    }

                } else if (items[item].equals("Choose from Library")) {
                    if (!hasPermissions(MainActivity.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
                    } else {
                        EasyImage.openGallery(MainActivity.this, 1);
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

            EasyImage.handleActivityResult(requestCode, resultCode, data, MainActivity.this, new EasyImage.Callbacks() {
                @Override
                public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onImagePicked(File imageFile, EasyImage.ImageSource source, final int type) {

                    new Compressor(MainActivity.this)
                            .compressToFileAsFlowable(imageFile)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new io.reactivex.functions.Consumer<File>() {
                                @Override
                                public void accept(File file) {
                                    if (type==0){
                                        compressedImageFile = file;
                                        iv_image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                        iv_image.setAdjustViewBounds(true);
                                        Glide.with(MainActivity.this).load(compressedImageFile).into(iv_image);
                                    }else{
                                        bankImageFile = file;
                                        iv_bank_image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                        iv_bank_image.setAdjustViewBounds(true);
                                        Glide.with(MainActivity.this).load(bankImageFile).into(iv_bank_image);
                                    }

                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) {
                                    throwable.printStackTrace();
                                    Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();


                                }
                            });
                }

                @Override
                public void onCanceled(EasyImage.ImageSource source, int type) {
                }
            });
        }
            super.onActivityResult(requestCode,resultCode,data);
    }
    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
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


}
