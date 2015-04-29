package com.yu.car_android.ui.fragment;

import com.yu.car_android.CarHelp;
import com.yu.car_android.Constants;
import com.yu.car_android.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

public class FragmentHomeActivity extends FragmentActivity implements OnClickListener {

	private HomeFragment homeFragment;
	private ServerFragment serverFragment;
	private SettingFragment settingFragment;
	private AccountFragment accountFragment;
	private TextView homeView;
	private TextView serverView;
	private TextView settingView;
	private TextView accountView;
	private View tabs_line;
	private int startLeft = 0;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.fragment_main);
		homeView = (TextView) findViewById(R.id.tabs_home_tv);
		homeView.setOnClickListener(this);
		serverView = (TextView) findViewById(R.id.tabs_server_tv);
		serverView.setOnClickListener(this);
		settingView = (TextView) findViewById(R.id.tabs_information_tv);
		settingView.setOnClickListener(this);
		accountView = (TextView) findViewById(R.id.tabs_account_tv);
		accountView.setOnClickListener(this);

		tabs_line = findViewById(R.id.tabs_line);
		DisplayMetrics displayMetrics = CarHelp.getDisplayMetrics(this);
		tabs_line.setLayoutParams(new FrameLayout.LayoutParams(displayMetrics.widthPixels / 4, CarHelp.dip2px(this, 2)));

		onTabFragment(Constants.HOME);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tabs_home_tv:
			onTabFragment(Constants.HOME);
			setBackgroupText(homeView, R.color.sharese, R.drawable.tabs_home_p);
			setBackgroupText(serverView, R.color.x7f7f7f, R.drawable.tabs_server_n);
			setBackgroupText(settingView, R.color.x7f7f7f, R.drawable.tabs_info_n);
			setBackgroupText(accountView, R.color.x7f7f7f, R.drawable.tabs_account_n);

			moveFrontBg(tabs_line, startLeft = 0, 0, 0, 0);
			break;
		case R.id.tabs_server_tv:
			onTabFragment(Constants.SERVER);
			setBackgroupText(homeView, R.color.x7f7f7f, R.drawable.tabs_home_n);
			setBackgroupText(serverView, R.color.sharese, R.drawable.tabs_server_p);
			setBackgroupText(settingView, R.color.x7f7f7f, R.drawable.tabs_info_n);
			setBackgroupText(accountView, R.color.x7f7f7f, R.drawable.tabs_account_n);

			moveFrontBg(tabs_line, startLeft, startLeft = tabs_line.getWidth(), 0, 0);
			break;
		case R.id.tabs_information_tv:
			onTabFragment(Constants.SETTING);
			
			setBackgroupText(homeView, R.color.x7f7f7f, R.drawable.tabs_home_n);
			setBackgroupText(serverView, R.color.x7f7f7f, R.drawable.tabs_server_n);
			setBackgroupText(settingView, R.color.sharese, R.drawable.tabs_info_p);
			setBackgroupText(accountView, R.color.x7f7f7f, R.drawable.tabs_account_n);
			
			moveFrontBg(tabs_line, startLeft, startLeft = tabs_line.getWidth()*2, 0, 0);
			break;
		case R.id.tabs_account_tv:
			onTabFragment(Constants.ACCOUNT);
			
			setBackgroupText(homeView, R.color.sharese, R.drawable.tabs_home_n);
			setBackgroupText(serverView, R.color.x7f7f7f, R.drawable.tabs_server_n);
			setBackgroupText(settingView, R.color.x7f7f7f, R.drawable.tabs_info_n);
			setBackgroupText(accountView, R.color.sharese, R.drawable.tabs_account_p);
			moveFrontBg(tabs_line, startLeft, startLeft = tabs_line.getWidth()*3, 0, 0);
			break;

		}
	}

	private void onTabFragment(String tag) {
		// FragmentManager获取fragment事务
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		if (Constants.HOME.equals(tag)) {
			if (homeFragment == null) {
					homeFragment = new HomeFragment();

				ft.add(R.id.fragment_main, homeFragment, tag);
			} else {
				if (!homeFragment.isAdded()) {
					ft.add(R.id.fragment_main, homeFragment, tag);
				}
			}
			hideFragment(ft, serverFragment);
			hideFragment(ft, settingFragment);
			hideFragment(ft, accountFragment);
			ft.show(homeFragment);
		} else if (Constants.SERVER.equals(tag)) {
				if (serverFragment == null) {
					serverFragment = new ServerFragment();

					ft.add(R.id.fragment_main, serverFragment, tag);
				} else {
					if (!serverFragment.isAdded()) {
						ft.add(R.id.fragment_main, serverFragment, tag);
					}
				}
			hideFragment(ft, homeFragment);
			hideFragment(ft, settingFragment);
			hideFragment(ft, accountFragment);
			ft.show(serverFragment);

		} else if (Constants.SETTING.equals(tag)) {
				if (settingFragment == null) {
					settingFragment = new SettingFragment();

					ft.add(R.id.fragment_main, settingFragment, tag);
				} else {
					if (!settingFragment.isAdded()) {
						ft.add(R.id.fragment_main, settingFragment, tag);
					}
				}
			hideFragment(ft, serverFragment);
			hideFragment(ft, homeFragment);
			hideFragment(ft, accountFragment);
			ft.show(settingFragment);

		} else if (Constants.ACCOUNT.equals(tag)) {
				if (accountFragment == null) {
					accountFragment = new AccountFragment();

					ft.add(R.id.fragment_main, accountFragment, tag);
				} else {
					if (!accountFragment.isAdded()) {
						ft.add(R.id.fragment_main, accountFragment, tag);
					}
				}
			hideFragment(ft, serverFragment);
			hideFragment(ft, settingFragment);
			hideFragment(ft, homeFragment);
			ft.show(accountFragment);

		}
		ft.commit();
	}

	private void hideFragment(FragmentTransaction ft, Fragment fragment) {
		if (fragment != null) {
			ft.hide(fragment);
		}
	}


	/**
	 * 对按下效果变化控制。
	 * 
	 * @param view
	 *            --textview
	 * @param textcolor
	 *            color
	 * @param backgroupId
	 *            topDrawable
	 */
	private void setBackgroupText(TextView view, int textcolor, int backgroupId) {
		view.setTextColor(this.getResources().getColor(textcolor));
		view.setCompoundDrawablesWithIntrinsicBounds(null, this.getResources().getDrawable(backgroupId), null, null);
	}

	/**
	 * 移动方法
	 * 
	 * @param v
	 *            需要移动的View
	 * @param startX
	 *            起始x坐标
	 * @param toX
	 *            终止x坐标
	 * @param startY
	 *            起始y坐标
	 * @param toY
	 *            终止y坐标
	 */
	private void moveFrontBg(View v, int startX, int toX, int startY, int toY) {
		TranslateAnimation anim = new TranslateAnimation(startX, toX, startY, toY);
		anim.setDuration(200);
		anim.setFillAfter(true);
		v.startAnimation(anim);
	}

}
