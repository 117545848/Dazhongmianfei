package com.dazhongmianfei.dzmfreader.read.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.read.ReadingConfig;
import com.dazhongmianfei.dzmfreader.read.dialog.AutoProgress;
import com.dazhongmianfei.dzmfreader.read.util.PageFactory;
import com.dazhongmianfei.dzmfreader.read.view.animation.AnimationProvider;
import com.dazhongmianfei.dzmfreader.read.view.animation.CoverAnimation;
import com.dazhongmianfei.dzmfreader.read.view.animation.NoneAnimation;
import com.dazhongmianfei.dzmfreader.read.view.animation.PageAnimation;
import com.dazhongmianfei.dzmfreader.read.view.animation.SimulationAnimation;
import com.dazhongmianfei.dzmfreader.read.view.animation.SlideAnimation;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;
import com.dazhongmianfei.dzmfreader.utils.Utils;

import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.READBUTTOM_HEIGHT;

/**
 * Created by scb on 2018/8/29 0029.
 */
public class PageWidget extends View {
    private final static String TAG = "BookPageWidget";
    private int mScreenWidth = 0; // 屏幕宽
    public int mScreenHeight = 0; // 屏幕高
    private Context mContext;

    //是否移动了
    private Boolean isMove = false;
    //是否翻到下一页
    private Boolean isNext = false;
    //是否取消翻页
    private Boolean cancelPage = false;
    //是否没下一页或者上一页
    private Boolean noNext = false;
    private int downX = 0;
    private int downY = 0;

    private int moveX = 0;
    private int moveY = 0;
    //翻页动画是否在执行
    private Boolean isRuning = false;

    Bitmap mCurPageBitmap = null; // 当前页
    Bitmap mNextPageBitmap = null;
    private AnimationProvider mAnimationProvider;
    private PageAnimation mPageAnim;

    Scroller mScroller;
    public int mBgColor = 0xFFCEC29C;
    private TouchListener mTouchListener;
    FrameLayout ADview;
    public int Current_Page;

    public void setADview(FrameLayout ADview) {
        this.ADview = ADview;


    }

    public PageWidget(Context context) {
        this(context, null);
    }

    public PageWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initPage();
        mScroller = new Scroller(getContext(), new LinearInterpolator());
        mAnimationProvider = new SimulationAnimation(mCurPageBitmap, mNextPageBitmap, mScreenWidth, mScreenHeight);
    }


    private void initPage() {
   /*     WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);*/
        mScreenWidth = ScreenSizeUtils.getInstance(mContext).getScreenWidth();
        mScreenHeight = ScreenSizeUtils.getInstance(mContext).getScreenHeight();  //RGB_565
        if (ReaderConfig.USE_AD) {
            mScreenHeight = mScreenHeight - ImageUtil.dp2px(mContext, READBUTTOM_HEIGHT);
        }

        MyToash.Log("mScreenHeight", mScreenHeight + "");
        try {
            mCurPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.RGB_565);      //android:LargeHeap=true  use in  manifest application
            mNextPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.RGB_565);
        } catch (Error e) {
            mCurPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ALPHA_8);      //android:LargeHeap=true  use in  manifest application
            mNextPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ALPHA_8);
        }
    }

    public void setPageMode(int pageMode) {
        switch (pageMode) {
            case ReadingConfig.PAGE_MODE_SIMULATION:
                mAnimationProvider = new SimulationAnimation(mCurPageBitmap, mNextPageBitmap, mScreenWidth, mScreenHeight);
                break;
            case ReadingConfig.PAGE_MODE_COVER:
                mAnimationProvider = new CoverAnimation(mCurPageBitmap, mNextPageBitmap, mScreenWidth, mScreenHeight);
                break;
            case ReadingConfig.PAGE_MODE_SLIDE:
                mAnimationProvider = new SlideAnimation(mCurPageBitmap, mNextPageBitmap, mScreenWidth, mScreenHeight);
                break;
            case ReadingConfig.PAGE_MODE_NONE:
                mAnimationProvider = new NoneAnimation(mCurPageBitmap, mNextPageBitmap, mScreenWidth, mScreenHeight);
                break;

            default:
                mAnimationProvider = new SimulationAnimation(mCurPageBitmap, mNextPageBitmap, mScreenWidth, mScreenHeight);
        }
    }



