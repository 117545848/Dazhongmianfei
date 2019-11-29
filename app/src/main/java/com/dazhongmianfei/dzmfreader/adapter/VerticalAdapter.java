package com.dazhongmianfei.dzmfreader.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.bean.BaseTag;
import com.dazhongmianfei.dzmfreader.bean.LabelListItem;
import com.dazhongmianfei.dzmfreader.book.been.StroreBookcLable;
import com.dazhongmianfei.dzmfreader.config.ReaderApplication;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyPicasso;
import com.dazhongmianfei.dzmfreader.utils.MyToash;

import java.util.List;

/**
 * 垂直布局的adapter，包括"大神推荐" "新书力推" "宅男最爱"上半部分 "小编最爱"
 */
public class VerticalAdapter extends BaseAdapter {
    Activity activity;
    List<StroreBookcLable.Book> bookList;
    LayoutInflater layoutInflater;
    private int WIDTH, HEIGHT, height;
    boolean orientation;

    public VerticalAdapter(Activity activity, List<StroreBookcLable.Book> bookListint, int WIDTH, int HEIGHT, boolean orientation) {
        this.activity = activity;
        this.bookList = bookListint;
        this.layoutInflater = LayoutInflater.from(activity);
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.orientation = orientation;
        height = ImageUtil.dp2px(activity, 50);
        MyToash.Log("VerticalAdapter",WIDTH+"  "+HEIGHT+"  "+height+"  "+orientation);
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public StroreBookcLable.Book getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StroreBookcLable.Book book = getItem(position);
        View contentView = null;
        if (!orientation) {
            contentView = layoutInflater.inflate(R.layout.item_store_label_male_vertical, null, false);
            ImageView imageView = contentView.findViewById(R.id.item_store_label_male_vertical_img);
            LinearLayout item_store_label_male_vertical_layout = contentView.findViewById(R.id.item_store_label_male_vertical_layout);
            TextView name = contentView.findViewById(R.id.item_store_label_male_vertical_text);
            TextView item_store_label_male_vertical_text2 = contentView.findViewById(R.id.item_store_label_male_vertical_text2);

            if( !book.tag.isEmpty()) {
                String tagString = "";
                for (BaseTag tag : book.tag) {
                    tagString += " " + tag.getTab();
                }
                item_store_label_male_vertical_text2.setText(tagString.substring(1));
                item_store_label_male_vertical_text2.setVisibility(View.VISIBLE);
            }else {
                item_store_label_male_vertical_text2.setVisibility(View.GONE);
            }
            name.setText(book.getName());
            LinearLayout.LayoutParams layoutParamsIm = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            layoutParamsIm.height = HEIGHT;

            imageView.setLayoutParams(layoutParamsIm);
            MyPicasso.GlideImage(activity, book.getCover(), imageView, WIDTH, HEIGHT);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) item_store_label_male_vertical_layout.getLayoutParams();
            layoutParams.height = HEIGHT + height;
            item_store_label_male_vertical_layout.setLayoutParams(layoutParams);


        } else {

            contentView = layoutInflater.inflate(R.layout.item_store_label_male_horizontal2, null, false);
            LinearLayout item_store_label_male_vertical_layout = contentView.findViewById(R.id.item_store_label_male_vertical_layout);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) item_store_label_male_vertical_layout.getLayoutParams();
            layoutParams.height = HEIGHT;
            item_store_label_male_vertical_layout.setLayoutParams(layoutParams);

            ImageView imageView = contentView.findViewById(R.id.item_store_label_male_horizontal_img);
            TextView name = contentView.findViewById(R.id.item_store_label_male_horizontal_name);
            TextView flag = contentView.findViewById(R.id.item_store_label_male_horizontal_flag);
            TextView description = contentView.findViewById(R.id.item_store_label_male_horizontal_description);
            TextView author = contentView.findViewById(R.id.item_store_label_male_horizontal_author);
            TextView item_store_label_male_horizontal_tag = contentView.findViewById(R.id.item_store_label_male_horizontal_tag);

            LinearLayout.LayoutParams layoutParamsIm = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            layoutParamsIm.height = HEIGHT;
            layoutParamsIm.width = WIDTH;
            imageView.setLayoutParams(layoutParamsIm);
            MyPicasso.GlideImageNoSize(activity, book.getCover(), imageView);


            name.setText(book.getName());
            if (TextUtils.isEmpty(book.getFlag())) {
                flag.setVisibility(View.GONE);
            } else {
                flag.setVisibility(View.VISIBLE);
            }
            flag.setText(book.getFlag());

            description.setText(book.getDescription());
            author.setText(book.getAuthor());
            String str="";
            for (BaseTag tag : book.tag) {
                 str+="&nbsp&nbsp<font color='"+tag.color+"'>"+tag.tab+"</font>";
            }
            item_store_label_male_horizontal_tag.setText(Html.fromHtml(str));


        }
        return contentView;
    }
}
