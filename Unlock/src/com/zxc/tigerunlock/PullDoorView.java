package com.zxc.tigerunlock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * zaker�Զ���Ч��ҳ��
 * 
 * @author Administrator
 * 
 */
public class PullDoorView extends RelativeLayout {

	private Context mContext;

	private Scroller mScroller;

	private int mScreenWidth = 0;

	private int mScreenHeigh = 0;

	private int mLastDownY = 0;

	private int mCurryY;

	private int mDelY;
	private SoundPool soundPool;// ����һ��SoundPool,SoundPool���ֻ������1M���ڴ�ռ�
	private int musicId;// ����һ��������load������������suondID
	private boolean mCloseFlag = false;
	private static Handler mainHandler = null; // ����Activityͨ�ŵ�Handler����
	private ImageView mImgView;

	public PullDoorView(Context context) {
		super(context);
		mContext = context;
		setupView();
	}

	public PullDoorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setupView();
	}

	@SuppressLint("ResourceAsColor") private void setupView() {

		// ���Interpolator��������ñ�� ������ѡ������е���Ч����Interpolator
		Interpolator polator = new BounceInterpolator();
		mScroller = new Scroller(mContext, polator);

		// ��ȡ��Ļ�ֱ���
		WindowManager wm = (WindowManager) (mContext.getSystemService(Context.WINDOW_SERVICE));
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		mScreenHeigh = dm.heightPixels;
		mScreenWidth = dm.widthPixels;

		// ������һ��Ҫ���ó�͸������,��Ȼ��Ӱ���㿴���ײ㲼��
		// this.setBackgroundColor(Color.argb(0, 0, 0, 0));
		this.setBackgroundColor(R.color.transparency);
		mImgView = new ImageView(mContext);
		mImgView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mImgView.setScaleType(ImageView.ScaleType.FIT_XY);// ���������Ļ
		mImgView.setImageResource(R.drawable.ic_app_background); // Ĭ�ϱ���
		
		addView(mImgView);

	}

	public static void setMainHandler(Handler handler) {
		mainHandler = handler;// activity���ڵ�Handler����
	}
}
