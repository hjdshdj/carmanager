package com.yu.car_android.ui.slidingmenu;

import com.yu.car_android.R;
import com.yu.car_android.listener.Slidingpane_Fragment_data;
import com.yu.car_android.ui.slidingmenu.SlidingpaneLayoutActivity.layoutchangeListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

@SuppressLint("NewApi")
public class ContentFragment extends Fragment {

	private Slidingpane_Fragment_data slidingpane_data;
	private TextView textview;
	private Activity activity;
	private View view;
	
	public void setCurrentViewPararms(FrameLayout.LayoutParams layoutParams) {
		view.setLayoutParams(layoutParams);
	}

	public FrameLayout.LayoutParams getCurrentViewParams() {
		return (LayoutParams) view.getLayoutParams();
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity=activity;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.slidingpane_content, null, false);
		textview = (TextView) view.findViewById(R.id.text);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		((SlidingpaneLayoutActivity) getActivity()).setlayoutListener(new layoutchangeListener() {
			
			@Override
			public void setlayoulistener(String text) {
				textview.setText(text);
			}
		});
		
	}
}
