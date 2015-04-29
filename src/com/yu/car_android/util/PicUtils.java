package com.yu.car_android.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.yu.car_android.Constants;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

/**
 * 图片处理工具类
 * 
 * @author Administrator
 * 
 */
public class PicUtils {
	public static final int CUT_PIC = 106;
	public static final int TAKE_PHOTO = 107;
	public static final int PHOTO_FROM_BUCKET = 108;

	public static int TWO_MB = 1024 * 1024 * 2;

	/** 保存Bitmap到sd卡 */
	public static void saveBitmapToSdcard(Bitmap bitmap, String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}

		ByteArrayOutputStream baos = null;
		FileOutputStream fos = null;
		try {
			baos = new ByteArrayOutputStream();
			fos = new FileOutputStream(filePath);
			if (fos != null) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				fos.write(baos.toByteArray());
				baos.close();
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 从图库的Uri中读取图片资源 */
	public static Bitmap getPhotoFromUri(Context context, long id) {
		ContentResolver resolver = context.getContentResolver();
		Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(resolver, id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
		return bitmap;
	}

	/**
	 * 图片按比例大小压缩方法（根据Bitmap图片压缩），这个压缩方法对图片质量没有损失，只是改变了大小。opts.inSampleSize
	 * 代表缩放比例，8代表是原图的八分之一，4代表是原图的四分之一，我这里只对质量做了压缩，懒得判断大小进行压缩了
	 * 
	 * @author weichenglin
	 */
	public static String compressBm(String filePath) {
		File imgFile = null;
		do {
			filePath = toCompress(filePath);
			imgFile = new File(filePath);
		} while (imgFile.length() > 1024 * 1024 * 5);

		return filePath;
	}

	private static String toCompress(String filePath) {
		String PicPath = Environment.getExternalStorageDirectory().getPath() + Constants.BAIHE_PIC_ROOT + "/compress.jpg";
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = false;

		File imgFile = new File(filePath);
		if (imgFile.length() > 1024 * 1024 * 5) {
			opts.inSampleSize = 4;
		} else if (imgFile.length() > 1024 * 1024 * 2) {
			opts.inSampleSize = 2;
		} else {
			return filePath;
		}
		Bitmap myBitmap = BitmapFactory.decodeFile(filePath, opts);
		saveBitmapToSdcard(myBitmap, PicPath);
		myBitmap.recycle();
		return PicPath;
	}

	/**
	 * 防止内存溢出的解析Bitmap的方法，图片内存溢出真是个让人头疼的问题，现在的照相机像素越来越高，不知道除了压缩图片外是否
	 * 还有别的处理内存溢出的办法，现在的这个方法据说是从android的源代码里拷贝出来的，暂时顶一下吧
	 * 
	 * @author 魏成林 2013-09-09
	 */
	public static Bitmap decodeFile(String imageFile) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imageFile, opts);
		opts.inSampleSize = computeSampleSize(opts, -1, getScaleSize());
		opts.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(imageFile, opts);
	}

	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/** 是否有头像 */
	public static boolean isHaveAvatar(String url) {
		if (TextUtils.isEmpty(url)) {// 增加判空，否则可能空指针
			return false;
		} else {
			if (url.contains("noHeadImg")) {
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * 
	 * @param uri
	 * @param pContext
	 * @param pPicName
	 */
	public void startPicCut(Uri uri, Context pContext, String pPicName) {
		Intent intentCarema = new Intent("com.android.camera.action.CROP");
		intentCarema.setDataAndType(uri, "image/*");
		intentCarema.putExtra("crop", true);
		// aspectX aspectY是宽高比例
		intentCarema.putExtra("aspectX", 100);
		intentCarema.putExtra("aspectY", 56);
		// outputX outputY 是裁剪图片的宽高
		int x = (int) SystemUtils.dip2px(pContext, 220);
		intentCarema.putExtra("outputX", x);
		intentCarema.putExtra("outputY", x * 56 / 100);
		intentCarema.putExtra("return-data", true);
		intentCarema.putExtra("outputFormat", "JPEG");
		intentCarema.putExtra("noFaceDetection", true);
		intentCarema.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Constants.BAIHE_PIC_FULL_PATH + pPicName + ".jpg")));
		((Activity) pContext).startActivityForResult(intentCarema, PicUtils.CUT_PIC);
	}

	/** 根据大小来压缩图片 */
	public static Bitmap compressBmBySize(Bitmap bm, int size) {
		File imgFile = null;
		String filePath = "";
		do {
			filePath = CompressBm(bm);
			imgFile = new File(filePath);
		} while (imgFile.length() > size);

		return BitmapFactory.decodeFile(filePath);
	}

	private static String CompressBm(Bitmap bm) {
		String PicPath = Environment.getExternalStorageDirectory().getPath() + Constants.BAIHE_PIC_ROOT + "/compress.jpg";
		saveBitmapToSdcard(bm, PicPath);

		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = false;

		File imgFile = new File(PicPath);
		if (imgFile.length() > 1024 * 1024 * 5) {
			opts.inSampleSize = 4;
		} else if (imgFile.length() > 1024 * 1024 * 2) {
			opts.inSampleSize = 2;
		} else {
			return PicPath;
		}

		Bitmap myBitmap = BitmapFactory.decodeFile(PicPath, opts);
		saveBitmapToSdcard(myBitmap, PicPath);
		myBitmap.recycle();
		return PicPath;
	}

	/** 得到一张图片的缩放尺寸 */
	public static int getScaleSize() {
		int size = 400 * 400;
		if (Constants.screenWidth >= 480 && Constants.screenWidth <= 640) {
			size = 600 * 600;
		} else if (Constants.screenWidth > 640) {
			size = 1280 * 1280;
		} else if (Constants.screenWidth < 480) {
			size = 300 * 300;
		}
		return size;
	}

}
