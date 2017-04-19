package com.baidu.rxandroidtaste;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

public class DrawableActivity extends AppCompatActivity {

    private ImageView mImageView;
    private int toggle;
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mImageView.setScaleX(0f);
        mImageView.setScaleY(0f);
        mImageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                mImageView.animate().scaleY(1f).scaleX(1f).setDuration(500);
                return true;
            }
        });
    }

    private Runnable changeImageRunnable = new Runnable() {
        @Override
        public void run() {
            if(toggle == Integer.MAX_VALUE){
                toggle = 0;
            }
            if (toggle % 2 == 0){
                mImageView.setImageResource(R.drawable.battery_body_dark);
            } else {
                mImageView.setImageResource(R.drawable.battery_body_light);
            }
            toggle ++;
            mHandler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
//        mHandler.post(changeImageRunnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        mHandler.removeCallbacksAndMessages(null);
    }
}
