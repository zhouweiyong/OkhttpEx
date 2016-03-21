package com.huika.xokhttp.utils;

import android.util.Log;

import com.zwy.okhttp.BuildConfig;

/**
 * Description:
 * Created by zhouweiyong on 2016/1/8.
 */
public class L {
    private L()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
    private static final String TAG = "okhttp";

    public static void i(String msg)
    {
        if (isDebug)
            Log.i(TAG, msg);
    }
    public static void e(String msg)
    {
        if (isDebug)
            Log.e(TAG, msg);
    }
}
