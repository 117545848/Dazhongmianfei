package com.dazhongmianfei.dzmfreader.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.activity.PayActivity;
import com.dazhongmianfei.dzmfreader.bean.AcquirePayItem;

import java.util.List;

/**
 * Created by scb on 2018/8/12.
 */
public class AcquireBaoyuePayAdapter extends ReaderBaseAdapter<AcquirePayItem> {
    public AcquireBaoyuePayAdapter(Context context, List<AcquirePayItem> list, int count) {
        super(context, list, count);
    }

    public AcquireBaoyuePayAdapter(Context context, List<AcquirePayItem> list, int count, boolean close) {
        super(context, list, count, close);
    }

    @Override
    public View getOwnView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_acquire_pay, null, false);
            viewHolder.item_acquire_pay_title = convertView.findViewById(R.id.item_acquire_pay_title);
            viewHolder.item_acquire_pay_title_tag = convertView.findViewById(R.id.item_acquire_pay_title_tag);
            viewHolder.item_acquire_pay_note = convertView.findViewById(R.id.item_acquire_pay_note);
            viewHolder.item_acquire_pay_price = convertView.findViewById(R.id.item_acquire_pay_price);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.item_acquire_pay_title.setText(mList.get(position).getTitle());
        if (mList.get(position).getTag().size() != 0) {
            viewHolder.item_acquire_pay_title_tag.setVisibility(View.VISIBLE);
            viewHolder.item_acquire_pay_title_tag.setText(mList.get(position).getTag().get(0).getTab());
        } else {
            viewHolder.item_acquire_pay_title_tag.setVisibility(View.GONE);
        }
        viewHolder.item_acquire_pay_note.setText(mList.get(position).getNote());
        viewHolder.item_acquire_pay_price.setText("¥ " + mList.get(position).getPrice());

        viewHolder.item_acquire_pay_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调起支付
                Intent intent = new Intent(mContext, PayActivity.class);
                intent.putExtra("goods_id", mList.get(position).getGoods_id());
                String price = mList.get(position).getPrice() + "";
                if (price.contains("元")) {
                    price = price.substring(0, price.lastIndexOf("元"));
                }
                intent.putExtra("price", price);
                ((Activity)(mContext)).startActivityForResult(intent,1);

            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView item_acquire_pay_title;
        TextView item_acquire_pay_title_tag;
        TextView item_acquire_pay_note;
        TextView item_acquire_pay_price;
    }
}
