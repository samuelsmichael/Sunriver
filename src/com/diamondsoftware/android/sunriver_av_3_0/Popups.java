package com.diamondsoftware.android.sunriver_av_3_0;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;

public abstract class Popups extends Activity {

	private WakeLock screenLock = null;
	private static WindowManager mWindowManager = null;
	private LinearLayout mPopup = null;
	protected SharedPreferences mSharedPreferences; 


	protected abstract int getResourceId();
	protected abstract boolean getDoesPopupNeedToRunInBackground();
	protected abstract void loadView(LinearLayout popup);
	protected abstract void performCloseActions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mWindowManager == null) {
			mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		}
		mSharedPreferences=getSharedPreferences(getPREFS_NAME(), MODE_PRIVATE);
		if (getDoesPopupNeedToRunInBackground()) {
			/* This makes it happen even if the system is sleeping or locked */
			screenLock = ((PowerManager) getSystemService(POWER_SERVICE))
					.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
							| PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
			screenLock.acquire();

			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
			getWindow()
					.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		}

		LayoutInflater vi = (LayoutInflater) getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPopup = (LinearLayout) vi.inflate(getResourceId(), null);
		loadView(mPopup);

		/*
		 * This is how you popup a dialog even if you're not the window at the
		 * front
		 */
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT, 10, 10,
				WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY
						| WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
				WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
						| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
				PixelFormat.OPAQUE);
/*		
	    WindowManager.LayoutParams lp=new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, 10, 10,
				WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY | WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
//				WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG,
//				WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,0
				WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
					| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,PixelFormat.OPAQUE);
///				    lp.token=notificationPopup.getWindowToken();		
		*/
		
		mWindowManager.addView(mPopup, lp);

		Button closeMe = (Button) mPopup.findViewById(R.id.closePopup);

		closeMe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				performCloseActions();
				removeView();
				close();
			}
		});
	}
	protected String getPREFS_NAME() {
		return getApplicationContext().getPackageName() + "_preferences";
	}	

	private synchronized void removeView() {
		try {
			mWindowManager.removeView(mPopup);
		} catch (Exception ee3) {
		}
	}


	public void close() {
		try {
			if (screenLock != null) {
				screenLock.release();
				screenLock = null;
			}
		} catch (Exception e3) {
		}
	}
}
