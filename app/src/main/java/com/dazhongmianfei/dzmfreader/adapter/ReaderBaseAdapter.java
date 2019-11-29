package com.dazhongmianfei.dzmfreader.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 书城多个gridview的adapter基类
 *
 * @param <T>
 */
public abstract class ReaderBaseAdapter<T> extends BaseAdapter {

    public Context mContext;
    public List<T> mList;
    private int mCount;
    private boolean mClose = true;
    int size;
    public ReaderBaseAdapter(Context context, List<T> list, int count) {
        mContext = context;
        mList = list;
        size = list.size();
        mCount = count;
    }

    public ReaderBaseAdapter(Context context, List<T> list, int count, boolean close) {
        mContext = context;
        mList = list;
        mCount = count;
        mClose = close;
        size = list.size();
    }

    @Override
    public int getCount() {
        if (mClose) {
            return size > mCount ? mCount : size;
        } else {
            return size;
        }

    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getOwnView(position, convertView, parent);
    }

    public abstract View getOwnView(int position, View convertView, ViewGroup parent);

}
