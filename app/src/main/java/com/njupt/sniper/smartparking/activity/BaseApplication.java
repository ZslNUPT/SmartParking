package com.njupt.sniper.smartparking.activity;

import android.app.Application;

import com.njupt.sniper.smartparking.utils.CrashHandler;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
    }
}
