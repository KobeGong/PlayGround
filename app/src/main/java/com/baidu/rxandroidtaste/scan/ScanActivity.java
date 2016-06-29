package com.baidu.rxandroidtaste.scan;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.baidu.rxandroidtaste.R;

public class ScanActivity extends AppCompatActivity {

    private ImageView bigRing;
    private WifiView wifiView;
    private SweepView sweepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        bigRing = (ImageView) findViewById(R.id.bigRing);
        wifiView = (WifiView) findViewById(R.id.wifiView);
        sweepView = (SweepView) findViewById(R.id.sweepView);
        bigRing.setAlpha(0F);
        sweepView.setAlpha(0F);
        getWindow().getDecorView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getWindow().getDecorView().getViewTreeObserver().removeOnPreDrawListener(this);

                ObjectAnimator bigRingAlpha = ObjectAnimator.ofFloat(bigRing, "alpha", 0F, 1F);
                bigRingAlpha.setDuration(1000L);

                ObjectAnimator wifiScaleX = ObjectAnimator.ofFloat(wifiView, "scaleX", 0F, 1F);
                wifiScaleX.setDuration(1000L);
                ObjectAnimator wifiScaleY = ObjectAnimator.ofFloat(wifiView, "scaleY", 0F, 1F);
                wifiScaleY.setDuration(1000L);

                ObjectAnimator powerAnimator = ObjectAnimator.ofInt(wifiView, "wifiPower", 1, 4);
                powerAnimator.setDuration(2000L);
                powerAnimator.setRepeatCount(ObjectAnimator.INFINITE);
                powerAnimator.setRepeatMode(ObjectAnimator.RESTART);

                ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(sweepView, "alpha", 0F, 1F);
                alphaAnimator.setDuration(1000L);

                ObjectAnimator sweepAnimator = ObjectAnimator.ofFloat(sweepView, "rotation", 0, 360);
                sweepAnimator.setDuration(2000L);
                sweepAnimator.setInterpolator(new LinearInterpolator());
                sweepAnimator.setRepeatCount(ObjectAnimator.INFINITE);
                sweepAnimator.setRepeatMode(ObjectAnimator.RESTART);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(bigRingAlpha).with(wifiScaleX).with(wifiScaleY).before(alphaAnimator).before(sweepAnimator);
                animatorSet.play(sweepAnimator).with(powerAnimator);
//                .with(powerAnimator);
                animatorSet.start();
                return true;
            }
        });
    }
}
