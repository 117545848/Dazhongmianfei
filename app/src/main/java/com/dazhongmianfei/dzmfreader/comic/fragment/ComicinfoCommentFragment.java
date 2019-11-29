package com.dazhongmianfei.dzmfreader.comic.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.activity.WebViewActivity;
import com.dazhongmianfei.dzmfreader.adapter.StoreComicAdapter;
import com.dazhongmianfei.dzmfreader.bean.BaseAd;
import com.dazhongmianfei.dzmfreader.bean.BookInfoComment;
import com.dazhongmianfei.dzmfreader.comic.activity.ComicCommentActivity;
import com.dazhongmianfei.dzmfreader.comic.activity.ComicInfoActivity;

import com.dazhongmianfei.dzmfreader.comic.been.StroreComicLable;

import com.dazhongmianfei.dzmfreader.config.ReaderApplication;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.fragment.BaseButterKnifeFragment;
import com.dazhongmianfei.dzmfreader.jinritoutiao.TodayOneAD;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyPicasso;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;
import com.dazhongmianfei.dzmfreader.view.AdaptionGridViewNoMargin;
import com.dazhongmianfei.dzmfreader.view.CircleImageView;
import com.dazhongmianfei.dzmfreader.view.ObservableScrollView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by scb on 2018/6/9.
 */

public class ComicinfoCommentFragment extends BaseButterKnifeFragment {
    @Override
    public int initContentView() {
        return R.layout.fragment_comicinfo_comment;
    }

    /**
     * 评论的视图容器
     */
    @BindView(R2.id.activity_book_info_content_comment_container)
    public LinearLayout activity_book_info_content_comment_container;
    @BindView(R2.id.activity_book_info_scrollview)
    public ObservableScrollView activity_book_info_scrollview;
    @BindView(R2.id.activity_book_info_content_comment_des)
    public TextView activity_book_info_content_comment_des;

    /**
     * 精品推荐的视图容器
     */
    @BindView(R2.id.activity_book_info_content_label_container)
    public LinearLayout activity_book_info_content_label_container;
    @BindView(R2.id.list_ad_view_layout)
    FrameLayout activity_book_info_ad;
    @BindView(R2.id.list_ad_view_img)
    ImageView list_ad_view_img;

    @BindView(R2.id.list_ad_view_layout_chuanshanjia)
    FrameLayout list_ad_view_layout_chuanshanjia;

    @BindView(R2.id.list_ad_layout)
    LinearLayout list_ad_layout;


    @BindView(R2.id.activity_book_info_content_add_comment)
    public TextView activity_book_info_content_add_comment;
    public int WIDTH, HEIGHT;

    //  List<BookInfoComment> bookInfoComment;
    StroreComicLable.Comic baseComic;


