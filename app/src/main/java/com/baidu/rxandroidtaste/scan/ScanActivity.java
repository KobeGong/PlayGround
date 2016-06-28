package com.baidu.rxandroidtaste.scan;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import com.baidu.rxandroidtaste.R;

public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        getWindow().getDecorView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getWindow().getDecorView().getViewTreeObserver().removeOnPreDrawListener(this);
                WifiView wifiView = (WifiView) findViewById(R.id.wifiView);
                ObjectAnimator powerAnimator = ObjectAnimator.ofInt(wifiView, "wifiPower", 1, 4);
                powerAnimator.setDuration(2000L);
                powerAnimator.setRepeatCount(ObjectAnimator.INFINITE);
                powerAnimator.setRepeatMode(ObjectAnimator.RESTART);
                powerAnimator.start();
                return true;
            }
        });
    }
}
