package com.baidu.rxandroidtaste;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LifeCycleActivity extends AppCompatActivity {

    public static final String TAG = "lifeCycle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_cycle);
        getFragmentManager().beginTransaction()
                .add(R.id.container, new ImageFragment(), "image")
                .commit();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "LifeCycleActivity onRestart() called");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "LifeCycleActivity onStart() called");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "LifeCycleActivity onStop() called");
        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "LifeCycleActivity onResume() called");
        super.onResume();
    }

    public static class ImageFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.image_fitxy, container, false);
        }
    }
}
