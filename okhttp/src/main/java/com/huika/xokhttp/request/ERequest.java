package com.huika.xokhttp.request;

import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huika.okhttp.OkHttpClient;
import com.huika.xokhttp.callback.OnNetError;
import com.huika.xokhttp.callback.OnNetSuccuss;
import com.huika.xokhttp.params.AjaxParams;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Description:网络请求的基类
 * Created by zhouweiyong on 2016/1/7.
 */
public abstract class ERequest<T> implements IRequest{
    public String url;
    public AjaxParams ajaxParams;
    public OnNetSuccuss<T> onNetSuccuss;
    public OnNetError onError;
    public Type typeOfT;
    public OkHttpClient mOkHttpClient;
    public Handler mDelivery;
    public Gson mGson = new GsonBuilder().serializeNulls().create();
    public Objects tag;

    public ERequest(String url, AjaxParams ajaxParams, OnNetSuccuss<T> onNetSuccuss, OnNetError onError, Type typeOfT) {
        this.url = url;
        this.ajaxParams = ajaxParams;
        this.onNetSuccuss = onNetSuccuss;
        this.onError = onError;
        this.typeOfT = typeOfT;
    }

}
