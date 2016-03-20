package com.baidu.rxandroidtaste;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gonggaofeng on 15/12/31.
 */
public class PackageViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_viewer);
        initViews();
    }

    private void initViews() {
        ListView listView = (ListView) findViewById(R.id.listView);
        Adapter adapter = new Adapter(this);
        listView.setAdapter(adapter);
        List<PackageInfo> apps = getPackageManager().getInstalledPackages(0);
        adapter.addAll(apps);
    }

    private static class Adapter extends ArrayAdapter<PackageInfo> {

        private PackageManager mPm;
        public Adapter(Context context) {
            super(context, 0);
            mPm = context.getPackageManager();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_package_viewer, parent, false);
                holder = new Holder();
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.label = (TextView) convertView.findViewById(R.id.label);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            PackageInfo item = getItem(position);
            holder.icon.setImageDrawable(item.applicationInfo.loadIcon(mPm));
            holder.name.setText(item.packageName);
            holder.label.setText(item.applicationInfo.loadLabel(mPm));
            return convertView;
        }
    }

    private static class Holder {
        ImageView icon;
        TextView name;
        TextView label;
    }
}
