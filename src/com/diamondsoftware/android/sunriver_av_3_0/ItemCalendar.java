package com.diamondsoftware.android.sunriver_av_3_0;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;

public class ItemCalendar extends SunriverDataItem {
	public static final String DATABASE_TABLE_CALENDAR = "calendar";
	public static final String DATE_LAST_UPDATED = "date_last_updated_calendar";

	public static final String KEY_CALENDAR_ROWID = "_id";
	public static final String KEY_CALENDAR_SRACTID = "srCalID";
	public static final String KEY_CALENDAR_SRACTNAME = "srCalName";
	public static final String KEY_CALENDAR_SRACTDESCRIPTION = "srCalDescription";
	public static final String KEY_CALENDAR_SRACTDATE = "srCalDate";
	public static final String KEY_CALENDAR_SRACTTIME = "srCalTime";
	public static final String KEY_CALENDAR_SRACTDURATION = "srCalDuration";
	public static final String KEY_CALENDAR_SRACTLINKS = "srCalLinks";
	public static final String KEY_CALENDAR_SRACTURLIMAGE = "srCalUrlImage";
	public static final String KEY_CALENDAR_SRACTLAT = "srCalLat";
	public static final String KEY_CALENDAR_SRACTLONG = "srCalLong";
	public static final String KEY_CALENDAR_ISAPPROVED = "isApproved";
	public static final String KEY_CALENDAR_SRACTADDRESS = "srCalAddress";

	private int srCalId;
	private String srCalName;
	private String srCalDescription;
	private GregorianCalendar srCalDate;
	private String srCalTime;
	private String srCalAddress;
	private String srCalDuration;
	private String srCalLinks;
	private String srCalUrlImage;
	private double srCalLat;
	private double srCalLong;
	private boolean isApproved;
	private SimpleDateFormat simpleFormatter;

	@Override
	public String toString() {
		String theDate = null;
		if (srCalDate != null) {
			simpleFormatter = new SimpleDateFormat("EEEE, MMMM d, yyyy",
					Locale.getDefault());
			simpleFormatter.setCalendar(srCalDate);
			Date dt = srCalDate.getTime();
			theDate = simpleFormatter.format(dt);
		}
		return "Name: "
				+ ((srCalName == null || srCalName.trim().length() == 0) ? "None provided"
						: srCalName)
				+ "\n"
				+ "Description: "
				+ ((srCalDescription == null || srCalDescription.trim()
						.length() == 0) ? "None provided" : srCalDescription)
				+ "\n"
				+ "Location :"
				+ ((srCalAddress == null || srCalAddress.trim().length() == 0) ? "None provided"
						: srCalAddress)
				+ "\n"
				+ "Date: "
				+ ((srCalDate == null) ? "None provided" : theDate)
				+ "\n"
				+ "Time: "
				+ ((srCalTime == null || srCalTime.trim().length() == 0) ? "None provided"
						: srCalTime)
				+ "\n"
				+ "Duration: "
				+ ((srCalDuration == null || srCalDuration.trim().length() == 0) ? "None provided"
						: srCalDuration)
				+ "\n"
				+ "Link:  "
				+ ((srCalLinks == null || srCalLinks.trim().length() == 0) ? "None provided"
						: srCalLinks);
	}

	public ItemCalendar() {
	}

	public ItemCalendar(Cursor cursor) {
		this.setSrCalId(cursor.getInt(cursor
				.getColumnIndex(KEY_CALENDAR_SRACTID)));
		this.setSrCalName(cursor.getString(cursor
				.getColumnIndex(KEY_CALENDAR_SRACTNAME)));
		this.setSrCalDescription(cursor.getString(cursor
				.getColumnIndex(KEY_CALENDAR_SRACTDESCRIPTION)));
		GregorianCalendar gc = new GregorianCalendar();
		try {
			gc.setTime(DbAdapter.mDateFormat.parse(cursor.getString(cursor
					.getColumnIndex(KEY_CALENDAR_SRACTDATE))));
		} catch (Exception e) {
			gc.setTime(new Date());
		}
		this.setSrCalDate(gc);
		this.setSrCalTime(cursor.getString(cursor
				.getColumnIndex(KEY_CALENDAR_SRACTTIME)));
		this.setSrCalDuration(cursor.getString(cursor
				.getColumnIndex(KEY_CALENDAR_SRACTDURATION)));
		this.setSrCalLinks(cursor.getString(cursor
				.getColumnIndex(KEY_CALENDAR_SRACTLINKS)));
		this.setSrCalUrlImage(cursor.getString(cursor
				.getColumnIndex(KEY_CALENDAR_SRACTURLIMAGE)));
		this.setSrCalLat(Double.parseDouble(cursor.getString(cursor
				.getColumnIndex(KEY_CALENDAR_SRACTLAT))));
		this.setSrCalLong(Double.parseDouble(cursor.getString(cursor
				.getColumnIndex(KEY_CALENDAR_SRACTLONG))));
		this.setApproved(cursor.getInt(cursor
				.getColumnIndex(KEY_CALENDAR_ISAPPROVED)) != 0);
		this.setSrCalAddress(cursor.getString(cursor
				.getColumnIndex(KEY_CALENDAR_SRACTADDRESS)));
	}

