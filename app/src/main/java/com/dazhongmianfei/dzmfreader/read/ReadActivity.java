package com.dazhongmianfei.dzmfreader.read;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.dialog.GetDialog;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.activity.AcquireBaoyueActivity;
import com.dazhongmianfei.dzmfreader.activity.CatalogInnerActivity;
import com.dazhongmianfei.dzmfreader.activity.LoginActivity;
import com.dazhongmianfei.dzmfreader.activity.WebViewActivity;
import com.dazhongmianfei.dzmfreader.bean.BaseAd;
import com.dazhongmianfei.dzmfreader.bean.ChapterItem;
import com.dazhongmianfei.dzmfreader.book.activity.BookCommentActivity;
import com.dazhongmianfei.dzmfreader.book.been.BaseBook;
import com.dazhongmianfei.dzmfreader.book.config.BookConfig;
import com.dazhongmianfei.dzmfreader.config.MainHttpTask;
import com.dazhongmianfei.dzmfreader.config.ReaderApplication;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.dialog.DownDialog;
import com.dazhongmianfei.dzmfreader.eventbus.CloseAnimation;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshBookInfo;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshBookSelf;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshMine;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.jinritoutiao.TodayOneAD;
import com.dazhongmianfei.dzmfreader.read.dialog.AutoProgress;
import com.dazhongmianfei.dzmfreader.read.dialog.AutoSettingDialog;
import com.dazhongmianfei.dzmfreader.read.dialog.BrightnessDialog;
import com.dazhongmianfei.dzmfreader.read.dialog.SettingDialog;
import com.dazhongmianfei.dzmfreader.read.manager.ChapterManager;
import com.dazhongmianfei.dzmfreader.read.util.BrightnessUtil;
import com.dazhongmianfei.dzmfreader.read.util.PageFactory;
import com.dazhongmianfei.dzmfreader.read.view.PageWidget;
import com.dazhongmianfei.dzmfreader.utils.FileManager;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyShare;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.NotchScreen;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;
import com.dazhongmianfei.dzmfreader.utils.Utils;
import com.dazhongmianfei.dzmfreader.view.MScrollView;

import com.dazhongmianfei.dzmfreader.view.ScrollEditText;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


import static com.dazhongmianfei.dzmfreader.book.fragment.NovelFragmentNew.BookShelfOpen;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.READBUTTOM_HEIGHT;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.USE_AD;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.XIAOSHUO;

//.TodayOneAD;
//.http.RequestParams;


/**
 * Created by Administrator on 2016/7/15 0015.
 */
public class ReadActivity extends BaseReadActivity {
    @Override
    public int initContentView() {
        return R.layout.activity_read;
    }

    // private static final String TAG = "ReadActivity";
    private final static String EXTRA_BOOK = "book";
    private final static String EXTRA_CHAPTER = "chapter";


    @BindView(R.id.bookpage)
    PageWidget bookpage;
    @BindView(R.id.activity_read_top_back_view)
    LinearLayout activity_read_top_back_view;
    @BindView(R.id.activity_read_top_menu)
    View activity_read_top_menu;
    @BindView(R.id.tv_directory)
    TextView tv_directory;
    @BindView(R.id.tv_brightness)
    TextView tv_brightness;
    @BindView(R.id.tv_comment)
    TextView tv_comment;
    @BindView(R.id.tv_setting)
    TextView tv_setting;
    @BindView(R.id.bookpop_bottom)
    RelativeLayout bookpop_bottom;
    @BindView(R.id.activity_read_bottom_view)
    RelativeLayout activity_read_bottom_view;
    @BindView(R.id.activity_read_change_day_night)
    ImageView activity_read_change_day_night;
    @BindView(R.id.titlebar_share)
    RelativeLayout titlebar_share;
    @BindView(R.id.titlebar_down)
    RelativeLayout titlebar_down;

    @BindView(R.id.activity_read_firstread)
    ImageView activity_read_firstread;

    @BindView(R.id.auto_read_progress_bar)
    ProgressBar auto_read_progress_bar;

