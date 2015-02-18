package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

public class ActivityTipsHomePage extends ActivityTips {

	@Override
	protected ArrayList<Object> getItems() {
		return ((GlobalState)getApplication()).TheItemsTipsHomePage;
	}

	@Override
	protected int getLayoutFragmentId() {
		return (R.layout.activity_tips_homepage_fragment);
	}

	@Override
	protected String getPreferencesKey() {
		return "show_home_page_tips";
	}

	@Override
	protected String getLastTipViewedPosition_Key() {
		return "tips_homepage_lastkeyviewedPosition_key";
	}

}
