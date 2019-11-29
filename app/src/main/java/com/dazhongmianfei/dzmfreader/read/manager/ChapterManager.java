package com.dazhongmianfei.dzmfreader.read.manager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.Task.InstructTask;
import com.dazhongmianfei.dzmfreader.Task.TaskManager;
import com.dazhongmianfei.dzmfreader.book.been.BaseBook;
import com.dazhongmianfei.dzmfreader.bean.ChapterContent;
import com.dazhongmianfei.dzmfreader.bean.ChapterItem;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.read.ReadActivity;
import com.dazhongmianfei.dzmfreader.utils.FileManager;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.InternetUtils;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
//.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 书籍阅读管理类
 * Created by scb on 2018/7/25.
 */
public class ChapterManager {
    public static final String TAG = ChapterManager.class.getSimpleName();
    Gson gson = new Gson();
    public static Activity mContext;
    //章节下载任务管理器
    private TaskManager mTaskManager;
    /**
     * 书籍id
     */
    public String mBookId;
    /**
     * 章节列表
     */
    public List<ChapterItem> mChapterList;
    List<ChapterItem> LitePalchapterList;
    /**
     * 循环检测章节列表是否初始化完毕
     */
    private Handler mHandler;

    //  private boolean isLoadFinish = false;
    public BaseBook baseBook;
    /**
     * 当前章节
     */
    public ChapterItem mCurrentChapter;

    public ChapterManager(Context context) {
//        mContext = (Activity) context;
        mChapterList = new ArrayList<>();
        mHandler = new Handler(Looper.getMainLooper());
        //初始化任务环境
        mTaskManager = new TaskManager();
    }

    public ChapterManager() {
        mTaskManager = new TaskManager();
    }

    public static ChapterManager mReadingManager;

    public static ChapterManager getInstance(Activity activity) {
        mContext = activity;
        if (mReadingManager == null) {
            mReadingManager = new ChapterManager(activity);
        }
        return mReadingManager;
    }

    public void openBook(BaseBook baseBook, String book_id) {
        Utils.showLoadingDialog(mContext);
        mBookId = book_id;
        CatalogActivity = null;
        this.baseBook = baseBook;
        loadChapterList(null);
    }

    public void openBook(BaseBook baseBook, String book_id, String chapter_id) {
        Log.i("RequestParams11", "    dakai");

        MyToash.Log("openBook", book_id + "    " + chapter_id);
        Utils.showLoadingDialog(mContext);
        mBookId = book_id;
        CatalogActivity = null;
        this.baseBook = baseBook;
        loadChapterList(chapter_id);

    }

    String CatalogActivity;

    public void openBook(BaseBook baseBook, String book_id, String chapter_id, String CatalogActivity) {
        MyToash.Log("openBook", book_id + "    " + chapter_id);
        Utils.showLoadingDialog(mContext);
        mBookId = book_id;
        this.baseBook = baseBook;
        this.CatalogActivity = CatalogActivity;
        loadChapterList(chapter_id);

    }
    public void openBook(BaseBook baseBook, String book_id, String chapter_id, List<ChapterItem> updateList) {
        mBookId = book_id;
        this.baseBook = baseBook;
        mChapterList.clear();
        mChapterList.addAll(updateList);
        openCurrentChapter(chapter_id);
    }

    public void openCurrentChapter(BaseBook baseBook, final ChapterItem chapterItem) {
        this.baseBook = baseBook;
        final String chapter_id = chapterItem.getChapter_id();
        if (chapter_id.contains("/")) {//SD卡自带的本地图书
            mCurrentChapter = mChapterList.get(0);
            ReadActivity.openBook(baseBook, mCurrentChapter, mContext);
        } else {
            getChapter(chapterItem, chapter_id, new QuerychapterItemInterface() {
                @Override
                public void success(ChapterItem querychapterItem) {
                    mCurrentChapter = querychapterItem;
                    if (mCurrentChapter == null) return;
                    if (TextUtils.isEmpty(mCurrentChapter.getChapter_path()) || !FileManager.isExist(mCurrentChapter.getChapter_path())) {
                        addDownloadTaskWithoutAutoBuy(mCurrentChapter, new ChapterDownload() {
                            @Override
                            public void finish() {

                                OpenBookAndDown(false);
                            }
                        });
                    } else {
                        OpenBookAndDown(true);
                    }


                }

                @Override
                public void fail() {

                    MyToash.ToashError(mContext, LanguageUtil.getString(mContext, R.string.ReadActivity_chapterfail));
                    ReadActivity.handleAnimation();
                }
            });
        }


    }

