package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

import android.content.ContentValues;
import android.database.Cursor;

import com.esri.core.geometry.Point;


public class ItemLocation extends SunriverDataItem {
    public static enum LocationType {
    	RESTAURANT, RETAIL,POOL,TENNIS_COURT,GAS_STATION,PERFECT_PICTURE_SPOT,SUNRIVER,NULL
    }
	
	public static final String DATABASE_TABLE_LOCATION = "location";
	public static final String DATE_LAST_UPDATED="date_last_updated_location";
	
	public static final String KEY_LOCATION_ROWID="_id";
	public static final String KEY_LOCATION_srMapCategory= "srMapCategory";
	public static final String KEY_LOCATION_srMapCategoryName= "srMapCategoryName";
	public static final String KEY_LOCATION_srMapName= "srMapName";
	public static final String KEY_LOCATION_srMapDescription= "srMapDescription";
	public static final String KEY_LOCATION_srMapAddress= "srMapAddress";
	public static final String KEY_LOCATION_srMapUrlImage= "srMapUrlImage";
	public static final String KEY_LOCATION_srMapLinks= "srMapLinks";
	public static final String KEY_LOCATION_srMapPhone= "srMapPhone";
	public static final String KEY_LOCATION_isGPSPopup= "isGPSPopup";
	public static final String KEY_LOCATION_srMapId= "srMapId";
	public static final String KEY_LOCATION_srMapLat= "srMapLat";
	public static final String KEY_LOCATION_srMapLong= "srMapLong";
	
	private String mName;
	private String mDescription;
	private String mAddress;
	private String mPhone;
	private Point  mCoordinates;
	private String mImageUrl;
	private String mSoundUrl;
	private String mWebUrl;
	private Point  mGoogleCoordinates;
	private boolean mIsGPSPopup;
	private int mCategory;
	private String mCategoryName;
	
	public ItemLocation() {
	}
	
	public ItemLocation (Cursor cursor) {
		this.setmIsGPSPopup(cursor.getInt(cursor.getColumnIndex(KEY_LOCATION_isGPSPopup))!=0);
		this.setmId(cursor.getInt(cursor.getColumnIndex(KEY_LOCATION_srMapId)));
		this.setmCategory(cursor.getInt(cursor.getColumnIndex(KEY_LOCATION_srMapCategory)));
		this.setmCategoryName(cursor.getString(cursor.getColumnIndex(KEY_LOCATION_srMapCategoryName)));
		this.setmName(cursor.getString(cursor.getColumnIndex(KEY_LOCATION_srMapName)));
		this.setmDescription(cursor.getString(cursor.getColumnIndex(KEY_LOCATION_srMapDescription)));
		this.setmAddress(cursor.getString(cursor.getColumnIndex(KEY_LOCATION_srMapAddress)));
		this.setmImageUrl(cursor.getString(cursor.getColumnIndex(KEY_LOCATION_srMapUrlImage)));
		this.setmWebUrl(cursor.getString(cursor.getColumnIndex(KEY_LOCATION_srMapLinks)));
		this.setmPhone(cursor.getString(cursor.getColumnIndex(KEY_LOCATION_srMapPhone)));
		Point point=new Point();
		point.setX(Double.valueOf(cursor.getString(cursor.getColumnIndex(KEY_LOCATION_srMapLong))));
		point.setY(Double.valueOf(cursor.getString(cursor.getColumnIndex(KEY_LOCATION_srMapLat))));
		this.setmCoordinates(point);
		Point googlePoint=Utils.ToGeographic(point);
		this.setmGoogleCoordinates(googlePoint);
	}
	
	
	public String getmCategoryName() {
		return mCategoryName;
	}

	public void setmCategoryName(String mCategoryName) {
		this.mCategoryName = mCategoryName;
	}

	public int getmCategory() {
		return mCategory;
	}
	public void setmCategory(int mCategory) {
		this.mCategory = mCategory;
	}

