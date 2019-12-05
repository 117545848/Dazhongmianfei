package com.dazhongmianfei.dzmfreader.config;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Path;
import android.widget.Switch;

import com.google.gson.Gson;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.Task.TaskManager;
import com.dazhongmianfei.dzmfreader.activity.LoginActivity;
import com.dazhongmianfei.dzmfreader.bean.UserInfoItem;
import com.dazhongmianfei.dzmfreader.book.config.BookConfig;
;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;
import com.dazhongmianfei.dzmfreader.utils.Utils;

import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.GETPRODUCT_TYPE;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.MANHAU;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.USE_AD_FINAL;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.XIAOSHUO;

//主界面接口数据缓存
public class MainHttpTask {

    //   private static TaskManager taskManager;

    private MainHttpTask() {
    }

    ;
    private static MainHttpTask mainHttpTask;


    public static MainHttpTask getInstance() {
        if (mainHttpTask == null) {
            mainHttpTask = new MainHttpTask();
        }
        return mainHttpTask;
    }

    private String ShelfBook;
    private String ShelfComic;

    private String StoreBookMan;
    private String StoreBookWoMan;

    private String StoreComicMan;
    private String StoreComicWoMan;

    private String DiscoverBook;
    private String DiscoverComic;

    private String Mine;


    public void InitHttpData(Activity activity) {
        if (GETPRODUCT_TYPE(activity) != MANHAU) {
            httpSend(activity, BookConfig.mBookStoreUrl, "StoreBookMan", null);
            httpSend(activity, BookConfig.mBookStoreUrl, "StoreBookWoMan", null);
            httpSend(activity, BookConfig.mDiscoveryUrl, "DiscoverBook", null);
            httpSend(activity, BookConfig.mBookCollectUrl, "ShelfBook", null);
        }

        if (Utils.isLogin(activity)) {
            httpSend(activity, ReaderConfig.mUserCenterUrl, "Mine", null);
        }
    }

    public interface GetHttpData {
        void getHttpData(String result);
    }

    public void httpSend(Activity activity, String url, String Option, GetHttpData getHttpData) {
        ReaderParams params = new ReaderParams(activity);//StoreBookWoMan
        if (Option.equals("StoreBookMan") || Option.equals("StoreComicMan")) {
            params.putExtraParams("channel_id", "1");
        } else if (Option.equals("StoreBookWoMan") || Option.equals("StoreComicWoMan")) {
            params.putExtraParams("channel_id", "2");
        }
        String json = params.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParams3(url, json, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {

                        switch (Option) {
                            case "ShelfBook":
                                ShelfBook = result;
                                break;
                            case "ShelfComic":
                                ShelfComic = result;
                                break;
                            case "StoreBookMan":
                                StoreBookMan = result;
                                break;
                            case "StoreBookWoMan":
                                StoreBookWoMan = result;
                                break;
                            case "StoreComicMan":
                                StoreComicMan = result;
                                break;
                            case "StoreComicWoMan":
                                StoreComicWoMan = result;
                                break;
                            case "DiscoverBook":
                                DiscoverBook = result;
                                break;
                            case "DiscoverComic":
                                DiscoverComic = result;
                                break;
                            case "Mine":
                                Mine = result;
                                if (USE_AD_FINAL) {
                                    ReaderConfig.REFREASH_USERCENTER = false;
                                    UserInfoItem mUserInfo = new Gson().fromJson(result, UserInfoItem.class);
                                    if (mUserInfo.getIs_vip() == 1) {
                                        ReaderConfig.USE_AD = false;
                                    } else {
                                        ReaderConfig.USE_AD = ReaderConfig.ad_switch == 1;
                                    }
                                }
                                break;
                        }
                        ShareUitls.putMainHttpTaskString(activity, Option, result);
                        try {
                            if (getHttpData != null) {
                                getHttpData.getHttpData(result);
                            }
                        } catch (Exception e) {
                        }

                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        try {
                            if (getHttpData != null) {
                                getHttpData.getHttpData(null);
                            }
                        } catch (Exception e) {
                        }
                    }
                }
        );
    }

    public void getResultString(Activity activity, String Option, GetHttpData getHttpData) {
        try {
            switch (Option) {
                case "ShelfBook":
                    if (ShelfBook == null) {
                        ShelfBook = ShareUitls.getMainHttpTaskString(activity, Option, null);
                    }
                    if (ShelfBook != null) {
                        getHttpData.getHttpData(ShelfBook);
                    } else {
                        httpSend(activity, BookConfig.mBookCollectUrl, Option, getHttpData);
                    }
                    break;

                case "StoreBookMan":
                    if (StoreBookMan == null) {
                        StoreBookMan = ShareUitls.getMainHttpTaskString(activity, Option, null);
                    }
                    if (StoreBookMan != null) {
                        getHttpData.getHttpData(StoreBookMan);
                    } else {
                        httpSend(activity, BookConfig.mBookStoreUrl, Option, getHttpData);
                    }
                    break;
                case "StoreBookWoMan":
                    if (StoreBookWoMan == null) {
                        StoreBookWoMan = ShareUitls.getMainHttpTaskString(activity, Option, null);
                    }
                    if (StoreBookWoMan != null) {
                        getHttpData.getHttpData(StoreBookWoMan);
                    } else {
                        httpSend(activity, BookConfig.mBookStoreUrl, Option, getHttpData);
                    }
                    break;

                case "DiscoverBook":
                    if (DiscoverBook == null) {
                        DiscoverBook = ShareUitls.getMainHttpTaskString(activity, Option, null);
                    }
                    if (DiscoverBook != null) {
                        getHttpData.getHttpData(DiscoverBook);
                    } else {
                        httpSend(activity, BookConfig.mDiscoveryUrl, Option, getHttpData);
                    }
                    break;

                case "Mine":
                    if (Mine == null) {
                        Mine = ShareUitls.getMainHttpTaskString(activity, Option, null);
                    }
                    if (Mine != null) {
                        getHttpData.getHttpData(Mine);
                    } else {
                        if (Utils.isLogin(activity)) {
                            httpSend(activity, ReaderConfig.mUserCenterUrl, Option, getHttpData);
                        }
                    }
                    break;
            }
        } catch (Exception e) {
        }


    }



    public  boolean  Gotologin(Activity activity){
        if (!Utils.isLogin(activity)) {
            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.MineNewFragment_nologin));
            Intent intent = new Intent();
            intent.setClass(activity, LoginActivity.class);
            activity.startActivity(intent);
            return false;
        }
        return  true;
    }
}
