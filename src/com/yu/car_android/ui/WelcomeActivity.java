package com.yu.car_android.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.yu.car_android.R;
import com.yu.car_android.bean.User;
import com.yu.car_android.net.AppClient;
import com.yu.car_android.util.ThreadPoolUtils;

/**
 * 欢迎页
 * 
 * @author Administrator
 * 
 */
public class WelcomeActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		User user=new User();
		user.setPhone_number("13391521089");
		user.setPassword("yujian12943");
		Button button=(Button) findViewById(R.id.button);
		button.setOnClickListener(this);
		
//		setRequest(this,User.conformBeanToJson(user));
	}

	/**
	 * 访问网络
	 * 
	 * @param welcomeActivity
	 */
	private void setRequest(final Context context,final String jsString) {
		System.out.println("jsString::"+jsString);
		ThreadPoolUtils.execute(new Runnable() {

			@Override
			public void run() {
				try {
					//访问网络
					AppClient.loginUser(context, "login", jsString);
					
					
				} catch (Exception e) {
					
					
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button:
			this.startActivity(new Intent(this,FrameActivity.class));
			break;

		}
		
	}
}
