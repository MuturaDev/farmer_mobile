package com.cw.farmer.crashreporting;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cw.farmer.BuildConfig;
import com.cw.farmer.R;
import com.cw.farmer.activity.SplashScreen;
import com.cw.farmer.crashreporting.filepicker.FilePickerProvider;


import com.cw.farmer.utils.UtilityFunctions;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.cw.farmer.crashreporting.error.CatchoError;
import com.jakewharton.processphoenix.ProcessPhoenix;

import javax.mail.MessagingException;


/**
 * The type Catcho report activity.
 */
public class CatchoReportActivity extends AppCompatActivity implements View.OnClickListener {
    private CatchoError mError;
    private TextInputEditText title, description, deviceInfo, senderEmail;
    private CatchoTags.EmailMode emailMode;
    private String[] recipientEmail;
    private String smtpEmail, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_catcho_report);

        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(CatchoReportActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CatchoReportActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CatchoTags.REPORT, mError);
        outState.putString(CatchoTags.TITLE, title.getText().toString());
        outState.putString(CatchoTags.EMAIL, senderEmail.getText().toString());
        outState.putString(CatchoTags.DESCRIPTION, description.getText().toString());
        outState.putStringArray(CatchoTags.RECIPIENT_EMAIL, recipientEmail);
        outState.putString(CatchoTags.SMTP_EMAIL, smtpEmail);
        outState.putString(CatchoTags.PASSWORD, password);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mError = (CatchoError) savedInstanceState.getSerializable(CatchoTags.REPORT);
            deviceInfo.setText(savedInstanceState.getString(CatchoTags.REPORT));
            title.setText(savedInstanceState.getString(CatchoTags.TITLE));
            description.setText(savedInstanceState.getString(CatchoTags.DESCRIPTION));
            recipientEmail = savedInstanceState.getStringArray(CatchoTags.RECIPIENT_EMAIL);
            smtpEmail = savedInstanceState.getString(CatchoTags.SMTP_EMAIL);
            password = savedInstanceState.getString(CatchoTags.PASSWORD);
        }
    }

    private void init() {
        title =  findViewById(R.id.catcho_bug_title);
        description =  findViewById(R.id.catcho_bug_description);
        deviceInfo =  findViewById(R.id.catcho_device_info);
        senderEmail =  findViewById(R.id.catcho_email);
       final CardView sendButton =  findViewById(R.id.catcho_fab_send);
        if (sendButton != null) {
            sendButton.setOnClickListener(this);
        }


        new AsyncTask(){
            @Override
            protected void onPostExecute(Object o) {
                Intent intent = getIntent();
                if (intent != null) {
                    mError = (CatchoError) intent.getSerializableExtra(Catcho.ERROR);
                    mError.setAppName("Farmer App");
                    //mError.setUser(o.toString());
                    mError.setAppVersion((BuildConfig.VERSION_NAME));
                    emailMode = (CatchoTags.EmailMode) intent.getSerializableExtra(CatchoTags.EMAIL_MODE);
                    recipientEmail = intent.getStringArrayExtra(CatchoTags.RECIPIENT_EMAIL);
                    smtpEmail = intent.getStringExtra(CatchoTags.SMTP_EMAIL);
                    password = intent.getStringExtra(CatchoTags.PASSWORD);

                    deviceInfo.setText("Tap on the button 'SEND CRASH REPORT', to email the crash log file attached.");
                    //deviceInfo.setVisibility(View.GONE);
                    if(deviceInfo.getText().toString()==""){
                        //sendButton.setEnabled(false);
                        sendButton.setVisibility(View.GONE);
                    }

                }

                CardView restart = findViewById(R.id.restart);
                restart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //this not working still crashing
                       // UtilityFunctions.doRestart(CatchoReportActivity.this);
                       // ProcessPhoenix.triggerRebirth(getApplicationContext());
                        Intent nextIntent = new Intent(getApplicationContext(), SplashScreen.class);
                       // ProcessPhoenix.triggerRebirth(getApplicationContext(), nextIntent);
                        startActivity(nextIntent);
                        finish();
                    }
                });
                super.onPostExecute(o);
            }

            @Override
            protected Object doInBackground(Object[] objects) {

                //TODO: GET CREDENTIALS

                return null;
            }
        }.execute();

    }

    @Override
    public void onClick(View view) {
        switch (emailMode) {
            case DEFAULT:
                sendEmailViaIntent();

                break;
            case G_MAIL_SENDER:
                sendEmailViaSMTP();
                break;
        }
        //SendLogcatMail();
    }

    public static boolean isExternalStorageMounted(){
        String dirState = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(dirState)){
            return true;
        }else{
            return false;
        }
    }

    public static String getPrivateExternalStorageBaseDir(Context context, String dirType){


        String ret = "";
        if(isExternalStorageMounted()){
            File file = context.getExternalFilesDir(dirType);
            ret = file.getAbsolutePath();
        }
        return ret;
    }



    private String writeLogFile(String text){
        Log.d(getPackageName().toUpperCase(), "fIle Path" + text);
        String returnpath = "";
        try{
            if(isExternalStorageMounted()){
                 int writeExternalStoragePermission = ContextCompat.checkSelfPermission(CatchoReportActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                 if(writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED){
                     ActivityCompat.requestPermissions(CatchoReportActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                 }else{
                     String privateDirPath = getPrivateExternalStorageBaseDir(getApplicationContext(),null);
                     File newFile = new File(privateDirPath, "farmer_app_crash_log.txt");
                     FileWriter fw = new FileWriter(newFile);
                     fw.write(mError.toString());
                     fw.flush();
                     fw.close();

                     returnpath = newFile.getAbsolutePath();
                 }
            }
        }catch (Exception ex){
            return returnpath;
        }

        return returnpath;
    }


    private void sendEmailViaIntent() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, recipientEmail);
        i.putExtra(Intent.EXTRA_SUBJECT, title.getText().toString());



        i.setType("text/plain");
        String filepath = writeLogFile(mError.toString());





        if(!filepath.isEmpty()) {
            File logFile = new File(writeLogFile(filepath));

//        Log.d(TAG, "file:"+logFile.getAbsolutePath());
//        Log.d(TAG, "exists?" + (logFile.exists()?"true":"false"));
//        Log.d(TAG, "canRead?" + (logFile.canRead()?"true":"false"));

            Log.d(getPackageName().toUpperCase(), "can read" + logFile.canRead());

            if (logFile.canRead()) {
                i.putExtra(Intent.EXTRA_TEXT, "Kindly send the file, to report the crash that just occurred.");
                //Uri uri = Uri.fromFile(logFile);
                i.putExtra(Intent.EXTRA_STREAM, FilePickerProvider.getUriForFile(this, logFile));
            } else {
                i.putExtra(Intent.EXTRA_TEXT, "Nothing logged.  No file to send.");
            }

        }else{
            i.putExtra(Intent.EXTRA_TEXT, description.getText() + "\n" + mError);
        }


        try {
            startActivity(Intent.createChooser(i, getResources().getString(R.string.catcho_send_email)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CatchoReportActivity.this, getResources().getText(R.string.catcho_no_email_clients), Toast.LENGTH_SHORT).show();
        }
    }

    private  void SendLogcatMail(){
        shareLogFile();
    }

    public void shareLogFile() {
        File logFile = new File(getExternalCacheDir(), "logcat.txt");
        try {
            if (logFile.exists())
                logFile.delete();
            logFile.createNewFile();
            Runtime.getRuntime().exec("logcat -f " + logFile.getAbsolutePath() + " -t 100 *:W Glide:S " + getApplicationContext().getPackageName().toLowerCase());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (logFile.exists()) {
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            intentShareFile.setType("text/plain");
            intentShareFile.putExtra(Intent.EXTRA_STREAM,
                    FilePickerProvider.getUriForFile(this, logFile));
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "FilePicker Log File");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "FilePicker Log File");
            startActivity(Intent.createChooser(intentShareFile, "Share"));
        }
    }

    private void sendEmailViaSMTP() {

        try {
            GMailSender gMailSender = new GMailSender(smtpEmail, password);
            gMailSender.sendMail(title.getText().toString(), description.getText() + "\n" + mError, senderEmail.getText().toString(), recipientEmail);

        } catch (MessagingException e) {
            Toast.makeText(CatchoReportActivity.this, getResources().getText(R.string.catcho_err_send_msg), Toast.LENGTH_SHORT).show();
        }


    }


    public static Intent getCallingIntent(Context context, CatchoTags.EmailMode emailMode, String[] recipientEmail, String smtpEmail, String password, CatchoError errorReport) {
        Intent intent = new Intent(context, CatchoReportActivity.class);
        intent.putExtra(CatchoTags.EMAIL_MODE, emailMode);
        intent.putExtra(CatchoTags.RECIPIENT_EMAIL, recipientEmail);
        intent.putExtra(CatchoTags.SMTP_EMAIL, smtpEmail);
        intent.putExtra(CatchoTags.PASSWORD, password);
        intent.putExtra(Catcho.ERROR, errorReport);
        return intent;
    }
}