package com.huika.xokhttp.callback;

import com.huika.xokhttp.OkhttpError;

/**
 * Description:
 * Created by zhouweiyong on 2016/1/7.
 */
public interface OnNetError {
    public void onError(OkhttpError okhttpError);
}
