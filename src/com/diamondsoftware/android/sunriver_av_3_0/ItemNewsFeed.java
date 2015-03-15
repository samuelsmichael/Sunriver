package com.diamondsoftware.android.sunriver_av_3_0;

public class ItemNewsFeed {
    private int newsFeedID;
    private String newsFeedTitle;
    private String newsFeedDescription;
    private boolean isOnNewsFeedAlert;
	public boolean getIsOnNewsFeedAlert() {
		return isOnNewsFeedAlert;
	}
	public void setIsOnNewsFeedAlert(boolean isOnNewsFeedAlert) {
		this.isOnNewsFeedAlert = isOnNewsFeedAlert;
	}
	public ItemNewsFeed( int id, String title, String description) {
		newsFeedID=id;
		newsFeedTitle=title;
		newsFeedDescription=description;
	}
	public ItemNewsFeed() {
	}
	public void setnewsFeedID(int newsFeedID) {
		this.newsFeedID = newsFeedID;
	}
	public void setnewsFeedTitle(String newsFeedTitle) {
		this.newsFeedTitle = newsFeedTitle;
	}
	public void setnewsFeedDescription(String newsFeedDescription) {
		this.newsFeedDescription = newsFeedDescription;
	}
	public int getnewsFeedID() {
		return newsFeedID;
	}
	public String getnewsFeedTitle() {
		return newsFeedTitle;
	}
	public String getnewsFeedDescription() {
		return newsFeedDescription;
	}
}
