package com.yu.car_android.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.yu.car_android.CarConfig;
import com.yu.car_android.util.DESUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 网络请求
 * 
 * @author Administrator
 * 
 */
public class HttpRequest {

	// 网络状态-OK
	private static final int REPONSE_OK = 200;
	private static final int CONNECTION_TIMEOUT = 1000 * 10;//连接超时时间
	private static final int SO_TIMEOUT = 1000 * 15;//响应超时时间
	// 创建cookie store的本地实例
	private static final CookieStore cookieStore = new BasicCookieStore();
	
	private final static int RETRY_TIME = 2;
	
	private static String appCookie;
	private static String appUserAgent;
	
	public HttpRequest() {
	}

	private static String getCookie(Context context) {
		if (appCookie == null || appCookie == "") {
			appCookie = CarConfig.shareCookie(context);
		}
		return appCookie;
	}

	
	/**
	 * user-agent
	 * 
	 * @param context
	 * @return
	 */
	private static String getUserAgent(Context context) {
//		if (appUserAgent == null || appUserAgent == "") {
//			StringBuilder ua = new StringBuilder(ApiClient.HOST);
//			ua.append('/' + Ka360Helper.getPackageInfo(context).versionName + '_' + Ka360Helper.getPackageInfo(context).versionCode);// App版本
//			ua.append("/Android");// 手机系统平台
//			ua.append("/" + android.os.Build.VERSION.RELEASE);// 手机系统版本
//			ua.append("/" + android.os.Build.MODEL); // 手机型号
//			ua.append("/" + Ka360Helper.getOnly(context));// 客户端唯一标识
//			ua.append("/" + Ka360Helper.getChannelId(context));// 渠道
//			appUserAgent = ua.toString();
//		}
		return appUserAgent;
	}
	/**
	 * 获取httpclient 实例
	 * 
	 * @return
	 */
	private static HttpClient getHttpClient() {
		HttpParams params=new BasicHttpParams();//http参数设置
		HttpConnectionParams.setConnectionTimeout(params, HttpRequest.CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, HttpRequest.SO_TIMEOUT);
		
		HttpClient httpClient = new DefaultHttpClient(params);
		//强制执行Cookie
		httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2965);
		
		return httpClient;
	}
	
	private static HttpContext getHttpContext() {
		// 创建本地的HTTP内容
		HttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		return localContext;
	}
	/**
	 * 
	 * @param context
	 * @param http
	 */
	private static void setHeader(Context context, HttpRequestBase http) {
		// 对post请求覆盖默认cookie策略
		http.setHeader(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
//		http.setHeader("Host", ApiClient.HOST);
		http.setHeader("Connection", "Keep-Alive");
		http.setHeader("Cookie", getCookie(context));
//		http.setHeader("User-Agent", getUserAgent(context));
	}

	/**
	 * get请求
	 * 
	 * @param context
	 * @param url
	 * @return
	 */
	public static String get(Context context, String url) throws Exception {
		try {
			// httpClient
			HttpClient httpClient = getHttpClient();

			// GET Request
			HttpGet get = new HttpGet(url);
			setHeader(context, get);

			// 发送请求,获得影响结果
			HttpResponse httpResponse = httpClient.execute(get, getHttpContext());

			if (httpResponse.getStatusLine().getStatusCode() == REPONSE_OK) {
				// 处理cookie
				handlerCookie(context, cookieStore);
				// 取出回应字串
				byte[] data = EntityUtils.toByteArray(httpResponse.getEntity());
				return new String(data, HTTP.UTF_8);
			}
		} catch (Exception e) {
//			throw Ka360Exception.http(e);
		}
		return null;

	}

	/**
	 * post请求
	 * 
	 * @param context
	 * @param url
	 * @return
	 */
	public static String post(Context context, String url, List<NameValuePair> params) throws Exception {
		int time = 0;
		do {
			try {
				// httpClient
				HttpClient httpClient = getHttpClient();

				// POST Request
				HttpPost post = new HttpPost(url);
				setHeader(context, post);

				// 设置请求参数
				post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

				// 发送请求,获得影响结果
				HttpResponse httpResponse = httpClient.execute(post, getHttpContext());

				// 若状态码为200
				if (httpResponse.getStatusLine().getStatusCode() == REPONSE_OK) {
					// 处理cookie
					handlerCookie(context, cookieStore);
					// 取出回应字串
					byte[] data = EntityUtils.toByteArray(httpResponse.getEntity());
					return new String(data, HTTP.UTF_8);
				}
			} catch (Exception e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
					}
					continue;
				}
//				throw Ka360Exception.http(e);
			}
		} while (time < RETRY_TIME);
		return null;
	}

	/**
	 * post 附件请求
	 * 
	 * @param context
	 * @param url
	 * @param entity
	 * @return
	 * @throws Ka360Exception
	 */
	public static String post(Context context, String url, MultipartEntity entity) throws Exception {
		try {
			// httpClient
			HttpClient httpClient = getHttpClient();

			// POST Request
			HttpPost post = new HttpPost(url);
			setHeader(context, post);

			post.setEntity(entity);

			// 发送请求,获得影响结果
			HttpResponse httpResponse = httpClient.execute(post, getHttpContext());

			// 若状态码为200
			if (httpResponse.getStatusLine().getStatusCode() == REPONSE_OK) {
				// 处理cookie
				handlerCookie(context, cookieStore);
				// 取出回应字串
				byte[] data = EntityUtils.toByteArray(httpResponse.getEntity());
				return new String(data, HTTP.UTF_8);
			}
		} catch (Exception e) {
//			throw Ka360Exception.http(e);
		}
		return null;

	}

	/**
	 * get
	 * 
	 * @param context
	 * @param url
	 * @return
	 * @throws Ka360Exception
	 */
	public static HttpEntity getEntity(Context context, String url) throws Exception {
		try {
			// httpClient
			HttpClient httpClient = getHttpClient();

			// GET Request
			HttpGet get = new HttpGet(url);
			setHeader(context, get);

			// 发送请求,获得影响结果
			HttpResponse httpResponse = httpClient.execute(get, getHttpContext());

			if (httpResponse.getStatusLine().getStatusCode() == REPONSE_OK) {
				return httpResponse.getEntity();
			}
		} catch (Exception e) {
//			throw Ka360Exception.http(e);
		}
		return null;
	}

	/**
	 * 获取网络图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getNetBitmap(Context context, String url) throws Exception {
		try {
			HttpEntity entity = getEntity(context, url);
			if (entity != null)
				return BitmapFactory.decodeStream(entity.getContent());
		} catch (Exception e) {
//			throw Ka360Exception.http(e);
		}
		return null;
	}

	/**
	 * post加密
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private static List<NameValuePair> encryptParams(List<NameValuePair> params) throws Exception {
		List<NameValuePair> rParams = new ArrayList<NameValuePair>(params.size());
		for (NameValuePair nvp : params)
			rParams.add(new BasicNameValuePair(nvp.getName(), nvp.getValue() == null ? null : DESUtils.encrypt(nvp.getValue())));
		return rParams;
	}

	/**
	 * 处理cookie
	 * 
	 * @param context
	 * @param cookieStore
	 */
	private static void handlerCookie(Context context, CookieStore cookieStore) {
		List<Cookie> cookies = cookieStore.getCookies();
		String tmpCookies = "";
		for (Cookie ck : cookies) {
			tmpCookies += ck.toString() + ";";
		}
		// 保存cookie
		if (tmpCookies != "") {
			CarConfig.editorCookie(context, tmpCookies);
			appCookie = tmpCookies;
		}
	}

}
