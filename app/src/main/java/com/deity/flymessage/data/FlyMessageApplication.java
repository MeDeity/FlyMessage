package com.deity.flymessage.data;

import android.app.Application;
import android.util.Log;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Application
 * Created by Deity on 2018/1/16.
 */

public class FlyMessageApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("IMDebugApplication", "init");
        JMessageClient.setDebugMode(true);
        JMessageClient.init(getApplicationContext(), true);
    }
}
