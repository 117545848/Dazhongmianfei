package com.dazhongmianfei.dzmfreader.adapter;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;

import java.util.List;

/**
 * 搜索页上方的热词的adapter
 */
public class HotWordsAdapter extends BaseAdapter {
    Activity activity;
    String[] strings;

    public HotWordsAdapter(Activity activity, String[] strings) {
        this.activity = activity;
        this.strings = strings;
    }
    @Override
    public int getCount() {
        return strings.length;
    }

    @Override
    public String  getItem(int position) {
        return strings[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View contentView = LayoutInflater.from(activity).inflate(R.layout.item_hot_words, null, false);
        LinearLayout container = contentView.findViewById(R.id.item_hot_word_container);
        TextView index = container.findViewById(R.id.item_hot_word_index);
        index.setText((position + 1) + "");
        if (position == 0) {
            container.setBackgroundResource(R.drawable.shape_hotword_1);
        } else if (position == 1) {
            container.setBackgroundResource(R.drawable.shape_hotword_2);
        } else if (position == 2) {
            container.setBackgroundResource(R.drawable.shape_hotword_3);
        } else {
            container.setBackgroundResource(R.drawable.shape_hotword_4);
        }
        TextView content = contentView.findViewById(R.id.item_hot_word_content);
        content.setText(strings[position]);
        return contentView;
    }
}
