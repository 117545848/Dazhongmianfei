package com.dazhongmianfei.dzmfreader.utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.WindowManager;


/**
 * Created by abc on 2016/9/1.
 */
public class GetWindowSize {
    private static GetWindowSize GetWindowSize;
    private static Activity context;
    private static DisplayMetrics outMetrics;

    private GetWindowSize() {
    }

    public static GetWindowSize getInstance(Activity activity) {
        //context = activity;
        WindowManager manager = activity.getWindowManager();
        outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        if (GetWindowSize == null) {
            synchronized (GetWindowSize.class) {
                if (GetWindowSize == null) {
                    GetWindowSize = new GetWindowSize();
                }

            }
        }
        return GetWindowSize;
    }

    public  int getGetWindowwidth() {
        return outMetrics.widthPixels;
    }
    public  int getGetWindowheight() {
        return outMetrics.heightPixels;
    }
}
