package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ParsesXMLDidYouKnow extends ParsesXML {

	public ParsesXMLDidYouKnow() {

	}

	@Override
	protected ArrayList<Object> parse(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		ArrayList<Object> items = null;
        int eventType = parser.getEventType();
        ItemDidYouKnow currentItem = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                	items = new ArrayList<Object>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("DidYouKnow")) {
                        currentItem = new ItemDidYouKnow();
                    } else if (currentItem != null){
                        if (name.equalsIgnoreCase("didYouKnowId")){
                        	String nextText=parser.nextText();
                            currentItem.setDidYouKnowId(Integer.parseInt(nextText));
                        } else if (name.equalsIgnoreCase("didYouKnowURL")){
                        	currentItem.setDidYouKnowURL(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("DidYouKnow") && currentItem != null){
           				items.add(currentItem);
                    } 
                    break;
            }
            eventType = parser.next();
        }
        return items;

	}

}
