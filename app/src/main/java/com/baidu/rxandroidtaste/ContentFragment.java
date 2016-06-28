package com.baidu.rxandroidtaste;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;

/**
 * Created by gonggaofeng on 16/4/21.
 *
 */
public class ContentFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "title" + position;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return new InnerFragment();
        }

    }

    public static class InnerFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            LinearLayout linearLayout = new LinearLayout(container.getContext());
            linearLayout.setLayoutParams(new ViewPager.LayoutParams());
            TextView textView = new TextView(container.getContext());
            textView.setText("เพิ่มประสิทธิภาพให้เครือข่ายทันทีเพิ่มประสิทธิภาพให้เครือข่ายทันทีเพิ่มประสิทธิภาพให้เครือข่ายทันที");
            linearLayout.addView(textView);
            return linearLayout;
        }
    }
}
