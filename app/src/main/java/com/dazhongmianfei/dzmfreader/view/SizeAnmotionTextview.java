package com.dazhongmianfei.dzmfreader.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import java.util.logging.LogRecord;

public class SizeAnmotionTextview extends TextView {

    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;
    private Paint mPaint;
    private int mViewWidth = 0;
    private int mTranslate = 0;

    private boolean mAnimating = true;

    public SizeAnmotionTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setTextSize(float size) {
        // float oldSize = getTextSize();
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        // super.setTextSize(size);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


        }
    };
}
