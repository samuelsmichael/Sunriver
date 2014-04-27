package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ParsesXMLAllHomes extends ParsesXML {

	public ParsesXMLAllHomes() {

	}

	@Override
	protected ArrayList<Object> parse(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		ArrayList<Object> items = null;
        int eventType = parser.getEventType();
        ItemAllHomes currentItem = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                	items = new ArrayList<Object>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("AddConvert")) {
                        currentItem = new ItemAllHomes();
                    } else if (currentItem != null){
                        if (name.equalsIgnoreCase("SRAddress")){
                            currentItem.setSunriverAddress(parser.nextText());
                        } else if (name.equalsIgnoreCase("DC_Address")){
                        	currentItem.setCountyAddress(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("AddConvert") && currentItem != null){
           				items.add(currentItem);
                    } 
                    break;
            }
            eventType = parser.next();
        }
        return items;

	}

}
