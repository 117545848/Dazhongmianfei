package com.dazhongmianfei.dzmfreader.utils;

import android.content.Context;
import android.content.SharedPreferences;


import java.io.File;

/**
 * Created by Administrator on 2015/8/18.
 */
public class ShareUitls {


    public static boolean getFirstAddShelf(Context c, String key, boolean d) {
        SharedPreferences sp = c.getSharedPreferences("FirstAddShelf.xml", Context.MODE_PRIVATE);
        return sp.getBoolean(key, d);
    }
    public static void putFirstAddShelf(Context c, String key, boolean msg) {
        SharedPreferences sp = c.getSharedPreferences("FirstAddShelf.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean(key, msg);
        e.commit();
    }
    public static int getTab(Context c, String key, int d) {
        SharedPreferences sp = c.getSharedPreferences("dazhongmianfeitab.xml", Context.MODE_PRIVATE);
        return sp.getInt(key, d);
    }
    public static void putTab(Context c, String key, int msg) {
        SharedPreferences sp = c.getSharedPreferences("dazhongmianfeitab.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putInt(key, msg);
        e.commit();
    }
    public static int getComicDownStatus(Context c, String key, int d) {//0 未下载 1 已下载 2 下载中  3 下载失败
        SharedPreferences sp = c.getSharedPreferences("ComicDownStatus.xml", Context.MODE_PRIVATE);
        return sp.getInt(key, d);
    }

    public static void putComicDownStatus(Context c, String key, int msg) {
        SharedPreferences sp = c.getSharedPreferences("ComicDownStatus.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putInt(key, msg);
        e.apply();
    }




    public static String getMainHttpTaskString(Context c, String key, String d) {
        SharedPreferences sp = c.getSharedPreferences("MainHttpTask.xml", Context.MODE_PRIVATE);
        return sp.getString(key, d);
    }
    public static void putMainHttpTaskString(Context c, String key, String msg) {
        SharedPreferences sp = c.getSharedPreferences("MainHttpTask.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString(key, msg);
        e.commit();
    }

    public static void clearComicDownStatus(Context c) {
        SharedPreferences sp = c.getSharedPreferences("ComicDownStatus.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.clear();
        e.apply();
    }

    public static boolean getBoolean(Context c, String key, boolean d) {
        SharedPreferences sp = c.getSharedPreferences("BOOLEAN.xml", Context.MODE_PRIVATE);
        return sp.getBoolean(key, d);
    }

    public static void putBoolean(Context c, String key, boolean msg) {
        SharedPreferences sp = c.getSharedPreferences("BOOLEAN.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean(key, msg);
        e.apply();
    }



    public static int getInt(Context c, String key, int d) {
        SharedPreferences sp = c.getSharedPreferences("downoption.xml", Context.MODE_PRIVATE);
        return sp.getInt(key, d);
    }

    public static void putInt(Context c, String key, int msg) {
        SharedPreferences sp = c.getSharedPreferences("downoption.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putInt(key, msg);
        e.commit();
    }

    public static String getString(Context c, String key, String d) {
        SharedPreferences sp = c.getSharedPreferences("dazhongmianfeistring.xml", Context.MODE_PRIVATE);
        return sp.getString(key, d);
    }
    public static void putString(Context c, String key, String msg) {
        SharedPreferences sp = c.getSharedPreferences("dazhongmianfeistring.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString(key, msg);
        e.commit();
    }

    public static String getDown(Context c, String key, String d) {
        SharedPreferences sp = c.getSharedPreferences("downoption.xml", Context.MODE_PRIVATE);
        return sp.getString(key, d);
    }

    public static void putDown(Context c, String key, String msg) {
        SharedPreferences sp = c.getSharedPreferences("downoption.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString(key, msg);
        e.commit();
    }

}
