package com.zwy.okhttpdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zwy.okhttpdemo.encryption.EncryptionActivity;
import com.zwy.okhttpdemo.get.GetActionActivity;
import com.zwy.okhttpdemo.get.GetActionActivity2;
import com.zwy.okhttpdemo.post.PostActionActivity;
import com.zwy.okhttpdemo.upload.UpImageActivity;
import com.zwy.okhttpdemo.upload.UpImageActivity2;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{
    private List<String> list;
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout. activity_main);
        lv = (ListView) findViewById(R.id. lv_main);
        lv.setOnItemClickListener(this);

        String[] arr = getResources().getStringArray(R.array.main_list );
        list = Arrays. asList(arr);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                skipAct(this,GetActionActivity.class);
                break;
            case 1:
                skipAct(this,PostActionActivity.class);
                break;
            case 2:
                skipAct(this,GetActionActivity2.class);
                break;
            case 3:
                skipAct(this,UpImageActivity.class);
                break;
            case 4:
                skipAct(this,UpImageActivity2.class);
                break;
            case 5:
                skipAct(this,EncryptionActivity.class);
                break;
        }
    }

    private void skipAct(Activity aty, Class<?> cls){
        Intent intent = new Intent(aty,cls);
        startActivity(intent);
    }
}
