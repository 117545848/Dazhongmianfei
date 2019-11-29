package com.dazhongmianfei.dzmfreader.banner;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.PageTransformer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.banner.adapter.CBPageAdapter;
import com.dazhongmianfei.dzmfreader.banner.holder.CBViewHolderCreator;
import com.dazhongmianfei.dzmfreader.banner.listener.CBPageChangeListener;
import com.dazhongmianfei.dzmfreader.banner.listener.OnItemClickListener;
import com.dazhongmianfei.dzmfreader.banner.view.CBLoopViewPager;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.view.PullToRefreshLayout;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * banner控件，支持无限循环，自动翻页，翻页特效
 */
public class ConvenientBannerBookShelf<T> extends LinearLayout {
    private List<T> mDatas;
    private int[] page_indicatorId;
    private ArrayList<ImageView> mPointViews = new ArrayList<ImageView>();
    private CBPageChangeListener pageChangeListener;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private CBPageAdapter pageAdapter;
    private CBLoopViewPager viewPager;
    private ViewPagerScroller scroller;
    private LinearLayout loPageTurningPoint;
    private long autoTurningTime;
    private boolean turning;
    private boolean canTurn = false;
    private boolean manualPageable = true;
    private boolean canLoop = true;
    int size;

    public enum PageIndicatorAlign {
        ALIGN_PARENT_LEFT, ALIGN_PARENT_RIGHT, CENTER_HORIZONTAL
    }

    private AdSwitchTask adSwitchTask;

    public ConvenientBannerBookShelf(Context context) {
        super(context);
        init(context);
    }

