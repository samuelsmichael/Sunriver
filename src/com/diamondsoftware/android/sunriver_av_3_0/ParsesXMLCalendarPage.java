package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ParsesXMLCalendarPage extends ParsesXML {

	public ParsesXMLCalendarPage(String dummy) {
		super(dummy);
	}
	
	@Override
	protected ArrayList<Object> parse(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		
		ArrayList<Object> items = null;
        int eventType = parser.getEventType();
        ItemCalendar currentItem = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                	items = new ArrayList<Object>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("Calendar")) {
                        currentItem = new ItemCalendar();
                    } else if (currentItem != null){
                        if (name.equalsIgnoreCase("srCalId")){
                        	String nextText=parser.nextText();
                            currentItem.setSrCalId(Integer.parseInt(nextText));
                        } else if (name.equalsIgnoreCase("srCalName")){
                        	currentItem.setSrCalName(parser.nextText());
                        } else if (name.equalsIgnoreCase("srCalDescription")){
                            currentItem.setSrCalDescription(parser.nextText());
                        } else if (name.equalsIgnoreCase("srCalDate")) {
                        	currentItem.setSrCalDate(Utils.toDateFromYYYYdashMMdashDDTHHcolonMMcolonSS(parser.nextText()));
                        } else if (name.equalsIgnoreCase("srCalTime")){
                            currentItem.setSrCalTime(parser.nextText());
                        } else if (name.equalsIgnoreCase("srCalDuration")){
                            currentItem.setSrCalDuration(parser.nextText());
                        } else if (name.equalsIgnoreCase("srCalLinks")){
                            currentItem.setSrCalLinks(parser.nextText());
                        } else if (name.equalsIgnoreCase("srCalUrlImage")){
                            currentItem.setSrCalUrlImage(parser.nextText());
                        } else if (name.equalsIgnoreCase("srCalLat")){
                            currentItem.setSrCalLat(Double.valueOf(parser.nextText()));
                        } else if (name.equalsIgnoreCase("srCalLong")){
                            currentItem.setSrCalLong(Double.valueOf(parser.nextText()));
                        } else if (name.equalsIgnoreCase("isApproved")){
                            currentItem.setApproved(Boolean.valueOf(parser.nextText()));
                        } else if (name.equalsIgnoreCase("srCalAddress")){
                            currentItem.setSrCalAddress(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("Calendar") && currentItem != null){
                    	items.add(currentItem);
                    } 
                    break;
            }
            eventType = parser.next();
        }
        return items;

	}

}