    public void openCurrentChapter(final String chapter_id) {
        if (chapter_id.contains("/")) {
            mCurrentChapter = mChapterList.get(0);
            ReadActivity.openBook(baseBook, mCurrentChapter, mContext);
        } else {
            getChapter(chapter_id, new QuerychapterItemInterface() {
                @Override
                public void success(ChapterItem querychapterItem) {
                    mCurrentChapter = querychapterItem;
                    if (mCurrentChapter == null) return;
                    if (TextUtils.isEmpty(mCurrentChapter.getChapter_path()) || !FileManager.isExist(mCurrentChapter.getChapter_path())) {
                        addDownloadTaskWithoutAutoBuy(mCurrentChapter, new ChapterDownload() {
                            @Override
                            public void finish() {

                                OpenBookAndDown(false);
                            }
                        });
                    } else {
                        OpenBookAndDown(true);
                    }


                }

                @Override
                public void fail() {

                    MyToash.ToashError(mContext, LanguageUtil.getString(mContext, R.string.ReadActivity_chapterfail));
                    ReadActivity.handleAnimation();
                }
            });
        }


    }

    /**
     * 打开当前章节
     */
    public void openCurrentChapter() {
        String currentChapterId = null;
        currentChapterId = baseBook.getCurrent_chapter_id();
        if (currentChapterId == null || currentChapterId.equals("0")) {
            mCurrentChapter = mChapterList.get(0);
            Log.i("mChapterCatalogUr5----", mCurrentChapter.getChapter_id() + "  " + mCurrentChapter.getChapter_path() + "  " + mCurrentChapter.getBook_name());
            if (TextUtils.isEmpty(mCurrentChapter.getChapter_path()) || !FileManager.isExist(mCurrentChapter.getChapter_path())) {
                addDownloadTaskWithoutAutoBuy(mCurrentChapter, new ChapterDownload() {
                    @Override
                    public void finish() {
                        OpenBookAndDown(false);


                    }
                });
            } else {
                OpenBookAndDown(true);


            }


        } else {
            //  Log.i("mChapterCatalogUr5", currentChapterId + "");
            openCurrentChapter(currentChapterId);
        }

    }

    private void OpenBookAndDown(boolean flag) {
        ReadActivity.openBook(baseBook, mCurrentChapter, mContext);
        if (flag) {
            addDownloadTask2(false, mCurrentChapter.getNext_chapter_id(), null);
            addDownloadTask2(true, mCurrentChapter.getPre_chapter_id(), null);
        }
    }


    /**
     * 获取当前章节
     *
     * @return
     */
    public ChapterItem getCurrentChapter() {
        return mCurrentChapter;
    }

    public void setCurrentChapter(ChapterItem chapter) {
        mCurrentChapter = chapter;
    }

    /**
     * 判断是否有下一章节
     *
     * @return
     */


    public boolean hasNextChapter() {
        //  Log.i("hasNextChapter", (mCurrentChapter == null) + "'  " + mCurrentChapter.getNext_chapter_id());
        if (mCurrentChapter == null || mCurrentChapter.getNext_chapter_id() == null) {
            return false;
        }
        if (mCurrentChapter.getNext_chapter_id() == null) {
            mCurrentChapter.setNext_chapter_id((Integer.parseInt(mCurrentChapter.getChapter_id()) + 1) + "");
        }

        return !mCurrentChapter.getNext_chapter_id().equals("-2");

    }

    /**
     * 判断是否有上一章节
     *
     * @return
     */
    public boolean hasPreChapter() {

        if (mCurrentChapter == null || mCurrentChapter.getPre_chapter_id() == null) {
            return false;
        }
        return !mCurrentChapter.getPre_chapter_id().equals("-1");


    }

