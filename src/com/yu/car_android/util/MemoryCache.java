package com.yu.car_android.util;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.yu.car_android.Constants;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 内存缓存
 * 
 * @author Administrator
 * 
 */
public class MemoryCache {

	// 放入缓存时是个同步操作
	// LinkedHashMap构造方法的最后一个参数true代表这个map里的元素将按照最近使用次数由少到多排列，即LRU
	// 这样的好处是如果要将缓存中的元素替换，则先遍历出最近最少使用的元素来替换以提高效率
	private Map<String, Bitmap> cache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));
	// 缓存中图片所占用的字节，初始0，将通过此变量严格控制缓存所占用的堆内存
	private long size = 0;// current allocated size
	// 缓存只能占用的最大堆内存
	private long limit = 1000000;// max memory in bytes
	private final int HARD_CACHE_SIZE = 1024 * 1024 * Constants.PHONE_MEM / 8;
	private final int HARD_CACHE_SIZE_TEMP = 1024 * 1024 * 16 / 8;

	/**
	 * 当mHardBitmapCache的key大于30的时候，会根据LRU算法把最近没有被使用的key放入到这个缓存中。
	 * Bitmap使用了SoftReference，当内存空间不足时，此cache中的bitmap会被垃圾回收掉
	 */
	private final static ConcurrentHashMap<String, SoftReference<Bitmap>> mSoftBitmapCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(32);

	private LruCache<String, Bitmap> mHardBitmapCache = new LruCache<String, Bitmap>(HARD_CACHE_SIZE == 0 ? HARD_CACHE_SIZE_TEMP : HARD_CACHE_SIZE) {
		/**
		 * Measure item size in bytes rather than units which is more practical
		 * for a bitmap cache
		 */
		@Override
		protected int sizeOf(String key, Bitmap bitmap) {
			return bitmap.getRowBytes() * bitmap.getHeight();
		}

		@Override
		protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
			// 硬引用缓存区满，将一个最不经常使用的oldvalue推入到软引用缓存区
			mSoftBitmapCache.put(key, new SoftReference<Bitmap>(oldValue));
		}
	};

	public MemoryCache() {
		// use 25% of available heap size
		setLimit(Runtime.getRuntime().maxMemory() / 4);
	}

	public void setLimit(long new_limit) {
		limit = new_limit;
	}

	public Bitmap get(String url) {
		synchronized (mHardBitmapCache) {
			final Bitmap bitmap = mHardBitmapCache.get(url);
			if (bitmap != null && !bitmap.isRecycled()) {
				return bitmap;
			} else {
				// 如果为空从缓存中删除
				mHardBitmapCache.remove(url);
			}
		}
		// 如果mHardBitmapCache中找不到，到mSoftBitmapCache中找
		SoftReference<Bitmap> bitmapReference = mSoftBitmapCache.get(url);
		if (bitmapReference != null) {
			final Bitmap bitmap = bitmapReference.get();
			if (bitmap != null && !bitmap.isRecycled()) {
				// 将图片移动到硬缓存
				if (mHardBitmapCache.get(url) == null) {
					mHardBitmapCache.put(url, bitmap);
				}
				mSoftBitmapCache.remove(url);
				return bitmap;
			} else {
				mSoftBitmapCache.remove(url);
			}
		}
		return null;

	}

	public void put(String id, Bitmap bitmap) {
		try {
			if (cache.containsKey(id))
				size -= getSizeInBytes(cache.get(id));
			cache.put(id, bitmap);
			size += getSizeInBytes(bitmap);
			checkSize();
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

	/**
	 * 将图片添加到硬缓存
	 */
	public void insertCache(final String url, final Bitmap bitmap) {
		if (url == null || bitmap == null) {
			return;
		}
		if (mHardBitmapCache != null && mHardBitmapCache.get(url) == null) {
			synchronized (mHardBitmapCache) {
				mHardBitmapCache.put(url, bitmap);
			}
		}
	}

	/**
	 * 严格控制堆内存，如果超过将首先替换最近最少使用的那个图片缓存
	 * 
	 */
	private void checkSize() {
		// Log.i(TAG, "cache size=" + size + " length=" + cache.size());
		if (size > limit) {
			// 先遍历最近最少使用的元素
			Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Bitmap> entry = iter.next();
				size -= getSizeInBytes(entry.getValue());
				iter.remove();
				if (size <= limit)
					break;
			}
			// Log.i(TAG, "Clean cache. New size " + cache.size());
		}
	}

	public void clear() {
		cache.clear();
	}

	/**
	 * 图片占用的内存
	 */
	long getSizeInBytes(Bitmap bitmap) {
		if (bitmap == null)
			return 0;
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	/**
	 * 从缓存中移除指定图片的Bitmap资源
	 */
	public void removeBitmapFromCache(String url) {
		if (url != null) {
			if (mHardBitmapCache.get(url) != null) {
				recycleBitmap(mHardBitmapCache.get(url));
				mHardBitmapCache.remove(url);
			} else if (mSoftBitmapCache.containsKey(url)) {
				SoftReference<Bitmap> reference = mSoftBitmapCache.get(url);
				Bitmap bitmap = reference.get();
				recycleBitmap(bitmap);
				mSoftBitmapCache.remove(url);
			}
		}
	}

	/**
	 * 回收Bitmap资源
	 */
	private void recycleBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
	}
}
