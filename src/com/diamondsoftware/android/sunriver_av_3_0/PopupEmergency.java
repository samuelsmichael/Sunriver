package com.diamondsoftware.android.sunriver_av_3_0;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class PopupEmergency extends Popups2 {
	private ItemEmergency mItemEmergency;
	
	public PopupEmergency(Activity activity, ItemEmergency itemEmergency) {
		super(activity);
		mItemEmergency=itemEmergency;
	}

	@Override
	protected void childPerformCloseActions() {
	}

	@Override
	protected void loadView(ViewGroup popup) {
		Button mapsButton=(Button)popup.findViewById(R.id.emergencyViewMapButton);
		mapsButton.setVisibility(View.GONE);
		if(mItemEmergency.getMapURLs()!=null && mItemEmergency.getMapURLs().size()>0) {
			mapsButton.setVisibility(View.VISIBLE);
			mapsButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String[] urls= new String[mItemEmergency.getMapURLs().size()];
					int i=0;
					for(String url: mItemEmergency.getMapURLs()) {
						urls[i]=url;
						i++;
					}
					removeView();
					Intent intent=new Intent(mActivity,Maps.class)
						.putExtra("EmergencyMapURLs",urls)
						.putExtra("EmergencyMapTitle", mItemEmergency.getEmergencyTitle());
					mActivity.startActivity(intent);
				}
			});
		}
		((TextView)popup.findViewById(R.id.emergencyTitle)).setText(mItemEmergency.getEmergencyTitle());
		((TextView)popup.findViewById(R.id.emergencyDescription)).setText(mItemEmergency.getEmergencyDescription());
	}

	@Override
	protected int getResourceId() {
		return R.layout.emergency;
	}

	@Override
	protected boolean getDoesPopupNeedToRunInBackground() {
		return false;
	}

	@Override
	protected int getClosePopupId() {
		return R.id.emergencyCloseButton;
	}

	@Override
	protected String getGoogleAnalyticsAction() {
		return "Emergency";
	}

	@Override
	protected String getGoogleAnalyticsLabel() {
		return "";
	}

}
