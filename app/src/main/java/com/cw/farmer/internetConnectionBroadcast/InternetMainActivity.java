package com.cw.farmer.internetConnectionBroadcast;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cw.farmer.R;




/*Notify when device gets connected to the Internet from Background.
Notify when device gets connected to internet in android pie and lower version.

For Higher version:-
We have to use JobScheduler because android.net.conn.CONNECTIVITY_CHANGE is deprecated.

For Lower version:-
We are using android.net.conn.CONNECTIVITY_CHANGE BroadcastReceiver.*/
public class InternetMainActivity extends AppCompatActivity {


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.internet_main_activity_layout);

        if(getIntent() != null){
            if(getIntent().getExtras() != null){
                if(getIntent().getExtras().getBoolean("FromNotification")){
                    new NotificationNotify(this).cancelNotificationDisplay();
                }else{
                    startJob();
                }
            }else{
                startJob();
            }
        }else{
            startJob();
        }
    }


    void startJob(){
        JobInfo myJob = new JobInfo.Builder(0, new ComponentName(this, CheckNetworkJob.class))
                .setMinimumLatency(1000)
                .setOverrideDeadline(2000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(myJob);
    }

}