    /**
     * 添加一个下载任务，里面包含自动购买章节
     *
     * @param chapter_id
     * @param download
     */
    public void addDownloadTask(final boolean flag, final String chapter_id, final ChapterDownload download) {
        if (chapter_id == null || chapter_id.equals("-1") || chapter_id.equals("-2"))
            return;

        mTaskManager.addQueueTask(new InstructTask<String, String>(null) {

            @Override
            public String doRun(String s) {
                if (flag) {
                    Next_chapter = 0;
                    Last_chapter = 0;
                    downloadWithoutAutoBuy(flag, mBookId, chapter_id, download);
                }

                return null;
            }
        });
    }

    public void addDownloadTask2(final boolean flag, final String chapter_id, final ChapterDownload download) {
        if (chapter_id == null || chapter_id.equals("-1") || chapter_id.equals("-2"))
            return;

        mTaskManager.addQueueTask(new InstructTask<String, String>(null) {

            @Override
            public String doRun(String s) {
                downloadWithoutAutoBuy2(flag, mBookId, chapter_id);
                return null;
            }
        });
    }

    public void addDownloadTaskWithoutAutoBuy(final ChapterItem chapterItem, final ChapterDownload download) {
        //  Log.i("mChapterCatalogUr5----", "1");
        if (chapterItem == null)
            return;

        mTaskManager.addQueueTask(new InstructTask<String, String>(null) {

            @Override
            public String doRun(String s) {
                //  Log.i("mChapterCatalogUr5----", "2");
                downloadWithoutAutoBuy(mBookId, chapterItem, download);
                return null;
            }
        });
    }

    public void downloadWithoutAutoBuy(final boolean flag, final String book_id, final String chapter_id, final ChapterDownload download) {

        getChapter(chapter_id, new QuerychapterItemInterface() {
            @Override
            public void success(final ChapterItem querychapterItem) {
                String path = FileManager.getSDCardRoot().concat("Reader/book/").concat(querychapterItem.getBook_id() + "/").concat(querychapterItem.getChapter_id() + "/").concat(querychapterItem.getIs_preview() + "/").concat(querychapterItem.getUpdate_time()).concat(".txt");
                if (FileManager.isExist(path)) {
                    querychapterItem.setChapter_path(path);
                    ContentValues values = new ContentValues();
                    values.put("chapter_path", path);
                    LitePal.updateAll(ChapterItem.class, values, "chapter_id = ?", chapter_id);
                    return;
                }
                //  Log.i("mChapterCatalogUr5----", book_id + "    4   " + chapter_id);

                ReaderParams params = new ReaderParams(mContext);
                params.putExtraParams("book_id", book_id);
                params.putExtraParams("chapter_id", chapter_id);
                // params.putExtraParams("num", "1");
                String json = params.generateParamsJson();
                HttpUtils.getInstance(mContext).sendRequestRequestParams3(ReaderConfig.chapter_text, json, false, new HttpUtils.ResponseListener() {
                            @Override
                            public void onResponse(final String result) {
                                //  Log.i("mChapterCatalogUr5----", book_id + "   5   " + chapter_id);
                                //  Log.i("mChapterBuy-2", result);


                                //  JSONObject jsonObj = new JSONObject(result);
                                // JSONArray chapterContentArr = jsonObj.getJSONArray("data");
                                ChapterContent chapterContent = gson.fromJson(result, ChapterContent.class);
                                querychapterItem.setUpdate_time(chapterContent.getUpdate_time());
                                querychapterItem.setIs_preview(chapterContent.getIs_preview());

                                String filepath = FileManager.getSDCardRoot().concat("Reader/book/").concat(book_id + "/").concat(chapter_id + "/").concat(querychapterItem.getIs_preview() + "/").concat(querychapterItem.getUpdate_time()).concat(".txt");


                                FileManager.createFile(filepath, chapterContent.getContent().getBytes());

                                querychapterItem.setChapteritem_begin(0);
                                querychapterItem.setChapter_path(filepath);
                                querychapterItem.setIs_preview(chapterContent.getIs_preview());
                                ContentValues values = new ContentValues();
                                values.put("chapteritem_begin", 0);
                                values.put("chapter_path", filepath);
                                values.put("update_time", chapterContent.getUpdate_time());
                                values.put("is_preview", chapterContent.getIs_preview());
                                LitePal.updateAll(ChapterItem.class, values, "chapter_id = ?", chapter_id);
                                if (download != null)
                                    download.finish();
                                addDownloadTask2(false, chapterContent.getNext_chapter(), null);
                                addDownloadTask2(true, chapterContent.getLast_chapter(), null);
                                ReaderConfig.REFREASH_USERCENTER = true;

                            }

                            @Override
                            public void onErrorResponse(String ex) {

                            }
                        }

                );


            }

            @Override
            public void fail() {

            }
        });


    }