	public int getSrCalId() {
		return srCalId;
	}

	public void setSrCalId(int srCalId) {
		this.srCalId = srCalId;
	}

	public String getSrCalName() {
		return srCalName;
	}

	public void setSrCalName(String srCallName) {
		this.srCalName = srCallName;
	}

	public String getSrCalDescription() {
		return srCalDescription;
	}

	public void setSrCalDescription(String srCallDescription) {
		this.srCalDescription = srCallDescription;
	}

	public GregorianCalendar getSrCalDate() {
		return srCalDate;
	}

	public void setSrCalDate(GregorianCalendar srCalDate) {
		this.srCalDate = srCalDate;
	}

	public String getSrCalTime() {
		return srCalTime;
	}

	public void setSrCalTime(String srCalTime) {
		this.srCalTime = srCalTime;
	}

	public String getSrCalDuration() {
		return srCalDuration;
	}

	public void setSrCalDuration(String srCalDuration) {
		this.srCalDuration = srCalDuration;
	}

	public String getSrCalLinks() {
		return srCalLinks;
	}

	public void setSrCalLinks(String srCalLinks) {
		this.srCalLinks = srCalLinks;
	}

	public String getSrCalUrlImage() {
		return srCalUrlImage;
	}

	public void setSrCalUrlImage(String srCalUrlImage) {
		this.srCalUrlImage = srCalUrlImage;
	}

	public double getSrCalLat() {
		return srCalLat;
	}

	public void setSrCalLat(double srCalLat) {
		this.srCalLat = srCalLat;
	}

	public double getSrCalLong() {
		return srCalLong;
	}

	public void setSrCalLong(double srCalLong) {
		this.srCalLong = srCalLong;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public String getSrCalAddress() {
		return srCalAddress;
	}

	public void setSrCalAddress(String srCalAddress) {
		this.srCalAddress = srCalAddress;
	}

	@Override
	protected String getTableName() {
		return DATABASE_TABLE_CALENDAR;
	}

	@Override
	protected String getDateLastUpdatedKey() {
		return DATE_LAST_UPDATED;
	}

	@Override
	protected void loadWriteItemToDatabaseContentValuesTo(ContentValues values) {
		values.put(KEY_CALENDAR_SRACTID, this.getSrCalId());
		values.put(KEY_CALENDAR_SRACTNAME, getSrCalName());
		values.put(KEY_CALENDAR_SRACTDESCRIPTION, getSrCalDescription());
		values.put(KEY_CALENDAR_SRACTDATE,
				DbAdapter.mDateFormat.format(getSrCalDate().getTime()));
		values.put(KEY_CALENDAR_SRACTTIME, getSrCalTime());
		values.put(KEY_CALENDAR_SRACTDURATION, getSrCalDuration());
		values.put(KEY_CALENDAR_SRACTLINKS, getSrCalLinks());
		values.put(KEY_CALENDAR_SRACTURLIMAGE, getSrCalUrlImage());
		values.put(KEY_CALENDAR_SRACTADDRESS, getSrCalAddress());
		values.put(KEY_CALENDAR_SRACTLAT, getSrCalLat());
		values.put(KEY_CALENDAR_SRACTLONG, getSrCalLong());
		values.put(KEY_CALENDAR_ISAPPROVED, isApproved());
	}

	@Override
	protected String[] getProjectionForFetch() {
		String[] projection = { KEY_CALENDAR_ROWID, KEY_CALENDAR_SRACTID,
				KEY_CALENDAR_SRACTNAME, KEY_CALENDAR_SRACTDESCRIPTION,
				KEY_CALENDAR_SRACTDATE, KEY_CALENDAR_SRACTTIME,
				KEY_CALENDAR_SRACTDURATION, KEY_CALENDAR_SRACTLINKS,
				KEY_CALENDAR_SRACTURLIMAGE, KEY_CALENDAR_SRACTADDRESS,
				KEY_CALENDAR_SRACTLAT, KEY_CALENDAR_SRACTLONG,
				KEY_CALENDAR_ISAPPROVED };
		return projection;

	}

	@Override
	protected SunriverDataItem itemFromCursor(Cursor cursor) {
		return new ItemCalendar(cursor);
	}

	@Override
	public boolean isDataExpired() {
		if(GlobalState.TheItemUpdate==null) {
			return true;
		}
		Date dateDatabaseAtSunriverLastUpdated=GlobalState.TheItemUpdate.getUpdateCalendar().getTime();
		Date lastTimeWeveFetchedData=getLastDateRead();
		return (
				GlobalState.TheItemUpdate==null || lastTimeWeveFetchedData==null || GlobalState.TheItemUpdate.getUpdateCalendar()==null ||
						dateDatabaseAtSunriverLastUpdated.after(lastTimeWeveFetchedData)
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
