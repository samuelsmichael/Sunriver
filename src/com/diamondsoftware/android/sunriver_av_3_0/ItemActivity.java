package com.diamondsoftware.android.sunriver_av_3_0;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;


public class ItemActivity extends SunriverDataItem {
	
	public static final String KEY_ACTIVITY_ROWID = "_id";
	public static final String KEY_ACTIVITY_SRACTID = "srActID";
	public static final String KEY_ACTIVITY_SRACTNAME = "srActName";
	public static final String KEY_ACTIVITY_SRACTDESCRIPTION = "srActDescription";
	public static final String KEY_ACTIVITY_SRACTDATE = "srActDate";
	public static final String KEY_ACTIVITY_SRACTTIME = "srActTime";
	public static final String KEY_ACTIVITY_SRACTDURATION = "srActDuration";
	public static final String KEY_ACTIVITY_SRACTLINKS = "srActLinks";
	public static final String KEY_ACTIVITY_SRACTURLIMAGE = "srActUrlImage";
	public static final String KEY_ACTIVITY_SRACTADDRESS = "srActAddress";
	public static final String KEY_ACTIVITY_SRACTLAT = "srActLat";
	public static final String KEY_ACTIVITY_SRACTLONG = "srActLong";
	public static final String KEY_ACTIVITY_ISAPPROVED = "isApproved";

	public static final String DATABASE_TABLE_ACTIVITY = "activity";	
	public static final String DATE_LAST_UPDATED="date_last_updated_activity";
	private SimpleDateFormat simpleFormatter;


	private int srActID;
	private String srActName;
	private String srActDescription;
	private GregorianCalendar srActDate;
	private String srActTime;
	private String srActDuration;
	private String srActLinks;
	private String srActUrlImage;
	private String srActAddress;
	private double srActLat;
	private double srActLong;
	private boolean isApproved;

	public ItemActivity() {
	}
	
