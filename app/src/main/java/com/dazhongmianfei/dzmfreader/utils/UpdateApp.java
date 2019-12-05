package com.dazhongmianfei.dzmfreader.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.bean.AppUpdate;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.dialog.GetDialog;
import com.dazhongmianfei.dzmfreader.http.DownloadUtil;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.view.ProgressBarView;


import org.json.JSONObject;
//.HttpManager;
//.common.Callback;
//.http.RequestParams;
//.view.annotation.ViewInject;
//.x;

import java.io.File;


import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.PUTPRODUCT_TYPE;

/**
 * Created by scb on 2018/11/28.
 */

public class UpdateApp {
    Activity activity;


    public UpdateApp(Activity activity) {
        this.activity = activity;
    }

    public UpdateApp() {
    }

    public interface UpdateAppInterface {
        void Next(String str);
    }

    public void getRequestData(final UpdateAppInterface updateAppInterface) {
        ReaderParams readerParams = new ReaderParams(activity);
        String json = readerParams.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParams3(ReaderConfig.mAppUpdateUrl, json, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        //Log.i("respon检查版本号", response);
                        ShareUitls.putString(activity, "Update", response);
                        updateAppInterface.Next(response);


                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        //Log.i("respon检查版本号", "ex");
                        String response = ShareUitls.getString(activity, "Update", "");
                        updateAppInterface.Next(response);
                    }
                }

        );


    }

    public void getCheck_switch(final UpdateAppInterface updateAppInterface) {
        ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("channel", getChannelName(activity));
        String json = params.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParams3(ReaderConfig.check_switch, json, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        ShareUitls.putString(activity, "Check_switch", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("service") == 1) {//开启壳子
                                activity.startActivity(new Intent(activity, com.dazhongmianfei.dzmfreader.localapp.MainActivity.class));
                            } else {
                                updateAppInterface.Next(response);
                            }
                        } catch (Exception e) {
                            updateAppInterface.Next(response);
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        String response = ShareUitls.getString(activity, "Check_switch", "");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("service") == 1) {//开启壳子
                                activity.startActivity(new Intent(activity, com.dazhongmianfei.dzmfreader.localapp.MainActivity.class));
                            } else {
                                updateAppInterface.Next(response);
                            }
                        } catch (Exception e) {
                            updateAppInterface.Next(response);
                        }
                    }
                }

        );


    }

    //获取当前 的 渠道号
    public static String getChannelName(Context activity) {
        String channelName = null;
        try {
            PackageManager packageManager = activity.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.
                        getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = String.valueOf(applicationInfo.metaData.get("UMENG_CHANNEL"));
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (channelName == null || channelName.equals("null")) ? "huawei" : channelName;
    }
    /*
    *
    * 360
wandoujia
yingyongbao
baiduzhushou
oppo
vivo
xiaomi
huawei


ios
    *
    * */
    class UpdateHolder {
        @BindView(R2.id.dialog_updateapp_sec)
        public TextView dialog_updateapp_sec;

        @BindView(R2.id.dialog_updateapp_no)
        public TextView dialog_updateapp_no;
        @BindView(R2.id.dialog_updateapp_yes)
        public TextView dialog_updateapp_yes;
        @BindView(R2.id.dialog_updateapp_layout)
        public LinearLayout dialog_updateapp_layout;

        @BindView(R2.id.dialog_updateapp_view)
        public View dialog_updateapp_view;
        @BindView(R2.id.materialSeekBar)
        public ProgressBarView materialSeekBar;

        public UpdateHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    public interface SuccessInstal {

        void success(String address);
    }

    public Dialog popupWindow;
    boolean flag = false;
    String urlmd5;
    String applocal;

    public Dialog getAppUpdatePop(final Activity activity, final AppUpdate mAppUpdate) {
        this.activity = activity;
        if (mAppUpdate.getUrl() == null) {
            return null;
        }
        urlmd5 = Utils.MD5(mAppUpdate.getUrl() + Utils.getAppVersionName(activity));
        applocal = FileManager.getSDCardRoot() + "/Apk/" + urlmd5 + "/dazhongmianfei.apk";
        File tempfile = new File(applocal);
        if (tempfile.exists()) {
            GetDialog.IsOperation(activity, "升级", "新版本安装包已准备就绪，是否更新？", new GetDialog.IsOperationInterface() {
                @Override
                public void isOperation() {
                    installApp(activity, tempfile);
                }
            });
            return null;
        }
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_update_app, null);
        //  int width = ScreenSizeUtils.getInstance(activity).getScreenWidth() - ImageUtil.dp2px(activity, 60);
        popupWindow = new Dialog(activity, R.style.updateapp);
        final UpdateHolder updateHolder = new UpdateHolder(view);

        updateHolder.dialog_updateapp_sec.setText(mAppUpdate.getMsg());

        updateHolder.dialog_updateapp_sec.setMovementMethod(ScrollingMovementMethod.getInstance());
        if (mAppUpdate.getUpdate() == 1) {
            updateHolder.dialog_updateapp_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        } else {
            updateHolder.dialog_updateapp_view.setVisibility(View.GONE);
            updateHolder.dialog_updateapp_no.setVisibility(View.GONE);
        }
        updateHolder.dialog_updateapp_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateHolder.materialSeekBar.setVisibility(View.VISIBLE);
                updateHolder.dialog_updateapp_layout.setVisibility(View.GONE);
                // popupWindow.dismiss();
                MyToash.Log("onDownloading", "111 " + flag);
                if (!flag) {
                    flag = true;
                    downloadApk(activity, mAppUpdate, updateHolder);
                }
            }
        });
        popupWindow.setContentView(view);
        popupWindow.setCancelable(false);
        popupWindow.setCanceledOnTouchOutside(false);
        popupWindow.show();
        return popupWindow;
    }

    /**
     * 下载新版本
     */
    protected void downloadApk(final Activity activity, final AppUpdate mAppUpdate, final UpdateHolder updateHolder) {

        File tempfile = new File(FileManager.getSDCardRoot() + "/Apk/" + urlmd5 + "/");
        if (!tempfile.exists()) {
            tempfile.mkdirs();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                DownloadUtil.get().download(mAppUpdate.getUrl(), tempfile.getAbsolutePath(), "dazhongmianfeitemp.apk",
                        new DownloadUtil.OnDownloadListener() {
                            @Override
                            public void onDownloadSuccess(File file) {
                                updateHolder.materialSeekBar.setProgress(100);
                                try {
                                    File file1 = new File(applocal);
                                    file.renameTo(file1);
                                    installApp(activity, file1);
                                } catch (Exception e) {
                                }
                                installApp(activity, file);
                            }

                            @Override
                            public void onDownloading(int total, int progress) {
                                updateHolder.materialSeekBar.setProgress((int) ((float) progress * 100 / (float) total));
                            }

                            @Override
                            public void onDownloadFailed(Exception e) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyToash.ToashError(activity, "网络异常,升级失败");
                                    }
                                });
                            }
                        });
            }
        }).start();

    }

    public static void installApp(Context pContext, File pFile) {
        if (null == pFile)
            return;
        if (!pFile.exists())
            return;
        Log.i("installApp", pFile.toString());
        Intent _Intent = new Intent();
        _Intent.setAction(Intent.ACTION_VIEW);
        Uri _uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            _Intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            _uri = FileProvider.getUriForFile(pContext, pContext.getPackageName() + ".fileProvider", pFile);
        } else {
            _uri = Uri.fromFile(pFile);
            _Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        _Intent.setDataAndType(_uri, "application/vnd.android.package-archive");
        pContext.startActivity(_Intent);
    }

}
