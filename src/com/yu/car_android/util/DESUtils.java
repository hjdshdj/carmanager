package com.yu.car_android.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class DESUtils {

	private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };

	// desPlus encrypt cardm360
	private static String strKey = String.valueOf(new char[] { '3', 'd', '6', 'c', 'f', 'a', '1', 'a', 'c', 'f', '5', '6', '4', '4', '8', '7', '8',
			'0', 'b', '2', '4', 'f', 'f', 'c', '2', '2', '8', 'f', 'f', 'a', '9', '9' });

	private static String transformation = String.valueOf(new char[] { 'b', '8', '7', '9', '8', '7', '3', '2', '3', '0', 'f', '6', '2', 'a', '0',
			'c', 'f', '6', '3', '1', 'c', '5', '2', 'd', '8', '4', 'c', 'f', '1', '2', 'e', 'b', 'c', '3', '8', 'f', '9', '3', '8', '1', '1', '7',
			'9', 'c', '7', '8', '5', 'f' });

	private static String des = String.valueOf(new char[] { '7', 'd', '1', 'c', '7', '4', 'a', '5', 'f', 'd', '4', '6', '4', '9', 'd', '0' });

	static {
		strKey = DESPlus.decrypt(strKey);
		transformation = DESPlus.decrypt(transformation);
		des = DESPlus.decrypt(des);
	}

	public static String encrypt(String encryptString) throws Exception {

		IvParameterSpec zeroIv = new IvParameterSpec(iv);

		SecretKeySpec key = new SecretKeySpec(strKey.getBytes(), des);

		Cipher cipher = Cipher.getInstance(transformation);

		cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);

		byte[] encryptedData = cipher.doFinal(encryptString.getBytes());

		return Base64.encodeToString(encryptedData, 1);

	}

	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

}
