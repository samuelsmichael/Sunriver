package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.xmlpull.v1.XmlPullParserException;

public class SRWebServiceData {
	private XMLReaderFromRemotelyAcquiredXML mXmlReader;
	private Cacheable mCacheable;
	
	public SRWebServiceData(XMLReaderFromRemotelyAcquiredXML xmlReader, Cacheable cacheable) {
		mXmlReader=xmlReader;
		mCacheable=cacheable;
	}
	public ArrayList<Object> procureTheData() throws Exception  {
		/*
		 * 1. Check if data is expired
		 * 2. If data is expired, return mXmlReader's parse();
		 * 3. Otherwise, return a fetch from the database.
		 */
		if (!doWeHaveData()) {
			if(SplashPage.gotInternet) {
				try {
					ArrayList<Object> aloo= mXmlReader.parse();
					mCacheable.setLastDateReadToNow();
					// For ItemService, as it is two layered, fetchDataFromDatabase separates the layers properly
					if(mCacheable instanceof ItemService) {
						return fetchDataFromDatabase();
					}
					return aloo;
				} catch (XmlPullParserException e) {
					throw new Exception("Failed trying to fetch data off of Sunriver web service.  Msg: "+e.getLocalizedMessage());
				} catch (IOException e) {
					throw new Exception("Failed trying to fetch data off of Sunriver web service.  Msg: "+e.getLocalizedMessage());
				}
			} else {
				throw new Exception("Cannot obtain data.  No cached data exists, and cannot connect to Sunriver web service");
			}
		} else {
			if(isDataExpired()) { 
				try {
					ArrayList<Object> aloo= mXmlReader.parse();
					mCacheable.setLastDateReadToNow();
					// For ItemService, as it is two layered, fetchDataFromDatabase separates the layers properly
					if(mCacheable instanceof ItemService) {
						return fetchDataFromDatabase();
					}					
					return aloo;
				} catch (Exception e) {
					return fetchDataFromDatabase();
				}
			} else {
				return fetchDataFromDatabase();
			}
		}
	}
	private boolean doWeHaveData() {
		Date theDate=mCacheable.getLastDateRead();
		return theDate!=null;
	}
	private ArrayList<Object> fetchDataFromDatabase() {
		return mCacheable.fetchDataFromDatabase();
	}
	private boolean isDataExpired() {
		try {
			return mCacheable.isDataExpired();
		} catch (Exception e) {
			return true;
		}
	}
}
