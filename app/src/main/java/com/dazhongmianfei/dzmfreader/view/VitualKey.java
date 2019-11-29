package com.dazhongmianfei.dzmfreader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by scb on 2018/9/6.
 */

public class VitualKey extends RelativeLayout {

    private onLayoutKeyChange mLayoutKeyChange;

    public VitualKey(Context context) {
        super(context);
    }

    public VitualKey(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VitualKey(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * @param changed 布局发生改变 ture 没有改变False
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //Log.e("onLayout", "onLayout: b "+b);
        if (changed)
            mLayoutKeyChange.onLayoutKeyChange(b);
    }


    public void setonLayoutKeyChange (onLayoutKeyChange layoutKeyChange) {
        mLayoutKeyChange = layoutKeyChange;
    }


    public interface onLayoutKeyChange {
        /**
         * 虚拟键盘状态监听
         *
         * @param b 布局距离底部的布局
         */
        void onLayoutKeyChange(int b);
    }

}