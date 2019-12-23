package com.dazhongmianfei.dzmfreader.config;

import android.app.Activity;


import com.dazhongmianfei.dzmfreader.http.OkHttp3Engine;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.http.ResultCallback;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by scb on 2018/5/27.
 */
public class ReaderConfig {
    //本应用的appkey
    public static final String mAppkey = "dazhongmianfeiv3Android";
    //本应用的appsecret
    public static final String mAppSecretKey = "1XGRoGKNSubMgvA1kWnD4u19MBpj0Y65";
    //本应用的IP域名
    public static final String BASE_URL = "http://open.damii.cn";

    public static final String TENCENT_AD_APPID = "1109934888";


    public static final String appId = "SDK20191817061259eyrxgg0pwde6fqj";

    public static final String appId2 = "SDK201910201012126cs6htga2iiozgx";


    public static final String posId = "NATIVEchzdqc0qachn";

    //appid 微信分配的APPID
    public static final String WEIXIN_PAY_APPID = "wx5b7302b335d969fc";
    //appid 微信分配的SECRET
    public static final String WEIXIN_APP_SECRET = "444a5b63498cd532da845fe1c02d9f68";
    //友盟统计
    public static final String UMENG = "5b716a23a40fa323ca000063";
    //QQ分享
    public static final String QQ_APPID = "101738731";
    //QQ_SECRET
    public static final String QQ_SECRET = "f47bd0bfa74725136e3e226fc338e6af";
    // 当前产品类型 1 只使用小说     2 只使用漫画     3  小说 漫画             4  漫画小说
    private static int PRODUCT_TYPE = 3;

    public static int GETPRODUCT_TYPE(Activity activity) {
       /* if (PRODUCT_TYPE == 0) {
            PRODUCT_TYPE = ShareUitls.getInt(activity, "PRODUCT_TYPE", 3);
        }*/
        return XIAOSHUO;
    }
    public static void PUTPRODUCT_TYPE(Activity activity,int  UI) {
        PRODUCT_TYPE=   UI;
        ShareUitls.putInt(activity, "PRODUCT_TYPE", PRODUCT_TYPE);
    }
    public static final int MIANFEI = 0;//免费频道
    public static final int WANBEN = 1;//完本频道
    public static final int SHUKU = 2;//书库频道
    public static final int PAIHANG = 3;//排行频道
    public static final int PAIHANGINSEX = 10;//排行首页频道
    public static final int BAOYUE = 4;//包月
    public static final int BAOYUE_SEARCH = 5;//包月赛选列表


    public static final int DOWN = 6;//下载频道
    public static final int READHISTORY = 7;//阅读历史频道
    public static final int LIUSHUIJIELU = 8;//流水记录
    public static final int LOOKMORE = 9;//查看更多
    public static final int MYCOMMENT = 11;//我的评论

    public static final int XIAOSHUO = 1;//只有小说
    public static final int MANHAU = 2;//只有漫画
    public static final int XIAOSHUOMAHUA = 3;//小说漫画
    public static final int MANHAUXIAOSHUO = 4;//漫画小说


    public static final int fragment_store_xiaoshuo_dp = 23;
    public static final int fragment_store_manhau_dp = 15;
    public static final int REFRESH_HEIGHT = 120;//书城 发现 向下滑动距离 改变bar 背景
    public static  int    MAXheigth;//获取手机能显示图片的最高高度
    public static  boolean BookShelfOpen = false;
    public static final int READBUTTOM_HEIGHT = 60;//yeudulibu AD

    public  static  int  getMAXheigth(){
        if(MAXheigth==0){
            MAXheigth= ImageUtil.getOpenglRenderLimitValue();
        }
        return  MAXheigth;
    }
    // 是否 启用支付 功能
    public static boolean USE_PAY = true;
    //是否使用微信 包括微信登录和微信分享
    public static boolean USE_WEIXIN = true;
    //是否使用QQ 分享
    public static boolean USE_QQ = true;
    //是否使用分享
    public static boolean USE_SHARE = true;
    //是否使用广告
    public static boolean USE_AD = true;

    public static final  boolean USE_AD_FINAL = true;

    public static final String BASE_PAY = "http://mp.dazhongmianfei-xiaoshuo.com";
    /**
     * 书城url
     */

