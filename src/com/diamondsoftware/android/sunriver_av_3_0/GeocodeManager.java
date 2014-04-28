package com.diamondsoftware.android.sunriver_av_3_0;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;



import com.diamondsoftware.android.sunriver_av_3_0.GeofenceUtils.REMOVE_TYPE;
import com.diamondsoftware.android.sunriver_av_3_0.GeofenceUtils.REQUEST_TYPE;
import com.esri.core.geometry.Point;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class GeocodeManager {
    public static boolean _CurrentlyPoppedUp=false;

	private MainActivity mMainActivity;
    /*
     * Use to set an expiration time for a geofence. After this amount
     * of time Location Services will stop tracking the geofence.
     * Remember to unregister a geofence when you're finished with it.
     * Otherwise, your app will use up battery. To continue monitoring
     * a geofence indefinitely, set the expiration time to
     * Geofence#NEVER_EXPIRE.
     */
    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    private static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * DateUtils.HOUR_IN_MILLIS;	
    // Store the current request
    private REQUEST_TYPE mRequestType;

    // Store the current type of removal
    private REMOVE_TYPE mRemoveType;

    // Persistent storage for geofences
    private SimpleGeofenceStore mPrefs;

    // Store a list of geofences to add
    List<Geofence> mCurrentGeofences;

    public List<Geofence> getCurrentGeofences() {
		return mCurrentGeofences;
	}

	public REMOVE_TYPE getRemoveType() {
		return mRemoveType;
	}


	public REQUEST_TYPE getRequestType() {
		return mRequestType;
	}

	public GeofenceRequester getGeofenceRequester() {
		return mGeofenceRequester;
	}

	public GeofenceRemover getGeofenceRemover() {
		return mGeofenceRemover;
	}


	public List<String> getGeofenceIdsToRemove() {
		return mGeofenceIdsToRemove;
	}

	public GeofenceSampleReceiver getBroadcastReceiver() {
		return mBroadcastReceiver;
	}


	public IntentFilter getIntentFilter() {
		return mIntentFilter;
	}

	// Add geofences handler
    private GeofenceRequester mGeofenceRequester;
    // Remove geofences handler
    private GeofenceRemover mGeofenceRemover;
    /*
     * An instance of an inner class that receives broadcasts from listeners and from the
     * IntentService that receives geofence transition events
     */
    private GeofenceSampleReceiver mBroadcastReceiver;

    // An intent filter for the broadcast receiver
    private IntentFilter mIntentFilter;

    // Store the list of geofences to remove
    private List<String> mGeofenceIdsToRemove;

	
	public void onCreate() {
        // Create a new broadcast receiver to receive updates from the listeners and service
		if(mBroadcastReceiver!=null) { // don't want duplicate messages
			LocalBroadcastManager.getInstance(mMainActivity).unregisterReceiver(mBroadcastReceiver);
		}
        mBroadcastReceiver = new GeofenceSampleReceiver();

        // Create an intent filter for the broadcast receiver
        mIntentFilter = new IntentFilter();
        // Action for Transitions
        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCE_TRANSITION);
        
        // Action for broadcast Intents that report successful addition of geofences
        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCES_ADDED);

        // Action for broadcast Intents that report successful removal of geofences
        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCES_REMOVED);

        // Action for broadcast Intents containing various types of geofencing errors
        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCE_ERROR);

        // All Location Services sample apps use this category
        mIntentFilter.addCategory(GeofenceUtils.CATEGORY_LOCATION_SERVICES);

        // Instantiate a new geofence storage area
        mPrefs = new SimpleGeofenceStore(mMainActivity);

        // Instantiate the current List of geofences
        mCurrentGeofences = new ArrayList<Geofence>();

        // Instantiate a Geofence requester
        mGeofenceRequester = new GeofenceRequester(mMainActivity);

        // Instantiate a Geofence remover
        mGeofenceRemover = new GeofenceRemover(mMainActivity);

	}

	
	public GeocodeManager(MainActivity activity) {
		mMainActivity=activity;
		onCreate();
		
	}
	public void enableGeocode() {
        /*
         * Record the request as an ADD. If a connection error occurs,
         * the app can automatically restart the add request if Google Play services
         * can fix the error
         */
        mRequestType = GeofenceUtils.REQUEST_TYPE.ADD;
        /*
         * Check for Google Play services. Do this after
         * setting the request type. If connecting to Google Play services
         * fails, onActivityResult is eventually called, and it needs to
         * know what type of request was in progress.
         */
        if (!servicesConnected()) {

            return;
        }
        setLocations();
        // Start the request. Fail if there's already a request in progress
        try {
            // Try to add geofences
            mGeofenceRequester.addGeofences(mCurrentGeofences);
        } catch (UnsupportedOperationException e) {

        }

	}
	
	
	public void disableGeocode() {
		
	}


    /**
     * Verify that Google Play services is available before making a request. Click notification
     *
     * @return true if Google Play services is available, otherwise false
     */
    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(mMainActivity);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {

            // In debug mode, log the status
            Log.d(GeofenceUtils.APPTAG, mMainActivity.getString(R.string.play_services_available));

            // Continue
            return true;

        // Google Play services was not available for some reason
        } else {

            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, mMainActivity, 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                errorFragment.show(mMainActivity.getFragmentManager(), GeofenceUtils.APPTAG);
            }
            return false;
        }
    }

    /**
     * Called when the user clicks the "Remove geofences" button
     *
     * @param view The view that triggered this callback
     */
    public void onUnregisterByPendingIntentClicked(View view) {
        /*
         * Remove all geofences set by this app. To do this, get the
         * PendingIntent that was added when the geofences were added
         * and use it as an argument to removeGeofences(). The removal
         * happens asynchronously; Location Services calls
         * onRemoveGeofencesByPendingIntentResult() (implemented in
         * the current Activity) when the removal is done
         */

        /*
         * Record the removal as remove by Intent. If a connection error occurs,
         * the app can automatically restart the removal if Google Play services
         * can fix the error
         */
        // Record the type of removal
        mRemoveType = GeofenceUtils.REMOVE_TYPE.INTENT;

        /*
         * Check for Google Play services. Do this after
         * setting the request type. If connecting to Google Play services
         * fails, onActivityResult is eventually called, and it needs to
         * know what type of request was in progress.
         */
        if (!servicesConnected()) {

            return;
        }

        // Try to make a removal request
        try {
        /*
         * Remove the geofences represented by the currently-active PendingIntent. If the
         * PendingIntent was removed for some reason, re-create it; since it's always
         * created with FLAG_UPDATE_CURRENT, an identical PendingIntent is always created.
         */
        mGeofenceRemover.removeGeofencesByIntent(mGeofenceRequester.getRequestPendingIntent());

        } catch (UnsupportedOperationException e) {
            // Notify user that previous request hasn't finished.
            Toast.makeText(mMainActivity, R.string.remove_geofences_already_requested_error,
                        Toast.LENGTH_LONG).show();
        }

    }
	
	
	
	public void setLocations() {

        /*
         * Create a version of geofence 2 that is "flattened" into individual fields. This
         * allows it to be stored in SharedPreferences.
         */
        SimpleGeofence sg2 = new SimpleGeofence(
            "99992",
            // Get latitude, longitude, and radius from the UI
            Double.valueOf(39.733413),
            Double.valueOf(-105.08113),
            Float.valueOf(200),
            // Set the expiration time
            GEOFENCE_EXPIRATION_IN_MILLISECONDS,
            // Detect both entry and exit transitions
            Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT,
            "Pet World" 
            );

        // Store this flat version in SharedPreferences
        mPrefs.setGeofence("99992", sg2);

        /*
         * Add Geofence objects to a List. toGeofence()
         * creates a Location Services Geofence object from a
         * flat object
         */
        mCurrentGeofences.add(sg2.toGeofence());
        
        
        // Load the data fetched from the uri

		for(Hashtable ht :MainActivity.LocationData) {
			for (Object al: ht.values()) {
				ArrayList<Object> aroo = (ArrayList<Object>)al;
				for (Object theElement :aroo) {
					ItemLocation location=(ItemLocation)theElement;
					double latitude=Double.valueOf(location.getmGoogleCoordinates().getY());
					double longitude=Double.valueOf(location.getmGoogleCoordinates().getX());
			        SimpleGeofence sg = new SimpleGeofence(
			                String.valueOf(location.getmId()),
			                // Get latitude, longitude, and radius from the UI
			                latitude,
			                longitude,
			                Float.valueOf(200),
			                // Set the expiration time
			                GEOFENCE_EXPIRATION_IN_MILLISECONDS,
			                // Detect both entry and exit transitions
			                Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT,
			                location.getmName()
			                );

		            // Store this flat version in SharedPreferences
		            mPrefs.setGeofence(String.valueOf(location.getmId()), sg);
		            mCurrentGeofences.add(sg.toGeofence());
				}
			}
		}



    }
	
	
	
	
    /**
     * Define a Broadcast receiver that receives updates from connection listeners and
     * the geofence transition service.
     */
    public class GeofenceSampleReceiver extends BroadcastReceiver {
        /*
         * Define the required method for broadcast receivers
         * This method is invoked when a broadcast Intent triggers the receiver
         */
        @Override
        public void onReceive(Context context, Intent intent) {

            // Check the action code and determine what to do
            String action = intent.getAction();

            // Intent contains information about errors in adding or removing geofences
            if (TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCE_ERROR)) {

                handleGeofenceError(context, intent);

            // Intent contains information about successful addition or removal of geofences
            } else if (
                    TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCES_ADDED)
                    ||
                    TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCES_REMOVED)) {

                handleGeofenceStatus(context, intent);

            // Intent contains information about a geofence transition
            } else if (TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCE_TRANSITION)) {

                handleGeofenceTransition(context, intent);

            // The Intent contained an invalid action
            } else {
                Log.e(GeofenceUtils.APPTAG, mMainActivity.getString(R.string.invalid_action_detail, action));
                Toast.makeText(context, R.string.invalid_action, Toast.LENGTH_LONG).show();
            }
        }

        /**
         * If you want to display a UI message about adding or removing geofences, put it here.
         *
         * @param context A Context for this component
         * @param intent The received broadcast Intent
         */
        private void handleGeofenceStatus(Context context, Intent intent) {

        }

        /**
         * Report geofence transitions to the UI
         *
         * @param context A Context for this component
         * @param intent The Intent containing the transition
         */
        
        
        private void handleGeofenceTransition(Context context, Intent intent) {

            String ids = intent.getStringExtra(GeofenceUtils.EXTRA_GEOFENCE_ID);
            String transitionType = intent.getStringExtra(GeofenceUtils.EXTRA_GEOFENCE_TRANSITION_TYPE);
            if(transitionType.equals("Entering")) {
            	try {
	            	String[] idArray=ids.split(","); 
	            	ItemLocation theItemToPopup=mMainActivity.findFirstItemLocationWhoseIdIs(idArray[0]);
		            	if(theItemToPopup!=null) {
		            		if(theItemToPopup.ismIsGPSPopup()) {
			            		if(!_CurrentlyPoppedUp) {
			            			_CurrentlyPoppedUp=true;
			            			new PopupMapLocation(mMainActivity,theItemToPopup.toHashMap(),false).createPopup();
			            		}
		            		}
		            	}
            	} catch (Exception e) {
            	
            	}
            }
            /*
             * If you want to change the UI when a transition occurs, put the code
             * here. The current design of the app uses a notification to inform the
             * user that a transition has occurred.
             */
        }

        /**
         * Report addition or removal errors to the UI, using a Toast
         *
         * @param intent A broadcast Intent sent by ReceiveTransitionsIntentService
         */
        private void handleGeofenceError(Context context, Intent intent) {
            String msg = intent.getStringExtra(GeofenceUtils.EXTRA_GEOFENCE_STATUS);
            Log.e(GeofenceUtils.APPTAG, msg);
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }
    /**
     * Define a DialogFragment to display the error dialog generated in
     * showErrorDialog.
     */
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        /**
         * Default constructor. Sets the dialog field to null
         */
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        /**
         * Set the dialog to display
         *
         * @param dialog An error dialog
         */
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        /*
         * This method must return a Dialog to the DialogFragment.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
	
}

