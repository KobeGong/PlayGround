package com.baidu.rxandroidtaste;

import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.lang.Process;

public class ImageDetailActivity extends AppCompatActivity {

    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        image = (ImageView) findViewById(R.id.image);
    }
}
