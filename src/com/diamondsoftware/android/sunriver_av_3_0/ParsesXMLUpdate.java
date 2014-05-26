package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ParsesXMLUpdate extends ParsesXML {
	
	public ParsesXMLUpdate(String dummy) {
		super(dummy);
	}
	
	@Override
	protected ArrayList<Object> parse(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		
		ArrayList<Object> items = null;
        int eventType = parser.getEventType();
        ItemUpdate currentItem = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                	items = new ArrayList<Object>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("Update")) {
                        currentItem = new ItemUpdate();
                    } else if (currentItem != null){
                        if (name.equalsIgnoreCase("UpdateID")){
                        	String nextText=parser.nextText();
                            currentItem.setUpdateID(Integer.parseInt(nextText));
                        } else if (name.equalsIgnoreCase("updateOverlay")) {
                        	currentItem.setUpdateOverlay(Utils.toDateFromYYYYdashMMdashDDTHHcolonMMcolonSS(parser.nextText()));
                        } else if (name.equalsIgnoreCase("updateActivity")) {
                        	currentItem.setUpdateActivity(Utils.toDateFromYYYYdashMMdashDDTHHcolonMMcolonSS(parser.nextText()));
                        } else if (name.equalsIgnoreCase("updateCalendar")) {
                        	currentItem.setUpdateCalendar(Utils.toDateFromYYYYdashMMdashDDTHHcolonMMcolonSS(parser.nextText()));
                        } else if (name.equalsIgnoreCase("updateMaps")) {
                        	currentItem.setUpdateMaps(Utils.toDateFromYYYYdashMMdashDDTHHcolonMMcolonSS(parser.nextText()));
                        } else if (name.equalsIgnoreCase("updateServices")) {
                        	currentItem.setUpdateServices(Utils.toDateFromYYYYdashMMdashDDTHHcolonMMcolonSS(parser.nextText()));
                        } else if (name.equalsIgnoreCase("updateWelcome")) {
                        	currentItem.setUpdateWelcome(Utils.toDateFromYYYYdashMMdashDDTHHcolonMMcolonSS(parser.nextText()));
                        } else if (name.equalsIgnoreCase("srActDate")) {
                        	currentItem.setSrActDate(Utils.toDateFromYYYYdashMMdashDDTHHcolonMMcolonSS(parser.nextText()));
                        } else if (name.equalsIgnoreCase("updateData")) {
                        	currentItem.setUpdateData(Utils.toDateFromYYYYdashMMdashDDTHHcolonMMcolonSS(parser.nextText()));
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("Update") && currentItem != null){
                   		items.add(currentItem);
                    } 
                    break;
            }
            eventType = parser.next();
        }
        return items;

	}

}
