package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

public class ItemWelcome extends SunriverDataItem {
	private int welcomeID;
	private String welcomeURL;

	public static final String DATABASE_TABLE_WELCOME = "welcome";
	public static final String DATE_LAST_UPDATED = "date_last_updated_welcome";
	public static final String KEY_WELCOME_ROWID = "_id";
	public static final String KEY_WELCOME_WELCOMEID = "welcomeID";
	public static final String KEY_WELCOME_WELCOMEURL = "welcomeURL";
	
	
	public ItemWelcome() {
	}
	protected ItemWelcome(Cursor cursor) {
		this.setWelcomeID(cursor.getInt(cursor.getColumnIndex(this.KEY_WELCOME_WELCOMEID)));
		setWelcomeURL(cursor.getString(cursor.getColumnIndex(KEY_WELCOME_WELCOMEURL)));
		
	}

	public int getWelcomeID() {
		return welcomeID;
	}
	public void setWelcomeID(int welcomeID) {
		this.welcomeID = welcomeID;
	}
	public String getWelcomeURL() {
		return welcomeURL;
	}
	public void setWelcomeURL(String welcomeURL) {
		this.welcomeURL = welcomeURL;
	}

	@Override
	public boolean isDataExpired() {
		Date dateDatabaseAtSunriverLastUpdated=SplashPage.TheItemUpdate.getUpdateWelcome().getTime();
		Date lastTimeWeveFetchedData=getLastDateRead();
		return (
				SplashPage.TheItemUpdate==null || lastTimeWeveFetchedData==null ||
						dateDatabaseAtSunriverLastUpdated.after(lastTimeWeveFetchedData)
		);
	}

	@Override
	protected String getTableName() {
		return DATABASE_TABLE_WELCOME;
	}

	@Override
	protected String getDateLastUpdatedKey() {
		return DATE_LAST_UPDATED;
	}

	@Override
	protected void loadWriteItemToDatabaseContentValuesTo(ContentValues values) {
		values.put(KEY_WELCOME_WELCOMEID, this.getWelcomeID());
		values.put(KEY_WELCOME_WELCOMEURL, getWelcomeURL());
	}

	@Override
	protected String[] getProjectionForFetch() {
		String[] projection = {KEY_WELCOME_WELCOMEID, KEY_WELCOME_WELCOMEURL };
		return projection;
	}

	@Override
	protected SunriverDataItem itemFromCursor(Cursor cursor) {
		return new ItemWelcome(cursor);
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