    public ConvenientBannerBookShelf(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ConvenientBanner);
        canLoop = a.getBoolean(R.styleable.ConvenientBanner_canLoop, true);
        a.recycle();
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ConvenientBannerBookShelf(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ConvenientBanner);
        canLoop = a.getBoolean(R.styleable.ConvenientBanner_canLoop, true);
        a.recycle();
        init(context);
        //setPageIndicatorAlign(PageIndicatorAlign.ALIGN_PARENT_RIGHT);
    }

    //	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    //	public ConvenientBanner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    //		super(context, attrs, defStyleAttr, defStyleRes);
    //		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ConvenientBanner);
    //		canLoop = a.getBoolean(R.styleable.ConvenientBanner_canLoop, true);
    //		a.recycle();
    //		init(context);
    //	}

    private void init(Context context) {
        size = ImageUtil.dp2px(context, 12);
        View hView = LayoutInflater.from(context).inflate(R.layout.include_viewpagerbookshelf, this, true);
        viewPager = hView.findViewById(R.id.cbLoopViewPager);
        Field field = null; // 通过ViewPager类得到字段，不能通过实例得到字段。
        try {
            field = ViewPager.class.getDeclaredField("mTouchSlop");
            field.setAccessible(true); // 设置Java不检查权限。
            field.setInt(viewPager, 1); // 设置字段的值，此处应该使用ViewPager实例。设置只有滑动长度大于1px的时候，ViewPager才进行滑动

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        loPageTurningPoint = hView.findViewById(R.id.loPageTurningPoint);
        initViewPagerScroll();
        adSwitchTask = new AdSwitchTask(this);
    }

    static class AdSwitchTask implements Runnable {

        private final WeakReference<ConvenientBannerBookShelf> reference;

        AdSwitchTask(ConvenientBannerBookShelf convenientBanner) {
            this.reference = new WeakReference<ConvenientBannerBookShelf>(convenientBanner);
        }

        @Override
        public void run() {
            ConvenientBannerBookShelf convenientBanner = reference.get();

            if (convenientBanner != null) {
                if (convenientBanner.viewPager != null && convenientBanner.turning) {
                    int page = convenientBanner.viewPager.getCurrentItem() + 1;
                    convenientBanner.viewPager.setCurrentItem(page);
                    convenientBanner.postDelayed(convenientBanner.adSwitchTask, convenientBanner.autoTurningTime);
                }
            }
        }
    }

    public ConvenientBannerBookShelf setPages(CBViewHolderCreator holderCreator, List<T> datas) {
        this.mDatas = datas;
        pageAdapter = new CBPageAdapter(holderCreator, mDatas);
        viewPager.setAdapter(pageAdapter, canLoop);

        if (page_indicatorId != null)
            setPageIndicator(page_indicatorId);
        return this;
    }

    /**
     * 通知数据变化 如果只是增加数据建议使用 notifyDataSetAdd()
     */
    public void notifyDataSetChanged() {
        viewPager.getAdapter().notifyDataSetChanged();
        if (page_indicatorId != null)
            setPageIndicator(page_indicatorId);
    }

    /**
     * 设置底部指示器是否可见
     *
     * @param visible
     */
    public ConvenientBannerBookShelf setPointViewVisible(boolean visible) {
        loPageTurningPoint.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 底部指示器资源图片
     *
     * @param page_indicatorId
     */
    public ConvenientBannerBookShelf setPageIndicator(int[] page_indicatorId) {
        loPageTurningPoint.removeAllViews();
        mPointViews.clear();
        this.page_indicatorId = page_indicatorId;
        if (mDatas == null)
            return this;
        for (int count = 0; count < mDatas.size(); count++) {
            // 翻页指示的点
            ImageView pointView = new ImageView(getContext());

            if (mPointViews.isEmpty())
                pointView.setImageResource(page_indicatorId[1]);
            else
                pointView.setImageResource(page_indicatorId[0]);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams2.setMargins(size / 2, 0, 0, 0);
            layoutParams2.width = size/2;
            layoutParams2.height = size/2;
            mPointViews.add(pointView);
            loPageTurningPoint.addView(pointView, layoutParams2);
        }
        pageChangeListener = new CBPageChangeListener(mPointViews, page_indicatorId);
        viewPager.setOnPageChangeListener(pageChangeListener);
        pageChangeListener.onPageSelected(viewPager.getRealItem());
        if (onPageChangeListener != null)
            pageChangeListener.setOnPageChangeListener(onPageChangeListener);

        return this;
    }

    /**
     * 指示器的方向
     * <p>
     * 居左 （RelativeLayout.ALIGN_PARENT_LEFT），居中
     * （RelativeLayout.CENTER_HORIZONTAL），居右
     * （RelativeLayout.ALIGN_PARENT_RIGHT）
     *
     * @return
     */
    public ConvenientBannerBookShelf setPageIndicatorAlign() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) loPageTurningPoint.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        loPageTurningPoint.setLayoutParams(layoutParams);
        return this;
    }

    /***
     * 是否开启了翻页
     *
     * @return
     */
    public boolean isTurning() {
        return turning;
    }

    /***
     * 开始翻页
     *
     * @param autoTurningTime 自动翻页时间
     * @return
     */
    public ConvenientBannerBookShelf startTurning(long autoTurningTime) {
        //如果是正在翻页的话先停掉
        if (turning) {
            stopTurning();
        }
        //设置可以翻页并开启翻页
        canTurn = true;
        this.autoTurningTime = autoTurningTime;
        turning = true;
        postDelayed(adSwitchTask, autoTurningTime);
        return this;
    }

    public void stopTurning() {
        turning = false;
        removeCallbacks(adSwitchTask);
    }

    /**
     * 自定义翻页动画效果
     *
     * @param transformer
     * @return
     */
    public ConvenientBannerBookShelf setPageTransformer(PageTransformer transformer) {
        viewPager.setPageTransformer(true, transformer);
        return this;
    }

    /**
     * 设置ViewPager的滑动速度
     */
    private void initViewPagerScroll() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            scroller = new ViewPagerScroller(viewPager.getContext());
            mScroller.set(viewPager, scroller);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public boolean isManualPageable() {
        return viewPager.isCanScroll();
    }

    public void setManualPageable(boolean manualPageable) {
        viewPager.setCanScroll(manualPageable);
    }

    //触碰控件的时候，翻页应该停止，离开的时候如果之前是开启了翻页的话则重新启动翻页
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ViewParent parent = getParent();
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE) {
            PullToRefreshLayout.REFRESH_FLAG = true;
            // 开始翻页
            if (canTurn)
                startTurning(autoTurningTime);
        } else if (action == MotionEvent.ACTION_DOWN) {
            mLastMotionX = ev.getX();
            mLastMotionY = ev.getY();

            // 停止翻页
            if (canTurn)
                stopTurning();
        } else if (action == MotionEvent.ACTION_MOVE) {
            final float x = ev.getX();
            final float dx = x - mLastMotionX;
            final float xDiff = Math.abs(dx);
            final float y = ev.getY();
            final float yDiff = Math.abs(y - mLastMotionY);

            if (xDiff > yDiff) {
                PullToRefreshLayout.REFRESH_FLAG = false;
                mLastMotionX = x;
                mLastMotionY = y;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean mIsBeingDragged;
    private float mLastMotionX;
    private float mLastMotionY;

    //获取当前的页面index
    public int getCurrentItem() {
        if (viewPager != null) {
            return viewPager.getRealItem();
        }
        return -1;
    }

    //设置当前的页面index
    public void setcurrentitem(int index) {
        if (viewPager != null) {
            viewPager.setCurrentItem(index);
        }
    }

    public ViewPager.OnPageChangeListener getOnPageChangeListener() {
        return onPageChangeListener;
    }

    /**
     * 设置翻页监听器
     *
     * @param onPageChangeListener
     * @return
     */
    public ConvenientBannerBookShelf setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
        //如果有默认的监听器（即是使用了默认的翻页指示器）则把用户设置的依附到默认的上面，否则就直接设置
        if (pageChangeListener != null)
            pageChangeListener.setOnPageChangeListener(onPageChangeListener);
        else
            viewPager.setOnPageChangeListener(onPageChangeListener);
        return this;
    }

    public boolean isCanLoop() {
        return viewPager.isCanLoop();
    }

    /**
     * 监听item点击
     *
     * @param onItemClickListener
     */
    public ConvenientBannerBookShelf setOnItemClickListener(OnItemClickListener onItemClickListener) {
        if (onItemClickListener == null) {
            viewPager.setOnItemClickListener(null);
            return this;
        }
        viewPager.setOnItemClickListener(onItemClickListener);
        return this;
    }

    /**
     * 设置ViewPager的滚动速度
     *
     * @param scrollDuration
     */
    public void setScrollDuration(int scrollDuration) {
        scroller.setScrollDuration(scrollDuration);
    }

    public int getScrollDuration() {
        return scroller.getScrollDuration();
    }

    public CBLoopViewPager getViewPager() {
        return viewPager;
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
        viewPager.setCanLoop(canLoop);
    }
}
