package com.dazhongmianfei.dzmfreader.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

public abstract class BaseButterKnifeActivity extends FragmentActivity {
    public Activity activity;

    public abstract int initContentView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        //侵染状态栏
        //  StatusBarUtil.setTransparent(this);
        //注入布局
        setContentView(initContentView());
        // 初始化View注入
        ButterKnife.bind(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        StatusBarUtil.transparencyBar2(this, R.color.white, true);
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

    @Override
    protected void onDestroy() {
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
        }
        super.onDestroy();
    }
}
