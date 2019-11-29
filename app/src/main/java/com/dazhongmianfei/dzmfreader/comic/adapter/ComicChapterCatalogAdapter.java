package com.dazhongmianfei.dzmfreader.comic.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.comic.been.ComicChapter;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyPicasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//.view.annotation.ViewInject;
//.x;

public class ComicChapterCatalogAdapter extends BaseAdapter {
    Activity activity;
    public List<ComicChapter> comicChapterCatalogList;
    public int size;
    LayoutInflater layoutInflater;
    int width, height, H96;
    private String currentChapterId;
    private int CurrentPosition;

    public String getCurrentChapterId() {
        return currentChapterId;
    }

    public void setCurrentChapterId(String currentChapterId) {
        this.currentChapterId = currentChapterId;
    }

    public void setCurrentPosition(int currentPosition) {
        CurrentPosition = currentPosition;
    }

    public ComicChapterCatalogAdapter(Activity activity, String currentChapterId, List<ComicChapter> comicChapterCatalogList, int H96) {
        this.activity = activity;
        size = comicChapterCatalogList.size();
        layoutInflater = LayoutInflater.from(activity);
        this.comicChapterCatalogList = comicChapterCatalogList;
        width = ImageUtil.dp2px(activity, 120);
        height = ImageUtil.dp2px(activity, 80);
        this.H96 = H96;

        if (currentChapterId != null) {
            this.currentChapterId = currentChapterId;
        } else {
            this.currentChapterId = comicChapterCatalogList.get(0).chapter_id;
        }
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public ComicChapter getItem(int i) {
        return comicChapterCatalogList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public int getCurrentPosition() {


        return CurrentPosition;
    }

    ;

    @Override
    public View getView(int i, View contentView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (contentView == null) {
            contentView = LayoutInflater.from(activity).inflate(R.layout.item_comicchaptercatalog, null, false);
            viewHolder = new ViewHolder(contentView);
            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }
        final ComicChapter comicChapterCatalog = getItem(i);
        MyPicasso.GlideImage(activity, comicChapterCatalog.small_cover, viewHolder.item_comicchaptercatalog_img, width, height);
        viewHolder.item_comicchaptercatalog_time.setText(comicChapterCatalog.subtitle);
        viewHolder.item_comicchaptercatalog_name.setText(comicChapterCatalog.chapter_title);




        if (comicChapterCatalog.is_preview == 0) {
            viewHolder.item_comicchaptercatalog_needbuy.setVisibility(View.GONE);
        } else {
            viewHolder.item_comicchaptercatalog_needbuy.setVisibility(View.VISIBLE);
        }
        if (comicChapterCatalog.isRead()) {
            viewHolder.item_comicchaptercatalog_current_bg.setBackgroundColor(activity.getResources().getColor(R.color.lightgray2));
        } else {
            viewHolder.item_comicchaptercatalog_current_bg.setBackgroundColor(Color.WHITE);

        }
        if (comicChapterCatalog.chapter_id.equals(currentChapterId)) {
            CurrentPosition = i;
            viewHolder.item_comicchaptercatalog_current.setVisibility(View.VISIBLE);
        } else {
            viewHolder.item_comicchaptercatalog_current.setVisibility(View.GONE);
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewHolder.item_comicchaptercatalog_current_bg.getLayoutParams();
        layoutParams.height = H96;
        viewHolder.item_comicchaptercatalog_current_bg.setLayoutParams(layoutParams);
        return contentView;
    }


    class ViewHolder {
        @BindView(R2.id.item_comicchaptercatalog_current_bg)
        public LinearLayout item_comicchaptercatalog_current_bg;
        @BindView(R2.id.item_comicchaptercatalog_img)
        public ImageView item_comicchaptercatalog_img;
        @BindView(R2.id.item_comicchaptercatalog_current)
        public ImageView item_comicchaptercatalog_current;
        @BindView(R2.id.item_comicchaptercatalog_name)
        public TextView item_comicchaptercatalog_name;
        @BindView(R2.id.item_comicchaptercatalog_time)
        public TextView item_comicchaptercatalog_time;

        @BindView(R2.id.item_comicchaptercatalog_needbuy)
        public RelativeLayout item_comicchaptercatalog_needbuy;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
