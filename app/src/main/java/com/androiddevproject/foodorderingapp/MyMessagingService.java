package com.androiddevproject.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"MyNotifications")
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setSmallIcon(R.drawable.icon)
                .setAutoCancel(true)
                .setContentText(remoteMessage.getNotification().getBody());
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(999,builder.build());
    }
}
