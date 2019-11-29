package com.dazhongmianfei.dzmfreader.comic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.adapter.ReaderBaseAdapter;
import com.dazhongmianfei.dzmfreader.bean.EntranceItem;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyPicasso;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;

import java.util.List;

/**
 * 书城banner下方的gridview的adapter
 */
public class StoreEntranceAdapter /*extends ReaderBaseAdapter<EntranceItem> */{

/*
    public StoreEntranceAdapter(Context context, List<EntranceItem> list, int count) {
        super(context, list,count);
    }

    @Override
    public View getOwnView(int position, View convertView, ViewGroup parent) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.item_store_entrance_comic, null, false);

        ImageView item_store_entrance_comic_bg = contentView.findViewById(R.id.item_store_entrance_comic_bg);
        ImageView item_store_entrance_comic_img = contentView.findViewById(R.id.item_store_entrance_comic_img);

        MyPicasso.GlideImageRoundedCorners(mContext, mList.get(position).setResId();, item_store_entrance_comic_bg, ScreenSizeUtils.getInstance(activity).getScreenWidth(), ImageUtil.dp2px(activity, 250));
        MyPicasso.GlideImageRoundedGasoMohu(mContext, comic.horizontal_cover, item_store_entrance_comic_img, ScreenSizeUtils.getInstance(activity).getScreenWidth(), ImageUtil.dp2px(activity, 205));


        TextView textView = contentView.findViewById(R.id.item_store_entrance_male_text);
        imageView.setImageResource(mList.get(position).getResId());
        textView.setText(mList.get(position).getName());

        return contentView;
    }*/

}
