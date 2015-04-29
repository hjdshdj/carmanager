package com.yu.car_android.ui.server;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.yu.car_android.R;
import com.yu.car_android.ui.BaseActivity;
import com.yu.car_android.ui.FrameActivity;
import com.yu.car_android.view.Dialog_share;

/**
 * 服务
 * 
 * @author Administrator
 * 
 */
public class ServerActivity extends BaseActivity implements OnClickListener {

	private UMSocialService mController = null;
	public static final String DESCRIPTOR = "com.umeng.share";
	 // 要分享的文字内容
    private String mShareContent = "";
    // 要分享的图片
    private UMImage mUMImgBitmap = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server);
		findViewById(R.id.server_share).setOnClickListener(this);
		findViewById(R.id.server_share_weix).setOnClickListener(this);
	}

	private void init() {

	}

	@Override
	protected void onResume() {
		super.onResume();
		FrameActivity fa = (FrameActivity) getParent();
		Button t_center = (Button) fa.findViewById(R.id.t_center);
		t_center.setText(getResources().getString(R.string.tw_server));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.server_share:
			Dialog_share share= new Dialog_share(this);
			share.show();
			break;
		case R.id.server_share_weix:
			
			break;

		}
	}
}
