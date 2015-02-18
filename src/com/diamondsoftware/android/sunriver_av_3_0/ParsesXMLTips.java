package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


public class ParsesXMLTips extends ParsesXML {
	public ParsesXMLTips(String dummy) {
		super(dummy);
	}
	@Override
	protected ArrayList<Object> parse(XmlPullParser parser) throws XmlPullParserException, IOException {
		ArrayList<Object> items = null;
        int eventType = parser.getEventType();
        ItemTip currentItem = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                	items = new ArrayList<Object>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("item")) {
                        currentItem = new ItemTip();
                    } else if (currentItem != null){
                        if (name.equalsIgnoreCase("tipsID")){
                        	String nextText=parser.nextText();
                            currentItem.setTipsId(Integer.parseInt(nextText));
                        } else if (name.equalsIgnoreCase("tipsURL")){
                        	currentItem.setTipsURL(parser.nextText());
                        } else if (name.equalsIgnoreCase("tipsAndroidOrder")){
                        	String nextText=parser.nextText();
                            currentItem.setTipsAndroidOrder(Integer.parseInt(nextText));
                        } else if (name.equalsIgnoreCase("tipsAppleOrder")) {
                        	String nextText=parser.nextText();
                        	currentItem.setTipsAppleOrder(Integer.parseInt(nextText));
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

