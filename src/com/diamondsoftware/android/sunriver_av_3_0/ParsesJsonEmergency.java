package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParsesJsonEmergency extends ParsesJson {

	public ParsesJsonEmergency() {
	}

	@Override
	protected ArrayList<Object> parse(String jsonString) throws Exception {
		ArrayList<Object> items = new ArrayList<Object>();
		JSONArray jsonArray=new JSONArray(jsonString);
		int c=jsonArray.length();
		for(int i=0;i<c;i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			ItemEmergency item=new ItemEmergency();
			item.setEmergencyId(jsonObject.getInt("emergencyId"));
			item.setEmergencyTitle(jsonObject.getString("emergencyTitle"));
			item.setEmergencyDescription(jsonObject.getString("emergencyDescription"));
			item.setEmergencyAlert(jsonObject.getBoolean("isEmergencyAlert"));
			item.setHasMap(jsonObject.getBoolean("hasMap"));
			
			item.addMapURL("http://tiles.arcgis.com/tiles/PPpMbTaRDuKoF0e4/arcgis/rest/services/BikePaths/MapServer");
			item.addMapURL("http://tiles.arcgis.com/tiles/PPpMbTaRDuKoF0e4/arcgis/rest/services/SRCircleNumbersV3/MapServer");
			
			items.add(item);
		}
		return items;
		
	}

}
