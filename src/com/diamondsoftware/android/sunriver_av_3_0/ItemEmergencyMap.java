package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

public class ItemEmergencyMap {
	private int emergencyMapsId;
	private String emergencyMapsURL;
	private String emergencyMapsDescription;
	private String emergencyMapsPic;
	private boolean emergencyMapsIsVisible;
	private ArrayList<String> mEmergencyMapsURLs;
	public ArrayList<String> getEmergencyMapsURLs() {
		return mEmergencyMapsURLs;
	}
	public int getEmergencyMapsId() {
		return emergencyMapsId;
	}
	public void setEmergencyMapsId(int emergencyMapsId) {
		this.emergencyMapsId = emergencyMapsId;
	}
	public String getEmergencyMapsURL() {
		return emergencyMapsURL;
	}
	public void setEmergencyMapsURL(String emergencyMapsURL) {
		this.emergencyMapsURL = emergencyMapsURL;
	}
	public String getEmergencyMapsDescription() {
		return emergencyMapsDescription;
	}
	public void setEmergencyMapsDescription(String emergencyMapsDescription) {
		this.emergencyMapsDescription = emergencyMapsDescription;
	}
	public String getEmergencyMapsPic() {
		return emergencyMapsPic;
	}
	public void setEmergencyMapsPic(String emergencyMapsPic) {
		this.emergencyMapsPic = emergencyMapsPic;
	}
	public boolean isEmergencyMapsIsVisible() {
		return emergencyMapsIsVisible;
	}
	public void setEmergencyMapsIsVisible(boolean emergencyMapsIsVisible) {
		this.emergencyMapsIsVisible = emergencyMapsIsVisible;
	}
}
