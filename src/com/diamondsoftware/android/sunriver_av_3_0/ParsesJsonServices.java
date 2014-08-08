package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParsesJsonServices extends ParsesJson {

	public ParsesJsonServices() {
	}

	@Override
	protected ArrayList<Object> parse(String jsonString) throws Exception {
		ArrayList<Object> items = new ArrayList<Object>();
		JSONArray jsonArray = new JSONArray(jsonString);
		int c=jsonArray.length();
		for(int i=0;i<c;i++) {
			ItemService item=new ItemService();
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			item.setServiceID(jsonObject.getInt(ItemService.KEY_SERVICE_SERVICEID));
			String name=jsonObject.getString(ItemService.KEY_SERVICE_SERVICENAME);
			item.setServiceName(name);
			item.setServiceWebURL(jsonObject.getString("serviceWebURl"));
			item.setServicePictureURL(jsonObject.getString(ItemService.KEY_SERVICE_SERVICEPICTUREURL));
			item.setServiceIconURL(jsonObject.getString(ItemService.KEY_SERVICE_SERVICEICONURL));
			String desc=jsonObject.getString(ItemService.KEY_SERVICE_SERVICEDESCRIPTION);
			item.setServiceDescription(desc);
			item.setServicePhone(jsonObject.getString(ItemService.KEY_SERVICE_SERVICEPHONE));
			item.setServiceAddress(jsonObject.getString(ItemService.KEY_SERVICE_SERVICEADDRESS));
			item.setServiceLat(jsonObject.getDouble(ItemService.KEY_SERVICE_SERVICELAT));
			item.setServiceLong(jsonObject.getDouble(ItemService.KEY_SERVICE_SERVICELONG));
			String categoryName=jsonObject.getString(ItemService.KEY_SERVICE_SERVICECATEGORYNAME);
			item.setServiceCategoryName(categoryName);
			item.setServiceCategoryIconURL(jsonObject.getString(ItemService.KEY_SERVICE_SERVICECATEGORYICONURL));
			item.setSortOrder(ItemService.deriveSortOrder(item.getServiceCategoryName()));
			if(categoryName == null || categoryName.equals("")) {
				int bkhere=3;
				int bkthere=bkhere;
			} else {
				items.add(item);
			}
		}
		return items;
	}

}
