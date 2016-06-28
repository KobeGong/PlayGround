package com.baidu.rxandroidtaste;

import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PackageSizeActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_size);
        textView = (TextView) findViewById(R.id.text);
        PackageManager pm = getPackageManager();
        String methodName = "getPackageSizeInfo";
        try {
            Method method = pm.getClass().getMethod(methodName, String.class, IPackageStatsObserver.class);
            method.setAccessible(true);
            method.invoke(pm, "com.facebook.katana", new PackageStatsObserver());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private class PackageStatsObserver extends IPackageStatsObserver.Stub {

        @Override
        public void onGetStatsCompleted(final PackageStats pStats, boolean succeeded) throws RemoteException {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("facebook use " + pStats.dataSize);
                }
            });
        }
    }
}
