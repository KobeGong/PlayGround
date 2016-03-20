package com.baidu.rxandroidtaste;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class ImageActivity extends AppCompatActivity {

    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        image = (ImageView) findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ImageActivity.this, ImageDetailActivity.class);
                startActivity(in, ActivityOptions.makeSceneTransitionAnimation(ImageActivity.this,
                        image, "image").toBundle());
            }
        });
        logName();
    }

    private String getName(){
        return getClass().getSimpleName();
    }

    private void logName(){
        log(getName());
    }

    private void log(String name){
        Log.d("gonggaofeng", "name = "+name);
    }

}
