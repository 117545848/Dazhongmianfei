package com.dazhongmianfei.dzmfreader.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.adapter.VerticalAdapter;
import com.dazhongmianfei.dzmfreader.bean.BaseTag;
import com.dazhongmianfei.dzmfreader.bean.InfoBook;
import com.dazhongmianfei.dzmfreader.bean.InfoBookItem;
import com.dazhongmianfei.dzmfreader.book.activity.BookCommentActivity;
import com.dazhongmianfei.dzmfreader.book.been.BaseBook;
import com.dazhongmianfei.dzmfreader.bean.BookInfoComment;
import com.dazhongmianfei.dzmfreader.book.been.StroreBookcLable;
import com.dazhongmianfei.dzmfreader.config.ReaderApplication;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshBookInfo;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshBookSelf;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
//.TodayOneAD;
import com.dazhongmianfei.dzmfreader.jinritoutiao.TodayOneAD;
import com.dazhongmianfei.dzmfreader.read.manager.ChapterManager;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyPicasso;
import com.dazhongmianfei.dzmfreader.utils.MyShare;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;
import com.dazhongmianfei.dzmfreader.utils.Utils;
import com.dazhongmianfei.dzmfreader.view.AdaptionGridView;

import com.dazhongmianfei.dzmfreader.view.AndroidWorkaround;
import com.dazhongmianfei.dzmfreader.view.BlurImageview;
import com.dazhongmianfei.dzmfreader.view.CircleImageView;
import com.dazhongmianfei.dzmfreader.view.ObservableScrollView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.dazhongmianfei.dzmfreader.utils.StatusBarUtil.setStatusTextColor;

/**
 * 作品详情
 */
public class BookInfoActivity extends BaseButterKnifeTransparentActivity {
    public final String TAG = BookInfoActivity.class.getSimpleName();

    @Override
    public int initContentView() {
        return R.layout.activity_book_info;
    }

    public String mBookId;
    @BindView(R2.id.book_info_titlebar_container)
    public RelativeLayout book_info_titlebar_container;
    @BindView(R2.id.book_info_titlebar_container_shadow)
    public View book_info_titlebar_container_shadow;
    @BindView(R2.id.activity_book_info_scrollview)
    public ObservableScrollView activity_book_info_scrollview;
    @BindView(R2.id.activity_book_info_content_cover_bg)
    public RelativeLayout activity_book_info_content_cover_bg;
    @BindView(R2.id.titlebar_back)
    public LinearLayout titlebar_back;
    @BindView(R2.id.back)
    public ImageView back;
    @BindView(R2.id.titlebar_share)
    RelativeLayout titlebar_share;
    @BindView(R2.id.titlebar_text)
    public TextView titlebar_text;
    @BindView(R2.id.activity_book_info_content_name)
    public TextView activity_book_info_content_name;
    @BindView(R2.id.activity_book_info_content_author)
    public TextView activity_book_info_content_author;
    @BindView(R2.id.activity_book_info_content_display_label)
    public TextView activity_book_info_content_display_label;
    @BindView(R2.id.activity_book_info_content_total_comment)
    public TextView activity_book_info_content_total_comment;
    @BindView(R2.id.activity_book_info_content_total_shoucanshu)
    public TextView activity_book_info_content_total_shoucanshu;


    @BindView(R2.id.activity_book_info_content_cover)
    public ImageView activity_book_info_content_cover;
    @BindView(R2.id.activity_book_info_content_description)
    public TextView activity_book_info_content_description;
    @BindView(R2.id.activity_book_info_content_last_chapter_time)
    public TextView activity_book_info_content_last_chapter_time;
    @BindView(R2.id.activity_book_info_content_last_chapter)
    public TextView activity_book_info_content_last_chapter;
    @BindView(R2.id.activity_book_info_content_comment_container)
    public LinearLayout activity_book_info_content_comment_container;
    @BindView(R2.id.activity_book_info_content_label_container)
    public LinearLayout activity_book_info_content_label_container;
    @BindView(R2.id.activity_book_info_content_category_layout)
    public RelativeLayout activity_book_info_content_category_layout;
    @BindView(R2.id.activity_book_info_add_shelf)
    public TextView activity_book_info_add_shelf;
    @BindView(R2.id.activity_book_info_start_read)
    public TextView activity_book_info_start_read;
    @BindView(R2.id.activity_book_info_tag)
    LinearLayout activity_book_info_tag;
    @BindView(R2.id.list_ad_view_layout)
    FrameLayout activity_book_info_ad;
    @BindView(R2.id.list_ad_view_img)
    ImageView list_ad_view_img;

