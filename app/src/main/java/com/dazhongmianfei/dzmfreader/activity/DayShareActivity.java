package com.dazhongmianfei.dzmfreader.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.bean.FloatImageViewShow;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONException;
import org.json.JSONObject;
//.view.annotation.ContentView;
//.view.annotation.Event;
//.view.annotation.ViewInject;
//.x;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by abc on 2016/11/4.
 */


public class DayShareActivity extends BaseButterKnifeActivity {
    @Override
    public int initContentView() {
        return R.layout.activity_dayshare;
    }
    @BindView(R2.id.titlebar_back)
    public LinearLayout titlebar_back;
    @BindView(R2.id.titlebar_text)
    public TextView titlebar_text;

    @BindView(R2.id.activity_dayshare_webview)
    public WebView mWebView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titlebar_text.setText("每日分享");
        WebSettings settings = mWebView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptEnabled(true);
       /* settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setLoadWithOverviewMode(true);*/

        mWebView.addJavascriptInterface(new DecoObject() {
            @JavascriptInterface //必须加的注解
            @Override
            public void share(String data) {
                Log.i("DecoObject", data);
//{"type":1,
// "title":"支霄飞",
// "desc":"支霄飞描述",
// "image":"http://haozanw.oss-cn-beijing.aliyuncs.com/e509344721437403ed3c460f56f0fd43.jpg?x-oss-process=image%2Fresize%2Cw_750%2Ch_350%2Cm_lfit",
// "url":"http://mp.haozanw.com/book/share-read?book_id=29&uid=1&aid=1"}

                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int type = jsonObject.getInt("type");
                    String title = jsonObject.getString("title");
                    String desc = jsonObject.getString("desc");
                    String image = jsonObject.getString("image");
                    String url = jsonObject.getString("url");
                    //String url = ReaderConfig.BASE_URL + "/site/share?uid=" + Utils.getUID(BookInfoActivity.this) + "&book_id=" + mBookId + "&osType=2&product=1";
                    UMWeb web = new UMWeb(url);
                    web.setTitle(title);//标题
                    web.setThumb(new UMImage(activity, image));  //缩略图
                    web.setDescription(desc);//描述

                    ShareAction shareAction = new ShareAction(activity);
                    if (type == 1) {
                        shareAction.setPlatform(SHARE_MEDIA.WEIXIN);
                    } else {
                        shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                    }
                    shareAction.withText(desc)//分享内容
                            .setCallback(umShareListener)//回调监听器
                             .withMedia(web)
                            .share();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, "decoObject");
        //   mWebView.loadUrl("file:///android_asset/web/test.html");

        mWebView.loadUrl(FloatImageViewShow.share_read_url);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //  Log.d(tag, " url:"+url);
                view.loadUrl(url);// 当打开新链接时，使用当前的 WebView，不会使用系统其他浏览器
                return true;
            }
        });

     /*   mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //Log.d(tag, " url:"+url);
                view.loadUrl(url);// 当打开新链接时，使用当前的 WebView，不会使用系统其他浏览器
                return true;
            }
        });*/



   /*     mWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);

                mWebView.evaluateJavascript("share(1)", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String responseJson) {
                        Log.i("DecoObject1",responseJson);

                        // LogUtils.d("调用js返回值:" + paths[2] + "--" + responseJson);
                        // analyParams(paths, responseJson);
                    }
                });




               // stopMyDialog();
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {

                super.onPageStarted(view, url, favicon);
               // startMyDialog();
            }
        });*/

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//sdk>19才有用

                    mWebView.evaluateJavascript("share(1)", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String responseJson) {
                            Log.i("DecoObject1",responseJson);

                            // LogUtils.d("调用js返回值:" + paths[2] + "--" + responseJson);
                            // analyParams(paths, responseJson);
                        }
                    });



        } else {//sdk<19后,通过prompt来获取
            // String[] paths = moduleUrl.split("/", 3);
            // promptMap.put(paths[2], paths);
            mWebView.loadUrl("javascript:share(1)");
            //  LogUtils.d("Prompt请求:" + "mandaobridge.getParams('!" + paths[2] + "')");
        }*/


    }

    @OnClick(value = {R.id.titlebar_back})
    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.titlebar_back:
                finish();
                break;


        }
    }

    interface DecoObject {
        @JavascriptInterface
        void share(String data);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }


        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            // getGold();

            MyToash.ToashSuccess(activity, "分享成功");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            MyToash.ToashError(activity, "分享失败");
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            MyToash.ToashError(activity, "分享取消");
        }
    };
}
