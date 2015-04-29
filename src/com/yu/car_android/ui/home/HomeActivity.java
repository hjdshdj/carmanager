package com.yu.car_android.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.yu.car_android.R;
import com.yu.car_android.ui.BaseActivity;
import com.yu.car_android.ui.FrameActivity;
import com.yu.car_android.ui.fragment.FragmentHomeActivity;
import com.yu.car_android.ui.slidingmenu.SlidingpaneLayoutActivity;

/**
 * 首页
 * 
 * @author Administrator
 * 
 */
public class HomeActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		Button button_manager = (Button) findViewById(R.id.button_manager);
		button_manager.setOnClickListener(this);
		findViewById(R.id.button_fragment).setOnClickListener(this);
		findViewById(R.id.button_slidingpane).setOnClickListener(this);
		findViewById(R.id.slidingmenu).setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		FrameActivity fa = (FrameActivity) getParent();
		Button t_center = (Button) fa.findViewById(R.id.t_center);
		t_center.setText(getResources().getString(R.string.tw_home));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_manager:

			break;
		case R.id.button_fragment:
			this.startActivity(new Intent(this, FragmentHomeActivity.class));
			break;
		case R.id.button_slidingpane:
			this.startActivity(new Intent(this, SlidingpaneLayoutActivity.class));
			break;
		case R.id.slidingmenu:
			break;
		}
	}
}
