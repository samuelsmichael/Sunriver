package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ParsesXMLActivityPage extends ParsesXML {

	public ParsesXMLActivityPage() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ArrayList<Object> parse(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		ArrayList<Object> items = null;
        int eventType = parser.getEventType();
        ItemActivity currentItem = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                	items = new ArrayList<Object>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("Activity")) {
                        currentItem = new ItemActivity();
                    } else if (currentItem != null){
                        if (name.equalsIgnoreCase("srActId")){
                        	String nextText=parser.nextText();
                            currentItem.setSrActID(Integer.parseInt(nextText));
                        } else if (name.equalsIgnoreCase("srActName")){
                        	currentItem.setSrActName(parser.nextText());
                        } else if (name.equalsIgnoreCase("srActDescription")){
                            currentItem.setSrActDescription(parser.nextText());
                        } else if (name.equalsIgnoreCase("srActDate")) {
                        	currentItem.setSrActDate(Utils.toDateFromYYYYdashMMdashDDTHHcolonMMcolonSS(parser.nextText()));
                        } else if (name.equalsIgnoreCase("srActTime")){
                            currentItem.setSrActTime(parser.nextText());
                        } else if (name.equalsIgnoreCase("srActDuration")){
                            currentItem.setSrActDuration(parser.nextText());
                        } else if (name.equalsIgnoreCase("srActLinks")){
                            currentItem.setSrActLinks(parser.nextText());
                        } else if (name.equalsIgnoreCase("srActUrlImage")){
                            currentItem.setSrActUrlImage(parser.nextText());
                        } else if (name.equalsIgnoreCase("srActLat")){
                            currentItem.setSrActLat(Double.valueOf(parser.nextText()));
                        } else if (name.equalsIgnoreCase("srActLong")){
                            currentItem.setSrActLong(Double.valueOf(parser.nextText()));
                        } else if (name.equalsIgnoreCase("isApproved")){
                            currentItem.setApproved(Boolean.valueOf(parser.nextText()));
                        } else if (name.equalsIgnoreCase("srActAddress")){
                            currentItem.setSrActAddress(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("Activity") && currentItem != null){
           				items.add(currentItem);
                    } 
                    break;
            }
            eventType = parser.next();
        }
        return items;

	}

}
