package com.dazhongmianfei.dzmfreader.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 顺时针旋转45度的textview
 * Created by scb on 2018/8/12.
 */
public class RotateTextView extends TextView {
    public RotateTextView(Context context) {
        super(context);
    }

    public RotateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotateTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //倾斜度45,上下左右居中
        canvas.rotate(45, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        canvas.translate(0, -getMeasuredHeight() / 4);
        super.onDraw(canvas);
    }
}
