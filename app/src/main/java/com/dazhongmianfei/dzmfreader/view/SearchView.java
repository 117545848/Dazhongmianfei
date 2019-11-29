package com.dazhongmianfei.dzmfreader.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 放大镜图标
 * Created by scb on 2018/7/9.
 */
public class SearchView extends View {
    Paint mPaint;
    int mWidth;
    int mHeight;

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);

    }


    public void setSearchColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float mCenterPointX = mWidth / 2;
        float mCenterPointY = mHeight / 2;
        float radius = mWidth / 5.0f;
        canvas.drawCircle(mCenterPointX, mCenterPointY, radius, mPaint);
        //获取和圆环的交点
        float x1 = (float) (mCenterPointX + radius * Math.cos(45 * Math.PI / 180));
        float y1 = (float) (mCenterPointY + radius * Math.sin(45 * Math.PI / 180));
        canvas.drawLine(x1, y1, x1 + radius / 2, y1 + radius / 2, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }
}
