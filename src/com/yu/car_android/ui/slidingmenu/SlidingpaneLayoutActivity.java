package com.yu.car_android.ui.slidingmenu;

import com.yu.car_android.R;
import com.yu.car_android.listener.Slidingpane_Fragment_data;
import com.yu.car_android.ui.slidingmenu.MainFragment.onChangeListener;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;

public class SlidingpaneLayoutActivity extends Activity {

	private SlidingPaneLayout slidingpanellayout;
	private ContentFragment content;
	private MainFragment main;
	private DisplayMetrics displayMetrics = new DisplayMetrics();
	public layoutchangeListener layoutListener;
	
	private int maxMargin = 0;
	
	public interface layoutchangeListener{
		public abstract void setlayoulistener(String text);
	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		setContentView(R.layout.slidingpane_activity);
		slidingpanellayout = (SlidingPaneLayout) findViewById(R.id.slidingpanellayout);
		// 这里要保证11版本以上
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		content = new ContentFragment();
		main = new MainFragment();
		ft.replace(R.id.slidingpane_content, content);
		ft.replace(R.id.slidingpane_menu, main);
		ft.commit();
		maxMargin = displayMetrics.heightPixels / 10;

		slidingpanellayout.setPanelSlideListener(new PanelSlideListener() {
			
			@Override
			public void onPanelSlide(View arg0, float slideOffset) {
				int contentMargin = (int) (slideOffset * maxMargin);
				FrameLayout.LayoutParams contentParams = content.getCurrentViewParams();
				contentParams.setMargins(0, contentMargin, 0, contentMargin);
				content.setCurrentViewPararms(contentParams);

				float scale = 1 - ((1 - slideOffset) * maxMargin * 2) / (float) displayMetrics.heightPixels;
				main.getCurrentView().setScaleX(scale);// 设置缩放的基准点
				main.getCurrentView().setScaleY(scale);// 设置缩放的基准点
				main.getCurrentView().setPivotX(0);// 设置缩放和选择的点
				main.getCurrentView().setPivotY(displayMetrics.heightPixels / 2);
				main.getCurrentView().setAlpha(slideOffset);
			}
			
			@Override
			public void onPanelOpened(View arg0) {
				
			}
			
			@Override
			public void onPanelClosed(View arg0) {
				
			}
		});
		setListener();
	}

	private void setListener() {
		main.setChangeListener(new onChangeListener() {
			
			@Override
			public void setchangeListener(String text) {
				System.out.println("text:::"+text);
				layoutListener.setlayoulistener(text);
			}
		});
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
	 * @return the slidingPaneLayout
	 */
	public SlidingPaneLayout getSlidingPaneLayout() {
		return slidingpanellayout;
	}
	public void setlayoutListener(layoutchangeListener listener){
		this.layoutListener=listener;
	}
}
