package com.dazhongmianfei.dzmfreader.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.dialog.ZToast;


/**
 * Created by abc on 2017/8/30.
 */
public class MyToash {
    public static void Toash(Context activity, String Message) {
        // Toast.makeText(activity, Message, Toast.LENGTH_LONG).show();
      /*  ZToast.init(
                Color.WHITE,
                Color.BLACK,
                false,
                R.mipmap.tips_error,
                90);//参数为 背景色 文字颜色 是否有图标 图标资源 高度*/
        ZToast.MakeText((Activity) activity, Message, 1500, R.mipmap.tips_success).show();
    }

    public static void ToashSuccess(Context activity, String Message) {
        ZToast.MakeText((Activity) activity, Message, 1500, R.mipmap.tips_success).show();
    }

    public static void ToashError(Context activity, String Message) {
        ZToast.MakeText((Activity) activity, Message, 1500, R.mipmap.tips_error).show();
    }


    /*
    * Logger.v(String message); // VERBOSE级别，可添加占位符
Logger.d(Object object); // DEBUG级别，打印对象
Logger.d(String message); // DEBUG级别，可添加占位符
Logger.i(String message); // INFO级别，可添加占位符
Logger.w(String message); // WARN级别，可添加占位符
Logger.e(String message); // ERROR级别，可添加占位符
Logger.e(Throwable throwable, String message); // ERROR级别，可添加占位符
Logger.wtf(String message); // ASSERT级别，可添加占位符
Logger.xml(String xml);
Logger.json(String json);


    *
    * */

    public static void Log(Object Message) {
        if (SUE_LOG) {
            Log.i("dazhongmianfei", Message.toString());
        }
    }

    public static void Log(String Message, String message) {
        if (SUE_LOG) {
            Log.i(Message, message);
            //  LogObject(Message,message);
            //Logger.i(Message + "    " + message);
        }

    }

    public static void LogJson(String Message, String message) {
        if (SUE_LOG) {
            // Log(flag,message);
            Log.i(Message, message);
        }


    }

    public static void Log(String Message, int message) {
        if (SUE_LOG) {
            Log.i(Message, message + "");
        }
    }


    public static void LogObject(String flag, Object o) {
        if (SUE_LOG) {
            // Log(flag);
            Logger.d(o);
        }
    }

    public static boolean SUE_LOG = true;
}