    int Next_chapter = 0, Last_chapter = 0;

    public void downloadWithoutAutoBuy2(final boolean onretiation, final String book_id, final String chapter_id) {


        getChapter(chapter_id, new QuerychapterItemInterface() {
            @Override
            public void success(final ChapterItem querychapterItem) {
                String path = FileManager.getSDCardRoot().concat("Reader/book/").concat(querychapterItem.getBook_id() + "/").concat(querychapterItem.getChapter_id() + "/").concat(querychapterItem.getIs_preview() + "/").concat(querychapterItem.getUpdate_time()).concat(".txt");
                if (FileManager.isExist(path)) {
                    querychapterItem.setChapter_path(path);
                    ContentValues values = new ContentValues();
                    values.put("chapter_path", path);
                    LitePal.updateAll(ChapterItem.class, values, "chapter_id = ?", chapter_id);
                    return;
                }

                ReaderParams params = new ReaderParams(mContext);
                params.putExtraParams("book_id", book_id);
                params.putExtraParams("chapter_id", chapter_id);
                String json = params.generateParamsJson();
                HttpUtils.getInstance(mContext).sendRequestRequestParams3(ReaderConfig.chapter_text, json, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {

                        ChapterContent chapterContent = gson.fromJson(result, ChapterContent.class);
                        querychapterItem.setIs_preview(chapterContent.getIs_preview());
                        querychapterItem.setUpdate_time(chapterContent.getUpdate_time());
                        String filepath = FileManager.getSDCardRoot().concat("Reader/book/").concat(book_id + "/").concat(chapter_id + "/").concat(querychapterItem.getIs_preview() + "/").concat(querychapterItem.getUpdate_time()).concat(".txt");

                        FileManager.createFile(filepath, chapterContent.getContent().getBytes());
                        querychapterItem.setChapteritem_begin(0);
                        querychapterItem.setChapter_path(filepath);

                        ContentValues values = new ContentValues();
                        values.put("chapteritem_begin", 0);
                        values.put("chapter_path", filepath);
                        values.put("update_time", chapterContent.getUpdate_time());
                        values.put("is_preview", chapterContent.getIs_preview());
                        LitePal.updateAll(ChapterItem.class, values, "chapter_id = ?", chapter_id);
                        if (!onretiation) {
                            if (!chapterContent.getNext_chapter().equals("0") && Next_chapter < 3) {
                                Next_chapter++;
                                addDownloadTask2(false, chapterContent.getNext_chapter(), null);
                            }
                        } else {
                            if (!chapterContent.getLast_chapter().equals("0") && Last_chapter < 2) {
                                Last_chapter++;
                                addDownloadTask2(true, chapterContent.getLast_chapter(), null);
                            }
                        }
                        ReaderConfig.REFREASH_USERCENTER = true;
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                });


            }

            @Override
            public void fail() {

            }
        });


    }