/*    private PageAnimation.OnPageChangeListener mPageAnimListener = new PageAnimation.OnPageChangeListener() {
        @Override
        public boolean hasPrev() {
            return mTouchListener.prePage();
        }

        @Override
        public boolean hasNext() {
            return mTouchListener.nextPage();
        }

        @Override
        public void pageCancel() {
            mTouchListener.cancel();
        }
    };*/

    public Bitmap getCurPage() {
        return mCurPageBitmap;
    }

    public Bitmap getNextPage() {
        return mNextPageBitmap;
    }

    /////////////////
    public interface OnSwitchPreListener {
        void switchPreChapter();
    }

    private OnSwitchPreListener mOnSwitchPreListener;

    public void setOnSwitchPreListener(OnSwitchPreListener listener) {
        mOnSwitchPreListener = listener;
    }

    //////////////
    public interface OnSwitchNextListener {
        void switchNextChapter();
    }

    private OnSwitchNextListener mOnSwitchNextListener;

    public void setOnSwitchNextListener(OnSwitchNextListener listener) {
        mOnSwitchNextListener = listener;
    }

    public void setBgColor(int color) {
        mBgColor = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(0xFFAAAAAA);
        canvas.drawColor(mBgColor);
        Utils.printLog("onDraw", "isNext:" + isNext + "          isRuning:" + isRuning);
        if (isRuning) {
            try {
                mAnimationProvider.drawMove(canvas);
            } catch (Exception e) {
            }
        } else {
            mAnimationProvider.drawStatic(canvas);
        }
    }

    boolean onTouchEventing;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        MyToash.Log("onTouchEvent", "11111");
        if (ADview != null) {
            ADview.setVisibility(INVISIBLE);
        }

        long time1 = System.currentTimeMillis();
        long time = ShareUitls.getLong(getContext(), "OnRewardVerify", 0);

        if (Math.abs((time - time1)) > 1200000) {
            PageFactory.close_AD = false;
        } else {
            PageFactory.close_AD = true;
        }
        if (PageFactory.getStatus() == PageFactory.Status.OPENING) {
            onTouchEventing = false;
            return false;
        }

        MyToash.Log("onTouchEvent", "222");

        int x = (int) event.getX();
        int y = (int) event.getY();

        mAnimationProvider.setTouchPoint(x, y);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downX = (int) event.getX();
            downY = (int) event.getY();
            moveX = 0;
            moveY = 0;
            isMove = false;
