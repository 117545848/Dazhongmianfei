package com.dazhongmianfei.dzmfreader.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.view.webview.ReaderWebView;

/**
 * Created by scb on 2018/8/9.
 */
public class AboutActivity extends BaseActivity implements ShowTitle {
    private final String TAG = AboutActivity.class.getSimpleName();
    private WebView mWebView;

    @Override
    public int initContentView() {
        return R.layout.activity_about;
    }
    String flag;
    @Override
    public void initView() {///user/explain

        String str = ReaderConfig.privacy;
        Intent intent = getIntent();
        try {
            flag = intent.getStringExtra("flag");
        } catch (Exception e) {
        }
        if (intent != null && intent.getStringExtra("url") != null) {
            str = intent.getStringExtra("url");
            String title = intent.getStringExtra("title");
            if (title != null) {
                initTitleBarView(title);
            } else {
                initTitleBarView("");//ceshi
            }
            int style = intent.getIntExtra("style", 0);
            if (style == 4) {
                Uri uri = Uri.parse(str);
                Intent intentother = new Intent(Intent.ACTION_VIEW, uri);
                intentother.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                startActivity(intentother);
                finish();
            }
        } else {
            initTitleBarView(LanguageUtil.getString(this, R.string.AboutActivity_title));
            str = "file:///android_asset/web/notify.html";
        }


        mWebView = findViewById(R.id.software_webview);
        WebSettings settings = mWebView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);//解决图片不显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        MyToash.Log(str);
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());  //在前面加入下载监听器
        mWebView.setWebViewClient(new DemoWebViewClient());
        mWebView.loadUrl(str);


    }

    class DemoWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            } catch (Exception s) {
            }
            return true;
        }
    }

    private class MyWebViewDownLoadListener implements DownloadListener {
        //添加监听事件即可
        public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                    String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {
        super.initInfo(json);

    }

    @Override
    public void initTitleBarView(String text) {
        LinearLayout mBack;
        TextView mTitle;
        mBack = findViewById(R.id.titlebar_back);
        mTitle = findViewById(R.id.titlebar_text);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag != null && flag.equals("splash")) {
                    startActivity(new Intent(AboutActivity.this, MainActivity.class));
                }
                finish();
            }
        });
        mTitle.setText(text);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (flag != null && flag.equals("splash")) {
                startActivity(new Intent(AboutActivity.this, MainActivity.class));
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
