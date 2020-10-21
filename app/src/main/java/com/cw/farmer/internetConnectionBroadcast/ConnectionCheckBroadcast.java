package com.cw.farmer.internetConnectionBroadcast;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.core.app.NotificationCompat;

import com.cw.farmer.R;
import com.cw.farmer.utils.OfflineFeature;


public class ConnectionCheckBroadcast extends BroadcastReceiver {

    Context context;


    @Override
    public void onReceive(Context context, Intent intent) {
        this.context =context;
        if (CheckInternetConnection(context)){
            if(OfflineFeature.hasAnyData()){
                //new NotificationNotify(context).displayNotification("Farmer App, connected to the internet", "You are tasked with syncing the data to the online servers.","Proceed by clicking on \"OPEN TO SYNC\" for further instructions");
            }
        }else {
            new NotificationNotify(context).cancelNotificationDisplay();
           // sendNotification("Check Internet","You are Not Connected to Internet From Lollipop+");
        }
    }

    public static boolean CheckInternetConnection(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private void sendNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //If on Oreo then notification required a notification channel.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(context, InternetMainActivity.class);
        resultIntent.putExtra("FromNotification", true);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "default")
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(1, notification.build());
    }
}