    @BindView(R2.id.list_ad_view_layout_chuanshanjia)
    FrameLayout list_ad_view_layout_chuanshanjia;

    @BindView(R2.id.list_ad_layout)
    LinearLayout list_ad_layout;

    @OnClick(value = {R.id.titlebar_back, R.id.activity_book_info_content_category_layout,
            R.id.activity_book_info_add_shelf, R.id.activity_book_info_start_read,
            R.id.titlebar_share,
            //R.id.activity_book_info_down  下载按钮
    })
    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.titlebar_back:
                if (MainActivity.activity == null) {
                    startActivity(new Intent(BookInfoActivity.this, MainActivity.class));
                }
                finish();
                break;
            case R.id.activity_book_info_content_category_layout:
                if (!onclickTwo) {
                    onclickTwo = true;

                    Intent intent = new Intent(this, CatalogActivity.class);
                    intent.putExtra("book_id", mBookId);
                    intent.putExtra("book", mBaseBook);
                    startActivity(intent);
                    onclickTwo = false;
                }
                break;
            case R.id.activity_book_info_add_shelf:
                addBookToLocalShelf();
                break;
            case R.id.activity_book_info_start_read:
                if (!onclickTwo) {
                    onclickTwo = true;
                    mBaseBook.saveIsexist(0);
                    ChapterManager.getInstance(BookInfoActivity.this).openBook(mBaseBook, mBookId);

                    onclickTwo = false;
                    ReaderConfig.integerList.add(1);
                }
                break;
                //大众免费 去掉下载
//            case R.id.activity_book_info_down:
//                // mBaseBook.saveIsexist();
//                new DownDialog().getDownoption(BookInfoActivity.this, mBaseBook, null);
//                // DownDialog.showOpen = true;
//                break;
            case R.id.titlebar_share:
                String url = ReaderConfig.BASE_URL + "/site/share?uid=" + Utils.getUID(BookInfoActivity.this) + "&book_id=" + mBookId + "&osType=2&product=1";
                UMWeb web = new UMWeb(url);
                web.setTitle(mBaseBook.getName());//标题
                web.setThumb(new UMImage(BookInfoActivity.this, mBaseBook.getCover()));  //缩略图
                web.setDescription(mBaseBook.getDescription());//描述
                MyShare.Share(BookInfoActivity.this, "", web);
                break;
        }
    }


    public BaseBook mBaseBook;


    public int WIDTH, WIDTHV, HEIGHT, HEIGHTV, HorizontalSpacing, H100, H50, H20;
    LayoutInflater layoutInflater;


    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setStatusTextColor(false, activity);
        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {                                  //适配华为手机虚拟键遮挡tab的问题
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));                   //需要在setContentView()方法后面执行
        }
        //StatusBarUtil.setTransparent(this);
        EventBus.getDefault().register(this);
        WIDTH = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        layoutInflater = LayoutInflater.from(activity);
        WIDTH = (WIDTH - ImageUtil.dp2px(activity, 40)) / 3;//横向排版 图片宽度
        HEIGHT = (int) (((float) WIDTH * 4f / 3f));//
        HorizontalSpacing = ImageUtil.dp2px(activity, 4);//横间距
        WIDTHV = WIDTH - HorizontalSpacing;//竖向 图片宽度
        HEIGHTV = (int) (((float) WIDTHV * 4f / 3f));//
        H50 = ImageUtil.dp2px(activity, 50);
        H100 = H50; //  相比书城 没有换一换 布局高度
        H20 = ImageUtil.dp2px(activity, 12);
        initView();
    }


    public void initView() {
        if (!ReaderConfig.USE_SHARE) {
            titlebar_share.setVisibility(View.GONE);
        }
        activity_book_info_scrollview.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y <= 0) {
                    setStatusTextColor(false, activity);
                    back.setBackgroundResource(R.mipmap.back_white);
                } else {
                    setStatusTextColor(true, activity);
                    back.setBackgroundResource(R.mipmap.back_black);
                }
                final float ratio = (float) Math.min(Math.max(y, 0), 120) / 120;
                float alpha = (int) (ratio * 255);
                book_info_titlebar_container.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                titlebar_text.setAlpha(ratio);
                book_info_titlebar_container_shadow.setAlpha(ratio);
            }
        });
        initData();
    }

    Gson gson = new Gson();

    public void initInfo(String json) {
        try {
            InfoBookItem infoBookItem = gson.fromJson(json, InfoBookItem.class);
            InfoBook infoBook = infoBookItem.book;
            mBaseBook.setName(infoBook.name);
            mBaseBook.setCover(infoBook.cover);
            mBaseBook.setAuthor(infoBook.author);
            mBaseBook.setDescription(infoBook.description);
            mBaseBook.setTotal_Chapter(infoBook.total_chapter);
            mBaseBook.setRecentChapter(infoBook.total_chapter);
            mBaseBook.setName(infoBook.name);
            mBaseBook.setUid(Utils.getUID(activity));


            activity_book_info_content_name.setText(infoBook.name);
            activity_book_info_content_author.setText(infoBook.author);
            activity_book_info_content_display_label.setText(infoBook.display_label);
            activity_book_info_content_total_comment.setText(infoBook.hot_num);
            activity_book_info_content_total_shoucanshu.setText(infoBook.total_favors);
            ImageLoader.getInstance().displayImage(infoBook.cover, activity_book_info_content_cover, ReaderApplication.getOptions());

            activity_book_info_content_description.setText(infoBook.description);
            activity_book_info_content_last_chapter_time.setText(infoBook.last_chapter_time);
            activity_book_info_content_last_chapter.setText(infoBook.last_chapter);
            titlebar_text.setText(infoBook.name);
            titlebar_text.setAlpha(0);
            book_info_titlebar_container_shadow.setAlpha(0);
            try {
                Glide.with(this).asBitmap().load(infoBook.cover).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        try {
                            activity_book_info_content_cover.setImageBitmap(resource);
                            // activity_book_info_content_cover_bg.setBackground(BlurImageview.BlurImages(resource, BookInfoActivity.this,activity_book_info_content_cover_bg));

                            BlurImageview.BlurImages(resource, BookInfoActivity.this, activity_book_info_content_cover_bg);
                        } catch (Exception e) {
                        } catch (Error r) {
                        }
                    }
                });
            } catch (Exception e) {
            } catch (Error r) {
            }
            int dp6 = ImageUtil.dp2px(activity, 6);
            int dp3 = ImageUtil.dp2px(activity, 3);

            for (BaseTag tag : infoBook.tag) {
                TextView textView = new TextView(activity);
                textView.setText(tag.getTab());
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                textView.setLines(1);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(dp6, dp3, dp6, dp3);
                textView.setTextColor(Color.parseColor(tag.getColor()));//resources.getColor(R.color.comic_info_tag_text)
                GradientDrawable drawable = new GradientDrawable();
                drawable.setCornerRadius(20);
                drawable.setColor(Color.parseColor("#1A" + tag.getColor().substring(1)));
                textView.setBackground(drawable);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.rightMargin = ImageUtil.dp2px(activity, 10);
                layoutParams.gravity = Gravity.CENTER_VERTICAL;
                activity_book_info_tag.addView(textView, layoutParams);
            }
            if (!infoBookItem.comment.isEmpty()) {
                for (BookInfoComment bookInfoComment : infoBookItem.comment) {

                    LinearLayout commentView = (LinearLayout) layoutInflater.inflate(R.layout.activity_book_info_content_comment_item, null, false);
                    CircleImageView activity_book_info_content_comment_item_avatar = commentView.findViewById(R.id.activity_book_info_content_comment_item_avatar);
                    TextView activity_book_info_content_comment_item_nickname = commentView.findViewById(R.id.activity_book_info_content_comment_item_nickname);
                    TextView activity_book_info_content_comment_item_content = commentView.findViewById(R.id.activity_book_info_content_comment_item_content);
                    TextView activity_book_info_content_comment_item_reply = commentView.findViewById(R.id.activity_book_info_content_comment_item_reply_info);
                    TextView activity_book_info_content_comment_item_time = commentView.findViewById(R.id.activity_book_info_content_comment_item_time);

                    MyPicasso.IoadImage(this, bookInfoComment.getAvatar(), R.mipmap.icon_def_head, activity_book_info_content_comment_item_avatar);


                    activity_book_info_content_comment_item_nickname.setText(bookInfoComment.getNickname());
                    activity_book_info_content_comment_item_content.setText(bookInfoComment.getContent());
                    activity_book_info_content_comment_item_reply.setText(bookInfoComment.getReply_info());
                    activity_book_info_content_comment_item_reply.setVisibility(TextUtils.isEmpty(bookInfoComment.getReply_info()) ? View.GONE : View.VISIBLE);
                    activity_book_info_content_comment_item_time.setText(bookInfoComment.getTime());
                    //评论点击的处理
                    commentView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(BookInfoActivity.this, BookCommentActivity.class);
                            intent.putExtra("book_id", mBookId);
                            intent.putExtra("comment_id", bookInfoComment.getComment_id());
                            //  intent.putExtra("avatar", bookInfoComment.getAvatar());
                            intent.putExtra("nickname", bookInfoComment.getNickname());
                            //  intent.putExtra("origin_content", bookInfoComment.getContent());
                            startActivity(intent);
                        }
                    });

                    activity_book_info_content_comment_container.addView(commentView);

                }
            }
            String moreText;
            if (infoBook.total_comment > 0) {
                moreText = LanguageUtil.getString(activity, R.string.BookInfoActivity_lookpinglun);
            } else {
                moreText = LanguageUtil.getString(activity, R.string.BookInfoActivity_nopinglun);
            }
            LinearLayout commentMoreView = (LinearLayout) layoutInflater.inflate(R.layout.activity_book_info_content_comment_more, null, false);
            TextView activity_book_info_content_comment_more_text = commentMoreView.findViewById(R.id.activity_book_info_content_comment_more_text);
            activity_book_info_content_comment_more_text.setText(String.format(moreText, infoBook.total_comment));
            TextView activity_book_info_content_add_comment = findViewById(R.id.activity_book_info_content_add_comment);
            activity_book_info_content_add_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //写评论
                    Intent intent = new Intent(BookInfoActivity.this, BookCommentActivity.class);
                    intent.putExtra("book_id", mBookId);
                    startActivity(intent);
                }
            });
            commentMoreView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BookInfoActivity.this, BookCommentActivity.class);
                    intent.putExtra("book_id", mBookId);
                    startActivity(intent);
                }
            });
            activity_book_info_content_comment_container.addView(commentMoreView);
            initWaterfall(infoBookItem.label);
            if (basebooks != null) {
                ContentValues values = new ContentValues();
                values.put("total_chapter", infoBook.total_chapter);
                values.put("name", infoBook.name);
                values.put("cover", infoBook.cover);
                values.put("author", infoBook.author);
                values.put("description", infoBook.description);
                LitePal.updateAsync(BaseBook.class, values, basebooks.getId());

            }
            MyToash.Log("advertisement",ReaderConfig.USE_AD+"");

            new TodayOneAD(activity, 2, infoBookItem.advert.ad_android_key).getTodayOneBanner(list_ad_view_layout_chuanshanjia, null, 2);;



        } catch (Exception e) {
        }
    }

    boolean falseDialg;
    BaseBook basebooks;


    public void initData() {
        if (mBookId == null) {
            falseDialg = true;
            try {
                mBookId = getIntent().getStringExtra("book_id");

            } catch (Exception e) {
            }
        } else {
            falseDialg = false;
        }
        if (mBookId == null) {
            return;
        }
        mBaseBook = new BaseBook();
        mBaseBook.setBook_id(mBookId);


        basebooks = LitePal.where("book_id = ?", mBookId).findFirst(BaseBook.class);

        if (basebooks != null) {
            mBaseBook.setAddBookSelf(basebooks.isAddBookSelf());
            mBaseBook.setCurrent_chapter_id(basebooks.getCurrent_chapter_id());
            mBaseBook.setChapter_text(basebooks.getChapter_text());
            mBaseBook.setId(basebooks.getId());

        } else {
            mBaseBook.setAddBookSelf(0);
        }
        if (mBaseBook.isAddBookSelf() == 1) {
            activity_book_info_add_shelf.setText(LanguageUtil.getString(this, R.string.BookInfoActivity_jiarushujias));
            activity_book_info_add_shelf.setTextColor(Color.parseColor("#7F22b48d"));
            activity_book_info_add_shelf.setEnabled(false);
        } else {
            activity_book_info_add_shelf.setText(LanguageUtil.getString(this, R.string.BookInfoActivity_jiarushujia));
            activity_book_info_add_shelf.setTextColor(ContextCompat.getColor(activity, R.color.mainColor));
            activity_book_info_add_shelf.setEnabled(true);
        }

        ReaderParams params = new ReaderParams(this);
        params.putExtraParams("book_id", mBookId);
        String json = params.generateParamsJson();
        HttpUtils.getInstance(BookInfoActivity.this).sendRequestRequestParams3(ReaderConfig.mBookInfoUrl, json, falseDialg, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String result) {
                        try {
                            initInfo(result);
                        } catch (Exception e) {
                        }

                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );


    }

    boolean onclickTwo = false;

    public void addBookToLocalShelf() {
        if (mBaseBook.isAddBookSelf() == 0) {
            mBaseBook.saveIsexist(1);
            activity_book_info_add_shelf.setText(LanguageUtil.getString(this, R.string.BookInfoActivity_jiarushujias));
            activity_book_info_add_shelf.setTextColor(Color.parseColor("#7F22b48d"));
            activity_book_info_add_shelf.setEnabled(false);

            List<BaseBook> list = new ArrayList<>();
            list.add(mBaseBook);
            EventBus.getDefault().post(new RefreshBookSelf(list));
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        BaseBook basebooks = LitePal.where("book_id = ?", mBookId).findFirst(BaseBook.class);
        if (basebooks != null) {
            mBaseBook.setCurrent_chapter_id(basebooks.getCurrent_chapter_id());
            mBaseBook.setChapter_text(basebooks.getChapter_text());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshBookInfo refreshBookInfo) {
        if (refreshBookInfo.isSave) {
            mBaseBook.setAddBookSelf(1);
            activity_book_info_add_shelf.setText(LanguageUtil.getString(this, R.string.BookInfoActivity_jiarushujias));
            activity_book_info_add_shelf.setTextColor(Color.parseColor("#7F22b48d"));
            activity_book_info_add_shelf.setEnabled(false);
        } else {
            activity_book_info_content_label_container.removeAllViews();
            activity_book_info_content_comment_container.removeAllViews();
            //加载
            initData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        onclickTwo = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (MainActivity.activity == null) {
                startActivity(new Intent(BookInfoActivity.this, MainActivity.class));
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void initWaterfall(List<StroreBookcLable> stroreBookcLables) {
        activity_book_info_content_label_container.removeAllViews();

        for (StroreBookcLable stroreComicLable : stroreBookcLables) {
            int Size = stroreComicLable.list.size();
            if (Size == 0) {
                continue;
            }
            View type3 = layoutInflater.inflate(R.layout.lable_bookinfo_layout, null, false);
            TextView fragment_store_gridview3_text = type3.findViewById(R.id.fragment_store_gridview3_text);
            fragment_store_gridview3_text.setText(stroreComicLable.label);
            AdaptionGridView fragment_store_gridview3_gridview_first = type3.findViewById(R.id.fragment_store_gridview3_gridview_first);
            fragment_store_gridview3_gridview_first.setHorizontalSpacing(HorizontalSpacing);
            fragment_store_gridview3_gridview_first.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(activity, BookInfoActivity.class);
                    intent.putExtra("book_id", stroreComicLable.list.get(position).getBook_id());
                    activity.startActivity(intent);
                }
            });
            int minSize = 0;
            int ItemHeigth = 0, start = 0;
            if (stroreComicLable.style == 2) {
                minSize = Math.min(Size, 6);
                if (minSize > 3) {
                    ItemHeigth = H100 + (HEIGHT + H50) * 2;
                } else {
                    ItemHeigth = H100 + HEIGHT + H50;
                }
            } else {
                minSize = Math.min(Size, 3);
                ItemHeigth = H100 + HEIGHT + H50;
            }
            List<StroreBookcLable.Book> firstList = stroreComicLable.list.subList(start, minSize);
            VerticalAdapter verticalAdapter = new VerticalAdapter(activity, firstList, WIDTH, HEIGHT, false);
            fragment_store_gridview3_gridview_first.setAdapter(verticalAdapter);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, H20, 0, 0);
            params.height = ItemHeigth + H20;
            activity_book_info_content_label_container.addView(type3, params);
        }


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
