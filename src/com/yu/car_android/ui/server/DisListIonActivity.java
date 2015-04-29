package com.yu.car_android.ui.server;

import android.os.Bundle;
import android.widget.ListView;

import com.yu.car_android.R;
import com.yu.car_android.ui.BaseActivity;

/**
 * 实现ion测试的listview界面
 * @author Administrator
 *
 */
public class DisListIonActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server_dision_activity);
		ListView dision_list=(ListView) findViewById(R.id.dision_list);
		//访问网络拿去数据
		setRequest();
		
	}

	/**
	 * 访问网络
	 */
	private void setRequest() {
	}
}
