package com.yu.car_android.ui.fragment;

import com.yu.car_android.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SettingFragment extends Fragment {

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.info, container, false);
	}
	@Override
	public void onResume() {
		super.onResume();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	@Override
	public void onDetach() {
		super.onDetach();
	}
	
}