    /**
     * 分享成功增加金币
     */
    public static final String ShareAddGold = BASE_URL + "/user/share-reward";

    public static final String APP_SHARE = BASE_URL + "/user/app-share";



    public static final String task_read = BASE_URL + "/user/task-read";


    public static final String mBookStoreUrl = BASE_URL + "/book/store";


    public static final String privacy = BASE_URL + "/site/privacy";
    /**
     * 作品详情url
     */
    public static final String mBookInfoUrl = BASE_URL + "/book/info";


    /**
     * 关于我们
     */
    public static final String aBoutUs = BASE_URL + "/service/about";

    /**
     * 任务中心
     */
    public static final String taskcenter = BASE_URL + "/task/center";

    /**
     * 签到
     */
    public static final String sIgnin = BASE_URL + "/user/sign-center";
    /**
     * 签到
     */
    public static final String sIgninhttp = BASE_URL + "/user/sign";


    /**
     * 完本作品列表
     */
    public static final String mFinishUrl = BASE_URL + "/book/finish";


    public static final String check_switch = BASE_URL + "/service/check-data";

    /**
     * 作品章节目录
     */
    public static final String mChapterCatalogUrl = BASE_URL + "/chapter/catalog";

    /**
     * 作品评论列表
     */
    public static final String mCommentListUrl = BASE_URL + "/comment/list";

    /**
     * 发布作品评论
     */
    public static final String mCommentPostUrl = BASE_URL + "/comment/post";

    /**
     * 发现页接口
     */
    // public static final String mDiscoveryUrl = BASE_URL + "/book/featured";
    public static final String mDiscoveryUrl = BASE_URL + "/book/new-featured";

    /**
     * 个人中心首页接口
     */
    public static final String mUserCenterUrl = BASE_URL + "/user/center";


    /**
     * 提交意见反馈
     */
    public static final String mFeedbackUrl = BASE_URL + "/user/post-feedback";

    /**
     * 发送短信获取验证码
     */
    public static final String mMessageUrl = BASE_URL + "/message/send";

    /**
     * 手机号登录
     */
    public static final String mMobileLoginUrl = BASE_URL + "/user/mobile-login";

    /**
     * 用户名登录
     */
    public static final String mUsernameLoginUrl = BASE_URL + "/user/account-login";


    /**
     * 添加书架作品
     */
    public static final String mBookAddCollectUrl = BASE_URL + "/user/collect-add";

    /**
     * 添加书架作品
     */
    public static final String mBookDelCollectUrl = BASE_URL + "/user/collect-del";

    /**
     * 章节下载
     */
    public static final String mChapterDownUrl = BASE_URL + "/chapter/down";

    public static final String auto_sub = BASE_URL + "/user/auto-sub";


    public static final String chapter_text = BASE_URL + "/chapter/text";

    /**
     * 消费充值记录
     */
    public static final String mPayGoldDetailUrl = BASE_URL + "/pay/gold-detail";

    /**
     * 查询用户个人资料
     */
    public static final String mUserInfoUrl = BASE_URL + "/user/info";

    /**
     * 用户设置头像
     */
    public static final String mUserSetAvatarUrl = BASE_URL + "/user/set-avatar";

    /**
     * 修改昵称
     */
    public static final String mUserSetNicknameUrl = BASE_URL + "/user/set-nickname";

    /**
     * 修改性别
     */
    public static final String mUserSetGender = BASE_URL + "/user/set-gender";
    /**
     * 绑定手机号
     */
    public static final String mUserBindPhoneUrl = BASE_URL + "/user/bind-mobile";


    /**
     * 充值页面
     */
    public static final String mPayRechargeCenterUrl = BASE_URL + "/pay/center";

    /**
     * 微信下单接口
     */
    public static final String mWXPayUrl = BASE_URL + "/pay/wxpay";

    /**
     * 支付宝下单接口
     */
    public static final String mAlipayUrl = BASE_URL + "/pay/alipay";

    /**
     * 更新deviceId接口
     */
    public static final String mSyncDeviceIdUrl = BASE_URL + "/user/sync-device";

    /**
     * 章节购买
     */
    public static final String mChapterBuy = BASE_URL + "/chapter/buy";

