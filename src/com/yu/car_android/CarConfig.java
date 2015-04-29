package com.yu.car_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 临时数据sp
 * 
 * @author Administrator
 * 
 */
public class CarConfig {
	/**
	 * String类型数据存储
	 */
	public static void editorCarManager(Context context, String tag, String value) {
		Editor sharedata = context.getSharedPreferences("CarManager", 0).edit();
		sharedata.putString(tag, value);
		sharedata.commit();
	}

	/**
	 * String类型数据读取
	 */
	public static String shareStringCarManager(Context context, String tag) {
		SharedPreferences sharedata = context.getSharedPreferences("CarManager", 0);
		String re = sharedata.getString(tag, "");
		return re;
	}
	/**
	 * Cookie
	 */
	public static void editorCookie(Context con, String value) {
		Editor sharedata = con.getSharedPreferences("Cookie", 0).edit();
		sharedata.putString("Cookie", value);
		sharedata.commit();
	}

	/**
	 * Cookie
	 */
	public static String shareCookie(Context con) {
		SharedPreferences sharedata = con.getSharedPreferences("Cookie", 0);
		String re = sharedata.getString("Cookie", "");
		return re;
}
}
