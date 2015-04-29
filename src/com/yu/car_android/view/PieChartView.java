package com.yu.car_android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * 饼图的处理
 * 
 * @author Administrator
 * 
 */
public class PieChartView extends View {

	private Context context;
	public PieChartView(Context context) {
		super(context);
		this.context= context;
		init();
	}

	/**
	 * 初始化数据
	 */
	private void init() {
		
	}

	public PieChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PieChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	//测量布局大小
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	//对界面进行布局
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}
	
	//绘制图形
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
}
