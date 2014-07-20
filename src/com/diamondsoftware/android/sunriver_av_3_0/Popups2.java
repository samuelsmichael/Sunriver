package com.diamondsoftware.android.sunriver_av_3_0;


import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

public abstract class Popups2 {
	private  WindowManager mWindowManager = null;
	private ViewGroup mPopup = null;
	protected Activity mActivity=null;
	protected SharedPreferences mSharedPreferences; 
	private WakeLock mScreenLock = null;
	private ViewGroup mViewGroup=null;
	
	
	protected abstract void childPerformCloseActions();
	protected abstract void loadView(ViewGroup popup);
	protected abstract int getResourceId();
	protected abstract int getClosePopupId();
	protected abstract boolean getDoesPopupNeedToRunInBackground();
	protected abstract String getGoogleAnalyticsAction();
	protected abstract String getGoogleAnalyticsLabel();
	
	public Popups2(Activity activity) {
		mActivity=activity;
	}
	
	/* If the phone's orientation changes, the Activity is destroyed, and so we need
	 * a way to effect close of the popup; otherwise, we get a memory leak.
	 */
	public void publicRemoveView() {
		removeView();
	}
	public void createPopup() {
	    	try {
			mSharedPreferences=mActivity.getSharedPreferences(getPREFS_NAME(), Context.MODE_PRIVATE);
			mWindowManager = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
			LayoutInflater vi = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mPopup = (ViewGroup) vi.inflate(getResourceId(), null);
	        // Get tracker.
	        Tracker t = ((GlobalState) mActivity.getApplication()).getTracker(
	            GlobalState.TrackerName.APP_TRACKER);
	        // Build and send an Event.
	        t.send(new HitBuilders.EventBuilder()
	            .setCategory(getGoogleAnalyticsCategory())
	            .setAction(getGoogleAnalyticsAction())
	            .setLabel(getGoogleAnalyticsLabel())
	            .build());

			loadView(mPopup);
	
			final Button closeMe = (Button) mPopup.findViewById(getClosePopupId());
	
			closeMe.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					childPerformCloseActions();
					if(mActivity instanceof Maps) {
						((Maps)mActivity).refreshGraphicsLayersVisibility();
					}
					removeView();
				}
			});
			
			/*  This makes it close too often
			mPopup.setOnTouchListener(new OnTouchListener() {
	
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					removeView();
					return true;
				}
				
			});
			*/
			if (getDoesPopupNeedToRunInBackground()) {
				/* This makes it happen even if the system is sleeping or locked */
				mScreenLock = ((PowerManager) mActivity.getSystemService(Context.POWER_SERVICE))
						.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
								| PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
				mScreenLock.acquire();
	
				mActivity.getWindow().addFlags(
						WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
				mActivity.getWindow().addFlags(
						WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
				mActivity.getWindow()
						.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
			}
	
			WindowManager wm = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			int height=display.getHeight();

			
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.WRAP_CONTENT, 10, 10,
					WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY
							| WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
					WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
							| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
	// blackens the entire background						| WindowManager.LayoutParams.FLAG_DIM_BEHIND
							,
					PixelFormat.OPAQUE);
			if(height<1300) {
				lp.height=(int)(((float)height)*.9f);
			}
			mWindowManager.addView(mPopup, lp);
	    } catch (Exception e) {
    		
    	}
	}
	protected String getGoogleAnalyticsCategory() {
		return "Item Detail";
	}
	protected synchronized void removeView() {
		try {
			if (mScreenLock != null) {
				mScreenLock.release();
				mScreenLock = null;
			}
		} catch (Exception e3) {
		}
		MainActivity.mSingleton.resetCurrentPoppedUp();
		try {
			mWindowManager.removeView(mPopup);
			mWindowManager=null;
		} catch (Exception ee3) {
		}
	}	

	protected String getPREFS_NAME() {
		return mActivity.getPackageName() + "_preferences";
	}
	
    /* If the popup is open, we need to explicitly close it, else there will be a memory leak. */
	public void onDestroy() {
		if(mPopup.isShown()) {
			removeView();
		}
	}

}
