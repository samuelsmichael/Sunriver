package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import android.content.ContentValues;
import android.database.Cursor;

public class ItemPromotedEvent extends SunriverDataItem {

	private int promotedEventsID;
	private String promotedEventsName;
	private Boolean isOnPromotedEvents;
	private String promotedEventPictureURL;
	private int promotedCatID;
	private String promotedCatName;
	private int promotedCatSortOrder;
	private String promotedCatURLForIconImage;
	private int promotedEventsDetailsID;
	private String promotedEventsDetailsTitle;
	private String promotedEventsDetailsDescription;
	private String promotedEventsDetailsURLDocDownload;
	private String promotedEventsDetailsAddress;
	private String promotedEventsDetailsTelephone;
	private String promotedEventsDetailsWebsite;
	/**
	 * @return the promotedEventDetailOrder
	 */
	public int getPromotedEventDetailOrder() {
		return promotedEventDetailOrder;
	}

	/**
	 * @param promotedEventDetailOrder the promotedEventDetailOrder to set
	 */
	public void setPromotedEventDetailOrder(int promotedEventDetailOrder) {
		this.promotedEventDetailOrder = promotedEventDetailOrder;
	}

	/**
	 * @return the promotedEventDetailIconURL
	 */
	public String getPromotedEventDetailIconURL() {
		return promotedEventDetailIconURL;
	}

	/**
	 * @param promotedEventDetailIconURL the promotedEventDetailIconURL to set
	 */
	public void setPromotedEventDetailIconURL(String promotedEventDetailIconURL) {
		this.promotedEventDetailIconURL = promotedEventDetailIconURL;
	}

	/**
	 * @return the promotedEventIconURL
	 */
	public String getPromotedEventIconURL() {
		return promotedEventIconURL;
	}

	/**
	 * @param promotedEventIconURL the promotedEventIconURL to set
	 */
	public void setPromotedEventIconURL(String promotedEventIconURL) {
		this.promotedEventIconURL = promotedEventIconURL;
	}

	private int promotedEventDetailOrder; 
	private String promotedEventDetailIconURL;
	private String promotedEventIconURL;
	
	/**
	 * @return the promotedEventsDetailsID
	 */
	public int getPromotedEventsDetailsID() {
		return promotedEventsDetailsID;
	}

	/**
	 * @param promotedEventsDetailsID the promotedEventsDetailsID to set
	 */
	public void setPromotedEventsDetailsID(int promotedEventsDetailsID) {
		this.promotedEventsDetailsID = promotedEventsDetailsID;
	}

	/**
	 * @return the promotedEventsDetailsTitle
	 */
	public String getPromotedEventsDetailsTitle() {
		return promotedEventsDetailsTitle;
	}

	/**
	 * @param promotedEventsDetailsTitle the promotedEventsDetailsTitle to set
	 */
	public void setPromotedEventsDetailsTitle(String promotedEventsDetailsTitle) {
		this.promotedEventsDetailsTitle = promotedEventsDetailsTitle;
	}

	/**
	 * @return the promotedEventsDetailsDescription
	 */
	public String getPromotedEventsDetailsDescription() {
		return promotedEventsDetailsDescription;
	}

	/**
	 * @param promotedEventsDetailsDescription the promotedEventsDetailsDescription to set
	 */
	public void setPromotedEventsDetailsDescription(
			String promotedEventsDetailsDescription) {
		this.promotedEventsDetailsDescription = promotedEventsDetailsDescription;
	}

	/**
	 * @return the promotedEventsDetailsURLDocDownload
	 */
	public String getPromotedEventsDetailsURLDocDownload() {
		return promotedEventsDetailsURLDocDownload;
	}

	/**
	 * @param promotedEventsDetailsURLDocDownload the promotedEventsDetailsURLDocDownload to set
	 */
	public void setPromotedEventsDetailsURLDocDownload(
			String promotedEventsDetailsURLDocDownload) {
		this.promotedEventsDetailsURLDocDownload = promotedEventsDetailsURLDocDownload;
	}

	/**
	 * @return the promotedEventsDetailsAddress
	 */
	public String getPromotedEventsDetailsAddress() {
		return promotedEventsDetailsAddress;
	}

