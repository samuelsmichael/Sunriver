package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public abstract class ParsesXMLServices extends ParsesXML  {

	protected abstract boolean shallIAddThisItem(ItemService itemService);
	
	public ParsesXMLServices() {
	}
	
	@Override
	protected ArrayList<Object> parse(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		
		ArrayList<Object> items = null;
        int eventType = parser.getEventType();
        ItemService currentItem = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                	items = new ArrayList<Object>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("Services")) {
                        currentItem = new ItemService();
                    } else if (currentItem != null){
                        if (name.equalsIgnoreCase(ItemService.KEY_SERVICE_SERVICEID)){
                        	String nextText=parser.nextText();
                            currentItem.setServiceID(Integer.parseInt(nextText));
                        } else if (name.equalsIgnoreCase(ItemService.KEY_SERVICE_SERVICENAME)){
                        	currentItem.setServiceName(parser.nextText());
                        } else if (name.equalsIgnoreCase(ItemService.KEY_SERVICE_SERVICEWEBURL)){
                            currentItem.setServiceWebURL(parser.nextText());
                        } else if (name.equalsIgnoreCase(ItemService.KEY_SERVICE_SERVICEPICTUREURL)) {
                        	currentItem.setServicePictureURL(parser.nextText());
                        } else if (name.equalsIgnoreCase(ItemService.KEY_SERVICE_SERVICEICONURL)){
                            currentItem.setServiceIconURL(parser.nextText());
                        } else if (name.equalsIgnoreCase(ItemService.KEY_SERVICE_SERVICEDESCRIPTION)){
                            currentItem.setServiceDescription(parser.nextText());
                        } else if (name.equalsIgnoreCase(ItemService.KEY_SERVICE_SERVICEPHONE)){
                            currentItem.setServicePhone(parser.nextText());
                        } else if (name.equalsIgnoreCase(ItemService.KEY_SERVICE_SERVICEADDRESS)){
                            currentItem.setServiceAddress(parser.nextText());                        
                        } else if (name.equalsIgnoreCase(ItemService.KEY_SERVICE_SERVICELAT)){
                            currentItem.setServiceLat(Double.valueOf(parser.nextText()));
                        } else if (name.equalsIgnoreCase(ItemService.KEY_SERVICE_SERVICELONG)){
                            currentItem.setServiceLong(Double.valueOf(parser.nextText()));
                        } else if (name.equalsIgnoreCase(ItemService.KEY_SERVICE_SERVICECATEGORYNAME)){
                        	currentItem.setServiceCategoryName(parser.nextText());
                        } else if (name.equalsIgnoreCase(ItemService.KEY_SERVICE_SERVICECATEGORYICONURL)){
                        	currentItem.setServiceCategoryIconURL(parser.nextText());
                        };
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("Services") && currentItem != null){
                    	String category=currentItem.getServiceCategoryName();
                    	if(category!=null && !category.trim().isEmpty()) {
                    		currentItem.setSortOrder(deriveSortOrder(category));
                    		items.add(currentItem);
                    	}
                    } 
                    break;
            }
            eventType = parser.next();
            
        }
        return items;
	}
	//Emergency, Medical, Grocery, Gas, Churches, Extras
	private int deriveSortOrder(String category) {
		if(category.equalsIgnoreCase("emergency")) {
			return 0;
		} else {
			if(category.equalsIgnoreCase("medical")) {
				return 1;
			} else {
				if(category.equalsIgnoreCase("grocery")) {
					return 2;
				} else {
					if(category.equalsIgnoreCase("gas")) {
						return 3;
					} else {
						if(category.equalsIgnoreCase("churches")) {
							return 4;
						} else {
							if(category.equalsIgnoreCase("extras")) {
								return 5;
							} else {
								return 99;
							}
						}
					}
				}
			}
		}
	}
}
