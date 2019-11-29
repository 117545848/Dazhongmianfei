package com.dazhongmianfei.dzmfreader.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.dazhongmianfei.dzmfreader.activity.SettingsActivity;

import java.util.Locale;

/**
 * 切换语言的工具类
 *
 * @author : barry.huang
 * @time : 16/9/21
 **/
public class LanguageUtil {

    private static final String LAST_LANGUAGE = "lastLanguage";

    /**
     * 当改变系统语言时,重启App
     *
     * @param activity
     * @param homeActivityCls 主activity
     * @return
     */
    public static boolean isLanguageChanged(Activity activity, Class<?> homeActivityCls) {
        Locale locale = Locale.getDefault();
        if (locale == null) {
            return false;
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        String localeStr = sp.getString(LAST_LANGUAGE, "");
        String curLocaleStr = getLocaleString(locale);
        if (TextUtils.isEmpty(localeStr)) {
            sp.edit().putString(LAST_LANGUAGE, curLocaleStr).commit();
            return false;
        } else {
            if (localeStr.equals(curLocaleStr)) {
                return false;
            } else {
                sp.edit().putString(LAST_LANGUAGE, curLocaleStr).commit();
                restartApp(activity, homeActivityCls);
                return true;
            }
        }
    }

    public static String getString(Context context, int id) {
        String str = "";
        try {
            str = context.getString(id);
        } catch (Exception e) {
            str = context.getResources().getString(id);
        }
        return str;
    }

    private static String getLocaleString(Locale locale) {
        if (locale == null) {
            return "";
        } else {
            return locale.getCountry() + locale.getLanguage();
        }
    }

    public static void restartApp(Activity activity, Class<?> homeClass) {

        Intent intent = new Intent(activity, homeClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        // 杀掉进程
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);

    }


    //刷新显示配置
    public static void reFreshLanguage(Locale locale, Context activity, Class<?> homeClass) {
        if (homeClass == null) {
            String Langaupage = ShareUitls.getString(activity, "Langaupage", "");
            switch (Langaupage) {
                case  "":
                    return;
                case  "TW":
                    locale=Locale.TRADITIONAL_CHINESE;
                    break;
                case  "CN":
                    locale=Locale.SIMPLIFIED_CHINESE;
                    break;
                case  "GB":
                    locale=Locale.UK;
                    break;
            }
        }
        Resources resources = activity.getResources();
        Locale locale_cu;
        Configuration configuration = resources.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale_cu = configuration.getLocales().get(0);
        } else {
            locale_cu = configuration.locale;
        }
        MyToash.Log("reFreshLanguage", locale.getCountry() + "  A " + locale.getLanguage() + "  B  "
                + locale_cu.getCountry() + "  C " + locale_cu.getLanguage());

        if (!locale_cu.getLanguage().equals(locale.getLanguage()) || !locale_cu.getCountry().equals(locale.getCountry())) {
            DisplayMetrics metrics = resources.getDisplayMetrics();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLocale(locale);
            } else {
                configuration.locale = locale;
            }
            activity.getResources().updateConfiguration(configuration, metrics);
            SettingsActivity.chengeLangaupage = true;
            ShareUitls.putString(activity, "Langaupage", locale.getCountry());

            if (homeClass != null) {
                //重新启动Activity
                Intent intent = new Intent(activity, homeClass);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);

            }


          /*  // 杀掉进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);*/

        } else {

        }
    }

  /*  //得到App配置文件目前的语言
    public String getLanguage(Activity activity) {
        if (config) {
            //获得资源Resources对象
            Resources resources = activity.getApplicationContext().getResources();
            //得到返回当前对象的语言"en""zh"等
            defaultLanguage = resources.getConfiguration().locale.getLanguage();
            config = false;
            return "en";
        } else {
            Resources resources = activity.getApplicationContext().getResources();
            defaultLanguage = resources.getConfiguration().locale.getLanguage();
            config = true;
            return "zh";
        }
    }*/

    //创建新的Locale并覆盖原Locale
    public static Locale getLocale(String language) {
        //根据传进来的语言 生成一个语言环境Locale
        Locale localeLocale = new Locale(language);
        //将传进来的语言环境变成本地默认的，进行更换前的准备，返回这个语言环境Locale
        Locale.setDefault(localeLocale);
        return localeLocale;
    }

    /**
     * 创建Configuration
     *
     * @param language
     * @return
     */
    public static Configuration getUpdateLocalConfig(String language, Activity activity) {
        //得到本地资源配置对象
        Configuration localConfiguration = activity.getResources().getConfiguration();
        //得到最新的语言环境Locale
        Locale localLocale = getLocale(language);
        //资源配置对象设置语言环境
        localConfiguration.locale = localLocale;
        return localConfiguration;
    }

}
