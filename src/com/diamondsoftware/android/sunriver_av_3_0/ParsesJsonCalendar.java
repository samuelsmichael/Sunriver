package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParsesJsonCalendar extends ParsesJson {

	public ParsesJsonCalendar() {
	}

	@Override
	protected ArrayList<Object> parse(String jsonString) throws Exception {
		ArrayList<Object> items = new ArrayList<Object>();
		JSONArray jsonArray = new JSONArray(jsonString);
		int c=jsonArray.length();
		for(int i=0;i<c;i++) {
			ItemCalendar item=new ItemCalendar();
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			item.setSrCalId(jsonObject.getInt("srCalId"));
			item.setSrCalName(jsonObject.getString("srCalName"));
			item.setSrCalDescription(jsonObject.getString("srCalDescription"));
			item.setSrCalDate(Utils.toDateFromYYYYdashMMdashDDTHHcolonMMcolonSS(jsonObject.getString("srCalDate")));
			item.setSrCalTime(jsonObject.getString("srCalTime"));
			item.setSrCalDuration(jsonObject.getString("srCalDuration"));
			item.setSrCalLinks(jsonObject.getString("srCalLinks"));
			item.setSrCalUrlImage(jsonObject.getString("srCalUrlImage"));
			item.setSrCalLat(jsonObject.getDouble("srCalLat"));
			item.setSrCalLong(jsonObject.getDouble("srCalLong"));
			item.setApproved(jsonObject.getBoolean("isApproved"));
			item.setSrCalAddress(jsonObject.getString("srCalAddress"));			
			items.add(item);
		}
		return items;
	}

}
