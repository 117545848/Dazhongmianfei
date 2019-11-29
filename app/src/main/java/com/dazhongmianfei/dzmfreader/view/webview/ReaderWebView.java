package com.dazhongmianfei.dzmfreader.view.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.utils.NetType;
import com.dazhongmianfei.dzmfreader.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

public class ReaderWebView extends WebView {
    private static final String TAG = ReaderWebView.class.getSimpleName();
    public static Dialog mDialog = null;
    protected int mTimeOut = 30 * 1000;
    protected Timer mTimer;
    private WebViewCallback mCallback;
    private Context mContext;
    private Activity mActivity;

    public ReaderWebView(Context context) {
        super(context);
        mContext = context;
        init(context);
    }

    public ReaderWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context);
    }

    public ReaderWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(context);
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public Dialog getDialog() {
        return mDialog;
    }


    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                if (mCallback != null)
                    mCallback.onLoadFailure(msg.obj.toString());
            } else if (msg.what == 2) {
                stopLoading();
            }
        }
    };

    @SuppressLint("SetJavaScriptEnabled")
    private void init(final Context context) {
        // 配置WebView
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setBuiltInZoomControls(false);
        settings.setUseWideViewPort(true); // 让浏览器支持用户自定义view
        settings.setBlockNetworkImage(true);// 把图片加载放在最后来加载渲染
        settings.setRenderPriority(RenderPriority.HIGH);// 提高渲染的优先级
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(false);
        settings.setAllowUniversalAccessFromFileURLs(false);
        settings.setTextZoom(100);

        if (NetType.TYPE_NONE.equals(Utils.checkNet(mContext))) {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        } else {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        }

        String webviewCacheDir = mContext.getApplicationContext().getDir(ReaderConfig.mWebviewCacheDir, Context.MODE_PRIVATE).getPath();
        String appCacheDir = mContext.getApplicationContext().getDir(ReaderConfig.mAppCacheDir, Context.MODE_PRIVATE).getPath();
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabasePath(webviewCacheDir);
        settings.setAppCachePath(appCacheDir);


        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(final WebView view, final String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                try {
                    if (mDialog == null)
                        mDialog = Utils.showProgressBar(context);
                    else if (!mDialog.isShowing()) {
                        mDialog.show();
                    }

                    mTimer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(2);
                            onReceivedError(view, -6, "Connection time-out", url);
                        }
                    };
                    mTimer.schedule(task, mTimeOut);// 30秒超时时间过后也要关闭加载对话框

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (getProgress() < 10 || errorCode == -2) {
                    handler.sendMessage(handler.obtainMessage(1, failingUrl));
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.cancel();
                    mDialog = null;
                }

                if (mCallback != null)
                    mCallback.onLoadSuccess();
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer.purge();
                    mTimer = null;
                }
                getSettings().setBlockNetworkImage(false);// 把图片加载放在最后来加载渲染
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http") || url.startsWith("https")) {
                    //原始的自己的
                    Utils.printLog(TAG, "当前url：" + url);
                    view.loadUrl(url);// 点击连接从自身打开
                    return true;
                } else {
                    return false;
                }

            }

            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return super.shouldInterceptRequest(view, url);
            }
        });

    }

    public void setOnLoadCallback(WebViewCallback dataCallBack) {
        this.mCallback = dataCallBack;
    }
}
