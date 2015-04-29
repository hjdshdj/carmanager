package com.yu.car_android.bean;

import android.graphics.drawable.Drawable;

/**
 * 应用程序信息
 * 
 * @author Administrator
 * 
 */
public class ApkItem {
	public String appName, appVersion, packageName;
	public Drawable image;
	public boolean isInstalled = false;
	public String path;
	public String ID;
	public String appDesc = "";
	public long appSize = 0;
	public int versionCode;
}
