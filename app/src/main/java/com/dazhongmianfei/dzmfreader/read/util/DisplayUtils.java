package com.dazhongmianfei.dzmfreader.read.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DisplayUtils {
	/**
	 * 将px值转换为dp值
	 */
	public static int px2dp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将dp值转换为px值
	 */
	public static int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 获取屏幕宽度
	 */
	public static int getScreenWidthPixels(Activity context) {
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		return metric.widthPixels;
	}

	/**
	 * 获取屏幕高度
	 */
	public static int getScreenHeightPixels(Activity context) {
		return getScreentHeight(context)+getStatusBarHeight(context);
	}

	public static int getStatusBarHeight(Activity activity) {
		int result = 0;
		int resourceId = activity.getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			result = activity.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	public static int getScreentHeight(Activity context ) {
		int heightPixels;
		WindowManager w = context.getWindowManager();
		Display d = w.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);
		// since SDK_INT = 1;
		heightPixels = metrics.heightPixels;
		// includes window decorations (statusbar bar/navigation bar)
		if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
			try {
				heightPixels = (Integer) Display.class
						.getMethod("getRawHeight").invoke(d);
			} catch (Exception ignored) {
			}
			// includes window decorations (statusbar bar/navigation bar)
		else if (Build.VERSION.SDK_INT >= 17)
			try {
				android.graphics.Point realSize = new android.graphics.Point();
				Display.class.getMethod("getRealSize",
						android.graphics.Point.class).invoke(d, realSize);
				heightPixels = realSize.y;
			} catch (Exception ignored) {
			}

		return heightPixels;
	}


}
