package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;

public abstract class SunriverDataItem implements Cacheable {

	public SunriverDataItem() {
		// TODO Auto-generated constructor stub
	}
	protected abstract String getTableName();
	protected abstract String getDateLastUpdatedKey();
	protected abstract void loadWriteItemToDatabaseContentValuesTo(ContentValues values);
	protected abstract String[] getProjectionForFetch();
	protected abstract SunriverDataItem itemFromCursor(Cursor cursor);
	protected abstract String getColumnNameForWhereClause();
	protected abstract String[] getColumnValuesForWhereClause();
	protected abstract String getGroupBy();
	protected abstract String getOrderBy();
	
	@Override
	public void forceNewFetch() {
		SharedPreferences sharedPreferences=MainActivity.getSharedPreferences();
		Editor edit=sharedPreferences.edit();
		edit.remove(getDateLastUpdatedKey());
		edit.commit();
	}
	
	@Override
	public void clearTable() {
		try {
			String sql="DELETE FROM " + getTableName();
			MainActivity.staticGetDbAdapter().exec(sql);
			} catch (Exception e) { // maybe the table doesn't exist yet
				int bkhere=3;
				int bkhere2=bkhere;
		}
	}

	@Override
	public void setLastDateReadToNow() {
		SharedPreferences sharedPreferences=MainActivity.getSharedPreferences();
		Editor edit=sharedPreferences.edit();
		edit.putString(getDateLastUpdatedKey(), DbAdapter.mDateFormat.format(new GregorianCalendar().getTime()));
		edit.commit();
	}
	
	@Override
	public long writeItemToDatabase() {
		ContentValues values=new ContentValues();
		loadWriteItemToDatabaseContentValuesTo(values);
		return MainActivity.staticGetDbAdapter().insert(getTableName(), values);
	}

	@Override
	public Date getLastDateRead() {
		SharedPreferences sharedPreferences=MainActivity.getSharedPreferences();
		String date= sharedPreferences.getString(getDateLastUpdatedKey(), null);
		if(date==null) {
			return null;
		} else {
			try {
				Date theDate=DbAdapter.mDateFormat.parse(date);
				return theDate;
			} catch (Exception e) {
				return null;
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.diamondsoftware.android.sunriver_av_3_0.Cacheable#fetchDataFromDatabase()
	 * 
	 * With services, we have two levels: the summary (first page) and the detail (chosen category).
	 * We use the Where clause in the detail page in order to only fetch items of the chosen category.
	 * We use the Group By clause in the summary page so that only 1 record of each category is chosen. 
	 */
	
	@Override
	public ArrayList<Object> fetchDataFromDatabase() {
		ArrayList<Object> array= new ArrayList<Object>();
		String[] projection=getProjectionForFetch();
		String columnNameForWhereClause=getColumnNameForWhereClause();
		String[] columnValuesForWhereClause=getColumnValuesForWhereClause();
		Cursor cursor=MainActivity.staticGetDbAdapter().get(getTableName(), projection, 
				columnNameForWhereClause, columnValuesForWhereClause, getGroupBy(), getOrderBy());
		if(cursor.moveToFirst()) {
			do  {
				array.add(this.itemFromCursor(cursor));
			} while(cursor.moveToNext());
		}
		cursor.close();
		return array;
	}
	
}
