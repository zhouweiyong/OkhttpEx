package com.zwy.okhttpdemo.get;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.zwy.okhttpdemo.R;
import com.zwy.okhttpdemo.post.Bean;
import com.huika.xokhttp.OkHttpManage;
import com.huika.xokhttp.OkhttpError;
import com.huika.xokhttp.callback.OnNetError;
import com.huika.xokhttp.callback.OnNetSuccuss;
import com.huika.xokhttp.https.RequestResult;
import com.huika.xokhttp.params.AjaxParams;
import com.huika.xokhttp.request.GetRequest;

import java.util.ArrayList;

/**
 * Description:
 * Created by zhouweiyong on 2016/1/12.
 */
public class GetActionActivity2 extends Activity implements View.OnClickListener{
    private Button btn1;
    private TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_get_action);
        btn1 = (Button) findViewById(R.id.btn1);
        textView1 = (TextView) findViewById(R.id.text1);
        btn1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.putCommonTypeParam("page","1");
        ajaxParams.putCommonTypeParam("pageSize","4");

        GetRequest<RequestResult<ArrayList<Bean>>> request = new GetRequest<RequestResult<ArrayList<Bean>>>("http://192.168.53.218:80/api/getcontent.php", ajaxParams, new OnNetSuccuss<RequestResult<ArrayList<Bean>>>() {

            @Override
            public void onSuccess(RequestResult<ArrayList<Bean>> response) {
                textView1.setText(response.msg);
            }
        }, new OnNetError() {
            @Override
            public void onError(OkhttpError okhttpError) {
                Log.i("zwy","");
            }
        },new TypeToken<RequestResult<Bean>>(){}.getType());

        OkHttpManage.getInstance().execute(request);


    }
}