//            cancelPage = false;
            noNext = false;
            isNext = false;
            isRuning = false;
            mAnimationProvider.setStartPoint(downX, downY);
            abortAnimation();
            Utils.printLog(TAG, "ACTION_DOWN");
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

            final int slop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
            //判断是否移动了
            if (!isMove) {
                isMove = Math.abs(downX - x) > slop || Math.abs(downY - y) > slop;
            }

            if (isMove) {
                isMove = true;
                if (moveX == 0 && moveY == 0) {
                    //  Utils.printLog(TAG, "isMove");
                    //判断翻得是上一页还是下一页
                    isNext = x - downX <= 0;
                    cancelPage = false;
                    if (isNext) {
                        Boolean isNext = mTouchListener.nextPage();
//                        calcCornerXY(downX,mScreenHeight);
                        mAnimationProvider.setDirection(AnimationProvider.Direction.next);

                        if (!isNext) {
                            onTouchEventing = false;
                            noNext = true;
                            return true;
                        }
                    } else {
                        Boolean isPre = mTouchListener.prePage();
                        mAnimationProvider.setDirection(AnimationProvider.Direction.pre);

                        if (!isPre) {
                            noNext = true;
                            onTouchEventing = false;
                            return true;
                        }
                    }
                    Utils.printLog(TAG, "isNext:" + isNext);
                } else {
                    //判断是否取消翻页
                    if (isNext) {
                        if (x - moveX > 0) {
                            cancelPage = true;
                            mAnimationProvider.setCancel(true);
                        } else {
                            cancelPage = false;
                            mAnimationProvider.setCancel(false);
                        }
                    } else {
                        if (x - moveX < 0) {
                            mAnimationProvider.setCancel(true);
                            cancelPage = true;
                        } else {
                            mAnimationProvider.setCancel(false);
                            cancelPage = false;
                        }
                    }
                    Utils.printLog(TAG, "cancelPage:" + cancelPage);
                }

                moveX = x;
                moveY = y;
                isRuning = true;
                this.postInvalidate();
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            Utils.printLog(TAG, "ACTION_UP");
            if (!isMove) {
                cancelPage = false;
                //是否点击了中间
                if (downX > mScreenWidth / 5 && downX < mScreenWidth * 4 / 5) {//&& downY > mScreenHeight / 3 && downY < mScreenHeight * 2 / 3
                    if (mTouchListener != null) {
                        mTouchListener.center();
                    }
                    onTouchEventing = false;
                    return true;
                } else isNext = x >= mScreenWidth / 2;

                if (isNext) {
                    Boolean isNext = mTouchListener.nextPage();
                    mAnimationProvider.setDirection(AnimationProvider.Direction.next);
                    if (!isNext) {
                        onTouchEventing = false;
                        return true;
                    }
                } else {
                    Boolean isPre = mTouchListener.prePage();
                    mAnimationProvider.setDirection(AnimationProvider.Direction.pre);
                    if (!isPre) {
                        onTouchEventing = false;
                        return true;
                    }
                }
            }

            if (cancelPage && mTouchListener != null) {
                mTouchListener.cancel();
            }

            Utils.printLog(TAG, "isNext:" + isNext);


            if (!noNext) {
                isRuning = true;
                //mAnimationProvider.setDirection(AnimationProvider.Direction.next);


                mAnimationProvider.startAnimation(mScroller, new AnimationProvider.OnAnimationStopped() {
                    @Override
                    public void nextStop() {
                        ++Current_Page;
                        onTouchEventing = false;
                        if (AutoProgress.getInstance().isStarted()) {
                            AutoProgress.getInstance().restart();
                        }
                        if (mOnSwitchNextListener != null) {
                            mOnSwitchNextListener.switchNextChapter();
                            Utils.printLog("onDrawaaa", "开始加载下一章");
                            mOnSwitchNextListener = null;
                        }

                    }

                    @Override
                    public void preStop() {
                        --Current_Page;
                        onTouchEventing = false;
                        if (AutoProgress.getInstance().isStarted()) {
                            AutoProgress.getInstance().restart();
                        }

                        if (mOnSwitchPreListener != null) {
                            mOnSwitchPreListener.switchPreChapter();
                            Utils.printLog("onDrawaaa", "开始加载上一章");
                            mOnSwitchPreListener = null;
                        }
                    }
                });
                this.postInvalidate();
            }
        }
        onTouchEventing = false;
        return true;
    }


    public void next_page() {

        if (PageFactory.getStatus() == PageFactory.Status.OPENING) {
            return;
        }

        downX = mScreenWidth * 4 / 5;
        downY = mScreenHeight / 2;
        mAnimationProvider.setTouchPoint(downX, downY);
        moveX = 0;
        moveY = 0;
        isMove = false;
        noNext = false;
        isNext = false;
        isRuning = false;
        mAnimationProvider.setStartPoint(downX, downY);
        abortAnimation();

        if (!isMove) {
            cancelPage = false;
            isNext = true;

            if (isNext) {
                Boolean isNext = mTouchListener.nextPage();
                mAnimationProvider.setDirection(AnimationProvider.Direction.next);

            }
        }

        if (cancelPage && mTouchListener != null) {
            mTouchListener.cancel();
        }

        if (!noNext) {
            isRuning = true;
            mAnimationProvider.startAnimation(mScroller, new AnimationProvider.OnAnimationStopped() {
                @Override
                public void nextStop() {
                    if (mOnSwitchNextListener != null) {
                        mOnSwitchNextListener.switchNextChapter();
                        Utils.printLog("onDrawaaa", "开始加载下一章");
                        mOnSwitchNextListener = null;
                    }
                }

                @Override
                public void preStop() {
                }
            });
            this.postInvalidate();
        }


    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            float x = mScroller.getCurrX();
            float y = mScroller.getCurrY();
            mAnimationProvider.setTouchPoint(x, y);
            if (mScroller.getFinalX() == x && mScroller.getFinalY() == y) {
                isRuning = false;
            }
            postInvalidate();
        }
        super.computeScroll();
    }

    public void abortAnimation() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
            mAnimationProvider.setTouchPoint(mScroller.getFinalX(), mScroller.getFinalY());
            postInvalidate();
        }
    }

    public boolean isRunning() {
        return isRuning;
    }

    public void setTouchListener(TouchListener mTouchListener) {
        this.mTouchListener = mTouchListener;
    }

    public interface TouchListener {
        void center();

        Boolean prePage();

        Boolean nextPage();

        void cancel();
    }


