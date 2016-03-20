package com.baidu.rxandroidtaste;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.security.Security;

public class NotificationManageActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    private int notificationId;
    private NotificationManager mNotificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_manage);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                Intent intent1 = new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS);
                startActivity(intent1);
                break;
            case R.id.button2:
                Notification.Builder builder = new Notification.Builder(this);
                Notification notification = builder.setSmallIcon(R.mipmap.ic_launcher)
                        .setContentText("Content Content content")
                        .setContentTitle("title")
                        .build();
                mNotificationManager.notify(notificationId++, notification);

                break;
        }
    }

}
