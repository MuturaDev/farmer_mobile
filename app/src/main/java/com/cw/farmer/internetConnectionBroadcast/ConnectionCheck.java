package com.cw.farmer.internetConnectionBroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cw.farmer.utils.OfflineFeature;


public class ConnectionCheck extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        final String action = intent.getAction();
        switch (action) {
            case ConnectivityManager.CONNECTIVITY_ACTION:
                ConnectivityManager connMgr = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    if(OfflineFeature.hasAnyData()){
                        //new NotificationNotify(context).displayNotification("Farmer App, connected to the internet", "You are tasked with syncing the data to the online servers.","Proceed by clicking on \"OPEN TO SYNC\" for further instructions");
                    }
                }else {
                    new NotificationNotify(context).cancelNotificationDisplay();
                    // sendNotification("Check Internet","You are Not Connected to Internet From Lollipop+");
                }
                break;
        }

    }


}