package com.diamondsoftware.android.sunriver_av_3_0;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Website extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_website);
		setTitle("Sunriver Web Browser");
		WebView myWebView = (WebView) findViewById(R.id.webview);
		
		myWebView.loadUrl(getIntent().getStringExtra("url"));
		WebSettings webSettings = myWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
				 myWebView.setWebViewClient(new WebViewClient() {
		   public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		     Toast.makeText(getApplicationContext(), "Website failed loading. Error is:" + description, Toast.LENGTH_SHORT).show();
		   }
		 });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.website, menu);
		return true;
	}

}
