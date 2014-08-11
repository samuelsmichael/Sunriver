package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParsesJsonEmergencyMaps extends ParsesJson {

	public ParsesJsonEmergencyMaps() {
	}

	@Override
	protected ArrayList<Object> parse(String jsonString) throws Exception {
		ArrayList<Object> items = new ArrayList<Object>();
		JSONArray jsonArray=new JSONArray(jsonString);
		int c=jsonArray.length();
		for(int i=0;i<c;i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			ItemEmergencyMap item=new ItemEmergencyMap();
			item.setEmergencyMapsId(jsonObject.getInt("emergencyMapsId"));
			item.setEmergencyMapsURL(jsonObject.getString("emergencyMapsURL"));
			item.setEmergencyMapsDescription(jsonObject.getString("emergencyMapsDescription"));
			item.setEmergencyMapsPic(jsonObject.getString("emergencyMapsPic"));
			item.setEmergencyMapsIsVisible(jsonObject.getBoolean("emergencyMapsIsVisible"));
			/* This is how I tested, since there hasn't yet been established the means of fetching the map urls
			item.addMapURL("http://tiles.arcgis.com/tiles/PPpMbTaRDuKoF0e4/arcgis/rest/services/BikePaths/MapServer");
			item.addMapURL("http://tiles.arcgis.com/tiles/PPpMbTaRDuKoF0e4/arcgis/rest/services/SRCircleNumbersV3/MapServer");
			*/
			items.add(item);
		}
		return items;
		
	}

}
