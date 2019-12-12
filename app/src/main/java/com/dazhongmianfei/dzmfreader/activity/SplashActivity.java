package com.dazhongmianfei.dzmfreader.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Message;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import android.widget.ImageView;

import com.dazhongmianfei.dzmfreader.bean.UserInfoItem;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.jinritoutiao.WeakHandler;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.Utils;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppWakeUpAdapter;
import com.fm.openinstall.model.AppData;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.google.gson.Gson;
import com.hubcloud.adhubsdk.AdListener;
import com.hubcloud.adhubsdk.SplashAd;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.config.MainHttpTask;
import com.dazhongmianfei.dzmfreader.utils.FileManager;
import com.dazhongmianfei.dzmfreader.utils.InternetUtils;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;
import com.dazhongmianfei.dzmfreader.utils.UpdateApp;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dazhongmianfei.dzmfreader.read.util.PageFactory.IS_VIP;
import static com.dazhongmianfei.dzmfreader.read.util.PageFactory.close_AD;


/**
 * 闪屏页
 */


public class SplashActivity extends Activity {

    public Activity activity;
    String isfirst;
    UpdateApp updateApp;
    @BindView(R2.id.activity_splash_im)
    public ImageView activity_splash_im;
    @BindView(R2.id.splash_container)
    public FrameLayout mSplashContainer;
    //开屏广告加载超时时间,建议大于3000,这里为了冷启动第一次加载到广告并且展示,示例设置了3000ms
    private static final int AD_TIME_OUT = 3000;
    private static final int MSG_GO_MAIN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取唤醒参数
        activity = SplashActivity.this;

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        updateApp = new UpdateApp(activity);
        //首次启动 flag
        isfirst = ShareUitls.getString(activity, "isfirst", "yes");
        if (isfirst.equals("yes")) {//首次使用删除文件
            FileManager.deleteFile(FileManager.getSDCardRoot());
        }
        OpenInstall.getWakeUp(getIntent(), wakeUpAdapter);
        //获取OpenInstall安装数据
        requestReadPhoneState();
        if (Utils.isLogin(activity)) {
            ReaderParams params = new ReaderParams(activity);
            String json = params.generateParamsJson();
            HttpUtils.getInstance(activity).sendRequestRequestParams3(ReaderConfig.mUserCenterUrl, json, true, new HttpUtils.ResponseListener() {
                        @Override
                        public void onResponse(final String result) {
                            UserInfoItem mUserInfo = new Gson().fromJson(result, UserInfoItem.class);
                            if (mUserInfo!=null&&mUserInfo.getIs_vip() == 1) {
                                IS_VIP=true;
                            }
                        }

                        @Override
                        public void onErrorResponse(String ex) {

                        }
                    }
            );
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 此处要调用，否则App在后台运行时，会无法截获
        OpenInstall.getWakeUp(intent, wakeUpAdapter);
    }

    AppWakeUpAdapter wakeUpAdapter = new AppWakeUpAdapter() {
        @Override
        public void onWakeUp(AppData appData) {
            //获取渠道数据
            String channelCode = appData.getChannel();
            //获取绑定数据
            String bindData = appData.getData();
            Log.d("OpenInstall", "getWakeUp : wakeupData = " + appData.toString());
        }
    };

    //开屏广告
    SplashAd splashAd;

    private void loadspad() {
        splashAd = new SplashAd(this, mSplashContainer, listener, "9039");
        splashAd.setCloseButtonPadding(10, 20, 10, 10);
    }

    @Override
    protected void onDestroy() {
        if (splashAd != null) {
            splashAd.cancel();
        }

        super.onDestroy();
        //  wakeUpAdapter = null;
    }


    private void requestReadPhoneState() {

        if (PermissionsUtil.hasPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            hasPermission();
        } else {
            PermissionsUtil.requestPermission(this, new PermissionListener() {
                        @Override
                        public void permissionGranted(@NonNull String[] permission) {
                            //判断是否开启壳子
                            hasPermission();

                        }

                        @Override
                        public void permissionDenied(@NonNull String[] permission) {//@Nullable String title,  @Nullable String content,  @Nullable String cancel,  @Nullable String ensure
                            finish();
                        }
                    }, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    true, new PermissionsUtil.TipInfo("开启权限",
                            "「 " + activity.getString(R.string.app_name) + " 」需要开启储存卡使用权限才能正常使用",
                            "取消", "设置"));
        }
    }

    private void hasPermission() {
        //loadspad();
        updateApp.getCheck_switch(new UpdateApp.UpdateAppInterface() {
            @Override
            public void Next(String response) {
                MyToash.Log("AppUpdate", "0");
                next();

                MainHttpTask.getInstance().InitHttpData(activity);

            }
        });
    }


    public void next() {
        updateApp.getRequestData(new UpdateApp.UpdateAppInterface() {
            @Override
            public void Next(String response) {
                goToMainActivity();
            }
        });

    }

    private void goToMainActivity() {

        if (InternetUtils.internett(activity) && isfirst.equals("yes")) {
            startActivity(new Intent(activity, FirstStartActivity.class));
        } else startActivity(new Intent(activity, MainActivity.class));
        this.finish();
    }

    /**
     * 调用设备同步接口
     */
    //防止用户点击返回取消广告展示
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canJumpImmediately) {
            jumpWhenCanClick();
        }
        canJumpImmediately = true;
        MobclickAgent.onResume(this); // 基础指标统计，不能遗漏
    }

    @Override
    protected void onPause() {
        super.onPause();
        canJumpImmediately = false;
        MobclickAgent.onPause(this); // 基础指标统计，不能遗漏
    }

    AdListener listener = new AdListener() {
        @Override
        public void onAdLoaded() {
            activity_splash_im.setVisibility(View.GONE);
            Log.i("lance", "onAdLoaded");
            //Toast.makeText(SplashActivity.this, "onAdLoaded", //Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdShown() {
            Log.i("lance", "onAdShown");
            //Toast.makeText(SplashActivity.this, "onAdShown", //Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            Log.i("lance", "onAdFailedToLoad");
            //Toast.makeText(SplashActivity.this, "onAdFailedToLoad", //Toast.LENGTH_SHORT).show();
            goToMainActivity();
        }

        @Override
        public void onAdClosed() {
            Log.i("lance", "onAdClosed");
            jumpWhenCanClick(); // 跳转至您的应用主界面
            //Toast.makeText(SplashActivity.this, "onAdClosed", //Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdClicked() {
            Log.i("lance", "onAdClick");
            // 设置开屏可接受点击时，该回调可用
        }
    };
    public boolean canJumpImmediately = false;

    private void jumpWhenCanClick() {
        Log.d("lance", "canJumpImmediately:" + canJumpImmediately);
        if (canJumpImmediately) {
            goToMainActivity();
        } else {
            canJumpImmediately = true;
        }
    }

}
