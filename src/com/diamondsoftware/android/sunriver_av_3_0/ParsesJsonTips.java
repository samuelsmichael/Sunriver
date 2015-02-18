package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParsesJsonTips extends ParsesJson {

	public ParsesJsonTips() {
	}

	@Override
	protected ArrayList<Object> parse(String jsonString) throws Exception {
		ArrayList<Object> items = new ArrayList<Object>();
		JSONArray jsonArray=new JSONArray(jsonString);
		int c=jsonArray.length();
		for(int i=0;i<c;i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			ItemTip item=new ItemTip();
			item.setTipsURL(jsonObject.getString("tipsURL"));
			item.setTipsId(jsonObject.getInt("tipsID"));
			item.setTipsAndroidOrder(jsonObject.getInt("tipsAndroidOrder"));
			item.setTipsAppleOrder(jsonObject.getInt("tipsAppleOrder"));
			items.add(item);
		}
		return items;
		
	}

}
