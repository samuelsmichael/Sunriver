package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonReader {
	public String getRemoteData(String url) throws IOException {
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestProperty("content-type", "text/json; charset=utf-8");
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
		conn.setRequestProperty("Accept", "*/*");
		conn.setUseCaches(false);
		conn.connect();
		OutputStream out = conn.getOutputStream();
		PrintWriter pw = new PrintWriter(out);
		pw.close();
		InputStream is = conn.getInputStream();
		InputStreamReader is2 = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(is2);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		is.close();
		return sb.toString();
	}

}
