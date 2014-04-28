package com.diamondsoftware.android.sunriver_av_3_0;



import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import com.diamondsoftware.android.sunriver_av_3_0.ParsesXMLMapLocations.LocationType;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
/**
 * Manages the maps graphics data for the Current Location.  Uses Google's Play Services location api to set up a
 * location listener; and requests updates every UPDATE_INTERVAL_IN_SECONDS seconds.  If the location changes, then the 
 * graphics item is removed and then re-added with the new location.
 * @author Diamond
 *
 */
public class MapsGraphicsLayerCurrentLocation extends MapsGraphicsLayer  implements
	GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener,
    LocationListener {

   // Global constants
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 8319;
	public final static String POINT_LATITUDE_KEY_ON_OPENING_MAPS="point_latitude_key_ON_OPENING_MAPS";
	public final static String POINT_LONGITUDE_KEY_ON_OPENING_MAPS="point_longitude_key_ON_OPENING_MAPS";
	private LocationClient mLocationClient=null;
    private Location mCurrentLocation=null;
    private int mCurrentGraphicsItem=-1;
    private LocationRequest mLocationRequest;
    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
       

	
	public MapsGraphicsLayerCurrentLocation(Activity activity, MapView mapView,
			int color, int size, STYLE style, LocationType locationType,
			boolean updateGraphics) {
		super(activity, mapView, color, size, style, locationType,
				updateGraphics, null);
		mLocationClient= new LocationClient(mActivity, this, this);
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		
	}

	@Override
	public void constructGraphicItems() {
		if(mLocationClient!=null) {
			mLocationClient.connect();
		}
	}

	@Override
	public boolean doesUserWantMeVisible() {
		return true;
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
	}

	@Override
	public void onConnected(Bundle arg0) {
	    mCurrentLocation = mLocationClient.getLastLocation();
	    if(mCurrentLocation!=null) {
	    	// This is to make it so I can test in Denver, but it looks like I'm in Sunriver.
			if(mCurrentLocation.getLongitude()>-110) {
				mCurrentLocation.setLongitude(-121.438544);
				mCurrentLocation.setLatitude(43.88481);
			}	    
		    
	    	redrawGraphics();
	    }
	}

	// This is called when we've got a new location;
	private void redrawGraphics() {
		Point pnt2=new Point();
		pnt2.setXY(mCurrentLocation.getLongitude(),mCurrentLocation.getLatitude());
		Point[] pointInArray = new Point[1];
		pointInArray[0]=pnt2;
		if(mUpdateGraphics) {
			new PushLocationData2().execute(pointInArray);
		}
	}
	public class PushLocationData2 extends AsyncTask<Point, Void, Boolean> {
		private Point mPoint;
		private Point mPointGoogle;
		
		protected void onPostExecute(Boolean isFirstTime) {
			if(mUpdateGraphics) {
				((Maps)mActivity).hookCalledWhenGraphicsLayerIsUpdated(MapsGraphicsLayerCurrentLocation.this);
				if(isFirstTime.booleanValue()) {
					Editor editor=MainActivity.mSingleton.mSharedPreferences.edit();
					editor.putString(POINT_LATITUDE_KEY_ON_OPENING_MAPS, String.valueOf(mPointGoogle.getY()));
					editor.putString(POINT_LONGITUDE_KEY_ON_OPENING_MAPS, String.valueOf(mPointGoogle.getX()));
					editor.commit();
					((Maps)mActivity).centerMapAt(mPoint);
					if(mLocationClient.isConnected()) {
						mLocationClient.requestLocationUpdates(mLocationRequest, MapsGraphicsLayerCurrentLocation.this);
					}
				}
			}
		}

		protected Boolean doInBackground(Point... params) {
			Boolean isFirstTime=false;
			if(mGraphicsLayer!=null && (mGraphicsLayer.getGraphicIDs() ==null || mGraphicsLayer.getGraphicIDs().length==0)) {
				isFirstTime=true;
			}
			mPointGoogle=params[0];
			mPoint=Utils.ToWebMercator(mPointGoogle);

			if(mCurrentGraphicsItem!=-1) {
				mGraphicsLayer.removeGraphic(mCurrentGraphicsItem);
			} else {
				isFirstTime=true;
			}
			mCurrentGraphicsItem=addGraphicsItem(mPoint,null);
			return isFirstTime;
		}
	}
	@Override
	public void onDisconnected() {
	}

	@Override
	public void onStart() {
		if(mLocationClient!=null) {
			mLocationClient.connect();
		}
		mGraphicsLayer.removeAll();
		mCurrentGraphicsItem=-1;
	}

	@Override
	public void onStop() {
		mGraphicsLayer.removeAll();
		mCurrentGraphicsItem=-1;
		if(mLocationClient!=null) {
			if(mLocationClient.isConnected()) {
				mLocationClient.removeLocationUpdates(this);
				mLocationClient.disconnect();
			}
		}
	}

	@Override
	public void onLocationChanged(Location arg0) {
		if(arg0!=null) {
			/*
			 * So I don't keep flipping between sunriver and my Denver
			 */
			if(arg0.getLongitude()>-110) {
				arg0.setLongitude(-121.438544);
				arg0.setLatitude(43.88481);
			}
			mCurrentLocation=arg0;
			redrawGraphics();
		}		
	}

}