	/**
	 * @param promotedEventsDetailsAddress the promotedEventsDetailsAddress to set
	 */
	public void setPromotedEventsDetailsAddress(String promotedEventsDetailsAddress) {
		this.promotedEventsDetailsAddress = promotedEventsDetailsAddress;
	}

	/**
	 * @return the promotedEventsDetailsTelephone
	 */
	public String getPromotedEventsDetailsTelephone() {
		return promotedEventsDetailsTelephone;
	}

	/**
	 * @param promotedEventsDetailsTelephone the promotedEventsDetailsTelephone to set
	 */
	public void setPromotedEventsDetailsTelephone(
			String promotedEventsDetailsTelephone) {
		this.promotedEventsDetailsTelephone = promotedEventsDetailsTelephone;
	}

	/**
	 * @return the promotedEventsDetailsWebsite
	 */
	public String getPromotedEventsDetailsWebsite() {
		return promotedEventsDetailsWebsite;
	}

	/**
	 * @param promotedEventsDetailsWebsite the promotedEventsDetailsWebsite to set
	 */
	public void setPromotedEventsDetailsWebsite(String promotedEventsDetailsWebsite) {
		this.promotedEventsDetailsWebsite = promotedEventsDetailsWebsite;
	}
	
	
	/**
	 * @return the promotedCatID
	 */
	public int getPromotedCatID() {
		return promotedCatID;
	}

	/**
	 * @param promotedCatID the promotedCatID to set
	 */
	public void setPromotedCatID(int promotedCatID) {
		this.promotedCatID = promotedCatID;
	}

	/**
	 * @return the promotedCatName
	 */
	public String getPromotedCatName() {
		return promotedCatName;
	}

	/**
	 * @param promotedCatName the promotedCatName to set
	 */
	public void setPromotedCatName(String promotedCatName) {
		this.promotedCatName = promotedCatName;
	}

	/**
	 * @return the promotedCatSortOrder
	 */
	public int getPromotedCatSortOrder() {
		return promotedCatSortOrder;
	}

	/**
	 * @param promotedCatSortOrder the promotedCatSortOrder to set
	 */
	public void setPromotedCatSortOrder(int promotedCatSortOrder) {
		this.promotedCatSortOrder = promotedCatSortOrder;
	}

	/**
	 * @return the promotedCatURLForIconImage
	 */
	public String getPromotedCatURLForIconImage() {
		return promotedCatURLForIconImage;
	}

	/**
	 * @param promotedCatURLForIconImage the promotedCatURLForIconImage to set
	 */
	public void setPromotedCatURLForIconImage(String promotedCatURLForIconImage) {
		this.promotedCatURLForIconImage = promotedCatURLForIconImage;
	}
	
	/**
	 * @return the promotedEventsID
	 */
	public int getPromotedEventsID() {
		return promotedEventsID;
	}

	/**
	 * @param promotedEventsID the promotedEventsID to set
	 */
	public void setPromotedEventsID(int promotedEventsID) {
		this.promotedEventsID = promotedEventsID;
	}

	/**
	 * @return the promotedEventsName
	 */
	public String getPromotedEventsName() {
		return promotedEventsName;
	}

	/**
	 * @param promotedEventsName the promotedEventsName to set
	 */
	public void setPromotedEventsName(String promotedEventsName) {
		this.promotedEventsName = promotedEventsName;
	}

	/**
	 * @return the isOnPromotedEvents
	 */
	public Boolean getIsOnPromotedEvents() {
		return isOnPromotedEvents;
	}

	/**
	 * @param isOnPromotedEvents the isOnPromotedEvents to set
	 */
	public void setIsOnPromotedEvents(Boolean isOnPromotedEvents) {
		this.isOnPromotedEvents = isOnPromotedEvents;
	}

	/**
	 * @return the promotedEventPictureURL
	 */
	public String getPromotedEventPictureURL() {
		return promotedEventPictureURL;
	}

	/**
	 * @param promotedEventPictureURL the promotedEventPictureURL to set
	 */
	public void setPromotedEventPictureURL(String promotedEventPictureURL) {
		this.promotedEventPictureURL = promotedEventPictureURL;
	}

