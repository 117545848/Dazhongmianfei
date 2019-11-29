package com.dazhongmianfei.dzmfreader.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * 头像图片压缩
 * Created by scb on 2018/8/10.
 */
public class BitmapOption {
    private static final String TAG = BitmapOption.class.getSimpleName();
    private static final BitmapOption bitmapOption = new BitmapOption();

    private BitmapOption() {
    }

    public static BitmapOption getBitmapOption() {
        return bitmapOption;
    }


    /**
     * @param image 要压缩的图片
     * @param size  压缩到多大，单位KB
     * @return
     */
    public static Bitmap bitmapOption(Bitmap image, int size) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, out);
        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 0.5f);
        Bitmap result = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        out.reset();
        result.compress(Bitmap.CompressFormat.JPEG, 50, out);
        while (out.toByteArray().length > size * 1024) {
            matrix.setScale(0.9f, 0.9f);
            result = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);
            out.reset();
            result.compress(Bitmap.CompressFormat.JPEG, 60, out);
        }
        return result;
    }


}