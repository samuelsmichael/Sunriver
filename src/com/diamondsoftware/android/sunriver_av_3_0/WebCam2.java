package com.diamondsoftware.android.sunriver_av_3_0;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebCam2 extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webcam2);
		WebView myWebView1 = (WebView) findViewById(R.id.webview2);
		myWebView1.loadUrl(getResources().getString(R.string.webcam_url_2));
		myWebView1.setWebViewClient(new WebViewClient());
	}
}
