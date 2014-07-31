package com.diamondsoftware.android.sunriver_av_3_0;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebCam4 extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webcam4);
		WebView myWebView1 = (WebView) findViewById(R.id.webview4);

		 myWebView1.getSettings().setJavaScriptEnabled(true);

		 final Activity activity = this;
		 myWebView1.setWebChromeClient(new WebChromeClient() {
		   public void onProgressChanged(WebView view, int progress) {
		     // Activities and WebViews measure progress with different scales.
		     // The progress meter will automatically disappear when we reach 100%
		     activity.setProgress(progress * 1000);
		   }
		 });
		
		myWebView1.getSettings().setBuiltInZoomControls(true);
		myWebView1.getSettings().setLoadWithOverviewMode(true);
		myWebView1.getSettings().setUseWideViewPort(true);


		myWebView1.loadUrl(getResources().getString(R.string.webcam_url_4));
		myWebView1.setWebViewClient(new WebViewClient());
	}
}
