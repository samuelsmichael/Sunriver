package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ParsesXMLAlert extends ParsesXML {

	public ParsesXMLAlert(String dummy) {
		super(dummy);
	}

	@Override
	protected ArrayList<Object> parse(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		ArrayList<Object> items = null;
        int eventType = parser.getEventType();
        ItemAlert currentItem = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                	items = new ArrayList<Object>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("Alert")) {
                        currentItem = new ItemAlert();
                    } else if (currentItem != null){
                        if (name.equalsIgnoreCase("ALID")){
                        	String nextText=parser.nextText();
                            currentItem.setmALID(Integer.parseInt(nextText));
                        } else if (name.equalsIgnoreCase("ALTitle")){
                        	currentItem.setmALTitle(parser.nextText());
                        } else if (name.equalsIgnoreCase("ALDescription")){
                            currentItem.setmALDescription(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("Alert") && currentItem != null){
           				items.add(currentItem);
                    } 
                    break;
            }
            eventType = parser.next();
        }
        return items;

	}

}
