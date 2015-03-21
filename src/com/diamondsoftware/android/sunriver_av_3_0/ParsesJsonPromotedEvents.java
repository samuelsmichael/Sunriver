package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParsesJsonPromotedEvents extends ParsesJson {

	public ParsesJsonPromotedEvents() {
	}

	@Override
	protected ArrayList<Object> parse(String jsonString) throws Exception {
		ArrayList<Object> items = new ArrayList<Object>();
		JSONArray jsonArray = new JSONArray(jsonString);
		int c=jsonArray.length();
		for(int i=0;i<c;i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			ItemPromotedEvent item=new ItemPromotedEvent();
			item.setPromotedEventsID(jsonObject.getInt("promotedEventsID"));
			item.setPromotedEventsName(jsonObject.getString("promotedEventsName"));
			item.setIsOnPromotedEvents(jsonObject.getBoolean("isOnPromotedEvents"));
			item.setPromotedEventPictureURL(jsonObject.getString("promotedEventPictureURL"));
			item.setPromotedCatID(jsonObject.getInt("promotedCatID"));
			item.setPromotedCatName(jsonObject.getString("promotedCatName"));
			item.setPromotedCatSortOrder(jsonObject.getInt("promotedCatSortOrder"));
			item.setPromotedCatURLForIconImage(jsonObject.getString("promotedCatURLForIconImage"));
			item.setPromotedEventsDetailsID(jsonObject.getInt("promotedEventsDetailsID"));
			item.setPromotedEventsDetailsTitle(jsonObject.getString("promotedEventsDetailsTitle"));
			item.setPromotedEventsDetailsDescription(jsonObject.getString("promotedEventsDetailsDescription"));
			item.setPromotedEventsDetailsURLDocDownload(jsonObject.getString("promotedEventsDetailsURLDocDownload"));
			item.setPromotedEventsDetailsAddress(jsonObject.getString("promotedEventsDetailsAddress"));
			item.setPromotedEventsDetailsTelephone(jsonObject.getString("promotedEventsDetailsTelephone"));
			item.setPromotedEventsDetailsWebsite(jsonObject.getString("promotedEventsDetailsWebsite"));
			item.setPromotedEventDetailOrder(jsonObject.getInt("promotedEventDetailOrder"));
			item.setPromotedEventDetailIconURL(jsonObject.getString("promotedEventsDetailIconURL"));
			item.setPromotedEventIconURL(jsonObject.getString("promotedEventIconURL"));
			if(item.getIsOnPromotedEvents()) {
				items.add(item);
			}
		}
		return items;
	}

}
