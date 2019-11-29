package com.dazhongmianfei.dzmfreader.view;

import android.content.Context;
import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by scb on 2018/5/26. 滚动监听  没有API  等级限制
 */
public class ObservableScrollView extends NestedScrollView implements Pullable {

    public interface ScrollViewListener {


        void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);

    }

    private ScrollViewListener scrollViewListener = null;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    @Override
    public boolean canPullDown() {
        return getScrollY() == 0;
    }

    private boolean LoadingMoreEnabled;

    public void setLoadingMoreEnabled(boolean LoadingMoreEnabled) {
        this.LoadingMoreEnabled = LoadingMoreEnabled;
    }

    @Override
    public boolean canPullUp() {
        if (LoadingMoreEnabled&&getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
            return true;
        else
            return false;
    }

}