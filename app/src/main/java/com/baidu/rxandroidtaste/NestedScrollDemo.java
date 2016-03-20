package com.baidu.rxandroidtaste;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Created by gonggaofeng on 15/12/19.
 */
public class NestedScrollDemo extends AppCompatActivity {

    private NestedScrollViewGroup mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(0);
        super.onCreate(savedInstanceState);
        mRootView = new NestedScrollViewGroup(this);
        createContentView();
        setContentView(mRootView);

    }

    private void createContentView(){
        mRootView.setOrientation(LinearLayout.VERTICAL);
        NestedScrollView textView = new NestedScrollView(this);
        textView.setBackground(new ColorDrawable(Color.BLUE));
        textView.setText(getTitle());
        textView.setTextSize(32f);
        mRootView.addView(textView, new LinearLayout.LayoutParams(-1, -2));
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setAdapter(new ListAdapter(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRootView.addView(recyclerView, new LinearLayout.LayoutParams(-1,-1));
    }

    private static class NestedScrollViewGroup extends LinearLayout implements NestedScrollingParent{

        private static final String TAG = NestedScrollViewGroup.class.getSimpleName();

        private NestedScrollingParentHelper mNestedScrollingParentHelper;

        public NestedScrollViewGroup(Context context) {
            super(context);
            init();
        }

        public NestedScrollViewGroup(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public NestedScrollViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init(){
            mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        }

        @Override
        public int getNestedScrollAxes() {
            return mNestedScrollingParentHelper.getNestedScrollAxes();
        }

        @Override
        public void onNestedScrollAccepted(View child, View target, int axes) {
            mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        }

        @Override
        public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
            Log.d(TAG, "onNestedPreScroll(): dx =" + dx + ", dy = " + dy + ", consumed = " + Arrays.toString(consumed));
            if(getChildCount() > 0){
                View firstChild = getChildAt(0);
                int firstChildTop = firstChild.getTop();
                int firstChildBottom = firstChild.getBottom();
                if(firstChildBottom > 0 && firstChildTop <= 0){
                    int min = Math.min(firstChildBottom, dy);
                    firstChild.offsetTopAndBottom(min);
                    consumed[1] = min;
                }
            }
        }

        @Override
        public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            Log.d(TAG, "onNestedScroll() dxConsumed = " + dxConsumed + ", dyConsumed = " + dyConsumed + ", dxUnconsumed = " + dxUnconsumed + ", dyUnconsumed = " + dyUnconsumed);

        }

        @Override
        public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
            return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) == ViewCompat.SCROLL_AXIS_VERTICAL;
        }
    }

    private static class NestedScrollView extends TextView implements NestedScrollingChild{

        private NestedScrollingChildHelper mNestedScrollingChildHelper;
        public NestedScrollView(Context context) {
            super(context);
            init();
        }

        public NestedScrollView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public NestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init(){
            mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        }

        @Override
        public boolean startNestedScroll(int axes) {
            return mNestedScrollingChildHelper.startNestedScroll(axes);
        }
    }

    private static class ListAdapter extends RecyclerView.Adapter{

        private Context mContext;

        public ListAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.mTextView.setText("RecyclerView Item "+position);
        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView mTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }


}