    public void downloadWithoutAutoBuy(final String book_id, final ChapterItem querychapterItem, final ChapterDownload download) {

        final String chapter_id = querychapterItem.getChapter_id();
        String path = FileManager.getSDCardRoot().concat("Reader/book/").concat(querychapterItem.getBook_id() + "/").concat(querychapterItem.getChapter_id() + "/").concat(querychapterItem.getIs_preview() + "/").concat(querychapterItem.getUpdate_time()).concat(".txt");
        if (FileManager.isExist(path)) {
            querychapterItem.setChapter_path(path);
            ContentValues values = new ContentValues();
            values.put("chapter_path", path);
            LitePal.updateAll(ChapterItem.class, values, "chapter_id = ?", chapter_id);
            if (download != null) {
                download.finish();
            }
            addDownloadTask2(false, querychapterItem.getNext_chapter_id(), null);
            addDownloadTask2(true, querychapterItem.getPre_chapter_id(), null);
            return;

        }
        Log.i("mChapterCatalogUr5----", book_id + "    4   " + chapter_id);

        ReaderParams params = new ReaderParams(mContext);
        params.putExtraParams("book_id", book_id);
        params.putExtraParams("chapter_id", chapter_id);
        String json = params.generateParamsJson();
        HttpUtils.getInstance(mContext).sendRequestRequestParams3(ReaderConfig.chapter_text, json, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {

                        ChapterContent chapterContent = gson.fromJson(result, ChapterContent.class);
                        querychapterItem.setIs_preview(chapterContent.getIs_preview());
                        querychapterItem.setUpdate_time(chapterContent.getUpdate_time());
                        String filepath = FileManager.getSDCardRoot().concat("Reader/book/").concat(book_id + "/").concat(chapter_id + "/").concat(querychapterItem.getIs_preview() + "/").concat(querychapterItem.getUpdate_time()).concat(".txt");
                        FileManager.createFile(filepath, chapterContent.getContent().getBytes());

                        querychapterItem.setChapteritem_begin(0);
                        querychapterItem.setChapter_path(filepath);


                        ContentValues values = new ContentValues();
                        values.put("chapteritem_begin", 0);
                        values.put("chapter_path", filepath);
                        values.put("is_preview", chapterContent.getIs_preview());
                        LitePal.updateAll(ChapterItem.class, values, "chapter_id = ?", chapter_id);
                        if (download != null)
                            download.finish();
                        if (!chapterContent.getNext_chapter().equals("0")) {

                            addDownloadTask2(false, chapterContent.getNext_chapter(), null);

                        }
                        if (!chapterContent.getLast_chapter().equals("0")) {
                            addDownloadTask2(true, chapterContent.getLast_chapter(), null);
                        }
                        ReaderConfig.REFREASH_USERCENTER = true;

                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );


    }

    public interface QuerychapterItemInterface {
        void success(ChapterItem querychapterItem);

        void fail();

    }

    public void getChapter(final int chapter_possition, final String chapterId, QuerychapterItemInterface querychapterItemInterface) {

        MyToash.Log("chapter_possition", chapter_possition + "  " + chapterId);
        try {
            if (mChapterList != null) {
                if (chapter_possition < 0) {
                    querychapterItemInterface.fail();
                    return;
                }
                if (chapter_possition > mChapterList.size()) {

                    return;
                }
                ChapterItem chapterItem = mChapterList.get(chapter_possition);

                if (chapterItem.getChapter_id().equals(chapterId)) {
                    MyToash.Log("chapter_possition2", chapter_possition + "  " + chapterId);
                    querychapterItemInterface.success(chapterItem);
                    return;
                } else {
                    getChapter(chapterId, querychapterItemInterface);
                }

            }
        } catch (Exception w) {

            getChapter(chapterId, querychapterItemInterface);
        }

    }

    public void getChapter(final String chapterId, QuerychapterItemInterface querychapterItemInterface) {
        if (mChapterList != null) {
            for (ChapterItem chapterItem : mChapterList) {
                if (chapterItem.getChapter_id().equals(chapterId)) {
                    querychapterItemInterface.success(chapterItem);
                    return;
                }

            }
            querychapterItemInterface.success(mChapterList.get(0));
        }
    }

    public void getChapter(ChapterItem ChapterItem, final String chapterId, QuerychapterItemInterface querychapterItemInterface) {
        if (ChapterItem != null && ChapterItem.getChapter_id().equals(chapterId)) {
            querychapterItemInterface.success(ChapterItem);
            return;
        }
        if (mChapterList != null) {
            for (ChapterItem chapterItem : mChapterList) {
                if (chapterItem.getChapter_id().equals(chapterId)) {
                    querychapterItemInterface.success(chapterItem);
                    return;
                }

            }
            querychapterItemInterface.success(mChapterList.get(0));
        }
    }

    public void loadChapterList(final String chapter_id) {
        Next_chapter = 0;
        Last_chapter = 0;
        LitePalchapterList = LitePal.where("book_id = ?", mBookId).find(ChapterItem.class);
        if (!LitePalchapterList.isEmpty()) {
            mChapterList.clear();
            mChapterList.addAll(LitePalchapterList);
            if (mBookId.startsWith("/storage")) {//SD卡自带的本地图书
                openCurrentChapter(chapter_id);
            } else {
                if (InternetUtils.internet(mContext)) {
                    httpDdata(0, mBookId, chapter_id);
                } else {
                    if (chapter_id == null) {
                        openCurrentChapter();
                    } else {
                        openCurrentChapter(chapter_id);
                    }
                }
            }
        } else {
            if (InternetUtils.internet(mContext)) {
                httpDdata(0, mBookId, chapter_id);
            } else {
                MyToash.ToashError(mContext, LanguageUtil.getString(mContext, R.string.chapterupdateing));
                Utils.hideLoadingDialog();
                ReadActivity.handleAnimation();
            }
        }
    }


    private void httpDdata(final int update, final String mBookId, final String chapter_id) {
        if (CatalogActivity != null) {//目录带过来的数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    handleData(CatalogActivity, mBookId, update, chapter_id);
                    CatalogActivity = null;
                }
            }).start();

