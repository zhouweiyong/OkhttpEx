package com.zwy.okhttpdemo;

import android.app.Application;

/**
 * Description:
 * Created by zhouweiyong on 2016/1/13.
 */
public class MyApplication extends Application{
    private static MyApplication instance;

    public static MyApplication getInstance(){
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
