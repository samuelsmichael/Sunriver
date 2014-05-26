package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParsesJsonSelfie extends ParsesJson {

	public ParsesJsonSelfie() {
	}

	@Override
	protected ArrayList<Object> parse(String jsonString) throws Exception {
		ArrayList<Object> items = new ArrayList<Object>();
		JSONArray jsonArray = new JSONArray(jsonString);
		int c=jsonArray.length();
		for(int i=0;i<c;i++) {
			ItemSelfie item=new ItemSelfie();
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			item.setOverlayId(jsonObject.getInt("overlayId"));
			item.setOverlayLsURL(jsonObject.getString("overlayLsURL"));
			item.setOverlayLsSelectURL(jsonObject.getString("overlayLsSelectURL"));
			item.setOverlayPortURL(jsonObject.getString("overlayPortURL"));
			item.setOverlayPortCamURL(jsonObject.getString("overlayPortCamURL"));
			items.add(item);
		}
		return items;
	}

}
