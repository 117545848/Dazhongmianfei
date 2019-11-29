package com.dazhongmianfei.dzmfreader.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by scb on 2018/8/9.
 */
public class ScreenSizeUtils {
    private static ScreenSizeUtils instance = null;
    private int screenWidth, screenHeight,screenWidthDP, screenHeightDP;

    public static ScreenSizeUtils getInstance(Context mContext) {
        if (instance == null) {
            synchronized (ScreenSizeUtils.class) {
                if (instance == null)
                    instance = new ScreenSizeUtils(mContext);
            }
        }
        return instance;
    }

    private ScreenSizeUtils(Context mContext) {
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        screenHeight = getScreenRealHeight(mContext);// 获取屏幕分辨率高度

        screenWidthDP = ImageUtil.dp2px(mContext,screenHeightDP);// 获取屏幕分辨率宽度
        screenHeightDP =ImageUtil.dp2px(mContext,screenHeight);// 获取屏幕分辨率高度
    }

    //获取屏幕宽度
    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenWidthDP() {
        return screenWidthDP;
    }

    public void setScreenWidthDP(int screenWidthDP) {
        this.screenWidthDP = screenWidthDP;
    }

    public int getScreenHeightDP() {
        return screenHeightDP;
    }

    public void setScreenHeightDP(int screenHeightDP) {
        this.screenHeightDP = screenHeightDP;
    }

    //获取屏幕高度
    public int getScreenHeight() {
        return screenHeight;
    }


    private static final int PORTRAIT = 0;
    private static final int LANDSCAPE = 1;
    @NonNull
    private volatile static Point[] mRealSizes = new Point[2];


    public static int getScreenRealHeight(@Nullable Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return getScreenHeight(context);
        }

        int orientation = context != null
                ? context.getResources().getConfiguration().orientation
                : context.getResources().getConfiguration().orientation;
        orientation = orientation == Configuration.ORIENTATION_PORTRAIT ? PORTRAIT : LANDSCAPE;

        if (mRealSizes[orientation] == null) {
            WindowManager windowManager = context != null
                    ? (WindowManager) context.getSystemService(Context.WINDOW_SERVICE)
                    : (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager == null) {
                return getScreenHeight(context);
            }
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            mRealSizes[orientation] = point;
        }
        return mRealSizes[orientation].y;
    }

    public static int getScreenHeight(@Nullable Context context) {
        if (context != null) {
            return context.getResources().getDisplayMetrics().heightPixels;
        }
        return 0;
    }

    private volatile static boolean mHasCheckAllScreen;
    private volatile static boolean mIsAllScreenDevice;

    public static boolean isAllScreenDevice(Context context) {
        if (mHasCheckAllScreen) {
            return mIsAllScreenDevice;
        }
        mHasCheckAllScreen = true;
        mIsAllScreenDevice = false;
        // 低于 API 21的，都不会是全面屏。。。
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            float width, height;
            if (point.x < point.y) {
                width = point.x;
                height = point.y;
            } else {
                width = point.y;
                height = point.x;
            }
            if (height / width >= 1.97f) {
                mIsAllScreenDevice = true;
            }
        }
        return mIsAllScreenDevice;
    }


}
