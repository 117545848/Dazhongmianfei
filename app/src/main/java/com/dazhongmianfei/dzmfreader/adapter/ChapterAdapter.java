package com.dazhongmianfei.dzmfreader.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.bean.ChapterItem;

import java.util.List;

/**
 * 作品章节目录的adapter
 */
public class ChapterAdapter extends ReaderBaseAdapter<ChapterItem> {

    public String current_chapter_id;
    public int mDisplayOrder;
    public ChapterAdapter(Context context, List<ChapterItem> list, int count) {
        super(context, list, count);
    }


    @Override
    public View getOwnView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_chapter_catalog, null, false);
            viewHolder.title = convertView.findViewById(R.id.item_chapter_catalog_title);
            viewHolder.vip = convertView.findViewById(R.id.item_chapter_catalog_vip);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ChapterItem chapterItem = (mList.get(position));//chapterItem.getChapter_id().equals(current_chapter_id)
        if (chapterItem.getChapter_id().equals(current_chapter_id)) {
            viewHolder.title.setTextColor(mContext.getResources().getColor(R.color.mainColor));
        }else {
            viewHolder.title.setTextColor(Color.BLACK);
        }
        viewHolder.title.setText(chapterItem.getChapter_title());
        viewHolder.vip.setText(chapterItem.getChaptertab());
        viewHolder.vip.setTextColor(Color.parseColor(chapterItem.getChaptercolor()));
        return convertView;
    }

    class ViewHolder {
        TextView title;
        TextView vip;
    }

}
