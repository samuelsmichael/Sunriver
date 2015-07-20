package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

// This is the ACRA "crash-detection" software
@ReportsCrashes(
		formKey="dGVacG0ydVHnaNHjRjVTUTEtb3FPWGc6MQ",
        mode = ReportingInteractionMode.DIALOG,
        resToastText = R.string.crash_toast_text, // optional, displayed as soon as the crash occurs, before collecting data which can take a few seconds
        resDialogText = R.string.crash_dialog_text,
        resDialogIcon = android.R.drawable.ic_dialog_info, //optional. default is a warning sign
        resDialogTitle = R.string.crash_dialog_title, // optional. default is your application name
        resDialogCommentPrompt = R.string.crash_dialog_comment_prompt, // optional. when defined, adds a user text field input with this text resource as a label
        resDialogOkToast = R.string.crash_dialog_ok_toast2,  // optional. displays a Toast message when the user accepts to send a report.
        mailTo = "diamondsoftware222@gmail.com;MobileApp@srowners.com"	
)

public class GlobalState extends Application  {
	public static boolean homePageNeedsRefreshing=false;
	public static ArrayList<Object> TheItemsEmergency=null;
	public ItemAlert theItemAlert=null;
	public static ItemUpdate TheItemUpdate=null;
	public  ArrayList<Object> TheItemWelcomes=null;
	public  ArrayList<Object> TheItemsSelfie=null;
	public  ArrayList<Object> TheItemsDidYouKnow=null;
	public  ArrayList<Object> TheItemsGISLayers=null;
	public ArrayList<Object> TheItemsTipsHomePage=null;
	public ArrayList<Object> TheItemsEventPics=null;
	public ArrayList<Object> TheItemsPromotedEvents=null;
	public ArrayList<Object> TheItemsLane=null;
	public String[] TheItemsLaneStrings=null;
	public Hashtable<Integer,ItemPromotedEventNormalized> TheItemsPromotedEventsNormalized=null;
	public ItemNewsFeed theItemNewsFeed=null;
	public static boolean gotInternet=false;
	public static SharedPreferences sharedPreferences=null;
	private static DbAdapter mDbAdapter=null;
	public static GlobalState mSingleton;
	private GoogleAnalytics mAnalytics;
	public static String PREFERENCES_LOCATION="com.diamondsoftware.android.sunriver_av_3_0_preferences";

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

        // The following line triggers the initialization of ACRA
        ACRA.init(this);
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
