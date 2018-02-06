package com.deity.flymessage.utils;

import android.content.Context;

/**
 * 适用于本应用的工具类
 * Created by Deity on 2018/2/6.
 */

public class FlyMessageUtils {

    public static int dip2px(Context ctx, float dpValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
