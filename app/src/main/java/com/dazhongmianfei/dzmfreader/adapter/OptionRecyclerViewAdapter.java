package com.dazhongmianfei.dzmfreader.adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.activity.WebViewActivity;
import com.dazhongmianfei.dzmfreader.bean.BaseTag;
import com.dazhongmianfei.dzmfreader.bean.OptionBeen;
import com.dazhongmianfei.dzmfreader.jinritoutiao.TodayOneAD;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyPicasso;
import com.dazhongmianfei.dzmfreader.utils.MyToash;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abc on 2017/4/28.
 */
public class OptionRecyclerViewAdapter extends RecyclerView.Adapter<OptionRecyclerViewAdapter.ViewHolder> {
    Activity activity;
    LayoutInflater layoutInflater;
    List<OptionBeen> optionBeenList;
    int OPTION, HEIGHT, WIDTH;
    boolean PRODUCT;

    public interface OnItemClick {
        void OnItemClick(int position, OptionBeen optionBeen);
    }

    OnItemClick onItemClick;

    public OptionRecyclerViewAdapter(Activity activity, List<OptionBeen> optionBeenList, int OPTION, boolean PRODUCT, LayoutInflater layoutInflater, OnItemClick onItemClick) {
        this.activity = activity;
        this.optionBeenList = optionBeenList;
        this.OPTION = OPTION;
        this.PRODUCT = PRODUCT;
        this.onItemClick = onItemClick;
        this.layoutInflater = layoutInflater;
        WIDTH = ImageUtil.dp2px(activity, 90);
        HEIGHT = WIDTH * 4 / 3;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = layoutInflater.inflate(R.layout.item_option, null, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        OptionBeen optionBeen = optionBeenList.get(position);

        if (optionBeen.ad_type == 0) {
            viewHolder.item_store_label_male_vertical_layout.setVisibility(View.VISIBLE);
            viewHolder.list_ad_view_layout.setVisibility(View.GONE);
            viewHolder.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyToash.Log("onBindViewHolder", position);
                    onItemClick.OnItemClick(position, optionBeen);
                }
            });

            MyPicasso.GlideImage(activity, optionBeen.getCover(), viewHolder.imageView, WIDTH, HEIGHT);
            viewHolder.name.setText(optionBeen.getName());
            viewHolder.description.setText(optionBeen.getDescription());
            viewHolder.author.setText(optionBeen.getAuthor());
            String str = "";
            for (BaseTag tag : optionBeen.tag) {
                str += "&nbsp&nbsp<font color='" + tag.color + "'>" + tag.tab + "</font>";
            }
            viewHolder.item_store_label_male_horizontal_tag.setText(Html.fromHtml(str));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.item_store_label_male_vertical_layout.getLayoutParams();
            layoutParams.height = HEIGHT;
            viewHolder.item_store_label_male_vertical_layout.setLayoutParams(layoutParams);

        } else {
            viewHolder.item_store_label_male_vertical_layout.setVisibility(View.GONE);
            viewHolder.list_ad_view_layout.setVisibility(View.VISIBLE);
            if (optionBeen.ad_type==1) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewHolder.list_ad_view_img.getLayoutParams();
                layoutParams.height = WIDTH * optionBeen.ad_height / optionBeen.ad_height;
                viewHolder.list_ad_view_img.setLayoutParams(layoutParams);

                MyPicasso.GlideImageAD(activity, optionBeen.ad_image, viewHolder.list_ad_view_img, WIDTH, layoutParams.height);
                viewHolder.list_ad_view_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(activity, WebViewActivity.class);
                        intent.putExtra("url", optionBeen.ad_skip_url);
                        intent.putExtra("title", optionBeen.ad_title);
                        intent.putExtra("advert_id", optionBeen.advert_id);
                        activity.startActivity(intent);
                    }
                });
            } else {
                viewHolder.list_ad_view_img.setVisibility(View.GONE);
                new TodayOneAD(activity, 2, optionBeen.ad_android_key).getTodayOneBanner(viewHolder.list_ad_view_layout, null, 2);

            }
        }

    }

    @Override
    public int getItemCount() {
        return optionBeenList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.item_store_label_male_horizontal_img)
        ImageView imageView;
        @BindView(R2.id.item_store_label_male_horizontal_name)
        TextView name;
        //  @BindView(R2.id.item_store_label_male_horizontal_flag)
        //  TextView flag;
        @BindView(R2.id.item_store_label_male_horizontal_description)
        TextView description;
        @BindView(R2.id.item_store_label_male_horizontal_author)
        TextView author;
        @BindView(R2.id.item_store_label_male_horizontal_tag)
        TextView item_store_label_male_horizontal_tag;
        @BindView(R2.id.item_store_label_male_vertical_layout)
        LinearLayout item_store_label_male_vertical_layout;
        @BindView(R2.id.item_layout)
        RelativeLayout item_layout;
        @BindView(R2.id.list_ad_view_layout)
        FrameLayout list_ad_view_layout;
        @BindView(R2.id.list_ad_view_img)
        ImageView list_ad_view_img;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}



