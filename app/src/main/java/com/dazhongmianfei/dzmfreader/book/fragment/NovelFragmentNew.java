package com.dazhongmianfei.dzmfreader.book.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.activity.AnnounceActivity;
import com.dazhongmianfei.dzmfreader.activity.BookInfoActivity;
import com.dazhongmianfei.dzmfreader.activity.LoginActivity;
import com.dazhongmianfei.dzmfreader.activity.MainActivity;
import com.dazhongmianfei.dzmfreader.activity.TaskCenterActivity;
import com.dazhongmianfei.dzmfreader.banner.ConvenientBannerBookShelf;
import com.dazhongmianfei.dzmfreader.banner.holder.CBViewHolderCreator;
import com.dazhongmianfei.dzmfreader.banner.listener.OnItemClickListener;
import com.dazhongmianfei.dzmfreader.bean.Announce;
import com.dazhongmianfei.dzmfreader.book.been.BaseBook;
import com.dazhongmianfei.dzmfreader.book.config.BookConfig;
import com.dazhongmianfei.dzmfreader.config.MainHttpTask;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.eventbus.CloseAnimation;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshBookSelf;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshMine;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshTopbook;
import com.dazhongmianfei.dzmfreader.eventbus.ToStore;
import com.dazhongmianfei.dzmfreader.fragment.BookshelfFragment;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.book.adapter.ShelfAdapterNew;
import com.dazhongmianfei.dzmfreader.read.animation.ContentScaleAnimation;
import com.dazhongmianfei.dzmfreader.read.animation.Rotate3DAnimation;
import com.dazhongmianfei.dzmfreader.read.manager.ChapterManager;
import com.dazhongmianfei.dzmfreader.read.util.DisplayUtils;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.InternetUtils;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;
import com.dazhongmianfei.dzmfreader.utils.Utils;
import com.dazhongmianfei.dzmfreader.view.AdaptionGridViewNoMargin;
import com.dazhongmianfei.dzmfreader.view.BookShelfBannerHolderView;
import com.dazhongmianfei.dzmfreader.view.MarqueeTextView;
import com.dazhongmianfei.dzmfreader.view.MarqueeTextViewClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 书架
 */
public class NovelFragmentNew extends Fragment implements View.OnClickListener, Animation.AnimationListener {

    private WindowManager mWindowManager;
    private AbsoluteLayout wmRootView;


    public  List<BaseBook> bookLists;
    public ShelfAdapterNew adapter;
    private ImageView shelfitem_img;
    //点击书本在屏幕中的x，y坐标
    private int[] location = new int[2];
    private int[] location2 = new int[2];

    private ImageView cover;
    private ImageView content;
    //书本打开动画缩放比例
    private float scaleTimes;
    //书本打开缩放动画
    private ContentScaleAnimation contentAnimation;
    private Rotate3DAnimation coverAnimation;
    //书本打开缩放动画持续时间
    public static final int ANIMATION_DURATION = 800;
    //打开书本的第一个动画是否完成
    private boolean mIsOpen = false;
    //动画加载计数器  0 默认  1一个动画执行完毕   2二个动画执行完毕
    private int animationCount = 0;
    AdaptionGridViewNoMargin adaptionGridViewNoMargin;
    /**
     * 公告
     */
    private MarqueeTextView fragment_bookshelf_marquee;
    private LinearLayout fragment_bookshelf_marquee_layout;
    private TextView mDeleteBtn;
    private LinearLayout fragment_bookshelf_noresult;
    NestedScrollView fragment_bookshelf_scrollview;

    LinearLayout fragment_bookshelf_head;

    ConvenientBannerBookShelf fragment_discovery_banner_male;

    LinearLayout fragment_shelf_banner_layout;
    /**
     * 点击的书本的position
     */
    private int mPosition;

    View MainActivityNavigationView;

    int read_background_paperYellow;
    boolean openbooking;
    Activity activity;

