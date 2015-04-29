package com.yu.car_android.util;

import java.io.File;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.yu.car_android.Constants;
import com.yu.car_android.bean.ApkItem;

/**
 * 公用工具类
 * 
 * @author Administrator
 * 
 */
public class SystemUtils {
	public static final String TAG = SystemUtils.class.getSimpleName();

	/** 将dip转化为px * */
	public static float dip2px(Context context, float dipValue) {
		float value = 0;
		value = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, context.getResources().getDisplayMetrics());
		return value;
	}

	/**
	 * 判断某个表是否存在
	 */
	public static boolean isTableExist(SQLiteDatabase db, String tableName) {
		boolean exist = false;
		Cursor cursor = db.rawQuery("select name from sqlite_master where name = ?", new String[] { tableName });
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				exist = true;
			}
			cursor.close();
		}
		return exist;
	}

	/**
	 * 将格林时间转换为标准DATE时间（比如新浪微博返回的那个格林时间）
	 */
	public static Date convertTime(String GMT_time) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

		Date myDate = null;
		try {
			myDate = sdf.parse(GMT_time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return myDate;
	}

	/**
	 * 中国移动号段：134、135、136、137、138、139、150、151、152、157、158、159、182、183、184、187、
	 * 188、178(4G)、147(上网卡) 中国电信号段：133、153、180、181、189 、177(4G)
	 * 中国联通号段：130、131、132、155、156、185、186、176(4G)、145(上网卡) 卫星通信号段：1349
	 * 虚拟运营商号段：170
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isPhoneNum(String mobiles) {
		// Pattern p =
		// Pattern.compile("^((14[5,7])|(17[0,6,7,8])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		// Matcher m = p.matcher(mobiles);
		// return m.matches();

		if (isNumber(mobiles) && mobiles.length() == 11 && mobiles.startsWith("1")) {
			return true;
		} else {
			return false;
		}
	}

	public static final int SIZE_120 = 120, SIZE_250 = 250, SIZE_350 = 350, SIZE_700 = 700;

	/** 转换从网络要下载的图片的尺寸 */
	public static String changeImageSize(String url, int sizeType) {
		final String jpg = ".jpg";

		if (url == null || !url.endsWith(jpg)) {
			return url;
		}
		String imageUrl = url;
		imageUrl = imageUrl.substring(0, imageUrl.length() - jpg.length());
		imageUrl = imageUrl + "-" + sizeType + jpg;
		return imageUrl;
	}

	/** 将java的13位时间长度转换成10位的PHP长度 */
	public static String javaTimeToPhpTime(String time) {
		if (time == null) {
			return null;
		}
		if (time.length() == 13) {
			return time.substring(0, time.length() - 3);
		} else {
			return time;
		}

	}

	/** 将String日期转换为Long型日期 */
	public static long dateForStringToLong(String strTime, String formatType) {
		Date date = dateForStringToDate(strTime, formatType);
		if (date == null) {
			return 0;
		} else {
			long currentTime = date.getTime();
			return currentTime;
		}
	}

	/** 将Long日期转换为String型日期 */
	public static String longToString(long time, String formatType) {
		SimpleDateFormat formatter = null;
		formatter = new SimpleDateFormat(formatType);
		String str = formatter.format(time);
		return str;
	}

	/** 将String日期转换为Date日期 */
	public static Date dateForStringToDate(String strTime, String formatType) {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		try {
			date = formatter.parse(strTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/** 从标准View 得到图片 */
	public static Bitmap getBitmapFromView(View view) {
		view.setDrawingCacheEnabled(true);
		view.measure(View.MeasureSpec.EXACTLY + view.getWidth(), View.MeasureSpec.EXACTLY + view.getHeight());
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}

	/**
	 * 叠加两张图片，以左下角为基准点
	 * 
	 * @param pBackground
	 *            背景图
	 * @param pForeground
	 *            前景图
	 * @return
	 */
	public static Bitmap toOverlayBitmap(Bitmap pBackground, Bitmap pForeground) {
		if (pBackground == null || pForeground == null) {
			return null;
		}
		int _BgWidth = pBackground.getWidth();
		int _BgHeight = pBackground.getHeight();
		Bitmap _NewBmp = Bitmap.createBitmap(_BgWidth, _BgHeight, Config.ARGB_8888);
		Canvas _Canvas = new Canvas(_NewBmp);
		_Canvas.drawBitmap(pBackground, 0, 0, null);
		_Canvas.drawBitmap(pForeground, 0, 0, null);
		_Canvas.save(Canvas.ALL_SAVE_FLAG);
		_Canvas.restore();

		return _NewBmp;
	}

	/** 从自定义View中获取bitmap */
	public static Bitmap getCustomViewBitmap(View pView) {
		Bitmap bitmap = Bitmap.createBitmap(pView.getWidth(), pView.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		pView.draw(canvas);
		return bitmap;
	}

	/** 解析出一个JSON的值 */
	public static String ParseJson(String jsonStr, String key) {
		if (jsonStr == null) {
			return null;
		}
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jsonStr);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (jsonObject == null) {
			return "";
		}
		String value = jsonObject.has(key) ? jsonObject.optString(key) : null;
		if (value == null) {
			value = "";
		}
		return value;
	}

	/** MD5加密一个字符串 */
	public final static String MD5(String s) {
		final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/** MD5加密一个字符串 */
	public final static String MD5Lower(String s) {
		final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 重启一个应用
	// public static void restartApp(Context context) {
	// Intent i =
	// context.getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
	// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	// startActivity(i);
	// }

	/** 显示输入法键盘 */
	public static void showInput(Context context, EditText edit) {
		edit.setFocusable(true);
		edit.requestFocus();
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/** 关闭输入法键盘 */
	public static void hideInput(Context context, EditText edit) {
		edit.clearFocus();
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
	}

	/** 四舍五入 */
	public final static String siSheWuRu(String num, int dot) {
		double pow = Math.pow(10, dot);
		float number = Float.parseFloat(num);
		return (Math.round(number * pow) / pow) + "";
	}

	/** 四舍五入 */
	public final static double siSheWuRu(float number, int dot) {
		double pow = Math.pow(10, dot);
		return (Math.round(number * pow) / pow);
	}

	/**
	 * 计算时间差
	 * 
	 * @param pStartTime
	 *            开始时间
	 * @param pEndTime
	 *            结束时间
	 */
	public static void CalculateHttpTime(String pTAG, Date pStartTime, Date pEndTime) {
		long _MillSecond = pEndTime.getTime() - pStartTime.getTime();
		long _Day = _MillSecond / (24 * 60 * 60 * 1000);
		long _Hour = (_MillSecond / (60 * 60 * 1000) - _Day * 24);
		long _Min = ((_MillSecond / (60 * 1000)) - _Day * 24 * 60 - _Hour * 60);
		long _Second = (_MillSecond / 1000 - _Day * 24 * 60 * 60 - _Hour * 60 * 60 - _Min * 60);
	}

	/** 打开发送短信的界面 */
	public static void sendSms(Context context, ArrayList<String> numberList, String sms_body) {
		String mobile = "";
		for (int i = 0; i < numberList.size(); i++) {
			if (i == 0) {
				mobile = numberList.get(i);
			} else {
				mobile = mobile + ";" + numberList.get(i);
			}
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.putExtra("address", mobile);
		intent.putExtra("sms_body", sms_body);
		intent.setType("vnd.android-dir/mms-sms");
		context.startActivity(intent);
	}

	/**
	 * 格式化数据，指定保留几位小数
	 */
	public static String formatSize(long size, int flag) {
		NumberFormat df = NumberFormat.getNumberInstance();
		df.setMaximumFractionDigits(flag);

		StringBuffer buffer = new StringBuffer();
		if (size < 0) {
			buffer.append(0);
			buffer.append("B");
		} else if (size < 1000) {
			buffer.append(size);
			buffer.append("B");
		} else if (size < 1024000) {
			buffer.append(df.format(((double) size) / 1024));
			buffer.append("K");
		} else if (size < 1048576000) {
			buffer.append(df.format(((double) size) / 1048576));
			buffer.append("M");
		} else {
			buffer.append(df.format(((double) size) / 1073741824));
			buffer.append("G");
		}
		return buffer.toString();
	}

	/** 安装一个APK包 */
	public static void installApk(Context context, String path) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/** 得到APK包的信息 */
	public static ApkItem getApkInfor(Context context, String path) {
		File file = new File(path);
		if (TextUtils.isEmpty(path) || !file.exists()) {
			return null;
		}

		PackageManager mPackageManager = context.getPackageManager();
		ApkItem apkItem = new ApkItem();
		PackageInfo ApkInfor = mPackageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);

		if (ApkInfor == null) {
			return null;
		}

		apkItem.appSize = file.length();
		apkItem.appVersion = ApkInfor.versionName;
		apkItem.versionCode = ApkInfor.versionCode;

		ApplicationInfo appInfo = ApkInfor.applicationInfo;
		appInfo.sourceDir = path;
		appInfo.publicSourceDir = path;

		apkItem.appName = appInfo.loadLabel(mPackageManager).toString().trim();
		apkItem.image = appInfo.loadIcon(mPackageManager);
		apkItem.packageName = ApkInfor.applicationInfo.packageName;
		return apkItem;
	}

	/** 获umeng的渠道id值 */
	public static String getUmengChannel(Context pContext) {
		String _ChannelId = "unknowChannel";
		try {
			ApplicationInfo _AppInfo = pContext.getPackageManager().getApplicationInfo(pContext.getPackageName(), PackageManager.GET_META_DATA);
			Object _Value = _AppInfo.metaData.get("UMENG_CHANNEL");
			if (_Value != null) {
				_ChannelId = _Value.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return _ChannelId;
	}

	/** 清除自己的程序产生的notification */
	public static void clearNotification(Context context) {
		NotificationManager nm = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		nm.cancelAll();
	}

	/** 只能输入字母和汉字 */
	public static String LetterAndChinese(String text) {
		for (int i = 0; i < text.length(); i++) {
			char letter = text.charAt(i);
			if (!((letter >= 'a' && letter <= 'z') || (letter >= 'A' && letter <= 'Z') || (letter >= '0' && letter <= '9') || letter > 128)) {
				return letter + "";
			}
		}

		return null;
	}

	/** 返回状态栏的高度 */
	public static int getStatusBarHeight(Activity activity) {
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		return frame.top;
	}

	public static void sortStrList(ArrayList<String> pArrayList) {
		Comparator<String> _StrComparator = new Comparator<String>() {

			@Override
			public int compare(String lhs, String rhs) {
				return lhs.toString().compareTo(rhs.toString());
			}
		};
		Collections.sort(pArrayList, _StrComparator);
	}

	public static boolean match(String value, String keyword) {
		if (value == null || keyword == null)
			return false;
		if (keyword.length() > value.length())
			return false;

		int i = 0, j = 0;
		do {
			if (keyword.charAt(j) == value.charAt(i)) {
				i++;
				j++;
			} else if (j > 0)
				break;
			else
				i++;
		} while (i < value.length() && j < keyword.length());

		return (j == keyword.length()) ? true : false;
	}

	/** 判断是不是小米平板 */
	public static boolean isMiPad() {
		if (Constants.screenWidth == 1536) {
			return true;
		} else {
			return false;
		}
	}

	/** 得到设备的串号 */
	public String getDeviceId(Context pContext) {
		String mStrImei = "";
		try {
			TelephonyManager telephonyManager = (TelephonyManager) pContext.getSystemService(Context.TELEPHONY_SERVICE);
			mStrImei = telephonyManager.getDeviceId();
			if (mStrImei == null || mStrImei.length() <= 0) {
				mStrImei = Secure.getString(pContext.getContentResolver(), Secure.ANDROID_ID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mStrImei;
	}

	public static SpannableString changeTextColorAndSize(CharSequence pstr, int pStarLengh, int pEndLengh, int pColorValue, float pTextSizeValue) {
		SpannableString _TxtSpan = new SpannableString(pstr);
		AbsoluteSizeSpan _SizeSpan = new AbsoluteSizeSpan((int) pTextSizeValue);
		ForegroundColorSpan _ColorSpan = new ForegroundColorSpan(pColorValue);

		if ((pStarLengh + pEndLengh) > pstr.length()) {
			pEndLengh = pstr.length();
		} else {
			pEndLengh = pStarLengh + pEndLengh;
		}
		if (pStarLengh < 0 || pStarLengh > pEndLengh) {
			pStarLengh = 0;
		}
		_TxtSpan.setSpan(_ColorSpan, pStarLengh, pEndLengh, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		_TxtSpan.setSpan(_SizeSpan, pStarLengh, pEndLengh, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return _TxtSpan;
	}

	public static SpannableString changeTextColor(CharSequence pstr, int pStarLengh, int pEndLengh, int pColorValue) {
		SpannableString _TxtSpan = new SpannableString(pstr);
		ForegroundColorSpan _ColorSpan = new ForegroundColorSpan(pColorValue);
		_TxtSpan.setSpan(_ColorSpan, pStarLengh, pEndLengh, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return _TxtSpan;
	}

	public static String getWeekStrByIndex(int pIndex) {
		String _ResStr = "";
		switch (pIndex) {
		case 1:
			_ResStr = "日";
			break;
		case 2:
			_ResStr = "一";
			break;
		case 3:
			_ResStr = "二";
			break;
		case 4:
			_ResStr = "三";
			break;
		case 5:
			_ResStr = "四";
			break;
		case 6:
			_ResStr = "五";
			break;
		case 7:
			_ResStr = "六";
			break;
		}
		return _ResStr;
	}

	/** 得到显示的字体所占的高度 */
	public static int getFontHeight(TextPaint paint) {
		Rect bounds = new Rect();
		String text = "Hello World";
		paint.getTextBounds(text, 0, text.length(), bounds);
		return bounds.height();
	}

	/** 判断字符是不是数字 */
	public static boolean isNumber(String str) {
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[0-9]*");
		java.util.regex.Matcher match = pattern.matcher(str);
		if (match.matches() == false) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 检查字符是否在33-126之间的Ascii表里
	 */
	public static boolean checkIsInAscii(String str) {
		for (int i = 0; i < str.length(); i++) {
			int asc = (int) str.charAt(i);
			if (asc < 33 || asc > 126) {
				return false;
			}
		}
		return true;
	}

	/** 第一个参数是要计算的字符串，第二个参数是TextView */
	public static float getTextWidth(String text, TextView textView) {
		TextPaint paint = textView.getPaint();
		float len = paint.measureText(text);
		return len;
	}

}
