package com.dazhongmianfei.dzmfreader.activity.model;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by scb on 2018/7/14.
 */
public abstract class BaseModel {

    public final int SUCCESS = 0x00;
    public final int FAILURE = 0x01;
    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    initInfo((String) msg.obj);
                    break;
                case FAILURE:
                    break;
            }

        }
    };

    public void handleResult(String result) {
        Message msg = new Message();
        msg.what = SUCCESS;
        msg.obj = result;
        mHandler.sendMessage(msg);

    }

    public void handleResult(String result, int what) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = result;
        mHandler.sendMessage(msg);
    }


    /**
     * 处理网络请求数据
     *
     * @param json
     */
    public abstract void initInfo(String json);


}
