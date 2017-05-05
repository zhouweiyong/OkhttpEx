package com.vst.osample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2017/2/17
 * class description:请输入类描述
 */
public class SimpleActivity extends Activity implements View.OnClickListener {
    private static final String TAG = SimpleActivity.class.getSimpleName();
    private static final String GET_URL = "http://192.168.3.155/index.php/account/getinfo";
    private static final String POST_URL = "http://192.168.3.155/index.php/account/postinfo";
    private static final String POST_STRING_URL = "http://192.168.3.155/index.php/account/poststring";
    private static final String POST_FILE_URL = "http://192.168.3.155/index.php/account/postfile";
    private static final String POST_FILE1_URL = "http://192.168.3.155/index.php/account/postfile1";
    private static final String DOWNLOAD_URL = "http://192.168.3.155/uploads/image01.jpg";
    private static final String DOWNLOAD_IMAGE_URL = "http://192.168.3.155/uploads/img04.jpg";

    private Button btn_get;
    private Button btn_post;
    private Button btn_post_string;
    private Button btn_post_file;
    private Button btn_post_file2;
    private Button btn_download;
    private Button btn_down_image;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        initView();
    }

    private void initView() {
        btn_get = (Button) findViewById(R.id.btn_get);
        btn_post = (Button) findViewById(R.id.btn_post);

        btn_get.setOnClickListener(this);
        btn_post.setOnClickListener(this);
        btn_post_string = (Button) findViewById(R.id.btn_post_string);
        btn_post_string.setOnClickListener(this);
        btn_post_file2 = (Button) findViewById(R.id.btn_post_file2);
        btn_post_file2.setOnClickListener(this);
        btn_download = (Button) findViewById(R.id.btn_download);
        btn_download.setOnClickListener(this);
        btn_down_image = (Button) findViewById(R.id.btn_down_image);
        btn_down_image.setOnClickListener(this);
        iv = (ImageView) findViewById(R.id.iv);
        iv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get:
                getRequest();
                break;
            case R.id.btn_post:
                postRequest();
                break;
            case R.id.btn_post_string:
                postString();
                break;
            case R.id.btn_post_file2:
                postFile2();
                break;
            case R.id.btn_download:
                download();
                break;
            case R.id.btn_down_image:
                loadImage();
                break;
        }
    }

    private void getRequest() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(GET_URL).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "fail>>>" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "success>>>" + response.body().string());
            }
        });
    }

    private void postRequest() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
//                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addNetworkInterceptor(new LoggingInterceptor())
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        for (Cookie cookie : cookies) {
                            Log.i("zwy", "key:" + cookie.name() + " value:" + cookie.value());
                        }
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        return new ArrayList<Cookie>();
                    }
                })
                .build();

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("userName", "jack");
        builder.add("age", "38");
        builder.add("gender", "women");
        Request request = new Request.Builder().url(POST_URL).post(builder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "fail>>>" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "success>>>" + response.body().string());
            }
        });
    }

    private void postString() {
        OkHttpClient client = new OkHttpClient();
        //RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), "{\"userName\":\"Hely\",\"age\":16,\"gender\":\"women\"}");
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), "This is a String");
        Request request = new Request.Builder().url(POST_STRING_URL).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "fail>>>" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "success>>>" + response.body().string());
            }
        });

    }


    private void postFile2() {
        OkHttpClient client = new OkHttpClient();
        File file = new File(Environment.getExternalStorageDirectory(), "image1.jpg");
        if (!file.exists()) {
            Log.i("zwy", "file not foud!");
        }
        MultipartBody.Builder builder = new MultipartBody.Builder();
        RequestBody requestBody = builder.setType(MultipartBody.FORM).addFormDataPart("userName", "ama").addFormDataPart("age", "38")
                .addFormDataPart("upImage", "image1.jpg", RequestBody.create(MediaType.parse("image/pjpeg"), file))
                .build();
        CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody, new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(long byteWrited, long contentLength) {
                Log.i("zwy", byteWrited + " / " + contentLength);
            }
        });
        //检测上传进度
        Request request = new Request.Builder().url(POST_FILE_URL).post(countingRequestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "fail>>>" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "success>>>" + response.body().string());
            }
        });
    }

    public void download() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(DOWNLOAD_URL).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "fail>>>" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "success");
                InputStream is = response.body().byteStream();
                int len = 0;
                File file = new File(Environment.getExternalStorageDirectory(), "image01.jpg");
                byte[] buf = new byte[1024];
                FileOutputStream fos = new FileOutputStream(file);
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                fos.flush();
                fos.close();
                is.close();
            }
        });
    }

    public void loadImage() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(DOWNLOAD_IMAGE_URL).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "fail>>>" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "success");
                byte[] bytes = response.body().bytes();
                final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bitmap != null) {
                            iv.setVisibility(View.VISIBLE);
                            iv.setImageBitmap(bitmap);
                        }
                    }
                });
            }
        });
    }


}
