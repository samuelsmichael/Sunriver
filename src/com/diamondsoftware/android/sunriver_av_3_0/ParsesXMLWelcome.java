package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ParsesXMLWelcome extends ParsesXML {

	public ParsesXMLWelcome(String dummy) {
		super(dummy);
	}

	@Override
	protected ArrayList<Object> parse(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		ArrayList<Object> items = null;
        int eventType = parser.getEventType();
        ItemWelcome currentItem = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                	items = new ArrayList<Object>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("Welcome")) {
                        currentItem = new ItemWelcome();
                    } else if (currentItem != null){
                        if (name.equalsIgnoreCase("welcomeID")){
                        	String nextText=parser.nextText();
                            currentItem.setWelcomeID(Integer.parseInt(nextText));
                        } else if (name.equalsIgnoreCase("welcomeURL")){
                        	currentItem.setWelcomeURL(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("Welcome") && currentItem != null){
           				items.add(currentItem);
                    } 
                    break;
            }
            eventType = parser.next();
        }
        return items;

	}

}
