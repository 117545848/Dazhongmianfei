package com.dazhongmianfei.dzmfreader.book.config;

import android.graphics.Color;

import com.dazhongmianfei.dzmfreader.BuildConfig;

import java.util.ArrayList;
import java.util.List;

import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.BASE_URL;

/**
 * Created by scb on 2018/5/27.
 */
public class BookConfig {

    /**
     * 书城url
     */


    public static final String mBookStoreUrl = BASE_URL + "/book/store";

    /**
     * 作品详情url
     */
    public static final String mBookInfoUrl = BASE_URL + "/book/info";

    /**
     * 分类列表接口
     */
    public static final String mCategoryUrl = BASE_URL + "/book/category";

    /**
     * 分类详情首页
     */
    public static final String mCategoryIndexUrl = BASE_URL + "/book/category-index";

    /**
     * 分类作品列表
     */
    public static final String mCategoryListUrl = BASE_URL + "/book/category-list";

    /**
     * 包月接口
     */
    public static final String mBaoyueUrl = BASE_URL + "/book/baoyue";
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
     * 包月库作品库首页
     */
    public static final String mBaoyueIndexUrl = BASE_URL + "/book/baoyue-index";

    /**
     * 包月作品列表
     */
    public static final String mBaoyueListUrl = BASE_URL + "/book/baoyue-list";

    /**
     * 完本作品列表
     */
    public static final String mFinishUrl = BASE_URL + "/book/finish";

    /**
     * 限免作品列表  book/free
     */
    public static final String mFreeTimeUrl = BASE_URL + "/book/free";

    public static final String free_time = BASE_URL + "/book/free-time";

    public static final String check_switch = BASE_URL + "/service/check-data";
    /**
     * 推荐更多
     */
    public static final String mRecommendUrl = BASE_URL + "/book/recommend";

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
     * 排行榜首页
     */
    public static final String mRankUrl = BASE_URL + "/rank/index";

    /**
     * 排行榜作品列表
     */
    public static final String mRankListUrl = BASE_URL + "/rank/book-list";

    /**
     * 搜索首页
     */
    public static final String mSearchIndexUrl = BASE_URL + "/book/search-index";

    /**
     * 搜索页接口
     */
    public static final String mSearchUrl = BASE_URL + "/book/search";

    /**
     * 发现页接口
     */
   // public static final String mDiscoveryUrl = BASE_URL + "/book/featured";
    public static final String mDiscoveryUrl = BASE_URL + "/book/new-featured";
    /**
     * 微信首次登录
     *
     *
     */
    public static final String first_mWeiXinLoginUrl = BASE_URL + "/user/login-wechat";
    /**
     * 微信登录
     */
    public static final String nofirst_mWeiXinLoginUrl = BASE_URL + "/user/openid-login";

    /**
     * 个人中心首页接口
     */
    public static final String mUserCenterUrl = BASE_URL + "/user/center";

    /**
     * 我的评论
     */
    public static final String mUserCommentsUrl = BASE_URL + "/user/comments";

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
     * 用户书架
     */
    public static final String mBookCollectUrl = BASE_URL + "/user/book-collect";




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




    public static final String chapter_text= BASE_URL + "/chapter/text";

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
     * 微信登录
     */
    public static final String mWeiXinLoginUrl = BASE_URL + "/user/login-wechat";

    /**
     * 包月购买页
     */
    public static final String mPayBaoyueCenterUrl = BASE_URL + "/pay/baoyue-center";

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
    /**
     * 阅读历史
     */

    public static final String read_log = BASE_URL + "/user/read-log";
    /**
     * 删除阅读历史
     */
    public static final String del_read_log = BASE_URL + "/user/del-read-log";
    /**
     * 新增阅读历史
     */
    public static final String add_read_log = BASE_URL + "/user/add-read-log";
    /**
     * 阅读页关闭广告
     */
    public static final String del_ad = BASE_URL + "/user/del-ad";



    public static final String start_recommend = BASE_URL + "/user/start-recommend";
    /**
     * 换一换
     */
    public static final String book_refresh = BASE_URL + "/book/refresh";


/*
    public static String Book_collect;

    //漫画书架接口
    public static String Comic_collect;
    //阅读历史接口
    public static String Read_log;
    //书城小说男女频道接口
    public static String Book_store1, Book_store2;
    public static String Comic_store1,  Comic_store2;
    //发现页接口
    public static String New_featured;*/

}
