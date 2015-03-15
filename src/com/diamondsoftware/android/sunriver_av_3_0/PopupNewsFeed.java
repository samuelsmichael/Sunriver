package com.diamondsoftware.android.sunriver_av_3_0;

import android.app.Activity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.ViewGroup;
import android.widget.TextView;

public class PopupNewsFeed extends Popups2 {
	private ItemNewsFeed mItemNewsFeed;
	
	public PopupNewsFeed(Activity activity, ItemNewsFeed itemNewsFeed) {
		super(activity);
		mItemNewsFeed=itemNewsFeed;
	}

	@Override
	protected void childPerformCloseActions() {
	}

	@Override
	protected void loadView(ViewGroup popup) {
		TextView newsFeedDescription = (TextView)popup.findViewById(R.id.newsFeedDescription);
		String newsFeedDescriptionText=mItemNewsFeed.getnewsFeedDescription();
		if(newsFeedDescriptionText.indexOf("href=")!=-1) {
			newsFeedDescription.setClickable(true);
			newsFeedDescription.setMovementMethod(LinkMovementMethod.getInstance());
		}
		((TextView)popup.findViewById(R.id.newsFeedTitle)).setText(mItemNewsFeed.getnewsFeedTitle());
		newsFeedDescription.setText(Html.fromHtml(newsFeedDescriptionText));
	}

	@Override
	protected int getResourceId() {
		return R.layout.news_feed;
	}

	@Override
	protected boolean getDoesPopupNeedToRunInBackground() {
		return false;
	}

	@Override
	protected int getClosePopupId() {
		return R.id.newsFeedCloseButton;
	}

	@Override
	protected String getGoogleAnalyticsAction() {
		return "News Feed";
	}

	@Override
	protected String getGoogleAnalyticsLabel() {
		return "";
	}

}
