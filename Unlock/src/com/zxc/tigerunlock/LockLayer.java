package com.zxc.tigerunlock;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

public class LockLayer {
	private Context mContext;
	private WindowManager mWindowManager;
	private View mLockView;
	private WindowManager.LayoutParams wmParams;
	private static LockLayer mLockLayer;
	private boolean isLocked;
	private Button btn_one, btn_two, btn_three, btn_four, btn_five, btn_six,
					btn_seven, btn_eight, btn_neigh, btn_zero,btn_again,btn_delete;
	private PasswordTextView et_pwd1, et_pwd2, et_pwd3, et_pwd4;
	private String input;
	private int type=1;
	//private boolean isTel=false;
	/** 初始设置密码 */
	public static final int SETTING_PASSWORD = 0;
	/** 确认密码 */
	public static final int SURE_SETTING_PASSWORD = 2;
	/** 验证登录密码 */
	public static final int LOGIN_PASSWORD = 1;
	/** SharedPreferences的文件名 */
	public static final String PREF_NAME = "numberlock";
	private static Handler mainHandler = null; // 与主Activity通信的Handler对象
	
	public static synchronized LockLayer getInstance(Context mContext) {
		if (mLockLayer == null) {
			mLockLayer = new LockLayer(mContext);
			
		}
		return mLockLayer;
	}

	public LockLayer(Context mContext) {
		this.mContext = mContext;
		init();
	}

	private void init() {
		isLocked = false;
		// 获取WindowManager
		mWindowManager = (WindowManager) mContext.getApplicationContext().getSystemService("window");
		// 设置LayoutParams(全局变量）相关参数
		wmParams = new WindowManager.LayoutParams();
		wmParams.type = LayoutParams.TYPE_SYSTEM_ERROR;//关键部分
		wmParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
		// 设置Window flag
//		wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		wmParams.gravity=Gravity.LEFT|Gravity.BOTTOM;
		wmParams.width = WindowManager.LayoutParams.FILL_PARENT;
		
		DisplayMetrics metric = new DisplayMetrics();
		mWindowManager.getDefaultDisplay().getMetrics(metric);
        //Global.width =;     // 屏幕宽度（像素）
        int height = metric.heightPixels; 
       

		wmParams.height=height-getStatusHeight((Activity)mContext);
		wmParams.flags = 1280;
		wmParams.windowAnimations=R.style.AnimExit;

		TelephonyManager telephonyManager = (TelephonyManager)mContext.getSystemService(mContext.TELEPHONY_SERVICE);
		telephonyManager.listen(new PhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	public static int getStatusHeight(Activity activity){  
		int statusHeight = 0;  
		Rect localRect = new Rect();  
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);  
		statusHeight = localRect.top;  
		if (0 == statusHeight){  
			Class<?> localClass;  
			try {  
				localClass = Class.forName("com.android.internal.R$dimen");  
				Object localObject = localClass.newInstance();  
				int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());  
				statusHeight = activity.getResources().getDimensionPixelSize(i5);  
			} catch (Exception e) {  
				e.printStackTrace();  
			}  
		}  
		return statusHeight;  
	}  
	
	

	public synchronized void lock() {
		if (mLockView != null && !isLocked) {
			mWindowManager.addView(mLockView, wmParams);
			btn_one = (Button)mLockView.findViewById(R.id.btn_one);//1
			btn_two = (Button) mLockView.findViewById(R.id.btn_two);//2
			btn_three = (Button)mLockView.findViewById(R.id.btn_three);//3
			btn_four = (Button)mLockView.findViewById(R.id.btn_four);//4
			btn_five = (Button)mLockView.findViewById(R.id.btn_five);//5
			btn_six = (Button)mLockView.findViewById(R.id.btn_six);//6
			btn_seven = (Button)mLockView.findViewById(R.id.btn_seven);//7
			btn_eight = (Button)mLockView.findViewById(R.id.btn_eight);//8
			btn_neigh = (Button)mLockView.findViewById(R.id.btn_neigh);//9
			btn_zero = (Button)mLockView.findViewById(R.id.btn_zero);//0
			btn_again=(Button)mLockView.findViewById(R.id.btn_again);
			btn_delete=(Button)mLockView.findViewById(R.id.btn_delete);
			et_pwd1=(PasswordTextView)mLockView.findViewById(R.id.et_pwd1);
			et_pwd2 = (PasswordTextView)mLockView.findViewById(R.id.et_pwd2);
			et_pwd3 = (PasswordTextView) mLockView.findViewById(R.id.et_pwd3);
			et_pwd4 = (PasswordTextView) mLockView.findViewById(R.id.et_pwd4);
			
			btn_one.setOnClickListener(new ButtonListener());
			btn_two.setOnClickListener(new ButtonListener());
			btn_three.setOnClickListener(new ButtonListener());
			btn_four.setOnClickListener(new ButtonListener());
			btn_five.setOnClickListener(new ButtonListener());
			btn_six.setOnClickListener(new ButtonListener());
			btn_seven.setOnClickListener(new ButtonListener());
			btn_eight.setOnClickListener(new ButtonListener());
			btn_neigh.setOnClickListener(new ButtonListener());
			btn_zero.setOnClickListener(new ButtonListener());
			btn_again.setOnClickListener(new ButtonListener());
			btn_delete.setOnClickListener(new ButtonListener());
			
			et_pwd4.setOnTextChangedListener(new PasswordTextView.OnTextChangedListener() {
				@Override
				public void textChanged(String content) {
					input = et_pwd1.getTextContent() + et_pwd2.getTextContent()+
							et_pwd3.getTextContent() + et_pwd4.getTextContent();
					if(type == LOGIN_PASSWORD){//登录
						if(input!=""){
							mainHandler.obtainMessage(MainActivity.MSG_LOCK_SUCESS).sendToTarget();
						}
					}
				}
			});
		}
		isLocked = true;
	}

