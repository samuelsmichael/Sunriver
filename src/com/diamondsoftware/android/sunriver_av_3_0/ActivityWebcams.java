package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TabHost;

public class ActivityWebcams extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webcam);
		
		
		final TabHost tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("tab1")
				.setIndicator(getResources().getString(R.string.webcam_title_1))
				.setContent(new Intent(this, WebCam1.class)));

		tabHost.addTab(tabHost.newTabSpec("tab2")
				.setIndicator(getResources().getString(R.string.webcam_title_2))
				.setContent(new Intent(this, WebCam2.class)));

		tabHost.addTab(tabHost.newTabSpec("tab3")
				.setIndicator(getResources().getString(R.string.webcam_title_3))
				.setContent(new Intent(this, WebCam3.class)));
	}

}
