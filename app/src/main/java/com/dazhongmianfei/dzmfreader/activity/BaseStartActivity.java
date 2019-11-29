package com.dazhongmianfei.dzmfreader.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/**
 * 启动页的父类  全屏显示
 * Created by SCB on 2018/8/27.
 */
public abstract class BaseStartActivity extends Activity {
    public final int SUCCESS = 0x00;
    public final int FAILURE = 0x01;
    public final int STARTMAINACTIVITY = 0x02;
    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    initInfo((String) msg.obj);
                    break;
                case FAILURE:
                    break;
                case STARTMAINACTIVITY:
                    Log.i("checkVersion", "3");
                    Log.i("STARTMAINACTIVITY","STARTMAINACTIVITY");
                    Activity activity=(Activity)msg.obj;
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(initContentView());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 初始化View注入
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }




    /**
     * 配置布局文件
     *
     * @return
     */
    public abstract int initContentView();

    /**
     * 初始化各个视图
     */
    public abstract void initView();

    /**
     * 发起网络请求
     */
    public abstract void initData();

    /**
     * 处理网络请求数据
     *
     * @param json
     */
    public void initInfo(String json) {

    }

    public void handleResult(String result) {
        Message msg = new Message();
        msg.what = SUCCESS;
        msg.obj = result;
        mHandler.sendMessage(msg);

    }
    public  void startMainActivity(Activity activity){
        Log.i("checkVersion", "2");
        Message message=Message.obtain();
        message.what=STARTMAINACTIVITY;
        message.obj=activity;
        mHandler.sendMessageDelayed(message,2000);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); // 基础指标统计，不能遗漏
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); // 基础指标统计，不能遗漏
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void test(String entity) {

    }
}
