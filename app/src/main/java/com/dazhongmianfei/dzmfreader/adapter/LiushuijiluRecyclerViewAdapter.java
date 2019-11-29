package com.dazhongmianfei.dzmfreader.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.bean.PayGoldDetail;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abc on 2017/4/28.
 */
public class LiushuijiluRecyclerViewAdapter extends RecyclerView.Adapter<LiushuijiluRecyclerViewAdapter.ViewHolder> {
    Activity activity;
    LayoutInflater layoutInflater;
    List<PayGoldDetail> optionBeenList;


    public interface OnItemClick {
        void OnItemClick(int position, PayGoldDetail optionBeen);
    }
    OnItemClick onItemClick;
    public LiushuijiluRecyclerViewAdapter(Activity activity, List<PayGoldDetail> optionBeenList, LayoutInflater layoutInflater, OnItemClick onItemClick) {
        this.activity = activity;
        this.optionBeenList = optionBeenList;
        this.onItemClick = onItemClick;
        this.layoutInflater = layoutInflater;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = layoutInflater.inflate(R.layout.item_pay_gold_detail, null, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        PayGoldDetail optionBeen = optionBeenList.get(position);

     /*   viewHolder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyToash.Log("onBindViewHolder",position);
                onItemClick.OnItemClick(position,optionBeen);
            }
        });*/
        viewHolder.article.setText(optionBeen.getArticle());
        viewHolder.date.setText(optionBeen.getDate());
        viewHolder.detail.setText(optionBeen.getDetail());

    }

    @Override
    public int getItemCount() {
        return optionBeenList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.item_pay_gold_detail_article)
        TextView article;
        @BindView(R2.id.item_pay_gold_detail_date)
        TextView date;
        @BindView(R2.id.item_pay_gold_detail_detail)
        TextView detail;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}



