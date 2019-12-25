package com.dazhongmianfei.dzmfreader.read.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.activity.RechargeActivity;
import com.google.gson.Gson;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.activity.WebViewActivity;
import com.dazhongmianfei.dzmfreader.bean.BaseAd;
import com.dazhongmianfei.dzmfreader.bean.ChapterContent;
import com.dazhongmianfei.dzmfreader.bean.ChapterItem;
import com.dazhongmianfei.dzmfreader.book.been.BaseBook;
import com.dazhongmianfei.dzmfreader.book.been.ReadHistory;
import com.dazhongmianfei.dzmfreader.config.MainHttpTask;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshTopbook;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.jinritoutiao.TodayOneAD;
import com.dazhongmianfei.dzmfreader.read.ReadActivity;
import com.dazhongmianfei.dzmfreader.read.ReadingConfig;
import com.dazhongmianfei.dzmfreader.read.manager.ChapterManager;
import com.dazhongmianfei.dzmfreader.read.view.PageWidget;
import com.dazhongmianfei.dzmfreader.utils.FileManager;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyPicasso;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.NotchScreen;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;
import com.dazhongmianfei.dzmfreader.utils.ViewToBitmapUtil;
import com.dazhongmianfei.dzmfreader.view.BorderTextView;
import com.dazhongmianfei.dzmfreader.view.MScrollView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.READBUTTOM_HEIGHT;

public class PageFactory {
    //&&(chapterItem.getChapter_uid() == null || !chapterItem.getChapter_uid().contains(uid))
    public static boolean close_AD;//关闭广告
    public static boolean IS_VIP;//关闭广告
    int AD_PAGE;//
    // private static PageFactory pageFactory;


    private ReadingConfig config;
    //当前的书本
//    private File book_file = null;
    // 默认背景颜色
    // private int m_backColor = 0xffff9e85;
    //章节名称字体大小是正文字体大小的多少倍
    private static final float RATIO = 1.02f;
    private static final int OFFSET = 2;
    //页面宽
    private int mWidth;


    private int PercentWidth;
    //页面高
    private int mHeight, MHeight;
    //文字字体大小
    private float m_fontSize;
    //时间格式
    private SimpleDateFormat sdf;
    //时间
    private String date;
    //进度格式
    private DecimalFormat df;
    //电池边界宽度
    private float mBorderWidth;
    // 上下与边缘的距离
    private float marginHeight;
    //书名距离顶部的高度
    private float BookNameTop;
    // 左右与边缘的距离
    private float measureMarginWidth;
    // 左右与边缘的距离
    private float marginWidth;
    //状态栏距离底部高度
    private float statusMarginBottom;
    //行间距
    private float lineSpace;
    //段间距
    // private float paragraphSpace;
    //字高度
    // private float fontHeight;
    //字体
    private Typeface typeface;
    //文字画笔
    private Paint mPaint;
    //加载画笔
    private Paint waitPaint;
    //文字颜色
    private int m_textColor = Color.rgb(50, 65, 78);
    // 绘制内容的宽
    private float mVisibleHeight;
    // 绘制内容的宽
    private float mVisibleWidth;
    // 每页可以显示的行数
    private int mLineCount;
    //电池画笔
    private Paint mBatteryPaint;
    //章节名称画笔 粗体字 字号较文章偏大
    private Paint mChapterPaint;
    //画书名
    // private Paint mBookNamePaint;
    //背景图片
    private Bitmap m_book_bg = null;
    private Intent batteryInfoIntent;
    //电池电量百分比
    private float mBatteryPercentage;
    //电池外边框
    private RectF rect1 = new RectF();
    //电池内边框
    private RectF rect2 = new RectF();
    //文件编码
//    private String m_strCharsetName = "GBK";
    //当前是否为第一页
    private boolean m_isfirstPage;
    //当前是否为最后一页
    private boolean m_islastPage;
    //书本widget
    private PageWidget mBookPageWidget;
    //书本名字
    private String bookName = "";
    //章节名字
    private String chapterTitle;
    public ChapterItem chapterItem;
    //当前电量
    private int level = 0;
    private BookUtil mBookUtil;
    private PageEvent mPageEvent;

    //下一章节的第一页
    //  private TRPage nextChapterFirstPage;
    //上一章节的最后一页
    // private TRPage lastChapterLastPage;

    private TRPage currentPage;
    private TRPage prePage;
    private TRPage cancelPage;
    // private BookTaskWithCallback bookTaskWithCallback;
    ContentValues values = new ContentValues();

    private String book_id;
    private String chapter_id;
    //是否是预览章节，1 预览章节  2 非预览章节
    private String mIsPreview;
    private static Status mStatus = Status.OPENING;

    private LinearLayout mPurchaseLayout;
    private BorderTextView mPurchaseOne;
    private BorderTextView mPurchaseSome;
    private TextView mSupport;
    private View activity_read_line_left, activity_read_line_right;


    private boolean hasNotchScreen;
    private float tendp, chapterRight;
    int Chapter_height;
    float reading_shangxia_textsize;
    Resources resources;

    public enum Status {
        OPENING,
        FINISH,
        FAIL,
    }

    private Activity mActivity;
    private PurchaseDialog mPurchaseDialog;
    //  Map<String, BookUtil> bookUtilList = new HashMap<>();

    //Map<String,TRPage> trPageMap= new HashMap<>();


    FrameLayout insert_todayone2;

    BaseBook baseBook;
    TodayOneAD todayOneAD;

    FrameLayout insert_todayone_button;
    TodayOneAD todayOneAD_buttom;
    int button_ad_heigth, bg_color, color;

    public void setTodayOneAD(TodayOneAD todayOneAD_buttom, FrameLayout insert_todayone_button) {
        this.insert_todayone_button = insert_todayone_button;
        this.todayOneAD_buttom = todayOneAD_buttom;
        if ( todayOneAD_buttom != null && todayOneAD_buttom.adViewHolder != null) {
            insert_todayone_button.setBackgroundColor(bg_color);
            todayOneAD_buttom.adViewHolder.mDescription.setTextColor(color);
            todayOneAD_buttom.adViewHolder.mTitle.setTextColor(color);
            todayOneAD_buttom.adViewHolder.mSource.setTextColor(color);
           // todayOneAD_buttom.adViewHolder.mCreativeButton.setTextColor(color);
        }
    }

