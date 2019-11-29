package com.dazhongmianfei.dzmfreader.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by scb on 2019/1/3.
 */

public class ViewToBitmapUtil {

    private static final String TAG = "ViewToBitmapUtil";
    public static Bitmap convertViewToBitmap(View v, int y) {
        try {
            if (y <= 0) {
                return null;
            }
            Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);

            Canvas c = new Canvas(b);
            //  v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
            v.layout(v.getLeft(), y, v.getRight(), y + v.getHeight());
            // Draw background
            Drawable bgDrawable = v.getBackground();
            if (bgDrawable != null)
                bgDrawable.draw(c);
            else
                c.drawColor(Color.WHITE);
            // Draw view to canvas
            v.draw(c);
        }catch (Error e){
            Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ALPHA_8);

            Canvas c = new Canvas(b);
            //  v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
            v.layout(v.getLeft(), y, v.getRight(), y + v.getHeight());
            // Draw background
            Drawable bgDrawable = v.getBackground();
            if (bgDrawable != null)
                bgDrawable.draw(c);
            else
                c.drawColor(Color.WHITE);
            // Draw view to canvas
            v.draw(c);
        }
        return null;

    }
    public static Bitmap convertViewToBitmap(View v, int y,int AD_HEIGHT) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), y, v.getRight(), y+AD_HEIGHT);
        // Draw background
        Drawable bgDrawable = v.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(c);
        else
            c.drawColor(Color.WHITE);
        // Draw view to canvas
        v.draw(c);
        return b;
    /*    Bitmap bitmap=null;
        try {
            int w = v.getWidth();
            int h = v.getHeight();
             bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bitmap);

            c.drawColor(Color.WHITE);
            v.layout(0, y, w, y + h);
            v.draw(c);

        } catch (Exception e) {
        }
        return bitmap;*/

    }

 /*   public static Bitmap convertViewToBitmap(View view, int bitmapWidth, int bitmapHeight) {
        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }*/

    public static void getScreenRectOfView(View view, Rect outRect) {
        int pos[] = new int[2];
        view.getLocationOnScreen(pos);
        outRect.set(pos[0], pos[1], pos[0] + view.getWidth(), pos[1] + view.getHeight());
    }


}
