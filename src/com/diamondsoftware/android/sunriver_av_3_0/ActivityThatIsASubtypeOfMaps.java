package com.diamondsoftware.android.sunriver_av_3_0;

import java.util.ArrayList;

import android.view.View;
import android.widget.AdapterView;

public abstract class ActivityThatIsASubtypeOfMaps extends AbstractActivityForListViews {
	protected ListViewAdapter mListViewAdapter;


	@Override
	protected int getListViewId() {
		return R.id.list;
	}

	@Override
	protected int getViewId() {
		return R.layout.activity_eatsandtreats;
	}

	/*
	 * (non-Javadoc)
	 * @see com.diamondsoftware.android.sunriver_av_3_0.AbstractActivityForListViews#childOnItemClick(android.widget.AdapterView, android.view.View, int, long)
	 * 
	 * When an EatsAndTreats item is clicked, a popup (class Popups2) is created.
	 */
	@Override
	protected void childOnItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		ItemLocation itemLocation=(ItemLocation)mListViewAdapter.mData.get(position);
		Popups2 mPopup = new PopupMapLocation(this, itemLocation.toHashMap(),true);
		mPopup.createPopup();
	}

	@Override
	protected void childOnCreate() {

	}

	@Override
	protected int getImageId() {
		return R.id.activity_eatsandtreats_image;
	}

	@Override
	protected String getImageURL() {
		return getRandomImageURL();
	}

	@Override
	protected void hookDoSomethingWithTheDataIfYouWant(ArrayList<Object> data) {
	}

}
