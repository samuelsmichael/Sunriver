package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParsesJsonGISLayers extends ParsesJson {

	public ParsesJsonGISLayers() {
	}

	@Override
	protected ArrayList<Object> parse(String jsonString) throws Exception {
		ArrayList<Object> items = new ArrayList<Object>();
		JSONArray jsonArray = new JSONArray(jsonString);
		int c=jsonArray.length();
		for(int i=0;i<c;i++) {
			ItemGISLayers item=new ItemGISLayers();
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			item.setSrGISLayersId(jsonObject.getInt(ItemGISLayers.KEY_GISLAYERS_ID));
			item.setSrGISLayersIsBikePaths(jsonObject.getBoolean(ItemGISLayers.KEY_GISLAYERS_ISBIKEPATHS));
			item.setSrGISLayersURL(jsonObject.getString(ItemGISLayers.KEY_GISLAYERS_URL));
			item.setSrGISLayersUseNum(jsonObject.getInt(ItemGISLayers.KEY_GISLAYERS_USENUM));
			items.add(item);
		}
		return items;
	}

}
