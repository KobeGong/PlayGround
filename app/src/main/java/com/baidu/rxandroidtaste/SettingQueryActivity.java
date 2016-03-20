package com.baidu.rxandroidtaste;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SettingQueryActivity extends AppCompatActivity {

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_query);
        text = (TextView) findViewById(R.id.text);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String queryResult = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
                text.setText(queryResult);
            }
        });

        logIsLoggable("NotificationListeners");
        logIsLoggable("ManagedServices");
        logIsLoggable("NotificationService");

        try {
            Class cls = Class.forName("com.android.server.notification.ManagedServices");
            String name = cls.getSimpleName();
            Log.d("ggf", "simpleName = " + name);
        } catch (ClassNotFoundException e) {
            Log.d("ggf", e.toString());
        }

        Log.d("ggf", "this.simpleName = " + getClass().getSimpleName());

    }

    private void logIsLoggable(String tag) {
        boolean b = Log.isLoggable(tag, Log.DEBUG);
        Log.d("ggf", tag + " = " + b);
    }
}
