package com.yu.car_android.ui.account;

import android.os.Bundle;
import android.widget.Button;

import com.yu.car_android.R;
import com.yu.car_android.ui.BaseActivity;
import com.yu.car_android.ui.FrameActivity;

/**
 * 账号
 * 
 * @author Administrator
 * 
 */
public class AccountActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);
	}

	@Override
	protected void onResume() {
		super.onResume();
		FrameActivity fa = (FrameActivity) getParent();
		Button t_center = (Button) fa.findViewById(R.id.t_center);
		t_center.setText(getResources().getString(R.string.tw_account));


	}
}
