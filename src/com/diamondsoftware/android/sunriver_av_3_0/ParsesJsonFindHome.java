package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParsesJsonFindHome extends ParsesJson {
	String mResortName;
	public ParsesJsonFindHome(String resortName) {
		mResortName=resortName;
	}

	@Override
	protected ArrayList<Object> parse(String jsonString) throws Exception {
		ArrayList<Object> items = new ArrayList<Object>();
		JSONObject jsonObject = new JSONObject(jsonString);
		ItemFindHome item=new ItemFindHome(mResortName);
		item.setmDC_Address(jsonObject.getString("mAddress"));
		items.add(item);
		return items;
	}

}
