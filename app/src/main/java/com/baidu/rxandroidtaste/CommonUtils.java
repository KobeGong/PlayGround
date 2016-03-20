package com.baidu.rxandroidtaste;

import android.content.res.Resources;

/**
 * Created by gonggaofeng on 15/12/15.
 */
public class CommonUtils {

    public static float dpToPx(int dp){
        return Resources.getSystem().getDisplayMetrics().density * dp;
    }
}
