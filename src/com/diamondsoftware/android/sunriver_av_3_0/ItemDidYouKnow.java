package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

public class ItemDidYouKnow extends SunriverDataItem {
	int didYouKnowId;
	String didYouKnowURL;
	public static final String DATABASE_TABLE_DIDYOUKNOW="didyouknow";
	public static final String DATE_LAST_UPDATED = "date_last_updated_didyouknow";
	public static final String KEY_DIDYOUKNOW_ROWID = "_id";
	public static final String KEY_DIDYOUKNOW_DIDYOUKNOWID = "didYouKnowId";
	public static final String KEY_DIDYOUKNOW_DIDYOUKNOWURL = "didYouKnowURL";
	public int getDidYouKnowId() {
		return didYouKnowId;
	}

	public void setDidYouKnowId(int didYouKnowId) {
		this.didYouKnowId = didYouKnowId;
	}

	public String getDidYouKnowURL() {
		return didYouKnowURL;
	}

	public void setDidYouKnowURL(String didYouKnowURL) {
		this.didYouKnowURL = didYouKnowURL;
	}

	public ItemDidYouKnow() {		
	}
	public ItemDidYouKnow(Cursor cursor) {
		setDidYouKnowId(cursor.getInt(cursor.getColumnIndex(KEY_DIDYOUKNOW_DIDYOUKNOWID)));
		setDidYouKnowURL(cursor.getString(cursor.getColumnIndex(KEY_DIDYOUKNOW_DIDYOUKNOWURL)));
	}
	@Override
	public boolean isDataExpired() {
		Date dateDatabaseAtSunriverLastUpdated=SplashPage.TheItemUpdate.getUpdateDidYouKnow().getTime();
		Date lastTimeWeveFetchedData=getLastDateRead();
		return (
				SplashPage.TheItemUpdate==null || lastTimeWeveFetchedData==null ||
						dateDatabaseAtSunriverLastUpdated.after(lastTimeWeveFetchedData)
		);	
	}

	@Override
	protected String getTableName() {
		return DATABASE_TABLE_DIDYOUKNOW;
	}

	@Override
	protected String getDateLastUpdatedKey() {
		return DATE_LAST_UPDATED;
	}

	@Override
	protected void loadWriteItemToDatabaseContentValuesTo(ContentValues values) {
		values.put(KEY_DIDYOUKNOW_DIDYOUKNOWID, getDidYouKnowId());
		values.put(KEY_DIDYOUKNOW_DIDYOUKNOWURL, getDidYouKnowURL());
	}

	@Override
	protected String[] getProjectionForFetch() {
		String[] projectDetail={KEY_DIDYOUKNOW_DIDYOUKNOWID,KEY_DIDYOUKNOW_DIDYOUKNOWURL};
		return projectDetail;
	}

	@Override
	protected SunriverDataItem itemFromCursor(Cursor cursor) {
		return new ItemDidYouKnow(cursor);
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
