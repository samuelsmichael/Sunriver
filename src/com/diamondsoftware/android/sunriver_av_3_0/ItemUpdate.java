package com.diamondsoftware.android.sunriver_av_3_0;


import java.util.GregorianCalendar;

public class ItemUpdate {

	/*
	 * For now, we'll force reading it every time.
	 */
	public GregorianCalendar getUpdateDidYouKnow() {
		return new GregorianCalendar();
	}
	
	
	private int updateID;
	private GregorianCalendar updateCalendar;
    private GregorianCalendar updateActivity;
    private GregorianCalendar srActDate;
    private GregorianCalendar updateMaps;
    private GregorianCalendar updateServices;
    private GregorianCalendar updateWelcome;
    private GregorianCalendar updateData;
    private GregorianCalendar updateOverlay;
    private GregorianCalendar updateHospitality;
	
	public GregorianCalendar getUpdateHospitality() {
		return updateHospitality;
	}

	public void setUpdateHospitality(GregorianCalendar updateHospitality) {
		this.updateHospitality = updateHospitality;
	}

	public GregorianCalendar getUpdateOverlay() {
		return updateOverlay;
	}

	public void setUpdateOverlay(GregorianCalendar updateOverlay) {
		this.updateOverlay = updateOverlay;
	}

	public ItemUpdate() {
	}

	public GregorianCalendar getUpdateData() {
		return updateData;
	}

	public void setUpdateData(GregorianCalendar updateData) {
		this.updateData = updateData;
	}

	public int getUpdateID() {
		return updateID;
	}

	public void setUpdateID(int updateID) {
		this.updateID = updateID;
	}

	public GregorianCalendar getUpdateCalendar() {
		return updateCalendar;
	}

	public void setUpdateCalendar(GregorianCalendar updateCalendar) {
		this.updateCalendar = updateCalendar;
	}

	public GregorianCalendar getUpdateActivity() {
		return updateActivity;
	}

	public void setUpdateActivity(GregorianCalendar updateActivity) {
		this.updateActivity = updateActivity;
	}

	public GregorianCalendar getSrActDate() {
		return srActDate;
	}

	public void setSrActDate(GregorianCalendar srActDate) {
		this.srActDate = srActDate;
	}

	public GregorianCalendar getUpdateMaps() {
		return updateMaps;
	}

	public void setUpdateMaps(GregorianCalendar updateMaps) {
		this.updateMaps = updateMaps;
	}

	public GregorianCalendar getUpdateServices() {
		return updateServices;
	}

	public void setUpdateServices(GregorianCalendar updateServices) {
		this.updateServices = updateServices;
	}

	public GregorianCalendar getUpdateWelcome() {
		return updateWelcome;
	}

	public void setUpdateWelcome(GregorianCalendar updateWelcome) {
		this.updateWelcome = updateWelcome;
	}
}
