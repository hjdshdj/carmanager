package com.yu.car_android.util;

import java.io.File;

import com.yu.car_android.Constants;

import android.content.Context;

/**
 * 文件缓存
 * @author Administrator
 *
 */
public class FileCache {

	private File cacheDir;

	/**
	 * 如果有SD卡，则在SD卡中建一个目录缓存图片，没有SD卡就放在系统的缓存目录中
	 */
	public FileCache(Context context) {

		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), Constants.BAIHE_PIC_ROOT);
		} else {
			cacheDir = context.getCacheDir();
		}

		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}

	}

	/**
	 * 建立一个以url的hashCode作为文件名的缓存文件
	 */
	public File getFile(String url) {
		if (url != null) {
			String filename = String.valueOf(url.hashCode());
			File file = new File(cacheDir, filename);
			return file;
		} else {
			return null;
		}
	}

	/**
	 * 清除缓存目录的所有文件
	 */
	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files == null)
			return;
		for (File f : files)
			f.delete();
	}
}