    @BindView(R.id.list_ad_view_layout)
    public FrameLayout insert_todayone2;

    @BindView(R.id.activity_read_buttom_ad_layout)
    public FrameLayout activity_read_buttom_ad_layout;

    @BindView(R.id.activity_read_buttom_ad_layout_qq)
    public FrameLayout activity_read_buttom_ad_layout_qq;


    /*@BindView(R.id.insert_todayone)
    public FrameLayout insert_todayone;*/
    @BindView(R.id.tv_noad)
    TextView tv_noad;
    @BindView(R.id.activity_read_top_back_bookname)
    TextView activity_read_top_back_bookname;


    private ReadingConfig config;
    private WindowManager.LayoutParams lp;
    private ChapterItem chapter;
    private String mBookId;
    private PageFactory pageFactory;
    private Boolean isShow = false;
    private BrightnessDialog mBrightDialog;
    private SettingDialog mSettingDialog;
    private AutoSettingDialog mAutoSettingDialog;
    private Boolean mDayOrNight;
    private boolean isSpeaking = false;



    @BindView(R.id.activity_read_purchase_layout)
    LinearLayout activity_read_purchase_layout;

    BaseBook baseBook;
    // 接收电池信息更新的广播
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = intent.getIntExtra("level", 0);
                pageFactory.updateBattery(level);
            } else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                pageFactory.updateTime();
            }
        }
    };
    TodayOneAD todayOneAD;

    boolean showreward;

   /* @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMine refreshMine) {
        if (Utils.isLogin(activity)) {
            if (!showreward) {
                request_reward();
            }
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == 301) {
            int is_vip = data.getIntExtra("is_vip", 0);
            if (USE_AD && is_vip == 1) {
                ReaderConfig.USE_AD = false;

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) bookpage.getLayoutParams();
                layoutParams.height = mScreenHeight;
                bookpage.setLayoutParams(layoutParams);

                activity_read_buttom_ad_layout.setVisibility(View.GONE);
                insert_todayone2.setVisibility(View.GONE);
                tv_noad.setVisibility(View.GONE);

                try {
                    pageFactory.openBook(0, pageFactory.chapterItem, null);
                    handler.removeMessages(1);
                } catch (Exception e) {
                }

            }

        }
    }


    @Override
    public void initView() {
        if (isNotchEnable==0) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) activity_read_top_menu.getLayoutParams();
            layoutParams.height = ImageUtil.dp2px(this, 70);
            activity_read_top_menu.setLayoutParams(layoutParams);
        }
        if (ShareUitls.getString(ReadActivity.this, "FirstRead", "yes").equals("yes")) {
            ShareUitls.putString(ReadActivity.this, "FirstRead", "no");
            activity_read_firstread.setVisibility(View.VISIBLE);
            activity_read_firstread.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity_read_firstread.setVisibility(View.GONE);
                }
            });
        }
    }

   /* @Override
    protected void onRestart() {
        super.onRestart();
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) insert_todayone2.getLayoutParams();
        layoutParams.topMargin = pageFactory.Y;
        insert_todayone2.setLayoutParams(layoutParams);

    }*/

    @Override
    public void initData() {
        // try {
        if (ReaderConfig.USE_AD) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) bookpage.getLayoutParams();
            layoutParams.height = mScreenHeight - ImageUtil.dp2px(activity, READBUTTOM_HEIGHT);
            bookpage.setLayoutParams(layoutParams);

            bookpage.setADview(insert_todayone2);
            tv_noad.setVisibility(View.VISIBLE);
            handler.sendEmptyMessageDelayed(1, 30000);
        } else {
            activity_read_buttom_ad_layout.setVisibility(View.GONE);
            insert_todayone2.setVisibility(View.GONE);
            tv_noad.setVisibility(View.GONE);
        }
        config = ReadingConfig.getInstance();
        //  阅读管理器
        //获取intent中的携带的信息
        Intent intent = getIntent();
        chapter = (ChapterItem) intent.getSerializableExtra(EXTRA_CHAPTER);
        baseBook = (BaseBook) intent.getSerializableExtra(EXTRA_BOOK);
        pageFactory = new PageFactory(baseBook,  insert_todayone2,
                this,isNotchEnable,NavigationBarHeight);

        activity_read_top_back_bookname.setText(baseBook.getName());
        pageFactory.setPurchaseLayout(activity_read_purchase_layout);
        if (ReaderConfig.USE_AD) {
            pageFactory.getWebViewAD(ReadActivity.this);//获取广告
            handler.sendEmptyMessageDelayed(2, 20000);
            int noad_time = ShareUitls.getInt(activity, "close_AD", 0);
            if ((System.currentTimeMillis() - noad_time) / 60000 <= 20) {
                pageFactory.close_AD = true;
            }
            getWebViewAD(activity);
        }
        IntentFilter mfilter = new IntentFilter();
        mfilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mfilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(myReceiver, mfilter);

        mSettingDialog = new SettingDialog(this);
        mSettingDialog.setPageFactory(pageFactory);
        mBrightDialog = new BrightnessDialog(this);
        mAutoSettingDialog = new AutoSettingDialog(this);
        mAutoSettingDialog.setSettingDialog(mSettingDialog);
        ;
        //获取屏幕宽高
        WindowManager manage = getWindowManager();
        Display display = manage.getDefaultDisplay();
        Point displaysize = new Point();
        display.getSize(displaysize);
        //保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //隐藏

        //改变屏幕亮度
        if (!config.isSystemLight()) {
            BrightnessUtil.setBrightness(this, config.getLight());
        }

        mBookId = chapter.getBook_id();
        //壳子SD本地书籍
        if (mBookId.contains("/")) {
            tv_comment.setVisibility(View.GONE);
            titlebar_share.setVisibility(View.GONE);
        }

        bookpage.setPageMode(config.getPageMode());
        pageFactory.setPageWidget(bookpage);
        pageFactory.setLineSpacingMode(config.getLineSpacingMode());
        pageFactory.setFontSize((int) config.getFontSize());


        if (config.getPageMode() != 4) {
            pageFactory.openBook(0, chapter, null);
        } else {
            SettingDialog.scroll = true;
            pageFactory.openBook(4, chapter, null);
        }
        ReadTwoBook();


        initDayOrNight();
        initListener();


    }

    private void chengeChapter(boolean flag) {
        int chapter_possition = flag ? (pageFactory.chapterItem.getDisplay_order() + 1) : (pageFactory.chapterItem.getDisplay_order() - 1);
        String chapter_id = flag ? pageFactory.chapterItem.getNext_chapter_id() : pageFactory.chapterItem.getPre_chapter_id();

        ChapterManager.getInstance(ReadActivity.this).getChapter(chapter_possition, chapter_id, new ChapterManager.QuerychapterItemInterface() {
            @Override
            public void success(final ChapterItem querychapterItem) {

                final String nextChapterId = querychapterItem.getChapter_id();
                if (querychapterItem.getChapter_path() == null) {
                    String path = FileManager.getSDCardRoot().concat("Reader/book/").concat(mBookId + "/").concat(nextChapterId + "/").concat(querychapterItem.getIs_preview() + "/").concat(querychapterItem.getUpdate_time()).concat(".txt");

                    if (FileManager.isExist(path)) {
                        ContentValues values = new ContentValues();
                        values.put("chapter_path", path);
                        LitePal.updateAll(ChapterItem.class, values, "book_id = ? and chapter_id = ?", mBookId, nextChapterId);
                        querychapterItem.setChapter_path(path);
                        if (SettingDialog.scroll) {
                            SettingDialog.scroll = false;
                            pageFactory.openBook(4, querychapterItem, null);
                            SettingDialog.scroll = true;
                        }
                    } else {
                        ChapterManager.notfindChapter(pageFactory.chapterItem, mBookId, pageFactory.chapterItem.getChapter_id(), new ChapterManager.ChapterDownload() {
                            @Override
                            public void finish() {
                                if (SettingDialog.scroll) {
                                    SettingDialog.scroll = false;
                                    pageFactory.openBook(4, querychapterItem, null);
                                    SettingDialog.scroll = true;
                                }
                            }
                        });
                    }
                } else {
                    if (SettingDialog.scroll) {
                        SettingDialog.scroll = false;
                        pageFactory.openBook(4, querychapterItem, null);
                        SettingDialog.scroll = true;
                    }
                }


                ChapterManager.getInstance(ReadActivity.this).addDownloadTask(true, flag ? querychapterItem.getNext_chapter_id() : querychapterItem.getPre_chapter_id(), new ChapterManager.ChapterDownload() {
                    @Override
                    public void finish() {

                    }
                });

            }

            @Override
            public void fail() {

            }
        });
    }


    protected void initListener() {

        mSettingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ;
            }
        });

        mSettingDialog.setSettingListener(new SettingDialog.SettingListener() {
            @Override
            public void changeSystemBright(Boolean isSystem, float brightness) {
                if (!isSystem) {
                    BrightnessUtil.setBrightness(ReadActivity.this, brightness);
                } else {
                    int bh = BrightnessUtil.getScreenBrightness(ReadActivity.this);
                    BrightnessUtil.setBrightness(ReadActivity.this, bh);
                }
            }

            @Override
            public void changeFontSize(int fontSize) {
                pageFactory.changeFontSize(fontSize);
            }

            @Override
            public void changeTypeFace(Typeface typeface) {
                pageFactory.changeTypeface(typeface);
            }

            @Override
            public void changeBookBg(int type) {
                pageFactory.changeBookBg(type);
            }

            @Override
            public void changeLineSpacing(int mode) {
                pageFactory.changeLineSpacing(mode);
            }
        });

        pageFactory.setPageEvent(new PageFactory.PageEvent() {
            @Override
            public void changeProgress(float progress) {

            }
        });

        bookpage.setTouchListener(new PageWidget.TouchListener() {
            @Override
            public void center() {
                //ZANTING_TIME = 0;
                if (AutoProgress.getInstance().isStarted()) {
                    if (!mAutoSettingDialog.isShowing()) {
                        AutoProgress.getInstance().pause();
                        mAutoSettingDialog.show();
                        ;
                    }

                    return;
                }

                if (isShow) {
                    hideReadSetting();
                } else {
                    showReadSetting();
                }
            }

            @Override
            public Boolean prePage() {
                //ZANTING_TIME = 0;
                if (AutoProgress.getInstance().isStarted()) {
                    AutoProgress.getInstance().pause();
                }

                if (isShow || isSpeaking) {
                    return false;
                }
                try {
                    pageFactory.prePage();
                } catch (Exception e) {
                }
                return !pageFactory.isfirstPage();

            }

            @Override
            public Boolean nextPage() {
                //ZANTING_TIME = 0;
                if (AutoProgress.getInstance().isStarted()) {
                    AutoProgress.getInstance().pause();
                }

                Utils.printLog("setTouchListener", "nextPage");
                if (isShow || isSpeaking) {
                    return false;
                }
                try {
                    pageFactory.nextPage();
                } catch (Exception e) {
                }
                return !pageFactory.islastPage();
            }

            @Override
            public void cancel() {
                //ZANTING_TIME = 0;
                pageFactory.cancelPage();
            }
        });
        if (!ReaderConfig.USE_SHARE) {
            titlebar_share.setVisibility(View.GONE);
        } else {
            titlebar_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (baseBook != null) {
                        String url = ReaderConfig.BASE_URL + "/site/share?uid=" + Utils.getUID(ReadActivity.this) + "&book_id=" + mBookId + "&chapter_id=" + pageFactory.chapterItem.getChapter_id() + "&osType=2&product=1";
                        UMWeb web = new UMWeb(url);
                        web.setTitle(baseBook.getName());//标题
                        web.setThumb(new UMImage(ReadActivity.this, baseBook.getCover()));  //缩略图
                        web.setDescription(baseBook.getDescription());//描述
                        MyShare.Share(ReadActivity.this, "", web);
                    }
                }
            });
        }
        titlebar_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownDialog().getDownoption(ReadActivity.this, baseBook, pageFactory.chapterItem);
                // DownDialog.showOpen = false;
            }
        });

    }


    public PageWidget getBookPage() {
        return bookpage;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isShow) {
            ;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (AutoProgress.getInstance().isStarted()) {
                if (!mAutoSettingDialog.isShowing()) {
                    AutoProgress.getInstance().pause();
                    mAutoSettingDialog.show();
                    ;
                    return true;
                }
            }

            if (isShow) {
                hideReadSetting();
                return true;
            }
            if (mSettingDialog.isShowing()) {
                mSettingDialog.hide();
                return true;
            }
            if (mBrightDialog.isShowing()) {
                mBrightDialog.hide();
                return true;
            }
//询问加入书架//Utils.isBookInShelf(this, mBookId)[0]
            if (baseBook.isAddBookSelf() == 1) {
                handleAnimation();
                finish();
            } else {
                askIsNeedToAddShelf();
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 询问是否加入书架
     */
    private void askIsNeedToAddShelf() {
        final Dialog dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.dialog_add_shelf, null);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        //设置对话框的大小
        view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(this).getScreenHeight() * 0.2f));
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtils.getInstance(this).getScreenWidth() * 0.75f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                handleAnimation();
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseBook.saveIsexist(1);
                baseBook.setAddBookSelf(1);
                List<BaseBook> list = new ArrayList<>();
                list.add(baseBook);
                EventBus.getDefault().post(new RefreshBookSelf(list));
                EventBus.getDefault().post(new RefreshBookInfo(true));
                dialog.dismiss();
                handleAnimation();
                finish();


            }
        });
        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!AutoProgress.getInstance().isStop()) {
            AutoProgress.getInstance().stop();
        }
        if (mSettingDialog.isShowing()) {
            mSettingDialog.dismiss();
        }

    }


    public static boolean openBook(final BaseBook baseBook, final ChapterItem chapterItem, Activity context) {

        Intent intent = new Intent(context, ReadActivity.class);
        intent.putExtra(EXTRA_CHAPTER, chapterItem);
        intent.putExtra(EXTRA_BOOK, baseBook);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        context.startActivity(intent);
        Utils.hideLoadingDialog();
       /* if (!ReaderConfig.CatalogInnerActivityOpen) {
            PageFactory.close_AD = false;
        }*/
        ReaderConfig.CatalogInnerActivityOpen = false;
        return true;
    }

    /**
     * 隐藏菜单。沉浸式阅读
     */


    public void initDayOrNight() {
        mDayOrNight = config.getDayOrNight();
        if (mDayOrNight) {
            activity_read_change_day_night.setImageResource(R.mipmap.light_mode);
        } else {
            activity_read_change_day_night.setImageResource(R.mipmap.night_mode);
        }
    }

    //改变显示模式
    public void changeDayOrNight() {
        if (mDayOrNight) {
            mDayOrNight = false;
            activity_read_change_day_night.setImageResource(R.mipmap.night_mode);
        } else {
            mDayOrNight = true;
            activity_read_change_day_night.setImageResource(R.mipmap.light_mode);
        }
        config.setDayOrNight(mDayOrNight);
        pageFactory.setDayOrNight(mDayOrNight);
    }

    //设置菜单
    private void showReadSetting() {
        isShow = true;
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.menu_ins);
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.menu_in);
        activity_read_bottom_view.startAnimation(topAnim);
        activity_read_top_menu.startAnimation(bottomAnim);
        activity_read_bottom_view.setVisibility(View.VISIBLE);
        activity_read_top_menu.setVisibility(View.VISIBLE);


    }

    private void hideReadSetting() {
        isShow = false;
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.menu_outs);
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.menu_out);
        if (activity_read_bottom_view.getVisibility() == View.VISIBLE) {
            activity_read_bottom_view.startAnimation(topAnim);
        }
        if (activity_read_top_menu.getVisibility() == View.VISIBLE) {
            activity_read_top_menu.startAnimation(bottomAnim);
        }

        activity_read_bottom_view.setVisibility(View.GONE);
        activity_read_top_menu.setVisibility(View.GONE);
        ;
    }

    public PageFactory getPageFactory() {
        return pageFactory;
    }

    @OnClick({R.id.tv_noad, R.id.tv_brightness, R.id.activity_read_top_back_view, R.id.tv_directory, R.id.tv_comment, R.id.tv_setting, R.id.bookpop_bottom, R.id.activity_read_bottom_view, R.id.activity_read_change_day_night})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_directory:
                if (CatalogInnerActivity.activity != null) {
                    CatalogInnerActivity.activity.finish();
                }
                hideReadSetting();
                Intent intent = new Intent(this, CatalogInnerActivity.class);
                intent.putExtra("book_id", mBookId);
                intent.putExtra("book", baseBook);
                intent.putExtra("display_order", pageFactory.chapterItem.getDisplay_order());
                startActivity(intent);
                break;
            case R.id.activity_read_change_day_night:
                changeDayOrNight();
                break;
            case R.id.tv_comment:
                hideReadSetting();
                //打开评论页面
                Intent intentComment = new Intent(this, BookCommentActivity.class);
                intentComment.putExtra("book_id", mBookId);
                startActivity(intentComment);
                break;
            case R.id.tv_setting:
                hideReadSetting();
                mSettingDialog.setProgressBar(auto_read_progress_bar);
                mSettingDialog.show();
                break;
            case R.id.bookpop_bottom:
                break;
            case R.id.activity_read_bottom_view:
                break;
            case R.id.activity_read_top_back_view:
                if (baseBook.isAddBookSelf() == 1) {
                    handleAnimation();
                    finish();
                } else {
                    askIsNeedToAddShelf();
                }
                break;
            case R.id.tv_brightness:
                hideReadSetting();
                mBrightDialog.show();
                break;
            case R.id.tv_noad:
                hideReadSetting();
                if (ReaderConfig.USE_AD) {
                    if (pageFactory.close_AD) {
                        MyToash.ToashSuccess(activity, "阅读界面广告已关闭");
                        return;
                    }
                    if (todayOneAD != null) {
                        GetDialog.IsOperation(ReadActivity.this, "去广告", "观看完整视频可以免广告阅读20分钟,是否观看？", new GetDialog.IsOperationInterface() {
                            @Override
                            public void isOperation() {
                                todayOneAD.loadJiliAd(new TodayOneAD.OnRewardVerify() {
                                    @Override
                                    public void OnRewardVerify() {
                                        pageFactory.close_AD = true;
                                    }
                                });
                            }
                        });

                    }
                    /*if (MainHttpTask.getInstance().Gotologin(activity)) {
                        startActivityForResult(new Intent(activity, AcquireBaoyueActivity.class), 301);
                    }*/
                    // NoAD(this, pageFactory, null, true);

                 /*   GetDialog.IsOperation(ReadActivity.this, "去广告", "是否观看视频消除后10个章节的广告?", new GetDialog.IsOperationInterface() {
                        @Override
                        public void isOperation() {
                            todayOneAD.loadJiliAd("912218745", TTAdConstant.VERTICAL, new OnRewardVerify() {
                                @Override
                                public void OnRewardVerify() {
                                    adVideo_complete();
                                }
                            });
                            if (todayOneAD.mttRewardVideoAd != null) {
                                todayOneAD.mttRewardVideoAd.showRewardVideoAd(ReadActivity.this);
                            } else {
                                handler.sendEmptyMessage(1);
                            }
                        }
                    });*/
                }
                break;
        }
    }

    public static void handleAnimation() {

        try {
            if (BookShelfOpen) {
                EventBus.getDefault().post(new CloseAnimation());
            }
        } catch (Exception e) {
        }

    }


    //每日阅读两本书 就上传接口  完成阅读任务
    public void ReadTwoBook() {
        if (!ReaderConfig.USE_PAY) {
            return;
        }
        if (!Utils.isLogin(this)) {
            return;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        final String ReadTwoBookDate = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳

        final String flag = ShareUitls.getString(this, "ReadTwoBookDate", ReadTwoBookDate + "-0-0");


        String[] flag2 = flag.split("-");
        if (!flag2[0].equals(ReadTwoBookDate)) {
            ShareUitls.putString(this, "ReadTwoBookDate", ReadTwoBookDate + "-" + mBookId + "-0");
            return;
        }
        if (!flag2[2].equals("0")) {
            return;
        } else if (flag2[1].equals("0")) {
            ShareUitls.putString(this, "ReadTwoBookDate", ReadTwoBookDate + "-" + mBookId + "-0");
            return;
        } else if (flag2[1].equals(mBookId)) {
            return;
        }

        final ReaderParams params = new ReaderParams(this);
        String json = params.generateParamsJson();
        HttpUtils.getInstance(this).sendRequestRequestParams3(ReaderConfig.task_read, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        EventBus.getDefault().post(new RefreshMine(null));
                        ShareUitls.putString(ReadActivity.this, "ReadTwoBookDate", flag + mBookId);
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );


    }


    public void NoAD(final Activity activity, final PageFactory pageFactory, final ChapterItem chapterItem, final boolean AUTO_NOAD) {
        if (pageFactory.close_AD) {
            MyToash.ToashSuccess(activity, "广告已关闭");
            return;
        }

    /*    if (AUTO_NOAD) {
            if (!AppPrefs.getSharedBoolean(activity, ReaderConfig.AUTONOAD, true) || !Utils.isLogin(activity)) {
                return;
            }
        }*/

        ReaderParams params = new ReaderParams(activity);
        String json = params.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParams3(BookConfig.del_ad, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String result) {
                        if (result.equals("true")) {
                            pageFactory.close_AD = true;
                            ShareUitls.putInt(activity, "close_AD", (int) System.currentTimeMillis());
                            EventBus.getDefault().post(new RefreshMine(null));
                            MyToash.ToashSuccess(activity, "广告已关闭");
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );

    }

/*

    //上报监理
    boolean zanting;

    public void report_reward() {
        zanting = true;
        final ReaderParams params = new ReaderParams(this);
        String json = params.generateParamsJson();
        HttpUtils.getInstance(this).sendRequestRequestParams3(ReaderConfig.BASE_URL + "/remain/report-reward", json, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int getgold = jsonObject.getInt("reward_gold");
                            if (getgold > 0) {
                                final GoodView goodView = new GoodView(activity);
                                goodView.setDuration(4000);
                                goodView.setDistance(70);
                                // goodView.setTranslateY(((int)activity_read_currentY+ 20), 0);
                                goodView.setTextInfo("+" + getgold + jsonObject.getString("unit"), Color.RED, 15);
                                goodView.show(read_ringProgress);
                                zanting = false;
                                READ_TIME = 0;
                            } else {
                                handler.removeMessages(4);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );


    }


    public void request_reward() {
        final ReaderParams params = new ReaderParams(this);
        String json = params.generateParamsJson();
        HttpUtils.getInstance(this).sendRequestRequestParams3(ReaderConfig.BASE_URL + "/remain/request-reward", json, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("show") == 1) {
                                read_ringProgress.setVisibility(View.VISIBLE);
                                if (!showreward) {
                                    showreward = true;
                                    handler.sendEmptyMessage(4);
                                }
                            } else {
                                showreward = false;
                                read_ringProgress.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );


    }

    long READ_TIME_START = 0;
    int READ_TIME, //ZANTING_TIME;
   // boolean ZANTING_REWARD;

    private void ReadTime() {
        if (!Utils.isLogin(activity)) {
            return;
        }
        final ReaderParams params = new ReaderParams(this);
        String requestParams;
        requestParams = ReaderConfig.BASE_URL + "/remain/add-read-time";
        params.putExtraParams("read_time", (System.currentTimeMillis() - READ_TIME_START) / 1000 + "");

        String json = params.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParams3(requestParams, json, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        EventBus.getDefault().post(new RefreshMine(null));
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );
    }
*/


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    getWebViewAD(activity);
                    handler.sendEmptyMessageDelayed(1, 30000);
                    break;
                case 2:
                    pageFactory.getWebViewAD(ReadActivity.this);//获取广告
                    handler.sendEmptyMessageDelayed(2, 20000);
                    break;
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ReadTime();
        try {
            //handler.removeMessages(4);
            if (USE_AD) {
                handler.removeMessages(1);
                handler.removeMessages(2);
            }
        } catch (Exception e) {
        }
        pageFactory.clear();
        bookpage = null;
        unregisterReceiver(myReceiver);
        isSpeaking = false;
    }


    //BaseAd baseAd;
    // ImageView list_ad_view_img;

    public void getWebViewAD(Activity activity) {

        if (todayOneAD == null) {
            todayOneAD = new TodayOneAD(activity, 3, "925050875");
            pageFactory.setTodayOneAD(todayOneAD, activity_read_buttom_ad_layout);
        }
        todayOneAD.getTodayOneBanner(activity_read_buttom_ad_layout, activity_read_buttom_ad_layout_qq, 3);



        /*if (baseAd == null) {
            ReaderParams params = new ReaderParams(activity);
            String requestParams = ReaderConfig.BASE_URL + "/advert/info";
            params.putExtraParams("type", XIAOSHUO + "");
            params.putExtraParams("position", "12");
            String json = params.generateParamsJson();
            HttpUtils.getInstance(activity).sendRequestRequestParams3(requestParams, json, false, new HttpUtils.ResponseListener() {
                        @Override
                        public void onResponse(final String result) {
                            try {
                                baseAd = new Gson().fromJson(result, BaseAd.class);
                                if (baseAd.ad_type == 1) {
                                    insert_todayone2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent();
                                            intent.setClass(activity, WebViewActivity.class);
                                            intent.putExtra("url", baseAd.ad_skip_url);
                                            intent.putExtra("title", baseAd.ad_title);
                                            intent.putExtra("advert_id", baseAd.advert_id);
                                            activity.startActivity(intent);
                                        }
                                    });
                                    if (list_ad_view_img == null) {
                                        list_ad_view_img = new ImageView(activity);
                                        list_ad_view_img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ImageUtil.dp2px(activity, READBUTTOM_HEIGHT));
                                        //  params.leftMargin = ImageUtil.dp2px(activity, 10);
                                        // params.rightMargin = ImageUtil.dp2px(activity, 10);
                                        activity_read_buttom_ad_layout.addView(list_ad_view_img, params);
                                    }
                                    ImageLoader.getInstance().displayImage(baseAd.ad_image, list_ad_view_img, ReaderApplication.getOptions());
                                } else {
                                    if (todayOneAD == null) {
                                        todayOneAD = new TodayOneAD(activity, 3, baseAd.ad_android_key);
                                    }
                                    pageFactory.setTodayOneAD(todayOneAD, activity_read_buttom_ad_layout);
                                    todayOneAD.getTodayOneBanner(activity_read_buttom_ad_layout, activity_read_buttom_ad_layout_qq, 3);

                                }

                            } catch (Exception e) {
                            }
                        }

                        @Override
                        public void onErrorResponse(String ex) {

                        }
                    }

            );
        } else {
            if (baseAd.ad_type == 1) {
                insert_todayone2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(activity, WebViewActivity.class);
                        intent.putExtra("url", baseAd.ad_skip_url);
                        intent.putExtra("title", baseAd.ad_title);
                        intent.putExtra("advert_id", baseAd.advert_id);
                        activity.startActivity(intent);
                    }
                });
                if (list_ad_view_img == null) {
                    list_ad_view_img = new ImageView(activity);
                    list_ad_view_img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ImageUtil.dp2px(activity, READBUTTOM_HEIGHT));
                    insert_todayone2.addView(list_ad_view_img, params);

                }
                ImageLoader.getInstance().displayImage(baseAd.ad_image, list_ad_view_img, ReaderApplication.getOptions());
            } else {
                if (todayOneAD == null) {
                    todayOneAD = new TodayOneAD(activity, 3, baseAd.ad_android_key);
                }
                todayOneAD.getTodayOneBanner(activity_read_buttom_ad_layout, activity_read_buttom_ad_layout_qq, 3);

            }
        }*/


    }


}
