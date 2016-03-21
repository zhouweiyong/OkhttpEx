package com.zwy.okhttpdemo.upload;

/**
 * Description:
 * Created by zhouweiyong on 2016/1/13.
 */
public class Respone<T> {
    public T a;
    public interface OnSuccess<T>{
        public void onSuccess();
    }

    public interface OnError<T>{
        public void onError();
    }
}
