package com.diamondsoftware.android.sunriver_av_3_0;


import java.util.HashMap;


/*
 * This class only writes 1 record for each category
 */
public class ParsesXMLServicesPage extends ParsesXMLServices {
	HashMap<Integer,Integer> doneCategories=new HashMap<Integer,Integer>();

	public ParsesXMLServicesPage(String dummy) {
		super(dummy);
	}

	@Override
	protected boolean shallIAddThisItem(ItemService itemService) {
		if(itemService.getServiceCategoryName().trim().isEmpty()) {
			return false;
		}
		boolean containsKey=doneCategories.containsKey(itemService.getServiceCategory());
		if(!containsKey) {
			doneCategories.put(itemService.getServiceCategory(), itemService.getServiceCategory());
		}
		return !containsKey;
	}
	
}
