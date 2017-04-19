package com.baidu.rxandroidtaste.scan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by gonggaofeng on 16/6/28.
 * WifiView
 */
public class WifiView extends View {

    public static final int STROKE_WIDTH = 80;
    private Paint outRingPaint;
    private Paint wifiPaint;
    private float centerX;
    private float centerY;
    private RectF wifiArcRect;
    private float wifiRadius;
    private int wifiPower;

    public WifiView(Context context) {
        this(context, null);
    }

    public WifiView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WifiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        outRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        outRingPaint.setColor(Color.WHITE);
        outRingPaint.setAlpha((int) (0.3f * 255));
        outRingPaint.setStyle(Paint.Style.STROKE);
        outRingPaint.setStrokeWidth(STROKE_WIDTH);

        wifiPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        wifiPaint.setAntiAlias(true);
        wifiPaint.setColor(Color.WHITE);

        wifiArcRect = new RectF();
        wifiPower = 3;
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
            float outRingRadius = Math.min(width, height) / 2;
            wifiRadius = outRingRadius * 1.2f;
            wifiArcRect.set(0F, 0F, wifiRadius, wifiRadius);
        }
    }

    public void setWifiPower(int power) {
        if (this.wifiPower != power) {
            this.wifiPower = power;
            postInvalidateOnAnimation();
        }
    }

    public int getWifiPower() {
        return wifiPower;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float step = (wifiRadius - 60) / 3;
        for (int i = wifiPower; i >= 1; i--) {
            canvas.save();
            int alpha = (int) ((5 - i + 1) * 0.2f * 255);
            wifiPaint.setAlpha(alpha);
            float wifiR = step * i + 60;
            wifiArcRect.set(0F, 0F, wifiR, wifiR);
            canvas.translate(centerX - wifiArcRect.centerX(), centerY - wifiArcRect.centerY() + wifiRadius / 4);
            canvas.drawArc(wifiArcRect, 235, 70, true, wifiPaint);
            canvas.restore();
        }
    }
}
