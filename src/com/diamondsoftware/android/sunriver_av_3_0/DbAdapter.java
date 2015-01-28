package com.diamondsoftware.android.sunriver_av_3_0;	

import java.text.DateFormat;
import java.text.SimpleDateFormat;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper class.
 * Provides methods for fetching data from the database, updating the database when the
 * database has changed.
 * 
 * Note that the onUpgrade method is called automatically by Android whenever the 
 * DATABASE_VERSION has changed.
 * 
 * @author Diamond
 *
 */
public class DbAdapter {
	private static final int DATABASE_VERSION = 26;
	public static enum FavoriteItemType {
		Activity,
		Calendar,
		Hospitality,
		Service,
		EatAndTreat,
		Retail,
		Unknown
	}
	public static final String DATABASE_TABLE_FAVORITES = "Favorites";
	public static final String KEY_FAVORITES_ROWID = "_id";
	public static final String KEY_FAVORITES_ITEM_TYPE = "ItemType";
	public static final String KEY_FAVORITES_ITEM_ID = "ItemId";

	public static final DateFormat mDateFormat = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss.S");


	private final Context mContext;
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	
	/* Public interface ---------------------------------------------------------------------- */
	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param activity
	 *            the Activity within which to work
	 */
	public DbAdapter(Context context) {
		this.mContext = context;
	}
	
	public void close() {
		mDbHelper.close();
		mDbHelper = null;
		mDb = null;
	}
	
	public long insert(String tableName,ContentValues values) {
		return getSqlDb().insert(
				tableName,
		         null,
		         values);
	}
	
	public synchronized boolean areThereAnyFavorites() {
		boolean retValue=false;
		Cursor cursor=get(DATABASE_TABLE_FAVORITES,null,null,null,null,null);
		if(cursor.getCount()>0) {
			retValue=true;
		}
		cursor.close();
		return retValue;
	}
	public synchronized boolean areThereAnyFavoritesForThisCategory(FavoriteItemType category) {
		boolean retValue=false;
		Cursor cursor=getItemsInFavoritesForThisCategory(category);
		if(cursor.getCount()>0) {
			retValue=true;
		}
		cursor.close();
		return retValue;
	}
	
	public synchronized Cursor getItemsInFavoritesForThisCategory(FavoriteItemType category) {
		return get(DATABASE_TABLE_FAVORITES,new String[]{KEY_FAVORITES_ITEM_ID},KEY_FAVORITES_ITEM_TYPE,new String[]{String.valueOf(category.ordinal())},null,null);		
	}
	
	public synchronized int delete (String table, String whereClause, String[] whereArgs) {
		int i=getSqlDb().delete(table, whereClause, whereArgs);
		return i;
	}
	
	public synchronized Cursor get(String tableName, String[] projection, 
			String columnNameForWhereClause, String[] columnValuesForWhereClause, String groupBy, String sortBy) {
		String whereClause=columnNameForWhereClause==null?null:(columnNameForWhereClause+"=?");
		Cursor cu = getSqlDb().query(
			tableName,				  				// The table to query
		    projection,                             // The columns to return
		    whereClause,                           	// The columns for the WHERE clause
		    columnValuesForWhereClause,             // The values for the WHERE clause
		    groupBy,                                // group the rows
		    null,                                   // don't filter by row groups
		    sortBy	                                // The sort order
		    );
		return cu;
	}
	public synchronized void exec(String sql) {
		getSqlDb().execSQL(sql);
	}

	public static String doubleApostophize(String str) 
	{
		if(str==null) {
			return null;
		}
		return str.replaceAll("'", "''");
	}
		
	private static class DatabaseHelper extends SQLiteOpenHelper {
		/**
		 * Database creation sql statement 
		 */
		private static final String CREATE_TABLE_FAVORITES = "create table " + DATABASE_TABLE_FAVORITES + " (" +
				KEY_FAVORITES_ROWID + " integer primary key autoincrement," +
				KEY_FAVORITES_ITEM_TYPE + " integer, " +
				KEY_FAVORITES_ITEM_ID + " integer);";
				
