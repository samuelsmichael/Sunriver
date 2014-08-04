package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.location.Geofence;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class TimerService extends Service  implements DataGetter, WaitingForDataAcquiredAsynchronously {
	private Timer mTimer=null;
	private SharedPreferences mSharedPreferences=null;

	private void doS() {
		Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandlerTimer(
				this));
		Logger logger=new Logger(0,"EmergencyTimer",this);
		logger.log("Timer popped",999);
		new AcquireDataRemotelyAsynchronously("emergency",this,this);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandlerTimer(
				this));

		String defaultValue=getResources().getString(R.string.emergencytimerintervalinseconds);
		mSharedPreferences=getSharedPreferences(getPackageName() + "_preferences", Activity.MODE_PRIVATE);
		long interval=Long.valueOf(defaultValue);

		startTimer2(1000*interval,1000*interval);
		return START_STICKY;
	}		

	@Override
	public void onDestroy() {
    	stopTimer2();		
	}
	
	private void stopTimer2() {
		if (mTimer != null) {
			try {
				mTimer.cancel();
				mTimer.purge();
			} catch (Exception e) {
			}
			mTimer = null;
		}
	}	
	private void startTimer2(long trigger, long interval) {
		getTimer().schedule(new TimerTask() {
			public void run() {
				try {
					doS();

				} catch (Exception ee) {
					
				}
			}
		}, trigger, interval);
	}

	private Timer getTimer() {
		if (mTimer == null) {
			mTimer = new Timer("SunriverEmergencyAlert");
		}
		return mTimer;
	}

	public TimerService() {		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void gotMyData(String name, ArrayList<Object> data) {
		if(name.equalsIgnoreCase("emergency")) {
			String comma="";
			StringBuilder sb=new StringBuilder();
			ArrayList<Object> liveEmergencies=new ArrayList<Object>();
			if(data!=null && data.size()>0) {
				for(Object itemEmergency: data) {
					if(((ItemEmergency)itemEmergency).isEmergencyAlert()) {
						liveEmergencies.add(itemEmergency);
						// we're going to keep track of emergencies so that if it changes (in TimerService), we'll know whether or not to do a notification
						sb.append(comma+((ItemEmergency)itemEmergency).getEmergencyId());
						comma=",";
					}
				}
				if(liveEmergencies.size()>0) {
					String emergenciesFromLastFetch=mSharedPreferences.getString("EmergenciesFromLastFetch", "");
					String current=sb.toString();
					if(!emergenciesFromLastFetch.equals(current)) {
						for(Object itemEmergency: liveEmergencies) {
							sendNotification(
									((ItemEmergency)itemEmergency).getEmergencyTitle(),
									((ItemEmergency)itemEmergency).getEmergencyDescription(),
									((ItemEmergency)itemEmergency).getEmergencyId()
									);	
						}
					}
					SharedPreferences.Editor editor = mSharedPreferences.edit();
					editor.putString("EmergenciesFromLastFetch", sb.toString());
					editor.commit();
				}
			}
		}
	}
	
	private void sendNotification(String emergencyTitle, String emergencyDescription, int idOfEmergency) {

        // Create an explicit content Intent that starts the main Activity
        Intent notificationIntent =
                new Intent(this,SplashPage.class); 

        // Construct a task stack
        android.support.v4.app.TaskStackBuilder stackBuilder = android.support.v4.app.TaskStackBuilder.create(this);

        // Adds the main Activity to the task stack as the parent
        stackBuilder.addParentStack(SplashPage.class);

        // Push the content Intent onto the stack
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Set the notification contents
        String buildingContentText="Click notification to see more details";
        if(emergencyTitle!=null && emergencyTitle!="") {
        	buildingContentText+=" for";
        }
        
        builder.setSmallIcon(R.drawable.ic_launcher)
               .setContentTitle(this.getString(R.string.emergencynotificationtitle))
               .setContentText(buildingContentText)
               .setContentIntent(notificationPendingIntent);
        if(emergencyDescription!=null && emergencyDescription!="") {
        	builder.setSubText(emergencyDescription);
        }


        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
            (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

       	mNotificationManager.notify(idOfEmergency, builder.build());
    }

	
	

	@Override
	public ArrayList<Object> getRemoteData(String name) {
		ArrayList<Object> data=null;
		if(name.equalsIgnoreCase("emergency")) {
			try {
				String defaultValue=getResources().getString(R.string.urlemergencyjson);
				String uri=GlobalState.sharedPreferences.getString("urlemergencyjson", defaultValue);
				
				data = new JsonReaderFromRemotelyAcquiredJson(
					new ParsesJsonEmergency(), 
					uri).parse();
				return data;
			} catch (Exception e) {
				int bkhere=3;
				int bkthere=bkhere;
			} finally {
			}
		} else {
		}
		return data;
	}

}
