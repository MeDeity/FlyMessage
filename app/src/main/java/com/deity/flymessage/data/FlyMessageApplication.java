package com.deity.flymessage.data;

import android.app.Application;
import android.util.DisplayMetrics;
import android.util.Log;

import com.mob.MobSDK;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Application
 * Created by Deity on 2018/1/16.
 */

public class FlyMessageApplication extends Application {

    /**
     * 屏幕宽度
     */
    public static int screenWidth;
    /**
     * 屏幕高度
     */
    public static int screenHeight;
    /**
     * 屏幕密度
     */
    public static float screenDensity;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("IMDebugApplication", "init");
        JMessageClient.setDebugMode(true);
        JMessageClient.init(getApplicationContext(), true);
        initScreenSize();
        MobSDK.init(this);//短信验证码
    }

    /**
     * 初始化当前设备屏幕宽高
     */
    private void initScreenSize() {
        DisplayMetrics curMetrics = getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = curMetrics.widthPixels;
        screenHeight = curMetrics.heightPixels;
        screenDensity = curMetrics.density;
    }
}
