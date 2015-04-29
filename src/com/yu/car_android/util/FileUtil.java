package com.yu.car_android.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Stack;

import com.yu.car_android.Constants;
import com.yu.car_android.bean.LocalFileInfo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * 文件操作工具类
 * 
 * @author Administrator
 * 
 */
public class FileUtil {

	public static final String tag = "[FileUtil]";
	private final static String HEAD_STR = "dianChang";
	private static String mDataPath = null;

	public static String getMyPath(Context context) {
		if (mDataPath == null) {
			mDataPath = context.getFilesDir().getPath() + "/";
		}
		return mDataPath;
	}

	/**
	 * 获取手机SD卡路径
	 */
	public static String getSDPath() {
		if (isSDCardExist()) {
			return Environment.getExternalStorageDirectory().toString();
		} else {
			return "";
		}
	}

	/**
	 * 判断是否存在SDCard
	 */
	public static boolean isSDCardExist() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取百合缓存路径
	 */
	public static String getBaihePicPath() {
		return getSDPath() + Constants.BAIHE_PIC_ROOT;
	}

	/**
	 * 获取目录下所有文件大小
	 */
	public static ArrayList<LocalFileInfo> getDirFileSize(String dir) {
		if (!isSDCardExist()) {
			return null;
		}
		Stack<String> stack = new Stack<String>();
		// 入栈
		stack.push(dir);
		ArrayList<LocalFileInfo> localFileInfos = new ArrayList<LocalFileInfo>();
		LocalFileInfo localFileInfo = null;
		// 开始遍历文件
		while (!stack.isEmpty()) {
			String filepath = stack.pop();
			if (filepath == null) {
				continue;
			}
			File path = new File(filepath);
			if (path != null && path.exists()) {
				File[] files = path.listFiles();
				if (files == null) {
					continue;
				}
				for (File f : files) {
					if (f.exists() && f.isDirectory()) {
						stack.push(f.getAbsolutePath());
					} else if (f.exists() && f.isFile()) {
						localFileInfo = new LocalFileInfo();
						localFileInfo.size = f.length();
						localFileInfo.filePath = f.getAbsolutePath();
						localFileInfos.add(localFileInfo);
					}
				}
			}
		}
		return localFileInfos;
	}

	/**
	 * 复制文件
	 * 
	 * @param source
	 *            文件源路径
	 * @param target
	 *            文件目标路径
	 * @throws Exception
	 */
	public static void copyFile(String source, String target) throws Exception {
		File in = new File(source);
		File out = new File(target);
		FileInputStream inFile = new FileInputStream(in);
		FileOutputStream outFile = new FileOutputStream(out);
		byte[] buffer = new byte[1024];
		int i = 0;
		while ((i = inFile.read(buffer)) != -1) {
			outFile.write(buffer, 0, i);
		}
		inFile.close();
		outFile.close();
	}

	/**
	 * 收藏图片处理方法
	 * 
	 * @param root
	 *            收藏的目标目录
	 * @param bitmap
	 *            图片Bitmap对象
	 * @param imageName
	 *            图片名称
	 * @return
	 */
	public static String storeImageToFile(String root, Bitmap bitmap, String imageName) {
		if (bitmap == null) {
			return null;
		}
		File file = null;
		RandomAccessFile accessFile = null;
		String path = null;
		File dir = new File(root);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		path = root + imageName;
		file = new File(path + ".jpg");
		ByteArrayOutputStream steam = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, steam);
		byte[] buffer = steam.toByteArray();
		try {
			accessFile = new RandomAccessFile(file, "rw");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			steam.close();
			accessFile.close();
		} catch (IOException e) {
		}

