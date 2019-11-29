package com.dazhongmianfei.dzmfreader.book.adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.activity.WebViewActivity;
import com.dazhongmianfei.dzmfreader.book.been.ReadHistory;
import com.dazhongmianfei.dzmfreader.book.fragment.ReadHistoryBookFragment;
import com.dazhongmianfei.dzmfreader.config.ReaderApplication;

import com.nostra13.universalimageloader.core.ImageLoader;
//.TodayOneAD;
import com.dazhongmianfei.dzmfreader.jinritoutiao.TodayOneAD;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abc on 2017/4/28.
 */
public class ReadHistoryRecyclerViewAdapter extends RecyclerView.Adapter<ReadHistoryRecyclerViewAdapter.MyViewHolder> {
    private List<ReadHistory.ReadHistoryBook> list;
    private ReadHistoryBookFragment.GetPosition getPosition;
    private Activity activity;

    public ReadHistoryRecyclerViewAdapter(Activity activity, List<ReadHistory.ReadHistoryBook> list, ReadHistoryBookFragment.GetPosition getPosition) {
        this.list = list;
        listPosition.clear();
        this.activity = activity;
        this.getPosition = getPosition;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_readhistory, parent, false);

        return new MyViewHolder(rootView);
    }

    List<Integer> listPosition = new ArrayList<>();

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ReadHistory.ReadHistoryBook readHistoryBook = list.get(position);

        if (readHistoryBook.ad_type == 0) {
            holder.recyclerview_item_readhistory_HorizontalScrollView.scrollTo(0, 0);
            holder.list_ad_view_layout.setVisibility(View.GONE);
            holder.recyclerview_item_readhistory_name.setText(readHistoryBook.getName());
            holder.recyclerview_item_readhistory_des.setText(readHistoryBook.chapter_title);
            holder.recyclerview_item_readhistory_time.setText(readHistoryBook.getLast_chapter_time() + "  " + String.format(LanguageUtil.getString(activity, R.string.ReadHistoryFragment_total_chapter), readHistoryBook.getTotal_Chapter()));

            ImageLoader.getInstance().displayImage(readHistoryBook.getCover(), holder.recyclerview_item_readhistory_img, ReaderApplication.getOptions());
            holder.recyclerview_item_readhistory_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPosition.getPosition(0, readHistoryBook, position);
                }
            });
            holder.recyclerview_item_readhistory_goon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPosition.getPosition(1, readHistoryBook, position);
                }
            });
            holder.recyclerview_item_readhistory_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPosition.getPosition(2, readHistoryBook, position);
                }
            });
        } else {
            holder.recyclerview_item_readhistory_HorizontalScrollView.setVisibility(View.GONE);
            holder.list_ad_view_layout.setVisibility(View.VISIBLE);
            if (readHistoryBook.ad_type == 1) {
                ImageLoader.getInstance().displayImage(readHistoryBook.ad_image, holder.list_ad_view_img, ReaderApplication.getOptions());
                holder.list_ad_view_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(activity, WebViewActivity.class);
                        intent.putExtra("url", readHistoryBook.ad_skip_url);
                        intent.putExtra("title", readHistoryBook.ad_title);
                        intent.putExtra("advert_id", readHistoryBook.advert_id);
                        activity.startActivity(intent);
                    }
                });
            } else {
                holder.list_ad_view_img.setVisibility(View.GONE);
                new TodayOneAD(activity, 2, readHistoryBook.ad_android_key).getTodayOneBanner(holder.list_ad_view_layout, null, 2);
            }
         /*   holder.recyclerview_item_readhistory_HorizontalScrollView.setVisibility(View.GONE);
            holder.recyclerview_item_readhistory_ad.setVisibility(View.VISIBLE);

            listPosition.add(position);
            TodayOneAD getQQBannerAD = new TodayOneAD(activity);
            getQQBannerAD.getTodayOneBanner(holder.recyclerview_item_readhistory_ad, null, 0);
*/

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.recyclerview_item_readhistory_img)
        ImageView recyclerview_item_readhistory_img;
        @BindView(R2.id.recyclerview_item_readhistory_name)
        TextView recyclerview_item_readhistory_name;
        @BindView(R2.id.recyclerview_item_readhistory_des)
        TextView recyclerview_item_readhistory_des;
        @BindView(R2.id.recyclerview_item_readhistory_time)
        TextView recyclerview_item_readhistory_time;
        @BindView(R2.id.recyclerview_item_readhistory_goon)
        Button recyclerview_item_readhistory_goon;
        @BindView(R2.id.list_ad_view_layout)
        FrameLayout list_ad_view_layout;
        @BindView(R2.id.list_ad_view_img)
        ImageView list_ad_view_img;


        @BindView(R2.id.recyclerview_item_readhistory_book)
        LinearLayout recyclerview_item_readhistory_book;
        @BindView(R2.id.recyclerview_item_readhistory_del)
        TextView recyclerview_item_readhistory_del;
        @BindView(R2.id.recyclerview_item_readhistory_HorizontalScrollView)
        HorizontalScrollView recyclerview_item_readhistory_HorizontalScrollView;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recyclerview_item_readhistory_book.getLayoutParams();
            layoutParams.width = ScreenSizeUtils.getInstance(activity).getScreenWidth();
            recyclerview_item_readhistory_book.setLayoutParams(layoutParams);
        }
    }
}


