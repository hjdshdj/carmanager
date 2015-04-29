package com.yu.car_android.ui.control;

import android.database.Observable;
import android.widget.RelativeLayout;

/**
 * 中间容器
 * 
 * @author Administrator
 * 
 */
public class MiddleManager extends Observable {
	private MiddleManager middlerManager = new MiddleManager();
	
	private RelativeLayout middlelayout;

	private  MiddleManager() {

	}

	public MiddleManager getInstance(){
		if(middlerManager==null)
			middlerManager= new MiddleManager();
		return middlerManager;
	}
	
	
}
