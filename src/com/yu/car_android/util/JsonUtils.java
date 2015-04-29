package com.yu.car_android.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author ANDSON
 * @date 2014年6月3日
 */
public class JsonUtils {

	/**
	 * 
	 * @param jsonObject
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public static int getInt(JSONObject jsonObject, String key) throws JSONException {
		return getInt(jsonObject, key, 0);
	}

	public static int getInt(JSONObject jsonObject, String key, int defVal) throws JSONException {
		if (isNotNull(jsonObject, key))
			return jsonObject.getInt(key);
		return defVal;
	}

	/**
	 * 
	 * @param jsonObject
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public static String getString(JSONObject jsonObject, String key) throws JSONException {
		return getString(jsonObject, key, null);
	}

	public static String getString(JSONObject jsonObject, String key, String defVal) throws JSONException {
		if (isNotNull(jsonObject, key))
			return jsonObject.getString(key);
		return defVal;
	}

	/**
	 * 
	 * @param jsonArray
	 * @param index
	 * @return
	 * @throws JSONException
	 */
	public static String getString(JSONArray jsonArray, int index) throws JSONException {
		return getString(jsonArray, index, null);
	}

	public static String getString(JSONArray jsonArray, int index, String defVal) throws JSONException {
		if (isNotNull(jsonArray, index))
			return jsonArray.getString(index);
		return defVal;
	}

	/**
	 * 
	 * @param jsonObject
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public static double getDouble(JSONObject jsonObject, String key) throws JSONException {
		return getDouble(jsonObject, key, 0);
	}

	public static double getDouble(JSONObject jsonObject, String key, double defVal) throws JSONException {
		if (isNotNull(jsonObject, key))
			return jsonObject.getDouble(key);
		return defVal;
	}

	/**
	 * 
	 * @param jsonObject
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public static long getLong(JSONObject jsonObject, String key) throws JSONException {
		return getLong(jsonObject, key, 0);
	}

	public static long getLong(JSONObject jsonObject, String key, long defVal) throws JSONException {
		if (isNotNull(jsonObject, key))
			return jsonObject.getLong(key);

		return defVal;
	}

	/**
	 * 
	 * @param jsonObject
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public static JSONArray getJsonArray(JSONObject jsonObject, String key) throws JSONException {
		return getJsonArray(jsonObject, key, new JSONArray());
	}

	/**
	 * 
	 * @param jsonObject
	 * @param key
	 * @param defVal
	 * @return
	 * @throws JSONException
	 */
	public static JSONArray getJsonArray(JSONObject jsonObject, String key, JSONArray defVal) throws JSONException {
		if (isNotNull(jsonObject, key))
			return jsonObject.getJSONArray(key);

		return defVal;
	}

	/**
	 * 
	 * @param jsonObject
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getJSONObject(JSONObject jsonObject, String key) throws JSONException {
		return getJSONObject(jsonObject, key, new JSONObject());
	}

	/**
	 * 
	 * @param jsonObject
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getJSONObject(JSONObject jsonObject, String key, JSONObject defVal) throws JSONException {
		if (isNotNull(jsonObject, key))
			return jsonObject.getJSONObject(key);
		return defVal;
	}

	/**
	 * 
	 * @param jsonArray
	 * @param index
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getJSONObject(JSONArray jsonArray, int index) throws JSONException {
		return getJSONObject(jsonArray, index, new JSONObject());
	}

	/**
	 * 
	 * @param jsonArray
	 * @param index
	 * @param defVal
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getJSONObject(JSONArray jsonArray, int index, JSONObject defVal) throws JSONException {
		if (isNotNull(jsonArray, index))
			return jsonArray.getJSONObject(index);

		return defVal;
	}

	/**
	 * 
	 * @param jsonObject
	 * @param key
	 * @return
	 */
	public static boolean isNotNull(JSONObject jsonObject, String key) {
		return (jsonObject != null && !jsonObject.isNull(key));
	}

	/**
	 * 
	 * @param jsonArray
	 * @param index
	 * @return
	 */
	public static boolean isNotNull(JSONArray jsonArray, int index) {
		return (jsonArray != null && !jsonArray.isNull(index));
	}
}
