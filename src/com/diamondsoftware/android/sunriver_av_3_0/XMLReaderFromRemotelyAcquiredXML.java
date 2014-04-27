package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.xmlpull.v1.XmlPullParserException;

public class XMLReaderFromRemotelyAcquiredXML extends XMLReader {
	private String mUrl=null;
	private List<NameValuePair> mParameters=null;
	
	public XMLReaderFromRemotelyAcquiredXML(List<NameValuePair> parameters, ParsesXML parsesXML, String url) {
		this(parsesXML,url);
		mParameters=parameters;
	}

	public XMLReaderFromRemotelyAcquiredXML(ParsesXML parsesXML, String url) {
		super(parsesXML);
		mUrl=url;
	}

	@Override
	public ArrayList<Object> parse() throws XmlPullParserException, IOException {
		ArrayList<Object> data=parse(getInputStream());
		if(data.size()>0) { // write data to database
			if(data.get(0) instanceof Hashtable) {
				for (Object objht: data) {
					Hashtable ht=(Hashtable)objht;
					for (Object objArray : ht.values()) {
						ArrayList<Object> aroo = (ArrayList<Object>)objArray;
						for (Object theElement :aroo) {
							((ItemLocation)theElement).writeItemToDatabase();
						}
					}
				}
			} else {
				if(data.get(0) instanceof Cacheable) {
					((Cacheable)data.get(0)).clearTable();
					for(Object obj: data) {
						((Cacheable)obj).writeItemToDatabase();
					}
				}
			}
		}
		return data;
	}
	
	private InputStream getInputStream() throws IOException{
		InputStream is=null;
		if(mParameters!=null) {
			HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost(mUrl);

		    try {
		        // Add your data
		        httppost.setEntity(new UrlEncodedFormEntity(mParameters,HTTP.UTF_8));

		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity=response.getEntity();
		        is=entity.getContent();
		    } catch (ClientProtocolException e) {
		        return null;
		    }
		} else {
			URL u = new URL(mUrl);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setRequestMethod("POST");
			
			
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("content-type", "text/json; charset=utf-8");
			conn.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
			conn.setRequestProperty("Accept","*/*");
			conn.setUseCaches(false);
			conn.connect();
			OutputStream out = conn.getOutputStream();
			PrintWriter pw = new PrintWriter(out);
			pw.close();
			is=conn.getInputStream();
		}
		InputStreamReader is2 = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(is2);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		is.close();
		String json = sb.toString();
		return new ByteArrayInputStream(json.getBytes("UTF-8"));
	}	
}