	@Override
	public String toString() {
		String theDate = null;
		if (srActDate != null) {
			simpleFormatter = new SimpleDateFormat("EEEE, MMMM d, yyyy",
					Locale.getDefault());
			simpleFormatter.setCalendar(srActDate);
			Date dt = srActDate.getTime();
			theDate = simpleFormatter.format(dt);
		}
		return "Name: "
				+ ((srActName == null || srActName.trim().length() == 0) ? "None provided"
						: srActName)
				+ "\n"
				+ "Description: "
				+ ((srActDescription == null || srActDescription.trim()
						.length() == 0) ? "None provided" : srActDescription)
				+ "\n"
				+ "Location :"
				+ ((srActAddress == null || srActAddress.trim().length() == 0) ? "None provided"
						: srActAddress)
				+ "\n"
				+ "Date: "
				+ ((srActDate == null) ? "None provided" : theDate)
				+ "\n"
				+ "Time: "
				+ ((srActTime == null || srActTime.trim().length() == 0) ? "None provided"
						: srActTime)
				+ "\n"
				+ "Duration: "
				+ ((srActDuration == null || srActDuration.trim().length() == 0) ? "None provided"
						: srActDuration)
				+ "\n"
				+ "Link:  "
				+ ((srActLinks == null || srActLinks.trim().length() == 0) ? "None provided"
						: srActLinks);
	}
	
	
	public ItemActivity (Cursor cursor) {
		this.setApproved(cursor.getInt(cursor.getColumnIndex(KEY_ACTIVITY_ISAPPROVED))!=0);
		this.setSrActAddress(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_SRACTADDRESS)));
		GregorianCalendar gc=new GregorianCalendar();
		try {
			gc.setTime(DbAdapter.mDateFormat.parse(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_SRACTDATE))));
		} catch (Exception e) {
			gc.setTime(new Date());
		}
		this.setSrActDate(gc);
		this.setSrActDescription(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_SRACTDESCRIPTION)));
		this.setSrActDuration(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_SRACTDURATION)));
		this.setSrActID(cursor.getInt(cursor.getColumnIndex(KEY_ACTIVITY_SRACTID)));
		this.setSrActLat(Double.parseDouble(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_SRACTLAT))));
		this.setSrActLinks(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_SRACTLINKS)));
		this.setSrActLong(Double.parseDouble(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_SRACTLONG))));
		this.setSrActName(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_SRACTNAME)));
		this.setSrActTime(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_SRACTTIME)));
		this.setSrActUrlImage(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_SRACTURLIMAGE)));
	}
	
	
	public int getSrActID() {
		return srActID;
	}

	public void setSrActID(int srActID) {
		this.srActID = srActID;
	}

	public String getSrActName() {
		return srActName;
	}

	public void setSrActName(String srActName) {
		this.srActName = srActName;
	}

	public String getSrActDescription() {
		return srActDescription;
	}

	public void setSrActDescription(String srActDescription) {
		this.srActDescription = srActDescription;
	}

	public GregorianCalendar getSrActDate() {
		return srActDate;
	}

	public void setSrActDate(GregorianCalendar srActDate) {
		this.srActDate = srActDate;
	}

	public String getSrActTime() {
		return srActTime;
	}

	public void setSrActTime(String srActTime) {
		this.srActTime = srActTime;
	}

	public String getSrActDuration() {
		return srActDuration;
	}

	public void setSrActDuration(String srActDuration) {
		this.srActDuration = srActDuration;
	}

	public String getSrActLinks() {
		return srActLinks;
	}

	public void setSrActLinks(String srActLinks) {
		this.srActLinks = srActLinks;
	}

	public String getSrActUrlImage() {
		return srActUrlImage;
	}

	public void setSrActUrlImage(String srActUrlImage) {
		this.srActUrlImage = srActUrlImage;
	}

	public String getSrActAddress() {
		return srActAddress;
	}

	public void setSrActAddress(String srActAddress) {
		this.srActAddress = srActAddress;
	}

	public double getSrActLat() {
		return srActLat;
	}

	public void setSrActLat(double srActLat) {
		this.srActLat = srActLat;
	}

	public double getSrActLong() {
		return srActLong;
	}

	public void setSrActLong(double srActLong) {
		this.srActLong = srActLong;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	@Override
	protected String getTableName() {
		return DATABASE_TABLE_ACTIVITY;
	}

	@Override
	protected String getDateLastUpdatedKey() {
		return DATE_LAST_UPDATED;
	}
	
	@Override
	protected void loadWriteItemToDatabaseContentValuesTo(ContentValues values) {
		values.put(KEY_ACTIVITY_SRACTID, this.getSrActID());
		values.put(KEY_ACTIVITY_SRACTNAME,getSrActName());
		values.put(KEY_ACTIVITY_SRACTDESCRIPTION,getSrActDescription());
		values.put(KEY_ACTIVITY_SRACTDATE, getSrActDate()==null?null: DbAdapter.mDateFormat.format(getSrActDate().getTime()));
		values.put(KEY_ACTIVITY_SRACTTIME, getSrActTime());
		values.put(KEY_ACTIVITY_SRACTDURATION, getSrActDuration() );
		values.put(KEY_ACTIVITY_SRACTLINKS, getSrActLinks());
		values.put(KEY_ACTIVITY_SRACTURLIMAGE, getSrActUrlImage());
		values.put(KEY_ACTIVITY_SRACTADDRESS, getSrActAddress());
		values.put(KEY_ACTIVITY_SRACTLAT, getSrActLat());
		values.put(KEY_ACTIVITY_SRACTLONG, getSrActLong());
		values.put(KEY_ACTIVITY_ISAPPROVED, isApproved());
	}
	
	@Override
	protected String[] getProjectionForFetch() {
		String[] projection = {
				KEY_ACTIVITY_ROWID,
				KEY_ACTIVITY_SRACTID,
				KEY_ACTIVITY_SRACTNAME,
				KEY_ACTIVITY_SRACTDESCRIPTION,
				KEY_ACTIVITY_SRACTDATE,
				KEY_ACTIVITY_SRACTTIME,
				KEY_ACTIVITY_SRACTDURATION,
				KEY_ACTIVITY_SRACTLINKS,
				KEY_ACTIVITY_SRACTURLIMAGE,
				KEY_ACTIVITY_SRACTADDRESS,
				KEY_ACTIVITY_SRACTLAT,
				KEY_ACTIVITY_SRACTLONG,
				KEY_ACTIVITY_ISAPPROVED
		};
		return projection;
	}
	@Override
	protected SunriverDataItem itemFromCursor(Cursor cursor) {
		return new ItemActivity(cursor);
	}
	@Override
	public boolean isDataExpired() {
		Date lastDateRead=getLastDateRead();
		return (
				SplashPage.TheItemUpdate==null || lastDateRead==null || SplashPage.TheItemUpdate.getUpdateActivity()==null ||
				SplashPage.TheItemUpdate.getUpdateActivity().getTime().after(lastDateRead)
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
