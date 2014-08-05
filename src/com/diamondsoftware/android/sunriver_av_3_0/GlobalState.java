package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

public class GlobalState extends Application {
	public static boolean homePageNeedsRefreshing=false;
	public static ArrayList<Object> TheItemsEmergency=null;
	public ItemAlert theItemAlert=null;
	public static ItemUpdate TheItemUpdate=null;
	public  ItemWelcome TheItemWelcome=null;
	public  ArrayList<Object> TheItemsSelfie=null;
	public  ArrayList<Object> TheItemsDidYouKnow=null;
	public  ArrayList<Object> TheItemsGISLayers=null;
	public static boolean gotInternet=false;
	public static SharedPreferences sharedPreferences=null;
	private static DbAdapter mDbAdapter=null;
	private static GlobalState mSingleton;
	private GoogleAnalytics mAnalytics;
	
	public static DbAdapter getDbAdapter() {
		if(mDbAdapter==null) {
			mDbAdapter=new DbAdapter(mSingleton.getApplicationContext());
		}
		return mDbAdapter;
	}
	
	public static ItemEmergency getEmergencyItemWhoseIdIs(int id) {
		ItemEmergency theEmergencyItem=null;
			if(TheItemsEmergency!=null) {
			for(Object emergencyItem : TheItemsEmergency) {
				if(((ItemEmergency)emergencyItem).getEmergencyId()==id) {
					theEmergencyItem=(ItemEmergency)emergencyItem;
					break;
				}
			}
		}
		return theEmergencyItem;
	}

	protected String getPREFS_NAME() {
		return getPackageName() + "_preferences";
	}
	public void dbAdapterClose() {
		if(mDbAdapter!=null) {
			mDbAdapter.close();
			mDbAdapter=null;
		}
	}
	@Override
	public void onCreate() {
		super.onCreate();
		mSingleton=this;
		sharedPreferences=getSharedPreferences(getPREFS_NAME(), Activity.MODE_PRIVATE);
    	mAnalytics= GoogleAnalytics.getInstance(this);
    	mAnalytics.enableAutoActivityReports(this);

	}
	/**
	   * Enum used to identify the tracker that needs to be used for tracking.
	   *
	   * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
	   * storing them all in Application object helps ensure that they are created only once per
	   * application instance.
	   */
	  public enum TrackerName {
	    APP_TRACKER // Tracker used only in this app.
	  }
	  synchronized Tracker getTracker(TrackerName trackerId) {
		  if (!mTrackers.containsKey(trackerId)) {

		    	Tracker t =  mAnalytics.newTracker(R.xml.app_tracker);
		    	mTrackers.put(trackerId, t);
		    }
		    return mTrackers.get(trackerId);
		  }
	  HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
	  public void gaSendView(String screenName) {
	        // Get tracker.
	        Tracker t = getTracker(TrackerName.APP_TRACKER);

	        // Set screen name.
	        // Where path is a String representing the screen name.
	        t.setScreenName(screenName);

	        // Send a screen view.
	        t.send(new HitBuilders.AppViewBuilder().build());
	  }
}