    int isNotchEnable;
    float dianchi;
    float huadong;
    public PageFactory(BaseBook baseBook, Context context, int isNotchEnable, int NavigationBarHeight
            , FrameLayout frameLayout) {
        mActivity = (Activity) context;
        insert_todayone2 = frameLayout;
        this.isNotchEnable = isNotchEnable;

        this.baseBook = baseBook;
        mPurchaseDialog = new PurchaseDialog(context, false);
        mBookUtil = new BookUtil();
        config = ReadingConfig.getInstance();
        resources = context.getResources();
        //获取屏幕宽高 @dimen/public_read_buttom_ad_hegth
        MHeight = ScreenSizeUtils.getInstance(mActivity).getScreenHeight();
        MHeight -= NavigationBarHeight;

        mHeight = MHeight;
        button_ad_heigth = ImageUtil.dp2px(mActivity, READBUTTOM_HEIGHT);
        mHeight -= button_ad_heigth;

        mWidth = ScreenSizeUtils.getInstance(mActivity).getScreenWidth();
        sdf = new SimpleDateFormat("HH:mm");//HH:mm为24小时制,hh:mm为12小时制
        date = sdf.format(new java.util.Date());
        df = new DecimalFormat("#0.0");

        marginWidth = resources.getDimension(R.dimen.readingMarginWidth);
        lineSpace = resources.getDimension(R.dimen.reading_line_spacing_medium);
        if (hasNotchScreen = ScreenSizeUtils.isAllScreenDevice(mActivity)) {
            statusMarginBottom = ImageUtil.dp2px(mActivity, 15);
            marginHeight = ImageUtil.dp2px(mActivity, 35);
        } else {
            statusMarginBottom = ImageUtil.dp2px(mActivity, 10);
            marginHeight = ImageUtil.dp2px(mActivity, 30);
            BookNameTop = ImageUtil.dp2px(mActivity, 15);
        }

        reading_shangxia_textsize = 0;
        chapterRight = ImageUtil.dp2px(mActivity, 15);
        //  MyToash.Log("mHeightmHeight", mHeight + " " + mWidth);

        mVisibleWidth = mWidth - marginWidth * 2;
        mVisibleHeight = mHeight - marginHeight * 2;

        typeface = config.getTypeface();
        m_fontSize = config.getFontSize();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
        mPaint.setTextAlign(Paint.Align.LEFT);// 左对齐
        mPaint.setTextSize(m_fontSize);// 字体大小
        mPaint.setColor(m_textColor);// 字体颜色
        mPaint.setTypeface(typeface);
        mPaint.setSubpixelText(true);// 设置该项为true，将有助于文本在LCD屏幕上的显示效果

        waitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
        waitPaint.setTextAlign(Paint.Align.LEFT);// 左对齐
        waitPaint.setTextSize(resources.getDimension(R.dimen.reading_max_text_size));// 字体大小
        waitPaint.setColor(m_textColor);// 字体颜色
        waitPaint.setTypeface(typeface);
        waitPaint.setSubpixelText(true);// 设置该项为true，将有助于文本在LCD屏幕上的显示效果
        mBorderWidth = resources.getDimension(R.dimen.reading_board_battery_border_width);
        mBatteryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBatteryPaint.setTextSize(CommonUtil.sp2px(context, 12));


        mBatteryPaint.setTypeface(typeface);
        mBatteryPaint.setTextAlign(Paint.Align.LEFT);
        mBatteryPaint.setColor(m_textColor);
        PercentWidth = (int) mBatteryPaint.measureText("999.9%") + 1;
        mChapterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mChapterPaint.setTextSize(RATIO * m_fontSize);
        mChapterPaint.setFakeBoldText(true);
        mChapterPaint.setTextAlign(Paint.Align.LEFT);
        mChapterPaint.setColor(m_textColor);


        batteryInfoIntent = context.getApplicationContext().registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));//注册广播,随时获取到电池电量信息

        initBg(config.getDayOrNight());
        measureMarginWidth();
       // top = (MHeight - Insert_todayone2) / 2;
         huadong= mHeight - statusMarginBottom-ImageUtil.dp2px(mActivity,40);

        top=(int) marginHeight;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) insert_todayone2.getLayoutParams();
        layoutParams.topMargin = top;
        layoutParams.height= (int) (huadong-top)-ImageUtil.dp2px(mActivity,130);
        Insert_todayone2 = layoutParams.height;
        insert_todayone2.setLayoutParams(layoutParams);



    }


    private void measureMarginWidth() {


        float wordWidth = mPaint.measureText("\u3000");
        float width = mVisibleWidth % wordWidth;
        measureMarginWidth = marginWidth + width / 2;
    }


    private void calculateLineCount() {
        try {
            if (currentPage == null || currentPage.getBegin() == 0) {
                calculateLineCount1();
            } else {
                calculateLineCount2();
            }
        } catch (Exception e) {
            calculateLineCount1();
        }
    }

    private void calculateLineCount1() {
        mLineCount = (int) ((mVisibleHeight - OFFSET * marginHeight) / (m_fontSize + lineSpace));// 可显示的行数
    }

    private void calculateLineCount2() {
        mLineCount = (int) (mVisibleHeight / (m_fontSize + lineSpace));// 可显示的行数
    }

    public void onDraw(Bitmap bitmap, List<String> m_lines, Boolean updateChapter) {
        try {
            //  IS_CHAPTERLast = false;

            Canvas c = new Canvas(bitmap);
            try {
                c.drawBitmap(getBgBitmap(), 0, 0, null);
            } catch (Exception e) {
                c.drawBitmap(getBgBitmap2(), 0, 0, null);
            }

//        word.setLength(0);
            mPaint.setTextSize(getFontSize());
            mPaint.setColor(getTextColor());

            mBatteryPaint.setColor(getTextColor());
            mChapterPaint.setColor(getTextColor());
            mChapterPaint.setTextSize(RATIO * getFontSize());
            mPaint.setColor(getTextColor());
            float y = 0;
            float y0;
            if (isNotchEnable == 0) {
                y0 = marginHeight / 2;
            } else {
                y0 = marginHeight;
            }


            if (!m_lines.isEmpty()) {
                if (currentPage.getBegin() == 0) {
                    String line_1 = m_lines.get(0);
                    m_lines.set(0, "       " + line_1);
                    y = (OFFSET + 1) * y0 - Chapter_height;
                } else {
                    y = y0;
                }
                for (String strLine : m_lines) {
                    y += m_fontSize + lineSpace;
                    c.drawText(changeJIanfan(strLine), measureMarginWidth, y, mPaint);
                }
            }

            float fPercent = (float) (currentPage.getBegin() * 1.0 / mBookUtil.getBookLen());//进度
            if (mPageEvent != null) {
                mPageEvent.changeProgress(fPercent);
            }
            float myfPercent = (float) ((mBookUtil.getBookLen() * (chapterItem.getDisplay_order()) + currentPage.getBegin()) * 1.0 / (mBookUtil.getBookLen() * ChapterManager.getInstance(mActivity).mChapterList.size()));//进度

            // 画白分例
            String strPercent = df.format(myfPercent * 100);//进度文字

            c.drawText(strPercent + "%", mWidth - PercentWidth, mHeight - statusMarginBottom, mBatteryPaint);//x y为坐标值

            baseBook.setAllPercent(strPercent);

            if (baseBook.isAddBookSelf() == 1) {
                ContentValues values = new ContentValues();
                values.put("allPercent", strPercent);
                LitePal.updateAll(BaseBook.class, values, "book_id = ?", book_id);
                EventBus.getDefault().post(new RefreshTopbook(book_id, strPercent));
            }
            if (currentPage != null && chapterItem != null) {
                ChapterManager.getInstance(mActivity).getCurrentChapter().setChapteritem_begin(currentPage.getBegin());
                ContentValues values1 = new ContentValues();
                values1.put("chapteritem_begin", currentPage.getBegin());
                LitePal.updateAll(ChapterItem.class, values1, "chapter_id = ?", chapter_id);
            }
            // 画时间
            drawBatteryAndDate(c);
            //画书名
            //c.drawText(chapterTitle, marginWidth, statusMarginBottom + BookNameTop + tendp, mBatteryPaint);

            if (currentPage.getBegin() == 0) {
                mChapterPaint.setTextSize(RATIO * getFontSize());
                for (int i = 100; i > 0; i--) {

                    int With = (int) (mChapterPaint.measureText(chapterTitle));
                    if (With < mWidth - chapterRight) {
                        break;
                    }
                    float s = (float) i / 100;
                    mChapterPaint.setTextSize((s * getFontSize()));
                }
                c.drawText(chapterTitle, marginWidth, 2 * marginHeight + Chapter_height, mChapterPaint);
            }
            //更新购买view的文字颜色
            mSupport(mIsPreview);
            mBookPageWidget.postInvalidate();
        } catch (Exception e) {
        }
    }

    private void mSupport(String mIsPreview) {
        if (mIsPreview.equals("1")) {
            activity_read_line_left.setBackgroundColor(mPaint.getColor());
            activity_read_line_right.setBackgroundColor(mPaint.getColor());
            mSupport.setTextColor(mPaint.getColor());
            mPurchaseOne.setBorder(mPaint.getColor(), new int[]{2, 2, 2, 2}, new int[]{6, 6, 6, 6});
            mPurchaseSome.setBorder(mPaint.getColor(), new int[]{2, 2, 2, 2}, new int[]{6, 6, 6, 6});
        }
    }

    private void mSupport2(int color) {

    }

    public void setPurchaseLayout(LinearLayout view) {

        mPurchaseLayout = view;
        mPurchaseOne = mPurchaseLayout.findViewById(R.id.activity_read_purchase_one);
        mPurchaseSome = mPurchaseLayout.findViewById(R.id.activity_read_purchase_some);
        mSupport = mPurchaseLayout.findViewById(R.id.activity_read_support);
        activity_read_line_left = mPurchaseLayout.findViewById(R.id.activity_read_line_left);
        activity_read_line_right = mPurchaseLayout.findViewById(R.id.activity_read_line_right);
        mPurchaseOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseSingleChapter(book_id, chapter_id, 1);
            }
        });

        mPurchaseSome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPurchaseDialog.initData(book_id, chapter_id, null, null);
                mPurchaseDialog.show();

            }
        });

        RelativeLayout.LayoutParams activity_read_purchase_l = (RelativeLayout.LayoutParams) mPurchaseLayout.getLayoutParams();
        activity_read_purchase_l.topMargin = (int) (mHeight - statusMarginBottom) - ImageUtil.dp2px(mActivity, 200);
        mPurchaseLayout.setLayoutParams(activity_read_purchase_l);

    }

    /**
     * @param bitmap
     * @param m_lines
     * @param updateChapter
     */
    public void onDraw1(String mIsPreview, Bitmap bitmap, String chapterTitle, List<String> m_lines, Boolean updateChapter) {
        try {

            //更新数据库进度
            if (currentPage != null && chapterItem != null) {
                ContentValues values = new ContentValues();
                values.put("chapteritem_begin", currentPage.getBegin());
                ChapterManager.getInstance(mActivity).getCurrentChapter().setChapteritem_begin(currentPage.getBegin());
                LitePal.updateAll(ChapterItem.class, values, "chapter_id = ?", chapter_id);
            }

            Canvas c = new Canvas(bitmap);
            try {
                c.drawBitmap(getBgBitmap(), 0, 0, null);
            } catch (Exception e) {
                c.drawBitmap(getBgBitmap2(), 0, 0, null);
            }
//        word.setLength(0);
            mPaint.setTextSize(getFontSize());
            mPaint.setColor(getTextColor());

            mBatteryPaint.setColor(getTextColor());
            mChapterPaint.setColor(getTextColor());
            mChapterPaint.setTextSize(RATIO * getFontSize());
            mPaint.setColor(getTextColor());
            if (m_lines.isEmpty()) {
                return;
            }
            String line_1 = m_lines.get(0);
            m_lines.set(0, "       " + line_1);
            float y0;
            if (isNotchEnable == 0) {
                y0 = marginHeight / 2;
            } else {
                y0 = marginHeight;
            }

            float y = (OFFSET + 1) * y0 - Chapter_height;
            for (String strLine : m_lines) {
                y += m_fontSize + lineSpace;
                c.drawText(strLine, measureMarginWidth, y, mPaint);
            }


            //画进度及时间

            float fPercent = (float) (currentPage.getBegin() * 1.0 / mBookUtil.getBookLen());//进度
            if (mPageEvent != null) {
                mPageEvent.changeProgress(fPercent);
            }
            float myfPercent = (float) ((mBookUtil.getBookLen() * (chapterItem.getDisplay_order()) + currentPage.getBegin()) * 1.0 / (mBookUtil.getBookLen() * ChapterManager.getInstance(mActivity).mChapterList.size()));//进度

            // 画白分例
            String strPercent = df.format(myfPercent * 100) + "%";//进度文字
            c.drawText(strPercent, mWidth - PercentWidth, mHeight - statusMarginBottom, mBatteryPaint);//x y为坐标值

            baseBook.setAllPercent(strPercent);
            if (baseBook.isAddBookSelf() == 1) {
                ContentValues values = new ContentValues();
                values.put("allPercent", strPercent);
                LitePal.updateAll(BaseBook.class, values, "book_id = ?", book_id);
                EventBus.getDefault().post(new RefreshTopbook(book_id, strPercent));
            }

            drawBatteryAndDate(c);

            //画书名
            //c.drawText(chapterTitle, marginWidth, statusMarginBottom + BookNameTop + tendp, mBatteryPaint);
            //画章
            mChapterPaint.setTextSize(RATIO * getFontSize());
            for (int i = 100; i > 0; i--) {

                int With = (int) (mChapterPaint.measureText(chapterTitle));
                if (With < mWidth - chapterRight) {
                    break;
                }
                float s = (float) i / 100;
                mChapterPaint.setTextSize((s * getFontSize()));
            }
            c.drawText(chapterTitle, marginWidth, 2 * marginHeight + Chapter_height, mChapterPaint);

            //更新购买view的文字颜色
            mSupport(mIsPreview);
            mBookPageWidget.postInvalidate();

        } catch (Exception e) {
        }
    }

    String HUADONG = "点击或滑动可继续阅读";
    int HUADONG_text_WIDTH;
    String AD_text = "广告是为了更多的免费内容";
    int AD_text_WIDTH;


    private void drawBatteryAndDate(Canvas c) {
        if (c == null) {
            return;
        }
        if (c == null) {
            return;
        }
        int dateWith = (int) (mBatteryPaint.measureText(date) + mBorderWidth);//时间宽度
        c.drawText(date, marginWidth, mHeight - statusMarginBottom, mBatteryPaint);
        // 画电池
        level = batteryInfoIntent.getIntExtra("level", 0);
        int scale = batteryInfoIntent.getIntExtra("scale", 100);
        mBatteryPercentage = (float) level / scale;
        float rect1Left = marginWidth + dateWith + statusMarginBottom;//电池外框left位置
        //画电池外框
        float width = CommonUtil.convertDpToPixel(mActivity, 20) - mBorderWidth;
        float height = CommonUtil.convertDpToPixel(mActivity, 10);
        rect1.set(rect1Left, mHeight - height - statusMarginBottom, rect1Left + width, mHeight - statusMarginBottom);
        rect2.set(rect1Left + mBorderWidth, mHeight - height + mBorderWidth - statusMarginBottom, rect1Left + width - mBorderWidth, mHeight - mBorderWidth - statusMarginBottom);
        c.save();
        c.clipRect(rect2, Region.Op.DIFFERENCE);
        c.drawRect(rect1, mBatteryPaint);
        c.restore();
        //画电量部分
        rect2.left += mBorderWidth;
        rect2.right -= mBorderWidth;
        rect2.right = rect2.left + rect2.width() * mBatteryPercentage;
        rect2.top += mBorderWidth;
        rect2.bottom -= mBorderWidth;
        c.drawRect(rect2, mBatteryPaint);
        //画电池头
        int poleHeight = (int) CommonUtil.convertDpToPixel(mActivity, 10) / 2;
        rect2.left = rect1.right;
        rect2.top = rect2.top + poleHeight / 4;
        rect2.right = rect1.right + mBorderWidth;
        rect2.bottom = rect2.bottom - poleHeight / 4;
        c.drawRect(rect2, mBatteryPaint);

        if (AD_text_WIDTH == 0) {
            AD_text_WIDTH = (int) (mBatteryPaint.measureText(AD_text));//;
        }
        c.drawText(AD_text, (mWidth - AD_text_WIDTH) / 2, mHeight - statusMarginBottom, mBatteryPaint);


    }

    /**
     * @param bitmap
     * @param m_lines
     * @param updateChapter
     */

    public void onDraw2(String mIsPreview, Bitmap bitmap, String chapterTitle, List<String> m_lines, String process, Boolean updateChapter) {
        try {
            if (updateChapter) {
                onDraw1(mIsPreview, bitmap, chapterTitle, m_lines, true);
                return;
            }
            if (currentPage != null && chapterItem != null) {
                ContentValues values = new ContentValues();
                values.put("chapteritem_begin", currentPage.getBegin());
                ChapterManager.getInstance(mActivity).getCurrentChapter().setChapteritem_begin(currentPage.getBegin());
                LitePal.updateAll(ChapterItem.class, values, "chapter_id = ?", chapter_id);
            }


            Canvas c = new Canvas(bitmap);
            try {
                c.drawBitmap(getBgBitmap(), 0, 0, null);
            } catch (Exception e) {
                c.drawBitmap(getBgBitmap2(), 0, 0, null);
            }
//        word.setLength(0);
            mPaint.setTextSize(getFontSize());
            mPaint.setColor(getTextColor());

            mBatteryPaint.setColor(getTextColor());
            mChapterPaint.setColor(getTextColor());
            mChapterPaint.setTextSize(RATIO * getFontSize());
            mPaint.setColor(getTextColor());

            if (m_lines.size() == 0) {
                return;
            }
            float y = 0;

            if (isNotchEnable == 0) {
                y = marginHeight / 2;
            } else {
                y = marginHeight;
            }
            for (String strLine : m_lines) {
                y += m_fontSize + lineSpace;
                c.drawText(strLine, measureMarginWidth, y, mPaint);

            }


            //画进度及时间

            float fPercent = (float) (currentPage.getBegin() * 1.0 / mBookUtil.getBookLen());//进度
            if (mPageEvent != null) {
                mPageEvent.changeProgress(fPercent);
            }
            //String strPercent = process;//进度文字
            //Paint.measureText直接返回參數字串所佔用的寬度

            float myfPercent = (float) ((mBookUtil.getBookLen() * (chapterItem.getDisplay_order()) + currentPage.getBegin()) * 1.0 / (mBookUtil.getBookLen() * ChapterManager.getInstance(mActivity).mChapterList.size()));//进度

            // 画白分例
            String strPercent = df.format(myfPercent * 100) + "%";//进度文字
            c.drawText(strPercent, mWidth - PercentWidth, mHeight - statusMarginBottom, mBatteryPaint);//x y为坐标值

            baseBook.setAllPercent(strPercent);
            if (baseBook.isAddBookSelf() == 1) {
                ContentValues values = new ContentValues();
                values.put("allPercent", strPercent);
                LitePal.updateAll(BaseBook.class, values, "book_id = ?", book_id);
                EventBus.getDefault().post(new RefreshTopbook(book_id, strPercent));
            }
            drawBatteryAndDate(c);
            //画书名
            // c.drawText(chapterTitle, marginWidth, statusMarginBottom + BookNameTop + tendp, mBatteryPaint);
            //更新购买view的文字颜色
            mSupport(mIsPreview);
            mBookPageWidget.postInvalidate();
        } catch (Exception e) {
        }
    }


    //向前翻页
    public void prePage() {

/*
        if (ReaderConfig. !IS_VIP&&!close_AD && IS_CHAPTERFirst && mBookPageWidget.Current_Page > 5 && mBookPageWidget.Current_Page % 5 == 0) {
            cancelPage = currentPage;
            onDraw(mBookPageWidget.getCurPage(), currentPage.getLines(), true);
            //    currentPage = getPrePage();
            drawAD(mBookPageWidget.getNextPage());


            mBookPageWidget.setOnSwitchPreListener(new PageWidget.OnSwitchPreListener() {
                @Override
                public void switchPreChapter() {


                    mBookPageWidget.removeView(insert_todayone2);

                    getWebViewAD();
                    mBookPageWidget.addView(insert_todayone2);

                    IS_CHAPTERFirst = false;
                    mPurchaseLayout.setVisibility(View.GONE);
                }

            });
            return;
        }*/


        boolean last_chapter = currentPage.getBegin() <= 0;
        if (last_chapter) {
            if (!ChapterManager.getInstance(mActivity).hasPreChapter()) {
                if (!m_isfirstPage) {

                    MyToash.ToashError(mActivity, LanguageUtil.getString(mActivity, R.string.ReadActivity_startpage));
                }
                m_isfirstPage = true;
                return;
            }


            try {
                ChapterItem chapterItem = ChapterManager.getInstance(mActivity).mCurrentChapter;
                final String preChapterId = chapterItem.getPre_chapter_id();
                ChapterManager.getInstance(mActivity).getChapter(chapterItem.getDisplay_order() - 1, preChapterId, new ChapterManager.QuerychapterItemInterface() {
                    @Override
                    public void success(final ChapterItem querychapterItem) {
                        if (querychapterItem.getChapter_path() == null) {
                            String path = FileManager.getSDCardRoot().concat("Reader/book/").concat(book_id + "/").concat(preChapterId + "/").concat(querychapterItem.getIs_preview() + "/").concat(querychapterItem.getUpdate_time()).concat(".txt");
                            if (FileManager.isExist(path)) {
                                ContentValues values = new ContentValues();
                                values.put("chapter_path", path);
                                LitePal.updateAll(ChapterItem.class, values, "chapter_id = ?", preChapterId);
                                querychapterItem.setChapter_path(path);
                            } else {

                                ChapterManager.notfindChapter(querychapterItem, book_id, preChapterId, new ChapterManager.ChapterDownload() {
                                    @Override
                                    public void finish() {
                                        drawLastChapter(querychapterItem, preChapterId);
                                    }
                                });

                                return;
                            }
                        }

                        drawLastChapter(querychapterItem, preChapterId);
                    }

                    @Override
                    public void fail() {
                        MyToash.ToashError(mActivity, LanguageUtil.getString(mActivity, R.string.ReadActivity_chapterfail));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                m_isfirstPage = true;
            }

        } else {

         /*   m_isfirstPage = false;
            cancelPage = currentPage;
            onDraw(mBookPageWidget.getCurPage(), currentPage.getLines(), true);
            currentPage = getPrePage();
            onDraw(mBookPageWidget.getNextPage(), currentPage.getLines(), true);
*/


            MyToash.Log("IS_CHAPTERLastTT", IS_CHAPTERLast + "");
            if (IS_CHAPTERFirst) {
                m_isfirstPage = false;
                cancelPage = currentPage;
                onDraw(mBookPageWidget.getCurPage(), currentPage.getLines(), true);
                currentPage = getPrePage();
                onDraw(mBookPageWidget.getNextPage(), currentPage.getLines(), true);


            } else {
                IS_CHAPTERFirst = true;
                drawAD(mBookPageWidget.getCurPage());
                //   currentPage = getPrePage();
                onDraw(mBookPageWidget.getNextPage(), currentPage.getLines(), true);


            }

        }
    }


    //向后翻页
    public void nextPage() {
        MyToash.Log("onTouchEvent", "333");

        if (!IS_VIP&&!close_AD && IS_CHAPTERLast && mBookPageWidget.Current_Page > 5 && mBookPageWidget.Current_Page % 5 == 0) {
            cancelPage = currentPage;
            onDraw(mBookPageWidget.getCurPage(), currentPage.getLines(), true);
            prePage = currentPage;
            drawAD(mBookPageWidget.getNextPage());
            mBookPageWidget.setOnSwitchNextListener(new PageWidget.OnSwitchNextListener() {
                @Override
                public void switchNextChapter() {
                    insert_todayone2.setVisibility(View.VISIBLE);
                    getWebViewAD();
                    Log.i("nativeRender", "---cccc");
                    IS_CHAPTERLast = false;
                    mPurchaseLayout.setVisibility(View.GONE);
                }
            });
            return;
        }/*else {
            if (!IS_VIP && !close_AD && (mBookPageWidget.Current_Page+1) % 5 == 0) {
                getWebViewAD();
            }
        }*/

        boolean nextChapter = currentPage.getEnd() >= mBookUtil.getBookLen();

        if (currentPage == null) {
            return;

        }
        if (nextChapter) {//开启新章节
            if (!m_islastPage) {
            }
            if (!ChapterManager.getInstance(mActivity).hasNextChapter()) {
                if (!m_islastPage) {

                    MyToash.ToashError(mActivity, LanguageUtil.getString(mActivity, R.string.ReadActivity_endpage));
                }
                m_islastPage = true;
                return;
            }
            if (false) {

            } else {
                try {

                    final String nextChapterId = mBookUtil.getCurrentChapter().getNext_chapter_id();
                    ChapterManager.getInstance(mActivity).getChapter(mBookUtil.getCurrentChapter().getDisplay_order() + 1, nextChapterId, new ChapterManager.QuerychapterItemInterface() {
                        @Override
                        public void success(final ChapterItem querychapterItem) {
                            if (querychapterItem.getChapter_path() == null) {
                                String path = FileManager.getSDCardRoot().concat("Reader/book/").concat(book_id + "/").concat(nextChapterId + "/").concat(querychapterItem.getIs_preview() + "/").concat(querychapterItem.getUpdate_time()).concat(".txt");
                                if (FileManager.isExist(path)) {
                                    ContentValues values = new ContentValues();
                                    values.put("chapter_path", path);
                                    LitePal.updateAll(ChapterItem.class, values, "chapter_id = ?", nextChapterId);
                                    querychapterItem.setChapter_path(path);
                                } else {
                                    ChapterManager.notfindChapter(querychapterItem, book_id, nextChapterId, new ChapterManager.ChapterDownload() {
                                        @Override
                                        public void finish() {
                                            drawNextChapter(querychapterItem, nextChapterId);
                                        }
                                    });

                                    return;


                                }
                            }

                            drawNextChapter(querychapterItem, nextChapterId);

                            //  Next_Flag=true;
                        }

                        @Override
                        public void fail() {
                            MyToash.ToashError(mActivity, LanguageUtil.getString(mActivity, R.string.ReadActivity_chapterfail));
                        }
                    });

                } catch (Exception e) {
                    ChapterManager.getInstance(mActivity).addDownloadTaskWithoutAutoBuy(mBookUtil.getCurrentChapter(), new ChapterManager.ChapterDownload() {
                        @Override
                        public void finish() {
                            ChapterManager.getInstance(mActivity).mCurrentChapter = mBookUtil.getCurrentChapter();
                            ReadActivity.openBook(baseBook, ChapterManager.getInstance(mActivity).mCurrentChapter, mActivity);
                        }
                    });

                }
            }

        } else {
            if (IS_CHAPTERLast) {
                m_islastPage = false;
                cancelPage = currentPage;

                onDraw(mBookPageWidget.getCurPage(), currentPage.getLines(), true);
                prePage = currentPage;

                mLineCount = (int) (mVisibleHeight / (m_fontSize + lineSpace));
                currentPage = getNextPage();
                onDraw(mBookPageWidget.getNextPage(), currentPage.getLines(), true);

            } else {
                IS_CHAPTERLast = true;
                drawAD(mBookPageWidget.getCurPage());
                currentPage = getNextPage();
                onDraw(mBookPageWidget.getNextPage(), currentPage.getLines(), true);
            }


        }


    }

    public void openBook(int isfirst, ChapterItem chapterItem, BookUtil bookUtil) {
        // calculateLineCount();
        if (isfirst == 0 || isfirst == 3) {
            initBg(config.getDayOrNight());
        }

        this.chapterItem = chapterItem;
        ;
        bookName = chapterItem.getBook_name();
        chapterTitle = chapterItem.getChapter_title();
        book_id = chapterItem.getBook_id();
        chapter_id = chapterItem.getChapter_id();
        mIsPreview = chapterItem.getIs_preview();


        baseBook.setCurrent_chapter_displayOrder(chapterItem.getDisplay_order());
        baseBook.setCurrent_chapter_id(chapter_id);

        ContentValues values = new ContentValues();
        values.put("current_chapter_id", chapter_id);
        values.put("current_chapter_displayOrder", chapterItem.getDisplay_order());
        LitePal.updateAll(BaseBook.class, values, "book_id = ?", book_id);

        if (baseBook.isAddBookSelf() == 1) {
            EventBus.getDefault().post(new RefreshTopbook(book_id, chapter_id, true));
        }
        if (isfirst != 4) {
            mBookPageWidget.setVisibility(View.VISIBLE);
            mStatus = Status.OPENING;
            if (bookUtil == null) {
                try {
                    mBookUtil.openBook(mActivity, chapterItem, book_id, chapter_id);
                    if (mIsPreview.equals("1")) {
                        mPurchaseLayout.setVisibility(View.VISIBLE);
                        mActivity.startActivity(new Intent(mActivity, RechargeActivity.class));

                    } else if (mIsPreview.equals("0")) {
                        mPurchaseLayout.setVisibility(View.GONE);
                    }
                    PageFactory.mStatus = PageFactory.Status.FINISH;
                    if (chapterItem.getBegin() == 0) {
                        calculateLineCount1();
                    } else {
                        calculateLineCount2();
                    }

                    currentPage = getPageForBegin(chapterItem.getBegin());
                    onDraw(mBookPageWidget.getNextPage(), currentPage.getLines(), true);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

                mBookUtil = bookUtil;
                if (mIsPreview.equals("1")) {
                    mPurchaseLayout.setVisibility(View.VISIBLE);

                    mActivity.startActivity(new Intent(mActivity, RechargeActivity.class));


                } else if (mIsPreview.equals("0")) {
                    mPurchaseLayout.setVisibility(View.GONE);
                }

                PageFactory.mStatus = PageFactory.Status.FINISH;
//                m_mbBufLen = mBookUtil.getBookLen();
                currentPage = getPageForBegin(chapterItem.getBegin());
            }
        } else {
            //drawScroll();

        }

        ReadHistory.addReadHistory(true, mActivity, book_id, chapter_id);//阅读历史上传 没看一个新章节都上传一次
    }

    /**
     * 单章购买
     */
    public void purchaseSingleChapter(final String book_id, final String chapter_id, int num) {
        if (!MainHttpTask.getInstance().Gotologin(mActivity)) {
            return;
        }
        ;
        ReaderParams params = new ReaderParams(mActivity);
        params.putExtraParams("book_id", book_id);
        params.putExtraParams("chapter_id", chapter_id);
        params.putExtraParams("num", num + "");
        String json = params.generateParamsJson();

        HttpUtils.getInstance(mActivity).sendRequestRequestParams3(ReaderConfig.mChapterBuy, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {

                        ReaderConfig.REFREASH_USERCENTER = true;
                        downloadWithoutAutoBuy(book_id, chapter_id);
                        ReaderConfig.integerList.add(1);

                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );


    }

    public void cancelPage() {
        currentPage = cancelPage;
        if (!IS_VIP&&!close_AD) {
            if (IS_CHAPTERLast && IS_CHAPTERFirst) {
                insert_todayone2.setVisibility(View.INVISIBLE);
            } else {
                insert_todayone2.setVisibility(View.VISIBLE);
            }
        }
    }

    public void downloadWithoutAutoBuy(final String book_id, final String chapter_id) {
        ReaderParams params = new ReaderParams(mActivity);

        params.putExtraParams("book_id", book_id);
        params.putExtraParams("chapter_id", chapter_id);
        params.putExtraParams("num", "1");
        String json = params.generateParamsJson();
        HttpUtils.getInstance(mActivity).sendRequestRequestParams3(ReaderConfig.mChapterDownUrl, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        Log.i("mChapterDownUrl", result);
                        try {

                            MyToash.ToashSuccess(mActivity, LanguageUtil.getString(mActivity, R.string.ReadActivity_buysuccess));
                            JSONArray jsonArray = new JSONArray(result);

                            ChapterContent chapterContent = new Gson().fromJson(jsonArray.getString(0), ChapterContent.class);
                            chapterItem.setIs_preview(chapterContent.getIs_preview());
                            chapterItem.setUpdate_time(chapterContent.getUpdate_time());
                            ContentValues values = new ContentValues();
                            String filepath = FileManager.getSDCardRoot().concat("Reader/book/").concat(book_id + "/").concat(chapter_id + "/").concat(chapterItem.getIs_preview() + "/").concat(chapterItem.getUpdate_time()).concat(".txt");
                            FileManager.createFile(filepath, chapterContent.getContent().getBytes());
                            values.put("chapteritem_begin", 0);
                            values.put("chapter_path", filepath);
                            values.put("is_preview", "0");
                            values.put("update_time", chapterContent.getUpdate_time());
                            ChapterManager.getInstance(mActivity).getCurrentChapter().setIs_preview("0");
                            ChapterManager.getInstance(mActivity).getCurrentChapter().setChapter_path(filepath);
                            ChapterManager.getInstance(mActivity).getCurrentChapter().setUpdate_time(chapterContent.getUpdate_time());

                            LitePal.updateAll(ChapterItem.class, values, "chapter_id = ?", chapter_id);

                            if (config.getPageMode() != 4) {
                                ChapterManager.getInstance(mActivity).openCurrentChapter(chapter_id);
                            } else {
                                openBook(4, ChapterManager.getInstance(mActivity).getCurrentChapter(), null);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );

    }

    public TRPage getNextPage() {
        mBookUtil.setPostition(currentPage.getEnd());
        long position = currentPage.getEnd() + 1;
        TRPage trPage = new TRPage();
        trPage.setBegin(currentPage.getEnd() + 1);
        trPage.setLines(getNextLines());
        trPage.setEnd(mBookUtil.getPosition());
        return trPage;
    }

    public TRPage getPrePage() {
        mBookUtil.setPostition(currentPage.getBegin());
        TRPage trPage = new TRPage();
        trPage.setEnd(mBookUtil.getPosition() - 1);
        trPage.setLines(getPreLines());
        trPage.setBegin(mBookUtil.getPosition());
        return trPage;
    }

    public TRPage getPageForBegin(long begin) {
        TRPage trPage = new TRPage();
        trPage.setBegin(begin);
        mBookUtil.setPostition(begin - 1);
        trPage.setLines(getNextLines());
        trPage.setEnd(mBookUtil.getPosition());
        // trPageMap.put(chapter_id,trPage);
        return trPage;
    }


    public List<String> getNextLines() {

        List<String> lines = new ArrayList<>();
        float width = 0;
        float height = 0;
        String line = "";
        while (mBookUtil.next(true) != -1) {
            char word = (char) mBookUtil.next(false);
            //判断是否换行
            if ((word + "").equals("\r") && (((char) mBookUtil.next(true)) + "").equals("\n")) {

                mBookUtil.next(false);
                if (!line.isEmpty()) {
                    lines.add(line);
                    line = "";
                    width = 0;
//                    height +=  paragraphSpace;
                    if (lines.size() == mLineCount) {
                        break;
                    }
                }

            } else {
                float widthChar = mPaint.measureText(word + "");
                width += widthChar;
                if (width > mVisibleWidth) {
                    width = widthChar;
                    lines.add(line);
                    line = word + "";
                } else {
                    line += word;
                }
            }

            if (lines.size() == mLineCount) {
                if (!line.isEmpty()) {
                    mBookUtil.setPostition(mBookUtil.getPosition() - 1);
                }
                break;
            }
        }

        if (!line.isEmpty() && lines.size() < mLineCount) {
            lines.add(line);
        }

        return lines;
    }

    public List<String> getPreLines() {
        List<String> lines = new ArrayList<>();
        float width = 0;
        String line = "";

        char[] par = mBookUtil.preLine();
        while (par != null) {
            List<String> preLines = new ArrayList<>();
            for (int i = 0; i < par.length; i++) {
                char word = par[i];
                float widthChar = mPaint.measureText(word + "");
                width += widthChar;
                if (width > mVisibleWidth) {
                    width = widthChar;
                    preLines.add(line);
                    line = word + "";
                } else {
                    line += word;

                }
            }
            if (!line.isEmpty()) {
                preLines.add(line);
            }

            lines.addAll(0, preLines);

            if (lines.size() >= mLineCount) {
                break;
            }
            width = 0;
            line = "";
            par = mBookUtil.preLine();
        }
        List<String> reLines = new ArrayList<>();
        int num = 0;
        for (int i = lines.size() - 1; i >= 0; i--) {
            if (reLines.size() < mLineCount) {
                reLines.add(0, lines.get(i));
            } else {
                num = num + lines.get(i).length();
            }

        }

        if (num > 0) {
            if (mBookUtil.getPosition() > 0) {
                mBookUtil.setPostition(mBookUtil.getPosition() + num + 2);
            } else {
                mBookUtil.setPostition(mBookUtil.getPosition() + num);
            }
        }

        return reLines;
    }

    //绘制当前页面
    public void currentPage(Boolean updateChapter) {
        if (close_AD && (!IS_CHAPTERFirst || !IS_CHAPTERLast)) {
            //  drawAD(mBookPageWidget.getNextPage());
        } else {
            onDraw(mBookPageWidget.getNextPage(), currentPage.getLines(), updateChapter);
        }
    }

    //更新电量
    public void updateBattery(int mLevel) {
        if (config.getPageMode() != 4) {
            if (currentPage != null && mBookPageWidget != null && !mBookPageWidget.isRunning()) {
                if (level != mLevel) {
                    level = mLevel;
                    if(insert_todayone2.getVisibility()!=View.VISIBLE) {
                        currentPage(false);
                    }
                    //drawBatteryAndDate(onDrawCanvas);
                }
            }
        }
    }

    public void updateTime() {
        if (config.getPageMode() != 4) {
            if (currentPage != null && mBookPageWidget != null && !mBookPageWidget.isRunning()) {
                String mDate = sdf.format(new java.util.Date());
                if (date != mDate) {
                    date = mDate;
                    if(insert_todayone2.getVisibility()!=View.VISIBLE) {
                        currentPage(false);
                    }
                    // drawBatteryAndDate(onDrawCanvas);

                }
            }
        }
    }

    //改变字体大小
    public void changeFontSize(int fontSize) {
        this.m_fontSize = fontSize;
        mPaint.setTextSize(m_fontSize);
        if (config.getPageMode() != 4) {
            calculateLineCount1();
            measureMarginWidth();
            currentPage = getPageForBegin(0);
            currentPage(true);


        }
    }

    //改变文字间距
    public void changeLineSpacing(int mode) {
        if (mode == ReadingConfig.LINE_SPACING_BIG) {
            lineSpace = resources.getDimension(R.dimen.reading_line_spacing_big);
        } else if (mode == ReadingConfig.LINE_SPACING_MEDIUM) {
            lineSpace = resources.getDimension(R.dimen.reading_line_spacing_medium);
        } else if (mode == ReadingConfig.LINE_SPACING_SMALL) {
            lineSpace = resources.getDimension(R.dimen.reading_line_spacing_small);
        }
        if (config.getPageMode() != 4) {
            calculateLineCount1();
            measureMarginWidth();
            currentPage = getPageForBegin(0);
            currentPage(true);
        }
    }

    public void setLineSpacingMode(int mode) {
        if (mode == ReadingConfig.LINE_SPACING_BIG) {
            lineSpace = resources.getDimension(R.dimen.reading_line_spacing_big);
        } else if (mode == ReadingConfig.LINE_SPACING_MEDIUM) {
            lineSpace = resources.getDimension(R.dimen.reading_line_spacing_medium);
        } else if (mode == ReadingConfig.LINE_SPACING_SMALL) {
            lineSpace = resources.getDimension(R.dimen.reading_line_spacing_small);
        }
    }

    public void setFontSize(int fontSize) {
        this.m_fontSize = fontSize;
    }

    boolean jianfan;
    // ZHConverter zhConverter, zhConverterS;

    public String changeJIanfan(String s) {
    /*    if (!jianfan) {
            if (zhConverter == null) {
                zhConverter = ZHConverter.getInstance(ZHConverter.TRADITIONAL);
            }
           return zhConverter.convert(s);
        }*/
        return s;
    }

    //改变字体
    public void changeTypeface(Typeface typeface) {
        jianfan = !jianfan;
        calculateLineCount1();
        measureMarginWidth();
        currentPage = getPageForBegin(0);
        currentPage(true);

/*        this.typeface = typeface;
        mPaint.setTypeface(typeface);
        mBatteryPaint.setTypeface(typeface);
        calculateLineCount();
        measureMarginWidth();
        currentPage = getPageForBegin(currentPage.getBegin());
        currentPage(true);*/
    }

    //改变背景
    public void changeBookBg(int type) {
        if (config.getPageMode() != 4) {
            setBookBg(type);
            currentPage(false);
        }
    }

    //初始化背景
    private void initBg(Boolean isNight) {

        if (!isNight) {
            setBookBg(config.getBookBgType());
        } else {
            setBookBg(ReadingConfig.BOOK_BG_8);
        }
    }

    //设置页面的背景
    public void setBookBg(int type) {
        try {
            Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            int color = 0, bg_color = 0;
            switch (type) {
                case ReadingConfig.BOOK_BG_DEFAULT:
                    //canvas.drawColor(resources.getColor(R.color.read_bg_default));
                    bg_color = resources.getColor(R.color.read_bg_default);
                    color = resources.getColor(R.color.read_font_1);

                    // setBookPageBg(resources.getColor(R.color.read_bg_default));
                    //insert_todayone2.setBackgroundColor(resources.getColor(R.color.read_bg_default));

                    break;
                case ReadingConfig.BOOK_BG_1:
                    bg_color = resources.getColor(R.color.read_bg_1);
                    color = resources.getColor(R.color.read_font_1);
                    //  setBookPageBg(resources.getColor(R.color.read_bg_1));
                    //   insert_todayone2.setBackgroundColor(resources.getColor(R.color.read_bg_1));

                    break;
                case ReadingConfig.BOOK_BG_2:
                    bg_color = resources.getColor(R.color.read_bg_2);
                    color = resources.getColor(R.color.read_font_2);
                    //  setBookPageBg(resources.getColor(R.color.read_bg_2));
                    // insert_todayone2.setBackgroundColor(resources.getColor(R.color.read_bg_2));

                    break;
                case ReadingConfig.BOOK_BG_3:
                    bg_color = resources.getColor(R.color.read_bg_3);
                    color = resources.getColor(R.color.read_font_3);
                    //  setBookPageBg(resources.getColor(R.color.read_bg_3));
                    //  insert_todayone2.setBackgroundColor(resources.getColor(R.color.read_bg_3));
                    break;
                case ReadingConfig.BOOK_BG_4:
                    bg_color = resources.getColor(R.color.read_bg_4);
                    color = resources.getColor(R.color.read_font_4);
                    // setBookPageBg(resources.getColor(R.color.read_bg_4));
                    // insert_todayone2.setBackgroundColor(resources.getColor(R.color.read_bg_4));
                    break;
                case ReadingConfig.BOOK_BG_7:
                    bg_color = resources.getColor(R.color.read_bg_7);
                    color = resources.getColor(R.color.read_font_7);
                    // setBookPageBg(resources.getColor(R.color.read_bg_7));
                    // insert_todayone2.setBackgroundColor(resources.getColor(R.color.read_bg_7));
                    break;
                case ReadingConfig.BOOK_BG_8:
                    bg_color = resources.getColor(R.color.read_bg_8);
                    color = resources.getColor(R.color.read_font_8);
                    // setBookPageBg(resources.getColor(R.color.read_bg_8));
                    // insert_todayone2.setBackgroundColor(resources.getColor(R.color.read_bg_8));
                    break;
            }
            canvas.drawColor(bg_color);
            setBookPageBg(bg_color);
            this.bg_color = bg_color;
            this.color = color;
            //    try {
            //insert_todayone2.setBackgroundColor(bg_color);

            try {
                if (todayOneAD_buttom != null && !todayOneAD_buttom.is_getNativeInfoListView) {
                    insert_todayone_button.setBackgroundColor(bg_color);
                    todayOneAD_buttom.adViewHolder.mDescription.setTextColor(color);
                    todayOneAD_buttom.adViewHolder.mTitle.setTextColor(color);
                    todayOneAD_buttom.adViewHolder.mSource.setTextColor(color);
                   // todayOneAD_buttom.adViewHolder.mCreativeButton.setTextColor(color);
                }
            } catch (Exception E) {
            }
          /*  } catch (Exception e) {
            }*/
            setBgBitmap(bitmap);
            //设置字体颜色
            setM_textColor(color);
        } catch (Error e) {

        }
    }

    public void setBookPageBg(int color) {
        if (mBookPageWidget != null) {
            mBookPageWidget.setBgColor(color);
        }
    }

    //设置日间或者夜间模式
    public void setDayOrNight(Boolean isNgiht) {
        /*if (isNgiht) {
            config.setBookBg(ReadingConfig.BOOK_BG_8);
        } else {
            config.setBookBg(ReadingConfig.BOOK_BG_4);
        }*/

        initBg(isNgiht);

        if (config.getPageMode() != 4) {
            currentPage(false);
        } else {
            changeBookBg(config.getBookBgType());
        }
    }

    public void clear() {
        try {
            if (m_book_bg != null && !m_book_bg.isRecycled()) {
                m_book_bg.recycle();      //scaleBitmap 回收
                m_book_bg = null;
            }
        } catch (Exception e) {
        }
        try {
            if (mBookPageWidget.getCurPage() != null && !mBookPageWidget.getCurPage().isRecycled()) {
                mBookPageWidget.getCurPage().recycle();      //scaleBitmap 回收

            }
        } catch (Exception e) {
        }
        try {
            if (mBookPageWidget.getNextPage() != null && !mBookPageWidget.getNextPage().isRecycled()) {
                mBookPageWidget.getNextPage().recycle();      //scaleBitmap 回收
            }
        } catch (Exception e) {
        }
        bookName = "";
        chapterItem = null;
        mBookPageWidget = null;
        mPageEvent = null;
        cancelPage = null;
        prePage = null;
        currentPage = null;

    }

    public float getmVisibleWidth() {
        return mVisibleWidth;
    }

    public int getmLineCount() {
        return mLineCount;
    }

    public int getmLineCount1() {
        return (int) ((mVisibleHeight - OFFSET * marginHeight) / (m_fontSize + lineSpace));// 可显示的行数
    }

    public int getmLineCount2() {
        return (int) ((mVisibleHeight) / (m_fontSize + lineSpace));// 可显示的行数
    }

    public Paint getmPaint() {
        return mPaint;
    }

    public static Status getStatus() {
        return mStatus;
    }

    //是否是第一页
    public boolean isfirstPage() {
        return m_isfirstPage;
    }

    //是否是最后一页
    public boolean islastPage() {
        return m_islastPage;
    }

    //设置页面背景
    public void setBgBitmap(Bitmap BG) {
        m_book_bg = BG;
    }

    //设置页面背景
    public Bitmap getBgBitmap() {
        return m_book_bg;
    }

    public Bitmap getBgBitmap2() {
        initBg(config.getDayOrNight());
        return m_book_bg;
    }

    //设置文字颜色
    public void setM_textColor(int m_textColor) {
        this.m_textColor = m_textColor;
    }

    //获取文字颜色
    public int getTextColor() {
        return this.m_textColor;
    }

    //获取文字大小
    public float getFontSize() {
        return this.m_fontSize;
    }

    public float getChapterFontSize() {
        return this.m_fontSize * RATIO;
    }

    public void setPageWidget(PageWidget mBookPageWidget) {
        this.mBookPageWidget = mBookPageWidget;

    }

    public PageWidget getPageWidget() {
        return mBookPageWidget;
    }

    public void setPageEvent(PageEvent pageEvent) {
        this.mPageEvent = pageEvent;
    }

    public interface PageEvent {
        void changeProgress(float progress);
    }

    long aapo = 0;

    private void drawLastChapter(ChapterItem querychapterItem, String preChapterId) {
        final ChapterItem preChapter = querychapterItem;
        BookUtil bookUtiltemp = getBookUtil(querychapterItem, preChapterId);
        final BookUtil bookUtil = bookUtiltemp;
        TRPage page = new TRPage();
        if (preChapter.getIs_preview().equals("0")) {
            for (int i = 1; (bookUtil.getPosition() < bookUtil.getBookLen() - 2); i++) {
                aapo = bookUtil.getPosition();
                bookUtil.getNextLines1(PageFactory.this, i);
            }
            long lastPageStartPosition = aapo + 1;
            MyToash.Log("lastPageStartPosition", lastPageStartPosition + "");


            bookUtil.setPostition(lastPageStartPosition - 1);

            calculateLineCount2();

            page.setLines(bookUtil.getNextLines(PageFactory.this));
            page.setEnd(bookUtil.getPosition());

       /* if (preChapter.getIs_preview().equals("1")) {
            ContentValues values2 = new ContentValues();
            values.put("chapteritem_begin", 0);
            LitePal.updateAll(ChapterItem.class, values2, "chapter_id = ?", preChapter.getChapter_id());
            preChapter.setChapteritem_begin(0);
            preChapter.setBegin(0);
            page.setBegin(0);

        } else {
            preChapter.setBegin(lastPageStartPosition);
            page.setBegin(lastPageStartPosition);
        }*/
            preChapter.setBegin(lastPageStartPosition);
            page.setBegin(lastPageStartPosition);
        } else {
            bookUtil.setPostition(-1);
            page.setBegin(0);
            calculateLineCount1();
            page.setLines(bookUtil.getNextLines(PageFactory.this));
            page.setEnd(bookUtil.getBookLen());

        }

        mBookPageWidget.setOnSwitchPreListener(new PageWidget.OnSwitchPreListener() {
            @Override
            public void switchPreChapter() {
                try {
                    //打开上一章节之前，更新当前章节的begin为0
                    ContentValues values = new ContentValues();
                    values.put("chapteritem_begin", 0);
                    LitePal.updateAll(ChapterItem.class, values, "chapter_id = ?", chapter_id);
                    chapterItem.setChapteritem_begin(0);
                    openBook(1, preChapter, bookUtil);
                    IS_CHAPTERFirst = true;
                    ChapterManager.getInstance(mActivity).setCurrentChapter(preChapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if ( !IS_VIP&&!close_AD & !IS_CHAPTERFirst) {
            drawAD(mBookPageWidget.getCurPage());
        } else
            onDraw(mBookPageWidget.getCurPage(), currentPage.getLines(), true);

        float fPercent = (float) (page.getBegin() * 1.0 / bookUtil.getBookLen());//进度
        String strPercent = df.format(fPercent * 100) + "%";//进度文字
        if (preChapter.getIs_preview().equals("1")) {
            onDraw2("1", mBookPageWidget.getNextPage(), preChapter.getChapter_title(), page.getLines(), strPercent, true);
        } else {
            onDraw2("", mBookPageWidget.getNextPage(), preChapter.getChapter_title(), page.getLines(), strPercent, false);
        }
        if (AD_SHOW) {
            CANCLE_AD_SHOW = true;
        } else {
            CANCLE_AD_SHOW = false;
        }
        m_isfirstPage = false;
        ChapterManager.getInstance(mActivity).addDownloadTask(true, preChapter.getPre_chapter_id(), new ChapterManager.ChapterDownload() {
            @Override
            public void finish() {

            }
        });
    }

    private BookUtil getBookUtil(ChapterItem querychapterItem, String preChapterId) {

        BookUtil bookUtiltemp = null;//&& !querychapterItem.getIs_preview().equals("1")
        bookUtiltemp = new BookUtil();
        try {
            bookUtiltemp.openBook(mActivity, querychapterItem, book_id, preChapterId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookUtiltemp;
    }


    private void drawNextChapter(ChapterItem querychapterItem, String nextChapterId) {
        final ChapterItem nextChapter = querychapterItem;
        BookUtil bookUtiltemp = getBookUtil(nextChapter, nextChapterId);

        final BookUtil bookUtil = bookUtiltemp;

        TRPage page = new TRPage();
        bookUtil.setPostition(-1);
        page.setBegin(0);
        calculateLineCount1();
        page.setLines(bookUtil.getNextLines(PageFactory.this));
        page.setEnd(bookUtil.getPosition());


        mBookPageWidget.setOnSwitchNextListener(new PageWidget.OnSwitchNextListener() {
            @Override
            public void switchNextChapter() {
                //打开下一章节之前，更新当前章节的begin为0
                ContentValues values = new ContentValues();
                values.put("chapteritem_begin", 0);
                LitePal.updateAll(ChapterItem.class, values, "chapter_id = ?", nextChapter.getChapter_id());
                nextChapter.setChapteritem_begin(0);
                openBook(2, nextChapter, bookUtil);
                ChapterManager.getInstance(mActivity).setCurrentChapter(nextChapter);
                IS_CHAPTERLast = true;
            }
        });
        if ( !IS_VIP&&!close_AD && !IS_CHAPTERLast) {
            drawAD(mBookPageWidget.getCurPage());
        } else
            onDraw(mBookPageWidget.getCurPage(), currentPage.getLines(), true);


        onDraw1(nextChapter.getIs_preview(), mBookPageWidget.getNextPage(), nextChapter.getChapter_title(), page.getLines(), true);
        m_islastPage = false;
        ChapterManager.getInstance(mActivity).addDownloadTask(true, nextChapter.getNext_chapter_id(), new ChapterManager.ChapterDownload() {
            @Override
            public void finish() {

            }
        });
    }

    // BaseAd baseAd;
    // ImageView list_ad_view_img;

    //加载webview 广告
    public void getWebViewAD() {

        if (todayOneAD == null) {
            todayOneAD = new TodayOneAD(mActivity, 0, "POSID2oxtf4pvfp2y");
        }
        todayOneAD.getTodayOneBanner(insert_todayone2, null, 0);
    }

    public boolean IS_CHAPTERLast = true;
    public boolean IS_CHAPTERFirst = true;

    private int Insert_todayone2;
    private boolean AD_SHOW, CANCLE_AD_SHOW;
    int top;

    private void drawAD(Bitmap bitmap) {
        Canvas c = new Canvas(bitmap);
        try {
            c.drawBitmap(getBgBitmap(), 0, 0, null);
        } catch (Exception e) {
            c.drawBitmap(getBgBitmap2(), 0, 0, null);
        }

    /*    Bitmap bitmapAD = ViewToBitmapUtil.convertViewToBitmap(insert_todayone2, top, Insert_todayone2);
        if(bitmapAD!=null&&bitmapAD.getHeight()!=0) {
            Rect mSrcRect = new Rect(0, 0, mWidth, Insert_todayone2);
            Paint mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBitPaint.setFilterBitmap(true);
            mBitPaint.setDither(true);
            Rect mDestRect = new Rect(0, top, mWidth, top + Insert_todayone2);
            c.drawBitmap(bitmapAD, mSrcRect, mDestRect, mBitPaint);
        }*/
        //画进度及时间
        float fPercent = (float) (currentPage.getBegin() * 1.0 / mBookUtil.getBookLen());//进度

        if (mPageEvent != null) {
            mPageEvent.changeProgress(fPercent);
        }
        float myfPercent = (float) ((mBookUtil.getBookLen() * (chapterItem.getDisplay_order()) + currentPage.getBegin()) * 1.0 / (mBookUtil.getBookLen() * ChapterManager.getInstance(mActivity).mChapterList.size()));//进度

        // 画白分例
        String strPercent = df.format(myfPercent * 100) + "%";//进度文字
        c.drawText(strPercent, mWidth - PercentWidth, mHeight - statusMarginBottom, mBatteryPaint);//x y为坐标值
        // 画时间
        drawBatteryAndDate(c);


        if (HUADONG_text_WIDTH == 0) {
            HUADONG_text_WIDTH = (int) (mPaint.measureText(HUADONG));//;
        }
      //  float huadong = (mHeight - statusMarginBottom - (top + Insert_todayone2)) / 2 + top + Insert_todayone2;

        c.drawText(HUADONG, (mWidth - HUADONG_text_WIDTH) / 2, huadong, mPaint);


        mBookPageWidget.postInvalidate();

    }

}