	public static final String DATABASE_TABLE_PROMOTEDEVENT="PromotedEvent";
	public static final String DATE_LAST_UPDATED = "date_last_updated_promotedevents";
	
	public static final String KEY_PROMOTEDEVENT_ID="_id";

	/* From table SRPromotedEvents */
	public static final String KEY_PROMOTEDEVENT_PROMOTEDEVENTSID="promotedEventsID";
	public static final String KEY_PROMOTEDEVENT_PROMOTEDEVENTSNAME="promotedEventsName";
	public static final String KEY_PROMOTEDEVENT_ISONPROMOTEDEVENTS="isOnPromotedEvents";
	public static final String KEY_PROMOTEDEVENT_PROMOTEDEVENTPICTUREURL="promotedEventPictureURL";
	
	/* From table SRPromotedCategory */
	public static final String KEY_PROMOTEDEVENT_PROMOTEDCATID="promotedCatID";
	public static final String KEY_PROMOTEDEVENT_PROMOTEDCATNAME="promotedCatName";
	public static final String KEY_PROMOTEDEVENT_PROMOTEDCATSORTORDER="promotedCatSortOrder";
	public static final String KEY_PROMOTEDEVENT_PROMOTEDCATURLFORICONIMAGE="promotedCatURLForIconImage";
	
	/* From table SRPromotedEventsDetails */
	public static final String KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSID="promotedEventsDetailsID";
	public static final String KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSTITLE="promotedEventsDetailsTitle";
	public static final String KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSDESCRIPTION="promotedEventsDetailsDescription";
	public static final String KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSURLDOCDOWNLOAD="promotedEventsDetailsURLDocDownload";
	public static final String KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSADDRESS="promotedEventsDetailsAddress";
	public static final String KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSTELEPHONE="promotedEventsDetailsTelephone";
	public static final String KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSWEBSITE="promotedEventsDetailsWebsite";
	// omitted as duplicates: (fk's) */
	//promtedEventDetailsCategoryNumber
	//promotedEventID_FK

	@Override
	public boolean isDataExpired() {
		if(GlobalState.TheItemUpdate==null) {
			return true;
		}
		Date dateDatabaseAtSunriverLastUpdated=GlobalState.TheItemUpdate.getPromotedEvent().getTime();
		Date lastTimeWeveFetchedData=getLastDateRead();
		return (
				GlobalState.TheItemUpdate==null || lastTimeWeveFetchedData==null || GlobalState.TheItemUpdate.getPromotedEvent()==null ||
						dateDatabaseAtSunriverLastUpdated.after(lastTimeWeveFetchedData)
		);	
	}

	@Override
	protected String getTableName() {
		return DATABASE_TABLE_PROMOTEDEVENT;
	}

	@Override
	protected String getDateLastUpdatedKey() {
		return DATE_LAST_UPDATED;
	}

	@Override
	protected void loadWriteItemToDatabaseContentValuesTo(ContentValues values) {
		values.put(KEY_PROMOTEDEVENT_PROMOTEDEVENTSID, getPromotedEventsID());
		values.put(KEY_PROMOTEDEVENT_PROMOTEDEVENTSNAME, getPromotedEventsName());
		values.put(KEY_PROMOTEDEVENT_ISONPROMOTEDEVENTS, getIsOnPromotedEvents());
		values.put(KEY_PROMOTEDEVENT_PROMOTEDEVENTPICTUREURL, getPromotedEventPictureURL());
		values.put(KEY_PROMOTEDEVENT_PROMOTEDCATID, getPromotedCatID());
		values.put(KEY_PROMOTEDEVENT_PROMOTEDCATNAME, getPromotedCatName());
		values.put(KEY_PROMOTEDEVENT_PROMOTEDCATSORTORDER, getPromotedCatSortOrder());
		values.put(KEY_PROMOTEDEVENT_PROMOTEDCATURLFORICONIMAGE, getPromotedCatURLForIconImage());
		values.put(KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSID, getPromotedEventsDetailsID());
		values.put(KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSTITLE, getPromotedEventsDetailsTitle());
		values.put(KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSDESCRIPTION, getPromotedEventsDetailsDescription());
		values.put(KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSURLDOCDOWNLOAD, getPromotedEventsDetailsURLDocDownload());
		values.put(KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSADDRESS, getPromotedEventsDetailsAddress());
		values.put(KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSTELEPHONE, getPromotedEventsDetailsTelephone());
		values.put(KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSWEBSITE, getPromotedEventsDetailsWebsite());
	}

