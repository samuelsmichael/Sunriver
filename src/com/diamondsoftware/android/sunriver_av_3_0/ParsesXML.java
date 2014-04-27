package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public abstract class ParsesXML {
	public ParsesXML() {
	}
	protected abstract ArrayList<Object> parse(XmlPullParser parser) throws XmlPullParserException, IOException;
}
