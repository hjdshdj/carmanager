package com.yu.car_android.bean;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户登陆
 * 
 * @author Administrator
 * 
 */
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3004084002869663393L;

	private String phone_number;
	private String password;

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * bean整合成json
	 * 
	 * @param user
	 * @return
	 * @throws JSONException
	 */
	public static String conformBeanToJson(User user) {
		String data = "";
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("phone_number", user.getPhone_number());
			jsonObject.put("password", user.getPassword());
			data = jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

}
