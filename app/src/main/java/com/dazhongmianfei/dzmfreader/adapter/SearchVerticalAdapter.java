package com.dazhongmianfei.dzmfreader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.bean.OptionBeen;
import com.dazhongmianfei.dzmfreader.bean.SearchItem;
import com.dazhongmianfei.dzmfreader.config.ReaderApplication;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;

import java.util.List;

/**
 * 搜索页下方的图书的adapter
 */
public class SearchVerticalAdapter extends ReaderBaseAdapter<SearchItem> {
    public int WIDTH, HEIGHT, H20,H33;

    public SearchVerticalAdapter(Context context, List<SearchItem> list) {
        super(context, list, list.size());
        WIDTH = ScreenSizeUtils.getInstance(mContext).getScreenWidth();
        H20 = ImageUtil.dp2px(mContext, 40);
        H33=ImageUtil.dp2px(mContext, 28);
        WIDTH = (WIDTH - H20) / 3;//横向排版 图片宽度
        HEIGHT = (int) (((float) WIDTH * 4f / 3f));//
    }

    @Override
    public View getOwnView(int position, View convertView, ViewGroup parent) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.item_store_label_male_vertical, null, false);
        ImageView imageView = contentView.findViewById(R.id.item_store_label_male_vertical_img);
        TextView name = contentView.findViewById(R.id.item_store_label_male_vertical_text);
        LinearLayout  item_store_label_male_vertical_layout = contentView.findViewById(R.id.item_store_label_male_vertical_layout);
        //  TextView status = contentView.findViewById(R.id.item_store_label_male_vertical_status);

        RelativeLayout.LayoutParams layoutParamss= (RelativeLayout.LayoutParams) item_store_label_male_vertical_layout.getLayoutParams();
        layoutParamss.width=WIDTH;
        layoutParamss.height=HEIGHT+H33;
        item_store_label_male_vertical_layout.setLayoutParams(layoutParamss);

        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.width=WIDTH;
        layoutParams.height=HEIGHT;
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoader.getInstance().displayImage(mList.get(position).getCover(), imageView, ReaderApplication.getOptions());



        name.setText(mList.get(position).getName());
//        status.setText(mList.get(position).getFlag());
        return contentView;
    }

}
