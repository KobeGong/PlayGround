package com.baidu.rxandroidtaste;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import rx.functions.Action1;

import static android.content.pm.PackageManager.GET_SIGNATURES;
import static com.google.android.gms.internal.a.T;

public class LaunchActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<ResolveInfo>> {

    private static final String QUERY_CATEGORY = "com.baidu.activity.main";
    @Bind(R.id.list) ListView mListView;
    private static final int LOADER_ACTIVITIES = 0;
    private ActivityAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);
        mAdapter = new ActivityAdapter(this);
        mListView.setAdapter(mAdapter);
        getSupportLoaderManager().initLoader(LOADER_ACTIVITIES, null, this);
        registerReceiver();

        getSignTag();

    }

    public void getSignTag() {

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.duapps.antivirus", GET_SIGNATURES);
            Signature[] signatures = packageInfo.signatures;
            for (Signature signature : signatures) {
                Log.d("gonggaofeng", "signature = " + signature.toCharsString());
                Log.d("gonggaofeng", "--------");
            }
            if (signatures != null && signatures.length > 0) {
                // Only check the first signature
                String signatureTag;
                String checksum = getMD5Checksum(signatures[0].toByteArray(), Character.MAX_RADIX);
                Log.d("gonggaofeng", "checkSum = " + checksum);
                if (checksum != null) {
                    if ("1qiucpl9pw0ol4l4ae5pupuhn".equals(checksum)) {
                        signatureTag = "r1";
                    } else if ("3ya3kmvchr0sy572vwothpb3c".equals(checksum)) {
                        signatureTag = "r2";
                    } else if ("4xdm7oblwioop9e9sihzdiceo".equals(checksum)) {
                        signatureTag = "d1";
                    } else if ("6q2f72pbvsundumlbh8qo54ta".equals(checksum)) {
                        signatureTag = "d2";
                    } else {
                        signatureTag = checksum.substring(0, 5);
                    }
                    Log.d("gonggaofeng", "signTag = " + signatureTag);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "packageName not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * @return May be null
     */
    public static String getMD5Checksum(byte[] data, int radix) {
        String result = null;

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(data);
            BigInteger big = new BigInteger(md5.digest()).abs();
            return big.toString(radix);  // radix: 36
        } catch (Exception e) {
        }

        return result;
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_RESTARTED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @OnItemClick(R.id.list)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ResolveInfo resolveInfo = mAdapter.getItem(position);
        Intent in = new Intent();
        in.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
        startActivity(in);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public Loader<List<ResolveInfo>> onCreateLoader(int id, Bundle args) {
        Loader<List<ResolveInfo>> loader = null;
        if (id == LOADER_ACTIVITIES) {
            loader = new ActivityLoader(this);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<ResolveInfo>> loader, List<ResolveInfo> data) {
        mAdapter.clear();
        if (data != null) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<ResolveInfo>> loader) {
        mAdapter.clear();
    }

    private static class ActivityLoader extends AsyncTaskLoader<List<ResolveInfo>> {


        public ActivityLoader(Context context) {
            super(context);
        }

        @Override
        public List<ResolveInfo> loadInBackground() {
            PackageManager pm = getContext().getPackageManager();
            Intent in = new Intent();
            in.setAction(Intent.ACTION_MAIN);
            in.addCategory(QUERY_CATEGORY);
            return pm.queryIntentActivities(in, PackageManager.GET_ACTIVITIES);
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

    }

    private class ActivityAdapter extends ArrayAdapter<ResolveInfo>{
        private PackageManager pm;
        public ActivityAdapter(Context context) {
            super(context, 0);
            pm = context.getPackageManager();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            TextView text;

            if (convertView == null) {
                view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            try {
                text = (TextView) view;
            } catch (ClassCastException e) {
                Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
                throw new IllegalStateException(
                        "ArrayAdapter requires the resource ID to be a TextView", e);
            }
            ResolveInfo item = getItem(position);
            text.setText(item.activityInfo.loadLabel(pm));

            return view;
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String packageName = intent.getStringExtra("package");
            String content = "action: " + action + ", package: " + packageName;;
            if (action.equals(Intent.ACTION_PACKAGE_CHANGED)) {
                ArrayList<String> list = intent.getStringArrayListExtra(Intent.EXTRA_CHANGED_COMPONENT_NAME_LIST);
                content = content + ", list: " + list.toString();
                Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
        }
    };
}
