package com.diamondsoftware.android.sunriver_av_3_0;

import android.content.ContentValues;
import android.database.Cursor;

public class ItemAllHomes extends SunriverDataItem {
	public static String CITY_STATE_ZIP="Sunriver OR 97707";
	public String getSunriverAddress() {
		return sunriverAddress;
	}
	public void setSunriverAddress(String sunriverAddress) {
		this.sunriverAddress = sunriverAddress;
	}
	public String getCountyAddress() {
		return countyAddress;
	}
	public void setCountyAddress(String countyAddress) {
		this.countyAddress = countyAddress;
	}

	private String sunriverAddress;
	private String countyAddress;
	public static final String DATABASE_TABLE_ALLHOMES = "allhomes";
	public static final String DATE_LAST_UPDATED = "date_last_allhomes_welcome";
	public static final String KEY_ALLHOMES_ROWID = "_id";
	public static final String KEY_ALLHOMES_SUNRIVER_ADDRESS = "SRAddress";
	public static final String KEY_ALLHOMES_COUNTY_ADDRESS = "DC_Address";
	
	public ItemAllHomes() {}
	protected ItemAllHomes(Cursor cursor) {
		setSunriverAddress(cursor.getString(cursor.getColumnIndex(KEY_ALLHOMES_SUNRIVER_ADDRESS)));
		setCountyAddress(cursor.getString(cursor.getColumnIndex(KEY_ALLHOMES_COUNTY_ADDRESS)));
	}

	@Override
	public boolean isDataExpired() {
		/* Don't load these more than once */
		return false;
	}

	@Override
	protected String getTableName() {
		return DATABASE_TABLE_ALLHOMES;
	}

	@Override
	protected String getDateLastUpdatedKey() {
		return DATE_LAST_UPDATED;
	}

	@Override
	protected void loadWriteItemToDatabaseContentValuesTo(ContentValues values) {
		values.put(KEY_ALLHOMES_SUNRIVER_ADDRESS, getSunriverAddress());
		values.put(KEY_ALLHOMES_COUNTY_ADDRESS, getCountyAddress());
	}

	@Override
	protected String[] getProjectionForFetch() {
		String[] projection={KEY_ALLHOMES_SUNRIVER_ADDRESS,KEY_ALLHOMES_COUNTY_ADDRESS};
		return projection;
	}

	@Override
	protected SunriverDataItem itemFromCursor(Cursor cursor) {
		return new ItemAllHomes(cursor);
	}

	@Override
	protected String getColumnNameForWhereClause() {
		return null;
	}

	@Override
	protected String[] getColumnValuesForWhereClause() {
		return null;
	}

	@Override
	protected String getGroupBy() {
		return null;
	}
	@Override
	protected String getOrderBy() {
		return null;
	}

}
