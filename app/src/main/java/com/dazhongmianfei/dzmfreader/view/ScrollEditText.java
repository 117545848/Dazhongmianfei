package com.dazhongmianfei.dzmfreader.view;

import android.annotation.SuppressLint;

import android.app.AlertDialog;

import android.content.Context;

import android.content.DialogInterface;

import android.text.method.ScrollingMovementMethod;

import android.util.AttributeSet;

import android.view.MotionEvent;

import android.view.View;

import android.widget.EditText;

import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.utils.MyToash;


/**
 * Created by scb on 2019/4/3.
 */

@SuppressLint("AppCompatCustomView")

public class ScrollEditText extends TextView {

    private static final String TAG = "ScrollEditText";

    private boolean pressFlag = false;//判断手指是否按着屏幕，如果是就不需要滚动了。


    public ScrollEditText(Context context) {

        super(context);

    }


    public ScrollEditText(Context context, AttributeSet attrs) {

        super(context, attrs);

    }


    public ScrollEditText(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

    }


    @Override

    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {

            postDelayed(runnable, 2000);

        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {

            pressFlag = true;

        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            pressFlag = true;

            removeCallbacks(runnable);

        }


        return super.dispatchTouchEvent(event);

    }


    private Runnable runnable = new Runnable() {

        @Override

        public void run() {

            pressFlag = false;

        }

    };


    @Override

    protected void onFinishInflate() {

        super.onFinishInflate();

        setOnLongClickListener(new OnLongClickListener() {

            @Override

            public boolean onLongClick(View v) {

                showDialog();

                return false;

            }

        });

        setMovementMethod(ScrollingMovementMethod.getInstance());

    }

/*    @Override
    protected void onScrollChanged(int horiz, int vert, int oldHoriz, int oldVert) {
        super.onScrollChanged(horiz, vert, oldHoriz, oldVert);
        if (onScrollChangedListener != null) {
            if (getScrollY() == 0) {
                onScrollChangedListener.top();
            } else {
                onScrollChangedListener.bottom();
            }
        }
    }*/

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        MyToash.Log("onOverScrolled",scrollX+"  "+ scrollY+"  "+ clampedX+"   "+ clampedY);
        if (onScrollChangedListener != null) {
            if (scrollX == 0) {
                onScrollChangedListener.top();
            } else if (clampedY) {
                onScrollChangedListener.bottom();
            }
        }
    }

    public OnScrollChangedListener onScrollChangedListener;

    public void setonScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener;
    }

    public interface OnScrollChangedListener {
        void top();

        void bottom();

    }

    public void appendStr(final CharSequence text) {

        post(new Runnable() {

            @Override

            public void run() {

                ScrollEditText.super.append(text);
                if (pressFlag) return;

                int scrollAmount = getLayout().getLineTop(getLineCount())

                        - getHeight();

                if (scrollAmount > 0)

                    scrollTo(0, scrollAmount);

                else

                    scrollTo(0, 0);

            }

        });

    }


    public void setTextStr(final CharSequence text) {

        post(new Runnable() {

            @Override

            public void run() {

                ScrollEditText.super.setText(text);

                if (pressFlag) return;

                int scrollAmount = getLayout().getLineTop(getLineCount()) - getHeight();
                if (scrollAmount > 0)

                    scrollTo(0, scrollAmount);
                else
                    scrollTo(0, 0);
            }

        });

    }


    private void scrollTop() {

        scrollTo(0, 0);

    }


    private void scrollBottom() {

        int scrollAmount = getLayout().getLineTop(getLineCount()) - getHeight();

        if (scrollAmount > 0)

            scrollTo(0, scrollAmount);

        else

            scrollTo(0, 0);

    }

    //信息内容是简单地列表项


    public void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());

        //定义复选框

        builder.setItems(new String[]{"回到顶部", "滑到底部"}, listener);

        builder.create().show();

    }


    private DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

        @Override

        public void onClick(DialogInterface dialog, int which) {

            switch (which) {

                case 0:

                    scrollTop();

                    break;

                case 1:

                    scrollBottom();

                    break;

            }

        }

    };

}