		return path;
	}

	/**
	 * 该方法是用来创建临时文件存放从网络获取的图片
	 */
	public static File createTempFile(String pathString) {
		String folderpath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Baihe" + File.separator;
		File file = null;
		try {
			URL url = new URL(pathString);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("GET");
			if (connection.getResponseCode() == 200) {
				InputStream inputStream = connection.getInputStream();
				file = new File(folderpath, "temp.jpg");
				file.deleteOnExit();
				FileOutputStream out = new FileOutputStream(file);
				byte[] buffer = new byte[2048];
				int length = 0;
				while ((length = inputStream.read(buffer)) != -1) {
					out.write(buffer, 0, length);
				}
				out.close();
				inputStream.close();
			}
		} catch (IOException e) {
		}
		return file.getAbsoluteFile();
	}

	/**
	 * 删除指定目录
	 * 
	 * @param dir
	 */
	public static void deleMir(String dir) {
		File f = new File(dir);
		if (!f.exists()) {
			return;
		}
		if (!f.delete()) {

			File[] fs = f.listFiles();
			for (int i = 0; i < fs.length; i++) {
				if (fs[i].isDirectory()) {
					if (fs[i].getName().equals("SC_Picture")) {
						continue;
					}
					if (!fs[i].delete())
						deleMir(fs[i].getAbsolutePath());
				} else {
					fs[i].delete();
				}
			}
			f.delete();

		}
	}

	/**
	 * 删除指定目录
	 * 
	 * @param dir
	 */
	public static void deleFile(String fileName) {
		File f = new File(fileName);
		if (!f.exists()) {
			return;
		} else {
			f.delete();
			// System.out.println("删除文件...");
		}
	}

	/**
	 * 图片文件过滤器
	 * 
	 * @author Administrator
	 * 
	 */
	public static class Filter implements FileFilter {
		public boolean accept(File f) {
			return f.isDirectory() || f.getName().matches("^.*?\\.(jpg|png|bmp|jpeg|gif|qc)$");
		}
	}

	/**
	 * 获取指定目录下地所有文件
	 * 
	 * @param 目录
	 * 
	 * @return 文件路徑集合
	 */
	public static ArrayList<String> getAllFiles(File root, ArrayList<String> list) {
		Filter filter = new Filter();
		File files[] = root.listFiles(filter);
		if (files != null) {
			if (list == null)
				list = new ArrayList<String>();
			for (File f : files) {
				if (f.isDirectory()) {
					if (!f.getName().toString().matches("^\\..*")) {
						// getAllFiles(f,list);
					}
				} else {
					list.add(f.getAbsolutePath());
				}
			}
			return list;
		} else {
			return null;
		}
	}

	/**
	 * Decode and sample down a bitmap from resources to the requested width and
	 * height.
	 * 
	 * @param res
	 *            The resources object containing the image data
	 * @param resId
	 *            The resource id of the image data
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @return A bitmap sampled down from the original with the same aspect
	 *         ratio and dimensions that are equal to or greater than the
	 *         requested width and height
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

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

	/**
	 * Decode and sample down a bitmap from a file to the requested width and
	 * height.
	 * 
	 * @param filename
	 *            The full path of the file to decode
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @return A bitmap sampled down from the original with the same aspect
	 *         ratio and dimensions that are equal to or greater than the
	 *         requested width and height
	 */
	public static synchronized Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		try {
			File file = new File(filename);
			InputStream inputStream = new FileInputStream(file);
			// byte[] buffer;
			//
			// try {
			// buffer = new byte[inputStream.available()];
			// inputStream.read(buffer);
			// int size = HEAD_STR.length();
			// return BitmapFactory.decodeByteArray(buffer, size,
			// buffer.length - size, options);
			//
			// } catch (IOException e) {
			// e.printStackTrace();
			// } finally {
			// if (inputStream != null) {
			// try {
			// inputStream.close();
			// inputStream = null;
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// }
			// }
			// return BitmapFactory.decodeFile(filename, options);
			return BitmapFactory.decodeStream(inputStream, null, options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static synchronized Bitmap decodeSampledBitmapFromInput(InputStream in, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		try {
			in.mark(in.available() + 1);
			in.reset();
			BitmapFactory.decodeStream(in, null, options);
		} catch (IOException e) {

		}
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(in, null, options);
	}

	/**
	 * Calculate an inSampleSize for use in a {@link BitmapFactory.Options}
	 * object when decoding bitmaps using the decode* methods from
	 * {@link BitmapFactory}. This implementation calculates the closest
	 * inSampleSize that will result in the final decoded bitmap having a width
	 * and height equal to or larger than the requested width and height. This
	 * implementation does not ensure a power of 2 is returned for inSampleSize
	 * which can be faster when decoding but results in a larger bitmap which
	 * isn't as useful for caching purposes.
	 * 
	 * @param options
	 *            An options object with out* params already populated (run
	 *            through a decode* method with inJustDecodeBounds==true
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @return The value to be used for inSampleSize
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}

			// This offers some additional logic in case the image has a strange
			// aspect ratio. For example, a panorama may have a much larger
			// width than height. In these cases the total pixels might still
			// end up being too large to fit comfortably in memory, so we should
			// be more aggressive with sample down the image (=larger
			// inSampleSize).

			final float totalPixels = width * height;

			// Anything more than 2x the requested pixels we'll sample down
			// further.
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

}