    // List<StroreComicLable.Comic> comicList;
    public void senddata(StroreComicLable.Comic baseComic, List<BookInfoComment> bookInfoComments, StroreComicLable stroreComicLable,   BaseAd baseAd) {
        MyToash.Log("http_utaa", bookInfoComments.toString());

        this.baseComic = baseComic;
        activity_book_info_content_comment_des.setText(baseComic.description);


        if (ReaderConfig.USE_AD && baseAd != null) {
            if (baseAd.ad_type == 2) {
                activity_book_info_ad.setVisibility(View.GONE);
                new TodayOneAD(activity, 2).getTodayOneBanner(list_ad_view_layout_chuanshanjia, null, 2);
            } else {
                list_ad_view_layout_chuanshanjia.setVisibility(View.GONE);
                activity_book_info_ad.setVisibility(View.VISIBLE);
                activity_book_info_ad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(activity, WebViewActivity.class);
                        intent.putExtra("url", baseAd.ad_skip_url);
                        intent.putExtra("title", baseAd.ad_title);
                        intent.putExtra("advert_id", baseAd.advert_id);

                        activity.startActivity(intent);
                    }
                });
                ImageLoader.getInstance().displayImage(baseAd.ad_image, list_ad_view_img, ReaderApplication.getOptions());
            }
        } else {
            list_ad_layout.setVisibility(View.GONE);
        }


        activity_book_info_content_label_container.removeAllViews();
        activity_book_info_content_comment_container.removeAllViews();
        try {
            if (bookInfoComments != null || !bookInfoComments.isEmpty()) {
                for (BookInfoComment bookInfoComment : bookInfoComments) {

                    LinearLayout commentView = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.activity_book_info_content_comment_item, null, false);
                    CircleImageView activity_book_info_content_comment_item_avatar = commentView.findViewById(R.id.activity_book_info_content_comment_item_avatar);
                    TextView activity_book_info_content_comment_item_nickname = commentView.findViewById(R.id.activity_book_info_content_comment_item_nickname);
                    TextView activity_book_info_content_comment_item_content = commentView.findViewById(R.id.activity_book_info_content_comment_item_content);
                    TextView activity_book_info_content_comment_item_reply = commentView.findViewById(R.id.activity_book_info_content_comment_item_reply_info);
                    TextView activity_book_info_content_comment_item_time = commentView.findViewById(R.id.activity_book_info_content_comment_item_time);
                    MyPicasso.IoadImage(activity, bookInfoComment.getAvatar(), R.mipmap.icon_def_head, activity_book_info_content_comment_item_avatar);


                    //  ImageLoader.getInstance().displayImage(bookInfoComment.getAvatar(), activity_book_info_content_comment_item_avatar, ReaderApplication.getOptions());


                    activity_book_info_content_comment_item_nickname.setText(bookInfoComment.getNickname());
                    activity_book_info_content_comment_item_content.setText(bookInfoComment.getContent());
                    activity_book_info_content_comment_item_reply.setText(bookInfoComment.getReply_info());
                    activity_book_info_content_comment_item_reply.setVisibility(TextUtils.isEmpty(bookInfoComment.getReply_info()) ? View.GONE : View.VISIBLE);
                    activity_book_info_content_comment_item_time.setText(bookInfoComment.getTime());
                    //评论点击的处理
                    commentView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, ComicCommentActivity.class);
                            intent.putExtra("comic_id", baseComic.comic_id);
                            intent.putExtra("comment_id", bookInfoComment.getComment_id());
                           // intent.putExtra("avatar", bookInfoComment.getAvatar());
                           // intent.putExtra("nickname", bookInfoComment.getNickname());
                           // intent.putExtra("origin_content", bookInfoComment.getContent());
                            startActivity(intent);
                        }
                    });


                    activity_book_info_content_comment_container.addView(commentView);


                }
            }

            //"查看全部评论"
            String moreText;
            if (baseComic.total_comment > 0) {
                moreText = LanguageUtil.getString(activity, R.string.BookInfoActivity_lookpinglun);
            } else {
                moreText = LanguageUtil.getString(activity, R.string.BookInfoActivity_nopinglun);
            }
            LinearLayout commentMoreView = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.activity_book_info_content_comment_more, null, false);
            TextView activity_book_info_content_comment_more_text = commentMoreView.findViewById(R.id.activity_book_info_content_comment_more_text);

            activity_book_info_content_comment_more_text.setText(String.format(moreText, baseComic.total_comment + ""));

            activity_book_info_content_add_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(
                            new Intent(activity, ComicCommentActivity.class).
                                    putExtra("comic_id", baseComic.comic_id).
                                    putExtra("IsBook", false), 11);

                }
            });
            commentMoreView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(activity, ComicCommentActivity.class).putExtra("comic_id", baseComic.comic_id).putExtra("IsBook", false), 11);

                }
            });
            activity_book_info_content_comment_container.addView(commentMoreView);

            //3 推荐数据的处理
            //   JSONArray labelArr = jsonObject.getJSONObject("data").getJSONArray("label");
            if (stroreComicLable != null && !stroreComicLable.list.isEmpty()) {
                List<StroreComicLable.Comic> comicList = stroreComicLable.list;
                View type1 = LayoutInflater.from(activity).inflate(R.layout.fragment_store_comic_layout, null, false);
                TextView lable = type1.findViewById(R.id.fragment_store_gridview1_text);
                lable.setText(stroreComicLable.label);

                //  TextView fragment_store_gridview1_more = type1.findViewById(R.id.fragment_store_gridview1_more);
                //  View right_arrow = type1.findViewById(R.id.right_arrow);
                // fragment_store_gridview1_more.setVisibility(View.GONE);
                //   right_arrow.setVisibility(View.GONE);
                LinearLayout fragment_store_gridview1_huanmore = type1.findViewById(R.id.fragment_store_gridview1_huanmore);
                fragment_store_gridview1_huanmore.setVisibility(View.GONE);
                AdaptionGridViewNoMargin fragment_store_gridview1_gridview = type1.findViewById(R.id.fragment_store_gridview1_gridview);
                StoreComicAdapter storeComicAdapter;
                fragment_store_gridview1_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent(activity, ComicInfoActivity.class);
                        intent.putExtra("comic_id", comicList.get(position).comic_id);
                        activity.startActivity(intent);
                    }
                });
                fragment_store_gridview1_gridview.setNumColumns(3);
                int width = WIDTH / 3;
                int height = width * 4 / 3;
                double size = Math.min(6, comicList.size());
                storeComicAdapter = new StoreComicAdapter(comicList.subList(0, (int) size), activity, 2, width, height);
                fragment_store_gridview1_gridview.setAdapter(storeComicAdapter);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 20, 0, 0);
                params.height = height * (int) (Math.ceil(size / 3d)) + ImageUtil.dp2px(activity, 170);
                activity_book_info_content_label_container.addView(type1, params);
            }


        } catch (Exception e) {
        }

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        WIDTH = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        HEIGHT = ScreenSizeUtils.getInstance(activity).getScreenHeight();
    }


}
