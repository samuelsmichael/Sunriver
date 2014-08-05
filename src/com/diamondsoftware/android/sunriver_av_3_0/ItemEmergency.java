package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

public class ItemEmergency {
	private int emergencyId;
	private String emergencyTitle;
	private String emergencyDescription;
	private boolean isEmergencyAlert;
	private boolean hasMap;
	private ArrayList<String> mMapURLs;
	public void addMapURL(String url) {
		if(mMapURLs==null) {
			mMapURLs=new ArrayList<String>();
		}
		mMapURLs.add(url);
	}
	public ArrayList<String> getMapURLs() {
		return mMapURLs;
	}
	public int getEmergencyId() {
		return emergencyId;
	}
	public void setEmergencyId(int emergencyId) {
		this.emergencyId = emergencyId;
	}
	public String getEmergencyTitle() {
		return emergencyTitle;
	}
	public void setEmergencyTitle(String emergencyTitle) {
		this.emergencyTitle = emergencyTitle;
	}
	public String getEmergencyDescription() {
		return emergencyDescription;
	}
	public void setEmergencyDescription(String emergencyDescription) {
		this.emergencyDescription = emergencyDescription;
	}
	public boolean isEmergencyAlert() {
		return isEmergencyAlert;
	}
	public void setEmergencyAlert(boolean isEmergencyAlert) {
		this.isEmergencyAlert = isEmergencyAlert;
	}
	public boolean isHasMap() {
		return hasMap;
	}
	public void setHasMap(boolean hasMap) {
		this.hasMap = hasMap;
	}
}
