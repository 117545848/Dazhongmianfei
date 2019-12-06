package com.dazhongmianfei.dzmfreader.read;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import com.dazhongmianfei.dzmfreader.view.AndroidWorkaround;
import com.jaeger.library.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

import static com.dazhongmianfei.dzmfreader.view.AndroidWorkaround.getNavigationBarHeight2;

/**
 * Created by scb on 2018/5/26.
 */
public abstract class BaseReadActivity extends Activity {
    Activity activity;
    int mScreenHeight;
    public int isNotchEnable, NavigationBarHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        mScreenHeight = ScreenSizeUtils.getInstance(activity).getScreenHeight();
        StatusBarUtil.setTransparent(activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if ( ScreenSizeUtils.isAllScreenDevice(activity)) {
            isNotchEnable = 1;
        }
        setContentView(initContentView());
        // 初始化View注入
        ButterKnife.bind(activity);
        if (AndroidWorkaround.checkDeviceHasNavigationBar(activity)) {                                  //适配华为手机虚拟键遮挡tab的问题
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));                   //需要在setContentView()方法后面执行
           // mScreenHeight -= getNavigationBarHeight2(activity);
        }
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
