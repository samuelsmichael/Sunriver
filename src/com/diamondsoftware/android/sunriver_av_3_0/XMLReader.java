/**
 * 
 */
package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


/**
 * @author Mike Samuels
 *
 */
public abstract class XMLReader {
	private ParsesXML mParsesXML;
	/**
	 * 
	 */
	protected XMLReader(ParsesXML parsesXML) {
		mParsesXML=parsesXML;
	}
	public abstract ArrayList<Object> parse() throws XmlPullParserException, IOException; 
	protected ArrayList<Object> parse(InputStream inputStream) throws XmlPullParserException, IOException {
		XmlPullParserFactory pullParserFactory;
		pullParserFactory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = pullParserFactory.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);		
        return mParsesXML.parse(parser);
	}
}

