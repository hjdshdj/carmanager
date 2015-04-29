package com.yu.car_android.ui.control;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * 基础UI
 * 
 * @author Administrator ---主要为页面调用过程和布局
 */
public abstract class BaseUI implements View.OnClickListener {

	private Context context;
	private ViewGroup showMiddle;

	public BaseUI(Context context) {
		this.context = context;
		init();
		setListener();
	}

	/**
	 * 初始化
	 */
	public abstract void init();

	/**
	 * 设置监听
	 */
	public abstract void setListener();

	public View getChild() {
		if (showMiddle.getLayoutParams() == null) {
			RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
			showMiddle.setLayoutParams(layoutparams);
		}
		return showMiddle;
	}

	/**
	 * 进入界面
	 */
	public abstract void onResue();

	/**
	 * 离开界面
	 */
	public abstract void onPauses();
	
	@Override
	public void onClick(View v) {
		
	}
	
	public View getView(int id){
		return showMiddle.findViewById(id);
	}
}
