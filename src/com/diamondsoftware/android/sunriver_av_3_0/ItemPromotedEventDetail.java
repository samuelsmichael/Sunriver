package com.diamondsoftware.android.sunriver_av_3_0;

public class ItemPromotedEventDetail {
	private int promotedEventsDetailsID;
	private String promotedEventsDetailsTitle;
	private String promotedEventsDetailsDescription;
	private String promotedEventsDetailsURLDocDownload;
	private String promotedEventsDetailsAddress;
	private String promotedEventsDetailsTelephone;
	private String promotedEventsDetailsWebsite;
	/**
	 * @return the promotedEventDetailOrder
	 */
	public int getPromotedEventDetailOrder() {
		return promotedEventDetailOrder;
	}

	/**
	 * @param promotedEventDetailOrder the promotedEventDetailOrder to set
	 */
	public void setPromotedEventDetailOrder(int promotedEventDetailOrder) {
		this.promotedEventDetailOrder = promotedEventDetailOrder;
	}

	/**
	 * @return the promotedEventsDetailIconURL
	 */
	public String getPromotedEventsDetailIconURL() {
		return promotedEventsDetailIconURL;
	}

	/**
	 * @param promotedEventsDetailIconURL the promotedEventsDetailIconURL to set
	 */
	public void setPromotedEventsDetailIconURL(String promotedEventsDetailIconURL) {
		this.promotedEventsDetailIconURL = promotedEventsDetailIconURL;
	}

	private int promotedEventDetailOrder;
	private String promotedEventsDetailIconURL;
	
	public ItemPromotedEventDetail() {
		
	}
	
	public String toString(
			ItemPromotedEventNormalized ipen,
			ItemPromotedEventCategory ipec,
			ItemPromotedEventDetail iped
			) {
		StringBuilder sb=new StringBuilder();
		sb.append("Name: " + Utils.objectToString(ipen.getPromotedEventsName())+"\n");
		sb.append("Category: " + Utils.objectToString(ipec.getPromotedCatName())+"\n");
		sb.append("Address: " + Utils.objectToString(iped.getPromotedEventsDetailsAddress())+"\n");
		sb.append("Description: " + Utils.objectToString(iped.getPromotedEventsDetailsDescription())+"\n");
		sb.append("Telephone: " + Utils.objectToString(iped.getPromotedEventsDetailsTelephone())+"\n");
		sb.append("Website: " + Utils.objectToString(iped.getPromotedEventsDetailsWebsite())+"\n");
		sb.append("More information: " + Utils.objectToString(iped.getPromotedEventsDetailsURLDocDownload())+"\n");
		return sb.toString();
	}

	/**
	 * @return the promotedEventsDetailsID
	 */
	public int getPromotedEventsDetailsID() {
		return promotedEventsDetailsID;
	}

	/**
	 * @param promotedEventsDetailsID the promotedEventsDetailsID to set
	 */
	public void setPromotedEventsDetailsID(int promotedEventsDetailsID) {
		this.promotedEventsDetailsID = promotedEventsDetailsID;
	}

	/**
	 * @return the promotedEventsDetailsTitle
	 */
	public String getPromotedEventsDetailsTitle() {
		return promotedEventsDetailsTitle;
	}

	/**
	 * @param promotedEventsDetailsTitle the promotedEventsDetailsTitle to set
	 */
	public void setPromotedEventsDetailsTitle(String promotedEventsDetailsTitle) {
		this.promotedEventsDetailsTitle = promotedEventsDetailsTitle;
	}

	/**
	 * @return the promotedEventsDetailsDescription
	 */
	public String getPromotedEventsDetailsDescription() {
		return promotedEventsDetailsDescription;
	}

	/**
	 * @param promotedEventsDetailsDescription the promotedEventsDetailsDescription to set
	 */
	public void setPromotedEventsDetailsDescription(
			String promotedEventsDetailsDescription) {
		this.promotedEventsDetailsDescription = promotedEventsDetailsDescription;
	}

	/**
	 * @return the promotedEventsDetailsURLDocDownload
	 */
	public String getPromotedEventsDetailsURLDocDownload() {
		return promotedEventsDetailsURLDocDownload;
	}

	/**
	 * @param promotedEventsDetailsURLDocDownload the promotedEventsDetailsURLDocDownload to set
	 */
	public void setPromotedEventsDetailsURLDocDownload(
			String promotedEventsDetailsURLDocDownload) {
		this.promotedEventsDetailsURLDocDownload = promotedEventsDetailsURLDocDownload;
	}

	/**
	 * @return the promotedEventsDetailsAddress
	 */
	public String getPromotedEventsDetailsAddress() {
		return promotedEventsDetailsAddress;
	}

	/**
	 * @param promotedEventsDetailsAddress the promotedEventsDetailsAddress to set
	 */
	public void setPromotedEventsDetailsAddress(String promotedEventsDetailsAddress) {
		this.promotedEventsDetailsAddress = promotedEventsDetailsAddress;
	}

	/**
	 * @return the promotedEventsDetailsTelephone
	 */
	public String getPromotedEventsDetailsTelephone() {
		return promotedEventsDetailsTelephone;
	}

	/**
	 * @param promotedEventsDetailsTelephone the promotedEventsDetailsTelephone to set
	 */
	public void setPromotedEventsDetailsTelephone(
			String promotedEventsDetailsTelephone) {
		this.promotedEventsDetailsTelephone = promotedEventsDetailsTelephone;
	}

	/**
	 * @return the promotedEventsDetailsWebsite
	 */
	public String getPromotedEventsDetailsWebsite() {
		return promotedEventsDetailsWebsite;
	}

	/**
	 * @param promotedEventsDetailsWebsite the promotedEventsDetailsWebsite to set
	 */
	public void setPromotedEventsDetailsWebsite(String promotedEventsDetailsWebsite) {
		this.promotedEventsDetailsWebsite = promotedEventsDetailsWebsite;
	}

}
