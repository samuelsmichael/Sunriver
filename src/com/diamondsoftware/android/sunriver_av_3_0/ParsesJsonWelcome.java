package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParsesJsonWelcome extends ParsesJson {

	public ParsesJsonWelcome() {
	}

	@Override
	protected ArrayList<Object> parse(String jsonString) throws Exception {
		ArrayList<Object> items = new ArrayList<Object>();
		JSONArray jsonArray = new JSONArray(jsonString);
		int c=jsonArray.length();
		for(int i=0;i<c;i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			ItemWelcome item=new ItemWelcome();
			item.setWelcomeID(jsonObject.getInt("welcomeID"));
			item.setWelcomeURL(jsonObject.getString("welcomeURL"));
			item.setInRotation(jsonObject.getBoolean("isInRotation"));
			items.add(item);
		}
		return items;
	}

}
