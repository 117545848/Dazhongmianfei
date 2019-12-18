package com.dazhongmianfei.dzmfreader.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;

import androidx.annotation.NonNull;

import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.config.MainHttpTask;
import com.dazhongmianfei.dzmfreader.utils.FileManager;
import com.dazhongmianfei.dzmfreader.utils.InternetUtils;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;
import com.dazhongmianfei.dzmfreader.utils.UpdateApp;
import com.kuaiyou.loader.AdViewSpreadManager;
import com.kuaiyou.loader.InitSDKManager;
import com.kuaiyou.loader.loaderInterface.AdViewSpreadListener;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dazhongmianfei.dzmfreader.read.util.PageFactory.IS_VIP;
import static com.dazhongmianfei.dzmfreader.read.util.PageFactory.close_AD;


/**
 * 闪屏页
 */


public class SplashActivity extends Activity implements AdViewSpreadListener {

    public Activity activity;
    String isfirst;
    UpdateApp updateApp;
    @BindView(R.id.splash_container)
    public RelativeLayout mSplashContainer;

    @BindView(R.id.activity_splash_im)
    public View activity_splash_im;


    private AdViewSpreadManager adSpreadBIDView = null;
    private int count = 1;
    public String[] permissions = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取唤醒参数
        activity = SplashActivity.this;

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        InitSDKManager.getInstance().init(this, ReaderConfig.appId, null);
        //下载类广告默认弹出二次确认框，如需关闭提示请设置如下；设置后对全部广告生效
        InitSDKManager.setDownloadNotificationEnable(false);
        //在调用SDK之前，如果您的App的targetSDKVersion >= 23，那么一定要把"READ_PHONE_STATE"、"WRITE_EXTERNAL_STORAGE"、"ACCESS_FINE_LOCATION"这几个权限申请到


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

    private void requestSpreadAd() {
        adSpreadBIDView = new AdViewSpreadManager(this,ReaderConfig.appId,
                mSplashContainer);
        adSpreadBIDView.setBackgroundColor(Color.WHITE);
        adSpreadBIDView.setSpreadNotifyType(AdViewSpreadManager.NOTIFY_COUNTER_NUM);

        adSpreadBIDView.setOnAdViewListener(this
        );
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
        requestSpreadAd();
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
                //goToMainActivity();
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

    @SuppressLint("ResourceType")
    @Override
    public void onAdNotifyCustomCallback(final int ruleTime, final int delayTime) {
        final TextView tv1 = new TextView(this);
        final Button btn1 = new Button(this);
        final RelativeLayout.LayoutParams btnLp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        final RelativeLayout.LayoutParams tvLp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        btn1.setId(123123);
        btn1.setText("Skip");
        tv1.setText(ruleTime + delayTime + "");

        btnLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tvLp.addRule(RelativeLayout.LEFT_OF, btn1.getId());

        adSpreadBIDView.getParentLayout().postDelayed(new Runnable() {

            @Override
            public void run() {
                btn1.setVisibility(View.VISIBLE);
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adSpreadBIDView.cancelAd();
                    }
                });
            }
        }, ruleTime * 1000);
        adSpreadBIDView.getParentLayout().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (ruleTime + delayTime - count >= 1) {
                    tv1.setText(ruleTime + delayTime - count + "");
                    count++;
                    adSpreadBIDView.getParentLayout().postDelayed(this, 1000);
                }
            }
        }, 1000);
        adSpreadBIDView.getParentLayout().addView(btn1, btnLp);
        adSpreadBIDView.getParentLayout().addView(tv1, tvLp);
        btn1.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onAdClicked() {
        Log.i("AdViewBID", "onAdClicked");
    }

    @Override
    public void onAdClosed() {
        Log.i("AdViewBID", "onAdClosedAd");
        jump();
    }

    @Override
    public void onAdClosedByUser() {
        Log.i("AdViewBID", "onAdClosedByUser");
        jump();
    }

    @Override
    public void onAdDisplayed() {
        Log.i("AdViewBID", "onAdDisplayed");
    }

    @Override
    public void onAdFailedReceived(String arg1) {
        Log.i("AdViewBID", arg1);
        jump();
    }



    @Override
    public void onAdReceived() {
        activity_splash_im.setVisibility(View.GONE);
        Log.i("AdViewBID", "onAdRecieved");
    }

    @Override
    public void onAdSpreadPrepareClosed() {
        Log.i("AdViewBID", "onAdSpreadPrepareClosed");
        jump();
    }

    private void jump() {
        goToMainActivity();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != adSpreadBIDView)
            adSpreadBIDView.destroy();
    }
}
