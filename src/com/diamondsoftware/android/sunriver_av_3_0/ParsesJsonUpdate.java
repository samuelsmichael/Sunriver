package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParsesJsonUpdate extends ParsesJson {

	public ParsesJsonUpdate() {
	}

	@Override
	protected ArrayList<Object> parse(String jsonString) throws Exception {
		ArrayList<Object> items = new ArrayList<Object>();
		JSONArray jsonArray = new JSONArray(jsonString);
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		int updateID=jsonObject.getInt("updateID");
		ItemUpdate item=new ItemUpdate();
		item.setUpdateID(jsonObject.getInt("updateID"));
		item.setUpdateCalendar(Utils.toDateFromYYYYdashMMdashDDTHHcolonMMcolonSS(jsonObject.getString("updateCalendar")));
		item.setUpdateActivity(Utils.toDateFromYYYYdashMMdashDDTHHcolonMMcolonSS(jsonObject.getString("updateActivity")));
		item.setUpdateData(Utils.toDateFromYYYYdashMMdashDDTHHcolonMMcolonSS(jsonObject.getString("updateData")));
		item.setUpdateMaps(Utils.toDateFromYYYYdashMMdashDDTHHcolonMMcolonSS(jsonObject.getString("updateMaps")));
		item.setUpdateOverlay(Utils.toDateFromYYYYdashMMdashDDTHHcolonMMcolonSS(jsonObject.getString("updateOverlay")));
		item.setUpdateServices(Utils.toDateFromYYYYdashMMdashDDTHHcolonMMcolonSS(jsonObject.getString("updateServices")));
		item.setUpdateWelcome(Utils.toDateFromYYYYdashMMdashDDTHHcolonMMcolonSS(jsonObject.getString("updateWelcome")));
		items.add(item);
		return items;
	}

}