		private static final String CREATE_TABLE_DIDYOUKNOW = "create table " + ItemDidYouKnow.DATABASE_TABLE_DIDYOUKNOW + " ("+
				ItemDidYouKnow.KEY_DIDYOUKNOW_ROWID + " integer primary key autoincrement," +
				ItemDidYouKnow.KEY_DIDYOUKNOW_DIDYOUKNOWID + " integer, " +
				ItemDidYouKnow.KEY_DIDYOUKNOW_DIDYOUKNOWURL + " string);";
		private static final String CREATE_TABLE_WELCOME = "create table " + ItemWelcome.DATABASE_TABLE_WELCOME +" (" +
				ItemWelcome.KEY_WELCOME_ROWID + " integer primary key autoincrement," +
				ItemWelcome.KEY_WELCOME_WELCOMEID + " integer," +
				ItemWelcome.KEY_WELCOME_WELCOMEURL + " string ); ";
		private static final String CREATE_TABLE_GISLAYERS = "create table " + ItemGISLayers.DATABASE_TABLE_GISLAYERS + " (" +
				ItemGISLayers.KEY_GISLAYERS_ROWID + " integer primary key autoincrement," +
				ItemGISLayers.KEY_GISLAYERS_ISBIKEPATHS + " bit," +
				ItemGISLayers.KEY_GISLAYERS_ID + " integer,"+
				ItemGISLayers.KEY_GISLAYERS_URL + " string,"+
				ItemGISLayers.KEY_GISLAYERS_USENUM + " integer );";
		private static final String CREATE_TABLE_ACTIVITY = "create table " + ItemActivity.DATABASE_TABLE_ACTIVITY + " (" +
				ItemActivity.KEY_ACTIVITY_ROWID + " integer primary key autoincrement," +
				ItemActivity.KEY_ACTIVITY_SRACTID + " integer," +
				ItemActivity.KEY_ACTIVITY_SRACTNAME + " string," +
				ItemActivity.KEY_ACTIVITY_SRACTDESCRIPTION + " string," +
				ItemActivity.KEY_ACTIVITY_SRACTDATE + " datetime," +
				ItemActivity.KEY_ACTIVITY_SRACTTIME + " string," +
				ItemActivity.KEY_ACTIVITY_SRACTDURATION + " string," +
				ItemActivity.KEY_ACTIVITY_SRACTLINKS + " string," +
				ItemActivity.KEY_ACTIVITY_SRACTURLIMAGE + " string," +
				ItemActivity.KEY_ACTIVITY_SRACTADDRESS + " string," +
				ItemActivity.KEY_ACTIVITY_SRACTLAT + " string," +
				ItemActivity.KEY_ACTIVITY_SRACTLONG + " string," +
				ItemActivity.KEY_ACTIVITY_ISAPPROVED + " bit ); ";
		private static final String CREATE_TABLE_HOSPITALITY = "create table " + ItemHospitality.DATABASE_TABLE_HOSPITALITY + " (" +
				ItemHospitality.KEY_HOSPITALITY_ROWID + " integer primary key autoincrement,"+
				ItemHospitality.KEY_HOSPITALITY_ID + " integer, " +
				ItemHospitality.KEY_HOSPITALITY_ADDRESS + " string, "+
				ItemHospitality.KEY_HOSPITALITY_DESCRIPTION + " string, "+
				ItemHospitality.KEY_HOSPITALITY_LAT + " string, "+
				ItemHospitality.KEY_HOSPITALITY_LONG + " string, " +
				ItemHospitality.KEY_HOSPITALITY_NAME + " string, " +
				ItemHospitality.KEY_HOSPITALITY_PHONE + " string, " +
				ItemHospitality.KEY_HOSPITALITY_URLIMAGE + " string, " +
				ItemHospitality.KEY_HOSPITALITY_URLWEBSITE + " string );";
		private static final String CREATE_TABLE_CALENDAR = "create table " + ItemCalendar.DATABASE_TABLE_CALENDAR + " (" +
				ItemCalendar.KEY_CALENDAR_ROWID + " integer primary key autoincrement," +
				ItemCalendar.KEY_CALENDAR_SRACTID + " integer," +
				ItemCalendar.KEY_CALENDAR_SRACTNAME + " string," +
				ItemCalendar.KEY_CALENDAR_SRACTDESCRIPTION + " string," +
				ItemCalendar.KEY_CALENDAR_SRACTDATE + " datetime," +
				ItemCalendar.KEY_CALENDAR_SRACTTIME + " string," +
				ItemCalendar.KEY_CALENDAR_SRACTDURATION + " string," +
				ItemCalendar.KEY_CALENDAR_SRACTLINKS + " string," +
				ItemCalendar.KEY_CALENDAR_SRACTURLIMAGE + " string," +
				ItemCalendar.KEY_CALENDAR_SRACTADDRESS + " string," +
				ItemCalendar.KEY_CALENDAR_SRACTLAT + " string," +
				ItemCalendar.KEY_CALENDAR_SRACTLONG + " string," +
				ItemCalendar.KEY_CALENDAR_ISAPPROVED + " bit ); ";
		private static final String CREATE_TABLE_LOCATION = "create table " + ItemLocation.DATABASE_TABLE_LOCATION + " (" +
				ItemLocation.KEY_LOCATION_ROWID + " integer primary key autoincrement," +
				ItemLocation.KEY_LOCATION_srMapId + " integer," +
				ItemLocation.KEY_LOCATION_srMapCategory + " int," +
				ItemLocation.KEY_LOCATION_srMapCategoryName + " string," +
				ItemLocation.KEY_LOCATION_srMapName + " string," +
				ItemLocation.KEY_LOCATION_srMapDescription + " string," +
				ItemLocation.KEY_LOCATION_srMapAddress + " string," +
				ItemLocation.KEY_LOCATION_srMapUrlImage + " string," +
				ItemLocation.KEY_LOCATION_srMapLinks + " string," +
				ItemLocation.KEY_LOCATION_srMapPhone + " string," +
				ItemLocation.KEY_LOCATION_isGPSPopup + " bit," +
				ItemLocation.KEY_LOCATION_srMapLat + " string," +
				ItemLocation.KEY_LOCATION_srMapLong + " string ); ";
		private static final String CREATE_TABLE_SERVICE = "create table " + ItemService.DATABASE_TABLE_SERVICE + " (" +
				ItemService.KEY_SERVICE_ROWID + " integer primary key autoincrement," +
				ItemService.KEY_SERVICE_SERVICEID + " integer," +
				ItemService.KEY_SERVICE_SERVICENAME + " string," +
				ItemService.KEY_SERVICE_SERVICEWEBURL+ " string," +
				ItemService.KEY_SERVICE_SERVICEPICTUREURL + " string," +
				ItemService.KEY_SERVICE_SERVICEICONURL + " string," +
				ItemService.KEY_SERVICE_SERVICEDESCRIPTION + " string," +
				ItemService.KEY_SERVICE_SERVICEPHONE + " string," +
				ItemService.KEY_SERVICE_SERVICEADDRESS + " string," +
				ItemService.KEY_SERVICE_SERVICELAT + " string," +
				ItemService.KEY_SERVICE_SERVICELONG + " string," +
				ItemService.KEY_SERVICE_SERVICECATEGORYNAME + " string," +
				ItemService.KEY_SERVICE_SERVICECATEGORYICONURL + " string," +
				ItemService.KEY_SERVICE_SORTORDER + " integer);";
		private static final String CREATE_TABLE_SELFIE = "create table " + ItemSelfie.DATABASE_TABLE_SELFIE + " (" +
				ItemSelfie.KEY_SELFIE_ROWID + " integer primary key autoincrement,"+
				ItemSelfie.KEY_SELFIE_OVERLAYID + " integer," +
				ItemSelfie.KEY_SELFIE_OVERLAYLSSELECTURL + " string," +
				ItemSelfie.KEY_SELFIE_OVERLAYLSURL + " string, " +
				ItemSelfie.KEY_SELFIE_OVERLAYPORTCAMURL + " string, " +
				ItemSelfie.KEY_SELFIE_OVERLAYPORTURL + " string );";
		private static final String CREATE_TABLE_ALLHOMES = "create table " + ItemAllHomes.DATABASE_TABLE_ALLHOMES + " (" +
				ItemAllHomes.KEY_ALLHOMES_ROWID + " integer primary key autoincrement," +
				ItemAllHomes.KEY_ALLHOMES_SUNRIVER_ADDRESS + " string collate nocase," +
				ItemAllHomes.KEY_ALLHOMES_COUNTY_ADDRESS+ " string );";