            return;
        }

        ReaderParams params = new ReaderParams(mContext);
        params.putExtraParams("book_id", mBookId);
        String json = params.generateParamsJson();
        HttpUtils.getInstance(mContext).sendRequestRequestParams3(ReaderConfig.mChapterCatalogUrl, json, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                handleData(result, mBookId, update, chapter_id);
                                CatalogActivity = null;
                            }
                        }).start();
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        Utils.hideLoadingDialog();
                        ReadActivity.handleAnimation();
                    }


                }

        );
    }

    private void handleData(String result, final String mBookId, int update, String chapter_id) {

        boolean isupdata = false;
        String Chapter_text_Sign = Utils.MD5(result);
        if (!LitePalchapterList.isEmpty()) {//校验章节列表是否发生更新

            if (baseBook.getChapter_text() == null) {
                BaseBook basebooks = LitePal.where("book_id = ?", mBookId).findFirst(BaseBook.class);
                if (basebooks != null) {
                    baseBook.setChapter_text(basebooks.getChapter_text());
                }
            }
            if (Chapter_text_Sign.equals(baseBook.getChapter_text())) {//无数据更新 直接阅读
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (chapter_id == null) {

                            openCurrentChapter();
                        } else {
                            openCurrentChapter(chapter_id);
                        }
                    }
                });


                return;
            } else {
                baseBook.setChapter_text(Chapter_text_Sign);
                isupdata = true;
                ContentValues values = new ContentValues();
                values.put("Chapter_text", Chapter_text_Sign);
                LitePal.updateAll(BaseBook.class, values, "book_id = ? ", mBookId);
            }
        } else {
            baseBook.setChapter_text(Chapter_text_Sign);
            ContentValues values = new ContentValues();
            values.put("Chapter_text", Chapter_text_Sign);
            LitePal.updateAll(BaseBook.class, values, "book_id = ? ", mBookId);
        }
