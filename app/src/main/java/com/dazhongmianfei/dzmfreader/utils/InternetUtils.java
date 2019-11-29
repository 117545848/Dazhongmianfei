package com.dazhongmianfei.dzmfreader.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.dazhongmianfei.dzmfreader.R;

/**
 * Created by scb on 2016/2/16.
 */
public class InternetUtils {
    // / 没有连接
    public static final int NETWORN_NONE = 0;
    // / wifi连接
    public static final int NETWORN_WIFI = 1;
    // / 手机网络数据连接
    public static final int NETWORN_2G = 2;
    public static final int NETWORN_3G = 3;
    public static final int NETWORN_4G = 4;
    public static final int NETWORN_MOBILE = 5;
    static  boolean ISfirst_tixing;

    public static boolean internet(final Context context) {
        //检查当前网络连接
        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return true;
        } else {
            if(!ISfirst_tixing) {
                ISfirst_tixing=true;
               MyToash.ToashError(context, LanguageUtil.getString(context, R.string.splashactivity_nonet));
            }
            //  NoNetWorkNotification(context);
        }
        return false;

    }

    public static boolean internet2(final Context context) {
        //检查当前网络连接
        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return true;
        }
        return false;

    }

    private static ConnectivityManager connectivityManager;


    private static void NoNetWorkNotification(final Context context) {
        try {
            MyToash.ToashError(context, LanguageUtil.getString(context, R.string.splashactivity_nonet));
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage(LanguageUtil.getString(context, R.string.splashactivity_nonet))//设置显示的内容
                    .setNegativeButton(LanguageUtil.getString(context, R.string.splashactivity_cancle), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton(LanguageUtil.getString(context, R.string.splashactivity_set), new DialogInterface.OnClickListener() {//添加确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
// 跳转到系统的网络设置界面
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            ((Activity) (context)).startActivityForResult(intent, 100);


                        }

                    }).show();
        } catch (Exception E) {
        }
    }

    public static boolean internett(final Context context) {
        //检查当前网络连接
        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null;

    }

    public static boolean internetNoWifi(final Context context) {
        //检查当前网络连接
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return true;

        } else {
            MyToash.ToashError(context, "当前无网络连接");
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage("当前无网络连接，是否前去设置网络？")//设置显示的内容
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {//添加确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
// 跳转到系统的网络设置界面
                            Intent intent = null;
                            // 先判断当前系统版本
                            if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上
                                intent = new Intent(Settings.ACTION_SETTINGS);
                            } else {
                                intent = new Intent();
                                intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
                            }
                            context.startActivity(intent);


                        }

                    }).show();
            return false;
        }


    }

    public static int getNetworkState(Context context) {

        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        if (null == connectivityManager)

            return NETWORN_NONE;

        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {

            return NETWORN_NONE;

        }

        // Wifi

        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (null != wifiInfo) {

            NetworkInfo.State state = wifiInfo.getState();

            if (null != state)

                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {

                    return NETWORN_WIFI;

                }

        }

        // 网络

        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (null != networkInfo) {

            NetworkInfo.State state = networkInfo.getState();

            String strSubTypeName = networkInfo.getSubtypeName();

            if (null != state)

                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {

                    switch (activeNetInfo.getSubtype()) {

                        case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g

                        case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g

                        case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g

                        case TelephonyManager.NETWORK_TYPE_1xRTT:

                        case TelephonyManager.NETWORK_TYPE_IDEN:

                            return NETWORN_2G;

                        case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g

                        case TelephonyManager.NETWORK_TYPE_UMTS:

                        case TelephonyManager.NETWORK_TYPE_EVDO_0:

                        case TelephonyManager.NETWORK_TYPE_HSDPA:

                        case TelephonyManager.NETWORK_TYPE_HSUPA:

                        case TelephonyManager.NETWORK_TYPE_HSPA:

                        case TelephonyManager.NETWORK_TYPE_EVDO_B:

                        case TelephonyManager.NETWORK_TYPE_EHRPD:

                        case TelephonyManager.NETWORK_TYPE_HSPAP:

                            return NETWORN_3G;

                        case TelephonyManager.NETWORK_TYPE_LTE:

                            return NETWORN_4G;

                        default://有机型返回16,17

                            //中国移动 联通 电信 三种3G制式

                            if (strSubTypeName.equalsIgnoreCase("TD-SCDMA") || strSubTypeName.equalsIgnoreCase("WCDMA") || strSubTypeName.equalsIgnoreCase("CDMA2000")) {

                                return NETWORN_3G;

                            } else {

                                return NETWORN_MOBILE;

                            }

                    }

                }

        }

        return NETWORN_NONE;

    }


}