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
 * Provides the abstraction for fetching Sunriver data needed by a ListViewAdapter -- whether that
 * data be local on the machine (e.g. LandingPage, EatsAndTreats (whose data has already been acquired 
 * via the fetch of Sunriver locations)), or remote data (e.g. - Activities, Events)
 * 
 * XMLReader always have an ParsesXML object to which it passes an InputStream (again, acquired
 * either locally or remotely), and ask to parse the data into an ArrayList of SunriverDataItems. 
 *  
 * @author Mike Samuels
 *
 */
public abstract class XMLReader implements FormattedDataReader {
	private ParsesXML mParsesXML;
	/**
	 * 
	 */
	protected XMLReader(ParsesXML parsesXML) {
		mParsesXML=parsesXML;
	}
	public abstract ArrayList<Object> parse() throws Exception; 
	protected ArrayList<Object> parse(InputStream inputStream) throws  Exception {
		XmlPullParserFactory pullParserFactory;
		pullParserFactory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = pullParserFactory.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);
        ArrayList<Object> aL=mParsesXML.parse(parser);
        inputStream.close();
        return aL;
	}
}

