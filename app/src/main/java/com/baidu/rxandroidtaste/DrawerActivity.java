package com.baidu.rxandroidtaste;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gonggaofeng on 16/4/21.
 *
 */
public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.navigationView)
    NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        ButterKnife.bind(this);
        getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, new MyFragment(), "").commit();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        boolean result = false;
        int id = menuItem.getItemId();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case R.id.item1:
                transaction.replace(R.id.contentContainer, new MyFragment(), "");
                result = true;
                break;
            case R.id.item2:
                transaction.replace(R.id.contentContainer, new MyFragment2(), "");
                result = true;
                break;
        }
        transaction.commit();
        drawerLayout.closeDrawer(GravityCompat.START);
        return result;
    }

    public static class MyFragment extends Fragment {

        @Bind(R.id.recyclerView)
        RecyclerView recyclerView;

        @Bind(R.id.toolBar)
        Toolbar toolbar;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_my, container, false);
            ButterKnife.bind(this, view);
            return view;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            ButterKnife.unbind(this);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            toolbar.setTitle(getActivity().getTitle());
            recyclerView.setAdapter(new RecyclerViewAdapter());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        static class RecyclerViewAdapter extends RecyclerView.Adapter<SimpleHolder> {

            @Override
            public SimpleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_view, parent, false);
                return new SimpleHolder(itemView);
            }

            @Override
            public void onBindViewHolder(SimpleHolder holder, int position) {
                holder.text.setText("recycler item " + position);
            }

            @Override
            public int getItemCount() {
                return 10;
            }
        }

        static class SimpleHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.text)
            TextView text;

            public SimpleHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public static class MyFragment2 extends Fragment {

        @Bind(R.id.toolBar)
        Toolbar toolbar;

        @Bind(R.id.tabLayout)
        TabLayout tabLayout;

        @Bind(R.id.viewPager)
        ViewPager viewPager;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_my2, container, false);
            ButterKnife.bind(this, view);
            return view;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ContentFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "title " + position;
        }
    }
}
