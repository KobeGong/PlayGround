package com.baidu.rxandroidtaste.scan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by gonggaofeng on 16/6/28.
 */
public class SweepView extends View {

    public static final int STROKE_WIDTH = 80;
    private Paint outRingPaint;
    private Paint ringPaint;
    private float centerX, centerY, outRingRadius;
    private RectF wifiArcRect;
    private float wifiRadius;
    private int wifiPower;
    private SweepGradient sweepGradient, ringSweepGradient;
    Matrix matrix = new Matrix();

    public SweepView(Context context) {
        this(context, null);
    }

    public SweepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SweepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        outRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        outRingPaint.setColor(Color.WHITE);
        outRingPaint.setStyle(Paint.Style.FILL);

        ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        ringPaint.setColor(Color.WHITE);
        ringPaint.setStyle(Paint.Style.STROKE);

        sweepGradient = new SweepGradient(0.5f, 0.5f,
                new int[]{Color.parseColor("#00FFFFFF"), Color.parseColor("#AAFFFFFF")},
                new float[]{0.6f, 1f});
        outRingPaint.setShader(sweepGradient);
        ringSweepGradient = new SweepGradient(0.5f, 0.5f,
                new int[]{Color.parseColor("#00FFFFFF"), Color.parseColor("#FFFFFFFF")},
                new float[]{0.6f, 1f});
        ringPaint.setShader(ringSweepGradient);
        ringPaint.setStrokeWidth(STROKE_WIDTH / 4);
        wifiArcRect = new RectF();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            int width = right - left;
            int height = bottom - top;
            float halfWidth = width / 2;
            float halfHeight = height / 2;
            centerX = halfWidth;
            centerY = halfHeight;
            outRingRadius = Math.min(width, height) / 2;
            wifiRadius = outRingRadius;
            wifiArcRect.set(0F, 0F, wifiRadius, wifiRadius);

            matrix.postScale(width, height);
            sweepGradient.setLocalMatrix(matrix);
            ringSweepGradient.setLocalMatrix(matrix);
//            wifiArcRect.offset(halfWidth / 2, (halfHeight - halfWidth / 2) + outRingRadius / 4);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float cx = centerX;
        float cy = centerY;
        canvas.drawCircle(cx, cy, outRingRadius - STROKE_WIDTH / 2, outRingPaint);
        canvas.drawCircle(cx, cy, outRingRadius - STROKE_WIDTH * 5/ 8, ringPaint);

    }
}
