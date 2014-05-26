package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParsesJsonDidYouKnow extends ParsesJson {

	public ParsesJsonDidYouKnow() {
	}

	@Override
	protected ArrayList<Object> parse(String jsonString) throws Exception {
		ArrayList<Object> items = new ArrayList<Object>();
		JSONArray jsonArray = new JSONArray(jsonString);
		int c=jsonArray.length();
		for(int i=0;i<c;i++) {
			ItemDidYouKnow item=new ItemDidYouKnow();
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			item.setDidYouKnowId(jsonObject.getInt("didYouKnowId"));
			item.setDidYouKnowURL(jsonObject.getString("didYouKnowURL"));
			items.add(item);
		}
		return items;
	}
}
