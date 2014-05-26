package com.diamondsoftware.android.sunriver_av_3_0;


import java.util.HashMap;


/*
 * This class only writes 1 record for each category
 */
public class ParsesXMLServicesPage extends ParsesXMLServices {
	HashMap<String,String> doneCategories=new HashMap<String,String>();

	public ParsesXMLServicesPage(String dummy) {
		super(dummy);
	}

	@Override
	protected boolean shallIAddThisItem(ItemService itemService) {
		if(itemService.getServiceCategoryName().trim().isEmpty()) {
			return false;
		}
		boolean containsKey=doneCategories.containsKey(itemService.getServiceCategoryName());
		if(!containsKey) {
			doneCategories.put(itemService.getServiceCategoryName(), itemService.getServiceCategoryName());
		}
		return !containsKey;
	}
	
}
