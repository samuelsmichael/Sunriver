package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

/*
 * Subclass of XMLReader, XMLREaderFromAndroidAssets read the XML from an asset.  That asset 
 * is then passed (as an XML document) to the associated ParsesXML object for parsing into
 * an ArrayList of SunriverDataItems.
 */
public class XMLReaderFromAndroidAssets extends XMLReader {

	private Context mContext;
	private String mXmlName;
	
	public XMLReaderFromAndroidAssets(Context context,ParsesXML parsesXML,String xmlName) {
		super(parsesXML);
		mXmlName=xmlName;			
		mContext=context;		
	}

	@Override
	public ArrayList<Object> parse() throws XmlPullParserException, IOException {
		return parse(mContext.getAssets().open(mXmlName));
	}
}


