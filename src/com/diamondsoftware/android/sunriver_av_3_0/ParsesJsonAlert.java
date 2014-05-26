package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import org.json.JSONObject;

public class ParsesJsonAlert extends ParsesJson {

	public ParsesJsonAlert() {
	}

	@Override
	protected ArrayList<Object> parse(String jsonString) throws Exception {
		ArrayList<Object> items = new ArrayList<Object>();
		JSONObject jsonObject = new JSONObject(jsonString);
		ItemAlert item=new ItemAlert();
		item.setmALDescription(jsonObject.getString("ALDescription"));
		item.setmALID(jsonObject.getInt("ALID"));
		item.setmALTitle(jsonObject.getString("ALTitle"));
		items.add(item);
		return items;
		
	}

}
