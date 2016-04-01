package com.huika.xokhttp;

import android.text.TextUtils;

import com.huika.okhttp.Request;
import com.huika.xokhttp.utils.L;

/**
 * Description:
 * Created by zhouweiyong on 2016/1/8.
 */
public class OkhttpError {
    public Request request;//请求的信息
    public String errMsg;//自定义错误消息
    public Exception exception;//错误信息
    /**
     * 500：网络连接错误
     * 400：
     * 300：解析错误
     * 600:自定义错误，或其它未知错误，需要进一步明确
     * 700:链接超时
     * 705:链接关闭
     */
    public int errorCode;//错误码

    public OkhttpError(Request request, Exception exception) {
        this.request = request;
        this.exception = exception;
        initCode();
    }

    public OkhttpError(Exception exception) {
        this.exception = exception;
        initCode();
    }

    public OkhttpError(String errMsg) {
        this.errMsg = errMsg;
    }

    public void initCode() {
        if (exception != null && !TextUtils.isEmpty(exception.getMessage())) {
            if (exception.getMessage().contains("Failed to connect")||exception.getMessage().contains("failed to connect")) {
                errorCode = 500;
            } else if (exception.getMessage().contains("IllegalStateException")||exception.getMessage().contains("illegalStateException")) {
                errorCode = 300;
            } else if (exception.getMessage().contains("timeout")) {
                errorCode = 700;
            }else if(exception.getMessage().contains("Socket closed")|| exception.getMessage().contains("socket closed")){//java.net.SocketException: Socket closed
                errorCode = 705;
            }
            else {
                errorCode = 600;
            }
        } else {
            errorCode = 600;
        }
        L.i(String.format("ErrorMessage:%s,ErrorCode:%d", exception.getMessage(), errorCode));
    }
}
