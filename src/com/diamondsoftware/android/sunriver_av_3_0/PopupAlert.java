package com.diamondsoftware.android.sunriver_av_3_0;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;

public class PopupAlert extends Popups2 {
	private ItemAlert mItemAlert;
	
	public PopupAlert(Activity activity, ItemAlert itemAlert) {
		super(activity);
		mItemAlert=itemAlert;
	}

	@Override
	protected void childPerformCloseActions() {
	}

	@Override
	protected void loadView(ViewGroup popup) {
		((TextView)popup.findViewById(R.id.alertTitle)).setText(mItemAlert.getmALTitle());
		((TextView)popup.findViewById(R.id.alertDescription)).setText(mItemAlert.getmALDescription());
	}

	@Override
	protected int getResourceId() {
		return R.layout.alert;
	}

	@Override
	protected boolean getDoesPopupNeedToRunInBackground() {
		return false;
	}

	@Override
	protected int getClosePopupId() {
		return R.id.alertCloseButton;
	}

}
