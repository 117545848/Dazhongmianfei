package com.dazhongmianfei.dzmfreader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.bean.EntranceItem;

import java.util.List;

/**
 * 书城banner下方的gridview的adapter
 */
public class EntranceAdapter extends ReaderBaseAdapter<EntranceItem> {


    public EntranceAdapter(Context context, List<EntranceItem> list,int count) {
        super(context, list,count);
    }

    @Override
    public View getOwnView(int position, View convertView, ViewGroup parent) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.item_store_entrance_male, null, false);
        ImageView imageView = contentView.findViewById(R.id.item_store_entrance_male_img);
        TextView textView = contentView.findViewById(R.id.item_store_entrance_male_text);
        imageView.setImageResource(mList.get(position).getResId());
        textView.setText(mList.get(position).getName());
        return contentView;
    }

}
