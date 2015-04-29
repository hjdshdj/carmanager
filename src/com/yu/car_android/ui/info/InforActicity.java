package com.yu.car_android.ui.info;

import android.os.Bundle;
import android.widget.Button;

import com.yu.car_android.R;
import com.yu.car_android.ui.BaseActivity;
import com.yu.car_android.ui.FrameActivity;

/**
 * 分类
 * 
 * @author Administrator
 * 
 */
public class InforActicity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
	}
	@Override
	protected void onResume() {
		super.onResume();
		FrameActivity fa= (FrameActivity) getParent();
		Button t_center=(Button)fa.findViewById(R.id.t_center);
		t_center.setText(getResources().getString(R.string.tw_information));
	}
}
