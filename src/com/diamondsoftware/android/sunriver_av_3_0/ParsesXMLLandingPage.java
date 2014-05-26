package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


public class ParsesXMLLandingPage extends ParsesXML {
	public ParsesXMLLandingPage(String dummy) {
		super(dummy);
	}
	@Override
	protected ArrayList<Object> parse(XmlPullParser parser) throws XmlPullParserException, IOException {
		ArrayList<Object> items = null;
        int eventType = parser.getEventType();
        ItemLandingPage currentItem = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                	items = new ArrayList<Object>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("item")) {
                        currentItem = new ItemLandingPage();
                    } else if (currentItem != null){
                        if (name.equalsIgnoreCase("id")){
                        	String nextText=parser.nextText();
                            currentItem.setId(Integer.parseInt(nextText));
                        } else if (name.equalsIgnoreCase("name")){
                        	currentItem.setName(parser.nextText());
                        } else if (name.equalsIgnoreCase("description")){
                            currentItem.setDescription(parser.nextText());
                        } else if (name.equalsIgnoreCase("iconname")) {
                        	currentItem.setIconName(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("item") && currentItem != null){
                    	items.add(currentItem);
                    } 
            }
            eventType = parser.next();
        }
        return items;
	}
}

