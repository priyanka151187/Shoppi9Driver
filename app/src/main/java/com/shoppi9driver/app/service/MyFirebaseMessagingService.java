package com.shoppi9driver.app.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.shoppi9driver.app.MainActivity;
import com.shoppi9driver.app.R;
import com.shoppi9driver.app.utility.MyApplication;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    MyApplication mApplication;
    NotificationManager mNotificationManager;
    NotificationChannel mChannel = null;
    int importance;
    NotificationCompat.Builder builder;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        mApplication=(MyApplication) getApplication();
        String idChannel = "Shoppi9Driver";
        Intent mainIntent;
        if(remoteMessage.getNotification().getBody()!=null)
        {
           // mApplication.getPreferences().setString("orderNo",ordernumber);
            mainIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);
            mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            importance = NotificationManager.IMPORTANCE_HIGH;
            builder = new NotificationCompat.Builder(this, idChannel);
            builder.setContentTitle(remoteMessage.getNotification().getBody())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setContentText("");
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            mChannel = new NotificationChannel(idChannel, this.getString(R.string.app_name), importance);
            // Configure the notification channel.
            mChannel.setDescription(this.getString(R.string.app_name));
            mChannel.enableLights(true);
            mChannel.setLightColor(getResources().getColor(R.color.black));
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            mNotificationManager.createNotificationChannel(mChannel);
        } else
        {
            builder.setContentTitle(remoteMessage.getNotification().getBody()
            )
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(this, R.color.black))
                    .setVibrate(new long[]{100, 250})
                    .setLights(Color.YELLOW, 500, 5000)
                    .setAutoCancel(true);
        }
        mNotificationManager.notify(1, builder.build());

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        mApplication=(MyApplication) getApplication();
        mApplication.getPreferences().setToken("PREFERENCE_DEVICE_TOKEN",s);
        Log.e("TOKEN", s);
    }
    public String drawDigitsFromString(String strValue){
        String str = strValue.trim();
        String digits="";
        for (int i = 0; i < str.length(); i++) {
            char chrs = str.charAt(i);
            if (Character.isDigit(chrs))
                digits = digits+chrs;
        }
        return digits;
    }

}