/*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        *//**
     * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
     *//*
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);


        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        *//**
     * 记录如果是wrap_content是设置的宽和高
     *//*
        int width = 0;
        int height = 0;

        int cCount = getChildCount();

        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;

        // 用于计算左边两个childView的高度
        int lHeight = 0;
        // 用于计算右边两个childView的高度，最终高度取二者之间大值
        int rHeight = 0;

        // 用于计算上边两个childView的宽度
        int tWidth = 0;
        // 用于计算下面两个childiew的宽度，最终宽度取二者之间大值
        int bWidth = 0;

        *//**
     * 根据childView计算的出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是warp_content时
     *//*
        for (int i = 0; i < cCount; i++)
        {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();

            // 上面两个childView
            if (i == 0 || i == 1)
            {
                tWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            }

            if (i == 2 || i == 3)
            {
                bWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            }

            if (i == 0 || i == 2)
            {
                lHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
            }

            if (i == 1 || i == 3)
            {
                rHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
            }

        }

        width = Math.max(tWidth, bWidth);
        height = Math.max(lHeight, rHeight);

        *//**
     * 如果是wrap_content设置为我们计算的值
     * 否则：直接设置为父容器计算的值
     *//*
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth
                : width, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight
                : height);
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b){
        int cCount = getChildCount();
        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;
        *//**
     * 遍历所有childView根据其宽和高，以及margin进行布局
     *//*
        for (int i = 0; i < cCount; i++)
        {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();

            int cl = 0, ct = 0, cr = 0, cb = 0;

            switch (i)
            {
                case 0:
                    cl = cParams.leftMargin;
                    ct = cParams.topMargin;
                    break;
                case 1:
                    cl = getWidth() - cWidth - cParams.leftMargin
                            - cParams.rightMargin;
                    ct = cParams.topMargin;

                    break;
                case 2:
                    cl = cParams.leftMargin;
                    ct = getHeight() - cHeight - cParams.bottomMargin;
                    break;
                case 3:
                    cl = getWidth() - cWidth - cParams.leftMargin
                            - cParams.rightMargin;
                    ct = getHeight() - cHeight - cParams.bottomMargin;
                    break;

            }
            cr = cl + cWidth;
            cb = cHeight + ct;
            childView.layout(cl, ct, cr, cb);
        }
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }*/
}
