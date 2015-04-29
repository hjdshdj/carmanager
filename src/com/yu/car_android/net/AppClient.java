package com.yu.car_android.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.yu.car_android.util.MD5Utils;

import android.content.Context;
import android.content.Entity.NamedContentValues;

/**
 * 访问网络
 * 
 * @author Administrator
 * 
 */
public class AppClient {
	 public static final String HOME_URL="http://hunboapi.xiaoliangkou.com:8901";
	 public static final String INTENFERCE_TYPE="actionType";
	 public static final String INTENFERCE_JSON="jsonString";
	 
	 AppClient(){}
	/**
	 * 简单类型参数post请求
	 * 
	 * @param context
	 * @param url
	 * @param params
	 * @return
	 * @throws Ka360Exception
	 */
	public static JSONObject api(Context context, String url, List<NameValuePair> params) throws Exception {
		JSONObject jsonObject;
		try {
			String json;

			if (params == null)
				json = HttpRequest.get(context, url);// get请求
			else
				json = HttpRequest.post(context, url, params);// post请求

			jsonObject = new JSONObject(json);
		} catch (Exception e) {
//			Ka360Exception.json(e);
			jsonObject = new JSONObject();
		}
		return jsonObject;
	}

	/**
	 * 多类型参数api post请求
	 * 
	 * @param context
	 * @param url
	 * @param entity
	 * @return
	 * @throws Ka360Exception
	 */
	public static JSONObject apiMultipart(Context context, String url, MultipartEntity entity) throws Exception {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(HttpRequest.post(context, url, entity));
		} catch (Exception e) {
//			Ka360Exception.json(e);
			jsonObject = new JSONObject();
		}
		return jsonObject;
	}
	
	/**
	 * 请求登陆界面
	 * @param context----application	
	 * @param name-----接口名
	 * @param jsString----传递的参数（只能唯一）
	 * @throws Exception 
	 */
	public static void loginUser(Context context,String name,String jsString) throws Exception{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(AppClient.INTENFERCE_TYPE,MD5Utils.MD5(name)));
		params.add(new BasicNameValuePair(AppClient.INTENFERCE_JSON, jsString));
		JSONObject js=api(context, HOME_URL, params);
		System.out.println("login::"+js);
	}
}
