package com.dazhongmianfei.dzmfreader.utils;

import android.app.Activity;
import android.content.Intent;

;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.activity.LoginActivity;
import com.dazhongmianfei.dzmfreader.activity.RechargeActivity;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.dialog.WaitDialog;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshMine;
import com.dazhongmianfei.dzmfreader.http.OkHttp3Engine;
import com.dazhongmianfei.dzmfreader.http.ResultCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
//.http.RequestParams;

import okhttp3.Request;

/**
 * Created by abc on 2016/7/18.
 */
public class HttpUtils {
    private static HttpUtils httpUtils;
    private static Activity context;
    private WaitDialog waitDialog;

    public interface ResponseListener {
        void onResponse(String response);

        void onErrorResponse(String ex);
    }

    public interface ResponseListenerDialog {
        void onResponse(String response, WaitDialog waitDialog);

        void onErrorResponse(String ex);
    }

    public HttpUtils() {

    }

    public static HttpUtils getInstance(Activity activity) {
        if (activity != null) {

            context = activity;
            // httpManager = x.http();
            if (httpUtils == null) {
                synchronized (HttpUtils.class) {
                    if (httpUtils == null) {
                        httpUtils = new HttpUtils();
                    }

                }
            }
        }
        return httpUtils;
    }


    private void initDialog() {
        if (waitDialog != null) {
            waitDialog.dismissDialog();

        }
        waitDialog = null;

        waitDialog = new WaitDialog(context);
        waitDialog.setCancleable(true);
    }

    public void sendRequestRequestParams3(final String url, final String body, final boolean dialog, final ResponseListener responseListener) {
        if (context == null) {
            responseListener.onErrorResponse(null);
            return;
        }
        if (waitDialog != null) {
            waitDialog.dismissDialog();
        }
        if (InternetUtils.internet(context)) {
            if (dialog) {
                initDialog();
                waitDialog.showDailog();
            }
            MyToash.Log("httpUtils_log21", url + "   " + body);
            OkHttp3Engine.getInstance(context).postAsyncHttp(url, body, new ResultCallback() {
                @Override
                public void onError(Request request, Exception e) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyToash.ToashError(context, LanguageUtil.getString(context, R.string.splashactivity_nonet));
                            responseListener.onErrorResponse(null);

                        }
                    });
                }

