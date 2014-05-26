package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParsesJsonActivities extends ParsesJson {

	public ParsesJsonActivities() {
	}

	@Override
	protected ArrayList<Object> parse(String jsonString) throws Exception {
		ArrayList<Object> items = new ArrayList<Object>();
		JSONArray jsonArray = new JSONArray(jsonString);
		int c=jsonArray.length();
		for(int i=0;i<c;i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			ItemActivity item=new ItemActivity();
			item.setApproved(jsonObject.getBoolean("isApproved"));
			item.setSrActAddress(jsonObject.getString("srActAddress"));
			item.setSrActDate(Utils.toDateFromYYYYdashMMdashDDTHHcolonMMcolonSS(jsonObject.getString("srActDate")));
			item.setSrActDescription(jsonObject.getString("srActDescription"));
			item.setSrActDuration(jsonObject.getString("srActDuration"));
			item.setSrActID(jsonObject.getInt("srActID"));
			item.setSrActLat(jsonObject.getDouble("srActLat"));
			item.setSrActLinks(jsonObject.getString("srActLinks"));
			item.setSrActLong(jsonObject.getDouble("srActLong"));
			item.setSrActName(jsonObject.getString("srActName"));
			item.setSrActTime(jsonObject.getString("srActTime"));
			item.setSrActUrlImage(jsonObject.getString("srActUrlImage"));
			items.add(item);
		}
		return items;
	}

}
