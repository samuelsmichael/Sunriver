package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebCam3 extends Activity  implements DataGetter, WaitingForDataAcquiredAsynchronously {
	private WebView mWebView3;
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webcam3);
		mWebView3 = (WebView) findViewById(R.id.webview3);
		
		new AcquireDataRemotelyAsynchronously(null,this,this);
	    pd=ProgressDialog.show(this,"Finding webcam ...","",true,false,null);
	}
	@Override
	public ArrayList<Object> getRemoteData(String name) {
	    HttpGet pageGet = new HttpGet(getResources().getString(R.string.webcam_url_3));
	    ResponseHandler<String> handler = new ResponseHandler<String>() {
	        public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
	            HttpEntity entity = response.getEntity();
	            String html; 

	            if (entity != null) {
	                html = EntityUtils.toString(entity);
	                return html;
	            } else {
	                return null;
	            }
	        }
	    };

	    String pageHTML = null;
	    HttpClient client=new DefaultHttpClient();
	    try {
	        while (pageHTML==null){
	            pageHTML = client.execute(pageGet, handler);
	        }
	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    if(pageHTML!=null) {
	    	ArrayList<Object> theHTML=new ArrayList<Object>();
	    	theHTML.add(pageHTML);
	    	return theHTML;
	    } else {
	    	return null;
	    }
	}

	@Override
	public void gotMyData(String name, ArrayList<Object> data) {
		pd.dismiss();

		if(data!=null) {
			/*
			 * Parse web page to find image url
			 */
			String html=(String)data.get(0);
			int index=html.indexOf("/goform/capture");
			if(index!=-1) {
				int index2=html.indexOf("></td>",index);
				if(index2!=-1) {
					mWebView3.getSettings().setJavaScriptEnabled(true);
					 final Activity activity = this;
					 mWebView3.setWebChromeClient(new WebChromeClient() {
					   public void onProgressChanged(WebView view, int progress) {
					     // Activities and WebViews measure progress with different scales.
					     // The progress meter will automatically disappear when we reach 100%
					     activity.setProgress(progress * 1000);
					   }
					 });
					mWebView3.getSettings().setBuiltInZoomControls(true);
					mWebView3.getSettings().setLoadWithOverviewMode(true);
					mWebView3.getSettings().setUseWideViewPort(true);

					String url="http://67.204.166.155:8081" +
							html.substring(index, index2-1);
					mWebView3.loadUrl(url);
					mWebView3.setWebViewClient(new WebViewClient());
				}
			}			
		}
	}

}
