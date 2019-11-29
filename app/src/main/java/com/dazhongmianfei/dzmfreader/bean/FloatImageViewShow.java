package com.dazhongmianfei.dzmfreader.bean;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dazhongmianfei.dzmfreader.activity.DayShareActivity;
import com.dazhongmianfei.dzmfreader.activity.LoginActivity;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.Utils;
import com.dazhongmianfei.dzmfreader.view.FloatDragView;

/**
 * Created by scb on 2018/11/27.
 */

public class FloatImageViewShow {
    public static FloatImageView floatImageView;

    public interface GetFloatImageView {
        void show(boolean show);

    }

    public static String icon_url;
    public static Bitmap bitmap;
    public static boolean No_Float;
    public static String share_read_url;
    public static int bitmapWidth = 60;

    public static void showFloatImage(final Activity activity, int res) {
        MyToash.Log("OkHttpEngine0", "" + (share_read_url == null || icon_url == null));
        if (share_read_url == null || icon_url == null) {
            return;
        }
        MyToash.Log("OkHttpEngine1", icon_url);
        try {
            final FloatDragView floatDragView = new FloatDragView(activity);
            if (No_Float) {
                return;
            }
            final RelativeLayout relativeLayout = activity.findViewById(res);
            Log.i("OkHttpEngine-2", (relativeLayout == null) + "  " + icon_url + "   " + share_read_url);
            if (activity == null || relativeLayout == null || icon_url.length() == 0 || share_read_url.length() == 0) {
                return;
            }
            if (bitmap != null && !bitmap.isRecycled()) {// ImageUtil.imageScale(bitmap, ImageUtil.dp2px(activity, 70), ImageUtil.dp2px(activity, 70))
                floatDragView.addFloatDragView(activity, bitmap, relativeLayout, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Utils.isLogin(activity)) {
                            activity.startActivity(new Intent(activity, DayShareActivity.class));
                        } else {
                            activity.startActivity(new Intent(activity, LoginActivity.class));
                        }
                    }
                });
                return;
            }
            Glide.with(activity).asBitmap().load(icon_url).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    int width = ImageUtil.dp2px(activity, 60);
                    int heiht = width * resource.getHeight() / resource.getWidth();
                    bitmap = ImageUtil.imageScale(resource, width, heiht);
                    floatDragView.addFloatDragView(activity, bitmap, relativeLayout, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (Utils.isLogin(activity)) {
                                activity.startActivity(new Intent(activity, DayShareActivity.class));
                            } else {
                                activity.startActivity(new Intent(activity, LoginActivity.class));
                            }

                        }
                    });

                }
            });

        } catch (Exception e) {
        }
    }


}

