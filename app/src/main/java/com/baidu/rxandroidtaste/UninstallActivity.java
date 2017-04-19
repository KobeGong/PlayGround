package com.baidu.rxandroidtaste;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UninstallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uninstall);
        final EditText packageText = (EditText) findViewById(R.id.edit);
        Button button = (Button) findViewById(R.id.button3);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String packageName = packageText.getText().toString();
                if (TextUtils.isEmpty(packageName)) {
                    Toast.makeText(UninstallActivity.this, "input package name is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                PackageManager packageManager = getPackageManager();
                try {
                    Method deletePackageMethod = PackageManager.class.getDeclaredMethod(
                            "deletePackage",
                            String.class,
                            Class.forName("android.content.pm.IPackageDeleteObserver"),
                            int.class);
                    deletePackageMethod.setAccessible(true);
                    deletePackageMethod.invoke(packageManager, "", null, 1);
                    Toast.makeText(UninstallActivity.this, "uninstall success", Toast.LENGTH_SHORT).show();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
