package com.yu.car_android.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.yu.car_android.Constants;
import com.yu.car_android.R;

/**
 * 图片异步加载类
 */
public class ImageLoader {

	/** 内存缓存 **/
	private MemoryCache memoryCache = new MemoryCache();
	/** SD卡文件缓存 **/
	private FileCache fileCache;
	/** 返回由指定 collection 支持的同步（线程安全的）collection。 **/
	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	/** 线程池 **/
	private ExecutorService executorService;
	/** 保存到SD卡时做简单的文件加密，在文件头前面添加自定义字节 **/
	private final static String HEAD_STR = "baihe";
	/** M计算 **/
	private static int MB = 1024 * 1024;
	/** 缓存空间大小 */
	private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 20;
	/** SD卡缓存是否可用 */
	volatile static boolean sdcardCache = true;
	private static ImageLoader imageLoader = null;
	private Context mContext;

	private ImageLoader(Context context) {
		this.mContext = context;
		fileCache = new FileCache(context);
		int cpuNumber = Runtime.getRuntime().availableProcessors();
		executorService = Executors.newFixedThreadPool(cpuNumber * 4);
		initCachePool();
	}

	public synchronized static ImageLoader getInstance(Context context) {
		if (imageLoader == null) {
			imageLoader = new ImageLoader(context);
		}
		return imageLoader;
	}

