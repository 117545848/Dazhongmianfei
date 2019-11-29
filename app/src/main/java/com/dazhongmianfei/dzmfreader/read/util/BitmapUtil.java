package com.dazhongmianfei.dzmfreader.read.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/7/29 0029.
 */
public class BitmapUtil {
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    public static Bitmap decodeSampledBitmapFromResource2(Resources res, int resId,int reqWidth, int reqHeight) {

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
//获取资源图片
        InputStream is = res.openRawResource(resId);

        Bitmap temp=BitmapFactory.decodeStream(is, null, opt);

        return getNewBitmap(temp,reqWidth,reqHeight);
    }

    //把传进来的bitmap对象转换为宽度为x,长度为y的bitmap对象
    public static Bitmap getNewBitmap(Bitmap b, float x, float y) {
        int w = b.getWidth();
        int h = b.getHeight();
        float sx = x / w;
        float sy = y / h;
        Matrix matrix = new Matrix();
        //也可以按两者之间最大的比例来设置放大比例，这样不会是图片压缩
//        float bigerS = Math.max(sx,sy);
//        matrix.postScale(bigerS,bigerS);
        matrix.postScale(sx, sy); // 长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(b, 0, 0, w,
                h, matrix, true);
        return resizeBmp;
    }

    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 50, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.PNG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            /**   final int halfHeight = height / 2;
             final int halfWidth = width / 2;

             // Calculate the largest inSampleSize value that is a power of 2 and keeps both
             // height and width larger than the requested height and width.
             while ((halfHeight / inSampleSize) > reqHeight
             && (halfWidth / inSampleSize) > reqWidth) {
             inSampleSize *= 2;
             } */
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都不会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

        }

        return inSampleSize;
    }
}
