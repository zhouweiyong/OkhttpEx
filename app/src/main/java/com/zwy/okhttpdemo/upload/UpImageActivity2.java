package com.zwy.okhttpdemo.upload;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.huika.okhttp.Call;
import com.huika.okhttp.Callback;
import com.huika.okhttp.Headers;
import com.huika.okhttp.MediaType;
import com.huika.okhttp.MultipartBody;
import com.huika.okhttp.OkHttpClient;
import com.huika.okhttp.Request;
import com.huika.okhttp.RequestBody;
import com.huika.okhttp.Response;
import com.huika.xokhttp.OkHttpManage;
import com.huika.xokhttp.OkhttpError;
import com.huika.xokhttp.callback.OnNetError;
import com.huika.xokhttp.callback.OnNetSuccuss;
import com.huika.xokhttp.https.RequestResult;
import com.huika.xokhttp.params.encryption.Base64Encoder;
import com.huika.xokhttp.params.type.FileTypeParam;
import com.huika.xokhttp.request.FileRequest;
import com.zwy.okhttpdemo.R;
import com.zwy.okhttpdemo.encryption.JSONWrapAjaxParams;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


/**
 * Description:
 * Created by zhouweiyong on 2016/1/13.
 */
public class UpImageActivity2 extends Activity implements View.OnClickListener{

    private ImageView ivUpload;
    private Button btnUpload;

    /** 从 拍照中选择 */
    public static final int ACTIVITY_RESULT_CROPCAMARA_WITH_DATA = 1;
    /** 从 相册中选择 */
    public static final int ACTIVITY_RESULT_CROPIMAGE_WITH_DATA = 2;
    /** 从 拍照中选择不剪裁 */
    public static final int ACTIVITY_RESULT_NO_CROPCAMARA_WITH_DATA = 3;
    /** 从 相册中选择不剪裁 */
    public static final int ACTIVITY_RESULT_NO_CROPIMAGE_WITH_DATA = 4;
    private File picFile;
    private Dialog picDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        ivUpload = (ImageView) findViewById(R.id.iv_upload_image);
        btnUpload = (Button) findViewById(R.id.btn_upload_image);
        btnUpload.setOnClickListener(this);
        ivUpload.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_upload_image:
                selectPhoto();
                break;
            case R.id.btn_upload_image:
                upload();
                break;

            default:
                break;
        }
    }


    private void selectPhoto() {
        if (picDialog == null) {
            picDialog = MMAlert.createTwoChoicAlertNoTitle(this, R.string.common_camera, R.string.common_gallery, new MMAlert.DialogOnItemClickListener() {

                @Override
                public void onItemClickListener(View v, int position) {
                    int id = v.getId();
                    switch (id) {
                        case R.id.item_first:
                            if (!ImageTools.isSDCardExist()) {
                                showToastMsg("sd卡不可用");
                                return;
                            }
                            Intent cameraIntent = null;
                            picFile = ImageTools.initTempFile();
                            cameraIntent = ImageTools.getTakeCameraIntent(Uri.fromFile(picFile));
                            startActivityForResult(cameraIntent, ACTIVITY_RESULT_CROPCAMARA_WITH_DATA);
                            break;

                        case R.id.item_second:
                            picFile = ImageTools.initTempFile();
                            Intent photoIntent = ImageTools.cropPhotoOfCompressFromGalleryIntent(Uri.fromFile(picFile));
                            startActivityForResult(photoIntent, ACTIVITY_RESULT_CROPIMAGE_WITH_DATA);
                            break;
                    }
                }

            });
        }
        picDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ACTIVITY_RESULT_CROPCAMARA_WITH_DATA: // 拍照
                Intent intent = ImageTools.cropPhotoOfCompressIntent(Uri.fromFile(picFile));
                startActivityForResult(intent, ACTIVITY_RESULT_CROPIMAGE_WITH_DATA);
                break;
            case ACTIVITY_RESULT_CROPIMAGE_WITH_DATA:

                ///data/data/com.zwy.okhttpdemo/cache/img_temp/2016-01-13_16-01-34_394.jpg
                if (null == data) {
                    return;
                }
                if (TextUtils.isEmpty(picFile.toString()) || !picFile.exists()) {
                    showToastMsg("没有选择到图片");
                    return;
                }
                ivUpload.setImageURI(Uri.fromFile(picFile));
                break;
        }
    }

    private void upload2(){
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            MediaType mediaType = MediaType.parse(FileUtils.getMimeType(Uri.fromFile(picFile).toString()));
            RequestBody fileBody = RequestBody.create(mediaType, picFile);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("color","red");
            jsonObject.put("age", "21");

            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addPart(Headers.of("Content-Disposition", "form-data; name=\"userName\""), RequestBody.create(null, "winson"))
                    .addPart(Headers.of("Content-Disposition", "form-data; name=\"upImage\"; filename=\"wodetupian\""), fileBody)
                    .addPart(Headers.of("Content-Disposition", "form-data; name=\"seImage\"; filename=\"seconpic\""), fileBody)
                    .build();

            Request request = new Request.Builder()
                    .url("http://192.168.53.218/api/uploadImageTwo.php")
                    .post(requestBody)
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("zwy","");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.i("zwy",response.body().string());//{"flag": "1","msg": "上传成功","rs": {"image":"wodetupian1452849150.jpg"}}
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void upload() {
        JSONWrapAjaxParams ajaxParams = new JSONWrapAjaxParams();
        try {
            ajaxParams.putCommonTypeParam("size","300");
            ajaxParams.putCommonTypeParam("area","china");
            ajaxParams.put(new FileTypeParam("upImage",picFile.getPath(),FileUtils.getMimeType(Uri.fromFile(picFile).toString())));

            FileRequest<RequestResult<Map<String,String>>> request = new FileRequest<RequestResult<Map<String,String>>>("http://192.168.53.218/api/uploadImageTwo.php", ajaxParams, new OnNetSuccuss() {
                @Override
                public void onSuccess(Object response) {
                    Log.i("zwy","");
                }
            }, new OnNetError() {
                @Override
                public void onError(OkhttpError okhttpError) {
                    Log.i("zwy","");
                }
            },new TypeToken<RequestResult<Map<String,String>>>(){}.getType());
            OkHttpManage.getInstance().execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private String getMD5FromFile(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        byte[] bytes = convertStreamToByteArray(in);
        ByteArrayInputStream ins = new ByteArrayInputStream(bytes);
        String fileStr = Base64Encoder.encode(bytes);
//		String fileMD5 = MD5Security.getMd5_32(fileStr).toUpperCase();
        return fileStr;
    }

    private byte[] convertStreamToByteArray(InputStream is) throws IOException {
        if (is == null) {
            return null;
        }
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            return baos == null ? null : baos.toByteArray();
        } catch (IOException e) {
            return null;
        } finally {
            if (baos != null)
                baos.close();
            baos = null;
        }
    }

    private void showToastMsg(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
