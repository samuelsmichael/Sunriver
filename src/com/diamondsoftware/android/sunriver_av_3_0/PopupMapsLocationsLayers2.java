package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.Map;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class PopupMapsLocationsLayers2 extends Popups2 {
	private CheckBox restaurants = null;
	private CheckBox retail = null;
	private CheckBox pools = null;
	private CheckBox tennisCourts = null;
	private CheckBox gas = (CheckBox) null;
	private CheckBox perfectPictureSpots = null;
	private CheckBox bikePaths = null;

	public PopupMapsLocationsLayers2(Activity activity, Map attributes) {
		super(activity);
	}

	@Override
	protected void childPerformCloseActions() {
		Editor edit=mSharedPreferences.edit();
		edit.putBoolean(MainActivity.PREFERENCES_MAPS_POPUP_RESTAURANTS, restaurants.isChecked());
		edit.putBoolean(MainActivity.PREFERENCES_MAPS_POPUP_RETAIL, retail.isChecked());
		edit.putBoolean(MainActivity.PREFERENCES_MAPS_POPUP_POOLS, pools.isChecked());
		edit.putBoolean(MainActivity.PREFERENCES_MAPS_POPUP_TENNISCOURTS, tennisCourts.isChecked());
		edit.putBoolean(MainActivity.PREFERENCES_MAPS_POPUP_GAS, gas.isChecked());
		edit.putBoolean(MainActivity.PREFERENCES_MAPS_POPUP_PERFECTPICTURESPOTS, perfectPictureSpots.isChecked());
		edit.putBoolean(MainActivity.PREFERENCES_MAPS_POPUP_BIKEPATHS, bikePaths.isChecked());
		edit.commit();
        // Get tracker.
        Tracker t = ((GlobalState) mActivity.getApplication()).getTracker(
            GlobalState.TrackerName.APP_TRACKER);
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
            .setCategory("Popup")
            .setAction("Close Maps menu popup")
            .setLabel("Post-popup Layers chosen: "+toString())
            .build());

	}	
	@Override
	protected void loadView(ViewGroup popup) {
		restaurants = (CheckBox) popup.findViewById(R.id.checkBoxRestaurants);
		retail = (CheckBox) popup.findViewById(R.id.checkBoxRetail);
		pools = (CheckBox) popup.findViewById(R.id.checkBoxPools);
		tennisCourts = (CheckBox) popup.findViewById(R.id.checkBoxTennisCourts);
		gas = (CheckBox) popup.findViewById(R.id.checkBoxGas);
		perfectPictureSpots = (CheckBox) popup.findViewById(R.id.checkBoxPerfectPictureSpots);
		restaurants.setChecked(mSharedPreferences.getBoolean(MainActivity.PREFERENCES_MAPS_POPUP_RESTAURANTS, true));
		bikePaths=(CheckBox)popup.findViewById(R.id.checkBoxBikePaths);
		retail.setChecked(mSharedPreferences.getBoolean(MainActivity.PREFERENCES_MAPS_POPUP_RETAIL, true));
		pools.setChecked(mSharedPreferences.getBoolean(MainActivity.PREFERENCES_MAPS_POPUP_POOLS, false));
		tennisCourts.setChecked(mSharedPreferences.getBoolean(MainActivity.PREFERENCES_MAPS_POPUP_TENNISCOURTS, false));
		gas.setChecked(mSharedPreferences.getBoolean(MainActivity.PREFERENCES_MAPS_POPUP_GAS, false));
		perfectPictureSpots.setChecked(mSharedPreferences.getBoolean(MainActivity.PREFERENCES_MAPS_POPUP_PERFECTPICTURESPOTS, false));
		bikePaths.setChecked(mSharedPreferences.getBoolean(MainActivity.PREFERENCES_MAPS_POPUP_BIKEPATHS, false));
		/* Make the checkboxes gray */
		int id = Resources.getSystem().getIdentifier("btn_check_holo_dark", "drawable", "android");
		restaurants.setButtonDrawable(id);
		retail.setButtonDrawable(id);
		pools.setButtonDrawable(id);
		tennisCourts.setButtonDrawable(id);
		gas.setButtonDrawable(id);
		perfectPictureSpots.setButtonDrawable(id);
		bikePaths.setButtonDrawable(id);
	}
	
	@Override
	protected String getGoogleAnalyticsCategory() {
		return "Popup";
	}


	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		String delimiter="";
		if(mSharedPreferences.getBoolean(MainActivity.PREFERENCES_MAPS_POPUP_RESTAURANTS,true)) {
			sb.append(delimiter+"Restaurants");
			delimiter=",";
		}
		if(mSharedPreferences.getBoolean(MainActivity.PREFERENCES_MAPS_POPUP_RETAIL,true)) {
			sb.append(delimiter+"Shopping");
			delimiter=",";
		}
		if(mSharedPreferences.getBoolean(MainActivity.PREFERENCES_MAPS_POPUP_POOLS,true)) {
			sb.append(delimiter+"Pools");
			delimiter=",";
		}
		if(mSharedPreferences.getBoolean(MainActivity.PREFERENCES_MAPS_POPUP_TENNISCOURTS,true)) {
			sb.append(delimiter+"Tennis Courts");
			delimiter=",";
		}
		if(mSharedPreferences.getBoolean(MainActivity.PREFERENCES_MAPS_POPUP_GAS,true)) {
			sb.append(delimiter+"Gas");
			delimiter=",";
		}
		if(mSharedPreferences.getBoolean(MainActivity.PREFERENCES_MAPS_POPUP_PERFECTPICTURESPOTS,true)) {
			sb.append(delimiter+"Perfect picture spots");
			delimiter=",";
		}
		if(mSharedPreferences.getBoolean(MainActivity.PREFERENCES_MAPS_POPUP_BIKEPATHS,true)) {
			sb.append(delimiter+"Bike paths");
			delimiter=",";
		}
		return sb.toString();
	}
	
	@Override
	protected int getResourceId() {
		return R.layout.maps_layers_popup;
	}

	@Override
	protected boolean getDoesPopupNeedToRunInBackground() {
		return true;
	}
	@Override
	protected int getClosePopupId() {
		return R.id.closePopup;
	}

	@Override
	protected String getGoogleAnalyticsAction() {
		return "Maps Menu popup";
	}

	@Override
	protected String getGoogleAnalyticsLabel() {
		return "Pre-popup Layers chosen: "+toString();
	}
}
