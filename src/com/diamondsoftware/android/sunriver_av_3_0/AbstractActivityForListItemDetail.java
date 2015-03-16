package com.diamondsoftware.android.sunriver_av_3_0;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.os.AsyncTask;
import android.os.Bundle;
public abstract class AbstractActivityForListItemDetail extends AbstractActivityForMenu {
	protected abstract String getGoogleAnalyticsAction();
	protected abstract String getGoogleAnalyticsLabel();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        // Get tracker... but it has to be done after each object has the chance to build itself
		new WaitASecThenCheckIfGPSIsOn()
		.execute(new Void[] {null});
	}
	private class WaitASecThenCheckIfGPSIsOn extends
	AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}
		protected void onPostExecute(Void result) {		
	        Tracker t = ((GlobalState) getApplication()).getTracker(
	            GlobalState.TrackerName.APP_TRACKER);
	        // Build and send an Event.
	        t.send(new HitBuilders.EventBuilder()
	            .setCategory("Item Detail")
	            .setAction(getGoogleAnalyticsAction())
	            .setLabel(getGoogleAnalyticsLabel())
	            .build());
		}
	}
}
