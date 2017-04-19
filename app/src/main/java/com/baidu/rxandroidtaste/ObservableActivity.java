package com.baidu.rxandroidtaste;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ObservableActivity extends AppCompatActivity {

    private static final String url = "http://gank.avosapps.com/api/day/2015/08/06";
    @Bind(R.id.text) TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Observable.create(new Observable.OnSubscribe<String>(){

            @Override
            public void call(Subscriber<? super String> subscriber) {
                String result = null;
                try {
                    URLConnection conn = new URL(url).openConnection();
                    InputStream in = conn.getInputStream();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    int b;
                    while((b = in.read()) != -1){
                        out.write(b);
                    }
                    result = new String(out.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                subscriber.onNext(result);
            }

        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mTextView.setText(s);
                    }
                });

        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                List<ResolveInfo> searchList;
                final Intent intent = new Intent(Intent.ACTION_SEARCH);
                PackageManager pm = getPackageManager();
                long ident = Binder.clearCallingIdentity();
                try {
                    searchList = pm.queryIntentActivities(intent, PackageManager.GET_META_DATA);
                    List<ResolveInfo> webSearchInfoList;
                    final Intent webSearchIntent = new Intent(Intent.ACTION_WEB_SEARCH);
                    webSearchInfoList = pm.queryIntentActivities(webSearchIntent, PackageManager.GET_META_DATA);

                    // analyze each one, generate a Searchables record, and record
                    StringWriter sw = new StringWriter();
                    if (searchList != null || webSearchInfoList != null) {
                        int search_count = (searchList == null ? 0 : searchList.size());
                        int web_search_count = (webSearchInfoList == null ? 0 : webSearchInfoList.size());
                        int count = search_count + web_search_count;
                        for (int ii = 0; ii < count; ii++) {
                            // for each component, try to find metadata
                            assert searchList != null;
                            assert webSearchInfoList != null;
                            ResolveInfo info = (ii < search_count)
                                    ? searchList.get(ii)
                                    : webSearchInfoList.get(ii - search_count);
                            ActivityInfo ai = info.activityInfo;
                            PrintWriter pw = new PrintWriter(sw);
                            pw.println(ai.packageName + "/" + ai.name);
                        }
                    }

                    subscriber.onNext(sw.getBuffer().toString());
                } finally {
                    Binder.restoreCallingIdentity(ident);
                }

                ComponentName cn = findGlobalSearchActivity(findGlobalSearchActivities());
                Log.d("ggf", "cn = " + cn.flattenToString());

            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mTextView.append(s);
            }
        });
    }

    private List<ResolveInfo> findGlobalSearchActivities() {
        // Step 1 : Query the package manager for a list
        // of activities that can handle the GLOBAL_SEARCH intent.
        Intent intent = new Intent(SearchManager.INTENT_ACTION_GLOBAL_SEARCH);
        List<ResolveInfo> activities =
                getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (activities != null && !activities.isEmpty()) {
            // Step 2: Rank matching activities according to our heuristics.
            Collections.sort(activities, GLOBAL_SEARCH_RANKER);
        }

        return activities;
    }

    /**
     * Finds the global search activity.
     */
    private ComponentName findGlobalSearchActivity(List<ResolveInfo> installed) {
        // Fetch the global search provider from the system settings,
        // and if it's still installed, return it.
        final String searchProviderSetting = getGlobalSearchProviderSetting();
        Log.d("ggf", "search setting is " + searchProviderSetting);
        if (!TextUtils.isEmpty(searchProviderSetting)) {
            final ComponentName globalSearchComponent = ComponentName.unflattenFromString(
                    searchProviderSetting);
            if (globalSearchComponent != null && isInstalled(globalSearchComponent)) {
                return globalSearchComponent;
            }
        }

        return getDefaultGlobalSearchProvider(installed);
    }

    private String getGlobalSearchProviderSetting() {
        return Settings.Secure.getString(getContentResolver(),
                "search_global_search_activity");
    }

    private boolean isInstalled(ComponentName globalSearch) {
        Intent intent = new Intent(SearchManager.INTENT_ACTION_GLOBAL_SEARCH);
        intent.setComponent(globalSearch);

        List<ResolveInfo> activities = getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if (activities != null && !activities.isEmpty()) {
            return true;
        }

        return false;
    }

    /**
     * Returns the highest ranked search provider as per the
     * ranking defined in .
     */
    private ComponentName getDefaultGlobalSearchProvider(List<ResolveInfo> providerList) {
        if (providerList != null && !providerList.isEmpty()) {
            ActivityInfo ai = providerList.get(0).activityInfo;
            return new ComponentName(ai.packageName, ai.name);
        }

        Log.w("gonggaofeng", "No global search activity found");
        return null;
    }

    private static final Comparator<ResolveInfo> GLOBAL_SEARCH_RANKER =
            new Comparator<ResolveInfo>() {
                @Override
                public int compare(ResolveInfo lhs, ResolveInfo rhs) {
                    if (lhs == rhs) {
                        return 0;
                    }
                    boolean lhsSystem = isSystemApp(lhs);
                    boolean rhsSystem = isSystemApp(rhs);

                    if (lhsSystem && !rhsSystem) {
                        return -1;
                    } else if (rhsSystem && !lhsSystem) {
                        return 1;
                    } else {
                        // Either both system engines, or both non system
                        // engines.
                        //
                        // Note, this isn't a typo. Higher priority numbers imply
                        // higher priority, but are "lower" in the sort order.
                        return rhs.priority - lhs.priority;
                    }
                }
            };

    private static final boolean isSystemApp(ResolveInfo res) {
        return (res.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }
}
