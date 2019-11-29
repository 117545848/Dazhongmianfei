package com.dazhongmianfei.dzmfreader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.bean.AcquirePrivilegeItem;
import com.dazhongmianfei.dzmfreader.config.ReaderApplication;

import java.util.List;

/**
 * Created by scb on 2018/8/12.
 */
public class AcquireBaoyuePrivilegeAdapter extends ReaderBaseAdapter<AcquirePrivilegeItem> {
    public AcquireBaoyuePrivilegeAdapter(Context context, List<AcquirePrivilegeItem> list, int count) {
        super(context, list, count);
    }

    public AcquireBaoyuePrivilegeAdapter(Context context, List<AcquirePrivilegeItem> list, int count, boolean close) {
        super(context, list, count, close);
    }

    @Override
    public View getOwnView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_acquire_privilege, null, false);
            viewHolder.item_acquire_privilege_img= convertView.findViewById(R.id.item_acquire_privilege_img);
            viewHolder.item_acquire_privilege_title= convertView.findViewById(R.id.item_acquire_privilege_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageLoader.getInstance().displayImage(mList.get(position).getIcon(),viewHolder.item_acquire_privilege_img, ReaderApplication.getOptions());
        viewHolder.item_acquire_privilege_title.setText(mList.get(position).getLabel());

        return convertView;
    }

    class ViewHolder {
        ImageView item_acquire_privilege_img;
        TextView item_acquire_privilege_title;
    }
}
