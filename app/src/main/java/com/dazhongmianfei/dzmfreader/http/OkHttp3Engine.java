package com.dazhongmianfei.dzmfreader.http;

import android.content.Context;
import android.util.Log;

import com.dazhongmianfei.dzmfreader.utils.InternetUtils;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/4/16.
 */
public class OkHttp3Engine {
    public static OkHttp3Engine mInstance;
    private OkHttpClient mOkHttpClient;
    private static Context mContext;
    private static boolean mShowDialog = true;
    private Gson mGson;

    public static OkHttp3Engine getInstance(Context context) {
        return getInstance(context, true);
    }

    public static OkHttp3Engine getInstance(Context context, boolean showDialog) {
        mContext = context;
        mShowDialog = showDialog;
        if (mInstance == null) {
            synchronized (OkHttp3Engine.class) {
                if (mInstance == null) {
                    mInstance = new OkHttp3Engine(context);
                }
            }
        }
        return mInstance;
    }

    private OkHttp3Engine(Context context) {
    }






    public void postAsyncHttp(String url, String json, ResultCallback callback) {
        Log.i("jsonjson", json);
        postAsyncHttp(url, json, callback, false);
    }

    /**
     * 异步post请求
     *
     * @param url
     * @param json
     * @param callback
     * @param keepGoing 没网情况下是否还是继续往下走
     */
    public void postAsyncHttp(String url, String json, ResultCallback callback, boolean keepGoing) {
        mOkHttpClient=new OkHttpClient();
             Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                    .build();
            Call call = mOkHttpClient.newCall(request);


            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    sendFailedCallback(request, e, callback);
                }
                private void sendFailedCallback(final Request request, final Exception e, final ResultCallback callback) {
                    if (callback != null) {
                        callback.onError(request,e);
                    }
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    callback.onResponse(response.body().string());

                }
            });
    }
}



