package com.dazhongmianfei.dzmfreader.push;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.activity.AboutActivity;
import com.dazhongmianfei.dzmfreader.activity.BookInfoActivity;
import com.dazhongmianfei.dzmfreader.activity.MainActivity;
import com.dazhongmianfei.dzmfreader.comic.activity.ComicInfoActivity;
import com.dazhongmianfei.dzmfreader.dialog.GetDialog;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 用于接收推送的通知和消息
 */
public class ReaderMessageReceiver extends MessageReceiver {

    // 消息接收部分的LOG_TAG
    public static final String REC_TAG = ReaderMessageReceiver.class.getSimpleName();

    /**
     * 推送通知的回调方法
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        // TODO 处理推送通知
        if (null != extraMap) {
            for (Map.Entry<String, String> entry : extraMap.entrySet()) {
                Utils.printLog("onNotification1", "@Get diy param : Key=" + entry.getKey() + " , Value=" + entry.getValue());
            }
        } else {
            Utils.printLog("onNotification1", "@收到通知 && 自定义消息为空");
        }
        Utils.printLog("onNotification11", "收到一条推送通知 ： " + title + "  " + summary);

        if (extraMap != null && !extraMap.isEmpty() && extraMap.get("skip_type") != null) {
            sendNotification(context, title, summary, extraMap.get("skip_type"), extraMap.get("content"));
        } else {
            sendNotification(context, title, summary, null, null);
        }


    }

    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        Utils.printLog(REC_TAG, "onNotificationReceivedInApp ： " + " : " + title + " : " + summary + "  " + extraMap + " : " + openType + " : " + openActivity + " : " + openUrl);
    }





    public static void sendNotification(final Context context, String title, String summary, String skip_type, String content) {
        if (!isNotificationEnabled(context)) {
            GetDialog.IsOperation(MainActivity.activity, "设置消息通知", "允许「" + (context.getResources().getString(R.string.app_name) + "」消息通知更多精彩不再错过"), new GetDialog.IsOperationInterface() {
                @Override
                public void isOperation() {
                    gotoNotificationSetting(MainActivity.activity);
                }
            });
            return;
        }
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); // 为该通知设置一个id
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "com.dazhongmianfei.dzmfreader");
        notification.setContentTitle(title)
                .setContentText(summary)
                .setSmallIcon(R.mipmap.appicon)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setVisibility(Notification.VISIBILITY_PRIVATE)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_ALL | Notification.DEFAULT_SOUND)
                .setVibrate(new long[]{1000, 500, 1000});

        Intent intent = new Intent();
        if(content!=null && content.length()!=0) {
            switch (skip_type) {
                case "1":
                    intent.setClass(context, BookInfoActivity.class);
                    intent.putExtra("book_id", content);
                    break;
                case "2":
                    intent.setClass(context, AboutActivity.class);
                    intent.putExtra("url", content);
                    intent.putExtra("flag", "skip_url");
                    break;
                case "3":
                    intent.setClass(context, ComicInfoActivity.class);
                    intent.putExtra("comic_id", content);
                    break;
                default:
                    intent.setClass(context, MainActivity.class);
                    break;
            }
        }else {
            intent.setClass(context, MainActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntents = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        notification.setContentIntent(pendingIntents);
        mNotificationManager.cancelAll();
        mNotificationManager.notify("com.dazhongmianfei.dzmfreader", 1, notification.build());

    }


    /**
     * 推送消息的回调方法    *
     *
     * @param context
     * @param cPushMessage
     */
    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        MyToash.Log("otificationMessage",cPushMessage.getTitle()+"   "+cPushMessage.getContent());
 /*       try {
            Utils.printLog(REC_TAG, "收到一条推送消息 ： " + cPushMessage.getTitle());
            // sendNotification(context, cPushMessage.getTitle(), cPushMessage.getContent());
        } catch (Exception e) {
            Utils.printLog(REC_TAG, e.toString());
        }*/
    }

    /**
     * 从通知栏打开通知的扩展处理
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
/*
        CloudPushService cloudPushService = PushServiceFactory.getCloudPushService();
//        cloudPushService.setNotificationSoundFilePath();
        Utils.printLog(REC_TAG, "onNotificationOpened ： " + " : " + title + " : " + summary + " : " + extraMap);
        //{"action":"goDetail","_ALIYUN_NOTIFICATION_ID_":"39077","bookId":"170"}这种样式，取bookId，跳书籍详情页
        if (!TextUtils.isEmpty(extraMap)) {
            try {
                JSONObject object = new JSONObject(extraMap);
                if (object.has("action")) {
                    if (object.getString("action").equals("goDetail")) {
                        if (object.has("bookId")) {
                            if (!TextUtils.isEmpty(object.getString("bookId"))) {
                                String bookId = object.getString("bookId");
                                Intent intent = new Intent(context, BookInfoActivity.class);
                                intent.putExtra("book_id", bookId);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }
        //默认打开首页
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
*/

    }

    @Override
    public void onNotificationRemoved(Context context, String messageId) {
        Utils.printLog(REC_TAG, "onNotificationRemoved ： " + messageId);
    }

    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        Utils.printLog(REC_TAG, "onNotificationClickedWithNoAction ： " + " : " + title + " : " + summary + " : " + extraMap);
    }

    public static boolean isNotificationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return isEnableV26(context);
        } else {
            return isEnableV19(context);
        }
    }

    /**
     * Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
     * 19及以上
     *
     * @param context
     * @return
     */
    public static boolean isEnableV19(Context context) {
        AppOpsManager mAppOps = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;

            Class appOpsClass;
            try {
                appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                        String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

                int value = (Integer) opPostNotificationValue.get(Integer.class);
                return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

            } catch (ClassNotFoundException e) {
                //sTAG, e);
            } catch (NoSuchMethodException e) {
                //sTAG, e);
            } catch (NoSuchFieldException e) {
                //sTAG, e);
            } catch (InvocationTargetException e) {
                //sTAG, e);
            } catch (IllegalAccessException e) {
                //sTAG, e);
            }
        }
        return false;
    }

    /**
     * Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
     * 针对8.0及以上设备
     *
     * @param context
     * @return
     */
    public static boolean isEnableV26(Context context) {
        try {
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            Method sServiceField = notificationManager.getClass().getDeclaredMethod("getService");
            sServiceField.setAccessible(true);
            Object sService = sServiceField.invoke(notificationManager);

            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;

            Method method = sService.getClass().getDeclaredMethod("areNotificationsEnabledForPackage"
                    , String.class, Integer.TYPE);
            method.setAccessible(true);
            return (boolean) method.invoke(sService, pkg, uid);
        } catch (Exception e) {
            //sTAG, e);
        }
        return false;
    }


    public static void gotoNotificationSetting(Activity activity) {
        ApplicationInfo appInfo = activity.getApplicationInfo();
        String pkg = activity.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, pkg);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid);
                //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                intent.putExtra("app_package", pkg);
                intent.putExtra("app_uid", uid);
                activity.startActivityForResult(intent, 1);
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                activity.startActivityForResult(intent, 1);
            } else {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                activity.startActivityForResult(intent, 1);
            }
        } catch (Exception e) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            activity.startActivityForResult(intent, 1);
            //sTAG, e);
        }
    }

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

}