		private static final String DATABASE_NAME = "data";

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(CREATE_TABLE_ACTIVITY);
			} catch (Exception eieio33) {}
			try {
				db.execSQL(CREATE_TABLE_CALENDAR);
			} catch (Exception eieio33) {}
			try {
				db.execSQL(CREATE_TABLE_LOCATION);
			} catch (Exception eieio33) {}
			try {
				db.execSQL(CREATE_TABLE_SERVICE);
			} catch (Exception eieio33) {}
			try {
				db.execSQL(CREATE_TABLE_WELCOME);
			} catch (Exception eieio33) {}
			try {
				db.execSQL(CREATE_TABLE_DIDYOUKNOW);
			} catch (Exception eieieo33) {}
			try {
				db.execSQL(CREATE_TABLE_ALLHOMES);
			} catch (Exception eieio){}
			try {
				db.execSQL(CREATE_TABLE_SELFIE);
			} catch (Exception eieio) {
			}
			try {
				db.execSQL(CREATE_TABLE_HOSPITALITY);
			} catch (Exception eieio) {}
			try {
				db.execSQL(CREATE_TABLE_GISLAYERS);
			} catch (Exception eieio) {}
			try {
				db.execSQL(CREATE_TABLE_FAVORITES);
			} catch (Exception eieio) {};
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			if(newVersion==13 || newVersion==19) {
				db.execSQL("DROP TABLE IF EXISTS " + ItemService.DATABASE_TABLE_SERVICE);
				/* I also need to clear the "last saved date" persisted field. */
				new ItemService().forceNewFetch();
			} else {
				if(newVersion==18) {
					db.execSQL("DROP INDEX IF EXISTS " + "allhomes_index");
					db.execSQL("DROP TABLE IF EXISTS "+ItemAllHomes.DATABASE_TABLE_ALLHOMES);
					new ItemAllHomes().forceNewFetch();
				} else {
					if(newVersion>=20) {
						db.execSQL("DROP INDEX IF EXISTS " + "allhomes_index");
						db.execSQL("DROP TABLE IF EXISTS "+ItemAllHomes.DATABASE_TABLE_ALLHOMES);
					} else {
						if(newVersion==24) {
							db.execSQL("DROP TABLE IF EXISTS "+ItemHospitality.DATABASE_TABLE_HOSPITALITY);
						}
					}
				}
			}
			onCreate(db);
		}

		@Override
		public void onOpen(SQLiteDatabase db) {

		}

	
	}
	private SQLiteDatabase getSqlDb() {
		if (mDb == null) {
			if (mDbHelper == null) {
				mDbHelper = new DatabaseHelper(mContext);
			}
			mDb = mDbHelper.getWritableDatabase();
		}
		return mDb;
	}
}
