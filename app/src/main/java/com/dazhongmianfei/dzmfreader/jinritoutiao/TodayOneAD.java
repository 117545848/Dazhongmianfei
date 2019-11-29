package com.dazhongmianfei.dzmfreader.jinritoutiao;

import android.app.Activity;
import android.app.NativeActivity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.activity.AcquireBaoyueActivity;
import com.dazhongmianfei.dzmfreader.config.MainHttpTask;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyPicasso;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;
import com.dazhongmianfei.dzmfreader.utils.UpdateApp;
import com.hubcloud.adhubsdk.AdHub;
import com.hubcloud.adhubsdk.AdRequest;
import com.hubcloud.adhubsdk.NativeAd;
import com.hubcloud.adhubsdk.NativeAdListener;
import com.hubcloud.adhubsdk.NativeAdResponse;
import com.hubcloud.adhubsdk.RewardItem;
import com.hubcloud.adhubsdk.RewardedVideoAd;
import com.hubcloud.adhubsdk.RewardedVideoAdListener;
import com.hubcloud.adhubsdk.internal.nativead.NativeAdEventListener;
import com.hubcloud.adhubsdk.internal.nativead.NativeAdUtil;
import com.hubcloud.adhubsdk.internal.network.ServerResponse;
import com.hz.yl.b.HhInfo;
import com.hz.yl.b.mian.HmNative;
import com.hz.yl.b.mian.NativeListener;
import com.hz.yl.b.mian.UpLoadPay;
import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TodayOneAD {
    int flag;
    private NativeAd nativeAd;
    private List<? extends View> mAdViewList;
    public boolean is_getNativeInfoListView;
    public FrameLayout frameLayoutToday;

    public void nativeRender() {
        if (is_getNativeInfoListView  && nativeAd != null&&mAdViewList!=null&&!mAdViewList.isEmpty()&&frameLayoutToday!=null) {
            for (int i = 0; i < mAdViewList.size(); i++) {
                View lyAdView = mAdViewList.get(i);
                frameLayoutToday.addView(lyAdView);
                nativeAd.nativeRender(lyAdView);//这个方法需要广告真正显示到屏幕上的时候再去调用
            }
        }
    }

    public void getTodayOneBanner(final FrameLayout frameLayoutToday, final FrameLayout frameLayoutToday1, int flag) {
        this.flag = flag;
        this.frameLayoutToday = frameLayoutToday;
//        int currentAD = (int) (Math.random() * 2);
        int currentAD = 1;
        switch (currentAD) {
            case 0:
                is_getNativeInfoListView = false;
                if (frameLayoutToday1 != null) {
                    frameLayoutToday1.setVisibility(View.GONE);
                    frameLayoutToday.setVisibility(View.VISIBLE);
                }
                adViewHolder.iv_listitem_icon_.setImageResource(R.mipmap.chuanshanjia);
                if (mTTAdNative == null) {
                    mTTAdNative = TTAdManagerHolder.get().createAdNative(activity);
                }
                loadTodayOneBannerAdXINXILIU(frameLayoutToday, flag);
                break;
            case 1:
                nativeAd = new NativeAd(activity, "9040", 3, new NativeAdListener() {
                    @Override
                    public void onAdFailed(int errorcode) {
                    }

                    @Override
                    public void onAdClick() {
                    }

                    @Override
                    public void onAdLoaded(NativeAdResponse response) {
                        Log.i("lance", (response.getNativeInfoListView() != null) + "");
                        if (response.getNativeInfoListView() != null) {
                            is_getNativeInfoListView = true;
                            mAdViewList = response.getNativeInfoListView();
                            frameLayoutToday.removeAllViews();
                            if (flag != 1) {
                                nativeRender();
                            }
                        } else {
                            is_getNativeInfoListView = false;
                            invoke();
                            adViewHolder.mTitle.setText(response.getHeadline());
                            //需要开发者自己处理图片URL，可使用图片加载框架去处理，本示例使用glide加载仅供参考
                            try {
                                Glide.with(activity).load(Uri.parse(response.getImageUrls().get(0))).into(adViewHolder.mImage);
                                // Glide.with(activity).load(Uri.parse(response.getImageUrls().get(1))).into((ImageView) findViewById(R.id.iv_native2));
                                // Glide.with(activity).load(Uri.parse(response.getImageUrls().get(2))).into((ImageView) findViewById(R.id.iv_native3));
                            } catch (Exception ignored) {
                                Log.e("lance", "Exception:" + ignored.getMessage());
                            }
                            try {
                                adViewHolder.mCreativeButton.setText(response.getTexts().get(0));
                                adViewHolder.mDescription.setText(response.getTexts().get(1));
                            } catch (Exception ignored) {
                                Log.e("lance", "Exception:" + ignored.getMessage());
                            }
                            //sdk内部提供了以下方法，可以将一个view加上logo并返回一个加入了logo的framelayout替代原本无logo的view;
                            //注意调用了此方法之后原来的view将不存在于之前的布局之中，须将返回的framelayout加入之前的布局。
                            //若此方法不满足要求，请开发者自己实现加入logo及广告字样
                            FrameLayout frameLayout = NativeAdUtil.addADLogo(frameLayoutToday, response);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                            layoutParams.gravity = Gravity.CENTER;
                            frameLayoutToday.addView(frameLayout, 0, layoutParams);

                            // This must be called.
                            //注册原生广告展示及点击曝光，必须调用。
                            NativeAdUtil.registerTracking(response, frameLayoutToday, new NativeAdEventListener() {
                                @Override
                                public void onAdWasClicked() {
                                    //Toast.makeText(NativeActivity.this, "onAdWasClicked", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onAdWillLeaveApplication() {
                                    // Toast.makeText(NativeActivity.this, "onAdWillLeaveApplication", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                //加载广告
                nativeAd.loadAd();
                break;
        }

    }


    TTAdNative mTTAdNative;
    Activity activity;
    TTFeedAd ttFeedAd;
    View convertView;
    public AdViewHolder adViewHolder;
    String daimaweiID;

    public TodayOneAD(Activity activity, int flag/*,String daimaweiID*/) {
        this.activity = activity;
        //    invoke();
    }

    public TodayOneAD(Activity activity, int flag, String daimaweiID) {
        this.daimaweiID = daimaweiID;
        this.activity = activity;
        //  invoke();
    }

    public void setShelftodayOneAD(Activity activity, RelativeLayout shelf, ImageView shelfImageView, TextView shelfttitle, TextView shelfdes) {
        if (mTTAdNative == null) {
            mTTAdNative = TTAdManagerHolder.get().createAdNative(activity);
            if (mTTAdNative != null) {
                AdSlot adSlot = new AdSlot.Builder()
                        .setCodeId(daimaweiID)
                        .setSupportDeepLink(true)
                        .setImageAcceptedSize(228, 150)
                        .setAdCount(1) //请求广告数量为1到3条
                        .build();
                mTTAdNative.loadFeedAd(adSlot, new TTAdNative.FeedAdListener() {
                    @Override
                    public void onError(int code, String mTTAdNative) {
                        Log.i("mTTAdNative", code + "   " + mTTAdNative);
                    }

                    @Override
                    public void onFeedAdLoad(List<TTFeedAd> ads) {
                        MyToash.Log("onFeedAdLoad", (ads != null) ? (ads.size() + "") : "ssss");
                        if (ads == null || ads.isEmpty()) {
                            return;
                        }
                        TTFeedAd ad = ads.get(0);
                        if (ad.getTitle() != null) {
                            shelfttitle.setText(ad.getTitle()); //title为广告的简单信息提示
                        }
                        shelfdes.setText(ad.getDescription()); //description为广告的较长的说明
                        boolean b = ad.getImageList() != null && !ad.getImageList().isEmpty();
                        if (b) {
                            TTImage image = ad.getImageList().get(0);
                            if (image != null && image.isValid()) {
                                MyPicasso.GlideImageNoSize(activity, image.getImageUrl(), shelfImageView);
                            }
                        }
                        List<View> clickViewList = new ArrayList<>();
                        clickViewList.add(shelf);
                        List<View> creativeViewList = new ArrayList<>();
                        creativeViewList.add(shelfttitle);
                        creativeViewList.add(shelfImageView);
                        creativeViewList.add(shelfdes);
                        ad.registerViewForInteraction(shelf, clickViewList, creativeViewList, new TTNativeAd.AdInteractionListener() {
                            @Override
                            public void onAdClicked(View view, TTNativeAd ad) {
                                if (ad != null) {
                                    MyToash.Toash(activity, "广告" + ad.getTitle() + "被点击");
                                }
                            }

                            @Override
                            public void onAdCreativeClick(View view, TTNativeAd ad) {
                                if (ad != null) {
                                    ///  MyToash.Toash(mContext, "广告" + ad.getTitle() + "被创意按钮被点击");
                                }
                            }

                            @Override
                            public void onAdShow(TTNativeAd ad) {
                                if (ad != null) {
                                    // MyToash.Toash(mContext, "广告" + ad.getTitle() + "展示");
                                }
                            }
                        });

                    }
                });
            }

        } else {

        }


    }

    public TodayOneAD(String daimaweiID) {
        if (daimaweiID == null) {
            daimaweiID = "925050605";
        }
        this.daimaweiID = daimaweiID;
    }

    public TodayOneAD() {

    }

    public class AdViewHolder {
        //  @BindView(R2.id.iv_listitem_icon)
        //  ImageView mIcon;
        @BindView(R2.id.btn_listitem_creative)
        public Button mCreativeButton;
        @BindView(R2.id.tv_listitem_ad_title)
        public TextView mTitle;
        @BindView(R2.id.tv_listitem_ad_desc)
        public TextView mDescription;
        @BindView(R2.id.tv_listitem_ad_source)
        public TextView mSource;
        @BindView(R2.id.iv_listitem_image)
        public ImageView mImage;
        @BindView(R2.id.iv_listitem_icon_)
        public ImageView iv_listitem_icon_;


        @OnClick(value = {R.id.tv_listitem_VIP})
        public void getEvent(View view) {
            switch (view.getId()) {
                case R.id.tv_listitem_VIP:
                    if (MainHttpTask.getInstance().Gotologin(activity)) {
                        activity.startActivityForResult(new Intent(activity, AcquireBaoyueActivity.class), 301);
                    }
                    break;


            }
        }

        public AdViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private void loadTodayOneBannerAdXINXILIU(final FrameLayout frameLayoutToday, final int flag) {
        invoke();

        //step4:创建feed广告请求类型参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = null;
        switch (flag) {
            case 0:
                adSlot = new AdSlot.Builder()
                        .setCodeId(daimaweiID)
                        .setSupportDeepLink(true)
                        .setImageAcceptedSize(690, 388)
                        .setAdCount(1) //请求广告数量为1到3条
                        .build();
                break;
            case 1:
            case 2:
            case 3:
                adSlot = new AdSlot.Builder()
                        .setCodeId(daimaweiID)
                        .setSupportDeepLink(true)
                        .setImageAcceptedSize(228, 150)
                        .setAdCount(1) //请求广告数量为1到3条
                        .build();
                break;
        }
      /*  if (flag == 0) {
            if (frameLayoutToday.getVisibility() == View.VISIBLE) {
                return;
            }
        }
*/
        //step5:请求广告，调用feed广告异步请求接口，加载到广告后，拿到广告素材自定义渲染
        mTTAdNative.loadFeedAd(adSlot, new TTAdNative.FeedAdListener() {
            @Override
            public void onError(int code, String mTTAdNative) {
                Log.i("mTTAdNative", code + "   " + mTTAdNative + "  " + daimaweiID);
            }

            @Override
            public void onFeedAdLoad(List<TTFeedAd> ads) {
                MyToash.Log("onFeedAdLoad", (ads != null) ? (ads.size() + "    " + daimaweiID) : "  AA   " + daimaweiID);
                if (ads == null || ads.isEmpty()) {
                    return;
                }

                ttFeedAd = ads.get(0);
                bindData(frameLayoutToday, flag);
            }
        });


    }


    public void bindData(final FrameLayout frameLayoutToday, int flag) {
        frameLayoutToday.removeAllViews();
        if (ttFeedAd == null) {
            return;
        }
        TTFeedAd ad = ttFeedAd;
        if (ad.getTitle() != null) {
            adViewHolder.mTitle.setText(ad.getTitle()); //title为广告的简单信息提示
        }
        adViewHolder.mDescription.setText(ad.getDescription()); //description为广告的较长的说明
        Button adCreativeButton = adViewHolder.mCreativeButton;
        switch (ad.getInteractionType()) {
            case TTAdConstant.INTERACTION_TYPE_DOWNLOAD:
                adCreativeButton.setText("立即下载");
                adCreativeButton.setVisibility(View.VISIBLE);
                break;
            case TTAdConstant.INTERACTION_TYPE_DIAL:
                adCreativeButton.setVisibility(View.VISIBLE);
                adCreativeButton.setText("立即拨打");
                //  adViewHolder.mStopButton.setVisibility(View.GONE);
                //   adViewHolder.mRemoveButton.setVisibility(View.GONE);
                break;
            case TTAdConstant.INTERACTION_TYPE_LANDING_PAGE:
            case TTAdConstant.INTERACTION_TYPE_BROWSER:
//                    adCreativeButton.setVisibility(View.GONE);
                adCreativeButton.setVisibility(View.VISIBLE);
                adCreativeButton.setText("查看详情");
                // adViewHolder.mStopButton.setVisibility(View.GONE);
                //  adViewHolder.mRemoveButton.setVisibility(View.GONE);
                break;
            default:
                adCreativeButton.setVisibility(View.GONE);
                // adViewHolder.mStopButton.setVisibility(View.GONE);
                // adViewHolder.mRemoveButton.setVisibility(View.GONE);
                //  MyToash.Toash(mContext, "交互类型异常");
        }
        boolean b = ad.getImageList() != null && !ad.getImageList().isEmpty();
        if (b) {
            TTImage image = ad.getImageList().get(0);
            if (image != null && image.isValid()) {
                MyPicasso.GlideImageNoSize(activity, image.getImageUrl(), adViewHolder.mImage);
            }
        }
        frameLayoutToday.addView(convertView);
        List<View> clickViewList = new ArrayList<>();
        clickViewList.add(convertView);
        List<View> creativeViewList = new ArrayList<>();
        creativeViewList.add(adViewHolder.mDescription);
        creativeViewList.add(adViewHolder.mCreativeButton);
        creativeViewList.add(adViewHolder.mTitle);
        ad.setActivityForDownloadApp(activity);


        ad.registerViewForInteraction((ViewGroup) convertView, clickViewList, creativeViewList, new TTNativeAd.AdInteractionListener() {
            @Override
            public void onAdClicked(View view, TTNativeAd ad) {
                if (ad != null) {
                    MyToash.Log("onDownloadFinished---onAdClicked", ad.getTitle());

                    ;
                    //MyToash.Toash(mContext, "广告" + ad.getTitle() + "被点击");
                }
            }

            @Override
            public void onAdCreativeClick(View view, TTNativeAd ad) {
                if (ad != null) {
                    String fileName = ShareUitls.getString(activity, ad.getTitle(), null);
                    if (!TextUtils.isEmpty(fileName)) {
                        UpdateApp.installApp(activity, new File(fileName));
                    }
                    MyToash.Log("onDownloadFinished--onAdCreativeClick", ad.getTitle());

                    ///  MyToash.Toash(mContext, "广告" + ad.getTitle() + "被创意按钮被点击");
                }
            }

            @Override
            public void onAdShow(TTNativeAd ad) {
                if (ad != null) {
                    // MyToash.Toash(mContext, "广告" + ad.getTitle() + "展示");
                }
            }
        });
        if (ad.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            ad.setDownloadListener(new TTAppDownloadListener() {
                @Override
                public void onIdle() {

                }

                @Override
                public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {

                }

                @Override
                public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {

                }

                @Override
                public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {

                }

                @Override
                public void onDownloadFinished(long totalBytes, String fileName, String appName) {

                    Toast.makeText(activity, "下载完成点击安装", Toast.LENGTH_LONG).show();
                    ShareUitls.putString(activity, appName, fileName);

                    // Toast.makeText(activity,"下载完成点击图片安装",Toast.LENGTH_LONG).show();

                    // MyToash.Log("onDownloadFinished---",fileName+"  "+appName);
                    // UpdateApp.installApp(activity,new File(fileName));
                }

                @Override
                public void onInstalled(String fileName, String appName) {
                    // TToast.show(NativeExpressActivity.this, "安装完成，点击图片打开", Toast.LENGTH_LONG);
                    Toast.makeText(activity, "安装完成，点击图片打开", Toast.LENGTH_LONG).show();

                    // MyToash.Log("onDownloadFinished---aa",fileName+"  "+appName);
                    //UpdateApp.installApp(activity,new File(fileName));


                }
            });
        }

        // frameLayout.setVisibility(View.INVISIBLE);

    }


    public interface OnRewardVerify {
        void OnRewardVerify();


    }

    public void loadJiliAd(OnRewardVerify onRewardVerify) {

        RewardedVideoAd mRewardedVideoAd = AdHub.getRewardedVideoAdInstance(activity);

        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewarded(RewardItem rewardItem) {
                Log.d("lance", "onRewarded:" + rewardItem.getType() + "==" + rewardItem.getAmount());
            }

            @Override
            public void onRewardedVideoAdClosed() {
                Log.d("lance", "onRewardedVideoAdClosed");
            }

            @Override
            public void onRewardedVideoAdShown() {
                Log.d("lance", "onRewardedVideoAdShown");
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Log.d("lance", "onRewardedVideoAdFailedToLoad:" + i);
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                Log.d("lance", "onRewardedVideoAdLeftApplication");
            }

            @Override
            public void onRewardedVideoAdLoaded() {
                Log.d("lance", "onRewardedVideoAdLoaded");
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }
            }

            @Override
            public void onRewardedVideoAdOpened() {
                Log.d("lance", "onRewardedVideoAdOpened");
            }

            @Override
            public void onRewardedVideoStarted() {
                Log.d("lance", "onRewardedVideoStarted");
            }
        });

        if (!mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.loadAd("435", new AdRequest.Builder().build());
        }
    }

    LayoutInflater layoutInflater;

    public void invoke() {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(activity);
        }
        switch (flag) {
            case 0:
            case 1:
                convertView = layoutInflater.inflate(R.layout.listitem_ad_large_pic2, null, false);
                break;
            case 2:
                convertView = layoutInflater.inflate(R.layout.listitem_ad_smallpage_pic, null, false);
                break;
            case 3:
                convertView = layoutInflater.inflate(R.layout.listitem_ad_bookread, null, false);
                break;
        }
        if (flag != 4) {
            adViewHolder = new AdViewHolder(convertView);
        }
    }

}
