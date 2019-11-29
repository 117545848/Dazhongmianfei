package com.dazhongmianfei.dzmfreader.view;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by scb on 2018/11/20.
 */

public class NoLeftRightViewPager extends ViewPager {
    public NoLeftRightViewPager(Context context) {
        super(context);
    }

    public NoLeftRightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

}
