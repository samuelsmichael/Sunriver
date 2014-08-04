package com.diamondsoftware.android.sunriver_av_3_0;

public class ItemEmergency {
	private int emergencyId;
	private String emergencyTitle;
	private String emergencyDescription;
	private boolean isEmergencyAlert;
	private boolean hasMap;
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
