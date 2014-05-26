package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParsesJsonHospitality extends ParsesJson {

	public ParsesJsonHospitality() {
	}

	@Override
	protected ArrayList<Object> parse(String jsonString) throws Exception {
		ArrayList<Object> items = new ArrayList<Object>();
		JSONArray jsonArray = new JSONArray(jsonString);
		int c=jsonArray.length();
		for(int i=0;i<c;i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			ItemHospitality item=new ItemHospitality();
			item.setSrHospitalityIsApproved(jsonObject.getBoolean("srHospitalityIsApproved"));
			item.setSrHospitalityAddress(jsonObject.getString("srHospitalityAddress"));
			item.setSrHospitalityDescription(jsonObject.getString("srHospitalityDescription"));
			item.setSrHospitalityPhone(jsonObject.getString("srHospitalityPhone"));
			item.setSrHospitalityID(jsonObject.getInt("srHospitalityID"));
			item.setSrHospitalityLat(jsonObject.getDouble("srHospitalityLat"));
			item.setSrHospitalityUrlWebsite(jsonObject.getString("srHospitalityUrlWebsite"));
			item.setSrHospitalityLong(jsonObject.getDouble("srHospitalityLong"));
			item.setSrHospitalityName(jsonObject.getString("srHospitalityName"));
			item.setSrHospitalityUrlImage(jsonObject.getString("srHospitalityUrlImage"));
			if(item.getSrHospitalityIsApproved()) {
				items.add(item);
			}
		}
		return items;
	}

}
