package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ParsesXMLSelfie extends ParsesXML {
	
	public ParsesXMLSelfie(String dummy) {
		super(dummy);
	}
	
	@Override
	protected ArrayList<Object> parse(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		
		ArrayList<Object> items = null;
        int eventType = parser.getEventType();
        ItemSelfie currentItem = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                	items = new ArrayList<Object>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("Overlay")) {
                        currentItem = new ItemSelfie();
                    } else if (currentItem != null){
                        if (name.equalsIgnoreCase("overlayId")){
                        	String nextText=parser.nextText();
                            currentItem.setOverlayId(Integer.parseInt(nextText));
                        } else if (name.equalsIgnoreCase("overlayLsURL")) {
                        	currentItem.setOverlayLsURL(parser.nextText());
                        } else if (name.equalsIgnoreCase("overlayLsSelectURL")) {
                        	currentItem.setOverlayLsSelectURL(parser.nextText());
                        } else if (name.equalsIgnoreCase("overlayPortURL")) {
                        	currentItem.setOverlayPortURL(parser.nextText());
                        } else if (name.equalsIgnoreCase("overlayPortCamURL")) {
                        	currentItem.setOverlayPortCamURL(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("Overlay") && currentItem != null){
                   		items.add(currentItem);
                    } 
                    break;
            }
            eventType = parser.next();
        }
        return items;

	}

}
