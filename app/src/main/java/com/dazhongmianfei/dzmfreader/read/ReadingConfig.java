package com.dazhongmianfei.dzmfreader.read;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import com.dazhongmianfei.dzmfreader.R;

/**
 * Created by Administrator on 2016/7/18 0018.
 */
public class ReadingConfig {
    private final static String SP_NAME = "config";
    private final static String BOOK_BG_KEY = "bookbg";
    private final static String FONT_TYPE_KEY = "fonttype";
    private final static String FONT_SIZE_KEY = "fontsize";
    private final static String AUTO_SPEED = "autospeed";
    private final static String NIGHT_KEY = "night";
    private final static String LIGHT_KEY = "light";
    private final static String SYSTEM_LIGHT_KEY = "systemlight";
    private final static String PAGE_MODE_KEY = "pagemode";
    private final static String LINE_SPACING_KEY = "linespacingmode";

    public final static int BOOK_BG_DEFAULT = 0;
    public final static int BOOK_BG_1 = 1;
    public final static int BOOK_BG_2 = 2;
    public final static int BOOK_BG_3 = 3;
    public final static int BOOK_BG_4 = 4;
    public final static int BOOK_BG_5 = 5;
    public final static int BOOK_BG_6 = 6;
    public final static int BOOK_BG_7 = 7;
    public final static int BOOK_BG_8 = 8;


    public final static int PAGE_MODE_SIMULATION = 0;
    public final static int PAGE_MODE_COVER = 1;
    public final static int PAGE_MODE_SLIDE = 2;
    public final static int PAGE_MODE_NONE = 3;
    public final static int PAGE_MODE_SCROLL = 4;

    public final static int LINE_SPACING_SMALL = 1;
    public final static int LINE_SPACING_MEDIUM = 2;
    public final static int LINE_SPACING_BIG = 3;
   // public  static boolean IS_SCROLL;

    private Context mContext;
    private static ReadingConfig config;
    private SharedPreferences sp;
    //字体
    private Typeface typeface;
    //字体大小
    private float mFontSize = 0;
    //亮度值
    private float light = 0;
    //自动阅读的速度
    private int autoSpeed;
    private int bookBG;

    private ReadingConfig(Context mContext) {
        this.mContext = mContext.getApplicationContext();
        sp = this.mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized ReadingConfig getInstance() {
        return config;
    }

    public static synchronized ReadingConfig createConfig(Context context) {
        if (config == null) {
            config = new ReadingConfig(context);
        }

        return config;
    }

    public int getPageMode() {
        return sp.getInt(PAGE_MODE_KEY, PAGE_MODE_SIMULATION);
    }

    public void setPageMode(int pageMode) {
        sp.edit().putInt(PAGE_MODE_KEY, pageMode).commit();
    }

    public int getLineSpacingMode() {
        return sp.getInt(LINE_SPACING_KEY, LINE_SPACING_MEDIUM);
    }

    public void setLineSpacingMode(int mode) {
        sp.edit().putInt(LINE_SPACING_KEY, mode).commit();
    }

    public int getBookBgType() {
        return sp.getInt(BOOK_BG_KEY, BOOK_BG_DEFAULT);
    }

    public void setBookBg(int type) {
        sp.edit().putInt(BOOK_BG_KEY, type).commit();
    }

    public Typeface getTypeface() {
        return Typeface.DEFAULT;
    }

    public float getFontSize() {
        if (mFontSize == 0) {
            mFontSize = sp.getFloat(FONT_SIZE_KEY, mContext.getResources().getDimension(R.dimen.reading_default_text_size));
        }
        return mFontSize;
    }

    public void setFontSize(float fontSize) {
        mFontSize = fontSize;
        sp.edit().putFloat(FONT_SIZE_KEY, fontSize).commit();
    }

    public int getAutoSpeed() {
        autoSpeed = sp.getInt(AUTO_SPEED, 15);
        return autoSpeed;
    }

    public void setAutoSpeed(int autoSpeed) {
        this.autoSpeed = autoSpeed;
        sp.edit().putInt(AUTO_SPEED, autoSpeed).commit();
    }

    /**
     * 获取夜间还是白天阅读模式,true为夜晚，false为白天
     */
    public boolean getDayOrNight() {
        return sp.getBoolean(NIGHT_KEY, false);
    }

    public void setDayOrNight(boolean isNight) {
        sp.edit().putBoolean(NIGHT_KEY, isNight).commit();
    }

    public Boolean isSystemLight() {
        return sp.getBoolean(SYSTEM_LIGHT_KEY, true);
    }

    public void setSystemLight(Boolean isSystemLight) {
        sp.edit().putBoolean(SYSTEM_LIGHT_KEY, isSystemLight).commit();
    }

    public float getLight() {
        if (light == 0) {
            light = sp.getFloat(LIGHT_KEY, 0.1f);
        }
        return light;
    }

    /**
     * 记录配置文件中亮度值
     */
    public void setLight(float light) {
        this.light = light;
        sp.edit().putFloat(LIGHT_KEY, light).commit();
    }
}
