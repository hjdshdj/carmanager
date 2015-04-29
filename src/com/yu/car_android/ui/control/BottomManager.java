package com.yu.car_android.ui.control;

import java.util.Observer;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yu.car_android.R;

/**
 * 底部管理器
 * 
 * @author Administrator
 * 
 */
public class BottomManager  implements Observer {

	private static BottomManager bottomManager = new BottomManager();
	private TextView home_tv_manager;
	private TextView server_tv_manager;
	private TextView information_tv_manager;
	private TextView account_tv_manager;
	private LinearLayout bottom_manager;

	private BottomManager() {
	}

	public static BottomManager getInstance() {
		if (bottomManager == null)
			bottomManager = new BottomManager();
		return bottomManager;
	}

	/**
	 * 初始化数据
	 * 
	 * @param activity
	 */
	public void init(Activity activity) {
		bottom_manager = (LinearLayout) activity.findViewById(R.id.bottom_manager);
		home_tv_manager = (TextView) activity.findViewById(R.id.home_tv_manager);
		server_tv_manager = (TextView) activity.findViewById(R.id.server_tv_manager);
		information_tv_manager = (TextView) activity.findViewById(R.id.information_tv_manager);
		account_tv_manager = (TextView) activity.findViewById(R.id.account_tv_manager);
	}

	private void setListener(){
		home_tv_manager.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		server_tv_manager.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			}
		});
		information_tv_manager.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			}
		});
		account_tv_manager.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			}
		});
	}
	@Override
	public void update(java.util.Observable observable, Object data) {

	}

}
