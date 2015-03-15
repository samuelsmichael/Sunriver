package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

public class ItemEventPic extends SunriverDataItem {
	int eventPicsId;
	String eventPicsURL;
	public static final String DATABASE_TABLE_EVENTPICS="eventpics";
	public static final String DATE_LAST_UPDATED = "date_last_updated_eventpics";
	public static final String KEY_EVENTPICS_ROWID = "_id";
	public static final String KEY_EVENTPICS_EVENTPICSID = "eventPicsID";
	public static final String KEY_EVENTPICS_EVENTPICSURL = "eventPicsURL";
	
	public int getEventPicsId() {
		return eventPicsId;
	}

	public void setEventPicsId(int eventPicsId) {
		this.eventPicsId = eventPicsId;
	}

	public String getEventPicsURL() {
		return eventPicsURL;
	}

	public void setEventPicsURL(String eventPicsURL) {
		this.eventPicsURL = eventPicsURL;
	}

	public ItemEventPic() {		
	}
	public ItemEventPic(Cursor cursor) {
		setEventPicsId(cursor.getInt(cursor.getColumnIndex(KEY_EVENTPICS_EVENTPICSID)));
		setEventPicsURL(cursor.getString(cursor.getColumnIndex(KEY_EVENTPICS_EVENTPICSURL)));
	}
	@Override
	public boolean isDataExpired() {
		if(GlobalState.TheItemUpdate==null) {
			return true;
		}
		Date dateDatabaseAtSunriverLastUpdated=GlobalState.TheItemUpdate.getEventPics().getTime();
		Date lastTimeWeveFetchedData=getLastDateRead();
		return (
				GlobalState.TheItemUpdate==null || lastTimeWeveFetchedData==null || GlobalState.TheItemUpdate.getEventPics()==null ||
						dateDatabaseAtSunriverLastUpdated.after(lastTimeWeveFetchedData)
		);	
	}

	@Override
	protected String getTableName() {
		return DATABASE_TABLE_EVENTPICS;
	}

	@Override
	protected String getDateLastUpdatedKey() {
		return DATE_LAST_UPDATED;
	}

	@Override
	protected void loadWriteItemToDatabaseContentValuesTo(ContentValues values) {
		values.put(KEY_EVENTPICS_EVENTPICSID, getEventPicsId());
		values.put(KEY_EVENTPICS_EVENTPICSURL, getEventPicsURL());
	}

	@Override
	protected String[] getProjectionForFetch() {
		String[] projectDetail={KEY_EVENTPICS_EVENTPICSID,KEY_EVENTPICS_EVENTPICSURL};
		return projectDetail;
	}

	@Override
	protected SunriverDataItem itemFromCursor(Cursor cursor) {
		return new ItemEventPic(cursor);
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
