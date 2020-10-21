package com.cw.farmer.internetConnectionBroadcast;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.cw.farmer.R;
import com.cw.farmer.activity.HomeActivity;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION.SDK_INT;


public class NotificationNotify {


    static List<Integer> notificationIdHolder = new ArrayList<>();//for deleting purposes
    private NotificationManager notifyMgr;
    private  NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 234;

    private Context mContext;



    public NotificationNotify(Context context){
        this.mContext = context;
    }


    //You are connected to the internet...hence you are tasked with syncing the data to the online servers.
    public void displayNotification(String title, String message, String subMessage){

        if(SDK_INT>= Build.VERSION_CODES.O) {

            String CHANNEL_ID = "my_farmer_01";
            CharSequence name = "my_farmer";
            String Description = "This is my farmer";

            notificationManager = (NotificationManager) mContext.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(true);

            if (notificationManager != null) {

                notificationManager.createNotificationChannel(mChannel);
            }


            Intent toPIntent = new Intent(mContext, HomeActivity.class);
            toPIntent.putExtra("FromNotification", true);


            TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
            stackBuilder.addNextIntentWithParentStack(toPIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE);

//            NotificationCompat.InboxStyle add = new NotificationCompat.InboxStyle();
//            add.addLine(message);
//            //add.addLine(subMessage);
//            add.setSummaryText("Sync offline data...");


            //To set large icon in notification

            //Assign BigText style notification
            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.bigText(message + ".\n\n" + subMessage + ".");
            bigText.setSummaryText("Sync offline data...");

            Uri sounduri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setStyle(bigText)
                    .setSmallIcon(R.mipmap.ic_farmer_logo_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_farmer_logo_icon))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(resultPendingIntent)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setSound(sounduri)
                    .setShowWhen(true)
                    .setWhen(System.currentTimeMillis())
                    .setColor(mContext.getResources().getColor(android.R.color.holo_green_dark))
                    .addAction(R.mipmap.ic_farmer_logo_icon, "Open to sync", resultPendingIntent);
//                        .addAction(R.mipmap.ic_launcher, "More", resultPendingIntent)
//                        .addAction(R.mipmap.ic_launcher, "And more", resultPendingIntent);

            if (notificationManager != null) {

                notificationManager.notify(NOTIFICATION_ID, builder.build());
            }


        }else{



            final  String GROUP_KEY_NOTIFY = "group_key_notify";

            Uri sounduri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            Intent toPIntent = new Intent(mContext, HomeActivity.class);
            toPIntent.putExtra("FromNotification", true);


            // Create the TaskStackBuilder and add the intent, which inflates the back stack
            android.app.TaskStackBuilder stackBuilder = android.app.TaskStackBuilder.create(mContext);
            stackBuilder.addNextIntentWithParentStack(toPIntent);
            // Get the PendingIntent containing the entire back stack
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


          //  PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1251, toPIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            int summaryNotificaitonId = 9961;
            NotificationCompat.Builder builderSummary =
                    new NotificationCompat.Builder(mContext, String.valueOf(summaryNotificaitonId))
                            .setSmallIcon(R.mipmap.ic_farmer_logo_icon)
                            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_farmer_logo_icon))
                            .setContentTitle(title)
                            .setContentText(message)
                            //.setSubText(subMessage)
                            .setSound(sounduri)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setAutoCancel(false)
                            .setOngoing(true)
                            .setGroup(GROUP_KEY_NOTIFY)
                            .setContentIntent(resultPendingIntent)
                            .setGroupSummary(true);


            int count_notification_id = 0;

            notifyMgr = (NotificationManager) mContext.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            int notificationId = 100;
            NotificationCompat.Builder builder;


            int countToNotifySoundUri = 0;


            builder= new NotificationCompat.Builder(mContext, String.valueOf(count_notification_id))
                    .setSmallIcon(R.mipmap.ic_farmer_logo_icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSubText(subMessage)
                    //The notification will be cleared if the application is uninstalled or by calling Cancel or CancelAll:
                    .setAutoCancel(false)
                    .setOngoing(true)//The setOngoing(true) call achieves this, and setAutoCancel(false) stops the notification from going away when the user taps the notification.
                    .setContentIntent(resultPendingIntent)
                    .setGroup(GROUP_KEY_NOTIFY);

            if(countToNotifySoundUri == 1) {
                builder.setSound(sounduri);
            }

            notifyMgr.notify(notificationId, builder.build());

            count_notification_id++;


            Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(1000);

            notificationIdHolder.add(summaryNotificaitonId);
            notifyMgr.notify(summaryNotificaitonId, builderSummary.build());


        }
    }


    public void cancelNotificationDisplay() {
        NotificationManager notify = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notify.cancelAll();
    }

}
