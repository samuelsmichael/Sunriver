package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONObject;

import com.esri.core.geometry.Point;

public class ParsesJsonMapLocations extends ParsesJson {

	public ParsesJsonMapLocations() {
	}

	@Override
	protected ArrayList<Object> parse(String jsonString) throws Exception {
		ArrayList<Object> items = new ArrayList<Object>();
		Hashtable<ItemLocation.LocationType,ArrayList<Object>> restaurants= new Hashtable<ItemLocation.LocationType,ArrayList<Object>>();
		Hashtable<ItemLocation.LocationType,ArrayList<Object>> retails= new Hashtable<ItemLocation.LocationType,ArrayList<Object>>();
		Hashtable<ItemLocation.LocationType,ArrayList<Object>> pools= new Hashtable<ItemLocation.LocationType,ArrayList<Object>>();
		Hashtable<ItemLocation.LocationType,ArrayList<Object>> tennisCourts= new Hashtable<ItemLocation.LocationType,ArrayList<Object>>();
		Hashtable<ItemLocation.LocationType,ArrayList<Object>> gasStations= new Hashtable<ItemLocation.LocationType,ArrayList<Object>>();
		Hashtable<ItemLocation.LocationType,ArrayList<Object>> perfectPictureSpots= new Hashtable<ItemLocation.LocationType,ArrayList<Object>>();
		restaurants.put(ItemLocation.LocationType.RESTAURANT, new ArrayList<Object>());
		retails.put(ItemLocation.LocationType.RETAIL, new ArrayList<Object>());
		pools.put(ItemLocation.LocationType.POOL, new ArrayList<Object>());
		tennisCourts.put(ItemLocation.LocationType.TENNIS_COURT, new ArrayList<Object>());
		gasStations.put(ItemLocation.LocationType.GAS_STATION, new ArrayList<Object>());
		perfectPictureSpots.put(ItemLocation.LocationType.PERFECT_PICTURE_SPOT, new ArrayList<Object>());
		items.add(restaurants);
		items.add(retails);
		items.add(pools);
		items.add(tennisCourts);
		items.add(gasStations);
		items.add(perfectPictureSpots);

		JSONArray jsonArray = new JSONArray(jsonString);
		int c=jsonArray.length();
		for(int i=0;i<c;i++) {
			ItemLocation item=new ItemLocation();
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			item.setmCategory(jsonObject.getInt("srMapCategory"));
			item.setmName(jsonObject.getString("srMapName"));
			item.setmDescription(jsonObject.getString("srMapDescription"));
			item.setmCategoryName(jsonObject.getString("srMapCategoryName"));
			item.setmAddress(jsonObject.getString("srMapAddress"));
			item.setmImageUrl(jsonObject.getString("srMapUrlImage"));
			item.setmWebUrl(jsonObject.getString("srMapLinks"));
			item.setmPhone(jsonObject.getString("srMapPhone"));
			item.setmIsGPSPopup(jsonObject.getBoolean("isGPSpopup"));
			item.setmId(jsonObject.getInt("srMapId"));
			if(item.getmGoogleCoordinates()==null) {
				item.setmGoogleCoordinates(new Point());
			}
			item.getmGoogleCoordinates().setY(jsonObject.getDouble("srMapLat"));
			item.getmGoogleCoordinates().setX(jsonObject.getDouble("srMapLong"));
			item.setmCoordinates(Utils.ToWebMercator(item.getmGoogleCoordinates()));
			switch(item.getmCategory()) {
			case 1:
				restaurants.get(ItemLocation.LocationType.RESTAURANT).add(item);
				break;
			case 2:
				tennisCourts.get(ItemLocation.LocationType.TENNIS_COURT).add(item);
				break;
			case 3:
				retails.get(ItemLocation.LocationType.RETAIL).add(item);
				break;
			case 4:
				pools.get(ItemLocation.LocationType.POOL).add(item);
				break;
			case 5:
				gasStations.get(ItemLocation.LocationType.GAS_STATION).add(item);
				break;
			case 6:
				perfectPictureSpots.get(ItemLocation.LocationType.PERFECT_PICTURE_SPOT).add(item);
				break;
			default:
				restaurants.get(ItemLocation.LocationType.RESTAURANT).add(item);
				break;
			}
		}
		return items;
	}

}
