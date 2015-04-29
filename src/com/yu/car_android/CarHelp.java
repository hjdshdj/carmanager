package com.yu.car_android;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 帮助类
 * 
 * @author Administrator
 * 
 */
public class CarHelp {
	/**
	 * 屏幕参数获取
	 * 
	 * @param activity
	 * @return
	 */
	public static DisplayMetrics getDisplayMetrics(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}

	/**
	 * dip转换px
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * px转换dip
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
