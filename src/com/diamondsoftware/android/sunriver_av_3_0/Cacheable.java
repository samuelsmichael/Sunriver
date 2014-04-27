package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;
import java.util.Date;


public interface Cacheable {
	public long writeItemToDatabase(); // writes an item to the database, and returns rowId
	public void clearTable();
	public Date getLastDateRead();
	public ArrayList<Object> fetchDataFromDatabase();
	public void setLastDateReadToNow();
	public boolean isDataExpired();
	public void forceNewFetch();
}
