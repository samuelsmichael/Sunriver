package com.diamondsoftware.android.sunriver_av_3_0;



/*
 * This class only considers items of a specific CategoryName
 */

public class ParsesXMLServicesDetailsPage extends ParsesXMLServices {

	String mCategoryName;
	
	public ParsesXMLServicesDetailsPage(String categoryName) {
		mCategoryName=categoryName;
	}

	@Override
	protected boolean shallIAddThisItem(ItemService itemService) {
		return itemService.getServiceCategoryName().equalsIgnoreCase(mCategoryName);
	}

}
