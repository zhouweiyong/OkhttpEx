package com.zwy.okhttpdemo.post;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.zwy.okhttpdemo.R;
import com.huika.xokhttp.OkHttpManage;
import com.huika.xokhttp.OkhttpError;
import com.huika.xokhttp.callback.OnNetError;
import com.huika.xokhttp.callback.OnNetSuccuss;
import com.huika.xokhttp.https.RequestResult;
import com.huika.xokhttp.params.AjaxParams;
import com.huika.xokhttp.request.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Created by zhouweiyong on 2016/1/6.
 */
public class PostActionActivity extends Activity implements View.OnClickListener {

    private Button btn1;
    private TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post_action);
        btn1 = (Button) findViewById(R.id.btn1);
        textView1 = (TextView) findViewById(R.id.text1);
        btn1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page", "1");
            jsonObject.put("pageSize", "4");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("zwy", "1:" + Thread.currentThread().getId());


        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.putCommonTypeParam("page","1");
        ajaxParams.putCommonTypeParam("pageSize","4");

        PostRequest<RequestResult<ArrayList<Bean>>> request = new PostRequest<RequestResult<ArrayList<Bean>>>("http://192.168.3.116:8080//OkhttpEx_API_PHP/api/getnews.php", ajaxParams, new OnNetSuccuss<RequestResult<ArrayList<Bean>>>() {
            @Override
            public void onSuccess(RequestResult<ArrayList<Bean>> response) {
                Log.i("zwy", "3:" + Thread.currentThread().getId());
                Log.i("zwy", "4:" + Thread.currentThread().getName());

                //ttest  ttt
                Log.i("zwy", response.msg);
                textView1.setText(response.msg);
            }
        }, new OnNetError() {
            @Override
            public void onError(OkhttpError e) {
                Log.i("zwy", e.toString());
            }
        }, new TypeToken<RequestResult<ArrayList<Bean>>>(){}.getType());

        OkHttpManage.getInstance().setConnectTimeout(35, TimeUnit.SECONDS).execute(request,"request1");


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpManage.getInstance().cancelRequest("request1");
    }
}
