package com.baidu.rxandroidtaste;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;

public class ReferrerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referer);
        View button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent("com.android.vending.INSTALL_REFERRER");
                in.setPackage("com.duapps.antivirus");
                in.putExtra("referrer", "asdf");
                ReferrerActivity.this.sendBroadcast(in);
            }
        });
    }
}
