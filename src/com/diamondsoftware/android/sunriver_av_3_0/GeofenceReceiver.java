package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.List;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;


public class GeofenceReceiver extends BroadcastReceiver { //LocalBroadcastManager
    Context context;

    Intent broadcastIntent = new Intent();

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        broadcastIntent.addCategory(GeofenceUtils.CATEGORY_LOCATION_SERVICES);

        if (LocationClient.hasError(intent)) {
            handleError(intent);
        } else {
            handleEnterExit(intent);
        }
    }

    private void handleError(Intent intent){
        // Get the error code
        int errorCode = LocationClient.getErrorCode(intent);

        // Get the error message
        String errorMessage = LocationServiceErrorMessages.getErrorString(
                context, errorCode);

        // Log the error
        
        Log.e(GeofenceUtils.APPTAG,
                context.getString(R.string.geofence_transition_error_detail,
                        errorMessage));

        // Set the action and error message for the broadcast intent
        broadcastIntent
                .setAction(GeofenceUtils.ACTION_GEOFENCE_ERROR)
                .putExtra(GeofenceUtils.EXTRA_GEOFENCE_STATUS, errorMessage);

        // Broadcast the error *locally* to other components in this app
        LocalBroadcastManager.getInstance(context).sendBroadcast(
                broadcastIntent);
    }


    private void handleEnterExit(Intent intent) {
        // Get the type of transition (entry or exit)
        int transition = LocationClient.getGeofenceTransition(intent);
        if ((transition == Geofence.GEOFENCE_TRANSITION_ENTER)|| 
        		(transition == Geofence.GEOFENCE_TRANSITION_EXIT)) {

	       	String locationDescription="";
	        // Post a notification
	        List<Geofence> geofences = LocationClient
	                    .getTriggeringGeofences(intent);
	        String[] geofenceIds = new String[geofences.size()];
	        String jdId="0";
	        for (int index = 0; index < geofences.size() ; index++) {
	        	jdId=geofences.get(index).getRequestId();
	            geofenceIds[index] = jdId;
	           	/*
	        	 * If my main activity happens to still be in memory, I can fetch the name
	        	 * of the location; otherwise, I have to show a general message. I wrap it
	        	 * in a try catch, too, as we don't need the name that badly
	        	 */
	            try {
	            	if(MainActivity.mSingleton!=null) {
	            		locationDescription=MainActivity.mSingleton.findFirstItemLocationWhoseIdIs(jdId).getmName();
	            	}
	            }
	            catch (Exception ee) {}
	            if(locationDescription=="") {
	            	try {
	            		locationDescription=new SimpleGeofenceStore(context).getGeofence(jdId).getName();
	            	} catch (Exception eee) {}
	            }
	        }
	        String ids = TextUtils.join(GeofenceUtils.GEOFENCE_ID_DELIMITER,
	                geofenceIds);
	        String transitionType = GeofenceUtils
	                .getTransitionString(transition);
	        int locationId=0;
	        try {
	        	locationId=Integer.valueOf(jdId);
	        } catch (Exception ee3) {}
	        if(MainActivity.mSingleton!=null) {
		        ItemLocation theItemToNotify=MainActivity.mSingleton.findFirstItemLocationWhoseIdIs(String.valueOf(locationId));
		        if(theItemToNotify.ismIsGPSPopup()) {
		        	sendNotification(transition,locationDescription,locationId);
		        }
	        }

            // Create an Intent to broadcast to the app
            broadcastIntent
                    .setAction(GeofenceUtils.ACTION_GEOFENCE_TRANSITION)
                    .addCategory(GeofenceUtils.CATEGORY_LOCATION_SERVICES)
                    .putExtra(GeofenceUtils.EXTRA_GEOFENCE_ID, ids)
                    .putExtra(GeofenceUtils.EXTRA_GEOFENCE_TRANSITION_TYPE,
                            transitionType);

            LocalBroadcastManager.getInstance(context)
                    .sendBroadcast(broadcastIntent);

            // Log the transition type and a message
            Log.d(GeofenceUtils.APPTAG, transitionType + ": " + ids);
            Log.d(GeofenceUtils.APPTAG,
                    context.getString(R.string.geofence_transition_notification_text));

            // In debug mode, log the result
            Log.d(GeofenceUtils.APPTAG, "transition");

            // An invalid transition was reported
        } else {
            
            Log.e(GeofenceUtils.APPTAG,
                    context.getString(R.string.geofence_transition_invalid_type,
                            transition));
                            
        }
    }

    /**
     * Posts a notification in the notification bar when a transition is
     * detected. If the user clicks the notification, control goes to the main
     * Activity.
     * 
     * @param transitionType
     *            The type of transition that occurred.
     * 
     */
    
	private void sendNotification(int transition, String locationName, int theIdOfLocation) {

        // Create an explicit content Intent that starts the main Activity
        Intent notificationIntent =
                new Intent(context,MainActivity.class); // not sure about this: .putExtra("GoToMaps", true);

        // Construct a task stack
        android.support.v4.app.TaskStackBuilder stackBuilder = android.support.v4.app.TaskStackBuilder.create(context);

        // Adds the main Activity to the task stack as the parent
        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        // Set the notification contents
        String buildingContentText="Click notification to see more details";
        if(locationName!=null && locationName!="") {
        	buildingContentText+=" for";
        }
        
        builder.setSmallIcon(R.drawable.ic_launcher)
               .setContentTitle(context.getString(R.string.geofence_transition_notification_titletii))
               .setContentText(buildingContentText)
               .setContentIntent(notificationPendingIntent);
        if(locationName!=null && locationName!="") {
        	builder.setSubText(locationName);
        }


        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
        // Issue the notification
        	mNotificationManager.notify(theIdOfLocation, builder.build());
        } else {
    		if (transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
    			mNotificationManager.cancel(theIdOfLocation);
    		}
        }
    }
}