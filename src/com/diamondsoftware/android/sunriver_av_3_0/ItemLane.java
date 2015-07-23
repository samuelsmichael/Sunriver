package com.diamondsoftware.android.sunriver_av_3_0;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.diamondsoftware.android.sunriver_av_3_0.DbAdapter.FavoriteItemType;

import android.content.ContentValues;
import android.database.Cursor;


public class ItemLane extends SunriverDataItem {
	
	public static final String KEY_LANE_ROWID = "_id";
	public static final String KEY_LANE_SRLANE = "srLane";

	public static final String DATABASE_TABLE_LANE = "lane";	
	public static final String DATE_LAST_UPDATED="date_last_updated_lane";
	private SimpleDateFormat simpleFormatter;


	private String srLane;
	public static ArrayList<Object> mLastDataFetch;
	
	@Override
	public ArrayList<Object> fetchDataFromDatabase() {
		if(mLastDataFetch==null) {
			mLastDataFetch=super.fetchDataFromDatabase();
		}
		return mLastDataFetch;
	}
	
	public ItemLane() {
	}
		
	@Override
	public String toString() {
		return "Lane: "
				+ ((srLane == null || srLane.trim().length() == 0) ? ""
						: srLane);
	}
	
	
	public ItemLane (Cursor cursor) {
		this.setSrLane(cursor.getString(cursor.getColumnIndex(KEY_LANE_SRLANE)));
	}
	
	public String getSrLane() {
		return srLane;
	}

	public void setSrLane(String srLane) {
		this.srLane = srLane;
	}


	@Override
	protected String getTableName() {
		return DATABASE_TABLE_LANE;
	}

	@Override
	protected String getDateLastUpdatedKey() {
		return DATE_LAST_UPDATED;
	}
	
	@Override
	protected void loadWriteItemToDatabaseContentValuesTo(ContentValues values) {
		values.put(KEY_LANE_SRLANE, getSrLane());
	}
	
	@Override
	protected String[] getProjectionForFetch() {
		String[] projection = {
				KEY_LANE_ROWID,
				KEY_LANE_SRLANE
		};
		return projection;
	}
	@Override
	protected SunriverDataItem itemFromCursor(Cursor cursor) {
		return new ItemLane(cursor);
	}
	@Override
	public boolean isDataExpired() {
		Date lastDateRead=getLastDateRead();
		//boolean tableExists=tableExists();
		//tableExists=tableExists;
		return (
				GlobalState.TheItemUpdate==null || lastDateRead==null || GlobalState.TheItemUpdate.getUpdateLane()==null ||
				GlobalState.TheItemUpdate.getUpdateLane().getTime().after(lastDateRead)// || !tableExists()
		);
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
