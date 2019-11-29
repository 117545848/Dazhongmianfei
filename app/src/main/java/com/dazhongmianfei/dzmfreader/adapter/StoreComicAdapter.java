package com.dazhongmianfei.dzmfreader.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.bean.BaseTag;
import com.dazhongmianfei.dzmfreader.comic.adapter.ComicDownOptionAdapter;
import com.dazhongmianfei.dzmfreader.comic.been.StroreComicLable;
import com.dazhongmianfei.dzmfreader.config.ReaderApplication;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyPicasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by scb on 2018/10/28.
 */

public class StoreComicAdapter extends BaseAdapter {

    private List<StroreComicLable.Comic> taskCenter2s;
    private Activity activity;
    private LayoutInflater layoutInflater;
    private int style;
    private int WIDTH, HEIGHT, height;

    public StoreComicAdapter(List<StroreComicLable.Comic> taskCenter2s, Activity activity, int style, int WIDTH, int HEIGHT) {
        this.taskCenter2s = taskCenter2s;
        this.layoutInflater = LayoutInflater.from(activity);
        this.style = style;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        height = ImageUtil.dp2px(activity, 55);
        this.activity = activity;

    }

    @Override
    public int getCount() {
        return taskCenter2s.size();
    }

    @Override
    public StroreComicLable.Comic getItem(int i) {
        return taskCenter2s.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.liem_store_comic_style1, null, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        StroreComicLable.Comic comic = getItem(i);
        if (style == 1 || style == 3) {
            MyPicasso.GlideImage(activity, comic.horizontal_cover ,viewHolder.liem_store_comic_style1_img, WIDTH, HEIGHT);
        } else {
            MyPicasso.GlideImage(activity, comic.vertical_cover, viewHolder.liem_store_comic_style1_img, WIDTH, HEIGHT);
        }
        viewHolder.liem_store_comic_style1_flag.setText(comic.flag);
        viewHolder.liem_store_comic_style1_name.setText(comic.name);
        if (comic.description != null) {
            viewHolder.liem_store_comic_style1_description.setText(comic.description);
        } else if (comic.tag != null && !comic.tag.isEmpty()) {
            String str = "";
            for (BaseTag tag : comic.tag) {
                str += tag.tab + "  ";
            }
            viewHolder.liem_store_comic_style1_description.setText(str);
        } else {
            viewHolder.liem_store_comic_style1_description.setVisibility(View.GONE);
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.liem_store_comic_style1_layout.getLayoutParams();
        layoutParams.height = HEIGHT + height;
        viewHolder.liem_store_comic_style1_layout.setLayoutParams(layoutParams);
        return convertView;
    }

    class ViewHolder {
        @BindView(R2.id.liem_store_comic_style1_layout)
        LinearLayout liem_store_comic_style1_layout;
        @BindView(R2.id.liem_store_comic_style1_img)
        ImageView liem_store_comic_style1_img;

        @BindView(R2.id.liem_store_comic_style1_flag)
        TextView liem_store_comic_style1_flag;
        @BindView(R2.id.liem_store_comic_style1_name)
        TextView liem_store_comic_style1_name;
        @BindView(R2.id.liem_store_comic_style1_description)
        TextView liem_store_comic_style1_description;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
