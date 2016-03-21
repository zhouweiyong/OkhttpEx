package com.huika.xokhttp.request;

import com.huika.okhttp.Call;
import com.huika.okhttp.Callback;
import com.huika.okhttp.MediaType;
import com.huika.okhttp.Request;
import com.huika.okhttp.RequestBody;
import com.huika.okhttp.Response;
import com.huika.xokhttp.OkhttpError;
import com.huika.xokhttp.callback.OnNetError;
import com.huika.xokhttp.callback.OnNetSuccuss;
import com.huika.xokhttp.https.RequestResult;
import com.huika.xokhttp.params.AjaxParams;
import com.huika.xokhttp.utils.L;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Description:POST的网络请求
 * 以json方式提交
 * Created by zhouweiyong on 2016/1/6.
 */
public class PostRequest<T> extends ERequest {

    public PostRequest(String url, AjaxParams ajaxParams, OnNetSuccuss onNetSuccuss, OnNetError onError, Type typeOfT) {
        super(url, ajaxParams, onNetSuccuss, onError, typeOfT);
    }


    @Override
    public void netWork() {
        try {
            L.i(String.format("请求的URL:%s", url));
            Map<String, String> params = ajaxParams.getUrlParams();
            Set<String> keySet = params.keySet();
            final JSONObject jsonObject = new JSONObject();
            for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
                String k = it.next();
                String v = params.get(k);
                jsonObject.put(k, v);
            }
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(mediaType, jsonObject.toString());
            L.i(String.format("请求参数:%s", jsonObject.toString()));
            final Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .tag(tag)
                    .build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    onError.onError(new OkhttpError(e));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                        String date = null;
                        String url = null;
                        if (response.headers() != null) date = response.header("Date", null);
                        if (response.request() != null) url = response.request().url().toString();
                        String json = response.body().string();
                        L.i(String.format("返回的信息:%s", json));
                        try {
                            final T result = mGson.fromJson(json, typeOfT);
                            if (result instanceof RequestResult) {
                                ((RequestResult) result).dateStr = date;
                                ((RequestResult) result).url = url;
                            }
                            mDelivery.post(new Runnable() {
                                @Override
                                public void run() {
                                    onNetSuccuss.onSuccess(result);
                                }
                            });
                        }catch (Exception e){
                            onError.onError(new OkhttpError(e));
                        }
                }
            });
        } catch (Exception e) {
            onError.onError(new OkhttpError(e));
        }
    }
}
