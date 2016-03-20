package com.baidu.rxandroidtaste;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

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
}