    /**
     * 章节购买预览
     */
    public static final String mChapterBuyIndex = BASE_URL + "/chapter/buy-index";

    /**
     * 检测更新
     */
    public static final String mAppUpdateUrl = BASE_URL + "/service/checkver";
    /**
     * 上传 性别和 分类
     */
    public static final String save_recommend = BASE_URL + "/user/save-recommend";
    /**
     * 绑定微信
     */

    public static final String bind_wechat = BASE_URL + "/user/bind-wechat";

    public static final String start_recommend = BASE_URL + "/user/start-recommend";

    public static final String mWebviewCacheDir = "webviewCache";

    /**
     * webview settings.setAppCachePath(appCacheDir)的路径
     */
    public static final String mAppCacheDir = "appCache";

    /**
     * 用户登录token
     */
    public static final String TOKEN = "token";

    /**
     * 用户唯一标识
     */
    public static final String UID = "uid";

    /**
     * 3G4G
     */
    public static final String IS3G4G = "is3G4G";

    /**
     * wifi download
     */
    public static final String WIFIDOWNLOAD = "wifidownload";

    /**
     * auto buy
     */
    public static final String AUTOBUY = "autobuy";



    public static String PUSH_TOKEN = "";


    private static ReaderConfig mReaderConfig = null;


    public static List<Integer> integerList = new ArrayList<>();

    /**
     * 单例模式：获得WuyeConfig对象
     *
     * @return
     */
    public static ReaderConfig newInstance() {
        if (mReaderConfig == null) {
            mReaderConfig = new ReaderConfig();
        }
        return mReaderConfig;
    }

    /**
     * 本地log日志文件存储路径
     */
    private String mLocalLogDir = "ReaderAppLog/";

    /**
     * 获取本地日志文件存储路径
     *
     * @return
     */
    public String getLocalLogDir() {
        return mLocalLogDir;
    }

    /**
     * 使用3G/4G同步书架
     */
    private boolean is3G4G = true;

    public boolean is3G4G() {
        return is3G4G;
    }

    public void setIs3G4G(boolean is3G4G) {
        this.is3G4G = is3G4G;
    }

    /**
     * WiFi下自动下载已购章节，由于和预下载上／下章有冲突，暂不处理
     */
    private boolean isWiFiDownload = true;

    public boolean isWiFiDownload() {
        return isWiFiDownload;
    }

    public void setWiFiDownload(boolean wiFiDownload) {
        isWiFiDownload = wiFiDownload;
    }

    /**
     * 自动购买下一章
     */
    private boolean autoBuy = true;

    public boolean isAutoBuy() {
        return autoBuy;
    }

    public void setAutoBuy(boolean autoBuy) {
        this.autoBuy = autoBuy;
    }

    //从阅读页直接进入目录页再打开阅读 不改变 广告是否显示 的布尔值
    public static boolean CatalogInnerActivityOpen;
    //是是否显示广告的 接口控制开光
    public static int ad_switch;
    //个人中心界面是否需要刷新 （章节阅读发生过购买充值 导致个人账户 书币变化 需要刷新个人中心界面）
    public static boolean REFREASH_USERCENTER = false;


    //版本控制接口
    //一级单位
    public static String currencyUnit;
    //二级单位
    public static String subUnit;


    public  static String getCurrencyUnit(Activity activity){
        if(currencyUnit!=null){
            return  currencyUnit;
        }else {
            String cu= ShareUitls.getString(activity,"currencyUnit","书币");
            return  cu;
        }
    }

    public  static String getSubUnit(Activity activity){
        if(subUnit!=null){
            return  subUnit;
        }else {
            String cu= ShareUitls.getString(activity,"subUnit","书券");
            return  cu;
        }
    }
    public static void syncDevice(Activity activity) {
        String device_id = ShareUitls.getString(activity, "PUSH_TOKEN", ReaderConfig.PUSH_TOKEN);
        if (device_id.length() == 0) {
            return;
        }
        final ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("device_id",device_id );
        String json = params.generateParamsJson();
        OkHttp3Engine.getInstance(activity).postAsyncHttp(ReaderConfig.mSyncDeviceIdUrl, json, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(final String result) {

            }
        });
    }


}
