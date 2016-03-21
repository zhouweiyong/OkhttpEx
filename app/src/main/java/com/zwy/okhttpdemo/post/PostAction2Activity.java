package com.zwy.okhttpdemo.post;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zwy.okhttpdemo.R;

import okhttp3.OkHttpClient;

/**
 * Description:
 * Created by zhouweiyong on 2016/1/6.
 */
public class PostAction2Activity extends Activity implements View.OnClickListener {

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
        OkHttpClient okHttpClient = new OkHttpClient();
    }
}
