package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/**
 * All ParsesXML subclasses build an XmlPullParser, passing it an InputStream.  The
 * XmlPullParser then parses the XML, creating an ArrayList of objects (generally SunriverDataItems).
 * @author Diamond
 *
 */
public abstract class ParsesXML {
	public ParsesXML(String dummy) {
	}
	protected abstract ArrayList<Object> parse(XmlPullParser parser) throws XmlPullParserException, IOException;
}