	@Override
	protected String[] getProjectionForFetch() {
		String[] projection = {KEY_PROMOTEDEVENT_PROMOTEDEVENTSID, KEY_PROMOTEDEVENT_PROMOTEDEVENTSNAME, KEY_PROMOTEDEVENT_ISONPROMOTEDEVENTS, KEY_PROMOTEDEVENT_PROMOTEDEVENTPICTUREURL,
				KEY_PROMOTEDEVENT_PROMOTEDCATID,KEY_PROMOTEDEVENT_PROMOTEDCATNAME,KEY_PROMOTEDEVENT_PROMOTEDCATSORTORDER,KEY_PROMOTEDEVENT_PROMOTEDCATURLFORICONIMAGE,
				KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSID,KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSTITLE,KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSDESCRIPTION,
					KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSURLDOCDOWNLOAD,KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSADDRESS,
					KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSTELEPHONE,KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSWEBSITE};
		return projection;
	}

	public ItemPromotedEvent() {
		
	}
	protected ItemPromotedEvent(Cursor cursor) {
		setPromotedEventsID(cursor.getInt(cursor.getColumnIndex(KEY_PROMOTEDEVENT_PROMOTEDEVENTSID)));
		setPromotedEventsName(cursor.getString(cursor.getColumnIndex(KEY_PROMOTEDEVENT_PROMOTEDEVENTSNAME)));
		setIsOnPromotedEvents(cursor.getInt(cursor.getColumnIndex(KEY_PROMOTEDEVENT_ISONPROMOTEDEVENTS))==1);
		setPromotedEventPictureURL(cursor.getString(cursor.getColumnIndex(KEY_PROMOTEDEVENT_PROMOTEDEVENTPICTUREURL)));
		setPromotedCatID(cursor.getInt(cursor.getColumnIndex(KEY_PROMOTEDEVENT_PROMOTEDCATID)));
		setPromotedCatSortOrder(cursor.getInt(cursor.getColumnIndex(KEY_PROMOTEDEVENT_PROMOTEDCATNAME)));
		setPromotedCatURLForIconImage(cursor.getString(cursor.getColumnIndex(KEY_PROMOTEDEVENT_PROMOTEDCATURLFORICONIMAGE)));
		setPromotedEventsDetailsID(cursor.getInt(cursor.getColumnIndex(KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSID)));
		setPromotedEventsDetailsTitle(cursor.getString(cursor.getColumnIndex(KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSTITLE)));
		setPromotedEventsDetailsDescription(cursor.getString(cursor.getColumnIndex(KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSDESCRIPTION)));
		setPromotedEventsDetailsURLDocDownload(cursor.getString(cursor.getColumnIndex(KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSURLDOCDOWNLOAD)));
		setPromotedEventsDetailsAddress(cursor.getString(cursor.getColumnIndex(KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSADDRESS)));
		setPromotedEventsDetailsTelephone(cursor.getString(cursor.getColumnIndex(KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSTELEPHONE)));
		setPromotedEventsDetailsWebsite(cursor.getString(cursor.getColumnIndex(KEY_PROMOTEDEVENT_PROMOTEDEVENTSDETAILSWEBSITE)));		
	}
	
