package com.yu.car_android.ui.control;

import java.util.Observer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.yu.car_android.R;

/**
 * 初始化数据
 * 
 * @author Administrator
 * 
 */
public class TitleManager implements Observer{
	private static TitleManager title = new TitleManager();
	private Button t_left;
	private Button t_right;
	private Button t_center;
	private ProgressBar t_right_pro;
	private Activity activity;

	// 类型区分
	private int leftType = -1;
	private int rightType = -1;

	private TitleManager() {
	}

	public static TitleManager getInstance(Activity activity) {
		if (title == null) {
			title = new TitleManager();
		}
		return title;
	}

	/**
	 * 初始化头布局
	 * 
	 * @param activity
	 */
	public void init(Activity activity) {
		this.activity = activity;
		View view = LayoutInflater.from(activity).inflate(R.layout.widget_title, null);
		t_left = (Button) view.findViewById(R.id.t_left);
		t_right = (Button) view.findViewById(R.id.t_right);
		t_center = (Button) view.findViewById(R.id.t_center);
		t_right_pro = (ProgressBar) view.findViewById(R.id.t_right_pro);
		
		setListener();
	}

	private void setListener() {
		t_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		t_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		t_center.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
	}

	/**
	 * 对中心文字进行设置
	 * 
	 * @param text
	 */
	public void setCenterText(String text) {
		t_center.setText(text);
	}

	public void setNomal() {
		t_left.setVisibility(View.VISIBLE);
		t_right.setVisibility(View.VISIBLE);
		t_center.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置图标
	 * 
	 * @param leftDrawable
	 *            左边图标
	 * @param rightDrawable
	 *            右边图标
	 */
	public void setDrawableId(int leftDrawable, int rightDrawable) {
		t_left.setBackgroundDrawable(activity.getResources().getDrawable(leftDrawable));
		t_right.setBackgroundDrawable(activity.getResources().getDrawable(rightDrawable));
	}

	/**
	 * 设置图标
	 * 
	 * @param rightDrawable
	 *            右边图标
	 */
	public void setDrawableId(int rightDrawable) {
		t_right.setBackgroundDrawable(activity.getResources().getDrawable(rightDrawable));

	}

	/**
	 * 展示进度条。
	 */
	public void setShowProgress() {
		t_right_pro.setVisibility(View.VISIBLE);
	}

	/**
	 * 隐藏进度条
	 */
	public void setHideProgress() {
		t_right_pro.setVisibility(View.INVISIBLE);
		t_right.setVisibility(View.VISIBLE);
	}
	/**
	 * 添加View对象
	 */
	public void addView() {
	}

	public void removeView() {
	}
	

	public int getLeftType() {
		return leftType;
	}

	public void setLeftType(int leftType) {
		this.leftType = leftType;
	}

	public int getRightType() {
		return rightType;
	}

	public void setRightType(int rightType) {
		this.rightType = rightType;
	}

	@Override
	public void update(java.util.Observable observable, Object data) {
		
	}
	
	
}
