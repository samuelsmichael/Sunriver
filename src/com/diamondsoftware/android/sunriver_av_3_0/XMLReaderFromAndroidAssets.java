package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

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