	/**
	 * 初始化SD卡缓存池
	 */
	private static void initCachePool() {
		// 检查SDCARD是否可用
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			// 判断sdcard上的空间
			if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
				// SD空间不足,SD卡缓存不可用
				sdcardCache = false;
			}
		} else {
			sdcardCache = false;
		}
	}

	/**
	 * 空闲的SD卡空间
	 */
	private static int freeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
		return (int) sdFreeMB;
	}

	/**
	 * 加载图片方法
	 */
	public void displayImage(String url, ImageView imageView, int resId) {
		if (isCrop(imageView)) {
			imageView.setScaleType(ScaleType.CENTER_INSIDE);
		}
		if (TextUtils.isEmpty(url)) {
			imageView.setImageResource(resId);
			return;
		}
//		url = SystemUtils.changeTestImgUrl(url);

		imageViews.put(imageView, url);
		// 先从内存缓存中查找
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			if (isCrop(imageView)) {
				imageView.setScaleType(ScaleType.CENTER_CROP);
			}
			// 内存中的Bitmap
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setImageResource(resId);
			// 若没有的话则开启新线程从网络获取加载图片
			queuePhoto(url, imageView);

		}
	}

	/**
	 * 先从SD读取缓存图片，如果没有，那么就从网络加载图片
	 */
	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	/**
	 * 获取图片Bitmap对象（SD卡不存在这个图片缓存的时候，就从服务器下载，下载之后存到SD卡里，并作内存的硬缓存和软缓存）
	 */
	private Bitmap getBitmap(String url) {
//		url = SystemUtils.changeTestImgUrl(url);
		Bitmap _ResultBmp = memoryCache.get(url);
		if (_ResultBmp == null) {
			File imageFile = fileCache.getFile(url);
			InputStream is = null;
			OutputStream os = null;
			HttpGet httpGet = null;
			try {
				httpGet = new HttpGet(new URI(url));
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			HttpResponse response;
			HttpEntity entity = null;
			if (httpGet != null) {
				try {
					response = new DefaultHttpClient().execute(httpGet);
					entity = response.getEntity();

					if (entity != null) {
						is = entity.getContent();
						os = new FileOutputStream(imageFile);
						if (sdcardCache) {
							CopyStream(is, os);
							_ResultBmp = decodeFile(imageFile);
							memoryCache.insertCache(url, _ResultBmp);
						} else {
							_ResultBmp = FileUtil.decodeSampledBitmapFromInput(is, Constants.screenWidth, Constants.screenHeight);
							memoryCache.insertCache(url, _ResultBmp);
						}
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (os != null)
						try {
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			}
		}
		return _ResultBmp;
	}

	/** 先从内存里取图片，如果没有再从SD卡取图片 */
	public Bitmap getCacheBitmap(String url) {
//		url = SystemUtils.changeTestImgUrl(url);
		Bitmap _ResultBmp = memoryCache.get(url);
		if (_ResultBmp == null) {
			File imageFile = fileCache.getFile(url);
			_ResultBmp = decodeFile(imageFile);
		}
		return _ResultBmp;
	}

	/**
	 * decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
	 */
	public Bitmap decodeFile(File file) {
		try {
			int scale = 1;
			if (file.length() > 1024 * 1024 * 1.5) {
				scale = 4;
			} else if (file.length() > 1024 * 1024 * 0.67) {
				scale = 2;
			}

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			InputStream inputStream = new FileInputStream(file);
			byte[] buffer;

			try {
				buffer = new byte[inputStream.available()];
				inputStream.read(buffer);
				int size = HEAD_STR.length();
				return BitmapFactory.decodeByteArray(buffer, size, buffer.length - size, o2);

			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
						inputStream = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = null;
			// 从SD卡文件中获取
			if (sdcardCache) {
				File f = fileCache.getFile(photoToLoad.url);
				if (f.exists()) {
					bmp = decodeFile(f);
				}
			}

			if (bmp == null) {
				bmp = getBitmap(photoToLoad.url);
			}
			memoryCache.insertCache(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			// 更新的操作放在UI线程中
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}

	}

	/**
	 * 防止快速滑动时图片错位的问题
	 */
	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url)) {
			return true;
		}
		return false;
	}

	// 用于在UI线程中更新界面
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;

			if (bitmap != null) {
				if (isCrop(photoToLoad.imageView)) {
					photoToLoad.imageView.setScaleType(ScaleType.CENTER_CROP);
				}
				photoToLoad.imageView.setImageBitmap(bitmap);
				// new
				// MyAnimation(mContext).setImageAlpha(photoToLoad.imageView);
			}
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

	/**
	 * 保存图片到SD卡
	 */
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			os.write(HEAD_STR.getBytes());
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
				// System.out.println("保存成功!");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					is.close();
					os = null;
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	/**
	 * 删除一个缓存在SD卡或者内存中的图片
	 */
	public boolean deleteCacheImage(String url) {
//		url = SystemUtils.changeTestImgUrl(url);
		memoryCache.removeBitmapFromCache(url);
		File file = fileCache.getFile(url);
		if (file == null) {
			return false;
		}
		if (file.exists()) {
			return file.delete();
		} else {
			return false;
		}
	}

	/** 将上传的图片保存的自己的缓存文件夹中 */
	public void saveImgCache(String imgPath, String url) {
//		url = SystemUtils.changeTestImgUrl(url);
		try {
			Bitmap bitmap = PicUtils.decodeFile(imgPath);
			if (bitmap != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				File imageFile = fileCache.getFile(url);
				CopyStream(new ByteArrayInputStream(baos.toByteArray()), new FileOutputStream(imageFile));
				bitmap.recycle();
				bitmap = null;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/** 只从SD卡缓存的目录里得到Bitmap */
	public Bitmap getBitMapFromSdcard(String url) {
//		url = SystemUtils.changeTestImgUrl(url);
		Bitmap bmp = null;
		if (sdcardCache) {
			File f = fileCache.getFile(url);
			if (f.exists()) {
				bmp = decodeFile(f);
			}
		}
		return bmp;
	}

	private boolean isCrop(final ImageView imageView) {
		String image_crop_inside = (String) imageView.getTag(R.string.image_crop_inside);
		if (!TextUtils.isEmpty(image_crop_inside) && image_crop_inside.equals(imageView.//
				getResources().getString(R.string.image_crop_inside))) {
			return true;
		} else {
			return false;
		}
	}
}
