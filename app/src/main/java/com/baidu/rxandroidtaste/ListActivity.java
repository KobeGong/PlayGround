package com.baidu.rxandroidtaste;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by gonggaofeng on 15/12/8.
 */
public class ListActivity extends AppCompatActivity {
    @Bind(R.id.listView)
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        for (int i = 0; i < 20; i++) {
            mAdapter.add("List Item "+i);
        }
        mListView.setAdapter(mAdapter);
    }

    @OnItemClick(R.id.listView)
    void onItemClick(AdapterView<?> parent, View view, int postion, long id) {
        Intent in = new Intent(this, ListDetailActivity.class);
        ActivityCompat.startActivity(this, in,
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this, view, "root").toBundle());
    }

}
