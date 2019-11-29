package com.dazhongmianfei.dzmfreader.http;


import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/4/16.
 */
public abstract class ResultCallback<T> {
    public abstract void onError(Request request, Exception e);

    public abstract void onResponse(String response);

    public void onResponse(Response response) {

    }
}

