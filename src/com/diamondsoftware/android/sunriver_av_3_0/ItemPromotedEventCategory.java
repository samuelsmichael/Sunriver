package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

public class ItemPromotedEventCategory {
	private int promotedCatID;
	private String promotedCatName;
	private int promotedCatSortOrder;
	private String promotedCatURLForIconImage;
	private ArrayList<ItemPromotedEventDetail> promotedEventDetails;
	
	public void addDetailItem(ItemPromotedEventDetail iped) {
		promotedEventDetails.add(iped);
	}
	
	/**
	 * @return the promotedCatID
	 */
	public int getPromotedCatID() {
		return promotedCatID;
	}

	/**
	 * @param promotedCatID the promotedCatID to set
	 */
	public void setPromotedCatID(int promotedCatID) {
		this.promotedCatID = promotedCatID;
	}

	/**
	 * @return the promotedCatName
	 */
	public String getPromotedCatName() {
		return promotedCatName;
	}

	/**
	 * @param promotedCatName the promotedCatName to set
	 */
	public void setPromotedCatName(String promotedCatName) {
		this.promotedCatName = promotedCatName;
	}

	/**
	 * @return the promotedCatSortOrder
	 */
	public int getPromotedCatSortOrder() {
		return promotedCatSortOrder;
	}

	/**
	 * @param promotedCatSortOrder the promotedCatSortOrder to set
	 */
	public void setPromotedCatSortOrder(int promotedCatSortOrder) {
		this.promotedCatSortOrder = promotedCatSortOrder;
	}

	/**
	 * @return the promotedCatURLForIconImage
	 */
	public String getPromotedCatURLForIconImage() {
		return promotedCatURLForIconImage;
	}

	/**
	 * @param promotedCatURLForIconImage the promotedCatURLForIconImage to set
	 */
	public void setPromotedCatURLForIconImage(String promotedCatURLForIconImage) {
		this.promotedCatURLForIconImage = promotedCatURLForIconImage;
	}

	/**
	 * @return the promotedEventDetails
	 */
	public ArrayList<ItemPromotedEventDetail> getPromotedEventDetails() {
		return promotedEventDetails;
	}

	/**
	 * @param promotedEventDetails the promotedEventDetails to set
	 */
	public void setPromotedEventDetails(
			ArrayList<ItemPromotedEventDetail> promotedEventDetails) {
		this.promotedEventDetails = promotedEventDetails;
	}

	public ItemPromotedEventCategory() {
		
	}

}
