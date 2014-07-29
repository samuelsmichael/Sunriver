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
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebCam3 extends Activity  implements DataGetter, WaitingForDataAcquiredAsynchronously {
	private WebView mWebView3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webcam3);
		mWebView3 = (WebView) findViewById(R.id.webview3);
		
		new AcquireDataRemotelyAsynchronously(null,this,this);
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
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
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
		if(data!=null) {
			/*
			 * Parse web page to find image url
			 */
			String html=(String)data.get(0);
			int index=html.indexOf("/goform/capture");
			if(index!=-1) {
				int index2=html.indexOf("></td>",index);
				if(index2!=-1) {
					String url="http://67.204.166.155:8081" +
							html.substring(index, index2-1);
					mWebView3.loadUrl(url);
					mWebView3.setWebViewClient(new WebViewClient());
				}
			}			
		}
	}

}