	@Override
	protected SunriverDataItem itemFromCursor(Cursor cursor) {
		return new ItemPromotedEvent(cursor);
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

	public static Hashtable<Integer,ItemPromotedEventNormalized> normalize(ArrayList<Object> data) {
		Hashtable<Integer,ItemPromotedEventNormalized> penz=new Hashtable<Integer,ItemPromotedEventNormalized>();		
		for(Object promotedEventLine: data) {
			ItemPromotedEvent pe = (ItemPromotedEvent)promotedEventLine;
			ItemPromotedEventNormalized pen = penz.get(pe.promotedEventsID);
			if(pen==null) {
				pen=new ItemPromotedEventNormalized();
				pen.setCategories(new ArrayList<ItemPromotedEventCategory>());
				pen.setIsOnPromotedEvents(pe.getIsOnPromotedEvents());
				pen.setPromotedEventPictureURL(pe.getPromotedEventPictureURL());
				pen.setPromotedEventsID(pe.getPromotedEventsID());
				pen.setPromotedEventsName(pe.getPromotedEventsName());
				pen.setPromotedEventIconURL(pe.getPromotedEventIconURL());
				penz.put(pe.promotedEventsID, pen);				
			}
			ItemPromotedEventCategory ipec=new ItemPromotedEventCategory();
			ipec.setPromotedCatID(pe.getPromotedCatID());
			ipec.setPromotedCatName(pe.getPromotedCatName());
			ipec.setPromotedCatSortOrder(pe.getPromotedCatSortOrder());
			ipec.setPromotedCatURLForIconImage(pe.getPromotedCatURLForIconImage());
			ipec.setPromotedEventDetails(new ArrayList<ItemPromotedEventDetail>());
			ItemPromotedEventCategory theIpec=pen.findCategory(ipec);
			if(theIpec==null) {				
				pen.getCategories().add(ipec);
			} else {
				ipec=theIpec;
			}
			ItemPromotedEventDetail iped = new ItemPromotedEventDetail();
			iped.setPromotedEventsDetailsAddress(pe.getPromotedEventsDetailsAddress());
			iped.setPromotedEventsDetailsDescription(pe.getPromotedEventsDetailsDescription());
			iped.setPromotedEventsDetailsID(pe.getPromotedEventsDetailsID());
			iped.setPromotedEventsDetailsTelephone(pe.getPromotedEventsDetailsTelephone());
			iped.setPromotedEventsDetailsTitle(pe.getPromotedEventsDetailsTitle());
			iped.setPromotedEventsDetailsURLDocDownload(pe.getPromotedEventsDetailsURLDocDownload());
			iped.setPromotedEventsDetailsWebsite(pe.getPromotedEventsDetailsWebsite());
			iped.setPromotedEventDetailOrder(pe.getPromotedEventDetailOrder());
			iped.setPromotedEventsDetailIconURL(pe.getPromotedEventDetailIconURL());
			ipec.addDetailItem(iped);
		}
		Enumeration<ItemPromotedEventNormalized> eipen=penz.elements();
		while(eipen.hasMoreElements()) {
			ItemPromotedEventNormalized ipen=eipen.nextElement();
			ipen.getCategories();
			Collections.sort(ipen.getCategories(),new Comparator<ItemPromotedEventCategory>() {

				@Override
				public int compare(ItemPromotedEventCategory lhs,
						ItemPromotedEventCategory rhs) {
					if(lhs.getPromotedCatSortOrder()>rhs.getPromotedCatSortOrder()) {
						return 1;
					} else {
						if(lhs.getPromotedCatSortOrder()<rhs.getPromotedCatSortOrder()) {
							return -1;
						} else {
							return 0;
						}
					}
				}

			});
			for(ItemPromotedEventCategory ipec: ipen.getCategories()) {
				Collections.sort(ipec.getPromotedEventDetails(), new Comparator<ItemPromotedEventDetail>() {

					@Override
					public int compare(ItemPromotedEventDetail lhs,	ItemPromotedEventDetail rhs) {
						if(lhs.getPromotedEventDetailOrder()>lhs.getPromotedEventDetailOrder()) {
							return 1;
						} else {
							if(lhs.getPromotedEventDetailOrder()<lhs.getPromotedEventDetailOrder()) {
								return -1;
							} else {
								return 0;
							}
						}
					}
					
				});
			}
		}
		ArrayList<ItemPromotedEventNormalized> aL = Collections.list(penz.elements());
		Hashtable<Integer,ItemPromotedEventNormalized> ht=new Hashtable<Integer,ItemPromotedEventNormalized>();
		for(ItemPromotedEventNormalized ipen : aL) {
			ht.put(ipen.getPromotedEventsID(), ipen);
		}
		return ht;
	}

}
