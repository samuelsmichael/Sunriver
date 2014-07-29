package com.diamondsoftware.android.sunriver_av_3_0;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebCam1 extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webcam1);
		WebView myWebView1 = (WebView) findViewById(R.id.webview1);
		myWebView1.loadUrl(getResources().getString(R.string.webcam_url_1));
		myWebView1.setWebViewClient(new WebViewClient());
	}
}
