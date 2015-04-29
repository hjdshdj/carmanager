package com.yu.car_android;

import android.os.Environment;

/**
 * 全局常量
 * 
 * @author Administrator
 * 
 */
public class Constants {
	// 手机内存大小
	public static int PHONE_MEM;
	// 屏幕宽度（像素）
	public static int screenWidth;
	// 屏幕高度（像素）
	public static int screenHeight;
	// 缓存目录
	public static final String BAIHE_PIC_ROOT = "/baiheMarry/cacheImage";
	public static final String BAIHE_DOWNLOAD = "/baiheMarry/download";
	
	//fragment切换
	public static final String HOME="home",SERVER="server",SETTING="setting",ACCOUNT="account";

	/**
	 * 图片在SD卡存放的路径
	 */
	public static String BAIHE_PIC_FULL_PATH = Environment.getExternalStorageDirectory().getPath() + Constants.BAIHE_PIC_ROOT + "/";
}
