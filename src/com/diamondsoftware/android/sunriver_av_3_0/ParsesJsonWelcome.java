package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import org.json.JSONObject;

public class ParsesJsonWelcome extends ParsesJson {

	public ParsesJsonWelcome() {
	}

	@Override
	protected ArrayList<Object> parse(String jsonString) throws Exception {
		ArrayList<Object> items = new ArrayList<Object>();
		JSONObject jsonObject = new JSONObject(jsonString);
		ItemWelcome item=new ItemWelcome();
		item.setWelcomeID(jsonObject.getInt("welcomeID"));
		item.setWelcomeURL(jsonObject.getString("welcomeURL"));
		items.add(item);
		return items;
	}

}
