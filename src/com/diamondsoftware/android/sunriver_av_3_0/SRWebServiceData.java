package com.diamondsoftware.android.sunriver_av_3_0;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.xmlpull.v1.XmlPullParserException;

/**
 * This class is a wrapper class to XMLReaderFromRemotelyAcquiredXML, providing the functionality of
 * making it so if the data is not in the database, or if the data is outdated, that the data is 
 * fetched from the Internet; otherwise it is fetched from the database.  This makes it transparent to
 * the whichever object needs to obtain the data (e.g. - a ListViewAdapter) as to where the data is
 * being fetched.
 * 
 * @author Diamond
 *
 */
public class SRWebServiceData {
	private FormattedDataReader mFormattedDataReader;
	private Cacheable mCacheable;
	
	public SRWebServiceData(FormattedDataReader formattedDataReader, Cacheable cacheable) {
		mFormattedDataReader=formattedDataReader;
		mCacheable=cacheable;
	}
	public ArrayList<Object> procureTheData() throws Exception  {
		/*
		 * 1. Check if data is expired
		 * 2. If data is expired, return mXmlReader's parse();
		 * 3. Otherwise, return a fetch from the database.
		 */
		if (!doWeHaveData()) {
			if(GlobalState.gotInternet) {
				try {
					ArrayList<Object> aloo= mFormattedDataReader.parse();
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
					ArrayList<Object> aloo= mFormattedDataReader.parse();
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
