package com.dazhongmianfei.dzmfreader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.bean.RankItem;
import com.dazhongmianfei.dzmfreader.config.ReaderApplication;

import java.util.List;

/**
 * 书城banner下方的gridview的adapter
 */
public class RankAdapter extends ReaderBaseAdapter<RankItem> {


    public RankAdapter(Context context, List<RankItem> list, int count) {
        super(context, list, count);
    }

    @Override
    public View getOwnView(int position, View convertView, ViewGroup parent) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.item_store_rank_male, null, false);
        ImageView img1 = contentView.findViewById(R.id.item_store_rank_male_img_1);
        ImageView img2 = contentView.findViewById(R.id.item_store_rank_male_img_2);
        ImageView img3 = contentView.findViewById(R.id.item_store_rank_male_img_3);
        TextView name = contentView.findViewById(R.id.item_store_rank_male_text);
        TextView description = contentView.findViewById(R.id.item_store_rank_male_description);

        if (mList.get(position).getIcon().size() > 0) {
            ImageLoader.getInstance().displayImage(mList.get(position).getIcon().get(0), img1, ReaderApplication.getOptions());
        }
        if (mList.get(position).getIcon().size() > 1) {
            ImageLoader.getInstance().displayImage(mList.get(position).getIcon().get(1), img2, ReaderApplication.getOptions());
        }
        if (mList.get(position).getIcon().size() > 2) {
            ImageLoader.getInstance().displayImage(mList.get(position).getIcon().get(2), img3, ReaderApplication.getOptions());
        }

        name.setText(mList.get(position).getList_name());
        description.setText(mList.get(position).getDescription());
        return contentView;
    }

}
