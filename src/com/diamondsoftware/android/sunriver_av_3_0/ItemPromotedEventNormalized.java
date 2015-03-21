package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

public class ItemPromotedEventNormalized {

	private int promotedEventsID;
	private String promotedEventsName;
	private Boolean isOnPromotedEvents;
	private String promotedEventPictureURL;
	private String promotedEventIconURL;
	public ItemPromotedEventCategory findCategory(ItemPromotedEventCategory ipec) {
		for(ItemPromotedEventCategory ipecInCategoriesList: categories) {
			if(ipecInCategoriesList.getPromotedCatID()==ipec.getPromotedCatID()) {
				return ipecInCategoriesList;
			}
		}
		return null;
	}
	/**
	 * @return the categories
	 */
	public ArrayList<ItemPromotedEventCategory> getCategories() {
		return categories;
	}

	/**
	 * @param categories the categories to set
	 */
	public void setCategories(ArrayList<ItemPromotedEventCategory> categories) {
		this.categories = categories;
	}

	private ArrayList<ItemPromotedEventCategory> categories;
	
	/**
	 * @return the promotedEventsID
	 */
	public int getPromotedEventsID() {
		return promotedEventsID;
	}

	/**
	 * @param promotedEventsID the promotedEventsID to set
	 */
	public void setPromotedEventsID(int promotedEventsID) {
		this.promotedEventsID = promotedEventsID;
	}

	/**
	 * @return the promotedEventsName
	 */
	public String getPromotedEventsName() {
		return promotedEventsName;
	}

	/**
	 * @param promotedEventsName the promotedEventsName to set
	 */
	public void setPromotedEventsName(String promotedEventsName) {
		this.promotedEventsName = promotedEventsName;
	}

	/**
	 * @return the isOnPromotedEvents
	 */
	public Boolean getIsOnPromotedEvents() {
		return isOnPromotedEvents;
	}

	/**
	 * @param isOnPromotedEvents the isOnPromotedEvents to set
	 */
	public void setIsOnPromotedEvents(Boolean isOnPromotedEvents) {
		this.isOnPromotedEvents = isOnPromotedEvents;
	}

	/**
	 * @return the promotedEventPictureURL
	 */
	public String getPromotedEventPictureURL() {
		return promotedEventPictureURL;
	}

	/**
	 * @param promotedEventPictureURL the promotedEventPictureURL to set
	 */
	public void setPromotedEventPictureURL(String promotedEventPictureURL) {
		this.promotedEventPictureURL = promotedEventPictureURL;
	}
	
	/**
	 * @return the promotedEventIconURL
	 */
	public String getPromotedEventIconURL() {
		return promotedEventIconURL;
	}
	/**
	 * @param promotedEventIconURL the promotedEventIconURL to set
	 */
	public void setPromotedEventIconURL(String promotedEventIconURL) {
		this.promotedEventIconURL = promotedEventIconURL;
	}
}
