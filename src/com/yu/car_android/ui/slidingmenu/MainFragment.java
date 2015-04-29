package com.yu.car_android.ui.slidingmenu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.yu.car_android.R;

@SuppressLint("NewApi")
public class MainFragment extends Fragment implements OnClickListener {
	private Activity activitys;
	private onChangeListener changeListener;
	private View view;
	
	public View getCurrentView() {
		return view;
	}
	public interface onChangeListener{
		public abstract void setchangeListener(String text);
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activitys= activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.slidingpane_main, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		view.findViewById(R.id.bt_1).setOnClickListener(this);
		view.findViewById(R.id.bt_2).setOnClickListener(this);
		view.findViewById(R.id.bt_3).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		((SlidingpaneLayoutActivity) getActivity()).getSlidingPaneLayout().closePane();
		switch (v.getId()) {
		case R.id.bt_1:
			changeListener.setchangeListener("11");
			break;
		case R.id.bt_2:
			changeListener.setchangeListener("22");
			break;
		case R.id.bt_3:
			changeListener.setchangeListener("33");
			break;

		}
	}
	public void setChangeListener(onChangeListener listener){
		this.changeListener=listener;
	}
}
