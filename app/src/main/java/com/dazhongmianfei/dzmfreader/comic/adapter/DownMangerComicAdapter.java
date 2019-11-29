package com.dazhongmianfei.dzmfreader.comic.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.bean.Downoption;
import com.dazhongmianfei.dzmfreader.book.been.BaseBook;
import com.dazhongmianfei.dzmfreader.comic.activity.ComicDownActivity;
import com.dazhongmianfei.dzmfreader.comic.activity.ComicInfoActivity;
import com.dazhongmianfei.dzmfreader.comic.activity.ComicLookActivity;
import com.dazhongmianfei.dzmfreader.comic.been.BaseComic;
import com.dazhongmianfei.dzmfreader.comic.been.ComicChapter;
import com.dazhongmianfei.dzmfreader.config.ReaderApplication;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.dialog.DownDialog;
import com.dazhongmianfei.dzmfreader.read.manager.ChapterManager;
import com.dazhongmianfei.dzmfreader.utils.FileManager;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyPicasso;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;

import org.litepal.LitePal;
//.view.annotation.ViewInject;
//.x;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownMangerComicAdapter extends BaseAdapter {
    private List<BaseComic> list;
    private Activity activity;
    private LinearLayout fragment_bookshelf_noresult;
    private int WIDTH;

    public DownMangerComicAdapter(Activity activity, List<BaseComic> list, LinearLayout fragment_bookshelf_noresult) {
        this.list = list;
        this.fragment_bookshelf_noresult = fragment_bookshelf_noresult;
        this.activity = activity;
        WIDTH = ScreenSizeUtils.getInstance(activity).getScreenWidth();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public BaseComic getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View contentView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (contentView == null) {
            contentView = LayoutInflater.from(activity).inflate(R.layout.item_downmangercomic, null, false);
            viewHolder = new ViewHolder(contentView);
            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }
        final BaseComic baseComic = getItem(i);
        viewHolder.item_dowmmanger_LinearLayout2.getLayoutParams().width = WIDTH;/*+ holder.rl_left.getLayoutParams().width*/
        viewHolder.item_dowmmanger_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, ComicLookActivity.class);
                intent.putExtra("baseComic", baseComic);
                //intent.putExtra("comicChapter", (Serializable) LitePal.where("comic_id = ?", baseComic.getComic_id()).find(ComicChapter.class));
                activity.startActivity(intent);
            }
        });
        viewHolder.item_dowmmanger_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, ComicDownActivity.class);
                intent.putExtra("baseComic", baseComic);
                intent.putExtra("flag", true);//只查看已下载
                activity.startActivity(intent);
            }
        });
        viewHolder.item_dowmmanger_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, ComicInfoActivity.class);
                intent.putExtra("baseComic", baseComic);
                activity.startActivity(intent);
            }
        });

        viewHolder.item_dowmmanger_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values1 = new ContentValues();
                values1.put("down_chapters", 0);
                LitePal.update(BaseComic.class, values1, baseComic.getId());
                List<ComicChapter> comicChapterList = LitePal.where().find(ComicChapter.class);
                for (ComicChapter comicChapter : comicChapterList) {
                    ShareUitls.putComicDownStatus(activity, comicChapter.chapter_id, 0);
                }
                String localPath = FileManager.getManhuaSDCardRoot().concat(baseComic.getComic_id());
                FileManager.deleteFile(localPath);
                list.remove(baseComic);
                notifyDataSetChanged();
                if (list.isEmpty()) {
                    fragment_bookshelf_noresult.setVisibility(View.VISIBLE);
                }
            }
        });

        MyPicasso.GlideImage(activity, baseComic.vertical_cover, viewHolder.item_dowmmanger_cover, ImageUtil.dp2px(activity, 113), ImageUtil.dp2px(activity, 150));
        viewHolder.item_dowmmanger_name.setText(baseComic.getName());

        viewHolder.item_dowmmanger_xiazaiprocess.setText(String.format(LanguageUtil.getString(activity,R.string.ComicDownActivity_downprocess), baseComic.getDown_chapters()+"/"+baseComic.getTotal_chapters()));
      //  viewHolder.item_dowmmanger_open.setText(String.format(LanguageUtil.getString(activity,R.string.ComicDownActivity_xuhandi), baseComic.getCurrent_display_order()));


       return contentView;
    }


    class ViewHolder {
        @BindView(R2.id.item_dowmmanger_LinearLayout2)
        public LinearLayout item_dowmmanger_LinearLayout2;
        @BindView(R2.id.item_dowmmanger_cover)
        public ImageView item_dowmmanger_cover;
        @BindView(R2.id.item_dowmmanger_name)
        public TextView item_dowmmanger_name;
        @BindView(R2.id.item_dowmmanger_open)
        public TextView item_dowmmanger_open;
        @BindView(R2.id.item_dowmmanger_delete)
        public TextView item_dowmmanger_delete;
        @BindView(R2.id.item_dowmmanger_xiazaiprocess)
        public TextView item_dowmmanger_xiazaiprocess;
        @BindView(R2.id.item_dowmmanger_info)
        public RelativeLayout item_dowmmanger_info;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
