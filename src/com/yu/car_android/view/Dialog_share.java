package com.yu.car_android.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;

import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;
import com.yu.car_android.R;

/**
 * 分享的dialog
 * 
 * @author Administrator
 * 
 */
public class Dialog_share extends Dialog implements android.view.View.OnClickListener {

	private Activity context;
	private View view;
	private int height;
	private UMSocialService mController = null;
	private String mShareContent = "";
	private final SHARE_MEDIA mTestMedia = SHARE_MEDIA.SINA;
	// 要分享的图片
	private UMImage mUMImgBitmap = null;
	public static final String DESCRIPTOR = "com.umeng.share";

	public Dialog_share(Activity activity) {
		super(activity, R.style.SimpleDialog);
		this.context = activity;
		init(activity);
	}

	// 初始化布局数据
	private void init(Activity activity) {
		Window window = this.getWindow();
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.width = WindowManager.LayoutParams.MATCH_PARENT;
		wl.height = WindowManager.LayoutParams.MATCH_PARENT;
		window.setGravity(Gravity.BOTTOM);

		view = activity.getLayoutInflater().inflate(R.layout.share_dialog, null);
		Display display = this.getWindow().getWindowManager().getDefaultDisplay();

		height = display.getHeight();
		this.addContentView(view, new LayoutParams(display.getWidth(), WindowManager.LayoutParams.WRAP_CONTENT));

		this.setOnDismissListener(onDismissListener);

		view.findViewById(R.id.twiter).setOnClickListener(this);
		view.findViewById(R.id.wechat).setOnClickListener(this);
		view.findViewById(R.id.qq).setOnClickListener(this);
		view.findViewById(R.id.qqspace).setOnClickListener(this);
		view.findViewById(R.id.everyone).setOnClickListener(this);
		view.findViewById(R.id.friends).setOnClickListener(this);
		view.findViewById(R.id.sms).setOnClickListener(this);

		init();
	}

	private void init() {

		mController = UMServiceFactory.getUMSocialService(DESCRIPTOR, RequestType.SOCIAL);

		// 要分享的文字内容
//		mShareContent = context.getResources().getString(R.string.umeng_socialize_share_content);
		mController.setShareContent("友盟社会化组件还不错，让移动应用快速整合社交分享功能。www.umeng.com/social");

		mUMImgBitmap = new UMImage(context, "http://www.umeng.com/images/pic/banner_module_social.png");
		// mUMImgBitmap = new UMImage(mContext, new
		// File("/mnt/sdcard/DCIM/Camera/1357290284463.jpg"));
		// 设置图片
		// 其他方式构造UMImage
		// UMImage umImage_url = new UMImage(mContext,
		// "http://historyhots.com/uploadfile/2013/0110/20130110064307373.jpg");
		//
		// mUMImgBitmap = new UMImage(mContext, new File(
		// "mnt/sdcard/test.png"));

		UMusic uMusic = new UMusic("http://sns.whalecloud.com/test_music.mp3");
		uMusic.setAuthor("zhangliyong");
		uMusic.setTitle("天籁之音");

		UMVideo umVedio = new UMVideo("http://v.youku.com/v_show/id_XNTE5ODAwMDM2.html?f=19001023");
		umVedio.setThumb("http://historyhots.com/uploadfile/2013/0110/20130110064307373.jpg");
		umVedio.setTitle("哇喔喔喔！");

		// 添加新浪和QQ空间的SSO授权支持
//		mController.getConfig().setSsoHandler(new SinaSsoHandler());
//		// 添加腾讯微博SSO支持
//		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
	}

	@Override
	public void show() {
		super.show();

		TranslateAnimation animation = new TranslateAnimation(0, 0, height, 0);
		animation.setDuration(500);

		// 开始动画
		view.startAnimation(animation);
	}

	/**
	 * 销毁监听
	 */
	private OnDismissListener onDismissListener = new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface dialog) {
			closeDialog();
		}

	};

	// 关闭dialog
	private void closeDialog() {
		TranslateAnimation animation = new TranslateAnimation(0, 0, 0, height);
		animation.setDuration(500);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				myDismiss();
			}
		});
		view.startAnimation(animation);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sms:// 短信
			openShareBoard();
			break;
		case R.id.qq:// qq
			break;
		case R.id.qqspace:// qq空间
			break;
		case R.id.wechat:// 微信
			break;
		case R.id.friends:// 朋友圈
			break;
		case R.id.twiter:// 新浪
			break;
		case R.id.everyone:// 人人
			break;

		}
	}

	@Override
	public void dismiss() {
		closeDialog();
	}

	private void myDismiss() {
		super.dismiss();
	}
	
	 /**
     * @功能描述 : 分享(先选择平台)
     */
    private void openShareBoard() {
        mController.setShareContent("友盟社会化组件还不错，让移动应用快速整合社交分享功能。http://www.umeng.com/social");
        mController.setShareMedia(mUMImgBitmap);
        mController.openShare(context, false);
    }
}