    public TextView fragment_novel_allchoose;
    public TextView fragment_novel_cancle;
    // public RelativeLayout fragment_novel_sign_layout;
    boolean showGuangbo;

    LinearLayout shelf_book_delete_btn;
    public int WIDTH, HEIGHT, HorizontalSpacing, H40;

    @SuppressLint("ValidFragment")
    public NovelFragmentNew(List<BaseBook> bookLists, LinearLayout shelf_book_delete_btn) {
        MyToash.Log("shelf_book_delete_btn 11", (shelf_book_delete_btn == null) + "");
        this.shelf_book_delete_btn = shelf_book_delete_btn;
        this.fragment_novel_allchoose = shelf_book_delete_btn.findViewById(R.id.fragment_novel_allchoose);
        this.fragment_novel_cancle = shelf_book_delete_btn.findViewById(R.id.fragment_novel_cancle);
        mDeleteBtn = shelf_book_delete_btn.findViewById(R.id.shelf_book_delete_del);
        if (bookLists != null) {
            this.bookLists = bookLists;
        }

    }

    public void AllchooseAndCancleOnclick(boolean flag) {
        if (flag) {
            if (adapter.checkedBookList.size() ==bookLists.size()) {
                adapter.selectAll(false);
            } else {
                adapter.selectAll(true);
            }
            adapter.notifyDataSetChanged();
        } else {
            if (adapter != null && adapter.isDeletable()) {
                adapter.setDeletable(false);
                adapter.notifyDataSetChanged();

                MainActivityNavigationView.setVisibility(View.VISIBLE);
                shelf_book_delete_btn.setVisibility(View.GONE);
                fragment_bookshelf_head.setVisibility(View.VISIBLE);
                if (showGuangbo) {
                    fragment_bookshelf_marquee_layout.setVisibility(View.VISIBLE);
                }

            }
        }
    }

    public NovelFragmentNew() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        WIDTH = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        WIDTH = (WIDTH - ImageUtil.dp2px(activity, 40)) / 3;//横向排版 图片宽度
        HEIGHT = (int) (((float) WIDTH * 4f / 3f));//
        HorizontalSpacing = ImageUtil.dp2px(activity, 5);

