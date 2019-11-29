package com.dazhongmianfei.dzmfreader.view;

import android.content.Context;

import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;

public class MScrollView  extends NestedScrollView {
    public MScrollView(Context context) {
        super(context);
    }

    public MScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    /*    MyToash.Log("onOverScrolled",scrollX+"  "+ scrollY+"  "+ clampedX+"   "+ clampedY);
        if (onScrollChangedListener != null) {
            if (scrollY == 0) {
                onScrollChangedListener.top();
            } else if (clampedY) {
                onScrollChangedListener.bottom();
            }
        }*/
    }


    public OnScrollChangedListener onScrollChangedListener;

    public void setonScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        this.onScrollChangedListener=onScrollChangedListener;
    }

    public interface OnScrollChangedListener {
        void top();

        void bottom();

    }
}
