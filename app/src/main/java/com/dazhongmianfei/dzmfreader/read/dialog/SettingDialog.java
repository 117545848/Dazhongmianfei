package com.dazhongmianfei.dzmfreader.read.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.read.ReadActivity;
import com.dazhongmianfei.dzmfreader.read.ReadingConfig;
import com.dazhongmianfei.dzmfreader.read.manager.ChapterManager;
import com.dazhongmianfei.dzmfreader.read.util.PageFactory;
import com.dazhongmianfei.dzmfreader.utils.MyToash;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class SettingDialog extends Dialog {

    @BindView(R2.id.tv_subtract)
    View tv_subtract;
    @BindView(R2.id.tv_size)
    TextView tv_size;
    @BindView(R2.id.tv_add)
    View tv_add;

    @BindView(R2.id.iv_bg_default)
    View iv_bg_default;
    @BindView(R2.id.iv_bg_1)
    View iv_bg1;
    @BindView(R2.id.iv_bg_2)
    View iv_bg2;
    @BindView(R2.id.iv_bg_3)
    View iv_bg3;
    @BindView(R2.id.iv_bg_4)
    View iv_bg4;
    @BindView(R2.id.iv_bg_7)
    View iv_bg7;
    @BindView(R2.id.iv_bg_8)
    View iv_bg8;


    @BindView(R2.id.iv_bg_default_select)
    View iv_bg_default_select;
    @BindView(R2.id.iv_bg_1_select)
    View iv_bg1_select;
    @BindView(R2.id.iv_bg_2_select)
    View iv_bg2_select;
    @BindView(R2.id.iv_bg_3_select)
    View iv_bg3_select;
    @BindView(R2.id.iv_bg_4_select)
    View iv_bg4_select;
    @BindView(R2.id.iv_bg_7_select)
    View iv_bg7_select;
    @BindView(R2.id.iv_bg_8_select)
    View iv_bg8_select;


    @BindView(R2.id.tv_simulation)
    TextView tv_simulation;
    @BindView(R2.id.tv_cover)
    TextView tv_cover;
    @BindView(R2.id.tv_slide)
    TextView tv_slide;
    @BindView(R2.id.tv_none)
    TextView tv_none;
    @BindView(R2.id.tv_shangxia)
    TextView tv_shangxia;


    @BindView(R2.id.margin_big)
    View margin_big;
    @BindView(R2.id.margin_medium)
    View margin_medium;
    @BindView(R2.id.margin_small)
    View margin_small;
    @BindView(R2.id.margin_big_tv)
    View margin_big_tv;
    @BindView(R2.id.margin_medium_tv)
    View margin_medium_tv;
    @BindView(R2.id.margin_small_tv)
    View margin_small_tv;

    @BindView(R2.id.auto_read_layout)
    View auto_read_layout;

    @BindView(R2.id.tv_jianfan)
    View tv_jianfan;


    private ReadActivity mContext;
    private ReadingConfig config;
    private Boolean isSystem;
    private SettingListener mSettingListener;
    private int FONT_SIZE_MIN;
    private int FONT_SIZE_MAX;
    private int currentFontSize;
    private ProgressBar auto_read_progress_bar;
    public static boolean scroll;

    private SettingDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    PageFactory pageFactory;

    public SettingDialog(Context context) {
        this(context, R.style.setting_dialog);
        mContext = (ReadActivity) context;
    }

    public SettingDialog(Context context, int themeResId) {
        super(context, themeResId);

    }

    public void setPageFactory(PageFactory pageFactory) {
        this.pageFactory = pageFactory;
    }

    private void setBG(int i) {
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(10f);
        gd.setGradientType(GradientDrawable.RECTANGLE);

        switch (i) {
            case 0:
                gd.setColor(mContext.getResources().getColor(R.color.read_bg_default));
                iv_bg_default.setBackground(gd);
                break;
            case 1:
                gd.setColor(mContext.getResources().getColor(R.color.read_bg_1));
                iv_bg1.setBackground(gd);
                break;
            case 2:
                gd.setColor(mContext.getResources().getColor(R.color.read_bg_2));
                iv_bg2.setBackground(gd);
                break;
            case 3:
                gd.setColor(mContext.getResources().getColor(R.color.read_bg_3));
                iv_bg3.setBackground(gd);
                break;
            case 4:
                gd.setStroke(1, mContext.getResources().getColor(R.color.gray2));//描边的颜色和宽度
                gd.setColor(Color.WHITE);
                iv_bg4.setBackground(gd);
                break;
            case 7:
                gd.setColor(mContext.getResources().getColor(R.color.read_bg_7));
                iv_bg7.setBackground(gd);
                break;
            case 8:
                gd.setColor(mContext.getResources().getColor(R.color.read_bg_8));
                iv_bg8.setBackground(gd);
                break;
        }
    }

    private void set_seclet(int i, boolean flag) {
        switch (i) {
            case 0:
                if (flag) {
                   iv_bg_default_select.setVisibility(View.VISIBLE);
                }else {
                    iv_bg_default_select.setVisibility(View.GONE);
                }
                break;
            case 1:
                if (flag) {
                    iv_bg1_select.setVisibility(View.VISIBLE);
                }else {
                    iv_bg1_select.setVisibility(View.GONE);
                }
                break;
            case 2:
                if (flag) {
                    iv_bg2_select.setVisibility(View.VISIBLE);
                }else {
                    iv_bg2_select.setVisibility(View.GONE);
                }
                break;
            case 3:
                if (flag) {
                    iv_bg3_select.setVisibility(View.VISIBLE);
                }else {
                    iv_bg3_select.setVisibility(View.GONE);
                }
                break;
            case 4:
                if (flag) {
                    iv_bg4_select.setVisibility(View.VISIBLE);
                }else {
                    iv_bg4_select.setVisibility(View.GONE);
                }
                break;
            case 7:
                if (flag) {
                    iv_bg7_select.setVisibility(View.VISIBLE);
                }else {
                    iv_bg7_select.setVisibility(View.GONE);
                }

                break;
            case 8:
                if (flag) {
                    iv_bg8_select.setVisibility(View.VISIBLE);
                }else {
                    iv_bg8_select.setVisibility(View.GONE);
                }

                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);
        setContentView(R.layout.dialog_setting);
        // 初始化View注入
        ButterKnife.bind(this);
        for (int i = 0; i <= 8; i++) {
            if (i != 5 && i != 6) {
                setBG(i);
            }
        }

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);

        FONT_SIZE_MIN = (int) getContext().getResources().getDimension(R.dimen.reading_min_text_size);
        FONT_SIZE_MAX = (int) getContext().getResources().getDimension(R.dimen.reading_max_text_size);

        config = ReadingConfig.getInstance();
        set_seclet(config.getBookBgType(),true);
        //是上下滑动模式
        if (config.getPageMode() == 4) {
            scroll = true;
        }
        //初始化亮度
        isSystem = config.isSystemLight();
//        defaultFontSize();
        //初始化字体大小
        currentFontSize = (int) config.getFontSize();
        tv_size.setText(currentFontSize + "");

        //初始化字体
//        tv_default.setTypeface(config.getTypeface(ReadingConfig.FONTTYPE_DEFAULT));
//        tv_qihei.setTypeface(config.getTypeface(ReadingConfig.FONTTYPE_QIHEI));
//        tv_fzxinghei.setTypeface(config.getTypeface(Config.FONTTYPE_FZXINGHEI));
//        tv_fzkatong.setTypeface(config.getTypeface(ReadingConfig.FONTTYPE_FZKATONG));
//        tv_bysong.setTypeface(config.getTypeface(ReadingConfig.FONTTYPE_BYSONG));
//        tv_xinshou.setTypeface(config.getTypeface(Config.FONTTYPE_XINSHOU));
//        tv_wawa.setTypeface(config.getTypeface(Config.FONTTYPE_WAWA));
//        selectTypeface(config.getTypefacePath());
        selectFontSize(config.getFontSize());
        selectBg(config.getBookBgType());
        selectPageMode(ReadingConfig.getInstance().getPageMode());
        selectLineSpacing(config.getLineSpacingMode());


    }

    public void selectFontSize(float size) {
        currentFontSize = (int) size;
        tv_size.setText(currentFontSize + "");
    }

    //选择翻页
    private void selectPageMode(int pageMode) {
        if (pageMode == ReadingConfig.PAGE_MODE_SIMULATION) {
            setTextViewSelect(tv_simulation, true);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_none, false);
            setTextViewSelect(tv_shangxia, false);
        } else if (pageMode == ReadingConfig.PAGE_MODE_COVER) {
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, true);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_none, false);
            setTextViewSelect(tv_shangxia, false);
        } else if (pageMode == ReadingConfig.PAGE_MODE_SLIDE) {
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, true);
            setTextViewSelect(tv_none, false);
            setTextViewSelect(tv_shangxia, false);
        } else if (pageMode == ReadingConfig.PAGE_MODE_SCROLL) {
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_shangxia, true);
            setTextViewSelect(tv_none, false);
        } else if (pageMode == ReadingConfig.PAGE_MODE_NONE) {
            setTextViewSelect(tv_shangxia, false);
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_none, true);
        }
    }

    //设置按钮选择的背景
    private void setLineSpacingViewSelect(View view, View tv, Boolean isSelect, int selectedRes, int unSelectedRes) {
        if (isSelect) {
            view.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.shape_setting_dialog_font_bg_pressed));
            tv.setBackgroundResource(selectedRes);
        } else {
            view.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.shape_setting_dialog_font_bg_unpressed));
            tv.setBackgroundResource(unSelectedRes);
        }
    }

    //选择间距
    private void selectLineSpacing(int marginMode) {
        if (marginMode == ReadingConfig.LINE_SPACING_BIG) {
            setLineSpacingViewSelect(margin_big, margin_big_tv, true, R.mipmap.line_spacing_big_select, R.mipmap.line_spacing_big);
            setLineSpacingViewSelect(margin_medium, margin_medium_tv, false, R.mipmap.line_spacing_medium_select, R.mipmap.line_spacing_medium);
            setLineSpacingViewSelect(margin_small, margin_small_tv, false, R.mipmap.line_spacing_small_select, R.mipmap.line_spacing_small);
        } else if (marginMode == ReadingConfig.LINE_SPACING_MEDIUM) {
            setLineSpacingViewSelect(margin_big, margin_big_tv, false, R.mipmap.line_spacing_big_select, R.mipmap.line_spacing_big);
            setLineSpacingViewSelect(margin_medium, margin_medium_tv, true, R.mipmap.line_spacing_medium_select, R.mipmap.line_spacing_medium);
            setLineSpacingViewSelect(margin_small, margin_small_tv, false, R.mipmap.line_spacing_small_select, R.mipmap.line_spacing_small);
        } else if (marginMode == ReadingConfig.LINE_SPACING_SMALL) {
            setLineSpacingViewSelect(margin_big, margin_big_tv, false, R.mipmap.line_spacing_big_select, R.mipmap.line_spacing_big);
            setLineSpacingViewSelect(margin_medium, margin_medium_tv, false, R.mipmap.line_spacing_medium_select, R.mipmap.line_spacing_medium);
            setLineSpacingViewSelect(margin_small, margin_small_tv, true, R.mipmap.line_spacing_small_select, R.mipmap.line_spacing_small);
        }
    }

    //选择背景
    private void selectBg(int type) {
//        switch (type) {
//            case ReadingConfig.BOOK_BG_DEFAULT:
//                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
//                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                break;
//            case ReadingConfig.BOOK_BG_1:
//                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
//                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                break;
//            case ReadingConfig.BOOK_BG_2:
//                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
//                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                break;
//            case ReadingConfig.BOOK_BG_3:
//                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
//                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                break;
//            case ReadingConfig.BOOK_BG_4:
//                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
//                break;
//        }
    }

    //设置背景
    public void setBookBg(int type) {
        config.setBookBg(type);
        if (mSettingListener != null) {
            mSettingListener.changeBookBg(type);
        }
    }

