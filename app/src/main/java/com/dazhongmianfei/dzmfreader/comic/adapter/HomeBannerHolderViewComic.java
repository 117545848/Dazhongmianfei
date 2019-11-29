package com.dazhongmianfei.dzmfreader.comic.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.banner.holder.Holder;
import com.dazhongmianfei.dzmfreader.bean.BannerItem;
import com.dazhongmianfei.dzmfreader.bean.BannerItemStore;
import com.dazhongmianfei.dzmfreader.comic.been.MyGlide;
import com.dazhongmianfei.dzmfreader.config.ReaderApplication;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyPicasso;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;


/**
 * 轮播图Holder
 */
public class HomeBannerHolderViewComic implements Holder<BannerItemStore> {
    private ImageView item_store_entrance_comic_bg, item_store_entrance_comic_img;
    View item_store_entrance_comic_bgVIEW;
    Activity activity;
    int width, width2, height;


    @Override
    public View createView(Context context, int size) {
        activity = (Activity) context;

        View contentView = LayoutInflater.from(activity).inflate(R.layout.item_store_entrance_comic, null, false);
        width = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        width2 = width - ImageUtil.dp2px(activity, 20);
        height = width2 * 3 / 5;



        item_store_entrance_comic_img = contentView.findViewById(R.id.item_store_entrance_comic_img);
        item_store_entrance_comic_bg = contentView.findViewById(R.id.item_store_entrance_comic_bg);
        item_store_entrance_comic_bgVIEW = contentView.findViewById(R.id.item_store_entrance_comic_bgVIEW);



        ViewGroup.LayoutParams layoutParams =  item_store_entrance_comic_img.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width=width2;
        item_store_entrance_comic_img.setLayoutParams(layoutParams);

   /*     FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) item_store_entrance_comic_img.getLayoutParams();
        layoutParams.height = height;
        item_store_entrance_comic_img.setLayoutParams(layoutParams);
*/

        return contentView;
    }

    @Override
    public void UpdateUI(Context context, int position, BannerItemStore data) {//- ImageUtil.dp2px(activity, 20)
//"#4D"+
        int[] colors = {0xE6FFFFFF, Color.parseColor(data.color)};

       // int[] colors = {0xE6FFFFFF, Color.parseColor("#1A"+data.color.substring(1))};
        GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors);
        item_store_entrance_comic_bgVIEW.setBackground(g);
        MyPicasso.GlideImageRoundedCornersNoSize(6, activity, data.getImage(), item_store_entrance_comic_img);


        //MyGlide.GlideImagePalette(activity, data.getImage(), item_store_entrance_comic_bg, width, ImageUtil.dp2px(activity, 310));
        MyPicasso.GlideImage(activity, data.getImage(), item_store_entrance_comic_bg, width, ImageUtil.dp2px(activity, 310));
    }
}
