package com.dazhongmianfei.dzmfreader.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowInsets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by scb on 2018/11/30.
 */

public class NotchScreen {
    /**
     * 判断是否是刘海屏
     * @return
     */
    public static boolean hasNotchScreen(Activity activity){
        return getInt("ro.miui.notch", activity) == 1 || hasNotchAtHuawei(activity) || hasNotchAtOPPO(activity)
                || hasNotchAtVivo(activity) || isAndroidP(activity) != null;

    }

    /**
     * Android P 刘海屏判断
     * @param activity
     * @return
     */
    public static DisplayCutout isAndroidP(Activity activity){
        View decorView = activity.getWindow().getDecorView();
        if (decorView != null && android.os.Build.VERSION.SDK_INT >= 28){
            WindowInsets windowInsets = decorView.getRootWindowInsets();
            if (windowInsets != null)
                return windowInsets.getDisplayCutout();
        }
        return null;
    }

    /**
     * 小米刘海屏判断.
     * @return 0 if it is not notch ; return 1 means notch
     //* @throws IllegalArgumentException if the key exceeds 32 characters
     */

    public static int getInt(String key,Activity activity) {
        int result = 0;
        if (isXiaomi()) {
            try {
                ClassLoader classLoader = activity.getClassLoader();
                @SuppressWarnings("rawtypes")
                Class SystemProperties = classLoader.loadClass("android.os.SystemProperties");
                //参数类型
                @SuppressWarnings("rawtypes")
                Class[] paramTypes = new Class[2];
                paramTypes[0] = String.class;
                paramTypes[1] = int.class;
                Method getInt = SystemProperties.getMethod("getInt", paramTypes);
                //参数
                Object[] params = new Object[2];
                params[0] = new String(key);
                params[1] = new Integer(0);
                result = (Integer) getInt.invoke(SystemProperties, params);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static int getIntt(String key, Activity activity) {
        int result = 0;
        try {
           // result= SystemProperties.getInt("ro.miui.notch", 0);
        } catch (Exception e) {
        } catch (Error error) {
        }
        return result;
       /* int result = 0;
        if (isXiaomi()){
            try {
                ClassLoader classLoader = activity.getClassLoader();
                @SuppressWarnings("rawtypes")
                Class SystemProperties = classLoader.loadClass("android.os.SystemProperties");
                //参数类型
                @SuppressWarnings("rawtypes")
                Class[] paramTypes = new Class[2];
                paramTypes[0] = String.class;
                paramTypes[1] = int.class;
                Method getInt = SystemProperties.getMethod("getInt", paramTypes);
                //参数
                Object[] params = new Object[2];
                params[0] = new String(key);
                params[1] = new Integer(0);
                result = (Integer) getInt.invoke(SystemProperties, params);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return result;*/
    }

    /**
     * 华为刘海屏判断
     * @return
     */
    public static boolean hasNotchAtHuawei(Activity context) {
        boolean ret = false;
        try {
            ClassLoader classLoader =context.getClassLoader();
            Class HwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
           // AppLog.e("hasNotchAtHuawei ClassNotFoundException");
        } catch (NoSuchMethodException e) {
       //     AppLog.e("hasNotchAtHuawei NoSuchMethodException");
        } catch (Exception e) {
          //  AppLog.e( "hasNotchAtHuawei Exception");
        } finally {
            return ret;
        }
    }

    public static final int VIVO_NOTCH = 0x00000020;//是否有刘海
    public static final int VIVO_FILLET = 0x00000008;//是否有圆角

    /**
     * VIVO刘海屏判断
     * @return
     */
    public static boolean hasNotchAtVivo(Activity context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class FtFeature = classLoader.loadClass("android.util.FtFeature");
            Method method = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) method.invoke(FtFeature, VIVO_NOTCH);
        } catch (ClassNotFoundException e) {
          //  AppLog.e( "hasNotchAtVivo ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            //AppLog.e(  "hasNotchAtVivo NoSuchMethodException");
        } catch (Exception e) {
           // AppLog.e(  "hasNotchAtVivo Exception");
        } finally {
            return ret;
        }
    }
    /**
     * OPPO刘海屏判断
     * @return
     */
    public static boolean hasNotchAtOPPO(Activity context) {
        return  context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }
    public static boolean hasNotchInScreen(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
          //  Log.e("test", "hasNotchInScreen ClassNotFoundException");
        } catch (NoSuchMethodException e) {
          //  Log.e("test", "hasNotchInScreen NoSuchMethodException");
        } catch (Exception e) {
         //   Log.e("test", "hasNotchInScreen Exception");
        } finally {
            return ret;
        }
    }

    // 是否是小米手机
    public static boolean isXiaomi() {
        return "Xiaomi".equals(Build.MANUFACTURER);
    }
}
