package com.dazhongmianfei.dzmfreader.comic.config;

import android.app.Activity;
import android.graphics.Color;

import com.dazhongmianfei.dzmfreader.BuildConfig;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;

import java.util.ArrayList;
import java.util.List;

import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.BASE_URL;

/**
 * Created by scb on 2018/5/27.
 */
public class ComicConfig {


    /**
     * 漫画书架
     */
    public static final String COMIC_SHELF = BASE_URL + "/fav/index";

    /**
     * 漫画新增书架
     */
    public static final String COMIC_SHELF_ADD = BASE_URL + "/fav/add";
    /**
     * 漫画删除书架
     */
    public static final String COMIC_SHELF_DEL = BASE_URL + "/fav/del";


    /**
     * 漫画书城
     */
    public static final String COMIC_home_stock = BASE_URL + "/comic/home-stock";
    /**
     * 漫画换一换
     */
    public static final String COMIC_home_refresh = BASE_URL + "/comic/refresh";




    /**
     * 漫画发现
     */
    public static final String COMIC_featured = BASE_URL + "/comic/featured";
    /**
     * 漫画详情
     */
    public static final String COMIC_info = BASE_URL + "/comic/info";

    /**
     * 漫画目录
     */
    public static final String COMIC_catalog = BASE_URL + "/comic/catalog";
    /**
     * 漫画章节
     */
    public static final String COMIC_chapter = BASE_URL + "/comic/chapter";
    /**
     * 漫画吐槽
     */
    public static final String COMIC_tucao = BASE_URL + "/comic/tucao";

    /**
     * 漫画分类
     */
    public static final String COMIC_list = BASE_URL + "/comic/list";

    /**
     * 漫画 排行首页
     *
     */
    public static final String COMIC_rank_index = BASE_URL +   "/rank/comic-index";

    /**
     * 漫画 排行 列表
     */
    public static final String COMIC_rank_list = BASE_URL + "/rank/comic-list";
    /**
     * 漫画搜索列表
     */
    public static final String COMIC_search = BASE_URL + "/comic/search";

    /**
     * 漫画搜索首页
     */
    public static final String COMIC_search_index = BASE_URL + "/comic/search-index";

    /**
     * 漫画评论类表
     */
    public static final String COMIC_comment_list = BASE_URL + "/comic/comment-list";
    /**
     * WODE漫画评论类表
     */
    public static final String COMIC_comment_list_MY = BASE_URL + "/user/comic-comments";



    /**
     * 漫画发评论
     */
    public static final String COMIC_sendcomment = BASE_URL + "/comic/post";
    /**
     * 漫画下载选项
     */
    public static final String COMIC_down_option = BASE_URL +   "/comic/down-option";

    /**
     * 漫画下载
     */
    public static final String COMIC_down = BASE_URL +   "/comic/down";

    /**
     * 漫画购买预览
     */
    public static final String COMIC_buy_index = BASE_URL +   "/comic-chapter/buy-index";


    /**
     * 漫画购买
     */
    public static final String COMIC_buy_buy = BASE_URL +   "/comic-chapter/buy";

    /**
     * 漫画 获取阅读历史
     */
    public static final String COMIC_read_log = BASE_URL +   "/user/comic-read-log";
    /**
     * 漫画删除阅读历史
     */
    public static final String COMIC_read_log_del = BASE_URL +   "/user/del-comic-read-log";
    /**
     * 漫画 新增阅读历史
     */
    public static final String COMIC_read_log_add = BASE_URL +   "/user/add-comic-read-log";
    /**
     * 漫画限免
     */
    public static final String COMIC_free_time = BASE_URL +   "/comic/free";

    /**
     * 漫画 完本
     */
    public static final String COMIC_finish = BASE_URL +   "/comic/finish";

    /**
     * 漫画会员首页
     */
    public static final String COMIC_baoyue = BASE_URL +   "/comic/baoyue";
    /**
     * 漫画会员分类
     */
    public static final String COMIC_baoyue_index = BASE_URL +   "/comic/baoyue-index";
    /**
     * 漫画 会员列表
     */
   public static final String COMIC_baoyue_list = BASE_URL +   "/comic/baoyue-list";


    /**
     * 漫画 查看更多
     *
     */
    public static final String COMIC_recommend = BASE_URL +   "/comic/recommend";








    private static boolean IS_OPEN_DANMU;

    public static boolean IS_OPEN_DANMU(Activity activity) {
        if (!IS_OPEN_DANMU) {
            IS_OPEN_DANMU = ShareUitls.getBoolean(activity, "IS_OPEN_DANMU", true);
        }
        return IS_OPEN_DANMU;
    }

    public static void SET_OPEN_DANMU(Activity activity, boolean flag) {
        IS_OPEN_DANMU=flag;
        ShareUitls.putBoolean(activity, "IS_OPEN_DANMU", flag);
    }




}
