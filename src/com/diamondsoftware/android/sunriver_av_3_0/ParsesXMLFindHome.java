package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ParsesXMLFindHome extends ParsesXML {
	String mResortName;
	public ParsesXMLFindHome(String resortName, String dummy) {
		super(dummy);
		mResortName=resortName;
	}

	@Override
	protected ArrayList<Object> parse(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		ArrayList<Object> items = null;
        int eventType = parser.getEventType();
        ItemFindHome currentItem = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                	items = new ArrayList<Object>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("CountyAddress")) {
                        currentItem = new ItemFindHome(mResortName);
                    } else if (currentItem != null){
                        if (name.equalsIgnoreCase("mAddress")){
                        	String nextText=parser.nextText();
                            currentItem.setmDC_Address(nextText);  
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("CountyAddress") && currentItem != null){
           				items.add(currentItem);
                    } 
                    break;
            }
            eventType = parser.next();
        }
        return items;

	}

}
