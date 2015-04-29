package com.yu.car_android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库操作类
 * 
 * @author Administrator
 * 
 */
public class DBhelper extends SQLiteOpenHelper {

	private static DBhelper dbhelper = null;
	public static final int version = 8;
	public static final String name = "Car_app.db";
	private Context context;

	public DBhelper(Context context) {
		super(context, name, null, version);
		this.context = context;
	}

	public synchronized static DBhelper getInstance(Context context) {
		if (dbhelper == null) {
			
		}
		return dbhelper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