	public synchronized void unlock() {
		if (mWindowManager != null && isLocked) {
			mWindowManager.removeView(mLockView);
			//Global.isTel=2;
			
		}
		isLocked = false;
	}

	public synchronized void update() {
		if (mLockView != null && !isLocked) {
			mWindowManager.updateViewLayout(mLockView, wmParams);
			//Global.isTel=3;
		}
		//isLocked=true;
	}

	public synchronized void setLockView(View v) {
		mLockView = v;
	}
	
	private void setText(String text) {
		// 从左往右依次显示
		if (TextUtils.isEmpty(et_pwd1.getTextContent())) {
			et_pwd1.setTextContent(text);
		} else if (TextUtils.isEmpty(et_pwd2.getTextContent())) {
			et_pwd2.setTextContent(text);
		} else if (TextUtils.isEmpty(et_pwd3.getTextContent())) {
			et_pwd3.setTextContent(text);
		} else if (TextUtils.isEmpty(et_pwd4.getTextContent())) {
			et_pwd4.setTextContent(text);
		}
	}

	/**
	 * 清除输入的内容--重输
	 */
	private void clearText() {
		et_pwd1.setTextContent("");
		et_pwd2.setTextContent("");
		et_pwd3.setTextContent("");
		et_pwd4.setTextContent("");
	}

	/**
	 * 删除刚刚输入的内容
	 */
	private void deleteText() {
		// 从右往左依次删除
		if (!TextUtils.isEmpty(et_pwd4.getTextContent())) {
			et_pwd4.setTextContent("");
		} else if (!TextUtils.isEmpty(et_pwd3.getTextContent())) {
			et_pwd3.setTextContent("");
		} else if (!TextUtils.isEmpty(et_pwd2.getTextContent())) {
			et_pwd2.setTextContent("");
		} else if (!TextUtils.isEmpty(et_pwd1.getTextContent())) {
			et_pwd1.setTextContent("");
		}
	}
	
	private class PhoneListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			
			switch(state){
			case TelephonyManager.CALL_STATE_RINGING:	//来电状态
				unlock();
				Global.isTel=1;
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:	//接听状态
				Global.isTel=2;
				break;
			case TelephonyManager.CALL_STATE_IDLE:		//挂断后
				//Global.isTel=3;
				if(Global.isTel==1 || Global.isTel==2){	
					mainHandler.obtainMessage(MainActivity.MSG_LOCK_SUCESS).sendToTarget();
					Global.isTel=0;
				}
				
				break;
			default:
				break;
			}
		}
		
	}
	
	public class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_one://1
				setText("1");
				break;
				case R.id.btn_two://2
					setText("2");	
					break;
				case R.id.btn_three://3
					setText("3");
					break;
				case R.id.btn_four://4
					setText("4");
					break;
				case R.id.btn_five://5
					setText("5");
					break;
				case R.id.btn_six://6
					setText("6");
					break;
				case R.id.btn_seven://7
					setText("7");
					break;
				case R.id.btn_eight://8
					setText("8");
					break;
				case R.id.btn_neigh://9
					setText("9");
					break;
				case R.id.btn_zero://0
					setText("0");
					break;
				case R.id.btn_again:
					clearText();
					break;
				case R.id.btn_delete:
					deleteText();
				default:
					break;
			}
		}
		
	}
	public static void setMainHandler(Handler handler) {
		mainHandler = handler;// activity所在的Handler对象
	}
}
