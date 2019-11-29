package com.dazhongmianfei.dzmfreader.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.dazhongmianfei.dzmfreader.banner.holder.Holder;
import com.dazhongmianfei.dzmfreader.bean.BannerItem;
import com.dazhongmianfei.dzmfreader.config.ReaderApplication;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * 轮播图Holder
 */
public class HomeBannerHolderView implements Holder<BannerItem> {
    private ImageView imageView;

    @Override
    public View createView(Context context, int size) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context,int position, BannerItem data) {
        ImageLoader.getInstance().displayImage(data.getImage(), imageView, ReaderApplication.getOptions());
//        ImageLoader.getInstance().displayImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527943969400&di=7bd62bbf863c30d260808e64cc0776a1&imgtype=0&src=http%3A%2F%2Fwww.taopic.com%2Fuploads%2Fallimg%2F140403%2F240438-1404030P31712.jpg", imageView, ReaderApplication.getOptions());
    }
}
