package com.example.smistry.woke;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class WokeApp extends Application{
    public static final String Channel_1_ID = "channel1";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }


    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel1 = new NotificationChannel(Channel_1_ID,
                    "Bring Jacket", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("You should bring a jacket today!");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }


    }


}

