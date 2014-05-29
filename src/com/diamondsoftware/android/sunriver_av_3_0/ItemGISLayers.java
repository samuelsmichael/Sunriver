package com.diamondsoftware.android.sunriver_av_3_0;

import android.content.ContentValues;
import android.database.Cursor;

public class ItemGISLayers extends SunriverDataItem {
	private int srGISLayersUseNum;
	private int srGISLayersId;
	private String srGISLayersURL;
	private boolean srGISLayersIsBikePaths;
	public static final String DATABASE_TABLE_GISLAYERS = "gislayers";
	public static final String DATE_LAST_UPDATED = "date_last_updated_gislayers";

	public static final String KEY_GISLAYERS_ROWID="_id";
	public static final String KEY_GISLAYERS_USENUM="srGISLayersUseNum";
	public static final String KEY_GISLAYERS_ID="srGISLayersId";
	public static final String KEY_GISLAYERS_URL="srGISLayersURL";
	public static final String KEY_GISLAYERS_ISBIKEPATHS="srGISLayersIsBikePaths";
	
	public ItemGISLayers(Cursor cursor) {
		setSrGISLayersUseNum(cursor.getInt(cursor.getColumnIndex(KEY_GISLAYERS_USENUM)));
		setSrGISLayersId(cursor.getInt(cursor.getColumnIndex(KEY_GISLAYERS_ID)));
		setSrGISLayersIsBikePaths(cursor.getInt(cursor.getColumnIndex(KEY_GISLAYERS_ISBIKEPATHS))!=0);
		setSrGISLayersURL(cursor.getString(cursor.getColumnIndex(KEY_GISLAYERS_URL)));
		
	}
	public int getSrGISLayersUseNum() {
		return srGISLayersUseNum;
	}

	public void setSrGISLayersUseNum(int srGISLayersUseNum) {
		this.srGISLayersUseNum = srGISLayersUseNum;
	}

	public int getSrGISLayersId() {
		return srGISLayersId;
	}

	public void setSrGISLayersId(int srGISLayersId) {
		this.srGISLayersId = srGISLayersId;
	}

	public String getSrGISLayersURL() {
		return srGISLayersURL;
	}

	public void setSrGISLayersURL(String srGISLayersURL) {
		this.srGISLayersURL = srGISLayersURL;
	}
	public boolean isSrGISLayersIsBikePaths() {
		return srGISLayersIsBikePaths;
	}

	public void setSrGISLayersIsBikePaths(boolean srGISLayersIsBikePaths) {
		this.srGISLayersIsBikePaths = srGISLayersIsBikePaths;
	}

	
	public ItemGISLayers() {
	}

	@Override
	public boolean isDataExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected String getTableName() {
		return DATABASE_TABLE_GISLAYERS;
	}

	@Override
	protected String getDateLastUpdatedKey() {
		return DATE_LAST_UPDATED;
	}

	@Override
	protected void loadWriteItemToDatabaseContentValuesTo(ContentValues values) {
		values.put(KEY_GISLAYERS_ID, getSrGISLayersId());
		values.put(KEY_GISLAYERS_ISBIKEPATHS, isSrGISLayersIsBikePaths());
		values.put(KEY_GISLAYERS_USENUM, getSrGISLayersUseNum());
		values.put(KEY_GISLAYERS_URL, getSrGISLayersURL());
	}

	@Override
	protected String[] getProjectionForFetch() {
		String[] projection={KEY_GISLAYERS_ID,KEY_GISLAYERS_ISBIKEPATHS,KEY_GISLAYERS_USENUM,KEY_GISLAYERS_URL};
		return projection;
	}

	@Override
	protected SunriverDataItem itemFromCursor(Cursor cursor) {
		return new ItemGISLayers(cursor);
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
