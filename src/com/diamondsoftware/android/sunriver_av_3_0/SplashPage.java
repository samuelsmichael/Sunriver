package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import com.esri.android.runtime.ArcGISRuntime;

/*
 * Brings up a splash image and manages the procuring of necessary initialization data,
 * such as:
 * 		-- Alert information (needed by MainActivity to build Alert button.
 * 		-- Database dates (date when a given database has last been updated.)
 * The page remains around at least 1 second, or until the last background fetch is completed.
 * The public static variables theItemAlert, and gotInternet are used elsewhere.
 */
public class SplashPage extends Activity implements DataLoaderClient {
	private Handler mHandler;
	private int mCountItemsLeft=0;
	private SplashPageProgressViewManager mSplashPageProgressViewManager;
	private CountDownTimer mCountDownTimer;
	private DataLoader mDataLoader; 
	public static SplashPage mSingleton;

	@Override
	protected void onDestroy() {
		((GlobalState)getApplicationContext()).dbAdapterClose();
		super.onDestroy();
	}
	
	
	private synchronized int getMCountItemsLeft() {
		return mCountItemsLeft;
	}
	public synchronized void incrementMCountItemsLeft(String name) {
		mCountItemsLeft++;
	}
	public synchronized void decrementMCountItemsLeft() {
		mCountItemsLeft--;
	}
	private Runnable myRunnable = new Runnable() {
	    @Override
	    public void run() {
	    	decrementMCountItemsLeft();
	    	anAsynchronousActionCompleted("timer");
	    }
	};
	public void amGettingRemoteData(String name) {
		getSplashPageProgressViewManager().addItem(name);

	}
	public void gotMyDataFromDataLoader(String name, ArrayList<Object> data) {
		getSplashPageProgressViewManager().indicateDone(
				name, data!=null && data.size()>0?""+data.size()+ " rows read":(
						name.toLowerCase().equals("update")?"Stale connection. You can try closing the app and starting it again, or press the \"press to proceed\" button that will appear momentarily.":
						"no data"));
	}
	boolean wereFinishing=false;
	public synchronized void anAsynchronousActionCompleted(String xname) {
		if(getMCountItemsLeft()<=0 && !wereFinishing) {
			wereFinishing=true;
			mCountDownTimer=
			new CountDownTimer(500,400) {
				@Override
				public void onTick(long millisUntilFinished) {
				}
				@Override
				public void onFinish() {
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							mSplashPageProgressViewManager.killTimer();
							doFinish();
						}
						
					});
				}

			};
			mCountDownTimer.start();
		}
	}
	public void doFinish() {
		if(mCountDownTimer!=null) {
			mCountDownTimer.cancel();
		}
		startService(new Intent(SplashPage.this,TimerService.class));
        startActivity(new Intent(SplashPage.this, MainActivity.class));
        finish();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSingleton=this;
		this.setContentView(R.layout.activity_splashpage);
        int secondsDelayed = 1;
        mHandler=new Handler();
        // we'll wait at least 1 second; but we won't return until all asynchronous data fetches have completed
        incrementMCountItemsLeft("timer"); // for timer
        
        
        mHandler.postDelayed(myRunnable, secondsDelayed * 1000);      
        incrementMCountItemsLeft("alert"); // for alert
        incrementMCountItemsLeft("emergency"); // for Emergency
        incrementMCountItemsLeft("newsFeeds"); // for newsFeeds
        incrementMCountItemsLeft("HomePage tips"); // for HomePage tips
        incrementMCountItemsLeft("update");
        
        mDataLoader=new DataLoader(this,(GlobalState)getApplicationContext());
        mDataLoader.execute();

        ArcGISRuntime.setClientId("p7sflEMVP6Pb9okf");
	}

	private SplashPageProgressViewManager getSplashPageProgressViewManager() {
		if(mSplashPageProgressViewManager==null) {
			mSplashPageProgressViewManager=new SplashPageProgressViewManager(this,mHandler);
		}
		return mSplashPageProgressViewManager;
	}

}