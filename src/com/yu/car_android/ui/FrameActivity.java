package com.yu.car_android.ui;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yu.car_android.CarHelp;
import com.yu.car_android.R;
import com.yu.car_android.ui.account.AccountActivity;
import com.yu.car_android.ui.control.TabHostView;
import com.yu.car_android.ui.home.HomeActivity;
import com.yu.car_android.ui.info.InforActicity;
import com.yu.car_android.ui.server.ServerActivity;

/**
 * 框架主页面
 * 
 * @author Administrator
 * 
 */
public class FrameActivity extends BaseActivity implements OnClickListener {

	private TextView homeView, serverView, inforView, accountView;
	private TabHostView tabHostView;
	private View tabs_line;
	private int startLeft = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame);

		homeView = (TextView) findViewById(R.id.tabs_home_tv);
		homeView.setOnClickListener(this);
		serverView = (TextView) findViewById(R.id.tabs_server_tv);
		serverView.setOnClickListener(this);
		inforView = (TextView) findViewById(R.id.tabs_information_tv);
		inforView.setOnClickListener(this);
		accountView = (TextView) findViewById(R.id.tabs_account_tv);
		accountView.setOnClickListener(this);
		// LocalManager 定位服务
		LocalActivityManager localActivityManager = new LocalActivityManager(this, true);
		localActivityManager.dispatchCreate(savedInstanceState);
		
		tabHostView = (TabHostView) findViewById(R.id.car_tabhost);
		tabHostView.setup(localActivityManager);
		tabHostView.addContent("homeView ", new Intent(this, HomeActivity.class));
		tabHostView.addContent("serverView ", new Intent(this, ServerActivity.class));
		tabHostView.addContent("inforView ", new Intent(this, InforActicity.class));
		tabHostView.addContent("accountView ", new Intent(this, AccountActivity.class));
		tabHostView.setContentTab(0);
		
		tabs_line = findViewById(R.id.tabs_line);
		DisplayMetrics displayMetrics= CarHelp.getDisplayMetrics(this);
		tabs_line.setLayoutParams(new FrameLayout.LayoutParams(displayMetrics.widthPixels/4, CarHelp.dip2px(this, 2)));
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tabs_home_tv:
			if (tabHostView.getCurrentTab() == 0)
				return;
			tabHostView.setContentTab(0);
			// 对按钮进行处理
			setBackgroupText(homeView, R.color.sharese, R.drawable.tabs_home_p);
			setBackgroupText(serverView, R.color.x7f7f7f, R.drawable.tabs_server_n);
			setBackgroupText(inforView, R.color.x7f7f7f, R.drawable.tabs_info_n);
			setBackgroupText(accountView, R.color.x7f7f7f, R.drawable.tabs_account_n);
			
			moveFrontBg(tabs_line, startLeft = 0, 0, 0, 0);
			break;
		case R.id.tabs_server_tv:
			if (tabHostView.getCurrentTab() == 1)
				return;
			tabHostView.setContentTab(1);
			setBackgroupText(homeView, R.color.x7f7f7f, R.drawable.tabs_home_n);
			setBackgroupText(serverView, R.color.sharese, R.drawable.tabs_server_p);
			setBackgroupText(inforView, R.color.x7f7f7f, R.drawable.tabs_info_n);
			setBackgroupText(accountView, R.color.x7f7f7f, R.drawable.tabs_account_n);

			moveFrontBg(tabs_line, startLeft, startLeft = tabs_line.getWidth(), 0, 0);
			break;
		case R.id.tabs_information_tv:
			if (tabHostView.getCurrentTab() == 2)
				return;
			tabHostView.setContentTab(2);
			setBackgroupText(homeView, R.color.x7f7f7f, R.drawable.tabs_home_n);
			setBackgroupText(serverView, R.color.x7f7f7f, R.drawable.tabs_server_n);
			setBackgroupText(inforView, R.color.sharese, R.drawable.tabs_info_p);
			setBackgroupText(accountView, R.color.x7f7f7f, R.drawable.tabs_account_n);
			
			moveFrontBg(tabs_line, startLeft, startLeft = tabs_line.getWidth()*2, 0, 0);
			break;
		case R.id.tabs_account_tv:
			if (tabHostView.getCurrentTab() == 3)
				return;
			tabHostView.setContentTab(3);
			setBackgroupText(homeView, R.color.sharese, R.drawable.tabs_home_n);
			setBackgroupText(serverView, R.color.x7f7f7f, R.drawable.tabs_server_n);
			setBackgroupText(inforView, R.color.x7f7f7f, R.drawable.tabs_info_n);
			setBackgroupText(accountView, R.color.sharese, R.drawable.tabs_account_p);
			moveFrontBg(tabs_line, startLeft, startLeft = tabs_line.getWidth()*3, 0, 0);
			break;
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