//    //选择字体
//    private void selectTypeface(String typeface) {
//        if (typeface.equals(ReadingConfig.FONTTYPE_DEFAULT)) {
//            setTextViewSelect(tv_default, true);
//            setTextViewSelect(tv_qihei, false);
//            setTextViewSelect(tv_fzxinghei, false);
//            setTextViewSelect(tv_fzkatong, false);
//            setTextViewSelect(tv_bysong, false);
////            setTextViewSelect(tv_xinshou, false);
////            setTextViewSelect(tv_wawa, false);
//        } else if (typeface.equals(ReadingConfig.FONTTYPE_QIHEI)) {
//            setTextViewSelect(tv_default, false);
//            setTextViewSelect(tv_qihei, true);
//            setTextViewSelect(tv_fzxinghei, false);
//            setTextViewSelect(tv_fzkatong, false);
//            setTextViewSelect(tv_bysong, false);
////            setTextViewSelect(tv_xinshou, false);
////            setTextViewSelect(tv_wawa, false);
//        } else if (typeface.equals(ReadingConfig.FONTTYPE_FZXINGHEI)) {
//            setTextViewSelect(tv_default, false);
//            setTextViewSelect(tv_qihei, false);
//            setTextViewSelect(tv_fzxinghei, true);
//            setTextViewSelect(tv_fzkatong, false);
//            setTextViewSelect(tv_bysong, false);
////            setTextViewSelect(tv_xinshou, true);
////            setTextViewSelect(tv_wawa, false);
//        } else if (typeface.equals(ReadingConfig.FONTTYPE_FZKATONG)) {
//            setTextViewSelect(tv_default, false);
//            setTextViewSelect(tv_qihei, false);
//            setTextViewSelect(tv_fzxinghei, false);
//            setTextViewSelect(tv_fzkatong, true);
//            setTextViewSelect(tv_bysong, false);
////            setTextViewSelect(tv_xinshou, false);
////            setTextViewSelect(tv_wawa, true);
//        } else if (typeface.equals(ReadingConfig.FONTTYPE_BYSONG)) {
//            setTextViewSelect(tv_default, false);
//            setTextViewSelect(tv_qihei, false);
//            setTextViewSelect(tv_fzxinghei, false);
//            setTextViewSelect(tv_fzkatong, false);
//            setTextViewSelect(tv_bysong, true);
////            setTextViewSelect(tv_xinshou, false);
////            setTextViewSelect(tv_wawa, true);
//        }
//    }

    //设置按钮选择的背景
    private void setTextViewSelect(TextView textView, Boolean isSelect) {
        if (isSelect) {
            textView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.shape_setting_dialog_font_bg_pressed));
            textView.setTextColor(getContext().getResources().getColor(R.color.white));
        } else {
            textView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.shape_setting_dialog_font_bg_unpressed));
            textView.setTextColor(getContext().getResources().getColor(R.color.black));
        }
    }

    private void applyCompat() {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
    }

    public void setProgressBar(ProgressBar bar) {
        auto_read_progress_bar = bar;
    }

    public Boolean isShow() {
        return isShowing();
    }


    @OnClick({R.id.tv_subtract, R.id.tv_add, R.id.iv_bg_default,
            R.id.iv_bg_1, R.id.iv_bg_2, R.id.iv_bg_3,
            R.id.iv_bg_4, /*R.id.iv_bg_5, R.id.iv_bg_6,*/
            R.id.iv_bg_7, R.id.iv_bg_8, R.id.tv_simulation,
            R.id.tv_cover, R.id.tv_slide, R.id.tv_none, R.id.tv_shangxia,
            R.id.margin_big, R.id.margin_medium, R.id.margin_small,
            R.id.auto_read_layout, R.id.tv_jianfan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_jianfan:

                mSettingListener.changeTypeFace(null);
                break;
            case R.id.tv_subtract:
                subtractFontSize();
                break;
            case R.id.tv_add:
                addFontSize();
                break;

            case R.id.iv_bg_default:
                set_seclet(config.getBookBgType(),false);
                set_seclet(0,true);
                setBookBg(ReadingConfig.BOOK_BG_DEFAULT);
                selectBg(ReadingConfig.BOOK_BG_DEFAULT);

                break;
            case R.id.iv_bg_1:
                set_seclet(config.getBookBgType(),false);
                set_seclet(1,true);
                setBookBg(ReadingConfig.BOOK_BG_1);
                selectBg(ReadingConfig.BOOK_BG_1);
                break;
            case R.id.iv_bg_2:
                set_seclet(config.getBookBgType(),false);
                set_seclet(2,true);
                setBookBg(ReadingConfig.BOOK_BG_2);
                selectBg(ReadingConfig.BOOK_BG_2);
                break;
            case R.id.iv_bg_3:
                set_seclet(config.getBookBgType(),false);
                set_seclet(3,true);
                setBookBg(ReadingConfig.BOOK_BG_3);
                selectBg(ReadingConfig.BOOK_BG_3);
                break;
            case R.id.iv_bg_4:
                set_seclet(config.getBookBgType(),false);
                set_seclet(4,true);
                setBookBg(ReadingConfig.BOOK_BG_4);
                selectBg(ReadingConfig.BOOK_BG_4);
                break;
            case R.id.iv_bg_7:
                set_seclet(config.getBookBgType(),false);
                set_seclet(7,true);
                setBookBg(ReadingConfig.BOOK_BG_7);
                selectBg(ReadingConfig.BOOK_BG_7);
                break;
            case R.id.iv_bg_8:
                set_seclet(config.getBookBgType(),false);
                set_seclet(8,true);
                setBookBg(ReadingConfig.BOOK_BG_8);
                selectBg(ReadingConfig.BOOK_BG_8);
                break;
            case R.id.tv_simulation:

                selectPageMode(ReadingConfig.PAGE_MODE_SIMULATION);
                setPageMode(ReadingConfig.PAGE_MODE_SIMULATION);
                MyToash.Log("openBook", ReadingConfig.getInstance().getPageMode() + "");
                if (scroll) {
                    pageFactory.openBook(3, pageFactory.chapterItem, null);
                    scroll = false;
                }
                break;
            case R.id.tv_cover:

                selectPageMode(ReadingConfig.PAGE_MODE_COVER);
                setPageMode(ReadingConfig.PAGE_MODE_COVER);
                MyToash.Log("openBook", ReadingConfig.getInstance().getPageMode() + "");

                if (scroll) {
                    pageFactory.openBook(3, pageFactory.chapterItem, null);
                    scroll = false;
                }

                break;
            case R.id.tv_slide:

                selectPageMode(ReadingConfig.PAGE_MODE_SLIDE);
                setPageMode(ReadingConfig.PAGE_MODE_SLIDE);
                MyToash.Log("openBook", ReadingConfig.getInstance().getPageMode() + "");
                if (scroll) {
                    pageFactory.openBook(3, pageFactory.chapterItem, null);
                    scroll = false;
                }

                break;
            case R.id.tv_shangxia:


                selectPageMode(ReadingConfig.PAGE_MODE_SCROLL);
                setPageMode(ReadingConfig.PAGE_MODE_SCROLL);
                MyToash.Log("openBook", ReadingConfig.getInstance().getPageMode() + "");
                if (!scroll) {
                    pageFactory.openBook(4, pageFactory.chapterItem, null);
                    scroll = true;
                }
                break;
            case R.id.tv_none:

                selectPageMode(ReadingConfig.PAGE_MODE_NONE);
                setPageMode(ReadingConfig.PAGE_MODE_NONE);
                if (scroll) {
                    pageFactory.openBook(3, pageFactory.chapterItem, null);
                    scroll = false;
                }
                break;
            case R.id.margin_big:
                selectLineSpacing(ReadingConfig.LINE_SPACING_BIG);
                setLineSpacingMode(ReadingConfig.LINE_SPACING_BIG);
                break;
            case R.id.margin_medium:
                selectLineSpacing(ReadingConfig.LINE_SPACING_MEDIUM);
                setLineSpacingMode(ReadingConfig.LINE_SPACING_MEDIUM);
                break;
            case R.id.margin_small:
                selectLineSpacing(ReadingConfig.LINE_SPACING_SMALL);
                setLineSpacingMode(ReadingConfig.LINE_SPACING_SMALL);
                break;
            case R.id.auto_read_layout:
                if (ReadingConfig.getInstance().getPageMode() != 4) {
                    AutoProgress.getInstance().setProgressBar(auto_read_progress_bar);
                    AutoProgress.getInstance().setTime(config.getAutoSpeed() * 1000);
                    AutoProgress.getInstance().start(new AutoProgress.ProgressCallback() {
                        @Override
                        public void finish() {


                            if (ChapterManager.getInstance(mContext).hasNextChapter()) {
                                mContext.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mContext.getPageFactory().getPageWidget().next_page();

                                    }
                                });
                            } else {
                                if (!AutoProgress.getInstance().isStop()) {
                                    AutoProgress.getInstance().stop();
                                }
                            }
                        }
                    });
                    hideSystemUI();
                    hide();
                } else {
                    MyToash.ToashError(mContext, "当前模式不支持自动阅读");
                }
                break;
        }
    }

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
/*        mContext.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );*/
    }

    //设置翻页
    public void setPageMode(int pageMode) {
        config.setPageMode(pageMode);
        mContext.getBookPage().setPageMode(pageMode);
    }

    public void setLineSpacingMode(int mode) {
        config.setLineSpacingMode(mode);
        if (mSettingListener != null) {
            mSettingListener.changeLineSpacing(mode);
        }
        /////////////////////
    }

    //变大书本字体
    private void addFontSize() {
        if (currentFontSize < FONT_SIZE_MAX) {
            currentFontSize += 1;
            tv_size.setText(currentFontSize + "");
            config.setFontSize(currentFontSize);
            if (mSettingListener != null) {
                mSettingListener.changeFontSize(currentFontSize);
            }
        }
    }

    private void defaultFontSize() {
        currentFontSize = (int) getContext().getResources().getDimension(R.dimen.reading_default_text_size);
        tv_size.setText(currentFontSize + "");
        config.setFontSize(currentFontSize);
        if (mSettingListener != null) {
            mSettingListener.changeFontSize(currentFontSize);
        }
    }

    //变小书本字体
    private void subtractFontSize() {
        if (currentFontSize > FONT_SIZE_MIN) {
            currentFontSize -= 1;
            tv_size.setText(currentFontSize + "");
            config.setFontSize(currentFontSize);
            if (mSettingListener != null) {
                mSettingListener.changeFontSize(currentFontSize);
            }
        }
    }

    public void setSettingListener(SettingListener settingListener) {
        this.mSettingListener = settingListener;
    }

    public interface SettingListener {
        void changeSystemBright(Boolean isSystem, float brightness);

        void changeFontSize(int fontSize);

        void changeTypeFace(Typeface typeface);

        void changeBookBg(int type);

        void changeLineSpacing(int mode);
    }

}