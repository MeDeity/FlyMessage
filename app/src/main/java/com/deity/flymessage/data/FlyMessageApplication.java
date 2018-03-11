package com.deity.flymessage.data;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import com.mob.MobSDK;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import io.realm.Realm;
import io.realm.RealmConfiguration;

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
        if (!getCurProcessName(getApplicationContext()).equals(this.getPackageName())){//因此存在守护线程的原因导致Applcation执行多次
            return;
        }
        super.onCreate();

        Log.i("IMDebugApplication", "init");
        JMessageClient.setDebugMode(true);
        JMessageClient.init(getApplicationContext(), true);
        initScreenSize();
        MobSDK.init(this);//短信验证码
        //数据库初始化
        initReleamDatabase();
    }

    /**
     * 只有当前的进程才可以执行OnCreate方法
     * @param context
     * @return
     */
    private String getCurProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return "";
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return "";
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

    private void initReleamDatabase(){
        RealmConfiguration configuration = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(configuration);
    }
}