                @Override
                public void onResponse(final String result) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MyToash.LogJson("http_utils", result);
                                JSONObject jsonObj = new JSONObject(result);
                                int code = jsonObj.getInt("code");
                                String msg = jsonObj.getString("msg");
                                switch (code) {
                                    case 0:
                                        responseListener.onResponse(jsonObj.getString("data"));
                                        break;
                                    case 315:
                                        MyToash.ToashSuccess(context, msg);
                                        responseListener.onResponse(jsonObj.getString("data"));

                                        break;
                                    case 301://登录已过期
                                        if (Utils.isLogin(context)) {
                                            AppPrefs.putSharedString(context, ReaderConfig.TOKEN, "");
                                            AppPrefs.putSharedString(context, ReaderConfig.UID, "");
                                            ReaderConfig.REFREASH_USERCENTER = true;
                                            EventBus.getDefault().post(new RefreshMine(null));
                                            MyToash.ToashError(context, msg);
                                            context.startActivity(new Intent(context, LoginActivity.class));
                                        }
                                        responseListener.onErrorResponse(null);
                                        break;
                                    case 802://余额不足 充值
                                        MyToash.ToashError(context, msg);
                                        context.startActivity(new Intent(context, RechargeActivity.class));
                                        responseListener.onErrorResponse(null);
                                        break;
                                    case 701://余额不足 充值
                                        MyToash.ToashError(context, msg);
                                        responseListener.onErrorResponse("701");
                                        break;
                                    default:
                                        if (code != 311 && code != 300) {//今日已签到//用户不存在
                                            MyToash.ToashError(context, msg);
                                        }
                                        responseListener.onErrorResponse(null);
                                        break;
                                }

                            } catch (JSONException j) {
                            }
                            if (waitDialog != null) {
                                waitDialog.dismissDialog();
                            }


                        }
                    });

                }
            });
        } else {
            MyToash.Log("getCurrentComicChapter", "  sss");
            responseListener.onErrorResponse("nonet");
        }

    }

    //下发了 菊花等待
    public void sendRequestRequestParamsDialog(final String url, final String body, final ResponseListenerDialog responseListener) {
        if (context == null) {
            responseListener.onErrorResponse(null);
            return;
        }
        if (waitDialog != null) {
            waitDialog.dismissDialog();
        }
        if (InternetUtils.internet(context)) {
            initDialog();
            waitDialog.showDailog();
            OkHttp3Engine.getInstance(context).postAsyncHttp(url, body, new ResultCallback() {
                @Override
                public void onError(Request request, Exception e) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyToash.ToashError(context, LanguageUtil.getString(context, R.string.splashactivity_nonet));
                            responseListener.onErrorResponse(null);

                        }
                    });
                }

                @Override
                public void onResponse(final String result) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int code = 0;
                            try {
                                MyToash.LogJson("http_utils", result);
                                JSONObject jsonObj = new JSONObject(result);
                                code = jsonObj.getInt("code");
                                String msg = jsonObj.getString("msg");
                                switch (code) {
                                    case 0:
                                        responseListener.onResponse(jsonObj.getString("data"), waitDialog);
                                        break;
                                    case 301://登录已过期
                                        if (Utils.isLogin(context)) {
                                            AppPrefs.putSharedString(context, ReaderConfig.TOKEN, "");
                                            AppPrefs.putSharedString(context, ReaderConfig.UID, "");
                                            ReaderConfig.REFREASH_USERCENTER = true;
                                            EventBus.getDefault().post(new RefreshMine(null));
                                            MyToash.ToashError(context, msg);
                                        }
                                       /* if (LoginActivity.activity == null) {
                                            context.startActivity(new Intent(context, LoginActivity.class));
                                        }*/
                                        responseListener.onErrorResponse("301");


                                        break;
                                    case 802://余额不足 充值
                                        MyToash.ToashError(context, msg);
                                        context.startActivity(new Intent(context, RechargeActivity.class));
                                        responseListener.onErrorResponse(null);
                                        break;
                                    case 701://余额不足 充值
                                        MyToash.ToashError(context, msg);
                                        responseListener.onErrorResponse("701");
                                        break;
                                    default:
                                        if (code != 311 && code != 300) {//今日已签到//用户不存在
                                            MyToash.ToashError(context, msg);
                                        }
                                        responseListener.onErrorResponse(null);
                                        break;
                                }

                            } catch (JSONException j) {
                            }
                            if (code != 0) {
                                if (waitDialog != null) {
                                    waitDialog.dismissDialog();
                                }
                            }


                        }
                    });

                }
            });
        } else {
            MyToash.Log("getCurrentComicChapter", "  sss");
            responseListener.onErrorResponse("nonet");
        }

    }
    public void sendRequestRequestParams4(final String url, final String body,  final ResponseListener responseListener) {

        if (InternetUtils.internet(context)) {
            OkHttp3Engine.getInstance(context).postAsyncHttp(url, body, new ResultCallback() {
                @Override
                public void onError(Request request, Exception e) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyToash.ToashError(context, LanguageUtil.getString(context, R.string.splashactivity_nonet));
                            responseListener.onErrorResponse(null);

                        }
                    });
                }

                @Override
                public void onResponse(final String result) {
                    try {
                        JSONObject jsonObj = new JSONObject(result);
                        int code = jsonObj.getInt("code");
                        switch (code) {
                            case 0:
                                responseListener.onResponse(jsonObj.getString("data"));
                                break;

                        }
                    } catch (Exception e) {
                    }
                }
            });
        } else {
            MyToash.Log("getCurrentComicChapter", "  sss");
            responseListener.onErrorResponse("nonet");
        }

    }

}


