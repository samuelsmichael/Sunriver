package com.diamondsoftware.android.sunriver_av_3_0;

import android.app.Activity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.ViewGroup;
import android.widget.TextView;

public class PopupPromotedEventDownloadNotification extends Popups2 {
	private String mTitle;
	private String mDescription;
	public PopupPromotedEventDownloadNotification(Activity activity, String title, String description) {
		super(activity);
		mTitle=title;
		mDescription=description;
	}

	@Override
	protected void childPerformCloseActions() {
	}

	@Override
	protected void loadView(ViewGroup popup) {
		TextView description = (TextView)popup.findViewById(R.id.promotedEventDownloadNotificationDescription);
		description.setText(mDescription);
		((TextView)popup.findViewById(R.id.promotedEventDownloadNotificationTitle)).setText(mTitle);
	}

	@Override
	protected int getResourceId() {
		return R.layout.promoted_event_download_complete;
	}

	@Override
	protected int getClosePopupId() {
		return R.id.promotedEventDownloadNotificationCloseButton;
	}

	@Override
	protected boolean getDoesPopupNeedToRunInBackground() {
		return false;
	}

	@Override
	protected String getGoogleAnalyticsAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getGoogleAnalyticsLabel() {
		// TODO Auto-generated method stub
		return null;
	}

}
