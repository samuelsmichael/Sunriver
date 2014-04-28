package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

public class ItemSelfie extends SunriverDataItem {
	
	public static final String DATABASE_TABLE_SELFIE = "selfie";
	public static final String DATE_LAST_UPDATED = "date_last_updated_selfie";
	public static final String KEY_SELFIE_ROWID = "_id";
	public static final String KEY_SELFIE_OVERLAYID = "overlayId";
	public static final String KEY_SELFIE_OVERLAYLSURL = "overlaylSurl";
	public static final String KEY_SELFIE_OVERLAYLSSELECTURL = "overlayLsSelectURL";
	public static final String KEY_SELFIE_OVERLAYPORTURL = "overlayPortURL";
	public static final String KEY_SELFIE_OVERLAYPORTCAMURL = "overlayPortCamURL";

	
	private int overlayId;
	private String overlayLsURL;
	private String overlayLsSelectURL;
	private String overlayPortURL;
	private String overlayPortCamURL;

	public int getOverlayId() {
		return overlayId;
	}

	public void setOverlayId(int overlayId) {
		this.overlayId = overlayId;
	}

	public String getOverlayLsURL() {
		return overlayLsURL;
	}

	public void setOverlayLsURL(String overlayLsURL) {
		this.overlayLsURL = overlayLsURL;
	}

	public String getOverlayLsSelectURL() {
		return overlayLsSelectURL;
	}

	public void setOverlayLsSelectURL(String overlayLsSelectURL) {
		this.overlayLsSelectURL = overlayLsSelectURL;
	}

	public String getOverlayPortURL() {
		return overlayPortURL;
	}

	public void setOverlayPortURL(String overlayPortURL) {
		this.overlayPortURL = overlayPortURL;
	}

	public String getOverlayPortCamURL() {
		return overlayPortCamURL;
	}

	public void setOverlayPortCamURL(String overlayPortCamURL) {
		this.overlayPortCamURL = overlayPortCamURL;
	}

	public ItemSelfie() {
	}

	protected ItemSelfie(Cursor cursor) {
		setOverlayId(cursor.getInt(cursor.getColumnIndex(KEY_SELFIE_OVERLAYID)));
		this.setOverlayLsSelectURL(cursor.getString(cursor.getColumnIndex(KEY_SELFIE_OVERLAYLSSELECTURL)));
		this.setOverlayLsURL(cursor.getString(cursor.getColumnIndex(KEY_SELFIE_OVERLAYLSURL)));
		this.setOverlayPortCamURL(cursor.getString(cursor.getColumnIndex(KEY_SELFIE_OVERLAYPORTCAMURL)));
		this.setOverlayPortURL(cursor.getString(cursor.getColumnIndex(KEY_SELFIE_OVERLAYPORTURL)));
	}
	
	@Override
	public boolean isDataExpired() {
		Date dateDatabaseAtSunriverLastUpdated=SplashPage.TheItemUpdate.getUpdateOverlay().getTime();
		Date lastTimeWeveFetchedData=getLastDateRead();
		return (
				SplashPage.TheItemUpdate==null || lastTimeWeveFetchedData==null ||
						dateDatabaseAtSunriverLastUpdated.after(lastTimeWeveFetchedData)
		);
	}

	@Override
	protected String getTableName() {
		return DATABASE_TABLE_SELFIE;
	}

	@Override
	protected String getDateLastUpdatedKey() {
		return DATE_LAST_UPDATED;
	}

	@Override
	protected void loadWriteItemToDatabaseContentValuesTo(ContentValues values) {
		values.put(KEY_SELFIE_OVERLAYID, this.getOverlayId());
		values.put(KEY_SELFIE_OVERLAYLSSELECTURL,this.getOverlayLsSelectURL());
		values.put(KEY_SELFIE_OVERLAYLSURL, this.getOverlayLsURL());
		values.put(KEY_SELFIE_OVERLAYPORTCAMURL, this.getOverlayPortCamURL());
		values.put(KEY_SELFIE_OVERLAYPORTURL, this.getOverlayPortURL());
	}

	@Override
	protected String[] getProjectionForFetch() {
		String[] projection={KEY_SELFIE_OVERLAYID,KEY_SELFIE_OVERLAYLSSELECTURL,KEY_SELFIE_OVERLAYLSURL,KEY_SELFIE_OVERLAYPORTCAMURL,KEY_SELFIE_OVERLAYPORTURL };
		return projection;
	}

	@Override
	protected SunriverDataItem itemFromCursor(Cursor cursor) {
		return new ItemSelfie(cursor);
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
