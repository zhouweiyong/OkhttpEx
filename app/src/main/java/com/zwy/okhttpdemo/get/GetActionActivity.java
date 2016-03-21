package com.zwy.okhttpdemo.get;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.huika.okhttp.Call;
import com.huika.okhttp.Callback;
import com.huika.okhttp.OkHttpClient;
import com.huika.okhttp.Request;
import com.huika.okhttp.Response;
import com.zwy.okhttpdemo.R;

import java.io.IOException;

/**
 * Description:
 * Created by zhouweiyong on 2016/1/6.
 */
public class GetActionActivity  extends Activity implements View.OnClickListener{
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
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder().url("http://192.168.53.218:80/api/getcontent.php?page=1&pageSize=4").build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }

        });

    }
}
