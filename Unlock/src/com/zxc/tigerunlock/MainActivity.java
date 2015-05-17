package com.zxc.tigerunlock;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

 public class MainActivity extends Activity {

	private static String TAG = "QINZDLOCK";
	private TextView tvTime, tvDate;
	private ImageView ivHint;
	private AnimationDrawable animArrowDrawable = null;
	public static int MSG_LOCK_SUCESS = 0x123;
	public static int UPDATE_TIME = 0x234;
	private boolean isTime = true;
	private LockLayer lockLayer;
	private View lock;
	private TextView et_pwd1, et_pwd2, et_pwd3, et_pwd4;
	private Button btn_one, btn_two, btn_three, btn_four, btn_five, btn_six,
    				btn_seven, btn_eight, btn_neigh, btn_zero;
	
	/**
	 * 消息处理
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (MSG_LOCK_SUCESS == msg.what) {
				lockLayer.unlock();
				finish(); // 锁屏成功时，结束我们的Activity界面
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out2);  
			}
			if (UPDATE_TIME == msg.what) {
				Date date = (Date) msg.obj;
			}
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("test", "onCreate()");
		//requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		lock = View.inflate(this, R.layout.main, null);
		lockLayer = new LockLayer(this);
		lockLayer.setLockView(lock);
		lockLayer.lock();
		lockLayer.setMainHandler(mHandler);
//		DisplayMetrics metric = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metric);
//        //Global.width =;     // 屏幕宽度（像素）
//        Global.height = metric.heightPixels; 
		
//		 Rect frame = new Rect();
//	     getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//	     int statusBarHeight = frame.top;
		
		//Global.statusBarHeight =getStatusHeight(this);
		startService(new Intent(MainActivity.this, ZdLockService.class));
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");
		isTime = true;
		// getNewTime();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
		isTime = false;
	}

	protected void onDestory() {
		super.onDestroy();
		Log.i(TAG, "onDestory");
		isTime = false;
	}

}