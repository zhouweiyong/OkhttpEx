package com.zwy.okhttpdemo.encryption;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.zwy.okhttpdemo.R;
import com.huika.xokhttp.OkHttpManage;
import com.huika.xokhttp.OkhttpError;
import com.huika.xokhttp.callback.OnNetError;
import com.huika.xokhttp.callback.OnNetSuccuss;
import com.huika.xokhttp.https.RequestResult;
import com.huika.xokhttp.request.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Description:
 * Created by zhouweiyong on 2016/1/13.
 */
public class EncryptionActivity extends Activity implements View.OnClickListener{
    private EditText et;
    private Button btn;
    //换行的地方加了\n
//	private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC3//sR2tXw0wrC2DySx8vNGlqt\n3Y7ldU9+LBLI6e1KS5lfc5jlTGF7KBTSkCHBM3ouEHWqp1ZJ85iJe59aF5gIB2kl\nBd6h4wrbbHA2XE1sq21ykja/Gqx7/IRia3zQfxGv/qEkyGOx+XALVoOlZqDwh76o\n2n1vP1D+tD3amHsK7QIDAQAB";
    //换行不加\n直接拼接在一起
    private static final String PUBLIC_KEY
            ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC3//sR2tXw0wrC2DySx8vNGlqt3Y7ldU9+LBLI6e1KS5lfc5jlTGF7KBTSkCHBM3ouEHWqp1ZJ85iJe59aF5gIB2klBd6h4wrbbHA2XE1sq21ykja/Gqx7/IRia3zQfxGv/qEkyGOx+XALVoOlZqDwh76o2n1vP1D+tD3amHsK7QIDAQAB";
    private static final String ALGORITHM = "RSA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_one);
        et = (EditText) findViewById(R.id.et_aeo);
        btn = (Button) findViewById(R.id.btn_aeo);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        FormResultRequest<String> request = new FormResultRequest<String>(UrlConstants.ENCRYPTONE, this, this, new TypeToken<RequestResult<String>>() {
//        }.getType());
//        JSONWrapAjaxParams ajaxParams = new JSONWrapAjaxParams();
//        // ajaxParams.putStringTypeParam("myName",
//        // RsaHelper.encryptDataFromStr(getInputStr(et),getPublicKeyFromX509(ALGORITHM,
//        // PUBLIC_KEY)));
//        ajaxParams.putStringTypeParam("myName", encryptByPublic(getInputStr(et)));
//        request.setRequestParams(ajaxParams);
//        executeRequest(request);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name","tom");
            jsonObject.put("age","21");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONWrapAjaxParams ajaxParams = new JSONWrapAjaxParams();
        ajaxParams.putCommonTypeParam("myName","admin");

        ajaxParams.putCommonTypeParam("info",jsonObject);
        PostRequest<RequestResult<Map<String,String>>> request = new PostRequest<RequestResult<Map<String,String>>>("http://192.168.53.218:80/api/encryptone.php", ajaxParams, new OnNetSuccuss<RequestResult<Map<String,String>>>() {
            @Override
            public void onSuccess(RequestResult<Map<String,String>> response) {

            }
        }, new OnNetError() {
            @Override
            public void onError(OkhttpError e) {

            }
        }, new TypeToken<RequestResult<Map<String,String>>>(){}.getType());

        OkHttpManage.getInstance().execute(request);
    }


}