//有数据更新或者该书第一次阅读  刷新数据
        final List<ChapterItem> updateList = new ArrayList<>();
        int size = 0;
        try {
            JSONObject jsonObj = new JSONObject(result);
            String bookName = jsonObj.getString("name");
            JSONArray chapterListArr = jsonObj.getJSONArray("chapter_list");
            size = chapterListArr.length();

            if (size == 0) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyToash.ToashError(mContext, LanguageUtil.getString(mContext, R.string.chapterupdateing));
                        Utils.hideLoadingDialog();
                        ReadActivity.handleAnimation();

                    }
                });
                return;
            }
            for (int i = 0; i < size; i++) {
                JSONObject jsonObject1 = chapterListArr.getJSONObject(i);
                String tempchapter_id = jsonObject1.getString("chapter_id");
                if (tempchapter_id != null && tempchapter_id.length() != 0) {
                    ChapterItem querychapterItem = new ChapterItem();
                    querychapterItem.setChapter_id(jsonObject1.getString("chapter_id"));
                    querychapterItem.setChapter_title(jsonObject1.getString("chapter_title"));
                    querychapterItem.setIs_preview(jsonObject1.getString("is_preview"));
                    querychapterItem.setUpdate_time(jsonObject1.getString("update_time"));
                    querychapterItem.setDisplay_order(jsonObject1.getInt("display_order"));
                    querychapterItem.setChapteritem_begin(0);
                    querychapterItem.setBook_id(mBookId);
                    querychapterItem.setBook_name(bookName);
                    querychapterItem.setCharset("utf-8");

                    if (i + 1 == size) {
                        querychapterItem.setNext_chapter_id("-2");
                    } else {
                        querychapterItem.setNext_chapter_id(chapterListArr.getJSONObject(i + 1).getString("chapter_id"));
                    }
                    if (i == 0) {
                        querychapterItem.setPre_chapter_id("-1");
                    } else {
                        querychapterItem.setPre_chapter_id(updateList.get(i - 1).getChapter_id());
                    }
                    String filepath = FileManager.getSDCardRoot().concat("Reader/book/").concat(querychapterItem.getBook_id() + "/").concat(querychapterItem.getChapter_id() + "/").concat(querychapterItem.getIs_preview() + "/").concat(querychapterItem.getUpdate_time()).concat(".txt");
                    querychapterItem.setChapter_path(filepath);
                    updateList.add(querychapterItem);

                    //   querychapterItem.save();
                }
            }
            //  Log.i("mChapterCatalogUrl2", mChapterList.size() + "");

        } catch (JSONException e) {
        }

        if (!updateList.isEmpty()) {
            if (!isupdata) {//首次阅读
                mChapterList.clear();
                mChapterList.addAll(updateList);
                LitePal.saveAll(mChapterList);
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (chapter_id == null) {

                            openCurrentChapter();
                        } else {
                            openCurrentChapter(chapter_id);
                        }
                    }
                });
            } else {
                for (ChapterItem chapterItem : updateList) {
                    flag:
                    for (ChapterItem chapterItem1 : LitePalchapterList) {
                        if (chapterItem.getChapter_id().equals(chapterItem1.getChapter_id())) {//只保留本地数据的 章节本地路径 和章节阅读进度数据 其他数据已服务端为准
                            chapterItem.setChapteritem_begin(chapterItem1.getChapteritem_begin());
                            break flag;//跳出内循环
                        }
                    }

                }
                LitePal.deleteAll(ChapterItem.class, "book_id = ?", mBookId);
                LitePal.saveAll(updateList);
                mChapterList.clear();
                mChapterList.addAll(updateList);
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (chapter_id == null) {
                            openCurrentChapter();
                        } else {
                            openCurrentChapter(chapter_id);
                        }
                    }
                });
            }
        } else {
            if (!isupdata) {//首次阅读
                Utils.hideLoadingDialog();
                ReadActivity.handleAnimation();
            } else {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (chapter_id == null) {
                            openCurrentChapter();
                        } else {
                            openCurrentChapter(chapter_id);
                        }
                    }
                });
            }

        }
    }

    public interface ChapterDownload {
        void finish();

    }
    public static void notfindChapter(final ChapterItem querychapterItem, final String book_id, final String chapter_id, final ChapterDownload download) {//找不到章节重新下载

        ReaderParams params = new ReaderParams(mContext);
        params.putExtraParams("book_id", book_id);
        params.putExtraParams("chapter_id", chapter_id);
        // params.putExtraParams("num", "1");
        String json = params.generateParamsJson();
        HttpUtils.getInstance(mContext).sendRequestRequestParams3(ReaderConfig.chapter_text, json, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        ChapterContent chapterContent = new Gson().fromJson(result, ChapterContent.class);
                        querychapterItem.setIs_preview(chapterContent.getIs_preview());
                        querychapterItem.setUpdate_time(chapterContent.getUpdate_time());
                        String filepath = FileManager.getSDCardRoot().concat("Reader/book/").concat(book_id + "/").concat(chapter_id + "/").concat(querychapterItem.getIs_preview() + "/").concat(querychapterItem.getUpdate_time()).concat(".txt");
                        FileManager.createFile(filepath, chapterContent.getContent().getBytes());
                        ContentValues values = new ContentValues();
                        values.put("chapteritem_begin", 0);
                        values.put("chapter_path", filepath);
                        values.put("is_preview", chapterContent.getIs_preview());
                        LitePal.updateAll(ChapterItem.class, values, "chapter_id = ?", chapter_id);
                        querychapterItem.setChapter_path(filepath);
                        download.finish();
                        ReaderConfig.REFREASH_USERCENTER = true;

                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );


    }


    public interface DownChapter {
        void success();

        void fail();

    }

    public static void downChapter(Activity mContext, final String mBookId, DownChapter downChapter) {

        ChapterItem chapterItem = LitePal.where("book_id = ?", mBookId).findFirst(ChapterItem.class);
        if (chapterItem == null) {

            ReaderParams params = new ReaderParams(mContext);
            params.putExtraParams("book_id", mBookId);
            String json = params.generateParamsJson();
            HttpUtils.getInstance(mContext).sendRequestRequestParams3(ReaderConfig.mChapterCatalogUrl, json, false, new HttpUtils.ResponseListener() {
                        @Override
                        public void onResponse(final String result) {
                            handledownChapter(result, mBookId, downChapter);
                        }

                        @Override
                        public void onErrorResponse(String ex) {
                            downChapter.fail();
                        }


                    }

            );
        } else {
            downChapter.success();
        }
    }

    private static void handledownChapter(String result, final String mBookId, DownChapter downChapter) {
        final List<ChapterItem> updateList = new ArrayList<>();
        int size = 0;
        try {
            JSONObject jsonObj = new JSONObject(result);
            String bookName = jsonObj.getString("name");
            JSONArray chapterListArr = jsonObj.getJSONArray("chapter_list");
            size = chapterListArr.length();
            if (size == 0) {
                downChapter.fail();
                return;
            }
            for (int i = 0; i < size; i++) {
                JSONObject jsonObject1 = chapterListArr.getJSONObject(i);
                String tempchapter_id = jsonObject1.getString("chapter_id");
                if (tempchapter_id != null && tempchapter_id.length() != 0) {
                    ChapterItem querychapterItem = new ChapterItem();
                    querychapterItem.setChapter_id(jsonObject1.getString("chapter_id"));
                    querychapterItem.setChapter_title(jsonObject1.getString("chapter_title"));
                    querychapterItem.setIs_vip(jsonObject1.getString("is_vip"));
                    querychapterItem.setIs_preview(jsonObject1.getString("is_preview"));
                    querychapterItem.setUpdate_time(jsonObject1.getString("update_time"));
                    querychapterItem.setDisplay_order(jsonObject1.getInt("display_order"));
                    querychapterItem.setChapteritem_begin(0);
                    querychapterItem.setBook_id(mBookId);
                    querychapterItem.setBook_name(bookName);
                    querychapterItem.setCharset("utf-8");
                    String filepath = FileManager.getSDCardRoot().concat("Reader/book/").concat(querychapterItem.getBook_id() + "/").concat(querychapterItem.getChapter_id() + "/").concat(querychapterItem.getIs_preview() + "/").concat(querychapterItem.getUpdate_time()).concat(".txt");
                    querychapterItem.setChapter_path(filepath);
                    if (i + 1 == size) {
                        querychapterItem.setNext_chapter_id("-2");
                    } else {
                        querychapterItem.setNext_chapter_id(chapterListArr.getJSONObject(i + 1).getString("chapter_id"));
                    }
                    if (i == 0) {
                        querychapterItem.setPre_chapter_id("-1");
                    } else {
                        querychapterItem.setPre_chapter_id(updateList.get(i - 1).getChapter_id());
                    }
                    updateList.add(querychapterItem);

                }
            }

        } catch (JSONException e) {

        }

        if (!updateList.isEmpty()) {
            LitePal.saveAll(updateList);
            downChapter.success();
        } else {
            downChapter.fail();
        }
    }
}
