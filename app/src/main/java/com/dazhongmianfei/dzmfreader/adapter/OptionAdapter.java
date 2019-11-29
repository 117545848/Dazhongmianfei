package com.dazhongmianfei.dzmfreader.adapter;

import android.app.Activity;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;

import com.dazhongmianfei.dzmfreader.bean.BaseTag;
import com.dazhongmianfei.dzmfreader.bean.OptionBeen;

import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyPicasso;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 完本书籍水平布局的adapter
 */
public class OptionAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<OptionBeen> optionBeenList;
    private int OPTION, HEIGHT, WIDTH;
    private boolean PRODUCT;

    public OptionAdapter(Activity activity, List<OptionBeen> optionBeenList, int OPTION, boolean PRODUCT, LayoutInflater layoutInflater) {
        this.activity = activity;
        this.optionBeenList = optionBeenList;
        this.OPTION = OPTION;
        this.PRODUCT = PRODUCT;
        this.layoutInflater = layoutInflater;
        WIDTH = ImageUtil.dp2px(activity, 90);
        HEIGHT = WIDTH * 4 / 3;
    }

    @Override
    public int getCount() {
        return optionBeenList.size();
    }

    @Override
    public OptionBeen getItem(int position) {

        return optionBeenList.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_option, null, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OptionBeen optionBeen = getItem(position);

   /*     LinearLayout.LayoutParams layoutParamsIm = (LinearLayout.LayoutParams) viewHolder.imageView.getLayoutParams();
        layoutParamsIm.height = HEIGHT;
        viewHolder.imageView.setLayoutParams(layoutParamsIm);*/

        //处理由于图片重用，导致界面图片闪烁效果
      /*  if(! optionBeen.equals(viewHolder.imageView.getTag())){

            viewHolder.imageView.setImageResource(0);
            //重新设置图片

        }*/


        viewHolder.name.setText(optionBeen.getName());
        if (optionBeen.getFlag() == null || optionBeen.getFlag().length() == 0||!PRODUCT) {
            viewHolder.flag.setVisibility(View.GONE);
        } else {
            viewHolder.flag.setText(optionBeen.getFlag());
            viewHolder.flag.setVisibility(View.VISIBLE);
        }
        viewHolder.description.setText(optionBeen.getDescription());
        viewHolder.author.setText(optionBeen.getAuthor());
        String str = "";
        for (BaseTag tag : optionBeen.tag) {
            str += "&nbsp&nbsp<font color='" + tag.color + "'>" + tag.tab + "</font>";
        }
        viewHolder.item_store_label_male_horizontal_tag.setText(Html.fromHtml(str));
        MyPicasso.GlideImage(activity, optionBeen.getCover(), viewHolder.imageView, WIDTH, HEIGHT);
     /*   RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.item_store_label_male_vertical_layout.getLayoutParams();
        layoutParams.height = HEIGHT;
        viewHolder.item_store_label_male_vertical_layout.setLayoutParams(layoutParams);
*/
        return convertView;
    }

    class ViewHolder {
        @BindView(R2.id.item_store_label_male_horizontal_img)
        ImageView imageView;
        @BindView(R2.id.item_store_label_male_horizontal_name)
        TextView name;
        @BindView(R2.id.item_store_label_male_horizontal_flag)
        TextView flag;
        @BindView(R2.id.item_store_label_male_horizontal_description)
        TextView description;
        @BindView(R2.id.item_store_label_male_horizontal_author)
        TextView author;
        @BindView(R2.id.item_store_label_male_horizontal_tag)
        TextView item_store_label_male_horizontal_tag;
        @BindView(R2.id.item_store_label_male_vertical_layout)
        LinearLayout item_store_label_male_vertical_layout;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }


}
