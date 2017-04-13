package com.gyansagarji.android.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.gyansagarji.android.Activity.EventsActivity;
import com.gyansagarji.android.Activity.MapsActivity;
import com.gyansagarji.android.Activity.NewsActivity;
import com.gyansagarji.android.Activity.SplashScreen;
import com.gyansagarji.android.R;

import java.util.Random;

/**
 * Created by New android on 27-02-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if(remoteMessage.getData().size()>0){
            Gson gson = new Gson();
            String remotedata = gson.toJson(remoteMessage.getData());

            sendNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("message"),"");
        }else if(remoteMessage.getNotification()!=null){
            Gson gson = new Gson();
            String remotedata = gson.toJson(remoteMessage.getNotification());

            sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getClickAction());
        }
    }

    private void sendNotification(String title, String message,String type) {
        Intent intent = null;
        if(type.equalsIgnoreCase("News")) {
             intent = new Intent(this, NewsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else if(type.equalsIgnoreCase("Event")){
            intent = new Intent(this, EventsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else if(type.equalsIgnoreCase("Location")){

                intent = new Intent(this, MapsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        }else{
            intent = new Intent(this, SplashScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultsoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.launcher_ic)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultsoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        int num = random.nextInt(99999-1000)+1000;
        notificationManager.notify(num,notificationBuilder.build());

    }


}
