package com.dazhongmianfei.dzmfreader.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.bean.RechargeItem;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;

import java.util.List;

/**
 * Created by scb on 2018/8/12.
 */
public class RechargeAdapter extends ReaderBaseAdapter<RechargeItem> {
    public RechargeAdapter(Context context, List<RechargeItem> list, int count) {
        super(context, list, count);
    }

    public RechargeAdapter(Context context, List<RechargeItem> list, int count, boolean close) {
        super(context, list, count, close);
    }

    @Override
    public View getOwnView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_recharge_new, null, false);
            viewHolder.item_recharge_price = convertView.findViewById(R.id.item_recharge_price);
            viewHolder.item_recharge_title = convertView.findViewById(R.id.item_recharge_title);
            viewHolder.item_recharge_note = convertView.findViewById(R.id.item_recharge_note);
            viewHolder.item_recharge_flag = convertView.findViewById(R.id.item_recharge_flag);
            viewHolder.item_recharge_layout = convertView.findViewById(R.id.item_recharge_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RechargeItem rechargeItem = mList.get(position);
        if (rechargeItem.choose) {
            viewHolder.item_recharge_layout.setBackgroundResource(R.drawable.shape_recharge_item_yes);
            viewHolder.item_recharge_price.setTextColor(Color.WHITE);
            viewHolder.item_recharge_title.setTextColor(Color.WHITE);

        } else {
            viewHolder.item_recharge_layout.setBackgroundResource(R.drawable.shape_recharge_item_no);
            viewHolder.item_recharge_price.setTextColor(Color.parseColor("#D5B66E"));
            viewHolder.item_recharge_title.setTextColor(mContext.getResources().getColor(R.color.gray4));

        }
        String str = "￥" + mList.get(position).getPrice();
        Spannable span = new SpannableString(str);
        span.setSpan(new AbsoluteSizeSpan(ImageUtil.dp2px(mContext, 18)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        viewHolder.item_recharge_price.setText(span);
        viewHolder.item_recharge_title.setText(mList.get(position).getTitle() + mList.get(position).getNote());

        //   viewHolder.item_recharge_note.setText(TextUtils.isEmpty(mList.get(position).getNote()) ? "" : "(" + mList.get(position).getNote() + ")");
        //  viewHolder.item_recharge_flag.setText(mList.get(position).getFlag());

        return convertView;
    }

    class ViewHolder {
        TextView item_recharge_price;
        TextView item_recharge_title;
        TextView item_recharge_note;
        TextView item_recharge_flag;
        RelativeLayout item_recharge_layout;

    }
}
