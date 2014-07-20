package com.diamondsoftware.android.sunriver_av_3_0;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.os.Bundle;
public abstract class AbstractActivityForListItemDetail extends AbstractActivityForMenu {
	protected abstract String getGoogleAnalyticsAction();
	protected abstract String getGoogleAnalyticsLabel();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        // Get tracker.
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
