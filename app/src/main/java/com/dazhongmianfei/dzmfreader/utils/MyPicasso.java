package com.dazhongmianfei.dzmfreader.utils;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import com.dazhongmianfei.dzmfreader.R;


import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by scb on 2018/12/14.
 */

public class MyPicasso {


    public static void IoadImage(Activity activity, String url, int error, ImageView imageView) {
        GlideImageNoSize(activity, url, imageView);
 /*       if (url == null || url.length() == 0) {
            imageView.setImageResource(error);
        } else

            Picasso.with(activity).load(url).error(error).placeholder(error).into(imageView);
*/
    }

    public static void GlideImageNoSize(Activity activity, String url, ImageView imageView) {
        if (url == null || url.length() == 0) {
            return;
        } else {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.icon_comic_def)    //加载成功之前占位图
                    .error(R.mipmap.icon_comic_def)    //加载错误之后的错误图
                    .skipMemoryCache(true)        //
                    .diskCacheStrategy(DiskCacheStrategy.ALL);    //缓存所有版本的图像

            setGlide(activity, url, imageView, options);
        }
    }
    public static void GlideImageHeadNoSize(Activity activity, String url, ImageView imageView) {
        if (url == null || url.length() == 0) {
            return;
        } else {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.icon_def_head)    //加载成功之前占位图
                    .error(R.mipmap.icon_def_head)    //加载错误之后的错误图
                    .skipMemoryCache(false)        //
                    .diskCacheStrategy(DiskCacheStrategy.ALL);    //缓存所有版本的图像

            setGlide(activity, url, imageView, options);
        }
    }

    public static void GlideImage(Activity activity, String url, ImageView imageView, int width, int height) {
        if (url == null || url.length() == 0 || activity == null || imageView == null) {
            return;
        } else {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.icon_comic_def)    //加载成功之前占位图
                    .error(R.mipmap.icon_comic_def)    //加载错误之后的错误图
                    //指定图片的尺寸
                    .override(width, height)
                    .centerCrop()
                    .skipMemoryCache(false)        //
                    .diskCacheStrategy(DiskCacheStrategy.ALL);    //缓存所有版本的图像


            setGlide(activity, url, imageView, options);
        }
    }

    private static void setGlide(Activity activity, String url, ImageView imageView, RequestOptions options) {
        try {
            Glide.with(activity).load(url).apply(options).into(imageView);
        } catch (Exception e) {
        } catch (Error error) {
        }
    }

    public static void GlideImageAD(Activity activity, String url, ImageView imageView, int width, int height) {
        if (url == null || url.length() == 0) {
            return;
        } else {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.icon_comic_def)    //加载成功之前占位图
                    .error(R.mipmap.icon_comic_def)    //加载错误之后的错误图
                    //指定图片的尺寸
                    // .override(width,height)
                    .skipMemoryCache(false)        //
                    .diskCacheStrategy(DiskCacheStrategy.ALL);    //缓存所有版本的图像


            setGlide(activity, url, imageView, options);
        }
    }


    public static void GlideImageRoundedCorners(int radius, Activity activity, String url, ImageView imageView, int width, int height) {

        if (url == null || url.length() == 0) {
            return;
        } else {
            RequestOptions options = new RequestOptions()
                    //  .placeholder(R.mipmap.icon_comic_def)	//加载成功之前占位图
                    .error(R.mipmap.icon_comic_def)    //加载错误之后的错误图

                    .override(width, height)

                    // .transform(new CornersTranform(radius))
                    //  .transform(new RoundedCornersTransformation(ImageUtil.dp2px(activity,radius),0))
                    .skipMemoryCache(false)        //
                    .diskCacheStrategy(DiskCacheStrategy.ALL)        //缓存所有版本的图像
                    //  .diskCacheStrategy(DiskCacheStrategy.NONE)        //不使用硬盘本地缓存
                    //  .diskCacheStrategy(DiskCacheStrategy.DATA)        //只缓存原来分辨率的图片
                    //   .diskCacheStrategy(DiskCacheStrategy.RESOURCE)        //只缓存最终的图片
                    .transforms(new CenterCrop(), new RoundedCornersTransformation(ImageUtil.dp2px(activity, radius), 0));

            setGlide(activity, url, imageView, options);
        }
    }

    public static void GlideImageRoundedCornersNoSize(int radius, Activity activity, String url, ImageView imageView) {
        if (url == null || url.length() == 0) {
            return;
        } else {
            RequestOptions options = new RequestOptions()
                    //  .placeholder(R.mipmap.icon_comic_def)	//加载成功之前占位图
                    .error(R.mipmap.icon_comic_def)    //加载错误之后的错误图
                    .skipMemoryCache(false)        //
                    .diskCacheStrategy(DiskCacheStrategy.ALL)        //缓存所有版本的图像
                    .transforms(new CenterCrop(), new RoundedCornersTransformation(ImageUtil.dp2px(activity, radius), 0));

            setGlide(activity, url, imageView, options);
        }
    }

    public static void GlideImageRoundedGasoMohu(Activity activity, String url, ImageView imageView, int width, int height) {
        if (url == null || url.length() == 0) {
            return;
        } else {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.icon_comic_def)    //加载成功之前占位图
                    .error(R.mipmap.icon_comic_def)    //加载错误之后的错误图
                    //指定图片的尺寸
                    .override(width, height)
                    .centerCrop()
                    .transform(new BlurTransformation())
                    .skipMemoryCache(true)        //
                    .diskCacheStrategy(DiskCacheStrategy.ALL)        //缓存所有版本的图像
                    ;

            setGlide(activity, url, imageView, options);
        }
    }
}





