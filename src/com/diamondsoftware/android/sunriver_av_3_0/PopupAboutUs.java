package com.diamondsoftware.android.sunriver_av_3_0;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PopupAboutUs extends Popups2 {
	private TextView mVersionName;
	private TextView mAboutUsPrivacyPolicy;
	private TextView mAboutUsTermsAndConditions;


	public PopupAboutUs(Activity activity) {
		super(activity);
	}

	@Override
	protected void childPerformCloseActions() {
	}

	@Override
	protected void loadView(ViewGroup popup) {
		mVersionName = (TextView)popup.findViewById(R.id.popup_aboutus_version);
		try {
			mVersionName.setText(mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			mVersionName.setText("1.0");
		}
		mAboutUsPrivacyPolicy=(TextView)popup.findViewById(R.id.aboutus_privacypolicyurl);	
		mAboutUsTermsAndConditions=(TextView)popup.findViewById(R.id.aboutus_termsandconditionsurl);
		mAboutUsPrivacyPolicy.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		mAboutUsTermsAndConditions.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		mAboutUsPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String url=mActivity.getString(R.string.aboutuslink_privacypolicy);
		        Intent intent=new Intent(mActivity,Website.class).
		        		putExtra("url",(url.indexOf("http")==-1?"http://":"")+url);
		        mActivity.startActivity(intent);
		        removeView();
			}
		});
		mAboutUsTermsAndConditions.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String url=mActivity.getString(R.string.aboutuslink_termsandconditions);
		        Intent intent=new Intent(mActivity,Website.class).
		        		putExtra("url",(url.indexOf("http")==-1?"http://":"")+url);
		        mActivity.startActivity(intent);
		        removeView();
			}
		});

	}

	@Override
	protected int getResourceId() {
		return R.layout.popup_aboutus_layout;
	}

	@Override
	protected int getClosePopupId() {
		return R.id.aboutusClosePopup;
	}

	@Override
	protected boolean getDoesPopupNeedToRunInBackground() {
		return false;
	}

	@Override
	protected String getGoogleAnalyticsAction() {
		return "About Us";
	}

	@Override
	protected String getGoogleAnalyticsLabel() {
		return "";
	}

}
