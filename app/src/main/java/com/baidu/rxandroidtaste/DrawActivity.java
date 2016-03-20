package com.baidu.rxandroidtaste;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class DrawActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new DrawView(this));
    }

    static class DrawView extends View {

        private Bitmap mBitmap;
        private Paint mBmpPaint;
        private int  mCurrentTop;
        private Xfermode mXfermode;
        private int mWidth;
        private int mHeight;

        public DrawView(Context context) {
            super(context);
            init();
        }

        public DrawView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init(){
            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image8);
            mBmpPaint = new Paint();
            mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(getContext(), TransparentActivity.class);
                    getContext().startActivity(in);
                }
            });
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mWidth = w;
            mHeight = h;
            Log.d("gonggaofeng", "onSizeChanged() called with: " + "w = [" + w + "], h = [" + h + "]");
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawBitmap(mBitmap, 0, 0, null);
//            int saveLayerCount = canvas.saveLayer(0, 0, mWidth, mHeight, mBmpPaint,
//                    Canvas.ALL_SAVE_FLAG);

            mBmpPaint.setColor(Color.GREEN);
            mBmpPaint.setXfermode(mXfermode);
            canvas.drawRect(0, mCurrentTop, 100, 400, mBmpPaint);
//            canvas.restoreToCount(saveLayerCount);
            mBmpPaint.setXfermode(null);

//        Log.e("rafe","saveLayerCount == " + saveLayerCount);
            Log.d("kobe", "mCurrentTop = " + mCurrentTop);
            mCurrentTop -= 8;
            if(mCurrentTop <= 0)
                mCurrentTop = 400;

            postInvalidateDelayed(500);

        }
    }
}
