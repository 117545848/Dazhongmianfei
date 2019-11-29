package com.dazhongmianfei.dzmfreader.utils;

/**
 * Created by scb on 2018/9/4.
 */


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.util.Locale;

/**
 * 系统工具类
 * <p>
 * <p>
 * <p>
 * <p>
 * Created by zhuwentao on 2016-07-18.
 */


public class SystemUtil {


    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */


    /*** 获取当前app version code*/
    public static long getAppVersionCode(Context context) {
        long appVersionCode = 0;
        try {
            PackageInfo packageInfo = context.getApplicationContext().getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                appVersionCode = packageInfo.getLongVersionCode();
            } else {
                appVersionCode = packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
           // Log.e("", e.getMessage());
        }
        return appVersionCode;
    }

    /*** 获取当前app version name*/
    public static String getAppVersionName(Context context) {
        String appVersionName = "";
        try {
            PackageInfo packageInfo = context.getApplicationContext().getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
           // Log.e("", e.getMessage());
        }
        return appVersionName;
    }


    public static String getSystemLanguage() {


        return Locale.getDefault().getLanguage();


    }


    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */


    public static Locale[] getSystemLanguageList() {


        return Locale.getAvailableLocales();


    }


    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */


    public static String getSystemVersion() {


        return android.os.Build.VERSION.RELEASE;


    }


    /**
     * 获取手机型号
     *
     * @return 手机型号
     */


    public static String getSystemModel() {


        return android.os.Build.MODEL;


    }


    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */


    public static String getDeviceBrand() {


        return android.os.Build.BRAND;


    }


    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */


    @SuppressLint("MissingPermission")
    public static String getIMEI(Context ctx) {


        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);


        if (tm != null) {


            return tm.getDeviceId();


        }


        return null;


    }


}