	public boolean ismIsGPSPopup() {
		return mIsGPSPopup;
	}


	
	public void setmIsGPSPopup(boolean mIsGPSPopup) {
		this.mIsGPSPopup = mIsGPSPopup;
	}
	private int mId;
	
	public int getmId() {
		return mId;
	}

	public void setmId(int mId) {
		this.mId = mId;
	}

	public String getmWebUrl() {
		return mWebUrl;
	}

	public void setmWebUrl(String mWebUrl) {
		this.mWebUrl = mWebUrl;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmDescription() {
		return mDescription;
	}

	public void setmDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	public String getmAddress() {
		return mAddress;
	}

	public void setmAddress(String mAddress) {
		this.mAddress = mAddress;
	}

	public String getmPhone() {
		return mPhone;
	}

	public void setmPhone(String mPhone) {
		this.mPhone = mPhone;
	}

	public Point getmCoordinates() {
		return mCoordinates;
	}

	public void setmCoordinates(Point mCoordinates) {
		this.mCoordinates = mCoordinates;
	}

	public String getmImageUrl() {
		return mImageUrl;
	}

	public void setmImageUrl(String mImageUrl) {
		this.mImageUrl = mImageUrl;
	}

	public String getmSoundUrl() {
		return mSoundUrl;
	}

	public void setmSoundUrl(String mSoundUrl) {
		this.mSoundUrl = mSoundUrl;
	}

	public Point getmGoogleCoordinates() {
		return mGoogleCoordinates;
	}

	public void setmGoogleCoordinates(Point mGoogleCoordinates) {
		this.mGoogleCoordinates = mGoogleCoordinates;
	}
	
	public HashMap toHashMap() {
		double latitude = getmCoordinates().getY();
		double longitude = getmCoordinates().getX();		
		
		HashMap  attributes=new HashMap();
		attributes.put("description", getmDescription());
		attributes.put("imageUrl", getmImageUrl());
		attributes.put("name", getmName());
		attributes.put("phone", getmPhone());
		attributes.put("soundUrl", getmSoundUrl());
		attributes.put("webUrl", getmWebUrl());
		attributes.put("address", getmAddress());
		attributes.put("latitude", String.valueOf(getmGoogleCoordinates().getY()));
		attributes.put("longitude", String.valueOf(getmGoogleCoordinates().getX()));
		attributes.put("id", String.valueOf(getmId()));
		attributes.put("isGPSPopup", String.valueOf(ismIsGPSPopup()));
		return attributes;
		
	}

	@Override
	protected String getTableName() {
		return DATABASE_TABLE_LOCATION;
	}

	@Override
	protected String getDateLastUpdatedKey() {
		return DATE_LAST_UPDATED;
	}

	@Override
	protected void loadWriteItemToDatabaseContentValuesTo(ContentValues values) {
		values.put(KEY_LOCATION_srMapId,this.getmId());
		values.put(KEY_LOCATION_srMapCategory,this.getmCategory());
		values.put(KEY_LOCATION_srMapCategoryName, this.getmCategoryName());
		values.put(KEY_LOCATION_srMapName,this.getmName());
		values.put(KEY_LOCATION_srMapDescription,this.getmDescription());
		values.put(KEY_LOCATION_srMapAddress,this.getmAddress());
		values.put(KEY_LOCATION_srMapUrlImage,this.getmImageUrl());
		values.put(KEY_LOCATION_srMapLinks,this.getmWebUrl());
		values.put(KEY_LOCATION_srMapPhone,this.getmPhone());
		values.put(KEY_LOCATION_isGPSPopup,this.ismIsGPSPopup());
		values.put(KEY_LOCATION_srMapId,this.getmId());
		values.put(KEY_LOCATION_srMapLat,String.valueOf(this.getmCoordinates().getY()));
		values.put(KEY_LOCATION_srMapLong,String.valueOf(this.getmCoordinates().getX()));
	}

	@Override
	protected String[] getProjectionForFetch() {
		String[] projection = {
				KEY_LOCATION_ROWID,
				KEY_LOCATION_srMapId,
				KEY_LOCATION_srMapCategory,
				KEY_LOCATION_srMapCategoryName,
				KEY_LOCATION_srMapName,
				KEY_LOCATION_srMapDescription,
				KEY_LOCATION_srMapAddress,
				KEY_LOCATION_srMapUrlImage,
				KEY_LOCATION_srMapLinks,
				KEY_LOCATION_srMapPhone,
				KEY_LOCATION_isGPSPopup,
				KEY_LOCATION_srMapId,
				KEY_LOCATION_srMapLat,
				KEY_LOCATION_srMapLong
		};
		return projection;
	}
	@Override
	public ArrayList<Object> fetchDataFromDatabase() {
		ArrayList<Object> rawData=super.fetchDataFromDatabase();  /* this is just the rows of raw data.  
			Now we have to put it into categories ... just like ParsesXMLMapLocations does. */ 
		ArrayList<Object> items = new ArrayList<Object>();;
		Hashtable<LocationType,ArrayList<Object>> restaurants= new Hashtable<LocationType,ArrayList<Object>>();
		Hashtable<LocationType,ArrayList<Object>> retails= new Hashtable<LocationType,ArrayList<Object>>();
		Hashtable<LocationType,ArrayList<Object>> pools= new Hashtable<LocationType,ArrayList<Object>>();
		Hashtable<LocationType,ArrayList<Object>> tennisCourts= new Hashtable<LocationType,ArrayList<Object>>();
		Hashtable<LocationType,ArrayList<Object>> gasStations= new Hashtable<LocationType,ArrayList<Object>>();
		Hashtable<LocationType,ArrayList<Object>> perfectPictureSpots= new Hashtable<LocationType,ArrayList<Object>>();
	    restaurants.put(LocationType.RESTAURANT, new ArrayList<Object>());
		retails.put(LocationType.RETAIL, new ArrayList<Object>());
		pools.put(LocationType.POOL, new ArrayList<Object>());
		tennisCourts.put(LocationType.TENNIS_COURT, new ArrayList<Object>());
		gasStations.put(LocationType.GAS_STATION, new ArrayList<Object>());
		perfectPictureSpots.put(LocationType.PERFECT_PICTURE_SPOT, new ArrayList<Object>());
		items.add(restaurants);
		items.add(retails);
		items.add(pools);
		items.add(tennisCourts);
		items.add(gasStations);
		items.add(perfectPictureSpots);
		
		for (Object obj : rawData) {
        	int mapCategory=((ItemLocation)obj).getmCategory();
        	switch (mapCategory) {
        	case 1:
        		restaurants.get(LocationType.RESTAURANT).add(obj);
        		break;	                    		
        	case 2:
        		tennisCourts.get(LocationType.TENNIS_COURT).add(obj);
        		break;
        	case 3:
        		retails.get(LocationType.RETAIL).add(obj);
        		break;
        	case 4:
        		pools.get(LocationType.POOL).add(obj);
        		break;
        	case 5:
        		gasStations.get(LocationType.GAS_STATION).add(obj);
        		break;
        	case 6:
        		perfectPictureSpots.get(LocationType.PERFECT_PICTURE_SPOT).add(obj);
        		break;
        	default: 
        		break;	                    			                    		
        	}	                    	
		}
		return items;
	}
	@Override
	protected SunriverDataItem itemFromCursor(Cursor cursor) {
		return new ItemLocation(cursor);
	}

	@Override
	public boolean isDataExpired() {
		Date lastDateRead=getLastDateRead();
		return (
				SplashPage.TheItemUpdate==null || lastDateRead==null || SplashPage.TheItemUpdate.getUpdateMaps()==null ||
				SplashPage.TheItemUpdate.getUpdateMaps().getTime().after(lastDateRead));
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
