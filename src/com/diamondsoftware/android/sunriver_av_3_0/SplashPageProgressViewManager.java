package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.Hashtable;

import android.app.Activity;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SplashPageProgressViewManager {
	private class TextViewAndProgressBar {
		TextView mTextView;
		ProgressBar mProgressBar;
		TextViewAndProgressBar(TextView textView, ProgressBar progressBar) {
			mTextView=textView;
			mProgressBar=progressBar;
		}
	}
	Handler mHandler;
	SplashPage mSpashPage;
	TableRow mCurrentTableRow;
	LayoutInflater mLayoutInflater;
	TableLayout mTableLayout;
	Button mBtnTakingTooLong;
	CountDownTimer mCountdownTimer;
	Hashtable<String,TextViewAndProgressBar> mHashTableViews = new Hashtable<String,TextViewAndProgressBar>();
	public SplashPageProgressViewManager(SplashPage spashPage,Handler handler) {
		mHandler=handler;
		mSpashPage=spashPage;
		mLayoutInflater = LayoutInflater.from(spashPage);
		mCurrentTableRow=(TableRow)spashPage.findViewById(R.id.trSplashPageProgress);
		mTableLayout=(TableLayout)spashPage.findViewById(R.id.tlSplashPageProgress);
		mBtnTakingTooLong= (Button)spashPage.findViewById(R.id.btnSplashPageContinueCrippled);
		mBtnTakingTooLong.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SplashPageProgressViewManager.this.mSpashPage.doFinish();
				
			}
		});
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				SplashPageProgressViewManager.this.mCountdownTimer=new CountDownTimer(15000, 1000) {
					@Override
					public void onTick(long millisUntilFinished) {
					}
					@Override
					public void onFinish() {
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								mBtnTakingTooLong.setVisibility(View.VISIBLE);
								mCountdownTimer=null;
							}
							
						});
					}
				};
				mCountdownTimer.start();
			}
			
		});
	}
	class MyRunnable implements Runnable {
		String mName;
		String mAddedDescription;
		MyRunnable(String name, String addedDescription) {
			mName=name;
			mAddedDescription=addedDescription;
		}
		@Override
		public void run() {
			TextView tv=(TextView)mHashTableViews.get(mName).mTextView;
			ProgressBar pb=(ProgressBar)mHashTableViews.get(mName).mProgressBar;
			String desc=mName + " done. " + (mAddedDescription!=null && mAddedDescription.length()>0?mAddedDescription:"");
			tv.setText(desc);
			pb.setVisibility(View.GONE);
		}
	}
	public void indicateDone(String name, final String addedDescription) {
		mHandler.post(new MyRunnable(name,addedDescription));
	}
	
	class MyRunnable2 implements Runnable {
		String mName;
		MyRunnable2(String name) {
			mName=name;
		}
		@Override
		public void run() {
			int cnt=mCurrentTableRow.getChildCount();
			if(cnt==1) {
				TableRow tr = new TableRow(mSpashPage);
				LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				tr.setLayoutParams(lp);
				mTableLayout.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				mCurrentTableRow=tr;
			}
			LinearLayout ll=(LinearLayout)mLayoutInflater.inflate(R.layout.progress_circle_with_text, mCurrentTableRow);
			TextView tv=(TextView)ll.findViewById(R.id.tvProgressCircle);
			tv.setText("Fetching " + mName + " data...");
			
			ProgressBar pb=(ProgressBar)ll.findViewById(R.id.pbProgressCircle);		
			mHashTableViews.put(mName, new TextViewAndProgressBar(tv,pb));
			mTableLayout.setStretchAllColumns(true);
			mTableLayout.invalidate();
		}
	}
	
	public void killTimer() {
		if(mCountdownTimer!=null) {
			mCountdownTimer.cancel();
			mCountdownTimer=null;
		}
	}
	public void addItem (final String name) {
		mHandler.post(new MyRunnable2(name));
	}
}
