package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

public class PopupDisclaimer extends Popups2 {
	public static String DISCLAIMER_MAPS_FILE="dnsam.dat";
	public static String DISCLAIMER_BIKEPATHS_FILE="dnsab.dat";
	private CheckBox mCheckBoxDoNotShowAgain;
	private Button mCancelButton;
	private String mFileName;
	private Intent mIntent;
	public PopupDisclaimer(Activity activity,String fileName, Intent classToLaunch) {
		super(activity);
		mFileName=fileName;
		mIntent=classToLaunch;
	}

	@Override
	protected void childPerformCloseActions() {
		if(mCheckBoxDoNotShowAgain.isChecked()) {
			File dir=new File(android.os.Environment.getExternalStorageDirectory(),"/sunriver");
			File donotshowagain=new File(dir, mFileName);
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(donotshowagain);
				fos.close();
			} catch (Exception e) {
			}

		}
		mActivity.startActivity(mIntent);

	}

	@Override
	protected void loadView(ViewGroup popup) {
		mCheckBoxDoNotShowAgain=(CheckBox)popup.findViewById(R.id.cb_disclaimermap_donotshowagain);
		mCancelButton=(Button)popup.findViewById(R.id.disclaimermap_cancelbutton);
		mCancelButton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				removeView();
			}
		});
	}

	@Override
	protected int getResourceId() {
		return R.layout.disclaimer_map;
	}

	@Override
	protected int getClosePopupId() {
		return R.id.closePopup;
	}

	@Override
	protected boolean getDoesPopupNeedToRunInBackground() {
		return false;
	}

	@Override
	protected String getGoogleAnalyticsAction() {
		return "Disclaimer";
	}

	@Override
	protected String getGoogleAnalyticsLabel() {
		return "";
	}

}
