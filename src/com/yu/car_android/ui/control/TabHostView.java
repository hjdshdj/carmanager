package com.yu.car_android.ui.control;

import java.util.ArrayList;
import java.util.List;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TabHost.TabContentFactory;

/**
 * tabhost切换页面
 * 
 * @author Administrator
 * 
 */
public class TabHostView extends FrameLayout {

	private int mCurrentTab = -1;
	private View mCurrentView = null;
	private LocalActivityManager localActivityManager;
	private List<ContentStrategy> contentStrategys = new ArrayList<ContentStrategy>();

	public TabHostView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TabHostView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TabHostView(Context context) {
		super(context);
	}

	public void setup(LocalActivityManager localActivityManager) {
		this.localActivityManager = localActivityManager;
	}

	// 添加数据进行切换
	public void addContent(String tag, Intent intent) {
		contentStrategys.add(new IntentContentStrategy(tag, intent));
	}

	public void addContent(CharSequence tag, TabContentFactory factory) {
		contentStrategys.add(new FactoryContentStrategy(tag, factory));
	}

	public void addContent(int viewId) {
		contentStrategys.add(new ViewIdContentStrategy(viewId));
	}

	public void setContentTab(int index) {
		if (index == mCurrentTab)
			return;
		if (index < 0 || index >= contentStrategys.size())
			return;
		if (mCurrentTab != -1)
			contentStrategys.get(mCurrentTab).tabClosed();

		// 得到要加载的view
		mCurrentTab = index;
		ContentStrategy contentStrategy = contentStrategys.get(index);
		mCurrentView = contentStrategy.getContentView();
		// 加载到父类中去
		if (mCurrentView.getParent() == null) {
			addView(mCurrentView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
		mCurrentView.requestFocus();// 获取焦点

	}

	// view
	private class IntentContentStrategy implements ContentStrategy {
		private Intent mintent;
		private String mtag;
		private View view;

		private IntentContentStrategy(String tag, Intent intent) {
			this.mintent = intent;
			this.mtag = tag;
		}

		@Override
		public View getContentView() {
			if (localActivityManager == null) {
				throw new IllegalStateException("Did you forget to call 'public void setup(LocalActivityManager activityGroup)'?");
			}
			final Window w = localActivityManager.startActivity(mtag, mintent);
			final View v = w != null ? w.getDecorView() : null;
			if (view != v && view != null) {
				if (view.getParent() != null) {
					TabHostView.this.removeView(view);
				}
			}
			view = v;

			if (view != null) {
				view.setVisibility(View.VISIBLE);
				view.setFocusableInTouchMode(true);
				((ViewGroup) view).setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
			}
			return view;
		}

		@Override
		public void tabClosed() {
			if (view != null) {
				view.setVisibility(View.GONE);
			}
		}

	}

	private class ViewIdContentStrategy implements ContentStrategy {

		private int viewid;
		private View idView;

		private ViewIdContentStrategy(int id) {
			this.viewid = id;
		}

		@Override
		public View getContentView() {
			if (idView == null) {
				idView = findViewById(viewid);
			}
			idView.setVisibility(View.VISIBLE);
			return null;
		}

		@Override
		public void tabClosed() {
			idView.setVisibility(View.GONE);
		}

	}

	private class FactoryContentStrategy implements ContentStrategy {

		private CharSequence mtag;
		private TabContentFactory mfactory;
		private View factoryview;

		private FactoryContentStrategy(CharSequence tag, TabContentFactory factory) {
			this.mtag = tag;
			this.mfactory = factory;
		}

		@Override
		public View getContentView() {
			if (factoryview == null) {
				factoryview = mfactory.createTabContent(mtag.toString());
			}
			factoryview.setVisibility(View.VISIBLE);
			return factoryview;
		}

		@Override
		public void tabClosed() {
			factoryview.setVisibility(View.GONE);
		}

	}

	private static interface ContentStrategy {

		/**
		 * Return the content view. The view should may be cached locally.
		 */
		View getContentView();

		/**
		 * Perhaps do something when the tab associated with this content has
		 * been closed (i.e make it invisible, or remove it).
		 */
		void tabClosed();
	}

	public int getCurrentTab() {
		return mCurrentTab;
	}

	public View getCurrentView() {
		return mCurrentView;
	}

	public void setCurrentView(View currentView) {
		this.mCurrentView = currentView;
	}

	public int getsize(){
		return contentStrategys.size();
	}
}
