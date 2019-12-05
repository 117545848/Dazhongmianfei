package com.dazhongmianfei.dzmfreader.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.bean.BaseAd;
;
import com.dazhongmianfei.dzmfreader.config.ReaderApplication;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;

import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.Utils;
import com.dazhongmianfei.dzmfreader.view.webview.ReaderWebView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.MANHAU;

/**
 *
 */
public class WebViewActivity extends BaseButterKnifeActivity {
    @Override
    public int initContentView() {
        return R.layout.activity_about;
    }

    @BindView(R2.id.software_webview)
    public ReaderWebView mWebView;
    @BindView(R2.id.titlebar_text)
    public TextView titlebar_text;

    @OnClick(value = {R.id.titlebar_back
    })
    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.titlebar_back:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }


    public void initView() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String title = intent.getStringExtra("title");
        if (title != null) {
            titlebar_text.setText(title);
        }
        if (url == null) {
            return;
        }
        WebSettings settings = mWebView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);//解决图片不显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        MyToash.Log(url);
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());  //在前面加入下载监听器
        mWebView.setWebViewClient(new DemoWebViewClient());
        mWebView.loadUrl(url);
        String advert_id=intent.getStringExtra("advert_id");
        if(advert_id!=null) {
            onclicAD(advert_id);
        }
    }

    class DemoWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            } catch (Exception e) {
            }
            return true;
        }
    }

    private class MyWebViewDownLoadListener implements DownloadListener {
        //添加监听事件即可
        public void onDownloadStart(String url,
                                    String userAgent, String contentDisposition,
                                    String mimetype, long contentLength) {
            try {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } catch (Exception e) {
            }
        }
    }

    //加载webview 广告
    private void onclicAD(String advert_id) {
        ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("advert_id", MANHAU + "");
        String json = params.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParams3(ReaderConfig.BASE_URL+"/advert/click", json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {

                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );
    }
}