        read_background_paperYellow = Color.parseColor("#D4C5A3");
        mWindowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        wmRootView = new AbsoluteLayout(activity);
        EventBus.getDefault().register(this);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bookshelf_old, null);
        //   fragment_novel_sign_layout = view.findViewById(R.id.fragment_novel_sign_layout);
        fragment_bookshelf_head = view.findViewById(R.id.fragment_bookshelf_head);
        fragment_discovery_banner_male = view.findViewById(R.id.fragment_shelf_banner_male);
        fragment_shelf_banner_layout = view.findViewById(R.id.fragment_shelf_banner_layout);

        fragment_bookshelf_noresult = view.findViewById(R.id.fragment_bookshelf_noresult);
        fragment_bookshelf_marquee_layout = view.findViewById(R.id.fragment_bookshelf_marquee_layout);
        fragment_bookshelf_marquee = view.findViewById(R.id.fragment_bookshelf_marquee);

        adaptionGridViewNoMargin = view.findViewById(R.id.bookShelf);
        //   adaptionGridViewNoMargin.setHorizontalSpacing(HorizontalSpacing);

        fragment_bookshelf_scrollview = view.findViewById(R.id.fragment_bookshelf_scrollview);
        adaptionGridViewNoMargin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setAnimation(view, position);
            }
        });
        adaptionGridViewNoMargin.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                setLongClickListener(position);
                return true;
            }
        });
        MainActivityNavigationView = ((MainActivity) activity).getNavigationView();
        view.findViewById(R.id.fragment_bookshelf_go_shelf).setOnClickListener(this);
        view.findViewById(R.id.fragment_bookshelf_sign).setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (bookLists == null || bookLists.isEmpty()) {
            bookLists = LitePal.where("isAddBookSelf = ?", "1").find(BaseBook.class);
            Collections.sort(bookLists);// 排序
        }
        adapter = new ShelfAdapterNew(WIDTH, HEIGHT, bookLists, activity);
        adaptionGridViewNoMargin.setAdapter(adapter);
        if (bookLists.isEmpty()) {
            fragment_bookshelf_noresult.setVisibility(View.VISIBLE);
            adaptionGridViewNoMargin.setVisibility(View.GONE);
        } else {
            fragment_bookshelf_noresult.setVisibility(View.GONE);
            adaptionGridViewNoMargin.setVisibility(View.VISIBLE);
        }
        MainHttpTask.getInstance().getResultString(activity, "ShelfBook", new MainHttpTask.GetHttpData() {
            @Override
            public void getHttpData(String result) {
                try {
                    handleResult(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshTopbook(RefreshTopbook s) {
        if (!s.flag) {
        } else {
            for (BaseBook baseBook : bookLists) {
                if (baseBook.getBook_id().equals(s.book_id)) {
                    baseBook.setCurrent_chapter_id(s.current_chapter_id);
                    return;
                }
            }

        }
    }

    public void exchangePosition(BaseBook onclickbook) {

        BaseBook mTopBook = bookLists.get(0);
        ContentValues values2 = new ContentValues();
        onclickbook.setBookselfPosition(10000);
        values2.put("bookselfPosition", 10000);

        if (onclickbook.getId() == 0) {
            LitePal.updateAll(BaseBook.class, values2, "comic_id = ?", onclickbook.getBook_id());
        } else {
            LitePal.update(BaseBook.class, values2, onclickbook.getId());
        }

        ContentValues values = new ContentValues();
        mTopBook.setBookselfPosition(0);
        values.put("bookselfPosition", 0);


        if (mTopBook.getId() == 0) {
            LitePal.updateAll(BaseBook.class, values, "book_id = ?", mTopBook.getBook_id());
        } else {
            LitePal.update(BaseBook.class, values, mTopBook.getId());
        }
        Collections.sort(bookLists);// 排序
    }

    RefreshBookSelf S;

    long time1;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshBookSelf(RefreshBookSelf s) {
        if (System.currentTimeMillis() - time1 <= 2000) {
            return;
        }
        time1 = System.currentTimeMillis();
        if (s.flag) {//退出登录集合数据 刷新


         /*   for (BaseBook baseBook : bookLists) {//更新书架 和 本地数据库书籍 uid为 none
                baseBook.setUid("none");
                // baseBook.setAddBookSelf(0);
                ContentValues values = new ContentValues();
                values.put("uid", "none");
                //   values.put("isAddBookSelf", "1");
                if (baseBook.getId() == 0) {
                    LitePal.update(BaseBook.class, values, baseBook.getId());
                } else {
                    LitePal.updateAll(BaseBook.class, values, "book_id = ? ", baseBook.getBook_id());
                }
            }
            adapter.notifyDataSetChanged();
*/

        } else if (s.baseBooks == null || s.baseBooks.isEmpty()) {//登录
            init();
        } else {//加书架
            MyToash.Log("setColor---1", " " + bookLists.size());
            for (BaseBook baseBook : s.baseBooks) {
                if (!bookLists.contains(baseBook)) {
                    bookLists.add(baseBook);
                    //secondBookLists.add(baseBook);
                }
            }
            adapter = new ShelfAdapterNew(WIDTH, HEIGHT, bookLists, activity);
            adaptionGridViewNoMargin.setAdapter(adapter);
            if (!bookLists.isEmpty()) {
                fragment_bookshelf_noresult.setVisibility(View.GONE);
                adaptionGridViewNoMargin.setVisibility(View.VISIBLE);
            }
            String bookid = "";
            for (BaseBook baseBook : s.baseBooks) {
                bookid += "," + baseBook.getBook_id();
            }
            bookid = bookid.substring(1);
            addHttpBookself(bookid);

        }


    }

    public void setDataBaseData(boolean IsSncyWebBookShelf) {
        if (bookLists != null && bookLists.isEmpty()) {
            fragment_bookshelf_noresult.setVisibility(View.VISIBLE);
            adaptionGridViewNoMargin.setVisibility(View.GONE);

        } else {
            fragment_bookshelf_noresult.setVisibility(View.GONE);
            adaptionGridViewNoMargin.setVisibility(View.VISIBLE);

            setBookSelfBooks();

            if (IsSncyWebBookShelf) {
                addHttpBookself(null);
            }
        }
     /*   } catch (Exception e) {
        }*/
    }

    private void setBookSelfBooks() {
        adapter = new ShelfAdapterNew(WIDTH, HEIGHT, bookLists, activity);
        adaptionGridViewNoMargin.setAdapter(adapter);
    }

    private void addHttpBookself(String bookid) {
        if (Utils.isLogin(activity) && InternetUtils.internett(activity)) {
            if (bookid == null) {
                for (BaseBook baseBook : bookLists) {
                    bookid += "," + baseBook.getBook_id();

                }
                bookid = bookid.substring(1);
            }

            addBookToShelf(bookid, activity, new AddBookToShelf() {
                @Override
                public void addSuccess() {

                }

                @Override
                public void addFail() {
                }
            });
        }
    }

    private void setAnimation(View view, int position) {

        if (!adapter.isDeletable()) {
            if (openbooking) {
                return;
            }
            openbooking = true;
            cover = new ImageView(activity);
            shelfitem_img = view.findViewById(R.id.shelfitem_img);
            cover.setImageBitmap(loadBitmapFromView(shelfitem_img));
            mPosition = position;
            shelfitem_img.getLocationInWindow(location);
            try {
                mWindowManager.addView(wmRootView, getDefaultWindowParams());
            } catch (Exception e) {
            }

            content = new ImageView(activity);
            Bitmap contentBitmap = Bitmap.createBitmap(shelfitem_img.getMeasuredWidth(), shelfitem_img.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            contentBitmap.eraseColor(read_background_paperYellow);
            content.setImageBitmap(contentBitmap);

            AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(shelfitem_img.getLayoutParams());
            params.x = location[0];
            params.y = location[1];
            wmRootView.addView(content, params);
            wmRootView.addView(cover, params);

            initAnimation();
            if (contentAnimation.getMReverse()) {
                contentAnimation.reverse();
            }
            if (coverAnimation.getMReverse()) {
                coverAnimation.reverse();
            }
            cover.clearAnimation();
            cover.startAnimation(coverAnimation);
            content.clearAnimation();
            content.startAnimation(contentAnimation);


        }
    }


    //初始化dialog动画
    private void initAnimation() {
        AccelerateInterpolator interpolator = new AccelerateInterpolator();

        float scale1 = DisplayUtils.getScreenWidthPixels(activity) / ((float) shelfitem_img.getMeasuredWidth());
        float scale2 = DisplayUtils.getScreenHeightPixels(activity) / ((float) shelfitem_img.getMeasuredHeight());


        scaleTimes = scale1 > scale2 ? scale1 : scale2;  //计算缩放比例

        contentAnimation = new ContentScaleAnimation(location[0], location[1], scaleTimes, false);
        contentAnimation.setInterpolator(interpolator);  //设置插值器
        contentAnimation.setDuration(ANIMATION_DURATION);
        contentAnimation.setFillAfter(true);  //动画停留在最后一帧
        contentAnimation.setAnimationListener(this);

        coverAnimation = new Rotate3DAnimation(0, -180, location[0], location[1], scaleTimes, false);
        coverAnimation.setInterpolator(interpolator);
        coverAnimation.setDuration(ANIMATION_DURATION);
        coverAnimation.setFillAfter(true);
        coverAnimation.setAnimationListener(this);
        Log.i("initAnimation", "" + ANIMATION_DURATION);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        openbooking = false;
        //有两个动画监听会执行两次，所以要判断
        if (!mIsOpen) {
            animationCount++;
            if (animationCount >= 2) {
                mIsOpen = true;
                handler.sendEmptyMessage(4);
            }

        } else {
            animationCount--;
            if (animationCount <= 0) {
                mIsOpen = false;
                wmRootView.removeView(cover);
                wmRootView.removeView(content);
                mWindowManager.removeView(wmRootView);
            }
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void handleResult(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);
        initBanner(obj);
        initAnnoun(obj);
        if (Utils.isLogin(activity)) {
            final JSONArray bookArr = obj.getJSONArray("list");
            if (bookArr.length() != 0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String uid=Utils.getUID(activity);
                            for (int i = 0; i < bookArr.length(); i++) {
                                BaseBook baseBook = new BaseBook();
                                JSONObject jsonObject = bookArr.getJSONObject(i);
                                baseBook.setName(jsonObject.getString("name"));
                                baseBook.setBook_id(jsonObject.getString("book_id"));
                                baseBook.setCover(jsonObject.getString("cover"));
                                baseBook.setAuthor(jsonObject.getString("author"));
                                int total_chapter = jsonObject.getInt("total_chapter");
                                baseBook.setDescription(jsonObject.getString("description"));
                                baseBook.setRecentChapter(total_chapter);
                                baseBook.setTotal_Chapter(total_chapter);
                                baseBook.setUid(Utils.getUID(activity));
                                boolean isflag = false;//是否存在
                                FALG:

                                for (BaseBook baseBookt : bookLists) {
                                    if (baseBookt.getBook_id().equals(baseBook.getBook_id())) {
                                        isflag = true;
                                        baseBookt.setUid(uid);
                                        if (total_chapter > baseBookt.getTotal_Chapter()) {//更新最新章节数目
                                            baseBookt.setTotal_Chapter(total_chapter);
                                            baseBookt.setRecentChapter(total_chapter - baseBookt.getRecentChapter());
                                        }
                                        baseBookt.setName(baseBook.getName());
                                        baseBookt.setName(baseBook.getName());
                                        baseBookt.setCover(baseBook.getCover());
                                        baseBookt.setAuthor(baseBook.getAuthor());
                                        baseBookt.setDescription(baseBook.getDescription());
                                        break FALG;
                                    }
                                }
                                if (!isflag) {
                                    baseBook.setAddBookSelf(1);
                                    baseBook.saveIsexist(1);
                                    bookLists.add(baseBook);
                                }
                            }
                            for (BaseBook baseBookt : bookLists) {

                                if (baseBookt.isAddBookSelf() == 0) {
                                    baseBookt.saveIsexist(1);
                                    baseBookt.setAddBookSelf(1);
                                } else {
                                    ContentValues values = new ContentValues();
                                    values.put("uid", uid);
                                    values.put("total_chapter", baseBookt.getTotal_Chapter());
                                    values.put("recentChapter", baseBookt.getTotal_Chapter());
                                    values.put("name", baseBookt.getName());
                                    values.put("cover", baseBookt.getCover());
                                    values.put("author", baseBookt.getAuthor());
                                    values.put("description", baseBookt.getDescription());
                                    if (baseBookt.getId() == 0) {
                                        LitePal.updateAll(BaseBook.class, values, "book_id = ?", baseBookt.getBook_id());
                                    } else {
                                        LitePal.update(BaseBook.class, values, baseBookt.getId());
                                    }
                                }
                            }
                            handler.sendEmptyMessage(0);
                        } catch (Exception e) {
                        }


                    }
                }).start();

            }
        }
    }

    private void initAnnoun(JSONObject obj)  {
        try {
            JSONArray announceArr = obj.getJSONArray("announce");
            if (announceArr.length() > 0) {
                final List<Announce> announceList = new ArrayList<>();
                Gson gson = new Gson();
                for (int i = 0; i < announceArr.length(); i++) {
                    announceList.add(gson.fromJson(announceArr.getJSONObject(i).toString(), Announce.class));
                }
                fragment_bookshelf_marquee.setTextArraysAndClickListener(announceList, new MarqueeTextViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(activity, AnnounceActivity.class);
                        intent.putExtra("announce_content", announceList.get(position).getTitle() + "/-/" + announceList.get(position).getContent());
                        startActivity(intent);
                    }
                });

                showGuangbo = true;
            } else {
                showGuangbo = false;
                fragment_bookshelf_marquee_layout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            showGuangbo = false;
            fragment_bookshelf_marquee_layout.setVisibility(View.GONE);
        }
    }

    private JSONObject initBanner(JSONObject obj){
        try {
            JSONArray recomment = obj.getJSONArray("recommend");
            List<BaseBook> mBannerItemListMale = new ArrayList<>();

            for (int i = 0; i < recomment.length(); i++) {
                final JSONObject labelObj = recomment.getJSONObject(i);
                BaseBook baseBook = new BaseBook();
                baseBook.setBook_id(labelObj.getString("book_id"));
                baseBook.setName(labelObj.getString("name"));
                baseBook.setCover(labelObj.getString("cover"));
                baseBook.setDescription(labelObj.getString("description"));
                mBannerItemListMale.add(baseBook);
            }

            if (!mBannerItemListMale.isEmpty()) {
                fragment_discovery_banner_male.setVisibility(View.VISIBLE);
                fragment_discovery_banner_male.setPages(new CBViewHolderCreator<BookShelfBannerHolderView>() {
                    @Override
                    public BookShelfBannerHolderView createHolder() {
                        return new BookShelfBannerHolderView();
                    }
                }, mBannerItemListMale).setPageIndicator(new int[]{R.mipmap.ic_shelf_yes, R.mipmap.ic_shelf_no})
                        .setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(int position) {
                                Intent intent = new Intent(activity, BookInfoActivity.class);
                                intent.putExtra("book_id", mBannerItemListMale.get(position).getBook_id());
                                startActivity(intent);
                            }
                        });
                fragment_discovery_banner_male.startTurning(2000);
            } else {
                fragment_shelf_banner_layout.setVisibility(View.GONE);
            }
            return obj;
        } catch (Exception e) {
            fragment_shelf_banner_layout.setVisibility(View.GONE);
            return null;
        }
    }

    int i;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                setDataBaseData(true);
            } else if (msg.what == 4) {
                openbooking = false;
                BaseBook baseBook = bookLists.get(mPosition);
                baseBook.setAddBookSelf(1);
                BookShelfOpen = true;
                ChapterManager.getInstance(activity).openBook(baseBook, baseBook.getBook_id());
                if (mPosition != 0) {
                    exchangePosition(baseBook);
                    setBookSelfBooks();
                    fragment_bookshelf_scrollview.scrollTo(0, 0);
                }
            }
        }
    };

    public static boolean BookShelfOpen;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void close(CloseAnimation closeAnimation) {
        BookShelfOpen = false;
        closeBookAnimation();

    }

    public void init() {
        MainHttpTask.getInstance().httpSend(activity, BookConfig.mBookCollectUrl, "ShelfBook", new MainHttpTask.GetHttpData() {
            @Override
            public void getHttpData(String result) {
                try {
                    handleResult(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void closeBookAnimation() {

        if (adapter != null && adapter.shelfitem_img_first != null) {
            adapter.shelfitem_img_first.getLocationInWindow(location2);
        }
        if (mIsOpen && wmRootView != null) {
            //因为书本打开后会移动到第一位置，所以要设置新的位置参数
            contentAnimation.setmPivotXValue(location2[0]);
            contentAnimation.setmPivotYValue(location2[1]);
            coverAnimation.setmPivotXValue(location2[0]);
            coverAnimation.setmPivotYValue(location2[1]);

            AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(shelfitem_img.getLayoutParams());
            params.x = location2[0];
            params.y = location2[1];//firstLocation[1]在滑动的时候回改变,所以要在dispatchDraw的时候获取该位置值
            cover.setImageBitmap(loadBitmapFromView(shelfitem_img));
            wmRootView.updateViewLayout(cover, params);
            wmRootView.updateViewLayout(content, params);
            //动画逆向运行
            if (!contentAnimation.getMReverse()) {
                contentAnimation.reverse();
            }
            if (!coverAnimation.getMReverse()) {
                coverAnimation.reverse();
            }
            //清除动画再开始动画
            content.clearAnimation();
            content.startAnimation(contentAnimation);
            cover.clearAnimation();
            cover.startAnimation(coverAnimation);
        }

    }


    public static Bitmap loadBitmapFromView(View v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(screenshot);
        canvas.translate(-v.getScrollX(), -v.getScrollY());//我们在用滑动View获得它的Bitmap时候，获得的是整个View的区域（包括隐藏的），如果想得到当前区域，需要重新定位到当前可显示的区域
        v.draw(canvas);// 将 view 画到画布上
        return screenshot;
    }

    //获取dialog属性
    private WindowManager.LayoutParams getDefaultWindowParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                0, 0,
                WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,//windown类型,有层级的大的层级会覆盖在小的层级
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.RGBA_8888);

        return params;
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        fragment_bookshelf_marquee.releaseResources();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {//
            case R.id.fragment_bookshelf_go_shelf:
                //去书城逛逛
                EventBus.getDefault().post(new ToStore(1));
                break;

            case R.id.fragment_bookshelf_sign:
                if (Utils.isLogin(activity)) {
                    startActivity(new Intent(activity, TaskCenterActivity.class));
                } else {
                    MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.MineNewFragment_nologin));
                    Intent intent = new Intent();
                    intent.setClass(activity, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private void setLongClickListener(int position) {
        fragment_bookshelf_head.setVisibility(View.GONE);
        //ibility(View.VISIBLE);
        //ibility(View.VISIBLE);
        MainActivityNavigationView.setVisibility(View.INVISIBLE);
        shelf_book_delete_btn.setVisibility(View.VISIBLE);
        fragment_novel_allchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllchooseAndCancleOnclick(true);
            }
        });
        fragment_novel_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllchooseAndCancleOnclick(false);
            }
        });
        adapter.setDeletable(true, mDeleteBtn, position, new BookshelfFragment.DeleteBook() {

            @Override
            public void success() {
                fragment_bookshelf_head.setVisibility(View.VISIBLE);
                MainActivityNavigationView.setVisibility(View.VISIBLE);
                shelf_book_delete_btn.setVisibility(View.GONE);

                if (showGuangbo) {
                    fragment_bookshelf_marquee_layout.setVisibility(View.VISIBLE);
                }
                //ibility(View.GONE);
                //ibility(View.GONE);

                if (adapter != null && adapter.isDeletable()) {
                    adapter.setDeletable(false);
                }

                if (bookLists.isEmpty()) {
                    setDataBaseData(false);
                } else {

                    setBookSelfBooks();
                }

            }

            @Override
            public void fail() {

            }
        });
    }

    /**
     * 加入书架
     */


    public interface AddBookToShelf {
        void addSuccess();

        void addFail();
    }

    public static void addBookToShelf(String Book_id, final Activity activity, final AddBookToShelf addBookToShelf) {


        ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("book_id", Book_id);

        String json = params.generateParamsJson();

        HttpUtils.getInstance(activity).sendRequestRequestParams3(ReaderConfig.mBookAddCollectUrl, json, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        addBookToShelf.addSuccess();
                        String UID=Utils.getUID(activity);
                        if(ShareUitls.getFirstAddShelf(activity,UID,true)){
                            EventBus.getDefault().post(new RefreshMine(null));
                            ShareUitls.putFirstAddShelf(activity,UID,false);
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                        addBookToShelf.addFail();
                    }
                }

        );


    }
}
